package gui;

import gui.Jieya.解压文件;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static a.本地数据库.*;
import static abc.sancu.FileDemo_05.删除文件;
import static gui.ZEVMS.下载文件;

public class 检测数据库表 {

    public static void 检测数据库表(String a) {
        //判断检测是否存在，不存在就下载
        try {
            ByteArrayOutputStream baos = null;
            InputStream os = null;
            String s = "";
            Process p = Runtime.getRuntime().exec("cmd /c tasklist");
            baos = new ByteArrayOutputStream();
            File file = new File(服务端数据库目录() + "/" + a + ".frm");

            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                System.err.println("○ 更新数据库:" + a);
                try {
                    下载文件("http://123.207.53.97:8082/魔改文件相关/数据库/" + a + ".zip", "" + a + ".zip", "" + 数据库下载目录() + "/");
                     解压文件.解压文件(数据库解压目录(a), 数据库导入目录("zevms"));
                     删除文件(数据库解压目录(a));
                } catch (Exception e) {
                    System.err.println("○ 检测必要的运行文件出错。");
                }
            }
        } catch (IOException ex) {

        }
    }
}
