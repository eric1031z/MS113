/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package handling.login;

import client.MapleClient;
import constants.ServerConfig;
import handling.MapleServerHandler;
import java.util.HashMap;
import java.util.Map;

import handling.mina.ServerConnection;
import java.util.Collection;
import java.util.WeakHashMap;

import server.ServerProperties;

public class LoginServer {

    private static final Map<Integer, Long> SelectCharTime = new WeakHashMap<>();
    private static final Map<Integer, String> LoginKey = new HashMap<>();
    private static final Map<Integer, Long> ChangeChannelTime = new HashMap<>();
    private static final Map<Integer, Long> EnterGameTime = new HashMap<>();
    private static Map<Integer, Integer> load = new HashMap<>();
    private static int usersOn = 0;
    public static short port = 8484;
    private static boolean finishedShutdown = true;
    private static AccountStorage clients;
    private static ServerConnection acceptor;

    public static final void addChannel(final int channel) {
        load.put(channel, 0);
    }

    public static final void removeChannel(final int channel) {
        load.remove(channel);
    }

    public static final void setup() {
        port = Short.parseShort(ServerProperties.getProperty("server.settings.login.port"));
        acceptor = new ServerConnection(ServerConfig.IP, port, 0, MapleServerHandler.LOGIN_SERVER);
        acceptor.run();
        System.out.println("\n【登入伺服器】  - 監聽端口: " + Short.toString(port) + " \n");
    }

    public static final void shutdown() {
        if (finishedShutdown) {
            System.out.println("【登入伺服器】 已經關閉了...無法執行此動作");
            return;
        }
        System.out.println("【登入伺服器】 關閉中...");
        acceptor.close();
        System.out.println("【登入伺服器】 關閉完畢...");
        finishedShutdown = true; //nothing. lol
    }

    public static final String getServerName() {
        return ServerConfig.SERVER_NAME;
    }

    public static final Map<Integer, Integer> getLoad() {
        return load;
    }

    public static void setLoad(final Map<Integer, Integer> load_, final int usersOn_) {
        load = load_;
        usersOn = usersOn_;
    }

    public static final int getUsersOn() {
        return usersOn;
    }

    public static final boolean isShutdown() {
        return finishedShutdown;
    }

    public static final void setOn() {
        finishedShutdown = false;
    }

    public static boolean getAutoReg() {
        return ServerConfig.AUTO_REGISTER;
    }

    public static void setAutoReg(boolean x) {
        ServerConfig.AUTO_REGISTER = x;
    }

    public static void forceRemoveClient(MapleClient client, boolean remove) {
        Collection<MapleClient> cls = getClientStorage().getAllClientsThreadSafe();
        for (MapleClient c : cls) {
            if (c == null) {
                continue;
            }
            if (c.getAccID() == client.getAccID() || c == client) {
                if (c != client) {
                    c.unLockDisconnect();
                }
                if (remove) {
                    removeClient(c);
                }
            }
        }
    }

    public static void forceRemoveClient(MapleClient client) {
        forceRemoveClient(client, true);
    }

    public static AccountStorage getClientStorage() {
        if (clients == null) {
            clients = new AccountStorage();
        }
        return clients;
    }

    public static final void addClient(final MapleClient c) {
        getClientStorage().registerAccount(c);
    }

    public static final void removeClient(final MapleClient c) {
        getClientStorage().deregisterAccount(c);
    }

    public static boolean CanLoginKey(MapleClient c, String key) {
        if (LoginKey.get(c.getAccID()) == null) {
            return true;
        }
        if (LoginKey.containsValue(key)) {
            if (LoginKey.get(c.getAccID()).equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean removeLoginKey(MapleClient c) {
        LoginKey.remove(c.getAccID());
        return true;
    }

    public static boolean addLoginKey(MapleClient c, String key) {
        LoginKey.put(c.getAccID(), key);
        return true;
    }

    public static String getLoginKey(MapleClient c) {
        return LoginKey.get(c.getAccID());
    }

    public static boolean CheckSelectChar(int accid) {
        long lastTime = System.currentTimeMillis();
        if (SelectCharTime.containsKey(accid)) {
            long lastSelectCharTime = SelectCharTime.get(accid);
            if (lastSelectCharTime + 3000 > lastTime) {
                return false;
            }
            SelectCharTime.remove(accid);
        } else {
            SelectCharTime.put(accid, lastTime);
        }
        return true;
    }

    public static long getLoginAgainTime(int accid) {
        return ChangeChannelTime.get(accid);
    }

    public static void addLoginAgainTime(int accid) {
        ChangeChannelTime.put(accid, System.currentTimeMillis());
    }

    public static boolean canLoginAgain(int accid) {
        long lastTime = System.currentTimeMillis();
        if (ChangeChannelTime.containsKey(accid)) {
            long lastSelectCharTime = ChangeChannelTime.get(accid);
            if (lastSelectCharTime + 40 * 1000 > lastTime) {
                return false;
            }
        }
        return true;
    }

    public static long getEnterGameAgainTime(int accid) {
        return EnterGameTime.get(accid);
    }

    public static void addEnterGameAgainTime(int accid) {
        EnterGameTime.put(accid, System.currentTimeMillis());
    }

    public static boolean canEnterGameAgain(int accid) {
        long lastTime = System.currentTimeMillis();
        if (EnterGameTime.containsKey(accid)) {
            long lastSelectCharTime = EnterGameTime.get(accid);
            if (lastSelectCharTime + 60 * 1000 > lastTime) {
                return false;
            }
        }
        return true;
    }
}
