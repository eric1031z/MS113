package server.life;

import client.MapleCharacter;
import client.SkillFactory;
import client.anticheat.CheatTracker;
import client.status.MonsterStatus;
import handling.channel.handler.InventoryHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//import net.server.channel.handlers.ItemPickupHandler;
//import static net.server.channel.handlers.ItemPickupHandler.useItem;
import handling.world.MaplePartyCharacter;
import java.lang.ref.WeakReference;
import java.util.concurrent.ScheduledFuture;
//import scripting.item.ItemScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Timer;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import tools.MaplePacketCreator;
import server.Randomizer;
import server.Timer.MapTimer;
import server.Timer.MobTimer;
import tools.packet.MobPacket;

/**
 *
 * Edit LYZ
 */
public class MapleMonsterPet extends MapleMonster {
    //private WeakReference<MapleCharacter> controller = new WeakReference<>(null);
    final static int baseHp = 10000;
    MapleCharacter owner;
    int mobSkills = 0;
    //MapleCharacter control = controller.get();
    private long lastSpawnTime = 0;
     

    public enum MapleMonsterPetSkill {
        None(0),
        Attack(1),
        Heal(2),
        ItemPickup(3);
        int skillId;

        MapleMonsterPetSkill(int thisSkillId) {
            skillId = thisSkillId;
        }

        public int getSkillId() {
            return skillId;
        }
    }

    public MapleMonsterPet(int mobId, MapleCharacter mobowner) {
        super(MapleLifeFactory.getMonster(mobId));
        this.owner = mobowner;
        this.getStats().setHp(baseHp);
        this.setHp(baseHp);
        //this.setMp(0);
        this.getStats().setRevives(new ArrayList<Integer>());
        owner.getMap().spawnFakeMonsterOnGroundBelow(this, owner.getPosition());
        this.switchController(owner, true);
        this.cancelDropItem();
        doAction();
    }
    
    public MapleMonsterPetSkill getPetSkill(){
        MapleMonsterPetSkill pskill = MapleMonsterPetSkill.None;
        return pskill;
    }
    
    public int getPSkillId(){
        return getPetSkill().getSkillId();
    }

    public boolean hasSkill(MapleMonsterPetSkill skill,int skillId) {
        return mobSkills == skillId;
    }

    public void giveSkill(MapleMonsterPetSkill skill) {
        mobSkills |= (int) Math.pow(2, skill.getSkillId());
    }

    public void setSkills(int newSkills) {
        mobSkills = newSkills;
    }
    
    public void setNewPet(int monsterId){
        new MapleMonsterPet(monsterId,owner);
    } //用新寵物
    
    /*public boolean canNewSpawn() {
        long time = 300 * 1000;
        if (owner != null) {
            if (lastSpawnTime + time > System.currentTimeMillis()) {
                return false;
            }
        }
        lastSpawnTime = System.currentTimeMillis();
        return true;
    }//可以再召喚嗎? 設定5分鐘以後*/
    
    public int getPskill(){
        return mobSkills;
    } //召喚出來寵物的技能
    

