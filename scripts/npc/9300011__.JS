load('nashorn:mozilla_compat.js');
importPackage(Packages.handling.world);

var points;

function start() {
    var record = cm.getQuestRecord(150001);
    points = record.getCustomData() == null ? "0" : record.getCustomData();
    cm.sendSimple(
        "財神到，新年發大財！！\r\n"
        + "#L87#我想要#b領取888楓葉點數#k#l\r\n"
        + "#L89#我想要#b進入活動地圖#k#l\r\n"
        + "#L90#我想要#b兌換入場券#k#l\r\n"
        + "#L90#我想要#b兌換新年好禮#k#l\r\n"

        // + "想挑戰BOSS副本嗎？？\n\r\n\r #b#L3#查看可兌換點數#l#k \r\n\r\n #b#L0##v03994115##l#l#L1##v03994116##l#L2##v03994117##l#L28##v03994118##l \r\n #b#L4#進入幸福村#l#k"
    );
}

function action(mode, type, selection) {
    if (mode === 1) {
        switch (selection) {
            case 89:
            {
                if (cm.getParty() == null) {
                    cm.sendOk("你是邊緣人？");
                } else if (!cm.isLeader()) {
                    cm.sendOk("必須由隊長來發號施令。");
                } else {
                    var members = cm.getParty().getMembers();

                    if (members.size() < 1) {
                        cm.sendNext("至少需要" + 1 + "人");
                        cm.dispose();
                        return;
                    }

                    var next = true;
                    var it = members.iterator();
                    var msg = "";

                    while (it.hasNext()) {
                        var cPlayer = it.next();
                        var chr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(cPlayer.getId());

                        if (cPlayer.getMapid() !== cm.getMapId()) {
                            next = false;
                            msg += cPlayer.getName() + " 不在這裡\r\n";
                            //break;
                        }

                        if (!chr.haveItem(4110001, 1)) {
                            next = false;
                            msg += cPlayer.getName() + " 這位害群之馬沒有入場券\r\n";
                            //break;
                        }
                    }
                    if (!next) {
                        cm.sendNext(msg);
                        cm.dispose();
                        return;
                    }

                    var q = cm.getEventManager("NewYear2018");
                    if (q == null) {
                        cm.sendOk("找不到腳本，請聯繫GM！");
                        cm.dispose();
                        return;
                    } else {
                        cm.givePartyItems(4110001, -1);
                        q.startInstance(cm.getParty(), cm.getMap());
                    }

                }
                break;
            }
            case 87:
            {
                if (cm.getPlayer().getLevel() < 30) {
                    cm.sendOk("角色必須 30 等才能領唷！");
                } else if (cm.getPlayer().getPrizeLog('2018888') >= 1) {
                    cm.sendOk("你已經領過囉！");
                } else {
                    cm.getPlayer().modifyCSPoints(2, 888, true);
                    cm.getPlayer().setPrizeLog('2018888');
                    cm.sendOk("恭喜發財！");
                }
                break;
            }
            case 90:
            {
                cm.sendSimple(
                    "您想要兌換什麼呢？\r\n"
                    + "#L100#我要用 #i4032053#x5000 兌換 #v4110001##z4110001##l\r\n"
                    + "#L101#我要用 #i4032053#x3000 兌換 #v3010798##z3010798##l\r\n"
                    + "#L102#我要用 #i4032053#x5000 和 #i3993001#x30 兌換 #v1113077##z1113077##l\r\n"
                    + "#L103#我要用 #i4032053#x5000 和 #i3993001#x30 兌換 #v1142590##z1142590##l\r\n"
                );
                return;
            }
            case 100:
            {
                if (!cm.haveItem(4032053, 5000)) {
                    cm.sendOk("你身上的 #i4032053# 不夠！");
                } else {
                    cm.gainItem(4032053, -5000);
                    cm.gainItem(4110001, 1);
                    cm.sendOk("恭喜發財！");
                }
                break;
            }
            case 101:
            {
                if (!cm.haveItem(4032053, 3000)) {
                    cm.sendOk("你身上的 #i4032053# 不夠！");
                } else {
                    cm.gainItem(4032053, -3000);
                    cm.gainItem(3010798, 1);
                    cm.sendOk("恭喜發財！");
                }
                break;
            }
            case 102:
            {
                if (!cm.haveItem(4032053, 5000)) {
                    cm.sendOk("你身上的 #i4032053# 不夠！");
                } else if (!cm.haveItem(3993001, 30)) {
                    cm.sendOk("你身上的 #i3993001# 不夠！");
                } else {
                    cm.gainItem(4032053, -5000);
                    cm.gainItem(3993001, -30);
                    cm.gainItem(1113077, 1);
                    cm.sendOk("恭喜發財！");
                }
                break;
            }
            case 103:
            {
                if (!cm.haveItem(4032053, 5000)) {
                    cm.sendOk("你身上的 #i4032053# 不夠！");
                } else if (!cm.haveItem(3993001, 30)) {
                    cm.sendOk("你身上的 #i3993001# 不夠！");
                } else {
                    cm.gainItem(4032053, -5000);
                    cm.gainItem(3993001, -30);
                    cm.gainItem(1142590, 1);
                    cm.sendOk("恭喜發財！");
                }
                break;
            }
            case 0:
                if (cm.getParty() != null) {
                    if (cm.getDisconnected("BossQuestEASY") != null) {
                        cm.getDisconnected("BossQuestEASY").registerPlayer(cm.getPlayer());
                    } else if (cm.isLeader()) {
						if (World.isShutDown) {
							cm.sendNext("因為伺服器在維修中，所以暫時不能打BOSSPQ。");
							cm.dispose();
							return;
						}
                        var party = cm.getPlayer().getParty().getMembers();
                        var mapId = cm.getPlayer().getMapId();
                        var next = true;
                        var it = party.iterator();
                        while (it.hasNext()) {
                            var cPlayer = it.next();
                            var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
                            if (ccPlayer == null || ccPlayer.getLevel() < 70) {
                                next = false;
                                break;
                            }
                        }	
                        if (next) {
                            var q = cm.getEventManager("BossQuestEASY");
                            if (q == null) {
                                cm.sendOk("找不到腳本，請聯繫GM！");
								cm.dispose();
								break;
                            } else {
                                q.startInstance(cm.getParty(), cm.getMap());
                            }
                        } else {
                            cm.sendOk("全部隊友必須達到70等.");
							cm.dispose();
							break;
                        }
                    } else {
                        cm.sendOk("你不是隊長.");
						cm.dispose();
						break;
                    }
                } else {
                    cm.sendOk("你沒有隊伍.");
					cm.dispose();
					break;
                }
                break;
            case 1:
                if (cm.getParty() != null) {
                    if (cm.getDisconnected("BossQuestMed") != null) {
                        cm.getDisconnected("BossQuestMed").registerPlayer(cm.getPlayer());
                    } else if (cm.isLeader()) {
						if (World.isShutDown) {
							cm.sendNext("因為伺服器在維修中，所以暫時不能打BOSSPQ。");
							cm.dispose();
							return;
						}
                        var party = cm.getPlayer().getParty().getMembers();
                        var mapId = cm.getPlayer().getMapId();
                        var next = true;
                        var it = party.iterator();
                        while (it.hasNext()) {
                            var cPlayer = it.next();
                            var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
                            if (ccPlayer == null || ccPlayer.getLevel() < 100) {
                                next = false;
                                break;
                            }
                        }	
                        if (next) {
                            var q = cm.getEventManager("BossQuestMed");
                            if (q == null) {
                                cm.sendOk("找不到腳本，請聯繫GM！");
								cm.dispose();
								break;
                            } else {
                                q.startInstance(cm.getParty(), cm.getMap());
                            }
                        } else {
                            cm.sendOk("全部隊友必須達到100等.");
							cm.dispose();
							break;
                        }
                    } else {
                        cm.sendOk("你不是隊長.");
						cm.dispose();
						break;
                    }
                } else {
                    cm.sendOk("你沒有隊伍.");
					cm.dispose();
					break;
                }
                break;
            case 2:
                if (cm.getParty() != null) {
                    if (cm.getDisconnected("BossQuestHARD") != null) {
                        cm.getDisconnected("BossQuestHARD").registerPlayer(cm.getPlayer());
                    } else if (cm.isLeader()) {
						if (World.isShutDown) {
							cm.sendNext("因為伺服器在維修中，所以暫時不能打BOSSPQ。");
							cm.dispose();
							return;
						}
                        var party = cm.getPlayer().getParty().getMembers();
                        var mapId = cm.getPlayer().getMapId();
                        var next = true;
                        var it = party.iterator();
                        while (it.hasNext()) {
                            var cPlayer = it.next();
                            var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
                            if (ccPlayer == null || ccPlayer.getLevel() < 120) {
                                next = false;
                                break;
                            }
                        }	
                        if (next) {
                            var q = cm.getEventManager("BossQuestHARD");
                            if (q == null) {
                                cm.sendOk("找不到腳本，請聯繫GM！");
								cm.dispose();
                            } else {
                                q.startInstance(cm.getParty(), cm.getMap());
                            }
                        } else {
                            cm.sendOk("全部隊友必須達到120等.");
							cm.dispose();
							break;
                        }
                    } else {
                        cm.sendOk("你不是隊長.");
						cm.dispose();
						break;
                    }
                } else {
                    cm.sendOk("你沒有隊伍.");
					cm.dispose();
					break;
                }
                break;
            case 28:
                if (cm.getParty() != null) {
                    if (cm.getDisconnected("BossQuestHELL") != null) {
                        cm.getDisconnected("BossQuestHELL").registerPlayer(cm.getPlayer());
                    } else if (cm.isLeader()) {
						if (World.isShutDown) {
							cm.sendNext("因為伺服器在維修中，所以暫時不能打BOSSPQ。");
							cm.dispose();
							return;
						}
                        var party = cm.getPlayer().getParty().getMembers();
                        var mapId = cm.getPlayer().getMapId();
                        var next = true;
                        var it = party.iterator();
                        while (it.hasNext()) {
                            var cPlayer = it.next();
                            var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
                            if (ccPlayer == null || ccPlayer.getLevel() < 160) {
                                next = false;
                                break;
                            }
                        }	
                        if (next) {
                            var q = cm.getEventManager("BossQuestHELL");
                            if (q == null) {
                                cm.sendOk("找不到腳本，請聯繫GM！");
								cm.dispose();
                            } else {
                                q.startInstance(cm.getParty(), cm.getMap());
                            }
                        } else {
                            cm.sendOk("全部隊友必須達到160等.");
							cm.dispose();
							break;
                        }
                    } else {
                        cm.sendOk("你不是隊長.");
						cm.dispose();
						break;
                    }
                } else {
                    cm.sendOk("你沒有隊伍.");
					cm.dispose();
					break;
                }
                break;
            case 3:
                cm.sendOk("#b點數數量 : " + points);
				cm.dispose();
                break;
            case 4:
                cm.warp(209000000);
                break;
        }
    }
    cm.dispose();
}
