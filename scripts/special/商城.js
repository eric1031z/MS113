load('nashorn:mozilla_compat.js');
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.sever.filter);


var icon1 = "#fUI/UIWindow/Quest/icon1#";//黃驚嘆
var icon5 = "#fUI/UIWindow/Quest/icon1#";
var icon2 = "#fUI/UIWindow/Quest/icon1#"; //灰驚嘆
var icon3 = "#fUI/UIWindow/Quest/icon1#";
var icon4 = "#fUI/UIWindow/Quest/icon1#";
var cake = "#fUI/UIWindow/Quest/icon1#";


function start(){
	status = -1
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
		//cm.openNpc(9010000,"功能專區");
	}
	
	if(status == 0){
		var msg = "";
		msg += "                    " + icon5 + " #d物品過濾#k\r\n\r\n";
		msg += "   " + cake + "" + cake + "" + cake + "" + cake + "" + cake + "" + cake + "" + cake + "" + cake + "" + cake + "" + cake + "" + cake + "" + cake + "" + cake + "" + cake+ "" + cake + "" + cake + "" + cake + "" + cake +  "  \r\n";
		msg += "#b#L9999#" + icon3 + " #e新增物品過濾道具#n\r\n";
		cm.sendSimple(msg + "" + MapleFilter.getAllFilter(cm.getPlayer().getId()));	
	}else if(status == 1){
		this. s = selection;
		if(s == 9999){
			cm.sendGetText("#d請輸入想新增道具名稱:#k");
		}else{
			var msg = "";
			msg += icon3 +" 選擇道具為 : #d#t" + s + "##k\r\n";
            msg += icon3 +" #b當前已過濾總數為 :#k#r" + MapleFilter.getHasFilteredCount(cm.getPlayer().getId(),s) + "#k #b個#k\r\n";
            msg += "#L1##d從過濾表移除此物品#k\r\n";
            cm.sendSimple(msg);			
		}
	}else if(status == 2){
		this. s2;
		if(s == 9999){
			s2 = cm.getText();
		}else{
			s2 = selection;
		}
		
		if(s == 9999){
			cm.sendOk(cm.searchData(1, s2));
		}else{
			MapleFilter.deleteFilterItem(cm.getPlayer().getId(),s);
			cm.sendOk("#d已為您從過濾表移除道具#k #r#t" + s + "##k");
			status = -1;
		}
	}else if(status == 3){
		this. sel = selection;
		if(s == 9999){
			if (!cm.foundData(1, s2)) {
			    cm.dispose();
			    //return;
		    }else{
				if(sel != -1){
				    MapleFilter.setFilterItem(cm.getPlayer().getId(),sel);
				    cm.sendOk("#d已為您將 :#k #r#t" + sel + "##k #d加入過濾名單#k\r\n");
				    status = -1;
				}else{
					cm.sendOk("輸入有誤");
					status = -1;
				}
			}
		}
	}
		
}
			