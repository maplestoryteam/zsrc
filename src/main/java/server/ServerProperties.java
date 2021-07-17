package server;

import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import database.DatabaseConnection;
import static gui.ZEVMS.检测文件3;
import java.io.*;

/**
 * 配置台
 */
public class ServerProperties {

    private static final Properties props = new Properties();
    private static final String[] toLoad = {
        "Load\\服务端配置加载项.ini",
        "Load\\服务端加载事件.ini",
         "Load\\重生永恒属性升级设置.ini",
        /*"PVP\\战士.ini",
        "PVP\\剑客.ini",
        "PVP\\勇士.ini",
        "PVP\\英雄.ini",
        "PVP\\龙骑士.ini",
        "PVP\\准骑士.ini",
        "PVP\\圣骑士.ini",
        "PVP\\骑士.ini",
        "PVP\\魔法师.ini",
        "PVP\\火毒法师.ini",
        "PVP\\火毒巫师.ini",
        "PVP\\火毒魔导师.ini",
        "PVP\\冰雷法师.ini",
        "PVP\\冰雷巫师.ini",
        "PVP\\冰雷魔导师.ini",
        "PVP\\牧师.ini",
        "PVP\\祭师.ini",
        "PVP\\主教.ini",
        "PVP\\弓箭手.ini",
        "PVP\\猎人.ini",
        "PVP\\射手.ini",
        "PVP\\神射手.ini",
        "PVP\\飞侠.ini",
        "PVP\\刺客.ini",
        "PVP\\无影人.ini",
        "PVP\\隐士.ini",
        "PVP\\海盗.ini",*/
        "Load\\游戏频道状态显示.txt"
    };

    private ServerProperties() {

    }

    static {
        检测文件3();
        for (String s : toLoad) {
            InputStreamReader fr;
            try {
                fr = new InputStreamReader(new FileInputStream(s), "UTF-8");//UTF-8
                props.load(fr);
                fr.close();
            } catch (IOException ex) {
                //System.out.println("加载配置文件错误：" + ex);
                System.out.println("加载文件缺失，请启动服务端自我修复后重启。");
            }
        }
        try {
            //PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM auth_server_channel_ip");数据库
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM auth_ip");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                props.put(rs.getString("name") + rs.getInt("channelid"), rs.getString("value"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(0); //Big ass error.
        }

    }

    public static String getProperty(String s) {
        return props.getProperty(s);
    }

    public static void setProperty(String prop, String newInf) {
        props.setProperty(prop, newInf);
    }

    public static String getProperty(String s, String def) {
        return props.getProperty(s, def);
    }
}
