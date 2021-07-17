package gui;

import handling.world.MapleParty;
import handling.world.World;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import server.Timer;
import static tools.FileoutputUtil.CurrentReadable_Time;
import tools.MaplePacketCreator;

/**
 *
 * @author Administrator
 */
public class 活动每日彩票 {

    public static ScheduledFuture<?> 彩票线程 = null;
    public static Boolean 三 = false;
    public static Boolean 二 = false;
    public static Boolean 一 = false;
    public static Boolean 特 = false;

    public static void main(String args[]) {
        彩票线程();
    }

    /**
     * <23点>
     */
    public static void 彩票线程() {
        if (彩票线程 == null) {
            System.err.println("[服务端]" + CurrentReadable_Time() + " : [每日彩票] : 每日彩票已经开始摇奖。");
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[每日彩票] : 每日彩票已经开始摇奖，今天的大奖将会花落谁家呢。"));
            彩票线程 = Timer.BuffTimer.getInstance().register(new Runnable() {
                @Override
                public void run() {
                    int 时 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    int 分 = Calendar.getInstance().get(Calendar.MINUTE);
                    if (时 == 23 && 分 == 10 && 三 == false) {
                        三等奖();
                    } else if (时 == 23 && 分 == 11 && 二 == false) {
                        二等奖();
                    } else if (时 == 23 && 分 == 12 && 一 == false) {
                        一等奖();
                    } else if (时 == 23 && 分 == 13 && 特 == false) {
                        特等奖();
                    }
                    System.err.println("1");
                }
            }, 1000 * 1);
        }
    }

    public static void 三等奖() {
        double 随机 = Math.ceil(Math.random() * 200);
        随机 += 100;
        MapleParty.三等奖 = (int) 随机;
        三 = true;
    }

    public static void 二等奖() {
        double 随机 = Math.ceil(Math.random() * 200);
        随机 += 100;
        MapleParty.二等奖 = (int) 随机;
        二 = true;
    }

    public static void 一等奖() {
        double 随机 = Math.ceil(Math.random() * 200);
        随机 += 100;
        MapleParty.一等奖 = (int) 随机;
        一 = true;
    }

    public static void 特等奖() {
        double 随机 = Math.ceil(Math.random() * 200);
        随机 += 100;
        MapleParty.特等奖 = (int) 随机;
        特 = true;
        关闭彩票线程();
    }

    public static void 关闭彩票线程() {
        if (彩票线程 != null) {
            彩票线程.cancel(true);
            彩票线程 = null;

        }
    }

}
