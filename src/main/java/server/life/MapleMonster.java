/*
攻击怪物之后的判断？？
 */
package server.life;

import client.inventory.Equip;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import constants.GameConstants;
import client.inventory.IItem;
import client.ISkill;
import client.inventory.Item;
import client.MapleDisease;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.inventory.MapleInventoryType;
import client.MapleClient;
import handling.channel.ChannelServer;
import client.SkillFactory;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.ServerConstants;
import static gui.QQMsgServer.sendMsgToQQGroup;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import java.awt.Point;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import scripting.EventInstanceManager;
import static scripting.NPCConversationManager.角色ID取名字;
import server.AutobanManager;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.Timer.MobTimer;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.ConcurrentEnumMap;
import tools.Pair;
import tools.MaplePacketCreator;
import tools.packet.MobPacket;

public class MapleMonster extends AbstractLoadedMapleLife {

    public MapleMonsterStats stats;
    private OverrideMonsterStats ostats = null;
    private long hp, exp;
    private int mp;
    private byte venom_counter, carnivalTeam;
    private MapleMap map;
    private WeakReference<MapleMonster> sponge = new WeakReference<MapleMonster>(null);
    private int linkoid = 0, lastNode = -1, lastNodeController = -1, highestDamageChar = 0; // Just a reference for monster EXP distribution after dead
    private WeakReference<MapleCharacter> controller = new WeakReference<MapleCharacter>(null);
    private boolean fake, dropsDisabled, controllerHasAggro, controllerKnowsAboutAggro;
    private final Collection<AttackerEntry> attackers = new LinkedList<AttackerEntry>();
    private EventInstanceManager eventInstance;
    private MonsterListener listener = null;
    private byte[] reflectpack = null, nodepack = null;
    private final Map<MonsterStatus, MonsterStatusEffect> stati = new ConcurrentEnumMap<MonsterStatus, MonsterStatusEffect>(MonsterStatus.class);
    private Map<Integer, Long> usedSkills;
    private int stolen = -1; //monster can only be stolen ONCE
    private ScheduledFuture<?> dropItemSchedule;
    private boolean shouldDropItem = false;
    private final ReentrantReadWriteLock poisonsLock = new ReentrantReadWriteLock();
    private final LinkedList<MonsterStatusEffect> poisons = new LinkedList<MonsterStatusEffect>();

    public MapleMonster(final int id, final MapleMonsterStats stats) {
        super(id);
        initWithStats(stats);
    }

    public MapleMonster(final MapleMonster monster) {
        super(monster);
        initWithStats(monster.stats);
    }

    private final void initWithStats(final MapleMonsterStats stats) {
        setStance(5);
        this.stats = stats;
        hp = stats.getHp();
        exp = stats.getExp();
        mp = stats.getMp();
        venom_counter = 0;
//	showdown = 100;
        carnivalTeam = -1;
        fake = false;
        dropsDisabled = false;

        if (stats.getNoSkills() > 0) {
            usedSkills = new HashMap<Integer, Long>();
        }
    }

    public final MapleMonsterStats getStats() {
        return stats;
    }

    public final void disableDrops() {
        this.dropsDisabled = true;
    }

    public final boolean dropsDisabled() {
        return dropsDisabled;
    }

    public final void setSponge(final MapleMonster mob) {
        sponge = new WeakReference<MapleMonster>(mob);
    }

    public final void setMap(final MapleMap map) {
        this.map = map;
        startDropItemSchedule();
    }

    public final long getHp() {
        return hp;
    }

    public final long getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public final void setHp(long hp) {
        this.hp = hp;
    }

    public final long getMobMaxHp() {
        if (ostats != null) {
            return ostats.getHp();
        }
        return stats.getHp();
    }

    public final int getMp() {
        return mp;
    }

    public final void setMp(int mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.mp = mp;
    }

    public final int getMobMaxMp() {
        if (ostats != null) {
            return ostats.getMp();
        }
        return stats.getMp();
    }

    public final int getMobExp() {
        if (ostats != null) {
            return ostats.getExp();
        }
        return stats.getExp();
    }

    public final int getMobLevel() {
        if (ostats != null) {
            return ostats.getlevel();
        }
        return stats.getLevel();
    }

    public final void setOverrideStats(final OverrideMonsterStats ostats) {
        this.ostats = ostats;
        this.hp = ostats.getHp();
        this.mp = ostats.getMp();
    }

    public OverrideMonsterStats getOstats() {
        return ostats;
    }

    public void setOstats(OverrideMonsterStats ostats) {
        this.ostats = ostats;
    }

    public final MapleMonster getSponge() {
        return sponge.get();
    }

    public final byte getVenomMulti() {
        return venom_counter;
    }

    public final void setVenomMulti(final byte venom_counter) {
        this.venom_counter = venom_counter;
    }

    public final void damage(final MapleCharacter from, final long damage, final boolean updateAttackTime) {
        damage(from, damage, updateAttackTime, 0, false);
    }

    public final void damage(final MapleCharacter from, final long damage, final boolean updateAttackTime, final boolean reaptToFrom) {
        damage(from, damage, updateAttackTime, 0, reaptToFrom);
    }

    public final void damage(final MapleCharacter from, final long damage, final boolean updateAttackTime, final int lastSkill) {
        damage(from, damage, updateAttackTime, lastSkill, false);
    }

