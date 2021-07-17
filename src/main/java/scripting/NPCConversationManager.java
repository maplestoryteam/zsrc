/*
NPC用法调用大全
 */
package scripting;

import static abc.Game.主城;
import client.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import client.inventory.Equip;
import client.inventory.IItem;
import constants.GameConstants;
import client.inventory.ItemFlag;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.*;
//import static constants.GameConstants.绑定IP;
import server.MapleCarnivalParty;
import server.Randomizer;
import server.MapleInventoryManipulator;
import server.MapleShopFactory;
import server.MapleSquad;
import server.maps.MapleMap;
import server.maps.Event_DojoAgent;
import server.maps.AramiaFireWorks;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.packet.PlayerShopPacket;
import server.MapleItemInformationProvider;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import database.DatabaseConnection;
import static download.Toupdate.readInputStream;
import handling.cashshop.CashShopServer;
import handling.login.handler.AutoRegister;
import handling.world.CharacterTransfer;
import handling.world.MapleMessengerCharacter;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PlayerBuffStorage;
import handling.world.World;
import handling.world.guild.MapleGuild;
import server.MapleCarnivalChallenge;
import java.util.HashMap;
import handling.world.guild.MapleGuildAlliance;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import server.*;
import static server.MapleCarnivalChallenge.getJobNameById;
import static gui.QQMsgServer.sendMsg;
import static gui.QQMsgServer.sendMsgToQQGroup;
import gui.Start;
import static gui.Start.GetConfigValues;
import static gui.Start.GetFuMoInfo;
import static gui.Start.读取技个人信息设置;
import java.text.DecimalFormat;
import server.maps.SpeedRunType;
import server.Timer.CloneTimer;
import server.custom.bossrank8.BossRankInfo8;
import server.custom.bossrank8.BossRankManager8;
import server.custom.forum.Forum_Reply;
import server.custom.forum.Forum_Section;
import server.custom.forum.Forum_Thread;
import server.custom.rank.MiniGamePoints;
import server.custom.rank.RankManager;
import server.life.*;
import server.maps.*;
import tools.FileoutputUtil;
import static tools.FileoutputUtil.CurrentReadable_Date;
import static tools.FileoutputUtil.CurrentReadable_Time;
import static tools.MaplePacketCreator.showSpecialEffect;
import tools.StringUtil;
import tools.packet.UIPacket;

public class NPCConversationManager extends AbstractPlayerInteraction {

    private MapleClient c;
    private int npc, questid;
    private String getText;
    private byte type; // -1 = NPC, 0 = start quest, 1 = end quest
    private byte lastMsg = -1;
    public boolean pendingDisposal = false;
    private Invocable iv;
    private int wh = 0;

    /*
     * public NPCConversationManager(MapleClient c, int npc, int questid, byte
     * type, Invocable iv) { super(c); this.c = c; this.npc = npc; this.questid
     * = questid; this.type = type; this.iv = iv; }
     */
    public static int 今日全服总在线时间() {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("todayOnlineTime") > 0) {
                    data += rs.getInt("todayOnlineTime");
                }
            }
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 今日家族总在线时间(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("guildid") == a) {
                    if (rs.getInt("todayOnlineTime") > 0) {
                        data += rs.getInt("todayOnlineTime");
                    }
                }
            }
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 角色ID取GM(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT gm as DATA FROM characters WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("角色ID取上线喇叭、出错");
        }
        return data;
    }

    public static String 角色ID取上线喇叭(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id2 as DATA FROM shangxianlaba WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("角色ID取上线喇叭、出错");
        }
        return data;
    }

    public static void 修改上线提醒语言(int a, String b) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM shangxianlaba");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String aa = null;
                aa = "update shangxianlaba set id2 = '" + b + "' where id = " + a + ";";
                PreparedStatement ab = DatabaseConnection.getConnection().prepareStatement(aa);
                ab.executeUpdate(aa);
                //}
            }
        } catch (SQLException ex) {
        }
    }

    public NPCConversationManager(MapleClient c, int npc, int questid, byte type, Invocable iv, int wh) {
        super(c);
        this.c = c;
        this.npc = npc;
        this.questid = questid;
        this.type = type;
        this.iv = iv;
        this.wh = wh;
    }

    public int getwh() {
        return this.wh;
    }

    public Invocable getIv() {
        return iv;
    }

    public String serverName() {//取服务器名字
        return c.getChannelServer().getServerName();
    }

    public int getNpc() {
        return npc;
    }

    public int getQuest() {
        return questid;
    }

    public byte getType() {
        return type;
    }

    public void safeDispose() {
        pendingDisposal = true;
    }

    public void dispose() {
        NPCScriptManager.getInstance().dispose(c);
    }

    public void 对话结束() {
        NPCScriptManager.getInstance().dispose(c);
    }

    public void askMapSelection(final String sel) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getMapSelection(npc, sel));
        lastMsg = 0xD;
    }

    public void sendNext(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { //sendNext will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", (byte) 0));
        lastMsg = 0;
    }

    public void sendNextS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", type));
        lastMsg = 0;
    }

    public void sendPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", type));
        lastMsg = 0;
    }

    public void sendNextPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", (byte) 0));
        lastMsg = 0;
    }

    public void PlayerToNpc(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", type));
        lastMsg = 0;
    }

    public void 说明文字(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendOk(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendOkS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", type));
        lastMsg = 0;
    }

    public void sendYesNo(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", (byte) 0));
        lastMsg = 1;
    }

    public void 是否说明文字(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", (byte) 0));
        lastMsg = 1;
    }

    public void sendYesNoS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", type));
        lastMsg = 1;
    }

    public void sendAcceptDecline(String text) {
        askAcceptDecline(text);
    }

    public void sendAcceptDeclineNoESC(String text) {
        askAcceptDeclineNoESC(text);
    }

    public void askAcceptDecline(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0B, text, "", (byte) 0));
        lastMsg = 0xB;
    }

    public void askAcceptDeclineNoESC(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", (byte) 0));
        lastMsg = 0xC;
    }

    public void askAvatar(String text, int card, int... args) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkStyle(npc, text, card, args));
        lastMsg = 7;
    }

    public void sendSimple(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) 0));
        lastMsg = 4;
    }

    public void sendSimple(String text, int speaker) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        getClient().sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) speaker));
        lastMsg = 4;
    }

    public void sendSimpleS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNextS(text, type);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) type));
        lastMsg = 4;
    }

    public void openWeb(String web) {//打开网址
        this.c.sendPacket(MaplePacketCreator.openWeb(web));
    }

    public void sendStyle(String text, int caid, int styles[]) {
        if (lastMsg > -1) {
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkStyle(npc, text, caid, styles));
        lastMsg = 7;
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
        lastMsg = 3;
    }

    public void sendGetNumber2(String text, int def, int min, int max) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
        lastMsg = 3;
    }

    public void sendGetText(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.sendPacket(MaplePacketCreator.getNPCTalkText(npc, text));
        lastMsg = 2;
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return getText;
    }

    public void setHair(int hair) {
        if (c.getPlayer().getMapId() == 100000104) {
            getPlayer().setHair(hair);
            getPlayer().updateSingleStat(MapleStat.HAIR, hair);//设置发型
            getPlayer().equipChanged();
        } else {
            c.getPlayer().dropMessage(1, "发型代理权已被射手村 娜塔丽 代理了，换发型请去找她的团队吧。");
        }
    }

    public void setFace(int face) {
        if (c.getPlayer().getMapId() == 100000103) {
            getPlayer().setFace(face);
            getPlayer().updateSingleStat(MapleStat.FACE, face);//设置脸型
            getPlayer().equipChanged();
        } else {
            c.getPlayer().dropMessage(1, "整容代理权已被射手村 差不多医生 代理了，整容请去找她的团队吧。");
        }
    }

    public void setSkin(int color) {
        getPlayer().setSkinColor((byte) color);
        getPlayer().updateSingleStat(MapleStat.SKIN, color);//设置肤色
        getPlayer().equipChanged();
    }

    public int setRandomAvatar(int ticket, int[] args_all) {//设置发型时
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);
        int args = args_all[Randomizer.nextInt(args_all.length)];
        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public int 随角色(int[] args_all) {//设置发型时

        int args = args_all[Randomizer.nextInt(args_all.length)];
        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public int setAvatar(int ticket, int args) {//设置发型
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public void sendStorage() {
        c.getPlayer().setConversation(4);
        c.getPlayer().getStorage().sendStorage(c, npc);
    }

    public void openShop(int id) {//打开商店
        MapleShopFactory.getInstance().getShop(id).sendShop(c);
    }

    public void 给赛季积分(int add) {
        if (Getsaiji("积分赛季开关", 1) <= 0) {
            setBossRank8(getPlayer().getId(), getPlayer().getName(), "赛季积分", (byte) 2, add);
            c.getPlayer().dropMessage(5, "赛季积分 + " + add + "");
            //段位达成提示
            /* if (getBossRank8("赛季积分", (byte) 2) >= Getsaiji("王者分数", 1)) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.startMapEffect("恭喜 " + c.getPlayer().getName() + " 活跃赛季达到 王者 段位。", 5120009);
                    }
                }
            } else if (getBossRank8("赛季积分", (byte) 2) >= Getsaiji("大师分数", 1)) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.startMapEffect("恭喜 " + c.getPlayer().getName() + " 活跃赛季达到 大师 段位。", 5120009);
                    }
                }
            } else if (getBossRank8("赛季积分", (byte) 2) >= Getsaiji("钻石分数", 1)) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.startMapEffect("恭喜 " + c.getPlayer().getName() + " 活跃赛季达到 钻石 段位。", 5120009);
                    }
                }
            } else if (getBossRank8("赛季积分", (byte) 2) >= Getsaiji("铂金分数", 1)) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.startMapEffect("恭喜 " + c.getPlayer().getName() + " 活跃赛季达到 铂金 段位。", 5120009);
                    }
                }
            } else if (getBossRank8("赛季积分", (byte) 2) >= Getsaiji("黄金分数", 1)) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.startMapEffect("恭喜 " + c.getPlayer().getName() + " 活跃赛季达到 黄金 段位。", 5120009);
                    }
                }
            } else if (getBossRank8("赛季积分", (byte) 2) >= Getsaiji("白银分数", 1)) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.startMapEffect("恭喜 " + c.getPlayer().getName() + " 活跃赛季达到 白银 段位。", 5120009);
                    }
                }
            }*/
        }
    }

    @Override
    public int getBossRank8(int cid, String bossname, byte type) {
        int ret = -1;
        BossRankInfo8 info = BossRankManager8.getInstance().getInfo(cid, bossname);
        if (null == info) {
            return ret;
        }
        switch (type) {
            case 1://积分
                ret = info.getPoints();
                break;
            case 2://次数
                ret = info.getCount();
                break;
        }
        return ret;
    }

    public void 打开商店(int id) {//打开商店
        MapleShopFactory.getInstance().getShop(id).sendShop(c);
    }

    public final MapleInventory 判断背包装备栏() {//判断背包
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) 1));
    }

    public final MapleInventory 判断背包消耗栏() {//判断背包
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) 2));
    }

    public final MapleInventory 判断背包设置栏() {//判断背包
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) 3));
    }

    public final MapleInventory 判断背包其他栏() {//判断背包
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) 4));
    }

    public final MapleInventory 判断背包特殊栏() {//判断背包
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) 5));
    }

