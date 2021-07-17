package gui;

import client.MapleCharacter;
import database.DatabaseConnection;
import static gui.QQ通信.OX答题;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import scripting.NPCScriptManager;
import server.Timer;
import static tools.FileoutputUtil.CurrentReadable_Time;

/**
 *
 * @author Administrator
 */
public class 活动OX答题 {

    public static ScheduledFuture<?> OX答题线程 = null;
    public static String 题目 = "";
    public static String 答案 = "";
    public static int 题 = 1;
    public static Boolean 调试 = false;//true//false
    public static Boolean x = false;

    /**
     * <
     *8:00 发出通知OX答题10分后开始，*启动OX答题主线程*延迟10分钟启动OX出题线程
     * >
     */
    public static void OX答题线程() {
        if (OX答题线程 == null) {
            OX答题线程 = Timer.BuffTimer.getInstance().register(new Runnable() {
                @Override
                public void run() {
                    int 时 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    int 分 = Calendar.getInstance().get(Calendar.MINUTE);
                    int 星期 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    if (调试 == true) {
                        System.out.println("[服务端]" + CurrentReadable_Time() + " : OX答题线程运行中·/ " + 时 + " / " + 分 + "/  " + x + "");
                    }
                    switch (题) {
                        case 1:
                            if (时 == 20 && 分 == 10 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 11 && x == true) {
                                判断答案();
                            }
                            break;
                        case 2:
                            if (时 == 20 && 分 == 12 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 13 && x == true) {
                                判断答案();
                            }
                            break;
                        case 3:
                            if (时 == 20 && 分 == 14 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 15 && x == true) {
                                判断答案();
                            }
                            break;
                        case 4:
                            if (时 == 20 && 分 == 16 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 17 && x == true) {
                                判断答案();
                            }
                            break;
                        case 5:
                            if (时 == 20 && 分 == 18 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 19 && x == true) {
                                判断答案();
                            }
                            break;
                        case 6:
                            if (时 == 20 && 分 == 20 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 21 && x == true) {
                                判断答案();
                            }
                            break;
                        case 7:
                            if (时 == 20 && 分 == 22 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 23 && x == true) {
                                判断答案();
                            }
                            break;
                        case 8:
                            if (时 == 20 && 分 == 24 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 25 && x == true) {
                                判断答案();
                            }
                            break;
                        case 9:
                            if (时 == 20 && 分 == 26 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 27 && x == true) {
                                判断答案();
                            }
                            break;
                        case 10:
                            if (时 == 20 && 分 == 28 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 29 && x == true) {
                                判断答案();
                                if (星期 > 1 && 星期 < 7) {
                                    关闭OX答题线程();
                                }
                            }
                        case 11:
                            if (时 == 20 && 分 == 30 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 31 && x == true) {
                                判断答案();
                            }
                        case 12:
                            if (时 == 20 && 分 == 32 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 33 && x == true) {
                                判断答案();
                            }
                        case 13:
                            if (时 == 20 && 分 == 34 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 35 && x == true) {
                                判断答案();
                            }
                        case 14:
                            if (时 == 20 && 分 == 36 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 37 && x == true) {
                                判断答案();
                            }
                        case 15:
                            if (时 == 20 && 分 == 38 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 39 && x == true) {
                                判断答案();
                            }
                        case 16:
                            if (时 == 20 && 分 == 40 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 41 && x == true) {
                                判断答案();
                            }
                        case 17:
                            if (时 == 20 && 分 == 42 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 43 && x == true) {
                                判断答案();
                            }
                        case 18:
                            if (时 == 20 && 分 == 44 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 45 && x == true) {
                                判断答案();
                            }
                        case 19:
                            if (时 == 20 && 分 == 46 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 47 && x == true) {
                                判断答案();
                            }
                        case 20:
                            if (时 == 20 && 分 == 48 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 49 && x == true) {
                                判断答案();
                            }
                            break;
                        case 21:
                            if (时 == 20 && 分 == 50 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 51 && x == true) {
                                判断答案();
                            }
                            break;
                        case 22:
                            if (时 == 20 && 分 == 52 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 53 && x == true) {
                                判断答案();
                            }
                            break;
                        case 23:
                            if (时 == 20 && 分 == 54 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 55 && x == true) {
                                判断答案();
                            }
                            break;
                        case 24:
                            if (时 == 20 && 分 == 56 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 57 && x == true) {
                                判断答案();
                            }
                            break;
                        case 25:
                            if (时 == 20 && 分 == 58 && x == false) {
                                if ("".equals(题目)) {
                                    OX出题();
                                }
                            } else if (时 == 20 && 分 == 59 && x == true) {
                                判断答案();
                                关闭OX答题线程();
                            }
                            break;
                        default:

                            break;
                    }
                }
            }, 1000 * 5);
        }
    }

