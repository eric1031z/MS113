var letters0 = Array("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
var answers0 = Array("APPLE", "BEAR", "CHEESE", "DOUGHNUT", "EARTH", "FLY", "GOLD", "HALLOWEEN", "ICE", "JEWELRY", "KING", "LOVE", "MOUNTAIN", "NOTE", "ORANGE", "POLICE", "QUIZ", "ROSE", "SNAKE", "TABLE", "UFO", "VIP", "WOOD", "XMAS", "YOUNG", "ZZZ");
var letters1 = Array(2010000, 2010001, 2010002, 2010003, 2010004, 2010005, 2010009, 2020000, 2020001, 2020002, 2020003, 2020004, 2020005, 2020007, 2020008, 2020009, 2020010, 2020012, 2020013, 2020014, 2020015, 2020016);
var answers1 = Array("APPLE", "MEAT", "EGG", "ORANGE", "LEMON", "HONEY", "GREENAPPLE", "SALAD", "FRIEDCHICKEN", "CAKE", "PIZZA", "HAMBURGER", "HOTDOG", "DRIEDSQUID", "FATSAUSAGE", "ORANGEJUICE", "GRAPEJUICE", "MELTINGCHEESE", "REINDEERMILK", "SUNRISEDEW", "SUNSETDEW", "CHEESECAKE");
var letters2 = Array("Lirin最喜歡什麼動物??", "金字塔法老王是什麼顏色??", "請給我的這些字母 [REINDEER]", "Which Valentine Rose is level 48?", "How much EXP does it take for level 1-2?", "Who exchanges Vote Points in FM?", "Who is the owner of this server (HINT: A _ _ _ _ _ _) ?", "What level does a Beginner become a Magician?", "What town does an Evan start in?", "What town is the home to the Black Wings?", "What are the Wild Hunters, Battle Mages, and Mechanics?", "What type of dragon is Mir?", "Who is Mir's ancestor?", "What weapon does the Aran use?", "Who is the job instructor for Mechanic?", "冒險家三轉需要用到哪一種石頭??", "哪一個NPC可以給你第一只神奇寶貝??", "狂暴獵人的豹英文??", "皇家騎士團幾等可以四轉??", "裝備淺能何者排名最大 特殊,罕見??", "皮卡丘頭上的雕像名字是??", "暗黑龍王靠近哪個地圖??", "在楓之谷世界中殘暴炎魔幾等才能挑戰??");
var answers2 = Array("WOLF", "YELLOW", "REINDEER", "BLUE", "FIFTEEN", "PHOENIX", "AWESOME", "EIGHT", "HENESYS", "EDELSTEIN", "RESISTANCE", "ONYX", "AFRIEN", "POLEARM", "CHECKY", "DARKCRYSTAL", "GAGA", "JAGUAR", "CYGNUS", "UNIQUE", "ARIEL", "LEAFRE", "FIFTY");

function init() {
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup(mapid) {

    var eim = em.newInstance("English" + mapid);
    eim.setInstanceMap(702090101 + (parseInt(mapid) * 100)).resetFully();
    eim.setInstanceMap(702090102 + (parseInt(mapid) * 100)).resetFully();
    eim.setInstanceMap(702090103 + (parseInt(mapid) * 100)).resetFully();
    
	eim.setProperty("mode", mapid);
	if (eim.getProperty("mode").equals("0")) {
		var ee = java.lang.Math.floor(java.lang.Math.random() * letters0.length);
		eim.setProperty("question", letters0[ee]);
		eim.setProperty("answer", answers0[ee]);
	} else if (eim.getProperty("mode").equals("1")) {
		var ee = java.lang.Math.floor(java.lang.Math.random() * letters1.length);
		eim.setProperty("question", letters1[ee]);
		eim.setProperty("answer", answers1[ee]);
	} else if (eim.getProperty("mode").equals("2")) {
		var ee = java.lang.Math.floor(java.lang.Math.random() * letters2.length);
		eim.setProperty("question", letters2[ee]);
		eim.setProperty("answer", answers2[ee]);
	}
    eim.startEventTimer(300000); //5 mins lol

    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
	if (eim.getProperty("mode").equals("0")) {
		player.sendEnglishQuiz("在英文村的 [" + eim.getProperty("question") + "] 怪物代表什麼??");
	} else if (eim.getProperty("mode").equals("1")) {
		player.sendEnglishQuiz("請給我這些字母 #i" + parseInt(eim.getProperty("question")) + "#.");
	} else if (eim.getProperty("mode").equals("2")) {
		player.sendEnglishQuiz(eim.getProperty("question"));
	}
}

function playerDead(eim, player) {
}

function changedMap(eim, player, mapid) {
    switch (mapid) {
	case 702090101: // 1st Stage
	case 702090102: // 2nd Stage
	case 702090103: // 3rd Stage
	case 702090201:
	case 702090202:
	case 702090203:
	case 702090301:
	case 702090302:
		case 702090303:
	    return; // Everything is fine
    }
    eim.unregisterPlayer(player);

    if (eim.disposeIfPlayerBelow(2, 702090400)) {
    }
}

function playerRevive(eim, player) {
}

function playerDisconnected(eim, player) {
    return -2;
}

function leftParty(eim, player) {			
    // If only 2 players are left, uncompletable
    if (!eim.disposeIfPlayerBelow(2, 702090400)) {
	playerExit(eim, player);
    }
}

function disbandParty(eim) {
    // Boot whole party and end
    eim.disposeIfPlayerBelow(100, 702090400);
}


function scheduledTimeout(eim) {
    clearPQ(eim);
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);

    var exit = eim.getMapFactory().getMap(702090400);
    player.changeMap(exit, exit.getPortal(0));
    if (eim.disposeIfPlayerBelow(2, 702090400)) {
    }
}

function clearPQ(eim) {
    // KPQ does nothing special with winners
    eim.disposeIfPlayerBelow(100, 702090400);
}

function allMonstersDead(eim) {
}

function cancelSchedule() {
}