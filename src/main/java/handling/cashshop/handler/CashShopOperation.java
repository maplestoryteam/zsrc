/*
游戏商城
 */
package handling.cashshop.handler;

import static a.用法大全.验证商城物品;
import abc.商城检测文件;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import constants.GameConstants;
import client.MapleClient;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.inventory.*;
import database.DatabaseConnection;
import gui.控制台.游戏商城登陆显示窗;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import static handling.channel.handler.InterServerHandler.角色ID取账号ID;
import handling.login.LoginServer;
import static handling.login.handler.CharLoginHandler.Gaincharacterz;
import static handling.login.handler.CharLoginHandler.Getcharacterz;
import handling.world.CharacterTransfer;
import handling.world.MapleParty;
import handling.world.World;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import scripting.NPCConversationManager;
import static scripting.NPCConversationManager.提取点券库存;
import static scripting.NPCConversationManager.账号ID取账号;
import server.CashItemFactory;
import server.CashItemInfo;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import static gui.QQMsgServer.sendMsg;
import java.util.Calendar;
import server.ServerProperties;
import server.custom.bossrank.BossRankManager;
import tools.FileoutputUtil;
import static tools.FileoutputUtil.CurrentReadable_Time;
import tools.MaplePacketCreator;
import tools.packet.MTSCSPacket;
import tools.Pair;
import tools.data.LittleEndianAccessor;

public class CashShopOperation {

    //private static 游戏商城登陆显示窗 输出日志 = new 游戏商城登陆显示窗();
    //退出商城
    public static void LeaveCS(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        //final String login = 账号ID取账号(角色名字取账号ID(c.getPlayer().getName()));
        String[] socket = c.getChannelServer().getIP().split(":");
        CashShopServer.getPlayerStorageMTS().deregisterPlayer(chr);
        CashShopServer.getPlayerStorage().deregisterPlayer(chr);
        //c.getChannelServer().removePlayer(c.getPlayer());  
        String ip = c.getSessionIPAddress();
        LoginServer.putLoginAuth(chr.getId(), ip.substring(ip.indexOf('/') + 1, ip.length()), c.getTempIP(), c.getChannel());
        c.updateLoginState(1, ip);
        //  c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
        String 输出 = "[服务端]" + CurrentReadable_Time() + " : 退出商城 玩家 : " + c.getPlayer().getName() + " ";
        World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:" + 输出));
        System.err.println(输出);
        //输出日志.游戏商城登陆显示窗(输出);
        try {
            chr.saveToDB(false, false);
            //chr.saveToDB(false, false);
            c.setReceiving(false);
            World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
            c.sendPacket(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //输出日志.show();
    }

    //进入商城
    public static void EnterCS(final int playerid, final MapleClient c) {

        CharacterTransfer transfer = CashShopServer.getPlayerStorage().getPendingCharacter(playerid);
        boolean mts = false;
        if (transfer == null) {
            transfer = CashShopServer.getPlayerStorageMTS().getPendingCharacter(playerid);
            mts = true;
            if (transfer == null) {
                c.getSession().close();
                return;
            }
        }
        MapleCharacter chr = MapleCharacter.ReconstructChr(transfer, c, false);
        //整个角色信息
        c.setPlayer(chr);
        //进入商城的账号ID
        c.setAccID(chr.getAccountID());
        //取该账号下有多少个角色
//        System.err.println("[点券]；" + c.getPlayer().getCSPoints(1));
//        System.err.println("[点券2]；" + NPCConversationManager.提取点券库存(chr.getAccountID()));
//        if (c.getPlayer().getCSPoints(1) == NPCConversationManager.提取点券库存(chr.getAccountID())) {
//            c.getPlayer().dropMessage(1, "警告：\r\n检测到你使用非法手段，企图破坏利用游戏漏洞破坏游戏平衡。你的账号已被封禁。");
//
//        }
        String 输出 = "[服务端]" + CurrentReadable_Time() + " : 进入商城 玩家 : " + c.getPlayer().getName() + "";
        World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:" + 输出));
        System.err.println(输出);
        //输出日志.游戏商城登陆显示窗(输出);
        //远程黑客攻击???
        if (!c.CheckIPAddress()) {
            c.getSession().close();
            return;
        }
        final int state = c.getLoginState();
        boolean allowLogin = false;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL) {
            if (!World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()))) {
                allowLogin = true;
            }
        }
        if (!allowLogin) {
            c.setPlayer(null);
            c.getSession().close();
            return;
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        if (mts) {
            CashShopServer.getPlayerStorage().registerPlayer(chr);
            c.sendPacket(MTSCSPacket.warpCSS(c));
            CSUpdate(c);
            /*  CashShopServer.getPlayerStorageMTS().registerPlayer(chr);
            c.sendPacket(MTSCSPacket.startMTS(chr, c));
            MTSOperation.MTSUpdate(MTSStorage.getInstance().getCart(c.getPlayer().getId()), c);*/
        } else {
            CashShopServer.getPlayerStorage().registerPlayer(chr);
            c.sendPacket(MTSCSPacket.warpCS(c));
            CSUpdate(c);
        }
        //输出日志.show();
    }

    public static void CSUpdate(final MapleClient c) {

        c.sendPacket(MTSCSPacket.showCashInventory(c));
        c.sendPacket(MTSCSPacket.sendWishList(c.getPlayer(), false));
        c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        //   c.sendPacket(MTSCSPacket.getCSInventory(c));
        c.sendPacket(MTSCSPacket.getCSGifts(c));
        //c.sendPacket(MTSCSPacket.getCSInventory(c));
        //  doCSPackets(c);
    }

    public static void TouchingCashShop(final MapleClient c) {
        c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
    }

