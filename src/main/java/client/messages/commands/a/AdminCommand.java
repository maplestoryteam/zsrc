package client.messages.commands.a;

import client.*;
import client.inventory.*;
import client.messages.CommandProcessorUtil;
import client.messages.commands.CommandExecute;
import com.mysql.jdbc.PreparedStatement;
import constants.GameConstants;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.login.handler.AutoRegister;
import handling.world.CheaterData;
import handling.world.MapleParty;
import handling.world.World;
import handling.world.family.MapleFamily;
import handling.world.guild.MapleGuild;
import scripting.EventManager;
import scripting.NPCScriptManager;
import scripting.PortalScriptManager;
import scripting.ReactorScriptManager;
import server.*;
import server.Timer.EventTimer;
import server.events.MapleEvent;
import server.events.MapleEventType;
import server.events.MapleOxQuizFactory;
import server.life.*;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleReactor;
import server.quest.MapleQuest;
import tools.*;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.MobPacket;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;

import static tools.FileoutputUtil.CurrentReadable_Time;

/**
 *
 * @author 游戏管理员
 *
 *
 */
public class AdminCommand {

    /*public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.ADMIN;
    }*/
 /* public static class 重载传送 extends ReloadPortals {
    }
    public static class 重载商店 extends ReloadShops {
    }
    public static class 重载爆物 extends ReloadDrops {
    }*/
    // public static class 开放地图 extends openmap {
    //  }
    // public static class 关闭地图 extends closemap {
    // }
    //  public static class 传所有人到地图 extends WarpPlayersTo {
    // }
    /* public static class 修改当前频道物品爆率 extends DropRate {
    }
    
    public static class 修改当前频道经验倍率 extends ExpRate {
    }
    
    public static class 修改当前频道金币倍率 extends MesoRate {
    }

    public static class 注册 extends register {
    }

    public static class 满属性 extends maxstats {
    }

    public static class 满技能 extends maxSkills {
    }

    public static class 拉全部人到我这里 extends WarpAllHere {
    }

    public static class 给所有人金币 extends mesoEveryone {
    }

    public static class 给所有人经验 extends ExpEveryone {
    }

    public static class 给所有人点券 extends CashEveryone {
    }*/
    // public static class 给点券 extends GainCash {
    // }
    // public static class 给经验 extends GainExp {
    // }
    //  public static class 给当前地图所有人 extends GainMap {
    //  }
    // public static class 祝福 extends buff {
    // }
    //  public static class 倍率设置 extends setRate {
    //   }
    // public static class 地图代码 extends WhereAmI {
    // }
    // public static class 刷 extends Item {
    // }
    // public static class 丢 extends Drop {
    // }
    // public static class 全部复活 extends HealMap {
    // }
    //  public static class 清怪 extends KillAll {
    //  }
    //  public static class 人气 extends Fame {
    //  }
    // public static class 吸怪 extends MobVac {
    // }
    //  public static class 清除地板 extends cleardrops {
    //  }
    //  public static class 召唤怪物 extends Spawn {
    //  }
    //  public static class 计时器 extends Clock {
    // }
    // public static class 自动注册 extends autoreg {
    // }
    // public static class 怪物代码 extends mob {
    //  }
    // public static class 封号状态 extends BanStatus {
    //  }
    public static class Debug extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setDebugMessage(!c.getPlayer().getDebugMessage());
            return 1;
        }
    }

    public static class 封号状态 extends CommandExecute {//BanStatus

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            String name = splitted[1];
            String mac = "";
            String ip = "";
            int acid = 0;
            boolean Systemban = false;
            boolean ACbanned = false;
            boolean IPbanned = false;
            boolean MACbanned = false;
            String reason = null;
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps;
                ps = (PreparedStatement) con.prepareStatement("select accountid from characters where name = ?");
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        acid = rs.getInt("accountid");
                    }
                }
                ps = (PreparedStatement) con.prepareStatement("select banned, banreason, macs, Sessionip from accounts where id = ?");
                ps.setInt(1, acid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Systemban = rs.getInt("banned") == 2;
                        ACbanned = rs.getInt("banned") == 1 || rs.getInt("banned") == 2;
                        reason = rs.getString("banreason");
                        mac = rs.getString("macs");
                        ip = rs.getString("Sessionip");
                    }
                }
                ps.close();
            } catch (Exception e) {
            }
            if (reason == null || reason == "") {
                reason = "?";
            }
            if (c.isBannedIP(ip)) {
                IPbanned = true;
            }
            if (c.isBannedMac(mac)) {
                MACbanned = true;
            }
            c.getPlayer().dropMessage("玩家[" + name + "] 帐号ID[" + acid + "]是否被封锁: " + (ACbanned ? "是" : "否") + (Systemban ? "(系统自动封锁)" : "") + ", 原因: " + reason);
            c.getPlayer().dropMessage("IP: " + ip + " 是否在封锁IP名单: " + (IPbanned ? "是" : "否"));
            c.getPlayer().dropMessage("MAC: " + mac + " 是否在封锁MAC名单: " + (MACbanned ? "是" : "否"));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!封号状态 <角色名> - ").toString();
        }
    }

    public static class 服务器人数上限 extends CommandExecute {//////setUserLimit

        public int execute(MapleClient c, String[] splitted) {
            int UserLimit = LoginServer.getUserLimit();
            try {
                UserLimit = Integer.parseInt(splitted[1]);
            } catch (Exception ex) {
            }
            LoginServer.setUserLimit(UserLimit);
            c.getPlayer().dropMessage("服务器人数上限已更改为" + UserLimit);
            return 1;
        }
    }

    public static class 保存雇佣商人 extends CommandExecute {///////SavePlayerShops

        public int execute(MapleClient c, String[] splitted) {
            for (handling.channel.ChannelServer cserv : handling.channel.ChannelServer.getAllInstances()) {
                cserv.closeAllMerchant();
            }
            c.getPlayer().dropMessage(6, "雇佣商人储存完毕.");

            return 1;
        }
    }

    public static class 关闭服务端 extends CommandExecute {//////Shutdown

        private static Thread t = null;

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "关闭服务器...");
            if (t == null || !t.isAlive()) {
                t = new Thread(server.ShutdownServer.getInstance());
                t.start();
            } else {
                c.getPlayer().dropMessage(6, "已在执行中...");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!关闭服务端 - 关闭服务器").toString();
        }
    }

    public static class 倒计时关闭服务端 extends CommandExecute {/////shutdowntime

        private static ScheduledFuture<?> ts = null;
        private int minutesLeft = 0;
        private static Thread t = null;

        public int execute(MapleClient c, String[] splitted) {

            if (splitted.length < 2) {
                return 0;
            }
            minutesLeft = Integer.parseInt(splitted[1]);
            c.getPlayer().dropMessage(6, "服务器将在 " + minutesLeft + "分钟后关闭. 请尽速关闭精灵商人并下线,造成任何损失概不负责");
            if (ts == null && (t == null || !t.isAlive())) {
                t = new Thread(ShutdownServer.getInstance());
                ts = EventTimer.getInstance().register(new Runnable() {

                    public void run() {
                        if (minutesLeft == 0) {
                            ShutdownServer.getInstance().run();
                            t.start();
                            ts.cancel(false);
                            return;
                        }
                        StringBuilder message = new StringBuilder();
                        message.append("[冒险岛公告] 服务器将在 ");
                        message.append(minutesLeft);
                        message.append("分钟后关闭，请各位玩家收起你的雇佣商人并下线.");
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, message.toString()));
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverMessage(message.toString()));
                        for (ChannelServer cs : ChannelServer.getAllInstances()) {
                            cs.setServerMessage("服务器将于 " + minutesLeft + " 分钟后开启");
                        }
                        minutesLeft--;
                    }
                }, 60000);
            } else {
                c.getPlayer().dropMessage(6, new StringBuilder().append("服务器关闭时间修改为 ").append(minutesLeft).append("分钟后，请稍等服务器关闭").toString());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!倒计时关闭服务端 <秒数> - 关闭服务器").toString();
        }
    }

    public static class 保存所有角色信息 extends CommandExecute {/////////////SaveAll

        private int p = 0;

        public int execute(MapleClient c, String[] splitted) {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                List<MapleCharacter> chrs = cserv.getPlayerStorage().getAllCharactersThreadSafe();
                for (MapleCharacter chr : chrs) {
                    p++;
                    chr.saveToDB(false, false);
                }
            }
            c.getPlayer().dropMessage("[保存] " + p + "个玩家数据保存到数据中.");
            p = 0;
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!保存所有角色信息 - 保存所有角色資料").toString();
        }
    }

    public static class 回血回蓝 extends CommandExecute {/////////LowHP

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getStat().setHp((short) 1);
            c.getPlayer().getStat().setMp((short) 1);
            c.getPlayer().updateSingleStat(MapleStat.HP, 1);
            c.getPlayer().updateSingleStat(MapleStat.MP, 1);
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!回血回蓝 - 回血回蓝ㄧ").toString();
        }
    }

    public static class 恢复状态 extends CommandExecute {/////////Heal

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getStat().setHp(c.getPlayer().getStat().getCurrentMaxHp());
            c.getPlayer().getStat().setMp(c.getPlayer().getStat().getCurrentMaxMp());
            c.getPlayer().updateSingleStat(MapleStat.HP, c.getPlayer().getStat().getCurrentMaxHp());
            c.getPlayer().updateSingleStat(MapleStat.MP, c.getPlayer().getStat().getCurrentMaxMp());
            c.getPlayer().dispelDebuffs();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!恢复 - 满血满蓝").toString();
        }
    }

    public static class 解锁IP extends CommandExecute {//////UnbanIP

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            byte ret_ = MapleClient.unbanIPMacs(splitted[1]);
            if (ret_ == -2) {
                c.getPlayer().dropMessage(6, "[unbanip] SQL 错误.");
            } else if (ret_ == -1) {
                c.getPlayer().dropMessage(6, "[unbanip] 角色不存在.");
            } else if (ret_ == 0) {
                c.getPlayer().dropMessage(6, "[unbanip] No IP or Mac with that character exists!");
            } else if (ret_ == 1) {
                c.getPlayer().dropMessage(6, "[unbanip] IP或Mac已解锁其中一個.");
            } else if (ret_ == 2) {
                c.getPlayer().dropMessage(6, "[unbanip] IP以及Mac已成功解锁.");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!解锁IP <玩家名称> - 解锁玩家").toString();
        }
    }

    public static class 封锁玩家 extends CommandExecute {///////TempBan

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim;
            String name = splitted[1];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                return 0;
            }
            victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            final int reason = Integer.parseInt(splitted[2]);
            final int numDay = Integer.parseInt(splitted[3]);

            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, numDay);
            final DateFormat df = DateFormat.getInstance();

            if (victim == null) {
                c.getPlayer().dropMessage(6, "[tempban] 找不到目标角色");

            } else {
                victim.tempban("由" + c.getPlayer().getName() + "暂时锁定了", cal, reason, true);
                c.getPlayer().dropMessage(6, "[tempban] " + splitted[1] + " 已成功被暂时锁定至 " + df.format(cal.getTime()));
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!封锁玩家 <玩家名称> - 暂时锁定玩家").toString();
        }
    }

    public static class 杀玩家 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (splitted.length < 2) {
                return 0;
            }
            MapleCharacter victim;
            for (int i = 1; i < splitted.length; i++) {
                String name = splitted[1];
                int ch = World.Find.findChannel(name);
                if (ch <= 0) {
                    return 0;
                }
                victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
                if (victim == null) {
                    c.getPlayer().dropMessage(6, "[kill] 玩家 " + splitted[i] + " 不存在.");
                } else if (player.allowedToTarget(victim)) {
                    victim.getStat().setHp((short) 0);
                    victim.getStat().setMp((short) 0);
                    victim.updateSingleStat(MapleStat.HP, 0);
                    victim.updateSingleStat(MapleStat.MP, 0);
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!杀玩家 <玩家名称1> <玩家名称2> ...  - 杀掉玩家").toString();
        }
    }

    public static class 自学技能 extends CommandExecute {////////Skill

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            ISkill skill = SkillFactory.getSkill(Integer.parseInt(splitted[1]));
            byte level = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
            byte masterlevel = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1);
            if (level > skill.getMaxLevel()) {
                level = skill.getMaxLevel();
            }
            c.getPlayer().changeSkillLevel(skill, level, masterlevel);
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!自学技能 <技能ID> [技能等級] [技能最大等級] ...  - 学习技能").toString();
        }
    }

    public static class 人气设置 extends CommandExecute {////////////Fame

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(("人气设置 <角色名称> <名声> ...  - 名声"));
                return 0;
            }
            MapleCharacter victim;
            String name = splitted[1];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                return 0;
            }
            victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

            short fame;
            try {
                fame = Short.parseShort(splitted[2]);
            } catch (NumberFormatException nfe) {
                c.getPlayer().dropMessage(6, "不合法的数字");
                return 0;
            }
            if (victim != null && player.allowedToTarget(victim)) {
                victim.addFame(fame);
                victim.updateSingleStat(MapleStat.FAME, victim.getFame());
            } else {
                c.getPlayer().dropMessage(6, "[fame] 角色不存在");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!人气设置 <角色名称> <名声> ...  - 名声").toString();
        }
    }

    public static class 自动注册 extends CommandExecute {////autoreg

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage("目前自动注册已经 " + ServerConstants.ChangeAutoReg());
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!autoreg  - 自动注册开关").toString();
        }
    }

    public static class 群体治愈 extends CommandExecute {//HealMap

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                if (mch != null) {
                    mch.getStat().setHp(mch.getStat().getMaxHp());
                    mch.updateSingleStat(MapleStat.HP, mch.getStat().getMaxHp());
                    mch.getStat().setMp(mch.getStat().getMaxMp());
                    mch.updateSingleStat(MapleStat.MP, mch.getStat().getMaxMp());
                    mch.dispelDebuffs();
                }
            }
            return 1;

        }

        public String getMessage() {
            return new StringBuilder().append("!群体治愈  - 治愈地图上所有的人").toString();
        }
    }

    public static class 无敌 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (player.isInvincible()) {
                player.setInvincible(false);
                player.dropMessage(6, "无敌已经关闭");
            } else {
                player.setInvincible(true);
                player.dropMessage(6, "无敌已经开启.");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!无敌  - 无敌开关").toString();
        }
    }

    public static class 给予技能 extends CommandExecute {//GiveSkill

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                return 0;
            }
            MapleCharacter victim;
            String name = splitted[1];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                return 0;
            }
            victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

            ISkill skill = SkillFactory.getSkill(Integer.parseInt(splitted[2]));
            byte level = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1);
            byte masterlevel = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 4, 1);

            if (level > skill.getMaxLevel()) {
                level = skill.getMaxLevel();
            }
            victim.changeSkillLevel(skill, level, masterlevel);
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!给予技能 <玩家名称> <技能ID> [技能等級] [技能最大等級] - 给予技能").toString();
        }
    }

    public static class 刷技能点 extends CommandExecute {//SP

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setRemainingSp(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 1));
            c.sendPacket(MaplePacketCreator.updateSp(c.getPlayer(), false));
            String msg = "[管理员信息][" + c.getPlayer().getName() + "] 使用指令增加SP " + splitted + "点";
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!刷技能点 [数量] - 增加SP").toString();
        }
    }

    public static class 刷能力点 extends CommandExecute {//AP

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setRemainingAp((short) CommandProcessorUtil.getOptionalIntArg(splitted, 1, 1));
            c.getPlayer().updateSingleStat(MapleStat.AVAILABLEAP, CommandProcessorUtil.getOptionalIntArg(splitted, 1, 1));
            String msg = "[管理员信息][" + c.getPlayer().getName() + "] 使用指令增加AP " + splitted + "点";
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!刷能力点 [数量] - 增加AP").toString();
        }
    }

    public static class 打开商店 extends CommandExecute {//Shop

        public int execute(MapleClient c, String[] splitted) {
            MapleShopFactory shop = MapleShopFactory.getInstance();
            int shopId;
            try {
                shopId = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException ex) {
                return 0;
            }
            if (shop.getShop(shopId) != null) {
                shop.getShop(shopId).sendShop(c);
            } else {
                c.getPlayer().dropMessage(5, "此商店ID不存在");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!打开商店 - 开启商店").toString();
        }
    }

    public static class 关键时刻 extends CommandExecute {

        protected static ScheduledFuture<?> ts = null;

        public int execute(final MapleClient c, String[] splitted) {
            if (splitted.length < 1) {
                return 0;
            }
            if (ts != null) {
                ts.cancel(false);
                c.getPlayer().dropMessage(0, "原定的关键时刻已取消");
            }
            int minutesLeft;
            try {
                minutesLeft = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException ex) {
                return 0;
            }
            if (minutesLeft > 0) {
                ts = EventTimer.getInstance().schedule(new Runnable() {

                    public void run() {
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                                if (!c.getPlayer().isGM()) {
                                    NPCScriptManager.getInstance().start(mch.getClient(), 9010010);
                                }
                            }
                        }
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "关键时刻已经开始了!!!"));
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverMessage("关键时刻已经开始了!!!"));
                        ts.cancel(false);
                        ts = null;
                    }
                }, minutesLeft * 60 * 1000); // 六十秒
                c.getPlayer().dropMessage(0, "关键时刻预定已完成");
            } else {
                c.getPlayer().dropMessage(0, "设定的时间必须 > 0。");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!关键时刻 <时间:分钟> - 关键时刻").toString();
        }
    }

    public static class 给点券 extends CommandExecute {//GainCash

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                return 0;
            }
            MapleCharacter player = c.getPlayer();
            int amount = 0;
            String name = "";
            try {
                amount = Integer.parseInt(splitted[1]);
                name = splitted[2];
            } catch (Exception ex) {
                c.getPlayer().dropMessage("该玩家不在线");
                return 1;
            }
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage("该玩家不在线");
                return 1;
            }
            player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            if (player == null) {
                c.getPlayer().dropMessage("该玩家不在线");
                return 1;
            }
            player.modifyCSPoints(1, amount, true);
            player.dropMessage("[密信]管理员赠予你 " + amount + " 点券");
            String msg = "[管理员信息][" + c.getPlayer().getName() + "] 給了 [" + player.getName() + "] [" + amount + "] 点卷";
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
            FileoutputUtil.logToFile("服务端记录信息/管理员给予信息/给予点卷.txt", "\r\n " + FileoutputUtil.NowTime() + " GM " + c.getPlayer().getName() + " 给了 " + player.getName() + " 点卷 " + amount + "点");
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!给点券 <数量> <玩家>").toString();
        }
    }

    public static class 给经验 extends CommandExecute {//GainExp

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                return 0;
            }
            MapleCharacter player = c.getPlayer();
            int amount = 0;
            String name = "";
            try {
                amount = Integer.parseInt(splitted[1]);
                name = splitted[2];
            } catch (Exception ex) {
                c.getPlayer().dropMessage("该玩家不在线");
                return 1;
            }
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage("该玩家不在线");
                return 1;
            }
            player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            if (player == null) {
                c.getPlayer().dropMessage("该玩家不在线");
                return 1;
            }
            player.gainExp(amount, true, true, false);
            player.dropMessage("[密信]管理员赠予你 " + amount + " 经验");
            //String msg = "[管理员信息] GM " + c.getPlayer().getName() + " 給了 " + player.getName() + " 经验 " + amount + "点";
            String msg = "[管理员信息][" + c.getPlayer().getName() + "] 給了 [" + player.getName() + "] [" + amount + "] 经验";
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
            FileoutputUtil.logToFile("服务端记录信息/管理员给予信息/给予经验.txt", "\r\n " + FileoutputUtil.NowTime() + " GM " + c.getPlayer().getName() + " 给了 " + player.getName() + " 经验 " + amount + "点");
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!给经验 <数量> <玩家>").toString();
        }
    }

    public static class 给当前地图所有人 extends CommandExecute {//GainMap

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 1) {
                return 0;
            }
            Iterator i;
            MapleCharacter player = c.getPlayer();
            LinkedHashSet cmc = new LinkedHashSet(c.getPlayer().getMap().getCharacters());
            String type = splitted[1];
            int amount;
            if (type.equalsIgnoreCase("金钱")) {
                amount = Integer.parseInt(splitted[2]);

                for (i = cmc.iterator(); i.hasNext();) {
                    player = (MapleCharacter) i.next();
                    player.gainMeso(amount, true, true, true);
                }
            } else if (type.equalsIgnoreCase("经验")) {
                amount = Integer.parseInt(splitted[2]);
                for (i = cmc.iterator(); i.hasNext();) {
                    player = (MapleCharacter) i.next();
                    player.gainExp(amount, true, true, false);
                }
            } else if (type.equalsIgnoreCase("点券")) {
                amount = Integer.parseInt(splitted[2]);
                for (i = cmc.iterator(); i.hasNext();) {
                    player = (MapleCharacter) i.next();
                    player.modifyCSPoints(1, amount, true);
                    player.dropMessage("已经收到点卷" + amount + "点");
                    String msg = "[管理员信息] GM " + c.getPlayer().getName() + " 給了 " + player.getName() + " 点卷 " + amount + "点";
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
                    FileoutputUtil.logToFile("服务端记录信息/管理员给予信息/给予点卷.txt", "\r\n " + FileoutputUtil.NowTime() + " GM " + c.getPlayer().getName() + " 给了 " + player.getName() + " 点卷 " + amount + "点");
                }
            } else if (type.equalsIgnoreCase("抵用券")) {
                amount = Integer.parseInt(splitted[2]);
                for (i = cmc.iterator(); i.hasNext();) {
                    player = (MapleCharacter) i.next();
                    player.modifyCSPoints(2, amount, true);
                    player.dropMessage("已经收到抵用券" + amount + "点");
                    String msg = "[管理员信息] GM " + c.getPlayer().getName() + " 給了 " + player.getName() + " 点卷 " + amount + "点";
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
                    FileoutputUtil.logToFile("服务端记录信息/管理员给予信息/给予点卷.txt", "\r\n " + FileoutputUtil.NowTime() + " GM " + c.getPlayer().getName() + " 给了 " + player.getName() + " 点卷 " + amount + "点");
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*给当前地图所有人 <金钱/经验/点券/抵用券> <数量> - 例如：*给当前地图所有人 金钱 5000 ").toString();
        }
    }

    /* public static class GainMaplePoint extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 3) {
                return 0;
            }
            MapleCharacter player;
            int amount = Integer.parseInt(splitted[1]);
            String name = splitted[2];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                return 0;
            }
            player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            if (player == null) {
                return 0;
            }
            player.modifyCSPoints(2, amount, true);
            String msg = "[管理员信息] GM " + c.getPlayer().getName() + " 给了 " + player.getName() + " 枫叶点数 " + amount + "点";
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!gainmaplepoint <數量> <玩家> - 取得枫叶点数").toString();
        }
    }

    public static class GainPoint extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 3) {
                return 0;
            }
            MapleCharacter player;
            int amount = Integer.parseInt(splitted[1]);
            String name = splitted[2];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                return 0;
            }
            player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            if (player == null) {
                return 0;
            }
            player.setPoints(player.getPoints() + amount);
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!gainpoint <數量> <玩家> - 取得Point").toString();
        }
    }

    public static class GainVP extends GainPoint {
    }*/
    public static class 升级 extends CommandExecute {//LevelUp

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().levelUp();
            } else {
                int up = 0;
                try {
                    up = Integer.parseInt(splitted[1]);
                } catch (Exception ex) {
                }
                for (int i = 0; i < up; i++) {
                    c.getPlayer().levelUp();
                }
            }
            c.getPlayer().setExp(0);
            c.getPlayer().updateSingleStat(MapleStat.EXP, 0);
