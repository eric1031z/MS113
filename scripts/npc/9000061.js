/* global cm */

var status = -1;
var select = -1;

function start() {
    cm.sendSimple("#b[簽到及領獎兌換區]#k\r\n如果有蒐集到GM卡片的話可以找我兌換獎勵唷!\r\n你想要兌換哪一種GM卡片的獎勵呢?\r\n"
	+ "#L8##b我在線滿兩小時了#v4000114#我要簽到領獎勵!\r\n"
	+ "#L1#我要用小龍女GM卡#v4030002#兌換獎勵!\r\n"
	+ "#L2#我要用BBGM卡#v4030003#兌換獎勵!\r\n"
	+ "#L3#我要用咕咕雞GM卡#v4030004#兌換獎勵!\r\n"
	+ "#L4#我要用花花GM卡#v4030005#兌換獎勵!\r\n"
	+ "#L5#我要用慈禧太后GM卡#v4030006#兌換獎勵!\r\n"
	+ "#L6#我要用暖男GM卡#v4030007#兌換獎勵!\r\n"
	+ "#L7#我要用QQGM卡#v4030008#兌換獎勵!\r\n"
	+ "#L9#我要用仙女GM卡#v4000217#兌換獎勵!\r\n"
	+ "#L87##r我要垃圾回收= =#k\r\n"
    );
}

function action(mode, type, selection) {
    if (select === -1) {
        select = selection;
    }
    switch (select) {
		
        case 1: {
			cm.dispose();
            cm.openNpc(9000061,1);
			 break;
        }
        case 2: {
			cm.dispose();
            cm.openNpc(9000061,2);
             break;
        }
		 case 3: {
			cm.dispose();
            cm.openNpc(9000061,3);
             break;
        }
		case 4: {
			cm.dispose();
            cm.openNpc(9000061,4);
			 break;
		}
		case 5: {
			cm.dispose();
            cm.openNpc(9000061,5);
			 break;
		}
		case 6: {
			cm.dispose();
            cm.openNpc(9000061,6);
			 break;
		}
		case 7: {
			cm.dispose();
            cm.openNpc(9000061,7);
			 break;
		}
		case 9: {
			cm.dispose();
            cm.openNpc(9000061,9);
			 break;
		}
		case 87 : {
			cm.dispose();
			cm.openNpc(9000061,87);
			 break;
		}
		case 8: {
			var itemSet = new Array(4000209, 4000211, 4000214, 4000217, 2450000, 2022463, 2450000, 2022463, 2022463, 2022530, 2250000, 2022220, 2022567, 2022567 );
			var rand = Math.floor(Math.random() * itemSet.length);
			
			if (cm.getOnlineHours() < 2) {
				cm.sendOk("您上線的時間 #r" + getOnlineTime() + "#k 未滿 2 小時\r\n");
				cm.dispose();
			} else if (!cm.canHold(itemSet[rand])) {
				cm.sendOk("您的背包已滿,請確認所有欄位是否都有空出一格!");
				cm.dispose();
			} else if (cm.getChar().getAcLog("2hr") >= 1) {
				cm.sendOk("您今天已經領過了!!");
				cm.dispose();
			} else {
				cm.gainItem(itemSet[rand], 1);
				cm.getChar().setAcLog("2hr");
				cm.sendOk("您已簽到完成,簽到獎勵已經發放至您的背包!");
				cm.worldMessage(6, "[簽到獎勵] " + " 玩家 " + cm.getChar().getName() + "在線上待滿兩小時成功領取了簽到獎勵!");
				cm.dispose();
			}
			break;
		}
		default: {
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
        cm.sendOk("你的等級不能小於10等!");
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
        cm.sendOk("你不能在這裡使用這個功能!");
    } else {
        if (script == null) {
            cm.openNpc(npcid);
        } else {
            cm.openNpc(npcid, script);
        }
    }
}
function getOnlineTime() {
	var sec = cm.getOnlineSeconds();
	return + parseInt(sec / 60 / 60) + ":" + parseInt(sec / 60 % 60) + ":" + parseInt(sec % 60 % 60 );
}