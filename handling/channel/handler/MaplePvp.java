package handling.channel.handler;

import client.ISkill;
import client.MapleCharacter;
import client.MapleBuffStat;
import client.MapleStat;
import client.MapleJob;
import client.SkillFactory;
import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import handling.channel.handler.AttackInfo;
import handling.channel.handler.DamageParse;
import handling.world.World;
import handling.world.guild.MapleGuild;
import java.util.Random;
import server.life.MapleMonster;
import server.life.MapleLifeFactory;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import tools.MaplePacketCreator;
import tools.packet.MobPacket;

public class MaplePvp {

    private static int pvpDamage;
    private static int maxDis;
    private static int maxHeight;
    private static boolean isAoe = false;
    public static boolean isLeft = false;
    public static boolean isRight = false;
    private static boolean magic = false;
    private static boolean magicrecovery = false;
    private static boolean magicguard = false;
    private static boolean ignore = false;
    private static double multi = 1;
    private static Integer combo;
    private static int skill = 0;
    private static ISkill skil;
    private static boolean mesguard = false;
    private static MapleMonster pvpMob = MapleLifeFactory.getMonster(9400711);
    private static int attackedDamage = 0;
    private static int decidepvp = 0;
    private static int mode;
    
    private static boolean isMeleeAttack(AttackInfo attack) {
        switch (attack.skill) {
            case 1001004:    //Power Strike
            case 1001005:    //Slash Blast
            case 4001334:    //Double Stab
            case 4201005:    //Savage Blow
            case 1111004:    //Panic: Axe
            case 1111003:    //Panic: Sword
            case 1311004:    //Dragon Fury: Pole Arm
            case 1311003:    //Dragon Fury: Spear
            case 1311002:    //Pole Arm Crusher
            case 1311005:    //Sacrifice
            case 1311001:    //Spear Crusher
            case 1121008:    //Brandish
            case 1221009:    //Blast
            case 1121006:    //Rush
            case 1221007:    //Rush
            case 1321003:    //Rush
            case 4221001:    //Assassinate
            case 5001002: //Sommersault Kick
            case 5101003: //double uppercut
            case 5101004: //corkscrew blow
                return true;
        }
        return false;
    }

    private static boolean isRangeAttack(AttackInfo attack) {
        switch (attack.skill) {
            case 2001004:    //Energy Bolt
            case 2001005:    //Magic Claw
            case 3001004:    //Arrow Blow
            case 3001005:    //Double Shot
            case 4001344:    //Lucky Seven
            case 2101004:    //Fire Arrow
            case 2101005:    //Poison Brace
            case 2201004:    //Cold Beam
            case 2301005:    //Holy Arrow
            case 4101005:    //Drain
            case 2211002:    //Ice Strike
            case 2211003:    //Thunder Spear
            case 3111006:    //Strafe
            case 3211006:    //Strafe
            case 4111005:    //Avenger
            case 4211002:    //Assaulter
            case 2121003:    //Fire Demon
            case 2221006:    //Chain Lightning
            case 2221003:    //Ice Demon
            case 2111006:	 //Element Composition F/P
            case 2211006:	 //Element Composition I/L
            case 2321007:    //Angel's Ray
            case 3121003:    //Dragon Pulse
            case 3121004:    //Hurricane
            case 3221003:    //Dragon Pulse
            case 3221001:    //Piercing
            case 3221007:    //Sniping
            case 4121003:    //Showdown taunt
            case 4121007:    //Triple Throw
            case 4221007:    //Boomerang Step
            case 4221003:    //Showdown taunt
            case 4111004:    //Shadow Meso
            case 5001003:    //Double Shot
            case 5101002:   //Backspin Blow
            case 5201001: //invisible shot
            case 5201002: //grenade
            case 5201004: //blank shot
            case 5201006: //recoil shot
            case 5211004: //flamethrower
            case 5211005: //icethrower
                return true;
        }
        return false;
    }

