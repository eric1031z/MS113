//雪人 9330021 雪人男髮到36990 女38880
var status = 0;
var beauty = 0;
var hairprice = 1000000;
var haircolorprice = 1000000;
var mhair = Array(30000, 30020, 30480, 30490, 30510, 30520, 30530, 30540, 30550, 30560, 30570, 30580, 30590, 30600, 30610, 30620, 30630, 30640, 30650, 30660, 30670, 30680, 30700, 30710, 30720, 30730, 30740, 30750, 30790, 30800, 30810, 30820, 30830, 30840, 30850, 30860, 30870, 30880, 30890, 30900, 30910, 30920, 30930, 30940, 30950, 30990, 32160, 32430, 32440, 32450, 32460, 32470, 32480, 32490, 32500, 32560, 32580, 32640, 32650, 32660, 33000, 33060, 33070, 33080, 33090, 33100, 33110, 33120, 33130, 33140, 33150, 33170, 33180, 33190, 33200, 33210, 33220, 33230, 33240, 33250, 33260, 33270);
var fhair = Array(37580,37610,37620,37630,37640,37650,37670,37680,37690,37700,37710,37720,37730,37740,37750,37760,37770,37780,37790,37800,37810,37820,37830,37840,37850,37860,37880,37910,37920,37940,37950,37960,37970,37980,37990,38000,38010,38020,38030,38040,38050,38060,38070,38090,38100,38110,38120,39160,38700,38730,38710,38950,38960,38970,38980,38990,39100,39110,39120,39130,38860,38890,38900,38906,38907);
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
