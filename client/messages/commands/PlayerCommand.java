package client.messages.commands;

import client.MapleCharacter;
import client.messages.CommandExecute;
import constants.GameConstants;
import client.MapleClient;
import client.MapleStat;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.ModifyInventory;
import constants.MapConstants;
import constants.PiPiConfig;
import constants.ServerConstants;
import constants.ServerConstants.PlayerGMRank;
import constants.WorldConstants;
import handling.channel.ChannelServer;
import scripting.NPCScriptManager;
import tools.MaplePacketCreator;
import server.life.MapleMonster;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import java.util.Arrays;
import tools.StringUtil;
import handling.world.World;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import scripting.AbstractPlayerInteraction;
import scripting.ReactorScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.ShutdownServer;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleMonsterInformationProvider;
import server.maps.MapleMap;
import server.swing.WvsCenter;
import tools.FilePrinter;
import tools.FileoutputUtil;
import tools.packet.MobPacket;
import tools.packet.UIPacket;

/**
 *
 * @author Emilyx3
 */
public class PlayerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return ServerConstants.PlayerGMRank.普通玩家;
    }

    /*public static class help extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
               String name = c.getPlayer().getName(); 
               c.sendPacket(UIPacket.summonHelper(true));
               c.sendPacket(UIPacket.summonMessage("您好#r" + name + "#k\r\n請點選我來進行各種說明\r\n希望您有快樂的一天!\r\n#d(若需要關閉我請打指令 - @close)#k"));
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("help - 幫助").toString();
        }
    }
    
    public static class close extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
               c.sendPacket(UIPacket.summonHelper(false));
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("close - 關閉幫助").toString();
        }
    }*/ //指令更改

    public abstract static class OpenNPCCommand extends CommandExecute {

        protected int npc = -1;
        private static final int[] npcs = { //Ish yur job to make sure these are in order and correct ;(
            9010017,
            9010000};

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            if (npc != 1 && c.getPlayer().getMapId() != 910000000) { //drpcash can use anywhere
                for (int i : GameConstants.blockedMaps) {
                    if (c.getPlayer().getMapId() == i) {
                        c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
                        return true;
                    }
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(1, "你的等級必須是10等.");
                    return true;
                }
                if (c.getPlayer().getMap().getSquadByMap() != null || c.getPlayer().getEventInstance() != null || c.getPlayer().getMap().getEMByMap() != null || c.getPlayer().getMapId() >= 990000000/* || FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())*/) {
                    c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
                    return true;
                }
                if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || (c.getPlayer().getMapId() / 100 == 1030008) || (c.getPlayer().getMapId() / 100 == 922010) || (c.getPlayer().getMapId() / 10 == 13003000)) {
                    c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
                    return true;
                }
            }
            NPCScriptManager.getInstance().start(c, npcs[npc]);
            return true;
        }
    }
    
    public static class 掉寶 extends OpenNPCCommand {

        public 掉寶() {
            npc = 1;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("掉寶 - 呼叫掉寶npc").toString();
        }
    }

    public static class save extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            try {
                int res = c.getPlayer().saveToDB(true, true);
                if (res == 1) {
                    c.getPlayer().dropMessage(5, "您的角色 : " + c.getPlayer().getName() + "　已於" + FilePrinter.getLocalDateString()  + "時儲存完畢。");
                } else {
                    c.getPlayer().dropMessage(5, "角色更新失敗，請詢問GM。");
                }
            } catch (UnsupportedOperationException ex) {

            }
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("save - 存檔").toString();
        }
    }

    public static class expfix extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            c.getPlayer().setExp(0);
            c.getPlayer().updateSingleStat(MapleStat.EXP, c.getPlayer().getExp());
            c.getPlayer().dropMessage(5, "已完成經驗修復，請您確認");
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("expfix - 經驗歸零").toString();
        }
    }

    public static class TSmega extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            c.getPlayer().setSmega();
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("TSmega - 開/關閉廣播").toString();
        }
    }
    
    

    public static class ea extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            c.removeClickedNPC();
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            /*
            c.sendPacket(UIPacket.summonHelper(true));
            c.sendPacket(UIPacket.summonMessage("#b已經為您解卡#k\r\n#d目前時間 : #k" + FilePrinter.getLocalDateString() + "\r\n#d基礎倍率(經驗/掉寶/楓幣) :#k " +  c.getChannelServer().getExpRate() + "/" +  c.getChannelServer().getDropRate() + "/" + c.getChannelServer().getMesoRate() 
            +"\r\n#d額外倍率(經驗/掉寶/楓幣) : #k" + (((Math.round(c.getPlayer().getEXPMod()) * 100) * Math.round(c.getPlayer().getStat().expBuff / 100.0) + (c.getPlayer().getStat().equippedFairy ? c.getPlayer().getFairyExp() : 0)))/100 + "/" + (Math.round(c.getPlayer().getDropMod() * (c.getPlayer().getStat().dropBuff / 100.0) * 100))/100 + "/"+ (Math.round((c.getPlayer().getStat().mesoBuff / 100.0) * 100))/100 
            +"\r\n#dVIP額外經驗 : #k"+ c.getPlayer().getVipExpRate() + "%"
            +"\r\n#d點數餘額(GASH/楓點) : #k\r\n" + c.getPlayer().getCSPoints(1) + "點/" + c.getPlayer().getCSPoints(2) + "點"
            +"\r\n#d使用能力捲數量 :#k" + c.getPlayer().getHpMpApUsed() + "張" 
            +"\r\n#r欲關閉此視窗請輸入指令 - @close#k"));*/
            
            c.getPlayer().dropMessage(6,"歡迎您，以下為伺服器與個人資訊－－");
            c.getPlayer().dropMessage(6, "目前時間" + FilePrinter.getLocalDateString());
            c.getPlayer().dropMessage(6, "伺服器經驗倍率 : "+ c.getChannelServer().getExpRate()+ " 伺服器掉寶基礎倍率 : "+ c.getChannelServer().getDropRate());
            c.getPlayer().dropMessage(6, "經驗值額外倍率 " + ((Math.round(c.getPlayer().getEXPMod()) * 100) * Math.round(c.getPlayer().getStat().expBuff / 100.0) + (c.getPlayer().getStat().equippedFairy ? c.getPlayer().getFairyExp() : 0)) + "%, 掉寶額外倍率 " + Math.round(c.getPlayer().getDropMod() * (c.getPlayer().getStat().dropBuff / 100.0) * 100) + "%, 楓幣額外倍率 " + Math.round((c.getPlayer().getStat().mesoBuff / 100.0) * 100) + "% VIP經驗加成：" + c.getPlayer().getVipExpRate() + "%");
            if (c.getChannelServer().getExExpRate() > 1 || c.getChannelServer().getExDropRate() > 1 || c.getChannelServer().getExMesoRate() > 1) {
                c.getPlayer().dropMessage(6, "額外經驗值倍率 " + (c.getChannelServer().getExExpRate()) + "倍, 掉寶倍率 " + (c.getChannelServer().getExDropRate()) + "倍, 楓幣倍率 " + (c.getChannelServer().getExMesoRate()) + "倍");
            }
            c.getPlayer().dropMessage(6, "擁有 " + c.getPlayer().getCSPoints(1) + " GASH " + c.getPlayer().getCSPoints(2) + " 楓葉點數 ");
            //c.getPlayer().dropMessage(6, "當前延遲 " + c.getPlayer().getClient().getLatency() + " 毫秒");
            c.getPlayer().dropMessage(6, "您已使用:" + c.getPlayer().getHpMpApUsed() + " 張能力重置捲");
            if (c.getPlayer().getLevel() >= 120 && c.getPlayer().getQuestStatus(29400) == 1) {
                c.getPlayer().dropMessage(6, "精明的獵人已經擊殺:" + c.getPlayer().getMobCount() + "隻怪物.");
            }
            if (c.getPlayer().isAdmin()) {
                c.getPlayer().dropMessage(6, "MAC: " + c.getPlayer().getNowMacs());
                c.getPlayer().dropMessage(6, "頻道: " + c.getChannel() + " ch2: " + World.Find.findChannel(c.getPlayer().getName()) + " ch3: " + World.Find.findChannel(c.getPlayer().getId()));
            }
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("ea - 解卡").toString();
        }

        public static String getDayOfWeek() {
            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
            String dd = String.valueOf(dayOfWeek);
            switch (dayOfWeek) {
                case 0:
                    dd = "日";
                    break;
                case 1:
                    dd = "一";
                    break;
                case 2:
                    dd = "二";
                    break;
                case 3:
                    dd = "三";
                    break;
                case 4:
                    dd = "四";
                    break;
                case 5:
                    dd = "五";
                    break;
                case 6:
                    dd = "六";
                    break;
            }
            return dd;
        }
        
        
        
    }

