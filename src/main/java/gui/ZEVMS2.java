package gui;

import static a.本地数据库.任务更新下载目录;
import static a.本地数据库.任务更新导入目录;
import static a.本地数据库.任务更新解压目录;
import static a.用法大全.传输在线时长;
import static a.用法大全.关系验证;
import static a.用法大全.判断容纳人数;
import client.MapleCharacter;
import client.inventory.Equip;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import static gui.ZEVMS.jButton1111;
import static gui.ZEVMS.网关;
import static gui.ZEVMS.jButton11112;
import static gui.ZEVMS.jButton11113;
import static gui.ZEVMS.jButton11115;
import static gui.ZEVMS.服务端功能开关;
import static gui.ZEVMS.脚本编辑器2;
import gui.控制台.控制台3号;
import gui.控制台.活动控制台;
import gui.控制台.控制台2号;
import gui.控制台.充值卡后台;
import gui.控制台.控制台1号;
import gui.控制台.脚本编辑器2;
import handling.channel.ChannelServer;
import handling.login.handler.AutoRegister;
import handling.world.World;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.PortalScriptManager;
import scripting.ReactorScriptManager;
import server.CashItemFactory;
import server.CashItemInfo;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleShopFactory;
import server.life.MapleMonsterInformationProvider;
import tools.MaplePacketCreator;
import client.LoginCrypto;
import java.util.concurrent.ScheduledFuture;
import javax.swing.JDialog;
import javax.swing.UIManager;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;
import server.ShutdownServer;
import server.Timer;
import static handling.cashshop.handler.CashShopOperation.Gaincharacter7;
import static handling.cashshop.handler.CashShopOperation.Getcharacter7;
import handling.world.MapleParty;
import scripting.NPCConversationManager;
import static gui.QQMsgServer.sendMsgToQQGroup;
import static a.用法大全.获取到期时间;
import static gui.ZEVMS2.经济控制台;
import static abc.Game.窗口标题;
import static abc.sancu.FileDemo_05.删除文件;
import client.inventory.IItem;
import client.inventory.ItemLoader;
import database1.DatabaseConnection1;
import gui.Jieya.解压文件;
import static gui.Start.GetCloudBacklist;
import static gui.Start.读取技能PVP伤害;
import static gui.ZEVMS.下载文件;
import static gui.ZEVMS.快捷面板;
import static server.MapleCarnivalChallenge.getJobNameById;
import gui.网关.机器人群设置面板;
import static gui.ZEVMS.更多应用;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import server.MerchItemPackage;
import tools.Pair;

public final class ZEVMS2 extends javax.swing.JFrame {

    private static final MapleDataProvider Mobdata = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Mob.wz"));
    private static final MapleDataProvider npcData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Npc.wz"));
    private static final MapleDataProvider stringDataWZ = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz"));
    private static final MapleDataProvider etcDataWZ = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Etc.wz"));
    private static final MapleData mobStringData = stringDataWZ.getData("Mob.img");
    private static final MapleData npcStringData = stringDataWZ.getData("Npc.img");
    private static final MapleData npclocData = etcDataWZ.getData("NpcLocation.img");
    private static Object jButton11117;

    MapleDataProvider stringProvider = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz"));
    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
    MapleData cash = stringProvider.getData("Cash.img");
    MapleData consume = stringProvider.getData("Consume.img");
    MapleData eqp = stringProvider.getData("Eqp.img").getChildByPath("Eqp");
    MapleData etc = stringProvider.getData("Etc.img").getChildByPath("Etc");
    MapleData ins = stringProvider.getData("Ins.img");
    MapleData pet = stringProvider.getData("Pet.img");
    MapleData map = stringProvider.getData("Map.img");
    MapleData mob = stringProvider.getData("Mob.img");
    MapleData skill = stringProvider.getData("Skill.img");
    MapleData npc = stringProvider.getData("Npc.img");

    private MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Etc.wz"));
    private MapleData commodities = this.data.getData("Commodity.img");

