package gui;

import static a.本地数据库.任务更新下载目录;

import static a.本地数据库.任务更新导入目录;
import static a.本地数据库.任务更新解压目录;
import static a.用法大全.关系验证;
import static abc.Game.数据库数据导入;
import static abc.Game.版本;
import static abc.sancu.FileDemo_05.删除文件;

import client.Class2;
import client.DebugWindow;
import client.MapleCharacter;
import client.SkillFactory;
import handling.MapleServerHandler;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.login.LoginServer;
import handling.cashshop.CashShopServer;
import handling.login.LoginInformationProvider;
import handling.world.World;

import java.sql.SQLException;

import database.DatabaseConnection;
import gui.Jieya.解压文件;

import static gui.ZEVMS.下载文件;

import handling.world.MapleParty;
import handling.world.family.MapleFamilyBuff;

import java.io.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import gui.控制台.控制台1号;
import gui.未分类.广告;
import gui.控制台.脚本更新器;
import gui.控制台.聊天记录显示;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import server.Timer.*;
import server.custom.forum.Forum_Section;
import server.events.MapleOxQuizFactory;
import server.life.MapleLifeFactory;
import server.maps.MapleMapFactory;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import server.AutobanManager;
import server.CashItemFactory;
import server.ItemMakerFactory;
import server.MTSStorage;
import server.MapleCarnivalFactory;
import server.MapleItemInformationProvider;
import server.PredictCardFactory;
import server.RandomRewards;
import server.ServerProperties;
import server.ShutdownServer;
import server.SpeedRunner;
import server.Timer;
import server.custom.bossrank3.BossRankManager3;
import tools.packet.UIPacket;

import static gui.QQ通信.通信;
import static a.用法大全.本地取广播;
import static abc.Game.主城;
import static gui.QQ通信.OX答题;
import static gui.QQ通信.群通知;
import static gui.活动野外通缉.随机通缉;
import static gui.进阶BOSS.进阶BOSS线程.开启进阶BOSS线程;

import java.util.Scanner;

import static server.MapleCarnivalChallenge.getJobNameById;
import static tools.FileoutputUtil.CurrentReadable_Time;

public class Start {