//            if (c.getPlayer().getLevel() < 200) {
//                c.getPlayer().gainExp(GameConstants.getExpNeededForLevel(c.getPlayer().getLevel()) + 1, true, false, true);
//            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!升级 - 等級上升").toString();
        }
    }

    public static class 解锁道具 extends CommandExecute {//UnlockInv

        public int execute(MapleClient c, String[] splitted) {
            java.util.Map<IItem, MapleInventoryType> eqs = new ArrayMap<>();
            boolean add = false;
            if (splitted.length < 2 || splitted[1].equals("全部")) {
                for (MapleInventoryType type : MapleInventoryType.values()) {
                    for (IItem item : c.getPlayer().getInventory(type)) {
                        if (ItemFlag.LOCK.check(item.getFlag())) {
                            item.setFlag((byte) (item.getFlag() - ItemFlag.LOCK.getValue()));
                            add = true;
                            c.getPlayer().reloadC();
                            c.getPlayer().dropMessage(5, "已经解锁");
                            //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                        }
                        if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
                            item.setFlag((byte) (item.getFlag() - ItemFlag.UNTRADEABLE.getValue()));
                            add = true;
                            c.getPlayer().reloadC();
                            c.getPlayer().dropMessage(5, "已经解锁");
                            //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                        }
                        if (add) {
                            eqs.put(item, type);
                        }
                        add = false;
                    }
                }
            } else if (splitted[1].equals("已装备道具")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.LOCK.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.UNTRADEABLE.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已經解鎖");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (add) {
                        eqs.put(item, MapleInventoryType.EQUIP);
                    }
                    add = false;
                }
            } else if (splitted[1].equals("武器")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.EQUIP)) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.LOCK.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.UNTRADEABLE.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (add) {
                        eqs.put(item, MapleInventoryType.EQUIP);
                    }
                    add = false;
                }
            } else if (splitted[1].equals("消耗")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.USE)) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.LOCK.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.UNTRADEABLE.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (add) {
                        eqs.put(item, MapleInventoryType.USE);
                    }
                    add = false;
                }
            } else if (splitted[1].equals("装饰")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.SETUP)) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.LOCK.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.UNTRADEABLE.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (add) {
                        eqs.put(item, MapleInventoryType.SETUP);
                    }
                    add = false;
                }
            } else if (splitted[1].equals("其他")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.LOCK.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.UNTRADEABLE.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (add) {
                        eqs.put(item, MapleInventoryType.ETC);
                    }
                    add = false;
                }
            } else if (splitted[1].equals("特殊")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.CASH)) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.LOCK.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
                        item.setFlag((byte) (item.getFlag() - ItemFlag.UNTRADEABLE.getValue()));
                        add = true;
                        c.getPlayer().reloadC();
                        c.getPlayer().dropMessage(5, "已经解锁");
                        //c.sendPacket(MaplePacketCreator.updateSpecialItemUse(item, type.getType()));
                    }
                    if (add) {
                        eqs.put(item, MapleInventoryType.CASH);
                    }
                    add = false;
                }
            } else {
                return 0;
            }

            for (Entry<IItem, MapleInventoryType> eq : eqs.entrySet()) {
                c.getPlayer().forceReAddItem_NoUpdate(eq.getKey().copy(), eq.getValue());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!解锁道具 <全部/已装备道具/武器/消耗/装饰/其他/特殊> - 解锁道具").toString();
        }
    }

    public static class 刷 extends CommandExecute {//Item

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            int itemId = 0;
            try {
                itemId = Integer.parseInt(splitted[1]);
            } catch (Exception ex) {
            }
            short quantity = (short) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);

            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (GameConstants.isPet(itemId)) {
                MaplePet pet = MaplePet.createPet(itemId, MapleInventoryIdentifier.getInstance());
                if (pet != null) {
                    MapleInventoryManipulator.addById(c, itemId, (short) 1, c.getPlayer().getName(), pet, 90, (byte) 0);

                }
            } else if (!ii.itemExists(itemId)) {
                c.getPlayer().dropMessage(5, itemId + " - 物品不存在");
            } else {
                IItem item;
                byte flag = 0;
                flag |= ItemFlag.LOCK.getValue();

                if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    item = ii.randomizeStats((Equip) ii.getEquipById(itemId));
                    //    item.setFlag(flag);
                } else {
                    item = new client.inventory.Item(itemId, (byte) 0, quantity, (byte) 0);
                    if (GameConstants.getInventoryType(itemId) != MapleInventoryType.USE) {
                        //     item.setFlag(flag);
                    }
                }
                item.setOwner(c.getPlayer().getName());
                item.setGMLog(c.getPlayer().getName());

                MapleInventoryManipulator.addbyItem(c, item);

            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!刷 <道具ID> - 刷出道具").toString();
        }
    }

    public static class 更改滚动公告 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length > 1) {
                StringBuilder sb = new StringBuilder();
                sb.append(StringUtil.joinStringFrom(splitted, 1));
                for (ChannelServer ch : ChannelServer.getAllInstances()) {
                    ch.setServerMessage(sb.toString());
                }
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverMessage(sb.toString()));
            } else {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!更改滚动公告 讯息 - 更改上方黃色公告").toString();
        }
    }

    public static class 公告 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length > 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                sb.append(c.getPlayer().getName());
                sb.append("] ");
                sb.append(StringUtil.joinStringFrom(splitted, 1));
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, sb.toString()));
            } else {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!公告 讯息 - 服务器公告").toString();
        }
    }

    public static class 向全服推送信息 extends CommandExecute {///////////Letter

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, "指令规则: ");
                return 0;
            }
            int start, nstart;
            if (splitted[1].equalsIgnoreCase("green")) {
                start = 3991026;
                nstart = 3990019;
            } else if (splitted[1].equalsIgnoreCase("red")) {
                start = 3991000;
                nstart = 3990009;
            } else {
                c.getPlayer().dropMessage(6, "未知的顏色!");
                return 1;
            }
            String splitString = StringUtil.joinStringFrom(splitted, 2);
            List<Integer> chars = new ArrayList<>();
            splitString = splitString.toUpperCase();
            // System.out.println(splitString);
            for (int i = 0; i < splitString.length(); i++) {
                char chr = splitString.charAt(i);
                if (chr == ' ') {
                    chars.add(-1);
                } else if ((int) (chr) >= (int) 'A' && (int) (chr) <= (int) 'Z') {
                    chars.add((int) (chr));
                } else if ((int) (chr) >= (int) '0' && (int) (chr) <= (int) ('9')) {
                    chars.add((int) (chr) + 200);
                }
            }
            final int w = 32;
            int dStart = c.getPlayer().getPosition().x - (splitString.length() / 2 * w);
            for (Integer i : chars) {
                if (i == -1) {
                    dStart += w;
                } else if (i < 200) {
                    int val = start + i - (int) ('A');
                    client.inventory.Item item = new client.inventory.Item(val, (byte) 0, (short) 1);
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), item, new Point(dStart, c.getPlayer().getPosition().y), false, false);
                    dStart += w;
                } else if (i >= 200 && i <= 300) {
                    int val = nstart + i - (int) ('0') - 200;
                    client.inventory.Item item = new client.inventory.Item(val, (byte) 0, (short) 1);
                    c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), item, new Point(dStart, c.getPlayer().getPosition().y), false, false);
                    dStart += w;
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append(" !letter <color (green/red)> <word> - 送信").toString();
        }
    }

    public static class 结婚 extends CommandExecute {/////Marry

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                return 0;
            }
            int itemId = Integer.parseInt(splitted[2]);
            if (!GameConstants.isEffectRing(itemId)) {
                c.getPlayer().dropMessage(6, "错误的戒指ID.");
            } else {
                MapleCharacter fff;
                String name = splitted[1];
                int ch = World.Find.findChannel(name);
                if (ch <= 0) {
                    c.getPlayer().dropMessage(6, "玩家必须在线");
                    return 0;
                }
                fff = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
                if (fff == null) {
                    c.getPlayer().dropMessage(6, "玩家必须在线");
                } else {
                    int[] ringID = {MapleInventoryIdentifier.getInstance(), MapleInventoryIdentifier.getInstance()};
                    try {
                        MapleCharacter[] chrz = {fff, c.getPlayer()};
                        for (int i = 0; i < chrz.length; i++) {
                            Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemId);
                            if (eq == null) {
                                c.getPlayer().dropMessage(6, "错误的戒指ID.");
                                return 1;
                            } else {
                                eq.setUniqueId(ringID[i]);
                                MapleInventoryManipulator.addbyItem(chrz[i].getClient(), eq.copy());
                                chrz[i].dropMessage(6, "成功与  " + chrz[i == 0 ? 1 : 0].getName() + " 结婚");
                            }
                        }
                        MapleRing.addToDB(itemId, c.getPlayer(), fff.getName(), fff.getId(), ringID);
                    } catch (SQLException e) {
                    }
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!结婚 <玩家名称> <戒指代码> - 结婚").toString();
        }
    }

    public static class 检测物品 extends CommandExecute {///ItemCheck

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3 || splitted[1] == null || splitted[1].equals("") || splitted[2] == null || splitted[2].equals("")) {
                return 0;
            } else {
                int item = Integer.parseInt(splitted[2]);
                MapleCharacter chr;
                String name = splitted[1];
                int ch = World.Find.findChannel(name);
                if (ch <= 0) {
                    c.getPlayer().dropMessage(6, "玩家必须在线");
                    return 0;
                }
                chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

                int itemamount = chr.getItemQuantity(item, true);
                if (itemamount > 0) {
                    c.getPlayer().dropMessage(6, chr.getName() + " 有 " + itemamount + " (" + item + ").");
                } else {
                    c.getPlayer().dropMessage(6, chr.getName() + " 并沒有 (" + item + ")");
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!检测物品 <玩家名字> <物品ID> - 检查物品").toString();
        }
    }

    public static class 吸怪 extends CommandExecute {////////////MobVac

        public int execute(MapleClient c, String[] splitted) {
            for (final MapleMapObject mmo : c.getPlayer().getMap().getAllMonstersThreadsafe()) {
                final MapleMonster monster = (MapleMonster) mmo;
                c.getPlayer().getMap().broadcastMessage(MobPacket.moveMonster(false, -1, 0, 0, 0, 0, monster.getObjectId(), monster.getPosition(), c.getPlayer().getPosition(), c.getPlayer().getLastRes()));
                monster.setPosition(c.getPlayer().getPosition());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!吸怪 - 全图吸怪").toString();
        }
    }

    public static class 音乐 extends CommandExecute {/////Song

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.musicChange(splitted[1]));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!音乐 - 播放音乐").toString();
        }
    }

    public static class 开启自动活动 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            final EventManager em = c.getChannelServer().getEventSM().getEventManager("AutomatedEvent");
            if (em != null) {
                em.scheduleRandomEvent();
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!开启自动活动 - 开启自动活动").toString();
        }
    }

    public static class 活动开始 extends CommandExecute {

        private static ScheduledFuture<?> ts = null;
        private int min = 1;

        public int execute(final MapleClient c, String[] splitted) {
            if (c.getChannelServer().getEvent() == c.getPlayer().getMapId()) {
                MapleEvent.setEvent(c.getChannelServer(), false);
                c.getPlayer().dropMessage(5, "已经关闭活动入口，可以使用 !活动开始 來启动。");
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "頻道:" + c.getChannel() + "活动目前已经关闭大门口。"));
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getClock(60));
                ts = EventTimer.getInstance().register(new Runnable() {

                    public void run() {
                        if (min == 0) {
                            MapleEvent.onStartEvent(c.getPlayer());
                            ts.cancel(false);
                            return;
                        }
                        min--;
                    }
                }, 60 * 1000);
                return 1;
            } else {
                c.getPlayer().dropMessage(5, "您必须先使用 !选择活动 设定當前頻道的活动，并在当前頻道活动地图里使用。");
                return 1;
            }
        }

        public String getMessage() {
            return new StringBuilder().append("!活动开始 - 活动开始").toString();
        }
    }

    public static class 关闭活动入口 extends CommandExecute {

        private static boolean tt = false;

        public int execute(MapleClient c, String[] splitted) {
            if (c.getChannelServer().getEvent() == c.getPlayer().getMapId()) {
                MapleEvent.setEvent(c.getChannelServer(), false);
                c.getPlayer().dropMessage(5, "已经关闭活动入口，可以使用 !活动开始 來启动。");
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "頻道:" + c.getChannel() + "活动目前已经关闭大门口。"));
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getClock(60));
                EventTimer.getInstance().register(new Runnable() {

                    public void run() {
                        关闭活动入口.tt = true;
                    }
                }, 60 * 1000);
                if (tt) {
                    MapleEvent.onStartEvent(c.getPlayer());
                }
                return 1;
            } else {
                c.getPlayer().dropMessage(5, "您必须先使用 !选择活动 设定当前頻道的活动，并在当前頻道活动地图里使用。");
                return 1;
            }
        }

        public String getMessage() {
            return new StringBuilder().append("!关闭活动入口 -关闭活动入口").toString();
        }
    }

    public static class 选择活动 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            final MapleEventType type = MapleEventType.getByString(splitted[1]);
            if (type == null) {
                final StringBuilder sb = new StringBuilder("目前开放的活动有: ");
                for (MapleEventType t : MapleEventType.values()) {
                    sb.append(t.name()).append(",");
                }
                c.getPlayer().dropMessage(5, sb.substring(0, sb.toString().length() - 1));
            }
            final String msg = MapleEvent.scheduleEvent(type, c.getChannelServer());
            if (msg.length() > 0) {
                c.getPlayer().dropMessage(5, msg);
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!选择活动 - 选择活动").toString();
        }
    }

    public static class 检查点数 extends CommandExecute {//////checkgas

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleCharacter chrs;
            String name = splitted[1];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage(6, "玩家必须在线");
                return 0;
            }
            chrs = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            if (chrs == null) {
                c.getPlayer().dropMessage(5, "找不到该角色");
            } else {
                c.getPlayer().dropMessage(6, chrs.getName() + " 有 " + chrs.getCSPoints(1) + " 点数.");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!检查点数 <玩家名称>").toString();
        }
    }

    public static class 移除道具 extends CommandExecute {//RemoveItem

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                return 0;
            }
            MapleCharacter chr;
            String name = splitted[1];
            int id = Integer.parseInt(splitted[2]);
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage(6, "玩家必须在线");
                return 0;
            }
            chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

            if (chr == null) {
                c.getPlayer().dropMessage(6, "此玩家并不存在");
            } else {
                chr.removeAll(id);
                c.getPlayer().dropMessage(6, "所有ID为 " + id + " 的道具已经从 " + name + " 身上被移除了");
            }
            return 1;

        }

        public String getMessage() {
            return new StringBuilder().append("!移除 <角色名称> <物品ID> - 移除玩家身上的道具").toString();
        }
    }

