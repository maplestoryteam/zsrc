package client.messages.commands.a;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.messages.commands.CommandExecute;
import handling.channel.ChannelServer;
import handling.world.World;
import server.maps.MapleMap;
import tools.StringUtil;

import java.util.Collection;

/**
 *
 * @author 管理权限2
 */
public class GMCommand {

    /*public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.GM;
    }*/
    public static class 拉 extends WarpHere {
    }

    // public static class 等级 extends Level {
    // }
    // public static class 转职 extends Job {
    // }
    // public static class 清空 extends ClearInv {
    //}
    // public static class 踢人 extends DC {
    // }
    // public static class 读取玩家 extends spy {
    //  }
    //  public static class 在线人数 extends online {
    // }
    // public static class 在线玩家 extends onlinecounts {
    // }
    // public static class 解除封号 extends UnBan {
    // }
    // public static class 刷钱 extends GainMeso {
    // }
    public static class WarpHere extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().findClosestSpawnpoint(c.getPlayer().getPosition()));
            } else {
                int ch = World.Find.findChannel(splitted[1]);
                if (ch < 0) {
                    c.getPlayer().dropMessage(5, "角色不在线");
                    return 1;
                } else {
                    victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
                    c.getPlayer().dropMessage(5, "正在传送玩家到身边");
                    victim.dropMessage(5, "管理员正在传送你");
                    if (victim.getMapId() != c.getPlayer().getMapId()) {
                        final MapleMap mapp = victim.getClient().getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId());
                        victim.changeMap(mapp, mapp.getPortal(0));
                    }
                    victim.changeChannel(c.getChannel());
                }
            }
            return 1;
        }
    }

    public static class 封号 extends CommandExecute {

        protected boolean hellban = false;

        private String getCommand() {
            return "UnBan";
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "[Syntax] !" + getCommand() + " <原因>");

                return 0;
            }
            byte ret;
            if (hellban) {
                ret = MapleClient.unHellban(splitted[1]);
            } else {
                ret = MapleClient.unban(splitted[1]);
            }
            if (ret == -2) {
                c.getPlayer().dropMessage(6, "[" + getCommand() + "] SQL error.");
                return 0;
            } else if (ret == -1) {
                c.getPlayer().dropMessage(6, "[" + getCommand() + "] The character does not exist.");
                return 0;
            } else {
                c.getPlayer().dropMessage(6, "[" + getCommand() + "] Successfully unbanned!");

            }
            byte ret_ = MapleClient.unbanIPMacs(splitted[1]);
            if (ret_ == -2) {
                c.getPlayer().dropMessage(6, "[UnbanIP] SQL error.");
            } else if (ret_ == -1) {
                c.getPlayer().dropMessage(6, "[UnbanIP] The character does not exist.");
            } else if (ret_ == 0) {
                c.getPlayer().dropMessage(6, "[UnbanIP] No IP or Mac with that character exists!");
            } else if (ret_ == 1) {
                c.getPlayer().dropMessage(6, "[UnbanIP] IP/Mac -- one of them was found and unbanned.");
            } else if (ret_ == 2) {
                c.getPlayer().dropMessage(6, "[UnbanIP] Both IP and Macs were unbanned.");
            }
            return ret_ > 0 ? 1 : 0;
        }
    }

    public static class 解卡角色 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            ChannelServer.forceRemovePlayerByCharName(splitted[1]);
            c.getPlayer().dropMessage("解除卡号卡角成功");
            return 1;
        }
    }

    public static class 转职 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().changeJob(Integer.parseInt(splitted[1]));
            return 1;
        }
    }

    /* public static class GainMeso extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().gainMeso(Integer.MAX_VALUE - c.getPlayer().getMeso(), true);
            return 1;
        }
    }*/
    public static class 等级 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setLevel(Short.parseShort(splitted[1]));
            c.getPlayer().levelUp();
            if (c.getPlayer().getExp() < 0) {
                c.getPlayer().gainExp(-c.getPlayer().getExp(), false, false, true);
            }
            return 1;
        }
    }

    public static class 查看角色 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "使用规则: !spy <玩家名字>");
            } else {
                MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim.getGMLevel() > c.getPlayer().getGMLevel() && c.getPlayer().getId() != victim.getId()) {
                    c.getPlayer().dropMessage(5, "你不能查看比你高权限的人!");
                    return 0;
                }
                if (victim != null) {
                    c.getPlayer().dropMessage(5, "此玩家(" + victim.getId() + ")状态:");
                    c.getPlayer().dropMessage(5, "等級: " + victim.getLevel() + "职业: " + victim.getJob() + "名声: " + victim.getFame());
                    c.getPlayer().dropMessage(5, "地图: " + victim.getMapId() + " - " + victim.getMap().getMapName());
                    c.getPlayer().dropMessage(5, "力量: " + victim.getStat().getStr() + "  ||  敏捷: " + victim.getStat().getDex() + "  ||  智力: " + victim.getStat().getInt() + "  ||  运气: " + victim.getStat().getLuk());
                    c.getPlayer().dropMessage(5, "拥有 " + victim.getMeso() + " 金币.");
                } else {
                    c.getPlayer().dropMessage(5, "找不到此玩家.");
                }
            }
            return 1;
        }
    }

    public static class 在线人数 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int total = 0;
            int curConnected = c.getChannelServer().getConnectedClients();
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            c.getPlayer().dropMessage(6, new StringBuilder().append("頻道: ").append(c.getChannelServer().getChannel()).append(" 线上人数: ").append(curConnected).toString());
            total += curConnected;
            for (MapleCharacter chr : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                if (chr != null && c.getPlayer().getGMLevel() >= chr.getGMLevel()) {
                    StringBuilder ret = new StringBuilder();
                    ret.append(" 角色名称 ");
                    ret.append(StringUtil.getRightPaddedStr(chr.getName(), ' ', 15));
                    ret.append(" ID: ");
                    ret.append(StringUtil.getRightPaddedStr(chr.getId() + "", ' ', 4));
                    ret.append(" 等级: ");
                    ret.append(StringUtil.getRightPaddedStr(String.valueOf(chr.getLevel()), ' ', 4));
                    ret.append(" 职业: ");
                    ret.append(chr.getJob());
                    if (chr.getMap() != null) {
                        ret.append(" 地图: ");
                        ret.append(chr.getMapId());
                        ret.append("(").append(chr.getMap().getMapName()).append(")");
                        c.getPlayer().dropMessage(6, ret.toString());
                    }
                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("当前频道总计在线人数: ").append(total).toString());
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            int channelOnline = c.getChannelServer().getConnectedClients();
            int totalOnline = 0;
            /*
             * 伺服器總人數
             */
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                totalOnline += cserv.getConnectedClients();
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("当前服务器总计在线人数: ").append(totalOnline).append("个").toString());
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            return 1;
        }
    }

    public static class 在线玩家 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage("在线人物: ");
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                c.getPlayer().dropMessage("[频道 " + cs.getChannel() + "]");
                StringBuilder sb = new StringBuilder();
                Collection<MapleCharacter> cmc = cs.getPlayerStorage().getAllCharacters();
                for (MapleCharacter chr : cmc) {
                    if (sb.length() > 150) {
                        sb.setLength(sb.length() - 2);
                        c.getPlayer().dropMessage(sb.toString());
                        sb = new StringBuilder();
                    }
                    if (!chr.isGM()) {
                        sb.append(MapleCharacterUtil.makeMapleReadable(" ID:" + chr.getId() + " 角色名字:" + chr.getName() + " 等级: " + chr.getLevel() + " 地图: " + chr.getMapId() + " " + chr.getMap().getMapName() + "\r\n"));
                    }
                }
                if (sb.length() >= 2) {
                    sb.setLength(sb.length() - 2);
                    c.getPlayer().dropMessage(sb.toString());
                }
            }
            return 1;
        }
    }

    /*  public static class ClearInv extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            java.util.Map<Pair<Short, Short>, MapleInventoryType> eqs = new ArrayMap<Pair<Short, Short>, MapleInventoryType>();
            if (splitted[1].equals("全部")) {
                for (MapleInventoryType type : MapleInventoryType.values()) {
                    for (IItem item : c.getPlayer().getInventory(type)) {
                        eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), type);
                    }
                }
            } else if (splitted[1].equals("已装备道具")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.EQUIPPED);
                }
            } else if (splitted[1].equals("武器")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.EQUIP)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.EQUIP);
                }
            } else if (splitted[1].equals("消耗")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.USE)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.USE);
                }
            } else if (splitted[1].equals("装饰")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.SETUP)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.SETUP);
                }
            } else if (splitted[1].equals("其他")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.ETC);
                }
            } else if (splitted[1].equals("特殊")) {
                for (IItem item : c.getPlayer().getInventory(MapleInventoryType.CASH)) {
                    eqs.put(new Pair<Short, Short>(item.getPosition(), item.getQuantity()), MapleInventoryType.CASH);
                }
            } else {
                c.getPlayer().dropMessage(6, "[全部/已装备道具/武器/消耗/装饰/其他/特殊]");
            }
            for (Entry<Pair<Short, Short>, MapleInventoryType> eq : eqs.entrySet()) {
                MapleInventoryManipulator.removeFromSlot(c, eq.getValue(), eq.getKey().left, eq.getKey().right, false, false);
            }
            return 1;
        }
    }
    
        public static class 给当前地图点卷 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "用法: !给当前地图点卷 [点卷类型1：点卷 - 2：抵用] [点卷数量]");
                return 0;
            }
            int type = Integer.parseInt(splitted[1]);
            int quantity = Integer.parseInt(splitted[2]);
            if ((type <= 0) || (type > 2)) {
                type = 2;
            }
            if (quantity > 9000) {
                quantity = 9000;
            }
            int ret = 0;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    if (c.getPlayer().getMapId() == mch.getMapId()) {
                        mch.modifyCSPoints(type, quantity, false);
                        mch.dropMessage(-11, new StringBuilder().append("[系统提示] 恭喜您获得管理员赠送给您的").append(type == 1 ? "点券 " : " 抵用券 ").append(quantity).append(" 点.").toString());
                        ret++;
                    }
                }
            }
            if (type == 1) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        if (c.getPlayer().getMapId() == mch.getMapId()) {
                            mch.startMapEffect(ServerProperties.getProperty("ZEV.CommandMessage") + "管理员发放" + quantity + "点卷给当前地图在线的所有玩家！快感谢管理员吧！", 5120027);
                        }
                    }
                }
            } else if (type == 2) {
                for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                    for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                        if (c.getPlayer().getMapId() == mch.getMapId()) {
                            mch.startMapEffect(ServerProperties.getProperty("ZEV.CommandMessage") + "管理员发放" + quantity + "抵用卷给当前地图在线的所有玩家！快感谢管理员吧！", 5120027);
                        }
                    }
                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(quantity).append(" 点的").append(type == 1 ? "点券 " : " 抵用券 ").append(" 总计: ").append(ret * quantity).toString());
            return 1;
        }
    }

    public static class 给当前地图经验 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            int quantity = Integer.parseInt(splitted[1]);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    //     mch.gainMeso(quantity, true);
                    if (c.getPlayer().getMapId() == mch.getMapId()) {
                        mch.gainExp(quantity, true, false, true);
                    }
                }
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (c.getPlayer().getMapId() == mch.getMapId()) {
                        mch.startMapEffect(ServerProperties.getProperty("ZEV.CommandMessage") + "管理员发放" + quantity + "经验给当前地图在线的所有玩家！快感谢管理员吧！", 5120027);
                    }
                }
            }
            return 1;
        }
    }

    public static class 给当前地图冒险币 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            int quantity = Integer.parseInt(splitted[1]);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    if (c.getPlayer().getMapId() == mch.getMapId()) {
                        mch.gainMeso(quantity, true);
                    }
                }
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (c.getPlayer().getMapId() == mch.getMapId()) {
                        mch.startMapEffect(ServerProperties.getProperty("ZEV.CommandMessage") + "管理员发放" + quantity + "冒险币给当前地图在线的所有玩家！快感谢管理员吧！", 5120027);
                    }

                }
            }
            return 1;
        }
    }

    public static class 给当前地图物品 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "用法: !给当前地图物品 [物品ID] [数量]");
                return 0;
            }
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int item = Integer.parseInt(splitted[1]);
            int quantity = Integer.parseInt(splitted[2]);
            String mz = ii.getName(item);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    if (c.getPlayer().getMapId() == mch.getMapId()) {
                        MapleInventoryManipulator.addById(mch.getClient(), item, (short) quantity, (byte) 0);
                    }
                }
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (quantity <= 1) {
                        if (c.getPlayer().getMapId() == mch.getMapId()) {
                            mch.startMapEffect(ServerProperties.getProperty("ZEV.CommandMessage") + "管理员发放【" + mz + "】物品给当前地图在线的所有玩家！快感谢管理员吧！", 5120027);
                        }

                    } else {
                        if (c.getPlayer().getMapId() == mch.getMapId()) {
                            mch.startMapEffect(ServerProperties.getProperty("ZEV.CommandMessage") + "管理员发放【" + mz + "】物品【" + quantity + "】个给当前地图在线的所有玩家！快感谢管理员吧！", 5120027);
                        }

                    }
                }
            }
            return 1;
        }
    }
    
        public static class 总在线人数 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            Map connected = World.getConnected();
            StringBuilder conStr = new StringBuilder("连接数量: ");
            boolean first = true;
            for (Iterator i$ = connected.keySet().iterator(); i$.hasNext();) {
                int i = ((Integer) i$.next()).intValue();
                if (!first) {
                    conStr.append(", ");
                } else {
                    first = false;
                }
                if (i == 0) {
                    conStr.append("总计: ");
                    conStr.append(connected.get(Integer.valueOf(i)));
                } else {
                    conStr.append("频道 ");
                    conStr.append(i);
                    conStr.append(": ");
                    conStr.append(connected.get(Integer.valueOf(i)));
                }
            }
            c.getPlayer().dropMessage(conStr.toString());
            return 1;
        }
    }
        
     public static class 临时NPC extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int npcId = Integer.parseInt(splitted[1]);
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
                c.getPlayer().dropMessage(6, "You have entered an invalid NPC ID");
                return 0;
            }
            return 1;
        }
    }
     
    public static class 永久NPC extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 1) {
                c.getPlayer().dropMessage(6, "!pnpc [npcid]");
                return 0;
            }
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
                    Connection con = (Connection) DatabaseConnection.getConnection();
                    try (PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO spawns (idd, f, hide, fh, cy, rx0, rx1, type, x, y, mid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
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
                    c.getPlayer().dropMessage(6, "Failed to save NPC to the database");
                }
                c.getPlayer().getMap().addMapObject(npc);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
                c.getPlayer().dropMessage(6, "Please do not reload this map or else the NPC will disappear till the next restart.");
            } else {
                c.getPlayer().dropMessage(6, "You have entered an invalid Npc-Id");
                return 0;
            }
            return 1;
        }
    }
    
    public static class 给所有人物品 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, "用法: !给所有人物品 [物品ID] [数量]");
                return 0;
            }
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int item = Integer.parseInt(splitted[1]);
            int quantity = Integer.parseInt(splitted[2]);
            String mz = ii.getName(item);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    MapleInventoryManipulator.addById(mch.getClient(), item, (short) quantity, (byte) 0);
                }
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    if (quantity <= 1) {
                          mch.startMapEffect(ServerProperties.getProperty("ZEV.CommandMessage")+"管理员发放【" + mz + "】物品给在线的所有玩家！快感谢管理员吧！", 5120027);
                       
                    } else {
                          mch.startMapEffect(ServerProperties.getProperty("ZEV.CommandMessage")+"管理员发放【" + mz + "】物品【" + quantity + "】个给在线的所有玩家！快感谢管理员吧！", 5120027);
                       
                    }
                }
            }
            return 1;
        }
    }
    
    public static class 给玩家物品 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, "用法: !给玩家物品 [角色名字][物品ID] [数量]");
                return 0;
            }
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            String name = splitted[1];
            int item = Integer.parseInt(splitted[2]);
            int quantity = Integer.parseInt(splitted[3]);
            String mz = ii.getName(item);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    if (mch.getName().equals(name)) {
                        MapleInventoryManipulator.addById(mch.getClient(), item, (short) quantity, (byte) 0);
                        c.getPlayer().dropMessage(6, "给予成功！");
                    }
                }
            }
            return 1;
        }
    }
    
    public static class 断开玩家连接 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[(splitted.length - 1)]);
            if ((victim != null) && (c.getPlayer().getGMLevel() >= victim.getGMLevel())) {
                victim.getClient().getSession().close();
                victim.getClient().disconnect(true, false);
                c.getPlayer().dropMessage(6, "已经成功断开 " + victim.getName() + " 的连接.");
                return 1;
            }
            c.getPlayer().dropMessage(6, "使用的对象不存在或者角色名字错误或者对放的GM权限比你高.");
            return 0;
        }
    }
    
  public static class 隐身模式 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            SkillFactory.getSkill(9001004).getEffect(1).applyTo(c.getPlayer());
            c.getPlayer().dropMessage(6, "隐身模式已开启.");
            return 0;
        }
    }
  
    public static class 开启活动副本 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {

            if (splitted.length < 2 ) {
                c.getPlayer().dropMessage(6, "用法: !开启活动副本 <副本频道> <类型1-6>");
                return 0;
            }
            int channel = Integer.parseInt(splitted[1]);
            int lx = Integer.parseInt(splitted[2]);
            final EventManager em = c.getChannelServer().getEventSM().getEventManager("AutomatedEvent");
            if (em != null) {
                em.scheduleRandomEventInChannel(c.getPlayer(), channel, lx);
            }
            return 1;
        }
    }*/
}
