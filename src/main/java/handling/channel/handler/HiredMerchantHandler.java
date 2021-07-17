/*
雇佣商店
 */
package handling.channel.handler;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import client.MapleClient;
import client.MapleCharacter;
import client.inventory.IEquip;
import constants.GameConstants;
import client.inventory.ItemLoader;
import database.DatabaseConnection;
import handling.world.World;
import java.util.Map;
import scripting.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MerchItemPackage;
import server.MapleItemInformationProvider;
import server.ServerProperties;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.StringUtil;
import tools.packet.PlayerShopPacket;
import tools.data.LittleEndianAccessor;

public class HiredMerchantHandler {

    /**
     * <开设雇佣>
     */
    public static final void UseHiredMerchant(final LittleEndianAccessor slea, final MapleClient c) {
        if (gui.Start.ConfigValuesMap.get("雇佣商人开关") <= 0) {
            if (c.getPlayer().getMap().allowPersonalShop()) {
                final byte state = checkExistance(c.getPlayer().getAccountID(), c.getPlayer().getId());

                switch (state) {
                    case 1:
                        c.getPlayer().dropMessage(1, "请先去领取你之前摆摊的东西");
                        break;
                    case 0:
                        boolean merch = World.hasMerchant(c.getPlayer().getAccountID());
                        if (!merch) {
                            c.sendPacket(PlayerShopPacket.sendTitleBox());
                            if (c.getPlayer().getBossLog("日常开启雇佣") <= 0) {
                                c.getPlayer().setBossLog("日常开启雇佣");//为自己加记录
                            }
                        } else {
                            c.getPlayer().dropMessage(1, "请换个地方开或者是你已经有开店了");
                        }
                        break;
                    default:
                        c.getPlayer().dropMessage(1, "发生未知错误.");
                        break;
                }
            } else {
                c.getSession().close();
            }
        } else {
            c.getPlayer().dropMessage(1, "管理员已经从后台关闭了雇佣商人功能");
        }
    }

