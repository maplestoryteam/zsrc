/*
ʹ����Ʒ��
����
 */
package handling.channel.handler;

import gui.Start;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import client.inventory.Equip;
import client.inventory.IEquip;
import client.inventory.IEquip.ScrollResult;
import client.inventory.IItem;
import client.ISkill;
import client.inventory.ItemFlag;
import client.inventory.MaplePet;
import client.inventory.MaplePet.PetFlag;
import client.inventory.MapleMount;
import client.MapleCharacter;
import client.MapleClient;
import client.inventory.MapleInventoryType;
import client.inventory.MapleInventory;
import client.MapleStat;
import client.PlayerStats;
import constants.GameConstants;
import client.SkillFactory;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import server.AutobanManager;
import server.Randomizer;
import abc.��Ʒ�������;
import static gui.QQMsgServer.sendMsgToQQGroup;
import handling.world.MapleParty;
import server.MapleShopFactory;
import server.MapleItemInformationProvider;
import server.MapleInventoryManipulator;
import server.quest.MapleQuest;
import server.maps.FieldLimitType;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.life.MapleMonster;
import server.life.MapleLifeFactory;
import scripting.NPCScriptManager;
import server.*;
import handling.channel.ChannelServer;
import scripting.NPCConversationManager;
import static scripting.NPCConversationManager.��ɫIDȡ����;
import server.maps.*;
import server.shops.HiredMerchant;
import server.shops.IMaplePlayerShop;
import static tools.FileoutputUtil.CurrentReadable_Time;
import tools.Pair;
import tools.packet.MTSCSPacket;
import tools.packet.PetPacket;
import tools.data.LittleEndianAccessor;
import tools.MaplePacketCreator;
import tools.packet.PlayerShopPacket;

public class InventoryHandler {

    public static void ���װ����׸�ħ��Ϣ(final String mxmxdDaKongFuMo, final MapleCharacter player) {
        if (mxmxdDaKongFuMo != null && mxmxdDaKongFuMo.length() == 0) {
            return;
        }
        String arr1[] = mxmxdDaKongFuMo.split(",");
        for (int i = 0; i < arr1.length; i++) {
            String pair = arr1[i];
            if (pair.contains(":")) {
                String kongInfo = "��";//��" + (i + 1) + "�ף�:
                /* if (i == 0) {
                    kongInfo = "���٣�";
                } else if (i == 1) {
                    kongInfo = "���ڣ�";
                } else if (i == 2) {
                    kongInfo = "���ۣ�";
                }*/
                String arr2[] = pair.split(":");
                int fumoType = Integer.parseInt(arr2[0]);
                int fumoVal = Integer.parseInt(arr2[1]);
                if (fumoType > 0 && Start.FuMoInfoMap.containsKey(fumoType)) {
                    String infoArr[] = Start.FuMoInfoMap.get(fumoType);
                    String fumoName = infoArr[0];
                    String fumoInfo = infoArr[1];
                    kongInfo += fumoName + " " + String.format(fumoInfo, fumoVal);
                } else {
                    kongInfo += "[δ��ħ]";
                }
                player.dropMessage(2, "\t\t\t��ħ : " + kongInfo);
            }
        }
    }

    public static final void ItemMove(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer().getPlayerShop() != null || c.getPlayer().getConversation() > 0 || c.getPlayer().getTrade() != null) { //hack
            return;
        }
        c.getPlayer().updateTick(slea.readInt());
        final MapleInventoryType type = MapleInventoryType.getByType(slea.readByte()); //04
        final short src = slea.readShort();                                            //01 00
        final short dst = slea.readShort();                                            //00 00
        final short quantity = slea.readShort();
        if (src < 0 && dst > 0) {
            MapleInventoryManipulator.unequip(c, src, dst);
            //ȡ��װ��
            int itemided = c.getPlayer().getInventory(type).getItem(dst).getItemId();

        } else if (dst < 0) {
            if (dst == -128) {
                c.getPlayer().dropMessage(1, "�ǳ���Ǹ�����ﲻ��װ��ʱװ��ָ��");
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }

            MapleInventoryManipulator.equip(c, src, dst);
        } else if (dst == 0) {
            ��Ʒ������� itemjc = new ��Ʒ�������();
            IItem item = c.getPlayer().getInventory(type).getItem(src);
            int itemided = item.getItemId();
            String items1 = itemjc.getid();
            if (items1.contains("," + itemided + ",")) {
                c.getPlayer().dropMessage(1, "����Ʒ��ֹ������");
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            MapleInventoryManipulator.drop(c, type, src, quantity);
            //����װ��
        } else {
            //�ƶ���Ʒ
            IItem item = c.getPlayer().getInventory(type).getItem(src);
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int itemided = item.getItemId();
            if (itemided < 2000000 && !ii.isCash(itemided)) {
                Equip nEquip = (Equip) item;
                c.getPlayer().dropMessage(2, " : ��������������������������������������������������������������������������������������");
                c.getPlayer().dropMessage(2, "\t\t\tӵ���� : " + c.getPlayer().getName());
                c.getPlayer().dropMessage(2, "\t\t\tװ������ : " + MapleItemInformationProvider.getInstance().getName(itemided));
                c.getPlayer().dropMessage(2, "\t\t\t�����ȼ� : " + ii.getReqLevel(itemided));
                c.getPlayer().dropMessage(2, "\t\t\tװ���ȼ� : " + nEquip.getEquipLevel());
                c.getPlayer().dropMessage(2, "\t\t\t��ʯ��Ƕ : " + nEquip.getMpR());
                //c.getPlayer().dropMessage(2, "\t\t\tװ���;� : " + nEquip.getHpR());
                c.getPlayer().dropMessage(2, "\t\t\t���Ҿ� : " + nEquip.getUpgradeSlots());
                if (nEquip.getStr() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t���� : " + nEquip.getStr());
                }
                if (nEquip.getDex() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t���� : " + nEquip.getDex());
                }
                if (nEquip.getInt() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t���� : " + nEquip.getInt());
                }
                if (nEquip.getLuk() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t���� : " + nEquip.getLuk());
                }
                if (nEquip.getHp() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\tMaxHP : " + nEquip.getHp());
                }
                if (nEquip.getMp() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\tMaxMP : " + nEquip.getMp());
                }

                if (nEquip.getWatk() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t������ : " + nEquip.getWatk());
                }

                if (nEquip.getMatk() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\tħ���� : " + nEquip.getMatk());
                }

                if (nEquip.getWdef() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t���������� : " + nEquip.getWdef());
                }

                if (nEquip.getMdef() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\tħ�������� : " + nEquip.getMdef());
                }

                if (nEquip.getAcc() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t������ : " + nEquip.getAcc());
                }

                if (nEquip.getAvoid() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t�ر��� : " + nEquip.getAvoid());
                }

                if (nEquip.getHands() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t�ּ� : " + nEquip.getHands());
                }

                if (nEquip.getSpeed() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t�ƶ��ٶ� : " + nEquip.getSpeed());
                }

                if (nEquip.getJump() > 0) {
                    c.getPlayer().dropMessage(2, "\t\t\t��Ծ�� : " + nEquip.getJump());
                }
                String mxmxdDaKongFuMo = item.getDaKongFuMo();
                if (mxmxdDaKongFuMo != null && mxmxdDaKongFuMo.length() > 0) {
                    ���װ����׸�ħ��Ϣ(mxmxdDaKongFuMo, c.getPlayer());

                }
                c.getPlayer().dropMessage(2, "\t\t\t���� :  " + MapleParty.��������);
                c.getPlayer().dropMessage(2, " : ��������������������������������������������������������������������������������������");
            }
            if (c.getPlayer().getGMLevel() > 0 && gui.Start.ConfigValuesMap.get("�ű����뿪��") <= 0) {
                c.getPlayer().dropMessage(5, "��Ʒ���� : " + itemided);
                //MapleItemInformationProvider.getInstance().getName(itemided);
            }
            MapleInventoryManipulator.move(c, type, src, dst);
        }
        //c.getPlayer().dropMessage(5, "2..." + dst);
        c.getPlayer().ˢ������װ����ħ��������();
        //c.getPlayer().saveToDB(false, false);
        NPCScriptManager.getInstance().dispose(c);
        c.sendPacket(MaplePacketCreator.enableActions());
    }

    public static final void ItemSort(final LittleEndianAccessor slea, final MapleClient c) {
        long nowTimestamp = System.currentTimeMillis();
        if (nowTimestamp - c.getPlayer().����������ȴ > 3000) {
            c.getPlayer().updateTick(slea.readInt());
            final MapleInventoryType pInvType = MapleInventoryType.getByType(slea.readByte());
            if (pInvType == MapleInventoryType.UNDEFINED) {
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            final MapleInventory pInv = c.getPlayer().getInventory(pInvType);
            boolean sorted = false;

            while (!sorted) {
                final byte freeSlot = (byte) pInv.getNextFreeSlot();
                if (freeSlot != -1) {
                    byte itemSlot = -1;
                    for (byte i = (byte) (freeSlot + 1); i <= pInv.getSlotLimit(); i++) {
                        if (pInv.getItem(i) != null) {
                            itemSlot = i;
                            break;
                        }
                    }
                    if (itemSlot > 0) {
                        MapleInventoryManipulator.move(c, pInvType, itemSlot, freeSlot);
                    } else {
                        sorted = true;
                    }
                } else {
                    sorted = true;
                }
            }
            c.sendPacket(MaplePacketCreator.finishedSort(pInvType.getType()));
            c.sendPacket(MaplePacketCreator.enableActions());
            c.getPlayer().saveToDB(false, false);
            c.getPlayer().dropMessage(1, "���������ɹ���");
            c.getPlayer().����������ȴ = nowTimestamp;
        } else {
            c.getPlayer().dropMessage(1, "���Ե�Ƭ��������������");
            //�⿨
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
        }
    }

    public static final void ItemGather(final LittleEndianAccessor slea, final MapleClient c) {//�ɿ�������
        c.getPlayer().updateTick(slea.readInt());
        final byte mode = slea.readByte();
        final MapleInventoryType invType = MapleInventoryType.getByType(mode);
        MapleInventory Inv = c.getPlayer().getInventory(invType);

        final List<IItem> itemMap = new LinkedList<IItem>();
        for (IItem item : Inv.list()) {
            itemMap.add(item.copy()); // clone all  items T___T.
        }
        for (IItem itemStats : itemMap) {
            if (itemStats.getItemId() != 5110000) {
                MapleInventoryManipulator.removeById(c, invType, itemStats.getItemId(), itemStats.getQuantity(), true, false);
            }
        }

        final List<IItem> sortedItems = sortItems(itemMap);
        for (IItem item : sortedItems) {
            if (item.getItemId() != 5110000) {
                MapleInventoryManipulator.addFromDrop(c, item, false);
            }
        }
        c.sendPacket(MaplePacketCreator.finishedGather(mode));
        c.sendPacket(MaplePacketCreator.enableActions());
        itemMap.clear();
        sortedItems.clear();
    }

    private static final List<IItem> sortItems(final List<IItem> passedMap) {
        final List<Integer> itemIds = new ArrayList<Integer>(); // empty list.
        for (IItem item : passedMap) {
            itemIds.add(item.getItemId()); // ��������id���ӵ�Ҫ����Ŀ��б��С�
        }
        Collections.sort(itemIds); // ���ָ�������ID

        final List<IItem> sortedList = new LinkedList<IItem>(); // �����б�pl0x��3��

        for (Integer val : itemIds) {
            for (IItem item : passedMap) {
                if (val == item.getItemId()) { // ���ÿ���������ҵ�ƥ��ĵ�һ��ֵ��
                    sortedList.add(item);
                    passedMap.remove(item);
                    break;
                }
            }
        }
        return sortedList;
    }

    //��������
    public static final boolean UseRewardItem(final byte slot, final int itemId, final MapleClient c, final MapleCharacter chr) {
//        final IItem toUse = c.getPlayer().getInventory(GameConstants.getInventoryType(itemId)).getItem(slot);
//        c.sendPacket(MaplePacketCreator.enableActions());
//        if (toUse != null && toUse.getQuantity() >= 1 && toUse.getItemId() == itemId) {
//            if (chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot() > -1 && chr.getInventory(MapleInventoryType.USE).getNextFreeSlot() > -1 && chr.getInventory(MapleInventoryType.SETUP).getNextFreeSlot() > -1 && chr.getInventory(MapleInventoryType.ETC).getNextFreeSlot() > -1) {
//                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
//                final Pair<Integer, List<StructRewardItem>> rewards = ii.getRewardItem(itemId);
//
//                if (rewards != null && rewards.getLeft() > 0) {
//                    boolean rewarded = false;
//                    while (!rewarded) {
//                        for (StructRewardItem reward : rewards.getRight()) {
//                            if (reward.prob > 0 && Randomizer.nextInt(rewards.getLeft()) < reward.prob) { // Total prob
//                                if (GameConstants.getInventoryType(reward.itemid) == MapleInventoryType.EQUIP) {
//                                    final IItem item = ii.getEquipById(reward.itemid);
//                                    if (reward.period > 0) {
//                                        item.setExpiration(System.currentTimeMillis() + (reward.period * 60 * 60 * 10));
//                                    }
//                                    MapleInventoryManipulator.addbyItem(c, item);
//                                } else {
//                                    MapleInventoryManipulator.addById(c, reward.itemid, reward.quantity, (byte) 0);
//                                }
//                                MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(itemId), itemId, 1, false, false);
//
//                                // c.sendPacket(MaplePacketCreator.showRewardItemAnimation(reward.itemid, reward.effect));
//                                //  chr.getMap().broadcastMessage(chr, MaplePacketCreator.showRewardItemAnimation(reward.itemid, reward.effect, chr.getId()), false);
//                                rewarded = true;
//                                return true;
//                            }
//                        }
//                    }
//                } else {
//                    chr.dropMessage(6, "Unknown error.");
//                }
//            } else {
//                chr.dropMessage(6, "λ�ò���");
//            }
//        }
        c.sendPacket(MaplePacketCreator.enableActions());
        NPCScriptManager.getInstance().dispose(c);
        NPCScriptManager.getInstance().start(c, 1204033, itemId);
        return true;
    }

    public static final void QuestKJ(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || !chr.isAlive() || chr.getCSPoints(2) < 200) {
            chr.dropMessage(1, "��û���㹻�ĵ��þ���");
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        final byte action = (byte) (slea.readByte() + 1);
        short quest = slea.readShort();
        if (quest < 0) { //questid 50000 and above, WILL cast to negative, this was tested.
            quest += 65536; //probably not the best fix, but whatever
        }
        if (chr == null) {
            return;
        }
        final MapleQuest q = MapleQuest.getInstance(quest);
        switch (action) {
            /*
             * case 0: { // Restore lost item chr.updateTick(slea.readInt());
             * final int itemid = slea.readInt();
             * MapleQuest.getInstance(quest).RestoreLostItem(chr, itemid);
             * break; } case 1: { // Start Quest final int npc = slea.readInt();
             * q.start(chr, npc); break; }
             */
            case 2: { // Complete Quest
                final int npc = slea.readInt();
                //chr.updateTick(slea.readInt());

                // if (slea.available() >= 4) {
                //      q.complete(chr, npc, slea.readInt());
                //  } else {
                q.complete(chr, npc);
                //  }
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
            /*
             * case 3: { // Forefit Quest if
             * (GameConstants.canForfeit(q.getId())) { q.forfeit(chr); } else {
             * chr.dropMessage(1, "You may not forfeit this quest."); } break; }
             * case 4: { // Scripted Start Quest final int npc = slea.readInt();
             * slea.readInt(); NPCScriptManager.getInstance().startQuest(c, npc,
             * quest); break; } case 5: { // Scripted End Quest final int npc =
             * slea.readInt(); NPCScriptManager.getInstance().endQuest(c, npc,
             * quest, false);
             * c.sendPacket(MaplePacketCreator.showSpecialEffect(9)); //
             * Quest completion chr.getMap().broadcastMessage(chr,
             * MaplePacketCreator.showSpecialEffect(chr.getId(), 9), false);
             * break; }
             */
        }
        chr.modifyCSPoints(2, -200);

    }

    public static final void UseItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {

        if (chr == null || !chr.isAlive() || chr.getMapId() == 749040100 || chr.getMap() == null) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }

        if (chr.getMapId() == 100000203 && MapleParty.����ɱ� > 0) {
            chr.dropMessage(5, "��Ѿ���ʼ�������ͼ��ֹʹ��������Ʒ��");
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        /**
         * <����ͼ����������Ʒ��ȴ��consumeItemCoolTime>
         */
        final long time = System.currentTimeMillis();
        if (chr.getNextConsume() > time) {
            chr.dropMessage(5, "���޷�ʹ�ã����Ժ����ԡ�");
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        c.getPlayer().updateTick(slea.readInt());
        final byte slot = (byte) slea.readShort();
        final int itemId = slea.readInt();
        final IItem toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);

        /*if (toUse.getItemId() == 2430008) {
            chr.dropMessage(5, "2430008��");
        }*/
        if (toUse.getItemId() == 2050004) {
            long nowTimestamp = System.currentTimeMillis();
            if (nowTimestamp - c.getPlayer().��������ҩ < gui.Start.ConfigValuesMap.get("��������ҩ") * 1000) {
                c.sendPacket(MaplePacketCreator.enableActions());
                chr.dropMessage(5, "������ʹ����������ҩ��");
                return;
            } else {
                c.getPlayer().��������ҩ = nowTimestamp;
            }
        }
        if (toUse.getItemId() == 2000004) {
            long nowTimestamp = System.currentTimeMillis();
            if (nowTimestamp - c.getPlayer().����ҩˮ < gui.Start.ConfigValuesMap.get("����ҩˮ") * 1000) {
                c.sendPacket(MaplePacketCreator.enableActions());
                chr.dropMessage(5, "������ʹ������ҩˮ��");
                return;
            } else {
                c.getPlayer().����ҩˮ = nowTimestamp;
            }
        }
        if (toUse.getItemId() == 2000005) {
            long nowTimestamp = System.currentTimeMillis();
            if (nowTimestamp - c.getPlayer().����ҩˮ < gui.Start.ConfigValuesMap.get("����ҩˮ") * 1000) {
                c.sendPacket(MaplePacketCreator.enableActions());
                chr.dropMessage(5, "������ʹ�ó���ҩˮ��");
                return;
            } else {
                c.getPlayer().����ҩˮ = nowTimestamp;
            }
        }
        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        if (!FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit()) || chr.getMapId() == 610030600) { //��ʦ������
            if (MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyTo(chr)) {
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                if (chr.getMap().getConsumeItemCoolTime() > 0) {
                    chr.setNextConsume(time + (chr.getMap().getConsumeItemCoolTime() * 1000));
                }
            }

        } else {
            c.sendPacket(MaplePacketCreator.enableActions());
        }
    }

    public static final void UseReturnScroll(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        //����ѩ����Ϣ��
        if (!chr.isAlive() || chr.getMapId() == 749040100) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        c.getPlayer().updateTick(slea.readInt());
        final byte slot = (byte) slea.readShort();
        final int itemId = slea.readInt();
        final IItem toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        //��Ʒ���Ƿ�С��1��ID�Ƿ�һ��
        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        //������ʿ���뺯
//        if (itemId == 2031000) {
//            c.getPlayer().changeMap(682000000, 0);
//        }
        if (chr.getMapId() >= 120000000 && chr.getMapId() <= 120010000) {
            switch (itemId) {
                case 2030001:
                    c.getPlayer().changeMap(104000000, 0);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                case 2030002:
                    c.getPlayer().changeMap(101000000, 0);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                case 2030003:
                    c.getPlayer().changeMap(102000000, 0);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                case 2030004:
                    c.getPlayer().changeMap(100000000, 0);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                case 2030005:
                    c.getPlayer().changeMap(103000000, 0);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                case 2030006:
                    c.getPlayer().changeMap(105040300, 0);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                case 2030007:
                    c.getPlayer().changeMap(211041500, 0);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                default:
                    break;
            }
            if (MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyReturnScroll(chr)) {
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
            } else {
                c.sendPacket(MaplePacketCreator.enableActions());
            }
        } else if (MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyReturnScroll(chr)) {
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
        } else {
            c.sendPacket(MaplePacketCreator.enableActions());
        }
        //if (!FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit())) {
//        } else {
//            c.sendPacket(MaplePacketCreator.enableActions());
//        }
    }

    //�Ŵ󾵣���������
    public static final void UseMagnify(final LittleEndianAccessor slea, final MapleClient c) {
        c.getPlayer().updateTick(slea.readInt());
        final IItem magnify = c.getPlayer().getInventory(MapleInventoryType.USE).getItem((byte) slea.readShort());
        final IItem toReveal = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((byte) slea.readShort());
        if (magnify == null || toReveal == null) {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            return;
        }
        final Equip eqq = (Equip) toReveal;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final int reqLevel = ii.getReqLevel(eqq.getItemId()) / 10;
        if (eqq.getState() == 1 && (magnify.getItemId() == 2460003 || (magnify.getItemId() == 2460002 && reqLevel <= 12) || (magnify.getItemId() == 2460001 && reqLevel <= 7) || (magnify.getItemId() == 2460000 && reqLevel <= 3))) {
            final List<List<StructPotentialItem>> pots = new LinkedList<List<StructPotentialItem>>(ii.getAllPotentialInfo().values());
            /* int new_state = Math.abs(eqq.getPotential1());
            if (new_state > 7 || new_state < 5) { //luls
                new_state = 5;
            }
            final int lines = (eqq.getPotential2() != 0 ? 3 : 2);
            while (eqq.getState() != new_state) {
                //31001 = haste, 31002 = door, 31003 = se, 31004 = hb
                for (int i = 0; i < lines; i++) { //2 or 3 line
                    boolean rewarded = false;
                    while (!rewarded) {
                        StructPotentialItem pot = pots.get(Randomizer.nextInt(pots.size())).get(reqLevel);
                        if (pot != null && pot.reqLevel / 10 <= reqLevel && GameConstants.optionTypeFits(pot.optionType, eqq.getItemId()) && GameConstants.potentialIDFits(pot.potentialID, new_state, i)) { //optionType
                            //have to research optionType before making this truely sea-like
                            if (i == 0) {
                                eqq.setPotential1(pot.potentialID);
                            } else if (i == 1) {
                                eqq.setPotential2(pot.potentialID);
                            } else if (i == 2) {
                                eqq.setPotential3(pot.potentialID);
                            }
                            rewarded = true;
                        }
                    }
                }
            }*/
            c.sendPacket(MaplePacketCreator.scrolledItem(magnify, toReveal, false, true));
            // c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getPotentialReset(c.getPlayer().getId(), eqq.getPosition()));
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, magnify.getPosition(), (short) 1, false);
        } else {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            return;
        }
    }

