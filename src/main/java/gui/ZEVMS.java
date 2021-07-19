/*
ZEVMS
服务端最先启动的窗口
 */
package gui;

import abc.Game;
import com.cyb.MonitorInfoBean;
import com.cyb.dao.IMonitorService;
import com.cyb.dao.MonitorServiceImpl;
import database.DatabaseConnection;
import database1.DatabaseConnection1;
import gui.控制台.*;
import gui.网关.机器人群设置面板;
import handling.world.MapleParty;
import javazoom.jl.player.Player;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static abc.Game.*;
import static gui.Start.GetConfigValues;
import static tools.FileoutputUtil.CurrentReadable_Time;

public class ZEVMS extends javax.swing.JFrame {

    private static final ZEVMS instance = new ZEVMS();
    public static 一键还原 一键还原;
    public static 更多应用 更多应用;
    public static 锻造控制台 锻造控制台;
    public static 脚本编辑器2 脚本编辑器2;
    public static 快捷面板 快捷面板;
    public static 控制台3号 jButton1111;
    public static 活动控制台 jButton11112;
    public static 控制台2号 jButton11113;
    public static 充值卡后台 jButton11115;
    public static 控制台1号 服务端功能开关;
    public static 聊天记录显示 信息输出;
    public static 启动进度条 启动进度条;
    public static 机器人群设置面板 网关;
    public static 角色转移工具 角色转移工具;

    public static final ZEVMS getInstance() {
        return instance;
    }
    private static final ScheduledFuture<?> ts = null;
    private final int minutesLeft = 0;
    private static final Thread t = null;