    public final void damage(final MapleCharacter from, final long damage, final boolean updateAttackTime, final int lastSkill, final boolean reaptToFrom) {
        if (from == null || damage <= 0 || !isAlive()) {
            return;
        }
        AttackerEntry attacker = null;

        if (from.getParty() != null) {
            attacker = new PartyAttackerEntry(from.getParty().getId(), map.getChannel());
        } else {
            attacker = new SingleAttackerEntry(from, map.getChannel());
        }
        boolean replaced = false;
        for (final AttackerEntry aentry : attackers) {
            if (aentry.equals(attacker)) {
                attacker = aentry;
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            attackers.add(attacker);
        }
        final long rDamage = Math.max(0, Math.min(damage, hp));
        attacker.addDamage(from, rDamage, updateAttackTime);

        if (stats.getSelfD() != -1) {
            hp -= rDamage;
            if (hp > 0) {
                if (hp < stats.getSelfDHp()) { // HP is below the selfd level
                    map.killMonster(this, from, false, false, stats.getSelfD(), lastSkill);
                } else { // Show HP
                    for (final AttackerEntry mattacker : attackers) {
                        for (final AttackingMapleCharacter cattacker : mattacker.getAttackers()) {
                            if (cattacker.getAttacker().getMap() == from.getMap()) { // current attacker is on the map of the monster
                                if (cattacker.getLastAttackTime() >= System.currentTimeMillis() - 4000) {
                                    cattacker.getAttacker().getClient().sendPacket(MobPacket.showMonsterHP(getObjectId(), (int) Math.ceil((hp * 100.0) / getMobMaxHp())));
                                }
                            }
                        }
                    }
                }
            } else { // Character killed it without explosing :(
                map.killMonster(this, from, true, false, (byte) 1, lastSkill);
            }
        } else {
            if (sponge.get() != null) {
                if (sponge.get().hp > 0) { // If it's still alive, dont want double/triple rewards
                    // Sponge are always in the same map, so we can use this.map
                    // The only mob that uses sponge are PB/HT
                    sponge.get().hp -= rDamage;

                    if (sponge.get().hp <= 0) {
                        map.broadcastMessage(MobPacket.showBossHP(((MapleMonster) this.sponge.get()).getId(), -1L, ((MapleMonster) this.sponge.get()).getMobMaxHp()));
                        map.killMonster(sponge.get(), from, true, false, (byte) 1, lastSkill);
                    } else {
                        map.broadcastMessage(MobPacket.showBossHP(sponge.get()));
                    }
                }
            }
            if (hp > 0) {
                hp -= rDamage;
                if (eventInstance != null) {
                    eventInstance.monsterDamaged(from, this, (int) rDamage);
                } else {
                    final EventInstanceManager em = from.getEventInstance();
                    if (em != null) {
                        em.monsterDamaged(from, this, (int) rDamage);
                    }
                }
                if (sponge.get() == null && hp > 0) {
                    switch (stats.getHPDisplayType()) {
                        case 0:
                            map.broadcastMessage(MobPacket.showBossHP(this), this.getPosition());
                            break;

                        case 1:
                            map.broadcastMessage(from, MobPacket.damageFriendlyMob(this, damage, true), reaptToFrom);
                            //   map.broadcastMessage(MobPacket.damageFriendlyMob(this, damage, true));
                            // map.broadcastMessage(MobPacket.showMonsterHP(getObjectId(), (int) Math.ceil((hp * 100.0) / getMobMaxHp())));
                            break;
                        case -1:
                        case 2:
                            int oid = getObjectId();
                            double percent = (hp * 100.0) / getMobMaxHp();
                            int show = (int) Math.ceil(percent);
                            map.broadcastMessage(MobPacket.showMonsterHP(getObjectId(), (int) Math.ceil((hp * 100.0) / getMobMaxHp())));
                            from.mulung_EnergyModify(true);
                            break;
                        case 3:
                            for (final AttackerEntry mattacker : attackers) {
                                for (final AttackingMapleCharacter cattacker : mattacker.getAttackers()) {
                                    if (cattacker.getAttacker().getMap() == from.getMap()) { // current attacker is on the map of the monster
                                        if (cattacker.getLastAttackTime() >= System.currentTimeMillis() - 4000) {
                                            cattacker.getAttacker().getClient().sendPacket(MobPacket.showMonsterHP(getObjectId(), (int) Math.ceil((hp * 100.0) / getMobMaxHp())));
                                        }
                                    }
                                }
                            }
                            break;
                        default:
                            System.out.println(stats.isBoss() + " " + stats.getHPDisplayType());
                            break;
                    }
                }
                if (hp <= 0) {
                    if (this.stats.getHPDisplayType() == 0 || this.stats.getHPDisplayType() == -1) {
                        this.map.broadcastMessage(MobPacket.showBossHP(getId(), -1L, getMobMaxHp()), this.getPosition());
                    }
                    map.killMonster(this, from, true, false, (byte) 1, lastSkill);
                }
            }
        }
        startDropItemSchedule();
    }

    public final void heal(int hp, int mp, final boolean broadcast) {
        final long TotalHP = getHp() + hp;
        final int TotalMP = getMp() + mp;

        if (TotalHP >= getMobMaxHp()) {
            setHp(getMobMaxHp());
        } else {
            setHp(TotalHP);
        }
        if (TotalMP >= getMp()) {
            setMp(getMp());
        } else {
            setMp(TotalMP);
        }
        if (broadcast) {
            map.broadcastMessage(MobPacket.healMonster(getObjectId(), hp));
        } else if (sponge.get() != null) {
            sponge.get().hp += hp;
        }
    }

    ///给经验
    private final void giveExpToCharacter(final MapleCharacter attacker, int exp, final boolean highestDamage, final int numExpSharers, final byte pty, final byte Class_Bonus_EXP_PERCENT, final byte Premium_Bonus_EXP_PERCENT, final int lastskillID) {
        if (highestDamage) {
            if (eventInstance != null) {
                eventInstance.monsterKilled(attacker, this);
            } else {
                final EventInstanceManager em = attacker.getEventInstance();
                if (em != null) {
                    em.monsterKilled(attacker, this);
                }
            }
            highestDamageChar = attacker.getId();
        }
        double 怪物坐标X = getPosition().getX();
        double 怪物坐标Y = getPosition().getY();
        double X坐标误差 = attacker.getPosition().getX() - 怪物坐标X;
        double Y坐标误差 = attacker.getPosition().getY() - 怪物坐标Y;
        if (gui.Start.ConfigValuesMap.get("吸怪检测开关") != null) {
            if (gui.Start.ConfigValuesMap.get("吸怪检测开关") == 0) {
                if (attacker.记录坐标X == 0 && attacker.记录坐标Y == 0) {
                    attacker.记录坐标X = 怪物坐标X;
                    attacker.记录坐标Y = 怪物坐标Y;
                } else if (怪物坐标X != attacker.记录坐标X || 怪物坐标Y != attacker.记录坐标Y) {
                    attacker.记录坐标X = 0;
                    attacker.记录坐标Y = 0;
                } else if (怪物坐标X == attacker.记录坐标X && 怪物坐标Y == attacker.记录坐标Y) {
                    attacker.吸怪指数++;
                    if (attacker.吸怪指数 == 50) {
                        int ch = World.Find.findChannel(角色ID取名字(attacker.getId()));
                        MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(attacker.getId()));
                        if (target.ban("被吸怪检测封号", attacker.isAdmin(), false, false)) {
                            String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，游戏吸怪/定点生怪，破坏游戏平衡，被系统永久封号。";
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                            sendMsgToQQGroup(信息);
                        }
                    }
                }
            }
        }
        int 记录地图 = attacker.getMapId();
        if (attacker.打怪地图 == 0) {
            attacker.打怪地图 = 记录地图;
        } else if (记录地图 != attacker.打怪地图) {
            attacker.打怪地图 = 0;
            attacker.打怪数量 = 0;
            attacker.X坐标误差 = 0;
            attacker.Y坐标误差 = 0;
        }
        if (attacker.打怪地图 > 0) {
            if (X坐标误差 > attacker.X坐标误差) {
                attacker.X坐标误差 = X坐标误差;
            }
            if (Y坐标误差 > attacker.Y坐标误差) {
                attacker.Y坐标误差 = Y坐标误差;
            }
        }
        //当经验大于0
        if (exp > 0) {
            if (gui.Start.ConfigValuesMap.get("越级打怪开关") == 0) {
                int 怪物 = getMobLevel();
                int 玩家 = attacker.getLevel();
                if (玩家 < 怪物) {
                    int 相差 = 怪物 - 玩家;
                    if (相差 >= 10 && 相差 <= 20) {
                        exp = (int) (exp * 0.6);
                    } else if (相差 >= 21 && 相差 <= 30) {
                        exp = (int) (exp * 0.4);
                    } else if (相差 >= 31) {
                        exp = (int) (exp * 0.2);
                    }
                }
            }
            //怪物本身的经验
            final MonsterStatusEffect mse = stati.get(MonsterStatus.SHOWDOWN);
            if (mse != null) {
                exp += (int) (exp * (mse.getX() / 100.0));
            }
            //神圣祈祷，牧师技能
            final Integer holySymbol = attacker.getBuffedValue(MapleBuffStat.HOLY_SYMBOL);
            if (holySymbol != null) {
                if (numExpSharers == 1) {
                    exp *= 1.0 + (holySymbol.doubleValue() / 500.0);
                } else {
                    exp *= 1.0 + (holySymbol.doubleValue() / 100.0);
                }
                if (attacker.getEquippedFuMoMap().get(43) != null) {
                    attacker.gainExp(exp / 100 * attacker.getEquippedFuMoMap().get(43), true, false, false);
                }
            }
            int 职业 = attacker.getJob();
            int 职业2 = MapleParty.幸运职业;
            if ((职业 == 职业2) || (职业 - 职业2 == 1) || (职业2 - 职业 == -1)) {
                exp += exp * 0.5;
            }
            //被诅咒减少经验
            if (attacker.hasDisease(MapleDisease.CURSE)) {
                if (attacker.getEquippedFuMoMap().get(32) != null) {
                    exp *= 5;
                } else {
                    exp /= 2;
                }
            }
            //怪物的经验
            exp *= attacker.getEXPMod() * (int) (attacker.getStat().expBuff / 100.0);
            exp = (int) Math.min(Integer.MAX_VALUE, exp * (attacker.getLevel() < 10 ? GameConstants.getExpRate_Below10(attacker.getJob()) : ChannelServer.getInstance(map.getChannel()).getExpRate()));

            //未知经验加成?似乎没作用
            int Class_Bonus_EXP = 0;
            if (Class_Bonus_EXP_PERCENT > 0) {
                Class_Bonus_EXP = (int) ((exp / 100.0) * Class_Bonus_EXP_PERCENT);
                attacker.dropMessage(5, "Class_Bonus_EXP:" + Class_Bonus_EXP);
            }
            //网吧经验
            int Premium_Bonus_EXP = 0;
            //if (Premium_Bonus_EXP_PERCENT == 0) {
            if (gui.Start.ConfigValuesMap.get("网吧经验加成") != 0) {
                int 网吧经验加成 = gui.Start.ConfigValuesMap.get("网吧经验加成");
                //Premium_Bonus_EXP = (int) ((exp / 100.0) * Premium_Bonus_EXP_PERCENT);
                Premium_Bonus_EXP += (int) ((exp / 100.0) * 网吧经验加成);
            }
            //道具佩戴经验加成
            int Equipment_Bonus_EXP = (int) ((exp / 100.0) * attacker.getStat().equipmentBonusExp);
//            if (attacker.getStat().equippedFairy) {
            if (attacker.getStat().精灵吊坠) {
                Equipment_Bonus_EXP += (int) ((exp / 100.0) * attacker.getFairyExp());
            }
            //结婚经验加成
            int wedding_EXP = 0;
            if (attacker.getMarriageId() > 0 && attacker.getMap().getCharacterById_InMap(attacker.getMarriageId()) != null && gui.Start.ConfigValuesMap.get("结婚经验加成") != 0) {
                //wedding_EXP += (exp / 100.0d) * 30.0d;
                int 结婚经验加成 = gui.Start.ConfigValuesMap.get("结婚经验加成");
                wedding_EXP += (exp / 100.0d) * 结婚经验加成;
            }
            //人气经验加成
            if (gui.Start.ConfigValuesMap.get("人气经验加成") > 0) {
                if (attacker.getFame() > 0) {
                    attacker.人气经验加成();
                }
            }
            if (attacker.getEquippedFuMoMap().get(31) != null) {
                int EXP = (int) ((exp / 100.0) * attacker.getEquippedFuMoMap().get(31));
                attacker.gainExp(EXP, true, false, false);
            }

            attacker.gainExpMonster(exp, true, highestDamage, pty, wedding_EXP, Class_Bonus_EXP, Equipment_Bonus_EXP, Premium_Bonus_EXP, lastskillID, getId(), stats.getLevel(), stats.getHp());
            //给装备经验
            if (gui.Start.ConfigValuesMap.get("永恒升级开关") == 0 || gui.Start.ConfigValuesMap.get("重生升级开关") == 0) {
                if (exp > gui.Start.ConfigValuesMap.get("装备一次最多经验获取")) {
                    exp = gui.Start.ConfigValuesMap.get("装备一次最多经验获取");
                }
                attacker.increaseEquipExp(exp * gui.Start.ConfigValuesMap.get("装备经验获取率") / 100);
            }
        }
        attacker.mobKilled(getId(), lastskillID);
    }