    private static boolean isAoeAttack(AttackInfo attack) {
        switch (attack.skill) {
            case 2201005:    //Thunderbolt
            case 3101005:    //Arrow Bomb : Bow
            case 3201005:    //Iron Arrow : Crossbow
            case 1111006:    //Coma: Axe
            case 1111005:    //Coma: Sword
            case 1211002:    //Charged Blow
            case 1311006:    //Dragon Roar
            case 2111002:    //Explosion
            case 2111003:    //Poison Mist
            case 2311004:    //Shining Ray
            case 3111004:    //Arrow Rain
            case 3111003:    //Inferno
            case 3211004:    //Arrow Eruption
            case 3211003:    //Blizzard (Sniper)
            case 4211004:    //Band of Thieves
            case 1221011:    //Sanctuary Skill
            case 2121001:    //Big Bang
            case 2121007:    //Meteo
            case 2121006:    //Paralyze
            case 2221001:    //Big Bang
            case 2221007:    //Blizzard
            case 2321008:    //Genesis
            case 2321001:    //Big Bang
            case 4121004:    //Ninja Ambush
            case 4121008:    //Ninja Storm knockback
            case 4221004:    //Ninja Ambush
            case 5121001: //dragon strike
            case 5111006: //shockwave
                return true;
        }
        return false;
    }

    private static void getDirection(AttackInfo attack) {
        if (isAoe) {
            isRight = true;
            isLeft = true;
        } else {
            isRight = true;
            isLeft = true;
        }
    }
    
   

    private static void DamageBalancer(AttackInfo attack) {
        if (attack.skill == 0) {
            pvpDamage = 100;
            maxDis = 130;
            maxHeight = 35;
        } else if (isMeleeAttack(attack)) {
            maxDis = 130;
            maxHeight = 45;
            isAoe = false;
            if (attack.skill == 4201005) {
                pvpDamage = (int) (Math.floor(Math.random() * (75 - 5) + 5));
            } else if (attack.skill == 1121008) {
                pvpDamage = (int) (Math.floor(Math.random() * (320 - 180) + 180));
                maxHeight = 50;
            } else if (attack.skill == 4221001) {
                pvpDamage = (int) (Math.floor(Math.random() * (200 - 150) + 150));
            } else if (attack.skill == 1121006 || attack.skill == 1221007 || attack.skill == 1321003) {
                pvpDamage = (int) (Math.floor(Math.random() * (200 - 80) + 80));
            } else {
                pvpDamage = (int) (Math.floor(Math.random() * (600 - 250) + 250));
            }
        } else if (isRangeAttack(attack)) {
            maxDis = 300;
            maxHeight = 40;
            isAoe = false;
            if (attack.skill == 4201005) {
                pvpDamage = (int) (Math.floor(Math.random() * (75 - 5) + 5));
            } else if (attack.skill == 4121007) {
                pvpDamage = (int) (Math.floor(Math.random() * (60 - 15) + 15));
            } else if (attack.skill == 4001344 || attack.skill == 2001005) {
                pvpDamage = (int) (Math.floor(Math.random() * (195 - 90) + 90));
            } else if (attack.skill == 4221007) {
                pvpDamage = (int) (Math.floor(Math.random() * (350 - 180) + 180));
            } else if (attack.skill == 3121004 || attack.skill == 3111006 || attack.skill == 3211006) {
                maxDis = 450;
                pvpDamage = (int) (Math.floor(Math.random() * (50 - 20) + 20));
            } else if (attack.skill == 2121003 || attack.skill == 2221003) {
                pvpDamage = (int) (Math.floor(Math.random() * (600 - 300) + 300));
            } else {
                pvpDamage = (int) (Math.floor(Math.random() * (400 - 250) + 250));
            }
        } else if (isAoeAttack(attack)) {
            maxDis = 350;
            maxHeight = 350;
            isAoe = true;
            if (attack.skill == 2121001 || attack.skill == 2221001 || attack.skill == 2321001 || attack.skill == 2121006) {
                maxDis = 175;
                maxHeight = 175;
                pvpDamage = (int) (Math.floor(Math.random() * (350 - 180) + 180));
            } else {
                pvpDamage = (int) (Math.floor(Math.random() * (700 - 300) + 300));
            }
        }
    }

