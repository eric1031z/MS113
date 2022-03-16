/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scripting;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import client.inventory.Equip;
import client.ISkill;
import client.inventory.IItem;
import client.MapleCharacter;
import client.MapleClans;

import constants.GameConstants;
import client.inventory.ItemFlag;
import client.MapleClient;
import client.MapleJob;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.SkillFactory;
import client.SkillEntry;
import client.MapleStat;
import client.inventory.IEquip;
import client.inventory.ModifyInventory;
import com.mysql.jdbc.StringUtils;
import server.MapleCarnivalParty;
import server.Randomizer;
import server.MapleInventoryManipulator;
import server.MapleShopFactory;
import server.MapleSquad;
import server.maps.MapleMap;
import server.maps.Event_DojoAgent;
import server.maps.AramiaFireWorks;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.packet.PlayerShopPacket;
import server.MapleItemInformationProvider;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import database.DatabaseConnection;
import database.DatabaseException;
import handling.channel.handler.InventoryHandler;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import handling.world.guild.MapleGuild;
import server.MapleCarnivalChallenge;
import java.util.HashMap;
import handling.world.guild.MapleGuildAlliance;
import java.awt.Image;
import java.io.File;
import static java.lang.Double.NaN;
import static java.lang.Math.random;
import java.sql.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.swing.ImageIcon;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.MapleGachapon;
import server.MapleGachaponItem;
import server.MapleStatEffect;
import server.SpeedRunner;
import server.maps.SpeedRunType;
import server.StructPotentialItem;
import server.Timer;
import server.Timer.CloneTimer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MonsterDropEntry;
import server.maps.Event_PyramidSubway;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.FilePrinter;
import tools.FileoutputUtil;
import tools.SearchGenerator;
import tools.StringUtil;
import tools.data.LittleEndianAccessor;
import tools.packet.BeansPacket;
import tools.wztosql.MonsterDropCreator;


public class NPCConversationManager extends AbstractPlayerInteraction {

    private final MapleClient c;
    private final int npc, questid, mode;
    protected String script;
    private String getText;
    private final byte type; // -1 = NPC, 0 = start quest, 1 = end quest
    private byte lastMsg = -1;
    public boolean pendingDisposal = false;
    private final Invocable iv;
    private int para ;
    private static final MapleDataProvider source = MapleDataProviderFactory.getDataProvider("Map.wz");

    public NPCConversationManager(MapleClient c, int npc, int questid, int mode, String npcscript, byte type, Invocable iv) {
        super(c);
        this.c = c;
        this.npc = npc;
        this.questid = questid;
        this.mode = mode;
        this.type = type;
        this.iv = iv;
        this.script = npcscript;
        if (c.getPlayer() != null) {
            c.getPlayer().setNpcNow(npc);
        }
    }
    //722
 
     
    public void getMobs(int itemid) {
       MapleMonsterInformationProvider mi = MapleMonsterInformationProvider.getInstance();
       final List<Integer> mobs = MapleMonsterInformationProvider.getInstance().getMobByItem(itemid);
       String text = "#d這些怪物會掉落您查詢的物品#k: \r\n\r\n";
  
       for (int i = 0; i < mobs.size(); i++) {
           int quest = 0;
           if (mi.getDropQuest(mobs.get(i)) > 0) {
            quest = mi.getDropQuest(mobs.get(i));
           }
            int chance = mi.getDropChance(mobs.get(i)) * getClient().getChannelServer().getDropRate();
           
            text += "#r#o" + mobs.get(i) +"##k " + (Integer.valueOf(chance >= 999999 ? 1000000 : chance).doubleValue() / 10000.0)  + "%" + (quest > 0 && MapleQuest.getInstance(quest).getName().length() > 0 ? ("#b需要進行 " + MapleQuest.getInstance(quest).getName() + " 任務來取得#k") : "") + "\r\n";  
            
       }
       sendNext(text);
    }


    public Invocable getIv() {
        return iv;
    }

    public int getMode() {
        return mode;
    }

    public int getNpc() {
        return npc;
    }
    
    
    
    public String getItemName(int itemId){
        return MapleItemInformationProvider.getInstance().getName(itemId);
    }//名字
    
     public String getDesc(int itemId){
        return MapleItemInformationProvider.getInstance().getDesc(itemId);
    }//敘述
    
    
    public int getQuest() {
        return questid;
    }

    public String getScript() {
        return script;
    }

    public byte getType() {
        return type;
    }

    public void safeDispose() {
        pendingDisposal = true;
    }

    public void dispose() {
        NPCScriptManager.getInstance().dispose(c);
    }

