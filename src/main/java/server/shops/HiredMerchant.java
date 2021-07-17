/**
 * <雇佣商人>
 */
package server.shops;

import client.inventory.IItem;
import client.inventory.ItemFlag;
import constants.GameConstants;
import client.MapleCharacter;
import client.MapleClient;
import database.DatabaseConnection;
import server.MapleItemInformationProvider;
import handling.channel.ChannelServer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import server.MapleInventoryManipulator;
import server.Timer.EtcTimer;
import server.maps.MapleMap;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.packet.PlayerShopPacket;

public class HiredMerchant extends AbstractPlayerStore {

    public ScheduledFuture<?> schedule;
    private List<String> blacklist;
    private int storeid;
    private long start;

    public HiredMerchant(String ownerName, int ownerId, MapleMap map, int x, int y, int itemId, String desc, int channel, int accid) {
        super(ownerName, ownerId, x, y, y, desc, channel, itemId, channel, accid);
        this.itemId = itemId;
        this.map = map.getId();
        start = System.currentTimeMillis();
        blacklist = new LinkedList<>();
    }

    /**
     * <建立雇佣商人>
     */
    public HiredMerchant(MapleCharacter owner, int itemId, String desc) {
        super(owner, itemId, desc, "", 3);
        this.start = System.currentTimeMillis();
        this.blacklist = new LinkedList<String>();
        this.schedule = EtcTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                closeShop(true, true);
            }
        }, 1000 * 60 * 60 * gui.Start.ConfigValuesMap.get("雇佣持续时间"));
    }

    public byte getShopType() {
        return IMaplePlayerShop.HIRED_MERCHANT;
    }

    public final void setStoreid(final int storeid) {
        this.storeid = storeid;
    }

    public List<MaplePlayerShopItem> searchItem(final int itemSearch) {
        final List<MaplePlayerShopItem> itemz = new LinkedList<MaplePlayerShopItem>();
        for (MaplePlayerShopItem item : this.items) {
            if ((item.item.getItemId() == itemSearch) && (item.bundles > 0)) {
                itemz.add(item);
            }
        }
        return itemz;
    }

    @Override
    public void buy(MapleClient c, int item, short quantity) {
        MaplePlayerShopItem pItem = (MaplePlayerShopItem) this.items.get(item);
        IItem shopItem = pItem.item;
        IItem newItem = shopItem.copy();
        short perbundle = newItem.getQuantity();
        int theQuantity = pItem.price * quantity;
        newItem.setQuantity((short) (quantity * perbundle));

        byte flag = newItem.getFlag();

        if (ItemFlag.KARMA_EQ.check(flag)) {
            newItem.setFlag((byte) (flag - ItemFlag.KARMA_EQ.getValue()));
        } else if (ItemFlag.KARMA_USE.check(flag)) {
            newItem.setFlag((byte) (flag - ItemFlag.KARMA_USE.getValue()));
        }

        if (!c.getPlayer().canHold(newItem.getItemId())) {
            c.getPlayer().dropMessage(1, "背包已满");
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        if (MapleInventoryManipulator.checkSpace(c, newItem.getItemId(), newItem.getQuantity(), newItem.getOwner())) {
            int gainmeso = getMeso() + theQuantity - GameConstants.EntrustedStoreTax(theQuantity);
            if (gainmeso > 0) {
                setMeso(gainmeso);
                MaplePlayerShopItem tmp167_165 = pItem;
                tmp167_165.bundles = (short) (tmp167_165.bundles - quantity);
                MapleInventoryManipulator.addFromDrop(c, newItem, false);
                this.bought.add(new AbstractPlayerStore.BoughtItem(newItem.getItemId(), quantity, theQuantity, c.getPlayer().getName()));
                c.getPlayer().gainMeso(-theQuantity, false);
                saveItems();
                MapleCharacter chr = getMCOwnerWorld();
                int chr2 = getOwnerId();
                String itemText = new StringBuilder().append(MapleItemInformationProvider.getInstance().getName(newItem.getItemId())).append(" (").append(perbundle).append(") x ").append(quantity).append(" 已经被卖出。 剩余数量: ").append(pItem.bundles).append(" 购买者: ").append(c.getPlayer().getName()).toString();
                if (c.getPlayer().getBossLog("日常雇佣购买") <= 0) {
                    c.getPlayer().setBossLog("日常雇佣购买");
                }
                if (chr != null) {
                    chr.dropMessage(5, new StringBuilder().append("你的雇佣商店里面的道具: ").append(itemText).toString());
                }
//                if (newItem.getItemId() < 2000000) {
//                    IEquip zev = (IEquip) shopItem;
//                    删除道具(chr2, newItem.getItemId(), zev.getUpgradeSlots(), zev.getLevel(), zev.getStr(), zev.getDex(), zev.getInt(), zev.getLuk(), zev.getHp(), zev.getMp(), zev.getWatk(), zev.getMatk(), zev.getWdef(), zev.getMdef(), zev.getAcc(), zev.getAvoid(), zev.getHands(), zev.getSpeed(), zev.getJump(), zev.getViciousHammer(), zev.getItemEXP(), zev.getDurability(), zev.getEnhance(), zev.getPotential1(), zev.getPotential2(), zev.getPotential3(), zev.getHpR(), zev.getMpR(), zev.getEquipLevel(), zev.getDaKongFuMo());
//                } else {
//                    修改道具(chr2, newItem.getItemId(), pItem.bundles);
//                }
            } else {
                c.getPlayer().dropMessage(1, "金币不足.");
                c.sendPacket(MaplePacketCreator.enableActions());
            }
        } else {
            c.getPlayer().dropMessage(1, "背包已满" + "\r\n" + "请留1格以上位置" + "\r\n" + "在进行购买物品" + "\r\n" + "防止非法复制");
            c.sendPacket(MaplePacketCreator.enableActions());
        }
    }

    @Override
    public void closeShop(boolean saveItems, boolean remove) {
        
        if (this.schedule != null) {
            this.schedule.cancel(false);
        }
        if (saveItems) {
            saveItems();
            //this.items.clear();
            items.clear();
        }
        if (remove) {
            ChannelServer.getInstance(this.getChannel()).removeMerchant(this);
            getMap().broadcastMessage(PlayerShopPacket.destroyHiredMerchant(getOwnerId()));
        }
        getMap().removeMapObject(this);
//        try {//
//            for (ChannelServer ch : ChannelServer.getAllInstances()) {
//                MapleMap map = null;
//                for (int i = 910000001; i <= 910000022; i++) {
//                    map = ch.getMapFactory().getMap(i);
//                    if (map != null) {                        
//                        List<MapleMapObject> HMS = map.getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.HIRED_MERCHANT));
//                        for (MapleMapObject HM : HMS) {
//                            HiredMerchant HMM = (HiredMerchant) HM;
//                            if (HMM.getOwnerId() == getOwnerId()) {
//                                map.removeMapObject(this);
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            int aaa = 0;
//
//        }

        this.schedule = null;
    }

    public int getTimeLeft() {
        return (int) ((System.currentTimeMillis() - start) / 1000);
    }

    public final int getStoreId() {
        return storeid;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.HIRED_MERCHANT;
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        if (isAvailable()) {
            client.sendPacket(PlayerShopPacket.destroyHiredMerchant(getOwnerId()));
        }
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        if (isAvailable()) {
            client.sendPacket(PlayerShopPacket.spawnHiredMerchant(this));
        }
    }

    public final boolean isInBlackList(final String bl) {
        return this.blacklist.contains(bl);
    }

    public final void addBlackList(final String bl) {
        this.blacklist.add(bl);
    }

    public final void removeBlackList(final String bl) {
        this.blacklist.remove(bl);
    }

    public final void sendBlackList(final MapleClient c) {
        c.sendPacket(PlayerShopPacket.MerchantBlackListView(this.blacklist));
    }

    public final void sendVisitor(final MapleClient c) {
        c.sendPacket(PlayerShopPacket.MerchantVisitorView(this.visitors));
    }

    public void 修改道具(int a1, int a2, int a3) {;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM hire WHERE cid = " + a1 + " && itemid = " + a2 + "");
            rs = ps1.executeQuery();
            if (rs.next()) {
                String sqlString1 = null;
                sqlString1 = "update hire set upgradeslots = " + a3 + " where cid=" + a1 + " && itemid = " + a2 + ";";
                PreparedStatement name = DatabaseConnection.getConnection().prepareStatement(sqlString1);
                name.executeUpdate(sqlString1);
            }
        } catch (SQLException ex) {

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
                ps.close();

            }
        } catch (SQLException ex) {

        }
    }
}
