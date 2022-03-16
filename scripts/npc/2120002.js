load('nashorn:mozilla_compat.js');

importPackage(Packages.util);
importPackage(Packages.client.inventory);
importPackage(Packages.server);


var status;
var selected;
var slot=Array();
var h=-1;
var h1=-1;
var pay=-1;


function start(){
	status=-1;
	action(1, 0, 0);
}

function action (mode, type, selection){
	
    var c=cm.getPlayer();
	var gash=c.getCSPoints(1);
	var MP=c.getCSPoints(2);
	
	
	if(mode==1){
		status++;
	}else
		status--;
	
	if(status==0){
		var avail = "";
	    for (var i = -1; i > -199; i--) {
		if (cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(i) != null && !cm.isCash(cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(i).getItemId())) {
		    avail += "#L" + Math.abs(i) + "##t" + cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(i).getItemId() + "##l\r\n";
		}
		slot.push(i);
	    }
		
	
	    cm.sendSimple("你想要附魔哪一件裝備呢?\r\n#e#rP.S.#k#n附魔完請#e#b換頻#k#n或#e#b重登#k#n才會看到效果\r\n#d" + avail+"#k");
	}else if(status==1){
		selected=selection-1;
		cm.sendOk("你確定要附魔#r#t"+cm.getInventory(-1).getItem(slot[selected]).getItemId()+"##k嗎?\r\n#L0##b確定#k\r\n#L1##b不要#k");
	}else if(status==2){
		h=selection;
		if(h==1){
			cm.sendOk("想變得更加強大再來找我吧!");
			cm.dispose();
		}else
		cm.sendSimple("你想要進行第幾階段附魔?\r\n#L0##d第一階段(需要1000GASH或1500楓葉點數) #k#l\r\n\r\n    #e#r70%成功率全能力+1#k#n\r\n#L1##d第二階段(需要1500GASH或2000楓葉點數) #k#l\r\n\r\n    #e#r60%成功率全能力+2#k#n\r\n#L2##d第三階段(需要2000GASH或2500楓葉點數) #k#l\r\n\r\n    #e#r50%成功率全能力+3#k#n");
	}else if(status==3){
		h1=selection;
		cm.sendSimple("你想要怎麼支付呢?\r\n#L0##rGASH#k\r\n#L1##r楓葉點數#k");	
	}else if(status==4){
		var Success = Math.floor(Math.random()*1000+1); 
		var Success2 = Math.floor(Math.random()*1000+1); 
		var Success3 = Math.floor(Math.random()*1000+1); 
		
		pay=selection;
if(h==0&&h1==0&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&Success<=700&&gash>=1000){
	var b= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getStr();
	var b1= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getDex();
	var b2= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getInt();
	var b3= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getLuk();
	var k=b+1;
	var k1=b1+1;
	var k2=b2+1;
	var k3=b3+1;
	
	cm.changeStat(slot[selected],0,k);
	cm.changeStat(slot[selected],1,k1);
	cm.changeStat(slot[selected],2,k2);
	cm.changeStat(slot[selected],3,k3);
	
	cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).setOwner("★");
	cm.getPlayer().modifyCSPoints(1,-1000,true);
	cm.sendOk("完成第一階附魔!");
	cm.dispose();
}

    if(h==0&&h1==0&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&Success<=700&&MP>=1500){
	var b= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getStr();
	var b1= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getDex();
	var b2= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getInt();
	var b3= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getLuk();
	var k=b+1;
	var k1=b1+1;
	var k2=b2+1;
	var k3=b3+1;
	
	cm.changeStat(slot[selected],0,k);
	cm.changeStat(slot[selected],1,k1);
	cm.changeStat(slot[selected],2,k2);
	cm.changeStat(slot[selected],3,k3);
	cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).setOwner("★");
	cm.getPlayer().modifyCSPoints(2,-1500,true);
	cm.sendOk("完成第一階附魔!");
	cm.dispose();
}

if(h==0&&h1==0&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&gash<1000){
	cm.sendOk("您的GASH不夠喔!");
	cm.dispose();
}

if(h==0&&h1==0&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&gash<1000){
	cm.sendOk("您的GASH不夠喔!");
	cm.dispose();
}

