/*
◊¢≤·’À∫≈
 */
package handling.login.handler;

import client.LoginCrypto;
import constants.ServerConstants;
import database.DatabaseConnection;
import database1.DatabaseConnection1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutoRegister {

    //’À∫≈◊¢≤· ˝¡ø
    private static final int ACCOUNTS_PER_MAC = 100;//
    //Integer.parseInt(ServerProperties.getProperty("ZEV.zhhh"));
    //private static final int ACCOUNTS_PER_MAC = 100;
    public static boolean autoRegister = ServerConstants.getAutoReg();
    public static boolean success = false, mac = true;

    //≈–∂œ’À∫≈ «∑Ò¥Ê‘⁄
    public static boolean getAccountExists(String login) {
        boolean accountExists = false;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                accountExists = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("ZZ"+ex);
        }
        return accountExists;
    }
    
        public static boolean ≈–∂œ’À∫≈ «∑Ò¥Ê‘⁄(String login) {
        boolean accountExists = false;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                accountExists = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("ZZ"+ex);
        }
        return accountExists;
    }

    public static boolean getAccountExists2(String login) {
        boolean accountExists = false;
        Connection con = DatabaseConnection1.getConnection1();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT qq FROM ”Œœ∑QQ∫≈ WHERE qq = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                accountExists = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("ZZ"+ex);
        }
        return accountExists;
    }

    public static boolean ≈–∂œΩ«…´ID «∑Ò¥Ê‘⁄(String login) {
        boolean accountExists = false;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id FROM characters WHERE id = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                accountExists = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("ZZ"+ex);
        }
        return accountExists;
    }

    public static void createAccount(String login, String pwd, String eip, String macs) {
        String sockAddr = eip;
        Connection con;

        try {
            con = DatabaseConnection.getConnection();
        } catch (Exception ex) {
            System.out.println("ZZ"+ex);
            return;
        }

        try {
            ResultSet rs;
            PreparedStatement ipc = con.prepareStatement("SELECT macs FROM accounts WHERE macs = ?");
            ipc.setString(1, macs);
            rs = ipc.executeQuery();
            if (rs.first() == false || rs.last() == true && rs.getRow() < ACCOUNTS_PER_MAC) {
                PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, macs, SessionIP,qq) VALUES (?, ?, ?, ?, ?, ?,?)");
                ps.setString(1, login);
                ps.setString(2, LoginCrypto.hexSha1(pwd));
                ps.setString(3, "513251863@qq.com");
                ps.setString(4, "2020-09-03");
                ps.setString(5, macs);
                ps.setString(6, "/" + sockAddr.substring(1, sockAddr.lastIndexOf(':')));
                ps.setString(7, "123456789");
                ps.executeUpdate();
                success = true;
            }
            //  
            //  ipc.close();
            if (rs.getRow() >= ACCOUNTS_PER_MAC) {
                mac = false;
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            return;
        }
    }
}
