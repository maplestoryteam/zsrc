/*
频道
 */
package handling.channel;

import static abc.Game.事件;
import static abc.Game.私服滚动公告;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.IItem;
import client.inventory.Item;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import database.DatabaseConnection;
import gui.ZevmsLauncherServer;
import handling.MapleServerHandler;
import handling.cashshop.CashShopServer;
import handling.channel.handler.HiredMerchantHandler;
import handling.login.LoginServer;
import handling.netty.ServerConnection;
import handling.world.CheaterData;
import handling.world.MapleParty;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import scripting.EventScriptManager;
import server.MapleSquad;
import server.MapleSquad.MapleSquadType;
import server.maps.MapleMapFactory;
import server.shops.HiredMerchant;
import tools.MaplePacketCreator;
import server.life.PlayerNPC;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import server.ServerProperties;
import server.events.MapleCoconut;
import server.events.MapleEvent;
import server.events.MapleEventType;
import server.events.MapleFitness;
import server.events.MapleOla;
import server.events.MapleOxQuiz;
import server.events.MapleSnowball;
import server.maps.MapleMap;
import server.shops.MaplePlayerShopItem;
import tools.CollectionUtil;
import tools.ConcurrentEnumMap;
import tools.Pair;
import tools.packet.PlayerShopPacket;

public class ChannelServer implements Serializable {

    public static long serverStartTime;
    private int expRate, mesoRate, dropRate, cashRate, BossdropRate = 1;
    private int doubleExp = 1;
    private int doubleMeso = 1;
    private int doubleDrop = 1;
    private short port = 7574;
    private static final short DEFAULT_PORT = 7574;
    private int channel, running_MerchantID = 0, flags = 0;
    private String serverMessage, key, ip, serverName;
    private boolean shutdown = false, finishedShutdown = false, MegaphoneMuteState = false, adminOnly = false;
    private PlayerStorage players;
    private MapleServerHandler serverHandler;
    private ServerConnection acceptor;
    private final MapleMapFactory mapFactory;
    private EventScriptManager eventSM;
    private static final Map<Integer, ChannelServer> instances = new HashMap<>();
    private final Map<MapleSquadType, MapleSquad> mapleSquads = new ConcurrentEnumMap<>(MapleSquadType.class);
    private final Map<Integer, HiredMerchant> merchants = new HashMap<>();
    private final Map<Integer, PlayerNPC> playerNPCs = new HashMap<>();
    private final ReentrantReadWriteLock merchLock = new ReentrantReadWriteLock(); //merchant
    private final ReentrantReadWriteLock squadLock = new ReentrantReadWriteLock(); //squad
    private int eventmap = -1;
    private final Map<MapleEventType, MapleEvent> events = new EnumMap<>(MapleEventType.class);
    private boolean debugMode = false;
    private int instanceId = 0;

//    public Map<Long, IoSession> getSessions() {
//        return acceptor.getManagedSessions();
//    }

    public boolean isConnected(String name) {
        return getPlayerStorage().getCharacterByName(name) != null;
    }

    private ChannelServer(final int channel) {
        this.channel = channel;
        this.mapFactory = new MapleMapFactory(channel);
    }

    public static Set<Integer> getAllInstance() {
        return new HashSet<>(instances.keySet());
    }

    public final void loadEvents() {
        if (events.size() != 0) {
            return;
        }
        events.put(MapleEventType.打椰子比赛, new MapleCoconut(channel, MapleEventType.打椰子比赛.mapids));
        events.put(MapleEventType.打瓶盖比赛, new MapleCoconut(channel, MapleEventType.打瓶盖比赛.mapids));
        events.put(MapleEventType.向高地比赛, new MapleFitness(channel, MapleEventType.向高地比赛.mapids));
        events.put(MapleEventType.上楼上楼, new MapleOla(channel, MapleEventType.上楼上楼.mapids));
        events.put(MapleEventType.OX答题比赛, new MapleOxQuiz(channel, MapleEventType.OX答题比赛.mapids));
        events.put(MapleEventType.推雪球比赛, new MapleSnowball(channel, MapleEventType.推雪球比赛.mapids));
    }