    public final int killBy(final MapleCharacter killer, final int lastSkill) {
        int totalBaseExp = getMobExp();
        AttackerEntry highest = null;
        long highdamage = 0;
        for (final AttackerEntry attackEntry : attackers) {
            if (attackEntry.getDamage() > highdamage) {
                highest = attackEntry;
                highdamage = attackEntry.getDamage();
            }
        }
        int baseExp;
        for (final AttackerEntry attackEntry : attackers) {
            baseExp = (int) Math.ceil(totalBaseExp * ((double) attackEntry.getDamage() / getMobMaxHp()));
            attackEntry.killedMob(getMap(), baseExp, attackEntry == highest, lastSkill);
        }
        final MapleCharacter controll = controller.get();
        if (controll != null) { // this can/should only happen when a hidden gm attacks the monster
            controll.getClient().sendPacket(MobPacket.stopControllingMonster(getObjectId()));
            controll.stopControllingMonster(this);
        }
        //int achievement = 0;

        switch (getId()) {
            case 9400121:
            //achievement = 12;
            //break;
            case 8500002:
            //achievement = 13;
            //break;
            case 8510000:
            case 8520000:
            //achievement = 14;
            //break;
            default:
                break;
        }
        spawnRevives(getMap());
        if (eventInstance != null) {
            eventInstance.unregisterMonster(this);
            eventInstance = null;
        }
        if (killer != null && killer.getPyramidSubway() != null) {
            killer.getPyramidSubway().onKill(killer);
        }
        MapleMonster oldSponge = getSponge();
        sponge = new WeakReference<MapleMonster>(null);
        if (oldSponge != null && oldSponge.isAlive()) {
            boolean set = true;
            for (MapleMapObject mon : map.getAllMonstersThreadsafe()) {
                MapleMonster mons = (MapleMonster) mon;
                if (mons.getObjectId() != oldSponge.getObjectId() && mons.getObjectId() != this.getObjectId() && (mons.getSponge() == oldSponge || mons.getLinkOid() == oldSponge.getObjectId())) { //sponge was this, please update
                    set = false;
                    break;
                }
            }
            if (set) { //all sponge monsters are dead, please kill off the sponge
                map.killMonster(oldSponge, killer, true, false, (byte) 1);
            }
        }

        nodepack = null;
        reflectpack = null;
        stati.clear();
        //attackers.clear();
        cancelDropItem();
        if (listener != null) {
            listener.monsterKilled();
        }
        int v1 = highestDamageChar;
        this.highestDamageChar = 0; //reset so we dont kill twice
        return v1;
    }

    //召唤怪物
    public final void spawnRevives(final MapleMap map) {
        final List<Integer> toSpawn = stats.getRevives();

        if (toSpawn == null) {
            return;
        }
        MapleMonster spongy = null;
        switch (getId()) {
            case 8810118:
            case 8810119:
            case 8810120:
            case 8810121: //must update sponges
                for (final int i : toSpawn) {
                    final MapleMonster mob = MapleLifeFactory.getMonster(i);

                    mob.setPosition(getPosition());
                    if (eventInstance != null) {
                        eventInstance.registerMonster(mob);
                    }
                    if (dropsDisabled()) {
                        mob.disableDrops();
                    }
                    switch (mob.getId()) {
                        case 8810119:
                        case 8810120:
                        case 8810121:
                        case 8810122:
                            spongy = mob;
                            break;
                    }
                }
                if (spongy != null) {
                    map.spawnRevives(spongy, this.getObjectId());
                    for (MapleMapObject mon : map.getAllMonstersThreadsafe()) {
                        MapleMonster mons = (MapleMonster) mon;
                        if (mons.getObjectId() != spongy.getObjectId() && (mons.getSponge() == this || mons.getLinkOid() == this.getObjectId())) { //sponge was this, please update
                            mons.setSponge(spongy);
                            mons.setLinkOid(spongy.getObjectId());
                        }
                    }
                }
                break;
            case 8810026:
            case 8810130:
            case 8820008:
            case 8820009:
            case 8820010:
            case 8820011:
            case 8820012:
            case 8820013: {
                final List<MapleMonster> mobs = new ArrayList<MapleMonster>();

                for (final int i : toSpawn) {
                    final MapleMonster mob = MapleLifeFactory.getMonster(i);

                    mob.setPosition(getPosition());
                    if (eventInstance != null) {
                        eventInstance.registerMonster(mob);
                    }
                    if (dropsDisabled()) {
                        mob.disableDrops();
                    }
                    switch (mob.getId()) {
                        case 8810018: // Horntail Sponge
                        case 8810118:
                        case 8820009: // PinkBeanSponge0
                        case 8820010: // PinkBeanSponge1
                        case 8820011: // PinkBeanSponge2
                        case 8820012: // PinkBeanSponge3
                        case 8820013: // PinkBeanSponge4
                        case 8820014: // PinkBeanSponge5
                            spongy = mob;
                            break;
                        default:
                            mobs.add(mob);
                            break;
                    }
                }
                if (spongy != null) {
                    map.spawnRevives(spongy, this.getObjectId());

                    for (final MapleMonster i : mobs) {
                        i.setSponge(spongy);
                        map.spawnRevives(i, this.getObjectId());
                    }
                }
                break;
            }
            default: {
                for (final int i : toSpawn) {
                    final MapleMonster mob = MapleLifeFactory.getMonster(i);

                    if (eventInstance != null) {
                        eventInstance.registerMonster(mob);
                    }
                    mob.setPosition(getPosition());
                    if (dropsDisabled()) {
                        mob.disableDrops();
                    }
                    map.spawnRevives(mob, this.getObjectId());
                    if (mob.getId() == 9300216) {
                        map.broadcastMessage(MaplePacketCreator.environmentChange("Dojang/clear", 4));
                        map.broadcastMessage(MaplePacketCreator.environmentChange("dojang/end/clear", 3));
                    }
                }
                break;
            }
        }
    }

