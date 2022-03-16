/* Chrishrama
 Dungeon: Sleepywood (105040300)
 
 Refining NPC: 
 * Shoes - All classes, 25 (20 for magicians)-60
 */

var status = 0;
var selectedType = -1;
var selectedItem = -1;
var item;
var mats;
var matQty;
var cost;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else
        cm.dispose();
    if (status == 0 && mode == 1) {
        var selStr = "您好，我住在這裡，但是不要低估了我。怎麼樣我幫你把你一雙新鞋？#b"
        var options = new Array("做一雙劍士鞋子", "做一雙弓箭手鞋子", "做一雙法師鞋子", "做一雙盜賊鞋子");
        for (var i = 0; i < options.length; i++) {
            selStr += "\r\n#L" + i + "# " + options[i] + "#l";
        }

        cm.sendSimple(selStr);
    } else if (status == 1 && mode == 1) {
        selectedType = selection;
        var selStr;
        var shoes;
        if (selectedType == 0) { //warrior shoe
            selStr = "你想要做什麼的鞋子？？#b";
            shoes = new Array("銀戰鬥鞋#k - 劍士 等級. 25#b", "金戰鬥鞋#k - 劍士 等級. 25#b", "黑戰鬥鞋#k - 劍士 等級. 25#b",
                    "綠鬥士鞋#k - 劍士 等級. 30#b", "藍鬥士鞋#k - 劍士 等級. 30#b", "銀鬥士鞋#k - 劍士 等級. 30#b", "紅鬥士鞋#k - 劍士 等級. 30#b",
                    "鐵頭皮鞋#k - 劍士 等級. 35#b", "藍鐵頭鞋#k - 劍士 等級. 35#b", "黑鐵頭鞋#k - 劍士 等級. 35#b",
                    "黃金月長靴#k - 劍士 等級. 40#b", "紫金月鞋#k - 劍士 等級. 40#b", "藍金月鞋#k - 劍士 等級. 40#b",
                    "祖母綠將軍靴#k - 劍士 等級. 50#b", "鋰礦藍將軍靴#k - 劍士 等級. 50#b", "紫礦將軍靴#k - 劍士 等級. 50#b", "黃金將軍鞋#k - 劍士 等級. 50#b",
                    "藍十字鞋#k - 劍士 等級. 60#b", "紫十字鞋#k - 劍士 等級. 60#b", "紅十字鞋#k - 劍士 等級. 60#b");
            ;
        } else if (selectedType == 1) { //bowman shoe
            selStr = "你想要做什麼的鞋子？？#b";
            shoes = new Array("#t1072027##k - 弓箭手 等級. 25#b", "#t1072034##k - 弓箭手 等級. 25#b", "#t1072069##k - 弓箭手 等級. 25#b",
                    "#t1072079##k - 弓箭手 等級. 30#b", "#t1072080##k - 弓箭手 等級. 30#b", "#t1072081##k - 弓箭手 等級. 30#b", "#t1072082##k - 弓箭手 等級. 30#b", "#t1072083##k - 弓箭手 等級. 30#b",
                    "#t1072101##k - 弓箭手 等級. 35#b", "#t1072102##k - 弓箭手 等級. 35#b", "#t1072103##k - 弓箭手 等級. 35#b",
                    "#t1072118##k - 弓箭手 等級. 40#b", "#t1072119##k - 弓箭手 等級. 40#b", "#t1072120##k - 弓箭手 等級. 40#b", "#t1072121##k - 弓箭手 等級. 40#b",
                    "#t1072122##k - 弓箭手 等級. 50#b", "#t1072123##k - 弓箭手 等級. 50#b", "#t1072124##k - 弓箭手 等級. 50#b", "#t1072125##k - 弓箭手 等級. 50#b",
                    "#t1072144##k - 弓箭手 等級. 60#b", "#t1072145##k - 弓箭手 等級. 60#b", "#t1072146##k - 弓箭手 等級. 60#b");
        } else if (selectedType == 2) { //magician shoe
            selStr = "你想要做什麼的鞋子？？#b";
            shoes = new Array("#t1072019##k - 法師 等級. 20#b", "#t1072020##k - 法師 等級. 20#b", "#t1072021##k - 法師 等級. 20#b",
                    "#t1072072##k - 法師 等級. 25#b", "#t1072073##k - 法師 等級. 25#b", "#t1072074##k - 法師 等級. 25#b",
                    "#t1072075##k - 法師 等級. 30#b", "#t1072076##k - 法師 等級. 30#b", "#t1072077##k - 法師 等級. 30#b", "#t1072078##k - 法師 等級. 30#b",
                    "#t1072089##k - 法師 等級. 35#b", "#t1072090##k - 法師 等級. 35#b", "#t1072091##k - 法師 等級. 35#b",
                    "#t1072114##k - 法師 等級. 40#b", "#t1072115##k - 法師 等級. 40#b", "#t1072116##k - 法師 等級. 40#b", "#t1072117##k - 法師 等級. 40#b",
                    "#t1072140##k - 法師 等級. 50#b", "#t1072141##k - 法師 等級. 50#b", "#t1072142##k - 法師 等級. 50#b", "#t1072143##k - 法師 等級. 50#b",
                    "#t1072136##k - 法師 等級. 60#b", "#t1072137##k - 法師 等級. 60#b", "#t1072138##k - 法師 等級. 60#b", "#t1072139##k - 法師 等級. 60#b");
        } else if (selectedType == 3) { //thief shoe
            selStr = "你想要做什麼的鞋子？？#b";
            shoes = new Array("#t1072084##k - 盜賊 等級. 25#b", "#t1072085##k - 盜賊 等級. 25#b", "#t1072086##k - 盜賊 等級. 25#b", "#t1072087##k - 盜賊 等級. 25#b",
                    "#t1072032##k - 盜賊 等級. 30#b", "#t1072033##k - 盜賊 等級. 30#b", "#t1072035##k - 盜賊 等級. 30#b", "#t1072036##k - 盜賊 等級. 30#b",
                    "#t1072104##k - 盜賊 等級. 35#b", "#t1072105##k - 盜賊 等級. 35#b", "#t1072106##k - 盜賊 等級. 35#b",
                    "#t1072107##k - 盜賊 等級. 40#b", "#t1072108##k - 盜賊 等級. 40#b", "#t1072109##k - 盜賊 等級. 40#b", "#t1072110##k - 盜賊 等級. 40#b",
                    "#t1072128##k - 盜賊 等級. 50#b", "#t1072130##k - 盜賊 等級. 50#b", "#t1072129##k - 盜賊 等級. 50#b", "#t1072131##k - 盜賊 等級. 50#b",
                    "#t1072150##k - 盜賊 等級. 60#b", "#t1072151##k - 盜賊 等級. 60#b", "#t1072152##k - 盜賊 等級. 60#b");
        }

        for (var i = 0; i < shoes.length; i++) {
            selStr += "\r\n#L" + i + "# " + shoes[i] + "#l";
        }
        cm.sendSimple(selStr);
    } else if (status == 2 && mode == 1) {
        selectedItem = selection;
        if (selectedType == 0) { //warrior shoe
            var itemSet = new Array(1072051, 1072053, 1072052, 1072003, 1072039, 1072040, 1072041, 1072002, 1072112, 1072113, 1072000, 1072126, 1072127, 1072132, 1072133, 1072134, 1072135, 1072147, 1072148, 1072149);
            var matSet = new Array(new Array(4011004, 4011001, 4000021, 4003000), new Array(4011006, 4011001, 4000021, 4003000), new Array(4021008, 4011001, 4000021, 4003000), new Array(4021003, 4011001, 4000021, 4003000), new Array(4011002, 4011001, 4000021, 4003000),
                    new Array(4011004, 4011001, 4000021, 4003000), new Array(4021000, 4011001, 4000021, 4003000), new Array(4011001, 4021004, 4000021, 4000030, 4003000), new Array(4011002, 4021004, 4000021, 4000030, 4003000), new Array(4021008, 4021004, 4000021, 4000030, 4003000),
                    new Array(4011003, 4000021, 4000030, 4003000, 4000033), new Array(4011005, 4021007, 4000030, 4003000, 4000042), new Array(4011002, 4021007, 4000030, 4003000, 4000041), new Array(4021008, 4011001, 4021003, 4000030, 4003000),
                    new Array(4021008, 4011001, 4011002, 4000030, 4003000), new Array(4021008, 4011001, 4011005, 4000030, 4003000), new Array(4021008, 4011001, 4011006, 4000030, 4003000), new Array(4021008, 4011007, 4021005, 4000030, 4003000),
                    new Array(4021008, 4011007, 4011005, 4000030, 4003000), new Array(4021008, 4011007, 4021000, 4000030, 4003000));
            var matQtySet = new Array(new Array(2, 1, 15, 10), new Array(2, 1, 15, 10), new Array(1, 2, 20, 10), new Array(4, 2, 45, 15), new Array(4, 2, 45, 15), new Array(4, 2, 45, 15), new Array(4, 2, 45, 15), new Array(3, 1, 30, 20, 25), new Array(3, 1, 30, 20, 25), new Array(2, 1, 30, 20, 25),
                    new Array(4, 100, 40, 30, 100), new Array(4, 1, 40, 30, 250), new Array(4, 1, 40, 30, 120), new Array(1, 3, 6, 65, 45), new Array(1, 3, 6, 65, 45), new Array(1, 3, 6, 65, 45), new Array(1, 3, 6, 65, 45), new Array(1, 1, 8, 80, 55), new Array(1, 1, 8, 80, 55), new Array(1, 1, 8, 80, 55));
            var costSet = new Array(10000, 10000, 12000, 20000, 20000, 20000, 20000, 22000, 22000, 25000, 38000, 38000, 38000, 50000, 50000, 50000, 50000, 60000, 60000, 60000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 1) { //bowman shoe
            var itemSet = new Array(1072027, 1072034, 1072069, 1072079, 1072080, 1072081, 1072082, 1072083, 1072101, 1072102, 1072103, 1072118, 1072119, 1072120, 1072121, 1072122, 1072123, 1072124, 1072125, 1072144, 1072145, 1072146);
            var matSet = new Array(new Array(4000021, 4011000, 4003000), new Array(4000021, 4021003, 4003000), new Array(4000021, 4021000, 4003000), new Array(4000021, 4021000, 4003000), new Array(4000021, 4021005, 4003000), new Array(4000021, 4021003, 4003000),
                    new Array(4000021, 4021004, 4003000), new Array(4000021, 4021006, 4003000), new Array(4021002, 4021006, 4000030, 4000021, 4003000), new Array(4021003, 4021006, 4000030, 4000021, 4003000), new Array(4021000, 4021006, 4000030, 4000021, 4003000),
                    new Array(4021000, 4003000, 4000030, 4000024), new Array(4021006, 4003000, 4000030, 4000027), new Array(4011003, 4003000, 4000030, 4000044), new Array(4021002, 4003000, 4000030, 4000009), new Array(4011001, 4021006, 4021008, 4000030, 4003000, 4000033),
                    new Array(4011001, 4021006, 4021008, 4000030, 4003000, 4000032), new Array(4011001, 4021006, 4021008, 4000030, 4003000, 4000041), new Array(4011001, 4021006, 4021008, 4000030, 4003000, 4000042), new Array(4011006, 4021000, 4021007, 4000030, 4003000),
                    new Array(4011006, 4021005, 4021007, 4000030, 4003000), new Array(4011006, 4021003, 4021007, 4000030, 4003000));
            var matQtySet = new Array(new Array(35, 3, 10), new Array(35, 1, 10), new Array(35, 1, 10), new Array(50, 2, 15), new Array(50, 2, 15), new Array(50, 2, 15), new Array(50, 2, 15), new Array(50, 2, 15),
                    new Array(3, 1, 15, 30, 20), new Array(3, 1, 15, 30, 20), new Array(3, 1, 15, 30, 20), new Array(4, 30, 45, 20), new Array(4, 30, 45, 20), new Array(5, 30, 45, 40), new Array(5, 30, 45, 120),
                    new Array(3, 3, 1, 60, 35, 80), new Array(3, 3, 1, 60, 35, 150), new Array(3, 3, 1, 60, 35, 100), new Array(3, 3, 1, 60, 35, 250), new Array(5, 8, 1, 75, 50), new Array(5, 8, 1, 75, 50), new Array(5, 8, 1, 75, 50));
            var costSet = new Array(9000, 9000, 9000, 19000, 19000, 19000, 19000, 19000, 19000, 20000, 20000, 20000, 32000, 32000, 40000, 40000, 50000, 50000, 50000, 50000, 60000, 60000, 60000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 2) { //magician shoe
            var itemSet = new Array(1072019, 1072020, 1072021, 1072072, 1072073, 1072074, 1072075, 1072076, 1072077, 1072078, 1072089, 1072090, 1072091, 1072114, 1072115, 1072116, 1072117, 1072140, 1072141, 1072142, 1072143, 1072136, 1072137, 1072138, 1072139);
            var matSet = new Array(new Array(4021005, 4000021, 4003000), new Array(4021001, 4000021, 4003000), new Array(4021000, 4000021, 4003000), new Array(4011004, 4000021, 4003000), new Array(4021006, 4000021, 4003000), new Array(4021004, 4000021, 4003000),
                    new Array(4021000, 4000021, 4003000), new Array(4021002, 4000021, 4003000), new Array(4011004, 4000021, 4003000), new Array(4021008, 4000021, 4003000), new Array(4021001, 4021006, 4000021, 4000030, 4003000), new Array(4021000, 4021006, 4000021, 4000030, 4003000),
                    new Array(4021008, 4021006, 4000021, 4000030, 4003000), new Array(4021000, 4000030, 4000043, 4003000), new Array(4021005, 4000030, 4000037, 4003000), new Array(4011006, 4021007, 4000030, 4000027, 4003000), new Array(4021008, 4021007, 4000030, 4000014, 4003000),
                    new Array(4021009, 4011006, 4021000, 4000030, 4003000), new Array(4021009, 4011006, 4021005, 4000030, 4003000), new Array(4021009, 4011006, 4021001, 4000030, 4003000), new Array(4021009, 4011006, 4021003, 4000030, 4003000),
                    new Array(4021009, 4011006, 4011005, 4000030, 4003000), new Array(4021009, 4011006, 4021003, 4000030, 4003000), new Array(4021009, 4011006, 4011003, 4000030, 4003000), new Array(4021009, 4011006, 4021002, 4000030, 4003000));
            var matQtySet = new Array(new Array(1, 30, 5), new Array(1, 30, 5), new Array(1, 30, 5), new Array(1, 35, 10), new Array(1, 35, 10), new Array(1, 35, 10), new Array(2, 50, 15), new Array(2, 50, 15), new Array(2, 50, 15),
                    new Array(1, 50, 15), new Array(3, 1, 30, 15, 20), new Array(3, 1, 30, 15, 20), new Array(2, 1, 40, 25, 20), new Array(4, 40, 35, 25), new Array(4, 40, 70, 25), new Array(2, 1, 40, 20, 25), new Array(2, 1, 40, 30, 30),
                    new Array(1, 3, 3, 60, 40), new Array(1, 3, 3, 60, 40), new Array(1, 3, 3, 60, 40), new Array(1, 3, 3, 60, 40), new Array(1, 4, 5, 70, 50), new Array(1, 4, 5, 70, 50), new Array(1, 4, 5, 70, 50), new Array(1, 4, 5, 70, 50));
            var costSet = new Array(3000, 3000, 3000, 8000, 8000, 8000, 18000, 18000, 18000, 18000, 20000, 20000, 22000, 30000, 30000, 35000, 40000, 50000, 50000, 50000, 50000, 60000, 60000, 60000, 60000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        } else if (selectedType == 3) { //thief shoe
            var itemSet = new Array(1072084, 1072085, 1072086, 1072087, 1072032, 1072033, 1072035, 1072036, 1072104, 1072105, 1072106, 1072107, 1072108, 1072109, 1072110, 1072128, 1072130, 1072129, 1072131, 1072150, 1072151, 1072152);
            var matSet = new Array(new Array(4021005, 4000021, 4003000), new Array(4021000, 4000021, 4003000), new Array(4021003, 4000021, 4003000), new Array(4021004, 4000021, 4003000), new Array(4011000, 4000021, 4003000), new Array(4011001, 4000021, 4003000), new Array(4011004, 4000021, 4003000), new Array(4011006, 4000021, 4003000), new Array(4021000, 4021004, 4000021, 4000030, 4003000), new Array(4021003, 4021004, 4000021, 4000030, 4003000), new Array(4021002, 4021004, 4000021, 4000030, 4003000), new Array(4021000, 4000030, 4000033, 4003000), new Array(4021003, 4000030, 4000032, 4003000), new Array(4021006, 4000030, 4000040, 4003000), new Array(4021005, 4000030, 4000037, 4003000), new Array(4011007, 4021005, 4000030, 4000037, 4003000), new Array(4011007, 4021000, 4000030, 4000043, 4003000), new Array(4011007, 4021003, 4000030, 4000045, 4003000), new Array(4011007, 4021001, 4000030, 4000036, 4003000), new Array(4021008, 4011007, 4021005, 4000030, 4003000), new Array(4021008, 4011007, 4011005, 4000030, 4003000), new Array(4021008, 4011007, 4021000, 4000030, 4003000));
            var matQtySet = new Array(new Array(1, 35, 10), new Array(1, 35, 10), new Array(1, 35, 10), new Array(1, 35, 10), new Array(3, 50, 15), new Array(3, 50, 15), new Array(2, 50, 15), new Array(2, 50, 15), new Array(3, 1, 30, 15, 20), new Array(3, 1, 30, 15, 20), new Array(3, 1, 30, 15, 20),
                    new Array(5, 45, 50, 30), new Array(4, 45, 30, 30), new Array(4, 45, 3, 30), new Array(4, 45, 70, 30), new Array(2, 3, 50, 200, 35), new Array(2, 3, 50, 150, 35), new Array(2, 3, 50, 80, 35), new Array(2, 3, 50, 80, 35), new Array(1, 1, 8, 75, 50), new Array(1, 1, 5, 75, 50), new Array(1, 1, 1, 75, 50));
            var costSet = new Array(9000, 9000, 9000, 9000, 19000, 19000, 19000, 21000, 20000, 20000, 20000, 40000, 32000, 35000, 35000, 50000, 50000, 50000, 50000, 60000, 60000, 60000);
            item = itemSet[selectedItem];
            mats = matSet[selectedItem];
            matQty = matQtySet[selectedItem];
            cost = costSet[selectedItem];
        }

        var prompt = "你想要做一雙 #t" + item + "#? 在這種情況下, 我為了要做出最棒的鞋子，我建議你確保裝備欄空間足夠。#b";

        if (mats instanceof Array) {
            for (var i = 0; i < mats.length; i++) {
                prompt += "\r\n#i" + mats[i] + "# " + matQty[i] + " #t" + mats[i] + "#";
            }
        } else {
            prompt += "\r\n#i" + mats + "# " + matQty + " #t" + mats + "#";
        }

        if (cost > 0)
            prompt += "\r\n#i4031138# " + cost + " 楓幣";

        cm.sendYesNo(prompt);
    } else if (status == 3 && mode == 1) {
        var complete = true;

        if (cm.getMeso() < (cost)) {
            cm.sendOk("我只接受楓幣。");
            cm.dispose();
            return;
        } else {
            if (mats instanceof Array) {

                for (var i = 0; complete && i < mats.length; i++)
                {
                    if (!cm.haveItem(mats[i], matQty[i]))
                    {
                        complete = false;
                    }
                }
            } else {
                if (!cm.haveItem(mats, matQty))
                {
                    complete = false;
                }
            }
        }

        if (!complete)
            cm.sendOk("很抱歉由於你的材料不足，所以我不想幫你做了。");
        else {
            if (mats instanceof Array) {
                for (var i = 0; i < mats.length; i++) {
                    cm.gainItem(mats[i], -matQty[i]);
                }
            } else
                cm.gainItem(mats, -matQty);

            cm.gainMeso(-cost);
            cm.gainItem(item, 1);

            cm.sendOk("製作完畢。");
        }
        cm.dispose();
    }
}