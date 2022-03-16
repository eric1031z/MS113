load('nashorn:mozilla_compat.js');
importPackage(Packages.tools);
importPackage(Packages.client.inventory);
importPackage(Packages.server);


var status;
var h=-1;
var h1=-1;
var h2=-1;

var req = 2450000 // 點歌券代碼




function start(){
  status = -1;
  action(1,0,0);
}



function action(mode, type, selection){
 if(mode==1){
  status++;
 }else 
  status--;
 
 if(status==0){
  this. play = cm.getBossLog("點歌囉");
  if(cm.getBossLog("點歌囉")==-1){
   play=0;
  }
  cm.sendSimple("歡迎來到#r咕咕雞谷#k!\r\n這裡是點歌NPC,只要使用#r500萬#k楓幣或點歌券就能點歌囉!每天可以點5首你喜歡的歌\r\n#d今日已使用次數:"+play+"/5#k\r\n\r\n#L0#低能英文歌\r\n#L1#低能中文歌");
 }else if(status==1){
  h=selection;
  if(h==0&&play<100){
   cm.sendSimple("#L0# Slushii-So Long\r\n" +
   "#L1# Joe Hertz-Ashes\r\n");
  }
  if(play>=100){
   cm.sendOk("您今天的點歌功能已達使用上限");
   cm.dispose();
  }
 }else if(status==2){
  h1=selection;
     cm.sendSimple("請問你想要如何點播呢?\r\n#L0##d使用500萬楓幣\r\n#L1#使用#i"+req+"##t"+req+"##k");  
 }else if(status==3){
  h2=selection;
  var c=0 ;
  var w = cm.getPlayer().getMap().getId()
     var k = cm.getClient().getChannelServer().getMapFactory().getMap(w).getCharacters();
     var j = cm.getClient().getChannelServer().getMapFactory().getMap(w).getCharactersSize();
  if(h==0&&h1==0&&h2==0&&cm.getPlayer().getMeso()>=5000000){
   for (var i=0; i<j ; i++){
           cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(k[i].getName()).getMap().broadcastMessage(MaplePacketCreator.musicChange("Bgm00/SleepyWood"));
         }
   cm.setBossLog("點歌囉");
   cm.gainMeso(-5000000);
   cm.dispose();
  }
  if(h==0&&h1==0&&h2==1&&cm.haveItem(req)){
   for (var i=0; i<j ; i++){
           cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(k[i].getName()).getMap().broadcastMessage(MaplePacketCreator.musicChange("Bgm00/SleepyWood"));
         }
   cm.gainItem(req,-1);
   cm.setBossLog("點歌囉");
   c=1;
   cm.dispose();
  }
  if(h==0&&h1==0&&h2==0&&cm.getPlayer().getMeso()<5000000){
   cm.sendOk("您的楓幣不夠喔");
   cm.dispose();
  }
  if(h==0&&h1==0&&h2==1&&!cm.haveItem(req)&&c==0){
   cm.sendOk("您身上沒有點歌卷");
   cm.dispose();
  }
  if(h==0&&h1==1&&h2==0&&cm.getPlayer().getMeso()>=5000000){
   for (var i=0; i<j ; i++){
           cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(k[i].getName()).getMap().broadcastMessage(MaplePacketCreator.musicChange("Bgm00/FloralLife"));
         }
   cm.setBossLog("點歌囉");
   cm.gainMeso(-5000000);
   cm.dispose();
  }
  if(h==0&&h1==1&&h2==1&&cm.haveItem(req)){
   for (var i=0; i<j ; i++){
           cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(k[i].getName()).getMap().broadcastMessage(MaplePacketCreator.musicChange("Bgm00/FloralLife"));
         }
   cm.setBossLog("點歌囉");
   cm.gainItem(req,-1);
   c=1;
   cm.dispose();
  }
  if(h==0&&h1==0&&h2==0&&cm.getPlayer().getMeso()<5000000){
   cm.sendOk("您的楓幣不夠喔");
   cm.dispose();
  }
  if(h==0&&h1==1&&h2==1&&!cm.haveItem(req)&&c==0){
   cm.sendOk("您身上沒有點歌卷");
   cm.dispose();
  }
  
 }
}