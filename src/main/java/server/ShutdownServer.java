package server;

import client.MapleCharacter;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.World.Alliance;
import handling.world.World.Broadcast;
import handling.world.World.Family;
import handling.world.World.Guild;
import tools.MaplePacketCreator;

import java.util.Set;

import static gui.QQMsgServer.sendMsgToQQGroup;

public class ShutdownServer implements Runnable {

    private static final ShutdownServer instance = new ShutdownServer();
    public static boolean running = false;
    public int mode = 0;

    public static ShutdownServer getInstance() {
        return instance;
    }

    public void shutdown() {
        run();

    }

    @Override
    public void run() {
        //保存频道雇佣
        System.out.println("开始保存游戏雇佣商人...");
        int p = 0;
        for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
            p++;
            cserv.closeAllMerchant();
        }
        System.out.println("游戏雇佣商人保存完成...");
        //保存玩家数据
        System.out.println("开始保存在线玩家数据A...");
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                p++;
                chr.saveToDB(true, true);
            }
        }
        System.out.println("保存在线所有玩家数据A完成...");
        //保存全部数据
        System.out.println("开始保存玩家数据B...");
        int ppl = 0;
        try {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr == null) {
                        continue;
                    }
                    ppl++;
                    chr.saveToDB(false, false);

                }
            }
        } catch (Exception e) {
        }
        System.out.println("保存所有玩家数据B完成...");
        System.out.println("开始切断所有在线玩家...");
        //断线玩家
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.getPlayerStorage().disconnectAll(true);
        }
        System.out.println("切断所有在线玩家成功...");
        //Timer
        Timer.WorldTimer.getInstance().stop();
        Timer.MapTimer.getInstance().stop();
        Timer.BuffTimer.getInstance().stop();
        Timer.CloneTimer.getInstance().stop();
        Timer.EventTimer.getInstance().stop();
        Timer.EtcTimer.getInstance().stop();

        //Merchant
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.closeAllMerchant();
        }

        try {
            //Guild
            Guild.save();
            //Alliance
            Alliance.save();
            //Family
            Family.save();
        } catch (Exception ex) {
        }

        Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, " 游戏服务器将关闭维护，请玩家安全下线..."));
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            try {
                cs.setServerMessage("游戏服务器将关闭维护，请玩家安全下线...");
            } catch (Exception ex) {
            }
        }
        Set<Integer> channels = ChannelServer.getAllInstance();

        for (Integer channel : channels) {
            try {
                ChannelServer cs = ChannelServer.getInstance(channel);
                cs.saveAll();
                cs.setFinishShutdown();
                cs.shutdown();
            } catch (Exception e) {
                System.out.println("频道" + channel + " 关闭错误.");
            }
        }

        try {
            LoginServer.shutdown();
            System.out.println("游戏登录线程关闭完成...");
        } catch (Exception e) {
        }

        try {
            CashShopServer.shutdown();
            System.out.println("游戏商城线程关闭完成...");
        } catch (Exception e) {
        }
        try {
            DatabaseConnection.closeAll();
        } catch (Exception e) {
        }
        //Timer.PingTimer.getInstance().stop();
        System.out.println("服务端事件关闭完成...");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("关闭服务端错误 - 2" + e);

        }
        System.out.println("服务端已经成功关闭");

        if (gui.Start.ConfigValuesMap.get("QQ机器人开关") == 0) {
            sendMsgToQQGroup("[服务端信息]:服务端关闭成功。");
        }
        //重启服务端();
        System.exit(0);
    }
}
