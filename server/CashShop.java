/*
 This file is part of the ZeroFusion MapleStory Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 ZeroFusion organized by "RMZero213" <RMZero213@hotmail.com>

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
package server;

import client.MapleCharacter;
import java.io.Serializable;
import client.inventory.Equip;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import client.inventory.IItem;
import constants.GameConstants;
import client.inventory.MaplePet;
import client.inventory.Item;
import client.inventory.ItemLoader;
import client.MapleClient;
import client.inventory.MapleRing;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import database.DatabaseConnection;
import tools.packet.MTSCSPacket;
import tools.Pair;

public class CashShop implements Serializable {

    private static final long serialVersionUID = 231541893513373579L;
    private int accountId, characterId;
    private ItemLoader factory;
    private List<IItem> inventory = new ArrayList<>();
    private List<Integer> uniqueids = new ArrayList<>();

    public CashShop(int accountId, int characterId, int jobType) throws SQLException {
        this.accountId = accountId;
        this.characterId = characterId;

        if (jobType / 1000 == 1) {
            factory = ItemLoader.CASHSHOP_CYGNUS;
        } else if ((jobType / 100 == 21 || jobType / 100 == 20) && jobType != 2001) {
            factory = ItemLoader.CASHSHOP_ARAN;
        } else if (jobType == 2001 || jobType / 100 == 22) {
            factory = ItemLoader.CASHSHOP_EVAN;
        } else if (jobType >= 3000) {
            factory = ItemLoader.CASHSHOP_RESIST;
        } else if (jobType / 10 == 43) {
            factory = ItemLoader.CASHSHOP_DB;
        } else {
            factory = ItemLoader.CASHSHOP_EXPLORER;
        }

        for (Pair<IItem, MapleInventoryType> item : factory.loadItems(false, accountId).values()) {
            inventory.add(item.getLeft());
        }
    }

    public int getItemsSize() {
        return inventory.size();
    }

    public List<IItem> getInventory() {
        return inventory;
    }

    public IItem findByCashId(int cashId) {
        for (IItem item : inventory) {
            if (item.getUniqueId() == cashId) {
                return item;
            }
        }

        return null;
    }

    public void checkExpire(MapleClient c) {
        List<IItem> toberemove = new ArrayList<>();
        for (IItem item : inventory) {
            if (item != null && !GameConstants.isPet(item.getItemId()) && item.getExpiration() > 0 && item.getExpiration() < System.currentTimeMillis()) {
                toberemove.add(item);
            }
        }
        if (toberemove.size() > 0) {
            for (IItem item : toberemove) {
                removeFromInventory(item);
                c.sendPacket(MTSCSPacket.cashItemExpired(item.getUniqueId()));
            }
            toberemove.clear();
        }
    }

    public IItem toItem(CashItem cItem, MapleCharacter chr) {
        return toItem(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), "", chr);
    }

    public IItem toItem(CashItem cItem) {
        return toItem(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), "");
    }

    public IItem toItem(CashItem cItem, String gift) {
        return toItem(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), gift);
    }

    public IItem toItem(CashItem cItem, int uniqueid) {
        return toItem(cItem, uniqueid, "");
    }

    public IItem toItem(CashItem cItem, int uniqueid, String gift) {
        return toItem(cItem, null, uniqueid, gift);
    }

    public IItem toItem(CashItem cItem, int uniqueid, String gift, MapleCharacter chr) {
        return toItem(cItem, chr, uniqueid, gift);
    }

    public IItem toItem(CashItem cItem, MapleCharacter chr, int uniqueid, String gift) {
        if (uniqueid <= 0) {
            uniqueid = MapleInventoryIdentifier.getInstance();
        }
        long period = cItem.getPeriod();
        if (period <= 0) {
            period = -1;
        }
        if ((cItem.getId() >= 5210000 && cItem.getId() <= 5210011) || (cItem.getId() >= 5360000 && cItem.getId() <= 5360015)) { // 加倍卡

        }
        else if (cItem.getId() == 5320000) { // 護身符
            //period = 90;
        }
        else if (GameConstants.isPet(cItem.getId())) {
            period = 90;
        }
        IItem ret = null;
        if (GameConstants.getInventoryType(cItem.getId()) == MapleInventoryType.EQUIP) {
            Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(cItem.getId());
            eq.setUniqueId(uniqueid);
            if (period == -1)
                eq.setExpiration(-1);
            else
                eq.setExpiration((long) (System.currentTimeMillis() + (long) (period * 24 * 60 * 60 * 1000)));
            eq.setGiftFrom(gift);
            if (GameConstants.isEffectRing(cItem.getId()) && uniqueid > 0) {
                MapleRing ring = MapleRing.loadFromDb(uniqueid);
                if (ring != null) {
                    eq.setRing(ring);
                }
            }
            ret = eq.copy();
        } else {
            Item item = new Item(cItem.getId(), (byte) 0, (short) cItem.getCount(), (byte) 0, uniqueid);
            if (period == -1)
                item.setExpiration(-1);
            else
                item.setExpiration((long) (System.currentTimeMillis() + (long) (period * 24 * 60 * 60 * 1000)));
            item.setGiftFrom(gift);
            if (GameConstants.isPet(cItem.getId())) {
                final MaplePet pet = MaplePet.createPet(cItem.getId(), uniqueid);
                if (pet != null) {
                    item.setPet(pet);
                }
            }
            ret = item.copy();
        }
        return ret;
    }

    public void addToInventory(IItem item) {
        inventory.add(item);
    }

    public void removeFromInventory(IItem item) {
        inventory.remove(item);
    }

    public void gift(int recipient, String from, String message, int sn, int type) {
        gift(recipient, from, message, sn, 0, type);
    }

    public void gift(int recipient, String from, String message, int sn, int uniqueid, int type) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO `gifts` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, recipient);
            ps.setString(2, from);
            ps.setString(3, message);
            ps.setInt(4, sn);
            ps.setInt(5, uniqueid);
            ps.setInt(6, type);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public List<Pair<IItem, String>> loadGifts() {
        List<Pair<IItem, String>> gifts = new ArrayList<>();
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM `gifts` WHERE `recipient` = ?");
            ps.setInt(1, characterId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CashItem cmitem = CashItemFactory.getInstance().getItemForGift(rs.getInt("sn"),rs.getInt("types"));
                IItem item = toItem(cmitem, rs.getInt("uniqueid"), rs.getString("from"));
                gifts.add(new Pair<>(item, rs.getString("message")));
                uniqueids.add(item.getUniqueId());
                List<Integer> packages = CashItemFactory.getInstance().getPackageItems(cmitem.getId());
                if (packages != null && packages.size() > 0) {
                    for (Integer packageItem : packages) {
                        addToInventory(toItem(CashItemFactory.getInstance().getSimpleItem(packageItem), rs.getString("from")));
                    }
                } else {
                    addToInventory(item);
                }
            }

            rs.close();
            ps.close();
            ps = con.prepareStatement("DELETE FROM `gifts` WHERE `recipient` = ?");
            ps.setInt(1, characterId);
            ps.executeUpdate();
            ps.close();
            save();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return gifts;
    }

    public boolean canSendNote(int uniqueid) {
        return uniqueids.contains(uniqueid);
    }

    public void sendedNote(int uniqueid) {
        for (int i = 0; i < uniqueids.size(); i++) {
            if (uniqueids.get(i) == uniqueid) {
                uniqueids.remove(i);
            }
        }
    }

    public void save() throws SQLException {
        List<Pair<IItem, MapleInventoryType>> itemsWithType = new ArrayList<>();

        for (IItem item : inventory) {
            itemsWithType.add(new Pair<>(item, GameConstants.getInventoryType(item.getItemId())));
        }

        factory.saveItems(itemsWithType, accountId);
    }
}
