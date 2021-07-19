package client.messages.commands;

import client.MapleCharacter;
import client.MapleClient;
import constants.ServerConstants;
import handling.channel.ChannelServer;
import handling.world.World;
import scripting.NPCScriptManager;
import server.maps.MapleMap;
import tools.MaplePacketCreator;
import tools.StringUtil;

import static gui.QQMsgServer.sendMsgToQQGroup;

/*
1.跟踪
2.封号
3.隐身开/关
4.无敌
 */
public class 巡查管理 {//管理权限1

    public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
        return ServerConstants.PlayerGMRank.巡查管理;
    }

    public static class 拍卖 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.sendPacket(MaplePacketCreator.enableActions());
            NPCScriptManager.getInstance().dispose(c);
            NPCScriptManager.getInstance().start(c, 9900004, 0);
            return 1;
        }
    }

    public static class 回收地图 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int 地图 = c.getPlayer().getMapId();
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
            return 1;
        }
    }

    public static class 跟踪 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (splitted.length == 2) {
                    c.getPlayer().changeMap(victim.getMap(), victim.getMap().findClosestSpawnpoint(victim.getPosition()));
                } else {
                    MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(Integer.parseInt(splitted[2]));
                    victim.changeMap(target, target.getPortal(0));
                }
            } else {
                try {
                    victim = c.getPlayer();
                    int ch = World.Find.findChannel(splitted[1]);
                    if (ch < 0) {
                        MapleMap target = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
                        c.getPlayer().changeMap(target, target.getPortal(0));
                    } else {
                        victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
                        c.getPlayer().dropMessage(6, "正在换频道,请等待.");
                        if (victim.getMapId() != c.getPlayer().getMapId()) {
                            final MapleMap mapp = c.getChannelServer().getMapFactory().getMap(victim.getMapId());
                            c.getPlayer().changeMap(mapp, mapp.getPortal(0));
                        }
                        c.getPlayer().changeChannel(ch);
                    }
                } catch (Exception e) {
                    c.getPlayer().dropMessage(6, "该玩家不在线 " + e.getMessage());
                    return 0;
                }
            }
            return 1;
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

    public static class 封号 extends CommandExecute {

        protected boolean hellban = false;

        private String getCommand() {
            return "封号";
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(5, "[封号] *" + getCommand() + " <空格>[玩家名字]<空格>[原因]");
                return 0;
            }
            int ch = World.Find.findChannel(splitted[1]);

            StringBuilder sb = new StringBuilder(c.getPlayer().getName());
            sb.append(" banned ").append(splitted[1]).append(": ").append(StringUtil.joinStringFrom(splitted, 2));
            MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
            if (target == null || ch < 1) {
                if (MapleCharacter.ban(splitted[1], sb.toString(), false, c.getPlayer().isAdmin() ? 250 : c.getPlayer().getGMLevel(), splitted[0].equals("*封号"))) {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 成功离线封锁 " + splitted[1] + ".");

                    return 1;
                } else {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 封锁失败 " + splitted[1]);
                    return 0;
                }
            }

            if (c.getPlayer().getGMLevel() > target.getGMLevel()) {
                sb.append(" (IP: ").append(target.getClient().getSessionIPAddress()).append(")");
                if (target.ban(sb.toString(), c.getPlayer().isAdmin(), false, hellban)) {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 成功封锁 " + splitted[1] + ".");
                    String 信息 = "[系统提醒]" + target.getName() + " 因为使用非法插件/破坏游戏平衡，而被永久封号。";
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                    sendMsgToQQGroup(信息);
                    return 1;
                } else {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 封锁失败");
                    return 0;
                }
            } else {
                c.getPlayer().dropMessage(6, "[" + getCommand() + "] 不能封锁GM");
                return 1;
            }
        }
    }

    public static class 断线 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().disconnectAll();
            String msg = "[断线指令] 管理员 " + c.getPlayer().getName() + "  切断了地图玩家";
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
            return 1;
        }
    }
}
