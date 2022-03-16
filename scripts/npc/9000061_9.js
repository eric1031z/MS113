var status = -1;
var rawnItems = [
	//點數裝備
	//[1061039, 4000209, 10, 0,1],
    //[1060026, 4000209, 10, 0,1],
	[2022671, 4000217, 20, 0,1],
	[2022672, 4000217, 20, 0,1],
	[2022673, 4000217, 20, 0,1],
	[5150038, 4000217, 50, 0,1]	
];
function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 0 && status == 0) {
		cm.dispose();
		return;
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		var mapId = cm.getMapId();
		var outText, msg = "", sel;
		if (mapId > 0) {
			if (status == 0) {
				for (var i = 0; i < rawnItems.length; i++) {
					msg += "\r\n#L" + i + "##b用" + rawnItems[i][2] + "個#v"+ rawnItems[i][1] + "#兌換"+rawnItems[i][4]+"個#v"+rawnItems[i][0]+"#";
					//msg += "# " + rawnItems[i][2] + "個";
				}
				cm.sendSimple("#b[仙女GM卡兌換專區]#k\r\n如果有蒐集到GM卡片的話可以找我兌換獎勵唷!\r\n你想要兌換哪一種GM卡片的獎勵呢?\r\n"+ msg);
			} else if (status == 1) {
				sel = selection;
				var gainItem = rawnItems[sel][0];
				var needitem = rawnItems[sel][1];
				var quatity = rawnItems[sel][2];
				var meso = rawnItems[sel][3];
				var amount = rawnItems[sel][4];
				
				if (sel > 8 || sel < 0) {
					cm.dispose();
					return;
				}
				if (cm.canHold(gainItem, amount)) {
					if (cm.haveItem(needitem, quatity) && cm.getPlayer().getMeso() >= meso) {
						cm.gainItem(needitem, -quatity);
						cm.gainMeso(-meso);
						cm.gainItem(gainItem, amount);
						cm.sendNext("恭喜兌換完成!");
						status = -1;
					} else {
						cm.sendNext("您兌換的物品者所需的道具需求不足，請再次確認!");
					}

				} else {
					cm.sendNext("請確認是否背包空間足夠!");
				}
				cm.dispose();
			}
		} else {
			if (mapId == 180000000) {
				outText = "你確定要離開BOSS PQ？？";
			}
			if (status == 0) {
				cm.sendYesNo(outText);
			} else if (mode == 1) {
				cm.warp(910000000, 0); // 回自由
				cm.dispose();
			}
		}
	}
}
