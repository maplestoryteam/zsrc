package a;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class 雇佣数据库函数 {

    public static void 写入道具(int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, int a10, int a11, int a12, int a13, int a14, int a15, int a16, int a17, int a18, int a19, int a20, int a21, int a22, int a23, int a24, int a25, int a26, int a27, int a28, int a29, String a30) {
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO hire (cid, itemid,upgradeslots,level,str,dex,int,luk,hp,mp,watk,matk,wdef,mdef,acc,avoid,hands,speed,jump,ViciousHammer,itemEXP,durability,enhance,potential1,potential2,potential3,hpR,mpR,itemlevel,mxmxd_dakong_fumo) VALUES ( ?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?)")) {
            ps.setInt(1, a1);
            ps.setInt(2, a2);
            ps.setInt(3, a3);
            ps.setInt(4, a4);
            ps.setInt(5, a5);
            ps.setInt(6, a6);
            ps.setInt(7, a7);
            ps.setInt(8, a8);
            ps.setInt(9, a9);
            ps.setInt(10, a10);
            ps.setInt(11, a11);
            ps.setInt(12, a12);
            ps.setInt(13, a13);
            ps.setInt(14, a14);
            ps.setInt(15, a15);
            ps.setInt(16, a16);
            ps.setInt(17, a17);
            ps.setInt(18, a18);
            ps.setInt(19, a19);
            ps.setInt(20, a20);
            ps.setInt(21, a21);
            ps.setInt(22, a22);
            ps.setInt(23, a23);
            ps.setInt(24, a24);
            ps.setInt(25, a25);
            ps.setInt(26, a26);
            ps.setInt(27, a27);
            ps.setInt(28, a28);
            ps.setInt(29, a29);
            ps.setString(30, a30);
            ps.executeUpdate();

        } catch (SQLException ex) {

        }
    }

}
