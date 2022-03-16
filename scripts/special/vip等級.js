var status;

function start(){
	status=-1;
	action(1,0,0);
}

function action (mode,type,selection){
	if(mode==1){
		status++;
	}else 
		status--;
	if(mode==0){
		cm.dispose();
	}
	
	if(status==0){
		if(cm.getPlayer().getGMLevel()>=100){
		cm.sendGetText("你想要修改誰的vip等級?");
		}
		else if(cm.getPlayer().getGMLevel()<100){
		cm.sendOk("您的權限無法使用此功能");
		cm.dispose();
		}
	}else if(status==1){
		this. name=cm.getText();
		cm.sendGetText("目前"+name+"的vip等級為"+cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(name).getVip()+"\r\n您想要修改"+name+"玩家的vip等級成多少?");
	}else if(status==2){
		this. vip=cm.getText();
		cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(name).setVip(vip);
		cm.sendOk("已經為您調整");
		cm.dispose();
	}
}
