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
package server.life;

import client.MapleCharacter;
import client.MonsterBook;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import database.DatabaseConnection;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

public class MapleMonsterInformationProvider {

    private static final MapleMonsterInformationProvider instance = new MapleMonsterInformationProvider();
    private final Map<Integer, String> mobCache = new HashMap<>();
    private final Map<Integer, List<MonsterDropEntry>> drops = new HashMap<>();
    private final List<MonsterGlobalDropEntry> globaldrops = new ArrayList<>();
    private final Map<Integer, Integer> cards = new HashMap<>();
    public MapleCharacter chr ;

    protected MapleMonsterInformationProvider() {
        retrieveGlobal();
    }

    public static final MapleMonsterInformationProvider getInstance() {
        return instance;
    }

    public final List<MonsterGlobalDropEntry> getGlobalDrop() {
        return globaldrops;
    }

    public Map<Integer, String> getAllMonsters() {
        if (mobCache.isEmpty()) {
            final MapleDataProvider stringData = MapleDataProviderFactory.getDataProvider("String.wz");
            MapleData mobsData = stringData.getData("Mob.img");
            for (MapleData itemFolder : mobsData.getChildren()) {
                mobCache.put(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME"));
            }
        }
        return mobCache;
    }
    
   
    
    public int getDropQuest(int monsterId) {        PreparedStatement ps = null;
        ResultSet rs = null;
        int quest = 0;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT questid FROM drop_data where dropperid = ?");
            ps.setInt(1, monsterId);
            rs = ps.executeQuery();
            while (rs.next()) {
                quest = rs.getInt("questid");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            
        }
        return quest;
    }
    
    public int getMonsterCardByMonster(int monsterId){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cardid = 0;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT itemid FROM drop_data where dropperid = ? AND FLOOR(itemid/10000) = 238");
            ps.setInt(1, monsterId);
            rs = ps.executeQuery();
            while (rs.next()) {
               cardid = rs.getInt("itemid");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
        }
        return cardid;
    }
    
    
    public int getDropChance(int monsterId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int chance = 0;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT chance FROM drop_data where dropperid = ?");
            ps.setInt(1, monsterId);
            rs = ps.executeQuery();
            while (rs.next()) {
               
                chance = rs.getInt("chance");
                
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            
        }
        return chance;
   }
   
    
   public List<Integer> getMobByItem(final int itemId) {
       final List<Integer> mobs = new LinkedList<Integer>() {};
       PreparedStatement ps = null;
       ResultSet rs = null;
       try {
           ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM drop_data WHERE itemid = ?");
           ps.setInt(1, itemId);
           rs = ps.executeQuery();
           int mobid;
           while (rs.next()) {
               mobid = rs.getInt("dropperid");
               if (!mobs.contains(mobid))
               mobs.add(mobid);
           }
           ps.close();
           rs.close();
       } catch (SQLException e) {
           e.printStackTrace();
           } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignore) {
                return null;
            }
       }
       return mobs;
   }

    private void retrieveGlobal() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            final Connection con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM drop_data_global WHERE chance > 0");
            rs = ps.executeQuery();

            while (rs.next()) {
                globaldrops.add(
                        new MonsterGlobalDropEntry(
                                rs.getInt("itemid"),
                                rs.getInt("chance"),
                                rs.getInt("continent"),
                                rs.getByte("dropType"),
                                rs.getInt("minimum_quantity"),
                                rs.getInt("maximum_quantity"),
                                rs.getShort("questid")));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving drop" + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignore) {
            }
        }
    }

    public final List<MonsterDropEntry> retrieveDrop(final int monsterId) {
        if (drops.containsKey(monsterId)) {
            return drops.get(monsterId);
        }
        final List<MonsterDropEntry> ret = new LinkedList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM drop_data WHERE dropperid = ?");
            ps.setInt(1, monsterId);
            rs = ps.executeQuery();
            int itemid, chance;
            while (rs.next()) {
                itemid = rs.getInt("itemid");
                chance = rs.getInt("chance");
                /*if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
                 chance *= 10; //in GMS/SEA it was raised
                 }*/
                ret.add(new MonsterDropEntry(
                        itemid,
                        chance,
                        rs.getInt("minimum_quantity"),
                        rs.getInt("maximum_quantity"),
                        rs.getShort("questid")));
            }
        } catch (SQLException e) {
            return ret;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignore) {
                return ret;
            }
        }
        drops.put(monsterId, ret);
        return ret;
    }

    public final void clearDrops() {
        drops.clear();
        globaldrops.clear();
        retrieveGlobal();
    }
}
