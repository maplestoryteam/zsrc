package gui;

import server.Timer;

import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author Administrator
 */
public class 冒险股票系统 {

    public static ScheduledFuture<?> 彩票线程 = null;
    public static Boolean 三 = false;
    public static Boolean 二 = false;
    public static Boolean 一 = false;
    public static Boolean 特 = false;

    public static void main(String[] args) {
        彩票线程();
    }

    /**
     * <23点>
     */
    public static void 彩票线程() {
        if (彩票线程 == null) {
            彩票线程 = Timer.BuffTimer.getInstance().register(new Runnable() {
                @Override
                public void run() {
                    System.err.println("1");
                }
            }, 1000 * 1);
        }
    }

}
