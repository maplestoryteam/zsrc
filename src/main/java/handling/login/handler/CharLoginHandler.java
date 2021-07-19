/*
登陆账号
 */
package handling.login.handler;

import abc.注册白名单;
import abc.注册黑名单;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.inventory.IItem;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import database.DatabaseConnection;
import gui.Start;
import handling.channel.ChannelServer;
import handling.login.LoginInformationProvider;
import handling.login.LoginServer;
import handling.login.LoginWorker;
import handling.world.MapleParty;
import server.MapleItemInformationProvider;
import server.ServerProperties;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.KoreanDateUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.data.LittleEndianAccessor;
import tools.packet.LoginPacket;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import static a.本地数据库.查询QQ下是否有封禁账号;
import static a.本地数据库.账号取绑定QQ;
import static tools.FileoutputUtil.CurrentReadable_Date;
import static tools.FileoutputUtil.CurrentReadable_Time;

public class CharLoginHandler {

    public static final void login(final LittleEndianAccessor slea, final MapleClient c) {
        /**
         * ***
         * 输入账号点击登陆游戏
         */
        //判断服务端是否启动完成
        /*if (MapleParty.服务端启动中 == 0) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "服务端未启动完成，请稍后。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }*/
        final String login = slea.readMapleAsciiString();
        final String pwd = slea.readMapleAsciiString();
        //记录账号
        c.setAccountName(login);
        int[] bytes = new int[6];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = slea.readByteAsInt();
        }
        StringBuilder sps = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sps.append(StringUtil.getLeftPaddedStr(Integer.toHexString(bytes[i]).toUpperCase(), '0', 2));
            sps.append("-");
        }
        //机器码地址
        String macData = sps.toString();
        macData = macData.substring(0, macData.length() - 1);
        c.setMac(macData);
        //判断IP是否被禁止
        final boolean ipBan = c.hasBannedIP();
        //判断机器码是否被禁
        final boolean macBan = c.isBannedMac(macData);
        //某一个被禁则被禁止
        final boolean banned = ipBan || macBan;
        int loginok = 0;
        if (AutoRegister.autoRegister && !AutoRegister.判断账号是否存在(login) && (!banned)) {
            if (pwd.equalsIgnoreCase("disconnect") || pwd.equalsIgnoreCase("fixme")) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "此密码无效。"));
                c.sendPacket(LoginPacket.getLoginFailed(1));
                return;
            }
            if (gui.Start.ConfigValuesMap.get("账号注册开关") <= 0) {
                AutoRegister.createAccount(login, pwd, c.getSession().remoteAddress().toString(), macData);
                AutoRegister.success = true;
                AutoRegister.mac = true;
                c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n账号注册成功，请重新登录，即可进入游戏。"));
            } else {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n管理员未开启注册功能。"));
            }
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }
        loginok = c.login(login, pwd, ipBan || macBan);
        final Calendar tempbannedTill = c.getTempBanCalendar();
        if (loginok == 0 && (ipBan || macBan) && !c.isGm()) {
            loginok = 3;
            if (macBan) {
            }
        }

        /**
         * <登录前清掉登陆信息>
         */
        //取账号绑定QQ
        String 绑定QQ = 账号取绑定QQ(login);
        //判断账号是否绑定QQ
        if (绑定QQ == null) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n你的账号暂未绑定QQ，请绑定QQ后再尝试登陆。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }
        if (gui.Start.ConfigValuesMap.get("名单拦截开关") == 0) {
            注册白名单 itemjc = new 注册白名单();
            String items1 = itemjc.getQQ();
            if (!items1.contains("," + 绑定QQ + ",")) {
                //云黑名单
                try {
                    String qq = 绑定QQ;
                    String ip = c.getSession().remoteAddress().toString().split(":")[0];
                    if (Start.CloudBlacklist.containsKey(qq) || Start.CloudBlacklist.containsValue(ip)) {
                        c.sendPacket(MaplePacketCreator.serverNotice(1, ""
                                + "<<冒险岛云防系统>>\r\n"
                                + "" + MapleParty.开服名字 + "\r\n"
                                + "\r\n\r\n检测到你有使用非法插件，恶意破坏游戏平衡的不良记录，或者你的账号是辅助外挂群的成员，你的此次登陆已经被拦截，已被禁止登陆本端一切服务区域，如有疑问，请联系管理员。"));
                        c.sendPacket(LoginPacket.getLoginFailed(1));
                        return;
                    }
                } catch (Exception ex) {
                }
            }
            //本地黑名单
            注册黑名单 itemjc2 = new 注册黑名单();
            String items2 = itemjc2.getQQ();
            if (items2.contains("," + 绑定QQ + ",")) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, ""
                        + "<<冒险岛黑名单系统>>\r\n"
                        + "" + MapleParty.开服名字 + "\r\n"
                        + "\r\n\r\n你的账号已被禁，如有疑问，请联系管理员。"));
                c.sendPacket(LoginPacket.getLoginFailed(1));
                return;
            }
        }
        //登陆保护
        if (gui.Start.ConfigValuesMap.get("登陆保护开关") == 0) {
            if (Getcharacterz("" + login + "", 101) > 0) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n请稍后在登陆，目前你的账号还在登陆保护时间。"));
                c.sendPacket(LoginPacket.getLoginFailed(1));
                return;
            }
        }
        //登陆队列
        if (gui.Start.ConfigValuesMap.get("登陆队列开关") == 0) {
            if (队列人数() > 5) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n在你的前面还有 " + 队列人数() + " 个玩家。"));
                c.sendPacket(LoginPacket.getLoginFailed(1));
                return;
            }
        }
        //IP限制多开
        if (gui.Start.ConfigValuesMap.get("IP多开开关") == 0) {
            if (IP登陆数("" + c.getSessionIPAddress() + "") == false) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n该IP下已经有登陆的账号。"));
                c.sendPacket(LoginPacket.getLoginFailed(1));
                return;
            }
        } else if (IP登陆数2("" + c.getSessionIPAddress() + "") >= gui.Start.ConfigValuesMap.get("IP多开数")) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n登陆超出多开上限 A01。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }
        //机器码限制多开
        if (gui.Start.ConfigValuesMap.get("机器多开开关") == 0) {
            if (机器码登陆数("" + macData + "") == false) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n该机器下已经有登陆的账号。"));
                c.sendPacket(LoginPacket.getLoginFailed(1));
                return;
            }
        } else if (机器码登陆数2("" + macData + "") >= gui.Start.ConfigValuesMap.get("机器码多开数")) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n登陆超出多开上限 A02。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }

        //判断该QQ是否被封禁
        if (查询QQ下是否有封禁账号(绑定QQ) > 0) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n你的 QQ " + 绑定QQ + " 已经被封禁。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }
        //判断容纳人数
