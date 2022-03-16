var status = -1;
var ch = 1;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else if (mode == 0) {
		status--;
	} else {
		cm.dispose();
		return;
	}
	if (!cm.getPlayer().isGM()) {
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendGetNumber("頻道", 1, 1, 20);
	} else if (status == 1) {
		ch = selection;
		cm.sendGetNumber("地圖", 910000000, 1, 999999999);

	} else if (status == 2) {
		var map = Packages.handling.channel.ChannelServer.getInstance(ch).getMapFactory().getMap(selection);
		msg = "";
		var chars = map.getCharacters();
		for (var i = 0; i < chars.size(); i++)
			msg += chars[i].getId() + " " + chars[i].getName() + "\r\n";
		cm.sendSimple(msg);
		cm.dispose();
	}
}
