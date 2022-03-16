var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	    cm.givePartyItems(4001161, 0, true);
	    cm.givePartyItems(4001162, 0, true);
	    cm.givePartyItems(4001163, 0, true);
	    cm.givePartyItems(4001169, 0, true);
	    cm.givePartyItems(2270004, 0, true);
	cm.sendSimple("#b#L0#我要兌換亞泰爾耳環#l\r\n#L1#我要兌換藍色亞泰爾耳環#l\r\n#L2#我要進入毒霧森林#l#k");
    } else if (status == 1) {
	if (selection == 0) {
	    if (!cm.haveItem(1032060) && cm.haveItem(4001198, 20)) {
		cm.gainItem(1032060,1, true);
		cm.gainItem(4001198, -20);
	    } else {
		cm.sendOk("你需要20個亞泰爾碎片,或者是你已經有亞泰爾耳環了");
	    }
	} else if (selection == 1){
	    if (cm.haveItem(1032060) && !cm.haveItem(1032061) && cm.haveItem(4001198, 30)) {
		cm.gainItem(1032060,-1);
		cm.gainItem(1032061, 1, true);
		cm.gainItem(4001198, -30);
	    } else {
		cm.sendOk("你需要30個亞泰爾碎片跟亞泰爾耳環,或者是你已經有藍色亞泰爾耳環了");
	    }
	} else if (selection == 2) {
	    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
		cm.sendOk("找您的隊長來和我談話。");
	    } else {
		var party = cm.getPlayer().getParty().getMembers();
		var mapId = cm.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
                    if (ccPlayer == null || ccPlayer.getLevel() < 45 || ccPlayer.getLevel() > 55) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 4 : 1);
		}	
		if (next && size >= 4) {
			var em = cm.getEventManager("Ellin");
			if (em == null) {
				cm.sendOk("當前副本有問題，請聯絡管理員....");
			} else {
				var prop = em.getProperty("state");
                if (prop.equals("0") || prop == null) {
					em.startInstance(cm.getParty(), cm.getMap());
					cm.dispose();
					return;
				} else {
					cm.sendOk("裡面已經有人了,請你稍後在進入看看,或者是換頻");
				}

			}
		} else {
			cm.sendOk("你的隊伍4個(含)以上45~55的隊員才能進入");
		}
	    }
	}
	cm.dispose();
    }
}