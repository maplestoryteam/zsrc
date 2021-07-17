/*
进入游戏
 */
package handling.channel.handler;


import static a.用法大全.角色ID取上线喇叭;
import static abc.Game.安全系统;
import client.*;
import database.DatabaseConnection;
import java.util.List;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import static handling.login.handler.CharLoginHandler.Gaincharacterz;
import static handling.login.handler.CharLoginHandler.Getcharacterz;
import handling.world.CharacterTransfer;
import handling.world.MapleMessenger;
import handling.world.MapleMessengerCharacter;
import handling.world.CharacterIdChannelPair;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.PlayerBuffStorage;
import handling.world.World;
import handling.world.guild.MapleGuild;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import static scripting.NPCConversationManager.账号取绑定QQ;
import scripting.NPCScriptManager;
import static gui.QQMsgServer.sendMsg;
import server.ServerProperties;
import server.maps.FieldLimitType;
import tools.FileoutputUtil;
import static tools.FileoutputUtil.CurrentReadable_Time;
import tools.MaplePacketCreator;
import tools.packet.FamilyPacket;
import tools.data.LittleEndianAccessor;

public class InterServerHandler {

    public static final void EnterCS(final MapleClient c, final MapleCharacter chr, final boolean mts) {
        try {
            //输出日志.世界频道登陆显示窗("进入商城？");
            //输出日志.show();
            if (c.getPlayer().getBuffedValue(MapleBuffStat.SUMMON) != null) {
                c.getPlayer().cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
            }
            c.getPlayer().saveToDB(false, false);
            String[] socket = c.getChannelServer().getIP().split(":");
            final ChannelServer ch = ChannelServer.getInstance(c.getChannel());
            chr.changeRemoval();
            if (chr.getMessenger() != null) {
                MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
                World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
            }
            PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
            PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
            PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
            World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), mts ? -20 : -10);//
            ch.removePlayer(chr);
            c.updateLoginState(MapleClient.CHANGE_CHANNEL, c.getSessionIPAddress());
            c.sendPacket(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(CashShopServer.getIP().split(":")[1])));
            //chr.saveToDB(false, false);
            chr.getMap().removePlayer(chr);
            c.getPlayer().expirationTask(true, false);
            c.setPlayer(null);
            c.setReceiving(false);
        } catch (UnknownHostException ex) {
            Logger.getLogger(InterServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static final void EnterMTS(final MapleClient c, final MapleCharacter chr, final boolean mts) {
//        if (!chr.isAlive() || chr.getEventInstance() != null || c.getChannelServer() == null) {
        String[] socket = c.getChannelServer().getIP().split(":");
        if (c.getPlayer().getTrade() != null) {
            c.getPlayer().dropMessage(1, "交易中无法进行其他操作！");
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        if (chr.isGM() == false || chr.isGM() == true) {
            //NPCScriptManager.getInstance().start(c, 9900004, 9900005);//拍卖npc
            c.sendPacket(MaplePacketCreator.enableActions());
            // c.sendPacket(MaplePacketCreator.serverBlocked(2));
            // c.sendPacket(MaplePacketCreator.enableActions());
            // return;
        } else {
            try {
                //if (c.getChannel() == 1 && !c.getPlayer().isGM()) {
                //    c.getPlayer().dropMessage(5, "你不能进入这个频道。请更改频道再试一次.");
                //    c.sendPacket(MaplePacketCreator.enableActions());
                //    return;
                //}
                final ChannelServer ch = ChannelServer.getInstance(c.getChannel());
                chr.changeRemoval();
                if (chr.getMessenger() != null) {
                    MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
                    World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
                }
                PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
                PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
                PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
                World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), mts ? -20 : -10);
                ch.removePlayer(chr);
                c.updateLoginState(MapleClient.CHANGE_CHANNEL, c.getSessionIPAddress());
                c.sendPacket(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(CashShopServer.getIP().split(":")[1])));
                //chr.saveToDB(false, false);
                chr.getMap().removePlayer(chr);
                c.setPlayer(null);
                c.setReceiving(false);
            } catch (UnknownHostException ex) {
                Logger.getLogger(InterServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //private static 世界频道登陆显示窗 输出日志 = new 世界频道登陆显示窗();
    public static void Loggedin(final int playerid, final MapleClient c) {//连接服务器
        if (c == null) {
            return;
        }
        final ChannelServer channelServer = c.getChannelServer();
        MapleCharacter player;
        final CharacterTransfer transfer = channelServer.getPlayerStorage().getPendingCharacter(playerid);
        if (transfer == null) { // Player isn't in storage, probably isn't CC
            player = MapleCharacter.loadCharFromDB(playerid, c, true);
        } else {
            player = MapleCharacter.ReconstructChr(transfer, c, true);
        }
        c.setPlayer(player);
        //登陆验证码
        if (gui.Start.ConfigValuesMap.get("登陆验证开关") <= 0) {
            //账号ID取账号(player.getAccountID()
            c.getPlayer().Gaincharacterz("" + 账号ID取账号(player.getAccountID()) + "", 100, 10);
            c.sendPacket(MaplePacketCreator.serverNotice(1, "" + 安全系统 + "请在 10 秒内对机器人发送[*abc]。\r\n稍等一下就会进入游戏。\r\n否则会被强制断开连接。"));
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 10);
                        if (c != null) {
                            if (c.getPlayer().Getcharacterz("" + 账号ID取账号(角色ID取账号ID(c.getPlayer().getId())) + "", 100) == 0) {
                                InterServerHandler.Loggedin2(playerid, c);
                            } else {
                                c.getPlayer().getClient().getSession().close();
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        } else {
            InterServerHandler.Loggedin2(playerid, c);
        }
    }

    public static void Loggedin2(final int playerid, final MapleClient c) {//连接服务器

        final ChannelServer channelServer = c.getChannelServer();
        MapleCharacter player;
        final CharacterTransfer transfer = channelServer.getPlayerStorage().getPendingCharacter(playerid);
        if (transfer == null) { // Player isn't in storage, probably isn't CC
            player = MapleCharacter.loadCharFromDB(playerid, c, true);
        } else {
            player = MapleCharacter.ReconstructChr(transfer, c, true);
        }
        c.setPlayer(player);
        c.setAccID(player.getAccountID());
        c.loadAccountData(player.getAccountID());
        ChannelServer.forceRemovePlayerByAccId(c, c.getAccID());
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        //这句为啥屏蔽掉？
        //c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
        channelServer.addPlayer(player);//显示顶上滚动公告
        c.sendPacket(MaplePacketCreator.getCharInfo(player));

        int 管理隐身 = gui.Start.ConfigValuesMap.get("管理隐身开关");
        if (管理隐身 <= 0) {//管理隐身
            if (player.isGM()) {
                SkillFactory.getSkill(9001004).getEffect(1).applyTo(player);
            }
        }
        int 管理加速 = gui.Start.ConfigValuesMap.get("管理加速开关");
        if (管理加速 <= 0) {//管理加速
            if (player.isGM()) {
                SkillFactory.getSkill(9001001).getEffect(1).applyTo(player);
            }
        }
        c.sendPacket(MaplePacketCreator.temporaryStats_Reset());
        player.getMap().addPlayer(player);
        //召唤宠物
        c.getPlayer().spawnSavedPets();
        try {
            player.silentGiveBuffs(PlayerBuffStorage.getBuffsFromStorage(player.getId()));
            player.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(player.getId()));
            player.giveSilentDebuff(PlayerBuffStorage.getDiseaseFromStorage(player.getId()));
            final Collection<Integer> buddyIds = player.getBuddylist().getBuddiesIds();
            World.Buddy.loggedOn(player.getName(), player.getId(), c.getChannel(), buddyIds, player.getGMLevel(), player.isHidden());
            if (player.getParty() != null) {
                //channelServer.getWorldInterface().updateParty(player.getParty().getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
                World.Party.updateParty(player.getParty().getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));
            }
            final CharacterIdChannelPair[] onlineBuddies = World.Find.multiBuddyFind(player.getId(), buddyIds);
            for (CharacterIdChannelPair onlineBuddy : onlineBuddies) {
                final BuddyEntry ble = player.getBuddylist().get(onlineBuddy.getCharacterId());
                ble.setChannel(onlineBuddy.getChannel());
                player.getBuddylist().put(ble);
            }
            c.sendPacket(MaplePacketCreator.updateBuddylist(player.getBuddylist().getBuddies()));
            final MapleMessenger messenger = player.getMessenger();
            if (messenger != null) {
                World.Messenger.silentJoinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()));
                World.Messenger.updateMessenger(messenger.getId(), c.getPlayer().getName(), c.getChannel());
            }

            if (player.getGuildId() > 0) {
                World.Guild.setGuildMemberOnline(player.getMGC(), true, c.getChannel());
                c.sendPacket(MaplePacketCreator.showGuildInfo(player));
                final MapleGuild gs = World.Guild.getGuild(player.getGuildId());
                if (gs != null) {
                    final List<byte[]> packetList = World.Alliance.getAllianceInfo(gs.getAllianceId(), true);
                    if (packetList != null) {
                        for (byte[] pack : packetList) {
                            if (pack != null) {
                                c.sendPacket(pack);
                            }
                        }
                    }
                    //c.sendPacket(MaplePacketCreator.getGuildAlliance(gs.packetList()));//家族
                }
            }
            if (player.getFamilyId() > 0) {
                World.Family.setFamilyMemberOnline(player.getMFC(), true, c.getChannel());
            }
            c.sendPacket(FamilyPacket.getFamilyInfo(player));
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.Login_Error, e);
        }
        c.sendPacket(FamilyPacket.getFamilyData());
        //加载技能宏
        player.sendMacros();
        //检测物品时间
        player.expirationTask();/////
        //显示小字条消息
        player.showNote();
        //更新组队HP
        player.updatePartyMemberHP();
        //开始计算角色精灵吊坠时间
        //player.startFairySchedule(false);
        player.updatePetEquip();
        //修复3转以上角色技能
        player.baseSkills(); //修复失去技能的人
        c.sendPacket(MaplePacketCreator.getKeymap(player.getKeyLayout()));

        for (MapleQuestStatus status : player.getStartedQuests()) {
            if (status.hasMobKills()) {
                c.sendPacket(MaplePacketCreator.updateQuestMobKills(status));
            }
        }

        final BuddyEntry pendingBuddyRequest = player.getBuddylist().pollPendingRequest();
        if (pendingBuddyRequest != null) {
            player.getBuddylist().put(new BuddyEntry(pendingBuddyRequest.getName(), pendingBuddyRequest.getCharacterId(), "ETC", -1, false, pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob()));
            c.sendPacket(MaplePacketCreator.requestBuddylistAdd(pendingBuddyRequest.getCharacterId(), pendingBuddyRequest.getName(), pendingBuddyRequest.getLevel(), pendingBuddyRequest.getJob()));
        }
        if (player.getJob() == 132) { // DARKKNIGHT
            player.checkBerserk();
        }
        //角色上线喇叭
        if (gui.Start.ConfigValuesMap.get("上线提醒开关") <= 0) {
            if (!player.isGM()) {
                String 玩家 = c.getPlayer().getName();
                String 内容 = 角色ID取上线喇叭(c.getPlayer().getId());
                if (!"".equals(内容)) {
                    World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(11, c.getChannel(), "[ " + 玩家 + " ] : " + 内容));
                }
            }
        }
        //召唤分身
        //player.spawnClones();
        // player.getHyPay(1);
        //player.spawnSavedPets();
        c.sendPacket(MaplePacketCreator.showCharCash(c.getPlayer()));
        if (player.getGMLevel() == 0) {
            String 输出 = "[服务端]" + CurrentReadable_Time() + " : 进入(" + c.getChannel() + ")频道，玩家: " + c.getPlayer().getName() + " ";
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:" + 输出));
            //输出日志.世界频道登陆显示窗(输出);
            System.err.println(输出);
        } else {
            String 输出 = "[服务端]" + CurrentReadable_Time() + " : 进入(" + c.getChannel() + ")频道，管理: " + c.getPlayer().getName() + " ";
            //输出日志.世界频道登陆显示窗(输出);
            System.err.println(输出);
        }

        /*if (gui.Start.ConfigValuesMap.get("上线特效开关") <= 0) {
            c.sendPacket(MaplePacketCreator.showEffect("nightmare/wakeup"));//裂屏特效
            c.sendPacket(MaplePacketCreator.playSound("Party1/bb"));//音乐特效
        }*/
        if (c.getPlayer().Getcharacterz("" + c.getPlayer().getId() + "", 2) <= 0) {
            sendMsg(" [角色登陆信息]:"
                    + "\r\n 账号: " + c.getAccountName() + " "
                    + "\r\n 角色: " + c.getPlayer().getName() + ""
                    + "\r\n 时间: " + CurrentReadable_Time() + " "
                    + "\r\n 地址: " + c.getSession().remoteAddress()+ ""
                    + "\r\n 区域: " + MapleParty.开服名字 + "",
                    "" + 账号取绑定QQ(c.getAccountName()) + "");

        }

        int 双爆频道 = gui.Start.ConfigValuesMap.get("双爆频道开关");
        if (双爆频道 == 0) {
            if (c.getChannel() == Integer.parseInt(ServerProperties.getProperty("ZEV.Count")) && !c.getPlayer().haveItem(5220002, 1, false, false)) {
                c.getPlayer().dropMessage(5, "对不起，你缺少特殊物品，无法进入 " + Integer.parseInt(ServerProperties.getProperty("ZEV.Count")) + " 频道！");
                c.getPlayer().changeChannel(1);
            }
        }
        c.sendPacket(MaplePacketCreator.weirdStatUpdate());

        if (gui.Start.ConfigValuesMap.get("登陆帮助开关") == 0) {
            if (player.getGMLevel() > 0 && player.getBossLog("管理上线提示") == 0) {
                player.dropMessage(5, "指令: [*指令大全] 查看管理员指令");
                player.dropMessage(5, "指令: [@帮助] 查看玩家指令 / 说[老子知道了233]，今日上线不再提醒");
            } else if (player.getGMLevel() <= 0 && player.getBossLog("玩家上线提示") == 0) {
                player.dropMessage(5, "指令: [@帮助] 查看玩家指令 / 说[我知道了233]，今日上线不再提醒");
            }
        }
        if (gui.Start.ConfigValuesMap.get("登陆保护开关") == 0) {
            final String login = 账号ID取账号(player.getAccountID());
            //增加登陆值
            Gaincharacterz("" + login + "", 101, 1);
            //30秒的登陆保护
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 30);
                        Gaincharacterz("" + login + "", 101, -Getcharacterz("" + login + "", 101));
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        if (gui.Start.ConfigValuesMap.get("登陆队列开关") == 0) {
            //进入游戏后从队列中退出
            final String login = 账号ID取账号(player.getAccountID());
            Gaincharacterz("" + login + "", 102, -Getcharacterz("" + login + "", 102));
        }
        player.刷新身上装备附魔汇总数据();
        NPCScriptManager.getInstance().dispose(c);
        //重返
        if (c.getChannel() == 2) {
            if (c.getPlayer().Getcharacterz("" + c.getPlayer().getId() + "", 500) > 0) {
                NPCScriptManager.getInstance().dispose(c);
                NPCScriptManager.getInstance().start(c, 2030010, 1);
            } else if (c.getPlayer().Getcharacterz("" + c.getPlayer().getId() + "", 501) > 0) {
                NPCScriptManager.getInstance().dispose(c);
                NPCScriptManager.getInstance().start(c, 2083004, 10);
                //PB重返
            } else if (c.getPlayer().Getcharacterz("" + c.getPlayer().getId() + "", 502) > 0) {
                NPCScriptManager.getInstance().dispose(c);
                NPCScriptManager.getInstance().start(c, 2141001, 10);
            }
        }
        //player.保护线程();
        if (gui.Start.ConfigValuesMap.get("幸运职业开关") == 0) {
            int 职业 = player.getJob();
            int 职业2 = MapleParty.幸运职业;
            if ((职业 == 职业2) || (职业 - 职业2 == 1) || (职业2 - 职业 == -1)) {
                player.dropMessage(5, "[幸运职业] : 恭喜你幸运成为幸运职业，增加50%基础狩猎经验");
            }
        }
        //player.dropMessage(5, "[精灵吊坠] : " + player.getFairyExp());
        player.dropMessage(5, "[疲劳值] : " + 60 * gui.Start.ConfigValuesMap.get("每日疲劳值") + " / " + (60 * gui.Start.ConfigValuesMap.get("每日疲劳值") - player.判断疲劳值()) + "");
        //player.养鱼线程();
        //加载套装效果(c);
        //清理掉这个不存在的物品
