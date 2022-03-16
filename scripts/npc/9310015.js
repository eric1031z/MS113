var status;

var req = 2022034 // 需要的物品 默認是粽子了
var h  // 不要動
var h0=-1; // 不要動

var item = [
     [2450000,200],  // 新增物品在這裡改 形式 [獎勵物品ID,需要的粽子數量],
	 [2022530,200],
	 [2022531,350],
	 [2049600,500],
	 [2049100,300],
	 [2049118,500],
	 [1012451,3000],

]


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
  var exchange = "";
  for(var v=0; v<item.length; v++){
   exchange += "\r\n#L" + v + "#我要使用" + item[v][1] + "個#i" + req + "##t" + req + "#來兌換#i" + item[v][0] + "##t" + item[v][0] + "#\r\n" ;
  }
  cm.sendSimple("咕咕雞兌換活動開跑了!" + exchange + "\r\n#L100#用#r3000#k個#i" + req + "##t" + req + "#加#i1012451##t1012451#換#i1012452##t1012452#");
  
 }else if (status == 1){
  h=selection;	 
  if(h!=100){
  cm.sendSimple("你確定要用"+ item[h][1] + "個#i" + req + "##t" + req + "#兌換#i" + item[h][0] + "##t" + item[h][0] + "#嗎?\r\n#L0##d確定#k\r\n#L1##d不要#k");
  }
  if(h==100){
  cm.sendSimple("你確定要用3000個#i"+req+"##t"+req+"#和#i1012451##t1012451#來兌換#i1012452##t1012452#嗎?\r\n#L2##d確定#k\r\n#L3##d不要#k");
  }
 }else if (status == 2){
  h0=selection;
  if (h0==0&&h!=100&&!cm.canHold(item[h][0])) {
   cm.sendNext("你的背包裝不下");
   cm.dispose();
   return;
  }
  if (h0==2&&h==100&&!cm.canHold(1012452)) {
   cm.sendNext("你的背包裝不下");
   cm.dispose();
   return;
  }
  if(h0==2&&h==100&&cm.haveItem(1012451)&&cm.haveItem(req,3000)){
   cm.gainItem(1012452,1);
   cm.gainItem(req,-3000);
   cm.gainItem(1012451,-1);
   cm.sendOk("完成了!恭喜你!");
   cm.dispose();      
  }
        if(h0==2&&h==100&&!cm.haveItem(1012451)){
   cm.sendOk("您背包裡並沒有#i1012451##t1012451#喔!");
   cm.dispose();
  }
  if(h0==2&&h==100&&!cm.haveItem(req,3000)){
            cm.sendOk("兌換#i1012452##t1012452#需要#r3000#k個#i" + req + "##t" + req + "#請確認一下喔");
            cm.dispose();
  }
        if(h0==2&&h==100&&!cm.haveItem(req,3000)&&!cm.haveItem(1012451)){
            cm.sendOk("你啥都沒你來幹嘛的?");
            cm.dispose();
  }   
  else if (h0==0&&h!=100&&!cm.haveItem(req, item[h][1])) {
   cm.sendNext("您身上的#i" + req + "##t" + req + "#不夠\r\n兌換#i" + item[h][0] + "##t" + item[h][0] + "#需要#r" + item[h][1] + "#k個#t" + req + "#" );
   cm.dispose();
   return;
  } 
  else if(h0==0&&h!=100&&cm.haveItem(req, item[h][1])){
  cm.gainItem(req, -item[h][1]);
  cm.gainItem(item[h][0], 1);
  cm.sendOk("恭喜你!有需要再找我呦!");
  cm.dispose();
 }
  if(h0==1||h0==3){
	  cm.sendOk("沒關係!");
	  cm.dispose();
  }
 }
}