//    public static class JK extends CommandExecute {
//
//        @Override
//        public boolean execute(MapleClient c, String[] splitted) {
//            for (int i : GameConstants.blockedMaps) {
//                if (c.getPlayer().getMapId() == i) {
//                    c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
//                    return true;
//                }
//            }
//            if (c.getPlayer().getLevel() < 10) {
//                c.getPlayer().dropMessage(1, "你的等級必須是10等.");
//                return true;
//            }
//            if (c.getPlayer().getMap().getSquadByMap() != null || c.getPlayer().getEventInstance() != null || c.getPlayer().getMap().getEMByMap() != null || c.getPlayer().getMapId() >= 990000000/* || FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())*/) {
//                c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
//                return true;
//            }
//            if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || (c.getPlayer().getMapId() / 100 == 1030008) || (c.getPlayer().getMapId() / 100 == 922010) || (c.getPlayer().getMapId() / 10 == 13003000)) {
//                c.getPlayer().dropMessage(1, "你不能在這裡使用指令.");
//                return true;
//            }
//            InterServerHandler.EnterCashShop(c, c.getPlayer(), false);
//            return true;
//        }
//
//        @Override
//        public String getMessage() {
//            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("jk - 重製").toString();
//        }
//    }
    public static class mob extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            MapleMonster monster = null;
            for (final MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER))) {
                monster = (MapleMonster) monstermo;

                if (monster.isAlive()) {
                    c.getPlayer().dropMessage(6, "怪物 " + monster.toString() );
                }
            }
            if (monster == null) {
                c.getPlayer().dropMessage(6, "找不到地圖上的怪物");
            }
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("mob - 查看怪物狀態").toString();
        }
    }

    public static class CGM extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            boolean autoReply = false;

            if (splitted.length < 2) {
                return false;
            }
            String talk = StringUtil.joinStringFrom(splitted, 1);
            if (c.getPlayer().isGM()) {
                c.getPlayer().dropMessage(6, "因為你自己是GM所以無法使用此指令,可以嘗試!cngm <訊息> 來建立GM聊天頻道~");
            } else {
                if (!c.getPlayer().getCheatTracker().GMSpam(100000, 1)) { // 1 minutes.
                    boolean fake = false;
                    boolean showmsg = true;

                    // 管理員收不到，玩家有顯示傳送成功
                    if (PiPiConfig.getBlackList().containsKey(c.getAccID())) {
                        fake = true;
                    }

                    // 管理員收不到，玩家沒顯示傳送成功
                    if (talk.contains("搶") && talk.contains("圖")) {
                        c.getPlayer().dropMessage(1, "管理員不介入玩家糾紛");
                        fake = true;
                        showmsg = false;
                    } else if ((talk.contains("被") && talk.contains("騙")) || (talk.contains("點") && talk.contains("騙"))) {
                        c.getPlayer().dropMessage(1, "管理員因為遊戲公平性無法為此負責");
                        fake = true;
                        showmsg = false;
                    } else if ((talk.contains("被") && talk.contains("盜"))) {
                        c.getPlayer().dropMessage(1, "如有被盜帳號跡象，請確實堤共相關證據私訊粉絲團");
                        fake = true;
                        showmsg = false;
                    } else if (talk.contains("刪") && ((talk.contains("角") || talk.contains("腳")) && talk.contains("錯"))) {
                        c.getPlayer().dropMessage(1, "刪錯角色為己方過失，管理團隊無法做任何補償");
                        fake = true;
                        showmsg = false;
                    } else if (talk.contains("亂") && (talk.contains("名") && talk.contains("聲"))) {
                        c.getPlayer().dropMessage(1, "管理員不介入玩家糾紛");
                        fake = true;
                        showmsg = false;
                    } else if (talk.contains("密") && talk.contains("咒") && talk.contains("賣")) {
                        c.getPlayer().dropMessage(1, "密咒出售價格為1楓幣");
                        fake = true;
                        showmsg = false;
                    } else if (talk.contains("改") && talk.contains("密") && talk.contains("碼")) {
                        c.getPlayer().dropMessage(1, "需改密碼者請詳細提供相關證明後私訊粉絲團");
                        fake = true;
                        showmsg = false;
                    }

                    // 管理員收的到，自動回復
                    if (talk.toUpperCase().contains("VIP") && ((talk.contains("領") || (talk.contains("獲"))) && talk.contains("取"))) {
                        c.getPlayer().dropMessage(1, "VIP將會於儲值後一段時間後自行發放，請耐心等待");
                        autoReply = true;
                    } else if (talk.contains("貢獻") || talk.contains("666") || ((talk.contains("取") || talk.contains("拿") || talk.contains("發") || talk.contains("領")) && ((talk.contains("勳") || talk.contains("徽") || talk.contains("勛")) && talk.contains("章")))) {
                        c.getPlayer().dropMessage(1, "勳章請去點拍賣NPC案領取勳章\r\n如尚未被加入清單請耐心等候GM。");
                        autoReply = true;
                    } else if (((talk.contains("商人") || talk.contains("精靈")) && talk.contains("吃")) || (talk.contains("商店") && talk.contains("補償"))) {
                        c.getPlayer().dropMessage(1, "目前精靈商人裝備和楓幣有機率被吃\r\n如被吃了請務必將當時的情況完整描述給管理員\r\n\r\nPS: 不會補償任何物品");
                        autoReply = true;
                    } else if (talk.contains("檔") && talk.contains("案") && talk.contains("受") && talk.contains("損")) {
                        c.getPlayer().dropMessage(1, "檔案受損請重新解壓縮主程式唷");
                        autoReply = true;
                    } else if ((talk.contains("缺") || talk.contains("少")) && ((talk.contains("技") && talk.contains("能") && talk.contains("點")) || talk.toUpperCase().contains("SP"))) {
                        c.getPlayer().dropMessage(1, "缺少技能點請重練，沒有其他方法了唷");
                        autoReply = true;

                    } else if (talk.contains("母書")) {
                        if (talk.contains("火流星")) {
                            c.getPlayer().dropMessage(1, "技能[火流星] 並沒有母書唷");
                            autoReply = true;
                        }
                    } else if (talk.contains("黑符") && talk.contains("不") && (talk.contains("掉") || talk.contains("噴"))) {
                        MapleMonsterInformationProvider.getInstance().clearDrops();
                        ReactorScriptManager.getInstance().clearDrops();
                        c.getPlayer().dropMessage(1, "黑符掉落並非100%，再請您多嘗試");
                        autoReply = true;
                    } else if (talk.contains("鎖") && talk.contains("寶")) {
                        c.getPlayer().dropMessage(1, "本伺服器目前並未鎖寶\r\n只有尚未添加的掉寶資料或是掉落機率偏低");
                        autoReply = true;
                    }

                    if (showmsg) {
                        c.sendCGMLog(c, talk);
                        c.getPlayer().dropMessage(6, "訊息已經寄送給GM了!");
                    }

                    if (!fake) {
                        World.Broadcast.broadcastGMMessage(MaplePacketCreator.getItemNotice("[管理員幫幫忙]頻道 " + c.getPlayer().getClient().getChannel() + " 玩家 [" + c.getPlayer().getName() + "] (" + c.getPlayer().getId() + "): " + talk + (autoReply ? " -- (系統已自動回復)" : "")));
                        if (System.getProperty("StartBySwing") != null) {
                            WvsCenter.addChatLog("[管理員幫幫忙] " + c.getPlayer().getName() + ": " + StringUtil.joinStringFrom(splitted, 1) + (autoReply ? " -- (系統已自動回復)" : "") + "\r\n");
                        }
                    }

                    FileoutputUtil.logToFile("logs/data/管理員幫幫忙.txt", "\r\n " + FileoutputUtil.NowTime() + " 玩家[" + c.getPlayer().getName() + "] 帳號[" + c.getAccountName() + "]: " + talk + (autoReply ? " -- (系統已自動回復)" : "") + "\r\n");

                } else {
                    c.getPlayer().dropMessage(6, "為了防止對GM刷屏所以每1分鐘只能發一次.");
                }
            }
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("cgm - 跟GM回報").toString();
        }
    }

    public static class 賣裝 extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            if (splitted.length < 4) {
                return false;
            }
            MapleInventory inv;
            MapleInventoryType type;
            int totalMesosGained = 0;
            String Column = "null";
            int start = -1;
            int end = -1;
            try {
                Column = splitted[1];
                start = Integer.parseInt(splitted[2]);
                end = Integer.parseInt(splitted[3]);
            } catch (Exception ex) {
            }
            if (start == -1 || end == -1) {
                c.getPlayer().dropMessage("@清除道具 <1/2/3/4/5> <開始格數> <結束格數>");
                return true;
            }
            if (start < 1) {
                start = 1;
            }
            if (end > 96) {
                end = 96;
            }

            switch (Column) {
                case "1":
                    type = MapleInventoryType.EQUIP;
                    break;
                case "2":
                    type = MapleInventoryType.USE;
                    break;
                case "3":
                    type = MapleInventoryType.SETUP;
                    break;
                case "4":
                    type = MapleInventoryType.ETC;
                    break;
                case "5":
                    type = MapleInventoryType.CASH;
                    break;
                default:
                    type = null;
                    break;
            }
            if (type == null) {
                c.getPlayer().dropMessage("@清除道具 <1/2/3/4/5> <開始格數> <結束格數>");
                return true;
            }
            inv = c.getPlayer().getInventory(type);
            
            for (int i = start; i <= end; i++) {
                if (inv.getItem((short) i) != null) {
                    MapleItemInformationProvider iii = MapleItemInformationProvider.getInstance();
                    int itemPrice = (int) iii.getPrice(inv.getItem((short) i).getItemId());
                    int amount = inv.getItem((short) i).getQuantity();
                    int itemQ = (amount > 1 ? amount : 1);
                    totalMesosGained += (itemPrice * itemQ);
                    itemPrice = 0;
                    MapleInventoryManipulator.removeFromSlot(c, type, (short) i, inv.getItem((short) i).getQuantity(), true);
                }
            }
            c.getPlayer().gainMeso(totalMesosGained < 0 ? 0 : ((int)(Math.floor(totalMesosGained * 0.5))), true);
            FileoutputUtil.logToFile("logs/data/玩家指令.txt", "\r\n " + FileoutputUtil.NowTime() + " IP: " + c.getSession().remoteAddress().toString().split(":")[0] + " 帳號: " + c.getAccountName() + " 玩家: " + c.getPlayer().getName() + " 使用了指令 " + StringUtil.joinStringFrom(splitted, 0));
            c.getPlayer().dropMessage(6, "您已經清除了第 " + start + " 格到 " + end + "格的" + Column + "道具");
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("清除道具 <裝備欄/消耗欄/裝飾欄/其他欄/特殊欄> <開始格數> <結束格數>").toString();
        }
    }

    public static class jk_hm extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            c.getPlayer().RemoveHired();
            c.getPlayer().dropMessage("卡精靈商人已經解除");
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("jk_hm - 卡精靈商人解除").toString();
        }
    }

    public static class jcds extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            int gain = c.getPlayer().getMP();
            if (gain <= 0) {
                c.getPlayer().dropMessage("目前沒有任何在線點數唷。");
                return true;
            }
            if (splitted.length < 2) {
                c.getPlayer().dropMessage("目前紅利點數: " + c.getPlayer().getPoints());
                c.getPlayer().dropMessage("目前在線點數已經累積: " + gain + " 點，若要領取請輸入 @jcds true");
            } else if ("true".equals(splitted[1])) {
                gain = c.getPlayer().getMP();
                //c.getPlayer().modifyCSPoints(2, gain, true);
                c.getPlayer().setPoints(c.getPlayer().getPoints() + gain);
                c.getPlayer().setMP(0);
                c.getPlayer().saveToDB(false, false);
                c.getPlayer().dropMessage("領取了 " + gain + " 點在線點數, 目前紅利點數: " + c.getPlayer().getPoints());
            }
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("jcds - 領取在線點數").toString();
        }
    }

    public static class 在線點數 extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            int gain = c.getPlayer().getMP();
            if (gain <= 0) {
                c.getPlayer().dropMessage("目前沒有任何在線點數唷。");
                return true;
            }
            if (splitted.length < 2) {
                c.getPlayer().dropMessage("目前楓葉點數: " + c.getPlayer().getCSPoints(2));
                c.getPlayer().dropMessage("目前在線點數已經累積: " + gain + " 點，若要領取請輸入 @在線點數 是");
            } else if ("是".equals(splitted[1])) {
                gain = c.getPlayer().getMP();
                c.getPlayer().modifyCSPoints(2, gain, true);
                c.getPlayer().setMP(0);
                c.getPlayer().saveToDB(false, false);
                c.getPlayer().dropMessage("領取了 " + gain + " 點在線點數, 目前楓葉點數: " + c.getPlayer().getCSPoints(2));
            }
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("在線點數 - 領取在線點數").toString();
        }
    }
    
    public static class 輪迴 extends CommandExecute {
       
        
        @Override
        public boolean execute(MapleClient c, String splitted[]) {
            
            if(c.getPlayer().getMap().getRespawnTask()){
                c.getPlayer().dropMessage("此地圖已有輪迴增幅");
                return true;
            }
            
            if(c.getPlayer().getMap().isTown() || GameConstants.is輪迴(c.getPlayer().getMap().getId())){
                c.getPlayer().dropMessage("村莊或特定地圖無法使用輪迴");
                return true;
            }
            
            if(c.getPlayer().getItemQuantity(1112127,true) <= 0){
                c.getPlayer().dropMessage("您並沒有輪迴道具！");
                return true;
            }
            
            MapleMonster mob =  MapleLifeFactory.getMonster(100009);
            c.getPlayer().getMap().spawnMonster_sSack(mob, c.getPlayer().getPosition(), 0); //召喚
            c.getPlayer().startRespawn();

            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("輪迴 - 測試用").toString();
        }
    }
    
    /*
     public static class dpm extends CommandExecute {

     @Override
     public boolean execute(final MapleClient c, String splitted[]) {
     if (c.getPlayer().getMapId() == 100000000 && c.getPlayer().getLevel() >= 70 || !c.getPlayer().isGM()) {
     if (!c.getPlayer().isTestingDPS()) {
     c.getPlayer().toggleTestingDPS();
     c.getPlayer().dropMessage(5, "請持續攻擊怪物1分鐘，來測試您的每秒輸出！");
     final MapleMonster mm = MapleLifeFactory.getMonster(9001007);
     int distance = ((c.getPlayer().getJob() >= 300 && c.getPlayer().getJob() < 413) || (c.getPlayer().getJob() >= 1300 && c.getPlayer().getJob() < 1500) || (c.getPlayer().getJob() >= 520 && c.getPlayer().getJob() < 600)) ? 125 : 50;
     Point p = new Point(c.getPlayer().getPosition().x - distance, c.getPlayer().getPosition().y);
     mm.setBelongTo(c.getPlayer());
     final long newhp = Long.MAX_VALUE;
     OverrideMonsterStats overrideStats = new OverrideMonsterStats();
     overrideStats.setOHp(newhp);
     mm.setHp(newhp);
     mm.setOverrideStats(overrideStats);
     c.getPlayer().getMap().spawnMonsterOnGroundBelow(mm, p);
     final MapleMap nowMap = c.getPlayer().getMap();
     Timer.EventTimer.getInstance().schedule(new Runnable() {
     @Override
     public void run() {
     long health = mm.getHp();
     nowMap.killMonster1(mm);
     long dps = (newhp - health) / 15;
     if (dps > c.getPlayer().getDPS()) {
     c.getPlayer().dropMessage(6, "你的DPM是 " + dps + ". 這是一個新的紀錄！");
     c.getPlayer().setDPS(dps);
     c.getPlayer().savePlayer();
     c.getPlayer().toggleTestingDPS();
     } else {
     c.getPlayer().dropMessage(6, "你的DPM是 " + dps + ". 您目前的紀錄是 " + c.getPlayer().getDPS() + ".");
     c.getPlayer().toggleTestingDPS();
     }

     }
     }, 60000);
     } else {
     c.getPlayer().dropMessage(5, "請先把你的這回DPM測試完畢。");
     return true;
     }
     } else {
     c.getPlayer().dropMessage(5, "只能在弓箭手村測試DPM，並且等級符合70以上。");
     return true;
     }
     return true;
     }

     @Override
     public String getMessage() {
     return new StringBuilder().append("").toString();
     }
     }
     EnterCashShop
     public static final void EnterCashShop(final MapleClient c, final MapleCharacter chr, final boolean mts) {
     if (res == 1) {
     chr.dropMessage(5, "角色保存成功！");
     }
     if (chr.isTestingDPS()) {
     final MapleMonster mm = MapleLifeFactory.getMonster(9001007);
     if(chr.getMap() != null)
     chr.getMap().Killdpm(true);
     chr.toggleTestingDPS();
     chr.dropMessage(5, "已停止當前的DPM測試。");
     }

    
     */
    public static class fm extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            if(c.getPlayer().getEventInstance()!=null){
               c.getPlayer().dropMessage(5,"於副本中無法使用");
            }else if(c.getPlayer().getLevel() >= 10){
               final MapleMap warpz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(910000000);
               c.getPlayer().changeMap(warpz,warpz.getPortal(0));
            }else{
               c.getPlayer().dropMessage(5,"10等以上才能使用這個指令喔!");
            }
            return true;
        }
        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("fm - 到自由廣場").toString();
        }
    }
    
    public abstract static class DistributeStatCommands extends CommandExecute {

        private static int statLim = 32767;
        private static int statLow = 4;
        private static int LOW = 0;
        protected MapleStat stat = null;

        private void setStat(MapleCharacter chr, int amount) {
            switch (stat) {
                case STR:
                    chr.getStat().setStr((short) amount);
                    chr.updateSingleStat(MapleStat.STR, chr.getStat().getStr());
                    break;
                case DEX:
                    chr.getStat().setDex((short) amount);
                    chr.updateSingleStat(MapleStat.DEX, chr.getStat().getDex());
                    break;
                case INT:
                    chr.getStat().setInt((short) amount);
                    chr.updateSingleStat(MapleStat.INT, chr.getStat().getInt());
                    break;
                case LUK:
                    chr.getStat().setLuk((short) amount);
                    chr.updateSingleStat(MapleStat.LUK, chr.getStat().getLuk());
                    break;
            }
        }

        private int getStat(MapleCharacter chr) {
            switch (stat) {
                case STR:
                    return chr.getStat().getStr();
                case DEX:
                    return chr.getStat().getDex();
                case INT:
                    return chr.getStat().getInt();
                case LUK:
                    return chr.getStat().getLuk();
                default:
                    throw new RuntimeException(); //Will never happen.
            }
        }

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return false;
            }
            int change = 0;
            try {
                change = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException nfe) {
                return false;
            }
            if (change <= 0) {
                c.getPlayer().dropMessage(6, "您必需輸入大於0的數字.");
                return true;
            }
            if (LOW == 1 && c.getPlayer().getRemainingAp() != 0 && change < 0) {
                c.getPlayer().dropMessage("您的能力值尚未重製完，還剩下" + c.getPlayer().getRemainingAp() + "點沒分配");
                return true;
            } else {
                LOW = 0;
            }
            if (c.getPlayer().getRemainingAp() < change) {
                c.getPlayer().dropMessage(6, "您的AP不足.");
                return true;
            }
            if (getStat(c.getPlayer()) + change > statLim) {
                c.getPlayer().dropMessage(6, "能力值不能高於 " + statLim + ".");
                return true;
            }
            if (getStat(c.getPlayer()) + change < statLow) {
                c.getPlayer().dropMessage("能力值不能低於 " + statLow + ".");
                return true;
            }
            setStat(c.getPlayer(), getStat(c.getPlayer()) + change);
            c.getPlayer().setRemainingAp((short) (c.getPlayer().getRemainingAp() - change));
            c.getPlayer().updateSingleStat(MapleStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
            int a = change;
            if (change < 0) {
                c.getPlayer().dropMessage("重製AP完成，現在有" + c.getPlayer().getRemainingAp() + "點可以分配");
                LOW = 1;
            }
            int b = Math.abs(a);
            c.getPlayer().dropMessage((change >= 0 ? "增加" : "減少") + stat.name() + b + "點");
            FileoutputUtil.logToFile("logs/data/玩家快速點能力指令.txt", "\r\n " + FileoutputUtil.NowTime() + " IP: " + c.getSession().remoteAddress().toString().split(":")[0] + " 帳號: " + c.getAccountName() + " 玩家: " + c.getPlayer().getName() + " 使用了指令 " + StringUtil.joinStringFrom(splitted, 0) + (change >= 0 ? "增加" : "減少") + stat.name() + b + "點");
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("STR/DEX/INT/LUK <數量> 快速點能力值").toString();
        }
    }
    
    public static class STR extends DistributeStatCommands {

        public STR() {
            stat = MapleStat.STR;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("STR <數量> - 快速點力量").toString();
        }
    }

    public static class DEX extends DistributeStatCommands {

        public DEX() {
            stat = MapleStat.DEX;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("DEX <數量> - 快速點敏捷").toString();
        }
    }

    public static class INT extends DistributeStatCommands {

        public INT() {
            stat = MapleStat.INT;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("INT <數量> - 快速點智力").toString();
        }
    }

    public static class LUK extends DistributeStatCommands {

        public LUK() {
            stat = MapleStat.LUK;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("LUK <數量> - 快速點幸運").toString();
        }
    }
    
    public static class 吸 extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String splitted[]) {
            
            final int item = 1112127;
            if(GameConstants.isBossMap(c.getPlayer().getMapId())){
                c.getPlayer().dropMessage("王圖無法使用吸怪道具");
            }else if(c.getPlayer().getMap().getMobsSize() < 1){
                c.getPlayer().dropMessage("您所在的地圖不需要使用此道具");
            }else if(c.getPlayer().getItemQuantity(item,true) < 1){
                c.getPlayer().dropMessage("您所擁有的吸物道具不足");
            }else {
               for (final MapleMapObject mmo : c.getPlayer().getMap().getAllMonstersThreadsafe()) {
                    final MapleMonster monster = (MapleMonster) mmo;
                    if(!monster.getStats().isBoss() && monster.getId() != 100009){
                        c.getPlayer().getMap().broadcastMessage(MobPacket.moveMonster(false, 0, 0, monster.getObjectId(), monster.getPosition(), c.getPlayer().getPosition(), c.getPlayer().getLastRes()));
                    }
                    monster.setPosition(c.getPlayer().getPosition());
               }
               
            }
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("吸怪 - 全圖吸怪").toString();
        }
    }
    
    public static class 附魔 extends CommandExecute {
        @Override
        public  boolean execute(MapleClient c, String[] splitted) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (splitted.length < 3) {
                return false;
            }
            
            Random rand = new Random();
            byte slot = (byte) Integer.parseInt(splitted[2]);
            Item item = (Item) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
           
            String mode = splitted[1];
            int itemid = 0;
            int main = 0;
            int atk = 0;
            if("初".equals(mode)){
                itemid = 5062801;
                main = 2;
                atk = 2; //0
            }else if("高".equals(mode)){
                itemid = 5062802;
                main = 5;
                atk = 5; // 4-5
            }
            
            
            
            Equip eq = (Equip) item;
            final short us = eq.getUpgradeSlots(); //衝捲數
            final short str = eq.getStr();
            final short dex = eq.getDex();
            final short Int = eq.getInt();
            final short luk = eq.getLuk();
            final short watk = eq.getWatk();
            final short matk = eq.getMatk();
            
            
            
            final int checkItem = c.getPlayer().itemQuantity(itemid);
            int upgradeLimit = itemid == 5062800 ? 20 : 50;
            
            if(!ii.isCash(item.getItemId())){
                c.getPlayer().dropMessage("請確認選取裝備為點數裝備");
                return true;
            }
            
           
            
            
            if(checkItem <= 0){
                c.getPlayer().dropMessage("請確認您的附魔石數量");
                return true;
            }
            
            if(us >= upgradeLimit){
                c.getPlayer().dropMessage("目前單樣點妝最多附魔 :" + upgradeLimit + " 次");
                return true;
            }
            
            /*if(item.getItemId() >= 1802000 && item.getItemId() < 1820000){
                c.getPlayer().dropMessage("寵物點妝無法附魔");
                return true;
            }*/
            
            
            eq.setStr((short)(str + main));
            eq.setDex((short)(dex + main));
            eq.setInt((short)(Int + main));
            eq.setLuk((short)(luk + main));
            eq.setWatk((short)(watk + atk));
            eq.setMatk((short)(matk + atk));
            eq.setUpgradeSlots((byte)(us+1));
            eq.setOwner("" + String.valueOf((int)(eq.getUpgradeSlots())) + " 次附魔");
            MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(itemid), itemid, 1, true, false);
                        //MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(ctype), ctype, 1, true, false);
            c.getPlayer().forceReAddItem_NoUpdate(eq, MapleInventoryType.EQUIP);
            c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.REMOVE, eq)));
            c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, eq)));
            c.getPlayer().dropMessage("已成功完成附魔");
            
                      
            return true;
        }

        @Override
        public  String getMessage() {
            return new StringBuilder().append(PlayerGMRank.普通玩家.getCommandPrefix()).append("附魔<初/中/高/頂> <格數> -- 附魔道具").toString();
        }
    }
    
    public static class back extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            if(c.getPlayer().getLevel() > 10){
               final MapleMap warpz = ChannelServer.getInstance(c.getPlayer().getDCChannel()).getMapFactory().getMap(c.getPlayer().getDCMap());
               if(c.getPlayer().getDCMap() <= 0 || warpz == null){
                    c.getPlayer().dropMessage(5,"無法獲取斷線紀錄");
                    return true;
               }
               
               Iterator<MapleMonster> mob = warpz.getAllMonstersThreadsafe().iterator();
               //int bossCheck = 0;
               while (mob.hasNext()){
                   MapleMonster boss = mob.next();
                   if(boss.getStats().isBoss() && boss.getStats().getLevel() > 100){
                       //bossCheck ++;
                   }
               }
               
                  if (!MapConstants.isBossMap(warpz.getId())){
                     c.getPlayer().dropMessage(5,"回傳僅限至王圖");
                  }else{
                     c.getPlayer().changeMap(warpz,warpz.getPortal(0));
                     c.getPlayer().dropMessage(5,"[回傳] 已為您傳送至您的斷線地圖頻道 " + warpz.getChannel() + " : " + warpz.getMapName() + "");
                  }
            }else{
               c.getPlayer().dropMessage(5,"10等以上才能使用這個指令喔!");
            }
            return true;
        }
        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("back - 回到斷線地圖").toString();
        }
    }
    
    public static class 轉移 extends CommandExecute {
        @Override
        public  boolean execute(MapleClient c, String[] splitted) {
            if(splitted.length < 3){
                return false;
            }
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            byte slot1 = (byte) Integer.parseInt(splitted[1]); //有素質的
            byte slot2 = (byte) Integer.parseInt(splitted[2]); //轉移到的
            Item item1 = (Item) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot1);
            Item item2 = (Item) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot2);
            boolean cloth = false;
            if(item1 == null ||item2 == null ){
                c.getPlayer().dropMessage("請確認輸入皆有對應裝備");
                return true;
            }
            if(!ii.isCash(item1.getItemId()) || !ii.isCash(item2.getItemId())){
                c.getPlayer().dropMessage("請確認選取道具皆為點數裝備");
                return true;
            }
            if ((item1.getItemId() / 10000 == 104 && item2.getItemId() / 10000 == 105)||(item2.getItemId() / 10000 == 104 && item1.getItemId() / 10000 == 105)){
                cloth = true;
            }
            if((item1.getItemId()/10000 != item2.getItemId()/10000) && !cloth){
                c.getPlayer().dropMessage("請確認選取道具屬於同一部位道具");
                return true;
            }
                        
            if(item1.getExpiration()!=-1 || item2.getExpiration()!=-1){
                c.getPlayer().dropMessage("您選擇的道具有包含時效裝備");
                return true;
            }
           
            Equip eq1 = (Equip) item1;
            Equip eq2 = (Equip) item2;
            final short str1 = eq1.getStr();
            final short str2 = eq2.getStr();
            final short dex1 = eq1.getDex();
            final short dex2 = eq2.getDex();
            final short int1 = eq1.getInt();
            final short int2 = eq2.getInt();
            final short luk1 = eq1.getLuk();
            final short luk2 = eq2.getLuk();
            final short hp1 = eq1.getHp();
            final short hp2 = eq2.getHp();
            final short mp1 = eq1.getMp();
            final short mp2 = eq2.getMp();
            final short watk1 = eq1.getWatk();
            final short watk2 = eq2.getWatk();
            final short matk1 = eq1.getMatk();
            final short matk2 = eq2.getMatk();
            final short wdef1 = eq1.getWdef();
            final short wdef2 = eq2.getWdef();
            final short mdef1 = eq1.getMdef();
            final short mdef2 = eq2.getMdef();
            final short acc1 = eq1.getAcc();
            final short acc2 = eq2.getAcc();
            final short av1 = eq1.getAvoid();
            final short av2 = eq2.getAvoid();
            final short u1 = eq1.getUpgradeSlots();
            final short u2 = eq2.getUpgradeSlots();
            final String o1 = eq1.getOwner();
            final String o2 = eq2.getOwner();
            eq1.setStr(str2);
            eq2.setStr(str1);
            eq1.setDex(dex2);
            eq2.setDex(dex1);
            eq1.setInt(int2);
            eq2.setInt(int1);
            eq1.setLuk(luk2);
            eq2.setLuk(luk1);
            eq1.setHp(hp2);
            eq2.setHp(hp1);
            eq1.setMp(mp2);
            eq2.setMp(mp1);
            eq1.setWatk(watk2);
            eq2.setWatk(watk1);
            eq1.setMatk(matk2);
            eq2.setMatk(matk1);
            eq1.setWdef(wdef2);
            eq2.setWdef(wdef1);
            eq1.setMdef(mdef2);
            eq2.setMdef(mdef1);
            eq1.setAcc(acc2);
            eq2.setAcc(acc1);
            eq1.setAvoid(av2);
            eq2.setAvoid(av1);
            eq1.setUpgradeSlots((byte)u2);
            eq2.setUpgradeSlots((byte)u1);
            eq1.setOwner(o2);
            eq2.setOwner(o1);
            
            String n1 = MapleItemInformationProvider.getInstance().getName(eq1.getItemId());
            String n2 = MapleItemInformationProvider.getInstance().getName(eq2.getItemId());
            
            c.getPlayer().forceReAddItem_NoUpdate(eq1, MapleInventoryType.EQUIP);
            c.getPlayer().forceReAddItem_NoUpdate(eq2, MapleInventoryType.EQUIP);
            
            
            c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.REMOVE, eq1)));
            c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, eq1)));
            c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.REMOVE, eq2)));
            c.sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, eq2)));
            
            c.getPlayer().dropMessage("[重要提醒] " + n1 + " 與 " + n2 + " 素質交換成功，請確認");
            

           return true;
        }

        @Override
        public  String getMessage() {
            return new StringBuilder().append(PlayerGMRank.普通玩家.getCommandPrefix()).append("轉移 <格數1> <格數2> - 交換點妝素質").toString();
        }
    }
    
    public static class 卡經驗 extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String splitted[]) {
            if(c.getPlayer().getExp() == c.getPlayer().getNeededExp()){
                c.getPlayer().gainExp(1,true,false,true);
                c.getPlayer().dropMessage("經驗已成功修復");
            }else{
                c.getPlayer().dropMessage("目前使用不上(於經驗99.9%使用)");
            }
            
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("卡經驗 - 修復卡99.9%").toString();
        }
    }
    
    public static class dice extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String splitted[]) {
            int rand = Randomizer.nextInt(6) + 1;
            c.getPlayer().getMap().mapMessage(6, c.getPlayer().getName() + " 骰出了數字 [" + rand + "] !");
            
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.普通玩家.getCommandPrefix()).append("dice - 隨機骰").toString();
        }
    }

 }