//    public void 判断背包装备栏() {
//        c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNextFreeSlot();
//    }
//
//    public void 判断背包消耗栏() {
//        c.getPlayer().getInventory(MapleInventoryType.USE).getNextFreeSlot();
//    }
//
//    public void 判断背包设置栏() {
//        c.getPlayer().getInventory(MapleInventoryType.SETUP).getNextFreeSlot();
//    }
//
//    public void 判断背包其他栏() {
//        c.getPlayer().getInventory(MapleInventoryType.ETC).getNextFreeSlot();
//    }
//
//    public void 判断背包特殊栏() {
//        c.getPlayer().getInventory(MapleInventoryType.CASH).getNextFreeSlot();
//    }
    public int gainGachaponItem(int id, int quantity) {//物品类
        return gainGachaponItem(id, quantity, c.getPlayer().getMap().getStreetName() + " - " + c.getPlayer().getMap().getMapName());
    }

    public int gainGachaponItem(int id, int quantity, final String msg) {//百宝箱抽奖
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            final byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            if (rareness > 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[" + msg + "] " + c.getPlayer().getName(), " : 恭喜获得道具!", item, rareness, getPlayer().getClient().getChannel()));
            }
            return item.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int gainGachaponItem(int id, int quantity, final String msg, int 概率) {//百宝箱抽奖
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            if (概率 > 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[ " + msg + " ] : 已经被玩家 [ " + c.getPlayer().getName(), " ] 幸运抽中！", item, (byte) 0, getPlayer().getClient().getChannel()));
            }
            return item.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int gainGachaponItem2(int id, int quantity, final String msg, int 概率) {//百宝箱抽奖
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            if (概率 > 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[ " + msg + " ] : 已经被玩家 [ " + c.getPlayer().getName(), " ] 幸运抽中！", item, (byte) 0, getPlayer().getClient().getChannel()));
            }

            return item.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int gainGachaponItem3(int id, int quantity, final String msg, int 概率) {//百宝箱抽奖
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            if (概率 > 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[ " + msg + " ] : 已经被玩家 [ " + c.getPlayer().getName(), " ] 幸运抽中！", item, (byte) 0, getPlayer().getClient().getChannel()));
            }
            return item.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void changeJob(int job) {//转换职业
        c.getPlayer().changeJob(job);
    }

    public void startQuest(int id) {//任务
        MapleQuest.getInstance(id).start(getPlayer(), npc);
    }

//    public void 开始任务(int id) {//任务
//        MapleQuest.getInstance(id).start(getPlayer(), npc);
//    }
    public void 完成任务(int id) {
        MapleQuest.getInstance(id).complete(getPlayer(), npc);
    }

    public void 放弃任务(int id) {
        MapleQuest.getInstance(id).forfeit(getPlayer());
    }

    public void completeQuest(int id) {
        MapleQuest.getInstance(id).complete(getPlayer(), npc);
    }

    public void forfeitQuest(int id) {
        MapleQuest.getInstance(id).forfeit(getPlayer());
    }

    public void forceStartQuest() {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(int id) {
        MapleQuest.getInstance(id).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(String customData) {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), customData);
    }

    public void 任务开始() {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), null);
    }

    public void 任务开始(int id) {
        MapleQuest.getInstance(id).forceStart(getPlayer(), getNpc(), null);
    }

    public void 开始任务(int id) {
        MapleQuest.getInstance(id).forceStart(getPlayer(), getNpc(), null);
    }

    public void 任务开始(String customData) {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), customData);
    }

    public void 任务完成() {
        MapleQuest.getInstance(questid).forceComplete(getPlayer(), getNpc());
    }

    public void 任务完成(final int id) {
        MapleQuest.getInstance(id).forceComplete(getPlayer(), getNpc());
    }

    public void 任务放弃(int id) {
        MapleQuest.getInstance(id).forfeit(getPlayer());
    }

    public void completeQuest() {
        forceCompleteQuest();
    }

    public void forceCompleteQuest() {
        MapleQuest.getInstance(questid).forceComplete(getPlayer(), getNpc());
    }

    public void forceCompleteQuest(final int id) {
        MapleQuest.getInstance(id).forceComplete(getPlayer(), getNpc());
    }

    public String getQuestCustomData() {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).getCustomData();
    }

    public void setQuestCustomData(String customData) {
        getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(customData);
    }

    public int getLevel() {//判断等级
        return getPlayer().getLevel();
    }

    public void 设置等级(int s) {//判断等级
        c.getPlayer().setLevel((short) s);
    }

    public int 判断等级() {//判断等级
        return getPlayer().getLevel();
    }

    public void 刷新() {//刷新
        MapleCharacter player = c.getPlayer();
        c.sendPacket(MaplePacketCreator.getCharInfo(player));
        player.getMap().removePlayer(player);
        player.getMap().addPlayer(player);
    }

    public int 给炼金经验(int s) {
        return setBossRankCount5("炼金经验", s);
    }

    public int 挖矿经验() {//判断等级        
        return getBossRankCount5("炼金经验");
    }

    public int 判断金币() {//判断金币
        return getPlayer().getMeso();
    }

    public int 判断角色ID() {//判断金币
        return c.getPlayer().getId();
    }

    public int 判断点券() {
        return c.getPlayer().getCSPoints(1);
    }

    public int 判断抵用券() {
        return c.getPlayer().getCSPoints(2);
    }

    public int 判断声望() {
        return getPlayer().getCurrentRep();
    }

    public int 判断学院() {
        return getPlayer().getFamilyId();
    }

    public int 判断师傅() {
        return getPlayer().getSeniorId();
    }

    public void 给声望(int s) {
        c.getPlayer().setCurrentRep(s);
    }

    public void 组队人数() {
        if (getParty() != null) {
            c.getPlayer().getParty().getMembers().size();
        }
    }

    public int 判断经验() {
        return c.getPlayer().getExp();
    }

    public int 判断当前地图怪物数量() {
        return c.getPlayer().getMap().getAllMonstersThreadsafe().size();
    }

    public int 判断指定地图怪物数量(int a) {
        return getMap(a).getAllMonstersThreadsafe().size();
    }

    public int 判断当前地图玩家数量() {
        return c.getPlayer().getMap().getCharactersSize();
    }

    public int 随机数(int a) {
        return (int) Math.ceil(Math.random() * a);
    }

    public boolean 百分率(int q) {
        int a = (int) Math.ceil(Math.random() * 100);
        if (a <= q) {
            return true;
        } else {
            return false;
        }
    }

    public void 重置目标地图(int a) {
        getMap(a).resetFully();
    }

    public void 清除地图怪物(int a, int b) {
        MapleMonster mob;
        double range = Double.POSITIVE_INFINITY;
        MapleMap map = getMap(a);
        boolean drop = false;
        if (b == 0) {
            drop = true;
        } else {
            drop = false;
        }
        for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
            mob = (MapleMonster) monstermo;
            map.killMonster(mob, c.getPlayer(), drop, false, (byte) 1);
        }
    }

    public void 清除地图物品(int a) {
        getMap(a).removeDrops();
    }

    public String 职业(int a) {
        return getJobNameById(a);
    }

    public int getMeso() {//判断金币
        return getPlayer().getMeso();
    }

    public void gainAp(final int amount) {//给AP
        c.getPlayer().gainAp((short) amount);
    }

    public void 给能力点(final int amount) {//给AP
        c.getPlayer().gainAp((short) amount);
    }

    public void 收能力点(final int amount) {//给AP
        c.getPlayer().gainAp((short) -amount);
    }

    public void 给技能点(final int amount) {//给AP
        c.getPlayer().gainSP((short) amount);
    }

    public void 收技能点(final int amount) {//给AP
        c.getPlayer().gainSP2((short) amount);
    }

    public void expandInventory(byte type, int amt) {//待实现
        c.getPlayer().expandInventory(type, amt);
    }

    public void 脱光装备() {//脱下身上装备
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<Short> ids = new LinkedList<Short>();
        for (IItem item : equipped.list()) {
            ids.add(item.getPosition());
        }
        for (short id : ids) {
            MapleInventoryManipulator.unequip(getC(), id, equip.getNextFreeSlot());
        }
    }

    public void 脱掉并且销毁装备(int x) {//脱下身上装备
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<Short> ids = new LinkedList<Short>();
        for (IItem item : equipped.list()) {
            ids.add(item.getPosition());
        }
        for (short id : ids) {
            if (id == x) {
                MapleInventoryManipulator.unequip(getC(), id, equip.getNextFreeSlot());
            }
        }
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
    }

    public void unequipEverything() {//脱下身上装备
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<Short> ids = new LinkedList<Short>();
        for (IItem item : equipped.list()) {
            ids.add(item.getPosition());
        }
        for (short id : ids) {
            MapleInventoryManipulator.unequip(getC(), id, equip.getNextFreeSlot());
        }
    }

    public final void clearSkills() {//应该是关于技能的
        Map<ISkill, SkillEntry> skills = getPlayer().getSkills();
        for (Entry<ISkill, SkillEntry> skill : skills.entrySet()) {
            getPlayer().changeSkillLevel(skill.getKey(), (byte) 0, (byte) 0);
        }
    }

    public boolean hasSkill(int skillid) {//技能类
        ISkill theSkill = SkillFactory.getSkill(skillid);
        if (theSkill != null) {
            return c.getPlayer().getSkillLevel(theSkill) > 0;
        }
        return false;
    }

    public void showEffect(boolean broadcast, String effect) {//cm.showEffect(true, "quest/party/clear");播放动画
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.showEffect(effect));
        } else {
            c.sendPacket(MaplePacketCreator.showEffect(effect));
        }
    }

    public void showString(boolean broadcast, String effect) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.showString(effect));
        } else {
            c.sendPacket(MaplePacketCreator.showString(effect));
        }
    }

    public void playSound(boolean broadcast, String sound) {//播放音乐
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.playSound(sound));
        } else {
            c.sendPacket(MaplePacketCreator.playSound(sound));
        }
    }

    public void 全服音效(boolean broadcast, String sound) {//播放音乐
        for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
            for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.playSound(sound));
            }
        }
    }

    public void environmentChange(boolean broadcast, String env) {//环境变化，副本使用，唤出地图反应门？？
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(env, 2));
        } else {
            c.sendPacket(MaplePacketCreator.environmentChange(env, 2));
        }
    }

    public void updateBuddyCapacity(int capacity) {//增加好友栏
        c.getPlayer().setBuddyCapacity((byte) capacity);
    }

    public int getBuddyCapacity() {//好友相关
        return c.getPlayer().getBuddyCapacity();
    }

    public int partyMembersInMap() {//家族联盟判断人员
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharactersThreadsafe()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    public List<MapleCharacter> getPartyMembers() {//家族联盟
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<MapleCharacter>(); // creates an empty array full of shit..
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                if (ch != null) { // double check <3
                    chars.add(ch);
                }
            }
        }
        return chars;
    }

    public void warpPartyWithExp(int mapId, int exp) {//指定地图给经验
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
            }
        }
    }

    public void warpPartyWithExpMeso(int mapId, int exp, int meso) {//指定地图给经验金币
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
                curChar.gainMeso(meso, true);
            }
        }
    }

    public MapleSquad getSquad(String type) {//判断事件？
        return c.getChannelServer().getMapleSquad(type);
    }

    public int getSquadAvailability(String type) {//判断事件？
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        }
        return squad.getStatus();
    }

    public boolean registerSquad(String type, int minutes, String startText) {//开始一个时钟
        if (c.getChannelServer().getMapleSquad(type) == null) {
            final MapleSquad squad = new MapleSquad(c.getChannel(), type, c.getPlayer(), minutes * 60 * 1000, startText);
            final boolean ret = c.getChannelServer().addMapleSquad(squad, type);
            if (ret) {
                final MapleMap map = c.getPlayer().getMap();

                map.broadcastMessage(MaplePacketCreator.getClock(minutes * 60));
                map.broadcastMessage(MaplePacketCreator.serverNotice(6, c.getPlayer().getName() + startText));
            } else {
                squad.clear();
            }
            return ret;
        }
        return false;
    }

    public boolean getSquadList(String type, byte type_) {//事件相关
//        try {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return false;
        }
        if (type_ == 0 || type_ == 3) { // Normal viewing
            sendNext(squad.getSquadMemberString(type_));
        } else if (type_ == 1) { // Squad Leader banning, Check out banned participant
            sendSimple(squad.getSquadMemberString(type_));
        } else if (type_ == 2) {
            if (squad.getBannedMemberSize() > 0) {
                sendSimple(squad.getSquadMemberString(type_));
            } else {
                sendNext(squad.getSquadMemberString(type_));
            }
        }
        return true;
        /*
         * } catch (NullPointerException ex) {
         * FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
         * return false; }
         */
    }

    public byte isSquadLeader(String type) {//事件相关
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else if (squad.getLeader() != null && squad.getLeader().getId() == c.getPlayer().getId()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean reAdd(String eim, String squad) {//事件相关
        EventInstanceManager eimz = getDisconnected(eim);
        MapleSquad squadz = getSquad(squad);
        if (eimz != null && squadz != null) {
            squadz.reAddMember(getPlayer());
            eimz.registerPlayer(getPlayer());
            return true;
        }
        return false;
    }

    public void banMember(String type, int pos) {//事件相关
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.banMember(pos);
        }
    }

    public void acceptMember(String type, int pos) {//事件相关
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.acceptMember(pos);
        }
    }

    public String getReadableMillis(long startMillis, long endMillis) {//？
        return StringUtil.getReadableMillis(startMillis, endMillis);
    }

    public int addMember(String type, boolean join) {//事件
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return squad.addMember(c.getPlayer(), join);
        }
        return -1;
    }

    public byte isSquadMember(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else if (squad.getMembers().contains(c.getPlayer())) {
            return 1;
        } else if (squad.isBanned(c.getPlayer())) {
            return 2;
        } else {
            return 0;
        }
    }

    public void resetReactors() {//事件
        getPlayer().getMap().resetReactors();
    }

    public void genericGuildMessage(int code) {//设置家族ID
        c.sendPacket(MaplePacketCreator.genericGuildMessage((byte) code));
    }

    public void disbandGuild() {//家族
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0 || c.getPlayer().getGuildRank() != 1) {
            return;
        }
        World.Guild.disbandGuild(gid);
    }

    //扩充家族人数
    public void increaseGuildCapacity() {
        if (c.getPlayer().getMeso() < 2500000) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "你没有足够的金币来扩充家族人数。"));
            return;
        }
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0) {
            return;
        }
        World.Guild.increaseGuildCapacity(gid);
        c.getPlayer().gainMeso(-2500000, true, false, true);
    }

    public void displayGuildRanks() {//家族排行榜
        c.sendPacket(MaplePacketCreator.showGuildRanks(npc, MapleGuildRanking.getInstance().getGuildRank()));
    }

    public boolean removePlayerFromInstance() {//事件类
        if (c.getPlayer().getEventInstance() != null) {
            c.getPlayer().getEventInstance().removePlayer(c.getPlayer());
            return true;
        }
        return false;
    }

    public boolean isPlayerInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            return true;
        }
        return false;
    }

    public void changeStat(byte slot, int type, short amount) {//修改装备的脚本
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
        switch (type) {
            case 0:
                sel.setStr(amount);
                break;
            case 1:
                sel.setDex(amount);
                break;
            case 2:
                sel.setInt(amount);
                break;
            case 3:
                sel.setLuk(amount);
                break;
            case 4:
                sel.setHp(amount);
                break;
            case 5:
                sel.setMp(amount);
                break;
            case 6:
                sel.setWatk(amount);
                break;
            case 7:
                sel.setMatk(amount);
                break;
            case 8:
                sel.setWdef(amount);
                break;
            case 9:
                sel.setMdef(amount);
                break;
            case 10:
                sel.setAcc(amount);
                break;
            case 11:
                sel.setAvoid(amount);
                break;
            case 12:
                sel.setHands(amount);
                break;
            case 13:
                sel.setSpeed(amount);
                break;
            case 14:
                sel.setJump(amount);
                break;
            case 15:
                sel.setUpgradeSlots((byte) amount);
                break;
            case 16:
                sel.setViciousHammer((byte) amount);
                break;
            case 17:
                sel.setLevel((byte) amount);
                break;
            case 18:
                sel.setEnhance((byte) amount);
                break;
            case 19:
                sel.setPotential1(amount);
                break;
            case 20:
                sel.setPotential2(amount);
                break;
            case 21:
                sel.setPotential3(amount);
                break;
            case 22:
                sel.setOwner(getText());
                break;
            default:
                break;
        }
        c.getPlayer().equipChanged();
    }

    public void killAllMonsters() {//技能？？
        MapleMap map = c.getPlayer().getMap();
        double range = Double.POSITIVE_INFINITY;
        MapleMonster mob;
        for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
            mob = (MapleMonster) monstermo;
            if (mob.getStats().isBoss()) {
                map.killMonster(mob, c.getPlayer(), false, false, (byte) 1);
            }
        }
        /*
         * int mapid = c.getPlayer().getMapId(); MapleMap map =
         * c.getChannelServer().getMapFactory().getMap(mapid);
         * map.killAllMonsters(true); // No drop.
         */
    }

    public void giveMerchantMesos() {//金币类？
        long mesos = 0;
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM hiredmerchants WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
            } else {
                mesos = rs.getLong("mesos");
            }
            rs.close();
            ps.close();

            ps = (PreparedStatement) con.prepareStatement("UPDATE hiredmerchants SET mesos = 0 WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        c.getPlayer().gainMeso((int) mesos, true);
    }

    public void dc() {
        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(c.getPlayer().getName().toString());
        victim.getClient().getSession().close();
        victim.getClient().disconnect(true, false);

    }

    public void 玩家反馈信息(String msg1, String msg2) {
        FileoutputUtil.logToFile("玩家反馈信息/" + CurrentReadable_Date() + "/" + msg1 + " 反馈信息.txt", "" + msg2 + "\r\n");
    }

    public void 充值卡兑换记录(String msg1, String msg2) {
        FileoutputUtil.logToFile("充值卡兑换记录/" + CurrentReadable_Date() + "/" + msg1 + " 充值卡.txt", "" + msg2 + "\r\n");
    }

    public void 修改密码(String 账号, String 密码) {
        if (密码.length() > 11) {
            c.getPlayer().dropMessage(1, "密码过长");
            return;
        }
        if (!AutoRegister.getAccountExists(账号)) {
            c.getPlayer().dropMessage(1, "账号不存在");
            return;
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("Update accounts set password = ? Where name = ?");
            ps.setString(1, LoginCrypto.hexSha1(密码));
            ps.setString(2, 账号);
            ps.execute();
            ps.close();
        } catch (Exception ex) {
        }
    }

    public long getMerchantMesos() {//关于金币
        long mesos = 0;
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM hiredmerchants WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
            } else {
                mesos = rs.getLong("mesos");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        return mesos;
    }

    public void openDuey() {//?
        c.getPlayer().setConversation(2);
        c.sendPacket(MaplePacketCreator.sendDuey((byte) 9, null));
    }

    public void openMerchantItemStore() {
        c.getPlayer().setConversation(3);
        c.sendPacket(PlayerShopPacket.merchItemStore((byte) 0x22));
//        boolean merch = World.hasMerchant(c.getPlayer().getAccountID());
//        if (!merch) {
//            c.getPlayer().setConversation(3);
//            c.sendPacket(PlayerShopPacket.merchItemStore((byte) 0x22));
//        } else {
//            c.getPlayer().dropMessage(1, "请先关闭你的商店。");
//        }

    }

    public void openMerchantItemStore1() {
        final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());
        //c.getPlayer().setConversation(3);
        c.sendPacket(PlayerShopPacket.merchItemStore_ItemData(pack));
    }

    private static final MerchItemPackage loadItemFrom_Database(final int charid, final int accountid) {
        final Connection con = DatabaseConnection.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where characterid = ? OR accountid = ?");
            ps.setInt(1, charid);
            ps.setInt(2, accountid);

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

            Map<Integer, Pair<IItem, MapleInventoryType>> items = ItemLoader.HIRED_MERCHANT.loadItems(false, charid);
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

    public void sendRepairWindow() {
        c.sendPacket(MaplePacketCreator.sendRepairWindow(npc));
    }

    public final int getDojoPoints() {
        return c.getPlayer().getDojo();
    }

    public final int getDojoRecord() {
        return c.getPlayer().getDojoRecord();
    }

    public void setDojoRecord(final boolean reset) {
        c.getPlayer().setDojoRecord(reset);
    }

    public boolean start_DojoAgent(final boolean dojo, final boolean party) {
        if (dojo) {
            return Event_DojoAgent.warpStartDojo(c.getPlayer(), party);
        }
        return Event_DojoAgent.warpStartAgent(c.getPlayer(), party);
    }

    public boolean start_PyramidSubway(final int pyramid) {//废弃地铁，金字塔
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpStartPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpStartSubway(c.getPlayer());
    }

    public boolean bonus_PyramidSubway(final int pyramid) {//废弃地铁，金字塔
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpBonusPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpBonusSubway(c.getPlayer());
    }

    public final short getKegs() {//是个什么东西，判断
        return AramiaFireWorks.getInstance().getKegsPercentage();
    }

    public void giveKegs(final int kegs) {//是个什么东西，显示
        AramiaFireWorks.getInstance().giveKegs(c.getPlayer(), kegs);
    }

    public final short getSunshines() {
        return AramiaFireWorks.getInstance().getSunsPercentage();
    }

    public void addSunshines(final int kegs) {
        AramiaFireWorks.getInstance().giveSuns(c.getPlayer(), kegs);
    }

    public final short getDecorations() {
        return AramiaFireWorks.getInstance().getDecsPercentage();
    }

    public void addDecorations(final int kegs) {
        try {
            AramiaFireWorks.getInstance().giveDecs(c.getPlayer(), kegs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final MapleInventory getInventory(int type) {//判断背包
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) type));
    }

    public final MapleCarnivalParty getCarnivalParty() {//嘉年华类，判断什么不为空
        return c.getPlayer().getCarnivalParty();
    }

    public final MapleCarnivalChallenge getNextCarnivalRequest() {//嘉年华类
        return c.getPlayer().getNextCarnivalRequest();
    }

    public final MapleCarnivalChallenge getCarnivalChallenge(MapleCharacter chr) {//让对方打开NPC？？嘉年华类
        return new MapleCarnivalChallenge(chr);
    }

    public void setHP(short hp) {//设置当前生命值
        c.getPlayer().getStat().setHp(hp);
    }

    public void 增加角色最大生命值(short hp) {//设置当前生命值
        c.getPlayer().getStat().setHp(hp);
    }

    public void 增加角色最大法力值(short MP) {//设置当前生命值
        c.getPlayer().getStat().setMp(MP);
    }

    public void maxStats() {//设置最大属性
        List<Pair<MapleStat, Integer>> statup = new ArrayList<Pair<MapleStat, Integer>>(2);
        c.getPlayer().getStat().setStr((short) 32767);
        c.getPlayer().getStat().setDex((short) 32767);
        c.getPlayer().getStat().setInt((short) 32767);
        c.getPlayer().getStat().setLuk((short) 32767);

        c.getPlayer().getStat().setMaxHp((short) 30000);
        c.getPlayer().getStat().setMaxMp((short) 30000);
        c.getPlayer().getStat().setHp((short) 30000);
        c.getPlayer().getStat().setMp((short) 30000);

        statup.add(new Pair<MapleStat, Integer>(MapleStat.STR, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.DEX, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.LUK, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.INT, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, Integer.valueOf(30000)));

        c.sendPacket(MaplePacketCreator.updatePlayerStats(statup, c.getPlayer().getJob()));
    }

    public Pair<String, Map<Integer, String>> getSpeedRun(String typ) {//地图人数？
        final SpeedRunType type = SpeedRunType.valueOf(typ);
        if (SpeedRunner.getInstance().getSpeedRunData(type) != null) {
            return SpeedRunner.getInstance().getSpeedRunData(type);
        }
        return new Pair<String, Map<Integer, String>>("", new HashMap<Integer, String>());
    }

    public boolean getSR(Pair<String, Map<Integer, String>> ma, int sel) {
        if (ma.getRight().get(sel) == null || ma.getRight().get(sel).length() <= 0) {
            dispose();
            return false;
        }
        sendOk(ma.getRight().get(sel));
        return true;
    }

    public Equip getEquip(int itemid) {//物品输出
        return (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
    }

    public void setExpiration(Object statsSel, long expire) {//设置物品时间
        if (statsSel instanceof Equip) {
            ((Equip) statsSel).setExpiration(System.currentTimeMillis() + (expire * 24 * 60 * 60 * 1000));
        }
    }

    public void setLock(Object statsSel) {//设置社么
        if (statsSel instanceof Equip) {
            Equip eq = (Equip) statsSel;
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
        }
    }

    public boolean addFromDrop(Object statsSel) {
        if (statsSel instanceof IItem) {
            final IItem it = (IItem) statsSel;
            return MapleInventoryManipulator.checkSpace(getClient(), it.getItemId(), it.getQuantity(), it.getOwner()) && MapleInventoryManipulator.addFromDrop(getClient(), it, false);
        }
        return false;
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type) {
        return replaceItem(slot, invType, statsSel, offset, type, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type, boolean takeSlot) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        IItem item = getPlayer().getInventory(inv).getItem((byte) slot);
        if (item == null || statsSel instanceof IItem) {
            item = (IItem) statsSel;
        }
        if (offset > 0) {
            if (inv != MapleInventoryType.EQUIP) {
                return false;
            }
            Equip eq = (Equip) item;
            if (takeSlot) {
                if (eq.getUpgradeSlots() < 1) {
                    return false;
                } else {
                    eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() - 1));
                }
            }
            if (type.equalsIgnoreCase("Slots")) {
                eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() + offset));
            } else if (type.equalsIgnoreCase("Level")) {
                eq.setLevel((byte) (eq.getLevel() + offset));
            } else if (type.equalsIgnoreCase("Hammer")) {
                eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
            } else if (type.equalsIgnoreCase("STR")) {
                eq.setStr((short) (eq.getStr() + offset));
            } else if (type.equalsIgnoreCase("DEX")) {
                eq.setDex((short) (eq.getDex() + offset));
            } else if (type.equalsIgnoreCase("INT")) {
                eq.setInt((short) (eq.getInt() + offset));
            } else if (type.equalsIgnoreCase("LUK")) {
                eq.setLuk((short) (eq.getLuk() + offset));
            } else if (type.equalsIgnoreCase("HP")) {
                eq.setHp((short) (eq.getHp() + offset));
            } else if (type.equalsIgnoreCase("MP")) {
                eq.setMp((short) (eq.getMp() + offset));
            } else if (type.equalsIgnoreCase("WATK")) {
                eq.setWatk((short) (eq.getWatk() + offset));
            } else if (type.equalsIgnoreCase("MATK")) {
                eq.setMatk((short) (eq.getMatk() + offset));
            } else if (type.equalsIgnoreCase("WDEF")) {
                eq.setWdef((short) (eq.getWdef() + offset));
            } else if (type.equalsIgnoreCase("MDEF")) {
                eq.setMdef((short) (eq.getMdef() + offset));
            } else if (type.equalsIgnoreCase("ACC")) {
                eq.setAcc((short) (eq.getAcc() + offset));
            } else if (type.equalsIgnoreCase("Avoid")) {
                eq.setAvoid((short) (eq.getAvoid() + offset));
            } else if (type.equalsIgnoreCase("Hands")) {
                eq.setHands((short) (eq.getHands() + offset));
            } else if (type.equalsIgnoreCase("Speed")) {
                eq.setSpeed((short) (eq.getSpeed() + offset));
            } else if (type.equalsIgnoreCase("Jump")) {
                eq.setJump((short) (eq.getJump() + offset));
            } else if (type.equalsIgnoreCase("ItemEXP")) {
                eq.setItemEXP(eq.getItemEXP() + offset);
            } else if (type.equalsIgnoreCase("Expiration")) {
                eq.setExpiration((long) (eq.getExpiration() + offset));
            } else if (type.equalsIgnoreCase("Flag")) {
                eq.setFlag((byte) (eq.getFlag() + offset));
            }
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
            item = eq.copy();
        }
        MapleInventoryManipulator.removeFromSlot(getClient(), inv, (short) slot, item.getQuantity(), false);
        return MapleInventoryManipulator.addFromDrop(getClient(), item, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int upgradeSlots) {
        return replaceItem(slot, invType, statsSel, upgradeSlots, "Slots");
    }

    public boolean isCash(final int itemId) {
        return MapleItemInformationProvider.getInstance().isCash(itemId);
    }

    public boolean 时装(final int itemId) {
        return MapleItemInformationProvider.getInstance().isCash(itemId);
    }

    public void buffGuild(final int buff, final int duration, final String msg) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.getItemEffect(buff) != null && getPlayer().getGuildId() > 0) {
            final MapleStatEffect mse = ii.getItemEffect(buff);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr.getGuildId() == getPlayer().getGuildId()) {
                        mse.applyTo(chr, chr, true, null, duration);
                        chr.dropMessage(5, "Your guild has gotten a " + msg + " buff.");
                    }
                }
            }
        }
    }

    public boolean createAlliance(String alliancename) {
        MapleParty pt = c.getPlayer().getParty();
        MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterById(pt.getMemberByIndex(1).getId());
        if (otherChar == null || otherChar.getId() == c.getPlayer().getId()) {
            return false;
        }
        try {
            return World.Alliance.createAlliance(alliancename, c.getPlayer().getId(), otherChar.getId(), c.getPlayer().getGuildId(), otherChar.getGuildId());
        } catch (Exception re) {
            re.printStackTrace();
            return false;
        }
    }

    public boolean addCapacityToAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.changeAllianceCapacity(gs.getAllianceId())) {
                    gainMeso(-MapleGuildAlliance.CHANGE_CAPACITY_COST);
                    return true;
                }
            }
        } catch (Exception re) {
            re.printStackTrace();
        }
        return false;
    }

    public boolean disbandAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.disbandAlliance(gs.getAllianceId())) {
                    return true;
                }
            }
        } catch (Exception re) {
            re.printStackTrace();
        }
        return false;
    }

    public byte getLastMsg() {
        return lastMsg;
    }

    public final void setLastMsg(final byte last) {
        this.lastMsg = last;
    }

    public final void maxAllSkills() {
        for (ISkill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId())) { //no db/additionals/resistance skills
                teachSkill(skil.getId(), skil.getMaxLevel(), skil.getMaxLevel());
            }
        }
    }

    public final void resetStats(int str, int dex, int z, int luk) {
        c.getPlayer().resetStats(str, dex, z, luk);
    }

    public final boolean dropItem(int slot, int invType, int quantity) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        return MapleInventoryManipulator.drop(c, inv, (short) slot, (short) quantity, true);
    }

    public final List<Integer> getAllPotentialInfo() {
        return new ArrayList<Integer>(MapleItemInformationProvider.getInstance().getAllPotentialInfo().keySet());
    }

    public final String getPotentialInfo(final int id) {//修改装备脚本
        final List<StructPotentialItem> potInfo = MapleItemInformationProvider.getInstance().getPotentialInfo(id);
        final StringBuilder builder = new StringBuilder("#b#ePOTENTIAL INFO FOR ID: ");
        builder.append(id);
        builder.append("#n#k\r\n\r\n");
        int minLevel = 1, maxLevel = 10;
        for (StructPotentialItem item : potInfo) {
            builder.append("#eLevels ");
            builder.append(minLevel);
            builder.append("~");
            builder.append(maxLevel);
            builder.append(": #n");
            builder.append(item.toString());
            minLevel += 10;
            maxLevel += 10;
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public final void sendRPS() {
        c.sendPacket(MaplePacketCreator.getRPSMode((byte) 8, -1, -1, -1));
    }

    public final void setQuestRecord(Object ch, final int questid, final String data) {
        ((MapleCharacter) ch).getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(data);
    }

    public final void doWeddingEffect(final Object ch) {//结婚类
        final MapleCharacter chr = (MapleCharacter) ch;
        // getMap().broadcastMessage(MaplePacketCreator.yellowChat(getPlayer().getName() + ", do you take " + chr.getName() + " as your wife and promise to stay beside her through all downtimes, crashes, and lags?"));
        CloneTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (chr == null || getPlayer() == null) {
                    warpMap(680000500, 0);
                } else {
                    // getMap().broadcastMessage(MaplePacketCreator.yellowChat(chr.getName() + ", do you take " + getPlayer().getName() + " as your husband and promise to stay beside him through all downtimes, crashes, and lags?"));
                }
            }
        }, 10000);
        CloneTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (chr == null || getPlayer() == null) {
                    if (getPlayer() != null) {
                        setQuestRecord(getPlayer(), 160001, "3");
                        setQuestRecord(getPlayer(), 160002, "0");
                    } else if (chr != null) {
                        setQuestRecord(chr, 160001, "3");
                        setQuestRecord(chr, 160002, "0");
                    }
                    warpMap(680000500, 0);
                } else {
                    setQuestRecord(getPlayer(), 160001, "2");
                    setQuestRecord(chr, 160001, "2");
                    sendNPCText(getPlayer().getName() + " and " + chr.getName() + ",我希望你们两个都好你的旅程!", 9201002);
                    getMap().startExtendedMapEffect("你现在可以吻新娘 " + getPlayer().getName() + "!", 5120006);
                    if (chr.getGuildId() > 0) {
                        World.Guild.guildPacket(chr.getGuildId(), MaplePacketCreator.sendMarriage(false, chr.getName()));
                    }
                    if (chr.getFamilyId() > 0) {
                        World.Family.familyPacket(chr.getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), chr.getId());
                    }
                    if (getPlayer().getGuildId() > 0) {
                        World.Guild.guildPacket(getPlayer().getGuildId(), MaplePacketCreator.sendMarriage(false, getPlayer().getName()));
                    }
                    if (getPlayer().getFamilyId() > 0) {
                        World.Family.familyPacket(getPlayer().getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), getPlayer().getId());
                    }
                }
            }
        }, 20000); //10 sec 10 sec
    }

    public void openDD(int type) {//打开豆豆机
        c.sendPacket(MaplePacketCreator.openBeans(getPlayer().getBeans(), type));
        //c.sendPacket(MaplePacketCreator.testPacket(HexTool.getByteArrayFromHexString("5B 01 01 00 00 00 00 04 00 61 61 61 61")));
    }

    public void 快递(int type) {//打开豆豆机
        c.sendPacket(MaplePacketCreator.openBeans(getPlayer().getBeans(), type));
    }

    public void 小纸条(String type1, String type2) {
        c.getPlayer().sendNote(type1, type2);
    }

    public void 群输出信息(String type) {//打开豆豆机
        if (gui.Start.ConfigValuesMap.get("QQ机器人开关") == 0) {
            if (MapleParty.机器人待机 == 0) {
                sendMsgToQQGroup(type);
            }
        }
    }

    public void Gainrebirth(String Name, int Channale, int Piot) {
        try {
            int ret = Getrebirth(Name, Channale);
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO rebirth (channel, Name,Point) VALUES (?, ?, ?)");
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
            PreparedStatement ps = con.prepareStatement("UPDATE rebirth SET `Point` = ? WHERE Name = ? and channel = ?");
            ps.setInt(1, ret);
            ps.setString(2, Name);
            ps.setInt(3, Channale);
            ps.execute();
            ps.close();
        } catch (SQLException sql) {
            System.err.println("Getrebirth!!55" + sql);
        }
    }

    public int Getrebirth(String Name, int Channale) {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM rebirth WHERE channel = ? and Name = ?");
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

    public void 私聊输出信息(String type1, String type2) {//c.getPlayer().sendNote("[信息推送]", "这是一段测试信息");
        if (gui.Start.ConfigValuesMap.get("QQ机器人开关") == 0) {
            if (MapleParty.机器人待机 == 0) {
                sendMsg(type1, type2);
            }
        }
    }

    public void 动态数据(String type1) {
        gui.Start.ConfigValuesMap.get(type1);
    }

    public void worldMessage(String text) {//公告类型
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, text));
    }

    public void 全服公告(String text) {//公告类型
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, text));
    }

    public void 全服喇叭(String text) {//公告类型
        World.Broadcast.broadcastMessage(MaplePacketCreator.黄色喇叭(text));
    }

    public int getBeans() {//判断豆豆
        return getClient().getPlayer().getBeans();
    }

    public int 判断豆豆数量() {//判断豆豆
        return getClient().getPlayer().getBeans();
    }

    public void gainBeans(int s) {//给豆豆
        getPlayer().gainBeans(s);
        c.sendPacket(MaplePacketCreator.updateBeans(c.getPlayer().getId(), s));
    }

    public void 给豆豆(int s) {//给豆豆
        getPlayer().gainBeans(s);
        c.sendPacket(MaplePacketCreator.updateBeans(c.getPlayer().getId(), s));
    }

    public void 收豆豆(int s) {//给豆豆
        getPlayer().gainBeans(-s);
        c.sendPacket(MaplePacketCreator.updateBeans(c.getPlayer().getId(), s));
    }

    public int getHyPay(int type) {//判断HYPAY
        return getPlayer().getHyPay(type);
    }

    public void szhs(String ss) {//8级？？
        c.sendPacket(MaplePacketCreator.游戏屏幕中间黄色字体(ss));
    }

    public void 测试发包(int effect) {//8级？？
        c.sendPacket(MaplePacketCreator.showSpecialEffect(effect));
    }

    public void szhs(String ss, int id) {
        c.sendPacket(MaplePacketCreator.游戏屏幕中间黄色字体(ss, id));
    }

    public int gainHyPay(int hypay) {//给hypay
        return getPlayer().gainHyPay(hypay);
    }

    public void 强化加卷次数(int upgr) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setUpgradeSlots((byte) (item.getUpgradeSlots() + upgr));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void gainEquiPproperty(int upgr, int watk, int matk, int str, int dex, int Int, int luk, int hp, int mp, int acc, int avoid) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setUpgradeSlots((byte) (item.getUpgradeSlots() + upgr));
        item.setWatk((short) (item.getWatk() + watk));
        item.setMatk((short) (item.getMatk() + matk));
        item.setStr((short) (item.getStr() + str));
        item.setDex((short) (item.getDex() + dex));
        item.setInt((short) (item.getInt() + Int));
        item.setLuk((short) (item.getLuk() + luk));
        item.setHp((short) (item.getHp() + hp));
        item.setMp((short) (item.getMp() + mp));
        item.setAcc((byte) (item.getAcc() + acc));
        item.setAvoid((byte) (item.getAvoid() + avoid));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public String 显示装备属性() {
        StringBuilder name = new StringBuilder();
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        if (item.getUpgradeSlots() > 0) {
            name.append("升级次数:#b" + item.getUpgradeSlots() + "#k\r\n");
        }
        if (item.getWatk() > 0) {
            name.append("物理攻击力:#b" + item.getWatk() + "#k\r\n");
        }
        if (item.getMatk() > 0) {
            name.append("魔法攻击力:#b" + item.getMatk() + "#k\r\n");
        }
        if (item.getWdef() > 0) {
            name.append("物理防御力:#b" + item.getWdef() + "#k\r\n");
        }
        if (item.getMdef() > 0) {
            name.append("魔法防御力:#b" + item.getMdef() + "#k\r\n");
        }
        if (item.getStr() > 0) {
            name.append("力量:#b" + item.getStr() + "#k\r\n");
        }
        if (item.getDex() > 0) {
            name.append("敏捷:#b" + item.getDex() + "#k\r\n");
        }
        if (item.getLuk() > 0) {
            name.append("运气:#b" + item.getLuk() + "#k\r\n");
        }
        if (item.getInt() > 0) {
            name.append("智力:#b" + item.getInt() + "#k\r\n");
        }
        if (item.getHp() > 0) {
            name.append("HP:#b" + item.getHp() + "#k\r\n");
        }
        if (item.getMp() > 0) {
            name.append("MP:#b" + item.getMp() + "#k\r\n");
        }
        if (item.getAcc() > 0) {
            name.append("命中率:#b" + item.getAcc() + "#k\r\n");
        }
        if (item.getAvoid() > 0) {
            name.append("闪避率:#b" + item.getAvoid() + "#k\r\n");
        }
        if (item.getSpeed() > 0) {
            name.append("移动速度:#b" + item.getSpeed() + "#k\r\n");
        }
        return name.toString();
    }

    public void 获取装备栏物品代码() {
        c.getPlayer().dropMessage(5, "" + ((Equip) c.getPlayer().getInventory(MapleInventoryType.装备栏).getItem((short) 1)) + "");
    }

    //部位。属性，成功率，值
    public void 强化穿戴装备(int aa, int bb, int cc, int dd) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) aa).copy();

    }

    public void 装备洗练() {

        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();

        if (item.getUpgradeSlots() > 0) {
            int getUpgradeSlots = (int) Math.ceil(Math.random() * (item.getUpgradeSlots() + item.getUpgradeSlots() * 0.5));
            item.setUpgradeSlots((byte) getUpgradeSlots);
        }
        if (item.getWatk() > 0) {
            int getWatk = (int) Math.ceil(Math.random() * (item.getWatk() + item.getWatk() * 0.5));
            item.setWatk((byte) getWatk);
        }
        if (item.getMatk() > 0) {
            int getMatk = (int) Math.ceil(Math.random() * (item.getMatk() + item.getMatk() * 0.5));
            item.setMatk((byte) getMatk);
        }
        if (item.getWdef() > 0) {
            int getWdef = (int) Math.ceil(Math.random() * (item.getWdef() + item.getWdef() * 0.5));
            item.setWdef((byte) getWdef);
        }
        if (item.getMdef() > 0) {
            int getMdef = (int) Math.ceil(Math.random() * (item.getMdef() + item.getMdef() * 0.5));
            item.setMdef((byte) getMdef);
        }
        if (item.getStr() > 0) {
            int getStr = (int) Math.ceil(Math.random() * (item.getStr() + item.getStr() * 0.5));
            item.setStr((byte) getStr);
        }
        if (item.getDex() > 0) {
            int getDex = (int) Math.ceil(Math.random() * (item.getDex() + item.getDex() * 0.5));
            item.setDex((byte) getDex);
        }
        if (item.getLuk() > 0) {
            int getLuk = (int) Math.ceil(Math.random() * (item.getLuk() + item.getLuk() * 0.5));
            item.setLuk((byte) getLuk);
        }
        if (item.getInt() > 0) {
            int getInt = (int) Math.ceil(Math.random() * (item.getInt() + item.getInt() * 0.5));
            item.setInt((byte) getInt);
        }
        if (item.getHp() > 0) {
            int getHp = (int) Math.ceil(Math.random() * (item.getHp() + item.getHp() * 0.5));
            item.setHp((byte) getHp);
        }
        if (item.getMp() > 0) {
            int getMp = (int) Math.ceil(Math.random() * (item.getMp() + item.getMp() * 0.5));
            item.setMp((byte) getMp);
        }
        if (item.getAcc() > 0) {
            int getAcc = (int) Math.ceil(Math.random() * (item.getAcc() + item.getAcc() * 0.5));
            item.setAcc((byte) getAcc);
        }
        if (item.getAvoid() > 0) {
            int getAvoid = (int) Math.ceil(Math.random() * (item.getAvoid() + item.getAvoid() * 0.5));
            item.setAvoid((byte) getAvoid);
        }
        if (item.getSpeed() > 0) {
            int getSpeed = (int) Math.ceil(Math.random() * (item.getSpeed() + item.getSpeed() * 0.5));
            item.setSpeed((byte) getSpeed);
        }
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }


    /*
    1.类型，
    2.数值，
    3.概率，
    4.
     */
    public void 强化装备1(int aa, int bb, int cc, int dd) {

    }

    /*
    //强化第一件装备属性,加法力值
     */
    public void 加最大法力值(int mp) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setMp((short) (item.getMp() + mp));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加最大法力值(int mp, int a) {
        final double 概率 = Math.ceil(Math.random() * 100);
        if (概率 <= a) {
            Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
            item.setMp((short) (item.getMp() + mp));
            MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
            MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
        } else {
            c.getPlayer().dropMessage(1, "强化失败。");
        }
    }

    public void 加最大法力值2(int mp) {
        final double 随机 = Math.ceil(Math.random() * mp);
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setMp((short) (item.getMp() + 随机));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加最大法力值2(int mp, int a) {
        final double 随机 = Math.ceil(Math.random() * mp);
        final double 概率 = Math.ceil(Math.random() * 100);
        if (概率 <= a) {
            Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
            item.setMp((short) (item.getMp() + 随机));
            MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
            MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
        } else {
            c.getPlayer().dropMessage(1, "强化失败。");
        }
    }

    /*
    //强化第一件装备属性,加生命值
     */
    public void 加宝石(int a) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setMpR((short) (item.getMpR() + a));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 装备耐久(int a, int b) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) a).copy();
        item.setHpR((short) (item.getHpR() + b));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIPPED, (short) a, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加最大生命值(int hp) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setHp((short) (item.getHp() + hp));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加运气(int luk) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setLuk((short) (item.getLuk() + luk));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加智力(int Int) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setInt((short) (item.getInt() + Int));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加敏捷(int dex) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setDex((short) (item.getDex() + dex));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加力量(int str) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setStr((short) (item.getStr() + str));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加命中率(int Acc) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setAcc((short) (item.getAcc() + Acc));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加跳跃力(int Jump) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setJump((short) (item.getJump() + Jump));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加移动速度(int Speed) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setSpeed((short) (item.getSpeed() + Speed));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加闪避率(int Avoid) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setAvoid((short) (item.getAvoid() + Avoid));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加魔法攻击(int matk) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setMatk((short) (item.getMatk() + matk));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加魔法防御(int Mdef) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setMdef((short) (item.getMdef() + Mdef));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加物理攻击(int watk) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setWatk((short) (item.getWatk() + watk));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加物理防御(int Wdef) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setWdef((short) (item.getWdef() + Wdef));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public void 加升级次数(int upgr) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        item.setUpgradeSlots((byte) (item.getUpgradeSlots() + upgr));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    /*
    强化规则
     */
    //提取强化规则
    public void 强化规则(int a) {
        //判断装备栏第一格是否有道具
        if (c.getPlayer().getInventory(MapleInventoryType.装备栏).getItem((short) 1) == null) {
            c.getPlayer().dropMessage(1, "装备栏第一格没有道具。");
            return;
        }
        //读取装备栏装备道具信息
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();
        //数据值类型
        byte 可升级次数 = 0;
        int HP, MP, 力量, 敏捷, 智力, 运气, 物理攻击力, 物理防御力, 魔法攻击力, 魔法防御力, 回避率, 命中率, 跳跃力, 移动速度;
        int 成功率, 随机固定值;
        //初始化数据
        HP = 0;
        MP = 0;
        力量 = 0;
        敏捷 = 0;
        智力 = 0;
        运气 = 0;
        物理攻击力 = 0;
        物理防御力 = 0;
        魔法攻击力 = 0;
        魔法防御力 = 0;
        回避率 = 0;
        命中率 = 0;
        跳跃力 = 0;
        移动速度 = 0;
        成功率 = 0;
        随机固定值 = 0;
        //从数据库提取信息
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Strengthening WHERE id2 = " + a + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                可升级次数 = (byte) rs.getInt("可升级次数");
                HP = rs.getInt("HP");
                MP = rs.getInt("MP");
                力量 = rs.getInt("力量");
                敏捷 = rs.getInt("敏捷");
                智力 = rs.getInt("智力");
                运气 = rs.getInt("运气");
                物理攻击力 = rs.getInt("物理攻击力");
                物理防御力 = rs.getInt("物理防御力");
                魔法攻击力 = rs.getInt("魔法攻击力");
                魔法防御力 = rs.getInt("魔法防御力");
                回避率 = rs.getInt("回避率");
                命中率 = rs.getInt("命中率");
                跳跃力 = rs.getInt("跳跃力");
                移动速度 = rs.getInt("移动速度");
                成功率 = rs.getInt("成功率");
                随机固定值 = rs.getInt("随机值");//0随机，1固定
            }
        } catch (SQLException ex) {
        }
        //提取信息后，开始运作
        if (成功率 <= Math.ceil(Math.random() * 100)) {
            if (随机固定值 == 1) {
                item.setUpgradeSlots((byte) (item.getUpgradeSlots() + 可升级次数));
                item.setHp((short) (item.getHp() + HP));
                item.setMp((short) (item.getMp() + MP));
                item.setWatk((short) (item.getWatk() + 物理攻击力));
                item.setWdef((short) (item.getWdef() + 物理防御力));
                item.setMdef((short) (item.getMdef() + 魔法防御力));
                item.setMatk((short) (item.getMatk() + 魔法攻击力));
                item.setStr((short) (item.getStr() + 力量));
                item.setDex((short) (item.getDex() + 敏捷));
                item.setInt((short) (item.getInt() + 智力));
                item.setLuk((short) (item.getLuk() + 运气));
                item.setAcc((byte) (item.getAcc() + 命中率));
                item.setAvoid((byte) (item.getAvoid() + 回避率));
                item.setJump((byte) (item.getJump() + 跳跃力));
                item.setSpeed((byte) (item.getSpeed() + 移动速度));
                c.getPlayer().dropMessage(1, "[强化成功]:\r\n"
                        + "HP + " + HP + ""
                        + "MP + " + MP + ""
                        + "力量 + " + 力量 + ""
                        + "敏捷 + " + 敏捷 + ""
                        + "智力 + " + 智力 + ""
                        + "运气 + " + 运气 + ""
                        + "命中率 + " + 命中率 + ""
                        + "回避率 + " + 回避率 + ""
                        + "跳跃力 + " + 跳跃力 + ""
                        + "移动速度 + " + 移动速度 + ""
                        + "物理攻击力 + " + 物理攻击力 + ""
                        + "物理防御力 + " + 物理防御力 + ""
                        + "魔法攻击力 + " + 魔法攻击力 + ""
                        + "魔法防御力 + " + 魔法防御力 + ""
                        + "");
                MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
                MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
            } else {
                item.setUpgradeSlots((byte) (item.getUpgradeSlots() + Math.ceil(Math.random() * 可升级次数)));
                item.setHp((short) (item.getHp() + Math.ceil(Math.random() * HP)));
                item.setMp((short) (item.getMp() + Math.ceil(Math.random() * MP)));
                item.setWatk((short) (item.getWatk() + Math.ceil(Math.random() * 物理攻击力)));
                item.setWdef((short) (item.getWdef() + Math.ceil(Math.random() * 物理防御力)));
                item.setMdef((short) (item.getMdef() + Math.ceil(Math.random() * 魔法防御力)));
                item.setMatk((short) (item.getMatk() + Math.ceil(Math.random() * 魔法攻击力)));
                item.setStr((short) (item.getStr() + Math.ceil(Math.random() * 力量)));
                item.setDex((short) (item.getDex() + Math.ceil(Math.random() * 敏捷)));
                item.setInt((short) (item.getInt() + Math.ceil(Math.random() * 智力)));
                item.setLuk((short) (item.getLuk() + Math.ceil(Math.random() * 运气)));
                item.setAcc((byte) (item.getAcc() + Math.ceil(Math.random() * 命中率)));
                item.setAvoid((byte) (item.getAvoid() + Math.ceil(Math.random() * 回避率)));
                item.setJump((byte) (item.getJump() + Math.ceil(Math.random() * 跳跃力)));
                item.setSpeed((byte) (item.getSpeed() + Math.ceil(Math.random() * 移动速度)));
                c.getPlayer().dropMessage(1, "[强化成功]:\r\n"
                        + "HP + " + HP + ""
                        + "MP + " + MP + ""
                        + "力量 + " + 力量 + ""
                        + "敏捷 + " + 敏捷 + ""
                        + "智力 + " + 智力 + ""
                        + "运气 + " + 运气 + ""
                        + "命中率 + " + 命中率 + ""
                        + "回避率 + " + 回避率 + ""
                        + "跳跃力 + " + 跳跃力 + ""
                        + "移动速度 + " + 移动速度 + ""
                        + "物理攻击力 + " + 物理攻击力 + ""
                        + "物理防御力 + " + 物理防御力 + ""
                        + "魔法攻击力 + " + 魔法攻击力 + ""
                        + "魔法防御力 + " + 魔法防御力 + ""
                        + "");
            }
            MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
            MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
        } else {
            c.getPlayer().dropMessage(1, "强化失败了");
        }

    }

    public void 魔方(int upgr, int watk, int matk, int str, int dex, int Int, int luk, int hp, int mp, int acc, int avoid) {
        Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) 1).copy();

        item.setUpgradeSlots((byte) (item.getUpgradeSlots() + upgr));
        item.setWatk((short) (item.getWatk() + watk));
        item.setMatk((short) (item.getMatk() + matk));
        item.setStr((short) (item.getStr() + str));
        item.setDex((short) (item.getDex() + dex));
        item.setInt((short) (item.getInt() + Int));
        item.setLuk((short) (item.getLuk() + luk));
        item.setHp((short) (item.getHp() + hp));
        item.setMp((short) (item.getMp() + mp));
        item.setAcc((byte) (item.getAcc() + acc));
        item.setAvoid((byte) (item.getAvoid() + avoid));
        MapleInventoryManipulator.removeFromSlot(getC(), MapleInventoryType.EQUIP, (short) 1, (short) 1, true);
        MapleInventoryManipulator.addFromDrop(getChar().getClient(), item, false);
    }

    public int addHyPay(int hypay) {
        return getPlayer().addHyPay(hypay);
    }

    public int delPayReward(int pay) {
        return getPlayer().delPayReward(pay);
    }

    public int getItemLevel(int id) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return ii.getReqLevel(id);
    }

    public void alatPQ() {
        //   c.sendPacket(MaplePacketCreator.updateAriantPQRanking(getText, npc, pendingDisposal))
    }

    public void xlkc(long days) {
        MapleQuestStatus marr = getPlayer().getQuestNoAdd(MapleQuest.getInstance(122700));
        if ((marr != null) && (marr.getCustomData() != null) && (Long.parseLong(marr.getCustomData()) >= System.currentTimeMillis())) {
            getPlayer().dropMessage(1, "项链扩充失败，您已经进行过项链扩充。");
        } else {
            String customData = String.valueOf(System.currentTimeMillis() + days * 24L * 60L * 60L * 1000L);
            getPlayer().getQuestNAdd(MapleQuest.getInstance(122700)).setCustomData(customData);
            getPlayer().dropMessage(1, "项链" + days + "扩充扩充成功！");
        }
    }

    public String 道具掉落(int itemid) {
        int rate = getClient().getChannelServer().getDropRate();
        MapleMonster mob = MapleLifeFactory.getMonster(itemid);
        if (MapleLifeFactory.getMonster(itemid) != null) {
            if (mob.getStats().isBoss()) {
                rate = getClient().getChannelServer().getBossDropRate();
            }
        }
        List ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(itemid);
        if ((ranks != null) && (ranks.size() > 0)) {
            int num = 0;
            int itemId = 0;
            int ch = 0;

            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                MonsterDropEntry de = (MonsterDropEntry) ranks.get(i);
                if ((de.chance > 0) && ((de.questid <= 0) || ((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0)))) {
                    itemId = de.itemId;
                    if (!ii.itemExists(itemId)) {
                        continue;
                    }
                    if (num == 0) {
                        name.append(" #r#v").append(itemid).append("##k 的掉落出处:\r\n");
                        name.append("\r\n");
                    }
                    String namez = new StringBuilder().append("#z").append(itemId).append("#").toString();
                    if (itemId == 0) {
                        itemId = 4031041;
                        namez = new StringBuilder().append(de.Minimum * getClient().getChannelServer().getMesoRate()).append(" - ").append(de.Maximum * getClient().getChannelServer().getMesoRate()).append(" 的金币").toString();
                    }
                    ch = de.chance * rate;
                    //  if (getPlayer().isAdmin()) {
                    name.append(num + 1).append(".  #b#o").append(itemId).append("#").append(namez).append("#k 爆率: ").append(Integer.valueOf(ch >= 999999 ? 1000000 : ch).doubleValue() / 10000.0D).append(" %").append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    // } else {
                    //     name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    // }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "没有找到这个道具的掉落信息。";
    }

    /**
     * 查询怪物掉落
     *
     * @param mobId
     * @return
     */
    public String checkDrop(int mobId) {
        int rate = getClient().getChannelServer().getDropRate();
        MapleMonster mob = MapleLifeFactory.getMonster(mobId);
        if (MapleLifeFactory.getMonster(mobId) != null) {
            if (mob.getStats().isBoss()) {
                rate = getClient().getChannelServer().getBossDropRate();
            }
        }
        List ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        if ((ranks != null) && (ranks.size() > 0)) {
            int num = 0;
            int itemId = 0;
            int ch = 0;

            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                MonsterDropEntry de = (MonsterDropEntry) ranks.get(i);
                if ((de.chance > 0) && ((de.questid <= 0) || ((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0)))) {
                    itemId = de.itemId;
                    if (!ii.itemExists(itemId)) {
                        continue;
                    }
                    if (num == 0) {
                        name.append("#r" + MapleParty.开服名字 + "#k#k #b#o").append(mobId).append("##k 的爆物详细为:\r\n\r\n");
                        name.append("\r\n");
                    }
                    String namez = new StringBuilder().append("#z").append(itemId).append("#").toString();
                    if (itemId == 0) {
                        itemId = 4031041;
                        namez = new StringBuilder().append(de.Minimum * getClient().getChannelServer().getMesoRate()).append(" - ").append(de.Maximum * getClient().getChannelServer().getMesoRate()).append(" 的金币").toString();
                    }
                    ch = de.chance * rate;
                    if (getPlayer().isAdmin()) {
                        name.append(num + 1).append(".  #b#v").append(itemId).append("#").append(namez).append("#k 爆率: ").append(Integer.valueOf(ch >= 999999 ? 1000000 : ch).doubleValue() / 10000.0D).append(" %").append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    } else {
                        name.append(num + 1).append(".  #b#v").append(itemId).append("#").append(namez).append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "没有找到这个怪物的爆率数据。";
    }

    public String checkMapDrop() {
        List ranks = new ArrayList(MapleMonsterInformationProvider.getInstance().getGlobalDrop());
        int mapid = this.c.getPlayer().getMap().getId();
        int cashServerRate = getClient().getChannelServer().getCashRate();
        int globalServerRate = 1;
        if ((ranks != null) && (ranks.size() > 0)) {
            int num = 0;

            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                MonsterGlobalDropEntry de = (MonsterGlobalDropEntry) ranks.get(i);
                //if ((de.continent < 0) || ((de.continent < 10) && (mapid / 100000000 == de.continent)) || ((de.continent < 100) && (mapid / 10000000 == de.continent)) || ((de.continent < 1000) && (mapid / 1000000 == de.continent))) {

                int itemId = de.itemId;
                if (num == 0) {
                    name.append("#r" + MapleParty.开服名字 + "#k全局爆物预览；\r\n\r\n");
                    /*name.append("当前地图 #r").append(mapid).append("#k - #m").append(mapid).append("# 的全局爆率为:");
                        name.append("\r\n--------------------------------------\r\n");*/
                }

                String names = new StringBuilder().append("#z").append(itemId).append("#").toString();
                if ((itemId == 0) && (cashServerRate != 0)) {
                    itemId = 4031041;
                    names = new StringBuilder().append(de.Minimum * cashServerRate).append(" - ").append(de.Maximum * cashServerRate).append(" 的抵用卷").toString();
                }
                int chance = de.chance * globalServerRate;
                if (getPlayer().isAdmin()) {
                    name.append(num + 1).append(".  #b#v").append(itemId).append("#").append(names).append("#k 爆率: ").append(Integer.valueOf(chance >= 999999 ? 1000000 : chance).doubleValue() / 10000.0D).append(" %").append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                } else {
                    name.append(num + 1).append(".  #b#v").append(itemId).append("#").append(names).append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                }
                num++;
                //}
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "全局爆物数据为空。";
    }

    public String 查询物品出处(int account) {
        List ranks = new ArrayList(MapleMonsterInformationProvider.getInstance().getGlobalDrop());
        int mapid = this.c.getPlayer().getMap().getId();
        int cashServerRate = getClient().getChannelServer().getCashRate();
        int globalServerRate = 1;
        if ((ranks != null) && (ranks.size() > 0)) {
            int num = 0;

            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                MonsterGlobalDropEntry de = (MonsterGlobalDropEntry) ranks.get(i);
                //if ((de.continent < 0) || ((de.continent < 10) && (mapid / 100000000 == de.continent)) || ((de.continent < 100) && (mapid / 10000000 == de.continent)) || ((de.continent < 1000) && (mapid / 1000000 == de.continent))) {

                int itemId = de.itemId;
                if (num == 0) {
                    name.append("#r" + MapleParty.开服名字 + "#k全局爆物预览；\r\n\r\n");
                }

                String names = new StringBuilder().append("#z").append(itemId).append("#").toString();
                int chance = de.chance * globalServerRate;
                if (getPlayer().isAdmin()) {
                    name.append(num + 1).append(".  #b#v").append(itemId).append("#").append(names).append("#k 爆率: ").append(Integer.valueOf(chance >= 999999 ? 1000000 : chance).doubleValue() / 10000.0D).append(" %").append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                } else {
                    name.append(num + 1).append(".  #b#v").append(itemId).append("#").append(names).append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                }
                num++;
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "未查询到掉落。";
    }

    public int getzb() {//判断账号的ID？
        int money = 0;
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement limitCheck = con.prepareStatement("SELECT * FROM accounts WHERE id=" + cid + "");
            ResultSet rs = limitCheck.executeQuery();
            if (rs.next()) {
                money = rs.getInt("money");
            }
            limitCheck.close();
            rs.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return money;
    }

    public int 获取当前星期() {

        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

    }

    public void setzb(int slot) {
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE accounts SET money =money+ " + slot + " WHERE id = " + cid + "")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public int getmoneyb() {
        int moneyb = 0;
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            ResultSet rs;
            try (PreparedStatement limitCheck = con.prepareStatement("SELECT * FROM accounts WHERE id=" + cid + "")) {
                rs = limitCheck.executeQuery();
                if (rs.next()) {
                    moneyb = rs.getInt("moneyb");
                }
            }
            rs.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return moneyb;
    }

    public void setmoneyb(int slot) {
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET moneyb =moneyb+ " + slot + " WHERE id = " + cid + "");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public MapleMapFactory getMapFactory() {
        return getClient().getChannelServer().getMapFactory();
    }

    public void warpBack(int mid, final int retmap, final int time) {/////定时传送

        MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(mid);
        c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
        c.sendPacket(MaplePacketCreator.getClock(time));
        Timer.EventTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(retmap);
                if (c.getPlayer() != null) {
                    c.sendPacket(MaplePacketCreator.stopClock());
                    c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
                    //c.getPlayer().dropMessage(6, "到达目的地");
                }
            }
        }, 1000 * time);
    }

    public void 定时传送(int mid, final int retmap, final int time) {/////定时传送

        MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(mid);
        c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
        c.sendPacket(MaplePacketCreator.getClock(time));
        Timer.EventTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(retmap);
                if (c.getPlayer() != null) {
                    c.sendPacket(MaplePacketCreator.stopClock());
                    c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
                    //c.getPlayer().dropMessage(6, "到达目的地");
                }
            }
        }, 1000 * time);
    }

    public void 物品掉落() {

    }

//        public void 物品掉落() {
//        client.inventory.Item item1 = new client.inventory.Item(2000000, (byte) 0, (short) 1);
//        client.inventory.Item item2 = new client.inventory.Item(2000001, (byte) 0, (short) 1);
//        client.inventory.Item item3 = new client.inventory.Item(2000002, (byte) 0, (short) 1);
//        client.inventory.Item item4 = new client.inventory.Item(2000003, (byte) 0, (short) 1);
//        client.inventory.Item item5 = new client.inventory.Item(2000004, (byte) 0, (short) 1);
//        client.inventory.Item item6 = new client.inventory.Item(2000005, (byte) 0, (short) 1);
//        client.inventory.Item item7 = new client.inventory.Item(2000006, (byte) 0, (short) 1);
//        c.getPlayer().getMap().物品掉落(c.getPlayer(), c.getPlayer(), item1, new Point(c.getPlayer().getPosition().x, c.getPlayer().getPosition().y), false, false);
//        c.getPlayer().getMap().物品掉落(c.getPlayer(), c.getPlayer(), item2, new Point(c.getPlayer().getPosition().x + 20, c.getPlayer().getPosition().y), false, false);
//        c.getPlayer().getMap().物品掉落(c.getPlayer(), c.getPlayer(), item3, new Point(c.getPlayer().getPosition().x - 20, c.getPlayer().getPosition().y), false, false);
//        c.getPlayer().getMap().物品掉落(c.getPlayer(), c.getPlayer(), item4, new Point(c.getPlayer().getPosition().x + 40, c.getPlayer().getPosition().y), false, false);
//        c.getPlayer().getMap().物品掉落(c.getPlayer(), c.getPlayer(), item5, new Point(c.getPlayer().getPosition().x - 40, c.getPlayer().getPosition().y), false, false);
//        c.getPlayer().getMap().物品掉落(c.getPlayer(), c.getPlayer(), item6, new Point(c.getPlayer().getPosition().x + 60, c.getPlayer().getPosition().y), false, false);
//        c.getPlayer().getMap().物品掉落(c.getPlayer(), c.getPlayer(), item7, new Point(c.getPlayer().getPosition().x - 60, c.getPlayer().getPosition().y), false, false);
//    }
    public void 判断是否结婚() {
        c.getPlayer().getMarriageId();
    }

    public void 召唤假人(int a) {
        c.getPlayer().召唤假人(a);
    }

    public void 玩家名字() {
        c.getPlayer().getName();
    }

    public void warpMapWithClock(final int mid, int seconds) {
        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getClock(seconds));
        Timer.MapTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (c.getPlayer() != null) {
                    for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                        chr.changeMap(mid);
                    }
                }
            }
        }, seconds * 1000);
    }

    public void 剑客排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().剑客()));
    }

    public void 勇士排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().勇士()));
    }

    public void 英雄排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().英雄()));
    }

    public void 准骑士排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().准骑士()));
    }

    public void 骑士排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().骑士()));
    }

    public void 圣骑士排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().圣骑士()));
    }

    public void 枪战士排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().枪战士()));
    }

    public void 龙骑士排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().龙骑士()));
    }

    public void 黑骑士排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().黑骑士()));
    }

    public void 火毒法师排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().火毒法师()));
    }

    public void 火毒巫师排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().火毒巫师()));
    }

    public void 火毒魔导师排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().火毒魔导师()));
    }

    public void 冰雷法师排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().冰雷法师()));
    }

    public void 冰雷巫师排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().冰雷巫师()));
    }

    public void 冰雷魔导师排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().冰雷魔导师()));
    }

    public void 牧师排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().牧师()));
    }

    public void 祭师排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().祭师()));
    }

    public void 主教排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().主教()));
    }

    public void 猎人排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().猎人()));
    }

    public void 射手排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().射手()));
    }

    public void 神射手排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().神射手()));
    }

    public void 弩弓手排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().弩弓手()));
    }

    public void 游侠排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().游侠()));
    }

    public void 箭神排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().箭神()));
    }

    public void 刺客排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().刺客()));
    }

    public void 无影人排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().无影人()));
    }

    public void 隐士排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().隐士()));
    }

    public void 侠客排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().侠客()));
    }

    public void 独行客排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().独行客()));
    }

    public void 侠盗排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().侠盗()));
    }

    public void 拳手排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().拳手()));
    }

    public void 斗士排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().斗士()));
    }

    public void 冲锋队长排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().冲锋队长()));
    }

    public void 火枪手排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().火枪手()));
    }

    public void 大副排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().大副()));
    }

    public void 船长排行榜() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().船长()));
    }

    public void showlvl() {//等级榜
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().getLevelRank()));
    }

    public void showmeso() {//金币榜
        c.sendPacket(MaplePacketCreator.showmesoRanks(npc, MapleGuildRanking.getInstance().getMesoRank()));
    }

    public void ShowMarrageEffect() {
        c.getPlayer().getMap().broadcastMessage((MaplePacketCreator.sendMarrageEffect()));
    }