    /*private static void monsterBomb(MapleCharacter player, MapleCharacter attackedPlayers, MapleMap map, AttackInfo attack) {
        //level balances
        if (attackedPlayers.getLevel() > player.getLevel() + 25) {
            pvpDamage *= 1.35;
        } else if (attackedPlayers.getLevel() < player.getLevel() - 25) {
            pvpDamage /= 1.35;
        } else if (attackedPlayers.getLevel() > player.getLevel() + 100) {
            pvpDamage *= 1.50;
        } else if (attackedPlayers.getLevel() < player.getLevel() - 100) {
            pvpDamage /= 1.50;
        }
        //class balances
        if (!MapleJob.is法師(player.getJob())) {
            pvpDamage *= 1.20;
        }
        //buff modifiers
        Integer mguard = attackedPlayers.getBuffedValue(MapleBuffStat.MAGIC_GUARD);
        Integer mesoguard = attackedPlayers.getBuffedValue(MapleBuffStat.MESOGUARD);
        if (mguard != null) {
            int mploss = (int) (pvpDamage / .5);
            pvpDamage *= .70;
            if (mploss > attackedPlayers.getMp()) {
                pvpDamage /= .70;
                attackedPlayers.cancelBuffStats(MapleBuffStat.MAGIC_GUARD);
            } else {
                attackedPlayers.setMp(attackedPlayers.getMp() - mploss);
                attackedPlayers.updateSingleStat(MapleStat.MP, attackedPlayers.getMp());
            }
        } else if (mesoguard != null) {
            int mesoloss = (int) (pvpDamage * .75);
            pvpDamage *= .75;
            if (mesoloss > attackedPlayers.getMeso()) {
                pvpDamage /= .75;
                attackedPlayers.cancelBuffStats(MapleBuffStat.MESOGUARD);
            } else {
                attackedPlayers.gainMeso(-mesoloss, false);
            }
        }
        MapleMonster pvpMob = MapleLifeFactory.getMonster(9400711);
        MobPacket.showBossHP(9400711, attackedPlayers.getHp(), attackedPlayers.getMaxHp());
        map.spawnMonsterOnGroundBelow(pvpMob, attackedPlayers.getPosition());
        for (int attacks = 0; attacks < attack.hits; attacks++) {
            map.broadcastMessage(MaplePacketCreator.damagePlayer(attack.hits, pvpMob.getId(), attackedPlayers.getId(), pvpDamage));
            attackedPlayers.addHP(-pvpDamage);
        }
        int attackedDamage = pvpDamage * attack.hits;
        attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(player.getName() + " 對你造成 " + attackedDamage + " 點傷害！"));
        map.killMonster(pvpMob, player, false, false, (byte) -1);
        //rewards
        if (attackedPlayers.getHp() <= 0 && !attackedPlayers.isAlive()) {
            int expReward = attackedPlayers.getLevel() * 100;
            int gpReward = (int) (Math.floor(Math.random() * (200 - 50) + 50));
            if (player.getStat().getPvpKills() * .25 >= player.getStat().getPvpDeaths()) {
                expReward *= 20;
            }
            player.gainExp(expReward, true, false,true);
            if (player.getGuildId() != 0 && player.getGuildId() != attackedPlayers.getGuildId()) {
                try {
                    MapleGuild guild = World.Guild.getGuild(player.getGuildId()); //pvp
                    guild.gainGP(gpReward);
                } catch (Exception e) {
                }
            }
            player.getStat().gainPvpKill();
            player.getClient().getSession().write(MaplePacketCreator.serverNotice("您已經將 " + attackedPlayers.getName() + "玩家殺死了！"));
            attackedPlayers.getStat().gainPvpDeath();
            attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(player.getName() + " 將你殺死了！"));
        }
    }*/
    
     
    
     
    