    final public void doAction() {
        final MapleMonsterPet mmPet = this;
        
        
        MobTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (mmPet.isAlive()) {
                    int petDamage = 0;
                    mmPet.getMap().damageMonster(owner,mmPet, Randomizer.nextInt(200)); //來 但最後她媽會掉東西
  
                    try {
                        if (mmPet.getMap() != owner.getMap() || mmPet.getPosition().distance(owner.getPosition()) > 350.0) {
                            MapleMonsterPet nextPet = new MapleMonsterPet(mmPet.getId(), owner);
                            nextPet.setHp(mmPet.getHp());
                            nextPet.mobSkills = mmPet.mobSkills;
                            mmPet.getMap().killMonster(mmPet,null,false,false,(byte)-1); //再來
                            return;
                        }
                        if (Randomizer.nextDouble() < 0.3) {
                            if (mobSkills == 1) {
                                int range = 150;
                                boolean thereIsMonster = false; // Checks if there is attackable monster
                                for (MapleMapObject monstermo : mmPet.getMap().getMapObjectsInRange(mmPet.getPosition(), range * range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                                    MapleMonster monster = (MapleMonster) monstermo;
                                    if (!monster.isFake() && monster != mmPet) {
                                        thereIsMonster = true;
                                    }
                                }
                                if (thereIsMonster) {
                                    mmPet.getMap().spawnMist(new MapleMist(new java.awt.Rectangle(mmPet.getPosition().x, mmPet.getPosition().y - 50, range, range), owner, SkillFactory.getSkill(12111005).getEffect(30)), 1500, false); //來
                                    for (MapleMapObject monstermo : mmPet.getMap().getMapObjectsInRange(mmPet.getPosition(), range * range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                                        MapleMonster monster = (MapleMonster) monstermo;
                                        if (!monster.isFake() && monster != mmPet) {
                                            int dmg = Randomizer.nextInt((int)monster.getMobMaxHp() / ((monster.getStats().isBoss()) ? 400 : 4)) + ((monster.getStats().isBoss()) ? 0 : (int)(monster.getMobMaxHp() / 4)); //還要我轉換
                                            mmPet.getMap().broadcastMessage(owner, MobPacket.damageMonster(monster.getObjectId(), dmg), true);
                                            mmPet.getMap().damageMonster(owner,monster, dmg); //還要我誇號
                                            petDamage += Randomizer.nextInt(500);
                                        }
                                    }
                                }
                            }
                            if (mobSkills==2) {
                                if ((owner.getMaxHp() != owner.getHp()) || (owner.getMaxMp() != owner.getMp())) {
                                    int hpToAdd = owner.getMaxHp() / 3;
                                    hpToAdd = Math.min(hpToAdd, owner.getMaxHp() - owner.getHp());
                                    owner.addHP(hpToAdd);
                                    int mpToAdd = owner.getMaxMp() / 5;
                                    mpToAdd = Math.min(mpToAdd, owner.getMaxMp() - owner.getMp());
                                    owner.addMP(mpToAdd);
                                    owner.getClient().getSession().write(MaplePacketCreator.showOwnBuffEffect(2301002, 2)); //= =
                                    owner.getMap().broadcastMessage(owner, MaplePacketCreator.showBuffeffect(owner.getId(), 2301002, 2), false); //??
                                    petDamage += Randomizer.nextInt(300);
                                }
                            }
                            if (mobSkills==3) {
                                int range = 250;
                                List<MapleMapObject> items = mmPet.getMap().getMapObjectsInRange(mmPet.getPosition(), range * range, Arrays.asList(MapleMapObjectType.ITEM));
                                for (MapleMapObject ob : items) {
                                    if (ob instanceof MapleMapItem) {
                                        MapleMapItem mapitem = (MapleMapItem) ob;
                                        synchronized (mapitem) {
                                            if (mapitem.isPickedUp()) {
                                                owner.getClient().sendPacket(MaplePacketCreator.getInventoryFull());
                                                owner.getClient().sendPacket(MaplePacketCreator.getShowInventoryFull());
                                                continue;
                                            }
                                            if (mapitem.getMeso() > 0) {
                                                if (owner.getParty() != null) {
                                                    int mesosamm = mapitem.getMeso();
                                                    if (mesosamm > 50000 * owner.getClient().getChannelServer().getMesoRate()) {
                                                        continue;
                                                    }
                                                    int partynum = 0;
                                                    for (MaplePartyCharacter partymem : owner.getParty().getMembers()) {
                                                        if (partymem.isOnline() && partymem.getMapid() == owner.getMap().getId() && partymem.getChannel() == owner.getClient().getChannel()) {
                                                            partynum++;
                                                        }
                                                    }
                                                    for (MaplePartyCharacter partymem : owner.getParty().getMembers()) {
                                                        if (partymem.isOnline() && partymem.getMapid() == owner.getMap().getId()) {
                                                            MapleCharacter somecharacter = owner.getClient().getChannelServer().getPlayerStorage().getCharacterById(partymem.getId());
                                                            if (somecharacter != null) {
                                                                somecharacter.gainMeso(mesosamm / partynum, true, true, false);
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    owner.gainMeso(mapitem.getMeso(), true, true, false);
                                                }
                                            } else if (mapitem.getItem().getItemId() / 10000 == 243) {
                                                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                                                
                                                    if (!MapleInventoryManipulator.addFromDrop(owner.getClient(), mapitem.getItem(), true)) {
                                                        owner.getClient().sendPacket(MaplePacketCreator.enableActions());
                                                        continue;
                                                    }
                                                
                                            } else if (InventoryHandler.useItem(owner.getClient(), mapitem.getItem().getItemId())) { //人真好
                                                if (mapitem.getItem().getItemId() / 10000 == 238) {
                                                    owner.getMonsterBook().addCard(owner.getClient(), mapitem.getItem().getItemId());
                                                }
                                            } else if (MapleInventoryManipulator.addFromDrop(owner.getClient(), mapitem.getItem(), true)) {
                                            } else if (mapitem.getItem().getItemId() == 4031868) {
                                                owner.getMap().broadcastMessage(MaplePacketCreator.updateAriantPQRanking(owner.getName(), owner.getItemQuantity(4031868, false), false));
                                            } else {
                                                owner.getClient().sendPacket(MaplePacketCreator.enableActions());
                                                continue;
                                            }
                                            mapitem.setPickedUp(true);
                                            owner.getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 2, owner.getId()), mapitem.getPosition());
                                            owner.getMap().removeMapObject(ob);
                                        }
                                    }
                                }

                            }
                        }
                        if (petDamage > 0) {
                            mmPet.getMap().broadcastMessage(owner, MobPacket.damageMonster(mmPet.getObjectId(), petDamage), true);
                            mmPet.getMap().damageMonster(owner, mmPet, petDamage);
                        }
                        doAction();
                        
                        
                    } catch (Exception ex) {
                        mmPet.getMap().killMonster(mmPet,null,false,false,(byte)-1); //= =到底
                    }
                }
            }
        }, 1000);
    } 
 }



