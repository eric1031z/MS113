

var status = -1;
var select = -1;

/* Clear inv */
var ClearText = "";
var ClearUp = 0;
var ClearTitle = Array("裝備欄", "消耗欄", "裝飾欄", "其他欄", "特殊欄");
var slot = Array();
var startnum = 0;
var endnum = 0;



function action(mode, type, selection) {
	if (mode === 1) {
		status++;
	} else if (mode === 0) {
		status--;
	}

	var i = -1;
	if (status <= i++) {
		cm.dispose();
	} else if (status === i++) {
		cm.sendGetText("請輸入你要對GM傳送的訊息");
	} else if (status === i++) {
		var text = cm.getText();
		if (text === null || text === "") {
			cm.sendOk("並未輸入任何內容.");
			cm.dispose();
			return;
		}
		cm.dispose();
		cm.processCommand("@CGM " + text);
	} else {
		cm.dispose();
	}
}