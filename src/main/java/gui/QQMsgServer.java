/*
QQ机器人
 */
package gui;

import static a.本地数据库.查询QQ下是否有封禁账号;
import static a.用法大全.取账号绑定的QQ;
import static a.用法大全.角色ID取角色名;
import static a.用法大全.角色ID取账号ID;
import static a.用法大全.账号ID取账号;
import abc.注册白名单;
import abc.注册黑名单;
import client.LoginCrypto;
import client.MapleCharacter;
import client.inventory.Equip;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import database1.DatabaseConnection1;
import static gui.QQ通信.通信;
import static gui.Start.CashShopServer;
import static gui.控制台.快捷面板.账号ID取绑定QQ;
import gui.控制台.控制台1号;
import handling.channel.ChannelServer;
import handling.login.handler.AutoRegister;
import handling.world.MapleParty;
import handling.world.World;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static scripting.NPCConversationManager.变更银行点券;
import static scripting.NPCConversationManager.变更银行金币;
import static scripting.NPCConversationManager.角色名字取ID;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.ServerProperties;
import static scripting.NPCConversationManager.账号取绑定QQ;
import static scripting.NPCConversationManager.银行开户人;
import static scripting.NPCConversationManager.银行点券余额;
import static scripting.NPCConversationManager.银行账号;
import static scripting.NPCConversationManager.银行账号密码;
import static scripting.NPCConversationManager.银行金币余额;
import server.maps.MapleMap;
import tools.FileoutputUtil;
import static tools.FileoutputUtil.CurrentReadable_Time;
import tools.MaplePacketCreator;

/**
 *
 * @author 时代先锋
 */
public class QQMsgServer implements Runnable {

    public static void main(String args[]) {
        new Thread(QQMsgServer.getInstance()).start();
         System.err.println("QQ");
    }
    private static final int INPORT = 9001;
    private static final int PeerPort = 9000;

    private byte[] buf = new byte[1024];
    private DatagramPacket dp = new DatagramPacket(buf, buf.length);
    private static DatagramSocket socket;
    private static final QQMsgServer instance = new QQMsgServer();

    public static QQMsgServer getInstance() {
        return instance;

    }
    public static int 冷却 = 0;
    public static int 冷却时间 = 5;