    //启动框架
    public static void main(String[] args) throws Exception {
        说明();
        System.out.println("○ 服务端开始加载");
        ZEVMS.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //检测是否有网络();
        //检测运行文件();
        检测数据库();
        //播放音乐文件
        Properties 設定檔 = System.getProperties();
        String 欢迎音效播放 = 設定檔.getProperty("user.dir") + "\\dist\\lib\\Welcome.mp3";
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(欢迎音效播放));
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("○ 启动服务端登陆窗口");
                new ZEVMS().setVisible(true);
            }
        });
    }

    public static void 检测数据库() {
        System.out.println("○ 开始检测数据库");
        //附魔显示表更新
        try {
//            下载文件("http://123.207.53.97:8082/魔改文件相关/数据库/mxmxd_fumo_info.zip", "mxmxd_fumo_info.zip", "" + 数据库下载目录() + "/");
//            解压文件.解压文件(数据库解压目录("mxmxd_fumo_info"), 数据库导入目录("zevms"));
//            删除文件(数据库解压目录("mxmxd_fumo_info"));
        } catch (Exception e) {

        }

        System.out.println("○ 数据库检测完成");
    }

    public static void 检测运行文件() {
       
        //检测文件1();
        //检测文件3();        检测数据库表("divine");
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
        } catch (SQLException ex) {
        }
        return data;
    }

    //服务端启动窗口
    public ZEVMS() {}
    public void ZEVMS2() {
        Properties 設定檔 = System.getProperties();
        //设置窗口
        setTitle("");
        //加载
        initComponents();
        //文本加载
        String 输出1 = 取公告信息("服务端公告");
        //输出版本号和公告信息
        /*try {
            String versionInfo = Class2.Pv(版本);
            String unicode = new String(versionInfo.getBytes(), "UTF-8");
            unicode = unicode.replace("\\n", "\n");
            输出1 = "" + unicode + "";

        } catch (Exception e) {
            System.out.println("获取版本信息出错。");
        }*/

        //读取不到服务器的时候显示
        if ("".equals(输出1)) {
            输出1 = "\r\r\r\r\r\r\r                                                    ( >_< )与服务器链接错误  ·  ·  · \r\n"
                    + "\r\n                                                    1.可能您的网络有波动。"
                    + "\r\n                                                    2.可能服务器正在维护。"
                    + "\r\n                                                    3.可能服务器网络有波动。";
        }
        //输出公告
        print公告(输出1);
        //设置版本检测数值为0
        MapleParty.版本检测 = 0;
        //加载窗口图标
        ImageIcon icon = new ImageIcon(getClass().getResource("/gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        GetConfigValues();
    //    账号.setText("" + gui.Start.ConfigValuesMap.get("记录账号") + "");
        //刷新网关开关();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        启动服务端 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        启动服务端1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImages(null);
        setMinimumSize(new java.awt.Dimension(350, 350));
        setPreferredSize(new java.awt.Dimension(350, 350));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        启动服务端.setBackground(new java.awt.Color(255, 255, 255));
        启动服务端.setFont(new java.awt.Font("幼圆", 0, 16)); // NOI18N
     //   启动服务端.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/开机.png"))); // NOI18N
        启动服务端.setText("启动服务端");
        启动服务端.setToolTipText("");
        启动服务端.setActionCommand("");
        启动服务端.setIconTextGap(3);
        启动服务端.setPreferredSize(new java.awt.Dimension(100, 30));
        启动服务端.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                启动服务端ActionPerformed(evt);
            }
        });
        getContentPane().add(启动服务端, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 265, 210, 40));

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 450, -1, -1));

        启动服务端1.setBackground(new java.awt.Color(255, 255, 255));
        启动服务端1.setFont(new java.awt.Font("幼圆", 0, 16)); // NOI18N
        启动服务端1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/图片/pp/开机.png"))); // NOI18N
        启动服务端1.setText("启动服务端");
        启动服务端1.setToolTipText("");
        启动服务端1.setActionCommand("");
        启动服务端1.setIconTextGap(3);
        启动服务端1.setPreferredSize(new java.awt.Dimension(100, 30));
        启动服务端1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                启动服务端1ActionPerformed(evt);
            }
        });
        getContentPane().add(启动服务端1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 140, 25));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private static String 获取网络时间(String webUrl) {
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void 启动服务端ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_启动服务端ActionPerformed
        try {
            Start.startServer();
        } catch (InterruptedException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_启动服务端ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            Start.startServer();
        } catch (InterruptedException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void 启动服务端1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_启动服务端1ActionPerformed

      /*  boolean A = this.账号.getText().matches("[0-9]+");
        if (!A) {
            JOptionPane.showMessageDialog(null, "请输入正确的账号。");
            return;
        }
        if (账号.getText().equals("") || 密码.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "请输入账号密码。");
            return;
        }
        if (判断账号存在(账号.getText()) == 0) {
            JOptionPane.showMessageDialog(null, "账号不存在。");
            return;
        }
        if (!判断账号密码是否正确(账号.getText()).equals(密码.getText())) {
            JOptionPane.showMessageDialog(null, "密码错误。");
            return;
        }
        if (判断账户状态(账号.getText()) != 0) {
            JOptionPane.showMessageDialog(null, "非常抱歉，你的账号已经被封禁。");
            return;
        }*/

     //   if ("".equals(开服名字(账号.getText()))) {
            MapleParty.开服名字 = "冒险岛";
     //   } else {
       //     MapleParty.开服名字 = 开服名字(账号.getText());
       // }
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET dropperid = ? WHERE id = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
            ps1.setInt(1, 5000);
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString2 = null;
                sqlString2 = "update configvalues set Val='71447500' where id= 5000 ;";
                PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                dropperid.executeUpdate(sqlString2);
            }
        } catch (SQLException ex) {
        }
