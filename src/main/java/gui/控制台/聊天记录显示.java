package gui.控制台;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.UIManager;
import gui.Start;
import static gui.Start.instance;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

public class 聊天记录显示 extends javax.swing.JFrame {

    public 聊天记录显示() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        setTitle("玩家聊天记录信息");
        initComponents();
    }

    public static final Start getInstance() {
        return instance;
    }

    public void 游戏端登陆信息输出(String str) {
        游戏端登陆信息.setText(游戏端登陆信息.getText() + str);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        游戏端登陆信息 = new javax.swing.JTextPane();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        游戏端登陆信息.setFont(new java.awt.Font("黑体", 0, 16)); // NOI18N
        jScrollPane2.setViewportView(游戏端登陆信息);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        聊天记录显示.setDefaultLookAndFeelDecorated(true);
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
                new 聊天记录显示().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane 游戏端登陆信息;
    // End of variables declaration//GEN-END:variables
}