    public void askMapSelection(final String sel) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getMapSelection(npc, sel));
        lastMsg = 0xD;
    }

    public void sendNext(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { //sendNext will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", (byte) 0));
        lastMsg = 0;
    }

    public void sendNextS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", type));
        lastMsg = 0;
    }

    public void sendPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", type));
        lastMsg = 0;
    }

    public void sendNextPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", (byte) 0));
        lastMsg = 0;
    }

    public void PlayerToNpc(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", type));
        lastMsg = 0;

    }

    public void sendOk(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendOkS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", type));
        lastMsg = 0;
    }

    public void sendYesNo(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", (byte) 0));
        lastMsg = 1;
    }

    public void sendYesNoS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", type));
        lastMsg = 1;
    }

    public void sendAcceptDecline(String text) {
        askAcceptDecline(text);
    }

    public void sendAcceptDeclineNoESC(String text) {
        askAcceptDeclineNoESC(text);
    }

    public void askAcceptDecline(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0B, text, "", (byte) 0));
        lastMsg = 0xB;
    }

    public void askAcceptDeclineNoESC(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", (byte) 0));
        lastMsg = 0xC;
    }

    public void askAvatar(String text, int... args) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkStyle(npc, text, args));
        lastMsg = 7;
    }

    public void sendSimple(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) 0));
        lastMsg = 4;
    }

    public void sendSimpleS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNextS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) type));
        lastMsg = 4;
    }

    public void sendStyle(String text, int styles[]) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkStyle(npc, text, styles));
        lastMsg = 7;
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
        lastMsg = 3;
    }

    public void sendGetText(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkText(npc, text));
        lastMsg = 2;
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return getText;
    }

    public void setHair(int hair) {
        if (hairExists(hair)) {
            getPlayer().setHair(hair);
            getPlayer().updateSingleStat(MapleStat.HAIR, hair);
            getPlayer().equipChanged();
        }
    }

    public void setFace(int face) {
        if (faceExists(face)) {
            getPlayer().setFace(face);
            getPlayer().updateSingleStat(MapleStat.FACE, face);
            getPlayer().equipChanged();
        }
    }

    public void setSkin(int color) {
        getPlayer().setSkinColor((byte) color);
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public int[] getSavedFaces() {
        return getPlayer().getSavedFaces();
    }

    public int getSavedFace(int sel) {
        return getPlayer().getSavedFace(sel);
    }

    public void setSavedFace(int sel, int id) {
        getPlayer().setSavedFace(sel, id);
    }

    public int[] getSavedHairs() {
        return getPlayer().getSavedHairs();
    }

    public int getSavedHair(int sel) {
        return getPlayer().getSavedHair(sel);
    }

    public void setSavedHair(int sel, int id) {
        getPlayer().setSavedHair(sel, id);
    }

    public static boolean hairExists(int hair) {
        return MapleItemInformationProvider.getInstance().itemExists(hair);
    }

    public static boolean faceExists(int face) {
        return MapleItemInformationProvider.getInstance().itemExists(face);
    }
    
    public boolean hairExist(int hair) {
        return MapleItemInformationProvider.getInstance().itemExists(hair);
    }

    public boolean faceExist(int face) {
        return MapleItemInformationProvider.getInstance().itemExists(face);
    }

    public int[] getCanHair(int[] hairs) {
        List<Integer> canHair = new ArrayList();
        List<Integer> cantHair = new ArrayList();
        for (int hair : hairs) {
            if (hairExists(hair)) {
                canHair.add(hair);
            } else {
                cantHair.add(hair);
            }
        }
        if (cantHair.size() > 0 && c.getPlayer().isAdmin()) {
            StringBuilder sb = new StringBuilder("正在讀取的髮型里有");
            sb.append(cantHair.size()).append("個髮型用戶端不支援顯示，已經被清除：");
            for (int i = 0; i < cantHair.size(); i++) {
                sb.append(cantHair.get(i));
                if (i < cantHair.size() - 1) {
                    sb.append(",");
                }
            }
            playerMessage(sb.toString());
        }
        int[] getHair = new int[canHair.size()];
        for (int i = 0; i < canHair.size(); i++) {
            getHair[i] = canHair.get(i);
        }
        return getHair;
    }

    public int[] getCanFace(int[] faces) {
        List<Integer> canFace = new ArrayList();
        List<Integer> cantFace = new ArrayList();
        for (int face : faces) {
            if (faceExists(face)) {
                canFace.add(face);
            } else {
                cantFace.add(face);
            }
        }
        if (cantFace.size() > 0 && c.getPlayer().isAdmin()) {
            StringBuilder sb = new StringBuilder("正在讀取的臉型里有");
            sb.append(cantFace.size()).append("個臉型用戶端不支援顯示，已經被清除：");
            for (int i = 0; i < cantFace.size(); i++) {
                sb.append(cantFace.get(i));
                if (i < cantFace.size() - 1) {
                    sb.append(",");
                }
            }
            playerMessage(sb.toString());
        }
        int[] getFace = new int[canFace.size()];
        for (int i = 0; i < canFace.size(); i++) {
            getFace[i] = canFace.get(i);
        }
        return getFace;
    }

    public int setRandomAvatar(int ticket, int... args_all) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        int args = args_all[Randomizer.nextInt(args_all.length)];
        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            if (faceExists(args)) {
                c.getPlayer().setFace(args);
                c.getPlayer().updateSingleStat(MapleStat.FACE, args);
            }
        } else {
            if (hairExists(args)) {
                c.getPlayer().setHair(args);
                c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
            }
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public int setAvatar(int ticket, int args) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            if (faceExists(args)) {
                c.getPlayer().setFace(args);
                c.getPlayer().updateSingleStat(MapleStat.FACE, args);
            }
        } else {
            if (hairExists(args)) {
                c.getPlayer().setHair(args);
                c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
            }
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public void sendStorage() {
        if (!World.isShutDown) {
            c.getPlayer().setConversation(4);
            c.getPlayer().getStorage().sendStorage(c, npc);
        } else {
            c.getPlayer().dropMessage(1, "目前不能使用倉庫。");
            c.sendPacket(MaplePacketCreator.enableActions());
        }
    }

    public void openShop(int id) {
        MapleShopFactory.getInstance().getShop(id).sendShop(c);
    }
    
    
    

    public int gachapon(int type) {
        MapleGachaponItem gitem = MapleGachapon.randomItem(type);
        if (gitem == null) {
            return -1;
        }
        int quantity = MapleGachapon.gainItem(gitem);
        final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, gitem.getItemId(), (short) quantity);

        if (item == null) {
            return -1;
        }

        if (gitem.getSmegaType() > 0 ) {
            //World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega(c.getPlayer().getName() + " : x" + quantity + (gitem.getQuantity() > 0 ? gitem.getRemainingQuantity() == 0 ? "(已無剩餘)" : ("（剩餘" + gitem.getRemainingQuantity() + "個）") : "") + "，恭喜" + c.getPlayer().getName() + "從楓葉轉蛋機獲得。", item, c.getChannel()));
            World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega(c.getPlayer().getName() + " : x" + quantity + "恭喜" + c.getPlayer().getName() + "從轉蛋機獲得。", item, c.getChannel()));
        } 
        return item.getItemId();
    }
    

    public int gainGachaponItem(int id, int quantity) {
        return gainGachaponItem(id, quantity, c.getPlayer().getMap().getStreetName() + " - " + c.getPlayer().getMap().getMapName());
    }

    public int gainGachaponItem(int id, int quantity, final String msg) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            final byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            if (rareness == 1) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[" + msg + "] " + c.getPlayer().getName() + " : 被他抽到了，大家恭喜他吧！", item, c.getChannel()));
            } else if (rareness == 2) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[" + msg + "] " + c.getPlayer().getName() + " : 被他成功轉到了，大家恭喜他吧！", item, c.getChannel()));
            } else if (rareness > 2) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[" + msg + "] " + c.getPlayer().getName() + " : 被他從楓葉轉蛋機轉到了，大家恭喜他吧！", item, c.getChannel()));
            }

            return item.getItemId();
        } catch (Exception e) {
        }
        return -1;
    }

    public void changeJob(int job) {
        c.getPlayer().changeJob(job);
    }

    public void startQuest(int id) {
        MapleQuest.getInstance(id).start(getPlayer(), npc);
    }

    public void completeQuest(int id) {
        MapleQuest.getInstance(id).complete(getPlayer(), npc);
    }

    public void forfeitQuest(int id) {
        MapleQuest.getInstance(id).forfeit(getPlayer());
    }

    public void forceStartQuest() {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), null);
    }

    @Override
    public void forceStartQuest(int id) {
        MapleQuest.getInstance(id).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(String customData) {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), customData);
    }

    public void forceCompleteQuest() {
        MapleQuest.getInstance(questid).forceComplete(getPlayer(), getNpc());
    }

    @Override
    public void forceCompleteQuest(final int id) {
        MapleQuest.getInstance(id).forceComplete(getPlayer(), getNpc());
    }

    public String getQuestCustomData() {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).getCustomData();
    }

    public void setQuestCustomData(String customData) {
        getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(customData);
    }

    public int getMeso() {
        return getPlayer().getMeso();
    }

    public void gainAp(final int amount) {
        c.getPlayer().gainAp((short) amount);
    }

    public void expandInventory(byte type, int amt) {
        c.getPlayer().expandInventory(type, amt);
    }

    public void unequipEverything() {
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<Short> ids = new LinkedList<>();
        for (IItem item : equipped.list()) {
            ids.add(item.getPosition());
        }
        for (short id : ids) {
            MapleInventoryManipulator.unequip(getC(), id, equip.getNextFreeSlot());
        }
    }

    public final void clearSkills() {
        Map<ISkill, SkillEntry> skills = getPlayer().getSkills();
        for (Entry<ISkill, SkillEntry> skill : skills.entrySet()) {
            getPlayer().changeSkillLevel(skill.getKey(), (byte) 0, (byte) 0);
        }
    }

    public boolean hasSkill(int skillid) {
        ISkill theSkill = SkillFactory.getSkill(skillid);
        if (theSkill != null) {
            return c.getPlayer().getSkillLevel(theSkill) > 0;
        }
        return false;
    }

    public void playSound(boolean broadcast, String sound) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.playSound(sound));
        } else {
            c.sendPacket(MaplePacketCreator.playSound(sound));
        }
    }

    public void environmentChange(boolean broadcast, String env) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(env, 2));
        } else {
            c.sendPacket(MaplePacketCreator.environmentChange(env, 2));
        }
    }

    public void updateBuddyCapacity(int capacity) {
        c.getPlayer().setBuddyCapacity((byte) capacity);
    }

    public int getBuddyCapacity() {
        return c.getPlayer().getBuddyCapacity();
    }

    public int partyMembersInMap() {
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharactersThreadsafe()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<>(); // creates an empty array full of shit..
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                if (ch != null) { // double check <3
                    chars.add(ch);
                }
            }
        }
        return chars;
    }

    public void warpPartyWithExp(int mapId, int exp) {
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
            }
        }
    }

    public void warpPartyWithExpMeso(int mapId, int exp, int meso) {
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
                curChar.gainMeso(meso, true);
            }
        }
    }

    public MapleSquad getSquad(String type) {
        return c.getChannelServer().getMapleSquad(type);
    }

    public int getSquadAvailability(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        }
        return squad.getStatus();
    }

    public boolean registerSquad(String type, int minutes, String startText) {
        if (c.getChannelServer().getMapleSquad(type) == null) {
            final MapleSquad squad = new MapleSquad(c.getChannel(), type, c.getPlayer(), minutes * 60 * 1000, startText);
            final boolean ret = c.getChannelServer().addMapleSquad(squad, type);
            if (ret) {
                final MapleMap map = c.getPlayer().getMap();

                map.broadcastMessage(MaplePacketCreator.getClock(minutes * 60));
                map.broadcastMessage(MaplePacketCreator.getItemNotice(c.getPlayer().getName() + startText));
            } else {
                squad.clear();
            }
            return ret;
        }
        return false;
    }

    public boolean getSquadList(String type, byte type_) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return false;
        }
        switch (type_) {
            case 0:
            case 3:
                // Normal viewing
                sendNext(squad.getSquadMemberString(type_));
                break;
            case 1:
                // Squad Leader banning, Check out banned participant
                sendSimple(squad.getSquadMemberString(type_));
                break;
            case 2:
                if (squad.getBannedMemberSize() > 0) {
                    sendSimple(squad.getSquadMemberString(type_));
                } else {
                    sendNext(squad.getSquadMemberString(type_));
                }   break;
            default:
                break;
        }
        return true;

    }

    public byte isSquadLeader(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else if (squad.getLeader() != null && squad.getLeader().getId() == c.getPlayer().getId()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean reAdd(String eim, String squad) {
        EventInstanceManager eimz = getDisconnected(eim);
        MapleSquad squadz = getSquad(squad);
        if (eimz != null && squadz != null) {
            squadz.reAddMember(getPlayer());
            eimz.registerPlayer(getPlayer());
            return true;
        }
        return false;
    }

    public void banMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.banMember(pos);
        }
    }

    public void acceptMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.acceptMember(pos);
        }
    }

    public String getReadableMillis(long startMillis, long endMillis) {
        return StringUtil.getReadableMillis(startMillis, endMillis);
    }

    public int addMember(String type, boolean join) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return squad.addMember(c.getPlayer(), join);
        }
        return -1;
    }

    public byte isSquadMember(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else if (squad.getMembers().contains(c.getPlayer().getName())) {
            return 1;
        } else if (squad.isBanned(c.getPlayer())) {
            return 2;
        } else {
            return 0;
        }
    }

    public void resetReactors() {
        getPlayer().getMap().resetReactors();
    }

    public void genericGuildMessage(int code) {
        c.sendPacket(MaplePacketCreator.genericGuildMessage((byte) code));
    }

    public void disbandGuild() {
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0 || c.getPlayer().getGuildRank() != 1) {
            return;
        }
        World.Guild.disbandGuild(gid);
    }

    public void increaseGuildCapacity() {
        if (c.getPlayer().getMeso() < 250000) {
            c.sendPacket(MaplePacketCreator.getPopupMsg("金錢不足25萬."));
            return;
        }
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0) {
            return;
        }
        World.Guild.increaseGuildCapacity(gid);
        c.getPlayer().gainMeso(-250000, true, false, true);
    }

    public void displayGuildRanks() {
        c.sendPacket(MaplePacketCreator.showGuildRanks(npc, MapleGuildRanking.getInstance().getGuildRank()));
    }

    public void showlvl() {
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().getLevelRank()));
    }

    public void showmeso() {
        c.sendPacket(MaplePacketCreator.showmesoRanks(npc, MapleGuildRanking.getInstance().getMesoRank()));
    }

    public boolean removePlayerFromInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            c.getPlayer().getEventInstance().removePlayer(c.getPlayer());
            return true;
        }
        return false;
    }

    public boolean isPlayerInstance() {
        return c.getPlayer().getEventInstance() != null;
    }

    public void changeStat(byte slot, int type, short amount) {
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
        switch (type) {
            case 0:
                sel.setStr(amount);
                break;
            case 1:
                sel.setDex(amount);
                break;
            case 2:
                sel.setInt(amount);
                break;
            case 3:
                sel.setLuk(amount);
                break;
            case 4:
                sel.setHp(amount);
                break;
            case 5:
                sel.setMp(amount);
                break;
            case 6:
                sel.setWatk(amount);
                break;
            case 7:
                sel.setMatk(amount);
                break;
            case 8:
                sel.setWdef(amount);
                break;
            case 9:
                sel.setMdef(amount);
                break;
            case 10:
                sel.setAcc(amount);
                break;
            case 11:
                sel.setAvoid(amount);
                break;
            case 12:
                sel.setHands(amount);
                break;
            case 13:
                sel.setSpeed(amount);
                break;
            case 14:
                sel.setJump(amount);
                break;
            case 15:
                sel.setUpgradeSlots((byte) amount);
                break;
            case 16:
                sel.setViciousHammer((byte) amount);
                break;
            case 17:
                sel.setLevel((byte) amount);
                break;
            case 18:
                sel.setEnhance((byte) amount);
                break;
            case 19:
                sel.setPotential1(amount);
                break;
            case 20:
                sel.setPotential2(amount);
                break;
            case 21:
                sel.setPotential3(amount);
                break;
            case 22:
                sel.setOwner(getText());
                break;
            default:
                break;
        }
        c.getPlayer().equipChanged();
    }
    
    public void cashStat(byte slot, int type, short amount) {
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        switch (type) {
            case 0:
                if((sel.getStr() + amount)<0){
                   sel.setStr((short) 0);
                }
                sel.setStr((short) (sel.getStr() + amount));
                break;
            case 1:
                if((sel.getDex() + amount)<0){
                   sel.setDex((short) 0);
                }
                sel.setDex((short) (sel.getDex() + amount));
                break;
            case 2:
                if((sel.getInt() + amount)<0){
                   sel.setInt((short) 0);
                }
                sel.setInt((short) (sel.getInt() + amount));
                break;
            case 3:
                if((sel.getLuk() + amount)<0){
                   sel.setLuk((short) 0);
                }
                sel.setLuk((short) (sel.getLuk() + amount));
                break;
            case 4:
                if((sel.getWatk() + amount)<0){
                   sel.setWatk((short) 0);
                }
                sel.setWatk((short) (sel.getWatk() + amount));
                break;
            case 5:
                if((sel.getMatk() + amount)<0){
                   sel.setMatk((short) 0);
                }
                sel.setMatk((short) (sel.getMatk() + amount));
                break;
            case 6:
                sel.setHp(amount);
                break;
            case 7:
                sel.setMp(amount);
                break;
            case 8:
                sel.setWdef(amount);
                break;
            case 9:
                sel.setMdef(amount);
                break;
            case 10:
                sel.setAcc(amount);
                break;
            case 11:
                sel.setAvoid(amount);
                break;
            case 12:
                sel.setHands(amount);
                break;
            case 13:
                sel.setSpeed(amount);
                break;
            case 14:
                sel.setJump(amount);
                break;
            case 15:
                sel.setUpgradeSlots((byte) amount);
                break;
            case 16:
                sel.setViciousHammer((byte) amount);
                break;
            case 17:
                sel.setLevel((byte) amount);
                break;
            case 18:
                sel.setEnhance((byte) amount);
                break;
            case 19:
                sel.setPotential1(amount);
                break;
            case 20:
                sel.setPotential2(amount);
                break;
            case 21:
                sel.setPotential3(amount);
                break;
            case 22:
                sel.setOwner(getText());
                break;
            default:
                break;
        }
        c.getPlayer().equipChanged();
    }

    public void cleardrops() {
        MapleMonsterInformationProvider.getInstance().clearDrops();
    }
    
    
