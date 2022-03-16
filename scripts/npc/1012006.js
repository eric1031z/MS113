﻿/* Author: Xterminator
	NPC Name: 		Trainer Bartos
	Map(s): 		Victoria Road : Pet-Walking Road (100000202)
	Description: 		Pet Trainer
*/
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == 0 && mode == 0) {
	cm.dispose();
	return;
    } else if (status >= 1 && mode == 0) {
	cm.sendNext("需要的時候可以來找我。");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	cm.sendSimple("你想要跟我談什麼？？\r\n#L0##b我想要訓練寵物。#l\r\n#L1#我想要學習三個寵物的技能。#k#l");
    } else if (status == 1) {
	if (selection == 0) {
	    if (cm.haveItem(4031035)) {
		cm.sendNext("拿到這一封信，跳躍過那些障礙把這封信給我弟弟他會給你獎勵...");
		cm.dispose();
	    } else {
		cm.sendYesNo("這是在路上，你可以去與你的寵物散步。你可以走動的，或者你可以訓練你的寵物要經過這裡的障礙。如果你不是太密切的與您的寵物然而，這可能會出現問題，他不會聽從你的命令一樣多......那麼，你有什麼感想？想培養你的寵物？");
	    }
	} else {
	    cm.sendOk("嘿，你肯定見過 #b三個寵物的技能#k。");
	    cm.dispose();
	}
    } else if (status == 2) {
	cm.gainItem(4031035, 1);
	cm.sendNext("好運。");
	cm.dispose();
    }
}