//1点卷/2抵用/3金币/4经验
    public int 给全服发点卷(int 数量, int 类型) {
        int count = 0;

        try {
            if (数量 <= 0 || 类型 <= 0) {
                return 0;
            }

            if (类型 == 1 || 类型 == 2) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.modifyCSPoints(类型, 数量);
                        String cash = null;
                        if (类型 == 1) {
                            cash = "点卷";
                        } else if (类型 == 2) {
                            cash = "抵用卷";
                        }
                        //mch.startMapEffect("管理员发放" + 数量 + cash + "给在线的所有玩家！快感谢管理员吧！", 5121009);
                        count++;
                    }
                }
            } else if (类型 == 3) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        // mch.modifyCSPoints(类型, 数量);
                        mch.gainMeso(数量, true);
                        //mch.startMapEffect("管理员发放" + 数量 + "冒险币给在线的所有玩家！快感谢管理员吧！", 5121009);
                        count++;
                    }
                }
            } else if (类型 == 4) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.gainExp(数量, true, false, true);
                        //mch.startMapEffect("管理员发放" + 数量 + "经验给在线的所有玩家！快感谢管理员吧！", 5121009);
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("给全服发点卷出错：" + e.getMessage());
        }

        return count;
    }

//1点卷/2抵用/3金币/4经验
    public int 给当前地图发点卷(int 数量, int 类型) {
        int count = 0;
        int mapId = c.getPlayer().getMapId();
        try {
            if (数量 <= 0 || 类型 <= 0) {
                return 0;
            }

            if (类型 == 1 || 类型 == 2) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        if (mch.getMapId() != mapId) {
                            continue;
                        }
                        mch.modifyCSPoints(类型, 数量);
                        String cash = null;
                        if (类型 == 1) {
                            cash = "点卷";
                        } else if (类型 == 2) {
                            cash = "抵用卷";
                        }
                        //mch.startMapEffect("管理员发放" + 数量 + cash + "给在线的所有玩家！快感谢管理员吧！", 5121009);
                        count++;
                    }
                }
            } else if (类型 == 3) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        if (mch.getMapId() != mapId) {
                            continue;
                        }
                        mch.gainMeso(数量, true);
                        //mch.startMapEffect("管理员发放" + 数量 + "冒险币给在线的所有玩家！快感谢管理员吧！", 5121009);
                        count++;
                    }
                }
            } else if (类型 == 4) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        if (mch.getMapId() != mapId) {
                            continue;
                        }
                        mch.gainExp(数量, true, false, true);
                        //mch.startMapEffect("管理员发放" + 数量 + "经验给在线的所有玩家！快感谢管理员吧！", 5121009);
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("给当前地图发点卷出错：" + e.getMessage());
        }

        return count;
    }

