


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
	} else 
		status--;
	

	
	if (mode == 0) {
		cm.dispose();
	} 
	if (status == 0) {
		var gain = cm.getMP();
		if (gain <= 0) {
			cm.sendOk("目前沒有任何在線點數唷。");
			cm.dispose();
			return;
		} else {
			cm.sendYesNo("目前楓葉點數: " + cm.getMaplePoint() + "\r\n" + "目前在線點數已經累積: " + gain + " 點，是否領取?");
		}
	} else if (status == 1) {
		var gain = cm.getMP();
		cm.setMP(0);
		cm.gainMaplePoint(gain);
		cm.save();
		cm.sendOk("領取了 " + gain + " 點在線點數, 目前楓葉點數: " + cm.getMaplePoint());
		cm.dispose();
	} else {
		cm.dispose();
	}
}