    /**
     * <雇佣保存金币>
     */
    private static final byte checkExistance(final int accid, final int charid) {
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

    /**
     * <雇佣取物>
     */
    public static void MerchantItemStore(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer() == null) {
            return;
        }
        final byte operation = slea.readByte();
        /*if (operation == 20 || operation == 26) {
            if (c.getPlayer().getLastHM() + 5 * 60 * 1000 > System.currentTimeMillis()) {
                c.getPlayer().dropMessage(1, "5 分钟后可以领回雇佣物品。");
                c.sendPacket(MaplePacketCreator.enableActions());
                c.getPlayer().setConversation(0);
                return;
            }
        }*/
        switch (operation) {
            /**
             * <打开雇佣道具领取>
             */
            case 20: {
                slea.readMapleAsciiString();
                final int conv = c.getPlayer().getConversation();
                /**
                 * <雇佣商店ID是账号ID？>
                 */
                boolean merch = World.hasMerchant(c.getPlayer().getAccountID());
                if (merch) {
                    c.getPlayer().dropMessage(1, "请关闭商店后再试一次.");
                    c.getPlayer().setConversation(0);
                } else if (conv == 3) { // Hired Merch 雇来的东西
                    //这里是判断有没有需要领取的东西
                    final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());
                    if (pack == null) {
                        c.getPlayer().dropMessage(1, "你没有物品可以领取!");
                        c.getPlayer().setConversation(0);
                    } else if (pack.getItems().size() <= 0) { //error fix for complainers.对于抱怨错误修复。
                        if (!check(c.getPlayer(), pack)) {
                            c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x21));
                            return;
                        }
                        if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
                            FileoutputUtil.logToFile_chr(c.getPlayer(), "服务端记录信息/玩家个人信息记录/" + c.getPlayer().getName() + "个人信息/雇佣金币领取记录.txt", " 领回金币 " + pack.getMesos());
                            c.getPlayer().gainMeso(pack.getMesos(), false);
                            c.getPlayer().setConversation(0);
                            c.getPlayer().dropMessage(1, "雇佣领回金币 : " + pack.getMesos());
                            //     c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x1d));
                            //      c.getPlayer().setLastHM(System.currentTimeMillis());
                        } else {
                            c.getPlayer().dropMessage(1, "发生未知错误。");
                        }
                        c.getPlayer().setConversation(0);
                        c.sendPacket(MaplePacketCreator.enableActions());
                    } else {
                        c.sendPacket(PlayerShopPacket.merchItemStore_ItemData(pack));
                    }
                }
                break;
            }
            /**
             * <显示可以掏出物品>
             */
            case 25: {
                if (c.getPlayer().getConversation() != 3) {
                    return;
                }
                c.sendPacket(PlayerShopPacket.merchItemStore((byte) 0x24));
                //c.getPlayer().saveToDB(false, false);
                break;
            }
            /**
             * <掏出物品>
             */
            case 26: {
                if (c.getPlayer().getConversation() != 3) {
                    c.getPlayer().dropMessage(1, "发生未知错误1.");
                    c.sendPacket(MaplePacketCreator.enableActions());
                    return;
                }
                final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());

                if (pack == null) {
                    c.getPlayer().dropMessage(1, "发生未知错误。\r\n你没有物品可以领取！");
                    c.getPlayer().道具存档();
                    return;
                }
                if (!check(c.getPlayer(), pack)) {
                    c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x21));
                    return;
                }
                /**
                 * <清除雇佣的道具>
                 */
                if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
                    c.getPlayer().gainMeso(pack.getMesos(), false);
                    for (IItem item : pack.getItems()) {
                        MapleInventoryManipulator.addFromDrop(c, item, false);
                    }
                    c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x1d));
                    String item_id = "";
                    String item_name = "";
                    for (IItem item : pack.getItems()) {
                        item_id += item.getItemId() + "(" + item.getQuantity() + "), ";
                        item_name += MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "(" + item.getQuantity() + "), ";
                    }
                    删除道具(c.getPlayer().getId());
                    c.getPlayer().道具存档();
                    FileoutputUtil.logToFile_chr(c.getPlayer(), "服务端记录信息/雇佣领取记录.txt", " 领回金币 " + pack.getMesos() + " 领回道具数量 " + pack.getItems().size() + " 道具代码 " + item_id + " 道具名称 " + item_name);
                    // FileoutputUtil.logToFile_chr(c.getPlayer(), "服务端记录信息/雇佣领取记录2.txt", " 领回金币 " + pack.getMesos() + " 领回道具数量 " + pack.getItems().size() + " 道具 " + item_name);
                    //    c.getPlayer().setLastHM(System.currentTimeMillis());
                } else {
                    c.getPlayer().dropMessage(1, "发生未知错误.");
                }
                break;
            }
            /**
             * <离开雇佣管理员>
             */
            case 27: {
                c.getPlayer().setConversation(0);
                //c.getPlayer().道具存档();
                break;
            }

        }
        //c.getPlayer().saveToDB(false, false);
    }

    private static void getShopItem(MapleClient c) {
        if (c.getPlayer().getConversation() != 3) {
            return;
        }
        final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());

        if (pack == null) {
            c.getPlayer().dropMessage(1, "发生未知错误。");
            return;
        }
        if (!check(c.getPlayer(), pack)) {
            c.getPlayer().dropMessage(1, "你背包格子不够。");
            //    c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x21));
            return;
        }
        if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
            c.getPlayer().gainMeso(pack.getMesos(), false);
            for (IItem item : pack.getItems()) {
                MapleInventoryManipulator.addFromDrop(c, item, false);
            }
            c.getPlayer().dropMessage(5, "领取成功。");
            c.getPlayer().saveToDB(false, false);
            //  c.sendPacket(PlayerShopPacket.merchItem_Message((byte) 0x1d));
        } else {
            c.getPlayer().dropMessage(1, "发生未知错误。");
        }
    }

    private static final boolean check(final MapleCharacter chr, final MerchItemPackage pack) {
        if (chr.getMeso() + pack.getMesos() < 0) {
            return false;
        }
        byte eq = 0, use = 0, setup = 0, etc = 0, cash = 0;
        for (IItem item : pack.getItems()) {
            final MapleInventoryType invtype = GameConstants.getInventoryType(item.getItemId());
            if (null != invtype) {
                switch (invtype) {
                    case EQUIP:
                        eq++;
                        break;
                    case USE:
                        use++;
                        break;
                    case SETUP:
                        setup++;
                        break;
                    case ETC:
                        etc++;
                        break;
                    case CASH:
                        cash++;
                        break;
                    default:
                        break;
                }
            }

        }
        if (chr.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() <= eq
                || chr.getInventory(MapleInventoryType.USE).getNumFreeSlot() <= use
                || chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() <= setup
                || chr.getInventory(MapleInventoryType.ETC).getNumFreeSlot() <= etc
                || chr.getInventory(MapleInventoryType.CASH).getNumFreeSlot() <= cash) {
            return false;
        }
        return true;
    }

    private static final boolean deletePackage(final int charid, final int accid, final int packageid) {
        final Connection con = DatabaseConnection.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement("DELETE from hiredmerch where characterid = ? OR accountid = ? OR packageid = ?");
            ps.setInt(1, charid);
            ps.setInt(2, accid);
            ps.setInt(3, packageid);
            ps.execute();
            ps.close();
            //System.out.println("4");
            ItemLoader.HIRED_MERCHANT.saveItems(null, packageid, accid, charid);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void 删除道具(int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, int a10, int a11, int a12, int a13, int a14, int a15, int a16, int a17, int a18, int a19, int a20, int a21, int a22, int a23, int a24, int a25, int a26, int a27, int a28, int a29, String a30) {
        try {
            ResultSet rs = null;
            PreparedStatement ps = null;
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM hire");
            rs = ps.executeQuery();
            if (rs.next()) {
                int a = 0;

                String sqlstr = " delete from hire where "
                        + "cid = " + a1 + " && "
                        + "itemid = " + a2 + "&& "
                        + "upgradeslots = " + a3 + " && "
                        + "level = " + a4 + " &&"
                        + "str=" + a5 + " &&"
                        + "dex=" + a6 + " && "
                        + "2int=" + a7 + "&&"
                        + "luk=" + a8 + "&&"
                        + "hp=" + a9 + " &&"
                        + "mp=" + a10 + "&&"
                        + "watk=" + a11 + "&&"
                        + "matk=" + a12 + "&&"
                        + "wdef=" + a13 + "&&"
                        + "mdef=" + a14 + "&&"
                        + "acc=" + a15 + "&&"
                        + "avoid=" + a16 + "&&"
                        + "hands=" + a17 + "&&"
                        + "speed=" + a18 + "&&"
                        + "jump=" + a19 + "&&"
                        + "ViciousHammer=" + a20 + "&&"
                        + "itemEXP=" + a21 + "&&"
                        + "durability=" + a22 + "&&"
                        + "enhance=" + a23 + "&&"
                        + "potential1=" + a24 + "&&"
                        + "potential2=" + a25 + "&&"
                        + "potential3=" + a26 + "&&"
                        + "hpR=" + a27 + "&&"
                        + "mpR=" + a28 + "&&"
                        + "itemlevel=" + a29 + "&&"
                        + "mxmxd_dakong_fumo= '" + a30 + "' LIMIT 1";
                ps.executeUpdate(sqlstr);
                //ps.close();

            }
        } catch (SQLException ex) {

        }
    }

    public static void 删除道具(int a1) {
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
                //ps.close();

            }
        } catch (SQLException ex) {

        }
    }

    private static final MerchItemPackage loadItemFrom_Database(final int charid, final int accountid) {
        final Connection con = DatabaseConnection.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where characterid = ? OR accountid = ?");
            ps.setInt(1, charid);
            ps.setInt(2, accountid);
            //System.out.println("5");
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
            Map<Integer, Pair<IItem, MapleInventoryType>> items = ItemLoader.HIRED_MERCHANT.loadItems_hm(packageid, accountid);
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

}