//1点卷/2抵用/3金币/4经验
    public int 给当前频道发点卷(int 数量, int 类型) {
        int count = 0;
        int chlId = c.getPlayer().getMap().getChannel();

        try {
            if (数量 <= 0 || 类型 <= 0) {
                return 0;
            }
            if (类型 == 1 || 类型 == 2) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    if (cserv1.getChannel() != chlId) {
                        continue;
                    }
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.modifyCSPoints(类型, 数量);
                        String cash = null;
                        if (类型 == 1) {
                            cash = "点卷";
                        } else if (类型 == 2) {
                            cash = "抵用卷";
                        }
                        //mch.startMapEffect("管理员发放" + 数量 + cash + "给在线的所有玩家！快感谢管理员吧！", 5121009);
                        count++;
                    }
                }
            } else if (类型 == 3) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    if (cserv1.getChannel() != chlId) {
                        continue;
                    }
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        // mch.modifyCSPoints(类型, 数量);
                        mch.gainMeso(数量, true);
                        //mch.startMapEffect("管理员发放" + 数量 + "冒险币给在线的所有玩家！快感谢管理员吧！", 5121009);
                        count++;
                    }
                }
            } else if (类型 == 4) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    if (cserv1.getChannel() != chlId) {
                        continue;
                    }
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.gainExp(数量, true, false, true);
                        //mch.startMapEffect("管理员发放" + 数量 + "经验给在线的所有玩家！快感谢管理员吧！", 5121009);
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("给当前频道发点卷出错：" + e.getMessage());
        }

        return count;
    }

    public int 给全服发物品(int 物品ID, int 数量, int 力量, int 敏捷, int 智力, int 运气, int HP, int MP, int 可加卷次数, String 制作人名字, int 给予时间, String 是否可以交易, int 攻击力, int 魔法力, int 物理防御, int 魔法防御) {
        int count = 0;

        try {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType type = GameConstants.getInventoryType(物品ID);

            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (数量 >= 0) {
                        if (!MapleInventoryManipulator.checkSpace(mch.getClient(), 物品ID, 数量, "")) {
                            return 0;
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
                            MapleInventoryManipulator.addById(mch.getClient(), 物品ID, (short) 数量, "", null, 给予时间, (byte) 0);
                        }
                    } else {
                        MapleInventoryManipulator.removeById(mch.getClient(), GameConstants.getInventoryType(物品ID), 物品ID, -数量, true, false);
                    }
                    mch.getClient().sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 数量, true));

                    count++;
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("给全服发物品出错：" + e.getMessage());
        }

        return count;
    }

    public int 给当前地图发物品(int 物品ID, int 数量, int 力量, int 敏捷, int 智力, int 运气, int HP, int MP, int 可加卷次数, String 制作人名字, int 给予时间, String 是否可以交易, int 攻击力, int 魔法力, int 物理防御, int 魔法防御) {
        int count = 0;
        int mapId = c.getPlayer().getMapId();

        try {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType type = GameConstants.getInventoryType(物品ID);

            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (mch.getMapId() != mapId) {
                        continue;
                    }
                    if (数量 >= 0) {
                        if (!MapleInventoryManipulator.checkSpace(mch.getClient(), 物品ID, 数量, "")) {
                            return 0;
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
                            MapleInventoryManipulator.addById(mch.getClient(), 物品ID, (short) 数量, "", null, 给予时间, (byte) 0);
                        }
                    } else {
                        MapleInventoryManipulator.removeById(mch.getClient(), GameConstants.getInventoryType(物品ID), 物品ID, -数量, true, false);
                    }
                    mch.getClient().sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 数量, true));

                    count++;
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("给当前地图发物品出错：" + e.getMessage());
        }

        return count;
    }

    public int 给当前频道发物品(int 物品ID, int 数量, int 力量, int 敏捷, int 智力, int 运气, int HP, int MP, int 可加卷次数, String 制作人名字, int 给予时间, String 是否可以交易, int 攻击力, int 魔法力, int 物理防御, int 魔法防御) {
        int count = 0;
        int chlId = c.getPlayer().getMap().getChannel();

        try {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType type = GameConstants.getInventoryType(物品ID);

            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                if (cserv1.getChannel() != chlId) {
                    continue;
                }
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (数量 >= 0) {
                        if (!MapleInventoryManipulator.checkSpace(mch.getClient(), 物品ID, 数量, "")) {
                            return 0;
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
                            MapleInventoryManipulator.addById(mch.getClient(), 物品ID, (short) 数量, "", null, 给予时间, (byte) 0);
                        }
                    } else {
                        MapleInventoryManipulator.removeById(mch.getClient(), GameConstants.getInventoryType(物品ID), 物品ID, -数量, true, false);
                    }
                    mch.getClient().sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 数量, true));

                    count++;
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("给当前频道发物品出错：" + e.getMessage());
        }

        return count;
    }

    public int 是否是认证玩家() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as DATA FROM 会员 WHERE name = ?")) {
                ps.setString(1, getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("DATA");
                    }
                }
            }
        } catch (SQLException Ex) {
            System.err.println("查询认证玩家出错 - 数据库查询失败：" + Ex);
        }
        return 0;
    }

    public int 强化() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as DATA FROM 强化会员 WHERE name = ?")) {
                ps.setString(1, getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("DATA");
                    }
                }
            }
        } catch (SQLException Ex) {
            System.err.println("查询认证玩家出错 - 数据库查询失败：" + Ex);
        }

        return 0;
    }

    public int 音乐() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as DATA FROM 音乐会员 WHERE name = ?")) {
                ps.setString(1, getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("DATA");
                    }
                }
            }
        } catch (SQLException Ex) {
            System.err.println("查询认证玩家出错 - 数据库查询失败：" + Ex);
        }

        return 0;
    }

    public int 减伤装备() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as DATA FROM 减伤装备 WHERE name = ?")) {
                ps.setString(1, getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("DATA");
                    }
                }
            }
        } catch (SQLException Ex) {
            System.err.println("查询认证玩家出错 - 数据库查询失败：" + Ex);
        }

        return 0;
    }

    public int 白名单() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as DATA FROM 白名单 WHERE name = ?")) {
                ps.setString(1, getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("DATA");
                    }
                }
            }
        } catch (SQLException Ex) {
            System.err.println("查询白名单出错 - 数据库查询失败：" + Ex);
        }

        return 0;
    }

    public boolean 判断当前地图是否已禁用此脚本(int scriptId) {
        try {
            int mapId = c.getPlayer().getMapId();
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as DATA FROM 禁用脚本地图 WHERE scriptId = ? AND mapId = ?")) {
                ps.setInt(1, scriptId);
                ps.setInt(2, mapId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("DATA") > 0;
                    }
                }
            }
        } catch (SQLException Ex) {
            System.err.println("判断当前地图是否已禁用此脚本出错 - 数据库查询失败：" + Ex);
        }

        return false;
    }

    public int 认证主播() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as DATA FROM 认证主播 WHERE name = ?")) {
                ps.setString(1, getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("DATA");
                    }
                }
            }
        } catch (SQLException Ex) {
            System.err.println("查询认证主播出错 - 数据库查询失败：" + Ex);
        }

        return 0;
    }

    public int 禁用脚本地图() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) as DATA FROM 禁用脚本地图 WHERE name = ?")) {
                ps.setString(1, getmapId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("DATA");
                    }
                }
            }
        } catch (SQLException Ex) {
            System.err.println("查询认证玩家出错 - 数据库查询失败：" + Ex);
        }

        return 0;
    }

    public int 传送当前地图所有人到指定地图(int destMapId, Boolean includeSelf) {
        int count = 0;
        int myMapId = c.getPlayer().getMapId();
        int myId = c.getPlayer().getId();

        try {
            final MapleMap tomap = getMapFactory().getMap(destMapId);
            final MapleMap frommap = getMapFactory().getMap(myMapId);
            List<MapleCharacter> list = frommap.getCharactersThreadsafe();
            if (tomap != null && frommap != null && list != null && frommap.getCharactersSize() > 0) {
                for (MapleMapObject mmo : list) {
                    MapleCharacter chr = (MapleCharacter) mmo;
                    if (chr.getId() == myId) { // 自己
                        if (includeSelf) {
                            chr.changeMap(tomap, tomap.getPortal(0));
                            count++;
                        }
                    } else {
                        chr.changeMap(tomap, tomap.getPortal(0));
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("传送当前地图所有人到指定地图出错：" + e.getMessage());
        }

        return count;
    }

    public int 杀死当前地图所有人(Boolean includeSelf) {
        int count = 0;
        int myMapId = c.getPlayer().getMapId();
        int myId = c.getPlayer().getId();

        try {
            final MapleMap frommap = getMapFactory().getMap(myMapId);
            List<MapleCharacter> list = frommap.getCharactersThreadsafe();
            if (frommap != null && list != null && frommap.getCharactersSize() > 0) {
                for (MapleMapObject mmo : list) {
                    if (mmo != null) {
                        MapleCharacter chr = (MapleCharacter) mmo;
                        if (chr.getId() == myId) { // 自己
                            if (includeSelf) {
                                chr.setHp(0);
                                chr.updateSingleStat(MapleStat.HP, 0);
                                count++;
                            }
                        } else {
                            chr.setHp(0);
                            chr.updateSingleStat(MapleStat.HP, 0);
                            count++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("杀死当前地图所有人出错：" + e.getMessage());
        }

        return count;
    }

    public int 复活当前地图所有人(Boolean includeSelf) {
        int count = 0;
        int myMapId = c.getPlayer().getMapId();
        int myId = c.getPlayer().getId();

        try {
            final MapleMap frommap = getMapFactory().getMap(myMapId);
            List<MapleCharacter> list = frommap.getCharactersThreadsafe();
            if (frommap != null && list != null && frommap.getCharactersSize() > 0) {
                for (MapleMapObject mmo : list) {
                    if (mmo != null) {
                        MapleCharacter chr = (MapleCharacter) mmo;
                        if (chr.getId() == myId) { // 自己
                            if (includeSelf) {
                                chr.getStat().setHp(chr.getStat().getMaxHp());
                                chr.updateSingleStat(MapleStat.HP, chr.getStat().getMaxHp());
                                chr.getStat().setMp(chr.getStat().getMaxMp());
                                chr.updateSingleStat(MapleStat.MP, chr.getStat().getMaxMp());
                                chr.dispelDebuffs();
                                count++;
                            }
                        } else {
                            chr.getStat().setHp(chr.getStat().getMaxHp());
                            chr.updateSingleStat(MapleStat.HP, chr.getStat().getMaxHp());
                            chr.getStat().setMp(chr.getStat().getMaxMp());
                            chr.updateSingleStat(MapleStat.MP, chr.getStat().getMaxMp());
                            chr.dispelDebuffs();
                            count++;
                        }

                    }
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("复活当前地图所有人出错：" + e.getMessage());
        }

        return count;
    }

    public void 跟踪玩家(String charName) {
        for (ChannelServer chl : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : chl.getPlayerStorage().getAllCharacters()) {
                if (chr.getName() == charName) {
                    c.getPlayer().changeMap(chr.getMapId());
                }
            }
        }
    }

    public int 杀人(int mapId, int max) {
        int count = 0;

        if (mapId < 1) {
            mapId = c.getPlayer().getMapId();
        }

        int myId = c.getPlayer().getId();

        try {
            final MapleMap frommap = getMapFactory().getMap(mapId);
            List<MapleCharacter> list = frommap.getCharactersThreadsafe();
            if (list != null && frommap.getCharactersSize() > 0) {
                for (MapleMapObject mmo : list) {
                    if (mmo != null && count < max) {
                        MapleCharacter chr = (MapleCharacter) mmo;
                        if (chr.getId() != myId) {
                            chr.setHp(0);
                            chr.updateSingleStat(MapleStat.HP, 0);
                            count++;
                            this.setBossRankCount("被杀");
                        }
                    }
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("杀人出错：" + e.getMessage());
        }

        return count;
    }

    public int 给指定地图发物品(int 地图ID, int 物品ID, int 数量, int 力量, int 敏捷, int 智力, int 运气, int HP, int MP, int 可加卷次数, String 制作人名字, int 给予时间, String 是否可以交易, int 攻击力, int 魔法力, int 物理防御, int 魔法防御) {
        int count = 0;

        if (地图ID < 1) {
            地图ID = c.getPlayer().getMapId();
        }

        try {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            MapleInventoryType type = GameConstants.getInventoryType(物品ID);

            final MapleMap frommap = getMapFactory().getMap(地图ID);
            List<MapleCharacter> list = frommap.getCharactersThreadsafe();
            if (list != null && frommap.getCharactersSize() > 0) {
                for (MapleMapObject mmo : list) {
                    if (mmo != null) {
                        MapleCharacter chr = (MapleCharacter) mmo;
                        if (数量 >= 0) {
                            if (!MapleInventoryManipulator.checkSpace(chr.getClient(), 物品ID, 数量, "")) {
                                return 0;
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
                                    chr.dropMessage(5, msg);
                                }
                                MapleInventoryManipulator.addbyItem(chr.getClient(), item.copy());
                            } else {
                                MapleInventoryManipulator.addById(chr.getClient(), 物品ID, (short) 数量, "", null, 给予时间, (byte) 0);
                            }
                        } else {
                            MapleInventoryManipulator.removeById(chr.getClient(), GameConstants.getInventoryType(物品ID), 物品ID, -数量, true, false);
                        }
                        chr.getClient().sendPacket(MaplePacketCreator.getShowItemGain(物品ID, (short) 数量, true));

                        count++;
                    }
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("给指定地图发物品出错：" + e.getMessage());
        }

        return count;
    }

    public int 给指定地图发物品(int 地图ID, int 物品ID, int 数量) {
        return 给指定地图发物品(地图ID, 物品ID, 数量, 0, 0, 0, 0, 0, 0, 0, "", 0, "", 0, 0, 0, 0);
    }

    //1点卷/2抵用/3金币/4经验
    public int 给指定地图发点卷(int 地图ID, int 数量, int 类型) {
        int count = 0;

        String name = c.getPlayer().getName();

        if (地图ID < 1) {
            地图ID = c.getPlayer().getMapId();
        }

        try {
            if (数量 <= 0 || 类型 <= 0) {
                return 0;
            }

            final MapleMap frommap = getMapFactory().getMap(地图ID);
            List<MapleCharacter> list = frommap.getCharactersThreadsafe();
            if (list != null && frommap.getCharactersSize() > 0) {
                if (类型 == 1 || 类型 == 2) {
                    for (MapleMapObject mmo : list) {
                        if (mmo != null) {
                            MapleCharacter chr = (MapleCharacter) mmo;
                            chr.modifyCSPoints(类型, 数量);
                            String cash = null;
                            if (类型 == 1) {
                                cash = "点卷";
                            } else if (类型 == 2) {
                                cash = "抵用卷";
                            }

                            //  chr.startMapEffect(name + "为你发放了" + 数量 + cash + "！", 5121009);
                            count++;
                        }
                    }
                } else if (类型 == 3) {
                    for (MapleMapObject mmo : list) {
                        if (mmo != null) {
                            MapleCharacter chr = (MapleCharacter) mmo;
                            chr.gainMeso(数量, true);
                            //  chr.startMapEffect(name + "为你发放了" + 数量 + "金币！", 5121009);
                            count++;
                        }
                    }
                } else if (类型 == 4) {
                    for (MapleMapObject mmo : list) {
                        if (mmo != null) {
                            MapleCharacter chr = (MapleCharacter) mmo;
                            chr.gainExp(数量, true, false, true);
                            //   chr.startMapEffect(name + "为你发放了" + 数量 + "经验值！", 5121009);
                            count++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            c.getPlayer().dropMessage("给指定地图发点卷出错：" + e.getMessage());
        }

        return count;
    }

    public int 获取指定地图玩家数量(int mapId) {//var count = cm.获取指定地图玩家数量(910000000);
        return getMapFactory().getMap(mapId).characterSize();
    }

    public int 判断指定地图玩家数量(int mapId) {//var count = cm.获取指定地图玩家数量(910000000);
        return getMapFactory().getMap(mapId).characterSize();
    }

    public void 给指定地图发公告(int mapId, String msg, int itemId) {//cm.给指定地图发公告(910000000)
        getMapFactory().getMap(mapId).startMapEffect(msg, itemId);
    }

    public String getServerName() {
        return MapleParty.开服名字;//var MC = cm.getServerName();
    }

    public String getbanben() {
        return ServerProperties.getProperty("ZEV.banben");//var MC = cm.getbanben();
    }

    public void 克隆() {
        c.getPlayer().cloneLook();
    }

    public void 取消克隆() {
        c.getPlayer().disposeClones();
    }

    public void 设置天气(int 天气ID) {
        if (c.getPlayer().getMap().getPermanentWeather() > 0) {
            c.getPlayer().getMap().setPermanentWeather(0);
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeMapEffect());
        } else if (!MapleItemInformationProvider.getInstance().itemExists(天气ID) || 天气ID / 10000 != 512) {
            c.getPlayer().dropMessage(5, "无效的天气ID。");
        } else {
            c.getPlayer().getMap().setPermanentWeather(天气ID);
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 天气ID, false));
            c.getPlayer().dropMessage(5, "地图天气已启用。");
        }
    }

    public void MapleMSpvpkills() {
        MapleGuildRanking.MapleMSpvpkills(c, this.npc);
    }

    public void MapleMSpvpdeaths() {
        MapleGuildRanking.MapleMSpvpdeaths(c, this.npc);
    }

    public void 爆物开关() {
        c.getPlayer().getMap().toggleDrops();
    }

    public long 查看蓄力一击() {
        return (System.currentTimeMillis() - c.getPlayer().蓄力一击) / 10;
    }

    public void 给人气(int r) {
        c.getPlayer().addFame(r);
    }

    public void 判断人气() {
        c.getPlayer().getFame();
    }

    public void 发言(String 内容) {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (MapleCharacter victim : cserv.getPlayerStorage().getAllCharacters()) {
                if (victim.getId() != c.getPlayer().getId()) {
                    victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(内容, 1), victim.isGM(), 0));
                }
            }
        }
    }

    public void 回收地图() {
        if (判断地图(c.getPlayer().getMapId()) <= 0) {
            final int 地图 = c.getPlayer().getMapId();
            记录地图(地图);
            c.getPlayer().dropMessage(1, "回收成功，此地图将在 5 分钟后被回收。");
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 60 * 5);
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                                if (chr == null) {
                                    continue;
                                }
                                if (chr.getMapId() == 地图) {
                                    chr.getClient().getSession().close();
                                }
                            }
                        }
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            cserv.getMapFactory().destroyMap(地图, true);
                            cserv.getMapFactory().HealMap(地图);
                        }
                        删除地图(地图);
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        } else {
            c.getPlayer().dropMessage(1, "回收失败，此地图在回收队列中。");
        }
    }

    public void 回收地图(final int a) {
        if (判断地图(a) <= 0) {
            final int 地图 = a;
            记录地图(地图);
            c.getPlayer().dropMessage(1, "回收成功，此地图将在 1 小时后被重置。");
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 60 * 1);
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            cserv.getMapFactory().destroyMap(地图, true);
                            cserv.getMapFactory().HealMap(地图);
                        }
                        删除地图(地图);
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        } else {
            c.getPlayer().dropMessage(1, "回收失败，此地图在回收队列中。");
        }
    }

    public void 记录地图(int a) {
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO map (id) VALUES ( ?)")) {
            ps.setInt(1, a);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {

        }
    }

    public void 删除地图(int a) {
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM map where id =" + a + "");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlstr = " Delete from map where id = '" + a + "'";
                ps1.executeUpdate(sqlstr);
            }
        } catch (SQLException ex) {

        }
    }

    public int 判断地图(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM map where id =" + a + "");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data += 1;
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public void 人气排行榜() {
        MapleGuild.人气排行(getClient(), this.npc);
    }

    public void 声望排行榜() {
        MapleGuild.声望排行(getClient(), this.npc);
    }

    public void 豆豆排行榜() {
        MapleGuild.豆豆排行(getClient(), this.npc);
    }

    public void 战斗力排行榜() {
        MapleGuild.战斗力排行(getClient(), this.npc);
    }

    public void 总在线时间排行榜() {
        MapleGuild.总在线时间排行(getClient(), this.npc);
    }

    public int 查询今日在线时间() {
        int data = 0;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement psu = con.prepareStatement("SELECT todayOnlineTime FROM characters WHERE id = ?");
            psu.setInt(1, c.getPlayer().getId());
            ResultSet rs = psu.executeQuery();
            if (rs.next()) {
                data = rs.getInt("todayOnlineTime");
            }
            rs.close();
            psu.close();

        } catch (SQLException ex) {
            System.err.println("查询今日在线时间出错：" + ex.getMessage());
        }

        return data;
    }

    public int 查询总在线时间() {
        int data = 0;

        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement psu = con.prepareStatement("SELECT totalOnlineTime FROM characters WHERE id = ?");
            psu.setInt(1, c.getPlayer().getId());
            ResultSet rs = psu.executeQuery();
            if (rs.next()) {
                data = rs.getInt("totalOnlineTime");
            }
            rs.close();
            psu.close();
        } catch (SQLException ex) {
            System.err.println("查询总在线时间出错：" + ex.getMessage());
        }

        return data;
    }

    public int 查询在线人数() {
        int count = 0;
        for (ChannelServer chl : ChannelServer.getAllInstances()) {
            count += chl.getPlayerStorage().getAllCharacters().size();
        }
        return count;
    }

    /**
     * 獲取指定類型小遊戲積分
     *
     * @param gameType 1 五子棋 GameConstants.OMOK_SCORE = 122200; //2 記憶大考驗
     * GameConstants.MATCH_SCORE = 122210;
     * @param top 最大顯示前多少名
     * @return
     */
    public List<MiniGamePoints> getMiniGameTop(int gameType, int top) {
        return RankManager.getInstance().getTop(gameType, top);
    }

    private String getmapId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    //角色ID。背包类型。背包位置cm.判断背包位置是否有物品(角色ID,类型,位置)
    public static int 判断背包位置是否有物品(int a, int b, int c) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE characterid =" + a + " && inventorytype = " + b + " && position = " + c + "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("判断背包位置是否有物品 - 数据库查询失败：" + Ex);
        }
        return data;
    }
    //背包类型，背包位置

    public static int 判断背包位置代码(int a, int b, int c) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE characterid =" + a + " && inventorytype = " + b + " && position = " + c + "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("itemid");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("判断背包位置是否有物品 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    //角色ID，装备代码,穿戴类型
    public static int 判断玩家是否穿戴某装备(int a, int b, int c) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE characterid =" + a + " && itemid = " + b + " && inventorytype = " + c + "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("判断玩家是否穿戴某装备 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    public static int 获取最高玩家等级() {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT MAX(level) as DATA FROM characters WHERE gm = 0");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取最高玩家等级出错 - 数据库查询失败：" + Ex);
        }

        return data;
    }

    public static String 账号取绑定QQ(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT qq as DATA FROM accounts WHERE name = ?");
            ps.setString(1, id);
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

    public static String 账号ID取绑定QQ(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT qq as DATA FROM accounts WHERE id = ?");
            ps.setString(1, id);
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

    /*     public static int 获取最高新手玩家等级() {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT MAX(level) as DATA FROM characters WHERE gm = 0  WHERE job = 100");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取最高新手玩家等级出错 - 数据库查询失败：" + Ex);
        }

        return data;
    }*/
    public static String 获取最高等级玩家名字() {
        String name = "";
        String level = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `name`, `level` FROM characters WHERE gm = 0 ORDER BY `level` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                    level = rs.getString("level");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族名称出错 - 数据库查询失败：" + Ex);
        }

        return String.format("%s", name);
    }

    public static int 获取最高等级() {
        int level = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT  `level` FROM characters WHERE gm = 0 ORDER BY `level` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    level = rs.getInt("level");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族名称出错 - 数据库查询失败：" + Ex);
        }

        return level;
    }

    public static int 获取最高玩家人气() {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT MAX(fame) as DATA FROM characters WHERE gm = 0");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取最高玩家等级出错 - 数据库查询失败：" + Ex);
        }

        return data;
    }

    public static String 获取最高人气玩家名字() {
        String name = "";
        String level = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `name`, `fame` FROM characters WHERE gm = 0 ORDER BY `fame` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                    level = rs.getString("fame");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族名称出错 - 数据库查询失败：" + Ex);
        }

        return String.format("%s", name);
    }

    public static int 获取最高玩家金币() {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT MAX(meso) as DATA FROM characters WHERE gm = 0");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取最高玩家等级出错 - 数据库查询失败：" + Ex);
        }

        return data;
    }

    public static String 获取最高金币玩家名字() {
        String name = "";
        String level = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `name`, `meso` FROM characters WHERE gm = 0 ORDER BY `meso` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                    level = rs.getString("meso");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族名称出错 - 数据库查询失败：" + Ex);
        }

        return String.format("%s", name);
    }

    public static int 获取最高玩家在线() {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT MAX(totalOnlineTime) as DATA FROM characters WHERE gm = 0");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取最高玩家等级出错 - 数据库查询失败：" + Ex);
        }

        return data;
    }

    public static String 获取最高在线玩家名字() {
        String name = "";
        String level = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `name`, `totalOnlineTime` FROM characters WHERE gm = 0 ORDER BY `totalOnlineTime` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                    level = rs.getString("totalOnlineTime");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族名称出错 - 数据库查询失败：" + Ex);
        }

        return String.format("%s", name);
    }

    public static String 获取今日在线玩家名字() {
        String name = "";
        String level = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `name`, `todayOnlineTime` FROM characters WHERE gm = 0 ORDER BY `todayOnlineTime` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                    level = rs.getString("todayOnlineTime");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族名称出错 - 数据库查询失败：" + Ex);
        }

        return String.format("%s", name);
    }

    public static String 获取最强家族名称() {
        String name = "";
        String level = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `name`, `GP` FROM guilds  ORDER BY `GP` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                    level = rs.getString("GP");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族名称出错 - 数据库查询失败：" + Ex);
        }

        return String.format("%s", name);
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

    public static String 获取家族族长备注(int guildId) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank1title as DATA FROM guilds WHERE guildid = ?");
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

    public static String 获取家族副族长备注(int guildId) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank2title as DATA FROM guilds WHERE guildid = ?");
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

    public static String 获取家族一级成员备注(int guildId) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank3title as DATA FROM guilds WHERE guildid = ?");
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

    public static String 获取家族二级成员备注(int guildId) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank4title as DATA FROM guilds WHERE guildid = ?");
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

    public static String 获取家族三级成员备注(int guildId) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank5title as DATA FROM guilds WHERE guildid = ?");
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

    public static String 获取家族族长ID(int guildId) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT leader as DATA FROM guilds WHERE guildid = ?");
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

    public static int 家族成员数(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getInt("guildid") == a) {
                        data += 1;
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族家族成员数失败：" + Ex);
        }
        return data;
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

    public static String IP取账号(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM accounts WHERE SessionIP = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data = rs.getString("DATA");
                    return data;
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

    public static String MAC取账号(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM accounts WHERE macs = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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

    public static String IP取在线值(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT loggedin as DATA FROM accounts WHERE SessionIP = " + id + "");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data = rs.getString("DATA");
                    return data;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("IP取在线值数据库查询失败：" + Ex);
        }
        if (data == null) {
            data = "匿名人士";
        }
        return data;
    }

    public static int 账号下所有角色ID(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (rs.getInt("accountid") == id) {
                        data = rs.getInt("ACash");
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("提取点券库存、出错");
        }
        return data;
    }

    public static int 提取点券库存(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts ");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getInt("id") == id) {
                        data = rs.getInt("ACash");
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("提取点券库存、出错");
        }
        return data;
    }

    public static String SN取出售(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT Point as DATA FROM character7 WHERE Name = ? && channel = 1");
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

    public static String SN取库存(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT Point as DATA FROM character7 WHERE Name = ? &&  channel = 2");
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

    public static String SN取折扣(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT Point as DATA FROM character7 WHERE Name = ? &&  channel = 3");
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

    public static String SN取限购(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT Point as DATA FROM character7 WHERE Name = ? &&  channel = 4");
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

    public static String SN取类型(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT Point as DATA FROM character7 WHERE Name = ? &&  channel = 5");
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

    public static int 角色名字取ID(String id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id as DATA FROM characters WHERE name = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取角色名字取ID出错 - 数据库查询失败：" + Ex);
        }

        return data;
    }

    public static int 角色名字取账号ID(String id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT accountid as DATA FROM characters WHERE name = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取角色名字取ID出错 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    public static String 账号ID取账号(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name as DATA FROM accounts WHERE id = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取角色名字取ID出错 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    public static String 账号ID取在线(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT loggedin as DATA FROM accounts WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取角色名字取ID出错 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    public static String 角色名字取等级(String id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT level as DATA FROM characters WHERE name = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取角色名字取ID出错 - 数据库查询失败：" + Ex);
        }
        if (data == null) {
            data = "匿名人士";
        }
        return data;
    }

    public static String 物品获取掉落怪物(int itemid) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT dropperid as DATA FROM drop_data WHERE itemid = ?");
            ps.setInt(1, itemid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取物品获取掉落怪物出错 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    /* public static String 获取最高玩家等级和名字() {
        String name = "";
        String level = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `name`, `level` FROM characters WHERE gm = 0 ORDER BY `level` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                    level = rs.getString("level");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取家族名称出错 - 数据库查询失败：" + Ex);
        }

        return String.format("第一名目前等级 %s 名字 %s", name, level);
    }*/
    public int 获取自己等级排名() {
        int DATA = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank FROM (SELECT @rownum := @rownum + 1 AS rank, `name`, `level`, `id` FROM characters, (SELECT @rownum := 0) r WHERE gm = 0 ORDER BY `level` DESC) AS T1 WHERE `id` = ?");
            ps.setInt(1, c.getPlayer().getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DATA = rs.getInt("rank");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取自己等级排名出错 - 数据库查询失败：" + Ex);
        }
        return DATA;
    }

    public int 获取自己金币排名() {
        int DATA = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank FROM (SELECT @rownum := @rownum + 1 AS rank, `name`, `meso`, `id` FROM characters, (SELECT @rownum := 0) r WHERE gm = 0 ORDER BY `meso` DESC) AS T1 WHERE `id` = ?");
            ps.setInt(1, c.getPlayer().getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DATA = rs.getInt("rank");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取自己金币排名 - 数据库查询失败：" + Ex);
        }
        return DATA;
    }

    public int 获取自己在线排名1() {
        int DATA = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank FROM (SELECT @rownum := @rownum + 1 AS rank, `name`, `totalOnlineTime`, `id` FROM characters, (SELECT @rownum := 0) r WHERE gm = 0 ORDER BY `totalOnlineTime` DESC) AS T1 WHERE `id` = ?");
            ps.setInt(1, c.getPlayer().getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DATA = rs.getInt("rank");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取自己金币排名 - 数据库查询失败：" + Ex);
        }
        return DATA;
    }

    public int 获取自己在线排名2() {
        int DATA = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank FROM (SELECT @rownum := @rownum + 1 AS rank, `name`, `todayOnlineTime`, `id` FROM characters, (SELECT @rownum := 0) r WHERE gm = 0 ORDER BY `todayOnlineTime` DESC) AS T1 WHERE `id` = ?");
            ps.setInt(1, c.getPlayer().getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DATA = rs.getInt("rank");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取自己金币排名 - 数据库查询失败：" + Ex);
        }
        return DATA;
    }

    public int 获取自战斗力排名() {
        int DATA = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT rank FROM (SELECT @rownum := @rownum + 1 AS rank, `name`, `level`, `id` FROM characters, (SELECT @rownum := 0) r WHERE gm = 0 ORDER BY `level` DESC) AS T1 WHERE `id` = ?");
            ps.setInt(1, c.getPlayer().getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DATA = rs.getInt("rank");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("获取自己等级排名出错 - 数据库查询失败：" + Ex);
        }
        return DATA;
    }

    public String GetPlayerList() {
        String ret = "";
        ret = "#e#r <" + MapleParty.开服名字 + "> 在线玩家追踪#k#n\r\n\r\n";
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                //if (!chr.isGM()) {
                //ret = "#e#r <"+MapleParty.开服名字+"> 在线玩家追踪#k#n\r\n";
                ret += "#b#L" + chr.getId() + "#[跟踪] #d玩家:  #r" + chr.getName() + " #k#l \r\n";
                // }

            }
        }
        return ret;
    }

    public String 角色信息2(final int objectid) {
        String ret = "";
        ret = "#e#r 角色信息#k#n\r\n";
        ret += "玩家:  #r" + c.getPlayer().getMap().getCharacterById(objectid).getId() + " #k\r\n";
        ret += "玩家:  #r" + c.getPlayer().getMap().getCharacterById(objectid).getName() + " #k\r\n";

        return ret;
    }

    public MapleCharacter GetPlayer(int cid) {
        MapleCharacter ret = null;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                if (!chr.isGM()) {
                    if (chr.getId() == cid) {
                        ret = chr;
                    }
                }
            }
        }
        return ret;
    }

    public String getItemName(int id) {//物品名字
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return ii.getName(id);
        // MapleItemInformationProvider.getInstance().getName(id);
    }

    public String getip() {
        return MapleParty.IP地址;
    }

    public ArrayList<Forum_Section> getForum() {
        return Forum_Section.getAllSection();
    }

    public boolean addSection(String name) {
        return Forum_Section.addSection(name);
    }

    public boolean deleteSection(int id) {
        return Forum_Section.deleteSection(id);
    }

    public void 测试1(int id) {
        showSpecialEffect(id);
    }

    public void 升1级() {

        c.getPlayer().levelUp();

        c.getPlayer().setExp(0);
        c.getPlayer().updateSingleStat(MapleStat.EXP, 0);
    }

    public void 怪物清除() {
        double range = Double.POSITIVE_INFINITY;
        MapleMap map = c.getPlayer().getMap();
        MapleMonster mob;
        List<MapleMapObject> monsters = map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
        for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
            mob = (MapleMonster) monstermo;
            map.killMonster(mob, c.getPlayer(), true, false, (byte) 1);
        }
    }

    public void 清除装备融合材料(int cid) {

        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM bank_item2 WHERE cid = ?");
            ps1.setInt(1, cid);
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlstr = " delete from bank_item2 where cid = '" + cid + "'";
                ps1.executeUpdate(sqlstr);
            }
        } catch (SQLException ex) {
        }

    }

    public void 切换频道(int id) {
        c.getPlayer().changeChannel(id);
    }

    public void 重置扎昆重回记录(int a) {
        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 200 && Point =" + a + "")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("");
        }
    }

    public void 解散黑龙远征队() {
        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 201")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("");
        }
    }

    public void 延迟() {
        c.getPlayer().getClient().getLatency();
        return;
    }
    private final String rankTitles[] = new String[5];

    public void MapleGuild(final int guildid) {

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM guilds WHERE guildid = ?");
            ps.setInt(1, guildid);
            ResultSet rs = ps.executeQuery();
            rankTitles[0] = rs.getString("rank1title");
//            rankTitles[1] = rs.getString("rank2title");
//            rankTitles[2] = rs.getString("rank3title");
//            rankTitles[3] = rs.getString("rank4title");
//            rankTitles[4] = rs.getString("rank5title");
            rs.close();
            ps.close();
        } catch (SQLException se) {
            System.err.println("unable to read guild information from sql");
            se.printStackTrace();
        }
    }
