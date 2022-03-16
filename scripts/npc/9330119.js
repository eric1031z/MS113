var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }
    if (status === -1) {
        cm.dispose();
    } else if (status === 0) {
        if (!cm.haveItem(5220000) || cm.getPlayer().getLevel() < 30) {
            cm.sendOk("你等級不足30或是沒有轉蛋券哦!");
            cm.dispose();
        } else {
            cm.sendYesNo("確定要使用 #b潮流轉蛋機#k嗎？\r\n祝你好運哦!");
        }
    } else if (status === 1) {
        var result = cm.gachapon(1);
        if (result !== -1) {
            cm.gainItem(5220000, -1);
            cm.sendOk("您已獲得 #v" + result + "##k.");
		} else {
            cm.sendOk("檢查一下背包是否已滿,或者所有設置的獎品已經發放完畢。");
        }
        cm.dispose();
    }
}