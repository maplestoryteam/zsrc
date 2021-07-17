package client.messages.commands;

import static abc.Game.主城;
import static abc.Game.作者QQ;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import tools.FileoutputUtil;
import constants.ServerConstants.PlayerGMRank;
import client.MapleClient;
import client.MapleStat;
import database.DatabaseConnection;
import handling.world.World;
import java.sql.SQLException;
import java.util.Arrays;
import server.life.MapleMonster;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.StringUtil;
import scripting.NPCScriptManager;
import server.ServerProperties;
import client.DebugWindow;
import client.MapleCharacter;
import static gui.QQMsgServer.sendMsgToQQGroup;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import server.shops.IMaplePlayerShop;
import static tools.FileoutputUtil.CurrentReadable_Date;
import static tools.FileoutputUtil.CurrentReadable_Time;

public class 玩家指令 {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.玩家指令;
    }

//    public static class 封号 extends CommandExecute {
//
//        protected boolean hellban = false;
//
//        private String getCommand() {
//            return "封号";
//        }
//
//        @Override
//        public int execute(MapleClient c, String[] splitted) {
//
//            if (c.getPlayer().getLevel() <= 30) {
//                c.getPlayer().dropMessage(1, "等级低于 30 级无法使用");
//                return 0;
//            }
//            if (splitted.length < 3) {
//                c.getPlayer().dropMessage(5, "[封号]*" + getCommand() + " <玩家> <原因>");
//                return 0;
//
//            }
//            int ch = World.Find.findChannel(splitted[1]);
//            StringBuilder sb = new StringBuilder(c.getPlayer().getName());
//            sb.append(" banned ").append(splitted[1]).append(": ").append(StringUtil.joinStringFrom(splitted, 2));
//            MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
//            if (target == null || ch < 1) {
//                if (MapleCharacter.ban(splitted[1], sb.toString(), false, c.getPlayer().isAdmin() ? 250 : c.getPlayer().getGMLevel(), splitted[0].equals("@帮助"))) {
//                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 成功离线封锁 " + splitted[1] + ".");
//                    return 1;
//                } else {
//                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 封锁失败 " + splitted[1]);
//                    return 0;
//                }
//            }
//            sb.append(" (IP: ").append(target.getClient().getSessionIPAddress()).append(")");
//            if (target.ban(sb.toString(), c.getPlayer().isAdmin(), false, hellban)) {
//                c.getPlayer().dropMessage(6, "[" + getCommand() + "] 成功封锁 " + splitted[1] + ".");
//                FileoutputUtil.logToFile("玩家封号记录/[" + CurrentReadable_Date() + "]玩家封号.txt", "" + CurrentReadable_Time() + " " + c.getPlayer().getName() + " 封禁 " + target.getName() + " 原因是 " + splitted[3] + "\r\n");
//                String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 被系统封号处理，封号原因 " + splitted[3] + " 。如果玩家 " + target.getName() + "  对封号存在异议，请找管理员进行申诉。";
//                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
//                sendMsgToQQGroup(信息);
//            }
//            return 1;
//        }
//    }

    public static class lhdy extends 拉回队友 {
    }

    public static class 关闭雇佣 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            final IMaplePlayerShop merchant = c.getPlayer().getPlayerShop();
            //if (merchant != null && merchant.getShopType() == 1 && merchant.isOwner(c.getPlayer()) && merchant.isAvailable()) {
            //merchant.removeAllVisitors(-1, -1);
            //merchant.closeShop(true, true);
            c.getPlayer().setPlayerShop(null);
            c.getPlayer().dropMessage(1, "请在市场领回雇佣物品");
            //}
            return 1;
        }
    }

    public static class 拉回队友 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            /**
             * <废弃都市拉回队友>
             */
            if (c.getPlayer().getMapId() >= 103000800 && c.getPlayer().getMapId() <= 103000805) {
                for (final MaplePartyCharacter chr : c.getPlayer().getParty().getMembers()) {
                    final MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
                    final int Map = c.getPlayer().getMapId();
                    if (curChar.getMapId() != Map && curChar.getMapId() == 103000890) {
                        curChar.dropMessage(1, "你的队友拉你回副本，稍后就会传送至队友身边。");
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000 * 3);
                                    curChar.changeMap(Map, 0);
                                } catch (InterruptedException e) {
                                }
                            }
                        }.start();
                    }
                }
                /**
                 * <月妙拉回队友>
                 */
            } else if (c.getPlayer().getMapId() == 910010000) {
                for (final MaplePartyCharacter chr : c.getPlayer().getParty().getMembers()) {
                    final MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
                    final int Map = c.getPlayer().getMapId();
                    if (curChar.getMapId() != Map && curChar.getMapId() == 910010300) {
                        curChar.dropMessage(1, "你的队友拉你回副本，稍后就会传送至队友身边。");
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000 * 3);
                                    curChar.changeMap(Map, 0);
                                } catch (InterruptedException e) {
                                }
                            }
                        }.start();
                    }
                }
                /**
                 * <玩具拉回队友>
                 */
            } else if (c.getPlayer().getMapId() == 922011000 || c.getPlayer().getMapId() == 922010900 || c.getPlayer().getMapId() == 922010800 || c.getPlayer().getMapId() == 922010700 || c.getPlayer().getMapId() == 922010600 || c.getPlayer().getMapId() == 922010500 || c.getPlayer().getMapId() == 922010400 || c.getPlayer().getMapId() == 922010100 || c.getPlayer().getMapId() == 922010200 || c.getPlayer().getMapId() == 922010300) {
                for (final MaplePartyCharacter chr : c.getPlayer().getParty().getMembers()) {
                    final MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
                    final int Map = c.getPlayer().getMapId();
                    if (curChar.getMapId() != Map && curChar.getMapId() == 922010000) {
                        curChar.dropMessage(1, "你的队友拉你回副本，稍后就会传送至队友身边。");
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000 * 3);
                                    curChar.changeMap(Map, 0);
                                } catch (InterruptedException e) {
                                }
                            }
                        }.start();
                    }
                }
            }
            return 1;
        }
    }

    public static class jj extends 解救 {
    }

    public static boolean 禁止传送(int a) {
        switch (a) {
            case 104000400:
            case 910010300:
            case 910000000:
                return true;
            default:
                return false;
        }
    }

    public static class 解救 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getMapId() >= 100000000) {
                if (!禁止传送(c.getPlayer().getMapId())) {
                    c.getPlayer().changeMap(100000000, 0);
                } else {
                    c.getPlayer().dropMessage(5, "此处无法使用。");
                }
            } else {
                c.getPlayer().dropMessage(5, "此处无法使用。");
            }
            return 1;
        }
    }

    public static class jk extends 解卡 {
    }

    public static class 解卡 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用解卡功能"));
            return 1;
        }
    }

    public static class cd extends 存档 {
    }

    public static class 存档 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().saveToDB(false, false);
            c.getPlayer().dropMessage(5, "当前时间是 " + FileoutputUtil.CurrentReadable_Time() + " ，角色信息存档成功了");
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用个人存档功能"));
            return 1;
        }
    }

    public static class zs extends 自杀 {
    }

    public static class 自杀 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setHp(0);
            c.getPlayer().updateSingleStat(MapleStat.HP, 0);
            c.getPlayer().dropMessage(5, "自杀成功");
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用自杀功能"));

            return 1;
        }
    }

    public static class fh extends 复活 {
    }

    public static class 复活 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getMapId() != 100000203) {
                if (c.getPlayer().getBossLog("复活") < 2) {
                    c.getPlayer().setBossLog("复活");
                    c.getPlayer().setHp(1);
                    c.getPlayer().updateSingleStat(MapleStat.HP, 1);
                    c.getPlayer().dropMessage(5, "复活成功");
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用复活功能"));
                } else {
                    c.getPlayer().dropMessage(5, "今日复活次数已经用完了， " + c.getPlayer().getBossLog("复活") + "次");
                }
            } else {
                c.getPlayer().dropMessage(5, "该地图无法使用复活。");
            }
            return 1;
        }
    }

    public static class gwxl extends 怪物血量 {
    }

    public static class 怪物血量 extends CommandExecute {//mob

        public int execute(MapleClient c, String[] splitted) {
            MapleMonster monster = null;
            for (final MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER))) {
                monster = (MapleMonster) monstermo;
                if (monster.isAlive()) {
                    if (c.getPlayer().getMapId() >= 190000000 && c.getPlayer().getMapId() <= 190000002) {
                        c.getPlayer().dropMessage(5, "[异界怪物]:" + monster.toString2());
                    } else {
                        c.getPlayer().dropMessage(6, "[怪物]:" + monster.toString2());
                    }
                }
            }
            if (monster == null) {
                c.getPlayer().dropMessage(6, "你附近没有怪物。");
            }
            return 1;
        }
    }

    public static class 对话 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted[1] == null) {
                c.getPlayer().dropMessage(6, "@对话+空格+<输入要说的话>");
                return 1;
            }
            if (c.getPlayer().isGM()) {
                c.getPlayer().dropMessage(6, "因为你自己是管理，无法使用此命令,可以尝试!cngm <讯息> 建立GM聊天頻道~");
                return 1;
            }
            if (!c.getPlayer().getCheatTracker().GMSpam(100000, 1)) { // 5 minutes.
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "頻道 " + c.getPlayer().getClient().getChannel() + " 玩家 [" + c.getPlayer().getName() + "] : " + StringUtil.joinStringFrom(splitted, 1)));
                c.getPlayer().dropMessage(6, "讯息已经发给了在线管理员!");
            } else {
                c.getPlayer().dropMessage(6, "为了防止对管理员刷屏所以每1分钟只能发一次.");
            }
            return 1;
        }
    }

    public static class 作者 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.sendPacket(MaplePacketCreator.openWeb("http://wpa.qq.com/msgrd?v=3&uin=" + 作者QQ + "&site=qq&menu=yes"));
            return 1;
        }
    }

    public static class gwbw extends 怪物爆物 {
    }

    public static class 怪物爆物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            NPCScriptManager npc = NPCScriptManager.getInstance();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用怪物爆物查询功能"));
            npc.start(c, 2000, 1);
            return 1;
        }
    }

    public static class qjbw extends 全局爆物 {
    }

    public static class 全局爆物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            NPCScriptManager npc = NPCScriptManager.getInstance();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用全局爆物查询功能"));
            npc.start(c, 2000, 2);
            return 1;
        }
    }

    public static class yxbl extends 游戏倍率 {
    }

    public static class 游戏倍率 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {

            if ("开".equals(ServerProperties.getProperty("ZEV.ZEV"))) {
                if (c.getChannel() == 1 || c.getChannel() == 2 || c.getChannel() == 3 || c.getChannel() == 4 || c.getChannel() == 5) {
                    c.getPlayer().dropMessage(1, ""
                            + "《" + MapleParty.开服名字 + "游戏倍率》\r\n\r\n"
                            + "经验倍率；" + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp")) + "\r\n"
                            + "金币倍率；" + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso")) + "\r\n"
                            + "爆物倍率；" + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop")) + "\r\n\r\n"
                            + "[勇者区域]"
                    );
                } else {
                    c.getPlayer().dropMessage(1, ""
                            + "《" + MapleParty.开服名字 + "游戏倍率》\r\n\r\n"
                            + "经验倍率；" + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp2")) + "\r\n"
                            + "金币倍率；" + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso2")) + "\r\n"
                            + "爆物倍率；" + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop2")) + "\r\n\r\n"
                            + "[萌新区域]"
                    );

                }
            } else {
                c.getPlayer().dropMessage(1, ""
                        + "《" + MapleParty.开服名字 + "游戏倍率》\r\n\r\n"
                        + "经验倍率；" + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp")) + "\r\n"
                        + "金币倍率；" + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso")) + "\r\n"
                        + "爆物倍率；" + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop")) + "\r\n\r\n"
                        + "基于游戏服务端的基础倍率"
                );
            }
            return 1;
        }
    }

    public static class jsbl extends 角色倍率 {
    }

    public static class 角色倍率 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(1, ""
                    + "《" + MapleParty.开服名字 + "角色当前倍率》\r\n\r\n"
                    + "经验倍率；" + (Math.round(c.getPlayer().getEXPMod()) * 100) * Math.round(c.getPlayer().getStat().expBuff / 100.0) + "%\r\n"
                    + "怪物倍率；" + (Math.round(c.getPlayer().getDropMod()) * 100) * Math.round(c.getPlayer().getStat().dropBuff / 100.0) + "%\r\n"
                    + "金币倍率；" + Math.round(c.getPlayer().getStat().mesoBuff / 100.0) * 100 + "%\r\n\r\n"
                    + "基于游戏服务端的基础倍率上，为角色加持的倍率加成,双倍卡，技能BUFF"
            );
            return 1;
        }
    }

    public static class zc extends 主城 {
    }

    public static class 主城 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (主城(c.getPlayer().getMapId())) {
                c.getPlayer().dropMessage(1, "所在地图是主城");
            } else {
                c.getPlayer().dropMessage(1, "所在地图不是主城");
            }
            return 1;
        }
    }

    public static class jkzd extends 解卡组队 {
    }

    public static class 解卡组队 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setParty(null);
            c.getPlayer().dropMessage(1, "解卡组队成功，请重新登陆游戏。");
            return 1;
        }
    }

    public static class jkjz extends 解卡家族 {
    }

    public static class 解卡家族 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            ResultSet rs = null;

            try {
                ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET (guildid = ?,guildrank = ?,allianceRank = ?)WHERE id = ?");
                ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM characters WHERE id = ?");
                ps1.setInt(1, c.getPlayer().getId());
                rs = ps1.executeQuery();
                if (rs.next()) {
                    String sqlString1 = null;
                    String sqlString2 = null;
                    String sqlString3 = null;
                    sqlString1 = "update characters set guildid='0' where id=" + c.getPlayer().getId() + ";";
                    PreparedStatement hair = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                    hair.executeUpdate(sqlString1);
                    sqlString2 = "update characters set guildrank='5' where id=" + c.getPlayer().getId() + ";";
                    PreparedStatement face = DatabaseConnection.getConnection().prepareStatement(sqlString2);
                    face.executeUpdate(sqlString2);
                    sqlString3 = "update characters set allianceRank='5' where id=" + c.getPlayer().getId() + ";";
                    PreparedStatement allianceRank = DatabaseConnection.getConnection().prepareStatement(sqlString3);
                    allianceRank.executeUpdate(sqlString3);
                    c.getPlayer().dropMessage(1, "解卡组队成功，请重新登陆游戏。");
                }
            } catch (SQLException ex) {

            }

            return 1;
        }
    }

    public static class csyc extends 测试延迟 {
    }

    public static class 测试延迟 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int 延迟 = c.getPlayer().getClient().getLatency();

            if (延迟 <= 1000) {
                c.getPlayer().dropMessage(1, "与服务器连接良好");
            }
            if (延迟 > 1000 && 延迟 <= 2000) {
                c.getPlayer().dropMessage(1, "与服务器连接一般");
            }
            if (延迟 > 2000 && 延迟 <= 3000) {
                c.getPlayer().dropMessage(1, "与服务器连接较差");
            }
            if (延迟 > 3000) {
                c.getPlayer().dropMessage(1, "与服务器连接很差");
            }
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用了测试延迟功能"));
            //c.getPlayer().dropMessage(1, "当前延迟 " + 延迟 + " 毫秒");
            return 1;
        }
    }

    public static class dths extends 地图回收 {
    }

    public static class 地图回收 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.sendPacket(MaplePacketCreator.enableActions());
            NPCScriptManager.getInstance().dispose(c);
            NPCScriptManager.getInstance().start(c, 9900004, 25);
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用了地图回收功能"));

            return 1;
        }
    }

    public static class wdwz extends 我的位置 {
    }

    public static class 我的位置 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().dropMessage(5, "地图: " + c.getPlayer().getMap().getMapName() + " ");
            c.getPlayer().dropMessage(5, "代码: " + c.getPlayer().getMap().getId() + " ");
            c.getPlayer().dropMessage(5, "坐标: " + String.valueOf(c.getPlayer().getPosition().x) + " , " + String.valueOf(c.getPlayer().getPosition().y) + "");

            return 1;
        }
    }

    public static class bz extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "<" + MapleParty.开服名字 + " 玩家指令>");
            c.getPlayer().dropMessage(5, "指令可为前一个字母，如 @bz = @帮助 ");
            c.getPlayer().dropMessage(5, "@存档     <保存当前数据>");
            c.getPlayer().dropMessage(5, "@解救     <返回射手村了>");
            c.getPlayer().dropMessage(5, "@解卡     <解除假死卡死>");
            c.getPlayer().dropMessage(5, "@自杀     <自杀自行了断>");
            c.getPlayer().dropMessage(5, "@主城     <地图是否主城>");
            c.getPlayer().dropMessage(5, "@测试延迟 <测试游戏延迟>");
            c.getPlayer().dropMessage(5, "@解卡组队 <解决无法组队>");
            c.getPlayer().dropMessage(5, "@解卡家族 <解决家族异常>");
            c.getPlayer().dropMessage(5, "@怪物爆物 <查询当前爆物>");
            c.getPlayer().dropMessage(5, "@全局爆物 <查询全局爆物>");
            c.getPlayer().dropMessage(5, "@怪物血量 <查询怪物血量>");
            c.getPlayer().dropMessage(5, "@游戏倍率 <查询游戏倍率>");
            c.getPlayer().dropMessage(5, "@角色倍率 <查询角色倍率>");
            c.getPlayer().dropMessage(5, "@我的位置 <查看地图位置>");
            c.getPlayer().dropMessage(5, "@拉回队友 <副本拉回掉线队友>");
            c.getPlayer().dropMessage(5, "@地图回收 <解决地图掉线卡顿>");
            //c.getPlayer().dropMessage(5, "@封号<空格>[玩家名字]<空格>[原因]");
            // c.getPlayer().dropMessage(5, "该封号请勿恶意使用，如果发现恶意使用者，将会进行删号处理，封号请截图为证据发送给管理员。");
            return 1;
        }
    }

    public static class 帮助 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "<" + MapleParty.开服名字 + " 玩家指令>");
            c.getPlayer().dropMessage(5, "指令可为前一个字母，如 @bz = @帮助 ");
            c.getPlayer().dropMessage(5, "@存档     <保存当前数据>");
            c.getPlayer().dropMessage(5, "@解救     <返回射手村了>");
            c.getPlayer().dropMessage(5, "@解卡     <解除假死卡死>");
            c.getPlayer().dropMessage(5, "@自杀     <自杀自行了断>");
            c.getPlayer().dropMessage(5, "@主城     <地图是否主城>");
            c.getPlayer().dropMessage(5, "@测试延迟 <测试游戏延迟>");
            c.getPlayer().dropMessage(5, "@解卡家族 <解决家族异常>");
            c.getPlayer().dropMessage(5, "@解卡组队 <解决无法组队>");
            c.getPlayer().dropMessage(5, "@怪物爆物 <查询当前爆物>");
            c.getPlayer().dropMessage(5, "@全局爆物 <查询全局爆物>");
            c.getPlayer().dropMessage(5, "@怪物血量 <查询怪物血量>");
            c.getPlayer().dropMessage(5, "@游戏倍率 <查询游戏倍率>");
            c.getPlayer().dropMessage(5, "@角色倍率 <查询角色倍率>");
            c.getPlayer().dropMessage(5, "@我的位置 <查看地图位置>");
            c.getPlayer().dropMessage(5, "@拉回队友 <副本拉回掉线队友>");
            c.getPlayer().dropMessage(5, "@地图回收 <解决地图掉线卡顿>");
            // c.getPlayer().dropMessage(5, "@封号<空格>[玩家名字]<空格>[原因]");
            //c.getPlayer().dropMessage(5, "该封号请勿恶意使用，如果发现恶意使用者，将会进行删号处理，封号请截图为证据发送给管理员。");

            return 1;
        }
    }

    public static class ZEV extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            DebugWindow debug = new DebugWindow();
            debug.setC(c);
            debug.setVisible(true);
            return 1;
        }
    }

}
