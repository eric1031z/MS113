/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sever.wmonster;

import java.sql.Timestamp;

/**
 *
 * @author timis
 */
public class MapleWorldMonsterControl {
    private final int mobid;
    private final int hp;
    private final int mp;
    private final int watk;
    private final int matk;
    private final int chance;
    private final int prize;
    private final int prizecount;
    private final int prizeneed;
    private final int bonus;
    private final int bonuscount;
    private final int currentmap;
    private final Timestamp lastappear;


    public MapleWorldMonsterControl(int mobid, int hp, int mp, int watk, int matk, int chance, int prize, int prizecount, int prizeneed, int bonus, int bonuscount, int currentmap, Timestamp lastappear) {
        this. mobid = mobid;
        this. hp = hp;
        this. mp = mp;
        this. watk = watk;
        this. matk = matk;
        this. chance = chance;
        this. prize = prize;
        this. prizecount = prizecount;
        this. prizeneed = prizeneed;
        this. bonus = bonus;
        this. bonuscount = bonuscount;
        this. currentmap = currentmap;
        this. lastappear = lastappear;
        
    }
}
