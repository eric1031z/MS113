/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sever.filter;

import client.MapleCharacter;
import client.inventory.MapleInventory;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import server.MapleInventoryManipulator;
import tools.MaplePacketCreator;

/**
 *
 * @author timis
 */
public class MapleFilter {
    
    
    public static int getFilterItem(int cid, int itemid){
        int id = 0;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT itemid FROM itemfilter WHERE cid = ? and itemid = ?");
            ps.setInt(1, cid);
            ps.setInt(2, itemid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                } else {
                    id = 0;
                }
            }
            ps.close();
            return id;
        } catch (SQLException ex) {
            return 0;
        }
    }
    
    public static void getFilter(int cid, List<Integer> item){
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM itemfilter WHERE cid = ?");
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                item.add(rs.getInt("itemid"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void setFilterItem(int cid, int itemid){
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps;
            ps = con.prepareStatement("insert into itemfilter (cid, itemid, counts) values (?, ?, ?)");
            ps.setInt(1, cid);
            ps.setInt(2, itemid); 
            ps.setInt(3, 0);
            ps.executeUpdate();
            ps.close();
        } catch (Exception Ex) {
                
        }
    }
    
    public static int getHasFilteredCount(int cid, int itemid){
        int filtered = 0;
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT counts FROM itemfilter WHERE cid = ? and itemid = ?");
            ps.setInt(1, cid);
            ps.setInt(2, itemid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    filtered = rs.getInt(1);
                } else {
                    filtered = 0;
                }
            }
            ps.close();
            return filtered;
        } catch (SQLException ex) {
            return 0;
        }
    }
    
    public static void updateHasFilteredCount(int cid, int itemid, int set){
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("update itemfilter set counts = ? where cid = ? and itemid = ?");
            ps.setInt(1, getHasFilteredCount(cid,itemid) + set);
            ps.setInt(2, cid);
            ps.setInt(3, itemid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }
    }
    
    public static void deleteFilterItem(int cid, int itemid){
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("delete from itemfilter WHERE cid = ? and itemid = ?")){
            ps.setInt(1, cid);
            ps.setInt(2, itemid);
            ps.executeUpdate();
            ps.close();
        }catch(SQLException ex){
            ex.getStackTrace();
        }
    }
    
    public static String getAllFilter(int cid){
        String msg= "";
        int a = 0;
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT * from itemfilter where cid = ?")){
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               a++;
               msg += "#L" + rs.getInt("itemid") + "# #r物品 "+ a +":#k #d#t" + rs.getInt("itemid") + "##k\r\n" ;
            }
            rs.close();
            ps.close();
            return msg;
        }catch(SQLException ex){
            return "";
        }
    }
    
    
}