//    public static class LockItem extends CommandExecute {
//
//        public int execute(MapleClient c, String splitted[]) {
//            if (splitted.length < 3) {
//                return 0;
//            }
//            MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
//            if (chr == null) {
//                c.getPlayer().dropMessage(6, "此玩家并不存在");
//            } else {
//                int itemid = Integer.parseInt(splitted[2]);
//                MapleInventoryType type = GameConstants.getInventoryType(itemid);
//                for (IItem item : chr.getInventory(type).listById(itemid)) {
//                    item.setFlag((byte) (item.getFlag() | ItemFlag.LOCK.getValue()));
//                    chr.getClient().sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.UPDATE, item)));
//                }
//                if (type == MapleInventoryType.EQUIP) {
//                    type = MapleInventoryType.EQUIPPED;
//                    for (IItem item : chr.getInventory(type).listById(itemid)) {
//                        item.setFlag((byte) (item.getFlag() | ItemFlag.LOCK.getValue()));
//                        chr.getClient().sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.UPDATE, item)));
//                    }
//                }
//                c.getPlayer().dropMessage(6, "玩家 " + splitted[1] + "身上所有ID为 " + splitted[2] + " 的道具已经被锁定了");
//            }
//            return 1;
//        }
//
//        public String getMessage() {
//            return new StringBuilder().append("!lockitem <角色名称> <物品ID> - 上锁玩家身上的道具").toString();
//        }
//    }
    public static class 杀所有人 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            for (MapleCharacter map : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (map != null && !map.isGM()) {
                    map.getStat().setHp((short) 0);
                    map.getStat().setMp((short) 0);
                    map.updateSingleStat(MapleStat.HP, 0);
                    map.updateSingleStat(MapleStat.MP, 0);
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!杀所有人 - 杀掉所有玩家").toString();
        }
    }

    public static class 控制玩家发喇叭 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = null;
            if (splitted.length >= 2) {
                victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            }
            try {
                World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(3, victim == null ? c.getChannel() : victim.getClient().getChannel(), victim == null ? splitted[1] : victim.getName() + " : " + StringUtil.joinStringFrom(splitted, 2), true));
            } catch (Exception e) {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!控制玩家发喇叭 [玩家名称] <讯息> ").toString();
        }
    }

    public static class 控制玩家说话 extends CommandExecute {///Speak

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim;
            String name = splitted[1];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage(6, "玩家必须在线");
                return 0;
            }
            victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

            if (victim == null) {
                c.getPlayer().dropMessage(5, "找不到 '" + splitted[1]);
                return 0;
            } else {
                victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 2), victim.isGM(), 0));
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!控制玩家说话 <玩家名称> <讯息>").toString();
        }
    }

    public static class 本地图的人跟我说 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            for (MapleCharacter victim : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (victim.getId() != c.getPlayer().getId()) {
                    victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 1), victim.isGM(), 0));
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!本地图的人跟我说 <讯息> - 对目前地图进行发送信息").toString();
        }
    }

    public static class 本频道人跟我说 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            for (MapleCharacter victim : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                if (victim.getId() != c.getPlayer().getId()) {
                    victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 1), victim.isGM(), 0));
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!本频道人跟我说 <讯息> - 对目前频道进行发送信息").toString();
        }
    }

    public static class 全服的人跟我说 extends CommandExecute {//SpeakWorld

        public int execute(MapleClient c, String[] splitted) {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter victim : cserv.getPlayerStorage().getAllCharacters()) {
                    if (victim.getId() != c.getPlayer().getId()) {
                        victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 1), victim.isGM(), 0));
                    }
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!全服的人跟我说 <讯息> - 对目前服务器进行传送信息").toString();
        }
    }


    /* public static class Disease extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 3) {
                //   c.getPlayer().dropMessage(6, "");
                return 0;
            }
            int type;
            MapleDisease dis;
            if (splitted[1].equalsIgnoreCase("SEAL")) {
                type = 120;
            } else if (splitted[1].equalsIgnoreCase("DARKNESS")) {
                type = 121;
            } else if (splitted[1].equalsIgnoreCase("WEAKEN")) {
                type = 122;
            } else if (splitted[1].equalsIgnoreCase("STUN")) {
                type = 123;
            } else if (splitted[1].equalsIgnoreCase("CURSE")) {
                type = 124;
            } else if (splitted[1].equalsIgnoreCase("POISON")) {
                type = 125;
            } else if (splitted[1].equalsIgnoreCase("SLOW")) {
                type = 126;
            } else if (splitted[1].equalsIgnoreCase("SEDUCE")) {
                type = 128;
            } else if (splitted[1].equalsIgnoreCase("REVERSE")) {
                type = 132;
            } else if (splitted[1].equalsIgnoreCase("ZOMBIFY")) {
                type = 133;
            } else if (splitted[1].equalsIgnoreCase("POTION")) {
                type = 134;
            } else if (splitted[1].equalsIgnoreCase("SHADOW")) {
                type = 135;
            } else if (splitted[1].equalsIgnoreCase("BLIND")) {
                type = 136;
            } else if (splitted[1].equalsIgnoreCase("FREEZE")) {
                type = 137;
            } else {
                return 0;
            }
            dis = MapleDisease.getBySkill(type);
            if (splitted.length == 4) {
                MapleCharacter victim;
                String name = splitted[2];
                int ch = World.Find.findChannel(name);
                if (ch <= 0) {
                    c.getPlayer().dropMessage(6, "玩家必须在线");
                    return 0;
                }
                victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

                if (victim == null) {
                    c.getPlayer().dropMessage(5, "找不到此玩家");
                } else {
                    victim.setChair(0);
                    victim.getClient().sendPacket(MaplePacketCreator.cancelChair(-1));
                    victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(c.getPlayer().getId(), 0), false);
                    victim.giveDebuff(dis, MobSkillFactory.getMobSkill(type, CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1)));
                }
            } else {
                for (MapleCharacter victim : c.getPlayer().getMap().getCharactersThreadsafe()) {
                    victim.setChair(0);
                    victim.getClient().sendPacket(MaplePacketCreator.cancelChair(-1));
                    victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(c.getPlayer().getId(), 0), false);
                    victim.giveDebuff(dis, MobSkillFactory.getMobSkill(type, CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1)));
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!disease <SEAL/DARKNESS/WEAKEN/STUN/CURSE/POISON/SLOW/SEDUCE/REVERSE/ZOMBIFY/POTION/SHADOW/BLIND/FREEZE> [角色名称] <状态等级> - 让人得到特殊状态").toString();
        }
    }*/
    public static class 推送信息 extends CommandExecute {////////sendallnote

        public int execute(MapleClient c, String[] splitted) {

            if (splitted.length >= 1) {
                String text = StringUtil.joinStringFrom(splitted, 1);
                for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    c.getPlayer().sendNote(mch.getName(), text);
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]推送了信息" + StringUtil.joinStringFrom(splitted, 1) + ""));

                }
            } else {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!推送信息 <文字> ").toString();
        }
    }

    public static class 给金币 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleCharacter victim;
            String name = splitted[1];
            int gain = Integer.parseInt(splitted[2]);
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage(6, "玩家必须在线");
                return 0;
            }
            victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            if (victim == null) {
                c.getPlayer().dropMessage(5, "找不到 '" + name);
            } else {
                victim.gainMeso(gain, true);
                String msg = "[GM 密语] GM " + c.getPlayer().getName() + " 给了 " + victim.getName() + " 金币 " + gain + "点";
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!给金币 <名字> <数量> - 給玩家金币").toString();
        }
    }

    public static class 克隆 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().cloneLook();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!克隆 - 产生克隆体").toString();
        }
    }

    public static class 取消克隆 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, c.getPlayer().getCloneSize() + "个克隆体消失了.");
            c.getPlayer().disposeClones();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!取消克隆 - 摧毁克隆体").toString();
        }
    }

    /* public static class 记录玩家资讯 extends CommandExecute {///////////monitor

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleCharacter target = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (target != null) {
                if (target.getClient().isMonitored()) {
                    target.getClient().setMonitored(false);
                    c.getPlayer().dropMessage(5, "Not monitoring " + target.getName() + " anymore.");
                } else {
                    target.getClient().setMonitored(true);
                    c.getPlayer().dropMessage(5, "Monitoring " + target.getName() + ".");
                }
            } else {
                c.getPlayer().dropMessage(5, "找不到该玩家");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("记录玩家资讯 <玩家> - 记录玩家资讯").toString();
        }
    }*/
    public static class 天气 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getMap().getPermanentWeather() > 0) {
                c.getPlayer().getMap().setPermanentWeather(0);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.removeMapEffect());
                c.getPlayer().dropMessage(5, "地图天气已被禁用.");
            } else {
                final int weather = CommandProcessorUtil.getOptionalIntArg(splitted, 1, 5120000);
                if (!MapleItemInformationProvider.getInstance().itemExists(weather) || weather / 10000 != 512) {
                    c.getPlayer().dropMessage(5, "无效的ID.");
                } else {
                    c.getPlayer().getMap().setPermanentWeather(weather);
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", weather, false));
                    c.getPlayer().dropMessage(5, "地图天气已启用.");
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!天气 - 设定天气").toString();

        }
    }

    public static class 查看角色状态 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {

            if (splitted.length < 2) {
                return 0;
            }
            final StringBuilder builder = new StringBuilder();
            MapleCharacter other;
            String name = splitted[1];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage(6, "玩家必须在线");
                return 0;
            }
            other = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

            if (other == null) {
                builder.append("角色不存在");
                c.getPlayer().dropMessage(6, builder.toString());
            } else {
                if (other.getClient().getLastPing() <= 0) {
                    other.getClient().sendPing();
                }
                builder.append(MapleClient.getLogMessage(other, ""));
                builder.append(" 在 ").append(other.getPosition().x);
                builder.append(" /").append(other.getPosition().y);

                builder.append(" || HP : ");
                builder.append(other.getStat().getHp());
                builder.append(" /");
                builder.append(other.getStat().getCurrentMaxHp());

                builder.append(" || MP : ");
                builder.append(other.getStat().getMp());
                builder.append(" /");
                builder.append(other.getStat().getCurrentMaxMp());

                builder.append(" || 物理攻击力 : ");
                builder.append(other.getStat().getTotalWatk());
                builder.append(" || 魔法攻击力 : ");
                builder.append(other.getStat().getTotalMagic());
                // builder.append(" || 最高攻擊 : ");
                //builder.append(other.getStat().getCurrentMaxBaseDamage());
                // builder.append(" || 攻擊%數 : ");
                // builder.append(other.getStat().dam_r);
                // builder.append(" || BOSS攻擊%數 : ");
                //  builder.append(other.getStat().bossdam_r);

                builder.append(" || 力量 : ");
                builder.append(other.getStat().getStr());
                builder.append(" || 敏捷 : ");
                builder.append(other.getStat().getDex());
                builder.append(" || 智力 : ");
                builder.append(other.getStat().getInt());
                builder.append(" || 幸運 : ");
                builder.append(other.getStat().getLuk());

                builder.append(" || 全部力量 : ");
                builder.append(other.getStat().getTotalStr());
                builder.append(" || 全部敏捷 : ");
                builder.append(other.getStat().getTotalDex());
                builder.append(" || 全部智力 : ");
                builder.append(other.getStat().getTotalInt());
                builder.append(" || 全部幸運 : ");
                builder.append(other.getStat().getTotalLuk());

                builder.append(" || 经验值 : ");
                builder.append(other.getExp());

                builder.append(" || 组队状态 : ");
                builder.append(other.getParty() != null);

                builder.append(" || 交易状态: ");
                builder.append(other.getTrade() != null);
                //builder.append(" || Latency: ");
                //builder.append(other.getClient().getLatency());
                builder.append(" || PING: ");
                builder.append(other.getClient().getLastPing());
                // builder.append(" || 最後PONG: ");
                //  builder.append(other.getClient().getLastPong());
                builder.append(" || IP: ");
                builder.append(other.getClient().getSessionIPAddress());
                other.getClient().DebugMessage(builder);

                c.getPlayer().dropMessage(6, builder.toString());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!查看角色状态 <角色名称> - 查看角色状态").toString();

        }
    }

    public static class 查看此地图所有人 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            StringBuilder builder = new StringBuilder("在此地图的玩家: ");
            for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (builder.length() > 150) { // wild guess :o
                    builder.setLength(builder.length() - 2);
                    c.getPlayer().dropMessage(6, builder.toString());
                    builder = new StringBuilder();
                }
                builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                builder.append(", ");
            }
            builder.setLength(builder.length() - 2);
            c.getPlayer().dropMessage(6, builder.toString());
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!查看此地图所有人 - 查看目前地图上的玩家").toString();

        }
    }

    public static class 看作弊 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            List<CheaterData> cheaters = World.getCheaters();
            for (int x = cheaters.size() - 1; x >= 0; x--) {
                CheaterData cheater = cheaters.get(x);
                c.getPlayer().dropMessage(6, cheater.getInfo());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!看作弊 - 查看作弊角色").toString();

        }
    }

    public static class 已连接人数 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            java.util.Map<Integer, Integer> connected = World.getConnected();
            StringBuilder conStr = new StringBuilder("已连接的客戶端: ");
            boolean first = true;
            for (int i : connected.keySet()) {
                if (!first) {
                    conStr.append(", ");
                } else {
                    first = false;
                }
                if (i == 0) {
                    conStr.append("所有: ");
                    conStr.append(connected.get(i));
                } else {
                    conStr.append("頻道 ");
                    conStr.append(i);
                    conStr.append(": ");
                    conStr.append(connected.get(i));
                }
            }
            c.getPlayer().dropMessage(6, conStr.toString());
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!已连接人数 - 查看已连线的客戶端").toString();

        }
    }

    public static class 重置任务 extends CommandExecute {///////resetquest

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleQuest.getInstance(Integer.parseInt(splitted[1])).forfeit(c.getPlayer());
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重置任务 <任务ID> - 重置任务").toString();

        }
    }

    public static class 开始任务 extends CommandExecute {////////StartQuest

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleQuest.getInstance(Integer.parseInt(splitted[1])).start(c.getPlayer(), Integer.parseInt(splitted[2]));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!开始任务 <任务ID> - 开始任务").toString();

        }
    }

    public static class 完成任务 extends CommandExecute {//CompleteQuest

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleQuest.getInstance(Integer.parseInt(splitted[1])).complete(c.getPlayer(), Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!完成任务 <任务ID> - 完成任务").toString();

        }
    }

    public static class 强制开始任务 extends CommandExecute {//FStartQuest

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleQuest.getInstance(Integer.parseInt(splitted[1])).forceStart(c.getPlayer(), Integer.parseInt(splitted[2]), splitted.length >= 4 ? splitted[3] : null);
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!强制开始任务 <任务ID> - 强制开始任务").toString();

        }
    }

    public static class 强制完成任务 extends CommandExecute {//FCompleteQuest

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleQuest.getInstance(Integer.parseInt(splitted[1])).forceComplete(c.getPlayer(), Integer.parseInt(splitted[2]));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!强制完成任务 <任务ID> - 强制完成任务").toString();

        }
    }

    /* public static class FStartOther extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {

            MapleQuest.getInstance(Integer.parseInt(splitted[2])).forceStart(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]), Integer.parseInt(splitted[3]), splitted.length >= 4 ? splitted[4] : null);
            return 1;
        }

        public String 未知() {
            return new StringBuilder().append("!未知1 - 不知道啥").toString();

        }
    }

    public static class FCompleteOther extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            MapleQuest.getInstance(Integer.parseInt(splitted[2])).forceComplete(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]), Integer.parseInt(splitted[3]));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!fcompleteother - 不知道啥").toString();

        }
    }

    public static class NearestPortal extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            MaplePortal portal = c.getPlayer().getMap().findClosestSpawnpoint(c.getPlayer().getPosition());
            c.getPlayer().dropMessage(6, portal.getName() + " id: " + portal.getId() + " script: " + portal.getScriptName());

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!nearestportal - 不知道啥").toString();

        }
    }

    public static class SpawnDebug extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().dropMessage(6, c.getPlayer().getMap().spawnDebug());
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!spawndebug - debug怪物出生").toString();

        }
    }

    public static class Threads extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            Thread[] threads = new Thread[Thread.activeCount()];
            Thread.enumerate(threads);
            String filter = "";
            if (splitted.length > 1) {
                filter = splitted[1];
            }
            for (int i = 0; i < threads.length; i++) {
                String tstring = threads[i].toString();
                if (tstring.toLowerCase().contains(filter.toLowerCase())) {
                    c.getPlayer().dropMessage(6, i + ": " + tstring);
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!threads - 查看Threads资讯").toString();

        }
    }

    public static class ShowTrace extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;
            }
            Thread[] threads = new Thread[Thread.activeCount()];
            Thread.enumerate(threads);
            Thread t = threads[Integer.parseInt(splitted[1])];
            c.getPlayer().dropMessage(6, t.toString() + ":");
            for (StackTraceElement elem : t.getStackTrace()) {
                c.getPlayer().dropMessage(6, elem.toString());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!showtrace - show trace info").toString();

        }
    }*/
    public static class 刷新 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            c.sendPacket(MaplePacketCreator.getCharInfo(player));
            player.getMap().removePlayer(player);
            player.getMap().addPlayer(player);
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!刷新 - 假登出再登入").toString();

        }
    }

    /*public static class ToggleOffense extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;

            }
            try {
                CheatingOffense co = CheatingOffense.valueOf(splitted[1]);
                co.setEnabled(!co.isEnabled());
            } catch (IllegalArgumentException iae) {
                c.getPlayer().dropMessage(6, "Offense " + splitted[1] + " not found");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!toggleoffense <Offense> - 开启或关闭CheatOffense").toString();

        }
    }*/
    public static class 爆物开关 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().toggleDrops();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!爆物开关 - 开启或关闭掉落").toString();

        }
    }

    public static class 喇叭开关 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            World.toggleMegaphoneMuteState();
            c.getPlayer().dropMessage(6, "广播是否封锁 : " + (c.getChannelServer().getMegaphoneMuteState() ? "是" : "否"));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!喇叭开关 - 开启或者关闭广播").toString();

        }
    }

    /* public static class SpawnReactor extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;
            }
            int id = 0;
            try {
                id = Integer.parseInt(splitted[1]);
            } catch (Exception ex) {
            }
            MapleReactorStats reactorSt = MapleReactorFactory.getReactor(id);
            MapleReactor reactor = new MapleReactor(reactorSt, id);
            reactor.setDelay(-1);
            reactor.setPosition(c.getPlayer().getPosition());
            c.getPlayer().getMap().spawnReactor(reactor);
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!spawnreactor - 设立Reactor").toString();

        }
    }

    public static class HReactor extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;
            }
            c.getPlayer().getMap().getReactorByOid(Integer.parseInt(splitted[1])).hitReactor(c);
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!hitreactor - 触碰Reactor").toString();

        }
    }

    public static class DestroyReactor extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleMap map = c.getPlayer().getMap();
            List<MapleMapObject> reactors = map.getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.REACTOR));
            if (splitted[1].equals("all")) {
                for (MapleMapObject reactorL : reactors) {
                    MapleReactor reactor2l = (MapleReactor) reactorL;
                    c.getPlayer().getMap().destroyReactor(reactor2l.getObjectId());
                }
            } else {
                c.getPlayer().getMap().destroyReactor(Integer.parseInt(splitted[1]));
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!drstroyreactor - 移除Reactor").toString();

        }
    }*/
    public static class 重置反应堆 extends CommandExecute {//ResetReactors

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().resetReactors();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重置反应堆 - 重置此地图所有的Reactor").toString();

        }
    }

    public static class 触碰反应堆 extends CommandExecute {//SetReactor

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            c.getPlayer().getMap().setReactorState(Byte.parseByte(splitted[1]));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!触碰反应堆 - 触碰Reactor").toString();

        }
    }

    //public static class cleardrops extends RemoveDrops {
    // }
    public static class 清物 extends CommandExecute {//RemoveDrops

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "清除了地图上 " + c.getPlayer().getMap().getNumItems() + " 个掉落物");
            c.getPlayer().getMap().removeDrops();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!清物 - 移除地上的物品").toString();

        }
    }

    public static class 修改当前频道经验倍率 extends CommandExecute {//ExpRate

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length > 1) {
                final int rate = Integer.parseInt(splitted[1]);
                if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        cserv.setExpRate(rate);
                    }
                } else {
                    c.getChannelServer().setExpRate(rate);
                }
                c.getPlayer().dropMessage(6, "当前频道经验倍率更改为 " + rate + "倍");
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]将当前频道经验倍率更改为 " + splitted[1] + " 倍"));

            } else {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!修改当前频道经验倍率 <倍率> - 更改经验倍率").toString();

        }
    }

    public static class 修改当前频道物品爆率 extends CommandExecute {//DropRate

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length > 1) {
                final int rate = Integer.parseInt(splitted[1]);
                if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        cserv.setDropRate(rate);
                    }
                } else {
                    c.getChannelServer().setDropRate(rate);
                }
                c.getPlayer().dropMessage(6, "当前频道物品爆率更改为 " + rate + "倍");
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]将当前频道爆物倍率更改为 " + splitted[1] + " 倍"));

            } else {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!修改当前频道物品爆率 <倍率> - 更改掉落倍率").toString();

        }
    }

    public static class 修改当前频道金币倍率 extends CommandExecute {//MesoRate

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length > 1) {
                final int rate = Integer.parseInt(splitted[1]);
                if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        cserv.setMesoRate(rate);
                    }
                } else {
                    c.getChannelServer().setMesoRate(rate);
                }
                c.getPlayer().dropMessage(6, "当前频道金币倍率更改为 " + rate + " 倍");
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]将当前频道金币倍率更改为 " + splitted[1] + " 倍"));

            } else {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!修改当前频道金币倍率 <倍率> - 更改金钱倍率").toString();

        }
    }

    public static class 断线 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            int range = -1;
            if (splitted.length < 2) {
                return 0;
            }
            String input = null;
            try {
                input = splitted[1];
            } catch (Exception ex) {
            }
            switch (splitted[1]) {
                case "m":
                    range = 0;
                    break;
                case "c":
                    range = 1;
                    break;
                case "w":
                default:
                    range = 2;
                    break;
            }
            if (range == -1) {
                range = 1;
            }
            if (range == 0) {
                c.getPlayer().getMap().disconnectAll();
            } else if (range == 1) {
                c.getChannelServer().getPlayerStorage().disconnectAll();
            } else if (range == 2) {
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    cserv.getPlayerStorage().disconnectAll(true);
                }
            }
            String show = "";
            switch (range) {
                case 0:
                    show = "地图";
                    break;
                case 1:
                    show = "頻道";
                    break;
                case 2:
                    show = "世界";
                    break;
            }
            String msg = "[断线指令] 管理员 " + c.getPlayer().getName() + "  切断了 " + show + "玩家";
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!断线 [m|c|w] - 所有玩家断线").toString();
        }
    }

    /* public static class GoTo extends CommandExecute {

        private static final HashMap<String, Integer> gotomaps = new HashMap<>();

        static {
            gotomaps.put("gmmap", 180000000);
            gotomaps.put("southperry", 2000000);
            gotomaps.put("amherst", 1010000);
            gotomaps.put("henesys", 100000000);
            gotomaps.put("ellinia", 101000000);
            gotomaps.put("perion", 102000000);
            gotomaps.put("kerning", 103000000);
            gotomaps.put("lithharbour", 104000000);
            gotomaps.put("sleepywood", 105040300);
            gotomaps.put("florina", 110000000);
            gotomaps.put("orbis", 200000000);
            gotomaps.put("happyville", 209000000);
            gotomaps.put("elnath", 211000000);
            gotomaps.put("ludibrium", 220000000);
            gotomaps.put("aquaroad", 230000000);
            gotomaps.put("leafre", 240000000);
            gotomaps.put("mulung", 250000000);
            gotomaps.put("herbtown", 251000000);
            gotomaps.put("omegasector", 221000000);
            gotomaps.put("koreanfolktown", 222000000);
            gotomaps.put("newleafcity", 600000000);
            gotomaps.put("sharenian", 990000000);
            gotomaps.put("pianus", 230040420);
            gotomaps.put("horntail", 240060200);
            gotomaps.put("chorntail", 240060201);
            gotomaps.put("mushmom", 100000005);
            gotomaps.put("griffey", 240020101);
            gotomaps.put("manon", 240020401);
            gotomaps.put("zakum", 280030000);
            gotomaps.put("czakum", 280030001);
            gotomaps.put("papulatus", 220080001);
            gotomaps.put("showatown", 801000000);
            gotomaps.put("zipangu", 800000000);
            gotomaps.put("ariant", 260000100);
            gotomaps.put("nautilus", 120000000);
            gotomaps.put("boatquay", 541000000);
            gotomaps.put("malaysia", 550000000);
            gotomaps.put("taiwan", 740000000);
            gotomaps.put("thailand", 500000000);
            gotomaps.put("erev", 130000000);
            gotomaps.put("ellinforest", 300000000);
            gotomaps.put("kampung", 551000000);
            gotomaps.put("singapore", 540000000);
            gotomaps.put("amoria", 680000000);
            gotomaps.put("timetemple", 270000000);
            gotomaps.put("pinkbean", 270050100);
            gotomaps.put("peachblossom", 700000000);
            gotomaps.put("fm", 910000000);
            gotomaps.put("freemarket", 910000000);
            gotomaps.put("oxquiz", 109020001);
            gotomaps.put("ola", 109030101);
            gotomaps.put("fitness", 109040000);
            gotomaps.put("snowball", 109060000);
            gotomaps.put("cashmap", 741010200);
            gotomaps.put("golden", 950100000);
            gotomaps.put("phantom", 610010000);
            gotomaps.put("cwk", 610030000);
            gotomaps.put("rien", 140000000);
        }

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "Syntax: !goto <mapname>");
            } else if (gotomaps.containsKey(splitted[1])) {
                MapleMap target = c.getChannelServer().getMapFactory().getMap(gotomaps.get(splitted[1]));
                MaplePortal targetPortal = target.getPortal(0);
                c.getPlayer().changeMap(target, targetPortal);
            } else if (splitted[1].equals("locations")) {
                c.getPlayer().dropMessage(6, "Use !goto <location>. Locations are as follows:");
                StringBuilder sb = new StringBuilder();
                for (String s : gotomaps.keySet()) {
                    sb.append(s).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.substring(0, sb.length() - 2));
            } else {
                c.getPlayer().dropMessage(6, "Invalid command 指令規則 - Use !goto <location>. For a list of locations, use !goto locations.");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!goto <名称> - 到某个地图").toString();

        }
    }*/
    public static class 清怪 extends CommandExecute {//KillAll

        public int execute(MapleClient c, String[] splitted) {
            MapleMap map = c.getPlayer().getMap();
            double range = Double.POSITIVE_INFINITY;
            boolean drop = false;
            if (splitted.length > 1) {
                int irange = 9999;
                if (splitted.length < 2) {
                    range = irange * irange;
                } else {
                    try {
                        map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
                        range = Integer.parseInt(splitted[2]) * Integer.parseInt(splitted[2]);
                    } catch (Exception ex) {
                    }
                }
                if (splitted.length >= 3) {
                    drop = splitted[3].equalsIgnoreCase("true");
                }
            }
            MapleMonster mob;
            List<MapleMapObject> monsters = map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                map.killMonster(mob, c.getPlayer(), drop, false, (byte) 1);
            }
            c.getPlayer().dropMessage("您总共杀了 " + monsters.size() + " 怪物");
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!清怪 [range] [mapid] - 杀掉所有怪物").toString();

        }
    }

    public static class 重置怪物 extends CommandExecute {//ResetMobs

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().killAllMonsters(false);
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]重置了地图[" + splitted[1] + "]的怪物"));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重置怪物 - 重置地图上所有怪物").toString();

        }
    }

    public static class 清除指定怪物 extends CommandExecute {//KillMonster

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleMap map = c.getPlayer().getMap();
            double range = Double.POSITIVE_INFINITY;
            MapleMonster mob;
            for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                if (mob.getId() == Integer.parseInt(splitted[1])) {
                    mob.damage(c.getPlayer(), mob.getHp(), false);
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!清除指定怪物 <mobid> - 杀掉地图上某个怪物").toString();

        }
    }

    /* public static class KillMonsterByOID extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;
            }
            MapleMap map = c.getPlayer().getMap();
            int targetId = Integer.parseInt(splitted[1]);
            MapleMonster monster = map.getMonsterByOid(targetId);
            if (monster != null) {
                map.killMonster(monster, c.getPlayer(), false, false, (byte) 1);
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!killmonsterbyoid <moboid> - 杀掉地图上某个怪物").toString();

        }
    }

    public static class HitMonsterByOID extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            MapleMap map = c.getPlayer().getMap();
            int targetId = Integer.parseInt(splitted[1]);
            int damage = Integer.parseInt(splitted[2]);
            MapleMonster monster = map.getMonsterByOid(targetId);
            if (monster != null) {
                map.broadcastMessage(MobPacket.damageMonster(targetId, damage));
                monster.damage(c.getPlayer(), damage, false);
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!hitmonsterbyoid <moboid> <damage> - 碰撞地图上某個怪物").toString();

        }
    }*/
    public static class 打开NPC extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            int npcId = 0;
            try {
                npcId = Integer.parseInt(splitted[1]);
            } catch (Exception ex) {
            }
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            if (npc != null && !npc.getName().equals("MISSINGNO")) {
                npc.setPosition(c.getPlayer().getPosition());
                npc.setCy(c.getPlayer().getPosition().y);
                npc.setRx0(c.getPlayer().getPosition().x + 50);
                npc.setRx1(c.getPlayer().getPosition().x - 50);
                npc.setFh(c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
                npc.setCustom(true);
                c.getPlayer().getMap().addMapObject(npc);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
            } else {
                c.getPlayer().dropMessage(6, "找不到此代码为" + npcId + "的Npc");

            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!打开NPC <npcid> - 呼叫出NPC").toString();
        }
    }

    public static class 删除NPC extends CommandExecute {////////RemoveNPCs

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().resetNPCs();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!删除NPC - 刪除所有NPC").toString();
        }
    }

    public static class 查看NPC extends CommandExecute {////LookNPCs

        public int execute(MapleClient c, String[] splitted) {
            for (MapleMapObject reactor1l : c.getPlayer().getMap().getAllNPCsThreadsafe()) {
                MapleNPC reactor2l = (MapleNPC) reactor1l;
                c.getPlayer().dropMessage(5, "NPC: oID: " + reactor2l.getObjectId() + " npcID: " + reactor2l.getId() + " Position: " + reactor2l.getPosition().toString() + " Name: " + reactor2l.getName());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!查看NPC - 查看所有NPC").toString();
        }
    }

    public static class 查看反应堆 extends CommandExecute {//////////LookReactors

        public int execute(MapleClient c, String[] splitted) {
            for (MapleMapObject reactor1l : c.getPlayer().getMap().getAllReactorsThreadsafe()) {
                MapleReactor reactor2l = (MapleReactor) reactor1l;
                c.getPlayer().dropMessage(5, "Reactor: oID: " + reactor2l.getObjectId() + " reactorID: " + reactor2l.getReactorId() + " Position: " + reactor2l.getPosition().toString() + " State: " + reactor2l.getState() + " Name: " + reactor2l.getName());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!查看反应堆").toString();
        }
    }

    public static class 查看所有反应堆 extends CommandExecute {//////LookPortals

        public int execute(MapleClient c, String[] splitted) {
            for (MaplePortal portal : c.getPlayer().getMap().getPortals()) {
                c.getPlayer().dropMessage(5, "Portal: ID: " + portal.getId() + " script: " + portal.getScriptName() + " name: " + portal.getName() + " pos: " + portal.getPosition().x + "," + portal.getPosition().y + " target: " + portal.getTargetMapId() + " / " + portal.getTarget());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!查看所有反应堆").toString();
        }
    }

    public static class 玩家NPC extends CommandExecute {/////////MakePNPC

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                return 0;
            }
            try {
                c.getPlayer().dropMessage(6, "Making playerNPC...");
                MapleCharacter chhr;
                String name = splitted[1];
                int ch = World.Find.findChannel(name);
                if (ch <= 0) {
                    c.getPlayer().dropMessage(6, "玩家必须在线");
                    return 1;
                }
                chhr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

                if (chhr == null) {
                    c.getPlayer().dropMessage(6, splitted[1] + " is not online");
                } else {
                    int npcId = Integer.parseInt(splitted[2]);
                    MapleNPC npc_c = MapleLifeFactory.getNPC(npcId);
                    if (npc_c == null || npc_c.getName().equals("MISSINGNO")) {
                        c.getPlayer().dropMessage(6, "NPC不存在");
                        return 1;
                    }
                    PlayerNPC npc = new PlayerNPC(chhr, npcId, c.getPlayer().getMap(), c.getPlayer());
                    npc.addToServer();
                    c.getPlayer().dropMessage(6, "Done");
                }
            } catch (Exception e) {
                c.getPlayer().dropMessage(6, "NPC failed... : " + e.getMessage());

            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!玩家npc <playername> <npcid> - 创造玩家NPC").toString();
        }
    }

    public static class 离线npc extends CommandExecute {//////////MakeOfflineP

        public int execute(MapleClient c, String[] splitted) {
            try {
                c.getPlayer().dropMessage(6, "Making playerNPC...");
                MapleClient cs = new MapleClient(null, null, new MockIOSession());
                MapleCharacter chhr = MapleCharacter.loadCharFromDB(MapleCharacterUtil.getIdByName(splitted[1]), cs, false);
                if (chhr == null) {
                    c.getPlayer().dropMessage(6, splitted[1] + " does not exist");

                } else {
                    PlayerNPC npc = new PlayerNPC(chhr, Integer.parseInt(splitted[2]), c.getPlayer().getMap(), c.getPlayer());
                    npc.addToServer();
                    c.getPlayer().dropMessage(6, "Done");
                }
            } catch (Exception e) {
                c.getPlayer().dropMessage(6, "NPC failed... : " + e.getMessage());

            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!离线npc <charname> <npcid> - 创造离线PNPC").toString();
        }
    }

    public static class 刪除NPC extends CommandExecute {////////destroypnpc

        public int execute(MapleClient c, String[] splitted) {
            try {
                c.getPlayer().dropMessage(6, "Destroying playerNPC...");
                final MapleNPC npc = c.getPlayer().getMap().getNPCByOid(Integer.parseInt(splitted[1]));
                if (npc instanceof PlayerNPC) {
                    ((PlayerNPC) npc).destroy(true);
                    c.getPlayer().dropMessage(6, "Done");
                } else {
                    c.getPlayer().dropMessage(6, "!destroypnpc [objectid]");
                }
            } catch (Exception e) {
                c.getPlayer().dropMessage(6, "NPC failed... : " + e.getMessage());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!刪除NPC [objectid] - 刪除PNPC").toString();
        }
    }

    public static class 我的位置 extends CommandExecute {//////MyPos

        public int execute(MapleClient c, String[] splitted) {
            Point pos = c.getPlayer().getPosition();
            c.getPlayer().dropMessage(6, "X: " + pos.x + " | Y: " + pos.y + " | RX0: " + (pos.x + 50) + " | RX1: " + (pos.x - 50) + " | FH: " + c.getPlayer().getFH() + "| CY:" + pos.y);
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!我的位置").toString();
        }
    }

    public static class 重载箱子 extends CommandExecute {//ReloadOps

        public int execute(MapleClient c, String[] splitted) {
            SendPacketOpcode.reloadValues();
            RecvPacketOpcode.reloadValues();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了重载箱子指令"));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重载箱子 - 重新载入箱子").toString();
        }
    }

    public static class 重新爆物 extends CommandExecute {//ReloadDrops

        public int execute(MapleClient c, String[] splitted) {
            MapleMonsterInformationProvider.getInstance().clearDrops();
            ReactorScriptManager.getInstance().clearDrops();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了重载爆物指令"));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重新爆物 - 重新爆物爆率").toString();
        }
    }

    public static class 重载传送 extends CommandExecute {//ReloadPortals

        public int execute(MapleClient c, String[] splitted) {
            PortalScriptManager.getInstance().clearScripts();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了重载传送指令"));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重载传送 - 重新载入进入点").toString();
        }
    }

    public static class 重载商店 extends CommandExecute {//ReloadShops

        public int execute(MapleClient c, String[] splitted) {
            MapleShopFactory.getInstance().clear();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了重载商店指令"));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重载商店 - 重载商店物品").toString();
        }
    }

    /* public static class ReloadEvents extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            for (ChannelServer instance : ChannelServer.getAllInstances()) {
                instance.reloadEvents();
            }
            return 1;
        }
    }*/
    public static class 重新载入任务 extends CommandExecute {//ReloadQuests

        public int execute(MapleClient c, String[] splitted) {
            MapleQuest.clearQuests();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了重载任务指令"));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重新载入任务 - 重新载入任务").toString();
        }
    }

    public static class 召唤怪物 extends CommandExecute {//Spawn

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            int mid = 0;
            try {
                mid = Integer.parseInt(splitted[1]);
            } catch (Exception ex) {
            }
            int num = Math.min(CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1), 500);
            if (num > 1000) {
                num = 1000;
            }
            Long hp = CommandProcessorUtil.getNamedLongArg(splitted, 1, "hp");
            Integer exp = CommandProcessorUtil.getNamedIntArg(splitted, 1, "exp");
            Double php = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "php");
            Double pexp = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "pexp");

            MapleMonster onemob;
            try {
                onemob = MapleLifeFactory.getMonster(mid);
            } catch (RuntimeException e) {
                c.getPlayer().dropMessage(5, "错误: " + e.getMessage());
                return 1;
            }

            long newhp;
            int newexp;
            if (hp != null) {
                newhp = hp;
            } else if (php != null) {
                newhp = (long) (onemob.getMobMaxHp() * (php / 100));
            } else {
                newhp = onemob.getMobMaxHp();
            }
            if (exp != null) {
                newexp = exp;
            } else if (pexp != null) {
                newexp = (int) (onemob.getMobExp() * (pexp / 100));
            } else {
                newexp = onemob.getMobExp();
            }
            if (newhp < 1) {
                newhp = 1;
            }

            final OverrideMonsterStats overrideStats = new OverrideMonsterStats(newhp, onemob.getMobMaxMp(), newexp, false);
            for (int i = 0; i < num; i++) {
                MapleMonster mob = MapleLifeFactory.getMonster(mid);
                mob.setHp(newhp);
                mob.setOverrideStats(overrideStats);
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, c.getPlayer().getPosition());
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]召唤了怪物[" + splitted[1] + "]指令"));

            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!召唤怪物 <怪物ID> <hp|exp|php||pexp = ?> - 召唤怪物").toString();

        }
    }

    public static class 倒计时 extends CommandExecute {///////clock

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getClock(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 60)));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!倒计时 <time>").toString();
        }
    }

    public static class 传所有人到 extends CommandExecute {//////////WarpPlayersTo

        public int execute(MapleClient c, String[] splitted) {
            try {
                final MapleMap target = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
                final MapleMap from = c.getPlayer().getMap();
                for (MapleCharacter chr : from.getCharactersThreadsafe()) {
                    chr.changeMap(target, target.getPortal(0));
                }
            } catch (Exception e) {
                return 0; //assume drunk GM
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!传所有人到 <maipid> ").toString();
        }
    }

    /* public static class LOLCastle extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length != 2) {
                return 0;
            }
            MapleMap target = c.getChannelServer().getEventSM().getEventManager("lolcastle").getInstance("lolcastle" + splitted[1]).getMapFactory().getMap(990000300, false, false);
            c.getPlayer().changeMap(target, target.getPortal(0));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!lolcastle level (level = 1-5) - 不知道是啥").toString();
        }
    }*/
    public static class 传送 extends CommandExecute {//Map

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            try {
                MapleMap target = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
                if (target == null) {
                    c.getPlayer().dropMessage(5, "地图不存在.");
                    return 1;
                }
                MaplePortal targetPortal = null;
                if (splitted.length > 2) {
                    try {
                        targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
                    } catch (IndexOutOfBoundsException e) {
                        // noop, assume the gm didn't know how many portals there are
                        c.getPlayer().dropMessage(5, "传送点错误.");
                    } catch (NumberFormatException a) {
                        // noop, assume that the gm is drunk
                    }
                }
                if (targetPortal == null) {
                    targetPortal = target.getPortal(0);
                }
                c.getPlayer().changeMap(target, targetPortal);
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]传送到地图[" + splitted[1] + "]"));

            } catch (Exception e) {
                c.getPlayer().dropMessage(5, "Error: " + e.getMessage());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!传送 <mapid|charname> [portal] - 传送到某地图/人").toString();
        }
    }

    /*  public static class StartProfiling extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            CPUSampler sampler = CPUSampler.getInstance();
            sampler.addIncluded("client");
            sampler.addIncluded("constants"); //or should we do Packages.constants etc.?
            sampler.addIncluded("database");
            sampler.addIncluded("handling");
            sampler.addIncluded("provider");
            sampler.addIncluded("scripting");
            sampler.addIncluded("server");
            sampler.addIncluded("tools");
            sampler.start();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!startprofiling 开始记录JVM资讯").toString();
        }
    }

    public static class StopProfiling extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            CPUSampler sampler = CPUSampler.getInstance();
            try {
                String filename = "odinprofile.txt";
                if (splitted.length > 1) {
                    filename = splitted[1];
                }
                File file = new File(filename);
                if (file.exists()) {
                    c.getPlayer().dropMessage(6, "The entered filename already exists, choose a different one");
                    return 1;
                }
                sampler.stop();
                try (FileWriter fw = new FileWriter(file)) {
                    sampler.save(fw, 1, 10);
                }
            } catch (IOException e) {
                System.err.println("Error saving profile" + e);
            }
            sampler.reset();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!stopprofiling <filename> - 取消记录JVM资讯并保存到档案").toString();
        }
    }*/
    public static class 重置某个地图 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (splitted.length < 2) {
                return 0;
            }
            boolean custMap = splitted.length >= 2;
            int mapid = custMap ? Integer.parseInt(splitted[1]) : player.getMapId();
            MapleMap map = custMap ? player.getClient().getChannelServer().getMapFactory().getMap(mapid) : player.getMap();
            if (player.getClient().getChannelServer().getMapFactory().destroyMap(mapid)) {
                MapleMap newMap = player.getClient().getChannelServer().getMapFactory().getMap(mapid);
                MaplePortal newPor = newMap.getPortal(0);
                LinkedHashSet<MapleCharacter> mcs = new LinkedHashSet<>(map.getCharacters()); // do NOT remove, fixing ConcurrentModificationEx.
                outerLoop:
                for (MapleCharacter m : mcs) {
                    for (int x = 0; x < 5; x++) {
                        try {
                            m.changeMap(newMap, newPor);
                            continue outerLoop;
                        } catch (Throwable t) {
                        }
                    }
                    player.dropMessage("Failed warping " + m.getName() + " to the new map. Skipping...");
                }
                player.dropMessage("地图刷新完毕，如还出现NPC不见请使用此命令.");
                return 1;
            }
            player.dropMessage("Unsuccessful reset!");
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重置某个地图 <maipid> - 重置某个地图").toString();
        }
    }

    public static class 重载地图 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().respawn(true);
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了重载地图指令"));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重载地图 - 重新载入地图").toString();
        }
    }

    public static class 重置地图 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().resetFully();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了重置地图指令"));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重置地图 - 重置这个地图").toString();
        }
    }

    public static class 重置服务端 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            for (ChannelServer instance : ChannelServer.getAllInstances()) {
                instance.reloadEvents();
            }
            MapleShopFactory.getInstance().clear();
            PortalScriptManager.getInstance().clearScripts();
            MapleItemInformationProvider.getInstance().load();

            CashItemFactory.getInstance().initialize();
            MapleMonsterInformationProvider.getInstance().clearDrops();

            MapleGuild.loadAll(); //(this); 
            MapleFamily.loadAll(); //(this); 
            MapleLifeFactory.loadQuestCounts();
            MapleQuest.initQuests();
            MapleOxQuizFactory.getInstance();
            ReactorScriptManager.getInstance().clearDrops();
            SendPacketOpcode.reloadValues();
            RecvPacketOpcode.reloadValues();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了重置服务端指令"));

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!重置服务端 - 重置全服务器").toString();
        }
    }

    public static class PNPC extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {

            int npcId = Integer.parseInt(splitted[1]);
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            if (npc != null && !npc.getName().equals("MISSINGNO")) {
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
                    try (com.mysql.jdbc.PreparedStatement ps = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("INSERT INTO 游戏NPC管理 (dataid, f, hide, fh, cy, rx0, rx1, type, x, y, mid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
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
                    c.getPlayer().dropMessage(6, "未能将NPC保存到数据库");
                }
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    cserv.getMapFactory().getMap(c.getPlayer().getMapId()).addMapObject(npc);
                    cserv.getMapFactory().getMap(c.getPlayer().getMapId()).broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
//                    c.getPlayer().getMap().addMapObject(npc);
//                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
                }
                c.getPlayer().dropMessage(6, "NPC建立成功，如果需要删除，请在数据库《游戏NPC管理》中删除，重启生效");
            } else {
                c.getPlayer().dropMessage(6, "查无此 Npc ");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!永久npc - 建立永久NPC").toString();
        }
    }

    public static class 家具 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {

            int npcId = Integer.parseInt(splitted[1]);
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
                try (com.mysql.jdbc.PreparedStatement ps = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("INSERT INTO 家具管理 (dataid, f, hide, fh, cy, rx0, rx1, type, x, y, mid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
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
                c.getPlayer().dropMessage(6, "未能将家具保存到数据库");
            }
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.getMapFactory().getMap(c.getPlayer().getMapId()).addMapObject(npc);
                cserv.getMapFactory().getMap(c.getPlayer().getMapId()).broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
//                    c.getPlayer().getMap().addMapObject(npc);
//                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
            }
            c.getPlayer().dropMessage(6, "创建家具成功，如果需要删除，请在数据库《家具管理》中删除，重启生效");

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!家具 - 建立永久家具").toString();
        }
    }

    public static class 复制玩家物品 extends CommandExecute {////////copyinv

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            MapleCharacter victim;
            int type = 1;
            if (splitted.length < 2) {
                return 0;
            }

            String name = splitted[1];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage(6, "玩家必须在线");
                return 0;
            }
            victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
            if (victim == null) {
                player.dropMessage("找不到该玩家");
                return 1;
            }
            try {
                type = Integer.parseInt(splitted[2]);
            } catch (Exception ex) {
            }
            if (type == 0) {
                for (client.inventory.IItem ii : victim.getInventory(MapleInventoryType.EQUIPPED).list()) {
                    client.inventory.IItem n = ii.copy();
                    player.getInventory(MapleInventoryType.EQUIP).addItem(n);
                }
                player.fakeRelog();
            } else {
                MapleInventoryType types;
                if (type == 1) {
                    types = MapleInventoryType.EQUIP;
                } else if (type == 2) {
                    types = MapleInventoryType.USE;
                } else if (type == 3) {
                    types = MapleInventoryType.ETC;
                } else if (type == 4) {
                    types = MapleInventoryType.SETUP;
                } else if (type == 5) {
                    types = MapleInventoryType.CASH;
                } else {
                    types = null;
                }
                if (types == null) {
                    c.getPlayer().dropMessage("发生错误");
                    return 1;
                }
                int[] equip = new int[97];
                for (int i = 1; i < 97; i++) {
                    if (victim.getInventory(types).getItem((short) i) != null) {
                        equip[i] = i;
                    }
                }
                for (int i = 0; i < equip.length; i++) {
                    if (equip[i] != 0) {
                        client.inventory.IItem n = victim.getInventory(types).getItem((short) equip[i]).copy();
                        player.getInventory(types).addItem(n);
                    }
                }
                player.fakeRelog();
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!复制玩家物品 玩家名称 装备栏位(0 = 装备中 1=装备栏 2=消耗栏 3=其他栏 4=装饰栏 5=点数栏)(预设装备栏) - 复制玩家道具").toString();
        }
    }

    public static class 移除玩家物品 extends CommandExecute {//RemoveItemOff

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            com.mysql.jdbc.Connection dcon = (com.mysql.jdbc.Connection) DatabaseConnection.getConnection();
            try {
                int id = 0, quantity = 0;
                String name = splitted[2];
                com.mysql.jdbc.PreparedStatement ps = (com.mysql.jdbc.PreparedStatement) dcon.prepareStatement("select * from characters where name = ?");
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt("id");
                    }
                }
                if (id == 0) {
                    c.getPlayer().dropMessage(5, "角色不存在资料库。");
                    return 0;
                }
                com.mysql.jdbc.PreparedStatement ps2 = (com.mysql.jdbc.PreparedStatement) dcon.prepareStatement("delete from inventoryitems WHERE itemid = ? and characterid = ?");
                ps2.setInt(1, Integer.parseInt(splitted[1]));
                ps2.setInt(2, id);
                ps2.executeUpdate();
                c.getPlayer().dropMessage(6, "所有ID为 " + splitted[1] + " 的道具" + quantity + "已经从 " + name + " 身上被移除了");
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]移除了 " + name + " 身上所有ID为 " + splitted[1] + " 的道具" + quantity + ""));
                ps.close();
                ps2.close();
                return 1;
            } catch (SQLException e) {
                return 0;
            }
        }

        public String getMessage() {
            return new StringBuilder().append("!移除玩家物品 <物品ID> <角色名稱> - 移除玩家身上的道具").toString();
        }
    }

    public static class 给所有人经验 extends CommandExecute {//ExpEveryone

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " <经验量>");
                return 0;
            }
            int gain = Integer.parseInt(splitted[1]);
            int ret = 0;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    mch.gainExp(gain, true, true, true);
                    ret++;
                }
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect("管理员发放" + gain + "经验给在线的所有玩家！祝您玩的开心玩的快乐", 5120027);
                    FileoutputUtil.logToFile("服务端记录信息/在线经验发送记录.txt", "\r\n【时间:" + CurrentReadable_Time() + "】【名字:" + c.getPlayer().getName() + "】给所有在线玩家 " + gain + " 经验，此次总计发送 " + ret * gain + " 经验");

                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 点的").append(" 经验 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class 给所有人点券 extends CommandExecute {//CashEveryone

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length > 2) {
                int type = Integer.parseInt(splitted[1]);
                int quantity = Integer.parseInt(splitted[2]);
                if (type == 1) {
                    type = 1;
                } else if (type == 2) {
                    type = 2;
                } else {
                    c.getPlayer().dropMessage(6, "用法: !给所有人点券 [点卷类型1-2] [点卷数量][1是点卷.2是抵用卷]");
                    return 0;
                }
                if (quantity > 10000) {
                    quantity = 10000;
                }
                int ret = 0;
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                        mch.modifyCSPoints(type, quantity);
                        ret++;
                    }
                }
                String show = type == 1 ? "点卷" : "抵用卷";
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        mch.startMapEffect("管理员发放" + quantity + show + "点卷给在线的所有玩家！祝您的开心玩的快乐", 5120027);
                        FileoutputUtil.logToFile("服务端记录信息/在线点券发送记录.txt", "\r\n【时间:" + CurrentReadable_Time() + "】【名字:" + c.getPlayer().getName() + "】给所有在线玩家 " + quantity + show + " 点券，此次总计发送 " + ret * quantity + " 点券");

                    }
                }
                c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(quantity).append(" 点的").append(type == 1 ? "点券 " : " 抵用券 ").append(" 总计: ").append(ret * quantity).toString());
            } else {
                c.getPlayer().dropMessage(6, "用法: !给所有人点卷 [点卷类型1-2] [点卷数量][1是点卷.2是抵用卷]");
            }
            return 1;
        }
    }

    public static class 给所有人金币 extends CommandExecute {//mesoEveryone

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " <金币量>");
                return 0;
            }
            int ret = 0;
            int gain = Integer.parseInt(splitted[1]);
            for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharactersThreadSafe()) {
                mch.gainMeso(gain, true);
                ret++;
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect("管理员发放" + gain + "冒险币给在线的所有玩家！祝您玩的开心玩的快乐", 5120027);
                    FileoutputUtil.logToFile("服务端记录信息/在线金币发送记录.txt", "\r\n【时间:" + CurrentReadable_Time() + "】【名字:" + c.getPlayer().getName() + "】给所有在线玩家 " + gain + " 金币，此次总计发送 " + ret * gain + " 金币");

                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 冒险币 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class 倍率设置 extends CommandExecute {//setRate

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            MapleCharacter mc = player;
            if (splitted.length > 2) {
                int arg = Integer.parseInt(splitted[2]);
                int seconds = Integer.parseInt(splitted[3]);
                int mins = Integer.parseInt(splitted[4]);
                int hours = Integer.parseInt(splitted[5]);
                int time = seconds + (mins * 60) + (hours * 60 * 60);
                boolean bOk = true;
                if (splitted[1].equals("经验")) {
                    if (arg <= Integer.parseInt(ServerProperties.getProperty("ZEV.Exp2"))) {
                        for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                            cservs.setExpRate(arg);
                            cservs.broadcastPacket(MaplePacketCreator.serverNotice(6, "[" + MapleParty.开服名字 + "系统公告]：" + "服务器经验倍率已经修改为 " + arg + " 倍，持续时间为 " + hours + " 小时 " + mins + " 分 " + seconds + " 秒。"));
                            //cservs.startMapEffect("["+MapleParty.开服名字+"系统公告]："+"服务器经验倍率已经修改为 " + arg + " 倍，持续时间为 "+hours+" 小时 "+mins+" 分 "+seconds+" 秒。", 5120027);
                            FileoutputUtil.logToFile("服务端记录信息/开启经验活动.txt", "【时间：" + CurrentReadable_Time() + "】【" + c.getPlayer().getName() + "】 开启了 【" + arg + "】 倍经验活动，持续时间为【" + hours + " 小时 " + mins + " 分 " + seconds + " 秒】\r\n");
                        }
                    } else {
                        mc.dropMessage("经验倍率设置最高 " + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp2")) + " 倍");
                    }
                } else if (splitted[1].equals("爆率")) {//drop
                    if (arg <= Integer.parseInt(ServerProperties.getProperty("ZEV.Drop2"))) {
                        for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                            cservs.setDropRate(arg);
                            cservs.broadcastPacket(MaplePacketCreator.serverNotice(6, "[" + MapleParty.开服名字 + "系统公告]：" + "服务器爆物倍率已经修改为 " + arg + " 倍，持续时间为 " + hours + " 小时 " + mins + " 分 " + seconds + " 秒。"));
                            FileoutputUtil.logToFile("服务端记录信息/开启爆物活动.txt", "【时间：" + CurrentReadable_Time() + "】【" + c.getPlayer().getName() + "】 开启了 【" + arg + "】 倍爆物活动，持续时间为【" + hours + " 小时 " + mins + " 分 " + seconds + " 秒】\r\n");
                        }
                    } else {
                        mc.dropMessage("爆物倍率设置最高 " + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop2")) + " 倍");
                    }
                } else if (splitted[1].equals("金币")) {
                    if (arg <= Integer.parseInt(ServerProperties.getProperty("ZEV.Meso2"))) {
                        for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                            cservs.setMesoRate(arg);
                            cservs.broadcastPacket(MaplePacketCreator.serverNotice(6, "[" + MapleParty.开服名字 + "系统公告]：" + "服务器金币倍率已经修改为 " + arg + " 倍，持续时间为 " + hours + " 小时 " + mins + " 分 " + seconds + " 秒。"));
                            FileoutputUtil.logToFile("服务端记录信息/开启金币活动.txt", "【时间：" + CurrentReadable_Time() + "】【" + c.getPlayer().getName() + "】 开启了 【" + arg + "】 倍金币活动，持续时间为【" + hours + " 小时 " + mins + " 分 " + seconds + " 秒】\r\n");
                        }
                    } else {
                        mc.dropMessage("金币倍率设置最高 " + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso2")) + " 倍");
                    }
                } else if (splitted[1].equalsIgnoreCase("boss爆率")) {
                    if (arg <= Integer.parseInt(ServerProperties.getProperty("ZEV.BDrop2"))) {
                        for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                            cservs.setBossDropRate(arg);
                            cservs.broadcastPacket(MaplePacketCreator.serverNotice(6, "[" + MapleParty.开服名字 + "系统公告]：" + "服务器BOSS爆物倍率已经修改为 " + arg + " 倍，持续时间为 " + hours + " 小时 " + mins + " 分 " + seconds + " 秒。"));
                            FileoutputUtil.logToFile("服务端记录信息/开启BOSS爆物活动.txt", "【时间：" + CurrentReadable_Time() + "】【" + c.getPlayer().getName() + "】 开启了 【" + arg + "】 倍BOSS爆物活动，持续时间为【" + hours + " 小时 " + mins + " 分 " + seconds + " 秒】\r\n");
                        }
                    } else {
                        mc.dropMessage("BOSS爆物倍率设置最高 " + Integer.parseInt(ServerProperties.getProperty("ZEV.BDrop2")) + " 倍");
                    }
                } else if (splitted[1].equals("宠物经验")) {//petexp
                    if (arg <= 5) {
//                        for (ChannelServer cservs : ChannelServer.getAllInstances()) {
//                            cservs.setPetExpRate(arg);
//                            cservs.broadcastPacket(MaplePacketCreator.serverNotice(6, "宠物经验已经成该修改为 " + arg + "倍。祝大家游戏开心！"));
//                        }
                    } else {
                        mc.dropMessage("操作已被系统限制。");
                    }
                } else {
                    bOk = false;
                }
                if (bOk) {
                    final String rate = splitted[1];
                    World.scheduleRateDelay(rate, time);
                } else {
                    mc.dropMessage("使用方法: !倍率设置 <经验|爆率|金币|boss爆率> <倍率> <秒> <分> <时>");

                }
            } else {
                mc.dropMessage("使用方法: !倍率设置 <经验|爆率|金币|boss爆率> <倍率> <秒> <分> <时>");
            }
            return 1;
        }
    }

    public static class 拉所有人 extends CommandExecute {//WarpAllHere

        public int execute(MapleClient c, String[] splitted) {
            for (ChannelServer CS : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : CS.getPlayerStorage().getAllCharactersThreadSafe()) {
                    if (mch.getMapId() != c.getPlayer().getMapId()) {
                        mch.changeMap(c.getPlayer().getMap(), c.getPlayer().getPosition());
                    }
                    if (mch.getClient().getChannel() != c.getPlayer().getClient().getChannel()) {
                        mch.changeChannel(c.getPlayer().getClient().getChannel());
                    }
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!拉所有人 把所有玩家传送到这里").toString();
        }
    }

    public static class 满技能 extends CommandExecute {//maxSkills

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            player.maxSkills();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了满技能指令"));

            return 1;
        }
    }

    public static class 丢 extends CommandExecute {//Drop

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                return 0;
            }
            int itemId = 0;
            try {
                itemId = Integer.parseInt(splitted[1]);
            } catch (Exception ex) {
            }

            final short quantity = (short) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

            if (GameConstants.isPet(itemId)) {
                c.getPlayer().dropMessage(5, "宠物请到购物商城购买.");
            } else if (!ii.itemExists(itemId)) {
                c.getPlayer().dropMessage(5, itemId + " - 物品不存在");
            } else {
                IItem toDrop;
                if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {

                    toDrop = ii.randomizeStats((Equip) ii.getEquipById(itemId));
                } else {
                    toDrop = new client.inventory.Item(itemId, (byte) 0, quantity, (byte) 0);
                }
                //toDrop.setOwner(c.getPlayer().getName());
                toDrop.setGMLog(c.getPlayer().getName());

                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]丢出了物品[" + splitted[1] + "]"));
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
            }
            return 1;
        }
    }

    public static class 祝福 extends CommandExecute {//buff

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            SkillFactory.getSkill(9001002).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001003).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001008).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001001).getEffect(1).applyTo(player);
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了祝福指令"));

            return 1;
        }
    }

    public static class 满属性 extends CommandExecute {//maxstats

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            player.getStat().setMaxHp((short) 30000);
            player.getStat().setMaxMp((short) 30000);
            player.getStat().setStr(Short.MAX_VALUE);
            player.getStat().setDex(Short.MAX_VALUE);
            player.getStat().setInt(Short.MAX_VALUE);
            player.getStat().setLuk(Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.MAXHP, 30000);
            player.updateSingleStat(MapleStat.MAXMP, 30000);
            player.updateSingleStat(MapleStat.STR, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.DEX, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.INT, Short.MAX_VALUE);
            player.updateSingleStat(MapleStat.LUK, Short.MAX_VALUE);
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了满属性指令"));
            return 1;
        }
    }

    public static class 低属性 extends CommandExecute {//maxstats

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            player.getStat().setMaxHp((short) 2000);
            player.getStat().setMaxMp((short) 2000);
            /*player.getStat().setStr(200);
            player.getStat().setDex(200);
            player.getStat().setInt(200);
            player.getStat().setLuk(200);*/
            player.updateSingleStat(MapleStat.MAXHP, 2000);
            player.updateSingleStat(MapleStat.MAXMP, 2000);
            player.updateSingleStat(MapleStat.STR, 200);
            player.updateSingleStat(MapleStat.DEX, 200);
            player.updateSingleStat(MapleStat.INT, 200);
            player.updateSingleStat(MapleStat.LUK, 200);
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了低属性指令"));
            return 1;
        }
    }

    public static class 地图代码 extends CommandExecute {//WhereAmI

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "目前地图 " + c.getPlayer().getMap().getId() + "坐标 (" + c.getPlayer().getPosition().x + " , " + c.getPlayer().getPosition().y + ")");
            return 1;
        }
    }

    public static class 发送封包 extends CommandExecute {//Packet

        @Override
        public int execute(MapleClient c, String[] splitted) {
            final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            int packetheader = Integer.parseInt(splitted[1]);
            String packet_in = " 00 00 00 00 00 00 00 00 00 ";
            if (splitted.length > 2) {
                packet_in = StringUtil.joinStringFrom(splitted, 2);
            }
            mplew.writeShort(packetheader);
            mplew.write(HexTool.getByteArrayFromHexString(packet_in));
            // mplew.write(1);
            // mplew.writeInt(2270002);
            // mplew.writeInt(100100);
            /*
             * mplew.write(1);// ariant mplew.writeMapleAsciiString("123456");
             * mplew.write(0x64);
             */
            mplew.writeZeroBytes(20);
            c.sendPacket(mplew.getPacket());

            c.getPlayer().dropMessage(packetheader + "已传送封包[" + mplew.getPacket().length + "] : " + mplew);
            return 1;
        }
    }

    public static class 怪物代码 extends CommandExecute {//mob

        public int execute(MapleClient c, String[] splitted) {
            MapleMonster monster = null;
            for (final MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER))) {
                monster = (MapleMonster) monstermo;
                if (monster.isAlive()) {
                    c.getPlayer().dropMessage(6, "怪物 " + monster);
                }
            }
            if (monster == null) {
                c.getPlayer().dropMessage(6, "找不到怪物");
            }
            return 1;
        }
    }

    public static class 注册 extends CommandExecute {//register

        @Override
        public int execute(MapleClient c, String[] splitted) {
            String acc = null;
            String password = null;
            try {
                acc = splitted[1];
                password = splitted[2];
            } catch (Exception ex) {
            }
            if (acc == null || password == null) {
                c.getPlayer().dropMessage("账号或密码异常");
                return 0;
            }
            boolean ACCexist = AutoRegister.getAccountExists(acc);
            if (ACCexist) {
                c.getPlayer().dropMessage("帐号已被使用");
                return 0;
            }
            if (acc.length() >= 12) {
                c.getPlayer().dropMessage("密码长度过长");
                return 0;
            }

            Connection con;
            try {
                con = DatabaseConnection.getConnection();
            } catch (Exception ex) {
                System.out.println(ex);
                return 0;
            }

            try {
                try (PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO accounts (name, password) VALUES (?, ?)")) {
                    ps.setString(1, acc);
                    ps.setString(2, LoginCrypto.hexSha1(password));
                    ps.executeUpdate();
                    ps.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
                return 0;
            }
            c.getPlayer().dropMessage("[注册完成]账号: " + acc + " 密码: " + password);

            return 1;
        }
    }

    public static class 开放地图 extends CommandExecute {//openmap

        public int execute(MapleClient c, String[] splitted) {
            int mapid = 0;
            String input = null;
            MapleMap map = null;
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " - 开放地图");
                return 0;
            }
            try {
                input = splitted[1];
                mapid = Integer.parseInt(input);
            } catch (Exception ex) {
            }
//            if (c.getChannelServer().getMapFactory().getMap(mapid) == null) {
//                c.getPlayer().dropMessage("地图不存在");
//                return 0;
//            }
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.getMapFactory().HealMap(mapid);

            }
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]开放了地图[" + splitted[1] + "]"));
            return 1;

        }
    }

    public static class 关闭地图 extends CommandExecute {//closemap

        public int execute(MapleClient c, String[] splitted) {
            int mapid = 0;
            String input = null;
            MapleMap map = null;
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " - 关闭地图");
                return 0;
            }
            try {
                input = splitted[1];
                mapid = Integer.parseInt(input);
            } catch (Exception ex) {
            }
            if (c.getChannelServer().getMapFactory().getMap(mapid) == null) {
                c.getPlayer().dropMessage("地图不存在");
                return 0;
            }
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.getMapFactory().destroyMap(mapid, true);
            }
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]关闭了地图[" + splitted[1] + "]"));

            return 1;

        }
    }

    public static class reloadcpq extends CommandExecute {//reloadcpq

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().reloadCPQ();
            c.getPlayer().dropMessage("嘉年华地图更新成功");
            return 1;
        }
    }
}
