package gui.控制台;

import database.DatabaseConnection;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.UIManager;
import gui.Start;
import static gui.Start.instance;
import gui.ZEVMS2;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;
import server.MapleItemInformationProvider;

public class 快捷面板 extends javax.swing.JFrame {

    public 快捷面板() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        setTitle("游戏资源预览");
        initComponents();
        刷新物品数量();
    }

    private void 刷新物品数量() {
        物品名称.setText("" + MapleItemInformationProvider.getInstance().getName(Integer.parseInt(物品代码.getText())) + "");
        金币拍卖行.setText("" + 获取材料数量("auctionitems1", "quantity", "itemid", Integer.parseInt(物品代码.getText())) + "");
        点券拍卖行.setText("" + 获取材料数量("auctionitems", "quantity", "itemid", Integer.parseInt(物品代码.getText())) + "");
        背包.setText("" + 获取材料数量("inventoryitems", "quantity", "itemid", Integer.parseInt(物品代码.getText())) + "");
        个人随身仓库.setText("" + 获取材料数量("bank_item", "count", "itemid", Integer.parseInt(物品代码.getText())) + "");
        家族随身仓库.setText("" + 获取材料数量("bank_item1", "count", "itemid", Integer.parseInt(物品代码.getText())) + "");
        游戏商城.setText("" + 获取材料数量("csitems", "quantity", "itemid", Integer.parseInt(物品代码.getText())) + "");
    }

    public static final Start getInstance() {
        return instance;
    }

    public static int 获取材料数量(String a, String b, String c, int d) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM " + a + " WHERE " + c + " = " + d + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data += rs.getInt(b);
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        金币拍卖行 = new javax.swing.JTextField();
        jLabel212 = new javax.swing.JLabel();
        背包 = new javax.swing.JTextField();
        物品代码 = new javax.swing.JTextField();
        jLabel213 = new javax.swing.JLabel();
        物品名称 = new javax.swing.JTextField();
        jLabel214 = new javax.swing.JLabel();
        点券拍卖行 = new javax.swing.JTextField();
        jLabel215 = new javax.swing.JLabel();
        个人随身仓库 = new javax.swing.JTextField();
        jLabel216 = new javax.swing.JLabel();
        家族随身仓库 = new javax.swing.JTextField();
        jLabel217 = new javax.swing.JLabel();
        游戏商城 = new javax.swing.JTextField();
        jLabel218 = new javax.swing.JLabel();
        查询 = new javax.swing.JButton();
        jLabel219 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        玩家拥有数量 = new javax.swing.JTable();
        jLabel220 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        玩家拥有数量1 = new javax.swing.JTable();
        jLabel221 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel222 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        拍卖行金币 = new javax.swing.JTable();
        jLabel223 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        拍卖行金币1 = new javax.swing.JTable();
        jLabel224 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        随身仓库 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel225 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        家族仓库 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        金币拍卖行.setEditable(false);
        金币拍卖行.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        金币拍卖行.setForeground(new java.awt.Color(51, 0, 255));
        jPanel3.add(金币拍卖行, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 160, 30));

        jLabel212.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel212.setText("背包/仓库；");
        jPanel3.add(jLabel212, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, 30));

        背包.setEditable(false);
        背包.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        背包.setForeground(new java.awt.Color(51, 0, 255));
        jPanel3.add(背包, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 160, 30));

        物品代码.setFont(new java.awt.Font("宋体", 0, 18)); // NOI18N
        物品代码.setText("4006000");
        jPanel3.add(物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 70, 160, 30));

        jLabel213.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel213.setText("物品名称；");
        jPanel3.add(jLabel213, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 80, 30));

        物品名称.setEditable(false);
        物品名称.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        物品名称.setForeground(new java.awt.Color(204, 0, 0));
        jPanel3.add(物品名称, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 160, 30));

        jLabel214.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel214.setText("拍卖行(金币)；");
        jPanel3.add(jLabel214, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 120, 30));

        点券拍卖行.setEditable(false);
        点券拍卖行.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        点券拍卖行.setForeground(new java.awt.Color(51, 0, 255));
        jPanel3.add(点券拍卖行, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 160, 30));

        jLabel215.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel215.setText("拍卖行(点券)；");
        jPanel3.add(jLabel215, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 120, 30));

        个人随身仓库.setEditable(false);
        个人随身仓库.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        个人随身仓库.setForeground(new java.awt.Color(51, 0, 255));
        jPanel3.add(个人随身仓库, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 160, 30));

        jLabel216.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel216.setText("随身仓库(个人)；");
        jPanel3.add(jLabel216, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, 30));

        家族随身仓库.setEditable(false);
        家族随身仓库.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        家族随身仓库.setForeground(new java.awt.Color(51, 0, 255));
        jPanel3.add(家族随身仓库, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 160, 30));

        jLabel217.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel217.setText("随身仓库(家族)；");
        jPanel3.add(jLabel217, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, 30));

        游戏商城.setEditable(false);
        游戏商城.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        游戏商城.setForeground(new java.awt.Color(51, 0, 255));
        jPanel3.add(游戏商城, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 160, 30));

        jLabel218.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel218.setText("游戏商城；");
        jPanel3.add(jLabel218, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, 30));

        查询.setText("查询");
        查询.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查询ActionPerformed(evt);
            }
        });
        jPanel3.add(查询, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 400, 90, 30));

        jLabel219.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel219.setText("角色背包；");
        jPanel3.add(jLabel219, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 140, 30));

        玩家拥有数量.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "玩家", "数量", "QQ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(玩家拥有数量);
        if (玩家拥有数量.getColumnModel().getColumnCount() > 0) {
            玩家拥有数量.getColumnModel().getColumn(0).setResizable(false);
            玩家拥有数量.getColumnModel().getColumn(0).setPreferredWidth(100);
            玩家拥有数量.getColumnModel().getColumn(1).setResizable(false);
            玩家拥有数量.getColumnModel().getColumn(2).setResizable(false);
            玩家拥有数量.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 370, 560));

        jLabel220.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel220.setText("物品代码；");
        jPanel3.add(jLabel220, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 80, 30));

        玩家拥有数量1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "玩家", "数量", "QQ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(玩家拥有数量1);
        if (玩家拥有数量1.getColumnModel().getColumnCount() > 0) {
            玩家拥有数量1.getColumnModel().getColumn(0).setResizable(false);
            玩家拥有数量1.getColumnModel().getColumn(0).setPreferredWidth(100);
            玩家拥有数量1.getColumnModel().getColumn(1).setResizable(false);
            玩家拥有数量1.getColumnModel().getColumn(2).setResizable(false);
            玩家拥有数量1.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 50, 370, 560));

        jLabel221.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel221.setText("角色仓库；");
        jPanel3.add(jLabel221, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 20, 140, 30));

        jTabbedPane1.addTab("首页", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel222.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel222.setText("金币拍卖行；");
        jPanel4.add(jLabel222, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 140, 30));

        拍卖行金币.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "玩家", "数量", "QQ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(拍卖行金币);
        if (拍卖行金币.getColumnModel().getColumnCount() > 0) {
            拍卖行金币.getColumnModel().getColumn(0).setResizable(false);
            拍卖行金币.getColumnModel().getColumn(0).setPreferredWidth(100);
            拍卖行金币.getColumnModel().getColumn(1).setResizable(false);
            拍卖行金币.getColumnModel().getColumn(2).setResizable(false);
            拍卖行金币.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 370, 560));

        jLabel223.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel223.setText("点券拍卖行；");
        jPanel4.add(jLabel223, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 20, 140, 30));

        拍卖行金币1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "玩家", "数量", "QQ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(拍卖行金币1);
        if (拍卖行金币1.getColumnModel().getColumnCount() > 0) {
            拍卖行金币1.getColumnModel().getColumn(0).setResizable(false);
            拍卖行金币1.getColumnModel().getColumn(0).setPreferredWidth(100);
            拍卖行金币1.getColumnModel().getColumn(1).setResizable(false);
            拍卖行金币1.getColumnModel().getColumn(2).setResizable(false);
            拍卖行金币1.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel4.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 50, 370, 560));

        jLabel224.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel224.setText("随身仓库；");
        jPanel4.add(jLabel224, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 20, 140, 30));

        随身仓库.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "玩家", "数量", "QQ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(随身仓库);
        if (随身仓库.getColumnModel().getColumnCount() > 0) {
            随身仓库.getColumnModel().getColumn(0).setResizable(false);
            随身仓库.getColumnModel().getColumn(0).setPreferredWidth(100);
            随身仓库.getColumnModel().getColumn(1).setResizable(false);
            随身仓库.getColumnModel().getColumn(2).setResizable(false);
            随身仓库.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel4.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 50, 370, 560));

        jTabbedPane1.addTab("**2", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel225.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel225.setText("家族仓库；");
        jPanel5.add(jLabel225, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 140, 30));

        家族仓库.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "玩家", "数量", "QQ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(家族仓库);
        if (家族仓库.getColumnModel().getColumnCount() > 0) {
            家族仓库.getColumnModel().getColumn(0).setResizable(false);
            家族仓库.getColumnModel().getColumn(0).setPreferredWidth(100);
            家族仓库.getColumnModel().getColumn(1).setResizable(false);
            家族仓库.getColumnModel().getColumn(2).setResizable(false);
            家族仓库.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel5.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 370, 560));

        jTabbedPane1.addTab("**3", jPanel5);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 650));

        jTabbedPane2.addTab("材料查询", jPanel1);
        jTabbedPane2.addTab("财产查询", jPanel2);

        getContentPane().add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 680));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void 查询ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查询ActionPerformed
        boolean result1 = this.物品代码.getText().matches("[0-9]+");
        if (result1) {
            刷新物品数量();
            刷新物品背包数量();
            刷新物品背包数量2();
            刷新金币排卖行();
            刷新点券排卖行();
            刷新随身仓库();
            刷新家族仓库();
        }

    }//GEN-LAST:event_查询ActionPerformed

    private void 刷新家族仓库() {
        for (int i = ((DefaultTableModel) (this.家族仓库.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.家族仓库.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM bank_item1  WHERE itemid = " + Integer.parseInt(物品代码.getText()) + "  order by count desc");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 家族仓库.getModel()).insertRow(家族仓库.getRowCount(), new Object[]{
                    获取家族名称(rs.getInt("cid")), //账号ID
                    rs.getInt("count"), //账号
                    账号ID取绑定QQ(角色ID取账号ID(rs.getInt("cid")))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String 获取家族名称(int guildId) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM guilds WHERE guildid = ?");
            ps.setInt(1, guildId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族名称出错 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    private void 刷新随身仓库() {
        for (int i = ((DefaultTableModel) (this.随身仓库.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.随身仓库.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM bank_item  WHERE itemid = " + Integer.parseInt(物品代码.getText()) + "  order by count desc");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 随身仓库.getModel()).insertRow(随身仓库.getRowCount(), new Object[]{
                    角色ID取角色名(rs.getInt("cid")), //账号ID
                    rs.getInt("count"), //账号
                    账号ID取绑定QQ(角色ID取账号ID(rs.getInt("cid")))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void 刷新点券排卖行() {
        for (int i = ((DefaultTableModel) (this.拍卖行金币1.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.拍卖行金币1.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM auctionitems  WHERE itemid = " + Integer.parseInt(物品代码.getText()) + "  order by quantity desc");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 拍卖行金币1.getModel()).insertRow(拍卖行金币1.getRowCount(), new Object[]{
                    rs.getString("characterName"), //账号ID
                    rs.getInt("quantity"), //账号
                    账号ID取绑定QQ(角色ID取账号ID(rs.getInt("characterid")))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void 刷新金币排卖行() {
        for (int i = ((DefaultTableModel) (this.拍卖行金币.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.拍卖行金币.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM auctionitems1  WHERE itemid = " + Integer.parseInt(物品代码.getText()) + "  order by quantity desc");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 拍卖行金币.getModel()).insertRow(拍卖行金币.getRowCount(), new Object[]{
                    rs.getString("characterName"), //账号ID
                    rs.getInt("quantity"), //账号
                    账号ID取绑定QQ(角色ID取账号ID(rs.getInt("characterid")))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private void 刷新物品背包数量() {
        for (int i = ((DefaultTableModel) (this.玩家拥有数量.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.玩家拥有数量.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM inventoryitems  WHERE itemid = " + Integer.parseInt(物品代码.getText()) + " && characterid > 0 order by quantity desc");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 玩家拥有数量.getModel()).insertRow(玩家拥有数量.getRowCount(), new Object[]{
                    角色ID取角色名(rs.getInt("characterid")) + " [" + rs.getInt("characterid") + "]", //账号ID
                    rs.getInt("quantity"), //账号
                    账号ID取绑定QQ(角色ID取账号ID(rs.getInt("characterid")))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private void 刷新物品背包数量2() {
        for (int i = ((DefaultTableModel) (this.玩家拥有数量1.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.玩家拥有数量1.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM inventoryitems  WHERE itemid = " + Integer.parseInt(物品代码.getText()) + " && accountid > 0 order by quantity desc");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 玩家拥有数量1.getModel()).insertRow(玩家拥有数量1.getRowCount(), new Object[]{
                    rs.getInt("accountid"), //账号ID
                    rs.getInt("quantity"), //账号
                    账号ID取绑定QQ(rs.getInt("accountid"))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public static int 角色ID取账号ID(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT accountid as DATA FROM characters WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("角色名字取账号ID、出错");
        }
        return data;
    }

    public static String 账号ID取绑定QQ(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT qq as DATA FROM accounts WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("账号ID取账号、出错");
        }
        return data;
    }

    public static String 角色ID取角色名(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM characters WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("角色ID取角色名、出错");
        }
        return data;
    }

    public static void main(String args[]) {
        快捷面板.setDefaultLookAndFeelDecorated(true);
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
                new 快捷面板().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel212;
    private javax.swing.JLabel jLabel213;
    private javax.swing.JLabel jLabel214;
    private javax.swing.JLabel jLabel215;
    private javax.swing.JLabel jLabel216;
    private javax.swing.JLabel jLabel217;
    private javax.swing.JLabel jLabel218;
    private javax.swing.JLabel jLabel219;
    private javax.swing.JLabel jLabel220;
    private javax.swing.JLabel jLabel221;
    private javax.swing.JLabel jLabel222;
    private javax.swing.JLabel jLabel223;
    private javax.swing.JLabel jLabel224;
    private javax.swing.JLabel jLabel225;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField 个人随身仓库;
    private javax.swing.JTable 家族仓库;
    private javax.swing.JTextField 家族随身仓库;
    private javax.swing.JTable 拍卖行金币;
    private javax.swing.JTable 拍卖行金币1;
    private javax.swing.JButton 查询;
    private javax.swing.JTextField 游戏商城;
    private javax.swing.JTextField 点券拍卖行;
    private javax.swing.JTextField 物品代码;
    private javax.swing.JTextField 物品名称;
    private javax.swing.JTable 玩家拥有数量;
    private javax.swing.JTable 玩家拥有数量1;
    private javax.swing.JTextField 背包;
    private javax.swing.JTextField 金币拍卖行;
    private javax.swing.JTable 随身仓库;
    // End of variables declaration//GEN-END:variables
}
