function enter(pi) {
    var returnMap = pi.getSavedLocation("MULUNG_TC");
    pi.clearSavedLocation("MULUNG_TC");
    if(returnMap < 0)
    returnMap = 100000000;
    var target = pi.getMap(returnMap);
    var portal = target.getPortal("GHousingIn00");
    if (portal == null) {
        portal = target.getPortal(0);
    }
    if (pi.getMapId() != target) {
        pi.playPortalSE();
        pi.getPlayer().changeMap(target, portal);
    }
}