//    public String 族长昵称() {
//       c.getPlayer().getMapleGuild().rankTitles[0];
//    }

    public void 动画(String String) {
        c.sendPacket(MaplePacketCreator.showEffect(String));//裂屏特效
        return;
    }

    public void 动画3(String data) {
        c.sendPacket(UIPacket.ShowWZEffect(data, -1));
        return;
    }

    private static void showIntro(final MapleClient c, final String data) {
        c.sendPacket(UIPacket.IntroDisableUI(true));
        c.sendPacket(UIPacket.IntroLock(true));
        c.sendPacket(UIPacket.ShowWZEffect(data, -1));
    }

    public void 动画2(String String) {
        c.sendPacket(UIPacket.AranTutInstructionalBalloon(String));

        return;
    }

    public Forum_Section getSectionById(int id) {
        return Forum_Section.getSectionById(id);
    }

    public ArrayList<Forum_Thread> getCurrentAllThread(int sid) {
        return Forum_Thread.getCurrentAllThread(sid);
    }

    public Forum_Thread getThreadById(int sid, int id) {
        return Forum_Thread.getThreadById(sid, id);
    }

    public Forum_Thread getThreadByName(int sid, String name) {
        return Forum_Thread.getThreadByName(sid, name);
    }

    public boolean addThread(int sid, String tname, int cid, String cname) {
        return Forum_Thread.addThread(sid, tname, cid, cname);
    }

    public boolean deleteThread(int sid, int tid) {
        return Forum_Thread.deleteThread(sid, tid, false);
    }

    public ArrayList<Forum_Reply> getCurrentAllReply(int tid) {
        return Forum_Reply.getCurrentAllReply(tid);
    }

    public boolean addReply(int tid, int cid, String cname, String news) {
        return Forum_Reply.addReply(tid, cid, cname, news);
    }

    public void EnterCS() {
        MapleCharacter chr = c.getPlayer();
        final ChannelServer ch = ChannelServer.getInstance(c.getChannel());

        chr.changeRemoval();

        if (chr.getMessenger() != null) {
            MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
            World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
        }
        PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
        PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), -20);
        ch.removePlayer(chr);
        c.updateLoginState(MapleClient.CHANGE_CHANNEL, c.getSessionIPAddress());

        c.sendPacket(MaplePacketCreator.getChannelChange(Integer.parseInt(CashShopServer.getIP().split(":")[1])));
        chr.saveToDB(false, false);
        chr.getMap().removePlayer(chr);
        c.setPlayer(null);
        c.setReceiving(false);
    }

    public void 设置家具(int npcId) {
        MapleNPC npc = MapleLifeFactory.getNPC(npcId);

        final int xpos = c.getPlayer().getPosition().x;
        final int ypos = c.getPlayer().getPosition().y;
        final int fh = c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId();
        npc.setPosition(c.getPlayer().getPosition());
        npc.setCy(ypos);
        npc.setRx0(xpos);
        npc.setRx1(xpos);
        npc.setFh(fh);
        npc.setCustom(true);
        try {
            com.mysql.jdbc.Connection con = (com.mysql.jdbc.Connection) DatabaseConnection.getConnection();
            try (com.mysql.jdbc.PreparedStatement ps = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("INSERT INTO 家具 (dataid, f, hide, fh, cy, rx0, rx1, type, x, y, mid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, npcId);
                ps.setInt(2, 0); // 1 = right , 0 = left
                ps.setInt(3, 0); // 1 = hide, 0 = show
                ps.setInt(4, fh);
                ps.setInt(5, ypos);
                ps.setInt(6, xpos);
                ps.setInt(7, xpos);
                ps.setString(8, "n");
                ps.setInt(9, xpos);
                ps.setInt(10, ypos);
                ps.setInt(11, c.getPlayer().getMapId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            c.getPlayer().dropMessage(6, "未能将《家具》保存到数据库");
        }
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.getMapFactory().getMap(c.getPlayer().getMapId()).addMapObject(npc);
            cserv.getMapFactory().getMap(c.getPlayer().getMapId()).broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
//                    c.getPlayer().getMap().addMapObject(npc);
//                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
        }
        //c.getPlayer().dropMessage(6, "创建家具成功，如果需要删除，请在数据库《家具》中删除，重启生效");
    }

    public void 物品摆放(int npcId) {
        MapleNPC npc = MapleLifeFactory.getNPC(npcId);

        final int xpos = c.getPlayer().getPosition().x;
        final int ypos = c.getPlayer().getPosition().y;
        final int fh = c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId();
        npc.setPosition(c.getPlayer().getPosition());
        npc.setCy(ypos);
        npc.setRx0(xpos);
        npc.setRx1(xpos);
        npc.setFh(fh);
        npc.setCustom(true);
        try {
            com.mysql.jdbc.Connection con = (com.mysql.jdbc.Connection) DatabaseConnection.getConnection();
            try (com.mysql.jdbc.PreparedStatement ps = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("INSERT INTO 物品摆放 (dataid, f, hide, fh, cy, rx0, rx1, type, x, y, mid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, npcId);
                ps.setInt(2, 0); // 1 = right , 0 = left
                ps.setInt(3, 0); // 1 = hide, 0 = show
                ps.setInt(4, fh);
                ps.setInt(5, ypos);
                ps.setInt(6, xpos);
                ps.setInt(7, xpos);
                ps.setString(8, "n");
                ps.setInt(9, xpos);
                ps.setInt(10, ypos);
                ps.setInt(11, c.getPlayer().getMapId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            c.getPlayer().dropMessage(6, "未能将《物品摆放》保存到数据库");
        }
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            cserv.getMapFactory().getMap(c.getPlayer().getMapId()).addMapObject(npc);
            cserv.getMapFactory().getMap(c.getPlayer().getMapId()).broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
//                    c.getPlayer().getMap().addMapObject(npc);
//                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
        }
        //c.getPlayer().dropMessage(6, "创建家具成功，如果需要删除，请在数据库《家具》中删除，重启生效");
    }

    public int 封号(String banPlayerName, String reason) {
        int ch = World.Find.findChannel(banPlayerName);

        StringBuilder sb = new StringBuilder(c.getPlayer().getName());
        sb.append(" banned ").append(banPlayerName).append(": ").append(reason);
        MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(banPlayerName);

        /*if (target == null || ch < 1) {
            if (MapleCharacter.ban(banPlayerName, sb.toString(), false, c.getPlayer().isAdmin() ? 250 : c.getPlayer().getGMLevel(), false)) {
                c.getPlayer().dropMessage(6, "成功离线封锁 " + banPlayerName + ".");
                return 1;
            } else {
                c.getPlayer().dropMessage(6, "封锁失败 " + banPlayerName);
                return 0;
            }
        }*/
        //if (c.getPlayer().getGMLevel() > target.getGMLevel()) {
        if (target.getGMLevel() < 0) {
            sb.append(" (IP: ").append(target.getClient().getSessionIPAddress()).append(")");
            if (target.ban(sb.toString(), c.getPlayer().isAdmin(), false, false)) {
                c.getPlayer().dropMessage(6, "[成功封锁 " + banPlayerName + ".");
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[封号系统]" + target.getName() + " 因为使用了非法软件而被永久封号。"));

                return 1;
            } else {
                c.getPlayer().dropMessage(6, "封锁失败.");
                return 0;
            }
        } else {
            c.getPlayer().dropMessage(6, "不能封锁GM...");
            return 1;
        }

    }

    public void 女神塔副本奖励() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  女神塔副本奖励 ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double 概率 = Math.ceil(Math.random() * rs.getInt("chance"));
                    double 对比 = Math.ceil(Math.random() * 100);
                    double 最少 = Math.ceil(Math.random() * rs.getInt("baseQty"));
                    double 最多 = Math.ceil(Math.random() * rs.getInt("maxRandomQty"));
                    if (最多 <= 最少) {
                        最多 = 最少;
                    }
                    if (概率 >= 100) {
                        概率 = 100;
                    }
                    if (概率 <= 对比) {
                        gainItem(rs.getInt("itemId"), (short) 最多);
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("女神塔副本奖励、出错");
        }
    }

    public void 女神塔副本奖励2() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 女神塔副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询女神塔副本奖励1之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 女神塔副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询女神塔副本奖励出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 月妙副本奖励() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  月妙副本奖励 ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double 概率 = Math.ceil(Math.random() * rs.getInt("chance"));
                    double 对比 = Math.ceil(Math.random() * 100);
                    double 最少 = Math.ceil(Math.random() * rs.getInt("baseQty"));
                    double 最多 = Math.ceil(Math.random() * rs.getInt("maxRandomQty"));
                    if (最多 <= 最少) {
                        最多 = 最少;
                    }
                    if (概率 >= 100) {
                        概率 = 100;
                    }
                    if (概率 <= 对比) {
                        gainItem(rs.getInt("itemId"), (short) 最多);
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("月妙副本奖励、出错");
        }
    }

    public void 月妙副本奖励2() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 月妙副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 月妙副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 废都副本奖励() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  废都副本奖励 ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double 概率 = Math.ceil(Math.random() * rs.getInt("chance"));
                    double 对比 = Math.ceil(Math.random() * 100);
                    double 最少 = Math.ceil(Math.random() * rs.getInt("baseQty"));
                    double 最多 = Math.ceil(Math.random() * rs.getInt("maxRandomQty"));
                    if (最多 <= 最少) {
                        最多 = 最少;
                    }
                    if (概率 >= 100) {
                        概率 = 100;
                    }
                    if (概率 <= 对比) {
                        gainItem(rs.getInt("itemId"), (short) 最多);
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("废都副本奖励、出错");
        }
    }

    public void 废都副本奖励2() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 废都副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 废都副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 测试奖励() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  测试奖励 ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double 概率 = Math.ceil(Math.random() * rs.getInt("chance"));
                    double 对比 = Math.ceil(Math.random() * 100);
                    double 最少 = Math.ceil(Math.random() * rs.getInt("baseQty"));
                    double 最多 = Math.ceil(Math.random() * rs.getInt("maxRandomQty"));
                    if (最多 <= 最少) {
                        最多 = 最少;
                    }
                    if (概率 >= 100) {
                        概率 = 100;
                    }
                    if (概率 <= 对比) {
                        gainItem(rs.getInt("itemId"), (short) 最多);
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("测试奖励、出错");
        }
    }

    public void 玩具塔副本奖励() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  玩具塔副本奖励 ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double 概率 = Math.ceil(Math.random() * rs.getInt("chance"));
                    double 对比 = Math.ceil(Math.random() * 100);
                    double 最少 = Math.ceil(Math.random() * rs.getInt("baseQty"));
                    double 最多 = Math.ceil(Math.random() * rs.getInt("maxRandomQty"));
                    if (最多 <= 最少) {
                        最多 = 最少;
                    }
                    if (概率 >= 100) {
                        概率 = 100;
                    }
                    if (概率 <= 对比) {
                        gainItem(rs.getInt("itemId"), (short) 最多);
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("玩具塔副本奖励、出错");
        }
    }

    public void 海盗副本奖励() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  海盗副本奖励 ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double 概率 = Math.ceil(Math.random() * rs.getInt("chance"));
                    double 对比 = Math.ceil(Math.random() * 100);
                    double 最少 = Math.ceil(Math.random() * rs.getInt("baseQty"));
                    double 最多 = Math.ceil(Math.random() * rs.getInt("maxRandomQty"));
                    if (最多 <= 最少) {
                        最多 = 最少;
                    }
                    if (概率 >= 100) {
                        概率 = 100;
                    }
                    if (概率 <= 对比) {
                        gainItem(rs.getInt("itemId"), (short) 最多);
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("海盗副本奖励、出错");
        }
    }

    public void 测试奖励2() {
        // itemId baseQty maxRandomQty chancechance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 测试奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 测试奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 玩具塔副本奖励2() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 玩具塔副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 玩具塔副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 海盗副本奖励2() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 海盗副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 海盗副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询海盗副本奖励出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 魔女塔奖励简单() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 魔女塔奖励简单");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道具奖励1之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 魔女塔奖励简单");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询魔女塔奖励简单出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 每日送货奖励() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  每日送货奖励 ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double 概率 = Math.ceil(Math.random() * rs.getInt("chance"));
                    double 对比 = Math.ceil(Math.random() * 100);
                    double 最少 = Math.ceil(Math.random() * rs.getInt("baseQty"));
                    double 最多 = Math.ceil(Math.random() * rs.getInt("maxRandomQty"));
                    if (最多 <= 最少) {
                        最多 = 最少;
                    }
                    if (概率 >= 100) {
                        概率 = 100;
                    }
                    if (概率 <= 对比) {
                        gainItem(rs.getInt("itemId"), (short) 最多);
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("每日送货奖励、出错");
        }
    }

    public void 每日送货奖励2() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 每日送货奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询每日送货奖励之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 每日送货奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询每日送货奖励出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 魔女塔奖励困难() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 魔女塔奖励困难");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询道魔女塔奖励困难之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 魔女塔奖励困难");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询魔女塔奖励困难出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 嘉年华胜者奖励() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  嘉年华胜者奖励 ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final double 概率 = Math.ceil(Math.random() * rs.getInt("chance"));
                    final double 对比 = Math.ceil(Math.random() * 100);
                    final double 最少 = Math.ceil(Math.random() * rs.getInt("baseQty"));
                    double 最多 = Math.ceil(Math.random() * rs.getInt("maxRandomQty"));
                    if (最多 <= 最少) {
                        最多 = 最多 + 最少;
                    }
                    if (概率 >= 对比) {
                        gainItem(rs.getInt("itemId"), (short) 最多);
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("测试奖励、出错");
        }
    }

    public void 嘉年华胜者奖励2() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 嘉年华胜者奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询每日送货奖励之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 嘉年华胜者奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询每日送货奖励出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 嘉年华败者奖励() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 嘉年华败者奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询每日送货奖励之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 嘉年华败者奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询每日送货奖励出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

    public void 毒雾副本奖励() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  毒雾副本奖励 ");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double 概率 = Math.ceil(Math.random() * rs.getInt("chance"));
                    double 对比 = Math.ceil(Math.random() * 100);
                    double 最少 = Math.ceil(Math.random() * rs.getInt("baseQty"));
                    double 最多 = Math.ceil(Math.random() * rs.getInt("maxRandomQty"));
                    if (最多 <= 最少) {
                        最多 = 最少;
                    }
                    if (概率 >= 100) {
                        概率 = 100;
                    }
                    if (概率 <= 对比) {
                        gainItem(rs.getInt("itemId"), (short) 最多);
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("毒雾副本奖励、出错");
        }
    }

    public void 毒雾副本奖励2() {
        // itemId baseQty maxRandomQty chance
        int count = 0;
        int arrLength = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(itemId) as Count, SUM(chance) as Data FROM 毒雾副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Count");
                    arrLength = rs.getInt("Data");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询每日送货奖励之概率分母出错：" + ex.getMessage());
        }

        if (count == 0 || arrLength == 0) {
            return;
        }

        int[][] data = new int[arrLength][2];

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT itemId, baseQty, maxRandomQty, chance FROM 毒雾副本奖励");
            try (ResultSet rs = ps.executeQuery()) {
                int j = 0;
                while (rs.next()) {
                    int randomQty = new Random().nextInt(rs.getInt("maxRandomQty"));
                    for (int i = 0; i <= rs.getInt("chance") - 1; i++) {
                        data[j] = new int[]{rs.getInt("itemId"), rs.getInt("baseQty") + randomQty};
                        j++;
                    }
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询每日送货奖励出错：" + ex.getMessage());
        }

        List<int[]> result = new ArrayList<>();
        for (int i = 0; i <= count - 1; i++) {
            int r = new Random().nextInt(arrLength - 1);

            int itemId = data[r][0];
            int existsCount = 0;
            for (int[] is : result) {
                if (is[0] == itemId) {
                    existsCount++;
                }
            }
            if (existsCount > 0) {
                continue;
            }

            result.add(data[r]);
        }

        if (result.size() > 0) {
            for (int[] is : result) {
                int itemId = is[0];
                short qty = (short) is[1];
                gainItem(itemId, qty);
            }
        }
    }

//    public void 扩充背包(int type) {
//        chr.getInventory(type).addSlot((byte) 4);
//
//    }
    public void 摇(String text) {
        c.sendPacket(MaplePacketCreator.showEffect(text));

    }

    public void 倒计时(int time) {

        c.sendPacket(MaplePacketCreator.getClock(time));
    }

    public void 执行摇奖脚本() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(3500);
                    NPCScriptManager.getInstance().dispose(c);
                    NPCScriptManager.getInstance().start(c, 9100201, 1);
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

    public void 延迟踢下线() {
        new Thread() {
            public void run() {
                try {
                    c.getPlayer().dropMessage(1, "配置文件与服务器IP地址不符，5秒后将断开连接。");
                    Thread.sleep(5000);
                    //c.getPlayer().changeChannel(100);
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

    public void 摇奖动画() throws InterruptedException {
        final double r1 = Math.ceil(Math.random() * 5);
        final double r2 = Math.ceil(Math.random() * 5);
        final double r3 = Math.ceil(Math.random() * 6);
        c.sendPacket(MaplePacketCreator.showEffect("miro/frame"));
        if (r1 == 1) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR1/0"));
        } else if (r1 == 2) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR1/1"));
        } else if (r1 == 3) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR1/2"));
        } else if (r1 == 4) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR1/3"));
        } else if (r1 == 5) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR1/4"));
        }
        if (r2 == 1) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR2/0"));
        } else if (r2 == 2) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR2/1"));
        } else if (r2 == 3) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR2/2"));
        } else if (r2 == 4) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR2/3"));
        } else if (r2 == 5) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR2/4"));
        }
        if (r3 == 1) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR3/0"));
        } else if (r3 == 2) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR3/1"));
        } else if (r3 == 3) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR3/2"));
        } else if (r3 == 4) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR3/3"));
        } else if (r3 == 5) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR3/4"));
        } else if (r3 == 6) {
            c.sendPacket(MaplePacketCreator.showEffect("miro/RR3/4"));
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3500);

                } catch (InterruptedException e) {
                }
            }
        }.start();

    }

    /*
    NPCScriptManager.getInstance().dispose(c);
                    NPCScriptManager.getInstance().start(c, 2000, 10);
                    if (r1 == 1 && r2 == 1 && r3 == 1) {
                        System.out.println("111");
                    } else if (r1 == 1 && r2 == 2 && r3 == 1) {
                        System.out.println("121");
                    } else if (r1 == 1 && r2 == 3 && r3 == 1) {
                        System.out.println("131");
                    } else if (r1 == 1 && r2 == 4 && r3 == 1) {
                        System.out.println("141");
                    } else if (r1 == 1 && r2 == 5 && r3 == 1) {
                        System.out.println("151");

                    } else if (r1 == 1 && r2 == 1 && r3 == 2) {
                        System.out.println("112");
                    } else if (r1 == 1 && r2 == 2 && r3 == 2) {
                        System.out.println("122");
                    } else if (r1 == 1 && r2 == 3 && r3 == 2) {
                        System.out.println("132");
                    } else if (r1 == 1 && r2 == 4 && r3 == 2) {
                        System.out.println("142");
                    } else if (r1 == 1 && r2 == 5 && r3 == 2) {
                        System.out.println("152");

                    } else if (r1 == 1 && r2 == 1 && r3 == 3) {
                        System.out.println("113");
                    } else if (r1 == 1 && r2 == 2 && r3 == 3) {
                        System.out.println("123");
                    } else if (r1 == 1 && r2 == 3 && r3 == 3) {
                        System.out.println("133");
                    } else if (r1 == 1 && r2 == 4 && r3 == 3) {
                        System.out.println("143");
                    } else if (r1 == 1 && r2 == 5 && r3 == 3) {
                        System.out.println("153");
                    }
     */
    public void 摇奖奖励() throws InterruptedException {

        Thread.sleep(5000);

        NPCScriptManager.getInstance().dispose(c);
        NPCScriptManager.getInstance().start(c, 9900004, 1);//

        /* c.sendPacket(MaplePacketCreator.enableActions());//聊天解卡
        NPCScriptManager.getInstance().dispose(c);*/
    }
