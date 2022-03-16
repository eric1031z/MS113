function enter(pi) {
    var mapid = 0;
    var portal = 0;
    switch (pi.getPlayer().getMapId()) {
        case 931050410:
            mapid = 102040600;
            portal = 1;
            break;
		case 931050411:
            mapid = 200080000;
            portal = 1;
            break;			
		case 931050423:
            mapid = 211040000;
            portal = 1;
            break;		
        case 931050412:
            mapid = 220011000;
            portal = 1;
            break;		
        case 931050413:
            mapid = 220040200;
            portal = 1;
            break;	 	
		case 931050414:
            mapid = 221040400;
            portal = 1;
            break;		
		case 931050415:
            mapid = 260010201;
            portal = 1;
            break;	
		case 931050417:
            mapid = 261020500;
            portal = 1;
            break;		
		case 931050418:
            mapid = 251010500;
            portal = 1;
            break;		
		case 931050419:
            mapid = 240010200;
            portal = 1;
            break;	
		case 931050420:
            mapid = 749080121;
            portal = 1;
            break;	
		case 931050421:
            mapid = 240010500;
            portal = 1;
            break;		
		case 931050422:
            mapid = 240020200;
            portal = 1;
            break;			
    }
    if (mapid != 0) {
        pi.warp(mapid, portal);
    }
    return true;
}
