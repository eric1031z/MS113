var status;
var curMap;
var playerStatus;
var chatState;
var questions = Array("請問法師一轉要幾等",
        "請問盜賊一轉要幾等",
        "請問法師轉職需要多少智力",
        "請問弓箭手轉職需要多少敏捷",
        "請問幾等才能進行二轉",
        "請問劍士一轉要多少力量");
var qanswers = Array(8, 10, 20, 25, 30, 35);
var party;
var preamble;
var stage2rects = Array(Rectangle(-770, -132, 28, 178), Rectangle(-733, -337, 26, 105), Rectangle(-601, -328, 29, 105), Rectangle(-495, -125, 24, 165));
var stage2combos = Array(Array(0, 1, 1, 1), Array(1, 0, 1, 1), Array(1, 1, 0, 1), Array(1, 1, 1, 0));
var stage3rects = Array(Rectangle(608, -180, 140, 50), Rectangle(791, -117, 140, 45), Rectangle(958, -180, 140, 50), Rectangle(876, -238, 140, 45), Rectangle(702, -238, 140, 45));
var stage3combos = Array(Array(0, 0, 1, 1, 1), Array(0, 1, 0, 1, 1), Array(0, 1, 1, 0, 1), Array(0, 1, 1, 1, 0), Array(1, 0, 0, 1, 1), Array(1, 0, 1, 0, 1), Array(1, 0, 1, 1, 0), Array(1, 1, 0, 0, 1), Array(1, 1, 0, 1, 0), Array(1, 1, 1, 0, 0));
var stage4rects = Array(Rectangle(910, -236, 35, 5), Rectangle(877, -184, 35, 5), Rectangle(946, -184, 35, 5), Rectangle(845, -132, 35, 5), Rectangle(910, -132, 35, 5), Rectangle(981, -132, 35, 5));
var stage4combos = Array(Array(0, 0, 0, 1, 1, 1), Array(0, 0, 1, 0, 1, 1), Array(0, 0, 1, 1, 0, 1), Array(0, 0, 1, 1, 1, 0), Array(0, 1, 0, 0, 1, 1), Array(0, 1, 0, 1, 0, 1), Array(0, 1, 0, 1, 1, 0), Array(0, 1, 1, 0, 0, 1), Array(0, 1, 1, 0, 1, 0), Array(0, 1, 1, 1, 0, 0), Array(1, 0, 0, 0, 1, 1), Array(1, 0, 0, 1, 0, 1), Array(1, 0, 0, 1, 1, 0), Array(1, 0, 1, 0, 0, 1), Array(1, 0, 1, 0, 1, 0), Array(1, 0, 1, 1, 0, 0), Array(1, 1, 0, 0, 0, 1), Array(1, 1, 0, 0, 1, 0), Array(1, 1, 0, 1, 0, 0), Array(1, 1, 1, 0, 0, 0));
var eye = 9300002;
var necki = 9300000;
var slime = 9300003;
var monsterIds = Array(eye, eye, eye, necki, necki, necki, necki, necki, necki, slime);
var prizeIdScroll = Array(2040502, 2040505, // Overall DEX and DEF
        2040802, // Gloves for DEX
        2040002, 2040402, 2040602);						// Helmet, Topwear and Bottomwear for DEF
var prizeIdUse = Array(2000001, 2000002, 2000003, 2000006, // Orange, White and Blue Potions and Mana Elixir
        2000004, 2022000, 2022003);						// Elixir, Pure Water and Unagi
var prizeQtyUse = Array(80, 80, 80, 50,
        5, 15, 15);
var prizeIdEquip = Array(1032004, 1032005, 1032009, // Level 20-25 Earrings
        1032006, 1032007, 1032010, // Level 30 Earrings
        1032002, // Level 35 Earring
        1002026, 1002089, 1002090);						// Bamboo Hats
var prizeIdEtc = Array(4010000, 4010001, 4010002, 4010003, // Mineral Ores
        4010004, 4010005, 4010006, // Mineral Ores
        4020000, 4020001, 4020002, 4020003, // Jewel Ores
        4020004, 4020005, 4020006, // Jewel Ores
        4020007, 4020008, 4003000);						// Diamond and Black Crystal Ores and Screws
