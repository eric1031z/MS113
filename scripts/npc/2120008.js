var status = -1;

var item =[
[3994015,1],
[3994016,1],
[3994002,1],
[3994003,1],
[3994051,1],
[3994007,1],
[3994001,1],
[3994004,1],
[3994051,1],
[3994000,1],
[3994010,1]
];



function start() {
 status=-1;
 action(1, 0, 0);
}

function action(mode, type, selection) {
 if (mode == 1) {
  status++;
 } else if (mode == 0) {
  status--;
 } else {
  cm.dispose();
  return;
 }
  var i = -1;
 if (status <= i++) {
  cm.sendOk("你考慮清楚再來吧!");
  cm.dispose();
 } else if (status === i++) {
  var msg = "";
  for (var v = 0; v < item.length; v++) {
   var id = item[v][0];
   var qty = item[v][1];
   msg += "#i" + item[v][0] + "#"  ;
  }
 cm.sendNext("歡慶兒童節!這是咕咕雞谷兒童節蒐集兌換獎勵npc\r\n若蒐集了\r\n"+msg +"\r\n即可兌換超稀有點武!\r\n#r#L0#我要兌換!#r");
}else if(status === i++){
switch (selection){
 case 0:
if (cm.haveItem(3994015,1)&&cm.haveItem(3994016,1)&&cm.haveItem(3994002,1)&&cm.haveItem(3994003,1)&&cm.haveItem(3994051,1)&&cm.haveItem(3994007,1)&&cm.haveItem(3994001,1)&&cm.haveItem(3994004,1)&&cm.haveItem(3994051,1)&&cm.haveItem(3994000,1)&&cm.haveItem(3994010,1)){
cm.gainItem(3994015,-1);
cm.gainItem(3994016,-1);
cm.gainItem(3994002,-1);
cm.gainItem(3994003,-1);
cm.gainItem(3994051,-1);
cm.gainItem(3994007,-1);
cm.gainItem(3994001,-1);
cm.gainItem(3994004,-1);
cm.gainItem(3994051,-1);
cm.gainItem(3994000,-1);
cm.gainItem(3994010,-1);
cm.gainItem(1702693,1);
cm.sendOk("恭喜你!");
cm.dispose();
}else
 cm.sendOk("你還沒蒐集完成喔!");
cm.dispose();
}
}
}