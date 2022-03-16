
/* global cm */

var status = -1;
var select = -1;

/* Clear inv */
var ClearText = "";
var ClearUp = 0;
var ClearTitle = Array("裝備欄", "消耗欄", "裝飾欄", "其他欄", "特殊欄");
var slot = Array();
var startnum = 0;
var endnum = 0;

function start(){
	status=-1;
	action(1, 0, 0);
}





function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else if (mode == 0) {
		status--;
	}

      
	if (mode==0) {
		cm.dispose();
	} 
	if (status == 0) {
		ClearText = "";
		for (var i = 0; i < ClearTitle.length; i++)
			ClearText += "\r\n#b#L" + i + "#" + ClearTitle[i] + "#l#k";

		cm.sendSimple("清除身上背包的道具是一個很慎重的事情!!\r\n#r請慎重做抉擇，若誤清GM不會負責!!" + ClearText);
	} else if (status == 1) {
		ClearText = ClearTitle[selection];
		switch (ClearText) {
		case '裝備欄':
			ClearUp = 1;
			break;
		case '消耗欄':
			ClearUp = 2;
			break;
		case '裝飾欄':
			ClearUp = 3;
			break;
		case '其他欄':
			ClearUp = 4;
			break;
		case '特殊欄':
			ClearUp = 5;
			break;
		}
		var avail = "";
		var dd = 0;
		for (var i = 0; i < 96; i++) {
			if (cm.getInventory(ClearUp).getItem(i) != null) {
				var itemId = cm.getInventory(ClearUp).getItem(i).getItemId();
				if (itemId == null) {
					i++; //防止下一步錯誤
				}
				avail += "#L" + Math.abs(i) + "##i" + cm.getInventory(ClearUp).getItem(i).getItemId() + "##z" + cm.getInventory(ClearUp).getItem(i).getItemId() + "##l\r\n";
			} else {
				dd++;
			}
			slot.push(i);
		}
		if (dd == 96) {
			cm.sendNext(ClearText + "沒有任何道具可以清除!");
			cm.dispose();
			return;
		}
		cm.sendSimple("想要從哪裡開始清除呢??\r\n#b" + avail);
	} else if (status == 2) {
		startnum = selection;
		var avail = "";
		for (var i = startnum; i < 96; i++) {
			if (cm.getInventory(ClearUp).getItem(i) != null) {
				avail += "#L" + Math.abs(i) + "##i" + cm.getInventory(ClearUp).getItem(i).getItemId() + "##z" + cm.getInventory(ClearUp).getItem(i).getItemId() + "##l\r\n";
			}
			slot.push(i);
		}
		cm.sendSimple("想要從哪裡結束清除呢??\r\n#b" + avail);
	} else if (status == 3) {
		endnum = selection;
		cm.dispose();
		cm.processCommand("@清除道具 " + ClearText + " " + startnum + " " + endnum);
	} else {
		cm.dispose();
	}
}