    public final boolean isAlive() {
        return hp > 0;
    }

    public final void setCarnivalTeam(final byte team) {
        carnivalTeam = team;
    }

    public final byte getCarnivalTeam() {
        return carnivalTeam;
    }

    public final MapleCharacter getController() {
        return controller.get();
    }

    public final void setController(final MapleCharacter controller) {
        this.controller = new WeakReference<MapleCharacter>(controller);
    }

    //邪摩斯
    public final void switchController(final MapleCharacter newController, final boolean immediateAggro) {
        final MapleCharacter controllers = getController();
        if (controllers == newController) {
            return;
        } else if (controllers != null) {
            controllers.stopControllingMonster(this);
            controllers.getClient().sendPacket(MobPacket.stopControllingMonster(getObjectId()));
        }
        newController.controlMonster(this, immediateAggro);
        setController(newController);
        if (immediateAggro) {
            setControllerHasAggro(true);
        }
        setControllerKnowsAboutAggro(false);
        if (getId() == 9300275 && map.getId() >= 921120100 && map.getId() < 921120500) { //shammos
            if (lastNodeController != -1 && lastNodeController != newController.getId()) { //new controller, please re update
                resetShammos(newController.getClient());
            } else {
                setLastNodeController(newController.getId());
            }
        }
    }

    public final void resetShammos(MapleClient c) {
        map.killAllMonsters(true);
        map.broadcastMessage(MaplePacketCreator.serverNotice(5, "一个球员从Shammos搬太远。Shammos回到开始。"));
        for (MapleCharacter chr : map.getCharactersThreadsafe()) {
            chr.changeMap(chr.getMap(), chr.getMap().getPortal(0));
        }
        //屏蔽地图动画
        // MapScriptMethods.startScript_FirstUser(c, "shammos_Fenter");
    }

    public final void addListener(final MonsterListener listener) {
        this.listener = listener;
    }

    public final boolean isControllerHasAggro() {
        return controllerHasAggro;
    }

    public final void setControllerHasAggro(final boolean controllerHasAggro) {
        this.controllerHasAggro = controllerHasAggro;
    }

    public final boolean isControllerKnowsAboutAggro() {
        return controllerKnowsAboutAggro;
    }

    public final void setControllerKnowsAboutAggro(final boolean controllerKnowsAboutAggro) {
        this.controllerKnowsAboutAggro = controllerKnowsAboutAggro;
    }

    @Override
    public final void sendSpawnData(final MapleClient client) {
        if (!isAlive()) {
            return;
        }
        client.sendPacket(MobPacket.spawnMonster(this, (lastNode >= 0 ? -2 : -1), fake ? 0xfc : (lastNode >= 0 ? 12 : 0), 0));
        if (reflectpack != null) {
            client.sendPacket(reflectpack);
        }
        if (lastNode >= 0) {
            //   client.sendPacket(MaplePacketCreator.getNodeProperties(this, map));
            if (getId() == 9300275 && map.getId() >= 921120100 && map.getId() < 921120500) { //shammos
                if (lastNodeController != -1) { //new controller, please re update. sendSpawn only comes when you get too far then come back anyway
                    resetShammos(client);
                } else {
                    setLastNodeController(client.getPlayer().getId());
                }
            }
        }
    }

    @Override
    public final void sendDestroyData(final MapleClient client) {
        if (lastNode == -1) {
            client.sendPacket(MobPacket.killMonster(getObjectId(), 0));
        }
        if (getId() == 9300275 && map.getId() >= 921120100 && map.getId() < 921120500) { //shammos
            resetShammos(client);
        }
    }

    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(stats.getName());
        sb.append("(");
        sb.append(getId());
        sb.append(") (等级 ");
        sb.append(stats.getLevel());
        sb.append(") 在 (X");
        sb.append(getPosition().x);
        sb.append("/ Y");
        sb.append(getPosition().y);
        sb.append(") 坐标 ");
        sb.append(getHp());
        sb.append("/ ");
        sb.append(getMobMaxHp());
        sb.append("血量, ");
        sb.append(getMp());
        sb.append("/ ");
        sb.append(getMobMaxMp());
        sb.append(" 魔力，反应物: ");
        sb.append(getObjectId());
        sb.append(" || 仇恨目标 : ");
        final MapleCharacter chr = controller.get();
        sb.append(chr != null ? chr.getName() : "未知");
        return sb.toString();
    }

    public final String toString2() {
        final StringBuilder sb = new StringBuilder();
        sb.append(stats.getName());
        sb.append("  等级:");
        sb.append(stats.getLevel());
        sb.append("  血量:");
        sb.append(getHp());
        return sb.toString();
    }
