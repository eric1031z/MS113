load('nashorn:mozilla_compat.js');
importPackage(java.lang);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.tools);



var status ;

var status = -1;
var state = new Array();
var Random = new Array(14);
var possbile = [-5,-4,-3,-2,-1,0,1,2,3,4,5];
var ret = new Array(14);

function start(){
	action(1,0,0);
}


function action(mode,type,selection){
	if(mode==1){
		status ++ ;
	}else {
		status --;
	}
	
	if(mode == 0){
		cm.dispose();

	}

    var eq = cm.getInventory(1).getItem(1);
	
	
	if(status == 0){
		if(eq == null){
			cm.sendOk("#e您的裝備欄第一格並無道具");
			cm.dispose();
		}
		
		if(eq.getUpgradeSlots() <= 0){
			cm.sendOk("#e沒卷軸數了");
			cm.dispose();
		}
		
		var msg = "";
		msg += "#e請把要衝捲的裝備放置裝備欄第一格 :\r\n";
		msg += "#d欲衝捲裝備 : #b#z" + eq.getItemId() + "##k\r\n";
		cm.sendYesNo(msg);
	}else if(status == 1){
		var msg = "#e此裝備目前的素質狀態為 : #k\r\n\r\n";
		getStatus(eq);
	    for(var i = 0; i < state.length; i++){
			if(state[i].val > 0){
			    msg += "#d" + state[i].name + "#k - #b" + state[i].val + "#k\r\n";
			}
		}
		cm.sendYesNo(msg);
	}else if(status == 2){
		fillRandom(Random);
		initRet(ret);
		var msg = "#e結果如下 :\r\n";
		for(var i = 0; i < state.length - 2; i++){
			var val = state[i].val - Random[i];
			if(state[i].val > 0){
				msg += "#d" + state[i].name + "#k - #b" + (val < 0 ? 0 :  val) + "#k\r\n";
				ret[i] = val;
			}
		}
	    
		msg += "\r\n\r\n";
		msg += "#L0# #r再來一次幹\r\n";
		msg += "#L1# #r我好喜翻喔\r\n";
		cm.sendSimple(msg);
	}else if(status == 3){
		var s = selection;
		if(s == 0){
			cm.sendOk("#e可悲啦衰狗");
			status = 1;
		}else{
			makeNewEq(eq);
			cm.sendOk("#e好ㄌ好ㄌ");
			status = -1;
		}
	}
}

function getStatus(eq){
	state = new Array();
	var str = {name : "力量", val : eq.getStr()};
	var dex = {name : "敏捷", val : eq.getDex()};
	var Int = {name : "智力", val : eq.getInt()};
	var luk = {name : "幸運", val : eq.getLuk()};
	var hp = {name : "血量", val : eq.getHp()};
	var mp = {name : "魔力", val : eq.getMp()};
	var watk = {name : "物攻", val : eq.getWatk()};
	var matk = {name : "魔攻", val : eq.getMatk()};
	var wdef = {name : "物防", val : eq.getWdef()};
	var mdef = {name : "魔防", val : eq.getMdef()};
	var acc = {name : "命中", val : eq.getAcc()};
	var avoid = {name : "迴避", val : eq.getAvoid()};
	var speed = {name : "速度", val : eq.getSpeed()};
	var jump = {name : "跳躍", val : eq.getJump()};
	var upgrade = {name : "卷數", val : eq.getUpgradeSlots()};
	var level = {name : "強化", val : eq.getLevel()};
	state.push(str);
	state.push(dex);
	state.push(Int);
	state.push(luk);
	state.push(hp);
	state.push(mp);
	state.push(watk);
	state.push(matk);
	state.push(wdef);
	state.push(mdef);
	state.push(acc);
	state.push(avoid);
	state.push(speed);
	state.push(jump);
	state.push(upgrade);
	state.push(level);
}

function fillRandom(arr){
	for(var i = 0; i < arr.length; i++){
		arr[i] = possbile[Math.floor(Math.random()*possbile.length)];
	}
}

function initRet(arr){
	for(var i = 0; i < arr.length; i++){
		arr[i] = -1;
	}
}

function makeNewEq(eq){
	if(ret[0] > -1) eq.setStr(ret[0]);
	if(ret[1] > -1) eq.setDex(ret[1]);
	if(ret[2] > -1) eq.setInt(ret[2]);
	if(ret[3] > -1) eq.setLuk(ret[3]);
	if(ret[4] > -1) eq.setHp(ret[4]);
	if(ret[5] > -1) eq.setMp(ret[5]);
	if(ret[6] > -1) eq.setWatk(ret[6]);
	if(ret[7] > -1) eq.setMatk(ret[7]);
	if(ret[8] > -1) eq.setWdef(ret[8]);
	if(ret[9] > -1) eq.setMdef(ret[9]);
	if(ret[10] > -1) eq.setAcc(ret[10]);
	if(ret[11] > -1) eq.setAvoid(ret[11]);
	if(ret[12] > -1) eq.setSpeed(ret[12]);
	if(ret[13] > -1) eq.setJump(ret[13]);
	eq.setUpgradeSlots(state[14].val-1);
	eq.setLevel(state[15].val+1);
	cm.getPlayer().forceReAddItem_NoUpdate(eq, MapleInventoryType.EQUIP);
	cm.getPlayer().getClient().sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.ADD, eq)));
}







		