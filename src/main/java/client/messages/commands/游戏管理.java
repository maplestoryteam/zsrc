package client.messages.commands;

import static abc.Game.人气设置;
import static abc.Game.传送;
import static abc.Game.刷;
import static abc.Game.刷技能点;
import static abc.Game.刷新;
import static abc.Game.刷能力点;
import static abc.Game.召唤怪物;
import static abc.Game.吸怪;
import static abc.Game.我的位置;
import static abc.Game.无敌;
import static abc.Game.清怪;
import static abc.Game.清怪2;
import static abc.Game.清物;
import client.ISkill;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import constants.ServerConstants.PlayerGMRank;
import client.MapleClient;
import client.MapleStat;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.messages.CommandProcessorUtil;
import constants.GameConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.world.World;
import java.awt.Point;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleShopFactory;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleNPC;
import server.life.OverrideMonsterStats;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.packet.MobPacket;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import scripting.NPCScriptManager;
import server.Randomizer;
import server.ServerProperties;
import tools.*;
import static tools.FileoutputUtil.CurrentReadable_Time;
import tools.packet.UIPacket;
import static abc.Game.等级;
import client.DebugWindow;
import handling.world.MapleParty;
import java.text.DateFormat;
import java.util.Calendar;
import static gui.QQMsgServer.sendMsgToQQGroup;
import static gui.Start.CashShopServer;
import server.life.PlayerNPC;
import server.quest.MapleQuest;
import static gui.进阶BOSS.进阶BOSS线程.开启进阶BOSS线程;

/**
 *
 * @author 游戏管理员
 *
 *
 */
public class 游戏管理 {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.游戏管理;
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

    public static class 重置任务 extends CommandExecute {///////resetquest

        public int execute(MapleClient c, String splitted[]) {
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

        public int execute(MapleClient c, String splitted[]) {
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

        public int execute(MapleClient c, String splitted[]) {
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

    public static class 分身 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().cloneLook();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!克隆 - 产生克隆体").toString();
        }
    }

    public static class 克隆 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (c.getPlayer().getCloneSize() == 0) {
                c.getPlayer().cloneLook2();
            } else {
                c.getPlayer().disposeClones();
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!克隆 - 产生克隆体").toString();
        }
    }

