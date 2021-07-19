/*
过图主文件
 */
package server.maps;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.inventory.*;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import database.DatabaseConnection;
import gui.Start;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import pvp.MaplePvp;
import scripting.EventManager;
import server.*;
import server.MapleCarnivalFactory.MCSkill;
import server.Timer;
import server.MapleSquad.MapleSquadType;
import server.Timer.MapTimer;
import server.custom.bossrank.BossRankInfo;
import server.custom.bossrank.BossRankManager;
import server.events.MapleEvent;
import server.life.*;
import server.maps.MapleNodes.MapleNodeInfo;
import server.maps.MapleNodes.MaplePlatform;
import server.maps.MapleNodes.MonsterPoint;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.packet.MTSCSPacket;
import tools.packet.MobPacket;
import tools.packet.PetPacket;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static fumo.FumoSkill.FM;
import static gui.QQ通信.群通知;
import static gui.活动野外通缉.随机通缉;
import static gui.进阶BOSS.进阶BOSS线程.关闭进阶BOSS线程;
import static tools.FileoutputUtil.CurrentReadable_Time;

public final class MapleMap {

    private final Map<MapleMapObjectType, LinkedHashMap<Integer, MapleMapObject>> mapobjects;
    private final Map<MapleMapObjectType, ReentrantReadWriteLock> mapobjectlocks;
    private final List<MapleCharacter> characters = new ArrayList<MapleCharacter>();
    private final ReentrantReadWriteLock charactersLock = new ReentrantReadWriteLock();
    private int runningOid = 100000;
    private final Lock runningOidLock = new ReentrantLock();
    private final List<Spawns> monsterSpawn = new ArrayList<Spawns>();
    private final AtomicInteger spawnedMonstersOnMap = new AtomicInteger(0);
    private final Map<Integer, MaplePortal> portals = new HashMap<Integer, MaplePortal>();
    private MapleFootholdTree footholds = null;
    private final float monsterRate;
    private float recoveryRate;
    private MapleMapEffect mapEffect;
    private final byte channel;
    private short decHP = 0, createMobInterval = 9000;
    private int consumeItemCoolTime = 0;
    private int protectItem = 0;
    private int decHPInterval = 10000;
    private final int mapid;
    private int returnMapId;
    private int timeLimit;
    private int fieldLimit;
    private int maxRegularSpawn = 0;
    private int fixedMob;
    private int forcedReturnMap = 999999999;
    private int lvForceMove = 0;
    private int lvLimit = 0;
    private int permanentWeather = 0;
    private boolean town, clock, personalShop, everlast = false, dropsDisabled = false, gDropsDisabled = false,
            soaring = false, squadTimer = false, isSpawns = true;
    private String mapName, streetName, onUserEnter, onFirstUserEnter, bgm, speedRunLeader = "";
    private final List<Integer> dced = new ArrayList<Integer>();
    private ScheduledFuture<?> squadSchedule;
    private long speedRunStart = 0, lastSpawnTime = 0, lastHurtTime = 0;
    private MapleNodes nodes;
    private boolean boat;
    private boolean docked;
    private MapleSquadType squad;
    private int fieldType;
    private final Map<String, Integer> environment = new LinkedHashMap<String, Integer>();

    public MapleMap(final int mapid, final int channel, final int returnMapId, final float monsterRate) {
        //地图代码
        this.mapid = mapid;
        //频道
        this.channel = (byte) channel;
        //返回映射
        this.returnMapId = returnMapId;
        if (this.returnMapId == 910000000) {
            this.returnMapId = mapid;
        }
        this.monsterRate = monsterRate;
        EnumMap<MapleMapObjectType, LinkedHashMap<Integer, MapleMapObject>> objsMap = new EnumMap<MapleMapObjectType, LinkedHashMap<Integer, MapleMapObject>>(MapleMapObjectType.class);
        EnumMap<MapleMapObjectType, ReentrantReadWriteLock> objlockmap = new EnumMap<MapleMapObjectType, ReentrantReadWriteLock>(MapleMapObjectType.class);
        for (MapleMapObjectType type : MapleMapObjectType.values()) {
            objsMap.put(type, new LinkedHashMap<Integer, MapleMapObject>());
            objlockmap.put(type, new ReentrantReadWriteLock());
        }
        mapobjects = Collections.unmodifiableMap(objsMap);
        mapobjectlocks = Collections.unmodifiableMap(objlockmap);
        //地图回收();
        //送货员(10);
        //AutoNx();
    }

