package server;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.*;
import constants.GameConstants;
import database.DatabaseConnection;
import tools.Pair;
import tools.packet.MTSCSPacket;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static abc.Game.*;

public class CashShop implements Serializable {

    private static final long serialVersionUID = 231541893513373579L;
    private final int accountId;
    private final int characterId;
    private final ItemLoader factory;
    private final List<IItem> inventory = new ArrayList<IItem>();
    private final List<Integer> uniqueids = new ArrayList<Integer>();

    public CashShop(int accountId, int characterId, int jobType) throws SQLException {
        this.accountId = accountId;
        this.characterId = characterId;

        if (jobType / 1000 == 1) {
            factory = ItemLoader.CASHSHOP_CYGNUS;
        } else if ((jobType / 100 == 21 || jobType / 100 == 20) && jobType != 2001) {
            factory = ItemLoader.CASHSHOP_ARAN;
        } else if (jobType == 2001 || jobType / 100 == 22) {
            factory = ItemLoader.CASHSHOP_EVAN;
        } else if (jobType >= 3000) {
            factory = ItemLoader.CASHSHOP_RESIST;
        } else if (jobType / 10 == 43) {
            factory = ItemLoader.CASHSHOP_DB;
        } else {
            factory = ItemLoader.CASHSHOP_EXPLORER;
        }

        for (Pair<IItem, MapleInventoryType> item : factory.loadItems(false, accountId).values()) {
            inventory.add(item.getLeft());
        }

    }

    public int getItemsSize() {
        return inventory.size();
    }

    public List<IItem> getInventory() {
        return inventory;
    }

    public IItem findByCashId(int cashId) {
        for (IItem item : inventory) {
            if (item.getUniqueId() == cashId) {
                return item;
            }
        }
        return null;
    }

    public void checkExpire(MapleClient c) {
        List<IItem> toberemove = new ArrayList<IItem>();
        for (IItem item : inventory) {
            if (item != null && !GameConstants.isPet(item.getItemId()) && item.getExpiration() > 0 && item.getExpiration() < System.currentTimeMillis()) {
                toberemove.add(item);
            }
        }
        if (toberemove.size() > 0) {
            for (IItem item : toberemove) {
                removeFromInventory(item);
                c.sendPacket(MTSCSPacket.cashItemExpired(item.getUniqueId()));
            }
            toberemove.clear();
        }
    }

    public IItem toItemA(CashItemInfoA cItem) {
        return toItemA(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), "");
    }

    public IItem toItemA(CashItemInfoA cItem, String gift) {
        return toItemA(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), gift);
    }

    public IItem toItemA(CashItemInfoA cItem, int uniqueid) {
        return toItemA(cItem, uniqueid, "");
    }

    public IItem toItemA(CashItemInfoA cItem, int uniqueid, String gift) {
        if (uniqueid <= 0) {
            uniqueid = MapleInventoryIdentifier.getInstance();
        }
        long period = cItem.getPeriod();
        if (period <= 0 || GameConstants.isPet(cItem.getId())) {
            period = 45;
        }
        IItem ret = null;
        if (GameConstants.getInventoryType(cItem.getId()) == MapleInventoryType.EQUIP) {
            Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(cItem.getId());
            eq.setUniqueId(uniqueid);
            eq.setExpiration(System.currentTimeMillis() + (period * 24 * 60 * 60 * 1000));
            eq.setGiftFrom(gift);
            if (GameConstants.isEffectRing(cItem.getId()) && uniqueid > 0) {
                MapleRing ring = MapleRing.loadFromDb(uniqueid);
                if (ring != null) {
                    eq.setRing(ring);
                }
            }
            ret = eq.copy();
        } else {
            Item item = new Item(cItem.getId(), (byte) 0, (short) cItem.getCount(), (byte) 0, uniqueid);
            item.setExpiration(System.currentTimeMillis() + (period * 24 * 60 * 60 * 1000));
            item.setGiftFrom(gift);
            if (GameConstants.isPet(cItem.getId())) {
                final MaplePet pet = MaplePet.createPet(cItem.getId(), uniqueid);
                if (pet != null) {
                    item.setPet(pet);
                }
            }
            ret = item.copy();
        }
        return ret;
    }

    public IItem toItem(CashItemInfo cItem) {
        return toItem(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), "");
    }

    public IItem toItem(CashItemInfo cItem, String gift) {
        return toItem(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), gift);
    }

    public IItem toItem(CashItemInfo cItem, int uniqueid) {
        return toItem(cItem, uniqueid, "");
    }

