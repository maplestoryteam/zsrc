package a;

import database.DatabaseConnection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class 本地数据库 {

    public static String 账号取绑定QQ(String a) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("name") == null ? a == null : rs.getString("name").equals(a)) {
                    data = rs.getString("qq");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static String 取椅子备注(int a) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM qqstem");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("channel") == a) {
                    data = rs.getString("Name");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 查询QQ下是否有封禁账号(String a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE banned > 0");
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

    public static String 服务端目录() {
        String data = "";
        Properties 設定檔 = System.getProperties();
        data = 設定檔.getProperty("user.dir");
        return data;
    }

    public static String 服务端数据库目录() {
        String data = "";
        Properties 設定檔 = System.getProperties();
        data = 設定檔.getProperty("user.dir") + "\\MYSQL\\MySQL\\data\\zevms\\";
        return data;
    }

    public static String 数据库解压目录(String a) {
        String data = "";
        Properties 設定檔 = System.getProperties();
        data = 設定檔.getProperty("user.dir") + "\\MYSQL\\MySQL\\data\\" + a + ".zip";
        return data;
    }

    public static String 数据库导入目录(String a) {
        String data = "";
        Properties 設定檔 = System.getProperties();
        data = 設定檔.getProperty("user.dir") + "\\MYSQL\\MySQL\\data\\" + a + "";
        return data;
    }

    public static String 数据库下载目录() {
        String data = "";
        Properties 設定檔 = System.getProperties();
        data = 設定檔.getProperty("user.dir") + "\\MYSQL\\MySQL\\data";
        return data;
    }

    public static String 任务更新下载目录() {
        String data = "";
        Properties 設定檔 = System.getProperties();
        data = 設定檔.getProperty("user.dir") + "\\scripts";
        return data;
    }

    public static String 任务更新解压目录(String a) {
        String data = "";
        Properties 設定檔 = System.getProperties();
        data = 設定檔.getProperty("user.dir") + "\\scripts\\" + a + ".zip";
        return data;
    }

    public static String 任务更新导入目录(String a) {
        String data = "";
        Properties 設定檔 = System.getProperties();
        data = 設定檔.getProperty("user.dir") + "\\scripts\\" + a + "";
        return data;
    }

    public static void load() {
        try {
            String fPath = "I:\\Users\\divine.sql";
            Runtime rt = Runtime.getRuntime();

            Process child = rt.exec("mysql -uroot -pzevms zevms");
            OutputStream out = child.getOutputStream();
            String inStr;
            StringBuffer sb = new StringBuffer();
            String outStr;
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fPath), StandardCharsets.UTF_8));
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();

            OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            writer.write(outStr);
            writer.flush();

            out.close();
            br.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