//        if (MapleParty.容纳人数 != 999) {
//            if (在线账号() >= MapleParty.容纳人数) {
//                c.sendPacket(MaplePacketCreator.serverNotice(1, "<<" + MapleParty.开服名字 + ">>\r\n\r\n非常抱歉，服务器人数已达到上限，你的此次登陆被拒绝。"));
//                c.sendPacket(LoginPacket.getLoginFailed(1));
//                return;
//            }
//        }
        if (loginok != 0) {
            if (!loginFailCount(c)) {
                c.sendPacket(LoginPacket.getLoginFailed(loginok));
            }
        } else if (tempbannedTill.getTimeInMillis() != 0) {
            if (!loginFailCount(c)) {
                c.sendPacket(LoginPacket.getTempBan(KoreanDateUtil.getTempBanTimestamp(tempbannedTill.getTimeInMillis()), c.getBanReason()));
            }
        } else {
            //登陆账号记录IP和机器码
            if (gui.Start.ConfigValuesMap.get("登陆记录开关") <= 0) {
                FileoutputUtil.logToFile("账号登陆源记录/" + CurrentReadable_Date() + "/IP地址登陆信息/" + c.getSession().remoteAddress().toString() + ".txt", "[时间：" + CurrentReadable_Time() + "]账号：[ " + login + " ]机器码：[ " + macData + " ]\r\n");
                FileoutputUtil.logToFile("账号登陆源记录/" + CurrentReadable_Date() + "/机器码登陆信息/" + macData + ".txt", "[时间：" + CurrentReadable_Time() + "]账号：[ " + login + " ]IP：[ " + c.getSession().remoteAddress().toString() + " ]\r\n");
                FileoutputUtil.logToFile("账号登陆源记录/" + CurrentReadable_Date() + "/账号登陆信息/" + login + ".txt", "[时间：" + CurrentReadable_Time() + "]机器码：[ " + macData + " ]IP：[ " + c.getSession().remoteAddress().toString() + " ]\r\n");
            }
            FileoutputUtil.logToZev("服务端信息文件/服务端登陆账号收集/" + MapleParty.开服名字 + ".txt", "" + login + "\r\n");
            //记录此次登陆的mac
            c.updateMacs();
            c.loginAttempt = 0;
            //登陆账号
            LoginWorker.registerClient(c);

            if (gui.Start.ConfigValuesMap.get("登陆队列开关") == 0) {
                //进入队列
                Gaincharacterz("" + login + "", 102, 1);
                //30秒超时时间
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000 * 30);
                            if (Getcharacterz("" + login + "", 102) > 0) {
                                Gaincharacterz("" + login + "", 102, -Getcharacterz("" + login + "", 102));
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                }.start();
            }
            System.out.println("[服务端]" + CurrentReadable_Time() + " : 登陆账号（ " + login + " ）绑定QQ（ " + 绑定QQ + " ）√ ");
        }
    }

    public static int Getcharacterz(String Name, int Channale) {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characterz WHERE channel = ? and Name = ?");
            ps.setInt(1, Channale);
            ps.setString(2, Name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            ret = rs.getInt("Point");
            rs.close();
            ps.close();
        } catch (SQLException ex) {
        }
        return ret;
    }

    public static void Gaincharacterz(String Name, int Channale, int Piot) {
        try {
            int ret = Getcharacterz(Name, Channale);
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO characterz (channel, Name,Point) VALUES (?, ?, ?)");
                    ps.setInt(1, Channale);
                    ps.setString(2, Name);
                    ps.setInt(3, ret);

                    ps.execute();
                } catch (SQLException e) {
                    System.out.println("xxxxxxxx:" + e);
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("xxxxxxxxzzzzzzz:" + e);
                    }
                }
            }
            ret += Piot;
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE characterz SET `Point` = ? WHERE Name = ? and channel = ?");
            ps.setInt(1, ret);
            ps.setString(2, Name);
            ps.setInt(3, Channale);
            ps.execute();
            ps.close();
        } catch (SQLException sql) {
            System.err.println("Getcharacterz!!55" + sql);
        }
    }

    public static final void SetGenderRequest(final LittleEndianAccessor slea, final MapleClient c) {
        byte gender = slea.readByte();
        String username = slea.readMapleAsciiString();
        // String password = slea.readMapleAsciiString();
        if (c.getAccountName().equals(username)) {
            c.setGender(gender);
            // c.setSecondPassword(password);
            c.updateSecondPassword();
            c.updateGender();
            c.sendPacket(LoginPacket.getGenderChanged(c));
            c.sendPacket(MaplePacketCreator.licenseRequest());
            c.updateLoginState(MapleClient.LOGIN_NOTLOGGEDIN, c.getSessionIPAddress());

        } else {
            c.getSession().close();
        }

    }

    public static final void ServerListRequest(final MapleClient c) {//显示频道
        if (gui.Start.ConfigValuesMap.get("蓝蜗牛开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(0, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.蓝蜗牛状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("蘑菇仔开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(1, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.蘑菇仔状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("绿水灵开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(2, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.绿水灵状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("漂漂猪开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(3, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.漂漂猪状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("小青蛇开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(4, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.小青蛇状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("红螃蟹开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(5, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.红螃蟹状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("大海龟开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(6, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.大海龟状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("章鱼怪开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(7, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.章鱼怪状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("顽皮猴开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(8, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.顽皮猴状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("星精灵开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(9, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.星精灵状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("胖企鹅开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(10, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.胖企鹅状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("白雪人开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(11, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.白雪人状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("石头人开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(12, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.石头人状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("紫色猫开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(13, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.紫色猫状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("大灰狼开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(14, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.大灰狼状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("小白兔开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(15, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.小白兔状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("喷火龙开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(16, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.喷火龙状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("火野猪开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(17, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.火野猪状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("青鳄鱼开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(18, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.青鳄鱼状态"))));
        }
        if (gui.Start.ConfigValuesMap.get("花蘑菇开关") == 0) {
            c.sendPacket(LoginPacket.getServerList(19, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.花蘑菇状态"))));
        }
        c.sendPacket(LoginPacket.getEndOfServerList());
    }

    public static final void ServerStatusRequest(final MapleClient c) {//人数太多提示
        // 0 = 人数正常
        // 1 = 人数拥挤
        // 2 = 人数爆满
        final int numPlayer = LoginServer.getUsersOn();
        final int userLimit = LoginServer.getUserLimit();
        if (numPlayer >= userLimit) {
            c.sendPacket(LoginPacket.getServerStatus(2));
        } else if (numPlayer * 2 >= userLimit) {
            c.sendPacket(LoginPacket.getServerStatus(1));
        } else {
            c.sendPacket(LoginPacket.getServerStatus(0));
        }
    }

    public static final void LicenseRequest(final LittleEndianAccessor slea, final MapleClient c) {
        if (slea.readByte() == 1) {
            c.sendPacket(MaplePacketCreator.licenseResult());
            c.updateLoginState(0);
        } else {
            c.getSession().close();

        }

    }

    //选择频道
    public static final void CharlistRequest(final LittleEndianAccessor slea, final MapleClient c) throws UnknownHostException, SocketException {
        // slea.readByte();
        final int server = slea.readByte();
        final int channel = slea.readByte() + 1;
        slea.readInt();

        //c.setWorld(server);
        c.setWorld(0);
        // System.out.println("Client " + c.getSession().getRemoteAddress().toString().split(":")[0] + " is connecting to server " + server + " channel " + channel + "");
        c.setChannel(channel);
        //final List<MapleCharacter> chars = c.loadCharacters(server);
        final List<MapleCharacter> chars = c.loadCharacters(0);
        if (chars != null) {
            c.sendPacket(LoginPacket.getCharList(c.getSecondPassword() != null, chars, c.getCharacterSlots()));
        } else {
            c.getSession().close();
        }
    }

    //判断玩家名字
    public static final void CheckCharName(final String name, final MapleClient c) {
        if (!MapleCharacterUtil.canCreateChar(name) || LoginInformationProvider.getInstance().isForbiddenName(name)) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "提示：角色名格式不正确，只能中文或者数字。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
        }
        c.sendPacket(LoginPacket.charNameResponse(name, !MapleCharacterUtil.canCreateChar(name) || LoginInformationProvider.getInstance().isForbiddenName(name)));
    }

    public static final void CreateChar(final LittleEndianAccessor slea, final MapleClient c) {
        int 冒险家 = gui.Start.ConfigValuesMap.get("冒险家职业开关");
        int 战神 = gui.Start.ConfigValuesMap.get("战神职业开关");
        int 骑士团 = gui.Start.ConfigValuesMap.get("骑士团职业开关");
        final String name = slea.readMapleAsciiString();
        // 1 = 冒险岛, 0 = 骑士团, 2 = 战神
        final int JobType = slea.readInt();

        if (骑士团 == 1 && JobType == 0) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "骑士团职业群已被关闭！无法创建。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        } else if (冒险家 == 1 && JobType == 1) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "冒险家职业群已被关闭！无法创建。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        } else if (战神 == 1 && JobType == 2) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "战神职业群已被关闭！无法创建。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }//server.Start.ConfigValuesMap.get("创建角色数量")
        if (c.loadCharacterIds(c.getWorld()).size() >= gui.Start.ConfigValuesMap.get("创建角色数量")) {//LoginServer.getMaxCharacters()//LoginServer.getMaxCharacters()
            c.sendPacket(MaplePacketCreator.serverNotice(1, "无法继续创建新角色。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }
        final short db = 0; //whether dual blade = 1 or adventurer = 0
        final int face = slea.readInt();
        final int hair = slea.readInt();
        final int hairColor = 0;
        final byte skinColor = 0;
        final int top = slea.readInt();
        final int bottom = slea.readInt();
        final int shoes = slea.readInt();
        final int weapon = slea.readInt();

        final byte gender = c.getGender();

        if (gender == 0) {
            /*if (face != 20100 && face != 20401 && face != 20402) {
                return;
            }
            if (hair != 30030 && hair != 30027 && hair != 30000) {
                return;
            }
            if (top != 1040002 && top != 1040006 && top != 1040010 && top != 1042167) {
                return;
            }
            if (bottom != 1060002 && bottom != 1060006 && bottom != 1062115) {
                return;
            }*/
        } else if (gender == 1) {
            /*if (face != 21002 && face != 21700 && face != 21201) {
                return;
            }
            if (hair != 31002 && hair != 31047 && hair != 31057) {
                return;
            }
            if (top != 1041002 && top != 1041006 && top != 1041010 && top != 1041011) {
                return;
            }
            if (bottom != 1061002 && bottom != 1061008 && bottom != 1062115) {
                return;
            }*/
        } else {
            return;
        }
        if (shoes != 1072001 && shoes != 1072005 && shoes != 1072037 && shoes != 1072038 && shoes != 1072383) {
            return;
        }
        if (weapon != 1302000 && weapon != 1322005 && weapon != 1312004 && weapon != 1442079) {
            return;
        }

        MapleCharacter newchar = MapleCharacter.getDefault(c, JobType);
        //newchar.setWorld((byte) c.getWorld());
        newchar.setWorld((byte) 0);
        newchar.setFace(face);
        newchar.setHair(hair + hairColor);
        newchar.setGender(gender);
        newchar.setName(name);
        newchar.setSkinColor(skinColor);
        MapleInventory equip = newchar.getInventory(MapleInventoryType.EQUIPPED);
        final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();

        IItem item = li.getEquipById(top);
        item.setPosition((byte) -5);
        equip.addFromDB(item);

        item = li.getEquipById(bottom);
        item.setPosition((byte) -6);
        equip.addFromDB(item);

        item = li.getEquipById(shoes);
        item.setPosition((byte) -7);
        equip.addFromDB(item);

        item = li.getEquipById(weapon);
        item.setPosition((byte) -11);
        equip.addFromDB(item);

        //blue/red pots
        switch (JobType) {
            case 0: // Cygnus
                newchar.setQuestAdd(MapleQuest.getInstance(20022), (byte) 1, "1");
                newchar.setQuestAdd(MapleQuest.getInstance(20010), (byte) 1, null); //>_>_>_> ugh

                newchar.setQuestAdd(MapleQuest.getInstance(20000), (byte) 1, null); //>_>_>_> ugh
                newchar.setQuestAdd(MapleQuest.getInstance(20015), (byte) 1, null); //>_>_>_> ugh
                newchar.setQuestAdd(MapleQuest.getInstance(20020), (byte) 1, null); //>_>_>_> ugh

                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161047, (byte) 0, (short) 1, (byte) 0));
                break;
            case 1: // Adventurer
                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161001, (byte) 0, (short) 1, (byte) 0));
                break;
            case 2: // Aran
                newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161048, (byte) 0, (short) 1, (byte) 0));
                break;
        }

        if (MapleCharacterUtil.canCreateChar(name) && !LoginInformationProvider.getInstance().isForbiddenName(name)) {
            MapleCharacter.saveNewCharToDB(newchar, JobType, JobType == 1 && db == 0);
            c.sendPacket(LoginPacket.addNewCharEntry(newchar, true));
            c.createdChar(newchar.getId());
            if (MapleParty.调试3 > 0) {
                System.out.printf("检测名字通过");
            }
        } else {
            if (MapleParty.调试3 > 0) {
                System.out.printf("检测名字不通过");
            }
            c.sendPacket(LoginPacket.addNewCharEntry(newchar, false));
        }
    }

    public static final void DeleteChar(final LittleEndianAccessor slea, final MapleClient c) {

        slea.readByte();
        String Secondpw_Client = null;
//        if (slea.readByte() > 0) { // Specific if user have second password or not
        Secondpw_Client = slea.readMapleAsciiString();
//        }
//        slea.readMapleAsciiString();
        final int Character_ID = slea.readInt();

        if (!c.login_Auth(Character_ID)) {
            c.sendPacket(LoginPacket.secondPwError((byte) 0x14));
            return; // Attempting to delete other character
        }
        byte state = 0;

        if (c.getSecondPassword() != null) { // On the server, there's a second password
            if (Secondpw_Client == null) { // Client's hacking
                c.getSession().close();
                return;
            } else if (!c.CheckSecondPassword(Secondpw_Client)) { // Wrong Password
                //state = 12;
                state = 16;
            }
        }
        // TODO, implement 13 digit Asiasoft passport too.

        if (state == 0) {
            state = (byte) c.deleteCharacter(Character_ID);
        }
        c.sendPacket(LoginPacket.deleteCharResponse(Character_ID, state));
    }

    public static int 取账号在线状态(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("id") == a) {
                    data = rs.getInt("loggedin");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static void Character_WithoutSecondPassword(final LittleEndianAccessor slea, final MapleClient c) {//进入游戏选择频道
        // slea.skip(1);
        //判断是不是在线
        if (取账号在线状态(c.getAccID()) != 2) {
            c.sendPacket(LoginPacket.getLoginFailed(7));
            return;
        }

        if (c.getLoginState() != 2) {
            c.sendPacket(LoginPacket.getLoginFailed(7));
            return;
        }
        final int charId = slea.readInt();
        if ((!c.isLoggedIn()) || (loginFailCount(c)) || (!c.login_Auth(charId))) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        if ((ChannelServer.getInstance(c.getChannel()) == null) || (c.getWorld() != 0)) {
            c.getSession().close();
            return;
        }
        if (c.getIdleTask() != null) {
            c.getIdleTask().cancel(true);
        }

        String ip = c.getSessionIPAddress();
        LoginServer.putLoginAuth(charId, ip.substring(ip.indexOf('/') + 1), c.getTempIP(), c.getChannel());
        c.sendPacket(MaplePacketCreator.getServerIP(Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1]), charId));
    }

    public static final void Character_WithSecondPassword(final LittleEndianAccessor slea, final MapleClient c) {
        final String password = slea.readMapleAsciiString();
        final int charId = slea.readInt();
        // 除非玩家入侵，否则不会发生这种情况。
        if (loginFailCount(c) || c.getSecondPassword() == null || !c.login_Auth(charId)) {
            c.getSession().close();
            return;
        }
        if (c.CheckSecondPassword(password)) {
            c.updateMacs(slea.readMapleAsciiString());
            if (c.getIdleTask() != null) {
                c.getIdleTask().cancel(true);
            }
            c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
            c.sendPacket(MaplePacketCreator.getServerIP(Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1]), charId));
        } else {
            c.sendPacket(LoginPacket.secondPwError((byte) 0x14));
        }
    }

    public static int 在线账号() {
        int ret = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM  accounts WHERE loggedin > 0");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ret += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("在线账号、出错");
        }
        return ret;
    }

    public static int 队列人数() {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT Point as DATA FROM characterz WHERE channel = 102");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data += rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("队列人数、出错");
        }
        return data;
    }

    public static boolean IP登陆数(String a) {
        boolean ret = true;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM  accounts");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString("SessionIP") != null) {
                        if (rs.getString("SessionIP").equals("" + a + "")) {
                            if (rs.getInt("loggedin") > 0) {
                                ret = false;
                            }
                        }
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("IP登陆数、出错");
        }
        return ret;
    }

    public static int IP登陆数2(String a) {
        int ret = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM  accounts");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString("SessionIP") != null) {
                        if (rs.getString("SessionIP").equals("" + a + "")) {
                            if (rs.getInt("loggedin") > 0) {
                                ret += 1;
                            }
                        }
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("IP登陆数2、出错");
        }
        return ret;
    }

    public static boolean 机器码登陆数(String a) {
        boolean ret = true;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM  accounts");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString("macs") != null) {
                        if (rs.getString("macs").equals("" + a + "")) {
                            if (rs.getInt("loggedin") > 0) {
                                ret = false;
                            }
                        }
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("机器码登陆数、出错");
        }
        return ret;
    }

    public static int 机器码登陆数2(String a) {
        int ret = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM  accounts");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getString("macs") != null) {
                        if (rs.getString("macs").equals("" + a + "")) {
                            if (rs.getInt("loggedin") > 0) {
                                ret += 1;
                            }
                        }
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("机器码登陆数2、出错");
        }
        return ret;
    }

    private static final boolean loginFailCount(final MapleClient c) {
        c.loginAttempt++;
        return c.loginAttempt > 5;
    }

    public static final void Welcome(final MapleClient c) {
        System.out.println("HELLO_CHANNEL···Welcome");
    }
}
