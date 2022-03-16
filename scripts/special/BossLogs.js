var status = -1;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	var pap = cm.getChar().getBossLog("pop");
	var horntail = cm.getChar().getBossLog("闇黑龍王");
	var lionbear = cm.getChar().getBossLog("熊獅王次數");
	var shoaling = cm.getChar().getBossLog("shaoling");
	if (cm.getChar().getBossLog("pop")==-1){
		pap=0;
	}
	if(cm.getChar().getBossLog("闇黑龍王")==-1){
		horntail=0;
	}
	if(cm.getChar().getBossLog("熊獅王次數")==-1){
		lionbear=0;
	}
	if(cm.getChar().getBossLog("shaoling")==-1){
		shaoling=0;
	}
	var vip = 2;
	if(cm.haveItem(4009324,1)&&!cm.haveItem(4009325,1)&&!cm.haveItem(4009326,1)){
		vip=3;
	}
	if(cm.haveItem(4009325,1)&&!cm.haveItem(4009326,1)){
		vip=4;
	}
	if(cm.haveItem(4009326,1)){
		vip=5;
	}
	
	if (mode == 1) {
		status++;
	} else {
		status--;
	}
	if (status == 0) {
		cm.sendSimple("親愛的 #h \r\n 您好我是#p9209006#\r\n以下是您今天各種東西挑戰次數\r\n#r請注意!!!\r\n每日挑戰次數是以今天完成兩次開始算起的24小時#k \r\n熊獅王挑戰次數: "+lionbear+"/"+vip+"\r\n闇黑龍王挑戰次數 "+horntail+"/"+vip+"\r\n拉圖斯挑戰次數: "+pap+"/2\r\n妖僧次數: "+shaoling+"/100\r\n");
	}
	cm.dispose();
}