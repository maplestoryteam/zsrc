package gui.图片.gui1;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import javazoom.jl.player.Player;

public class TestURL {

    public static void main(String[] args) throws IOException {
        test4();
        //test3();
        //test2();
        //test();
        //获取游戏公告1();
    }

    public static void test4() throws IOException {
        URL url = new URL("http://123.207.53.97:8082/服务端控制/配置检测开关.txt");
        //打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。 
        Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
        int c;
        while ((c = reader.read()) != -1) {
            System.out.println((char) c);
        }
        reader.close();
    }

    /**
     * 获取URL指定的资源。
     *
     * @throws IOException
     */
    public static void test42() throws IOException {
        URL url = new URL("http://123.207.53.97:8082/服务端控制/配置检测开关.txt");
//获得此 URL 的内容。 
        Object obj = url.getContent();
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(obj.getClass().getName()));
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
        }
        System.out.println(obj.getClass().getName());
    }

    /**
     * 获取URL指定的资源
     *
     * @throws IOException
     */
    public static void test3() throws IOException {
        URL url = new URL("http://www.hrtsea.com/down/soft/45.htm");
//返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。 
        URLConnection uc = url.openConnection();
//打开的连接读取的输入流。 
        InputStream in = uc.getInputStream();
        int c;
        while ((c = in.read()) != -1) {
            System.out.print(c);
        }
        in.close();
    }

    /**
     * 读取URL指定的网页内容
     *
     * @throws IOException
     */
    public static void test2() throws IOException {
        URL url = new URL("http://www.baidu.com");
//打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。 
        Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
        int c;
        while ((c = reader.read()) != -1) {
            System.out.print((char) c);
        }
        reader.close();
    }

    /**
     * 获取URL的输入流，并输出
     *
     * @throws IOException
     */
    public static void test() throws IOException {
        URL url = new URL("http://www.baidu.com");
//打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。 
        InputStream in = url.openStream();
        int c;
        while ((c = in.read()) != -1) {
            System.out.print(c);
        }
        in.close();
    }

    public static void 获取游戏公告1() throws IOException {
        URL url = new URL("http://123.207.53.97:8082/新闻.txt");
        //打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。 
        Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
        int c;
        while ((c = reader.read()) != -1) {
            if (c == 48) { //0 = 48
                System.out.print("开");
            } else if (c == 49) {
                System.out.print("关");
            } else {
                System.out.print("错误！！！");
            }
        }
        reader.close();
    }
}