    /**
     * <�Ҿ�>
     */
    public static final boolean UseUpgradeScroll(final byte slot, final byte dst, final byte ws, final MapleClient c, final MapleCharacter chr) {
        return UseUpgradeScroll(slot, dst, ws, c, chr, 0);
    }

    public static final boolean UseUpgradeScroll(final byte slot, final byte dst, final byte ws, final MapleClient c, final MapleCharacter chr, final int vegas) {
        boolean whiteScroll = false; //ʹ�ð�ɫ���᣿
        boolean legendarySpirit = false; // ���澫����?
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

        if ((ws & 2) == 2) {
            whiteScroll = true;
        }

        IEquip toScroll;
        if (dst < 0) {
            toScroll = (IEquip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
        } else { // legendary spirit
            legendarySpirit = true;
            toScroll = (IEquip) chr.getInventory(MapleInventoryType.EQUIP).getItem(dst);
        }
        if (toScroll == null) {
            return false;
        }
        final byte oldLevel = toScroll.getLevel();
        final byte oldEnhance = toScroll.getEnhance();
        final byte oldState = toScroll.getState();
        final byte oldFlag = toScroll.getFlag();
        final byte oldSlots = toScroll.getUpgradeSlots();

        if (oldLevel >= 50) {
            chr.dropMessage(1, "�޷�������ǿ��װ����");
            c.sendPacket(MaplePacketCreator.enableActions());
            return false;
        }

        boolean checkIfGM = c.getPlayer().isGM();
        IItem scroll = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        if (scroll == null) {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            return false;
        }
        if (!GameConstants.isSpecialScroll(scroll.getItemId()) && !GameConstants.isCleanSlate(scroll.getItemId()) && !GameConstants.isEquipScroll(scroll.getItemId()) && !GameConstants.isPotentialScroll(scroll.getItemId())) {
            if (toScroll.getUpgradeSlots() < 1) {
                c.sendPacket(MaplePacketCreator.getInventoryFull());
                return false;
            }
        } else if (GameConstants.isEquipScroll(scroll.getItemId())) {
            if (toScroll.getUpgradeSlots() >= 1 || toScroll.getEnhance() >= 100 || vegas > 0 || ii.isCash(toScroll.getItemId())) {
                c.sendPacket(MaplePacketCreator.getInventoryFull());
                return false;
            }
        } else if (GameConstants.isPotentialScroll(scroll.getItemId())) {
            if (toScroll.getState() >= 1 || (toScroll.getLevel() == 0 && toScroll.getUpgradeSlots() == 0) || vegas > 0 || ii.isCash(toScroll.getItemId())) {
                c.sendPacket(MaplePacketCreator.getInventoryFull());
                return false;
            }
        }
        if (!GameConstants.canScroll(toScroll.getItemId()) && !GameConstants.isChaosScroll(toScroll.getItemId())) {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            return false;
        }
        if ((GameConstants.isCleanSlate(scroll.getItemId()) || GameConstants.isTablet(scroll.getItemId()) || GameConstants.isChaosScroll(scroll.getItemId())) && (vegas > 0 || ii.isCash(toScroll.getItemId()))) {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            return false;
        }
        if (GameConstants.isTablet(scroll.getItemId()) && toScroll.getDurability() < 0) { //not a durability item
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            return false;
        } else if (!GameConstants.isTablet(scroll.getItemId()) && toScroll.getDurability() >= 0) {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            return false;
        }

        IItem wscroll = null;

        // Anti cheat and validation
        List<Integer> scrollReqs = ii.getScrollReqs(scroll.getItemId());
        if (scrollReqs.size() > 0 && !scrollReqs.contains(toScroll.getItemId())) {
            c.sendPacket(MaplePacketCreator.getInventoryFull());
            return false;
        }

        if (whiteScroll) {
            //ף������
            wscroll = chr.getInventory(MapleInventoryType.USE).findById(2340000);
            if (wscroll == null) {
                whiteScroll = false;
            }
        }
        //������ͷ��???
        if (scroll.getItemId() == 2049115 && toScroll.getItemId() != 1003068) {
            //ravana
            return false;
        }
        if (GameConstants.isTablet(scroll.getItemId())) {
            switch (scroll.getItemId() % 1000 / 100) {
                case 0: //1h
                    if (GameConstants.isTwoHanded(toScroll.getItemId()) || !GameConstants.isWeapon(toScroll.getItemId())) {
                        return false;
                    }
                    break;
                case 1: //2h
                    if (!GameConstants.isTwoHanded(toScroll.getItemId()) || !GameConstants.isWeapon(toScroll.getItemId())) {
                        return false;
                    }
                    break;
                case 2: //armor
                    if (GameConstants.isAccessory(toScroll.getItemId()) || GameConstants.isWeapon(toScroll.getItemId())) {
                        return false;
                    }
                    break;
                case 3: //accessory
                    if (!GameConstants.isAccessory(toScroll.getItemId()) || GameConstants.isWeapon(toScroll.getItemId())) {
                        return false;
                    }
                    break;
            }
        } else if (!GameConstants.isAccessoryScroll(scroll.getItemId()) && !GameConstants.isChaosScroll(scroll.getItemId()) && !GameConstants.isCleanSlate(scroll.getItemId()) && !GameConstants.isEquipScroll(scroll.getItemId()) && !GameConstants.isPotentialScroll(scroll.getItemId())) {
            if (!ii.canScroll(scroll.getItemId(), toScroll.getItemId())) {
                return false;
            }
        }
        if (GameConstants.isAccessoryScroll(scroll.getItemId()) && !GameConstants.isAccessory(toScroll.getItemId())) {
            return false;
        }
        if (scroll.getQuantity() <= 0) {
            return false;
        }

        if (legendarySpirit && vegas == 0) {
            if (chr.getSkillLevel(SkillFactory.getSkill(1003)) <= 0 && chr.getSkillLevel(SkillFactory.getSkill(10001003)) <= 0 && chr.getSkillLevel(SkillFactory.getSkill(20001003)) <= 0 && chr.getSkillLevel(SkillFactory.getSkill(20011003)) <= 0 && chr.getSkillLevel(SkillFactory.getSkill(30001003)) <= 0) {
                AutobanManager.getInstance().addPoints(c, 50, 120000, "�����õġ����澫�񡱡�");
                return false;
            }
        }

        // Scroll Success/ Failure/ Curse
        final IEquip scrolled = (IEquip) ii.scrollEquipWithId(toScroll, scroll, whiteScroll, chr, vegas, checkIfGM);
        ScrollResult scrollSuccess;
        if (scrolled == null) {
            scrollSuccess = IEquip.ScrollResult.CURSE;
        } else if (scrolled.getLevel() > oldLevel || scrolled.getEnhance() > oldEnhance || scrolled.getState() > oldState || scrolled.getFlag() > oldFlag) {
            scrollSuccess = IEquip.ScrollResult.SUCCESS;
        } else if ((GameConstants.isCleanSlate(scroll.getItemId()) && scrolled.getUpgradeSlots() > oldSlots)) {
            scrollSuccess = IEquip.ScrollResult.SUCCESS;
        } else {
            scrollSuccess = IEquip.ScrollResult.FAIL;
        }

        // Update
        chr.getInventory(MapleInventoryType.USE).removeItem(scroll.getPosition(), (short) 1, false);
        if (whiteScroll) {
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, wscroll.getPosition(), (short) 1, false, false);
        }

        if (scrollSuccess == IEquip.ScrollResult.CURSE) {
            c.sendPacket(MaplePacketCreator.scrolledItem(scroll, toScroll, true, false));
            if (dst < 0) {
                chr.getInventory(MapleInventoryType.EQUIPPED).removeItem(toScroll.getPosition());
            } else {
                chr.getInventory(MapleInventoryType.EQUIP).removeItem(toScroll.getPosition());
            }
        } else if (vegas == 0) {
            c.sendPacket(MaplePacketCreator.scrolledItem(scroll, scrolled, false, false));
        }

        chr.getMap().broadcastMessage(chr, MaplePacketCreator.getScrollEffect(c.getPlayer().getId(), scrollSuccess, legendarySpirit), vegas == 0);

        // equipped item was scrolled and changed
        if (dst < 0 && (scrollSuccess == IEquip.ScrollResult.SUCCESS || scrollSuccess == IEquip.ScrollResult.CURSE) && vegas == 0) {
            chr.equipChanged();
        }
        return true;
    }

    public static final void UseCatchItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        c.getPlayer().updateTick(slea.readInt());
        final byte slot = (byte) slea.readShort();
        final int itemid = slea.readInt();
        final MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());
        final IItem toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);

        if (toUse != null && toUse.getQuantity() > 0 && toUse.getItemId() == itemid && mob != null) {
            switch (itemid) {
                //����֮��.���ж��Ĺ���װ��ȥ��ʹ��õ������������ħ��֮�顣
                case 2270004: {
                    final MapleMap map = chr.getMap();

                    if (mob.getHp() <= mob.getMobMaxHp() / 2) {
                        map.broadcastMessage(MaplePacketCreator.catchMonster(mob.getId(), itemid, (byte) 1));
                        map.killMonster(mob, chr, true, false, (byte) 0);
                        MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemid, 1, false, false);
                        MapleInventoryManipulator.addById(c, 4001169, (short) 1, (byte) 0);
                    } else {
                        map.broadcastMessage(MaplePacketCreator.catchMonster(mob.getId(), itemid, (byte) 0));
                        chr.dropMessage(5, "���������������ǿ��,�޷���׽.");
                    }
                    break;
                }
                //�ٳ�ʯ.���չ�������ʱ��Ϊ��ʯ�ģ���������������ߡ������ǰ�͸���ġ�
                case 2270002: {
                    final MapleMap map = chr.getMap();

                    if (mob.getHp() <= mob.getMobMaxHp() / 2) {
                        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.catchMonster(mob.getId(), itemid, (byte) 1));
                        //  map.broadcastMessage(MaplePacketCreator.catchMonster(mob.getId(), itemid, (byte) 1));
                        map.killMonster(mob, chr, true, false, (byte) 0);
                        MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemid, 1, false, false);
                        c.getPlayer().setAPQScore(c.getPlayer().getAPQScore() + 1);
                        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.updateAriantPQRanking(c.getPlayer().getName(), c.getPlayer().getAPQScore(), false));
                    } else {
                        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.catchMonster(mob.getId(), itemid, (byte) 0));
                        c.sendPacket(MaplePacketCreator.catchMob(mob.getId(), itemid, (byte) 0));
                        //  map.broadcastMessage(MaplePacketCreator.catchMonster(mob.getId(), itemid, (byte) 0));
                        // chr.dropMessage(5, "���������������ǿ��,�޷���׽.");
                    }
                    break;
                }
                //��������ˮ
                case 2270000: {
                    if (mob.getId() != 9300101) {
                        break;
                    }
                    final MapleMap map = c.getPlayer().getMap();

                    map.broadcastMessage(MaplePacketCreator.catchMonster(mob.getId(), itemid, (byte) 1));
                    map.killMonster(mob, chr, true, false, (byte) 0);
                    MapleInventoryManipulator.addById(c, 1902000, (short) 1, null, (byte) 0);
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemid, 1, false, false);
                    break;
                }
                //������ħ����
                case 2270003: {
                    if (mob.getId() != 9500320) {
                        break;
                    }
                    final MapleMap map = c.getPlayer().getMap();

                    if (mob.getHp() <= mob.getMobMaxHp() / 2) {
                        map.broadcastMessage(MaplePacketCreator.catchMonster(mob.getId(), itemid, (byte) 1));
                        map.killMonster(mob, chr, true, false, (byte) 0);
                        MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemid, 1, false, false);

                        final double ���Ӹ��� = Math.ceil(Math.random() * 100);
                        final double ���� = Math.ceil(Math.random() * 100);
                        if (���Ӹ��� >= 50) {
                            MapleInventoryManipulator.addById(chr.getClient(), 2028168, (short) 1, (byte) 0);
                        }
                        if (���� >= 98) {
                            MapleMonster mob1 = MapleLifeFactory.getMonster(9400014); // guaiwu id
                            c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob1, c.getPlayer().getPosition());
                            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, 20, "[������] : ������ĳ��ʥ��Сɽ������~~~"));
                        }
                    } else {
                        map.broadcastMessage(MaplePacketCreator.catchMonster(mob.getId(), itemid, (byte) 0));
                        chr.dropMessage(5, "���޷���׽[��·�Ե�ʥ��¹]");
                    }
                    break;
                }
            }
        }
        c.sendPacket(MaplePacketCreator.enableActions());
    }

    public static final void UseMountFood(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        c.getPlayer().updateTick(slea.readInt());
        final byte slot = (byte) slea.readShort();
        final int itemid = slea.readInt(); //2260000 usually����	�ָ�ƣ�Ͳ�ҩ
        final IItem toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        final MapleMount mount = chr.getMount();

        if (toUse != null && toUse.getQuantity() > 0 && toUse.getItemId() == itemid && mount != null) {
            final int fatigue = mount.getFatigue();

            boolean levelup = false;
            mount.setFatigue((byte) -30);

            if (fatigue > 0) {
                mount.increaseExp();
                final int level = mount.getLevel();
                if (mount.getExp() >= GameConstants.getMountExpNeededForLevel(level + 1) && level < 31) {
                    mount.setLevel((byte) (level + 1));
                    levelup = true;
                }
            }
            chr.getMap().broadcastMessage(MaplePacketCreator.updateMount(chr, levelup));
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
        }
        c.sendPacket(MaplePacketCreator.enableActions());
    }

    public static final void UseScriptedNPCItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        c.getPlayer().updateTick(slea.readInt());
        final byte slot = (byte) slea.readShort();
        final int itemId = slea.readInt();
        final IItem toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        long expiration_days = 0;
        int mountid = 0;

        //if (toUse != null && toUse.getQuantity() >= 1 && toUse.getItemId() == itemId) {
        //if (toUse != null && toUse.getItemId() == itemId) {
        switch (toUse.getItemId()) {
            //������
            case 2430007: {
                final MapleInventory inventory = chr.getInventory(MapleInventoryType.SETUP);
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);

                if (inventory.countById(3994102) >= 20 // 	������N	
                        && inventory.countById(3994103) >= 20 // ������E
                        && inventory.countById(3994104) >= 20 // ������W
                        && inventory.countById(3994105) >= 20) { // ������S	
                    MapleInventoryManipulator.addById(c, 2430008, (short) 1, (byte) 0); //�ƽ�����
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.SETUP, 3994102, 20, false, false);
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.SETUP, 3994103, 20, false, false);
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.SETUP, 3994104, 20, false, false);
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.SETUP, 3994105, 20, false, false);
                } else {
                    MapleInventoryManipulator.addById(c, 2430007, (short) 1, (byte) 0); // Blank Compass
                }
                NPCScriptManager.getInstance().start(c, 2084001);
                break;
            }
            //�ƽ�����

            case 2430008: {
                MapleMap map0;
                MapleMap map1;
                MapleMap map2;
                MapleMap map3;
                MapleMap map4;
                map0 = c.getChannelServer().getMapFactory().getMap(390001000);
                map1 = c.getChannelServer().getMapFactory().getMap(390001001);
                map2 = c.getChannelServer().getMapFactory().getMap(390001002);
                map3 = c.getChannelServer().getMapFactory().getMap(390001003);
                map4 = c.getChannelServer().getMapFactory().getMap(390001004);
                if (map0.getCharactersSize() == 0) {
                    c.getPlayer().changeMap(390001000, 0);
                    MapleInventoryManipulator.addById(c, 2430008, (short) -1, (byte) 0);
                    World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, c.getChannel(), "[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��"));
                    sendMsgToQQGroup("[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��");
                } else if (map1.getCharactersSize() == 0) {
                    c.getPlayer().changeMap(390001001, 0);
                    MapleInventoryManipulator.addById(c, 2430008, (short) -1, (byte) 0);
                    World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, c.getChannel(), "[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��"));
                    sendMsgToQQGroup("[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��");
                } else if (map2.getCharactersSize() == 0) {
                    c.getPlayer().changeMap(390001002, 0);
                    MapleInventoryManipulator.addById(c, 2430008, (short) -1, (byte) 0);
                    World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, c.getChannel(), "[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��"));
                    sendMsgToQQGroup("[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��");
                } else if (map3.getCharactersSize() == 0) {
                    c.getPlayer().changeMap(390001003, 0);
                    MapleInventoryManipulator.addById(c, 2430008, (short) -1, (byte) 0);
                    World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, c.getChannel(), "[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��"));
                    sendMsgToQQGroup("[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��");
                } else if (map4.getCharactersSize() == 0) {
                    c.getPlayer().changeMap(390001004, 0);
                    MapleInventoryManipulator.addById(c, 2430008, (short) -1, (byte) 0);
                    World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, c.getChannel(), "[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��"));
                    sendMsgToQQGroup("[����]:��� " + c.getPlayer().getName() + " ʹ�ûƽ����̣����뱦��ͼ��");
                } else {
                    c.getPlayer().dropMessage(5, "���е�ͼ����ʹ���У�����Ի���Ƶ�����ԣ������Ե�һ����ʹ�á�");
                }
                break;

