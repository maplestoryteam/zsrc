/*
 对话NPC
仓库对话
 */
package handling.channel.handler;

import abc.任务修复;
import abc.拍卖行限制;
import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import client.MapleClient;
import client.MapleCharacter;
import constants.GameConstants;
import client.MapleQuestStatus;
import client.RockPaperScissors;
import client.inventory.ItemFlag;
import handling.SendPacketOpcode;
import handling.world.MapleParty;
import handling.world.World;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static quest.Quest.Quest;
import static quest.Quest.canForfeit;
import server.AutobanManager;
import server.MapleShop;
import server.MapleInventoryManipulator;
import server.MapleStorage;
import server.life.MapleNPC;
import server.quest.MapleQuest;
import scripting.NPCScriptManager;
import scripting.NPCConversationManager;
import server.MapleItemInformationProvider;
import server.ServerProperties;
import server.maps.MapleMap;
import tools.ArrayMap;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;

public class NPCHandler {

//    public static int 对话 = 0;
    public static final void NPCAnimation(final LittleEndianAccessor slea, final MapleClient c) {//实时连接对话
////        System.out.println("测试:" + 对话);
////        对话++;
        //如果什么小于4
        final int length = (int) slea.available();
        if (length < 4) {
            return;
        }
        //如果地图空
        MapleMap map = c.getPlayer().getMap();
        if (map == null) {
            return;
        }
        //如果NPC
        int oid = slea.readInt();
        MapleNPC npc = map.getNPCByOid(oid);
        if (npc == null) {
            if (c.getPlayer().isAdmin()) {
                //c.getPlayer().dropMessage("NPC OID =" + oid);
                c.getPlayer().dropMessage(6, "dialogue:[NPCHandler.java/NPCAnimation/" + oid + "]");

            }
            return;
        }
        //以下NPC返回空
        switch (npc.getId()) {
            case 1010100://1010100
            case 1012003://长老斯坦
            case 1012106://明明女士
            case 1052103://内拉
            case 1061100://宾馆服务员
            case 1032004://路易斯
            case 2103://奥兹
            case 10000://皮奥
                return;
        }
        if (!c.getPlayer().isMapObjectVisible(npc)) {
            return;
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.NPC_ACTION.getValue());
        mplew.writeInt(oid);
        if (length == 6) { // NPC 谈
            mplew.writeShort(slea.readShort());
        } else if (length > 9) { // NPC 移动
            mplew.write(slea.read(length - 13));
        } else {
            if (c.getPlayer().isAdmin()) {
                c.getPlayer().dropMessage("NPC, 小包裹:" + slea.toString());
            }
            return;
        }

    }

    public static void NPCShop(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final byte bmode = slea.readByte();
        if (chr == null) {
            return;
        }

        switch (bmode) {
            case 0: {
                final MapleShop shop = chr.getShop();
                if (shop == null) {
                    return;
                }
                slea.skip(2);
                final int itemId = slea.readInt();
                final short quantity = slea.readShort();
                shop.buy(c, itemId, quantity);
                break;
            }
            case 1: {
                final MapleShop shop = chr.getShop();
                if (shop == null) {
                    return;
                }
                final byte slot = (byte) slea.readShort();
                final int itemId = slea.readInt();
                final short quantity = slea.readShort();
                shop.sell(c, GameConstants.getInventoryType(itemId), slot, quantity);
                break;
            }
            case 2: {
                final MapleShop shop = chr.getShop();
                if (shop == null) {
                    return;
                }
                final byte slot = (byte) slea.readShort();
                shop.recharge(c, slot);
                break;
            }
            default:
                chr.setConversation(0);
                break;
        }
    }

