/* global cm */

var status = -1;
var select = -1;

function start() {
    cm.sendSimple(cm.getChannelServer().getServerName() + "你好我是魅力無限魔髮師，要用什麼來換髮型呢？\r\n"+
	"#L1#用通用整形券隨機更換臉型(分性別)\r\n" +
	"#L2#用傳說中的美髮券隨機更換髮型(分性別)\r\n" +
	"#L3#使用傳說中的美髮券隨機更換髮型(不分性別)\r\n" +
	"#L4#使用通用整形券隨機更換臉型\r\n" +
	"#L5#瀏覽小羊髮型\r\n"+
	"#L6#瀏覽小羊臉型\r\n"

    );
}

function action(mode, type, selection) {
    if (select === -1) {
        select = selection;
    }
    switch (select) {
        case 1: {
			cm.dispose();
            cm.openNpc(9105006,4);
			 break;
        }
        case 2: {
			cm.dispose();
            cm.openNpc(9105006,3);
             break;
        }
		 case 3: {
			cm.dispose();
            cm.openNpc(9105006,2);
             break;
        }
		case 4: {
			cm.dispose();
            cm.openNpc(9105006,1);
			 break;
        }
	    case 5: {
            cm.dispose();
            cm.openNpc(9105006,5);
            break;
		}
	    case 6: {
            cm.dispose();
            cm.openNpc(9105006,6);
            break;
		}			
            default : {
            cm.sendOk("此功能未完成");
            cm.dispose();
        }
    }
}

function openNpc(npcid) {
    openNpc(npcid, null);
}
function openNpc(npcid, script) {
    var mapid = cm.getMapId();
    cm.dispose();
    if (cm.getPlayerStat("LVL") < 10) {
        cm.sendOk("你的等級不能小於10等.");
    } else if (
            cm.hasSquadByMap() ||
            cm.hasEventInstance() ||
            cm.hasEMByMap() ||
            mapid >= 990000000 ||
            (mapid >= 680000210 && mapid <= 680000502) ||
            (mapid / 1000 === 980000 && mapid !== 980000000) ||
            mapid / 100 === 1030008 ||
            mapid / 100 === 922010 ||
            mapid / 10 === 13003000
    ) {
        cm.sendOk("你不能在這裡使用這個功能.");
    } else {
        if (script == null) {
            cm.openNpc(npcid);
        } else {
            cm.openNpc(npcid, script);
        }
    }
}