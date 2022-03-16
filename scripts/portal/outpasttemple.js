function enter(pi) {
	if (pi.getQuestStatus(31178) == 2){
		//pi.resetMap(272000000);
        pi.warp(272000000, 0);
		return;
	}else{
	   pi.playerMessage(5, "....?");
	}
    return true;
}