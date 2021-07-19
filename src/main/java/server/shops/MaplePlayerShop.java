package server.shops;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.IItem;
import client.inventory.ItemFlag;
import server.MapleInventoryManipulator;
import tools.packet.PlayerShopPacket;

import java.util.ArrayList;
import java.util.List;

public class MaplePlayerShop extends AbstractPlayerStore {

    private int boughtnumber = 0;
    private final List<String> bannedList = new ArrayList<String>();

    public MaplePlayerShop(MapleCharacter owner, int itemId, String desc) {
        super(owner, itemId, desc, "", 3);
    }

    @Override
    public void buy(MapleClient c, int item, short quantity) {
        MaplePlayerShopItem pItem = items.get(item);
        if (pItem.bundles > 0) {
            IItem newItem = pItem.item.copy();
            newItem.setQuantity((short) (quantity * newItem.getQuantity()));
            byte flag = newItem.getFlag();

            if (ItemFlag.KARMA_EQ.check(flag)) {
                newItem.setFlag((byte) (flag - ItemFlag.KARMA_EQ.getValue()));
            } else if (ItemFlag.KARMA_USE.check(flag)) {
                newItem.setFlag((byte) (flag - ItemFlag.KARMA_USE.getValue()));
            }
            final int gainmeso = pItem.price * quantity;
            if (c.getPlayer().getMeso() >= gainmeso) {
                if (getMCOwner().getMeso() + gainmeso > 0 && MapleInventoryManipulator.checkSpace(c, newItem.getItemId(), newItem.getQuantity(), newItem.getOwner()) && MapleInventoryManipulator.addFromDrop(c, newItem, false)) {
                    pItem.bundles -= quantity;
                    bought.add(new BoughtItem(newItem.getItemId(), quantity, gainmeso, c.getPlayer().getName()));
                    c.getPlayer().gainMeso(-gainmeso, false);
                    getMCOwner().gainMeso(gainmeso, false);
                    if (pItem.bundles <= 0) {
                        boughtnumber++;
                        if (boughtnumber == items.size()) {
                            closeShop(false, true);
                            return;
                        }
                    }
                } else {
                    c.getPlayer().dropMessage(1, "你的库存已经满了。");
                }
            } else {
                c.getPlayer().dropMessage(1, "你没有足够的金币。");
                //}
            }
            getMCOwner().getClient().sendPacket(PlayerShopPacket.shopItemUpdate(this));
        }
    }

    @Override
    public byte getShopType() {
        return IMaplePlayerShop.PLAYER_SHOP;
    }

    @Override
    public void closeShop(boolean saveItems, boolean remove) {
        MapleCharacter owner = getMCOwner();
        removeAllVisitors(10, 1);
        getMap().removeMapObject(this);

        for (MaplePlayerShopItem items : getItems()) {
            if (items.bundles > 0) {
                IItem newItem = items.item.copy();
                newItem.setQuantity((short) (items.bundles * newItem.getQuantity()));
                if (MapleInventoryManipulator.addFromDrop(owner.getClient(), newItem, false)) {
                    items.bundles = 0;
                } else {
                    saveItems(); //O_o
                    break;
                }
            }
        }
        owner.setPlayerShop(null);
        update();
        getMCOwner().getClient().sendPacket(PlayerShopPacket.shopErrorMessage(10, 1));
    }

    public void banPlayer(String name) {
        if (!bannedList.contains(name)) {
            bannedList.add(name);
        }
        for (int i = 0; i < 3; i++) {
            MapleCharacter chr = getVisitor(i);
            if (chr.getName().equals(name)) {
                chr.getClient().sendPacket(PlayerShopPacket.shopErrorMessage(5, 1));
                chr.setPlayerShop(null);
                removeVisitor(chr);
            }
        }
    }

    public boolean isBanned(String name) {
        return bannedList.contains(name);
    }
}
