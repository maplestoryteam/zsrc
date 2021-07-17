package gui.控制台;

import static abc.Game.版本;
import abc.sancu.FileDemo_05;
import client.脚本编辑器;
import handling.world.MapleParty;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;
import server.MapleItemInformationProvider;
import tools.FileoutputUtil;

public class 脚本编辑器2 extends javax.swing.JFrame {

    public 脚本编辑器2() {
        setTitle("ZEVMS脚本生成器");
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        initComponents();
        String 输出1 = "";
        //输出版本号和公告信息
        try {
            String versionInfo = 脚本编辑器.Pv(版本);
            String unicode = new String(versionInfo.getBytes(), "UTF-8");
            unicode = unicode.replace("\\n", "\n");
            脚本显示窗.setText("" + unicode + "");

        } catch (Exception e) {
            System.out.println("获取版本信息出错。");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        脚本编号 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        脚本显示窗 = new javax.swing.JTextPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        判断物品代码 = new javax.swing.JTextField();
        判断区域 = new javax.swing.JButton();
        判断物品数量 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        判断金钱数量 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        判断金币 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        收取物品代码 = new javax.swing.JTextField();
        收取区域 = new javax.swing.JButton();
        收取物品数量 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        收取金钱数量 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        给予物品代码 = new javax.swing.JTextField();
        给予区域 = new javax.swing.JButton();
        给予物品数量 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        脚本结束 = new javax.swing.JButton();
        开始写脚本 = new javax.swing.JButton();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel1.setText("请先定义脚本编号；");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, -1, -1));
        jPanel1.add(脚本编号, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 30, 100, 30));

        脚本显示窗.setBackground(new java.awt.Color(0, 0, 0));
        脚本显示窗.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        脚本显示窗.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(脚本显示窗);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 700));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "判断区域", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(判断物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 150, 30));

        判断区域.setText("判断物品和数量");
        判断区域.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                判断区域ActionPerformed(evt);
            }
        });
        jPanel2.add(判断区域, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 150, 30));
        jPanel2.add(判断物品数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 150, 30));

        jLabel2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel2.setText("物品数量；");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel3.setText("物品代码；");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        jPanel4.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 210, 210));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "判断金钱", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(判断金钱数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 110, 30));

        jLabel4.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel4.setText("数量；");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 30));

        jButton1.setText("点券");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 70, 30));

        判断金币.setText("金币");
        判断金币.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                判断金币ActionPerformed(evt);
            }
        });
        jPanel3.add(判断金币, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 70, 30));

        jPanel4.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 210, 140));

        jTabbedPane1.addTab("判断类", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "收取区域", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(收取物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 150, 30));

        收取区域.setText("收取材料");
        收取区域.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                收取区域ActionPerformed(evt);
            }
        });
        jPanel6.add(收取区域, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 150, 30));
        jPanel6.add(收取物品数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 150, 30));

        jLabel5.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel5.setText("物品数量；");
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jLabel6.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel6.setText("物品代码；");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 210, 210));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "收取金钱", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(收取金钱数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 110, 30));

        jLabel7.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel7.setText("数量；");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 30));

        jButton3.setText("点券");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 70, 30));

        jButton4.setText("金币");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 70, 30));

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 210, 140));

        jTabbedPane1.addTab("收取类", jPanel5);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "给予区域", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel9.add(给予物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 150, 30));

        给予区域.setText("给予物品");
        给予区域.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                给予区域ActionPerformed(evt);
            }
        });
        jPanel9.add(给予区域, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 150, 30));
        jPanel9.add(给予物品数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 150, 30));

        jLabel8.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel8.setText("物品数量；");
        jPanel9.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jLabel9.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel9.setText("物品代码；");
        jPanel9.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        jPanel8.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 210, 210));

        jTabbedPane1.addTab("给予类", jPanel8);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 70, 240, 580));

        脚本结束.setText("脚本结束");
        脚本结束.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                脚本结束ActionPerformed(evt);
            }
        });
        jPanel1.add(脚本结束, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 660, 130, 30));

        开始写脚本.setText("开始写脚本");
        开始写脚本.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                开始写脚本ActionPerformed(evt);
            }
        });
        jPanel1.add(开始写脚本, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 30, 130, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 700));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void 显示(String str) {
        脚本显示窗.setText(脚本显示窗.getText() + "" + str + "\r\n");
    }
    private void 开始写脚本ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_开始写脚本ActionPerformed

        boolean 编号 = this.脚本编号.getText().matches("[0-9]+");
        if (!编号 || 脚本编号.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "脚本编号错误。");
            return;
        }
        脚本显示窗.setText("");
        //删除已经同名的文件
        Properties 設定檔 = System.getProperties();
        FileDemo_05.deleteFiles("" + 設定檔.getProperty("user.dir") + "/脚本生成文档/" + 脚本编号.getText() + ".js");
        //输出
        FileoutputUtil.logToFile("脚本生成文档/" + 脚本编号.getText() + ".js",
                "/*\r\n"
                + "ZEVMS脚本生成器2\r\n"
                + "*/\r\n"
                + "importPackage(java.lang);\r\n"
                + "importPackage(Packages.tools);\r\n"
                + "importPackage(Packages.client);\r\n"
                + "importPackage(Packages.server);\r\n"
                + "function action(mode, type, selection) {\r\n"
        );
        MapleParty.脚本给予1 = 0;
        MapleParty.脚本判断1 = 0;
        MapleParty.脚本收取1 = 0;
        MapleParty.脚本判断数量 = 0;
        MapleParty.脚本收取数量 = 0;
        显示("脚本开始，编号为；" + 脚本编号.getText() + ".js\r\n");
    }//GEN-LAST:event_开始写脚本ActionPerformed

    private void 判断区域ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_判断区域ActionPerformed
        boolean a1 = this.判断物品代码.getText().matches("[0-9]+");
        boolean a2 = this.判断物品代码.getText().matches("[0-9]+");
        if (!a1 || !a2 || 判断物品代码.getText().equals("") || 判断物品代码.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "无效的数据填写。");
            return;
        }
        FileoutputUtil.logToFile("脚本生成文档/" + 脚本编号.getText() + ".js",
                "    if(!cm.判断物品数量(" + 判断物品代码.getText() + ", " + 判断物品数量.getText() + ")){\r\n"
                + "        cm.说明文字(\"需要#v" + 判断物品代码.getText() + "##b#t" + 判断物品代码.getText() + "# #kx " + 判断物品数量.getText() + "#k\");\r\n"
                + "        cm.对话结束();\r\n"
                + "        return;\r\n"
                + "    }\r\n"
        );
        if (MapleParty.脚本判断1 <= 0) {
            显示("需要材料物品；\r\n┏(判断)");
            MapleParty.脚本判断1 = 1;
        }
        MapleParty.脚本判断数量 += 1;
        显示("┣判断需要 " + MapleItemInformationProvider.getInstance().getName(Integer.parseInt(判断物品代码.getText())) + "(" + Integer.parseInt(判断物品代码.getText()) + ") x " + 判断物品数量.getText() + "");
    }//GEN-LAST:event_判断区域ActionPerformed

    private void 脚本结束ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_脚本结束ActionPerformed
        Properties 設定檔 = System.getProperties();
        MapleParty.脚本给予1 = 0;
        MapleParty.脚本判断1 = 0;
        MapleParty.脚本收取1 = 0;
        MapleParty.脚本判断数量 = 0;
        MapleParty.脚本收取数量 = 0;
        FileoutputUtil.logToFile("脚本生成文档/" + 脚本编号.getText() + ".js",
                "    cm.对话结束()\r\n"
                + "}"
        );

        显示("┗(脚本结束)\r\n\r\n脚本结束，请去目录路径:" + 設定檔.getProperty("user.dir") + "\\脚本生成文档\\" + 脚本编号.getText() + ".js更改脚本编号。");
    }//GEN-LAST:event_脚本结束ActionPerformed

    private void 收取区域ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_收取区域ActionPerformed
        boolean a1 = this.收取物品代码.getText().matches("[0-9]+");
        boolean a2 = this.收取物品数量.getText().matches("[0-9]+");
        if (!a1 || !a2 || 收取物品代码.getText().equals("") || 收取物品数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "无效的数据填写。");
            return;
        }
        FileoutputUtil.logToFile("脚本生成文档/" + 脚本编号.getText() + ".js",
                "    cm.收物品(" + 收取物品代码.getText() + ", " + 收取物品数量.getText() + ");\r\n"
        );
        if (MapleParty.脚本收取1 <= 0) {
            显示("┗(判断结束)\r\n收取材料物品；\r\n┏(收取)");
            MapleParty.脚本收取1 = 1;
        }
        MapleParty.脚本收取数量 += 1;
        显示("┣收取物品 " + MapleItemInformationProvider.getInstance().getName(Integer.parseInt(收取物品代码.getText())) + "(" + Integer.parseInt(收取物品代码.getText()) + ") x " + 收取物品数量.getText() + "");
    }//GEN-LAST:event_收取区域ActionPerformed

    private void 给予区域ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_给予区域ActionPerformed
        boolean a1 = this.给予物品代码.getText().matches("[0-9]+");
        boolean a2 = this.给予物品数量.getText().matches("[0-9]+");
        if (!a1 || !a2 || 给予物品代码.getText().equals("") || 给予物品数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "无效的数据填写。");
            return;
        }
        if (MapleParty.脚本判断数量 != MapleParty.脚本收取数量) {
            int n = JOptionPane.showConfirmDialog(this, "判断数量不等于收取数量，是否要这样写？。", "提示", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.NO_OPTION) {
                return;
            }
        }
        if (MapleParty.脚本给予1 <= 0) {
            显示("┗(收取结束)\r\n给予的物品；\r\n┏(给予)");
            MapleParty.脚本给予1 = 1;
        }
        FileoutputUtil.logToFile("脚本生成文档/" + 脚本编号.getText() + ".js",
                "    cm.给物品(" + 给予物品代码.getText() + ", " + 给予物品数量.getText() + ")\r\n"
        );

        显示("┣给予物品 " + MapleItemInformationProvider.getInstance().getName(Integer.parseInt(给予物品代码.getText())) + "(" + Integer.parseInt(给予物品代码.getText()) + ") x " + 给予物品数量.getText() + "");
    }//GEN-LAST:event_给予区域ActionPerformed

    private void 判断金币ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_判断金币ActionPerformed
        boolean a1 = this.判断金钱数量.getText().matches("[0-9]+");
        boolean a2 = this.判断金钱数量.getText().matches("[0-9]+");
        if (!a1 || !a2 || 判断金钱数量.getText().equals("") || 判断金钱数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "无效的数据填写。");
            return;
        }
        FileoutputUtil.logToFile("脚本生成文档/" + 脚本编号.getText() + ".js",
                "    if(cm.判断金币()<=" + 判断金钱数量.getText() + "){\r\n"
                + "        cm.说明文字(\"需要 #b" + 判断金钱数量.getText() + " #k 金币。\");\r\n"
                + "        cm.对话结束();\r\n"
                + "        return;\r\n"
                + "    }\r\n"
        );
        if (MapleParty.脚本判断1 <= 0) {
            显示("需要材料物品；\r\n┏(判断)");
            MapleParty.脚本判断1 = 1;
        }
        MapleParty.脚本判断数量 += 1;
        显示("┣判断需要 " + 判断金钱数量.getText() + " 金币");
    }//GEN-LAST:event_判断金币ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        boolean a1 = this.判断金钱数量.getText().matches("[0-9]+");
        boolean a2 = this.判断金钱数量.getText().matches("[0-9]+");
        if (!a1 || !a2 || 判断金钱数量.getText().equals("") || 判断金钱数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "无效的数据填写。");
            return;
        }
        FileoutputUtil.logToFile("脚本生成文档/" + 脚本编号.getText() + ".js",
                "    if(cm.判断点券()<=" + 判断金钱数量.getText() + "){\r\n"
                + "        cm.说明文字(\"需要  #b" + 判断金钱数量.getText() + " #k 点券。\");\r\n"
                + "        cm.对话结束();\r\n"
                + "        return;\r\n"
                + "    }\r\n"
        );
        if (MapleParty.脚本判断1 <= 0) {
            显示("需要材料物品；\r\n┏(判断)");
            MapleParty.脚本判断1 = 1;
        }
        MapleParty.脚本判断数量 += 1;
        显示("┣判断需要 " + 判断金钱数量.getText() + " 点券");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        boolean a1 = this.收取金钱数量.getText().matches("[0-9]+");
        boolean a2 = this.收取金钱数量.getText().matches("[0-9]+");
        if (!a1 || !a2 || 收取金钱数量.getText().equals("") || 收取金钱数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "无效的数据填写。");
            return;
        }
        FileoutputUtil.logToFile("脚本生成文档/" + 脚本编号.getText() + ".js",
                "    cm.收金币(" + 收取金钱数量.getText() + ");\r\n"
        );
        if (MapleParty.脚本收取1 <= 0) {
            显示("┗(判断结束)\r\n收取材料物品；\r\n┏(收取)");
            MapleParty.脚本收取1 = 1;
        }
        MapleParty.脚本收取数量 += 1;
        显示("┣收取金币 " + 收取金钱数量.getText() + "");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        boolean a1 = this.收取金钱数量.getText().matches("[0-9]+");
        boolean a2 = this.收取金钱数量.getText().matches("[0-9]+");
        if (!a1 || !a2 || 收取金钱数量.getText().equals("") || 收取金钱数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "无效的数据填写。");
            return;
        }
        FileoutputUtil.logToFile("脚本生成文档/" + 脚本编号.getText() + ".js",
                "    cm.收点券(" + 收取金钱数量.getText() + ");\r\n"
        );
        if (MapleParty.脚本收取1 <= 0) {
            显示("┗(判断结束)\r\n收取材料物品；\r\n┏(收取)");
            MapleParty.脚本收取1 = 1;
        }
        MapleParty.脚本收取数量 += 1;
        显示("┣收取点券 " + 收取金钱数量.getText() + "");
    }//GEN-LAST:event_jButton3ActionPerformed

    public static void main(String args[]) {
        脚本编辑器2.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new 脚本编辑器2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton 判断区域;
    private javax.swing.JTextField 判断物品代码;
    private javax.swing.JTextField 判断物品数量;
    private javax.swing.JButton 判断金币;
    private javax.swing.JTextField 判断金钱数量;
    private javax.swing.JButton 开始写脚本;
    private javax.swing.JButton 收取区域;
    private javax.swing.JTextField 收取物品代码;
    private javax.swing.JTextField 收取物品数量;
    private javax.swing.JTextField 收取金钱数量;
    private javax.swing.JButton 给予区域;
    private javax.swing.JTextField 给予物品代码;
    private javax.swing.JTextField 给予物品数量;
    private javax.swing.JTextPane 脚本显示窗;
    private javax.swing.JButton 脚本结束;
    private javax.swing.JTextField 脚本编号;
    // End of variables declaration//GEN-END:variables
}
