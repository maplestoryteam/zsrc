package gui.控制台;

import client.MapleCharacterUtil;
import database.DatabaseConnection;
import gui.ZEVMS;
import gui.ZEVMS2;
import handling.world.World;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.NPCConversationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static abc.Game.窗口标题;
import static gui.Start.ConfigValuesMap;
import static gui.Start.读取技能范围检测;
import static tools.FileoutputUtil.CurrentReadable_Time;

/**
 *
 * @author Administrator
 */
public class 控制台1号 extends javax.swing.JFrame {

    /**
     * Creates new form 测试
     */
    public 控制台1号() {
        setTitle("" + 窗口标题 + "【 1 号控制台，可关闭】");
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        GetConfigValues();
        initComponents();
        刷新技能值列表();
        刷新账号注册();
        刷新升级快讯();
        刷新游戏找人开关();
        刷新冒险家职业开关();
        刷新战神职业开关();
        刷新骑士团职业开关();
        刷新管理隐身开关();
        刷新管理加速开关();
        刷新游戏指令开关();
        刷新游戏喇叭开关();
        刷新丢出金币开关();
        刷新丢出物品开关();
        刷新玩家交易开关();
        刷新禁止登陆开关();
        刷新全服决斗开关();
        刷新欢迎弹窗开关();
        刷新屠令广播开关();
        刷新游戏仓库开关();
        刷新登陆验证开关();
        刷新随身仓库开关();
        刷新聊天记录开关();
        刷新拍卖行开关();
        刷新下雪天开关();
        刷新下雪花开关();
        刷新下气泡开关();
        刷新下枫叶开关();
        刷新注册账号数量();
        刷新冒险家等级上限();
        刷新骑士团等级上限();
        刷新创建角色数量();
        刷新下红花开关();
        刷新金锤子();
        刷新金锤子上限();
        刷新脚本显码开关();
        刷新雇佣商人开关();
        刷新上线提醒开关();
        刷新巅峰广播开关();
        刷新玩家聊天开关();
        刷新滚动公告开关();
        刷新指令通知开关();
        刷新回收地图开关();
        刷新登陆记录开关();
        刷新经验加成表();
        刷新蓝蜗牛开关();
        刷新蘑菇仔开关();
        刷新绿水灵开关();
        刷新漂漂猪开关();
        刷新小青蛇开关();
        刷新红螃蟹开关();
        刷新大海龟开关();
        刷新章鱼怪开关();
        刷新顽皮猴开关();
        刷新星精灵开关();
        刷新胖企鹅开关();
        刷新白雪人开关();
        刷新石头人开关();
        刷新紫色猫开关();
        刷新大灰狼开关();
        刷新小白兔开关();
        刷新喷火龙开关();
        刷新火野猪开关();
        刷新青鳄鱼开关();
        刷新花蘑菇开关();
        刷新服务端最大人数();
        刷新商城检测开关();
        刷新机器多开();
        刷新IP多开();
        刷新登陆保护();
        刷新登陆队列();
        刷新名单拦截();
        刷新登陆帮助();
        刷新机器人();
        刷新双爆频道开关();
        刷新机器人注册开关();
        刷新怪物状态开关();
        刷新越级打怪开关();
        刷新永恒升级开关();
        刷新重生升级开关();
        刷新吸怪检测开关();
        刷新加速检测开关();
        刷新全屏检测开关();
        刷新技能调试开关();
        刷新技能惩罚开关();
        刷新地图名称开关();
        刷新段数检测开关();
        刷新群攻检测开关();
        刷新雇佣持续时间();
        刷新过图存档时间();
        刷新多开数量();
        刷新挂机检测开关();
        刷新每日疲劳值();
        刷新附魔提醒开关();
        刷新仙人模式开关();
        刷新最高伤害();
        刷新捡物检测开关();
    }