    public static void 关闭OX答题线程() {
        if (OX答题线程 != null) {
            OX答题线程.cancel(true);
            OX答题线程 = null;
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect("[每日活动] : OX 答题此次答题圆满结束，感谢各位小伙伴的支持。", 5120027);
                }
            }
            题 = 1;
            OX答题("[每日活动] : OX答题此次答题圆满结束，感谢各位小伙伴的支持。");
            MapleParty.OX答题活动 = 0;
        }
    }

    public static void OX出题() {
        x = true;
        题目 = 随机取题目();
        答案 = 随机取题目答案(题目);
        System.err.println("[服务端]" + CurrentReadable_Time() + " : [OX答题]第" + 题 + "题 出题: " + 题目);
        System.err.println("[服务端]" + CurrentReadable_Time() + " : [OX答题]第" + 题 + "题 答案: " + 答案);
        OX答题("【第" + 题 + "题】: " + 题目 + "");
        for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
            for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                if (mch.getMapId() == 109020001) {
                    mch.startMapEffect("【第" + 题 + "题】: " + 题目 + "", 5120027);
                }
            }
        }
    }

    public static void 判断答案() {
        x = false;
        答案 = 随机取题目答案(题目);
        if ("O".equals(答案) || "o".equals(答案)) {
            O();
        } else if ("X".equals(答案) || "x".equals(答案)) {
            X();
        } else {
            Z();
        }
        System.err.println("[服务端]" + CurrentReadable_Time() + " : 判断答案: " + 答案);
        题目 = "";
        题++;
    }

    public static String 随机取题目() {
        String 题目 = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM `oxdt` ORDER BY RAND() LIMIT 0,100 ;  ")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    题目 = rs.getString("b");
                }
            }
        } catch (SQLException ex) {

        }
        return 题目;
    }

    public static String 随机取题目答案(String a) {
        String 答案 = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM `oxdt` ; ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString("b").equals(a)) {
                        答案 = rs.getString("c");
                    }
                }
            }
        } catch (SQLException ex) {

        }
        return 答案;
    }

    public static void O() {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                if (chr == null) {
                    continue;
                }
                if (chr.getMapId() == 109020001) {
                    double x = chr.getPosition().getX();
                    double y = chr.getPosition().getY();
                    if (x <= -307 && y > -200) {
                        NPCScriptManager.getInstance().dispose(chr.getClient());
                        NPCScriptManager.getInstance().start(chr.getClient(), 9000011, 4);
                        chr.dropMessage(1, "恭喜你选择了正确的答案");
                    } else {
                        chr.dropMessage(1, "很抱歉，你选择了不正确的答案");
                    }
                }
            }
        }
    }

    public static void X() {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                if (chr == null) {
                    continue;
                }
                if (chr.getMapId() == 109020001) {
                    double x = chr.getPosition().getX();
                    double y = chr.getPosition().getY();
                    if (x >= -146 && y > -200) {
                        NPCScriptManager.getInstance().dispose(chr.getClient());
                        NPCScriptManager.getInstance().start(chr.getClient(), 9000011, 4);
                        chr.dropMessage(1, "恭喜你选择了正确的答案");
                    } else {
                        chr.dropMessage(1, "很抱歉，你选择了不正确的答案");
                    }
                }
            }
        }
    }

    public static void Z() {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                if (chr == null) {
                    continue;
                }
                if (chr.getMapId() == 109020001) {
                    chr.dropMessage(1, "很抱歉，被录入的答案既不是O，也不是X，答案有问题。");
                }
            }
        }

    }

}
