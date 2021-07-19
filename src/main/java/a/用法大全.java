/*
加密脚本
10200
10201
10202
10203
10204
 */package a;

import database.DatabaseConnection;
import database1.DatabaseConnection1;
import handling.world.MapleParty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static tools.FileoutputUtil.CurrentReadable_Time;

/**
 *
 * @author Administrator
 */
public class 用法大全 {

    /*
    Integer.parseInt()//转数字
    c.getPlayer().getClient().getSession().close();//断线
     */
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                //System.out.println("可以正常使用");
                return 0;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("已经过期，无法正常使用");
                System.out.println("请在官方群签到，即可延后到期时间");
                System.exit(0);
                return 1;
            } else {
                return 2;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String 角色ID取上线喇叭(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id2 as DATA FROM shangxianlaba WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("角色ID取上线喇叭、出错");
        }
        return data;
    }

    public static int 验证商城物品(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemid as DATA FROM cashshop_modified_items WHERE itemid = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("验证商城物品 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    public static int 队列人数(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT Point as DATA FROM characterz WHERE name = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("队列人数、出错");
        }
        return data;
    }

    public static String 角色名字取账号ID(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT accountid as DATA FROM characters WHERE name = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("角色名字取账号ID、出错");
        }
        return data;
    }

    public static String 角色ID取角色名(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM characters WHERE id = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("角色ID取角色名、出错");
        }
        return data;
    }

    public static String 角色ID取账号ID(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT accountid as DATA FROM characters WHERE id = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("角色名字取账号ID、出错");
        }
        return data;
    }

    public static int 角色ID取账号ID(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT accountid as DATA FROM characters WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("角色名字取账号ID、出错");
        }
        return data;
    }

    public static String 账号ID取账号(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM accounts WHERE id = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("账号ID取账号、出错");
        }
        return data;
    }

    public static String 账号ID取账号(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM accounts WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("账号ID取账号、出错");
        }
        return data;
    }

    public static String 账号ID取绑定QQ(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT qq as DATA FROM accounts WHERE id = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("账号ID取账号、出错");
        }
        return data;
    }

    public static String 账号ID取绑定QQ(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT qq as DATA FROM accounts WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("账号ID取账号、出错");
        }
        return data;
    }

    public static String 账号取账号ID(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id as DATA FROM accounts WHERE name = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("账号取账号ID、出错");
        }
        return data;
    }

    public static int 账号取账号点券余额(String id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT ACash as DATA FROM accounts WHERE name = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("账号取账号点券余额、出错");
        }
        return data;
    }

    public static int 账号查在线(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT loggedin as DATA FROM accounts WHERE name = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("账号ID取账号、出错");
        }
        return data;
    }

    public static String 取账号绑定的QQ(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT qq as DATA FROM accounts WHERE name = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("取账号绑定的QQ、出错");
        }
        return data;
    }

    public static String 获取到期时间(String a) {
        String data = "";
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 到期时间 WHERE Name = " + a + "");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data = rs.getString("Point");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 判断账号存在(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data += 1;
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 判断名字存在(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 名字所有权");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data += 1;
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 判断物品记录(String a, String b) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 常用的发福利物品记录");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("物品代码").equals("" + a + "")) {
                    if (rs.getString("账号").equals("" + b + "")) {
                        data += 1;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 判断物品记录(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 常用的发福利物品记录");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("物品代码").equals("" + a + "")) {
                    data += 1;
                }
            }
        } catch (SQLException ex) {
        }
        return data;
    }

    public static String 判断账号密码是否正确(String a) {
        String data = "";
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data = rs.getString("mima");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 判断启动模式(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data += 1;
                }
            }
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 判断容纳人数(String a) {
        int data = 9999;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data = rs.getInt("容纳人数");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 刷新剩余时间(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 剩余时间");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data = rs.getInt("Point");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static String 获取上次启动时间(String a) {
        int sns = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT `id` FROM 登陆记录 WHERE name = " + a + " ORDER BY `id` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String SN = rs.getString("id");
                    sns = Integer.parseInt(SN);
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("出错：" + ex.getMessage());
        }
        String data = "";
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 登陆记录 ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data = rs.getString("time");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 判断账户状态(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data = rs.getInt("账户状态");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static String 关系验证(String a) {
        String data = "";
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 关系验证");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name") == null ? a == null : rs.getString("name").equals(a)) {
                    data = rs.getString("IP");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static String 取广播() {
        String data = "";
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM `广播信息` ORDER BY RAND() LIMIT 0,100;  ");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data = rs.getString("广播");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }
    private static int 本地取广播 = 0;

    public static String 本地取广播() {

        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM `广播信息` ORDER BY RAND() LIMIT 0,100;  ");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data = rs.getString("广播");
            }
            if (本地取广播 > 0) {
                System.err.println("[服务端]" + CurrentReadable_Time() + " : 服务端输出循环广播 √");
            } else {
                本地取广播++;
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 服务端输出循环广播 ×");
        }
        return data;
    }

    public static String 本地取特定广播() {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 广播信息  ");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("id") == 1) {
                    data = rs.getString("广播");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static String 取公告信息(String a) {
        String data = "";
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 服务端公告 ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("应用") == null ? a == null : rs.getString("应用").equals(a)) {
                    data = rs.getString("信息");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }
//    public  String 脚本更新() {
//        try {
//            Connection con = DatabaseConnection1.getConnection1();
//            PreparedStatement ps = con.prepareStatement("SELECT * FROM 脚本更新 ");
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Properties 設定檔 = System.getProperties();
//                try {
//                    downLoadFromUrl("http://123.207.53.97:8082/魔改文件相关/文件下载/" + rs.getString("下载路径") + "/" + rs.getString("文件名") + "", "" + rs.getString("文件名") + "", "" + 設定檔.getProperty("user.dir") + "" + rs.getString("保存路径") + "");
//                } catch (Exception e) {
//                }
//            }
//        } catch (SQLException ex) {
//        }
//    }

    public static int 启动时长排名(String name) {
        int DATA = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT rank FROM (SELECT @rownum := @rownum + 1 AS rank, `name`, `积分`, `id` FROM 启动时间, (SELECT @rownum := 0) r  ORDER BY `积分` DESC) AS T1 WHERE `id` = ?");
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DATA = rs.getInt("rank");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取出错");
        }
        return DATA;
    }

    public static int 名字验证(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 名字所有权");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data = rs.getInt("qq");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static String 开服名字(String a) {
        String data = "";
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 名字所有权");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("qq").equals("" + a + "")) {
                    data = rs.getString("name");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 剩余时间(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 剩余时间");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("Name").equals("" + a + "")) {
                    data = rs.getInt("Point");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static void 传输IP地址(int a, String b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 关系验证");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    String aa = null;
                    aa = "update 关系验证 set IP=" + b + " where name=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                    System.out.println("○ 服务限制人数:1111111111111");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static void 传输版本号(String a, int b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    String aa = null;
                    aa = "update 账号 set 版本=" + b + " where name=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static void 传输账户类型(String a, int b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    String aa = null;
                    aa = "update 账号 set 账户类型=" + b + " where name=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);

                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static int 取在线时长(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 启动时间");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("id").equals("" + a + "")) {
                    data = rs.getInt("积分");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static void 传输在线时长(int a, int b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 启动时间");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("id").equals("" + a + "")) {
                    String aa = null;
                    int 积分 = rs.getInt("积分");
                    aa = "update 启动时间 set 积分=" + (积分 + b) + " where id=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                }
            }
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 上传运行时间 √ 已经运行 " + MapleParty.服务端运行时长 + " 分钟");
            ps.close();
        } catch (SQLException ex) {
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 上传运行时间 ×");
        }
    }

    public static void 修改上线提醒语言(int a, int b) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM shangxianlaba");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("id") == a) {
                    String aa = null;
                    aa = "update shangxianlaba set id2 =" + b + " where id = " + a + ";";
                    PreparedStatement ab = DatabaseConnection.getConnection().prepareStatement(aa);
                    ab.executeUpdate(aa);
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static void 注册名字(int a, String b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 名字所有权");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    String aa = null;
                    aa = "update 账号 set 版本=" + b + " where name=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static void 传输链接信息(String name, String b, String c) {
        try (Connection con = DatabaseConnection1.getConnection1();
                PreparedStatement ps = con.prepareStatement("INSERT INTO 连接池 (name,IP,time) VALUES ( ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, b);
            ps.setString(3, c);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static void 传输启动记录(String a, String b) {
        Connection con;
        try {
            con = DatabaseConnection1.getConnection1();
        } catch (Exception ex) {
            System.out.println(ex);
            return;
        }
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO 登陆记录 (name, time) VALUES (?, ?)");
            ps.setString(1, a);
            ps.setString(2, b);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            return;
        }

    }

    public static void 传输授权认证(String a, int b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    String aa = null;
                    aa = "update 账号 set 版本=" + b + " where name=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                }
            }
        } catch (SQLException ex) {
        }
    }

    public static void 传输服务端账号数量(String a, int b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    String aa = null;
                    aa = "update 账号 set 账号数量=" + b + " where name=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static void 传输服务端角色数量(String a, int b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    String aa = null;
                    aa = "update 账号 set 角色数量=" + b + " where name=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static void 传输人数上限(String a, int b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    String aa = null;
                    aa = "update 账号 set 容纳人数=" + b + " where name=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static void 用户上线(String a, int b) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    String aa = null;
                    aa = "update 账号 set 账户状态=" + b + " where name=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
    }

    public static int 强制更新() {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 开关");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("功能").equals("强制更新")) {
                    data = rs.getInt("状态");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 脚本应用() {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 开关");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("功能").equals("脚本应用")) {
                    data = rs.getInt("状态");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 判断连接池数量(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 账号");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name").equals("" + a + "")) {
                    data += rs.getInt("name");
                }
            }
        } catch (SQLException ex) {
        }
        return data;
    }
}
