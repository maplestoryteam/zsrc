package a.http;

import abc.Game;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class 数据库更新文件 {

    public static void main(String[] args) {
        数据库检测并更新();
    }

    public static void 数据库检测并更新() {
        检测文件("accountsz.frm");
    }

    /*
    1.检测的文件名字
    2.目录
     */
    public static void 检测文件(String a) {
        try {
            ByteArrayOutputStream baos = null;
            InputStream os = null;
            String s = "";
            Process p = Runtime.getRuntime().exec("cmd /c tasklist");
            baos = new ByteArrayOutputStream();
            final Runtime runtime = Runtime.getRuntime();
            Properties ZEV = System.getProperties();
            Process process = null;
            File file = new File(ZEV.getProperty("user.dir") + "/MYSQL/MySQL/data/zevms/" + a + "");
            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                System.err.println("○ 数据库文件: " + a + " 缺失。");
                DownLoad downLoad = new DownLoad();
                String url = "http://123.207.53.97:8082/数据库文件/" + a + "";
                downLoad.downLoad(url, ZEV.getProperty("user.dir") + "/" + a + "");
                System.err.println("○ 数据库文件: " + a + " 更新成功。");
            }
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