public static String byteArrayToStr(byte[] byteArray) {
    if (byteArray == null) {
        return null;
    }
    String str = new String(byteArray);
    return str;
}
    
    
    public void cashItemUpgrader(byte slot,int type){
        if(type == 1){
            IEquip cash;
            String jj = null;
            cash = (IEquip) getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
            int ostr = cash.getStr();
            int odex = cash.getDex();
            int oint = cash.getInt();
            int oluk = cash.getLuk();
            int oatt = cash.getWatk();
            int omat = cash.getMatk();
            int increase = 1;
            if (Math.ceil(Math.random() * 100.0) <= 50)
            increase = increase*-1;
            
           
            
            String cashC = "已附魔次數 :";
            if(getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).getOwner().toString()==""){
                jj = "已附魔次數 :0";
            }else if(!getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).getOwner().contains(cashC)){
                jj = "已附魔次數 :0";
            }
            else {
                jj = getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).getOwner();
            }
            String times = difference(cashC,jj);
            cashStat(slot,cashORandom()[0],(short)(Math.ceil(Math.random() * 3.0)*increase));
            cashStat(slot,cashORandom()[1],(short)(Math.ceil(Math.random() * 3.0)*increase));
            cashStat(slot,cashORandom()[2],(short)(Math.ceil(Math.random() * 3.0)*increase));
            cashStat(slot,cashORandom()[3],(short)(Math.ceil(Math.random() * 3.0)*increase));
            
            int ntimes = Integer.parseInt(times);
            
            
            getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).setOwner("已附魔次數 :"+ (ntimes+1));
            getPlayer().saveToDB(true,true);
        }
        if(type == 2){
            IEquip cash;
            String jj = null;
            cash = (IEquip) getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
            int ostr = cash.getStr();
            int odex = cash.getDex();
            int oint = cash.getInt();
            int oluk = cash.getLuk();
            int oatt = cash.getWatk();
            int omat = cash.getMatk();
            int increase = 1;
           
            String cashC = "已附魔次數 :";
             if(getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).getOwner().toString()==""){
                jj = "已附魔次數 :0";
            }else if(!getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).getOwner().contains(cashC)){
                jj = "已附魔次數 :0";
            }
            else {
                jj = getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).getOwner();
            }
            String times = difference(cashC,jj);
            Random random = new Random();
            cashStat(slot,cashORandom()[0],(short)(random.nextInt(5)%(5-1+1) + 1));
            cashStat(slot,cashORandom()[1],(short)(random.nextInt(5)%(5-1+1) + 1));
            cashStat(slot,cashORandom()[2],(short)(random.nextInt(5)%(5-1+1) + 1));
            cashStat(slot,cashORandom()[3],(short)(random.nextInt(5)%(5-1+1) + 1));
            
            int ntimes = Integer.parseInt(times);
            //getPlayer().dropMessage(1,Integer.toString(ntimes+1));
            getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).setOwner("已附魔次數 :"+ (ntimes+1));
            getPlayer().saveToDB(true,true);
        }
        if(type == 3){
            IEquip cash;
            String jj = null;
            cash = (IEquip) getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
            int ostr = cash.getStr();
            int odex = cash.getDex();
            int oint = cash.getInt();
            int oluk = cash.getLuk();
            int oatt = cash.getWatk();
            int omat = cash.getMatk();
            String cashC = "已附魔次數 :";
            if(getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).getOwner().toString()==""){
                jj = "已附魔次數 :0";
            }else if(!getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).getOwner().contains(cashC)){
                jj = "已附魔次數 :0";
            }
            else {
                jj = getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).getOwner();
            }
            String times = difference(cashC,jj);
            cashStat(slot,0,(short)(10));
            cashStat(slot,1,(short)(10));
            cashStat(slot,2,(short)(10));
            cashStat(slot,3,(short)(10));
            cashStat(slot,4,(short)(10));
            cashStat(slot,5,(short)(10));

            int ntimes = Integer.parseInt(times);
            //getPlayer().dropMessage(1,Integer.toString(ntimes+1));
            getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot).setOwner("已附魔次數 :"+ (ntimes+1));
            getPlayer().saveToDB(true,true);
        }
    }
    
   public String difference(String str1, String str2) {
    if (str1 == null) {
        return str2;
    }
    if (str2 == null) {
        return str1;
    }
    int at = indexOfDifference(str1, str2);
    if (at == 0) {
        return null;
    }
    return str2.substring(at);
   }
   
   public int indexOfDifference(CharSequence cs1, CharSequence cs2) {
    if (cs1 == cs2) {
        return 0;
    }
    if (cs1 == null || cs2 == null) {
        return 0;
    }
    int i;
    for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
        if (cs1.charAt(i) != cs2.charAt(i)) {
            break;
        }
    }
    if (i < cs2.length() || i < cs1.length()) {
        return i;
    }
    return 0;
}
    
    public int[] cashORandom(){
        Random random = new Random();
       
        int[] cashchange = new int[4];
            for (int i=0; i<4; i++){
			cashchange[i] = random.nextInt(6);		// 將隨機數(0-6)放入lottoNum[i]
			for (int j=0; j<i;){			// 與前數列比較，若有相同則再取亂數
				if (cashchange[j]==cashchange[i]){	
					cashchange[i] = random.nextInt(6);
					j=0;			// 避面重新亂數後又產生相同數字，若出現重覆，迴圈從頭開始重新比較所有數
				}
				else j++;			// 若都不重複則下一個數
			}
        }
            return cashchange;
    }
    
    
    
 

    public void killAllMonsters() {
        MapleMap map = c.getPlayer().getMap();
        double range = Double.POSITIVE_INFINITY;
        MapleMonster mob;
        for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
            mob = (MapleMonster) monstermo;
            if (mob.getStats().isBoss()) {
                map.killMonster(mob, c.getPlayer(), false, false, (byte) 1);
            }
        }
        /*int mapid = c.getPlayer().getMapId();
         MapleMap map = c.getChannelServer().getMapFactory().getMap(mapid);
         map.killAllMonsters(true); // No drop. */
    }

    public void giveMerchantMesos() {
        long mesos = 0;
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT mesos FROM hiredmerchants WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
            } else {
                mesos = rs.getLong("mesos");
            }
            rs.close();
            ps.close();

            ps = (PreparedStatement) con.prepareStatement("UPDATE hiredmerchants SET mesos = 0 WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        c.getPlayer().gainMeso((int) mesos, true);
    }

    public void CreateLog(String log) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO logs(`cid`, `name`, `log`) VALUES (?,?,?)");
            ps.setInt(1, c.getPlayer().getId());
            ps.setString(2, c.getPlayer().getName());
            ps.setString(3, log);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error createlog" + ex);
            FileoutputUtil.printError("CreateLog.txt", ex, "ErrorCreateLog has SQL Exception");
        }
    }

    public void dc() {
        MapleCharacter victim = getChannelServer().getPlayerStorage().getCharacterByName(getPlayer().getName());
        victim.getClient().getSession().close();
        victim.getClient().disconnect(true, false);
    }

    public long getMerchantMesos() {
        long mesos = 0;

        Connection con = (Connection) DatabaseConnection.getConnection();
        try (PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT mesos FROM hiredmerchants WHERE merchantid = ?")) {
            ps.setInt(1, getPlayer().getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                } else {
                    mesos = rs.getLong("mesos");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        return mesos;
    }

    public void openDuey() {
        c.getPlayer().setConversation(2);
        c.sendPacket(MaplePacketCreator.sendDuey((byte) 9, null));
    }

    public void openMerchantItemStore() {
        if (!World.isShutDown) {
            c.getPlayer().setConversation(3);
            c.sendPacket(PlayerShopPacket.merchItemStore((byte) 0x22));
        } else {
            c.getPlayer().dropMessage(1, "目前不能使用精靈商人領取。");
            c.sendPacket(MaplePacketCreator.enableActions());
        }
    }
    
     public String masterQuery(String query, String return_item) {
        if (!query.toLowerCase().contains("select")) {
            return "此功能僅用於查詢";
        }
        ArrayList<Object> queryObjects = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    queryObjects.add(rs.getObject(return_item));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(NPCConversationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (queryObjects.isEmpty()) {
            return "您所輸入的 :\r\n#e" + query + "#n\r\n 並無查詢結果.";
        }
        String return_query = "查詢結果為 :\r\n#e" + query + "#n";
        for (Object queryObject : queryObjects) {
            return_query += "\r\n" + queryObject;
        }
        return return_query;
    }


    public void sendRepairWindow() {
        c.sendPacket(MaplePacketCreator.sendRepairWindow(npc));
    }

    public final int getDojoPoints() {
        return c.getPlayer().getDojo();
    }

    public final int getDojoRecord() {
        return c.getPlayer().getDojoRecord();
    }

    public void setDojoRecord(final boolean reset) {
        c.getPlayer().setDojoRecord(reset);
    }

    public boolean start_DojoAgent(final boolean dojo, final boolean party) {
        if (dojo) {
            return Event_DojoAgent.warpStartDojo(c.getPlayer(), party);
        }
        return Event_DojoAgent.warpStartAgent(c.getPlayer(), party);
    }

    public boolean start_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpStartPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpStartSubway(c.getPlayer());
    }

    public boolean bonus_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            c.getPlayer().setCheckedPyramid(true);
            return Event_PyramidSubway.warpBonusPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpBonusSubway(c.getPlayer());
    }

    public final short getKegs() {
        return AramiaFireWorks.getInstance().getKegsPercentage();
    }

    public void giveKegs(final int kegs) {
        AramiaFireWorks.getInstance().giveKegs(c.getPlayer(), kegs);
    }

    public final short getSunshines() {
        return AramiaFireWorks.getInstance().getSunsPercentage();
    }

    public void addSunshines(final int kegs) {
        AramiaFireWorks.getInstance().giveSuns(c.getPlayer(), kegs);
    }

    public final short getDecorations() {
        return AramiaFireWorks.getInstance().getDecsPercentage();
    }

    public void addDecorations(final int kegs) {
        try {
            AramiaFireWorks.getInstance().giveDecs(c.getPlayer(), kegs);
        } catch (Exception e) {
        }
    }

    public final MapleInventory getInventory(int type) {
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) type));
    }

    public final MapleCarnivalParty getCarnivalParty() {
        return c.getPlayer().getCarnivalParty();
    }

    public final MapleCarnivalChallenge getNextCarnivalRequest() {
        return c.getPlayer().getNextCarnivalRequest();
    }

    public final MapleCarnivalChallenge getCarnivalChallenge(MapleCharacter chr) {
        return new MapleCarnivalChallenge(chr);
    }

    public void maxStats() {
        List<Pair<MapleStat, Integer>> statup = new ArrayList<>(2);
        c.getPlayer().getStat().setStr((short) 32767);
        c.getPlayer().getStat().setDex((short) 32767);
        c.getPlayer().getStat().setInt((short) 32767);
        c.getPlayer().getStat().setLuk((short) 32767);

        c.getPlayer().getStat().setMaxHp((short) 30000);
        c.getPlayer().getStat().setMaxMp((short) 30000);
        c.getPlayer().getStat().setHp((short) 30000);
        c.getPlayer().getStat().setMp((short) 30000);

        statup.add(new Pair<>(MapleStat.STR, 32767));
        statup.add(new Pair<>(MapleStat.DEX, 32767));
        statup.add(new Pair<>(MapleStat.LUK, 32767));
        statup.add(new Pair<>(MapleStat.INT, 32767));
        statup.add(new Pair<>(MapleStat.HP, 30000));
        statup.add(new Pair<>(MapleStat.MAXHP, 30000));
        statup.add(new Pair<>(MapleStat.MP, 30000));
        statup.add(new Pair<>(MapleStat.MAXMP, 30000));

        c.sendPacket(MaplePacketCreator.updatePlayerStats(statup, c.getPlayer().getJob()));
    }

    public Pair<String, Map<Integer, String>> getSpeedRun(String typ) {
        final SpeedRunType stype = SpeedRunType.valueOf(typ);
        if (SpeedRunner.getInstance().getSpeedRunData(stype) != null) {
            return SpeedRunner.getInstance().getSpeedRunData(stype);
        }
        return new Pair<>("", new HashMap<>());
    }

    public boolean getSR(Pair<String, Map<Integer, String>> ma, int sel) {
        if (ma.getRight().get(sel) == null || ma.getRight().get(sel).length() <= 0) {
            dispose();
            return false;
        }
        sendOk(ma.getRight().get(sel));
        return true;
    }

    public Equip getEquip(int itemid) {
        return (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
    }

    public void setExpiration(Object statsSel, long expire) {
        if (statsSel instanceof Equip) {
            ((Equip) statsSel).setExpiration(System.currentTimeMillis() + (expire * 24 * 60 * 60 * 1000));
        }
    }

    public void setLock(Object statsSel) {
        if (statsSel instanceof Equip) {
            Equip eq = (Equip) statsSel;
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
        }
    }

    public boolean addFromDrop(Object statsSel) {
        if (statsSel instanceof IItem) {
            final IItem it = (IItem) statsSel;
            return MapleInventoryManipulator.checkSpace(getClient(), it.getItemId(), it.getQuantity(), it.getOwner()) && MapleInventoryManipulator.addFromDrop(getClient(), it, false);
        }
        return false;
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type) {
        return replaceItem(slot, invType, statsSel, offset, type, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type, boolean takeSlot) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        IItem item = getPlayer().getInventory(inv).getItem((byte) slot);
        if (item == null || statsSel instanceof IItem) {
            item = (IItem) statsSel;
        }
        if (offset > 0) {
            if (inv != MapleInventoryType.EQUIP) {
                return false;
            }
            Equip eq = (Equip) item;
            if (takeSlot) {
                if (eq.getUpgradeSlots() < 1) {
                    return false;
                } else {
                    eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() - 1));
                }
            }
            if (type.equalsIgnoreCase("Slots")) {
                eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() + offset));
            } else if (type.equalsIgnoreCase("Level")) {
                eq.setLevel((byte) (eq.getLevel() + offset));
            } else if (type.equalsIgnoreCase("Hammer")) {
                eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
            } else if (type.equalsIgnoreCase("STR")) {
                eq.setStr((short) (eq.getStr() + offset));
            } else if (type.equalsIgnoreCase("DEX")) {
                eq.setDex((short) (eq.getDex() + offset));
            } else if (type.equalsIgnoreCase("INT")) {
                eq.setInt((short) (eq.getInt() + offset));
            } else if (type.equalsIgnoreCase("LUK")) {
                eq.setLuk((short) (eq.getLuk() + offset));
            } else if (type.equalsIgnoreCase("HP")) {
                eq.setHp((short) (eq.getHp() + offset));
            } else if (type.equalsIgnoreCase("MP")) {
                eq.setMp((short) (eq.getMp() + offset));
            } else if (type.equalsIgnoreCase("WATK")) {
                eq.setWatk((short) (eq.getWatk() + offset));
            } else if (type.equalsIgnoreCase("MATK")) {
                eq.setMatk((short) (eq.getMatk() + offset));
            } else if (type.equalsIgnoreCase("WDEF")) {
                eq.setWdef((short) (eq.getWdef() + offset));
            } else if (type.equalsIgnoreCase("MDEF")) {
                eq.setMdef((short) (eq.getMdef() + offset));
            } else if (type.equalsIgnoreCase("ACC")) {
                eq.setAcc((short) (eq.getAcc() + offset));
            } else if (type.equalsIgnoreCase("Avoid")) {
                eq.setAvoid((short) (eq.getAvoid() + offset));
            } else if (type.equalsIgnoreCase("Hands")) {
                eq.setHands((short) (eq.getHands() + offset));
            } else if (type.equalsIgnoreCase("Speed")) {
                eq.setSpeed((short) (eq.getSpeed() + offset));
            } else if (type.equalsIgnoreCase("Jump")) {
                eq.setJump((short) (eq.getJump() + offset));
            } else if (type.equalsIgnoreCase("ItemEXP")) {
                eq.setItemEXP(eq.getItemEXP() + offset);
            } else if (type.equalsIgnoreCase("Expiration")) {
                eq.setExpiration((long) (eq.getExpiration() + offset));
            } else if (type.equalsIgnoreCase("Flag")) {
                eq.setFlag((byte) (eq.getFlag() + offset));
            }
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
            item = eq.copy();
        }
        MapleInventoryManipulator.removeFromSlot(getClient(), inv, (short) slot, item.getQuantity(), false);
        return MapleInventoryManipulator.addFromDrop(getClient(), item, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int upgradeSlots) {
        return replaceItem(slot, invType, statsSel, upgradeSlots, "Slots");
    }

    public boolean isCash(final int itemId) {
        return MapleItemInformationProvider.getInstance().isCash(itemId);
    }
    
    public Image getImage(){
        Image icon = new ImageIcon(getClass().getClassLoader().getResource("Character.wz/Weapon/01212000.img/info/icon.png")).getImage();
        return icon;
    }

    public void buffGuild(final int buff, final int duration, final String msg) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.getItemEffect(buff) != null && getPlayer().getGuildId() > 0) {
            final MapleStatEffect mse = ii.getItemEffect(buff);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharactersThreadSafe()) {
                    if (chr.getGuildId() == getPlayer().getGuildId()) {
                        mse.applyTo(chr, chr, true, null, duration);
                        chr.dropMessage(5, "Your guild has gotten a " + msg + " buff.");
                    }
                }
            }
        }
    }

    public boolean createAlliance(String alliancename) {
        MapleParty pt = c.getPlayer().getParty();
        MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterById(pt.getMemberByIndex(1).getId());
        if (otherChar == null || otherChar.getId() == c.getPlayer().getId()) {
            return false;
        }
        try {
            return World.Alliance.createAlliance(alliancename, c.getPlayer().getId(), otherChar.getId(), c.getPlayer().getGuildId(), otherChar.getGuildId());
        } catch (Exception re) {
            return false;
        }
    }

    public boolean addCapacityToAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.changeAllianceCapacity(gs.getAllianceId())) {
                    gainMeso(-MapleGuildAlliance.CHANGE_CAPACITY_COST);
                    return true;
                }
            }
        } catch (Exception re) {
        }
        return false;
    }

    public boolean disbandAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.disbandAlliance(gs.getAllianceId())) {
                    return true;
                }
            }
        } catch (Exception re) {
        }
        return false;
    }

    public byte getLastMsg() {
        return lastMsg;
    }

    public final void setLastMsg(final byte last) {
        this.lastMsg = last;
    }

    public void setPartyBossLog(String bossid) {
        MapleParty party = getPlayer().getParty();
        for (MaplePartyCharacter pc : party.getMembers()) {
            MapleCharacter chr = World.getStorage(this.getChannelNumber()).getCharacterById(pc.getId());
            if (chr != null) {
                chr.setBossLog(bossid);
            }
        }
    }

    public final void maxAllSkills() {
        for (ISkill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId())) { //no db/additionals/resistance skills
                teachSkill(skil.getId(), skil.getMaxLevel(), skil.getMaxLevel());
            }
        }
    }

    public final void resetStats(int str, int dex, int z, int luk) {
        c.getPlayer().resetStats(str, dex, z, luk);
    }

    public final boolean dropItem(int slot, int invType, int quantity) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        return MapleInventoryManipulator.drop(c, inv, (short) slot, (short) quantity, true);
    }

    public final List<Integer> getAllPotentialInfo() {
        return new ArrayList<>(MapleItemInformationProvider.getInstance().getAllPotentialInfo().keySet());
    }

    public final String getPotentialInfo(final int id) {
        final List<StructPotentialItem> potInfo = MapleItemInformationProvider.getInstance().getPotentialInfo(id);
        final StringBuilder builder = new StringBuilder("#b#ePOTENTIAL INFO FOR ID: ");
        builder.append(id);
        builder.append("#n#k\r\n\r\n");
        int minLevel = 1, maxLevel = 10;
        for (StructPotentialItem item : potInfo) {
            builder.append("#eLevels ");
            builder.append(minLevel);
            builder.append("~");
            builder.append(maxLevel);
            builder.append(": #n");
            builder.append(item.toString());
            minLevel += 10;
            maxLevel += 10;
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public final void sendRPS() {
        c.sendPacket(MaplePacketCreator.getRPSMode((byte) 8, -1, -1, -1));
    }

    public final void setQuestRecord(Object ch, final int questid, final String data) {
        ((MapleCharacter) ch).getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(data);
    }

    public final void doWeddingEffect(final Object ch) {
        final MapleCharacter chr = (MapleCharacter) ch;
        getMap().broadcastMessage(MaplePacketCreator.yellowChat(chr.getName() + ", 妳願意承認 " + getPlayer().getName() + " 做妳的丈夫，誠實遵照上帝的誡命，和他生活在一起，無論在什麼環境願順服他、愛惜他、安慰他、尊重他保護他，以致奉召歸主？？"));
        CloneTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (chr == null || getPlayer() == null) {
                    warpMap(680000500, 0);
                } else {
                    getMap().broadcastMessage(MaplePacketCreator.yellowChat(getPlayer().getName() + ", 你願意承認接納 " + chr.getName() + " 做你的妻子，誠實遵照上帝的誡命，和她生活在一起，無論在什麼環境，願意終生養她、愛惜她、安慰她、尊重她、保護她，以至奉召歸主？？"));
                }
            }
        }, 10000);
        CloneTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (chr == null || getPlayer() == null) {
                    if (getPlayer() != null) {
                        setQuestRecord(getPlayer(), 160001, "3");
                        setQuestRecord(getPlayer(), 160002, "0");
                    } else if (chr != null) {
                        setQuestRecord(chr, 160001, "3");
                        setQuestRecord(chr, 160002, "0");
                    }
                    warpMap(680000500, 0);
                } else {
                    setQuestRecord(getPlayer(), 160001, "2");
                    setQuestRecord(chr, 160001, "2");
                    sendNPCText(getPlayer().getName() + " 和 " + chr.getName() + "， 我希望你們兩個能在此時此刻永遠愛著對方！", 9201002);
                    getMap().startExtendedMapEffect("那麼現在請新娘親吻 " + getPlayer().getName() + "！", 5120006);
                    if (chr.getGuildId() > 0) {
                        World.Guild.guildPacket(chr.getGuildId(), MaplePacketCreator.sendMarriage(false, chr.getName()));
                    }
                    if (chr.getFamilyId() > 0) {
                        World.Family.familyPacket(chr.getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), chr.getId());
                    }
                    if (getPlayer().getGuildId() > 0) {
                        World.Guild.guildPacket(getPlayer().getGuildId(), MaplePacketCreator.sendMarriage(false, getPlayer().getName()));
                    }
                    if (getPlayer().getFamilyId() > 0) {
                        World.Family.familyPacket(getPlayer().getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), getPlayer().getId());
                    }
                }
            }
        }, 20000); //10 sec 10 sec
    }

    public void 開啟小鋼珠() {
        c.sendPacket(BeansPacket.showBeans(getPlayer().getBeans()));
    }

    public void worldMessage(String text) {
        World.Broadcast.broadcastMessage(MaplePacketCreator.getItemNotice(text));
    }

    public void warpBack(int mid, final int retmap, final int time) { //時間秒數

        MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(mid);
        c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
        c.sendPacket(MaplePacketCreator.getClock(time));
        Timer.EventTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(retmap);
                if (c.getPlayer() != null) {
                    c.sendPacket(MaplePacketCreator.stopClock());
                    c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
                    c.getPlayer().dropMessage(6, "已經到達目的地了!");
                }
            }
        }, 1000 * time); //設定時間, (1 秒 = 1000)
    }

    public void ChangeName(String name) {
        getPlayer().setName(name);
        save();
        getPlayer().fakeRelog();
    }

    public String searchData(int type, String search) {
        return SearchGenerator.searchData(type, search);
    }
    
    public int[] getSearchData(int type, String search) {
        Map<Integer, String> data = SearchGenerator.getSearchData(type, search);
        if (data.isEmpty()) {
            return null;
        }
        int[] searches = new int[data.size()];
        int i = 0;
        for (int key : data.keySet()) {
            searches[i] = key;
            i++;
        }
        return searches;
    }

    public void OwlAdv(int point, int itemid) {
        InventoryHandler.owlse(c, point, itemid);
    }

    public boolean foundData(int type, String search) {
        return SearchGenerator.foundData(type, search);
    }

    public boolean ReceiveMedal() {
        int acid = getPlayer().getAccountID();
        int id = getPlayer().getId();
        String name = getPlayer().getName();
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        int item = 1142475;
        if (!getPlayer().canHold(item)) {
            return false;
        } else if (getPlayer().haveItem(item)) {
            return false;
        }

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id FROM rcmedals WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.first()) {// 角色不存在於勳章表單
                return false;
            }
            ps.close();

            ps = con.prepareStatement("SELECT id FROM rcmedals WHERE accountid = ? and amount = ?");
            ps.setInt(1, acid);
            ps.setInt(2, 0);
            rs = ps.executeQuery();
            if (rs.next()) {// 帳號存在於勳章表單
                return false;
            }
            ps.close();
            rs.close();

            ps = con.prepareStatement("Update rcmedals set amount = ? Where id = ?");
            ps.setInt(1, 0);
            ps.setInt(2, id);
            ps.execute();
            ps.close();
        } catch (Exception ex) {
            FilePrinter.printError("NPCConversationManager.txt", ex, "ReceiveMedal(" + name + ")");
        }
        IItem toDrop = ii.randomizeStats((Equip) ii.getEquipById(item));;
        toDrop.setGMLog(getPlayer().getName() + " 領取勳章");
        MapleInventoryManipulator.addbyItem(c, toDrop);
        FileoutputUtil.logToFile("logs/data/NPC領取勳章.txt", "\r\n " + FileoutputUtil.NowTime() + " IP: " + c.getSession().remoteAddress().toString().split(":")[0] + " 帳號: " + c.getAccountName() + " 玩家: " + c.getPlayer().getName() + " 領取了RC勳章");
        return true;
    }
    
    
     public String getDayOfWeek() {
            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
            String dd = String.valueOf(dayOfWeek);
            switch (dayOfWeek) {
                case 0:
                    dd = "星期日";
                    break;
                case 1:
                    dd = "星期一";
                    break;
                case 2:
                    dd = "星期二";
                    break;
                case 3:
                    dd = "星期三";
                    break;
                case 4:
                    dd = "星期四";
                    break;
                case 5:
                    dd = "星期五";
                    break;
                case 6:
                    dd = "星期六";
                    break;
            }
            return dd;
    }

    public String ShowJobRank(int type) {
        StringBuilder sb = new StringBuilder();
        List<MapleGuildRanking.JobRankingInfo> Ranking = MapleGuildRanking.getInstance().getJobRank(type);
        if (Ranking != null) {
            int num = 0;
            for (MapleGuildRanking.JobRankingInfo info : Ranking) {
                num++;
                sb.append("#n#e#k排名:#r ");
                sb.append(num);
                sb.append("\r\n#n#e#k玩家名稱:#d ");
                sb.append(StringUtil.getRightPaddedStr(info.getName(), ' ', 13));
                sb.append("\r\n#n#e#k等級:#e#r ");
                sb.append(StringUtil.getRightPaddedStr(String.valueOf(info.getLevel()), ' ', 3));
                sb.append("\r\n#n#e#k職業:#e#b ");
                sb.append(MapleJob.getName(MapleJob.getById(info.getJob())));
                sb.append("\r\n#n#e#k力量:#e#d ");
                sb.append(StringUtil.getRightPaddedStr(String.valueOf(info.getStr()), ' ', 4));
                sb.append("\r\n#n#e#k敏捷:#e#d ");
                sb.append(StringUtil.getRightPaddedStr(String.valueOf(info.getDex()), ' ', 4));
                sb.append("\r\n#n#e#k智力:#e#d ");
                sb.append(StringUtil.getRightPaddedStr(String.valueOf(info.getInt()), ' ', 4));
                sb.append("\r\n#n#e#k幸運:#e#d ");
                sb.append(StringUtil.getRightPaddedStr(String.valueOf(info.getLuk()), ' ', 4));
                sb.append("\r\n");
                sb.append("#n#k======================================================\r\n");
            }
        } else {
            sb.append("#r查詢無任何結果唷");
        }
        return sb.toString();
    }
    
    public String getGachaView(int type){
        String item = "#e#d【#k#r" + type  + " #k#d轉蛋機內容物如下】:#k #b(機率小至大)#k#n\r\n";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId FROM gachaponitems WHERE gachaponType = ? ORDER BY chance ASC");
            ps.setInt(1, type);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                item += "#v" + rs.getInt("itemId") + "#"; 
            }
        }catch (SQLException ex) {
            Logger.getLogger(NPCConversationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return item;
    }
    
    public String editGacha(int type){
        //reloadgacha();
        String item = "#b請點擊物品後進行修改:#k\r\n";
        //ArrayList<Object> queryObjects = new ArrayList<>();
        item += "======================================\r\n#L30678##r我要新增物品#k#l\r\n";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId,chance FROM gachaponitems WHERE gachaponType = ? ");
            ps.setInt(1, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               item += "#L" + rs.getInt("itemId") + "##i" + rs.getInt("itemId") + "# #d#z" + rs.getInt("itemId") + "##k 機率: #b" + (rs.getDouble("chance")/1000) + "%#k#l\r\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(NPCConversationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return item;
    }
    
    public void gachaponadditem(int itemId, int chance, int smega, int type){
        reloadgacha();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO gachaponitems (`itemId`, `quantity`, `remainingQuantity`, `minimum_quantity`, `maximum_quantity`, `chance`,  `smegaType`,  `gachaponType`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, itemId);
            ps.setInt(2, 0);
            ps.setInt(3, 999);
            ps.setInt(4, 1);
            ps.setInt(5, 1);
            ps.setInt(6, chance);
            ps.setInt(7, smega);
            ps.setInt(8, type);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("無法新增此物品" + e);
        }
    }
    
    public void reloadgacha(){
        MapleGachapon.reloaditems();
    }
    
    public void newGachapon(int itemId, int chance, int type){
        reloadgacha();
        try {
             Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("Update gachaponitems set chance = ? Where itemId = ? and gachaponType = ?");
            ps.setInt(1, chance);
            ps.setInt(2, itemId);
            ps.setInt(3, type);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("[Gacha]無法連接資料庫");
        } catch (Exception ex) {
            FilePrinter.printError("更新轉蛋.txt", ex, "newGachapon");
            System.err.println("[Gacha]" + ex);
        }      
    }
    
    public void zerodelete(int itemId, int type){
        reloadgacha();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("Delete from gachaponitems WHERE itemId = ? and gachaponType = ?");
            ps.setInt(1, itemId);
            ps.setInt(2, type);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("[Gacha]無法連接資料庫");
        } catch (Exception ex) {
            FilePrinter.printError("更新轉蛋.txt", ex, "newGachapon");
            System.err.println("[Gacha]" + ex);
        }      
    }
    
    public double gachaponchance(int itemId, int type){
        reloadgacha();
        int ret = 0;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps;
            ps = con.prepareStatement("SELECT chance FROM gachaponitems WHERE itemId = ? and gachaponType = ?");
            ps.setInt(1, itemId);
            ps.setInt(2, type);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            ret = rs.getInt("chance");
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new DatabaseException("error getting create", e);
        }
          return ret/1000;
    }
    
    public String checkDrop(int mobId) {
        final List<MonsterDropEntry> ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        if (ranks != null && ranks.size() > 0) {
            int num = 0, itemId = 0, ch = 0;
            MonsterDropEntry de;
            StringBuilder name = new StringBuilder();
            if(getPlayer().isGM()){
                 name.append("\r\n=========================================\r\n#L30678##r新增掉落物#k");
            }
            for (int i = 0; i < ranks.size(); i++) {
                de = ranks.get(i);
                if (de.chance > 0 && (de.questid <= 0 || (de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0))) {
                    itemId = de.itemId;
                    if (num == 0) {
                        name.append("怪物名稱：#o").append(mobId).append("#\r\n");
                        name.append("--------------------------------------\r\n");
                    }
                    String namez = "#z" + itemId + "#";
                    if (itemId == 0) { //meso
                        itemId = 4031041; //display sack of cash
                        namez = (de.Minimum * getClient().getChannelServer().getMesoRate()) + " 至 " + (de.Maximum * getClient().getChannelServer().getMesoRate()) + " 楓幣";
                    }
                    ch = de.chance * getClient().getChannelServer().getDropRate();
                    
                    name.append((getPlayer().isGM() ? ("#L" + itemId + "#" + (num + 1) ) : (num + 1)) + ") #v" + itemId + "#" + namez + (getPlayer().isGM()  ? ("- 機率 " + (getDropChance(itemId,mobId)*getClient().getChannelServer().getDropRate() / 10000.0) + "% ") : "") + (de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("相關任務： " + MapleQuest.getInstance(de.questid).getName()) : "") + (getPlayer().isGM()? "#l\r\n" : "\r\n"));
                    num++;
                    
                }
            }
            
            if (name.length() > 0) {
                return name.toString();
            }

        }
        return getPlayer().isGM()? "\r\n=========================================\r\n#L30678##r新增掉落物#k" : "查無掉寶資訊。";
    }
    
    public int getDropChance(int itemid, int dropper){
        int ret = 0;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps;
            ps = con.prepareStatement("SELECT chance FROM drop_data WHERE itemid = ? and dropperid = ?");
            ps.setInt(1, itemid);
            ps.setInt(2, dropper);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return -1;
            }
            ret = rs.getInt("chance");
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new DatabaseException("error getting create", e);
        }
          return ret;
    }
    
    public void dropnadditem(int dropper, int itemId, int min, int max, int questid, int chance){
        MapleMonsterInformationProvider.getInstance().clearDrops();
        ReactorScriptManager.getInstance().clearDrops();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO drop_data (`dropperid`, `itemid`, `minimum_quantity`, `maximum_quantity`, `questid`,  `chance`) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, dropper);
            ps.setInt(2, itemId);
            ps.setInt(3, min);
            ps.setInt(4, max);
            ps.setInt(5, questid);
            ps.setInt(6, chance);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("無法新增此物品" + e);
        }
       
    }
    
    public void zeroDrop(int itemId, int dropper){
        MapleMonsterInformationProvider.getInstance().clearDrops();
        ReactorScriptManager.getInstance().clearDrops();
        
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("Delete from drop_data WHERE itemid = ? and dropperid = ?");
            ps.setInt(1, itemId);
            ps.setInt(2, dropper);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("[Drop]無法連接資料庫");
        } catch (Exception ex) {
            FilePrinter.printError("調整掉寶.txt", ex, "newDropData");
            System.err.println("[Drop]" + ex);
        }     
       
    }
    
    public void newDropData(int itemId, int chance, int dropper, int quest){
        MapleMonsterInformationProvider.getInstance().clearDrops();
        ReactorScriptManager.getInstance().clearDrops();
        
        try {
             Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("Update drop_data set chance = ?,questid = ? Where itemid = ? and dropperid = ?");
            ps.setInt(1, chance);
            ps.setInt(2, quest);
            ps.setInt(3, itemId);
            ps.setInt(4, dropper);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("[Drop]無法連接資料庫");
        } catch (Exception ex) {
            FilePrinter.printError("調整掉寶.txt", ex, "newDropData");
            System.err.println("[Drop]" + ex);
        }     
       
    }
    
    public void refreshEq(Equip eq){
        c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.REMOVE, eq)));
        c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, eq)));
    }

}
