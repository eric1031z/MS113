/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.world;

import client.MapleCharacter;
import constants.ServerConfig;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import server.Randomizer;
import server.Timer.MapTimer;
import tools.CheckCodeImageCreator;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.Pair;

/**
 *
 * @author pungin
 */
public class MapleAntiMacro {

    public static class MapleAntiMacroInfo {

        private final MapleCharacter source;
        private final int mode;
        private String code;
        private final long startTime;
        private ScheduledFuture<?> schedule;
        private int timesLeft = 2;

        MapleAntiMacroInfo(MapleCharacter from, int mode, String code, long time, ScheduledFuture<?> schedule) {
            source = from;
            this.mode = mode;
            this.code = code;
            startTime = time;
            this.schedule = schedule;
        }

        public MapleCharacter getSourcePlayer() {
            return source;
        }

        public int antiMode() {
            return mode;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setSchedule(ScheduledFuture<?> schedule) {
            cancelSchedule();
            this.schedule = schedule;
        }

        public void cancelSchedule() {
            if (schedule != null) {
                schedule.cancel(false);
            }
        }

        public int antiFailure() {
            return --timesLeft;
        }

        public int getTimesLeft() {
            return timesLeft - 1;
        }
    }

    private static final Map<String, MapleAntiMacroInfo> antiPlayers = new HashMap();
    private static final Map<String, Long> lastAntiTime = new HashMap();

    // ???????????????
    public final static int SYSTEM_ANTI = 0; // ??????????????????
    public final static int ITEM_ANTI = 1; // ????????????
    public final static int GM_SKILL_ANTI = 2; // ?????????????????????

    // ????????????????????????
    public final static int CAN_ANTI = 0; // ?????????
    public final static int NON_ATTACK = 1; // ???????????????
    public final static int ANTI_COOLING = 2; // ???????????????
    public final static int ANTI_NOW = 3; // ????????????

    public static int getCharacterState(MapleCharacter chr) {
        // ?????????????????????
        /*
        if (!chr.isAttacking) { TODO : ???????????????????????????
            return NON_ATTACK;
        }
         */

        // ??????????????????
        if (isCooling(chr.getName())) {
            return ANTI_COOLING;
        }

        // ???????????????????????????
        if (isAntiNow(chr.getName())) {
            return ANTI_NOW;
        }

        // ?????????
        return CAN_ANTI;
    }

    public static boolean isCooling(String name) {
        if (lastAntiTime.containsKey(name)) {
            if (System.currentTimeMillis() - lastAntiTime.get(name) < 30 * 60 * 1000) { // 30????????????
                return true;
            } else {
                lastAntiTime.remove(name);
            }
        }
        return false;
    }

    public static boolean isAntiNow(String name) {
        if (antiPlayers.containsKey(name) && System.currentTimeMillis() - antiPlayers.get(name).getStartTime() < 60 * 1000) { // 60???
            return true;
        }
        return false;
    }

    public static boolean startAnti(MapleCharacter chr, MapleCharacter victim, byte mode) {
        int antiState = MapleAntiMacro.getCharacterState(victim);
        switch (antiState) {
            case MapleAntiMacro.CAN_ANTI: {
                break;
            }
            case MapleAntiMacro.NON_ATTACK: {
                if (chr != null) {
                    chr.getClient().sendPacket(MaplePacketCreator.AntiMacro.nonAttack());
                }
                return false;
            }
            case MapleAntiMacro.ANTI_COOLING: {
                if (chr != null) {
                    chr.getClient().sendPacket(MaplePacketCreator.AntiMacro.alreadyPass());
                }
                return false;
            }
            case MapleAntiMacro.ANTI_NOW: {
                if (chr != null) {
                    chr.getClient().sendPacket(MaplePacketCreator.AntiMacro.antiMacroNow());
                }
                return false;
            }
            default: {
                System.out.println("????????????????????????????????????" + antiState);
                return false;
            }
        }

        Pair<String, File> checkCode = CheckCodeImageCreator.createCheckCode(Randomizer.nextInt(100) > 80);
        MapleAntiMacroInfo ami = new MapleAntiMacroInfo(chr, mode, checkCode.getLeft(), System.currentTimeMillis(), MapTimer.getInstance().schedule(() -> {
            if (antiPlayers.containsKey(victim.getName())) {
                antiFailure(victim);
            }
        }, 60 * 1000));
        antiPlayers.put(victim.getName(), ami);
        victim.getClient().sendPacket(MaplePacketCreator.AntiMacro.getImage(mode, checkCode.getRight(), ami.getTimesLeft()));
        checkCode.getRight().delete();
        if (chr != null) {
            chr.getClient().sendPacket(MaplePacketCreator.AntiMacro.antiMsg(mode, victim.getName()));
        }
        return true;
    }