var prizeQtyEtc = Array(15, 15, 15, 15,
        8, 8, 8,
        8, 8, 8, 8,
        8, 8, 8,
        3, 3, 30);

function start() {
    status = -1;
    mapId = cm.getMapId();
    if (mapId == 103000800)
        curMap = 1;
    else if (mapId == 103000801)
        curMap = 2;
    else if (mapId == 103000802)
        curMap = 3;
    else if (mapId == 103000803)
        curMap = 4;
    else if (mapId == 103000804)
        curMap = 5;
    playerStatus = cm.isLeader();
    preamble = null;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;

    if (curMap == 1) { // First Stage.
        if (playerStatus) { // Check if player is leader
            if (status == 0) {
                var eim = cm.getEventInstance();
                party = eim.getPlayers();
                preamble = eim.getProperty("leader1stpreamble");

                if (preamble == null) {
                    cm.sendNext("嗨，我是#p9020001# 歡迎來到墮落城市PQ 我需要隊員的通行證，請叫隊員收集完成後把卡片收集起來然後給我。");
                    eim.setProperty("leader1stpreamble", "done");
                    cm.dispose();
                } else { // Check how many they have compared to number of party members
                    // Check for stage completed
                    var complete = eim.getProperty(curMap.toString() + "stageclear");
                    if (complete != null) {
                        cm.sendNext("恭喜您過關 通往下一階段的門已開啟!");
                        cm.dispose();
                    } else {
                        var numpasses = party.size() - 1;
                        var strpasses = "#b" + numpasses.toString() + " 張通行證#k";
                        if (!cm.haveItem(4001008, numpasses)) {
                            cm.sendNext("我很抱歉我不能讓你過關，我需要: " + strpasses + " 交給我之後我就會讓你過關。");
                            cm.dispose();
                        } else {
                            cm.sendNext("你收集 " + strpasses + "! 恭喜過關。");
                            clear(1, eim, cm);
                            cm.givePartyExp(300, party);
                            cm.gainItem(4001008, -numpasses);
                            cm.dispose();
                            // TODO: Make the shiny thing flash
                        }
                    }
                }
            }
        } else { // 不是隊長
            var eim = cm.getChar().getEventInstance();
            pstring = "member1stpreamble" + cm.getChar().getId().toString();
            preamble = eim.getProperty(pstring);
            if (status == 0 && preamble == null) {
                var qstring = "member1st" + cm.getChar().getId().toString();
                var question = eim.getProperty(qstring);
                if (question == null) {
                    // Select a random question to ask the player.
                    var questionNum = Math.floor(Math.random() * questions.length);
                    eim.setProperty(qstring, questionNum.toString());
                }
                cm.sendNext("在這裡，你需要收集 #b優惠券#k 過擊敗鱷魚的數目相同的答案為單獨提出的問題。");
            } else if (status == 0) { // Otherwise, check for stage completed
                var complete = eim.getProperty(curMap.toString() + "stageclear");
                if (complete != null) {
//		    cm.sendNext("Please hurry on to the next stage, the portal opened!");
                    cm.dispose();
                } else {
                    // Reply to player correct/incorrect response to the question they have been asked
                    var qstring = "member1st" + cm.getChar().getId().toString();
                    var numcoupons = qanswers[parseInt(eim.getProperty(qstring))];
                    var qcorr = cm.haveItem(4001007, (numcoupons + 1));
                    var enough = false;
                    if (!qcorr) { // Not too many
                        qcorr = cm.haveItem(4001007, numcoupons);
                        if (qcorr) { // Just right
                            cm.sendNext("來，這是我答應你的 #b#t4001008##k. 快點拿去給你的隊長吧。");
                            cm.gainItem(4001007, -numcoupons);
                            cm.gainItem(4001008, 1);
                            enough = true;
                        }
                    }
                    if (!enough) {
						var question = parseInt(eim.getProperty(qstring));
                        cm.sendNext("我很抱歉，但是這是不正確的答案！請在您的給我的正確數量。\r\n題目:"+questions[question]);
                    }
                    cm.dispose();
                }
            } else if (status == 1) {
                if (preamble == null) {
                    var qstring = "member1st" + cm.getChar().getId().toString();
                    var question = parseInt(eim.getProperty(qstring));
                    cm.sendNextPrev(questions[question]);
                } else { // Shouldn't happen, if it does then just dispose
                    cm.dispose();
                }
            } else if (status == 2) { // Preamble completed
                eim.setProperty(pstring, "done");
                cm.dispose();
            } else { // Shouldn't happen, but still...
                eim.setProperty(pstring, "done"); // Just to be sure
                cm.dispose();
            }
        } // End first map scripts
    } else if (2 <= curMap && 4 >= curMap) {
        rectanglestages(cm);
    } else if (curMap == 5) { // Final stage
        var eim = cm.getChar().getEventInstance();
        var stage5done = eim.getProperty("5stageclear");
        if (stage5done == null) {
            if (playerStatus) { // Leader
                var passes = cm.haveItem(4001008, 10);
				if (cm.getMonsterCount(103000804) <= 0) {
                if (passes) {
                    // Clear stage
                    cm.sendNext("恭喜過關！");
                    party = eim.getPlayers();
                    cm.gainItem(4001008, -10);
                    clear(5, eim, cm);
                    cm.givePartyExp(6000, party);
                    cm.dispose();
                } else { // Not done yet
                    cm.sendNext("歡迎來到最終階段你只要把通行證收集起來交給我就行了！");
                }
				} else { // Not Kill Map Monster
					cm.sendOk("貌似還沒把地圖上的怪物清除乾淨");
				}
                cm.dispose();
            } else { // Members
                cm.sendNext("歡迎來到最終階段~現在你只要把所有的通行證交給隊長就行了！");
                cm.dispose();
            }
        } else { // Give rewards and warp to bonus
            if (status == 0) {
                cm.sendNext("真的很不可思議！");
            }
            if (status == 1) {
                getPrize(eim, cm);
                cm.dispose();
            }
        }
    } else { // No map found
        cm.sendNext("無效的地圖，請聯絡GM！");
        cm.dispose();
    }
}