//                    chr.saveLocation(SavedLocationType.RICHIE);
//                    MapleMap map;
//                    boolean warped = false;
//
//                    for (int i = 390001000; i <= 390001004; i++) {
//                        map = c.getChannelServer().getMapFactory().getMap(i);
//
//                        if (map.getCharactersSize() == 0) {
//                            chr.changeMap(map, map.getPortal(0));
//                            warped = true;
//                            break;
//                        }
//                    }
//                    if (warped) { // Removal of gold compass
//                        MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, 2430008, 1, false, false);
//                    } else { // Or mabe some other message.
//                        c.getPlayer().dropMessage(5, "���е�ͼ����ʹ�ã����Ժ�����.");
//                    }
            }
            case 2430112: //miracle cube
                if (c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
                    if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430112) >= 25) {
                        if (MapleInventoryManipulator.checkSpace(c, 2049400, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, 2430112, 25, true, false)) {
                            MapleInventoryManipulator.addById(c, 2049400, (short) 1, (byte) 0);
                        } else {
                            c.getPlayer().dropMessage(5, "�������ռ�.");
                        }
                    } else if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430112) >= 10) {
                        if (MapleInventoryManipulator.checkSpace(c, 2049400, 1, "") && MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, 2430112, 10, true, false)) {
                            MapleInventoryManipulator.addById(c, 2049401, (short) 1, (byte) 0);
                        } else {
                            c.getPlayer().dropMessage(5, "�������ռ�.");
                        }
                    } else {
                        c.getPlayer().dropMessage(5, "һ��Ǳ�ڵĹ�������Ҫ��10��Ƭ�Σ�25��Ǳ�ڵĹ�����.");
                    }
                } else {
                    c.getPlayer().dropMessage(5, "�������ռ�.");
                }
                break;
            case 2430036: //croco 1 day����ʹ��
                mountid = 1027;
                expiration_days = 1;
                break;
            case 2430037: //black scooter 1 day
                mountid = 1028;
                expiration_days = 1;
                break;
            case 2430038: //pink scooter 1 day
                mountid = 1029;
                expiration_days = 1;
                break;
            case 2430039: //clouds 1 day
                mountid = 1030;
                expiration_days = 1;
                break;
            case 2430040: //balrog 1 day
                mountid = 1031;
                expiration_days = 1;
                break;
            case 2430053: //croco 30 day
                mountid = 1027;
                expiration_days = 1;
                break;
            case 2430054: //black scooter 30 day
                mountid = 1028;
                expiration_days = 30;
                break;
            case 2430055: //pink scooter 30 day
                mountid = 1029;
                expiration_days = 30;
                break;
            case 2430056: //mist rog 30 day
                mountid = 1035;
                expiration_days = 30;
                break;
            //race kart 30 day? unknown 2430057
            case 2430072: //ZD tiger 7 day
                mountid = 1034;
                expiration_days = 7;
                break;
            case 2430073: //lion 15 day
                mountid = 1036;
                expiration_days = 15;
                break;
            case 2430074: //unicorn 15 day
                mountid = 1037;
                expiration_days = 15;
                break;
            case 2430075: //low rider 15 day
                mountid = 1038;
                expiration_days = 15;
                break;
            case 2430076: //red truck 15 day
                mountid = 1039;
                expiration_days = 15;
                break;
            case 2430077: //gargoyle 15 day
                mountid = 1040;
                expiration_days = 15;
                break;
            case 2430080: //shinjo 20 day
                mountid = 1042;
                expiration_days = 20;
                break;
            case 2430082: //orange mush 7 day
                mountid = 1044;
                expiration_days = 7;
                break;
            case 2430091: //nightmare 10 day
                mountid = 1049;
                expiration_days = 10;
                break;
            case 2430092: //yeti 10 day
                mountid = 1050;
                expiration_days = 10;
                break;
            case 2430093: //ostrich 10 day
                mountid = 1051;
                expiration_days = 10;
                break;
            case 2430101: //pink bear 10 day
                mountid = 1052;
                expiration_days = 10;
                break;
            case 2430102: //transformation robo 10 day
                mountid = 1053;
                expiration_days = 10;
                break;
            case 2430103: //chicken 30 day
                mountid = 1054;
                expiration_days = 30;
                break;
            case 2430117: //lion 1 year
                mountid = 1036;
                expiration_days = 365;
                break;
            case 2430118: //red truck 1 year
                mountid = 1039;
                expiration_days = 365;
                break;
            case 2430119: //gargoyle 1 year
                mountid = 1040;
                expiration_days = 365;
                break;
            case 2430120: //unicorn 1 year
                mountid = 1037;
                expiration_days = 365;
                break;
            case 2430136: //owl 30 day
                mountid = 1069;
                expiration_days = 30;
                break;
            case 2430137: //owl 1 year
                mountid = 1069;
                expiration_days = 365;
                break;
            case 2430201: //giant bunny 60 day
                mountid = 1096;
                expiration_days = 60;
                break;
            case 2430228: //tiny bunny 60 day
                mountid = 1101;
                expiration_days = 60;
                break;
            case 2430229: //bunny rickshaw 60 day
                mountid = 1102;
                expiration_days = 60;
                break;
            default: // Up to no good
                System.out.println("UseScriptedNPCItem");
                return;

        }

        if (mountid > 0) {
            mountid += (GameConstants.isAran(c.getPlayer().getJob()) ? 20000000 : (GameConstants.isEvan(c.getPlayer().getJob()) ? 20010000 : (GameConstants.isKOC(c.getPlayer().getJob()) ? 10000000 : (GameConstants.isResist(c.getPlayer().getJob()) ? 30000000 : 0))));
            if (c.getPlayer().getSkillLevel(mountid) > 0) {
                c.getPlayer().dropMessage(5, "���Ѿ�ӵ�����������.");
            } else if (expiration_days > 0) {
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
                c.getPlayer().changeSkillLevel(SkillFactory.getSkill(mountid), (byte) 1, (byte) 1, System.currentTimeMillis() + (long) (expiration_days * 24 * 60 * 60 * 1000));
                c.getPlayer().dropMessage(5, "�Ѿ��ﵽ�ļ���.");
            }
        }
        if ((itemId >= 2022570 && itemId <= 2022573) && (itemId >= 2022575 && itemId <= 2022578) && itemId >= 2022580 && itemId <= 2022583 && itemId == 2022336 && itemId == 2022615 && itemId == 2022503 && itemId == 2022514 && itemId == 2022428 && itemId == 2022504 && itemId == 2022505 && itemId == 2022506 && itemId == 2022507 && itemId == 2029999) {//2022504
            if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() >= 1) {
                //     UsePenguinBox(c, itemId);
            } else {
                c.getPlayer().dropMessage(1, "������");
            }
        }
        c.sendPacket(MaplePacketCreator.enableActions());
    }

    public static void UsePenguinBox(final LittleEndianAccessor slea, MapleClient c) {
        final List<Integer> gift = new ArrayList<>();
        final byte slot = (byte) slea.readShort();
        final int item = slea.readInt();
        final IItem toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);

        if (toUse.getItemId() != item) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        } else if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() <= 1) {
            c.getPlayer().dropMessage(1, "�����������޷������Ʒ");
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        switch (item) {
//            case 2028168://ʥ������
//                NPCScriptManager.getInstance().start(c, 9209101, 2028168);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                NPCScriptManager.getInstance().dispose(c);
//                return;
//            case 2022504://С���
//                NPCScriptManager.getInstance().start(c, 9300011, 101);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                NPCScriptManager.getInstance().dispose(c);
//                return;
//            case 2022505://�к��
//                NPCScriptManager.getInstance().start(c, 9300011, 102);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                NPCScriptManager.getInstance().dispose(c);
//                return;
//            case 2022506://����
//                NPCScriptManager.getInstance().start(c, 9300011, 103);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                NPCScriptManager.getInstance().dispose(c);
//                return;
//            case 2022507://�ش���
//                NPCScriptManager.getInstance().start(c, 9300011, 104);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                NPCScriptManager.getInstance().dispose(c);
//                return;
//            case 2022465:
//                NPCScriptManager.getInstance().start(c, 1, 2022465);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                NPCScriptManager.getInstance().dispose(c);
//                return;
//            case 2022466:
//                NPCScriptManager.getInstance().start(c, 1, 2022466);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                NPCScriptManager.getInstance().dispose(c);
//                return;
//            case 2022467:
//                NPCScriptManager.getInstance().start(c, 1, 2022467);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                NPCScriptManager.getInstance().dispose(c);
//                return;
//            case 2022468:
//                NPCScriptManager.getInstance().start(c, 1, 2022468);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                NPCScriptManager.getInstance().dispose(c);
//                return;
//
//            case 2022524://10
//                NPCScriptManager.getInstance().start(c, 9900004, 9999998);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                return;
//            case 4280000://10
//                NPCScriptManager.getInstance().start(c, 1, 4280000);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                return;
//            case 4280001://10
//                NPCScriptManager.getInstance().start(c, 1, 4280001);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                return;
//            case 2029997://10
//                NPCScriptManager.getInstance().start(c, 1, 2029997);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                return;
//            case 2029996://10
//                NPCScriptManager.getInstance().start(c, 1, 2029996);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                return;
//            case 2029995://10
//                NPCScriptManager.getInstance().start(c, 1, 2029995);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                return;
//            case 2029994://10
//                NPCScriptManager.getInstance().start(c, 5, 6004);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                return;
//            case 2029993://10
//                NPCScriptManager.getInstance().start(c, 5, 6005);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                return;
//            case 2029992://10
//                NPCScriptManager.getInstance().start(c, 5, 6006);
//                c.sendPacket(MaplePacketCreator.enableActions());
//                return;

            case 2022570:
                gift.add(1302119);
                gift.add(1312045);
                gift.add(1322073);
                break;
            case 2022571:
                gift.add(1372053);
                gift.add(1382070);
                break;
            case 2022572:
                gift.add(1462066);
                gift.add(1452073);
                break;
            case 2022573:
                gift.add(1332088);
                gift.add(1472089);
                break;
            case 2022575:
                gift.add(1040145);
                gift.add(1041148);
                break;
            case 2022576:
                gift.add(1050155);
                gift.add(1051191);
                break;
            case 2022577:
                gift.add(1040146);
                gift.add(1041149);
                break;
            case 2022578:
                gift.add(1040147);
                gift.add(1041150);
                break;
            case 2022580:
                gift.add(1072399);
                gift.add(1060134);
                gift.add(1061156);
                break;
            case 2022581:
                gift.add(1072400);
                break;
            case 2022582:
                gift.add(1072401);
                gift.add(1060135);
                gift.add(1061157);
                break;
            case 2022583:
                gift.add(1072402);
                gift.add(1060136);
                gift.add(1061158);
                break;

            default:
                c.sendPacket(MaplePacketCreator.enableActions());
                NPCScriptManager.getInstance().dispose(c);
                NPCScriptManager.getInstance().start(c, 1204033, item);
                return;

        }
        //if (gift.isEmpty() && item != 2022428 && item != 2022465 && item != 2022466 && item != 2022467 && item != 2022468 && item != 2022336 && item != 2022615 && item != 2022503 && item != 2022514 && item != 2022428 && item != 2022504 && item != 2022505 && item != 2022506 && item != 2022507 && item != 2022508 && item != 2022509 && item != 2022510 && item != 2022511 && item != 2022512 && item != 2022513 && item != 2022514 && item != 2022515 && item != 2022516 && item != 2022517 && item != 2029999) {//2022504
        //   c.getPlayer().dropMessage(1, item + " �����޷��򿪣�����ϵZEV��" + ����QQ);
        //} else {
        int rand = (java.util.concurrent.ThreadLocalRandom.current().nextInt(gift.size()));
        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
        MapleInventoryManipulator.addById(c, gift.get(rand), (short) 1, (byte) 0);
        gift.clear();
        // }
        c.sendPacket(MaplePacketCreator.enableActions());
    }

    //���ӱ���
    public static void SunziBF(final LittleEndianAccessor slea, final MapleClient c) {
        slea.readInt();
        byte slot = (byte) slea.readShort();
        int itemid = slea.readInt();
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        IItem item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
        if ((item == null) || (item.getItemId() != itemid) || (c.getPlayer().getLevel() > 255)) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        //int expGained = ii.getExpCache(itemid) * c.getChannelServer().getExpRate();
        int expGained = ii.getExpCache(itemid);
        c.getPlayer().gainExp(expGained, true, false, false);
        c.sendPacket(MaplePacketCreator.enableActions());
        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
    }

    public static final void UseSummonBag(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (!chr.isAlive()) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        c.getPlayer().updateTick(slea.readInt());
        final byte slot = (byte) slea.readShort();
        final int itemId = slea.readInt();
        final IItem toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);

        if (chr.getMapId() >= 910000000 && chr.getMapId() <= 910000022) {
            c.sendPacket(MaplePacketCreator.enableActions());
            c.getPlayer().dropMessage(5, "�г��޷�ʹ���ٻ���.");
            return;
        }
        if (toUse != null && toUse.getQuantity() >= 1 && toUse.getItemId() == itemId) {
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
            if (c.getPlayer().isGM() || !FieldLimitType.SummoningBag.check(chr.getMap().getFieldLimit())) {
                final List<Pair<Integer, Integer>> toSpawn = MapleItemInformationProvider.getInstance().getSummonMobs(itemId);
                if (toSpawn == null) {
                    c.sendPacket(MaplePacketCreator.enableActions());
                    return;
                }
                MapleMonster ht;
                int type = 0;
                for (int i = 0; i < toSpawn.size(); i++) {
                    if (Randomizer.nextInt(99) <= toSpawn.get(i).getRight()) {
                        ht = MapleLifeFactory.getMonster(toSpawn.get(i).getLeft());
                        if (ht.getId() == 9300166) {
                            chr.spawnBomb();
                        } else {
                            chr.getMap().spawnMonster_sSack(ht, chr.getPosition(), type);
                        }
                    }
                }
            }
        }
        c.sendPacket(MaplePacketCreator.enableActions());
    }

    public static void UseTreasureChest(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        /*
         * [B4 00] [19 00] [C0 4E 41 00] [01] - û��������ȶ���ʾʹ�õ�� 800 �����ľ��� 1
         * [B4 00] [19 00] [C0 4E 41 00] [00]
         */
        short slot = slea.readShort();
        int itemid = slea.readInt();
        boolean useCash = slea.readByte() > 0;
        IItem toUse = chr.getInventory(MapleInventoryType.ETC).getItem((byte) slot);
        if (toUse == null || toUse.getQuantity() <= 0 || toUse.getItemId() != itemid) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        int reward;
        int keyIDforRemoval;
        String box, key;
        int price;
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        switch (toUse.getItemId()) {
            case 4280000: //�������֮��
                reward = RandomRewards.getInstance().getGoldBoxReward();
                keyIDforRemoval = 5490000; //������ȶ�
                box = "�������֮��";
                key = "������ȶ�";
                price = 5000;
                break;
            case 4280001: //��������֮��
                reward = RandomRewards.getInstance().getSilverBoxReward();
                keyIDforRemoval = 5490001;//�������ȶ�
                box = "��������֮��";
                key = "�������ȶ�";
                price = 5000;
                break;
            default: // Up to no good
                return;
        }

        // Get the quantity
        int amount = 1;
        switch (reward) {
            case 2000004: //����ҩˮ
                amount = 200; // Elixir
                break;
            case 2000005://����ҩˮ
                amount = 100; // Power Elixir
                break;
        }   //�жϵ��þ��Ƿ��㹻
        if (useCash && chr.getCSPoints(2) < price) {
            chr.dropMessage(1, "����ȯ����" + price + "��");
            c.sendPacket(MaplePacketCreator.enableActions());
            //�ж��Ƿ����ȶ�
        } else if (chr.getInventory(MapleInventoryType.CASH).countById(keyIDforRemoval) < 0) {
            chr.dropMessage(1, "����" + box + "��Ҫ" + key + "���뵽�̳ǹ���");
            c.sendPacket(MaplePacketCreator.enableActions());
            //������ȶȣ��� ���ĵ��þ� ���þ�> ����ļ۸�
        } else if (chr.getInventory(MapleInventoryType.CASH).countById(keyIDforRemoval) > 0 || (useCash && chr.getCSPoints(2) > price)) {
            IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, reward, (short) amount);
            if (item == null) {
                chr.dropMessage(1, "����ʧ�ܣ�������һ�Ρ�\r\n��ı�����������");
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            //ɾ������
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.ETC, slot, (short) 1, true);
            //��������ĵ��þ�
            if (useCash) {
                //��ȥ��ҵ��þ�
                chr.modifyCSPoints(2, -price, true);
            } else {
                //��֮ɾ���ȶ�
                MapleInventoryManipulator.removeById(c, MapleInventoryType.CASH, keyIDforRemoval, 1, true, false);
            }
            if ("�������֮��".equals(box)) {
                c.sendPacket(MaplePacketCreator.enableActions());
                NPCScriptManager.getInstance().dispose(c);
                NPCScriptManager.getInstance().start(c, 1204033, 4280000);
            } else if ("��������֮��".equals(box)) {
                c.sendPacket(MaplePacketCreator.enableActions());
                NPCScriptManager.getInstance().dispose(c);
                NPCScriptManager.getInstance().start(c, 1204033, 4280001);

            }
            //��������ߵķ��
            //c.sendPacket(MaplePacketCreator.getShowItemGain(reward, (short) amount, true));
            /*byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            if (rareness > 0 || reward > 0) {
                //ˢ������
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[" + box + "] : " + c.getPlayer().getName(), " ��� [ " + ii.getName(item.getItemId()) + " ]}�����һ��ϲ���������ɡ�", item, rareness, c.getChannel()));
            }*/
        } else {
            chr.dropMessage(5, "����" + box + "ʧ��\r\n�����Ƿ���" + key + "\r\n���ߵ��þ��Ƿ���" + price + "�㡣");
            c.sendPacket(MaplePacketCreator.enableActions());
        }
    }

    public static final void UseCashItem(final LittleEndianAccessor slea, final MapleClient c) {
        final List<Integer> gift = new ArrayList<>();
        final byte slot = (byte) slea.readShort();
        final int itemId = slea.readInt();
        final IItem toUse = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(slot);
        if (toUse == null || toUse.getItemId() != itemId || toUse.getQuantity() < 1) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        boolean used = false, cc = false;
        if (c.getPlayer().getMapId() == 180000001) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        switch (itemId) {
            case 5060002: {
                slea.skip(4);
                short itemId2 = slea.readShort();
                int item = NPCConversationManager.�жϱ���λ�ô���(c.getPlayer().getId(), 4, itemId2);
                c.sendPacket(MaplePacketCreator.enableActions());
                NPCScriptManager.getInstance().dispose(c);
                switch (item) {
                    //���ִ�
                    case 4170000:
                        NPCScriptManager.getInstance().start(c, 9050001, 1);
                        used = true;
                        break;
                    //ħ������
                    case 4170001:
                        NPCScriptManager.getInstance().start(c, 9050000, 1);
                        used = true;
                        break;
                    //��������
                    case 4170002:
                        NPCScriptManager.getInstance().start(c, 9050003, 1);
                        used = true;
                        break;
                    //��ʿ����
                    case 4170003:
                        NPCScriptManager.getInstance().start(c, 9050002, 1);
                        used = true;
                        break;
                    //���֮�ǡ�������������������������������������
                    /*case 4170004:
                        NPCScriptManager.getInstance().start(c, 9050001, 1);
                        break;*/
                    //��߳�
                    case 4170005:
                        NPCScriptManager.getInstance().start(c, 9050005, 1);
                        used = true;
                        break;
                    //ˮ������
                    case 4170007:
                        NPCScriptManager.getInstance().start(c, 9050006, 1);
                        used = true;
                        break;
                    //ŵ����˹
                    case 4170009:
                        NPCScriptManager.getInstance().start(c, 9310093, 1);
                        used = true;
                        break;
                    //�¼���
                    /*case 4170010:
                        NPCScriptManager.getInstance().start(c, 9050001, 1);
                        break;*/
                    //��Ҷ��
                    case 4170011:
                        NPCScriptManager.getInstance().start(c, 9310096, 1);
                        used = true;
                        break;
                    //�ۺʹ�
                    /*case 4170013:
                        NPCScriptManager.getInstance().start(c, 9050001, 1);
                        break;*/
                    //������
                    /* case 4170015:
                        NPCScriptManager.getInstance().start(c, 9050001, 1);
                        break;*/
                    //�Ŵ�����
                    /*case 4170016:
                        NPCScriptManager.getInstance().start(c, 9050001, 1);
                        break;*/
                    //��������
                    /*case 4170017:
                        NPCScriptManager.getInstance().start(c, 9050001, 1);
                        break;*/
                    //����֮��
                    case 4170018:
                        NPCScriptManager.getInstance().start(c, 9050004, 1);
                        used = true;
                        break;
                    default:
                        c.getPlayer().dropMessage(1, "û�в鵽�÷������ĵ���ط�����Ϣ������ϵСZ��");
                        break;
                }

            }
            break;
            //ԥ԰�߼�˲��֮ʯ
            case 5042000:
                c.getPlayer().changeMap(701000200);
                used = true;
                break;
            //�߼�˲��֮ʯ
            case 5042001:
                c.getPlayer().changeMap(741000000);
                used = true;
                break;
            //һ���������۾�����ɫ��
            case 5152100: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� != 100) {
                    if (c.getPlayer().getGender() == 0) {
                        int ���� = c.getPlayer().getFace() % 100 + 20000;
                        c.setFace(����);
                    } else {
                        int ���� = c.getPlayer().getFace() % 100 + 21000;
                        c.setFace(����);
                    }
                } else if (c.getPlayer().getGender() == 0) {
                    int ���� = c.getPlayer().getFace() % 100 + 20000;
                    c.setFace(���� + 800);
                } else {
                    int ���� = c.getPlayer().getFace() % 100 + 21000;
                    c.setFace(���� + 800);
                }
                used = true;
                break;
            }
            //һ���������۾�����ɫ��
            case 5152101: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� != 100) {
                    if (c.getPlayer().getGender() == 0) {
                        int ���� = c.getPlayer().getFace() % 100 + 20000;
                        c.setFace(���� + 100);
                    } else {
                        int ���� = c.getPlayer().getFace() % 100 + 21000;
                        c.setFace(���� + 100);
                    }
                } else if (c.getPlayer().getGender() == 0) {
                    int ���� = c.getPlayer().getFace() % 100 + 20000;
                    c.setFace(���� + 800);
                } else {
                    int ���� = c.getPlayer().getFace() % 100 + 21000;
                    c.setFace(���� + 800);
                }
                used = true;
                break;
            }
            //һ���������۾�����ɫ��
            case 5152102: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� != 100) {
                    if (c.getPlayer().getGender() == 0) {
                        int ���� = c.getPlayer().getFace() % 100 + 20000;
                        c.setFace(���� + 200);
                    } else {
                        int ���� = c.getPlayer().getFace() % 100 + 21000;
                        c.setFace(���� + 200);
                    }
                } else if (c.getPlayer().getGender() == 0) {
                    int ���� = c.getPlayer().getFace() % 100 + 20000;
                    c.setFace(���� + 800);
                } else {
                    int ���� = c.getPlayer().getFace() % 100 + 21000;
                    c.setFace(���� + 800);
                }
                used = true;
                break;
            }
            //һ���������۾�����ɫ��
            case 5152103: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� != 100) {
                    if (c.getPlayer().getGender() == 0) {
                        int ���� = c.getPlayer().getFace() % 100 + 20000;
                        c.setFace(���� + 300);
                    } else {
                        int ���� = c.getPlayer().getFace() % 100 + 21000;
                        c.setFace(���� + 300);
                    }
                } else if (c.getPlayer().getGender() == 0) {
                    int ���� = c.getPlayer().getFace() % 100 + 20000;
                    c.setFace(���� + 800);
                } else {
                    int ���� = c.getPlayer().getFace() % 100 + 21000;
                    c.setFace(���� + 800);
                }
                used = true;
                break;
            }
            //һ���������۾�����ɫ��
            case 5152104: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� != 100) {
                    if (c.getPlayer().getGender() == 0) {
                        int ���� = c.getPlayer().getFace() % 100 + 20000;
                        c.setFace(���� + 400);
                    } else {
                        int ���� = c.getPlayer().getFace() % 100 + 21000;
                        c.setFace(���� + 400);
                    }
                } else if (c.getPlayer().getGender() == 0) {
                    int ���� = c.getPlayer().getFace() % 100 + 20000;
                    c.setFace(���� + 800);
                } else {
                    int ���� = c.getPlayer().getFace() % 100 + 21000;
                    c.setFace(���� + 800);
                }
                used = true;
                break;
            }
            //һ���������۾�����ĸ��ɫ��
            case 5152105: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� != 100) {
                    if (c.getPlayer().getGender() == 0) {
                        int ���� = c.getPlayer().getFace() % 100 + 20000;
                        c.setFace(���� + 500);
                    } else {
                        int ���� = c.getPlayer().getFace() % 100 + 21000;
                        c.setFace(���� + 500);
                    }
                } else if (c.getPlayer().getGender() == 0) {
                    int ���� = c.getPlayer().getFace() % 100 + 20000;
                    c.setFace(���� + 800);
                } else {
                    int ���� = c.getPlayer().getFace() % 100 + 21000;
                    c.setFace(���� + 800);
                }
                used = true;
                break;
            }
            //һ���������۾�����ɫ��
            case 5152106: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� != 100) {
                    if (c.getPlayer().getGender() == 0) {
                        int ���� = c.getPlayer().getFace() % 100 + 20000;
                        c.setFace(���� + 600);
                    } else {
                        int ���� = c.getPlayer().getFace() % 100 + 21000;
                        c.setFace(���� + 600);
                    }
                } else if (c.getPlayer().getGender() == 0) {
                    int ���� = c.getPlayer().getFace() % 100 + 20000;
                    c.setFace(���� + 800);
                } else {
                    int ���� = c.getPlayer().getFace() % 100 + 21000;
                    c.setFace(���� + 800);
                }
                used = true;
                break;
            }
            //һ���������۾�����ˮ��ɫ��
            case 5152107: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� != 100) {
                    if (c.getPlayer().getGender() == 0) {
                        int ���� = c.getPlayer().getFace() % 100 + 20000;
                        c.setFace(���� + 700);
                    } else {
                        int ���� = c.getPlayer().getFace() % 100 + 21000;
                        c.setFace(���� + 700);
                    }
                } else if (c.getPlayer().getGender() == 0) {
                    int ���� = c.getPlayer().getFace() % 100 + 20000;
                    c.setFace(���� + 800);
                } else {
                    int ���� = c.getPlayer().getFace() % 100 + 21000;
                    c.setFace(���� + 800);
                }
                used = true;
                break;
            }
            //�ƶ��ֿ�
            case 5450008:
            case 5450009: {
                NPCScriptManager.getInstance().start(c, 1012009, 0);
                c.sendPacket(MaplePacketCreator.enableActions());
                break;
            }
            //2000������
            case 5201000: {
                //c.getPlayer().gainBeans(2000);
                //c.getPlayer().dropMessage(1, "�ɹ��һ� 2000 ������");
                c.sendPacket(MaplePacketCreator.updateBeans(c.getPlayer().getId(), 2000));
                //MapleCharacter player = c.getPlayer();
                //c.sendPacket(MaplePacketCreator.getCharInfo(player));
                // player.getMap().removePlayer(player);
                //player.getMap().addPlayer(player);
                used = true;
                break;
            }
            //500������
            case 5201001: {
                c.getPlayer().gainBeans(500);
                c.getPlayer().dropMessage(1, "�ɹ��һ� 500 ������");
                MapleCharacter player = c.getPlayer();
                c.sendPacket(MaplePacketCreator.getCharInfo(player));
                player.getMap().removePlayer(player);
                player.getMap().addPlayer(player);
                used = true;
                break;
            }
            //3000������
            case 5201002: {
                c.getPlayer().gainBeans(3000);
                c.getPlayer().dropMessage(1, "�ɹ��һ� 3000 ������");
                MapleCharacter player = c.getPlayer();
                c.sendPacket(MaplePacketCreator.getCharInfo(player));
                player.getMap().removePlayer(player);
                player.getMap().addPlayer(player);
                used = true;
                break;
            }
            //20������
            case 5201004: {
                c.getPlayer().gainBeans(20);
                c.getPlayer().dropMessage(1, "�ɹ��һ� 20 ������");
                MapleCharacter player = c.getPlayer();
                c.sendPacket(MaplePacketCreator.getCharInfo(player));
                player.getMap().removePlayer(player);
                player.getMap().addPlayer(player);
                used = true;
                break;
            }
            //50������
            case 5201005: {
                c.getPlayer().gainBeans(50);
                c.getPlayer().dropMessage(1, "�ɹ��һ� 50 ������");
                MapleCharacter player = c.getPlayer();
                c.sendPacket(MaplePacketCreator.getCharInfo(player));
                player.getMap().removePlayer(player);
                player.getMap().addPlayer(player);
                used = true;
                break;
            }
            //�߼�˲��֮ʯ
            case 5041000:
            //����ʯ
            case 5040000:
            //���ֳ��
            case 5040001: {
                if (MapleParty.�������� == 0) {
                    if (slea.readByte() == 0) {
                        final MapleMap target = c.getChannelServer().getMapFactory().getMap(slea.readInt());
                        if (target != null) {
                            if ((itemId == 5041000 && c.getPlayer().isRockMap(target.getId())) || (itemId != 5041000 && c.getPlayer().isRegRockMap(target.getId()))) {
                                if (!FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit()) && !FieldLimitType.VipRock.check(target.getFieldLimit()) && c.getPlayer().getEventInstance() == null) { //Makes sure this map doesn't have a forced return map
                                    c.getPlayer().changeMap(target, target.getPortal(0));
                                    used = true;
                                }
                            }
                        }
                    } else {
                        final MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(slea.readMapleAsciiString());
                        if (victim != null && !victim.isGM() && c.getPlayer().getEventInstance() == null && victim.getEventInstance() == null) {
                            if (!FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit()) && !FieldLimitType.VipRock.check(c.getChannelServer().getMapFactory().getMap(victim.getMapId()).getFieldLimit())) {
                                if (itemId == 5041000 || (victim.getMapId() / 100000000) == (c.getPlayer().getMapId() / 100000000)) { // Viprock or same continent
                                    c.getPlayer().changeMap(victim.getMap(), victim.getMap().findClosestSpawnpoint(victim.getPosition()));
                                    c.getPlayer().dropMessage(5, "" + c.getPlayer().getName() + " ���ٵ�������ߡ�");
                                    used = true;
                                }
                            }
                        }
                    }
                } else {
                    c.getPlayer().dropMessage(1, "�������˳���ʱ�䣬�޷�ʹ�ô˹��ܡ�");
                }
                break;
            }
            //ϴ���������
            case 5050000: {
                List<Pair<MapleStat, Integer>> statupdate = new ArrayList<>(2);
                final int apto = slea.readInt();
                final int apfrom = slea.readInt();

                if (apto == apfrom) {
                    break; // Hack
                }
                final int job = c.getPlayer().getJob();
                final PlayerStats playerst = c.getPlayer().getStat();
                used = true;

                if (apfrom == 0x2000 && apto != 0x8000) {
                    c.sendPacket(MaplePacketCreator.enableActions());
                    return;
                } else if (apfrom == 0x8000 && apto != 0x2000) {
                    c.sendPacket(MaplePacketCreator.enableActions());
                    return;
                }
                switch (apto) { //���Ե�����ֵ
                    case 0x100: // str
                        if (playerst.getStr() >= 999) {
                            used = false;
                        }
                        break;
                    case 0x200: // dex
                        if (playerst.getDex() >= 999) {
                            used = false;
                        }
                        break;
                    case 0x400: // int
                        if (playerst.getInt() >= 999) {
                            used = false;
                        }
                        break;
                    case 0x800: // luk
                        if (playerst.getLuk() >= 999) {
                            used = false;
                        }
                        break;
                    case 0x2000: // hp
                        if (playerst.getMaxHp() >= 30000) {
                            used = false;
                        }
                        break;
                    case 0x8000: // mp
                        if (playerst.getMaxMp() >= 30000) {
                            used = false;
                        }
                        break;
                }
                switch (apfrom) { // AP to
                    case 0x100: // str
                        if (playerst.getStr() <= 4) {
                            used = false;
                        }
                        break;
                    case 0x200: // dex
                        if (playerst.getDex() <= 4) {
                            used = false;
                        }
                        break;
                    case 0x400: // int
                        if (playerst.getInt() <= 4) {
                            used = false;
                        }
                        break;
                    case 0x800: // luk
                        if (playerst.getLuk() <= 4) {
                            used = false;
                        }
                        break;
                    case 0x2000: // hp
                        // if (playerst.getMaxMp() < ((c.getPlayer().getLevel() * 14) + 134) || c.getPlayer().getHpApUsed() <= 0 || c.getPlayer().getHpApUsed() >= 10000 || playerst.getMaxHp() < 1) {
                        if (playerst.getMaxHp() >= 30000) {
                            used = false;
                        }
                        break;
                    case 0x8000: // mp
                        //  if (playerst.getMaxMp() < ((c.getPlayer().getLevel() * 14) + 134) || c.getPlayer().getHpApUsed() <= 0 || c.getPlayer().getHpApUsed() >= 10000) {
                        if (playerst.getMaxMp() >= 30000) {
                            used = false;
                        }
                        break;
                }
                if (used) {
                    switch (apto) { // AP to
                        case 0x100: { // str
                            final int toSet = playerst.getStr() + 1;
                            playerst.setStr((short) toSet);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.STR, toSet));
                            break;
                        }
                        case 0x200: { // dex
                            final int toSet = playerst.getDex() + 1;
                            playerst.setDex((short) toSet);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.DEX, toSet));
                            break;
                        }
                        case 0x400: { // int
                            final int toSet = playerst.getInt() + 1;
                            playerst.setInt((short) toSet);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.INT, toSet));
                            break;
                        }
                        case 0x800: { // luk
                            final int toSet = playerst.getLuk() + 1;
                            playerst.setLuk((short) toSet);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.LUK, toSet));
                            break;
                        }
                        case 0x2000: // hp
                            short maxhp = playerst.getMaxHp();

                            if (job == 0) { // Beginner
                                maxhp += Randomizer.rand(8, 12);
                            } else if ((job >= 100 && job <= 132) || (job >= 3200 && job <= 3212)) { // Warrior
                                ISkill improvingMaxHP = SkillFactory.getSkill(1000001);
                                int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                                maxhp += Randomizer.rand(20, 25);
                                if (improvingMaxHPLevel >= 1) {
                                    maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                }
                            } else if ((job >= 200 && job <= 232) || (GameConstants.isEvan(job))) { // Magician
                                maxhp += Randomizer.rand(10, 20);
                            } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 3300 && job <= 3312)) { // Bowman
                                maxhp += Randomizer.rand(16, 20);
                            } else if ((job >= 500 && job <= 522) || (job >= 3500 && job <= 3512)) { // Pirate
                                ISkill improvingMaxHP = SkillFactory.getSkill(5100000);
                                int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                                maxhp += Randomizer.rand(18, 22);
                                if (improvingMaxHPLevel >= 1) {
                                    maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                }
                            } else if (job >= 1500 && job <= 1512) { // Pirate
                                ISkill improvingMaxHP = SkillFactory.getSkill(15100000);
                                int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                                maxhp += Randomizer.rand(18, 22);
                                if (improvingMaxHPLevel >= 1) {
                                    maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                }
                            } else if (job >= 1100 && job <= 1112) { // Soul Master
                                ISkill improvingMaxHP = SkillFactory.getSkill(11000000);
                                int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                                maxhp += Randomizer.rand(36, 42);
                                if (improvingMaxHPLevel >= 1) {
                                    maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                }
                            } else if (job >= 1200 && job <= 1212) { // Flame Wizard
                                maxhp += Randomizer.rand(15, 21);
                            } else if (job >= 2000 && job <= 2112) { // Aran
                                maxhp += Randomizer.rand(40, 50);
                            } else { // GameMaster
                                maxhp += Randomizer.rand(50, 100);
                            }
                            maxhp = (short) Math.min(30000, Math.abs(maxhp));
                            c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() + 1));
                            playerst.setMaxHp(maxhp);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, (int) maxhp));
                            break;

                        case 0x8000: // mp
                            short maxmp = playerst.getMaxMp();

                            if (job == 0) { // Beginner
                                maxmp += Randomizer.rand(6, 8);
                            } else if (job >= 100 && job <= 132) { // Warrior
                                maxmp += Randomizer.rand(5, 7);
                            } else if ((job >= 200 && job <= 232) || (GameConstants.isEvan(job)) || (job >= 3200 && job <= 3212)) { // Magician
                                ISkill improvingMaxMP = SkillFactory.getSkill(2000001);
                                int improvingMaxMPLevel = c.getPlayer().getSkillLevel(improvingMaxMP);
                                maxmp += Randomizer.rand(18, 20);
                                if (improvingMaxMPLevel >= 1) {
                                    maxmp += improvingMaxMP.getEffect(improvingMaxMPLevel).getY() * 2;
                                }
                            } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 500 && job <= 522) || (job >= 3200 && job <= 3212) || (job >= 3500 && job <= 3512) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 1500 && job <= 1512)) { // Bowman
                                maxmp += Randomizer.rand(10, 12);
                            } else if (job >= 1100 && job <= 1112) { // Soul Master
                                maxmp += Randomizer.rand(6, 9);
                            } else if (job >= 1200 && job <= 1212) { // Flame Wizard
                                ISkill improvingMaxMP = SkillFactory.getSkill(12000000);
                                int improvingMaxMPLevel = c.getPlayer().getSkillLevel(improvingMaxMP);
                                maxmp += Randomizer.rand(18, 20);
                                if (improvingMaxMPLevel >= 1) {
                                    maxmp += improvingMaxMP.getEffect(improvingMaxMPLevel).getY() * 2;
                                }
                            } else if (job >= 2000 && job <= 2112) { // Aran
                                maxmp += Randomizer.rand(6, 9);
                            } else { // GameMaster
                                maxmp += Randomizer.rand(50, 100);
                            }
                            maxmp = (short) Math.min(30000, Math.abs(maxmp));
                            c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() + 1));
                            playerst.setMaxMp(maxmp);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, (int) maxmp));
                            break;
                    }
                    switch (apfrom) { // AP from
                        case 256: { // str
                            final int toSet = playerst.getStr() - 1;
                            playerst.setStr((short) toSet);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.STR, toSet));
                            break;
                        }
                        case 512: { // dex
                            final int toSet = playerst.getDex() - 1;
                            playerst.setDex((short) toSet);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.DEX, toSet));
                            break;
                        }
                        case 1024: { // int
                            final int toSet = playerst.getInt() - 1;
                            playerst.setInt((short) toSet);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.INT, toSet));
                            break;
                        }
                        case 2048: { // luk
                            final int toSet = playerst.getLuk() - 1;
                            playerst.setLuk((short) toSet);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.LUK, toSet));
                            break;
                        }
                        case 8192: // HP
                            short maxhp = playerst.getMaxHp();
                            if (job == 0) { // Beginner
                                maxhp -= 12;
                            } else if (job >= 100 && job <= 132) { // Warrior
                                ISkill improvingMaxHP = SkillFactory.getSkill(1000001);
                                int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                                maxhp -= 24;
                                if (improvingMaxHPLevel >= 1) {
                                    maxhp -= improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                }
                            } else if (job >= 200 && job <= 232) { // Magician
                                maxhp -= 10;
                            } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 3300 && job <= 3312) || (job >= 3500 && job <= 3512)) { // Bowman, Thief
                                maxhp -= 15;
                            } else if (job >= 500 && job <= 522) { // Pirate
                                ISkill improvingMaxHP = SkillFactory.getSkill(5100000);
                                int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                                maxhp -= 15;
                                if (improvingMaxHPLevel > 0) {
                                    maxhp -= improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                }
                            } else if (job >= 1500 && job <= 1512) { // Pirate
                                ISkill improvingMaxHP = SkillFactory.getSkill(15100000);
                                int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                                maxhp -= 15;
                                if (improvingMaxHPLevel > 0) {
                                    maxhp -= improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                }
                            } else if (job >= 1100 && job <= 1112) { // Soul Master
                                ISkill improvingMaxHP = SkillFactory.getSkill(11000000);
                                int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                                maxhp -= 27;
                                if (improvingMaxHPLevel >= 1) {
                                    maxhp -= improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                                }
                            } else if (job >= 1200 && job <= 1212) { // Flame Wizard
                                maxhp -= 12;
                            } else if ((job >= 2000 && job <= 2112) || (job >= 3200 && job <= 3212)) { // Aran
                                maxhp -= 40;
                            } else { // GameMaster
                                maxhp -= 20;
                            }
                            c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() - 1));
                            playerst.setHp(maxhp);
                            playerst.setMaxHp(maxhp);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.HP, (int) maxhp));
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, (int) maxhp));
                            break;
                        case 32768: // MP
                            short maxmp = playerst.getMaxMp();
                            if (job == 0) { // Beginner
                                maxmp -= 8;
                            } else if (job >= 100 && job <= 132) { // Warrior
                                maxmp -= 4;
                            } else if (job >= 200 && job <= 232) { // Magician
                                ISkill improvingMaxMP = SkillFactory.getSkill(2000001);
                                int improvingMaxMPLevel = c.getPlayer().getSkillLevel(improvingMaxMP);
                                maxmp -= 20;
                                if (improvingMaxMPLevel >= 1) {
                                    maxmp -= improvingMaxMP.getEffect(improvingMaxMPLevel).getY();
                                }
                            } else if ((job >= 500 && job <= 522) || (job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 1500 && job <= 1512) || (job >= 3300 && job <= 3312) || (job >= 3500 && job <= 3512)) { // Pirate, Bowman. Thief
                                maxmp -= 10;
                            } else if (job >= 1100 && job <= 1112) { // Soul Master
                                maxmp -= 6;
                            } else if (job >= 1200 && job <= 1212) { // Flame Wizard
                                ISkill improvingMaxMP = SkillFactory.getSkill(12000000);
                                int improvingMaxMPLevel = c.getPlayer().getSkillLevel(improvingMaxMP);
                                maxmp -= 25;
                                if (improvingMaxMPLevel >= 1) {
                                    maxmp -= improvingMaxMP.getEffect(improvingMaxMPLevel).getY();
                                }
                            } else if (job >= 2000 && job <= 2112) { // Aran
                                maxmp -= 5;
                            } else { // GameMaster
                                maxmp -= 20;
                            }
                            c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() - 1));
                            playerst.setMp(maxmp);
                            playerst.setMaxMp(maxmp);
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MP, (int) maxmp));
                            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, (int) maxmp));
                            break;
                    }
                    c.sendPacket(MaplePacketCreator.updatePlayerStats(statupdate, true, c.getPlayer().getJob()));
                }
                break;
            }
            //ϴ���ܵ�
            case 5050001:
            case 5050002:
            case 5050003:
            case 5050004: {
                int skill1 = slea.readInt();
                int skill2 = slea.readInt();

                ISkill skillSPTo = SkillFactory.getSkill(skill1);
                ISkill skillSPFrom = SkillFactory.getSkill(skill2);

                if (skillSPTo.isBeginnerSkill() || skillSPFrom.isBeginnerSkill()) {
                    break;
                }
                if ((c.getPlayer().getSkillLevel(skillSPTo) + 1 <= skillSPTo.getMaxLevel()) && c.getPlayer().getSkillLevel(skillSPFrom) > 0) {
                    c.getPlayer().changeSkillLevel(skillSPFrom, (byte) (c.getPlayer().getSkillLevel(skillSPFrom) - 1), c.getPlayer().getMasterLevel(skillSPFrom));
                    c.getPlayer().changeSkillLevel(skillSPTo, (byte) (c.getPlayer().getSkillLevel(skillSPTo) + 1), c.getPlayer().getMasterLevel(skillSPTo));
                    used = true;
                }
                break;
            }
            //����ҩˮ
            case 5156000:
                //����
                switch (c.getPlayer().getGender()) {
                    case 0:
                        c.getPlayer().setFace(20000);//�任����
                        c.getPlayer().setHair(30000);//�任����
                        c.getPlayer().setGender((byte) 1);//�任�Ա�
                        //c.getPlayer().setFame((short) 20000);
                        used = true;
                        break;
                    case 1:
                        //Ů��
                        c.getPlayer().setFace(30000);//�任����
                        c.getPlayer().setHair(30000);//�任����
                        c.getPlayer().setGender((byte) 0);//�任�Ա�
                        //c.getPlayer().setFame((short) 20000);
                        used = true;
                        break;
                    default:
                        c.getPlayer().dropMessage(5, "����������Ȳ���������Ҳ����Ů����");
                        break;
                }
                break;
            //����ȡ��
            case 5060000: {
                final IItem item = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readByte());

                if (item != null && item.getOwner().equals("")) {
                    boolean change = true;
                    for (String z : GameConstants.RESERVED) {
                        if (c.getPlayer().getName().indexOf(z) != -1 || item.getOwner().indexOf(z) != -1) {
                            change = false;
                        }
                    }
                    if (change) {
                        item.setOwner(c.getPlayer().getName());
                        c.getPlayer().forceReAddItem(item, MapleInventoryType.EQUIPPED);
                        used = true;
                    }
                }
                break;
            }
            //ħ��
            case 5062000: {
                NPCScriptManager.getInstance().start(c, 1, 5062000);
                c.sendPacket(MaplePacketCreator.enableActions());
                break;
            }
            //������
            case 5080000:
            case 5080001:
            case 5080002:
            case 5080003: {
                MapleLove love = new MapleLove(c.getPlayer(), c.getPlayer().getPosition(), c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId(), slea.readMapleAsciiString(), itemId);
                c.getPlayer().getMap().spawnLove(love);
                MapleInventoryManipulator.removeById(c, MapleInventoryType.CASH, itemId, 1, true, false);
                break;

            }
            //��������
            case 5520000: {
                final MapleInventoryType type = MapleInventoryType.getByType((byte) slea.readInt());
                final IItem item = c.getPlayer().getInventory(type).getItem((byte) slea.readInt());
                if (item != null && !ItemFlag.KARMA_EQ.check(item.getFlag()) && !ItemFlag.KARMA_USE.check(item.getFlag())) {
                    if (itemId == 5520000 && MapleItemInformationProvider.getInstance().isKarmaEnabled(item.getItemId()) || MapleItemInformationProvider.getInstance().isPKarmaEnabled(item.getItemId())) {
                        byte flag = item.getFlag();
                        if (type == MapleInventoryType.EQUIP) {
                            flag |= ItemFlag.KARMA_EQ.getValue();
                        } else {
                            flag |= ItemFlag.KARMA_USE.getValue();
                        }
                        item.setFlag(flag);
                        c.getPlayer().forceReAddItem_Flag(item, type);
                        used = true;
                    }
                }
                break;
            }
            //��������
            case 5530212: {
                final double ���� = Math.ceil(Math.random() * 500);
                if (���� == 250) {
                    final double �ӳ� = Math.ceil(Math.random() * 10);
                    c.getPlayer().getStat().setMaxHp((short) (c.getPlayer().getStat().getCurrentMaxHp() + �ӳ�));
                    MapleCharacter player = c.getPlayer();
                    c.sendPacket(MaplePacketCreator.getCharInfo(player));
                    player.getMap().removePlayer(player);
                    player.getMap().addPlayer(player);
                    World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, 20, "[��������] : ��� " + c.getPlayer().getName() + " ������������֮��������ˬ������ 1 HP����"));
                }
                c.getPlayer().addHP(30000);
                c.getPlayer().addMP(30000);
                used = true;
            }
            break;
            //ң��
            case 5470000:
                c.getPlayer().dropMessage(1, "��Ӷң��.");
                break;
            //����
            case 5570000: {
                slea.readInt();
                final Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((byte) slea.readInt());
                if (item != null) {
                    if (GameConstants.���Ӳ���ʹ���ڴ���Ʒ(item.getItemId())) {//����ʹ�õ���Ʒ
                        final double �Ӿ������ɹ��� = Math.ceil(Math.random() * 100);
                        int �Ӿ��ɹ��� = gui.Start.ConfigValuesMap.get("���������ɹ���");
                        if (�Ӿ��ɹ��� >= �Ӿ������ɹ���) {
                            if (gui.Start.ConfigValuesMap.get("������������") == 0) {
                                item.setViciousHammer((byte) (item.getViciousHammer() + 1));
                            }
                            item.setUpgradeSlots((byte) (item.getUpgradeSlots() + 1));
                            c.getPlayer().dropMessage(1, "�ɹ�����װ����������");
                            c.getPlayer().forceReAddItem(item, MapleInventoryType.EQUIP);
                            used = true;
                            cc = true;
                        } else {
                            c.getPlayer().dropMessage(1, "����װ����������ʧ��");
                            c.getPlayer().forceReAddItem(item, MapleInventoryType.EQUIP);
                            used = true;
                            cc = true;
                        }
                    } else {
                        c.getPlayer().dropMessage(5, "�˵����޷�ʹ�ý���");
                        cc = true;
                    }
                }
                break;
            }
            //NPC˲��֮ʯ	
            case 5043001:
            case 5043000: {
                final short questid = slea.readShort();
                final int npcid = slea.readInt();
                final MapleQuest quest = MapleQuest.getInstance(questid);

                if (c.getPlayer().getQuest(quest).getStatus() == 1 && quest.canComplete(c.getPlayer(), npcid)) {
                    final int mapId = MapleLifeFactory.getNPCLocation(npcid);
                    if (mapId != -1) {
                        final MapleMap map = c.getChannelServer().getMapFactory().getMap(mapId);
                        if (map.containsNPC(npcid) && !FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit()) && !FieldLimitType.VipRock.check(map.getFieldLimit()) && c.getPlayer().getEventInstance() == null) {
                            c.getPlayer().changeMap(map, map.getPortal(0));
                        }
                        used = true;
                    } else {
                        c.getPlayer().dropMessage(1, "����δ֪����.");
                    }
                }
                break;
            }
            //����֮ӡ����
            case 5060001: {
                final MapleInventoryType type = MapleInventoryType.getByType((byte) slea.readInt());
                final IItem item = c.getPlayer().getInventory(type).getItem((byte) slea.readInt());
                if (item != null && item.getExpiration() == -1) {
                    byte flag = item.getFlag();
                    flag |= ItemFlag.LOCK.getValue();
                    item.setFlag(flag);

                    c.getPlayer().forceReAddItem_Flag(item, type);
                    used = true;
                }
                break;
            }
            // ����֮ӡ7
            case 5061000: {
                final MapleInventoryType type = MapleInventoryType.getByType((byte) slea.readInt());
                final IItem item = c.getPlayer().getInventory(type).getItem((byte) slea.readInt());
                if (item != null && item.getExpiration() == -1) {
                    byte flag = item.getFlag();
                    flag |= ItemFlag.LOCK.getValue();
                    item.setFlag(flag);
                    item.setExpiration(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));

                    c.getPlayer().forceReAddItem_Flag(item, type);
                    used = true;
                }
                break;
            }
            //����֮ӡ30
            case 5061001: {
                final MapleInventoryType type = MapleInventoryType.getByType((byte) slea.readInt());
                final IItem item = c.getPlayer().getInventory(type).getItem((byte) slea.readInt());
                if (item != null && item.getExpiration() == -1) {
                    byte flag = item.getFlag();
                    flag |= ItemFlag.LOCK.getValue();
                    item.setFlag(flag);
                    item.setExpiration(System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000));
                    c.getPlayer().forceReAddItem_Flag(item, type);
                    used = true;
                }
                break;
            }
            // ����֮ӡ90
            case 5061002: {
                final MapleInventoryType type = MapleInventoryType.getByType((byte) slea.readInt());
                final IItem item = c.getPlayer().getInventory(type).getItem((byte) slea.readInt());
                if (item != null && item.getExpiration() == -1) {
                    byte flag = item.getFlag();
                    flag |= ItemFlag.LOCK.getValue();
                    item.setFlag(flag);
                    item.setExpiration(System.currentTimeMillis() + (90 * 24 * 60 * 60 * 1000));
                    c.getPlayer().forceReAddItem_Flag(item, type);
                    used = true;
                }
                break;
            }
            //������
            case 5060003: {
                IItem item = c.getPlayer().getInventory(MapleInventoryType.ETC).findById(4170023);
                if (item == null || item.getQuantity() <= 0) {
                    return;
                }
                if (getIncubatedItems(c)) {
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.ETC, item.getPosition(), (short) 1, false);
                    used = true;
                }
            }
            break;
            //���
            case 5090100:
            //��Ϣ
            case 5090000: {
                final String sendTo = slea.readMapleAsciiString();
                final String msg = slea.readMapleAsciiString();
                c.getPlayer().sendNote(sendTo, msg);
                used = true;
                break;
            }
            //���ֺ�
            case 5100000: {
                c.getPlayer().getMap().broadcastMessage(MTSCSPacket.playCashSong(5100000, c.getPlayer().getName()));
                used = true;
                break;
            }
            //�Զ�����HPҩˮ����
            case 5190001:
            case 5190002:
            case 5190003:
            case 5190004:
            case 5190005:
            case 5190006:
            case 5190007:
            case 5190008:
            case 5190000: {
                final int uniqueid = (int) slea.readLong();
                MaplePet pet = c.getPlayer().getPet(0);
                int slo = 0;

                if (pet == null) {
                    break;
                }
                if (pet.getUniqueId() != uniqueid) {
                    pet = c.getPlayer().getPet(1);
                    slo = 1;
                    if (pet != null) {
                        if (pet.getUniqueId() != uniqueid) {
                            pet = c.getPlayer().getPet(2);
                            slo = 2;
                            if (pet != null) {
                                if (pet.getUniqueId() != uniqueid) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }

                PetFlag zz = PetFlag.getByAddId(itemId);
                if (zz != null && !zz.check(pet.getFlags())) {
                    pet.setFlags(pet.getFlags() | zz.getValue());
                    c.sendPacket(PetPacket.updatePet(pet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem((byte) pet.getInventoryPosition()), true));
                    c.sendPacket(MaplePacketCreator.enableActions());
                    c.sendPacket(MTSCSPacket.changePetFlag(uniqueid, true, zz.getValue()));
                    used = true;
                }
                break;
            }
            //ȡ���Զ�����ҩˮ����
            case 5191001:
            case 5191002:
            case 5191003:
            case 5191004:
            case 5191000: { // Pet Flags
                final int uniqueid = (int) slea.readLong();
                MaplePet pet = c.getPlayer().getPet(0);
                int slo = 0;

                if (pet == null) {
                    break;
                }
                if (pet.getUniqueId() != uniqueid) {
                    pet = c.getPlayer().getPet(1);
                    slo = 1;
                    if (pet != null) {
                        if (pet.getUniqueId() != uniqueid) {
                            pet = c.getPlayer().getPet(2);
                            slo = 2;
                            if (pet != null) {
                                if (pet.getUniqueId() != uniqueid) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                PetFlag zz = PetFlag.getByDelId(itemId);
                if (zz != null && zz.check(pet.getFlags())) {
                    pet.setFlags(pet.getFlags() - zz.getValue());
                    c.sendPacket(PetPacket.updatePet(pet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem((byte) pet.getInventoryPosition()), true));
                    c.sendPacket(MaplePacketCreator.enableActions());
                    c.sendPacket(MTSCSPacket.changePetFlag(uniqueid, false, zz.getValue()));
                    used = true;
                }
                break;
            }
            //ȡ������
            case 5170000: {
                MaplePet pet = c.getPlayer().getPet(0);
                int slo = 0;

                if (pet == null) {
                    break;
                }
                String nName = slea.readMapleAsciiString();
                pet.setName(nName);
                c.sendPacket(PetPacket.updatePet(pet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem((byte) pet.getInventoryPosition()), true));
                c.sendPacket(MaplePacketCreator.enableActions());
                c.getPlayer().getMap().broadcastMessage(MTSCSPacket.changePetName(c.getPlayer(), nName, slo));
                used = true;
//                }
                break;
            }
            //����ʳ��
            case 5240000:
            case 5240001:
            case 5240002:
            case 5240003:
            case 5240004:
            case 5240005:
            case 5240006:
            case 5240007:
            case 5240008:
            case 5240009:
            case 5240010:
            case 5240011:
            case 5240012:
            case 5240013:
            case 5240014:
            case 5240015:
            case 5240016:
            case 5240017:
            case 5240018:
            case 5240019:
            case 5240020:
            case 5240021:
            case 5240022:
            case 5240023:
            case 5240024:
            case 5240025:
            case 5240026:
            case 5240027:
            case 5240028: {
                MaplePet pet = c.getPlayer().getPet(0);

                if (pet == null) {
                    break;
                }
                if (!pet.canConsume(itemId)) {
                    pet = c.getPlayer().getPet(1);
                    if (pet != null) {
                        if (!pet.canConsume(itemId)) {
                            pet = c.getPlayer().getPet(2);
                            if (pet != null) {
                                if (!pet.canConsume(itemId)) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                final byte petindex = c.getPlayer().getPetIndex(pet);
                pet.setFullness(100);
                if (pet.getCloseness() < 30000) {
                    if (pet.getCloseness() + 100 > 30000) {
                        pet.setCloseness(30000);
                    } else {
                        pet.setCloseness(pet.getCloseness() + 100);
                    }
                    if (pet.getCloseness() >= GameConstants.getClosenessNeededForLevel(pet.getLevel() + 1)) {
                        pet.setLevel(pet.getLevel() + 1);
                        c.sendPacket(PetPacket.showOwnPetLevelUp(c.getPlayer().getPetIndex(pet)));
                        c.getPlayer().getMap().broadcastMessage(PetPacket.showPetLevelUp(c.getPlayer(), petindex));
                    }
                }
                c.sendPacket(PetPacket.updatePet(pet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), true));
                c.getPlayer().getMap().broadcastMessage(c.getPlayer(), PetPacket.commandResponse(c.getPlayer().getId(), (byte) 1, petindex, true, true), true);
                used = true;
                break;
            }
            //�̵�������
            case 5230000: {
                final int itemSearch = slea.readInt();
                final List<HiredMerchant> hms = c.getChannelServer().searchMerchant(itemSearch);
                if (hms.size() > 0) {
                    c.sendPacket(MaplePacketCreator.getOwlSearched(itemSearch, hms));
                    used = true;
                } else {
                    c.getPlayer().dropMessage(1, "�޷��ҵ�����Ŀ.");
                }
                break;
            }
            //������ʹ��ȯ
            case 5320000: {
                String name = slea.readMapleAsciiString();
                String otherName = slea.readMapleAsciiString();
                long unk = slea.readInt();
                long unk_2 = slea.readInt();
                int cardId = slea.readByte();
                short unk_3 = slea.readShort();
                byte unk_4 = slea.readByte();
                // int comm = slea.readByte();
                int comm = Randomizer.rand(0, 6);
                PredictCardFactory pcf = PredictCardFactory.getInstance();
                PredictCardFactory.PredictCard Card = pcf.getPredictCard(cardId);
                // int commentId = Randomizer.nextInt(pcf.getCardCommentSize() + comm);
                PredictCardFactory.PredictCardComment Comment = pcf.getPredictCardComment(comm);
                //  PredictCardFactory.PredictCardComment Comment = pcf.getPredictCardComment(commentId);
                if ((Card == null) || (Comment == null)) {
                    break;
                }
                c.getPlayer().dropMessage(5, "����ռ���ɹ���");

                int love = Randomizer.rand(1, Comment.score) + 5;
                c.sendPacket(MTSCSPacket.show������(name, otherName, love, cardId, Comment.effectType));
                used = true;
                break;
            }
            case 5370000: { //�ڰ�7��
                if (c.getPlayer().getMapId() / 1000000 == 109) {
                    c.getPlayer().dropMessage(1, "�����ڻ��ͼʹ�úڰ�");
                } else {
                    c.getPlayer().setChalkboard(slea.readMapleAsciiString());
                }
                break;
            }
            case 5370001: { //�ڰ�1��
                if (c.getPlayer().getMapId() / 1000000 == 910) {
                    c.getPlayer().setChalkboard(slea.readMapleAsciiString());
                }
                break;
            }
            case 5390000: // Diablo Messenger
            case 5390001: // Cloud 9 Messenger
            case 5390002: // Loveholic Messenger
            case 5390003: // New Year Megassenger 1
            case 5390004: // New Year Megassenger 2
            case 5390005: // Cute Tiger Messenger
            case 5390006:
            case 5390007:
            case 5390008:
            case 5390009:
            case 5390011:
            case 5390012:
            case 5390013: {

                int ��Ϸ���� = gui.Start.ConfigValuesMap.get("��Ϸ���ȿ���");
                if (��Ϸ���� > 0) {
                    c.getPlayer().dropMessage(1, "Ŀǰ���ȹ��ܴӺ�̨�ر��ˣ��޷��㲥����");
                    break;
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(5, "����ȼ�10�����ϲſ���ʹ��.");
                    break;
                }
                if (!c.getPlayer().getCheatTracker().canAvatarSmega2()) {
                    c.getPlayer().dropMessage(6, "Ϊ�˷�ֹˢ��������������10��һ��.");
                    break;
                }
                if (!c.getChannelServer().getMegaphoneMuteState()) {
                    final String text = slea.readMapleAsciiString();
                    if (text.length() > 55) {
                        break;
                    }
                    final boolean ear = slea.readByte() != 0;
                    if (c.getPlayer().isPlayer() && text.indexOf("��") != -1 || text.indexOf("�i") != -1 || text.indexOf("��") != -1 || text.indexOf("��") != -1 || text.indexOf("�X��") != -1 || text.indexOf("�X") != -1 || text.indexOf("����") != -1 || text.indexOf("��Ŀ") != -1 || text.indexOf("�׳�") != -1) {
                        c.getPlayer().dropMessage("˵ʲô�أ�");
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    World.Broadcast.broadcastSmega(MaplePacketCreator.getAvatarMega(c.getPlayer(), c.getChannel(), itemId, text, ear));
                    System.err.println("[�����]" + CurrentReadable_Time() + " : [����] " + c.getPlayer().getName() + " : " + text);
                    used = true;
                } else {
                    c.getPlayer().dropMessage(5, "Ŀǰ����ֹͣʹ����");
                }
                break;
            }
            case 5070000: { // Megaphone
                int ��Ϸ���� = gui.Start.ConfigValuesMap.get("��Ϸ���ȿ���");
                if (��Ϸ���� > 0) {
                    c.getPlayer().dropMessage(1, "Ŀǰ���ȹ��ܴӺ�̨�ر��ˣ��޷��㲥����");
                    break;
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(5, "����ȼ�10�����ϲſ���ʹ��.");
                    break;
                }
                if (!c.getPlayer().getCheatTracker().canAvatarSmega2()) {
                    c.getPlayer().dropMessage(6, "Ϊ�˷�ֹˢ��������������10��һ��.");
                    break;
                }
                if (!c.getChannelServer().getMegaphoneMuteState()) {
                    final String message = slea.readMapleAsciiString();

                    if (message.length() > 65) {
                        break;
                    }
                    final StringBuilder sb = new StringBuilder();
                    addMedalString(c.getPlayer(), sb);
                    sb.append(c.getPlayer().getName());
                    sb.append(" : ");
                    sb.append(message);
                    final boolean ear = slea.readByte() != 0;//ServerProperties.getProperty("ZEV.pbzh").split(",")
                    if (c.getPlayer().isPlayer() && message.indexOf("��") != -1 || message.indexOf("�i") != -1 || message.indexOf("��") != -1 || message.indexOf("��") != -1 || message.indexOf("�X��") != -1 || message.indexOf("�X") != -1 || message.indexOf("����") != -1 || message.indexOf("��Ŀ") != -1 || message.indexOf("�׳�") != -1) {;
                        c.getPlayer().dropMessage("������˵����");
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(2, sb.toString()));
                    System.err.println("[�����]" + CurrentReadable_Time() + " : [����] " + c.getPlayer().getName() + " : " + message);
                    used = true;
                } else {
                    c.getPlayer().dropMessage(5, "Ŀǰ����ֹͣʹ��.");
                }
                break;
            }
            case 5071000: { // Megaphone
                int ��Ϸ���� = gui.Start.ConfigValuesMap.get("��Ϸ���ȿ���");
                if (��Ϸ���� > 0) {
                    c.getPlayer().dropMessage(1, "Ŀǰ���ȹ��ܴӺ�̨�ر��ˣ��޷��㲥����");
                    break;
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(5, "����ȼ�10�����ϲſ���ʹ��.");
                    break;
                }
                if (!c.getPlayer().getCheatTracker().canAvatarSmega2()) {
                    c.getPlayer().dropMessage(6, "Ϊ�˷�ֹˢ��������������10��һ��.");
                    break;
                }
                if (!c.getChannelServer().getMegaphoneMuteState()) {
                    final String message = slea.readMapleAsciiString();

                    if (message.length() > 65) {
                        break;
                    }
                    final boolean ear = slea.readByte() != 0;
                    if (c.getPlayer().isPlayer() && message.indexOf("��") != -1 || message.indexOf("�i") != -1 || message.indexOf("��") != -1 || message.indexOf("��") != -1 || message.indexOf("�X��") != -1 || message.indexOf("�X") != -1 || message.indexOf("����") != -1 || message.indexOf("��Ŀ") != -1 || message.indexOf("�׳�") != -1) {
                        c.getPlayer().dropMessage("������˵����");
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    final StringBuilder sb = new StringBuilder();
                    addMedalString(c.getPlayer(), sb);
                    sb.append(c.getPlayer().getName());
                    sb.append(" : ");
                    sb.append(message);
                    c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(2, sb.toString()));
                    System.err.println("[�����]" + CurrentReadable_Time() + " : [����] " + c.getPlayer().getName() + " : " + message);
                    used = true;
                } else {
                    c.getPlayer().dropMessage(5, "Ŀǰ����ֹͣʹ��.");
                }
                break;
            }
            case 5077000: { // 3 line Megaphone
                int ��Ϸ���� = gui.Start.ConfigValuesMap.get("��Ϸ���ȿ���");
                if (��Ϸ���� > 0) {
                    c.getPlayer().dropMessage(1, "Ŀǰ���ȹ��ܴӺ�̨�ر��ˣ��޷��㲥����");
                    break;
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(5, "����ȼ�10�����ϲſ���ʹ��.");
                    break;
                }
                if (!c.getPlayer().getCheatTracker().canAvatarSmega2()) {
                    c.getPlayer().dropMessage(6, "Ϊ�˷�ֹˢ��������������10��һ��.");
                    break;
                }
                if (!c.getChannelServer().getMegaphoneMuteState()) {
                    final byte numLines = slea.readByte();
                    if (numLines > 3) {
                        return;
                    }
                    final List<String> messages = new LinkedList<String>();
                    String message;
                    for (int i = 0; i < numLines; i++) {
                        message = slea.readMapleAsciiString();
                        if (message.length() > 65) {
                            break;
                        }
                        messages.add(c.getPlayer().getName() + " : " + message);
                    }
                    final boolean ear = slea.readByte() > 0;
                    if (c.getPlayer().isPlayer() && messages.indexOf("��") != -1 || messages.indexOf("�i") != -1 || messages.indexOf("��") != -1 || messages.indexOf("��") != -1 || messages.indexOf("�X��") != -1 || messages.indexOf("�X") != -1 || messages.indexOf("����") != -1 || messages.indexOf("��Ŀ") != -1 || messages.indexOf("�׳�") != -1) {
                        c.getPlayer().dropMessage("������˵����");
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    System.err.println("[�����]" + CurrentReadable_Time() + " : [����] " + c.getPlayer().getName() + " : " + messages);
                    used = true;
                } else {
                    c.getPlayer().dropMessage(5, "Ŀǰ����ֹͣʹ��.");
                }
                break;
            }
            case 5073000: { // Heart Megaphone
                int ��Ϸ���� = gui.Start.ConfigValuesMap.get("��Ϸ���ȿ���");
                if (��Ϸ���� > 0) {
                    c.getPlayer().dropMessage(1, "Ŀǰ���ȹ��ܴӺ�̨�ر��ˣ��޷��㲥����");
                    break;
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(5, "����ȼ�10�����ϲſ���ʹ��.");
                    break;
                }
                if (!c.getPlayer().getCheatTracker().canAvatarSmega2()) {
                    c.getPlayer().dropMessage(6, "Ϊ�˷�ֹˢ��������������10��һ��.");
                    break;
                }
                if (!c.getChannelServer().getMegaphoneMuteState()) {
                    final String message = slea.readMapleAsciiString();

                    if (message.length() > 65) {
                        break;
                    }
                    final StringBuilder sb = new StringBuilder();
                    addMedalString(c.getPlayer(), sb);
                    sb.append(c.getPlayer().getName());
                    sb.append(" : ");
                    sb.append(message);

                    final boolean ear = slea.readByte() != 0;
                    if (c.getPlayer().isPlayer() && message.indexOf("��") != -1 || message.indexOf("�i") != -1 || message.indexOf("��") != -1 || message.indexOf("��") != -1 || message.indexOf("�X��") != -1 || message.indexOf("�X") != -1 || message.indexOf("����") != -1 || message.indexOf("��Ŀ") != -1 || message.indexOf("�׳�") != -1) {
                        c.getPlayer().dropMessage("������˵����");
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    System.err.println("[�����]" + CurrentReadable_Time() + " : [����] " + c.getPlayer().getName() + " : " + message);
                    used = true;
                } else {
                    c.getPlayer().dropMessage(5, "Ŀǰ����ֹͣʹ��.");
                }
                break;
            }
            case 5074000: { // Skull Megaphone
                int ��Ϸ���� = gui.Start.ConfigValuesMap.get("��Ϸ���ȿ���");
                if (��Ϸ���� > 0) {
                    c.getPlayer().dropMessage(1, "Ŀǰ���ȹ��ܴӺ�̨�ر��ˣ��޷��㲥����");
                    break;
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(5, "����ȼ�10�����ϲſ���ʹ��.");
                    break;
                }
                if (!c.getPlayer().getCheatTracker().canAvatarSmega2()) {
                    c.getPlayer().dropMessage(6, "Ϊ�˷�ֹˢ��������������10��һ��.");
                    break;
                }
                if (!c.getChannelServer().getMegaphoneMuteState()) {
                    final String message = slea.readMapleAsciiString();

                    if (message.length() > 65) {
                        break;
                    }
                    final StringBuilder sb = new StringBuilder();
                    addMedalString(c.getPlayer(), sb);
                    sb.append(c.getPlayer().getName());
                    sb.append(" : ");
                    sb.append(message);

                    final boolean ear = slea.readByte() != 0;
                    if (c.getPlayer().isPlayer() && message.indexOf("��") != -1 || message.indexOf("�i") != -1 || message.indexOf("��") != -1 || message.indexOf("��") != -1 || message.indexOf("�X��") != -1 || message.indexOf("�X") != -1 || message.indexOf("����") != -1 || message.indexOf("��Ŀ") != -1 || message.indexOf("�׳�") != -1) {
                        c.getPlayer().dropMessage("������˵����");
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(12, c.getChannel(), sb.toString(), ear));
                    System.err.println("[�����]" + CurrentReadable_Time() + " : [����] " + c.getPlayer().getName() + " : " + message);
                    used = true;
                } else {
                    c.getPlayer().dropMessage(5, "Ŀǰ����ֹͣʹ��.");
                }
                break;
            }
            case 5072000: { // Super Megaphone
                int ��Ϸ���� = gui.Start.ConfigValuesMap.get("��Ϸ���ȿ���");
                if (��Ϸ���� > 0) {
                    c.getPlayer().dropMessage(1, "Ŀǰ���ȹ��ܴӺ�̨�ر��ˣ��޷��㲥����");
                    break;
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(5, "����ȼ�10�����ϲſ���ʹ��.");
                    break;
                }
                if (!c.getPlayer().getCheatTracker().canAvatarSmega2()) {
                    c.getPlayer().dropMessage(6, "Ϊ�˷�ֹˢ��������������10��һ��.");
                    break;
                }
                if (!c.getChannelServer().getMegaphoneMuteState()) {
                    final String message = slea.readMapleAsciiString();

                    if (message.length() > 65) {
                        break;
                    }
                    final StringBuilder sb = new StringBuilder();
                    addMedalString(c.getPlayer(), sb);
                    sb.append(c.getPlayer().getName());
                    sb.append(" : ");
                    sb.append(message);

                    final boolean ear = slea.readByte() != 0;
                    if (c.getPlayer().isPlayer() && message.indexOf("��") != -1 || message.indexOf("�i") != -1 || message.indexOf("��") != -1 || message.indexOf("��") != -1 || message.indexOf("�X��") != -1 || message.indexOf("�X") != -1 || message.indexOf("����") != -1 || message.indexOf("��Ŀ") != -1 || message.indexOf("�׳�") != -1) {
                        c.getPlayer().dropMessage("������˵����");
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }

                    World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(3, c.getChannel(), sb.toString(), ear));
                    System.err.println("[�����]" + CurrentReadable_Time() + " : [����] " + c.getPlayer().getName() + " : " + message);
                    used = true;
                } else {
                    c.getPlayer().dropMessage(5, "Ŀǰ����ֹͣʹ����");
                }
                break;
            }
            case 5076000: { // Item Megaphone
                int ��Ϸ���� = gui.Start.ConfigValuesMap.get("��Ϸ���ȿ���");
                if (��Ϸ���� > 0) {
                    c.getPlayer().dropMessage(1, "Ŀǰ���ȹ��ܴӺ�̨�ر��ˣ��޷��㲥����");
                    break;
                }
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(5, "����ȼ�10�����ϲſ���ʹ��.");
                    break;
                }
                if (!c.getPlayer().getCheatTracker().canAvatarSmega2()) {
                    c.getPlayer().dropMessage(6, "Ϊ�˷�ֹˢ��������������10��һ��.");
                    break;
                }
                if (!c.getChannelServer().getMegaphoneMuteState()) {
                    final String message = slea.readMapleAsciiString();

                    if (message.length() > 65) {
                        break;
                    }
                    final StringBuilder sb = new StringBuilder();
                    addMedalString(c.getPlayer(), sb);
                    sb.append(c.getPlayer().getName());
                    sb.append(" : ");
                    sb.append(message);

                    final boolean ear = slea.readByte() > 0;

                    IItem item = null;
                    if (slea.readByte() == 1) { //item
                        byte invType = (byte) slea.readInt();
                        byte pos = (byte) slea.readInt();
                        item = c.getPlayer().getInventory(MapleInventoryType.getByType(invType)).getItem(pos);
                    }
                    if (c.getPlayer().isPlayer() && message.contains("��")) {
                        c.getPlayer().dropMessage("������˵����");
                        c.sendPacket(MaplePacketCreator.enableActions());
                        return;
                    }
                    World.Broadcast.broadcastSmega(MaplePacketCreator.itemMegaphone(sb.toString(), ear, c.getChannel(), item));
                    System.err.println("[�����]" + CurrentReadable_Time() + " : [����] " + c.getPlayer().getName() + " : " + message);
                    used = true;
                } else {
                    c.getPlayer().dropMessage(5, "Ŀǰ����ֹͣʹ����");
                }
                break;
            }
            case 5075000: // MapleTV Messenger
            case 5075001: // MapleTV Star Messenger
            case 5075002: { // MapleTV Heart Messenger
                c.getPlayer().dropMessage(5, "û�й㲥��Ϣ.");
                break;
            }
            case 5075003:
            case 5075004:
            case 5075005: {
                if (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().dropMessage(5, "10�����ϲſ���ʹ��.");
                    break;
                }
                int tvType = itemId % 10;
                if (tvType == 3) {
                    slea.readByte(); //who knows
                }
                boolean ear = tvType != 1 && tvType != 2 && slea.readByte() > 1; //for tvType 1/2, there is no byte. 
                MapleCharacter victim = tvType == 1 || tvType == 4 ? null : c.getChannelServer().getPlayerStorage().getCharacterByName(slea.readMapleAsciiString()); //for tvType 4, there is no string.
                if (tvType == 0 || tvType == 3) { //doesn't allow two
                    victim = null;
                } else if (victim == null) {
                    c.getPlayer().dropMessage(1, "�����ɫ������Ƶ����.");
                    break;
                }
                String message = slea.readMapleAsciiString();
                World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(3, c.getChannel(), c.getPlayer().getName() + " : " + message, ear));
                break;
            }
            //������������
            case 5450000: {
                MapleShopFactory.getInstance().getShop(61).sendShop(c);
                used = true;
                break;
            }
            //1ǧ����ȯ����ȯ
            case 5680151: {
                c.getPlayer().modifyCSPoints(2, 1000, true);
                used = true;
                break;
            }
            //ħ��ɳ©��7�죩
            case 5500001:
            //ħ��ɳ©��20�죩
            case 5500002: {
                IItem item = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                int days = 20;
                if (item != null && !GameConstants.isAccessory(item.getItemId()) && item.getExpiration() > -1 && !ii.isCash(item.getItemId()) && System.currentTimeMillis() + (100 * 24 * 60 * 60 * 1000L) > item.getExpiration() + (days * 24 * 60 * 60 * 1000L)) {
                    boolean change = true;
                    for (String z : GameConstants.RESERVED) {
                        if (c.getPlayer().getName().indexOf(z) != -1 || item.getOwner().indexOf(z) != -1) {
                            change = false;
                        }
                    }
                    if (change) {
                        item.setExpiration(item.getExpiration() + (days * 24 * 60 * 60 * 1000));
                        c.getPlayer().forceReAddItem(item, MapleInventoryType.EQUIPPED);
                        used = true;
                    } else {
                        c.getPlayer().dropMessage(1, "��װ���޷�ʹ��.");
                    }
                }
                break;
            }
            case 5281001:
            case 5280001:
            case 5281000: {
                Rectangle bounds = new Rectangle((int) c.getPlayer().getPosition().getX(), (int) c.getPlayer().getPosition().getY(), 1, 1);
                MapleMist mist = new MapleMist(bounds, c.getPlayer());
                c.getPlayer().getMap().spawnMist(mist, 10000, true);
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getChatText(c.getPlayer().getId(), "Ŷ�����ҷ�ƨ�ˣ�", false, 1));
                c.sendPacket(MaplePacketCreator.enableActions());
                used = true;
                break;
            }
            //��ͭ��Ұ�100W
            case 5200000: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� >= 90) {
                    final double ��� = Math.ceil(Math.random() * 1000000);//100W
                    c.getPlayer().gainMeso((int) ���, false);
                } else if (���� < 90 && ���� > 70) {
                    final double ��� = Math.ceil(Math.random() * 500000);//200W
                    c.getPlayer().gainMeso((int) ���, false);
                } else {
                    final double ��� = Math.ceil(Math.random() * 200000);//50W
                    c.getPlayer().gainMeso((int) ���, false);
                }
                used = true;
            }
            break;
            //������Ұ�500W
            case 5200001: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� >= 95) {
                    final double ��� = Math.ceil(Math.random() * 5000000);//500W
                    c.getPlayer().gainMeso((int) ���, false);
                } else if (���� < 95 && ���� > 70) {
                    final double ��� = Math.ceil(Math.random() * 2000000);//200W
                    c.getPlayer().gainMeso((int) ���, false);
                } else {
                    final double ��� = Math.ceil(Math.random() * 1000000);//200W
                    c.getPlayer().gainMeso((int) ���, false);
                }
                used = true;
            }
            break;
            //�ƽ��Ұ�1000W
            case 5200002: {
                final double ���� = Math.ceil(Math.random() * 100);
                if (���� >= 95) {
                    final double ��� = Math.ceil(Math.random() * 10000000);//1000W
                    c.getPlayer().gainMeso((int) ���, false);
                } else if (���� < 95 && ���� > 80) {
                    final double ��� = Math.ceil(Math.random() * 5000000);//500W
                    c.getPlayer().gainMeso((int) ���, false);
                } else {
                    final double ��� = Math.ceil(Math.random() * 1000000);//100W
                    c.getPlayer().gainMeso((int) ���, false);
                }
                used = true;
            }
            break;
            case 5610001:
            case 5610000: { // Vega 30
                slea.readInt(); // Inventory type, always eq
                final byte dst = (byte) slea.readInt();
                slea.readInt(); // Inventory type, always use
                final byte src = (byte) slea.readInt();
                used = UseUpgradeScroll(src, dst, (byte) 2, c, c.getPlayer(), itemId); //cannot use ws with vega but we dont care
                cc = used;
                break;
            }
            default:
                if (itemId / 10000 == 512) {//��ѩ��
                    final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    final String msg = ii.getMsg(itemId).replaceFirst("%s", c.getPlayer().getName()).replaceFirst("%s", slea.readMapleAsciiString());
                    c.getPlayer().getMap().startMapEffect(msg, itemId);

                    final int buff = ii.getStateChangeItem(itemId);
                    if (buff != 0) {
                        for (MapleCharacter mChar : c.getPlayer().getMap().getCharactersThreadsafe()) {
                            ii.getItemEffect(buff).applyTo(mChar);
                        }
                    }
                    used = true;
                } else if (itemId / 10000 == 510) {//���ֺ�
                    c.getPlayer().getMap().startJukebox(c.getPlayer().getName(), itemId);
                    used = true;
                } else if (itemId / 10000 == 553) {
                    UseRewardItem(slot, itemId, c, c.getPlayer());// this too
                } else {
                    System.out.println("Unhandled CS item : " + itemId);
                    System.out.println("ZZ" + slea.toString(true));
                }
                break;

        }
        if (used) {
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false, true);
        }
        c.sendPacket(MaplePacketCreator.enableActions());
        if (cc) {
            MapleCharacter player = c.getPlayer();
            c.sendPacket(MaplePacketCreator.getCharInfo(player));
            player.getMap().removePlayer(player);
            player.getMap().addPlayer(player);
        }
    }

    //��Ҽ���Ʒ
    public static final void Pickup_Player(final LittleEndianAccessor slea, MapleClient c, final MapleCharacter chr) {
        if (c.getPlayer().getPlayerShop() != null || c.getPlayer().getConversation() > 0 || c.getPlayer().getTrade() != null) {
            return;
        }
        chr.updateTick(slea.readInt());
        slea.skip(1); // [4] Seems to be tickcount, [1] always 0
        final Point Client_Reportedpos = slea.readPos();
        if (chr == null) {
            return;
        }

        final MapleMapObject ob = chr.getMap().getMapObject(slea.readInt(), MapleMapObjectType.ITEM);

        if (ob == null) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        final MapleMapItem mapitem = (MapleMapItem) ob;
        final Lock lock = mapitem.getLock();
        lock.lock();
        try {
            if (mapitem.isPickedUp()) {
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            if (mapitem.getOwner() != chr.getId() && ((!mapitem.isPlayerDrop() && mapitem.getDropType() == 0) || (mapitem.isPlayerDrop() && chr.getMap().getEverlast()))) {
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            if (!mapitem.isPlayerDrop() && mapitem.getDropType() == 1 && mapitem.getOwner() != chr.getId() && (chr.getParty() == null || chr.getParty().getMemberById(mapitem.getOwner()) == null)) {
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            if (gui.Start.ConfigValuesMap.get("�����⿪��") != null) {
                if (gui.Start.ConfigValuesMap.get("�����⿪��") == 0) {
                    final double Distance = Client_Reportedpos.distanceSq(mapitem.getPosition());
                    if (Distance > 500) {
                        int ch = World.Find.findChannel(��ɫIDȡ����(chr.getId()));
                        MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(��ɫIDȡ����(chr.getId()));
                        if (target.ban("��Ҽ�����Ʒ�����" + Distance, chr.isAdmin(), false, false)) {
                            String ��Ϣ = "[ϵͳ����] : ��� " + target.getName() + " ��Ϊʹ�÷Ƿ���������ȫ������ƻ���Ϸƽ�⣬��ϵͳ���÷�š�";
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, ��Ϣ));
                            sendMsgToQQGroup(��Ϣ);
                        }
                    }
                }
            }
            if (mapitem.getMeso() > 0) {
                if (chr.getParty() != null && mapitem.getOwner() != chr.getId()) {
                    final List<MapleCharacter> toGive = new LinkedList<MapleCharacter>();

                    for (MaplePartyCharacter z : chr.getParty().getMembers()) {
                        MapleCharacter m = chr.getMap().getCharacterById(z.getId());
                        if (m != null) {
                            toGive.add(m);
                        }
                    }
                    for (final MapleCharacter m : toGive) {
                        m.gainMeso(mapitem.getMeso() / toGive.size() + (m.getStat().װ������֤ ? (int) (mapitem.getMeso() / 20.0) : 0), true, true);
                    }
                } else {
                    chr.gainMeso(mapitem.getMeso(), true, true);
                }
                removeItem(chr, mapitem, ob);
            } else if (MapleItemInformationProvider.getInstance().isPickupBlocked(mapitem.getItem().getItemId())) {
                c.sendPacket(MaplePacketCreator.enableActions());
                c.getPlayer().dropMessage(5, "�����Ŀ���ܱ�ѡ��.");// c.getPlayer().useItem();
            } else if (useItem(c, mapitem.getItemId())) {
                removeItem(c.getPlayer(), mapitem, ob);
            } else if (MapleInventoryManipulator.checkSpace(c, mapitem.getItem().getItemId(), mapitem.getItem().getQuantity(), mapitem.getItem().getOwner())) {
                if (mapitem.getItem().getQuantity() >= 50 && GameConstants.isUpgradeScroll(mapitem.getItem().getItemId())) {
                    c.setMonitored(true); //hack check
                }
                if (MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true, mapitem.getDropper() instanceof MapleMonster)) {
                    removeItem(chr, mapitem, ob);
                }
            } else {
                c.sendPacket(MaplePacketCreator.getInventoryFull());
                c.sendPacket(MaplePacketCreator.getShowInventoryFull());
                c.sendPacket(MaplePacketCreator.enableActions());
            }
        } finally {
            lock.unlock();
        }
    }

    //���������Ʒ
    public static final void Pickup_Pet(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        final byte petz = (byte) c.getPlayer().getPetIndex((int) slea.readLong());
        final MaplePet pet = chr.getPet(petz);
        slea.skip(1); // [4] Zero, [4] Seems to be tickcount, [1] Always zero
        chr.updateTick(slea.readInt());
        final Point Client_Reportedpos = slea.readPos();
        final MapleMapObject ob = chr.getMap().getMapObject(slea.readInt(), MapleMapObjectType.ITEM);

        if (ob == null || pet == null) {
            return;
        }

        final MapleMapItem mapitem = (MapleMapItem) ob;
        //  final Lock lock = mapitem.getLock();
        //  lock.lock();
        try {
            // chr.dropMessage(5, "OW: " + mapitem.getOwner() + " CH:" + chr.getId() + " Type: " + mapitem.getDropType() + " PD: " + mapitem.isPlayerDrop());
            //����Ʒ
            if (mapitem.isPickedUp()) {
                /*c.getPlayer().dropMessage(5, "isPickedUp1.");
                if (c.Getcharacterz("" + chr.getId() + "", 5) > 0) {
                    NPCScriptManager.getInstance().start(c, 9270063, 0);
                } else {*/
                c.sendPacket(MaplePacketCreator.getInventoryFull());
                //}
                return;
            }
            if (mapitem.getOwner() != chr.getId() && mapitem.isPlayerDrop()) {
                return;
            }
            if (mapitem.getOwner() != chr.getId() && ((!mapitem.isPlayerDrop() && mapitem.getDropType() == 0) || (mapitem.isPlayerDrop() && chr.getMap().getEverlast()))) {
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            if (!mapitem.isPlayerDrop() && mapitem.getDropType() == 1 && mapitem.getOwner() != chr.getId() && (chr.getParty() == null || chr.getParty().getMemberById(mapitem.getOwner()) == null)) {
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            if (mapitem.isPlayerDrop() && mapitem.getDropType() == 2 && mapitem.getOwner() == chr.getId()) {
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            if (mapitem.isPlayerDrop() && mapitem.getDropType() == 0 && mapitem.getOwner() == chr.getId() && mapitem.getMeso() != 0) {
                c.sendPacket(MaplePacketCreator.enableActions());
                return;
            }
            if (gui.Start.ConfigValuesMap.get("�����⿪��") != null) {
                if (gui.Start.ConfigValuesMap.get("�����⿪��") == 0) {
                    final double Distance = Client_Reportedpos.distanceSq(mapitem.getPosition());
                    if (Distance > 500) {
                        int ch = World.Find.findChannel(��ɫIDȡ����(chr.getId()));
                        MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(��ɫIDȡ����(chr.getId()));
                        if (target.ban("���������Ʒ���" + Distance, chr.isAdmin(), false, false)) {
                            String ��Ϣ = "[ϵͳ����] : ��� " + target.getName() + " ��Ϊʹ�÷Ƿ����������ȫ������ƻ���Ϸƽ�⣬��ϵͳ���÷�š�";
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, ��Ϣ));
                            sendMsgToQQGroup(��Ϣ);
                        }
                    }
                }
            }
            if (mapitem.getMeso() > 0) {
                if (chr.getParty() != null && mapitem.getOwner() != chr.getId()) {
                    final List<MapleCharacter> toGive = new LinkedList<MapleCharacter>();
                    final int splitMeso = mapitem.getMeso() * 40 / 100;
                    for (MaplePartyCharacter z : chr.getParty().getMembers()) {
                        MapleCharacter m = chr.getMap().getCharacterById(z.getId());
                        if (m != null && m.getId() != chr.getId()) {
                            toGive.add(m);
                        }
                    }
                    for (final MapleCharacter m : toGive) {
                        m.gainMeso(splitMeso / toGive.size() + (m.getStat().װ������֤ ? (int) (mapitem.getMeso() / 20.0) : 0), true);
                    }
                    chr.gainMeso(mapitem.getMeso() - splitMeso, true);
                } else {
                    chr.gainMeso(mapitem.getMeso(), true);
                }
                removeItem_Pet(chr, mapitem, petz);// ������Ʒ
            } else if (MapleItemInformationProvider.getInstance().isPickupBlocked(mapitem.getItemId()) || mapitem.getItemId() / 10000 == 291) {
                c.sendPacket(MaplePacketCreator.enableActions());
            } else if (useItem(c, mapitem.getItemId())) {
                removeItem_Pet(chr, mapitem, petz);//���������Ʒ
            } else if (MapleInventoryManipulator.checkSpace(c, mapitem.getItemId(), mapitem.getItem().getQuantity(), mapitem.getItem().getOwner())) {
                if (mapitem.getItem().getQuantity() >= 50 && mapitem.getItemId() == 2340000) {
                    c.setMonitored(true); //hack check
                }
                MapleInventoryManipulator.pet_addFromDrop(c, mapitem.getItem(), true, mapitem.getDropper() instanceof MapleMonster);
                // MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true, mapitem.getDropper() instanceof MapleMonster);
                removeItem_Pet(chr, mapitem, petz);//���������Ʒ
//                if (c.Getcharacterz("" + chr.getId() + "", 5) > 0) {
//                    c.getPlayer().dropMessage(5, "�Զ�������ָ����Ʒ��");
//                    NPCScriptManager.getInstance().start(c, 9270063, 0);
//                }
            }
        } finally {

            //lock.unlock();
        }
    }

    public static final boolean useItem(final MapleClient c, final int id) {
        if (GameConstants.isUse(id)) { // TO prevent caching of everything, waste of mem
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            final byte consumeval = ii.isConsumeOnPickup(id);

            if (consumeval > 0) {
                if (consumeval == 2) {
                    if (c.getPlayer().getParty() != null) {
                        for (final MaplePartyCharacter pc : c.getPlayer().getParty().getMembers()) {
                            final MapleCharacter chr = c.getPlayer().getMap().getCharacterById(pc.getId());
                            if (chr != null) {
                                ii.getItemEffect(id).applyTo(chr);
                            }
                        }
                    } else {
                        ii.getItemEffect(id).applyTo(c.getPlayer());
                    }
                } else {
                    ii.getItemEffect(id).applyTo(c.getPlayer());
                }
                c.sendPacket(MaplePacketCreator.getShowItemGain(id, (byte) 1));
                return true;
            }
        }
        return false;
    }

    public static final void removeItem_Pet(final MapleCharacter chr, final MapleMapItem mapitem, int pet) {
        //System.out.println("���������Ʒ");
        //long nowTimestamp = System.currentTimeMillis();
        //if (nowTimestamp - chr.���������ȴ > 100) {
        mapitem.setPickedUp(true);
        chr.getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 5, chr.getId(), pet), mapitem.getPosition());
        chr.getMap().removeMapObject(mapitem);
        if (mapitem.isRandDrop()) {
            chr.getMap().spawnRandDrop();
        }
        // chr.���������ȴ = nowTimestamp;
        // }
    }

    private static final void removeItem(final MapleCharacter chr, final MapleMapItem mapitem, final MapleMapObject ob) {
        //System.out.println("��Ҽ�����Ʒ");
//        long nowTimestamp = System.currentTimeMillis();
//        if (nowTimestamp - chr.��Ҽ�����ȴ > 100) {
        mapitem.setPickedUp(true);
        chr.getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 2, chr.getId()), mapitem.getPosition());
        chr.getMap().removeMapObject(ob);
        if (mapitem.isRandDrop()) {
            chr.getMap().spawnRandDrop();
        }
