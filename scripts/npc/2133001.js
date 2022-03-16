﻿var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    switch(cm.getPlayer().getMapId()) {
	case 930000000:
	    cm.sendNext("歡迎，請進入。");
	    break;
	case 930000100:
	    cm.sendNext("我們必須消除所有這些怪物的污染！");
	    break;
	case 930000200:
	    cm.sendNext("我們必須消除所有這些被污染的反應堆！");
	    break;
	case 930000300:
	    cm.warpParty(930000400);
	    break;
	case 930000400:
	    if (cm.haveItem(4001169,10)) {
                cm.warpParty(930000500, 0);
		cm.gainItem(4001169,-10);
	    } else if (!cm.haveItem(2270004)) {
		cm.gainItem(2270004,10);
		cm.sendOk("請淨化這些怪物");
	    } else {
		cm.sendOk("請給我10個怪物株!");
	    }
	    break;
	case 930000600:
	    cm.sendNext("就是這個！");
	    break;
	case 930000700:
            if (cm.canHold(4001198,1)) {
                cm.gainItem(4001198,1);
                cm.gainExp(52000);
	        cm.getPlayer().endPartyQuest(1206);
	        cm.removeAll(4001161);
	        cm.removeAll(4001162);
	        cm.removeAll(4001163);
	        cm.removeAll(4001164);
	        cm.removeAll(4001169);
	        cm.removeAll(2270004);
	        cm.warp(930000800,0);
	} else {
		cm.getPlayer().dropMessage(5, "請確認你的其他欄有沒有滿");
	}
	    break;
    }
    cm.dispose();
}
