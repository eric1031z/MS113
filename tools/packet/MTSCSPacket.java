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
package tools.packet;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.List;
import client.MapleClient;
import client.MapleCharacter;
import client.inventory.IItem;
import server.CashShop;
import server.CashItemFactory;
import handling.SendPacketOpcode;
import constants.ServerConstants;
import java.util.ArrayList;
import tools.Pair;
import java.util.Map;
import java.util.Map.Entry;
import server.CashItem;
import server.CashItemFlag;
import server.CashModItem;
import server.MTSStorage.MTSItemInfo;
import tools.HexTool;
import tools.KoreanDateUtil;
import tools.data.MaplePacketLittleEndianWriter;

public class MTSCSPacket {

    public static byte[] showPredictCard(String name, String otherName, int love, int cardId, int commentId) {

        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(0x67);
        mplew.writeMapleAsciiString(name);
        mplew.writeMapleAsciiString(otherName);
        mplew.writeInt(love);
        mplew.writeInt(cardId);
        mplew.writeInt(commentId);
        return mplew.getPacket();
    }

    public static byte[] warpCS(MapleClient c, int type) {

        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SET_CASH_SHOP.getValue());

        PacketHelper.addCharacterInfo(mplew, c.getPlayer());

        mplew.writeMapleAsciiString(c.getAccountName());

        CashItemFactory cif = CashItemFactory.getInstance();

        mplew.writeInt(0); // someinfo , it'size , decodeBuffer(4*size)

        // 商城道具
        List<CashItem> csHideItems = cif.getHideAllDefaultItems(c); // 關閉預設物品
        List<CashModItem> csItems = cif.getAllModItems(type);
        mplew.writeShort(csHideItems.size() + csItems.size());
        // 隱藏不出售的商品
        for (CashItem csItem : csHideItems) {
            mplew.writeInt(csItem.getSN());
            mplew.writeInt(0x400);
            mplew.write(0);
        }
        // 自定義商品寫入
        for (CashModItem csMod : csItems) {
            addCashModItem(mplew, csMod);
        }

        mplew.write(HexTool.getByteArrayFromHexString("00 00 0A 00 50 10 27 00 00 00 5A 00 00 00 00 00 00 00 00 00 00 00 00 FF 00 00 00 00 00 00 00 00 00 "));
        mplew.write(HexTool.getByteArrayFromHexString("06 00 00 00 31 00 30 00 31 00 00 00 00 00 00 00 05 00 0E 00 05 00 08 06 A0 01 14 00 C8 FE 8D 06 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 00 13 00 0A 01 0C 06 06 00 00 00 31 00 30 00 31 00 00 00 00 00 00 00 03 00 16 00 0D 00 0C 06 90 01 14 00 F8 36 8C 06 31 00 00 00 00 00 00 00 03 00 19 00 10 01 0C 06 06 00 00 00 31 00 30 00"));

