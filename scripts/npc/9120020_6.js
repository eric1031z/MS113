var status = 0;
var beauty = 0;
var hairprice = 1000000;
var haircolorprice = 1000000;
var mhair = Array(35690,35700,35710,35711,35712,35713,35714,35715,35716,35717,35950,35951,35952,35953,35954,35955,35956,35957,35960,35961,35962,35963,35964,35965,35966,35967,35400,35550,35551,35552,35553,35554,35555,35556,35557,31661,31662,31663,31664,31665,31666,31667,37440,37441,37442,37443,37444,37445,37446,37447,37871,37872,37873,37874,37875,37876,37877,38610,38611,38612,38613,38614,38615,38616,38617,38680,38681,38682,38683,38684,38685,38686,38687,38920,38940,38941,38942,38943,38944,38945,38946,38947);
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