    public static boolean Check = true;
    public static final Start instance = new Start();
    private static int maxUsers = 0;
    public static ZEVMS2 CashGui;
    public static 控制台1号 服务端功能开关;
    public static 广告 广告;
    public static 聊天记录显示 信息输出;
    public static 脚本更新器 更新通知;
    public static DebugWindow DebugWindow;
    public static Map<String, Integer> ConfigValuesMap = new HashMap<>();
    public static Map<String, Integer> 地图吸怪检测 = new HashMap<>();
    public static Map<String, Integer> 技能范围检测 = new HashMap<>();
    public static Map<String, Integer> PVP技能伤害 = new HashMap<>();
    public static Map<String, Integer> 个人信息设置 = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        startServer();
    }

    public static void startServer() throws InterruptedException, SocketException, UnsupportedEncodingException, IOException {
        MapleParty.每日清理 = 0;
        long start = System.currentTimeMillis();
        数据库数据导入();
        GetConfigValues();
        Check = false;
        System.out.println("\r\n○ 开始启动服务端");
        // MapleParty.IP地址 = ServerProperties.getProperty("ZEV.IP");
        //  MapleParty.开服名字 = ServerProperties.getProperty("ZEV.name");
        System.out.println("○ 游戏名字 : " + MapleParty.开服名字);
        System.out.println("○ 游戏地址 : " + MapleParty.IP地址);

        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET loggedin = 0")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET lastGainHM = 0")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 202")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 201")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 200")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 500")) {
                ps.executeUpdate();
            }
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 501")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("[重置数据值出错] Please check if the SQL server is active.");
        }
        System.out.println("○ 开始加载各项数据");
        World.init();
        System.out.println("○ 开始加载时间线程");
        时间线程();
        System.out.println("○ 开始游戏商城");
        LoginInformationProvider.getInstance();
        System.out.println("○ 开始加载任务信息1");
        MapleQuest.initQuests();
        System.out.println("○ 开始加载任务信息2");
        MapleLifeFactory.loadQuestCounts();
        System.out.println("○ 开始加载背包物品1");
        ItemMakerFactory.getInstance();
        System.out.println("○ 开始加载背包物品2");
        MapleItemInformationProvider.getInstance().load();
        System.out.println("○ 开始加载刷怪线程");
        World.registerRespawn();
        System.out.println("○ 开始加载备用刷怪线程");
        World.registerRespawn2();
        System.out.println("○ 开始加载反应堆1");
        System.out.println("○ 开始加载反应堆2");
        RandomRewards.getInstance();
        System.out.println("○ 开始加载技能信息");
        SkillFactory.getSkill(99999999);
        System.out.println("○ 开始加载活动信息");
        MapleOxQuizFactory.getInstance().initialize();
        MapleCarnivalFactory.getInstance();
        System.out.println("○ 开始加载家族信息");
        MapleGuildRanking.getInstance().RankingUpdate();
        System.out.println("○ 开始加载学院信息");
        MapleFamilyBuff.getBuffEntry();
        new Thread(QQMsgServer.getInstance()).start();
        System.out.println("○ 开始加载装备附魔信息");
        GetFuMoInfo();
        System.out.println("○ 开始加载云防护黑名单");
        GetCloudBacklist();
        System.out.println("○ 开始加载技能范围检测");
        读取技能范围检测();
        System.out.println("○ 开始加载地图吸怪检测");
        读取地图吸怪检测();
        System.out.println("○ 开始加载PVP技能配置");
        读取技能PVP伤害();
        System.out.println("○ 开始加载个人配置信息");
        读取技个人信息设置();
        重置仙人数据();
        System.out.println("○ 开始加载游戏论坛");
        System.out.println("○ 开始加载游戏邮箱");
        System.out.println("○ 开始加载传送点数据");
        Forum_Section.loadAllSection();
        System.out.println("○ 开始加载登陆服务器");
        //MapleServerHandler.registerMBean();
        MTSStorage.load();
        PredictCardFactory.getInstance().initialize();
        CashItemFactory.getInstance().initialize();
        System.out.println("○ 开始加载游戏倍率信息");
        LoginServer.run_startup_configurations();
        //怪物攻城
        //RespawnManager.getInstance().run();
        System.out.println("○ 开始加载游戏频道信息");
        ChannelServer.startChannel_Main();
        LoginServer.setOn();
        System.out.println("○ 开始加载新建NPC位置信息");
        MapleMapFactory.loadCustomLife();
        System.out.println("○ 开始加载游戏商城物品");
        CashShopServer.run_startup_configurations();
        CheatTimer.getInstance().register(AutobanManager.getInstance(), 60000);
        //new ZevmsLauncherServer(60000).start();
        //好像是掉落物品？
        //ChannelServer.getInstance(1).getMapFactory().getMap(749030000).spawnRandDrop();
        //WorldTimer.getInstance().register(new World.Respawn(), 10000);
        Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
        try {
            SpeedRunner.getInstance().loadSpeedRuns();
        } catch (SQLException e) {
            System.out.println("SpeedRunner错误:" + e);
        }
        WorldTimer.getInstance().register(DatabaseConnection.CloseSQLConnections, 10 * 60 * 1000);//18
        System.out.println("○ 商城: " + CurrentReadable_Time() + " 读取商城上架时装完成");
        //游戏倍率显示
        System.out.println("○ 游戏倍率信息");
        //显示经验倍率，不能超过100倍
        if (Integer.parseInt(ServerProperties.getProperty("ZEV.Exp", "1")) > 100) {
            System.out.println("○ 游戏经验倍率: 100 倍 (上限)");
        } else {
            System.out.println("○ 游戏经验倍率: " + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp", "1")) + " 倍 ");
        }
        //显示物品倍率，不能超过100倍
        if (Integer.parseInt(ServerProperties.getProperty("ZEV.Drop", "1")) > 100) {
            System.out.println("○ 游戏物品倍率：100 倍 (上限)");
        } else {
            System.out.println("○ 游戏物品倍率：" + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop", "1")) + " 倍 ");
        }
        //显示金币倍率，不能超过100倍
        if (Integer.parseInt(ServerProperties.getProperty("ZEV.Meso", "1")) > 100) {
            System.out.println("○ 游戏金币倍率：100 倍 (上限)");
        } else {
            System.out.println("○ 游戏金币倍率：" + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso", "1")) + " 倍 ");
        }
        System.out.println("○ 启动自动存档线程");
        自动存档(5);
        System.out.println("○ 启动角色福利泡点线程");
        福利泡点(30);
        System.out.println("○ 启动雇佣福利泡点线程");
        福利泡点2(30);
        System.out.println("○ 启动数据库通信线程");
        定时查询(3);
        System.out.println("○ 启动服务端内存回收线程");
        //回收内存(30);
        System.out.println("○ 启动记录在线时长线程");
        System.out.println("○ 启动记录在线时长补救线程");
        记录在线时间(1);
        //回收地图(60 * 3);
        System.out.println("○ 启动游戏公告播放线程");
        公告(20);
        MapleParty.服务端运行时长 = 0;
        System.out.println("○ 启动云端验证IP地址线程");
        统计在线人数(22);
        检测服务端版本(60);
        System.out.println("○ 同步云端任务修复文件夹");
        任务同步修复();
        System.out.println("\r\n");
        System.out.println("○ [加载账号数量]: " + 服务器账号() + " 个");
        System.out.println("○ [加载角色数量]: " + 服务器角色() + " 个");
        System.out.println("○ [加载道具数量]: " + 服务器道具() + " 个");
        System.out.println("○ [加载技能数量]: " + 服务器技能() + " 个");
        System.out.println("○ [加载游戏商品数量]: " + 服务器游戏商品() + " 个");
        System.out.println("○ [加载商城商品数量]: " + 服务器商城商品() + " 个");
        //显示加载完成的时间
        long now = System.currentTimeMillis() - start;
        long seconds = now / 1000;
        System.out.println("○ 服务端启动耗时: " + seconds + " 秒");
        System.out.println("\r\n");
        System.out.println("○ 正在启动主控制台");
        CashShopServer();
        //开启进阶BOSS线程();
        qingkongguyong();

    }

    public static void qingkongguyong() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM hiredmerch");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                雇佣写入(rs.getInt("characterid"), 1, rs.getInt("Mesos"));
            }
            ps.close();
        } catch (SQLException ex) {
        }

        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM hiredmerchantshop");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("hiredmerchantshop " + e);
        }
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM hiredmerchequipment");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("hiredmerchequipment " + e);
        }
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM hiredmerchitems");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("hiredmerchitems " + e);
        }
    }

    /**
     * * <1分钟执行一次>
     */
    private static int 记录在线时间 = 0;
    private static int 重置数据库 = 0;
    private static Boolean 每日送货 = false;
    private static Boolean 喜从天降 = false;
    private static Boolean 鱼来鱼往 = false;
    private static int 初始通缉令 = 0;
    private static Boolean 倍率活动 = false;
    private static Boolean 幸运职业 = false;
    private static Boolean 启动OX答题线程 = false;
    private static Boolean 魔族入侵 = false;
    private static Boolean isClearBossLog = false;
    private static Boolean 魔族攻城 = false;
    private static int Z = 0;

    public static void 记录在线时间(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (记录在线时间 > 0) {
                                                            MapleParty.服务端运行时长 += 1;
                                                            Calendar calendar = Calendar.getInstance();
                                                            int 时 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                                                            int 分 = Calendar.getInstance().get(Calendar.MINUTE);
                                                            int 星期 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                                                            /**
                                                             * <凌晨清理每日信息>
                                                             */
                                                            if (时 == 0 && isClearBossLog == false) {
                                                                System.err.println("[服务端]" + CurrentReadable_Time() + " : ------------------------------");
                                                                System.err.println("[服务端]" + CurrentReadable_Time() + " : 服务端开始清理每日信息 √");
                                                                通信("服务端开始清理每日信息");
                                                                try {
                                                                    //重置今日在线时间
                                                                    try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET todayOnlineTime = 0")) {
                                                                        ps.executeUpdate();
                                                                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 清理今日在线时间完成 √");
                                                                    }
                                                                    //重置每日bosslog信息
                                                                    try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE bosslog SET characterid = 0")) {
                                                                        ps.executeUpdate();
                                                                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 清理今日log信息完成 √");
                                                                    }
                                                                    //重置每日bosslog信息
                                                                    try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET df_tired_point = 0")) {
                                                                        ps.executeUpdate();
                                                                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 清理今日疲劳完成 √");
                                                                    }
                                                                    System.err.println("[服务端]" + CurrentReadable_Time() + " : 服务端清理每日信息完成 √");
                                                                    通信("服务端清理每日信息完成");
                                                                    System.err.println("[服务端]" + CurrentReadable_Time() + " : ------------------------------");
                                                                } catch (SQLException ex) {
                                                                    System.err.println("[服务端]" + CurrentReadable_Time() + " : 服务端处理每日数据出错 × " + ex.getMessage());
                                                                    通信("服务端处理每日数据出错，你可能要手动清理一下。");
                                                                    System.err.println("[服务端]" + CurrentReadable_Time() + " : ------------------------------");
                                                                }
                                                                isClearBossLog = true;
                                                                每日送货 = false;
                                                                启动OX答题线程 = false;
                                                                魔族入侵 = false;
                                                                魔族攻城 = false;
                                                                喜从天降 = false;
                                                                鱼来鱼往 = false;
                                                                MapleParty.OX答题活动 = 0;
                        /*if (重置数据库 == 1) {
                            重置数据库();
                        } else {
                            重置数据库++;
                        }*/
                                                            } else if (时 == 23) {
                                                                isClearBossLog = false;
                                                            }
                                                            /**
                                                             * <鱼来鱼往>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("鱼来鱼往开关") == 0) {
                                                                if (时 == 21 && 分 >= 30 && 鱼来鱼往 == false) {
                                                                    for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                                                                        for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                                                                            mch.startMapEffect("[鱼来鱼往]: 水下世界 2 频道 1 分钟后开始喜从鱼来鱼往活动。", 5120027);
                                                                        }
                                                                    }
                                                                    QQ通信.群通知("[鱼来鱼往]: 水下世界 2 频道 1 分钟后开始喜从鱼来鱼往活动。");
                                                                    new Thread() {
                                                                        @Override
                                                                        public void run() {
                                                                            try {
                                                                                Thread.sleep(1000 * 60);
                                                                                活动鱼来鱼往.鱼来鱼往();
                                                                            } catch (InterruptedException e) {
                                                                            }
                                                                        }
                                                                    }.start();
                                                                    鱼来鱼往 = true;
                                                                }
                                                            }
                                                            /**
                                                             * <喜从天降>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("喜从天降开关") == 0) {
                                                                if (星期 == 1 || 星期 == 7) {
                                                                    if (时 == 22 && 分 == 30 && 喜从天降 == false) {
                                                                        for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                                                                            for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                                                                                mch.startMapEffect("[喜从天降]: 市场 2 频道 1 分钟后开始喜从天降活动。", 5120027);
                                                                            }
                                                                        }
                                                                        QQ通信.群通知("[喜从天降]: 市场 2 频道 1 分钟后开始喜从天降活动。");
                                                                        new Thread() {
                                                                            @Override
                                                                            public void run() {
                                                                                try {
                                                                                    Thread.sleep(1000 * 60);
                                                                                    活动喜从天降.喜从天降();
                                                                                } catch (InterruptedException e) {
                                                                                }
                                                                            }
                                                                        }.start();

                                                                        喜从天降 = true;
                                                                    }
                                                                }
                                                            }
                                                            /**
                                                             * <每日送货>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("每日送货开关") == 0) {
                                                                if (时 == 12 && 每日送货 == false) {
                                                                    for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                                                                        for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                                                                            mch.startMapEffect("[每日送货]: 送货活动开启，明珠港送货员处可以开始送货哦，可以获得丰厚的奖励。", 5120027);
                                                                        }
                                                                    }
                                                                    QQ通信.群通知("[每日送货]: 送货活动开启，明珠港送货员处可以开始送货哦，可以获得丰厚的奖励。");
                                                                    每日送货 = true;
                                                                }
                                                            }
                                                            /**
                                                             * <OX答题>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("OX答题开关") == 0) {
                                                                if (时 == 20 && 启动OX答题线程 == false) {
                                                                    for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                                                                        for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                                                                            mch.startMapEffect("[每日答题]: 10 分钟后将举行 OX 答题比赛，想要参加的小伙伴，请找活动向导参加吧。", 5120027);
                                                                        }
                                                                    }
                                                                    System.err.println("[服务端]" + CurrentReadable_Time() + " : 每日活动 OX答题 线程已经启动");
                                                                    OX答题("[每日活动]: 10 分钟后将举行 OX 答题比赛，想要参加的小伙伴，请找活动向导参加吧。");
                                                                    活动OX答题.OX答题线程();
                                                                    MapleParty.OX答题活动++;
                                                                    启动OX答题线程 = true;
                                                                }
                                                            }
                                                            /**
                                                             * <魔族入侵>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("魔族突袭开关") == 0) {
                                                                if (calendar.get(Calendar.HOUR_OF_DAY) == 22 && 魔族入侵 == false) {
                                                                    活动魔族入侵.魔族入侵线程();
                                                                    魔族入侵 = true;
                                                                }
                                                            }
                                                            /**
                                                             * <魔族攻城>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("魔族攻城开关") == 0) {
                                                                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                                                                    if (时 == 21 && 分 <= 10 && 魔族攻城 == false) {
                                                                        活动魔族攻城.魔族攻城线程();
                                                                        魔族攻城 = true;
                                                                    }
                                                                }
                                                            }
                                                            /**
                                                             * <幸运职业，天选之人-狩猎>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("幸运职业开关") == 0) {
                                                                if (时 == 11 && 幸运职业 == false) {
                                                                    幸运职业();
                                                                    幸运职业 = true;
                                                                } else if (时 == 23 && 幸运职业 == true) {
                                                                    幸运职业();
                                                                    幸运职业 = false;
                                                                } else if (MapleParty.幸运职业 == 0) {
                                                                    幸运职业();
                                                                }
                                                            }
                                                            /**
                                                             * <周末随机倍率活动>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("周末倍率开关") == 0) {
                                                                switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                                                                    case 7:
                                                                        if (时 == 0 && 倍率活动 == false) {
                                                                            活动倍率活动.倍率活动线程();
                                                                            倍率活动 = true;
                                                                        } else if (时 == 23) {
                                                                            倍率活动 = false;
                                                                        }
                                                                        break;
                                                                    case 1:
                                                                        if (时 == 0 && 倍率活动 == false) {
                                                                            活动倍率活动.倍率活动线程();
                                                                            倍率活动 = true;
                                                                        } else if (时 == 23) {
                                                                            倍率活动 = false;
                                                                        }
                                                                        break;
                                                                    case 6:
                                                                        if (倍率活动 == true) {
                                                                            倍率活动 = false;
                                                                        }
                                                                        break;
                                                                    default:
                                                                        break;
                                                                }
                                                            }
                                                            /**
                                                             * <启动神秘商人>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("神秘商人开关") == 0) {
                                                                //第一次启动神秘商人
                                                                if (MapleParty.神秘商人线程 == 0) {
                                                                    活动神秘商人.启动神秘商人();
                                                                    MapleParty.神秘商人线程++;
                                                                }
                                                                //召唤神秘商人
                                                                if (MapleParty.神秘商人线程 > 0) {
                                                                    if (时 == MapleParty.神秘商人时间 && MapleParty.神秘商人 == 0) {
                                                                        活动神秘商人.召唤神秘商人();
                                                                    }
                                                                }

                                                            }
                                                            /**
                                                             * <初始化通缉令>
                                                             */
                                                            if (gui.Start.ConfigValuesMap.get("野外通缉开关") == 0) {
                                                                if (初始通缉令 == 30) {
                                                                    随机通缉();
                                                                    初始通缉令++;
                                                                } else {
                                                                    初始通缉令++;
                                                                }
                                                            }

                                                            /**
                                                             * <记录在线时间>
                                                             */
                                                            Z = 0;
                                                            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                                                                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                                                                    if (chr == null) {
                                                                        continue;
                                                                    }
                                                                    chr.增加角色疲劳值(1);
                                                                    Connection con = DatabaseConnection.getConnection();
                                                                    try {
                                                                        /**
                                                                         * <加在线>
                                                                         */
                                                                        try (PreparedStatement psu = con.prepareStatement("UPDATE characters SET todayOnlineTime = todayOnlineTime + ?, totalOnlineTime = totalOnlineTime + ? WHERE id = ?")) {
                                                                            psu.setInt(1, time);
                                                                            psu.setInt(2, time);
                                                                            psu.setInt(3, chr.getId());
                                                                            psu.executeUpdate();
                                                                            psu.close();
                                                                        }
                                                                        chr.getClient().sendPacket(MaplePacketCreator.enableActions());
                                                                        if (Z == 0) {
                                                                            Z++;
                                                                            System.err.println("[服务端]" + CurrentReadable_Time() + " : 记录在线 √");
                                                                        }
                                                                    } catch (SQLException ex) {
                                                                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 记录在线 × (" + chr.getId() + ")");//+ ex.getMessage()
                                                                        //通信("玩家 [" + 角色ID取名字(chr.getId()) + "] 记录在线错误，现在开始补救。");
                                                                        记录在线时间补救(chr.getId());
                                                                    }

                                                                }
                                                            }
                                                        } else {
                                                            记录在线时间++;
                                                        }
                                                    }
                                                }, 60 * 1000 * time
        );
    }

    public static void 重置数据库() {
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[系统通知] : 为保证服务端流畅，系统即将进行清理缓存，预计 1 分钟清理完毕，过程会导致游戏卡顿，请勿下线。"));
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[系统通知] : 为保证服务端流畅，系统即将进行清理缓存，预计 1 分钟清理完毕，过程会导致游戏卡顿，请勿下线。"));
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[系统通知] : 为保证服务端流畅，系统即将进行清理缓存，预计 1 分钟清理完毕，过程会导致游戏卡顿，请勿下线。"));
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[系统通知] : 为保证服务端流畅，系统即将进行清理缓存，预计 1 分钟清理完毕，过程会导致游戏卡顿，请勿下线。"));
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                if (chr == null) {
                    continue;
                }
                chr.saveToDB(false, false);
            }
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 5);
                    关闭服务("MySQL5");
                } catch (InterruptedException e) {
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 10);
                    重启服务("MySQL5");
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

    public static void 幸运职业() {
        int 随机 = (int) Math.ceil(Math.random() * 18);
        if (随机 == 0) {
            随机 += 1;
        }
        switch (随机) {
            case 1:
                MapleParty.幸运职业 = 111;
                break;
            case 2:
                MapleParty.幸运职业 = 121;
                break;
            case 3:
                MapleParty.幸运职业 = 131;
                break;
            case 4:
                MapleParty.幸运职业 = 211;
                break;
            case 5:
                MapleParty.幸运职业 = 221;
                break;
            case 6:
                MapleParty.幸运职业 = 231;
                break;
            case 7:
                MapleParty.幸运职业 = 311;
                break;
            case 8:
                MapleParty.幸运职业 = 321;
                break;
            case 9:
                MapleParty.幸运职业 = 411;
                break;
            case 10:
                MapleParty.幸运职业 = 421;
                break;
            case 11:
                MapleParty.幸运职业 = 511;
                break;
            case 12:
                MapleParty.幸运职业 = 521;
                break;
            case 13:
                MapleParty.幸运职业 = 1111;
                break;

            case 14:
                MapleParty.幸运职业 = 1211;
                break;
            case 15:
                MapleParty.幸运职业 = 1311;
                break;
            case 16:
                MapleParty.幸运职业 = 1411;
                break;
            case 17:
                MapleParty.幸运职业 = 1511;
                break;
            case 18:
                MapleParty.幸运职业 = 2111;
                break;
            default:

                break;
        }
        String 信息 = "恭喜 " + getJobNameById((MapleParty.幸运职业 - 1)) + " " + getJobNameById(MapleParty.幸运职业) + " " + getJobNameById((MapleParty.幸运职业 + 1)) + " 幸运成为幸运职业，增加50%基础狩猎经验";
        群通知("[幸运职业] : " + 信息);
        System.err.println("[服务端]" + CurrentReadable_Time() + " : [幸运职业] : " + 信息);
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[幸运职业] : " + 信息));

    }

    private static int 定时查询 = 0;
    /**
     * <3分钟检测一次数据库>
     * <每3分钟检测一下数据库通信信息，如果数据异常，通知主人，3分钟后如果还是异常，主人可以选择手动，或者通过指令重启数据库>
     * <机器人指令； *重启数据库>
     */
    public static int 异常警告 = 0;

    public static void 定时查询(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (定时查询 > 0) {
                    try {
                        try (PreparedStatement psu = DatabaseConnection.getConnection().prepareStatement("SELECT COUNT(id) FROM accounts WHERE loggedin > 0")) {
                            psu.execute();
                            psu.close();
                            System.err.println("[服务端]" + CurrentReadable_Time() + " : 检测数据库通信 √");
                            if (异常警告 > 0) {
                                通信("服务端数据库通信已恢复");
                                异常警告 = 0;
                            }
                        }
                    } catch (SQLException ex) {
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 检测数据库通信 × " + ex.getMessage());
                        if (异常警告 >= 5) {
                            通信("服务端数据库通信严重异常，请及时重启数据库");
                        } else {
                            通信("服务端数据库通信异常，如果持续提示此信息，请重启数据库");
                            异常警告++;
                        }
                    }
                } else {
                    定时查询++;
                }
            }
        }, 60 * 1000 * time);
    }

    /**
     * * <30分钟泡点一次>
     */
    public static int 福利泡点 = 0;

    public static void 福利泡点(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (福利泡点 > 0) {
                    try {
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                                if (chr == null) {
                                    continue;
                                }
                                if (MapleParty.OX答题活动 == 0) {
                                    if (chr.level > 10) {
                                        int 点券 = 0;
                                        int 经验 = 0;
                                        int 金币 = 0;
                                        int 抵用 = 0;
                                        int 泡点金币开关 = gui.Start.ConfigValuesMap.get("泡点金币开关");
                                        if (泡点金币开关 <= 0) {
                                            int 泡点金币 = gui.Start.ConfigValuesMap.get("泡点金币");
                                            金币 += 泡点金币;
                                            if (chr.getEquippedFuMoMap().get(34) != null) {
                                                金币 += 泡点金币 / 100 * chr.getEquippedFuMoMap().get(34);
                                            }
                                            chr.gainMeso(泡点金币, true);
                                        }
                                        int 泡点点券开关 = gui.Start.ConfigValuesMap.get("泡点点券开关");
                                        if (泡点点券开关 <= 0) {
                                            int 泡点点券 = gui.Start.ConfigValuesMap.get("泡点点券");
                                            chr.modifyCSPoints(1, 泡点点券, true);
                                            点券 += 泡点点券;
                                        }
                                        int 泡点抵用开关 = gui.Start.ConfigValuesMap.get("泡点抵用开关");
                                        if (泡点抵用开关 <= 0) {
                                            int 泡点抵用 = gui.Start.ConfigValuesMap.get("泡点抵用");
                                            chr.modifyCSPoints(2, 泡点抵用, true);
                                            抵用 += 泡点抵用;
                                        }
                                        int 泡点经验开关 = gui.Start.ConfigValuesMap.get("泡点经验开关");
                                        if (泡点经验开关 <= 0) {
                                            int 泡点经验 = gui.Start.ConfigValuesMap.get("泡点经验");
                                            经验 += 泡点经验;
                                            if (chr.getEquippedFuMoMap().get(33) != null) {
                                                经验 += 泡点经验 / 100 * chr.getEquippedFuMoMap().get(33);
                                            }
                                            if (chr.Getcharactera("射手村繁荣度", 1) > 0) {
                                                经验 += 泡点经验 * chr.Getcharactera("射手村繁荣度", 1) * 0.000001;
                                            }
                                            chr.gainExp(经验, false, false, false);
                                        }
                                        BossRankManager3.getInstance().setLog(chr.getId(), chr.getName(), "泡点经验", (byte) 2, (byte) 1);
                                        chr.getClient().sendPacket(UIPacket.getTopMsg("[" + MapleParty.开服名字 + "泡点]:" + 点券 + " 点券 / " + 抵用 + " 抵用 / " + 经验 + " 经验 / " + 金币 + " 金币，泡点经验 + 1"));
                                        chr.getClient().sendPacket(MaplePacketCreator.enableActions());
                                    }
                                }
                            }
                        }
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在发放泡点 √ ");
                    } catch (Exception e) {
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在启用备用发放泡点 √ ");
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000 * 10);
                                    福利泡点();
                                } catch (InterruptedException e) {
                                }
                            }
                        }.start();
                    }
                } else {
                    福利泡点++;
                }
            }
        }, 60 * 1000 * time);
    }

    /**
     * * <备用发送福利泡点>
     */
    public static void 福利泡点() {
        try {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr == null) {
                        continue;
                    }
                    if (chr.level > 10) {
                        int 点券 = 0;
                        int 经验 = 0;
                        int 金币 = 0;
                        int 抵用 = 0;
                        int 泡点金币开关 = gui.Start.ConfigValuesMap.get("泡点金币开关");
                        if (泡点金币开关 <= 0) {
                            int 泡点金币 = gui.Start.ConfigValuesMap.get("泡点金币");
                            金币 += 泡点金币;
                            if (chr.getEquippedFuMoMap().get(34) != null) {
                                金币 += 泡点金币 / 100 * chr.getEquippedFuMoMap().get(34);
                            }
                            chr.gainMeso(泡点金币, true);
                        }
                        int 泡点点券开关 = gui.Start.ConfigValuesMap.get("泡点点券开关");
                        if (泡点点券开关 <= 0) {
                            int 泡点点券 = gui.Start.ConfigValuesMap.get("泡点点券");
                            chr.modifyCSPoints(1, 泡点点券, true);
                            点券 += 泡点点券;
                        }
                        int 泡点抵用开关 = gui.Start.ConfigValuesMap.get("泡点抵用开关");
                        if (泡点抵用开关 <= 0) {
                            int 泡点抵用 = gui.Start.ConfigValuesMap.get("泡点抵用");
                            chr.modifyCSPoints(2, 泡点抵用, true);
                            抵用 += 泡点抵用;
                        }
                        int 泡点经验开关 = gui.Start.ConfigValuesMap.get("泡点经验开关");
                        if (泡点经验开关 <= 0) {
                            int 泡点经验 = gui.Start.ConfigValuesMap.get("泡点经验");
                            经验 += 泡点经验;
                            if (chr.getEquippedFuMoMap().get(33) != null) {
                                经验 += 泡点经验 / 100 * chr.getEquippedFuMoMap().get(33);
                            }
                            chr.gainExp(泡点经验, true, true, false);
                        }
                        BossRankManager3.getInstance().setLog(chr.getId(), chr.getName(), "泡点经验", (byte) 2, (byte) 1);
                        chr.getClient().sendPacket(UIPacket.getTopMsg("[" + MapleParty.开服名字 + "泡点]:" + 点券 + " 点券 / " + 抵用 + " 抵用 / " + 经验 + " 经验 / " + 金币 + " 金币，泡点经验 + 1"));
                        chr.getClient().sendPacket(MaplePacketCreator.enableActions());
                    }
                }
            }
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在发放泡点 ↑√ ");
        } catch (Exception e) {
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在发放泡点 ↑× ");
        }
    }

    /**
     * * <30分钟雇佣泡点一次>
     */
    public static int 福利泡点2 = 0;

    public static void 福利泡点2(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (福利泡点2 > 0) {

                    int ID = 0;
                    try {
                        Connection con = DatabaseConnection.getConnection();
                        PreparedStatement ps = con.prepareStatement("SELECT `id` FROM accounts  ORDER BY `id` DESC LIMIT 1");
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                String SN = rs.getString("id");
                                int sns = Integer.parseInt(SN);
                                sns++;
                                ID = sns;
                            }
                        }
                        for (int i = 0; i <= ID; i++) {
                            boolean 雇佣 = World.hasMerchant(i);
                            if (雇佣) {
                                //写入金币
                                雇佣写入(i, 1, gui.Start.ConfigValuesMap.get("雇佣泡点金币"));
                                //写入点券
                                雇佣写入(i, 2, gui.Start.ConfigValuesMap.get("雇佣泡点点券"));
                            }
                        }
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在发放雇佣泡点 √ ");
                    } catch (SQLException | NumberFormatException e) {
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在启用备用发放泡点 x ");
                    }
                } else {
                    福利泡点2++;
                }
            }
        }, 1000 * 60 * time);
    }

    /**
     * * <5分钟存档一次>
     */
    private static int 自动存档 = 0;

    public static void 自动存档(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (自动存档 > 0) {
                    try {
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                                if (chr == null) {
                                    continue;
                                }
                                chr.saveToDB(false, false);
                                chr.getClient().sendPacket(MaplePacketCreator.enableActions());
                                if (chr.getSession() != null && !chr.getClient().getSession().isActive()) {
                                    System.err.println("[服务端]" + CurrentReadable_Time() + " : 心跳超时，断开链接(" + chr.getClient().getSession().isActive() + " ？ " + chr.getId() + ") √");
                                    chr.getClient().getSession().close();
                                }
                            }
                        }
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在自动存档，并且检测心跳 √");
                    } catch (Exception e) {
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在启用备用自动存档 √");
                    }
                } else {
                    自动存档++;
                }
            }
        }, 60 * 1000 * time);
    }

    private static int 回收地图 = 0;

    public static void 回收地图(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (回收地图 > 0) {
                    try {
                        try {
                            Connection con = DatabaseConnection.getConnection();
                            PreparedStatement ps = con.prepareStatement("SELECT * FROM map  ");
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                if (rs.getInt("id") < 910000000 || rs.getInt("id") > 910000024) {
                                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                                        if (cserv == null) {
                                            continue;
                                        }
                                        cserv.getMapFactory().destroyMap(rs.getInt("id"), true);
                                        cserv.getMapFactory().HealMap(rs.getInt("id"));
                                    }
                                }
                            }
                            ps.close();
                        } catch (SQLException ex) {
                        }
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在回收地图 √");
                    } catch (Exception e) {
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在回收地图 √");
                    }
                } else {
                    回收地图++;
                }
            }
        }, 60 * 1000 * time);
    }

    /**
     * * <备用自动存档>
     */
    public static void 自动存档() {
        try {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr == null) {
                        continue;
                    }
                    chr.saveToDB(false, false);
                    chr.getClient().sendPacket(MaplePacketCreator.enableActions());
                }
            }
        } catch (Exception e) {
        }
    }

    public static void 记录在线时间补救(int a) {
        Connection con = DatabaseConnection.getConnection();
        try {
            try (PreparedStatement psu = con.prepareStatement("UPDATE characters SET todayOnlineTime = todayOnlineTime + ?, totalOnlineTime = totalOnlineTime + ? WHERE id = ?")) {
                psu.setInt(1, 1);
                psu.setInt(2, 1);
                psu.setInt(3, a);
                psu.executeUpdate();
                psu.close();
            }
            //通信("玩家 [" + 角色ID取名字(a) + "] 补救成功。");
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 记录在线第一次补救成功 √ (" + a + ")");
        } catch (SQLException ex) {
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 记录在线第一次补救失败 × (" + a + ")" + ex.getMessage());
            记录在线时间补救2(a);
        }
    }

    public static void 记录在线时间补救2(int a) {
        Connection con = DatabaseConnection.getConnection();
        try {
            try (PreparedStatement psu = con.prepareStatement("UPDATE characters SET todayOnlineTime = todayOnlineTime + ?, totalOnlineTime = totalOnlineTime + ? WHERE id = ?")) {
                psu.setInt(1, 1);
                psu.setInt(2, 1);
                psu.setInt(3, a);
                psu.executeUpdate();
                psu.close();
            }
            //通信("玩家 [" + 角色ID取名字(a) + "] 补救成功。");
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 记录在线第二次补救成功 √ (" + a + ")");
        } catch (SQLException ex) {
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 记录在线第二次补救失败 × (" + a + ")" + ex.getMessage());
            记录在线时间补救3(a);
        }
    }

    public static void 记录在线时间补救3(int a) {
        Connection con = DatabaseConnection.getConnection();
        try {
            try (PreparedStatement psu = con.prepareStatement("UPDATE characters SET todayOnlineTime = todayOnlineTime + ?, totalOnlineTime = totalOnlineTime + ? WHERE id = ?")) {
                psu.setInt(1, 1);
                psu.setInt(2, 1);
                psu.setInt(3, a);
                psu.executeUpdate();
                psu.close();
            }
            //通信("玩家 [" + 角色ID取名字(a) + "] 补救成功。");
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 记录在线第三次补救成功 √ (" + a + ")");
        } catch (SQLException ex) {
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 记录在线第三次补救失败 × (" + a + ")" + ex.getMessage());

        }
    }

    /**
     * * <30分钟强制回收一次内存>
     */
    private static int 回收内存 = 0;

    public static void 回收内存(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (回收内存 > 0) {
                    System.gc();
                    System.err.println("[服务端]" + CurrentReadable_Time() + " : 回收服务端内存 √");
                } else {
                    回收内存++;
                }
            }
        }, 60 * 1000 * time);
    }

    /**
     * * <30分钟检检测一次IP绑定情况>
     */
    private static int 关系验证 = 0;

    public static void 关系验证2(int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (关系验证 > 0) {
                    if (关系验证(MapleParty.启动账号) == null ? "" + MapleParty.IP地址 + "" != null : !关系验证(MapleParty.启动账号).equals("" + MapleParty.IP地址 + "")) {
                        System.out.println("[服务端]" + CurrentReadable_Time() + " : 绑定地址发生变化，服务端 1 分钟后将会关闭 ×");
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000 * 60);
                                    System.exit(0);
                                } catch (InterruptedException e) {
                                }
                            }
                        }.start();
                    } else {
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 服务端验证云端绑定IP地址 √");
                    }
                } else {
                    关系验证 += 1;
                }
            }
        }, 1000 * 60 * time);
    }

    /**
     * * <60分钟检检测一次服务端版本>
     */
    private static int 检测服务端版本 = 0;
    private static int 检测服务端版本1 = 0;

    public static void 检测服务端版本(int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (检测服务端版本 > 0 && 检测服务端版本1 == 0) {
                    String v2 = Class2.Pgv();
                    if (Integer.parseInt(v2) != 版本) {
                        if (检测服务端版本1 == 0) {
                            通信("服务端有新版本，请你及时更新。");
                            检测服务端版本1++;
                        }
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 检测服务端有新版本，请你及时更新 √");
                    } else {
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 检测服务端版本，服务端当前是最新版本 √");
                    }
                } else {
                    检测服务端版本++;
                }
            }
        }, 1000 * 60 * time);
    }

    /**
     * * <60分钟统计一次最高在线人数>
     */
    private static int 统计最高在线人数 = 0;
    private static int 最高在线人数 = 0;
    private static int 最高在线人数2 = 0;

    public static void 统计在线人数(int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (统计最高在线人数 > 0) {
                    int p = 0;
                    try {
                        Connection con = DatabaseConnection.getConnection();
                        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE loggedin > 0"); ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                p += 1;
                            }
                            ps.close();
                        }
                    } catch (SQLException Ex) {
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 统计最高在线出错 ×");
                    }
                    最高在线人数 = p;
                    if (最高在线人数 > 最高在线人数2) {
                        最高在线人数 = 0;
                        最高在线人数2 = p;
                    }
                    if (最高在线人数2 > 最高在线人数 && 最高在线人数2 > 0) {
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 统计此次最高在线 √ 人数: " + 最高在线人数2);
                    }
                } else {
                    统计最高在线人数++;
                }
            }
        }, 1000 * 60 * time);
    }

    /**
     * * <循环播放公告> @param time
     *
     * @param time
     */
    public static void 公告(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                String 公告 = 本地取广播();
                if (!"".equals(公告)) {
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, " : " + 公告));
                }
            }
        }, time * 1000 * 60);
    }

    /**
     * * <其他>
     */
    public static void GetConfigValues() {
        //动态数据库连接
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT name, val FROM ConfigValues")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int val = rs.getInt("val");
                    ConfigValuesMap.put(name, val);
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("读取动态数据库出错：" + ex.getMessage());
        }
    }

    public static void 读取地图吸怪检测() {
        //动态数据库连接
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT name, val FROM 地图吸怪检测")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int val = rs.getInt("val");
                    地图吸怪检测.put(name, val);
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("读取吸怪检测错误：" + ex.getMessage());
        }
    }

    public static void GetFuMoInfo() {
        FuMoInfoMap.clear();
        System.out.println("○ 开始加载附魔装备效果");
        //重载//
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT fumoType, fumoName, fumoInfo FROM mxmxd_fumo_info")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int fumoType = rs.getInt("fumoType");
                    String fumoName = rs.getString("fumoName");
                    String fumoInfo = rs.getString("fumoInfo");
                    FuMoInfoMap.put(fumoType, new String[]{fumoName, fumoInfo});
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("○ 加载附魔装备效果预览失败。");
        }
    }

    public static void 读取技能范围检测() {
        //动态数据库连接
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT name, val FROM 技能范围检测")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int val = rs.getInt("val");
                    技能范围检测.put(name, val);
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("读取吸怪检测错误：" + ex.getMessage());
        }
    }

    public static void 读取技能PVP伤害() {
        //动态数据库连接
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT name, val FROM pvpskills")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int val = rs.getInt("val");
                    PVP技能伤害.put(name, val);
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("读取技能PVP伤害错误：" + ex.getMessage());
        }
    }

    public static void 读取技个人信息设置() {
        //动态数据库连接
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT name, val FROM jiezoudashi")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int val = rs.getInt("val");
                    个人信息设置.put(name, val);
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("读取个人信息设置错误：" + ex.getMessage());
        }
    }

    public static void 服务器信息() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress().toString(); //獲取本機ip
            String hostName = addr.getHostName().toString(); //獲取本機計算機名稱
            //System.out.println("本機IP："+ip+"\n本機名稱:"+hostName);
            System.out.println("○ 检测服务端运行环境");
            System.out.println("○ 服务器名: " + hostName);
            Properties 設定檔 = System.getProperties();
            System.out.println("○ 操作系统：" + 設定檔.getProperty("os.name"));
            System.out.println("○ 系统框架：" + 設定檔.getProperty("os.arch"));
            System.out.println("○ 系统版本：" + 設定檔.getProperty("os.version"));
            System.out.println("○ 服务端目录：" + 設定檔.getProperty("user.dir"));
            System.out.println("○ 服务端环境检测完成");
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static class Shutdown implements Runnable {

        @Override
        public void run() {
            new Thread(ShutdownServer.getInstance()).start();
        }
    }

    public static void 更新通知() {
        if (更新通知 != null) {
            更新通知.dispose();
        }
        更新通知 = new 脚本更新器();
        更新通知.setVisible(true);
    }

    public static void CashShopServer() {
        if (Start.CashGui != null) {
            Start.CashGui.dispose();
        }
        Start.CashGui = new ZEVMS2();
        Start.CashGui.setVisible(true);
    }

    public static void 启动动态配置台() {
        if (Start.服务端功能开关 != null) {
            Start.服务端功能开关.dispose();
        }
        Start.服务端功能开关 = new 控制台1号();
        Start.服务端功能开关.setVisible(true);
    }

    public static void 广告() {
        if (Start.广告 != null) {
            Start.广告.dispose();
        }
        Start.广告 = new 广告();
        Start.广告.setVisible(true);
    }

    public static void 信息输出() {
        if (Start.信息输出 != null) {
            Start.信息输出.dispose();
        }
        Start.信息输出 = new 聊天记录显示();
        Start.信息输出.setVisible(true);
    }

    public static void CashShopServer2() {
        if (Start.DebugWindow != null) {
            Start.DebugWindow.dispose();
        }
        Start.DebugWindow = new DebugWindow();
        Start.DebugWindow.setVisible(true);
    }

    public static Map<Integer, String[]> FuMoInfoMap = new HashMap<>();

    public static Map<String, String> CloudBlacklist = new HashMap<>();

    public static void GetCloudBacklist() {
        CloudBlacklist.clear();
        try {
            String backlistStr = Class2.Pgb();
            if (backlistStr.contains("/")) {
                String blacklistArr[] = backlistStr.split(",");
                for (int i = 0; i < blacklistArr.length; i++) {
                    String pairString = blacklistArr[i];
                    String pair[] = pairString.split("/");
                    String qq = pair[0];
                    String ip = "/" + pair[1];
                    CloudBlacklist.put(qq, ip);
                }
            }
        } catch (Exception ex) {
            System.err.println("获取云黑名单出错：" + ex.getMessage());
        }
    }

    public static boolean 检测合法() {
        //检测是否启动指定程序
        ByteArrayOutputStream baos = null;
        InputStream os = null;
        String s = "";
        try {
            Process p = Runtime.getRuntime().exec("cmd /c tasklist");
            baos = new ByteArrayOutputStream();
            os = p.getInputStream();
            byte b[] = new byte[256];
            while (os.read(b) > 0) {
                baos.write(b);
            }
            s = baos.toString();
            if (s.indexOf("zevms079.exe") >= 0) {
            } else {
                System.out.println("服务端检测到缺少文件，已自动关闭。");
                System.exit(0);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int 取装备代码(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemid as DATA FROM inventoryitems WHERE inventoryitemid = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("取装备代码、出错");
        }
        return data;
    }

    public static int 取装备拥有者(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT characterid as DATA FROM inventoryitems WHERE inventoryitemid = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("取装备代码、出错");
        }
        return data;
    }

    public static int 服务器金币() {
        int p = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT meso as DATA FROM characters ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p += rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("上传数据,服务器金币,出错");
        }
        return p;
    }

    public static String 角色ID取名字(int id) {
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
            System.err.println("获取角色ID取名字出错 - 数据库查询失败：" + Ex);
        }
        if (data == null) {
            data = "匿名人士";
        }
        return data;
    }

    public static int 服务器角色() {
        int p = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id as DATA FROM characters WHERE id >=0");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("服务器角色？");
        }
        return p;
    }

    public static int 服务器账号() {
        int p = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id as DATA FROM accounts WHERE id >=0");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("服务器账号？");
        }
        return p;
    }

    public static int 服务器技能() {
        int p = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id as DATA FROM skills ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("服务器技能？");
        }
        return p;
    }

    public static int 服务器道具() {
        int p = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT inventoryitemid as DATA FROM inventoryitems WHERE inventoryitemid >=0");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("服务器道具？");
        }
        return p;
    }

    public static int 服务器商城商品() {
        int p = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT serial as DATA FROM cashshop_modified_items WHERE serial >=0");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("服务器商城商品？");
        }
        return p;
    }

    public static int 服务器游戏商品() {
        int p = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT shopitemid as DATA FROM shopitems WHERE shopitemid >=0");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("服务器道具游戏商品？");
        }
        return p;
    }

    private static void 任务同步修复() {
        try {
            下载文件("http://123.207.53.97:8082/任务修复/任务修复.zip", "任务修复.zip", "" + 任务更新下载目录() + "/");
            解压文件.解压文件(任务更新解压目录("任务修复"), 任务更新导入目录("zevms"));
            删除文件(任务更新解压目录("任务修复"));
        } catch (Exception e) {

        }
    }

    protected static void checkCopyItemFromSql() {
        System.out.println("服务端启用 防复制系统，发现复制装备.进行删除处理功能");
        List<Integer> equipOnlyIds = new ArrayList<>(); //[道具的唯一ID信息]
        Map<Integer, Integer> checkItems = new HashMap<>(); //[道具唯一ID 道具ID]
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            //读取检测复制装备
            ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE equipOnlyId > 0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt("itemId");
                int equipOnlyId = rs.getInt("equipOnlyId");
                if (equipOnlyId > 0) {
                    if (checkItems.containsKey(equipOnlyId)) { //发现重复的唯一ID装备
                        if (checkItems.get(equipOnlyId) == itemId) {
                            equipOnlyIds.add(equipOnlyId);
                        }
                    } else {
                        checkItems.put(equipOnlyId, itemId);
                    }
                }
            }
            rs.close();
            ps.close();
            //删除所有复制装备的唯一ID信息
            Collections.sort(equipOnlyIds);
            for (int i : equipOnlyIds) {
                ps = con.prepareStatement("DELETE FROM inventoryitems WHERE equipOnlyId = ?");
                ps.setInt(1, i);
                ps.executeUpdate();
                ps.close();
                System.out.println("发现复制装备 该装备的唯一ID: " + i + " 已进行删除处理..");
                FileoutputUtil.log("装备复制.txt", "发现复制装备 该装备的唯一ID: " + i + " 已进行删除处理..");
            }
        } catch (SQLException ex) {
            System.out.println("[EXCEPTION] 清理复制装备出现错误." + ex);
        }
    }

    private static void 时间线程() {
        WorldTimer.getInstance().start();
        EtcTimer.getInstance().start();
        MapTimer.getInstance().start();
        MobTimer.getInstance().start();
        RespawnTimer.getInstance().start();
        CloneTimer.getInstance().start();
        EventTimer.getInstance().start();
        BuffTimer.getInstance().start();
    }

    public static String 重启服务(String string) {
        StringBuilder builder = new StringBuilder();
        try {
            // 调用 cmd命令，执行 net start mysql， /c 必须要有
            Process p = Runtime.getRuntime().exec("cmd.exe /c net start " + string);
            InputStream inputStream = p.getInputStream();
            // 获取命令执行完的结果
            Scanner scanner = new Scanner(inputStream, "GBK");
            scanner.useDelimiter("\\A");

            while (scanner.hasNext()) {
                builder.append(scanner.next());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static String 关闭服务(String string) {
        StringBuilder builder = new StringBuilder();
        try {
            // 调用 cmd命令，执行 net start mysql， /c 必须要有
            Process p = Runtime.getRuntime().exec("cmd.exe /c net stop " + string);
            InputStream inputStream = p.getInputStream();

            // 获取命令执行完的结果
            Scanner scanner = new Scanner(inputStream, "GBK");
            scanner.useDelimiter("\\A");

            while (scanner.hasNext()) {
                builder.append(scanner.next());
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static void 重置仙人数据() {
        int ID = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `id` FROM characters  ORDER BY `id` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String SN = rs.getString("id");
                    int sns = Integer.parseInt(SN);
                    sns++;
                    ID = sns;
                }
            }
        } catch (SQLException ex) {

        }
        for (int i = 0; i <= ID; i++) {
            if (gui.Start.个人信息设置.get("仙人模式" + i + "") == null) {
                学习仙人模式("仙人模式" + i, 1);
            }
            if (gui.Start.个人信息设置.get("BUFF增益" + i + "") == null) {
                学习仙人模式("BUFF增益" + i, 1);
            }
            if (gui.Start.个人信息设置.get("硬化皮肤" + i + "") == null) {
                学习仙人模式("硬化皮肤" + i, 1);
            }
            if (gui.Start.个人信息设置.get("聪明睿智" + i + "") == null) {
                学习仙人模式("聪明睿智" + i, 1);
            }
            if (gui.Start.个人信息设置.get("物理攻击力" + i + "") == null) {
                学习仙人模式("物理攻击力" + i, 1);
            }
            if (gui.Start.个人信息设置.get("魔法攻击力" + i + "") == null) {
                学习仙人模式("魔法攻击力" + i, 1);
            }
            if (gui.Start.个人信息设置.get("物理狂暴力" + i + "") == null) {
                学习仙人模式("物理狂暴力" + i, 1);
            }
            if (gui.Start.个人信息设置.get("魔法狂暴力" + i + "") == null) {
                学习仙人模式("魔法狂暴力" + i, 1);
            }
            if (gui.Start.个人信息设置.get("物理吸收力" + i + "") == null) {
                学习仙人模式("物理吸收力" + i, 1);
            }
            if (gui.Start.个人信息设置.get("魔法吸收力" + i + "") == null) {
                学习仙人模式("魔法吸收力" + i, 1);
            }
            /*if (gui.Start.个人信息设置.get("能力契合" + i + "") == null) {
                学习仙人模式("能力契合" + i, 1);
            }*/
        }
        读取技个人信息设置();
    }

    public static void 学习仙人模式(String a, int b) {
        int ID = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `id` FROM jiezoudashi  ORDER BY `id` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String SN = rs.getString("id");
                    int sns = Integer.parseInt(SN);
                    sns++;
                    ID = sns;
                }
            }
        } catch (SQLException ex) {

        }
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO jiezoudashi ( id,name,Val ) VALUES ( ? ,?,?)")) {
            ps.setInt(1, ID);
            ps.setString(2, a);
            ps.setInt(3, b);
            ps.executeUpdate();
            ps.close();
            System.err.println("[服务端]" + CurrentReadable_Time() + " : 数据补充 " + a);
        } catch (SQLException ex) {

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

    public static String 账号ID取账号(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM accounts WHERE id = ?");
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

    public static void 雇佣写入(int Name, int Channale, int Piot) {
        try {
            int ret = Get雇佣写入(Name, Channale);
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO hirex (channel, Name,Point) VALUES (?, ?, ?)");
                    ps.setInt(1, Channale);
                    ps.setInt(2, Name);
                    ps.setInt(3, ret);

                    ps.execute();
                } catch (SQLException e) {
                    System.out.println("雇佣写入1:" + e);
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("雇佣写入2:" + e);
                    }
                }
            }
            ret += Piot;
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE hirex SET `Point` = ? WHERE Name = ? and channel = ?");
            ps.setInt(1, ret);
            ps.setInt(2, Name);
            ps.setInt(3, Channale);
            ps.execute();
            ps.close();
        } catch (SQLException sql) {
            System.err.println("雇佣写入3" + sql);
        }
    }

    public static int Get雇佣写入(int Name, int Channale) {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM hirex WHERE channel = ? and Name = ?");
            ps.setInt(1, Channale);
            ps.setInt(2, Name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            ret = rs.getInt("Point");
            rs.close();
            ps.close();
        } catch (SQLException ex) {
        }
        return ret;
    }

}