    public ZEVMS2() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("gui/图片/pp/2.png"));
        setIconImage(icon.getImage());
        Properties 設定檔 = System.getProperties();
        String 用户类型 = "";
        if (MapleParty.容纳人数 == 999) {
            用户类型 = "授权用户";
        } else {
            用户类型 = "免费用户";
        }
       // setTitle("" + 窗口标题 + "  [主控制台，不可关闭]  [" + 用户类型 + "]");
        initComponents();
        initCharacterPannel();
        刷新商城扩充价格();
        刷新泡点金币开关();
        刷新泡点点券开关();
        刷新泡点经验开关();
        刷新泡点抵用开关();
        刷新泡点设置();
        刷新账号信息();
        刷新角色信息();
        刷新信息();
        刷新雇佣泡点设置();
        MapleParty.服务端 = 0;

        MapleParty.服务端启动中++;
        System.out.println("○ 主控制台启动完成\r\n\r\n");
        //new ZevmsLauncherServer(60000).start();
        //sendMsgToQQGroup("[服务端信息]:服务端启动成功，玩家可以正常登陆游戏。");
    }

    private void 刷新信息() {
        刷新账号();
        刷新人数限制();
        刷新账户类型();
        刷新游戏名字();
        刷新绑定地址();
    }

    private void 刷新绑定地址() {
     //   绑定地址.setText(关系验证(MapleParty.启动账号));
    }

    private void 刷新账户类型() {
        if (MapleParty.容纳人数 == 999) {
            剩余时间.setText("[授权用户] 无限制");
        } else {
            剩余时间.setText("[免费用户] " + 获取到期时间(MapleParty.启动账号));
        }

    }

    private void 刷新游戏名字() {
        游戏名字.setText("" + MapleParty.开服名字 + "");
    }

    private void 刷新账号() {
        启动账号.setText(MapleParty.启动账号);
    }

    private void 刷新人数限制() {
        if (MapleParty.容纳人数 == 999) {
            人数限制.setText("[授权用户] 无限制");
        } else {
            人数限制.setText("[免费用户] 限人数: " + 判断容纳人数(MapleParty.启动账号) + "");
        }
    }

    public void 删除SN(int a) {
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            ps2 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM sn" + a + " WHERE SN = ?");
            ps2.setInt(1, Integer.parseInt(this.商品编码.getText()));
            rs2 = ps2.executeQuery();
            if (rs2.next()) {
                String sqlstr = " delete from sn" + a + " where SN =" + Integer.parseInt(this.商品编码.getText()) + ";";
                ps2.executeUpdate(sqlstr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void 删除SN库存() {
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            ps2 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM character7 WHERE Name = ?");
            ps2.setInt(1, Integer.parseInt(this.商品编码.getText()));
            rs2 = ps2.executeQuery();
            if (rs2.next()) {
                String sqlstr = " delete from character7 where Name =" + Integer.parseInt(this.商品编码.getText()) + ";";
                ps2.executeUpdate(sqlstr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void 删除SN库存2() {
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            ps2 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM character7 WHERE Name = ?  &&  channel = 2");
            ps2.setInt(1, Integer.parseInt(this.商品编码.getText()));
            rs2 = ps2.executeQuery();
            if (rs2.next()) {
                String sqlstr = " delete from character7 where Name =" + Integer.parseInt(this.商品编码.getText()) + ";";
                ps2.executeUpdate(sqlstr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void 删除SN库存3() {
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            ps2 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM character7 WHERE Name = ?  &&  channel = 3");
            ps2.setInt(1, Integer.parseInt(this.商品编码.getText()));
            rs2 = ps2.executeQuery();
            if (rs2.next()) {
                String sqlstr = " delete from character7 where Name =" + Integer.parseInt(this.商品编码.getText()) + ";";
                ps2.executeUpdate(sqlstr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void 删除SN库存4() {
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {
            ps2 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM character7 WHERE Name = ?  &&  channel = 4");
            ps2.setInt(1, Integer.parseInt(this.商品编码.getText()));
            rs2 = ps2.executeQuery();
            if (rs2.next()) {
                String sqlstr = " delete from character7 where Name =" + Integer.parseInt(this.商品编码.getText()) + ";";
                ps2.executeUpdate(sqlstr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        首页 = new javax.swing.JPanel();
        jTabbedPane6 = new javax.swing.JTabbedPane();
        首页1 = new javax.swing.JPanel();
        首页LOGO = new javax.swing.JLabel();
        上次启动 = new javax.swing.JPanel();
        启动账号 = new javax.swing.JLabel();
        启动账号1 = new javax.swing.JLabel();
        人数限制 = new javax.swing.JLabel();
        上次启动时间2 = new javax.swing.JLabel();
        上次启动时间3 = new javax.swing.JLabel();
        剩余时间 = new javax.swing.JLabel();
        游戏名字 = new javax.swing.JLabel();
        启动账号3 = new javax.swing.JLabel();
        啊啊啊3 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        商品设置 = new javax.swing.JPanel();
        商城提示语言 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        charTable = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        无出售状态 = new javax.swing.JButton();
        NEW = new javax.swing.JButton();
        Sale = new javax.swing.JButton();
        HOT = new javax.swing.JButton();
        event = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        商品数量 = new javax.swing.JTextField();
        商品编码 = new javax.swing.JTextField();
        商品代码 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        商品价格 = new javax.swing.JTextField();
        商品时间 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        商品库存 = new javax.swing.JTextField();
        商品折扣 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        每日限购 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        货币类型 = new javax.swing.JTextField();
        jScrollPane132 = new javax.swing.JScrollPane();
        商城扩充价格 = new javax.swing.JTable();
        商城扩充价格修改 = new javax.swing.JTextField();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        主题馆 = new javax.swing.JButton();
        读取热销产品 = new javax.swing.JButton();
        活动 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        帽子 = new javax.swing.JButton();
        脸饰 = new javax.swing.JButton();
        眼饰 = new javax.swing.JButton();
        长袍 = new javax.swing.JButton();
        上衣 = new javax.swing.JButton();
        裙裤 = new javax.swing.JButton();
        鞋子 = new javax.swing.JButton();
        手套 = new javax.swing.JButton();
        武器 = new javax.swing.JButton();
        戒指 = new javax.swing.JButton();
        飞镖 = new javax.swing.JButton();
        披风 = new javax.swing.JButton();
        骑宠 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        喜庆物品 = new javax.swing.JButton();
        通讯物品 = new javax.swing.JButton();
        卷轴 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        会员卡 = new javax.swing.JButton();
        表情 = new javax.swing.JButton();
        个人商店 = new javax.swing.JButton();
        纪念日 = new javax.swing.JButton();
        游戏 = new javax.swing.JButton();
        效果 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        宠物 = new javax.swing.JButton();
        宠物服饰 = new javax.swing.JButton();
        其他 = new javax.swing.JButton();
        修改背包扩充价格 = new javax.swing.JButton();
        商品出售状态 = new javax.swing.JTextField();
        显示类型 = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        修改 = new javax.swing.JButton();
        添加 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        刷新模式 = new javax.swing.JButton();
        账号列表 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        账号信息 = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        抵用 = new javax.swing.JTextField();
        账号 = new javax.swing.JTextField();
        点券 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        修改账号点券抵用 = new javax.swing.JButton();
        账号ID = new javax.swing.JTextField();
        jLabel206 = new javax.swing.JLabel();
        jLabel312 = new javax.swing.JLabel();
        管理1 = new javax.swing.JTextField();
        jLabel353 = new javax.swing.JLabel();
        QQ = new javax.swing.JTextField();
        jLabel357 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        注册的账号 = new javax.swing.JTextField();
        注册的密码 = new javax.swing.JTextField();
        jButton35 = new javax.swing.JButton();
        jLabel111 = new javax.swing.JLabel();
        jLabel201 = new javax.swing.JLabel();
        jButton30 = new javax.swing.JButton();
        账号提示语言 = new javax.swing.JLabel();
        刷新账号信息 = new javax.swing.JButton();
        离线账号 = new javax.swing.JButton();
        解封 = new javax.swing.JButton();
        已封账号 = new javax.swing.JButton();
        在线账号 = new javax.swing.JButton();
        删除账号 = new javax.swing.JButton();
        封锁账号 = new javax.swing.JButton();
        解卡 = new javax.swing.JButton();
        账号操作 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        显示在线账号 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        清空QQ相关 = new javax.swing.JButton();
        角色列表 = new javax.swing.JPanel();
        jTabbedPane8 = new javax.swing.JTabbedPane();
        角色信息1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        角色信息 = new javax.swing.JTable();
        刷新角色信息 = new javax.swing.JButton();
        显示管理角色 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        删除角色 = new javax.swing.JButton();
        角色昵称 = new javax.swing.JTextField();
        等级 = new javax.swing.JTextField();
        力量 = new javax.swing.JTextField();
        敏捷 = new javax.swing.JTextField();
        智力 = new javax.swing.JTextField();
        运气 = new javax.swing.JTextField();
        HP = new javax.swing.JTextField();
        MP = new javax.swing.JTextField();
        金币 = new javax.swing.JTextField();
        地图 = new javax.swing.JTextField();
        GM = new javax.swing.JTextField();
        jLabel182 = new javax.swing.JLabel();
        jLabel183 = new javax.swing.JLabel();
        jLabel184 = new javax.swing.JLabel();
        jLabel185 = new javax.swing.JLabel();
        jLabel186 = new javax.swing.JLabel();
        jLabel187 = new javax.swing.JLabel();
        jLabel189 = new javax.swing.JLabel();
        jLabel190 = new javax.swing.JLabel();
        jLabel191 = new javax.swing.JLabel();
        jLabel192 = new javax.swing.JLabel();
        jLabel193 = new javax.swing.JLabel();
        角色ID = new javax.swing.JTextField();
        卡号自救1 = new javax.swing.JButton();
        卡号自救2 = new javax.swing.JButton();
        jLabel203 = new javax.swing.JLabel();
        查看技能 = new javax.swing.JButton();
        查看背包 = new javax.swing.JButton();
        卡家族解救 = new javax.swing.JButton();
        脸型 = new javax.swing.JTextField();
        发型 = new javax.swing.JTextField();
        jLabel214 = new javax.swing.JLabel();
        离线角色 = new javax.swing.JButton();
        在线角色 = new javax.swing.JButton();
        显示在线玩家 = new javax.swing.JLabel();
        角色背包 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel36 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        角色背包穿戴 = new javax.swing.JTable();
        背包物品名字1 = new javax.swing.JTextField();
        身上穿戴序号1 = new javax.swing.JTextField();
        背包物品代码1 = new javax.swing.JTextField();
        jLabel276 = new javax.swing.JLabel();
        jLabel283 = new javax.swing.JLabel();
        jLabel287 = new javax.swing.JLabel();
        删除穿戴装备 = new javax.swing.JButton();
        jPanel37 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        角色装备背包 = new javax.swing.JTable();
        装备背包物品名字 = new javax.swing.JTextField();
        装备背包物品序号 = new javax.swing.JTextField();
        装备背包物品代码 = new javax.swing.JTextField();
        jLabel288 = new javax.swing.JLabel();
        jLabel289 = new javax.swing.JLabel();
        jLabel290 = new javax.swing.JLabel();
        删除装备背包 = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        角色消耗背包 = new javax.swing.JTable();
        消耗背包物品名字 = new javax.swing.JTextField();
        消耗背包物品序号 = new javax.swing.JTextField();
        消耗背包物品代码 = new javax.swing.JTextField();
        jLabel291 = new javax.swing.JLabel();
        jLabel292 = new javax.swing.JLabel();
        jLabel293 = new javax.swing.JLabel();
        删除消耗背包 = new javax.swing.JButton();
        jPanel39 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        角色设置背包 = new javax.swing.JTable();
        设置背包物品名字 = new javax.swing.JTextField();
        设置背包物品序号 = new javax.swing.JTextField();
        设置背包物品代码 = new javax.swing.JTextField();
        jLabel294 = new javax.swing.JLabel();
        jLabel295 = new javax.swing.JLabel();
        jLabel296 = new javax.swing.JLabel();
        删除设置背包 = new javax.swing.JButton();
        jPanel40 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        角色其他背包 = new javax.swing.JTable();
        其他背包物品名字 = new javax.swing.JTextField();
        其他背包物品序号 = new javax.swing.JTextField();
        其他背包物品代码 = new javax.swing.JTextField();
        jLabel297 = new javax.swing.JLabel();
        jLabel298 = new javax.swing.JLabel();
        jLabel299 = new javax.swing.JLabel();
        删除其他背包 = new javax.swing.JButton();
        jPanel41 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        角色特殊背包 = new javax.swing.JTable();
        特殊背包物品名字 = new javax.swing.JTextField();
        特殊背包物品序号 = new javax.swing.JTextField();
        特殊背包物品代码 = new javax.swing.JTextField();
        jLabel300 = new javax.swing.JLabel();
        jLabel301 = new javax.swing.JLabel();
        jLabel302 = new javax.swing.JLabel();
        删除特殊背包 = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane21 = new javax.swing.JScrollPane();
        角色游戏仓库 = new javax.swing.JTable();
        游戏仓库物品名字 = new javax.swing.JTextField();
        游戏仓库物品序号 = new javax.swing.JTextField();
        游戏仓库物品代码 = new javax.swing.JTextField();
        jLabel303 = new javax.swing.JLabel();
        jLabel304 = new javax.swing.JLabel();
        jLabel305 = new javax.swing.JLabel();
        删除游戏仓库 = new javax.swing.JButton();
        jPanel43 = new javax.swing.JPanel();
        jScrollPane22 = new javax.swing.JScrollPane();
        角色商城仓库 = new javax.swing.JTable();
        商城仓库物品名字 = new javax.swing.JTextField();
        商城仓库物品序号 = new javax.swing.JTextField();
        商城仓库物品代码 = new javax.swing.JTextField();
        jLabel306 = new javax.swing.JLabel();
        jLabel307 = new javax.swing.JLabel();
        jLabel308 = new javax.swing.JLabel();
        删除商城仓库 = new javax.swing.JButton();
        jPanel48 = new javax.swing.JPanel();
        jScrollPane30 = new javax.swing.JScrollPane();
        角色点券拍卖行 = new javax.swing.JTable();
        拍卖行物品名字1 = new javax.swing.JTextField();
        角色点券拍卖行序号 = new javax.swing.JTextField();
        拍卖行物品代码1 = new javax.swing.JTextField();
        jLabel354 = new javax.swing.JLabel();
        jLabel355 = new javax.swing.JLabel();
        jLabel356 = new javax.swing.JLabel();
        删除拍卖行1 = new javax.swing.JButton();
        jPanel44 = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        角色金币拍卖行 = new javax.swing.JTable();
        拍卖行物品名字 = new javax.swing.JTextField();
        角色金币拍卖行序号 = new javax.swing.JTextField();
        拍卖行物品代码 = new javax.swing.JTextField();
        jLabel309 = new javax.swing.JLabel();
        jLabel310 = new javax.swing.JLabel();
        jLabel311 = new javax.swing.JLabel();
        删除拍卖行 = new javax.swing.JButton();
        技能 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        技能信息 = new javax.swing.JTable();
        技能代码 = new javax.swing.JTextField();
        技能目前等级 = new javax.swing.JTextField();
        技能最高等级 = new javax.swing.JTextField();
        技能名字 = new javax.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        修改技能 = new javax.swing.JButton();
        技能序号 = new javax.swing.JTextField();
        jLabel188 = new javax.swing.JLabel();
        jLabel204 = new javax.swing.JLabel();
        jLabel205 = new javax.swing.JLabel();
        删除技能 = new javax.swing.JButton();
        修改技能1 = new javax.swing.JButton();
        角色提示语言 = new javax.swing.JLabel();
        拍卖行 = new javax.swing.JPanel();
        jTabbedPane7 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane27 = new javax.swing.JScrollPane();
        金币拍卖行 = new javax.swing.JTable();
        刷新金币拍卖行 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane28 = new javax.swing.JScrollPane();
        点券拍卖行 = new javax.swing.JTable();
        刷新金币拍卖行1 = new javax.swing.JButton();
        发送福利 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        福利记录符号 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        全服发送物品数量 = new javax.swing.JTextField();
        全服发送物品代码 = new javax.swing.JTextField();
        给予物品1 = new javax.swing.JButton();
        jLabel217 = new javax.swing.JLabel();
        jLabel234 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        全服发送装备装备加卷 = new javax.swing.JTextField();
        全服发送装备装备制作人 = new javax.swing.JTextField();
        全服发送装备装备力量 = new javax.swing.JTextField();
        全服发送装备装备MP = new javax.swing.JTextField();
        全服发送装备装备智力 = new javax.swing.JTextField();
        全服发送装备装备运气 = new javax.swing.JTextField();
        全服发送装备装备HP = new javax.swing.JTextField();
        全服发送装备装备攻击力 = new javax.swing.JTextField();
        全服发送装备装备给予时间 = new javax.swing.JTextField();
        全服发送装备装备可否交易 = new javax.swing.JTextField();
        全服发送装备装备敏捷 = new javax.swing.JTextField();
        全服发送装备物品ID = new javax.swing.JTextField();
        全服发送装备装备魔法力 = new javax.swing.JTextField();
        全服发送装备装备魔法防御 = new javax.swing.JTextField();
        全服发送装备装备物理防御 = new javax.swing.JTextField();
        给予装备1 = new javax.swing.JButton();
        jLabel219 = new javax.swing.JLabel();
        jLabel220 = new javax.swing.JLabel();
        jLabel221 = new javax.swing.JLabel();
        jLabel222 = new javax.swing.JLabel();
        jLabel223 = new javax.swing.JLabel();
        jLabel224 = new javax.swing.JLabel();
        jLabel225 = new javax.swing.JLabel();
        jLabel226 = new javax.swing.JLabel();
        jLabel227 = new javax.swing.JLabel();
        jLabel228 = new javax.swing.JLabel();
        jLabel229 = new javax.swing.JLabel();
        jLabel230 = new javax.swing.JLabel();
        jLabel231 = new javax.swing.JLabel();
        jLabel232 = new javax.swing.JLabel();
        jLabel233 = new javax.swing.JLabel();
        发送装备玩家姓名 = new javax.swing.JTextField();
        给予装备2 = new javax.swing.JButton();
        jLabel246 = new javax.swing.JLabel();
        jLabel244 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        z2 = new javax.swing.JButton();
        z3 = new javax.swing.JButton();
        z1 = new javax.swing.JButton();
        z4 = new javax.swing.JButton();
        z5 = new javax.swing.JButton();
        z6 = new javax.swing.JButton();
        a1 = new javax.swing.JTextField();
        jLabel235 = new javax.swing.JLabel();
        福利提示语言 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        个人发送物品数量 = new javax.swing.JTextField();
        个人发送物品玩家名字 = new javax.swing.JTextField();
        个人发送物品代码 = new javax.swing.JTextField();
        给予物品 = new javax.swing.JButton();
        jLabel240 = new javax.swing.JLabel();
        jLabel241 = new javax.swing.JLabel();
        jLabel242 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        存储发送物品 = new javax.swing.JTable();
        刷新个人 = new javax.swing.JButton();
        刷新通用 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        福利序号 = new javax.swing.JTextField();
        新增个人 = new javax.swing.JButton();
        新增通用 = new javax.swing.JButton();
        删除通用 = new javax.swing.JButton();
        删除个人 = new javax.swing.JButton();
        修改记录 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane134 = new javax.swing.JScrollPane();
        在线泡点设置 = new javax.swing.JTable();
        泡点序号 = new javax.swing.JTextField();
        泡点类型 = new javax.swing.JTextField();
        泡点值 = new javax.swing.JTextField();
        泡点值修改 = new javax.swing.JButton();
        jLabel322 = new javax.swing.JLabel();
        jLabel323 = new javax.swing.JLabel();
        jLabel324 = new javax.swing.JLabel();
        jPanel45 = new javax.swing.JPanel();
        泡点金币开关 = new javax.swing.JButton();
        泡点经验开关 = new javax.swing.JButton();
        泡点点券开关 = new javax.swing.JButton();
        泡点抵用开关 = new javax.swing.JButton();
        jLabel325 = new javax.swing.JLabel();
        福利提示语言2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane135 = new javax.swing.JScrollPane();
        雇佣在线泡点设置 = new javax.swing.JTable();
        泡点序号1 = new javax.swing.JTextField();
        jLabel326 = new javax.swing.JLabel();
        泡点类型1 = new javax.swing.JTextField();
        jLabel327 = new javax.swing.JLabel();
        泡点值1 = new javax.swing.JTextField();
        jLabel328 = new javax.swing.JLabel();
        泡点值修改1 = new javax.swing.JButton();
        jLabel329 = new javax.swing.JLabel();
        发布公告 = new javax.swing.JPanel();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        公告填写 = new javax.swing.JTextField();
        公告发布喇叭代码 = new javax.swing.JTextField();
        jButton34 = new javax.swing.JButton();
        jLabel106 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jLabel259 = new javax.swing.JLabel();
        公告提示语言 = new javax.swing.JLabel();
        其他设置 = new javax.swing.JPanel();
        jScrollPane24 = new javax.swing.JScrollPane();
        家族信息 = new javax.swing.JTable();
        刷新家族信息 = new javax.swing.JButton();
        jLabel194 = new javax.swing.JLabel();
        家族ID = new javax.swing.JTextField();
        家族名称 = new javax.swing.JTextField();
        jLabel195 = new javax.swing.JLabel();
        家族族长 = new javax.swing.JTextField();
        jLabel196 = new javax.swing.JLabel();
        jLabel197 = new javax.swing.JLabel();
        家族成员1 = new javax.swing.JTextField();
        jLabel198 = new javax.swing.JLabel();
        家族成员2 = new javax.swing.JTextField();
        jLabel199 = new javax.swing.JLabel();
        家族成员3 = new javax.swing.JTextField();
        jLabel200 = new javax.swing.JLabel();
        家族成员4 = new javax.swing.JTextField();
        jLabel313 = new javax.swing.JLabel();
        家族成员5 = new javax.swing.JTextField();
        jLabel314 = new javax.swing.JLabel();
        家族GP = new javax.swing.JTextField();
        PVP技能 = new javax.swing.JPanel();
        jScrollPane26 = new javax.swing.JScrollPane();
        PVP技能设置 = new javax.swing.JTable();
        技能PVP编号 = new javax.swing.JTextField();
        技能PVPID = new javax.swing.JTextField();
        技能PVP伤害 = new javax.swing.JTextField();
        jLabel218 = new javax.swing.JLabel();
        jLabel236 = new javax.swing.JLabel();
        刷新PVP列表 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        新增1 = new javax.swing.JButton();
        修改1 = new javax.swing.JButton();
        搜索1 = new javax.swing.JButton();
        jLabel268 = new javax.swing.JLabel();
        jLabel269 = new javax.swing.JLabel();
        jLabel271 = new javax.swing.JLabel();
        jLabel272 = new javax.swing.JLabel();
        更新 = new javax.swing.JPanel();
        标题 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        更新记录 = new javax.swing.JTable();
        jScrollPane14 = new javax.swing.JScrollPane();
        更新记录详细 = new javax.swing.JTextPane();
        jButton10 = new javax.swing.JButton();
        雇佣角色ID = new javax.swing.JPanel();
        查询 = new javax.swing.JButton();
        jLabel270 = new javax.swing.JLabel();
        雇佣角色ID2 = new javax.swing.JTextField();
        指令 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        天谴1级码 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        保存数据 = new javax.swing.JButton();
        保存雇佣 = new javax.swing.JButton();
        应用重载 = new javax.swing.JButton();
        活动控制台 = new javax.swing.JButton();
        充值卡后台 = new javax.swing.JButton();
        副本控制台 = new javax.swing.JButton();
        控制台2号 = new javax.swing.JButton();
        控制台1号 = new javax.swing.JButton();
        更多应用 = new javax.swing.JButton();
        关闭服务端 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        脚本编辑器 = new javax.swing.JButton();
        快捷面板 = new javax.swing.JButton();
        保存数据1 = new javax.swing.JButton();
        保存雇佣1 = new javax.swing.JButton();
        应用重载1 = new javax.swing.JButton();
        活动控制台1 = new javax.swing.JButton();
        充值卡后台1 = new javax.swing.JButton();
        副本控制台1 = new javax.swing.JButton();
        控制台2号1 = new javax.swing.JButton();
        控制台1号1 = new javax.swing.JButton();
        更多应用1 = new javax.swing.JButton();
        关闭服务端1 = new javax.swing.JButton();

        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jTabbedPane1.setFont(new java.awt.Font("新宋体", 0, 16)); // NOI18N
        jTabbedPane1.setNextFocusableComponent(this);

        首页.setBackground(new java.awt.Color(255, 255, 255));
        首页.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        首页.setFont(new java.awt.Font("新宋体", 0, 15)); // NOI18N
        首页.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane6.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N

        首页1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        首页LOGO.setIcon(new javax.swing.JLabel() {
            public javax.swing.Icon getIcon() {
                try {
                    return new javax.swing.ImageIcon(
                        new java.net.URL("http://123.207.53.97:8082/服务端广告位/ad1.png")
                    );
                } catch (java.net.MalformedURLException e) {
                }
                return null;
            }
        }.getIcon());
        首页1.add(首页LOGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -5, 1260, 660));

        jTabbedPane6.addTab("         首页         ", null, 首页1, "");

        上次启动.setBackground(new java.awt.Color(255, 255, 255));
        上次启动.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "个人账户信息", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 1, 36), new java.awt.Color(255, 255, 255))); // NOI18N
        上次启动.setForeground(new java.awt.Color(255, 255, 255));
        上次启动.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        启动账号.setBackground(new java.awt.Color(255, 255, 255));
        启动账号.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        启动账号.setForeground(new java.awt.Color(255, 255, 255));
        启动账号.setText("71447500");
        上次启动.add(启动账号, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 450, 30));

        启动账号1.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        启动账号1.setForeground(new java.awt.Color(255, 255, 255));
        启动账号1.setText("|用户账号:");
        上次启动.add(启动账号1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 130, 30));

        人数限制.setBackground(new java.awt.Color(255, 255, 255));
        人数限制.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        人数限制.setForeground(new java.awt.Color(255, 255, 255));
        人数限制.setText("6");
        上次启动.add(人数限制, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 220, 730, 30));

        上次启动时间2.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        上次启动时间2.setForeground(new java.awt.Color(255, 255, 255));
        上次启动时间2.setText("|到期时间：");
        上次启动.add(上次启动时间2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, -1, 30));

        上次启动时间3.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        上次启动时间3.setForeground(new java.awt.Color(255, 255, 255));
        上次启动时间3.setText("|人数限制:");
        上次启动.add(上次启动时间3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 130, 30));

        剩余时间.setBackground(new java.awt.Color(255, 255, 255));
        剩余时间.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        剩余时间.setForeground(new java.awt.Color(255, 255, 255));
        剩余时间.setText("1111");
        上次启动.add(剩余时间, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 190, 730, 30));

        游戏名字.setBackground(new java.awt.Color(255, 255, 255));
        游戏名字.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        游戏名字.setForeground(new java.awt.Color(255, 255, 255));
        游戏名字.setText("自由冒险岛");
        上次启动.add(游戏名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 160, 440, 30));

        启动账号3.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        启动账号3.setForeground(new java.awt.Color(255, 255, 255));
        启动账号3.setText("|游戏名字:");
        上次启动.add(启动账号3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 130, 30));

        啊啊啊3.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        啊啊啊3.setForeground(new java.awt.Color(255, 255, 255));
        啊啊啊3.setText("|绑定地址:");
        上次启动.add(啊啊啊3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 130, 30));

        jButton11.setText("刷新一下");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        上次启动.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 600, 100, -1));

        jTabbedPane6.addTab("         账号信息         ", 上次启动);

        首页.add(jTabbedPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1255, 680));
        jTabbedPane6.getAccessibleContext().setAccessibleName("");

        jTabbedPane1.addTab("ZEVMS首页", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 首页); // NOI18N

        商品设置.setBackground(new java.awt.Color(255, 255, 255));
        商品设置.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        商品设置.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        商品设置.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        商城提示语言.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        商城提示语言.setText("[信息]:");
        商品设置.add(商城提示语言, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 650, 660, 25));

        charTable.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        charTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "商品编码", "物品代码", "道具名称", "数量", "价格", "限时/天", "出售状态", "上/下架", "已售出", "库存", "反馈/%", "每日限购"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        charTable.setToolTipText("");
        charTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(charTable);
        if (charTable.getColumnModel().getColumnCount() > 0) {
            charTable.getColumnModel().getColumn(0).setResizable(false);
            charTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            charTable.getColumnModel().getColumn(1).setResizable(false);
            charTable.getColumnModel().getColumn(1).setPreferredWidth(30);
            charTable.getColumnModel().getColumn(2).setResizable(false);
            charTable.getColumnModel().getColumn(2).setPreferredWidth(180);
            charTable.getColumnModel().getColumn(3).setResizable(false);
            charTable.getColumnModel().getColumn(3).setPreferredWidth(5);
            charTable.getColumnModel().getColumn(4).setResizable(false);
            charTable.getColumnModel().getColumn(4).setPreferredWidth(30);
            charTable.getColumnModel().getColumn(5).setResizable(false);
            charTable.getColumnModel().getColumn(5).setPreferredWidth(10);
            charTable.getColumnModel().getColumn(6).setResizable(false);
            charTable.getColumnModel().getColumn(6).setPreferredWidth(50);
            charTable.getColumnModel().getColumn(7).setResizable(false);
            charTable.getColumnModel().getColumn(7).setPreferredWidth(40);
            charTable.getColumnModel().getColumn(8).setResizable(false);
            charTable.getColumnModel().getColumn(8).setPreferredWidth(30);
            charTable.getColumnModel().getColumn(9).setResizable(false);
            charTable.getColumnModel().getColumn(9).setPreferredWidth(30);
            charTable.getColumnModel().getColumn(10).setResizable(false);
            charTable.getColumnModel().getColumn(10).setPreferredWidth(30);
            charTable.getColumnModel().getColumn(11).setResizable(false);
            charTable.getColumnModel().getColumn(11).setPreferredWidth(30);
        }

        商品设置.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 60, 1254, 500));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "出售状态", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        无出售状态.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        无出售状态.setText("无");
        无出售状态.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                无出售状态ActionPerformed(evt);
            }
        });
        jPanel15.add(无出售状态, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 70, 25));

        NEW.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        NEW.setText("NEW");
        NEW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NEWActionPerformed(evt);
            }
        });
        jPanel15.add(NEW, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 55, 70, 25));

        Sale.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        Sale.setText("Sale");
        Sale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaleActionPerformed(evt);
            }
        });
        jPanel15.add(Sale, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 70, 25));

        HOT.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        HOT.setText("HOT");
        HOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HOTActionPerformed(evt);
            }
        });
        jPanel15.add(HOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 70, 25));

        event.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        event.setText("event");
        event.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventActionPerformed(evt);
            }
        });
        jPanel15.add(event, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 55, 140, 25));

        商品设置.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 230, 90));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "添加值", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        商品数量.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        jPanel16.add(商品数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 55, 65, 20));

        商品编码.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        商品编码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                商品编码ActionPerformed(evt);
            }
        });
        jPanel16.add(商品编码, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 25, 65, 20));

        商品代码.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        jPanel16.add(商品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 55, 65, -1));

        jLabel30.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel30.setText("商品数量；");
        jPanel16.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, 30));

        jLabel29.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel29.setText("商品代码；");
        jPanel16.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 30));

        商品价格.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        jPanel16.add(商品价格, new org.netbeans.lib.awtextra.AbsoluteConstraints(234, 25, 65, -1));

        商品时间.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        jPanel16.add(商品时间, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 55, 65, 20));

        jLabel32.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel32.setText("商品库存；");
        jPanel16.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 90, 30));

        jLabel33.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel33.setText("限时时间；");
        jPanel16.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, -1, 30));

        jLabel34.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel34.setText("商品编码；");
        jPanel16.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, 30));

        jLabel35.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel35.setText("商品价格；");
        jPanel16.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 90, 30));

        商品库存.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        jPanel16.add(商品库存, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 25, 65, -1));

        商品折扣.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        jPanel16.add(商品折扣, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 25, 65, -1));

        jLabel37.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel37.setText("商品反馈；");
        jPanel16.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 90, 30));

        jLabel36.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel36.setText("每日限购；");
        jPanel16.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, 90, 30));

        每日限购.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        jPanel16.add(每日限购, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 55, 65, -1));

        jLabel38.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel38.setText("货币类型；");
        jPanel16.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 90, 30));

        货币类型.setEditable(false);
        货币类型.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        jPanel16.add(货币类型, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 25, 65, -1));

        商品设置.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 560, 730, 90));

        商城扩充价格.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        商城扩充价格.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "背包扩充价格"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        商城扩充价格.getTableHeader().setReorderingAllowed(false);
        jScrollPane132.setViewportView(商城扩充价格);
        if (商城扩充价格.getColumnModel().getColumnCount() > 0) {
            商城扩充价格.getColumnModel().getColumn(0).setResizable(false);
            商城扩充价格.getColumnModel().getColumn(0).setPreferredWidth(50);
        }

        商品设置.add(jScrollPane132, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 570, 130, 50));

        商城扩充价格修改.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        商品设置.add(商城扩充价格修改, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 620, 60, 27));

        jTabbedPane2.setBackground(new java.awt.Color(204, 255, 204));
        jTabbedPane2.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        主题馆.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        主题馆.setText("主题馆");
        主题馆.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">主题馆</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">主体馆</font></strong>分类下的所有商品<br> ");
        主题馆.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                主题馆ActionPerformed(evt);
            }
        });

        读取热销产品.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        读取热销产品.setText("热销产品");
        读取热销产品.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">热销产品</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">热销产品</font></strong>分类下的所有商品<br> ");
        读取热销产品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                读取热销产品ActionPerformed(evt);
            }
        });

        活动.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        活动.setText("活动");
        活动.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">活动</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">活动</font></strong>分类下的所有商品<br> ");
        活动.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                活动ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        jButton4.setText("每日特卖");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(读取热销产品, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(主题馆, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(活动, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(读取热销产品)
            .addComponent(主题馆)
            .addComponent(活动)
            .addComponent(jButton4)
        );

        jTabbedPane2.addTab("热销产品", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        帽子.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        帽子.setText("帽子");
        帽子.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">帽子</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">帽子</font></strong>分类下的所有商品<br> ");
        帽子.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                帽子ActionPerformed(evt);
            }
        });
        jPanel5.add(帽子, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 90, -1));

        脸饰.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        脸饰.setText("脸饰");
        脸饰.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">脸饰</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">脸饰</font></strong>分类下的所有商品<br> ");
        脸饰.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                脸饰ActionPerformed(evt);
            }
        });
        jPanel5.add(脸饰, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 0, 90, -1));

        眼饰.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        眼饰.setText("眼饰");
        眼饰.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">眼饰</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">眼饰</font></strong>分类下的所有商品<br> ");
        眼饰.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                眼饰ActionPerformed(evt);
            }
        });
        jPanel5.add(眼饰, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 0, 90, -1));

        长袍.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        长袍.setText("长袍");
        长袍.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">长袍</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">长袍</font></strong>分类下的所有商品<br> ");
        长袍.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                长袍ActionPerformed(evt);
            }
        });
        jPanel5.add(长袍, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 90, -1));

        上衣.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        上衣.setText("上衣");
        上衣.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">上衣</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">上衣</font></strong>分类下的所有商品<br> ");
        上衣.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                上衣ActionPerformed(evt);
            }
        });
        jPanel5.add(上衣, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 0, 90, -1));

        裙裤.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        裙裤.setText("裙裤");
        裙裤.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">裙裤</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">裙裤</font></strong>分类下的所有商品<br> ");
        裙裤.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                裙裤ActionPerformed(evt);
            }
        });
        jPanel5.add(裙裤, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 90, -1));

        鞋子.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        鞋子.setText("鞋子");
        鞋子.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">鞋子</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">鞋子</font></strong>分类下的所有商品<br> ");
        鞋子.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                鞋子ActionPerformed(evt);
            }
        });
        jPanel5.add(鞋子, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, 90, -1));

        手套.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        手套.setText("手套");
        手套.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">手套</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">手套</font></strong>分类下的所有商品<br> ");
        手套.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                手套ActionPerformed(evt);
            }
        });
        jPanel5.add(手套, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 0, 90, -1));

        武器.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        武器.setText("武器");
        武器.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">武器</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">武器</font></strong>分类下的所有商品<br> ");
        武器.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                武器ActionPerformed(evt);
            }
        });
        jPanel5.add(武器, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 0, 90, -1));

        戒指.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        戒指.setText("戒指");
        戒指.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">戒指</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">戒指</font></strong>分类下的所有商品<br> ");
        戒指.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                戒指ActionPerformed(evt);
            }
        });
        jPanel5.add(戒指, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 0, 90, -1));

        飞镖.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        飞镖.setText("飞镖");
        飞镖.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">飞镖</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">飞镖</font></strong>分类下的所有商品<br> ");
        飞镖.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                飞镖ActionPerformed(evt);
            }
        });
        jPanel5.add(飞镖, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 90, -1));

        披风.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        披风.setText("披风");
        披风.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">披风</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">披风</font></strong>分类下的所有商品<br> ");
        披风.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                披风ActionPerformed(evt);
            }
        });
        jPanel5.add(披风, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 90, -1));

        骑宠.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        骑宠.setText("骑宠");
        骑宠.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">骑宠</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">骑宠</font></strong>分类下的所有商品<br> ");
        骑宠.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                骑宠ActionPerformed(evt);
            }
        });
        jPanel5.add(骑宠, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 0, 90, -1));

        jTabbedPane2.addTab("装备", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        喜庆物品.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        喜庆物品.setText("喜庆物品");
        喜庆物品.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">喜庆物品</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">喜庆物品</font></strong>分类下的所有商品<br> ");
        喜庆物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                喜庆物品ActionPerformed(evt);
            }
        });

        通讯物品.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        通讯物品.setText("通讯物品");
        通讯物品.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">通讯物品</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">通讯物品</font></strong>分类下的所有商品<br> ");
        通讯物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                通讯物品ActionPerformed(evt);
            }
        });

        卷轴.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        卷轴.setText("卷轴");
        卷轴.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">卷轴</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">卷轴</font></strong>分类下的所有商品<br> ");
        卷轴.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                卷轴ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(喜庆物品, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(通讯物品, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(卷轴, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(喜庆物品)
            .addComponent(通讯物品)
            .addComponent(卷轴)
        );

        jTabbedPane2.addTab("消耗", jPanel6);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        会员卡.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        会员卡.setText("会员卡");
        会员卡.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">会员卡</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">会员卡</font></strong>分类下的所有商品<br> ");
        会员卡.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                会员卡ActionPerformed(evt);
            }
        });
        jPanel7.add(会员卡, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, -1));

        表情.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        表情.setText("表情");
        表情.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">表情</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">表情</font></strong>分类下的所有商品<br> ");
        表情.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                表情ActionPerformed(evt);
            }
        });
        jPanel7.add(表情, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 100, -1));

        个人商店.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        个人商店.setText("个人商店");
        个人商店.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">个人商店</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">个人商店</font></strong>分类下的所有商品<br> ");
        个人商店.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                个人商店ActionPerformed(evt);
            }
        });
        jPanel7.add(个人商店, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 100, -1));

        纪念日.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        纪念日.setText("纪念日");
        纪念日.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                纪念日ActionPerformed(evt);
            }
        });
        jPanel7.add(纪念日, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 100, -1));

        游戏.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        游戏.setText("游戏");
        游戏.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">游戏</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">游戏</font></strong>分类下的所有商品<br> ");
        游戏.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏ActionPerformed(evt);
            }
        });
        jPanel7.add(游戏, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 100, -1));

        效果.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        效果.setText("效果");
        效果.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">效果</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">效果</font></strong>分类下的所有商品<br> ");
        效果.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                效果ActionPerformed(evt);
            }
        });
        jPanel7.add(效果, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 100, -1));

        jTabbedPane2.addTab("其他", jPanel7);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        宠物.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        宠物.setText("宠物");
        宠物.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">宠物</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">宠物</font></strong>分类下的所有商品<br> ");
        宠物.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                宠物ActionPerformed(evt);
            }
        });

        宠物服饰.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        宠物服饰.setText("宠物服饰");
        宠物服饰.setToolTipText("<html>\n<strong><font color=\"#FF0000\">点击后；</font></strong><br> \n可在<strong><font color=\"#0000E3\">宠物服饰</font></strong>分类下添加商品<br> \n显示<strong><font color=\"#0000E3\">宠物服饰</font></strong>分类下的所有商品<br> ");
        宠物服饰.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                宠物服饰ActionPerformed(evt);
            }
        });

        其他.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        其他.setText("其他");
        其他.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                其他ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(宠物, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(宠物服饰, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(其他, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(宠物)
            .addComponent(宠物服饰)
            .addComponent(其他)
        );

        jTabbedPane2.addTab("宠物", jPanel8);

        商品设置.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 57));

        修改背包扩充价格.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改背包扩充价格.setText("修改");
        修改背包扩充价格.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改背包扩充价格ActionPerformed(evt);
            }
        });
        商品设置.add(修改背包扩充价格, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 620, 70, 27));

        商品出售状态.setEditable(false);
        商品出售状态.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N
        商品出售状态.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                商品出售状态ActionPerformed(evt);
            }
        });
        商品设置.add(商品出售状态, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 650, 70, 27));

        显示类型.setEditable(false);
        显示类型.setFont(new java.awt.Font("幼圆", 1, 14)); // NOI18N
        显示类型.setForeground(new java.awt.Color(255, 0, 51));
        显示类型.setText("测试字体");
        显示类型.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                显示类型ActionPerformed(evt);
            }
        });
        商品设置.add(显示类型, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 650, 120, 27));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 0, 255));
        jButton1.setText("刷新");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel19.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 70, 25));

        jButton24.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton24.setText("删除");
        jButton24.setToolTipText("<html>\n<strong><font color=\"#FF0000\">删除；</font></strong><br>\n1.选择物品<br>\n2.删除<br>");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel19.add(jButton24, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 70, 25));

        jButton27.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton27.setText("下架");
        jButton27.setToolTipText("<html>\n<strong><font color=\"#FF0000\">下架；</font></strong><br>\n1.选择物品<br>\n2.上架/下架<br>");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanel19.add(jButton27, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 70, 25));

        jButton13.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton13.setText("上架");
        jButton13.setToolTipText("<html>\n<strong><font color=\"#FF0000\">上架；</font></strong><br>\n1.选择物品<br>\n2.上架/下架<br>");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel19.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 70, 25));

        修改.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改.setText("修改");
        修改.setToolTipText("<html>\n<strong><font color=\"#FF0000\">修改；</font></strong><br> \n1.在列表中选择需要修改的物品<br>\n2.在文本框中输入修改值<br>\n");
        修改.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改ActionPerformed(evt);
            }
        });
        jPanel19.add(修改, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 45, 70, 25));

        添加.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        添加.setText("添加");
        添加.setToolTipText("<html>\n<strong><font color=\"#FF0000\">添加；</font></strong><br> \n1.选择物品分类<br>\n2.输入商品代码<br>\n3.输入商品数量<br>\n4.输入商品价格<br>\n5.输入限时时间(0代表永久)<br>\n6.选择出售状态<br>");
        添加.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                添加ActionPerformed(evt);
            }
        });
        jPanel19.add(添加, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 45, 70, 25));

        商品设置.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 570, 140, 70));

        jButton3.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton3.setText("重载商城");
        jButton3.setToolTipText("<html>\n<strong><font color=\"#FF0000\">重载商城；</font></strong><br>\n在商城控制台中的修改需要重载才能在游戏中生效");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        商品设置.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 650, 140, 25));

        刷新模式.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新模式.setText("重载商城");
        刷新模式.setToolTipText("<html>\n<strong><font color=\"#FF0000\">重载商城；</font></strong><br>\n在商城控制台中的修改需要重载才能在游戏中生效");
        刷新模式.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新模式ActionPerformed(evt);
            }
        });
        商品设置.add(刷新模式, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 650, 130, 25));

        jTabbedPane1.addTab("游戏商城", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 商品设置, "<html>\n<strong><font color=\"#FF0000\">小提示</font></strong><br> \n控制范围：控制游戏内独立商城商品<br> \n控制功能：上架，下架，添加，删除，修改<br><br>\n\n\n<strong><font color=\"#FF0000\">添加教程</font></strong><br>\n1.选择商品分类<br>\n2.输入商品代码，商品数量，商品价格，限时时间<br>\n3.选择出售状态<br>\n4.点击添加<br>\n5.每次添加重复此步骤<br><br>\n\n<strong><font color=\"#FF0000\">常见错误问题</font></strong><br>\n问题；SN编码重复<br>\n解答；需要点击商品分类获取该分类下最新的SN编码<br>\n问题；添加商品后游戏商城不显示<br>\n解答；添加完成后需要点击<strong><font color=\"#0000E3\">重载商城</font></strong>即可生效\n\n\n\n\n\n\n\n"); // NOI18N

        账号列表.setBackground(new java.awt.Color(255, 255, 255));
        账号列表.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        账号信息.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        账号信息.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "账号ID", "账号", "IP地址", "MAC地址", "绑定QQ", "点券", "抵用", "最近上线", "在线", "封号", "GM"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        账号信息.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(账号信息);
        if (账号信息.getColumnModel().getColumnCount() > 0) {
            账号信息.getColumnModel().getColumn(0).setResizable(false);
            账号信息.getColumnModel().getColumn(0).setPreferredWidth(10);
            账号信息.getColumnModel().getColumn(1).setResizable(false);
            账号信息.getColumnModel().getColumn(1).setPreferredWidth(80);
            账号信息.getColumnModel().getColumn(2).setResizable(false);
            账号信息.getColumnModel().getColumn(3).setResizable(false);
            账号信息.getColumnModel().getColumn(3).setPreferredWidth(130);
            账号信息.getColumnModel().getColumn(4).setResizable(false);
            账号信息.getColumnModel().getColumn(4).setPreferredWidth(130);
            账号信息.getColumnModel().getColumn(5).setResizable(false);
            账号信息.getColumnModel().getColumn(5).setPreferredWidth(60);
            账号信息.getColumnModel().getColumn(6).setResizable(false);
            账号信息.getColumnModel().getColumn(6).setPreferredWidth(60);
            账号信息.getColumnModel().getColumn(7).setResizable(false);
            账号信息.getColumnModel().getColumn(7).setPreferredWidth(140);
            账号信息.getColumnModel().getColumn(8).setResizable(false);
            账号信息.getColumnModel().getColumn(8).setPreferredWidth(10);
            账号信息.getColumnModel().getColumn(9).setResizable(false);
            账号信息.getColumnModel().getColumn(9).setPreferredWidth(20);
            账号信息.getColumnModel().getColumn(10).setResizable(false);
            账号信息.getColumnModel().getColumn(10).setPreferredWidth(20);
        }

        账号列表.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1255, 510));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "账号修改", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        抵用.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel13.add(抵用, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 40, 120, 30));

        账号.setEditable(false);
        账号.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel13.add(账号, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 100, 30));

        点券.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel13.add(点券, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 120, 30));

        jLabel55.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel55.setText("抵用；");
        jPanel13.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 60, -1));

        jLabel131.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel131.setText("点券；");
        jPanel13.add(jLabel131, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, -1, -1));

        修改账号点券抵用.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        修改账号点券抵用.setText("修改");
        修改账号点券抵用.setToolTipText("<html>\n点击账号后可修改账号的<strong><font color=\"#FF0000\">抵用券</font></strong><strong>和<font color=\"#FF0000\">点券</font></strong>");
        修改账号点券抵用.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改账号点券抵用ActionPerformed(evt);
            }
        });
        jPanel13.add(修改账号点券抵用, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, 70, 30));

        账号ID.setEditable(false);
        账号ID.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel13.add(账号ID, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 70, 30));

        jLabel206.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel206.setText("ID；");
        jPanel13.add(jLabel206, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel312.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel312.setText("管理；");
        jPanel13.add(jLabel312, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, -1, -1));

        管理1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel13.add(管理1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 70, 30));

        jLabel353.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel353.setText("账号；");
        jPanel13.add(jLabel353, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, -1, -1));

        QQ.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jPanel13.add(QQ, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 120, 30));

        jLabel357.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel357.setText("绑定QQ；");
        jPanel13.add(jLabel357, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 80, -1));

        账号列表.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 710, 90));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "注册/修改", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        注册的账号.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        注册的账号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                注册的账号ActionPerformed(evt);
            }
        });
        jPanel10.add(注册的账号, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 100, 30));

        注册的密码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        注册的密码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                注册的密码ActionPerformed(evt);
            }
        });
        jPanel10.add(注册的密码, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 100, 30));

        jButton35.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jButton35.setText("注册");
        jButton35.setToolTipText("<html>\n输入<strong><font color=\"#FF0000\">账号</font></strong><strong>和<strong><font color=\"#FF0000\">密码</font></strong><strong>即可注册账号");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton35, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 70, 30));

        jLabel111.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel111.setText("账号；");
        jPanel10.add(jLabel111, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, 30));

        jLabel201.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel201.setText("密码；");
        jPanel10.add(jLabel201, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, -1, 30));

        jButton30.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jButton30.setText("改密");
        jButton30.setToolTipText("<html>\n输入账号修改<strong><font color=\"#FF0000\">密码</font></strong><strong>");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton30, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, 70, 30));

        账号列表.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 560, 520, 90));

        账号提示语言.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        账号提示语言.setText("[信息]：");
        账号列表.add(账号提示语言, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 653, 700, 25));

        刷新账号信息.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        刷新账号信息.setText("全部账号");
        刷新账号信息.setToolTipText("显示所有玩家账号");
        刷新账号信息.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新账号信息ActionPerformed(evt);
            }
        });
        账号列表.add(刷新账号信息, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 100, 30));

        离线账号.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        离线账号.setText("离线账号");
        离线账号.setToolTipText("显示离线账号");
        离线账号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                离线账号ActionPerformed(evt);
            }
        });
        账号列表.add(离线账号, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 5, 100, 30));

        解封.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        解封.setText("解封账号");
        解封.setToolTipText("<html>\n在文本框<strong><font color=\"#FF0000\">操作的账号</font></strong>中输入账号即可解封已经被封禁的账号<br>\n");
        解封.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                解封ActionPerformed(evt);
            }
        });
        账号列表.add(解封, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 5, 100, 30));

        已封账号.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        已封账号.setText("已封账号");
        已封账号.setToolTipText("显示已经被封禁的账号");
        已封账号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                已封账号ActionPerformed(evt);
            }
        });
        账号列表.add(已封账号, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 5, 100, 30));

        在线账号.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        在线账号.setText("在线账号");
        在线账号.setToolTipText("显示在线账号");
        在线账号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                在线账号ActionPerformed(evt);
            }
        });
        账号列表.add(在线账号, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 5, 100, 30));

        删除账号.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        删除账号.setText("删除账号");
        删除账号.setToolTipText("<html>\n在文本框<strong><font color=\"#FF0000\">操作的账号</font></strong>中输入账号即可删除账号<br>");
        删除账号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除账号ActionPerformed(evt);
            }
        });
        账号列表.add(删除账号, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 5, 100, 30));

        封锁账号.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        封锁账号.setText("封锁账号");
        封锁账号.setToolTipText("<html>\n在文本框<strong><font color=\"#FF0000\">操作的账号</font></strong>中输入账号即可封禁账号<br>");
        封锁账号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                封锁账号ActionPerformed(evt);
            }
        });
        账号列表.add(封锁账号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 5, 100, 30));

        解卡.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        解卡.setText("解卡账号");
        解卡.setToolTipText("<html>\n在文本框<strong><font color=\"#FF0000\">操作的账号</font></strong>中输入账号即可解卡账号<br>");
        解卡.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                解卡ActionPerformed(evt);
            }
        });
        账号列表.add(解卡, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 5, 100, 30));

        账号操作.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        账号操作.setText("71447500");
        账号列表.add(账号操作, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 5, 110, 30));

        jButton7.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jButton7.setText("查QQ");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        账号列表.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 5, 80, 30));

        显示在线账号.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        账号列表.add(显示在线账号, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 650, 130, 30));

        jButton12.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jButton12.setText("查账号");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        账号列表.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 5, 80, 30));

        清空QQ相关.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        清空QQ相关.setForeground(new java.awt.Color(255, 51, 51));
        清空QQ相关.setText("清空QQ相关");
        清空QQ相关.setToolTipText("<html>\n在文本框<strong><font color=\"#FF0000\">操作的账号</font></strong>中输入账号即可删除账号<br>");
        清空QQ相关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                清空QQ相关ActionPerformed(evt);
            }
        });
        账号列表.add(清空QQ相关, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 5, 130, 30));

        jTabbedPane1.addTab("游戏账号", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 账号列表, "<html>\n<strong><font color=\"#FF0000\">小提示</font></strong><br> \n控制范围：控制游戏账号<br> \n控制功能：查看所有账号，在线账号，离线账号，已封账号。解封账号，<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;解卡账号，删除账号，封锁账号，修改点券，修改抵用。<br> \n\n\n<strong><font color=\"#FF0000\">功能</font></strong><br> \n1.在表中点击账号后，可以在角色表中查看此账号内所有角色<br> \n2.在表中点击账号后，会在右下方显示账号的点券，抵用<br> \n3.账号的点券抵用可以修改，但是账号必须<strong><font color=\"#0000E3\">离线</font></strong>才有效<br> "); // NOI18N

        角色列表.setBackground(new java.awt.Color(255, 255, 255));
        角色列表.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane8.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N

        角色信息1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色信息.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        角色信息.setFont(new java.awt.Font("幼圆", 0, 13)); // NOI18N
        角色信息.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "角色ID", "账号ID", "角色昵称", "职业", "等级", "力量", "敏捷", "智力", "运气", "MaxHP", "MaxMP", "金币", "所在地图", "在线时长", "状态", "GM", "发型", "脸型"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色信息.setName(""); // NOI18N
        角色信息.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(角色信息);
        if (角色信息.getColumnModel().getColumnCount() > 0) {
            角色信息.getColumnModel().getColumn(0).setResizable(false);
            角色信息.getColumnModel().getColumn(0).setPreferredWidth(35);
            角色信息.getColumnModel().getColumn(1).setResizable(false);
            角色信息.getColumnModel().getColumn(1).setPreferredWidth(35);
            角色信息.getColumnModel().getColumn(2).setResizable(false);
            角色信息.getColumnModel().getColumn(2).setPreferredWidth(100);
            角色信息.getColumnModel().getColumn(3).setResizable(false);
            角色信息.getColumnModel().getColumn(3).setPreferredWidth(50);
            角色信息.getColumnModel().getColumn(4).setResizable(false);
            角色信息.getColumnModel().getColumn(4).setPreferredWidth(50);
            角色信息.getColumnModel().getColumn(5).setResizable(false);
            角色信息.getColumnModel().getColumn(5).setPreferredWidth(40);
            角色信息.getColumnModel().getColumn(6).setResizable(false);
            角色信息.getColumnModel().getColumn(6).setPreferredWidth(40);
            角色信息.getColumnModel().getColumn(7).setResizable(false);
            角色信息.getColumnModel().getColumn(7).setPreferredWidth(40);
            角色信息.getColumnModel().getColumn(8).setResizable(false);
            角色信息.getColumnModel().getColumn(8).setPreferredWidth(40);
            角色信息.getColumnModel().getColumn(9).setResizable(false);
            角色信息.getColumnModel().getColumn(9).setPreferredWidth(50);
            角色信息.getColumnModel().getColumn(10).setResizable(false);
            角色信息.getColumnModel().getColumn(10).setPreferredWidth(50);
            角色信息.getColumnModel().getColumn(11).setResizable(false);
            角色信息.getColumnModel().getColumn(11).setPreferredWidth(50);
            角色信息.getColumnModel().getColumn(12).setResizable(false);
            角色信息.getColumnModel().getColumn(13).setResizable(false);
            角色信息.getColumnModel().getColumn(13).setPreferredWidth(80);
            角色信息.getColumnModel().getColumn(14).setResizable(false);
            角色信息.getColumnModel().getColumn(14).setPreferredWidth(20);
            角色信息.getColumnModel().getColumn(15).setResizable(false);
            角色信息.getColumnModel().getColumn(15).setPreferredWidth(20);
            角色信息.getColumnModel().getColumn(16).setResizable(false);
            角色信息.getColumnModel().getColumn(16).setPreferredWidth(30);
            角色信息.getColumnModel().getColumn(17).setResizable(false);
            角色信息.getColumnModel().getColumn(17).setPreferredWidth(30);
        }

        角色信息1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1247, 490));

        刷新角色信息.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        刷新角色信息.setText("刷新");
        刷新角色信息.setToolTipText("显示所有角色");
        刷新角色信息.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新角色信息ActionPerformed(evt);
            }
        });
        角色信息1.add(刷新角色信息, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 110, 30));

        显示管理角色.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        显示管理角色.setText("管理角色");
        显示管理角色.setToolTipText("显示所有GM管理员");
        显示管理角色.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                显示管理角色ActionPerformed(evt);
            }
        });
        角色信息1.add(显示管理角色, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 110, 30));

        jButton38.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jButton38.setText("修改");
        jButton38.setToolTipText("<html>\n修改角色信息<strong><font color=\"#FF0000\">文本框不可留空</font></strong><strong>");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });
        角色信息1.add(jButton38, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 570, 100, 50));

        删除角色.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除角色.setText("删除角色");
        删除角色.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除角色ActionPerformed(evt);
            }
        });
        角色信息1.add(删除角色, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 530, 100, 30));

        角色昵称.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色昵称.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                角色昵称ActionPerformed(evt);
            }
        });
        角色信息1.add(角色昵称, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 590, 120, 30));

        等级.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        等级.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                等级ActionPerformed(evt);
            }
        });
        角色信息1.add(等级, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 590, 70, 30));

        力量.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        力量.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                力量ActionPerformed(evt);
            }
        });
        角色信息1.add(力量, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 590, 70, 30));

        敏捷.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        敏捷.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                敏捷ActionPerformed(evt);
            }
        });
        角色信息1.add(敏捷, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 590, 70, 30));

        智力.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        智力.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                智力ActionPerformed(evt);
            }
        });
        角色信息1.add(智力, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 590, 70, 30));

        运气.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        运气.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                运气ActionPerformed(evt);
            }
        });
        角色信息1.add(运气, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 590, 70, 30));

        HP.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        HP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HPActionPerformed(evt);
            }
        });
        角色信息1.add(HP, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 590, 70, 30));

        MP.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        MP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MPActionPerformed(evt);
            }
        });
        角色信息1.add(MP, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 590, 70, 30));

        金币.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        金币.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                金币ActionPerformed(evt);
            }
        });
        角色信息1.add(金币, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 590, 130, 30));

        地图.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        地图.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                地图ActionPerformed(evt);
            }
        });
        角色信息1.add(地图, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 590, 130, 30));

        GM.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        GM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GMActionPerformed(evt);
            }
        });
        角色信息1.add(GM, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 590, 70, 30));

        jLabel182.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel182.setText("GM等级；");
        角色信息1.add(jLabel182, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 570, -1, -1));

        jLabel183.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel183.setText("角色ID；");
        角色信息1.add(jLabel183, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, -1, -1));

        jLabel184.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel184.setText("等级；");
        角色信息1.add(jLabel184, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 570, -1, -1));

        jLabel185.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel185.setText("力量；");
        角色信息1.add(jLabel185, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 570, -1, -1));

        jLabel186.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel186.setText("敏捷；");
        角色信息1.add(jLabel186, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 570, -1, -1));

        jLabel187.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel187.setText("智力；");
        角色信息1.add(jLabel187, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 570, -1, -1));

        jLabel189.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel189.setText("MaxHP；");
        角色信息1.add(jLabel189, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 570, -1, -1));

        jLabel190.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel190.setText("MaxMP；");
        角色信息1.add(jLabel190, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 570, -1, -1));

        jLabel191.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel191.setText("金币；");
        角色信息1.add(jLabel191, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 570, -1, -1));

        jLabel192.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel192.setText("发型/脸型");
        角色信息1.add(jLabel192, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 530, -1, 30));

        jLabel193.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel193.setText("角色昵称；");
        角色信息1.add(jLabel193, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 570, -1, -1));

        角色ID.setEditable(false);
        角色ID.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                角色IDActionPerformed(evt);
            }
        });
        角色信息1.add(角色ID, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 70, 30));

        卡号自救1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        卡号自救1.setText("卡发/脸型解救");
        卡号自救1.setToolTipText("<html>\n角色卡<strong><font color=\"#FF0000\">发型</font></strong><strong>或者<strong><font color=\"#FF0000\">脸型</font></strong><strong>时候可用此功能\n");
        卡号自救1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                卡号自救1ActionPerformed(evt);
            }
        });
        角色信息1.add(卡号自救1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 500, 130, 30));

        卡号自救2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        卡号自救2.setText("卡物品解救");
        卡号自救2.setToolTipText("<html>\n次卡号解救会对角色进行<strong><font color=\"#FF0000\">清空物品</font></strong><strong>处理");
        卡号自救2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                卡号自救2ActionPerformed(evt);
            }
        });
        角色信息1.add(卡号自救2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 530, 130, 30));

        jLabel203.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel203.setText("运气；");
        角色信息1.add(jLabel203, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 570, -1, -1));

        查看技能.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        查看技能.setText("查看角色技能");
        查看技能.setToolTipText("<html>\n选择角色后，点击此功能，可查看角色所有<strong><font color=\"#FF0000\">技能信息</font></strong><strong>");
        查看技能.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查看技能ActionPerformed(evt);
            }
        });
        角色信息1.add(查看技能, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 530, 130, 30));

        查看背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        查看背包.setText("查看角色背包");
        查看背包.setToolTipText("<html>\n选择角色后，点击此功能，可查看角色所有<strong><font color=\"#FF0000\">物品信息</font></strong><strong>");
        查看背包.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查看背包ActionPerformed(evt);
            }
        });
        角色信息1.add(查看背包, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 530, 130, 30));

        卡家族解救.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        卡家族解救.setText("卡家族解救");
        卡家族解救.setToolTipText("<html>\n角色卡<strong><font color=\"#FF0000\">发型</font></strong><strong>或者<strong><font color=\"#FF0000\">脸型</font></strong><strong>时候可用此功能\n");
        卡家族解救.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                卡家族解救ActionPerformed(evt);
            }
        });
        角色信息1.add(卡家族解救, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 500, 130, 30));

        脸型.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        脸型.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                脸型ActionPerformed(evt);
            }
        });
        角色信息1.add(脸型, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 530, 60, 30));

        发型.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        发型.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                发型ActionPerformed(evt);
            }
        });
        角色信息1.add(发型, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 530, 60, 30));

        jLabel214.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel214.setText("所在地图；");
        角色信息1.add(jLabel214, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 570, -1, -1));

        离线角色.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        离线角色.setText("离线角色");
        离线角色.setToolTipText("显示所有GM管理员");
        离线角色.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                离线角色ActionPerformed(evt);
            }
        });
        角色信息1.add(离线角色, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 530, 110, 30));

        在线角色.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        在线角色.setText("在线角色");
        在线角色.setToolTipText("显示所有GM管理员");
        在线角色.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                在线角色ActionPerformed(evt);
            }
        });
        角色信息1.add(在线角色, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 500, 110, 30));

        显示在线玩家.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        角色信息1.add(显示在线玩家, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 495, 130, 30));

        jTabbedPane8.addTab("角色信息", 角色信息1);

        角色背包.setBackground(new java.awt.Color(255, 255, 255));
        角色背包.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane5.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "角色穿戴装备信息", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色背包穿戴.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色背包穿戴.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色背包穿戴.getTableHeader().setReorderingAllowed(false);
        jScrollPane15.setViewportView(角色背包穿戴);
        if (角色背包穿戴.getColumnModel().getColumnCount() > 0) {
            角色背包穿戴.getColumnModel().getColumn(0).setResizable(false);
            角色背包穿戴.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色背包穿戴.getColumnModel().getColumn(1).setResizable(false);
            角色背包穿戴.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色背包穿戴.getColumnModel().getColumn(2).setResizable(false);
            角色背包穿戴.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel36.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 380, 530));

        背包物品名字1.setEditable(false);
        背包物品名字1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                背包物品名字1ActionPerformed(evt);
            }
        });
        jPanel36.add(背包物品名字1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        身上穿戴序号1.setEditable(false);
        身上穿戴序号1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                身上穿戴序号1ActionPerformed(evt);
            }
        });
        jPanel36.add(身上穿戴序号1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        背包物品代码1.setEditable(false);
        背包物品代码1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                背包物品代码1ActionPerformed(evt);
            }
        });
        jPanel36.add(背包物品代码1, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel276.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel276.setText("序号；");
        jPanel36.add(jLabel276, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel283.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel283.setText("物品名字；");
        jPanel36.add(jLabel283, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel287.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel287.setText("物品代码；");
        jPanel36.add(jLabel287, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除穿戴装备.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除穿戴装备.setText("删除");
        删除穿戴装备.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除穿戴装备ActionPerformed(evt);
            }
        });
        jPanel36.add(删除穿戴装备, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("身上穿戴", jPanel36);

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "装备背包", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel37.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色装备背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色装备背包.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色装备背包.getTableHeader().setReorderingAllowed(false);
        jScrollPane16.setViewportView(角色装备背包);
        if (角色装备背包.getColumnModel().getColumnCount() > 0) {
            角色装备背包.getColumnModel().getColumn(0).setResizable(false);
            角色装备背包.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色装备背包.getColumnModel().getColumn(1).setResizable(false);
            角色装备背包.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色装备背包.getColumnModel().getColumn(2).setResizable(false);
            角色装备背包.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel37.add(jScrollPane16, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 380, 530));

        装备背包物品名字.setEditable(false);
        装备背包物品名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                装备背包物品名字ActionPerformed(evt);
            }
        });
        jPanel37.add(装备背包物品名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        装备背包物品序号.setEditable(false);
        装备背包物品序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                装备背包物品序号ActionPerformed(evt);
            }
        });
        jPanel37.add(装备背包物品序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        装备背包物品代码.setEditable(false);
        装备背包物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                装备背包物品代码ActionPerformed(evt);
            }
        });
        jPanel37.add(装备背包物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel288.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel288.setText("序号；");
        jPanel37.add(jLabel288, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel289.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel289.setText("物品名字；");
        jPanel37.add(jLabel289, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel290.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel290.setText("物品代码；");
        jPanel37.add(jLabel290, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除装备背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除装备背包.setText("删除");
        删除装备背包.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除装备背包ActionPerformed(evt);
            }
        });
        jPanel37.add(删除装备背包, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("装备背包", jPanel37);

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "消耗背包", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel38.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色消耗背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色消耗背包.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字", "物品数量"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色消耗背包.getTableHeader().setReorderingAllowed(false);
        jScrollPane17.setViewportView(角色消耗背包);
        if (角色消耗背包.getColumnModel().getColumnCount() > 0) {
            角色消耗背包.getColumnModel().getColumn(0).setResizable(false);
            角色消耗背包.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色消耗背包.getColumnModel().getColumn(1).setResizable(false);
            角色消耗背包.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色消耗背包.getColumnModel().getColumn(2).setResizable(false);
            角色消耗背包.getColumnModel().getColumn(2).setPreferredWidth(100);
            角色消耗背包.getColumnModel().getColumn(3).setResizable(false);
            角色消耗背包.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jPanel38.add(jScrollPane17, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 430, 530));

        消耗背包物品名字.setEditable(false);
        消耗背包物品名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                消耗背包物品名字ActionPerformed(evt);
            }
        });
        jPanel38.add(消耗背包物品名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        消耗背包物品序号.setEditable(false);
        消耗背包物品序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                消耗背包物品序号ActionPerformed(evt);
            }
        });
        jPanel38.add(消耗背包物品序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        消耗背包物品代码.setEditable(false);
        消耗背包物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                消耗背包物品代码ActionPerformed(evt);
            }
        });
        jPanel38.add(消耗背包物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel291.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel291.setText("序号；");
        jPanel38.add(jLabel291, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel292.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel292.setText("物品名字；");
        jPanel38.add(jLabel292, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel293.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel293.setText("物品代码；");
        jPanel38.add(jLabel293, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除消耗背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除消耗背包.setText("删除");
        删除消耗背包.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除消耗背包ActionPerformed(evt);
            }
        });
        jPanel38.add(删除消耗背包, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("消耗背包", jPanel38);

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "设置背包", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel39.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色设置背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色设置背包.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字", "物品数量"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色设置背包.getTableHeader().setReorderingAllowed(false);
        jScrollPane18.setViewportView(角色设置背包);
        if (角色设置背包.getColumnModel().getColumnCount() > 0) {
            角色设置背包.getColumnModel().getColumn(0).setResizable(false);
            角色设置背包.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色设置背包.getColumnModel().getColumn(1).setResizable(false);
            角色设置背包.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色设置背包.getColumnModel().getColumn(2).setResizable(false);
            角色设置背包.getColumnModel().getColumn(2).setPreferredWidth(100);
            角色设置背包.getColumnModel().getColumn(3).setResizable(false);
            角色设置背包.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jPanel39.add(jScrollPane18, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 430, 530));

        设置背包物品名字.setEditable(false);
        设置背包物品名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                设置背包物品名字ActionPerformed(evt);
            }
        });
        jPanel39.add(设置背包物品名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        设置背包物品序号.setEditable(false);
        设置背包物品序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                设置背包物品序号ActionPerformed(evt);
            }
        });
        jPanel39.add(设置背包物品序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        设置背包物品代码.setEditable(false);
        设置背包物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                设置背包物品代码ActionPerformed(evt);
            }
        });
        jPanel39.add(设置背包物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel294.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel294.setText("序号；");
        jPanel39.add(jLabel294, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel295.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel295.setText("物品名字；");
        jPanel39.add(jLabel295, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel296.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel296.setText("物品代码；");
        jPanel39.add(jLabel296, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除设置背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除设置背包.setText("删除");
        删除设置背包.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除设置背包ActionPerformed(evt);
            }
        });
        jPanel39.add(删除设置背包, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("设置背包", jPanel39);

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "其他背包", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel40.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色其他背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色其他背包.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字", "物品数量"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色其他背包.getTableHeader().setReorderingAllowed(false);
        jScrollPane19.setViewportView(角色其他背包);
        if (角色其他背包.getColumnModel().getColumnCount() > 0) {
            角色其他背包.getColumnModel().getColumn(0).setResizable(false);
            角色其他背包.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色其他背包.getColumnModel().getColumn(1).setResizable(false);
            角色其他背包.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色其他背包.getColumnModel().getColumn(2).setResizable(false);
            角色其他背包.getColumnModel().getColumn(2).setPreferredWidth(100);
            角色其他背包.getColumnModel().getColumn(3).setResizable(false);
            角色其他背包.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jPanel40.add(jScrollPane19, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 430, 530));

        其他背包物品名字.setEditable(false);
        其他背包物品名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                其他背包物品名字ActionPerformed(evt);
            }
        });
        jPanel40.add(其他背包物品名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        其他背包物品序号.setEditable(false);
        其他背包物品序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                其他背包物品序号ActionPerformed(evt);
            }
        });
        jPanel40.add(其他背包物品序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        其他背包物品代码.setEditable(false);
        其他背包物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                其他背包物品代码ActionPerformed(evt);
            }
        });
        jPanel40.add(其他背包物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel297.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel297.setText("序号；");
        jPanel40.add(jLabel297, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel298.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel298.setText("物品名字；");
        jPanel40.add(jLabel298, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel299.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel299.setText("物品代码；");
        jPanel40.add(jLabel299, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除其他背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除其他背包.setText("删除");
        删除其他背包.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除其他背包ActionPerformed(evt);
            }
        });
        jPanel40.add(删除其他背包, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("其他背包", jPanel40);

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));
        jPanel41.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "特殊背包", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel41.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色特殊背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色特殊背包.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字", "物品数量"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色特殊背包.getTableHeader().setReorderingAllowed(false);
        jScrollPane20.setViewportView(角色特殊背包);
        if (角色特殊背包.getColumnModel().getColumnCount() > 0) {
            角色特殊背包.getColumnModel().getColumn(0).setResizable(false);
            角色特殊背包.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色特殊背包.getColumnModel().getColumn(1).setResizable(false);
            角色特殊背包.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色特殊背包.getColumnModel().getColumn(2).setResizable(false);
            角色特殊背包.getColumnModel().getColumn(2).setPreferredWidth(100);
            角色特殊背包.getColumnModel().getColumn(3).setResizable(false);
            角色特殊背包.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jPanel41.add(jScrollPane20, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 430, 530));

        特殊背包物品名字.setEditable(false);
        特殊背包物品名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                特殊背包物品名字ActionPerformed(evt);
            }
        });
        jPanel41.add(特殊背包物品名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        特殊背包物品序号.setEditable(false);
        特殊背包物品序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                特殊背包物品序号ActionPerformed(evt);
            }
        });
        jPanel41.add(特殊背包物品序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        特殊背包物品代码.setEditable(false);
        特殊背包物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                特殊背包物品代码ActionPerformed(evt);
            }
        });
        jPanel41.add(特殊背包物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel300.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel300.setText("序号；");
        jPanel41.add(jLabel300, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel301.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel301.setText("物品名字；");
        jPanel41.add(jLabel301, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel302.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel302.setText("物品代码；");
        jPanel41.add(jLabel302, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除特殊背包.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除特殊背包.setText("删除");
        删除特殊背包.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除特殊背包ActionPerformed(evt);
            }
        });
        jPanel41.add(删除特殊背包, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("特殊背包", jPanel41);

        jPanel42.setBackground(new java.awt.Color(255, 255, 255));
        jPanel42.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "游戏仓库", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel42.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色游戏仓库.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色游戏仓库.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字", "物品数量"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色游戏仓库.getTableHeader().setReorderingAllowed(false);
        jScrollPane21.setViewportView(角色游戏仓库);
        if (角色游戏仓库.getColumnModel().getColumnCount() > 0) {
            角色游戏仓库.getColumnModel().getColumn(0).setResizable(false);
            角色游戏仓库.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色游戏仓库.getColumnModel().getColumn(1).setResizable(false);
            角色游戏仓库.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色游戏仓库.getColumnModel().getColumn(2).setResizable(false);
            角色游戏仓库.getColumnModel().getColumn(2).setPreferredWidth(100);
            角色游戏仓库.getColumnModel().getColumn(3).setResizable(false);
            角色游戏仓库.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jPanel42.add(jScrollPane21, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 430, 530));

        游戏仓库物品名字.setEditable(false);
        游戏仓库物品名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏仓库物品名字ActionPerformed(evt);
            }
        });
        jPanel42.add(游戏仓库物品名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        游戏仓库物品序号.setEditable(false);
        游戏仓库物品序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏仓库物品序号ActionPerformed(evt);
            }
        });
        jPanel42.add(游戏仓库物品序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        游戏仓库物品代码.setEditable(false);
        游戏仓库物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                游戏仓库物品代码ActionPerformed(evt);
            }
        });
        jPanel42.add(游戏仓库物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel303.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel303.setText("序号；");
        jPanel42.add(jLabel303, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel304.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel304.setText("物品名字；");
        jPanel42.add(jLabel304, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel305.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel305.setText("物品代码；");
        jPanel42.add(jLabel305, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除游戏仓库.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除游戏仓库.setText("删除");
        删除游戏仓库.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除游戏仓库ActionPerformed(evt);
            }
        });
        jPanel42.add(删除游戏仓库, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("游戏仓库", jPanel42);

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "商城仓库", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel43.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色商城仓库.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色商城仓库.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字", "物品数量"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色商城仓库.getTableHeader().setReorderingAllowed(false);
        jScrollPane22.setViewportView(角色商城仓库);
        if (角色商城仓库.getColumnModel().getColumnCount() > 0) {
            角色商城仓库.getColumnModel().getColumn(0).setResizable(false);
            角色商城仓库.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色商城仓库.getColumnModel().getColumn(1).setResizable(false);
            角色商城仓库.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色商城仓库.getColumnModel().getColumn(2).setResizable(false);
            角色商城仓库.getColumnModel().getColumn(2).setPreferredWidth(100);
            角色商城仓库.getColumnModel().getColumn(3).setResizable(false);
            角色商城仓库.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jPanel43.add(jScrollPane22, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 430, 530));

        商城仓库物品名字.setEditable(false);
        商城仓库物品名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                商城仓库物品名字ActionPerformed(evt);
            }
        });
        jPanel43.add(商城仓库物品名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        商城仓库物品序号.setEditable(false);
        商城仓库物品序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                商城仓库物品序号ActionPerformed(evt);
            }
        });
        jPanel43.add(商城仓库物品序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        商城仓库物品代码.setEditable(false);
        商城仓库物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                商城仓库物品代码ActionPerformed(evt);
            }
        });
        jPanel43.add(商城仓库物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel306.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel306.setText("序号；");
        jPanel43.add(jLabel306, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel307.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel307.setText("物品名字；");
        jPanel43.add(jLabel307, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel308.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel308.setText("物品代码；");
        jPanel43.add(jLabel308, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除商城仓库.setText("删除");
        删除商城仓库.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除商城仓库ActionPerformed(evt);
            }
        });
        jPanel43.add(删除商城仓库, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("商城仓库", jPanel43);

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "点券拍卖行", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel48.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色点券拍卖行.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色点券拍卖行.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字", "物品数量"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色点券拍卖行.getTableHeader().setReorderingAllowed(false);
        jScrollPane30.setViewportView(角色点券拍卖行);
        if (角色点券拍卖行.getColumnModel().getColumnCount() > 0) {
            角色点券拍卖行.getColumnModel().getColumn(0).setResizable(false);
            角色点券拍卖行.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色点券拍卖行.getColumnModel().getColumn(1).setResizable(false);
            角色点券拍卖行.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色点券拍卖行.getColumnModel().getColumn(2).setResizable(false);
            角色点券拍卖行.getColumnModel().getColumn(2).setPreferredWidth(100);
            角色点券拍卖行.getColumnModel().getColumn(3).setResizable(false);
            角色点券拍卖行.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jPanel48.add(jScrollPane30, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 430, 530));

        拍卖行物品名字1.setEditable(false);
        拍卖行物品名字1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                拍卖行物品名字1ActionPerformed(evt);
            }
        });
        jPanel48.add(拍卖行物品名字1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        角色点券拍卖行序号.setEditable(false);
        角色点券拍卖行序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                角色点券拍卖行序号ActionPerformed(evt);
            }
        });
        jPanel48.add(角色点券拍卖行序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        拍卖行物品代码1.setEditable(false);
        拍卖行物品代码1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                拍卖行物品代码1ActionPerformed(evt);
            }
        });
        jPanel48.add(拍卖行物品代码1, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel354.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel354.setText("序号；");
        jPanel48.add(jLabel354, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel355.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel355.setText("物品名字；");
        jPanel48.add(jLabel355, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel356.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel356.setText("物品代码；");
        jPanel48.add(jLabel356, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除拍卖行1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除拍卖行1.setText("删除");
        删除拍卖行1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除拍卖行1ActionPerformed(evt);
            }
        });
        jPanel48.add(删除拍卖行1, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("点券拍卖行", jPanel48);

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));
        jPanel44.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "金币拍卖行", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel44.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        角色金币拍卖行.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        角色金币拍卖行.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "物品代码", "物品名字"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        角色金币拍卖行.getTableHeader().setReorderingAllowed(false);
        jScrollPane23.setViewportView(角色金币拍卖行);
        if (角色金币拍卖行.getColumnModel().getColumnCount() > 0) {
            角色金币拍卖行.getColumnModel().getColumn(0).setResizable(false);
            角色金币拍卖行.getColumnModel().getColumn(0).setPreferredWidth(30);
            角色金币拍卖行.getColumnModel().getColumn(1).setResizable(false);
            角色金币拍卖行.getColumnModel().getColumn(1).setPreferredWidth(30);
            角色金币拍卖行.getColumnModel().getColumn(2).setResizable(false);
            角色金币拍卖行.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        jPanel44.add(jScrollPane23, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 430, 530));

        拍卖行物品名字.setEditable(false);
        拍卖行物品名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                拍卖行物品名字ActionPerformed(evt);
            }
        });
        jPanel44.add(拍卖行物品名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 270, 150, 30));

        角色金币拍卖行序号.setEditable(false);
        角色金币拍卖行序号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                角色金币拍卖行序号ActionPerformed(evt);
            }
        });
        jPanel44.add(角色金币拍卖行序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 110, 30));

        拍卖行物品代码.setEditable(false);
        拍卖行物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                拍卖行物品代码ActionPerformed(evt);
            }
        });
        jPanel44.add(拍卖行物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 270, 110, 30));

        jLabel309.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel309.setText("序号；");
        jPanel44.add(jLabel309, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, 20));

        jLabel310.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel310.setText("物品名字；");
        jPanel44.add(jLabel310, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 250, -1, 20));

        jLabel311.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel311.setText("物品代码；");
        jPanel44.add(jLabel311, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, -1, 20));

        删除拍卖行.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除拍卖行.setText("删除");
        删除拍卖行.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除拍卖行ActionPerformed(evt);
            }
        });
        jPanel44.add(删除拍卖行, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 270, -1, 30));

        jTabbedPane5.addTab("金币拍卖行", jPanel44);

        角色背包.add(jTabbedPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 620));

        jTabbedPane8.addTab("角色道具信息", 角色背包);

        技能.setBackground(new java.awt.Color(255, 255, 255));
        技能.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "角色技能", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        技能.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        技能信息.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        技能信息.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "技能名字", "技能代码", "目前等级", "最高等级"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        技能信息.getTableHeader().setReorderingAllowed(false);
        jScrollPane13.setViewportView(技能信息);
        if (技能信息.getColumnModel().getColumnCount() > 0) {
            技能信息.getColumnModel().getColumn(0).setResizable(false);
            技能信息.getColumnModel().getColumn(0).setPreferredWidth(30);
            技能信息.getColumnModel().getColumn(1).setResizable(false);
            技能信息.getColumnModel().getColumn(1).setPreferredWidth(50);
            技能信息.getColumnModel().getColumn(2).setResizable(false);
            技能信息.getColumnModel().getColumn(3).setResizable(false);
            技能信息.getColumnModel().getColumn(3).setPreferredWidth(50);
            技能信息.getColumnModel().getColumn(4).setResizable(false);
        }

        技能.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 520, 560));

        技能代码.setEditable(false);
        技能代码.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        技能.add(技能代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 180, 120, 30));

        技能目前等级.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        技能.add(技能目前等级, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 180, 120, 30));

        技能最高等级.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        技能.add(技能最高等级, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 180, 120, 30));

        技能名字.setEditable(false);
        技能名字.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        技能名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                技能名字ActionPerformed(evt);
            }
        });
        技能.add(技能名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 180, 120, 30));

        jLabel86.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel86.setText("技能代码；");
        技能.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 160, -1, -1));

        jLabel89.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel89.setText("目前等级；");
        技能.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 160, -1, -1));

        jLabel107.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel107.setText("最高等级；");
        技能.add(jLabel107, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 160, -1, -1));

        jLabel108.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel108.setText("技能无法超出正常范围值。");
        技能.add(jLabel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 560, 490, 30));

        修改技能.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改技能.setText("修改");
        修改技能.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改技能ActionPerformed(evt);
            }
        });
        技能.add(修改技能, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 280, 120, 40));

        技能序号.setEditable(false);
        技能序号.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        技能.add(技能序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 180, 80, 30));

        jLabel188.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel188.setText("技能名字；");
        技能.add(jLabel188, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 160, -1, -1));

        jLabel204.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel204.setText("序号；");
        技能.add(jLabel204, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 160, -1, -1));

        jLabel205.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel205.setText("提示；");
        技能.add(jLabel205, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 530, 120, 30));

        删除技能.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        删除技能.setText("删除");
        删除技能.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除技能ActionPerformed(evt);
            }
        });
        技能.add(删除技能, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 280, 120, 40));

        修改技能1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        修改技能1.setText("刷新");
        修改技能1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改技能1ActionPerformed(evt);
            }
        });
        技能.add(修改技能1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 280, 120, 40));

        jTabbedPane8.addTab("角色技能信息", 技能);

        角色列表.add(jTabbedPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1257, 655));

        角色提示语言.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        角色提示语言.setText("[信息]：");
        角色列表.add(角色提示语言, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 655, 1240, 25));

        jTabbedPane1.addTab("玩家信息", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 角色列表); // NOI18N

        拍卖行.setBackground(new java.awt.Color(255, 255, 255));
        拍卖行.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane7.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "点券拍卖行", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        金币拍卖行.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        金币拍卖行.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "商品编号", "卖家", "商品状态", "买家", "商品价格", "商品名称"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        金币拍卖行.getTableHeader().setReorderingAllowed(false);
        jScrollPane27.setViewportView(金币拍卖行);
        if (金币拍卖行.getColumnModel().getColumnCount() > 0) {
            金币拍卖行.getColumnModel().getColumn(0).setResizable(false);
            金币拍卖行.getColumnModel().getColumn(0).setPreferredWidth(10);
            金币拍卖行.getColumnModel().getColumn(1).setResizable(false);
            金币拍卖行.getColumnModel().getColumn(2).setResizable(false);
            金币拍卖行.getColumnModel().getColumn(3).setResizable(false);
            金币拍卖行.getColumnModel().getColumn(4).setResizable(false);
            金币拍卖行.getColumnModel().getColumn(5).setResizable(false);
            金币拍卖行.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        jPanel2.add(jScrollPane27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 880, 580));

        刷新金币拍卖行.setText("刷新");
        刷新金币拍卖行.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新金币拍卖行ActionPerformed(evt);
            }
        });
        jPanel2.add(刷新金币拍卖行, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 570, 100, 40));

        jTabbedPane7.addTab("点券拍卖行", jPanel2);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "金币拍卖行", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        点券拍卖行.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        点券拍卖行.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "商品编号", "卖家", "商品状态", "买家", "商品价格", "商品名称"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        点券拍卖行.getTableHeader().setReorderingAllowed(false);
        jScrollPane28.setViewportView(点券拍卖行);
        if (点券拍卖行.getColumnModel().getColumnCount() > 0) {
            点券拍卖行.getColumnModel().getColumn(0).setResizable(false);
            点券拍卖行.getColumnModel().getColumn(0).setPreferredWidth(10);
            点券拍卖行.getColumnModel().getColumn(1).setResizable(false);
            点券拍卖行.getColumnModel().getColumn(2).setResizable(false);
            点券拍卖行.getColumnModel().getColumn(3).setResizable(false);
            点券拍卖行.getColumnModel().getColumn(4).setResizable(false);
            点券拍卖行.getColumnModel().getColumn(5).setResizable(false);
            点券拍卖行.getColumnModel().getColumn(5).setPreferredWidth(150);
        }

        jPanel11.add(jScrollPane28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 880, 580));

        刷新金币拍卖行1.setText("刷新");
        刷新金币拍卖行1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新金币拍卖行1ActionPerformed(evt);
            }
        });
        jPanel11.add(刷新金币拍卖行1, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 570, 100, 40));

        jTabbedPane7.addTab("金币拍卖行", jPanel11);

        拍卖行.add(jTabbedPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 680));

        jTabbedPane1.addTab("拍卖行", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 拍卖行); // NOI18N

        发送福利.setBackground(new java.awt.Color(255, 255, 255));
        发送福利.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane3.setFont(new java.awt.Font("幼圆", 0, 12)); // NOI18N

        福利记录符号.setBackground(new java.awt.Color(255, 255, 255));
        福利记录符号.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "全服发送福利", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        福利记录符号.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "全服发送福利", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        全服发送物品数量.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送物品数量ActionPerformed(evt);
            }
        });
        jPanel24.add(全服发送物品数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 100, 30));

        全服发送物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送物品代码ActionPerformed(evt);
            }
        });
        jPanel24.add(全服发送物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 110, 30));

        给予物品1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        给予物品1.setText("给予物品");
        给予物品1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                给予物品1ActionPerformed(evt);
            }
        });
        jPanel24.add(给予物品1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 100, 30));

        jLabel217.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel217.setText("物品数量；");
        jPanel24.add(jLabel217, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, -1, -1));

        jLabel234.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel234.setText("物品代码；");
        jPanel24.add(jLabel234, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        福利记录符号.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 510, 110));

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "全服发送福利", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        全服发送装备装备加卷.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备加卷ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备加卷, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, 100, 30));

        全服发送装备装备制作人.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备制作人ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备制作人, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 60, 100, 30));

        全服发送装备装备力量.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备力量ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备力量, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, 100, 30));

        全服发送装备装备MP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备MPActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备MP, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 100, 30));

        全服发送装备装备智力.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备智力ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备智力, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 180, 100, 30));

        全服发送装备装备运气.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备运气ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备运气, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, 100, 30));

        全服发送装备装备HP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备HPActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备HP, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, 100, 30));

        全服发送装备装备攻击力.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备攻击力ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备攻击力, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 100, 30));

        全服发送装备装备给予时间.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备给予时间ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备给予时间, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 240, 100, 30));

        全服发送装备装备可否交易.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备可否交易ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备可否交易, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 180, 100, 30));

        全服发送装备装备敏捷.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备敏捷ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备敏捷, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, 100, 30));

        全服发送装备物品ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备物品IDActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备物品ID, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 100, 30));

        全服发送装备装备魔法力.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备魔法力ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备魔法力, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 100, 30));

        全服发送装备装备魔法防御.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备魔法防御ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备魔法防御, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, 100, 30));

        全服发送装备装备物理防御.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                全服发送装备装备物理防御ActionPerformed(evt);
            }
        });
        jPanel25.add(全服发送装备装备物理防御, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, 100, 30));

        给予装备1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        给予装备1.setText("个人发送");
        给予装备1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                给予装备1ActionPerformed(evt);
            }
        });
        jPanel25.add(给予装备1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 160, 100, 30));

        jLabel219.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel219.setText("能否交易；填0");
        jPanel25.add(jLabel219, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 160, -1, -1));

        jLabel220.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel220.setText("HP加成；");
        jPanel25.add(jLabel220, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, -1, -1));

        jLabel221.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel221.setText("魔法攻击力；");
        jPanel25.add(jLabel221, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, -1, -1));

        jLabel222.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel222.setText("装备代码；");
        jPanel25.add(jLabel222, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, -1, -1));

        jLabel223.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel223.setText("MP加成；");
        jPanel25.add(jLabel223, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, -1));

        jLabel224.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel224.setText("物理攻击力；");
        jPanel25.add(jLabel224, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, -1, -1));

        jLabel225.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel225.setText("可砸卷次数；");
        jPanel25.add(jLabel225, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, -1, -1));

        jLabel226.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel226.setText("装备署名；");
        jPanel25.add(jLabel226, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 40, -1, -1));

        jLabel227.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel227.setText("装备力量；");
        jPanel25.add(jLabel227, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, -1, -1));

        jLabel228.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel228.setText("装备敏捷；");
        jPanel25.add(jLabel228, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, -1, -1));

        jLabel229.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel229.setText("装备智力；");
        jPanel25.add(jLabel229, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 160, -1, -1));

        jLabel230.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel230.setText("装备运气；");
        jPanel25.add(jLabel230, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 100, -1, -1));

        jLabel231.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel231.setText("魔法防御；");
        jPanel25.add(jLabel231, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, -1, -1));

        jLabel232.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel232.setText("物理防御；");
        jPanel25.add(jLabel232, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, -1, -1));

        jLabel233.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel233.setText("限时时间；");
        jPanel25.add(jLabel233, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, -1, -1));

        发送装备玩家姓名.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                发送装备玩家姓名ActionPerformed(evt);
            }
        });
        jPanel25.add(发送装备玩家姓名, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 60, 100, 30));

        给予装备2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        给予装备2.setText("全服发送");
        给予装备2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                给予装备2ActionPerformed(evt);
            }
        });
        jPanel25.add(给予装备2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 120, 100, 30));

        jLabel246.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel246.setText("玩家名字；");
        jPanel25.add(jLabel246, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 40, -1, -1));

        jLabel244.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel244.setText("个人发送需要填写名字");
        jPanel25.add(jLabel244, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 250, -1, -1));

        福利记录符号.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 760, 290));

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "全服发送福利", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        z2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        z2.setText("发送抵用");
        z2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                z2ActionPerformed(evt);
            }
        });
        jPanel26.add(z2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 100, 30));

        z3.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        z3.setText("发送金币");
        z3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                z3ActionPerformed(evt);
            }
        });
        jPanel26.add(z3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 100, 30));

        z1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        z1.setText("发送点券");
        z1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                z1ActionPerformed(evt);
            }
        });
        jPanel26.add(z1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 100, 30));

        z4.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        z4.setText("发送经验");
        z4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                z4ActionPerformed(evt);
            }
        });
        jPanel26.add(z4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 100, 30));

        z5.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        z5.setText("发送人气");
        z5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                z5ActionPerformed(evt);
            }
        });
        jPanel26.add(z5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, 100, 30));

        z6.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        z6.setText("发送豆豆");
        z6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                z6ActionPerformed(evt);
            }
        });
        jPanel26.add(z6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 100, 30));

        a1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a1ActionPerformed(evt);
            }
        });
        jPanel26.add(a1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 100, 30));

        jLabel235.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel235.setText("数量；");
        jPanel26.add(jLabel235, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, -1));

        福利记录符号.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, 240, 270));

        福利提示语言.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        福利提示语言.setText("[信息]：");
        福利记录符号.add(福利提示语言, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 620, 810, 25));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "个人发送物品", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        个人发送物品数量.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                个人发送物品数量ActionPerformed(evt);
            }
        });
        jPanel27.add(个人发送物品数量, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, 80, 30));

        个人发送物品玩家名字.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                个人发送物品玩家名字ActionPerformed(evt);
            }
        });
        jPanel27.add(个人发送物品玩家名字, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 130, 30));

        个人发送物品代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                个人发送物品代码ActionPerformed(evt);
            }
        });
        jPanel27.add(个人发送物品代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 130, 30));

        给予物品.setBackground(new java.awt.Color(255, 255, 255));
        给予物品.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        给予物品.setText("给予物品");
        给予物品.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                给予物品ActionPerformed(evt);
            }
        });
        jPanel27.add(给予物品, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, 100, 30));

        jLabel240.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel240.setText("物品数量；");
        jPanel27.add(jLabel240, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, -1, -1));

        jLabel241.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel241.setText("玩家名字；");
        jPanel27.add(jLabel241, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel242.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel242.setText("物品代码；");
        jPanel27.add(jLabel242, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, -1));

        福利记录符号.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 150, 510, 150));

        存储发送物品.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "序号", "名字", "代码"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        存储发送物品.getTableHeader().setReorderingAllowed(false);
        jScrollPane12.setViewportView(存储发送物品);
        if (存储发送物品.getColumnModel().getColumnCount() > 0) {
            存储发送物品.getColumnModel().getColumn(0).setResizable(false);
            存储发送物品.getColumnModel().getColumn(0).setPreferredWidth(30);
            存储发送物品.getColumnModel().getColumn(1).setResizable(false);
            存储发送物品.getColumnModel().getColumn(1).setPreferredWidth(200);
            存储发送物品.getColumnModel().getColumn(2).setResizable(false);
            存储发送物品.getColumnModel().getColumn(2).setPreferredWidth(70);
        }

        福利记录符号.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 360, 480));

        刷新个人.setText("刷新记录[个人]");
        刷新个人.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新个人ActionPerformed(evt);
            }
        });
        福利记录符号.add(刷新个人, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 610, 190, 30));

        刷新通用.setText("刷新记录[通用]");
        刷新通用.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新通用ActionPerformed(evt);
            }
        });
        福利记录符号.add(刷新通用, new org.netbeans.lib.awtextra.AbsoluteConstraints(1069, 610, 170, 30));
        福利记录符号.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 520, 170, 30));

        福利序号.setEditable(false);
        福利记录符号.add(福利序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 520, 80, 30));

        新增个人.setText("新增记录[个人]");
        新增个人.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增个人ActionPerformed(evt);
            }
        });
        福利记录符号.add(新增个人, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 550, 190, 30));

        新增通用.setText("新增记录[通用]");
        新增通用.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增通用ActionPerformed(evt);
            }
        });
        福利记录符号.add(新增通用, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 550, 170, 30));

        删除通用.setText("删除记录[通用]");
        删除通用.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除通用ActionPerformed(evt);
            }
        });
        福利记录符号.add(删除通用, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 580, 170, 30));

        删除个人.setText("删除记录[个人]");
        删除个人.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                删除个人ActionPerformed(evt);
            }
        });
        福利记录符号.add(删除个人, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 580, 190, 30));

        修改记录.setText("修改");
        修改记录.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改记录ActionPerformed(evt);
            }
        });
        福利记录符号.add(修改记录, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 520, 110, 30));

        jTabbedPane3.addTab("福利发送", 福利记录符号);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "玩家在线泡点", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        在线泡点设置.setFont(new java.awt.Font("幼圆", 0, 20)); // NOI18N
        在线泡点设置.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "类型", "数值"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        在线泡点设置.getTableHeader().setReorderingAllowed(false);
        jScrollPane134.setViewportView(在线泡点设置);
        if (在线泡点设置.getColumnModel().getColumnCount() > 0) {
            在线泡点设置.getColumnModel().getColumn(0).setResizable(false);
            在线泡点设置.getColumnModel().getColumn(0).setPreferredWidth(50);
            在线泡点设置.getColumnModel().getColumn(1).setResizable(false);
            在线泡点设置.getColumnModel().getColumn(1).setPreferredWidth(200);
            在线泡点设置.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel1.add(jScrollPane134, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 480, 550));

        泡点序号.setEditable(false);
        jPanel1.add(泡点序号, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 220, 100, 30));

        泡点类型.setEditable(false);
        jPanel1.add(泡点类型, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 220, 210, 30));
        jPanel1.add(泡点值, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 220, 120, 30));

        泡点值修改.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        泡点值修改.setText("修改");
        泡点值修改.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                泡点值修改ActionPerformed(evt);
            }
        });
        jPanel1.add(泡点值修改, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 220, 120, 30));

        jLabel322.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel322.setText("类型数值；");
        jPanel1.add(jLabel322, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 200, -1, -1));

        jLabel323.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel323.setText("提示：泡点时间为30分钟,其他设置即时生效。");
        jPanel1.add(jLabel323, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 60, -1, -1));

        jLabel324.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel324.setText("泡点奖励类型；");
        jPanel1.add(jLabel324, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 200, -1, -1));

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));
        jPanel45.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "在线泡点设置", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel45.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        泡点金币开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        泡点金币开关.setText("泡点金币");
        泡点金币开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                泡点金币开关ActionPerformed(evt);
            }
        });
        jPanel45.add(泡点金币开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 260, 40));

        泡点经验开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        泡点经验开关.setText("泡点经验");
        泡点经验开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                泡点经验开关ActionPerformed(evt);
            }
        });
        jPanel45.add(泡点经验开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 260, 40));

        泡点点券开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        泡点点券开关.setText("泡点点券");
        泡点点券开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                泡点点券开关ActionPerformed(evt);
            }
        });
        jPanel45.add(泡点点券开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 260, 40));

        泡点抵用开关.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        泡点抵用开关.setText("泡点抵用");
        泡点抵用开关.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                泡点抵用开关ActionPerformed(evt);
            }
        });
        jPanel45.add(泡点抵用开关, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 260, 40));

        jPanel1.add(jPanel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 290, 340, 300));

        jLabel325.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel325.setText("序号；");
        jPanel1.add(jLabel325, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 200, -1, -1));

        福利提示语言2.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        福利提示语言2.setText("[信息]：");
        jPanel1.add(福利提示语言2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 620, 1240, 25));

        jTabbedPane3.addTab("玩家在线泡点", jPanel1);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "雇佣在线泡点", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        雇佣在线泡点设置.setFont(new java.awt.Font("幼圆", 0, 20)); // NOI18N
        雇佣在线泡点设置.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "序号", "类型", "数值"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        雇佣在线泡点设置.getTableHeader().setReorderingAllowed(false);
        jScrollPane135.setViewportView(雇佣在线泡点设置);
        if (雇佣在线泡点设置.getColumnModel().getColumnCount() > 0) {
            雇佣在线泡点设置.getColumnModel().getColumn(0).setResizable(false);
            雇佣在线泡点设置.getColumnModel().getColumn(0).setPreferredWidth(50);
            雇佣在线泡点设置.getColumnModel().getColumn(1).setResizable(false);
            雇佣在线泡点设置.getColumnModel().getColumn(1).setPreferredWidth(200);
            雇佣在线泡点设置.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel3.add(jScrollPane135, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 480, 550));

        泡点序号1.setEditable(false);
        jPanel3.add(泡点序号1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 220, 100, 30));

        jLabel326.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel326.setText("序号；");
        jPanel3.add(jLabel326, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 200, -1, -1));

        泡点类型1.setEditable(false);
        jPanel3.add(泡点类型1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 220, 210, 30));

        jLabel327.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel327.setText("泡点奖励类型；");
        jPanel3.add(jLabel327, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 200, -1, -1));
        jPanel3.add(泡点值1, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 220, 120, 30));

        jLabel328.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        jLabel328.setText("类型数值；");
        jPanel3.add(jLabel328, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 200, -1, -1));

        泡点值修改1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        泡点值修改1.setText("修改");
        泡点值修改1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                泡点值修改1ActionPerformed(evt);
            }
        });
        jPanel3.add(泡点值修改1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 220, 120, 30));

        jLabel329.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel329.setText("提示：泡点时间为30分钟,其他设置即时生效。");
        jPanel3.add(jLabel329, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 60, -1, -1));

        jTabbedPane3.addTab("雇佣在线泡点", jPanel3);

        发送福利.add(jTabbedPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 680));

        jTabbedPane1.addTab("游戏福利", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 发送福利); // NOI18N

        发布公告.setBackground(new java.awt.Color(255, 255, 255));
        发布公告.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "游戏公告发布", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("幼圆", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        发布公告.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton31.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jButton31.setText("蓝色弹窗公告");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        发布公告.add(jButton31, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 350, 130, 50));

        jButton32.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jButton32.setText("聊天蓝色公告");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        发布公告.add(jButton32, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 350, 130, 50));

        jButton33.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jButton33.setText("顶端滚动公告");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });
        发布公告.add(jButton33, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 350, 130, 50));

        公告填写.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        公告填写.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                公告填写ActionPerformed(evt);
            }
        });
        发布公告.add(公告填写, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 1060, 170));

        公告发布喇叭代码.setText("5120027");
        公告发布喇叭代码.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                公告发布喇叭代码ActionPerformed(evt);
            }
        });
        发布公告.add(公告发布喇叭代码, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 370, 90, 30));

        jButton34.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jButton34.setText("屏幕正中公告");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });
        发布公告.add(jButton34, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 350, 130, 50));

        jLabel106.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel106.setText("4、不得含有法律、行政法规禁止的其他内容");
        发布公告.add(jLabel106, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 580, 680, 40));

        jLabel117.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel117.setText("1、不得散布谣言，扰乱社会秩序，破坏社会稳定的信息 ");
        发布公告.add(jLabel117, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 460, 680, 40));

        jLabel118.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel118.setText("2、不得散布赌博、暴力、凶杀、恐怖或者教唆犯罪的信息");
        发布公告.add(jLabel118, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 500, 680, 40));

        jLabel119.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        jLabel119.setText("3、不得侮辱或者诽谤他人，侵害他人合法权益");
        发布公告.add(jLabel119, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 540, 680, 40));

        jLabel259.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel259.setText("喇叭代码；");
        发布公告.add(jLabel259, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 350, -1, -1));

        公告提示语言.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        公告提示语言.setText("[信息]：");
        发布公告.add(公告提示语言, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 650, 1240, 25));

        jTabbedPane1.addTab("游戏/群公告", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 发布公告); // NOI18N

        其他设置.setBackground(new java.awt.Color(255, 255, 255));
        其他设置.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        家族信息.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族信息.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "家族ID", "家族名称", "族长/角色ID", "成员1", "成员2", "成员3", "成员4", "成员5", "家族GP"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        家族信息.getTableHeader().setReorderingAllowed(false);
        jScrollPane24.setViewportView(家族信息);
        if (家族信息.getColumnModel().getColumnCount() > 0) {
            家族信息.getColumnModel().getColumn(0).setResizable(false);
            家族信息.getColumnModel().getColumn(0).setPreferredWidth(10);
            家族信息.getColumnModel().getColumn(1).setResizable(false);
            家族信息.getColumnModel().getColumn(1).setPreferredWidth(80);
            家族信息.getColumnModel().getColumn(1).setHeaderValue("家族名称");
            家族信息.getColumnModel().getColumn(2).setResizable(false);
            家族信息.getColumnModel().getColumn(2).setHeaderValue("族长/角色ID");
            家族信息.getColumnModel().getColumn(3).setHeaderValue("成员1");
            家族信息.getColumnModel().getColumn(4).setResizable(false);
            家族信息.getColumnModel().getColumn(4).setPreferredWidth(130);
            家族信息.getColumnModel().getColumn(4).setHeaderValue("成员2");
            家族信息.getColumnModel().getColumn(5).setHeaderValue("成员3");
            家族信息.getColumnModel().getColumn(6).setResizable(false);
            家族信息.getColumnModel().getColumn(7).setResizable(false);
            家族信息.getColumnModel().getColumn(7).setHeaderValue("成员5");
            家族信息.getColumnModel().getColumn(8).setResizable(false);
            家族信息.getColumnModel().getColumn(8).setHeaderValue("家族GP");
        }

        其他设置.add(jScrollPane24, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 480));

        刷新家族信息.setText("刷新家族信息");
        刷新家族信息.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新家族信息ActionPerformed(evt);
            }
        });
        其他设置.add(刷新家族信息, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 577, -1, 40));

        jLabel194.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel194.setText("家族ID；");
        其他设置.add(jLabel194, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, -1, -1));

        家族ID.setEditable(false);
        家族ID.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                家族IDActionPerformed(evt);
            }
        });
        其他设置.add(家族ID, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 90, 30));

        家族名称.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族名称.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                家族名称ActionPerformed(evt);
            }
        });
        其他设置.add(家族名称, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 510, 140, 30));

        jLabel195.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel195.setText("家族名称；");
        其他设置.add(jLabel195, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 490, -1, -1));

        家族族长.setEditable(false);
        家族族长.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族族长.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                家族族长ActionPerformed(evt);
            }
        });
        其他设置.add(家族族长, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 510, 140, 30));

        jLabel196.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel196.setText("家族族长；");
        其他设置.add(jLabel196, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 490, -1, -1));

        jLabel197.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel197.setText("家族成员1；");
        其他设置.add(jLabel197, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 490, -1, -1));

        家族成员1.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族成员1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                家族成员1ActionPerformed(evt);
            }
        });
        其他设置.add(家族成员1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 510, 140, 30));

        jLabel198.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel198.setText("家族成员2；");
        其他设置.add(jLabel198, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 490, -1, -1));

        家族成员2.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族成员2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                家族成员2ActionPerformed(evt);
            }
        });
        其他设置.add(家族成员2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 510, 140, 30));

        jLabel199.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel199.setText("家族成员3；");
        其他设置.add(jLabel199, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 490, -1, -1));

        家族成员3.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族成员3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                家族成员3ActionPerformed(evt);
            }
        });
        其他设置.add(家族成员3, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 510, 140, 30));

        jLabel200.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel200.setText("家族成员4；");
        其他设置.add(jLabel200, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 490, -1, -1));

        家族成员4.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族成员4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                家族成员4ActionPerformed(evt);
            }
        });
        其他设置.add(家族成员4, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 510, 140, 30));

        jLabel313.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel313.setText("家族成员5；");
        其他设置.add(jLabel313, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 490, -1, -1));

        家族成员5.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族成员5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                家族成员5ActionPerformed(evt);
            }
        });
        其他设置.add(家族成员5, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 510, 140, 30));

        jLabel314.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel314.setText("家族GP；");
        其他设置.add(jLabel314, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 490, -1, -1));

        家族GP.setFont(new java.awt.Font("幼圆", 0, 15)); // NOI18N
        家族GP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                家族GPActionPerformed(evt);
            }
        });
        其他设置.add(家族GP, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 510, 140, 30));

        jTabbedPane1.addTab("游戏家族", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 其他设置); // NOI18N

        PVP技能.setBackground(new java.awt.Color(255, 255, 255));
        PVP技能.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PVP技能设置.setFont(new java.awt.Font("幼圆", 0, 20)); // NOI18N
        PVP技能设置.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "编号", "技能ID", "技能伤害", "技能名"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane26.setViewportView(PVP技能设置);
        if (PVP技能设置.getColumnModel().getColumnCount() > 0) {
            PVP技能设置.getColumnModel().getColumn(0).setResizable(false);
            PVP技能设置.getColumnModel().getColumn(1).setResizable(false);
            PVP技能设置.getColumnModel().getColumn(1).setPreferredWidth(100);
            PVP技能设置.getColumnModel().getColumn(2).setResizable(false);
            PVP技能设置.getColumnModel().getColumn(2).setPreferredWidth(100);
            PVP技能设置.getColumnModel().getColumn(3).setResizable(false);
            PVP技能设置.getColumnModel().getColumn(3).setPreferredWidth(200);
        }

        PVP技能.add(jScrollPane26, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 470, 640));

        技能PVP编号.setEditable(false);
        PVP技能.add(技能PVP编号, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 240, 60, -1));
        PVP技能.add(技能PVPID, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 240, 120, -1));
        PVP技能.add(技能PVP伤害, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 240, 100, -1));

        jLabel218.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel218.setText("技能伤害；");
        PVP技能.add(jLabel218, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 220, -1, -1));

        jLabel236.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        jLabel236.setText("技能代码；");
        PVP技能.add(jLabel236, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 220, -1, -1));

        刷新PVP列表.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        刷新PVP列表.setText("刷新列表");
        刷新PVP列表.setToolTipText("");
        刷新PVP列表.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                刷新PVP列表ActionPerformed(evt);
            }
        });
        PVP技能.add(刷新PVP列表, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 180, 130, 30));

        jButton2.setText("删除");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        PVP技能.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 280, 80, 30));

        新增1.setText("新增");
        新增1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                新增1ActionPerformed(evt);
            }
        });
        PVP技能.add(新增1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 280, 80, 30));

        修改1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        修改1.setText("修改");
        修改1.setToolTipText("");
        修改1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                修改1ActionPerformed(evt);
            }
        });
        PVP技能.add(修改1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 280, 80, 30));

        搜索1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        搜索1.setText("搜索");
        搜索1.setToolTipText("");
        搜索1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                搜索1ActionPerformed(evt);
            }
        });
        PVP技能.add(搜索1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 280, 80, 30));

        jLabel268.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel268.setText("如果没有设置就是默认的伤害。");
        PVP技能.add(jLabel268, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 430, 370, 30));

        jLabel269.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel269.setText("设置技能每个等级的伤害。");
        PVP技能.add(jLabel269, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 400, 370, 30));

        jLabel271.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel271.setText("新增或者修改即时生效，无需重启。");
        PVP技能.add(jLabel271, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 680, 370, 30));

        jLabel272.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel272.setText("新增或者修改即时生效，无需重启。");
        PVP技能.add(jLabel272, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 460, 370, 30));

        jTabbedPane1.addTab("PVP技能", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), PVP技能); // NOI18N

        更新.setBackground(new java.awt.Color(255, 255, 255));
        更新.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        标题.setFont(new java.awt.Font("幼圆", 0, 24)); // NOI18N
        更新.add(标题, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 0, 540, 40));

        更新记录.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        更新记录.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "日期", "版本", "简介", "null*"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(更新记录);
        if (更新记录.getColumnModel().getColumnCount() > 0) {
            更新记录.getColumnModel().getColumn(0).setResizable(false);
            更新记录.getColumnModel().getColumn(0).setPreferredWidth(200);
            更新记录.getColumnModel().getColumn(1).setResizable(false);
            更新记录.getColumnModel().getColumn(1).setPreferredWidth(200);
            更新记录.getColumnModel().getColumn(2).setResizable(false);
            更新记录.getColumnModel().getColumn(2).setPreferredWidth(800);
            更新记录.getColumnModel().getColumn(3).setResizable(false);
            更新记录.getColumnModel().getColumn(3).setPreferredWidth(10);
        }

        更新.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 550, 670));

        更新记录详细.setEditable(false);
        更新记录详细.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jScrollPane14.setViewportView(更新记录详细);

        更新.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 690, 635));

        jButton10.setText("刷新更新记录");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        更新.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 5, 130, 30));

        jTabbedPane1.addTab("更新记录", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 更新); // NOI18N

        雇佣角色ID.setBackground(new java.awt.Color(255, 255, 255));
        雇佣角色ID.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        查询.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        查询.setText("查询");
        查询.setToolTipText("");
        查询.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                查询ActionPerformed(evt);
            }
        });
        雇佣角色ID.add(查询, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 200, 130, 30));

        jLabel270.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel270.setText("该功能是用于玩家在市场摆摊后，道具不正常消失找回，输入角色ID后，即可查询丢失的道具。");
        雇佣角色ID.add(jLabel270, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 800, 30));

        雇佣角色ID2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                雇佣角色ID2ActionPerformed(evt);
            }
        });
        雇佣角色ID.add(雇佣角色ID2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, 100, 30));

        jTabbedPane1.addTab("雇佣商人", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 雇佣角色ID); // NOI18N

        指令.setBackground(new java.awt.Color(0, 0, 0));
        指令.setForeground(new java.awt.Color(255, 255, 255));
        指令.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton5.setFont(new java.awt.Font("幼圆", 1, 18)); // NOI18N
        jButton5.setText("执行");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        指令.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 480, 100, 30));
        指令.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 170, -1));

        天谴1级码.setText("jPasswordField1````");
        指令.add(天谴1级码, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 430, 290, 20));

        jLabel3.setFont(new java.awt.Font("宋体", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/图片/LOGO/警告.png"))); // NOI18N
        指令.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 160, -1, -1));

        保存数据.setBackground(new java.awt.Color(255, 255, 255));
        保存数据.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        保存数据.setText("保存数据");
        保存数据.setPreferredSize(new java.awt.Dimension(100, 30));
        保存数据.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                保存数据ActionPerformed(evt);
            }
        });
        指令.add(保存数据, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 40));

        保存雇佣.setBackground(new java.awt.Color(255, 255, 255));
        保存雇佣.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        保存雇佣.setText("保存雇佣");
        保存雇佣.setPreferredSize(new java.awt.Dimension(107, 30));
        保存雇佣.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                保存雇佣ActionPerformed(evt);
            }
        });
        指令.add(保存雇佣, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 100, 40));

        应用重载.setBackground(new java.awt.Color(255, 255, 255));
        应用重载.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        应用重载.setText("应用重载");
        应用重载.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                应用重载ActionPerformed(evt);
            }
        });
        指令.add(应用重载, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 100, 40));

        活动控制台.setBackground(new java.awt.Color(255, 255, 255));
        活动控制台.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        活动控制台.setText("活动后台");
        活动控制台.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                活动控制台ActionPerformed(evt);
            }
        });
        指令.add(活动控制台, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 100, 40));

        充值卡后台.setBackground(new java.awt.Color(255, 255, 255));
        充值卡后台.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        充值卡后台.setText("充值后台");
        充值卡后台.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                充值卡后台ActionPerformed(evt);
            }
        });
        指令.add(充值卡后台, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 100, 40));

        副本控制台.setBackground(new java.awt.Color(255, 255, 255));
        副本控制台.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        副本控制台.setText("3号控制台");
        副本控制台.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                副本控制台ActionPerformed(evt);
            }
        });
        指令.add(副本控制台, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 100, 40));

        控制台2号.setBackground(new java.awt.Color(255, 255, 255));
        控制台2号.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        控制台2号.setText("2号控制台");
        控制台2号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                控制台2号ActionPerformed(evt);
            }
        });
        指令.add(控制台2号, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 100, 40));

        控制台1号.setBackground(new java.awt.Color(255, 255, 255));
        控制台1号.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        控制台1号.setText("1号控制台");
        控制台1号.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                控制台1号ActionPerformed(evt);
            }
        });
        指令.add(控制台1号, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 100, 40));

        更多应用.setBackground(new java.awt.Color(255, 255, 255));
        更多应用.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        更多应用.setForeground(new java.awt.Color(51, 0, 255));
        更多应用.setText("更多应用");
        更多应用.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                更多应用ActionPerformed(evt);
            }
        });
        指令.add(更多应用, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 100, 40));

        关闭服务端.setBackground(new java.awt.Color(255, 255, 255));
        关闭服务端.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        关闭服务端.setForeground(new java.awt.Color(255, 0, 102));
        关闭服务端.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/图片/pp/关机.png"))); // NOI18N
        关闭服务端.setText("关闭服务端");
        关闭服务端.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                关闭服务端ActionPerformed(evt);
            }
        });
        指令.add(关闭服务端, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 510, 137, 40));

        jLabel11.setBackground(new java.awt.Color(255, 51, 51));
        jLabel11.setFont(new java.awt.Font("幼圆", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 51, 102));
        jLabel11.setText("非相关技术人员请勿操作");
        指令.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 390, -1, -1));

        jTabbedPane1.addTab("指令", new javax.swing.ImageIcon(getClass().getResource("/provider/WzXML/1.png")), 指令); // NOI18N

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1267, 720));

        脚本编辑器.setBackground(new java.awt.Color(255, 255, 255));
        脚本编辑器.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        脚本编辑器.setForeground(new java.awt.Color(255, 0, 0));
        脚本编辑器.setText("服务端网关");
        脚本编辑器.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                脚本编辑器ActionPerformed(evt);
            }
        });
        getContentPane().add(脚本编辑器, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 720, 120, 40));

        快捷面板.setBackground(new java.awt.Color(255, 255, 255));
        快捷面板.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        快捷面板.setText("数据面板");
        快捷面板.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                快捷面板ActionPerformed(evt);
            }
        });
        getContentPane().add(快捷面板, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 720, 100, 40));

        保存数据1.setBackground(new java.awt.Color(255, 255, 255));
        保存数据1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        保存数据1.setText("保存数据");
        保存数据1.setPreferredSize(new java.awt.Dimension(100, 30));
        保存数据1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                保存数据1ActionPerformed(evt);
            }
        });
        getContentPane().add(保存数据1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 720, 100, 40));

        保存雇佣1.setBackground(new java.awt.Color(255, 255, 255));
        保存雇佣1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        保存雇佣1.setText("保存雇佣");
        保存雇佣1.setPreferredSize(new java.awt.Dimension(107, 30));
        保存雇佣1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                保存雇佣1ActionPerformed(evt);
            }
        });
        getContentPane().add(保存雇佣1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 720, 100, 40));

        应用重载1.setBackground(new java.awt.Color(255, 255, 255));
        应用重载1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        应用重载1.setText("应用重载");
        应用重载1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                应用重载1ActionPerformed(evt);
            }
        });
        getContentPane().add(应用重载1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 720, 100, 40));

        活动控制台1.setBackground(new java.awt.Color(255, 255, 255));
        活动控制台1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        活动控制台1.setText("活动后台");
        活动控制台1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                活动控制台1ActionPerformed(evt);
            }
        });
        getContentPane().add(活动控制台1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 720, 100, 40));

        充值卡后台1.setBackground(new java.awt.Color(255, 255, 255));
        充值卡后台1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        充值卡后台1.setText("充值后台");
        充值卡后台1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                充值卡后台1ActionPerformed(evt);
            }
        });
        getContentPane().add(充值卡后台1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 720, 100, 40));

        副本控制台1.setBackground(new java.awt.Color(255, 255, 255));
        副本控制台1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        副本控制台1.setText("3号控制台");
        副本控制台1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                副本控制台1ActionPerformed(evt);
            }
        });
        getContentPane().add(副本控制台1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 720, 100, 40));

        控制台2号1.setBackground(new java.awt.Color(255, 255, 255));
        控制台2号1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        控制台2号1.setText("2号控制台");
        控制台2号1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                控制台2号1ActionPerformed(evt);
            }
        });
        getContentPane().add(控制台2号1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 720, 100, 40));

        控制台1号1.setBackground(new java.awt.Color(255, 255, 255));
        控制台1号1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        控制台1号1.setText("1号控制台");
        控制台1号1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                控制台1号1ActionPerformed(evt);
            }
        });
        getContentPane().add(控制台1号1, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 720, 110, 40));

        更多应用1.setBackground(new java.awt.Color(255, 255, 255));
        更多应用1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        更多应用1.setForeground(new java.awt.Color(51, 0, 255));
        更多应用1.setText("更多应用");
        更多应用1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                更多应用1ActionPerformed(evt);
            }
        });
        getContentPane().add(更多应用1, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 720, 100, 40));

        关闭服务端1.setBackground(new java.awt.Color(255, 255, 255));
        关闭服务端1.setFont(new java.awt.Font("幼圆", 0, 14)); // NOI18N
        关闭服务端1.setForeground(new java.awt.Color(255, 0, 102));
        关闭服务端1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/图片/pp/关机.png"))); // NOI18N
        关闭服务端1.setText("关闭服务端");
        关闭服务端1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                关闭服务端1ActionPerformed(evt);
            }
        });
        getContentPane().add(关闭服务端1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 720, 137, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void 刷新泡点金币开关() {
        String 泡点金币开关显示 = "";
        int 泡点金币开关 = gui.Start.ConfigValuesMap.get("泡点金币开关");
        if (泡点金币开关 <= 0) {
            泡点金币开关显示 = "泡点金币:开启";
        } else {
            泡点金币开关显示 = "泡点金币:关闭";
        }
        泡点金币开关(泡点金币开关显示);
    }

    private void 刷新泡点点券开关() {
        String 泡点点券开关显示 = "";
        int 泡点点券开关 = gui.Start.ConfigValuesMap.get("泡点点券开关");
        if (泡点点券开关 <= 0) {
            泡点点券开关显示 = "泡点点券:开启";
        } else {
            泡点点券开关显示 = "泡点点券:关闭";
        }
        泡点点券开关(泡点点券开关显示);
    }

    private void 刷新泡点经验开关() {
        String 泡点经验开关显示 = "";
        int 泡点经验开关 = gui.Start.ConfigValuesMap.get("泡点经验开关");
        if (泡点经验开关 <= 0) {
            泡点经验开关显示 = "泡点经验:开启";
        } else {
            泡点经验开关显示 = "泡点经验:关闭";
        }
        泡点经验开关(泡点经验开关显示);
    }

    private void 刷新泡点抵用开关() {
        String 泡点抵用开关显示 = "";
        int 泡点抵用开关 = gui.Start.ConfigValuesMap.get("泡点抵用开关");
        if (泡点抵用开关 <= 0) {
            泡点抵用开关显示 = "泡点抵用:开启";
        } else {
            泡点抵用开关显示 = "泡点抵用:关闭";
        }
        泡点抵用开关(泡点抵用开关显示);
    }

    private void 泡点金币开关(String str) {
        泡点金币开关.setText(str);
    }

    private void 泡点点券开关(String str) {
        泡点点券开关.setText(str);
    }

    private void 泡点经验开关(String str) {
        泡点经验开关.setText(str);
    }

    private void 泡点抵用开关(String str) {
        泡点抵用开关.setText(str);
    }

    private void 刷新账号信息() {
        for (int i = ((DefaultTableModel) (this.账号信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.账号信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM accounts order by id desc");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 封号 = "";
                if (rs.getInt("banned") == 0) {
                    封号 = "正常";
                } else {
                    封号 = "封禁";
                }
                String 在线 = "";
                if (rs.getInt("loggedin") == 0) {
                    // Font fnA = new Font("细明本",Font.PLAIN,12);
                    在线 = "不在线";
                } else {
                    在线 = "在线";
                }
                String QQ = "";
                if (rs.getString("qq") != null) {
                    QQ = rs.getString("qq");
                } else {
                    QQ = "未绑定QQ";
                }
                ((DefaultTableModel) 账号信息.getModel()).insertRow(账号信息.getRowCount(), new Object[]{
                    rs.getInt("id"), //账号ID
                    rs.getString("name"), //账号
                    rs.getString("SessionIP"), //账号IP地址
                    rs.getString("macs"), ///账号MAC地址
                    QQ,
                    rs.getInt("ACash"),//点券
                    rs.getInt("mPoints"),//抵用
                    rs.getString("lastlogin"),//最近上线
                    //rs.getInt("loggedin"),//在线
                    //rs.getInt("banned")//封号
                    在线,
                    封号,
                    rs.getInt("gm")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        读取显示账号();

    }

    private void 查找QQ() {

        for (int i = ((DefaultTableModel) (this.账号信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.账号信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM accounts WHERE qq =  '" + 账号操作.getText() + " ' ");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 封号 = "";
                if (rs.getInt("banned") == 0) {
                    封号 = "正常";
                } else {
                    封号 = "封禁";
                }
                String 在线 = "";
                if (rs.getInt("loggedin") == 0) {
                    // Font fnA = new Font("细明本",Font.PLAIN,12);
                    在线 = "不在线";
                } else {
                    在线 = "在线";
                }
                String QQ = "";
                if (rs.getString("qq") != null) {
                    QQ = rs.getString("qq");
                } else {
                    QQ = "未绑定QQ";
                }
                ((DefaultTableModel) 账号信息.getModel()).insertRow(账号信息.getRowCount(), new Object[]{
                    rs.getInt("id"), //账号ID
                    rs.getString("name"), //账号
                    rs.getString("SessionIP"), //账号IP地址
                    rs.getString("macs"), ///账号MAC地址
                    QQ,//注册时间
                    rs.getInt("ACash"),//点券
                    rs.getInt("mPoints"),//抵用
                    rs.getString("lastlogin"),//最近上线
                    在线,
                    封号,
                    rs.getInt("gm")
                });
            }
            账号提示语言.setText("[信息]:查找账号 " + this.账号操作.getText() + " 成功。");
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        账号信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 账号信息.getSelectedRow();
                String a = 账号信息.getValueAt(i, 0).toString();
                String a1 = 账号信息.getValueAt(i, 1).toString();
                String a2 = 账号信息.getValueAt(i, 5).toString();
                String a3 = 账号信息.getValueAt(i, 6).toString();
                账号ID.setText(a);
                账号操作.setText(a1);
                账号.setText(a1);
                点券.setText(a2);
                抵用.setText(a3);
                刷新角色信息2();
            }
        });
    }

    private void 查找账号() {

        for (int i = ((DefaultTableModel) (this.账号信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.账号信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM accounts WHERE name =  '" + 账号操作.getText() + "  '");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 封号 = "";
                if (rs.getInt("banned") == 0) {
                    封号 = "正常";
                } else {
                    封号 = "封禁";
                }
                String 在线 = "";
                if (rs.getInt("loggedin") == 0) {
                    // Font fnA = new Font("细明本",Font.PLAIN,12);
                    在线 = "不在线";
                } else {
                    在线 = "在线";
                }
                ((DefaultTableModel) 账号信息.getModel()).insertRow(账号信息.getRowCount(), new Object[]{
                    rs.getInt("id"), //账号ID
                    rs.getString("name"), //账号
                    rs.getString("SessionIP"), //账号IP地址
                    rs.getString("macs"), ///账号MAC地址
                    rs.getString("qq"),//注册时间
                    rs.getInt("ACash"),//点券
                    rs.getInt("mPoints"),//抵用
                    rs.getString("lastlogin"),//最近上线
                    在线,
                    封号,
                    rs.getInt("gm")
                });
            }
            账号提示语言.setText("[信息]:查找账号 " + this.账号操作.getText() + " 成功。");
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        账号信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 账号信息.getSelectedRow();
                String a = 账号信息.getValueAt(i, 0).toString();
                String a1 = 账号信息.getValueAt(i, 1).toString();
                String a2 = 账号信息.getValueAt(i, 5).toString();
                String a3 = 账号信息.getValueAt(i, 6).toString();
                账号ID.setText(a);
                账号操作.setText(a1);
                账号.setText(a1);
                点券.setText(a2);
                抵用.setText(a3);
                刷新角色信息2();
            }
        });
    }

    private void 刷新家族信息() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM characters");
            rs = ps.executeQuery();

            while (rs.next()) {
                ((DefaultTableModel) 角色信息.getModel()).insertRow(角色信息.getRowCount(), new Object[]{
                    rs.getString("name")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = ((DefaultTableModel) (this.家族信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.家族信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM guilds");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 家族信息.getModel()).insertRow(家族信息.getRowCount(), new Object[]{
                    rs.getInt("guildid"),
                    rs.getString("name"),
                    //ooors.getInt("leader"),
                    NPCConversationManager.角色ID取名字(rs.getInt("leader")),
                    rs.getString("rank1title"),
                    rs.getString("rank2title"),
                    rs.getString("rank3title"),
                    rs.getString("rank4title"),
                    rs.getString("rank5title"),
                    rs.getString("GP")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        家族信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 家族信息.getSelectedRow();
                String a1 = 家族信息.getValueAt(i, 0).toString();
                String a2 = 家族信息.getValueAt(i, 1).toString();
                String a3 = 家族信息.getValueAt(i, 2).toString();
                String a4 = 家族信息.getValueAt(i, 3).toString();
                String a5 = 家族信息.getValueAt(i, 4).toString();
                String a6 = 家族信息.getValueAt(i, 5).toString();
                String a7 = 家族信息.getValueAt(i, 6).toString();
                String a8 = 家族信息.getValueAt(i, 7).toString();
                String a9 = 家族信息.getValueAt(i, 8).toString();
                家族ID.setText(a1);
                家族名称.setText(a2);
                家族族长.setText(a3);
                家族成员1.setText(a4);
                家族成员2.setText(a5);
                家族成员3.setText(a6);
                家族成员4.setText(a7);
                家族成员5.setText(a8);
                家族GP.setText(a9);
            }
        });
    }

    private void 刷新技能信息() {
        boolean result1 = this.角色ID.getText().matches("[0-9]+");
        if (result1) {
            for (int i = ((DefaultTableModel) (this.技能信息.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.技能信息.getModel())).removeRow(i);
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = null;

                ResultSet rs = null;
                ps = con.prepareStatement("SELECT * FROM skills  WHERE characterid =" + this.角色ID.getText() + "");
                rs = ps.executeQuery();
                while (rs.next()) {
                    MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz"));
                    MapleData itemsData;
                    int itemId;
                    String itemName = "";
                    itemsData = data.getData("Skill.img");
                    for (MapleData itemFolder : itemsData.getChildren()) {
                        itemId = Integer.parseInt(itemFolder.getName());
                        if (rs.getInt("skillid") == itemId) {
                            itemName = MapleDataTool.getString("name", itemFolder, "NO-NAME");
                        }
                    }
                    ((DefaultTableModel) 技能信息.getModel()).insertRow(技能信息.getRowCount(), new Object[]{
                        rs.getInt("id"),
                        itemName,
                        rs.getInt("skillid"),
                        rs.getInt("skilllevel"),
                        rs.getInt("masterlevel")
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
            技能信息.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int i = 技能信息.getSelectedRow();
                    String a = 技能信息.getValueAt(i, 0).toString();
                    // String a1 = 技能信息.getValueAt(i, 1).toString();
                    String a2 = 技能信息.getValueAt(i, 2).toString();
                    String a3 = 技能信息.getValueAt(i, 3).toString();
                    String a4 = 技能信息.getValueAt(i, 4).toString();
                    技能序号.setText(a);
                    // 技能名字.setText(a1);
                    技能代码.setText(a2);
                    技能目前等级.setText(a3);
                    技能最高等级.setText(a4);
                    //出售状态.setText(a8);
                    //jTextField9.setText(a9);
                }
            });
        } else {
            角色提示语言.setText("[信息]:请先点击你想查看的角色。");
        }
    }

    private void 刷新角色信息() {
        String 输出 = "";
        for (int i = ((DefaultTableModel) (this.角色信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM characters order by id desc");
            rs = ps.executeQuery();

            while (rs.next()) {
                String 在线 = "";
                if (World.Find.findChannel(rs.getString("name")) > 0) {
                    在线 = "在线";
                } else {
                    在线 = "离线";
                }
                ((DefaultTableModel) 角色信息.getModel()).insertRow(角色信息.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    rs.getInt("accountid"),
                    rs.getString("name"),
                    getJobNameById(rs.getInt("job")),
                    rs.getInt("level"),
                    rs.getInt("str"),
                    rs.getInt("dex"),
                    rs.getInt("luk"),
                    rs.getInt("int"),
                    rs.getInt("maxhp"),
                    rs.getInt("maxmp"),
                    rs.getInt("meso"),
                    rs.getInt("map"),
                    rs.getInt("todayOnlineTime") + "/" + rs.getInt("totalOnlineTime"),
                    在线,
                    rs.getInt("gm"),
                    rs.getInt("hair"),
                    rs.getInt("face")
                });

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色信息.getSelectedRow();
                String a = 角色信息.getValueAt(i, 0).toString();
                String a1 = 角色信息.getValueAt(i, 2).toString();
                String a2 = 角色信息.getValueAt(i, 4).toString();
                String a3 = 角色信息.getValueAt(i, 5).toString();
                String a4 = 角色信息.getValueAt(i, 6).toString();
                String a5 = 角色信息.getValueAt(i, 7).toString();
                String a6 = 角色信息.getValueAt(i, 8).toString();
                String a7 = 角色信息.getValueAt(i, 9).toString();
                String a8 = 角色信息.getValueAt(i, 10).toString();
                String a9 = 角色信息.getValueAt(i, 11).toString();
                String a10 = 角色信息.getValueAt(i, 12).toString();
                String a11 = 角色信息.getValueAt(i, 15).toString();
                String a12 = 角色信息.getValueAt(i, 16).toString();
                String a13 = 角色信息.getValueAt(i, 17).toString();
                角色ID.setText(a);
                角色昵称.setText(a1);
                等级.setText(a2);
                力量.setText(a3);
                敏捷.setText(a4);
                智力.setText(a5);
                运气.setText(a6);
                HP.setText(a7);
                MP.setText(a8);
                金币.setText(a9);
                地图.setText(a10);
                GM.setText(a11);
                发型.setText(a12);
                脸型.setText(a13);
                个人发送物品玩家名字.setText(a1);
                发送装备玩家姓名.setText(a1);
            }
        });
    }

    private void 刷新角色背包穿戴() {
        for (int i = ((DefaultTableModel) (this.角色背包穿戴.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色背包穿戴.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE characterid =" + this.角色ID.getText() + " && inventorytype = -1");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 角色背包穿戴.getModel()).insertRow(角色背包穿戴.getRowCount(), new Object[]{
                    rs.getInt("inventoryitemid"),
                    rs.getInt("itemid"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("quantity")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色背包穿戴.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色背包穿戴.getSelectedRow();
                String a = 角色背包穿戴.getValueAt(i, 0).toString();
                String a1 = 角色背包穿戴.getValueAt(i, 1).toString();
                //String a2 = 角色背包穿戴.getValueAt(i, 2).toString();
                身上穿戴序号1.setText(a);
                背包物品代码1.setText(a1);
                //背包物品名字1.setText(a2);
            }
        });
    }

    private void 刷新角色装备背包() {
        for (int i = ((DefaultTableModel) (this.角色装备背包.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色装备背包.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE characterid =" + this.角色ID.getText() + " && inventorytype = 1");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 角色装备背包.getModel()).insertRow(角色装备背包.getRowCount(), new Object[]{
                    rs.getInt("inventoryitemid"),
                    rs.getInt("itemid"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("quantity")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色装备背包.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色装备背包.getSelectedRow();
                String a = 角色装备背包.getValueAt(i, 0).toString();
                String a1 = 角色装备背包.getValueAt(i, 1).toString();
                //String a2 = 角色装备背包.getValueAt(i, 2).toString();
                装备背包物品序号.setText(a);
                装备背包物品代码.setText(a1);
                //装备背包物品名字.setText(a2);
            }
        });
    }

    private void 刷新角色消耗背包() {
        for (int i = ((DefaultTableModel) (this.角色消耗背包.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色消耗背包.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE characterid =" + this.角色ID.getText() + " && inventorytype = 2");
            rs = ps.executeQuery();
            while (rs.next()) {

                ((DefaultTableModel) 角色消耗背包.getModel()).insertRow(角色消耗背包.getRowCount(), new Object[]{
                    rs.getInt("inventoryitemid"),
                    rs.getInt("itemid"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("quantity")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色消耗背包.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色消耗背包.getSelectedRow();
                String a = 角色消耗背包.getValueAt(i, 0).toString();
                String a1 = 角色消耗背包.getValueAt(i, 1).toString();
                //String a2 = 角色消耗背包.getValueAt(i, 2).toString();
                消耗背包物品序号.setText(a);
                消耗背包物品代码.setText(a1);
                //消耗背包物品名字.setText(a2);
            }
        });
    }

    private void 刷新角色特殊背包() {
        for (int i = ((DefaultTableModel) (this.角色特殊背包.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色特殊背包.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE characterid =" + this.角色ID.getText() + " && inventorytype = 5");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 角色特殊背包.getModel()).insertRow(角色特殊背包.getRowCount(), new Object[]{
                    rs.getInt("inventoryitemid"),
                    rs.getInt("itemid"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("quantity")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色特殊背包.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色特殊背包.getSelectedRow();
                String a = 角色特殊背包.getValueAt(i, 0).toString();
                String a1 = 角色特殊背包.getValueAt(i, 1).toString();
                //String a2 = 角色特殊背包.getValueAt(i, 2).toString();
                特殊背包物品序号.setText(a);
                特殊背包物品代码.setText(a1);
                //特殊背包物品名字.setText(a2);
            }
        });
    }

    private void 刷新角色游戏仓库() {
        for (int i = ((DefaultTableModel) (this.角色游戏仓库.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色游戏仓库.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE accountid =" + this.账号ID.getText());
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 角色游戏仓库.getModel()).insertRow(角色游戏仓库.getRowCount(), new Object[]{
                    rs.getInt("inventoryitemid"),
                    rs.getInt("itemid"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("quantity")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色游戏仓库.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色游戏仓库.getSelectedRow();
                String a = 角色游戏仓库.getValueAt(i, 0).toString();
                String a1 = 角色游戏仓库.getValueAt(i, 1).toString();
                //String a2 = 角色游戏仓库.getValueAt(i, 2).toString();
                游戏仓库物品序号.setText(a);
                游戏仓库物品代码.setText(a1);
                //游戏仓库物品名字.setText(a2);
            }
        });
    }

    private void 刷新角色商城仓库() {
        for (int i = ((DefaultTableModel) (this.角色商城仓库.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色商城仓库.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM csitems WHERE accountid =" + this.账号ID.getText());
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 角色商城仓库.getModel()).insertRow(角色商城仓库.getRowCount(), new Object[]{
                    rs.getInt("inventoryitemid"),
                    rs.getInt("itemid"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("quantity")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色商城仓库.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色商城仓库.getSelectedRow();
                String a = 角色商城仓库.getValueAt(i, 0).toString();
                String a1 = 角色商城仓库.getValueAt(i, 1).toString();
                //String a2 = 角色商城仓库.getValueAt(i, 2).toString();
                商城仓库物品序号.setText(a);
                商城仓库物品代码.setText(a1);
                //商城仓库物品名字.setText(a2);
            }
        });
    }

    private void 刷新角色点券拍卖行() {
        for (int i = ((DefaultTableModel) (this.角色点券拍卖行.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色点券拍卖行.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM auctionitems WHERE characterid =" + this.角色ID.getText());
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 角色点券拍卖行.getModel()).insertRow(角色点券拍卖行.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("characterName")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色点券拍卖行.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色点券拍卖行.getSelectedRow();
                String a = 角色点券拍卖行.getValueAt(i, 0).toString();
                角色点券拍卖行序号.setText(a);
            }
        });
    }

    private void 刷新角色金币拍卖行() {
        for (int i = ((DefaultTableModel) (this.角色金币拍卖行.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色金币拍卖行.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM auctionitems1 WHERE characterid =" + this.角色ID.getText());
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 角色金币拍卖行.getModel()).insertRow(角色金币拍卖行.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("characterName")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色金币拍卖行.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色金币拍卖行.getSelectedRow();
                String a = 角色金币拍卖行.getValueAt(i, 0).toString();
                角色金币拍卖行序号.setText(a);
            }
        });
    }

    private void 刷新角色其他背包() {
        for (int i = ((DefaultTableModel) (this.角色其他背包.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色其他背包.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE characterid =" + this.角色ID.getText() + " && inventorytype = 4");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 角色其他背包.getModel()).insertRow(角色其他背包.getRowCount(), new Object[]{
                    rs.getInt("inventoryitemid"),
                    rs.getInt("itemid"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("quantity")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色其他背包.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色其他背包.getSelectedRow();
                String a = 角色其他背包.getValueAt(i, 0).toString();
                String a1 = 角色其他背包.getValueAt(i, 1).toString();
                //String a2 = 角色其他背包.getValueAt(i, 2).toString();
                其他背包物品序号.setText(a);
                其他背包物品代码.setText(a1);
                //其他背包物品名字.setText(a2);
            }
        });
    }

    private void 刷新角色设置背包() {
        for (int i = ((DefaultTableModel) (this.角色设置背包.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色设置背包.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE characterid =" + this.角色ID.getText() + " && inventorytype = 3");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 角色设置背包.getModel()).insertRow(角色设置背包.getRowCount(), new Object[]{
                    rs.getInt("inventoryitemid"),
                    rs.getInt("itemid"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("quantity")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色设置背包.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色设置背包.getSelectedRow();
                String a = 角色设置背包.getValueAt(i, 0).toString();
                String a1 = 角色设置背包.getValueAt(i, 1).toString();
                String a2 = 角色设置背包.getValueAt(i, 2).toString();
                设置背包物品序号.setText(a);
                设置背包物品代码.setText(a1);
                设置背包物品名字.setText(a2);
            }
        });
    }

    private void 刷新角色信息2() {
        for (int i = ((DefaultTableModel) (this.角色信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM characters WHERE accountid =" + this.账号ID.getText() + "");
            rs = ps.executeQuery();

            while (rs.next()) {
                String 在线 = "";
                if (World.Find.findChannel(rs.getString("name")) > 0) {
                    在线 = "在线";
                } else {
                    在线 = "离线";
                }
                ((DefaultTableModel) 角色信息.getModel()).insertRow(角色信息.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    rs.getInt("accountid"),
                    rs.getString("name"),
                    getJobNameById(rs.getInt("job")),
                    rs.getInt("level"),
                    rs.getInt("str"),
                    rs.getInt("dex"),
                    rs.getInt("luk"),
                    rs.getInt("int"),
                    rs.getInt("maxhp"),
                    rs.getInt("maxmp"),
                    rs.getInt("meso"),
                    rs.getInt("map"),
                    rs.getInt("todayOnlineTime") + "/" + rs.getInt("totalOnlineTime"),
                    在线,
                    rs.getInt("gm"),
                    rs.getInt("hair"),
                    rs.getInt("face")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        角色信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 角色信息.getSelectedRow();
                String a = 角色信息.getValueAt(i, 0).toString();
                String a1 = 角色信息.getValueAt(i, 2).toString();
                String a2 = 角色信息.getValueAt(i, 4).toString();
                String a3 = 角色信息.getValueAt(i, 5).toString();
                String a4 = 角色信息.getValueAt(i, 6).toString();
                String a5 = 角色信息.getValueAt(i, 7).toString();
                String a6 = 角色信息.getValueAt(i, 8).toString();
                String a7 = 角色信息.getValueAt(i, 9).toString();
                String a8 = 角色信息.getValueAt(i, 10).toString();
                String a9 = 角色信息.getValueAt(i, 11).toString();
                String a10 = 角色信息.getValueAt(i, 12).toString();
                String a11 = 角色信息.getValueAt(i, 15).toString();
                String a12 = 角色信息.getValueAt(i, 16).toString();
                String a13 = 角色信息.getValueAt(i, 17).toString();
                角色ID.setText(a);
                角色昵称.setText(a1);
                等级.setText(a2);
                力量.setText(a3);
                敏捷.setText(a4);
                智力.setText(a5);
                运气.setText(a6);
                HP.setText(a7);
                MP.setText(a8);
                金币.setText(a9);
                地图.setText(a10);
                GM.setText(a11);
                发型.setText(a12);
                脸型.setText(a13);
                //出售状态.setText(a8);
                //jTextField9.setText(a9);
            }
        });
    }

    private void 刷装备2(int a) {
        try {
            int 物品ID;
            if ("物品ID".equals(全服发送装备物品ID.getText())) {
                物品ID = 0;
            } else {
                物品ID = Integer.parseInt(全服发送装备物品ID.getText());
            }
            int 力量;
            if ("力量".equals(全服发送装备装备力量.getText())) {
                力量 = 0;
            } else {
                力量 = Integer.parseInt(全服发送装备装备力量.getText());
            }
            int 敏捷;
            if ("敏捷".equals(全服发送装备装备敏捷.getText())) {
                敏捷 = 0;
            } else {
                敏捷 = Integer.parseInt(全服发送装备装备敏捷.getText());
            }
            int 智力;
            if ("智力".equals(全服发送装备装备智力.getText())) {
                智力 = 0;
            } else {
                智力 = Integer.parseInt(全服发送装备装备智力.getText());
            }
            int 运气;
            if ("运气".equals(全服发送装备装备运气.getText())) {
                运气 = 0;
            } else {
                运气 = Integer.parseInt(全服发送装备装备运气.getText());
            }
            int HP;
            if ("HP设置".equals(全服发送装备装备HP.getText())) {
                HP = 0;
            } else {
                HP = Integer.parseInt(全服发送装备装备HP.getText());
            }
            int MP;
            if ("MP设置".equals(全服发送装备装备MP.getText())) {
                MP = 0;
            } else {
                MP = Integer.parseInt(全服发送装备装备MP.getText());
            }
            int 可加卷次数;
            if ("加卷次数".equals(全服发送装备装备加卷.getText())) {
                可加卷次数 = 0;
            } else {
                可加卷次数 = Integer.parseInt(全服发送装备装备加卷.getText());
            }

            String 制作人名字;
            if ("制作人".equals(全服发送装备装备制作人.getText())) {
                制作人名字 = "";
            } else {
                制作人名字 = 全服发送装备装备制作人.getText();
            }
            int 给予时间;
            if ("给予物品时间".equals(全服发送装备装备给予时间.getText())) {
                给予时间 = 0;
            } else {
                给予时间 = Integer.parseInt(全服发送装备装备给予时间.getText());
            }
            String 是否可以交易 = 全服发送装备装备可否交易.getText();
            int 攻击力;
            if ("攻击力".equals(全服发送装备装备攻击力.getText())) {
                攻击力 = 0;
            } else {
                攻击力 = Integer.parseInt(全服发送装备装备攻击力.getText());
            }
            int 魔法力;
            if ("魔法力".equals(全服发送装备装备魔法力.getText())) {
                魔法力 = 0;
            } else {
                魔法力 = Integer.parseInt(全服发送装备装备魔法力.getText());
            }
            int 物理防御;
            if ("物理防御".equals(全服发送装备装备物理防御.getText())) {
                物理防御 = 0;
            } else {
                物理防御 = Integer.parseInt(全服发送装备装备物理防御.getText());
            }
            int 魔法防御;
            if ("魔法防御".equals(全服发送装备装备魔法防御.getText())) {
                魔法防御 = 0;
            } else {
                魔法防御 = Integer.parseInt(全服发送装备装备魔法防御.getText());
            }
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType type = GameConstants.getInventoryType(物品ID);
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (a == 1) {
                        if (1 >= 0) {
                            if (!MapleInventoryManipulator.checkSpace(mch.getClient(), 物品ID, 1, "")) {
                                return;
                            }
                            if (type.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(物品ID) && !GameConstants.isBullet(物品ID)
                                    || type.equals(MapleInventoryType.CASH) && 物品ID >= 5000000 && 物品ID <= 5000100) {
                                final Equip item = (Equip) (ii.getEquipById(物品ID));
                                if (ii.isCash(物品ID)) {
                                    item.setUniqueId(1);
                                }
                                if (力量 > 0 && 力量 <= 32767) {
                                    item.setStr((short) (力量));
                                }
                                if (敏捷 > 0 && 敏捷 <= 32767) {
                                    item.setDex((short) (敏捷));
                                }
                                if (智力 > 0 && 智力 <= 32767) {
                                    item.setInt((short) (智力));
                                }
                                if (运气 > 0 && 运气 <= 32767) {
                                    item.setLuk((short) (运气));
                                }
                                if (攻击力 > 0 && 攻击力 <= 32767) {
                                    item.setWatk((short) (攻击力));
                                }
                                if (魔法力 > 0 && 魔法力 <= 32767) {
                                    item.setMatk((short) (魔法力));
                                }
                                if (物理防御 > 0 && 物理防御 <= 32767) {
                                    item.setWdef((short) (物理防御));
                                }
                                if (魔法防御 > 0 && 魔法防御 <= 32767) {
                                    item.setMdef((short) (魔法防御));
                                }
                                if (HP > 0 && HP <= 30000) {
                                    item.setHp((short) (HP));
                                }
                                if (MP > 0 && MP <= 30000) {
                                    item.setMp((short) (MP));
                                }
                                if ("可以交易".equals(是否可以交易)) {
                                    byte flag = item.getFlag();
                                    if (item.getType() == MapleInventoryType.EQUIP.getType()) {
                                        flag |= ItemFlag.KARMA_EQ.getValue();
                                    } else {
                                        flag |= ItemFlag.KARMA_USE.getValue();
                                    }
                                    item.setFlag(flag);
                                }
                                if (给予时间 > 0) {
                                    item.setExpiration(System.currentTimeMillis() + (给予时间 * 24 * 60 * 60 * 1000));
                                }
                                if (可加卷次数 > 0) {
                                    item.setUpgradeSlots((byte) (可加卷次数));
                                }
                                if (制作人名字 != null) {
                                    item.setOwner(制作人名字);
                                }
                                final String name = ii.getName(物品ID);
                                if (物品ID / 10000 == 114 && name != null && name.length() > 0) { //medal
                                    final String msg = "你已获得称号 <" + name + ">";
                                    mch.getClient().getPlayer().dropMessage(5, msg);
                                }
                                MapleInventoryManipulator.addbyItem(mch.getClient(), item.copy());
                            } else {
                                MapleInventoryManipulator.addById(mch.getClient(), 物品ID, (short) 1, "", null, 给予时间, (byte) 0);
                            }
                        } else {
                            MapleInventoryManipulator.removeById(mch.getClient(), GameConstants.getInventoryType(物品ID), 物品ID, -1, true, false);
                        }
                        mch.getClient().sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 1, true));
                    } else if (mch.getName().equals(发送装备玩家姓名.getText())) {
                        if (1 >= 0) {
                            if (!MapleInventoryManipulator.checkSpace(mch.getClient(), 物品ID, 1, "")) {
                                return;
                            }
                            if (type.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(物品ID) && !GameConstants.isBullet(物品ID)
                                    || type.equals(MapleInventoryType.CASH) && 物品ID >= 5000000 && 物品ID <= 5000100) {
                                final Equip item = (Equip) (ii.getEquipById(物品ID));
                                if (ii.isCash(物品ID)) {
                                    item.setUniqueId(1);
                                }
                                if (力量 > 0 && 力量 <= 32767) {
                                    item.setStr((short) (力量));
                                }
                                if (敏捷 > 0 && 敏捷 <= 32767) {
                                    item.setDex((short) (敏捷));
                                }
                                if (智力 > 0 && 智力 <= 32767) {
                                    item.setInt((short) (智力));
                                }
                                if (运气 > 0 && 运气 <= 32767) {
                                    item.setLuk((short) (运气));
                                }
                                if (攻击力 > 0 && 攻击力 <= 32767) {
                                    item.setWatk((short) (攻击力));
                                }
                                if (魔法力 > 0 && 魔法力 <= 32767) {
                                    item.setMatk((short) (魔法力));
                                }
                                if (物理防御 > 0 && 物理防御 <= 32767) {
                                    item.setWdef((short) (物理防御));
                                }
                                if (魔法防御 > 0 && 魔法防御 <= 32767) {
                                    item.setMdef((short) (魔法防御));
                                }
                                if (HP > 0 && HP <= 30000) {
                                    item.setHp((short) (HP));
                                }
                                if (MP > 0 && MP <= 30000) {
                                    item.setMp((short) (MP));
                                }
                                if ("可以交易".equals(是否可以交易)) {
                                    byte flag = item.getFlag();
                                    if (item.getType() == MapleInventoryType.EQUIP.getType()) {
                                        flag |= ItemFlag.KARMA_EQ.getValue();
                                    } else {
                                        flag |= ItemFlag.KARMA_USE.getValue();
                                    }
                                    item.setFlag(flag);
                                }
                                if (给予时间 > 0) {
                                    item.setExpiration(System.currentTimeMillis() + (给予时间 * 24 * 60 * 60 * 1000));
                                }
                                if (可加卷次数 > 0) {
                                    item.setUpgradeSlots((byte) (可加卷次数));
                                }
                                if (制作人名字 != null) {
                                    item.setOwner(制作人名字);
                                }
                                final String name = ii.getName(物品ID);
                                if (物品ID / 10000 == 114 && name != null && name.length() > 0) { //medal
                                    final String msg = "你已获得称号 <" + name + ">";
                                    mch.getClient().getPlayer().dropMessage(5, msg);
                                }
                                MapleInventoryManipulator.addbyItem(mch.getClient(), item.copy());
                            } else {
                                MapleInventoryManipulator.addById(mch.getClient(), 物品ID, (short) 1, "", null, 给予时间, (byte) 0);
                            }
                        } else {
                            MapleInventoryManipulator.removeById(mch.getClient(), GameConstants.getInventoryType(物品ID), 物品ID, -1, true, false);
                        }
                        mch.getClient().sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 1, true));
                    }
                }
            }
            福利提示语言.setText("[信息]:发送成功。");
        } catch (Exception e) {
            福利提示语言.setText("[信息]:错误!" + e);
        }
    }

    private void ChangePassWord() {
        /* boolean result1 = this.jTextField24.getText().matches("[0-9]+");
        boolean result2 = this.jTextField25.getText().matches("[0-9]+");
        if (result1 && result2) {*/
        String account = 注册的账号.getText();
        String password = 注册的密码.getText();

        if (password.length() > 12) {
            账号提示语言.setText("[信息]:修改密码失败，密码过长。");
            return;
        }
        if (!AutoRegister.getAccountExists(account)) {
            账号提示语言.setText("[信息]:修改密码失败，账号不存在。");
            return;
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("Update accounts set password = ? Where name = ?");
            ps.setString(1, LoginCrypto.hexSha1(password));
            ps.setString(2, account);
            ps.execute();
            ps.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + ex);
        }
        账号提示语言.setText("[信息]:修改密码成功。账号: " + account + " 密码: " + password + "");
        //JOptionPane.showMessageDialog(null, "账号: " + account + "\r\n密码: " + password);
        /* } else 
            JOptionPane.showMessageDialog(null, "请填写数据");
            return;
        }*/
    }

    public void 注册新账号() {
        boolean result1 = this.注册的账号.getText().matches("[0-9]+");
        boolean result2 = this.注册的密码.getText().matches("[0-9]+");
        if (注册的账号.getText().equals("") || 注册的密码.getText().equals("")) {
            账号提示语言.setText("[信息]:请填写注册的账号密码");
            return;
        } else {
            Connection con;
            String account = 注册的账号.getText();
            String password = 注册的密码.getText();

            if (password.length() > 10) {
                账号提示语言.setText("[信息]:注册失败，密码过长");
                return;
            }
            if (AutoRegister.getAccountExists(account)) {
                账号提示语言.setText("[信息]:注册失败，账号已存在");
                return;
            }
            try {
                con = DatabaseConnection.getConnection();
            } catch (Exception ex) {
                System.out.println(ex);
                return;
            }
            try {
                PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password,qq) VALUES (?,?,?)");
                ps.setString(1, account);
                ps.setString(2, LoginCrypto.hexSha1(password));
                ps.setString(3, account);
                ps.executeUpdate();
                刷新账号信息();
                账号提示语言.setText("[信息]:注册成功。账号: " + account + " 密码: " + password + "");
            } catch (SQLException ex) {
                System.out.println(ex);
                return;
            }
        }
    }


    private void 保存数据ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_保存数据ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "需要保存数据吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            int p = 0;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    p++;
                    chr.saveToDB(true, true);
                }
            }
            String 输出 = "[保存数据系统] 保存" + p + "个成功。";
            JOptionPane.showMessageDialog(null, 输出);
        }

    }//GEN-LAST:event_保存数据ActionPerformed

    private void 保存雇佣ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_保存雇佣ActionPerformed
        // TODO add your handling code here:
        int p = 0;
        for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
            p++;
            cserv.closeAllMerchant();
        }
        JOptionPane.showMessageDialog(null, "雇佣商人保存" + p + "个频道成功。");
    }//GEN-LAST:event_保存雇佣ActionPerformed

    private void 应用重载ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_应用重载ActionPerformed

        int n = JOptionPane.showConfirmDialog(this, "你需要应用重载吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            //重载商店
            MapleShopFactory.getInstance().clear();
            //重载反应堆
            ReactorScriptManager.getInstance().clearDrops();
            //重载爆率
            MapleMonsterInformationProvider.getInstance().clearDrops();
            //重载传送
            PortalScriptManager.getInstance().clearScripts();
            //重载机器人
            new Thread(QQMsgServer.getInstance()).start();
            //重载云防黑名单
            GetCloudBacklist();
            //重载任务同步信息
            try {
                下载文件("http://123.207.53.97:8082/任务修复/任务修复.zip", "任务修复.zip", "" + 任务更新下载目录() + "/");
                解压文件.解压文件(任务更新解压目录("任务修复"), 任务更新导入目录("zevms"));
                删除文件(任务更新解压目录("任务修复"));
            } catch (Exception e) {

            }
            JOptionPane.showMessageDialog(null, "重载完成。");
        }

    }//GEN-LAST:event_应用重载ActionPerformed
    public static void 脚本编辑器2() {
        if (脚本编辑器2 != null) {
            脚本编辑器2.dispose();
        }
        脚本编辑器2 = new 脚本编辑器2();
        脚本编辑器2.setVisible(true);
    }

    public static void 副本控制台() {
        if (jButton1111 != null) {
            jButton1111.dispose();
        }
        jButton1111 = new 控制台3号();
        jButton1111.setVisible(true);
    }

    public static void 活动控制台() {
        if (jButton11112 != null) {
            jButton11112.dispose();
        }
        jButton11112 = new 活动控制台();
        jButton11112.setVisible(true);
    }

    public static void 经济控制台() {
        if (jButton11113 != null) {
            jButton11113.dispose();
        }
        jButton11113 = new 控制台2号();
        jButton11113.setVisible(true);
    }

    public static void 网关2() {
        if (网关 != null) {
            网关.dispose();
        }
        网关 = new 机器人群设置面板();
        网关.setVisible(true);
    }

    public static void 充值卡后台() {
        if (jButton11115 != null) {
            jButton11115.dispose();
        }
        jButton11115 = new 充值卡后台();
        jButton11115.setVisible(true);
    }


    private void 活动控制台ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_活动控制台ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开活动控制台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            活动控制台();
        }
    }//GEN-LAST:event_活动控制台ActionPerformed

    private void 副本控制台ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_副本控制台ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开 3 号制台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            副本控制台();
        }
    }//GEN-LAST:event_副本控制台ActionPerformed

    private void 脚本编辑器ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_脚本编辑器ActionPerformed
        JOptionPane.showMessageDialog(null, "禁止使用中。");
        /*int n = JOptionPane.showConfirmDialog(this, "你需要打开 网关 吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            网关2();
        }*/
    }//GEN-LAST:event_脚本编辑器ActionPerformed

    private void 更多应用ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_更多应用ActionPerformed
        更多应用();
    }//GEN-LAST:event_更多应用ActionPerformed

    private void 控制台2号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_控制台2号ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开 2 号制台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            经济控制台();
        }
    }//GEN-LAST:event_控制台2号ActionPerformed

    private void 充值卡后台ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_充值卡后台ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开充值卡后台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            充值卡后台();
        }
    }//GEN-LAST:event_充值卡后台ActionPerformed
    public void 刷新泡点设置() {
        for (int i = ((DefaultTableModel) (this.在线泡点设置.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.在线泡点设置.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id = 700 || id = 702 || id = 704 || id = 706|| id = 708");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 在线泡点设置.getModel()).insertRow(在线泡点设置.getRowCount(), new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("Val")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        在线泡点设置.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 在线泡点设置.getSelectedRow();
                String a = 在线泡点设置.getValueAt(i, 0).toString();
                String a1 = 在线泡点设置.getValueAt(i, 1).toString();
                String a2 = 在线泡点设置.getValueAt(i, 2).toString();
                泡点序号.setText(a);
                泡点类型.setText(a1);
                泡点值.setText(a2);
            }
        });
    }

    public void 刷新雇佣泡点设置() {
        for (int i = ((DefaultTableModel) (this.雇佣在线泡点设置.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.雇佣在线泡点设置.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id >= 720 && id <= 730");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 雇佣在线泡点设置.getModel()).insertRow(雇佣在线泡点设置.getRowCount(), new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("Val")});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        雇佣在线泡点设置.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 雇佣在线泡点设置.getSelectedRow();
                String a = 雇佣在线泡点设置.getValueAt(i, 0).toString();
                String a1 = 雇佣在线泡点设置.getValueAt(i, 1).toString();
                String a2 = 雇佣在线泡点设置.getValueAt(i, 2).toString();
                泡点序号1.setText(a);
                泡点类型1.setText(a1);
                泡点值1.setText(a2);
            }
        });
    }


    private void 控制台1号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_控制台1号ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开 1 号控制台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            动态配置台();
        }

    }//GEN-LAST:event_控制台1号ActionPerformed

    private void 关闭服务端ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_关闭服务端ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要关闭服务端吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            关闭服务端();
        }

    }//GEN-LAST:event_关闭服务端ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (天谴1级码.getText().equals("71447500FZ")) {
            天谴1级码.setText("");
        } else if (天谴1级码.getText().equals("zhangZEVMS12345!@#$%")) {
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, 2220);
                ps.setString(2, "存档信息数据");
                ps.setInt(3, 30);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "开启秘钥通行，解除限制，请重启服务端。");
            } catch (SQLException ex) {
            }
            天谴1级码.setText("");
        } else if (天谴1级码.getText().equals("tiaoshi3")) {
            if (MapleParty.调试3 == 0) {
                MapleParty.调试3++;
                JOptionPane.showMessageDialog(null, "开启调试+++模式。");
            } else {
                MapleParty.调试3 = 0;
                JOptionPane.showMessageDialog(null, "关闭调试+++模式。");
            }
            天谴1级码.setText("");
        } else if (天谴1级码.getText().equals("jinengtiaoshi")) {
            if (MapleParty.技能调试 == 0) {
                MapleParty.技能调试++;
                JOptionPane.showMessageDialog(null, "开启技能调试+++模式。");
            } else {
                MapleParty.技能调试 = 0;
                JOptionPane.showMessageDialog(null, "关闭技能调试+++模式。");
            }
            天谴1级码.setText("");
        } else if (天谴1级码.getText().equals("ZEVMSmxd")) {
            if (MapleParty.ZEVMSmxd == 0) {
                MapleParty.ZEVMSmxd++;
                JOptionPane.showMessageDialog(null, "开启管理员模式。");
            } else {
                MapleParty.ZEVMSmxd = 0;
                JOptionPane.showMessageDialog(null, "关闭管理员模式。");
            }
            天谴1级码.setText("");
        } else if (天谴1级码.getText().equals("renwu")) {
            if (MapleParty.任务修复 == 0) {
                MapleParty.任务修复++;
                JOptionPane.showMessageDialog(null, "开启管理员任务修复模式。");
            } else {
                MapleParty.任务修复 = 0;
                JOptionPane.showMessageDialog(null, "关闭管理员任务修复模式。");
            }
            天谴1级码.setText("");
        } else {
            if (MapleParty.试密码 >= 10) {
                System.exit(0);
            } else if (MapleParty.试密码 >= 5) {
                JOptionPane.showMessageDialog(null, "你再继续这样试下去，立马给你格式化所有硬盘。- " + (10 - MapleParty.试密码) + "");
                MapleParty.试密码++;
            } else {
                JOptionPane.showMessageDialog(null, "未有相关执行任务。- " + (10 - MapleParty.试密码) + "");
                MapleParty.试密码++;
            }
            天谴1级码.setText("");
        }


    }//GEN-LAST:event_jButton5ActionPerformed

    private void 删除技能ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除技能ActionPerformed

        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.技能序号.getText().matches("[0-9]+");

        if (result1) {
            if (Integer.parseInt(this.技能序号.getText()) < 0) {
                JOptionPane.showMessageDialog(null, "请填写正确的值");
            }
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM skills WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.技能序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from skills where id =" + Integer.parseInt(this.技能序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新技能信息();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的技能");
        }
    }//GEN-LAST:event_删除技能ActionPerformed

    private void 修改技能ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改技能ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.技能序号.getText().matches("[0-9]+");

        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE skills SET skilllevel = ?,masterlevel = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM skills WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.技能序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    String sqlString2 = null;

                    sqlString1 = "update skills set skilllevel='" + this.技能目前等级.getText() + "' where id=" + this.技能序号.getText() + ";";
                    PreparedStatement skilllevel = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    skilllevel.executeUpdate(sqlString1);

                    sqlString2 = "update skills set masterlevel='" + this.技能最高等级.getText() + "' where id=" + this.技能序号.getText() + ";";
                    PreparedStatement masterlevel = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    masterlevel.executeUpdate(sqlString2);

                    刷新技能信息();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要修改的技能");
        }// TODO add your handling code here:
    }//GEN-LAST:event_修改技能ActionPerformed

    private void 技能名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_技能名字ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_技能名字ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        公告发送(4);
    }//GEN-LAST:event_jButton34ActionPerformed

    private void 公告发布喇叭代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_公告发布喇叭代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_公告发布喇叭代码ActionPerformed

    private void 公告填写ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_公告填写ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_公告填写ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        公告发送(1);
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        公告发送(3);
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        公告发送(2);
    }//GEN-LAST:event_jButton31ActionPerformed

    private void 泡点抵用开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_泡点抵用开关ActionPerformed
        int 泡点抵用开关 = gui.Start.ConfigValuesMap.get("泡点抵用开关");
        if (泡点抵用开关 <= 0) {
            按键开关("泡点抵用开关", 707);
            刷新泡点抵用开关();
        } else {
            按键开关("泡点抵用开关", 707);
            刷新泡点抵用开关();
        }

    }//GEN-LAST:event_泡点抵用开关ActionPerformed

    private void 泡点点券开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_泡点点券开关ActionPerformed
        int 泡点点券开关 = gui.Start.ConfigValuesMap.get("泡点点券开关");
        if (泡点点券开关 <= 0) {
            按键开关("泡点点券开关", 703);
            刷新泡点点券开关();
        } else {
            按键开关("泡点点券开关", 703);
            刷新泡点点券开关();
        }
    }//GEN-LAST:event_泡点点券开关ActionPerformed

    private void 泡点经验开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_泡点经验开关ActionPerformed

        int 泡点经验开关 = gui.Start.ConfigValuesMap.get("泡点经验开关");
        if (泡点经验开关 <= 0) {
            按键开关("泡点经验开关", 705);
            刷新泡点经验开关();
        } else {
            按键开关("泡点经验开关", 705);
            刷新泡点经验开关();
        }

    }//GEN-LAST:event_泡点经验开关ActionPerformed

    private void 泡点金币开关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_泡点金币开关ActionPerformed
        int 泡点金币开关 = gui.Start.ConfigValuesMap.get("泡点金币开关");
        if (泡点金币开关 <= 0) {
            按键开关("泡点金币开关", 701);
            刷新泡点金币开关();
        } else {
            按键开关("泡点金币开关", 701);
            刷新泡点金币开关();
        }
    }//GEN-LAST:event_泡点金币开关ActionPerformed
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
    private void 泡点值修改ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_泡点值修改ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.泡点值.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");

                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");

                ps1.setInt(1, Integer.parseInt(this.泡点序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + this.泡点值.getText() + "' where id= " + this.泡点序号.getText() + ";";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    刷新泡点设置();
                    gui.Start.GetConfigValues();
                    福利提示语言2.setText("[信息]:修改成功已经生效。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            福利提示语言2.setText("[信息]:请选择你要修改的值。");
        }
    }//GEN-LAST:event_泡点值修改ActionPerformed

    private void 发送装备玩家姓名ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_发送装备玩家姓名ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_发送装备玩家姓名ActionPerformed

    private void 给予物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_给予物品ActionPerformed
        刷物品();       // TODO add your handling code here:
    }//GEN-LAST:event_给予物品ActionPerformed

    private void 个人发送物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_个人发送物品代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_个人发送物品代码ActionPerformed

    private void 个人发送物品玩家名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_个人发送物品玩家名字ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_个人发送物品玩家名字ActionPerformed

    private void 个人发送物品数量ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_个人发送物品数量ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_个人发送物品数量ActionPerformed

    private void a1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_a1ActionPerformed

    private void z6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_z6ActionPerformed
        发送福利(6);
    }//GEN-LAST:event_z6ActionPerformed

    private void z5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_z5ActionPerformed
        发送福利(5);
    }//GEN-LAST:event_z5ActionPerformed

    private void z4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_z4ActionPerformed
        发送福利(4);
    }//GEN-LAST:event_z4ActionPerformed

    private void z1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_z1ActionPerformed
        发送福利(1);        // TODO add your handling code here:
    }//GEN-LAST:event_z1ActionPerformed

    private void z3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_z3ActionPerformed
        发送福利(3);
    }//GEN-LAST:event_z3ActionPerformed

    private void z2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_z2ActionPerformed
        发送福利(2);
    }//GEN-LAST:event_z2ActionPerformed

    private void 给予装备1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_给予装备1ActionPerformed
        刷装备2(2);        // TODO add your handling code here:
    }//GEN-LAST:event_给予装备1ActionPerformed

    private void 全服发送装备装备物理防御ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备物理防御ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备物理防御ActionPerformed

    private void 全服发送装备装备魔法防御ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备魔法防御ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备魔法防御ActionPerformed

    private void 全服发送装备装备魔法力ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备魔法力ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备魔法力ActionPerformed

    private void 全服发送装备物品IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备物品IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备物品IDActionPerformed

    private void 全服发送装备装备敏捷ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备敏捷ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备敏捷ActionPerformed

    private void 全服发送装备装备可否交易ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备可否交易ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备可否交易ActionPerformed

    private void 全服发送装备装备给予时间ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备给予时间ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备给予时间ActionPerformed

    private void 全服发送装备装备攻击力ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备攻击力ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备攻击力ActionPerformed

    private void 全服发送装备装备HPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备HPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备HPActionPerformed

    private void 全服发送装备装备运气ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备运气ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备运气ActionPerformed

    private void 全服发送装备装备智力ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备智力ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备智力ActionPerformed

    private void 全服发送装备装备MPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备MPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备MPActionPerformed

    private void 全服发送装备装备力量ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备力量ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备力量ActionPerformed

    private void 全服发送装备装备制作人ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备制作人ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备制作人ActionPerformed

    private void 全服发送装备装备加卷ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送装备装备加卷ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_全服发送装备装备加卷ActionPerformed

    private void 给予物品1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_给予物品1ActionPerformed
        刷物品2();    // TODO add your handling code here:
    }//GEN-LAST:event_给予物品1ActionPerformed

    private void 全服发送物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送物品代码ActionPerformed

    }//GEN-LAST:event_全服发送物品代码ActionPerformed

    private void 全服发送物品数量ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_全服发送物品数量ActionPerformed

    }//GEN-LAST:event_全服发送物品数量ActionPerformed

    private void 删除拍卖行ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除拍卖行ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_删除拍卖行ActionPerformed

    private void 拍卖行物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_拍卖行物品代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_拍卖行物品代码ActionPerformed

    private void 角色金币拍卖行序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_角色金币拍卖行序号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_角色金币拍卖行序号ActionPerformed

    private void 拍卖行物品名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_拍卖行物品名字ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_拍卖行物品名字ActionPerformed

    private void 删除商城仓库ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除商城仓库ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.商城仓库物品序号.getText().matches("[0-9]+");
        if (result == true) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM csitems WHERE inventoryitemid = ?");
                ps1.setInt(1, Integer.parseInt(this.商城仓库物品序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from csitems where inventoryitemid =" + Integer.parseInt(this.商城仓库物品序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新角色商城仓库();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的物品");
        }
    }//GEN-LAST:event_删除商城仓库ActionPerformed

    private void 商城仓库物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_商城仓库物品代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_商城仓库物品代码ActionPerformed

    private void 商城仓库物品序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_商城仓库物品序号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_商城仓库物品序号ActionPerformed

    private void 商城仓库物品名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_商城仓库物品名字ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_商城仓库物品名字ActionPerformed

    private void 删除游戏仓库ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除游戏仓库ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_删除游戏仓库ActionPerformed

    private void 游戏仓库物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏仓库物品代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_游戏仓库物品代码ActionPerformed

    private void 游戏仓库物品序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏仓库物品序号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_游戏仓库物品序号ActionPerformed

    private void 游戏仓库物品名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏仓库物品名字ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_游戏仓库物品名字ActionPerformed

    private void 删除特殊背包ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除特殊背包ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.特殊背包物品序号.getText().matches("[0-9]+");
        if (result == true) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM inventoryitems WHERE inventoryitemid = ?");
                ps1.setInt(1, Integer.parseInt(this.特殊背包物品序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from inventoryitems where inventoryitemid =" + Integer.parseInt(this.特殊背包物品序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新角色特殊背包();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的物品");
        }
    }//GEN-LAST:event_删除特殊背包ActionPerformed

    private void 特殊背包物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_特殊背包物品代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_特殊背包物品代码ActionPerformed

    private void 特殊背包物品序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_特殊背包物品序号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_特殊背包物品序号ActionPerformed

    private void 特殊背包物品名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_特殊背包物品名字ActionPerformed

    }//GEN-LAST:event_特殊背包物品名字ActionPerformed

    private void 删除其他背包ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除其他背包ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.其他背包物品序号.getText().matches("[0-9]+");
        if (result == true) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM inventoryitems WHERE inventoryitemid = ?");
                ps1.setInt(1, Integer.parseInt(this.其他背包物品序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from inventoryitems where inventoryitemid =" + Integer.parseInt(this.其他背包物品序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新角色其他背包();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的物品");
        }
    }//GEN-LAST:event_删除其他背包ActionPerformed

    private void 其他背包物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_其他背包物品代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_其他背包物品代码ActionPerformed

    private void 其他背包物品序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_其他背包物品序号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_其他背包物品序号ActionPerformed

    private void 其他背包物品名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_其他背包物品名字ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_其他背包物品名字ActionPerformed

    private void 删除设置背包ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除设置背包ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.设置背包物品序号.getText().matches("[0-9]+");
        if (result == true) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM inventoryitems WHERE inventoryitemid = ?");
                ps1.setInt(1, Integer.parseInt(this.设置背包物品序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from inventoryitems where inventoryitemid =" + Integer.parseInt(this.设置背包物品序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新角色设置背包();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的物品");
        }
    }//GEN-LAST:event_删除设置背包ActionPerformed

    private void 设置背包物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_设置背包物品代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_设置背包物品代码ActionPerformed

    private void 设置背包物品序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_设置背包物品序号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_设置背包物品序号ActionPerformed

    private void 设置背包物品名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_设置背包物品名字ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_设置背包物品名字ActionPerformed

    private void 删除消耗背包ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除消耗背包ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.消耗背包物品序号.getText().matches("[0-9]+");
        if (result == true) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM inventoryitems WHERE inventoryitemid = ?");
                ps1.setInt(1, Integer.parseInt(this.消耗背包物品序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from inventoryitems where inventoryitemid =" + Integer.parseInt(this.消耗背包物品序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新角色消耗背包();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的物品");
        }
    }//GEN-LAST:event_删除消耗背包ActionPerformed

    private void 消耗背包物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_消耗背包物品代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_消耗背包物品代码ActionPerformed

    private void 消耗背包物品序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_消耗背包物品序号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_消耗背包物品序号ActionPerformed

    private void 消耗背包物品名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_消耗背包物品名字ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_消耗背包物品名字ActionPerformed

    private void 删除装备背包ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除装备背包ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.装备背包物品序号.getText().matches("[0-9]+");
        if (result == true) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM inventoryitems WHERE inventoryitemid = ?");
                ps1.setInt(1, Integer.parseInt(this.装备背包物品序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from inventoryitems where inventoryitemid =" + Integer.parseInt(this.装备背包物品序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新角色装备背包();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的物品");
        }
    }//GEN-LAST:event_删除装备背包ActionPerformed

    private void 装备背包物品代码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_装备背包物品代码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_装备背包物品代码ActionPerformed

    private void 装备背包物品序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_装备背包物品序号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_装备背包物品序号ActionPerformed

    private void 装备背包物品名字ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_装备背包物品名字ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_装备背包物品名字ActionPerformed

    private void 删除穿戴装备ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除穿戴装备ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result = this.身上穿戴序号1.getText().matches("[0-9]+");
        if (result == true) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM inventoryitems WHERE inventoryitemid = ?");
                ps1.setInt(1, Integer.parseInt(this.身上穿戴序号1.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from inventoryitems where inventoryitemid =" + Integer.parseInt(this.身上穿戴序号1.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新角色背包穿戴();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "请选择你要删除的物品");
        }
    }//GEN-LAST:event_删除穿戴装备ActionPerformed

    private void 背包物品代码1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_背包物品代码1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_背包物品代码1ActionPerformed

    private void 身上穿戴序号1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_身上穿戴序号1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_身上穿戴序号1ActionPerformed

    private void 背包物品名字1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_背包物品名字1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_背包物品名字1ActionPerformed

    private void 发型ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_发型ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_发型ActionPerformed

    private void 脸型ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_脸型ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_脸型ActionPerformed

    private void 卡家族解救ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_卡家族解救ActionPerformed
        显示在线玩家.setText("在线玩家; " + 在线玩家() + "");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.角色ID.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET (guildid = ?,guildrank = ?,allianceRank = ?)WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.角色ID.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    String sqlString2 = null;
                    String sqlString3 = null;
                    sqlString1 = "update characters set guildid='0' where id=" + this.角色ID.getText() + ";";
                    PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    hair.executeUpdate(sqlString1);
                    sqlString2 = "update characters set guildrank='5' where id=" + this.角色ID.getText() + ";";
                    PreparedStatement face = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    face.executeUpdate(sqlString2);
                    sqlString3 = "update characters set allianceRank='5' where id=" + this.角色ID.getText() + ";";
                    PreparedStatement allianceRank = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    allianceRank.executeUpdate(sqlString3);
                    角色提示语言.setText("[信息]:解卡家族角色成功。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            角色提示语言.setText("[信息]:请选择卡家族的角色。");
        }
    }//GEN-LAST:event_卡家族解救ActionPerformed

    private void 查看背包ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查看背包ActionPerformed
        显示在线玩家.setText("在线玩家; " + 在线玩家() + "");
        boolean result1 = this.角色ID.getText().matches("[0-9]+");
        if (!result1) {
            角色提示语言.setText("[信息]:请选择角色。");
            return;
        }
        if (账号ID.getText().equals("")) {
            角色提示语言.setText("[信息]:请先选择账号，再选择账号下的角色，接下来才可以查看游戏仓库。");
            return;
        }
        角色提示语言.setText("[信息]:查询速度跟角色信息量有关，请耐心等候。");
        刷新角色背包穿戴();
        刷新角色装备背包();
        刷新角色消耗背包();
        刷新角色设置背包();
        刷新角色其他背包();
        刷新角色特殊背包();
        刷新角色游戏仓库();
        刷新角色商城仓库();