    public void 地图回收() {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (MapleParty.神秘商人时间 == 1) {
                    if (getAllMonstersThreadsafe().size() > 0 && getCharactersSize() == 0) {
                        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                            System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在回收地图 √ " + getId());
                            cserv.getMapFactory().destroyMap(getId(), true);
                            cserv.getMapFactory().HealMap(getId());
                        }
                    }
                    System.err.println("[服务端]" + CurrentReadable_Time() + " : 系统正在回收地图 √");
                }
            }
        }, 1000 * 30);
    }

    public MapleCharacter getCharacterByName(String id) {
        charactersLock.readLock().lock();
        try {
            for (MapleCharacter mc : characters) {
                if (mc.getName().equalsIgnoreCase(id)) {
                    return mc;
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
        return null;
    }

    public void 妖僧地图回收() {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (getId() == 702060000) {
                    if (getAllMonstersThreadsafe().size() > 0 && getCharactersSize() == 0) {
                        清怪();
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, ";妖僧副本已被重置。"));
                    }
                }
            }
        }, 10 * 6000);
    }

    public void 定时召唤蜗牛王(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (getMonsterById(2220000) == null) {
                    try {
                        MapleMonster mob1 = MapleLifeFactory.getMonster(2220000);
                        spawnMonsterOnGroundBelow(mob1, new Point(439, 185));
                    } catch (Exception e) {
                    }
                }
            }
        }, 1000 * 60 * time);
    }

    public void 定时召唤普通扎昆(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (getAllMonstersThreadsafe().isEmpty()) {
                    try {
                        if (getChannel() != 1) {//8800100
                            spawnZakum(-10, -215);
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, ";扎昆在祭台出现了。"));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, 1000 * 60 * time);
    }

    public void 定时召唤暗黑龙王(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (getAllMonstersThreadsafe().isEmpty()) {
                    //if (getMonsterById(8810026) == null) {
                    try {
                        if (getChannel() != 1) {
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8810026);
                            spawnMonsterOnGroundBelow(mob1, new Point(71, 260));
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, ";暗黑龙王出现了。"));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, 1000 * 60 * time);
    }

    public void 定时召唤时间宠儿(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (getId() == 270050100) {
                    if (getAllMonstersThreadsafe().isEmpty()) {
                        if (getMonsterById(8820008) == null) {
                            try {
                                if (getChannel() != 1) {
                                    MapleMonster mob1 = MapleLifeFactory.getMonster(8820008);
                                    spawnMonsterOnGroundBelow(mob1, new Point(2, -42));
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }, 1000 * 60 * time);
    }

    public void 定时召唤蝙蝠怪(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (getId() == 105100300) {
                    //if (getAllMonstersThreadsafe().isEmpty()) {
                    if (getMonsterById(8830000) == null && getMonsterById(8830001) == null && getMonsterById(8830002) == null) {
                        try {
                            if (getChannel() != 1) {
                                MapleMonster mob1 = MapleLifeFactory.getMonster(8830000);
                                MapleMonster mob2 = MapleLifeFactory.getMonster(8830001);
                                MapleMonster mob3 = MapleLifeFactory.getMonster(8830002);
                                spawnMonsterOnGroundBelow(mob1, new Point(483, 258));
                                spawnMonsterOnGroundBelow(mob2, new Point(483, 258));
                                spawnMonsterOnGroundBelow(mob3, new Point(483, 258));
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }, 1000 * 60 * time);
    }

    /* public void 定时召唤蜘蛛女王(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                //if (getAllMonstersThreadsafe().size() == 0) {
                if (getMonsterById(8800400) == null) {
                    try {
                        if (getChannel() != 1) {
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8800400);
                            spawnMonsterOnGroundBelow(mob1, new Point(61, 363));

                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, 1000 * 60 * time);
    }*/
    public void 定时召唤混沌女王(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                //if (getAllMonstersThreadsafe().size() == 0) {
                if (getMonsterById(8920102) == null) {
                    try {
                        if (getChannel() != 1) {
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8920000);
                            spawnMonsterOnGroundBelow(mob1, new Point(-2118, 86));

                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, 1000 * 60 * time);
    }

    public void 定时召唤月妙巨兔(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                //if (getAllMonstersThreadsafe().size() == 0) {
                try {
                    if (getChannel() != 1) {
                        MapleMonster mob1 = MapleLifeFactory.getMonster(9500006);
                        spawnMonsterOnGroundBelow(mob1, new Point(475, 35));
                    }
                } catch (Exception e) {
                }
                // }
            }
        }, 1000 * 60 * time);
    }

    public void 定时召唤雷昂(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (getAllMonstersThreadsafe().size() == 0) {
                    try {
                        if (getChannel() != 1) {
                            MapleMonster mob1 = MapleLifeFactory.getMonster(8840000);
                            spawnMonsterOnGroundBelow(mob1, new Point(-570, 102));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, 1000 * 60 * time);
    }

    public void 定时召唤石像塔(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                //if (getAllMonstersThreadsafe().size() == 0) {
                if (getMonsterById(2500360) == null) {
                    try {
                        if (getChannel() != 1) {
                            MapleMonster mob1 = MapleLifeFactory.getMonster(2500360);
                            spawnMonsterOnGroundBelow(mob1, new Point(1927, 2205));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, 1000 * 60 * time);
    }

    public void 定时召唤守护塔(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                //if (getMonsterById(2500402) == null) {
                if (getMonsterById(2500402) == null) {
                    try {
                        if (getChannel() != 1) {
                            MapleMonster mob1 = MapleLifeFactory.getMonster(2500400);
                            spawnMonsterOnGroundBelow(mob1, new Point(821, 195));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, 1000 * 60 * time);
    }

    public void 定时召唤希纳斯(final int time) {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                //if (getAllMonstersThreadsafe().size() == 0) {
                if (getMonsterById(9300742) == null) {
                    try {
                        if (getChannel() != 1) {
                            MapleMonster mob1 = MapleLifeFactory.getMonster(9300742);
                            spawnMonsterOnGroundBelow(mob1, new Point(1285, 143));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, 1000 * 60 * Start.ConfigValuesMap.get("希纳斯刷新时间"));
    }

    public void 回收地图() {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (getId() != 103000804 && getId() != 922010900 && getId() != 240060200 && getId() != 280030000) {
                    if (getAllMonstersThreadsafe().size() > 0 && getCharactersSize() == 0) {
                        清怪();
                        MaplePacketCreator.removeMapEffect();
                    }
                }
            }
        }, gui.Start.ConfigValuesMap.get("回收地图时间") * 60 * 1000);
    }

//                for (MapleCharacter chr : characters) {
//                    int 点券 = 0;
//                    int 经验 = 0;
//                    int 金币 = 0;
//                    int 抵用 = 0;
//                    int 泡点金币开关 = gui.Start.ConfigValuesMap.get("泡点金币开关");
//                    if (泡点金币开关 <= 0) {
//                        int 泡点金币 = gui.Start.ConfigValuesMap.get("泡点金币");
//                        金币 += 泡点金币;
//                        if (chr.getEquippedFuMoMap().get(34) != null) {
//                            金币 += 泡点金币 / 100 * chr.getEquippedFuMoMap().get(34);
//                        }
//                        chr.gainMeso(泡点金币, true);//增加金币
//                    }
//                    int 泡点点券开关 = gui.Start.ConfigValuesMap.get("泡点点券开关");
//                    if (泡点点券开关 <= 0) {
//                        int 泡点点券 = gui.Start.ConfigValuesMap.get("泡点点券");
//                        chr.modifyCSPoints(1, 泡点点券, true);
//                        点券 += 泡点点券;
//                    }
//                    int 泡点抵用开关 = gui.Start.ConfigValuesMap.get("泡点抵用开关");
//                    if (泡点抵用开关 <= 0) {
//                        int 泡点抵用 = gui.Start.ConfigValuesMap.get("泡点抵用");
//                        chr.modifyCSPoints(2, 泡点抵用, true);
//                        抵用 += 泡点抵用;
//                    }
//                    int 泡点经验开关 = gui.Start.ConfigValuesMap.get("泡点经验开关");
//                    if (泡点经验开关 <= 0) {
//                        int 泡点经验 = gui.Start.ConfigValuesMap.get("泡点经验");
//                        经验 += 泡点经验;
//                        if (chr.getEquippedFuMoMap().get(33) != null) {
//                            经验 += 泡点经验 / 100 * chr.getEquippedFuMoMap().get(33);
//                        }
//                        chr.gainExp(泡点经验, true, true, false);
//                    }
//                    BossRankManager3.getInstance().setLog(chr.getId(), chr.getName(), "泡点经验", (byte) 2, (byte) 1);
//                    chr.getClient().sendPacket(UIPacket.getTopMsg("[" + MapleParty.开服名字 + "泡点]:" + 点券 + " 点券 / " + 抵用 + " 抵用 / " + 经验 + " 经验 / " + 金币 + " 金币，泡点经验 + 1"));
//                }
    public final void setSpawns(final boolean fm) {
        this.isSpawns = fm;
    }

    public final boolean getSpawns() {
        return isSpawns;
    }

    public final void setFixedMob(int fm) {
        this.fixedMob = fm;
    }

    public final void setForceMove(int fm) {
        this.lvForceMove = fm;
    }

    public final int getForceMove() {
        return lvForceMove;
    }

    public final void setLevelLimit(int fm) {
        this.lvLimit = fm;
    }

    public final int getLevelLimit() {
        return lvLimit;
    }

    public final void setReturnMapId(int rmi) {
        this.returnMapId = rmi;
    }

    public final void setSoaring(boolean b) {
        this.soaring = b;
    }

    public final boolean canSoar() {
        return soaring;
    }

    public final void toggleDrops() {
        this.dropsDisabled = !dropsDisabled;
    }

    public final void setDrops(final boolean b) {
        this.dropsDisabled = b;
    }

    public final void toggleGDrops() {
        this.gDropsDisabled = !gDropsDisabled;
    }

    public final int getId() {
        return mapid;
    }

    public final MapleMap getReturnMap() {
        return ChannelServer.getInstance(channel).getMapFactory().getMap(returnMapId);
    }

    public final int getReturnMapId() {
        return returnMapId;
    }

    public final int getForcedReturnId() {
        return forcedReturnMap;
    }

    public final MapleMap getForcedReturnMap() {
        return ChannelServer.getInstance(channel).getMapFactory().getMap(forcedReturnMap);
    }

    public final void setForcedReturnMap(final int map) {
        this.forcedReturnMap = map;
    }

    /**
     * <判断地图回收率>
     */
    public final float getRecoveryRate() {
        return recoveryRate;
    }

    /**
     * <设置地图回收率>
     */
    public final void setRecoveryRate(final float recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public final int getFieldLimit() {
        return fieldLimit;
    }

    public final void setFieldLimit(final int fieldLimit) {
        this.fieldLimit = fieldLimit;
    }

    public final void setCreateMobInterval(final short createMobInterval) {
        this.createMobInterval = createMobInterval;
    }

    public final void setTimeLimit(final int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public final void setMapName(final String mapName) {
        this.mapName = mapName;
    }

    public final String getMapName() {
        return mapName;
    }

    public final String getStreetName() {
        return streetName;
    }

    public final void setFirstUserEnter(final String onFirstUserEnter) {
        this.onFirstUserEnter = onFirstUserEnter;
    }

    public final void setUserEnter(final String onUserEnter) {
        this.onUserEnter = onUserEnter;
    }

    public void setOnUserEnter(String onUserEnter) {
        this.onUserEnter = onUserEnter;
    }

    public final boolean hasClock() {
        return clock;
    }

    public final void setClock(final boolean hasClock) {
        this.clock = hasClock;
    }

    public final boolean isTown() {
        return town;
    }

    public final void setTown(final boolean town) {
        this.town = town;
    }

    public final boolean allowPersonalShop() {
        return personalShop;
    }

    public final void setPersonalShop(final boolean personalShop) {
        this.personalShop = personalShop;
    }

    public final void setStreetName(final String streetName) {
        this.streetName = streetName;
    }

    public final void setEverlast(final boolean everlast) {
        this.everlast = everlast;
    }

    public final boolean getEverlast() {
        return everlast;
    }

    public final int getHPDec() {
        return decHP;
    }

    public final void setHPDec(final int delta) {
        if (delta > 0 || mapid == 749040100) { //pmd
            lastHurtTime = System.currentTimeMillis(); //start it up
        }
        decHP = (short) delta;
    }

    public final int getHPDecInterval() {
        return decHPInterval;
    }

    public final void setHPDecInterval(final int delta) {
        decHPInterval = delta;
    }

    public final int getHPDecProtect() {
        return protectItem;
    }

    public final void setHPDecProtect(final int delta) {
        this.protectItem = delta;
    }

    public final int getCurrentPartyId() {
        charactersLock.readLock().lock();
        try {
            final Iterator<MapleCharacter> ltr = characters.iterator();
            MapleCharacter chr;
            while (ltr.hasNext()) {
                chr = ltr.next();
                if (chr.getPartyId() != -1) {
                    return chr.getPartyId();
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
        return -1;
    }

    public final void addMapObject(final MapleMapObject mapobject) {
        runningOidLock.lock();
        int newOid;
        try {
            newOid = ++runningOid;
        } finally {
            runningOidLock.unlock();
        }

        mapobject.setObjectId(newOid);

        mapobjectlocks.get(mapobject.getType()).writeLock().lock();
        try {
            mapobjects.get(mapobject.getType()).put(newOid, mapobject);
        } finally {
            mapobjectlocks.get(mapobject.getType()).writeLock().unlock();
        }
    }

    /*
    new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 10);
                        
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
     */
    //掉落物品
    private void spawnAndAddRangedMapObject(final MapleMapObject mapobject, final DelayedPacketCreation packetbakery, final SpawnCondition condition) {
        addMapObject(mapobject);
        charactersLock.readLock().lock();
        try {
            final Iterator<MapleCharacter> itr = characters.iterator();
            MapleCharacter chr;
            while (itr.hasNext()) {
                chr = itr.next();
                if (condition == null || condition.canSpawn(chr)) {
                    if (!chr.isClone() && chr.getPosition().distanceSq(mapobject.getPosition()) <= GameConstants.maxViewRangeSq()) {
                        packetbakery.sendPackets(chr.getClient());
                        chr.addVisibleMapObject(mapobject);
                    }
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
    }

    public final void removeMapObject(final MapleMapObject obj) {
        mapobjectlocks.get(obj.getType()).writeLock().lock();
        try {
            mapobjects.get(obj.getType()).remove(Integer.valueOf(obj.getObjectId()));
        } finally {
            mapobjectlocks.get(obj.getType()).writeLock().unlock();
        }
    }

    public final Point calcPointBelow(final Point initial) {
        final MapleFoothold fh = footholds.findBelow(initial);
        if (fh == null) {
            return null;
        }
        int dropY = fh.getY1();
        if (!fh.isWall() && fh.getY1() != fh.getY2()) {
            final double s1 = Math.abs(fh.getY2() - fh.getY1());
            final double s2 = Math.abs(fh.getX2() - fh.getX1());
            if (fh.getY2() < fh.getY1()) {
                dropY = fh.getY1() - (int) (Math.cos(Math.atan(s2 / s1)) * (Math.abs(initial.x - fh.getX1()) / Math.cos(Math.atan(s1 / s2))));
            } else {
                dropY = fh.getY1() + (int) (Math.cos(Math.atan(s2 / s1)) * (Math.abs(initial.x - fh.getX1()) / Math.cos(Math.atan(s1 / s2))));
            }
        }
        return new Point(initial.x, dropY);
    }

    public final Point calcDropPos(final Point initial, final Point fallback) {
        final Point ret = calcPointBelow(new Point(initial.x, initial.y - 50));
        if (ret == null) {
            return fallback;
        }
        return ret;
    }

    /**
     * <怪物爆物>
     */
    private void dropFromMonster(final MapleCharacter chr, final MapleMonster mob) {//所有怪物掉落
        if ((mob.getId() >= 9400714 && mob.getId() <= 9400724) || mob == null || chr == null || ChannelServer.getInstance(channel) == null || dropsDisabled || mob.dropsDisabled() || chr.getPyramidSubway() != null) { //no drops in pyramid ok? no cash either
            return;
        }

        if (chr.判断疲劳值() > 60 * gui.Start.ConfigValuesMap.get("每日疲劳值")) {
            return;
        }
        //判断该地图道具数量
        if (mapobjects.get(MapleMapObjectType.ITEM).size() >= gui.Start.ConfigValuesMap.get("地图物品上限")) {
            removeDrops();
            chr.dropMessage(6, "[系统提示] : 当前地图物品数量已经达到限制，现在已被清除。");
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final byte droptype = (byte) (mob.getStats().isExplosiveReward() ? 3 : mob.getStats().isFfaLoot() ? 2 : chr.getParty() != null ? 1 : 0);
        //怪物爆金币
        int mobpos = mob.getPosition().x, cmServerrate = ChannelServer.getInstance(channel).getMesoRate(), chServerrate = ChannelServer.getInstance(channel).getDropRate(), caServerrate = ChannelServer.getInstance(channel).getCashRate();
        IItem idrop;

        byte d = 1;
        Point pos = new Point(0, mob.getPosition().y);
        double showdown = 100.0;
        final MonsterStatusEffect mse = mob.getBuff(MonsterStatus.SHOWDOWN);
        if (mse != null) {
            showdown += mse.getX();
        }
        if (mob.getStats().isBoss()) {
            chServerrate = ChannelServer.getInstance(channel).getBossDropRate();
        }
        final MapleMonsterInformationProvider mi = MapleMonsterInformationProvider.getInstance();//所有怪物掉落
        final List<MonsterDropEntry> dropEntry = mi.retrieveDrop(mob.getId());
        Collections.shuffle(dropEntry);
        /**
         * <怪物爆物>
         */
        for (final MonsterDropEntry de : dropEntry) {
            if (de.itemId == mob.getStolen()) {
                continue;
            }
            int Rand = Randomizer.nextInt(999999);
            int part1 = de.chance;
            int part2 = chServerrate;
            int part3 = chr.getDropMod();
            int part4 = (int) (chr.getStat().dropBuff / 100.0);
            int part5 = (int) (showdown / 100.0);
            int last = part1 * part2 * part3 * part4 * part5;
            if (Rand < last) {
                if (droptype == 3) {
                    pos.x = (mobpos + (d % 2 == 0 ? (40 * (d + 1) / 2) : -(40 * (d / 2))));
                } else {
                    pos.x = (mobpos + ((d % 2 == 0) ? (25 * (d + 1) / 2) : -(25 * (d / 2))));
                }
                if (de.itemId != 0) {
                    if (GameConstants.getInventoryType(de.itemId) == MapleInventoryType.EQUIP) {
                        idrop = ii.randomizeStats((Equip) ii.getEquipById(de.itemId));
                    } else {
                        final int range = Math.abs(de.Maximum - de.Minimum);
                        idrop = new Item(de.itemId, (byte) 0, (short) (de.Maximum != 1 ? Randomizer.nextInt(range <= 0 ? 1 : range) + de.Minimum : 1), (byte) 0);
                    }
                    spawnMobDrop(idrop, calcDropPos(pos, mob.getPosition()), mob, chr, droptype, de.questid);
                }
                d++;
            }
        }

        double mesoDecrease = Math.pow(0.93, mob.getStats().getExp() / 350.0);
        if (mesoDecrease > 1.0) {
            mesoDecrease = 1.0;
        }
        int tempmeso = Math.min(30000, (int) (mesoDecrease * (mob.getStats().getExp()) * (1.0 + Math.random() * 5) / 10.0));
        if (tempmeso > 0) {
            pos.x = Math.min(Math.max(mobpos - 25 * (d / 2), footholds.getMinDropX() + 25), footholds.getMaxDropX() - d * 25);
            spawnMobMesoDrop((int) (tempmeso * (chr.getStat().mesoBuff / 100.0) * chr.getDropMod() * cmServerrate), calcDropPos(pos, mob.getPosition()), mob, chr, false, droptype);
        }
        /**
         * <世界爆物>
         */
        final List<MonsterGlobalDropEntry> globalEntry = new ArrayList<MonsterGlobalDropEntry>(mi.getGlobalDrop());
        Collections.shuffle(globalEntry);
        for (final MonsterGlobalDropEntry de : globalEntry) {
            if (Randomizer.nextInt(999999) < de.chance && (de.continent < 0 || (de.continent < 10) || (de.continent < 100) || (de.continent < 1000))) {
                if (droptype == 3) {
                    pos.x = (mobpos + (d % 2 == 0 ? (40 * (d + 1) / 2) : -(40 * (d / 2))));
                } else {
                    pos.x = (mobpos + ((d % 2 == 0) ? (25 * (d + 1) / 2) : -(25 * (d / 2))));
                }
                if (de.itemId > 0) {
                    if (!gDropsDisabled) {
                        if (GameConstants.getInventoryType(de.itemId) == MapleInventoryType.EQUIP) {
                            idrop = ii.randomizeStats((Equip) ii.getEquipById(de.itemId));
                        } else {
                            idrop = new Item(de.itemId, (byte) 0, (short) (de.Maximum != 1 ? Randomizer.nextInt(de.Maximum - de.Minimum) + de.Minimum : 1), (byte) 0);
                        }
                        spawnMobDrop(idrop, calcDropPos(pos, mob.getPosition()), mob, chr, de.onlySelf ? 0 : droptype, de.questid);
                        d++;
                    }
                }
            }
        }

    }

    public void removeMonster(final MapleMonster monster) {
        spawnedMonstersOnMap.decrementAndGet();
        broadcastMessage(MobPacket.killMonster(monster.getObjectId(), 0));
        removeMapObject(monster);
    }

    private void killMonster(final MapleMonster monster) { // For mobs with removeAfter
        spawnedMonstersOnMap.decrementAndGet();
        monster.setHp(0);
        monster.spawnRevives(this);
        broadcastMessage(MobPacket.killMonster(monster.getObjectId(), 1));
        removeMapObject(monster);
    }

    public final void killMonster(final MapleMonster monster, final MapleCharacter chr, final boolean withDrops, final boolean second, byte animation) {
        killMonster(monster, chr, withDrops, second, animation, 0);
    }

    public final void 地图杀怪(final MapleMonster monster, final MapleCharacter chr) {
        final int mobid = monster.getId();

        if (mobid == MapleParty.通缉BOSS && mapid == MapleParty.通缉地图) {
            MapleParty.通缉BOSS = 0;
            MapleParty.通缉地图 = 0;
            String 信息 = "[野外通缉] : " + chr.getName() + " 完成了此次通缉令，下一次通缉令将在 1 小时后发布。";
            群通知(信息);
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
            System.err.println("[服务端]" + CurrentReadable_Time() + " : " + 信息);
            chr.击杀野外BOSS特效2();
            chr.打开奖励();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 60 * 60 * 1);
                        随机通缉();
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        } else if (mobid == 9500337 && mapid == 104000400) {
            关闭进阶BOSS线程();
        } else if (mobid == 2220000 && mapid == 104000400) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[红蜗牛王屠杀令]: " + chr.getName() + " 在海岸草丛III击杀了红蜗牛王"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("每日击杀红蜗牛王");
            chr.setBossLog("击杀高级怪物");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀红蜗牛王", (byte) 2, (byte) 1);
        } else if (mobid == 3220000 && mapid == 101030404) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[树妖王屠杀令]: " + chr.getName() + " 在东部岩山Ⅴ击杀了树妖王"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀树妖王");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀树妖王", (byte) 2, (byte) 1);
        } else if (mobid == 5220001 && mapid == 110040000) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[巨居蟹屠杀令]: " + chr.getName() + " 在阳光沙滩击杀了巨居蟹"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀巨居蟹");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀巨居蟹", (byte) 2, (byte) 1);
        } else if (mobid == 7220000 && mapid == 250010304) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[肯德熊屠杀令]: " + chr.getName() + " 在流浪熊的地盘击杀了肯德熊"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀肯德熊");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀肯德熊", (byte) 2, (byte) 1);
        } else if (mobid == 8220000 && mapid == 200010300) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[艾利杰屠杀令]: " + chr.getName() + " 在天空楼梯Ⅱ击杀了艾利杰"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀艾利杰");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀艾利杰", (byte) 2, (byte) 1);
        } else if (mobid == 7220002 && mapid == 250010503) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[妖怪禅师屠杀令]: " + chr.getName() + " 在妖怪森林击杀了妖怪禅师"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀妖怪禅师");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀妖怪禅师", (byte) 2, (byte) 1);

        } else if (mobid == 7220001 && mapid == 222010310) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[九尾狐屠杀令]: " + chr.getName() + " 在月岭击杀了九尾狐"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀九尾狐");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀九尾狐", (byte) 2, (byte) 1);
        } else if (mobid == 6220000 && mapid == 107000300) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[多尔屠杀令]: " + chr.getName() + " 在鳄鱼潭Ⅰ击杀了多尔"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀多尔");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀多尔", (byte) 2, (byte) 1);
        } else if (mobid == 5220002 && mapid == 100040105) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[浮士德屠杀令]: " + chr.getName() + " 在巫婆森林Ⅰ击杀了浮士德"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀浮士德");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀浮士德", (byte) 2, (byte) 1);
        } else if (mobid == 5220003 && mapid == 220050100) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[提莫屠杀令]: " + chr.getName() + " 在时间漩涡击杀了提莫"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀提莫");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀提莫", (byte) 2, (byte) 1);
        } else if (mobid == 6220001 && mapid == 221040301) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[朱诺屠杀令]: " + chr.getName() + " 在哥雷草原击杀了朱诺"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀朱诺");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀朱诺", (byte) 2, (byte) 1);
        } else if (mobid == 8220003 && mapid == 240040401) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[大海兽屠杀令]: " + chr.getName() + " 在大海兽 峡谷击杀了大海兽"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀大海兽");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀大海兽", (byte) 2, (byte) 1);
        } else if (mobid == 3220001 && mapid == 260010201) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[大宇屠杀令]: " + chr.getName() + " 在仙人掌爸爸沙漠击杀了大宇"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀大宇");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀大宇", (byte) 2, (byte) 1);
        } else if (mobid == 8220002 && mapid == 261030000) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[吉米拉屠杀令]: " + chr.getName() + " 在研究所地下秘密通道击杀了吉米拉"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀吉米拉");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀吉米拉", (byte) 2, (byte) 1);
        } else if (mobid == 4220000 && mapid == 230020100) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[歇尔夫屠杀令]: " + chr.getName() + " 在海草之塔击杀了歇尔夫"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀歇尔夫");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀歇尔夫", (byte) 2, (byte) 1);
        } else if (mobid == 6130101 && mapid == 100000005) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[蘑菇王屠杀令]: " + chr.getName() + " 在铁甲猪公园3击杀了蘑菇王"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀蘑菇王");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀蘑菇王", (byte) 2, (byte) 1);
        } else if (mobid == 6300005 && mapid == 105070002) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[僵尸蘑菇王屠杀令]: " + chr.getName() + " 在蘑菇王之墓击杀了僵尸蘑菇王"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀僵尸蘑菇王");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀僵尸蘑菇王", (byte) 2, (byte) 1);
        } else if (mobid == 8130100 && mapid == 105090900) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[蝙蝠怪屠杀令]: " + chr.getName() + " 在被诅咒的寺院击杀了蝙蝠怪"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀蝙蝠怪");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀蝙蝠怪", (byte) 2, (byte) 1);
        } else if (mobid == 9400205 && mapid == 800010100) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[蓝蘑菇王屠杀令]: " + chr.getName() + " 在天皇殿堂击杀了蓝蘑菇王"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀蓝蘑菇王");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀蓝蘑菇王", (byte) 2, (byte) 1);
        } else if (mobid == 9400120 && mapid == 801030000) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[老板屠杀令]: " + chr.getName() + " 在昭和内部街道3击杀了老板"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀老板");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀老板", (byte) 2, (byte) 1);
        } else if (mobid == 8220001 && mapid == 211040101) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[驮狼雪人屠杀令]: " + chr.getName() + " 在雪人谷击杀了驮狼雪人"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀驮狼雪人");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀驮狼雪人", (byte) 2, (byte) 1);
        } else if (mobid == 8180000 && mapid == 240020401) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[火焰龙屠杀令]: " + chr.getName() + " 在喷火龙栖息地击杀了火焰龙"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀火焰龙");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀火焰龙", (byte) 2, (byte) 1);
        } else if (mobid == 8180001 && mapid == 240020101) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[天鹰屠杀令]: " + chr.getName() + " 在格瑞芬多森林击杀了天鹰"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀天鹰");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀天鹰", (byte) 2, (byte) 1);
        } else if (mobid == 8220006 && mapid == 270030500) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[雷卡屠杀令]: " + chr.getName() + " 在忘却之路5击杀了雷卡"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀雷卡");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀雷卡", (byte) 2, (byte) 1);
        } else if (mobid == 8220005 && mapid == 270020500) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[玄冰独角兽屠杀令]: " + chr.getName() + " 在后悔之路5击杀了玄冰独角兽"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀玄冰独角兽");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀玄冰独角兽", (byte) 2, (byte) 1);
        } else if (mobid == 8220004 && mapid == 270010500) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[多多屠杀令]: " + chr.getName() + " 在追忆之路5击杀了多多"));
            }
            chr.击杀野外BOSS特效();
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀多多");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀多多", (byte) 2, (byte) 1);
        } else if (mobid == 8220004) {
            chr.setBossLog("蜈蚣");
            chr.击杀野外BOSS特效();
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "蜈蚣", (byte) 2, (byte) 1);
        } else if (mobid == 8500002 && mapid == 220080001) {
            if (gui.Start.ConfigValuesMap.get("屠令广播开关") <= 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(4, "[帕普拉图斯屠杀令]: " + chr.getName() + " 在时间塔的本源击杀了帕普拉图斯"));
            }
            chr.setBossLog("击杀高级怪物");
            chr.setBossLog("每日击杀帕普拉图斯");
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "每日挑战帕普拉图斯", (byte) 2, (byte) 1);
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "个人击杀帕普拉图斯", (byte) 2, (byte) 1);
        } else if (mobid == 9300003 && mapid == 103000804) {
            BossRankManager.getInstance().setLog(chr.getId(), chr.getName(), "废弃副本BOOS击杀次数", (byte) 2, (byte) 1);
        } else if (mobid == 8830000 && mapid == 105100300) {//蝙蝠怪，清理
            MapleMonster mob;
            MapleMap map = chr.getMap();
            boolean drop = false;
            double range = Double.POSITIVE_INFINITY;
            List<MapleMapObject> monsters = map.getMapObjectsInRange(chr.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : map.getMapObjectsInRange(chr.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                map.killMonster(mob, chr, drop, false, (byte) 1);
            }
            for (MapleMapObject monstermo : map.getMapObjectsInRange(chr.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                map.killMonster(mob, chr, drop, false, (byte) 1);
            }
            /**
             * <扎昆>
             */
        } else if (mobid == 8800002 && mapid == 280030000) {//清理扎昆
            MapleMonster mob;
            MapleMap map = chr.getMap();
            boolean drop = false;
            double range = Double.POSITIVE_INFINITY;
            List<MapleMapObject> monsters = map.getMapObjectsInRange(chr.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : map.getMapObjectsInRange(chr.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                map.killMonster(mob, chr, drop, false, (byte) 1);
            }
            for (MapleMapObject monstermo : map.getMapObjectsInRange(chr.getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                map.killMonster(mob, chr, drop, false, (byte) 1);
            }

        }

    }

    public final void killMonster(final MapleMonster monster, final MapleCharacter chr, final boolean withDrops, final boolean second, byte animation, final int lastSkill) {
        if ((monster.getId() == 8810122 || monster.getId() == 8810018) && !second) {
            MapTimer.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    killMonster(monster, chr, true, true, (byte) 1);
                    killAllMonsters(true);
                }
            }, 3000);
            return;
        }
        if (monster.getId() == 8150000) {
            if (MapleParty.蝙蝠魔A部队 > 0) {
                if (mapid == 106010100 || mapid == 106010000 || mapid == 100000000) {
                    MapleParty.蝙蝠魔A部队 -= 1;
                }
            } else if (MapleParty.蝙蝠魔B部队 > 0) {
                if (mapid == 107000400 || mapid == 107000300 || mapid == 107000200 || mapid == 107000100 || mapid == 107000000 || mapid == 103000000) {
                    MapleParty.蝙蝠魔B部队 -= 1;
                }
            } else if (MapleParty.蝙蝠魔C部队 > 0) {
                if (mapid == 101010103 || mapid == 101010102 || mapid == 101010101 || mapid == 101010100 || mapid == 101010000 || mapid == 101000000) {
                    MapleParty.蝙蝠魔C部队 -= 1;
                }
            } else if (MapleParty.蝙蝠魔D部队 > 0) {
                if (mapid == 106000300 || mapid == 106000200 || mapid == 106000100 || mapid == 106000000 || mapid == 102000000) {
                    MapleParty.蝙蝠魔D部队 -= 1;
                }
            }
        }
        //在杀死XX之前要杀死
        if (monster.getId() == 8820014) { //时间的宠儿－品克缤
            killMonster(8820000);//时间的宠儿－品克缤
        } else if (monster.getId() == 9300166) { //PQ弹出来
            animation = 2; //或者是3？
        } else if (this.getId() == 910320100) {
        }
        spawnedMonstersOnMap.decrementAndGet();
        removeMapObject(monster);
        int dropOwner = monster.killBy(chr, lastSkill);
        broadcastMessage(MobPacket.killMonster(monster.getObjectId(), animation));

        if (monster.getBuffToGive() > -1) {
            final int buffid = monster.getBuffToGive();
            final MapleStatEffect buff = MapleItemInformationProvider.getInstance().getItemEffect(buffid);
            charactersLock.readLock().lock();
            try {
                for (final MapleCharacter mc : characters) {
                    if (mc.isAlive()) {
                        buff.applyTo(mc);

                        switch (monster.getId()) {
                            case 8810018:
                            case 8810122:
                            case 8820001:
                                mc.getClient().sendPacket(MaplePacketCreator.showOwnBuffEffect(buffid, 11)); // HT nine spirit
                                broadcastMessage(mc, MaplePacketCreator.showBuffeffect(mc.getId(), buffid, 11), false); // HT nine spirit
                                break;
                        }
                    }
                }
            } finally {
                charactersLock.readLock().unlock();//reactor
            }
        }
        final int mobid = monster.getId();
        SpeedRunType type = SpeedRunType.NULL;
        final MapleSquad sqd = getSquadByMap();
        地图杀怪(monster, chr);
        if (mapid == 702060000 && monster.getId() == 9600025) {
            String 挑战者 = "";
            for (MapleCharacter player : this.getCharacters()) {
                挑战者 += player.getName() + " ";
                player.setBossLog("妖僧经验限制");
            }
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[挑战]:少林妖僧被 " + 挑战者 + "击败了。"));
            //群通知("[挑战]:少林妖僧被 " + 挑战者 + "击败了。");
        } else if (mapid == 541020800 && monster.getId() == 9420521) {
            String 挑战者 = "";
            for (MapleCharacter player : this.getCharacters()) {
                挑战者 += player.getName() + " ";
                player.setBossLog("树精经验限制");
            }
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[挑战]:克雷塞尔被 " + 挑战者 + "击败了。"));
            //群通知("[挑战]:克雷塞尔被 " + 挑战者 + "击败了。");
        } else if (mobid == 8810018 && mapid == 240060200) {
            String 挑战者 = "";
            for (MapleCharacter player : this.getCharacters()) {
                挑战者 += player.getName() + " ";
                player.setBossLog("黑龙经验限制");
            }
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[挑战]:黑暗龙王斯被 " + 挑战者 + "击败了。"));
            //群通知("[挑战]:黑暗龙王斯被 " + 挑战者 + "击败了。");
            if (speedRunStart > 0) {
                type = SpeedRunType.Horntail;
            }
            if (sqd != null) {
                doShrine(true);
            }
            /**
             * <击杀黑龙后，重置所有重返信息>
             */
            try {
                try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 501")) {
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
            }
            /**
             * < 帕普拉图斯 >
             */
        } else if (mobid == 8500002 && mapid == 220080001) {
            String 挑战者 = "";
            for (MapleCharacter player : this.getCharacters()) {
                挑战者 += player.getName() + " ";
                player.setBossLog("闹钟经验限制");
            }
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[挑战]:帕普拉图斯被 " + 挑战者 + "击败了。"));
            //群通知("[挑战]:帕普拉图斯被 " + 挑战者 + "击败了。");
            if (speedRunStart > 0) {
                type = SpeedRunType.Papulatus;
            }

            /**
             * <愤怒的心疤狮王 >
             */
        } else if ((mobid == 9420549 || mobid == 9420544) && mapid == 551030200) {
            String 挑战者 = "";
            for (MapleCharacter player : this.getCharacters()) {
                挑战者 += player.getName() + " ";
            }
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[挑战]:心疤狮王被 " + 挑战者 + "击败了。"));
            //群通知("[挑战]:心疤狮王被 " + 挑战者 + "击败了。");
            if (speedRunStart > 0) {
                if (mobid == 9420549) {
                    type = SpeedRunType.Scarlion;
                } else {
                    type = SpeedRunType.Targa;
                }
            }
            /**
             * <时间的宠儿－品克缤 >
             */
        } else if (mobid == 8820001 && mapid == 270050100) {
            String 挑战者 = "";
            for (MapleCharacter player : this.getCharacters()) {
                挑战者 += player.getName();

            }
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[挑战]:品克缤被" + 挑战者 + "击败了。"));
            //群通知("[挑战]:品克缤被" + 挑战者 + "击败了。");
            if (speedRunStart > 0) {
                type = SpeedRunType.Pink_Bean;
            }
            if (sqd != null) {
                doShrine(true);
            }
            /**
             * <扎昆手臂,击杀所有手臂后，才能打主体>
             */
        } else if (mobid >= 8800003 && mobid <= 8800010) {
            boolean makeZakReal = true;
            final Collection<MapleMonster> monsters = getAllMonstersThreadsafe();

            for (final MapleMonster mons : monsters) {
                if (mons.getId() >= 8800003 && mons.getId() <= 8800010) {
                    makeZakReal = false;
                    break;
                }
            }
            if (makeZakReal) {
                for (final MapleMapObject object : monsters) {
                    final MapleMonster mons = ((MapleMonster) object);
                    if (mons.getId() == 8800000) {
                        final Point pos = mons.getPosition();
                        this.killAllMonsters(true);
                        spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8800000), pos);
                        break;
                    }
                }
            }
            /**
             * <扎昆主体>
             */
        } else if (mobid == 8800002 && mapid == 280030000) {
            String 挑战者 = "";
            for (MapleCharacter player : this.getCharacters()) {
                挑战者 += player.getName() + " ";
                player.setBossLog("扎昆经验限制");
            }
            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[挑战]:扎昆被 " + 挑战者 + "击败了。"));
            //群通知("[挑战]:扎昆被 " + 挑战者 + "击败了。");
            if (speedRunStart > 0) {
                type = SpeedRunType.Zakum;
            }
            if (sqd != null) {
                doShrine(true);
            }
            /**
             * <击杀扎昆主体后，重置所有重返信息>
             */
            try {
                try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characterz SET Point = 0 WHERE channel = 500")) {
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
            }
        }
        /**
         * <宝宝首领怪召唤用透明怪物>
         */
        if (mobid == 8820008) {
            for (final MapleMapObject mmo : getAllMonstersThreadsafe()) {
                MapleMonster mons = (MapleMonster) mmo;
                if (mons.getLinkOid() != monster.getObjectId()) {
                    killMonster(mons, chr, false, false, animation);
                }
            }
            /**
             * <时间的宠儿－品克缤 >
             */
        } else if (mobid >= 8820010 && mobid <= 8820014) {
            for (final MapleMapObject mmo : getAllMonstersThreadsafe()) {
                MapleMonster mons = (MapleMonster) mmo;
                if (mons.getId() != 8820000 && mons.getObjectId() != monster.getObjectId() && mons.getLinkOid() != monster.getObjectId()) {
                    killMonster(mons, chr, false, false, animation);
                }
            }
        }
        /**
         * <击杀怪物后的爆物 >
         */
        if (withDrops) {
            MapleCharacter drop = null;
            if (dropOwner <= 0) {
                drop = chr;
            } else {
                drop = getCharacterById(dropOwner);
                if (drop == null) {
                    drop = chr;
                }
            }
            dropFromMonster(drop, monster);
        }
    }

    public List<MapleReactor> getAllReactor() {
        return getAllReactorsThreadsafe();
    }

    public List<MapleReactor> getAllReactorsThreadsafe() {
        ArrayList<MapleReactor> ret = new ArrayList<MapleReactor>();
        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.REACTOR).values()) {
                ret.add((MapleReactor) mmo);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
        return ret;
    }

    public List<MapleMapObject> getAllDoor() {
        return getAllDoorsThreadsafe();
    }

    public List<MapleMapObject> getAllDoorsThreadsafe() {
        ArrayList<MapleMapObject> ret = new ArrayList<MapleMapObject>();
        mapobjectlocks.get(MapleMapObjectType.DOOR).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.DOOR).values()) {
                ret.add(mmo);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.DOOR).readLock().unlock();
        }
        return ret;
    }

    public List<MapleMapObject> getAllMerchant() {
        return getAllHiredMerchantsThreadsafe();
    }

    public List<MapleMapObject> getAllHiredMerchantsThreadsafe() {
        ArrayList<MapleMapObject> ret = new ArrayList<MapleMapObject>();
        mapobjectlocks.get(MapleMapObjectType.HIRED_MERCHANT).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.HIRED_MERCHANT).values()) {
                ret.add(mmo);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.HIRED_MERCHANT).readLock().unlock();
        }
        return ret;
    }

    public List<MapleMonster> getAllMonster() {
        return getAllMonstersThreadsafe();
    }

    public List<MapleMonster> getAllMonstersThreadsafe() {
        ArrayList<MapleMonster> ret = new ArrayList<MapleMonster>();
        mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.MONSTER).values()) {
                ret.add((MapleMonster) mmo);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().unlock();
        }
        return ret;
    }

    public List<MapleMonster> getAllMonstersThreadsafe(int a) {
        ArrayList<MapleMonster> ret = new ArrayList<MapleMonster>();
        mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.MONSTER).values()) {
                ret.add((MapleMonster) mmo);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().unlock();
        }
        return ret;
    }

    public final void killAllMonsters(final boolean animate) {
        for (final MapleMapObject monstermo : getAllMonstersThreadsafe()) {
            final MapleMonster monster = (MapleMonster) monstermo;
            spawnedMonstersOnMap.decrementAndGet();
            monster.setHp(0);
            broadcastMessage(MobPacket.killMonster(monster.getObjectId(), animate ? 1 : 0));
            removeMapObject(monster);
            monster.killed();

        }
    }

    public final void killMonster(final int monsId) {
        for (final MapleMapObject mmo : getAllMonstersThreadsafe()) {
            if (((MapleMonster) mmo).getId() == monsId) {
                spawnedMonstersOnMap.decrementAndGet();
                removeMapObject(mmo);
                broadcastMessage(MobPacket.killMonster(mmo.getObjectId(), 1));
                break;
            }
        }
    }

    private String MapDebug_Log() {
        final StringBuilder sb = new StringBuilder("Defeat time : ");
        sb.append(FileoutputUtil.CurrentReadable_Time());

        sb.append(" | Mapid : ").append(this.mapid);

        charactersLock.readLock().lock();
        try {
            sb.append(" Users [").append(characters.size()).append("] | ");
            for (MapleCharacter mc : characters) {
                sb.append(mc.getName()).append(", ");
            }
        } finally {
            charactersLock.readLock().unlock();
        }
        return sb.toString();
    }

    public final void limitReactor(final int rid, final int num) {
        List<MapleReactor> toDestroy = new ArrayList<MapleReactor>();
        Map<Integer, Integer> contained = new LinkedHashMap<Integer, Integer>();
        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            for (MapleMapObject obj : mapobjects.get(MapleMapObjectType.REACTOR).values()) {
                MapleReactor mr = (MapleReactor) obj;
                if (contained.containsKey(mr.getReactorId())) {
                    if (contained.get(mr.getReactorId()) >= num) {
                        toDestroy.add(mr);
                    } else {
                        contained.put(mr.getReactorId(), contained.get(mr.getReactorId()) + 1);
                    }
                } else {
                    contained.put(mr.getReactorId(), 1);
                }
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
        for (MapleReactor mr : toDestroy) {
            destroyReactor(mr.getObjectId());
        }
    }

    public final void destroyReactors(final int first, final int last) {
        List<MapleReactor> toDestroy = new ArrayList<MapleReactor>();
        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            for (MapleMapObject obj : mapobjects.get(MapleMapObjectType.REACTOR).values()) {
                MapleReactor mr = (MapleReactor) obj;
                if (mr.getReactorId() >= first && mr.getReactorId() <= last) {
                    toDestroy.add(mr);
                }
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
        for (MapleReactor mr : toDestroy) {
            destroyReactor(mr.getObjectId());
        }
    }

    public final void destroyReactor(final int oid) {
        final MapleReactor reactor = getReactorByOid(oid);
        broadcastMessage(MaplePacketCreator.destroyReactor(reactor));
        reactor.setAlive(false);
        removeMapObject(reactor);
        reactor.setTimerActive(false);

        if (reactor.getDelay() > 0) {
            MapTimer.getInstance().schedule(new Runnable() {

                @Override
                public final void run() {
                    respawnReactor(reactor);
                }
            }, reactor.getDelay());
        }
    }

    public final void reloadReactors() {
        List<MapleReactor> toSpawn = new ArrayList<MapleReactor>();
        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            for (MapleMapObject obj : mapobjects.get(MapleMapObjectType.REACTOR).values()) {
                final MapleReactor reactor = (MapleReactor) obj;
                broadcastMessage(MaplePacketCreator.destroyReactor(reactor));
                reactor.setAlive(false);
                reactor.setTimerActive(false);
                toSpawn.add(reactor);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
        for (MapleReactor r : toSpawn) {
            removeMapObject(r);
            if (r.getReactorId() != 9980000 && r.getReactorId() != 9980001) { //guardians cpq
                respawnReactor(r);
            }
        }
    }

    /*
     * command to reset all item-reactors in a map to state 0 for GM/NPC use -
     * not tested (broken reactors get removed from mapobjects when destroyed)
     * Should create instances for multiple copies of non-respawning reactors...
     */
    public final void resetReactors() {
        setReactorState((byte) 0);
    }

    public final void setReactorState() {
        setReactorState((byte) 1);
    }

    public final void setReactorState(final byte state) {
        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            for (MapleMapObject obj : mapobjects.get(MapleMapObjectType.REACTOR).values()) {
                ((MapleReactor) obj).forceHitReactor(state);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
    }

    /*
     * command to shuffle the positions of all reactors in a map for PQ purposes
     * (such as ZPQ/LMPQ)
     */
    public final void shuffleReactors() {
        shuffleReactors(0, 9999999); //all
    }

    public final void shuffleReactors(int first, int last) {
        List<Point> points = new ArrayList<Point>();
        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            for (MapleMapObject obj : mapobjects.get(MapleMapObjectType.REACTOR).values()) {
                MapleReactor mr = (MapleReactor) obj;
                if (mr.getReactorId() >= first && mr.getReactorId() <= last) {
                    points.add(mr.getPosition());
                }
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
        Collections.shuffle(points);
        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            for (MapleMapObject obj : mapobjects.get(MapleMapObjectType.REACTOR).values()) {
                MapleReactor mr = (MapleReactor) obj;
                if (mr.getReactorId() >= first && mr.getReactorId() <= last) {
                    mr.setPosition(points.remove(points.size() - 1));
                }
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
    }

    /**
     * 自动地从字符中找到给定怪物的新控制器。 on the map...
     *
     * @param monster
     */
    public final void updateMonsterController(final MapleMonster monster) {
        if (!monster.isAlive()) {
            return;
        }
        if (monster.getController() != null) {
            if (monster.getController().getMap() != this) {
                monster.getController().stopControllingMonster(monster);
            } else { // Everything is fine :)
                return;
            }
        }
        int mincontrolled = -1;
        MapleCharacter newController = null;

        charactersLock.readLock().lock();
        try {
            final Iterator<MapleCharacter> ltr = characters.iterator();
            MapleCharacter chr;
            while (ltr.hasNext()) {
                chr = ltr.next();
                if (!chr.isHidden() && !chr.isClone() && (chr.getControlledSize() < mincontrolled || mincontrolled == -1)) {
                    mincontrolled = chr.getControlledSize();
                    newController = chr;
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
        if (newController != null) {
            if (monster.isFirstAttack()) {
                newController.controlMonster(monster, true);
                monster.setControllerHasAggro(true);
                monster.setControllerKnowsAboutAggro(true);
            } else {
                newController.controlMonster(monster, false);
            }
        }
    }

    public final MapleMapObject getMapObject(int oid, MapleMapObjectType type) {
        mapobjectlocks.get(type).readLock().lock();
        try {
            return mapobjects.get(type).get(oid);
        } finally {
            mapobjectlocks.get(type).readLock().unlock();
        }
    }

    public final boolean containsNPC(int npcid) {
        mapobjectlocks.get(MapleMapObjectType.NPC).readLock().lock();
        try {
            Iterator<MapleMapObject> itr = mapobjects.get(MapleMapObjectType.NPC).values().iterator();
            while (itr.hasNext()) {
                MapleNPC n = (MapleNPC) itr.next();
                if (n.getId() == npcid) {
                    return true;
                }
            }
            return false;
        } finally {
            mapobjectlocks.get(MapleMapObjectType.NPC).readLock().unlock();
        }
    }

    public MapleNPC getNPCById(int id) {
        mapobjectlocks.get(MapleMapObjectType.NPC).readLock().lock();
        try {
            Iterator<MapleMapObject> itr = mapobjects.get(MapleMapObjectType.NPC).values().iterator();
            while (itr.hasNext()) {
                MapleNPC n = (MapleNPC) itr.next();
                if (n.getId() == id) {
                    return n;
                }
            }
            return null;
        } finally {
            mapobjectlocks.get(MapleMapObjectType.NPC).readLock().unlock();
        }
    }

    public MapleMonster getMonsterById(int id) {
        mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().lock();
        try {
            MapleMonster ret = null;
            Iterator<MapleMapObject> itr = mapobjects.get(MapleMapObjectType.MONSTER).values().iterator();
            while (itr.hasNext()) {
                MapleMonster n = (MapleMonster) itr.next();
                if (n.getId() == id) {
                    ret = n;
                    break;
                }
            }
            return ret;
        } finally {
            mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().unlock();
        }
    }

    public int countMonsterById(int id) {
        mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().lock();
        try {
            int ret = 0;
            Iterator<MapleMapObject> itr = mapobjects.get(MapleMapObjectType.MONSTER).values().iterator();
            while (itr.hasNext()) {
                MapleMonster n = (MapleMonster) itr.next();
                if (n.getId() == id) {
                    ret++;
                }
            }
            return ret;
        } finally {
            mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().unlock();
        }
    }

    public MapleReactor getReactorById(int id) {
        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            MapleReactor ret = null;
            Iterator<MapleMapObject> itr = mapobjects.get(MapleMapObjectType.REACTOR).values().iterator();
            while (itr.hasNext()) {
                MapleReactor n = (MapleReactor) itr.next();
                if (n.getReactorId() == id) {
                    ret = n;
                    break;
                }
            }
            return ret;
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
    }

    /**
     * returns a monster with the given oid, if no such monster exists returns
     * null
     *
     * @param oid
     * @return
     */
    public final MapleMonster getMonsterByOid(final int oid) {
        MapleMapObject mmo = getMapObject(oid, MapleMapObjectType.MONSTER);
        if (mmo == null) {
            return null;
        }
        return (MapleMonster) mmo;
    }

    public final MapleNPC getNPCByOid(final int oid) {
        MapleMapObject mmo = getMapObject(oid, MapleMapObjectType.NPC);
        if (mmo == null) {
            return null;
        }
        return (MapleNPC) mmo;
    }

    public final MapleReactor getReactorByOid(final int oid) {
        MapleMapObject mmo = getMapObject(oid, MapleMapObjectType.REACTOR);
        if (mmo == null) {
            return null;
        }
        return (MapleReactor) mmo;
    }

    public final MapleReactor getReactorByName(final String name) {
        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            for (MapleMapObject obj : mapobjects.get(MapleMapObjectType.REACTOR).values()) {
                MapleReactor mr = ((MapleReactor) obj);
                if (mr.getName().equalsIgnoreCase(name)) {
                    return mr;
                }
            }
            return null;
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
    }

    public final void spawnNpc(final int id, final Point pos) {
        final MapleNPC npc = MapleLifeFactory.getNPC(id);
        npc.setPosition(pos);
        npc.setCy(pos.y);
        npc.setRx0(pos.x + 50);
        npc.setRx1(pos.x - 50);
        npc.setFh(getFootholds().findBelow(pos).getId());
        npc.setCustom(true);
        addMapObject(npc);
        broadcastMessage(MaplePacketCreator.spawnNPC(npc, true));
    }

    public final void removeNpc(final int npcid) {
        mapobjectlocks.get(MapleMapObjectType.NPC).writeLock().lock();
        try {
            Iterator<MapleMapObject> itr = mapobjects.get(MapleMapObjectType.NPC).values().iterator();
            while (itr.hasNext()) {
                MapleNPC npc = (MapleNPC) itr.next();
                if (npc.isCustom() && npc.getId() == npcid) {
                    broadcastMessage(MaplePacketCreator.removeNPC(npc.getObjectId()));
                    itr.remove();
                }
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.NPC).writeLock().unlock();
        }
    }

    public final void spawnMonster_sSack(final MapleMonster mob, final Point pos, final int spawnType) {
        final Point spos = calcPointBelow(new Point(pos.x, pos.y - 1));
        mob.setPosition(spos);
        spawnMonster(mob, spawnType);
    }

    public final void spawnMonsterOnGroundBelow(final MapleMonster mob, final Point pos) {
        spawnMonster_sSack(mob, pos, -2);
    }

    public final void spawnMonster_sSack(final MapleMonster mob, final Point pos, final int spawnType, int hp) {
        final Point spos = calcPointBelow(new Point(pos.x, pos.y - 1));
        mob.setPosition(spos);
        mob.setHp(hp);
        spawnMonster(mob, spawnType);
    }

    public final void spawnMonsterOnGroundBelow2(final MapleMonster mob, final Point pos, long hp, int Exp) {

        spawnMonster2(mob, pos, -2, hp, Exp);
    }

    public final void spawnMonster2(final MapleMonster mob, final Point pos, final int spawnType, long hp, int Exp) {
        final Point spos = calcPointBelow(new Point(pos.x, pos.y - 1));
        mob.setPosition(spos);
        mob.setHp(hp);
        mob.setExp(Exp);
        spawnMonster(mob, spawnType);
    }

    public final void spawnMonsterOnGroundBelow(final MapleMonster mob, final Point pos, int hp) {
        spawnMonster_sSack(mob, pos, -2, hp);
    }

    public final int spawnMonsterWithEffectBelow(final MapleMonster mob, final Point pos, final int effect) {
        final Point spos = calcPointBelow(new Point(pos.x, pos.y - 1));
        return spawnMonsterWithEffect(mob, effect, spos);
    }

    public final void spawnZakum(final int x, final int y) {
        final Point pos = new Point(x, y);
        final MapleMonster mainb = MapleLifeFactory.getMonster(8800000);
        final Point spos = calcPointBelow(new Point(pos.x, pos.y - 1));
        mainb.setPosition(spos);
        mainb.setFake(true);
        spawnFakeMonster(mainb);

        final int[] zakpart = {8800003, 8800004, 8800005, 8800006, 8800007,
            8800008, 8800009, 8800010};

        for (final int i : zakpart) {
            final MapleMonster part = MapleLifeFactory.getMonster(i);
            part.setPosition(spos);
            spawnMonster(part, -2);
        }
        if (squadSchedule != null) {
            cancelSquadSchedule();
            //broadcastMessage(MaplePacketCreator.stopClock());
        }
//        for (MapleCharacter player : this.getCharacters()) {
//            player.Gaincharacterz("" + player.getId() + "", 500, 1);
//        }
    }

    public final void spawnChaosZakum(final int x, final int y) {//召唤扎昆
        final Point pos = new Point(x, y);
        final MapleMonster mainb = MapleLifeFactory.getMonster(8800100);
        final Point spos = calcPointBelow(new Point(pos.x, pos.y - 1));
        mainb.setPosition(spos);
        mainb.setFake(true);

        // Might be possible to use the map object for reference in future.
        spawnFakeMonster(mainb);

        final int[] zakpart = {8800103, 8800104, 8800105, 8800106, 8800107,
            8800108, 8800109, 8800110};

        for (final int i : zakpart) {
            final MapleMonster part = MapleLifeFactory.getMonster(i);
            part.setPosition(spos);

            spawnMonster(part, -2);
        }
        if (squadSchedule != null) {
            cancelSquadSchedule();
            // broadcastMessage(MaplePacketCreator.stopClock());
        }
    }

    public List<MapleMist> getAllMistsThreadsafe() {
        ArrayList<MapleMist> ret = new ArrayList<MapleMist>();
        mapobjectlocks.get(MapleMapObjectType.MIST).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.MIST).values()) {
                ret.add((MapleMist) mmo);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.MIST).readLock().unlock();
        }
        return ret;
    }

    public final void spawnFakeMonsterOnGroundBelow(final MapleMonster mob, final Point pos) {
        Point spos = calcPointBelow(new Point(pos.x, pos.y - 1));
        spos.y -= 1;
        mob.setPosition(spos);
        spawnFakeMonster(mob);
    }

    public int getMobsSize() {
        return this.mapobjects.get(MapleMapObjectType.MONSTER).size();
    }

    private void checkRemoveAfter(final MapleMonster monster) {
        final int ra = monster.getStats().getRemoveAfter();

        if (ra > 0) {
            MapTimer.getInstance().schedule(new Runnable() {

                @Override
                public final void run() {
                    if (monster != null && monster == getMapObject(monster.getObjectId(), monster.getType())) {
                        killMonster(monster);
                    }
                }
            }, ra * 1000);
        }
    }

    public final void spawnRevives(final MapleMonster monster, final int oid) {
        monster.setMap(this);
        checkRemoveAfter(monster);
        monster.setLinkOid(oid);
        spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {

            @Override
            public final void sendPackets(MapleClient c) {
                c.sendPacket(MobPacket.spawnMonster(monster, -2, 0, oid)); // TODO effect
            }
        }, null);
        updateMonsterController(monster);

        spawnedMonstersOnMap.incrementAndGet();
    }

    public final void spawnMonster(final MapleMonster monster, final int spawnType) {
        if (monster.getId() == 6090000) {
            return;
        }

        monster.setMap(this);
        checkRemoveAfter(monster);

        spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {

            public final void sendPackets(MapleClient c) {
                c.sendPacket(MobPacket.spawnMonster(monster, spawnType, 0, 0));
            }
        }, null);
        updateMonsterController(monster);

        spawnedMonstersOnMap.incrementAndGet();
    }

    public final int spawnMonsterWithEffect(final MapleMonster monster, final int effect, Point pos) {
        try {
            monster.setMap(this);
            monster.setPosition(pos);

            spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {

                @Override
                public final void sendPackets(MapleClient c) {
                    c.sendPacket(MobPacket.spawnMonster(monster, -2, effect, 0));
                }
            }, null);
            updateMonsterController(monster);

            spawnedMonstersOnMap.incrementAndGet();
            return monster.getObjectId();
        } catch (Exception e) {
            return -1;
        }
    }

    public final void spawnFakeMonster(final MapleMonster monster) {
        monster.setMap(this);
        monster.setFake(true);

        spawnAndAddRangedMapObject(monster, new DelayedPacketCreation() {

            @Override
            public final void sendPackets(MapleClient c) {
                c.sendPacket(MobPacket.spawnMonster(monster, -2, 0xfc, 0));
//		c.sendPacket(MobPacket.spawnFakeMonster(monster, 0));
            }
        }, null);
        updateMonsterController(monster);

        spawnedMonstersOnMap.incrementAndGet();
    }

    public final void spawnReactor(final MapleReactor reactor) {
        reactor.setMap(this);

        spawnAndAddRangedMapObject(reactor, new DelayedPacketCreation() {

            @Override
            public final void sendPackets(MapleClient c) {
                c.sendPacket(MaplePacketCreator.spawnReactor(reactor));
            }
        }, null);
    }

    private void respawnReactor(final MapleReactor reactor) {
        reactor.setState((byte) 0);
        reactor.setAlive(true);
        spawnReactor(reactor);
    }

    public final void spawnDoor(final MapleDoor door) {
        spawnAndAddRangedMapObject(door, new DelayedPacketCreation() {

            public final void sendPackets(MapleClient c) {
                c.sendPacket(MaplePacketCreator.spawnDoor(door.getOwner().getId(), door.getTargetPosition(), false));
                if (door.getOwner().getParty() != null && (door.getOwner() == c.getPlayer() || door.getOwner().getParty().containsMembers(new MaplePartyCharacter(c.getPlayer())))) {
                    c.sendPacket(MaplePacketCreator.partyPortal(door.getTown().getId(), door.getTarget().getId(), door.getSkill(), door.getTargetPosition()));
                }
                c.sendPacket(MaplePacketCreator.spawnPortal(door.getTown().getId(), door.getTarget().getId(), door.getSkill(), door.getTargetPosition()));
                c.sendPacket(MaplePacketCreator.enableActions());
            }
        }, new SpawnCondition() {

            public final boolean canSpawn(final MapleCharacter chr) {
                return door.getTarget().getId() == chr.getMapId() || door.getOwnerId() == chr.getId() || (door.getOwner() != null && door.getOwner().getParty() != null && door.getOwner().getParty().getMemberById(chr.getId()) != null);
            }
        });
    }

    public final void spawnSummon(final MapleSummon summon) {
        summon.updateMap(this);
        spawnAndAddRangedMapObject(summon, new DelayedPacketCreation() {

            @Override
            public void sendPackets(MapleClient c) {
                if (!summon.isChangedMap() || summon.getOwnerId() == c.getPlayer().getId()) {
                    c.sendPacket(MaplePacketCreator.spawnSummon(summon, true));
                }
            }
        }, null);
    }

    public final void spawnDragon(final MapleDragon summon) {
        spawnAndAddRangedMapObject(summon, new DelayedPacketCreation() {

            @Override
            public void sendPackets(MapleClient c) {
                //  c.sendPacket(MaplePacketCreator.spawnDragon(summon));
            }
        }, null);
    }

    public final void spawnMist(final MapleMist mist, final int duration, boolean fake) {
        spawnAndAddRangedMapObject(mist, new DelayedPacketCreation() {

            @Override
            public void sendPackets(MapleClient c) {
                mist.sendSpawnData(c);
            }
        }, null);

        final MapTimer tMan = MapTimer.getInstance();
        final ScheduledFuture<?> poisonSchedule;
        switch (mist.isPoisonMist()) {
            case 1:
                //poison: 0 = none, 1 = poisonous, 2 = recovery aura
                final MapleCharacter owner = getCharacterById(mist.getOwnerId());
                poisonSchedule = tMan.register(new Runnable() {

                    @Override
                    public void run() {
                        /*
                         * for (final MapleMapObject mo :
                         * getMapObjectsInRect(mist.getBox(),
                         * Collections.singletonList(MapleMapObjectType.MONSTER)))
                         * { if (mist.makeChanceResult()) { ((MapleMonster)
                         * mo).applyStatus(owner, new
                         * MonsterStatusEffect(MonsterStatus.POISON, 1,
                         * mist.getSourceSkill().getId(), null, false), true,
                         * duration, false); } }
                         */
                        for (final MapleMapObject mo : getMapObjectsInRect(mist.getBox(), Collections.singletonList(MapleMapObjectType.MONSTER))) {
                            /*
                             * if (mist.makeChanceResult() && ((MapleCharacter)
                             * mo).getId() != mist.getOwnerId()) {
                             * ((MapleCharacter)
                             * mo).setDOT(mist.getSource().getDOT(),
                             * mist.getSourceSkill().getId(),
                             * mist.getSkillLevel()); } else
                             */
                            if (mist.makeChanceResult() && !((MapleMonster) mo).isBuffed(MonsterStatus.POISON)) {
                                ((MapleMonster) mo).applyStatus(owner, new MonsterStatusEffect(MonsterStatus.POISON, 1, mist.getSourceSkill().getId(), null, false), true, duration, true/*
                                         * , mist.getSource()
                                 */);
                            }
                        }
                    }
                }, 2000, 2500);
                break;
            case 2:
                poisonSchedule = tMan.register(new Runnable() {

                    @Override
                    public void run() {
                        for (final MapleMapObject mo : getMapObjectsInRect(mist.getBox(), Collections.singletonList(MapleMapObjectType.PLAYER))) {
                            if (mist.makeChanceResult()) {
                                final MapleCharacter chr = ((MapleCharacter) mo);
                                chr.addMP((int) (mist.getSource().getX() * (chr.getStat().getMaxMp() / 100.0)));
                            }
                        }
                    }
                }, 2000, 2500);
                break;
            default:
                poisonSchedule = null;
                break;
        }
        tMan.schedule(new Runnable() {

            @Override
            public void run() {
                //broadcastMessage(MaplePacketCreator.removeMist(mist.getObjectId()));
                broadcastMessage(MaplePacketCreator.removeMist(mist.getObjectId(), false));
                removeMapObject(mist);
                if (poisonSchedule != null) {
                    poisonSchedule.cancel(false);
                }
            }
        }, duration);
    }

    public final void disappearingItemDrop(final MapleMapObject dropper, final MapleCharacter owner, final IItem item, final Point pos) {
        if (owner.isCheating) {
            return;
        }

        final Point droppos = calcDropPos(pos, pos);
        final MapleMapItem drop = new MapleMapItem(item, droppos, dropper, owner, (byte) 1, false);
        broadcastMessage(MaplePacketCreator.dropItemFromMapObject(drop, dropper.getPosition(), droppos, (byte) 3), drop.getPosition());
    }

    public final void spawnMesoDrop(final int meso, final Point position, final MapleMapObject dropper, final MapleCharacter owner, final boolean playerDrop, final byte droptype) {
        final Point droppos = calcDropPos(position, position);
        final MapleMapItem mdrop = new MapleMapItem(meso, droppos, dropper, owner, droptype, playerDrop);
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                if (owner.isCheating) {
                    return;
                }
                c.sendPacket(MaplePacketCreator.dropItemFromMapObject(mdrop, dropper.getPosition(), droppos, (byte) 1));
            }
        }, null);
        if (!everlast) {
            int 物品掉落持续时间 = gui.Start.ConfigValuesMap.get("物品掉落持续时间");
            mdrop.registerExpire(物品掉落持续时间 * 1000);//角色丢出金币持续时间

            if (droptype == 0 || droptype == 1) {
                mdrop.registerFFA(30000);
            }
        }
    }

    public final void spawnMobMesoDrop(final int meso, final Point position, final MapleMapObject dropper, final MapleCharacter owner, final boolean playerDrop, final byte droptype) {

        final MapleMapItem mdrop = new MapleMapItem(meso, position, dropper, owner, droptype, playerDrop);
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                if (owner.isCheating) {
                    return;
                }
                c.sendPacket(MaplePacketCreator.dropItemFromMapObject(mdrop, dropper.getPosition(), position, (byte) 1));
            }
        }, null);
        int 物品掉落持续时间 = gui.Start.ConfigValuesMap.get("物品掉落持续时间");
        mdrop.registerExpire(物品掉落持续时间 * 1000);//金币持续时间

        if (droptype == 0 || droptype == 1) {
            mdrop.registerFFA(30000);
        }

    }

    public final void spawnMobDrop(final IItem idrop, final Point dropPos, final MapleMonster mob, final MapleCharacter chr, final byte droptype, final short questid) {
        final MapleMapItem mdrop = new MapleMapItem(idrop, dropPos, mob, chr, droptype, false, questid);
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                if (chr.isCheating) {
                    return;
                }
                if (questid <= 0 || c.getPlayer().getQuestStatus(questid) == 1) {
                    c.sendPacket(MaplePacketCreator.dropItemFromMapObject(mdrop, mob.getPosition(), dropPos, (byte) 1));
                }
            }
        }, null);
        //broadcastMessage(MaplePacketCreator.dropItemFromMapObject(mdrop, mob.getPosition(), dropPos, (byte) 0));
        int 物品掉落持续时间 = gui.Start.ConfigValuesMap.get("物品掉落持续时间");
        mdrop.registerExpire(物品掉落持续时间 * 1000);//怪物出物品持续时间？
        if (droptype == 0 || droptype == 1) {
            mdrop.registerFFA(30000);
        }
        activateItemReactors(mdrop, chr.getClient());

    }

    public final void spawnRandDrop() {
        if (mapid != 749030000) {
            return;
        }
        mapobjectlocks.get(MapleMapObjectType.ITEM).readLock().lock();
        try {
            for (MapleMapObject o : mapobjects.get(MapleMapObjectType.ITEM).values()) {
                if (((MapleMapItem) o).isRandDrop()) {
                    return;
                }
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.ITEM).readLock().unlock();
        }
        MapTimer.getInstance().schedule(new Runnable() {
            public void run() {
                final Point pos = new Point(27, -9);//Randomizer.nextInt(800) + 531, 34 - Randomizer.nextInt(800)
                int itemid = 0;
                itemid = 2000005;//4000463
                spawnAutoDrop(itemid, pos);
                System.out.println("○ XXXXXXXXXXX");
            }
        }, 1 * 1000);
    }

    public final void spawnAutoDrop(final int itemid, final Point pos) {
        IItem idrop = null;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
            idrop = ii.randomizeStats((Equip) ii.getEquipById(itemid));
        } else {
            idrop = new Item(itemid, (byte) 0, (short) 1, (byte) 0);
        }
        final MapleMapItem mdrop = new MapleMapItem(pos, idrop);
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {

            @Override
            public void sendPackets(MapleClient c) {
                if (c.getPlayer().isCheating) {
                    return;
                }
                c.sendPacket(MaplePacketCreator.dropItemFromMapObject(mdrop, pos, pos, (byte) 1));
            }
        }, null);

        broadcastMessage(MaplePacketCreator.dropItemFromMapObject(mdrop, pos, pos, (byte) 0));
        mdrop.registerExpire(120000);//不知道是什么持续时间
    }

    public final void spawnAutoDrop2(final int itemid, final Point pos) {
        IItem idrop = null;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
            idrop = ii.randomizeStats((Equip) ii.getEquipById(itemid));
        } else {
            idrop = new Item(itemid, (byte) 0, (short) 1, (byte) 0);
        }
        final MapleMapItem mdrop = new MapleMapItem(pos, idrop);
        spawnAndAddRangedMapObject(mdrop, new DelayedPacketCreation() {
            @Override
            public void sendPackets(MapleClient c) {
                if (c.getPlayer().isCheating) {
                    return;
                }
                c.sendPacket(MaplePacketCreator.dropItemFromMapObject(mdrop, pos, pos, (byte) 1));
            }
        }, null);
        broadcastMessage(MaplePacketCreator.dropItemFromMapObject(mdrop, pos, pos, (byte) 0));
        mdrop.registerExpire(1000 * 10);
    }

    public final void 物品掉落(final MapleMapObject dropper, final MapleCharacter owner, final IItem item, Point pos, final boolean ffaDrop, final boolean playerDrop) {
        final Point droppos = calcDropPos(pos, pos);
        final MapleMapItem drop = new MapleMapItem(item, droppos, dropper, owner, (byte) 2, playerDrop);

        spawnAndAddRangedMapObject(drop, new DelayedPacketCreation() {

            @Override
            public void sendPackets(MapleClient c) {
                if (owner.isCheating) {
                    return;
                }
                c.sendPacket(MaplePacketCreator.dropItemFromMapObject(drop, dropper.getPosition(), droppos, (byte) 1));
            }
        }, null);

        if (owner.isCheating) {
            return;
        }
        broadcastMessage(MaplePacketCreator.dropItemFromMapObject(drop, dropper.getPosition(), droppos, (byte) 0));

        if (!everlast) {
            drop.registerExpire(10 * 1000);//物品存在时间
            activateItemReactors(drop, owner.getClient());
        }
    }

    public final void spawnItemDrop(final MapleMapObject dropper, final MapleCharacter owner, final IItem item, Point pos, final boolean ffaDrop, final boolean playerDrop) {
        final Point droppos = calcDropPos(pos, pos);
        final MapleMapItem drop = new MapleMapItem(item, droppos, dropper, owner, (byte) 2, playerDrop);

        spawnAndAddRangedMapObject(drop, new DelayedPacketCreation() {

            @Override
            public void sendPackets(MapleClient c) {
                if (owner.isCheating) {
                    return;
                }
                c.sendPacket(MaplePacketCreator.dropItemFromMapObject(drop, dropper.getPosition(), droppos, (byte) 1));
            }
        }, null);

        if (owner.isCheating) {
            return;
        }
        broadcastMessage(MaplePacketCreator.dropItemFromMapObject(drop, dropper.getPosition(), droppos, (byte) 0));

        if (!everlast) {
            if (getId() >= 209000001 && getId() <= 209000015) {
                drop.registerExpire(1000 * 60 * 60 * 1);
            } else {
                int 物品掉落持续时间 = gui.Start.ConfigValuesMap.get("物品掉落持续时间");
                drop.registerExpire(物品掉落持续时间 * 1000);//角色丢出金币持续时间
            }
            activateItemReactors(drop, owner.getClient());
        }
    }

    private void activateItemReactors(final MapleMapItem drop, final MapleClient c) {
        final IItem item = drop.getItem();

        mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().lock();
        try {
            for (final MapleMapObject o : mapobjects.get(MapleMapObjectType.REACTOR).values()) {
                final MapleReactor react = (MapleReactor) o;

                if (react.getReactorType() == 100) {
                    if (GameConstants.isCustomReactItem(react.getReactorId(), item.getItemId(), react.getReactItem().getLeft()) && react.getReactItem().getRight() == item.getQuantity()) {
                        if (react.getArea().contains(drop.getPosition())) {
                            if (!react.isTimerActive()) {
                                MapTimer.getInstance().schedule(new ActivateItemReactor(drop, react, c), 5000);
                                react.setTimerActive(true);
                                break;
                            }
                        }
                    }
                }
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.REACTOR).readLock().unlock();
        }
    }

    public int getItemsSize() {
        return mapobjects.get(MapleMapObjectType.ITEM).size();
    }

    public List<MapleMapItem> getAllItems() {
        return getAllItemsThreadsafe(); //Which genius wrote this?
    }

    public List<MapleMapItem> getAllItemsThreadsafe() {
        ArrayList<MapleMapItem> ret = new ArrayList<MapleMapItem>();
        mapobjectlocks.get(MapleMapObjectType.ITEM).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.ITEM).values()) {
                ret.add((MapleMapItem) mmo);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.ITEM).readLock().unlock();
        }
        return ret;
    }

    public final void returnEverLastItem(final MapleCharacter chr) {
        for (final MapleMapObject o : getAllItemsThreadsafe()) {
            final MapleMapItem item = ((MapleMapItem) o);
            if (item.getOwner() == chr.getId()) {
                item.setPickedUp(true);
                broadcastMessage(MaplePacketCreator.removeItemFromMap(item.getObjectId(), 2, chr.getId()), item.getPosition());
                if (item.getMeso() > 0) {
                    chr.gainMeso(item.getMeso(), false);
                } else {
                    MapleInventoryManipulator.addFromDrop(chr.getClient(), item.getItem(), false);
                }
                removeMapObject(item);
            }
        }
        spawnRandDrop();
    }

    public final void talkMonster(final String msg, final int itemId, final int objectid) {
        if (itemId > 0) {
            startMapEffect(msg, itemId, false);
        }
        broadcastMessage(MobPacket.talkMonster(objectid, itemId, msg)); //5120035
        broadcastMessage(MobPacket.removeTalkMonster(objectid));
    }

    public final void startMapEffect(final String msg, final int itemId) {
        startMapEffect(msg, itemId, false);
    }

    public final void startMapEffect(final String msg, final int itemId, final boolean jukebox) {
        if (mapEffect != null) {
            return;
        }
        mapEffect = new MapleMapEffect(msg, itemId);
        mapEffect.setJukebox(jukebox);
        broadcastMessage(mapEffect.makeStartData());
        MapTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                broadcastMessage(mapEffect.makeDestroyData());
                mapEffect = null;
            }
        }, jukebox ? 300000 : 30000);
    }

    public final void startExtendedMapEffect(final String msg, final int itemId) {
        broadcastMessage(MaplePacketCreator.startMapEffect(msg, itemId, true));
        MapTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                broadcastMessage(MaplePacketCreator.removeMapEffect());
                broadcastMessage(MaplePacketCreator.startMapEffect(msg, itemId, false));
                //dont remove mapeffect.
            }
        }, 60000);
    }

    public final void startJukebox(final String msg, final int itemId) {
        startMapEffect(msg, itemId, true);
    }

    public int getBossRank(int cid, String bossname, byte type) {
        int ret = -1;
        BossRankInfo info = BossRankManager.getInstance().getInfo(cid, bossname);
        if (null == info) {
            return ret;
        }
        switch (type) {
            case 1://积分
                ret = info.getPoints();
                break;
            case 2://次数
                ret = info.getCount();
                break;
        }
        return ret;
    }

    public final void 送货(final MapleCharacter chr) {
        int 地图 = chr.getMapId();
        /**
         * <第一阶段9>
         */
        if (chr.getBossLog("送货") == 1) {
            if (地图 == 104000100 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 明珠港郊外 → 海岸草丛I");
            } else if (地图 == 104000200 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 海岸草丛I → 海岸草丛II");
            } else if (地图 == 104000300 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 海岸草丛II → 海岸草丛III");
            } else if (地图 == 104000400 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 海岸草丛III → 三叉路");
            } else if (地图 == 104010000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 三叉路 → 射手村西部森林");
            } else if (地图 == 104020000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 射手村西部森林 → 射手村西部小山");
            } else if (地图 == 104030000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 射手村西部小山 → 射手村训练场I");
            } else if (地图 == 104040000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 射手村训练场I → 射手村");
            } else if (地图 == 100000000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 射手村 → 找送货员");
            }
        }
        /**
         * <第二阶段7>
         */
        if (chr.getBossLog("送货") == 3) {
            if (地图 == 100010000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 射手村东部小山 → 射手村东部草丛");
            } else if (地图 == 100020000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 射手村东部草丛 → 射手村东部树林");
            } else if (地图 == 100030000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 射手村东部树林 → 魔法森林南部");
            } else if (地图 == 100040000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 魔法森林南部 → 智慧森林");
            } else if (地图 == 100040100 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 智慧森林 → 魔法森林南郊");
            } else if (地图 == 100050000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 魔法森林南郊 → 魔法森林");
            } else if (地图 == 101000000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 魔法森林 → 找送货员");
            }
        }

        /**
         * <第三阶段10>
         */
        if (chr.getBossLog("送货") == 5) {
            if (地图 == 101010000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 魔法森林北郊 → 大木林I");
            } else if (地图 == 101010100 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 大木林I → 魔法森林北郊");
            } else if (地图 == 101020000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 魔法森林北郊 → 勇士部落东部");
            } else if (地图 == 101030000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 勇士部落东部 → 岩石路III");
            } else if (地图 == 101030100 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 岩石路III → 岩石路II");
            } else if (地图 == 101030200 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 岩石路II → 岩石路I");
            } else if (地图 == 101030300 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 岩石路I → 东部岩山I");
            } else if (地图 == 101030400 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 东部岩山I → 勇士部落东部入口");
            } else if (地图 == 101040000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 勇士部落东部入口 → 勇士部落");
            } else if (地图 == 102000000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 勇士部落 → 找送货员");
            }
        }
        /**
         * <第三阶段6>
         */
        if (chr.getBossLog("送货") == 7) {
            if (地图 == 102010000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 勇士村西入口 → 西部岩山I");
            } else if (地图 == 102020000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 西部岩山I → 勇士部落西部");
            } else if (地图 == 102030000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 勇士部落西部 → 废都北方工地");
            } else if (地图 == 102040000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 废都北方工地 → 废都北入口");
            } else if (地图 == 102050000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 废都北入口 → 废弃都市");
            } else if (地图 == 103000000 && chr.getBossLog("送货" + 地图 + "") == 0) {
                chr.setBossLog("送货" + 地图 + "");
                chr.dropMessage(5, "[送货导航] : 废弃都市 → 找送货员");
            }
        }
    }

    //过图执行文件
    public final void addPlayer(final MapleCharacter chr) {
        //刷怪线程
        charactersLock.writeLock().lock();
        try {
            characters.add(chr);
        } finally {
            charactersLock.writeLock().unlock();
        }
        mapobjectlocks.get(MapleMapObjectType.PLAYER).writeLock().lock();
        try {
            mapobjects.get(MapleMapObjectType.PLAYER).put(chr.getObjectId(), chr);
        } finally {
            mapobjectlocks.get(MapleMapObjectType.PLAYER).writeLock().unlock();
        }
//        if (gui.Start.ConfigValuesMap.get("吸怪检测开关") == 0) {
//            if (chr.isAdmin()) {
//                int 地图 = chr.getMapId();
//                if (gui.Start.地图吸怪检测.get("" + 地图 + "") != null) {
//                    int 吸怪检测值 = gui.Start.地图吸怪检测.get("" + 地图 + "");
//                    chr.dropMessage(5, "<地图安全检测系统>");
//                    chr.dropMessage(5, " 地图代码: " + 地图);
//                    chr.dropMessage(5, " 地图检测类型: 自设");
//                    chr.dropMessage(5, " 吸怪检测数值: " + 吸怪检测值);
//                } else {
//                    int 吸怪检测值 = gui.Start.地图吸怪检测.get("0");
//                    chr.dropMessage(5, "<地图安全检测系统>");
//                    chr.dropMessage(5, " 地图代码: " + 地图);
//                    chr.dropMessage(5, " 地图检测类型: 默认");
//                    chr.dropMessage(5, " 吸怪检测数值: " + 吸怪检测值);
//                }
//            }
//        }
        //卡地图错误
        if (chr.getMapId() == 999999999) {
            chr.dropMessage(1, "卡地图解救");
            chr.changeMap(100000000, 0);
        }
        chr.关闭财神线程();
        chr.关闭浴桶线程();
        if (gui.Start.ConfigValuesMap.get("每日送货开关") == 0) {
            if (chr.getBossLog("送货") > 0 && chr.getBossLog("送货") < 8) {
                送货(chr);
            }
        }
        //重返信息
        switch (chr.getMapId()) {
            //扎昆
            case 280030000:
                chr.Gaincharacterz("" + chr.getId() + "", 500, 1);
                break;
            //黑龙
            case 240060200:
                chr.Gaincharacterz("" + chr.getId() + "", 501, 1);
                break;
            //pb
            case 270050100:
                chr.Gaincharacterz("" + chr.getId() + "", 502, 1);
                break;
            default:
                break;
        }
        if (chr.F().get(FM("落叶斩")) != null) {
            chr.枫叶数量 = chr.判断物品数量(4001126);
        }

        if (chr.F().get(FM("暗器伤人")) != null) {
            chr.飞镖数量 = chr.判断物品数量(2070006);
        }

        //看转职动画卡了的话会强制传送过来
        if (chr.getMapId() >= 1020100 && chr.getMapId() <= 1020500) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 5);
                        if (chr.getMapId() >= 1020100 && chr.getMapId() <= 1020500) {
                            chr.changeMap(1020000, 0);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }.start();
        }
        //进地图显示地图名字
        if (gui.Start.ConfigValuesMap.get("地图名称开关") == 0) {
            chr.startMapEffect(chr.getMap().getMapName(), 5120023, 5000);
        }
        //回收魔法手套和雪球
        chr.removeAll(1472063);
        chr.removeAll(2060006);
        //年糕
        chr.removeAll(4001101);

        //频幕中显示文字
        switch (mapid) {
            case 209080000:
                if (getMonsterById(9400714) == null) {
                    MapleMonster mob1 = MapleLifeFactory.getMonster(9400714);
                    spawnMonsterOnGroundBelow(mob1, new Point(1450, 140));
                }
                break;
            case 180000001:
                chr.startMapEffect("拉 桑 特 监 狱", 5120018);
                break;
            /* case 100000203:
                chr.removeAll(4000140);
                chr.dropMessage(1, "活动地图说明\r\n\r\n"
                        + "1.活动开始后禁止使用[群体治疗]，否则会被传送出局。\r\n"
                        + "2.活动开始后地图限制使用治疗道具。");
                break;*/
            case 910010000:
                chr.startMapEffect("快种上种子，保护月妙兔兔生产年糕吧！", 5120016);
                break;
            //废弃都市
            case 103000800:
                chr.removeAll(4001008);
                chr.removeAll(4001007);
                chr.startMapEffect("解决问题并收集通行证的数量，即可进入下一关！", 5120017);
                chr.dropMessage(6, "如果发现队友掉线，请使用 @拉回队友 进行拉回。");
                chr.Gaincharacterz("" + chr.getId() + "", 499, 1);
                break;
            case 103000801:
                chr.startMapEffect("团队合作，爬上绳索揭开正确的组合吧!", 5120017);
                break;
            case 103000802:
                chr.startMapEffect("团队合作，在平台上揭开正确的组合吧!", 5120017);
                break;
            case 103000803:
                chr.startMapEffect("团队合作，在木桶上揭开正确的组合吧!", 5120017);
                break;
            case 103000804:
                chr.startMapEffect("打败绿水灵王和它的小水灵吧！！!", 5120017);
                break;
            case 103000805:
                chr.startMapEffect("恭喜你通关了，领取你的奖励吧！", 5120017);
                break;
            //玩具塔
            case 922010100:
                chr.removeAll(4001022);
                chr.removeAll(4001023);
                chr.startMapEffect("击杀老鼠，收集通行证就可以进入下一关！", 5120018);
                break;
            case 922010200:
                chr.startMapEffect("打碎箱子，收集通行证就可以进入下一关!", 5120018);
                break;
            case 922010300:
                chr.startMapEffect("击杀怪物，收集通行证就可以进入下一关!", 5120018);
                break;
            case 922010400:
                chr.startMapEffect("击杀黑暗中的怪物，收集通行证!", 5120018);
                break;
            case 922010500:
                chr.startMapEffect("赶快动起来，收集通行证!", 5120018);
                break;
            case 922010600:
                chr.startMapEffect("这里只有一条正确的路！", 5120018);
                break;
            case 922010700:
                chr.startMapEffect("击杀怪物，收集通行证就可以进入下一关！", 5120018);
                break;
            case 922010800:
                chr.startMapEffect("跳上箱子，和队友推算是正确的组合吧！", 5120018);
                break;
            case 922010900:
                chr.startMapEffect("击杀BOSS，拿到钥匙！", 5120018);
                break;
            case 920010000:
                chr.startMapEffect("打碎白色的云朵，收集云片！", 5120019);
                break;
            case 920010200:
                chr.startMapEffect("收集第一个小碎片吧！", 5120019);
                break;
            case 920010300:
                chr.startMapEffect("收集第二个小碎片吧！", 5120019);
                break;
            case 920010400:
                chr.startMapEffect("这美妙的音乐，真是让人心旷神怡！", 5120019);
                break;
            case 920010500:
                chr.startMapEffect("想一想，该如何通关呢！", 5120019);
                break;
            case 920010600:
                chr.startMapEffect("想一想，该如何通关呢！", 5120019);
                break;
            case 920010700:
                chr.startMapEffect("想一想，该如何通关呢！", 5120019);
                break;
            case 925100000:
                //海盗清理
                chr.removeAll(4001117);
                chr.removeAll(4001120);
                chr.removeAll(4001121);
                chr.removeAll(4001122);
                chr.startMapEffect("打碎宝箱，收集钥匙", 5120020);
                break;
            case 925100100:
                chr.startMapEffect("击杀海盗，收集海盗证明！", 5120020);
                break;
            case 925100300:
                chr.startMapEffect("消灭这里的守卫，快消灭他们！", 5120020);
                break;
            case 925100500:
                chr.startMapEffect("消灭老海盗！", 5120020);
                break;
            case 926100000:
                chr.startMapEffect("请找到隐藏的门，通过调查实验室！", 5120021);
                break;
            case 926100001:
                chr.startMapEffect("找到你的方式通过这黑暗！", 5120021);
                break;
            case 926100100:
                chr.startMapEffect("充满能量的烧杯！", 5120021);
                break;
            case 926100200:
                chr.startMapEffect("获取实验的文件通过每个门!", 5120021);
                break;
            case 926100203:
                chr.startMapEffect("请打败所有的怪物！!", 5120021);
                break;
            case 926100300:
                chr.startMapEffect("找到你的方法通过实验室！", 5120021);
                break;
            case 926100401:
                chr.startMapEffect("请保护我的爱人！", 5120021);
                break;
            case 930000000:
                chr.startMapEffect("进入传送点，我要对你们施放变身魔法了！", 5120023);
                break;
            case 930000100:
                chr.startMapEffect("消灭这里的怪物，将怪物数量净化到低于20只就可以通关了！", 5120023);
                break;
            case 930000200:
                chr.startMapEffect("需要稀释毒液，然后释放在荆棘上面，就可以打破阻拦！", 5120023);
                break;
            case 930000300:
                chr.startMapEffect("找到出去的路，别迷失在这里!", 5120023);
                break;
            case 930000500:
                chr.startMapEffect("赶快去上方寻找紫色魔力石！!", 5120023);
                break;
            case 930000600:
                chr.startMapEffect("将魔力石放在祭坛上！", 5120023);
                break;
            default:
                break;
        }