    public static void NPCTalk(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
//        
//       
//        
//        /*long nowTimestamp = System.currentTimeMillis();
//        if (cnowTimestamp - 对话冷却 > 1000 * 5) {
//            chr.dropMessage(5, "还在冷却");
//            对话冷却 = nowTimestamp;
//            return;
//        }*/
//        c.getPlayer().setCurrenttime(System.currentTimeMillis());
//        if (c.getPlayer().getCurrenttime() - c.getPlayer().getLasttime() < 3000) {
//            c.getPlayer().dropMessage(1, "<小z提示>：不要丢那么快");
//            c.sendPacket(MaplePacketCreator.enableActions());
//            return ;
//        }
//        

        final MapleNPC npc = chr.getMap().getNPCByOid(slea.readInt());
        if (chr == null || chr.getMap() == null) {
            return;
        }
        //slea.readInt();
        if (npc == null) {
            return;
        }
        if (chr.getConversation() != 0) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            //  chr.dropMessage(5, "你现在已经假死请使用@ea");
            return;
        }
        c.getPlayer().updateTick(slea.readInt());// 暂时不检测点NPC的速度
        if (npc.hasShop()) {
            c.sendPacket(MaplePacketCreator.confirmShopTransaction((byte) 20));
            chr.setConversation(1);
            npc.sendShop(c);
        } else {
            NPCScriptManager.getInstance().start(c, npc.getId());
        }
//        NPCScriptManager.getInstance().dispose(c);
//        c.sendPacket(MaplePacketCreator.enableActions());
//        if (chr == null || chr.getMap() == null) {
//            return;
//        }
//        final MapleNPC npc = chr.getMap().getNPCByOid(slea.readInt());
//
//        slea.readInt();
//        if (npc == null) {
//            return;
//        }
//        if (chr.getConversation() != 0) {
//     
//           // chr.dropMessage(5, "[小z提示]：如果发现进图无反应，NPC无反应，请打开多功能盒子即可");
//            c.sendPacket(MaplePacketCreator.enableActions());//解卡
//            NPCScriptManager.getInstance().dispose(c);
//            return;
//        }
//
//        if (npc.hasShop()) {
//            /*
//             * if (c.getPlayer().getShop() != null) {
//             * c.getPlayer().setShop(null);
//             * c.sendPacket(MaplePacketCreator.confirmShopTransaction((byte)
//             * 20)); }
//             */
//            c.sendPacket(MaplePacketCreator.confirmShopTransaction((byte) 20));
//            chr.setConversation(1);
//            npc.sendShop(c);
//        } else {
//            NPCScriptManager.getInstance().start(c, npc.getId());
//        }
    }

    public static final void QuestAction(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final byte action = slea.readByte();
        short quest = slea.readShort();
        if (quest < 0) { //questid 50000 and above, WILL cast to negative, this was tested.
            quest += 65536; //probably not the best fix, but whatever
        }
        if (chr == null) {
            return;
        }
        if (!chr.canQuestAction()) {
            chr.dropMessage(1, "提交操作过快请稍后！");
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        任务修复 itemjc2 = new 任务修复();
        String items2 = itemjc2.getPM();
        if (items2.contains("," + quest + ",") && action != 3) {
            NPCScriptManager.getInstance().dispose(c);
            NPCScriptManager.getInstance().start(c, 2007, quest);
        }
        final MapleQuest q = MapleQuest.getInstance(quest);

        if (MapleParty.任务修复 > 0) {
            c.getPlayer().dropMessage(6, "任务代码: " + quest);
            c.getPlayer().dropMessage(6, "任务作为: " + action);
            c.getPlayer().dropMessage(6, "" + chr);
        }
        switch (action) {
            case 0: { // Restore lost item
                chr.updateTick(slea.readInt());
                final int itemid = slea.readInt();
                MapleQuest.getInstance(quest).RestoreLostItem(chr, itemid);
                break;
            }
            case 1: { // Start Quest
                final int npc = slea.readInt();
                q.start(chr, npc);
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(6, "Task start:[scripts/quest/" + quest + "]");
                    c.getPlayer().dropMessage(6, "dialogue:[scripts/npc/: " + npc + "]");
                }
                break;
            }
            case 2: { // Complete Quest
                final int npc = slea.readInt();
                chr.updateTick(slea.readInt());
                if (slea.available() >= 4) {
                    q.complete(chr, npc, slea.readInt());
                } else {
                    q.complete(chr, npc);
                }
                //阿里安特任务，扎诺

                /*
                 if (quest == 3936) {
                    NPCScriptManager.getInstance().dispose(c);
                    NPCScriptManager.getInstance().start(c, 2007, 3936);
                }
                 */
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(6, "Task complete:[scripts/quest/" + quest + "]");
                    c.getPlayer().dropMessage(6, "dialogue:[scripts/npc/: " + npc + "]");
                }
                // c.sendPacket(MaplePacketCreator.completeQuest(c.getPlayer(), quest));
                //c.sendPacket(MaplePacketCreator.updateQuestInfo(c.getPlayer(), quest, npc, (byte)14));
                // 6 = start quest
                // 7 = unknown error
                // 8 = equip is full
                // 9 = not enough mesos
                // 11 = due to the equipment currently being worn wtf o.o
                // 12 = you may not posess more than one of this item
                break;
            }
            case 3: { // Forefit Quest
                String items = itemjc2.getPMM();
                if (!items.contains("," + q.getId() + ",")) {
                    q.forfeit(chr);
                } else {
                    chr.dropMessage(1, "你不能放弃这个任务。");
                }
//                if (canForfeit(q.getId())) {
//                    q.forfeit(chr);
//                } else {
//                    chr.dropMessage(1, "你不能放弃这个任务。");
//                }
                break;
            }
            case 4: { // Scripted Start Quest
                final int npc = slea.readInt();
                slea.readInt();
                NPCScriptManager.getInstance().startQuest(c, npc, quest);
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(6, "Task start:[scripts/quest/" + quest + "]");
                    c.getPlayer().dropMessage(6, "dialogue:[scripts/npc/: " + npc + "]");
                }
                break;
            }
            case 5: { // Scripted End Quest
                final int npc = slea.readInt();
                NPCScriptManager.getInstance().endQuest(c, npc, quest, false);
                c.sendPacket(MaplePacketCreator.showSpecialEffect(9)); // Quest completion
                chr.getMap().broadcastMessage(chr, MaplePacketCreator.showSpecialEffect(chr.getId(), 9), false);
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(6, "Task complete:[scripts/quest/" + quest + "]");
                    c.getPlayer().dropMessage(6, "dialogue:[scripts/npc/: " + npc + "]");
                }
                break;
            }
        }
    }

    public static final void Storage(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final byte mode = slea.readByte();
        if (chr == null) {
            return;
        }
        if (gui.Start.ConfigValuesMap.get("游戏仓库开关") > 0) {
            chr.dropMessage(1, "管理员已经从后台关闭了游戏仓库功能");
            return;
        }
        final MapleStorage storage = chr.getStorage();

        switch (mode) {
            case 4: {  // 取出
                final byte type = slea.readByte();
                final byte slot = storage.getSlot(MapleInventoryType.getByType(type), slea.readByte());//// 从0开始计算的
                final IItem item = storage.takeOut(slot);//获取道具在仓库的信息
                if (ii.isCash(item.getItemId())) {
                    c.sendPacket(MaplePacketCreator.enableActions());
                    return;
                }
                if (item != null) {   //检测角色背包是否有位置
                    if (!MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                        storage.store(item);//从仓库中取出这个道具
                        chr.dropMessage(1, "你的物品栏已经满了..");
                    } else {
                        MapleInventoryManipulator.addFromDrop(c, item, false);//给角色道具
                    }
                    storage.sendTakenOut(c, GameConstants.getInventoryType(item.getItemId()));
                } else {
                    System.out.println("[作弊] " + chr.getName() + " (等级 " + chr.getLevel() + ") 试图从仓库取出不存在的道具.");
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[GM Message] 玩家: " + chr.getName() + " (等级 " + chr.getLevel() + ") 试图从仓库取出不存在的道具."));
                    c.sendPacket(MaplePacketCreator.enableActions());
                    return;
                }
                break;
            }
            case 5: { ////存入仓库
                final byte slot = (byte) slea.readShort();
                final int itemId = slea.readInt();
                short quantity = slea.readShort();
                //检测保存道具的数量是否小于 1 
                if (quantity < 1) {
                    //AutobanManager.getInstance().autoban(c, "Trying to store " + quantity + " of " + itemId);
                    return;
                }
                if (itemId >= 1112446 && itemId <= 1112495) {
                    c.getPlayer().dropMessage(1, "禁止存入仓库");
                    c.sendPacket(MaplePacketCreator.enableActions());
                    return;
                }

                //检测仓库的道具是否已满
                if (storage.isFull()) {
                    c.sendPacket(MaplePacketCreator.getStorageFull());
                    return;
                }
                //检测金币是否足够
                if (chr.getMeso() < 1000) {
                    chr.dropMessage(1, "需要金币 1000 金币.");
                } else {
                    //检测角色背包当前道具是否有道具
                    MapleInventoryType type = GameConstants.getInventoryType(itemId);
                    IItem item = chr.getInventory(type).getItem(slot).copy();
                    //如果是现金道具
                    if (ii.isCash(item.getItemId())) {
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    //如果 是宠物
                    if (GameConstants.isPet(item.getItemId())) {
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    final byte flag = item.getFlag();
                    if (ii.isPickupRestricted(item.getItemId()) && storage.findById(item.getItemId()) != null) {
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    if (item.getItemId() == itemId && (item.getQuantity() >= quantity || GameConstants.isThrowingStar(itemId) || GameConstants.isBullet(itemId))) {
                        if (ii.isDropRestricted(item.getItemId())) {
                            if (ItemFlag.KARMA_EQ.check(flag)) {
                                item.setFlag((byte) (flag - ItemFlag.KARMA_EQ.getValue()));
                            } else if (ItemFlag.KARMA_USE.check(flag)) {
                                item.setFlag((byte) (flag - ItemFlag.KARMA_USE.getValue()));
                            } else {
                                c.sendPacket(MaplePacketCreator.enableActions());
                                return;
                            }
                        }
                        if (GameConstants.isThrowingStar(itemId) || GameConstants.isBullet(itemId)) {
                            quantity = item.getQuantity();
                        }
                        chr.gainMeso(-100, false, true, false);//收取保存到仓库的费用
                        MapleInventoryManipulator.removeFromSlot(c, type, slot, quantity, false);//删除角色背包中的道具
                        item.setQuantity(quantity);//设置道具的数量
                        storage.store(item);//存入道具到仓库

                    } else {
                        AutobanManager.getInstance().addPoints(c, 1000, 0, "试图存入到仓库的道具: " + itemId + " 数量: " + quantity + " 当前玩家用道具: " + item.getItemId() + " 数量: " + item.getQuantity());
                        return;
                    }
                }
                storage.sendStored(c, GameConstants.getInventoryType(itemId));//发送当前仓库的道具封包
                break;
            }
            case 6: {
                c.sendPacket(MaplePacketCreator.enableActions());
                break;
            }
            case 7: {// 金币
                int meso = slea.readInt();
                final int storageMesos = storage.getMeso();
                final int playerMesos = chr.getMeso();

                if ((meso > 0 && storageMesos >= meso) || (meso < 0 && playerMesos >= -meso)) {
                    if (meso < 0 && (storageMesos - meso) < 0) { // storing with overflow
                        meso = -(Integer.MAX_VALUE - storageMesos);
                        if ((-meso) > playerMesos) { // should never happen just a failsafe
                            return;
                        }
                    } else if (meso > 0 && (playerMesos + meso) < 0) { // taking out with overflow
                        meso = (Integer.MAX_VALUE - playerMesos);
                        if ((meso) > storageMesos) { // should never happen just a failsafe
                            return;
                        }
                    }
                    storage.setMeso(storageMesos - meso);
                    chr.gainMeso(meso, false, true, false);
                } else {
                    AutobanManager.getInstance().addPoints(c, 1000, 0, "Trying to store or take out unavailable amount of mesos (" + meso + "/" + storage.getMeso() + "/" + c.getPlayer().getMeso() + ")");
                    return;
                }
                storage.sendMeso(c);
                break;
            }
            case 8: {// 退出仓库
                storage.close();
                chr.setConversation(0);
                break;
            }
            default:
                System.out.println("未知的仓库操作包 0x0: " + mode);
                break;
        }
        c.getPlayer().saveToDB(false, false);//存档
    }

    public static final void MarrageNpc(MapleClient c) {//结婚类
        if (c != null && c.getPlayer() != null) {
            if (c.getPlayer().getMapId() == 700000100) {
                c.getPlayer().changeMap(700000200);
            }
        }
    }

    public static final void NPCMoreTalk(final LittleEndianAccessor slea, final MapleClient c) {
        final byte lastMsg = slea.readByte(); // 00 (last msg type I think)
        final byte action = slea.readByte(); // 00 = end chat, 01 == follow

        final NPCConversationManager cm = NPCScriptManager.getInstance().getCM(c);

        if (cm == null || c.getPlayer().getConversation() == 0 || cm.getLastMsg() != lastMsg) {
            return;
        }
        cm.setLastMsg((byte) -1);
        if (lastMsg == 2) {
            if (action != 0) {
                cm.setGetText(slea.readMapleAsciiString());
                if (cm.getType() == 0) {
                    NPCScriptManager.getInstance().startQuest(c, action, lastMsg, -1);
                } else if (cm.getType() == 1) {
                    NPCScriptManager.getInstance().endQuest(c, action, lastMsg, -1);
                } else {
                    NPCScriptManager.getInstance().action(c, action, lastMsg, -1);
                }
            } else {
                cm.dispose();
            }
        } else {
            int selection = -1;
            if (slea.available() >= 4) {
                selection = slea.readInt();
            } else if (slea.available() > 0) {
                selection = slea.readByte();
            }
            if (lastMsg == 4 && selection == -1) {
                cm.dispose();
                return;//h4x
            }
            if (selection >= -1 && action != -1) {
                if (cm.getType() == 0) {
                    NPCScriptManager.getInstance().startQuest(c, action, lastMsg, selection);
                } else if (cm.getType() == 1) {
                    NPCScriptManager.getInstance().endQuest(c, action, lastMsg, selection);
                } else {
                    NPCScriptManager.getInstance().action(c, action, lastMsg, selection);
                }
            } else {
                cm.dispose();
            }
        }
    }

    public static final void repairAll(final MapleClient c) {
        if (c.getPlayer().getMapId() != 240000000) {
            return;
        }
        Equip eq;
        double rPercentage;
        int price = 0;
        Map<String, Integer> eqStats;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final Map<Equip, Integer> eqs = new ArrayMap<Equip, Integer>();
        final MapleInventoryType[] types = {MapleInventoryType.EQUIP, MapleInventoryType.EQUIPPED};
        for (MapleInventoryType type : types) {
            for (IItem item : c.getPlayer().getInventory(type)) {
                if (item instanceof Equip) { //redundant
                    eq = (Equip) item;
                    if (eq.getDurability() >= 0) {
                        eqStats = ii.getEquipStats(eq.getItemId());
                        if (eqStats.get("durability") > 0 && eq.getDurability() < eqStats.get("durability")) {
                            rPercentage = (100.0 - Math.ceil((eq.getDurability() * 1000.0) / (eqStats.get("durability") * 10.0)));
                            eqs.put(eq, eqStats.get("durability"));
                            price += (int) Math.ceil(rPercentage * ii.getPrice(eq.getItemId()) / (ii.getReqLevel(eq.getItemId()) < 70 ? 100.0 : 1.0));
                        }
                    }
                }
            }
        }
        if (eqs.size() <= 0 || c.getPlayer().getMeso() < price) {
            return;
        }
        c.getPlayer().gainMeso(-price, true);
        Equip ez;
        for (Entry<Equip, Integer> eqqz : eqs.entrySet()) {
            ez = eqqz.getKey();
            ez.setDurability(eqqz.getValue());
            c.getPlayer().forceReAddItem(ez.copy(), ez.getPosition() < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP);
        }
    }

    public static final void repair(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer().getMapId() != 240000000 || slea.available() < 4) { //leafre for now
            return;
        }
        final int position = slea.readInt(); //who knows why this is a int
        final MapleInventoryType type = position < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP;
        final IItem item = c.getPlayer().getInventory(type).getItem((byte) position);
        if (item == null) {
            return;
        }
        final Equip eq = (Equip) item;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final Map<String, Integer> eqStats = ii.getEquipStats(item.getItemId());
        if (eq.getDurability() < 0 || eqStats.get("durability") <= 0 || eq.getDurability() >= eqStats.get("durability")) {
            return;
        }
        final double rPercentage = (100.0 - Math.ceil((eq.getDurability() * 1000.0) / (eqStats.get("durability") * 10.0)));
        //drpq level 105 weapons - ~420k per %; 2k per durability point
        //explorer level 30 weapons - ~10 mesos per %
        final int price = (int) Math.ceil(rPercentage * ii.getPrice(eq.getItemId()) / (ii.getReqLevel(eq.getItemId()) < 70 ? 100.0 : 1.0)); // / 100 for level 30?
        //TODO: need more data on calculating off client
        if (c.getPlayer().getMeso() < price) {
            return;
        }
        c.getPlayer().gainMeso(-price, false);
        eq.setDurability(eqStats.get("durability"));
        c.getPlayer().forceReAddItem(eq.copy(), type);
    }

    public static final void QuestItemUI(final LittleEndianAccessor slea, MapleClient c) {
        int questID = slea.readShortAsInt();
        final MapleQuest quest = MapleQuest.getInstance(questID);
        final MapleQuestStatus stats = c.getPlayer().getQuestRemove(quest);
        if (c != null && c.getPlayer() != null) {
            if (stats != null) {
                c.getPlayer().updateQuest(stats, true);
            }
            c.getPlayer().setItemQuestId(questID);
        }
    }

    public static final void UpdateQuest(final LittleEndianAccessor slea, final MapleClient c) {
        int itemid = slea.readInt();
        if (c != null && c.getPlayer() != null) {
            c.getPlayer().setItemQuestItemId(itemid);
        }
        c.sendPacket(MaplePacketCreator.enableActions());
    }

    public static final void UseItemQuest(final LittleEndianAccessor slea, final MapleClient c) {
        slea.readByte();
        final short slot = slea.readShort();
        final int itemId = slea.readInt();
        final IItem item = c.getPlayer().getInventory(MapleInventoryType.ETC).getItem(slot);

        final MapleQuest quest = MapleQuest.getInstance(c.getPlayer().getItemQuestId());
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

        if (quest != null && item != null) {
            int newData = 1;
            switch (quest.getId()) {
                case 8252:
                    newData = 1;
                    break;
                default:
                    newData = 1;
                    break;
            }
            final MapleQuestStatus stats = c.getPlayer().getQuestNAdd(quest);
            if (stats != null) {
                int oldValue = stats.getCustomData() == null ? 0 : Integer.valueOf(stats.getCustomData());
                stats.setCustomData(String.valueOf(oldValue + newData));
                c.getPlayer().updateQuest(stats, true);
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.ETC, slot, (short) 1, false);
                c.sendPacket(MaplePacketCreator.updateQuestInfo(c.getPlayer(), c.getPlayer().getItemQuestId(), 9900001, (byte) 1));
            }
        }
        c.getPlayer().任务存档();
        c.sendPacket(MaplePacketCreator.enableActions());
    }

    public static final void RPSGame(final LittleEndianAccessor slea, final MapleClient c) {
        if (slea.available() == 0 || !c.getPlayer().getMap().containsNPC(9000019)) {
            if (c.getPlayer().getRPS() != null) {
                c.getPlayer().getRPS().dispose(c);
            }
            return;
        }
        final byte mode = slea.readByte();
        switch (mode) {
            case 0: //start game
            case 5: //retry
                if (c.getPlayer().getRPS() != null) {
                    c.getPlayer().getRPS().reward(c);
                }
                if (c.getPlayer().getMeso() >= 1000) {
                    c.getPlayer().setRPS(new RockPaperScissors(c, mode));
                } else {
                    c.sendPacket(MaplePacketCreator.getRPSMode((byte) 0x08, -1, -1, -1));
                }
                break;
            case 1: //answer
                if (c.getPlayer().getRPS() == null || !c.getPlayer().getRPS().answer(c, slea.readByte())) {
                    c.sendPacket(MaplePacketCreator.getRPSMode((byte) 0x0D, -1, -1, -1));
                }
                break;
            case 2: //time over
                if (c.getPlayer().getRPS() == null || !c.getPlayer().getRPS().timeOut(c)) {
                    c.sendPacket(MaplePacketCreator.getRPSMode((byte) 0x0D, -1, -1, -1));
                }
                break;
            case 3: //continue
                if (c.getPlayer().getRPS() == null || !c.getPlayer().getRPS().nextRound(c)) {
                    c.sendPacket(MaplePacketCreator.getRPSMode((byte) 0x0D, -1, -1, -1));
                }
                break;
            case 4: //leave
                if (c.getPlayer().getRPS() != null) {
                    c.getPlayer().getRPS().dispose(c);
                } else {
                    c.sendPacket(MaplePacketCreator.getRPSMode((byte) 0x0D, -1, -1, -1));
                }
                break;
        }
    }
}