function clear(stage, eim, cm) {
    eim.setProperty(stage.toString() + "stageclear", "true");

    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");

    var mf = eim.getMapFactory();
    map = mf.getMap(103000800 + stage);
    var nextStage = eim.getMapFactory().getMap(103000800 + stage);
    var portal = nextStage.getPortal("next00");
    if (portal != null) {
        portal.setScriptName("kpq" + (stage + 1).toString());
    }
}

function failstage(eim, cm) {
    cm.showEffect(true, "quest/party/wrong_kor");
    cm.playSound(true, "Party1/Failed");
}

function rectanglestages(cm) {
    // Debug makes these stages clear without being correct
    var debug = false;
    var eim = cm.getChar().getEventInstance();
    if (curMap == 2) {
        var nthtext = "2";
        var nthobj = "繩子";
        var nthverb = "掛";
        var nthpos = "掛在繩子太低";
        var curcombo = stage2combos;
        var currect = stage2rects;
        var objset = [0, 0, 0, 0];
    } else if (curMap == 3) {
        var nthtext = "3";
        var nthobj = "平台";
        var nthverb = "站";
        var nthpos = "站在太靠近邊緣";
        var curcombo = stage3combos;
        var currect = stage3rects;
        var objset = [0, 0, 0, 0, 0];
    } else if (curMap == 4) {
        var nthtext = "4";
        var nthobj = "酒桶";
        var nthverb = "站";
        var nthpos = "站在太靠近邊緣";
        var curcombo = stage4combos;
        var currect = stage4rects;
        var objset = [0, 0, 0, 0, 0, 0];
    }
    if (playerStatus) { // Check if player is leader
        if (status == 0) {
            // Check for preamble
            party = eim.getPlayers();
            preamble = eim.getProperty("leader" + nthtext + "preamble");
            if (preamble == null) {
                cm.sendNext("嗨，歡迎來到第 " + nthtext + " 階段. 在我旁邊，你會看到一些 " + nthobj + ", #b你需要三名隊員掛在上面猜我的答案，如果猜對就讓你過關，加油吧！ \r\n喔~對了不能#r" + nthpos + "不然會不能過關哦！");
                eim.setProperty("leader" + nthtext + "preamble", "done");
                var sequenceNum = Math.floor(Math.random() * curcombo.length);
                eim.setProperty("stage" + nthtext + "combo", sequenceNum.toString());
                cm.dispose();
            } else {
                // Otherwise, check for stage completed
                var complete = eim.getProperty(curMap.toString() + "stageclear");
                if (complete != null) {
                    var mapClear = curMap.toString() + "stageclear";
                    eim.setProperty(mapClear, "true"); // Just to be sure
//		    cm.sendNext("Please hurry on to the next stage, the portal opened!");
                    cm.dispose();
                } else { // Check for people on ropes and their positions
                    var totplayers = 0;
                    for (i = 0; i < objset.length; i++) {
                        for (j = 0; j < party.size(); j++) {
                            var present = currect[i].contains(party.get(j).getPosition());
                            if (present) {
                                objset[i] = objset[i] + 1;
                                totplayers = totplayers + 1;
                            }
                        }
                    }
                    // Compare to correct positions
                    // First, are there 3 players on the correct positions?
                    if (totplayers == 3 || debug) {
                        var combo = curcombo[parseInt(eim.getProperty("stage" + nthtext + "combo"))];
                        // Debug
                        // Combo = curtestcombo;
                        var testcombo = true;
                        for (i = 0; i < objset.length; i++) {
                            if (combo[i] != objset[i])
                                testcombo = false;
                        }
                        if (testcombo || debug) {
                            // Do clear
                            clear(curMap, eim, cm);
                            var exp = (Math.pow(2, curMap) * 50* 3);
                            cm.givePartyExp(exp, party);
                            cm.dispose();
                        } else { // Wrong
                            // Do wrong
                            failstage(eim, cm);
                            cm.dispose();
                        }
                    } else {
                        if (debug) {
                            var outstring = "Objects contain:"
                            for (i = 0; i < objset.length; i++) {
                                outstring += "\r\n" + (i + 1).toString() + ". " + objset[i].toString();
                            }
                            cm.sendNext(outstring);
                        } else {
                            cm.sendNext("嗨，歡迎來到第 " + nthtext + " 階段. 在我旁邊，你會看到一些 " + nthobj + ", #b你需要三名隊員掛在上面猜我的答案，如果猜對就讓你過關，加油吧！ \r\n喔~對了不能#r" + nthpos + "不然會不能過關哦！");
                        }
                        cm.dispose();
                    }
                }
            }
        } else {
            var complete = eim.getProperty(curMap.toString() + "stageclear");
            if (complete != null) {
                var target = eim.getMapInstance(103000800 + curMap);
                var targetPortal = target.getPortal("st00");
                cm.getChar().changeMap(target, targetPortal);
            }
            cm.dispose();
        }
    } else { // Not leader
        if (status == 0) {
            var complete = eim.getProperty(curMap.toString() + "stageclear");
            if (complete != null) {
                cm.dispose();
//		cm.sendNext("Please hurry on to the next stage, the portal opened!");
            } else {
//		cm.sendNext("Please have the party leader talk to me.");
                cm.dispose();
            }
        } else {
            var complete = eim.getProperty(curMap.toString() + "stageclear");
            if (complete != null) {
                var target = eim.getMapInstance(103000800 + curMap);
                var targetPortal = target.getPortal("st00");
                cm.getChar().changeMap(target, targetPortal);
            }
            cm.dispose();
        }
    }
}

function getPrize(eim, cm) {
    var itemSetSel = Math.random();
    var itemSet;
    var itemSetQty;
    var hasQty = false;
    if (itemSetSel < 0.3)
        itemSet = prizeIdScroll;
    else if (itemSetSel < 0.6)
        itemSet = prizeIdEquip;
    else if (itemSetSel < 0.9) {
        itemSet = prizeIdUse;
        itemSetQty = prizeQtyUse;
        hasQty = true;
    } else {
        itemSet = prizeIdEtc;
        itemSetQty = prizeQtyEtc;
        hasQty = true;
    }
    var sel = Math.floor(Math.random() * itemSet.length);
    var qty = 1;
    if (hasQty)
        qty = itemSetQty[sel];
    cm.gainItem(itemSet[sel], qty);
	cm.getPlayer().modifyCSPoints(2,50,true);
    cm.removeAll(4001007);
    cm.removeAll(4001008);
    cm.getPlayer().endPartyQuest(1201);
    cm.warp(103000805, "sp");
}

function Rectangle(x, y, length, width) {
    return new java.awt.Rectangle(x, y, length, width);
}