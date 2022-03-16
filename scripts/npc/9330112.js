load('nashorn:mozilla_compat.js');
importPackage(Packages.server);



var icon1 = "#fEffect/CharacterEff/1112904/0/0#";
var icon2 = "#fEffect/CharacterEff/1042107/0/0#";
var icon3 = "#fEffect/CharacterEff/1042107/0/0#";
var ticket = 5220040; //轉蛋消耗物品
var typeg = 2; //第幾號轉蛋機





var status = -1;

function start() {
    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }
	
	if(mode == 0){
		cm.dispose();
	}
    if (status == -1) {
        cm.dispose();
    } else if (status == 0) {
		
        if (cm.haveItem(ticket)) {
			cm.sendSimple(icon1 + "" + icon1 + "#d【潮流轉蛋機】#k" + icon1 + "" + icon1 + "\r\n#b#L0#" + icon2 + "我要轉蛋\r\n#L1#" + icon2 + "查看轉蛋機內容物\r\n"  + (cm.getPlayer().isGM() ? ("\r\n#L2#" + icon2 + "更新轉蛋機") : ""));
        } else {
            cm.sendSimple(icon1 + "" + icon1 + "#d【潮流轉蛋機】#k" + icon1 + "" + icon1 + "\r\n#b#L1#" + icon2 + "查看轉蛋機內容物\r\n"  + (cm.getPlayer().isGM() ? ("\r\n#L2#" + icon2 + "更新轉蛋機") : ""));
        }
    }else if (status == 1) {
	  this. se = selection;
	  if(se == 0){
        var result = cm.gachapon(typeg);
        if (result !== -1) {
            cm.gainItem(ticket, -1);
            cm.sendOk("您已獲得 #i" + result +"#  #b#t" + result + "##k.");
			//status = -1;
        } else {
            cm.sendOk("檢查一下背包是否已滿,或者所有設置的獎品已經發放完畢。");
			cm.dispose();
        }
        //cm.dispose();
	  }else if(se == 1){
		  cm.sendOk(cm.getGachaView(typeg));
		  status = -1;
	  }else if(se == 2){
		  cm.sendSimple(cm.editGacha(typeg));
	  }
    }else if(status == 2){
		this. sel1 = selection;
		if(se == 2){
		if(sel1!=30678){
			cm.sendGetNumber("修改物品 :#i" + sel1 + "##t" + sel1 + "# 您要設置其機率為多少%?",0,0,100);
		}else {
			cm.sendGetNumber("請輸入您要新增的物品代碼 :", 1, 1, 9999999);
		}
		}
	}else if(status == 3){
		this. so = selection;
		if(se == 2){
		if(sel1!=30678){
		    this. sel2 = selection;
			if(sel2 == 0){
				cm.zerodelete(sel1,typeg);
				cm.sendOk("已為您刪除物品#i" + sel1 + "##t" + sel1 + "#");
				cm.reloadgacha();
			    cm.dispose();
			}else{
			    cm.newGachapon(sel1,sel2*1000,typeg);
			    cm.sendOk("已為您將物品#i" + sel1 + "##t" + sel1 +"#更新機率至 :#r" + sel2 + "%#k");
			    cm.reloadgacha();
				status = -1
		    }
		}else{
			this. sel3 = selection;
			if(!MapleItemInformationProvider.getInstance().itemExists(sel3)){
				cm.sendOk("您輸入的物品代碼#r" + sel3 + "#k 物品不存在");
				cm.dispose();
			}else{
			    cm.sendGetNumber("您想要將物品#i" + sel3 + "##t" + sel3 +"#的機率設置為幾% ?",0,0,100);
			}
		}
		}
	}else if(status == 4){
		if(se == 2){
		if(sel1 == 30678){
			this. sel4 = selection;
			cm.sendGetNumber("您想要讓物品#i" + sel3 + "##t" + sel3 +"#上廣嗎?(輸入1代表上廣,0代表不要)",0,0,1);
		}
		}
	}else if(status == 5){
		if(se == 2){
		if(sel1 == 30678){
		   this. sel5 = selection;
		   cm.gachaponadditem(sel3,sel4*1000,sel5,typeg);
		   cm.sendOk("已為您新增完畢");
		   cm.reloadgacha();
		   status = -1;
		}
		}
	}
}