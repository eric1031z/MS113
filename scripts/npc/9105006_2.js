﻿var status = 0;
var beauty = 0;
var price = 0;
var mface = Array(38621,31890,31930,31990,32160,33080,33100,33170,33360,33240,33250,33270,33440,33450,33500,33510,33580,33630,34010,34040,34070,34090,34120,34160,34170,34180,34190,34210,34320,34220,34230,34250,34270,34290,34360,34420,34480,34440,34560,34490,34590,34620,34630,33400,33410,33430,34640,36120,31900,34450,33710,33720,34710,34730,34720,33660,33690,34660,33810,33960,36020,33760,33770,33780,33800,34790,34800,34840,34850,34860,34870,34770,34780,34940,34950,34970,35000,35010,35020,35030,35040,35050,35060,35070,35090,36080,36140,36150,36170,36180,36220,36230,36330,36430,36440,36520,37000,37010,37030,37060,37190,37210,37220,37230,37300,37310,37320,37370,37380,37400,37450,37510,37520,37530,37570,37080,37270,37340,37350,37610,37630,37640,37650,37670,37680,37700,37710,37720,37730,37740,37750,37760,37770,37780,37790,37800,37810,37820,37830,37840,37850,37860,37910,37940,37950,37960,37970,37980,37990,38000,38010,38020,38030,38040,38060,38070,38090,38100,38120,35150,35180,35190,35200,35260,35280,35290,35300,35310,38130,38270,38280,38290,38300,38310,38390,38410,38420,38430,38440,38460,38470,38490,38520,38540,38560,38570,38580,38600,38630,38640,38660,38690,38740,38750,38760,38800,38810,38880,37930,32650,35330,35360,35430,35440,35460,35470,35500,35510,35520,35530,35560,35570,35630,35650,35660,35680,35720,39140,39150,39170,39160,35590,35600,38730,38710,35840,35860,35910,35940,38970,39100,39110,39120,39130,35400,37440,39270,39180,39210,39230,37480,37410,38930,37420,39960);
var facenew = Array();

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("如果你有#b#v5150041##z5150041##k,就能抽到很好看的髮型囉!!\r\n#L1#我要使用#b#v5150041##k抽髮型");
        } else if (status == 1) {
            if (selection == 1) {
                if (cm.getPlayer().getGender() == 0) {
                    for (var i = 0; i < mface.length; i++) {
                        facenew.push(mface[i]);
                    }
                }
                if (cm.getPlayer().getGender() == 1) {
                    for (var i = 0; i < mface.length; i++) {
                        facenew.push(mface[i]);
                    }
                }
				if (cm.getPlayer().getGender() == 2) {
                    for (var i = 0; i < mface.length; i++) {
                        facenew.push(mface[i]);
                    }
				}
                cm.sendYesNo("注意!!!這是隨機抽獎唷!!你真的要抽看看嗎?");
            } else {
                cm.dispose();
            }
        } else if (status == 2) {
            if (cm.haveItem(5150041 ,1)) {
				cm.gainItem(5150041 , -1);
                
                cm.setHair(facenew[Math.floor(Math.random() * facenew.length)]);
                cm.sendOk("好了，是不是牛逼呀。");
				//cm.worldMessage(6, "[魅力無限魔髮屍] " + " 玩家 " + cm.getChar().getName() + " 找我隨機兌換超屌髮型呢!!!!!!");
            } else {
                cm.sendOk("您貌似沒有#b傳說中的美髮券#k..請至購物商城購買。");
            }
            cm.dispose();
        }
    }
}