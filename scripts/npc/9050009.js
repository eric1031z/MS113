load('nashorn:mozilla_compat.js');
importPackage(Packages.tools);

var log = "2018/1/8爆炸補償";
var item = 1702544;
var quantity = 1;
var MaplePoint = 0;
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
	if (status == 0) {
		if (cm.getPlayer().getPrizeLog(log) >= 1) {
			text = "您的帳號已經領取過了喔！";
		} else if (cm.getInventory(2).getNumFreeSlot() <= quantity && quantity > 0) {
			text = "您的消耗欄位空間不足10格唷";
		} else if (cm.getPlayer().getLevel() < 30) {
			text = "您的等級不足50";
		}
		if (text != "null") {
			cm.sendNext(text);
			cm.dispose();
			return;
		}
		
		text = "請問您是否要領取伺服器補償??";
		text += "本次補償獎勵為#v1702544##z1702544#x1?";
		cm.sendYesNo(text);
		text = "null";
	} else if (status == 1) {
		if (cm.getPlayer().getPrizeLog(log) >= 1) {
			text = "您的帳號已經領取過了喔！";
		} else if (cm.getInventory(1).getNumFreeSlot() <= quantity && quantity > 0) {
			text = "您的消耗欄位空間不足1格唷";
		} else if (cm.getPlayer().getLevel() < 30) {
			text = "您的等級不足30";
		}

		if (text != "null") {
			cm.sendNext(text);
			cm.dispose();
			return;
		}
		text = "請問您是否要領取伺服器補償??";
		cm.sendYesNo(text);
	} else if (status == 2) {
		if (quantity != 0) {
			cm.gainItem(item, quantity);
		}
		//cm.getPlayer().modifyCSPoints(2, MaplePoint, true);
		cm.getPlayer().setPrizeLog(log);
		cm.sendNext("補償皆已發放，請前往背包查收!");
		FileoutputUtil.logToFile("logs/Data/1月8日爆炸補償活動領取.txt", "領取時間:" + FileoutputUtil.NowTime() + "角色名稱:" + cm.getPlayer().getName() + "角色等級: " + cm.getPlayer().getLevel() + "\r\n");
		cm.dispose();
	} else {
		cm.dispose();
	}
}