//            chr.��Ҽ�����ȴ = nowTimestamp;
//        }
    }

    private static final void addMedalString(final MapleCharacter c, final StringBuilder sb) {
        final IItem medal = c.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -26);
        if (medal != null) { // Medal
            sb.append("<");
            sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
            sb.append("> ");
        }
    }

    private static final boolean getIncubatedItems(MapleClient c) {
        if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < 2 || c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 2 || c.getPlayer().getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < 2) {
            c.getPlayer().dropMessage(5, "�������Ŀ���������ռ�.");
            return false;
        }
        final int[] ids = {2430091, 2430092, 2430093, 2430101, 2430102, //mounts 
            2340000, //rares
            1152000, 1152001, 1152004, 1152005, 1152006, 1152007, 1152008, //toenail only comes when db is out.
            1000040, 1102246, 1082276, 1050169, 1051210, 1072447, 1442106, //blizzard
            3010019, //chairs
            1001060, 1002391, 1102004, 1050039, 1102040, 1102041, 1102042, 1102043, //equips
            1082145, 1082146, 1082147, 1082148, 1082149, 1082150, //wg
            2043704, 2040904, 2040409, 2040307, 2041030, 2040015, 2040109, 2041035, 2041036, 2040009, 2040511, 2040408, 2043804, 2044105, 2044903, 2044804, 2043009, 2043305, 2040610, 2040716, 2041037, 2043005, 2041032, 2040305, //scrolls
            2040211, 2040212, 1022097, //dragon glasses
            2049000, 2049001, 2049002, 2049003, //clean slate
            1012058, 1012059, 1012060, 1012061, //pinocchio nose msea only.
            1332100, 1382058, 1402073, 1432066, 1442090, 1452058, 1462076, 1472069, 1482051, 1492024, 1342009,//durability weapons level 105
            2049400, 2049401, 2049301};
        //out of 1000
        final int[] chances = {100, 100, 100, 100, 100,
            1,
            10, 10, 10, 10, 10, 10, 10,
            5, 5, 5, 5, 5, 5, 5,
            2,
            10, 10, 10, 10, 10, 10, 10, 10,
            5, 5, 5, 5, 5, 5,
            10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
            5, 5, 10,
            10, 10, 10, 10,
            5, 5, 5, 5,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            1, 2, 1, 2};
        int z = Randomizer.nextInt(ids.length);
        while (chances[z] < Randomizer.nextInt(1000)) {
            z = Randomizer.nextInt(ids.length);
        }
        int z_2 = Randomizer.nextInt(ids.length);
        while (z_2 == z || chances[z_2] < Randomizer.nextInt(1000)) {
            z_2 = Randomizer.nextInt(ids.length);
        }
        c.sendPacket(MaplePacketCreator.getPeanutResult(ids[z], (short) 1, ids[z_2], (short) 1));
        return MapleInventoryManipulator.addById(c, ids[z], (short) 1, (byte) 0) && MapleInventoryManipulator.addById(c, ids[z_2], (short) 1, (byte) 0);

    }

    public static final void OwlMinerva(final LittleEndianAccessor slea, final MapleClient c) {
        final byte slot = (byte) slea.readShort();
        final int itemid = slea.readInt();
        final IItem toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
        if (toUse != null && toUse.getQuantity() > 0 && toUse.getItemId() == itemid && itemid == 2310000) {
            final int itemSearch = slea.readInt();
            final List<HiredMerchant> hms = c.getChannelServer().searchMerchant(itemSearch);
            if (hms.size() > 0) {
                c.sendPacket(MaplePacketCreator.getOwlSearched(itemSearch, hms));
                MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemid, 1, true, false);
            } else {
                c.getPlayer().dropMessage(1, "�޷��ҵ�����Ŀ.");
            }
        }
        c.sendPacket(MaplePacketCreator.enableActions());
    }

    public static final void Owl(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer().haveItem(5230000, 1, true, false) || c.getPlayer().haveItem(2310000, 1, true, false)) {
            if (c.getPlayer().getMapId() >= 910000000 && c.getPlayer().getMapId() <= 910000022) {
                c.sendPacket(MaplePacketCreator.getOwlOpen());
            } else if (c.getPlayer().isGM()) {
                c.sendPacket(MaplePacketCreator.getOwlOpen());
            } else {
                c.getPlayer().dropMessage(5, "��ֻ�����������г�.");
                c.sendPacket(MaplePacketCreator.enableActions());
            }
        }
    }
    public static final int OWL_ID = 2; //don't change. 0 = owner ID, 1 = store ID, 2 = object ID

    public static final void UseSkillBook(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        slea.skip(4);
        final byte slot = (byte) slea.readShort();
        final int itemId = slea.readInt();
        final IItem toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);

        if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
            return;
        }
        final Map<String, Integer> skilldata = MapleItemInformationProvider.getInstance().getSkillStats(toUse.getItemId());
        if (skilldata == null) { // Hacking or used an unknown item
            return;
        }
        boolean canuse = false, success = false;
        int skill = 0, maxlevel = 0;

        final int SuccessRate = skilldata.get("success");
        final int ReqSkillLevel = skilldata.get("reqSkillLevel");
        final int MasterLevel = skilldata.get("masterLevel");

        byte i = 0;
        Integer CurrentLoopedSkillId;
        for (;;) {
            CurrentLoopedSkillId = skilldata.get("skillid" + i);
            i++;
            if (CurrentLoopedSkillId == null) {
                break; // End of data
            }
            if (Math.floor(CurrentLoopedSkillId / 10000) == chr.getJob()) {
                final ISkill CurrSkillData = SkillFactory.getSkill(CurrentLoopedSkillId);
                if (chr.getSkillLevel(CurrSkillData) >= ReqSkillLevel && chr.getMasterLevel(CurrSkillData) < MasterLevel) {
                    canuse = true;
                    if (Randomizer.nextInt(99) <= SuccessRate && SuccessRate != 0) {
                        success = true;
                        final ISkill skill2 = CurrSkillData;
                        chr.changeSkillLevel(skill2, chr.getSkillLevel(skill2), (byte) MasterLevel);
                    } else {
                        success = false;
                    }
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                    break;
                } else { // Failed to meet skill requirements
                    canuse = false;
                }
            }
        }
        c.sendPacket(MaplePacketCreator.useSkillBook(chr, skill, maxlevel, canuse, success));
    }

    public static final void OwlWarp(final LittleEndianAccessor slea, final MapleClient c) {
        c.sendPacket(MaplePacketCreator.enableActions());
        if (c.getPlayer().getMapId() >= 910000000 && c.getPlayer().getMapId() <= 910000022 && c.getPlayer().getPlayerShop() == null) {
            final int id = slea.readInt();
            final int map = slea.readInt();
            if (map >= 910000001 && map <= 910000022) {
                final MapleMap mapp = c.getChannelServer().getMapFactory().getMap(map);
                c.getPlayer().changeMap(mapp, mapp.getPortal(0));
                HiredMerchant merchant = null;
                List<MapleMapObject> objects;
                switch (OWL_ID) {
                    case 0:
                        objects = mapp.getAllHiredMerchantsThreadsafe();
                        for (MapleMapObject ob : objects) {
                            if (ob instanceof IMaplePlayerShop) {
                                final IMaplePlayerShop ips = (IMaplePlayerShop) ob;
                                if (ips instanceof HiredMerchant) {
                                    final HiredMerchant merch = (HiredMerchant) ips;
                                    if (merch.getOwnerId() == id) {
                                        merchant = merch;
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case 1:
                        objects = mapp.getAllHiredMerchantsThreadsafe();
                        for (MapleMapObject ob : objects) {
                            if (ob instanceof IMaplePlayerShop) {
                                final IMaplePlayerShop ips = (IMaplePlayerShop) ob;
                                if (ips instanceof HiredMerchant) {
                                    final HiredMerchant merch = (HiredMerchant) ips;
                                    if (merch.getStoreId() == id) {
                                        merchant = merch;
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        final MapleMapObject ob = mapp.getMapObject(id, MapleMapObjectType.HIRED_MERCHANT);
                        if (ob instanceof IMaplePlayerShop) {
                            final IMaplePlayerShop ips = (IMaplePlayerShop) ob;
                            if (ips instanceof HiredMerchant) {
                                merchant = (HiredMerchant) ips;
                            }
                            System.out.println("ssssssssssssss");

                        }
                        break;
                }
                if (merchant != null) {
                    if (merchant.isOwner(c.getPlayer())) {
                        merchant.setOpen(false);
                        merchant.removeAllVisitors((byte) 16, (byte) 0);
                        c.getPlayer().setPlayerShop(merchant);
                        c.sendPacket(PlayerShopPacket.getHiredMerch(c.getPlayer(), merchant, false));
                    } else if (!merchant.isOpen() || !merchant.isAvailable()) {
                        c.getPlayer().dropMessage(1, "��ҵ���ά�ޣ����Ժ�.");
                    } else if (merchant.getFreeSlot() == -1) {
                        c.getPlayer().dropMessage(1, "��ҵ��Ѿ��ﵽ��������������Ժ�.");
                    } else if (merchant.isInBlackList(c.getPlayer().getName())) {
                        c.getPlayer().dropMessage(1, "���ѱ���ֹ������̵�.");
                    } else {
                        c.getPlayer().setPlayerShop(merchant);
                        merchant.addVisitor(c.getPlayer());
                        c.sendPacket(PlayerShopPacket.getHiredMerch(c.getPlayer(), merchant, false));
                    }
                } else {
                    c.getPlayer().dropMessage(1, "��ҵ���ά�ޣ����Ժ�.");
                }
            }
        }
    }

    public static final void TestDouDou(final LittleEndianAccessor slea, final MapleClient c) {
        byte type = slea.readByte();//��������
        /*if (type != 0 || type != 2 || type != 6) {
            c.getPlayer().dropMessage(5, "[type]:" + type);
        }*/
        switch (type) {
            //��������
            case 0: {
                short Intensity = slea.readShort();
                c.getPlayer().���������� = Intensity;
                //c.getPlayer().dropMessage(5, "[���ȱ䶯]:" + Intensity);
                NPCScriptManager.getInstance().dispose(c);
                break;
            }
            //�رն�����
            case 2:
            case 6:
                c.getPlayer().�رն�����();
                //c.getPlayer().saveToDB(false, false);
                //c.getPlayer().dropMessage(5, "[�򶹶�����]");
                break;
            //��ʼ�򶹶�
            case 1:
                if (c.getPlayer().getBeans() >= 5) {
                    final short Intensity = slea.readShort();
                    c.getPlayer().�н����� = 0;
                    c.getPlayer().�������� = 0;
                    c.getPlayer().���������� = 1;
                    c.getPlayer().���������� = Intensity;
                    c.getPlayer().gainBeans(-5);
                    c.getPlayer().saveToDB(false, false);
                    c.getPlayer().����������();
                } else {
                    c.getPlayer().dropMessage(1, "��Ķ���������Ŷ��");
                    c.getPlayer().�رն�����();
                }

                break;
            //������Ĵ���
            case 3:
                int ���� = (int) Math.ceil(Math.random() * 100);
                if (���� <= 80) {
                    int ���� = (int) Math.ceil(Math.random() * c.getPlayer().�н�������);
                    c.sendPacket(MaplePacketCreator.��������Ч(����));
                    c.getPlayer().gainBeans(����);
                    c.getPlayer().dropMessage(1, "��ϲ������ " + ���� + " �Ŷ�����");
                    c.getPlayer().saveToDB(false, false);
                } else {
                    NPCScriptManager.getInstance().dispose(c);
                    NPCScriptManager.getInstance().start(c, 9100205, 1);
                }
                c.getPlayer().�رն�����();
                /*c.getPlayer().dropMessage(5, "[������������ʼ�����Ƴ齱]");
                if (c.getPlayer().�н����� >= 7) {
                    c.getPlayer().�н����� = 7;
                } else {
                    c.getPlayer().�н�����++;
                }
                c.sendPacket(MaplePacketCreator.������(c.getPlayer().�н�����));*/
                break;
            case 4:
                /*c.getPlayer().dropMessage(5, "[�����Ƴ齱��ʼ]");
                int ���� = MapleCharacter.rand(0, 100);
                if (���� <= c.getPlayer().�н�����) {
                    c.getPlayer().�Ƿ��н� = true;
                }
                int[] ���� = new int[3];
                ����[0] = MapleCharacter.rand(0, 1);
                ����[1] = MapleCharacter.rand(0, 1);
                ����[2] = MapleCharacter.rand(0, 1);
                c.sendPacket(MaplePacketCreator.������_�齱(c.getPlayer(), ����));*/
                c.getPlayer().�رն�����();
                break;
            case 5:
                if (c.getPlayer().�н����� <= 0) {
                    c.getPlayer().�н����� = 0;
                    c.sendPacket(MaplePacketCreator.������(c.getPlayer().�н�����));
                } else {
                    c.getPlayer().�н�����--;
                    c.sendPacket(MaplePacketCreator.������(c.getPlayer().�н�����));
                }
                if (c.getPlayer().�Ƿ��н�) {
                    c.sendPacket(MaplePacketCreator.��������Ч(100));
                    //��������
                }
                c.getPlayer().�رն�����();
                //��ԭ�н���Ϣ
                c.getPlayer().�Ƿ��н� = false;
                break;
            ///
            //��ʼ�齱..
            case 7:
                /*if (c.getPlayer().���������� == 0) {
                    c.getPlayer().dropMessage(5, "[��ϲ���н��ˣ���ʼҡ����]");
                    c.getPlayer().���������� = 1;
                    NPCScriptManager.getInstance().dispose(c);
                    NPCScriptManager.getInstance().start(c, 9900004, 999999999);
                }
                break;*/
                c.getPlayer().�رն�����();
            case 11:
                c.getPlayer().�رն�����();
                //c.sendPacket(MaplePacketCreator.test������111(1, (short) 0, (byte) 0, (short) 0));
                break;
            default:
                System.out.println("����:" + type + " " + slea.toString());
                break;
            //

        }
    }

}