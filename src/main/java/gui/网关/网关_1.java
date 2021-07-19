package gui.网关;

import database.DatabaseConnection;
import database1.DatabaseConnection1;
import gui.控制台.控制台2号;
import handling.world.MapleParty;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class 网关_1 extends javax.swing.JFrame {

    public 网关_1() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        setTitle("ZEVMS网关后台");
        initComponents();
        MapleParty.IP地址 = "127.0.0.1";
        登录地址.setText(MapleParty.IP地址);
        网关端口.setText(取值("网关端口"));
        官方网站.setText(取值("官方网站"));
        赞助链接.setText(取值("赞助链接"));
        网关公告.setText(取值("网关公告"));
        刷新检测值();
        //new ZevmsLauncherServer(60000).start();
        //刷新网关开关();
    }

    public static String 取值(String b) {
        String data = "";
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM wz设置");
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("a").equals("" + MapleParty.IP地址 + "") && rs.getString("b").equals(b)) {
                    data = rs.getString("c");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("取值、出错");
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        网关端口 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        官方网站 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        赞助链接 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        信息显示 = new javax.swing.JScrollPane();
        网关公告 = new javax.swing.JTextPane();
        修改端口 = new javax.swing.JButton();
        登录地址 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        修改公告 = new javax.swing.JButton();
        修改链接 = new javax.swing.JButton();
        修改网站 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        客户端检测 = new javax.swing.JTable();
        检测值 = new javax.swing.JTextField();
        检测文件 = new javax.swing.JTextField();
        修改客户端检测 = new javax.swing.JButton();
        刷新客户端检测 = new javax.swing.JButton();
        新增客户端检测 = new javax.swing.JButton();
        删除客户端检测 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel3.setText("登录器公告；");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 20));
        jPanel1.add(网关端口, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 150, 30));

        jLabel2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel2.setText("登录端口；");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 80, -1, 20));
        jPanel1.add(官方网站, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 200, 320, 30));

        jLabel4.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel4.setText("官方网站；");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, -1, 20));
        jPanel1.add(赞助链接, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 300, 320, 30));

        jLabel5.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel5.setText("赞助链接；");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 280, 240, 20));

        网关公告.setFont(new java.awt.Font("幼圆", 0, 17)); // NOI18N
        信息显示.setViewportView(网关公告);

        jPanel1.add(信息显示, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 440, 380));

        修改端口.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改端口.setText("修改端口");
        修改端口.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改端口ActionPerformed(evt);
            }
        });
        jPanel1.add(修改端口, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 140, 110, 30));

        登录地址.setEditable(false);
        jPanel1.add(登录地址, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, 150, 30));

        jLabel6.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel6.setText("登录地址；");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, -1, 20));

        修改公告.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改公告.setText("修改公告");
        修改公告.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改公告ActionPerformed(evt);
            }
        });
        jPanel1.add(修改公告, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 390, 110, 30));

        修改链接.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改链接.setText("修改链接");
        修改链接.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改链接ActionPerformed(evt);
            }
        });
        jPanel1.add(修改链接, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 340, 110, 30));

        修改网站.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改网站.setText("修改网站");
        修改网站.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改网站ActionPerformed(evt);
            }
        });
        jPanel1.add(修改网站, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 240, 110, 30));

        jTabbedPane1.addTab("基础设置", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        客户端检测.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        客户端检测.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "检测文件", "检测值"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(客户端检测);
        if (客户端检测.getColumnModel().getColumnCount() > 0) {
            客户端检测.getColumnModel().getColumn(0).setResizable(false);
            客户端检测.getColumnModel().getColumn(0).setPreferredWidth(100);
            客户端检测.getColumnModel().getColumn(1).setResizable(false);
            客户端检测.getColumnModel().getColumn(1).setPreferredWidth(500);
        }

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 790, 390));
        jPanel2.add(检测值, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 410, 370, -1));
        jPanel2.add(检测文件, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 130, -1));

        修改客户端检测.setText("修改");
        修改客户端检测.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改客户端检测ActionPerformed(evt);
            }
        });
        jPanel2.add(修改客户端检测, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 410, 70, 25));

        刷新客户端检测.setText("刷新");
        刷新客户端检测.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新客户端检测ActionPerformed(evt);
            }
        });
        jPanel2.add(刷新客户端检测, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 410, 70, 25));

        新增客户端检测.setText("新增");
        新增客户端检测.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增客户端检测ActionPerformed(evt);
            }
        });
        jPanel2.add(新增客户端检测, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 410, 70, 25));

        删除客户端检测.setText("删除");
        删除客户端检测.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除客户端检测ActionPerformed(evt);
            }
        });
        jPanel2.add(删除客户端检测, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 410, 70, 25));

        jTabbedPane1.addTab("客户端检测", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jTabbedPane1.addTab("    *    ", jPanel3);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 480));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void 修改客户端检测ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改客户端检测ActionPerformed
        if (检测文件.getText().equals("")) {
            return;
        }
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM WZ检测值");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String aa = null;
                aa = "update WZ检测值 set c ='" + 检测值.getText() + "' where b ='" + 检测文件.getText() + "'&& a = '" + MapleParty.IP地址 + "';";
                PreparedStatement cc = DatabaseConnection1.getConnection1().prepareStatement(aa);
                cc.executeUpdate(aa);
            }
            刷新检测值();
        } catch (SQLException ex) {
        }
    }//GEN-LAST:event_修改客户端检测ActionPerformed

    private void 修改端口ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改端口ActionPerformed
        if ("".equals(取值("网关端口"))) {
            try (Connection con = DatabaseConnection1.getConnection1(); PreparedStatement ps = con.prepareStatement("INSERT INTO WZ设置 ( a,b,c) VALUES ( ?, ?, ?)")) {
                ps.setString(1, MapleParty.IP地址);
                ps.setString(2, "网关端口");
                ps.setString(3, "" + 网关端口.getText() + "");
                ps.executeUpdate();
            } catch (SQLException ex) {
            }
        } else {
            try {
                Connection con = DatabaseConnection1.getConnection1();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM WZ设置");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String aa = null;
                    aa = "update WZ设置 set c ='" + 网关端口.getText() + "' where b ='网关端口'&& a = '" + MapleParty.IP地址 + "';";
                    PreparedStatement cc = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    cc.executeUpdate(aa);
                }
                刷新检测值();
            } catch (SQLException ex) {
            }
        }
    }//GEN-LAST:event_修改端口ActionPerformed

    private void 修改公告ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改公告ActionPerformed
        if ("".equals(取值("网关公告"))) {
            try (Connection con = DatabaseConnection1.getConnection1(); PreparedStatement ps = con.prepareStatement("INSERT INTO WZ设置 ( a,b,c) VALUES ( ?, ?, ?)")) {
                ps.setString(1, MapleParty.IP地址);
                ps.setString(2, "网关公告");
                ps.setString(3, "" + 网关公告.getText() + "");
                ps.executeUpdate();
            } catch (SQLException ex) {
            }
        } else {
            try {
                Connection con = DatabaseConnection1.getConnection1();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM WZ设置");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String aa = null;
                    aa = "update WZ设置 set c ='" + 网关公告.getText() + "' where b ='网关公告'&& a = '" + MapleParty.IP地址 + "';";
                    PreparedStatement cc = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    cc.executeUpdate(aa);
                }
                刷新检测值();
            } catch (SQLException ex) {
            }
        }
    }//GEN-LAST:event_修改公告ActionPerformed

    private void 修改链接ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改链接ActionPerformed
        if ("".equals(取值("赞助链接"))) {
            try (Connection con = DatabaseConnection1.getConnection1(); PreparedStatement ps = con.prepareStatement("INSERT INTO WZ设置 ( a,b,c) VALUES ( ?, ?, ?)")) {
                ps.setString(1, MapleParty.IP地址);
                ps.setString(2, "赞助链接");
                ps.setString(3, "" + 赞助链接.getText() + "");
                ps.executeUpdate();
            } catch (SQLException ex) {
            }
        } else {
            try {
                Connection con = DatabaseConnection1.getConnection1();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM WZ设置");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String aa = null;
                    aa = "update WZ设置 set c ='" + 赞助链接.getText() + "' where b ='赞助链接'&& a = '" + MapleParty.IP地址 + "';";
                    PreparedStatement cc = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    cc.executeUpdate(aa);
                }
                刷新检测值();
            } catch (SQLException ex) {
            }
        }
    }//GEN-LAST:event_修改链接ActionPerformed

    private void 修改网站ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改网站ActionPerformed
        if ("".equals(取值("官方网站"))) {
            try (Connection con = DatabaseConnection1.getConnection1(); PreparedStatement ps = con.prepareStatement("INSERT INTO WZ设置 ( a,b,c) VALUES ( ?, ?, ?)")) {
                ps.setString(1, MapleParty.IP地址);
                ps.setString(2, "官方网站");
                ps.setString(3, "" + 官方网站.getText() + "");
                ps.executeUpdate();
            } catch (SQLException ex) {
            }
        } else {
            try {
                Connection con = DatabaseConnection1.getConnection1();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM WZ设置");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String aa = null;
                    aa = "update WZ设置 set c ='" + 官方网站.getText() + "' where b ='官方网站'&& a = '" + MapleParty.IP地址 + "';";
                    PreparedStatement cc = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    cc.executeUpdate(aa);
                }
                刷新检测值();
            } catch (SQLException ex) {
            }
        }
    }//GEN-LAST:event_修改网站ActionPerformed

    private void 刷新客户端检测ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新客户端检测ActionPerformed
        刷新检测值();
    }//GEN-LAST:event_刷新客户端检测ActionPerformed

    private void 新增客户端检测ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增客户端检测ActionPerformed
        try (Connection con = DatabaseConnection1.getConnection1();
                PreparedStatement ps = con.prepareStatement("INSERT INTO WZ检测值 (a,b,c ) VALUES ( ?,?,? )")) {
            ps.setString(1, MapleParty.IP地址);
            ps.setString(2, 检测文件.getText());
            ps.setString(3, 检测值.getText());
            ps.executeUpdate();
            刷新检测值();
            JOptionPane.showMessageDialog(null, "新增检测成功。");
        } catch (SQLException ex) {

        }
    }//GEN-LAST:event_新增客户端检测ActionPerformed

    private void 删除客户端检测ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除客户端检测ActionPerformed

        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (!"".equals(检测文件.getText())) {
            try {
                ps1 = DatabaseConnection1.getConnection1().prepareStatement("SELECT * FROM WZ检测值 WHERE a = ?");
                ps1.setString(1, MapleParty.IP地址);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from WZ检测值 where b = '" + 检测文件.getText() + "';";
                    ps1.executeUpdate(sqlstr);
                    刷新检测值();
                }
            } catch (SQLException ex) {

            }
        } else {
            JOptionPane.showMessageDialog(null, "选择你要删除的记录。");
        }
    }//GEN-LAST:event_删除客户端检测ActionPerformed
    public static int 网关开关1 = 0;

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(控制台2号.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        机器人群设置面板.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
            // UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new 机器人群设置面板().setVisible(true);
            }
        });
    }

    private void 刷新检测值() {
        for (int i = this.客户端检测.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.客户端检测.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM WZ检测值 WHERE a = '" + MapleParty.IP地址 + "'");//
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 客户端检测.getModel()).insertRow(客户端检测.getRowCount(), new Object[]{
                    rs.getString("b"),
                    rs.getString("c")

                });
            }
        } catch (SQLException ex) {

        }
        客户端检测.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 客户端检测.getSelectedRow();
                String a = 客户端检测.getValueAt(i, 0).toString();
                String a1 = 客户端检测.getValueAt(i, 1).toString();
                检测文件.setText(a);
                检测值.setText(a1);

            }
        });

    }

    public void 按键开关(String a, int b) {
        int 检测开关 = gui.Start.ConfigValuesMap.get(a);
        if (检测开关 <= 0) {
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from configvalues where id =" + b + "";
                    ps1.executeUpdate(sqlstr);
                }
            } catch (SQLException ex) {

            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, b);
                ps.setString(2, a);
                ps.setInt(3, 1);
                ps.executeUpdate();
            } catch (SQLException ex) {

            }
        } else {
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from configvalues where id =" + b + "";
                    ps1.executeUpdate(sqlstr);
                }
            } catch (SQLException ex) {

            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, b);
                ps.setString(2, a);
                ps.setInt(3, 0);
                ps.executeUpdate();
            } catch (SQLException ex) {

            }
        }
        gui.Start.GetConfigValues();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollPane 信息显示;
    private javax.swing.JButton 修改公告;
    private javax.swing.JButton 修改客户端检测;
    private javax.swing.JButton 修改端口;
    private javax.swing.JButton 修改网站;
    private javax.swing.JButton 修改链接;
    private javax.swing.JButton 删除客户端检测;
    private javax.swing.JButton 刷新客户端检测;
    private javax.swing.JTextField 官方网站;
    private javax.swing.JTable 客户端检测;
    private javax.swing.JButton 新增客户端检测;
    private javax.swing.JTextField 检测值;
    private javax.swing.JTextField 检测文件;
    private javax.swing.JTextField 登录地址;
    private javax.swing.JTextPane 网关公告;
    private javax.swing.JTextField 网关端口;
    private javax.swing.JTextField 赞助链接;
    // End of variables declaration//GEN-END:variables
}
