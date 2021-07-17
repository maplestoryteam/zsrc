/////*
////QQ机器人
//// */
/////*package server;
////
////import static a.Yongfa.Yongfa1.角色ID取账号ID;
////import static a.Yongfa.Yongfa1.账号ID取账号;
////import static a.Yongfa.Yongfa1.账号查在线;
////import static abc.Game.开服名字;
////import static abc.Game.测速网速;
////import static abc.Game.版本;
////import abc.注册白名单;
////import abc.注册黑名单;
////import client.Class2;
////import client.LoginCrypto;
////import client.MapleCharacter;
////import database.DatabaseConnection;
////import gui.ZEVMS;
////import handling.RecvPacketOpcode;
////import handling.SendPacketOpcode;
////import handling.channel.ChannelServer;
////import handling.login.handler.AutoRegister;
////import handling.world.MapleParty;
////import handling.world.World;
////import handling.world.guild.MapleGuild;
////import java.awt.Point;
////import java.io.BufferedReader;
////import java.io.File;哈
////import java.io.IOException;
////import java.io.InputStream;
////import java.io.InputStreamReader;
////import java.io.UnsupportedEncodingException;
////import java.net.DatagramPacket;
////import java.net.DatagramSocket;
////import java.net.InetAddress;
////import java.net.MalformedURLException;
////import java.net.SocketException;
////import java.net.URL;
////import java.net.URLConnection;
////import java.sql.Connection;
////import java.sql.PreparedStatement;
////import java.sql.ResultSet;
////import java.sql.SQLException;
////import java.text.SimpleDateFormat;
////import java.util.ArrayList;
////import java.util.Calendar;
////import java.util.Date;
////import java.util.List;
////import java.util.Locale;
////import java.util.concurrent.ScheduledFuture;
////import java.util.logging.Level;
////import java.util.logging.Logger;
////import javax.swing.JOptionPane;
////import provider.MapleData;
////import provider.MapleDataProvider;
////import provider.MapleDataProviderFactory;
////import provider.WzXML.CashGui;
////import scripting.NPCConversationManager;
////import scripting.PortalScriptManager;
////import scripting.ReactorScriptManager;
////import static server.MapleCarnivalChallenge.getJobNameById;
////import static server.Start.GetCloudBacklist;
////import server.custom.respawn.RespawnInfo;
////import server.events.MapleEvent;
////import server.events.MapleEventType;
////import server.life.MapleLifeFactory;
////import server.life.MapleMonster;
////import server.life.MapleMonsterInformationProvider;
////import server.life.OverrideMonsterStats;
////import server.maps.MapleMap;
////import tools.FileoutputUtil;
////import static tools.FileoutputUtil.CurrentReadable_Date;
////import static tools.FileoutputUtil.CurrentReadable_Time;
////import tools.MaplePacketCreator;
////import tools.StringUtil;
////
/////**
//// *
//// * @author 时代先锋
//// */
////public class QQMsgServer implements Runnable {
////
//////    public static void main(String args[]) {
//////        new Thread(QQMsgServer.getInstance()).start();
//////        System.err.println("启动好了");
//////    }
////    private static final int INPORT = 9001;
////    private static final int PeerPort = 9000;
////
////    private byte[] buf = new byte[1024];
////    private DatagramPacket dp = new DatagramPacket(buf, buf.length);
////    private static DatagramSocket socket;
////    private static final MapleDataProvider stringDataWZ = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath","wz") + "/String.wz"));
////    private static final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath","wz") + "/Mob.wz"));
////    private static final MapleData mobStringData = stringDataWZ.getData("Mob.img");
////    private static final QQMsgServer instance = new QQMsgServer();
////
////    public static QQMsgServer getInstance() {
////        return instance;
////    }
////
////    private QQMsgServer() {
////        try {
////            socket = new DatagramSocket(INPORT);
////            //System.out.println("Launch qq msg server completed - Port: " + INPORT);
////        } catch (SocketException e) {
////            System.err.println("Can't open socket");
////            System.exit(1);
////        }
////    }
////
////    public static void sendMsg(final String msg, final String token) {
////        try {
////            String data = String.format("P_%s_%s", token, msg);
////            byte[] buf = data.getBytes();
////            //System.out.println("[->qq] : " + new String(buf));
////            DatagramPacket echo = new DatagramPacket(buf, buf.length, InetAddress.getLoopbackAddress(), PeerPort);
////            socket.send(echo);
////        } catch (IOException e) {
////            System.err.println("sendMsgToQQ error");
////            e.printStackTrace();
////        }
////    }
////
////    public static void sendMsgToAdminQQ(final String msg) {
////        try {
////            String data = "A_" + msg;
////            byte[] buf = data.getBytes();
////            //System.out.println("[->admin qq] : " + new String(buf));
////            DatagramPacket echo = new DatagramPacket(buf, buf.length, InetAddress.getLoopbackAddress(), PeerPort);
////            socket.send(echo);
////        } catch (IOException e) {
////            System.err.println("sendMsgToAdminQQ error");
////            e.printStackTrace();
////        }
////    }
////
////    public static void sendMsgToQQGroup(final String msg) {
////
////        try {
////            String data = "G_" + msg;
////            byte[] buf = data.getBytes();
////            //System.out.println("[->qq group] : " + new String(buf));
////            DatagramPacket echo = new DatagramPacket(buf, buf.length, InetAddress.getLoopbackAddress(), PeerPort);
////            socket.send(echo);
////        } catch (IOException e) {
////            System.err.println("sendMsgToQQGroup error");
////            e.printStackTrace();
////        }
////
////    }
////
////    private static int sendToOnlinePlayer(final String fromQQ, final String msg) {
////
////        int count = 0;
////        if (MapleParty.机器人待机 > 0) {
////            return count;
////        }
////        if (MapleParty.信息同步 > 0) {
////            return count;
////        }
////        for (ChannelServer chl : ChannelServer.getAllInstances()) {
////            for (MapleCharacter chr : chl.getPlayerStorage().getAllCharacters()) {
////                if (chr == null) {
////                    continue;
////                }
////                if (chr.Getcharacterz("" + chr.getId() + "", 3) > 0) {
////                    continue;
////                }
////                chr.dropMessage(String.format("[群][%s] : %s", fromQQ, msg));
////                count++;
////
////            }
////        }
////        return count;
////    }
////
////    public int 查询在线人数() {
////        int count = 0;
////        for (ChannelServer chl : ChannelServer.getAllInstances()) {
////            count += chl.getPlayerStorage().getAllCharacters().size();
////        }
////        return count;
////    }
////
////    private static void 解卡发型(final String qq, final String account, final String token) {
////        //账号ID取账号(角色ID取账号ID(account)) != qq
////        if (!账号ID取账号(角色ID取账号ID(account)).equals(qq)) {
////            sendMsg("<" + 开服名字 + ">机器人:你要解卡发型的角色不是属于你的。", token);
////            return;
////        }
////        if (账号查在线(Integer.parseInt(qq)) != 0) {
////            sendMsg("<" + 开服名字 + ">机器人:你的角色目前在线，你可以发送断开连接断开角色。", token);
////            return;
////        }
////        PreparedStatement ps = null;
////        PreparedStatement ps1 = null;
////        ResultSet rs = null;
////        try {
////            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET hair = ? WHERE id = ?");
////            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
////            ps1.setInt(1, Integer.parseInt(account));
////            rs = ps1.executeQuery();
////            if (rs.next()) {
////                String sqlString1 = null;
////                sqlString1 = "update characters set hair='30000' where id='" + Integer.parseInt(account) + "';";
////                PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString1);
////                hair.executeUpdate(sqlString1);
////                sendMsg("<" + 开服名字 + ">机器人:角色发型解卡成功阿。", token);
////            }
////        } catch (SQLException ex) {
////            Logger.getLogger(CashGui.class.getName()).log(Level.SEVERE, null, ex);
////        }
////    }
////
////    private static void 解卡脸型(final String qq, final String account, final String token) {
////        if (!账号ID取账号(角色ID取账号ID(account)).equals(qq)) {
////            sendMsg("<" + 开服名字 + ">机器人:你要解卡脸型的角色不是属于你的。", token);
////            return;
////        }
////        if (账号查在线(Integer.parseInt(qq)) != 0) {
////            sendMsg("<" + 开服名字 + ">机器人:你的角色目前在线，你可以发送断开连接断开角色。", token);
////            return;
////        }
////        PreparedStatement ps = null;
////        PreparedStatement ps1 = null;
////        ResultSet rs = null;
////        try {
////            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET face = ?WHERE id = ?");
////            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
////            ps1.setInt(1, Integer.parseInt(account));
////            rs = ps1.executeQuery();
////            if (rs.next()) {
////                String sqlString1 = null;
////                sqlString1 = "update characters set face='20000' where id='" + Integer.parseInt(account) + "';";
////                PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString1);
////                hair.executeUpdate(sqlString1);
////                sendMsgToQQGroup("<" + 开服名字 + ">机器人:管理员已经将角色id为 " + account + " 的玩家解卡脸型成功。");
////                sendMsg("<" + 开服名字 + ">机器人:角色脸型解卡成功。", token);
////            }
////        } catch (SQLException ex) {
////            Logger.getLogger(CashGui.class.getName()).log(Level.SEVERE, null, ex);
////        }
////
////    }
////
////    private static void 解卡地图(final String qq, final String account, final String token) {
////        if (!账号ID取账号(角色ID取账号ID(account)).equals(qq)) {
////            sendMsg("<" + 开服名字 + ">机器人:你要解卡地图的角色不是属于你的。", token);
////            return;
////        }
////        if (账号查在线(Integer.parseInt(qq)) != 0) {
////            sendMsg("<" + 开服名字 + ">机器人:你的角色目前在线，你可以发送断开连接断开角色。", token);
////            return;
////        }
////        PreparedStatement ps = null;
////        PreparedStatement ps1 = null;
////        ResultSet rs = null;
////        try {
////            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET map = ?WHERE id = ?");
////            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
////            ps1.setInt(1, Integer.parseInt(account));
////            rs = ps1.executeQuery();
////            if (rs.next()) {
////                String sqlString1 = null;
////                sqlString1 = "update characters set map='100000000' where id='" + Integer.parseInt(account) + "';";
////                PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString1);
////                hair.executeUpdate(sqlString1);
////                sendMsgToQQGroup("<" + 开服名字 + ">机器人:管理员已经将角色id为 " + account + " 的玩家解卡地图成功。");
////                sendMsg("<" + 开服名字 + ">机器人:角色地图解卡成功。", token);
////            }
////        } catch (SQLException ex) {
////            Logger.getLogger(CashGui.class.getName()).log(Level.SEVERE, null, ex);
////        }
////    }
////
////    private static void 切断链接(final String qq, final String account, final String token) {
////        if (!账号ID取账号(角色ID取账号ID(account)).equals(qq)) {
////            sendMsg("<" + 开服名字 + ">机器人:你要切断链接的角色不是属于你的。", token);
////            return;
////        }
////        if (账号查在线(Integer.parseInt(qq)) == 0) {
////            sendMsg("<" + 开服名字 + ">机器人:你的角色目前不在线。", token);
////            return;
////        }
////        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////            for (MapleCharacter c : cserv.getPlayerStorage().getAllCharacters()) {
////                if (c.getId() == Integer.parseInt(account)) {
////                    c.getClient().getSession().close();
////                }
////            }
////        }
////    }
////
////    private static void 上传云黑名单(final String qq, final String newPassword, final String token) {
////        if ("71447500".equals(qq) || "34443647".equals(qq)) {
////            FileoutputUtil.logToZev("blacklist", ",\r\n" + newPassword + "/0");
////            FileoutputUtil.logToZev("blacklist上传记录.txt", "\r\n" + token + "/" + newPassword + "");
////            sendMsg("<" + 开服名字 + ">机器人:账号 " + newPassword + " 上传到云黑名单成功。", token);
////        }
////    }
////
////    private static void 解卡账号(final String qq, final String account, final String token) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            try {
////                Connection con = DatabaseConnection.getConnection();
////                PreparedStatement ps;
////                ps = con.prepareStatement("Update accounts set loggedin = ? Where name = ?");
////                ps.setInt(1, 0);
////                ps.setString(2, account);
////                ps.execute();
////                ps.close();
////            } catch (Exception ex) {
////                JOptionPane.showMessageDialog(null, "错误!\r\n" + ex);
////            }
////            sendMsgToQQGroup("<" + 开服名字 + ">机器人:管理员已经将账号 " + account + " 解卡成功。");
////            sendMsg("<" + 开服名字 + ">机器人:账号 " + account + " 解卡成功。", token);
////        }
////    }
////
////    private static void 传话(final String qq, final String account, final String token) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq)) {
////            sendMsgToQQGroup("<" + 开服名字 + ">机器人:" + account + "");
////        }
////    }
////
////    private static void 传送(final String qq, final String account, final String a, final String token) {
////        if (!账号ID取账号(角色ID取账号ID(account)).equals(qq)) {
////            sendMsg("<" + 开服名字 + ">机器人:你要切断链接的角色不是属于你的。", token);
////            return;
////        }
////        if (账号查在线(Integer.parseInt(qq)) == 0) {
////            sendMsg("<" + 开服名字 + ">机器人:你的角色目前不在线。", token);
////            return;
////        }
////        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////            for (MapleCharacter c : cserv.getPlayerStorage().getAllCharacters()) {
////                if (c.getId() == Integer.parseInt(account)) {
////                    if (a == "射手村") {
////                        c.changeMap(100000000, 1);
////                    } else if (a == "市场") {
////                        c.changeMap(910000000, 1);
////                    }
////                }
////            }
////        }
////    }
////
////    @Override
////    public void run() {
////        try {
////            while (true) {
////                socket.receive(dp);
////                // 接收到来自QQ机器人的消息
////                String rcvd = new String(dp.getData(), 0, dp.getLength());
////                //System.out.println("QQ: " + rcvd);
////
////                String msgArr[] = rcvd.split("_");
////                String msgType = msgArr[0];
////                if (msgType.equals("P")) { // 私人
////                    int index = msgType.length() + 1;
////                    String fromQQ = msgArr[1];
////                    index += fromQQ.length() + 1;
////                    String token = msgArr[2];
////                    index += token.length() + 1;
////
////                    String msg[] = rcvd.substring(index).trim().split("\\s+");
////
////                    switch (msg[0]) {
////                        case "*密码修改":
////                        case "*修改密码":
////                            if (msg.length > 1) {
////                                修改密码(fromQQ, msg[1], token);
////                            } else {
////                                sendMsg("<" + 开服名字 + ">机器人:\r\n"
////                                        + "你发给了我不正确的指令\r\n"
////                                        + "格式:*修改密码<空格>[你的密码]", token);
////                            }
////                            break;
////                        case "*上传云黑名单":
////                            if (msg.length > 1) {
////                                上传云黑名单(fromQQ, msg[1], token);
////                            } else {
////                                sendMsg("<" + 开服名字 + ">机器人:\r\n"
////                                        + "你发给了我不正确的指令\r\n"
////                                        + "格式:*上传云黑名单<空格>[账号]", token);
////                            }
////                            break;
////                        case "*解卡账号":
////                            if (msg.length > 1) {
////                                解卡账号(fromQQ, msg[1], token);
////                            } else {
////                                sendMsg("<" + 开服名字 + ">机器人:\r\n"
////                                        + "你发给了我不正确的指令\r\n"
////                                        + "格式:*解卡账号<空格>[账号]", token);
////                            }
////                            break;
////                        case "*解卡发型":
////                            if (msg.length > 1) {
////                                解卡发型(fromQQ, msg[1], token);
////                            } else {
////                                sendMsg("<" + 开服名字 + ">机器人:\r\n"
////                                        + "你发给了我不正确的指令\r\n"
////                                        + "格式:*解卡发型<空格>[角色id]", token);
////                            }
////                            break;
////                        case "*解卡脸型":
////                            if (msg.length > 1) {
////                                解卡脸型(fromQQ, msg[1], token);
////                            } else {
////                                sendMsg("<" + 开服名字 + ">机器人:\r\n"
////                                        + "你发给了我不正确的指令\r\n"
////                                        + "格式:*解卡脸型<空格>[角色id]", token);
////                            }
////                            break;
////                        case "*解卡地图":
////                            if (msg.length > 1) {
////                                解卡地图(fromQQ, msg[1], token);
////                            } else {
////                                sendMsg("<" + 开服名字 + ">机器人:\r\n"
////                                        + "你发给了我不正确的指令\r\n"
////                                        + "格式:*解卡地图<空格>[角色id]", token);
////                            }
////                            break;
////                        case "*切断链接":
////                            if (msg.length > 1) {
////                                切断链接(fromQQ, msg[1], token);
////                            } else {
////                                sendMsg("<" + 开服名字 + ">机器人:\r\n"
////                                        + "你发给了我不正确的指令\r\n"
////                                        + "格式:*切断链接<空格>[角色id]", token);
////                            }
////                            break;
////                        case "*传送":
////                            if (msg.length > 1) {
////                                传送(fromQQ, msg[1], msg[2], token);
////                            } else {
////                                sendMsg("<" + 开服名字 + ">机器人:\r\n"
////                                        + "你发给了我不正确的指令\r\n"
////                                        + "格式:*传送<空格>[角色id]<空格>[目标地图]", token);
////                            }
////                            break;
////                        case "*传话":
////                            传话(fromQQ, msg[1], token);
////                            break;
////
////                        default: // 正常聊天
////                            break;
////                    }
////                } else if (msgType.equals("G")) { // 群组
////                    int index = msgType.length() + 1;
////                    String fromGroup = msgArr[1];
////                    index += fromGroup.length() + 1;
////                    String fromQQ = msgArr[2];
////                    index += fromQQ.length() + 1;
////                    String msg = rcvd.substring(index).trim();
////                    switch (msg) {
////                        case "*怪物攻城":
////                            怪物攻城(fromQQ);
////                            break;
////                        case "*发送1000点券福利":
////                            发送1000点券福利(fromQQ);
////                            break;
////                        case "*发送2000点券福利":
////                            发送2000点券福利(fromQQ);
////                            break;
////                        case "*发送5000点券福利":
////                            发送5000点券福利(fromQQ);
////                            break;
////                        case "*发送10000点券福利":
////                            发送10000点券福利(fromQQ);
////                            break;
////                        case "*开始推雪球比赛":
////                            开始推雪球比赛(fromQQ);
////                            break;
////                        case "*推雪球比赛开始":
////                            推雪球比赛开始(fromQQ);
////                            break;
////                        case "*开放1小时双倍经验":
////                            开放1小时双倍经验(fromQQ);
////                            break;
////                        case "*开放2小时双倍经验":
////                            开放2小时双倍经验(fromQQ);
////                            break;
////                        case "*开放1小时双倍爆率":
////                            开放1小时双倍爆率(fromQQ);
////                            break;
////                        case "*开放2小时双倍爆率":
////                            开放2小时双倍爆率(fromQQ);
////                            break;
////                        case "*禁止玩家登陆":
////                            禁止玩家登陆(fromQQ);
////                            break;
////                        case "*开启云黑名单":
////                            开启云黑名单(fromQQ);
////                            break;
////                        case "*关闭云黑名单":
////                            关闭云黑名单(fromQQ);
////                            break;
////                        case "*允许玩家登陆":
////                            允许玩家登陆(fromQQ);
////                            break;
////                        case "*允许玩家交易":
////                            允许玩家交易(fromQQ);
////                            break;
////                        case "*禁止玩家交易":
////                            禁止玩家交易(fromQQ);
////                            break;
////                        case "*允许游戏喇叭":
////                            允许游戏喇叭(fromQQ);
////                            break;
////                        case "*禁止游戏喇叭":
////                            禁止游戏喇叭(fromQQ);
////                            break;
////                        case "*禁止玩家聊天":
////                            禁止玩家聊天(fromQQ);
////                            break;
////                        case "*允许玩家聊天":
////                            允许玩家聊天(fromQQ);
////                            break;
////                        case "*关闭服务端":
////                            关闭服务端(fromQQ);
////                            break;
////                        case "*查看巅峰榜":
////                            查看巅峰榜(fromQQ);
////                            break;
////                        case "*开启全服决斗":
////                            开启全服决斗(fromQQ);
////                            break;
////                        case "*关闭全服决斗":
////                            关闭全服决斗(fromQQ);
////                            break;
////                        case "*开启登陆验证":
////                            开启登陆验证(fromQQ);
////                            break;
////                        case "*关闭登陆验证":
////                            关闭登陆验证(fromQQ);
////                            break;
////                        case "*数据存档":
////                            数据存档(fromQQ);
////                            break;
////                        case "*释放内存":
////                            释放内存(fromQQ);
////                            break;
////                        case "*运行状态":
////                            运行状态(fromQQ);
////                            break;
////                        case "*关闭巅峰广播":
////                            关闭巅峰广播(fromQQ);
////                            break;
////                        case "*开启巅峰广播":
////                            开启巅峰广播(fromQQ);
////                            break;
////                        case "*切断全服连接":
////                            切断全服连接(fromQQ);
////                            break;
////                        case "*重载服务端":
////                            重载服务端(fromQQ);
////                            break;
////                        case "*待机":
////                            待机(fromQQ);
////                            break;
////                        case "*开机":
////                            开机(fromQQ);
////                            break;
////                        case "*在线人数":
////                            在线人数(fromQQ);
////                            break;
////                        case "*开启伤害显示":
////                            开启伤害显示(fromQQ);
////                            break;
////                        case "*关闭伤害显示":
////                            关闭伤害显示(fromQQ);
////                            break;
////                        case "*关闭信息同步":
////                            关闭信息同步(fromQQ);
////                            break;
////                        case "*开启信息同步":
////                            开启信息同步(fromQQ);
////                            break;
////                        case "*关闭游戏公告群输出":
////                            关闭游戏公告群输出(fromQQ);
////                            break;
////                        case "*开启游戏公告群输出":
////                            开启游戏公告群输出(fromQQ);
////                            break;
////                        case "*开启数据统计":
////                            开启数据统计(fromQQ);
////                            break;
////                        case "*关闭数据统计":
////                            关闭数据统计(fromQQ);
////                            break;
////                        case "*开始大逃杀活动":
////                            开始大逃杀活动(fromQQ);
////                            break;
////                        case "*结束大逃杀活动":
////                            结束大逃杀活动(fromQQ);
////                            break;
////                        case "*版本检测":
////                            版本检测(fromQQ);
////                            break;
////                        case "*重载云黑名单":
////                            重载云黑名单(fromQQ);
////                            break;
////                        case "*经验":
////                        case "*爆率":
////                        case "*倍率信息":
////                        case "*倍率":
////                            服务端倍率();
////                            break;
////                        case "*怎么注册":
////                        case "*怎么注册呢":
////                        case "*注册":
////                        case "*注册帐号":
////                        case "*注册账号":
////                        case "*账号注册":
////                        case "*帐号注册":
////                            注册账号(fromQQ);
////                            break;
////                        case "*查询角色":
////                        case "*角色查询":
////                        case "*查看角色":
////                            查询角色(fromQQ);
////                            break;
////                        case "*开启流畅模式":
////                            开启流畅模式(fromQQ);
////                            break;
////                        case "*关闭流畅模式":
////                            关闭流畅模式(fromQQ);
////                            break;
////                        case "*上传云黑名单":
////                            上传云黑名单(fromQQ);
////                            break;
////                        //////////////////////////////////////////////////娱乐
////                        case "*查询帐号":
////                        case "*查询账号":
////                            查询账号(fromQQ);
////                            break;
////                        case "*赌金币":
////                            赌金币(fromQQ);
////                            break;
////                        case "*赌经验":
////                            赌经验(fromQQ);
////                            break;
////                        case "*赌点券":
////                            赌点券(fromQQ);
////                            break;
////                        case "*签到":
////                            签到(fromQQ);
////                            break;
////                        default: // 正常聊天
////                            sendToOnlinePlayer(fromQQ, msg);
////                            break;
////                    }
////                }
////            }
////        } catch (SocketException e) {
////            System.err.println("Can't open socket");
////            System.exit(1);
////        } catch (IOException e) {
////            System.err.println("UdpHost init error");
////            e.printStackTrace();
////        }
////    }
////
////    private static void 查询账号(final String qq) {
////        if (MapleParty.机器人待机 > 0) {
////            return;
////        }
////        /*
////        1，激活状态
////        2，金币
////        3，经验
////        4，点券
////        100，使用次数
////        101，签到次数
////
////         */
////        if (Getqqstem(qq, 1) > 0) {
////            sendMsgToQQGroup(String.format("<" + 开服名字 + "群娱乐>\r\n"
////                    + "账号：" + qq + "\r\n"
////                    + "金币：" + Getqqstem(qq, 2) + "\r\n"
////                    + "经验：" + Getqqstem(qq, 3) + "\r\n"
////                    + "点券：" + Getqqstem(qq, 4), qq));
////        } else {
////            sendMsgToQQGroup(String.format("<" + 开服名字 + "群娱乐>:账号" + qq + "未激活。请在游戏中小Z激活此功能。", qq));
////        }
////    }
////
////    private static void 赌经验(final String qq) {
////        if (MapleParty.机器人待机 > 0) {
////            return;
////        }
////        if (Getqqstem(qq, 1) <= 0) {
////            sendMsgToQQGroup(String.format("<" + 开服名字 + "群娱乐>:账号" + qq + "未激活。请在游戏中小Z激活此功能。", qq));
////            return;
////        }
////        int 经验 = server.Start.ConfigValuesMap.get("群赌博经验");
////        if (经验 == 0) {
////            return;
////        }
////        if (Getqqstem(qq, 4) >= 经验) {
////            final double 概率 = Math.ceil(Math.random() * 100);
////            if (概率 >= 70) {
////                //给金币
////                Gainqqstem(qq, 3, 经验);
////                //记录使用次数
////                Gainqqstem(qq, 100, 1);
////                sendMsgToQQGroup(String.format("<赌经验>: " + qq + " 恭喜你赢了 " + 经验 + " 经验。", qq));
////            } else {
////                //收取金币
////                Gainqqstem(qq, 3, -经验);
////                //记录使用次数
////                Gainqqstem(qq, 100, 1);
////                sendMsgToQQGroup(String.format("<赌经验>: " + qq + " 抱歉你输了 " + 经验 + " 经验。", qq));
////            }
////        } else {
////            sendMsgToQQGroup(String.format("<赌经验>: " + qq + " 你不够 " + 经验 + " 经验，请在游戏中找小z充值。", qq));
////        }
////    }
////
////    private static void 赌点券(final String qq) {
////        if (MapleParty.机器人待机 > 0) {
////            return;
////        }
////        if (Getqqstem(qq, 1) <= 0) {
////            sendMsgToQQGroup(String.format("<" + 开服名字 + "群娱乐>:账号" + qq + "未激活。请在游戏中小Z激活此功能。", qq));
////            return;
////        }
////        int 点券 = server.Start.ConfigValuesMap.get("群赌博点券");
////        if (点券 == 0) {
////            return;
////        }
////        if (Getqqstem(qq, 4) >= 点券) {
////            final double 概率 = Math.ceil(Math.random() * 100);
////            if (概率 >= 70) {
////                //给金币
////                Gainqqstem(qq, 4, 点券);
////                //记录使用次数
////                Gainqqstem(qq, 100, 1);
////                sendMsgToQQGroup(String.format("<赌点券>: " + qq + " 恭喜你赢了 " + 点券 + " 点券。", qq));
////            } else {
////                //收取金币
////                Gainqqstem(qq, 4, -点券);
////                //记录使用次数
////                Gainqqstem(qq, 100, 1);
////                sendMsgToQQGroup(String.format("<赌点券>: " + qq + " 抱歉你输了 " + 点券 + " 点券。", qq));
////            }
////        } else {
////            sendMsgToQQGroup(String.format("<赌点券>: " + qq + " 你不够 " + 点券 + " 点券，请在游戏中找小z充值。", qq));
////        }
////    }
////
////    public long 判断金币(final String qq) {
////        return Getqqstem(qq, 2);
////    }
////
////    public static void 金币(final String qq, int s) {
////        Gainqqstem(qq, 2, s);
////    }
////
////    public long 判断经验(final String qq) {
////        return Getqqstem(qq, 3);
////    }
////
////    public static void 经验(final String qq, int s) {
////        Gainqqstem(qq, 3, s);
////    }
////
////    public long 判断点券(final String qq) {
////        return Getqqstem(qq, 4);
////    }
////
////    public static void 点券(final String qq, int s) {
////        Gainqqstem(qq, 4, s);
////    }
////
////    public long 判使用次数(final String qq) {
////        return Getqqstem(qq, 100);
////    }
////
////    public static void 使用次数(final String qq, int s) {
////        Gainqqstem(qq, 100, s);
////    }
////
////    public long 判使签到数(final String qq) {
////        return Getqqstem(qq, 101);
////    }
////
////    public static void 签到次数(final String qq, int s) {
////        Gainqqstem(qq, 101, s);
////    }
////
////    public static long 判使每日(final String qq) {
////        return Getqqlog(qq, 1);
////    }
////
////    public static void 增加每日(final String qq, int s) {
////        Gainqqlog(qq, 1, s);
////    }
////
////    //签到
////    private static void 签到(final String qq) {
////        if (MapleParty.机器人待机 > 0) {
////            return;
////        }
////        if (Getqqstem(qq, 1) <= 0) {
////            sendMsgToQQGroup(String.format("<" + 开服名字 + "群娱乐>:账号" + qq + "未激活。请在游戏中小Z激活此功能。", qq));
////            return;
////        }
////        if (判使每日(qq) > 0) {
////            return;
////        }
////        if (server.Start.ConfigValuesMap.get("群签到金币奖励") == 0 && server.Start.ConfigValuesMap.get("群签到经验奖励") == 0 && server.Start.ConfigValuesMap.get("群签到点券奖励") == 0) {
////            return;
////        }
////        签到次数(qq, 1);
////        增加每日(qq, 1);
////        final double 金币 = Math.ceil(Math.random() * server.Start.ConfigValuesMap.get("群签到金币奖励"));
////        final double 经验 = Math.ceil(Math.random() * server.Start.ConfigValuesMap.get("群签到经验奖励"));
////        final double 点券 = Math.ceil(Math.random() * server.Start.ConfigValuesMap.get("群签到点券奖励"));
////        sendMsgToQQGroup(String.format("<" + 开服名字 + "群签到>\r\n"
////                + "恭喜 " + qq + " 签到成功\r\n"
////                + "获得金币：" + (int) 金币 + "\r\n"
////                + "获得点券：" + (int) 点券 + "\r\n"
////                + "获得经验：" + (int) 经验 + "", qq));
////        金币(qq, (int) 金币);
////        点券(qq, (int) 点券);
////        经验(qq, (int) 经验);
////    }
////
////    private static void 赌金币(final String qq) {
////        if (MapleParty.机器人待机 > 0) {
////            return;
////        }
////        if (Getqqstem(qq, 1) <= 0) {
////            sendMsgToQQGroup(String.format("<" + 开服名字 + "群娱乐>:账号" + qq + "未激活。请在游戏中小Z激活此功能。", qq));
////            return;
////        }
////        int 金币 = server.Start.ConfigValuesMap.get("群赌博金币");
////        if (金币 == 0) {
////            return;
////        }
////        if (Getqqstem(qq, 2) >= 金币) {
////            final double 概率 = Math.ceil(Math.random() * 100);
////            if (概率 >= 70) {
////                //给金币
////                Gainqqstem(qq, 2, 金币);
////                //记录使用次数
////                Gainqqstem(qq, 100, 1);
////                sendMsgToQQGroup(String.format("<赌金币>: " + qq + " 恭喜你赢了 " + 金币 + " 金币。", qq));
////            } else {
////                //收取金币
////                Gainqqstem(qq, 2, -金币);
////                //记录使用次数
////                Gainqqstem(qq, 100, 1);
////                sendMsgToQQGroup(String.format("<赌金币>: " + qq + " 抱歉你输了 " + 金币 + " 金币。", qq));
////            }
////        } else {
////            sendMsgToQQGroup(String.format("<赌金币>: " + qq + " 你不够 " + 金币 + " 金币，请在游戏中找小z充值。", qq));
////        }
////    }
////
////    private static int Getqqstem(String Name, int Channale) {
////        int ret = -1;
////        try {
////            Connection con = DatabaseConnection.getConnection();
////            PreparedStatement ps = con.prepareStatement("SELECT * FROM qqstem WHERE channel = ? and Name = ?");
////            ps.setInt(1, Channale);
////            ps.setString(2, Name);
////            ResultSet rs = ps.executeQuery();
////            rs.next();
////            ret = rs.getInt("Point");
////            rs.close();
////            ps.close();
////        } catch (SQLException ex) {
////        }
////        return ret;
////    }
////
////    public static void Gainqqstem(String qq, int Channale, int Piot) {
////        try {
////            int ret = Getqqstem(qq, Channale);
////            if (ret == -1) {
////                ret = 0;
////                PreparedStatement ps = null;
////                try {
////                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO qqstem (channel, Name,Point) VALUES (?, ?, ?)");
////                    ps.setInt(1, Channale);
////                    ps.setString(2, qq);
////                    ps.setInt(3, ret);
////                    ps.execute();
////                } catch (SQLException e) {
////                    System.out.println("xxxxxxxx:" + e);
////                } finally {
////                    try {
////                        if (ps != null) {
////                            ps.close();
////                        }
////                    } catch (SQLException e) {
////                        System.out.println("xxxxxxxxzzzzzzz:" + e);
////                    }
////                }
////            }
////            ret += Piot;
////            Connection con = DatabaseConnection.getConnection();
////            PreparedStatement ps = con.prepareStatement("UPDATE qqstem SET `Point` = ? WHERE Name = ? and channel = ?");
////            ps.setInt(1, ret);
////            ps.setString(2, qq);
////            ps.setInt(3, Channale);
////            ps.execute();
////            ps.close();
////        } catch (SQLException sql) {
////            System.err.println("Getqqstem!!55" + sql);
////        }
////    }
////
////    private static int Getqqlog(String Name, int Channale) {
////        int ret = -1;
////        try {
////            Connection con = DatabaseConnection.getConnection();
////            PreparedStatement ps = con.prepareStatement("SELECT * FROM qqlog WHERE channel = ? and Name = ?");
////            ps.setInt(1, Channale);
////            ps.setString(2, Name);
////            ResultSet rs = ps.executeQuery();
////            rs.next();
////            ret = rs.getInt("Point");
////            rs.close();
////            ps.close();
////        } catch (SQLException ex) {
////        }
////        return ret;
////    }
////
////    public static void Gainqqlog(String qq, int Channale, int Piot) {
////        try {
////            int ret = Getqqlog(qq, Channale);
////            if (ret == -1) {
////                ret = 0;
////                PreparedStatement ps = null;
////                try {
////                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO qqlog (channel, Name,Point) VALUES (?, ?, ?)");
////                    ps.setInt(1, Channale);
////                    ps.setString(2, qq);
////                    ps.setInt(3, ret);
////                    ps.execute();
////                } catch (SQLException e) {
////                    System.out.println("xxxxxxxx:" + e);
////                } finally {
////                    try {
////                        if (ps != null) {
////                            ps.close();
////                        }
////                    } catch (SQLException e) {
////                        System.out.println("xxxxxxxxzzzzzzz:" + e);
////                    }
////                }
////            }
////            ret += Piot;
////            Connection con = DatabaseConnection.getConnection();
////            PreparedStatement ps = con.prepareStatement("UPDATE qqlog SET `Point` = ? WHERE Name = ? and channel = ?");
////            ps.setInt(1, ret);
////            ps.setString(2, qq);
////            ps.setInt(3, Channale);
////            ps.execute();
////            ps.close();
////        } catch (SQLException sql) {
////            System.err.println("Getqqlog!!55" + sql);
////        }
////    }
////
////    private static void 上传云黑名单(final String qq) {
////        if ("71447500".equals(qq) || "34443647".equals(qq)) {
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:开始上传黑名单数据到云服务器。", qq));
////            String data = "";
////            try {
////                Connection con = DatabaseConnection.getConnection();
////                PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM accounts WHERE banned > 0");
////                try (ResultSet rs = ps.executeQuery()) {
////                    while (rs.next()) {
////                        data = rs.getString("DATA");
////                        FileoutputUtil.logToZev("blacklist", ",\r\n" + data + "/0");
////                    }
////                }
////                ps.close();
////            } catch (SQLException Ex) {
////                System.err.println("" + Ex);
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:上传黑名单数据完成，使用重载指令生效。", qq));
////        }
////    }
////
////    private static void 重载云黑名单(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:开始重载云黑名单数据。", qq));
////            GetCloudBacklist();
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:云黑名单数据重新加载完成。", qq));
////        }
////    }
////
////    private static void 开启流畅模式(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.流畅模式++;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端流畅模式开启成功。", qq));
////        }
////    }
////
////    private static void 关闭流畅模式(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.流畅模式 = 0;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端流畅模式关闭成功。", qq));
////        }
////    }
////
////    private static void 开启伤害显示(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.伤害显示++;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:开启伤害显示。", qq));
////        }
////    }
////
////    private static void 关闭伤害显示(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.伤害显示 = 0;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:关闭伤害显示。", qq));
////        }
////    }
////
////    private static void 版本检测(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            String v2 = Class2.Pgv();
////            if (Integer.parseInt(v2) != 版本) {
////                sendMsgToQQGroup(""
////                        + "<" + 开服名字 + ">\r\n"
////                        + "当前版本:v" + 版本 + "\r\n"
////                        + "最新版本:v" + Integer.parseInt(v2) + "\r\n"
////                        + "需要重启服务端进行更新"
////                );
////            } else {
////                sendMsgToQQGroup(""
////                        + "<" + 开服名字 + ">\r\n"
////                        + "当前版本:v" + 版本 + "\r\n"
////                        + "最新版本:v" + Integer.parseInt(v2) + "\r\n"
////                        + "服务端目前是最新版本。"
////                );
////            }
////        }
////    }
////
////    private static void 在线人数(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            int p = 0;
////            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
////                    if (chr != null) {
////                        ++p;
////                    }
////                }
////            }
////            sendMsgToQQGroup("<" + 开服名字 + ">机器人:已经将相关数据私聊给你啦。");
////            sendMsg("<" + 开服名字 + ">：\r\n"
////                    + "当前在线：" + p + " 人\r\n"
////                    //+ "登陆用户：" + MapleParty.角色登陆次数 + " 人\r\n"
////                    // + "登陆次数：" + MapleParty.玩家登陆次数 + " 次\r\n"
////                    + "运行时长：" + MapleParty.服务端运行时长 + " 分钟"
////                    + "", "71447500");
////            if (!"71447500".equals(ServerProperties.getProperty("ZEV.QQ1"))) {
////                sendMsg("<" + 开服名字 + ">：\r\n"
////                        + "当前在线：" + p + " 人\r\n"
////                        // + "登陆用户：" + MapleParty.角色登陆次数 + " 人\r\n"
////                        //+ "登陆次数：" + MapleParty.玩家登陆次数 + " 次\r\n"
////                        + "运行时长：" + MapleParty.服务端运行时长 + " 分钟"
////                        + "", "" + ServerProperties.getProperty("ZEV.QQ1") + "");
////            }
////            if (!ServerProperties.getProperty("ZEV.QQ1").equals(ServerProperties.getProperty("ZEV.QQ2"))) {
////                sendMsg("<" + 开服名字 + ">：\r\n"
////                        + "当前在线：" + p + " 人\r\n"
////                        // + "登陆用户：" + MapleParty.角色登陆次数 + " 人\r\n"
////                        // + "登陆次数：" + MapleParty.玩家登陆次数 + " 次\r\n"
////                        + "运行时长：" + MapleParty.服务端运行时长 + " 分钟"
////                        + "", "" + ServerProperties.getProperty("ZEV.QQ2") + "");
////            }
////        }
////    }
////
////    private static void 开始大逃杀活动(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.大逃杀活动 > 0) {
////                sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:活动已经开始，无法重复开始。", qq));
////                return;
////            }
////            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "<" + 开服名字 + ">机器人:活动[大逃杀]将在 5 分钟后 2 频道开始，请各位要参加的小伙伴，到射手村游戏地带参加，活动开始后将不可以再次进入。").getBytes());
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:活动[大逃杀]将在 5 分钟后 2 频道开始，请各位要参加的小伙伴，到射手村游戏地带参加，活动开始后将不可以再次进入。", qq));
////            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
////                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
////                    mch.startMapEffect("活动[大逃杀]将在 5 分钟后 2 频道开始，请各位要参加的小伙伴，到射手村游戏地带参加，活动开始后将不可以再次进入。", 5120027);
////                }
////            }
////            new Thread() {
////                @Override
////                public void run() {
////                    try {
////                        Thread.sleep(5 * 60 * 1000);//多少秒自动关闭
////                        World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "<" + 开服名字 + ">机器人:活动[大逃杀]开始，游戏地带关闭进入口，当存活最后一位玩家，点击自己即可获得胜利。").getBytes());
////                        sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:活动[大逃杀]开始，游戏地带关闭进入口，当存活最后一位玩家，点击自己即可获得胜利。", qq));
////                        for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
////                            for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
////                                mch.startMapEffect("活动[大逃杀]开始，游戏地带关闭进入口，当存活最后一位玩家，点击自己即可获得胜利。", 5120027);
////                            }
////                        }
////                        MapleParty.大逃杀活动 = 1;
////                    } catch (InterruptedException e) {
////                    }
////                }
////            }.start();
////        }
////    }
////
////    private static void 结束大逃杀活动(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.大逃杀活动 == 0) {
////                sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:活动并未开始。", qq));
////                return;
////            }
////            MapleParty.大逃杀活动 = 0;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:管理员结束了大逃杀活动。", qq));
////        }
////    }
////
////    private static void 开启数据统计(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.人数统计 = 0;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:已经开启数据统计。", qq));
////        }
////    }
////
////    private static void 关闭数据统计(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.人数统计++;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:已经关闭数据统计。", qq));
////        }
////    }
////
////    private static void 关闭游戏公告群输出(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.游戏公告群输出++;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:已经关闭游戏公告输出到群里。", qq));
////        }
////    }
////
////    private static void 开启游戏公告群输出(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.游戏公告群输出 = 0;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:已经开启游戏公告输出到群里。", qq));
////        }
////    }
////
////    private static void 关闭信息同步(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.信息同步++;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:已经关闭群与游戏信息同步。", qq));
////        }
////    }
////
////    private static void 开启信息同步(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.信息同步 = 0;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:已经开启群与游戏信息同步。", qq));
////        }
////    }
////
////    private static void 待机(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.机器人待机++;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:我已经进入待机状态，暂停工作。", qq));
////        }
////    }
////
////    private static void 开机(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            MapleParty.机器人待机 = 0;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:我已经进入开机状态，开始工作。", qq));
////        }
////    }
////
////    private static void 切断全服连接(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////                cserv.getPlayerStorage().断所有人(true);
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:已经成功切断所有在线玩家。", qq));
////        }
////    }
////
////    private static void 重载服务端(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            long start = System.currentTimeMillis();
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端开启重新载入，届时玩家可能会卡顿，请勿退出游戏，稍等片刻既可恢复。", qq));
////            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息]:服务端开启重新载入，届时玩家可能会卡顿，请勿退出游戏，稍等片刻既可恢复。").getBytes());
////            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////                //重载商店
////                MapleShopFactory.getInstance().clear();
////                //重载传送
////                PortalScriptManager.getInstance().clearScripts();
////                //重载物品WZ
////                MapleItemInformationProvider.getInstance().load();
////                //重载商城
////                CashItemFactory.getInstance().initialize();
////                //重载爆物
////                MapleMonsterInformationProvider.getInstance().clearDrops();
////                //重载家族
////                MapleGuild.loadAll(); //(this); ;
////                //重载爆物
////                ReactorScriptManager.getInstance().clearDrops();
////                SendPacketOpcode.reloadValues();
////                RecvPacketOpcode.reloadValues();
////
////            }
////            long now = System.currentTimeMillis() - start;
////            long seconds = now / 1000;
////            long ms = now % 1000;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端重载完毕，耗时: " + seconds + " 秒。", qq));
////            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息]:服务端重载完毕，耗时: " + seconds + " 秒。").getBytes());
////        }
////    }
////
////    private static void 发送1000点券福利(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            String 输出 = "";
////            String chars = "1234567890aAbBcCdDeEfFgGhHiIjJkKlLmMNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
////            char 生成1 = chars.charAt((int) (Math.random() * 62));
////            char 生成2 = chars.charAt((int) (Math.random() * 62));
////            char 生成3 = chars.charAt((int) (Math.random() * 62));
////            char 生成4 = chars.charAt((int) (Math.random() * 62));
////            char 生成5 = chars.charAt((int) (Math.random() * 62));
////            char 生成6 = chars.charAt((int) (Math.random() * 62));
////            char 生成7 = chars.charAt((int) (Math.random() * 62));
////            char 生成8 = chars.charAt((int) (Math.random() * 62));
////            char 生成9 = chars.charAt((int) (Math.random() * 62));
////            char 生成10 = chars.charAt((int) (Math.random() * 62));
////            char 生成11 = chars.charAt((int) (Math.random() * 62));
////            char 生成12 = chars.charAt((int) (Math.random() * 62));
////            char 生成13 = chars.charAt((int) (Math.random() * 62));
////            char 生成14 = chars.charAt((int) (Math.random() * 62));
////            char 生成15 = chars.charAt((int) (Math.random() * 62));
////            char 生成16 = chars.charAt((int) (Math.random() * 62));
////            char 生成17 = chars.charAt((int) (Math.random() * 62));
////            char 生成18 = chars.charAt((int) (Math.random() * 62));
////            char 生成19 = chars.charAt((int) (Math.random() * 62));
////            char 生成20 = chars.charAt((int) (Math.random() * 62));
////            char 生成21 = chars.charAt((int) (Math.random() * 62));
////            char 生成22 = chars.charAt((int) (Math.random() * 62));
////            char 生成23 = chars.charAt((int) (Math.random() * 62));
////            char 生成24 = chars.charAt((int) (Math.random() * 62));
////            char 生成25 = chars.charAt((int) (Math.random() * 62));
////            char 生成26 = chars.charAt((int) (Math.random() * 62));
////            char 生成27 = chars.charAt((int) (Math.random() * 62));
////            char 生成28 = chars.charAt((int) (Math.random() * 62));
////            char 生成29 = chars.charAt((int) (Math.random() * 62));
////            char 生成30 = chars.charAt((int) (Math.random() * 62));
////            String 充值卡 = "1Q" + 生成1 + "" + 生成2 + "" + 生成3 + "" + 生成4 + "" + 生成5 + "" + 生成6 + "" + 生成7 + "" + 生成8 + "" + 生成9 + "" + 生成10 + "" + 生成11 + "" + 生成12 + "" + 生成13 + "" + 生成14 + "" + 生成15 + "" + 生成16 + "" + 生成17 + "" + 生成18 + "" + 生成19 + "" + 生成20 + "" + 生成21 + "" + 生成22 + "" + 生成23 + "" + 生成24 + "" + 生成25 + "" + 生成26 + "" + 生成27 + "" + 生成28 + "" + 生成29 + "" + 生成30 + "";
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO z点券兑换卡 ( Name,Point,channel) VALUES ( ?, ?, ?)")) {
////                ps.setString(1, 充值卡);
////                ps.setInt(2, 1000);
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                FileoutputUtil.logToFile("充值卡后台库存/[" + CurrentReadable_Date() + "]1000点券充值卡[机器人].txt", "" + 充值卡 + "\r\n");
////                sendMsgToQQGroup(String.format("<" + 开服名字 + ">1000点券福利卡:" + 充值卡 + "", qq));
////            } catch (SQLException ex) {
////
////            }
////        }
////    }
////
////    private static void 发送2000点券福利(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            String 输出 = "";
////            String chars = "1234567890aAbBcCdDeEfFgGhHiIjJkKlLmMNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
////            char 生成1 = chars.charAt((int) (Math.random() * 62));
////            char 生成2 = chars.charAt((int) (Math.random() * 62));
////            char 生成3 = chars.charAt((int) (Math.random() * 62));
////            char 生成4 = chars.charAt((int) (Math.random() * 62));
////            char 生成5 = chars.charAt((int) (Math.random() * 62));
////            char 生成6 = chars.charAt((int) (Math.random() * 62));
////            char 生成7 = chars.charAt((int) (Math.random() * 62));
////            char 生成8 = chars.charAt((int) (Math.random() * 62));
////            char 生成9 = chars.charAt((int) (Math.random() * 62));
////            char 生成10 = chars.charAt((int) (Math.random() * 62));
////            char 生成11 = chars.charAt((int) (Math.random() * 62));
////            char 生成12 = chars.charAt((int) (Math.random() * 62));
////            char 生成13 = chars.charAt((int) (Math.random() * 62));
////            char 生成14 = chars.charAt((int) (Math.random() * 62));
////            char 生成15 = chars.charAt((int) (Math.random() * 62));
////            char 生成16 = chars.charAt((int) (Math.random() * 62));
////            char 生成17 = chars.charAt((int) (Math.random() * 62));
////            char 生成18 = chars.charAt((int) (Math.random() * 62));
////            char 生成19 = chars.charAt((int) (Math.random() * 62));
////            char 生成20 = chars.charAt((int) (Math.random() * 62));
////            char 生成21 = chars.charAt((int) (Math.random() * 62));
////            char 生成22 = chars.charAt((int) (Math.random() * 62));
////            char 生成23 = chars.charAt((int) (Math.random() * 62));
////            char 生成24 = chars.charAt((int) (Math.random() * 62));
////            char 生成25 = chars.charAt((int) (Math.random() * 62));
////            char 生成26 = chars.charAt((int) (Math.random() * 62));
////            char 生成27 = chars.charAt((int) (Math.random() * 62));
////            char 生成28 = chars.charAt((int) (Math.random() * 62));
////            char 生成29 = chars.charAt((int) (Math.random() * 62));
////            char 生成30 = chars.charAt((int) (Math.random() * 62));
////            String 充值卡 = "2Q" + 生成1 + "" + 生成2 + "" + 生成3 + "" + 生成4 + "" + 生成5 + "" + 生成6 + "" + 生成7 + "" + 生成8 + "" + 生成9 + "" + 生成10 + "" + 生成11 + "" + 生成12 + "" + 生成13 + "" + 生成14 + "" + 生成15 + "" + 生成16 + "" + 生成17 + "" + 生成18 + "" + 生成19 + "" + 生成20 + "" + 生成21 + "" + 生成22 + "" + 生成23 + "" + 生成24 + "" + 生成25 + "" + 生成26 + "" + 生成27 + "" + 生成28 + "" + 生成29 + "" + 生成30 + "";
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO z点券兑换卡 ( Name,Point,channel) VALUES ( ?, ?, ?)")) {
////                ps.setString(1, 充值卡);
////                ps.setInt(2, 2000);
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                FileoutputUtil.logToFile("充值卡后台库存/[" + CurrentReadable_Date() + "]2000点券充值卡[机器人].txt", "" + 充值卡 + "\r\n");
////                sendMsgToQQGroup(String.format("<" + 开服名字 + ">2000点券福利卡:" + 充值卡 + "", qq));
////            } catch (SQLException ex) {
////
////            }
////        }
////    }
////
////    private static void 发送5000点券福利(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            String 输出 = "";
////            String chars = "1234567890aAbBcCdDeEfFgGhHiIjJkKlLmMNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
////            char 生成1 = chars.charAt((int) (Math.random() * 62));
////            char 生成2 = chars.charAt((int) (Math.random() * 62));
////            char 生成3 = chars.charAt((int) (Math.random() * 62));
////            char 生成4 = chars.charAt((int) (Math.random() * 62));
////            char 生成5 = chars.charAt((int) (Math.random() * 62));
////            char 生成6 = chars.charAt((int) (Math.random() * 62));
////            char 生成7 = chars.charAt((int) (Math.random() * 62));
////            char 生成8 = chars.charAt((int) (Math.random() * 62));
////            char 生成9 = chars.charAt((int) (Math.random() * 62));
////            char 生成10 = chars.charAt((int) (Math.random() * 62));
////            char 生成11 = chars.charAt((int) (Math.random() * 62));
////            char 生成12 = chars.charAt((int) (Math.random() * 62));
////            char 生成13 = chars.charAt((int) (Math.random() * 62));
////            char 生成14 = chars.charAt((int) (Math.random() * 62));
////            char 生成15 = chars.charAt((int) (Math.random() * 62));
////            char 生成16 = chars.charAt((int) (Math.random() * 62));
////            char 生成17 = chars.charAt((int) (Math.random() * 62));
////            char 生成18 = chars.charAt((int) (Math.random() * 62));
////            char 生成19 = chars.charAt((int) (Math.random() * 62));
////            char 生成20 = chars.charAt((int) (Math.random() * 62));
////            char 生成21 = chars.charAt((int) (Math.random() * 62));
////            char 生成22 = chars.charAt((int) (Math.random() * 62));
////            char 生成23 = chars.charAt((int) (Math.random() * 62));
////            char 生成24 = chars.charAt((int) (Math.random() * 62));
////            char 生成25 = chars.charAt((int) (Math.random() * 62));
////            char 生成26 = chars.charAt((int) (Math.random() * 62));
////            char 生成27 = chars.charAt((int) (Math.random() * 62));
////            char 生成28 = chars.charAt((int) (Math.random() * 62));
////            char 生成29 = chars.charAt((int) (Math.random() * 62));
////            char 生成30 = chars.charAt((int) (Math.random() * 62));
////            String 充值卡 = "5Q" + 生成1 + "" + 生成2 + "" + 生成3 + "" + 生成4 + "" + 生成5 + "" + 生成6 + "" + 生成7 + "" + 生成8 + "" + 生成9 + "" + 生成10 + "" + 生成11 + "" + 生成12 + "" + 生成13 + "" + 生成14 + "" + 生成15 + "" + 生成16 + "" + 生成17 + "" + 生成18 + "" + 生成19 + "" + 生成20 + "" + 生成21 + "" + 生成22 + "" + 生成23 + "" + 生成24 + "" + 生成25 + "" + 生成26 + "" + 生成27 + "" + 生成28 + "" + 生成29 + "" + 生成30 + "";
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO z点券兑换卡 ( Name,Point,channel) VALUES ( ?, ?, ?)")) {
////                ps.setString(1, 充值卡);
////                ps.setInt(2, 5000);
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                FileoutputUtil.logToFile("充值卡后台库存/[" + CurrentReadable_Date() + "]5000点券充值卡[机器人].txt", "" + 充值卡 + "\r\n");
////                sendMsgToQQGroup(String.format("<" + 开服名字 + ">5000点券福利卡:" + 充值卡 + "", qq));
////            } catch (SQLException ex) {
////
////            }
////        }
////    }
////
////    private static void 发送10000点券福利(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            String 输出 = "";
////            String chars = "1234567890aAbBcCdDeEfFgGhHiIjJkKlLmMNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
////            char 生成1 = chars.charAt((int) (Math.random() * 62));
////            char 生成2 = chars.charAt((int) (Math.random() * 62));
////            char 生成3 = chars.charAt((int) (Math.random() * 62));
////            char 生成4 = chars.charAt((int) (Math.random() * 62));
////            char 生成5 = chars.charAt((int) (Math.random() * 62));
////            char 生成6 = chars.charAt((int) (Math.random() * 62));
////            char 生成7 = chars.charAt((int) (Math.random() * 62));
////            char 生成8 = chars.charAt((int) (Math.random() * 62));
////            char 生成9 = chars.charAt((int) (Math.random() * 62));
////            char 生成10 = chars.charAt((int) (Math.random() * 62));
////            char 生成11 = chars.charAt((int) (Math.random() * 62));
////            char 生成12 = chars.charAt((int) (Math.random() * 62));
////            char 生成13 = chars.charAt((int) (Math.random() * 62));
////            char 生成14 = chars.charAt((int) (Math.random() * 62));
////            char 生成15 = chars.charAt((int) (Math.random() * 62));
////            char 生成16 = chars.charAt((int) (Math.random() * 62));
////            char 生成17 = chars.charAt((int) (Math.random() * 62));
////            char 生成18 = chars.charAt((int) (Math.random() * 62));
////            char 生成19 = chars.charAt((int) (Math.random() * 62));
////            char 生成20 = chars.charAt((int) (Math.random() * 62));
////            char 生成21 = chars.charAt((int) (Math.random() * 62));
////            char 生成22 = chars.charAt((int) (Math.random() * 62));
////            char 生成23 = chars.charAt((int) (Math.random() * 62));
////            char 生成24 = chars.charAt((int) (Math.random() * 62));
////            char 生成25 = chars.charAt((int) (Math.random() * 62));
////            char 生成26 = chars.charAt((int) (Math.random() * 62));
////            char 生成27 = chars.charAt((int) (Math.random() * 62));
////            char 生成28 = chars.charAt((int) (Math.random() * 62));
////            char 生成29 = chars.charAt((int) (Math.random() * 62));
////            char 生成30 = chars.charAt((int) (Math.random() * 62));
////            String 充值卡 = "1W" + 生成1 + "" + 生成2 + "" + 生成3 + "" + 生成4 + "" + 生成5 + "" + 生成6 + "" + 生成7 + "" + 生成8 + "" + 生成9 + "" + 生成10 + "" + 生成11 + "" + 生成12 + "" + 生成13 + "" + 生成14 + "" + 生成15 + "" + 生成16 + "" + 生成17 + "" + 生成18 + "" + 生成19 + "" + 生成20 + "" + 生成21 + "" + 生成22 + "" + 生成23 + "" + 生成24 + "" + 生成25 + "" + 生成26 + "" + 生成27 + "" + 生成28 + "" + 生成29 + "" + 生成30 + "";
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO z点券兑换卡 ( Name,Point,channel) VALUES ( ?, ?, ?)")) {
////                ps.setString(1, 充值卡);
////                ps.setInt(2, 10000);
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                FileoutputUtil.logToFile("充值卡后台库存/[" + CurrentReadable_Date() + "]10000点券充值卡[机器人].txt", "" + 充值卡 + "\r\n");
////                sendMsgToQQGroup(String.format("<" + 开服名字 + ">10000点券福利卡:" + 充值卡 + "", qq));
////            } catch (SQLException ex) {
////
////            }
////        }
////    }
////
////    private static void 开始推雪球比赛(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            final MapleEventType type = MapleEventType.getByString("雪球赛");
////            if (type == null) {
////                final StringBuilder sb = new StringBuilder("目前开放的活动有: ");
////                for (MapleEventType t : MapleEventType.values()) {
////                    sb.append(t.name()).append(",");
////                }
////                //dropMessage(5, sb.toString().substring(0, sb.toString().length() - 1));
////            }
////            final String msg = MapleEvent.scheduleEvent(type, ChannelServer.getInstance(2));
////            MapleParty.雪球赛++;
////            if (msg.length() > 0) {
////                //c.getPlayer().dropMessage(5, msg);
////            }
////        }
////    }
////
////    private static void 推雪球比赛开始(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.雪球赛 > 0) {
////                MapleEvent.setEvent(ChannelServer.getInstance(2), true);
////            } else {
////                sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:请先“开始推雪球比赛”，待玩家到场后再使用“推雪球比赛开始”开始活动。", qq));
////            }
////        }
////    }
////
////    private static void 开放1小时双倍经验(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            int 原始经验 = Integer.parseInt(ServerProperties.getProperty("ZEV.Exp"));
////            int 双倍经验活动 = 原始经验 * 2;
////            int seconds = 0;
////            int mins = 0;
////            int hours = 1;
////            int time = seconds + (mins * 60) + (hours * 60 * 60);
////            final String rate = "经验";
////            World.scheduleRateDelay(rate, time);
////            for (ChannelServer cservs : ChannelServer.getAllInstances()) {
////                cservs.setExpRate(双倍经验活动);
////            }
////            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, 20, "[倍率活动] : 游戏开始 2 倍打怪经验活动，将持续 " + hours + " 小时，请各位玩家狂欢吧！").getBytes());
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:游戏开启 " + hours + " 小时双倍经验活动。", qq));
////        }
////    }
////
////    private static void 开放1小时双倍爆率(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            int 原始爆率 = Integer.parseInt(ServerProperties.getProperty("ZEV.Drop"));
////            int 双倍爆率活动 = 原始爆率 * 2;
////            int seconds = 0;
////            int mins = 0;
////            int hours = 1;
////            int time = seconds + (mins * 60) + (hours * 60 * 60);
////            final String rate = "爆率";
////            World.scheduleRateDelay(rate, time);
////            for (ChannelServer cservs : ChannelServer.getAllInstances()) {
////                cservs.setExpRate(双倍爆率活动);
////            }
////            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, 20, "[倍率活动] : 游戏开始 2 倍打怪爆率活动，将持续 " + hours + " 小时，请各位玩家狂欢吧！").getBytes());
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:游戏开启 " + hours + " 小时双倍爆率活动。", qq));
////        }
////    }
////
////    private static void 开放2小时双倍爆率(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            int 原始爆率 = Integer.parseInt(ServerProperties.getProperty("ZEV.Drop"));
////            int 双倍爆率活动 = 原始爆率 * 2;
////            int seconds = 0;
////            int mins = 0;
////            int hours = 2;
////            int time = seconds + (mins * 60) + (hours * 60 * 60);
////            final String rate = "爆率";
////            World.scheduleRateDelay(rate, time);
////            for (ChannelServer cservs : ChannelServer.getAllInstances()) {
////                cservs.setExpRate(双倍爆率活动);
////            }
////            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, 20, "[倍率活动] : 游戏开始 2 倍打怪爆率活动，将持续 " + hours + " 小时，请各位玩家狂欢吧！").getBytes());
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:游戏开启 " + hours + " 小时双倍爆率活动。", qq));
////        }
////    }
////
////    private static void 开放2小时双倍经验(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            int 原始经验 = Integer.parseInt(ServerProperties.getProperty("ZEV.Exp"));
////            int 双倍经验活动 = 原始经验 * 2;
////            int seconds = 0;
////            int mins = 0;
////            int hours = 2;
////            int time = seconds + (mins * 60) + (hours * 60 * 60);
////            final String rate = "经验";
////            World.scheduleRateDelay(rate, time);
////            for (ChannelServer cservs : ChannelServer.getAllInstances()) {
////                cservs.setExpRate(双倍经验活动);
////            }
////            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, 20, "[倍率活动] : 游戏开始 2 倍打怪经验活动，将持续 " + hours + " 小时，请各位玩家狂欢吧！").getBytes());
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:游戏开启 " + hours + " 小时双倍经验活动。", qq));
////        }
////    }
////
////    private static void 允许玩家登陆(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2013";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2013);
////                ps.setString(2, "禁止登陆开关");
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:允许玩家登陆游戏。", qq));
////        }
////    }
////
////    //开启云黑名单
////    private static void 开启云黑名单(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            MapleParty.云黑名单 = 1;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:云黑名单开启成功。", qq));
////        }
////    }
////
////    private static void 关闭云黑名单(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            MapleParty.云黑名单 = 0;
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:云黑名单关闭成功。", qq));
////        }
////    }
////
////    private static void 禁止玩家登陆(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2013";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2013);
////                ps.setString(2, "禁止登陆开关");
////                ps.setInt(3, 0);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:禁止玩家登陆游戏。", qq));
////        }
////    }
////
////    private static void 允许玩家交易(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2011";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2011);
////                ps.setString(2, "玩家交易开关");
////                ps.setInt(3, 0);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:允许玩家在游戏中交易。", qq));
////        }
////    }
////
////    private static void 禁止玩家交易(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2011";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2011);
////                ps.setString(2, "玩家交易开关");
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:禁止玩家在游戏中交易。", qq));
////        }
////    }
////
////    private static void 开启全服决斗(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2014";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2014);
////                ps.setString(2, "全服决斗开关");
////                ps.setInt(3, 0);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端开启全服决斗。", qq));
////            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息]:服务端开启全服决斗。").getBytes());
////        }
////    }
////
////    private static void 开启登陆验证(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2040";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2040);
////                ps.setString(2, "登陆验证开关");
////                ps.setInt(3, 0);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端开启登陆验证。", qq));
////
////        }
////    }
////
////    private static void 关闭登陆验证(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2040";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2040);
////                ps.setString(2, "登陆验证开关");
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端关闭登陆验证。", qq));
////
////        }
////    }
////
////    private static void 关闭全服决斗(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2014";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2014);
////                ps.setString(2, "全服决斗开关");
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端关闭全服决斗。", qq));
////            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息]:服务端关闭全服决斗。").getBytes());
////        }
////    }
////
////    private static void 禁止游戏喇叭(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2009";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2009);
////                ps.setString(2, "游戏喇叭开关");
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:禁止玩家在游戏中发送喇叭。", qq));
////        }
////    }
////
////    private static void 允许游戏喇叭(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2009";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2009);
////                ps.setString(2, "游戏喇叭开关");
////                ps.setInt(3, 0);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:允许玩家在游戏中发送喇叭。", qq));
////        }
////    }
////
////    private static void 禁止玩家聊天(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2024";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2024);
////                ps.setString(2, "玩家聊天开关");
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:禁止玩家在游戏中聊天。", qq));
////        }
////    }
////
////    private static void 允许玩家聊天(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2024";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2024);
////                ps.setString(2, "玩家聊天开关");
////                ps.setInt(3, 0);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////
////            } catch (SQLException ex) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:允许玩家在游戏中聊天。", qq));
////        }
////    }
////
////    private static void 启动服务端(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            try {
////
////                Start.instance.startServer();
////
////            } catch (InterruptedException ex) {
////                Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
////            } catch (UnsupportedEncodingException ex) {
////                Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
////            } catch (IOException ex) {
////                Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端开始释放服务器内存。", qq));
////            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息]:服务端开始释放服务器内存。").getBytes());
////        }
////    }
////
////    private boolean 网速检测2() {
////        boolean connect = false;
////        Runtime runtime = Runtime.getRuntime();
////        Process process;
////        String 输出 = "";
////        String 输出1 = "";
////        try {
////            process = runtime.exec("ping " + "" + 测速网速 + "");
////            InputStream is = process.getInputStream();
////            InputStreamReader isr = new InputStreamReader(is);
////            BufferedReader br = new BufferedReader(isr);
////            String line = null;
////            StringBuffer sb = new StringBuffer();
////            while ((line = br.readLine()) != null) {
////                sb.append(line);
////            }
////            is.close();
////            isr.close();
////            br.close();
////
////            if (null != sb && !sb.toString().equals("")) {
////                String logString = "";
////                if (sb.toString().indexOf("TTL") > 0) {
////                    //JOptionPane.showMessageDialog(null, "延迟 " + sb.toString().indexOf("TTL") + "\r\n服务器网速高于延迟 10 ，无法启动。");
////                    //输出 = "延迟 " + sb.toString().indexOf("TTL") + "";
////                } else {
////                    JOptionPane.showMessageDialog(null, "延迟 " + sb.toString().indexOf("TTL") + "\r\n服务器网速高于延迟 10 ，无法启动。");
////                    System.exit(0);
////                    return false;
////                }
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return connect;
////    }
////
////    private static void 运行状态(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            /*int 热度 = MapleParty.角色登陆次数;
////            String 热度显示 = "";
////            if (热度 > 100) {
////                热度显示 = "★★★★★";
////            } else if (热度 > 70) {
////                热度显示 = "★★★★☆";
////            } else if (热度 > 40) {
////                热度显示 = "★★★☆☆";
////            } else if (热度 > 20) {
////                热度显示 = "★★☆☆☆";
////            } else {
////                热度显示 = "★☆☆☆☆";
////            }*/
////            long start = System.currentTimeMillis();
////            Runtime runtime = Runtime.getRuntime();
////            Process process;
////            try {
////                process = runtime.exec("ping " + "www.baidu.com");
////                InputStream is = process.getInputStream();
////                InputStreamReader isr = new InputStreamReader(is);
////                BufferedReader br = new BufferedReader(isr);
////                String line = null;
////                StringBuffer sb = new StringBuffer();
////                while ((line = br.readLine()) != null) {
////                    sb.append(line);
////                }
////                is.close();
////                isr.close();
////                br.close();
////                if (null != sb && !sb.toString().equals("")) {
////                    long now = System.currentTimeMillis() - start;
////                    long seconds = now / 1000;
////                    long ms = now % 1000;
////                    if (sb.toString().indexOf("TTL") > 0) {
////                        sendMsgToQQGroup(String.format("<" + 开服名字 + ">状态监测\r\n"
////                                //+ "当前热度: " + 热度显示 + "\r\n"
////                                + "当前版本: v" + 版本 + "\r\n"
////                                + "频道数量: " + ServerProperties.getProperty("ZEV.Count") + "\r\n"
////                                + "网络延迟: " + sb.toString().indexOf("TTL") + "\r\n"
////                                + "响应速度: " + seconds + "." + ms + "\r\n"
////                                // + "连接次数: " + MapleParty.玩家登陆次数 + " 次\r\n"
////                                + "此次运行: " + MapleParty.服务端运行时长 + " 分钟\r\n"
////                                + "经验倍率: " + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp")) + " 倍\r\n"
////                                + "金币倍率: " + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso")) + " 倍\r\n"
////                                + "爆物倍率: " + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop")) + " 倍"
////                                + "", qq));
////                    }
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
////    }
////    //+ "本地时间: " + CurrentReadable_Time() + "\r\n"
////    //+ "网络时间: " + getWebsiteDatetime(获取网络时间) + "\r\n"MapleParty.角色登陆次数
////
////    private static String getWebsiteDatetime(String webUrl) {
////        try {
////            URL url = new URL(webUrl);// 取得资源对象
////            URLConnection uc = url.openConnection();// 生成连接对象
////            uc.connect();// 发出连接
////            long ld = uc.getDate();// 读取网站日期时间
////            Date date = new Date(ld);// 转换为标准时间对象
////            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
////            return sdf.format(date);
////        } catch (MalformedURLException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
////
////    private static void 释放内存(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            System.gc();
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端开始释放服务器内存。", qq));
////            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息]:服务端开始释放服务器内存。").getBytes());
////        }
////    }
////    private static ScheduledFuture<?> ts = null;
////    private int minutesLeft = 0;
////    private static Thread t = null;
////
////    private void 关闭服务端(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
//////开始保存第一次雇佣
////            int p = 0;
////            for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
////                p++;
////                cserv.closeAllMerchant();
////            }
////            //开始保存第二次雇佣
////            for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
////                p++;
////                cserv.closeAllMerchant();
////            }
////            //开始保存第三次雇佣
////            for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
////                p++;
////                cserv.closeAllMerchant();
////            }
////            //开始保存玩家数据
////            int pp = 0;
////            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
////                    pp++;
////                    chr.saveToDB(true, true);
////                }
////            }
////            //开始第二次保存玩家数据
////            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
////                    pp++;
////                    chr.saveToDB(true, true);
////                }
////            }
////            //开始第三次保存玩家数据
////            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
////                    pp++;
////                    chr.saveToDB(true, true);
////                }
////            }
////            //切断所有链接
////            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////                cserv.getPlayerStorage().断所有人(true);
////            }
////            //切断所有链接
////            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////                cserv.getPlayerStorage().断所有人(true);
////            }
////            //开始关闭服务端
////            try {
////                minutesLeft = 0;
////                if (ts == null && (t == null || !t.isAlive())) {
////                    t = new Thread(ShutdownServer.getInstance());
////                    ts = Timer.EventTimer.getInstance().register(new Runnable() {
////
////                        @Override
////                        public void run() {
////                            if (minutesLeft == 0) {
////                                ShutdownServer.getInstance();
////                                t.start();
////                                ts.cancel(false);
////                                return;
////                            }
////                            minutesLeft--;
////                        }
////                    }, 60000);
////                }
////            } catch (Exception e) {
////
////            }
////        }
////    }
////
////    private static void 查看巅峰榜(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            String 最高等级玩家 = NPCConversationManager.获取最高等级玩家名字();
////            int 最高等级 = NPCConversationManager.获取最高玩家等级();
////            String 最高人气玩家 = NPCConversationManager.获取最高人气玩家名字();
////            int 最高人气 = NPCConversationManager.获取最高玩家人气();
////            String 最高在线玩家 = NPCConversationManager.获取最高在线玩家名字();
////            int 最高在线 = NPCConversationManager.获取最高玩家在线();
////            String 今日在线玩家 = NPCConversationManager.获取今日在线玩家名字();
////            String 最强家族 = NPCConversationManager.获取最强家族名称();
////            sendMsgToQQGroup("<" + 开服名字 + "巅峰榜>\r\n"
////                    + "最高等级：" + 最高等级玩家 + "\r\n"
////                    + "最佳人气：" + 最高人气玩家 + "\r\n"
////                    + "最强家族：" + 最强家族 + "\r\n"
////                    + "在线时长(今)：" + 今日在线玩家 + "\r\n"
////                    + "在线时长(总)：" + 最高在线玩家
////            );
////        }
////    }
////
////    private static void 数据存档(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端开始保存玩家数据。", qq));
////            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息]:服务端开始保存玩家数据。").getBytes());
////            int ppl = 0;
////            try {
////                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
////                    for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
////                        if (chr == null) {
////                            continue;
////                        }
////                        ppl++;
////                        chr.saveToDB(false, false);
////                    }
////                }
////            } catch (Exception e) {
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:服务端保存玩家数据完成。", qq));
////            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息]:服务端保存玩家数据完成。").getBytes());
////        }
////    }
////
////    private static void 关闭巅峰广播(final String qq) {
////
////        //String 机器人主人 = Integer.parseInt(server.Start.ConfigValuesMap.get("机器人主人QQ"));
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2035";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2035);
////                ps.setString(2, "机器人巅峰广播开关");
////                ps.setInt(3, 1);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////                sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:巅峰广播关闭成功。", qq));
////            } catch (SQLException ex) {
////            }
////        }
////    }
////
////    private static void 开启巅峰广播(final String qq) {
////        // 先判断QQ账号是否已经注册
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            PreparedStatement ps1 = null;
////            ResultSet rs = null;
////            try {
////                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
////                ps1.setInt(1, 1);
////                rs = ps1.executeQuery();
////                if (rs.next()) {
////                    String sqlstr = " delete from configvalues where id =2035";
////                    ps1.executeUpdate(sqlstr);
////                }
////            } catch (SQLException ex) {
////            }
////            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
////                ps.setInt(1, 2035);
////                ps.setString(2, "机器人巅峰广播开关");
////                ps.setInt(3, 0);
////                ps.executeUpdate();
////                server.Start.GetConfigValues();
////                sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:巅峰广播开启成功。", qq));
////            } catch (SQLException ex) {
////            }
////        }
////    }
////
////    private static void 注册账号(final String qq) {
////
////        if (MapleParty.机器人待机 > 0) {
////            return;
////        }
////        注册白名单 itemjc = new 注册白名单();
////        String items1 = itemjc.getQQ();
////        if (!items1.contains("," + qq + ",")) {
////            try {
////                if (Start.CloudBlacklist.containsKey(qq)) {
////                    sendMsgToQQGroup(String.format("<<" + 开服名字 + "云防系统>>"
////                            + "：检测到你有使用非法插件，恶意破坏游戏平衡的不良记录，或者你的账号是辅助外挂群的成员，你的此次注册已经被拦截，已被禁止登陆，或者注册本端一切服务区域，如有疑问，请联系管理员。", qq));
////                    return;
////                }
////            } catch (Exception ex) {
////                System.err.println("拦截黑名单玩家出错：" + ex.getMessage());
////            }
////        }
////        注册黑名单 itemjc2 = new 注册黑名单();
////        String items2 = itemjc2.getQQ();
////        if (items2.contains("," + qq + ",")) {
////            sendMsgToQQGroup(String.format("<<" + 开服名字 + "黑名单系统>>"
////                    + "你的账号已被禁，如有疑问，请联系管理员。", qq));
////            return;
////        }
////        // 先判断QQ账号是否已经注册
////        Boolean isExists = AutoRegister.getAccountExists(qq);
////        if (isExists) {
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:你的QQ %s 已经注册过了,一个QQ只能注册一个账号，请珍惜账号。", qq));
////            return;
////        }
////
////        try {
////            Connection con = DatabaseConnection.getConnection();
////            PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, qq) VALUES (?, ?, ?, ?, ?)");
////
////            try {
////                ps.setString(1, qq);
////                ps.setString(2, LoginCrypto.hexSha1(qq));
////                ps.setString(3, String.format("%s@qq.com", qq));
////                ps.setString(4, "2018-11-11");
////                ps.setString(5, qq);
////                ps.executeUpdate();
////            } finally {
////                ps.close();
////            }
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:恭喜你，QQ %s 注册成功！请私聊我，使用“*修改密码”命令更新密码。", qq));
////            sendMsg("<" + 开服名字 + ">机器人:你好，请将你要设置的密码以[*修改密码]<空格>[密码]格式发给我，我将为您修改密码。", qq);
////            //sendMsgToAdminQQ(String.format("<" + 开服名字 + ">机器人:QQ %s 注册成功。", qq));
////        } catch (SQLException ex) {
////            System.out.println(ex);
////            sendMsgToQQGroup(String.format("<" + 开服名字 + ">机器人:对不起，QQ %s 注册失败！", qq));
////            //sendMsgToAdminQQ(String.format("<" + 开服名字 + ">机器人:QQ %s 注册失败。\n异常：%s", qq, ex.getMessage()));
////        }
////    }
////
////    private static final String[] 提示语 = {
////        "你是不是傻？你账号里面没有角色要我怎么查？ ",
////        "是你傻还是我傻，我看绝对你傻，先进游戏创个角色，不然我怎么查？ ",
////        "没角色怎么查？",
////        "总有哈嘛屁没有角色也要让我查询角色？ ",
////        "来来来，你告诉我怎么查询吧？没角色，查个卵。 ",
////        "你的智商已经欠费，无法查询角色。 "
////    };
////
////    private static void 查询角色(final String qq) {
////        if (MapleParty.机器人待机 > 0) {
////            return;
////        }
////        StringBuilder sb = new StringBuilder();
////
////        int count = 0;
////        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT c.`name`,c.`id`, c.`level`, c.`str`, c.`dex`, c.`luk`, c.`int`, c.`job`, c.`exp`, c.`fame`, c.`maxhp`, c.`maxmp`, c.`pvpkills`, c.`pvpdeaths`, c.`todayOnlineTime`, c.`totalOnlineTime` FROM accounts as a, characters as c WHERE a.id = c.accountid AND a.qq = ?")) {
////            ps.setString(1, qq);
////            try (ResultSet rs = ps.executeQuery()) {
////                while (rs.next()) {
////                    sb.append(String.format(""
////                            + "<" + 开服名字 + ">\r\n"
////                            + "角色: %s\n"
////                            + " id : %s\n"
////                            + "职业: %s\n"
////                            + "等级: %s\n"
////                            + "经验: %s\n"
////                            + "人气: %s\n"
////                            + "力量: %s\n"
////                            + "敏捷: %s\n"
////                            + "运气: %s\n"
////                            + "智力: %s\n"
////                            + "击败: %s \n"
////                            + "被击败: %s\n"
////                            + "生命值: %s\n"
////                            + "魔力值: %s\n"
////                            + "今日在线: %s \n"
////                            + "总共在线: %s \n", rs.getString("name"),
////                            rs.getString("id"),
////                            getJobNameById(rs.getInt("job")),
////                            rs.getString("level"),
////                            rs.getString("exp"),
////                            rs.getString("fame"),
////                            rs.getString("str"),
////                            rs.getString("dex"),
////                            rs.getString("luk"),
////                            rs.getString("int"),
////                            rs.getString("pvpkills"),
////                            rs.getString("pvpdeaths"),
////                            rs.getString("maxhp"),
////                            rs.getString("maxmp"),
////                            rs.getString("todayOnlineTime"),
////                            rs.getString("totalOnlineTime")));
////                    count++;
////                }
////            }
////        } catch (SQLException ex) {
////            System.out.println(ex);
////        }
////        /*if (count == 0) {
////            sendMsgToQQGroup("" + 提示语[Randomizer.nextInt(提示语.length)]);
////        }*/
////        //String info = String.format("账号: %s 共有%s个角色\n----------------------------\n", qq, count);
////        /*String info = String.format("账号: %s 共有%s个角色\n----------------------------\n", qq, count);
////        sb.insert(0, info);*/
////        sendMsgToQQGroup(sb.toString());
////    }
////
////    private static void 卡发型自救(final String qq, final String newPassword, final String token) {
////        try {
////            Connection con = DatabaseConnection.getConnection();
////            PreparedStatement pss = con.prepareStatement("UPDATE `accounts` SET `hair` = ?, `salt` = ? WHERE qq = ?");
////            try {
////                final String newSalt = LoginCrypto.makeSalt();
////                pss.setString(1, LoginCrypto.makeSaltedSha512Hash(newPassword, newSalt));
////                pss.setInt(2, 30000);
////                pss.setString(3, qq);
////                int res = pss.executeUpdate();
////                if (res > 0) {
////                    sendMsg("<" + 开服名字 + ">机器人:恭喜你，密码修改成功！在游戏中也可以找小z修改密码哦！", token);
////                } else {
////                    sendMsg("<" + 开服名字 + ">机器人:没有找到你的QQ对应的账号，密码修改失败！", token);
////                }
////            } finally {
////                pss.close();
////            }
////        } catch (SQLException e) {
////            System.err.println("修改密码出错。" + e);
////        }
////    }
////
////    private static void 修改密码(final String qq, final String newPassword, final String token) {
////        if (MapleParty.机器人待机 > 0) {
////            return;
////        }
////        /* if (server.Start.ConfigValuesMap.get("机器人修改密码开关") > 0) {
////            sendMsg("<" + 开服名字 + ">机器人:管理员从后台关闭了修改密码的功能。", token);
////            return;
////        }*/
////        if (!newPassword.matches("^[0-9A-Za-z]{6,10}$")) {
////            sendMsg("<" + 开服名字 + ">机器人:新密码不合格，必须由6-10位数字或字母组成。", token);
////            return;
////        }
////
////        try {
////            Connection con = DatabaseConnection.getConnection();
////            PreparedStatement pss = con.prepareStatement("UPDATE `accounts` SET `password` = ?, `salt` = ? WHERE qq = ?");
////            try {
////                final String newSalt = LoginCrypto.makeSalt();
////                pss.setString(1, LoginCrypto.makeSaltedSha512Hash(newPassword, newSalt));
////                pss.setString(2, newSalt);
////                pss.setString(3, qq);
////                int res = pss.executeUpdate();
////                if (res > 0) {
////                    sendMsg("<" + 开服名字 + ">机器人:恭喜你，密码修改成功！在游戏中也可以找小z修改密码哦！", token);
////                    FileoutputUtil.logToFile("机器人信息记录/" + CurrentReadable_Date() + "/修改密码指令[成功]/" + qq + ".txt", "[时间：" + CurrentReadable_Time() + "]账号：[" + qq + "]修改密码：[" + newPassword + "]\r\n");
////                } else {
////                    sendMsg("<" + 开服名字 + ">机器人:没有找到你的QQ对应的账号，密码修改失败！", token);
////                    FileoutputUtil.logToFile("机器人信息记录/" + CurrentReadable_Date() + "/修改密码指令[失败]/" + qq + ".txt", "[时间：" + CurrentReadable_Time() + "]账号：[" + qq + "] 修改失败，未注册账号修改密码\r\n");
////                }
////            } finally {
////                pss.close();
////            }
////        } catch (SQLException e) {
////            System.err.println("修改密码出错。" + e);
////        }
////    }
////
////    private static void 服务端倍率() {
////        if (MapleParty.机器人待机 > 0) {
////            return;
////        }
////        if ("开".equals(ServerProperties.getProperty("ZEV.ZEV"))) {
////            sendMsgToQQGroup(""
////                    + "<" + 开服名字 + ">"
////                    + "\r\n[勇者区域]:"
////                    + "\r\n经验倍率 :" + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp"))
////                    + "\r\n金币倍率 :" + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso"))
////                    + "\r\n爆物倍率 :" + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop"))
////                    + "\r\n[萌新区域]:"
////                    + "\r\n经验倍率 :" + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp2"))
////                    + "\r\n金币倍率 :" + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso2"))
////                    + "\r\n爆物倍率 :" + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop2"))
////            );
////        } else {
////            sendMsgToQQGroup(""
////                    + "<" + 开服名字 + ">"
////                    + "\r\n经验倍率 :" + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp"))
////                    + "\r\n金币倍率 :" + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso"))
////                    + "\r\n爆物倍率 :" + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop"))
////            );
////        }
////    }
////    private List<RespawnInfo> respawnInfolist = new ArrayList<>();
////
////    public void reloadSpawn() {
////
////        respawnInfolist.clear();
////        //respawnInfolist.add(new RespawnInfo(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城地图7")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城怪物7")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城数量7")), new Point(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城x坐标7")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城y坐标7"))), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城频道7")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻生命7")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城经验7")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城星期7")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城小时7")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城分钟7"))));
////        //respawnInfolist.add(new RespawnInfo(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城地图6")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城怪物6")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城数量6")), new Point(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城x坐标6")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城y坐标6"))), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城频道6")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻生命6")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城经验6")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城星期6")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城小时6")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城分钟6"))));
////        respawnInfolist.add(new RespawnInfo(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城地图5")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城怪物5")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城数量5")), new Point(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城x坐标5")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城y坐标5"))), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城频道5")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻生命5")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城经验5")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城星期5")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城小时5")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城分钟5"))));
////        respawnInfolist.add(new RespawnInfo(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城地图4")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城怪物4")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城数量4")), new Point(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城x坐标4")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城y坐标4"))), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城频道4")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻生命4")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城经验4")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城星期4")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城小时4")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城分钟4"))));
////        respawnInfolist.add(new RespawnInfo(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城地图2")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城怪物2")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城数量2")), new Point(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城x坐标2")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城y坐标2"))), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城频道2")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻生命2")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城经验2")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城星期2")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城小时2")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城分钟2"))));
////        respawnInfolist.add(new RespawnInfo(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城地图1")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城怪物1")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城数量1")), new Point(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城x坐标1")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城y坐标1"))), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城频道1")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻生命1")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城经验1")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城星期1")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城小时1")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城分钟1"))));
////        respawnInfolist.add(new RespawnInfo(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城地图3")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城怪物3")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城数量3")), new Point(Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城x坐标3")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城y坐标3"))), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城频道3")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻生命3")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城经验3")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城星期3")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城小时3")), Integer.parseInt(ServerProperties.getProperty("ZEV.怪物攻城分钟3"))));
////
////        //respawnInfolist.add(new RespawnInfo(地图, 怪物, 数量, new Point(坐标X, 坐标Y), 频道, 怪物生命, 怪物经验, 星期, 时, 分));
////    }
////
////    public void 怪物攻城(final String qq) {
////        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
////            if (MapleParty.机器人待机 > 0) {
////                return;
////            }
////            Calendar calendar = Calendar.getInstance();
////            for (RespawnInfo respawnInfo : respawnInfolist) {
////
////                //ChannelServer channelServer= ChannelServer.getInstance(Randomizer.nextInt(ChannelServer.getChannelCount())+1);
////                ChannelServer channelServer = ChannelServer.getInstance(respawnInfo.getChannelId());
////                if (channelServer == null) {
////                    System.out.println("spawnInTime:不存在的频道,ID" + respawnInfo.getChannelId());
////                    continue;
////                }
////                MapleMap mapleMap = channelServer.getMapFactory().getMap(respawnInfo.getMapId());
////                if (mapleMap == null) {
////                    System.out.println("spawnInTime:不存在的地图,ID" + respawnInfo.getMapId());
////                    continue;
////                }
////                MapleMonster mobName = MapleLifeFactory.getMonster(respawnInfo.getMobId());
////                if (mobName == null) {
////                    System.out.println("spawnInTime:不存在的怪物,ID" + respawnInfo.getMobId());
////                    continue;
////                }
////                for (int i = 0; i < respawnInfo.getMobCount(); i++) {
////                    MapleMonster mapleMonster = MapleLifeFactory.getMonster(respawnInfo.getMobId());
////                    if (respawnInfo.getHp() > 0) {
////                        mapleMonster.setHp(respawnInfo.getHp());
////                    }
////                    if (respawnInfo.getExp() > 0) {
////                        mapleMonster.setOverrideStats(new OverrideMonsterStats(mapleMonster.getHp(), mapleMonster.getMp(), respawnInfo.getExp()));
////                    }
////                    mapleMonster.setPosition(respawnInfo.getSpawnPoint());
////                    mapleMap.spawnMonster(mapleMonster, -2);
////                }
////            }
////        }
////    }
////}
////*/