    public static void CouponCode(final String code, final MapleClient c) {
        boolean validcode = false;
        int type = -1;
        int item = -1;

        try {
            validcode = MapleCharacterUtil.getNXCodeValid(code.toUpperCase(), validcode);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (validcode) {
            try {
                type = MapleCharacterUtil.getNXCodeType(code);
                item = MapleCharacterUtil.getNXCodeItem(code);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (type != 4) {
                try {
                    MapleCharacterUtil.setNXCodeUsed(c.getPlayer().getName(), code);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            Map<Integer, IItem> itemz = new HashMap<Integer, IItem>();
            int maplePoints = 0, mesos = 0;
            switch (type) {
                case 1:
                case 2:
                    c.getPlayer().modifyCSPoints(type, item, false);
                    maplePoints = item;
                    break;
                case 3:
                    CashItemInfo itez = CashItemFactory.getInstance().getItem(item);
                    if (itez == null) {
                        c.sendPacket(MTSCSPacket.sendCSFail(0));
                        doCSPackets(c);
                        return;
                    }
                    byte slot = MapleInventoryManipulator.addId(c, itez.getId(), (short) 1, "", (byte) 0);
                    if (slot <= -1) {
                        c.sendPacket(MTSCSPacket.sendCSFail(0));
                        doCSPackets(c);
                        return;
                    } else {
                        itemz.put(item, c.getPlayer().getInventory(GameConstants.getInventoryType(item)).getItem(slot));
                    }
                    break;
                case 4:
                    c.getPlayer().modifyCSPoints(1, item, false);
                    maplePoints = item;
                    break;
                case 5:
                    c.getPlayer().gainMeso(item, false);
                    mesos = item;
                    break;
            }
            c.sendPacket(MTSCSPacket.showCouponRedeemedItem(itemz, mesos, maplePoints, c));
        } else {
            c.sendPacket(MTSCSPacket.sendCSFail(validcode ? 0xA5 : 0xA7)); //A1, 9F
        }
        doCSPackets(c);
    }

    //购买物品
    public static void BuyCashItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) throws SQLException {
        //商城检测文件 itemjc = new 商城检测文件();
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

        final int action = slea.readByte();
        if (MapleParty.调试3 > 0) {
            System.out.println("BuyCashItem:" + action + "");
        }
        //购买商城物品
        switch (action) {
            case 3: {
                int useNX = slea.readByte() + 1;
                int snCS = slea.readInt();
                CashItemInfo item = CashItemFactory.getInstance().getItem(snCS);
                if (chr.getCSPoints(useNX) >= item.getPrice()) {
                    int 商城检测开关 = gui.Start.ConfigValuesMap.get("商城检测开关");
                    if (商城检测开关 <= 0) {
                        //String items1 = itemjc.getid();
                        //if (!items1.contains("," + item.getId() + ",")) {
                        if (验证商城物品(item.getId()) == 0) {
                            c.getPlayer().dropMessage(1, "购买物品不存在。");
                            sendMsg("<" + MapleParty.开服名字 + ">机器人: 警告，警告，玩家 " + chr.getName() + " 购买未经过允许的商城物品。代码是: " + item.getId(), "71447500");
                            if (!"71447500".equals(ServerProperties.getProperty("ZEV.QQ1"))) {
                                sendMsg("<" + MapleParty.开服名字 + ">机器人: 警告，警告，玩家 " + chr.getName() + " 购买未经过允许的商城物品。代码是: " + item.getId(), "" + ServerProperties.getProperty("ZEV.QQ1") + "");
                            }
                            if (!ServerProperties.getProperty("ZEV.QQ1").equals(ServerProperties.getProperty("ZEV.QQ2"))) {
                                sendMsg("<" + MapleParty.开服名字 + ">机器人: 警告，警告，玩家 " + chr.getName() + " 购买未经过允许的商城物品。代码是: " + item.getId(), "" + ServerProperties.getProperty("ZEV.QQ2") + "");
                            }
                            doCSPackets(c);
                            return;
                        }
                    }
                    //if (chr.getCSPoints(1) >= item.getPrice()) {
                    if (!ii.isCash(item.getId())) {
                        if (c.getPlayer().getInventory(GameConstants.getInventoryType(item.getId())).getNextFreeSlot() < 0) {
                            chr.dropMessage(1, "背包没空位了！");
                            doCSPackets(c);
                            return;
                        }
                        byte pos = MapleInventoryManipulator.addId(c, item.getId(), (short) item.getCount(), null, item.getPeriod(), (byte) 0);
                        if (pos < 0) {
                            chr.dropMessage(1, "背包坐标出错！\r\n可能是背包没空位了！");
                            doCSPackets(c);
                            return;
                        }
                        chr.modifyCSPoints(useNX, -item.getPrice(), false);
                        chr.dropMessage(1, "购买成功！\r\n物品自动放入了背包！");
                    } else {
                        //如果大于或者等于0就是限量物品
                        int 库存 = Getcharacter7("" + item.getSN() + "", 2);
                        int 物品数量 = item.getCount();
                        //如果该物品有库存信息
                        if (库存 >= 0) {
                            //如果是限量物品，判断是否够数量购买
                            if (库存 >= 物品数量) {
                                //减去限量物品库存数量
                                Gaincharacter7("" + item.getSN() + "", 2, -物品数量);
                                chr.dropMessage(1, "购买成功，剩余库存:" + 库存);
                            } else {
                                chr.dropMessage(1, "购买失败，剩余库存:" + 库存);
                                doCSPackets(c);
                                return;
                            }
                        }
//                        if (类型 == 1) {
                        int 折扣 = Getcharacter7("" + item.getSN() + "", 3);
                        int 回扣 = item.getPrice() * 折扣 / 100;
                        if (折扣 > 0) {
                            chr.modifyCSPoints(1, 回扣, false);
                            chr.dropMessage(1, "该物品反馈折扣(" + 折扣 + "%)\r\n"
                                    + "获得回扣点券:" + 回扣);
                            doCSPackets(c);
                        }
//                        }
                        int 每日限购 = Getcharacter7("" + item.getSN() + "", 4);
                        if (每日限购 >= 0) {
                            if (chr.getBossLog("" + item.getSN() + "") >= 每日限购) {
                                chr.dropMessage(1, "你今天不能再购买此类商品。");
                                doCSPackets(c);
                                return;
                            } else {
                                chr.setBossLog("" + item.getSN() + "");
                            }
                        }
                        int 时 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        int 分 = Calendar.getInstance().get(Calendar.MINUTE);
                        if (gui.Start.ConfigValuesMap.get("打折优惠开关") == 0) {
                            if ((时 == 11 && 分 == 11) || (时 == 23 && 分 == 11)) {
                                chr.modifyCSPoints(useNX, -item.getPrice() / 2, false);
                            } else {
                                chr.modifyCSPoints(useNX, -item.getPrice(), false);
                            }
                        } else {
                            chr.modifyCSPoints(useNX, -item.getPrice(), false);
                        }
                        if (MapleParty.商城出售通知 == 0) {
                            sendMsg("<" + MapleParty.开服名字 + ">商城出售物品通知\r\n"
                                    + "玩家名字；" + chr.getName() + " \r\n"
                                    + "购买物品；" + MapleItemInformationProvider.getInstance().getName(item.getId()) + "(" + item.getId() + ") \r\n"
                                    + "消耗点卷；" + item.getPrice(), "71447500");
                            if (!"71447500".equals(ServerProperties.getProperty("ZEV.QQ1"))) {
                                sendMsg("<" + MapleParty.开服名字 + ">商城出售物品通知\r\n"
                                        + "玩家名字；" + chr.getName() + " \r\n"
                                        + "购买物品；" + MapleItemInformationProvider.getInstance().getName(item.getId()) + "(" + item.getId() + ") \r\n"
                                        + "消耗点卷；" + item.getPrice(), "" + ServerProperties.getProperty("ZEV.QQ1") + "");
                            }
                            if (!"71447500".equals(ServerProperties.getProperty("ZEV.QQ1"))) {
                                sendMsg("<" + MapleParty.开服名字 + ">商城出售物品通知\r\n"
                                        + "玩家名字；" + chr.getName() + " \r\n"
                                        + "购买物品；" + MapleItemInformationProvider.getInstance().getName(item.getId()) + "(" + item.getId() + ") \r\n"
                                        + "消耗点卷；" + item.getPrice(), "" + ServerProperties.getProperty("ZEV.QQ2") + "");
                            }
                        }
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 商城 : 游戏商城");
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 玩家 : " + c.getPlayer().getName() + " 购买 ");
                        System.err.println("[服务端]" + CurrentReadable_Time() + " : 购买 : " + MapleItemInformationProvider.getInstance().getName(item.getId()) + " x " + 物品数量 + " 个");
                        if (gui.Start.ConfigValuesMap.get("打折优惠开关") == 0) {
                            if ((时 == 11 && 分 == 11) || (时 == 23 && 分 == 11)) {
                                System.err.println("[服务端]" + CurrentReadable_Time() + " : (5折)消耗 : " + item.getPrice() / 2 + " 点券");
                            } else {
                                System.err.println("[服务端]" + CurrentReadable_Time() + " : 消耗 : " + item.getPrice() + " 点券");
                            }
                        } else {
                            System.err.println("[服务端]" + CurrentReadable_Time() + " : 消耗 : " + item.getPrice() + " 点券");
                        }

                        BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "游戏消耗点券", (byte) 2, item.getPrice());
                        if (c.getPlayer().getBossLog("日常商城购买") <= 0) {
                            c.getPlayer().setBossLog("日常商城购买");//为自己加记录
                        }
                        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO Sell_goods ( name,item,itemname,shuliang,jiage,shijian) VALUES ( ?, ?, ?, ?, ?,?)")) {
                            ps.setString(1, chr.getName());
                            ps.setInt(2, item.getId());
                            ps.setString(3, MapleItemInformationProvider.getInstance().getName(item.getId()));
                            ps.setInt(4, item.getCount());
                            ps.setInt(5, item.getPrice());
                            ps.setString(6, CurrentReadable_Time());
                            ps.executeUpdate();
                        } catch (SQLException ex) {

                        }
                        //String 输出 = "[服务端]" + CurrentReadable_Time() + " : 玩家：" + chr.getName() + " 购买物品：" + MapleItemInformationProvider.getInstance().getName(item.getId()) + "(" + item.getId() + ") 消耗点卷：" + item.getPrice();
                        //输出日志.游戏商城登陆显示窗(输出);
                        //System.err.println(输出);
//                        System.err.println("[道具]:" + item.getId());
//                        System.err.println("[SN]:" + item.getSN());
//                        System.err.println("[价格]:" + item.getPrice());
                        //记录商城出售的物品
                        Gaincharacter7("" + item.getSN() + "", 1, 物品数量);
                        IItem itemz = chr.getCashInventory().toItem(item);
                        if (itemz != null && itemz.getUniqueId() > 0 && itemz.getItemId() == item.getId() && itemz.getQuantity() == item.getCount()) {
                            if (useNX == 1) {
                                byte flag = itemz.getFlag();
                                boolean 交易 = true;
                                if (交易 == true) {
                                    if (itemz.getType() == MapleInventoryType.EQUIP.getType()) {
                                        flag |= ItemFlag.KARMA_EQ.getValue();
                                    } else {
                                        flag |= ItemFlag.KARMA_USE.getValue();
                                    }
                                    itemz.setFlag(flag);
                                }
                            }
                            //给我物品？
                            chr.getCashInventory().addToInventory(itemz);
                            c.sendPacket(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
                        } else {
                            c.sendPacket(MTSCSPacket.sendCSFail(0));
                        }
                    }
                } else {
                    c.sendPacket(MTSCSPacket.sendCSFail(0));
                    chr.dropMessage(1, "买不起买不起。");
                    doCSPackets(c);
                    return;
                }
                c.getPlayer().saveToDB(true, true);
                c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer())); //显示点卷
                c.sendPacket(MaplePacketCreator.enableActions()); //能行动
                break;
            }

            case 26:
                chr.dropMessage(1, "换购未开启。");
                doCSPackets(c);
                return;
            /*int useNX = slea.readByte() + 1;
                int snCS = slea.readInt();
                CashItemInfo item = CashItemFactory.getInstance().getItem(snCS);
                int 换购物品 = item.getId();
                
                chr.dropMessage(1, "你要换购" + 换购物品 + "?");*/
            case 4:
            case 32:
                c.getPlayer().getClient().sendPacket(MaplePacketCreator.serverNotice(1, "禁止商城赠送礼物！"));
                break;
            case 5:
                // Wishlist
                chr.clearWishlist();
                if (slea.available() < 40) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                }
                int[] wishlist = new int[10];
                for (int i = 0; i < 10; i++) {
                    wishlist[i] = slea.readInt();
                }
                chr.setWishlist(wishlist);
                c.sendPacket(MTSCSPacket.sendWishList(chr, true));
                break;
            case 6: {
                // Increase inv
                int useNX = slea.readByte() + 1;
                final boolean coupon = slea.readByte() > 0;
                if (coupon) {
                    final MapleInventoryType type = getInventoryType(slea.readInt());

                    if (chr.getCSPoints(useNX) >= 4800 && chr.getInventory(type).getSlotLimit() < 89) {//89
                        chr.modifyCSPoints(useNX, -4800, false);
                        chr.getInventory(type).addSlot((byte) 8);
                        chr.dropMessage(1, "背包已增加到 " + chr.getInventory(type).getSlotLimit());
                    } else {
                        c.sendPacket(MTSCSPacket.sendCSFail(0xA4));
                    }
                } else {
                    final MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
                    int 价格 = gui.Start.ConfigValuesMap.get("商城扩充价格");
                    if (chr.getCSPoints(useNX) >= 价格 && chr.getInventory(type).getSlotLimit() < 93) {//93
                        chr.modifyCSPoints(useNX, -价格, false);
                        chr.getInventory(type).addSlot((byte) 4);
                        chr.getStorage().saveToDB();
                        chr.dropMessage(1, ""
                                + "消耗 " + 价格 + " 点券\r\n"
                                + "背包已增加到 " + chr.getInventory(type).getSlotLimit());
                    } else {
                        c.sendPacket(MTSCSPacket.sendCSFail(0xA4));
                    }
                }
                break;
            }
            case 7:
                // 扩充仓库
                if (chr.getCSPoints(1) >= 600 && chr.getStorage().getSlots() < 45) {
                    chr.modifyCSPoints(1, -600, false);
                    chr.getStorage().increaseSlots((byte) 4);
                    chr.getStorage().saveToDB();
                    //     c.sendPacket(MTSCSPacket.increasedStorageSlots(chr.getStorage().getSlots()));
                } else {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xA4));
                }
                RefreshCashShop(c);
                break;
            case 8: {
                //...9 = pendant slot expansion
                int useNX = slea.readByte() + 1;
                CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
                int slots = c.getCharacterSlots();
                if (slots > 3) {//角色列表
                    chr.dropMessage(1, "角色列表已满无法增加！");
                }
                if (item == null || c.getPlayer().getCSPoints(useNX) < item.getPrice() || slots > 3) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                }
                c.getPlayer().modifyCSPoints(useNX, -item.getPrice(), false);
                if (c.gainCharacterSlot()) {
                    c.sendPacket(MTSCSPacket.increasedStorageSlots(slots + 1));
                    chr.dropMessage(1, "角色列表已增加到：" + c.getCharacterSlots() + "个");
                } else {
                    c.sendPacket(MTSCSPacket.sendCSFail(0));
                }
                break;
            }
            case 0x0D: {
                //get item from csinventory 商城=>包裹
                //uniqueid, 00 01 01 00, type->position(short)
                int uniqueid = slea.readInt(); //csid.. not like we need it anyways
                slea.readInt();//0
                slea.readByte();//物品类型
                byte type = slea.readByte();
                byte unknown = slea.readByte();
                IItem item = c.getPlayer().getCashInventory().findByCashId(uniqueid);
                if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                    IItem item_ = item.copy();
                    byte slot = (byte) MapleInventoryManipulator.addbyItem(c, item_, true);
                    if (slot >= 0) {
                        if (item_.getPet() != null) {
                            item_.getPet().setInventoryPosition(type);
                            c.getPlayer().addPet(item_.getPet());
                        }
                        c.getPlayer().getCashInventory().removeFromInventory(item);
                        c.sendPacket(MTSCSPacket.confirmFromCSInventory(item_, type));
                    } else {
                        c.sendPacket(MaplePacketCreator.serverNotice(1, "您的包裹已满."));
                    }
                } else {
                    c.sendPacket(MaplePacketCreator.serverNotice(1, "放入背包错误A."));
                }
                break;
            }
            case 0x0E: {
                //put item in cash inventory 包裹=>商城
                int uniqueid = (int) slea.readLong();
                MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
                IItem item = c.getPlayer().getInventory(type).findByUniqueId(uniqueid);
                if (item != null && item.getQuantity() > 0 && item.getUniqueId() > 0 && c.getPlayer().getCashInventory().getItemsSize() < 100) {
                    IItem item_ = item.copy();
                    c.getPlayer().getInventory(type).removeItem(item.getPosition(), item.getQuantity(), false);
                    int sn = CashItemFactory.getInstance().getItemSN(item_.getItemId());
                    if (item_.getPet() != null) {
                        c.getPlayer().removePet(item_.getPet());
                    }
                    item_.setPosition((byte) 0);
                    item_.setGMLog("购物商场购买: " + FileoutputUtil.CurrentReadable_Time());
                    c.getPlayer().getCashInventory().addToInventory(item_);
                    c.sendPacket(MTSCSPacket.confirmToCSInventory(item, c.getAccID(), sn));
                } else {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
                }
                RefreshCashShop(c);
                break;
            }
            case 36:
            case 29: { //36 = friendship, 30 = crush //购买挚友戒指 结婚戒指

                int sn = slea.readInt();
                if (sn == 209000310) {
                    sn = 20900026;
                }
                final CashItemInfo item = CashItemFactory.getInstance().getItem(sn);
                final String partnerName = slea.readMapleAsciiString();
                final String msg = slea.readMapleAsciiString();
                IItem itemz = chr.getCashInventory().toItem(item);

                if (item == null || !GameConstants.isEffectRing(item.getId()) || c.getPlayer().getCSPoints(1) < item.getPrice() || msg.length() > 73 || msg.length() < 1) {

                    chr.dropMessage(1, "购买戒指错误：\r\n你没有足够的点卷或者该物品不存在。。");
                    // c.sendPacket(MTSCSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;

                } else if (!item.genderEquals(c.getPlayer().getGender())) {
                    chr.dropMessage(1, "购买戒指错误：B\r\n请联系GM！。");
                    //c.sendPacket(MTSCSPacket.sendCSFail(0xA6));
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                    chr.dropMessage(1, "购买戒指错误：C\r\n请联系GM！。");
                    //c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
                    doCSPackets(c);
                    return;
                } else if (item.getPrice() == 2990) {
//                c.getPlayer().dropMessage(1, "此物品暂时不开放.");
//                doCSPackets(c);
//                return;
                }

                Pair<Integer, Pair<Integer, Integer>> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
                if (info == null || info.getLeft().intValue() <= 0 || info.getLeft().intValue() == c.getPlayer().getId()) {
                    chr.dropMessage(1, "购买戒指错误：D\r\n请联系GM！。");
                    //c.sendPacket(MTSCSPacket.sendCSFail(0xB4)); //9E v75
                    doCSPackets(c);
                    return;
                } else if (info.getRight().getLeft().intValue() == c.getAccID()) {
                    chr.dropMessage(1, "购买戒指错误：E\r\n请联系GM！。");
                    //c.sendPacket(MTSCSPacket.sendCSFail(0xA3)); //9D v75
                    doCSPackets(c);
                    return;
                } else {
                    if (info.getRight().getLeft().intValue() == c.getAccID()) {//info.getRight().getRight().intValue() == c.getPlayer().getGender() && action == 29
                        chr.dropMessage(1, "购买戒指错误：F\r\n请联系GM！。");
                        //c.sendPacket(MTSCSPacket.sendCSFail(0xA1)); //9B v75
                        doCSPackets(c);
                        return;
                    }

                    int err = MapleRing.createRing(item.getId(), c.getPlayer(), partnerName, msg, info.getLeft().intValue(), item.getSN());

                    if (err != 1) {
                        chr.dropMessage(1, "购买戒指错误：G\r\n请联系GM！。");
                        //c.sendPacket(MTSCSPacket.sendCSFail(0)); //9E v75
                        doCSPackets(c);
                        return;
                    }
                    c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
                    doCSPackets(c);
                    return;
                }

            }
            case 0x1F: {
                //购买礼包
                int type = slea.readByte() + 1;
                int snID = slea.readInt();
                final CashItemInfo item = CashItemFactory.getInstance().getItem(snID);
                if (c.getPlayer().isAdmin()) {
                    System.out.println("礼包购买 ID: " + snID);
                }
                switch (snID) {
                    case 10001818:
                        c.getPlayer().dropMessage(1, "这个物品是禁止购买的.");
                        doCSPackets(c);
                        break;
                }
                List<CashItemInfo> ccc = null;
                if (item != null) {
                    ccc = CashItemFactory.getInstance().getPackageItems(item.getId());
                }
                if (item == null || ccc == null || c.getPlayer().getCSPoints(type) < item.getPrice()) {
                    chr.dropMessage(1, "购买礼包错误：\r\n你没有足够的点卷或者该物品不存在。");
                    //c.sendPacket(MTSCSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                } else if (!item.genderEquals(c.getPlayer().getGender())) {
                    chr.dropMessage(1, "购买礼包错误：B\r\n请联系GM！。");
                    //c.sendPacket(MTSCSPacket.sendCSFail(0xA6));
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getCashInventory().getItemsSize() >= (100 - ccc.size())) {
                    chr.dropMessage(1, "购买礼包错误：C\r\n请联系GM！。");
                    //c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
                    doCSPackets(c);
                    return;
                }
                Map<Integer, IItem> ccz = new HashMap<Integer, IItem>();
                for (CashItemInfo i : ccc) {
                    for (int iz : GameConstants.cashBlock) {
                        if (i.getId() == iz) {
                            continue;
                        }
                    }
                    IItem itemz = chr.getCashInventory().toItem(i, chr, MapleInventoryManipulator.getUniqueId(i.getId(), null), "");
                    if (itemz == null || itemz.getUniqueId() <= 0 || itemz.getItemId() != i.getId()) {
                        continue;
                    }
                    ccz.put(i.getSN(), itemz);
                    c.getPlayer().getCashInventory().addToInventory(itemz);
                    c.sendPacket(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
                }
                chr.modifyCSPoints(type, -item.getPrice(), false);
                break;
            }
            case 0x2A: {
                int snCS = slea.readInt();
                if ((snCS == 50200031) && (c.getPlayer().getCSPoints(1) >= 500)) {
                    c.getPlayer().modifyCSPoints(1, -500);
                    c.getPlayer().modifyCSPoints(2, 500);
                    c.sendPacket(MaplePacketCreator.serverNotice(1, "兑换500抵用卷成功"));
                } else if ((snCS == 50200032) && (c.getPlayer().getCSPoints(1) >= 1000)) {
                    c.getPlayer().modifyCSPoints(1, -1000);
                    c.getPlayer().modifyCSPoints(2, 1000);
                    c.sendPacket(MaplePacketCreator.serverNotice(1, "兑换抵1000用卷成功"));
                } else if ((snCS == 50200033) && (c.getPlayer().getCSPoints(1) >= 5000)) {
                    c.getPlayer().modifyCSPoints(1, -5000);
                    c.getPlayer().modifyCSPoints(2, 5000);
                    c.sendPacket(MaplePacketCreator.serverNotice(1, "兑换5000抵用卷成功"));
                } else {
                    c.sendPacket(MaplePacketCreator.serverNotice(1, "没有找到这个道具的信息！\r\n或者你点卷不足无法兑换！"));
                }
                c.sendPacket(MTSCSPacket.enableCSorMTS());
                c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
                c.sendPacket(MaplePacketCreator.enableActions());
                break;
            }
            case 33: {
                int 关闭 = 2;
                if (关闭 == 1) {
                    chr.dropMessage(1, "暂不支持。");
                    c.getPlayer().saveToDB(true, true);
                    c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer())); //显示点卷
                    c.sendPacket(MaplePacketCreator.enableActions()); //能行动
                    return;
                }
                final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
                if (item == null || !MapleItemInformationProvider.getInstance().isQuestItem(item.getId())) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getMeso() < item.getPrice()) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xB8));
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getInventory(GameConstants.getInventoryType(item.getId())).getNextFreeSlot() < 0) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
                    doCSPackets(c);
                    return;
                }
                for (int iz : GameConstants.cashBlock) {
                    if (item.getId() == iz) {
                        c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                        doCSPackets(c);
                        return;
                    }
                }
                byte pos = MapleInventoryManipulator.addId(c, item.getId(), (short) item.getCount(), null, (byte) 0);
                if (pos < 0) {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
                    doCSPackets(c);
                    return;
                }
                chr.gainMeso(-item.getPrice(), false);
                c.sendPacket(MTSCSPacket.showBoughtCSQuestItem(item.getPrice(), (short) item.getCount(), pos, item.getId()));
                break;
            }
            default:
                c.sendPacket(MTSCSPacket.sendCSFail(0));
                break;
        }
        doCSPackets(c);
        //输出日志.show();
    }