//    public final String toString2() {
//        final StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        sb.append(stats.getName());
//        sb.append("] 等级[");
//        sb.append(stats.getLevel());
//        sb.append("] 血量[");
//        sb.append(getHp());
////        sb.append("/");
////        sb.append(getMobMaxHp());
//        sb.append("]");
//        return sb.toString();
//    }

    public final String toString3() {
        final StringBuilder sb = new StringBuilder();
        sb.append("怪物:");
        sb.append(stats.getName());
        sb.append("\r\n等级:");
        sb.append(stats.getLevel());
        sb.append("\r\n血量:");
        sb.append(getMobMaxHp());
        return sb.toString();
    }

    @Override
    public final MapleMapObjectType getType() {
        return MapleMapObjectType.MONSTER;
    }

    public final EventInstanceManager getEventInstance() {
        return eventInstance;
    }

    public final void setEventInstance(final EventInstanceManager eventInstance) {
        this.eventInstance = eventInstance;
    }

    public final int getStatusSourceID(final MonsterStatus status) {
        final MonsterStatusEffect effect = stati.get(status);
        if (effect != null) {
            return effect.getSkill();
        }
        return -1;
    }

    public final ElementalEffectiveness getEffectiveness(final Element e) {
        if (stati.size() > 0 && stati.get(MonsterStatus.DOOM) != null) {
            return ElementalEffectiveness.NORMAL; // like blue snails
        }
        return stats.getEffectiveness(e);
    }

    public final void applyStatus(final MapleCharacter from, final MonsterStatusEffect status, final boolean poison, final long duration, final boolean venom) {
        applyStatus(from, status, poison, duration, venom, true);
    }

    //玩家给怪物上BUFF
    public final void applyStatus(final MapleCharacter from, final MonsterStatusEffect status, final boolean poison, final long duration, final boolean venom, final boolean checkboss) {
        if (!isAlive() || stats.isBoss()) {
            return;
        }
        if (gui.Start.ConfigValuesMap.get("怪物状态开关") <= 0) {
            if (from.hasGmLevel(5)) {
                String 状态 = "";
                if (null != status.getStati().name()) {
                    switch (status.getStati().name()) {
                        case "STUN":
                            状态 = "怪物无法移动,[昏迷]，[冰冻]";
                            break;
                        case "POISON":
                            状态 = "怪物持续掉血,[中毒]，[灼烧]";
                            break;
                        case "SPEED":
                            状态 = "怪物减少移动速度,[缓速]，[束缚]";
                            break;
                        case "DOOM":
                            状态 = "怪物改变外观,[巫毒]，[变身]";
                            break;
                        case "SEAL":
                            状态 = "怪物无法使用技能,[封印]，[沉默]";
                            break;
                        case "SHADOW_WEB":
                            状态 = "怪物定身，无法移动,[束缚]，[昏迷]，[定身]";
                            break;
                        case "SHOWDOWN":
                            状态 = "怪物被激怒,[挑衅]，[诱导]";
                            break;
                        case "MDEF":
                            状态 = "怪物防御发生变化,[魔防]";
                            break;
                        case "WDEF":
                            状态 = "怪物防御发生变化,[物防]";
                            break;
                        default:
                            from.dropMessage(5, "怪物状态: " + status.getStati().name() + "");
                            break;
                    }
                }
                from.dropMessage(5, "怪物状态: " + 状态 + "");
            }
        }
        ISkill skilz = SkillFactory.getSkill(status.getSkill());
        if (skilz != null) {
            switch (stats.getEffectiveness(skilz.getElement())) {
                case IMMUNE://免疫
                case STRONG://增强
                    return;
                case NORMAL://正常
                case WEAK://虚弱
                    break;
                default:
                    return;
            }
        }
        final int statusSkill = status.getSkill();
        switch (statusSkill) {
            //毒雾术
            case 2101005:
            //火毒合击
            case 2111006: {
                switch (stats.getEffectiveness(Element.POISON)) {//中毒
                    case IMMUNE://免疫
                    case STRONG://增强
                        return;
                }
                break;
            }
            //冰凤球
            case 2211006: {
                switch (stats.getEffectiveness(Element.ICE)) {
                    case IMMUNE://免疫
                    case STRONG://增强
                        return;
                }
                break;
            }
            //武器用毒液
            case 4120005:
            case 4220005:
            case 14110004: {
                switch (stats.getEffectiveness(Element.POISON)) {//中毒
                    case IMMUNE://免疫
                    case STRONG://增强
                        return;
                }
                break;
            }

        }
        //巫毒术
        final MonsterStatus stat = status.getStati();
        if (stats.isNoDoom() && stat == MonsterStatus.DOOM) {
            return;
        }
        //昏迷减速对BOSS无效
        if (stats.isBoss()) {
            if (stat == MonsterStatus.STUN || stat == MonsterStatus.SPEED) {//眩晕 或 速度
                return;
            }
            if (checkboss && stat != (MonsterStatus.NINJA_AMBUSH) && stat != (MonsterStatus.WATK) && stat != (MonsterStatus.POISON)) {
                return;
            }
        }
        final MonsterStatusEffect oldEffect = stati.get(stat);
        if (oldEffect != null) {
            stati.remove(stat);
            if (oldEffect.getStati() == null) {
                oldEffect.cancelTask();
                oldEffect.cancelPoisonSchedule();
            }
        }
        final MobTimer timerManager = MobTimer.getInstance();
        final Runnable cancelTask = new Runnable() {

            @Override
            public final void run() {
                cancelStatus(stat);
            }
        };

        if (stat == MonsterStatus.FREEZE) {
            poisonsLock.readLock().lock();
        }
        //设置中毒的持续时间
        if (poison && getHp() > 1) {
            final int poisonDamage = (int) Math.min(Short.MAX_VALUE, (long) (getMobMaxHp() / (70.0 - from.getSkillLevel(status.getSkill())) + 0.999));
            status.setValue(MonsterStatus.POISON, Integer.valueOf(poisonDamage));
            status.setPoisonSchedule(timerManager.register(new PoisonTask(poisonDamage, from, status, cancelTask, false), 1000, 1000));
        } else if (venom) {
            //武器用毒液
            int poisonLevel = 0;
            int matk = 0;
            switch (from.getJob()) {
                case 412:
                    poisonLevel = from.getSkillLevel(SkillFactory.getSkill(4120005));
                    if (poisonLevel <= 0) {
                        return;
                    }
                    matk = SkillFactory.getSkill(4120005).getEffect(poisonLevel).getMatk();
                    break;
                case 422:
                    poisonLevel = from.getSkillLevel(SkillFactory.getSkill(4220005));
                    if (poisonLevel <= 0) {
                        return;
                    }
                    matk = SkillFactory.getSkill(4220005).getEffect(poisonLevel).getMatk();
                    break;
                case 434:
                    poisonLevel = from.getSkillLevel(SkillFactory.getSkill(4340001));
                    if (poisonLevel <= 0) {
                        return;
                    }
                    matk = SkillFactory.getSkill(4340001).getEffect(poisonLevel).getMatk();
                    break;
                case 1411:
                case 1412:
                    poisonLevel = from.getSkillLevel(SkillFactory.getSkill(14110004));
                    if (poisonLevel <= 0) {
                        return;
                    }
                    matk = SkillFactory.getSkill(14110004).getEffect(poisonLevel).getMatk();
                    break;
                default:
                    return; // Hack, using venom without the job required
                }
            final int luk = from.getStat().getLuk();
            final int maxDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.2 * luk * matk));
            final int minDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.1 * luk * matk));
            int gap = maxDmg - minDmg;
            if (gap == 0) {
                gap = 1;
            }
            int poisonDamage = 0;
            for (int i = 0; i < getVenomMulti(); i++) {
                poisonDamage = poisonDamage + (Randomizer.nextInt(gap) + minDmg);
            }
            poisonDamage = Math.min(Short.MAX_VALUE, poisonDamage);
            status.setValue(MonsterStatus.POISON, Integer.valueOf(poisonDamage));
            status.setPoisonSchedule(timerManager.register(new PoisonTask(poisonDamage, from, status, cancelTask, false), 1000, 1000));
            // 4111003 夜行者- 影网术 - [最高等级:20]\n以自身的影子做成蜘蛛网，缠住6个以下的多个怪物。被缠住的怪物无法动弹。
        } else if (statusSkill == 4111003 || statusSkill == 14111001) { //14111001 夜行者- 影网术 - [最高等级：20]\n以自身的影子做成蜘蛛网，缠住6个以下的多个怪物。被缠住的怪物无法动弹。
            status.setPoisonSchedule(timerManager.schedule(new PoisonTask((int) (getMobMaxHp() / 50.0 + 0.999), from, status, cancelTask, true), 3500));
            //4121004 - 忍者伏击 - [最高等级 : 30]\n给一定范围内的敌人持续的伤害.一次不能攻击6只以上,HP不掉到1以下.\n必要技能 : #c假动作等级5以上#
        } else if (statusSkill == 4121004 || statusSkill == 4221004) {// - 忍者伏击 - 躲藏的同伴突然出现在一定时间内持续攻击敌人.\n一次无法攻击6只以上,HP不会掉到1以下.\n必要技能 : #c假动作 5级 以上#
            final int damage = (from.getStat().getStr() + from.getStat().getLuk()) * 2 * (60 / 100);
            status.setPoisonSchedule(timerManager.register(new PoisonTask(damage, from, status, cancelTask, false), 1000, 1000));
        }

        stati.put(stat, status);
        map.broadcastMessage(MobPacket.applyMonsterStatus(getObjectId(), status), getPosition());
        if (getController() != null && !getController().isMapObjectVisible(this)) {
            getController().getClient().sendPacket(MobPacket.applyMonsterStatus(getObjectId(), status));
        }
        int aniTime = 0;
        if (skilz != null) {
            aniTime = skilz.getAnimationTime();
        }
        ScheduledFuture<?> schedule = timerManager.schedule(cancelTask, duration + aniTime);
        status.setCancelTask(schedule);
    }

    public final void dispelSkill(final MobSkill skillId) {
        List<MonsterStatus> toCancel = new ArrayList<MonsterStatus>();
        for (Entry<MonsterStatus, MonsterStatusEffect> effects : stati.entrySet()) {
            if (effects.getValue().getMobSkill() != null && effects.getValue().getMobSkill().getSkillId() == skillId.getSkillId()) { //not checking for level.
                toCancel.add(effects.getKey());
            }
        }
        for (MonsterStatus stat : toCancel) {
            cancelStatus(stat);
        }
    }

    public final void applyMonsterBuff(final Map<MonsterStatus, Integer> effect, final int skillId, final long duration, final MobSkill skill, final List<Integer> reflection) {
        MobTimer timerManager = MobTimer.getInstance();
        final Runnable cancelTask = new Runnable() {

            @Override
            public final void run() {
                if (reflection.size() > 0) {
                    MapleMonster.this.reflectpack = null;
                }
                if (isAlive()) {
                    for (MonsterStatus z : effect.keySet()) {
                        cancelStatus(z);
                    }
                }
            }
        };
        for (Entry<MonsterStatus, Integer> z : effect.entrySet()) {
            final MonsterStatusEffect effectz = new MonsterStatusEffect(z.getKey(), z.getValue(), 0, skill, true);
            stati.put(z.getKey(), effectz);
        }
        if (reflection.size() > 0) {
            this.reflectpack = MobPacket.applyMonsterStatus(getObjectId(), effect, reflection, skill);
            map.broadcastMessage(reflectpack, getPosition());
            if (getController() != null && !getController().isMapObjectVisible(this)) {
                getController().getClient().sendPacket(this.reflectpack);
            }
        } else {
            for (Entry<MonsterStatus, Integer> z : effect.entrySet()) {
                map.broadcastMessage(MobPacket.applyMonsterStatus(getObjectId(), z.getKey(), z.getValue(), skill), getPosition());
                if (getController() != null && !getController().isMapObjectVisible(this)) {
                    getController().getClient().sendPacket(MobPacket.applyMonsterStatus(getObjectId(), z.getKey(), z.getValue(), skill));
                }
            }
        }
        timerManager.schedule(cancelTask, duration);
    }

    public final void setTempEffectiveness(final Element e, final long milli) {
        stats.setEffectiveness(e, ElementalEffectiveness.WEAK);
        MobTimer.getInstance().schedule(new Runnable() {

            public void run() {
                stats.removeEffectiveness(e);
            }
        }, milli);
    }

    public final boolean isBuffed(final MonsterStatus status) {
        return stati.containsKey(status);
    }

    public final MonsterStatusEffect getBuff(final MonsterStatus status) {
        return stati.get(status);
    }

    public final void setFake(final boolean fake) {
        this.fake = fake;
    }

    public final boolean isFake() {
        return fake;
    }

    public final MapleMap getMap() {
        return map;
    }

    public final List<Pair<Integer, Integer>> getSkills() {
        return stats.getSkills();
    }

    public final boolean hasSkill(final int skillId, final int level) {
        return stats.hasSkill(skillId, level);
    }

    public final long getLastSkillUsed(final int skillId) {
        if (usedSkills.containsKey(skillId)) {
            return usedSkills.get(skillId);
        }
        return 0;
    }

    public final void setLastSkillUsed(final int skillId, final long now, final long cooltime) {
        switch (skillId) {
            case 140:
                usedSkills.put(skillId, now + (cooltime * 2));
                usedSkills.put(141, now);
                break;
            case 141:
                usedSkills.put(skillId, now + (cooltime * 2));
                usedSkills.put(140, now + cooltime);
                break;
            default:
                usedSkills.put(skillId, now + cooltime);
                break;
        }
    }

    public final byte getNoSkills() {
        return stats.getNoSkills();
    }

    public final boolean isFirstAttack() {
        return stats.isFirstAttack();
    }

    public final int getBuffToGive() {
        return stats.getBuffToGive();
    }

    private final class PoisonTask implements Runnable {

        private final int poisonDamage;
        private final MapleCharacter chr;
        private final MonsterStatusEffect status;
        private final Runnable cancelTask;
        private final boolean shadowWeb;
        private final MapleMap map;

        private PoisonTask(final int poisonDamage, final MapleCharacter chr, final MonsterStatusEffect status, final Runnable cancelTask, final boolean shadowWeb) {
            this.poisonDamage = poisonDamage;
            this.chr = chr;
            this.status = status;
            this.cancelTask = cancelTask;
            this.shadowWeb = shadowWeb;
            this.map = chr.getMap();
        }

        @Override
        public void run() {
            long damage = poisonDamage;
            if (damage >= hp) {
                damage = hp - 1;
                if (!shadowWeb) {
                    cancelTask.run();
                    status.cancelTask();
                }
            }
            if (hp > 1 && damage > 0) {
                damage(chr, damage, false);
                if (shadowWeb) {
                    map.broadcastMessage(MobPacket.damageMonster(getObjectId(), damage), getPosition());
                }
            }
        }
    }

    private static class AttackingMapleCharacter {

        private MapleCharacter attacker;
        private long lastAttackTime;

        public AttackingMapleCharacter(final MapleCharacter attacker, final long lastAttackTime) {
            super();
            this.attacker = attacker;
            this.lastAttackTime = lastAttackTime;
        }

        public final long getLastAttackTime() {
            return lastAttackTime;
        }

        public final void setLastAttackTime(final long lastAttackTime) {
            this.lastAttackTime = lastAttackTime;
        }

        public final MapleCharacter getAttacker() {
            return attacker;
        }
    }

    private interface AttackerEntry {

        List<AttackingMapleCharacter> getAttackers();

        public void addDamage(MapleCharacter from, long damage, boolean updateAttackTime);

        public long getDamage();

        public boolean contains(MapleCharacter chr);

        public void killedMob(MapleMap map, int baseExp, boolean mostDamage, int lastSkill);
    }

    private final class SingleAttackerEntry implements AttackerEntry {

        private long damage = 0;
        private int chrid;
        private long lastAttackTime;
        private int channel;

        public SingleAttackerEntry(final MapleCharacter from, final int cserv) {
            this.chrid = from.getId();
            this.channel = cserv;
        }

        @Override
        public void addDamage(final MapleCharacter from, final long damage, final boolean updateAttackTime) {
            if (chrid == from.getId()) {
                this.damage += damage;
                if (updateAttackTime) {
                    lastAttackTime = System.currentTimeMillis();
                }
            }
        }

        @Override
        public final List<AttackingMapleCharacter> getAttackers() {
            final MapleCharacter chr = map.getCharacterById(chrid);
            if (chr != null) {
                return Collections.singletonList(new AttackingMapleCharacter(chr, lastAttackTime));
            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public boolean contains(final MapleCharacter chr) {
            return chrid == chr.getId();
        }

        @Override
        public long getDamage() {
            return damage;
        }

        @Override
        public void killedMob(final MapleMap map, final int baseExp, final boolean mostDamage, final int lastSkill) {
            final MapleCharacter chr = map.getCharacterById(chrid);
            if (chr != null && chr.isAlive()) {
                giveExpToCharacter(chr, baseExp, mostDamage, 1, (byte) 0, (byte) 0, (byte) 0, lastSkill);
            }
        }

        @Override
        public int hashCode() {
            return chrid;
        }

        @Override
        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SingleAttackerEntry other = (SingleAttackerEntry) obj;
            return chrid == other.chrid;
        }
    }

    private static final class ExpMap {

        public final int exp;
        public final byte ptysize;
        public final byte Class_Bonus_EXP;
        public final byte Premium_Bonus_EXP;

        public ExpMap(final int exp, final byte ptysize, final byte Class_Bonus_EXP, final byte Premium_Bonus_EXP) {
            super();
            this.exp = exp;
            this.ptysize = ptysize;
            this.Class_Bonus_EXP = Class_Bonus_EXP;
            this.Premium_Bonus_EXP = Premium_Bonus_EXP;
        }
    }

    private static final class OnePartyAttacker {

        public MapleParty lastKnownParty;
        public long damage;
        public long lastAttackTime;

        public OnePartyAttacker(final MapleParty lastKnownParty, final long damage) {
            super();
            this.lastKnownParty = lastKnownParty;
            this.damage = damage;
            this.lastAttackTime = System.currentTimeMillis();
        }
    }

    private class PartyAttackerEntry implements AttackerEntry {

        private long totDamage;
        private final Map<Integer, OnePartyAttacker> attackers = new HashMap<Integer, OnePartyAttacker>(6);
        private int partyid;
        private int channel;

        public PartyAttackerEntry(final int partyid, final int cserv) {
            this.partyid = partyid;
            this.channel = cserv;
        }

        public List<AttackingMapleCharacter> getAttackers() {
            final List<AttackingMapleCharacter> ret = new ArrayList<AttackingMapleCharacter>(attackers.size());
            for (final Entry<Integer, OnePartyAttacker> entry : attackers.entrySet()) {
                final MapleCharacter chr = map.getCharacterById(entry.getKey());
                if (chr != null) {
                    ret.add(new AttackingMapleCharacter(chr, entry.getValue().lastAttackTime));
                }
            }
            return ret;
        }

        private final Map<MapleCharacter, OnePartyAttacker> resolveAttackers() {
            final Map<MapleCharacter, OnePartyAttacker> ret = new HashMap<MapleCharacter, OnePartyAttacker>(attackers.size());
            for (final Entry<Integer, OnePartyAttacker> aentry : attackers.entrySet()) {
                final MapleCharacter chr = map.getCharacterById(aentry.getKey());
                if (chr != null) {
                    ret.put(chr, aentry.getValue());
                }
            }
            return ret;
        }

        @Override
        public final boolean contains(final MapleCharacter chr) {
            return attackers.containsKey(chr.getId());
        }

        @Override
        public final long getDamage() {
            return totDamage;
        }

        public void addDamage(final MapleCharacter from, final long damage, final boolean updateAttackTime) {
            final OnePartyAttacker oldPartyAttacker = attackers.get(from.getId());
            if (oldPartyAttacker != null) {
                oldPartyAttacker.damage += damage;
                oldPartyAttacker.lastKnownParty = from.getParty();
                if (updateAttackTime) {
                    oldPartyAttacker.lastAttackTime = System.currentTimeMillis();
                }
            } else {
                // TODO actually this causes wrong behaviour when the party changes between attacks
                // only the last setup will get exp - but otherwise we'd have to store the full party
                // constellation for every attack/everytime it changes, might be wanted/needed in the
                // future but not now
                final OnePartyAttacker onePartyAttacker = new OnePartyAttacker(from.getParty(), damage);
                attackers.put(from.getId(), onePartyAttacker);
                if (!updateAttackTime) {
                    onePartyAttacker.lastAttackTime = 0;
                }
            }
            totDamage += damage;
        }

        @Override
        public final void killedMob(final MapleMap map, final int baseExp, final boolean mostDamage, final int lastSkill) {
            MapleCharacter pchr, highest = null;
            long iDamage, highestDamage = 0;
            int iexp = 0;
            MapleParty party;
            double averagePartyLevel, expWeight, levelMod, innerBaseExp, expFraction;
            List<MapleCharacter> expApplicable;
            final Map<MapleCharacter, ExpMap> expMap = new HashMap<MapleCharacter, ExpMap>(6);
            byte Class_Bonus_EXP;
            byte Premium_Bonus_EXP;
            byte added_partyinc = 0;

            for (final Entry<MapleCharacter, OnePartyAttacker> attacker : resolveAttackers().entrySet()) {
                party = attacker.getValue().lastKnownParty;
                averagePartyLevel = 0;

                Class_Bonus_EXP = 0;
                Premium_Bonus_EXP = 0;
                expApplicable = new ArrayList<MapleCharacter>();
                for (final MaplePartyCharacter partychar : party.getMembers()) {
                    if (attacker.getKey().getLevel() - partychar.getLevel() <= 5 || stats.getLevel() - partychar.getLevel() <= 5) {
                        pchr = map.getCharacterById(partychar.getId());
                        if (pchr != null) {
                            if (pchr.isAlive() && pchr.getMap() == map) {
                                expApplicable.add(pchr);
                                averagePartyLevel += pchr.getLevel();

                                if (Class_Bonus_EXP == 0) {
                                    Class_Bonus_EXP = ServerConstants.Class_Bonus_EXP(pchr.getJob());
                                }
                                if (pchr.getStat().equippedWelcomeBackRing && Premium_Bonus_EXP == 0) {
                                    Premium_Bonus_EXP = 80;
                                }
                                if (pchr.getStat().装备许可证 && added_partyinc < 4) {
                                    added_partyinc++;
                                }
                            }
                        }
                    }
                }
                /*
                 * if (expApplicable.size() > 1) { averagePartyLevel /=
                 * expApplicable.size(); } else { Class_Bonus_EXP = 0; //no
                 * class bonus if not in a party. }
                 */

                iDamage = attacker.getValue().damage;
                if (iDamage > highestDamage) {
                    highest = attacker.getKey();
                    highestDamage = iDamage;
                }
                innerBaseExp = baseExp * ((double) iDamage / totDamage);
                //  expFraction = innerBaseExp / (expApplicable.size() + 1);
                double expBonus = 1.0;
                if (expApplicable.size() > 1) {
                    expBonus = 1.10 + 0.05 * expApplicable.size();
                    averagePartyLevel /= expApplicable.size();
                }
                expFraction = (innerBaseExp * expBonus) / (expApplicable.size() + 1);

                for (MapleCharacter expReceiver : expApplicable) {
                    Integer oexp = expMap.get(expReceiver) == null ? 0 : expMap.get(expReceiver).exp;
                    if (oexp == null) {
                        iexp = 0;
                    } else {
                        iexp = oexp.intValue();
                    }
                    expWeight = (expReceiver == attacker.getKey() ? 2.0 : 1.0);
                    levelMod = expReceiver.getLevel() / averagePartyLevel;
                    if (levelMod > 1.0 || this.attackers.containsKey(expReceiver.getId())) {
                        levelMod = 1.0;
                    }
                    iexp += (int) Math.round(expFraction * expWeight * levelMod);
                    expMap.put(expReceiver, new ExpMap(iexp, (byte) (expApplicable.size() + added_partyinc), Class_Bonus_EXP, Premium_Bonus_EXP));

                }
                /*
                 * for (final MapleCharacter expReceiver : expApplicable) { iexp
                 * = expMap.get(expReceiver) == null ? 0 :
                 * expMap.get(expReceiver).exp; expWeight = (expReceiver ==
                 * attacker.getKey() ? 2.0 : 0.3); //hopefully this is correct
                 * o.o -/+0.4 levelMod = expReceiver.getLevel() /
                 * averagePartyLevel * 0.4D; // levelMod =
                 * expReceiver.getLevel() / averagePartyLevel; // if (levelMod >
                 * 1.0 || attackers.containsKey(expReceiver.getId())) { ///
                 * levelMod = 1.0; // } iexp += (int)
                 * Math.round(((((MapleCharacter) attacker.getKey()).getId() ==
                 * expReceiver.getId() ? 0.6D : 0.0D) + levelMod) *
                 * innerBaseExp); //iexp += (int) Math.round(expWeight *
                 * innerBaseExp); expMap.put(expReceiver, new ExpMap(iexp,
                 * (byte) (expApplicable.size() + added_partyinc),
                 * Class_Bonus_EXP, Premium_Bonus_EXP)); }
                 */
            }
            ExpMap expmap;
            for (final Entry<MapleCharacter, ExpMap> expReceiver : expMap.entrySet()) {
                expmap = expReceiver.getValue();
                giveExpToCharacter(expReceiver.getKey(), expmap.exp, mostDamage ? expReceiver.getKey() == highest : false, expMap.size(), expmap.ptysize, expmap.Class_Bonus_EXP, expmap.Premium_Bonus_EXP, lastSkill);
            }
        }

        @Override
        public final int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + partyid;
            return result;
        }

        @Override
        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PartyAttackerEntry other = (PartyAttackerEntry) obj;
            if (partyid != other.partyid) {
                return false;
            }
            return true;
        }
    }

    public int getLinkOid() {
        return linkoid;
    }

    public void setLinkOid(int lo) {
        this.linkoid = lo;
    }

    public final Map<MonsterStatus, MonsterStatusEffect> getStati() {
        return stati;
    }

    public void addEmpty() {
        stati.put(MonsterStatus.EMPTY, new MonsterStatusEffect(MonsterStatus.EMPTY, 0, 0, null, false));
        stati.put(MonsterStatus.SUMMON, new MonsterStatusEffect(MonsterStatus.SUMMON, 0, 0, null, false));
    }

    public final int getStolen() {
        return stolen;
    }

    public final void setStolen(final int s) {
        this.stolen = s;
    }

    public final void 神通术(MapleCharacter chr) {//神通术
        double showdown = 100.0;
        final MonsterStatusEffect mse = getBuff(MonsterStatus.SHOWDOWN);
        if (mse != null) {
            showdown += mse.getX();
        }
        ISkill steal = SkillFactory.getSkill(4201004);
        final int level = chr.getSkillLevel(steal), chServerrate = ChannelServer.getInstance(chr.getClient().getChannel()).getDropRate();
        if (level > 0 && !getStats().isBoss() && stolen == -1 && steal.getEffect(level).makeChanceResult()) {
            final MapleMonsterInformationProvider mi = MapleMonsterInformationProvider.getInstance();//神通术偷全局爆物
            final List<MonsterDropEntry> de = mi.retrieveDrop(getId());//神通术偷怪物爆物
            if (de == null) {
                stolen = 0;
                return;
            }
            final List<MonsterDropEntry> dropEntry = new ArrayList<>(de);
            Collections.shuffle(dropEntry);
            IItem idrop;
            for (MonsterDropEntry d : dropEntry) {
                if (d.itemId > 0 && d.questid == 0 && d.itemId / 10000 != 238 && Randomizer.nextInt(999999) < (int) (10 * d.chance * chServerrate * chr.getDropMod() * (chr.getStat().dropBuff / 100.0) * (showdown / 100.0))) { //kinda op
                    if (GameConstants.getInventoryType(d.itemId) == MapleInventoryType.EQUIP) {
                        Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(d.itemId);
                        idrop = MapleItemInformationProvider.getInstance().randomizeStats(eq);
                    } else {
                        idrop = new Item(d.itemId, (byte) 0, (short) (d.Maximum != 1 ? Randomizer.nextInt(d.Maximum - d.Minimum) + d.Minimum : 1), (byte) 0);
                    }
                    stolen = d.itemId;
                    map.spawnMobDrop(idrop, map.calcDropPos(getPosition(), getTruePosition()), this, chr, (byte) 0, (short) 0);
                    break;
                }
            }
        } else {
            stolen = 0; //failed once, may not go again
        }
    }

    public final void 探云手(MapleCharacter chr) {//神通术
        double showdown = 100.0;
        final MonsterStatusEffect mse = getBuff(MonsterStatus.SHOWDOWN);
        if (mse != null) {
            showdown += mse.getX();
        }
        ISkill steal = SkillFactory.getSkill(4201004);
        final int level = chr.getSkillLevel(steal), chServerrate = ChannelServer.getInstance(chr.getClient().getChannel()).getDropRate();
        if (level > 0 && !getStats().isBoss() && stolen == -1 && steal.getEffect(level).makeChanceResult()) {
            final MapleMonsterInformationProvider mi = MapleMonsterInformationProvider.getInstance();//神通术偷全局爆物
            final List<MonsterDropEntry> de = mi.retrieveDrop(getId());//神通术偷怪物爆物
            if (de == null) {
                stolen = 0;
                return;
            }
            final List<MonsterDropEntry> dropEntry = new ArrayList<>(de);
            Collections.shuffle(dropEntry);
            IItem idrop;
            for (MonsterDropEntry d : dropEntry) {
                if (d.itemId > 0 && d.questid == 0 && d.itemId / 10000 != 238 && Randomizer.nextInt(999999) < (int) (10 * d.chance * chServerrate * chr.getDropMod() * (chr.getStat().dropBuff / 100.0) * (showdown / 100.0))) { //kinda op
                    if (GameConstants.getInventoryType(d.itemId) == MapleInventoryType.EQUIP) {
                        Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(d.itemId);
                        idrop = MapleItemInformationProvider.getInstance().randomizeStats(eq);
                    } else {
                        idrop = new Item(d.itemId, (byte) 0, (short) (d.Maximum != 1 ? Randomizer.nextInt(d.Maximum - d.Minimum) + d.Minimum : 1), (byte) 0);
                    }
                    stolen = d.itemId;
                    map.spawnMobDrop(idrop, map.calcDropPos(getPosition(), getTruePosition()), this, chr, (byte) 0, (short) 0);
                    break;
                }
            }
        } else {
            stolen = 0; //failed once, may not go again
        }
    }

    public final void setLastNode(final int lastNode) {
        this.lastNode = lastNode;
    }

    public final int getLastNode() {
        return lastNode;
    }

    public final void setLastNodeController(final int lastNode) {
        this.lastNodeController = lastNode;
    }

    public final int getLastNodeController() {
        return lastNodeController;
    }

    public final void cancelStatus(final MonsterStatus stat) {
        final MonsterStatusEffect mse = stati.get(stat);
        if (mse == null || !isAlive()) {
            return;
        }
        mse.cancelPoisonSchedule();
        map.broadcastMessage(MobPacket.cancelMonsterStatus(getObjectId(), stat), getPosition());
        if (getController() != null && !getController().isMapObjectVisible(MapleMonster.this)) {
            getController().getClient().sendPacket(MobPacket.cancelMonsterStatus(getObjectId(), stat));
        }
        stati.remove(stat);
        setVenomMulti((byte) 0);
    }

    public final void cancelDropItem() {
        if (dropItemSchedule != null) {
            dropItemSchedule.cancel(false);
            dropItemSchedule = null;
        }
    }

    //怪物指定掉落？自动掉落？？指定掉落！
    public final void startDropItemSchedule() {
        cancelDropItem();
        if (stats.getDropItemPeriod() <= 0 || !isAlive()) {
            return;
        }
        final int itemId;
        switch (getId()) {
            case 9300061://月妙
                itemId = 4001101;//月妙的年糕
                break;
            case 9300102://护卫用小浣猪
                itemId = 4031507;//费洛蒙
                break;

            default: //直到我们发现…还有什么其他的怪物使用该如何得到对应
                return;
        }
        shouldDropItem = false;
        dropItemSchedule = MobTimer.getInstance().register(new Runnable() {

            public void run() {
                if (isAlive() && map != null) {
                    if (shouldDropItem) {
                        map.spawnAutoDrop(itemId, getPosition());
                    } else {
                        shouldDropItem = true;
                    }
                }
            }
        }, stats.getDropItemPeriod() * 1000);
    }

    public byte[] getNodePacket() {
        return nodepack;
    }

    public void setNodePacket(final byte[] np) {
        this.nodepack = np;
    }

    public final void killed() {
        if (listener != null) {
            listener.monsterKilled();
        }
        listener = null;
    }

    public final void changeLevel(final int newLevel, boolean pqMob) {
        this.ostats = new ChangeableStats(stats, newLevel, pqMob);
        this.hp = ostats.getHp();
        this.mp = ostats.getMp();
    }
}

/*if (status.getStati().name().equals("FREEZE")) {
                    from.dropMessage(5, "怪物: " + getId() + " 状态: 冰冻  是否中毒: " + poison + " 持续时间: " + duration);
                } else if (status.getStati().name().equals("POISON")) {
                    from.dropMessage(5, "怪物: " + getId() + " 状态: 中毒 是否中毒： " + poison + " 持续时间: " + duration);
                } else {
                    from.dropMessage(5, "怪物: " + getId() + " 状态: " + status.getStati().name() + " 是否中毒: " + poison + " 持续时间: " + duration);
                }*/
