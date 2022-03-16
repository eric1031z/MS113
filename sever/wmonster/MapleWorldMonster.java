/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sever.wmonster;

import client.MapleCharacter;
import database.DatabaseConnection;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import sever.wmonster.MapleWorldMonsterControl;
import client.MapleClient;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.FileoutputUtil;


/**
 *
 * @author timis
 */
public class MapleWorldMonster {
    private static final List<MapleWorldMonsterControl> wm = new LinkedList();
    private static MapleClient client;

    public static void loadWM() {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM worldmonster");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               wm.add(new MapleWorldMonsterControl(
                        rs.getInt("mobid"),
                        rs.getInt("hp"),
                        rs.getInt("mp"),
                        rs.getInt("watk"),
                        rs.getInt("matk"),
                        rs.getInt("chance"),
                        rs.getInt("prize"),
                        rs.getInt("prizecount"),
                        rs.getInt("prizeneed"),
                        rs.getInt("bonus"),
                        rs.getInt("bonuscount"),
                        rs.getInt("currentmap"),
                        rs.getTimestamp("lastappear"))
                );
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }
    
    public static void reloadWM(){
        wm.clear();
        loadWM();
    }
    
    public static void newWM(int mobid, int hp, int mp, int watk, int matk, int chance, int prize, int prizecount, int prizeneed, int bonus, int bonuscount, Timestamp lastappear){
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO worldmonster (mobid,hp,mp,watk,matk,chance,prize,prizecount,prizeneed,bonus,bonuscount,lastappear) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, mobid);
            ps.setInt(2, hp);
            ps.setInt(3, mp);
            ps.setInt(4, watk);
            ps.setInt(5, matk);
            ps.setInt(6, chance);
            ps.setInt(7, prize);
            ps.setInt(8, prizecount);
            ps.setInt(9, prizeneed);
            ps.setInt(10, bonus);
            ps.setInt(11, bonuscount);
            ps.setTimestamp(12, null);
            ps.executeUpdate();
            ps.close();
        }catch(Exception e){
                       
        }
        MapleWorldMonster.reloadWM();
    } 
    
    
    public static void setWM(String name, int mobid, int set) {
       try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("update worldmonster set " + name +  " = ? where mobid = ?");
            ps.setInt(1, set);
            ps.setInt(2, mobid);
            ps.executeUpdate();
            ps.close();
            ps.close();
        } catch (SQLException ex) {
        }
        MapleWorldMonster.reloadWM();
    }
    
    public static void setWMTime(int mobid, Timestamp set) {
       try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("update worldmonster set lastappear = ? where mobid = ?");
            ps.setTimestamp(1, set);
            ps.setInt(2, mobid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }
        MapleWorldMonster.reloadWM();
    }
    
    public static int getWM(String name, int mobid) {
        int para;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT " + name +  " FROM worldmonster WHERE mobid = ?");
            ps.setInt(1, mobid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    para = rs.getInt(1);
                } else {
                    para = 0;
                }
            }
            ps.close();
            return para;
        } catch (SQLException ex) {
            return 0;
        }
    }
    
    public static int getRandomWM() {
        int para;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT mobid FROM worldmonster WHERE hp > 0 ORDER BY RAND() LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    para = rs.getInt(1);
                } else {
                    para = 0;
                }
            }
            ps.close();
            return para;
        } catch (SQLException ex) {
            return 0;
        }
    }
    
    public static Timestamp getWMTime(int mobid) {
        Timestamp para;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT lastappear FROM worldmonster WHERE mobid = ?");
            ps.setInt(1, mobid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    para = rs.getTimestamp(1);
                } else {
                    para = null;
                }
            }
            ps.close();
            return para;
        } catch (SQLException ex) {
            return null;
        }
    }
    
    public static void getAllTown(MapleClient c,int type){
        String msg = "";
        
       
        int a = 36;
        for(int q = 1 ; q < type ; q++){
        MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Map.wz/Map/Map" + q));
        MapleDataDirectoryEntry root = data.getRoot();
        for (MapleDataFileEntry topDir : root.getFiles()) {
            int id = Integer.parseInt(topDir.getName().substring(0, 9));
            
            try{
                MapleMap m =  c.getChannelServer().getMapFactory().getMap(id);
                if(m.getAllMonster().size() == 0 && m.getAllNPCs().size() < 3 && !m.isTown()){
                    a++;
                    msg += "MAP" + a + "(" + id + ", " + Math.round(Math.floor((Math.random()*100))) + ", " + Math.round(Math.floor(Math.random()*10)) + ", 0, new Point(" + (m.getPortal(0).getPosition().x -150) + ", " + m.getPortal(0).getPosition().y + "), new Point(" + (m.getPortal(0).getPosition().x + 150) + ", " + m.getPortal(0).getPosition().y + ")),\r\n";
                }
            }catch(Exception e){
                msg += String.valueOf(id) + "錯誤 !";
            }
        }
        }
         FileoutputUtil.logToFile("logs/地圖讀取.txt", msg);
    }
    
    public static int getWMLog(int cid, int mobid, String name) {
        Connection con = DatabaseConnection.getConnection();
        try {
            int ret_count;
            PreparedStatement ps;
            ps = con.prepareStatement("select " + name + " from wmlog where cid = ? and mobid = ?");
            ps.setInt(1, cid);
            ps.setInt(2, mobid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret_count = rs.getInt(1);
                } else {
                    ret_count = 0;
                }
            }
            ps.close();
            return ret_count;
        } catch (Exception Ex) {
            //log.error("Error while read bosslog.", Ex);
            return 0;
        }
    }
    
    public static Long getMaxWM(int mobid) {
        Connection con = DatabaseConnection.getConnection();
        try {
            Long ret_count;
            PreparedStatement ps;
            ps = con.prepareStatement("select max(damage) from wmlog where mobid = ?");
            ps.setInt(1, mobid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret_count = rs.getLong(1);
                } else {
                    ret_count = (long)0;
                }
            }
            ps.close();
            return ret_count;
        } catch (Exception Ex) {
            //log.error("Error while read bosslog.", Ex);
            return (long) 0;
        }
    }
    
    public static int getMaxWMId(int mobid) {
        Connection con = DatabaseConnection.getConnection();
        try {
            int ret_count;
            PreparedStatement ps;
            ps = con.prepareStatement("select cid from wmlog where damage = (select max(damage) from wmlog where mobid = ?) and mobid = ?");
            ps.setInt(1, mobid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret_count = rs.getInt(1);
                } else {
                    ret_count = 0;
                }
            }
            ps.close();
            return ret_count;
        } catch (Exception Ex) {
            //log.error("Error while read bosslog.", Ex);
            return 0;
        }
    }
    
    public static String getMaxWMChar(int mobid) {
        Connection con = DatabaseConnection.getConnection();
        try {
            int ret_count;
            PreparedStatement ps;
            ps = con.prepareStatement("select cid from wmlog where damage = (select max(damage) from wmlog where mobid = ?) and mobid = ?");
            ps.setInt(1, mobid);
            ps.setInt(2, mobid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret_count = rs.getInt(1);
                } else {
                    ret_count = 0;
                }
            }
            ps.close();
            return MapleCharacter.getCharacterNameById(ret_count);
        } catch (Exception Ex) {
            //log.error("Error while read bosslog.", Ex);
            return "" ;
        }
    }
    
    public static void clearWM(int mobid) {
        
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("update wmlog set damage = 0 where mobid = ?");
            ps.setInt(1, mobid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }
        
    }
    
    public static void clearAllCurrentWM() {
        
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("update worldmonster set currentmap = 0, lastappear = current_timestamp");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }
        
    }
    
    public static void clearWMPrize(int mobid) {
        
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("update wmlog set percentage = 0 where mobid = ?");
            ps.setInt(1, mobid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }
        
    }
    
    public static void setWMLog(int cid, int set, int mobid, String name) {
        Connection con = DatabaseConnection.getConnection();
        if(getWMLog(cid, mobid, "damage") == 0){
            try {
                PreparedStatement ps;
                ps = con.prepareStatement("insert into wmlog (cid, mobid, damage, percentage) values (?, ?, ?, ?)");
                ps.setInt(1, cid);
                ps.setInt(2, mobid);
                ps.setInt(3, 0); //超級綠水靈
                ps.setInt(4, 0);
                ps.executeUpdate();
                ps.close();
             } catch (Exception Ex) {
                //   log.error("Error while insert bosslog.", Ex);
             }
        }else{
            try {
                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("update wmlog set " + name + " = ? where cid = ? AND mobid = ?");
                ps.setInt(1, set);
                ps.setInt(2, cid);
                ps.setInt(3, mobid);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
            }
        }
    }
    
    
    
}
