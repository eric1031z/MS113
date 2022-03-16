load('nashorn:mozilla_compat.js');
importPackage(Packages.tools);

var debug = false;
var status = -1;
var item = 4031217;
var d = new Date();
var gift = ["Day7", "Day1", "Day2", "Day3", "Day4", "Day5", "Day6"];
var month = d.getMonth() + 1;
var date = d.getDate();
var day = d.getDay();
var day10 = 30; /// 10等獎勵幾天 自己改

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
	
    if(debug && !cm.getPlayer().isAdmin()){
		cm.sendNext("本NPC目前維修中。");
		cm.dispose();
		return;
	}
	
	if (mode == 1) {
        status++;
    } else if (mode == 0) {
		status--;
    } else {
		cm.dispose();
		return;
	}
	
    if (status == 0) {
        cm.sendSimple("#b您好，我是領等級獎勵專用的NPC\r\n" +
				"#L1##d領取10等獎勵#l\r\n"+
				"#L2##d領取30等獎勵#l\r\n"+
				"#L3##d領取70等獎勵#l\r\n"+
				"#L4##d領取90等獎勵#l\r\n"+
				"#L5##d領取120等獎勵#l\r\n"+
				"#L6##d領取160等獎勵#l\r\n"+
				"#L7##d領取200等獎勵#l\r\n"
				)
				
    } else if (status == 1) {
		if (selection == 1) {
			if (cm.getPlayer().getPrizeLog('10等') < 1 && cm.getPlayer().getLevel() >= 10) {
				cm.getPlayer().modifyCSPoints(2, 2688, false);
			    cm.gainItem(2450000, 5);
				cm.gainItem(1142505, 1);
				cm.gainItem(1112127,1,false,day10,"");
				cm.gainItem(1122017,1,false,day10,"");
				cm.getPlayer().setPrizeLog('10等');
				cm.sendOk("已領取10等獎勵!");
			} else {
				cm.sendOk("#d你已經領過囉或者沒有10等以上!!");
			}
			cm.dispose();
		} else if (selection == 2) {
			if (cm.getPlayer().getPrizeLog('30等') < 1 && cm.getPlayer().getLevel() >= 30) {
				cm.getPlayer().modifyCSPoints(2, 666, false);
				cm.getPlayer().setPrizeLog('30等');
				cm.gainItem(2450000, 5);
				cm.sendOk("已領取30等獎勵!");
			} else {
				cm.sendOk("#d你已經領過囉或者沒有30等以上!!");
			}
			cm.dispose();
		} else if (selection == 3) {
			if (cm.getPlayer().getPrizeLog('70等') < 1 && cm.getPlayer().getLevel() >= 70) {
				cm.getPlayer().modifyCSPoints(2, 666, false);
				cm.gainItem(1912031, 1);
				cm.gainItem(1902038, 1);
				cm.gainItem(2450000, 5);
				cm.getPlayer().setPrizeLog('70等');
				cm.sendOk("已領取70等獎勵!");
			} else {
				cm.sendOk("#d你已經領過囉或者沒有70等以上!!");
			}
			cm.dispose();
		} else if (selection == 4) {
			if (cm.getPlayer().getPrizeLog('90等') < 1 && cm.getPlayer().getLevel() >= 90) {
				cm.getPlayer().modifyCSPoints(2, 666, false);

				cm.gainItem(2450000, 5);
				cm.getPlayer().setPrizeLog('90等');
				cm.sendOk("已領取90等獎勵!");
			}		else {
				cm.sendOk("#d你已經領過囉或者沒有90等以上!!");
			}
			cm.dispose();
		} else if (selection == 5) {
			if (cm.getPlayer().getPrizeLog('120等') < 1 && cm.getPlayer().getLevel() >= 120) {
				cm.getPlayer().modifyCSPoints(2, 666, false);
				cm.gainItem(2450000, 5);
				cm.getPlayer().setPrizeLog('120等');
				cm.sendOk("已領取120等獎勵!");
			} else {
				cm.sendOk("#d你已經領過囉或者沒有120等以上!!");
			}
			cm.dispose();
			} else if (selection == 6) {
			if (cm.getPlayer().getPrizeLog('160等') < 1 && cm.getPlayer().getLevel() >= 160) {
				cm.getPlayer().modifyCSPoints(2, 666, false);
				cm.gainItem(2450000, 5);
				cm.getPlayer().setPrizeLog('160等');
				cm.sendOk("已領取160等獎勵!");
			} else {
				cm.sendOk("#d你已經領過囉或者沒有160等以上!!");
			}
			cm.dispose();
		} else if (selection == 7) {
			if (cm.getPlayer().getPrizeLog('200等') < 1 && cm.getPlayer().getLevel() >= 200) {
				cm.getPlayer().modifyCSPoints(1, 2000, false);
				cm.gainItem(1902002 , 1);
				cm.gainItem(1912000, 1);
				cm.getPlayer().setPrizeLog('200等');
				cm.sendOk("已領取200等獎勵!");
				cm.worldMessage(6, "[轉蛋屋發出等級獎勵] " + " 玩家 " + cm.getChar().getName() + " 領了200等獎勵!");
			} else {
				cm.sendOk("#d你已經領過囉或者沒有200等以上!!");
			}
			cm.dispose();
		}
	}
}
