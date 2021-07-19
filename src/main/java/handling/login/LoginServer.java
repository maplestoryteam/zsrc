package handling.login;

import handling.netty.ServerConnection;
import handling.world.MapleParty;
import server.ServerProperties;
import tools.Triple;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static a.用法大全.判断容纳人数;

public class LoginServer {

    public static int PORT = 5100;
    private static InetSocketAddress InetSocketadd;
    private static ServerConnection acceptor;
    private static Map<Integer, Integer> load = new HashMap<>();
    private static String serverName, eventMessage;
    private static byte flag;
    private static int maxCharacters, userLimit, usersOn = 0;
    private static boolean finishedShutdown = true, adminOnly = false;
    private static final HashMap<Integer, Triple<String, String, Integer>> loginAuth = new HashMap();
    private static final HashSet<String> loginIPAuth = new HashSet();
    private static final LoginServer instance = new LoginServer();

    public static LoginServer getInstance() {
        return instance;
    }

    public static void putLoginAuth(int chrid, String ip, String tempIp, int channel) {
        loginAuth.put(chrid, new Triple(ip, tempIp, channel));
        loginIPAuth.add(ip);
    }

    public static Triple<String, String, Integer> getLoginAuth(int chrid) {
        return loginAuth.remove(chrid);
    }

    public static boolean containsIPAuth(String ip) {
        return loginIPAuth.contains(ip);
    }

    public static void removeIPAuth(String ip) {
        loginIPAuth.remove(ip);
    }

    public static void addIPAuth(String ip) {
        loginIPAuth.add(ip);
    }

    public static final void addChannel(final int channel) {
        load.put(channel, 0);
    }

    public static final void removeChannel(final int channel) {
        load.remove(channel);
    }

    public static final void run_startup_configurations() {

        if (MapleParty.容纳人数 == 999) {
            userLimit = gui.Start.ConfigValuesMap.get("服务端最大人数");
        } else {
            userLimit = 判断容纳人数(MapleParty.启动账号);
        }
        serverName = MapleParty.开服名字;
        int 双爆频道 = gui.Start.ConfigValuesMap.get("双爆频道开关");
        if (双爆频道 == 0) {
            eventMessage = "#b频道 #r" + Integer.parseInt(ServerProperties.getProperty("ZEV.Count")) + " \r\n #b为双爆频道";
        } else {
            eventMessage = "";
        }
        flag = 0;//Byte.parseByte(ServerProperties.getProperty("ZEV.Flag"));
       // PORT = 8484;
        adminOnly = Boolean.parseBoolean(ServerProperties.getProperty("ZEV.Admin", "false"));
        ////maxCharacters = Integer.parseInt(ServerProperties.getProperty("ZEV.MaxCharacters"));
        System.out.println("○ 启动游戏频道:");
        acceptor = new ServerConnection(PORT);
        acceptor.run();
        System.out.println("○ ZEVMS服务端: 启动端口 " + PORT);
    }
    

    public static final void shutdown() {
        if (finishedShutdown) {
            return;
        }
        System.out.println("开始关闭频道服务器...");
        //       acceptor.setCloseOnDeactivation(true);
//        for (IoSession ss : acceptor.getManagedSessions().values()) {
//            ss.close(true);
//        }
        //acceptor.unbind();
        finishedShutdown = true; //nothing. lol
    }
    public static final void 关闭频道服务端() {
        if (finishedShutdown) {
            return;
        }
        System.out.println("开始关闭频道服务器...");
        acceptor.close();
        finishedShutdown = true; 
    }
    public static final String getServerName() {
        return serverName;
    }

    public static final String getEventMessage() {
        return eventMessage;
    }

    public static final byte getFlag() {
        return flag;
    }

    public static final int getMaxCharacters() {
        return maxCharacters;
    }

    public static final Map<Integer, Integer> getLoad() {
        return load;
    }

    public static void setLoad(final Map<Integer, Integer> load_, final int usersOn_) {
        load = load_;
        usersOn = usersOn_;
    }

    public static final void setEventMessage(final String newMessage) {
        eventMessage = newMessage;
    }

    public static final void setFlag(final byte newflag) {
        flag = newflag;
    }

    public static final int getUserLimit() {
        return userLimit;
        //  return Integer.parseInt(ServerProperties.getProperty("tms.UserLimit"));
    }

    public static final int getUsersOn() {
        return usersOn;
    }

    public static final void setUserLimit(final int newLimit) {
        userLimit = newLimit;
    }

    public static final boolean isAdminOnly() {
        return adminOnly;
    }

    public static final boolean isShutdown() {
        return finishedShutdown;
    }

    public static final void setOn() {
        finishedShutdown = false;
    }
    
}