if(h==0&&h1==0&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&gash<1000){
	cm.sendOk("您的GASH不夠喔!");
	cm.dispose();
}

if(h==0&&h1==0&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&MP<1500){
	cm.sendOk("您的楓點不夠喔!");
	cm.dispose();
}

if(h==0&&h1==0&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&MP<1500){
	cm.sendOk("您的楓點不夠喔!");
	cm.dispose();
}

if(h==0&&h1==0&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&MP<1500){
	cm.sendOk("您的楓點不夠喔!");
	cm.dispose();
}

if(h==0&&h1==0&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"){
cm.sendOk("此裝備已經附過第一階段魔");
cm.dispose();
}

if(h==0&&h1==0&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"){
cm.sendOk("此裝備已經附過第二階段魔");
cm.dispose();
}

if(h==0&&h1==0&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★★"){
cm.sendOk("此裝備已經附過第三階段魔");
cm.dispose();
}

if(h==0&&h1==0&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"){
cm.sendOk("此裝備已經附過第一階段魔");
cm.dispose();
}

if(h==0&&h1==0&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"){
cm.sendOk("此裝備已經附過第二階段魔");
cm.dispose();
}

if(h==0&&h1==0&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★★"){
cm.sendOk("此裝備已經附過第三階段魔");
cm.dispose();
}

if(h==0&&h1==0&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&Success>700&&gash>=1000){
	cm.getPlayer().modifyCSPoints(1,-1000,true);
	cm.sendOk("可惜第一階附魔失敗了");
	cm.dispose();
}
if(h==0&&h1==0&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&Success>700&&MP>=1500){
	cm.getPlayer().modifyCSPoints(2,-1500,true);
	cm.sendOk("可惜第一階附魔失敗了");
	cm.dispose();
}
// 以上是第一階段

if(h==0&&h1==1&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"&&Success2<=600&&gash>=1500){
	var b= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getStr();
	var b1= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getDex();
	var b2= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getInt();
	var b3= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getLuk();
	var k=b+2;
	var k1=b1+2;
	var k2=b2+2;
	var k3=b3+2;
	
	cm.changeStat(slot[selected],0,k);
	cm.changeStat(slot[selected],1,k1);
	cm.changeStat(slot[selected],2,k2);
	cm.changeStat(slot[selected],3,k3);
	cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).setOwner("★★");
	cm.getPlayer().modifyCSPoints(1,-1500,true);
	cm.sendOk("完成第二階附魔!");
	cm.dispose();
}


if(h==0&&h1==1&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"&&Success2<=600&&MP>=2000){
	var b= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getStr();
	var b1= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getDex();
	var b2= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getInt();
	var b3= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getLuk();
	var k=b+2;
	var k1=b1+2;
	var k2=b2+2;
	var k3=b3+2;
	
	cm.changeStat(slot[selected],0,k);
	cm.changeStat(slot[selected],1,k1);
	cm.changeStat(slot[selected],2,k2);
	cm.changeStat(slot[selected],3,k3);
	cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).setOwner("★★");
	cm.getPlayer().modifyCSPoints(2,-2000,true);
	cm.sendOk("完成第二階附魔!");
	cm.dispose();
}

if(h==0&&h1==1&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"&&gash<1500){
	cm.sendOk("您的gash不夠喔!");
	cm.dispose();
}


if(h==0&&h1==1&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"&&MP<2000){
	cm.sendOk("您的楓點不夠喔!");
	cm.dispose();
}


 if (h==0&&h1==1&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"){
	cm.sendOk("此裝備已附魔過第二階段魔");
    cm.dispose();
 }

 
 if (h==0&&h1==1&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★★"){
	cm.sendOk("此裝備已附魔過第三階段魔");
    cm.dispose();
 }
 
if (h==0&&h1==1&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"){
	cm.sendOk("此裝備已附魔過第二階段魔");
    cm.dispose();
 }
 
 
 if(h==0&&h1==1&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★★"){
	cm.sendOk("此裝備已附過第三階段魔");
	cm.dispose();
}
 
	
if (h==0&&h1==1&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"){
	cm.sendOk("此裝備尚未附第一階段魔");
    cm.dispose();		
}