//        MapleParty.开服名字 = 开服名字(账号.getText());
       // MapleParty.IP地址 = 关系验证(账号.getText());
                MapleParty.IP地址 = "127.0.0.1";
        
    //    MapleParty.启动账号 = 账号.getText();
        try {
            //这是没有授权的
            if (!服务端授权验证()) {
            /*    MapleParty.容纳人数 = 6;//判断容纳人数(账号.getText()
                System.out.println("○ 服务限制人数:" + MapleParty.容纳人数);
                System.out.println("○ 到期时间 : " + 获取到期时间(账号.getText()));
                System.out.println("○ 启动时间 : " + CurrentReadable_Time());
                传输账户类型(账号.getText(), 0);
                int i = compare_date(
                        "" + 获取到期时间(账号.getText()) + "",//过期日期
                        获取网络时间("http://baidu.com")//获取当前日期
                );
                if (i == 1) {
                    JOptionPane.showMessageDialog(null, "你的账号于 " + 获取到期时间(账号.getText()) + " 已到期。\r\n请在群里发送 *签到 指令进行签到续期。");
                    return;
                }
                //这是授权了的
            } else {*/
                MapleParty.容纳人数 = 999;
                System.out.println("○ 服务限制人数:999");
                System.out.println("○ 启动时间 : " + CurrentReadable_Time());
                //告诉服务器我是授权的、
                //传输账户类型(账号.getText(), 1);
            }
        } catch (SocketException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        //开始检测服务端版本
      /*  String v2 = Class2.Pgv();
        System.out.println("○ 检测服务端版本信息");
        //检测是否强制更新
        传输版本号(账号.getText(), 版本);
        if (强制更新() == 0) {
            if (Integer.parseInt(v2) != 版本) {
                //版本不对启动更新
                更新();
            }
        } else if (Integer.parseInt(v2) != 版本) {
            //版本不对启动更新
            int n = JOptionPane.showConfirmDialog(this, "检测到服务端有更新，是否进行更新？", "更新 v" + Integer.parseInt(v2) + "", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                更新();
            }
        }*/
        //空格
        System.out.println();
        //播放音效
        try {
            if (Start.Check) {
                //启动 startServer
                Start.startServer();
                //记录授权码
                记录通过授权码();
                Properties 設定檔 = System.getProperties();
                String 音效播放 = 設定檔.getProperty("user.dir") + "\\dist\\lib\\Start the success.mp3";
                try {
                    BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(音效播放));
                    Player player = new Player(buffer);
                    player.play();
                } catch (Exception e) {
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "无法重复运行。");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_启动服务端1ActionPerformed
    public void 按键开关(String a, int b) {
        int 检测开关 = gui.Start.ConfigValuesMap.get(a);
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (检测开关 > 0) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, b);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val= '0' where id= '" + b + "';";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                }
            } catch (SQLException ex) {

            }
        } else {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, b);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val= '1' where id='" + b + "';";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                }
            } catch (SQLException ex) {

            }
        }
        gui.Start.GetConfigValues();
    }

