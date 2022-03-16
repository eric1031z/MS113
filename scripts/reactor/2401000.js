function act() {
	if(rm.getPlayer().getClient().getChannel() == 13 || rm.getPlayer().getClient().getChannel() == 14 || rm.getPlayer().getClient().getChannel() == 15 ){
    rm.changeMusic("Bgm14/HonTale");
    rm.spawnMonster(8810026, 71, 260);
    rm.mapMessage("隨著一聲怒吼，闇黑龍王出現了。");
	}
	if(rm.getPlayer().getClient().getChannel() != 13 && rm.getPlayer().getClient().getChannel() != 14 && rm.getPlayer().getClient().getChannel() != 15 ){
	rm.spawnMonster(100100, 71, 260);
	rm.mapMessage("阿你是想換頻偷打喔??");
	}
	//rm.scheduleWarp(43200, 240000000);
/*	if (!rm.getPlayer().isGM()) {
		rm.getMap().startSpeedRun();
	}*/
}