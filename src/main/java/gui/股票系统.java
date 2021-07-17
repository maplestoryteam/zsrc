package gui;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ScheduledFuture;
import server.Timer;
import server.Timer.EtcTimer;
import server.Timer.MapTimer;
import server.Timer.WorldTimer;

public class 股票系统 {

    public static void main(String args[]) {
        读取股票数据();
        WorldTimer.getInstance().start();
        启动股票线程();
    }
    public static ScheduledFuture<?> 股票线程 = null;
    public static int 波动 = 0;//0
    public static long 绿蜗牛值 = 0;//0
    public static long 蓝蜗牛值 = 0;//1
    public static long 红蜗牛值 = 0;//2
    public static long 花蘑菇值 = 0;//3
    public static long 绿蘑菇值 = 0;//4
    public static long 刺蘑菇值 = 0;//5

    public static void 启动股票线程() {
        if (股票线程 == null) {
            股票线程 = Timer.WorldTimer.getInstance().register(new Runnable() {
                @Override
                public void run() {
                    率蜗牛波动();
                    波动++;
                }
            }, 1000 * 1);
        }
    }

    public static void 率蜗牛波动() {
        int 绿蜗牛波动 = (int) Math.ceil(Math.random() * 200) - 100;
        绿蜗牛值 += 绿蜗牛波动;
        int 蓝蜗牛值波动 = (int) Math.ceil(Math.random() * 2000) - 1000;
        蓝蜗牛值 += 蓝蜗牛值波动;
        int 红蜗牛值波动 = (int) Math.ceil(Math.random() * 20000) - 10000;
        红蜗牛值 += 红蜗牛值波动;
        int 花蘑菇值波动 = (int) Math.ceil(Math.random() * 400) - 200;
        花蘑菇值 += 花蘑菇值波动;
        int 绿蘑菇值波动 = (int) Math.ceil(Math.random() * 4000) - 2000;
        绿蘑菇值 += 绿蘑菇值波动;
        int 刺蘑菇值波动 = (int) Math.ceil(Math.random() * 40000) - 20000;
        刺蘑菇值 += 刺蘑菇值波动;

        System.err.println("\r\nZEVMS股票第 " + 波动 + " 波动");
        System.err.println("绿蜗牛值 " + 绿蜗牛值);
        System.err.println("蓝蜗牛值 " + 蓝蜗牛值);
        System.err.println("红蜗牛值 " + 红蜗牛值);
        System.err.println("花蘑菇值 " + 花蘑菇值);
        System.err.println("绿蘑菇值 " + 绿蘑菇值);
        System.err.println("刺蘑菇值 " + 刺蘑菇值);
    }

    public static void 读取股票数据() {
        绿蜗牛值 = 取值(0);
        蓝蜗牛值 = 取值(1);
        红蜗牛值 = 取值(2);
        花蘑菇值 = 取值(3);
        绿蘑菇值 = 取值(4);
        刺蘑菇值 = 取值(5);
        System.err.println("\r\n");
        System.err.println("绿蜗牛 每股 " + 绿蜗牛值 + " 金币 / 浮动100");
        System.err.println("蓝蜗牛 每股 " + 蓝蜗牛值 + " 金币 / 浮动1000");
        System.err.println("红蜗牛 每股 " + 红蜗牛值 + " 金币 / 浮动10000");
        System.err.println("花蘑菇 每股 " + 花蘑菇值 + " 金币 / 浮动200");
        System.err.println("绿蘑菇 每股 " + 绿蘑菇值 + " 金币 / 浮动2000");
        System.err.println("刺蘑菇 每股 " + 刺蘑菇值 + " 金币 / 浮动20000"); 
    }

    public static long 取值(int a) {
        long data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM shares  WHERE a = " + a + "");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data = rs.getLong("b");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }
}