    private QQMsgServer() {
        try {
            socket = new DatagramSocket(INPORT);
        } catch (SocketException e) {
            System.err.println("无法开启，端口被占用");
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                socket.receive(dp);
                // 接收到来自QQ机器人的消息
                String rcvd = new String(dp.getData(), 0, dp.getLength());
                //System.out.println("QQ: " + rcvd);
                String msgArr[] = rcvd.split("_");
                String msgType = msgArr[0];
                if (msgType.equals("P")) { // 私人
                    int index = msgType.length() + 1;
                    String fromQQ = msgArr[1];
                    index += fromQQ.length() + 1;
                    String token = msgArr[2];
                    index += token.length() + 1;
                    String msg[] = rcvd.substring(index).trim().split("\\s+");
                    switch (msg[0]) {
                        case "*查询银行余额":
                            if (msg.length > 2) {
                                银行余额(fromQQ, msg[1], msg[2], token);
                            } else {
                                sendMsg("指令格式不正确\r\n*查询银行余额<空格>[卡号]<空格>[密码]。", token);
                            }
                            break;
                        case "*转账金币":
                        case "*转帐金币":
                            if (msg.length > 2) {
                                转账金币(fromQQ, msg[1], msg[2], msg[3], msg[4], token);
                            } else {
                                sendMsg("指令格式不正确\r\n*转账金币<空格>[卡号]<空格>[密码]<空格>[转账卡号]<空格>[金额]。", token);
                            }
                            break;
                        case "*转账点券":
                        case "*转帐点券":
                            if (msg.length > 2) {
                                转账点券(fromQQ, msg[1], msg[2], msg[3], msg[4], token);
                            } else {
                                sendMsg("指令格式不正确\r\n*转账点券<空格>[卡号]<空格>[密码]<空格>[转账卡号]<空格>[金额]。", token);
                            }
                            break;
                        case "*指令大全":
                        case "*帮助":
                        case "*菜单":
                            指令大全(fromQQ, token);
                            break;
                        case "*注册账号":
                        case "*注册帐号":
                            if (msg.length > 1) {
                                注册账号(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确\r\n*注册账号<空格>[账号]。", token);
                            }
                            break;
                        case "*我的账号":
                        case "*我的帐号":
                            查询账号(fromQQ, token);
                            break;
                        case "*查询账号":
                        case "*查询帐号":
                            if (msg.length > 1) {
                                查询账号2(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确\r\n*查询账号<空格>[账号]。", token);
                            }
                            break;
                        case "*修改密码":
                        case "*修改尼玛":
                            if (msg.length > 2) {
                                修改密码(fromQQ, msg[1], msg[2], token);
                            } else {
                                sendMsg("指令格式不正确\r\n*修改密码<空格>[账号]<空格>[密码]。", token);
                            }
                            break;

                        case "*商城出售通知":
                            if (msg.length > 1) {
                                商城出售通知(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确。", token);
                            }
                            break;
                        case "*全服决斗":
                            if (msg.length > 1) {
                                全服决斗(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确。", token);
                            }
                            break;
                        case "*机器人状态":
                            if (msg.length > 1) {
                                机器人状态(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确。", token);
                            }
                            break;
                        case "*解卡发型":
                            if (msg.length > 1) {
                                解卡发型(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确。", token);
                            }
                            break;
                        case "*删除角色":
                            if (msg.length > 1) {
                                删除角色(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确。", token);
                            }
                            break;
                        case "*重启数据库":
                            重启数据库(fromQQ, token);
                            break;
                        case "*在线":
                        case "*在线角色":
                        case "*在线玩家":
                            在线玩家(fromQQ, token);
                            break;
                        case "*在线雇佣":
                        case "*在线商人":
                        case "*雇佣商人":
                            在线雇佣(fromQQ, token);
                            break;
                        case "*封号":
                            封号(fromQQ, msg[1], token);
                            break;

                        case "*解卡脸型":
                            if (msg.length > 1) {
                                解卡脸型(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确。", token);
                            }
                            break;
                        case "*删除异常物品":
                            if (msg.length > 1) {
                                删除异常物品(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确。", token);
                            }
                            break;
                        case "*解卡地图1":
                            if (msg.length > 1) {
                                解卡地图1(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确。", token);
                            }
                            break;
                        case "*解卡地图2":
                            if (msg.length > 1) {
                                解卡地图2(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式不正确。", token);
                            }
                            break;
                        case "*经验活动":
                            if (msg.length > 2) {
                                经验活动(fromQQ, msg[1], msg[2], token);
                            } else {
                                sendMsg("指令格式：[指令]<空格>[倍率]<空格>[持续时间]。", token);
                            }
                            break;
                        case "*爆率活动":
                            if (msg.length > 2) {
                                爆率活动(fromQQ, msg[1], msg[2], token);
                            } else {
                                sendMsg("指令格式：[指令]<空格>[倍率]<空格>[持续时间]。", token);
                            }
                            break;
                        case "*金币活动":
                            if (msg.length > 2) {
                                金币活动(fromQQ, msg[1], msg[2], token);
                            } else {
                                sendMsg("指令格式：[指令]<空格>[倍率]<空格>[持续时间]。", token);
                            }
                            break;
                        case "*呼出后台":
                            if (msg.length > 2) {
                                呼出后台(fromQQ, token);
                            }
                            break;
                        case "*给全服发点券":
                            if (msg.length > 1) {
                                给全服发点券(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式：[指令]<空格>[数量]。", token);
                            }
                            break;
                        case "*给全服发经验":
                            if (msg.length > 1) {
                                给全服发经验(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式：[指令]<空格>[数量]。", token);
                            }
                            break;
                        case "*给全服发金币":
                            if (msg.length > 1) {
                                给全服发金币(fromQQ, msg[1], token);
                            } else {
                                sendMsg("指令格式：[指令]<空格>[数量]。", token);
                            }
                            break;
                        case "*给全服发物品":
                            if (msg.length > 2) {
                                给全服发物品(fromQQ, msg[1], msg[2], token);
                            } else {
                                sendMsg("指令格式：[指令]<空格>[物品代码]<空格>[数量]。", token);
                            }
                            break;
                        case "*录题":
                            if (msg.length > 2) {
                                录题(fromQQ, msg[1], msg[2], token);
                            } else {
                                sendMsg("指令格式：[指令]<空格>[题目]<空格>[O/X]。", token);
                            }
                            break;
                        default: // 正常聊天
                            break;
                    }
                }
//                else if (msgType.equals("G")) { // 群组
//                    int index = msgType.length() + 1;
//                    String fromGroup = msgArr[1];
//                    index += fromGroup.length() + 1;
//                    String fromQQ = msgArr[2];
//                    index += fromQQ.length() + 1;
//                    String nickName = msgArr[3];
//                    index += nickName.length() + 1;
//                    String msg = rcvd.substring(index).trim();
//                    switch (msg) {
//                        default:
//                            break;
//                    }
//                }
            }
        } catch (SocketException e) {
            System.err.println("Can't open socket");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("UdpHost init error");
            e.printStackTrace();
        }
    }

    public static void sendMsg(final String msg, final String token) {
        try {
            String data = String.format("P_%s_%s", token, msg);
            byte[] buf = data.getBytes();
            //System.out.println("[->qq] : " + new String(buf));
            DatagramPacket echo = new DatagramPacket(buf, buf.length, InetAddress.getLoopbackAddress(), PeerPort);
            socket.send(echo);
        } catch (IOException e) {
            System.err.println("sendMsgToQQ error");
        }
    }

    public static void sendMsgToAdminQQ(final String msg) {
        try {
            String data = "A_" + msg;
            byte[] buf = data.getBytes();
            //System.out.println("[->admin qq] : " + new String(buf));
            DatagramPacket echo = new DatagramPacket(buf, buf.length, InetAddress.getLoopbackAddress(), PeerPort);
            socket.send(echo);
        } catch (IOException e) {
            System.err.println("sendMsgToAdminQQ error");
        }
    }

    public static void sendMsgToQQGroup(final String msg) {
        try {
            String data = "G_" + msg;
            byte[] buf = data.getBytes();
            DatagramPacket echo = new DatagramPacket(buf, buf.length, InetAddress.getLoopbackAddress(), PeerPort);
            socket.send(echo);
        } catch (IOException e) {
            System.err.println("sendMsgToQQGroup error");
        }

    }

    private static int sendToOnlinePlayer(final String fromQQ, final String msg) {

        int count = 0;
        if (MapleParty.机器人待机 > 0) {
            return count;
        }
        if (MapleParty.信息同步 > 0) {
            return count;
        }
        for (ChannelServer chl : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : chl.getPlayerStorage().getAllCharacters()) {
                if (chr == null) {
                    continue;
                }
                if (gui.Start.个人信息设置.get("" + chr.getName() + "10") != null) {
                    if (gui.Start.个人信息设置.get("" + chr.getName() + "10") == 0) {
                        chr.dropMessage(String.format("[群][%s] : %s", fromQQ, msg));
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static void 解卡发型(final String qq, final String account, final String token) {
        System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）解卡发型 √ " + account);
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        boolean result = account.matches("[0-9]+");
        if (!result) {
            sendMsg("请输入数字ID。", token);
            return;
        }
        Boolean isExists = AutoRegister.判断角色ID是否存在(account);
        if (!isExists) {
            sendMsg("你输入的角色ID不存在。", token);
            return;
        }
        if (!ServerProperties.getProperty("ZEV.QQ1").equals(qq) || !ServerProperties.getProperty("ZEV.QQ2").equals(qq) || !"71447500".equals(qq)) {
            if (!账号取绑定QQ(账号ID取账号(角色ID取账号ID(account))).equals(token)) {
                sendMsg("你要解卡发型的角色不是属于你的。", token);
                return;
            }
        }
        if (World.Find.findChannel(角色ID取角色名(account)) > 0) {
            sendMsg("你的角色目前在线，请让角色离线后在使用。", token);
            return;
        }
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET hair = ? WHERE id = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
            ps1.setInt(1, Integer.parseInt(account));
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update characters set hair='30000' where id='" + Integer.parseInt(account) + "';";
                PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                hair.executeUpdate(sqlString1);
                ps.close();
                ps1.close();
                sendMsg("角色发型解卡成功阿。", token);
            }
        } catch (SQLException ex) {

        }
    }

    private static void 删除角色(final String qq, final String account, final String token) {
        System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）删除角色 √ " + account);
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        boolean result = account.matches("[0-9]+");
        if (!result) {
            sendMsg("请输入数字ID。", token);
            return;
        }
        Boolean isExists = AutoRegister.判断角色ID是否存在(account);
        if (!isExists) {
            sendMsg("你输入的角色ID不存在。", token);
            return;
        }
        if (!ServerProperties.getProperty("ZEV.QQ1").equals(qq) || !ServerProperties.getProperty("ZEV.QQ2").equals(qq) || !"71447500".equals(qq)) {
            if (!账号取绑定QQ(账号ID取账号(角色ID取账号ID(account))).equals(token)) {
                sendMsg("你要删除的角色不属于你。", token);
                return;
            }
        }
        if (World.Find.findChannel(角色ID取角色名(account)) > 0) {
            sendMsg("你的角色目前在线，请让角色离线后在使用。", token);
            return;
        }
        sendMsg("申请删除角色 " + 角色ID取角色名(account) + " 成功，角色将在 1 小时候被删除。", token);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 60 * 60 * 1);
                    sendMsg("你申请删除的角色 " + 角色ID取角色名(account) + " 已经被删除。", token);
                    删除角色(Integer.parseInt(account));
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

    private static void 解卡脸型(final String qq, final String account, final String token) {
        System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）解卡脸型 √ " + account);
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        boolean result = account.matches("[0-9]+");
        if (!result) {
            sendMsg("请输入数字ID。", token);
            return;
        }
        Boolean isExists = AutoRegister.判断角色ID是否存在(account);
        if (!isExists) {
            sendMsg("你输入的角色ID不存在。", token);
            return;
        }
        if (!ServerProperties.getProperty("ZEV.QQ1").equals(qq) || !ServerProperties.getProperty("ZEV.QQ2").equals(qq) || !"71447500".equals(qq)) {
            if (!账号取绑定QQ(账号ID取账号(角色ID取账号ID(account))).equals(token)) {
                sendMsg("你要解卡发型的角色不是属于你的。", token);
                return;
            }
        }
        if (World.Find.findChannel(角色ID取角色名(account)) > 0) {
            sendMsg("你的角色目前在线，请让角色离线后在使用。", token);
            return;
        }
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET face = ?WHERE id = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
            ps1.setInt(1, Integer.parseInt(account));
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update characters set face='20000' where id='" + Integer.parseInt(account) + "';";
                PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                hair.executeUpdate(sqlString1);
                ps.close();
                ps1.close();
                sendMsg("角色脸型解卡成功。", token);
            }
        } catch (SQLException ex) {

        }

    }

    private static void 删除异常物品(final String qq, final String account, final String token) {
        if (!ServerProperties.getProperty("ZEV.QQ1").equals(qq) || !ServerProperties.getProperty("ZEV.QQ2").equals(qq) || !"71447500".equals(qq)) {
            System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）删除异常物品 √ " + account);
            boolean result = account.matches("[0-9]+");
            if (!result) {
                sendMsg("请输入数字ID。", token);
                return;
            }
            Boolean isExists = AutoRegister.判断角色ID是否存在(account);
            if (!isExists) {
                sendMsg("你输入的角色ID不存在。", token);
                return;
            }
            if (World.Find.findChannel(角色ID取角色名(account)) > 0) {
                sendMsg("你的角色目前在线，请让角色离线后在使用。", token);
                return;
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                try (PreparedStatement ps = con.prepareStatement("SELECT *  FROM inventoryitems WHERE characterid =" + account + ""); ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if (MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")) == null) {
                            String sqlstr = " delete from inventoryitems where itemid = " + rs.getInt("itemid") + "";
                            sendMsg("删除不存在物品，已删除: " + rs.getInt("itemid"), token);
                            ps.executeUpdate(sqlstr);
                        }
                    }
                    ps.close();
                }

            } catch (SQLException Ex) {
            }

        }

    }

    private static void 解卡地图1(final String qq, final String account, final String token) {
        System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）解卡地图 √ " + account);
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        boolean result = account.matches("[0-9]+");
        if (!result) {
            sendMsg("请输入数字ID。", token);
            return;
        }
        Boolean isExists = AutoRegister.判断角色ID是否存在(account);
        if (!isExists) {
            sendMsg("你输入的角色ID不存在。", token);
            return;
        }
        if (!ServerProperties.getProperty("ZEV.QQ1").equals(qq) || !ServerProperties.getProperty("ZEV.QQ2").equals(qq) || !"71447500".equals(qq)) {
            if (!账号取绑定QQ(账号ID取账号(角色ID取账号ID(account))).equals(token)) {
                sendMsg("你要解卡发型的角色不是属于你的。", token);
                return;
            }
        }
        if (World.Find.findChannel(角色ID取角色名(account)) > 0) {
            sendMsg("你的角色目前在线，请让角色离线后在使用。", token);
            return;
        }
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET map = ?WHERE id = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
            ps1.setInt(1, Integer.parseInt(account));
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update characters set map='100000000' where id='" + Integer.parseInt(account) + "';";
                PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                hair.executeUpdate(sqlString1);
                ps.close();
                ps1.close();
                sendMsg("角色地图解卡成功。", token);
            }
        } catch (SQLException ex) {

        }
    }

    private static void 解卡地图2(final String qq, final String account, final String token) {
        System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）解卡地图 √ " + account);

        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        boolean result = account.matches("[0-9]+");
        if (!result) {
            sendMsg("请输入数字ID。", token);
            return;
        }
        Boolean isExists = AutoRegister.判断角色ID是否存在(account);
        if (!isExists) {
            sendMsg("你输入的角色ID不存在。", token);
            return;
        }
        if (!ServerProperties.getProperty("ZEV.QQ1").equals(qq) || !ServerProperties.getProperty("ZEV.QQ2").equals(qq) || !"71447500".equals(qq)) {
            if (!账号取绑定QQ(账号ID取账号(角色ID取账号ID(account))).equals(token)) {
                sendMsg("你要解卡发型的角色不是属于你的。", token);
                return;
            }
        }
        if (World.Find.findChannel(角色ID取角色名(account)) <= 0) {
            sendMsg("你的角色目前不在线，请让角色上线后在使用。", token);
            return;
        }
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter c : cserv.getPlayerStorage().getAllCharacters()) {
                if (c.getId() == Integer.parseInt(account)) {
                    c.changeMap(100000000, 1);
                    sendMsg("角色地图解卡成功。", token);
                }
            }
        }
    }

    private static void 封号(final String qq, final String account, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq)) {
            String 账号 = 账号ID取账号(角色ID取账号ID(角色名字取ID(account)));
            int id = 角色名字取ID(account);
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps;
                ps = con.prepareStatement("Update accounts set banned = ? Where name = ?");
                ps.setInt(1, 1);
                ps.setString(2, 账号);
                ps.execute();
                ps.close();
            } catch (Exception ex) {
            }

            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr == null) {
                        continue;
                    }
                    if (chr.getId() == id) {
                        chr.getClient().getSession().close();
                    }
                }
            }
        }
    }

    private static void 机器人状态(final String qq, final String account, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq)) {
            if (null != account) {
                switch (account) {
                    case "开":
                        MapleParty.机器人待机 = 0;
                        sendMsg("<" + MapleParty.开服名字 + ">：机器人开始工作。", token);
                        break;
                    case "关":
                        MapleParty.机器人待机 = 1;
                        sendMsg("<" + MapleParty.开服名字 + ">：机器人开始待机。", token);
                        break;
                    default:
                        sendMsg("<" + MapleParty.开服名字 + ">：请输入[开],[关]。", token);
                        break;
                }
            }
        }
    }

    private static void 商城出售通知(final String qq, final String account, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq)) {
            if (null != account) {
                switch (account) {
                    case "开":
                        MapleParty.商城出售通知 = 0;
                        sendMsg("<" + MapleParty.开服名字 + ">：开启商城物品出售通知。", token);
                        break;
                    case "关":
                        MapleParty.商城出售通知 = 1;
                        sendMsg("<" + MapleParty.开服名字 + ">：关闭商城物品出售通知。", token);
                        break;
                    default:
                        sendMsg("<" + MapleParty.开服名字 + ">：请输入[开],[关]。", token);
                        break;
                }
            }
        }
    }

    private static void 全服决斗(final String qq, final String account, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq)) {
            if (null != account) {
                switch (account) {
                    case "开":
                        按键开("全服决斗开关", 2014);
                        sendMsg("<" + MapleParty.开服名字 + ">：开启全服决斗。", token);
                        break;
                    case "关":
                        按键关("全服决斗开关", 2014);
                        sendMsg("<" + MapleParty.开服名字 + ">：关闭全服决斗。", token);
                        break;
                    default:
                        sendMsg("<" + MapleParty.开服名字 + ">：请输入[开],[关]。", token);
                        break;
                }
            }
        }
    }

    private static void 重启数据库(final String qq, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            sendMsg("<" + MapleParty.开服名字 + ">：开始重启数据库。", token);
            关闭数据库(qq, token);
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 5);
                        打开数据库(qq, token);
                        sendMsg("<" + MapleParty.开服名字 + ">：数据库重启完毕。", token);
                    } catch (InterruptedException e) {
                    }
                }
            }.start();

        }
    }

    private static void 打开数据库(final String qq, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            try {
                // 调用 cmd命令，执行 net start mysql， /c 必须要有
                Process p = Runtime.getRuntime().exec("cmd.exe /c net start MySQL5");
                InputStream inputStream = p.getInputStream();
                try ( // 获取命令执行完的结果
                        Scanner scanner = new Scanner(inputStream, "GBK")) {
                    scanner.useDelimiter("\\A");
                    while (scanner.hasNext()) {
                        builder.append(scanner.next());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void 关闭数据库(final String qq, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            try {
                // 调用 cmd命令，执行 net start mysql， /c 必须要有
                Process p = Runtime.getRuntime().exec("cmd.exe /c net stop MySQL5");
                InputStream inputStream = p.getInputStream();

                try ( // 获取命令执行完的结果
                        Scanner scanner = new Scanner(inputStream, "GBK")) {
                    scanner.useDelimiter("\\A");

                    while (scanner.hasNext()) {
                        builder.append(scanner.next());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void 经验活动(final String qq, final String b, final String a, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            int 原始经验 = Integer.parseInt(ServerProperties.getProperty("ZEV.Exp"));
            int 经验活动 = 原始经验 * Integer.parseInt(b);
            int seconds = 0;
            int mins = 0;
            int hours = Integer.parseInt(a);
            int time = seconds + (mins * 60) + (hours * 60 * 60);
            final String rate = "经验";
            World.scheduleRateDelay(rate, time);
            for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                cservs.setExpRate(经验活动);
            }
            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, 20, "[倍率活动] : 游戏开始 " + b + " 倍打怪经验活动，将持续 " + a + " 小时，请各位玩家狂欢吧！"));
            sendMsgToQQGroup(String.format("<" + MapleParty.开服名字 + ">：游戏开始 " + b + " 倍打怪经验活动，将持续 " + a + " 小时，请各位玩家狂欢吧！", qq));
            sendMsg("<" + MapleParty.开服名字 + ">：经验活动处理完毕。", token);
        }
    }

    private static void 爆率活动(final String qq, final String b, final String a, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            int 原始爆率 = Integer.parseInt(ServerProperties.getProperty("ZEV.Drop"));
            int 爆率活动 = 原始爆率 * Integer.parseInt(b);
            int seconds = 0;
            int mins = 0;
            int hours = Integer.parseInt(a);
            int time = seconds + (mins * 60) + (hours * 60 * 60);
            final String rate = "爆率";
            World.scheduleRateDelay(rate, time);
            for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                cservs.setDropRate(爆率活动);
            }
            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, 20, "[倍率活动] : 游戏开始 " + b + " 倍打怪爆率活动，将持续 " + a + " 小时，请各位玩家狂欢吧！"));
            sendMsgToQQGroup(String.format("<" + MapleParty.开服名字 + ">：游戏开始 " + b + " 倍打怪爆率活动，将持续 " + a + " 小时，请各位玩家狂欢吧！", qq));
            sendMsg("<" + MapleParty.开服名字 + ">：爆率活动处理完毕。", token);
        }
    }

    private static void 金币活动(final String qq, final String b, final String a, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            int 原始金币 = Integer.parseInt(ServerProperties.getProperty("ZEV.Drop"));
            int 金币活动 = 原始金币 * Integer.parseInt(b);
            int seconds = 0;
            int mins = 0;
            int hours = Integer.parseInt(a);
            int time = seconds + (mins * 60) + (hours * 60 * 60);
            final String rate = "金币";
            World.scheduleRateDelay(rate, time);
            for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                cservs.setMesoRate(金币活动);
            }
            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, 20, "[倍率活动] : 游戏开始 " + b + " 倍打怪金币活动，将持续 " + a + " 小时，请各位玩家狂欢吧！"));
            sendMsgToQQGroup(String.format("<" + MapleParty.开服名字 + ">：游戏开始 " + b + " 倍打怪金币活动，将持续 " + a + " 小时，请各位玩家狂欢吧！", qq));
            sendMsg("<" + MapleParty.开服名字 + ">：金币活动处理完毕。", token);
        }
    }

    private static void 给全服发点券(final String qq, final String a, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.modifyCSPoints(1, Integer.parseInt(a));
                    mch.startMapEffect("" + MapleParty.开服名字 + "管理员发放 " + a + " 点券给在线的所有玩家，祝您玩的开心玩的快乐！", 5120027);
                }
            }
            sendMsgToQQGroup(String.format("<" + MapleParty.开服名字 + ">：管理员发放 " + a + " 点券给在线的所有玩家，祝您玩的开心玩的快乐！", qq));
            sendMsg("<" + MapleParty.开服名字 + ">：给全服发送点券完成。", token);
        }
    }

    private static void 给全服发金币(final String qq, final String a, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.gainMeso(Integer.parseInt(a), true);
                    mch.startMapEffect("" + MapleParty.开服名字 + "管理员发放 " + a + " 金币给在线的所有玩家，祝您玩的开心玩的快乐！", 5120027);
                }
            }
            sendMsgToQQGroup(String.format("<" + MapleParty.开服名字 + ">：管理员发放 " + a + " 金币给在线的所有玩家，祝您玩的开心玩的快乐！", qq));
            sendMsg("<" + MapleParty.开服名字 + ">：给全服发送金币完成。", token);
        }
    }

    private static void 给全服发经验(final String qq, final String a, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.gainExp(Integer.parseInt(a), true, true, true);
                    mch.startMapEffect("" + MapleParty.开服名字 + "管理员发放 " + a + " 经验给在线的所有玩家，祝您玩的开心玩的快乐！", 5120027);
                }
            }
            sendMsgToQQGroup(String.format("<" + MapleParty.开服名字 + ">：管理员发放 " + a + " 经验给在线的所有玩家，祝您玩的开心玩的快乐！", qq));
            sendMsg("<" + MapleParty.开服名字 + ">：给全服发送经验完成。", token);
        }
    }

    private static void 给全服发物品(final String qq, final String a, final String b, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            sendMsgToQQGroup(String.format("<" + MapleParty.开服名字 + ">：管理员发放 " + MapleItemInformationProvider.getInstance().getName(Integer.parseInt(a)) + " x " + b + " 给在线的所有玩家，祝您玩的开心玩的快乐！", qq));
            sendMsg("<" + MapleParty.开服名字 + ">：给全服发送物品完成。", token);
            try {
                int 数量;
                int 物品ID;
                物品ID = Integer.parseInt(a);
                数量 = Integer.parseInt(b);
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                MapleInventoryType type = GameConstants.getInventoryType(物品ID);
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        if (数量 >= 0) {
                            if (!MapleInventoryManipulator.checkSpace(mch.getClient(), 物品ID, 数量, "")) {
                                return;
                            }
                            if (type.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(物品ID) && !GameConstants.isBullet(物品ID)
                                    || type.equals(MapleInventoryType.CASH) && 物品ID >= 5000000 && 物品ID <= 5000100) {
                                final Equip item = (Equip) (ii.getEquipById(物品ID));
                                if (ii.isCash(物品ID)) {
                                    item.setUniqueId(1);
                                }
                                final String name = ii.getName(物品ID);
                                if (物品ID / 10000 == 114 && name != null && name.length() > 0) { //medal
                                    final String msg = "你已获得称号 <" + name + ">";
                                    mch.getClient().getPlayer().dropMessage(5, msg);
                                }
                                MapleInventoryManipulator.addbyItem(mch.getClient(), item.copy());
                            } else {
                                MapleInventoryManipulator.addById(mch.getClient(), 物品ID, (short) 数量, "", null, (byte) 0);
                            }
                        } else {
                            MapleInventoryManipulator.removeById(mch.getClient(), GameConstants.getInventoryType(物品ID), 物品ID, -数量, true, false);
                        }
                        mch.getClient().sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 数量, true));
                        mch.startMapEffect("" + MapleParty.开服名字 + "管理员发放 " + MapleItemInformationProvider.getInstance().getName(Integer.parseInt(a)) + " x " + b + " 给在线的所有玩家，祝您玩的开心玩的快乐！", 5120027);
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private static void 录题(final String qq, final String a, final String b, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }
            if (!"O".equals(b) && !"X".equals(b) && !"o".equals(b) && !"x".equals(b)) {
                sendMsg("<" + MapleParty.开服名字 + ">：答案只能为[O，o，X，x]", token);
                return;
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO oxdt (b, c) VALUES (?, ?)")) {
                ps.setString(1, a);
                ps.setString(2, b);
                ps.executeUpdate();
                sendMsg("<" + MapleParty.开服名字 + ">：题目录入成功。(" + a + "/" + b + ")", token);
            } catch (SQLException ex) {

            }
        }
    }

    private static void 悬赏令(final String qq, final String a, final String b, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            if (MapleParty.机器人待机 > 0) {
                return;
            }

        }
    }

    private static void 查询账号(final String qq, final String token) {
        System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）我的账号 √ " + token);
        if (查询QQ下是否有封禁账号(qq) > 0) {
            sendMsg("你的QQ关联的游戏账号已经违反了冒险岛绿色游戏守则，已经被永久封禁。", token);
            return;
        }
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        final StringBuilder sb = new StringBuilder();
        int a = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM accounts WHERE qq = " + qq + "");
            rs = ps.executeQuery();
            while (rs.next()) {
                String txt = "";
                txt += "\r\n";
                txt += "游戏账号：" + rs.getString("name") + "\r\n";
                if (rs.getString("lastlogin") != "") {
                    txt += "最后登录：" + rs.getString("lastlogin") + "\r\n";
                }
                /*if (账号角色2(rs.getInt("id"), "id") > 0) {
                    String 状态 = "离线";
                    if (World.Find.findChannel(账号角色(rs.getInt("id"), "name")) > 0) {
                        状态 = "在线";
                    }
                    txt += "目前状态：" + 状态 + "\r\n";
                    txt += "最后登录：" + rs.getString("lastlogin") + "\r\n";
                    txt += "角色名字：[" + 账号角色(rs.getInt("id"), "id") + "] " + 账号角色(rs.getInt("id"), "name") + "\r\n";
                } else {
                    txt += "没有数据：该账号没有角色\r\n";
                }*/
                a += 1;
                sb.append(String.format(txt));
            }
        } catch (SQLException ex) {

        }
        sendMsg("所有账号如下：\r\n"
                + "绑定QQ：" + token + "\r\n"
                // + "信誉积分：100\r\n"
                + "账号数量：" + a + "\r\n"
                + "" + sb.toString(), token);
    }

    private static void 查询账号2(final String qq, final String zhanghao, final String token) {
        System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）查询账号 √ " + token);
        if (查询QQ下是否有封禁账号(qq) > 0) {
            sendMsg("你的QQ关联的游戏账号已经违反了冒险岛绿色游戏守则，已经被永久封禁。", token);
            return;
        }
        if (!账号取绑定QQ(zhanghao).equals(token)) {
            sendMsg("你要查询到的账号不属于你。", token);
            return;
        }
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }

        final StringBuilder sb = new StringBuilder();
        int a = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM accounts WHERE name = " + zhanghao + "");
            rs = ps.executeQuery();
            if (rs.next()) {
                String txt = "";
                txt += "\r\n";
                txt += "游戏账号：" + rs.getString("name") + "\r\n";
                if (账号角色2(rs.getInt("id"), "id") > 0) {
                    String 状态 = "离线";
                    if (World.Find.findChannel(账号角色(rs.getInt("id"), "name")) > 0) {
                        状态 = "在线";
                    }
                    txt += "角色名字：[" + 账号角色(rs.getInt("id"), "id") + "] " + 账号角色(rs.getInt("id"), "name") + "\r\n";
                    txt += "角色等级：" + 账号角色(rs.getInt("id"), "level") + "\r\n";
                    txt += "角色职业：" + getJobNameById(Integer.parseInt(账号角色(rs.getInt("id"), "job"))) + "\r\n";
                    txt += "目前状态：" + 状态 + "\r\n";
                    txt += "最后登录：" + rs.getString("lastlogin") + "\r\n";
                } else {
                    txt += "没有数据：该账号没有角色\r\n";
                }
                a += 1;
                sb.append(String.format(txt));
            }
        } catch (SQLException ex) {

        }
        sendMsg(sb.toString(), token);
    }

    private static void 在线玩家(final String qq, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {

            System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）在线玩家 √ " + token);
            StringBuilder sb = new StringBuilder();
            int a = 0;
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;

                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM characters ");
                rs = ps.executeQuery();
                while (rs.next()) {
                    String txt = "";
                    if (World.Find.findChannel(rs.getString("name")) > 0) {
                        txt += "玩家 : " + rs.getString("name") + " [" + rs.getString("id") + "]\r\n";
                        a += 1;
                    }
                    sb.append(String.format(txt));
                }
            } catch (SQLException ex) {

            }
            sendMsg("所有在线角色：\r\n"
                    + "数量：" + a + "\r\n\r\n"
                    + "" + sb.toString(), token);
        }
    }

    private static void 在线雇佣(final String qq, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {

            System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）在线玩家 √ " + token);
            try {
                int ID = 0;
                int 雇佣数量 = 0;
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT `id` FROM accounts  ORDER BY `id` DESC LIMIT 1");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String SN = rs.getString("id");
                        int sns = Integer.parseInt(SN);
                        sns++;
                        ID = sns;
                    }
                }
                for (int i = 0; i <= ID; i++) {
                    boolean 雇佣 = World.hasMerchant(i);
                    if (雇佣) {
                        雇佣数量++;
                    }
                }
                sendMsg("所有在线雇佣：\r\n"
                        + "数量：" + 雇佣数量 + " 个", token);
                System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在发放雇佣泡点 √ ");
            } catch (SQLException | NumberFormatException e) {
                System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在启用备用发放泡点 x ");
            }

        }
    }

    private static void 呼出后台(final String qq, final String token) {
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq) || "34443647".equals(qq)) {
            System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）呼出后台 √ " + token);
            CashShopServer();
        }
    }

    public static int 账号数量(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("qq") == null ? a == null : rs.getString("qq").equals(a)) {
                    data += 1;
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static String 账号角色(int a, String b) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = " + a + "");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data = rs.getString(b);
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 账号角色2(int a, String b) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = " + a + "");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data = rs.getInt(b);
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    private static void 注册账号(final String qq, final String newPassword, final String token) {
        System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + qq + "）注册在账号 √ " + newPassword);
        if (查询QQ下是否有封禁账号(qq) > 0) {
            sendMsg("你的QQ关联的游戏账号已经违反了冒险岛绿色游戏守则，已经被永久封禁。", token);
            return;
        }
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        if (MapleParty.机器人待机 > 0) {
            return;
        }
        if (!newPassword.matches("^[0-9A-Za-z]{6,11}$")) {
            sendMsg("账号格式不对，必须由6-11位数字或字母组成。", token);
            return;
        }

        if (gui.Start.ConfigValuesMap.get("机器人注册开关") != 0) {
            sendMsg("管理员关闭了账号注册功能。", token);
            return;
        }
        注册白名单 itemjc = new 注册白名单();
        String items1 = itemjc.getQQ();
        if (!items1.contains("," + token + ",")) {
            try {
                if (Start.CloudBlacklist.containsKey(token)) {
                    sendMsg("<<" + MapleParty.开服名字 + "云防系统>>"
                            + "：检测到你有使用非法插件，恶意破坏游戏平衡的不良记录，或者你的账号是辅助外挂群的成员，你的此次注册已经被拦截，已被禁止登陆，或者注册本端一切服务区域，如有疑问，请联系管理员。", token);
                    return;
                }
            } catch (Exception ex) {
                System.err.println("拦截黑名单玩家出错：" + ex.getMessage());
            }
        }
        注册黑名单 itemjc2 = new 注册黑名单();
        String items2 = itemjc2.getQQ();
        if (items2.contains("," + token + ",")) {
            sendMsg("<<" + MapleParty.开服名字 + "黑名单系统>>"
                    + "你的账号已被禁，如有疑问，请联系管理员。", token);
            return;
        }
        //判断已经注册账号数量
        if (账号数量(token) >= gui.Start.ConfigValuesMap.get("注册账号数量")) {
            sendMsg("你已经无法继续注册账号了。", token);
            return;
        }
        // 先判断QQ账号是否已经注册
        Boolean isExists = AutoRegister.getAccountExists(newPassword);
        if (isExists) {
            sendMsg("该账号已经被注册，请换一个吧。", token);
            return;
        }

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, qq) VALUES (?, ?, ?, ?, ?)");
            try {
                ps.setString(1, newPassword);
                ps.setString(2, LoginCrypto.hexSha1(qq));
                ps.setString(3, String.format("%s@qq.com", qq));
                ps.setString(4, "2018-11-11");
                ps.setString(5, qq);
                ps.executeUpdate();
                ps.close();
            } finally {
                ps.close();
            }
            sendMsg("你好，请将你要设置的密码以[*修改密码]<空格>[账号]<空格>[密码]格式发给我，我将为您修改密码。", token);
            Boolean isExists2 = AutoRegister.getAccountExists2(qq);
            if (!isExists2) {
                try {
                    Connection con1 = DatabaseConnection1.getConnection1();
                    PreparedStatement ps1 = con1.prepareStatement("INSERT INTO 游戏QQ号 ( qq ) VALUES (?)");
                    ps1.setString(1, token);
                    ps1.executeUpdate();
                    ps1.close();
                } catch (SQLException ex) {
                    System.out.println(ex);
                    return;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            sendMsg("对不起，QQ %s 注册失败！", token);
        }
    }

    private static void 银行余额(final String qq, final String 卡号, final String 密码, final String token) {
        if (查询QQ下是否有封禁账号(qq) > 0) {
            sendMsg("你的QQ关联的游戏账号已经违反了冒险岛绿色游戏守则，已经被永久封禁。", token);
            return;
        }
        if (!卡号.matches("^[0-9]{7,7}$")) {
            sendMsg("不正确的输入卡号。", token);
            return;
        }
        if (!密码.matches("^[0-9]{7,7}$")) {
            sendMsg("不正确的输入密码。", token);
            return;
        }
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        if (银行账号密码(Integer.parseInt(卡号)) == Integer.parseInt(密码)) {
            sendMsg("该银行卡号的余额"
                    + "\r\n区域:" + MapleParty.开服名字
                    + "\r\n卡号:" + 卡号
                    + "\r\n点券:" + 银行点券余额(Integer.parseInt(卡号))
                    + "\r\n金币:" + 银行金币余额(Integer.parseInt(卡号)), token);
        } else {
            sendMsg("银行账号密码错误。", token);
        }

    }

    private static void 转账金币(final String qq, final String 卡号, final String 密码, final String 转账账号, final String 金额, final String token) {
        if (查询QQ下是否有封禁账号(qq) > 0) {
            sendMsg("你的QQ关联的游戏账号已经违反了冒险岛绿色游戏守则，已经被永久封禁。", token);
            return;
        }
        if (!卡号.matches("^[0-9]{7,7}$")) {
            sendMsg("不正确的输入卡号。", token);
            return;
        }
        if (!密码.matches("^[0-9]{7,7}$")) {
            sendMsg("不正确的输入密码。", token);
            return;
        }
        if (!转账账号.matches("^[0-9]{7,7}$")) {
            sendMsg("不正确的输入转账账号。", token);
            return;
        }
        if (!金额.matches("^[0-9]{1,100000000000}$")) {
            sendMsg("不正确的输入转账金额。", token);
            return;
        }
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        if (银行账号(Integer.parseInt(卡号)) <= 0) {
            sendMsg("银行卡号不存在。", token);
            return;
        }
        if (银行账号密码(Integer.parseInt(卡号)) == Integer.parseInt(密码)) {
            long 余额 = 银行金币余额(Integer.parseInt(卡号));
            long 转账金额 = Long.parseLong(金额);
            long 目标余额 = 银行金币余额(Integer.parseInt(转账账号));
            if (余额 >= 转账金额) {
                /**
                 * <转账人>
                 */
                变更银行金币(Integer.parseInt(卡号), (余额 - 转账金额));
                通信("您的银行账号 " + 卡号 + " 于 " + CurrentReadable_Time() + " 转出 " + 金额 + " 金币，目前该银行账号余额为 " + (余额 - 转账金额) + " 金币。（来自" + MapleParty.开服名字 + "自由银行手机转账）", 账号ID取绑定QQ(角色ID取账号ID(银行开户人(Integer.parseInt(卡号)))));
                /**
                 * <收账人>
                 */
                变更银行金币(Integer.parseInt(转账账号), (long) (转账金额 * 0.99 + 目标余额));
                通信("您的银行账号 " + 转账账号 + " 于 " + CurrentReadable_Time() + " 转入 " + (long) (转账金额 * 0.99) + " 金币，目前该银行账号余额为 " + (long) (转账金额 * 0.99 + 目标余额) + " 金币。（来自" + MapleParty.开服名字 + "自由银行手机转账）", 账号ID取绑定QQ(角色ID取账号ID(银行开户人(Integer.parseInt(转账账号)))));
            } else {
                sendMsg("转账余额不足，不可以进行转账。", token);
            }
        } else {
            sendMsg("银行账号密码错误。", token);
        }
    }

    private static void 转账点券(final String qq, final String 卡号, final String 密码, final String 转账账号, final String 金额, final String token) {
        if (查询QQ下是否有封禁账号(qq) > 0) {
            sendMsg("你的QQ关联的游戏账号已经违反了冒险岛绿色游戏守则，已经被永久封禁。", token);
            return;
        }
        if (!卡号.matches("^[0-9]{7,7}$")) {
            sendMsg("不正确的输入卡号。", token);
            return;
        }
        if (!密码.matches("^[0-9]{7,7}$")) {
            sendMsg("不正确的输入密码。", token);
            return;
        }
        if (!转账账号.matches("^[0-9]{7,7}$")) {
            sendMsg("不正确的输入转账账号。", token);
            return;
        }
        if (!金额.matches("^[0-9]{1,100000000000}$")) {
            sendMsg("不正确的输入转账金额。", token);
            return;
        }
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        if (银行账号(Integer.parseInt(卡号)) <= 0) {
            sendMsg("银行卡号不存在。", token);
            return;
        }
        if (银行账号密码(Integer.parseInt(卡号)) == Integer.parseInt(密码)) {
            long 余额 = 银行点券余额(Integer.parseInt(卡号));
            long 转账金额 = Long.parseLong(金额);
            long 目标余额 = 银行点券余额(Integer.parseInt(转账账号));
            if (余额 >= 转账金额) {
                /**
                 * <转账人>
                 */
                变更银行点券(Integer.parseInt(卡号), (余额 - 转账金额));
                通信("您的银行账号 " + 卡号 + " 于 " + CurrentReadable_Time() + " 转出 " + 金额 + " 点券，目前该银行账号余额为 " + (余额 - 转账金额) + " 点券。（来自" + MapleParty.开服名字 + "自由银行手机转账）", 账号ID取绑定QQ(角色ID取账号ID(银行开户人(Integer.parseInt(卡号)))));
                /**
                 * <收账人>
                 */

                变更银行点券(Integer.parseInt(转账账号), (long) (转账金额 * 0.99 + 目标余额));
                通信("您的银行账号 " + 转账账号 + " 于 " + CurrentReadable_Time() + " 转入 " + (long) (转账金额 * 0.99) + " 点券，目前该银行账号余额为 " + (long) (转账金额 * 0.99 + 目标余额) + " 点券。（来自" + MapleParty.开服名字 + "自由银行手机转账）", 账号ID取绑定QQ(角色ID取账号ID(银行开户人(Integer.parseInt(转账账号)))));
            } else {
                sendMsg("转账余额不足，不可以进行转账。", token);
            }
        } else {
            sendMsg("银行账号密码错误。", token);
        }
    }

    private static void 修改密码(final String qq, final String zhanghao, final String newPassword, final String token) {
        System.out.println("[机器人]" + CurrentReadable_Time() + " : QQ（" + zhanghao + "）修改密码 √ " + newPassword);
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        if (MapleParty.机器人待机 > 0) {
            return;
        }
        if (!newPassword.matches("^[0-9A-Za-z]{6,10}$")) {
            sendMsg("新密码不合格，必须由6-10位数字或字母组成。", token);
            return;
        }
        Boolean isExists = AutoRegister.getAccountExists(zhanghao);
        if (!isExists) {
            sendMsg("该账号不存在。", token);
            return;
        }
        if (取账号绑定的QQ(zhanghao) == null ? token != null : !取账号绑定的QQ(zhanghao).equals(token)) {
            sendMsg("这个账号不属于你。", token);
            return;
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement pss = con.prepareStatement("UPDATE `accounts` SET `password` = ?, `salt` = ? WHERE qq = ? && name = ?");
            try {
                final String newSalt = LoginCrypto.makeSalt();
                pss.setString(1, LoginCrypto.makeSaltedSha512Hash(newPassword, newSalt));
                pss.setString(2, newSalt);
                pss.setString(3, qq);
                pss.setString(4, zhanghao);
                int res = pss.executeUpdate();
                if (res > 0) {
                    sendMsg("恭喜你，密码修改成功！", token);
                } else {
                    sendMsg("没有找到你的QQ对应的账号，密码修改失败！", token);
                }
            } finally {
                pss.close();
            }
        } catch (SQLException e) {
            System.err.println("修改密码出错。" + e);
        }
    }

    public static void 按键开(String a, int b) {
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
            ps1.setInt(1, 1);
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlstr = " delete from configvalues where id =" + b + "";
                ps1.executeUpdate(sqlstr);
                ps1.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
            ps.setInt(1, b);
            ps.setString(2, a);
            ps.setInt(3, 0);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
        }
        gui.Start.GetConfigValues();
    }

    public static void 按键关(String a, int b) {
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
            ps1.setInt(1, 1);
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlstr = " delete from configvalues where id =" + b + "";
                ps1.executeUpdate(sqlstr);
                ps1.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
            ps.setInt(1, b);
            ps.setString(2, a);
            ps.setInt(3, 1);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
        }
        gui.Start.GetConfigValues();
    }

    private static void 指令大全(final String qq, final String token) {
        if (冷却 > 0) {
            sendMsg("机器人繁忙，请稍后再试。", token);
            return;
        } else {
            冷却 += 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 冷却时间);
                        冷却 = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        if (ServerProperties.getProperty("ZEV.QQ1").equals(qq) || ServerProperties.getProperty("ZEV.QQ2").equals(qq) || "71447500".equals(qq)) {
            sendMsg(""
                    + "<你可以使用的指令>\r\n"
                    + "[私]*我的账号\r\n"
                    + "[私]*在线玩家\r\n"
                    + "[私]*在线雇佣\r\n"
                    + "[私]*重启数据库\r\n"
                    + "[私]*注册账号<空格>[账号]\r\n"
                    + "[私]*查询账号<空格>[账号]\r\n"
                    + "[私]*录题<空格>[题目]<空格>[答案]\r\n"
                    + "[私]*删除角色<空格>[角色ID]\r\n"
                    + "[私]*修改密码<空格>[账号]<空格>[密码]\r\n"
                    + "[私]*解卡发型<空格>[角色ID]\r\n"
                    + "[私]*解卡脸型<空格>[角色ID]\r\n"
                    + "[私]*解卡地图1<空格>[角色ID](离线)\r\n"
                    + "[私]*解卡地图2<空格>[角色ID](在线)\r\n"
                    + "[私]*经验活动<空格>[倍率]<空格>[持续时间]\r\n"
                    + "[私]*爆率活动<空格>[倍率]<空格>[持续时间]\r\n"
                    + "[私]*金币活动<空格>[倍率]<空格>[持续时间]\r\n"
                    + "[私]*给全服发点券<空格>[数量]\r\n"
                    + "[私]*给全服发金币<空格>[数量]\r\n"
                    + "[私]*给全服发经验<空格>[数量]\r\n"
                    + "[私]*给全服发物品<空格>[物品代码]<空格>[数量]\r\n"
                    + "[私]*机器人状态<空格>[开/关]\r\n"
                    + "[私]*全服决斗<空格>[开/关]\r\n"
                    + "[私]*商城出售通知<空格>[开/关]\r\n"
                    + "[私]*呼出后台(不小心关闭服务端后台可使用呼出)\r\n"
                    + "[私]*查询银行余额<空格>[卡号]<空格>[密码]\r\n"
                    + "[私]*转账点券<空格>[卡号]<空格>[密码]<空格>[转账卡号]<空格>[金额]\r\n"
                    + "[私]*转账金币<空格>[卡号]<空格>[密码]<空格>[转账卡号]<空格>[金额]\r\n"
                    + "", token);
        } else {
            sendMsg(""
                    + "<你可以使用的指令>\r\n"
                    + "[私]*我的账号(查看QQ下所有账号)\r\n"
                    + "[私]*注册账号<空格>[账号]\r\n"
                    + "[私]*修改密码<空格>[账号]<空格>[密码]\r\n"
                    + "[私]*删除角色<空格>[角色ID]\r\n"
                    + "[私]*解卡发型<空格>[角色ID]\r\n"
                    + "[私]*解卡脸型<空格>[角色ID]\r\n"
                    + "[私]*解卡地图1<空格>[角色ID](离线)\r\n"
                    + "[私]*解卡地图2<空格>[角色ID](在线)\r\n"
                    + "[私]*查询账号<空格>[账号]\r\n"
                    + "[私]*查询银行余额<空格>[卡号]<空格>[密码]\r\n"
                    + "[私]*转账点券<空格>[卡号]<空格>[密码]<空格>[转账卡号]<空格>[金额]\r\n"
                    + "[私]*转账金币<空格>[卡号]<空格>[密码]<空格>[转账卡号]<空格>[金额]\r\n"
                    + "", token);
        }
    }

    public static final String getJobNameById(int job) {
        switch (job) {
            case 0:
                return "新手";
            case 1000:
                return "初心者";
            case 2000:
                return "战童";
            case 100:
                return "战士";
            case 110:
                return "剑客";
            case 111:
                return "勇士";
            case 112:
                return "英雄";
            case 120:
                return "准骑士";
            case 121:
                return "骑士";
            case 122:
                return "圣骑士";
            case 130:
                return "枪战士";
            case 131:
                return "龙骑士";
            case 132:
                return "黑骑士";
            case 200:
                return "魔法师";
            case 210:
                return "法师(火,毒)";
            case 211:
                return "巫师(火,毒)";
            case 212:
                return "魔导师(火,毒)";
            case 220:
                return "法师(冰,雷)";
            case 221:
                return "巫师(冰,雷)";
            case 222:
                return "魔导师(冰,雷)";
            case 230:
                return "牧师";
            case 231:
                return "祭司";
            case 232:
                return "主教";
            case 300:
                return "弓箭手";
            case 310:
                return "猎人";
            case 311:
                return "射手";
            case 312:
                return "神射手";
            case 320:
                return "弩弓手";
            case 321:
                return "游侠";
            case 322:
                return "箭神";
            case 400:
                return "飞侠";
            case 410:
                return "刺客";
            case 411:
                return "无影人";
            case 412:
                return "隐士";
            case 420:
                return "侠客";
            case 421:
                return "独行客";
            case 422:
                return "侠盗";
            case 500:
                return "海盜";
            case 510:
                return "拳手";
            case 511:
                return "斗士";
            case 512:
                return "冲锋队长";
            case 520:
                return "火枪手";
            case 521:
                return "大副";
            case 522:
                return "船长";
            case 800:
            case 900:
                return "管理员";
            case 1100:
                return "魂骑士(一转)";
            case 1110:
                return "魂骑士(二转)";
            case 1111:
                return "魂骑士(三转)";
            case 1112:
                return "魂骑士(四转)";
            case 1200:
                return "炎术士(一转)";
            case 1210:
                return "炎术士(二转)";
            case 1211:
                return "炎术士(三转)";
            case 1212:
                return "炎术士(四转)";
            case 1300:
                return "风灵使者(一转)";
            case 1310:
                return "风灵使者(二转)";
            case 1311:
                return "风灵使者(三转)";
            case 1312:
                return "风灵使者(四转)";
            case 1400:
                return "夜行者(一转)";
            case 1410:
                return "夜行者(二转)";
            case 1411:
                return "夜行者(三转)";
            case 1412:
                return "夜行者(四转)";
            case 1500:
                return "奇袭者(一转)";
            case 1510:
                return "奇袭者(二转)";
            case 1511:
                return "奇袭者(三转)";
            case 1512:
                return "奇袭者(四转)";
            case 2100:
                return "战神(一转)";
            case 2110:
                return "战神(二转)";
            case 2111:
                return "战神(三转)";
            case 2112:
                return "战神(四转)";
            default:
                return "未知职业";
        }
    }

    public static void 删除角色(final int cid) {
        PreparedStatement ps1 = null;
        ResultSet rs = null;

        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
            ps1.setInt(1, cid);
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlstr = " delete from characters where id =" + cid + "";
                ps1.executeUpdate(sqlstr);

                String sqlstr2 = " delete from inventoryitems where characterid =" + cid + "";
                ps1.executeUpdate(sqlstr2);

                String sqlstr21 = " delete from inventoryitems where characterid =" + cid + "";
                ps1.executeUpdate(sqlstr21);

                String sqlstr3 = " delete from auctionitems where characterid =" + cid + "";
                ps1.executeUpdate(sqlstr3);

                String sqlstr31 = " delete from auctionitems1 where characterid =" + cid + "";
                ps1.executeUpdate(sqlstr31);

                String sqlstr32 = " delete from auctionpoint where characterid =" + cid + "";
                ps1.executeUpdate(sqlstr32);

                String sqlstr33 = " delete from auctionpoint1 where characterid =" + cid + "";
                ps1.executeUpdate(sqlstr33);

                String sqlstr4 = " delete from csitems where accountid =" + cid + "";
                ps1.executeUpdate(sqlstr4);

                String sqlstr41 = " delete from awarp where id =" + cid + "";
                ps1.executeUpdate(sqlstr41);

                String sqlstr5 = " delete from bank_item where cid =" + cid + "";
                ps1.executeUpdate(sqlstr5);

                String sqlstr6 = " delete from bossrank where cid =" + cid + "";
                ps1.executeUpdate(sqlstr6);

                String sqlstr7 = " delete from skills where characterid =" + cid + "";
                ps1.executeUpdate(sqlstr7);

                String sqlstr71 = " delete from skillmacros where characterid =" + cid + "";
                ps1.executeUpdate(sqlstr71);

                String sqlstr711 = " delete from monsterbook where charid =" + cid + "";
                ps1.executeUpdate(sqlstr711);

            }
        } catch (SQLException ex) {
        }

    }

    public static void deleteCharacter(final int cid, final int accId) {
        try {
            final Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT guildid, guildrank, familyid, name FROM characters WHERE id = ? AND accountid = ?");
            ps.setInt(1, cid);
            ps.setInt(2, accId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();

            }
            if (rs.getInt("guildid") > 0) {
                if (rs.getInt("guildrank") == 1) {
                    rs.close();
                    ps.close();

                }
                World.Guild.deleteGuildCharacter(rs.getInt("guildid"), cid);
            }
            if (rs.getInt("familyid") > 0) {
                World.Family.getFamily(rs.getInt("familyid")).leaveFamily(cid);
            }
            rs.close();
            ps.close();
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM characters WHERE id = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM monsterbook WHERE charid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM hiredmerch WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mts_cart WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mts_items WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mountdata WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryitems WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM famelog WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM famelog WHERE characterid_to = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM dueypackages WHERE RecieverId = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM wishlist WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM buddies WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM buddies WHERE buddyid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM keymap WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM savedlocations WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM skills WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mountdata WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM skillmacros WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM trocklocations WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM queststatus WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryslot WHERE characterid = ?", cid);

        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
            e.printStackTrace();
        }

    }
}
