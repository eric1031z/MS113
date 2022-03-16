var pop = 2;

function enter(pi) {
    if (!pi.getPlayer().isGM() && pi.getPlayer().getClient().getChannel() != 14 && pi.getPlayer().getClient().getChannel() != 15) {
        pi.playerMessage(5, "拉圖斯只能在頻道14和15 打而已。");
        return false;
    }
	
    if (pi.haveItem(4031870)) {
        pi.warp(922020300, 0);
        return true;
    }
	
    if (pi.getParty() == null) {
        pi.playerMessage("你是邊緣人？");
        return false;
    }
    if (!pi.isLeader()) {
        pi.sendOk("必須由隊長來發號施令。");
        return false;
    }

    var members = pi.getParty().getMembers();
    var next = true;
    var it = members.iterator();

    while (it.hasNext()) {
        var cPlayer = it.next();
        var chr = pi.getClient().getChannelServer().getPlayerStorage().getCharacterById(cPlayer.getId());

        if (cPlayer.getMapid() !== pi.getMapId()) {
            next = false;
            pi.playerMessage(cPlayer.getName() + " 不在這裡");
        }

        if (!chr.haveItem(4031172, 1)) {
            next = false;
            pi.playerMessage(cPlayer.getName() + " 這位害群之馬沒有玩具獎牌");
        }
		
		if (chr.getBossLog("pop") >= 2) {
			next = false;
			pi.playerMessage(cPlayer.getName() + " 已經偷打完了");
		}
    }
    if (!next) {
		pi.playerMessage(5, "不明的力量無法進入。");
        return false;
    }

    if (pi.getPlayerCount(220080001) > 0) { // Papu Map
		pi.playerMessage(5, "裡面的戰鬥已經開始，請稍後再嘗試。");
        return false;
	}
		
	var papuMap = pi.getMap(220080001);
	papuMap.resetFully();
	
    members = pi.getParty().getMembers();
    it = members.iterator();
    while (it.hasNext()) {
        cPlayer = it.next();
        chr = pi.getClient().getChannelServer().getPlayerStorage().getCharacterById(cPlayer.getId());
		chr.setBossLog("pop"); 
    }
	pi.playPortalSE();
	pi.warpParty(220080001, "st00");
	return true;
}