//商店限时
    public IItem toItem(CashItemInfo cItem, int uniqueid, String gift) {
        if (uniqueid <= 0) {
            uniqueid = MapleInventoryIdentifier.getInstance();
        }
        long period = cItem.getPeriod();
        if (GameConstants.isPet(cItem.getId())) {
            period = 90;
            //幸运道符*拥有幸运道符，打猎时消耗一个，怪物将会掉落一次经验包
            //装备租赁券
        } else if (cItem.getId() >= 5210000 && cItem.getId() <= 5360099 && cItem.getId() != 5220007 && cItem.getId() != 5220008) {
        } else {
            period = 0;
        }
        IItem ret = null;
        if (GameConstants.getInventoryType(cItem.getId()) == MapleInventoryType.EQUIP) {
            Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(cItem.getId());
            eq.setUniqueId(uniqueid);
            if (GameConstants.isPet(cItem.getId()) || period > 0) {
                eq.setExpiration(System.currentTimeMillis() + (period * 24 * 60 * 60 * 1000));
            }
            eq.setGiftFrom(gift);
            if (GameConstants.isEffectRing(cItem.getId()) && uniqueid > 0) {
                MapleRing ring = MapleRing.loadFromDb(uniqueid);
                if (ring != null) {
                    eq.setRing(ring);
                }
            }
            ret = eq.copy();
        } else {
            Item item = new Item(cItem.getId(), (byte) 0, (short) cItem.getCount(), (byte) 0, uniqueid);
            if (period > 0) {
                item.setExpiration(System.currentTimeMillis() + (period * 24 * 60 * 60 * 1000));
            }

            if (三小时限时道具(cItem.getId())) {
                item.setExpiration(System.currentTimeMillis() + (long) (3 * 60 * 60 * 1000));
            } else if (两小时限时道具(cItem.getId())) {
                item.setExpiration(System.currentTimeMillis() + (long) (2 * 60 * 60 * 1000));
            } else if (一天时限时道具(cItem.getId())) {
                item.setExpiration(System.currentTimeMillis() + (long) (1 * 24 * 60 * 60 * 1000));
            } else if (七天时限时道具(cItem.getId())) {
                item.setExpiration(System.currentTimeMillis() + (long) (7 * 24 * 60 * 60 * 1000));
            }

            //周末4小时卡
            if (cItem.getId() == 5211108) {
                item.setExpiration(System.currentTimeMillis() + (long) (4 * 60 * 60 * 1000));
            }
            if (cItem.getId() == 5211109) {
                item.setExpiration(System.currentTimeMillis() + (long) (4 * 60 * 60 * 1000));
            }

            //  System.out.println(new Date(System.currentTimeMillis() + (long) (3 * 60 * 60 * 1000)));
            //item.setExpiration((long) (System.currentTimeMillis() + (long) (period * 24 * 60 * 60 * 1000)));
            item.setGiftFrom(gift);
            if (GameConstants.isPet(cItem.getId())) {
                final MaplePet pet = MaplePet.createPet(cItem.getId(), uniqueid);
                if (pet != null) {
                    item.setPet(pet);
                }
            }
            ret = item.copy();
        }
        return ret;
    }

    public void addToInventory(IItem item) {
        inventory.add(item);
    }

    public void removeFromInventory(IItem item) {
        inventory.remove(item);
    }

    public void gift(int recipient, String from, String message, int sn) {
        gift(recipient, from, message, sn, 0);
    }

    public void gift(int recipient, String from, String message, int sn, int uniqueid) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO `gifts` VALUES (DEFAULT, ?, ?, ?, ?, ?)");
            ps.setInt(1, recipient);
            ps.setString(2, from);
            ps.setString(3, message);
            ps.setInt(4, sn);
            ps.setInt(5, uniqueid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public List<Pair<IItem, String>> loadGifts() {
        List<Pair<IItem, String>> gifts = new ArrayList<Pair<IItem, String>>();
        Connection con = DatabaseConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM `gifts` WHERE `recipient` = ?");
            ps.setInt(1, characterId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CashItemInfo cItem = CashItemFactory.getInstance().getItem(rs.getInt("sn"));
                IItem item = toItem(cItem, rs.getInt("uniqueid"), rs.getString("from"));
                gifts.add(new Pair<IItem, String>(item, rs.getString("message")));
                uniqueids.add(item.getUniqueId());
                List<CashItemInfo> packages = CashItemFactory.getInstance().getPackageItems(cItem.getId());
                if (packages != null && packages.size() > 0) {
                    for (CashItemInfo packageItem : packages) {
                        addToInventory(toItem(packageItem, rs.getString("from")));
                    }
                } else {
                    addToInventory(item);
                }
            }

            rs.close();
            ps.close();
            ps = con.prepareStatement("DELETE FROM `gifts` WHERE `recipient` = ?");
            ps.setInt(1, characterId);
            ps.executeUpdate();
            ps.close();
            save();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return gifts;
    }

    public boolean canSendNote(int uniqueid) {
        return uniqueids.contains(uniqueid);
    }

    public void sendedNote(int uniqueid) {
        for (int i = 0; i < uniqueids.size(); i++) {
            if (uniqueids.get(i).intValue() == uniqueid) {
                uniqueids.remove(i);
            }
        }
    }

    public void save() throws SQLException {
        List<Pair<IItem, MapleInventoryType>> itemsWithType = new ArrayList<Pair<IItem, MapleInventoryType>>();

        for (IItem item : inventory) {
            itemsWithType.add(new Pair<IItem, MapleInventoryType>(item, GameConstants.getInventoryType(item.getItemId())));
        }

        factory.saveItems(itemsWithType, accountId);
    }

    public IItem toItem(CashItemInfo cItem, MapleCharacter chr, int uniqueid, String gift) {
        if (uniqueid <= 0) {
            uniqueid = MapleInventoryIdentifier.getInstance();
        }

        IItem ret = null;
        if (GameConstants.getInventoryType(cItem.getId()) == MapleInventoryType.EQUIP) {
            Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(cItem.getId());
            eq.setUniqueId(uniqueid);
            eq.setGiftFrom(gift);
            if (GameConstants.isEffectRing(cItem.getId()) && uniqueid > 0) {
                MapleRing ring = MapleRing.loadFromDb(uniqueid);
                if (ring != null) {
                    eq.setRing(ring);
                }
            }
            ret = eq.copy();
        } else {
            Item item = new Item(cItem.getId(), (byte) 0, (short) cItem.getCount(), (byte) 0, uniqueid);
            item.setGiftFrom(gift);
            if (GameConstants.isPet(cItem.getId())) {
                final MaplePet pet = MaplePet.createPet(cItem.getId(), uniqueid);
                if (pet != null) {
                    item.setPet(pet);
                }
            }
            ret = item.copy();
        }
        return ret;
    }
}