if (h==0&&h1==1&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"){
	cm.sendOk("此裝備尚未附第一階段魔");
    cm.dispose();		
}

if (h==0&&h1==2&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&gash<1500){
	cm.sendOk("您的GASH不夠喔");
    cm.dispose();		
}

if (h==0&&h1==2&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&MP<2000){
	cm.sendOk("您的楓點不夠喔");
    cm.dispose();		
}


if(h==0&&h1==1&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"&&Success2>600&&gash>=1500){
	cm.getPlayer().modifyCSPoints(1,-1500,true);
	cm.sendOk("可惜第二階附魔失敗了!");
	cm.dispose();
}

if(h==0&&h1==1&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"&&Success2>600&&MP>=2000){
	cm.getPlayer().modifyCSPoints(2,-2000,true);
	cm.sendOk("可惜第二階附魔失敗了!");
	cm.dispose();
}

//以上是第二階段

if(h==0&&h1==2&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"&&Success3<=500&&gash>=2000){
	var b= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getStr();
	var b1= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getDex();
	var b2= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getInt();
	var b3= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getLuk();
	var k=b+3;
	var k1=b1+3;
	var k2=b2+3;
	var k3=b3+3;
	
	cm.changeStat(slot[selected],0,k);
	cm.changeStat(slot[selected],1,k1);
	cm.changeStat(slot[selected],2,k2);
	cm.changeStat(slot[selected],3,k3);
	cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).setOwner("★★★");
	cm.getPlayer().modifyCSPoints(1,-2000,true);
	cm.sendOk("完成第三階段附魔!");
	cm.dispose();
}

if(h==0&&h1==2&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"&&Success3<=500&&MP>=2500){
	var b= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getStr();
	var b1= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getDex();
	var b2= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getInt();
	var b3= cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getLuk();
	var k=b+3;
	var k1=b1+3;
	var k2=b2+3;
	var k3=b3+3;
	
	cm.changeStat(slot[selected],0,k);
	cm.changeStat(slot[selected],1,k1);
	cm.changeStat(slot[selected],2,k2);
	cm.changeStat(slot[selected],3,k3);
	cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).setOwner("★★★");
	cm.getPlayer().modifyCSPoints(2,-2500,true);
	cm.sendOk("完成第三階段附魔!");
	cm.dispose();
}

if(h==0&&h1==2&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"&&Success3>500&&gash>=2000){
	cm.getPlayer().modifyCSPoints(1,-2000,true);
	cm.sendOk("可惜第三階附魔失敗了!");
	cm.dispose();
}

if(h==0&&h1==2&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"&&Success3>500&&MP>=2500){
	cm.getPlayer().modifyCSPoints(2,-2500,true);
	cm.sendOk("可惜第三階附魔失敗了!");
	cm.dispose();
}



if(h==0&&h1==2&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★★"){
	cm.sendOk("此裝備已附過第三階段魔");
	cm.dispose();
}

if(h==0&&h1==2&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★★"){
	cm.sendOk("此裝備已附過第三階段魔");
	cm.dispose();
}

if (h==0&&h1==2&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"){
	cm.sendOk("此裝備尚未附第一階段魔");
    cm.dispose();		
}

if (h==0&&h1==2&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"){
	cm.sendOk("此裝備尚未附第一階段魔");
    cm.dispose();		
}

if (h==0&&h1==2&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"){
	cm.sendOk("此裝備尚未附魔第二階段魔");
    cm.dispose();		
}


if (h==0&&h1==2&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★"){
	cm.sendOk("此裝備尚未附魔第二階段魔");
    cm.dispose();		
}


if (h==0&&h1==2&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"&&gash<2000){
	cm.sendOk("您的GASH不夠喔");
    cm.dispose();		
}

if (h==0&&h1==2&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()=="★★"&&MP<2500){
	cm.sendOk("您的楓點不夠喔");
    cm.dispose();		
}

if (h==0&&h1==2&&pay==0&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&gash<2000){
	cm.sendOk("您的GASH不夠喔");
    cm.dispose();		
}


if (h==0&&h1==2&&pay==1&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★"&&cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[selected]).getOwner()!="★★★"&&MP<2500){
	cm.sendOk("您的楓點不夠喔");
    cm.dispose();		
}




}
}		
