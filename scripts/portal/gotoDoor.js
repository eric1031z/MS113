function enter(pi) {
	if (pi.getQuestStatus(31178) == 1){
		pi.resetMap(272010200);
        pi.warp(272010200, 0);
		return;
	}else{
	   pi.warp(272020000, 0);
	   return;
	}
}