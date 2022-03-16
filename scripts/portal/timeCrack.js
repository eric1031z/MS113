function enter(pi) {
	if (pi.getQuestStatus(31180) > 0){
        pi.warp(272030000, 0);
	}else{
	   pi.warp(272000100, 0);
	   pi.playPortalSE();
	}
	return;
}