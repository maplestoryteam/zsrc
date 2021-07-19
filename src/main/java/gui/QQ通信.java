package gui;

import handling.world.MapleParty;
import server.ServerProperties;

import static gui.QQMsgServer.sendMsg;
import static gui.QQMsgServer.sendMsgToQQGroup;
import static tools.FileoutputUtil.CurrentReadable_Time;

public class QQ通信 {

    /**
     * <实现信息出数到QQ，提醒主人>
     * 专用的通信线程，服务端出现问题第一时间通知主人和作者
     */
    public static String Sammy = "513251863";
    public static String 主人1 = ServerProperties.getProperty("QQ1");
    public static String 主人2 = ServerProperties.getProperty("QQ2");
    public static String 冒险岛 = MapleParty.开服名字;

    public static void 通信(String a) {
        /**
         * <通知小Z>
         */
        sendMsg("[" + 冒险岛 + "]\r\n" + "Time:" + CurrentReadable_Time() + "\r\n" + a, Sammy);
        /**
         * <通知1号主人，并且1号主人不是小z>
         */
        if (!"71447500".equals(ServerProperties.getProperty("ZEV.QQ1"))) {
            sendMsg("[" + 冒险岛 + "]\r\n" + "Time:" + CurrentReadable_Time() + "\r\n" + a, 主人1);
        }
        /**
         * <通知2号主人，并且2号主人不是小z，并且2号和1号主人不一样，避免通知2次>
         */
        if (主人1 == null ? 主人2 != null : !主人1.equals(主人2)) {
            sendMsg("[" + 冒险岛 + "]\r\n" + "Time:" + CurrentReadable_Time() + "\r\n" + a, 主人2);
        }
    }

    public static void 通信(String a, String b) {
        sendMsg(a, b);
    }

    public static void OX答题(String a) {
        sendMsgToQQGroup(a);
    }
    
     public static void 群通知(String a) {
        sendMsgToQQGroup(a);
    }

}
