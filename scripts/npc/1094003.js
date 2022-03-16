﻿/* Author: Xterminator
	NPC Name: 		Bush
	Map(s): 		Victoria Road : Nautilus Harbor (120000000)
	Description: 		Quest
*/
var status = 0;
var item;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.getQuestStatus(2186) == 1) {
	    var rand = Math.floor(Math.random() * 2);
	    if (rand == 0 && !cm.haveItem(4031853)) {
		item = 4031853;
	    } else if (rand == 1) {
		item = 4031854;
	    } else {
		item = 4031855;
	    }
	    cm.gainItem(item, 1);
	    if (item == 4031853) {
		cm.sendNext("我發現了眼鏡！！");
	    } else {
		cm.sendOk("我發現了一副眼鏡但它似乎不是真正的眼鏡...");
	    }
	} else {
		cm.sendOk("我是參加歌唱比賽的前三名唷...");
		cm.dispose();
	}
	cm.dispose();
}
}