//////////////////////////////////////

    public void 打开网页(String web) {//打开网址
        this.c.sendPacket(MaplePacketCreator.openWeb(web));
    }

    public void 个人黄色字体(String message) {
        c.sendPacket(UIPacket.getTopMsg(message));
    }

    public void ZEVMS(String txt, String txt2) {
        FileoutputUtil.logToZev(txt, txt2);
    }

    public void 全服黄色字体(String message) {
        for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
            for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                c.sendPacket(UIPacket.getTopMsg(message));
            }
        }
    }

    public String 开服名称() {//取服务器名字
        return c.getChannelServer().getServerName();
    }

    public int 今日在线() {
        int data = 0;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement psu = con.prepareStatement("SELECT todayOnlineTime FROM characters WHERE id = ?");
            psu.setInt(1, c.getPlayer().getId());
            ResultSet rs = psu.executeQuery();
            if (rs.next()) {
                data = rs.getInt("todayOnlineTime");
            }
            rs.close();
            psu.close();

        } catch (SQLException ex) {
            System.err.println("查询今日在线时间出错：" + ex.getMessage());
        }

        return data;
    }

    public int 总在线() {
        int data = 0;
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement psu = con.prepareStatement("SELECT totalOnlineTime FROM characters WHERE id = ?");
            psu.setInt(1, c.getPlayer().getId());
            ResultSet rs = psu.executeQuery();
            if (rs.next()) {
                data = rs.getInt("totalOnlineTime");
            }
            rs.close();
            psu.close();
        } catch (SQLException ex) {
            System.err.println("查询总在线时间出错：" + ex.getMessage());
        }

        return data;
    }

    public int 在线人数() {
        int count = 0;
        for (ChannelServer chl : ChannelServer.getAllInstances()) {
            count += chl.getPlayerStorage().getAllCharacters().size();
        }
        return count;
    }
    private Point position = new Point();

    public void 清怪() {
        MapleMap map = c.getPlayer().getMap();
        double range = Double.POSITIVE_INFINITY;
        MapleMonster mob;
        for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
            mob = (MapleMonster) monstermo;
            map.killMonster(mob, c.getPlayer(), true, false, (byte) 1);
        }
    }

    public void 删除已使用的充值卡() {
        PreparedStatement ps1 = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM z点券兑换卡");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM z点券兑换卡 ");
            rs = ps.executeQuery();
            while (rs.next()) {
                String sqlstr = " delete from z点券兑换卡 where Point <= 0";
                ps1.executeUpdate(sqlstr);
            }

        } catch (SQLException ex) {
        }
    }

    public void 删除礼包兑换卡1() {
        PreparedStatement ps1 = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = con.prepareStatement("SELECT * FROM z礼包兑换卡1");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM z礼包兑换卡1 ");
            rs = ps.executeQuery();
            while (rs.next()) {
                String sqlstr = " delete from z礼包兑换卡1 where Point <= 0";
                ps1.executeUpdate(sqlstr);
            }

        } catch (SQLException ex) {
        }
    }

    public void 个人存档() {
        c.getPlayer().saveToDB(false, false);
    }

    public void 角色ID() {
        c.getPlayer().getId();
    }

    public void 打开验证() {
        try {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr == null) {
                        continue;
                    }
                    int mapId = c.getPlayer().getMapId();
                    if (chr.getMapId() != mapId) {
                        continue;
                    }
                    NPCScriptManager.getInstance().start(c, 9900004, 17);
                }
            }
        } catch (Exception e) {
        }
    }

    public void 全服存档() {
        try {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr == null) {
                        continue;
                    }
                    chr.saveToDB(false, false);
                }
            }
        } catch (Exception e) {
        }
    }

    public void 给技能(final int action, final byte level, final byte masterlevel) {
        c.getPlayer().changeSkillLevel(SkillFactory.getSkill(action), level, masterlevel);
    }

    public static void 商城物品(final int id, final int key) throws SQLException {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = null;
            ps = con.prepareStatement("INSERT INTO cashshop_modified_items (itemid, meso) VALUES (?, ?)");
            //写ID
            ps.setInt(1, id);
            //写类型
            ps.setInt(2, key);
            //写建委
        } catch (SQLException ex) {
            Logger.getLogger(NPCConversationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void 键盘上技能(final int id, final int key, final int type, final int action, final byte level, final byte masterlevel) throws SQLException {
        //给予技能先
        c.getPlayer().changeSkillLevel(SkillFactory.getSkill(action), level, masterlevel);
        c.getPlayer().dropMessage(1, "<提示>\r\n5秒后你会自动下线，请1分钟后再次登陆。");
        //存档
        c.getPlayer().saveToDB(false, false);
        new Thread() {
            @Override
            public void run() {
                try {
                    //5秒后断开玩家链接
                    Thread.sleep(1000 * 5);
                    c.getPlayer().getClient().getSession().close();
                    //10秒后开始执行上技能指令
                    Thread.sleep(1000 * 10);
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = null;
                    ps = con.prepareStatement("INSERT INTO keymap (characterid, `key`, `type`, `action`) VALUES (?, ?, ?, ?)");
                    //写ID
                    ps.setInt(1, id);
                    //写类型
                    ps.setInt(2, key);
                    //写建委
                    ps.setInt(3, type);
                    //写代码
                    ps.setInt(4, action);
                    ps.execute();
                } catch (InterruptedException e) {
                } catch (SQLException ex) {
                    Logger.getLogger(NPCConversationManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }

    public void 删除角色(int id) {
        PreparedStatement ps1 = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
        } catch (SQLException ex) {

        }
        String sqlstr = " delete from characters where id =" + id + "";
        try {
            ps1.executeUpdate(sqlstr);
            c.getPlayer().dropMessage(1, "角色删除成功。");
        } catch (SQLException ex) {
        }
    }

    public void 开始计时() {

        System.currentTimeMillis();
    }

    public void 彩票() {
        GetConfigValues();
        gui.Start.ConfigValuesMap.get("ZEVMS彩票1");
        gui.Start.ConfigValuesMap.get("ZEVMS彩票2");
        gui.Start.ConfigValuesMap.get("ZEVMS彩票3");
        gui.Start.ConfigValuesMap.get("ZEVMS彩票4");
        gui.Start.ConfigValuesMap.get("ZEVMS彩票5");
    }

    public void 加载附魔信息() {
        GetFuMoInfo();
    }

    public int 打孔(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        Connection con = DatabaseConnection.getConnection();

        // 先查询装备的打孔数据
        String mxmxdDaKongFuMo = null;
        String sqlQuery1 = "SELECT b.mxmxd_dakong_fumo FROM inventoryitems a, inventoryequipment b WHERE a.inventoryitemid = b.inventoryitemid AND a.characterid = ? AND a.inventorytype = -1 AND a.position = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlQuery1);
            ps.setInt(1, c.getPlayer().getId());
            ps.setInt(2, equipmentPosition);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mxmxdDaKongFuMo = rs.getString("mxmxd_dakong_fumo");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("打孔：查询查询装备的打孔数据出错：" + ex.getMessage());
            return 0;
        }

        if (mxmxdDaKongFuMo == null) {
            mxmxdDaKongFuMo = "";
        }

        // 再计算该装备已打孔数量
        int dakongCount = 0;
        if (mxmxdDaKongFuMo.length() > 0) {
            dakongCount = mxmxdDaKongFuMo.split(",").length;
        }

        if (dakongCount >= 20) {
            return 0;
        }
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equipmentPosition).setDaKongFuMo(mxmxdDaKongFuMo + "0:0,");
        c.getPlayer().道具存档();
        return 1;
    }

    public int 打孔2(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        Connection con = DatabaseConnection.getConnection();

        // 先查询装备的打孔数据
        String mxmxdDaKongFuMo = null;
        String sqlQuery1 = "SELECT b.mxmxd_dakong_fumo FROM inventoryitems a, inventoryequipment b WHERE a.inventoryitemid = b.inventoryitemid AND a.characterid = ? AND a.inventorytype = -1 AND a.position = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlQuery1);
            ps.setInt(1, c.getPlayer().getId());
            ps.setInt(2, equipmentPosition);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mxmxdDaKongFuMo = rs.getString("mxmxd_dakong_fumo");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("打孔：查询查询装备的打孔数据出错：" + ex.getMessage());
            return 0;
        }

        if (mxmxdDaKongFuMo == null) {
            mxmxdDaKongFuMo = "";
        }

        // 再计算该装备已打孔数量
        int dakongCount = 0;
        if (mxmxdDaKongFuMo.length() > 0) {
            dakongCount = mxmxdDaKongFuMo.split(",").length;
        }

        if (dakongCount >= 3) {
            return 0;
        }
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equipmentPosition).setDaKongFuMo(mxmxdDaKongFuMo + "0:0,");
        c.getPlayer().saveToDB(false, false);
        return 1;
    }

    /**
     * Replaces the first subsequence of the <tt>source</tt> string that matches
     * the literal target string with the specified literal replacement string.
     *
     * @param source source string on which the replacement is made
     * @param target the string to be replaced
     * @param replacement the replacement string
     * @return the resulting string
     */
    private static String replaceFirst2(String source, String target, String replacement) {
        int index = source.indexOf(target);
        if (index == -1) {
            return source;
        }

        return source.substring(0, index)
                .concat(replacement)
                .concat(source.substring(index + target.length()));
    }

    //编号，附魔类，附魔值
    public int 附魔(final short equipmentPosition, final int fuMoType, final int fuMoValue) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        Connection con = DatabaseConnection.getConnection();

        // 先查询装备的打孔附魔数据
        String mxmxdDaKongFuMo = null;
        String sqlQuery1 = "SELECT b.mxmxd_dakong_fumo FROM inventoryitems a, inventoryequipment b WHERE a.inventoryitemid = b.inventoryitemid AND a.characterid = ? AND a.inventorytype = -1 AND a.position = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlQuery1);
            ps.setInt(1, c.getPlayer().getId());
            ps.setInt(2, equipmentPosition);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mxmxdDaKongFuMo = rs.getString("mxmxd_dakong_fumo");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("附魔：查询装备的打孔附魔数据出错：" + ex.getMessage());
            return 0;
        }
        if (mxmxdDaKongFuMo == null || mxmxdDaKongFuMo.length() == 0) {
            return 0;
        }

        mxmxdDaKongFuMo = replaceFirst2(mxmxdDaKongFuMo, "0:0,", String.format("%s:%s,", fuMoType, fuMoValue));
        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equipmentPosition).setDaKongFuMo(mxmxdDaKongFuMo);
        String 输出 = "";
        switch (fuMoType) {
            case 1:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加对普通怪物 " + fuMoValue + "% 的伤害。";
                break;
            case 2:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加对高级怪物 " + fuMoValue + "% 的伤害。";
                break;
            case 3:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加对所有怪物 " + fuMoValue + "% 的伤害。";
                break;
            case 4:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加对普通怪物 " + fuMoValue + "% 的一击必杀率。";
                break;
            case 5:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加对高级怪物 " + fuMoValue + "% 的一击必杀率。";
                break;
            case 6:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加对所有怪物 " + fuMoValue + "% 的一击必杀率。";
                break;
            case 7:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加 " + fuMoValue + "% 的一击必杀值。";
                break;
            case 8:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加 " + fuMoValue + "% 机率让怪物血量只剩1。";
                break;
            case 9:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。攻击怪物时，" + fuMoValue + "% 机率怪物不会有仇恨。";
                break;
            case 10:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。攻击时附加 " + fuMoValue + " 点真实伤害。";
                break;
            case 21:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。减少角色 " + fuMoValue + "% 受到的伤害。";
                break;
            case 22:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。减少角色 " + fuMoValue + " 点受到的伤害。";
                break;
            case 23:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。受到的伤害大于最大生命值 60% 时，会减少 " + fuMoValue + "% 的伤害。";
                break;
            case 24:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。用超级药水抵消受到的伤害。";
                break;
            case 31:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加 " + fuMoValue + "% 的狩猎经验获取。";
                break;
            case 32:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加 " + fuMoValue + "% 的泡点经验获取。";
                break;
            case 33:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加 " + fuMoValue + "% 的泡点经验获取。";
                break;
            case 34:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。被诅咒状态下增加 5 倍狩猎经验。";
                break;
            case 100:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加 " + fuMoValue + " 点异常抗性。";
                break;
            case 101:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加 " + fuMoValue + " 点异常免疫。";
                break;
            case 200:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。使用BUFF技能 " + fuMoValue + "% 机率清空所有冷却。";
                break;
            case 300:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。升级时额外增加 " + fuMoValue + " 点 MaxHP 。";
                break;
            case 301:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。升级时额外增加 " + fuMoValue + " 点 MaxMP。";
                break;
            case 302:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。升级时额外增加 " + fuMoValue + " 点 MaxHP，MaxMP。";
                break;
            case 303:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。升级时 " + fuMoValue + " % 机率获得额外1级，不获得升级属性。";
                break;
            case 400:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。受到伤害时 " + fuMoValue + " % 机率发动 10 级稳如泰山。";
                break;
            case 401:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。受到伤害时 " + fuMoValue + " % 机率发动 10 级愤怒之火。";
                break;
            case 500:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加召唤兽 " + fuMoValue + " % 对普通怪物的伤害。";
                break;
            case 501:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加召唤兽 " + fuMoValue + " % 对高怪物的伤害。";
                break;
            case 502:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。召唤兽攻击时附加 " + fuMoValue + "  点真实伤害。";
                break;
            case 503:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。增加召唤兽和玩家 " + fuMoValue + " % 对所有怪物的伤害。";
                break;
            case 4211002:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[落叶斩] 值为 (" + fuMoValue + ") ";
                break;
            case 4111005:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[多重飞镖] 值为 (" + fuMoValue + ") ";
                break;
            case 1111002:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[斗气集中] 值为 (" + fuMoValue + ") ";
                break;
            case 1211002:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[属性攻击] 值为 (" + fuMoValue + ") ";
                break;
            case 1311005:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[龙之献祭] 值为 (" + fuMoValue + ") ";
                break;
            case 2111002:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[末日烈焰] 值为 (" + fuMoValue + ") ";
                break;
            case 2211003:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[落雷枪] 值为 (" + fuMoValue + ") ";
                break;
            case 2311004:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[圣光] 值为 (" + fuMoValue + ") ";
                break;
            case 3111006:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[箭扫射-弓] 值为 (" + fuMoValue + ") ";
                break;
            case 3211006:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[箭扫射-弩] 值为 (" + fuMoValue + ") ";
                break;
            case 5111005:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[超人变身] 值为 (" + fuMoValue + ") ";
                break;
            case 5211004:
                输出 = "[附魔系统] : 恭喜玩家 " + c.getPlayer().getName() + " 附魔成功。技能[双枪喷射] 值为 (" + fuMoValue + ") ";
                break;
            default:
                break;
        }
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 输出));
        /*if (gui.Start.ConfigValuesMap.get("附魔提醒开关") == 0) {
            sendMsgToQQGroup(输出);
        }*/
        c.getPlayer().道具存档();
        return 1;
    }

    public String 显示附魔效果() {
        StringBuilder name = new StringBuilder();

        if (c.getPlayer().getEquippedFuMoMap().get(7) != null) {
            int 增加 = 10000000 + (10000000 / 100 * c.getPlayer().getEquippedFuMoMap().get(7));
            name.append("\t[#e#b必杀值#k#n] : #r").append(增加).append("#k\r\n");
        } else {
            name.append("\t[#e#b必杀值#k#n] : #r10000000#k\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(100) != null) {
            name.append("\t[#e#b异常抗性#k#n] : 增加对异常状态抗性，减少持续 #r").append(c.getPlayer().getEquippedFuMoMap().get(100)).append("#k % 的异常状态持续时间\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(1) != null) {
            name.append("\t[#e#b强攻#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(1)).append("#k % 对普通怪物的伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(2) != null) {
            name.append("\t[#e#b超强攻#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(2)).append("#k % 对高级怪物的伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(3) != null) {
            name.append("\t[#e#b战争意志#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(3)).append("#k % 对所有怪物的伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(4) != null) {
            name.append("\t[#e#b鹰眼#k#n] : 对普通怪物 #r").append(c.getPlayer().getEquippedFuMoMap().get(4)).append("#k % 机率一击必杀\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(5) != null) {
            name.append("\t[#e#b锐眼#k#n] : 对高级怪物 #r").append(c.getPlayer().getEquippedFuMoMap().get(5)).append("#k % 机率一击必杀\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(6) != null) {
            name.append("\t[#e#b谢幕#k#n] : 对所有怪物 #r").append(c.getPlayer().getEquippedFuMoMap().get(6)).append("#k % 机率一击必杀\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(8) != null) {
            name.append("\t[#e#b致命打击#k#n] : 攻击怪物 #r").append(c.getPlayer().getEquippedFuMoMap().get(8)).append("#k % 概率让怪物血量只剩1\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(9) != null) {
            name.append("\t[#e#b蒙蔽#k#n] : 攻击怪物时 #r").append(c.getPlayer().getEquippedFuMoMap().get(9)).append("#k % 机率怪物不会有仇恨\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(10) != null) {
            name.append("\t[#e#b追击#k#n] : 攻击时附加 #r").append(c.getPlayer().getEquippedFuMoMap().get(10)).append("#k 点真实伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(21) != null) {
            name.append("\t[#e#b坚韧#k#n] : 减少 #r").append(c.getPlayer().getEquippedFuMoMap().get(21)).append("#k % 受到的伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(22) != null) {
            name.append("\t[#e#b坚不可摧#k#n] : 减少 #r").append(c.getPlayer().getEquippedFuMoMap().get(22)).append("#k 点受到的伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(23) != null) {
            name.append("\t[#e#b顽固#k#n] : 受到的伤害大于你最大生命值 #b60%#k 时，会减少 #r").append(c.getPlayer().getEquippedFuMoMap().get(23)).append("#k % 受到的伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(24) != null) {
            name.append("\t[#e#b未卜先知#k#n] : 用超级药水抵消受到的伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(31) != null) {
            name.append("\t[#e#b幸运狩猎#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(31)).append("#k % 狩猎经验\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(32) != null) {
            name.append("\t[#e#b苦中作乐#k#n] : 被诅咒状态下增加 #r5#k 倍狩猎经验\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(33) != null) {
            name.append("\t[#e#b闲来好运#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(33)).append("#k % 泡点经验\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(34) != null) {
            name.append("\t[#e#b财源滚滚#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(34)).append("#k % 泡点金币\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(200) != null) {
            name.append("\t[#e#b伺机待发#k#n] : 使用BUFF技能时 #r").append(c.getPlayer().getEquippedFuMoMap().get(200)).append("#k % 的机率重置所有已经进入冷却的技能\r\n");
        }
        if (c.getPlayer().getEquippedFuMoMap().get(300) != null) {
            name.append("\t[#e#b茁壮生命#k#n] : 升级时获得额外 #r").append(c.getPlayer().getEquippedFuMoMap().get(300)).append("#k 点 MaxHP\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(301) != null) {
            name.append("\t[#e#b茁壮魔力#k#n] : 升级时获得额外 #r").append(c.getPlayer().getEquippedFuMoMap().get(301)).append("#k 点 MaxMP\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(302) != null) {
            name.append("\t[#e#b茁壮生长#k#n] : 升级时获得额外 #r").append(c.getPlayer().getEquippedFuMoMap().get(302)).append("#k 点MaxMP.MaxHP\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(303) != null) {
            name.append("\t[#e#b拔苗助长#k#n] : 升级时 #r").append(c.getPlayer().getEquippedFuMoMap().get(303)).append("#k % 机率获得额外1级，不获得升级属性\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(400) != null) {
            name.append("\t[#e#b稳如泰山#k#n] : 被攻击时 #r").append(c.getPlayer().getEquippedFuMoMap().get(400)).append("#k % 机率发动 10 级稳如泰山\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(401) != null) {
            name.append("\t[#e#b愤怒之火#k#n] : 被攻击时 #r").append(c.getPlayer().getEquippedFuMoMap().get(401)).append("#k % 机率发动 10 级愤怒之火\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(500) != null) {
            name.append("\t[#e#b训练有方#k#n] : 增加召唤兽 #r").append(c.getPlayer().getEquippedFuMoMap().get(500)).append("#k % 对普通怪物的伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(501) != null) {
            name.append("\t[#e#b训练有素#k#n] : 增加召唤兽 #r").append(c.getPlayer().getEquippedFuMoMap().get(501)).append("#k % 对高级怪物的伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(502) != null) {
            name.append("\t[#e#b迅捷突袭#k#n] : 召唤兽攻击时附加 #r").append(c.getPlayer().getEquippedFuMoMap().get(502)).append("#k 点真实伤害 \r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(503) != null) {
            name.append("\t[#e#b心有灵犀#k#n] : 增加召唤兽和玩家 #r").append(c.getPlayer().getEquippedFuMoMap().get(503)).append("#k % 对所有怪物的伤害 \r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(4211002) != null) {
            name.append("\t[#e#b落叶斩#k#n] : 攻击时根据拥有的枫叶数量增加基础伤害，增加率为 #r").append(c.getPlayer().getEquippedFuMoMap().get(4211002)).append("#k %\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(4111005) != null) {
            name.append("\t[#e#b多重飞镖#k#n] : 攻击时根据拥有的飞镖数量增加基础伤害，增加率为 #r").append(c.getPlayer().getEquippedFuMoMap().get(4111005)).append("#k %\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(1111002) != null) {
            name.append("\t[#e#b斗气集中#k#n] : 斗气状态下增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(1111002)).append("#k % 基础伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(1211002) != null) {
            name.append("\t[#e#b属性攻击#k#n] : 属性攻击技能，根据状态调整。 烈焰状态增加基础伤害，寒冰状态增加吸血能力，雷鸣状态增加累积伤害，神圣状态增加秒杀率。参考值 #r").append(c.getPlayer().getEquippedFuMoMap().get(1211002)).append("#k\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(1311005) != null) {
            name.append("\t[#e#b龙之献祭#k#n] : 根据自身目前血量，增加龙之献祭伤害，使用 5 次龙之献祭后，龙咆哮将会增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(1311005)).append("#k %基础伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(2111002) != null) {
            name.append("\t[#e#b末日烈焰#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(2111002)).append("#k % 基础伤害，每次伤害附加上一次伤害值\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(2211003) != null) {
            name.append("\t[#e#b落雷枪#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(2211003)).append("#k % 基础伤害，并增加一定量的秒杀率\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(2311004) != null) {
            name.append("\t[#e#b圣光#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(2311004)).append("#k % 基础伤害，使用后获取一次概率伤害免疫，最多可储存两次\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(3111006) != null) {
            name.append("\t[#e#b箭扫射-弓#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(3111006)).append("#k % 基础伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(3211006) != null) {
            name.append("\t[#e#b箭扫射-弩#k#n] : 增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(3211006)).append("#k % 基础伤害。并增加一定量的秒杀率\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(5111005) != null) {
            name.append("\t[#e#b超人变身#k#n] : 增加变身状态的下 #r").append(c.getPlayer().getEquippedFuMoMap().get(5111005)).append("#k % 基础伤害，最终伤害\r\n");
        }

        if (c.getPlayer().getEquippedFuMoMap().get(5211004) != null) {
            name.append("\t[#e#b双枪喷射#k#n] : 烈焰喷射，寒冰喷射增加 #r").append(c.getPlayer().getEquippedFuMoMap().get(5211004)).append("#k % 基础伤害，最终伤害\r\n");
        }

        return name.toString();
    }

    /**
     * public int indexOf(int ch, int fromIndex)
     * 返回在此字符串中第一次出现指定字符处的索引，从指定的索引开始搜索
     *
     * @param srcText
     * @param findText
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        int index = 0;
        while ((index = srcText.indexOf(findText, index)) != -1) {
            index = index + findText.length();
            count++;
        }
        return count;
    }

    // 返回值就表示已打孔数量
    public int 查询身上装备已打孔数(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        Connection con = DatabaseConnection.getConnection();

        // 先查询装备的打孔数据
        String mxmxdDaKongFuMo = null;
        String sqlQuery1 = "SELECT b.mxmxd_dakong_fumo FROM inventoryitems a, inventoryequipment b WHERE a.inventoryitemid = b.inventoryitemid AND a.characterid = ? AND a.inventorytype = -1 AND a.position = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlQuery1);
            ps.setInt(1, c.getPlayer().getId());
            ps.setInt(2, equipmentPosition);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mxmxdDaKongFuMo = rs.getString("mxmxd_dakong_fumo");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询身上装备已打孔数：查询装备的打孔数据出错：" + ex.getMessage());
            return 0;
        }

        if (mxmxdDaKongFuMo == null) {
            mxmxdDaKongFuMo = "";
        }

        return appearNumber(mxmxdDaKongFuMo, ",");
    }

    // 返回值就表示可附魔数量
    public int 查询身上装备可附魔数(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        Connection con = DatabaseConnection.getConnection();

        // 先查询装备的打孔附魔数据
        String mxmxdDaKongFuMo = null;
        String sqlQuery1 = "SELECT b.mxmxd_dakong_fumo FROM inventoryitems a, inventoryequipment b WHERE a.inventoryitemid = b.inventoryitemid AND a.characterid = ? AND a.inventorytype = -1 AND a.position = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sqlQuery1);
            ps.setInt(1, c.getPlayer().getId());
            ps.setInt(2, equipmentPosition);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mxmxdDaKongFuMo = rs.getString("mxmxd_dakong_fumo");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("查询身上装备可附魔数：查询装备的打孔附魔数据出错：" + ex.getMessage());
            return 0;
        }

        if (mxmxdDaKongFuMo == null) {
            mxmxdDaKongFuMo = "";
        }

        return appearNumber(mxmxdDaKongFuMo, "0:0,");
    }

    // 返回值大于0表示清洗完成
    public int 清洗身上装备附魔(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        int dakongCount = 查询身上装备已打孔数(equipmentPosition);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= dakongCount; i++) {
            sb.append("0:0,");
        }

        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equipmentPosition).setDaKongFuMo(sb.toString());
        c.getPlayer().saveToDB(false, false);
        return 1;
    }

    public int 清洗(final short equipmentPosition) {
        if (equipmentPosition >= 0) {
            return 0;
        }

        int dakongCount = 查询身上装备已打孔数(equipmentPosition);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= dakongCount; i++) {
            sb.append("0:0,");
        }

        c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equipmentPosition).setDaKongFuMo(sb.toString());
        c.getPlayer().saveToDB(false, false);
        return 1;
    }
//
//    public static final void 目标(final int objectid) {
//        chr.getPlayer().getMap().getCharacterById(objectid);
//    }

    public void Gainrobot(String Name, int Channale, int Piot) {
        try {
            int ret = Getrobot(Name, Channale);
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO robot (channel, Name,Point) VALUES (?, ?, ?)");
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
            PreparedStatement ps = con.prepareStatement("UPDATE robot SET `Point` = ? WHERE Name = ? and channel = ?");
            ps.setInt(1, ret);
            ps.setString(2, Name);
            ps.setInt(3, Channale);
            ps.execute();
            ps.close();
        } catch (SQLException sql) {
            System.err.println("Getrobot!!55" + sql);
        }
    }

    public int Getrobot(String Name, int Channale) {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM robot WHERE channel = ? and Name = ?");
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

    public void 分身2() {
        if (c.getPlayer().getCloneSize() <= Getrobot("" + c.getPlayer().getId() + "", 1)) {
            c.getPlayer().cloneLook1();
        } else {
            c.getPlayer().dropMessage(5, "无法继续召唤分身。");
        }
    }

    public void 分身1() {
        if (c.getPlayer().getCloneSize() <= Getrobot("" + c.getPlayer().getId() + "", 1)) {
            c.getPlayer().cloneLook1();
        } else {
            c.getPlayer().dropMessage(5, "无法继续召唤分身。");
        }
    }

    public void 销毁分身() {
        if (c.getPlayer().getCloneSize() > 0) {
            c.getPlayer().disposeClones();
        }
    }

    public void 下载文件(String urlStr, String fileName, String savePath) {
        try {
            Properties 設定檔 = System.getProperties();
            downLoadFromUrl(urlStr, fileName, "" + 設定檔.getProperty("user.dir") + "" + savePath);
        } catch (Exception e) {

        }
    }

    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        //System.out.println("info:" + url + " download success");

    }

    public void 豆豆机抽奖() {
        int sns = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT `id` FROM 豆豆机奖品  ORDER BY `id` DESC LIMIT 1");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String SN = rs.getString("id");
                    sns = Integer.parseInt(SN);
                    sns++;
                    ps.close();
                }
            }
            ps.close();
        } catch (SQLException ex) {
            System.err.println("出错：" + ex.getMessage());
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM  豆豆机奖品 ");
            try (ResultSet rs = ps.executeQuery()) {
                final double 随机 = Math.ceil(Math.random() * sns);
                while (rs.next()) {
                    if (rs.getInt("id") == 随机) {
                        gainItem(rs.getInt("itemId"), (short) rs.getInt("cout"));
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("玩具塔副本奖励2、出错");
        }
    }

    public String 显示锻造需求材料(int a) {
        StringBuilder name = new StringBuilder();
        name.append("#r#e需要材料#k#n；—————————————————————\r\n");
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 锻造材料表 WHERE id2 = " + a + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int 物品代码 = rs.getInt("物品代码");
                int 物品数量 = rs.getInt("物品数量");
                //name.append(" #v" + 物品代码 + "# #d#t" + 物品代码 + "##k x [ #b" + 物品数量 + "#k / #r#c" + 物品代码 + "##k ]\r\n");
                name.append("\r\n#v").append(物品代码).append("# #d#t").append(物品代码).append("##k x [ #b").append(物品数量).append("#k / #r#c").append(物品代码).append("##k ]\r\n");
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    /*
    1.点券
    2.金币
    
     */
    public int 判断材料是否足够(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 锻造材料表 WHERE id2 = " + a + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data += 1;
                int 物品代码 = rs.getInt("物品代码");
                int 物品数量 = rs.getInt("物品数量");
                if (物品代码 == 0) {
                    if (getPlayer().getMeso() <= 物品数量) {
                        data -= 100;
                    }
                } else if (物品代码 == 1) {
                    if (c.getPlayer().getCSPoints(1) <= 物品数量) {
                        data -= 100;
                    }
                } else if (!haveItem(物品代码, 物品数量)) {
                    data -= 100;
                }
            }
        } catch (SQLException ex) {
        }
        //System.err.println(data);
        return data;
    }

    public int 收取制作材料(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 锻造材料表 WHERE id2 = " + a + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data += 1;
                int 物品代码 = rs.getInt("物品代码");
                int 物品数量 = rs.getInt("物品数量");
                if (物品代码 == 0) {
                    c.getPlayer().gainMeso(-物品数量, true, false, true);
                } else if (物品代码 == 1) {
                    c.getPlayer().modifyCSPoints(1, -物品数量, true);
                } else {
                    gainItem(物品代码, (short) -物品数量);
                }
            }
        } catch (SQLException ex) {
        }
        //System.err.println(data);
        return data;
    }

    public String 显示物品(int a) {
        String data = "";
        data = "#v" + a + "# #b#z" + a + "##k";
        return data;
    }

    /*
    0无状态
    1封印
    
     */
    public String 显示锻造奖励信息(int a) {
        StringBuilder name = new StringBuilder();
        name.append("#r#e制作获得#n#k；—————————————————————\r\n");
        name.append("number: #g" + a + "#k\r\n");
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 锻造物品表 WHERE id2 = " + a + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int 物品代码 = rs.getInt("物品代码");
                int 物品数量 = rs.getInt("物品数量");
                name.append("\r\n#v").append(物品代码).append("# #d#t").append(物品代码).append("##k x [ #r").append(物品数量).append("#k ]\r\n");
                if (rs.getInt("可升级次数") > 0) {
                    name.append("\r\n[升级次数:#r").append(rs.getInt("可升级次数")).append("#k]");
                }
                if (物品代码 < 2000000) {
                    switch (rs.getInt("绑定")) {
                        case 1:
                            name.append("\r\n[状态:#r").append("封印").append("#k]");
                            break;
                        case 2:
                            name.append("\r\n[状态:#r").append("防滑").append("#k]");
                            break;
                        case 3:
                            name.append("\r\n[状态:#r").append("封印,防滑").append("#k]");
                            break;
                        case 4:
                            name.append("\r\n[状态:#r").append("防寒").append("#k]");
                            break;
                        case 5:
                            name.append("\r\n[状态:#r").append("封印,防寒").append("#k]");
                            break;
                        case 6:
                            name.append("\r\n[状态:#r").append("防滑,防寒").append("#k]");
                            break;
                        case 7:
                            name.append("\r\n[状态:#r").append("封印,防滑,防寒").append("#k]");
                            break;
                        case 8:
                            name.append("\r\n[状态:#r").append("不可交换").append("#k]");
                            break;
                        case 9:
                            name.append("\r\n[状态:#r").append("封印,不可交换").append("#k]");
                            break;
                        case 10:
                            name.append("\r\n[状态:#r").append("防滑,不可交换").append("#k]");
                            break;
                        case 11:
                            name.append("\r\n[状态:#r").append("封印,防滑,不可交换").append("#k]");
                            break;
                        case 12:
                            name.append("\r\n[状态:#r").append("防寒,不可交换").append("#k]");
                            break;
                        case 13:
                            name.append("\r\n[状态:#r").append("封印,防寒,不可交换").append("#k]");
                            break;
                        case 14:
                            name.append("\r\n[状态:#r").append("防滑,防寒,不可交换").append("#k]");
                            break;
                        case 15:
                            name.append("\r\n[状态:#r").append("封印,防滑,防寒,不可交换").append("#k]");
                            break;
                        default:
                            break;
                    }
                }
                if (rs.getInt("力量") > 0) {
                    name.append("\r\n[力量:#r").append(rs.getInt("力量")).append("#k]");
                }
                if (rs.getInt("敏捷") > 0) {
                    name.append("\r\n[敏捷:#r").append(rs.getInt("敏捷")).append("#k]");
                }
                if (rs.getInt("智力") > 0) {
                    name.append("\r\n[智力:#r").append(rs.getInt("智力")).append("#k]");
                }
                if (rs.getInt("运气") > 0) {
                    name.append("\r\n[运气:#r").append(rs.getInt("运气")).append("#k]");
                }
                if (rs.getInt("HP") > 0) {
                    name.append("\r\n[HP:#r").append(rs.getInt("HP")).append("#k]");
                }
                if (rs.getInt("MP") > 0) {
                    name.append("\r\n[MP:#r").append(rs.getInt("MP")).append("#k]");
                }
                if (rs.getInt("物理攻击力") > 0) {
                    name.append("\r\n[物理攻击力:#r").append(rs.getInt("物理攻击力")).append("#k]");
                }
                if (rs.getInt("物理防御力") > 0) {
                    name.append("\r\n[物理防御力:#r").append(rs.getInt("物理防御力")).append("#k]");
                }
                if (rs.getInt("魔法攻击力") > 0) {
                    name.append("\r\n[魔法攻击力:#r").append(rs.getInt("魔法攻击力")).append("#k]");
                }
                if (rs.getInt("魔法防御力") > 0) {
                    name.append("\r\n[魔法防御力:#r").append(rs.getInt("魔法防御力")).append("#k]");
                }
                if (rs.getInt("命中率") > 0) {
                    name.append("\r\n[命中率:#r").append(rs.getInt("命中率")).append("#k]");
                }
                if (rs.getInt("回避率") > 0) {
                    name.append("\r\n[回避率:#r").append(rs.getInt("回避率")).append("#k]");
                }
                if (rs.getInt("跳跃力") > 0) {
                    name.append("\r\n[跳跃力:#r").append(rs.getInt("跳跃力")).append("#k]");
                }
                if (rs.getInt("移动速度") > 0) {
                    name.append("\r\n[移动速度:#r").append(rs.getInt("移动速度")).append("#k]");
                }
                if (rs.getInt("限时") > 0) {
                    name.append("\r\n[限时:#r").append(rs.getInt("限时")).append("#k]");
                }
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public void 给锻造奖励(int a) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM 锻造物品表 WHERE id2 = " + a + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int 物品代码 = rs.getInt("物品代码");
                int 物品数量 = rs.getInt("物品数量");
                if (物品代码 > 2000000) {
                    gainItem(物品代码, (short) 物品数量);
                } else {
                    给装备(rs.getInt("物品代码"),
                            rs.getInt("可升级次数"),
                            rs.getInt("绑定"),
                            rs.getInt("力量"),
                            rs.getInt("敏捷"),
                            rs.getInt("智力"),
                            rs.getInt("运气"),
                            rs.getInt("HP"),
                            rs.getInt("MP"),
                            rs.getInt("物理攻击力"),
                            rs.getInt("物理防御力"),
                            rs.getInt("魔法攻击力"),
                            rs.getInt("魔法防御力"),
                            rs.getInt("回避率"),
                            rs.getInt("命中率"),
                            rs.getInt("跳跃力"),
                            rs.getInt("移动速度"),
                            rs.getInt("限时")
                    );
                }
            }
        } catch (SQLException ex) {
        }
    }

    public final void 给装备(final int id, final int sj, final int Flag, final int str, final int dex, final int luk, final int Int, final int hp, int mp, int watk, int matk, int wdef, int mdef, int hb, int mz, int ty, int yd, int period) {
        给装备(id, sj, Flag, str, dex, luk, Int, hp, mp, watk, matk, wdef, mdef, hb, mz, ty, yd, c, period);
    }

    public final void 给装备(final int id, final int sj, final int Flag, final int str, final int dex, final int luk, final int Int, final int hp, int mp, int watk, int matk, int wdef, int mdef, int hb, int mz, int ty, int yd, final MapleClient cg, int period) {
        if (1 >= 0) {
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            final MapleInventoryType type = GameConstants.getInventoryType(id);

            if (!MapleInventoryManipulator.checkSpace(cg, id, 1, "")) {
                return;
            }
            if (type.equals(MapleInventoryType.EQUIP) && !GameConstants.isThrowingStar(id) && !GameConstants.isBullet(id)) {
                final Equip item = (Equip) (ii.getEquipById(id));
                if (period > 0) {
                    item.setExpiration(System.currentTimeMillis() + (period * 60 * 60 * 1000));
                }
                final String name = ii.getName(id);
                if (id / 10000 == 114 && name != null && name.length() > 0) { //medal
                    final String msg = "你已获得称号 <" + name + ">";
                    cg.getPlayer().dropMessage(5, msg);
                }
                if (sj > 0) {
                    item.setUpgradeSlots((byte) (short) sj);
                }
                if (Flag > 0) {
                    item.setFlag((byte) (short) Flag);
                }
                if (str > 0) {
                    item.setStr((short) str);
                }
                if (dex > 0) {
                    item.setDex((short) dex);
                }
                if (luk > 0) {
                    item.setLuk((short) luk);
                }
                if (Int > 0) {
                    item.setInt((short) Int);
                }
                if (hp > 0) {
                    item.setHp((short) hp);
                }
                if (mp > 0) {
                    item.setMp((short) mp);
                }
                if (watk > 0) {
                    item.setWatk((short) watk);
                }
                if (matk > 0) {
                    item.setMatk((short) matk);
                }
                if (wdef > 0) {
                    item.setWdef((short) wdef);
                }
                if (mdef > 0) {
                    item.setMdef((short) mdef);
                }
                if (hb > 0) {
                    item.setAvoid((short) hb);
                }
                if (mz > 0) {
                    item.setAcc((short) mz);
                }
                if (ty > 0) {
                    item.setJump((short) ty);
                }
                if (yd > 0) {
                    item.setSpeed((short) yd);
                }
                MapleInventoryManipulator.addbyItem(cg, item.copy());
            } else {
                MapleInventoryManipulator.addById(cg, id, (short) 1, "", (byte) 0);
            }
        } else {
            MapleInventoryManipulator.removeById(cg, GameConstants.getInventoryType(id), id, -1, true, false);
        }
        cg.sendPacket(MaplePacketCreator.getShowItemGain(id, (short) 1, true));
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

    public String 等级成就(int a) {
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Upgrade_career order by id desc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("id2") == a) {
                    name.append("        " + rs.getString("shijian") + "  " + rs.getString("name") + "\r\n");
                }
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public static String 显示参赛() {
        return server.custom.capture.capture_yongfa.显示参赛人员();
    }

    public static void 参加(int a) {
        server.custom.capture.capture_yongfa.参加(a);
    }

    public static int 是否参加(int a) {
        return server.custom.capture.capture_yongfa.判断是否已经参加(a);
    }

    public static int 判断队伍(int a) {
        return server.custom.capture.capture_yongfa.判断队伍(a);
    }

    public static String 杀怪数量(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT count as DATA FROM mxmxd_mapkilledcount WHERE chrId = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("杀怪数量、出错");
        }
        return data;
    }

    public final void 超时空战场() {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000 * 60 * 1);
    }

    public String 黑龙远征队() {
        StringBuilder name = new StringBuilder();
        name.append("黑龙远征队：\r\n");
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characterz WHERE channel = 201 ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("Point") > 0) {
                    String 身份 = "";
                    if (rs.getInt("Point") == 1) {
                        身份 = "[#b队员#k]";
                    } else {
                        身份 = "[#r队长#k]";
                    }
                    name.append("" + 身份 + "  玩家: #d" + 角色ID取名字(rs.getInt("Name")) + "#k \r\n");
                }
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public void 开始黑龙远征(int a, int b) {
        for (ChannelServer CS : ChannelServer.getAllInstances()) {
            for (final MapleCharacter mch : CS.getPlayerStorage().getAllCharactersThreadSafe()) {
                if (mch.Getcharacterz("" + mch.getId() + "", 201) > 0) {
                    if (mch.getClient().getChannel() != a) {
                        mch.changeChannel(a);
                    }
                    if (mch.getMapId() != b) {
                        mch.changeMap(b, 0);
                    }
                }
            }
        }
    }

    /**
     ******************************************************************************************************************************************************
     * 充值系统
     * *****************************************************************************************************************************************************
     */
    public static int 判断兑换卡是否存在(String id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT code as DATA FROM nxcodez WHERE code = ?");
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data += 1;
                }
            }
        } catch (SQLException Ex) {
            System.err.println("判断兑换卡是否存在、出错");
        }
        return data;
    }

    public static int 判断兑换卡类型(String code) throws SQLException {
        int item = -1;
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT `leixing` FROM nxcodez WHERE code = ?");
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            item = rs.getInt("leixing");
        }
        /*rs.close();
        ps.close();*/
        return item;
    }

    public static int 判断兑换卡数额(String code) throws SQLException {
        int item = -1;
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT `valid` FROM nxcodez WHERE code = ?");
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            item = rs.getInt("valid");
        }
        /*rs.close();
        ps.close();*/
        return item;
    }

    public static int 判断兑换卡礼包(String code) throws SQLException {
        int item = -1;
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT `itme` FROM nxcodez WHERE code = ?");
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            item = rs.getInt("itme");
        }
        /*rs.close();
        ps.close();*/
        return item;
    }

    //删除兑换卡
    public void Deleteexchangecard(String a) {
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM nxcodez ");
            rs = ps1.executeQuery();
            while (rs.next()) {
                String sqlstr = " delete from nxcodez where code = '" + a + "' ";
                ps1.executeUpdate(sqlstr);
            }
        } catch (SQLException ex) {
        }
    }

    /**
     ******************************************************************************************************************************************************
     * 神秘商人
     * *****************************************************************************************************************************************************
     * @param id
     * @return
     */
    public String 显示商品(int id) {
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mysterious WHERE f = " + id + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int 编号 = rs.getInt("a");
                int 物品 = rs.getInt("b");
                int 数量 = rs.getInt("c");
                int 点券 = rs.getInt("d");
                int 金币 = rs.getInt("e");
                name.append("   #L").append(编号).append("# #v").append(物品).append("# #b#t").append(物品).append("##k x ").append(数量).append("");
                name.append(" #d[券/币]:#b").append(点券).append("#k/#b").append(金币).append("#k#l\r\n");
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public void 购买物品(int id) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mysterious WHERE f = " + id + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int 编号 = rs.getInt("a");
                int 物品 = rs.getInt("b");
                int 数量 = rs.getInt("c");
                int 点券 = rs.getInt("d");
                int 金币 = rs.getInt("e");
                gainItem(物品, (short) 数量);
            }

        } catch (SQLException ex) {
        }
    }

    /**
     ******************************************************************************************************************************************************
     * 邮箱系统
     * *****************************************************************************************************************************************************
     */
    public String 显示邮件(int id) {
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mail");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("juese") == id) {
                    int 邮件编号 = rs.getInt("number");
                    String 邮件标题 = rs.getString("biaoti");
                    name.append("    #L").append(邮件编号).append("##fUI/UIWindow.img/Delivery/icon4##b").append(邮件标题).append("#k#l\r\n");
                }
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public void 领取邮件(int id) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mail");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("number") == id) {
                    int 数量 = rs.getInt("shuliang1");
                    int 代码 = rs.getInt("type1");
                    if (rs.getInt("type1") != 0) {
                        switch (代码) {
                            case 4:
                                c.getPlayer().gainMeso(数量, true, false, true);
                                break;
                            case 1:
                                c.getPlayer().modifyCSPoints(1, 数量, true);
                                break;
                            case 2:
                                c.getPlayer().modifyCSPoints(2, 数量, true);
                                break;
                            case 3:
                                c.getPlayer().gainExp(数量, true, true, true);
                                break;
                            default:
                                gainItem(代码, (short) 数量);
                                break;
                        }
                    }
                    int 数量2 = rs.getInt("shuliang2");
                    int 代码2 = rs.getInt("type2");
                    if (rs.getInt("type2") != 0) {
                        switch (代码2) {
                            case 4:
                                c.getPlayer().gainMeso(数量2, true, false, true);
                                break;
                            case 1:
                                c.getPlayer().modifyCSPoints(1, 数量2, true);
                                break;
                            case 2:
                                c.getPlayer().modifyCSPoints(2, 数量2, true);
                                break;
                            case 3:
                                c.getPlayer().gainExp(数量2, true, true, true);
                                break;
                            default:
                                gainItem(代码2, (short) 数量2);
                                break;
                        }
                    }

                    int 数量3 = rs.getInt("shuliang3");
                    int 代码3 = rs.getInt("type3");
                    if (rs.getInt("type3") != 0) {
                        switch (代码3) {
                            case 4:
                                c.getPlayer().gainMeso(数量3, true, false, true);
                                break;
                            case 1:
                                c.getPlayer().modifyCSPoints(1, 数量3, true);
                                break;
                            case 2:
                                c.getPlayer().modifyCSPoints(2, 数量3, true);
                                break;
                            case 3:
                                c.getPlayer().gainExp(数量3, true, true, true);
                                break;
                            default:
                                MapleInventoryManipulator.addById(c, 代码3, (short) 数量3, "", null, (byte) 0);
                                gainItem(代码3, (short) 数量3);
                                break;
                        }
                    }
                    PreparedStatement ps1 = null;
                    ResultSet rs1 = null;
                    try {
                        ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM mail WHERE number = ?");
                        ps1.setInt(1, id);
                        rs1 = ps1.executeQuery();
                        if (rs1.next()) {
                            String sqlstr = " delete from mail where number = " + id + "";
                            ps1.executeUpdate(sqlstr);
                        }
                    } catch (SQLException ex) {

                    }
                }
            }
        } catch (SQLException ex) {
        }
    }

    public String 显示邮件内容(int id) {
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mail");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("number") == id) {
                    String 邮件标题 = rs.getString("biaoti");
                    String 邮件时间 = rs.getString("shijian");
                    String 邮件正文 = rs.getString("wenben");
                    name.append("标题: #b").append(邮件标题).append("#k\r\n");
                    name.append("时间: #b").append(邮件时间).append("#k\r\n");
                    name.append("发件: #b系统管理员#k\r\n");
                    name.append("正文: #b").append(邮件正文).append("#k\r\n\r\n");
                    //4=金币，1=点券，2=抵用，3=经验
                    if (rs.getInt("type1") != 0) {
                        switch (rs.getInt("type1")) {
                            case 4:
                                name.append("[附件]:#d金币 x #b").append(rs.getInt("shuliang1")).append("#k\r\n");
                                break;
                            case 1:
                                name.append("[附件]:#d点券 x #b").append(rs.getInt("shuliang1")).append("#k\r\n");
                                break;
                            case 2:
                                name.append("[附件]:#d抵用 x #b").append(rs.getInt("shuliang1")).append("#k\r\n");
                                break;
                            case 3:
                                name.append("[附件]:#d经验 x #b").append(rs.getInt("shuliang1")).append("#k\r\n");
                                break;
                            default:
                                name.append("[附件]:#v").append(rs.getInt("type1")).append("# #b#t").append(rs.getInt("type1")).append("##k x #b").append(rs.getInt("shuliang1")).append("#k\r\n");
                                break;
                        }
                    }
                    if (rs.getInt("type2") != 0) {
                        switch (rs.getInt("type2")) {
                            case 4:
                                name.append("[附件]:#d金币 x #b").append(rs.getInt("shuliang2")).append("#k\r\n");
                                break;
                            case 1:
                                name.append("[附件]:#d点券 x #b").append(rs.getInt("shuliang2")).append("#k\r\n");
                                break;
                            case 2:
                                name.append("[附件]:#d抵用 x #b").append(rs.getInt("shuliang2")).append("#k\r\n");
                                break;
                            case 3:
                                name.append("[附件]:#d经验 x #b").append(rs.getInt("shuliang2")).append("#k\r\n");
                                break;
                            default:
                                name.append("[附件]:#v").append(rs.getInt("type2")).append("# #b#t").append(rs.getInt("type2")).append("##k x #b").append(rs.getInt("shuliang2")).append("#k\r\n");
                                break;
                        }
                    }
                    if (rs.getInt("type3") != 0) {
                        switch (rs.getInt("type3")) {
                            case 4:
                                name.append("[附件]:#d金币 x #b").append(rs.getInt("shuliang3")).append("#k\r\n");
                                break;
                            case 1:
                                name.append("[附件]:#d点券 x #b").append(rs.getInt("shuliang3")).append("#k\r\n");
                                break;
                            case 2:
                                name.append("[附件]:#d抵用 x #b").append(rs.getInt("shuliang3")).append("#k\r\n");
                                break;
                            case 3:
                                name.append("[附件]:#d经验 x #b").append(rs.getInt("shuliang3")).append("#k\r\n");
                                break;
                            default:
                                name.append("[附件]:#v").append(rs.getInt("type3")).append("# #b#t").append(rs.getInt("type3")).append("##k x #b").append(rs.getInt("shuliang3")).append("#k\r\n");
                                break;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public void 删除邮件(int a) {
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM mail WHERE number = ?");
            ps1.setInt(1, a);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                String sqlstr = " delete from mail where number = " + a + "";
                ps1.executeUpdate(sqlstr);
            }
        } catch (SQLException ex) {

        }
    }

    /**
     ******************************************************************************************************************************************************
     * 银行系统 1.养殖房间编号<id>
     * 2.养殖房间名字<mingzi>
     * 3.养殖房间主人<name>
     * 4.养殖房间主人<cid>
     * 5.养殖房间植物经验
     * *****************************************************************************************************************************************************
     */
    /**
     ******************************************************************************************************************************************************
     * 永恒重生
     *
     * *****************************************************************************************************************************************************
     * @return
     */
    public String 显示永生重生升级需要的经验() {
        StringBuilder name = new StringBuilder();
        String 等级 = "";
        for (int i = 1; i <= 30; i++) {
            等级 = "装备等级" + i + "";
            name.append("等级 [" + 等级 + "], 需要经验 (" + gui.Start.ConfigValuesMap.get(等级) + ")\r\n");
        }
        return name.toString();
    }

    /**
     ******************************************************************************************************************************************************
     * 传送点
     *
     * *****************************************************************************************************************************************************
     */
    public int 判断传送点x(int id, int cid) {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM awarp WHERE id = ? and cid = ?");
            ps.setInt(1, id);
            ps.setInt(2, cid);
            ResultSet rs = ps.executeQuery();
            rs.next();
            ret = rs.getInt("x");
            rs.close();
            ps.close();
        } catch (SQLException ex) {
        }
        return ret;
    }

    public int 判断传送点y(int id, int cid) {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM awarp WHERE id = ? and cid = ?");
            ps.setInt(1, id);
            ps.setInt(2, cid);
            ResultSet rs = ps.executeQuery();
            rs.next();
            ret = rs.getInt("y");
            rs.close();
            ps.close();
        } catch (SQLException ex) {
        }
        return ret;
    }

    public void 设置传送点x(int id, int cid, int x) {
        try {
            int ret = 判断传送点x(id, cid);
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO awarp (id, cid,x) VALUES (?, ?, ?)");
                    ps.setInt(1, id);
                    ps.setInt(2, cid);
                    ps.setInt(3, x);
                    ps.execute();
                } catch (SQLException e) {
                    System.out.println("设置传送点x1:" + e);
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("设置传送点x2:" + e);
                    }
                }
            } else {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE awarp SET `x` = ? WHERE id = ? and cid = ?");
                ps.setInt(1, x);
                ps.setInt(2, id);
                ps.setInt(3, cid);
                ps.execute();
                ps.close();
            }
        } catch (SQLException sql) {
            System.err.println("设置传送点x3" + sql);
        }
    }

    public void 设置传送点y(int id, int cid, int y) {
        try {
            int ret = 判断传送点x(id, cid);
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO awarp (id, cid,x) VALUES (?, ?, ?)");
                    ps.setInt(1, id);
                    ps.setInt(2, cid);
                    ps.setInt(3, y);

                    ps.execute();
                } catch (SQLException e) {
                    System.out.println("设置传送点y1:" + e);
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("设置传送点y2:" + e);
                    }
                }
            } else {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE awarp SET `y` = ? WHERE id = ? and cid = ?");
                ps.setInt(1, y);
                ps.setInt(2, id);
                ps.setInt(3, cid);
                ps.execute();
                ps.close();
            }
        } catch (SQLException sql) {
            System.err.println("设置传送点y3" + sql);
        }
    }

    /**
     ******************************************************************************************************************************************************
     * *任务发布目录表表<task>
     * 任务编号<a>
     * 任务发布人<b>
     * 任务发布时间<c>
     * 任务是否被完成<d>
     * 任务完成的人<e>
     * 任务标题<f>
     * 任务正文<g>
     * *任务发布收集材料表<task_1>
     * 任务编号<a>
     * 任务需求材料编号<b>
     * 任务需求材料数量<c>
     * *任务发布收集材料表<task_2>
     * 任务编号<a>
     * 任务奖励类型<b> * 1点券，2抵用，3金币，4经验，5人气 任务奖励数量<c>
     * *****************************************************************************************************************************************************
     */
    public String 显示任务内容(int id) {
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mail");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("a") == id) {
                    String 邮件标题 = rs.getString("f");
                    String 邮件时间 = rs.getString("c");
                    String 邮件正文 = rs.getString("g");
                    String 发布人 = 角色ID取名字(rs.getInt("b"));
                    name.append("任务标题: #b").append(邮件标题).append("#k\r\n");
                    name.append("发布时间: #b").append(邮件时间).append("#k\r\n");
                    name.append("任务正文: #b").append(邮件正文).append("#k\r\n");
                    name.append("发 布 人: #b").append(发布人).append("#k\r\n\r\n");
                }
            }
        } catch (SQLException ex) {
        }
        name.append("任务需求\r\n");
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM mail_1");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("a") == id) {
                    int 需要材料 = rs.getInt("b");
                    int 需要数量 = rs.getInt("c");
                    switch (rs.getInt("需要材料")) {
                        case 1:
                            name.append("[材料]:#d点券 x #b" + 需要数量 + "#k\r\n");
                            break;
                        case 2:
                            name.append("[材料]:#d抵用 x #b" + 需要数量 + "#k\r\n");
                            break;
                        case 3:
                            name.append("[材料]:#d金币 x #b" + 需要数量 + "#k\r\n");
                            break;
                        case 4:
                            name.append("[材料]:#d经验 x #b" + 需要数量 + "#k\r\n");
                            break;
                        case 5:
                            name.append("[材料]:#d人气 x #b" + 需要数量 + "#k\r\n");
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (SQLException ex) {
        }
        name.append("任务奖励\r\n");

        return name.toString();

    }

    public String 显示任务目录() {
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM task");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String 进度 = "";
                if (rs.getInt("d") == 0) {
                    进度 = "#r[未完成]#k";
                } else {
                    进度 = "#g[已完成]#k";
                }

                int 任务编号 = rs.getInt("a");
                String 任务标题 = rs.getString("f");
                name.append(" #L" + 任务编号 + "#" + 进度 + " #b" + 任务标题 + "#k#l\r\n");
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    /**
     * ****************************************************************************************************************************************************
     * 排行榜
     *
     * *****************************************************************************************************************************************************
     * @return
     */
    //新排行榜
    public String 等级排行榜() {
        int 名次 = 1;
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters  WHERE gm = 0 order by level desc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("level") < gui.Start.ConfigValuesMap.get("冒险家等级上限") && rs.getInt("level") > 30) {
                    if (名次 < 10) {
                        String 玩家名字 = rs.getString("name");
                        String 职业 = 职业(rs.getInt("job"));
                        name.append("Top.#e#d").append(名次).append("#n#k   ");
                        name.append("#b").append(玩家名字).append("#k");
                        for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("  ").append(职业).append("");
                        for (int j = 15 - 职业.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("  Lv.#d").append(rs.getInt("level")).append("#k\r\n");
                        名次++;
                    } else if (名次 >= 10 && 名次 <= 99) {
                        String 玩家名字 = rs.getString("name");
                        String 职业 = 职业(rs.getInt("job"));
                        name.append("Top.#e#d").append(名次).append("#n#k  ");
                        name.append("#b").append(玩家名字).append("#k");
                        for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("  ").append(职业).append("");
                        for (int j = 15 - 职业.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("  Lv.#d").append(rs.getInt("level")).append("#k\r\n");
                        名次++;
                    } else if (名次 > 99) {
                        String 玩家名字 = rs.getString("name");
                        String 职业 = 职业(rs.getInt("job"));
                        name.append("Top.#e#d").append(名次).append("#n#k ");
                        name.append("#b").append(玩家名字).append("#k");
                        for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("  ").append(职业).append("");
                        for (int j = 15 - 职业.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("  Lv.#d").append(rs.getInt("level")).append("#k\r\n");
                        名次++;

                    }
                }
            }
        } catch (SQLException ex) {
        }
        name.append("\r\n\r\n");
        return name.toString();
    }

    public String 满级排行榜() {
        int 名次 = 1;
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters  WHERE gm = 0 order by level desc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("level") == gui.Start.ConfigValuesMap.get("冒险家等级上限")) {
                    String 玩家名字 = rs.getString("name");
                    String 职业 = 职业(rs.getInt("job"));
                    int 家族编号 = rs.getInt("guildid");
                    name.append("    ");
                    name.append("#b").append(玩家名字).append("#k");
                    for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                        name.append(" ");
                    }
                    name.append("  ").append(职业).append("");
                    for (int j = 15 - 职业.getBytes().length; j > 0; j--) {
                        name.append(" ");
                    }
                    name.append("家族.#d").append(获取家族名称(家族编号)).append("#k\r\n");
                }
            }
        } catch (SQLException ex) {
        }
        name.append("\r\n\r\n");
        return name.toString();
    }

    public String 怪物卡片排行榜() {
        int 名次 = 1;
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM monsterbook   order by level desc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("level") == gui.Start.ConfigValuesMap.get("冒险家等级上限")) {
                    String 玩家名字 = rs.getString("name");
                    String 职业 = 职业(rs.getInt("job"));
                    int 家族编号 = rs.getInt("guildid");
                    name.append("    ");
                    name.append("#b").append(玩家名字).append("#k");
                    for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                        name.append(" ");
                    }
                    name.append("  ").append(职业).append("");
                    for (int j = 15 - 职业.getBytes().length; j > 0; j--) {
                        name.append(" ");
                    }
                    name.append("家族.#d").append(获取家族名称(家族编号)).append("#k\r\n");
                }
            }
        } catch (SQLException ex) {
        }
        name.append("\r\n\r\n");
        return name.toString();
    }

    //新排行榜
    public String 财富排行榜() {
        int 名次 = 1;
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE gm = 0 order by meso desc LIMIT 20 ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("meso") > 0) {
                    if (名次 < 10) {
                        String 玩家名字 = rs.getString("name");
                        String 金币 = rs.getString("meso");
                        name.append("Top.#e#d").append(名次).append("#n#k   ");
                        name.append("#b").append(玩家名字).append("#k");
                        for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("     Meso.#d").append(金币).append("#n\r\n");

                        名次++;
                    } else if (名次 >= 10 && 名次 <= 20) {
                        String 玩家名字 = rs.getString("name");
                        String 金币 = rs.getString("meso");
                        name.append("Top.#e#d").append(名次).append("#n#k  ");
                        name.append("#b").append(玩家名字).append("#k");
                        for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("     Meso.#d").append(金币).append("#n\r\n");

                        名次++;
                    }
                }
            }
        } catch (SQLException ex) {
        }
        name.append("\r\n\r\n");
        return name.toString();
    }

    //在线排行榜
    public String 在线排行榜() {
        int 名次 = 1;
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters WHERE gm = 0 order by totalOnlineTime desc LIMIT 20 ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("totalOnlineTime") > 0) {
                    if (名次 < 10) {
                        String 玩家名字 = rs.getString("name");
                        String 总在线 = rs.getString("totalOnlineTime");
                        String 今在线 = rs.getString("todayOnlineTime");
                        name.append("Top.#e#d").append(名次).append("#n#k   ");
                        name.append("#b").append(玩家名字).append("#k");
                        for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("     (tal/day).#d[").append(总在线).append(" / ").append(今在线).append("])\r\n");

                        名次++;
                    } else if (名次 >= 10 && 名次 <= 20) {
                        String 玩家名字 = rs.getString("name");
                        String 总在线 = rs.getString("totalOnlineTime");
                        String 今在线 = rs.getString("todayOnlineTime");
                        name.append("Top.#e#d").append(名次).append("#n#k  ");
                        name.append("#b").append(玩家名字).append("#k");
                        for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                            name.append(" ");
                        }
                        name.append("     (tal/day).#d[").append(总在线).append(" / ").append(今在线).append("])\r\n");
                        名次++;
                    }
                }
            }
        } catch (SQLException ex) {
        }
        name.append("\r\n\r\n");
        return name.toString();
    }

    //在线排行榜
    public String 永恒重生排行榜() {
        int 名次 = 1;
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM inventoryequipment order by itemlevel desc LIMIT 20");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("itemlevel") > 0) {
                    if (名次 < 10) {
                        int 玩家ID = 道具id获取主人(rs.getInt("inventoryitemid"));
                        String 玩家名字 = 角色ID取名字(玩家ID);
                        if (角色ID取GM(玩家ID) == 0) {
                            int 道具IP = 道具id获取道具ID(rs.getInt("inventoryitemid"));
                            name.append("Top.#e#d").append(名次).append("#n#k   ");
                            name.append("拥有者:#b").append(玩家名字).append("#k");
                            for (int j = 15 - 玩家名字.getBytes().length; j > 0; j--) {
                                name.append(" ");
                            }
                            name.append(" lv.#r").append(rs.getInt("itemlevel")).append("#k #b#t").append(道具IP).append("##k\r\n");
                            名次++;
                        }
                    } else if (名次 >= 10 && 名次 <= 20) {
                        int 玩家ID = 道具id获取主人(rs.getInt("inventoryitemid"));
                        String 玩家名字 = 角色ID取名字(玩家ID);
                        if (角色ID取GM(玩家ID) == 0) {
                            int 道具IP = 道具id获取道具ID(rs.getInt("inventoryitemid"));
                            name.append("Top.#e#d").append(名次).append("#n#k  ");
                            name.append("拥有者:#b").append(玩家名字).append("#k");
                            for (int j = 15 - 玩家名字.getBytes().length; j > 0; j--) {
                                name.append(" ");
                            }
                            name.append(" lv.#r").append(rs.getInt("itemlevel")).append("#k #b#t").append(道具IP).append("##k\r\n");
                            名次++;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
        }
        name.append("\r\n\r\n");
        return name.toString();
    }

    public static int 道具id获取道具ID(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE inventoryitemid = " + a + "");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {//itemid
                data = rs.getInt("itemid");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public static int 道具id获取主人(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM inventoryitems WHERE inventoryitemid = " + a + "");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {//itemid
                data = rs.getInt("characterid");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    /**
     * ****************************************************************************************************************************************************
     * 显示家族，并且加入家族
     *
     * *****************************************************************************************************************************************************
     */
    public String 显示所有家族() {
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM guilds order by GP desc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int 家族编号 = rs.getInt("guildid");
                String 家族名字 = rs.getString("name");
                int 家族GP点 = 家族成员数(家族编号);
                name.append("  #L").append(家族编号).append("##d家族:#b").append(家族名字).append("#k");
                for (int j = 13 - 家族名字.getBytes().length; j > 0; j--) {
                    name.append(" ");
                }
                String 玩家名字 = 角色ID取名字(rs.getInt("leader"));
                name.append("#d族长:#r").append(玩家名字).append("#k");
                for (int j = 13 - 玩家名字.getBytes().length; j > 0; j--) {
                    name.append(" ");
                }
                name.append("#d人数:#r").append(家族GP点).append("#k#l\r\n");
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    //加入的家族，角色
    public void 加入家族(int a, int b) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE characters SET `guildid` = ? WHERE id = ?");
            ps.setInt(1, a);
            ps.setInt(2, b);
            ps.execute();
            ps.close();

            c.getPlayer().setGuildId(a);
            c.getPlayer().setGuildRank((byte) 5);
            int s = World.Guild.addGuildMember(c.getPlayer().getMGC());
            if (s == 0) {
                c.getPlayer().dropMessage(1, "你想要加入的家族已经满员了，所以后续的操作可能已经无效了。");
                c.getPlayer().setGuildId(0);
                return;
            }
            c.sendPacket(MaplePacketCreator.showGuildInfo(c.getPlayer()));
            c.getPlayer().saveGuildStatus();
        } catch (SQLException sql) {
            System.err.println("加入家族错误:" + sql);
        }
    }

    public int 显示决斗开关() {
        if (gui.Start.个人信息设置.get("" + c.getPlayer().getName() + "2") == null) {
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
                        ps.close();
                    }
                }
                ps.close();
            } catch (SQLException ex) {

            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO jiezoudashi ( id,name,Val ) VALUES ( ? ,?,?)")) {
                ps.setInt(1, ID);
                ps.setString(2, c.getPlayer().getName() + "2");
                ps.setInt(3, 0);
                ps.executeUpdate();
                读取技个人信息设置();
            } catch (SQLException ex) {

            }
        }
        return gui.Start.个人信息设置.get("" + c.getPlayer().getName() + "2");
    }

    public void 打开个人决斗() {

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE jiezoudashi SET Val = ? WHERE name = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM jiezoudashi WHERE name = ?");
            ps1.setString(1, c.getPlayer().getName() + "2");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update jiezoudashi set Val =  '1' where name = '" + c.getPlayer().getName() + "2" + "';";
                PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                Val.executeUpdate(sqlString1);
                读取技个人信息设置();
            }
        } catch (SQLException ex) {

        }

    }

    public void 关闭个人决斗() {
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE jiezoudashi SET Val = ? WHERE name = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM jiezoudashi WHERE name = ?");
            ps1.setString(1, c.getPlayer().getName() + "2");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update jiezoudashi set Val = '0' where name = '" + c.getPlayer().getName() + "2" + "';";
                PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                Val.executeUpdate(sqlString1);
                读取技个人信息设置();
            }
        } catch (SQLException ex) {

        }
    }

    public int 显示伤害详细() {
        if (gui.Start.个人信息设置.get("" + c.getPlayer().getName() + "1") == null) {
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
                        ps.close();
                    }
                }
                ps.close();
            } catch (SQLException ex) {

            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO jiezoudashi ( id,name,Val ) VALUES ( ? ,?,?)")) {
                ps.setInt(1, ID);
                ps.setString(2, c.getPlayer().getName() + "1");
                ps.setInt(3, 0);
                ps.executeUpdate();
                读取技个人信息设置();
            } catch (SQLException ex) {

            }
        }
        return gui.Start.个人信息设置.get("" + c.getPlayer().getName() + "1");
    }

    public void 打开伤害详细() {

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE jiezoudashi SET Val = ? WHERE name = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM jiezoudashi WHERE name = ?");
            ps1.setString(1, c.getPlayer().getName() + "1");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update jiezoudashi set Val = '1' where name = '" + c.getPlayer().getName() + "1" + "';";
                PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                Val.executeUpdate(sqlString1);
                读取技个人信息设置();
            }
        } catch (SQLException ex) {

        }

    }

    public void 关闭伤害详细() {

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE jiezoudashi SET Val = ? WHERE name = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM jiezoudashi WHERE name = ?");
            ps1.setString(1, c.getPlayer().getName() + "1");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update jiezoudashi set Val = '0' where name = '" + c.getPlayer().getName() + "1" + "';";
                PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                Val.executeUpdate(sqlString1);
                读取技个人信息设置();
            }
        } catch (SQLException ex) {

        }

    }

    public int 显示群聊天开关() {
        if (gui.Start.个人信息设置.get("" + c.getPlayer().getName() + "10") == null) {
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
                        ps.close();
                    }
                }
                ps.close();
            } catch (SQLException ex) {

            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO jiezoudashi ( id,name,Val ) VALUES ( ? ,?,?)")) {
                ps.setInt(1, ID);
                ps.setString(2, c.getPlayer().getName() + "10");
                ps.setInt(3, 0);
                ps.executeUpdate();
                读取技个人信息设置();
            } catch (SQLException ex) {

            }
        }
        return gui.Start.个人信息设置.get("" + c.getPlayer().getName() + "10");
    }

    public void 打开群聊显示() {

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE jiezoudashi SET Val = ? WHERE name = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM jiezoudashi WHERE name = ?");
            ps1.setString(1, c.getPlayer().getName() + "10");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update jiezoudashi set Val = '1' where name = '" + c.getPlayer().getName() + "10" + "';";
                PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                Val.executeUpdate(sqlString1);
                读取技个人信息设置();
            }
        } catch (SQLException ex) {

        }

    }

    public void 关闭群聊显示() {

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE jiezoudashi SET Val = ? WHERE name = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM jiezoudashi WHERE name = ?");
            ps1.setString(1, c.getPlayer().getName() + "10");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update jiezoudashi set Val = '0' where name = '" + c.getPlayer().getName() + "10" + "';";
                PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                Val.executeUpdate(sqlString1);
                读取技个人信息设置();
            }
        } catch (SQLException ex) {

        }
    }

    public int 显示群聊天开关2() {
        if (gui.Start.个人信息设置.get("" + c.getPlayer().getName() + "11") == null) {
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
                        ps.close();
                    }
                }
                ps.close();
            } catch (SQLException ex) {

            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO jiezoudashi ( id,name,Val ) VALUES ( ? ,?,?)")) {
                ps.setInt(1, ID);
                ps.setString(2, c.getPlayer().getName() + "11");
                ps.setInt(3, 0);
                ps.executeUpdate();
                读取技个人信息设置();
            } catch (SQLException ex) {

            }
        }
        return gui.Start.个人信息设置.get("" + c.getPlayer().getName() + "11");
    }

    public void 打开群聊显示2() {

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE jiezoudashi SET Val = ? WHERE name = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM jiezoudashi WHERE name = ?");
            ps1.setString(1, c.getPlayer().getName() + "11");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update jiezoudashi set Val = '1' where name = '" + c.getPlayer().getName() + "11" + "';";
                PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                Val.executeUpdate(sqlString1);
                读取技个人信息设置();
            }
        } catch (SQLException ex) {

        }

    }

    public void 关闭群聊显示2() {

        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("UPDATE jiezoudashi SET Val = ? WHERE name = ?");
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM jiezoudashi WHERE name = ?");
            ps1.setString(1, c.getPlayer().getName() + "11");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update jiezoudashi set Val = '0' where name = '" + c.getPlayer().getName() + "11" + "';";
                PreparedStatement Val = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                Val.executeUpdate(sqlString1);
                读取技个人信息设置();
            }
        } catch (SQLException ex) {

        }
    }

    /**
     * <9>
     */
    public int 判断第一阶段是否完成() {
        int a = 0;
        if (c.getPlayer().getBossLog("送货104000100") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货104000200") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货104000300") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货104000400") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货104010000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货104020000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货104030000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货104040000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货100000000") > 0) {
            a += 1;
        }
        return a;
    }

    /**
     * <7>
     */
    public int 判断第二阶段是否完成() {
        int a = 0;
        if (c.getPlayer().getBossLog("送货100010000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货100020000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货100030000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货100040000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货100040100") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货100050000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货101000000") > 0) {
            a += 1;
        }
        return a;
    }

    /**
     * <10>
     */
    public int 判断第三阶段是否完成() {
        int a = 0;
        if (c.getPlayer().getBossLog("送货101010000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货101010100") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货101020000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货101030000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货101030100") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货101030200") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货101030300") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货101030400") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货101040000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货102000000") > 0) {
            a += 1;
        }
        return a;
    }

    /**
     * <6>
     */
    public int 判断第四阶段是否完成() {
        int a = 0;
        if (c.getPlayer().getBossLog("送货102010000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货102020000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货102030000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货102040000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货102050000") > 0) {
            a += 1;
        }
        if (c.getPlayer().getBossLog("送货103000000") > 0) {
            a += 1;
        }

        return a;
    }

    /**
     * <手册>
     */
    public void 录入手册(int a, int b, int c) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO shouce (b,c,d) VALUES ( ?, ?, ?)");
            ps.setInt(1, a);
            ps.setInt(2, b);
            ps.setInt(3, c);
            ps.execute();
            ps.close();
        } catch (SQLException sql) {
            System.err.println("录入手册!!55" + sql);
        }
    }

    public String 显示手册内容(int a) {
        StringBuilder name = new StringBuilder();

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM shouce order by c desc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("d") == a) {
                    int 编号 = rs.getInt("a");
                    int 代码 = rs.getInt("b");
                    int 数量 = rs.getInt("c");
                    String 物品名字 = MapleItemInformationProvider.getInstance().getName(rs.getInt("b"));
                    String 物品数量 = "" + 数量 + "";
                    name.append("#L").append(编号).append("##b#z").append(代码).append("##k ");
                    for (int j = 21 - 物品名字.getBytes().length; j > 0; j--) {
                        name.append(" ");
                    }
                    name.append("│册 #r").append(数量).append("#k");
                    for (int j = 8 - 物品数量.getBytes().length; j > 0; j--) {
                        name.append(" ");
                    }
                    name.append("│包 #b#c").append(代码).append("##k#l\r\n");

                }
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public static int 手册道具代码(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT b as DATA FROM shouce WHERE a = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("手册道具代码、出错");
        }
        return data;
    }

    public static int 手册道具数量(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT c as DATA FROM shouce WHERE a = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("手册道具数量、出错");
        }
        return data;
    }

    public static void 修改手册道具数量(int a, int b) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM shouce");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("a").equals("" + a + "")) {
                    String aa = null;
                    aa = "update shouce set c =" + b + " where a = " + a + ";";
                    PreparedStatement S = DatabaseConnection.getConnection().prepareStatement(aa);
                    S.executeUpdate(aa);
                }
            }
            ps.close();
        } catch (SQLException ex) {
        }

    }

    public static void 删除不存在() {
        PreparedStatement ps1 = null;
        ResultSet rs = null;

        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM shouce");
            rs = ps1.executeQuery();
            while (rs.next()) {
                String sqlstr = " delete from shouce where c = 0";
                ps1.executeUpdate(sqlstr);
            }
            ps1.close();
        } catch (SQLException ex) {

        }

    }

    public static void 新增手册收藏(int a, int b, int c) {
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO shouce ( b,c,d ) VALUES ( ? ,?,? )")) {
            ps.setInt(1, a);
            ps.setInt(2, b);
            ps.setInt(3, c);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {

        }
    }

    public int OX道题活动() {//var count = cm.获取指定地图玩家数量(910000000);
        return MapleParty.OX答题活动;
    }

    public int 神秘商人() {
        return MapleParty.神秘商人时间;
    }

    public int 神秘商人2() {
        return MapleParty.神秘商人频道;
    }

    public int 神秘商人3() {
        return MapleParty.神秘商人地图;
    }

    public int 通缉令1() {
        return MapleParty.通缉BOSS;
    }

    public int 通缉令2() {
        return MapleParty.通缉地图;
    }

    public int 雪球赛() {
        return MapleParty.雪球赛;
    }

    public String 幸运职业() {
        String 职业1 = getJobNameById((MapleParty.幸运职业 - 1));
        String 职业2 = getJobNameById((MapleParty.幸运职业));
        String 职业3 = getJobNameById((MapleParty.幸运职业 + 1));
        String 职业 = 职业1 + "," + 职业2 + "," + 职业3;
        return 职业;
    }

    public String 显示修炼(int a) {
        StringBuilder name = new StringBuilder();
        if (c.getPlayer().getLevel() >= 160) {
            读取技个人信息设置();
            DecimalFormat 精确显示 = new DecimalFormat("##0.0000");
            DecimalFormat 精确显示2 = new DecimalFormat("###");
            int 契合 = (gui.Start.个人信息设置.get("BUFF增益" + a + ""));
            int 物理攻击力 = gui.Start.个人信息设置.get("物理攻击力" + a + "");
            int 魔法攻击力 = gui.Start.个人信息设置.get("魔法攻击力" + a + "");
            String 阶级 = "";
            if (契合 > 50 && 契合 <= 100) {
                阶级 = "初级";
            } else if (契合 > 100 && 契合 <= 150) {
                阶级 = "中级";
            } else if (契合 > 150 && 契合 <= 200) {
                阶级 = "高级";
            } else {
                阶级 = "入门";
            }

            name.append("状态:#b仙人模式 : #r").append(阶级).append("#k\r\n\r\n");
            name.append("    #d开启此状态，各项属性得到巨额增涨，各属性值可通过修炼提升。#k\r\n");
            name.append("\r\n状态维持；\r\n");
            if (契合 > 50 && 契合 <= 100) {
                name.append("→ 每 #r5#k 秒消耗 #r15%#k 的最大法力值,最大生命值\r\n");
            } else if (契合 > 100 && 契合 <= 150) {
                name.append("→ 每 #r4#k 秒消耗 #r15%#k 的最大法力值,最大生命值\r\n");
            } else if (契合 > 150) {
                name.append("→ 每 #r4#k 秒消耗 #r20%#k 的最大法力值,最大生命值\r\n");
            } else {
                name.append("→ 每 #r5#k 秒消耗 #r10%#k 的最大法力值,最大生命值\r\n");
            }
            name.append("→ 每 #r20000#b(-").append(精确显示.format(gui.Start.个人信息设置.get("聪明睿智" + a + "") * gui.Start.个人信息设置.get("BUFF增益" + a + "") * 0.0001)).append(")#k毫秒获取增益BUFF\r\n");
            name.append("→ 增加 #r30%#k 受到的伤害\r\n");
            name.append("→ 增加 #r30#b(").append(精确显示.format((物理攻击力 + 魔法攻击力) * 0.5 * 契合 * 0.00001)).append(")#r%#k 对高级怪物的伤害\r\n");
            name.append("\r\n状态属性；\r\n");
            /**
             * <能力契合,10000>
             */
            String 介绍1 = "#d能力契合#k:#r" + 契合 + "#k";
            name.append(介绍1);
            for (int j = 43 - 介绍1.getBytes().length; j > 0; j--) {
                name.append(" ");
            }
            name.append("仙人模式熟练度\r\n");
            String 介绍2 = "#d硬化皮肤#k:#b" + 精确显示.format(gui.Start.个人信息设置.get("硬化皮肤" + a + "") * 契合 * 0.01) + "#r(" + gui.Start.个人信息设置.get("硬化皮肤" + a + "") + ")#k";
            name.append(介绍2);
            for (int j = 45 - 介绍2.getBytes().length; j > 0; j--) {
                name.append(" ");
            }
            name.append("减少受到的伤害\r\n");

            String 介绍5 = "#d聪明睿智#k:#b" + 精确显示.format(gui.Start.个人信息设置.get("聪明睿智" + a + "") * 契合 * 0.01) + "#r(" + gui.Start.个人信息设置.get("聪明睿智" + a + "") + ")#k";
            name.append(介绍5);
            for (int j = 45 - 介绍5.getBytes().length; j > 0; j--) {
                name.append(" ");
            }
            name.append("加快增益BUFF获取\r\n");

            /**
             * <物理攻击力>
             */
            String 介绍6 = "#d攻击力(物理)#k:#b" + 精确显示2.format(gui.Start.个人信息设置.get("物理攻击力" + a + "") * 契合 * 0.001) + "#r(" + gui.Start.个人信息设置.get("物理攻击力" + a + "") + ")#k";
            name.append(介绍6);
            for (int j = 45 - 介绍6.getBytes().length; j > 0; j--) {
                name.append(" ");
            }
            name.append("附加物理真实伤害\r\n");

            /**
             * <魔法攻击力>
             */
            String 介绍7 = "#d攻击力(魔法)#k:#b" + 精确显示2.format(gui.Start.个人信息设置.get("魔法攻击力" + a + "") * 契合 * 0.001) + "#r(" + gui.Start.个人信息设置.get("魔法攻击力" + a + "") + ")#k";
            name.append(介绍7);
            for (int j = 45 - 介绍7.getBytes().length; j > 0; j--) {
                name.append(" ");
            }
            name.append("附加魔法真实伤害\r\n");
            /**
             * <物理狂暴力>
             */
            String 介绍8 = "#d狂暴力(物理)#k:#b" + 精确显示.format(gui.Start.个人信息设置.get("物理狂暴力" + a + "") * 契合 * 0.002) + "#r(" + gui.Start.个人信息设置.get("物理狂暴力" + a + "") + ")#k";
            name.append(介绍8);
            for (int j = 45 - 介绍8.getBytes().length; j > 0; j--) {
                name.append(" ");
            }
            name.append("物理真实伤害暴击\r\n");
            /**
             * <魔法狂暴力>
             */
            String 介绍9 = "#d狂暴力(魔法)#k:#b" + 精确显示.format(gui.Start.个人信息设置.get("魔法狂暴力" + a + "") * 契合 * 0.002) + "#r(" + gui.Start.个人信息设置.get("魔法狂暴力" + a + "") + ")#k";
            name.append(介绍9);
            for (int j = 45 - 介绍9.getBytes().length; j > 0; j--) {
                name.append(" ");
            }
            name.append("魔法真实伤害暴击\r\n");
            /**
             * <物理吸收>
             */
            String 介绍10 = "#d吸收力(物理)#k:#b" + 精确显示.format(gui.Start.个人信息设置.get("物理吸收力" + a + "") * 契合 * 0.002) + "#r(" + gui.Start.个人信息设置.get("物理吸收力" + a + "") + ")#k";
            name.append(介绍10);
            for (int j = 45 - 介绍10.getBytes().length; j > 0; j--) {
                name.append(" ");
            }
            name.append("攻击时恢复HP\r\n");
            /**
             * <魔法吸收>
             */
            String 介绍11 = "#d吸收力(魔法)#k:#b" + 精确显示.format(gui.Start.个人信息设置.get("魔法吸收力" + a + "") * 契合 * 0.002) + "#r(" + gui.Start.个人信息设置.get("魔法吸收力" + a + "") + ")#k";
            name.append(介绍11);
            for (int j = 45 - 介绍11.getBytes().length; j > 0; j--) {
                name.append(" ");
            }
            name.append("攻击时恢复MP\r\n\r\n");
            name.append("#r#L3#修炼契合力#l\r\n");
            name.append("#r#L4#修炼硬化皮肤#l\r\n");
            name.append("#b#L1#修炼攻击力[物理]#l\r\n");
            name.append("#b#L2#修炼攻击力[魔法]#l\r\n");

        } else {
            name.append(" 角色 #b160#k 级后即可觉醒 #b仙人之力#k");
        }
        return name.toString();
    }

    public void 修炼硬化皮肤() {
        c.getPlayer().修炼硬化皮肤();
    }

    public void 修炼物理攻击力() {
        c.getPlayer().修炼物理攻击力();
    }

    public void 修炼BUFF增益() {
        c.getPlayer().修炼BUFF增益();
    }

    public void 修炼魔法攻击力() {
        c.getPlayer().修炼魔法攻击力();
    }

    public int 挂机检测验证码() {
        return c.getPlayer().挂机检测验证码;
    }

    public void 重置验证码() {
        c.getPlayer().挂机检测验证码 = 0;
        c.getPlayer().b = 0;
        c.getPlayer().d = 0;
    }

    public void 学习仙人模式(String a, int b) {
        if (gui.Start.个人信息设置.get(a) == null) {
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
                        ps.close();
                    }
                }
                ps.close();
            } catch (SQLException ex) {

            }
            try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO jiezoudashi ( id,name,Val ) VALUES ( ? ,?,?)")) {
                ps.setInt(1, ID);
                ps.setString(2, a);
                ps.setInt(3, b);
                ps.executeUpdate();
                读取技个人信息设置();
            } catch (SQLException ex) {

            }
        }

    }

    /**
     * <银行相关>
     */
    public String 显示银行列表() {
        StringBuilder name = new StringBuilder();

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM yinhang_1 ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int 编号 = rs.getInt("a");
                String 银行名字 = rs.getString("b");
                name.append("#L").append(编号).append("##b#z").append(银行名字).append("##k ");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public String 显示银行面板(int a) {
        StringBuilder name = new StringBuilder();

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM yinhang_1 ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int 编号 = rs.getInt("a");
                String 银行名字 = rs.getString("b");
                name.append("#L").append(编号).append("##b#z").append(银行名字).append("##k ");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public String 显示雇佣商人物品() {
        StringBuilder name = new StringBuilder();
        final byte state = checkExistance(c.getPlayer().getAccountID(), c.getPlayer().getId());
        boolean merch = World.hasMerchant(c.getPlayer().getAccountID());
        /**
         * <1.商店没开>
         * <2.>
         * <3.雇佣道具记录数据库里有东西>
         */
        if (!merch && state != 1 && 角色ID取雇佣数据(c.getPlayer().getId()) > 0) {
            name.append("   雇佣异常消失物品领回，请保证你背包装得下，不然会消失哦。如果发现道具数量不对，或者属性不对，请联系管理员。无法恢复附魔装备。\r\n\r\n");
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM hire where cid =" + c.getPlayer().getId() + "");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("itemid") < 2000000) {
                        name.append("#v").append(rs.getInt("itemid")).append("# #b#t").append(rs.getInt("itemid")).append("##k x 1\r\n");
                    } else {
                        name.append("#v").append(rs.getInt("itemid")).append("# #b#t").append(rs.getInt("itemid")).append("##k x ").append(rs.getInt("potential3")).append("\r\n");
                    }
                }
                ps.close();
            } catch (SQLException ex) {
            }
            name.append("\r\n#b#L1#[领回道具]#l");
        } else {
            name.append("你没有发生过异常。");
        }
        return name.toString();
    }

    public void 领回雇佣道具() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM hire where cid =" + c.getPlayer().getId() + "");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("itemid") < 2000000) {
                    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    final Equip item = (Equip) ii.getEquipById(rs.getInt("itemid"));
                    if (rs.getInt("upgradeslots") > 0) {
                        item.setUpgradeSlots((byte) rs.getInt("upgradeslots"));
                    }
                    if (rs.getInt("str") > 0) {
                        item.setStr((short) rs.getInt("str"));
                    }
                    if (rs.getInt("dex") > 0) {
                        item.setDex((short) rs.getInt("dex"));
                    }
                    if (rs.getInt("2int") > 0) {
                        item.setInt((short) rs.getInt("2int"));
                    }
                    if (rs.getInt("luk") > 0) {
                        item.setLuk((short) rs.getInt("luk"));
                    }
                    if (rs.getInt("watk") > 0) {
                        item.setWatk((short) rs.getInt("watk"));
                    }
                    if (rs.getInt("matk") > 0) {
                        item.setMatk((short) rs.getInt("matk"));
                    }
                    if (rs.getInt("wdef") > 0) {
                        item.setWdef((short) rs.getInt("wdef"));
                    }
                    if (rs.getInt("mdef") > 0) {
                        item.setMdef((short) rs.getInt("mdef"));
                    }
                    if (rs.getInt("hp") > 0) {
                        item.setHp((short) rs.getInt("hp"));
                    }
                    if (rs.getInt("mp") > 0) {
                        item.setMp((short) rs.getInt("mp"));
                    }
                    if (rs.getInt("acc") > 0) {
                        item.setAcc((short) rs.getInt("acc"));
                    }
                    if (rs.getInt("avoid") > 0) {
                        item.setAvoid((short) rs.getInt("avoid"));
                    }
                    if (rs.getInt("speed") > 0) {
                        item.setSpeed((short) rs.getInt("speed"));
                    }
                    if (rs.getInt("jump") > 0) {
                        item.setJump((short) rs.getInt("jump"));
                    }
                    MapleInventoryManipulator.addbyItem(c.getPlayer().getClient(), item.copy());
                } else {
                    gainItem(rs.getInt("itemid"), (short) rs.getInt("upgradeslots"));
                }
                删除道具(c.getPlayer().getId());
            }
        } catch (SQLException ex) {
        }
    }

    public void 删除道具(int a1) {
        try {
            ResultSet rs = null;
            PreparedStatement ps = null;
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM hire");
            rs = ps.executeQuery();
            if (rs.next()) {
                int a = 0;

                String sqlstr = " delete from hire where "
                        + "cid = " + a1 + " ";
                ps.executeUpdate(sqlstr);
                ps.close();
            }
        } catch (SQLException ex) {

        }
    }

    private final byte checkExistance(final int accid, final int charid) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where accountid = ? OR characterid = ?");
            ps.setInt(1, accid);
            ps.setInt(2, charid);
            //System.out.println("3");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ps.close();
                rs.close();
                return 1;
            }
            rs.close();
            ps.close();
            return 0;
        } catch (SQLException se) {
            return -1;
        }
    }

    public int 角色ID取雇佣数据(int id) {
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

    public int 取养殖数值(int a) {
        c.getPlayer().任务存档();
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM queststatus  WHERE characterid = " + c.getPlayer().getId() + " &&　quest = " + a + ";")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    data = Integer.parseInt(rs.getString("customData"));
                }
            }
        } catch (SQLException ex) {
        }
        return data;
    }

    public void 清养殖数值(int a) {
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM queststatus WHERE id = ?");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlstr = " delete from queststatus where characterid = " + c.getPlayer().getId() + " &&　quest=" + a + "";
                ps1.executeUpdate(sqlstr);
                c.getPlayer().任务存档();
            }
        } catch (SQLException ex) {

        }

    }

    public void 删除雇佣缓存() {
        try {
            ResultSet rs = null;
            PreparedStatement ps = null;
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM hiredmerch");
            rs = ps.executeQuery();
            if (rs.next()) {
                int a = 0;

                String sqlstr = " delete from hiredmerch where "
                        + "accountid = " + c.getPlayer().getAccountID() + " ";
                ps.executeUpdate(sqlstr);
                ps.close();

            }
        } catch (SQLException ex) {

        }
    }

    public String 查询爆物(int a) {
        StringBuilder name = new StringBuilder();
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM drop_data where itemid =" + a + "")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    name.append("    #o").append(rs.getInt("dropperid")).append("#\r\n");
                }
                ps.close();
            }
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public static int 银行账号(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bank WHERE a = " + id + "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data += 1;
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("银行账号、出错");
        }
        return data;
    }

    public static int 银行账号密码(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bank WHERE a = " + id + "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("b");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("银行账号密码、出错");
        }
        return data;
    }

    public static long 银行金币余额(int id) {
        long data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bank WHERE a = " + id + "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getLong("c");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("银行金币余额、出错");
        }
        return data;
    }

    public static long 银行点券余额(int id) {
        long data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bank WHERE a = " + id + "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getLong("d");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("银行点券余额、出错");
        }
        return data;
    }

    public static int 银行开户人(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bank WHERE a = " + id + "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("e");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("银行开户人、出错");
        }
        return data;
    }

    public static void 变更银行金币(int a, long b) {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("UPDATE bank SET c = " + b + " WHERE a = " + a + "");

            ps.executeUpdate();
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("变更银行金币出错: " + Ex);
        }
    }

    public static void 变更银行点券(int a, long b) {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("UPDATE bank SET d = " + b + " WHERE a = " + a + "");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("变更银行点券出错: " + Ex);
        }
    }

    //行号，转出转入，点券金币，文字
    public void 转账记录(int a, int d, int e, String b) {
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO bank_jl (a, d,e,b) VALUES (?,?,?,?)");
            ps.setInt(1, a);
            ps.setInt(2, d);
            ps.setInt(3, e);
            ps.setString(4, b);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("转账记录出错: " + Ex);
        }
    }

    public void 变更银行账号密码(int a, int b) {
        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("UPDATE bank SET b = " + b + " WHERE a = " + a + "");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("变更银行点券出错: " + Ex);
        }
    }

    public void 注册银行账号(int a, int b, int aa) {

        try {
            Connection con;
            con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO bank (a, b,e) VALUES (?,?,?)");
            ps.setInt(1, a);
            ps.setInt(2, b);
            ps.setInt(3, aa);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            return;
        }
    }

    public String 显示转账记录(int a, int b, int e) {
        StringBuilder name = new StringBuilder();

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM bank_jl  WHERE a = " + a + " && d = " + b + " && e = " + e + " order by c desc  LIMIT 100 ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                name.append("\t时间: #d").append(rs.getString("c")).append("#k \r\n\t#b").append(rs.getString("b")).append("#k\r\n\r\n");
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return name.toString();
    }

    public String 时间() {
        return CurrentReadable_Time();
    }

    public String 显示附魔效果(String a) {
        StringBuilder name = new StringBuilder();
        String arr1[] = a.split(",");
        for (int i = 0; i < arr1.length; i++) {
            String pair = arr1[i];
            if (pair.contains(":")) {
                String kongInfo = "●";
                String arr2[] = pair.split(":");
                int fumoType = Integer.parseInt(arr2[0]);
                int fumoVal = Integer.parseInt(arr2[1]);
                if (fumoType > 0 && Start.FuMoInfoMap.containsKey(fumoType)) {
                    String infoArr[] = Start.FuMoInfoMap.get(fumoType);
                    String fumoName = infoArr[0];
                    String fumoInfo = infoArr[1];
                    kongInfo += fumoName + " " + String.format(fumoInfo, fumoVal);
                } else {
                    kongInfo += "[未附魔]";
                }
                name.append("附魔 : ").append(kongInfo);
            }
        }
        return name.toString();
    }

    public static int 取个人副本通关时间最快(int a, int b) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM fubenjilu WHERE name = " + a + " && cid = " + b + "order by time asc");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("time");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("取副本通关时间最快 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    public static int 取副本通关最快时间(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM fubenjilu WHERE name = " + a + " order by time asc");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("time");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("取副本通关时间最快 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    public static int 取副本通关最快玩家(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM fubenjilu WHERE name = " + a + " order by time asc");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("cid");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("取副本通关时间最快 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    public static int 取副本通关时间(int a, int b) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT time as DATA FROM fubenjilu WHERE name = " + a + " && cid = " + b + "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getInt("DATA");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("取副本通关时间 - 数据库查询失败：" + Ex);
        }
        return data;
    }

    public void 写记录(int A, int B, int C) {
        try {
            PreparedStatement ps1 = null;
            ResultSet rs = null;
            Connection con;
            con = DatabaseConnection.getConnection();
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM fubenjilu ");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlstr = " Delete from fubenjilu where name ='" + A + "' && cid = " + B + "";
                ps1.executeUpdate(sqlstr);
            }
            PreparedStatement ps = con.prepareStatement("INSERT INTO fubenjilu (name, cid,time) VALUES (?,?,?)");
            ps.setInt(1, A);
            ps.setInt(2, B);
            ps.setInt(3, C);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            return;
        }
    }

    public String 显示在线玩家() {
        StringBuilder name = new StringBuilder();
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                if (c.getPlayer().getId() != chr.getId() && !chr.isGM()) {
                    if (!主城(chr.getMapId())) {
                        name.append("\t\t\t#b#L").append(chr.getId()).append("# 玩家: #r").append(chr.getName()).append("#l\r\n");
                    } else {
                        name.append("\t\t\t#b#L").append(chr.getId()).append("# 玩家: #d").append(chr.getName()).append("#l\r\n");
                    }
                }
            }
        }
        return name.toString();
    }

    public String 显示在线玩家详细(int a) {
        StringBuilder name = new StringBuilder();
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                if (chr == null) {
                    continue;
                }
                if (chr.getId() != a) {
                    continue;
                }
                if (!主城(chr.getMapId())) {
                    name.append("\t\t#d玩家名字:  #r").append(chr.getName()).append(" #k (").append(chr.getId()).append(")\r\n");
                    name.append("\t\t#d玩家等级:  #r").append(chr.getLevel()).append(" #k (").append(getJobNameById(chr.job)).append(")\r\n");
                    name.append("\t\t#d最高伤害:  #r").append(chr.最高伤害).append(" #k\r\n");
                    name.append("\t\t#d被攻击次:  #r").append(chr.被触碰次数).append(" #k\r\n");
                    name.append("\t\t#d累计掉血:  #r").append(chr.累计掉血).append(" #k\r\n");
                    name.append("\t\t#d所在地图:  [野外] #r").append(chr.getMap().getMapName()).append(" #k\r\n");
                    name.append("\t\t#d打怪数量:  #r").append(chr.打怪数量).append(" #k\r\n");
                    name.append("\t\t#d坐标误差:  #r").append(chr.X坐标误差).append(" #k(X)\r\n");
                    name.append("\t\t#d坐标误差:  #r").append(chr.Y坐标误差).append(" #k(Y)\r\n");
                    name.append("\t\t#d绑定QQ号:  #r").append(账号ID取绑定QQ(角色ID取账号ID(chr.getId()))).append(" #k\r\n");
                    name.append("\t\t\t   #b#L").append(chr.getId()).append("#[跟踪]#l  #L").append(chr.getId() + 1000000).append("#[封号]#l\r\n\r\n");
                    if (chr.地图缓存1 > 0 && !"".equals(chr.地图缓存时间1)) {
                        name.append("\t\t#d过图记录；\r\n");
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间1).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存1).append("##k\r\n");
                    }
                    if (chr.地图缓存2 > 0 && !"".equals(chr.地图缓存时间2)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间2).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存2).append("##k\r\n");
                    }
                    if (chr.地图缓存3 > 0 && !"".equals(chr.地图缓存时间3)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间3).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存3).append("##k\r\n");
                    }
                    if (chr.地图缓存4 > 0 && !"".equals(chr.地图缓存时间4)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间4).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存4).append("##k\r\n");
                    }
                    if (chr.地图缓存5 > 0 && !"".equals(chr.地图缓存时间5)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间5).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存5).append("##k\r\n");
                    }
                    if (chr.地图缓存6 > 0 && !"".equals(chr.地图缓存时间6)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间6).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存6).append("##k\r\n");
                    }
                    if (chr.地图缓存7 > 0 && !"".equals(chr.地图缓存时间7)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间7).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存7).append("##k\r\n");
                    }

                    if (chr.地图缓存8 > 0 && !"".equals(chr.地图缓存时间8)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间8).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存8).append("##k\r\n");
                    }
                    if (chr.地图缓存9 > 0 && !"".equals(chr.地图缓存时间9)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间9).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存9).append("##k\r\n");
                    }
                    if (chr.地图缓存10 > 0 && !"".equals(chr.地图缓存时间10)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间10).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存10).append("##k\r\n");
                    }
                } else {
                    name.append("\t\t#d玩家名字:  #r").append(chr.getName()).append(" #k (").append(chr.getId()).append(")\r\n");
                    name.append("\t\t#d玩家等级:  #r").append(chr.getLevel()).append(" #k (").append(getJobNameById(chr.job)).append(")\r\n");
                    name.append("\t\t#d最高伤害:  #r").append(chr.最高伤害).append(" #k\r\n");
                    name.append("\t\t#d被攻击次:  #r").append(chr.被触碰次数).append(" #k\r\n");
                    name.append("\t\t#d累计掉血:  #r").append(chr.累计掉血).append(" #k\r\n");
                    name.append("\t\t#d过图间隔:  #rnull  #k\r\n");
                    name.append("\t\t#d所在地图:  [主城] #r").append(chr.getMap().getMapName()).append(" #k\r\n");
                    name.append("\t\t#d打怪数量:  #r").append(chr.打怪数量).append(" #k\r\n");
                    name.append("\t\t#d坐标误差:  #r").append(chr.X坐标误差).append(" #k(X)\r\n");
                    name.append("\t\t#d坐标误差:  #r").append(chr.Y坐标误差).append(" #k(Y)\r\n");
                    name.append("\t\t#d绑定QQ号:  #r").append(账号ID取绑定QQ(角色ID取账号ID(chr.getId()))).append(" #k\r\n");
                    name.append("\t\t\t   #b#L").append(chr.getId()).append("#[跟踪]#l  #L").append(chr.getId() + 1000000).append("#[封号]#l\r\n\r\n");
                    if (chr.地图缓存1 > 0 && !"".equals(chr.地图缓存时间1)) {
                        name.append("\t\t#d过图记录；\r\n");
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间1).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存1).append("##k\r\n");
                    }
                    if (chr.地图缓存2 > 0 && !"".equals(chr.地图缓存时间2)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间2).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存2).append("##k\r\n");
                    }
                    if (chr.地图缓存3 > 0 && !"".equals(chr.地图缓存时间3)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间3).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存3).append("##k\r\n");
                    }
                    if (chr.地图缓存4 > 0 && !"".equals(chr.地图缓存时间4)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间4).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存4).append("##k\r\n");
                    }
                    if (chr.地图缓存5 > 0 && !"".equals(chr.地图缓存时间5)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间5).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存5).append("##k\r\n");
                    }
                    if (chr.地图缓存6 > 0 && !"".equals(chr.地图缓存时间6)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间6).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存6).append("##k\r\n");
                    }
                    if (chr.地图缓存7 > 0 && !"".equals(chr.地图缓存时间7)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间7).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存7).append("##k\r\n");
                    }
                    if (chr.地图缓存8 > 0 && !"".equals(chr.地图缓存时间8)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间8).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存8).append("##k\r\n");
                    }
                    if (chr.地图缓存9 > 0 && !"".equals(chr.地图缓存时间9)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间9).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存9).append("##k\r\n");
                    }
                    if (chr.地图缓存10 > 0 && !"".equals(chr.地图缓存时间10)) {
                        name.append("\t\t#d进图时间:  #r").append(chr.地图缓存时间10).append("\r\n");
                        name.append("\t\t#d地图名称:  #r#m").append(chr.地图缓存10).append("##k\r\n");
                    }
                }
            }
        }
        return name.toString();
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

    public void 根据ID跟踪玩家(int cid) {
        if (c.getPlayer().getId() == cid) {
            return;
        }
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters()) {
                if (chr.getId() == cid) {
                    if (c.getPlayer().getClient().getChannel() != chr.getClient().getChannel()) {
                        c.getPlayer().dropMessage(6, "正在换频道,请等待。");
                        c.getPlayer().changeChannel(chr.getClient().getChannel());
                    } else if (c.getPlayer().getId() != chr.getMapId()) {
                        MapleMap mapp = c.getChannelServer().getMapFactory().getMap(chr.getMapId());
                        c.getPlayer().changeMap(mapp, mapp.getPortal(0));
                    }
                }
            }
        }
    }

    public void 根据ID封号玩家(int cid) {
        if (c.getPlayer().getId() == cid) {
            return;
        }
        int ch = World.Find.findChannel(角色ID取名字(cid));
        MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(cid));
        if (target.ban("被巡查封禁/" + c.getPlayer().getName() + "", c.getPlayer().isAdmin(), false, false)) {
            String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件/破坏游戏平衡，被系统永久封号。";
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
            sendMsgToQQGroup(信息);
        }
    }
}
