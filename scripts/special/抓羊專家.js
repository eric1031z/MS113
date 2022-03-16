/*
By 梓條、宗達
 */
var item = 2450000;
var itemAmount = 5;
var need = 4000306;
var needAmount = 200;
var log = '抓羊專家勳章記錄';
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
		cm.sendYesNo("#k是否要使用\r\n\r\n#r#t"+need+"##i"+need+"##bx"+needAmount+"#r換#b#t"+item+"# = #i"+item+":#?");
	} else if (status == 1) {
		if (cm.getPlayer().getOneTimeLog(log) >= 1 ) {
			cm.sendNext("您已經領過了#i"+item+":#");
			cm.dispose();
			return;
		} else if (!cm.haveItem(need, needAmount)) {
			cm.sendNext("您身上沒有#i"+need+"#"+needAmount+"個,請再次確認");
			cm.dispose();
			return;
		} else if (!cm.canHold(item, itemAmount)) {
			cm.sendNext("您的背包空間不足,請再次確認");
			cm.dispose();
			return;
		}
		cm.getPlayer().setOneTimeLog(log);
		cm.gainItem(need, -needAmount);
		cm.gainItem(item, itemAmount);
		cm.sendNext("恭喜獲得#t"+item+"#");
		cm.dispose();
	} else {
		cm.dispose();
	}
}
