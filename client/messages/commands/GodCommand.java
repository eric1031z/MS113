package client.messages.commands;

import client.messages.CommandExecute;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleStat;
import client.PlayerStats;
import client.SkillFactory;
import constants.PiPiConfig;
import constants.ServerConstants;
import constants.ServerConstants.PlayerGMRank;
import constants.WorldConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.channel.handler.AttackInfo;
import handling.channel.handler.MaplePvp;
import handling.world.World;
import static java.lang.Math.random;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import server.ShutdownServer;
import server.Timer;
import server.life.MapleMonster;
import server.life.MapleMonsterPet;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;

/**
 *
 * @author benq
 */
public class GodCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.神;
    }

    public static class Buff extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String splitted[]) {
            MapleCharacter player = c.getPlayer();
            SkillFactory.getSkill(9001002).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001003).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001008).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001001).getEffect(1).applyTo(player);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("Buff - 施放管理BUFF").toString();
        }
    }
    
    public static class mpet extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String splitted[]) {
            
            new MapleMonsterPet(Integer.parseInt(splitted[1]), c.getPlayer()).setSkills(Integer.parseInt(splitted[2])); //..試試看吧
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("mpet - 恩").toString();
        }
    }
    
     public static class 序號產生 extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String splitted[]) {
           
              
              for(int o=0; o<= Integer.parseInt(splitted[1]) ; o++){
                   try (Connection con1 = DatabaseConnection.getConnection()) {
                    PreparedStatement ps;
                    ps = con1.prepareStatement("insert into nxcode (code,type,item,size,time) values (?,?,?,?,?)");
                    ps.setString(1,c.getPlayer().nxcodeGen(20));
                    ps.setInt(2,3);
                    ps.setInt(3,Integer.parseInt(splitted[2]));
                    ps.setInt(4,1);
                    ps.setInt(5,1);
                    ps.executeUpdate();
                    ps.close();
                    con1.close();
                } catch (Exception Ex) {
                         System.err.println("Error while insert nxcode." + Ex);
                 }
                   
              }
              
              return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("序號產生 幾個 換什麼 - 產生商城亂碼序號").toString();
        }
    }
    
     

    public static class MinStats extends CommandExecute {

        public boolean execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            player.getStat().setHp(50);
            player.getStat().setMp(50);
            player.getStat().setMaxHp((short) 50);
            player.getStat().setMaxMp((short) 50);
            player.getStat().setStr((short) 4);
            player.getStat().setDex((short) 4);
            player.getStat().setInt((short) 4);
            player.getStat().setLuk((short) 4);
            player.setLevel((short) 10);
            player.updateSingleStat(MapleStat.HP, 50);
            player.updateSingleStat(MapleStat.MP, 50);
            player.updateSingleStat(MapleStat.MAXHP, 50);
            player.updateSingleStat(MapleStat.MAXMP, 50);
            player.updateSingleStat(MapleStat.STR, 4);
            player.updateSingleStat(MapleStat.DEX, 4);
            player.updateSingleStat(MapleStat.INT, 4);
            player.updateSingleStat(MapleStat.LUK, 4);
            player.updateSingleStat(MapleStat.LEVEL, 10);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("MinStats - 預設屬性").toString();
        }
    }

    public static class Maxstats extends CommandExecute {

        public boolean execute(MapleClient c, String splitted[]) {

            MapleCharacter player = c.getPlayer();
            player.getStat().setHp(30000);
            player.getStat().setMp(30000);
            player.getStat().setMaxHp((short) 30000);
            player.getStat().setMaxMp((short) 30000);
            player.getStat().setStr(Short.MAX_VALUE);
            player.getStat().setDex(Short.MAX_VALUE);
            player.getStat().setInt(Short.MAX_VALUE);
            player.getStat().setLuk(Short.MAX_VALUE);
            player.setLevel((short) 199);
            player.updateSingleStat(MapleStat.HP, 30000);
            player.updateSingleStat(MapleStat.MP, 30000);
            player.updateSingleStat(MapleStat.MAXHP, 30000);
            player.updateSingleStat(MapleStat.MAXMP, 30000);
            player.updateSingleStat(MapleStat.STR, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.DEX, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.INT, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.LUK, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.LEVEL, 199);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("Maxstats - 滿屬性").toString();
        }
    }

    public static class BanCommand extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            PiPiConfig.setCommandLock(!PiPiConfig.getCommandLock());
            c.getPlayer().dropMessage("指令封鎖: " + PiPiConfig.getCommandLock());
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("BanCommand - 封鎖指令").toString();
        }
    }

    public static class face extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return false;
            }
            id = Integer.parseInt(splitted[1]);
            player.setFace(id);
            player.updateSingleStat(MapleStat.FACE, id);
            player.dropMessage(5, "您當前臉型的ＩＤ已被改為: " + id);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("Face <臉型代碼> - 修改臉型").toString();
        }
    }

    public static class hair extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return false;
            }
            id = Integer.parseInt(splitted[1]);
            player.setHair(id);
            player.updateSingleStat(MapleStat.HAIR, id);
            player.dropMessage(5, "您當前髮型的ＩＤ已被改為: " + id);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("Hair <髮型代碼> - 修改髮型").toString();
        }
    }

    public static class Str extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return false;
            }
            id = Integer.parseInt(splitted[1]);
            player.setStr(id);
            player.updateSingleStat(MapleStat.STR, id);
            player.dropMessage(5, "您當前力量已被改為: " + id);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("Str <能力值> - 修改能力值").toString();
        }
    }

    public static class Int extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return false;
            }
            id = Integer.parseInt(splitted[1]);
            player.setInt(id);
            player.updateSingleStat(MapleStat.INT, id);
            player.dropMessage(5, "您當前智力已被改為: " + id);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("Int <能力值> - 修改能力值").toString();
        }
    }

    public static class Luk extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return false;
            }
            id = Integer.parseInt(splitted[1]);
            player.setLuk(id);
            player.updateSingleStat(MapleStat.LUK, id);
            player.dropMessage(5, "您當前幸運已被改為: " + id);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("Luk <能力值> - 修改能力值").toString();
        }
    }

    public static class Dex extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return false;
            }
            id = Integer.parseInt(splitted[1]);
            player.setDex(id);
            player.updateSingleStat(MapleStat.DEX, id);
            player.dropMessage(5, "您當前敏捷已被改為: " + id);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("Luk <能力值> - 修改能力值").toString();
        }
    }

    public static class HP extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return false;
            }
            id = Integer.parseInt(splitted[1]);
            player.setHp(id);
            player.setMaxHp(id);
            player.updateSingleStat(MapleStat.HP, id);
            player.updateSingleStat(MapleStat.MAXHP, id);
            player.dropMessage(5, "您當前HP已被改為: " + id);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("HP <能力值> - 修改能力值").toString();
        }
    }
    public static class 改玩家 extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return false;
            }
            String Victim = splitted[1];
            String after = splitted[2];
            if(c.getChannelServer().getPlayerStorage().getCharacterByName(Victim)==null){
                c.getPlayer().dropMessage(6,"此玩家不在線上");
            }
            else if(after.length() > 12){
                c.getPlayer().dropMessage(6,"您輸入的名字太長");
            }
            
            else if (after.length() <= 12 && c.getChannelServer().getPlayerStorage().getCharacterByName(Victim)!=null) {
                c.getChannelServer().getPlayerStorage().getCharacterByName(Victim).setName(after);
                c.getChannelServer().getPlayerStorage().getCharacterByName(after).fakeRelog();
            }
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("改玩家 <玩家名字>  <新名字> - 改玩家名字").toString();
        }
    }

    public static class MP extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            int id = 0;
            if (splitted.length < 2) {
                return false;
            }
            id = Integer.parseInt(splitted[1]);
            player.setMp(id);
            player.setMaxMp(id);
            player.updateSingleStat(MapleStat.MP, id);
            player.updateSingleStat(MapleStat.MAXMP, id);
            player.dropMessage(5, "您當前MP已被改為: " + id);
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("MP <能力值> - 修改能力值").toString();
        }
    }
    
    public static class 給紅利 extends CommandExecute {

        @Override
        public boolean execute(MapleClient c, String splitted[]) {
            if (splitted.length < 3) {
                return false;
            }
            String error = null;
            String name = splitted[1];
            int gain = Integer.parseInt(splitted[2]);
            
            
            if (error != null) {
                c.getPlayer().dropMessage(error);
                return true;
            }

            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage("玩家必須上線");
                return true;
            }
            MapleCharacter victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            if (victim == null) {
                c.getPlayer().dropMessage("找不到此玩家");
            } else {
                c.getPlayer().dropMessage("已經給予玩家[" + name + "] " + gain + " 點紅利");
                victim.dropMessage("您已獲得"  + gain + " 點紅利" );
                FileoutputUtil.logToFile("logs/data/給予紅利點數.txt", "\r\n " + FileoutputUtil.NowTime() + " GM " + c.getPlayer().getName() + " 給了 " + victim.getName() + "  " + gain + "點紅利");
                victim.setDonatePoints(gain);
            }
            return true;
        }

        @Override
        public String getMessage() {
            return new StringBuilder().append(ServerConstants.PlayerGMRank.神.getCommandPrefix()).append("給紅利 玩家名稱 數量").toString();
        }
    }
}