        List<Integer> hot_sell = new ArrayList<>();
        for (int sn : ServerConstants.hot_sell) {
            if (cif.getItem(sn,c) != null) {
                hot_sell.add(sn);
            }
        }
        for (int i = 1; i <= 8; i++) {
            for (int j = 0; j < 2; j++) {
                for (int hs = 0; hs < 5; hs++) {
                    mplew.writeInt(i);
                    mplew.writeInt(j);
                    mplew.writeInt(hot_sell.size() > hs ? hot_sell.get(hs) : 0);
                }
            }
        }
        mplew.writeShort(0);
        mplew.writeShort(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] enableCSUse() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_USE.getValue());
        mplew.write(1);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static byte[] showCashShopAcc(MapleClient c) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_ACC.getValue());
        mplew.write(1);
        mplew.writeMapleAsciiString(c.getAccountName());
        return mplew.getPacket();
    }

    public static byte[] playCashSong(int itemid, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CASH_SONG.getValue());
        mplew.writeInt(itemid);
        mplew.writeMapleAsciiString(name);
        return mplew.getPacket();
    }

    public static byte[] useCharm(byte charmsleft, byte daysleft) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        mplew.write(6); //護身符總類??
        mplew.write(1); //未知
        mplew.write(charmsleft); //剩餘護身符
        mplew.write(daysleft); //天數

        return mplew.getPacket();
    }

    public static byte[] useWheel(byte charmsleft) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        mplew.write(21);
        mplew.writeLong(charmsleft);

        return mplew.getPacket();
    }

    //ok
    public static byte[] itemExpired(int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        // 1E 00 02 83 C9 51 00

        // 21 00 08 02
        // 50 62 25 00
        // 50 62 25 00
        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(2);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static byte[] ViciousHammer(boolean start, int hammered) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.VICIOUS_HAMMER.getValue());
        if (start) {
            mplew.write(49);
            mplew.writeInt(0);
            mplew.writeInt(hammered);
        } else {
            mplew.write(53);
            mplew.writeInt(0);
        }

        return mplew.getPacket();
    }

    public static byte[] changePetFlag(int uniqueId, boolean added, int flagAdded) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PET_FLAG_CHANGE.getValue());

        mplew.writeLong(uniqueId);
        mplew.write(added ? 1 : 0);
        mplew.writeShort(flagAdded);

        return mplew.getPacket();
    }

    public static byte[] changePetName(MapleCharacter chr, String newname, int slot) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PET_NAMECHANGE.getValue());

        mplew.writeInt(chr.getId());
        mplew.write(0);
        mplew.writeMapleAsciiString(newname);
        mplew.write(slot);

        return mplew.getPacket();
    }

    public static byte[] showNotes(ResultSet notes, int count) throws SQLException {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_NOTES.getValue());
        mplew.write(3);
        mplew.write(count);
        for (int i = 0; i < count; i++) {
            mplew.writeInt(notes.getInt("id"));
            mplew.writeMapleAsciiString(notes.getString("from"));
            mplew.writeMapleAsciiString(notes.getString("message"));
            mplew.writeLong(PacketHelper.getKoreanTimestamp(notes.getLong("timestamp")));
            mplew.write(notes.getInt("gift"));
            notes.next();
        }

        return mplew.getPacket();
    }

    public static byte[] useChalkboard(final int charid, final String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHALKBOARD.getValue());
        mplew.writeInt(charid);
        if (msg == null || msg.length() <= 0) {
            mplew.write(0);
        } else {
            mplew.write(1);
            mplew.writeMapleAsciiString(msg);
        }

        return mplew.getPacket();
    }

    public static byte[] getTrockRefresh(MapleCharacter chr, byte vip, boolean delete) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MAP_TRANSFER_RESULT.getValue());
        mplew.write(delete ? 2 : 3);
        mplew.write(vip);
        if (vip == 1) {
            int[] map = chr.getRocks();
            for (int i = 0; i < 10; i++) {
                mplew.writeInt(map[i]);
            }
        } else {
            int[] map = chr.getRegRocks();
            for (int i = 0; i < 5; i++) {
                mplew.writeInt(map[i]);
            }
        }
        return mplew.getPacket();
    }

    public static byte[] sendShowWishList(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x4A);
        int[] list = chr.getWishlist();
        for (int i = 0; i < 10; i++) {
            mplew.writeInt(list[i] != -1 ? list[i] : 0);
        }
        return mplew.getPacket();
    }

    public static byte[] sendShowWishListFail(MapleClient c, int flag) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x4B);
        mplew.write(flag);
        return mplew.getPacket();
    }

    public static byte[] setWishList(MapleCharacter chr) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());

        mplew.write(0x4C);
        int[] list = chr.getWishlist();
        for (int i = 0; i < 10; i++) {
            mplew.writeInt(list[i] != -1 ? list[i] : 0);
        }
        return mplew.getPacket();
    }

    public static byte[] sendSetWishListFail(MapleClient c, int flag) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x4D);
        mplew.write(flag);
        return mplew.getPacket();
    }

    public static byte[] showBoughtCashItem(int itemid, int sn, int uniqueid, int accid, int quantity, String giftFrom, long expire) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x4E);
        addCashItemInfo(mplew, uniqueid, accid, itemid, sn, quantity, giftFrom, expire);

        return mplew.getPacket();
    }

    public static byte[] showBoughtCashItem(IItem item, int sn, int accid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x4E);
        addCashItemInfo(mplew, item, accid, sn);

        return mplew.getPacket();
    }

    public static byte[] sendShowBoughtCashItemFail(int flag, int value) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x4F);
        mplew.writeShort(flag);
        if (flag == 194 || flag == 193) {
            mplew.writeInt(value);
        }
        return mplew.getPacket();
    }

    public static byte[] showBoughtCashPackage(Map<Integer, IItem> ccc, int accid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        /* 
         0x62 發生不明錯誤！
         0x63 刪除加值道具
         0x80 套裝購買成功！
         0x81 購買失敗！
         0x86 報名成功！
         0x95 完成拒絕收禮！
         0x96 因送禮人已刪除帳號，無法拒絕收禮！
         0x9B 恭喜中獎！本次購買為大贏家活動第一百筆消費！
         0x9D 購買成功！
         0x9F 購買成功！
         0xA1 購買成功！
         0xA4 已超過工作時間。休息一下再繼續。
         */
        mplew.write(0x80);
        mplew.write(ccc.size());
        for (Entry<Integer, IItem> sn : ccc.entrySet()) {
            addCashItemInfo(mplew, sn.getValue(), accid, sn.getKey());
        }
        mplew.writeShort(0); // 0 = 顯示買好了 1 = 獲得多少楓葉點數
        return mplew.getPacket();
    }

    public static byte[] sendShowBoughtCashPackageFail(int flag, int value) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x81);
        mplew.writeShort(flag);
        if (flag == 194 || flag == 193) {
            mplew.writeInt(value);
        }
        return mplew.getPacket();
    }

    public static byte[] sendGift(String to, CashItem item, int gainMaplePoint, boolean isPackage) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());

        mplew.write(isPackage ? 0x82 : 0x55);
        mplew.writeMapleAsciiString(to);
        mplew.writeInt(item.getId());
        mplew.writeShort(item.getCount());
        if (isPackage) {
            mplew.writeShort(gainMaplePoint);
        }

        return mplew.getPacket();
    }

    public static byte[] sendGiftFail(int flag, int page, boolean isPackage) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(isPackage ? 0x83 : 0x56);
        mplew.writeShort(flag);
        if (flag == 208 || flag == 207) {
            mplew.writeInt(page);
        }
        return mplew.getPacket();
    }

    public static byte[] showNXMapleTokens(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_UPDATE.getValue());
        mplew.writeInt(chr.getCSPoints(1)); // A-cash
        mplew.writeInt(chr.getCSPoints(2)); // MPoint

        return mplew.getPacket();
    }

    public static byte[] showXmasSurprise(boolean full, int idFirst, IItem item, int accid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        
        mplew.writeShort(SendPacketOpcode.XMAS_SURPRISE.getValue());
        mplew.write(full ? 222 : 223);
        if (!full) {
            mplew.writeLong(idFirst); //uniqueid of the xmas surprise itself
            mplew.writeInt(0);
            addCashItemInfo(mplew, item, accid, 0); //info of the new item, but packet shows 0 for sn?
            mplew.writeInt(item.getItemId());
            mplew.write(1);
            mplew.write(1);
        }

        return mplew.getPacket();
    }

    public static final void addCashItemInfo(MaplePacketLittleEndianWriter mplew, IItem item, int accId, int sn) {
        addCashItemInfo(mplew, item, accId, sn, true);
    }

    public static final void addCashItemInfo(MaplePacketLittleEndianWriter mplew, IItem item, int accId, int sn, boolean isFirst) {
        addCashItemInfo(mplew, item.getUniqueId(), accId, item.getItemId(), sn, item.getQuantity(), item.getGiftFrom(), item.getExpiration(), isFirst); //owner for the lulz
    }

    public static final void addCashItemInfo(MaplePacketLittleEndianWriter mplew, int uniqueid, int accId, int itemid, int sn, int quantity, String sender, long expire) {
        addCashItemInfo(mplew, uniqueid, accId, itemid, sn, quantity, sender, expire, true);
    }

    public static final void addCashItemInfo(MaplePacketLittleEndianWriter mplew, int uniqueid, int accId, int itemid, int sn, int quantity, String sender, long expire, boolean isFirst) {
        mplew.writeLong(uniqueid > 0 ? uniqueid : 0);
        mplew.writeLong(accId);
        mplew.writeInt(itemid);
        mplew.writeInt(isFirst ? sn : 0);
        mplew.writeShort(quantity);
        mplew.writeAsciiString(sender, 15); //owner for the lulzlzlzl
        PacketHelper.addExpirationTime(mplew, expire);
        mplew.writeLong(0);
        //if (isFirst && uniqueid > 0 && GameConstants.isEffectRing(itemid)) {
        //	MapleRing ring = MapleRing.loadFromDb(uniqueid);
        //	if (ring != null) { //or is this only for friendship rings, i wonder. and does isFirst even matter
        //		mplew.writeMapleAsciiString(ring.getPartnerName());
        //		mplew.writeInt(itemid);
        //		mplew.writeShort(quantity);
        //	}
        //}
    }

    public static void addCashModItem(MaplePacketLittleEndianWriter mplew, CashModItem csMod) {
        /*
         F3 C2 35 01 
         FF FF 01 00 
        
         0F 0E 10 00 
         01 00 
         5A 7C 15 00 
         00 
         00 
         5A 00 
         00 00 00 00 
         00 00 00 00 
         00 
         02 gender
         01 showup
         02 mark
         00 
         00 00 
         00 00 
         00 00 
         00"
        
         61 48 37 01 
         FF FF 01 00 
        
         F1 E6 0F 00 
         01 00 
         46 00 00 00 
         FF 0C 00 00 
         00 
         00 
         00 00 
         00 00 00 00 FF 02 01 01 FF 00 00 00 00 00 00 00
         */
        mplew.writeInt(csMod.getSN());
        int mask = 0;
        for (CashItemFlag cf : csMod.flags) {
            mask |= cf.getValue();
        }
        mplew.writeInt(mask);
        // [0x1][V]
        if (csMod.flags.contains(CashItemFlag.ITEMID)) {
            mplew.writeInt(csMod.getId());
        }
        // [0x2][V]
        if (csMod.flags.contains(CashItemFlag.COUNT)) {
            mplew.writeShort(csMod.getCount() == 0 ? 1 : csMod.getCount());
        }
        // [0x4][V]
        if (csMod.flags.contains(CashItemFlag.PRICE)) {
            mplew.writeInt(csMod.getPrice());
        }
        // [0x8][V]
        if (csMod.flags.contains(CashItemFlag.UNK3)) {
            mplew.write(0); // item.unk_1 - 1
        }
        // [0x10][V]
        if (csMod.flags.contains(CashItemFlag.PRIORITY)) {
            mplew.write(csMod.getPriority());
        }
        // [0x20][V]
        if (csMod.flags.contains(CashItemFlag.PERIOD)) {
            mplew.writeShort(csMod.getPeriod());
        }
        // [0x40][V]
        if (csMod.flags.contains(CashItemFlag.UNK6)) {
            mplew.writeInt(0);
        }
        // [0x80][V]
        if (csMod.flags.contains(CashItemFlag.MESO)) {
            mplew.writeInt(0);
        }
        // [0x100][V]
        if (csMod.flags.contains(CashItemFlag.UNK8)) {
            mplew.write(0); // unk_2 - 1
        }
        // [0x200][V]
        if (csMod.flags.contains(CashItemFlag.GENDER)) {
            mplew.write(csMod.getGender());
        }
        // [0x400][V]
        if (csMod.flags.contains(CashItemFlag.ONSALE)) {
            mplew.write(csMod.isOnSale());
        }
        // [0x800][V]
        if (csMod.flags.contains(CashItemFlag.FLAGE)) {
            mplew.write(csMod.getFlage());
        }
        // [0x1000][V]
        if (csMod.flags.contains(CashItemFlag.UNK12)) {
            mplew.write(0); // unk_3 - 1
        }
        // [0x2000][V]
        if (csMod.flags.contains(CashItemFlag.UNK13)) {
            mplew.writeShort(0);
        }
        // [0x4000][V]
        if (csMod.flags.contains(CashItemFlag.UNK14)) {
            mplew.writeShort(0);
        }
        // [0x8000][V]
        if (csMod.flags.contains(CashItemFlag.UNK15)) {
            mplew.writeShort(0);
        }
        // [0x10000][V]
        if (csMod.flags.contains(CashItemFlag.PACKAGEZ)) {
            List<Integer> pack = CashItemFactory.getInstance().getPackageItems(csMod.getSN());
            if (pack == null) {
                mplew.write(0);
            } else {
                mplew.write(pack.size());
                for (int i = 0; i < pack.size(); i++) {
                    mplew.writeInt(CashItemFactory.getInstance().getSimpleItem(pack.get(i)).getSN());
                }
            }
        }
    }

    public static byte[] showBoughtCSQuestItem(int price, short quantity, byte position, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x92);
        mplew.writeInt(price);
        mplew.writeShort(quantity);
        mplew.writeShort(position);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static byte[] sendCSFail(int err) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x4F);
        mplew.writeShort(err);
        if (err == 194 || err == 193) {
            mplew.writeInt(err);
        }
        return mplew.getPacket();
    }

    public static byte[] showCouponRedeemedItem(int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.writeShort(0x62);
        mplew.writeInt(0);
        mplew.writeInt(1);
        mplew.writeShort(1);
        mplew.writeShort(0x1A);
        mplew.writeInt(itemid);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] showCouponRedeemedItem(Map<Integer, IItem> items, int mesos, int maplePoints, MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x62); //use to be 4c
        mplew.write(items.size());
        for (Entry<Integer, IItem> item : items.entrySet()) {
            addCashItemInfo(mplew, item.getValue(), c.getAccID(), item.getKey().intValue());
        }
        mplew.writeLong(maplePoints);
        mplew.writeInt(mesos);

        return mplew.getPacket();
    }

    public static byte[] showCashInventory(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x46);
        CashShop mci = c.getPlayer().getCashInventory();
        mplew.writeShort(mci.getItemsSize());
        for (IItem itemz : mci.getInventory()) {
            addCashItemInfo(mplew, itemz, c.getAccID(), 0); //test
        }
        mplew.writeShort(c.getPlayer().getStorage().getSlots());
        mplew.writeShort(c.getCharacterSlots());
        return mplew.getPacket();
    }

    //work on this packet a little more
    public static byte[] showGifts(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());

        mplew.write(0x48); //use to be 40
        List<Pair<IItem, String>> mci = c.getPlayer().getCashInventory().loadGifts();
        mplew.writeShort(mci.size());
        for (Pair<IItem, String> mcz : mci) {
            mplew.writeLong(mcz.getLeft().getUniqueId());
            mplew.writeInt(mcz.getLeft().getItemId());
            mplew.writeAsciiString(mcz.getLeft().getGiftFrom(), 15);
            mplew.writeAsciiString(mcz.getRight(), 74);
        }

        return mplew.getPacket();
    }

    public static byte[] cashItemExpired(int uniqueid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x71); //use to be 5d
        mplew.writeLong(uniqueid);
        return mplew.getPacket();
    }

    public static byte[] increasedInvSlots(int inv, int slots) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x57);
        mplew.write(inv);
        mplew.writeShort(slots);

        return mplew.getPacket();
    }

    //also used for character slots !
    public static byte[] increasedStorageSlots(int slots) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x5B);
        mplew.writeShort(slots);

        return mplew.getPacket();
    }

    public static byte[] confirmToCSInventory(IItem item, int accId, int sn) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x61);
        addCashItemInfo(mplew, item, accId, sn, true);

        return mplew.getPacket();
    }

    public static byte[] confirmFromCSInventory(IItem item, short pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_OPERATION.getValue());
        mplew.write(0x5F);
        mplew.writeShort(pos);
        PacketHelper.addItemInfo(mplew, item, true, false);

        return mplew.getPacket();
    }

    public static byte[] sendMesobagFailed() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESOBAG_FAILURE.getValue());
        return mplew.getPacket();
    }

    public static byte[] sendMesobagSuccess(int mesos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MESOBAG_SUCCESS.getValue());
        mplew.writeInt(mesos);
        return mplew.getPacket();
    }