//        if (mapid == 100000203) {
//            if (MapleParty.大逃杀活动 > 0) {
//                chr.changeMap(100000200, 0);
//                chr.dropMessage(5, "活动已经开始，无法进入。");
//                return;
//            }
//        }

        if (!chr.isHidden()) {
            broadcastMessage(chr, MaplePacketCreator.spawnPlayerMapobject(chr), false);
            if (chr.isGM() && speedRunStart > 0) {
                endSpeedRun();
                broadcastMessage(MaplePacketCreator.serverNotice(5, "速度运行已经结束。"));
            }
        }
        //冒险岛活动
        if (mapid == 109080000 || mapid == 109080001 || mapid == 109080002 || mapid == 109080003 || mapid == 109080010 || mapid == 109080011 || mapid
                == 109080012) {
            chr.setCoconutTeam(getAndSwitchTeam() ? 0 : 1);
        }
        //冒险岛活动

        if (!chr.isClone()) {
            //屏蔽地图动画
            /*if (!onFirstUserEnter.equals("")) {
                if (getCharactersSize() == 1) {
                    MapScriptMethods.startScript_FirstUser(chr.getClient(), onFirstUserEnter);
                }
            }*/
            sendObjectPlacement(chr);
            chr.getClient().sendPacket(MaplePacketCreator.spawnPlayerMapobject(chr));
            //屏蔽地图动画
            /*if (!onUserEnter.equals("")) {
                MapScriptMethods.startScript_User(chr.getClient(), onUserEnter);
            }*/
            switch (mapid) {
                //上楼
                case 109030001:
                //向高地
                case 109040000:
                //活动入口
                case 109060001:
                //打椰子
                case 109080000:
                //打瓶盖
                case 109080010:
                    chr.getClient().sendPacket(MaplePacketCreator.showEventInstructions());
                    break;
                /*case 109080002:
                case 109080003:
                case 109080011:
                case 109080012:
                    chr.getClient().sendPacket(MaplePacketCreator.showEquipEffect(chr.getCoconutTeam()));
                    break;*/
                //澡堂男
                case 809000101:
                //澡堂女
                case 809000201:
                    chr.getClient().sendPacket(MaplePacketCreator.showEquipEffect());
                    break;
            }
        }
        for (final MaplePet pet : chr.getPets()) {
            if (pet.getSummoned()) {
                chr.getClient().sendPacket(PetPacket.updatePet(pet, chr.getInventory(MapleInventoryType.CASH).getItem((byte) pet.getInventoryPosition()), true));
                broadcastMessage(chr, PetPacket.showPet(chr, pet, false, false), false);
            }
        }

        if (hasForcedEquip()) {
            chr.getClient().sendPacket(MaplePacketCreator.showForcedEquip());
        }

        chr.getClient().sendPacket(MaplePacketCreator.removeTutorialStats());
        //战神剧情
        if (chr.getMapId() >= 914000200 && chr.getMapId() <= 914000220) {
            chr.getClient().sendPacket(MaplePacketCreator.addTutorialStats());
        }
        //战神召唤女跟随NPC
        if (chr.getMapId() >= 140090100 && chr.getMapId() <= 140090500 || chr.getJob() == 1000 && chr.getMapId() != 130030000) {
            chr.getClient().sendPacket(MaplePacketCreator.spawnTutorialSummon(1));
        }
        //地图事件1
        if (!onUserEnter.equals("")) {
            MapScriptMethods.startScript_User(chr.getClient(), onUserEnter);
            //MapScriptManager.getInstance().getMapScript(chr.getClient(), onUserEnter, false);
        }
        //地图事件2
        if (!onFirstUserEnter.equals("")) {
            if (getCharacters().size() == 1) {
                MapScriptMethods.startScript_FirstUser(chr.getClient(), onFirstUserEnter);
                // MapScriptManager.getInstance().getMapScript(chr.getClient(), onFirstUserEnter, true);
            }
        }
        final MapleStatEffect stat = chr.getStatForBuff(MapleBuffStat.SUMMON);
        if (stat != null && !chr.isClone()) {
            final MapleSummon summon = chr.getSummons().get(stat.getSourceId());
            summon.setPosition(chr.getPosition());
            try {
                summon.setFh(getFootholds().findBelow(chr.getPosition()).getId());
            } catch (NullPointerException e) {
                summon.setFh(0);
            }
            this.spawnSummon(summon);
            chr.addVisibleMapObject(summon);
        }

        if (chr.getChalkboard() != null) {
            chr.getClient().sendPacket(MTSCSPacket.useChalkboard(chr.getId(), chr.getChalkboard()));
        }
        //爱心？
        broadcastMessage(MaplePacketCreator.loveEffect());
        //刷怪？
        if (timeLimit > 0 && getForcedReturnMap() != null && !chr.isClone()) {
            chr.startMapTimeLimitTask(timeLimit, getForcedReturnMap());
        }

        if (getSquadBegin() != null && getSquadBegin().getTimeLeft() > 0 && getSquadBegin().getStatus() == 1) {
            chr.getClient().sendPacket(MaplePacketCreator.getClock((int) (getSquadBegin().getTimeLeft() / 1000)));
        }

        if (chr.getCarnivalParty() != null && chr.getEventInstance() != null) {
            chr.getEventInstance().onMapLoad(chr);
        }

        MapleEvent.mapLoad(chr, channel);

        if (chr.getEventInstance() != null && chr.getEventInstance().isTimerStarted() && !chr.isClone()) {
            chr.getClient().sendPacket(MaplePacketCreator.getClock((int) (chr.getEventInstance().getTimeLeft() / 1000)));
        }

        if (hasClock()) {
            final Calendar cal = Calendar.getInstance();
            chr.getClient().sendPacket((MaplePacketCreator.getClockTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND))));
        }

        if (isTown()) {
            chr.cancelEffectFromBuffStat(MapleBuffStat.RAINING_MINES);
        }

        if (chr.getParty() != null && !chr.isClone()) {
            //chr.silentPartyUpdate();
            //chr.getClient().sendPacket(MaplePacketCreator.updateParty(chr.getClient().getChannel(), chr.getParty(), PartyOperation.SILENT_UPDATE, null));
            chr.updatePartyMemberHP();
            chr.receivePartyMemberHP();
        }
        if (permanentWeather > 0) {
            chr.getClient().sendPacket(MaplePacketCreator.startMapEffect("", permanentWeather, false)); //snow, no msg
        }

        if (getPlatforms().size() > 0) {
            chr.getClient().sendPacket(MaplePacketCreator.getMovingPlatforms(this));
        }

        if (environment.size() > 0) {
            chr.getClient().sendPacket(MaplePacketCreator.getUpdateEnvironment(this));
        }

        if (isTown()) {
            chr.cancelEffectFromBuffStat(MapleBuffStat.RAINING_MINES);
        }
        if (gui.Start.ConfigValuesMap.get("过图存档开关") == 0) {
            chr.saveToDB(false, false);
        }
        if (gui.Start.ConfigValuesMap.get("下雪天开关") <= 0 || gui.Start.ConfigValuesMap.get("下红花开关") <= 0 || gui.Start.ConfigValuesMap.get("下气泡开关") <= 0 || gui.Start.ConfigValuesMap.get("下雪花开关") <= 0 || gui.Start.ConfigValuesMap.get("下枫叶开关") <= 0) {
            if (gui.Start.ConfigValuesMap.get("下雪天开关") <= 0) {
                chr.getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120000, false));
            } else if (gui.Start.ConfigValuesMap.get("下红花开关") <= 0) {
                chr.getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120001, false));
            } else if (gui.Start.ConfigValuesMap.get("下气泡开关") <= 0) {
                chr.getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120002, false));
            } else if (gui.Start.ConfigValuesMap.get("下雪花开关") <= 0) {
                chr.getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120003, false));
            } else if (gui.Start.ConfigValuesMap.get("下枫叶开关") <= 0) {
                chr.getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120008, false));
            }
        }
        /**
         * <飞天检测>
         */
        int 地图 = mapid;
        if (chr.过图 >= 10) {
            chr.过图 = 0;
            chr.地图缓存1 = 0;
            chr.地图缓存2 = 0;
            chr.地图缓存3 = 0;
            chr.地图缓存4 = 0;
            chr.地图缓存5 = 0;
            chr.地图缓存6 = 0;
            chr.地图缓存7 = 0;
            chr.地图缓存8 = 0;
            chr.地图缓存9 = 0;
            chr.地图缓存10 = 0;
            chr.地图缓存时间1 = "";
            chr.地图缓存时间2 = "";
            chr.地图缓存时间3 = "";
            chr.地图缓存时间4 = "";
            chr.地图缓存时间5 = "";
            chr.地图缓存时间6 = "";
            chr.地图缓存时间7 = "";
            chr.地图缓存时间8 = "";
            chr.地图缓存时间9 = "";
            chr.地图缓存时间10 = "";
        }
        chr.过图++;
        switch (chr.过图) {
            case 1:
                chr.地图缓存1 = 地图;
                chr.地图缓存时间1 = CurrentReadable_Time();
                break;
            case 2:
                chr.地图缓存2 = 地图;
                chr.地图缓存时间2 = CurrentReadable_Time();
                break;
            case 3:
                chr.地图缓存3 = 地图;
                chr.地图缓存时间3 = CurrentReadable_Time();
                break;
            case 4:
                chr.地图缓存4 = 地图;
                chr.地图缓存时间4 = CurrentReadable_Time();
                break;
            case 5:
                chr.地图缓存5 = 地图;
                chr.地图缓存时间5 = CurrentReadable_Time();
                break;
            case 6:
                chr.地图缓存6 = 地图;
                chr.地图缓存时间6 = CurrentReadable_Time();
                break;
            case 7:
                chr.地图缓存7 = 地图;
                chr.地图缓存时间7 = CurrentReadable_Time();
                break;
            case 8:
                chr.地图缓存8 = 地图;
                chr.地图缓存时间8 = CurrentReadable_Time();
                break;
            case 9:
                chr.地图缓存9 = 地图;
                chr.地图缓存时间9 = CurrentReadable_Time();
                break;
            case 10:
                chr.地图缓存10 = 地图;
                chr.地图缓存时间10 = CurrentReadable_Time();
                break;
            default:
                break;
        }
        if (mapid == 280020000) {
            chr.坐标检测开启();
        }
    }

    public void 记录地图(int a) {
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO map (id) VALUES ( ?)")) {
            ps.setInt(1, a);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {

        }
    }

    public int 判断地图(int a) {
        int data = 0;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM map where id =" + a + "");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data += 1;
            }
            ps.close();
        } catch (SQLException ex) {
        }
        return data;
    }

    public int getNumItems() {

        mapobjectlocks.get(MapleMapObjectType.ITEM).readLock().lock();
        try {
            return mapobjects.get(MapleMapObjectType.ITEM).size();
        } finally {
            mapobjectlocks.get(MapleMapObjectType.ITEM).readLock().unlock();
        }
    }

    private boolean hasForcedEquip() {
        return fieldType == 81 || fieldType == 82;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public int getNumMonsters() {
        mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().lock();
        try {
            return mapobjects.get(MapleMapObjectType.MONSTER).size();
        } finally {
            mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().unlock();
        }
    }

    public void doShrine(final boolean spawned) { //false = entering map, true = defeated
        if (squadSchedule != null) {
            cancelSquadSchedule();
        }
        final int mode = (mapid == 280030000 ? 1 : (mapid == 280030001 ? 2 : (mapid == 240060200 || mapid == 240060201 ? 3 : 0)));
        //chaos_horntail message for horntail too because it looks nicer
        final MapleSquad sqd = getSquadByMap();
        final EventManager em = getEMByMap();
        if (sqd != null && em != null && getCharactersSize() > 0) {
            final String leaderName = sqd.getLeaderName();
            final String state = em.getProperty("state");
            final Runnable run;
            MapleMap returnMapa = getForcedReturnMap();
            if (returnMapa == null || returnMapa.getId() == mapid) {
                returnMapa = getReturnMap();
            }
            switch (mode) {
                case 1:
                    //zakum
                    broadcastMessage(MaplePacketCreator.showZakumShrine(spawned, 5));
                    break;
                case 2:
                    //chaoszakum
                    broadcastMessage(MaplePacketCreator.showChaosZakumShrine(spawned, 5));
                    break;
                case 3:
                    //ht/chaosht
                    broadcastMessage(MaplePacketCreator.showChaosHorntailShrine(spawned, 5));
                    break;
                default:
                    broadcastMessage(MaplePacketCreator.showHorntailShrine(spawned, 5));
                    break;
            }
            if (mode == 1 || spawned) { //both of these together dont go well
                broadcastMessage(MaplePacketCreator.getClock(300)); //5 min
            }
            final MapleMap returnMapz = returnMapa;
            if (!spawned) { //no monsters yet; inforce timer to spawn it quickly
                final List<MapleMonster> monsterz = getAllMonstersThreadsafe();
                final List<Integer> monsteridz = new ArrayList<Integer>();
                for (MapleMapObject m : monsterz) {
                    monsteridz.add(m.getObjectId());
                }
                run = new Runnable() {

                    public void run() {
                        final MapleSquad sqnow = MapleMap.this.getSquadByMap();
                        if (MapleMap.this.getCharactersSize() > 0 && MapleMap.this.getNumMonsters() == monsterz.size() && sqnow != null && sqnow.getStatus() == 2 && sqnow.getLeaderName().equals(leaderName) && MapleMap.this.getEMByMap().getProperty("state").equals(state)) {
                            boolean passed = monsterz.isEmpty();
                            for (MapleMapObject m : MapleMap.this.getAllMonstersThreadsafe()) {
                                for (int i : monsteridz) {
                                    if (m.getObjectId() == i) {
                                        passed = true;
                                        break;
                                    }
                                }
                                if (passed) {
                                    break;
                                } //even one of the monsters is the same
                            }
                            if (passed) {
                                //are we still the same squad? are monsters still == 0?
                                byte[] packet;
                                if (mode == 1) { //zakum
                                    packet = MaplePacketCreator.showZakumShrine(spawned, 0);
                                } else if (mode == 2) { //chaoszakum
                                    packet = MaplePacketCreator.showChaosZakumShrine(spawned, 0);
                                } else {
                                    packet = MaplePacketCreator.showHorntailShrine(spawned, 0); //chaoshorntail message is weird
                                }
                                for (MapleCharacter chr : MapleMap.this.getCharactersThreadsafe()) { //warp all in map
                                    chr.getClient().sendPacket(packet);
                                    chr.changeMap(returnMapz, returnMapz.getPortal(0)); //hopefully event will still take care of everything once warp out
                                }
                                checkStates("");
                                resetFully();
                            }
                        }

                    }
                };
            } else { //inforce timer to gtfo
                run = new Runnable() {

                    public void run() {
                        MapleSquad sqnow = MapleMap.this.getSquadByMap();
                        //we dont need to stop clock here because they're getting warped out anyway
                        if (MapleMap.this.getCharactersSize() > 0 && sqnow != null && sqnow.getStatus() == 2 && sqnow.getLeaderName().equals(leaderName) && MapleMap.this.getEMByMap().getProperty("state").equals(state)) {
                            //are we still the same squad? monsters however don't count
                            byte[] packet;
                            if (mode == 1) { //zakum
                                packet = MaplePacketCreator.showZakumShrine(spawned, 0);
                            } else if (mode == 2) { //chaoszakum
                                packet = MaplePacketCreator.showChaosZakumShrine(spawned, 0);
                            } else {
                                packet = MaplePacketCreator.showHorntailShrine(spawned, 0); //chaoshorntail message is weird
                            }
                            for (MapleCharacter chr : MapleMap.this.getCharactersThreadsafe()) { //warp all in map
                                chr.getClient().sendPacket(packet);
                                chr.changeMap(returnMapz, returnMapz.getPortal(0)); //hopefully event will still take care of everything once warp out
                            }
                            checkStates("");
                            resetFully();
                        }
                    }
                };
            }
            squadSchedule = MapTimer.getInstance().schedule(run, 300000); //5 mins
        }
    }

    public final MapleSquad getSquadByMap() {
        MapleSquadType zz = null;
        switch (mapid) {
            case 105100400:
            case 105100300:
                zz = MapleSquadType.bossbalrog;
                break;
            case 280030000:
                zz = MapleSquadType.zak;
                break;
            case 280030001:
                zz = MapleSquadType.chaoszak;
                break;
            case 240060200:
                zz = MapleSquadType.horntail;
                break;
            case 240060201:
                zz = MapleSquadType.chaosht;
                break;
            case 270050100:
                zz = MapleSquadType.pinkbean;
                break;
            case 802000111:
                zz = MapleSquadType.nmm_squad;
                break;
            case 802000211:
                zz = MapleSquadType.vergamot;
                break;
            case 802000311:
                zz = MapleSquadType.tokyo_2095;
                break;
            case 802000411:
                zz = MapleSquadType.dunas;
                break;
            case 802000611:
                zz = MapleSquadType.nibergen_squad;
                break;
            case 802000711:
                zz = MapleSquadType.dunas2;
                break;
            case 802000801:
            case 802000802:
            case 802000803:
                zz = MapleSquadType.core_blaze;
                break;
            case 802000821:
            case 802000823:
                zz = MapleSquadType.aufheben;
                break;
            case 211070100:
            case 211070101:
            case 211070110:
                zz = MapleSquadType.vonleon;
                break;
            case 551030200:
                zz = MapleSquadType.scartar;
                break;
            case 271040100:
                zz = MapleSquadType.cygnus;
                break;
            default:
                return null;
        }
        return ChannelServer.getInstance(channel).getMapleSquad(zz);
    }

    public final MapleSquad getSquadBegin() {
        if (squad != null) {
            return ChannelServer.getInstance(channel).getMapleSquad(squad);
        }
        return null;
    }

    public final EventManager getEMByMap() {
        String em = null;
        switch (mapid) {
            case 105100400:
                em = "BossBalrog_EASY";
                break;
            case 105100300:
                em = "BossBalrog_NORMAL";
                break;
            case 280030000:
                em = "ZakumBattle";
                break;
            case 240060200:
                em = "HorntailBattle";
                break;
            case 280030001:
                em = "ChaosZakum";
                break;
            case 240060201:
                em = "ChaosHorntail";
                break;
            case 270050100:
                em = "PinkBeanBattle";
                break;
            case 802000111:
                em = "NamelessMagicMonster";
                break;
            case 802000211:
                em = "Vergamot";
                break;
            case 802000311:
                em = "2095_tokyo";
                break;
            case 802000411:
                em = "Dunas";
                break;
            case 802000611:
                em = "Nibergen";
                break;
            case 802000711:
                em = "Dunas2";
                break;
            case 802000801:
            case 802000802:
            case 802000803:
                em = "CoreBlaze";
                break;
            case 802000821:
            case 802000823:
                em = "Aufhaven";
                break;
            case 211070100:
            case 211070101:
            case 211070110:
                em = "VonLeonBattle";
                break;
            case 551030200:
                em = "ScarTarBattle";
                break;
            case 271040100:
                em = "CygnusBattle";
                break;
            case 262030300:
                em = "HillaBattle";
                break;
            case 262031300:
                em = "DarkHillaBattle";
                break;
            case 272020110:
            case 272030400:
                em = "ArkariumBattle";
                break;
            case 955000100:
            case 955000200:
            case 955000300:
                em = "AswanOffSeason";
                break;
            /*
             *
             */ case 280030100:
                /*
                 * 2829
                 */ em = "ZakumBattle";
                /*
                 * 2830
                 */ break;
            /*
             *
             */ case 272020200:
                /*
                 * 2862
                 */ em = "Akayile";
                /*
                 * 2863
                 */ break;
            /*
             *
             */ case 689013000:
                /*
                 * 2888
                 */ em = "PinkZakum";
                /*
                 * 2889
                 */ break;
            /*
             *
             */ case 703200400:
                /*
                 * 2891
                 */ em = "0AllBoss";
                /*
                 * 2892
                 */ break;
            //case 689010000:
            //    em = "PinkZakumEntrance";
            //    break;
            //case 689013000:
            //    em = "PinkZakumFight";
            //    break;
            default:
                return null;
        }
        return ChannelServer.getInstance(channel).getEventSM().getEventManager(em);
    }

    public final void removePlayer(final MapleCharacter chr) {
        //log.warn("[dc] [level2] Player {} leaves map {}", new Object[] { chr.getName(), mapid });

        if (everlast) {
            returnEverLastItem(chr);
        }

        charactersLock.writeLock().lock();
        try {
            characters.remove(chr);
        } finally {
            charactersLock.writeLock().unlock();
        }
        removeMapObject(chr);
        chr.checkFollow();
        broadcastMessage(MaplePacketCreator.removePlayerFromMap(chr.getId()));
        if (!chr.isClone()) {
            final List<MapleMonster> update = new ArrayList<MapleMonster>();
            final Iterator<MapleMonster> controlled = chr.getControlled().iterator();

            while (controlled.hasNext()) {
                MapleMonster monster = controlled.next();
                if (monster != null) {
                    monster.setController(null);
                    monster.setControllerHasAggro(false);
                    monster.setControllerKnowsAboutAggro(false);
                    controlled.remove();
                    update.add(monster);
                }
            }
            for (MapleMonster mons : update) {
                updateMonsterController(mons);
            }
            chr.leaveMap();
            checkStates(chr.getName());
            if (mapid == 109020001) {
                chr.canTalk(true);
            }
            for (final WeakReference<MapleCharacter> chrz : chr.getClones()) {
                if (chrz.get() != null) {
                    removePlayer(chrz.get());
                }
            }
        }
        chr.cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
        chr.cancelEffectFromBuffStat(MapleBuffStat.REAPER);
        boolean cancelSummons = false;
        for (final MapleSummon summon : chr.getSummons().values()) {
            if (summon.getMovementType() == SummonMovementType.STATIONARY || summon.getMovementType() == SummonMovementType.CIRCLE_STATIONARY || summon.getMovementType() == SummonMovementType.WALK_STATIONARY) {
                cancelSummons = true;
            } else {
                summon.setChangedMap(true);
                removeMapObject(summon);
            }
        }
        if (cancelSummons) {
            chr.cancelEffectFromBuffStat(MapleBuffStat.SUMMON);

        }
        if (chr.getDragon() != null) {
            removeMapObject(chr.getDragon());
        }
    }

    public List<MapleMapObject> getAllPlayers() {
        return getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.PLAYER));
    }

    public final void broadcastMessage(final byte[] packet) {
        broadcastMessage(null, packet, Double.POSITIVE_INFINITY, null);
    }

    public final void broadcastMessage(final MapleCharacter source, final byte[] packet, final boolean repeatToSource) {
        broadcastMessage(repeatToSource ? null : source, packet, Double.POSITIVE_INFINITY, source.getPosition());
    }

    /*
     * public void broadcastMessage(MapleCharacter source, MaplePacket packet,
     * boolean repeatToSource, boolean ranged) { broadcastMessage(repeatToSource
     * ? null : source, packet, ranged ? MapleCharacter.MAX_VIEW_RANGE_SQ :
     * Double.POSITIVE_INFINITY, source.getPosition()); }
     */
    public final void broadcastMessage(final byte[] packet, final Point rangedFrom) {
        broadcastMessage(null, packet, GameConstants.maxViewRangeSq(), rangedFrom);
    }

    public final void broadcastMessage(final MapleCharacter source, final byte[] packet, final Point rangedFrom) {
        broadcastMessage(source, packet, GameConstants.maxViewRangeSq(), rangedFrom);
    }

    //怪物死亡动画
    private void broadcastMessage(final MapleCharacter source, final byte[] packet, final double rangeSq, final Point rangedFrom) {
        charactersLock.readLock().lock();
        try {
            for (MapleCharacter chr : characters) {
                if (chr != source) {
                    if (rangeSq < Double.POSITIVE_INFINITY) {
                        if (rangedFrom.distanceSq(chr.getPosition()) <= rangeSq) {
                            chr.getClient().sendPacket(packet);
                        }
                    } else {
                        chr.getClient().sendPacket(packet);
                    }
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
    }

    /*
   
            new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 3);
                    charactersLock.readLock().lock();

                } catch (InterruptedException e) {
                }
            }
        }.start();
     */
    private void sendObjectPlacement(final MapleCharacter c) {
        if (c == null || c.isClone()) {
            return;
        }
        for (final MapleMapObject o : this.getAllMonstersThreadsafe()) {
            updateMonsterController((MapleMonster) o);
        }
        for (final MapleMapObject o : getMapObjectsInRange(c.getPosition(), GameConstants.maxViewRangeSq(), GameConstants.rangedMapobjectTypes)) {
            if (o.getType() == MapleMapObjectType.REACTOR) {
                if (!((MapleReactor) o).isAlive()) {
                    continue;
                }
            }
            o.sendSpawnData(c.getClient());
            c.addVisibleMapObject(o);
        }
    }

    public final List<MapleMapObject> getMapObjectsInRange(final Point from, final double rangeSq) {
        final List<MapleMapObject> ret = new ArrayList<>();
        for (MapleMapObjectType type : MapleMapObjectType.values()) {
            mapobjectlocks.get(type).readLock().lock();
            try {
                Iterator<MapleMapObject> itr = mapobjects.get(type).values().iterator();
                while (itr.hasNext()) {
                    MapleMapObject mmo = itr.next();
                    if (from.distanceSq(mmo.getPosition()) <= rangeSq) {
                        ret.add(mmo);
                    }
                }
            } finally {
                mapobjectlocks.get(type).readLock().unlock();
            }
        }
        return ret;
    }

    public List<MapleMapObject> getItemsInRange(Point from, double rangeSq) {
        return getMapObjectsInRange(from, rangeSq, Arrays.asList(MapleMapObjectType.ITEM));
    }

    public final List<MapleMapObject> getMapObjectsInRange(final Point from, final double rangeSq, final List<MapleMapObjectType> MapObject_types) {
        final List<MapleMapObject> ret = new ArrayList<MapleMapObject>();
        for (MapleMapObjectType type : MapObject_types) {
            mapobjectlocks.get(type).readLock().lock();
            try {
                Iterator<MapleMapObject> itr = mapobjects.get(type).values().iterator();
                while (itr.hasNext()) {
                    MapleMapObject mmo = itr.next();
                    if (from.distanceSq(mmo.getPosition()) <= rangeSq) {
                        ret.add(mmo);
                    }
                }
            } finally {
                mapobjectlocks.get(type).readLock().unlock();
            }
        }
        return ret;
    }

    public final List<MapleMapObject> getMapObjectsInRect(final Rectangle box, final List<MapleMapObjectType> MapObject_types) {
        final List<MapleMapObject> ret = new ArrayList<MapleMapObject>();
        for (MapleMapObjectType type : MapObject_types) {
            mapobjectlocks.get(type).readLock().lock();
            try {
                Iterator<MapleMapObject> itr = mapobjects.get(type).values().iterator();
                while (itr.hasNext()) {
                    MapleMapObject mmo = itr.next();
                    if (box.contains(mmo.getPosition())) {
                        ret.add(mmo);
                    }
                }
            } finally {
                mapobjectlocks.get(type).readLock().unlock();
            }
        }
        return ret;
    }

    public final List<MapleCharacter> getPlayersInRectAndInList(final Rectangle box, final List<MapleCharacter> chrList) {
        final List<MapleCharacter> character = new LinkedList<MapleCharacter>();

        charactersLock.readLock().lock();
        try {
            final Iterator<MapleCharacter> ltr = characters.iterator();
            MapleCharacter a;
            while (ltr.hasNext()) {
                a = ltr.next();
                if (chrList.contains(a) && box.contains(a.getPosition())) {
                    character.add(a);
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
        return character;
    }

    public final void addPortal(final MaplePortal myPortal) {
        portals.put(myPortal.getId(), myPortal);
    }

    public final MaplePortal getPortal(final String portalname) {
        for (final MaplePortal port : portals.values()) {
            if (port.getName().equals(portalname)) {
                return port;
            }
        }
        return null;
    }

    public final MaplePortal getPortal(final int portalid) {
        return portals.get(portalid);
    }

    public final void resetPortals() {
        for (final MaplePortal port : portals.values()) {
            port.setPortalState(true);
        }
    }

    public final void setFootholds(final MapleFootholdTree footholds) {
        this.footholds = footholds;
    }

    public final MapleFootholdTree getFootholds() {
        return footholds;
    }

    public final void loadMonsterRate(final boolean first) {
        final int spawnSize = monsterSpawn.size();
        maxRegularSpawn = Math.round(spawnSize * monsterRate);
        if (maxRegularSpawn < 2) {
            maxRegularSpawn = 2;
        } else if (maxRegularSpawn > spawnSize) {
            maxRegularSpawn = spawnSize - (spawnSize / 15);
        }
        if (fixedMob > 0) {
            maxRegularSpawn = fixedMob;
        }
        Collection<Spawns> newSpawn = new LinkedList<Spawns>();
        Collection<Spawns> newBossSpawn = new LinkedList<Spawns>();
        for (final Spawns s : monsterSpawn) {
            if (s.getCarnivalTeam() >= 2) {
                continue; // Remove carnival spawned mobs
            }
            if (s.getMonster().getStats().isBoss()) {
                newBossSpawn.add(s);
            } else {
                newSpawn.add(s);
            }
        }
        monsterSpawn.clear();
        monsterSpawn.addAll(newBossSpawn);
        monsterSpawn.addAll(newSpawn);

        if (first && spawnSize > 0) {
            lastSpawnTime = System.currentTimeMillis();
            if (GameConstants.isForceRespawn(mapid)) {
                createMobInterval = 15000;
            }
        }
    }

    public final SpawnPoint addMonsterSpawn(final MapleMonster monster, final int mobTime, final byte carnivalTeam, final String msg) {
        final Point newpos = calcPointBelow(monster.getPosition());
        newpos.y -= 1;
        final SpawnPoint sp = new SpawnPoint(monster, newpos, mobTime, carnivalTeam, msg);
        if (carnivalTeam > -1) {
            monsterSpawn.add(0, sp); //at the beginning
        } else {
            monsterSpawn.add(sp);
        }
        return sp;
    }

    public final void addAreaMonsterSpawn(final MapleMonster monster, Point pos1, Point pos2, Point pos3, final int mobTime, final String msg) {
        pos1 = calcPointBelow(pos1);
        pos2 = calcPointBelow(pos2);
        pos3 = calcPointBelow(pos3);
        if (pos1 != null) {
            pos1.y -= 1;
        }
        if (pos2 != null) {
            pos2.y -= 1;
        }
        if (pos3 != null) {
            pos3.y -= 1;
        }
        if (pos1 == null && pos2 == null && pos3 == null) {
            System.out.println("WARNING: mapid " + mapid + ", monster " + monster.getId() + " could not be spawned.");

            return;
        } else if (pos1 != null) {
            if (pos2 == null) {
                pos2 = new Point(pos1);
            }
            if (pos3 == null) {
                pos3 = new Point(pos1);
            }
        } else if (pos2 != null) {
            if (pos1 == null) {
                pos1 = new Point(pos2);
            }
            if (pos3 == null) {
                pos3 = new Point(pos2);
            }
        } else if (pos3 != null) {
            if (pos1 == null) {
                pos1 = new Point(pos3);
            }
            if (pos2 == null) {
                pos2 = new Point(pos3);
            }
        }
        monsterSpawn.add(new SpawnPointAreaBoss(monster, pos1, pos2, pos3, mobTime, msg));
    }

    public final List<MapleCharacter> getCharacters() {
        return getCharactersThreadsafe();
    }

    public final List<MapleCharacter> getCharactersThreadsafe() {
        final List<MapleCharacter> chars = new ArrayList<MapleCharacter>();

        charactersLock.readLock().lock();
        try {
            for (MapleCharacter mc : characters) {
                chars.add(mc);
            }
        } finally {
            charactersLock.readLock().unlock();
        }
        return chars;
    }

    public final MapleCharacter getCharacterById_InMap(final int id) {
        return getCharacterById(id);
    }

    public final MapleCharacter getCharacterById(final int id) {
        charactersLock.readLock().lock();
        try {
            for (MapleCharacter mc : characters) {
                if (mc.getId() == id) {
                    return mc;
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
        return null;
    }

    public final void updateMapObjectVisibility(final MapleCharacter chr, final MapleMapObject mo) {
        if (chr == null || chr.isClone()) {
            return;
        }
        if (!chr.isMapObjectVisible(mo)) { // monster entered view range
            if (mo.getType() == MapleMapObjectType.SUMMON || mo.getPosition().distanceSq(chr.getPosition()) <= GameConstants.maxViewRangeSq()) {
                chr.addVisibleMapObject(mo);
                mo.sendSpawnData(chr.getClient());
            }
        } else if (mo.getType() != MapleMapObjectType.SUMMON && mo.getPosition().distanceSq(chr.getPosition()) > GameConstants.maxViewRangeSq()) {
            chr.removeVisibleMapObject(mo);
            mo.sendDestroyData(chr.getClient());
        }
    }

    public void moveMonster(MapleMonster monster, Point reportedPos) {
        monster.setPosition(reportedPos);

        charactersLock.readLock().lock();
        try {
            for (MapleCharacter mc : characters) {
                updateMapObjectVisibility(mc, monster);
            }
        } finally {
            charactersLock.readLock().unlock();
        }
    }

    public void movePlayer(final MapleCharacter player, final Point newPosition) {
        player.setPosition(newPosition);
        if (!player.isClone()) {
            try {
                Collection<MapleMapObject> visibleObjects = player.getAndWriteLockVisibleMapObjects();
                ArrayList<MapleMapObject> copy = new ArrayList<MapleMapObject>(visibleObjects);
                Iterator<MapleMapObject> itr = copy.iterator();
                while (itr.hasNext()) {
                    MapleMapObject mo = itr.next();
                    if (mo != null && getMapObject(mo.getObjectId(), mo.getType()) == mo) {
                        updateMapObjectVisibility(player, mo);
                    } else if (mo != null) {
                        visibleObjects.remove(mo);
                    }
                }
                for (MapleMapObject mo : getMapObjectsInRange(player.getPosition(), GameConstants.maxViewRangeSq())) {
                    if (mo != null && !player.isMapObjectVisible(mo)) {
                        mo.sendSpawnData(player.getClient());
                        visibleObjects.add(mo);
                    }
                }
            } finally {
                player.unlockWriteVisibleMapObjects();
            }
        }
    }

    public MaplePortal findClosestSpawnpoint(Point from) {
        MaplePortal closest = null;
        double distance, shortestDistance = Double.POSITIVE_INFINITY;
        for (MaplePortal portal : portals.values()) {
            distance = portal.getPosition().distanceSq(from);
            if (portal.getType() >= 0 && portal.getType() <= 2 && distance < shortestDistance && portal.getTargetMapId() == 999999999) {
                closest = portal;
                shortestDistance = distance;
            }
        }
        return closest;
    }

    public String spawnDebug() {
        StringBuilder sb = new StringBuilder("Mapobjects in map : ");
        sb.append(this.getMapObjectSize());
        sb.append(" spawnedMonstersOnMap: ");
        sb.append(spawnedMonstersOnMap);
        sb.append(" spawnpoints: ");
        sb.append(monsterSpawn.size());
        sb.append(" maxRegularSpawn: ");
        sb.append(maxRegularSpawn);
        sb.append(" actual monsters: ");
        sb.append(getNumMonsters());

        return sb.toString();
    }

    public int characterSize() {
        return characters.size();
    }

    public final int getMapObjectSize() {
        return mapobjects.size() + getCharactersSize() - characters.size();
    }

    public final int getCharactersSize() {
        int ret = 0;
        charactersLock.readLock().lock();
        try {
            final Iterator<MapleCharacter> ltr = characters.iterator();
            MapleCharacter chr;
            while (ltr.hasNext()) {
                chr = ltr.next();
                if (!chr.isClone()) {
                    ret++;
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
        return ret;
    }

    public Collection<MaplePortal> getPortals() {
        return Collections.unmodifiableCollection(portals.values());
    }

    public int getSpawnedMonstersOnMap() {
        return spawnedMonstersOnMap.get();
    }

    //地图放置物品存在时间
    public void spawnLove(final MapleLove love) {
        addMapObject(love);
        broadcastMessage(love.makeSpawnData());
        final MapTimer tMan = MapTimer.getInstance();
        tMan.schedule(new Runnable() {

            @Override
            public void run() {
                removeMapObject(love);
                broadcastMessage(love.makeDestroyData());
            }
        }, 100 * 60 * 60 * 1000);
    }

    public void broadcastMessage(String songName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    private static class slea {

        private static int readInt() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public slea() {
        }
    }

    private class ActivateItemReactor implements Runnable {

        private final MapleMapItem mapitem;
        private final MapleReactor reactor;
        private final MapleClient c;

        public ActivateItemReactor(MapleMapItem mapitem, MapleReactor reactor, MapleClient c) {
            this.mapitem = mapitem;
            this.reactor = reactor;
            this.c = c;
        }

        @Override
        public void run() {
            if (mapitem != null && mapitem == getMapObject(mapitem.getObjectId(), mapitem.getType())) {
                if (mapitem.isPickedUp()) {
                    reactor.setTimerActive(false);
                    return;
                }
                mapitem.expire(MapleMap.this);
                reactor.hitReactor(c);
                reactor.setTimerActive(false);

                if (reactor.getDelay() > 0) {
                    MapTimer.getInstance().schedule(new Runnable() {

                        @Override
                        public void run() {
                            reactor.forceHitReactor((byte) 0);
                        }
                    }, reactor.getDelay());
                }
            } else {
                reactor.setTimerActive(false);
            }
        }
    }

    public void respawn(final boolean force) {
        lastSpawnTime = System.currentTimeMillis();
        if (force) { //cpq quick hack
            final int numShouldSpawn = monsterSpawn.size() - spawnedMonstersOnMap.get();

            if (numShouldSpawn > 0) {
                int spawned = 0;

                for (Spawns spawnPoint : monsterSpawn) {
                    spawnPoint.spawnMonster(this);
                    spawned++;
                    if (spawned >= numShouldSpawn) {
                        break;
                    }
                }
            }
        } else {
            final int numShouldSpawn = maxRegularSpawn - spawnedMonstersOnMap.get();
            if (numShouldSpawn > 0) {
                int spawned = 0;

                final List<Spawns> randomSpawn = new ArrayList<Spawns>(monsterSpawn);
                Collections.shuffle(randomSpawn);

                for (Spawns spawnPoint : randomSpawn) {
                    if ((!isSpawns) && (spawnPoint.getMobTime() > 0)) {
                        continue;
                    }
                    if (spawnPoint.shouldSpawn() || GameConstants.isForceRespawn(mapid)) {
                        spawnPoint.spawnMonster(this);
                        spawned++;
                    }
                    if (spawned >= numShouldSpawn) {
                        break;

                    }
                }
            }
        }
    }

    private interface DelayedPacketCreation {

        void sendPackets(MapleClient c);
    }

    private interface SpawnCondition {

        boolean canSpawn(MapleCharacter chr);
    }

    public String getSnowballPortal() {
        int[] teamss = new int[2];
        for (MapleCharacter chr : getCharactersThreadsafe()) {
            if (chr.getPosition().y > -80) {
                teamss[0]++;
            } else {
                teamss[1]++;
            }
        }
        if (teamss[0] > teamss[1]) {
            return "st01";
        } else {
            return "st00";
        }
    }

    public boolean isDisconnected(int id) {
        return dced.contains(Integer.valueOf(id));
    }

    public void addDisconnected(int id) {
        dced.add(Integer.valueOf(id));
    }

    public void resetDisconnected() {
        dced.clear();
    }

    public void startSpeedRun() {
        final MapleSquad squad = getSquadByMap();
        if (squad != null) {
            for (MapleCharacter chr : getCharactersThreadsafe()) {
                if (chr.getName().equals(squad.getLeaderName())) {
                    startSpeedRun(chr.getName());
                    return;
                }
            }
        }
    }

    public void startSpeedRun(String leader) {
        speedRunStart = System.currentTimeMillis();
        speedRunLeader = leader;
    }

    public void endSpeedRun() {
        speedRunStart = 0;
        speedRunLeader = "";
    }

    public void getRankAndAdd(String leader, String time, SpeedRunType type, long timz, Collection<String> squad) {
        try {
            //Pair<String, Map<Integer, String>>
            StringBuilder rett = new StringBuilder();
            if (squad != null) {
                for (String chr : squad) {
                    rett.append(chr);
                    rett.append(",");
                }
            }
            String z = rett.toString();
            if (squad != null) {
                z = z.substring(0, z.length() - 1);
            }
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO speedruns(`type`, `leader`, `timestring`, `time`, `members`) VALUES (?,?,?,?,?)");
            ps.setString(1, type.name());
            ps.setString(2, leader);
            ps.setString(3, time);
            ps.setLong(4, timz);
            ps.setString(5, z);
            ps.executeUpdate();
            ps.close();

            if (SpeedRunner.getInstance().getSpeedRunData(type) == null) { //great, we just add it
                SpeedRunner.getInstance().addSpeedRunData(type, SpeedRunner.getInstance().addSpeedRunData(new StringBuilder("#rThese are the speedrun times for " + type + ".#k\r\n\r\n"), new HashMap<Integer, String>(), z, leader, 1, time));
            } else {
                //i wish we had a way to get the rank
                //TODO revamp
                SpeedRunner.getInstance().removeSpeedRunData(type);
                SpeedRunner.getInstance().loadSpeedRunData(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getSpeedRunStart() {
        return speedRunStart;
    }

    public final void disconnectAll() {
        for (MapleCharacter chr : getCharactersThreadsafe()) {
            if (!chr.isGM()) {
                chr.getClient().disconnect(true, false);
                chr.getClient().getSession().close();
            }
        }
    }

    public final void 断玩家(int c) {
        for (MapleCharacter chr : getCharactersThreadsafe()) {
            if (!chr.isGM()) {
                chr.getClient().disconnect(true, false);
                chr.getClient().getSession().close();
            }
        }
    }

    public List<MapleNPC> getAllNPCs() {
        return getAllNPCsThreadsafe();
    }

    public List<MapleNPC> getAllNPCsThreadsafe() {
        ArrayList<MapleNPC> ret = new ArrayList<MapleNPC>();
        mapobjectlocks.get(MapleMapObjectType.NPC).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.NPC).values()) {
                ret.add((MapleNPC) mmo);
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.NPC).readLock().unlock();
        }
        return ret;
    }

    public final void resetNPCs() {
        List<MapleNPC> npcs = getAllNPCsThreadsafe();
        for (MapleNPC npc : npcs) {
            if (npc.isCustom()) {
                broadcastMessage(MaplePacketCreator.spawnNPC(npc, false));
                removeMapObject(npc);
            }
        }
    }

    public final void resetFully() {
        resetFully(true);
    }

    public final void 清怪() {
        killAllMonsters(true);
    }

    public final void resetFully(final boolean respawn) {
        killAllMonsters(false);
        reloadReactors();
        removeDrops();
        resetNPCs();
        resetSpawns();
        resetDisconnected();
        endSpeedRun();
        cancelSquadSchedule();
        resetPortals();
        environment.clear();
        if (respawn) {
            respawn(true);
        }
    }

    public final void cancelSquadSchedule() {
        squadTimer = false;
        if (squadSchedule != null) {
            squadSchedule.cancel(false);
            squadSchedule = null;
        }
    }

    public final void removeDrops() {
        List<MapleMapItem> items = this.getAllItemsThreadsafe();
        for (MapleMapItem i : items) {
            i.expire(this);
        }
    }

    public final void resetAllSpawnPoint(int mobid, int mobTime) {
        Collection<Spawns> sss = new LinkedList<Spawns>(monsterSpawn);
        resetFully();
        monsterSpawn.clear();
        for (Spawns s : sss) {
            MapleMonster newMons = MapleLifeFactory.getMonster(mobid);
            MapleMonster oldMons = s.getMonster();
            newMons.setCy(oldMons.getCy());
            newMons.setF(oldMons.getF());
            newMons.setFh(oldMons.getFh());
            newMons.setRx0(oldMons.getRx0());
            newMons.setRx1(oldMons.getRx1());
            newMons.setPosition(new Point(oldMons.getPosition()));
            newMons.setHide(oldMons.isHidden());
            addMonsterSpawn(newMons, mobTime, (byte) -1, null);
        }
        loadMonsterRate(true);
    }

    public final void resetSpawns() {
        boolean changed = false;
        Iterator<Spawns> sss = monsterSpawn.iterator();
        while (sss.hasNext()) {
            if (sss.next().getCarnivalId() > -1) {
                sss.remove();
                changed = true;
            }
        }
        setSpawns(true);
        if (changed) {
            loadMonsterRate(true);
        }
    }

    public final boolean makeCarnivalSpawn(final int team, final MapleMonster newMons, final int num) {
        MonsterPoint ret = null;
        for (MonsterPoint mp : nodes.getMonsterPoints()) {
            if (mp.team == team || mp.team == -1) {
                final Point newpos = calcPointBelow(new Point(mp.x, mp.y));
                newpos.y -= 1;
                boolean found = false;
                for (Spawns s : monsterSpawn) {
                    if (s.getCarnivalId() > -1 && (mp.team == -1 || s.getCarnivalTeam() == mp.team) && s.getPosition().x == newpos.x && s.getPosition().y == newpos.y) {
                        found = true;
                        break; //this point has already been used.
                    }
                }
                if (!found) {
                    ret = mp; //this point is safe for use.
                    break;
                }
                if (!found) {
                    ret = mp;
                    break;
                }
            }
        }
        if (ret != null) {
            newMons.setCy(ret.cy);
            newMons.setF(0); //always.
            newMons.setFh(ret.fh);
            newMons.setRx0(ret.x + 50);
            newMons.setRx1(ret.x - 50); //does this matter
            newMons.setPosition(new Point(ret.x, ret.y));
            newMons.setHide(false);
            final SpawnPoint sp = addMonsterSpawn(newMons, 1, (byte) team, null);
            sp.setCarnival(num);
        }
        return ret != null;
    }

    public final boolean makeCarnivalReactor(final int team, final int num) {
        final MapleReactor old = getReactorByName(team + "" + num);
        if (old != null && old.getState() < 5) { //already exists
            return false;
        }
        Point guardz = null;
        final List<MapleReactor> react = getAllReactorsThreadsafe();
        for (Pair<Point, Integer> guard : nodes.getGuardians()) {
            if (guard.right == team || guard.right == -1) {
                boolean found = false;
                for (MapleReactor r : react) {
                    if (r.getPosition().x == guard.left.x && r.getPosition().y == guard.left.y && r.getState() < 5) {
                        found = true;
                        break; //already used
                    }
                }
                if (!found) {
                    guardz = guard.left; //this point is safe for use.
                    break;
                }
            }
        }
        if (guardz != null) {
            final MapleReactorStats stats = MapleReactorFactory.getReactor(9980000 + team);
            final MapleReactor my = new MapleReactor(stats, 9980000 + team);
            stats.setFacingDirection((byte) 0); //always
            my.setPosition(guardz);
            my.setState((byte) 1);
            my.setDelay(0);
            my.setName(team + "" + num); //lol
            //with num. -> guardians in factory
            spawnReactor(my);
            final MCSkill skil = MapleCarnivalFactory.getInstance().getGuardian(num);
            for (MapleMonster mons : getAllMonstersThreadsafe()) {
                if (mons.getCarnivalTeam() == team) {
                    skil.getSkill().applyEffect(null, mons, false);
                }
            }
        }
        return guardz != null;
    }

    public final void blockAllPortal() {
        for (MaplePortal p : portals.values()) {
            p.setPortalState(false);
        }
    }

    public boolean getAndSwitchTeam() {
        return getCharactersSize() % 2 != 0;
    }

    public void setSquad(MapleSquadType s) {
        this.squad = s;

    }

    /**
     * ><判断频道>
     */
    public int getChannel() {
        return channel;
    }

    /**
     * <判断地图设置消耗冷却，秒>
     */
    public int getConsumeItemCoolTime() {
        return consumeItemCoolTime;
    }

    /**
     * <给地图设置消耗冷却，秒>
     */
    public void setConsumeItemCoolTime(int ciit) {
        this.consumeItemCoolTime = ciit;
    }

    /**
     * <给地图设置给地图设置天气>
     */
    public void setPermanentWeather(int pw) {
        this.permanentWeather = pw;
    }

    /**
     * <判断地图设置给地图设置天气>
     */
    public int getPermanentWeather() {
        return permanentWeather;
    }

    public void checkStates(final String chr) {
        final MapleSquad sqd = getSquadByMap();
        final EventManager em = getEMByMap();
        final int size = getCharactersSize();
        if (sqd != null) {
            sqd.removeMember(chr);
            if (em != null) {
                if (sqd.getLeaderName().equals(chr)) {
                    em.setProperty("leader", "false");
                }
                if (chr.equals("") || size == 0) {
                    sqd.clear();
                    em.setProperty("state", "0");
                    em.setProperty("leader", "true");
                    cancelSquadSchedule();
                }
            }
        }
        if (em != null && em.getProperty("state") != null) {
            if (size == 0) {
                em.setProperty("state", "0");
                if (em.getProperty("leader") != null) {
                    em.setProperty("leader", "true");
                }
            }
        }
        if (speedRunStart > 0 && speedRunLeader.equalsIgnoreCase(chr)) {
            if (size > 0) {
                broadcastMessage(MaplePacketCreator.serverNotice(5, "队长不在地图上！你的挑战失败"));
            }
            endSpeedRun();
        }
    }

    public void setNodes(final MapleNodes mn) {
        this.nodes = mn;
    }

    public final List<MaplePlatform> getPlatforms() {
        return nodes.getPlatforms();
    }

    public Collection<MapleNodeInfo> getNodes() {
        return nodes.getNodes();
    }

    public MapleNodeInfo getNode(final int index) {
        return nodes.getNode(index);
    }

    public final List<Rectangle> getAreas() {
        return nodes.getAreas();
    }

    public final Rectangle getArea(final int index) {
        return nodes.getArea(index);
    }

    public final void changeEnvironment(final String ms, final int type) {
        broadcastMessage(MaplePacketCreator.environmentChange(ms, type));
    }

    public final void toggleEnvironment(final String ms) {
        if (environment.containsKey(ms)) {
            moveEnvironment(ms, environment.get(ms) == 1 ? 2 : 1);
        } else {
            moveEnvironment(ms, 1);
        }
    }

    public final void moveEnvironment(final String ms, final int type) {
        //  broadcastMessage(MaplePacketCreator.environmentMove(ms, type));
        environment.put(ms, type);
    }

    public final Map<String, Integer> getEnvironment() {
        return environment;
    }

    public final int getNumPlayersInArea(final int index) {
        int ret = 0;

        charactersLock.readLock().lock();
        try {
            final Iterator<MapleCharacter> ltr = characters.iterator();
            MapleCharacter a;
            while (ltr.hasNext()) {
                if (getArea(index).contains(ltr.next().getPosition())) {
                    ret++;
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
        return ret;
    }

    public void broadcastGMMessage(MapleCharacter source, byte[] packet, boolean repeatToSource) {
        broadcastGMMessage(repeatToSource ? null : source, packet, Double.POSITIVE_INFINITY, source.getPosition());
    }

    private void broadcastGMMessage(MapleCharacter source, byte[] packet, double rangeSq, Point rangedFrom) {
        charactersLock.readLock().lock();
        try {
            if (source == null) {
                for (MapleCharacter chr : characters) {
                    if (chr.isStaff()) {
                        chr.getClient().sendPacket(packet);
                    }
                }
            } else {
                for (MapleCharacter chr : characters) {
                    if (chr != source && (chr.getGMLevel() >= source.getGMLevel())) {
                        chr.getClient().sendPacket(packet);
                    }
                }
            }
        } finally {
            charactersLock.readLock().unlock();
        }
    }

    public final List<Pair<Integer, Integer>> getMobsToSpawn() {
        return nodes.getMobsToSpawn();
    }

    public final List<Integer> getSkillIds() {
        return nodes.getSkillIds();
    }

    public final boolean canSpawn() {
        return lastSpawnTime > 0 && isSpawns && lastSpawnTime + createMobInterval < System.currentTimeMillis();
    }

    public final boolean canHurt() {
        if (lastHurtTime > 0 && lastHurtTime + decHPInterval < System.currentTimeMillis()) {
            lastHurtTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    /**
     * <判断地图怪物>
     */
    public List<Integer> getAllUniqueMonsters() {
        ArrayList ret = new ArrayList();
        this.mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.MONSTER).values()) {
                int theId = ((MapleMonster) mmo).getId();
                if (!ret.contains(Integer.valueOf(theId))) {
                    ret.add(Integer.valueOf(theId));
                }
            }
        } finally {
            this.mapobjectlocks.get(MapleMapObjectType.MONSTER).readLock().unlock();
        }
        return ret;
    }

    public int getNumPlayersItemsInArea(int index) {
        return getNumPlayersItemsInRect(getArea(index));
    }

    public final int getNumPlayersItemsInRect(final Rectangle rect) {
        int ret = getNumPlayersInRect(rect);

        mapobjectlocks.get(MapleMapObjectType.ITEM).readLock().lock();
        try {
            for (MapleMapObject mmo : mapobjects.get(MapleMapObjectType.ITEM).values()) {
                if (rect.contains(mmo.getPosition())) {
                    ret++;
                }
            }
        } finally {
            mapobjectlocks.get(MapleMapObjectType.ITEM).readLock().unlock();
        }
        return ret;
    }

    public int getNumPlayersInRect(Rectangle rect) {
        int ret = 0;
        this.charactersLock.readLock().lock();
        try {
            Iterator ltr = this.characters.iterator();

            while (ltr.hasNext()) {
                if (rect.contains(((MapleCharacter) ltr.next()).getTruePosition())) {
                    ret++;
                }
            }
        } finally {
            this.charactersLock.readLock().unlock();
        }
        return ret;
    }

    public final void resetAriantPQ(int level) {
        killAllMonsters(true);
        reloadReactors();
        removeDrops();
        resetNPCs();
        resetSpawns();
        resetDisconnected();
        endSpeedRun();
        resetPortals();
        environment.clear();
        respawn(true);
        for (MapleMonster mons : getAllMonstersThreadsafe()) {
            mons.changeLevel(level, true);
        }
        resetSpawnLevel(level);
    }

    public final void resetSpawnLevel(int level) {
        for (Spawns spawn : monsterSpawn) {
            if (spawn instanceof SpawnPoint) {
                ((SpawnPoint) spawn).setLevel(level);
            }
        }
    }

    public int hasBoat() {
        if (boat && docked) {
            return 2;
        } else if (boat) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setBoat(boolean hasBoat) {
        this.boat = hasBoat;
    }

    public void setDocked(boolean isDocked) {
        this.docked = isDocked;
    }

    /**
     * <召唤月妙>
     */
    public void spawnRabbit(int hp) {
        hp = 10000;
        int mid = 9300061;
        MapleMonster onemob = MapleLifeFactory.getMonster(mid);
        final OverrideMonsterStats overrideStats = new OverrideMonsterStats(hp, onemob.getMobMaxMp(), 0, false);
        MapleMonster mob = MapleLifeFactory.getMonster(mid);
        mob.setHp(hp);
        mob.setOverrideStats(overrideStats);
        spawnMonsterOnGroundBelow(mob, new java.awt.Point(-183, -433));
    }

    /*public void spawnRabbit(int hp) {
        int mid = 9300061;
        MapleMonster onemob = MapleLifeFactory.getMonster(mid);
        final OverrideMonsterStats overrideStats = new OverrideMonsterStats(hp, onemob.getMobMaxMp(), 0, false);
        MapleMonster mob = MapleLifeFactory.getMonster(mid);
        mob.setHp(4000);
        mob.setOverrideStats(overrideStats);
        spawnMonsterOnGroundBelow(mob, new java.awt.Point(-183, -433));
    }*/
    public void spawnRabbit2(int hp) {
        int mid = 9300061;
        MapleMonster onemob = MapleLifeFactory.getMonster(mid);
        final OverrideMonsterStats overrideStats = new OverrideMonsterStats(hp, onemob.getMobMaxMp(), 0, false);
        MapleMonster mob = MapleLifeFactory.getMonster(mid);
        mob.setHp(hp);
        mob.setOverrideStats(overrideStats);
        spawnMonsterOnGroundBelow(mob, new java.awt.Point(-183, -433));
    }

    public final void KillFk(final boolean animate) {
        List<MapleMapObject> monsters = getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
        for (MapleMapObject monstermo : monsters) {
            MapleMonster monster = (MapleMonster) monstermo;
            if (monster.getId() == 3230300 || monster.getId() == 3230301) {
                spawnedMonstersOnMap.decrementAndGet();
                monster.setHp(0);
                broadcastMessage(MobPacket.killMonster(monster.getObjectId(), animate ? 1 : 0));
                removeMapObject(monster);
                monster.killed();
            }
        }
    }

    /**
     * <判断地图怪物数量>
     */
    public final int mobCount() {
        List<MapleMapObject> mobsCount = getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
        return mobsCount.size();
    }

    /**
     * <判断地图玩家数量>
     */
    public final int playerCount() {
        List<MapleMapObject> players = getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.PLAYER));
        return players.size();
    }

    public void killMonster_2(final MapleMonster monster) { // For mobs with removeAfter
        if (monster == null) {
            return;
        }
        spawnedMonstersOnMap.decrementAndGet();
        monster.setHp(0);

        broadcastMessage(MobPacket.killMonster(monster.getObjectId(), monster.getStats().getSelfD() < 0 ? 1 : monster.getStats().getSelfD()));
        removeMapObject(monster);
        monster.killed();
    }

    public void reloadCPQ() {//嘉年华地图
        int[] maps = {980000101, 980000201, 980000301, 980000401, 980000501, 980000601};
        for (int i = 0; i < maps.length; i++) {
            int mapid = maps[i];
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.getMapFactory().destroyMap(mapid, true);
                cserv.getMapFactory().HealMap(mapid);
            }
        }
    }

    public Collection<MapleCharacter> getNearestPvpChar(Point attacker, double maxRange, double maxHeight, Collection<MapleCharacter> chr) {
        Collection<MapleCharacter> character = new LinkedList<MapleCharacter>();
        for (MapleCharacter a : characters) {
            if (chr.contains(a.getClient().getPlayer())) {
                Point attackedPlayer = a.getPosition();
                MaplePortal Port = a.getMap().findClosestSpawnpoint(a.getPosition());
                Point nearestPort = Port.getPosition();
                double safeDis = attackedPlayer.distance(nearestPort);
                double distanceX = attacker.distance(attackedPlayer.getX(), attackedPlayer.getY());
                if (MaplePvp.isLeft) {
                    if (attacker.x > attackedPlayer.x && distanceX < maxRange && distanceX > 2
                            && attackedPlayer.y >= attacker.y - maxHeight && attackedPlayer.y <= attacker.y + maxHeight && safeDis > 2) {
                        character.add(a);
                    }
                }
                if (MaplePvp.isRight) {
                    if (attacker.x < attackedPlayer.x && distanceX < maxRange && distanceX > 2
                            && attackedPlayer.y >= attacker.y - maxHeight && attackedPlayer.y <= attacker.y + maxHeight && safeDis > 2) {
                        character.add(a);
                    }
                }
            }
        }
        return character;
    }

}/*
 final int[] zakpart = {8800103, 8800104, 8800105, 8800106, 8800107,
            8800108, 8800109, 8800110};
 */
 /*                        mob1.setHp(2100000000 * 2); //guaiwu xie liang
         * if (mapEffect != null) { mapEffect.sendStartData(chr.getClient()); }
 */
 /*
         * if (timeLimit > 0 && getForcedReturnMap() != null && !chr.isClone())
         * { chr.startMapTimeLimitTask(timeLimit, getForcedReturnMap()); if
         * (ServerConstants.封包显示 || 进入地图开启显示数据) {
         * System.out.println("进入地图加载数据I"); } } if
         * (chr.getBuffedValue(MapleBuffStat.MONSTER_RIDING) != null) { if
         * (FieldLimitType.Mount.check(fieldLimit)) {
         * chr.cancelBuffStats(MapleBuffStat.MONSTER_RIDING); if
         * (ServerConstants.封包显示 || 进入地图开启显示数据) {
         * System.out.println("进入地图加载数据J"); } } }
 */
 /*
         * if (!chr.isClone()) { if (chr.getEventInstance() != null &&
         * chr.getEventInstance().isTimerStarted() && !chr.isClone()) {
         * chr.getClient().sendPacket(MaplePacketCreator.getClock((int)
         * (chr.getEventInstance().getTimeLeft() / 1000))); if
         * (ServerConstants.封包显示 || 进入地图开启显示数据) {
         * System.out.println("进入地图加载数据K"); } } if (hasClock()) { final Calendar
         * cal = Calendar.getInstance();
         * chr.getClient().sendPacket((MaplePacketCreator.getClockTime(cal.get(Calendar.HOUR_OF_DAY),
         * cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)))); if
         * (ServerConstants.封包显示 || 进入地图开启显示数据) {
         * System.out.println("进入地图加载数据L"); } } if (chr.getCarnivalParty() !=
         * null && chr.getEventInstance() != null) {
         * chr.getEventInstance().onMapLoad(chr); if (ServerConstants.封包显示 ||
         * 进入地图开启显示数据) { System.out.println("进入地图加载数据M"); } }
         * MapleEvent.mapLoad(chr, channel); if (ServerConstants.封包显示 ||
         * 进入地图开启显示数据) { System.out.println("进入地图加载数据N"); } if (getSquadBegin()
         * != null && getSquadBegin().getTimeLeft() > 0 &&
         * getSquadBegin().getStatus() == 1) {
         * chr.getClient().sendPacket(MaplePacketCreator.getClock((int)
         * (getSquadBegin().getTimeLeft() / 1000))); if (ServerConstants.封包显示 ||
         * 进入地图开启显示数据) { System.out.println("进入地图加载数据O"); } }
 */
 /*
         * if (mapid / 1000 != 105100 && mapid / 100 != 8020003 && mapid / 100
         * != 8020008) { //no boss_balrog/2095/coreblaze/auf. but coreblaze/auf
         * does AFTER final MapleSquad sqd = getSquadByMap(); //for all squads
         * if (!squadTimer && sqd != null &&
         * chr.getName().equals(sqd.getLeaderName()) && !chr.isClone()) {
         * //leader? display doShrine(false); squadTimer = true; } if
         * (ServerConstants.封包显示 || 进入地图开启显示数据) {
         * System.out.println("进入地图加载数据P"); } }
 */
 /*
         * if (getNumMonsters() > 0 && (mapid == 280030001 || mapid == 240060201
         * || mapid == 280030000 || mapid == 240060200 || mapid == 220080001 ||
         * mapid == 541020800 || mapid == 541010100)) { String music =
         * "Bgm09/TimeAttack"; switch (mapid) { case 240060200: case 240060201:
         * music = "Bgm14/HonTale"; break; case 280030000: case 280030001: music
         * = "Bgm06/FinalFight"; break; case 200090000: case 200090010: music =
         * "Bgm04/ArabPirate"; break; }
         * chr.getClient().sendPacket(MaplePacketCreator.musicChange(music));
         * if (ServerConstants.封包显示 || 进入地图开启显示数据) {
         * System.out.println("进入地图加载数据Q"); } //maybe timer too for zak/ht }
 */
 /*
         * for (final WeakReference<MapleCharacter> chrz : chr.getClones()) { if
         * (chrz.get() != null) { chrz.get().setPosition(new
         * Point(chr.getPosition())); chrz.get().setMap(this);
         * addPlayer(chrz.get()); } if (ServerConstants.封包显示 || 进入地图开启显示数据) {
         * System.out.println("进入地图加载数据R"); } }
 */
//  if (mapid == 914000000) {
///     chr.getClient().sendPacket(MaplePacketCreator.addTutorialStats());
//   if (ServerConstants.封包显示 || 进入地图开启显示数据) {
//       System.out.println("进入地图加载数据S");
//  }
// }
/*
         * else if (mapid == 105100300 && chr.getLevel() >= 91) {
         * chr.getClient().sendPacket(MaplePacketCreator.temporaryStats_Balrog(chr));
         * if (ServerConstants.封包显示 || 进入地图开启显示数据) {
         * System.out.println("进入地图加载数据T"); } }
 */
//  else if (mapid == 140090000 || mapid == 105100301 || mapid == 105100401 || mapid == 105100100) {
//  chr.getClient().sendPacket(MaplePacketCreator.temporaryStats_Reset());
//   if (ServerConstants.封包显示 || 进入地图开启显示数据) {
//      System.out.println("进入地图加载数据U");
//  }
// }
//  }
/*
                    //幸运道符
//                    if (!chr.haveItem(5220008, 0, false, false)) {
//                        MapleInventoryManipulator.removeById(chr.getClient(), GameConstants.getInventoryType(5220008), 5220008, 1, true, false);
//                        final double 概率 = Math.ceil(Math.random() * 3);
//                        if (概率 <= 1) {
//                            idrop = new Item(2022450, (byte) 0, (short) 1, (byte) 0);
//                        } else if (概率 == 2) {
//                            idrop = new Item(2022451, (byte) 0, (short) 1, (byte) 0);
//                        } else if (概率 == 3) {
//                            idrop = new Item(2022452, (byte) 0, (short) 1, (byte) 0);
//                        } else {
//                            idrop = new Item(2022450, (byte) 0, (short) 1, (byte) 0);
//                        }
//                    }
                    /*OtherSettings item = new OtherSettings();
                    String itempb[] = item.getItempb_id();
                    for (int i = 0; i < itempb.length; i++) {
                        if (de.itemId == Integer.parseInt(itempb[i])) {
                            if (server.Start.ConfigValuesMap.get("QQ机器人开关") == 0) {
                                if (MapleParty.机器人待机 > 0) {
                                    return;
                                }
                                sendMsgToQQGroup("[爆物提醒] : 恭喜玩家 [ " + chr.getName() + " ] 击杀怪物，爆出物品 [ " + ii.getName(de.itemId) + " ]");
                            }
                            String text = "[爆物提醒] : 恭喜玩家 [ " + chr.getName() + " ] 击杀怪物，爆出物品 [ " + ii.getName(de.itemId) + " ]";
                            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(9, channel, text));
                        }
                    }*/
//if (GameConstants.isEvan(chr.getJob()) && chr.getJob() >= 2200 &&
//         * chr.getBuffedValue(MapleBuffStat.MONSTER_RIDING) == null) { if
//         * (chr.getDragon() == null) { chr.makeDragon(); }
//         * spawnDragon(chr.getDragon()); if (!chr.isClone()) {
//         * updateMapObjectVisibility(chr, chr.getDragon()); } }

// if ((mapid == 10000 && chr.getJob() == 0) || (mapid == 130030000 && chr.getJob() == 1000) || (mapid == 914000000 && chr.getJob() == 2000) || (mapid == 900010000 && chr.getJob() == 2001)) {
//            chr.getClient().sendPacket(MaplePacketCreator.startMapEffect("Welcome to " + chr.getClient().getChannelServer().getServerName() + "!", 5122000, true));
//            chr.dropMessage(1, "Welcome to " + chr.getClient().getChannelServer().getServerName() + ", " + chr.getName() + " ! \r\nUse @joyce to collect your Item Of Appreciation once you're level 10! \r\nUse @help for commands. \r\nGood luck and have fun!");
//   chr.dropMessage(1, "新手技能記得在一轉之前點完 十等之後可以去自由市場找禮物盒領東西");
//            chr.dropMessage(5, "Use @joyce to collect your Item Of Appreciation once you're level 10! Use @help for commands. Good luck and have fun!");
        //    }
