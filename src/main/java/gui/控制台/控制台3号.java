package gui.控制台;

import database.DatabaseConnection;
import gui.ZEVMS;
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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static abc.Game.版本;

/**
 *
 * @author Administrator
 */
public class 控制台3号 extends javax.swing.JFrame {

    /**
     * Creates new form 副本奖励控制台
     */
    public 控制台3号() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        Properties 設定檔 = System.getProperties();
        setTitle("ZEVMS服务端后台 v" + 版本 + "<控制台3号>");
        initComponents();
        填充不存在的椅子信息();
        刷新财神椅子();
        刷新泡澡浴桶();
        刷新魔族突袭开关();
        刷新OX答题开关();
        刷新每日送货开关();
        刷新幸运职业开关();
        刷新魔族攻城开关();
        刷新周末倍率开关();
        刷新神秘商人开关();
        刷新野外通缉开关();
        刷新喜从天降开关();
        刷新题库();
        刷新飞鱼设置();
        刷新飞鱼金币开关();
        刷新飞鱼金币开关();
        刷新飞鱼金币开关();
        刷新鱼来鱼往开关();
        刷新神秘商人时间();
        刷新幸运职业();
        刷新响应人数();
        刷新椅子备注();
        刷新枫叶椅子();
        刷新打折优惠开关();
    }

    private void 刷新打折优惠开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("打折优惠开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        打折优惠开关.setText(显示);
    }

    private void 刷新枫叶椅子() {
        枫叶纪念凳金币.setText("" + gui.Start.ConfigValuesMap.get("枫叶纪念凳金币") + "");
        枫叶纪念凳经验.setText("" + gui.Start.ConfigValuesMap.get("枫叶纪念凳经验") + "");
        枫叶纪念凳人数.setText("" + gui.Start.ConfigValuesMap.get("枫叶纪念凳人数") + "");
        枫叶纪念凳间隔.setText("" + gui.Start.ConfigValuesMap.get("枫叶纪念凳间隔") + "");
    }

    private void 填充不存在的椅子信息() {
        if ("".equals(取椅子备注(3010025))) {
            Gainqqstem(" ", 3010025, 1);
        }
        if ("".equals(取椅子备注(3010100))) {
            Gainqqstem(" ", 3010100, 1);
        }
        if ("".equals(取椅子备注(3012002))) {
            Gainqqstem(" ", 3012002, 1);
        }
    }

    private void 刷新椅子备注() {
        枫叶纪念凳备注.setText(取椅子备注(3010025));
        泡澡浴桶备注.setText(取椅子备注(3010100));
        财神椅子备注.setText(取椅子备注(3012002));
    }

    private void 刷新响应人数() {
        响应人数显示.setText("" + gui.Start.ConfigValuesMap.get("响应人数设置") + "");
    }

    private void 刷新幸运职业() {

        幸运职业代码.setText("" + MapleParty.幸运职业 + "");
    }

    private void 刷新神秘商人时间() {

        神秘商人出现时间.setText("" + MapleParty.神秘商人时间 + "");
    }

    private void 刷新鱼来鱼往开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("鱼来鱼往开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        鱼来鱼往开关.setText(显示);
    }

    private void 刷新飞鱼设置() {
        飞鱼数量.setText("" + gui.Start.ConfigValuesMap.get("飞鱼数量") + "");
        经验倍率.setText("" + gui.Start.ConfigValuesMap.get("飞鱼经验倍率") + "");
        金币倍率.setText("" + gui.Start.ConfigValuesMap.get("飞鱼金币倍率") + "");
        点券倍率.setText("" + gui.Start.ConfigValuesMap.get("飞鱼点券倍率") + "");
    }

    private void 刷新飞鱼经验开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("飞鱼经验开关");
        if (S <= 0) {
            显示 = "开";
        } else {
            显示 = "关";
        }
        经验开关.setText(显示);
    }

    private void 刷新飞鱼金币开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("飞鱼金币开关");
        if (S <= 0) {
            显示 = "开";
        } else {
            显示 = "关";
        }
        金币开关.setText(显示);
    }

    private void 刷新飞鱼点券开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("飞鱼点券开关");
        if (S <= 0) {
            显示 = "开";
        } else {
            显示 = "关";
        }
        点券开关.setText(显示);
    }

    private void 刷新喜从天降开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("喜从天降开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        喜从天降开关.setText(显示);
    }

    private void 刷新野外通缉开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("野外通缉开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        野外通缉开关.setText(显示);
    }

    private void 刷新神秘商人开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("神秘商人开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        神秘商人开关.setText(显示);
    }

    private void 刷新周末倍率开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("周末倍率开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        周末倍率开关.setText(显示);
    }

    private void 刷新魔族攻城开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("魔族攻城开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        魔族攻城开关.setText(显示);
    }

    private void 刷新幸运职业开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("幸运职业开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        幸运职业开关.setText(显示);
    }

    private void 刷新每日送货开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("每日送货开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        每日送货开关.setText(显示);
    }

    private void 刷新OX答题开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("OX答题开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        OX答题开关.setText(显示);
    }

    private void 刷新魔族突袭开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("魔族突袭开关");
        if (S <= 0) {
            显示 = "开启";
        } else {
            显示 = "关闭";
        }
        魔族突袭开关.setText(显示);
    }

    private void 刷新泡澡浴桶() {
        浴桶间隔.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子0") + "");
        浴桶经验1.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子1") + "");
        浴桶经验2.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子2") + "");
        浴桶经验3.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子3") + "");
        浴桶经验4.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子4") + "");
        浴桶经验5.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子5") + "");
        浴桶经验6.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子6") + "");
        浴桶经验7.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子7") + "");
        浴桶经验8.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子8") + "");
        浴桶经验9.setText("" + gui.Start.ConfigValuesMap.get("浴桶椅子9") + "");
    }

    private void 刷新财神椅子() {
        财神椅子金币上限.setText("" + gui.Start.ConfigValuesMap.get("财神椅子1") + "");
        财神椅子间隔.setText("" + gui.Start.ConfigValuesMap.get("财神椅子2") + "");
        财神椅子叠加.setText("" + gui.Start.ConfigValuesMap.get("财神椅子3") + "");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane3 = new javax.swing.JTabbedPane();
        活动事件 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        神秘商人出现时间 = new javax.swing.JTextField();
        jLabel343 = new javax.swing.JLabel();
        修改神秘商人 = new javax.swing.JButton();
        幸运职业代码 = new javax.swing.JTextField();
        jLabel344 = new javax.swing.JLabel();
        幸运职业修改 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        魔族攻城开关 = new javax.swing.JButton();
        jLabel264 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        OX答题开关 = new javax.swing.JButton();
        jLabel265 = new javax.swing.JLabel();
        jLabel266 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        每日送货开关 = new javax.swing.JButton();
        jLabel268 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        每日送货开关1 = new javax.swing.JButton();
        jLabel271 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        幸运职业开关 = new javax.swing.JButton();
        jLabel267 = new javax.swing.JLabel();
        jLabel269 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        魔族突袭开关 = new javax.swing.JButton();
        jLabel270 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        周末倍率开关 = new javax.swing.JButton();
        jLabel272 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        每日送货开关3 = new javax.swing.JButton();
        jLabel273 = new javax.swing.JLabel();
        jLabel263 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        神秘商人开关 = new javax.swing.JButton();
        jLabel274 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        野外通缉开关 = new javax.swing.JButton();
        jLabel275 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        喜从天降开关 = new javax.swing.JButton();
        jLabel276 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        鱼来鱼往开关 = new javax.swing.JButton();
        jLabel277 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        打折优惠开关 = new javax.swing.JButton();
        jLabel302 = new javax.swing.JLabel();
        椅子相关 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        财神椅子金币上限 = new javax.swing.JTextField();
        财神椅子间隔 = new javax.swing.JTextField();
        jLabel249 = new javax.swing.JLabel();
        财神椅子叠加 = new javax.swing.JTextField();
        jLabel251 = new javax.swing.JLabel();
        财神椅子修改 = new javax.swing.JButton();
        jLabel262 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        浴桶间隔 = new javax.swing.JTextField();
        jLabel252 = new javax.swing.JLabel();
        jLabel254 = new javax.swing.JLabel();
        财神椅子修改1 = new javax.swing.JButton();
        浴桶经验1 = new javax.swing.JTextField();
        浴桶经验2 = new javax.swing.JTextField();
        浴桶经验3 = new javax.swing.JTextField();
        浴桶经验4 = new javax.swing.JTextField();
        浴桶经验5 = new javax.swing.JTextField();
        浴桶经验6 = new javax.swing.JTextField();
        浴桶经验7 = new javax.swing.JTextField();
        浴桶经验8 = new javax.swing.JTextField();
        浴桶经验9 = new javax.swing.JTextField();
        jLabel253 = new javax.swing.JLabel();
        jLabel255 = new javax.swing.JLabel();
        jLabel256 = new javax.swing.JLabel();
        jLabel257 = new javax.swing.JLabel();
        jLabel258 = new javax.swing.JLabel();
        jLabel259 = new javax.swing.JLabel();
        jLabel260 = new javax.swing.JLabel();
        jLabel261 = new javax.swing.JLabel();
        jLabel250 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        枫叶纪念凳金币 = new javax.swing.JTextField();
        枫叶纪念凳间隔 = new javax.swing.JTextField();
        jLabel295 = new javax.swing.JLabel();
        枫叶纪念凳经验 = new javax.swing.JTextField();
        jLabel296 = new javax.swing.JLabel();
        财神椅子修改2 = new javax.swing.JButton();
        jLabel297 = new javax.swing.JLabel();
        枫叶纪念凳人数 = new javax.swing.JTextField();
        jLabel298 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel299 = new javax.swing.JLabel();
        枫叶纪念凳备注 = new javax.swing.JTextField();
        jLabel300 = new javax.swing.JLabel();
        泡澡浴桶备注 = new javax.swing.JTextField();
        jLabel301 = new javax.swing.JLabel();
        财神椅子备注 = new javax.swing.JTextField();
        备注修改 = new javax.swing.JButton();
        题库 = new javax.swing.JPanel();
        jScrollPane110 = new javax.swing.JScrollPane();
        OX答题题库 = new javax.swing.JTable();
        录入问题 = new javax.swing.JTextField();
        录入答案 = new javax.swing.JTextField();
        录入问题按钮 = new javax.swing.JButton();
        修改问题按钮 = new javax.swing.JButton();
        删除问题按钮 = new javax.swing.JButton();
        录入序号 = new javax.swing.JTextField();
        jLabel336 = new javax.swing.JLabel();
        jLabel340 = new javax.swing.JLabel();
        jLabel342 = new javax.swing.JLabel();
        jLabel350 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel327 = new javax.swing.JLabel();
        点券倍率 = new javax.swing.JTextField();
        jLabel328 = new javax.swing.JLabel();
        金币倍率 = new javax.swing.JTextField();
        经验倍率2 = new javax.swing.JLabel();
        经验倍率 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        飞鱼数量 = new javax.swing.JTextField();
        点券开关 = new javax.swing.JButton();
        金币开关 = new javax.swing.JButton();
        经验开关 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel278 = new javax.swing.JLabel();
        jLabel279 = new javax.swing.JLabel();
        jLabel280 = new javax.swing.JLabel();
        jLabel281 = new javax.swing.JLabel();
        jLabel282 = new javax.swing.JLabel();
        jLabel283 = new javax.swing.JLabel();
        jLabel284 = new javax.swing.JLabel();
        jLabel285 = new javax.swing.JLabel();
        jLabel286 = new javax.swing.JLabel();
        jLabel287 = new javax.swing.JLabel();
        jLabel288 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        响应人数显示 = new javax.swing.JTextField();
        jLabel345 = new javax.swing.JLabel();
        修改神秘商人1 = new javax.swing.JButton();
        jLabel289 = new javax.swing.JLabel();
        jLabel290 = new javax.swing.JLabel();
        jLabel291 = new javax.swing.JLabel();
        jLabel292 = new javax.swing.JLabel();
        jLabel293 = new javax.swing.JLabel();
        jLabel294 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        修改神秘商人2 = new javax.swing.JButton();
        响应人数显示1 = new javax.swing.JTextField();
        仙人模式1 = new javax.swing.JTextField();
        BOSS相关 = new javax.swing.JPanel();
        jScrollPane104 = new javax.swing.JScrollPane();
        野外BOSS刷新时间 = new javax.swing.JTable();
        刷新野外BOSS刷新时间 = new javax.swing.JButton();
        野外BOSS序号 = new javax.swing.JTextField();
        野外BOSS刷新时间值 = new javax.swing.JTextField();
        野外BOSS = new javax.swing.JTextField();
        刷新野外BOSS刷新时间修改 = new javax.swing.JButton();
        jLabel323 = new javax.swing.JLabel();
        jLabel324 = new javax.swing.JLabel();
        jLabel325 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane105 = new javax.swing.JScrollPane();
        BOSS经验限制 = new javax.swing.JTable();
        野外BOSS序号1 = new javax.swing.JTextField();
        BOSS限次 = new javax.swing.JTextField();
        BOSS = new javax.swing.JTextField();
        修改BOSS限次 = new javax.swing.JButton();
        刷新BOSS限次 = new javax.swing.JButton();
        jLabel326 = new javax.swing.JLabel();
        jLabel329 = new javax.swing.JLabel();
        jLabel330 = new javax.swing.JLabel();

        setResizable(false);

        jTabbedPane3.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N

        活动事件.setBackground(new java.awt.Color(255, 255, 255));
        活动事件.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextPane1.setEditable(false);
        jTextPane1.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jTextPane1.setForeground(new java.awt.Color(51, 0, 255));
        jTextPane1.setText("[每日送货] 每日 12:00 - 23:59\n[魔族偷袭] 每日 22:00 - 22:10\n[魔族攻城] 周日 21:00 - 21:30\n[每日答题] 周一至周五 20:10 - 20:20 周末 20:10 - 20:59\n[神秘商人] 完全随机出现，无规律\n[野外通缉] 系统发布一个后，玩家完成后 1 小时刷新\n[幸运职业] 11:00 23:00 随机抽取职业群，增加 50% 狩猎经验\n[周末狂欢] 周六，周日凌晨随机开启2倍经验，2倍爆率，2倍经验和爆率\n[喜从天降] 周日，22:30 会在 2 频道市场狂欢发放物品\n[鱼来鱼往] 周一至周五 21:30 - 21:40 周末 21:30 - 21:59 在水下世界举行\n");
        jTextPane1.setToolTipText("");
        jScrollPane1.setViewportView(jTextPane1);

        jPanel18.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 960, 540));
        jPanel18.add(神秘商人出现时间, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 50, 110, 30));

        jLabel343.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel343.setText("神秘商人；");
        jPanel18.add(jLabel343, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 20, -1, 20));

        修改神秘商人.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改神秘商人.setText("修改");
        修改神秘商人.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改神秘商人ActionPerformed(evt);
            }
        });
        jPanel18.add(修改神秘商人, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 50, 70, 30));
        jPanel18.add(幸运职业代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 130, 110, 30));

        jLabel344.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel344.setText("幸运职业；");
        jPanel18.add(jLabel344, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 100, -1, 20));

        幸运职业修改.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        幸运职业修改.setText("修改");
        幸运职业修改.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                幸运职业修改ActionPerformed(evt);
            }
        });
        jPanel18.add(幸运职业修改, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 130, 70, 30));

        jTabbedPane2.addTab("预览", jPanel18);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "魔族攻城", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        魔族攻城开关.setText("开关");
        魔族攻城开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                魔族攻城开关ActionPerformed(evt);
            }
        });
        jPanel4.add(魔族攻城开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel264.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel264.setForeground(new java.awt.Color(51, 102, 255));
        jLabel264.setText("开启后，周末晚上 21:10 之后魔族会进行攻城，从林中之城开始攻向明珠港，射手村，废弃都市，魔法密林。");
        jPanel4.add(jLabel264, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 960, 30));

        jPanel6.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 1190, 80));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "OX答题", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        OX答题开关.setText("开关");
        OX答题开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OX答题开关ActionPerformed(evt);
            }
        });
        jPanel5.add(OX答题开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel265.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel265.setForeground(new java.awt.Color(255, 0, 51));
        jPanel5.add(jLabel265, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 610, 30));

        jLabel266.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel266.setForeground(new java.awt.Color(51, 102, 255));
        jLabel266.setText("开启后，每日20:10后，会进行OX答题活动，周一至周五20分钟10题，周六至周日50分钟25题。");
        jPanel5.add(jLabel266, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 880, 30));

        jPanel6.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 1190, 80));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "每日送货", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        每日送货开关.setText("开关");
        每日送货开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                每日送货开关ActionPerformed(evt);
            }
        });
        jPanel7.add(每日送货开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel268.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel268.setForeground(new java.awt.Color(51, 102, 255));
        jLabel268.setText("开启后，每日12:00之后开始送货，从明珠港开始，到废弃都市结束。(npc\\9010009.js)");
        jPanel7.add(jLabel268, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 820, 30));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "每日送货", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        每日送货开关1.setText("开关");
        每日送货开关1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                每日送货开关1ActionPerformed(evt);
            }
        });
        jPanel10.add(每日送货开关1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 30, 130, -1));

        jLabel271.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel271.setForeground(new java.awt.Color(255, 0, 51));
        jLabel271.setText("开启后，每日12:00之后开始送货，从明珠港开始，到废弃都市结束。");
        jPanel10.add(jLabel271, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 820, 30));

        jPanel7.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 1190, 80));

        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 1190, 80));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "幸运职业", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        幸运职业开关.setText("开关");
        幸运职业开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                幸运职业开关ActionPerformed(evt);
            }
        });
        jPanel8.add(幸运职业开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel267.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel267.setForeground(new java.awt.Color(255, 0, 51));
        jPanel8.add(jLabel267, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 610, 30));

        jLabel269.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel269.setForeground(new java.awt.Color(51, 102, 255));
        jLabel269.setText("开启后，给予指定的职业增加50%的额外狩猎经验，每日 11:00 23:00 会随机重置指定的职业，二转后生效。");
        jPanel8.add(jLabel269, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 880, 30));

        jPanel6.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 1190, 80));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "魔族突袭", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        魔族突袭开关.setText("开关");
        魔族突袭开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                魔族突袭开关ActionPerformed(evt);
            }
        });
        jPanel9.add(魔族突袭开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel270.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel270.setForeground(new java.awt.Color(51, 102, 255));
        jLabel270.setText("开启后，每日22:00 - 22:10，蝙蝠魔会偷袭在野外的冒险家，高于30级，落单，弱小的玩家 。");
        jPanel9.add(jLabel270, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 960, 30));

        jPanel6.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 1190, 80));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "周末随机双倍活动", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        周末倍率开关.setText("开关");
        周末倍率开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                周末倍率开关ActionPerformed(evt);
            }
        });
        jPanel11.add(周末倍率开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel272.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel272.setForeground(new java.awt.Color(51, 102, 255));
        jLabel272.setText("开启后，周六，周日凌晨会随机开启24小时2倍经验，爆率，或者经验爆率活动。");
        jPanel11.add(jLabel272, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 820, 30));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "每日送货", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        每日送货开关3.setText("开关");
        每日送货开关3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                每日送货开关3ActionPerformed(evt);
            }
        });
        jPanel12.add(每日送货开关3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 30, 130, -1));

        jLabel273.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel273.setForeground(new java.awt.Color(255, 0, 51));
        jLabel273.setText("开启后，每日12:00之后开始送货，从明珠港开始，到废弃都市结束。");
        jPanel12.add(jLabel273, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 820, 30));

        jPanel11.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 1190, 80));

        jPanel6.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 1190, 80));

        jLabel263.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel263.setForeground(new java.awt.Color(255, 0, 51));
        jLabel263.setText("修改后实时生效，必须在活动时间开始前修改才能触发。");
        jPanel6.add(jLabel263, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 500, 30));

        jTabbedPane2.addTab("一", jPanel6);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "神秘商人", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        神秘商人开关.setText("开关");
        神秘商人开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                神秘商人开关ActionPerformed(evt);
            }
        });
        jPanel14.add(神秘商人开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel274.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel274.setForeground(new java.awt.Color(51, 102, 255));
        jLabel274.setText("开启后，服务端会开始召唤神秘商人，商人每次只会待 5 分钟，消失并告知下一次出现的信息。（npc\\9900001.js）");
        jPanel14.add(jLabel274, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 960, 30));

        jPanel13.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 1190, 80));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "野外通缉", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        野外通缉开关.setText("开关");
        野外通缉开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                野外通缉开关ActionPerformed(evt);
            }
        });
        jPanel15.add(野外通缉开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel275.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel275.setForeground(new java.awt.Color(51, 102, 255));
        jLabel275.setText("开启后，服务端会在启动后 1 小时候发布通缉令，随后通缉目标被击杀后会 1 小时再次发布通缉令。(活动奖励\\9000011_5.js)");
        jPanel15.add(jLabel275, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 1020, 30));

        jPanel13.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 1190, 80));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "喜从天降", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        喜从天降开关.setText("开关");
        喜从天降开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                喜从天降开关ActionPerformed(evt);
            }
        });
        jPanel16.add(喜从天降开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel276.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel276.setForeground(new java.awt.Color(51, 102, 255));
        jLabel276.setText("开启后，周六，周日晚上 22:30 分会在市场 2 频道，开启喜从天降活动。");
        jPanel16.add(jLabel276, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 960, 30));

        jPanel13.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, 1190, 80));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "鱼来鱼往", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        鱼来鱼往开关.setText("开关");
        鱼来鱼往开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                鱼来鱼往开关ActionPerformed(evt);
            }
        });
        jPanel17.add(鱼来鱼往开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel277.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel277.setForeground(new java.awt.Color(51, 102, 255));
        jLabel277.setText("开启后，每天晚上 21:30 开始，在 2 频道水下世界举行鱼来鱼往活动，相关面板可设置。");
        jPanel17.add(jLabel277, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 960, 30));

        jPanel13.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 1190, 80));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "打折优惠", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        打折优惠开关.setText("开关");
        打折优惠开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                打折优惠开关ActionPerformed(evt);
            }
        });
        jPanel27.add(打折优惠开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 30, 80, 30));

        jLabel302.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel302.setForeground(new java.awt.Color(51, 102, 255));
        jLabel302.setText("开启后，每天 11:11  23:11 游戏商城，游戏商店购买物品打5折。");
        jPanel27.add(jLabel302, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 960, 30));

        jPanel13.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 1190, 80));

        jTabbedPane2.addTab("二", jPanel13);

        活动事件.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 640));

        jTabbedPane3.addTab("事件相关", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 活动事件); // NOI18N

        椅子相关.setBackground(new java.awt.Color(255, 255, 255));
        椅子相关.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "财神椅子", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(财神椅子金币上限, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 120, 25));
        jPanel2.add(财神椅子间隔, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 120, 25));

        jLabel249.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel249.setText("每次叠加；");
        jPanel2.add(jLabel249, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, 30));
        jPanel2.add(财神椅子叠加, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 120, 25));

        jLabel251.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel251.setText("获取间隔/分；");
        jPanel2.add(jLabel251, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, 30));

        财神椅子修改.setText("修改");
        财神椅子修改.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                财神椅子修改ActionPerformed(evt);
            }
        });
        jPanel2.add(财神椅子修改, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 120, 25));
        财神椅子修改.getAccessibleContext().setAccessibleDescription("");

        jLabel262.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel262.setText("金币上限；");
        jPanel2.add(jLabel262, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 60, 220, 470));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "泡澡浴桶", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(浴桶间隔, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 120, 25));

        jLabel252.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel252.setText("61-80级；");
        jPanel3.add(jLabel252, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, 30));

        jLabel254.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel254.setText("获取间隔/分；");
        jPanel3.add(jLabel254, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, 30));

        财神椅子修改1.setText("修改");
        财神椅子修改1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                财神椅子修改1ActionPerformed(evt);
            }
        });
        jPanel3.add(财神椅子修改1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, 120, 25));
        jPanel3.add(浴桶经验1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 120, 25));
        jPanel3.add(浴桶经验2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 120, 25));
        jPanel3.add(浴桶经验3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 120, 25));
        jPanel3.add(浴桶经验4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 120, 25));
        jPanel3.add(浴桶经验5, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 120, 25));
        jPanel3.add(浴桶经验6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 120, 25));
        jPanel3.add(浴桶经验7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, 120, 25));
        jPanel3.add(浴桶经验8, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 120, 25));
        jPanel3.add(浴桶经验9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, 120, 25));

        jLabel253.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel253.setText("161+；");
        jPanel3.add(jLabel253, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, -1, 30));

        jLabel255.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel255.setText("21-40级；");
        jPanel3.add(jLabel255, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, 30));

        jLabel256.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel256.setText("41-60级；");
        jPanel3.add(jLabel256, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, -1, 30));

        jLabel257.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel257.setText("0-20级；");
        jPanel3.add(jLabel257, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, 30));

        jLabel258.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel258.setText("81-100级；");
        jPanel3.add(jLabel258, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, -1, 30));

        jLabel259.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel259.setText("101-120级；");
        jPanel3.add(jLabel259, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, -1, 30));

        jLabel260.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel260.setText("121-140级；");
        jPanel3.add(jLabel260, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, -1, 30));

        jLabel261.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel261.setText("141-160级；");
        jPanel3.add(jLabel261, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, -1, 30));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 340, 470));

        jLabel250.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel250.setForeground(new java.awt.Color(255, 0, 51));
        jLabel250.setText("修改后，重启才可以生效。");
        jPanel1.add(jLabel250, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 310, 30));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "枫叶纪念凳", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel23.add(枫叶纪念凳金币, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 120, 25));
        jPanel23.add(枫叶纪念凳间隔, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 120, 25));

        jLabel295.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel295.setText("经验；");
        jPanel23.add(jLabel295, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, 30));
        jPanel23.add(枫叶纪念凳经验, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 120, 25));

        jLabel296.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel296.setText("获取间隔/分；");
        jPanel23.add(jLabel296, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, -1, 30));

        财神椅子修改2.setText("修改");
        财神椅子修改2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                财神椅子修改2ActionPerformed(evt);
            }
        });
        jPanel23.add(财神椅子修改2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, 120, 25));

        jLabel297.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel297.setText("金币；");
        jPanel23.add(jLabel297, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, -1, 30));
        jPanel23.add(枫叶纪念凳人数, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 120, 25));

        jLabel298.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel298.setText("人数上限；");
        jPanel23.add(jLabel298, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, -1, 30));

        jPanel1.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 220, 470));

        jTabbedPane1.addTab("第一页", jPanel1);

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel299.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel299.setText("枫叶纪念凳备注；");
        jPanel24.add(jLabel299, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 30));
        jPanel24.add(枫叶纪念凳备注, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 310, 25));

        jLabel300.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel300.setText("泡澡浴桶备注；");
        jPanel24.add(jLabel300, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, 30));
        jPanel24.add(泡澡浴桶备注, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 310, 25));

        jLabel301.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel301.setText("财神椅子备注；");
        jPanel24.add(jLabel301, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, 30));
        jPanel24.add(财神椅子备注, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 310, 25));

        备注修改.setText("修改");
        备注修改.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                备注修改ActionPerformed(evt);
            }
        });
        jPanel24.add(备注修改, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 120, 25));

        jTabbedPane1.addTab("备注", jPanel24);

        椅子相关.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 650));

        jTabbedPane3.addTab("椅子相关", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 椅子相关); // NOI18N

        题库.setBackground(new java.awt.Color(255, 255, 255));
        题库.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        OX答题题库.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        OX答题题库.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "问题", "答案"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        OX答题题库.getTableHeader().setReorderingAllowed(false);
        jScrollPane110.setViewportView(OX答题题库);
        if (OX答题题库.getColumnModel().getColumnCount() > 0) {
            OX答题题库.getColumnModel().getColumn(0).setResizable(false);
            OX答题题库.getColumnModel().getColumn(1).setResizable(false);
            OX答题题库.getColumnModel().getColumn(1).setPreferredWidth(700);
            OX答题题库.getColumnModel().getColumn(2).setResizable(false);
            OX答题题库.getColumnModel().getColumn(2).setPreferredWidth(50);
        }

        题库.add(jScrollPane110, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 1220, 520));
        题库.add(录入问题, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 590, 810, 30));
        题库.add(录入答案, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 590, 60, 30));

        录入问题按钮.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        录入问题按钮.setText("录入");
        录入问题按钮.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                录入问题按钮ActionPerformed(evt);
            }
        });
        题库.add(录入问题按钮, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 590, 70, 30));

        修改问题按钮.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改问题按钮.setText("修改");
        修改问题按钮.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改问题按钮ActionPerformed(evt);
            }
        });
        题库.add(修改问题按钮, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 590, 70, 30));

        删除问题按钮.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除问题按钮.setText("删除");
        删除问题按钮.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除问题按钮ActionPerformed(evt);
            }
        });
        题库.add(删除问题按钮, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 590, 70, 30));

        录入序号.setEditable(false);
        题库.add(录入序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 590, 60, 30));

        jLabel336.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel336.setText("序号；");
        题库.add(jLabel336, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 570, -1, 20));

        jLabel340.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel340.setText("答案；O或者X");
        题库.add(jLabel340, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 570, -1, 20));

        jLabel342.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel342.setText("问题；");
        题库.add(jLabel342, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 570, -1, 20));

        jLabel350.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel350.setForeground(new java.awt.Color(255, 51, 0));
        jLabel350.setText("题目目录；   机器人录题格式；*录题<空格>[问题]<空格>[答案]     案例：*录题 冒险岛是不是休闲娱乐的游戏 O");
        题库.add(jLabel350, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 1220, 20));

        jTabbedPane3.addTab("题库", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 题库); // NOI18N

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel327.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel327.setText("点券倍率；");
        jPanel19.add(jLabel327, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 400, -1, 20));
        jPanel19.add(点券倍率, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 420, 120, 30));

        jLabel328.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel328.setText("金币倍率；");
        jPanel19.add(jLabel328, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, -1, 20));
        jPanel19.add(金币倍率, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 270, 120, 30));

        经验倍率2.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        经验倍率2.setText("经验倍率；");
        jPanel19.add(经验倍率2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, -1, 20));
        jPanel19.add(经验倍率, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 120, 30));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "飞鱼数量", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), java.awt.Color.black)); // NOI18N
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel20.add(飞鱼数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 120, 30));

        jPanel19.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 210, 170));

        点券开关.setText("开");
        点券开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                点券开关ActionPerformed(evt);
            }
        });
        jPanel19.add(点券开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 420, 80, 30));

        金币开关.setText("开");
        金币开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                金币开关ActionPerformed(evt);
            }
        });
        jPanel19.add(金币开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 80, 30));

        经验开关.setText("开");
        经验开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                经验开关ActionPerformed(evt);
            }
        });
        jPanel19.add(经验开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 350, 80, 30));

        jButton1.setText("修改");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel19.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 490, 100, 30));

        jLabel278.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel278.setForeground(new java.awt.Color(51, 51, 255));
        jLabel278.setText("躲避值 * 点券率");
        jPanel19.add(jLabel278, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 340, 190, 30));

        jLabel279.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel279.setText("设置飞鱼数量，来决定活动的难度。每次玩家获取奖励后会释放鱼。");
        jPanel19.add(jLabel279, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 760, 30));

        jLabel280.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel280.setText("活动开始后，玩家进入2频道水下世界，即为参加活动。");
        jPanel19.add(jLabel280, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 690, 30));

        jLabel281.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel281.setText("活动每 30 秒为间隔，为参加活动的玩家增加奖励。");
        jPanel19.add(jLabel281, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 140, 690, 30));

        jLabel282.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel282.setForeground(new java.awt.Color(255, 51, 51));
        jLabel282.setText("修改左边的相关属性，来决定获得的收益。");
        jPanel19.add(jLabel282, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 530, 690, 30));

        jLabel283.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel283.setText("奖励获取公式：");
        jPanel19.add(jLabel283, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 220, 190, 30));

        jLabel284.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel284.setForeground(new java.awt.Color(51, 51, 255));
        jLabel284.setText("躲避值 * 金币率");
        jPanel19.add(jLabel284, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 260, 190, 30));

        jLabel285.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel285.setForeground(new java.awt.Color(51, 51, 255));
        jLabel285.setText("躲避值 * 经验率");
        jPanel19.add(jLabel285, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 300, 190, 30));

        jLabel286.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel286.setText("玩家每 30 秒增加 1 躲避值，被怪物碰到就减少 1 点躲避值。");
        jPanel19.add(jLabel286, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, 690, 30));

        jLabel287.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel287.setText("离开水下世界地图，离开2频道视为放弃，重置躲避值。");
        jPanel19.add(jLabel287, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 390, 690, 30));

        jLabel288.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel288.setText("活动期间再次进入可再次加入。");
        jPanel19.add(jLabel288, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 430, 690, 30));

        jTabbedPane3.addTab("鱼来鱼往", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), jPanel19); // NOI18N

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "相关设置", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel22.add(响应人数显示, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 110, 30));

        jLabel345.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel345.setText("响应人数；");
        jPanel22.add(jLabel345, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, 20));

        修改神秘商人1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改神秘商人1.setText("修改");
        修改神秘商人1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改神秘商人1ActionPerformed(evt);
            }
        });
        jPanel22.add(修改神秘商人1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 110, 30));

        jPanel21.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 220, 190));

        jLabel289.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel289.setForeground(new java.awt.Color(255, 0, 0));
        jLabel289.setText("我要玩雪球赛");
        jPanel21.add(jLabel289, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 110, 150, 30));

        jLabel290.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel290.setText("周六和周日 早上10点之后开放。");
        jPanel21.add(jLabel290, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 250, 610, 30));

        jLabel291.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel291.setText("响应指令为；");
        jPanel21.add(jLabel291, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 110, 150, 30));

        jLabel292.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel292.setText("设置响应人数，当响应人数到达后就会开启活动。");
        jPanel21.add(jLabel292, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 80, 610, 30));

        jLabel293.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel293.setText("活动结束后冷却时间为 1 小时。");
        jPanel21.add(jLabel293, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 140, 610, 30));

        jLabel294.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel294.setText("周一至周五 晚上17点之后开放。");
        jPanel21.add(jLabel294, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 220, 610, 30));

        jTabbedPane3.addTab("自助活动", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), jPanel21); // NOI18N

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        修改神秘商人2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改神秘商人2.setText("修改");
        修改神秘商人2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改神秘商人2ActionPerformed(evt);
            }
        });
        jPanel25.add(修改神秘商人2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 110, 30));
        jPanel25.add(响应人数显示1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 110, 30));

        仙人模式1.setEditable(false);
        jPanel25.add(仙人模式1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 110, 30));

        jTabbedPane3.addTab("仙人模式", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), jPanel25); // NOI18N

        BOSS相关.setBackground(new java.awt.Color(255, 255, 255));
        BOSS相关.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "BOSS刷新时间", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        BOSS相关.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        野外BOSS刷新时间.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "野外BOSS", "刷新时间/分"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        野外BOSS刷新时间.getTableHeader().setReorderingAllowed(false);
        jScrollPane104.setViewportView(野外BOSS刷新时间);
        if (野外BOSS刷新时间.getColumnModel().getColumnCount() > 0) {
            野外BOSS刷新时间.getColumnModel().getColumn(0).setResizable(false);
            野外BOSS刷新时间.getColumnModel().getColumn(0).setPreferredWidth(50);
            野外BOSS刷新时间.getColumnModel().getColumn(1).setResizable(false);
            野外BOSS刷新时间.getColumnModel().getColumn(1).setPreferredWidth(170);
            野外BOSS刷新时间.getColumnModel().getColumn(2).setResizable(false);
            野外BOSS刷新时间.getColumnModel().getColumn(2).setPreferredWidth(170);
        }

        BOSS相关.add(jScrollPane104, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 430, 600));

        刷新野外BOSS刷新时间.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新野外BOSS刷新时间.setText("刷新");
        刷新野外BOSS刷新时间.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新野外BOSS刷新时间ActionPerformed(evt);
            }
        });
        BOSS相关.add(刷新野外BOSS刷新时间, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 390, 90, 30));

        野外BOSS序号.setEditable(false);
        BOSS相关.add(野外BOSS序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 310, 100, 30));
        BOSS相关.add(野外BOSS刷新时间值, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 310, 120, 30));

        野外BOSS.setEditable(false);
        BOSS相关.add(野外BOSS, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 310, 210, 30));

        刷新野外BOSS刷新时间修改.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新野外BOSS刷新时间修改.setText("修改");
        刷新野外BOSS刷新时间修改.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新野外BOSS刷新时间修改ActionPerformed(evt);
            }
        });
        BOSS相关.add(刷新野外BOSS刷新时间修改, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 390, 90, 30));

        jLabel323.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel323.setText("刷新时间；");
        BOSS相关.add(jLabel323, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 290, -1, -1));

        jLabel324.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel324.setText("序号；");
        BOSS相关.add(jLabel324, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 290, -1, -1));

        jLabel325.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel325.setText("BOSS；");
        BOSS相关.add(jLabel325, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 290, -1, -1));

        jTabbedPane3.addTab("BOSS刷新调整", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), BOSS相关); // NOI18N

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BOSS经验限制.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "BOSS", "每日获取经验次数"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        BOSS经验限制.getTableHeader().setReorderingAllowed(false);
        jScrollPane105.setViewportView(BOSS经验限制);
        if (BOSS经验限制.getColumnModel().getColumnCount() > 0) {
            BOSS经验限制.getColumnModel().getColumn(0).setResizable(false);
            BOSS经验限制.getColumnModel().getColumn(0).setPreferredWidth(50);
            BOSS经验限制.getColumnModel().getColumn(1).setResizable(false);
            BOSS经验限制.getColumnModel().getColumn(1).setPreferredWidth(170);
            BOSS经验限制.getColumnModel().getColumn(2).setResizable(false);
            BOSS经验限制.getColumnModel().getColumn(2).setPreferredWidth(170);
        }

        jPanel26.add(jScrollPane105, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 430, 600));

        野外BOSS序号1.setEditable(false);
        jPanel26.add(野外BOSS序号1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 310, 100, 30));
        jPanel26.add(BOSS限次, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 310, 120, 30));

        BOSS.setEditable(false);
        jPanel26.add(BOSS, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 310, 210, 30));

        修改BOSS限次.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改BOSS限次.setText("修改");
        修改BOSS限次.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改BOSS限次ActionPerformed(evt);
            }
        });
        jPanel26.add(修改BOSS限次, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 390, 90, 30));

        刷新BOSS限次.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新BOSS限次.setText("刷新");
        刷新BOSS限次.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新BOSS限次ActionPerformed(evt);
            }
        });
        jPanel26.add(刷新BOSS限次, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 390, 90, 30));

        jLabel326.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel326.setText("BOSS；");
        jPanel26.add(jLabel326, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 290, -1, -1));

        jLabel329.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel329.setText("序号；");
        jPanel26.add(jLabel329, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 290, -1, -1));

        jLabel330.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel330.setText("限次；");
        jPanel26.add(jLabel330, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 290, -1, -1));

        jTabbedPane3.addTab("BOSS经验限制", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), jPanel26); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1260, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void 刷新野外BOSS刷新时间ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新野外BOSS刷新时间ActionPerformed
        刷新野外BOSS刷新时间();
    }//GEN-LAST:event_刷新野外BOSS刷新时间ActionPerformed
    public void 刷新野外BOSS刷新时间() {
        for (int i = this.野外BOSS刷新时间.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.野外BOSS刷新时间.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id >= 10000 && id < 20000");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 野外BOSS刷新时间.getModel()).insertRow(野外BOSS刷新时间.getRowCount(), new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("Val")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        野外BOSS刷新时间.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 野外BOSS刷新时间.getSelectedRow();
                String a = 野外BOSS刷新时间.getValueAt(i, 0).toString();
                String a1 = 野外BOSS刷新时间.getValueAt(i, 1).toString();
                String a2 = 野外BOSS刷新时间.getValueAt(i, 2).toString();
                野外BOSS序号.setText(a);
                野外BOSS.setText(a1);
                野外BOSS刷新时间值.setText(a2);
            }
        });
    }
    private void 刷新野外BOSS刷新时间修改ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新野外BOSS刷新时间修改ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.野外BOSS刷新时间值.getText().matches("[0-9]+");

        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");

                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");

                ps1.setInt(1, Integer.parseInt(this.野外BOSS序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + this.野外BOSS刷新时间值.getText() + "' where id= " + this.野外BOSS序号.getText() + ";";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    刷新野外BOSS刷新时间();
                    gui.Start.GetConfigValues();
                    JOptionPane.showMessageDialog(null, "修改成功，已经生效");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台3号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要修改的刷新时间");
        }
    }//GEN-LAST:event_刷新野外BOSS刷新时间修改ActionPerformed

    private void 财神椅子修改ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_财神椅子修改ActionPerformed
        boolean result1 = this.财神椅子金币上限.getText().matches("[0-9]+");
        boolean result2 = this.财神椅子间隔.getText().matches("[0-9]+");
        boolean result3 = this.财神椅子叠加.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result1 && result2 && result3) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 2302);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.财神椅子金币上限.getText() + "' where id = 210000;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);

                    String sqlString3 = null;
                    sqlString3 = "update configvalues set Val='" + this.财神椅子间隔.getText() + "' where id = 210001;";
                    PreparedStatement dropperid3 = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    dropperid3.executeUpdate(sqlString3);

                    String sqlString4 = null;
                    sqlString4 = "update configvalues set Val='" + this.财神椅子叠加.getText() + "' where id = 210002;";
                    PreparedStatement dropperid4 = DatabaseConnection.getConnection().prepareStatement(sqlString4);
                    dropperid4.executeUpdate(sqlString4);
                    gui.Start.GetConfigValues();
                    刷新财神椅子();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_财神椅子修改ActionPerformed

    private void 财神椅子修改1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_财神椅子修改1ActionPerformed
        boolean result0 = this.浴桶间隔.getText().matches("[0-9]+");
        boolean result1 = this.浴桶经验1.getText().matches("[0-9]+");
        boolean result2 = this.浴桶经验2.getText().matches("[0-9]+");
        boolean result3 = this.浴桶经验3.getText().matches("[0-9]+");
        boolean result4 = this.浴桶经验4.getText().matches("[0-9]+");
        boolean result5 = this.浴桶经验5.getText().matches("[0-9]+");
        boolean result6 = this.浴桶经验6.getText().matches("[0-9]+");
        boolean result7 = this.浴桶经验7.getText().matches("[0-9]+");
        boolean result8 = this.浴桶经验8.getText().matches("[0-9]+");
        boolean result9 = this.浴桶经验9.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result0 && result1 && result2 && result3 && result4 && result5 && result6 && result7 && result8 && result9) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 2302);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString0 = null;
                    sqlString0 = "update configvalues set Val='" + this.浴桶间隔.getText() + "' where id = 210010;";
                    PreparedStatement dropperid0 = DatabaseConnection.getConnection().prepareStatement(sqlString0);
                    dropperid0.executeUpdate(sqlString0);

                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val='" + this.浴桶经验1.getText() + "' where id = 210011;";
                    PreparedStatement dropperid1 = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    dropperid1.executeUpdate(sqlString1);

                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.浴桶经验2.getText() + "' where id = 210012;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);

                    String sqlString3 = null;
                    sqlString3 = "update configvalues set Val='" + this.浴桶经验3.getText() + "' where id = 210013;";
                    PreparedStatement dropperid3 = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    dropperid3.executeUpdate(sqlString3);

                    String sqlString4 = null;
                    sqlString4 = "update configvalues set Val='" + this.浴桶经验4.getText() + "' where id = 210014;";
                    PreparedStatement dropperid4 = DatabaseConnection.getConnection().prepareStatement(sqlString4);
                    dropperid4.executeUpdate(sqlString4);

                    String sqlString5 = null;
                    sqlString5 = "update configvalues set Val='" + this.浴桶经验5.getText() + "' where id = 210015;";
                    PreparedStatement dropperid5 = DatabaseConnection.getConnection().prepareStatement(sqlString5);
                    dropperid5.executeUpdate(sqlString5);

                    String sqlString6 = null;
                    sqlString6 = "update configvalues set Val='" + this.浴桶经验6.getText() + "' where id = 210016;";
                    PreparedStatement dropperid6 = DatabaseConnection.getConnection().prepareStatement(sqlString6);
                    dropperid6.executeUpdate(sqlString6);

                    String sqlString7 = null;
                    sqlString7 = "update configvalues set Val='" + this.浴桶经验7.getText() + "' where id = 210017;";
                    PreparedStatement dropperid7 = DatabaseConnection.getConnection().prepareStatement(sqlString7);
                    dropperid7.executeUpdate(sqlString7);

                    String sqlString8 = null;
                    sqlString8 = "update configvalues set Val='" + this.浴桶经验8.getText() + "' where id = 210018;";
                    PreparedStatement dropperid8 = DatabaseConnection.getConnection().prepareStatement(sqlString8);
                    dropperid8.executeUpdate(sqlString8);

                    String sqlString9 = null;
                    sqlString9 = "update configvalues set Val='" + this.浴桶经验9.getText() + "' where id = 210019;";
                    PreparedStatement dropperid9 = DatabaseConnection.getConnection().prepareStatement(sqlString9);
                    dropperid9.executeUpdate(sqlString9);

                    gui.Start.GetConfigValues();
                    刷新泡澡浴桶();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_财神椅子修改1ActionPerformed

    private void 魔族攻城开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_魔族攻城开关ActionPerformed
        按键开关("魔族攻城开关", 2404);
        刷新魔族攻城开关();
    }//GEN-LAST:event_魔族攻城开关ActionPerformed

    private void OX答题开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OX答题开关ActionPerformed
        按键开关("OX答题开关", 2401);
        刷新OX答题开关();
    }//GEN-LAST:event_OX答题开关ActionPerformed

    private void 每日送货开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_每日送货开关ActionPerformed
        按键开关("每日送货开关", 2402);
        刷新每日送货开关();
    }//GEN-LAST:event_每日送货开关ActionPerformed

    private void 幸运职业开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_幸运职业开关ActionPerformed
        按键开关("幸运职业开关", 749);
        刷新幸运职业开关();
    }//GEN-LAST:event_幸运职业开关ActionPerformed

    private void 魔族突袭开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_魔族突袭开关ActionPerformed
        按键开关("魔族突袭开关", 2400);
        刷新魔族突袭开关();
    }//GEN-LAST:event_魔族突袭开关ActionPerformed

    private void 每日送货开关1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_每日送货开关1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_每日送货开关1ActionPerformed

    private void 周末倍率开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_周末倍率开关ActionPerformed
        按键开关("周末倍率开关", 2405);
        刷新周末倍率开关();
    }//GEN-LAST:event_周末倍率开关ActionPerformed

    private void 每日送货开关3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_每日送货开关3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_每日送货开关3ActionPerformed

    private void 神秘商人开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_神秘商人开关ActionPerformed
        按键开关("神秘商人开关", 2406);
        刷新神秘商人开关();
    }//GEN-LAST:event_神秘商人开关ActionPerformed

    private void 野外通缉开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_野外通缉开关ActionPerformed
        按键开关("野外通缉开关", 2407);
        刷新野外通缉开关();
    }//GEN-LAST:event_野外通缉开关ActionPerformed

    private void 喜从天降开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_喜从天降开关ActionPerformed
        按键开关("喜从天降开关", 2408);
        刷新喜从天降开关();
    }//GEN-LAST:event_喜从天降开关ActionPerformed

    private void 录入问题按钮ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_录入问题按钮ActionPerformed
        if (录入问题.getText().equals("") || 录入答案.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "问题和答案不能为空");
            return;
        }
        if (!录入答案.getText().equals("O") && !录入答案.getText().equals("X")) {
            JOptionPane.showMessageDialog(null, "答案只能大写的 O 或者 X");
            return;
        }
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO oxdt (b, c) VALUES (?, ?)")) {
            ps.setString(1, 录入问题.getText());
            ps.setString(2, 录入答案.getText());
            ps.executeUpdate();
            刷新题库();

        } catch (SQLException ex) {

        }
    }//GEN-LAST:event_录入问题按钮ActionPerformed

    private void 修改问题按钮ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改问题按钮ActionPerformed
        if (!录入答案.getText().equals("O") && !录入答案.getText().equals("X")) {
            JOptionPane.showMessageDialog(null, "答案只能大写的 O 或者 X");
            return;
        }
        boolean result1 = this.录入序号.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE oxdt SET b = ?, c = ? WHERE a = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM oxdt WHERE a = ?");
                ps1.setInt(1, Integer.parseInt(this.录入序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    String sqlString3 = null;
                    sqlString2 = "update oxdt set b='" + this.录入问题.getText() + "' where a=" + this.录入序号.getText() + ";";
                    PreparedStatement priority = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    priority.executeUpdate(sqlString2);
                    sqlString3 = "update oxdt set c='" + this.录入答案.getText() + "' where a=" + this.录入序号.getText() + ";";
                    PreparedStatement period = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    period.executeUpdate(sqlString3);
                    刷新题库();

                }
            } catch (SQLException ex) {
            }
        } else {
            JOptionPane.showMessageDialog(null, "请点击你需要修改的问题");
        }
    }//GEN-LAST:event_修改问题按钮ActionPerformed

    private void 删除问题按钮ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除问题按钮ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.录入序号.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM oxdt WHERE a = ?");
                ps1.setInt(1, Integer.parseInt(this.录入序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from oxdt where a =" + Integer.parseInt(this.录入序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新题库();

                }
            } catch (SQLException ex) {
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的问题 ");
        }
    }//GEN-LAST:event_删除问题按钮ActionPerformed

    private void 鱼来鱼往开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_鱼来鱼往开关ActionPerformed
        按键开关("鱼来鱼往开关", 2409);
        刷新鱼来鱼往开关();
    }//GEN-LAST:event_鱼来鱼往开关ActionPerformed

    private void 金币开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_金币开关ActionPerformed
        按键开关("飞鱼金币开关", 4002);
        刷新飞鱼金币开关();
    }//GEN-LAST:event_金币开关ActionPerformed

    private void 经验开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_经验开关ActionPerformed
        按键开关("飞鱼经验开关", 4001);
        刷新飞鱼经验开关();
    }//GEN-LAST:event_经验开关ActionPerformed

    private void 点券开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_点券开关ActionPerformed
        按键开关("飞鱼点券开关", 4003);
        刷新飞鱼点券开关();
    }//GEN-LAST:event_点券开关ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = 飞鱼数量.getText().matches("[0-9]+");
        boolean result2 = 金币倍率.getText().matches("[0-9]+");
        boolean result3 = 经验倍率.getText().matches("[0-9]+");
        boolean result4 = 点券倍率.getText().matches("[0-9]+");
        if (result1 && result2 && result3 && result4) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues ");
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + 飞鱼数量.getText() + "' where id = 4010;";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);

                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val = '" + 经验倍率.getText() + "' where id = 4011;";
                    PreparedStatement Val2 = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    Val2.executeUpdate(sqlString2);

                    String sqlString3 = null;
                    sqlString3 = "update configvalues set Val = '" + 金币倍率.getText() + "' where id = 4012;";
                    PreparedStatement Val3 = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    Val3.executeUpdate(sqlString3);

                    String sqlString4 = null;
                    sqlString4 = "update configvalues set Val = '" + 点券倍率.getText() + "' where id = 4013;";
                    PreparedStatement Val4 = DatabaseConnection.getConnection().prepareStatement(sqlString4);
                    Val4.executeUpdate(sqlString4);
                    gui.Start.GetConfigValues();
                    刷新飞鱼设置();
                    JOptionPane.showMessageDialog(null, "修改成功，已经生效");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台3号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "？？？？？？？");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void 修改神秘商人ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改神秘商人ActionPerformed
        boolean result1 = 神秘商人出现时间.getText().matches("[0-9]+");
        if (result1) {
            MapleParty.神秘商人时间 = Integer.parseInt(神秘商人出现时间.getText());
            刷新神秘商人时间();
            JOptionPane.showMessageDialog(null, "修改成功，神秘商人出现时间变更为 " + 神秘商人出现时间.getText());
        }
    }//GEN-LAST:event_修改神秘商人ActionPerformed

    private void 幸运职业修改ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_幸运职业修改ActionPerformed
        boolean result1 = 幸运职业代码.getText().matches("[0-9]+");
        if (result1) {
            MapleParty.幸运职业 = Integer.parseInt(幸运职业代码.getText());
            刷新幸运职业();
            JOptionPane.showMessageDialog(null, "修改成功，幸运职业变更为 " + 幸运职业代码.getText());
        }
    }//GEN-LAST:event_幸运职业修改ActionPerformed

    private void 修改神秘商人1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改神秘商人1ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = 响应人数显示.getText().matches("[0-9]+");

        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues ");
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + 响应人数显示.getText() + "' where id = 4100;";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    JOptionPane.showMessageDialog(null, "修改成功，已经生效");
                    gui.Start.GetConfigValues();
                    刷新响应人数();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台3号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "？？？？？？？");
        }
    }//GEN-LAST:event_修改神秘商人1ActionPerformed

    private void 财神椅子修改2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_财神椅子修改2ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = 枫叶纪念凳金币.getText().matches("[0-9]+");
        boolean result2 = 枫叶纪念凳经验.getText().matches("[0-9]+");
        boolean result3 = 枫叶纪念凳人数.getText().matches("[0-9]+");
        boolean result4 = 枫叶纪念凳间隔.getText().matches("[0-9]+");
        if (result1 && result2 && result3 && result4) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues ");
                rs = ps.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + 枫叶纪念凳金币.getText() + "' where id = 210020;";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);

                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val = '" + 枫叶纪念凳经验.getText() + "' where id = 210021;";
                    PreparedStatement Val2 = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    Val2.executeUpdate(sqlString2);

                    String sqlString3 = null;
                    sqlString3 = "update configvalues set Val = '" + 枫叶纪念凳人数.getText() + "' where id = 210022;";
                    PreparedStatement Val3 = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    Val3.executeUpdate(sqlString3);

                    String sqlString4 = null;
                    sqlString4 = "update configvalues set Val = '" + 枫叶纪念凳间隔.getText() + "' where id = 210023;";
                    PreparedStatement Val4 = DatabaseConnection.getConnection().prepareStatement(sqlString4);
                    Val4.executeUpdate(sqlString4);
                    gui.Start.GetConfigValues();
                    刷新飞鱼设置();
                    JOptionPane.showMessageDialog(null, "修改成功，已经生效");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台3号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "？？？？？？？");
        }
    }//GEN-LAST:event_财神椅子修改2ActionPerformed

    private void 备注修改ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_备注修改ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM qqstem ");
            rs = ps.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update qqstem set Name = '" + 枫叶纪念凳备注.getText() + "' where channel = 3010025;";
                PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                Val.executeUpdate(sqlString1);

                String sqlString2 = null;
                sqlString2 = "update qqstem set Name = '" + 泡澡浴桶备注.getText() + "' where channel = 3012002;";
                PreparedStatement Val2 = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                Val2.executeUpdate(sqlString2);

                String sqlString3 = null;
                sqlString3 = "update qqstem set Name = '" + 财神椅子备注.getText() + "' where channel = 3010100;";
                PreparedStatement Val3 = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                Val3.executeUpdate(sqlString3);
                刷新椅子备注();
                JOptionPane.showMessageDialog(null, "修改成功，已经生效");
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台3号.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_备注修改ActionPerformed

    private void 修改神秘商人2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改神秘商人2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_修改神秘商人2ActionPerformed

    private void 修改BOSS限次ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改BOSS限次ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_修改BOSS限次ActionPerformed

    private void 刷新BOSS限次ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新BOSS限次ActionPerformed
        刷新BOSS限次();
    }//GEN-LAST:event_刷新BOSS限次ActionPerformed

    private void 打折优惠开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_打折优惠开关ActionPerformed
        按键开关("打折优惠开关", 2410);
        刷新打折优惠开关();
    }//GEN-LAST:event_打折优惠开关ActionPerformed
    public void 刷新BOSS限次() {
        for (int i = this.BOSS经验限制.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.BOSS经验限制.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id >= 15000 && id < 16000");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) BOSS经验限制.getModel()).insertRow(BOSS经验限制.getRowCount(), new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("Val")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        BOSS经验限制.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = BOSS经验限制.getSelectedRow();
                String a = BOSS经验限制.getValueAt(i, 0).toString();
                String a1 = BOSS经验限制.getValueAt(i, 1).toString();
                String a2 = BOSS经验限制.getValueAt(i, 2).toString();
                野外BOSS序号1.setText(a);
                BOSS.setText(a1);
                BOSS限次.setText(a2);
            }
        });

    }

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
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        gui.Start.GetConfigValues();
    }

    public void Gainqqstem(String Name, int Channale, int Piot) {
        try {
            int ret = Getqqstem(Name, Channale);
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO qqstem (channel, Name,Point) VALUES (?, ?, ?)");
                    ps.setInt(1, Channale);
                    ps.setString(2, Name);
                    ps.setInt(3, ret);
                    ps.execute();
                } catch (SQLException e) {
                    System.out.println("xxxxxxxx:" + e);
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("xxxxxxxxzzzzzzz:" + e);
                    }
                }
            }
            ret += Piot;
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE qqstem SET `Point` = ? WHERE Name = ? and channel = ?");
            ps.setInt(1, ret);
            ps.setString(2, Name);
            ps.setInt(3, Channale);
            ps.execute();
            ps.close();
        } catch (SQLException sql) {
            System.err.println("Getqqstem!!55" + sql);
        }
    }

    public int Getqqstem(String Name, int Channale) {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM qqstem WHERE channel = ? and Name = ?");
            ps.setInt(1, Channale);
            ps.setString(2, Name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            ret = rs.getInt("Point");
            rs.close();
            ps.close();
        } catch (SQLException ex) {
        }
        return ret;
    }

    public static String 取椅子备注(int a) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM qqstem");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("channel") == a) {
                    data = rs.getString("Name");
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public void 刷新题库() {
        for (int i = this.OX答题题库.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.OX答题题库.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM oxdt order by a desc");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) OX答题题库.getModel()).insertRow(OX答题题库.getRowCount(), new Object[]{rs.getInt("a"), rs.getString("b"), rs.getString("c")});

            }
        } catch (SQLException ex) {
            Logger.getLogger(活动控制台.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        OX答题题库.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = OX答题题库.getSelectedRow();
                String a = OX答题题库.getValueAt(i, 0).toString();
                String a1 = OX答题题库.getValueAt(i, 1).toString();
                String a2 = OX答题题库.getValueAt(i, 2).toString();
                录入序号.setText(a);
                录入问题.setText(a1);
                录入答案.setText(a2);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(控制台3号.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(控制台3号.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(控制台3号.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(控制台3号.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        控制台3号.setDefaultLookAndFeelDecorated(true);
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
                new 控制台3号().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BOSS;
    private javax.swing.JPanel BOSS相关;
    private javax.swing.JTable BOSS经验限制;
    private javax.swing.JTextField BOSS限次;
    private javax.swing.JButton OX答题开关;
    private javax.swing.JTable OX答题题库;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel249;
    private javax.swing.JLabel jLabel250;
    private javax.swing.JLabel jLabel251;
    private javax.swing.JLabel jLabel252;
    private javax.swing.JLabel jLabel253;
    private javax.swing.JLabel jLabel254;
    private javax.swing.JLabel jLabel255;
    private javax.swing.JLabel jLabel256;
    private javax.swing.JLabel jLabel257;
    private javax.swing.JLabel jLabel258;
    private javax.swing.JLabel jLabel259;
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
    private javax.swing.JLabel jLabel276;
    private javax.swing.JLabel jLabel277;
    private javax.swing.JLabel jLabel278;
    private javax.swing.JLabel jLabel279;
    private javax.swing.JLabel jLabel280;
    private javax.swing.JLabel jLabel281;
    private javax.swing.JLabel jLabel282;
    private javax.swing.JLabel jLabel283;
    private javax.swing.JLabel jLabel284;
    private javax.swing.JLabel jLabel285;
    private javax.swing.JLabel jLabel286;
    private javax.swing.JLabel jLabel287;
    private javax.swing.JLabel jLabel288;
    private javax.swing.JLabel jLabel289;
    private javax.swing.JLabel jLabel290;
    private javax.swing.JLabel jLabel291;
    private javax.swing.JLabel jLabel292;
    private javax.swing.JLabel jLabel293;
    private javax.swing.JLabel jLabel294;
    private javax.swing.JLabel jLabel295;
    private javax.swing.JLabel jLabel296;
    private javax.swing.JLabel jLabel297;
    private javax.swing.JLabel jLabel298;
    private javax.swing.JLabel jLabel299;
    private javax.swing.JLabel jLabel300;
    private javax.swing.JLabel jLabel301;
    private javax.swing.JLabel jLabel302;
    private javax.swing.JLabel jLabel323;
    private javax.swing.JLabel jLabel324;
    private javax.swing.JLabel jLabel325;
    private javax.swing.JLabel jLabel326;
    private javax.swing.JLabel jLabel327;
    private javax.swing.JLabel jLabel328;
    private javax.swing.JLabel jLabel329;
    private javax.swing.JLabel jLabel330;
    private javax.swing.JLabel jLabel336;
    private javax.swing.JLabel jLabel340;
    private javax.swing.JLabel jLabel342;
    private javax.swing.JLabel jLabel343;
    private javax.swing.JLabel jLabel344;
    private javax.swing.JLabel jLabel345;
    private javax.swing.JLabel jLabel350;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane104;
    private javax.swing.JScrollPane jScrollPane105;
    private javax.swing.JScrollPane jScrollPane110;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextField 仙人模式1;
    private javax.swing.JButton 修改BOSS限次;
    private javax.swing.JButton 修改神秘商人;
    private javax.swing.JButton 修改神秘商人1;
    private javax.swing.JButton 修改神秘商人2;
    private javax.swing.JButton 修改问题按钮;
    private javax.swing.JButton 删除问题按钮;
    private javax.swing.JButton 刷新BOSS限次;
    private javax.swing.JButton 刷新野外BOSS刷新时间;
    private javax.swing.JButton 刷新野外BOSS刷新时间修改;
    private javax.swing.JButton 周末倍率开关;
    private javax.swing.JTextField 响应人数显示;
    private javax.swing.JTextField 响应人数显示1;
    private javax.swing.JButton 喜从天降开关;
    private javax.swing.JButton 备注修改;
    private javax.swing.JTextField 幸运职业代码;
    private javax.swing.JButton 幸运职业修改;
    private javax.swing.JButton 幸运职业开关;
    private javax.swing.JTextField 录入序号;
    private javax.swing.JTextField 录入答案;
    private javax.swing.JTextField 录入问题;
    private javax.swing.JButton 录入问题按钮;
    private javax.swing.JButton 打折优惠开关;
    private javax.swing.JTextField 枫叶纪念凳人数;
    private javax.swing.JTextField 枫叶纪念凳备注;
    private javax.swing.JTextField 枫叶纪念凳经验;
    private javax.swing.JTextField 枫叶纪念凳金币;
    private javax.swing.JTextField 枫叶纪念凳间隔;
    private javax.swing.JPanel 椅子相关;
    private javax.swing.JButton 每日送货开关;
    private javax.swing.JButton 每日送货开关1;
    private javax.swing.JButton 每日送货开关3;
    private javax.swing.JTextField 泡澡浴桶备注;
    private javax.swing.JPanel 活动事件;
    private javax.swing.JTextField 浴桶经验1;
    private javax.swing.JTextField 浴桶经验2;
    private javax.swing.JTextField 浴桶经验3;
    private javax.swing.JTextField 浴桶经验4;
    private javax.swing.JTextField 浴桶经验5;
    private javax.swing.JTextField 浴桶经验6;
    private javax.swing.JTextField 浴桶经验7;
    private javax.swing.JTextField 浴桶经验8;
    private javax.swing.JTextField 浴桶经验9;
    private javax.swing.JTextField 浴桶间隔;
    private javax.swing.JTextField 点券倍率;
    private javax.swing.JButton 点券开关;
    private javax.swing.JTextField 神秘商人出现时间;
    private javax.swing.JButton 神秘商人开关;
    private javax.swing.JTextField 经验倍率;
    private javax.swing.JLabel 经验倍率2;
    private javax.swing.JButton 经验开关;
    private javax.swing.JButton 财神椅子修改;
    private javax.swing.JButton 财神椅子修改1;
    private javax.swing.JButton 财神椅子修改2;
    private javax.swing.JTextField 财神椅子叠加;
    private javax.swing.JTextField 财神椅子备注;
    private javax.swing.JTextField 财神椅子金币上限;
    private javax.swing.JTextField 财神椅子间隔;
    private javax.swing.JTextField 野外BOSS;
    private javax.swing.JTable 野外BOSS刷新时间;
    private javax.swing.JTextField 野外BOSS刷新时间值;
    private javax.swing.JTextField 野外BOSS序号;
    private javax.swing.JTextField 野外BOSS序号1;
    private javax.swing.JButton 野外通缉开关;
    private javax.swing.JTextField 金币倍率;
    private javax.swing.JButton 金币开关;
    private javax.swing.JPanel 题库;
    private javax.swing.JTextField 飞鱼数量;
    private javax.swing.JButton 魔族攻城开关;
    private javax.swing.JButton 魔族突袭开关;
    private javax.swing.JButton 鱼来鱼往开关;
    // End of variables declaration//GEN-END:variables

}