    //加载游戏信息
    public final void run_startup_configurations() {
        setChannel(this.channel);
        try {
            //经验倍率最高100
            if (Integer.parseInt(ServerProperties.getProperty("ZEV.Exp")) > 100) {
                expRate = 100;
            } else {
                expRate = Integer.parseInt(ServerProperties.getProperty("ZEV.Exp"));
            }
            //金币倍率最高100
            if (Integer.parseInt(ServerProperties.getProperty("ZEV.Meso")) > 100) {
                mesoRate = 100;
            } else {
                mesoRate = Integer.parseInt(ServerProperties.getProperty("ZEV.Meso"));
            }
            //金币倍率最高100
            if (Integer.parseInt(ServerProperties.getProperty("ZEV.Drop")) > 100) {
                dropRate = 100;
                //BOSS爆率
                BossdropRate = 100;
            } else {
                dropRate = Integer.parseInt(ServerProperties.getProperty("ZEV.Drop"));
                //BOSS爆率
                BossdropRate = Integer.parseInt(ServerProperties.getProperty("ZEV.Drop"));
            }
            int 双爆频道 = gui.Start.ConfigValuesMap.get("双爆频道开关");
            if (双爆频道 == 0) {
                if (this.channel == Integer.parseInt(ServerProperties.getProperty("ZEV.Count"))) {//双爆频道
                    dropRate *= 2;
                }
            }

            cashRate = 1;
            serverMessage = 私服滚动公告;
            serverName = MapleParty.开服名字;
            flags = 0;//Integer.parseInt(ServerProperties.getProperty("ZEV.WFlags", "0"));
            adminOnly = false;//Boolean.parseBoolean(ServerProperties.getProperty("ZEV.Admin", "false"));
            eventSM = new EventScriptManager(this, ServerProperties.getProperty("Events").split(","));
            port = Short.parseShort(ServerProperties.getProperty("ZEV.Port" + this.channel, String.valueOf(DEFAULT_PORT + this.channel)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ip = MapleParty.IP地址 + ":" + port;
        players = new PlayerStorage(channel);
        loadEvents();
        loadMerchant();
        acceptor = new ServerConnection(port, 0, channel);
        acceptor.run();
        System.out.println("○ 启动频道 " + this.channel + ": \r\n○ 启动端口  " + port + " 服务器IP:" + ip + "");
        eventSM.init();
    }

    public final void shutdown(Object threadToNotify) {
        if (finishedShutdown) {
            return;
        }
        broadcastPacket(MaplePacketCreator.serverNotice(0, "这个频道正在关闭中."));
        // dc all clients by hand so we get sessionClosed...
        shutdown = true;

        System.out.println("频道 " + channel + ", 保存雇佣商人…");

        System.out.println("频道 " + channel + ", 保存玩家信息…");

        // getPlayerStorage().disconnectAll();
        //System.out.println("频道 " + channel + ", Unbinding...");
        //temporary while we dont have !addchannel
        instances.remove(channel);
        LoginServer.removeChannel(channel);
        setFinishShutdown();
//        if (threadToNotify != null) {
//            synchronized (threadToNotify) {
//                threadToNotify.notify();
//            }
//        }
    }

//    public final void closeConn(String ip) {
//        for (IoSession ss : acceptor.getManagedSessions().values()) {
//            if (ss.getRemoteAddress() == null) {
//                continue;
//            }
//            if (ss.getRemoteAddress().toString().split(":")[0].equals(ip)) {
//                ss.close(false);
//                return;
//            }
//        }
//    }
//
//    public final boolean checkPorts() {
//        for (IoSession ss : acceptor.getManagedSessions().values()) {
//            if (ss.getRemoteAddress() == null) {
//                continue;
//            }
//            String[] arr = ss.getRemoteAddress().toString().split(":");
//            String sip = arr[0];
//            String sport = arr[1];
//
//            for (Map.Entry<String, List<String>> entry : ZevmsLauncherServer.allowClientMap.entrySet()) {
//                String ip = entry.getKey();
//                if (ip.equals(sip)) {
//                    List<String> ports = entry.getValue();
//                    return ports.contains(sport);
//                }
//            }
//        }
//
//        return true;
//    }

    public final boolean hasFinishedShutdown() {
        return finishedShutdown;
    }

    public final MapleMapFactory getMapFactory() {
        return mapFactory;
    }

//    public static final ChannelServer newInstance(final String key, final int channel) {
//        return new ChannelServer(key, channel);
//    }
    public static final ChannelServer newInstance(final int channel) {
        return new ChannelServer(channel);
    }

    public static final ChannelServer getInstance(final int channel) {
        return instances.get(channel);
    }

    public final void addPlayer(final MapleCharacter chr) {
        getPlayerStorage().registerPlayer(chr);
        if (gui.Start.ConfigValuesMap.get("滚动公告开关") <= 0) {
            chr.getClient().sendPacket(MaplePacketCreator.serverMessage(serverMessage));
        }
    }

    public final PlayerStorage getPlayerStorage() {
        if (players == null) { //wth
            players = new PlayerStorage(channel); //wthhhh
        }
        return players;
    }

    public final void removePlayer(final MapleCharacter chr) {
        getPlayerStorage().deregisterPlayer(chr);

    }

    public final void removePlayer(final int idz, final String namez) {
        getPlayerStorage().deregisterPlayer(idz, namez);

    }

    public final String getServerMessage() {
        return serverMessage;
    }

    public final void setServerMessage(final String newMessage) {
        serverMessage = newMessage;
        broadcastPacket(MaplePacketCreator.serverMessage(serverMessage));
    }

    public final void broadcastPacket(final byte[] data) {
        getPlayerStorage().broadcastPacket(data);
    }

    public final void broadcastSmegaPacket(final byte[] data) {
        getPlayerStorage().broadcastSmegaPacket(data);
    }

    public final void broadcastGMPacket(final byte[] data) {
        getPlayerStorage().broadcastGMPacket(data);
    }

    public final int getExpRate() {
        return expRate * doubleExp;
    }

    public final void setExpRate(final int expRate) {
        this.expRate = expRate;
    }

    public final int getCashRate() {
        return cashRate;
    }

    public final void setCashRate(final int cashRate) {
        this.cashRate = cashRate;
    }

    public final int getChannel() {
        return channel;
    }

    public final void setChannel(final int channel) {
        instances.put(channel, this);
        LoginServer.addChannel(channel);
    }

    public static final Collection<ChannelServer> getAllInstances() {
        return Collections.unmodifiableCollection(instances.values());
    }

    public final String getSocket() {
        return ip;
    }

    public final String getIP() {
        return ip;
    }

    public String getIPA() {
        return ip;
    }

    public final boolean isShutdown() {
        return shutdown;
    }

    public final int getLoadedMaps() {
        return mapFactory.getLoadedMaps();
    }

    public final EventScriptManager getEventSM() {
        return eventSM;
    }

    public final void reloadEvents() {
        eventSM.cancel();
        eventSM = new EventScriptManager(this, ServerProperties.getProperty("ZEV.Events").split(","));
        eventSM.init();
    }

    public final int getBossDropRate() {
        return BossdropRate;
    }

    public final void setBossDropRate(final int dropRate) {
        this.BossdropRate = dropRate;
    }

    public final int getMesoRate() {
        return mesoRate * doubleMeso;
    }

    public final void setMesoRate(final int mesoRate) {
        this.mesoRate = mesoRate;
    }

    public final int getDropRate() {
        return dropRate * doubleDrop;
    }

    public final void setDropRate(final int dropRate) {
        this.dropRate = dropRate;
    }

    public int getDoubleExp() {
        if ((this.doubleExp < 0) || (this.doubleExp > 2)) {
            return 1;
        }
        return this.doubleExp;
    }

    public void setDoubleExp(int doubleExp) {
        if ((doubleExp < 0) || (doubleExp > 2)) {
            this.doubleExp = 1;
        } else {
            this.doubleExp = doubleExp;
        }
    }

    public int getDoubleMeso() {
        if ((this.doubleMeso < 0) || (this.doubleMeso > 2)) {
            return 1;
        }
        return this.doubleMeso;
    }

    public void setDoubleMeso(int doubleMeso) {
        if ((doubleMeso < 0) || (doubleMeso > 2)) {
            this.doubleMeso = 1;
        } else {
            this.doubleMeso = doubleMeso;
        }
    }

    public int getDoubleDrop() {
        if ((this.doubleDrop < 0) || (this.doubleDrop > 2)) {
            return 1;
        }
        return this.doubleDrop;
    }

    public void setDoubleDrop(int doubleDrop) {
        if ((doubleDrop < 0) || (doubleDrop > 2)) {
            this.doubleDrop = 1;
        } else {
            this.doubleDrop = doubleDrop;
        }
    }

    /*
     * public static final void startChannel_Main() { serverStartTime =
     * System.currentTimeMillis();
     *
     * for (int i = 0; i <
     * Integer.parseInt(ServerProperties.getProperty("tms.Count", "0")); i++) {
     * //newInstance(ServerConstants.Channel_Key[i], i +
     * 1).run_startup_configurations(); newInstance(i +
     * 1).run_startup_configurations(); } }
     */
    public static void startChannel_Main() {
        serverStartTime = System.currentTimeMillis();

        int ch = Integer.parseInt(ServerProperties.getProperty("ZEV.Count", "0"));

        if (ch > 10) {
            ch = 10;
        }

        for (int i = 0; i < ch; i++) {
            newInstance(i + 1).run_startup_configurations();
        }
    }

    public static final void startChannel(final int channel) {
        serverStartTime = System.currentTimeMillis();
        for (int i = 0; i < Integer.parseInt(ServerProperties.getProperty("ZEV.Count", "0")); i++) {
            if (channel == i + 1) {

                //newInstance(ServerConstants.Channel_Key[i], i + 1).run_startup_configurations();
                newInstance(i + 1).run_startup_configurations();
                break;
            }
        }
    }

    public Map<MapleSquadType, MapleSquad> getAllSquads() {
        return Collections.unmodifiableMap(mapleSquads);
    }

    public final MapleSquad getMapleSquad(final String type) {
        return getMapleSquad(MapleSquadType.valueOf(type.toLowerCase()));
    }

    public final MapleSquad getMapleSquad(final MapleSquadType type) {
        return mapleSquads.get(type);
    }

    public final boolean addMapleSquad(final MapleSquad squad, final String type) {
        final MapleSquadType types = MapleSquadType.valueOf(type.toLowerCase());
        if (types != null && !mapleSquads.containsKey(types)) {
            mapleSquads.put(types, squad);
            squad.scheduleRemoval();
            return true;
        }
        return false;
    }

    public boolean removeMapleSquad(MapleSquad squad, MapleSquadType type) {
        if (type != null && mapleSquads.containsKey(type)) {
            if (mapleSquads.get(type) == squad) {
                mapleSquads.remove(type);
                return true;
            }
        }
        return false;
    }

    public final boolean removeMapleSquad(final MapleSquadType types) {
        if (types != null && mapleSquads.containsKey(types)) {
            mapleSquads.remove(types);
            return true;
        }
        return false;
    }

    public int closeAllMerchant() {
        int ret = 0;
        merchLock.writeLock().lock();
        try {
            Iterator merchants_ = this.merchants.entrySet().iterator();
            while (merchants_.hasNext()) {
                HiredMerchant hm = (HiredMerchant) ((Map.Entry) merchants_.next()).getValue();
                hm.closeShop(true, false);
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM hiredmerchantshop WHERE charid = ?");
                    ps.setInt(1, hm.getOwnerId());
                    ps.executeUpdate();
                    ps = con.prepareStatement("INSERT INTO hiredmerchantshop VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, DEFAULT,?)");
                    ps.setInt(1, hm.getPackageid());
                    ps.setInt(2, hm.getChannel());
                    ps.setInt(3, hm.getMap().getId());
                    ps.setInt(4, hm.getOwnerId());//c.getPlayer().getId());
                    ps.setInt(5, hm.getItemId());
                    ps.setString(6, hm.getDescription());
                    ps.setInt(7, hm.getPosition().x);
                    ps.setInt(8, hm.getPosition().y);
                    ps.setInt(9, 0); //默认别给它设置开店，因为这个状态时还未添加出售物品，而且地图上并未出现雇佣NPC
                    ps.setInt(10, hm.getOwnerAccId());
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException se) {

                }

                hm.getMap().removeMapObject(hm);
                merchants_.remove();
                ret++;
            }
        } finally {
            merchLock.writeLock().unlock();
        }
        return ret;
    }

    public final int addMerchant(final HiredMerchant hMerchant) {
        merchLock.writeLock().lock();

        int runningmer = 0;
        try {
            runningmer = running_MerchantID;
            merchants.put(running_MerchantID, hMerchant);
            running_MerchantID++;
        } finally {
            merchLock.writeLock().unlock();
        }
        return runningmer;
    }

    public final void removeMerchant(final HiredMerchant hMerchant) {
        merchLock.writeLock().lock();

        try {
            merchants.remove(hMerchant.getStoreId());
        } finally {
            merchLock.writeLock().unlock();
        }
    }

    public final boolean containsMerchant(final int accid) {
        boolean contains = false;

        merchLock.readLock().lock();
        try {
            final Iterator itr = merchants.values().iterator();

            while (itr.hasNext()) {
                if (((HiredMerchant) itr.next()).getOwnerAccId() == accid) {
                    contains = true;
                    break;
                }
            }
        } finally {
            merchLock.readLock().unlock();
        }
        return contains;
    }

    public final List<HiredMerchant> searchMerchant(final int itemSearch) {
        final List<HiredMerchant> list = new LinkedList<HiredMerchant>();
        merchLock.readLock().lock();
        try {
            final Iterator itr = merchants.values().iterator();

            while (itr.hasNext()) {
                HiredMerchant hm = (HiredMerchant) itr.next();
                if (hm.searchItem(itemSearch).size() > 0) {
                    list.add(hm);
                }
            }
        } finally {
            merchLock.readLock().unlock();
        }
        return list;
    }

    public final void toggleMegaphoneMuteState() {
        this.MegaphoneMuteState = !this.MegaphoneMuteState;
    }

    public final boolean getMegaphoneMuteState() {
        return MegaphoneMuteState;
    }

    public int getEvent() {
        return eventmap;
    }

    public final void setEvent(final int ze) {
        this.eventmap = ze;
    }

    public MapleEvent getEvent(final MapleEventType t) {
        return events.get(t);
    }

    public final Collection<PlayerNPC> getAllPlayerNPC() {
        return playerNPCs.values();
    }

    public final PlayerNPC getPlayerNPC(final int id) {
        return playerNPCs.get(id);
    }

    public final void addPlayerNPC(final PlayerNPC npc) {
        if (playerNPCs.containsKey(npc.getId())) {
            removePlayerNPC(npc);
        }
        playerNPCs.put(npc.getId(), npc);
        getMapFactory().getMap(npc.getMapId()).addMapObject(npc);
    }

    public final void removePlayerNPC(final PlayerNPC npc) {
        if (playerNPCs.containsKey(npc.getId())) {
            playerNPCs.remove(npc.getId());
            getMapFactory().getMap(npc.getMapId()).removeMapObject(npc);
        }
    }

    public final String getServerName() {
        return serverName;
    }

    public final void setServerName(final String sn) {
        this.serverName = sn;
    }

    public final int getPort() {
        return port;
    }

    public static final Set<Integer> getChannelServer() {
        return new HashSet<Integer>(instances.keySet());
    }

    public final void setShutdown() {
        this.shutdown = true;
        System.out.println("频道 " + channel + " 已开始关闭.");
    }

    public final void setFinishShutdown() {
        this.finishedShutdown = true;
        System.out.println("频道 " + channel + " 已关闭完成.");
    }

    public final boolean isAdminOnly() {
        return adminOnly;
    }

    public final static int getChannelCount() {
        return instances.size();
    }

    public final MapleServerHandler getServerHandler() {
        return serverHandler;
    }

    public final int getTempFlag() {
        return flags;
    }

    public static Map<Integer, Integer> getChannelLoad() {
        Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
        for (ChannelServer cs : instances.values()) {
            ret.put(cs.getChannel(), cs.getConnectedClients());
        }
        return ret;
    }

    public int getConnectedClients() {
        return getPlayerStorage().getConnectedClients();
    }

    public List<CheaterData> getCheaters() {
        List<CheaterData> cheaters = getPlayerStorage().getCheaters();

        Collections.sort(cheaters);
        return CollectionUtil.copyFirst(cheaters, 20);
    }

    public void broadcastMessage(byte[] message) {
        broadcastPacket(message);
    }

    public void broadcastSmega(byte[] message) {
        broadcastSmegaPacket(message);
    }

    public void broadcastGMMessage(byte[] message) {
        broadcastGMPacket(message);
    }

    public void saveAll() {
        int ppl = 0;
        for (MapleCharacter chr : this.players.getAllCharactersThreadSafe()) {
            if (chr != null) {
                ppl++;
                chr.saveToDB(false, false);
            }
        }
        System.out.println("[自动存档] 已经将频道 " + this.channel + " 的 " + ppl + " 个玩家保存到数据中.");
    }

    public void AutoTime(int dy) {
        try {
            for (ChannelServer chan : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : chan.getPlayerStorage().getAllCharacters()) {
                    if (chr == null) {
                        continue;
                    }
                    chr.gainGamePoints(1);
                    if (chr.getGamePoints() < 5) {
                        chr.resetGamePointsPD();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void addInstanceId() {
        instanceId++;
    }

    public void shutdown() {

        if (this.finishedShutdown) {
            return;
        }
        broadcastPacket(MaplePacketCreator.serverNotice(0, "游戏即将关闭维护..."));

        this.shutdown = true;
        System.out.println("频道 " + this.channel + " 正在清理活动脚本...");

        this.eventSM.cancel();

        System.out.println("频道 " + this.channel + " 正在保存所有角色数据...");

        // getPlayerStorage().disconnectAll();
        System.out.println("频道 " + this.channel + " 解除绑定端口...");
        acceptor.close();
        acceptor = null;
        instances.remove(this.channel);
        setFinishShutdown();
    }

    public static boolean forceRemovePlayerByCharName(String Name) {
        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            Collection<MapleCharacter> chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter c : chrs) {
                if (c.getName().equalsIgnoreCase(Name)) {
                    try {
                        if (c.getMap() != null) {
                            c.getMap().removePlayer(c);
                        }
                        if (c.getClient() != null) {
                            c.getClient().disconnect(true, false, false);
                            c.getClient().getSession().close();
                        }

                    } catch (Exception ex) {
                    }
                    chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
                    if (chrs.contains(c)) {
                        ch.removePlayer(c);
                        return true;
                    }

                }
            }
        }
        return false;
    }

    public static void forceRemovePlayerByAccId(MapleClient c, int accid) {
        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            Collection<MapleCharacter> chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter chr : chrs) {
                if (chr.getAccountID() == accid) {
                    try {
                        if (chr.getClient() != null) {
                            if (chr.getClient() != c) {
                                chr.getClient().disconnect(true, false, false);
                            }
                        }
                    } catch (Exception ex) {
                    }
                    chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
                    if (chr.getClient() != c) {
                        if (chrs.contains(chr)) {
                            ch.removePlayer(chr);
                        }
                        if (chr.getMap() != null) {
                            chr.getMap().removePlayer(chr);
                        }
                    }
                }
            }
        }
        try {
            Collection<MapleCharacter> chrs = CashShopServer.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter chr : chrs) {
                if (chr.getAccountID() == accid) {
                    try {
                        if (chr.getClient() != null) {
                            if (chr.getClient() != c) {
                                chr.getClient().disconnect(true, false, false);
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    public static void forceRemovePlayerByAccId(int accid) {
        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            Collection<MapleCharacter> chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
            for (MapleCharacter c : chrs) {
                if (c.getAccountID() == accid) {
                    try {
                        if (c.getClient() != null) {
                            c.getClient().disconnect(true, false, false);
                        }
                    } catch (Exception ex) {
                    }
                    chrs = ch.getPlayerStorage().getAllCharactersThreadSafe();
                    if (chrs.contains(c)) {
                        ch.removePlayer(c);
                    }
                    if (c.getMap() != null) {
                        c.getMap().removePlayer(c);
                    }
                }
            }
        }

    }

    public final void loadMerchant() {

        try {

            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM hiredmerchantshop WHERE channelid = ?");
            ps.setInt(1, channel);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int channelid = rs.getInt("channelid");
                    int mapid = rs.getInt("mapid");
                    int charid = rs.getInt("charid");
                    int itemid = rs.getInt("itemid");
                    String desc = rs.getString("desc");
                    int x = rs.getInt("x");
                    int y = rs.getInt("y");
                    int id = rs.getInt("id");
                    int accid = rs.getInt("accid");
                    Timestamp createdate = rs.getTimestamp("createdate");

                    //只有是当初玩家创建的频道才自动建立，否则等其他频道调用时才加载。
                    if (channel == channelid) { //通过地图ID兑换成地图对象
                        MapleMap map = this.getMapFactory().getMap(mapid); //通过角色ID读取角色名称
                        String charname = "";
                        try (PreparedStatement psW = con.prepareStatement("SELECT * FROM characters WHERE id = ?")) {
                            psW.setInt(1, charid);

                            try (ResultSet rsW = psW.executeQuery()) {
                                if (rsW.next()) {
                                    charname = rsW.getString("name");
                                }
                            }
                        }

                        HiredMerchant shop = new HiredMerchant(charname, charid, map, x, y, itemid, desc, channel, accid);
                        List<IItem> itemsW = new ArrayList<>();
                        Map<Integer, Pair<IItem, MapleInventoryType>> items = ItemLoader.HIRED_MERCHANTWWWWWWWWWWWWWW.loadHiredItems(false, id);
                        if (items != null) {
                            List<IItem> iters = new ArrayList<>();
                            for (Pair<IItem, MapleInventoryType> z : items.values()) {//992916233
                                iters.add(z.left);
                            }
                            for (IItem entry : iters) {
                                shop.addItem(new MaplePlayerShopItem(entry, (short) 1, entry.getPrice(), (byte) 0));
                            }
                        }
                        //HiredMerchantHandler.deletePackage(shop.getOwnerAccId(), id, shop.getOwnerId());
                        //删除雇佣信息
                        map.addMapObject(shop);
                        if (shop.getShopType() == 1) {
                            HiredMerchant merchant = (HiredMerchant) shop;
                            merchant.setAvailable(true);
                            merchant.setOpen(true);
                            addMerchant(merchant);
                            map.broadcastMessage(PlayerShopPacket.spawnHiredMerchant(merchant));
                        }

                    }
                }

            }

//            try (Connection con = DBConPool.getInstance().getDataSource().getConnection()) {
//               PreparedStatement ps = con.prepareStatement("DELETE FROM hiredmerchantshop");
//               ps.executeUpdate();
//               ps.close();
//            }
        } catch (SQLException se) {
            int aaa = 0;

        }

    }

    /*  public void AutoNx(int mini) {
        for (MapleMap map : mapFactory.getAllMaps()) {
            if (map.getId() == 910000000) {
                map.AutoNx(mini);//只有自由有
            }
        }
    }*/
//    public void AutoNx(int dy) {
//        for (MapleMap map : mapFactory.getAllMaps()) {
//            if (map.getId() <= 910000000 && map.getId() >= 910000022 && getChannel() == 1) {//&& getChannel() == 1
//                map.AutoNx(1, dy);//只有自由有
//            }
//        }
//    }
}