//    private void 刷新网关开关() {
//        String 显示 = "";
//        int S = gui.Start.ConfigValuesMap.get("网关开关");
//        if (S <= 0) {
//            显示 = "网关:开启";
//        } else {
//            显示 = "网关:关闭";
//        }
//        网关开关.setText(显示);
//    }
    public boolean 网速检测() {
        //服务器网速检测
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec("ping " + "" + 测速网速 + "");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            isr.close();
            br.close();

            if (null != sb && !sb.toString().equals("")) {
                if (sb.toString().indexOf("TTL") > 0) {
                } else {
                    JOptionPane.showMessageDialog(null, "延迟 " + sb.toString().indexOf("TTL") + "\r\n服务器网速高于延迟 10 ，无法启动。");
                    System.exit(0);
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }

    public boolean 网速检测2() {
        //网速检测2
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        String 输出 = "";
        String 输出1 = "";
        try {
            process = runtime.exec("ping " + "" + 测速网速 + "");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            isr.close();
            br.close();
            if (null != sb && !sb.toString().equals("")) {
                String logString = "";
                if (sb.toString().indexOf("TTL") > 0) {
                } else {
                    JOptionPane.showMessageDialog(null, "延迟 " + sb.toString().indexOf("TTL") + "\r\n服务器网速高于延迟 10 ，无法启动。");
                    System.exit(0);
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }

    public boolean isConnect() {
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec("ping " + "" + IP地址 + "");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            //System.out.println("返回值为:" + sb);
            is.close();
            isr.close();
            br.close();

            if (null != sb && !sb.toString().equals("")) {
                String logString = "";
                if (sb.toString().indexOf("TTL") > 0) {
                    //网速检测();
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "本地网络不稳定，无法链接到服务器。");

                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }

    //获取时间
    private static String getWebsiteDatetime(String webUrl) {
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //检测 zevms079.exe 是否存在
    public static void 检测文件1() {
        try {
            ByteArrayOutputStream baos = null;
            InputStream os = null;
            String s = "";
            Process p = Runtime.getRuntime().exec("cmd /c tasklist");
            baos = new ByteArrayOutputStream();
            final Runtime runtime = Runtime.getRuntime();
            Properties ZEV = System.getProperties();
            Process process = null;
            File file = new File(ZEV.getProperty("user.dir") + "/更新程序.exe");
            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                try {
                    下载文件("http://123.207.53.97:8082/魔改文件相关/文件下载/更新程序.exe", "更新程序.exe", "" + ZEV.getProperty("user.dir") + "/");
                } catch (Exception e) {
                    System.err.println("○ 检测必要的运行文件出错。");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void 检测文件3() {
        try {
            ByteArrayOutputStream baos = null;
            InputStream os = null;
            String s = "";
            Process p = Runtime.getRuntime().exec("cmd /c tasklist");
            baos = new ByteArrayOutputStream();
            final Runtime runtime = Runtime.getRuntime();
            Properties ZEV = System.getProperties();
            Process process = null;
            File file = new File(ZEV.getProperty("user.dir") + "/Load/游戏频道状态显示.txt");
            //如果文件夹不存在则创建
            if (!file.exists() && !file.isDirectory()) {
                try {
                    下载文件("http://123.207.53.97:8082/魔改文件相关/文件下载/游戏频道状态显示.txt", "游戏频道状态显示.txt", "" + ZEV.getProperty("user.dir") + "/Load/");
                } catch (Exception e) {
                    System.err.println("○ 检测必要的运行文件出错3。" + e);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void 下载文件(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }

    }

    public static boolean 检测是否有网络() {
        System.out.println("○ 开始检测服务器网络通信");
        long start = System.currentTimeMillis();
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        String 输出 = "";
        String 输出1 = "";
        try {
            process = runtime.exec("ping " + "" + 测速网速 + "");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            isr.close();
            br.close();

            if (null != sb && !sb.toString().equals("")) {
                String logString = "";
                if (sb.toString().indexOf("TTL") > 0) {
                    long now = System.currentTimeMillis() - start;
                    System.out.println("○ 服务器通信检测完成，" + now + "");
                } else {
                    JOptionPane.showMessageDialog(null, "没有网络或者网络不稳定，无法开启服务端。");
                    System.exit(0);
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println("○ 服务器通信检测失败，" + e + "");

        }
        return connect;
    }

    public static boolean 检测是否有网络2() {
        System.out.println("○ 开始与服务端网关连接");
        long start = System.currentTimeMillis();
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        String 输出 = "";
        String 输出1 = "";
        try {
            process = runtime.exec("ping " + "" + IP地址 + "");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            isr.close();
            br.close();

            if (null != sb && !sb.toString().equals("")) {
                String logString = "";
                if (sb.toString().indexOf("TTL") > 0) {
                    long now = System.currentTimeMillis() - start;
                    System.out.println("○ 与服务端网关连接完成，" + now + "");
                } else {
                    JOptionPane.showMessageDialog(null, "未连接到网关，网络故障或者正在维护。");
                    System.exit(0);
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println("○ 连接到网关失败，" + e + "");
        }
        return connect;
    }

    //更新程序
    public static void 更新() {
        //启动更新程序
        try {
            InetAddress addr = InetAddress.getLocalHost();
            final Runtime runtime = Runtime.getRuntime();
            Process process = null;
            Properties 設定檔 = System.getProperties();
            try {
                //打开指定程序
                process = runtime.exec("" + 設定檔.getProperty("user.dir") + "/" + 更新程序 + "");
                //关闭指定程序
                Runtime.getRuntime().exec("Taskkill /IM 启动服务端.exe");
                Runtime.getRuntime().exec("Taskkill /IM " + 設定檔.getProperty("user.dir") + "/启动服务端.exe");
                System.exit(0);
            } catch (final Exception e) {
                System.out.println("自动更新程序启动失败；");
                System.out.println("失败原因：" + process + "");
                System.out.println("失败原因：未安装NET4.5支持库");
            }
        } catch (Exception e) {
        }
    }

    //启动远控
    public static void 启动远控() {
        //启动更新程序
        try {
            InetAddress addr = InetAddress.getLocalHost();
            final Runtime runtime = Runtime.getRuntime();
            Process process = null;
            Properties 設定檔 = System.getProperties();
            try {
                //打开指定程序
                process = runtime.exec("" + 設定檔.getProperty("user.dir") + "/jdk/bin/zevms079.exe");
            } catch (final Exception e) {
                System.out.println("缺少运行文件，查看是否被查杀，请重启服务端，或者联系作者:71447500");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //检测服务器配置是否可以启动
    public static void 检测服务器配置() throws Exception {
        //检测服务器内存
        long start = System.currentTimeMillis();
        System.out.println("○ 开始检测服务器配置");
        IMonitorService service = new MonitorServiceImpl();
        MonitorInfoBean monitorInfo = service.getMonitorInfoBean();
        int 服务器内存 = (int) (monitorInfo.getTotalMemorySize() / 1024 / 1024);
        if (服务器内存 < 4) {
            System.out.println("○ 服务端最低要求 4G 运行内存，你的服务器内存为  " + (服务器内存 + 1) + " ，不符合最低运行要求。");
            System.exit(0);
        }
        long now = System.currentTimeMillis() - start;
        System.out.println("○ 服务器配置检测完成(4/" + (服务器内存 + 1) + ")，" + now + "");
    }

    //检测机器人是否启动
    public static boolean 验证机器人是否启动() {
        //检测是否启动指定程序
        ByteArrayOutputStream baos = null;
        InputStream os = null;
        String s = "";
        try {
            Process p = Runtime.getRuntime().exec("cmd /c tasklist");
            baos = new ByteArrayOutputStream();
            os = p.getInputStream();
            byte[] b = new byte[256];
            while (os.read(b) > 0) {
                baos.write(b);
            }
            s = baos.toString();
            if (s.indexOf("CQA.exe") >= 0) {

            } else {
                System.out.println("你选择打开机器人，但机器人未启动，请启动机器人再启动服务端。");
                System.exit(0);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }

        System.out.println("info:" + url + " download success");

    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void 远控启动开关() throws IOException {
        long start = System.currentTimeMillis();
        //System.out.println("○ 开始读取配置信息yans");
        try {
            URL url = new URL("http://123.207.53.97:8082/魔改文件相关/服务端控制/远控启动开关.txt");
            Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
            int c;
            while ((c = reader.read()) != -1) {
                switch (c) {
                    case 48:
                        //0 = 48
                        MapleParty.远控启动开关 = 0;
                        //System.out.print("开");
                        break;
                    case 49:
                        //1 = 49
                        MapleParty.远控启动开关 = 1;
                        //System.out.print("关");
                        break;
                    default:
                        System.out.print("yans错误！！！");
                        break;
                }
            }
            long now = System.currentTimeMillis() - start;
            System.out.println("○ 取配置信息yans完成，" + now + "");
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "数据读取失败:-yans\r\n请联系小z");
            System.exit(0);
        }
    }

    public static void 脚本编辑器授权开关() throws IOException {
        long start = System.currentTimeMillis();
        //System.out.println("○ 开始读取配置信息qans");
        try {
            URL url = new URL("http://123.207.53.97:8082/魔改文件相关/服务端控制/脚本编辑器开关.txt");
            Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
            int c;
            while ((c = reader.read()) != -1) {
                switch (c) {
                    case 48:
                        //0 = 48
                        MapleParty.脚本编辑器授权开关 = 0;
                        //System.out.print("开");
                        break;
                    case 49:
                        //1 = 49
                        MapleParty.脚本编辑器授权开关 = 1;
                        //System.out.print("关");
                        break;
                    default:
                        System.out.print("qans错误！！！");
                        break;
                }
            }
            long now = System.currentTimeMillis() - start;
            System.out.println("○ 取配置信息bans完成，" + now + "");
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "数据读取失败:-bans\r\n请联系小z");
            System.exit(0);
        }
    }

    public static void 强制更新开关() throws IOException {
        long start = System.currentTimeMillis();
        //System.out.println("○ 开始读取配置信息qans");
        try {
            URL url = new URL("http://123.207.53.97:8082/魔改文件相关/服务端控制/强制更新开关.txt");
            Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
            int c;
            while ((c = reader.read()) != -1) {
                switch (c) {
                    case 48:
                        //0 = 48
                        MapleParty.强制更新开关 = 0;
                        //System.out.print("开");
                        break;
                    case 49:
                        //1 = 49
                        MapleParty.强制更新开关 = 1;
                        //System.out.print("关");
                        break;
                    default:
                        System.out.print("qans错误！！！");
                        break;
                }
            }
            long now = System.currentTimeMillis() - start;
            System.out.println("○ 取配置信息qans完成，" + now + "");
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "数据读取失败:-qans\r\n请联系小z");
            System.exit(0);
        }
    }

    public static void 广告阅读开关() throws IOException {
        long start = System.currentTimeMillis();
        //System.out.println("○ 开始读取配置信息qans");
        try {
            URL url = new URL("http://123.207.53.97:8082/魔改文件相关/服务端控制/广告阅读开关.txt");
            Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
            int c;
            while ((c = reader.read()) != -1) {
                switch (c) {
                    case 48:
                        //0 = 48
                        MapleParty.广告阅读开关 = 0;
                        //System.out.print("开");
                        break;
                    case 49:
                        //1 = 49
                        MapleParty.广告阅读开关 = 1;
                        //System.out.print("关");
                        break;
                    default:
                        System.out.print("gans错误！！！");
                        break;
                }
            }
            long now = System.currentTimeMillis() - start;
            System.out.println("○ 取配置信息gans完成，" + now + "");
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "数据读取失败:-gans\r\n请联系小z");
            System.exit(0);
        }
    }

    public static void 配置检测开关() throws IOException {
        long start = System.currentTimeMillis();
        //System.out.println("○ 开始读取配置信息pans");
        try {
            URL url = new URL("http://123.207.53.97:8082/魔改文件相关/服务端控制/配置检测开关.txt");
            Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
            int c;
            while ((c = reader.read()) != -1) {
                switch (c) {
                    case 48:
                        //0 = 48
                        MapleParty.配置检测开关 = 0;
                        //System.out.print("开");
                        break;
                    case 49:
                        //1 = 49
                        MapleParty.配置检测开关 = 1;
                        //System.out.print("关");
                        break;
                    default:
                        System.out.print("pans错误！！！");
                        break;
                }
            }
            long now = System.currentTimeMillis() - start;
            System.out.println("○ 取配置信息pans完成，" + now + "");
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "数据读取失败:-pans\r\n请联系小z");
            System.exit(0);
        }
    }

    public static void 维护更新开关() throws IOException {
        long start = System.currentTimeMillis();
        //System.out.println("○ 开始读取配置信息pans");
        try {
            URL url = new URL("http://123.207.53.97:8082/魔改文件相关/服务端控制/维护更新开关.txt");
            Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
            int c;
            while ((c = reader.read()) != -1) {
                switch (c) {
                    case 48:
                        JOptionPane.showMessageDialog(null, "服务端正在维护中，无法开启，\r\n请留意QQ群或者8091最新通知。");
                        System.exit(0);
                        break;
                    case 49:

                        break;
                    default:
                        System.out.print("pans错误！！！");
                        break;
                }
            }
            long now = System.currentTimeMillis() - start;
            //System.out.println("○ 取配置信息pans完成，" + now + "");
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "数据读取失败:-wans\r\n请联系小z");
            System.exit(0);
        }
    }

    public static void 自动更新开关() throws IOException {

        long start = System.currentTimeMillis();
        //System.out.println("○ 开始读取配置信息zans");
        try {
            URL url = new URL("http://123.207.53.97:8082/魔改文件相关/服务端控制/自动更新开关.txt");
            Reader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));
            int c;
            while ((c = reader.read()) != -1) {
                switch (c) {
                    case 48:
                        //0 = 48
                        MapleParty.自动更新开关 = 0;
                        //System.out.print("开");
                        break;
                    case 49:
                        //1 = 49
                        MapleParty.自动更新开关 = 1;
                        //System.out.print("关");
                        break;
                    default:
                        System.out.print("pans错误！！！");
                        break;
                }
            }
            long now = System.currentTimeMillis() - start;
            System.out.println("○ 取配置信息zans完成，" + now + "");
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "数据读取失败:-zans\r\n请联系小z");
            System.exit(0);
        }
    }

    class D extends JDialog {

        public D() {
            setVisible(true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
    }

    private void print公告(String str) {
        //公告.setText(str);
    }

    public static void 一键还原() {

        if (一键还原 != null) {
            一键还原.dispose();
        }
        一键还原 = new 一键还原();
        一键还原.setVisible(true);

    }

    public static void 更多应用() {
        if (更多应用 != null) {
            更多应用.dispose();
        }
        更多应用 = new 更多应用();
        更多应用.setVisible(true);

    }

    public static void 锻造控制台() {
        if (锻造控制台 != null) {
            锻造控制台.dispose();
        }
        锻造控制台 = new 锻造控制台();
        锻造控制台.setVisible(true);

    }

    public static void 角色转移工具() {
        if (角色转移工具 != null) {
            角色转移工具.dispose();
        }
        角色转移工具 = new 角色转移工具();
        角色转移工具.setVisible(true);

    }

    public static void 快捷面板() {

        if (快捷面板 != null) {
            快捷面板.dispose();
        }
        快捷面板 = new 快捷面板();
        快捷面板.setVisible(true);

    }

    public static void 内部交流室() {

        if (一键还原 != null) {
            一键还原.dispose();
        }
        一键还原 = new 一键还原();
        一键还原.setVisible(true);

    }

    public static void 副本控制台() {
        if (jButton1111 != null) {
            jButton1111.dispose();
        }
        jButton1111 = new 控制台3号();
        jButton1111.setVisible(true);
    }

    public static void 经济控制台() {
        if (jButton11113 != null) {
            jButton11113.dispose();
        }
        jButton11113 = new 控制台2号();
        jButton11113.setVisible(true);
    }

    public static void 活动控制台() {
        if (jButton11112 != null) {
            jButton11112.dispose();
        }
        jButton11112 = new 活动控制台();
        jButton11112.setVisible(true);
    }

    public static void 启动进度条() {
        if (启动进度条 != null) {
            启动进度条.dispose();
        }
        启动进度条 = new 启动进度条();
        启动进度条.setVisible(true);
    }

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton 启动服务端;
    private javax.swing.JButton 启动服务端1;
    // End of variables declaration//GEN-END:variables

}
/*
@echo off
@title ZEVMS冒险岛(079)游戏服务端[监控窗口]
color 1A
set PATH=jdk\jre\bin
set JRE_HOME=jdk\jre
set JAVA_HOME=jdk\jre\bin
set CLASSPATH=.;dist\*
java -server -Dnet.sf.odinms.wzpath=wz gui.ZEVMS
pause


不显示窗口
if "%1"=="h" goto begin 
start mshta vbscript:createobject("wscript.shell").run("""%~nx0"" h",0)
(window.close)&&exit 
:begin

@echo off
if "%1"=="h" goto begin 
start mshta vbscript:createobject("wscript.shell").run("""%~nx0"" h",0)(window.close)&&exit 
:begin
set PATH=jdk\jre\bin
set JRE_HOME=jdk\jre
set JAVA_HOME=jdk\jre\bin
set CLASSPATH=.;jdk\*
java -server -Dnet.sf.odinms.wzpath=wz gui.ZEVMSdlQ
 */