//        刷新角色金币拍卖行();
//        刷新角色点券拍卖行();
        角色提示语言.setText("[信息]:请转到角色道具信息面板查看。");
    }//GEN-LAST:event_查看背包ActionPerformed

    private void 查看技能ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查看技能ActionPerformed

        角色提示语言.setText("[信息]:查看玩家技能信息。");
        刷新技能信息();
    }//GEN-LAST:event_查看技能ActionPerformed

    private void 卡号自救2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_卡号自救2ActionPerformed
        显示在线玩家.setText("在线玩家; " + 在线玩家() + "");
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.角色ID.getText().matches("[0-9]+");
        if (result1) {
            int n = JOptionPane.showConfirmDialog(this, "你确定要解卡物品自救这个角色吗？", "信息", JOptionPane.YES_NO_OPTION);
            if (n != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM inventoryitems WHERE characterid = ?");
                ps1.setInt(1, Integer.parseInt(this.角色ID.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr2 = " delete from inventoryitems where characterid =" + Integer.parseInt(this.角色ID.getText()) + "";
                    ps1.executeUpdate(sqlstr2);
                    角色提示语言.setText("[信息]:角色已经进行38处理。");
                    刷新角色信息();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            角色提示语言.setText("[信息]:请选择要38处理的角色。");
        }
    }//GEN-LAST:event_卡号自救2ActionPerformed

    private void 卡号自救1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_卡号自救1ActionPerformed
        显示在线玩家.setText("在线玩家; " + 在线玩家() + "");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.角色ID.getText().matches("[0-9]+");
        if (result1) {
            int n = JOptionPane.showConfirmDialog(this, "你确定要解卡发型脸型自救这个角色吗？", "信息", JOptionPane.YES_NO_OPTION);
            if (n != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET (hair = ?,face = ?)WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.角色ID.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    String sqlString2 = null;
                    sqlString1 = "update characters set hair='30000' where id=" + this.角色ID.getText() + ";";
                    PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    hair.executeUpdate(sqlString1);
                    sqlString2 = "update characters set face='20000' where id=" + this.角色ID.getText() + ";";
                    PreparedStatement face = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    face.executeUpdate(sqlString2);
                    角色提示语言.setText("[信息]:解救成功，发型脸型初始化。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            角色提示语言.setText("[信息]:请选择卡发型脸型的角色。");
        }
    }//GEN-LAST:event_卡号自救1ActionPerformed

    private void 角色IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_角色IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_角色IDActionPerformed

    private void GMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GMActionPerformed

    private void 地图ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_地图ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_地图ActionPerformed

    private void 金币ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_金币ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_金币ActionPerformed

    private void MPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MPActionPerformed

    private void HPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HPActionPerformed

    private void 运气ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_运气ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_运气ActionPerformed

    private void 智力ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_智力ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_智力ActionPerformed

    private void 敏捷ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_敏捷ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_敏捷ActionPerformed

    private void 力量ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_力量ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_力量ActionPerformed

    private void 等级ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_等级ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_等级ActionPerformed

    private void 角色昵称ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_角色昵称ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_角色昵称ActionPerformed

    private void 删除角色ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除角色ActionPerformed
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.角色ID.getText().matches("[0-9]+");

        if (result1) {
            int n = JOptionPane.showConfirmDialog(this, "你确定要删除这个角色吗？", "信息", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                try {
                    ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
                    ps1.setInt(1, Integer.parseInt(this.角色ID.getText()));
                    rs = ps1.executeQuery();
                    if (rs.next()) {
                        String sqlstr = " delete from characters where id =" + Integer.parseInt(this.角色ID.getText()) + "";
                        ps1.executeUpdate(sqlstr);
                        String sqlstr2 = " delete from inventoryitems where characterid =" + Integer.parseInt(this.角色ID.getText()) + "";
                        ps1.executeUpdate(sqlstr2);
                        String sqlstr3 = " delete from auctionitems where characterid =" + Integer.parseInt(this.角色ID.getText()) + "";
                        ps1.executeUpdate(sqlstr3);
                        String sqlstr31 = " delete from auctionitems1 where characterid =" + Integer.parseInt(this.角色ID.getText()) + "";
                        ps1.executeUpdate(sqlstr31);
                        String sqlstr4 = " delete from csitems where accountid =" + Integer.parseInt(this.角色ID.getText()) + "";
                        ps1.executeUpdate(sqlstr4);
                        String sqlstr5 = " delete from bank_item where cid =" + Integer.parseInt(this.角色ID.getText()) + "";
                        ps1.executeUpdate(sqlstr5);
                        String sqlstr6 = " delete from bossrank where cid =" + Integer.parseInt(this.角色ID.getText()) + "";
                        ps1.executeUpdate(sqlstr6);
                        String sqlstr7 = " delete from skills where characterid =" + Integer.parseInt(this.角色ID.getText()) + "";
                        ps1.executeUpdate(sqlstr7);
                        角色提示语言.setText("[信息]:成功删除角色 " + Integer.parseInt(this.角色ID.getText()) + " ，以及所有相关信息。");
                        刷新角色信息();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            角色提示语言.setText("[信息]:请选择删除的角色。");
        }
    }//GEN-LAST:event_删除角色ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean A = this.等级.getText().matches("[0-9]+");
        boolean B = this.GM.getText().matches("[0-9]+");
        boolean C = this.地图.getText().matches("[0-9]+");
        boolean D = this.金币.getText().matches("[0-9]+");
        boolean E = this.MP.getText().matches("[0-9]+");
        boolean F = this.HP.getText().matches("[0-9]+");
        boolean G = this.运气.getText().matches("[0-9]+");
        boolean H = this.智力.getText().matches("[0-9]+");
        boolean Y = this.敏捷.getText().matches("[0-9]+");
        boolean J = this.力量.getText().matches("[0-9]+");
        if (角色昵称.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "角色昵称不能留空");
            return;
        }
        if (World.Find.findChannel(角色昵称.getText()) > 0) {
            JOptionPane.showMessageDialog(null, "请先将角色离线后再修改。");
            return;
        }
        int n = JOptionPane.showConfirmDialog(this, "你确定要修改这个角色吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET (name = ?,level = ?, str = ?, dex = ?, luk = ?,int = ?,  maxhp = ?, maxmp = ?, meso = ?, map = ?, gm = ?, hair = ?, face = ? )WHERE id = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
            ps1.setInt(1, Integer.parseInt(this.角色ID.getText()));
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                String sqlString2 = null;
                String sqlString3 = null;
                String sqlString4 = null;
                String sqlString5 = null;
                String sqlString6 = null;
                String sqlString7 = null;
                String sqlString8 = null;
                String sqlString9 = null;
                String sqlString10 = null;
                String sqlString11 = null;
                String sqlString12 = null;
                String sqlString13 = null;
                sqlString1 = "update characters set name='" + this.角色昵称.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement name = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                name.executeUpdate(sqlString1);
                sqlString2 = "update characters set level='" + this.等级.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement level = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                level.executeUpdate(sqlString2);

                sqlString3 = "update characters set str='" + this.力量.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement str = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                str.executeUpdate(sqlString3);

                sqlString4 = "update characters set dex='" + this.敏捷.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement dex = DatabaseConnection.getConnection().prepareStatement(sqlString4);
                dex.executeUpdate(sqlString4);

                sqlString5 = "update characters set luk='" + this.智力.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement luk = DatabaseConnection.getConnection().prepareStatement(sqlString5);
                luk.executeUpdate(sqlString5);

                sqlString6 = "update characters set `int`='" + this.运气.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement executeUpdate = DatabaseConnection.getConnection().prepareStatement(sqlString6);
                executeUpdate.executeUpdate(sqlString6);

                sqlString7 = "update characters set maxhp='" + this.HP.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement maxhp = DatabaseConnection.getConnection().prepareStatement(sqlString7);
                maxhp.executeUpdate(sqlString7);

                sqlString8 = "update characters set maxmp='" + this.MP.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement maxmp = DatabaseConnection.getConnection().prepareStatement(sqlString8);
                maxmp.executeUpdate(sqlString8);

                sqlString9 = "update characters set meso='" + this.金币.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement meso = DatabaseConnection.getConnection().prepareStatement(sqlString9);
                meso.executeUpdate(sqlString9);

                sqlString10 = "update characters set map='" + this.地图.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement map = DatabaseConnection.getConnection().prepareStatement(sqlString10);
                map.executeUpdate(sqlString10);

                sqlString11 = "update characters set gm='" + this.GM.getText() + "' where id=" + this.角色ID.getText() + ";";
                PreparedStatement gm = DatabaseConnection.getConnection().prepareStatement(sqlString11);
                gm.executeUpdate(sqlString11);

                sqlString12 = "update characters set hair='" + this.发型.getText() + "' where id=" + this.发型.getText() + ";";
                PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString12);
                hair.executeUpdate(sqlString12);

                sqlString13 = "update characters set face='" + this.脸型.getText() + "' where id=" + this.脸型.getText() + ";";
                PreparedStatement face = DatabaseConnection.getConnection().prepareStatement(sqlString13);
                face.executeUpdate(sqlString13);
                角色提示语言.setText("[信息]:角色信息修改成功。");
                刷新角色信息();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton38ActionPerformed

    private void 显示管理角色ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_显示管理角色ActionPerformed
        显示在线玩家.setText("在线玩家; " + 在线玩家() + "");
        for (int i = ((DefaultTableModel) (this.角色信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.角色信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM characters  WHERE gm >0 ");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 在线 = "";
                if (World.Find.findChannel(rs.getString("name")) > 0) {
                    在线 = "在线";
                } else {
                    在线 = "离线";
                }
                ((DefaultTableModel) 角色信息.getModel()).insertRow(角色信息.getRowCount(), new Object[]{
                    rs.getInt("id"),
                    rs.getInt("accountid"),
                    rs.getString("name"),
                    getJobNameById(rs.getInt("job")),
                    rs.getInt("level"),
                    rs.getInt("str"),
                    rs.getInt("dex"),
                    rs.getInt("int"),
                    rs.getInt("luk"),
                    rs.getInt("maxhp"),
                    rs.getInt("maxmp"),
                    rs.getInt("meso"),
                    rs.getInt("map"),
                    rs.getInt("todayOnlineTime") + "/" + rs.getInt("totalOnlineTime"),
                    在线,
                    rs.getInt("gm"),
                    rs.getInt("hair"),
                    rs.getInt("face"
                    )});
            }
            角色提示语言.setText("[信息]:显示游戏所有管理员角色信息。");
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_显示管理角色ActionPerformed
    public static int 在线玩家() {
        int p = 0;
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                if (chr != null) {
                    p++;
                }
            }
        }
        return p;
    }
    private void 刷新角色信息ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新角色信息ActionPerformed
        角色提示语言.setText("[信息]:显示游戏所有玩家角色信息。");
        刷新角色信息();
        显示在线玩家.setText("在线玩家; " + 在线玩家() + "");
    }//GEN-LAST:event_刷新角色信息ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        ChangePassWord();
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        注册新账号();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton35ActionPerformed

    private void 注册的密码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_注册的密码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_注册的密码ActionPerformed

    private void 注册的账号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_注册的账号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_注册的账号ActionPerformed

    private void 删除账号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除账号ActionPerformed
        显示在线账号.setText("账号在线; " + 在线账号() + "");
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            int n = JOptionPane.showConfirmDialog(this, "你确定要删除这个账号吗？", "信息", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM accounts ");
                //ps1.setInt(1, Integer.parseInt(this.账号信息.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " Delete from accounts where name ='" + this.账号操作.getText() + "'";
                    账号提示语言.setText("[信息]:删除账号 " + this.账号操作.getText() + " 成功。");
                    ps1.executeUpdate(sqlstr);
                    刷新账号信息();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_删除账号ActionPerformed

    private void 封锁账号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_封锁账号ActionPerformed
        显示在线账号.setText("账号在线; " + 在线账号() + "");
        if (账号操作.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "请输入需要封锁的账号 ");
            return;
        }
        String account = 账号操作.getText();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;

            ps = con.prepareStatement("Update accounts set banned = ? Where name = ?");
            ps.setInt(1, 1);
            ps.setString(2, account);
            ps.execute();
            ps.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + ex);
        }
        账号提示语言.setText("[信息]:封锁账号 " + this.账号操作.getText() + " 成功。");
        刷新账号信息();
    }//GEN-LAST:event_封锁账号ActionPerformed

    private void 解卡ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_解卡ActionPerformed
        显示在线账号.setText("账号在线; " + 在线账号() + "");
        if (账号操作.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "请输入需要解卡的账号 ");
            return;
        }
        String account = 账号操作.getText();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;

            ps = con.prepareStatement("Update accounts set loggedin = ? Where name = ?");
            ps.setInt(1, 0);
            ps.setString(2, account);
            ps.execute();
            ps.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + ex);
        }
        账号提示语言.setText("[信息]:解卡账号 " + this.账号操作.getText() + " 成功。");
        刷新账号信息();
    }//GEN-LAST:event_解卡ActionPerformed

    private void 已封账号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_已封账号ActionPerformed
        显示在线账号.setText("账号在线; " + 在线账号() + "");
        账号提示语言.setText("[信息]:显示游戏所有已被封禁的玩家账号信息。");
        for (int i = ((DefaultTableModel) (this.账号信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.账号信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM accounts WHERE banned > 0 ");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 封号 = "";
                if (rs.getInt("banned") == 0) {
                    封号 = "正常";
                } else {
                    封号 = "封禁";
                }
                String 在线 = "";
                if (rs.getInt("loggedin") == 0) {
                    在线 = "不在线";
                } else {
                    在线 = "在线";
                }
                String QQ = "";
                if (rs.getString("qq") != null) {
                    QQ = rs.getString("qq");
                } else {
                    QQ = "未绑定QQ";
                }
                ((DefaultTableModel) 账号信息.getModel()).insertRow(账号信息.getRowCount(), new Object[]{
                    rs.getInt("id"), //账号ID
                    rs.getString("name"), //账号
                    rs.getString("SessionIP"), //账号IP地址
                    rs.getString("macs"), ///账号MAC地址
                    QQ,//注册时间
                    rs.getInt("ACash"),//点券
                    rs.getInt("mPoints"),//抵用
                    rs.getString("lastlogin"),//最近上线
                    在线,
                    封号,
                    rs.getInt("gm")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        读取显示账号();
    }//GEN-LAST:event_已封账号ActionPerformed

    private void 解封ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_解封ActionPerformed
        显示在线账号.setText("账号在线; " + 在线账号() + "");
        if (账号操作.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "请输入需要解封的账号 ");
            return;
        }
        String account = 账号操作.getText();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;

            ps = con.prepareStatement("Update accounts set banned = ? Where name = ?");
            ps.setInt(1, 0);
            ps.setString(2, account);
            ps.execute();
            ps.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + ex);
        }
        账号提示语言.setText("[信息]:解封账号 " + account + " 成功。");
        //JOptionPane.showMessageDialog(null, "账号解封成功");
        刷新账号信息();
    }//GEN-LAST:event_解封ActionPerformed

    private void 离线账号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_离线账号ActionPerformed
        显示在线账号.setText("账号在线; " + 在线账号() + "");
        账号提示语言.setText("[信息]:显示游戏所有离线玩家账号信息。");
        for (int i = ((DefaultTableModel) (this.账号信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.账号信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM accounts  WHERE loggedin = 0 ");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 封号 = "";
                if (rs.getInt("banned") == 0) {
                    封号 = "正常";
                } else {
                    封号 = "封禁";
                }
                String 在线 = "";
                if (rs.getInt("loggedin") == 0) {
                    在线 = "不在线";
                } else {
                    在线 = "在线";
                }
                String QQ = "";
                if (rs.getString("qq") != null) {
                    QQ = rs.getString("qq");
                } else {
                    QQ = "未绑定QQ";
                }
                ((DefaultTableModel) 账号信息.getModel()).insertRow(账号信息.getRowCount(), new Object[]{
                    rs.getInt("id"), //账号ID
                    rs.getString("name"), //账号
                    rs.getString("SessionIP"), //账号IP地址
                    rs.getString("macs"), ///账号MAC地址
                    QQ,//注册时间
                    rs.getInt("ACash"),//点券
                    rs.getInt("mPoints"),//抵用
                    rs.getString("lastlogin"),//最近上线
                    在线,
                    封号,
                    rs.getInt("gm")
                });

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        读取显示账号();
    }//GEN-LAST:event_离线账号ActionPerformed

    private void 在线账号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_在线账号ActionPerformed
        显示在线账号.setText("账号在线; " + 在线账号() + "");
        账号提示语言.setText("[信息]:显示游戏所有在线玩家账号信息。");
        for (int i = ((DefaultTableModel) (this.账号信息.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.账号信息.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM accounts  WHERE loggedin > 0 ");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 封号 = "";
                if (rs.getInt("banned") == 0) {
                    封号 = "正常";
                } else {
                    封号 = "封禁";
                }
                String 在线 = "";
                if (rs.getInt("loggedin") == 0) {
                    在线 = "不在线";
                } else {
                    在线 = "在线";
                }
                String QQ = "";
                if (rs.getString("qq") != null) {
                    QQ = rs.getString("qq");
                } else {
                    QQ = "未绑定QQ";
                }
                ((DefaultTableModel) 账号信息.getModel()).insertRow(账号信息.getRowCount(), new Object[]{
                    rs.getInt("id"), //账号ID
                    rs.getString("name"), //账号
                    rs.getString("SessionIP"), //账号IP地址
                    rs.getString("macs"), ///账号MAC地址
                    QQ,//注册时间
                    rs.getInt("ACash"),//点券
                    rs.getInt("mPoints"),//抵用
                    rs.getString("lastlogin"),//最近上线
                    在线,
                    封号,
                    rs.getInt("gm")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        读取显示账号();
    }//GEN-LAST:event_在线账号ActionPerformed
    public void 读取显示账号() {
        账号信息.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 账号信息.getSelectedRow();
                String a = 账号信息.getValueAt(i, 0).toString();
                String a1 = 账号信息.getValueAt(i, 1).toString();
                String a2 = 账号信息.getValueAt(i, 5).toString();
                String a3 = 账号信息.getValueAt(i, 6).toString();
                //if (账号信息.getValueAt(i, 4).toString() != null) {
                String a4 = 账号信息.getValueAt(i, 4).toString();
                QQ.setText(a4);
                //}
                String a10 = 账号信息.getValueAt(i, 10).toString();
                账号ID.setText(a);
                账号操作.setText(a1);
                账号.setText(a1);

                点券.setText(a2);
                抵用.setText(a3);
                管理1.setText(a10);
                账号提示语言.setText("[信息]:显示账号 " + 账号.getText() + " 下角色信息。");
                刷新角色信息2();
            }
        });
    }

    public static int 在线账号() {
        int data = 0;
        int p = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT loggedin as DATA FROM accounts WHERE loggedin > 0");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data = rs.getInt("DATA");
                    p += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("在线账号、出错");
        }
        return p;
    }
    private void 刷新账号信息ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新账号信息ActionPerformed
        账号提示语言.setText("[信息]:显示游戏所有玩家账号信息。");
        刷新账号信息();
        显示在线账号.setText("账号在线; " + 在线账号() + "");
    }//GEN-LAST:event_刷新账号信息ActionPerformed

    private void 修改账号点券抵用ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改账号点券抵用ActionPerformed
        boolean result1 = this.点券.getText().matches("[0-9]+");
        boolean result2 = this.抵用.getText().matches("[0-9]+");
        boolean result3 = this.管理1.getText().matches("[0-9]+");
        boolean result4 = this.QQ.getText().matches("[0-9]+");
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (result1 && result2 && result3 && result4) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET ACash = ?, mPoints = ?, gm = ?, qq = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM accounts  WHERE id = ? ");
                ps1.setInt(1, Integer.parseInt(this.账号ID.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString2 = null;
                    String sqlString3 = null;
                    String sqlString4 = null;
                    String sqlString5 = null;
                    sqlString2 = "update accounts set ACash=" + Integer.parseInt(this.点券.getText()) + " where id ='" + Integer.parseInt(this.账号ID.getText()) + "';";
                    PreparedStatement priority = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    priority.executeUpdate(sqlString2);
                    sqlString3 = "update accounts set mPoints=" + Integer.parseInt(this.抵用.getText()) + " where id='" + Integer.parseInt(this.账号ID.getText()) + "';";
                    PreparedStatement period = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    period.executeUpdate(sqlString3);
                    sqlString4 = "update accounts set gm=" + Integer.parseInt(this.管理1.getText()) + " where id='" + Integer.parseInt(this.账号ID.getText()) + "';";
                    PreparedStatement gm = DatabaseConnection.getConnection().prepareStatement(sqlString4);
                    gm.executeUpdate(sqlString4);
                    sqlString5 = "update accounts set qq=" + Integer.parseInt(this.QQ.getText()) + " where id='" + Integer.parseInt(this.账号ID.getText()) + "';";
                    PreparedStatement qq = DatabaseConnection.getConnection().prepareStatement(sqlString5);
                    qq.executeUpdate(sqlString5);
                    刷新账号信息();
                    账号提示语言.setText("[信息]:修改账号 " + this.账号操作.getText() + " / 点券→" + Integer.parseInt(this.点券.getText()) + " / 抵用券→" + Integer.parseInt(this.抵用.getText()) + " 成功。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            账号提示语言.setText("[信息]:请选择要修改的账号,数据不能为空，或者数值填写不对。");
        }
    }//GEN-LAST:event_修改账号点券抵用ActionPerformed

    private void 修改背包扩充价格ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改背包扩充价格ActionPerformed
        boolean result1 = this.商城扩充价格修改.getText().matches("[0-9]+");
        if (result1) {
            if (Integer.parseInt(this.商城扩充价格修改.getText()) < 0) {
                商城提示语言.setText("[信息]:请输入正确的修改值。");
                return;
            }
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");
                ps1.setInt(1, 1);
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from configvalues where id =999";
                    ps1.executeUpdate(sqlstr);

                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO configvalues (id, name,Val) VALUES ( ?, ?, ?)")) {
                ps.setInt(1, 999);
                ps.setString(2, "商城扩充价格");
                ps.setInt(3, Integer.parseInt(this.商城扩充价格修改.getText()));
                ps.executeUpdate();
                刷新商城扩充价格();
                gui.Start.GetConfigValues();
                商城提示语言.setText("[信息]:商城扩充背包价格修改成功，已经生效。");

            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改背包扩充价格ActionPerformed

    private void 其他ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_其他ActionPerformed
        读取商品(60200000, 60300000, 5, 3);
    }//GEN-LAST:event_其他ActionPerformed

    private void 宠物服饰ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_宠物服饰ActionPerformed
        读取商品(60100000, 60200000, 5, 2);
    }//GEN-LAST:event_宠物服饰ActionPerformed

    private void 宠物ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_宠物ActionPerformed
        读取商品(60000000, 60100000, 5, 1);
    }//GEN-LAST:event_宠物ActionPerformed

    private void 效果ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_效果ActionPerformed
        读取商品(50500000, 50600000, 4, 4);
    }//GEN-LAST:event_效果ActionPerformed

    private void 游戏ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_游戏ActionPerformed
        读取商品(50400000, 50500000, 4, 5);
    }//GEN-LAST:event_游戏ActionPerformed

    private void 纪念日ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_纪念日ActionPerformed
        读取商品(50300000, 50400000, 4, 6);
    }//GEN-LAST:event_纪念日ActionPerformed

    private void 个人商店ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_个人商店ActionPerformed
        读取商品(50200000, 50300000, 4, 3);
    }//GEN-LAST:event_个人商店ActionPerformed

    private void 表情ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_表情ActionPerformed
        读取商品(50100000, 50200000, 4, 2);
    }//GEN-LAST:event_表情ActionPerformed

    private void 会员卡ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_会员卡ActionPerformed
        读取商品(50000000, 50100000, 4, 1);
    }//GEN-LAST:event_会员卡ActionPerformed

    private void 卷轴ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_卷轴ActionPerformed
        读取商品(30200000, 30300000, 3, 3);
    }//GEN-LAST:event_卷轴ActionPerformed

    private void 通讯物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_通讯物品ActionPerformed
        读取商品(30100000, 30200000, 3, 2);
    }//GEN-LAST:event_通讯物品ActionPerformed

    private void 喜庆物品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_喜庆物品ActionPerformed
        读取商品(30000000, 30100000, 3, 1);
    }//GEN-LAST:event_喜庆物品ActionPerformed

    private void 骑宠ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_骑宠ActionPerformed
        读取商品(21200000, 21300000, 2, 8);
    }//GEN-LAST:event_骑宠ActionPerformed

    private void 披风ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_披风ActionPerformed
        读取商品(21100000, 21200000, 2, 3);
    }//GEN-LAST:event_披风ActionPerformed

    private void 飞镖ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_飞镖ActionPerformed
        读取商品(21000000, 21100000, 2, 4);
    }//GEN-LAST:event_飞镖ActionPerformed

    private void 戒指ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_戒指ActionPerformed
        读取商品(20900000, 21000000, 2, 9);
    }//GEN-LAST:event_戒指ActionPerformed

    private void 武器ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_武器ActionPerformed
        读取商品(20800000, 20900000, 2, 12);
    }//GEN-LAST:event_武器ActionPerformed

    private void 手套ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_手套ActionPerformed
        读取商品(20700000, 20800000, 2, 11);
    }//GEN-LAST:event_手套ActionPerformed

    private void 鞋子ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_鞋子ActionPerformed
        读取商品(20600000, 20700000, 2, 7);
    }//GEN-LAST:event_鞋子ActionPerformed

    private void 裙裤ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_裙裤ActionPerformed
        读取商品(20500000, 20600000, 2, 2);
    }//GEN-LAST:event_裙裤ActionPerformed

    private void 上衣ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_上衣ActionPerformed
        读取商品(20400000, 20500000, 2, 13);
    }//GEN-LAST:event_上衣ActionPerformed

    private void 长袍ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_长袍ActionPerformed
        读取商品(20300000, 20400000, 2, 5);
    }//GEN-LAST:event_长袍ActionPerformed

    private void 眼饰ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_眼饰ActionPerformed
        读取商品(20200000, 20300000, 2, 10);
    }//GEN-LAST:event_眼饰ActionPerformed

    private void 脸饰ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_脸饰ActionPerformed
        读取商品(20100000, 20200000, 2, 6);
    }//GEN-LAST:event_脸饰ActionPerformed

    private void 帽子ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_帽子ActionPerformed
        读取商品(20000000, 20100000, 2, 1);
    }//GEN-LAST:event_帽子ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        商城提示语言.setText("[信息]:未启用。");
        //JOptionPane.showMessageDialog(this, "未启用");  // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void 活动ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_活动ActionPerformed
        读取商品(10200000, 10300000, 1, 3);

    }//GEN-LAST:event_活动ActionPerformed

    private void 读取热销产品ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_读取热销产品ActionPerformed
        读取商品(10000000, 10100000, 1, 1);
    }//GEN-LAST:event_读取热销产品ActionPerformed

    private void 主题馆ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_主题馆ActionPerformed
        读取商品(10100000, 10200000, 1, 2);
    }//GEN-LAST:event_主题馆ActionPerformed

    private void 显示类型ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_显示类型ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_显示类型ActionPerformed

    private void 商品出售状态ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_商品出售状态ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_商品出售状态ActionPerformed

    private void 商品编码ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_商品编码ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_商品编码ActionPerformed

    private void eventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventActionPerformed
        商城提示语言.setText("[信息]:显示物品出售状态为 event");
        商品出售状态.setText("3");        // TODO add your handling code here:
    }//GEN-LAST:event_eventActionPerformed

    private void HOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HOTActionPerformed
        商城提示语言.setText("[信息]:显示物品出售状态为 HOT");
        商品出售状态.setText("2");        // TODO add your handling code here:
    }//GEN-LAST:event_HOTActionPerformed

    private void SaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaleActionPerformed
        商城提示语言.setText("[信息]:显示物品出售状态为 Sale");
        商品出售状态.setText("1");        // TODO add your handling code here:
    }//GEN-LAST:event_SaleActionPerformed

    private void NEWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NEWActionPerformed
        商城提示语言.setText("[信息]:显示物品出售状态为 NEW");
        商品出售状态.setText("0");        // TODO add your handling code here:
    }//GEN-LAST:event_NEWActionPerformed

    private void 无出售状态ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_无出售状态ActionPerformed
        商城提示语言.setText("[信息]:显示物品出售状态为 无");
        商品出售状态.setText("-1");        // TODO add your handling code here:
    }//GEN-LAST:event_无出售状态ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "是否刷新？\r\n刷新所耗时间会根据物品数量，服务器配置决定。", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            initCharacterPannel();
        }
        商城提示语言.setText("[信息]:刷新商城物品列表。");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        int n = JOptionPane.showConfirmDialog(this, "确定为[ " + 商品编码.getText() + " 商品]    下架?", "上架商品提示消息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            下架();
            //刷新();
        }
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        int n = JOptionPane.showConfirmDialog(this, "确定为[ " + 商品编码.getText() + " 商品]    上架?", "上架商品提示消息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            上架();
            //刷新();
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void 添加ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_添加ActionPerformed
        boolean result1 = this.商品编码.getText().matches("[0-9]+");
        boolean result2 = this.商品数量.getText().matches("[0-9]+");
        boolean result3 = this.商品价格.getText().matches("[0-9]+");
        boolean result4 = this.商品时间.getText().matches("[0-9]+");
        boolean result5 = this.商品库存.getText().matches("[0-9]+");
        boolean result6 = this.每日限购.getText().matches("[0-9]+");
        boolean result7 = this.商品折扣.getText().matches("[0-9]+");
        boolean result8 = this.商品代码.getText().matches("[0-9]+");

        if (!result1 && !result2 && !result3 && !result4 && !result5 && !result6 && !result7 && !result8) {
            商城提示语言.setText("[信息]:请输入正确的数据。");
            return;
        }
        if (商品编码.getText().equals("")) {
            商城提示语言.setText("[信息]:请点击商品分类选择添加类型。");
            return;
        }
        if (商品代码.getText().equals("")) {
            商城提示语言.setText("[信息]:请输入添加的商品代码。");
            return;
        }
        if (商品价格.getText().equals("")) {
            商城提示语言.setText("[信息]:请输入商品价格。");
            return;
        }
        if (Integer.parseInt(this.商品价格.getText()) > 999999999) {
            商城提示语言.setText("[信息]:商品数量不能大于999999999。");
            return;
        }
        if (商品时间.getText().equals("")) {
            商城提示语言.setText("[信息]:请输入商品的给予时间，0 代表无限制。");
            return;
        }
        if (商品数量.getText().equals("")) {
            商城提示语言.setText("[信息]:请输入商品的商品数量。");
            return;
        }
        if (Integer.parseInt(this.商品数量.getText()) > 100) {
            商城提示语言.setText("[信息]:商品数量不能大于100。");
            return;
        }
        int 商品出售状态2;
        if (商品出售状态.getText().equals("")) {
            商品出售状态2 = -1;
        } else {
            商品出售状态2 = Integer.parseInt(this.商品出售状态.getText());
        }
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM cashshop_modified_items WHERE serial = ?");
            ps1.setInt(1, Integer.parseInt(this.商品编码.getText()));
            rs = ps1.executeQuery();
            if (!rs.next()) {
                try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                    ps.setInt(1, Integer.parseInt(this.商品编码.getText()));
                    ps.setInt(2, 1);
                    ps.setInt(3, Integer.parseInt(this.商品代码.getText()));
                    ps.setInt(4, 1);
                    ps.setInt(5, Integer.parseInt(this.商品时间.getText()));
                    ps.setInt(6, 2);
                    ps.setInt(7, Integer.parseInt(this.商品数量.getText()));
                    ps.setInt(8, 0);
                    ps.setInt(9, Integer.parseInt(this.商品价格.getText()));
                    ps.setInt(10, 商品出售状态2);
                    ps.setInt(11, 0);
                    ps.setInt(12, 0);
                    ps.setInt(13, 0);
                    ps.executeUpdate();

                } catch (SQLException ex) {
                    Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!商品库存.getText().equals("")) {
                    int SN库存 = Getcharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 2);
                    if (SN库存 == -1) {
                        Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 2, -2);
                    }
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 2, -SN库存);
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 2, Integer.parseInt(this.商品库存.getText()));
                }
                if (!商品折扣.getText().equals("")) {
                    int SN库存 = Getcharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 3);
                    if (SN库存 == -1) {
                        Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 3, -2);
                    }
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 3, -SN库存);
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 3, Integer.parseInt(this.商品折扣.getText()));
                }

                商城提示语言.setText("[信息]:新物品载入成功。");
                int n = JOptionPane.showConfirmDialog(this, "是否刷新？\r\n刷新所耗时间会根据物品数量，服务器配置决定。", "信息", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    刷新();
                }
            } else {
                商城提示语言.setText("[信息]:已存在的SN编码无法成功载入。");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*int 数字 = Integer.parseInt(this.商品代码.getText()) - 1;
        商品代码.setText("" + 数字 + "");
        /*int 数字 = Integer.parseInt(this.商品代码.getText()) - 1;
        商品代码.setText("" + 数字 + "");
        读取商品(60100000, 60200000, 5, 2);*/
    }//GEN-LAST:event_添加ActionPerformed

    private void 修改ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改ActionPerformed
        boolean result1 = this.商品编码.getText().matches("[0-9]+");
        boolean result2 = this.商品数量.getText().matches("[0-9]+");
        boolean result3 = this.商品价格.getText().matches("[0-9]+");
        boolean result4 = this.商品时间.getText().matches("[0-9]+");
        boolean result5 = this.商品库存.getText().matches("[0-9]+");
        boolean result6 = this.每日限购.getText().matches("[0-9]+");
        boolean result7 = this.商品折扣.getText().matches("[0-9]+");
        boolean result8 = this.商品代码.getText().matches("[0-9]+");
        if (!result1 && !result2 && !result3 && !result4 && !result5 && !result6 && !result7 && !result8) {
            商城提示语言.setText("[信息]:请输入正确的数据。");
            return;
        }
        if (商品编码.getText().equals("")) {
            商城提示语言.setText("[信息]:请点击商品分类选择添加类型。");
            return;
        }
        if (商品代码.getText().equals("")) {
            商城提示语言.setText("[信息]:请输入添加的商品代码。");
            return;
        }
        if (商品价格.getText().equals("")) {
            商城提示语言.setText("[信息]:请输入商品价格。");
            return;
        }
        if (Integer.parseInt(this.商品价格.getText()) > 999999999) {
            商城提示语言.setText("[信息]:商品数量不能大于999999999。");
            return;
        }
        if (商品时间.getText().equals("")) {
            商城提示语言.setText("[信息]:请输入商品的给予时间，0 代表无限制。");
            return;
        }
        if (商品数量.getText().equals("")) {
            商城提示语言.setText("[信息]:请输入商品的商品数量。");
            return;
        }
        if (Integer.parseInt(this.商品数量.getText()) > 100) {
            商城提示语言.setText("[信息]:商品数量不能大于100。");
            return;
        }
        int 商品出售状态2;
        if (商品出售状态.getText().equals("")) {
            商品出售状态2 = -1;
        } else {
            商品出售状态2 = Integer.parseInt(this.商品出售状态.getText());
        }
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            //清楚table数据
            for (int i = ((DefaultTableModel) (this.charTable.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.charTable.getModel())).removeRow(i);
            }
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE cashshop_modified_items SET showup = ?, itemid = ?, priority = ?, period = ?, gender = ?, count = ?, meso = ?, discount_price = ?, mark = ?, unk_1 = ?, unk_2 = ?, unk_3 = ? WHERE serial = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM cashshop_modified_items WHERE serial = ?");
            ps1.setInt(1, Integer.parseInt(this.商品编码.getText()));
            rs = ps1.executeQuery();
            if (rs.next()) {

                String sqlString1 = null;
                String sqlString3 = null;
                String sqlString5 = null;
                String sqlString6 = null;
                String sqlString7 = null;

                sqlString1 = "update cashshop_modified_items set itemid='" + Integer.parseInt(this.商品代码.getText()) + "' where serial=" + Integer.parseInt(this.商品编码.getText()) + ";";
                PreparedStatement itemid = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                itemid.executeUpdate(sqlString1);

                sqlString3 = "update cashshop_modified_items set period='" + Integer.parseInt(this.商品时间.getText()) + "' where serial=" + Integer.parseInt(this.商品编码.getText()) + ";";
                PreparedStatement period = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                period.executeUpdate(sqlString3);

                sqlString5 = "update cashshop_modified_items set count='" + Integer.parseInt(this.商品数量.getText()) + "' where serial=" + Integer.parseInt(this.商品编码.getText()) + ";";
                PreparedStatement count = DatabaseConnection.getConnection().prepareStatement(sqlString5);
                count.executeUpdate(sqlString5);

                sqlString6 = "update cashshop_modified_items set discount_price='" + Integer.parseInt(this.商品价格.getText()) + "' where serial=" + Integer.parseInt(this.商品编码.getText()) + ";";
                PreparedStatement discount_price = DatabaseConnection.getConnection().prepareStatement(sqlString6);
                discount_price.executeUpdate(sqlString6);

                sqlString7 = "update cashshop_modified_items set mark='" + Integer.parseInt(this.商品出售状态.getText()) + "' where serial=" + Integer.parseInt(this.商品编码.getText()) + ";";
                PreparedStatement mark = DatabaseConnection.getConnection().prepareStatement(sqlString7);
                mark.executeUpdate(sqlString7);
                if (!商品库存.getText().equals("")) {
                    int SN库存 = Getcharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 2);
                    if (SN库存 == -1) {
                        Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 2, -2);
                    }
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 2, -SN库存);
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 2, Integer.parseInt(this.商品库存.getText()));
                } else {
                    删除SN库存2();
                }
                if (!商品折扣.getText().equals("")) {
                    int SN库存 = Getcharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 3);
                    if (SN库存 == -1) {
                        Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 3, -2);
                    }
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 3, -SN库存);
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 3, Integer.parseInt(this.商品折扣.getText()));
                } else {
                    删除SN库存3();
                }

                if (!每日限购.getText().equals("")) {
                    int SN库存 = Getcharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 4);
                    if (SN库存 == -1) {
                        Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 4, -2);
                    }
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 4, -SN库存);
                    Gaincharacter7("" + Integer.parseInt(this.商品编码.getText()) + "", 4, Integer.parseInt(this.每日限购.getText()));
                } else {
                    删除SN库存4();
                }
                商城提示语言.setText("[信息]:修改物品载入成功。");
                int n = JOptionPane.showConfirmDialog(this, "是否刷新？\r\n刷新所耗时间会根据物品数量，服务器配置决定。", "信息", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    刷新();
                }
            } else {
                商城提示语言.setText("[信息]:只是修改！如果需要添加新的SN编码！请点击添加。");

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_修改ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        String 输出 = "";
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        int 商城SN编码 = Integer.parseInt(this.商品编码.getText());
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM cashshop_modified_items WHERE serial = ?");
            ps1.setInt(1, 商城SN编码);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                String sqlstr = " delete from cashshop_modified_items where serial =" + 商城SN编码 + ";";
                ps1.executeUpdate(sqlstr);
                商城提示语言.setText("[信息]:成功删除商品。");
            } else {
                商城提示语言.setText("[信息]:删除商品失败具。");

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        删除SN库存();
        int n = JOptionPane.showConfirmDialog(this, "是否刷新？\r\n刷新所耗时间会根据物品数量，服务器配置决定。", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            刷新();
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        商城提示语言.setText("[信息]:商城重载开始。");
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, ";商城开始重新载入商品，正在维护，请勿进入。"));        // TODO add your handling code here:
        CashItemFactory.getInstance().clearCashShop();
        /*Properties 設定檔 = System.getProperties();
        FileDemo_05.deleteFiles("" + 設定檔.getProperty("user.dir") + "\\加载文件\\商城检测.ini");
        FileoutputUtil.logToFile("加载文件/商城检测.ini/", "id=,");
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemid as DATA FROM cashshop_modified_items WHERE serial > 0");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data = rs.getString("DATA");
                    FileoutputUtil.logToFile("加载文件/商城检测.ini/", "" + data + ",");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("" + Ex);
        }*/
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, ";商城重新载入商品成功，维护完毕，开放进入。"));
        商城提示语言.setText("[信息]:商城重载成功。");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        显示在线账号.setText("账号在线; " + 在线账号() + "");
        查找QQ();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void 刷新金币拍卖行ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新金币拍卖行ActionPerformed
        刷新金币拍卖行();
    }//GEN-LAST:event_刷新金币拍卖行ActionPerformed

    private void 刷新金币拍卖行1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新金币拍卖行1ActionPerformed
        刷新点券拍卖行();
    }//GEN-LAST:event_刷新金币拍卖行1ActionPerformed

    private void 拍卖行物品名字1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_拍卖行物品名字1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_拍卖行物品名字1ActionPerformed

    private void 角色点券拍卖行序号ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_角色点券拍卖行序号ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_角色点券拍卖行序号ActionPerformed

    private void 拍卖行物品代码1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_拍卖行物品代码1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_拍卖行物品代码1ActionPerformed

    private void 删除拍卖行1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除拍卖行1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_删除拍卖行1ActionPerformed

    private void 给予装备2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_给予装备2ActionPerformed
        刷装备2(1);
    }//GEN-LAST:event_给予装备2ActionPerformed

    private void 家族GPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_家族GPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_家族GPActionPerformed

    private void 家族成员5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_家族成员5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_家族成员5ActionPerformed

    private void 家族成员4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_家族成员4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_家族成员4ActionPerformed

    private void 家族成员3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_家族成员3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_家族成员3ActionPerformed

    private void 家族成员2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_家族成员2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_家族成员2ActionPerformed

    private void 家族成员1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_家族成员1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_家族成员1ActionPerformed

    private void 家族族长ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_家族族长ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_家族族长ActionPerformed

    private void 家族名称ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_家族名称ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_家族名称ActionPerformed

    private void 家族IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_家族IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_家族IDActionPerformed

    private void 刷新家族信息ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新家族信息ActionPerformed
        刷新家族信息();
    }//GEN-LAST:event_刷新家族信息ActionPerformed

    private void 修改技能1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改技能1ActionPerformed
        角色提示语言.setText("[信息]:查看玩家技能信息。");
        刷新技能信息();
    }//GEN-LAST:event_修改技能1ActionPerformed

    private void 离线角色ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_离线角色ActionPerformed
        显示在线玩家.setText("在线玩家; " + 在线玩家() + "");
        for (int i = ((DefaultTableModel) (this.角色信息.getModel())).getRowCount() - 1; i >= 0; i--) {
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
                        rs.getInt("accountid"),
                        rs.getString("name"),
                        getJobNameById(rs.getInt("job")),
                        rs.getInt("level"),
                        rs.getInt("str"),
                        rs.getInt("dex"),
                        rs.getInt("int"),
                        rs.getInt("luk"),
                        rs.getInt("maxhp"),
                        rs.getInt("maxmp"),
                        rs.getInt("meso"),
                        rs.getInt("map"),
                        rs.getInt("todayOnlineTime") + "/" + rs.getInt("totalOnlineTime"),
                        "在线",
                        rs.getInt("gm"),
                        rs.getInt("hair"),
                        rs.getInt("face"
                        )});
                }
            }
            角色提示语言.setText("[信息]:显示游戏所有离线角色信息。");

        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_离线角色ActionPerformed

    private void 在线角色ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_在线角色ActionPerformed
        显示在线玩家.setText("在线玩家; " + 在线玩家() + "");
        for (int i = ((DefaultTableModel) (this.角色信息.getModel())).getRowCount() - 1; i >= 0; i--) {
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
                        rs.getInt("accountid"),
                        rs.getString("name"),
                        getJobNameById(rs.getInt("job")),
                        rs.getInt("level"),
                        rs.getInt("str"),
                        rs.getInt("dex"),
                        rs.getInt("int"),
                        rs.getInt("luk"),
                        rs.getInt("maxhp"),
                        rs.getInt("maxmp"),
                        rs.getInt("meso"),
                        rs.getInt("map"),
                        rs.getInt("todayOnlineTime") + "/" + rs.getInt("totalOnlineTime"),
                        "在线",
                        rs.getInt("gm"),
                        rs.getInt("hair"),
                        rs.getInt("face"
                        )});
                }
            }
            角色提示语言.setText("[信息]:显示游戏所有在线角色信息。");

        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_在线角色ActionPerformed

    private static int 下载冷却 = 0;
    private void 新增个人ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增个人ActionPerformed
        boolean result1 = this.jTextField1.getText().matches("[0-9]+");
        if (result1) {
            try (Connection con = DatabaseConnection1.getConnection1(); PreparedStatement ps = con.prepareStatement("INSERT INTO 常用的发福利物品记录 ( 物品代码,账号 ) VALUES ( ?,? )")) {
                ps.setInt(1, Integer.parseInt(jTextField1.getText()));
                ps.setString(2, MapleParty.启动账号);
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
            刷新个人();
            JOptionPane.showMessageDialog(null, "新增成功。");
        } else {
            JOptionPane.showMessageDialog(null, "填写正确的物品代码。");
        }
    }//GEN-LAST:event_新增个人ActionPerformed

    private void 刷新个人ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新个人ActionPerformed
        刷新个人();
    }//GEN-LAST:event_刷新个人ActionPerformed

    private void 刷新通用ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新通用ActionPerformed
        JOptionPane.showMessageDialog(null, "暂未开放。");//刷新通用();
    }//GEN-LAST:event_刷新通用ActionPerformed

    private void 新增通用ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增通用ActionPerformed
        JOptionPane.showMessageDialog(null, "暂未开放。");

        /*boolean result1 = this.jTextField1.getText().matches("[0-9]+");
        if (result1) {
            try (Connection con = DatabaseConnection1.getConnection1(); PreparedStatement ps = con.prepareStatement("INSERT INTO 常用的发福利物品记录 ( 物品代码 ) VALUES ( ? )")) {
                ps.setInt(1, Integer.parseInt(jTextField1.getText()));
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
            刷新通用();
            JOptionPane.showMessageDialog(null, "新增成功。");
        } else {
            JOptionPane.showMessageDialog(null, "填写正确的物品代码。");*/
        //}
    }//GEN-LAST:event_新增通用ActionPerformed

    private void 删除个人ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除个人ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        if (!"".equals(福利序号.getText())) {
            try {
                ps1 = DatabaseConnection1.getConnection1().prepareStatement("SELECT * FROM 常用的发福利物品记录 WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.福利序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from 常用的发福利物品记录 where id =" + Integer.parseInt(this.福利序号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                }
                刷新个人();
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "选择你要删除的记录。");
        }
    }//GEN-LAST:event_删除个人ActionPerformed

    private void 删除通用ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_删除通用ActionPerformed
        JOptionPane.showMessageDialog(null, "暂未开放。");
    }//GEN-LAST:event_删除通用ActionPerformed

    private void 修改记录ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改记录ActionPerformed
        boolean result1 = this.jTextField1.getText().matches("[0-9]+");
        if (result1) {
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            try {
                ps = DatabaseConnection1.getConnection1().prepareStatement("UPDATE 常用的发福利物品记录 SET 物品代码 = ? WHERE id = ?");
                ps1 = DatabaseConnection1.getConnection1().prepareStatement("SELECT * FROM 常用的发福利物品记录 WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.福利序号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update 常用的发福利物品记录 set 物品代码='" + this.jTextField1.getText() + "' where id = " + this.福利序号.getText() + ";";
                    PreparedStatement name = DatabaseConnection1.getConnection1().prepareStatement(sqlString1);
                    name.executeUpdate(sqlString1);
                    刷新个人();
                }
            } catch (SQLException ex) {
                Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_修改记录ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        显示在线账号.setText("账号在线; " + 在线账号() + "");
        查找账号();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        刷新更新记录();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void 刷新模式ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新模式ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_刷新模式ActionPerformed

    private void 快捷面板ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_快捷面板ActionPerformed
        //JOptionPane.showMessageDialog(null, "未启用。");
        int n = JOptionPane.showConfirmDialog(this, "你需要打开数据面板吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            快捷面板();
        }
    }//GEN-LAST:event_快捷面板ActionPerformed

    private void 保存数据1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_保存数据1ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "需要保存数据吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            int p = 0;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    p++;
                    chr.saveToDB(true, true);
                }
            }
            String 输出 = "[保存数据系统] 保存" + p + "个成功。";
            JOptionPane.showMessageDialog(null, 输出);
        }
    }//GEN-LAST:event_保存数据1ActionPerformed

    private void 保存雇佣1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_保存雇佣1ActionPerformed
        // TODO add your handling code here:
        int p = 0;
        for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
            p++;
            cserv.closeAllMerchant();
        }
        JOptionPane.showMessageDialog(null, "雇佣商人保存" + p + "个频道成功。");
    }//GEN-LAST:event_保存雇佣1ActionPerformed

    private void 应用重载1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_应用重载1ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要应用重载吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            //重载商店
            MapleShopFactory.getInstance().clear();
            //重载反应堆
            ReactorScriptManager.getInstance().clearDrops();
            //重载爆率
            MapleMonsterInformationProvider.getInstance().clearDrops();
            //重载传送
            PortalScriptManager.getInstance().clearScripts();
            //重载机器人
            new Thread(QQMsgServer.getInstance()).start();
            //重载云防黑名单
            GetCloudBacklist();
            //重载任务同步信息
            gui.Start.GetConfigValues();
            gui.Start.读取地图吸怪检测();
            gui.Start.读取技能范围检测();

            gui.Start.读取技能PVP伤害();
            MapleParty.椅子3010025 = 取椅子备注(3010025);
            MapleParty.椅子3010100 = 取椅子备注(3010100);
            MapleParty.椅子3012002 = 取椅子备注(3012002);
            try {
                下载文件("http://123.207.53.97:8082/任务修复/任务修复.zip", "任务修复.zip", "" + 任务更新下载目录() + "/");
                解压文件.解压文件(任务更新解压目录("任务修复"), 任务更新导入目录("zevms"));
                删除文件(任务更新解压目录("任务修复"));
            } catch (Exception e) {

            }
            Start.GetFuMoInfo();
            //重置仙人数据();
            JOptionPane.showMessageDialog(null, "重载完成。");
        }
    }//GEN-LAST:event_应用重载1ActionPerformed
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
    private void 活动控制台1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_活动控制台1ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开活动控制台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            活动控制台();
        }
    }//GEN-LAST:event_活动控制台1ActionPerformed

    private void 充值卡后台1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_充值卡后台1ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开充值卡后台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            充值卡后台();
        }
    }//GEN-LAST:event_充值卡后台1ActionPerformed

    private void 副本控制台1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_副本控制台1ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开 3 号制台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            副本控制台();
        }
    }//GEN-LAST:event_副本控制台1ActionPerformed

    private void 控制台2号1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_控制台2号1ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开 2 号制台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            经济控制台();
        }
    }//GEN-LAST:event_控制台2号1ActionPerformed

    private void 控制台1号1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_控制台1号1ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要打开 1 号控制台吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            动态配置台();
        }
    }//GEN-LAST:event_控制台1号1ActionPerformed

    private void 更多应用1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_更多应用1ActionPerformed
        更多应用();
    }//GEN-LAST:event_更多应用1ActionPerformed

    private void 关闭服务端1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_关闭服务端1ActionPerformed
        int n = JOptionPane.showConfirmDialog(this, "你需要关闭服务端吗？", "信息", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            关闭服务端();
        }
    }//GEN-LAST:event_关闭服务端1ActionPerformed

    private void 刷新PVP列表ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_刷新PVP列表ActionPerformed
        刷新PVP技能设置();
    }//GEN-LAST:event_刷新PVP列表ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.技能PVP编号.getText().matches("[0-9]+");

        if (result1) {
            try {
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pvpskills WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.技能PVP编号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " delete from pvpskills where id =" + Integer.parseInt(this.技能PVP编号.getText()) + "";
                    ps1.executeUpdate(sqlstr);
                    刷新PVP技能设置();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void 新增1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_新增1ActionPerformed
        if (技能PVP伤害.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "请填信息哦。");
            return;
        }
        int ID = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `id` FROM pvpskills  ORDER BY `id` DESC LIMIT 1");
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
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO pvpskills ( id,name,Val ) VALUES ( ? ,?,?)")) {
            ps.setInt(1, ID);
            ps.setString(2, 技能PVPID.getText());
            ps.setInt(3, Integer.parseInt(技能PVP伤害.getText()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(控制台2号.class.getName()).log(Level.SEVERE, null, ex);
        }
        刷新PVP技能设置();
    }//GEN-LAST:event_新增1ActionPerformed

    private void 修改1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_修改1ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.技能PVP编号.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE pvpskills SET Val = ? WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pvpskills WHERE id = ?");
                ps1.setInt(1, Integer.parseInt(this.技能PVP编号.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update pvpskills set Val = '" + this.技能PVP伤害.getText() + "' where id= " + this.技能PVP编号.getText() + ";";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    刷新PVP技能设置();
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
        boolean result1 = this.技能PVPID.getText().matches("[0-9]+");

        if (!result1) {
            return;
        }
        for (int i = ((DefaultTableModel) (this.PVP技能设置.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.PVP技能设置.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM pvpskills WHERE name = " + 技能PVPID.getText() + "");
            rs = ps.executeQuery();
            while (rs.next()) {
                MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz"));
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
                ((DefaultTableModel) PVP技能设置.getModel()).insertRow(PVP技能设置.getRowCount(), new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("Val"),
                    itemName
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        PVP技能设置.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = PVP技能设置.getSelectedRow();
                String a = PVP技能设置.getValueAt(i, 0).toString();
                String a1 = PVP技能设置.getValueAt(i, 1).toString();
                String a2 = PVP技能设置.getValueAt(i, 2).toString();
                技能PVP编号.setText(a);
                技能PVPID.setText(a1);
                技能PVP伤害.setText(a2);
            }
        });
        读取技能PVP伤害();
    }//GEN-LAST:event_搜索1ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (MapleParty.冷却1 == 0) {
            MapleParty.冷却1 = 1;
            刷新信息();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 60 * 1);
                        MapleParty.冷却1 = 0;
                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }
                }
            }.start();
        } else {
            JOptionPane.showMessageDialog(null, "刷新冷却中。");
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void 查询ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_查询ActionPerformed
        final MerchItemPackage pack = loadItemFrom_Database(Integer.parseInt(雇佣角色ID2.getText()), 角色ID取账号ID(Integer.parseInt(雇佣角色ID2.getText())));
        if (pack == null && 角色ID取雇佣数据(Integer.parseInt(雇佣角色ID2.getText())) > 0) {
            JOptionPane.showMessageDialog(null, "确定为异常，可以补偿。");
        } else if (pack != null) {
            JOptionPane.showMessageDialog(null, "未发现异常。");
        }
    }//GEN-LAST:event_查询ActionPerformed
    private static final MerchItemPackage loadItemFrom_Database(final int charid, final int accountid) {
        final Connection con = DatabaseConnection.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where characterid = ? OR accountid = ?");
            ps.setInt(1, charid);
            ps.setInt(2, accountid);
            //System.out.println("5");
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                ps.close();
                rs.close();
                return null;
            }
            final int packageid = rs.getInt("PackageId");

            final MerchItemPackage pack = new MerchItemPackage();
            pack.setPackageid(packageid);
            pack.setMesos(rs.getInt("Mesos"));
            pack.setSentTime(rs.getLong("time"));
            ps.close();
            rs.close();
            Map<Integer, Pair<IItem, MapleInventoryType>> items = ItemLoader.HIRED_MERCHANT.loadItems_hm(packageid, accountid);
            if (items != null) {
                List<IItem> iters = new ArrayList<IItem>();
                for (Pair<IItem, MapleInventoryType> z : items.values()) {
                    iters.add(z.left);
                }
                pack.setItems(iters);
            }
            return pack;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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

    public static int 角色ID取雇佣数据(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT cid as DATA FROM hire WHERE cid = ?");
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
    private void 雇佣角色ID2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_雇佣角色ID2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_雇佣角色ID2ActionPerformed

    private void 泡点值修改1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_泡点值修改1ActionPerformed
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        boolean result1 = this.泡点值1.getText().matches("[0-9]+");
        if (result1) {
            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE configvalues SET Val = ? WHERE id = ?");

                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM configvalues WHERE id = ?");

                ps1.setInt(1, Integer.parseInt(this.泡点序号1.getText()));
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    sqlString1 = "update configvalues set Val = '" + this.泡点值1.getText() + "' where id= " + this.泡点序号1.getText() + ";";
                    PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    Val.executeUpdate(sqlString1);
                    刷新雇佣泡点设置();
                    gui.Start.GetConfigValues();
                    福利提示语言2.setText("[信息]:修改成功已经生效。");
                }
            } catch (SQLException ex) {
                Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            福利提示语言2.setText("[信息]:请选择你要修改的值。");
        }
    }//GEN-LAST:event_泡点值修改1ActionPerformed

    private void 清空QQ相关ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_清空QQ相关ActionPerformed
        // 清空QQ相关();

    }//GEN-LAST:event_清空QQ相关ActionPerformed
    public static int 账号ID取角色ID(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = " + a + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data = rs.getInt("id");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 绑定QQ取账号ID(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE qq = " + a + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data = rs.getInt("id");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 角色ID取账号ID并判断账号是否存在(int a) {
        int data = 0;
        int data2 = 2;
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = " + a + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data = rs.getInt("id");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE id = " + data + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data2 += 1;
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data2;
    }

    public void 清空QQ相关() {
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            int n = JOptionPane.showConfirmDialog(this, "你确定要删除这个QQ下所有账号，角色信息数据吗？", "信息", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                /**
                 * <清除绑定QQ下所有账号的角色>
                 */
                try {
                    ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters");
                    rs = ps1.executeQuery();
                    if (rs.next()) {
                        String sqlstr = " delete from characters where id =" + 账号ID取角色ID(绑定QQ取账号ID(Integer.parseInt(账号操作.getText()))) + "";
                        ps1.executeUpdate(sqlstr);
                        String sqlstr2 = " delete from inventoryitems where characterid =" + 账号ID取角色ID(绑定QQ取账号ID(Integer.parseInt(账号操作.getText()))) + "";
                        ps1.executeUpdate(sqlstr2);
                        String sqlstr3 = " delete from auctionitems where characterid =" + 账号ID取角色ID(绑定QQ取账号ID(Integer.parseInt(账号操作.getText()))) + "";
                        ps1.executeUpdate(sqlstr3);
                        String sqlstr31 = " delete from auctionitems1 where characterid =" + 账号ID取角色ID(绑定QQ取账号ID(Integer.parseInt(账号操作.getText()))) + "";
                        ps1.executeUpdate(sqlstr31);
                        String sqlstr4 = " delete from csitems where accountid =" + 账号ID取角色ID(绑定QQ取账号ID(Integer.parseInt(账号操作.getText()))) + "";
                        ps1.executeUpdate(sqlstr4);
                        String sqlstr5 = " delete from bank_item where cid =" + 账号ID取角色ID(绑定QQ取账号ID(Integer.parseInt(账号操作.getText()))) + "";
                        ps1.executeUpdate(sqlstr5);
                        String sqlstr6 = " delete from bossrank where cid =" + 账号ID取角色ID(绑定QQ取账号ID(Integer.parseInt(账号操作.getText()))) + "";
                        ps1.executeUpdate(sqlstr6);
                        String sqlstr7 = " delete from skills where characterid =" + 账号ID取角色ID(绑定QQ取账号ID(Integer.parseInt(账号操作.getText()))) + "";
                        ps1.executeUpdate(sqlstr7);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
                }

                /**
                 * <清除绑定QQ下所有账号>
                 */
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM accounts ");
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlstr = " Delete from accounts where qq ='" + this.账号操作.getText() + "'";
                    ps1.executeUpdate(sqlstr);
                    刷新账号信息();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void 刷新PVP技能设置() {
        for (int i = ((DefaultTableModel) (this.PVP技能设置.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.PVP技能设置.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM pvpskills ");
            rs = ps.executeQuery();
            while (rs.next()) {
                MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz"));
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
                ((DefaultTableModel) PVP技能设置.getModel()).insertRow(PVP技能设置.getRowCount(), new Object[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("Val"),
                    itemName
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        PVP技能设置.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = PVP技能设置.getSelectedRow();
                String a = PVP技能设置.getValueAt(i, 0).toString();
                String a1 = PVP技能设置.getValueAt(i, 1).toString();
                String a2 = PVP技能设置.getValueAt(i, 2).toString();
                技能PVP编号.setText(a);
                技能PVPID.setText(a1);
                技能PVP伤害.setText(a2);
            }
        });
        读取技能PVP伤害();
        // 读取地图吸怪检测();
    }

    public void 刷新个人() {
        for (int i = ((DefaultTableModel) (this.存储发送物品.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.存储发送物品.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM 常用的发福利物品记录 WHERE 账号 = " + MapleParty.启动账号 + "");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 存储发送物品.getModel()).insertRow(存储发送物品.getRowCount(), new Object[]{
                    rs.getString("id"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("物品代码")),
                    rs.getString("物品代码")
                });

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        存储发送物品.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 存储发送物品.getSelectedRow();
                String a = 存储发送物品.getValueAt(i, 0).toString();
                String a1 = 存储发送物品.getValueAt(i, 2).toString();
                福利序号.setText(a);
                jTextField1.setText(a1);
                全服发送物品代码.setText(a1);
                个人发送物品代码.setText(a1);
                全服发送装备物品ID.setText(a1);
            }
        });
    }

    public void 刷新通用() {
        for (int i = ((DefaultTableModel) (this.存储发送物品.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.存储发送物品.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM 常用的发福利物品记录");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 存储发送物品.getModel()).insertRow(存储发送物品.getRowCount(), new Object[]{
                    rs.getString("id"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("物品代码")),
                    rs.getString("物品代码")
                });

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        存储发送物品.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 存储发送物品.getSelectedRow();
                String a = 存储发送物品.getValueAt(i, 0).toString();
                String a1 = 存储发送物品.getValueAt(i, 2).toString();
                福利序号.setText(a);
                jTextField1.setText(a1);
                全服发送物品代码.setText(a1);
                个人发送物品代码.setText(a1);
                全服发送装备物品ID.setText(a1);
            }
        });
    }

    public static void 增加下载次数(String a) {
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 应用下载");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("文件名").equals("" + a + "")) {
                    String aa = null;
                    int b = rs.getInt("下载次数");
                    aa = "update 应用下载 set 下载次数=" + (b + 1) + " where 文件名=" + a + ";";
                    PreparedStatement 版本 = DatabaseConnection1.getConnection1().prepareStatement(aa);
                    版本.executeUpdate(aa);
                }
            }
        } catch (SQLException ex) {
        }
    }

    private void 刷新更新记录() {

        for (int i = ((DefaultTableModel) (this.更新记录.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.更新记录.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection1.getConnection1();
            PreparedStatement ps = null;

            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM 更新记录 order by id desc");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 更新记录.getModel()).insertRow(更新记录.getRowCount(), new Object[]{
                    rs.getString("id"),
                    rs.getString("版本"),
                    rs.getString("文本"),
                    rs.getString("详细")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        更新记录.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = 更新记录.getSelectedRow();
                String a0 = 更新记录.getValueAt(i, 0).toString();
                String a1 = 更新记录.getValueAt(i, 1).toString();
                String a2 = 更新记录.getValueAt(i, 2).toString();
                String a3 = 更新记录.getValueAt(i, 3).toString();
                更新记录详细.setText(a3);
                标题.setText(a0 + ":" + a1);
            }
        });

    }

    private void 刷新金币拍卖行() {
        for (int i = ((DefaultTableModel) (this.金币拍卖行.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.金币拍卖行.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM auctionitems");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 状态 = "";
                switch (rs.getInt("auctionState")) {
                    case 1:
                        状态 = "上架";
                        break;
                    case 2:
                        状态 = "售出";
                        break;
                    case 0:
                        状态 = "下架";
                        break;
                    default:
                        状态 = "XX";
                        break;
                }
                String 买家 = "";
                if (!"null".equals(rs.getString("buyerName"))) {
                    买家 = rs.getString("buyerName");
                } else {
                    买家 = "未售出";
                }

                ((DefaultTableModel) 金币拍卖行.getModel()).insertRow(金币拍卖行.getRowCount(), new Object[]{
                    rs.getInt("id"), //账号ID
                    rs.getString("characterName"),
                    状态,
                    买家,
                    rs.getString("price"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                });

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void 刷新点券拍卖行() {

        for (int i = ((DefaultTableModel) (this.点券拍卖行.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.点券拍卖行.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM auctionitems1");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 状态 = "";
                switch (rs.getInt("auctionState")) {
                    case 1:
                        状态 = "上架";
                        break;
                    case 2:
                        状态 = "售出";
                        break;
                    case 0:
                        状态 = "下架";
                        break;
                    default:
                        状态 = "XX";
                        break;
                }
                String 买家 = "";
                if (!"null".equals(rs.getString("buyerName"))) {
                    买家 = rs.getString("buyerName");
                } else {
                    买家 = "未售出";
                }
                ((DefaultTableModel) 点券拍卖行.getModel()).insertRow(点券拍卖行.getRowCount(), new Object[]{
                    rs.getInt("id"), //账号ID
                    rs.getString("characterName"),
                    状态,
                    买家,
                    rs.getString("price"),
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"))
                });

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void 关闭服务端() {
        //开始保存第一次雇佣
        int p = 0;
        for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
            p++;
            cserv.closeAllMerchant();
        }
        //开始保存玩家数据
        int pp = 0;
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                pp++;
                chr.saveToDB(true, true);
            }
        }
        //切断所有链接
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.getPlayerStorage().断所有人(true);
        }
        //切断所有链接
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.getPlayerStorage().断所有人(true);
        }
        //开始关闭服务端
        try {
            minutesLeft = 0;
            if (ts == null && (t == null || !t.isAlive())) {
                t = new Thread(ShutdownServer.getInstance());
                ts = Timer.EventTimer.getInstance().register(new Runnable() {

                    @Override
                    public void run() {
                        if (minutesLeft == 0) {
                            ShutdownServer.getInstance();
                            t.start();
                            ts.cancel(false);
                            return;
                        }
                        minutesLeft--;
                    }
                }, 60000);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "错误!\r\n" + e);
        }
    }

    private static ScheduledFuture<?> ts = null;
    private int minutesLeft = 0;
    private static Thread t = null;

    public static void 动态配置台() {
        if (服务端功能开关 != null) {
            服务端功能开关.dispose();
        }
        服务端功能开关 = new 控制台1号();
        服务端功能开关.setVisible(true);
    }

    public void 刷新商城扩充价格() {
        for (int i = ((DefaultTableModel) (this.商城扩充价格.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.商城扩充价格.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM configvalues WHERE id = 999 ");
            rs = ps.executeQuery();
            while (rs.next()) {
                ((DefaultTableModel) 商城扩充价格.getModel()).insertRow(商城扩充价格.getRowCount(), new Object[]{rs.getString("Val")});

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void 公告发送(int a) {
        try {
            String str = 公告填写.getText();
            String 输出 = "";
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    switch (a) {
                        case 1:
                            //顶端公告
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverMessage(str.toString()));
                            break;
                        case 2:
                            //弹窗公告
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(1, str));
                            break;
                        case 3:
                            //聊天蓝色公告
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, str));
                            break;
                        case 4:
                            mch.startMapEffect(str, Integer.parseInt(公告发布喇叭代码.getText()));
                            break;
                        default:
                            break;
                    }
                }
                公告发布喇叭代码.setText("5120027");
            }
            sendMsgToQQGroup("<" + MapleParty.开服名字 + ">公告:" + str + "。");
            公告提示语言.setText("[信息]:" + str + "");
        } catch (Exception e) {
            公告提示语言.setText("[信息]:" + e);
        }
    }

    private void 刷物品2() {
        try {
            int 数量;
            int 物品ID;
            物品ID = Integer.parseInt(全服发送物品代码.getText());
            数量 = Integer.parseInt(全服发送物品数量.getText());
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType type = GameConstants.getInventoryType(物品ID);
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (数量 >= 0) {
                        if (!MapleInventoryManipulator.checkSpace(mch.getClient(), 物品ID, 数量, "")) {
                            return;
                        }
                        if (type.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(物品ID) && !GameConstants.isBullet(物品ID)
                                || type.equals(MapleInventoryType.CASH) && 物品ID >= 5000000 && 物品ID <= 5000100) {
                            final Equip item = (Equip) (ii.getEquipById(物品ID));
                            if (ii.isCash(物品ID)) {
                                item.setUniqueId(1);
                            }
                            final String name = ii.getName(物品ID);
                            if (物品ID / 10000 == 114 && name != null && name.length() > 0) { //medal
                                final String msg = "你已获得称号 <" + name + ">";
                                mch.getClient().getPlayer().dropMessage(5, msg);
                            }
                            MapleInventoryManipulator.addbyItem(mch.getClient(), item.copy());
                        } else {
                            MapleInventoryManipulator.addById(mch.getClient(), 物品ID, (short) 数量, "", null, (byte) 0);
                        }
                    } else {
                        MapleInventoryManipulator.removeById(mch.getClient(), GameConstants.getInventoryType(物品ID), 物品ID, -数量, true, false);
                    }
                    mch.getClient().sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 数量, true));
                }
            }
            全服发送物品代码.setText("");
            全服发送物品数量.setText("");
            福利提示语言.setText("[信息]:发送成功。");
        } catch (Exception e) {
            福利提示语言.setText("[信息]:错误!" + e);
        }
    }

    private void 刷物品() {
        try {
            String 名字;
            if ("玩家名字".equals(个人发送物品玩家名字.getText())) {
                名字 = "";
            } else {
                名字 = 个人发送物品玩家名字.getText();
            }
            int 物品ID;
            if ("物品ID".equals(个人发送物品代码.getText())) {
                物品ID = 0;
            } else {
                物品ID = Integer.parseInt(个人发送物品代码.getText());
            }
            int 数量;
            if ("数量".equals(个人发送物品数量.getText())) {
                数量 = 0;
            } else {
                数量 = Integer.parseInt(个人发送物品数量.getText());
            }
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType type = GameConstants.getInventoryType(物品ID);
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (mch.getName().equals(名字)) {
                        if (数量 >= 0) {
                            if (!MapleInventoryManipulator.checkSpace(mch.getClient(), 物品ID, 数量, "")) {
                                return;
                            }
                            if (type.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(物品ID) && !GameConstants.isBullet(物品ID)
                                    || type.equals(MapleInventoryType.CASH) && 物品ID >= 5000000 && 物品ID <= 5000100) {
                                final Equip item = (Equip) (ii.getEquipById(物品ID));
                                if (ii.isCash(物品ID)) {
                                    item.setUniqueId(1);
                                }
                                final String name = ii.getName(物品ID);
                                if (物品ID / 10000 == 114 && name != null && name.length() > 0) { //medal
                                    final String msg = "你已获得称号 <" + name + ">";
                                    mch.getClient().getPlayer().dropMessage(5, msg);
                                }
                                MapleInventoryManipulator.addbyItem(mch.getClient(), item.copy());
                            } else {
                                MapleInventoryManipulator.addById(mch.getClient(), 物品ID, (short) 数量, "", null, (byte) 0);
                            }
                        } else {
                            MapleInventoryManipulator.removeById(mch.getClient(), GameConstants.getInventoryType(物品ID), 物品ID, -数量, true, false);
                        }
                        mch.getClient().sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 数量, true));
                    }
                }
            }
            个人发送物品玩家名字.setText("");
            个人发送物品代码.setText("");
            个人发送物品数量.setText("");
            福利提示语言.setText("[信息]:发送成功。");
        } catch (Exception e) {
            福利提示语言.setText("[信息]:错误!" + e);
        }
    }

    private void 发送福利(int a) {
        boolean result1 = this.a1.getText().matches("[0-9]+");
        if (result1) {
            int 数量;
            if ("100000000".equals(a1.getText())) {
                数量 = 100;
            } else {
                数量 = Integer.parseInt(a1.getText());
            }
            if (数量 <= 0 || 数量 > 999999999) {
                return;
            }
            String 类型 = "";
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {

                    switch (a) {
                        case 1:
                            类型 = "点券";
                            mch.modifyCSPoints(1, 数量, true);
                            break;
                        case 2:
                            类型 = "抵用券";
                            mch.modifyCSPoints(2, 数量, true);
                            break;
                        case 3:
                            类型 = "金币";
                            mch.gainMeso(数量, true);
                            break;
                        case 4:
                            类型 = "经验";
                            mch.gainExp(数量, false, false, false);
                            break;
                        case 5:
                            类型 = "人气";
                            mch.addFame(数量);
                            break;
                        case 6:
                            类型 = "豆豆";
                            mch.gainBeans(数量);
                            break;
                        default:
                            break;
                    }
                    mch.startMapEffect("管理员发放 " + 数量 + " " + 类型 + "给在线的所有玩家！", 5120027);
                }
            }
            福利提示语言.setText("[信息]:发放 " + 数量 + " " + 类型 + "给在线的所有玩家。");
            a1.setText("");
            JOptionPane.showMessageDialog(null, "发送成功");
        } else {
            福利提示语言.setText("[信息]:请输入要发送数量。");
        }
    }

    /* private void 发送福利(int a) {
        boolean result1 = this.a1.getText().matches("[0-9]+");
        if (result1) {
            int 数量;
            if ("100000000".equals(a1.getText())) {
                数量 = 100;
            } else {
                数量 = Integer.parseInt(a1.getText());
            }
            if (数量 <= 0 || 数量 > 999999999) {
                return;
            }
            String 类型 = "";
            switch (a) {
                case 1:
                    for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                        for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                            类型 = "点券";
                            mch.modifyCSPoints(1, 数量, true);
                            mch.startMapEffect("管理员发放 " + 数量 + " " + 类型 + "给在线的所有玩家！", 5120027);
                        }
                    }
                    break;
                case 2:
                    for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                        for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                            类型 = "抵用券";
                            mch.modifyCSPoints(2, 数量, true);
                            mch.startMapEffect("管理员发放 " + 数量 + " " + 类型 + "给在线的所有玩家！", 5120027);
                            
                        }
                    }
                    break;
                case 3:
                    for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                        for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                            类型 = "金币";
                            mch.gainMeso(数量, true);
                            mch.startMapEffect("管理员发放 " + 数量 + " " + 类型 + "给在线的所有玩家！", 5120027);
                           
                        }
                    }
                    break;
                case 4:
                    for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                        for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                            类型 = "经验";
                            mch.gainExp(数量, false, false, false);
                            mch.startMapEffect("管理员发放 " + 数量 + " " + 类型 + "给在线的所有玩家！", 5120027);
                            
                        }
                    }
                    break;
                case 5:
                    for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                        for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                            类型 = "人气";
                            mch.addFame(数量);
                            mch.startMapEffect("管理员发放 " + 数量 + " " + 类型 + "给在线的所有玩家！", 5120027);
                            福利提示语言.setText("[信息]:发放 " + 数量 + " " + 类型 + "给在线的所有玩家。");
                        }
                    }
                    break;
                case 6:
                    for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                        for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                            类型 = "豆豆";
                            mch.gainBeans(数量);
                            mch.startMapEffect("管理员发放 " + 数量 + " " + 类型 + "给在线的所有玩家！", 5120027);
                            
                        }
                    }
                    break;
                default:
                    break;
            }
            a1.setText("");
            福利提示语言.setText("[信息]:发送成功。");
        } else {
            福利提示语言.setText("[信息]:请输入要发送数量。");
        }
    }
     */
    public void 上架() {

        try {
            int SN_ = Integer.parseInt(String.valueOf(this.charTable.getValueAt(this.charTable.getSelectedRow(), 0)));
            //清楚table数据
            for (int i = ((DefaultTableModel) (this.charTable.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.charTable.getModel())).removeRow(i);
            }
            int OnSale_ = 1;
            CashItemInfo merchandise = new CashItemInfo(SN_, OnSale_);
            int success = update上下架(merchandise);
            if (success == 0) {
                商城提示语言.setText("[信息]:上架失败。");
            } else {
                initCharacterPannel();
                商城提示语言.setText("[信息]:上架成功。");
            }
        } catch (NumberFormatException e) {
            System.err.println(e);
            商城提示语言.setText("[信息]:上架失败，请选中你要上架的道具。");
        }
    }

    public void 下架() {
        try {
            int SN_ = Integer.parseInt(String.valueOf(this.charTable.getValueAt(this.charTable.getSelectedRow(), 0)));
            //清楚table数据
            for (int i = ((DefaultTableModel) (this.charTable.getModel())).getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) (this.charTable.getModel())).removeRow(i);
            }
            int OnSale_ = 0;
            CashItemInfo merchandise = new CashItemInfo(SN_, OnSale_);
            int success = update上下架(merchandise);
            if (success == 0) {
                商城提示语言.setText("[信息]:下架失败。");
            } else {
                initCharacterPannel();
                商城提示语言.setText("[信息]:下架成功。");
            }
        } catch (NumberFormatException e) {
            System.err.println(e);
            商城提示语言.setText("[信息]:下架失败，请选中你要上架的道具。");
        }
    }

    public static int update上下架(CashItemInfo merchandise) {//修改
        PreparedStatement ps = null;
        int resulet = 0;
        Connection conn = DatabaseConnection.getConnection();
        int i = 0;
        try {
            ps = conn.prepareStatement("update cashshop_modified_items set showup = ? where serial = ?");//itemid
            ps.setInt(++i, merchandise.getOnSale());
            ps.setInt(++i, merchandise.getSN());
            resulet = ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return resulet;
    }

    public void 读取商品(final int a, int b, int c, int d) {
        for (int i = ((DefaultTableModel) (this.charTable.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.charTable.getModel())).removeRow(i);
        }
        商品编码.setText("" + a + "");
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            PreparedStatement pse;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM cashshop_modified_items WHERE serial >= " + a + " && serial < " + b + "");
            rs = ps.executeQuery();
            while (rs.next()) {
                String 上架状态 = "";
                if (rs.getInt("showup") == 0) {
                    上架状态 = "已经下架↓";
                } else {
                    上架状态 = "已经上架↑";
                }
                String 出售状态2 = "";
                switch (rs.getInt("mark")) {
                    case -1:
                        出售状态2 = "无";
                        break;
                    case 0:
                        出售状态2 = "NEW";
                        break;
                    case 1:
                        出售状态2 = "Sale";
                        break;
                    case 2:
                        出售状态2 = "HOT";
                        break;
                    case 3:
                        出售状态2 = "Event";
                        break;
                    default:
                        break;
                }
                String 类型 = "";
                if ("".equals(NPCConversationManager.SN取类型(rs.getInt("serial")))) {
                    类型 = "点券";
                } else {
                    类型 = "点/抵用券";
                }
                ((DefaultTableModel) charTable.getModel()).insertRow(charTable.getRowCount(), new Object[]{
                    rs.getInt("serial"),
                    rs.getInt("itemid"),
                    //itemName,
                    MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid")),
                    rs.getInt("count"),
                    rs.getInt("discount_price"),
                    rs.getInt("period"),
                    出售状态2,
                    上架状态,
                    NPCConversationManager.SN取出售(rs.getInt("serial")),
                    NPCConversationManager.SN取库存(rs.getInt("serial")),
                    NPCConversationManager.SN取折扣(rs.getInt("serial")),
                    NPCConversationManager.SN取限购(rs.getInt("serial")),
                    类型
                });

            }
        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `serial` FROM cashshop_modified_items WHERE serial >= " + a + " && serial <" + b + " ORDER BY `serial` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String SN = rs.getString("serial");
                    int sns = Integer.parseInt(SN);
                    sns++;
                    商品编码.setText("" + sns);
                    ps.close();
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("出错读取商品：" + ex.getMessage());
        }
        if (c == 1 && d == 1) {
            显示类型.setText("热销产品");
            商城提示语言.setText("[信息]:显示热销产品，双击后可在热销产品下添加商品。");
        } else if (c == 1 && d == 2) {
            显示类型.setText("主题馆");
            商城提示语言.setText("[信息]:显示主题馆，双击后可在主题馆下添加商品。");
        } else if (c == 1 && d == 3) {
            显示类型.setText("活动");
            商城提示语言.setText("[信息]:显示活动，双击后可在活动下添加商品。");
        } else if (c == 2 && d == 1) {
            显示类型.setText("帽子");
            商城提示语言.setText("[信息]:显示帽子，双击后可在帽子下添加商品。");
        } else if (c == 2 && d == 2) {
            显示类型.setText("裙裤");
            商城提示语言.setText("[信息]:显示裙裤，双击后可在裙裤下添加商品。");
        } else if (c == 2 && d == 3) {
            显示类型.setText("披风");
            商城提示语言.setText("[信息]:显示披风，双击后可在披风下添加商品。");
        } else if (c == 2 && d == 4) {
            显示类型.setText("飞镖");
            商城提示语言.setText("[信息]:显示飞镖，双击后可在飞镖下添加商品。");
        } else if (c == 2 && d == 5) {
            显示类型.setText("长袍");
            商城提示语言.setText("[信息]:显示长袍，双击后可在长袍下添加商品。");
        } else if (c == 2 && d == 6) {
            显示类型.setText("脸饰");
            商城提示语言.setText("[信息]:显示脸饰，双击后可在脸饰下添加商品。");
        } else if (c == 2 && d == 7) {
            显示类型.setText("鞋子");
            商城提示语言.setText("[信息]:显示鞋子，双击后可在鞋子下添加商品。");
        } else if (c == 2 && d == 8) {
            显示类型.setText("骑宠");
            商城提示语言.setText("[信息]:显示骑宠，双击后可在骑宠下添加商品。");
        } else if (c == 2 && d == 9) {
            显示类型.setText("戒指");
            商城提示语言.setText("[信息]:显示戒指，双击后可在戒指下添加商品。");
        } else if (c == 2 && d == 10) {
            显示类型.setText("眼饰");
            商城提示语言.setText("[信息]:显示眼饰，双击后可在眼饰下添加商品。");
        } else if (c == 2 && d == 11) {
            显示类型.setText("手套");
            商城提示语言.setText("[信息]:显示手套，双击后可在手套下添加商品。");
        } else if (c == 2 && d == 12) {
            显示类型.setText("武器");
            商城提示语言.setText("[信息]:显示武器，双击后可在武器下添加商品。");
        } else if (c == 2 && d == 13) {
            显示类型.setText("上衣");
            商城提示语言.setText("[信息]:显示上衣，双击后可在上衣下添加商品。");
        } else if (c == 3 && d == 1) {
            显示类型.setText("喜庆物品");
            商城提示语言.setText("[信息]:显示喜庆物品，双击后可在喜庆物品下添加商品。");
        } else if (c == 3 && d == 2) {
            显示类型.setText("通讯物品");
            商城提示语言.setText("[信息]:显示通讯物品，双击后可在通讯物品下添加商品。");
        } else if (c == 3 && d == 3) {
            显示类型.setText("卷轴");
            商城提示语言.setText("[信息]:显示卷轴，双击后可在卷轴下添加商品。");
        } else if (c == 4 && d == 1) {
            显示类型.setText("会员卡");
            商城提示语言.setText("[信息]:显示会员卡，双击后可在会员卡下添加商品。");
        } else if (c == 4 && d == 2) {
            显示类型.setText("表情");
            商城提示语言.setText("[信息]:显示表情，双击后可在表情下添加商品。");
        } else if (c == 4 && d == 3) {
            显示类型.setText("个人商店");
            商城提示语言.setText("[信息]:显示个人商店，双击后可在个人商店下添加商品。");
        } else if (c == 4 && d == 4) {
            显示类型.setText("效果");
            商城提示语言.setText("[信息]:显示效果，双击后可在效果下添加商品。");
        } else if (c == 4 && d == 5) {
            显示类型.setText("游戏");
            商城提示语言.setText("[信息]:显示游戏，双击后可在游戏下添加商品。");
        } else if (c == 4 && d == 6) {
            显示类型.setText("纪念日");
            商城提示语言.setText("[信息]:显示纪念日，双击后可在纪念日下添加商品。");
        } else if (c == 5 && d == 1) {
            显示类型.setText("宠物");
            商城提示语言.setText("[信息]:显示宠物，双击后可在宠物下添加商品。");
        } else if (c == 5 && d == 2) {
            显示类型.setText("宠物服饰");
            商城提示语言.setText("[信息]:显示宠物服饰，双击后可在宠物服饰下添加商品。");
        } else if (c == 5 && d == 3) {
            显示类型.setText("其他");
            商城提示语言.setText("[信息]:显示其他，双击后可在其他下添加商品。");
        } else {
            显示类型.setText("XXXX");
            商城提示语言.setText("[信息]:XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX。");
        }
    }

    public void 刷新() {
        if ("热销产品".equals(显示类型.getText())) {
            读取商品(10000000, 10100000, 1, 1);
        } else if ("主题馆".equals(显示类型.getText())) {
            读取商品(10100000, 10200000, 1, 2);
        } else if ("活动".equals(显示类型.getText())) {
            读取商品(10200000, 10300000, 1, 3);
        } else if ("帽子".equals(显示类型.getText())) {
            读取商品(20000000, 20100000, 2, 1);
        } else if ("裙裤".equals(显示类型.getText())) {
            读取商品(20500000, 20600000, 2, 2);
        } else if ("披风".equals(显示类型.getText())) {
            读取商品(21100000, 21200000, 2, 3);
        } else if ("飞镖".equals(显示类型.getText())) {
            读取商品(21000000, 21100000, 2, 4);
        } else if ("长袍".equals(显示类型.getText())) {
            读取商品(20300000, 20400000, 2, 5);
        } else if ("脸饰".equals(显示类型.getText())) {
            读取商品(20100000, 20200000, 2, 6);
        } else if ("鞋子".equals(显示类型.getText())) {
            读取商品(20600000, 20700000, 2, 7);
        } else if ("骑宠".equals(显示类型.getText())) {
            读取商品(21200000, 21300000, 2, 8);
        } else if ("戒指".equals(显示类型.getText())) {
            读取商品(20900000, 21000000, 2, 9);
        } else if ("眼饰".equals(显示类型.getText())) {
            读取商品(20200000, 20300000, 2, 10);
        } else if ("手套".equals(显示类型.getText())) {
            读取商品(20700000, 20800000, 2, 11);
        } else if ("武器".equals(显示类型.getText())) {
            读取商品(20800000, 20900000, 2, 12);
        } else if ("上衣".equals(显示类型.getText())) {
            读取商品(20400000, 20500000, 2, 13);
        } else if ("喜庆物品".equals(显示类型.getText())) {
            读取商品(30000000, 30100000, 3, 1);
        } else if ("通讯物品".equals(显示类型.getText())) {
            读取商品(30100000, 30200000, 3, 2);
        } else if ("卷轴".equals(显示类型.getText())) {
            读取商品(30200000, 30300000, 3, 3);
        } else if ("会员卡".equals(显示类型.getText())) {
            读取商品(50000000, 50100000, 4, 1);
        } else if ("表情".equals(显示类型.getText())) {
            读取商品(50100000, 50200000, 4, 2);
        } else if ("个人商店".equals(显示类型.getText())) {
            读取商品(50200000, 50300000, 4, 3);
        } else if ("效果".equals(显示类型.getText())) {
            读取商品(50500000, 50600000, 4, 4);
        } else if ("纪念日".equals(显示类型.getText())) {
            读取商品(50300000, 50400000, 4, 6);
        } else if ("游戏".equals(显示类型.getText())) {
            读取商品(50400000, 50500000, 4, 5);
        } else if ("宠物".equals(显示类型.getText())) {
            读取商品(60000000, 60100000, 5, 1);
        } else if ("宠物服饰".equals(显示类型.getText())) {
            读取商品(60100000, 60200000, 5, 2);
        } else if ("其他".equals(显示类型.getText())) {
            读取商品(60200000, 60300000, 5, 3);
        } else if ("".equals(显示类型.getText())) {
            initCharacterPannel();
        }
    }

    public void initCharacterPannel() {
        long start = System.currentTimeMillis();
        for (int i = ((DefaultTableModel) (this.charTable.getModel())).getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) (this.charTable.getModel())).removeRow(i);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            PreparedStatement pse;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM cashshop_modified_items ");//WHERE serial > 10000000 && serial < 10100000

            rs = ps.executeQuery();
            while (rs.next()) {
                String itemName = "";
                itemName = MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"));
                String 上架状态 = "";
                if (rs.getInt("showup") == 0) {
                    上架状态 = "已经下架↓";
                } else {
                    上架状态 = "已经上架↑";
                }
                String 出售状态2 = "";
                switch (rs.getInt("mark")) {
                    case -1:
                        出售状态2 = "无";
                        break;
                    case 0:
                        出售状态2 = "NEW";
                        break;
                    case 1:
                        出售状态2 = "Sale";
                        break;
                    case 2:
                        出售状态2 = "HOT";
                        break;
                    case 3:
                        出售状态2 = "Event";
                        break;
                    default:
                        break;
                }
                ((DefaultTableModel) charTable.getModel()).insertRow(charTable.getRowCount(), new Object[]{
                    rs.getInt("serial"),
                    rs.getInt("itemid"),
                    "非详细分类不显示名称",
                    //itemName,
                    rs.getInt("count"),
                    rs.getInt("discount_price"),
                    rs.getInt("period"),
                    出售状态2,
                    上架状态,
                    NPCConversationManager.SN取出售(rs.getInt("serial")),
                    NPCConversationManager.SN取库存(rs.getInt("serial")),
                    NPCConversationManager.SN取折扣(rs.getInt("serial")),
                    NPCConversationManager.SN取限购(rs.getInt("serial"))
                });
            }
            long now = System.currentTimeMillis() - start;

        } catch (SQLException ex) {
            Logger.getLogger(ZEVMS2.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        charTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = charTable.getSelectedRow();
                String a1 = charTable.getValueAt(i, 0).toString();
                String a2 = charTable.getValueAt(i, 1).toString();
                String a3 = charTable.getValueAt(i, 3).toString();
                String a4 = charTable.getValueAt(i, 4).toString();
                String a5 = charTable.getValueAt(i, 5).toString();
                String a6 = charTable.getValueAt(i, 6).toString();
                String a7 = charTable.getValueAt(i, 7).toString();
                String a8 = charTable.getValueAt(i, 8).toString();
                String a9 = charTable.getValueAt(i, 9).toString();
                String a10 = charTable.getValueAt(i, 10).toString();
                String a11 = charTable.getValueAt(i, 11).toString();

                商品编码.setText(a1);
                商品代码.setText(a2);
                商品数量.setText(a3);
                商品价格.setText(a4);
                商品时间.setText(a5);
                商品库存.setText(a9);
                商品折扣.setText(a10);
                每日限购.setText(a11);

                if (null != charTable.getValueAt(i, 6).toString()) {
                    switch (charTable.getValueAt(i, 6).toString()) {
                        case "无":
                            商品出售状态.setText("-1");
                            break;
                        case "NEW":
                            商品出售状态.setText("0");
                            break;
                        case "Sale":
                            商品出售状态.setText("1");
                            break;
                        case "HOT":
                            商品出售状态.setText("2");
                            break;
                        case "Event":
                            商品出售状态.setText("3");
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    public static void main(String args[]) {
        ZEVMS2.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
        } catch (Exception e) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ZEVMS2().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField GM;
    private javax.swing.JButton HOT;
    private javax.swing.JTextField HP;
    private javax.swing.JTextField MP;
    private javax.swing.JButton NEW;
    private javax.swing.JPanel PVP技能;
    private javax.swing.JTable PVP技能设置;
    private javax.swing.JTextField QQ;
    private javax.swing.JButton Sale;
    private javax.swing.JTextField a1;
    private javax.swing.JTable charTable;
    private javax.swing.JButton event;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel182;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel184;
    private javax.swing.JLabel jLabel185;
    private javax.swing.JLabel jLabel186;
    private javax.swing.JLabel jLabel187;
    private javax.swing.JLabel jLabel188;
    private javax.swing.JLabel jLabel189;
    private javax.swing.JLabel jLabel190;
    private javax.swing.JLabel jLabel191;
    private javax.swing.JLabel jLabel192;
    private javax.swing.JLabel jLabel193;
    private javax.swing.JLabel jLabel194;
    private javax.swing.JLabel jLabel195;
    private javax.swing.JLabel jLabel196;
    private javax.swing.JLabel jLabel197;
    private javax.swing.JLabel jLabel198;
    private javax.swing.JLabel jLabel199;
    private javax.swing.JLabel jLabel200;
    private javax.swing.JLabel jLabel201;
    private javax.swing.JLabel jLabel203;
    private javax.swing.JLabel jLabel204;
    private javax.swing.JLabel jLabel205;
    private javax.swing.JLabel jLabel206;
    private javax.swing.JLabel jLabel214;
    private javax.swing.JLabel jLabel217;
    private javax.swing.JLabel jLabel218;
    private javax.swing.JLabel jLabel219;
    private javax.swing.JLabel jLabel220;
    private javax.swing.JLabel jLabel221;
    private javax.swing.JLabel jLabel222;
    private javax.swing.JLabel jLabel223;
    private javax.swing.JLabel jLabel224;
    private javax.swing.JLabel jLabel225;
    private javax.swing.JLabel jLabel226;
    private javax.swing.JLabel jLabel227;
    private javax.swing.JLabel jLabel228;
    private javax.swing.JLabel jLabel229;
    private javax.swing.JLabel jLabel230;
    private javax.swing.JLabel jLabel231;
    private javax.swing.JLabel jLabel232;
    private javax.swing.JLabel jLabel233;
    private javax.swing.JLabel jLabel234;
    private javax.swing.JLabel jLabel235;
    private javax.swing.JLabel jLabel236;
    private javax.swing.JLabel jLabel240;
    private javax.swing.JLabel jLabel241;
    private javax.swing.JLabel jLabel242;
    private javax.swing.JLabel jLabel244;
    private javax.swing.JLabel jLabel246;
    private javax.swing.JLabel jLabel259;
    private javax.swing.JLabel jLabel268;
    private javax.swing.JLabel jLabel269;
    private javax.swing.JLabel jLabel270;
    private javax.swing.JLabel jLabel271;
    private javax.swing.JLabel jLabel272;
    private javax.swing.JLabel jLabel276;
    private javax.swing.JLabel jLabel283;
    private javax.swing.JLabel jLabel287;
    private javax.swing.JLabel jLabel288;
    private javax.swing.JLabel jLabel289;
    private javax.swing.JLabel jLabel29;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel300;
    private javax.swing.JLabel jLabel301;
    private javax.swing.JLabel jLabel302;
    private javax.swing.JLabel jLabel303;
    private javax.swing.JLabel jLabel304;
    private javax.swing.JLabel jLabel305;
    private javax.swing.JLabel jLabel306;
    private javax.swing.JLabel jLabel307;
    private javax.swing.JLabel jLabel308;
    private javax.swing.JLabel jLabel309;
    private javax.swing.JLabel jLabel310;
    private javax.swing.JLabel jLabel311;
    private javax.swing.JLabel jLabel312;
    private javax.swing.JLabel jLabel313;
    private javax.swing.JLabel jLabel314;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel322;
    private javax.swing.JLabel jLabel323;
    private javax.swing.JLabel jLabel324;
    private javax.swing.JLabel jLabel325;
    private javax.swing.JLabel jLabel326;
    private javax.swing.JLabel jLabel327;
    private javax.swing.JLabel jLabel328;
    private javax.swing.JLabel jLabel329;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel353;
    private javax.swing.JLabel jLabel354;
    private javax.swing.JLabel jLabel355;
    private javax.swing.JLabel jLabel356;
    private javax.swing.JLabel jLabel357;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane132;
    private javax.swing.JScrollPane jScrollPane134;
    private javax.swing.JScrollPane jScrollPane135;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JTabbedPane jTabbedPane8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton z1;
    private javax.swing.JButton z2;
    private javax.swing.JButton z3;
    private javax.swing.JButton z4;
    private javax.swing.JButton z5;
    private javax.swing.JButton z6;
    private javax.swing.JPanel 上次启动;
    private javax.swing.JLabel 上次启动时间2;
    private javax.swing.JLabel 上次启动时间3;
    private javax.swing.JButton 上衣;
    private javax.swing.JTextField 个人发送物品代码;
    private javax.swing.JTextField 个人发送物品数量;
    private javax.swing.JTextField 个人发送物品玩家名字;
    private javax.swing.JButton 个人商店;
    private javax.swing.JButton 主题馆;
    private javax.swing.JLabel 人数限制;
    private javax.swing.JButton 会员卡;
    private javax.swing.JButton 保存数据;
    private javax.swing.JButton 保存数据1;
    private javax.swing.JButton 保存雇佣;
    private javax.swing.JButton 保存雇佣1;
    private javax.swing.JButton 修改;
    private javax.swing.JButton 修改1;
    private javax.swing.JButton 修改技能;
    private javax.swing.JButton 修改技能1;
    private javax.swing.JButton 修改背包扩充价格;
    private javax.swing.JButton 修改记录;
    private javax.swing.JButton 修改账号点券抵用;
    private javax.swing.JButton 充值卡后台;
    private javax.swing.JButton 充值卡后台1;
    private javax.swing.JTextField 全服发送物品代码;
    private javax.swing.JTextField 全服发送物品数量;
    private javax.swing.JTextField 全服发送装备物品ID;
    private javax.swing.JTextField 全服发送装备装备HP;
    private javax.swing.JTextField 全服发送装备装备MP;
    private javax.swing.JTextField 全服发送装备装备制作人;
    private javax.swing.JTextField 全服发送装备装备力量;
    private javax.swing.JTextField 全服发送装备装备加卷;
    private javax.swing.JTextField 全服发送装备装备可否交易;
    private javax.swing.JTextField 全服发送装备装备攻击力;
    private javax.swing.JTextField 全服发送装备装备敏捷;
    private javax.swing.JTextField 全服发送装备装备智力;
    private javax.swing.JTextField 全服发送装备装备物理防御;
    private javax.swing.JTextField 全服发送装备装备给予时间;
    private javax.swing.JTextField 全服发送装备装备运气;
    private javax.swing.JTextField 全服发送装备装备魔法力;
    private javax.swing.JTextField 全服发送装备装备魔法防御;
    private javax.swing.JTextField 公告发布喇叭代码;
    private javax.swing.JTextField 公告填写;
    private javax.swing.JLabel 公告提示语言;
    private javax.swing.JButton 关闭服务端;
    private javax.swing.JButton 关闭服务端1;
    private javax.swing.JButton 其他;
    private javax.swing.JTextField 其他背包物品代码;
    private javax.swing.JTextField 其他背包物品名字;
    private javax.swing.JTextField 其他背包物品序号;
    private javax.swing.JPanel 其他设置;
    private javax.swing.JButton 删除个人;
    private javax.swing.JButton 删除其他背包;
    private javax.swing.JButton 删除商城仓库;
    private javax.swing.JButton 删除技能;
    private javax.swing.JButton 删除拍卖行;
    private javax.swing.JButton 删除拍卖行1;
    private javax.swing.JButton 删除消耗背包;
    private javax.swing.JButton 删除游戏仓库;
    private javax.swing.JButton 删除特殊背包;
    private javax.swing.JButton 删除穿戴装备;
    private javax.swing.JButton 删除装备背包;
    private javax.swing.JButton 删除角色;
    private javax.swing.JButton 删除设置背包;
    private javax.swing.JButton 删除账号;
    private javax.swing.JButton 删除通用;
    private javax.swing.JButton 刷新PVP列表;
    private javax.swing.JButton 刷新个人;
    private javax.swing.JButton 刷新家族信息;
    private javax.swing.JButton 刷新模式;
    private javax.swing.JButton 刷新角色信息;
    private javax.swing.JButton 刷新账号信息;
    private javax.swing.JButton 刷新通用;
    private javax.swing.JButton 刷新金币拍卖行;
    private javax.swing.JButton 刷新金币拍卖行1;
    private javax.swing.JLabel 剩余时间;
    private javax.swing.JButton 副本控制台;
    private javax.swing.JButton 副本控制台1;
    private javax.swing.JTextField 力量;
    private javax.swing.JButton 卡号自救1;
    private javax.swing.JButton 卡号自救2;
    private javax.swing.JButton 卡家族解救;
    private javax.swing.JButton 卷轴;
    private javax.swing.JTextField 发型;
    private javax.swing.JPanel 发布公告;
    private javax.swing.JPanel 发送福利;
    private javax.swing.JTextField 发送装备玩家姓名;
    private javax.swing.JLabel 启动账号;
    private javax.swing.JLabel 启动账号1;
    private javax.swing.JLabel 启动账号3;
    private javax.swing.JTextField 商品代码;
    private javax.swing.JTextField 商品价格;
    private javax.swing.JTextField 商品出售状态;
    private javax.swing.JTextField 商品库存;
    private javax.swing.JTextField 商品折扣;
    private javax.swing.JTextField 商品数量;
    private javax.swing.JTextField 商品时间;
    private javax.swing.JTextField 商品编码;
    private javax.swing.JPanel 商品设置;
    private javax.swing.JTextField 商城仓库物品代码;
    private javax.swing.JTextField 商城仓库物品名字;
    private javax.swing.JTextField 商城仓库物品序号;
    private javax.swing.JTable 商城扩充价格;
    private javax.swing.JTextField 商城扩充价格修改;
    private javax.swing.JLabel 商城提示语言;
    private javax.swing.JLabel 啊啊啊3;
    private javax.swing.JButton 喜庆物品;
    private javax.swing.JTable 在线泡点设置;
    private javax.swing.JButton 在线角色;
    private javax.swing.JButton 在线账号;
    private javax.swing.JTextField 地图;
    private javax.swing.JPasswordField 天谴1级码;
    private javax.swing.JTable 存储发送物品;
    private javax.swing.JButton 宠物;
    private javax.swing.JButton 宠物服饰;
    private javax.swing.JTextField 家族GP;
    private javax.swing.JTextField 家族ID;
    private javax.swing.JTable 家族信息;
    private javax.swing.JTextField 家族名称;
    private javax.swing.JTextField 家族成员1;
    private javax.swing.JTextField 家族成员2;
    private javax.swing.JTextField 家族成员3;
    private javax.swing.JTextField 家族成员4;
    private javax.swing.JTextField 家族成员5;
    private javax.swing.JTextField 家族族长;
    private javax.swing.JButton 封锁账号;
    private javax.swing.JButton 已封账号;
    private javax.swing.JButton 帽子;
    private javax.swing.JButton 应用重载;
    private javax.swing.JButton 应用重载1;
    private javax.swing.JButton 快捷面板;
    private javax.swing.JButton 戒指;
    private javax.swing.JButton 手套;
    private javax.swing.JPanel 技能;
    private javax.swing.JTextField 技能PVPID;
    private javax.swing.JTextField 技能PVP伤害;
    private javax.swing.JTextField 技能PVP编号;
    private javax.swing.JTextField 技能代码;
    private javax.swing.JTable 技能信息;
    private javax.swing.JTextField 技能名字;
    private javax.swing.JTextField 技能序号;
    private javax.swing.JTextField 技能最高等级;
    private javax.swing.JTextField 技能目前等级;
    private javax.swing.JButton 披风;
    private javax.swing.JTextField 抵用;
    private javax.swing.JPanel 拍卖行;
    private javax.swing.JTextField 拍卖行物品代码;
    private javax.swing.JTextField 拍卖行物品代码1;
    private javax.swing.JTextField 拍卖行物品名字;
    private javax.swing.JTextField 拍卖行物品名字1;
    private javax.swing.JPanel 指令;
    private javax.swing.JButton 控制台1号;
    private javax.swing.JButton 控制台1号1;
    private javax.swing.JButton 控制台2号;
    private javax.swing.JButton 控制台2号1;
    private javax.swing.JButton 搜索1;
    private javax.swing.JButton 效果;
    private javax.swing.JTextField 敏捷;
    private javax.swing.JButton 新增1;
    private javax.swing.JButton 新增个人;
    private javax.swing.JButton 新增通用;
    private javax.swing.JButton 无出售状态;
    private javax.swing.JLabel 显示在线玩家;
    private javax.swing.JLabel 显示在线账号;
    private javax.swing.JButton 显示管理角色;
    private javax.swing.JTextField 显示类型;
    private javax.swing.JTextField 智力;
    private javax.swing.JButton 更多应用;
    private javax.swing.JButton 更多应用1;
    private javax.swing.JPanel 更新;
    private javax.swing.JTable 更新记录;
    private javax.swing.JTextPane 更新记录详细;
    private javax.swing.JButton 查看技能;
    private javax.swing.JButton 查看背包;
    private javax.swing.JButton 查询;
    private javax.swing.JLabel 标题;
    private javax.swing.JButton 武器;
    private javax.swing.JTextField 每日限购;
    private javax.swing.JTextField 泡点值;
    private javax.swing.JTextField 泡点值1;
    private javax.swing.JButton 泡点值修改;
    private javax.swing.JButton 泡点值修改1;
    private javax.swing.JTextField 泡点序号;
    private javax.swing.JTextField 泡点序号1;
    private javax.swing.JButton 泡点抵用开关;
    private javax.swing.JButton 泡点点券开关;
    private javax.swing.JTextField 泡点类型;
    private javax.swing.JTextField 泡点类型1;
    private javax.swing.JButton 泡点经验开关;
    private javax.swing.JButton 泡点金币开关;
    private javax.swing.JTextField 注册的密码;
    private javax.swing.JTextField 注册的账号;
    private javax.swing.JButton 活动;
    private javax.swing.JButton 活动控制台;
    private javax.swing.JButton 活动控制台1;
    private javax.swing.JTextField 消耗背包物品代码;
    private javax.swing.JTextField 消耗背包物品名字;
    private javax.swing.JTextField 消耗背包物品序号;
    private javax.swing.JButton 添加;
    private javax.swing.JButton 清空QQ相关;
    private javax.swing.JButton 游戏;
    private javax.swing.JTextField 游戏仓库物品代码;
    private javax.swing.JTextField 游戏仓库物品名字;
    private javax.swing.JTextField 游戏仓库物品序号;
    private javax.swing.JLabel 游戏名字;
    private javax.swing.JTextField 点券;
    private javax.swing.JTable 点券拍卖行;
    private javax.swing.JTextField 特殊背包物品代码;
    private javax.swing.JTextField 特殊背包物品名字;
    private javax.swing.JTextField 特殊背包物品序号;
    private javax.swing.JButton 眼饰;
    private javax.swing.JTextField 福利序号;
    private javax.swing.JLabel 福利提示语言;
    private javax.swing.JLabel 福利提示语言2;
    private javax.swing.JPanel 福利记录符号;
    private javax.swing.JButton 离线角色;
    private javax.swing.JButton 离线账号;
    private javax.swing.JTextField 等级;
    private javax.swing.JTextField 管理1;
    private javax.swing.JButton 纪念日;
    private javax.swing.JButton 给予物品;
    private javax.swing.JButton 给予物品1;
    private javax.swing.JButton 给予装备1;
    private javax.swing.JButton 给予装备2;
    private javax.swing.JTextField 背包物品代码1;
    private javax.swing.JTextField 背包物品名字1;
    private javax.swing.JButton 脚本编辑器;
    private javax.swing.JTextField 脸型;
    private javax.swing.JButton 脸饰;
    private javax.swing.JButton 表情;
    private javax.swing.JTextField 装备背包物品代码;
    private javax.swing.JTextField 装备背包物品名字;
    private javax.swing.JTextField 装备背包物品序号;
    private javax.swing.JButton 裙裤;
    private javax.swing.JTextField 角色ID;
    private javax.swing.JTable 角色信息;
    private javax.swing.JPanel 角色信息1;
    private javax.swing.JTable 角色其他背包;
    private javax.swing.JPanel 角色列表;
    private javax.swing.JTable 角色商城仓库;
    private javax.swing.JLabel 角色提示语言;
    private javax.swing.JTextField 角色昵称;
    private javax.swing.JTable 角色消耗背包;
    private javax.swing.JTable 角色游戏仓库;
    private javax.swing.JTable 角色点券拍卖行;
    private javax.swing.JTextField 角色点券拍卖行序号;
    private javax.swing.JTable 角色特殊背包;
    private javax.swing.JPanel 角色背包;
    private javax.swing.JTable 角色背包穿戴;
    private javax.swing.JTable 角色装备背包;
    private javax.swing.JTable 角色设置背包;
    private javax.swing.JTable 角色金币拍卖行;
    private javax.swing.JTextField 角色金币拍卖行序号;
    private javax.swing.JButton 解卡;
    private javax.swing.JButton 解封;
    private javax.swing.JTextField 设置背包物品代码;
    private javax.swing.JTextField 设置背包物品名字;
    private javax.swing.JTextField 设置背包物品序号;
    private javax.swing.JButton 读取热销产品;
    private javax.swing.JTextField 账号;
    private javax.swing.JTextField 账号ID;
    private javax.swing.JTable 账号信息;
    private javax.swing.JPanel 账号列表;
    private javax.swing.JLabel 账号提示语言;
    private javax.swing.JTextField 账号操作;
    private javax.swing.JTextField 货币类型;
    private javax.swing.JTextField 身上穿戴序号1;
    private javax.swing.JTextField 运气;
    private javax.swing.JButton 通讯物品;
    private javax.swing.JTextField 金币;
    private javax.swing.JTable 金币拍卖行;
    private javax.swing.JButton 长袍;
    private javax.swing.JTable 雇佣在线泡点设置;
    private javax.swing.JPanel 雇佣角色ID;
    private javax.swing.JTextField 雇佣角色ID2;
    private javax.swing.JButton 鞋子;
    private javax.swing.JButton 飞镖;
    private javax.swing.JPanel 首页;
    private javax.swing.JPanel 首页1;
    private javax.swing.JLabel 首页LOGO;
    private javax.swing.JButton 骑宠;
    // End of variables declaration//GEN-END:variables

}
