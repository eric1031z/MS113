load('nashorn:mozilla_compat.js');
importPackage(Packages.tools);

var debug = false;
var log = "半周年禮物";
var Items = [
	[5150041,33,-1],
	[5152049,33,-1],
	[2022179,6,-1],
	[2450000,6,-1],
	[5076000,66,-1],
	[1702717,1,-1],
	[1702563,1,7]
];
var MaplePoint = 0;
var Gash = 0;
var LevelCap = 50;

var text = "null";
var status = -1;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else if (mode == 0) {
		status--;
	} else {
		cm.dispose();
		return;
	}
	if (debug && !cm.getPlayer().isGM()) {
		cm.sendOk("還沒開放喔");
		cm.dispose();
		return;
	}
	if (status == 0) {
		if (cm.getPlayer().getPrizeLog(log) >= 1 && cm.getPlayer().getName() != "Toby") {
			text = "您的帳號已經領取過了喔！";
		} else if (cm.getPlayer().getLevel() < LevelCap) {
			text = "您的等級不足 " + LevelCap + " 喔！";
		} else {
			var slot = [0, 0, 0, 0, 0, 0];
			for (var i = 0; i < Items.length; i++) {
				var type = Math.floor(Items[i][0] / 1000000);
				if (Items[i][0] && Items[i][1])
					slot[type] += (type == 5 ? 1 : Items[i][1]);
			}
			for (var i = 1; i <= 5; i++)
				if (cm.getInventory(i).getNumFreeSlot() < slot[i])
					var full = true;
		}
		if (cm.getPlayer().getName() == "") {
			cm.sendOk(full);
			cm.dispose();
			return;
		}
		
		if (text != "null") {
			cm.sendOk(text);
			cm.dispose();
			return;
		}
		
		var msg = "咕咕雞半周年好禮!\r\n\r\n";
		for (var i = 0; i < Items.length; i++) {
			if (Items[i][0] && Items[i][1])
				msg += "#b#i" + Items[i][0] + "##z" + Items[i][0] + "# x" + Items[i][1] + "#k\r\n";
		}
		
		if (MaplePoint != 0)
			msg += "楓葉點數 " + MaplePoint + " 點\r\n";
		if (Gash != 0)
			msg += "GASH點數 " + Gash + " 點\r\n";
		if (full === true) {
			cm.sendOk(msg + "\r\n可惜您的腦容量不足?????");
			cm.dispose();
			return;
		}
		cm.sendYesNo(msg + "\r\n您要領取嗎?");
	} else if (status == 1) {
		if (debug) {
			cm.sendNext("獎勵皆未發放，請前往背包查收");
			cm.dispose();
			return;
		}
		for (var i = 0; i < Items.length; i++) {
			if (Items[i][0] && Items[i][1])
				cm.gainItemPeriod(Items[i][0], Items[i][1],Items[i][2]);
		}
		if (MaplePoint != 0)
			cm.getPlayer().modifyCSPoints(2, MaplePoint, true);
		if (Gash != 0)
			cm.getPlayer().modifyCSPoints(1, Gash, true);
		cm.getPlayer().setPrizeLog(log);
		cm.sendNext("獎勵皆已發放，請前往背包查收");
		FileoutputUtil.logToFile("logs/Data/" + log + "領取.txt", "領取時間:" + FileoutputUtil.NowTime() + "角色名稱:" + cm.getPlayer().getName() + "角色等級: " + cm.getPlayer().getLevel() + "\r\n");
		cm.dispose();
	} else {
		cm.dispose();
	}
}
