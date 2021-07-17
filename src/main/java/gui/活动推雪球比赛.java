package gui;

import database.DatabaseConnection;
import static gui.QQMsgServer.sendMsgToQQGroup;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.World;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.ServerProperties;
import server.events.MapleEvent;
import server.events.MapleEventType;
import static tools.FileoutputUtil.CurrentReadable_Time;
import tools.MaplePacketCreator;

public class 活动推雪球比赛 {

    public static void 开始推雪球() {
        final int pind = (int) Math.ceil(Math.random() * Integer.parseInt(ServerProperties.getProperty("ZEV.Count")));
        final MapleEventType type = MapleEventType.getByString("雪球赛");
        final String msg = MapleEvent.scheduleEvent(type, ChannelServer.getInstance(pind));
        MapleParty.活动安排++;
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 60 * 3);
                    MapleEvent.setEvent(ChannelServer.getInstance(pind), true);
                } catch (InterruptedException e) {
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 60 * 60 * 1);
                    MapleParty.活动安排 = 0;
                    try {
                        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 10")) {
                            ps.executeUpdate();
                        }
                    } catch (SQLException ex) {
                    }
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[系统提醒] : 自助活动冷却已经重置，玩家们可以选择开启活动了。"));
                    sendMsgToQQGroup("[系统提醒] : 自助活动冷却已经重置，玩家们可以选择开启活动了。");
                } catch (InterruptedException e) {
                }
            }
        }.start();
        System.err.println("[服务端]" + CurrentReadable_Time() + " : 雪球赛线程启动");
    }

}
