/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author pungin
 */
public enum CashItemFlag {

    //-----------------------------Mask[0]
    // 道具ID [0x1]
    ITEMID(0),
    // 數量 [0x2]
    COUNT(1),
    // 價錢(打折後) [0x4]
    PRICE(2),
    // 3 [0x8]    
    UNK3(3),
    // 排序 [0x10]
    PRIORITY(4),
    // 道具時間(什麼時間?限時?) [0x20]
    PERIOD(5),    
    // [0x40]
    UNK6(6),
    // [0x80]
    MESO(7),
    // [0x100]
    UNK8(8),    
    // 性別 [0x200]
    GENDER(9),
    // 出售中 [0x400]
    ONSALE(10),
    // 商品狀態[0x800] 0-NEW,1-SALE,2-HOT,3-EVENT,其他-無
    FLAGE(11),
    // 12 [0x1000]
    UNK12(12),
    // 13 [0x2000]
    UNK13(13),
    // 14 [0x4000]
    UNK14(14),
    // 15 [0x8000]
    UNK15(15),
    // 是否為禮包? [0x10000]
    PACKAGEZ(16),    
    // 17 [0x20000]
    UNK17(17),    
    // [0x40000]
    UNK18(18),
    // 開始販售時間 [0x80000]
    TIME_BEGIN(19),
    // 結束販售時間 [0x100000]
    TIME_END(20),
    // [0x200000]
    UNK21(21),
    // [0x400000]
    UNK22(22),
    // [0x800000]
    UNK23(23),
    // [0x1000000]
    UNK24(24),
    // [0x2000000]
    UNK25(25),
    // [0x4000000]
    UNK26(26),
    // [0x8000000]
    UNK27(27),
    // [0x10000000]
    UNK28(28),
    // [0x20000000]
    UNK29(29),
    // [0x40000000]
    UNK30(30),
    // [0x80000000]
    UNK31(31),
    // [0x1]
    UNK32(32),
    // [0x2]
    UNK33(33),
    ;

    private final int code;
    private final int first;

    private CashItemFlag(int code) {
        this.code = 1 << (code % 32);
        this.first = (int) Math.floor(code / 32);
    }

    
    public int getPosition() {
        return first;
    }

    public int getValue() {
        return code;
    }
}
