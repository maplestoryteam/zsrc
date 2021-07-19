package server;

import client.inventory.IItem;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import tools.Pair;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MTSCart implements Serializable {

    private static final long serialVersionUID = 231541893513373578L;
    private final int characterId;
    private int tab = 1;
    private int type = 0;
    private int page = 0;
    //tab; 1 = buy now, 2 = wanted, 3 = auction, 4 = cart
    //type = inventorytype; 0 = anything
    //page = whatever
    private final List<IItem> transfer = new ArrayList<IItem>();
    private final List<Integer> cart = new ArrayList<Integer>();
    private final List<Integer> notYetSold = new ArrayList<Integer>(10);
    private int owedNX = 0;

    public MTSCart(int characterId) throws SQLException {
        this.characterId = characterId;
        for (Pair<IItem, MapleInventoryType> item : ItemLoader.MTS_TRANSFER.loadItems(false, characterId).values()) {
            transfer.add(item.getLeft());
        }
        loadCart();
        loadNotYetSold();
    }

    public List<IItem> getInventory() {
        return transfer;
    }

    public void addToInventory(IItem item) {
        transfer.add(item);
    }

    public void removeFromInventory(IItem item) {
        transfer.remove(item);
    }

    public List<Integer> getCart() {
        return cart;
    }

    public boolean addToCart(final int car) {
        if (!cart.contains(car)) {
            cart.add(car);
            return true;
        }
        return false;
    }

    public void removeFromCart(final int car) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i) == car) {
                cart.remove(i);
            }
        }
    }

    public List<Integer> getNotYetSold() {
        return notYetSold;
    }

    public void addToNotYetSold(final int car) {
        notYetSold.add(car);
    }

    public void removeFromNotYetSold(final int car) {
        for (int i = 0; i < notYetSold.size(); i++) {
            if (notYetSold.get(i) == car) {
                notYetSold.remove(i);
            }
        }
    }

    public final int getSetOwedNX() {
        final int on = owedNX;
        owedNX = 0;
        return on;
    }

    public void increaseOwedNX(final int newNX) {
        owedNX += newNX;
    }

    public void save() throws SQLException {
        List<Pair<IItem, MapleInventoryType>> itemsWithType = new ArrayList<Pair<IItem, MapleInventoryType>>();

        for (IItem item : getInventory()) {
            itemsWithType.add(new Pair<IItem, MapleInventoryType>(item, GameConstants.getInventoryType(item.getItemId())));
        }

        ItemLoader.MTS_TRANSFER.saveItems(itemsWithType, characterId);
        final Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM mts_cart WHERE characterid = ?");
        ps.setInt(1, characterId);
        ps.execute();
        ps.close();
        ps = con.prepareStatement("INSERT INTO mts_cart VALUES(DEFAULT, ?, ?)");
        ps.setInt(1, characterId);
        for (int i : cart) {
            ps.setInt(2, i);
            ps.executeUpdate();
        }
        if (owedNX > 0) {
            ps.setInt(2, -owedNX);
            ps.executeUpdate();
        }
        ps.close();
        //notYetSold shouldnt be saved here
    }

    public void loadCart() throws SQLException {
        final PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM mts_cart WHERE characterid = ?");
        ps.setInt(1, characterId);
        final ResultSet rs = ps.executeQuery();
        int iId;
        while (rs.next()) {
            iId = rs.getInt("itemid");
            if (iId < 0) {
                owedNX -= iId;
            } else if (MTSStorage.getInstance().check(iId)) {
                cart.add(iId);
            }
        }
        rs.close();
        ps.close();
    }

    public void loadNotYetSold() throws SQLException {
        final PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM mts_items WHERE characterid = ?");
        ps.setInt(1, characterId);
        final ResultSet rs = ps.executeQuery();
        int pId;
        while (rs.next()) {
            pId = rs.getInt("id");
            if (MTSStorage.getInstance().check(pId)) {
                notYetSold.add(pId);
            }
        }
        rs.close();
        ps.close();
    }

    public void changeInfo(final int tab, final int type, final int page) {
        this.tab = tab;
        this.type = type;
        this.page = page;
    }

    public int getTab() {
        return tab;
    }

    public int getType() {
        return type;
    }

    public int getPage() {
        return page;
    }
}