    public static class 取消克隆 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().dropMessage(6, c.getPlayer().getCloneSize() + "个克隆体消失了.");
            c.getPlayer().disposeClones();
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!取消克隆 - 摧毁克隆体").toString();
        }
    }

    public static class 大逃杀活动测试开启 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            MapleParty.大逃杀活动++;
            return 1;
        }

    }

    public static class 大逃杀活动测试结束 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            MapleParty.大逃杀活动 = 0;
            return 1;
        }

    }

    public static class 测试 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            开启进阶BOSS线程();
            /*  MobSkill mobSkill = MobSkillFactory.getMobSkill(129, 3);
            MapleDisease disease = null;
            disease = MapleDisease.getBySkill(129);
            c.getPlayer().giveDebuff(disease, mobSkill);
             */
            // c.getPlayer().getClient().getSession().close();
            return 1;
        }

    }

    /*if (splitted[1].equalsIgnoreCase("SEAL")) {
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
    
     */
    public static class 玩家NPC extends CommandExecute {/////////MakePNPC

        public int execute(MapleClient c, String splitted[]) {
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

    public static class 测试2 extends CommandExecute {///////////Letter

        public int execute(MapleClient c, String splitted[]) {
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

    public static class 发福利了 extends CommandExecute {///////////Letter

        public int execute(MapleClient c, String splitted[]) {
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
            return new StringBuilder().append(" !letter <color (green/red)> <word> ").toString();
        }
    }

    public static class 测试1 extends CommandExecute {///////////Letter

        public int execute(MapleClient c, String splitted[]) {
            client.inventory.Item item = new client.inventory.Item(2000005, (byte) 0, (short) 1);
            c.getPlayer().getMap().物品掉落(c.getPlayer(), c.getPlayer(), item, new Point(c.getPlayer().getPosition().x, c.getPlayer().getPosition().y + 1000), false, false);
            return 1;
        }
    }

    public static class 解封 extends CommandExecute {

        protected boolean hellban = false;

        private String getCommand() {
            return "Ban";
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(5, "[解封] !" + getCommand() + " <玩家> <原因>");
                return 0;
            }
            int ch = World.Find.findChannel(splitted[1]);
            StringBuilder sb = new StringBuilder(c.getPlayer().getName());
            sb.append(" banned ").append(splitted[1]).append(": ").append(StringUtil.joinStringFrom(splitted, 0));
            MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
            return 1;
        }
    }

    public static class 等级 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (等级 == "关") {
                return 1;
            }
            c.getPlayer().setLevel(Short.parseShort(splitted[1]));
            c.getPlayer().levelUp();
            if (c.getPlayer().getExp() < 0) {
                c.getPlayer().gainExp(-c.getPlayer().getExp(), false, false, true);
            }
            return 1;
        }
    }

    public static class 升级 extends CommandExecute {//LevelUp

        public int execute(MapleClient c, String splitted[]) {
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
            if (c.getPlayer().getLevel() < 200) {
                c.getPlayer().gainExp(GameConstants.getExpNeededForLevel(c.getPlayer().getLevel()) + 1, true, false, true);
            }
            return 1;
        }

    }

    public static class 人气设置 extends CommandExecute {////////////Fame

        public int execute(MapleClient c, String splitted[]) {
            if (人气设置 == "关") {
                return 1;
            }
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
            return new StringBuilder().append("*人气设置+<角色名称> <名声> ...  - 名声").toString();
        }
    }

    public static class 无敌 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (无敌 == "关") {
                return 1;
            }
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
            return new StringBuilder().append("*无敌  - 无敌开关").toString();
        }
    }

    public static class 刷技能点 extends CommandExecute {//SP

        public int execute(MapleClient c, String splitted[]) {
            if (刷技能点 == "关") {
                return 1;
            }
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

        public int execute(MapleClient c, String splitted[]) {
            if (刷能力点 == "关") {
                return 1;
            }
            c.getPlayer().setRemainingAp((short) CommandProcessorUtil.getOptionalIntArg(splitted, 1, 1));
            c.getPlayer().updateSingleStat(MapleStat.AVAILABLEAP, CommandProcessorUtil.getOptionalIntArg(splitted, 1, 1));
            String msg = "[管理员信息][" + c.getPlayer().getName() + "] 使用指令增加AP " + splitted + "点";
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!刷能力点 [数量] - 增加AP").toString();
        }
    }

    public static class 刷 extends CommandExecute {//Item

        public int execute(MapleClient c, String splitted[]) {
            if (刷 == "关") {
                return 1;
            }
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
                if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]刷出了物品: " + MapleItemInformationProvider.getInstance().getName(Integer.parseInt(splitted[1])) + " [" + splitted[1] + "]"));
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!刷 <道具ID> - 刷出道具").toString();
        }
    }

    public static class 吸怪 extends CommandExecute {////////////MobVac

        public int execute(MapleClient c, String splitted[]) {
            if (吸怪 == "关") {
                return 1;
            }
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

    public static class 刷新 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (刷新 == "关") {
                return 1;
            }
            MapleCharacter player = c.getPlayer();
            c.sendPacket(MaplePacketCreator.getCharInfo(player));
            player.getMap().removePlayer(player);
            player.getMap().addPlayer(player);
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了刷新指令"));
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!刷新 - 假登出再登入").toString();

        }
    }

    public static class 清物 extends CommandExecute {//RemoveDrops

        public int execute(MapleClient c, String splitted[]) {
            if (清物 == "关") {
                return 1;
            }
            c.getPlayer().dropMessage(5, "清除了地图上 " + c.getPlayer().getMap().getNumItems() + " 个掉落物");
            c.getPlayer().getMap().removeDrops();
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了清理地上物品指令"));
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!清物 - 移除地上的物品").toString();

        }
    }

    public static class 清怪 extends CommandExecute {//KillAll

        public int execute(MapleClient c, String splitted[]) {
            if (清怪 == "关") {
                return 1;
            }
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
                map.killMonster(mob, c.getPlayer(), true, false, (byte) 1);
            }
            c.getPlayer().dropMessage(5, "您总共杀了 " + monsters.size() + " 怪物");
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了清除怪物指令"));
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!清怪 [range] [mapid] - 杀掉所有怪物").toString();

        }
    }

    /* if (server.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了清除怪物指令"));
            }*/
    public static class 清怪2 extends CommandExecute {//KillAll

        public int execute(MapleClient c, String splitted[]) {
            if (清怪2 == "关") {
                return 1;
            }
            MapleMap map = c.getPlayer().getMap();
            double range = Double.POSITIVE_INFINITY;
            boolean drop = true;
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
                map.killMonster(mob, c.getPlayer(), true, false, (byte) 1);
            }
            return 1;
        }
    }

    public static class 我的位置 extends CommandExecute {//////MyPos

        public int execute(MapleClient c, String splitted[]) {
            if (我的位置 == "关") {
                return 1;
            }
            Point pos = c.getPlayer().getPosition();
            c.getPlayer().dropMessage(6, "X: " + pos.x + " | Y: " + pos.y + " | RX0: " + (pos.x + 50) + " | RX1: " + (pos.x - 50) + " | FH: " + c.getPlayer().getFH() + "| CY:" + pos.y);
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了我的位置指令"));
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*我的位置").toString();
        }
    }

    public static class 召唤怪物 extends CommandExecute {//Spawn

        public int execute(MapleClient c, String splitted[]) {
            if (召唤怪物 == "关") {
                return 1;
            }
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
                if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]召唤了怪物[" + splitted[1] + "]指令"));
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*召唤怪物 <怪物ID> <hp|exp|php||pexp = ?> - 召唤怪物").toString();

        }
    }

    public static class 传送 extends CommandExecute {//Map

        public int execute(MapleClient c, String splitted[]) {
            if (传送 == "关") {
                return 1;
            }
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
                        c.getPlayer().dropMessage(5, "传送点错误.");
                    } catch (NumberFormatException a) {
                    }
                }
                if (targetPortal == null) {
                    targetPortal = target.getPortal(0);
                }
                c.getPlayer().changeMap(target, targetPortal);
                if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]传送到地图[" + splitted[1] + "]"));
                }
            } catch (Exception e) {
                c.getPlayer().dropMessage(5, "Error: " + e.getMessage());
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*传送 <mapid|charname> [portal] - 传送到某地图/人").toString();
        }
    }

    public static class 新建NPC extends CommandExecute {

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
                if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了新建NPC指令"));
                }
            } else {
                c.getPlayer().dropMessage(6, "查无此 Npc ");
            }

            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("!永久npc - 建立永久NPC").toString();
        }
    }

    public static class 拉所有人 extends CommandExecute {//WarpAllHere

        public int execute(MapleClient c, String splitted[]) {
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
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了拉所有人指令"));
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*拉所有人 把所有玩家传送到这里").toString();
        }
    }

    public static class 满技能 extends CommandExecute {//maxSkills

        public int execute(MapleClient c, String splitted[]) {
            MapleCharacter player = c.getPlayer();
            player.maxSkills();
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了满技能指令"));
            }
            return 1;
        }
    }

    public static class 丢 extends CommandExecute {//Drop

        public int execute(MapleClient c, String splitted[]) {
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
                    toDrop = new client.inventory.Item(itemId, (byte) 0, (short) quantity, (byte) 0);
                }
                //toDrop.setOwner(c.getPlayer().getName());
                toDrop.setGMLog(c.getPlayer().getName());
                if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]丢出了物品: " + MapleItemInformationProvider.getInstance().getName(Integer.parseInt(splitted[1])) + " [" + splitted[1] + "]"));
                }
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
            }
            return 1;
        }
    }

    public static class 祝福 extends CommandExecute {//buff

        public int execute(MapleClient c, String splitted[]) {
            MapleCharacter player = c.getPlayer();
            SkillFactory.getSkill(9001002).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001003).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001008).getEffect(1).applyTo(player);
            SkillFactory.getSkill(9001001).getEffect(1).applyTo(player);
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了祝福指令"));
            }
            return 1;
        }
    }

    public static class 满属性 extends CommandExecute {//maxstats

        public int execute(MapleClient c, String splitted[]) {
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
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了满属性指令"));
            }
            return 1;
        }
    }

    public static class 破攻伤害显示 extends CommandExecute {//maxstats

        public int execute(MapleClient c, String splitted[]) {
            if (MapleParty.伤害显示 == 0) {
                MapleParty.伤害显示 = 1;
                c.getPlayer().dropMessage(5, "开启破攻伤害显示");
            } else {
                MapleParty.伤害显示 = 0;
                c.getPlayer().dropMessage(5, "关闭破攻伤害显示");
            }
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了破攻伤害显示指令"));
            }
            return 1;
        }
    }

    public static class 加属性 extends CommandExecute {//maxstats

        public int execute(MapleClient c, String splitted[]) {
            MapleCharacter player = c.getPlayer();
            player.getStat().setMaxHp((short) 10000);
            player.getStat().setMaxMp((short) 10000);
            player.getStat().setStr((short) 1000);
            player.getStat().setDex((short) 1000);
            player.getStat().setInt((short) 1000);
            player.getStat().setLuk((short) 1000);
            player.updateSingleStat(MapleStat.MAXHP, 10000);
            player.updateSingleStat(MapleStat.MAXMP, 10000);
            player.updateSingleStat(MapleStat.STR, (short) 1000);
            player.updateSingleStat(MapleStat.DEX, (short) 1000);
            player.updateSingleStat(MapleStat.INT, (short) 1000);
            player.updateSingleStat(MapleStat.LUK, (short) 1000);
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了加属性指令"));
            }
            return 1;
        }
    }

    public static class 挖矿 extends CommandExecute {//WhereAmI

        @Override
        public int execute(MapleClient c, String splitted[]) {
            int r = Randomizer.nextInt(100000);
            c.getPlayer().dropMessage(5, "矿点记录成功,矿点代码:" + r + "");
            FileoutputUtil.logToFile("矿点坐标生成表/" + c.getPlayer().getMap().getId() + ".txt",
                    "<imgdir name=\"" + r + "\">\r\n"
                    + "<string name=\"id\" value=\"2112012\"/>\r\n"
                    + "<int name=\"x\" value=\"" + String.valueOf(c.getPlayer().getPosition().x) + "\"/>\r\n"
                    + "<int name=\"y\" value=\"" + String.valueOf(c.getPlayer().getPosition().y) + "\"/>\r\n"
                    + "<int name=\"reactorTime\" value=\"300\"/>\r\n"
                    + "<int name=\"f\" value=\"0\"/>\r\n"
                    + "<string name=\"name\" value=\"\"/>\r\n"
                    + "</imgdir>\r\n\r\n"
            );
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了生成挖矿地点功能"));
            }
            return 1;
        }
    }

    public static class 采药 extends CommandExecute {//WhereAmI

        @Override
        public int execute(MapleClient c, String splitted[]) {
            int r = Randomizer.nextInt(100000);
            c.getPlayer().dropMessage(5, "采药记录成功,采药点代码:" + r + "");
            FileoutputUtil.logToFile("采药点坐标生成表/" + c.getPlayer().getMap().getId() + ".txt",
                    "<imgdir name=\"" + r + "\">\r\n"
                    + "<string name=\"id\" value=\"1072000\"/>\r\n"
                    + "<int name=\"x\" value=\"" + String.valueOf(c.getPlayer().getPosition().x) + "\"/>\r\n"
                    + "<int name=\"y\" value=\"" + String.valueOf(c.getPlayer().getPosition().y) + "\"/>\r\n"
                    + "<int name=\"reactorTime\" value=\"300\"/>\r\n"
                    + "<int name=\"f\" value=\"0\"/>\r\n"
                    + "<string name=\"name\" value=\"\"/>\r\n"
                    + "</imgdir>\r\n\r\n"
            );
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了生成挖采药地点功能"));
            }
            return 1;
        }
    }

    public static class 怪物代码 extends CommandExecute {//mob

        public int execute(MapleClient c, String[] splitted) {
            MapleMonster monster = null;
            for (final MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER))) {
                monster = (MapleMonster) monstermo;
                if (monster.isAlive()) {
                    c.getPlayer().dropMessage(6, "怪物 " + monster.toString());
                }
            }
            if (monster == null) {
                c.getPlayer().dropMessage(6, "找不到怪物");
            }
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了查询怪物代码功能"));
            }
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
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.getMapFactory().HealMap(mapid);

            }
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]开放了地图[" + splitted[1] + "]"));
            }
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
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]关闭了地图[" + splitted[1] + "]"));
            }
            return 1;
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class 地图代码 extends CommandExecute {//WhereAmI

        @Override
        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            c.getPlayer().dropMessage(5, "地图: " + c.getPlayer().getMap().getMapName() + " ");
            c.getPlayer().dropMessage(5, "代码: " + c.getPlayer().getMap().getId() + " ");
            c.getPlayer().dropMessage(5, "坐标: " + String.valueOf(c.getPlayer().getPosition().x) + " , " + String.valueOf(c.getPlayer().getPosition().y) + "");

            c.getPlayer().dropMessage(5, "使用 *传送+<空格>+<地图ID> 可直接传送到目标地图");
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");

            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了查询当前地图代码功能"));
            }
            return 1;
        }
    }

    public static class 在线人数 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int totalOnline = 0;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                totalOnline += cserv.getConnectedClients();
            }
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            c.getPlayer().dropMessage(5, new StringBuilder().append("服务器当前: ").append(totalOnline).append(" 个在线玩家").toString());
            c.getPlayer().dropMessage(5, "                                            ");
            c.getPlayer().dropMessage(5, "使用 *在线玩家 可查看每个频道的玩家在线信息");
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            return 1;
        }
    }

    public static class 在线玩家 extends CommandExecute {

        @Override

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                c.getPlayer().dropMessage(6, "[频道:" + cs.getChannel() + "]→");
                StringBuilder sb = new StringBuilder();
                Collection<MapleCharacter> cmc = cs.getPlayerStorage().getAllCharacters();
                for (MapleCharacter chr : cmc) {
                    if (sb.length() > 150) {
                        sb.setLength(sb.length() - 2);
                        c.getPlayer().dropMessage(5, sb + "\r\n".toString());
                        sb = new StringBuilder();
                    }
                    //if (!chr.isGM()) {
                    c.getPlayer().dropMessage(5, sb + "\r\n".toString());
                    sb.append(MapleCharacterUtil.makeMapleReadable("-[角色ID: " + chr.getId() + " ][名字: " + chr.getName() + " ][等级: " + chr.getLevel() + " ] [地图: " + chr.getMapId() + " / " + chr.getMap().getMapName() + " ]\r\n"));
                    // }
                }

                if (sb.length() >= 2) {
                    sb.setLength(sb.length() - 2);
                    c.getPlayer().dropMessage(5, sb.toString());
                }
            }
            c.getPlayer().dropMessage(5, "                                             ");
            c.getPlayer().dropMessage(5, "使用 *在线人数 可查看服务器总在线人数");
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            return 1;
        }
    }

    public static class 解禁所有IP extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            Connection con = DatabaseConnection.getConnection();
            try {
                java.sql.PreparedStatement ps = con.prepareStatement("Delete from ipbans");
                ps.executeUpdate();
                ps.close();
                c.getPlayer().dropMessage(5, "成功解禁所有已被封禁的IP地址");
            } catch (SQLException e) {
                System.out.println("Error " + e);
            }
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            return 1;
        }
    }

    public static class 解禁所有机器码 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            Connection con = DatabaseConnection.getConnection();
            try {
                java.sql.PreparedStatement ps = con.prepareStatement("Delete from macbans");
                ps.executeUpdate();
                ps.close();
                c.getPlayer().dropMessage(5, "成功解禁所有已被封禁的机器码");
            } catch (SQLException e) {
                System.out.println("Error " + e);
            }
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            return 1;
        }
    }

    public static class 清除每日信息 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            Connection con = DatabaseConnection.getConnection();
            try {
                java.sql.PreparedStatement ps = con.prepareStatement("Delete from bosslog");
                ps.executeUpdate();
                ps.close();
                c.getPlayer().dropMessage(5, "成功清除每日信息");
            } catch (SQLException e) {
                System.out.println("Error " + e);
            }
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            return 1;
        }
    }

    public static class 职业代码 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            c.getPlayer().dropMessage(5, "《冒险岛职业代码一览》");
            c.getPlayer().dropMessage(5, "");
            c.getPlayer().dropMessage(5, "新手:0  初心者:1000  战童:2000");
            c.getPlayer().dropMessage(5, "魂骑士:1100 - 1112");
            c.getPlayer().dropMessage(5, "炎术士:1200 - 1212");
            c.getPlayer().dropMessage(5, "风灵使者:1300 - 1312");
            c.getPlayer().dropMessage(5, "夜行者:1400 - 1412");
            c.getPlayer().dropMessage(5, "奇袭者:1500 - 1512");
            c.getPlayer().dropMessage(5, "战神:2100 - 2112");
            c.getPlayer().dropMessage(5, "战  士:100   剑    客:110  勇    士:111  英      雄:112");
            c.getPlayer().dropMessage(5, "             准 骑 士:120  骑    士:121  圣  骑  士:122");
            c.getPlayer().dropMessage(5, "             枪 战 士:130  龙 骑 士:131  黑  骑  士:132");
            c.getPlayer().dropMessage(5, "魔法师:200   火毒法师:210  火毒巫师:211  火毒魔导师:212");
            c.getPlayer().dropMessage(5, "             冰雷法师:220  冰雷巫师:221  冰雷魔导师:222");
            c.getPlayer().dropMessage(5, "             牧    师:230  祭    师:231  主      教:232");
            c.getPlayer().dropMessage(5, "弓箭手:300   猎    人:310  射    手:311  神  射  手:312");
            c.getPlayer().dropMessage(5, "             弩 弓 手:320  游    侠:321  箭      神:322");
            c.getPlayer().dropMessage(5, "飞  侠:400   刺    客:410  无 影 人:411  隐      士:412");
            c.getPlayer().dropMessage(5, "             侠    客:420  独 行 客:421  侠      盗:422");
            c.getPlayer().dropMessage(5, "海  盗:500   拳    手:510  斗    士:511  冲锋  队长:512");
            c.getPlayer().dropMessage(5, "             火 枪 手:520  大    副:521  船      长:522");
            c.getPlayer().dropMessage(5, "                                      ");
            c.getPlayer().dropMessage(5, "转职指令:*转职+<空格>+<职业代码>");
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");

            return 1;
        }
    }

    public static class 修改当前频道经验倍率 extends CommandExecute {//ExpRate

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length > 1) {
                final int rate = Integer.parseInt(splitted[1]);
                if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        cserv.setExpRate(rate);
                    }
                } else {
                    c.getChannelServer().setExpRate(rate);
                }
                c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
                c.getPlayer().dropMessage(5, "游戏正常经验倍率为: " + Integer.parseInt(ServerProperties.getProperty("ZEV.Exp")) + " 倍");
                c.getPlayer().dropMessage(5, "修改当前频道经验倍率为: " + rate + "倍");
                c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
                if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]将当前频道经验倍率更改为 " + splitted[1] + " 倍"));
                }
            } else {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*修改当前频道经验倍率 <倍率> - 更改经验倍率").toString();

        }
    }

    public static class 修改当前频道物品爆率 extends CommandExecute {//DropRate

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length > 1) {
                final int rate = Integer.parseInt(splitted[1]);
                if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        cserv.setDropRate(rate);
                    }
                } else {
                    c.getChannelServer().setDropRate(rate);
                }
                c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
                c.getPlayer().dropMessage(5, "游戏正常物品爆率为: " + Integer.parseInt(ServerProperties.getProperty("ZEV.Drop")) + " 倍");
                c.getPlayer().dropMessage(5, "修改当前频道物品爆率为: " + rate + "倍");
                c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
                if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]将当前频道爆物倍率更改为 " + splitted[1] + " 倍"));
                }
            } else {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*修改当前频道物品爆率 <倍率> - 更改掉落倍率").toString();

        }
    }

    public static class 修改当前频道金币倍率 extends CommandExecute {//MesoRate

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length > 1) {
                final int rate = Integer.parseInt(splitted[1]);
                if (splitted.length > 2 && splitted[2].equalsIgnoreCase("all")) {
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                        cserv.setMesoRate(rate);
                    }
                } else {
                    c.getChannelServer().setMesoRate(rate);
                }
                c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
                c.getPlayer().dropMessage(5, "游戏正常金币倍率为: " + Integer.parseInt(ServerProperties.getProperty("ZEV.Meso")) + " 倍");
                c.getPlayer().dropMessage(5, "修改当前频道金币倍率为: " + rate + "倍");
                c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
                if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]将当前频道金币倍率更改为 " + splitted[1] + " 倍"));
                }
            } else {
                return 0;
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*修改当前频道金币倍率 <倍率> - 更改金钱倍率").toString();

        }
    }

    public static class 跟踪玩家 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            NPCScriptManager npc = NPCScriptManager.getInstance();
            npc.start(c, 9900004, 81);
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            c.getPlayer().dropMessage(5, "成功打开在线玩家跟踪面板，通过面板可直接跟踪到目标地图");
            c.getPlayer().dropMessage(5, "相同指令：*跟踪+<空格>+<角色名字>");
            c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
            return 1;
        }
    }

    public static class 修改面板 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            NPCScriptManager npc = NPCScriptManager.getInstance();
            npc.start(c, 9900004, 71447500);
            return 1;
        }
    }

    public static class 转职 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().changeJob(Integer.parseInt(splitted[1]));
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                String 职业 = "";
                if (Integer.parseInt(splitted[1]) == 0) {
                    职业 = "新手";
                } else if (Integer.parseInt(splitted[1]) == 100) {
                    职业 = "战士";
                } else if (Integer.parseInt(splitted[1]) == 200) {
                    职业 = "魔法师";
                } else if (Integer.parseInt(splitted[1]) == 300) {
                    职业 = "弓箭手";
                } else if (Integer.parseInt(splitted[1]) == 400) {
                    职业 = "飞侠";
                } else if (Integer.parseInt(splitted[1]) == 500) {
                    职业 = "海盗";
                } else if (Integer.parseInt(splitted[1]) == 110) {
                    职业 = "剑客";
                } else if (Integer.parseInt(splitted[1]) == 111) {
                    职业 = "勇士";
                } else if (Integer.parseInt(splitted[1]) == 112) {
                    职业 = "英雄";
                } else if (Integer.parseInt(splitted[1]) == 120) {
                    职业 = "准骑士";
                } else if (Integer.parseInt(splitted[1]) == 121) {
                    职业 = "骑士";
                } else if (Integer.parseInt(splitted[1]) == 122) {
                    职业 = "圣骑士";
                } else if (Integer.parseInt(splitted[1]) == 130) {
                    职业 = "枪战士";
                } else if (Integer.parseInt(splitted[1]) == 131) {
                    职业 = "龙骑士";
                } else if (Integer.parseInt(splitted[1]) == 132) {
                    职业 = "黑骑士";
                } else if (Integer.parseInt(splitted[1]) == 210) {
                    职业 = "火毒法师";
                } else if (Integer.parseInt(splitted[1]) == 211) {
                    职业 = "火毒巫师";
                } else if (Integer.parseInt(splitted[1]) == 212) {
                    职业 = "火毒导师";
                } else if (Integer.parseInt(splitted[1]) == 220) {
                    职业 = "冰雷法师";
                } else if (Integer.parseInt(splitted[1]) == 221) {
                    职业 = "冰雷巫师";
                } else if (Integer.parseInt(splitted[1]) == 222) {
                    职业 = "冰雷导师";
                } else if (Integer.parseInt(splitted[1]) == 230) {
                    职业 = "牧师";
                } else if (Integer.parseInt(splitted[1]) == 231) {
                    职业 = "祭师";
                } else if (Integer.parseInt(splitted[1]) == 232) {
                    职业 = "主教";
                } else if (Integer.parseInt(splitted[1]) == 310) {
                    职业 = "猎人";
                } else if (Integer.parseInt(splitted[1]) == 311) {
                    职业 = "射手";
                } else if (Integer.parseInt(splitted[1]) == 312) {
                    职业 = "神射手";
                } else if (Integer.parseInt(splitted[1]) == 320) {
                    职业 = "弓弩手";
                } else if (Integer.parseInt(splitted[1]) == 321) {
                    职业 = "游侠";
                } else if (Integer.parseInt(splitted[1]) == 322) {
                    职业 = "箭神";
                } else if (Integer.parseInt(splitted[1]) == 410) {
                    职业 = "刺客";
                } else if (Integer.parseInt(splitted[1]) == 411) {
                    职业 = "无影人";
                } else if (Integer.parseInt(splitted[1]) == 412) {
                    职业 = "隐士";
                } else if (Integer.parseInt(splitted[1]) == 420) {
                    职业 = "侠客";
                } else if (Integer.parseInt(splitted[1]) == 421) {
                    职业 = "独行客";
                } else if (Integer.parseInt(splitted[1]) == 422) {
                    职业 = "侠盗";
                } else if (Integer.parseInt(splitted[1]) == 510) {
                    职业 = "拳手";
                } else if (Integer.parseInt(splitted[1]) == 511) {
                    职业 = "斗士";
                } else if (Integer.parseInt(splitted[1]) == 512) {
                    职业 = "冲锋队长";
                } else if (Integer.parseInt(splitted[1]) == 520) {
                    职业 = "火枪手";
                } else if (Integer.parseInt(splitted[1]) == 521) {
                    职业 = "大副";
                } else if (Integer.parseInt(splitted[1]) == 522) {
                    职业 = "船长";
                } else if (Integer.parseInt(splitted[1]) == 1000) {
                    职业 = "初心者";
                } else if (Integer.parseInt(splitted[1]) == 1100 || Integer.parseInt(splitted[1]) == 1110 || Integer.parseInt(splitted[1]) == 1111 || Integer.parseInt(splitted[1]) == 1112) {
                    职业 = "魂骑士";
                } else if (Integer.parseInt(splitted[1]) == 1200 || Integer.parseInt(splitted[1]) == 1210 || Integer.parseInt(splitted[1]) == 1211 || Integer.parseInt(splitted[1]) == 1212) {
                    职业 = "炎术士";
                } else if (Integer.parseInt(splitted[1]) == 1300 || Integer.parseInt(splitted[1]) == 1310 || Integer.parseInt(splitted[1]) == 1311 || Integer.parseInt(splitted[1]) == 1312) {
                    职业 = "风灵使者";
                } else if (Integer.parseInt(splitted[1]) == 1400 || Integer.parseInt(splitted[1]) == 1410 || Integer.parseInt(splitted[1]) == 1411 || Integer.parseInt(splitted[1]) == 1412) {
                    职业 = "夜行者";
                } else if (Integer.parseInt(splitted[1]) == 1500 || Integer.parseInt(splitted[1]) == 1510 || Integer.parseInt(splitted[1]) == 1511 || Integer.parseInt(splitted[1]) == 1512) {
                    职业 = "奇袭者";
                } else if (Integer.parseInt(splitted[1]) == 2000) {
                    职业 = "战童";
                } else if (Integer.parseInt(splitted[1]) == 2100 || Integer.parseInt(splitted[1]) == 2110 || Integer.parseInt(splitted[1]) == 2111 || Integer.parseInt(splitted[1]) == 2112) {
                    职业 = "战神";
                } else if (Integer.parseInt(splitted[1]) == 800 || Integer.parseInt(splitted[1]) == 900) {
                    职业 = "运营员";
                } else {
                    职业 = "未知";
                }
                c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
                c.getPlayer().dropMessage(5, "恭喜你成功转职为: " + 职业);
                c.getPlayer().dropMessage(5, "使用 *职业代码 可查看所有职业的相关代码");
                c.getPlayer().dropMessage(6, "-------------------------------------------------------------------------------------");
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]转职成[" + 职业 + "]"));
            }
            return 1;
        }
    }

    public static class 全服变猪 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter cm : cserv1.getPlayerStorage().getAllCharacters()) {
                    MapleItemInformationProvider.getInstance().getItemEffect(2210049).applyTo(c.getPlayer());
                    c.sendPacket(UIPacket.getStatusMsg(2210049));
                }
            }
            return 1;
        }
    }

    public static class 断线 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
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

    public static class 给所有人经验 extends CommandExecute {//ExpEveryone

        public int execute(MapleClient c, String splitted[]) {
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
                    mch.startMapEffect("" + MapleParty.开服名字 + "管理员发放 " + gain + " 经验给在线的所有玩家！祝您玩的开心玩的快乐", 5120027);
                    FileoutputUtil.logToFile("服务端记录信息/在线经验发送记录.txt", "\r\n【时间:" + CurrentReadable_Time() + "】【名字:" + c.getPlayer().getName() + "】给所有在线玩家 " + gain + " 经验，此次总计发送 " + ret * gain + " 经验");

                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 点的").append(" 经验 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class 给所有人金币 extends CommandExecute {//mesoEveryone

        public int execute(MapleClient c, String splitted[]) {
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
                    mch.startMapEffect("" + MapleParty.开服名字 + "管理员发放 " + gain + " 冒险币给在线的所有玩家！祝您玩的开心玩的快乐", 5120027);
                    FileoutputUtil.logToFile("服务端记录信息/在线金币发送记录.txt", "\r\n【时间:" + CurrentReadable_Time() + "】【名字:" + c.getPlayer().getName() + "】给所有在线玩家 " + gain + " 金币，此次总计发送 " + ret * gain + " 金币");
                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 冒险币 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class 给所有人点券 extends CommandExecute {//CashEveryone

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " <点券>");
                return 0;
            }
            int ret = 0;
            int gain = Integer.parseInt(splitted[1]);
            for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharactersThreadSafe()) {
                mch.modifyCSPoints(1, gain);
                ret++;
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect("" + MapleParty.开服名字 + "管理员发放 " + gain + " 点券给在线的所有玩家！祝您玩的开心玩的快乐", 5120027);
                    FileoutputUtil.logToFile("服务端记录信息/在线点券发送记录.txt", "\r\n【时间:" + CurrentReadable_Time() + "】【名字:" + c.getPlayer().getName() + "】给所有在线玩家 " + gain + " 点券，此次总计发送 " + ret * gain + " 点券");

                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 点券 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class 给所有人抵用券 extends CommandExecute {//CashEveryone

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(splitted[0] + " <点券>");
                return 0;
            }
            int ret = 0;
            int gain = Integer.parseInt(splitted[1]);
            for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharactersThreadSafe()) {
                mch.modifyCSPoints(2, gain);
                ret++;
            }
            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv1.getPlayerStorage().getAllCharacters()) {
                    mch.startMapEffect("" + MapleParty.开服名字 + "管理员发放 " + gain + " 抵用券给在线的所有玩家！祝您玩的开心玩的快乐", 5120027);
                    FileoutputUtil.logToFile("服务端记录信息/在线抵用券发送记录.txt", "\r\n【时间:" + CurrentReadable_Time() + "】【名字:" + c.getPlayer().getName() + "】给所有在线玩家 " + gain + " 抵用券，此次总计发送 " + ret * gain + " 点券");

                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("命令使用成功，当前共有: ").append(ret).append(" 个玩家获得: ").append(gain).append(" 抵用券 ").append(" 总计: ").append(ret * gain).toString());

            return 1;
        }
    }

    public static class 给当前地图所有人 extends CommandExecute {//GainMap

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 1) {
                return 0;
            }
            Iterator i;
            MapleCharacter player = c.getPlayer();
            LinkedHashSet cmc = new LinkedHashSet(c.getPlayer().getMap().getCharacters());
            String type = splitted[1];
            int amount;
            if (type.equalsIgnoreCase("金币")) {
                amount = Integer.parseInt(splitted[2]);

                for (i = cmc.iterator(); i.hasNext();) {
                    player = (MapleCharacter) i.next();
                    player.gainMeso(amount, true, true, true);
                    player.dropMessage("已经收到 " + amount + " 金币");
                }
            } else if (type.equalsIgnoreCase("经验")) {
                amount = Integer.parseInt(splitted[2]);
                for (i = cmc.iterator(); i.hasNext();) {
                    player = (MapleCharacter) i.next();
                    player.gainExp(amount, true, true, false);
                    player.dropMessage("已经收到 " + amount + " 经验");
                }
            } else if (type.equalsIgnoreCase("点券")) {
                amount = Integer.parseInt(splitted[2]);
                for (i = cmc.iterator(); i.hasNext();) {
                    player = (MapleCharacter) i.next();
                    player.modifyCSPoints(1, amount, true);
                    player.dropMessage("已经收到点卷 " + amount + " 点");
                    String msg = "[管理员信息] GM " + c.getPlayer().getName() + " 給了 " + player.getName() + " 点卷 " + amount + "点";
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
                    FileoutputUtil.logToFile("服务端记录信息/管理员给予信息/给予点卷.txt", "\r\n " + FileoutputUtil.NowTime() + " GM " + c.getPlayer().getName() + " 给了 " + player.getName() + " 点卷 " + amount + "点");
                }
            } else if (type.equalsIgnoreCase("抵用券")) {
                amount = Integer.parseInt(splitted[2]);
                for (i = cmc.iterator(); i.hasNext();) {
                    player = (MapleCharacter) i.next();
                    player.modifyCSPoints(2, amount, true);
                    player.dropMessage("已经收到抵用券 " + amount + " 点");
                    String msg = "[管理员信息] GM " + c.getPlayer().getName() + " 給了 " + player.getName() + " 点卷 " + amount + "点";
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
                    FileoutputUtil.logToFile("服务端记录信息/管理员给予信息/给予点卷.txt", "\r\n " + FileoutputUtil.NowTime() + " GM " + c.getPlayer().getName() + " 给了 " + player.getName() + " 点卷 " + amount + "点");
                }
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*给当前地图所有人 <金币/经验/点券/抵用券> <数量> - 例如：*给当前地图所有人 金钱 5000 ").toString();
        }
    }

    public static class 给金币 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
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
                victim.dropMessage("[密信]管理员赠予你 " + gain + " 金币");
            }
            return 1;
        }
    }

    public static class 给经验 extends CommandExecute {//GainExp

        public int execute(MapleClient c, String splitted[]) {
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
            return 1;
        }
    }

    public static class 给点券 extends CommandExecute {//GainExp

        public int execute(MapleClient c, String splitted[]) {
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
            return 1;
        }
    }

    public static class 给抵用券 extends CommandExecute {//GainExp

        public int execute(MapleClient c, String splitted[]) {
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
            player.modifyCSPoints(2, amount, true);
            player.dropMessage("[密信]管理员赠予你 " + amount + " 抵用券");
            return 1;
        }
    }

    public static class 本地图的人跟我说 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            for (MapleCharacter victim : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (victim.getId() != c.getPlayer().getId()) {
                    victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 1), victim.isGM(), 0));
                }
            }
            return 1;
        }
    }

    public static class 本频道的人跟我说 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            for (MapleCharacter victim : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                if (victim.getId() != c.getPlayer().getId()) {
                    victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 1), victim.isGM(), 0));
                }
            }
            return 1;
        }
    }

    public static class 整个服的人跟我说 extends CommandExecute {//SpeakWorld

        public int execute(MapleClient c, String splitted[]) {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter victim : cserv.getPlayerStorage().getAllCharacters()) {
                    if (victim.getId() != c.getPlayer().getId()) {
                        victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 1), victim.isGM(), 0));
                    }
                }
            }
            return 1;
        }
    }

    public static class 解锁 extends CommandExecute {//////UnbanIP

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;
            }
            byte ret_ = MapleClient.unbanIPMacs(splitted[1]);
            if (ret_ == -2) {
                c.getPlayer().dropMessage(6, "[解锁IP] SQL 错误.");
            } else if (ret_ == -1) {
                c.getPlayer().dropMessage(6, "[解锁IP] 角色不存在.");
            } else if (ret_ == 0) {
                c.getPlayer().dropMessage(6, "[解锁IP] No IP or Mac with that character exists!");
            } else if (ret_ == 1) {
                c.getPlayer().dropMessage(6, "[解锁IP] IP或Mac已解锁其中一個.");
            } else if (ret_ == 2) {
                c.getPlayer().dropMessage(6, "[解锁IP] IP以及Mac已成功解锁.");
            }
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("*解锁IP <玩家名称> - 解锁玩家").toString();
        }
    }

    public static class 封锁 extends CommandExecute {///////TempBan

        public int execute(MapleClient c, String splitted[]) {
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

    public static class 杀 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
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
                    c.getPlayer().dropMessage(6, "[玩家 " + splitted[i] + " 不存在.");
                } else if (player.allowedToTarget(victim)) {
                    victim.getStat().setHp((short) 0);
                    victim.getStat().setMp((short) 0);
                    victim.updateSingleStat(MapleStat.HP, 0);
                    victim.updateSingleStat(MapleStat.MP, 0);
                }
            }
            return 1;
        }
    }

    public static class 给予技能 extends CommandExecute {//GiveSkill

        public int execute(MapleClient c, String splitted[]) {
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
            return new StringBuilder().append("*给予技能 <玩家名称> <技能ID> [技能等級] [技能最大等級] - 给予技能").toString();
        }
    }

    public static class 打开商店 extends CommandExecute {//Shop

        public int execute(MapleClient c, String splitted[]) {
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
                c.getPlayer().dropMessage(5, "此商店不存在");
            }
            return 1;
        }
    }

    public static class 群体治愈 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
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
    }

    public static class 删除NPC extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().getMap().resetNPCs();
            return 1;
        }
    }

    public static class 测试C extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            c.getPlayer().spawnSavedPets();
            // c.sendPacket(LoginPacket.getLoginFailed(1));
            return 1;
        }
    }

    public static class 游戏流畅模式 extends CommandExecute {//maxstats

        public int execute(MapleClient c, String splitted[]) {
            if (MapleParty.伤害显示 == 0) {
                MapleParty.流畅模式 = 1;
                c.getPlayer().dropMessage(5, "开启游戏流畅模式");
            } else {
                MapleParty.流畅模式 = 0;
                c.getPlayer().dropMessage(5, "关闭游戏流畅模式");
            }
            if (gui.Start.ConfigValuesMap.get("指令通知开关") <= 0) {
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:[管理员:" + c.getPlayer().getName() + "]使用了游戏流畅模式指令"));
            }
            return 1;
        }
    }

    public static class 指令大全 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "《" + MapleParty.开服名字 + "管理员指令大全》");
            c.getPlayer().dropMessage(5, "*挖矿   -   记录当前挖矿地点坐标");
            c.getPlayer().dropMessage(5, "*采药   -   记录当前采药地点坐标");
            c.getPlayer().dropMessage(5, "*祝福   -   增加增益技能");
            c.getPlayer().dropMessage(5, "*刷新   -   刷新一下");
            c.getPlayer().dropMessage(5, "*无敌   -   无敌状态");
            c.getPlayer().dropMessage(5, "*吸怪   -   将地图所有怪物吸附到当前位置");
            c.getPlayer().dropMessage(5, "*清物   -   清怪当前地图所有物品");
            c.getPlayer().dropMessage(5, "*清怪   -   清怪当前地图所有怪物（不爆物）");
            c.getPlayer().dropMessage(5, "*清怪2   -   清楚当前地图所有怪物（爆物）");
            c.getPlayer().dropMessage(5, "*地图代码   -   查看当前地图代码");
            c.getPlayer().dropMessage(5, "*职业代码   -   查看所有职业相关职业代码");
            c.getPlayer().dropMessage(5, "*我的位置   -   查看当前位置坐标");
            c.getPlayer().dropMessage(5, "*在线玩家   -   查看所有在线玩家");
            c.getPlayer().dropMessage(5, "*在线人数   -   统计所有在线人数");
            c.getPlayer().dropMessage(5, "*满技能   -   将所有技能加满");
            c.getPlayer().dropMessage(5, "*满属性   -   将所有属性值加满");
            c.getPlayer().dropMessage(5, "*怪物代码   -   查看附近怪物代码");
            c.getPlayer().dropMessage(5, "*拉所有人   -   将所有在线玩家拉到自己所在位置");
            c.getPlayer().dropMessage(5, "*解禁所有IP   -   解禁所有已被封禁的IP");
            c.getPlayer().dropMessage(5, "*解禁所有机器码   -   解禁所有已被封禁的机器码");
            c.getPlayer().dropMessage(5, "*刷<空格>[物品代码]   -   刷出指定物品");
            c.getPlayer().dropMessage(5, "*丢<空格>[物品代码]   -   丢出指定物品");
            c.getPlayer().dropMessage(5, "*等级<空格>[等级]   -   升级到指定等级");
            c.getPlayer().dropMessage(5, "*升级   -   提升一级");
            c.getPlayer().dropMessage(5, "*转职<空格>[职业代码]   -   转职指定职业");
            c.getPlayer().dropMessage(5, "*传送<空格>[地图代码]   -   转送到指定制图");
            c.getPlayer().dropMessage(5, "*跟踪<空格>[玩家名字]   -   跟踪到指定玩家所在地图");
            c.getPlayer().dropMessage(5, "*开放地图<空格>[地图代码]   -   开放地图");
            c.getPlayer().dropMessage(5, "*关闭地图<空格>[地图代码]   -   关闭地图");
            c.getPlayer().dropMessage(5, "*刷能力点<空格>[数量]   -   刷AP点");
            c.getPlayer().dropMessage(5, "*刷技能点<空格>[数量]   -   刷SP点");
            c.getPlayer().dropMessage(5, "*给金币<空格>[玩家名字]   -   给指定玩家发金币");
            c.getPlayer().dropMessage(5, "*给经验<空格>[玩家名字]   -   给指定玩家发经验");
            c.getPlayer().dropMessage(5, "*给点券<空格>[玩家名字]   -   给指定玩家发点券");
            c.getPlayer().dropMessage(5, "*给抵用券<空格>[玩家名字]   -   给指定玩家发抵用券");
            c.getPlayer().dropMessage(5, "*给所有人经验<空格>[数量]   -   给所有人经验");
            c.getPlayer().dropMessage(5, "*给所有人金币<空格>[数量]   -   给所有人金币");
            c.getPlayer().dropMessage(5, "*给所有人点券<空格>[数量]   -   给所有人点券");
            c.getPlayer().dropMessage(5, "*给当前地图所有人<空格>[类型]<空格>[数量]   -   类型:金币,经验,点券,抵用券");
            c.getPlayer().dropMessage(5, "*给所有人抵用券<空格>[数量]   -   给所有人抵用券");
            c.getPlayer().dropMessage(5, "*封号<空格>[玩家名称]<空格>[封号原因]   -   封禁玩家的指令");
            c.getPlayer().dropMessage(5, "*解封<空格>[玩家名称]<空格>[解封原因]   -   解封玩家的指令");
            c.getPlayer().dropMessage(5, "*解锁<空格>[玩家名称]   -   解锁IP，机器码");
            c.getPlayer().dropMessage(5, "*封锁<空格>[玩家名称]   -   封锁IP，机器码");
            c.getPlayer().dropMessage(5, "*断线<空格>[断线类型]   -   m=地图，c=频道，w=世界");
            c.getPlayer().dropMessage(5, "*打开商店<空格>[商店ID]   -   打开指定的商店");
            c.getPlayer().dropMessage(5, "*新建NPC<空格>[NPC代码]   -   在自己的位置新建永久存在的NPC");
            c.getPlayer().dropMessage(5, "*召唤怪物<空格>[怪物代码]   -   在自己的位置召唤指定怪物");
            c.getPlayer().dropMessage(5, "*人气设置<空格>[玩家]<空格>[数量]   -   刷人气");
            c.getPlayer().dropMessage(5, "*清除每日信息   -   清除每日信息，不正常无法清除时解决");
            c.getPlayer().dropMessage(5, "*修改当前频道金币倍率<空格>[倍率]   -   修改当前频道的金币倍率");
            c.getPlayer().dropMessage(5, "*修改当前频道物品爆率<空格>[倍率]   -   修改当前频道的物品爆率");
            c.getPlayer().dropMessage(5, "*修改当前频道经验倍率<空格>[倍率]   -   修改当前频道的经验倍率");
            c.getPlayer().dropMessage(5, "*本地图的人跟我说<空格>[文字]   -   控制地图所有玩家跟自己说话");
            c.getPlayer().dropMessage(5, "*本频道的人跟我说<空格>[文字]   -   控制频道所有玩家跟自己说话");
            c.getPlayer().dropMessage(5, "*整个服的人跟我说<空格>[文字]   -   控制全服所有玩家跟自己说话");
            c.getPlayer().dropMessage(5, "*给予技能<空格>[玩家名称]<空格>[技能ID]<空格>[技能现在等级]<空格>[技能最大等级]   -   给予玩家技能");
            c.getPlayer().dropMessage(5, "*杀<空格>[玩家名字]   -   杀掉指定玩家");
            c.getPlayer().dropMessage(5, "*群体治愈   -   治疗地图所有玩家");
            c.getPlayer().dropMessage(5, "*呼出后台(不小心关闭服务端后台可使用呼出)");
            c.getPlayer().dropMessage(5, "*地图回收(解决地图卡顿，掉线问题)");
            return 1;
        }
    }

    public static class 呼出后台 extends CommandExecute {//Packet

        @Override
        public int execute(MapleClient c, String[] splitted) {
            CashShopServer();
            return 1;
        }
    }

    public static class ZEV extends CommandExecute {//Packet

        @Override
        public int execute(MapleClient c, String[] splitted) {
            DebugWindow debug = new DebugWindow();
            debug.setC(c);
            debug.setVisible(true);
            return 1;
        }
    }

    public static class 地图回收 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int 地图 = c.getPlayer().getMapId();
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.getMapFactory().destroyMap(地图, true);
                cserv.getMapFactory().HealMap(地图);
            }
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [管理员:" + c.getPlayer().getName() + "]使用了地图回收功能"));
            return 1;
        }
    }
}
