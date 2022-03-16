package server;

import client.MapleCharacter;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.World;
import java.util.Set;
import server.Timer.*;

public class ShutdownServer implements Runnable, ShutdownServerMBean {

    private static final ShutdownServer instance = new ShutdownServer();
    public static boolean running = false;

    public static ShutdownServer getInstance() {
        return instance;
    }

    @Override
    public void run() {
        synchronized (this) {
            if (running) { //Run once!
                return;
            }
            running = true;
        }
        World.isShutDown = true;
        EventTimer.getInstance().stop();
        WorldTimer.getInstance().stop();
        MapTimer.getInstance().stop();
        MobTimer.getInstance().stop();
        BuffTimer.getInstance().stop();
        CloneTimer.getInstance().stop();
        EtcTimer.getInstance().stop();
        System.out.println("Timer 關閉完成");

        int ret = 0;
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            ret += cserv.closeAllMerchant();
        }
        System.out.println("共儲存了 " + ret + " 個精靈商人");
        ret = 0;
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            ret += cserv.closeAllPlayerShop();
        }
        System.out.println("共儲存了 " + ret + " 個個人執照商店");

        World.Guild.save();
        System.out.println("公會資料儲存完畢");

        World.Alliance.save();
        System.out.println("聯盟資料儲存完畢");

        World.Family.save();
        System.out.println("家族資料儲存完畢");

        System.out.println("正在將所有玩家下線中...");
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            try {
                for (final MapleCharacter chr : cserv.getPlayerStorage().getAllCharactersThreadSafe()) {
                    chr.getClient().getSession().close();
                }
            } catch (Exception ex) {
            }
        }

        try {
            for (final MapleCharacter chr : CashShopServer.getPlayerStorage().getAllCharactersThreadSafe()) {
                try {
                    chr.getClient().getSession().close();
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {
        }

        Set<Integer> channels = ChannelServer.getAllChannels();
        for (Integer channel : channels) {
            try {
                ChannelServer cs = ChannelServer.getInstance(channel);
                //  cs.saveAll();
                cs.setPrepareShutdown();
                cs.shutdown();
            } catch (Exception e) {
                System.out.println("頻道" + String.valueOf(channel) + " 關閉失敗.");
            }
        }

        try {
            LoginServer.shutdown();
            System.out.println("登陸伺服器關閉完成.");
        } catch (Exception e) {
            System.out.println("登陸伺服器關閉失敗");
        }
        try {
            CashShopServer.shutdown();
            System.out.println("購物商城伺服器關閉完成.");
        } catch (Exception e) {
            System.out.println("購物商城伺服器關閉失敗");
        }

        try {
            DatabaseConnection.closeAll();
            System.out.println("資料庫清除連線完成");
        } catch (Exception e) {
            System.out.println("資料庫清除連線失敗");
        }
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//
//        }
//        System.exit(0);
    }

    @Override
    public void shutdown() {
        this.run();
    }
}
