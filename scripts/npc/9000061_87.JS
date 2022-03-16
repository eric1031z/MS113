var status;
var h=-1;

function start(){
	status=-1;
	action(1,0,0);
}

function action(mode,type,selection){
	if(mode==1){
		status++;
	}else
		status--;
	
	if(status==0){
		cm.sendSimple("咕咕雞的各位大家好,我是#r垃圾回收員#k,這次收取的物品如下\r\n#L0##i4000210##t4000210#\r\n#L1##i4000212##t4000212#\r\n#L2##i4000213##t4000213#\r\n#L3##i4000216##t4000216#\r\n#L4##i4000209##t4000209#");
	}
	else if(status==1){
		h=selection;
		if(h==0){
			cm.sendGetNumber("請問您要出售多少#i4000210##t4000210#呢?",1,1,9999);
		}
		if(h==1){
			cm.sendGetNumber("請問您要出售多少#i4000212##t4000212#呢?",1,1,9999);
		}
		if(h==2){
			cm.sendGetNumber("請問您要出售多少#i4000213##t4000213#呢?",1,1,9999);
		}
		if(h==3){
			cm.sendGetNumber("請問您要出售多少#i4000216##t4000216#呢?",1,1,9999);
		}
		if(h==4){
			cm.sendGetNumber("請問您要出售多少#i4000209##t4000209#呢?",1,1,9999);
		}
	}else if(status==2){
		var trash = selection;
	
		if(h==0&&cm.haveItem(4000210,trash)){
			cm.gainItem(4000210,-trash);
			cm.gainMeso(5000000*trash);
			cm.sendOk("我們已經幫你回收了喔!");
			cm.dispose();
		}
		if(h==0&&!cm.haveItem(4000210,trash)){
			cm.sendOk("哈,我看你是欠回收吧");
			cm.dispose();
		}
		if(h==1&&cm.haveItem(4000212,trash)){
			cm.gainItem(4000212,-trash);
			cm.gainMeso(5000000*trash);
			cm.sendOk("我們已經幫你回收了喔!");
			cm.dispose();
		}
		if(h==1&&!cm.haveItem(4000212,trash)){
			cm.sendOk("哈,我看你是欠回收吧");
			cm.dispose();
		}
		if(h==2&&cm.haveItem(4000213,trash)){
			cm.gainItem(4000213,-trash);
			cm.gainMeso(5000000*trash);
			cm.sendOk("我們已經幫你回收了喔!");
			cm.dispose();
		}
		if(h==2&&!cm.haveItem(4000213,trash)){
			cm.sendOk("哈,我看你是欠回收吧");
			cm.dispose();
		}
		if(h==3&&cm.haveItem(4000216,trash)){
			cm.gainItem(4000216,-trash);
			cm.gainMeso(5000000*trash);
			cm.sendOk("我們已經幫你回收了喔!");
			cm.dispose();
		}
		if(h==3&&!cm.haveItem(4000216,trash)){
			cm.sendOk("哈,我看你是欠回收吧");
			cm.dispose();
		}
		if(h==4){
			cm.sendOk("哈哈我要跟小龍女講");
			cm.dispose();
		}
		if(cm.getPlayer().getMeso()+5000000*trash>2147483647){
			cm.sendOk("你這樣太有錢了喔,可以分我一點ㄇ?");
			cm.dispose();
		}
	}
	}
		