//        player.removeAll(2070018);
//        player.removeAll(4001197);
    }

//    public static void 加载套装效果(MapleClient c) {
//        Equip 武器 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).copy();
//        Equip 帽子 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1).copy();
//        Equip 套装 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -5).copy();
//        Equip 手套 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -8).copy();
//        Equip 鞋子 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -7).copy();
//        if (帽子.getItemId() == 1002776 && 套装.getItemId() == 1052155 && 手套.getItemId() == 1082234 && 鞋子.getItemId() == 1072355) {
//            if (战士武器(武器.getItemId())) {
//                c.getPlayer().dropMessage(5, "[战士]永恒斗志 输出 + 100%");
//                 layer.永恒套增加伤害=100;
//            }
//        }
//    }
    public static boolean 战士武器(int id) {
        switch (id) {
            //永恒破甲剑
            case 1302081:
            //永恒断蚺斧
            case 1312037:
            //永恒惊破天
            case 1322060:
            //永恒玄冥剑
            case 1402046:
            //永恒碎鼋斧
            case 1412033:
            //永恒威震天
            case 1422037:
            //永恒显圣枪
            case 1432047:
            //永恒神光戟
            case 1442063:
                return true;
        }
        return false;
    }

    public static final void ChangeChannel(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (c.getPlayer().getTrade() != null || !chr.isAlive() || chr.getEventInstance() != null || chr.getMap() == null || FieldLimitType.ChannelSwitch.check(chr.getMap().getFieldLimit())) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        //检测过期
        chr.expirationTask();
        chr.changeChannel(slea.readByte() + 1);
    }

    public static int 角色ID取账号ID(int id) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM characters ");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getInt("id") == id) {
                        data = rs.getInt("accountid");
                    }
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("[队列提醒]；角色名字取账号ID、出错");
        }
        return data;
    }

    public static String 账号ID取账号(int id) {
        String data = "";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = rs.getString("name");
                }
            }
            ps.close();
        } catch (SQLException Ex) {
            System.err.println("[队列提醒]；账号ID取账号、出错");
        }
        return data;
    }

}
