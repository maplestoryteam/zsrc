package gui.未分类;

import gui.ZEVMS;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
//从网络取得

public class 广告 extends JFrame {

    JTextField jtfUrl;  //输入图像地址url
    JButton jbGetImage;  //取图像按钮
    Image image; //获取的图像
    Toolkit toolKit;  //Toolkit对象,用于获取图像

    public 广告() {
        super("ZEVMS公告位 - < 7 秒后自动关闭窗口 > www.zevmsmxd.cn");  //调用父类构造函数
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/pp/2.png"));//引用图标
        setIconImage(icon.getImage());
        Container container = getContentPane();   //得到容器
        toolKit = getToolkit(); //得到工具包
        try {
            String urlStr = "http://123.207.53.97:8082/ad1.png";    //得到图像的URL地址
            URL url = new URL(urlStr);
            image = toolKit.getImage(url); //获取图像
            repaint(); //重绘屏幕
        } catch (MalformedURLException ex) {
            ex.printStackTrace(); //输出出错信息
        }
        container.setLayout(new FlowLayout()); //设置布局管理器
        setSize(1212, 700);  //设置窗口尺寸
        // setSize(1200, 650);  
        setVisible(true);  //设置窗口可视
        /*new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7 * 1000);//多少秒自动关闭
                    dispose();
                    CashShopServer();
                } catch (InterruptedException e) {
                }
            }
        }.start();*/
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //关闭窗口时退出程序
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            g.drawImage(image, 5, 33, this); //在组件上绘制图像
        }
    }

    public static void main(String[] args) {
        ZEVMS.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new 广告();
    }
}
