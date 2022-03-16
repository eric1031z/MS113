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
package handling.world;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedHashMap;
import java.util.Map;

import client.inventory.MapleMount;
import client.MapleCharacter;
import client.MapleQuestStatus;
import client.ISkill;
import client.SkillEntry;
import client.BuddyEntry;
import client.MapleClans;
import client.inventory.MaplePet;
import server.quest.MapleQuest;
import tools.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharacterTransfer implements Externalizable {

    public int characterid, accountid, exp,
            beans, meso, hair, face, mapid, guildid,
            partyid, messengerid, mBookCover, dojo, MaplePoints,
            mount_itemid, mount_exp, points, vpoints, marriageId,
            familyid, seniorid, junior1, junior2, currentrep, totalrep, expression, constellation, blood, month, day, battleshipHP, gachexp,
            addMove, MobCount;
    public byte channel, dojoRecord, gender, gmLevel, guildrank, alliancerank, clonez, fairyExp, buddysize, world, initialSpawnPoint, skinColor, mount_level, mount_Fatigue, subcategory;
    public long lastfametime, TranferTime, lastaddmovetime;
    public long[] lastTime;
    public String name, accountname, BlessOfFairy, chalkboard, charmessage, prefix, nowmacs;
    public short level, fame, str, dex, int_, luk, maxhp, maxmp, hp, mp, remainingAp, hpApUsed, job;
    public Object inventorys, skillmacro, storage, cs;
    public int[] savedlocation, wishlist, rocks, remainingSp, regrocks, savedHairs, savedFaces, lottonumbers;
    public byte[] petStore;
    public HashMap<Integer, Long> LeftSkillTime = new HashMap<>();
    public Map<Integer, Integer> mbook = new LinkedHashMap<>();
    public Map<Integer, Pair<Byte, Integer>> keymap = new LinkedHashMap<>();
    public final List<Integer> finishedAchievements = new ArrayList<>(), famedcharacters = new ArrayList<>();
    public final Map<BuddyEntry, Boolean> buddies = new LinkedHashMap<>();
    public final Map<Integer, Object> Quest = new LinkedHashMap<>(); // Questid instead of MapleQuest, as it's huge. Cant be transporting MapleQuest.java
    public Map<Integer, String> InfoQuest = new LinkedHashMap<>();
    public final Map<Integer, SkillEntry> Skills = new LinkedHashMap<>(); // Skillid instead of Skill.java, as it's huge. Cant be transporting Skill.java and MapleStatEffect.java
    public boolean check_msg_BuyMerChant, check_msg_Chat, check_msg_MobVac, Vip_Medal, DebugMessage, canTalk, smega, showLogin, showRegister, attackdebug;
    public int reborn;
    public int csType;
    public int damageSkin, dcChannel, dcMap;

    public CharacterTransfer() {
    }

    public CharacterTransfer(final MapleCharacter chr) {
        this.addMove = chr.getAddMove();
        this.lastTime = chr.getCheatTracker().getLastGMspam();
        this.showRegister = chr.getShow(1);
        this.showLogin = chr.getShow(2);
        this.nowmacs = chr.getNowMacs();
        this.canTalk = chr.getCanTalk();
        this.attackdebug = chr.getAttackDebugMessage();
        this.DebugMessage = chr.getDebugMessage();
        this.check_msg_MobVac = chr.getmsg_MobVac();
        this.Vip_Medal = chr.getVipMedal();
        this.check_msg_Chat = chr.getmsg_Chat();
        this.check_msg_BuyMerChant = chr.getmsg_HiredMerchant();
        this.smega = chr.getSmega();
        this.characterid = chr.getId();
        this.accountid = chr.getAccountID();
        this.accountname = chr.getClient().getAccountName();
        this.channel = (byte) chr.getClient().getChannel();
        this.MaplePoints = chr.getCSPoints(2);
        this.vpoints = chr.getVPoints();
        this.name = chr.getName();
        this.fame = chr.getFame();
        this.gender = (byte) chr.getGender();
        this.level = chr.getLevel();
        this.str = chr.getStat().getStr();
        this.dex = chr.getStat().getDex();
        this.int_ = chr.getStat().getInt();
        this.luk = chr.getStat().getLuk();
        this.hp = chr.getStat().getHp();
        this.mp = chr.getStat().getMp();
        this.maxhp = chr.getStat().getMaxHp();
        this.maxmp = chr.getStat().getMaxMp();
        this.exp = chr.getExp();
        this.hpApUsed = chr.getHpMpApUsed();
        this.remainingAp = chr.getRemainingAp();
        this.remainingSp = chr.getRemainingSps();
        this.beans = chr.getBeans();
        this.meso = chr.getMeso();
        this.skinColor = chr.getSkinColor();
        this.job = chr.getJob();
        this.hair = chr.getHair();
        this.face = chr.getFace();
        this.mapid = chr.getMapId();
        this.initialSpawnPoint = chr.getInitialSpawnpoint();
        this.marriageId = chr.getMarriageId();
        this.world = chr.getWorld();
        this.guildid = chr.getGuildId();
        this.guildrank = (byte) chr.getGuildRank();
        this.alliancerank = (byte) chr.getAllianceRank();
        this.gmLevel = (byte) chr.getGMLevel();
        this.points = chr.getPoints();
        this.fairyExp = chr.getFairyExp();
        this.clonez = chr.getNumClones();
        this.petStore = chr.getPetStores();
        this.subcategory = chr.getSubcategory();
        this.currentrep = chr.getCurrentRep();
        this.totalrep = chr.getTotalRep();
        this.familyid = chr.getFamilyId();
        this.seniorid = chr.getSeniorId();
        this.junior1 = chr.getJunior1();
        this.junior2 = chr.getJunior2();
        this.charmessage = chr.getcharmessage();
        this.expression = chr.getexpression();
        this.constellation = chr.getconstellation();
        this.blood = chr.getblood();
        this.month = chr.getmonth();
        this.day = chr.getday();
        this.battleshipHP = chr.getCurrentBattleshipHP();
        this.prefix = chr.getPrefix();
        this.gachexp = chr.getGachExp();
        this.MobCount = chr.getMobCount();
        this.savedFaces = chr.getSavedFaces();
        this.savedHairs = chr.getSavedHairs();
        this.lottonumbers = chr.getLottoNumbers();
        this.reborn = chr.getReborns();
        this.csType = chr.getCSType();
        this.damageSkin = chr.getDamageSkin();
        this.dcChannel = chr.getDCChannel();
        this.dcMap = chr.getDCMap();
        
        boolean uneq = false;
        for (int i = 0; i < this.petStore.length; i++) {
            final MaplePet pet = chr.getPet(i);
            if (this.petStore[i] == 0) {
                this.petStore[i] = (byte) -1;
            }
            if (pet != null) {
                uneq = true;
                this.petStore[i] = (byte) Math.max(this.petStore[i], pet.getInventoryPosition());
            }

        }
        if (uneq) {
            chr.unequipAllPets(false, false);
        }
        for (final BuddyEntry qs : chr.getBuddylist().getBuddies()) {
            this.buddies.put(qs, qs.isVisible());
        }
        this.buddysize = chr.getBuddyCapacity();

        this.partyid = chr.getPartyId();

        if (chr.getMessenger() != null) {
            this.messengerid = chr.getMessenger().getId();
        } else {
            this.messengerid = 0;
        }

        /*for (final Integer zz : chr.getFinishedAchievements()) {
         this.finishedAchievements.add(zz);
         }*/
        this.mBookCover = chr.getMonsterBookCover();
        this.dojo = chr.getDojo();
        this.dojoRecord = (byte) chr.getDojoRecord();

        this.InfoQuest = chr.getInfoQuest_Map();

        for (final Map.Entry<MapleQuest, MapleQuestStatus> qs : chr.getQuest_Map().entrySet()) {
            this.Quest.put(qs.getKey().getId(), qs.getValue());
        }

        this.mbook = chr.getMonsterBook().getCards();
        this.inventorys = chr.getInventorys();

        for (final Map.Entry<ISkill, SkillEntry> qs : chr.getSkills().entrySet()) {
            this.Skills.put(qs.getKey().getId(), qs.getValue());
        }
        for (final Map.Entry<Integer, Long> qs : chr.getSkillWorkings().entrySet()) {
            this.LeftSkillTime.put(qs.getKey(), qs.getValue());
        }
        this.BlessOfFairy = chr.getBlessOfFairyOrigin();
        this.chalkboard = chr.getChalkboard();
        this.skillmacro = chr.getMacros();
        this.keymap = chr.getKeyLayout().Layout();
        this.savedlocation = chr.getSavedLocations();
        this.wishlist = chr.getWishlist();
        this.rocks = chr.getRocks();
        this.regrocks = chr.getRegRocks();
        for (final Integer zz : chr.getFamedCharacters()) {
            this.famedcharacters.add(zz);
        }
        this.lastaddmovetime = chr.getLastAddMoveTime();
        this.lastfametime = chr.getLastFameTime();
        this.storage = chr.getStorage();
        this.cs = chr.getCashInventory();

        final MapleMount mount = chr.getMount();
        this.mount_itemid = mount.getItemId();
        this.mount_Fatigue = mount.getFatigue();
        this.mount_level = mount.getLevel();
        this.mount_exp = mount.getExp();

        TranferTime = System.currentTimeMillis();
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.addMove = in.readInt();
        this.lastTime = (long[]) in.readObject();
        this.showRegister = in.readBoolean();
        this.showLogin = in.readBoolean();
        this.nowmacs = in.readUTF();
        this.canTalk = in.readBoolean();
        this.attackdebug = in.readBoolean();
        this.DebugMessage = in.readBoolean();
        this.check_msg_MobVac = in.readBoolean();
        this.Vip_Medal = in.readBoolean();
        this.check_msg_Chat = in.readBoolean();
        this.check_msg_BuyMerChant = in.readBoolean();
        this.smega = in.readBoolean();
        this.characterid = in.readInt();
        this.accountid = in.readInt();
        this.accountname = in.readUTF();
        this.channel = in.readByte();
        this.MaplePoints = in.readInt();
        this.name = in.readUTF();
        this.fame = in.readShort();
        this.gender = in.readByte();
        this.level = in.readShort();
        this.str = in.readShort();
        this.dex = in.readShort();
        this.int_ = in.readShort();
        this.luk = in.readShort();
        this.hp = in.readShort();
        this.mp = in.readShort();
        this.maxhp = in.readShort();
        this.maxmp = in.readShort();
        this.exp = in.readInt();
        this.hpApUsed = in.readShort();
        this.remainingAp = in.readShort();
        this.remainingSp = new int[in.readByte()];
        for (int i = 0; i < this.remainingSp.length; i++) {
            this.remainingSp[i] = in.readInt();
        }
        this.savedHairs = new int[in.readByte()];
        for (int i = 0; i < this.savedHairs.length; i++) {
            this.savedHairs[i] = in.readInt();
        }
        this.savedFaces = new int[in.readByte()];
        for (int i = 0; i < this.savedFaces.length; i++) {
            this.savedFaces[i] = in.readInt();
        }
        this.lottonumbers = new int[in.readByte()];
        for (int i = 0; i < this.lottonumbers.length; i++) {
            this.lottonumbers[i] = in.readInt();
        }
        this.beans = in.readInt();
        this.meso = in.readInt();
        this.skinColor = in.readByte();
        this.job = in.readShort();
        this.hair = in.readInt();
        this.face = in.readInt();
        this.mapid = in.readInt();
        this.initialSpawnPoint = in.readByte();
        this.world = in.readByte();
        this.guildid = in.readInt();
        this.guildrank = in.readByte();
        this.alliancerank = in.readByte();
        this.gmLevel = in.readByte();
        this.points = in.readInt();
        this.vpoints = in.readInt();
        if (in.readByte() == 1) {
            this.BlessOfFairy = in.readUTF();
        } else {
            this.BlessOfFairy = null;
        }
        if (in.readByte() == 1) {
            this.chalkboard = in.readUTF();
        } else {
            this.chalkboard = null;
        }
        this.clonez = in.readByte();
        this.skillmacro = in.readObject();
        this.lastaddmovetime = in.readLong();
        this.lastfametime = in.readLong();
        this.storage = in.readObject();
        this.cs = in.readObject();
        this.mount_itemid = in.readInt();
        this.mount_Fatigue = in.readByte();
        this.mount_level = in.readByte();
        this.mount_exp = in.readInt();
        this.partyid = in.readInt();
        this.messengerid = in.readInt();
        this.mBookCover = in.readInt();
        this.dojo = in.readInt();
        this.dojoRecord = in.readByte();
        this.inventorys = in.readObject();
        this.fairyExp = in.readByte();
        this.subcategory = in.readByte();
        this.marriageId = in.readInt();
        this.familyid = in.readInt();
        this.seniorid = in.readInt();
        this.junior1 = in.readInt();
        this.junior2 = in.readInt();
        this.currentrep = in.readInt();
        this.totalrep = in.readInt();
        this.charmessage = in.readUTF();
        this.expression = in.readByte();
        this.constellation = in.readInt();
        this.blood = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.battleshipHP = in.readInt();
        this.prefix = in.readUTF();
        this.gachexp = in.readInt();
        this.MobCount = in.readInt();
        this.reborn = in.readInt();
        this.csType = in.readInt();
        this.damageSkin = in.readInt();
        this.dcChannel = in.readInt();
        this.dcMap = in.readInt();
        
        final int mbooksize = in.readShort();
        for (int i = 0; i < mbooksize; i++) {
            this.mbook.put(in.readInt(), in.readInt());
        }

        final int skillsize = in.readShort();
        for (int i = 0; i < skillsize; i++) {
            this.Skills.put(in.readInt(), new SkillEntry(in.readByte(), in.readByte(), in.readLong()));
        }
        final int skillWorksize = in.readShort();
        for (int i = 0; i < skillWorksize; i++) {
            this.LeftSkillTime.put(in.readInt(), in.readLong());
        }
        this.buddysize = in.readByte();
        final short addedbuddysize = in.readShort();
        for (int i = 0; i < addedbuddysize; i++) {
            buddies.put(new BuddyEntry(in.readUTF(), in.readInt(), in.readUTF(), in.readInt(), in.readBoolean()), in.readBoolean());
        }

        final int questsize = in.readShort();
        for (int i = 0; i < questsize; i++) {
            this.Quest.put(in.readInt(), in.readObject());
        }

        final int achievesize = in.readShort();
        for (int i = 0; i < achievesize; i++) {
            this.finishedAchievements.add(in.readInt());
        }

        final int famesize = in.readInt();
        for (int i = 0; i < famesize; i++) {
            this.famedcharacters.add(in.readInt());
        }

        final int savesize = in.readShort();
        savedlocation = new int[savesize];
        for (int i = 0; i < savesize; i++) {
            savedlocation[i] = in.readInt();
        }

        final int wsize = in.readShort();
        wishlist = new int[wsize];
        for (int i = 0; i < wsize; i++) {
            wishlist[i] = in.readInt();
        }

        final int rsize = in.readShort();
        rocks = new int[rsize];
        for (int i = 0; i < rsize; i++) {
            rocks[i] = in.readInt();
        }

        final int resize = in.readShort();
        regrocks = new int[resize];
        for (int i = 0; i < resize; i++) {
            regrocks[i] = in.readInt();
        }

        final int infosize = in.readShort();
        for (int i = 0; i < infosize; i++) {
            this.InfoQuest.put(in.readInt(), in.readUTF());
        }

        final int keysize = in.readInt();
        for (int i = 0; i < keysize; i++) {
            this.keymap.put(in.readInt(), new Pair<>(in.readByte(), in.readInt()));
        }
        this.petStore = new byte[in.readByte()];
        for (int i = 0; i < 3; i++) {
            this.petStore[i] = in.readByte();
        }
        TranferTime = System.currentTimeMillis();
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(this.addMove);
        out.writeObject(this.lastTime);
        out.writeBoolean(this.showLogin);
        out.writeBoolean(this.showRegister);
        out.writeUTF(this.nowmacs);
        out.writeBoolean(this.canTalk);
        out.writeBoolean(this.attackdebug);
        out.writeBoolean(this.DebugMessage);
        out.writeBoolean(this.check_msg_MobVac);
        out.writeBoolean(this.Vip_Medal);
        out.writeBoolean(this.check_msg_BuyMerChant);
        out.writeBoolean(this.check_msg_Chat);
        out.writeBoolean(this.smega);
        out.writeInt(this.characterid);
        out.writeInt(this.accountid);
        out.writeUTF(this.accountname);
        out.writeByte(this.channel);
        out.writeInt(this.MaplePoints);
        out.writeUTF(this.name);
        out.writeShort(this.fame);
        out.writeByte(this.gender);
        out.writeShort(this.level);
        out.writeShort(this.str);
        out.writeShort(this.dex);
        out.writeShort(this.int_);
        out.writeShort(this.luk);
        out.writeShort(this.hp);
        out.writeShort(this.mp);
        out.writeShort(this.maxhp);
        out.writeShort(this.maxmp);
        out.writeInt(this.exp);
        out.writeShort(this.hpApUsed);
        out.writeShort(this.remainingAp);
        out.writeByte(this.remainingSp.length);
        for (int i = 0; i < this.remainingSp.length; i++) {
            out.writeInt(this.remainingSp[i]);
        }
        out.writeByte(this.savedHairs.length);
        for (int i = 0; i < this.savedHairs.length; i++) {
            out.writeInt(this.savedHairs[i]);
        }
        out.writeByte(this.savedFaces.length);
        for (int i = 0; i < this.savedFaces.length; i++) {
            out.writeInt(this.savedFaces[i]);
        }
        out.writeByte(this.lottonumbers.length);
        for (int i = 0; i < this.lottonumbers.length; i++) {
            out.writeInt(this.lottonumbers[i]);
        }
        out.writeInt(this.beans);
        out.writeInt(this.meso);
        out.writeByte(this.skinColor);
        out.writeShort(this.job);
        out.writeInt(this.hair);
        out.writeInt(this.face);
        out.writeInt(this.mapid);
        out.writeByte(this.initialSpawnPoint);
        out.writeByte(this.world);
        out.writeInt(this.guildid);
        out.writeByte(this.guildrank);
        out.writeByte(this.alliancerank);
        out.writeByte(this.gmLevel);
        out.writeInt(this.points);
        out.writeInt(this.vpoints);
        out.writeByte(this.BlessOfFairy == null ? 0 : 1);
        if (this.BlessOfFairy != null) {
            out.writeUTF(this.BlessOfFairy);
        }
        out.writeByte(this.chalkboard == null ? 0 : 1);
        if (this.chalkboard != null) {
            out.writeUTF(this.chalkboard);
        }
        out.writeByte(this.clonez);

        out.writeObject(this.skillmacro);
        out.writeLong(this.lastaddmovetime);
        out.writeLong(this.lastfametime);
        out.writeObject(this.storage);
        out.writeObject(this.cs);
        out.writeInt(this.mount_itemid);
        out.writeByte(this.mount_Fatigue);
        out.writeByte(this.mount_level);
        out.writeInt(this.mount_exp);
        out.writeInt(this.partyid);
        out.writeInt(this.messengerid);
        out.writeInt(this.mBookCover);
        out.writeInt(this.dojo);
        out.writeByte(this.dojoRecord);
        out.writeObject(this.inventorys);
        out.writeByte(this.fairyExp);
        out.writeByte(this.subcategory);
        out.writeInt(this.marriageId);
        out.writeInt(this.familyid);
        out.writeInt(this.seniorid);
        out.writeInt(this.junior1);
        out.writeInt(this.junior2);
        out.writeInt(this.currentrep);
        out.writeInt(this.totalrep);
        out.writeInt(this.battleshipHP);
        out.writeUTF(this.charmessage);
        out.writeInt(this.expression);
        out.writeInt(this.constellation);
        out.writeInt(this.blood);
        out.writeInt(this.month);
        out.writeInt(this.day);
        out.writeUTF(this.prefix);
        out.writeInt(this.gachexp);
        out.writeInt(this.MobCount);
        out.writeInt(this.reborn); 
        out.writeInt(this.csType);
        out.writeInt(this.damageSkin);
        out.writeInt(this.dcChannel);
        out.writeInt(this.dcMap);
 

        out.writeShort(this.mbook.size());
        for (Map.Entry<Integer, Integer> ms : this.mbook.entrySet()) {
            out.writeInt(ms.getKey());
            out.writeInt(ms.getValue());
        }

        out.writeShort(this.Skills.size());
        for (final Map.Entry<Integer, SkillEntry> qs : this.Skills.entrySet()) {
            out.writeInt(qs.getKey()); // Questid instead of Skill, as it's huge :(
            out.writeByte(qs.getValue().skillevel);
            out.writeByte(qs.getValue().masterlevel);
            out.writeLong(qs.getValue().expiration);
            // Bless of fairy is transported here too.
        }
        out.writeShort(this.LeftSkillTime.size());
        for (final Map.Entry<Integer, Long> qs : this.LeftSkillTime.entrySet()) {
            out.writeInt(qs.getKey()); // Questid instead of Skill, as it's huge :(
            out.writeLong(qs.getValue());
            // Bless of fairy is transported here too.
        }
        out.writeByte(this.buddysize);
        out.writeShort(this.buddies.size());
        for (final Map.Entry<BuddyEntry, Boolean> qs : this.buddies.entrySet()) {
            out.writeUTF(qs.getKey().getName());
            out.writeInt(qs.getKey().getCharacterId());
            out.writeUTF(qs.getKey().getGroup());
            out.writeInt(qs.getKey().getChannel());
            out.writeBoolean(qs.getValue());
            out.writeBoolean(qs.getValue());
        }

        out.writeShort(this.Quest.size());
        for (final Map.Entry<Integer, Object> qs : this.Quest.entrySet()) {
            out.writeInt(qs.getKey()); // Questid instead of MapleQuest, as it's huge :(
            out.writeObject(qs.getValue());
        }

        out.writeShort(this.finishedAchievements.size());
        for (final Integer zz : finishedAchievements) {
            out.writeInt(zz);
        }

        out.writeInt(this.famedcharacters.size());
        for (final Integer zz : famedcharacters) {
            out.writeInt(zz);
        }

        out.writeShort(this.savedlocation.length);
        for (int zz : savedlocation) {
            out.writeInt(zz);
        }

        out.writeShort(this.wishlist.length);
        for (int zz : wishlist) {
            out.writeInt(zz);
        }

        out.writeShort(this.rocks.length);
        for (int zz : rocks) {
            out.writeInt(zz);
        }

        out.writeShort(this.regrocks.length);
        for (int zz : regrocks) {
            out.writeInt(zz);
        }

        out.writeShort(this.InfoQuest.size());
        for (final Map.Entry<Integer, String> qs : this.InfoQuest.entrySet()) {
            out.writeInt(qs.getKey());
            out.writeUTF(qs.getValue());
        }

        out.writeInt(this.keymap.size());
        for (final Map.Entry<Integer, Pair<Byte, Integer>> qs : this.keymap.entrySet()) {
            out.writeInt(qs.getKey());
            out.writeByte(qs.getValue().left);
            out.writeInt(qs.getValue().right);
        }
        out.writeByte(petStore.length);
        for (int i = 0; i < petStore.length; i++) {
            out.writeByte(petStore[i]);
        }
    }
}
