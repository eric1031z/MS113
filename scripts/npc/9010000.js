load('nashorn:mozilla_compat.js');
importPackage(Packages.util);
importPackage(Packages.client);
importPackage(Packages.server.life);
importPackage(Packages.server);

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
        if (cm.getMap().getAllMonstersThreadsafe().size() <= 0) {
            cm.sendOk("此地圖找不到怪物。");
            cm.dispose();
            return;
        }
        var selStr = "請選擇怪物：\r\n\r\n#b";
        var iz = cm.getMap().getAllUniqueMonsters().iterator();
        while (iz.hasNext()) {
            var zz = iz.next();
            selStr += "#L" + zz + "##o" + zz + "##l\r\n";
        }
        cm.sendSimple(selStr);
    } else if (status == 1) {
		this. sel = selection;
        cm.sendNext(cm.checkDrop(sel));
        //cm.dispose();
    } else if (status == 2) {
		this. sel2 = selection;
		if(sel2 != 30678){
			cm.sendGetNumber("您選擇的物品是 :#i" + sel2 + "##b#t" + sel2 + "##k,請問要設定其機率為幾%",0,0,100);
		}else{
			cm.sendGetNumber("您要新增甚麼物品至#o" + sel + "#呢?",1,1,9999999);
		}
	}else if(status == 3){
		if(sel2 != 30678){
			this. sel3 = selection;
			if(sel3 == 0){
				cm.zeroDrop(sel2,sel);
				cm.sendOk("已為您清除此掉落物");
				MapleMonsterInformationProvider.getInstance().clearDrops();
				cm.dispose();
			}else{
			    cm.sendGetNumber("您選擇的物品是 :#i" + sel2 + "##b#t" + sel2 + "##k,請問有前置任務需解嗎?\r\n#d(若有輸入任務代碼,無輸入0)#k",0,0,10000000);
			}
		}else {
			this. sel4 = selection;
			if(!MapleItemInformationProvider.getInstance().itemExists(sel4)){
				cm.sendOk("您輸入的物品 :#r"+ sel4 +"#k 不存在");
				cm.dispose();
			}else{
			    cm.sendGetNumber("您想要將物品#i" + sel4 + "##t" + sel4 +"#的機率設置為幾% ?",0,0,100);
			}
		}
	}else if(status == 4){
		if(sel2 != 30678){
			this. sel5 = selection;
			cm.newDropData(sel2,sel3*10000,sel,sel5);
			cm.sendOk("已為您更改物品#i" + sel2 + "##b#t" + sel2 + "##k 之機率至: #r" + sel3 + "%#k 前置任務ID為: #d" + (sel5 == 0 ? "無" : sel5) + "#k");
			MapleMonsterInformationProvider.getInstance().clearDrops();
			cm.dispose();
		}else{
			this. sel6 = selection;
			cm.sendGetNumber("您想要將物品#i" + sel4 + "##t" + sel4 +"#的最小掉落量設置為 ?",1,1,1000);
		}
	}else if(status == 5){
		if(sel2 == 30678){
			this. sel7 = selection;
			cm.sendGetNumber("您想要將物品#i" + sel4 + "##t" + sel4 +"#的最大掉落量設置為 ?",1,1,1000);
		}
	}else if(status == 6){
		if(sel2 == 30678){
			this. sel8 = selection;
			cm.sendGetNumber("您欲新增的物品是 :#i" + sel4 + "##b#t" + sel4 + "##k,請問有前置任務需解嗎?\r\n#d(若有輸入任務代碼,無輸入0)#k",0,0,10000000);
		}
	}else if(status == 7){
		this. sel9 = selection;
		cm.dropnadditem(sel,sel4,sel7,sel8,sel9,sel6*10000);
		cm.sendOk("已為您新增物品#i" + sel4 + "##b#t" + sel4 + "##k /機率: #r" + sel6 + "%#k 前置任務ID為: #d" + (sel9 == 0 ? "無" : sel9) + "#k 最小掉落量: #b" + sel7 + "#k個 最大掉落量: #b"+ sel8 + "#k個");
		MapleMonsterInformationProvider.getInstance().clearDrops();
		cm.dispose();
	}
}