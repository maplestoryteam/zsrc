package gui.控制台;

import static abc.Game.窗口标题;
import database.DatabaseConnection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;
import scripting.NPCConversationManager;
import scripting.ReactorScriptManager;
import server.MapleItemInformationProvider;

public class 控制台2号 extends javax.swing.JFrame {

    public 控制台2号() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        Properties 設定檔 = System.getProperties();
        setTitle("" + 窗口标题 + "【 2 号控制台，可关闭】");
        initComponents();
        //豆豆类
        刷新豆豆();
        刷新物品掉落持续时间();
        刷新地图物品上限();
        刷新地图刷新频率();
    }

    private void 刷新地图刷新频率() {
        int 显示 = gui.Start.ConfigValuesMap.get("地图刷新频率");
        地图刷新频率.setText("" + 显示 + "");
    }

    private void 刷新地图物品上限() {
        int 显示 = gui.Start.ConfigValuesMap.get("地图物品上限");
        地图物品上限.setText("" + 显示 + "");
    }

    private void 刷新物品掉落持续时间() {
        int 显示 = gui.Start.ConfigValuesMap.get("物品掉落持续时间");
        物品掉落持续时间.setText("" + 显示 + "");
    }
//    public void 刷新物品掉落持续时间() {
//        for (int i = ((DefaultTableModel) (this.物品掉落持续时间显示.getModel())).getRowCount() - 1; i >= 0; i--) {
//            ((DefaultTableModel) (this.物品掉落持续时间显示.getModel())).removeRow(i);
//        }
//        try {
//            Connection con = DatabaseConnection.getConnection();
//            PreparedStatement ps = null;
//            ResultSet rs = null;
//            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id = 998 ");
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                ((DefaultTableModel) 物品掉落持续时间显示.getModel()).insertRow(物品掉落持续时间显示.getRowCount(), new Object[]{rs.getString("Val")});
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    private void 刷新豆豆() {
        打豆豆力度.setText("" + gui.Start.ConfigValuesMap.get("豆豆机力度") + "");
        豆豆进洞率.setText("" + gui.Start.ConfigValuesMap.get("豆豆机进洞概率") + "");
        进洞开奖率.setText("" + gui.Start.ConfigValuesMap.get("豆豆机进洞开奖概率") + "");
        豆豆稀有率.setText("" + gui.Start.ConfigValuesMap.get("豆豆机豆豆稀有率") + "");
        红豆豆加开奖率.setText("" + gui.Start.ConfigValuesMap.get("红豆豆加开奖率") + "");
        蓝豆豆加开奖率.setText("" + gui.Start.ConfigValuesMap.get("蓝豆豆加开奖率") + "");
        绿豆豆加开奖率.setText("" + gui.Start.ConfigValuesMap.get("绿豆豆加开奖率") + "");
        豆豆奖励.setText("" + gui.Start.ConfigValuesMap.get("豆豆机豆豆奖励数量") + "");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        游戏爆物 = new javax.swing.JPanel();
        查询物品掉落 = new javax.swing.JButton();
        查询物品掉落代码 = new javax.swing.JTextField();
        查询怪物掉落 = new javax.swing.JButton();
        查询怪物掉落代码 = new javax.swing.JTextField();
        删除指定的掉落按键 = new javax.swing.JButton();
        删除指定的掉落 = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        怪物爆物 = new javax.swing.JTable();
        怪物爆物序列号 = new javax.swing.JTextField();
        怪物爆物怪物代码 = new javax.swing.JTextField();
        怪物爆物物品代码 = new javax.swing.JTextField();
        怪物爆物爆率 = new javax.swing.JTextField();
        怪物爆物物品名称 = new javax.swing.JTextField();
        删除怪物爆物 = new javax.swing.JButton();
        添加怪物爆物 = new javax.swing.JButton();
        jLabel120 = new javax.swing.JLabel();
        jLabel211 = new javax.swing.JLabel();
        jLabel212 = new javax.swing.JLabel();
        jLabel213 = new javax.swing.JLabel();
        修改怪物爆物 = new javax.swing.JButton();
        刷新怪物爆物 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        世界爆物 = new javax.swing.JTable();
        世界爆物序列号 = new javax.swing.JTextField();
        世界爆物物品代码 = new javax.swing.JTextField();
        世界爆物爆率 = new javax.swing.JTextField();
        添加世界爆物 = new javax.swing.JButton();
        删除世界爆物 = new javax.swing.JButton();
        jLabel210 = new javax.swing.JLabel();
        jLabel202 = new javax.swing.JLabel();
        jLabel209 = new javax.swing.JLabel();
        世界爆物名称 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        修改世界爆物 = new javax.swing.JButton();
        刷新世界爆物 = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jLabel216 = new javax.swing.JLabel();
        jLabel318 = new javax.swing.JLabel();
        jLabel321 = new javax.swing.JLabel();
        jLabel323 = new javax.swing.JLabel();
        jLabel315 = new javax.swing.JLabel();
        jLabel316 = new javax.swing.JLabel();
        jLabel317 = new javax.swing.JLabel();
        修改物品掉落持续时间 = new javax.swing.JButton();
        物品掉落持续时间 = new javax.swing.JTextField();
        jLabel320 = new javax.swing.JLabel();
        刷新怪物卡片 = new javax.swing.JButton();
        地图物品上限 = new javax.swing.JTextField();
        修改物品掉落持续时间1 = new javax.swing.JButton();
        jLabel322 = new javax.swing.JLabel();
        jLabel319 = new javax.swing.JLabel();
        jLabel324 = new javax.swing.JLabel();
        jLabel325 = new javax.swing.JLabel();
        地图刷新频率 = new javax.swing.JTextField();
        修改物品掉落持续时间2 = new javax.swing.JButton();
        jLabel330 = new javax.swing.JLabel();
        删除游戏物品 = new javax.swing.JPanel();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jSeparator18 = new javax.swing.JSeparator();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        游戏道具 = new javax.swing.JTable();
        游戏道具代码 = new javax.swing.JTextField();
        jLabel338 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        查找道具 = new javax.swing.JButton();
        删除道具 = new javax.swing.JButton();
        jLabel337 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        查找道具1 = new javax.swing.JButton();
        删除道具1 = new javax.swing.JButton();
        jLabel339 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        查找道具2 = new javax.swing.JButton();
        删除道具2 = new javax.swing.JButton();
        jLabel340 = new javax.swing.JLabel();
        jPanel46 = new javax.swing.JPanel();
        查找道具3 = new javax.swing.JButton();
        删除道具3 = new javax.swing.JButton();
        jLabel341 = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        查找道具4 = new javax.swing.JButton();
        删除道具4 = new javax.swing.JButton();
        jLabel342 = new javax.swing.JLabel();
        jLabel343 = new javax.swing.JLabel();
        jLabel344 = new javax.swing.JLabel();
        jLabel345 = new javax.swing.JLabel();
        钓鱼管理 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        钓鱼物品 = new javax.swing.JTable();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        jPanel32 = new javax.swing.JPanel();
        修改钓鱼物品 = new javax.swing.JButton();
        刷新钓鱼物品 = new javax.swing.JButton();
        钓鱼物品代码 = new javax.swing.JTextField();
        新增钓鱼物品 = new javax.swing.JButton();
        钓鱼物品概率 = new javax.swing.JTextField();
        钓鱼物品名称 = new javax.swing.JTextField();
        删除钓鱼物品 = new javax.swing.JButton();
        钓鱼物品序号 = new javax.swing.JTextField();
        jLabel264 = new javax.swing.JLabel();
        jLabel265 = new javax.swing.JLabel();
        jLabel266 = new javax.swing.JLabel();
        jLabel267 = new javax.swing.JLabel();
        挖矿管理 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        挖矿反应堆 = new javax.swing.JTable();
        jPanel29 = new javax.swing.JPanel();
        新增挖矿物品 = new javax.swing.JButton();
        增加挖矿物品 = new javax.swing.JTextField();
        挖矿物品概率 = new javax.swing.JTextField();
        挖矿序列号 = new javax.swing.JTextField();
        修改挖矿物品 = new javax.swing.JButton();
        物品名称 = new javax.swing.JTextField();
        删除挖矿物品1 = new javax.swing.JButton();
        jLabel260 = new javax.swing.JLabel();
        jLabel261 = new javax.swing.JLabel();
        jLabel262 = new javax.swing.JLabel();
        jLabel263 = new javax.swing.JLabel();
        jPanel54 = new javax.swing.JPanel();
        新增挖矿物品1 = new javax.swing.JButton();
        增加挖矿物品1 = new javax.swing.JTextField();
        挖矿物品概率1 = new javax.swing.JTextField();
        挖矿序列号1 = new javax.swing.JTextField();
        修改挖矿物品1 = new javax.swing.JButton();
        物品名称1 = new javax.swing.JTextField();
        删除挖矿物品2 = new javax.swing.JButton();
        jLabel326 = new javax.swing.JLabel();
        jLabel327 = new javax.swing.JLabel();
        jLabel328 = new javax.swing.JLabel();
        jLabel329 = new javax.swing.JLabel();
        新增挖矿物品2 = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        刷新挖矿奖励 = new javax.swing.JButton();
        jPanel53 = new javax.swing.JPanel();
        jButton16 = new javax.swing.JButton();
        刷新挖矿奖励1 = new javax.swing.JButton();
        副业提示语言 = new javax.swing.JLabel();
        游戏商店 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        游戏商店2 = new javax.swing.JTable();
        jPanel33 = new javax.swing.JPanel();
        查询商店2 = new javax.swing.JButton();
        查询商店 = new javax.swing.JTextField();
        jLabel270 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        删除商品 = new javax.swing.JButton();
        新增商品 = new javax.swing.JButton();
        商品序号 = new javax.swing.JTextField();
        商店代码 = new javax.swing.JTextField();
        商品物品代码 = new javax.swing.JTextField();
        商品售价金币 = new javax.swing.JTextField();
        jLabel268 = new javax.swing.JLabel();
        jLabel269 = new javax.swing.JLabel();
        jLabel271 = new javax.swing.JLabel();
        jLabel272 = new javax.swing.JLabel();
        修改商品 = new javax.swing.JButton();
        商品名称 = new javax.swing.JTextField();
        jLabel273 = new javax.swing.JLabel();
        商店提示语言 = new javax.swing.JLabel();
        反应堆设置 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        反应堆 = new javax.swing.JTable();
        删除反应堆物品代码 = new javax.swing.JTextField();
        删除反应堆物品 = new javax.swing.JButton();
        jLabel114 = new javax.swing.JLabel();
        jLabel281 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jButton20 = new javax.swing.JButton();
        反应堆序列号 = new javax.swing.JTextField();
        反应堆代码 = new javax.swing.JTextField();
        反应堆物品 = new javax.swing.JTextField();
        反应堆概率 = new javax.swing.JTextField();
        新增反应堆物品 = new javax.swing.JButton();
        删除反应堆物品1 = new javax.swing.JButton();
        查找反应堆掉落 = new javax.swing.JTextField();
        jButton36 = new javax.swing.JButton();
        查找物品 = new javax.swing.JTextField();
        jButton37 = new javax.swing.JButton();
        jLabel274 = new javax.swing.JLabel();
        jLabel275 = new javax.swing.JLabel();
        jLabel277 = new javax.swing.JLabel();
        jLabel278 = new javax.swing.JLabel();
        jLabel279 = new javax.swing.JLabel();
        jLabel280 = new javax.swing.JLabel();
        修改反应堆物品 = new javax.swing.JButton();
        jLabel282 = new javax.swing.JLabel();
        豆豆机管理 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        红豆豆加开奖率 = new javax.swing.JTextField();
        jLabel350 = new javax.swing.JLabel();
        jLabel351 = new javax.swing.JLabel();
        蓝豆豆加开奖率 = new javax.swing.JTextField();
        jLabel352 = new javax.swing.JLabel();
        绿豆豆加开奖率 = new javax.swing.JTextField();
        jLabel354 = new javax.swing.JLabel();
        jLabel355 = new javax.swing.JLabel();
        jLabel356 = new javax.swing.JLabel();
        豆豆机修改设置1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        豆豆奖励 = new javax.swing.JTextField();
        豆豆机修改设置2 = new javax.swing.JButton();
        jLabel357 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel346 = new javax.swing.JLabel();
        打豆豆力度 = new javax.swing.JTextField();
        豆豆进洞率 = new javax.swing.JTextField();
        jLabel347 = new javax.swing.JLabel();
        jLabel348 = new javax.swing.JLabel();
        进洞开奖率 = new javax.swing.JTextField();
        jLabel349 = new javax.swing.JLabel();
        豆豆稀有率 = new javax.swing.JTextField();
        豆豆机修改设置 = new javax.swing.JButton();
        jLabel353 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        药水冷却 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        药水冷却时间 = new javax.swing.JTextField();
        药水序号 = new javax.swing.JTextField();
        药水名字 = new javax.swing.JTextField();
        jLabel285 = new javax.swing.JLabel();
        jLabel286 = new javax.swing.JLabel();
        jLabel287 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        ZEVMS2提示框 = new javax.swing.JLabel();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        游戏爆物.setBackground(new java.awt.Color(255, 255, 255));
        游戏爆物.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        查询物品掉落.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        查询物品掉落.setText("查询物品掉落");
        查询物品掉落.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查询物品掉落ActionPerformed(evt);
            }
        });
        游戏爆物.add(查询物品掉落, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 100, 140, -1));

        查询物品掉落代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        查询物品掉落代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查询物品掉落代码ActionPerformed(evt);
            }
        });
        游戏爆物.add(查询物品掉落代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 70, 140, -1));

        查询怪物掉落.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        查询怪物掉落.setText("查询怪物掉落");
        查询怪物掉落.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查询怪物掉落ActionPerformed(evt);
            }
        });
        游戏爆物.add(查询怪物掉落, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 190, 140, -1));

        查询怪物掉落代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        游戏爆物.add(查询怪物掉落代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 160, 140, -1));

        删除指定的掉落按键.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除指定的掉落按键.setText("删除指定的掉落");
        删除指定的掉落按键.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除指定的掉落按键ActionPerformed(evt);
            }
        });
        游戏爆物.add(删除指定的掉落按键, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 280, 140, -1));

        删除指定的掉落.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        游戏爆物.add(删除指定的掉落, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 250, 140, -1));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "怪物爆物/(10000=1%)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        怪物爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        怪物爆物.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序列号", "怪物代码", "物品代码", "爆率", "物品名字"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        怪物爆物.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(怪物爆物);
        if (怪物爆物.getColumnModel().getColumnCount() > 0) {
            怪物爆物.getColumnModel().getColumn(0).setResizable(false);
            怪物爆物.getColumnModel().getColumn(1).setResizable(false);
            怪物爆物.getColumnModel().getColumn(2).setResizable(false);
            怪物爆物.getColumnModel().getColumn(3).setResizable(false);
            怪物爆物.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel17.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 590, 510));

        怪物爆物序列号.setEditable(false);
        怪物爆物序列号.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel17.add(怪物爆物序列号, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 80, 30));

        怪物爆物怪物代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel17.add(怪物爆物怪物代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 560, 110, 30));

        怪物爆物物品代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel17.add(怪物爆物物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 560, 110, 30));

        怪物爆物爆率.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel17.add(怪物爆物爆率, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 560, 120, 30));

        怪物爆物物品名称.setEditable(false);
        怪物爆物物品名称.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel17.add(怪物爆物物品名称, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 560, 170, 30));

        删除怪物爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除怪物爆物.setText("删除");
        删除怪物爆物.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除怪物爆物ActionPerformed(evt);
            }
        });
        jPanel17.add(删除怪物爆物, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 600, 70, 30));

        添加怪物爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        添加怪物爆物.setText("添加");
        添加怪物爆物.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                添加怪物爆物ActionPerformed(evt);
            }
        });
        jPanel17.add(添加怪物爆物, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 600, 70, 30));

        jLabel120.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel120.setText("怪物代码；");
        jPanel17.add(jLabel120, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 540, -1, -1));

        jLabel211.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel211.setText("物品代码；");
        jPanel17.add(jLabel211, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 540, -1, 20));

        jLabel212.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel212.setText("爆率；");
        jPanel17.add(jLabel212, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 540, -1, -1));

        jLabel213.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel213.setText("序列号；");
        jPanel17.add(jLabel213, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, -1, -1));

        修改怪物爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改怪物爆物.setText("修改");
        修改怪物爆物.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改怪物爆物ActionPerformed(evt);
            }
        });
        jPanel17.add(修改怪物爆物, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 600, 70, 30));

        刷新怪物爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新怪物爆物.setText("刷新怪物爆物");
        刷新怪物爆物.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新怪物爆物ActionPerformed(evt);
            }
        });
        jPanel17.add(刷新怪物爆物, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 600, 140, 30));

        jLabel39.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel39.setText("物品名；");
        jPanel17.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 540, -1, -1));

        游戏爆物.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 610, 640));

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "全局爆物/(10000=1%)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        世界爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        世界爆物.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序列号", "物品代码", "爆率", "物品名"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        世界爆物.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(世界爆物);
        if (世界爆物.getColumnModel().getColumnCount() > 0) {
            世界爆物.getColumnModel().getColumn(0).setResizable(false);
            世界爆物.getColumnModel().getColumn(0).setPreferredWidth(20);
            世界爆物.getColumnModel().getColumn(1).setResizable(false);
            世界爆物.getColumnModel().getColumn(2).setResizable(false);
            世界爆物.getColumnModel().getColumn(3).setResizable(false);
            世界爆物.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        jPanel18.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 440, 510));

        世界爆物序列号.setEditable(false);
        世界爆物序列号.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        世界爆物序列号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                世界爆物序列号ActionPerformed(evt);
            }
        });
        jPanel18.add(世界爆物序列号, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 90, 30));

        世界爆物物品代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        世界爆物物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                世界爆物物品代码ActionPerformed(evt);
            }
        });
        jPanel18.add(世界爆物物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 560, 120, 30));

        世界爆物爆率.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        世界爆物爆率.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                世界爆物爆率ActionPerformed(evt);
            }
        });
        jPanel18.add(世界爆物爆率, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 560, 90, 30));

        添加世界爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        添加世界爆物.setText("添加");
        添加世界爆物.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                添加世界爆物ActionPerformed(evt);
            }
        });
        jPanel18.add(添加世界爆物, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 600, 70, 30));

        删除世界爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除世界爆物.setText("删除");
        删除世界爆物.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除世界爆物ActionPerformed(evt);
            }
        });
        jPanel18.add(删除世界爆物, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 600, 70, 30));

        jLabel210.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel210.setText("序列号；");
        jPanel18.add(jLabel210, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, -1, -1));

        jLabel202.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel202.setText("物品代码；");
        jPanel18.add(jLabel202, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 540, -1, 20));

        jLabel209.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel209.setText("爆率；");
        jPanel18.add(jLabel209, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 540, -1, -1));

        世界爆物名称.setEditable(false);
        世界爆物名称.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        世界爆物名称.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                世界爆物名称ActionPerformed(evt);
            }
        });
        jPanel18.add(世界爆物名称, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 560, 140, 30));

        jLabel36.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel36.setText("物品名；");
        jPanel18.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 540, -1, -1));

        修改世界爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改世界爆物.setText("修改");
        修改世界爆物.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改世界爆物ActionPerformed(evt);
            }
        });
        jPanel18.add(修改世界爆物, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 600, 70, 30));

        刷新世界爆物.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新世界爆物.setText("刷新世界爆物");
        刷新世界爆物.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新世界爆物ActionPerformed(evt);
            }
        });
        jPanel18.add(刷新世界爆物, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 600, 140, 30));

        游戏爆物.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 10, 460, 640));

        jLabel62.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(204, 0, 51));
        游戏爆物.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 40, -1, -1));

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "查询", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel216.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
        jLabel216.setForeground(new java.awt.Color(204, 0, 51));
        jLabel216.setText("指定怪物查物品掉落");
        jPanel31.add(jLabel216, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 140, -1, -1));

        jLabel318.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel318.setText("指定怪物查询掉落");
        jPanel31.add(jLabel318, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        jLabel321.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel321.setText("删除指定物品掉落");
        jPanel31.add(jLabel321, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jLabel323.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel323.setText("指定物品查询掉落");
        jPanel31.add(jLabel323, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        游戏爆物.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 10, 160, 320));

        jLabel315.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
        jLabel315.setForeground(new java.awt.Color(204, 0, 51));
        jLabel315.setText("删除怪物爆物物品");
        游戏爆物.add(jLabel315, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 230, -1, -1));

        jLabel316.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
        jLabel316.setForeground(new java.awt.Color(204, 0, 51));
        jLabel316.setText("指定怪物查物品掉落");
        游戏爆物.add(jLabel316, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 140, -1, -1));

        jLabel317.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
        jLabel317.setForeground(new java.awt.Color(204, 0, 51));
        jLabel317.setText("指定物品查怪物掉落");
        游戏爆物.add(jLabel317, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 50, -1, -1));

        修改物品掉落持续时间.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改物品掉落持续时间.setText("修改确认");
        修改物品掉落持续时间.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改物品掉落持续时间ActionPerformed(evt);
            }
        });
        游戏爆物.add(修改物品掉落持续时间, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 430, 140, 30));

        物品掉落持续时间.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        游戏爆物.add(物品掉落持续时间, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 400, 90, -1));

        jLabel320.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel320.setForeground(new java.awt.Color(255, 0, 0));
        jLabel320.setText("需要重启生效，建议 10000 ");
        游戏爆物.add(jLabel320, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 640, 180, 40));

        刷新怪物卡片.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新怪物卡片.setText("刷新怪物卡片");
        刷新怪物卡片.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新怪物卡片ActionPerformed(evt);
            }
        });
        游戏爆物.add(刷新怪物卡片, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 340, 140, 30));

        地图物品上限.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        游戏爆物.add(地图物品上限, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 490, 90, -1));

        修改物品掉落持续时间1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改物品掉落持续时间1.setText("修改确认");
        修改物品掉落持续时间1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改物品掉落持续时间1ActionPerformed(evt);
            }
        });
        游戏爆物.add(修改物品掉落持续时间1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 520, 140, 30));

        jLabel322.setFont(new java.awt.Font("幼圆", 0, 20)); // NOI18N
        jLabel322.setText("秒");
        游戏爆物.add(jLabel322, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 390, 30, 40));

        jLabel319.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel319.setText("地图物品最多存在");
        游戏爆物.add(jLabel319, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 470, -1, -1));

        jLabel324.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel324.setText("物品掉落持续时间");
        游戏爆物.add(jLabel324, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 380, -1, -1));

        jLabel325.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel325.setText("地图刷新频率");
        游戏爆物.add(jLabel325, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 560, -1, -1));

        地图刷新频率.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        游戏爆物.add(地图刷新频率, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 580, 90, -1));

        修改物品掉落持续时间2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改物品掉落持续时间2.setText("修改确认");
        修改物品掉落持续时间2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改物品掉落持续时间2ActionPerformed(evt);
            }
        });
        游戏爆物.add(修改物品掉落持续时间2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 610, 140, 30));

        jLabel330.setFont(new java.awt.Font("幼圆", 0, 20)); // NOI18N
        jLabel330.setText("个");
        游戏爆物.add(jLabel330, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 480, 30, 40));

        jTabbedPane2.addTab("游戏怪物爆物", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 游戏爆物, ""); // NOI18N

        删除游戏物品.setBackground(new java.awt.Color(255, 255, 255));
        删除游戏物品.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        删除游戏物品.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));
        删除游戏物品.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));
        删除游戏物品.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        删除游戏物品.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "游戏物品操作", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        游戏道具.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        游戏道具.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "角色ID", "角色名字", "道具ID", "道具名字"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        游戏道具.setColumnSelectionAllowed(true);
        jScrollPane10.setViewportView(游戏道具);

        jPanel20.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 600, 640));
        jPanel20.add(游戏道具代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 60, 160, 30));

        jLabel338.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel338.setText("需离线操作。");
        jPanel20.add(jLabel338, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 290, 350, 20));

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        查找道具.setText("查找道具");
        查找道具.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查找道具ActionPerformed(evt);
            }
        });
        jPanel9.add(查找道具, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 30));

        删除道具.setText("删除道具");
        删除道具.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除道具ActionPerformed(evt);
            }
        });
        jPanel9.add(删除道具, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 140, 30));

        jLabel337.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel337.setText("角色背包，角色穿戴");
        jPanel9.add(jLabel337, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        jPanel20.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 100, 160, 100));

        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        查找道具1.setText("查找道具");
        查找道具1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查找道具1ActionPerformed(evt);
            }
        });
        jPanel22.add(查找道具1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 30));

        删除道具1.setText("删除道具");
        删除道具1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除道具1ActionPerformed(evt);
            }
        });
        jPanel22.add(删除道具1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 140, 30));

        jLabel339.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel339.setText("点券拍卖行");
        jPanel22.add(jLabel339, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        jPanel20.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 320, 160, 100));

        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        查找道具2.setText("查找道具");
        查找道具2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查找道具2ActionPerformed(evt);
            }
        });
        jPanel23.add(查找道具2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 30));

        删除道具2.setText("删除道具");
        删除道具2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除道具2ActionPerformed(evt);
            }
        });
        jPanel23.add(删除道具2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 140, 30));

        jLabel340.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel340.setText("金币拍卖行");
        jPanel23.add(jLabel340, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        jPanel20.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 210, 160, 100));

        jPanel46.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel46.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        查找道具3.setText("查找道具");
        查找道具3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查找道具3ActionPerformed(evt);
            }
        });
        jPanel46.add(查找道具3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 30));

        删除道具3.setText("删除道具");
        删除道具3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除道具3ActionPerformed(evt);
            }
        });
        jPanel46.add(删除道具3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 140, 30));

        jLabel341.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel341.setText("家族游戏仓库");
        jPanel46.add(jLabel341, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        jPanel20.add(jPanel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 540, 160, 100));

        jPanel47.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel47.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        查找道具4.setText("查找道具");
        查找道具4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查找道具4ActionPerformed(evt);
            }
        });
        jPanel47.add(查找道具4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 30));

        删除道具4.setText("删除道具");
        删除道具4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除道具4ActionPerformed(evt);
            }
        });
        jPanel47.add(删除道具4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 140, 30));

        jLabel342.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel342.setText("个人游戏仓库");
        jPanel47.add(jLabel342, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        jPanel20.add(jPanel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 430, 160, 100));

        jLabel343.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel343.setText("游戏道具代码；");
        jPanel20.add(jLabel343, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 40, -1, 20));

        jLabel344.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel344.setText("该功能可查询游戏内指定区域的游戏道具，可以查看");
        jPanel20.add(jLabel344, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 230, 350, 20));

        jLabel345.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel345.setText("该道具有多少玩家拥有。可以一键删除所有该道具。");
        jPanel20.add(jLabel345, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 260, 350, 20));

        删除游戏物品.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 690));

        jTabbedPane2.addTab("游戏物品删除", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 删除游戏物品); // NOI18N

        钓鱼管理.setBackground(new java.awt.Color(255, 255, 255));
        钓鱼管理.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "钓鱼管理", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        钓鱼管理.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        钓鱼物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        钓鱼物品.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "代码", "概率", "物品名称"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        钓鱼物品.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(钓鱼物品);

        钓鱼管理.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 510, 590));
        钓鱼管理.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, -1, -1));
        钓鱼管理.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, -1, -1));

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "钓鱼编辑", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        修改钓鱼物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改钓鱼物品.setText("修改");
        修改钓鱼物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改钓鱼物品ActionPerformed(evt);
            }
        });
        jPanel32.add(修改钓鱼物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, -1, 30));

        刷新钓鱼物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新钓鱼物品.setText("刷新钓鱼物品");
        刷新钓鱼物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新钓鱼物品ActionPerformed(evt);
            }
        });
        jPanel32.add(刷新钓鱼物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 30));

        钓鱼物品代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        钓鱼物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                钓鱼物品代码ActionPerformed(evt);
            }
        });
        jPanel32.add(钓鱼物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 110, 30));

        新增钓鱼物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        新增钓鱼物品.setText("新增");
        新增钓鱼物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增钓鱼物品ActionPerformed(evt);
            }
        });
        jPanel32.add(新增钓鱼物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, -1, 30));

        钓鱼物品概率.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel32.add(钓鱼物品概率, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 100, 30));

        钓鱼物品名称.setEditable(false);
        钓鱼物品名称.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel32.add(钓鱼物品名称, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 150, 30));

        删除钓鱼物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除钓鱼物品.setText("删除");
        删除钓鱼物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除钓鱼物品ActionPerformed(evt);
            }
        });
        jPanel32.add(删除钓鱼物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, -1, 30));

        钓鱼物品序号.setEditable(false);
        钓鱼物品序号.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        钓鱼物品序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                钓鱼物品序号ActionPerformed(evt);
            }
        });
        jPanel32.add(钓鱼物品序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 80, 30));

        jLabel264.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel264.setText("物品名字；");
        jPanel32.add(jLabel264, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, -1, -1));

        jLabel265.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel265.setText("序列号；");
        jPanel32.add(jLabel265, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel266.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel266.setText("物品代码；");
        jPanel32.add(jLabel266, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        jLabel267.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel267.setText("垂钓概率；");
        jPanel32.add(jLabel267, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, -1, -1));

        钓鱼管理.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 270, 480, 150));

        jTabbedPane2.addTab("渔场钓鱼管理", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 钓鱼管理); // NOI18N

        挖矿管理.setBackground(new java.awt.Color(255, 255, 255));
        挖矿管理.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "挖矿/采药管理", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 51, 51))); // NOI18N
        挖矿管理.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        挖矿反应堆.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        挖矿反应堆.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序列号", "物品代码", "概率", "名称"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        挖矿反应堆.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(挖矿反应堆);

        挖矿管理.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 420, 610));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "挖矿功能", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        新增挖矿物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        新增挖矿物品.setText("新增[挖矿]");
        新增挖矿物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增挖矿物品ActionPerformed(evt);
            }
        });
        jPanel29.add(新增挖矿物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 110, 30));

        增加挖矿物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel29.add(增加挖矿物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 110, 30));

        挖矿物品概率.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel29.add(挖矿物品概率, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 110, 30));

        挖矿序列号.setEditable(false);
        挖矿序列号.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel29.add(挖矿序列号, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 30));

        修改挖矿物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改挖矿物品.setText("修改");
        修改挖矿物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改挖矿物品ActionPerformed(evt);
            }
        });
        jPanel29.add(修改挖矿物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 80, 30));

        物品名称.setEditable(false);
        物品名称.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel29.add(物品名称, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 120, 30));

        删除挖矿物品1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除挖矿物品1.setText("删除");
        删除挖矿物品1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除挖矿物品1ActionPerformed(evt);
            }
        });
        jPanel29.add(删除挖矿物品1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 80, 30));

        jLabel260.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel260.setText("物品名称；");
        jPanel29.add(jLabel260, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, -1, -1));

        jLabel261.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel261.setText("序列号；");
        jPanel29.add(jLabel261, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel262.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel262.setText("物品代码；");
        jPanel29.add(jLabel262, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, -1, -1));

        jLabel263.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel263.setText("掉落概率；");
        jPanel29.add(jLabel263, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        jPanel54.setBackground(new java.awt.Color(255, 255, 255));
        jPanel54.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "挖矿编辑", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("黑体", 1, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel54.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        新增挖矿物品1.setText("新增");
        新增挖矿物品1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增挖矿物品1ActionPerformed(evt);
            }
        });
        jPanel54.add(新增挖矿物品1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 80, 30));
        jPanel54.add(增加挖矿物品1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 110, 30));
        jPanel54.add(挖矿物品概率1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 110, 30));

        挖矿序列号1.setEditable(false);
        jPanel54.add(挖矿序列号1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, 30));

        修改挖矿物品1.setText("修改");
        修改挖矿物品1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改挖矿物品1ActionPerformed(evt);
            }
        });
        jPanel54.add(修改挖矿物品1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, 80, 30));

        物品名称1.setEditable(false);
        jPanel54.add(物品名称1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 120, 30));

        删除挖矿物品2.setText("删除");
        删除挖矿物品2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除挖矿物品2ActionPerformed(evt);
            }
        });
        jPanel54.add(删除挖矿物品2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, 80, 30));

        jLabel326.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
        jLabel326.setForeground(new java.awt.Color(204, 0, 51));
        jLabel326.setText("物品名称；");
        jPanel54.add(jLabel326, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, -1, -1));

        jLabel327.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
        jLabel327.setForeground(new java.awt.Color(204, 0, 51));
        jLabel327.setText("序列号；");
        jPanel54.add(jLabel327, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel328.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
        jLabel328.setForeground(new java.awt.Color(204, 0, 51));
        jLabel328.setText("物品代码；");
        jPanel54.add(jLabel328, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, -1, -1));

        jLabel329.setFont(new java.awt.Font("宋体", 1, 15)); // NOI18N
        jLabel329.setForeground(new java.awt.Color(204, 0, 51));
        jLabel329.setText("掉落概率；");
        jPanel54.add(jLabel329, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        jPanel29.add(jPanel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 80, 500, 180));

        新增挖矿物品2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        新增挖矿物品2.setText("新增[采药]");
        新增挖矿物品2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增挖矿物品2ActionPerformed(evt);
            }
        });
        jPanel29.add(新增挖矿物品2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 110, 30));

        挖矿管理.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 360, 500, 250));

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "挖矿功能", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton11.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton11.setText("重载");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel30.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 130, -1));

        刷新挖矿奖励.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新挖矿奖励.setText("刷新挖矿奖励");
        刷新挖矿奖励.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新挖矿奖励ActionPerformed(evt);
            }
        });
        jPanel30.add(刷新挖矿奖励, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 130, 30));

        挖矿管理.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 180, 210, 140));

        jPanel53.setBackground(new java.awt.Color(255, 255, 255));
        jPanel53.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "挖矿功能", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel53.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton16.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton16.setText("重载");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel53.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 130, -1));

        刷新挖矿奖励1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新挖矿奖励1.setText("刷新采药奖励");
        刷新挖矿奖励1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新挖矿奖励1ActionPerformed(evt);
            }
        });
        jPanel53.add(刷新挖矿奖励1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 130, 30));

        挖矿管理.add(jPanel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 180, 210, 140));

        副业提示语言.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        副业提示语言.setText("[信息]：");
        挖矿管理.add(副业提示语言, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 650, 1240, 25));

        jTabbedPane2.addTab("游戏挖矿采药", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 挖矿管理); // NOI18N

        游戏商店.setBackground(new java.awt.Color(255, 255, 255));
        游戏商店.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "游戏商店管理", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        游戏商店.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        游戏商店2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        游戏商店2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "商店ID", "物品代码", "销售金币", "物品名称"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        游戏商店2.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(游戏商店2);

        游戏商店.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 490, 620));

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));
        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "查询商品出售物品", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        查询商店2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        查询商店2.setText("查询商店");
        查询商店2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查询商店2ActionPerformed(evt);
            }
        });
        jPanel33.add(查询商店2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 60, -1, 30));

        查询商店.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel33.add(查询商店, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 110, 30));

        jLabel270.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel270.setText("商店ID；");
        jPanel33.add(jLabel270, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, -1, -1));

        游戏商店.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 170, 380, 130));

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));
        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "查询商品出售物品", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel34.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        删除商品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除商品.setText("删除");
        删除商品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除商品ActionPerformed(evt);
            }
        });
        jPanel34.add(删除商品, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, -1, 30));

        新增商品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        新增商品.setText("新增");
        新增商品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增商品ActionPerformed(evt);
            }
        });
        jPanel34.add(新增商品, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, -1, 30));

        商品序号.setEditable(false);
        商品序号.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel34.add(商品序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 80, 30));

        商店代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        商店代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                商店代码ActionPerformed(evt);
            }
        });
        jPanel34.add(商店代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 80, 30));

        商品物品代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel34.add(商品物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 90, 30));

        商品售价金币.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel34.add(商品售价金币, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, 100, 30));

        jLabel268.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel268.setText("出售金币；");
        jPanel34.add(jLabel268, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, -1, -1));

        jLabel269.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel269.setText("序号；");
        jPanel34.add(jLabel269, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        jLabel271.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel271.setText("物品名称；");
        jPanel34.add(jLabel271, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, -1, -1));

        jLabel272.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel272.setText("商店ID；");
        jPanel34.add(jLabel272, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, -1));

        修改商品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改商品.setText("修改");
        修改商品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改商品ActionPerformed(evt);
            }
        });
        jPanel34.add(修改商品, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, -1, 30));

        商品名称.setEditable(false);
        商品名称.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        商品名称.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                商品名称ActionPerformed(evt);
            }
        });
        jPanel34.add(商品名称, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, 140, 30));

        jLabel273.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel273.setText("物品代码；");
        jPanel34.add(jLabel273, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        游戏商店.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 420, 580, 220));

        商店提示语言.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        商店提示语言.setText("[信息]：");
        游戏商店.add(商店提示语言, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 653, 1240, 25));

        jTabbedPane2.addTab("游戏商店管理", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 游戏商店, ""); // NOI18N

        反应堆设置.setBackground(new java.awt.Color(255, 255, 255));
        反应堆设置.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "反应堆/箱子", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        反应堆设置.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        反应堆.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        反应堆.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序列号", "反应堆", "物品代码", "概率", "物品名字"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        反应堆.getTableHeader().setReorderingAllowed(false);
        jScrollPane14.setViewportView(反应堆);

        反应堆设置.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 510, 630));
        反应堆设置.add(删除反应堆物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 600, 90, 30));

        删除反应堆物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除反应堆物品.setText("删除");
        删除反应堆物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除反应堆物品ActionPerformed(evt);
            }
        });
        反应堆设置.add(删除反应堆物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 600, 100, 30));

        jLabel114.setFont(new java.awt.Font("宋体", 1, 18)); // NOI18N
        jLabel114.setForeground(new java.awt.Color(255, 0, 102));
        反应堆设置.add(jLabel114, new org.netbeans.lib.awtextra.AbsoluteConstraints(756, 503, -1, -1));

        jLabel281.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel281.setText("提示；修改完成后请右侧应用重载后即可生效。");
        反应堆设置.add(jLabel281, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 370, -1, 20));

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "反应堆编辑", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton20.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton20.setText("刷新");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel35.add(jButton20, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 150, 70, 30));

        反应堆序列号.setEditable(false);
        反应堆序列号.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel35.add(反应堆序列号, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 70, 30));

        反应堆代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel35.add(反应堆代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 90, 30));

        反应堆物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel35.add(反应堆物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 90, 30));

        反应堆概率.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        反应堆概率.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                反应堆概率ActionPerformed(evt);
            }
        });
        jPanel35.add(反应堆概率, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, 90, 30));

        新增反应堆物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        新增反应堆物品.setText("新增");
        新增反应堆物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增反应堆物品ActionPerformed(evt);
            }
        });
        jPanel35.add(新增反应堆物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 70, 30));

        删除反应堆物品1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除反应堆物品1.setText("删除");
        删除反应堆物品1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除反应堆物品1ActionPerformed(evt);
            }
        });
        jPanel35.add(删除反应堆物品1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, 70, 30));

        查找反应堆掉落.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel35.add(查找反应堆掉落, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 90, 30));

        jButton36.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton36.setText("查找");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        jPanel35.add(jButton36, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 150, -1, 30));

        查找物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel35.add(查找物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 90, 30));

        jButton37.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton37.setText("查找");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });
        jPanel35.add(jButton37, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, -1, 30));

        jLabel274.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel274.setText("掉落概率；");
        jPanel35.add(jLabel274, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, -1, 20));

        jLabel275.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel275.setText("序号；");
        jPanel35.add(jLabel275, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, 20));

        jLabel277.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel277.setText("物品代码；");
        jPanel35.add(jLabel277, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, 20));

        jLabel278.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel278.setText("反应堆；");
        jPanel35.add(jLabel278, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, 20));

        jLabel279.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel279.setText("反应堆；");
        jPanel35.add(jLabel279, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, -1, 20));

        jLabel280.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel280.setText("物品代码；");
        jPanel35.add(jLabel280, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, 20));

        修改反应堆物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改反应堆物品.setText("修改");
        修改反应堆物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改反应堆物品ActionPerformed(evt);
            }
        });
        jPanel35.add(修改反应堆物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 70, 70, 30));

        反应堆设置.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 660, 220));

        jLabel282.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel282.setText("删除指定反应堆的所有物品；");
        反应堆设置.add(jLabel282, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 580, -1, 20));

        jTabbedPane2.addTab("反应堆/箱子", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 反应堆设置); // NOI18N

        豆豆机管理.setBackground(new java.awt.Color(255, 255, 255));
        豆豆机管理.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "豆豆机相关设置", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(1210, 550));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "豆豆类型增加开奖率", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(红豆豆加开奖率, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 80, 30));

        jLabel350.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel350.setText("%");
        jPanel3.add(jLabel350, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 20, 30));

        jLabel351.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel351.setText("绿豆豆；");
        jPanel3.add(jLabel351, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, 30));
        jPanel3.add(蓝豆豆加开奖率, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 80, 30));

        jLabel352.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel352.setText("蓝豆豆；");
        jPanel3.add(jLabel352, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, 30));
        jPanel3.add(绿豆豆加开奖率, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, 80, 30));

        jLabel354.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel354.setText("红豆豆；");
        jPanel3.add(jLabel354, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, 30));

        jLabel355.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel355.setText("%");
        jPanel3.add(jLabel355, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 20, 30));

        jLabel356.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel356.setText("%");
        jPanel3.add(jLabel356, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 20, 30));

        豆豆机修改设置1.setText("修改");
        豆豆机修改设置1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                豆豆机修改设置1ActionPerformed(evt);
            }
        });
        jPanel3.add(豆豆机修改设置1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 260, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, 260, 280));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "豆豆机奖励设置", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel4.add(豆豆奖励, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 100, 30));

        豆豆机修改设置2.setText("修改");
        豆豆机修改设置2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                豆豆机修改设置2ActionPerformed(evt);
            }
        });
        jPanel4.add(豆豆机修改设置2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 260, -1));

        jLabel357.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel357.setText("豆豆奖励；");
        jPanel4.add(jLabel357, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, -1, 30));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 260, 280));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "豆豆机设置", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel346.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel346.setText("打豆豆力度；");
        jPanel5.add(jLabel346, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, 30));
        jPanel5.add(打豆豆力度, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 100, 30));
        jPanel5.add(豆豆进洞率, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 100, 30));

        jLabel347.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel347.setText("豆豆进洞率；");
        jPanel5.add(jLabel347, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, 30));

        jLabel348.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel348.setText("进洞开奖率；");
        jPanel5.add(jLabel348, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 30));
        jPanel5.add(进洞开奖率, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 100, 30));

        jLabel349.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel349.setText("豆豆稀有率；");
        jPanel5.add(jLabel349, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, -1, 30));
        jPanel5.add(豆豆稀有率, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 100, 30));

        豆豆机修改设置.setText("修改");
        豆豆机修改设置.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                豆豆机修改设置ActionPerformed(evt);
            }
        });
        jPanel5.add(豆豆机修改设置, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 260, -1));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 260, 280));

        jLabel353.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel353.setText("豆豆机奖励在脚本内部设置 9100205_1");
        jPanel2.add(jLabel353, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 450, 30));

        jTabbedPane1.addTab("相关中奖设置", jPanel2);

        豆豆机管理.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1270, 690));

        jTabbedPane2.addTab("豆豆机管理", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 豆豆机管理); // NOI18N

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "药水冷却", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        药水冷却.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "序号", "药水", "冷却"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(药水冷却);
        if (药水冷却.getColumnModel().getColumnCount() > 0) {
            药水冷却.getColumnModel().getColumn(0).setResizable(false);
            药水冷却.getColumnModel().getColumn(0).setPreferredWidth(20);
            药水冷却.getColumnModel().getColumn(1).setResizable(false);
            药水冷却.getColumnModel().getColumn(1).setPreferredWidth(200);
            药水冷却.getColumnModel().getColumn(2).setResizable(false);
            药水冷却.getColumnModel().getColumn(2).setPreferredWidth(200);
        }

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 500, 620));

        jButton5.setText("刷新");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 270, 100, 30));
        jPanel6.add(药水冷却时间, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 170, 110, -1));

        药水序号.setEditable(false);
        jPanel6.add(药水序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 170, 70, -1));

        药水名字.setEditable(false);
        jPanel6.add(药水名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 170, 150, -1));

        jLabel285.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel285.setText("冷却；");
        jPanel6.add(jLabel285, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 150, -1, 20));

        jLabel286.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel286.setText("序号；");
        jPanel6.add(jLabel286, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 150, -1, 20));

        jLabel287.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel287.setText("药水；");
        jPanel6.add(jLabel287, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 150, -1, 20));

        jButton6.setText("修改");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 270, 100, 30));

        jTabbedPane2.addTab("药水冷却", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), jPanel6); // NOI18N

        getContentPane().add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1270, 720));

        ZEVMS2提示框.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        ZEVMS2提示框.setText("[信息]：");
        getContentPane().add(ZEVMS2提示框, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 725, 1260, 30));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void 查询物品掉落ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查询物品掉落ActionPerformed
        boolean result = this.查询物品掉落代码.getText().matches("[0-9]+");
        if (result) {
            if (Integer.parseInt(this.查询物品掉落代码.getText()) < 0) {
                ZEVMS2提示框.setText("[信息]:请填写正确的值。");
                return;
            }
            for (int i = ((DefaultTableModel) (this.怪物爆物.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.怪物爆物.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;

                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM drop_data WHERE itemid =  " + Integer.parseInt(this.查询物品掉落代码.getText()) + "");
                rs = ps.executeQuery();
                while (rs.next()) {

                    ((DefaultTableModel) 怪物爆物.getModel()).insertRow(怪物爆物.getRowCount(), new Object[]{
                        rs.getInt("id"),
                        rs.getInt("dropperid"),
                        rs.getInt("itemid"),
                        rs.getInt("chance"),
                        MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
            怪物爆物.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int i = 怪物爆物.getSelectedRow();
                    String a = 怪物爆物.getValueAt(i, 0).toString();
                    String a1 = 怪物爆物.getValueAt(i, 1).toString();
                    String a2 = 怪物爆物.getValueAt(i, 2).toString();
                    String a3 = 怪物爆物.getValueAt(i, 3).toString();
                    //String a4 = 怪物爆物.getValueAt(i, 4).toString();
                    怪物爆物序列号.setText(a);
                    怪物爆物怪物代码.setText(a1);
                    怪物爆物物品代码.setText(a2);
                    怪物爆物爆率.setText(a3);
                }
            });
        } else {
            ZEVMS2提示框.setText("[信息]:请输入你要查找的物品代码。");
        }
    }//GEN-LAST:event_查询物品掉落ActionPerformed

    private void 查询物品掉落代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查询物品掉落代码ActionPerformed

    }//GEN-LAST:event_查询物品掉落代码ActionPerformed

    private void 查询怪物掉落ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查询怪物掉落ActionPerformed
        boolean result = this.查询怪物掉落代码.getText().matches("[0-9]+");
        if (result) {
            if (Integer.parseInt(this.查询怪物掉落代码.getText()) < 0) {
                ZEVMS2提示框.setText("[信息]:请填写正确的值。");
                return;
            }
            for (int i = ((DefaultTableModel) (this.怪物爆物.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.怪物爆物.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;

                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM drop_data WHERE dropperid =  " + Integer.parseInt(this.查询怪物掉落代码.getText()) + " && itemid !=0");//&& itemid !=0
                rs = ps.executeQuery();
                while (rs.next()) {

                    ((DefaultTableModel) 怪物爆物.getModel()).insertRow(怪物爆物.getRowCount(), new Object[]{
                        rs.getInt("id"),
                        rs.getInt("dropperid"),
                        rs.getInt("itemid"),
                        rs.getInt("chance"),
                        MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
            怪物爆物.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int i = 怪物爆物.getSelectedRow();
                    String a = 怪物爆物.getValueAt(i, 0).toString();
                    String a1 = 怪物爆物.getValueAt(i, 1).toString();
                    String a2 = 怪物爆物.getValueAt(i, 2).toString();
                    String a3 = 怪物爆物.getValueAt(i, 3).toString();
                    //String a4 = 怪物爆物.getValueAt(i, 4).toString();
                    怪物爆物序列号.setText(a);
                    怪物爆物怪物代码.setText(a1);
                    怪物爆物物品代码.setText(a2);
                    怪物爆物爆率.setText(a3);
                    //怪物爆物物品名称.setText(a4);

                }
            });
        } else {
            ZEVMS2提示框.setText("[信息]:请输入你要查找的怪物代码。");
        }
    }//GEN-LAST:event_查询怪物掉落ActionPerformed

    private void 删除指定的掉落按键ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除指定的掉落按键ActionPerformed
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs = null;

        boolean result = this.删除指定的掉落.getText().matches("[0-9]+");
        if (result == true) {
            int 商城SN编码 = Integer.parseInt(this.删除指定的掉落.getText());
            try {
                // for (int i = ((DefaultTableModel) (this.怪物爆物.getModel())).getRowCount() - 1; i >= 0; i--) {
                //   ((DefaultTableModel) (this.怪物爆物.getModel())).removeRow(i);
                //}
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM drop_data WHERE itemid = ?");
                ps1.setInt(1, 商城SN编码);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from drop_data where itemid =" + 商城SN编码 + "";
                    ps1.executeUpdate(sqlstr);
                    ZEVMS2提示框.setText("[信息]:成功删除 " + 商城SN编码 + " 物品。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
            刷新怪物爆物();
        } else {
            ZEVMS2提示框.setText("[信息]:请输入你要查找的物品代码。");
        }

    }//GEN-LAST:event_删除指定的掉落按键ActionPerformed

    private void 删除怪物爆物ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除怪物爆物ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;

        boolean result = this.怪物爆物序列号.getText().matches("[0-9]+");
        if (result == true) {
            int 商城SN编码 = Integer.parseInt(this.怪物爆物序列号.getText());

            try {
                //清楚table数据
                for (int i = ((DefaultTableModel) (this.怪物爆物.getModel())).getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) (this.怪物爆物.getModel())).removeRow(i);
                }
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM drop_data WHERE id = ?");
                ps1.setInt(1, 商城SN编码);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from drop_data where id =" + 商城SN编码 + "";
                    ps1.executeUpdate(sqlstr);
                    ZEVMS2提示框.setText("[信息]:删除爆物成功。");
                    刷新指定怪物爆物();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_删除怪物爆物ActionPerformed

    private void 添加怪物爆物ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_添加怪物爆物ActionPerformed
        boolean result1 = this.怪物爆物怪物代码.getText().matches("[0-9]+");
        boolean result2 = this.怪物爆物物品代码.getText().matches("[0-9]+");
        boolean result3 = this.怪物爆物爆率.getText().matches("[0-9]+");
        if (result1 && result2 && result3) {
            if (Integer.parseInt(this.怪物爆物怪物代码.getText()) < 0 && Integer.parseInt(this.怪物爆物物品代码.getText()) < 0 && Integer.parseInt(this.怪物爆物爆率.getText()) < 0) {
                ZEVMS2提示框.setText("[信息]:请填写正确的值。");
                return;
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO drop_data ( dropperid,itemid,minimum_quantity,maximum_quantity,chance) VALUES ( ?, ?, ?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(this.怪物爆物怪物代码.getText()));
                ps.setInt(2, Integer.parseInt(this.怪物爆物物品代码.getText()));
                ps.setInt(3, 1);
                ps.setInt(4, 1);
                ps.setInt(5, Integer.parseInt(this.怪物爆物爆率.getText()));
                ps.executeUpdate();
                ZEVMS2提示框.setText("[信息]:添加成功。");
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请输入<怪物代码><物品代码><物品爆率>的格式来添加。");
        }
    }//GEN-LAST:event_添加怪物爆物ActionPerformed

    private void 修改怪物爆物ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改怪物爆物ActionPerformed
        boolean result1 = this.怪物爆物怪物代码.getText().matches("[0-9]+");
        boolean result2 = this.怪物爆物物品代码.getText().matches("[0-9]+");
        boolean result3 = this.怪物爆物爆率.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result1 && result2 && result3) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE drop_data SET dropperid = ?, itemid = ?, chance = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM drop_data WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.怪物爆物序列号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    String sqlString3 = null;
                    String sqlString4 = null;
                    sqlString2 = "update drop_data set dropperid='" + this.怪物爆物怪物代码.getText() + "' where id=" + this.怪物爆物序列号.getText() + ";";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    sqlString3 = "update drop_data set itemid='" + this.怪物爆物物品代码.getText() + "' where id=" + this.怪物爆物序列号.getText() + ";";
                    PreparedStatement itemid = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    itemid.executeUpdate(sqlString3);
                    sqlString4 = "update drop_data set chance='" + this.怪物爆物爆率.getText() + "' where id=" + this.怪物爆物序列号.getText() + ";";
                    PreparedStatement chance = DatabaseConnection.getConnection().prepareStatement(sqlString4);
                    chance.executeUpdate(sqlString4);
                    ZEVMS2提示框.setText("[信息]:修改成功。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请选择你要修改的数据。");
        }
    }//GEN-LAST:event_修改怪物爆物ActionPerformed

    private void 刷新怪物爆物ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新怪物爆物ActionPerformed
        ZEVMS2提示框.setText("[信息]:刷新怪物物品掉落数据。");
        刷新怪物爆物();
    }//GEN-LAST:event_刷新怪物爆物ActionPerformed

    private void 世界爆物序列号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_世界爆物序列号ActionPerformed

    }//GEN-LAST:event_世界爆物序列号ActionPerformed

    private void 世界爆物物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_世界爆物物品代码ActionPerformed

    }//GEN-LAST:event_世界爆物物品代码ActionPerformed

    private void 世界爆物爆率ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_世界爆物爆率ActionPerformed

    }//GEN-LAST:event_世界爆物爆率ActionPerformed

    private void 添加世界爆物ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_添加世界爆物ActionPerformed
        boolean result1 = this.世界爆物物品代码.getText().matches("[0-9]+");
        boolean result2 = this.世界爆物爆率.getText().matches("[0-9]+");
        if (result1 && result2) {
            if (Integer.parseInt(this.世界爆物物品代码.getText()) < 0 && Integer.parseInt(this.世界爆物爆率.getText()) < 0) {
                ZEVMS2提示框.setText("[信息]:请填写正确的值。");
                return;
            }
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO drop_data_global (continent,dropType,itemid,minimum_quantity,maximum_quantity,questid,chance) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, 1);
                ps.setInt(2, 1);
                ps.setInt(3, Integer.parseInt(this.世界爆物物品代码.getText()));
                ps.setInt(4, 1);
                ps.setInt(5, 1);
                ps.setInt(6, 0);
                ps.setInt(7, Integer.parseInt(this.世界爆物爆率.getText()));
                ps.executeUpdate();
                ZEVMS2提示框.setText("[信息]:世界爆物添加成功。");
                刷新世界爆物();
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请输入<物品代码>，<物品爆率> 。");
        }


    }//GEN-LAST:event_添加世界爆物ActionPerformed

    private void 删除世界爆物ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除世界爆物ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.世界爆物序列号.getText().matches("[0-9]+");
        if (result == true) {
            int 商城SN编码 = Integer.parseInt(this.世界爆物序列号.getText());
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM drop_data_global WHERE id = ?");
                ps1.setInt(1, 商城SN编码);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from drop_data_global where id =" + 商城SN编码 + "";
                    ps1.executeUpdate(sqlstr);
                    ZEVMS2提示框.setText("[信息]:删除成功。");
                    刷新世界爆物();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请选择你要删除的物品。");
        }
    }//GEN-LAST:event_删除世界爆物ActionPerformed

    private void 世界爆物名称ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_世界爆物名称ActionPerformed

    }//GEN-LAST:event_世界爆物名称ActionPerformed

    private void 修改世界爆物ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改世界爆物ActionPerformed

        boolean result2 = this.世界爆物物品代码.getText().matches("[0-9]+");
        boolean result3 = this.世界爆物爆率.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2 && result3) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE drop_data_global SET dropperid = ?, itemid = ?, chance = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM drop_data_global WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.世界爆物序列号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    String sqlString3 = null;
                    sqlString2 = "update drop_data_global set itemid='" + this.世界爆物物品代码.getText() + "' where id=" + this.世界爆物序列号.getText() + ";";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    sqlString3 = "update drop_data_global set chance='" + this.世界爆物爆率.getText() + "' where id=" + this.世界爆物序列号.getText() + ";";
                    PreparedStatement itemid = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    itemid.executeUpdate(sqlString3);
                    ZEVMS2提示框.setText("[信息]:修改成功。");
                    刷新世界爆物();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请选择你要修改的数据。");
        }
    }//GEN-LAST:event_修改世界爆物ActionPerformed

    private void 刷新世界爆物ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新世界爆物ActionPerformed
        ZEVMS2提示框.setText("[信息]:刷新世界物品掉落数据。");
        刷新世界爆物();
    }//GEN-LAST:event_刷新世界爆物ActionPerformed

    private void 修改物品掉落持续时间ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改物品掉落持续时间ActionPerformed
        boolean result2 = this.物品掉落持续时间.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 998);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.物品掉落持续时间.getText() + "' where id = 998;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新物品掉落持续时间();
                    ZEVMS2提示框.setText("[信息]:修改成功。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请输入你要修改的数据。");
        }
    }//GEN-LAST:event_修改物品掉落持续时间ActionPerformed

    private void 查找道具ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查找道具ActionPerformed
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");
        if (result1) {
            for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;

                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "");
                rs = ps.executeQuery();
                while (rs.next()) {
                    ((DefaultTableModel) 游戏道具.getModel()).insertRow(游戏道具.getRowCount(), new Object[]{
                        rs.getInt("characterid"),
                        NPCConversationManager.角色ID取名字(rs.getInt("characterid")),
                        rs.getInt("itemid"),
                        MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你要查找的<物品代码>");
        }
    }//GEN-LAST:event_查找道具ActionPerformed

    private void 删除道具ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除道具ActionPerformed
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");

        if (result1) {
            try {
                for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
                }
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM inventoryitems WHERE itemid = ?");
                ps1.setInt(1, Integer.parseInt(this.游戏道具代码.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from inventoryitems where itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(null, "成功删除所有代码为 " + Integer.parseInt(this.游戏道具代码.getText()) + " 物品。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你想要删除的<物品代码>");
        }
    }//GEN-LAST:event_删除道具ActionPerformed

    private void 查找道具1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查找道具1ActionPerformed
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");
        if (result1) {
            for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;

                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM auctionitems WHERE itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "");
                rs = ps.executeQuery();
                while (rs.next()) {
                    ((DefaultTableModel) 游戏道具.getModel()).insertRow(游戏道具.getRowCount(), new Object[]{
                        rs.getInt("characterid"),
                        NPCConversationManager.角色ID取名字(rs.getInt("characterid")),
                        rs.getInt("itemid"),
                        MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你要查找的<物品代码>");
        }
    }//GEN-LAST:event_查找道具1ActionPerformed

    private void 删除道具1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除道具1ActionPerformed
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");
        if (result1) {
            try {
                for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
                }
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM auctionitems WHERE itemid = ?");
                ps1.setInt(1, Integer.parseInt(this.游戏道具代码.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from auctionitems where itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(null, "成功删除所有代码为 " + Integer.parseInt(this.游戏道具代码.getText()) + " 物品。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你想要删除的<物品代码>");
        }
    }//GEN-LAST:event_删除道具1ActionPerformed

    private void 查找道具2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查找道具2ActionPerformed
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");
        if (result1) {
            for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM auctionitems1 WHERE itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "");
                rs = ps.executeQuery();
                while (rs.next()) {
                    ((DefaultTableModel) 游戏道具.getModel()).insertRow(游戏道具.getRowCount(), new Object[]{
                        rs.getInt("characterid"),
                        NPCConversationManager.角色ID取名字(rs.getInt("characterid")),
                        rs.getInt("itemid"),
                        MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你要查找的<物品代码>");
        }
    }//GEN-LAST:event_查找道具2ActionPerformed

    private void 删除道具2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除道具2ActionPerformed
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");

        if (result1) {
            try {
                for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
                }
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM auctionitems1 WHERE itemid = ?");
                ps1.setInt(1, Integer.parseInt(this.游戏道具代码.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from auctionitems1 where itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(null, "成功删除所有代码为 " + Integer.parseInt(this.游戏道具代码.getText()) + " 物品。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你想要删除的<物品代码>");
        }
    }//GEN-LAST:event_删除道具2ActionPerformed

    private void 查找道具3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查找道具3ActionPerformed
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");
        if (result1) {
            for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM bank_item1 WHERE itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "");
                rs = ps.executeQuery();
                while (rs.next()) {
                    ((DefaultTableModel) 游戏道具.getModel()).insertRow(游戏道具.getRowCount(), new Object[]{
                        rs.getInt("cid"),
                        NPCConversationManager.角色ID取名字(rs.getInt("cid")),
                        rs.getInt("itemid"),
                        MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你要查找的<物品代码>");
        }
    }//GEN-LAST:event_查找道具3ActionPerformed

    private void 删除道具3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除道具3ActionPerformed
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");
        if (result1) {
            try {
                for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
                }
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM bank_item1 WHERE itemid = ?");
                ps1.setInt(1, Integer.parseInt(this.游戏道具代码.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from bank_item1 where itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(null, "成功删除所有代码为 " + Integer.parseInt(this.游戏道具代码.getText()) + " 物品。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你想要删除的<物品代码>");
        }
    }//GEN-LAST:event_删除道具3ActionPerformed

    private void 查找道具4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查找道具4ActionPerformed
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");
        if (result1) {
            for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;

                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM bank_item WHERE itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "");
                rs = ps.executeQuery();
                while (rs.next()) {
                    ((DefaultTableModel) 游戏道具.getModel()).insertRow(游戏道具.getRowCount(), new Object[]{
                        rs.getInt("cid"),
                        NPCConversationManager.角色ID取名字(rs.getInt("cid")),
                        rs.getInt("itemid"),
                        MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你要查找的<物品代码>");
        }
    }//GEN-LAST:event_查找道具4ActionPerformed

    private void 删除道具4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除道具4ActionPerformed
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.游戏道具代码.getText().matches("[0-9]+");

        if (result1) {
            try {
                for (int i = ((DefaultTableModel) (this.游戏道具.getModel())).getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) (this.游戏道具.getModel())).removeRow(i);
                }
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM bank_item WHERE itemid = ?");
                ps1.setInt(1, Integer.parseInt(this.游戏道具代码.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from bank_item where itemid =" + Integer.parseInt(this.游戏道具代码.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(null, "成功删除所有代码为 " + Integer.parseInt(this.游戏道具代码.getText()) + " 物品。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你想要删除的<物品代码>");
        }
    }//GEN-LAST:event_删除道具4ActionPerformed

    private void 修改钓鱼物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改钓鱼物品ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.钓鱼物品序号.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE 钓鱼物品 SET itemid = ?,chance = ?WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM 钓鱼物品 WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.钓鱼物品序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    String sqlString2 = null;
                    sqlString1 = "update 钓鱼物品 set itemid='" + this.钓鱼物品代码.getText() + "' where id=" + this.钓鱼物品序号.getText() + ";";
                    PreparedStatement name = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    name.executeUpdate(sqlString1);
                    sqlString2 = "update 钓鱼物品 set chance='" + this.钓鱼物品概率.getText() + "' where id=" + this.钓鱼物品序号.getText() + ";";
                    PreparedStatement level = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    level.executeUpdate(sqlString2);
                    ZEVMS2提示框.setText("[信息]:修改钓鱼物品成功。");
                    刷新钓鱼();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:输入<物品代码><概率>。");
        }
    }//GEN-LAST:event_修改钓鱼物品ActionPerformed

    private void 刷新钓鱼物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新钓鱼物品ActionPerformed
        ZEVMS2提示框.setText("[信息]:刷新钓鱼奖励成功。");
        刷新钓鱼();
    }//GEN-LAST:event_刷新钓鱼物品ActionPerformed

    private void 钓鱼物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_钓鱼物品代码ActionPerformed

    }//GEN-LAST:event_钓鱼物品代码ActionPerformed

    private void 新增钓鱼物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增钓鱼物品ActionPerformed
        boolean result1 = this.钓鱼物品代码.getText().matches("[0-9]+");
        boolean result2 = this.钓鱼物品概率.getText().matches("[0-9]+");

        if (result1 && result2) {
            if (Integer.parseInt(this.钓鱼物品代码.getText()) < 0 && Integer.parseInt(this.钓鱼物品概率.getText()) < 0) {
                ZEVMS2提示框.setText("[信息]:请填写正确的值。");
                return;
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO 钓鱼物品 (itemid, chance ,expiration) VALUES (?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(this.钓鱼物品代码.getText()));
                ps.setInt(2, Integer.parseInt(this.钓鱼物品概率.getText()));
                ps.setInt(3, 1);
                ps.executeUpdate();
                ZEVMS2提示框.setText("[信息]:新增钓鱼奖励成功。");
                刷新钓鱼();
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请输入<物品代码><概率>。");
        }
    }//GEN-LAST:event_新增钓鱼物品ActionPerformed
    private void 刷新钓鱼() {
        for (int i = ((DefaultTableModel) (this.钓鱼物品.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.钓鱼物品.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM 钓鱼物品");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 钓鱼物品.getModel()).insertRow(钓鱼物品.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    rs.getInt("itemid"),
                    rs.getInt("chance"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        钓鱼物品.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 钓鱼物品.getSelectedRow();
                String a = 钓鱼物品.getValueAt(i, 0).toString();
                String a1 = 钓鱼物品.getValueAt(i, 1).toString();
                String a2 = 钓鱼物品.getValueAt(i, 2).toString();
                String a3 = 钓鱼物品.getValueAt(i, 3).toString();
                钓鱼物品序号.setText(a);
                钓鱼物品代码.setText(a1);
                钓鱼物品概率.setText(a2);
                钓鱼物品名称.setText(a3);
            }
        });
    }
    private void 删除钓鱼物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除钓鱼物品ActionPerformed
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.钓鱼物品序号.getText().matches("[0-9]+");
        if (result1) {
            try {
                //清楚table数据
                for (int i = ((DefaultTableModel) (this.钓鱼物品.getModel())).getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) (this.钓鱼物品.getModel())).removeRow(i);
                }
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM 钓鱼物品 WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.钓鱼物品序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from 钓鱼物品 where id =" + Integer.parseInt(this.钓鱼物品序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    ZEVMS2提示框.setText("[信息]:删除钓鱼奖励物品成功。");
                    刷新钓鱼();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请选择你要删除的钓鱼物品。");
        }
    }//GEN-LAST:event_删除钓鱼物品ActionPerformed

    private void 钓鱼物品序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_钓鱼物品序号ActionPerformed

    }//GEN-LAST:event_钓鱼物品序号ActionPerformed

    private void 新增挖矿物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增挖矿物品ActionPerformed
        boolean result1 = this.增加挖矿物品.getText().matches("[0-9]+");
        boolean result2 = this.挖矿物品概率.getText().matches("[0-9]+");
        if (result1 && result2) {
            if (Integer.parseInt(this.增加挖矿物品.getText()) < 0 && Integer.parseInt(this.挖矿物品概率.getText()) < 0) {
                ZEVMS2提示框.setText("[信息]:请填写正确的值。");
                return;
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO reactordrops (reactorid ,itemid ,chance ,questid ) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, 2112012);
                ps.setInt(2, Integer.parseInt(this.增加挖矿物品.getText()));
                ps.setInt(3, Integer.parseInt(this.挖矿物品概率.getText()));
                ps.setInt(4, -1);
                ps.executeUpdate();
                ZEVMS2提示框.setText("[信息]:挖矿奖励新增成功。");
                刷新挖矿();
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请输入物品代码，掉落率。");
        }
    }//GEN-LAST:event_新增挖矿物品ActionPerformed
    private void 刷新挖矿() {
        for (int i = ((DefaultTableModel) (this.挖矿反应堆.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.挖矿反应堆.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM reactordrops WHERE reactorid = 2112012");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 挖矿反应堆.getModel()).insertRow(挖矿反应堆.getRowCount(), new Object[]{rs.getInt("reactordropid"), rs.getInt("itemid"), rs.getInt("chance"), MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))});
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        挖矿反应堆.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 挖矿反应堆.getSelectedRow();
                String a = 挖矿反应堆.getValueAt(i, 0).toString();
                String a1 = 挖矿反应堆.getValueAt(i, 1).toString();
                String a2 = 挖矿反应堆.getValueAt(i, 2).toString();
                String a3 = 挖矿反应堆.getValueAt(i, 3).toString();
                挖矿序列号.setText(a);
                增加挖矿物品.setText(a1);
                挖矿物品概率.setText(a2);
                物品名称.setText(a3);
            }
        });

    }

    public void 查询商店() {

        boolean result = this.查询商店.getText().matches("[0-9]+");
        if (result) {
            if (Integer.parseInt(this.查询商店.getText()) < 0) {
                ZEVMS2提示框.setText("[信息]:请填写正确的值。");
                return;
            }
            for (int i = ((DefaultTableModel) (this.游戏商店2.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.游戏商店2.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM shopitems WHERE shopid = " + Integer.parseInt(this.查询商店.getText()) + " ");
                rs = ps.executeQuery();
                while (rs.next()) {
                    ((DefaultTableModel) 游戏商店2.getModel()).insertRow(游戏商店2.getRowCount(), new Object[]{
                        rs.getInt("shopitemid"),
                        rs.getInt("shopid"),
                        rs.getInt("itemid"),
                        rs.getInt("price"),
                        MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                    });
                }
                ZEVMS2提示框.setText("[信息]:商城物品查询成功。");
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
            游戏商店2.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int i = 游戏商店2.getSelectedRow();
                    String a = 游戏商店2.getValueAt(i, 0).toString();
                    String a1 = 游戏商店2.getValueAt(i, 1).toString();
                    String a2 = 游戏商店2.getValueAt(i, 2).toString();
                    String a3 = 游戏商店2.getValueAt(i, 3).toString();
                    //String a4 = 游戏商店2.getValueAt(i, 4).toString();
                    商品序号.setText(a);
                    商店代码.setText(a1);
                    商品物品代码.setText(a2);
                    商品售价金币.setText(a3);
                    // 商品名称.setText(a4);
                }
            });
        } else {
            ZEVMS2提示框.setText("[信息]:请输入你需要查询的商店ID。");
        }
    }

    private void 刷新采药() {
        for (int i = ((DefaultTableModel) (this.挖矿反应堆.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.挖矿反应堆.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM reactordrops WHERE reactorid = 1072000");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 挖矿反应堆.getModel()).insertRow(挖矿反应堆.getRowCount(), new Object[]{rs.getInt("reactordropid"), rs.getInt("itemid"), rs.getInt("chance"), MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))});
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        挖矿反应堆.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 挖矿反应堆.getSelectedRow();
                String a = 挖矿反应堆.getValueAt(i, 0).toString();
                String a1 = 挖矿反应堆.getValueAt(i, 1).toString();
                String a2 = 挖矿反应堆.getValueAt(i, 2).toString();
                String a3 = 挖矿反应堆.getValueAt(i, 3).toString();
                挖矿序列号.setText(a);
                增加挖矿物品.setText(a1);
                挖矿物品概率.setText(a2);
                物品名称.setText(a3);
            }
        });

    }
    private void 修改挖矿物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改挖矿物品ActionPerformed
        boolean result1 = this.增加挖矿物品.getText().matches("[0-9]+");
        boolean result2 = this.挖矿物品概率.getText().matches("[0-9]+");
        if (result1 && result2) {
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE reactordrops SET itemid = ?,chance = ?WHERE reactordropid = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM reactordrops WHERE reactordropid = ?");
                ps1.setInt(1, Integer.parseInt(this.挖矿序列号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    String sqlString2 = null;
                    sqlString1 = "update reactordrops set itemid='" + this.增加挖矿物品.getText() + "' where reactordropid=" + this.挖矿序列号.getText() + ";";
                    PreparedStatement name = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    name.executeUpdate(sqlString1);
                    sqlString2 = "update reactordrops set chance='" + this.挖矿物品概率.getText() + "' where reactordropid=" + this.挖矿序列号.getText() + ";";
                    PreparedStatement level = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    level.executeUpdate(sqlString2);
                    ZEVMS2提示框.setText("[信息]:修改成功。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请选择你要修改的物品。");
        }
    }//GEN-LAST:event_修改挖矿物品ActionPerformed

    private void 删除挖矿物品1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除挖矿物品1ActionPerformed

        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.挖矿序列号.getText().matches("[0-9]+");
        if (result1) {
            try {
                for (int i = ((DefaultTableModel) (this.挖矿反应堆.getModel())).getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) (this.挖矿反应堆.getModel())).removeRow(i);
                }
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM reactordrops WHERE reactordropid = ?");
                ps1.setInt(1, Integer.parseInt(this.挖矿序列号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from reactordrops where reactordropid =" + Integer.parseInt(this.挖矿序列号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    ZEVMS2提示框.setText("[信息]:删除成功。");
                    for (int i = ((DefaultTableModel) (this.挖矿反应堆.getModel())).getRowCount() - 1; i >= 0; i--) {
                        ((DefaultTableModel) (this.挖矿反应堆.getModel())).removeRow(i);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请选择你要删除的物品。");
        }
    }//GEN-LAST:event_删除挖矿物品1ActionPerformed

    private void 新增挖矿物品1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增挖矿物品1ActionPerformed

    }//GEN-LAST:event_新增挖矿物品1ActionPerformed

    private void 修改挖矿物品1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改挖矿物品1ActionPerformed

    }//GEN-LAST:event_修改挖矿物品1ActionPerformed

    private void 删除挖矿物品2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除挖矿物品2ActionPerformed

    }//GEN-LAST:event_删除挖矿物品2ActionPerformed

    private void 新增挖矿物品2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增挖矿物品2ActionPerformed
        boolean result1 = this.增加挖矿物品.getText().matches("[0-9]+");
        boolean result2 = this.挖矿物品概率.getText().matches("[0-9]+");
        if (result1 && result2) {
            if (Integer.parseInt(this.增加挖矿物品.getText()) < 0 && Integer.parseInt(this.挖矿物品概率.getText()) < 0) {
                ZEVMS2提示框.setText("[信息]:请填写正确的值。");
                return;
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO reactordrops (reactorid ,itemid ,chance ,questid ) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, 1072000);
                ps.setInt(2, Integer.parseInt(this.增加挖矿物品.getText()));
                ps.setInt(3, Integer.parseInt(this.挖矿物品概率.getText()));
                ps.setInt(4, -1);
                ps.executeUpdate();
                ZEVMS2提示框.setText("[信息]:挖矿奖励新增成功。");
                刷新采药();
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请输入物品代码，掉落率。");
        }
    }//GEN-LAST:event_新增挖矿物品2ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        ZEVMS2提示框.setText("[信息]:挖矿重载开始。");
        ReactorScriptManager.getInstance().clearDrops();
        ZEVMS2提示框.setText("[信息]:挖矿重载成功。");
    }//GEN-LAST:event_jButton11ActionPerformed

    private void 刷新挖矿奖励ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新挖矿奖励ActionPerformed
        ZEVMS2提示框.setText("[信息]:刷新挖矿奖励。");
        刷新挖矿();
    }//GEN-LAST:event_刷新挖矿奖励ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        ZEVMS2提示框.setText("[信息]:采药重载开始。");
        ReactorScriptManager.getInstance().clearDrops();
        ZEVMS2提示框.setText("[信息]:采药重载成功。");
    }//GEN-LAST:event_jButton16ActionPerformed

    private void 刷新挖矿奖励1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新挖矿奖励1ActionPerformed
        ZEVMS2提示框.setText("[信息]:刷新采药奖励。");
        刷新采药();
    }//GEN-LAST:event_刷新挖矿奖励1ActionPerformed

    private void 查询商店2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查询商店2ActionPerformed
        查询商店();
    }//GEN-LAST:event_查询商店2ActionPerformed

    private void 删除商品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除商品ActionPerformed

        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.商品序号.getText().matches("[0-9]+");
        if (result == true) {
            int 商城SN编码 = Integer.parseInt(this.商品序号.getText());
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM shopitems WHERE shopitemid = ?");
                ps1.setInt(1, 商城SN编码);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from shopitems where shopitemid =" + 商城SN编码 + "";
                    ps1.executeUpdate(sqlstr);
                    查询商店();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
            ZEVMS2提示框.setText("[信息]:删除商店商品成功。");
        } else {
            ZEVMS2提示框.setText("[信息]:请选择你要删除的商品。");
        }
    }//GEN-LAST:event_删除商品ActionPerformed

    private void 新增商品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增商品ActionPerformed

        boolean result = this.商品物品代码.getText().matches("[0-9]+");
        boolean result1 = this.商店代码.getText().matches("[0-9]+");
        boolean result2 = this.商品售价金币.getText().matches("[0-9]+");

        if (result && result1 && result2) {
            if (Integer.parseInt(this.商店代码.getText()) < 0 && Integer.parseInt(this.商品物品代码.getText()) < 0 && Integer.parseInt(this.商品售价金币.getText()) < 0) {
                ZEVMS2提示框.setText("[信息]:请填写正确的值。");
                return;
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO shopitems (shopid ,itemid ,price ,pitch ,position ,reqitem ,reqitemq) VALUES ( ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(this.商店代码.getText()));
                ps.setInt(2, Integer.parseInt(this.商品物品代码.getText()));
                ps.setInt(3, Integer.parseInt(this.商品售价金币.getText()));
                ps.setInt(4, 0);
                ps.setInt(5, 0);
                ps.setInt(6, 0);
                ps.setInt(7, 0);
                ps.executeUpdate();
                查询商店();
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
            ZEVMS2提示框.setText("[信息]:新增商店商品成功。");
        } else {
            ZEVMS2提示框.setText("[信息]:输入<商店ID><物品代码><售价>。");
        }
    }//GEN-LAST:event_新增商品ActionPerformed

    private void 商店代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_商店代码ActionPerformed

    }//GEN-LAST:event_商店代码ActionPerformed

    private void 修改商品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改商品ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.商品物品代码.getText().matches("[0-9]+");
        boolean result1 = this.商店代码.getText().matches("[0-9]+");
        boolean result2 = this.商品售价金币.getText().matches("[0-9]+");

        if (result && result1 && result2) {
            if (Integer.parseInt(this.商店代码.getText()) < 0 && Integer.parseInt(this.商品物品代码.getText()) < 0 && Integer.parseInt(this.商品售价金币.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "请填写正确的值");
            }
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE shopitems SET itemid = ?,price = ?,shopid = ?WHERE shopitemid = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM shopitems WHERE shopitemid = ?");
                ps1.setInt(1, Integer.parseInt(this.商品序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    String sqlString2 = null;
                    String sqlString3 = null;
                    sqlString1 = "update shopitems set itemid='" + this.商品物品代码.getText() + "' where shopitemid=" + this.商品序号.getText() + ";";
                    PreparedStatement itemid = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    itemid.executeUpdate(sqlString1);

                    sqlString2 = "update shopitems set price='" + this.商品售价金币.getText() + "' where shopitemid=" + this.商品序号.getText() + ";";
                    PreparedStatement price = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    price.executeUpdate(sqlString2);

                    sqlString3 = "update shopitems set shopid='" + this.商店代码.getText() + "' where shopitemid=" + this.商品序号.getText() + ";";
                    PreparedStatement shopid = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    shopid.executeUpdate(sqlString3);

                    查询商店();
                }
                ZEVMS2提示框.setText("[信息]:商店商品修改成功。");
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:选择你要修改的商品,并填写<商店ID><物品代码><售价金币>。");
        }
    }//GEN-LAST:event_修改商品ActionPerformed

    private void 商品名称ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_商品名称ActionPerformed

    }//GEN-LAST:event_商品名称ActionPerformed
    public void 刷新反应堆() {
        for (int i = ((DefaultTableModel) (this.反应堆.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.反应堆.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM reactordrops ");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 反应堆.getModel()).insertRow(反应堆.getRowCount(), new Object[]{rs.getInt("reactordropid"), rs.getInt("reactorid"), rs.getInt("itemid"), rs.getInt("chance"), MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))});
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        反应堆.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 反应堆.getSelectedRow();
                String a = 反应堆.getValueAt(i, 0).toString();
                String a1 = 反应堆.getValueAt(i, 1).toString();
                String a2 = 反应堆.getValueAt(i, 2).toString();
                String a3 = 反应堆.getValueAt(i, 3).toString();
                反应堆序列号.setText(a);
                反应堆代码.setText(a1);
                反应堆物品.setText(a2);
                反应堆概率.setText(a3);
            }
        });
    }
    private void 删除反应堆物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除反应堆物品ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.删除反应堆物品代码.getText().matches("[0-9]+");

        if (result1) {
            if (Integer.parseInt(this.删除反应堆物品代码.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "请填写正确的值");
            }
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM reactordrops WHERE itemid = ?");
                ps1.setInt(1, Integer.parseInt(this.删除反应堆物品代码.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from reactordrops where itemid =" + Integer.parseInt(this.删除反应堆物品代码.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(null, "成功删除 " + Integer.parseInt(this.删除反应堆物品代码.getText()) + " 物品，重载后生效。");
                    刷新反应堆();

                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你要删除的反应堆代码 ");
        }
    }//GEN-LAST:event_删除反应堆物品ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        刷新反应堆();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void 反应堆概率ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_反应堆概率ActionPerformed

    }//GEN-LAST:event_反应堆概率ActionPerformed

    private void 新增反应堆物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增反应堆物品ActionPerformed

        boolean result2 = this.反应堆代码.getText().matches("[0-9]+");

        if (result2) {
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO reactordrops ( reactorid ,itemid ,chance ,questid ) VALUES ( ?, ?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(this.反应堆代码.getText()));
                ps.setInt(2, Integer.parseInt(this.反应堆物品.getText()));
                ps.setInt(3, Integer.parseInt(this.反应堆概率.getText()));
                ps.setInt(4, -1);
                ps.executeUpdate();
                刷新反应堆();
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入反应堆代码，物品代码，掉落概率 ");
        }
    }//GEN-LAST:event_新增反应堆物品ActionPerformed

    private void 删除反应堆物品1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除反应堆物品1ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.反应堆序列号.getText().matches("[0-9]+");

        if (result1) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM reactordrops WHERE reactordropid = ?");
                ps1.setInt(1, Integer.parseInt(this.反应堆序列号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from reactordrops where reactordropid =" + Integer.parseInt(this.反应堆序列号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新反应堆();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的物品 ");
        }
    }//GEN-LAST:event_删除反应堆物品1ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        boolean result2 = this.查找反应堆掉落.getText().matches("[0-9]+");
        if (result2) {
            for (int i = ((DefaultTableModel) (this.反应堆.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.反应堆.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM reactordrops WHERE reactorid = " + Integer.parseInt(查找反应堆掉落.getText()));
                rs = ps.executeQuery();
                while (rs.next()) {
                    ((DefaultTableModel) 反应堆.getModel()).insertRow(反应堆.getRowCount(), new Object[]{rs.getInt("reactordropid"), rs.getInt("reactorid"), rs.getInt("itemid"), rs.getInt("chance"), MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))});
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你要查找的反应堆 ");
        }
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        boolean result2 = this.查找反应堆掉落.getText().matches("[0-9]+");
        if (result2) {
            for (int i = ((DefaultTableModel) (this.反应堆.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.反应堆.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;

                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM reactordrops WHERE itemid = " + Integer.parseInt(查找物品.getText()));
                rs = ps.executeQuery();
                while (rs.next()) {
                    ((DefaultTableModel) 反应堆.getModel()).insertRow(反应堆.getRowCount(), new Object[]{
                        rs.getInt("reactordropid"),
                        rs.getInt("reactorid"),
                        rs.getInt("itemid"),
                        rs.getInt("chance"),
                        MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                    });

                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入你要查找的物品代码 ");
        }
    }//GEN-LAST:event_jButton37ActionPerformed

    private void 修改反应堆物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改反应堆物品ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.反应堆代码.getText().matches("[0-9]+");
        boolean result1 = this.反应堆物品.getText().matches("[0-9]+");
        boolean result2 = this.反应堆概率.getText().matches("[0-9]+");

        if (result && result1 && result2) {
            if (Integer.parseInt(this.反应堆代码.getText()) < 0 && Integer.parseInt(this.反应堆物品.getText()) < 0 && Integer.parseInt(this.反应堆概率.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "请填写正确的值");
            }
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE reactordrops SET reactorid = ?,itemid = ?,chance = ?WHERE reactordropid = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM reactordrops WHERE reactordropid = ?");
                ps1.setInt(1, Integer.parseInt(this.反应堆序列号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    String sqlString2 = null;
                    String sqlString3 = null;
                    sqlString1 = "update reactordrops set reactorid='" + this.反应堆代码.getText() + "' where reactordropid=" + this.反应堆序列号.getText() + ";";
                    PreparedStatement itemid = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    itemid.executeUpdate(sqlString1);

                    sqlString2 = "update reactordrops set itemid='" + this.反应堆物品.getText() + "' where reactordropid=" + this.反应堆序列号.getText() + ";";
                    PreparedStatement price = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    price.executeUpdate(sqlString2);

                    sqlString3 = "update reactordrops set chance='" + this.反应堆概率.getText() + "' where reactordropid=" + this.反应堆序列号.getText() + ";";
                    PreparedStatement shopid = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    shopid.executeUpdate(sqlString3);

                    刷新反应堆();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要修改的数据");
        }
    }//GEN-LAST:event_修改反应堆物品ActionPerformed

    private void 豆豆机修改设置ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_豆豆机修改设置ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET (name = ? )WHERE id = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
            ps1.setInt(1, 1);
            rs = ps1.executeQuery();
            if (rs.next()) {
                String a1 = null;
                String a2 = null;
                String a3 = null;
                String a4 = null;
                a1 = "update configvalues set Val='" + this.打豆豆力度.getText() + "' where id=20000;";
                PreparedStatement aa1 = DatabaseConnection.getConnection().prepareStatement(a1);
                aa1.executeUpdate(a1);

                a2 = "update configvalues set Val='" + this.豆豆进洞率.getText() + "' where id=20001;";
                PreparedStatement aa2 = DatabaseConnection.getConnection().prepareStatement(a2);
                aa2.executeUpdate(a2);

                a3 = "update configvalues set Val='" + this.进洞开奖率.getText() + "' where id=20002;";
                PreparedStatement aa3 = DatabaseConnection.getConnection().prepareStatement(a3);
                aa3.executeUpdate(a3);

                a4 = "update configvalues set Val='" + this.豆豆稀有率.getText() + "' where id=20003;";
                PreparedStatement aa4 = DatabaseConnection.getConnection().prepareStatement(a4);
                aa4.executeUpdate(a4);
                gui.Start.GetConfigValues();
                ZEVMS2提示框.setText("[信息]:豆豆机设置修改成功。");
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_豆豆机修改设置ActionPerformed

    private void 豆豆机修改设置1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_豆豆机修改设置1ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET (name = ? )WHERE id = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
            ps1.setInt(1, 1);
            rs = ps1.executeQuery();
            if (rs.next()) {
                String a1 = null;
                String a2 = null;
                String a3 = null;
                a1 = "update configvalues set Val='" + this.红豆豆加开奖率.getText() + "' where id=20010;";
                PreparedStatement aa1 = DatabaseConnection.getConnection().prepareStatement(a1);
                aa1.executeUpdate(a1);

                a2 = "update configvalues set Val='" + this.蓝豆豆加开奖率.getText() + "' where id=20011;";
                PreparedStatement aa2 = DatabaseConnection.getConnection().prepareStatement(a2);
                aa2.executeUpdate(a2);

                a3 = "update configvalues set Val='" + this.绿豆豆加开奖率.getText() + "' where id=20012;";
                PreparedStatement aa3 = DatabaseConnection.getConnection().prepareStatement(a3);
                aa3.executeUpdate(a3);

                gui.Start.GetConfigValues();
                ZEVMS2提示框.setText("[信息]:豆豆机类型增加中奖率设置修改成功。");
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_豆豆机修改设置1ActionPerformed

    private void 豆豆机修改设置2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_豆豆机修改设置2ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET (name = ? )WHERE id = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
            ps1.setInt(1, 1);
            rs = ps1.executeQuery();
            if (rs.next()) {
                String a1 = null;
                a1 = "update configvalues set Val='" + this.豆豆奖励.getText() + "' where id=20005;";
                PreparedStatement aa1 = DatabaseConnection.getConnection().prepareStatement(a1);
                aa1.executeUpdate(a1);

                ZEVMS2提示框.setText("[信息]:豆豆机设置修改成功。");
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_豆豆机修改设置2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        刷新药水冷却();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.药水冷却时间.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ?WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.药水序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val='" + this.药水冷却时间.getText() + "' where id=" + this.药水序号.getText() + ";";
                    PreparedStatement name = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    name.executeUpdate(sqlString1);
                    ZEVMS2提示框.setText("[信息]:修改冷却成功。");
                    刷新药水冷却();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void 刷新怪物卡片ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新怪物卡片ActionPerformed
        刷新怪物卡片();
    }//GEN-LAST:event_刷新怪物卡片ActionPerformed

    private void 修改物品掉落持续时间1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改物品掉落持续时间1ActionPerformed
        boolean result2 = this.地图物品上限.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 997);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.地图物品上限.getText() + "' where id = 997;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新地图物品上限();
                    ZEVMS2提示框.setText("[信息]:修改成功。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请输入你要修改的数据。");
        }
    }//GEN-LAST:event_修改物品掉落持续时间1ActionPerformed

    private void 修改物品掉落持续时间2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改物品掉落持续时间2ActionPerformed
        boolean result2 = this.地图刷新频率.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 996);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.地图刷新频率.getText() + "' where id = 996;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新地图刷新频率();
                    ZEVMS2提示框.setText("[信息]:修改成功。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ZEVMS2提示框.setText("[信息]:请输入你要修改的数据。");
        }
    }//GEN-LAST:event_修改物品掉落持续时间2ActionPerformed
    private void 刷新药水冷却() {
        for (int i = ((DefaultTableModel) (this.药水冷却.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.药水冷却.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id >= 30000 && id<= 31000");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 药水冷却.getModel()).insertRow(药水冷却.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    rs.getString("Name"),
                    rs.getInt("Val")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        药水冷却.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 药水冷却.getSelectedRow();
                String a = 药水冷却.getValueAt(i, 0).toString();
                String a1 = 药水冷却.getValueAt(i, 1).toString();
                String a2 = 药水冷却.getValueAt(i, 2).toString();
                药水序号.setText(a);
                药水名字.setText(a1);
                药水冷却时间.setText(a2);
            }
        });

    }

    public void 刷新世界爆物() {

        for (int i = ((DefaultTableModel) (this.世界爆物.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.世界爆物.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM drop_data_global WHERE itemid !=0");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 世界爆物.getModel()).insertRow(世界爆物.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    rs.getInt("itemid"),
                    rs.getString("chance"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                });
            }
            世界爆物.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int i = 世界爆物.getSelectedRow();
                    String a = 世界爆物.getValueAt(i, 0).toString();
                    String a1 = 世界爆物.getValueAt(i, 1).toString();
                    String a2 = 世界爆物.getValueAt(i, 2).toString();
                    世界爆物序列号.setText(a);
                    世界爆物物品代码.setText(a1);
                    世界爆物爆率.setText(a2);
                }
            });

        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void 刷新指定怪物爆物() {
        boolean result = this.查询怪物掉落代码.getText().matches("[0-9]+");
        if (result) {
            if (Integer.parseInt(this.查询怪物掉落代码.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "请填写正确的值");
            }
            for (int i = ((DefaultTableModel) (this.怪物爆物.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.怪物爆物.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;

                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM drop_data WHERE dropperid =  " + Integer.parseInt(this.怪物爆物怪物代码.getText()) + "");
                rs = ps.executeQuery();
                while (rs.next()) {
                    ((DefaultTableModel) 怪物爆物.getModel()).insertRow(怪物爆物.getRowCount(), new Object[]{rs.getInt("id"), rs.getInt("dropperid"), rs.getInt("itemid"), rs.getInt("chance"), MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))});
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
            怪物爆物.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int i = 怪物爆物.getSelectedRow();
                    String a = 怪物爆物.getValueAt(i, 0).toString();
                    String a1 = 怪物爆物.getValueAt(i, 1).toString();
                    String a2 = 怪物爆物.getValueAt(i, 2).toString();
                    String a3 = 怪物爆物.getValueAt(i, 3).toString();
                    String a4 = 怪物爆物.getValueAt(i, 4).toString();
                    怪物爆物序列号.setText(a);
                    怪物爆物怪物代码.setText(a1);
                    怪物爆物物品代码.setText(a2);
                    怪物爆物爆率.setText(a3);
                    怪物爆物物品名称.setText(a4);
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, "请输入要查询的怪物代码 ");
        }
    }

    public void 刷新怪物爆物() {
        for (int i = ((DefaultTableModel) (this.怪物爆物.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.怪物爆物.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM drop_data WHERE itemid !=0");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 怪物爆物.getModel()).insertRow(怪物爆物.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    rs.getInt("dropperid"),
                    //MapleLifeFactory.getMonster(rs.getInt("dropperid")),
                    rs.getInt("itemid"),
                    rs.getInt("chance"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        怪物爆物.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 怪物爆物.getSelectedRow();
                String a = 怪物爆物.getValueAt(i, 0).toString();
                String a1 = 怪物爆物.getValueAt(i, 1).toString();
                String a2 = 怪物爆物.getValueAt(i, 2).toString();
                String a3 = 怪物爆物.getValueAt(i, 3).toString();
                //String a4 = 怪物爆物.getValueAt(i, 4).toString();
                怪物爆物序列号.setText(a);
                怪物爆物怪物代码.setText(a1);
                怪物爆物物品代码.setText(a2);
                怪物爆物爆率.setText(a3);
                //怪物爆物物品名称.setText(a4);

            }
        });
    }

    public void 刷新怪物卡片() {
        for (int i = ((DefaultTableModel) (this.怪物爆物.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.怪物爆物.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM drop_data WHERE itemid >=2380000&& itemid <2390000");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 怪物爆物.getModel()).insertRow(怪物爆物.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    rs.getInt("dropperid"),
                    //MapleLifeFactory.getMonster(rs.getInt("dropperid")),
                    rs.getInt("itemid"),
                    rs.getInt("chance"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        怪物爆物.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 怪物爆物.getSelectedRow();
                String a = 怪物爆物.getValueAt(i, 0).toString();
                String a1 = 怪物爆物.getValueAt(i, 1).toString();
                String a2 = 怪物爆物.getValueAt(i, 2).toString();
                String a3 = 怪物爆物.getValueAt(i, 3).toString();
                //String a4 = 怪物爆物.getValueAt(i, 4).toString();
                怪物爆物序列号.setText(a);
                怪物爆物怪物代码.setText(a1);
                怪物爆物物品代码.setText(a2);
                怪物爆物爆率.setText(a3);
                //怪物爆物物品名称.setText(a4);

            }
        });
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(控制台2号.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        控制台2号.setDefaultLookAndFeelDecorated(true);
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
                new 控制台2号().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ZEVMS2提示框;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel202;
    private javax.swing.JLabel jLabel209;
    private javax.swing.JLabel jLabel210;
    private javax.swing.JLabel jLabel211;
    private javax.swing.JLabel jLabel212;
    private javax.swing.JLabel jLabel213;
    private javax.swing.JLabel jLabel216;
    private javax.swing.JLabel jLabel260;
    private javax.swing.JLabel jLabel261;
    private javax.swing.JLabel jLabel262;
    private javax.swing.JLabel jLabel263;
    private javax.swing.JLabel jLabel264;
    private javax.swing.JLabel jLabel265;
    private javax.swing.JLabel jLabel266;
    private javax.swing.JLabel jLabel267;
    private javax.swing.JLabel jLabel268;
    private javax.swing.JLabel jLabel269;
    private javax.swing.JLabel jLabel270;
    private javax.swing.JLabel jLabel271;
    private javax.swing.JLabel jLabel272;
    private javax.swing.JLabel jLabel273;
    private javax.swing.JLabel jLabel274;
    private javax.swing.JLabel jLabel275;
    private javax.swing.JLabel jLabel277;
    private javax.swing.JLabel jLabel278;
    private javax.swing.JLabel jLabel279;
    private javax.swing.JLabel jLabel280;
    private javax.swing.JLabel jLabel281;
    private javax.swing.JLabel jLabel282;
    private javax.swing.JLabel jLabel285;
    private javax.swing.JLabel jLabel286;
    private javax.swing.JLabel jLabel287;
    private javax.swing.JLabel jLabel315;
    private javax.swing.JLabel jLabel316;
    private javax.swing.JLabel jLabel317;
    private javax.swing.JLabel jLabel318;
    private javax.swing.JLabel jLabel319;
    private javax.swing.JLabel jLabel320;
    private javax.swing.JLabel jLabel321;
    private javax.swing.JLabel jLabel322;
    private javax.swing.JLabel jLabel323;
    private javax.swing.JLabel jLabel324;
    private javax.swing.JLabel jLabel325;
    private javax.swing.JLabel jLabel326;
    private javax.swing.JLabel jLabel327;
    private javax.swing.JLabel jLabel328;
    private javax.swing.JLabel jLabel329;
    private javax.swing.JLabel jLabel330;
    private javax.swing.JLabel jLabel337;
    private javax.swing.JLabel jLabel338;
    private javax.swing.JLabel jLabel339;
    private javax.swing.JLabel jLabel340;
    private javax.swing.JLabel jLabel341;
    private javax.swing.JLabel jLabel342;
    private javax.swing.JLabel jLabel343;
    private javax.swing.JLabel jLabel344;
    private javax.swing.JLabel jLabel345;
    private javax.swing.JLabel jLabel346;
    private javax.swing.JLabel jLabel347;
    private javax.swing.JLabel jLabel348;
    private javax.swing.JLabel jLabel349;
    private javax.swing.JLabel jLabel350;
    private javax.swing.JLabel jLabel351;
    private javax.swing.JLabel jLabel352;
    private javax.swing.JLabel jLabel353;
    private javax.swing.JLabel jLabel354;
    private javax.swing.JLabel jLabel355;
    private javax.swing.JLabel jLabel356;
    private javax.swing.JLabel jLabel357;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable 世界爆物;
    private javax.swing.JTextField 世界爆物名称;
    private javax.swing.JTextField 世界爆物序列号;
    private javax.swing.JTextField 世界爆物爆率;
    private javax.swing.JTextField 世界爆物物品代码;
    private javax.swing.JButton 修改世界爆物;
    private javax.swing.JButton 修改反应堆物品;
    private javax.swing.JButton 修改商品;
    private javax.swing.JButton 修改怪物爆物;
    private javax.swing.JButton 修改挖矿物品;
    private javax.swing.JButton 修改挖矿物品1;
    private javax.swing.JButton 修改物品掉落持续时间;
    private javax.swing.JButton 修改物品掉落持续时间1;
    private javax.swing.JButton 修改物品掉落持续时间2;
    private javax.swing.JButton 修改钓鱼物品;
    private javax.swing.JButton 删除世界爆物;
    private javax.swing.JButton 删除反应堆物品;
    private javax.swing.JButton 删除反应堆物品1;
    private javax.swing.JTextField 删除反应堆物品代码;
    private javax.swing.JButton 删除商品;
    private javax.swing.JButton 删除怪物爆物;
    private javax.swing.JTextField 删除指定的掉落;
    private javax.swing.JButton 删除指定的掉落按键;
    private javax.swing.JButton 删除挖矿物品1;
    private javax.swing.JButton 删除挖矿物品2;
    private javax.swing.JPanel 删除游戏物品;
    private javax.swing.JButton 删除道具;
    private javax.swing.JButton 删除道具1;
    private javax.swing.JButton 删除道具2;
    private javax.swing.JButton 删除道具3;
    private javax.swing.JButton 删除道具4;
    private javax.swing.JButton 删除钓鱼物品;
    private javax.swing.JButton 刷新世界爆物;
    private javax.swing.JButton 刷新怪物卡片;
    private javax.swing.JButton 刷新怪物爆物;
    private javax.swing.JButton 刷新挖矿奖励;
    private javax.swing.JButton 刷新挖矿奖励1;
    private javax.swing.JButton 刷新钓鱼物品;
    private javax.swing.JLabel 副业提示语言;
    private javax.swing.JTable 反应堆;
    private javax.swing.JTextField 反应堆代码;
    private javax.swing.JTextField 反应堆序列号;
    private javax.swing.JTextField 反应堆概率;
    private javax.swing.JTextField 反应堆物品;
    private javax.swing.JPanel 反应堆设置;
    private javax.swing.JTextField 商品名称;
    private javax.swing.JTextField 商品售价金币;
    private javax.swing.JTextField 商品序号;
    private javax.swing.JTextField 商品物品代码;
    private javax.swing.JTextField 商店代码;
    private javax.swing.JLabel 商店提示语言;
    private javax.swing.JTextField 地图刷新频率;
    private javax.swing.JTextField 地图物品上限;
    private javax.swing.JTextField 增加挖矿物品;
    private javax.swing.JTextField 增加挖矿物品1;
    private javax.swing.JTable 怪物爆物;
    private javax.swing.JTextField 怪物爆物序列号;
    private javax.swing.JTextField 怪物爆物怪物代码;
    private javax.swing.JTextField 怪物爆物爆率;
    private javax.swing.JTextField 怪物爆物物品代码;
    private javax.swing.JTextField 怪物爆物物品名称;
    private javax.swing.JTextField 打豆豆力度;
    private javax.swing.JTable 挖矿反应堆;
    private javax.swing.JTextField 挖矿序列号;
    private javax.swing.JTextField 挖矿序列号1;
    private javax.swing.JTextField 挖矿物品概率;
    private javax.swing.JTextField 挖矿物品概率1;
    private javax.swing.JPanel 挖矿管理;
    private javax.swing.JButton 新增反应堆物品;
    private javax.swing.JButton 新增商品;
    private javax.swing.JButton 新增挖矿物品;
    private javax.swing.JButton 新增挖矿物品1;
    private javax.swing.JButton 新增挖矿物品2;
    private javax.swing.JButton 新增钓鱼物品;
    private javax.swing.JTextField 查找反应堆掉落;
    private javax.swing.JTextField 查找物品;
    private javax.swing.JButton 查找道具;
    private javax.swing.JButton 查找道具1;
    private javax.swing.JButton 查找道具2;
    private javax.swing.JButton 查找道具3;
    private javax.swing.JButton 查找道具4;
    private javax.swing.JTextField 查询商店;
    private javax.swing.JButton 查询商店2;
    private javax.swing.JButton 查询怪物掉落;
    private javax.swing.JTextField 查询怪物掉落代码;
    private javax.swing.JButton 查询物品掉落;
    private javax.swing.JTextField 查询物品掉落代码;
    private javax.swing.JButton 添加世界爆物;
    private javax.swing.JButton 添加怪物爆物;
    private javax.swing.JPanel 游戏商店;
    private javax.swing.JTable 游戏商店2;
    private javax.swing.JPanel 游戏爆物;
    private javax.swing.JTable 游戏道具;
    private javax.swing.JTextField 游戏道具代码;
    private javax.swing.JTextField 物品名称;
    private javax.swing.JTextField 物品名称1;
    private javax.swing.JTextField 物品掉落持续时间;
    private javax.swing.JTextField 红豆豆加开奖率;
    private javax.swing.JTextField 绿豆豆加开奖率;
    private javax.swing.JTable 药水冷却;
    private javax.swing.JTextField 药水冷却时间;
    private javax.swing.JTextField 药水名字;
    private javax.swing.JTextField 药水序号;
    private javax.swing.JTextField 蓝豆豆加开奖率;
    private javax.swing.JTextField 豆豆奖励;
    private javax.swing.JButton 豆豆机修改设置;
    private javax.swing.JButton 豆豆机修改设置1;
    private javax.swing.JButton 豆豆机修改设置2;
    private javax.swing.JPanel 豆豆机管理;
    private javax.swing.JTextField 豆豆稀有率;
    private javax.swing.JTextField 豆豆进洞率;
    private javax.swing.JTextField 进洞开奖率;
    private javax.swing.JTable 钓鱼物品;
    private javax.swing.JTextField 钓鱼物品代码;
    private javax.swing.JTextField 钓鱼物品名称;
    private javax.swing.JTextField 钓鱼物品序号;
    private javax.swing.JTextField 钓鱼物品概率;
    private javax.swing.JPanel 钓鱼管理;
    // End of variables declaration//GEN-END:variables
}