     private static void monsterBomb(MapleCharacter player, MapleCharacter attackedPlayers, MapleMap map, AttackInfo attack) {
                for (int dmgpacket = 0; dmgpacket < attack.hits; dmgpacket++) {
                if(!magic || !ignore) {
                    pvpDamage = (int) (player.getStat().getRandomage(player) * multi);
                }
                combo = player.getBuffedValue(MapleBuffStat.COMBO);
                if(combo != null) {
                //player.handleOrbgain();//comment out for now
                skil = SkillFactory.getSkill(1120003);
                skill = player.getSkillLevel(skil);
                if(skill > 0){
                multi = (skil.getEffect(player.getSkillLevel(skil)).getDamage() / 100.0);
                pvpDamage *= multi;
                }
                else {
                skil = SkillFactory.getSkill(1120003);
                skill = player.getSkillLevel(skil);
                multi = (skil.getEffect(player.getSkillLevel(skil)).getDamage() / 100.0);
                pvpDamage *= multi;
                }
                }//處理連擊
                
                //summon check

                //summon end
                //等級平衡
                if (attackedPlayers.getLevel() < player.getLevel()) { 
                int difference = player.getLevel() - attackedPlayers.getLevel();
                if(difference >= 5) {
                pvpDamage /= player.getLevel() / 5;
                }
                }
                if(!magic) {
                pvpDamage -= (attackedPlayers.getStat().getTotalWdef() * 1.5);
                } else {
                    pvpDamage -= (attackedPlayers.getStat().getTotalMdef() * 1.5);
                }
                if(pvpDamage < 0) {
                    pvpDamage = 1;
                }
                
                //盾= =
        Integer mguard = attackedPlayers.getBuffedValue(MapleBuffStat.MAGIC_GUARD);
        Integer mesoguard = attackedPlayers.getBuffedValue(MapleBuffStat.MESOGUARD);
        if (mguard != null) {
            skil = SkillFactory.getSkill(2001002);
            skill = attackedPlayers.getSkillLevel(skil);
            if(skill > 0){
                multi = (skil.getEffect(attackedPlayers.getSkillLevel(skil)).getX() / 100.0);
            }
            int mg = (int) (pvpDamage * multi);
            if(attackedPlayers.getMp() > mg) {
                attackedPlayers.setMp(attackedPlayers.getMp() - mg);
                pvpDamage -= mg;
            }
            else {
                pvpDamage -= attackedPlayers.getMp();
                attackedPlayers.setMp(0);
                attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice("您的MP不足"));
            }
            magicguard = true;
        }
        if (mesoguard != null) { 
            skil = SkillFactory.getSkill(4211005);
            skill = attackedPlayers.getSkillLevel(skil);
            if(skill > 0){
                multi = (skil.getEffect(attackedPlayers.getSkillLevel(skil)).getX() / 100.0);
            }
            int mg = (int) (pvpDamage * multi);
            if(attackedPlayers.getMeso() > mg) {
                attackedPlayers.gainMeso(-mg, false);
                pvpDamage *= 0.5;
            }
            else {
                attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice("您的楓幣不足"));
            }
            mesguard = true;
        }
        
        //對方被動技能
        int y = 2;
        int skillid;
        int aPmp;
        if(magic) {
        for (int i = 0; i<y; i++) {
            skillid = 100000 * i + 2000000;
            skil = SkillFactory.getSkill(skillid);
            skill = player.getSkillLevel(skil);
            if(skill > 0){
                multi = (skil.getEffect(player.getSkillLevel(skil)).getX() / 100.0);
                if(skil.getEffect(player.getSkillLevel(skil)).makeChanceResult()) {
                    aPmp = (int) (multi * attackedPlayers.getMaxMp());
                    if (attackedPlayers.getMp() > aPmp) {
                        attackedPlayers.setMp(attackedPlayers.getMp() - aPmp);
                        player.setMp(player.getMp() + aPmp);
                        if (player.getMp() > player.getMaxMp()) {
                            player.setMp(player.getMaxMp());
                        }
                    }
                    else 
                    {
                        player.setMp(player.getMp() + attackedPlayers.getMp());
                        if (player.getMp() > player.getMaxMp()) {
                            player.setMp(player.getMaxMp());
                        }
                        attackedPlayers.setMp(0);
                    }
                }
            }
            }
        magic = false;
        magicrecovery = true;
        }
       
