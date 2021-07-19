package gui.控制台;

import gui.Start;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import javax.swing.*;

import static gui.Start.instance;
import static tools.FileoutputUtil.CurrentReadable_Time;

public class 游戏商城登陆显示窗 extends javax.swing.JFrame {

    public 游戏商城登陆显示窗() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        setTitle("游戏商城登陆显示窗");
        initComponents();
    }

    public static final Start getInstance() {
        return instance;
    }

    public void 游戏商城登陆显示窗(String str) {
        队列显示框.setText("[" + CurrentReadable_Time() + "]:" + str + "\r\n" + 队列显示框.getText() + "");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        队列显示框 = new javax.swing.JTextPane();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        队列显示框.setBackground(new java.awt.Color(0, 0, 0));
        队列显示框.setFont(new java.awt.Font("幼圆", 1, 16)); // NOI18N
        队列显示框.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(队列显示框);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 800));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        游戏商城登陆显示窗.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            //UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new 游戏商城登陆显示窗().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane 队列显示框;
    // End of variables declaration//GEN-END:variables
}
