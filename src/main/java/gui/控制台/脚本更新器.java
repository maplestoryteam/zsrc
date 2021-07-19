package gui.控制台;

import database1.DatabaseConnection1;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static download.Toupdate.downLoadFromUrl;

public class 脚本更新器 extends javax.swing.JFrame {

    public 脚本更新器() {

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/pp/2.png"));
        Image background = new ImageIcon("gui/1.png").getImage();
        setIconImage(icon.getImage());
        setTitle("在线更新程序");
        initComponents();
        显示.setText("正在准备更新程序...");
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 5);
                    开始更新();
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

    public void Z(int i) {
        启动进度条.setValue(i);
    }
    public static int 进度 = 0;
    public static int 量 = 0;

    public void 开始更新() {
        int 进度值 = 100 / 计算更新数量();
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 脚本更新 ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Properties 設定檔 = System.getProperties();
                try {
                    downLoadFromUrl("http://123.207.53.97:8082/魔改文件相关/文件下载/" + rs.getString("下载路径") + "/" + rs.getString("文件名") + "", "" + rs.getString("文件名") + "", "" + 設定檔.getProperty("user.dir") + "/" + rs.getString("保存路径") + "");
                    量 += 1;
                    Z(进度 += 进度值);
                    if (量 == 计算更新数量()) {
                        显示.setText("更新完成，3秒后自动退出。");
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000 * 3);
                                    dispose();
                                } catch (InterruptedException e) {
                                }
                            }
                        }.start();
                    } else {
                        显示.setText("更新:" + rs.getString("文件名"));
                    }
                } catch (Exception e) {
                    System.out.println("" + rs.getString("文件名") + "更新出错。");
                }
            }
        } catch (SQLException ex) {
        }
    }

    public static int 计算更新数量() {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 脚本更新");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data += 1;
            }
        } catch (SQLException ex) {
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        启动进度条 = new javax.swing.JProgressBar();
        显示 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(启动进度条, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 520, 20));

        显示.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        显示.setForeground(new java.awt.Color(255, 255, 255));
        显示.setText("ZEVMS在线更新程序");
        jPanel1.add(显示, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 370, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/LOGO/A1_副本.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 170));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(脚本更新器.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(脚本更新器.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(脚本更新器.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(脚本更新器.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
            // UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new 脚本更新器().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar 启动进度条;
    private javax.swing.JLabel 显示;
    // End of variables declaration//GEN-END:variables

}
