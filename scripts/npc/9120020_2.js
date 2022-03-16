//雪人 9330021 雪人男髮到36990 女38880
var status = 0;
var beauty = 0;
var hairprice = 1000000;
var haircolorprice = 1000000;
var mhair = Array(35790, 35780, 30820, 33580, 35200, 35280, 35290, 35300, 35330, 35460, 36280, 36440, 36700, 36750, 36770, 36780, 36790, 36800, 36810, 36820, 36830, 36840, 36850, 36860, 36900, 36910, 36920, 36930, 36940, 36950, 36980, 36990, 38270, 33980, 33990, 34740, 36000, 36010, 36020, 36030, 36040, 36050, 36070, 36080, 36090, 36100, 36110, 36120, 36130, 36140, 36150, 36160, 36170, 36180, 36190, 36200, 36210, 36220, 36230, 36240, 36250, 36280, 36300, 36310, 36320, 36330, 36340, 36350, 36380, 36390, 36400, 36410, 36420, 36430, 36440, 36450, 36460, 36470, 36480, 36510, 36520, 36530, 36570, 36580, 36590, 36600, 36610, 36620, 36630, 36640, 36650, 36660, 36670, 36680, 36690, 36700, 36710, 36720, 36730, 36740, 36750, 36760, 35580, 35590, 35600,35050);
var fhair = Array(34870, 31030, 31040, 31050, 31060, 31070, 31080, 31090, 31100, 31110, 31120, 31130, 31140, 31150, 31160, 31170, 31180, 31190, 31200, 31210, 31220, 31230, 31240, 31250, 31260, 31270, 31280, 31290, 31300, 34880, 35440, 37040, 37110, 37210, 37230, 37340, 37380, 37400, 37450, 37650, 37700, 37710, 37760, 37920, 37930, 38010, 38060, 38100, 38110, 38120, 38310, 38390, 38400, 38420, 38430, 38460, 38490, 38520, 38560, 38570, 38600, 38750, 38760, 38800, 38810, 34210, 34220, 34230, 34240, 34250, 34260, 34270, 34280, 34290, 34300, 37530, 40180, 38860, 38280, 38290, 38300, 38310, 38390, 38400, 38410, 38440, 38460, 38470, 38490, 38540, 38560, 38580, 38570, 38590, 38600, 38630, 38750, 38760, 38800, 38810, 38800, 38840, 38880, 38710);
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
