package gui;

import client.MapleCharacter;
import handling.channel.ChannelServer;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.MaplePacketCreator;

import java.awt.*;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;

public class 活动鱼来鱼往 {

    /**
     * <鱼来鱼往> 2230107
     */
    public static ScheduledFuture<?> 鱼来鱼往线程 = null;
    public static int 飞鱼 = 2230107;
    public static int 地图 = 230000000;

    public static void 鱼来鱼往() {
        if (鱼来鱼往线程 == null) {
            鱼来鱼往线程 = Timer.BuffTimer.getInstance().register(new Runnable() {
                @Override
                public void run() {
                    int 时 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    int 分 = Calendar.getInstance().get(Calendar.MINUTE);
                    int 星期 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    if (星期 > 1 && 星期 < 7) {
                        if (时 == 21 && 分 >= 50) {
                            关闭鱼来鱼往线程();
                        }
                    } else if (时 == 21 && 分 >= 40) {
                        关闭鱼来鱼往线程();
                    }
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                            if (chr == null) {
                                continue;
                            }
                            if (chr.getMapId() == 地图) {
                                if (chr.getClient().getChannel() == 2) {
                                    chr.鱼来鱼往++;
                                    if (chr.鱼来鱼往 > 0) {
                                        for (int i = 0; i <= gui.Start.ConfigValuesMap.get("飞鱼数量"); i++) {
                                            召唤飞鱼(chr.getPosition());
                                        }
                                        chr.getClient().sendPacket(MaplePacketCreator.sendHint("#b<躲避值>\r\n\r\n#e#r " + chr.鱼来鱼往 + "\r\n", 200, 6));
                                        if (gui.Start.ConfigValuesMap.get("飞鱼经验开关") == 0) {
                                            chr.gainExp(chr.鱼来鱼往 * gui.Start.ConfigValuesMap.get("飞鱼经验倍率"), true, true, false);
                                        }
                                        if (gui.Start.ConfigValuesMap.get("飞鱼点券开关") == 0) {
                                            chr.modifyCSPoints(1, chr.鱼来鱼往 * gui.Start.ConfigValuesMap.get("飞鱼点券倍率"), true);
                                        }
                                        if (gui.Start.ConfigValuesMap.get("飞鱼金币开关") == 0) {
                                            chr.gainMeso(chr.鱼来鱼往 * gui.Start.ConfigValuesMap.get("飞鱼金币倍率"), true, true, true);
                                        }
                                    } else {
                                        chr.dropMessage(5, "请躲避怪物的攻击。");
                                    }
                                } else {
                                    chr.dropMessage(5, "请在 2 频道参加 鱼来鱼往 活动。");
                                }
                            }
                        }
                    }
                }
            }, 1000 * 30);
        }
    }

    public static void 召唤飞鱼() {
        ChannelServer channelServer = ChannelServer.getInstance(2);
        MapleMonster mapleMonster = MapleLifeFactory.getMonster(飞鱼);
        MapleMap mapleMap = channelServer.getMapFactory().getMap(地图);
        mapleMonster.setPosition(new Point(255, 340));
        mapleMonster.setHp(1000 * 10000 * 10000);
        mapleMap.spawnMonster(mapleMonster, -2);

    }

    public static void 召唤飞鱼(Point a) {
        ChannelServer channelServer = ChannelServer.getInstance(2);
        MapleMonster mapleMonster = MapleLifeFactory.getMonster(飞鱼);
        MapleMap mapleMap = channelServer.getMapFactory().getMap(地图);
        mapleMonster.setPosition(a);
        mapleMonster.setHp(1000);
        mapleMonster.setExp(-100000000);
        mapleMap.spawnMonster(mapleMonster, -2);
    }

    public static void 清怪(int a) {
        ChannelServer channelServer = ChannelServer.getInstance(2);
        MapleMap mapleMap = channelServer.getMapFactory().getMap(a);
        mapleMap.resetFully();
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                if (chr == null) {
                    continue;
                }
                if (chr.getMapId() == a) {
                    chr.changeMap(a, 0);
                }
            }
        }
    }

    public static void 关闭鱼来鱼往线程() {
        if (鱼来鱼往线程 != null) {
            鱼来鱼往线程.cancel(false);
            鱼来鱼往线程 = null;
            清怪(地图);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr == null) {
                        continue;
                    }
                    chr.startMapEffect("[鱼来鱼往]: 本次鱼来鱼往活动已经结束。", 5120027);
                    chr.关闭鱼来鱼往线程();
                }
            }
            QQ通信.群通知("[鱼来鱼往]: 本次鱼来鱼往活动已经结束。");
        }
    }
}