//======================================MTS===========================================
    public static final byte[] startMTS(final MapleCharacter chr, MapleClient c) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SET_ITC.getValue());

        PacketHelper.addCharacterInfo(mplew, chr);

        mplew.writeMapleAsciiString(c.getAccountName());
        mplew.writeInt(ServerConstants.MTS_MESO);
        mplew.writeInt(ServerConstants.MTS_TAX);
        mplew.writeInt(ServerConstants.MTS_BASE);
        mplew.writeInt(24);
        mplew.writeInt(168);
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        return mplew.getPacket();
    }

    public static final byte[] sendMTS(final List<MTSItemInfo> items, final int tab, final int type, final int page, final int pages) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x14); //operation
        mplew.writeInt(pages * 16); //total items
        mplew.writeInt(items.size()); //number of items on this page
        mplew.writeInt(tab);
        mplew.writeInt(type);
        mplew.writeInt(page);
        mplew.write(1);
        mplew.write(1);

        for (MTSItemInfo item : items) {
            addMTSItemInfo(mplew, item);
        }
        mplew.write(1); //0 or 1?

        return mplew.getPacket();
    }

    public static final byte[] showMTSCash(final MapleCharacter p) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GET_MTS_TOKENS.getValue());
//        mplew.writeInt(p.getCSPoints(1));
        mplew.writeInt(p.getCSPoints(2));
        return mplew.getPacket();
    }

    public static final byte[] getMTSWantedListingOver(final int nx, final int items) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x3D);
        mplew.writeInt(nx);
        mplew.writeInt(items);
        return mplew.getPacket();
    }

    public static final byte[] getMTSConfirmSell() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x1C);
        return mplew.getPacket();
    }

    public static final byte[] getMTSFailSell() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x1D);
        mplew.write(0x42);
        return mplew.getPacket();
    }

    public static final byte[] getMTSConfirmBuy() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x32);
        return mplew.getPacket();
    }

    public static final byte[] getMTSFailBuy() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x33);
        mplew.write(0x42);
        return mplew.getPacket();
    }

    public static final byte[] getMTSConfirmCancel() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x22);
        return mplew.getPacket();
    }

    public static final byte[] getMTSFailCancel() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x26);
        mplew.write(0x42);
        return mplew.getPacket();
    }

    public static final byte[] getMTSConfirmTransfer(final int quantity, final int pos) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x24);
        mplew.writeInt(quantity);
        mplew.writeInt(pos);
        return mplew.getPacket();
    }

    private static final void addMTSItemInfo(final MaplePacketLittleEndianWriter mplew, final MTSItemInfo item) {
        PacketHelper.addItemInfo(mplew, item.getItem(), true, true);
        mplew.writeInt(item.getId()); //id
        mplew.writeInt(item.getTaxes()); //this + below = price
        mplew.writeInt(item.getPrice()); //price
        mplew.writeInt(0);// Long?
        mplew.writeInt(item.getItem().getQuantity());// qiantity
        mplew.writeLong(0);
        mplew.writeLong(KoreanDateUtil.getQuestTimestamp(item.getEndingDate()));
        mplew.writeMapleAsciiString(item.getSeller()); //account name (what was nexon thinking?)
        mplew.writeMapleAsciiString(item.getSeller()); //char name
        mplew.writeZeroBytes(28);
    }

    public static final byte[] getNotYetSoldInv(final List<MTSItemInfo> items) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x20);

        mplew.writeInt(items.size());

        for (MTSItemInfo item : items) {
            addMTSItemInfo(mplew, item);
        }

        return mplew.getPacket();
    }

    public static final byte[] getTransferInventory(final List<IItem> items, final boolean changed) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        mplew.write(0x1E);

        mplew.writeInt(items.size());
        int i = 0;
        for (IItem item : items) {
            PacketHelper.addItemInfo(mplew, item, true, true);
            mplew.writeInt(Integer.MAX_VALUE - i); //fake ID
            mplew.writeInt(110);
            mplew.writeInt(1011); //fake
            mplew.writeZeroBytes(52);
            i++;
        }
        mplew.writeInt(-47 + i - 1);
        mplew.write(changed ? 1 : 0);

        return mplew.getPacket();
    }

    public static final byte[] addToCartMessage(boolean fail, boolean remove) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MTS_OPERATION.getValue());
        if (remove) {
            if (fail) {
                mplew.write(0x29);
                mplew.writeInt(-1);
            } else {
                mplew.write(0x28);
            }
        } else if (fail) {
            mplew.write(0x27);
            mplew.writeInt(-1);
        } else {
            mplew.write(0x26);
        }

        return mplew.getPacket();
    }

    public static byte[] sendWEB(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CS_WEB.getValue());
        return mplew.getPacket();
    }
}
