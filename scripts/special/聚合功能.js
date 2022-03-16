/* global cm */
load('nashorn:mozilla_compat.js');
importPackage(java.lang);

var status = -1;
var select = -1;

/* Clear inv */
var ClearText = "";
var ClearUp = 0;
var ClearTitle = Array("裝備欄", "消耗欄", "裝飾欄", "其他欄", "特殊欄");
var slot = Array();
var startnum = 0;
var endnum = 0;


/////////////////////////////////////// icon區

var serverImage = "#fEffect/ItemEff/theme/15#";

var icon2 = "#fEffect/ItemEff/icons/4#";
var icon1 = "#fEffect/ItemEff/icons/13#";
var icon3 = "#fEffect/ItemEff/icons/7#";

var n0 = "#fEffect/ItemEff/number/0#";
var n1 = "#fEffect/ItemEff/number/1#";
var n2 = "#fEffect/ItemEff/number/2#";
var n3 = "#fEffect/ItemEff/number/3#";
var n4 = "#fEffect/ItemEff/number/4#";
var n5 = "#fEffect/ItemEff/number/5#";
var n6 = "#fEffect/ItemEff/number/6#";
var n7 = "#fEffect/ItemEff/number/7#";
var n8 = "#fEffect/ItemEff/number/8#";
var n9 = "#fEffect/ItemEff/number/9#";


var online = "#fEffect/ItemEff/number/10#";
var online2 = "#fEffect/ItemEff/number/11#";
var point = "#fEffect/ItemEff/main/4#";
var point2 = "#fEffect/ItemEff/main/6#";


var theme1 = "#fEffect/ItemEff/theme/5#";
var theme2 = "#fEffect/ItemEff/theme/6#";
var theme3 = "#fEffect/ItemEff/theme/7#";
var theme4 = "#fEffect/ItemEff/theme/8#";
var theme5 = "#fEffect/ItemEff/theme/9#";
var theme6 = "#fEffect/ItemEff/theme/10#";
///////////////////////////////////////

/*
   
在線點數  
贊助兌換
*/
var menuList = [
    [theme2,"功能專區"],
	[theme5,"兌換專區"],
	[theme3,"獎勵專區"],
	[theme6,"購買專區"],
	[theme4,"造型專區"],
	[theme1,"活動專區"],
	

];




function getNumber(nn){
	var onu = "";
	for(var u = 0; u < nn.length; u++){
		var g = u == 0 ? 1 : u;
		if(nn.substr(u,1) == 0){
			onu += n0 + "";
		}else if(nn.substr(u,1) == 1){
			onu += n1 + "";
		}else if(nn.substr(u,1) == 2){
			onu += n2 + "";
		}else if(nn.substr(u,1) == 3){
			onu += n3 + "";
		}else if(nn.substr(u,1) == 4){
			onu += n4 + "";
		}else if(nn.substr(u,1) == 5){
			onu += n5 + "";
		}else if(nn.substr(u,1) == 6){
			onu += n6 + "";
		}else if(nn.substr(u,1) == 7){
			onu += n7 + "";
		}else if(nn.substr(u,1) == 8){
			onu += n8 + "";
		}else if(nn.substr(u,1) == 9){
			onu += n9 + "";
		}
	}
	return onu;
}


function start() {
	var onlinenumber = cm.getTotalOnline() + 2;

	var info = [
		
		["                " +  online + "", " " + getNumber((parseInt(cm.getTotalOnline()*1.7) ).toString()) + " " , "" + online2 + "\r\n"],
		//["               " + icon3 + " " + point + "", " " + getNumber(cm.getPlayer().getPoints().toString()) + " " , "" + point2 + "\r\n"],

	];

	var msg = "\r\n";
    msg += "              " + serverImage + "\r\n\r\n";
	
	for (var i = 0; i < info.length; i++) {
		msg += info[i][0];
		msg += FormatString(" ", 4, info[i][1].toString());
		//msg += info[i][1];
		msg += info[i][2];
	}
	msg += "\r\n";

	var x = 0;
	for (var i = 0; i < menuList.length; i++) {
		
		msg += "#L" + i + "#" + menuList[i][0] + "#l" + ((i+1)%3 == 0 ? "\r\n" : "");

	}
	
	msg += "\r\n ";
    
	cm.sendSimple(cm.getPlayer().getLevel() >= 8 ? msg : "#d#r8等以上才能使用拍賣#k");
	
}

function action(mode, type, selection) {
	var onlinenumber = cm.getTotalOnline() + 2;
	if (select === -1) {
		select = selection;
	}
	
	if (select == -1){
		cm.dispose();
		//cm.openNpc(9010000,"聚合功能");		
    }else if (select < menuList.length && select >= 0) {
		if (!isNaN(menuList[select][1])) {
			cm.dispose();
			cm.openNpc(menuList[select][1]);
			return;
		} else {
			cm.dispose();
			cm.openNpc(9010000, menuList[select][1]);
			return;
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
		mapid / 10 === 13003000) {
		cm.sendOk("你不能在這裡使用這個功能.");
	} else {
		if (script == null) {
			cm.openNpc(npcid);
		} else {
			cm.openNpc(npcid, script);
		}
	}
}




function FormatString(fill, length, content) {
	var str = content;
	var time = length - content.length;
	while (time > 0) {
		str += fill;
		time--;
	}
	return str;
}

function getOnlineTime() {
	var sec = cm.getPlayer().getOnlineSeconds();
	//return + parseInt(sec / 60 / 60) + "#d時#k#r" + parseInt(sec / 60 % 60) + "#k#d分#k#r" + parseInt(sec % 60 % 60 ) + "#k#d秒#k";
	return parseInt(sec / 60) ;
}