                //召喚一隻隱形的怪在該玩家旁邊 ,對隱形怪造成傷害 轉移到玩家顯示 
                pvpMob = MapleLifeFactory.getMonster(9400711);
                map.spawnMonsterOnGroundBelow(pvpMob, attackedPlayers.getPosition()); //召喚隱形怪
                player.getClient().getSession().write(player.getStat().makeHPBarPacket(attackedPlayers));
                //map.broadcastMessage(player, player.showHPBarPacket(attackedPlayers), false);
                //number of damage packets
                        map.broadcastMessage(player, MaplePacketCreator.damagePlayer(attack.hits, pvpMob.getId(), attackedPlayers.getId(), pvpDamage), false);
                        player.getClient().getSession().write(MaplePacketCreator.damagePlayer(attack.hits, pvpMob.getId(), attackedPlayers.getId(), pvpDamage));
                        attackedPlayers.addHP(-pvpDamage);
                        try{
                        Thread.sleep(200);
                        }
                        catch(Exception e)
                        {
                        }
                        attackedDamage += pvpDamage;
                }
                attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(player.getName() + " 對你造成 " + attackedDamage + " 點傷害!"));
                if(attackedDamage > 0) {
                    combo = player.getBuffedValue(MapleBuffStat.COMBO);
                    if(combo != null) {
                        player.handleOrbgain();
                    }
                }
                attackedDamage = 0;
                //map.broadcastMessage(player, player.showHPBarPacket(attackedPlayers), false);
                player.getClient().getSession().write(player.getStat().makeHPBarPacket(attackedPlayers));
                //announcements
                if(magicguard) {
                    player.getClient().getSession().write(MaplePacketCreator.serverNotice( player.getName() + " 阻擋了你的傷害!"));
                    magicguard = false;
                }
                if(mesguard) {
                    player.getClient().getSession().write(MaplePacketCreator.serverNotice(player.getName() + " 阻擋了你的傷害!"));
                    mesguard = false;
                }
                if(magicrecovery) {
                    attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(player.getName() + " 吸收了你的MP!"));
                    magicrecovery = false;
                }
                
              
                if(getPvpMode()==1){ //經驗模式
                if (attackedPlayers.getHp() <= 0 && !attackedPlayers.isAlive()) {
                        // exp bonuses
                        int expreward = attackedPlayers.getLevel() * 80;
                        player.setHp(player.getMaxHp());
                        player.setMp(player.getMaxMp()); //殺死了人會回血
                        if (player.getStat().getPvpKills() / 4 >= player.getStat().getPvpDeaths()) {
                                expreward *= 2;
                        }
                        //exp
                        if(attackedPlayers.getExp() > 0) {
                        expreward *= (attackedPlayers.getExp() / attackedPlayers.getLevel()) / 2;
                        }
                        player.gainExp(expreward, true, false, true); //再判斷該玩家身上的經驗                     
                        //紀錄殺死的人
                        player.getStat().gainPvpKill();
                        player.getClient().getSession().write(MaplePacketCreator.serverNotice("您已經殺死了 " + attackedPlayers.getName() + "玩家!"));
                        attackedPlayers.getStat().gainPvpDeath();
                        attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(player.getName() + " 殺死了你!"));
                        //被殺死的會扣經驗值，不要最好啦．．
                        attackedPlayers.gainExp(attackedPlayers.getExp() / -10, false, false,true);
                        if(attackedPlayers.getExp() < 0) {
                            attackedPlayers.gainExp(attackedPlayers.getExp() + attackedPlayers.getExp() * -1, true, false,true);
                        }
                }
                }else if(getPvpMode()==2){ //掉隨機裝備欄物品
                  
                }else if(getPvpMode()==3){ //死了就被關起來
                    if (attackedPlayers.getHp() <= 0 && !attackedPlayers.isAlive()) {
                        attackedPlayers.changeMap(1);
                    }   
                    player.getClient().getSession().write(MaplePacketCreator.serverNotice("您已經殺死了 " + attackedPlayers.getName() + "玩家!"));
                    attackedPlayers.getClient().getSession().write(MaplePacketCreator.serverNotice(player.getName() + " 殺死了你!"));
                }
                map.killMonster(pvpMob, player, false, false,(byte) -1);
    }

    public static void doPvP(MapleCharacter player, MapleMap map, AttackInfo attack) {
        DamageBalancer(attack);
        getDirection(attack);
        for (MapleCharacter attackedPlayers : player.getMap().getNearestPvpChar(player.getPosition(), maxDis, maxHeight, player.getMap().getCharacters())) {
            if (attackedPlayers.isAlive() && (player.getParty() == null || player.getParty() != attackedPlayers.getParty()) && getPvP()==1) {
                monsterBomb(player, attackedPlayers, map, attack);
            }
        }
    }
    
    public static int getPvP(){
        return decidepvp;
    }
    
    public static void setPvP(){
        decidepvp = 1;
    }
    
    public static void resetPvP(){
        decidepvp = 0;
    }
    
    public static int getPvpMode(){
        return mode;
    }
    
    public static void setPvpMode(int type){
        mode = type;
    }
}