//   public static void BuyCashItem(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) throws SQLException {
//        商城检测文件 itemjc = new 商城检测文件();
//        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
//
//        final int action = slea.readByte();
//        if (MapleParty.调试3 > 0) {
//            System.out.println("BuyCashItem:" + action + "");
//        }
//        //购买商城物品
//        if (action == 3) {
//            int useNX = slea.readByte() + 1;
//            int snCS = slea.readInt();
//            CashItemInfo item = CashItemFactory.getInstance().getItem(snCS);
//            if (item == null) {
//                chr.dropMessage(1, "该物品暂未开放！请留意最新公告");
//                doCSPackets(c);
//                return;
//            }
//            int 商城检测开关 = server.Start.ConfigValuesMap.get("商城检测开关");
//            if (商城检测开关 <= 0) {
//                String items1 = itemjc.getid();
//                if (!items1.contains("," + item.getId() + ",")) {
//                    c.getPlayer().dropMessage(1, "该购买物品不存在，请与管理员联系？");
//                    sendMsg("<" + MapleParty.开服名字 + ">机器人: 警告，警告，玩家 " + chr.getName() + " 购买未经过允许的商城物品。代码是: " + item.getId(), "71447500");
//                    if (!"71447500".equals(ServerProperties.getProperty("ZEV.QQ1"))) {
//                        sendMsg("<" + MapleParty.开服名字 + ">机器人: 警告，警告，玩家 " + chr.getName() + " 购买未经过允许的商城物品。代码是: " + item.getId(), "" + ServerProperties.getProperty("ZEV.QQ1") + "");
//                    }
//                    if (!ServerProperties.getProperty("ZEV.QQ1").equals(ServerProperties.getProperty("ZEV.QQ2"))) {
//                        sendMsg("<" + MapleParty.开服名字 + ">机器人: 警告，警告，玩家 " + chr.getName() + " 购买未经过允许的商城物品。代码是: " + item.getId(), "" + ServerProperties.getProperty("ZEV.QQ2") + "");
//                    }
//                    doCSPackets(c);
//                    return;
//                }
//            }
//            if (item != null && chr.getCSPoints(useNX) >= item.getPrice()) {
//                if (!ii.isCash(item.getId())) {
//                    if (c.getPlayer().getInventory(GameConstants.getInventoryType(item.getId())).getNextFreeSlot() < 0) {
//                        chr.dropMessage(1, "背包没空位了！");
//                        doCSPackets(c);
//                        return;
//                    }
//                    byte pos = MapleInventoryManipulator.addId(c, item.getId(), (short) item.getCount(), null, item.getPeriod(), (byte) 0);
//                    if (pos < 0) {
//                        chr.dropMessage(1, "背包坐标出错！\r\n可能是背包没空位了！");
//                        doCSPackets(c);
//                        return;
//                    }
//                    chr.modifyCSPoints(useNX, -item.getPrice(), false);
//                    chr.dropMessage(1, "购买成功！\r\n物品自动放入了背包！");
//                } else {
//                    //如果大于或者等于0就是限量物品
//                    int 库存 = Getcharacter7("" + item.getSN() + "", 2);
//                    int 物品数量 = item.getCount();
//                    //如果该物品有库存信息
//                    if (库存 >= 0) {
//                        //如果是限量物品，判断是否够数量购买
//                        if (库存 >= 物品数量) {
//                            //减去限量物品库存数量
//                            Gaincharacter7("" + item.getSN() + "", 2, -物品数量);
//                            chr.dropMessage(1, "购买成功，剩余库存:" + 库存);
//                        } else {
//                            chr.dropMessage(1, "购买失败，剩余库存:" + 库存);
//                            doCSPackets(c);
//                            return;
//                        }
//                    }
//                    int 折扣 = Getcharacter7("" + item.getSN() + "", 3);
//                    //int 回扣 = item.getPrice() - (item.getPrice() * 折扣 / 100);
//                    int 回扣 = item.getPrice() * 折扣 / 100;
//                    if (折扣 > 0) {
//                        chr.modifyCSPoints(1, 回扣, false);
//                        chr.dropMessage(1, "该物品反馈折扣(" + 折扣 + "%)\r\n"
//                                + "获得回扣点券:" + 回扣);
//                        doCSPackets(c);
//                        return;
//                    }
//                    int 每日限购 = Getcharacter7("" + item.getSN() + "", 4);
//                    if (每日限购 >= 0) {
//                        if (chr.getBossLog("" + item.getSN() + "") >= 每日限购) {
//                            chr.dropMessage(1, "你今天不能再购买此类商品。");
//                            doCSPackets(c);
//                            return;
//                        } else {
//                            chr.setBossLog("" + item.getSN() + "");
//                        }
//                    }
//                    chr.modifyCSPoints(useNX, -item.getPrice(), false);
//                    if (MapleParty.商城出售通知 == 0) {
//                        sendMsg("<" + MapleParty.开服名字 + ">机器人: 商城出售物品通知，玩家 " + chr.getName() + " 购买物品 " + MapleItemInformationProvider.getInstance().getName(item.getId()) + "(" + item.getId() + ") 消耗点卷 " + item.getPrice(), "71447500");
//
//                        if (!"71447500".equals(ServerProperties.getProperty("ZEV.QQ1"))) {
//                            sendMsg("<" + MapleParty.开服名字 + ">机器人: 商城出售物品通知，玩家 " + chr.getName() + " 购买物品 " + MapleItemInformationProvider.getInstance().getName(item.getId()) + "(" + item.getId() + ") 消耗点卷 " + item.getPrice(), "" + ServerProperties.getProperty("ZEV.QQ1") + "");
//                        }
//                        if (!"71447500".equals(ServerProperties.getProperty("ZEV.QQ1"))) {
//                            sendMsg("<" + MapleParty.开服名字 + ">机器人: 商城出售物品通知，玩家 " + chr.getName() + " 购买物品 " + MapleItemInformationProvider.getInstance().getName(item.getId()) + "(" + item.getId() + ") 消耗点卷 " + item.getPrice(), "" + ServerProperties.getProperty("ZEV.QQ2") + "");
//                        }
//                        BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "游戏消耗点券", (byte) 2, item.getPrice());
//                        if (c.getPlayer().getBossLog("日常商城购买") <= 0) {
//                            c.getPlayer().setBossLog("日常商城购买");//为自己加记录
//                        }
//                    }
//                    //记录商城出售的物品
//                    Gaincharacter7("" + item.getSN() + "", 1, 物品数量);
//                    IItem itemz = chr.getCashInventory().toItem(item);
//                    if (itemz != null && itemz.getUniqueId() > 0 && itemz.getItemId() == item.getId() && itemz.getQuantity() == item.getCount()) {
//                        if (useNX == 1) {
//                            byte flag = itemz.getFlag();
//                            boolean 交易 = true;
//                            if (交易 == true) {
//                                if (itemz.getType() == MapleInventoryType.EQUIP.getType()) {
//                                    flag |= ItemFlag.KARMA_EQ.getValue();
//                                } else {
//                                    flag |= ItemFlag.KARMA_USE.getValue();
//                                }
//                                itemz.setFlag(flag);
//                            }
//                        }
//                        chr.getCashInventory().addToInventory(itemz);
//                        c.sendPacket(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
//                    } else {
//                        c.sendPacket(MTSCSPacket.sendCSFail(0));
//                    }
//                }
//            } else {
//                c.sendPacket(MTSCSPacket.sendCSFail(0));
//            }
//            c.getPlayer().saveToDB(true, true);
//            c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer())); //显示点卷
//            c.sendPacket(MaplePacketCreator.enableActions()); //能行动
//        } else if (action == 26) {
//            chr.dropMessage(1, "换购未开启。");
//            doCSPackets(c);
//            return;
//            /*int useNX = slea.readByte() + 1;
//            int snCS = slea.readInt();
//            CashItemInfo item = CashItemFactory.getInstance().getItem(snCS);
//            int 换购物品 = item.getId();
//
//            chr.dropMessage(1, "你要换购" + 换购物品 + "?");*/
//
//        } else if (action == 4 || action == 32) {
//            c.getPlayer().getClient().sendPacket(MaplePacketCreator.serverNotice(1, "禁止商城赠送礼物！"));
//
//        } else if (action == 5) { // Wishlist
//            chr.clearWishlist();
//            if (slea.available() < 40) {
//                c.sendPacket(MTSCSPacket.sendCSFail(0));
//                doCSPackets(c);
//                return;
//            }
//            int[] wishlist = new int[10];
//            for (int i = 0; i < 10; i++) {
//                wishlist[i] = slea.readInt();
//            }
//            chr.setWishlist(wishlist);
//            c.sendPacket(MTSCSPacket.sendWishList(chr, true));
//
//        } else if (action == 6) { // Increase inv
//            int useNX = slea.readByte() + 1;
//            final boolean coupon = slea.readByte() > 0;
//            if (coupon) {
//                final MapleInventoryType type = getInventoryType(slea.readInt());
//
//                if (chr.getCSPoints(useNX) >= 4800 && chr.getInventory(type).getSlotLimit() < 89) {//89
//                    chr.modifyCSPoints(useNX, -4800, false);
//                    chr.getInventory(type).addSlot((byte) 8);
//                    chr.dropMessage(1, "背包已增加到 " + chr.getInventory(type).getSlotLimit());
//                } else {
//                    c.sendPacket(MTSCSPacket.sendCSFail(0xA4));
//                }
//            } else {
//                final MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
//                int 价格 = server.Start.ConfigValuesMap.get("商城扩充价格");
//                if (chr.getCSPoints(useNX) >= 价格 && chr.getInventory(type).getSlotLimit() < 93) {//93
//                    chr.modifyCSPoints(useNX, -价格, false);
//                    chr.getInventory(type).addSlot((byte) 4);
//                    chr.getStorage().saveToDB();
//                    chr.dropMessage(1, ""
//                            + "消耗 " + 价格 + " 点券\r\n"
//                            + "背包已增加到 " + chr.getInventory(type).getSlotLimit());
//                } else {
//                    c.sendPacket(MTSCSPacket.sendCSFail(0xA4));
//                }
//            }
//        } else if (action == 7) { // 扩充仓库
//            if (chr.getCSPoints(1) >= 600 && chr.getStorage().getSlots() < 45) {
//                chr.modifyCSPoints(1, -600, false);
//                chr.getStorage().increaseSlots((byte) 4);
//                chr.getStorage().saveToDB();
//                //     c.sendPacket(MTSCSPacket.increasedStorageSlots(chr.getStorage().getSlots()));
//            } else {
//                c.sendPacket(MTSCSPacket.sendCSFail(0xA4));
//            }
//            RefreshCashShop(c);
//        } else if (action == 8) { //...9 = pendant slot expansion
//            int useNX = slea.readByte() + 1;
//            CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
//            int slots = c.getCharacterSlots();
//            if (slots > 3) {//角色列表
//                chr.dropMessage(1, "角色列表已满无法增加！");
//            }
//            if (item == null || c.getPlayer().getCSPoints(useNX) < item.getPrice() || slots > 3) {
//                c.sendPacket(MTSCSPacket.sendCSFail(0));
//                doCSPackets(c);
//                return;
//            }
//            c.getPlayer().modifyCSPoints(useNX, -item.getPrice(), false);
//            if (c.gainCharacterSlot()) {
//                c.sendPacket(MTSCSPacket.increasedStorageSlots(slots + 1));
//                chr.dropMessage(1, "角色列表已增加到：" + c.getCharacterSlots() + "个");
//            } else {
//                c.sendPacket(MTSCSPacket.sendCSFail(0));
//            }
//        } else if (action == 0x0D) { //get item from csinventory 商城=>包裹
//            //uniqueid, 00 01 01 00, type->position(short)
//            int uniqueid = slea.readInt(); //csid.. not like we need it anyways
//            slea.readInt();//0
//            slea.readByte();//物品类型
//            byte type = slea.readByte();
//            byte unknown = slea.readByte();
//            IItem item = c.getPlayer().getCashInventory().findByCashId(uniqueid);
//            if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
//                IItem item_ = item.copy();
//                byte slot = (byte) MapleInventoryManipulator.addbyItem(c, item_, true);
//                if (slot >= 0) {
//                    if (item_.getPet() != null) {
//                        item_.getPet().setInventoryPosition(type);
//                        c.getPlayer().addPet(item_.getPet());
//                    }
//                    c.getPlayer().getCashInventory().removeFromInventory(item);
//                    c.sendPacket(MTSCSPacket.confirmFromCSInventory(item_, type));
//                } else {
//                    c.sendPacket(MaplePacketCreator.serverNotice(1, "您的包裹已满."));
//                }
//            } else {
//                c.sendPacket(MaplePacketCreator.serverNotice(1, "放入背包错误A."));
//            }
//        } else if (action == 0x0E) { //put item in cash inventory 包裹=>商城
//            int uniqueid = (int) slea.readLong();
//            MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
//            IItem item = c.getPlayer().getInventory(type).findByUniqueId(uniqueid);
//            if (item != null && item.getQuantity() > 0 && item.getUniqueId() > 0 && c.getPlayer().getCashInventory().getItemsSize() < 100) {
//                IItem item_ = item.copy();
//                c.getPlayer().getInventory(type).removeItem(item.getPosition(), item.getQuantity(), false);
//                int sn = CashItemFactory.getInstance().getItemSN(item_.getItemId());
//                if (item_.getPet() != null) {
//                    c.getPlayer().removePet(item_.getPet());
//                }
//                item_.setPosition((byte) 0);
//                item_.setGMLog("购物商场购买: " + FileoutputUtil.CurrentReadable_Time());
//                c.getPlayer().getCashInventory().addToInventory(item_);
//                c.sendPacket(MTSCSPacket.confirmToCSInventory(item, c.getAccID(), sn));
//            } else {
//                c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
//            }
//            RefreshCashShop(c);
//
//        } else if (action == 36 || action == 29) { //36 = friendship, 30 = crush //购买挚友戒指 结婚戒指
//
//            int sn = slea.readInt();
//            if (sn == 209000310) {
//                sn = 20900026;
//            }
//            final CashItemInfo item = CashItemFactory.getInstance().getItem(sn);
//            final String partnerName = slea.readMapleAsciiString();
//            final String msg = slea.readMapleAsciiString();
//            IItem itemz = chr.getCashInventory().toItem(item);
//
//            if (item == null || !GameConstants.isEffectRing(item.getId()) || c.getPlayer().getCSPoints(1) < item.getPrice() || msg.length() > 73 || msg.length() < 1) {
//
//                chr.dropMessage(1, "购买戒指错误：\r\n你没有足够的点卷或者该物品不存在。。");
//                // c.sendPacket(MTSCSPacket.sendCSFail(0));
//                doCSPackets(c);
//                return;
//
//            } else if (!item.genderEquals(c.getPlayer().getGender())) {
//                chr.dropMessage(1, "购买戒指错误：B\r\n请联系GM！。");
//                //c.sendPacket(MTSCSPacket.sendCSFail(0xA6));
//                doCSPackets(c);
//                return;
//            } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
//                chr.dropMessage(1, "购买戒指错误：C\r\n请联系GM！。");
//                //c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
//                doCSPackets(c);
//                return;
//            } else if (item.getPrice() == 2990) {
////                c.getPlayer().dropMessage(1, "此物品暂时不开放.");
////                doCSPackets(c);
////                return;
//            }
//
//            Pair<Integer, Pair<Integer, Integer>> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
//            if (info == null || info.getLeft().intValue() <= 0 || info.getLeft().intValue() == c.getPlayer().getId()) {
//                chr.dropMessage(1, "购买戒指错误：D\r\n请联系GM！。");
//                //c.sendPacket(MTSCSPacket.sendCSFail(0xB4)); //9E v75
//                doCSPackets(c);
//                return;
//            } else if (info.getRight().getLeft().intValue() == c.getAccID()) {
//                chr.dropMessage(1, "购买戒指错误：E\r\n请联系GM！。");
//                //c.sendPacket(MTSCSPacket.sendCSFail(0xA3)); //9D v75
//                doCSPackets(c);
//                return;
//            } else {
//                if (info.getRight().getLeft().intValue() == c.getAccID()) {//info.getRight().getRight().intValue() == c.getPlayer().getGender() && action == 29
//                    chr.dropMessage(1, "购买戒指错误：F\r\n请联系GM！。");
//                    //c.sendPacket(MTSCSPacket.sendCSFail(0xA1)); //9B v75
//                    doCSPackets(c);
//                    return;
//                }
//
//                int err = MapleRing.createRing(item.getId(), c.getPlayer(), partnerName, msg, info.getLeft().intValue(), item.getSN());
//
//                if (err != 1) {
//                    chr.dropMessage(1, "购买戒指错误：G\r\n请联系GM！。");
//                    //c.sendPacket(MTSCSPacket.sendCSFail(0)); //9E v75
//                    doCSPackets(c);
//                    return;
//                }
//                c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
//                doCSPackets(c);
//                return;
//            }
//
//        } else if (action == 0x1F) {//购买礼包
//            int type = slea.readByte() + 1;
//            int snID = slea.readInt();
//            final CashItemInfo item = CashItemFactory.getInstance().getItem(snID);
//            if (c.getPlayer().isAdmin()) {
//                System.out.println("礼包购买 ID: " + snID);
//            }
//            switch (snID) {
//                case 10001818:
//                    c.getPlayer().dropMessage(1, "这个物品是禁止购买的.");
//                    doCSPackets(c);
//                    break;
//            }
//            List<CashItemInfo> ccc = null;
//            if (item != null) {
//                ccc = CashItemFactory.getInstance().getPackageItems(item.getId());
//            }
//            if (item == null || ccc == null || c.getPlayer().getCSPoints(type) < item.getPrice()) {
//                chr.dropMessage(1, "购买礼包错误：\r\n你没有足够的点卷或者该物品不存在。");
//                //c.sendPacket(MTSCSPacket.sendCSFail(0));
//                doCSPackets(c);
//                return;
//            } else if (!item.genderEquals(c.getPlayer().getGender())) {
//                chr.dropMessage(1, "购买礼包错误：B\r\n请联系GM！。");
//                //c.sendPacket(MTSCSPacket.sendCSFail(0xA6));
//                doCSPackets(c);
//                return;
//            } else if (c.getPlayer().getCashInventory().getItemsSize() >= (100 - ccc.size())) {
//                chr.dropMessage(1, "购买礼包错误：C\r\n请联系GM！。");
//                //c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
//                doCSPackets(c);
//                return;
//            }
//
//            Map<Integer, IItem> ccz = new HashMap<Integer, IItem>();
//            for (CashItemInfo i : ccc) {
//                for (int iz : GameConstants.cashBlock) {
//                    if (i.getId() == iz) {
//                        continue;
//                    }
//                }
//                IItem itemz = chr.getCashInventory().toItem(i, chr, MapleInventoryManipulator.getUniqueId(i.getId(), null), "");
//                if (itemz == null || itemz.getUniqueId() <= 0 || itemz.getItemId() != i.getId()) {
//                    continue;
//                }
//                ccz.put(i.getSN(), itemz);
//                c.getPlayer().getCashInventory().addToInventory(itemz);
//                c.sendPacket(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
//            }
//            chr.modifyCSPoints(type, -item.getPrice(), false);
//
//        } else if (action == 0x2A) {
//            int snCS = slea.readInt();
//            if ((snCS == 50200031) && (c.getPlayer().getCSPoints(1) >= 500)) {
//                c.getPlayer().modifyCSPoints(1, -500);
//                c.getPlayer().modifyCSPoints(2, 500);
//                c.sendPacket(MaplePacketCreator.serverNotice(1, "兑换500抵用卷成功"));
//            } else if ((snCS == 50200032) && (c.getPlayer().getCSPoints(1) >= 1000)) {
//                c.getPlayer().modifyCSPoints(1, -1000);
//                c.getPlayer().modifyCSPoints(2, 1000);
//                c.sendPacket(MaplePacketCreator.serverNotice(1, "兑换抵1000用卷成功"));
//            } else if ((snCS == 50200033) && (c.getPlayer().getCSPoints(1) >= 5000)) {
//                c.getPlayer().modifyCSPoints(1, -5000);
//                c.getPlayer().modifyCSPoints(2, 5000);
//                c.sendPacket(MaplePacketCreator.serverNotice(1, "兑换5000抵用卷成功"));
//            } else {
//                c.sendPacket(MaplePacketCreator.serverNotice(1, "没有找到这个道具的信息！\r\n或者你点卷不足无法兑换！"));
//            }
//            c.sendPacket(MTSCSPacket.enableCSorMTS());
//            c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
//            c.sendPacket(MaplePacketCreator.enableActions());
//        } else if (action == 33) {
//            int 关闭 = 1;
//            if (关闭 == 1) {
//                chr.dropMessage(1, "暂不支持。");
//                c.getPlayer().saveToDB(true, true);
//                c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer())); //显示点卷
//                c.sendPacket(MaplePacketCreator.enableActions()); //能行动
//                return;
//            }
//            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
//            if (item == null || !MapleItemInformationProvider.getInstance().isQuestItem(item.getId())) {
//                c.sendPacket(MTSCSPacket.sendCSFail(0));
//                doCSPackets(c);
//                return;
//            } else if (c.getPlayer().getMeso() < item.getPrice()) {
//                c.sendPacket(MTSCSPacket.sendCSFail(0xB8));
//                doCSPackets(c);
//                return;
//            } else if (c.getPlayer().getInventory(GameConstants.getInventoryType(item.getId())).getNextFreeSlot() < 0) {
//                c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
//                doCSPackets(c);
//                return;
//            }
//            for (int iz : GameConstants.cashBlock) {
//                if (item.getId() == iz) {
//                    c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
//                    doCSPackets(c);
//                    return;
//                }
//            }
//            byte pos = MapleInventoryManipulator.addId(c, item.getId(), (short) item.getCount(), null, (byte) 0);
//            if (pos < 0) {
//                c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
//                doCSPackets(c);
//                return;
//            }
//            chr.gainMeso(-item.getPrice(), false);
//            c.sendPacket(MTSCSPacket.showBoughtCSQuestItem(item.getPrice(), (short) item.getCount(), pos, item.getId()));
//        } else {
//            c.sendPacket(MTSCSPacket.sendCSFail(0));
//        }
//        doCSPackets(c);
//    }

    private static final MapleInventoryType getInventoryType(final int id) {
        switch (id) {
            case 50200075:
                return MapleInventoryType.EQUIP;
            case 50200074:
                return MapleInventoryType.USE;
            case 50200073:
                return MapleInventoryType.ETC;
            default:
                return MapleInventoryType.UNDEFINED;
        }
    }

    private static final void RefreshCashShop(MapleClient c) {
        c.sendPacket(MTSCSPacket.showCashInventory(c));
        c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        c.sendPacket(MTSCSPacket.enableCSUse());
        c.getPlayer().getCashInventory().checkExpire(c);
    }

    private static final void doCSPackets(MapleClient c) {
        c.sendPacket(MTSCSPacket.getCSInventory(c));
        c.sendPacket(MTSCSPacket.enableCSorMTS());
        c.sendPacket(MTSCSPacket.sendWishList(c.getPlayer(), false));
        c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        // c.sendPacket(MTSCSPacket.enableCSUse());
        c.sendPacket(MaplePacketCreator.enableActions());
        c.getPlayer().getCashInventory().checkExpire(c);
    }

    public static void Gaincharacter7(String Name, int Channale, int Piot) {
        try {
            int ret = Getcharacter7(Name, Channale);
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO character7 (channel, Name,Point) VALUES (?, ?, ?)");
                    ps.setInt(1, Channale);
                    ps.setString(2, Name);
                    ps.setInt(3, ret);
                    ps.execute();
                } catch (SQLException e) {
                    System.out.println("xxxxxxxx:" + e);
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("xxxxxxxxzzzzzzz:" + e);
                    }
                }
            }
            ret += Piot;
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE character7 SET `Point` = ? WHERE Name = ? and channel = ?");
            ps.setInt(1, ret);
            ps.setString(2, Name);
            ps.setInt(3, Channale);
            ps.execute();
            ps.close();
        } catch (SQLException sql) {
            System.err.println("Getcharacter7!!55" + sql);
        }
    }

    public static int Getcharacter7(String Name, int Channale) {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM character7 WHERE channel = ? and Name = ?");
            ps.setInt(1, Channale);
            ps.setString(2, Name);
            ResultSet rs = ps.executeQuery();
            rs.next();
            ret = rs.getInt("Point");
            rs.close();
            ps.close();
        } catch (SQLException ex) {
        }
        return ret;
    }

    public static int Get商城物品() {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_modified_items WHERE serial = ?");
            int serial = 0;
            ps.setInt(1, serial);
            ResultSet rs = ps.executeQuery();
            rs.next();
            ret = rs.getInt("meso");
            rs.close();
            ps.close();
        } catch (SQLException ex) {
        }
        return ret;
    }

    public static void Gain商城物品(int Piot, int Piot1) {
        try {
            int ret = Get商城物品();
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO cashshop_modified_items (serial,meso) VALUES (?, ?)");
                    int serial = 0;
                    ps.setInt(1, serial);
                    ps.setInt(2, ret);
                    ps.execute();
                } catch (SQLException e) {
                    System.out.println("xxxxxxxx:" + e);
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("xxxxxxxxzzzzzzz:" + e);
                    }
                }
            }
            ret += Piot;
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE cashshop_modified_items SET `meso` = ? WHERE serial = ?");
            ps.setInt(1, ret);
            int serial = 0;
            ps.setInt(2, serial);
            ps.execute();
            ps.close();
        } catch (SQLException sql) {
            System.err.println("獲取錯誤!!55" + sql);
        }
    }
}
