//雪人 9330021 雪人男髮到36990 女38880
var status = 0;
var beauty = 0;
var hairprice = 1000000;
var haircolorprice = 1000000;
var mhair = Array(33280, 33290, 33300, 33310, 33320, 33330, 33340, 33350, 33360, 33370, 33380, 33390, 33400, 33410, 33420, 33430, 33440, 33450, 33460, 33470, 33480, 33490, 33500, 33510, 33520, 33530, 33540, 33550, 33580, 33600, 33610, 33620, 33630, 33640, 33650, 33660, 33670, 33680, 33690, 33700, 33710, 33720, 33730, 33740, 33750, 33760, 33770, 33780, 33790, 33800, 33810, 33820, 33830, 33930, 33940, 33950, 33960, 33970, 30030, 30040, 30050, 30060, 30070, 30080, 30090, 30100, 30110, 30120, 30130, 30140, 30150, 30160, 30170, 30180, 30190, 30200, 30210, 30220, 30230, 30240, 30250, 30260, 30270, 30280, 30290, 30300, 30310, 30320, 30330, 30340, 30350, 30360, 30370, 30400, 30410, 30420, 30440, 30450, 30460, 30470);
var fhair = Array(34310, 34320, 34330, 34340, 34350, 34360, 34370, 34380, 34390, 34400, 34410, 34420, 34430, 34440, 34450, 34460, 34470, 34480, 34490, 34510, 34540, 34560, 34590, 34600, 34610, 34620, 34630, 34640, 34650, 34660, 34670, 34680, 34690, 34700, 34710, 34720, 34730, 34750, 34760, 34770, 34780, 34790, 34800, 34810, 34820, 34830, 34840, 34850, 34860, 34870, 34880, 34890, 34900, 34910, 34940, 34950, 34960, 34970, 34980, 34990, 37000, 37010, 37020, 37030, 37040, 37050, 37060, 37070, 37080, 37090, 37100, 37110, 37120, 37130, 37140);
var hairnew = Array();

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode < 1) {
        cm.dispose();
    } else {
        status++;
        if (status == 0) 
            cm.sendSimple("沒問題，請選擇您想要的服務!\r\n#L1#使用 5個#z4000038#與100個#z4000100#來更換指定髮型#l");
        else if (status == 1) {
            if (selection == 0) {
                beauty = 0;
                cm.sendOk("87")
            } else if (selection == 1) {
                beauty = 1;
                hairnew = Array();
                if (cm.getPlayer().getGender() == 0)
                    for(var i = 0; i < mhair.length; i++)
                        hairnew.push(mhair[i] + parseInt(cm.getPlayer().getHair()% 10));
                if (cm.getPlayer().getGender() == 1)
                    for(var i = 0; i < fhair.length; i++)
                        hairnew.push(fhair[i] + parseInt(cm.getPlayer().getHair() % 10));
                cm.sendStyle("選擇您想要的髮型吧!!\r\n#r請將髮色更換為黑色後再使用此項功能!!", hairnew);
            } else if (selection == 2) {
                beauty = 2;
                haircolor = Array();
                var current = parseInt(cm.getPlayer().getHair()/10)*10;
                for(var i = 0; i < 8; i++)
                    haircolor.push(current + i);
                cm.sendStyle("選擇您想要的髮型吧!!\r\n#r請將髮色更換為黑色後再使用此項功能!!", haircolor);
            }
        } else if (status == 2){
            cm.dispose();
            if (beauty == 1){
                 if (cm.haveItem(4000038,5) && cm.haveItem(4000100,200)){
                    cm.gainItem(4000038, -5);
					cm.gainItem(4000100, -200);
                    cm.setHair(hairnew[selection]);
                    cm.sendOk("已經變更完成囉!!");
                } else
                    cm.sendOk("您貌似沒有#b5個#v4000038#及200個#v4000100#k..");
            }
            if (beauty == 2){
                if (cm.haveItem(5151001)){
                    cm.gainItem(5151001, -1);
                    cm.setHair(haircolor[selection]);
                    cm.sendOk("享受!");
                } else
                    cm.sendOk("您貌似沒有#b#t5151001##k..");
            }
            if (beauty == 0){
                if (selection == 0 && cm.getMeso() >= hairprice) {
                    cm.gainMeso(-hairprice);
                    cm.gainItem(5150001, 1);
                    cm.sendOk("享受!");
                } else if (selection == 1 && cm.getMeso() >= haircolorprice) {
                    cm.gainMeso(-haircolorprice);
                    cm.gainItem(5151001, 1);
                    cm.sendOk("享受!");
                } else
                    cm.sendOk("您沒有足夠的楓幣購買!");
            }
        }
    }
}