    private void 刷新捡物检测开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("捡物检测开关");
        if (S <= 0) {
            显示 = "捡物检测:开启";
        } else {
            显示 = "捡物检测:关闭";
        }
        捡物检测开关.setText(显示);
    }

    private void 刷新最高伤害() {
        最高伤害.setText("" + gui.Start.ConfigValuesMap.get("最高伤害") + "");
    }

    private void 刷新仙人模式开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("仙人模式开关");
        if (S <= 0) {
            显示 = "仙人模式:开启";
        } else {
            显示 = "仙人模式:关闭";
        }
        仙人模式开关.setText(显示);
    }

    private void 刷新附魔提醒开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("附魔提醒开关");
        if (S <= 0) {
            显示 = "附魔提醒:开启";
        } else {
            显示 = "附魔提醒:关闭";
        }
        附魔提醒开关.setText(显示);
    }

    private void 刷新每日疲劳值() {
        疲劳值.setText("" + gui.Start.ConfigValuesMap.get("每日疲劳值") + "");
    }

    private void 刷新多开数量() {

        IP多开数量.setText("" + gui.Start.ConfigValuesMap.get("IP多开数") + "");
        机器码多开数量.setText("" + gui.Start.ConfigValuesMap.get("机器码多开数") + "");
    }

    private void 刷新过图存档时间() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("过图存档开关");
        if (S <= 0) {
            显示 = "过图存档:开启";
        } else {
            显示 = "过图存档:关闭";
        }
        过图存档开关.setText(显示);
    }

    private void 刷新雇佣持续时间() {
        雇佣持续时间.setText("" + gui.Start.ConfigValuesMap.get("雇佣持续时间") + "");
    }

    private void 刷新挂机检测开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("挂机检测开关");
        if (S <= 0) {
            显示 = "挂机检测:开启";
        } else {
            显示 = "挂机检测:关闭";
        }
        挂机检测开关.setText(显示);
    }

    private void 刷新群攻检测开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("群攻检测开关");
        if (S <= 0) {
            显示 = "群攻检测:开启";
        } else {
            显示 = "群攻检测:关闭";
        }
        群攻检测开关.setText(显示);
    }

    private void 刷新段数检测开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("段数检测开关");
        if (S <= 0) {
            显示 = "段数检测:开启";
        } else {
            显示 = "段数检测:关闭";
        }
        段数检测开关.setText(显示);
    }

    private void 刷新加速检测开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("加速检测开关");
        if (S <= 0) {
            显示 = "加速检测:开启";
        } else {
            显示 = "加速检测:关闭";
        }
        加速检测开关.setText(显示);
    }

    private void 刷新地图名称开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("地图名称开关");
        if (S <= 0) {
            显示 = "地图名称:显示";
        } else {
            显示 = "地图名称:关闭";
        }
        地图名称开关.setText(显示);
    }

    private void 刷新技能惩罚开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("技能惩罚开关");
        if (S <= 0) {
            显示 = "技能惩罚:开启";
        } else {
            显示 = "技能惩罚:关闭";
        }
        技能惩罚开关.setText(显示);
    }

    private void 刷新技能调试开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("技能调试开关");
        if (S <= 0) {
            显示 = "技能调试:开启";
        } else {
            显示 = "技能调试:关闭";
        }
        技能调试开关.setText(显示);
    }

    private void 刷新全屏检测开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("全屏检测开关");
        if (S <= 0) {
            显示 = "全屏检测:开启";
        } else {
            显示 = "全屏检测:关闭";
        }
        全屏检测开关.setText(显示);
    }

    private void 刷新吸怪检测开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("吸怪检测开关");
        if (S <= 0) {
            显示 = "吸怪检测:开启";
        } else {
            显示 = "吸怪检测:关闭";
        }
        吸怪检测开关.setText(显示);
    }

    private void 刷新永恒升级开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("永恒升级开关");
        if (S <= 0) {
            显示 = "永恒升级:开启";
        } else {
            显示 = "永恒升级:关闭";
        }
        永恒升级开关.setText(显示);
    }

    private void 刷新重生升级开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("重生升级开关");
        if (S <= 0) {
            显示 = "重生升级:开启";
        } else {
            显示 = "重生升级:关闭";
        }
        重生升级开关.setText(显示);
    }

    private void 刷新越级打怪开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("越级打怪开关");
        if (S <= 0) {
            显示 = "越级打怪:开启";
        } else {
            显示 = "越级打怪:关闭";
        }
        越级打怪开关.setText(显示);
    }

    private void 刷新怪物状态开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("怪物状态开关");
        if (S <= 0) {
            显示 = "怪物状态:开启";
        } else {
            显示 = "怪物状态:关闭";
        }
        怪物状态开关.setText(显示);
    }

    private void 刷新机器人注册开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("机器人注册开关");
        if (S <= 0) {
            显示 = "机器人注册:开启";
        } else {
            显示 = "机器人注册:关闭";
        }
        机器人注册开关.setText(显示);
    }

    private void 刷新机器人() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("QQ机器人开关");
        if (S <= 0) {
            显示 = "机器人:开启";
        } else {
            显示 = "机器人:关闭";
        }
        机器人开关.setText(显示);
    }

    private void 刷新双爆频道开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("双爆频道开关");
        if (S <= 0) {
            显示 = "双爆频道:开启";
        } else {
            显示 = "双爆频道:关闭";
        }
        双爆频道开关.setText(显示);
    }

    private void 刷新游戏找人开关() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("游戏找人开关");
        if (S <= 0) {
            显示 = "游戏找人:开启";
        } else {
            显示 = "游戏找人:关闭";
        }
        游戏找人开关.setText(显示);
    }

    private void 刷新登陆帮助() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("登陆帮助开关");
        if (S <= 0) {
            显示 = "登陆帮助:开启";
        } else {
            显示 = "登陆帮助:关闭";
        }
        登陆帮助开关.setText(显示);
    }

    private void 刷新名单拦截() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("名单拦截开关");
        if (S <= 0) {
            显示 = "名单拦截:开启";
        } else {
            显示 = "名单拦截:关闭";
        }
        名单拦截开关.setText(显示);
    }

    private void 刷新登陆队列() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("登陆队列开关");
        if (S <= 0) {
            显示 = "登陆队列:开启";
        } else {
            显示 = "登陆队列:关闭";
        }
        登陆队列开关.setText(显示);
    }

    private void 刷新登陆保护() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("登陆保护开关");
        if (S <= 0) {
            显示 = "登陆保护:开启";
        } else {
            显示 = "登陆保护:关闭";
        }
        登陆保护开关.setText(显示);
    }

    private void 刷新IP多开() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("IP多开开关");
        if (S <= 0) {
            显示 = "IP多开:禁止";
        } else {
            显示 = "IP多开:允许";
        }
        IP多开开关.setText(显示);
    }

    private void 刷新机器多开() {
        String 显示 = "";
        int S = gui.Start.ConfigValuesMap.get("机器多开开关");
        if (S <= 0) {
            显示 = "机器多开:禁止";
        } else {
            显示 = "机器多开:允许";
        }
        机器多开开关.setText(显示);
    }

    private void 金锤子提升上限(String str) {
        金锤子提升上限.setText(str);
    }

    public void 刷新金锤子() {
        for (int i = this.金锤子表.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.金锤子表.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id = 600  ");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 金锤子表.getModel()).insertRow(金锤子表.getRowCount(), new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("Val")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
        }
        金锤子表.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 金锤子表.getSelectedRow();
                String a = 金锤子表.getValueAt(i, 0).toString();
                String a1 = 金锤子表.getValueAt(i, 1).toString();
                String a2 = 金锤子表.getValueAt(i, 2).toString();
                金锤子序号.setText(a);
                金锤子类型.setText(a1);
                金锤子数值.setText(a2);
            }
        });
    }

    private void 刷新金锤子上限() {
        String 金锤子提升上限显示 = "";
        int 金锤子提升上限 = gui.Start.ConfigValuesMap.get("金锤子提升上限");
        if (金锤子提升上限 <= 0) {
            金锤子提升上限显示 = "提升上限:开启";
        } else {
            金锤子提升上限显示 = "提升上限:关闭";
        }
        金锤子提升上限(金锤子提升上限显示);
    }

    public void 刷新经验加成表() {
        for (int i = this.经验加成表.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.经验加成表.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id = 150 ||  id = 151  ||  id=152  ||  id=153  ");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 经验加成表.getModel()).insertRow(经验加成表.getRowCount(), new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("Val")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        经验加成表.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 经验加成表.getSelectedRow();
                String a = 经验加成表.getValueAt(i, 0).toString();
                String a1 = 经验加成表.getValueAt(i, 1).toString();
                String a2 = 经验加成表.getValueAt(i, 2).toString();
                经验加成表序号.setText(a);
                经验加成表类型.setText(a1);
                经验加成表数值.setText(a2);
            }
        });
    }

    public static void GetConfigValues() {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT name, val FROM ConfigValues");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int val = rs.getInt("val");
                    ConfigValuesMap.put(name, val);
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("GetConfigValues出错：" + ex.getMessage());
        }
    }

    private void 游戏账号注册(String str) {
        游戏账号注册.setText(str);
    }

    private void 限制注册账号数量(String str) {
        限制注册账号数量.setText(str);
    }

    private void 服务端最大人数(String str) {
        服务端最大人数.setText(str);
    }

    private void 冒险家等级上限(String str) {
        冒险家等级上限.setText(str);
    }

    private void 骑士团等级上限(String str) {
        骑士团等级上限.setText(str);
    }

    private void 限制创建角色数量(String str) {
        限制创建角色数量.setText(str);
    }

    private void 游戏升级快讯(String str) {
        游戏升级快讯.setText(str);
    }

    private void 登陆记录开关(String str) {
        登陆记录开关.setText(str);
    }

    private void 刷新商城检测开关() {
        String 刷新登录记录开关显示 = "";
        int 登录记录开关 = gui.Start.ConfigValuesMap.get("商城检测开关");
        if (登录记录开关 <= 0) {
            刷新登录记录开关显示 = "商城检测:开启";
        } else {
            刷新登录记录开关显示 = "商城检测:关闭";
        }
        商城检测开关(刷新登录记录开关显示);
    }

    private void 商城检测开关(String str) {
        商城检测开关.setText(str);
    }

    private void 聊天记录开关(String str) {
        聊天记录开关.setText(str);
    }

    private void 登陆验证开关(String str) {
        登陆验证开关.setText(str);
    }

    private void 游戏冒险家职业开关(String str) {
        冒险家职业开关.setText(str);
    }

    private void 游戏骑士团职业开关(String str) {
        骑士团职业开关.setText(str);
    }

    private void 游戏战神职业开关(String str) {
        战神职业开关.setText(str);
    }

    private void 管理隐身开关(String str) {
        管理隐身开关.setText(str);
    }

    private void 管理加速开关(String str) {
        管理加速开关.setText(str);
    }

    private void 游戏指令开关(String str) {
        游戏指令开关.setText(str);
    }

    private void 游戏喇叭开关(String str) {
        游戏喇叭开关.setText(str);
    }

    private void 丢出金币开关(String str) {
        丢出金币开关.setText(str);
    }

    private void 玩家交易开关(String str) {
        玩家交易开关.setText(str);
    }

    private void 丢出物品开关(String str) {
        丢出物品开关.setText(str);
    }

    private void 禁止登陆开关(String str) {
        禁止登陆开关.setText(str);
    }

    private void 下雪天开关(String str) {
        下雪天开关.setText(str);
    }

    private void 下气泡开关(String str) {
        下气泡开关.setText(str);
    }

    private void 下红花开关(String str) {
        下红花开关.setText(str);
    }

    private void 下雪花开关(String str) {
        下雪花开关.setText(str);
    }

    private void 下枫叶开关(String str) {
        下枫叶开关.setText(str);
    }

    private void 全服决斗开关(String str) {
        全服决斗开关.setText(str);
    }

    private void 欢迎弹窗开关(String str) {
        欢迎弹窗开关.setText(str);
    }

    private void 屠令广播开关(String str) {
        屠令广播开关.setText(str);
    }

    private void 游戏仓库开关(String str) {
        游戏仓库开关.setText(str);
    }

    private void 随身仓库开关(String str) {
        随身仓库开关.setText(str);
    }

    private void 拍卖行开关(String str) {
        拍卖行开关.setText(str);
    }

    private void 上线提醒开关(String str) {
        上线提醒开关.setText(str);
    }

    private void 指令通知开关(String str) {
        指令通知开关.setText(str);
    }

    private void 雇佣商人开关(String str) {
        雇佣商人开关.setText(str);
    }

    private void 巅峰广播开关(String str) {
        巅峰广播开关.setText(str);
    }

    private void 玩家聊天开关(String str) {
        玩家聊天开关.setText(str);
    }

    private void 脚本显码开关(String str) {
        脚本显码开关.setText(str);
    }

    private void 滚动公告开关(String str) {
        滚动公告开关.setText(str);
    }

    private void 回收地图开关(String str) {
        回收地图开关.setText(str);
    }

    private void 蓝蜗牛开关(String str) {
        蓝蜗牛开关.setText(str);
    }

    private void 蘑菇仔开关(String str) {
        蘑菇仔开关.setText(str);
    }

    private void 绿水灵开关(String str) {
        绿水灵开关.setText(str);
    }

    private void 漂漂猪开关(String str) {
        漂漂猪开关.setText(str);
    }

    private void 小青蛇开关(String str) {
        小青蛇开关.setText(str);
    }

    private void 红螃蟹开关(String str) {
        红螃蟹开关.setText(str);
    }

    private void 大海龟开关(String str) {
        大海龟开关.setText(str);
    }

    private void 章鱼怪开关(String str) {
        章鱼怪开关.setText(str);
    }

    private void 顽皮猴开关(String str) {
        顽皮猴开关.setText(str);
    }

    private void 星精灵开关(String str) {
        星精灵开关.setText(str);
    }

    private void 胖企鹅开关(String str) {
        胖企鹅开关.setText(str);
    }

    private void 白雪人开关(String str) {
        白雪人开关.setText(str);
    }

    private void 紫色猫开关(String str) {
        紫色猫开关.setText(str);
    }

    private void 大灰狼开关(String str) {
        大灰狼开关.setText(str);
    }

    private void 小白兔开关(String str) {
        小白兔开关.setText(str);
    }

    private void 喷火龙开关(String str) {
        喷火龙开关.setText(str);
    }

    private void 火野猪开关(String str) {
        火野猪开关.setText(str);
    }

    private void 青鳄鱼开关(String str) {
        青鳄鱼开关.setText(str);
    }

    private void 花蘑菇开关(String str) {
        花蘑菇开关.setText(str);
    }

    private void 刷新花蘑菇开关() {
        String 花蘑菇显示 = "";
        int 花蘑菇 = gui.Start.ConfigValuesMap.get("花蘑菇开关");
        if (花蘑菇 <= 0) {
            花蘑菇显示 = "花蘑菇:开启";
        } else {
            花蘑菇显示 = "花蘑菇:关闭";
        }
        花蘑菇开关(花蘑菇显示);
    }

    private void 刷新火野猪开关() {
        String 火野猪显示 = "";
        int 火野猪 = gui.Start.ConfigValuesMap.get("火野猪开关");
        if (火野猪 <= 0) {
            火野猪显示 = "火野猪:开启";
        } else {
            火野猪显示 = "火野猪:关闭";
        }
        火野猪开关(火野猪显示);
    }

    private void 刷新青鳄鱼开关() {
        String 青鳄鱼显示 = "";
        int 青鳄鱼 = gui.Start.ConfigValuesMap.get("青鳄鱼开关");
        if (青鳄鱼 <= 0) {
            青鳄鱼显示 = "青鳄鱼:开启";
        } else {
            青鳄鱼显示 = "青鳄鱼:关闭";
        }
        青鳄鱼开关(青鳄鱼显示);
    }

    private void 刷新喷火龙开关() {
        String 喷火龙显示 = "";
        int 喷火龙 = gui.Start.ConfigValuesMap.get("喷火龙开关");
        if (喷火龙 <= 0) {
            喷火龙显示 = "喷火龙:开启";
        } else {
            喷火龙显示 = "喷火龙:关闭";
        }
        喷火龙开关(喷火龙显示);
    }

    private void 刷新小白兔开关() {
        String 小白兔显示 = "";
        int 小白兔 = gui.Start.ConfigValuesMap.get("小白兔开关");
        if (小白兔 <= 0) {
            小白兔显示 = "小白兔:开启";
        } else {
            小白兔显示 = "小白兔:关闭";
        }
        小白兔开关(小白兔显示);
    }

    private void 刷新大灰狼开关() {
        String 大灰狼显示 = "";
        int 大灰狼 = gui.Start.ConfigValuesMap.get("大灰狼开关");
        if (大灰狼 <= 0) {
            大灰狼显示 = "大灰狼:开启";
        } else {
            大灰狼显示 = "大灰狼:关闭";
        }
        大灰狼开关(大灰狼显示);
    }

    private void 刷新紫色猫开关() {
        String 紫色猫显示 = "";
        int 紫色猫 = gui.Start.ConfigValuesMap.get("紫色猫开关");
        if (紫色猫 <= 0) {
            紫色猫显示 = "紫色猫:开启";
        } else {
            紫色猫显示 = "紫色猫:关闭";
        }
        紫色猫开关(紫色猫显示);
    }

    private void 石头人开关(String str) {
        石头人开关.setText(str);
    }

    private void 刷新石头人开关() {
        String 石头人显示 = "";
        int 石头人 = gui.Start.ConfigValuesMap.get("石头人开关");
        if (石头人 <= 0) {
            石头人显示 = "石头人:开启";
        } else {
            石头人显示 = "石头人:关闭";
        }
        石头人开关(石头人显示);
    }

    private void 刷新白雪人开关() {
        String 白雪人显示 = "";
        int 白雪人 = gui.Start.ConfigValuesMap.get("白雪人开关");
        if (白雪人 <= 0) {
            白雪人显示 = "白雪人:开启";
        } else {
            白雪人显示 = "白雪人:关闭";
        }
        白雪人开关(白雪人显示);
    }

    private void 刷新胖企鹅开关() {
        String 胖企鹅显示 = "";
        int 胖企鹅 = gui.Start.ConfigValuesMap.get("胖企鹅开关");
        if (胖企鹅 <= 0) {
            胖企鹅显示 = "胖企鹅:开启";
        } else {
            胖企鹅显示 = "胖企鹅:关闭";
        }
        胖企鹅开关(胖企鹅显示);
    }

    private void 刷新星精灵开关() {
        String 星精灵显示 = "";
        int 星精灵 = gui.Start.ConfigValuesMap.get("星精灵开关");
        if (星精灵 <= 0) {
            星精灵显示 = "星精灵:开启";
        } else {
            星精灵显示 = "星精灵:关闭";
        }
        星精灵开关(星精灵显示);
    }

    private void 刷新顽皮猴开关() {
        String 顽皮猴显示 = "";
        int 顽皮猴 = gui.Start.ConfigValuesMap.get("顽皮猴开关");
        if (顽皮猴 <= 0) {
            顽皮猴显示 = "顽皮猴:开启";
        } else {
            顽皮猴显示 = "顽皮猴:关闭";
        }
        顽皮猴开关(顽皮猴显示);
    }

    private void 刷新章鱼怪开关() {
        String 章鱼怪显示 = "";
        int 章鱼怪 = gui.Start.ConfigValuesMap.get("章鱼怪开关");
        if (章鱼怪 <= 0) {
            章鱼怪显示 = "章鱼怪:开启";
        } else {
            章鱼怪显示 = "章鱼怪:关闭";
        }
        章鱼怪开关(章鱼怪显示);
    }

    private void 刷新大海龟开关() {
        String 大海龟显示 = "";
        int 大海龟 = gui.Start.ConfigValuesMap.get("大海龟开关");
        if (大海龟 <= 0) {
            大海龟显示 = "大海龟:开启";
        } else {
            大海龟显示 = "大海龟:关闭";
        }
        大海龟开关(大海龟显示);
    }

    private void 刷新红螃蟹开关() {
        String 红螃蟹显示 = "";
        int 红螃蟹 = gui.Start.ConfigValuesMap.get("红螃蟹开关");
        if (红螃蟹 <= 0) {
            红螃蟹显示 = "红螃蟹:开启";
        } else {
            红螃蟹显示 = "红螃蟹:关闭";
        }
        红螃蟹开关(红螃蟹显示);
    }

    private void 刷新小青蛇开关() {
        String 小青蛇显示 = "";
        int 小青蛇 = gui.Start.ConfigValuesMap.get("小青蛇开关");
        if (小青蛇 <= 0) {
            小青蛇显示 = "小青蛇:开启";
        } else {
            小青蛇显示 = "小青蛇:关闭";
        }
        小青蛇开关(小青蛇显示);
    }

    private void 刷新蓝蜗牛开关() {
        String 蓝蜗牛显示 = "";
        int 蓝蜗牛 = gui.Start.ConfigValuesMap.get("蓝蜗牛开关");
        if (蓝蜗牛 <= 0) {
            蓝蜗牛显示 = "蓝蜗牛:开启";
        } else {
            蓝蜗牛显示 = "蓝蜗牛:关闭";
        }
        蓝蜗牛开关(蓝蜗牛显示);
    }

    private void 刷新漂漂猪开关() {
        String 漂漂猪显示 = "";
        int 漂漂猪 = gui.Start.ConfigValuesMap.get("漂漂猪开关");
        if (漂漂猪 <= 0) {
            漂漂猪显示 = "漂漂猪:开启";
        } else {
            漂漂猪显示 = "漂漂猪:关闭";
        }
        漂漂猪开关(漂漂猪显示);
    }

    private void 刷新绿水灵开关() {
        String 绿水灵显示 = "";
        int 绿水灵 = gui.Start.ConfigValuesMap.get("绿水灵开关");
        if (绿水灵 <= 0) {
            绿水灵显示 = "绿水灵:开启";
        } else {
            绿水灵显示 = "绿水灵:关闭";
        }
        绿水灵开关(绿水灵显示);
    }

    private void 刷新蘑菇仔开关() {
        String 蘑菇仔显示 = "";
        int 蘑菇仔 = gui.Start.ConfigValuesMap.get("蘑菇仔开关");
        if (蘑菇仔 <= 0) {
            蘑菇仔显示 = "蘑菇仔:开启";
        } else {
            蘑菇仔显示 = "蘑菇仔:关闭";
        }
        蘑菇仔开关(蘑菇仔显示);
    }

    private void 刷新注册账号数量() {
        String 注册账号数量显示 = "";
        int 注册账号数量 = gui.Start.ConfigValuesMap.get("注册账号数量");

        注册账号数量显示 = "" + 注册账号数量;

        限制注册账号数量(注册账号数量显示);
    }

    private void 刷新注角色数量() {
        String 注册账号数量显示 = "";
        int 注册账号数量 = gui.Start.ConfigValuesMap.get("创建角色数量");

        注册账号数量显示 = "" + 注册账号数量;

        限制创建角色数量(注册账号数量显示);
    }

    private void 刷新服务端最大人数() {
        String 服务端最大人数显示 = "";
        int 服务端最大人数 = gui.Start.ConfigValuesMap.get("服务端最大人数");

        服务端最大人数显示 = "" + 服务端最大人数;

        服务端最大人数(服务端最大人数显示);
    }

    private void 刷新冒险家等级上限() {
        String 冒险家等级上限显示 = "";
        int 冒险家等级上限 = gui.Start.ConfigValuesMap.get("冒险家等级上限");

        冒险家等级上限显示 = "" + 冒险家等级上限;

        冒险家等级上限(冒险家等级上限显示);
    }

    private void 刷新骑士团等级上限() {
        String 骑士团等级上限显示 = "";
        int 骑士团等级上限 = gui.Start.ConfigValuesMap.get("骑士团等级上限");

        骑士团等级上限显示 = "" + 骑士团等级上限;

        骑士团等级上限(骑士团等级上限显示);
    }

    private void 刷新创建角色数量() {
        String 创建角色数量显示 = "";
        int 创建角色数量 = gui.Start.ConfigValuesMap.get("创建角色数量");

        创建角色数量显示 = "" + 创建角色数量;

        限制创建角色数量(创建角色数量显示);
    }

    public void 刷新封MAC() {
        for (int i = this.封MAC.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.封MAC.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM macbans");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 封MAC.getModel()).insertRow(封MAC.getRowCount(), new Object[]{
                    rs.getInt("macbanid"),
                    rs.getString("mac"),
                    NPCConversationManager.MAC取账号(rs.getString("mac"))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void 刷新封IP() {
        for (int i = this.封IP.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.封IP.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM ipbans");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 封IP.getModel()).insertRow(封IP.getRowCount(), new Object[]{
                    rs.getInt("ipbanid"),
                    rs.getString("ip"),
                    NPCConversationManager.IP取账号(rs.getString("ip"))
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        冒险家职业开关 = new javax.swing.JButton();
        战神职业开关 = new javax.swing.JButton();
        骑士团职业开关 = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        游戏升级快讯 = new javax.swing.JButton();
        管理隐身开关 = new javax.swing.JButton();
        管理加速开关 = new javax.swing.JButton();
        游戏指令开关 = new javax.swing.JButton();
        游戏喇叭开关 = new javax.swing.JButton();
        丢出金币开关 = new javax.swing.JButton();
        玩家交易开关 = new javax.swing.JButton();
        丢出物品开关 = new javax.swing.JButton();
        禁止登陆开关 = new javax.swing.JButton();
        全服决斗开关 = new javax.swing.JButton();
        欢迎弹窗开关 = new javax.swing.JButton();
        屠令广播开关 = new javax.swing.JButton();
        随身仓库开关 = new javax.swing.JButton();
        游戏仓库开关 = new javax.swing.JButton();
        拍卖行开关 = new javax.swing.JButton();
        雇佣商人开关 = new javax.swing.JButton();
        上线提醒开关 = new javax.swing.JButton();
        巅峰广播开关 = new javax.swing.JButton();
        玩家聊天开关 = new javax.swing.JButton();
        脚本显码开关 = new javax.swing.JButton();
        滚动公告开关 = new javax.swing.JButton();
        指令通知开关 = new javax.swing.JButton();
        回收地图开关 = new javax.swing.JButton();
        登陆记录开关 = new javax.swing.JButton();
        聊天记录开关 = new javax.swing.JButton();
        登陆验证开关 = new javax.swing.JButton();
        商城检测开关 = new javax.swing.JButton();
        机器多开开关 = new javax.swing.JButton();
        IP多开开关 = new javax.swing.JButton();
        登陆保护开关 = new javax.swing.JButton();
        登陆队列开关 = new javax.swing.JButton();
        名单拦截开关 = new javax.swing.JButton();
        机器人开关 = new javax.swing.JButton();
        登陆帮助开关 = new javax.swing.JButton();
        双爆频道开关 = new javax.swing.JButton();
        怪物状态开关 = new javax.swing.JButton();
        越级打怪开关 = new javax.swing.JButton();
        游戏找人开关 = new javax.swing.JButton();
        地图名称开关 = new javax.swing.JButton();
        雇佣持续时间 = new javax.swing.JTextField();
        jLabel249 = new javax.swing.JLabel();
        修改骑士团等级上限1 = new javax.swing.JButton();
        过图存档开关 = new javax.swing.JButton();
        挂机检测开关 = new javax.swing.JButton();
        疲劳值 = new javax.swing.JTextField();
        jLabel254 = new javax.swing.JLabel();
        修改骑士团等级上限3 = new javax.swing.JButton();
        附魔提醒开关 = new javax.swing.JButton();
        仙人模式开关 = new javax.swing.JButton();
        段数检测开关 = new javax.swing.JButton();
        群攻检测开关 = new javax.swing.JButton();
        全屏检测开关 = new javax.swing.JButton();
        吸怪检测开关 = new javax.swing.JButton();
        加速检测开关 = new javax.swing.JButton();
        jLabel264 = new javax.swing.JLabel();
        最高伤害 = new javax.swing.JTextField();
        修改最高伤害 = new javax.swing.JButton();
        捡物检测开关 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        蓝蜗牛开关 = new javax.swing.JButton();
        蘑菇仔开关 = new javax.swing.JButton();
        绿水灵开关 = new javax.swing.JButton();
        漂漂猪开关 = new javax.swing.JButton();
        小青蛇开关 = new javax.swing.JButton();
        红螃蟹开关 = new javax.swing.JButton();
        大海龟开关 = new javax.swing.JButton();
        章鱼怪开关 = new javax.swing.JButton();
        顽皮猴开关 = new javax.swing.JButton();
        星精灵开关 = new javax.swing.JButton();
        胖企鹅开关 = new javax.swing.JButton();
        白雪人开关 = new javax.swing.JButton();
        石头人开关 = new javax.swing.JButton();
        紫色猫开关 = new javax.swing.JButton();
        大灰狼开关 = new javax.swing.JButton();
        喷火龙开关 = new javax.swing.JButton();
        火野猪开关 = new javax.swing.JButton();
        小白兔开关 = new javax.swing.JButton();
        青鳄鱼开关 = new javax.swing.JButton();
        花蘑菇开关 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        冒险家等级上限 = new javax.swing.JTextField();
        修改冒险家等级上限 = new javax.swing.JButton();
        jLabel253 = new javax.swing.JLabel();
        骑士团等级上限 = new javax.swing.JTextField();
        jLabel252 = new javax.swing.JLabel();
        修改骑士团等级上限 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        限制注册账号数量 = new javax.swing.JTextField();
        jLabel250 = new javax.swing.JLabel();
        限制创建角色数量 = new javax.swing.JTextField();
        jLabel251 = new javax.swing.JLabel();
        修改账号数量 = new javax.swing.JButton();
        修改角色数量 = new javax.swing.JButton();
        服务端最大人数 = new javax.swing.JTextField();
        修改服务端最大人数 = new javax.swing.JButton();
        游戏账号注册 = new javax.swing.JButton();
        机器人注册开关 = new javax.swing.JButton();
        jLabel263 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        机器码多开数量 = new javax.swing.JTextField();
        修改冒险家等级上限1 = new javax.swing.JButton();
        jLabel262 = new javax.swing.JLabel();
        IP多开数量 = new javax.swing.JTextField();
        jLabel267 = new javax.swing.JLabel();
        修改骑士团等级上限2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane136 = new javax.swing.JScrollPane();
        经验加成表 = new javax.swing.JTable();
        经验加成表序号 = new javax.swing.JTextField();
        经验加成表类型 = new javax.swing.JTextField();
        经验加成表数值 = new javax.swing.JTextField();
        经验加成表修改 = new javax.swing.JButton();
        jLabel330 = new javax.swing.JLabel();
        jLabel331 = new javax.swing.JLabel();
        jLabel333 = new javax.swing.JLabel();
        游戏经验加成说明 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        下雪天开关 = new javax.swing.JButton();
        下气泡开关 = new javax.swing.JButton();
        下红花开关 = new javax.swing.JButton();
        下雪花开关 = new javax.swing.JButton();
        下枫叶开关 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel281 = new javax.swing.JLabel();
        一类设置 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        金锤子 = new javax.swing.JPanel();
        jScrollPane137 = new javax.swing.JScrollPane();
        金锤子表 = new javax.swing.JTable();
        金锤子序号 = new javax.swing.JTextField();
        金锤子类型 = new javax.swing.JTextField();
        金锤子数值 = new javax.swing.JTextField();
        金锤子修改 = new javax.swing.JButton();
        jLabel332 = new javax.swing.JLabel();
        jLabel334 = new javax.swing.JLabel();
        jLabel335 = new javax.swing.JLabel();
        jLabel337 = new javax.swing.JLabel();
        金锤子提升上限 = new javax.swing.JButton();
        封禁IPMAC = new javax.swing.JPanel();
        jScrollPane107 = new javax.swing.JScrollPane();
        封IP = new javax.swing.JTable();
        jScrollPane108 = new javax.swing.JScrollPane();
        封MAC = new javax.swing.JTable();
        刷新封IP = new javax.swing.JButton();
        刷新封MAC = new javax.swing.JButton();
        删除IP代码 = new javax.swing.JTextField();
        删除MAC = new javax.swing.JButton();
        删除IP = new javax.swing.JButton();
        删MAC代码 = new javax.swing.JTextField();
        jLabel338 = new javax.swing.JLabel();
        jLabel339 = new javax.swing.JLabel();
        删除NPC = new javax.swing.JPanel();
        jScrollPane106 = new javax.swing.JScrollPane();
        自添加NPC = new javax.swing.JTable();
        刷新自添加NPC = new javax.swing.JButton();
        删除自添加npc代码 = new javax.swing.JTextField();
        删除自添加npc = new javax.swing.JButton();
        jLabel336 = new javax.swing.JLabel();
        jLabel285 = new javax.swing.JLabel();
        游戏广播 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        广播信息 = new javax.swing.JTable();
        广播文本 = new javax.swing.JTextField();
        刷新广告 = new javax.swing.JButton();
        发布广告 = new javax.swing.JButton();
        广播序号 = new javax.swing.JTextField();
        删除广播 = new javax.swing.JButton();
        修改广播 = new javax.swing.JButton();
        邮件系统 = new javax.swing.JPanel();
        附件1数量 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        邮件文本 = new javax.swing.JTextPane();
        收件人 = new javax.swing.JTextField();
        附件3数量 = new javax.swing.JTextField();
        附件1代码 = new javax.swing.JTextField();
        附件2代码 = new javax.swing.JTextField();
        附件2数量 = new javax.swing.JTextField();
        附件3代码 = new javax.swing.JTextField();
        jLabel340 = new javax.swing.JLabel();
        jLabel341 = new javax.swing.JLabel();
        jLabel342 = new javax.swing.JLabel();
        jLabel343 = new javax.swing.JLabel();
        jLabel344 = new javax.swing.JLabel();
        jLabel345 = new javax.swing.JLabel();
        jLabel346 = new javax.swing.JLabel();
        jLabel347 = new javax.swing.JLabel();
        jLabel348 = new javax.swing.JLabel();
        jLabel349 = new javax.swing.JLabel();
        jLabel350 = new javax.swing.JLabel();
        jLabel351 = new javax.swing.JLabel();
        jLabel352 = new javax.swing.JLabel();
        jLabel353 = new javax.swing.JLabel();
        jLabel354 = new javax.swing.JLabel();
        全服发送 = new javax.swing.JButton();
        个人发送 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        角色信息 = new javax.swing.JTable();
        确认全服 = new javax.swing.JButton();
        刷新角色 = new javax.swing.JButton();
        在线角色 = new javax.swing.JButton();
        离线角色 = new javax.swing.JButton();
        收件人名字 = new javax.swing.JTextField();
        邮件标题 = new javax.swing.JTextField();
        jLabel355 = new javax.swing.JLabel();
        随机发送 = new javax.swing.JButton();
        在线发送 = new javax.swing.JButton();
        清理所有邮件 = new javax.swing.JButton();
        jLabel356 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        永恒升级开关 = new javax.swing.JButton();
        重生升级开关 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        升级经验信息 = new javax.swing.JTable();
        jLabel255 = new javax.swing.JLabel();
        刷新经验表 = new javax.swing.JButton();
        装备升级序号 = new javax.swing.JTextField();
        装备升级经验 = new javax.swing.JTextField();
        装备升级等级 = new javax.swing.JTextField();
        jLabel256 = new javax.swing.JLabel();
        jLabel257 = new javax.swing.JLabel();
        永恒升级开关1 = new javax.swing.JButton();
        jLabel258 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        装备升级设置 = new javax.swing.JTable();
        刷新设置表 = new javax.swing.JButton();
        装备升级序号1 = new javax.swing.JTextField();
        装备升级等级1 = new javax.swing.JTextField();
        装备升级经验1 = new javax.swing.JTextField();
        永恒升级开关2 = new javax.swing.JButton();
        jLabel259 = new javax.swing.JLabel();
        jLabel260 = new javax.swing.JLabel();
        jLabel261 = new javax.swing.JLabel();
        技能范围检测 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        全屏设置表 = new javax.swing.JTable();
        刷新全屏列表 = new javax.swing.JButton();
        技能调试开关 = new javax.swing.JButton();
        技能检测编号 = new javax.swing.JTextField();
        技能检测地图 = new javax.swing.JTextField();
        技能检测数值 = new javax.swing.JTextField();
        搜索1 = new javax.swing.JButton();
        修改1 = new javax.swing.JButton();
        新增1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel268 = new javax.swing.JLabel();
        jLabel269 = new javax.swing.JLabel();
        jLabel270 = new javax.swing.JLabel();
        jLabel271 = new javax.swing.JLabel();
        全局检测开关 = new javax.swing.JButton();
        技能惩罚开关 = new javax.swing.JButton();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "职业开关", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        冒险家职业开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        冒险家职业开关.setText("冒险家");
        冒险家职业开关.setToolTipText("<html>\n<strong><font color=\"#FF0000\">开启:</font></strong><br> \n开启后玩家可以创建冒险家职业。<br> \n<strong><font color=\"#FF0000\">关闭:</font></strong><br> \n关闭后玩家不能创建冒险家职业。<br> <br>  \n");
        冒险家职业开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                冒险家职业开关ActionPerformed(evt);
            }
        });
        jPanel5.add(冒险家职业开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 130, 30));

        战神职业开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        战神职业开关.setText("战神");
        战神职业开关.setToolTipText("<html>\n<strong><font color=\"#FF0000\">开启:</font></strong><br> \n开启后玩家可以创建战神职业。<br> \n<strong><font color=\"#FF0000\">关闭:</font></strong><br> \n关闭后玩家不能创建战神职业。<br> <br>  ");
        战神职业开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                战神职业开关ActionPerformed(evt);
            }
        });
        jPanel5.add(战神职业开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 130, 30));

        骑士团职业开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        骑士团职业开关.setText("骑士团");
        骑士团职业开关.setToolTipText("<html>\n<strong><font color=\"#FF0000\">开启:</font></strong><br> \n开启后玩家可以创建骑士团职业。<br> \n<strong><font color=\"#FF0000\">关闭:</font></strong><br> \n关闭后玩家不能创建骑士团职业。<br> <br>  ");
        骑士团职业开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                骑士团职业开关ActionPerformed(evt);
            }
        });
        jPanel5.add(骑士团职业开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 130, 30));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 190, 140));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "常用开关", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        游戏升级快讯.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        游戏升级快讯.setText("升级快讯");
        游戏升级快讯.setToolTipText("");
        游戏升级快讯.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏升级快讯ActionPerformed(evt);
            }
        });
        jPanel20.add(游戏升级快讯, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 40, 130, 30));

        管理隐身开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        管理隐身开关.setText("管理隐身");
        管理隐身开关.setToolTipText("");
        管理隐身开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                管理隐身开关ActionPerformed(evt);
            }
        });
        jPanel20.add(管理隐身开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 70, 130, 30));

        管理加速开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        管理加速开关.setText("管理加速");
        管理加速开关.setToolTipText("");
        管理加速开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                管理加速开关ActionPerformed(evt);
            }
        });
        jPanel20.add(管理加速开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 70, 130, 30));

        游戏指令开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        游戏指令开关.setText("游戏指令");
        游戏指令开关.setToolTipText("");
        游戏指令开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏指令开关ActionPerformed(evt);
            }
        });
        jPanel20.add(游戏指令开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 130, 30));

        游戏喇叭开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        游戏喇叭开关.setText("游戏喇叭");
        游戏喇叭开关.setToolTipText("");
        游戏喇叭开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏喇叭开关ActionPerformed(evt);
            }
        });
        jPanel20.add(游戏喇叭开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 130, 30));

        丢出金币开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        丢出金币开关.setText("丢出金币");
        丢出金币开关.setToolTipText("");
        丢出金币开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                丢出金币开关ActionPerformed(evt);
            }
        });
        jPanel20.add(丢出金币开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 40, 130, 30));

        玩家交易开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        玩家交易开关.setText("玩家交易");
        玩家交易开关.setToolTipText("");
        玩家交易开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                玩家交易开关ActionPerformed(evt);
            }
        });
        jPanel20.add(玩家交易开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 130, 30));

        丢出物品开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        丢出物品开关.setText("丢出物品");
        丢出物品开关.setToolTipText("");
        丢出物品开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                丢出物品开关ActionPerformed(evt);
            }
        });
        jPanel20.add(丢出物品开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 40, 130, 30));

        禁止登陆开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        禁止登陆开关.setText("游戏登陆");
        禁止登陆开关.setToolTipText("");
        禁止登陆开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                禁止登陆开关ActionPerformed(evt);
            }
        });
        jPanel20.add(禁止登陆开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 130, 30));

        全服决斗开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        全服决斗开关.setText("全服决斗");
        全服决斗开关.setToolTipText("");
        全服决斗开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服决斗开关ActionPerformed(evt);
            }
        });
        jPanel20.add(全服决斗开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, 130, 30));

        欢迎弹窗开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        欢迎弹窗开关.setText("欢迎弹窗");
        欢迎弹窗开关.setToolTipText("");
        欢迎弹窗开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                欢迎弹窗开关ActionPerformed(evt);
            }
        });
        jPanel20.add(欢迎弹窗开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 130, 30));

        屠令广播开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        屠令广播开关.setText("屠令广播");
        屠令广播开关.setToolTipText("");
        屠令广播开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                屠令广播开关ActionPerformed(evt);
            }
        });
        jPanel20.add(屠令广播开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 130, 30));

        随身仓库开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        随身仓库开关.setText("随身仓库");
        随身仓库开关.setToolTipText("");
        随身仓库开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                随身仓库开关ActionPerformed(evt);
            }
        });
        jPanel20.add(随身仓库开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 100, 130, 30));

        游戏仓库开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        游戏仓库开关.setText("游戏仓库");
        游戏仓库开关.setToolTipText("");
        游戏仓库开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏仓库开关ActionPerformed(evt);
            }
        });
        jPanel20.add(游戏仓库开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 100, 130, 30));

        拍卖行开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        拍卖行开关.setText("拍卖行");
        拍卖行开关.setToolTipText("");
        拍卖行开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                拍卖行开关ActionPerformed(evt);
            }
        });
        jPanel20.add(拍卖行开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 100, 130, 30));

        雇佣商人开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        雇佣商人开关.setText("雇佣商人");
        雇佣商人开关.setToolTipText("");
        雇佣商人开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                雇佣商人开关ActionPerformed(evt);
            }
        });
        jPanel20.add(雇佣商人开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 100, 130, 30));

        上线提醒开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        上线提醒开关.setText("上线提醒");
        上线提醒开关.setToolTipText("");
        上线提醒开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                上线提醒开关ActionPerformed(evt);
            }
        });
        jPanel20.add(上线提醒开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 70, 130, 30));

        巅峰广播开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        巅峰广播开关.setText("巅峰广播");
        巅峰广播开关.setToolTipText("");
        巅峰广播开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                巅峰广播开关ActionPerformed(evt);
            }
        });
        jPanel20.add(巅峰广播开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 40, 130, 30));

        玩家聊天开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        玩家聊天开关.setText("玩家聊天");
        玩家聊天开关.setToolTipText("");
        玩家聊天开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                玩家聊天开关ActionPerformed(evt);
            }
        });
        jPanel20.add(玩家聊天开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 130, 30));

        脚本显码开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        脚本显码开关.setText("脚本显码");
        脚本显码开关.setToolTipText("");
        脚本显码开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                脚本显码开关ActionPerformed(evt);
            }
        });
        jPanel20.add(脚本显码开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 100, 130, 30));

        滚动公告开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        滚动公告开关.setText("滚动公告");
        滚动公告开关.setToolTipText("");
        滚动公告开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                滚动公告开关ActionPerformed(evt);
            }
        });
        jPanel20.add(滚动公告开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, 130, 30));

        指令通知开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        指令通知开关.setText("指令通知");
        指令通知开关.setToolTipText("\n");
        指令通知开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                指令通知开关ActionPerformed(evt);
            }
        });
        jPanel20.add(指令通知开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 130, 30));

        回收地图开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        回收地图开关.setText("回收地图");
        回收地图开关.setToolTipText("");
        回收地图开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                回收地图开关ActionPerformed(evt);
            }
        });
        jPanel20.add(回收地图开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 70, 130, 30));

        登陆记录开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        登陆记录开关.setText("登陆记录");
        登陆记录开关.setToolTipText("\n");
        登陆记录开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                登陆记录开关ActionPerformed(evt);
            }
        });
        jPanel20.add(登陆记录开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 130, 30));

        聊天记录开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        聊天记录开关.setText("档案记录");
        聊天记录开关.setToolTipText("");
        聊天记录开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                聊天记录开关ActionPerformed(evt);
            }
        });
        jPanel20.add(聊天记录开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 70, 130, 30));

        登陆验证开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        登陆验证开关.setText("登陆验证");
        登陆验证开关.setToolTipText("");
        登陆验证开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                登陆验证开关ActionPerformed(evt);
            }
        });
        jPanel20.add(登陆验证开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, 130, 30));

        商城检测开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        商城检测开关.setText("商城检测");
        商城检测开关.setToolTipText("");
        商城检测开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                商城检测开关ActionPerformed(evt);
            }
        });
        jPanel20.add(商城检测开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 130, 30));

        机器多开开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        机器多开开关.setText("机器多开");
        机器多开开关.setToolTipText("");
        机器多开开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                机器多开开关ActionPerformed(evt);
            }
        });
        jPanel20.add(机器多开开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, 130, 30));

        IP多开开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        IP多开开关.setText("IP多开");
        IP多开开关.setToolTipText("");
        IP多开开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IP多开开关ActionPerformed(evt);
            }
        });
        jPanel20.add(IP多开开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 130, 130, 30));

        登陆保护开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        登陆保护开关.setText("登陆保护");
        登陆保护开关.setToolTipText("");
        登陆保护开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                登陆保护开关ActionPerformed(evt);
            }
        });
        jPanel20.add(登陆保护开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 130, 130, 30));

        登陆队列开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        登陆队列开关.setText("登陆队列");
        登陆队列开关.setToolTipText("");
        登陆队列开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                登陆队列开关ActionPerformed(evt);
            }
        });
        jPanel20.add(登陆队列开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 130, 130, 30));

        名单拦截开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        名单拦截开关.setText("名单拦截");
        名单拦截开关.setToolTipText("");
        名单拦截开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                名单拦截开关ActionPerformed(evt);
            }
        });
        jPanel20.add(名单拦截开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 130, 130, 30));

        机器人开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        机器人开关.setText("机器人");
        机器人开关.setToolTipText("");
        机器人开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                机器人开关ActionPerformed(evt);
            }
        });
        jPanel20.add(机器人开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 130, 30));

        登陆帮助开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        登陆帮助开关.setText("登陆帮助");
        登陆帮助开关.setToolTipText("");
        登陆帮助开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                登陆帮助开关ActionPerformed(evt);
            }
        });
        jPanel20.add(登陆帮助开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 130, 130, 30));

        双爆频道开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        双爆频道开关.setText("双爆频道");
        双爆频道开关.setToolTipText("");
        双爆频道开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                双爆频道开关ActionPerformed(evt);
            }
        });
        jPanel20.add(双爆频道开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 130, 30));

        怪物状态开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        怪物状态开关.setText("怪物状态");
        怪物状态开关.setToolTipText("");
        怪物状态开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                怪物状态开关ActionPerformed(evt);
            }
        });
        jPanel20.add(怪物状态开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 130, 30));

        越级打怪开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        越级打怪开关.setText("越级打怪");
        越级打怪开关.setToolTipText("");
        越级打怪开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                越级打怪开关ActionPerformed(evt);
            }
        });
        jPanel20.add(越级打怪开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 130, 130, 30));

        游戏找人开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        游戏找人开关.setText("游戏找人");
        游戏找人开关.setToolTipText("");
        游戏找人开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏找人开关ActionPerformed(evt);
            }
        });
        jPanel20.add(游戏找人开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 130, 30));

        地图名称开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        地图名称开关.setText("地图名称");
        地图名称开关.setToolTipText("");
        地图名称开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                地图名称开关ActionPerformed(evt);
            }
        });
        jPanel20.add(地图名称开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 160, 130, 30));
        jPanel20.add(雇佣持续时间, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 260, 100, 25));

        jLabel249.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel249.setText("雇佣持续时间;");
        jPanel20.add(jLabel249, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 230, -1, 30));

        修改骑士团等级上限1.setText("修改");
        修改骑士团等级上限1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改骑士团等级上限1ActionPerformed(evt);
            }
        });
        jPanel20.add(修改骑士团等级上限1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 260, 70, 25));

        过图存档开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        过图存档开关.setText("过图存档");
        过图存档开关.setToolTipText("");
        过图存档开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                过图存档开关ActionPerformed(evt);
            }
        });
        jPanel20.add(过图存档开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 160, 130, 30));

        挂机检测开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        挂机检测开关.setForeground(new java.awt.Color(255, 51, 51));
        挂机检测开关.setText("挂机检测");
        挂机检测开关.setToolTipText("");
        挂机检测开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                挂机检测开关ActionPerformed(evt);
            }
        });
        jPanel20.add(挂机检测开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 130, 30));
        jPanel20.add(疲劳值, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 260, 100, 25));

        jLabel254.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel254.setText("每日限制疲劳值;");
        jPanel20.add(jLabel254, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 230, -1, 30));

        修改骑士团等级上限3.setText("修改");
        修改骑士团等级上限3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改骑士团等级上限3ActionPerformed(evt);
            }
        });
        jPanel20.add(修改骑士团等级上限3, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 260, 70, 25));

        附魔提醒开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        附魔提醒开关.setText("附魔提醒");
        附魔提醒开关.setToolTipText("");
        附魔提醒开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                附魔提醒开关ActionPerformed(evt);
            }
        });
        jPanel20.add(附魔提醒开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 160, 130, 30));

        仙人模式开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        仙人模式开关.setText("仙人模式");
        仙人模式开关.setToolTipText("");
        仙人模式开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                仙人模式开关ActionPerformed(evt);
            }
        });
        jPanel20.add(仙人模式开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 160, 130, 30));

        段数检测开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        段数检测开关.setForeground(new java.awt.Color(255, 0, 51));
        段数检测开关.setText("段数检测");
        段数检测开关.setToolTipText("");
        段数检测开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                段数检测开关ActionPerformed(evt);
            }
        });
        jPanel20.add(段数检测开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 130, 30));

        群攻检测开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        群攻检测开关.setForeground(new java.awt.Color(255, 0, 51));
        群攻检测开关.setText("群攻检测");
        群攻检测开关.setToolTipText("");
        群攻检测开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                群攻检测开关ActionPerformed(evt);
            }
        });
        jPanel20.add(群攻检测开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 130, 30));

        全屏检测开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        全屏检测开关.setForeground(new java.awt.Color(255, 0, 51));
        全屏检测开关.setText("全屏检测");
        全屏检测开关.setToolTipText("");
        全屏检测开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全屏检测开关ActionPerformed(evt);
            }
        });
        jPanel20.add(全屏检测开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, 130, 30));

        吸怪检测开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        吸怪检测开关.setForeground(new java.awt.Color(255, 0, 51));
        吸怪检测开关.setText("吸怪检测");
        吸怪检测开关.setToolTipText("");
        吸怪检测开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                吸怪检测开关ActionPerformed(evt);
            }
        });
        jPanel20.add(吸怪检测开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 130, 30));

        加速检测开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        加速检测开关.setForeground(new java.awt.Color(255, 0, 51));
        加速检测开关.setText("加速检测");
        加速检测开关.setToolTipText("");
        加速检测开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                加速检测开关ActionPerformed(evt);
            }
        });
        jPanel20.add(加速检测开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, 130, 30));

        jLabel264.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel264.setText("不算秒杀值的最高伤害判定/万；");
        jPanel20.add(jLabel264, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 230, -1, 30));

        最高伤害.setText("500");
        jPanel20.add(最高伤害, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 260, 100, 25));

        修改最高伤害.setText("修改");
        修改最高伤害.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改最高伤害ActionPerformed(evt);
            }
        });
        jPanel20.add(修改最高伤害, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 260, 70, 25));

        捡物检测开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        捡物检测开关.setForeground(new java.awt.Color(255, 0, 51));
        捡物检测开关.setText("捡物检测");
        捡物检测开关.setToolTipText("");
        捡物检测开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                捡物检测开关ActionPerformed(evt);
            }
        });
        jPanel20.add(捡物检测开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 270, 130, 30));

        jPanel4.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 1250, 310));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "区域开关", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        蓝蜗牛开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        蓝蜗牛开关.setText("蓝蜗牛");
        蓝蜗牛开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                蓝蜗牛开关ActionPerformed(evt);
            }
        });
        jPanel21.add(蓝蜗牛开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 130, -1));

        蘑菇仔开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        蘑菇仔开关.setText("蘑菇仔");
        蘑菇仔开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                蘑菇仔开关ActionPerformed(evt);
            }
        });
        jPanel21.add(蘑菇仔开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 130, -1));

        绿水灵开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        绿水灵开关.setText("绿水灵");
        绿水灵开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                绿水灵开关ActionPerformed(evt);
            }
        });
        jPanel21.add(绿水灵开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 130, -1));

        漂漂猪开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        漂漂猪开关.setText("漂漂猪");
        漂漂猪开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                漂漂猪开关ActionPerformed(evt);
            }
        });
        jPanel21.add(漂漂猪开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 130, -1));

        小青蛇开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        小青蛇开关.setText("小青蛇");
        小青蛇开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                小青蛇开关ActionPerformed(evt);
            }
        });
        jPanel21.add(小青蛇开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 130, -1));

        红螃蟹开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        红螃蟹开关.setText("红螃蟹");
        红螃蟹开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                红螃蟹开关ActionPerformed(evt);
            }
        });
        jPanel21.add(红螃蟹开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, 130, -1));

        大海龟开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        大海龟开关.setText("大海龟");
        大海龟开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                大海龟开关ActionPerformed(evt);
            }
        });
        jPanel21.add(大海龟开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 40, 130, -1));

        章鱼怪开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        章鱼怪开关.setText("章鱼怪");
        章鱼怪开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                章鱼怪开关ActionPerformed(evt);
            }
        });
        jPanel21.add(章鱼怪开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 40, 130, -1));

        顽皮猴开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        顽皮猴开关.setText("顽皮猴");
        顽皮猴开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                顽皮猴开关ActionPerformed(evt);
            }
        });
        jPanel21.add(顽皮猴开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 40, 130, -1));

        星精灵开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        星精灵开关.setText("星精灵");
        星精灵开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                星精灵开关ActionPerformed(evt);
            }
        });
        jPanel21.add(星精灵开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, 130, -1));

        胖企鹅开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        胖企鹅开关.setText("胖企鹅");
        胖企鹅开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                胖企鹅开关ActionPerformed(evt);
            }
        });
        jPanel21.add(胖企鹅开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 130, -1));

        白雪人开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        白雪人开关.setText("白雪人");
        白雪人开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                白雪人开关ActionPerformed(evt);
            }
        });
        jPanel21.add(白雪人开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 40, 130, -1));

        石头人开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        石头人开关.setText("石头人");
        石头人开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                石头人开关ActionPerformed(evt);
            }
        });
        jPanel21.add(石头人开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 130, -1));

        紫色猫开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        紫色猫开关.setText("紫色猫");
        紫色猫开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                紫色猫开关ActionPerformed(evt);
            }
        });
        jPanel21.add(紫色猫开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 70, 130, -1));

        大灰狼开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        大灰狼开关.setText("大灰狼");
        大灰狼开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                大灰狼开关ActionPerformed(evt);
            }
        });
        jPanel21.add(大灰狼开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 70, 130, -1));

        喷火龙开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        喷火龙开关.setText("喷火龙");
        喷火龙开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                喷火龙开关ActionPerformed(evt);
            }
        });
        jPanel21.add(喷火龙开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 130, -1));

        火野猪开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        火野猪开关.setText("火野猪");
        火野猪开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                火野猪开关ActionPerformed(evt);
            }
        });
        jPanel21.add(火野猪开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 70, 130, -1));

        小白兔开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        小白兔开关.setText("小白兔");
        小白兔开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                小白兔开关ActionPerformed(evt);
            }
        });
        jPanel21.add(小白兔开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 130, -1));

        青鳄鱼开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        青鳄鱼开关.setText("青鳄鱼");
        青鳄鱼开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                青鳄鱼开关ActionPerformed(evt);
            }
        });
        jPanel21.add(青鳄鱼开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 70, 130, -1));

        花蘑菇开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        花蘑菇开关.setText("花蘑菇");
        花蘑菇开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                花蘑菇开关ActionPerformed(evt);
            }
        });
        jPanel21.add(花蘑菇开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 70, 130, -1));

        jPanel4.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 350, 1250, 160));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "等级上限", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel17.add(冒险家等级上限, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 60, -1));

        修改冒险家等级上限.setText("修改");
        修改冒险家等级上限.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改冒险家等级上限ActionPerformed(evt);
            }
        });
        jPanel17.add(修改冒险家等级上限, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 70, -1));

        jLabel253.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel253.setText("冒险家等级上限；");
        jPanel17.add(jLabel253, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 30));
        jPanel17.add(骑士团等级上限, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 60, -1));

        jLabel252.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel252.setText("骑士团等级上限；");
        jPanel17.add(jLabel252, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 30));

        修改骑士团等级上限.setText("修改");
        修改骑士团等级上限.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改骑士团等级上限ActionPerformed(evt);
            }
        });
        jPanel17.add(修改骑士团等级上限, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 70, -1));

        jPanel4.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 520, 310, 140));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "账号设置", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(限制注册账号数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 80, -1));

        jLabel250.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel250.setText("限制创建角色数量；");
        jPanel6.add(jLabel250, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, 30));

        限制创建角色数量.setText("1");
        jPanel6.add(限制创建角色数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 80, -1));

        jLabel251.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel251.setText("限制注册账号数量；");
        jPanel6.add(jLabel251, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 30));

        修改账号数量.setText("修改");
        修改账号数量.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改账号数量ActionPerformed(evt);
            }
        });
        jPanel6.add(修改账号数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, 65, 25));

        修改角色数量.setText("修改");
        修改角色数量.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改角色数量ActionPerformed(evt);
            }
        });
        jPanel6.add(修改角色数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 65, 25));
        jPanel6.add(服务端最大人数, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 80, -1));

        修改服务端最大人数.setText("修改");
        修改服务端最大人数.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改服务端最大人数ActionPerformed(evt);
            }
        });
        jPanel6.add(修改服务端最大人数, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 65, 25));

        游戏账号注册.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        游戏账号注册.setText("账号注册");
        游戏账号注册.setToolTipText("");
        游戏账号注册.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏账号注册ActionPerformed(evt);
            }
        });
        jPanel6.add(游戏账号注册, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 130, 25));

        机器人注册开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        机器人注册开关.setText("机器人注册");
        机器人注册开关.setToolTipText("");
        机器人注册开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                机器人注册开关ActionPerformed(evt);
            }
        });
        jPanel6.add(机器人注册开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 130, 25));

        jLabel263.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel263.setText("服务端最大人数；");
        jPanel6.add(jLabel263, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, 30));

        jPanel4.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 520, 440, 140));

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "多开限制", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel18.add(机器码多开数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 70, -1));

        修改冒险家等级上限1.setText("修改");
        修改冒险家等级上限1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改冒险家等级上限1ActionPerformed(evt);
            }
        });
        jPanel18.add(修改冒险家等级上限1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 70, -1));

        jLabel262.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel262.setText("机器码多开；");
        jPanel18.add(jLabel262, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, 30));
        jPanel18.add(IP多开数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 70, -1));

        jLabel267.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel267.setText("IP地址多开；");
        jPanel18.add(jLabel267, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, 30));

        修改骑士团等级上限2.setText("修改");
        修改骑士团等级上限2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改骑士团等级上限2ActionPerformed(evt);
            }
        });
        jPanel18.add(修改骑士团等级上限2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 70, -1));

        jPanel4.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 520, 290, 140));

        jTabbedPane4.addTab("服务端功能", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), jPanel4); // NOI18N

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "游戏经验加成", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        经验加成表.setFont(new java.awt.Font("幼圆", 0, 20)); // NOI18N
        经验加成表.setForeground(new java.awt.Color(102, 102, 255));
        经验加成表.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "类型", "数值"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        经验加成表.getTableHeader().setReorderingAllowed(false);
        jScrollPane136.setViewportView(经验加成表);

        jPanel9.add(jScrollPane136, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, 400, 360));

        经验加成表序号.setEditable(false);
        jPanel9.add(经验加成表序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 500, 70, -1));

        经验加成表类型.setEditable(false);
        jPanel9.add(经验加成表类型, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 500, 230, -1));
        jPanel9.add(经验加成表数值, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 500, 100, -1));

        经验加成表修改.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        经验加成表修改.setText("修改");
        经验加成表修改.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                经验加成表修改ActionPerformed(evt);
            }
        });
        jPanel9.add(经验加成表修改, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 540, 100, -1));

        jLabel330.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel330.setText("数值；");
        jPanel9.add(jLabel330, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 480, -1, -1));

        jLabel331.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel331.setText("类型；");
        jPanel9.add(jLabel331, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 480, -1, -1));

        jLabel333.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel333.setText("序号；");
        jPanel9.add(jLabel333, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 480, -1, -1));

        游戏经验加成说明.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        游戏经验加成说明.setText("说明");
        游戏经验加成说明.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏经验加成说明ActionPerformed(evt);
            }
        });
        jPanel9.add(游戏经验加成说明, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 540, 100, -1));

        jTabbedPane1.addTab("经验加成", jPanel9);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 730));

        jTabbedPane4.addTab("经验收益", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), jPanel1); // NOI18N

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "游戏世界天气设置\n", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        下雪天开关.setText("下雪天");
        下雪天开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                下雪天开关ActionPerformed(evt);
            }
        });
        jPanel7.add(下雪天开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 200, 130, 30));

        下气泡开关.setText("下气泡");
        下气泡开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                下气泡开关ActionPerformed(evt);
            }
        });
        jPanel7.add(下气泡开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 440, 130, 30));

        下红花开关.setText("下红花");
        下红花开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                下红花开关ActionPerformed(evt);
            }
        });
        jPanel7.add(下红花开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 320, 130, 30));

        下雪花开关.setText("下雪花");
        下雪花开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                下雪花开关ActionPerformed(evt);
            }
        });
        jPanel7.add(下雪花开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 320, 130, 30));

        下枫叶开关.setText("下枫叶");
        下枫叶开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                下枫叶开关ActionPerformed(evt);
            }
        });
        jPanel7.add(下枫叶开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 200, 130, 30));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/图片/天气/1_副本.png"))); // NOI18N
        jPanel7.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 130, -1, 100));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/图片/天气/2_副本.png"))); // NOI18N
        jPanel7.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 250, -1, 100));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/图片/天气/3_副本.png"))); // NOI18N
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, -1, 100));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/图片/天气/4_副本.png"))); // NOI18N
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 250, -1, 100));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/图片/天气/5_副本.png"))); // NOI18N
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 130, -1, 100));

        jLabel281.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel281.setText("开启天气后整个冒险岛所有地图都会有效果，如果发现玩家会卡顿，请关闭使用。");
        jPanel7.add(jLabel281, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 550, 610, 30));

        jTabbedPane4.addTab("天气效果", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), jPanel7); // NOI18N

        一类设置.setBackground(new java.awt.Color(255, 255, 255));
        一类设置.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane5.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N

        金锤子.setBackground(new java.awt.Color(255, 255, 255));
        金锤子.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "金锤子成功率设置", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        金锤子.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        金锤子表.setFont(new java.awt.Font("幼圆", 0, 20)); // NOI18N
        金锤子表.setForeground(new java.awt.Color(255, 255, 255));
        金锤子表.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "类型", "成功率/%"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        金锤子表.getTableHeader().setReorderingAllowed(false);
        jScrollPane137.setViewportView(金锤子表);

        金锤子.add(jScrollPane137, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 250, 480, 60));

        金锤子序号.setEditable(false);
        金锤子.add(金锤子序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 350, 100, -1));

        金锤子类型.setEditable(false);
        金锤子.add(金锤子类型, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 350, 170, -1));
        金锤子.add(金锤子数值, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 350, 100, -1));

        金锤子修改.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        金锤子修改.setText("修改");
        金锤子修改.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                金锤子修改ActionPerformed(evt);
            }
        });
        金锤子.add(金锤子修改, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 350, 80, -1));

        jLabel332.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel332.setText("成功率%;");
        金锤子.add(jLabel332, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 330, -1, -1));

        jLabel334.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel334.setText("类型；");
        金锤子.add(jLabel334, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 330, -1, -1));

        jLabel335.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel335.setForeground(new java.awt.Color(204, 0, 0));
        jLabel335.setText("提示:金锤子可以无限制提高升级次数，谨慎调试成功率");
        金锤子.add(jLabel335, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, -1, -1));

        jLabel337.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel337.setText("序号；");
        金锤子.add(jLabel337, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 330, -1, -1));

        金锤子提升上限.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        金锤子提升上限.setText("提升上限");
        金锤子提升上限.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                金锤子提升上限ActionPerformed(evt);
            }
        });
        金锤子.add(金锤子提升上限, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 450, 150, 60));

        jTabbedPane5.addTab("金锤子", 金锤子);

        封禁IPMAC.setBackground(new java.awt.Color(255, 255, 255));
        封禁IPMAC.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "IP/MAC封禁", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        封禁IPMAC.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        封IP.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        封IP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序列号", "IP地址"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane107.setViewportView(封IP);

        封禁IPMAC.add(jScrollPane107, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 330, 450));

        封MAC.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        封MAC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序列号", "MAC地址"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane108.setViewportView(封MAC);

        封禁IPMAC.add(jScrollPane108, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 330, 450));

        刷新封IP.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新封IP.setText("刷新");
        刷新封IP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新封IPActionPerformed(evt);
            }
        });
        封禁IPMAC.add(刷新封IP, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 520, 70, 30));

        刷新封MAC.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新封MAC.setText("刷新");
        刷新封MAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新封MACActionPerformed(evt);
            }
        });
        封禁IPMAC.add(刷新封MAC, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 520, 70, 30));
        封禁IPMAC.add(删除IP代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 520, 100, 30));

        删除MAC.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除MAC.setText("删除");
        删除MAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除MACActionPerformed(evt);
            }
        });
        封禁IPMAC.add(删除MAC, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 520, 70, 30));

        删除IP.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除IP.setText("删除");
        删除IP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除IPActionPerformed(evt);
            }
        });
        封禁IPMAC.add(删除IP, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 520, 70, 30));
        封禁IPMAC.add(删MAC代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 520, 100, 30));

        jLabel338.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel338.setText("序号；");
        封禁IPMAC.add(jLabel338, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 500, -1, 20));

        jLabel339.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel339.setText("序号；");
        封禁IPMAC.add(jLabel339, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 500, -1, 20));

        jTabbedPane5.addTab("封IP/MAC", 封禁IPMAC);

        删除NPC.setBackground(new java.awt.Color(255, 255, 255));
        删除NPC.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "删除自添加NPC", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), java.awt.Color.black)); // NOI18N
        删除NPC.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        自添加NPC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "地图代码", "NPC代码"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane106.setViewportView(自添加NPC);

        删除NPC.add(jScrollPane106, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, 390, 460));

        刷新自添加NPC.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新自添加NPC.setText("刷新");
        刷新自添加NPC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新自添加NPCActionPerformed(evt);
            }
        });
        删除NPC.add(刷新自添加NPC, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 340, -1, -1));
        删除NPC.add(删除自添加npc代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 300, 80, -1));

        删除自添加npc.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除自添加npc.setText("删除");
        删除自添加npc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除自添加npcActionPerformed(evt);
            }
        });
        删除NPC.add(删除自添加npc, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 370, -1, -1));

        jLabel336.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel336.setText("NPC代码；");
        删除NPC.add(jLabel336, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 280, -1, 20));

        jLabel285.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel285.setText("提示；删除用指令添加的NPC");
        删除NPC.add(jLabel285, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        jTabbedPane5.addTab("删除自添加NPC", 删除NPC);

        一类设置.add(jTabbedPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 730));

        jTabbedPane4.addTab("其他类型", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 一类设置); // NOI18N

        游戏广播.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        广播信息.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "序号", "5分钟一次随机广播内容"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(广播信息);
        if (广播信息.getColumnModel().getColumnCount() > 0) {
            广播信息.getColumnModel().getColumn(0).setResizable(false);
            广播信息.getColumnModel().getColumn(0).setPreferredWidth(100);
            广播信息.getColumnModel().getColumn(1).setResizable(false);
            广播信息.getColumnModel().getColumn(1).setPreferredWidth(2000);
        }

        游戏广播.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 670));

        广播文本.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        游戏广播.add(广播文本, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 680, 760, 40));

        刷新广告.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新广告.setText("刷新广播");
        刷新广告.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新广告ActionPerformed(evt);
            }
        });
        游戏广播.add(刷新广告, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 680, 100, 40));

        发布广告.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        发布广告.setText("新增广播");
        发布广告.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                发布广告ActionPerformed(evt);
            }
        });
        游戏广播.add(发布广告, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 680, 100, 40));

        广播序号.setEditable(false);
        广播序号.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        游戏广播.add(广播序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 680, 60, 40));

        删除广播.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除广播.setText("删除广播");
        删除广播.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除广播ActionPerformed(evt);
            }
        });
        游戏广播.add(删除广播, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 680, 100, 40));

        修改广播.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改广播.setText("修改广播");
        修改广播.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改广播ActionPerformed(evt);
            }
        });
        游戏广播.add(修改广播, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 680, 100, 40));

        jTabbedPane4.addTab("游戏广播", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 游戏广播); // NOI18N

        邮件系统.setBackground(new java.awt.Color(255, 255, 255));
        邮件系统.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        附件1数量.setText("0");
        邮件系统.add(附件1数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 390, 130, 20));

        邮件文本.setText("这里填写邮件的正文");
        jScrollPane1.setViewportView(邮件文本);

        邮件系统.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 410, 220));

        收件人.setEditable(false);
        邮件系统.add(收件人, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 130, 20));

        附件3数量.setText("0");
        邮件系统.add(附件3数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 470, 130, 20));

        附件1代码.setText("0");
        邮件系统.add(附件1代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 130, 20));

        附件2代码.setText("0");
        邮件系统.add(附件2代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 130, 20));

        附件2数量.setText("0");
        邮件系统.add(附件2数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 430, 130, 20));

        附件3代码.setText("0");
        附件3代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                附件3代码ActionPerformed(evt);
            }
        });
        邮件系统.add(附件3代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 130, 20));

        jLabel340.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel340.setText("附件3数量;");
        邮件系统.add(jLabel340, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 450, -1, 20));

        jLabel341.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel341.setText("收件人；");
        邮件系统.add(jLabel341, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, 20));

        jLabel342.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel342.setText("邮件文本；");
        邮件系统.add(jLabel342, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, -1, 20));

        jLabel343.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel343.setText("2代表抵用券");
        邮件系统.add(jLabel343, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 610, 230, 20));

        jLabel344.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel344.setText("附件1代码;");
        邮件系统.add(jLabel344, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, -1, 20));

        jLabel345.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel345.setText("附件2代码;");
        邮件系统.add(jLabel345, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, -1, 20));

        jLabel346.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel346.setText("附件1数量;");
        邮件系统.add(jLabel346, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 370, -1, 20));

        jLabel347.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel347.setText("附件2数量;");
        邮件系统.add(jLabel347, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 410, -1, 20));

        jLabel348.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel348.setText("附件3代码;");
        邮件系统.add(jLabel348, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, -1, 20));

        jLabel349.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel349.setText("从这里选择收件人；");
        邮件系统.add(jLabel349, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 60, -1, 20));

        jLabel350.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel350.setText("0代表没有附件");
        邮件系统.add(jLabel350, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 570, 230, 20));

        jLabel351.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel351.setText("1代表点券");
        邮件系统.add(jLabel351, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 590, 230, 20));

        jLabel352.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel352.setText("其余代表道具");
        邮件系统.add(jLabel352, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 670, 230, 20));

        jLabel353.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel353.setText("3代表经验");
        邮件系统.add(jLabel353, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 630, 230, 20));

        jLabel354.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel354.setText("4代表金币");
        邮件系统.add(jLabel354, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 650, 230, 20));

        全服发送.setText("全服发送");
        全服发送.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送ActionPerformed(evt);
            }
        });
        邮件系统.add(全服发送, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 150, 130, 30));

        个人发送.setText("个人发送");
        个人发送.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                个人发送ActionPerformed(evt);
            }
        });
        邮件系统.add(个人发送, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 180, 130, 30));

        角色信息.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        角色信息.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        角色信息.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "角色ID", "角色名字", "角色等级"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色信息.setName(""); // NOI18N
        角色信息.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(角色信息);
        if (角色信息.getColumnModel().getColumnCount() > 0) {
            角色信息.getColumnModel().getColumn(0).setResizable(false);
            角色信息.getColumnModel().getColumn(0).setPreferredWidth(100);
            角色信息.getColumnModel().getColumn(1).setResizable(false);
            角色信息.getColumnModel().getColumn(1).setPreferredWidth(200);
            角色信息.getColumnModel().getColumn(2).setResizable(false);
            角色信息.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        邮件系统.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 90, 330, 600));

        确认全服.setBackground(new java.awt.Color(255, 255, 255));
        确认全服.setForeground(new java.awt.Color(255, 0, 0));
        确认全服.setText("确认全服");
        确认全服.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                确认全服ActionPerformed(evt);
            }
        });
        邮件系统.add(确认全服, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 90, 130, 30));

        刷新角色.setText("刷新角色");
        刷新角色.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新角色ActionPerformed(evt);
            }
        });
        邮件系统.add(刷新角色, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 150, 130, 30));

        在线角色.setText("在线角色");
        在线角色.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                在线角色ActionPerformed(evt);
            }
        });
        邮件系统.add(在线角色, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 120, 130, 30));

        离线角色.setText("离线角色");
        离线角色.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                离线角色ActionPerformed(evt);
            }
        });
        邮件系统.add(离线角色, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 90, 130, 30));

        收件人名字.setEditable(false);
        邮件系统.add(收件人名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 130, 20));

        邮件标题.setText("这里填写邮件的标题");
        邮件系统.add(邮件标题, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 410, 30));

        jLabel355.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel355.setText("邮件标题；");
        邮件系统.add(jLabel355, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 20));

        随机发送.setText("随机发送");
        随机发送.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                随机发送ActionPerformed(evt);
            }
        });
        邮件系统.add(随机发送, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, 130, 30));

        在线发送.setText("全服在线发送");
        在线发送.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                在线发送ActionPerformed(evt);
            }
        });
        邮件系统.add(在线发送, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 130, 30));

        清理所有邮件.setText("清理所有邮件");
        清理所有邮件.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                清理所有邮件ActionPerformed(evt);
            }
        });
        邮件系统.add(清理所有邮件, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 240, 130, 30));

        jLabel356.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel356.setText("附件代码；");
        邮件系统.add(jLabel356, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, -1, 20));

        jTabbedPane4.addTab("邮件系统", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 邮件系统); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        永恒升级开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        永恒升级开关.setText("永恒升级");
        永恒升级开关.setToolTipText("");
        永恒升级开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                永恒升级开关ActionPerformed(evt);
            }
        });
        jPanel2.add(永恒升级开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 130, 30));

        重生升级开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        重生升级开关.setText("重生升级");
        重生升级开关.setToolTipText("");
        重生升级开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                重生升级开关ActionPerformed(evt);
            }
        });
        jPanel2.add(重生升级开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 130, 30));

        升级经验信息.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "*", "等级", "经验"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(升级经验信息);
        if (升级经验信息.getColumnModel().getColumnCount() > 0) {
            升级经验信息.getColumnModel().getColumn(0).setResizable(false);
            升级经验信息.getColumnModel().getColumn(0).setPreferredWidth(1);
            升级经验信息.getColumnModel().getColumn(1).setResizable(false);
            升级经验信息.getColumnModel().getColumn(1).setPreferredWidth(120);
            升级经验信息.getColumnModel().getColumn(2).setResizable(false);
            升级经验信息.getColumnModel().getColumn(2).setPreferredWidth(120);
        }

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 320, 570));

        jLabel255.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel255.setText("目前最高30级");
        jPanel2.add(jLabel255, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 120, 370, 20));

        刷新经验表.setText("刷新经验表");
        刷新经验表.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新经验表ActionPerformed(evt);
            }
        });
        jPanel2.add(刷新经验表, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 130, 30));

        装备升级序号.setEditable(false);
        jPanel2.add(装备升级序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 620, 60, -1));
        jPanel2.add(装备升级经验, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 620, 170, -1));

        装备升级等级.setEditable(false);
        jPanel2.add(装备升级等级, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 620, 90, -1));

        jLabel256.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel256.setText("装备升级开关；");
        jPanel2.add(jLabel256, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 30));

        jLabel257.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel257.setText("装备升级属性设置请在服务端中的设置");
        jPanel2.add(jLabel257, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 50, 370, 30));

        永恒升级开关1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        永恒升级开关1.setText("修改");
        永恒升级开关1.setToolTipText("");
        永恒升级开关1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                永恒升级开关1ActionPerformed(evt);
            }
        });
        jPanel2.add(永恒升级开关1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 650, 130, 30));

        jLabel258.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel258.setText("装备升级一般设置项；");
        jPanel2.add(jLabel258, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 150, -1, 30));

        装备升级设置.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "1", "信息", "值"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(装备升级设置);
        if (装备升级设置.getColumnModel().getColumnCount() > 0) {
            装备升级设置.getColumnModel().getColumn(0).setResizable(false);
            装备升级设置.getColumnModel().getColumn(0).setPreferredWidth(1);
            装备升级设置.getColumnModel().getColumn(1).setResizable(false);
            装备升级设置.getColumnModel().getColumn(1).setPreferredWidth(200);
            装备升级设置.getColumnModel().getColumn(2).setResizable(false);
            装备升级设置.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel2.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 180, 350, 440));

        刷新设置表.setText("刷新设置表");
        刷新设置表.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新设置表ActionPerformed(evt);
            }
        });
        jPanel2.add(刷新设置表, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 130, 30));

        装备升级序号1.setEditable(false);
        jPanel2.add(装备升级序号1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 620, 60, -1));

        装备升级等级1.setEditable(false);
        jPanel2.add(装备升级等级1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 620, 160, -1));
        jPanel2.add(装备升级经验1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 620, 130, -1));

        永恒升级开关2.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        永恒升级开关2.setText("修改");
        永恒升级开关2.setToolTipText("");
        永恒升级开关2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                永恒升级开关2ActionPerformed(evt);
            }
        });
        jPanel2.add(永恒升级开关2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 650, 130, 30));

        jLabel259.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel259.setText("装备升级所需经验；");
        jPanel2.add(jLabel259, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, -1, 30));

        jLabel260.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel260.setText("路径 *\\Load\\重生永恒属性升级设置");
        jPanel2.add(jLabel260, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 70, 370, 30));

        jLabel261.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel261.setText("防御不会增加双攻，而且属性是设置的一半");
        jPanel2.add(jLabel261, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 100, 370, 20));

        jTabbedPane4.addTab("永恒重生装备", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), jPanel2); // NOI18N

        技能范围检测.setBackground(new java.awt.Color(255, 255, 255));
        技能范围检测.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        全屏设置表.setFont(new java.awt.Font("幼圆", 0, 20)); // NOI18N
        全屏设置表.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "编号", "技能ID", "检测值", "技能名"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(全屏设置表);
        if (全屏设置表.getColumnModel().getColumnCount() > 0) {
            全屏设置表.getColumnModel().getColumn(0).setResizable(false);
            全屏设置表.getColumnModel().getColumn(1).setResizable(false);
            全屏设置表.getColumnModel().getColumn(1).setPreferredWidth(100);
            全屏设置表.getColumnModel().getColumn(2).setResizable(false);
            全屏设置表.getColumnModel().getColumn(2).setPreferredWidth(100);
            全屏设置表.getColumnModel().getColumn(3).setResizable(false);
            全屏设置表.getColumnModel().getColumn(3).setPreferredWidth(200);
        }

        技能范围检测.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 470, 690));

        刷新全屏列表.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        刷新全屏列表.setText("刷新列表");
        刷新全屏列表.setToolTipText("");
        刷新全屏列表.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新全屏列表ActionPerformed(evt);
            }
        });
        技能范围检测.add(刷新全屏列表, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 130, 30));

        技能调试开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        技能调试开关.setForeground(new java.awt.Color(255, 0, 0));
        技能调试开关.setText("技能调试");
        技能调试开关.setToolTipText("");
        技能调试开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                技能调试开关ActionPerformed(evt);
            }
        });
        技能范围检测.add(技能调试开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, 130, 30));

        技能检测编号.setEditable(false);
        技能范围检测.add(技能检测编号, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 510, 60, -1));
        技能范围检测.add(技能检测地图, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 510, 170, -1));
        技能范围检测.add(技能检测数值, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 510, 90, -1));

        搜索1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        搜索1.setText("搜索");
        搜索1.setToolTipText("");
        搜索1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                搜索1ActionPerformed(evt);
            }
        });
        技能范围检测.add(搜索1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 540, 80, 30));

        修改1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        修改1.setText("修改");
        修改1.setToolTipText("");
        修改1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改1ActionPerformed(evt);
            }
        });
        技能范围检测.add(修改1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 540, 80, 30));

        新增1.setText("新增");
        新增1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增1ActionPerformed(evt);
            }
        });
        技能范围检测.add(新增1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 540, 80, 30));

        jButton2.setText("删除");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        技能范围检测.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 540, 80, 30));

        jLabel268.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel268.setText("地图 0 是代表所有技能默认的检测值。");
        技能范围检测.add(jLabel268, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 590, 370, 30));

        jLabel269.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel269.setText("每个技能可以设置单独的检测数值。");
        技能范围检测.add(jLabel269, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 620, 680, 30));

        jLabel270.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel270.setText("检测数值如果大于这个值就会被系统审判。");
        技能范围检测.add(jLabel270, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 650, 630, 30));

        jLabel271.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel271.setText("新增或者修改即时生效，无需重启。");
        技能范围检测.add(jLabel271, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 680, 370, 30));

        全局检测开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        全局检测开关.setForeground(new java.awt.Color(255, 0, 51));
        全局检测开关.setText("技能检测");
        全局检测开关.setToolTipText("");
        全局检测开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全局检测开关ActionPerformed(evt);
            }
        });
        技能范围检测.add(全局检测开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 60, 130, 30));

        技能惩罚开关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        技能惩罚开关.setForeground(new java.awt.Color(255, 0, 0));
        技能惩罚开关.setText("惩罚");
        技能惩罚开关.setToolTipText("");
        技能惩罚开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                技能惩罚开关ActionPerformed(evt);
            }
        });
        技能范围检测.add(技能惩罚开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 130, 30));

        jTabbedPane4.addTab("角色技能检测", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 技能范围检测); // NOI18N

        getContentPane().add(jTabbedPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1268, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void 冒险家职业开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_冒险家职业开关ActionPerformed
        按键开关("冒险家职业开关", 2000);
        刷新冒险家职业开关();
    }//GEN-LAST:event_冒险家职业开关ActionPerformed

    private void 战神职业开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_战神职业开关ActionPerformed
        按键开关("战神职业开关", 2002);
        刷新战神职业开关();
    }//GEN-LAST:event_战神职业开关ActionPerformed

    private void 骑士团职业开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_骑士团职业开关ActionPerformed
        按键开关("骑士团职业开关", 2001);
        刷新骑士团职业开关();
    }//GEN-LAST:event_骑士团职业开关ActionPerformed

    private void 游戏账号注册ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏账号注册ActionPerformed
        按键开关("账号注册开关", 2004);
        刷新账号注册();
    }//GEN-LAST:event_游戏账号注册ActionPerformed

    private void 游戏升级快讯ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏升级快讯ActionPerformed
        按键开关("升级快讯开关", 2003);
        刷新升级快讯();
    }//GEN-LAST:event_游戏升级快讯ActionPerformed

    private void 管理隐身开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_管理隐身开关ActionPerformed
        按键开关("管理隐身开关", 2006);
        刷新管理隐身开关();
    }//GEN-LAST:event_管理隐身开关ActionPerformed

    private void 管理加速开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_管理加速开关ActionPerformed
        按键开关("管理加速开关", 2007);
        刷新管理加速开关();
    }//GEN-LAST:event_管理加速开关ActionPerformed

    private void 游戏指令开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏指令开关ActionPerformed
        按键开关("游戏指令开关", 2008);
        刷新游戏指令开关();
    }//GEN-LAST:event_游戏指令开关ActionPerformed

    private void 游戏喇叭开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏喇叭开关ActionPerformed
        按键开关("游戏喇叭开关", 2009);
        刷新游戏喇叭开关();
    }//GEN-LAST:event_游戏喇叭开关ActionPerformed

    private void 丢出金币开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_丢出金币开关ActionPerformed
        按键开关("丢出金币开关", 2010);
        刷新丢出金币开关();
    }//GEN-LAST:event_丢出金币开关ActionPerformed

    private void 玩家交易开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_玩家交易开关ActionPerformed
        按键开关("玩家交易开关", 2011);
        刷新玩家交易开关();
    }//GEN-LAST:event_玩家交易开关ActionPerformed

    private void 丢出物品开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_丢出物品开关ActionPerformed
        按键开关("丢出物品开关", 2012);
        刷新丢出物品开关();
    }//GEN-LAST:event_丢出物品开关ActionPerformed

    private void 禁止登陆开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_禁止登陆开关ActionPerformed
        按键开关("禁止登陆开关", 2013);
        刷新禁止登陆开关();
    }//GEN-LAST:event_禁止登陆开关ActionPerformed

    private void 全服决斗开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服决斗开关ActionPerformed
        按键开关("全服决斗开关", 2014);
        刷新全服决斗开关();
    }//GEN-LAST:event_全服决斗开关ActionPerformed

    private void 欢迎弹窗开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_欢迎弹窗开关ActionPerformed
        按键开关("欢迎弹窗开关", 2015);
        刷新欢迎弹窗开关();
    }//GEN-LAST:event_欢迎弹窗开关ActionPerformed

    private void 屠令广播开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_屠令广播开关ActionPerformed
        按键开关("屠令广播开关", 2016);
        刷新屠令广播开关();
    }//GEN-LAST:event_屠令广播开关ActionPerformed

    private void 随身仓库开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_随身仓库开关ActionPerformed
        按键开关("随身仓库开关", 2018);
        刷新随身仓库开关();
    }//GEN-LAST:event_随身仓库开关ActionPerformed

    private void 游戏仓库开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏仓库开关ActionPerformed
        按键开关("游戏仓库开关", 2017);
        刷新游戏仓库开关();
    }//GEN-LAST:event_游戏仓库开关ActionPerformed

    private void 拍卖行开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_拍卖行开关ActionPerformed
        按键开关("拍卖行开关", 2019);
        刷新拍卖行开关();
    }//GEN-LAST:event_拍卖行开关ActionPerformed

    private void 雇佣商人开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_雇佣商人开关ActionPerformed
        按键开关("雇佣商人开关", 2020);
        刷新雇佣商人开关();
    }//GEN-LAST:event_雇佣商人开关ActionPerformed

    private void 上线提醒开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_上线提醒开关ActionPerformed
        按键开关("上线提醒开关", 2021);
        刷新上线提醒开关();
    }//GEN-LAST:event_上线提醒开关ActionPerformed

    private void 巅峰广播开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_巅峰广播开关ActionPerformed
        按键开关("巅峰广播开关", 2023);
        刷新巅峰广播开关();
    }//GEN-LAST:event_巅峰广播开关ActionPerformed

    private void 玩家聊天开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_玩家聊天开关ActionPerformed
        按键开关("玩家聊天开关", 2024);
        刷新玩家聊天开关();
    }//GEN-LAST:event_玩家聊天开关ActionPerformed

    private void 脚本显码开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_脚本显码开关ActionPerformed
        按键开关("脚本显码开关", 2025);
        刷新脚本显码开关();
    }//GEN-LAST:event_脚本显码开关ActionPerformed

    private void 滚动公告开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_滚动公告开关ActionPerformed
        按键开关("滚动公告开关", 2026);
        刷新滚动公告开关();
    }//GEN-LAST:event_滚动公告开关ActionPerformed

    private void 指令通知开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_指令通知开关ActionPerformed
        按键开关("指令通知开关", 2028);
        刷新指令通知开关();
    }//GEN-LAST:event_指令通知开关ActionPerformed

    private void 回收地图开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_回收地图开关ActionPerformed
        按键开关("回收地图开关", 2029);
        刷新回收地图开关();
    }//GEN-LAST:event_回收地图开关ActionPerformed

    private void 登陆记录开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_登陆记录开关ActionPerformed
        按键开关("登陆记录开关", 2030);
        刷新登陆记录开关();
    }//GEN-LAST:event_登陆记录开关ActionPerformed

    private void 聊天记录开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_聊天记录开关ActionPerformed
        按键开关("聊天记录开关", 2031);
        刷新聊天记录开关();
    }//GEN-LAST:event_聊天记录开关ActionPerformed

    private void 登陆验证开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_登陆验证开关ActionPerformed
        按键开关("登陆验证开关", 2040);
        刷新登陆验证开关();
    }//GEN-LAST:event_登陆验证开关ActionPerformed

    private void 商城检测开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_商城检测开关ActionPerformed
        按键开关("商城检测开关", 2042);
        刷新商城检测开关();
    }//GEN-LAST:event_商城检测开关ActionPerformed

    private void 蓝蜗牛开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_蓝蜗牛开关ActionPerformed
        按键开关("蓝蜗牛开关", 2200);
        刷新蓝蜗牛开关();
    }//GEN-LAST:event_蓝蜗牛开关ActionPerformed

    private void 蘑菇仔开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_蘑菇仔开关ActionPerformed
        按键开关("蘑菇仔开关", 2201);
        刷新蘑菇仔开关();
    }//GEN-LAST:event_蘑菇仔开关ActionPerformed

    private void 绿水灵开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_绿水灵开关ActionPerformed
        按键开关("绿水灵开关", 2202);
        刷新绿水灵开关();
    }//GEN-LAST:event_绿水灵开关ActionPerformed

    private void 漂漂猪开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_漂漂猪开关ActionPerformed
        按键开关("漂漂猪开关", 2203);
        刷新漂漂猪开关();
    }//GEN-LAST:event_漂漂猪开关ActionPerformed

    private void 小青蛇开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_小青蛇开关ActionPerformed
        按键开关("小青蛇开关", 2204);
        刷新小青蛇开关();
    }//GEN-LAST:event_小青蛇开关ActionPerformed

    private void 红螃蟹开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_红螃蟹开关ActionPerformed
        按键开关("红螃蟹开关", 2205);
        刷新红螃蟹开关();
    }//GEN-LAST:event_红螃蟹开关ActionPerformed

    private void 大海龟开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_大海龟开关ActionPerformed
        按键开关("大海龟开关", 2206);
        刷新大海龟开关();
    }//GEN-LAST:event_大海龟开关ActionPerformed

    private void 章鱼怪开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_章鱼怪开关ActionPerformed
        按键开关("章鱼怪开关", 2207);
        刷新章鱼怪开关();
    }//GEN-LAST:event_章鱼怪开关ActionPerformed

    private void 顽皮猴开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_顽皮猴开关ActionPerformed
        按键开关("顽皮猴开关", 2208);
        刷新顽皮猴开关();
    }//GEN-LAST:event_顽皮猴开关ActionPerformed

    private void 星精灵开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_星精灵开关ActionPerformed
        按键开关("星精灵开关", 2209);
        刷新星精灵开关();
    }//GEN-LAST:event_星精灵开关ActionPerformed

    private void 胖企鹅开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_胖企鹅开关ActionPerformed
        按键开关("胖企鹅开关", 2210);
        刷新胖企鹅开关();
    }//GEN-LAST:event_胖企鹅开关ActionPerformed

    private void 白雪人开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_白雪人开关ActionPerformed
        按键开关("白雪人开关", 2211);
        刷新白雪人开关();
    }//GEN-LAST:event_白雪人开关ActionPerformed

    private void 石头人开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_石头人开关ActionPerformed
        按键开关("石头人开关", 2212);
        刷新石头人开关();
    }//GEN-LAST:event_石头人开关ActionPerformed

    private void 紫色猫开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_紫色猫开关ActionPerformed
        按键开关("紫色猫开关", 2213);
        刷新紫色猫开关();
    }//GEN-LAST:event_紫色猫开关ActionPerformed

    private void 大灰狼开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_大灰狼开关ActionPerformed
        按键开关("大灰狼开关", 2214);
        刷新大灰狼开关();
    }//GEN-LAST:event_大灰狼开关ActionPerformed

    private void 喷火龙开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_喷火龙开关ActionPerformed
        按键开关("喷火龙开关", 2216);
        刷新喷火龙开关();
    }//GEN-LAST:event_喷火龙开关ActionPerformed

    private void 火野猪开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_火野猪开关ActionPerformed
        按键开关("火野猪开关", 2217);
        刷新火野猪开关();
    }//GEN-LAST:event_火野猪开关ActionPerformed

    private void 小白兔开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_小白兔开关ActionPerformed
        按键开关("小白兔开关", 2215);
        刷新小白兔开关();
    }//GEN-LAST:event_小白兔开关ActionPerformed

    private void 青鳄鱼开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_青鳄鱼开关ActionPerformed
        按键开关("青鳄鱼开关", 2218);
        刷新青鳄鱼开关();
    }//GEN-LAST:event_青鳄鱼开关ActionPerformed

    private void 花蘑菇开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_花蘑菇开关ActionPerformed
        按键开关("花蘑菇开关", 2219);
        刷新花蘑菇开关();
    }//GEN-LAST:event_花蘑菇开关ActionPerformed

    private void 修改骑士团等级上限ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改骑士团等级上限ActionPerformed
        if (骑士团等级上限.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.骑士团等级上限.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 2301);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.骑士团等级上限.getText() + "' where id = 2301;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新骑士团等级上限();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改骑士团等级上限ActionPerformed

    private void 修改冒险家等级上限ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改冒险家等级上限ActionPerformed

        if (冒险家等级上限.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.冒险家等级上限.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 2300);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.冒险家等级上限.getText() + "' where id = 2300;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新冒险家等级上限();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改冒险家等级上限ActionPerformed

    private void 修改账号数量ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改账号数量ActionPerformed
        if (限制注册账号数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.限制注册账号数量.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1050);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.限制注册账号数量.getText() + "' where id = 1050;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新注册账号数量();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改账号数量ActionPerformed

    private void 修改角色数量ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改角色数量ActionPerformed
        if (限制创建角色数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.限制创建角色数量.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1051);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.限制创建角色数量.getText() + "' where id = 1051;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新注角色数量();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_修改角色数量ActionPerformed

    private void 修改服务端最大人数ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改服务端最大人数ActionPerformed
        boolean result1 = this.服务端最大人数.getText().matches("[0-9]+");
        if (服务端最大人数.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.服务端最大人数.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 2302);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.服务端最大人数.getText() + "' where id = 2302;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新服务端最大人数();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改服务端最大人数ActionPerformed

    private void 经验加成表修改ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_经验加成表修改ActionPerformed

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.经验加成表序号.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.经验加成表序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + this.经验加成表数值.getText() + "' where id= " + this.经验加成表序号.getText() + ";";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    刷新经验加成表();
                    gui.Start.GetConfigValues();
                    JOptionPane.showMessageDialog(null, "修改成功已经生效");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要修改的值");
        }
    }//GEN-LAST:event_经验加成表修改ActionPerformed

    private void 游戏经验加成说明ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏经验加成说明ActionPerformed
        JOptionPane.showMessageDialog(null, "<相关说明文>\r\n\r\n"
                + "1:相对应数值为0则为关闭经验加成。\r\n"
                + "2:人气经验 = 人气 * 人气经验加成数值。\r\n"
                + "\r\n");
    }//GEN-LAST:event_游戏经验加成说明ActionPerformed

    private void 下雪天开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_下雪天开关ActionPerformed
        int 下雪天 = gui.Start.ConfigValuesMap.get("下雪天开关");
        int 下红花 = gui.Start.ConfigValuesMap.get("下红花开关");
        int 下枫叶 = gui.Start.ConfigValuesMap.get("下枫叶开关");
        int 下气泡 = gui.Start.ConfigValuesMap.get("下气泡开关");
        int 下雪花 = gui.Start.ConfigValuesMap.get("下雪花开关");

        int 下雪天开关 = gui.Start.ConfigValuesMap.get("下雪天开关");
        if (下雪天开关 <= 0) {
            按键开关("下雪天开关", 1001);
            刷新下雪天开关();
            /*PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from configvalues where id =1001";
                    ps1.executeUpdate(sqlstr);
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, 1001);
                ps.setString(2, "下雪天开关");
                ps.setInt(3, 1);
                ps.executeUpdate();
                gui.Start.GetConfigValues();
                刷新下雪天开关();
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        } else {
            if (下红花 == 0 || 下枫叶 == 0 || 下气泡 == 0 || 下雪花 == 0) {
                JOptionPane.showMessageDialog(null, "开启此天气，需要保证其他天气处于关闭状态。");
                return;
            }
            按键开关("下雪天开关", 1001);
            刷新下雪天开关();
            /*PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from configvalues where id =1001";
                    ps1.executeUpdate(sqlstr);
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, 1001);
                ps.setString(2, "下雪天开关");
                ps.setInt(3, 0);
                ps.executeUpdate();
                gui.Start.GetConfigValues();
                刷新下雪天开关();
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }//GEN-LAST:event_下雪天开关ActionPerformed

    private void 下气泡开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_下气泡开关ActionPerformed
        int 下雪天 = gui.Start.ConfigValuesMap.get("下雪天开关");
        int 下红花 = gui.Start.ConfigValuesMap.get("下红花开关");
        int 下枫叶 = gui.Start.ConfigValuesMap.get("下枫叶开关");
        int 下气泡 = gui.Start.ConfigValuesMap.get("下气泡开关");
        int 下雪花 = gui.Start.ConfigValuesMap.get("下雪花开关");
        int 下气泡开关 = gui.Start.ConfigValuesMap.get("下气泡开关");
        if (下气泡开关 <= 0) {
            按键开关("下气泡开关", 1003);
            刷新下气泡开关();
            /* PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from configvalues where id =1003";
                    ps1.executeUpdate(sqlstr);
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, 1003);
                ps.setString(2, "下气泡开关");
                ps.setInt(3, 1);
                ps.executeUpdate();
                gui.Start.GetConfigValues();
                刷新下气泡开关();
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        } else {
            if (下红花 == 0 || 下枫叶 == 0 || 下雪天 == 0 || 下雪花 == 0) {
                JOptionPane.showMessageDialog(null, "开启此天气，需要保证其他天气处于关闭状态。");
                return;
            }
            按键开关("下气泡开关", 1003);
            刷新下气泡开关();
            /*PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from configvalues where id =1003";
                    ps1.executeUpdate(sqlstr);
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, 1003);
                ps.setString(2, "下气泡开关");
                ps.setInt(3, 0);
                ps.executeUpdate();
                gui.Start.GetConfigValues();
                刷新下气泡开关();
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }//GEN-LAST:event_下气泡开关ActionPerformed

    private void 下红花开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_下红花开关ActionPerformed
        int 下雪天 = gui.Start.ConfigValuesMap.get("下雪天开关");
        int 下红花 = gui.Start.ConfigValuesMap.get("下红花开关");
        int 下枫叶 = gui.Start.ConfigValuesMap.get("下枫叶开关");
        int 下气泡 = gui.Start.ConfigValuesMap.get("下气泡开关");
        int 下雪花 = gui.Start.ConfigValuesMap.get("下雪花开关");
        int 下红花开关 = gui.Start.ConfigValuesMap.get("下红花开关");
        if (下红花开关 <= 0) {
            按键开关("下红花开关", 1002);
            刷新下红花开关();
//            PreparedStatement ps1 = null;
//            ResultSet rs = null;
//            try {
//                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
//                ps1.setInt(1, 1);
//                rs = ps1.executeQuery();
//                if (rs.next()) {
//                    String sqlstr = " delete from configvalues where id =1002";
//                    ps1.executeUpdate(sqlstr);
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
//                ps.setInt(1, 1002);
//                ps.setString(2, "下红花开关");
//                ps.setInt(3, 1);
//                ps.executeUpdate();
//                gui.Start.GetConfigValues();
//                刷新下红花开关();
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
        } else {
            if (下雪天 == 0 || 下枫叶 == 0 || 下气泡 == 0 || 下雪花 == 0) {
                JOptionPane.showMessageDialog(null, "开启此天气，需要保证其他天气处于关闭状态。");
                return;
            }
            按键开关("下红花开关", 1002);
            刷新下红花开关();
//            PreparedStatement ps1 = null;
//            ResultSet rs = null;
//            try {
//                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
//                ps1.setInt(1, 1);
//                rs = ps1.executeQuery();
//                if (rs.next()) {
//                    String sqlstr = " delete from configvalues where id =1002";
//                    ps1.executeUpdate(sqlstr);
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
//                ps.setInt(1, 1002);
//                ps.setString(2, "下红花开关");
//                ps.setInt(3, 0);
//                ps.executeUpdate();
//                gui.Start.GetConfigValues();
//                刷新下红花开关();
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }//GEN-LAST:event_下红花开关ActionPerformed

    private void 下雪花开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_下雪花开关ActionPerformed
        int 下雪天 = gui.Start.ConfigValuesMap.get("下雪天开关");
        int 下红花 = gui.Start.ConfigValuesMap.get("下红花开关");
        int 下枫叶 = gui.Start.ConfigValuesMap.get("下枫叶开关");
        int 下气泡 = gui.Start.ConfigValuesMap.get("下气泡开关");
        int 下雪花 = gui.Start.ConfigValuesMap.get("下雪花开关");
        int 下雪花开关 = gui.Start.ConfigValuesMap.get("下雪花开关");
        if (下雪花开关 <= 0) {
            按键开关("下雪花开关", 1004);
            刷新下雪花开关();
//            PreparedStatement ps1 = null;
//            ResultSet rs = null;
//            try {
//                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
//                ps1.setInt(1, 1);
//                rs = ps1.executeQuery();
//                if (rs.next()) {
//                    String sqlstr = " delete from configvalues where id =1004";
//                    ps1.executeUpdate(sqlstr);
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
//                ps.setInt(1, 1004);
//                ps.setString(2, "下雪花开关");
//                ps.setInt(3, 1);
//                ps.executeUpdate();
//                gui.Start.GetConfigValues();
//                刷新下雪花开关();
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
        } else {
            if (下红花 == 0 || 下枫叶 == 0 || 下气泡 == 0 || 下雪天 == 0) {
                JOptionPane.showMessageDialog(null, "开启此天气，需要保证其他天气处于关闭状态。");
                return;
            }
            按键开关("下雪花开关", 1004);
            刷新下雪花开关();
//            PreparedStatement ps1 = null;
//            ResultSet rs = null;
//            try {
//                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
//                ps1.setInt(1, 1);
//                rs = ps1.executeQuery();
//                if (rs.next()) {
//                    String sqlstr = " delete from configvalues where id =1004";
//                    ps1.executeUpdate(sqlstr);
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
//                ps.setInt(1, 1004);
//                ps.setString(2, "下雪花开关");
//                ps.setInt(3, 0);
//                ps.executeUpdate();
//                gui.Start.GetConfigValues();
//                刷新下雪花开关();
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }//GEN-LAST:event_下雪花开关ActionPerformed

    private void 下枫叶开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_下枫叶开关ActionPerformed
        int 下雪天 = gui.Start.ConfigValuesMap.get("下雪天开关");
        int 下红花 = gui.Start.ConfigValuesMap.get("下红花开关");
        int 下枫叶 = gui.Start.ConfigValuesMap.get("下枫叶开关");
        int 下气泡 = gui.Start.ConfigValuesMap.get("下气泡开关");
        int 下雪花 = gui.Start.ConfigValuesMap.get("下雪花开关");
        int 下枫叶开关 = gui.Start.ConfigValuesMap.get("下枫叶开关");
        if (下枫叶开关 <= 0) {
            按键开关("下枫叶开关", 1005);
            刷新下枫叶开关();
            /* PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from configvalues where id =1005";
                    ps1.executeUpdate(sqlstr);
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, 1005);
                ps.setString(2, "下枫叶开关");
                ps.setInt(3, 1);
                ps.executeUpdate();
                gui.Start.GetConfigValues();
                刷新下枫叶开关();
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        } else {
            if (下红花 == 0 || 下雪花 == 0 || 下气泡 == 0 || 下雪天 == 0) {
                JOptionPane.showMessageDialog(null, "开启此天气，需要保证其他天气处于关闭状态。");
                return;
            }
            按键开关("下枫叶开关", 1005);
            刷新下枫叶开关();
//            PreparedStatement ps1 = null;
//            ResultSet rs = null;
//            try {
//                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
//                ps1.setInt(1, 1);
//                rs = ps1.executeQuery();
//                if (rs.next()) {
//                    String sqlstr = " delete from configvalues where id =1005";
//                    ps1.executeUpdate(sqlstr);
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
//                ps.setInt(1, 1005);
//                ps.setString(2, "下枫叶开关");
//                ps.setInt(3, 0);
//                ps.executeUpdate();
//                gui.Start.GetConfigValues();
//                刷新下枫叶开关();
//            } catch (SQLException ex) {
//                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }//GEN-LAST:event_下枫叶开关ActionPerformed

    private void 刷新自添加NPCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新自添加NPCActionPerformed
        刷新自添加NPC();        // TODO add your handling code here:
    }//GEN-LAST:event_刷新自添加NPCActionPerformed

    private void 删除自添加npcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除自添加npcActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.删除自添加npc代码.getText().matches("[0-9]+");

        if (result1) {
            if (Integer.parseInt(this.删除自添加npc代码.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "请填写正确的值");
            }
            try {

                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM 游戏npc管理 WHERE dataid = ?");
                ps1.setInt(1, Integer.parseInt(this.删除自添加npc代码.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from 游戏npc管理 where dataid =" + Integer.parseInt(this.删除自添加npc代码.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    JOptionPane.showMessageDialog(null, "成功删除 " + Integer.parseInt(this.删除自添加npc代码.getText()) + " npc.重启生效。");
                    刷新自添加NPC();

                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入数字 ");
        }
    }//GEN-LAST:event_删除自添加npcActionPerformed

    private void 金锤子修改ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_金锤子修改ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.金锤子序号.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.金锤子序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + this.金锤子数值.getText() + "' where id= " + this.金锤子序号.getText() + ";";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    刷新金锤子();
                    gui.Start.GetConfigValues();
                    JOptionPane.showMessageDialog(null, "修改成功已经生效");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要修改的值");
        }
    }//GEN-LAST:event_金锤子修改ActionPerformed

    private void 金锤子提升上限ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_金锤子提升上限ActionPerformed
        按键开关("金锤子提升上限", 601);
        刷新金锤子上限();
    }//GEN-LAST:event_金锤子提升上限ActionPerformed

    private void 刷新封IPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新封IPActionPerformed
        刷新封IP();
    }//GEN-LAST:event_刷新封IPActionPerformed

    private void 刷新封MACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新封MACActionPerformed
        刷新封MAC();
    }//GEN-LAST:event_刷新封MACActionPerformed

    private void 删除MACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除MACActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.删MAC代码.getText().matches("[0-9]+");

        if (result1) {
            if (Integer.parseInt(this.删MAC代码.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "请填写正确的值");
            }
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM macbans WHERE macbanid = ?");
                ps1.setInt(1, Integer.parseInt(this.删MAC代码.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from macbans where macbanid =" + Integer.parseInt(this.删MAC代码.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新封MAC();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入数字 ");
        }
    }//GEN-LAST:event_删除MACActionPerformed

    private void 删除IPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除IPActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.删除IP代码.getText().matches("[0-9]+");

        if (result1) {
            if (Integer.parseInt(this.删除IP代码.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "请填写正确的值");
            }
            try {

                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM ipbans WHERE ipbanid = ?");
                ps1.setInt(1, Integer.parseInt(this.删除IP代码.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from ipbans where ipbanid =" + Integer.parseInt(this.删除IP代码.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新封IP();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请输入数字 ");
        }
    }//GEN-LAST:event_删除IPActionPerformed

    private void 机器多开开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_机器多开开关ActionPerformed
        按键开关("机器多开开关", 2053);
        刷新机器多开();
    }//GEN-LAST:event_机器多开开关ActionPerformed

    private void IP多开开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IP多开开关ActionPerformed
        按键开关("IP多开开关", 2054);
        刷新IP多开();

    }//GEN-LAST:event_IP多开开关ActionPerformed

    private void 登陆保护开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_登陆保护开关ActionPerformed
        按键开关("登陆保护开关", 2055);
        刷新登陆保护();
    }//GEN-LAST:event_登陆保护开关ActionPerformed

    private void 登陆队列开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_登陆队列开关ActionPerformed
        按键开关("登陆队列开关", 2056);
        刷新登陆队列();
    }//GEN-LAST:event_登陆队列开关ActionPerformed

    private void 名单拦截开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_名单拦截开关ActionPerformed
        按键开关("名单拦截开关", 2057);
        刷新名单拦截();
    }//GEN-LAST:event_名单拦截开关ActionPerformed

    private void 机器人开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_机器人开关ActionPerformed
        按键开关("QQ机器人开关", 2223);
        刷新机器人();
    }//GEN-LAST:event_机器人开关ActionPerformed

    private void 登陆帮助开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_登陆帮助开关ActionPerformed
        按键开关("登陆帮助开关", 2058);
        刷新登陆帮助();
    }//GEN-LAST:event_登陆帮助开关ActionPerformed

    private void 刷新广告ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新广告ActionPerformed
        刷新公告广播();
    }//GEN-LAST:event_刷新广告ActionPerformed

    private void 发布广告ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_发布广告ActionPerformed
        if (广播文本.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "请填写广告信息哦。");
            return;
        }
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO 广播信息 ( 广播 ) VALUES ( ? )")) {
            ps.setString(1, 广播文本.getText());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        刷新公告广播();
        JOptionPane.showMessageDialog(null, "发布完成。");
    }//GEN-LAST:event_发布广告ActionPerformed

    private void 删除广播ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除广播ActionPerformed

        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.广播序号.getText().matches("[0-9]+");

        if (result1) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM 广播信息 WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.广播序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from 广播信息 where id =" + Integer.parseInt(this.广播序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新公告广播();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_删除广播ActionPerformed

    private void 修改广播ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改广播ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE 广播信息 SET 广播 = ? WHERE id = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM 广播信息  WHERE id = ? ");
            ps1.setInt(1, Integer.parseInt(广播序号.getText()));
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update 广播信息 set 广播 = '" + 广播文本.getText() + "' where id = " + Integer.parseInt(广播序号.getText()) + ";";
                PreparedStatement a1 = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                a1.executeUpdate(sqlString1);
                刷新公告广播();
                JOptionPane.showMessageDialog(null, "修改成功。");
            }
        } catch (SQLException ex) {
        }
    }//GEN-LAST:event_修改广播ActionPerformed

    private void 双爆频道开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_双爆频道开关ActionPerformed
        按键开关("双爆频道开关", 2059);
        刷新双爆频道开关();
        JOptionPane.showMessageDialog(null, "重启生效。");
    }//GEN-LAST:event_双爆频道开关ActionPerformed

    private void 机器人注册开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_机器人注册开关ActionPerformed
        按键开关("机器人注册开关", 2060);
        刷新机器人注册开关();
    }//GEN-LAST:event_机器人注册开关ActionPerformed

    private void 怪物状态开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_怪物状态开关ActionPerformed
        按键开关("怪物状态开关", 2061);
        刷新怪物状态开关();
    }//GEN-LAST:event_怪物状态开关ActionPerformed

    private void 越级打怪开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_越级打怪开关ActionPerformed
        按键开关("越级打怪开关", 2125);
        刷新越级打怪开关();
    }//GEN-LAST:event_越级打怪开关ActionPerformed

    private void 游戏找人开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏找人开关ActionPerformed
        按键开关("游戏找人开关", 2127);
        刷新游戏找人开关();
    }//GEN-LAST:event_游戏找人开关ActionPerformed

    private void 刷新角色ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新角色ActionPerformed
        刷新角色信息();
    }//GEN-LAST:event_刷新角色ActionPerformed

    private void 在线角色ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_在线角色ActionPerformed
        刷新在线角色信息();
    }//GEN-LAST:event_在线角色ActionPerformed

    private void 离线角色ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_离线角色ActionPerformed
        刷新离线角色信息();
    }//GEN-LAST:event_离线角色ActionPerformed

    private void 个人发送ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_个人发送ActionPerformed
        个人发送邮件();
    }//GEN-LAST:event_个人发送ActionPerformed

    private void 全服发送ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送ActionPerformed
        全服发送邮件();
    }//GEN-LAST:event_全服发送ActionPerformed

    private void 附件3代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_附件3代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_附件3代码ActionPerformed

    private void 确认全服ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_确认全服ActionPerformed
        全服发送邮件1();
    }//GEN-LAST:event_确认全服ActionPerformed

    private void 在线发送ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_在线发送ActionPerformed
        全服在线发送邮件();
    }//GEN-LAST:event_在线发送ActionPerformed

    private void 随机发送ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_随机发送ActionPerformed
        全服随机发送邮件();
    }//GEN-LAST:event_随机发送ActionPerformed

    private void 清理所有邮件ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_清理所有邮件ActionPerformed
        JOptionPane.showMessageDialog(null, "未启用。");
    }//GEN-LAST:event_清理所有邮件ActionPerformed

    private void 重生升级开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_重生升级开关ActionPerformed
        按键开关("重生升级开关", 2129);
        刷新重生升级开关();
    }//GEN-LAST:event_重生升级开关ActionPerformed

    private void 永恒升级开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_永恒升级开关ActionPerformed
        按键开关("永恒升级开关", 2128);
        刷新永恒升级开关();
    }//GEN-LAST:event_永恒升级开关ActionPerformed
    public void 刷新装备升级经验表() {
        for (int i = this.升级经验信息.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.升级经验信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id > 100000 && id < 200000  ");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 升级经验信息.getModel()).insertRow(升级经验信息.getRowCount(), new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("Val")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        升级经验信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 升级经验信息.getSelectedRow();
                String a = 升级经验信息.getValueAt(i, 0).toString();
                String a1 = 升级经验信息.getValueAt(i, 1).toString();
                String a2 = 升级经验信息.getValueAt(i, 2).toString();
                装备升级序号.setText(a);
                装备升级等级.setText(a1);
                装备升级经验.setText(a2);
            }
        });
    }

    public void 刷新装备升级设置表() {
        for (int i = this.装备升级设置.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.装备升级设置.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id >= 200000 && id <300000  ");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 装备升级设置.getModel()).insertRow(装备升级设置.getRowCount(), new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("Val")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        装备升级设置.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 装备升级设置.getSelectedRow();
                String a = 装备升级设置.getValueAt(i, 0).toString();
                String a1 = 装备升级设置.getValueAt(i, 1).toString();
                String a2 = 装备升级设置.getValueAt(i, 2).toString();
                装备升级序号1.setText(a);
                装备升级等级1.setText(a1);
                装备升级经验1.setText(a2);
            }
        });
    }
    private void 刷新经验表ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新经验表ActionPerformed
        刷新装备升级经验表();
    }//GEN-LAST:event_刷新经验表ActionPerformed

    private void 永恒升级开关1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_永恒升级开关1ActionPerformed

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.装备升级序号1.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.装备升级序号1.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + this.装备升级经验1.getText() + "' where id= " + this.装备升级序号1.getText() + ";";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    刷新装备升级设置表();
                    gui.Start.GetConfigValues();
                    JOptionPane.showMessageDialog(null, "修改成功已经生效");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要修改的值");
        }
    }//GEN-LAST:event_永恒升级开关1ActionPerformed

    private void 刷新设置表ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新设置表ActionPerformed
        刷新装备升级设置表();
    }//GEN-LAST:event_刷新设置表ActionPerformed

    private void 永恒升级开关2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_永恒升级开关2ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.装备升级序号.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.装备升级序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + this.装备升级经验.getText() + "' where id= " + this.装备升级序号.getText() + ";";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    刷新装备升级经验表();
                    gui.Start.GetConfigValues();
                    JOptionPane.showMessageDialog(null, "修改成功已经生效");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要修改的值");
        }
    }//GEN-LAST:event_永恒升级开关2ActionPerformed
    public void 刷新技能值列表() {
        for (int i = this.全屏设置表.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.全屏设置表.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM 技能范围检测 ");
            rs = ps.executeQuery();
            while (rs.next()) {
                MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath","wz") + "/String.wz"));
                MapleData itemsData;
                int itemId;
                String itemName = "";
                itemsData = data.getData("Skill.img");
                for (MapleData itemFolder : itemsData.getChildren()) {
                    itemId = Integer.parseInt(itemFolder.getName());
                    if (rs.getInt("name") == itemId) {
                        itemName = MapleDataTool.getString("name", itemFolder, "NO-NAME");
                    }
                }
                ((DefaultTableModel) 全屏设置表.getModel()).insertRow(全屏设置表.getRowCount(), new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("Val"),
                    itemName
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        全屏设置表.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 全屏设置表.getSelectedRow();
                String a = 全屏设置表.getValueAt(i, 0).toString();
                String a1 = 全屏设置表.getValueAt(i, 1).toString();
                String a2 = 全屏设置表.getValueAt(i, 2).toString();
                技能检测编号.setText(a);
                技能检测地图.setText(a1);
                技能检测数值.setText(a2);
            }
        });
        读取技能范围检测();
        // 读取地图吸怪检测();
    }
    private void 地图名称开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_地图名称开关ActionPerformed
        按键开关("地图名称开关", 2136);
        刷新地图名称开关();
    }//GEN-LAST:event_地图名称开关ActionPerformed

    private void 段数检测开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_段数检测开关ActionPerformed
        按键开关("段数检测开关", 2138);
        刷新段数检测开关();

    }//GEN-LAST:event_段数检测开关ActionPerformed

    private void 群攻检测开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_群攻检测开关ActionPerformed
        按键开关("群攻检测开关", 2139);
        刷新群攻检测开关();
    }//GEN-LAST:event_群攻检测开关ActionPerformed

    private void 修改骑士团等级上限1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改骑士团等级上限1ActionPerformed
        if (雇佣持续时间.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.雇佣持续时间.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 995);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.雇佣持续时间.getText() + "' where id = 995;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新雇佣持续时间();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改骑士团等级上限1ActionPerformed

    private void 过图存档开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_过图存档开关ActionPerformed
        按键开关("过图存档开关", 2140);
        刷新过图存档时间();
    }//GEN-LAST:event_过图存档开关ActionPerformed

    private void 修改冒险家等级上限1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改冒险家等级上限1ActionPerformed
        if (机器码多开数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.机器码多开数量.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 2301);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.机器码多开数量.getText() + "' where id = 2064;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新多开数量();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改冒险家等级上限1ActionPerformed

    private void 修改骑士团等级上限2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改骑士团等级上限2ActionPerformed
        if (IP多开数量.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.IP多开数量.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 2301);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.IP多开数量.getText() + "' where id = 2063;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新多开数量();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改骑士团等级上限2ActionPerformed

    private void 挂机检测开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_挂机检测开关ActionPerformed
        按键开关("挂机检测开关", 2141);
        刷新挂机检测开关();
    }//GEN-LAST:event_挂机检测开关ActionPerformed

    private void 修改骑士团等级上限3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改骑士团等级上限3ActionPerformed
        if (疲劳值.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.疲劳值.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 2143);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.疲劳值.getText() + "' where id = 2143;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新每日疲劳值();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改骑士团等级上限3ActionPerformed

    private void 附魔提醒开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_附魔提醒开关ActionPerformed
        按键开关("附魔提醒开关", 2144);
        刷新附魔提醒开关();
    }//GEN-LAST:event_附魔提醒开关ActionPerformed

    private void 仙人模式开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_仙人模式开关ActionPerformed
        按键开关("仙人模式开关", 2145);
        刷新仙人模式开关();
    }//GEN-LAST:event_仙人模式开关ActionPerformed

    private void 全屏检测开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全屏检测开关ActionPerformed
        按键开关("全屏检测开关", 2131);
        刷新全屏检测开关();
    }//GEN-LAST:event_全屏检测开关ActionPerformed

    private void 吸怪检测开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_吸怪检测开关ActionPerformed
        按键开关("吸怪检测开关", 2130);
        刷新吸怪检测开关();
    }//GEN-LAST:event_吸怪检测开关ActionPerformed

    private void 加速检测开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_加速检测开关ActionPerformed
        按键开关("加速检测开关", 2146);
        刷新加速检测开关();
    }//GEN-LAST:event_加速检测开关ActionPerformed

    private void 技能惩罚开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_技能惩罚开关ActionPerformed
        按键开关("技能惩罚开关", 2133);
        刷新技能惩罚开关();
        读取技能范围检测();
    }//GEN-LAST:event_技能惩罚开关ActionPerformed

    private void 全局检测开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全局检测开关ActionPerformed

    }//GEN-LAST:event_全局检测开关ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void 新增1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增1ActionPerformed
        if (技能检测地图.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "请填信息哦。");
            return;
        }
        int ID = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `id` FROM 技能范围检测  ORDER BY `id` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String SN = rs.getString("id");
                    int sns = Integer.parseInt(SN);
                    sns++;
                    ID = sns;
                    ps.close();
                }
            }
            ps.close();
        } catch (SQLException ex) {

        }
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO 技能范围检测 ( id,name,Val ) VALUES ( ? ,?,?)")) {
            ps.setInt(1, ID);
            ps.setString(2, 技能检测地图.getText());
            ps.setInt(3, Integer.parseInt(技能检测数值.getText()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        刷新技能值列表();
    }//GEN-LAST:event_新增1ActionPerformed

    private void 修改1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改1ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.技能检测编号.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE 技能范围检测 SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM 技能范围检测 WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.技能检测编号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update 技能范围检测 set Val = '" + this.技能检测数值.getText() + "' where id= " + this.技能检测编号.getText() + ";";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    刷新技能值列表();
                    JOptionPane.showMessageDialog(null, "修改成功已经生效");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要修改的值");
        }
    }//GEN-LAST:event_修改1ActionPerformed

    private void 搜索1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_搜索1ActionPerformed
        boolean result1 = this.技能检测地图.getText().matches("[0-9]+");

        if (!result1) {
            return;
        }
        for (int i = this.全屏设置表.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.全屏设置表.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM 技能范围检测 WHERE name = " + 技能检测地图.getText() + "");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 全屏设置表.getModel()).insertRow(全屏设置表.getRowCount(), new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("Val")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        全屏设置表.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 全屏设置表.getSelectedRow();
                String a = 全屏设置表.getValueAt(i, 0).toString();
                String a1 = 全屏设置表.getValueAt(i, 1).toString();
                String a2 = 全屏设置表.getValueAt(i, 2).toString();
                技能检测编号.setText(a);
                技能检测地图.setText(a1);
                技能检测数值.setText(a2);
            }
        });
        读取技能范围检测();
    }//GEN-LAST:event_搜索1ActionPerformed

    private void 技能调试开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_技能调试开关ActionPerformed
        按键开关("技能调试开关", 2132);
        刷新技能调试开关();
        读取技能范围检测();
    }//GEN-LAST:event_技能调试开关ActionPerformed

    private void 刷新全屏列表ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新全屏列表ActionPerformed
        刷新技能值列表();
    }//GEN-LAST:event_刷新全屏列表ActionPerformed

    private void 修改最高伤害ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改最高伤害ActionPerformed
        if (最高伤害.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "不能为空");
            return;
        }
        boolean result2 = this.最高伤害.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result2) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 2147);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    sqlString2 = "update configvalues set Val='" + this.最高伤害.getText() + "' where id = 2147;";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                    gui.Start.GetConfigValues();
                    刷新最高伤害();
                    JOptionPane.showMessageDialog(null, "修改成功");
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改最高伤害ActionPerformed

    private void 捡物检测开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_捡物检测开关ActionPerformed
        按键开关("捡物检测开关", 2148);
        刷新捡物检测开关();
    }//GEN-LAST:event_捡物检测开关ActionPerformed

    private void 个人发送邮件() {
        if (收件人.getText().equals("") || 收件人名字.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "请右边选择收件人。");
            return;
        }
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO mail (biaoti,juese,type1,shuliang1,type2,shuliang2,type3,shuliang3,wenben,shijian ) VALUES ( ?,?,?,?,?,?,?,?,?,? )")) {
            // ps.setInt(1, Integer.parseInt(收件人.getText()));
            ps.setString(1, 邮件标题.getText());
            ps.setInt(2, Integer.parseInt(收件人.getText()));
            ps.setInt(3, Integer.parseInt(附件1代码.getText()));
            ps.setInt(4, Integer.parseInt(附件1数量.getText()));
            ps.setInt(5, Integer.parseInt(附件2代码.getText()));
            ps.setInt(6, Integer.parseInt(附件2数量.getText()));
            ps.setInt(7, Integer.parseInt(附件3代码.getText()));
            ps.setInt(8, Integer.parseInt(附件3数量.getText()));
            ps.setString(9, 邮件文本.getText());
            ps.setString(10, CurrentReadable_Time());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "邮件发送成功。");
            MapleCharacterUtil.sendNote(收件人名字.getText(), 收件人名字.getText(), "[新邮件]" + 邮件标题.getText(), 0);
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private static int 全服发送确认 = 0;

    private void 全服发送邮件() {
        if (全服发送确认 == 0) {
            JOptionPane.showMessageDialog(null, "请先点击全服确认。");
            return;
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                try (Connection con1 = DatabaseConnection.getConnection();
                        PreparedStatement ps1 = con1.prepareStatement("INSERT INTO mail (biaoti,juese,type1,shuliang1,type2,shuliang2,type3,shuliang3,wenben,shijian ) VALUES ( ?,?,?,?,?,?,?,?,?,? )")) {
                    ps1.setString(1, 邮件标题.getText());
                    ps1.setInt(2, rs.getInt("id"));
                    ps1.setInt(3, Integer.parseInt(附件1代码.getText()));
                    ps1.setInt(4, Integer.parseInt(附件1数量.getText()));
                    ps1.setInt(5, Integer.parseInt(附件2代码.getText()));
                    ps1.setInt(6, Integer.parseInt(附件2数量.getText()));
                    ps1.setInt(7, Integer.parseInt(附件3代码.getText()));
                    ps1.setInt(8, Integer.parseInt(附件3数量.getText()));
                    ps1.setString(9, 邮件文本.getText());
                    ps1.setString(10, CurrentReadable_Time());
                    ps1.executeUpdate();
                    MapleCharacterUtil.sendNote(rs.getString("name"), rs.getString("name"), "[新邮件]" + 邮件标题.getText(), 0);
                } catch (SQLException ex) {
                }

            }
            JOptionPane.showMessageDialog(null, "邮件全服发送成功。");

        } catch (SQLException ex) {
        }
    }

    private void 全服随机发送邮件() {
        if (全服发送确认 == 0) {
            JOptionPane.showMessageDialog(null, "请先点击全服确认。");
            return;
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM `characters` ORDER BY RAND() LIMIT 0,100;  ");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                try (Connection con1 = DatabaseConnection.getConnection();
                        PreparedStatement ps1 = con1.prepareStatement("INSERT INTO mail (biaoti,juese,type1,shuliang1,type2,shuliang2,type3,shuliang3,wenben,shijian ) VALUES ( ?,?,?,?,?,?,?,?,?,? )")) {
                    ps1.setString(1, 邮件标题.getText());
                    ps1.setInt(2, rs.getInt("accountid"));
                    ps1.setInt(3, Integer.parseInt(附件1代码.getText()));
                    ps1.setInt(4, Integer.parseInt(附件1数量.getText()));
                    ps1.setInt(5, Integer.parseInt(附件2代码.getText()));
                    ps1.setInt(6, Integer.parseInt(附件2数量.getText()));
                    ps1.setInt(7, Integer.parseInt(附件3代码.getText()));
                    ps1.setInt(8, Integer.parseInt(附件3数量.getText()));
                    ps1.setString(9, 邮件文本.getText());
                    ps1.setString(10, CurrentReadable_Time());
                    ps1.executeUpdate();
                    MapleCharacterUtil.sendNote(rs.getString("name"), rs.getString("name"), "[新邮件]" + 邮件标题.getText(), 0);
                } catch (SQLException ex) {
                }
            }
            JOptionPane.showMessageDialog(null, "随机发送成功，随机发送给玩家：" + rs.getString("name"));
        } catch (SQLException ex) {
        }

    }

    private void 全服在线发送邮件() {
        if (全服发送确认 == 0) {
            JOptionPane.showMessageDialog(null, "请先点击全服确认。");
            return;
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (World.Find.findChannel(rs.getString("name")) > 0) {
                    try (Connection con1 = DatabaseConnection.getConnection();
                            PreparedStatement ps1 = con1.prepareStatement("INSERT INTO mail (biaoti,juese,type1,shuliang1,type2,shuliang2,type3,shuliang3,wenben,shijian ) VALUES ( ?,?,?,?,?,?,?,?,?,? )")) {
                        ps1.setString(1, 邮件标题.getText());
                        ps1.setInt(2, rs.getInt("accountid"));
                        ps1.setInt(3, Integer.parseInt(附件1代码.getText()));
                        ps1.setInt(4, Integer.parseInt(附件1数量.getText()));
                        ps1.setInt(5, Integer.parseInt(附件2代码.getText()));
                        ps1.setInt(6, Integer.parseInt(附件2数量.getText()));
                        ps1.setInt(7, Integer.parseInt(附件3代码.getText()));
                        ps1.setInt(8, Integer.parseInt(附件3数量.getText()));
                        ps1.setString(9, 邮件文本.getText());
                        ps1.setString(10, CurrentReadable_Time());
                        ps1.executeUpdate();
                        MapleCharacterUtil.sendNote(rs.getString("name"), rs.getString("name"), "[新邮件]" + 邮件标题.getText(), 0);
                    } catch (SQLException ex) {
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "邮件全服在线发送成功。");
        } catch (SQLException ex) {
        }
    }

    private void 全服发送邮件1() {
        if (全服发送确认 > 0) {
            JOptionPane.showMessageDialog(null, "你已经确认过了。");
            return;
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                try (Connection con1 = DatabaseConnection.getConnection();
                        PreparedStatement ps1 = con1.prepareStatement("INSERT INTO mail (biaoti,juese,type1,shuliang1,type2,shuliang2,type3,shuliang3,wenben,shijian ) VALUES ( ?,?,?,?,?,?,?,?,?,? )")) {
                    ps1.setString(1, 邮件标题.getText());
                    ps1.setInt(2, 0);
                    ps1.setInt(3, 0);
                    ps1.setInt(4, 0);
                    ps1.setInt(5, 0);
                    ps1.setInt(6, 0);
                    ps1.setInt(7, 0);
                    ps1.setInt(8, 0);
                    ps1.setString(9, "测试确认");
                    ps1.setString(10, CurrentReadable_Time());
                    ps1.executeUpdate();
                } catch (SQLException ex) {
                }

            }
            全服发送确认++;
            JOptionPane.showMessageDialog(null, "邮件全服发送确认成功。");
            PreparedStatement ps1 = null;
            ResultSet rs1 = null;
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM mail WHERE juese = ?");
                ps1.setInt(1, 0);
                rs1 = ps1.executeQuery();
                if (rs1.next()) {
                    String sqlstr = " delete from mail where juese = 0";
                    ps1.executeUpdate(sqlstr);
                }
            } catch (SQLException ex) {

            }
        } catch (SQLException ex) {
        }
    }

    private void 刷新角色信息() {
        for (int i = this.角色信息.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM characters order by id desc");
            rs = ps.executeQuery();

            while (rs.next()) {
                ((DefaultTableModel) 角色信息.getModel()).insertRow(角色信息.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("level"),});

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色信息.getSelectedRow();
                String a = 角色信息.getValueAt(i, 0).toString();
                String a1 = 角色信息.getValueAt(i, 1).toString();
                收件人.setText(a);
                收件人名字.setText(a1);
            }
        });
    }

    private void 刷新在线角色信息() {
        for (int i = this.角色信息.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM characters order by id desc");
            rs = ps.executeQuery();

            while (rs.next()) {
                if (World.Find.findChannel(rs.getString("name")) > 0) {
                    ((DefaultTableModel) 角色信息.getModel()).insertRow(角色信息.getRowCount(), new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("level"),});
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色信息.getSelectedRow();
                String a = 角色信息.getValueAt(i, 0).toString();
                String a1 = 角色信息.getValueAt(i, 1).toString();
                收件人.setText(a);
                收件人名字.setText(a1);
            }
        });
    }

    private void 刷新离线角色信息() {
        for (int i = this.角色信息.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM characters order by id desc");
            rs = ps.executeQuery();

            while (rs.next()) {
                if (World.Find.findChannel(rs.getString("name")) <= 0) {
                    ((DefaultTableModel) 角色信息.getModel()).insertRow(角色信息.getRowCount(), new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("level"),});
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色信息.getSelectedRow();
                String a = 角色信息.getValueAt(i, 0).toString();
                String a1 = 角色信息.getValueAt(i, 1).toString();
                收件人.setText(a);
                收件人名字.setText(a1);
            }
        });
    }

    private void 刷新公告广播() {
        for (int i = this.广播信息.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.广播信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM 广播信息");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 广播信息.getModel()).insertRow(广播信息.getRowCount(), new Object[]{
                    rs.getString("id"),
                    rs.getString("广播")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        广播信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 广播信息.getSelectedRow();
                String a = 广播信息.getValueAt(i, 0).toString();
                String a1 = 广播信息.getValueAt(i, 1).toString();
                广播序号.setText(a);
                广播文本.setText(a1);
            }
        });
    }

    private void 刷新屠令广播开关() {
        String 屠令广播显示 = "";
        int 屠令广播 = gui.Start.ConfigValuesMap.get("屠令广播开关");
        if (屠令广播 <= 0) {
            屠令广播显示 = "屠令广播:开启";
        } else {
            屠令广播显示 = "屠令广播:关闭";
        }
        屠令广播开关(屠令广播显示);
    }

    private void 刷新账号注册() {
        String 账号注册显示 = "";
        int 账号注册 = gui.Start.ConfigValuesMap.get("账号注册开关");
        if (账号注册 <= 0) {
            账号注册显示 = "账号注册:开启";
        } else {
            账号注册显示 = "账号注册:关闭";
        }
        游戏账号注册(账号注册显示);
    }

    private void 刷新升级快讯() {
        String 升级快讯显示 = "";
        int 升级快讯 = gui.Start.ConfigValuesMap.get("升级快讯开关");
        if (升级快讯 <= 0) {
            升级快讯显示 = "升级快讯:开启";
        } else {
            升级快讯显示 = "升级快讯:关闭";
        }
        游戏升级快讯(升级快讯显示);
    }

    private void 刷新冒险家职业开关() {
        String 冒险家职业开关显示 = "";
        int 冒险家职业开关 = gui.Start.ConfigValuesMap.get("冒险家职业开关");
        if (冒险家职业开关 <= 0) {
            冒险家职业开关显示 = "冒险家:开启";
        } else {
            冒险家职业开关显示 = "冒险家:关闭";
        }
        游戏冒险家职业开关(冒险家职业开关显示);
    }

    private void 刷新骑士团职业开关() {
        String 骑士团职业开关显示 = "";
        int 骑士团职业开关 = gui.Start.ConfigValuesMap.get("骑士团职业开关");
        if (骑士团职业开关 <= 0) {
            骑士团职业开关显示 = "骑士团:开启";
        } else {
            骑士团职业开关显示 = "骑士团:关闭";
        }
        游戏骑士团职业开关(骑士团职业开关显示);
    }

    private void 刷新战神职业开关() {
        String 战神职业开关显示 = "";
        int 战神职业开关 = gui.Start.ConfigValuesMap.get("战神职业开关");
        if (战神职业开关 <= 0) {
            战神职业开关显示 = "战   神:开启";
        } else {
            战神职业开关显示 = "战   神:关闭";
        }
        游戏战神职业开关(战神职业开关显示);
    }

    private void 刷新管理隐身开关() {
        String 刷新管理隐身开关显示 = "";
        int 管理隐身开关 = gui.Start.ConfigValuesMap.get("管理隐身开关");
        if (管理隐身开关 <= 0) {
            刷新管理隐身开关显示 = "管理隐身:开启";
        } else {
            刷新管理隐身开关显示 = "管理隐身:关闭";
        }
        管理隐身开关(刷新管理隐身开关显示);
    }

    private void 刷新管理加速开关() {
        String 刷新管理加速开关显示 = "";
        int 管理加速开关 = gui.Start.ConfigValuesMap.get("管理加速开关");
        if (管理加速开关 <= 0) {
            刷新管理加速开关显示 = "管理加速:开启";
        } else {
            刷新管理加速开关显示 = "管理加速:关闭";
        }
        管理加速开关(刷新管理加速开关显示);
    }

    private void 刷新游戏指令开关() {
        String 刷新游戏指令开关显示 = "";
        int 游戏指令开关 = gui.Start.ConfigValuesMap.get("游戏指令开关");
        if (游戏指令开关 <= 0) {
            刷新游戏指令开关显示 = "游戏指令:开启";
        } else {
            刷新游戏指令开关显示 = "游戏指令:关闭";
        }
        游戏指令开关(刷新游戏指令开关显示);
    }

    private void 刷新滚动公告开关() {
        String 刷新滚动公告开关显示 = "";
        int 滚动公告开关 = gui.Start.ConfigValuesMap.get("滚动公告开关");
        if (滚动公告开关 <= 0) {
            刷新滚动公告开关显示 = "滚动公告:开启";
        } else {
            刷新滚动公告开关显示 = "滚动公告:关闭";
        }
        滚动公告开关(刷新滚动公告开关显示);
    }

    private void 刷新游戏喇叭开关() {
        String 刷新游戏喇叭开关显示 = "";
        int 游戏喇叭开关 = gui.Start.ConfigValuesMap.get("游戏喇叭开关");
        if (游戏喇叭开关 <= 0) {
            刷新游戏喇叭开关显示 = "游戏喇叭:开启";
        } else {
            刷新游戏喇叭开关显示 = "游戏喇叭:关闭";
        }
        游戏喇叭开关(刷新游戏喇叭开关显示);
    }

    private void 刷新脚本显码开关() {
        String 刷新脚本显码开关显示 = "";
        int 脚本显码开关 = gui.Start.ConfigValuesMap.get("脚本显码开关");
        if (脚本显码开关 <= 0) {
            刷新脚本显码开关显示 = "脚本显码:开启";
        } else {
            刷新脚本显码开关显示 = "脚本显码:关闭";
        }
        脚本显码开关(刷新脚本显码开关显示);
    }

    private void 刷新丢出金币开关() {
        String 刷新丢出金币开关显示 = "";
        int 丢出金币开关 = gui.Start.ConfigValuesMap.get("丢出金币开关");
        if (丢出金币开关 <= 0) {
            刷新丢出金币开关显示 = "丢出金币:开启";
        } else {
            刷新丢出金币开关显示 = "丢出金币:关闭";
        }
        丢出金币开关(刷新丢出金币开关显示);
    }

    private void 刷新玩家交易开关() {
        String 刷新玩家交易开关显示 = "";
        int 玩家交易开关 = gui.Start.ConfigValuesMap.get("玩家交易开关");
        if (玩家交易开关 <= 0) {
            刷新玩家交易开关显示 = "玩家交易:开启";
        } else {
            刷新玩家交易开关显示 = "玩家交易:关闭";
        }
        玩家交易开关(刷新玩家交易开关显示);
    }

    private void 刷新丢出物品开关() {
        String 刷新丢出物品开关显示 = "";
        int 丢出物品开关 = gui.Start.ConfigValuesMap.get("丢出物品开关");
        if (丢出物品开关 <= 0) {
            刷新丢出物品开关显示 = "丢出物品:开启";
        } else {
            刷新丢出物品开关显示 = "丢出物品:关闭";
        }
        丢出物品开关(刷新丢出物品开关显示);
    }

    private void 刷新禁止登陆开关() {
        String 刷新禁止登陆开关显示 = "";
        int 禁止登陆开关 = gui.Start.ConfigValuesMap.get("禁止登陆开关");
        if (禁止登陆开关 <= 0) {
            刷新禁止登陆开关显示 = "游戏登陆:禁止";
        } else {
            刷新禁止登陆开关显示 = "游戏登陆:通行";
        }
        禁止登陆开关(刷新禁止登陆开关显示);
    }

    private void 刷新全服决斗开关() {
        String 刷新全服决斗开关显示 = "";
        int 全服决斗开关 = gui.Start.ConfigValuesMap.get("全服决斗开关");
        if (全服决斗开关 <= 0) {
            刷新全服决斗开关显示 = "全服决斗:开启";
        } else {
            刷新全服决斗开关显示 = "全服决斗:关闭";
        }
        全服决斗开关(刷新全服决斗开关显示);
    }

    private void 刷新欢迎弹窗开关() {
        String 刷新欢迎弹窗开关显示 = "";
        int 欢迎弹窗开关 = gui.Start.ConfigValuesMap.get("欢迎弹窗开关");
        if (欢迎弹窗开关 <= 0) {
            刷新欢迎弹窗开关显示 = "欢迎弹窗:开启";
        } else {
            刷新欢迎弹窗开关显示 = "欢迎弹窗:关闭";
        }
        欢迎弹窗开关(刷新欢迎弹窗开关显示);
    }

    private void 刷新游戏仓库开关() {
        String 刷新游戏仓库开关显示 = "";
        int 游戏仓库开关 = gui.Start.ConfigValuesMap.get("游戏仓库开关");
        if (游戏仓库开关 <= 0) {
            刷新游戏仓库开关显示 = "游戏仓库:开启";
        } else {
            刷新游戏仓库开关显示 = "游戏仓库:关闭";
        }
        游戏仓库开关(刷新游戏仓库开关显示);
    }

    private void 刷新登陆记录开关() {
        String 刷新登录记录开关显示 = "";
        int 登录记录开关 = gui.Start.ConfigValuesMap.get("登陆记录开关");
        if (登录记录开关 <= 0) {
            刷新登录记录开关显示 = "登陆记录:开启";
        } else {
            刷新登录记录开关显示 = "登陆记录:关闭";
        }
        登陆记录开关(刷新登录记录开关显示);
    }

    private void 刷新随身仓库开关() {
        String 刷新随身仓库开关显示 = "";
        int 随身仓库开关 = gui.Start.ConfigValuesMap.get("随身仓库开关");
        if (随身仓库开关 <= 0) {
            刷新随身仓库开关显示 = "随身仓库:开启";
        } else {
            刷新随身仓库开关显示 = "随身仓库:关闭";
        }
        随身仓库开关(刷新随身仓库开关显示);
    }

    private void 刷新聊天记录开关() {
        String 刷新聊天记录开关显示 = "";
        int 聊天记录开关 = gui.Start.ConfigValuesMap.get("聊天记录开关");
        if (聊天记录开关 <= 0) {
            刷新聊天记录开关显示 = "聊天记录:开启";
        } else {
            刷新聊天记录开关显示 = "聊天记录:关闭";
        }
        聊天记录开关(刷新聊天记录开关显示);
    }

    private void 刷新登陆验证开关() {
        String 刷新登陆验证开关显示 = "";
        int 登陆验证开关 = gui.Start.ConfigValuesMap.get("登陆验证开关");
        if (登陆验证开关 <= 0) {
            刷新登陆验证开关显示 = "登陆验证:开启";
        } else {
            刷新登陆验证开关显示 = "登陆验证:关闭";
        }
        登陆验证开关(刷新登陆验证开关显示);
    }

    private void 刷新拍卖行开关() {
        String 刷新拍卖行开关显示 = "";
        int 拍卖行开关 = gui.Start.ConfigValuesMap.get("拍卖行开关");
        if (拍卖行开关 <= 0) {
            刷新拍卖行开关显示 = "拍卖行:开启";
        } else {
            刷新拍卖行开关显示 = "拍卖行:关闭";
        }
        拍卖行开关(刷新拍卖行开关显示);
    }

    private void 刷新雇佣商人开关() {
        String 刷新雇佣商人开关显示 = "";
        int 雇佣商人开关 = gui.Start.ConfigValuesMap.get("雇佣商人开关");
        if (雇佣商人开关 <= 0) {
            刷新雇佣商人开关显示 = "雇佣商人:开启";
        } else {
            刷新雇佣商人开关显示 = "雇佣商人:关闭";
        }
        雇佣商人开关(刷新雇佣商人开关显示);
    }

    private void 刷新上线提醒开关() {
        String 刷新上线提醒开关显示 = "";
        int 上线提醒开关 = gui.Start.ConfigValuesMap.get("上线提醒开关");
        if (上线提醒开关 <= 0) {
            刷新上线提醒开关显示 = "上线提醒:开启";
        } else {
            刷新上线提醒开关显示 = "上线提醒:关闭";
        }
        上线提醒开关(刷新上线提醒开关显示);
    }

    private void 刷新巅峰广播开关() {
        String 刷新巅峰广播开关显示 = "";
        int 巅峰广播开关 = gui.Start.ConfigValuesMap.get("巅峰广播开关");
        if (巅峰广播开关 <= 0) {
            刷新巅峰广播开关显示 = "巅峰广播:开启";
        } else {
            刷新巅峰广播开关显示 = "巅峰广播:关闭";
        }
        巅峰广播开关(刷新巅峰广播开关显示);
    }

    private void 刷新玩家聊天开关() {
        String 刷新玩家聊天开关显示 = "";
        int 玩家聊天开关 = gui.Start.ConfigValuesMap.get("玩家聊天开关");
        if (玩家聊天开关 <= 0) {
            刷新玩家聊天开关显示 = "玩家聊天:开启";
        } else {
            刷新玩家聊天开关显示 = "玩家聊天:关闭";
        }
        玩家聊天开关(刷新玩家聊天开关显示);
    }

    private void 刷新回收地图开关() {
        String 刷新回收地图开关显示 = "";
        int 回收地图开关 = gui.Start.ConfigValuesMap.get("回收地图开关");
        if (回收地图开关 <= 0) {
            刷新回收地图开关显示 = "回收地图:开启";
        } else {
            刷新回收地图开关显示 = "回收地图:关闭";
        }
        回收地图开关(刷新回收地图开关显示);
    }

    private void 刷新指令通知开关() {
        String 刷新指令通知开关显示 = "";
        int 指令通知开关 = gui.Start.ConfigValuesMap.get("指令通知开关");
        if (指令通知开关 <= 0) {
            刷新指令通知开关显示 = "指令通知:开启";
        } else {
            刷新指令通知开关显示 = "指令通知:关闭";
        }
        指令通知开关(刷新指令通知开关显示);
    }

    private void 刷新下雪天开关() {
        String 刷新下雪天开关显示 = "";
        int 下雪天开关 = gui.Start.ConfigValuesMap.get("下雪天开关");
        if (下雪天开关 <= 0) {
            刷新下雪天开关显示 = "下雪天:开启";
        } else {
            刷新下雪天开关显示 = "下雪天:关闭";
        }
        下雪天开关(刷新下雪天开关显示);
    }

    private void 刷新下红花开关() {
        String 刷新下红花开关显示 = "";
        int 下红花开关 = gui.Start.ConfigValuesMap.get("下红花开关");
        if (下红花开关 <= 0) {
            刷新下红花开关显示 = "下红花:开启";
        } else {
            刷新下红花开关显示 = "下红花:关闭";
        }
        下红花开关(刷新下红花开关显示);
    }

    private void 刷新下气泡开关() {
        String 刷新下气泡开关显示 = "";
        int 下气泡开关 = gui.Start.ConfigValuesMap.get("下气泡开关");
        if (下气泡开关 <= 0) {
            刷新下气泡开关显示 = "下气泡:开启";
        } else {
            刷新下气泡开关显示 = "下气泡:关闭";
        }
        下气泡开关(刷新下气泡开关显示);
    }

    private void 刷新下雪花开关() {
        String 刷新下雪花开关显示 = "";
        int 下雪花开关 = gui.Start.ConfigValuesMap.get("下雪花开关");
        if (下雪花开关 <= 0) {
            刷新下雪花开关显示 = "下雪花:开启";
        } else {
            刷新下雪花开关显示 = "下雪花:关闭";
        }
        下雪花开关(刷新下雪花开关显示);
    }

    private void 刷新下枫叶开关() {
        String 刷新下枫叶开关显示 = "";
        int 下枫叶开关 = gui.Start.ConfigValuesMap.get("下枫叶开关");
        if (下枫叶开关 <= 0) {
            刷新下枫叶开关显示 = "下枫叶:开启";
        } else {
            刷新下枫叶开关显示 = "下枫叶:关闭";
        }
        下枫叶开关(刷新下枫叶开关显示);
    }

    public void 刷新自添加NPC() {
        for (int i = this.自添加NPC.getModel().getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.自添加NPC.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM 游戏npc管理");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 自添加NPC.getModel()).insertRow(自添加NPC.getRowCount(), new Object[]{rs.getInt("mid"), rs.getString("dataid")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                    String sqlString3 = null;
                    String sqlString4 = null;
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
                    String sqlString3 = null;
                    String sqlString4 = null;
                    sqlString2 = "update configvalues set Val= '1' where id='" + b + "';";
                    PreparedStatement dropperid = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    dropperid.executeUpdate(sqlString2);
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /*if (检测开关 <= 0) {
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
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, b);
                ps.setString(2, a);
                ps.setInt(3, 1);
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, b);
                ps.setString(2, a);
                ps.setInt(3, 0);
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(控制台1号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        gui.Start.GetConfigValues();
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new 控制台1号().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton IP多开开关;
    private javax.swing.JTextField IP多开数量;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JLabel jLabel267;
    private javax.swing.JLabel jLabel268;
    private javax.swing.JLabel jLabel269;
    private javax.swing.JLabel jLabel270;
    private javax.swing.JLabel jLabel271;
    private javax.swing.JLabel jLabel281;
    private javax.swing.JLabel jLabel285;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel330;
    private javax.swing.JLabel jLabel331;
    private javax.swing.JLabel jLabel332;
    private javax.swing.JLabel jLabel333;
    private javax.swing.JLabel jLabel334;
    private javax.swing.JLabel jLabel335;
    private javax.swing.JLabel jLabel336;
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane106;
    private javax.swing.JScrollPane jScrollPane107;
    private javax.swing.JScrollPane jScrollPane108;
    private javax.swing.JScrollPane jScrollPane136;
    private javax.swing.JScrollPane jScrollPane137;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JPanel 一类设置;
    private javax.swing.JButton 上线提醒开关;
    private javax.swing.JButton 下枫叶开关;
    private javax.swing.JButton 下气泡开关;
    private javax.swing.JButton 下红花开关;
    private javax.swing.JButton 下雪天开关;
    private javax.swing.JButton 下雪花开关;
    private javax.swing.JButton 丢出物品开关;
    private javax.swing.JButton 丢出金币开关;
    private javax.swing.JButton 个人发送;
    private javax.swing.JButton 仙人模式开关;
    private javax.swing.JButton 修改1;
    private javax.swing.JButton 修改冒险家等级上限;
    private javax.swing.JButton 修改冒险家等级上限1;
    private javax.swing.JButton 修改广播;
    private javax.swing.JButton 修改最高伤害;
    private javax.swing.JButton 修改服务端最大人数;
    private javax.swing.JButton 修改角色数量;
    private javax.swing.JButton 修改账号数量;
    private javax.swing.JButton 修改骑士团等级上限;
    private javax.swing.JButton 修改骑士团等级上限1;
    private javax.swing.JButton 修改骑士团等级上限2;
    private javax.swing.JButton 修改骑士团等级上限3;
    private javax.swing.JButton 全局检测开关;
    private javax.swing.JButton 全屏检测开关;
    private javax.swing.JTable 全屏设置表;
    private javax.swing.JButton 全服决斗开关;
    private javax.swing.JButton 全服发送;
    private javax.swing.JTextField 冒险家等级上限;
    private javax.swing.JButton 冒险家职业开关;
    private javax.swing.JTextField 删MAC代码;
    private javax.swing.JButton 删除IP;
    private javax.swing.JTextField 删除IP代码;
    private javax.swing.JButton 删除MAC;
    private javax.swing.JPanel 删除NPC;
    private javax.swing.JButton 删除广播;
    private javax.swing.JButton 删除自添加npc;
    private javax.swing.JTextField 删除自添加npc代码;
    private javax.swing.JButton 刷新全屏列表;
    private javax.swing.JButton 刷新封IP;
    private javax.swing.JButton 刷新封MAC;
    private javax.swing.JButton 刷新广告;
    private javax.swing.JButton 刷新经验表;
    private javax.swing.JButton 刷新自添加NPC;
    private javax.swing.JButton 刷新角色;
    private javax.swing.JButton 刷新设置表;
    private javax.swing.JButton 加速检测开关;
    private javax.swing.JTable 升级经验信息;
    private javax.swing.JButton 双爆频道开关;
    private javax.swing.JButton 发布广告;
    private javax.swing.JButton 名单拦截开关;
    private javax.swing.JButton 吸怪检测开关;
    private javax.swing.JButton 商城检测开关;
    private javax.swing.JButton 喷火龙开关;
    private javax.swing.JButton 回收地图开关;
    private javax.swing.JButton 在线发送;
    private javax.swing.JButton 在线角色;
    private javax.swing.JButton 地图名称开关;
    private javax.swing.JButton 大海龟开关;
    private javax.swing.JButton 大灰狼开关;
    private javax.swing.JTable 封IP;
    private javax.swing.JTable 封MAC;
    private javax.swing.JPanel 封禁IPMAC;
    private javax.swing.JButton 小白兔开关;
    private javax.swing.JButton 小青蛇开关;
    private javax.swing.JButton 屠令广播开关;
    private javax.swing.JButton 巅峰广播开关;
    private javax.swing.JTable 广播信息;
    private javax.swing.JTextField 广播序号;
    private javax.swing.JTextField 广播文本;
    private javax.swing.JButton 怪物状态开关;
    private javax.swing.JButton 战神职业开关;
    private javax.swing.JButton 技能惩罚开关;
    private javax.swing.JTextField 技能检测地图;
    private javax.swing.JTextField 技能检测数值;
    private javax.swing.JTextField 技能检测编号;
    private javax.swing.JPanel 技能范围检测;
    private javax.swing.JButton 技能调试开关;
    private javax.swing.JButton 拍卖行开关;
    private javax.swing.JButton 挂机检测开关;
    private javax.swing.JButton 指令通知开关;
    private javax.swing.JButton 捡物检测开关;
    private javax.swing.JButton 搜索1;
    private javax.swing.JTextField 收件人;
    private javax.swing.JTextField 收件人名字;
    private javax.swing.JButton 新增1;
    private javax.swing.JButton 星精灵开关;
    private javax.swing.JTextField 最高伤害;
    private javax.swing.JTextField 服务端最大人数;
    private javax.swing.JButton 机器人开关;
    private javax.swing.JButton 机器人注册开关;
    private javax.swing.JButton 机器多开开关;
    private javax.swing.JTextField 机器码多开数量;
    private javax.swing.JButton 欢迎弹窗开关;
    private javax.swing.JButton 段数检测开关;
    private javax.swing.JButton 永恒升级开关;
    private javax.swing.JButton 永恒升级开关1;
    private javax.swing.JButton 永恒升级开关2;
    private javax.swing.JButton 清理所有邮件;
    private javax.swing.JButton 游戏仓库开关;
    private javax.swing.JButton 游戏升级快讯;
    private javax.swing.JButton 游戏喇叭开关;
    private javax.swing.JPanel 游戏广播;
    private javax.swing.JButton 游戏找人开关;
    private javax.swing.JButton 游戏指令开关;
    private javax.swing.JButton 游戏经验加成说明;
    private javax.swing.JButton 游戏账号注册;
    private javax.swing.JButton 滚动公告开关;
    private javax.swing.JButton 漂漂猪开关;
    private javax.swing.JButton 火野猪开关;
    private javax.swing.JButton 玩家交易开关;
    private javax.swing.JButton 玩家聊天开关;
    private javax.swing.JTextField 疲劳值;
    private javax.swing.JButton 登陆保护开关;
    private javax.swing.JButton 登陆帮助开关;
    private javax.swing.JButton 登陆记录开关;
    private javax.swing.JButton 登陆队列开关;
    private javax.swing.JButton 登陆验证开关;
    private javax.swing.JButton 白雪人开关;
    private javax.swing.JButton 石头人开关;
    private javax.swing.JButton 确认全服;
    private javax.swing.JButton 禁止登陆开关;
    private javax.swing.JButton 离线角色;
    private javax.swing.JButton 章鱼怪开关;
    private javax.swing.JButton 管理加速开关;
    private javax.swing.JButton 管理隐身开关;
    private javax.swing.JButton 紫色猫开关;
    private javax.swing.JButton 红螃蟹开关;
    private javax.swing.JTable 经验加成表;
    private javax.swing.JButton 经验加成表修改;
    private javax.swing.JTextField 经验加成表序号;
    private javax.swing.JTextField 经验加成表数值;
    private javax.swing.JTextField 经验加成表类型;
    private javax.swing.JButton 绿水灵开关;
    private javax.swing.JButton 群攻检测开关;
    private javax.swing.JButton 聊天记录开关;
    private javax.swing.JButton 胖企鹅开关;
    private javax.swing.JButton 脚本显码开关;
    private javax.swing.JTable 自添加NPC;
    private javax.swing.JButton 花蘑菇开关;
    private javax.swing.JButton 蓝蜗牛开关;
    private javax.swing.JButton 蘑菇仔开关;
    private javax.swing.JTextField 装备升级序号;
    private javax.swing.JTextField 装备升级序号1;
    private javax.swing.JTextField 装备升级等级;
    private javax.swing.JTextField 装备升级等级1;
    private javax.swing.JTextField 装备升级经验;
    private javax.swing.JTextField 装备升级经验1;
    private javax.swing.JTable 装备升级设置;
    private javax.swing.JTable 角色信息;
    private javax.swing.JButton 越级打怪开关;
    private javax.swing.JButton 过图存档开关;
    private javax.swing.JTextPane 邮件文本;
    private javax.swing.JTextField 邮件标题;
    private javax.swing.JPanel 邮件系统;
    private javax.swing.JButton 重生升级开关;
    private javax.swing.JPanel 金锤子;
    private javax.swing.JButton 金锤子修改;
    private javax.swing.JTextField 金锤子序号;
    private javax.swing.JButton 金锤子提升上限;
    private javax.swing.JTextField 金锤子数值;
    private javax.swing.JTextField 金锤子类型;
    private javax.swing.JTable 金锤子表;
    private javax.swing.JTextField 附件1代码;
    private javax.swing.JTextField 附件1数量;
    private javax.swing.JTextField 附件2代码;
    private javax.swing.JTextField 附件2数量;
    private javax.swing.JTextField 附件3代码;
    private javax.swing.JTextField 附件3数量;
    private javax.swing.JButton 附魔提醒开关;
    private javax.swing.JTextField 限制创建角色数量;
    private javax.swing.JTextField 限制注册账号数量;
    private javax.swing.JButton 随机发送;
    private javax.swing.JButton 随身仓库开关;
    private javax.swing.JButton 雇佣商人开关;
    private javax.swing.JTextField 雇佣持续时间;
    private javax.swing.JButton 青鳄鱼开关;
    private javax.swing.JButton 顽皮猴开关;
    private javax.swing.JTextField 骑士团等级上限;
    private javax.swing.JButton 骑士团职业开关;
    // End of variables declaration//GEN-END:variables

    private class jTextField {

        public jTextField() {
        }
    }
}
