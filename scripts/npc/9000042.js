var status;
var h=-1; //不要動
var item=4034232; // 需要的道具
var req1=5; // 需要道具的數量
var req2=5;
var req3=3;
var day=3; // 道具幾天

function start(){
 status=-1;
 action(1,0,0);
}

function action(mode,type,selection){
 if(mode==1){
  status++;
 }else status--;
 
 if(mode==0){
  cm.dispose();
 }
 
 if(status==0){
  cm.sendSimple("這是超級綠水零兌換NPC\r\n只要你擁有一定數量的#i"+item+"#就能兌換以下好禮\r\n#L0##i1112127##t1112127##r(需要5個#i4034232#)#k\r\n#L1##i1122017##t1122017##r(需要5個#i4034232#)#k\r\n#L2##i2450000##t2450000##r(需要3個#i4034232#)#k");
 }else if(status==1){
  var h=selection;
  if(h==0&&cm.haveItem(item,req1)){
   cm.gainItem(1112127,1,false,day,"");
   cm.gainItem(item,-req1);
   cm.sendOk("恭喜你!繼續加油!");
   cm.dispose();
  }
  if(h==0&&!cm.haveItem(item,req1)){
   cm.sendOk("您所擁有的道具數量不夠");
   cm.dispose();
  }
  if(h==1&&cm.haveItem(item,req2)){
   cm.gainItem(1122017,1,false,day,"");
   cm.gainItem(item,-req2);
   cm.sendOk("恭喜你!繼續加油!");
   cm.dispose();
  }
  if(h==1&&!cm.haveItem(item,req2)){
   cm.sendOk("您所擁有的道具數量不夠");
   cm.dispose();
  }
  if(h==2&&cm.haveItem(item,req3)){
   cm.gainItem(2450000,1,false,day,"");
   cm.gainItem(item,-req3);
   cm.sendOk("恭喜你!繼續加油!");
   cm.dispose();
  }
  if(h==2&&!cm.haveItem(item,req3)){
   cm.sendOk("您所擁有的道具數量不夠");
   cm.dispose();
  }
 }
}