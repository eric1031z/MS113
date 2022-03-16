load('nashorn:mozilla_compat.js');
importPackage(Packages.util);
importPackage(Packages.client.inventory);
importPackage(Packages.server);


var status;
var slot=Array();
var h0=-1;
var h1=-1;
var h2=-1;
var h3=-1;
var h4=-1;
var h5=-1;
var max=4;
var min=-4;
var max2=6;
var min2=-6;
var a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p;
var ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8,ch9,ch10,ch11,ch12,ch13;
var reverseitem2 = 2049600 // 回真捲60%
var reverseitem = 2049601 // 回真捲100%




function start(){
	status=-1;
	action(1, 0, 0);
}

function action (mode, type, selection){
	
    var c=cm.getPlayer();
	var gash=c.getCSPoints(1);
	var MP=c.getCSPoints(2);

	
    var Success = Math.floor(Math.random()*1000+1); 
		   
		 
	
	
	
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
		
	
	    cm.sendSimple("您好,歡迎使用#b咕咕雞谷回真系統#k\r\n請選擇您想要進行回真的裝備,將會消耗一張回真卷軸\r\n---------------------------------------------------\r\n請注意,在使用此功能途中發生斷線,或其他異常狀況,若玩家#r無提供使用回真系統相關過程影片證明#k管理團隊因為公平性將無法進行任何形式的補償,#r當按下選擇裝備鍵即代表同意此規範#k\r\n---------------------------------------------------\r\n" + avail+"#k");
	}else if(status==1){
	   h0=selection-1;
       cm.sendSimple("您確定要將#r#t"+cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getItemId()+"##k進行回真保護嗎?\r\n#e回真保護可將不滿意的結果重置到衝捲前的素質#n\r\n#L0##d要#k\r\n#L1##d不要#k");
	}else if(status==2){	
	   h1=selection;
	   if(h1==0){
a=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getStr();
b=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getDex();
c=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getInt();
d=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getLuk();
e=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getHp(); 
f=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getMp(); 
g=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getWatk(); 
h=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getMatk();
i=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getWdef();
j=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getMdef();  		
k=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getAcc(); 
l=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getAvoid();
m=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getSpeed();  		
n=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getJump(); 		  
o=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getUpgradeSlots();
p=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getLevel();
		  cm.sendSimple("請核對一下以下資訊,#r若使用之後有任何問題皆自行負責#k\r\n"
		  +"---------------------------------------------------\r\n"
		  +"#r選用裝備#k:#i"+cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getItemId()+"##t"+cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getItemId()+"#\r\n"
		  +"---------------------------------------------------\r\n"
		  +"裝備素質 (#e請確實檢查是否正確#n):\r\n"
		  +"---------------------------------------------------\r\n"
		  +"#d力量#k#e: "+a+"#n\r\n"
		  +"#d敏捷#k#e: "+b+"#n\r\n"
		  +"#d智力#k#e: "+c+"#n\r\n"
		  +"#d幸運#k#e: "+d+"#n\r\n"
		  +"#d生命#k#e: "+e+"#n\r\n"
		  +"#d魔力#k#e: "+f+"#n\r\n"
		  +"#d物攻#k#e: "+g+"#n\r\n"
		  +"#d魔攻#k#e: "+h+"#n\r\n"
		  +"#d物防#k#e: "+i+"#n\r\n"
		  +"#d魔防#k#e: "+j+"#n\r\n"
		  +"#d命中#k#e: "+k+"#n\r\n"
		  +"#d迴避#k#e: "+l+"#n\r\n"
		  +"#d速度#k#e: "+m+"#n\r\n"
		  +"#d跳躍#k#e: "+n+"#n\r\n"
		  +"#d已衝捲數#k#e: "+p+"#n\r\n"
		  +"#d剩餘衝捲數#k#e: "+o+"#n\r\n"
		  +"---------------------------------------------------\r\n"
		  +"#r確定要使用回真系統嗎?\r\n#k#L0##b要#k\r\n#L1##b不要#k"
		  )
	   }
	       if(h1==1){
			   cm.sendOk("沒關係!運氣也是一種實力!");
			   cm.dispose();
		   }
	   }else if(status==3){
		   h2=selection;
		   if(h1==0&&h2==0){
			   cm.sendSimple("您想使用何種卷軸加強#i"+cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getItemId()+"##t"+cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getItemId()+"#\r\n#L0##i2049100##t2049100#且#r使用#k#i2340000#保護\r\n#L1##i2049118##t2049118#且#r使用#k#i2340000#保護\r\n#L2##i2049100##t2049100#且#r不用#k#i2340000#保護\r\n#L3##i2049118##t2049118#且#r不用#k#i2340000#保護");
		   }
		   if(h1==0&&h2==1){
			   cm.sendOk("沒關係!運氣也是一種實力!");
			   cm.dispose();
		   }
	   }else if(status == 4){
		   h3=selection;
		   cm.sendSimple("您想要用何種回真捲進行回真?\r\n#L0##r#i2049600##t2049600#\r\n#L1##i2049601##t2049601##k");
	   }else if(status== 5){
		   h4=selection;
		   a=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getStr();
           b=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getDex();
           c=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getInt();
           d=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getLuk();
           e=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getHp(); 
           f=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getMp(); 
           g=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getWatk(); 
           h=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getMatk();
           i=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getWdef();
           j=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getMdef();  		
           k=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getAcc(); 
           l=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getAvoid();
           m=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getSpeed();  		
           n=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getJump(); 		  
           o=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getUpgradeSlots();
           p=cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot[h0]).getLevel();
	
		   var Success = Math.floor(Math.random()*1000+1); 
		   
		   ch0 = (a== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch1 = (b== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch2 = (c== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch3 = (d== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch4 = (e== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch5 = (f== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch6 = (g== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch7 = (h== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch8 = (i== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch9 = (j== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch10 = (k== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   ch11 = (l== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1))); 
		   ch12 = (m== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
	       ch13 = (n== 0 ? 0 : min +Math.floor(Math.random() * (max-min+1)));
		   
		   this.store0=a+ch0;
		   this.store1=b+ch1;
		   this.store2=c+ch2;
		   this.store3=d+ch3;
		   this.store4=e+ch4;
		   this.store5=f+ch5;
		   this.store6=g+ch6; 
		   this.store7=h+ch7;
		   this.store8=i+ch8;
		   this.store9=j+ch9;
		   this.store10=k+ch10;
		   this.store11=l+ch11;
		   this.store12=m+ch12;
		   this.store13=n+ch13; 
		   /// 混沌
		   sch0 = (a== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch1 = (b== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch2 = (c== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch3 = (d== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch4 = (e== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch5 = (f== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch6 = (g== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch7 = (h== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch8 = (i== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch9 = (j== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch10 = (k== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   sch11 = (l== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1))); 
		   sch12 = (m== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
	       sch13 = (n== 0 ? 0 : min2 +Math.floor(Math.random() * (max2-min2+1)));
		   
		   this.sstore0=a+sch0;
		   this.sstore1=b+sch1;
		   this.sstore2=c+sch2;
		   this.sstore3=d+sch3;
		   this.sstore4=e+sch4;
		   this.sstore5=f+sch5;
		   this.sstore6=g+sch6; 
		   this.sstore7=h+sch7;
		   this.sstore8=i+sch8;
		   this.sstore9=j+sch9;
		   this.sstore10=k+sch10;
		   this.sstore11=l+sch11;
		   this.sstore12=m+sch12;
		   this.sstore13=n+sch13; 
		   
		   /// 超混
		   
           if(h1==0&&h2==0&&h3==0&&h4==1&&Success<=600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem,1)&&cm.haveItem(2049100,1)){
			   
			   cm.gainItem(reverseitem,-1);
			   cm.gainItem(2340000,-1);
			   cm.gainItem(2049100,-1);
             
			   cm.sendSimple("完成了,這是您這次衝捲的結果 :\r\n"
			   +"---------------------------------------------------\r\n"
		       +"#d力量#k:#e "+a+"#n#r("+ch0+")#k = #b"+(a+ch0)+"#k\r\n"
			   +"#d敏捷#k:#e "+b+"#n#r("+ch1+")#k = #b"+(b+ch1)+"#k\r\n"
			   +"#d智力#k:#e "+c+"#n#r("+ch2+")#k = #b"+(c+ch2)+"#k\r\n"
			   +"#d幸運#k:#e "+d+"#n#r("+ch3+")#k = #b"+(d+ch3)+"#k\r\n"
			   +"#d生命#k:#e "+e+"#n#r("+ch4+")#k = #b"+(e+ch4)+"#k\r\n"
			   +"#d魔力#k:#e "+f+"#n#r("+ch5+")#k = #b"+(f+ch5)+"#k\r\n"
			   +"#d物攻#k:#e "+g+"#n#r("+ch6+")#k = #b"+(g+ch6)+"#k\r\n" 
			   +"#d魔攻#k:#e "+h+"#n#r("+ch7+")#k = #b"+(h+ch7)+"#k\r\n"
			   +"#d物防#k:#e "+i+"#n#r("+ch8+")#k = #b"+(i+ch8)+"#k\r\n"
			   +"#d魔防#k:#e "+j+"#n#r("+ch9+")#k = #b"+(j+ch9)+"#k\r\n"
			   +"#d命中#k:#e "+k+"#n#r("+ch10+")#k = #b"+(k+ch10)+"#k\r\n"
			   +"#d迴避#k:#e "+l+"#n#r("+ch11+")#k = #b"+(l+ch11)+"#k\r\n"
			   +"#d速度#k:#e "+m+"#n#r("+ch12+")#k = #b"+(m+ch12)+"#k\r\n"
			   +"#d跳躍#k:#e "+n+"#n#r("+ch13+")#k = #b"+(n+ch13)+"#k\r\n"
			   +"---------------------------------------------------\r\n"
			   +"按下確定鍵即可保存狀態,否則將會回真至未衝捲素質\r\n#L0##r我要保存此數值#k\r\n#L1##r我要回真#k"
			   );
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==1&&Success>600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem,1)&&cm.haveItem(2049100,1)){
			   cm.gainItem(2049100,-1);
			   cm.gainItem(2340000,-1);
			   cm.sendOk("您的混沌卷爆了喔\r\n但因有使用#i2340000#的保護下並沒有扣除卷軸數")
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==1&&o==0){
			   cm.sendOk("此裝備無卷軸數可使用");
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==1&&!cm.haveItem(2340000,1)){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2340000#,#i2049100#,#i"+reverseitem+"#皆具備");
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==1&&!cm.haveItem(2049100,1)){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2340000#,#i2049100#,#i"+reverseitem+"#皆具備");
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==1&&!cm.haveItem(reverseitem,1)){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2340000#,#i2049100#,#i"+reverseitem+"#皆具備");
			   cm.dispose();
		   }
		   ///選項0
		   if(h1==0&&h2==0&&h3==2&&h4==1&&Success<=600&&o!=0&&cm.haveItem(2049100,1)&&cm.haveItem(reverseitem,1)){
			   
			   cm.gainItem(reverseitem,-1);
			   cm.gainItem(2049100,-1);
             
			   cm.sendSimple("完成了,這是您這次衝捲的結果 :\r\n"
			   +"---------------------------------------------------\r\n"
		       +"#d力量#k:#e "+a+"#n#r("+ch0+")#k = #b"+(a+ch0)+"#k\r\n"
			   +"#d敏捷#k:#e "+b+"#n#r("+ch1+")#k = #b"+(b+ch1)+"#k\r\n"
			   +"#d智力#k:#e "+c+"#n#r("+ch2+")#k = #b"+(c+ch2)+"#k\r\n"
			   +"#d幸運#k:#e "+d+"#n#r("+ch3+")#k = #b"+(d+ch3)+"#k\r\n"
			   +"#d生命#k:#e "+e+"#n#r("+ch4+")#k = #b"+(e+ch4)+"#k\r\n"
			   +"#d魔力#k:#e "+f+"#n#r("+ch5+")#k = #b"+(f+ch5)+"#k\r\n"
			   +"#d物攻#k:#e "+g+"#n#r("+ch6+")#k = #b"+(g+ch6)+"#k\r\n" 
			   +"#d魔攻#k:#e "+h+"#n#r("+ch7+")#k = #b"+(h+ch7)+"#k\r\n"
			   +"#d物防#k:#e "+i+"#n#r("+ch8+")#k = #b"+(i+ch8)+"#k\r\n"
			   +"#d魔防#k:#e "+j+"#n#r("+ch9+")#k = #b"+(j+ch9)+"#k\r\n"
			   +"#d命中#k:#e "+k+"#n#r("+ch10+")#k = #b"+(k+ch10)+"#k\r\n"
			   +"#d迴避#k:#e "+l+"#n#r("+ch11+")#k = #b"+(l+ch11)+"#k\r\n"
			   +"#d速度#k:#e "+m+"#n#r("+ch12+")#k = #b"+(m+ch12)+"#k\r\n"
			   +"#d跳躍#k:#e "+n+"#n#r("+ch13+")#k = #b"+(n+ch13)+"#k\r\n"
			   +"---------------------------------------------------\r\n"
			   +"請問您要將此數值保存下來嗎?\r\n#L2##r我要保存此數值#k\r\n#L3##r我要回真#k"
			   );
		   }
		   if(h1==0&&h2==0&&h3==2&&h4==1&&Success>600&&o!=0&&cm.haveItem(2049100,1)&&cm.haveItem(reverseitem,1)){
			   cm.gainItem(2049100,-1);
			   cm.sendOk("您的混沌卷爆了喔\r\n因無使用#i2340000#的保護故扣除一卷軸數")
			   cm.changeStat(slot[h0],15,o-1);
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==2&&h4==1&&o==0){
			   cm.sendOk("此裝備無卷軸數可使用");
			   cm.dispose();
		   }
		    if(h1==0&&h2==0&&h3==2&&h4==1&&(!cm.haveItem(2049100,1)||!cm.haveItem(reverseitem,1))){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2049100#,#i"+reverseitem+"#皆具備");
			   cm.dispose();
		   }
	
		   //選項2  ------------------------------------------------ 以上混沌--------------------------
		   if(h1==0&&h2==0&&h3==1&&h4==1&&Success<=600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem,1)&&cm.haveItem(2049118,1)){
			   
			   cm.gainItem(reverseitem,-1);
			   cm.gainItem(2340000,-1);
			   cm.gainItem(2049118,-1);
             
			   cm.sendSimple("完成了,這是您這次衝捲的結果 :\r\n"
			   +"---------------------------------------------------\r\n"
		       +"#d力量#k:#e "+a+"#n#r("+sch0+")#k = #b"+(a+sch0)+"#k\r\n"
			   +"#d敏捷#k:#e "+b+"#n#r("+sch1+")#k = #b"+(b+sch1)+"#k\r\n"
			   +"#d智力#k:#e "+c+"#n#r("+sch2+")#k = #b"+(c+sch2)+"#k\r\n"
			   +"#d幸運#k:#e "+d+"#n#r("+sch3+")#k = #b"+(d+sch3)+"#k\r\n"
			   +"#d生命#k:#e "+e+"#n#r("+sch4+")#k = #b"+(e+sch4)+"#k\r\n"
			   +"#d魔力#k:#e "+f+"#n#r("+sch5+")#k = #b"+(f+sch5)+"#k\r\n"
			   +"#d物攻#k:#e "+g+"#n#r("+sch6+")#k = #b"+(g+sch6)+"#k\r\n" 
			   +"#d魔攻#k:#e "+h+"#n#r("+sch7+")#k = #b"+(h+sch7)+"#k\r\n"
			   +"#d物防#k:#e "+i+"#n#r("+sch8+")#k = #b"+(i+sch8)+"#k\r\n"
			   +"#d魔防#k:#e "+j+"#n#r("+sch9+")#k = #b"+(j+sch9)+"#k\r\n"
			   +"#d命中#k:#e "+k+"#n#r("+sch10+")#k = #b"+(k+sch10)+"#k\r\n"
			   +"#d迴避#k:#e "+l+"#n#r("+sch11+")#k = #b"+(l+sch11)+"#k\r\n"
			   +"#d速度#k:#e "+m+"#n#r("+sch12+")#k = #b"+(m+sch12)+"#k\r\n"
			   +"#d跳躍#k:#e "+n+"#n#r("+sch13+")#k = #b"+(n+sch13)+"#k\r\n"
			   +"---------------------------------------------------\r\n"
			   +"請問您要將此數值保存下來嗎?\r\n#L4##r我要保存此數值#k\r\n#L5##r我要回真#k"
			   );
		   }
		   if(h1==0&&h2==0&&h3==1&&h4==1&&Success>600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem,1)&&cm.haveItem(2049118,1)){
			   cm.gainItem(2049118,-1);
			   cm.gainItem(2340000,-1);
			   cm.sendOk("您的混沌卷爆了喔\r\n但因有使用#i2340000#的保護下並沒有扣除卷軸數")
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==1&&h4==1&&o==0){
			   cm.sendOk("此裝備無卷軸數可使用");
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==1&&h4==1&&(!cm.haveItem(2340000,1)||!cm.haveItem(2049118,1)||!cm.haveItem(reverseitem,1))){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2340000#,#i2049118#,#i"+reverseitem+"#皆具備");
			   cm.dispose();
		   }
		   ///選項1
		   if(h1==0&&h2==0&&h3==3&&h4==1&&Success<=600&&o!=0&&cm.haveItem(2049118,1)&&cm.haveItem(reverseitem,1)){
			   
			   cm.gainItem(reverseitem,-1);
			   cm.gainItem(2049118,-1);
             
			   cm.sendSimple("完成了,這是您這次衝捲的結果 :\r\n"
			   +"---------------------------------------------------\r\n"
		       +"#d力量#k:#e "+a+"#n#r("+sch0+")#k = #b"+(a+sch0)+"#k\r\n"
			   +"#d敏捷#k:#e "+b+"#n#r("+sch1+")#k = #b"+(b+sch1)+"#k\r\n"
			   +"#d智力#k:#e "+c+"#n#r("+sch2+")#k = #b"+(c+sch2)+"#k\r\n"
			   +"#d幸運#k:#e "+d+"#n#r("+sch3+")#k = #b"+(d+sch3)+"#k\r\n"
			   +"#d生命#k:#e "+e+"#n#r("+sch4+")#k = #b"+(e+sch4)+"#k\r\n"
			   +"#d魔力#k:#e "+f+"#n#r("+sch5+")#k = #b"+(f+sch5)+"#k\r\n"
			   +"#d物攻#k:#e "+g+"#n#r("+sch6+")#k = #b"+(g+sch6)+"#k\r\n" 
			   +"#d魔攻#k:#e "+h+"#n#r("+sch7+")#k = #b"+(h+sch7)+"#k\r\n"
			   +"#d物防#k:#e "+i+"#n#r("+sch8+")#k = #b"+(i+sch8)+"#k\r\n"
			   +"#d魔防#k:#e "+j+"#n#r("+sch9+")#k = #b"+(j+sch9)+"#k\r\n"
			   +"#d命中#k:#e "+k+"#n#r("+sch10+")#k = #b"+(k+sch10)+"#k\r\n"
			   +"#d迴避#k:#e "+l+"#n#r("+sch11+")#k = #b"+(l+sch11)+"#k\r\n"
			   +"#d速度#k:#e "+m+"#n#r("+sch12+")#k = #b"+(m+sch12)+"#k\r\n"
			   +"#d跳躍#k:#e "+n+"#n#r("+sch13+")#k = #b"+(n+sch13)+"#k\r\n"
			   +"---------------------------------------------------\r\n"
			   +"請問您要將此數值保存下來嗎?\r\n#L6##r我要保存此數值#k\r\n#L7##r我要回真#k"
			   );
		   }
		   if(h1==0&&h2==0&&h3==3&&h4==1&&Success>600&&o!=0&&cm.haveItem(2049118,1)&&cm.haveItem(reverseitem,1)){
			   cm.gainItem(2049118,-1);
			   cm.sendOk("您的混沌卷爆了喔\r\n因無使用#i2340000#的保護故扣除一卷軸數")
			   cm.changeStat(slot[h0],15,o-1);
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==3&&h4==1&&o==0){
			   cm.sendOk("此裝備無卷軸數可使用");
			   cm.dispose();
		   }
		    if(h1==0&&h2==0&&h3==3&&h4==1&&(!cm.haveItem(2049118,1)||!cm.haveItem(reverseitem,1))){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2049118#,#i"+reverseitem+"#皆具備");
			   cm.dispose();
		   }
		   //// 以上100%
		    var Success2 = Math.floor(Math.random()*1000+1); 
		   
		    if(h1==0&&h2==0&&h3==0&&h4==0&&Success<=600&&Success2<=600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem2,1)&&cm.haveItem(2049100,1)){
			   
			   cm.gainItem(reverseitem2,-1);
			   cm.gainItem(2340000,-1);
			   cm.gainItem(2049100,-1);
             
			   cm.sendSimple("完成了,這是您這次衝捲的結果 :\r\n"
			   +"---------------------------------------------------\r\n"
		       +"#d力量#k:#e "+a+"#n#r("+ch0+")#k = #b"+(a+ch0)+"#k\r\n"
			   +"#d敏捷#k:#e "+b+"#n#r("+ch1+")#k = #b"+(b+ch1)+"#k\r\n"
			   +"#d智力#k:#e "+c+"#n#r("+ch2+")#k = #b"+(c+ch2)+"#k\r\n"
			   +"#d幸運#k:#e "+d+"#n#r("+ch3+")#k = #b"+(d+ch3)+"#k\r\n"
			   +"#d生命#k:#e "+e+"#n#r("+ch4+")#k = #b"+(e+ch4)+"#k\r\n"
			   +"#d魔力#k:#e "+f+"#n#r("+ch5+")#k = #b"+(f+ch5)+"#k\r\n"
			   +"#d物攻#k:#e "+g+"#n#r("+ch6+")#k = #b"+(g+ch6)+"#k\r\n" 
			   +"#d魔攻#k:#e "+h+"#n#r("+ch7+")#k = #b"+(h+ch7)+"#k\r\n"
			   +"#d物防#k:#e "+i+"#n#r("+ch8+")#k = #b"+(i+ch8)+"#k\r\n"
			   +"#d魔防#k:#e "+j+"#n#r("+ch9+")#k = #b"+(j+ch9)+"#k\r\n"
			   +"#d命中#k:#e "+k+"#n#r("+ch10+")#k = #b"+(k+ch10)+"#k\r\n"
			   +"#d迴避#k:#e "+l+"#n#r("+ch11+")#k = #b"+(l+ch11)+"#k\r\n"
			   +"#d速度#k:#e "+m+"#n#r("+ch12+")#k = #b"+(m+ch12)+"#k\r\n"
			   +"#d跳躍#k:#e "+n+"#n#r("+ch13+")#k = #b"+(n+ch13)+"#k\r\n"
			   +"---------------------------------------------------\r\n"
			   +"按下確定鍵即可保存狀態,否則將會回真至未衝捲素質\r\n#L100##r我要保存此數值#k\r\n#L101##r我要回真#k"
			   );
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==0&&Success2>600&&Success<=600&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem2,1)&&cm.haveItem(2049100,1)&&o!=0){
			   cm.gainItem(reverseitem2,-1);
			   cm.sendOk("可惜您的#i2049600#爆了,並沒有起到回真作用");
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==0&&Success2>600&&Success>600&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem2,1)&&cm.haveItem(2049100,1)&&o!=0){
			   cm.gainItem(reverseitem2,-1);
			   cm.gainItem(2340000,-1);
			   cm.gainItem(2049100,-1);
			   cm.sendOk("可惜您的#i2049600#跟#i2049100#都爆了,並沒有起到回真作用");
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==0&&Success>600&&Success2<=600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem2,1)&&cm.haveItem(2049100,1)){
			   cm.gainItem(2049100,-1);
			   cm.gainItem(2340000,-1);
			   cm.sendOk("您的混沌卷爆了喔\r\n但因有使用#i2340000#的保護下並沒有扣除卷軸數")
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==0&&o==0){
			   cm.sendOk("此裝備無卷軸數可使用");
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==0&&h4==0&&(!cm.haveItem(2340000,1)||!cm.haveItem(2049100,1)||!cm.haveItem(reverseitem2,1))){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2340000#,#i2049100#,#i"+reverseitem2+"#皆具備");
			   cm.dispose();
		   }
		   ///選項0
		   if(h1==0&&h2==0&&h3==2&&h4==0&&Success<=600&&Success2<=600&&o!=0&&cm.haveItem(2049100,1)&&cm.haveItem(reverseitem2,1)){
			   
			   cm.gainItem(reverseitem2,-1);
			   cm.gainItem(2049100,-1);
             
			   cm.sendSimple("完成了,這是您這次衝捲的結果 :\r\n"
			   +"---------------------------------------------------\r\n"
		       +"#d力量#k:#e "+a+"#n#r("+ch0+")#k = #b"+(a+ch0)+"#k\r\n"
			   +"#d敏捷#k:#e "+b+"#n#r("+ch1+")#k = #b"+(b+ch1)+"#k\r\n"
			   +"#d智力#k:#e "+c+"#n#r("+ch2+")#k = #b"+(c+ch2)+"#k\r\n"
			   +"#d幸運#k:#e "+d+"#n#r("+ch3+")#k = #b"+(d+ch3)+"#k\r\n"
			   +"#d生命#k:#e "+e+"#n#r("+ch4+")#k = #b"+(e+ch4)+"#k\r\n"
			   +"#d魔力#k:#e "+f+"#n#r("+ch5+")#k = #b"+(f+ch5)+"#k\r\n"
			   +"#d物攻#k:#e "+g+"#n#r("+ch6+")#k = #b"+(g+ch6)+"#k\r\n" 
			   +"#d魔攻#k:#e "+h+"#n#r("+ch7+")#k = #b"+(h+ch7)+"#k\r\n"
			   +"#d物防#k:#e "+i+"#n#r("+ch8+")#k = #b"+(i+ch8)+"#k\r\n"
			   +"#d魔防#k:#e "+j+"#n#r("+ch9+")#k = #b"+(j+ch9)+"#k\r\n"
			   +"#d命中#k:#e "+k+"#n#r("+ch10+")#k = #b"+(k+ch10)+"#k\r\n"
			   +"#d迴避#k:#e "+l+"#n#r("+ch11+")#k = #b"+(l+ch11)+"#k\r\n"
			   +"#d速度#k:#e "+m+"#n#r("+ch12+")#k = #b"+(m+ch12)+"#k\r\n"
			   +"#d跳躍#k:#e "+n+"#n#r("+ch13+")#k = #b"+(n+ch13)+"#k\r\n"
			   +"---------------------------------------------------\r\n"
			   +"請問您要將此數值保存下來嗎?\r\n#L102##r我要保存此數值#k\r\n#L103##r我要回真#k"
			   );
		   }
		   if(h1==0&&h2==0&&h3==2&&h4==0&&Success2>600&&Success<=600&&o!=0&&cm.haveItem(2049100,1)&&cm.haveItem(reverseitem2,1)){
			   cm.gainItem(reverseitem2,-1);
			   cm.sendOk("可惜您的#i2049600#爆了,並沒有起到回真作用")
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==2&&h4==0&&Success2>600&&Success>600&&o!=0&&cm.haveItem(2049100,1)&&cm.haveItem(reverseitem2,1)){
			   cm.gainItem(reverseitem2,-1);
			   cm.gainItem(2049100,-1);
			   cm.sendOk("您的混沌卷和回真卷都爆了喔\r\n因無使用#i2340000#的保護故扣除一卷軸數")
			   cm.changeStat(slot[h0],15,o-1);
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==2&&h4==0&&Success>600&&Success2<=600&&o!=0&&cm.haveItem(2049100,1)&&cm.haveItem(reverseitem2,1)){
			   cm.gainItem(2049100,-1);
			   cm.sendOk("您的混沌卷爆了喔\r\n因無使用#i2340000#的保護故扣除一卷軸數")
			   cm.changeStat(slot[h0],15,o-1);
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==2&&h4==0&&o==0){
			   cm.sendOk("此裝備無卷軸數可使用");
			   cm.dispose();
		   }
		    if(h1==0&&h2==0&&h3==2&&h4==0&&(!cm.haveItem(2049100,1)||!cm.haveItem(reverseitem2,1))){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2049100#,#i"+reverseitem2+"#皆具備");
			   cm.dispose();
		   }
	
		   //選項2  ------------------------------------------------ 以上混沌--------------------------
		   if(h1==0&&h2==0&&h3==1&&h4==0&&Success<=600&&Success2<=600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem2,1)&&cm.haveItem(2049118,1)){
			   
			   cm.gainItem(reverseitem2,-1);
			   cm.gainItem(2340000,-1);
			   cm.gainItem(2049118,-1);
             
			   cm.sendSimple("完成了,這是您這次衝捲的結果 :\r\n"
			   +"---------------------------------------------------\r\n"
		       +"#d力量#k:#e "+a+"#n#r("+sch0+")#k = #b"+(a+sch0)+"#k\r\n"
			   +"#d敏捷#k:#e "+b+"#n#r("+sch1+")#k = #b"+(b+sch1)+"#k\r\n"
			   +"#d智力#k:#e "+c+"#n#r("+sch2+")#k = #b"+(c+sch2)+"#k\r\n"
			   +"#d幸運#k:#e "+d+"#n#r("+sch3+")#k = #b"+(d+sch3)+"#k\r\n"
			   +"#d生命#k:#e "+e+"#n#r("+sch4+")#k = #b"+(e+sch4)+"#k\r\n"
			   +"#d魔力#k:#e "+f+"#n#r("+sch5+")#k = #b"+(f+sch5)+"#k\r\n"
			   +"#d物攻#k:#e "+g+"#n#r("+sch6+")#k = #b"+(g+sch6)+"#k\r\n" 
			   +"#d魔攻#k:#e "+h+"#n#r("+sch7+")#k = #b"+(h+sch7)+"#k\r\n"
			   +"#d物防#k:#e "+i+"#n#r("+sch8+")#k = #b"+(i+sch8)+"#k\r\n"
			   +"#d魔防#k:#e "+j+"#n#r("+sch9+")#k = #b"+(j+sch9)+"#k\r\n"
			   +"#d命中#k:#e "+k+"#n#r("+sch10+")#k = #b"+(k+sch10)+"#k\r\n"
			   +"#d迴避#k:#e "+l+"#n#r("+sch11+")#k = #b"+(l+sch11)+"#k\r\n"
			   +"#d速度#k:#e "+m+"#n#r("+sch12+")#k = #b"+(m+sch12)+"#k\r\n"
			   +"#d跳躍#k:#e "+n+"#n#r("+sch13+")#k = #b"+(n+sch13)+"#k\r\n"
			   +"---------------------------------------------------\r\n"
			   +"請問您要將此數值保存下來嗎?\r\n#L104##r我要保存此數值#k\r\n#L105##r我要回真#k"
			   );
		   }
		   if(h1==0&&h2==0&&h3==1&&h4==0&&Success2>600&&Success<=600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem2,1)&&cm.haveItem(2049118,1)){
			   cm.gainItem(reverseitem2,-1);
			   cm.sendOk("可惜您的#i2049600#爆了,並沒有起到回真作用")
			   cm.dispose();
		   }
		    if(h1==0&&h2==0&&h3==1&&h4==0&&Success>600&&Success2>600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem2,1)&&cm.haveItem(2049118,1)){
			   cm.gainItem(2049118,-1);
			   cm.gainItem(2340000,-1);
			   cm.gainItem(reverseitem2,-1);
			   cm.sendOk("您的混沌卷和回真卷都爆了喔\r\n但因有使用#i2340000#的保護下並沒有扣除卷軸數")
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==1&&h4==0&&Success>600&&Success2<=600&&o!=0&&cm.haveItem(2340000,1)&&cm.haveItem(reverseitem2,1)&&cm.haveItem(2049118,1)){
			   cm.gainItem(2049118,-1);
			   cm.gainItem(2340000,-1);
			   cm.sendOk("您的混沌卷爆了喔\r\n但因有使用#i2340000#的保護下並沒有扣除卷軸數")
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==1&&h4==0&&o==0){
			   cm.sendOk("此裝備無卷軸數可使用");
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==1&&h4==0&&(!cm.haveItem(2340000,1)||!cm.haveItem(2049118,1)||!cm.haveItem(reverseitem2,1))){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2340000#,#i2049118#,#i"+reverseitem2+"#皆具備");
			   cm.dispose();
		   }
		   ///選項1
		   if(h1==0&&h2==0&&h3==3&&h4==0&&Success<=600&&Success2<=600&&o!=0&&cm.haveItem(2049118,1)&&cm.haveItem(reverseitem2,1)){
			   
			   cm.gainItem(reverseitem2,-1);
			   cm.gainItem(2049118,-1);
             
			   cm.sendSimple("完成了,這是您這次衝捲的結果 :\r\n"
			   +"---------------------------------------------------\r\n"
		       +"#d力量#k:#e "+a+"#n#r("+sch0+")#k = #b"+(a+sch0)+"#k\r\n"
			   +"#d敏捷#k:#e "+b+"#n#r("+sch1+")#k = #b"+(b+sch1)+"#k\r\n"
			   +"#d智力#k:#e "+c+"#n#r("+sch2+")#k = #b"+(c+sch2)+"#k\r\n"
			   +"#d幸運#k:#e "+d+"#n#r("+sch3+")#k = #b"+(d+sch3)+"#k\r\n"
			   +"#d生命#k:#e "+e+"#n#r("+sch4+")#k = #b"+(e+sch4)+"#k\r\n"
			   +"#d魔力#k:#e "+f+"#n#r("+sch5+")#k = #b"+(f+sch5)+"#k\r\n"
			   +"#d物攻#k:#e "+g+"#n#r("+sch6+")#k = #b"+(g+sch6)+"#k\r\n" 
			   +"#d魔攻#k:#e "+h+"#n#r("+sch7+")#k = #b"+(h+sch7)+"#k\r\n"
			   +"#d物防#k:#e "+i+"#n#r("+sch8+")#k = #b"+(i+sch8)+"#k\r\n"
			   +"#d魔防#k:#e "+j+"#n#r("+sch9+")#k = #b"+(j+sch9)+"#k\r\n"
			   +"#d命中#k:#e "+k+"#n#r("+sch10+")#k = #b"+(k+sch10)+"#k\r\n"
			   +"#d迴避#k:#e "+l+"#n#r("+sch11+")#k = #b"+(l+sch11)+"#k\r\n"
			   +"#d速度#k:#e "+m+"#n#r("+sch12+")#k = #b"+(m+sch12)+"#k\r\n"
			   +"#d跳躍#k:#e "+n+"#n#r("+sch13+")#k = #b"+(n+sch13)+"#k\r\n"
			   +"---------------------------------------------------\r\n"
			   +"請問您要將此數值保存下來嗎?\r\n#L106##r我要保存此數值#k\r\n#L107##r我要回真#k"
			   );
		   }
		    if(h1==0&&h2==0&&h3==3&&h4==0&&Success2>600&&Success<=600&&o!=0&&cm.haveItem(2049118,1)&&cm.haveItem(reverseitem2,1)){
			   cm.gainItem(reverseitem2,-1);
			   cm.sendOk("可惜您的#i2049600#爆了,並沒有起到回真作用")
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==3&&h4==0&&Success>600&&Success2>600&&o!=0&&cm.haveItem(2049118,1)&&cm.haveItem(reverseitem2,1)){
			   cm.gainItem(2049118,-1);
			   cm.gainItem(reverseitem2,-1);
			   cm.sendOk("您的混沌卷和回真捲都爆了喔\r\n因無使用#i2340000#的保護故扣除一卷軸數")
			   cm.changeStat(slot[h0],15,o-1);
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==3&&h4==0&&Success>600&&Success2<=600&&o!=0&&cm.haveItem(2049118,1)&&cm.haveItem(reverseitem2,1)){
			   cm.gainItem(2049118,-1);
			   cm.sendOk("您的混沌卷爆了喔\r\n因無使用#i2340000#的保護故扣除一卷軸數")
			   cm.changeStat(slot[h0],15,o-1);
			   cm.dispose();
		   }
		   if(h1==0&&h2==0&&h3==3&&h4==0&&o==0){
			   cm.sendOk("此裝備無卷軸數可使用");
			   cm.dispose();
		   }
		    if(h1==0&&h2==0&&h3==3&&h4==0&&(!cm.haveItem(2049118,1)||!cm.haveItem(reverseitem2,1))){
			   cm.sendOk("您身上並沒有必備材料並不夠\r\n請檢查是否#i2049118#,#i"+reverseitem2+"#皆具備");
			   cm.dispose();
		   }
	
	  }else if(status==6){
          h5=selection; 
		  
          if(h1==0&&h2==0&&h3==0&&h4==1&&h5==0){
			  cm.changeStat(slot[h0],0,store0);
			   cm.changeStat(slot[h0],1,store1);
			   cm.changeStat(slot[h0],2,store2);
			   cm.changeStat(slot[h0],3,store3);
			   cm.changeStat(slot[h0],4,store4);
			   cm.changeStat(slot[h0],5,store5);
			   cm.changeStat(slot[h0],6,store6);
			   cm.changeStat(slot[h0],7,store7);
			   cm.changeStat(slot[h0],8,store8);
			   cm.changeStat(slot[h0],9,store9);
			   cm.changeStat(slot[h0],10,store10);
			   cm.changeStat(slot[h0],11,store11);
			   cm.changeStat(slot[h0],13,store12);
			   cm.changeStat(slot[h0],14,store13);
			   cm.changeStat(slot[h0],15,o-1);
			   cm.changeStat(slot[h0],17,p+1);
               cm.sendOk("完成了,請換頻查看");
               cm.dispose();
		  }
          if(h1==0&&h2==0&&h3==0&&h4==1&&h5==1){
               cm.sendOk("沒問題,已經為您回真");
               cm.dispose();
		  }
		  if(h1==0&&h2==0&&h3==2&&h4==1&&h5==2){
			  cm.changeStat(slot[h0],0,store0);
			   cm.changeStat(slot[h0],1,store1);
			   cm.changeStat(slot[h0],2,store2);
			   cm.changeStat(slot[h0],3,store3);
			   cm.changeStat(slot[h0],4,store4);
			   cm.changeStat(slot[h0],5,store5);
			   cm.changeStat(slot[h0],6,store6);
			   cm.changeStat(slot[h0],7,store7);
			   cm.changeStat(slot[h0],8,store8);
			   cm.changeStat(slot[h0],9,store9);
			   cm.changeStat(slot[h0],10,store10);
			   cm.changeStat(slot[h0],11,store11);
			   cm.changeStat(slot[h0],13,store12);
			   cm.changeStat(slot[h0],14,store13);
			   cm.changeStat(slot[h0],15,o-1);
			   cm.changeStat(slot[h0],17,p+1);
               cm.sendOk("完成了,請換頻查看");
               cm.dispose();
		  }
		   if(h1==0&&h2==0&&h3==2&&h4==1&&h5==3){
               cm.sendOk("沒問題,已經為您回真");
               cm.dispose();
		  }
		   if(h1==0&&h2==0&&h3==1&&h4==1&&h5==4){
			  cm.changeStat(slot[h0],0,sstore0);
			   cm.changeStat(slot[h0],1,sstore1);
			   cm.changeStat(slot[h0],2,sstore2);
			   cm.changeStat(slot[h0],3,sstore3);
			   cm.changeStat(slot[h0],4,sstore4);
			   cm.changeStat(slot[h0],5,sstore5);
			   cm.changeStat(slot[h0],6,sstore6);
			   cm.changeStat(slot[h0],7,sstore7);
			   cm.changeStat(slot[h0],8,sstore8);
			   cm.changeStat(slot[h0],9,sstore9);
			   cm.changeStat(slot[h0],10,sstore10);
			   cm.changeStat(slot[h0],11,sstore11);
			   cm.changeStat(slot[h0],13,sstore12);
			   cm.changeStat(slot[h0],14,sstore13);
			   cm.changeStat(slot[h0],15,o-1);
			   cm.changeStat(slot[h0],17,p+1);
               cm.sendOk("完成了,請換頻查看");
               cm.dispose();
		  }
		  if(h1==0&&h2==0&&h3==3&&h4==1&&h5==6){
			  cm.changeStat(slot[h0],0,sstore0);
			   cm.changeStat(slot[h0],1,sstore1);
			   cm.changeStat(slot[h0],2,sstore2);
			   cm.changeStat(slot[h0],3,sstore3);
			   cm.changeStat(slot[h0],4,sstore4);
			   cm.changeStat(slot[h0],5,sstore5);
			   cm.changeStat(slot[h0],6,sstore6);
			   cm.changeStat(slot[h0],7,sstore7);
			   cm.changeStat(slot[h0],8,sstore8);
			   cm.changeStat(slot[h0],9,sstore9);
			   cm.changeStat(slot[h0],10,sstore10);
			   cm.changeStat(slot[h0],11,sstore11);
			   cm.changeStat(slot[h0],13,sstore12);
			   cm.changeStat(slot[h0],14,sstore13);
			   cm.changeStat(slot[h0],15,o-1);
			   cm.changeStat(slot[h0],17,p+1);
               cm.sendOk("完成了,請換頻查看");
               cm.dispose();
		  }
		  if(h1==0&&h2==0&&h3==1&&h4==1&&h5==5){
               cm.sendOk("沒問題,已經為您回真");
               cm.dispose();
		  }
		  if(h1==0&&h2==0&&h3==3&&h4==1&&h5==7){
               cm.sendOk("沒問題,已經為您回真");
               cm.dispose();
		  }
		  ///// 以上100%
		  if(h1==0&&h2==0&&h3==0&&h4==0&&h5==100){
			  cm.changeStat(slot[h0],0,store0);
			   cm.changeStat(slot[h0],1,store1);
			   cm.changeStat(slot[h0],2,store2);
			   cm.changeStat(slot[h0],3,store3);
			   cm.changeStat(slot[h0],4,store4);
			   cm.changeStat(slot[h0],5,store5);
			   cm.changeStat(slot[h0],6,store6);
			   cm.changeStat(slot[h0],7,store7);
			   cm.changeStat(slot[h0],8,store8);
			   cm.changeStat(slot[h0],9,store9);
			   cm.changeStat(slot[h0],10,store10);
			   cm.changeStat(slot[h0],11,store11);
			   cm.changeStat(slot[h0],13,store12);
			   cm.changeStat(slot[h0],14,store13);
			   cm.changeStat(slot[h0],15,o-1);
			   cm.changeStat(slot[h0],17,p+1);
               cm.sendOk("完成了,請換頻查看");
               cm.dispose();
		  }
          if(h1==0&&h2==0&&h3==0&&h4==0&&h5==101){
               cm.sendOk("沒問題,已經為您回真");
               cm.dispose();
		  }
		  if(h1==0&&h2==0&&h3==2&&h4==0&&h5==102){
			  cm.changeStat(slot[h0],0,store0);
			   cm.changeStat(slot[h0],1,store1);
			   cm.changeStat(slot[h0],2,store2);
			   cm.changeStat(slot[h0],3,store3);
			   cm.changeStat(slot[h0],4,store4);
			   cm.changeStat(slot[h0],5,store5);
			   cm.changeStat(slot[h0],6,store6);
			   cm.changeStat(slot[h0],7,store7);
			   cm.changeStat(slot[h0],8,store8);
			   cm.changeStat(slot[h0],9,store9);
			   cm.changeStat(slot[h0],10,store10);
			   cm.changeStat(slot[h0],11,store11);
			   cm.changeStat(slot[h0],13,store12);
			   cm.changeStat(slot[h0],14,store13);
			   cm.changeStat(slot[h0],15,o-1);
			   cm.changeStat(slot[h0],17,p+1);
               cm.sendOk("完成了,請換頻查看");
               cm.dispose();
		  }
		   if(h1==0&&h2==0&&h3==2&&h4==0&&h5==103){
               cm.sendOk("沒問題,已經為您回真");
               cm.dispose();
		  }
		   if(h1==0&&h2==0&&h3==1&&h4==0&&h5==104){
			  cm.changeStat(slot[h0],0,sstore0);
			   cm.changeStat(slot[h0],1,sstore1);
			   cm.changeStat(slot[h0],2,sstore2);
			   cm.changeStat(slot[h0],3,sstore3);
			   cm.changeStat(slot[h0],4,sstore4);
			   cm.changeStat(slot[h0],5,sstore5);
			   cm.changeStat(slot[h0],6,sstore6);
			   cm.changeStat(slot[h0],7,sstore7);
			   cm.changeStat(slot[h0],8,sstore8);
			   cm.changeStat(slot[h0],9,sstore9);
			   cm.changeStat(slot[h0],10,sstore10);
			   cm.changeStat(slot[h0],11,sstore11);
			   cm.changeStat(slot[h0],13,sstore12);
			   cm.changeStat(slot[h0],14,sstore13);
			   cm.changeStat(slot[h0],15,o-1);
			   cm.changeStat(slot[h0],17,p+1);
               cm.sendOk("完成了,請換頻查看");
               cm.dispose();
		  }
		  if(h1==0&&h2==0&&h3==3&&h4==0&&h5==106){
			  cm.changeStat(slot[h0],0,sstore0);
			   cm.changeStat(slot[h0],1,sstore1);
			   cm.changeStat(slot[h0],2,sstore2);
			   cm.changeStat(slot[h0],3,sstore3);
			   cm.changeStat(slot[h0],4,sstore4);
			   cm.changeStat(slot[h0],5,sstore5);
			   cm.changeStat(slot[h0],6,sstore6);
			   cm.changeStat(slot[h0],7,sstore7);
			   cm.changeStat(slot[h0],8,sstore8);
			   cm.changeStat(slot[h0],9,sstore9);
			   cm.changeStat(slot[h0],10,sstore10);
			   cm.changeStat(slot[h0],11,sstore11);
			   cm.changeStat(slot[h0],13,sstore12);
			   cm.changeStat(slot[h0],14,sstore13);
			   cm.changeStat(slot[h0],15,o-1);
			   cm.changeStat(slot[h0],17,p+1);
               cm.sendOk("完成了,請換頻查看");
               cm.dispose();
		  }
		  if(h1==0&&h2==0&&h3==1&&h4==0&&h5==105){
               cm.sendOk("沒問題,已經為您回真");
               cm.dispose();
		  }
		  if(h1==0&&h2==0&&h3==3&&h4==0&&h5==107){
               cm.sendOk("沒問題,已經為您回真");
               cm.dispose();
		  }
	  }		  
}