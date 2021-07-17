package gui;

import handling.channel.ChannelServer;
import java.awt.Point;
import java.util.concurrent.ScheduledFuture;
import server.Timer;
import server.maps.MapleMap;
import java.util.Arrays;
import java.util.Scanner;

/**
 * <活动
 * 活动开始后，
 * 射手村
 * 100000000
 *
 * 射手村民宅
 * 100000001
 *
 * 花蘑菇
 * 100000002
 *
 * 铁甲猪公园Ⅰ
 * 100000003
 *
 * 铁甲猪公园Ⅱ
 * 100000004
 *
 * 铁甲猪公园Ⅲ
 * 100000005
 *
 * 猪猪公园休息地方
 * 100000006
 *
 * 射手村集市
 * 100000100
 *
 * 射手村武器店
 * 100000101
 *
 * 射手村杂货店
 * 100000102
 *
 * 射手村整容院
 * 100000103
 *
 * 射手村美发店
 * 100000104
 *
 * 射手村护肤中心
 * 100000105
 *
 * 射手村公园
 * 100000200
 *
 * 弓箭手培训中心
 * 100000201
 *
 * 射手村游戏中心
 * 100000203
 *
 * 弓箭手的殿堂
 * 100000204
 * >
 */
public class 活动绝地求生 {

    public static ScheduledFuture<?> 绝地求生线程 = null;
    public static ScheduledFuture<?> 物品掉落线程 = null;
    public static ScheduledFuture<?> 地区刷新线程 = null;
    public static int 地图安全1 = 0;
    public static int 地图安全2 = 0;
    public static int 地图安全3 = 0;
    public static int 地图安全4 = 0;
    public static int 地图安全5 = 0;
    public static int 地图安全6 = 0;
    public static int 地图安全7 = 0;
    public static int 地图安全8 = 0;
    public static int 地图安全9 = 0;
    public static int 地图安全10 = 0;
    public static int 地图安全11 = 0;
    public static int 地图安全12 = 0;
    public static int 地图安全13 = 0;
    public static int 地图安全14 = 0;
    public static int 地图安全15 = 0;
    public static int 地图安全16 = 0;
    public static int 地图安全17 = 0;
    public static int 地图1 = 100000000;
    public static int 地图2 = 100000001;
    public static int 地图3 = 100000002;
    public static int 地图4 = 100000003;
    public static int 地图5 = 100000004;
    public static int 地图6 = 100000005;
    public static int 地图7 = 100000006;
    public static int 地图8 = 100000100;
    public static int 地图9 = 100000101;
    public static int 地图10 = 100000102;
    public static int 地图11 = 100000103;
    public static int 地图12 = 100000104;
    public static int 地图13 = 100000105;
    public static int 地图14 = 100000200;
    public static int 地图15 = 100000201;
    public static int 地图16 = 100000203;
    public static int 地图17 = 100000204;
    public static int 预设地图1 = 0;
    public static int 预设地图2 = 0;
    public static int 预设地图3 = 0;
    public static int 预设地图4 = 0;
    public static int 预设地图5 = 0;
    public static int 预设地图6 = 0;
    public static int 预设地图7 = 0;
    public static int 预设地图8 = 0;
    public static int 预设地图9 = 0;
    public static int 预设地图10 = 0;
    public static int 预设地图11 = 0;
    public static int 预设地图12 = 0;
    public static int 预设地图13 = 0;
    public static int 预设地图14 = 0;
    public static int 预设地图15 = 0;
    public static int 预设地图16 = 0;
    public static int 预设地图17 = 0;

    public static void 预设活动地图() {
        for (int i = 1; i < 17; i++) {
           
        }
    }

    /**
     * * <绝地求生活动开始后
     * ，每30秒刷新一次地图资源
     *
     * >continue;
     */
    public static void 地区刷新线程() {
        if (地区刷新线程 == null) {
            地区刷新线程 = Timer.BuffTimer.getInstance().register(new Runnable() {
                @Override
                public void run() {
                    double 概率 = Math.ceil(Math.random() * 100);
                    if (概率 == 1) {
                        地图1++;
                        地图2++;
                    }
                }
            }, 1000 * 60 * 2);
        }
    }

    /**
     * <绝地求生活动开始后，每30秒刷新一次地图资源>
     */
    public static void 物品掉落线程() {
        if (物品掉落线程 == null) {
            物品掉落线程 = Timer.BuffTimer.getInstance().register(new Runnable() {
                @Override
                public void run() {
                    物品掉落(100000000, 2000005, -586, 116, 20);

                    物品掉落(100000000, 2000005, -160, 274, 20);
                }
            }, 1000 * 30);
        }
    }

    /**
     * <绝地求生活动开始后，3分钟让玩家自己选择自己的位置>
     */
    public static void 绝地求生预备() {

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 60 * 3);
                    绝地求生线程();
                } catch (InterruptedException e) {
                }
            }
        }.start();

    }

    /**
     * <绝地求生活动开始，持续运行线程>
     */
    public static void 绝地求生线程() {
        if (绝地求生线程 == null) {
            绝地求生线程 = Timer.BuffTimer.getInstance().register(new Runnable() {
                @Override
                public void run() {

                }
            }, 1000 * 5);
        }
    }

    /**
     * <掉落物品> @param a
     *
     * @param b
     * @param x
     * @param y
     * @param c
     */
    public static void 物品掉落(int a, int b, int x, int y, int c) {
        double 概率 = Math.ceil(Math.random() * 100);
        if (概率 <= c) {
            ChannelServer channelServer = ChannelServer.getInstance(2);
            MapleMap mapleMap = channelServer.getMapFactory().getMap(a);
            mapleMap.spawnAutoDrop2(b, new Point(x, y));
        }
    }
}