    public static void antiSuccess(MapleCharacter victim) {
        MapleAntiMacroInfo ami = null;
        if (antiPlayers.containsKey(victim.getName())) {
            ami = antiPlayers.get(victim.getName());
            if (ami.antiMode() == ITEM_ANTI) {
                victim.gainMeso(5000, true);
            }
            MapleCharacter chr = ami.getSourcePlayer();
            if (chr != null) {
                chr.getClient().sendPacket(MaplePacketCreator.AntiMacro.successMsg(2, victim.getName()));
            }
        }
        victim.setAntiMacroFailureTimes(0);
        victim.getClient().sendPacket(MaplePacketCreator.AntiMacro.success(ami == null ? SYSTEM_ANTI : ami.antiMode()));
        stopAnti(victim.getName());
        lastAntiTime.put(victim.getName(), System.currentTimeMillis());
    }

    public static void antiFailure(MapleCharacter victim) {
        MapleAntiMacroInfo ami = null;
        if (antiPlayers.containsKey(victim.getName())) {
            ami = antiPlayers.get(victim.getName());
            MapleCharacter chr = ami.getSourcePlayer();
            if (chr != null && ami.antiMode() == GM_SKILL_ANTI) {
                chr.getClient().sendPacket(MaplePacketCreator.AntiMacro.failureScreenshot(victim.getName()));
            }
        }
        if (victim.addAntiMacroFailureTimes() < 5) {
            victim.changeMap(victim.getMap().getReturnMap().getId());
            victim.getClient().sendPacket(MaplePacketCreator.AntiMacro.failure(ami == null ? SYSTEM_ANTI : ami.antiMode()));
        } else {
            victim.setAntiMacroFailureTimes(0);
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 7);
            victim.tempban("?????????????????????5??????", cal, 1, false);
            victim.getClient().getSession().close();
            victim.getClient().disconnect(true, false);
            if (ServerConfig.LOG_DC) {
                FileoutputUtil.logToFile("logs/data/DC.txt", "\r\n???????????????????????????????????????????????????: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            String msg = "[GM ??????] " + victim.getName() + "  ???????????????????????????5??????????????????????????????";
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.getItemNotice(msg));

        }
        stopAnti(victim.getName());
    }

    public static void stopAnti(String name) {
        if (antiPlayers.containsKey(name)) {
            antiPlayers.get(name).cancelSchedule();
        }
        antiPlayers.remove(name);
    }

    public static void antiReduce(MapleCharacter victim) {
        if (antiPlayers.containsKey(victim.getName())) {
            MapleAntiMacroInfo ami = antiPlayers.get(victim.getName());
            if (ami.antiFailure() > 0) {
                refreshCode(victim);
            } else {
                antiFailure(victim);
            }
        }
    }

    public static boolean verifyCode(String name, String code) {
        if (!antiPlayers.containsKey(name)) {
            return false;
        }
        return antiPlayers.get(name).getCode().equalsIgnoreCase(code);
    }

    public static void refreshCode(MapleCharacter victim) {
        if (antiPlayers.containsKey(victim.getName())) {
            MapleAntiMacroInfo ami = antiPlayers.get(victim.getName());
            Pair<String, File> checkCode = CheckCodeImageCreator.createCheckCode(Randomizer.nextInt(100) > 80);
            ami.setCode(checkCode.getLeft());
            ami.setSchedule(MapTimer.getInstance().schedule(() -> {
                if (antiPlayers.containsKey(victim.getName())) {
                    antiFailure(victim);
                }
            }, 60 * 1000));
            victim.getClient().sendPacket(MaplePacketCreator.AntiMacro.getImage((byte) ami.antiMode(), checkCode.getRight(), ami.getTimesLeft()));
            checkCode.getRight().delete();
        }
    }
}
