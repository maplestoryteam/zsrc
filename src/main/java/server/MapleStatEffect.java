package server;

import client.*;
import client.inventory.IItem;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import handling.channel.ChannelServer;
import provider.MapleData;
import provider.MapleDataTool;
import server.MapleCarnivalFactory.MCSkill;
import server.Timer.BuffTimer;
import server.life.MapleMonster;
import server.maps.*;
import tools.MaplePacketCreator;
import tools.Pair;

import java.awt.*;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

public class MapleStatEffect implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private byte mastery, mhpR, mmpR, mobCount, attackCount, bulletCount;
    private short hp, mp, watk, matk, wdef, mdef, acc, avoid, hands, speed, jump, mpCon, hpCon, damage, prop, ehp, emp, ewatk, ewdef, emdef;
    private double hpR, mpR;
    private int duration, sourceid, moveTo, x, y, z, itemCon, itemConNo, bulletConsume, moneyCon, cooldown, morphId = 0, expinc;
    private boolean overTime, skill, partyBuff = true;
    private List<Pair<MapleBuffStat, Integer>> statups;
    private Map<MonsterStatus, Integer> monsterStatus;
    private Point lt, rb;
    private int expBuff, itemup, mesoup, cashup, berserk, illusion, booster, berserk2, cp, nuffSkill;
    private byte level;
//    private List<Pair<Integer, Integer>> randomMorph;
    private List<MapleDisease> cureDebuffs;

    public static final MapleStatEffect loadSkillEffectFromData(final MapleData source, final int skillid, final boolean overtime, final byte level) {
        return loadFromData(source, skillid, true, overtime, level);
    }

    public static final MapleStatEffect loadItemEffectFromData(final MapleData source, final int itemid) {
        return loadFromData(source, itemid, false, false, (byte) 1);
    }

    private static final void addBuffStatPairToListIfNotZero(final List<Pair<MapleBuffStat, Integer>> list, final MapleBuffStat buffstat, final Integer val) {
        if (val.intValue() != 0) {
            list.add(new Pair<MapleBuffStat, Integer>(buffstat, val));
        }
    }

    private static MapleStatEffect loadFromData(final MapleData source, final int sourceid, final boolean skill, final boolean overTime, final byte level) {
        final MapleStatEffect ret = new MapleStatEffect();
        ret.sourceid = sourceid;
        ret.skill = skill;
        ret.level = level;
        if (source == null) {
            return ret;
        }
        ret.duration = MapleDataTool.getIntConvert("time", source, -1);
        ret.hp = (short) MapleDataTool.getInt("hp", source, 0);
        ret.hpR = MapleDataTool.getInt("hpR", source, 0) / 100.0;
        ret.mp = (short) MapleDataTool.getInt("mp", source, 0);
        ret.mpR = MapleDataTool.getInt("mpR", source, 0) / 100.0;
        ret.mhpR = (byte) MapleDataTool.getInt("mhpR", source, 0);
        ret.mmpR = (byte) MapleDataTool.getInt("mmpR", source, 0);
        ret.mpCon = (short) MapleDataTool.getInt("mpCon", source, 0);
        ret.hpCon = (short) MapleDataTool.getInt("hpCon", source, 0);
        ret.prop = (short) MapleDataTool.getInt("prop", source, 100);
        ret.cooldown = MapleDataTool.getInt("cooltime", source, 0);
        ret.expinc = MapleDataTool.getInt("expinc", source, 0);
        ret.morphId = MapleDataTool.getInt("morph", source, 0);
        ret.cp = MapleDataTool.getInt("cp", source, 0);
        ret.nuffSkill = MapleDataTool.getInt("nuffSkill", source, 0);
        ret.mobCount = (byte) MapleDataTool.getInt("mobCount", source, 1);

        if (skill) {
            switch (sourceid) {
                case 1100002://终极剑
                case 1100003://终极斧
                case 1200002://终极剑
                case 1200003://终极钝器
                case 1300002://终极枪
                case 1300003://终极矛
                case 3100001://终极弓
                case 3200001://终极弩
                case 11101002://终极剑
                case 13101002://终极弓
                    ret.mobCount = 6;
                    break;
            }
        }

        /*
         * final MapleData randMorph = source.getChildByPath("morphRandom"); if
         * (randMorph != null) { for (MapleData data : randMorph.getChildren())
         * { ret.randomMorph.add(new Pair( MapleDataTool.getInt("morph", data,
         * 0), MapleDataTool.getIntConvert("prop", data, 0))); } }
         */
        if (!ret.skill && ret.duration > -1) {
            ret.overTime = true;
        } else {
            ret.duration *= 1000; // items have their times stored in ms, of course
            ret.overTime = overTime || ret.isMorph() || ret.isPirateMorph() || ret.isFinalAttack();
        }
        final ArrayList<Pair<MapleBuffStat, Integer>> statups = new ArrayList<Pair<MapleBuffStat, Integer>>();

        ret.mastery = (byte) MapleDataTool.getInt("mastery", source, 0);
        ret.watk = (short) MapleDataTool.getInt("pad", source, 0);
        ret.wdef = (short) MapleDataTool.getInt("pdd", source, 0);
        ret.matk = (short) MapleDataTool.getInt("mad", source, 0);
        ret.mdef = (short) MapleDataTool.getInt("mdd", source, 0);
        ret.ehp = (short) MapleDataTool.getInt("emhp", source, 0);
        ret.emp = (short) MapleDataTool.getInt("emmp", source, 0);
        ret.ewatk = (short) MapleDataTool.getInt("epad", source, 0);
        ret.ewdef = (short) MapleDataTool.getInt("epdd", source, 0);
        ret.emdef = (short) MapleDataTool.getInt("emdd", source, 0);
        ret.acc = (short) MapleDataTool.getIntConvert("acc", source, 0);
        ret.avoid = (short) MapleDataTool.getInt("eva", source, 0);
        ret.speed = (short) MapleDataTool.getInt("speed", source, 0);
        ret.jump = (short) MapleDataTool.getInt("jump", source, 0);
        ret.expBuff = MapleDataTool.getInt("expBuff", source, 0);
        ret.cashup = MapleDataTool.getInt("cashBuff", source, 0);
        ret.itemup = MapleDataTool.getInt("itemupbyitem", source, 0);
        ret.mesoup = MapleDataTool.getInt("mesoupbyitem", source, 0);
        ret.berserk = MapleDataTool.getInt("berserk", source, 0);
        ret.berserk2 = MapleDataTool.getInt("berserk2", source, 0);
        ret.booster = MapleDataTool.getInt("booster", source, 0);
        ret.illusion = MapleDataTool.getInt("illusion", source, 0);

        List<MapleDisease> cure = new ArrayList<MapleDisease>(5);
        if (MapleDataTool.getInt("poison", source, 0) > 0) {
            cure.add(MapleDisease.POISON);
        }
        if (MapleDataTool.getInt("seal", source, 0) > 0) {
            cure.add(MapleDisease.SEAL);
        }
        if (MapleDataTool.getInt("darkness", source, 0) > 0) {
            cure.add(MapleDisease.DARKNESS);
        }
        if (MapleDataTool.getInt("weakness", source, 0) > 0) {
            cure.add(MapleDisease.WEAKEN);
        }
        if (MapleDataTool.getInt("curse", source, 0) > 0) {
            cure.add(MapleDisease.CURSE);
        }
        ret.cureDebuffs = cure;

        final MapleData ltd = source.getChildByPath("lt");
        if (ltd != null) {
            ret.lt = (Point) ltd.getData();
            ret.rb = (Point) source.getChildByPath("rb").getData();
        }

        ret.x = MapleDataTool.getInt("x", source, 0);
        ret.y = MapleDataTool.getInt("y", source, 0);
        ret.z = MapleDataTool.getInt("z", source, 0);
        ret.damage = (short) MapleDataTool.getIntConvert("damage", source, 100);
        ret.attackCount = (byte) MapleDataTool.getIntConvert("attackCount", source, 1);
        ret.bulletCount = (byte) MapleDataTool.getIntConvert("bulletCount", source, 1);
        ret.bulletConsume = MapleDataTool.getIntConvert("bulletConsume", source, 0);
        ret.moneyCon = MapleDataTool.getIntConvert("moneyCon", source, 0);

        ret.itemCon = MapleDataTool.getInt("itemCon", source, 0);
        ret.itemConNo = MapleDataTool.getInt("itemConNo", source, 0);
        ret.moveTo = MapleDataTool.getInt("moveTo", source, -1);

        Map<MonsterStatus, Integer> monsterStatus = new EnumMap<MonsterStatus, Integer>(MonsterStatus.class);
        if (ret.overTime && ret.getSummonMovementType() == null) {
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.WATK, Integer.valueOf(ret.watk));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.WDEF, Integer.valueOf(ret.wdef));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MATK, Integer.valueOf(ret.matk));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MDEF, Integer.valueOf(ret.mdef));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ACC, Integer.valueOf(ret.acc));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.AVOID, Integer.valueOf(ret.avoid));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.SPEED, Integer.valueOf(ret.speed));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.JUMP, Integer.valueOf(ret.jump));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MAXHP, (int) ret.mhpR);
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MAXMP, (int) ret.mmpR);
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.EXPRATE, Integer.valueOf(ret.expBuff)); // EXP
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ACASH_RATE, Integer.valueOf(ret.cashup)); // custom
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.DROP_RATE, Integer.valueOf(ret.itemup * 200)); // defaults to 2x
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MESO_RATE, Integer.valueOf(ret.mesoup * 200)); // defaults to 2x
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.BERSERK_FURY, Integer.valueOf(ret.berserk2));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.PYRAMID_PQ, Integer.valueOf(ret.berserk));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.BOOSTER, Integer.valueOf(ret.booster));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ILLUSION, Integer.valueOf(ret.illusion));

            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_WATK, Integer.valueOf(ret.ewatk));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_WDEF, Integer.valueOf(ret.ewdef));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_MDEF, Integer.valueOf(ret.emdef));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_MAXHP, Integer.valueOf(ret.ehp));
            addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_MAXMP, Integer.valueOf(ret.ehp));
        }

        if (skill) { // hack because we can't get from the datafile...
            switch (sourceid) {

                case 2001002: // 魔法盾

                    //statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DRAGONBLOOD, ret.x));
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAGIC_GUARD, ret.x));
                    break;
                case 12001001:// 魔法盾
                case 22111001:// 

                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAGIC_GUARD, ret.x));

                    break;
                case 2301003: // 神之保护
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.INVINCIBLE, ret.x));
                    break;
                case 2001003: // 魔法铠甲
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.INVINCIBLE, ret.y));
                    //statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAXMP, ret.x));

                    break;

                /*case 35120000:
                case 35001002: //TEMP. mech
                    ret.duration = 60 * 120 * 1000;
                    break;*/
                case 9001004: // 管理员隐身术

                    ret.duration = 60 * 120 * 1000;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DARKSIGHT, ret.x));

                case 5101007: //橡木伪装   

                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DARKSIGHT, ret.x));
                    break;
                case 13101006: // 风影疾步
                case 4001003: // 隐身术
                case 14001003: //隐身术
                case 4330001://进阶隐身术
                case 30001001: //潜入
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DARKSIGHT, ret.x));//技能解除
                    break;

                case 4211003: // 敛财术
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.PICKPOCKET, ret.x));
                    break;
                case 4211005: //金钱护盾
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MESOGUARD, ret.x));
                    break;
                case 4111001: // 聚财术
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MESOUP, ret.x));
                    break;
                case 4111002: // 影分身
                case 14111000: // 影分身
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SHADOWPARTNER, ret.x));
                    break;
                case 11101002: // 终极剑
                case 13101002: //终极弓
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.FINALATTACK, ret.x));
                    break;
                case 3101004: //无形箭
                case 3201004://无形箭:弩
                case 2311002: //时空之门
                case 13101003: //无形箭
                case 33101003: //无形箭：弩
                case 8001:
                    /*case 10008001:
                case 20008001:
                case 20018001:
                case 30008001:*/
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SOULARROW, ret.x));
                    break;
                case 1211006://寒冰钝器
                case 1211003://烈焰之剑
                case 1211004://烈焰钝器
                case 1211005://寒冰之剑
                case 1211008://雷电钝器
                case 1211007://雷电之剑
                case 1221003://圣灵之剑
                case 1221004://圣灵之锤
                case 11111007://灵魂属性
                case 21111005://冰雪矛
                case 15101006://雷鸣
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.WK_CHARGE, ret.x));
                    break;
                case 12101005://自然力重置
                    //case 22121001: // Elemental Reset
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.ELEMENT_RESET, ret.x));
                    break;
                case 3121008://集中精力
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.CONCENTRATE, ret.x));
                    break;
                case 5110001: //能量获得
                case 15100004://能量获得
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.ENERGY_CHARGE, 0));
                    break;
                case 1101005: // 快速武器
                case 1101004:
                case 1201005:
                case 1201004:
                case 1301005:
                case 1301004:
                case 3101002:
                case 3201002:
                case 4101003:
                case 4201002:
                case 2111005:
                case 2211005:
                case 5101006:
                case 5201003:
                case 11101001:
                case 12101004:
                case 13101001:
                case 14101002:
                case 15101002:
                case 21001003:
                case 22141002:
                case 4301002:
                case 32101005:
                case 33001003:
                case 35101006:
                case 35001003:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.BOOSTER, ret.x));
                    break;
                case 5121009://极速领域
                case 15111005://极速领域
                    statups.add(new Pair<>(MapleBuffStat.攻击加速, ret.x));
                    /*statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SPEED_INFUSION, ret.x));
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.BOOSTER, ret.x));*/
                    break;
                /* case 4321000: //tornado spin uses same buffstats
                    ret.duration = 1000;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DASH_SPEED, 100 + ret.x));
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DASH_JUMP, ret.y)); //always 0 but its there
                    break;*/
                case 5001005://疾驰
                case 15001003://疾驰
                case 2101002:
                    ret.duration = 5000;
                    statups.add(new Pair<>(MapleBuffStat.疾驰移动, ret.x));
                    statups.add(new Pair<>(MapleBuffStat.疾驰跳跃, ret.y));
                    /*statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DASH_SPEED, ret.x * 2));
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DASH_JUMP, ret.y * 2));*/
                    break;
                case 1101007: //伤害反击
                case 1201007: //伤害反击
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.POWERGUARD, ret.x));

                    break;
                case 1301007: // 神圣之火
                case 9001008: // 神圣之火

                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAXHP, ret.x));
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAXMP, ret.y));
                    break;
                case 1001: // 团队治疗
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.RECOVERY, ret.x));
                    break;
                case 1111002: // 斗气集中
                case 11111001: // 斗气集中
                case 1211009:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.COMBO, 1));
                    break;
                case 21120007: //战神之盾
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.COMBO_BARRIER, ret.x));
                    break;
                case 5211006: //导航
                case 5220011: //导航辅助
                    // case 22151002: //killer wings
                    ret.duration = 60 * 120000;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.HOMING_BEACON, ret.x));
                    break;
                case 1011: //狂暴战魂
                case 10001011://狂暴战魂
                case 20001011://狂暴战魂
                case 20011011://狂暴战魂
                    // case 30001011://狂暴战魂
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.BERSERK_FURY, 1));
                    break;
                case 1010://金刚霸体
                case 10001010://金刚霸体
                case 20001010://金刚霸体
                case 20011010://金刚霸体
                    //case 30001010:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DIVINE_BODY, 1));
                    break;
                case 1311006: //龙咆哮
                    ret.hpR = -ret.x / 100.0;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DRAGON_ROAR, ret.y));
                    break;
                /*  case 4341007:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.THORNS, ret.x << 8 | ret.y));
                    break;
                case 4341002:
                    ret.duration = 60 * 1000;
                    ret.hpR = -ret.x / 100.0;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.FINAL_CUT, ret.y));
                    break;
                case 4331002:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MIRROR_IMAGE, ret.x));
                    break;
                case 4331003:
                    ret.duration = 60 * 1000;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.OWL_SPIRIT, ret.y));
                    break;*/

                case 1311008: //龙之魂

                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DRAGONBLOOD, ret.x));
                    break;
                case 1121000: // 冒险岛勇士
                case 1221000:
                case 1321000:
                case 2121000:
                case 2221000:
                case 2321000:
                case 3121000:
                case 3221000:
                case 4121000:
                case 4221000:
                case 5121000:
                case 5221000:
                case 21121000:
                case 22171000:
                case 4341000:
                case 32121007:
                case 33121007:
                case 35121007:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAPLE_WARRIOR, ret.x));
                    break;
                case 15111006: //闪光击
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SPARK, ret.x));
                    break;
                case 3121002: // 火眼晶晶
                case 3221002: // 火眼晶晶
                    // case 33121004:
                    // case 8002:
                    // case 10008002:
                    // case 20008002:
                    // case 20018002:
                    // case 30008002:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SHARP_EYES, ret.x << 8 | ret.y));
                    break;
                /* case 22151003: //magic resistance
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAGIC_RESISTANCE, ret.x));
                    break;*/
                case 21101003: // 抗压
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.BODY_PRESSURE, ret.x));
                    break;
                case 21000000: // 矛连击强化
                    //statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.ARAN_COMBO, ret.x));
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SHARP_EYES, ret.x | ret.y));
                    break;
                case 21100005: //连环吸血
                    // case 32101004:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.COMBO_DRAIN, ret.x));
                    break;
                case 21111001: //灵巧击退
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SMART_KNOCKBACK, ret.x));
                    break;
                /*case 22131001: //magic shield
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAGIC_SHIELD, ret.x));
                    break;
                case 22181003: //soul stone
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SOUL_STONE, 1));
                    break;*/
                case 4001002: //诅咒术
                case 14001002: // 诅咒术
                    monsterStatus.put(MonsterStatus.WATK, ret.x);
                    monsterStatus.put(MonsterStatus.WDEF, ret.y);
                    break;
                case 5221009: //心灵控制
                    monsterStatus.put(MonsterStatus.HYPNOTIZE, 1);
                    break;
                case 1201006: //压制术
                    monsterStatus.put(MonsterStatus.SHOWDOWN, ret.x);
                    monsterStatus.put(MonsterStatus.WATK, ret.x);
                    monsterStatus.put(MonsterStatus.WDEF, ret.y);
                    break;
                case 1211002: // 属性攻击
                case 1111008: // 虎咆哮
                case 4211002: // 炼狱
                case 3101005: // 爆炸箭
                case 1111005: // 昏迷
                case 1111006: // 气绝斧
                case 4221007: //一出双击
                case 5101002: // 回马
                // case 5101003: //升龙连击
                case 5121004: // 金手指
                case 5121005: // 索命
                case 5121007: // 光速拳
                case 5201004: // 迷惑射击
                case 4121008: // 忍者冲击
                case 22151001:
                case 4201004: //steal, new
                // case 33101001:
                // case 33101002:
                // case 32111010:
                // case 32121004:
                // case 33111002:
                // case 33121002:
                // case 35101003:
                // case 35111015:
                case 5111002: //能量爆破
                case 15101005: //能量爆破
                    // case 4331005:
                    monsterStatus.put(MonsterStatus.STUN, 1);//昏迷，不能动
                    break;
                case 4321002://闪光弹
                    monsterStatus.put(MonsterStatus.DARKNESS, 1);//黑暗miss
                    break;
                case 4221003://挑衅
                case 4121003://挑衅
                case 33121005://神经毒气

                    monsterStatus.put(MonsterStatus.SHOWDOWN, ret.x);
                    monsterStatus.put(MonsterStatus.MDEF, ret.x);
                    monsterStatus.put(MonsterStatus.WDEF, ret.x);
                    break;

                case 2201004: // 冰冻术
                case 2211002: //冰咆哮
                case 3211003: // 寒冰箭
                case 2211006: // 冰凤球
                case 2221007: // 落霜冰破
                case 5211005: // 寒冰喷射
                case 2121006: // 美杜莎之眼
                case 21120006: // 钻石星辰
                case 22121000: //冰点寒气
                    //if (server.Start.ConfigValuesMap.get("冰冻效果开关") <= 0) {
                    monsterStatus.put(MonsterStatus.STUN, 1);//昏迷，不能动
                    // }
                    //monsterStatus.put(MonsterStatus.FREEZE, Integer.valueOf(1));//冰冻效果
                    //ret.duration *= 2; // freezing skills are a little strange
                    break;
                case 2101003: // 缓速术
                case 2201003: // 缓速术
                case 12101001:// 缓速术
                case 22141003: // 缓速术
                    monsterStatus.put(MonsterStatus.SPEED, ret.x);//缓速效果
                    break;
                case 2101005: // 毒雾术
                // case 2111006: // 火凤球
                // case 2121003: // 迷雾爆发
                // case 2221003: // 冰河锁链
                //  case 3111003: //烈焰箭
                case 22161002: //鬼刻符
                    monsterStatus.put(MonsterStatus.POISON, 1);//中毒效果
                    break;
                case 4121004: //忍者伏击
                case 4221004: //忍者伏击
                    monsterStatus.put(MonsterStatus.NINJA_AMBUSH, (int) ret.damage);
                    break;
                case 2311005: //巫毒术
                    monsterStatus.put(MonsterStatus.DOOM, 1);
                    break;
                case 32111006://重生
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.REAPER, 1));
                    break;
                case 4341006: //傀儡召唤
                case 3111002: // 替身术
                case 3211002: // 替身术
                case 13111004: // 替身术
                case 5211001: //章鱼炮台
                case 5220002: // 超级章鱼炮台
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.PUPPET, 1));
                    break;
                case 3211005: // 金鹰召唤
                case 3111005: // 银鹰召唤
                    // case 33111005: //银鹰
                    // case 35111002: //磁场
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SUMMON, 1));
                    monsterStatus.put(MonsterStatus.STUN, Integer.valueOf(1));
                    break;
                case 3221005: // 冰凤凰
                case 2121005: // 冰破魔兽
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SUMMON, 1));
                    monsterStatus.put(MonsterStatus.FREEZE, Integer.valueOf(1));
                    break;
                case 2311006: // 魔法狂暴
                case 3121006: // 魔法狂暴
                case 2221005: // 火魔兽
                case 2321003: // 强化圣龙
                case 1321007: // 灵魂助力
                case 5211002: // 海鸥空袭
                case 11001004:// 魂精灵
                case 12001004:// 炎精灵
                case 12111004:// 火魔兽
                case 13001004:// 风精灵
                case 14001005:// 夜精灵
                case 15001004:// 雷精灵
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SUMMON, 1));
                    break;
                case 2311003: // 神圣祈祷
                case 9001002: // 圣化之力
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.HOLY_SYMBOL, ret.x));
                    break;
                case 2211004: // 封印术
                case 2111004: // 封印术
                case 12111002: // 封印术
                    monsterStatus.put(MonsterStatus.SEAL, 1);
                    break;
                case 4111003: // 影网术
                case 14111001: // 影网术
                    monsterStatus.put(MonsterStatus.SHADOW_WEB, 1);
                    break;
                case 4121006: //暗器伤人
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SPIRIT_CLAW, 0));
                    break;
                case 2121004://终极无限
                case 2221004://终极无限
                case 2321004://终极无限
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.INFINITY, ret.x));
                    break;
                case 1121002://稳如泰山
                case 1221002://稳如泰山
                case 1321002://稳如泰山
                case 21121003://战神的意志
                    //case 32121005://稳如泰山
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.STANCE, (int) ret.prop));
                    break;
                case 1005: //英雄之回声
                case 10001005: //英雄之回声
                case 20001005: // 英雄的回声
                    // case 20011005: 
                    //case 30001005:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.ECHO_OF_HERO, ret.x));
                    break;
                /* case 1026: // Soaring
                case 10001026: // Soaring
                case 20001026: // Soaring
                case 20011026: // Soaring
                case 30001026:
                    ret.duration = 60 * 120 * 1000; //because it seems to dispel asap.
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SOARING, 1));
                    break;*/
                case 2121002://魔法反击
                case 2221002://魔法反击
                case 2321002://魔法反击
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MANA_REFLECTION, 1));
                    break;
                case 2321005: //圣灵之盾
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.HOLY_SHIELD, ret.x));
                    break;
                case 3121007: //击退箭
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.HAMSTRING, ret.x));
                    monsterStatus.put(MonsterStatus.SPEED, ret.x);
                    break;
                case 3221006: //刺眼箭
                    //case 33111004:
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.BLIND, ret.x));
                    monsterStatus.put(MonsterStatus.ACC, ret.x);
                    break;
                /*  case 33121006: //feline berserk
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAXHP, ret.x));
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.WATK, ret.y));//temp
                    //statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DASH_SPEED, ret.z));
                    break;
                case 32001003: //dark aura
                case 32120000:
                    ret.duration = 60 * 120 * 1000; //because it seems to dispel asap.
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DARK_AURA, ret.x));
                    break;
                case 32101002: //blue aura
                case 32110000:
                    ret.duration = 60 * 120 * 1000; //because it seems to dispel asap.
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.BLUE_AURA, ret.x));
                    break;
                case 32101003: //yellow aura
                case 32120001:
                    ret.duration = 60 * 120 * 1000; //because it seems to dispel asap.
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.YELLOW_AURA, ret.x));
                    break;
                case 33101004: //it's raining mines
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.RAINING_MINES, ret.x)); //x?
                    break;
                case 35101007: //perfect armor
                    ret.duration = 60 * 120 * 1000;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.PERFECT_ARMOR, ret.x));
                    break;
                case 35121006: //satellite safety
                    ret.duration = 60 * 120 * 1000;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SATELLITESAFE_PROC, ret.x));
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SATELLITESAFE_ABSORB, ret.y));
                    break;
                case 35001001: //flame
                case 35101009:
                case 35111007: //TEMP
                    //pre-bb = 35111007,
                    ret.duration = 8000;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MECH_CHANGE, (int) level)); //ya wtf
                    break;
                case 35121013:
                //case 35111004: //siege
                case 35101002: //TEMP
                    ret.duration = 5000;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MECH_CHANGE, (int) level)); //ya wtf
                    break;
                case 35121005: //missile
                    ret.duration = 60 * 120 * 1000;
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MECH_CHANGE, (int) level)); //ya wtf
                    break;
                default:
                    break;*/
            }
        }
        if (ret.isMonsterRiding()) {
            statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MONSTER_RIDING, 1));
        }
        if (ret.isMorph() || ret.isPirateMorph()) {
            statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MORPH, ret.getMorph()));
        }
        ret.monsterStatus = monsterStatus;
        statups.trimToSize();
        ret.statups = statups;

        return ret;
    }
    public void applyPassive(MapleCharacter applyto, MapleMapObject obj) {
        if (makeChanceResult()) {
            switch (sourceid) { // MP eater
                case 2100000://- 魔力吸收 - [最高等级 : 20]\n使用魔法攻击时，吸收怪物的MP。怪物的MP为0时无效.
                case 2200000://- 魔力吸收 - [最高等级 : 20]\n使用魔法攻击时，吸收怪物的MP。怪物的MP为0时无效.
                case 2300000://- 魔力吸收 - [最高等级 : 20]\n使用魔法攻击时，吸收怪物的MP。怪物的MP为0时无效.
                    if (obj == null || obj.getType() != MapleMapObjectType.MONSTER) {
                        /*如果对象为空或者对象的类型不是怪物就直接返回*/
                        return;
                    }
                    MapleMonster mob = (MapleMonster) obj; // x is absorb percentage
                    /*取当前怪物的状态信息，判断是否为BOSS*/
                    if (!mob.getStats().isBoss()) {
                        /* absorbMp 吸收MP的计算方法：技能X值除以100乘以怪物的最大MP，得到的结果如果小于或等于怪物当前的MP，就赋值给 absorbMp 反之 将怪物当前的mp赋值给 absorbMp*/
                        int absorbMp = Math.min((int) (mob.getMobMaxMp() * (getX() / 100.0)), mob.getMp());
                        /* 判断吸收MP的结果值是否大于0*/
                        if (absorbMp > 0) {
                            /*设置怪物当前的MP：怪物当前的MP减去被吸收的MP。*/
                            mob.setMp(mob.getMp() - absorbMp);
                            /*设置角色当前的MP：角色当前的MP加上吸收到的MP*/
                            applyto.getStat().setMp((short) (applyto.getStat().getMp() + absorbMp));
                            /*发送给角色吸收MP的效果包*/
                            applyto.getClient().sendPacket(MaplePacketCreator.showOwnBuffEffect(sourceid, 1));
                            /*发送给角色当前所在地图其他玩家的效果广播包*/
                            applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.showBuffeffect(applyto.getId(), sourceid, 1), false);
                        }
                    }
                    break;
            }
        }
    }

    public final boolean applyTo(MapleCharacter chr) {
        return applyTo(chr, chr, true, null, duration);
    }

    public final boolean applyTo(MapleCharacter chr, Point pos) {
        return applyTo(chr, chr, true, pos, duration);
    }

    private final boolean applyTo(final MapleCharacter applyfrom, final MapleCharacter applyto, final boolean primary, final Point pos) {
        return applyTo(applyfrom, applyto, primary, pos, duration);
    }

    //最终伤害
    public final boolean applyTo(final MapleCharacter applyfrom, final MapleCharacter applyto, final boolean primary, final Point pos, int newDuration) {
        //群体治愈,纯净雪人栖息地
        if (isHeal() && (applyfrom.getMapId() == 749040100 || applyto.getMapId() == 749040100)) {
            return false; //z

        }
        int hpchange = calcHPChange(applyfrom, primary);
        int mpchange = calcMPChange(applyfrom, primary);

        final PlayerStats stat = applyto.getStat();
        if (primary) {
            if (itemConNo != 0 && !applyto.isClone()) {
                MapleInventoryManipulator.removeById(applyto.getClient(), GameConstants.getInventoryType(itemCon), itemCon, itemConNo, false, true);
            }
        } else if (!primary && isResurrection()) {
            hpchange = stat.getMaxHp();
            applyto.setStance(0); //修复死亡bug，玩家不会在其他屏幕上产生
        }
        if (isDispel() && makeChanceResult()) {
            applyto.dispelDebuffs();
        } else if (isHeroWill()) {
            applyto.dispelDebuff(MapleDisease.SEDUCE);
        } else if (cureDebuffs.size() > 0) {
            for (final MapleDisease debuff : cureDebuffs) {
                applyfrom.dispelDebuff(debuff);
            }
        } else if (isMPRecovery()) {
            final int toDecreaseHP = ((stat.getMaxHp() / 100) * 10);
            if (stat.getHp() > toDecreaseHP) {
                hpchange += -toDecreaseHP; //
                mpchange += ((toDecreaseHP / 100) * getY());
            } else {
                hpchange = stat.getHp() == 1 ? 0 : stat.getHp() - 1;
            }
        }
        final List<Pair<MapleStat, Integer>> hpmpupdate = new ArrayList<Pair<MapleStat, Integer>>(2);
        if (hpchange != 0) {
            if (hpchange < 0 && (-hpchange) > stat.getHp() && !applyto.hasDisease(MapleDisease.ZOMBIFY)) {
                return false;
            }
            stat.setHp(stat.getHp() + hpchange);
        }
        if (mpchange != 0) {
            if (mpchange < 0 && (-mpchange) > stat.getMp()) {
                return false;
            }
            stat.setMp(stat.getMp() + mpchange);

            hpmpupdate.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(stat.getMp())));
        }
        hpmpupdate.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(stat.getHp())));
        applyto.getClient().sendPacket(MaplePacketCreator.updatePlayerStats(hpmpupdate, true, applyto.getJob()));

        if (expinc != 0) {
            applyto.gainExp(expinc, true, true, false);
//            applyto.getClient().sendPacket(MaplePacketCreator.showSpecialEffect(19));
        } else if (GameConstants.isMonsterCard(sourceid)) {
            applyto.getMonsterBook().addCard(applyto.getClient(), sourceid);
        } else if (isSpiritClaw() && !applyto.isClone()) {
            MapleInventory use = applyto.getInventory(MapleInventoryType.USE);
            IItem item;
            for (int i = 0; i < use.getSlotLimit(); i++) {
                item = use.getItem((byte) i);
                if (item != null) {
                    if (GameConstants.isThrowingStar(item.getItemId()) && item.getQuantity() >= 200) {
                        MapleInventoryManipulator.removeById(applyto.getClient(), MapleInventoryType.USE, item.getItemId(), 200, false, true);
                        break;
                    }
                }
            }
        } else if (cp != 0 && applyto.getCarnivalParty() != null) {
            applyto.getCarnivalParty().addCP(applyto, cp);
            applyto.CPUpdate(false, applyto.getAvailableCP(), applyto.getTotalCP(), 0);
            for (MapleCharacter chr : applyto.getMap().getCharactersThreadsafe()) {
                chr.CPUpdate(true, applyto.getCarnivalParty().getAvailableCP(), applyto.getCarnivalParty().getTotalCP(), applyto.getCarnivalParty().getTeam());
            }
        } else if (nuffSkill != 0 && applyto.getParty() != null) {
            final MCSkill skil = MapleCarnivalFactory.getInstance().getSkill(nuffSkill);
            if (skil != null) {
                final MapleDisease dis = skil.getDisease();
                for (MapleCharacter chr : applyto.getMap().getCharactersThreadsafe()) {
                    if (chr.getParty() == null || (chr.getParty().getId() != applyto.getParty().getId())) {
                        if (skil.targetsAll || Randomizer.nextBoolean()) {
                            if (dis == null) {
                                chr.dispel();
                            } else if (skil.getSkill() == null) {
                                chr.giveDebuff(dis, 1, 30000, MapleDisease.getByDisease(dis), 1);
                            } else {
                                chr.giveDebuff(dis, skil.getSkill());
                            }
                            if (!skil.targetsAll) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (overTime && !isEnergyCharge()) {
            applyBuffEffect(applyfrom, applyto, primary, newDuration);
        }
        if (skill) {
            removeMonsterBuff(applyfrom);
        }
        if (primary) {
            if ((overTime || isHeal()) && !isEnergyCharge()) {
                applyBuff(applyfrom, newDuration);
            }
            if (isMonsterBuff()) {
                applyMonsterBuff(applyfrom);
            }
        }
        final SummonMovementType summonMovementType = getSummonMovementType();
        if (summonMovementType != null) {
            final MapleSummon tosummon = new MapleSummon(applyfrom, this, new Point(pos == null ? applyfrom.getPosition() : pos), summonMovementType);
            if (!tosummon.isPuppet()) {
                applyfrom.getCheatTracker().resetSummonAttack();
            }
            //召唤兽
            applyfrom.getMap().spawnSummon(tosummon);
            applyfrom.getSummons().put(sourceid, tosummon);
            tosummon.addHP((short) x);
            if (isBeholder()) {
                tosummon.addHP((short) 1);
            }
            if (sourceid == 4341006) {
                applyfrom.cancelEffectFromBuffStat(MapleBuffStat.MIRROR_IMAGE);
            }
        } else if (isMagicDoor()) { // Magic Door
            MapleDoor door = new MapleDoor(applyto, new Point(applyto.getPosition()), sourceid); // Current Map door
            if (door.getTownPortal() != null) {
                applyto.getMap().spawnDoor(door);
                applyto.addDoor(door);
                MapleDoor townDoor = new MapleDoor(door); // Town door
                applyto.addDoor(townDoor);
                door.getTown().spawnDoor(townDoor);

                if (applyto.getParty() != null) { // update town doors
                    applyto.silentPartyUpdate();
                }
            } else {
                applyto.dropMessage(5, "你可能没办法使用传送们因为村庄内禁止..");
            }

        } else if (isMist()) {
            final Rectangle bounds = calculateBoundingBox(pos != null ? pos : new Point(applyfrom.getPosition()), applyfrom.isFacingLeft());
            final MapleMist mist = new MapleMist(bounds, applyfrom, this);
            applyfrom.getMap().spawnMist(mist, getDuration(), false);
        } else if (isTimeLeap()) { //飞跃
            for (MapleCoolDownValueHolder i : applyto.getCooldowns()) {
                //伺机待发
                if (i.skillId != 5121010) {
                    applyto.removeCooldown(i.skillId);
                    applyto.getClient().sendPacket(MaplePacketCreator.skillCooldown(i.skillId, 0));
                }
            }
        } else {
            for (WeakReference<MapleCharacter> chrz : applyto.getClones()) {
                if (chrz.get() != null) {
                    applyTo(chrz.get(), chrz.get(), primary, pos, newDuration);
                }
            }
        }
        return true;
    }

    //关于地图的判断，回城卷类？？？
    public final boolean applyReturnScroll(final MapleCharacter applyto) {
        if (moveTo != -1) {
            MapleMap target;
            if (moveTo == 999999999) {
                target = applyto.getMap().getReturnMap();
            } else {
                target = ChannelServer.getInstance(applyto.getClient().getChannel()).getMapFactory().getMap(moveTo);
                if (target.getId() / 10000000 != 60 && applyto.getMapId() / 10000000 != 61) {
                    if (target.getId() / 10000000 != 21 && applyto.getMapId() / 10000000 != 20) {
                        if (target.getId() / 10000000 != 12) {
                            if (target.getId() / 10000000 != applyto.getMapId() / 10000000) {
                                return false;
                            }
                        }
                    }
                }
            }
            try {
                applyto.changeMap(target, target.getPortal(0));
            } catch (Exception ex) {
                //applyto.dropMessage(5, "本地图目前尚未开放.");
                return false;
            }
            return true;
        }
        return false;
    }

    private final boolean isSoulStone() {
        return skill && sourceid == 22181003;
    }

    private final void applyBuff(final MapleCharacter applyfrom, int newDuration) {
        if (isSoulStone()) {
            if (applyfrom.getParty() != null) {
                int membrs = 0;
                for (MapleCharacter chr : applyfrom.getMap().getCharactersThreadsafe()) {
                    if (chr.getParty() != null && chr.getParty().equals(applyfrom.getParty()) && chr.isAlive()) {
                        membrs++;
                    }
                }
                List<MapleCharacter> awarded = new ArrayList<MapleCharacter>();
                while (awarded.size() < Math.min(membrs, y)) {
                    for (MapleCharacter chr : applyfrom.getMap().getCharactersThreadsafe()) {
                        if (chr.isAlive() && chr.getParty().equals(applyfrom.getParty()) && !awarded.contains(chr) && Randomizer.nextInt(y) == 0) {
                            awarded.add(chr);
                        }
                    }
                }
                for (MapleCharacter chr : awarded) {
                    applyTo(applyfrom, chr, false, null, newDuration);
                    chr.getClient().sendPacket(MaplePacketCreator.showOwnBuffEffect(sourceid, 2));
                    chr.getMap().broadcastMessage(chr, MaplePacketCreator.showBuffeffect(chr.getId(), sourceid, 2), false);
                }
            }
        } else if (isPartyBuff() && (applyfrom.getParty() != null || isGmBuff())) {
            final Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
            final List<MapleMapObject> affecteds = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.PLAYER));
            for (final MapleMapObject affectedmo : affecteds) {
                final MapleCharacter affected = (MapleCharacter) affectedmo;
                if (affected != applyfrom && (isGmBuff() || applyfrom.getParty().equals(affected.getParty()))) {
                    if ((isResurrection() && !affected.isAlive()) || (!isResurrection() && affected.isAlive())) {
                        applyTo(applyfrom, affected, false, null, newDuration);
                        affected.getClient().sendPacket(MaplePacketCreator.showOwnBuffEffect(sourceid, 2));
                        affected.getMap().broadcastMessage(affected, MaplePacketCreator.showBuffeffect(affected.getId(), sourceid, 2), false);
                    }
                    if (isTimeLeap()) {
                        for (MapleCoolDownValueHolder i : affected.getCooldowns()) {
                            if (i.skillId != 5121010) {
                                affected.removeCooldown(i.skillId);
                                affected.getClient().sendPacket(MaplePacketCreator.skillCooldown(i.skillId, 0));
                            }
                        }
                    }
                }
            }
        }
    }

    private final void removeMonsterBuff(final MapleCharacter applyfrom) {
        List<MonsterStatus> cancel = new ArrayList<MonsterStatus>();
        switch (sourceid) {
            case 1111007://防御崩坏
                cancel.add(MonsterStatus.WDEF);
                cancel.add(MonsterStatus.WEAPON_DEFENSE_UP);
                cancel.add(MonsterStatus.WEAPON_IMMUNITY);
                break;
            case 1211009://魔击无效
                cancel.add(MonsterStatus.MDEF);
                cancel.add(MonsterStatus.MAGIC_DEFENSE_UP);
                cancel.add(MonsterStatus.MAGIC_IMMUNITY);
                break;
            case 1311007://力量崩坏
                cancel.add(MonsterStatus.WATK);
                cancel.add(MonsterStatus.WEAPON_ATTACK_UP);
                cancel.add(MonsterStatus.MATK);
                cancel.add(MonsterStatus.MAGIC_ATTACK_UP);
                break;
            default:
                return;
        }
        final Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
        final List<MapleMapObject> affected = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.MONSTER));
        int i = 0;

        for (final MapleMapObject mo : affected) {
            if (makeChanceResult()) {
                for (MonsterStatus stat : cancel) {
                    ((MapleMonster) mo).cancelStatus(stat);
                }
            }
            i++;
            if (i >= mobCount) {
                break;
            }
        }
    }

    private final void applyMonsterBuff(final MapleCharacter applyfrom) {
        final Rectangle bounds = calculateBoundingBox(applyfrom.getPosition(), applyfrom.isFacingLeft());
        final List<MapleMapObject> affected = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.MONSTER));
        int i = 0;

        for (final MapleMapObject mo : affected) {
            if (makeChanceResult()) {
                for (Map.Entry<MonsterStatus, Integer> stat : getMonsterStati().entrySet()) {
                    ((MapleMonster) mo).applyStatus(applyfrom, new MonsterStatusEffect(stat.getKey(), stat.getValue(), sourceid, null, false), isPoison(), getDuration(), false);
                }
            }
            i++;
            if (i >= mobCount) {
                break;
            }
        }
    }

    private final Rectangle calculateBoundingBox(final Point posFrom, final boolean facingLeft) {
        if (lt == null || rb == null) {
            return new Rectangle(posFrom.x, posFrom.y, facingLeft ? 1 : -1, 1);
        }
        Point mylt;
        Point myrb;
        if (facingLeft) {
            mylt = new Point(lt.x + posFrom.x, lt.y + posFrom.y);
            myrb = new Point(rb.x + posFrom.x, rb.y + posFrom.y);
        } else {
            myrb = new Point(lt.x * -1 + posFrom.x, rb.y + posFrom.y);
            mylt = new Point(rb.x * -1 + posFrom.x, lt.y + posFrom.y);
        }
        return new Rectangle(mylt.x, mylt.y, myrb.x - mylt.x, myrb.y - mylt.y);
    }

    public final void setDuration(int d) {
        this.duration = d;
    }

    public final void silentApplyBuff(final MapleCharacter chr, final long starttime) {
        final int localDuration = alchemistModifyVal(chr, duration, false);
        chr.registerEffect(this, starttime, BuffTimer.getInstance().schedule(new CancelEffectAction(chr, this, starttime),
                ((starttime + localDuration) - System.currentTimeMillis())));

        final SummonMovementType summonMovementType = getSummonMovementType();
        if (summonMovementType != null) {
            final MapleSummon tosummon = new MapleSummon(chr, this, chr.getPosition(), summonMovementType);
            if (!tosummon.isPuppet()) {
                chr.getCheatTracker().resetSummonAttack();
                chr.getMap().spawnSummon(tosummon);
                chr.getSummons().put(sourceid, tosummon);
                tosummon.addHP((short) x);
                if (isBeholder()) {
                    tosummon.addHP((short) 1);
                }
            }
        }
    }

    /*
     * 战神矛连击，暴击强化
     */
    public final void applyComboBuff(final MapleCharacter applyto, short combo) {
        final ArrayList<Pair<MapleBuffStat, Integer>> statups = new ArrayList<Pair<MapleBuffStat, Integer>>();
        //  final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.ARAN_COMBO, (int) combo));
        //applyto.getClient().sendPacket(MaplePacketCreator.giveBuff(this.sourceid, 99999, stat, this)); // Hackish timing, todo find out
        // statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.矛连击强化2, Integer.valueOf(combo)));
        statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.矛连击强化, Integer.valueOf(combo / 5)));
        statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.WDEF, Integer.valueOf(combo / 2)));
        statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MDEF, Integer.valueOf(combo / 2)));
        applyto.getClient().sendPacket(MaplePacketCreator.giveBuff(this.sourceid, 29999, statups, this)); // Hackish timing, todo find out

        final long starttime = System.currentTimeMillis();
        final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime);
        final ScheduledFuture<?> schedule = BuffTimer.getInstance().schedule(cancelAction, ((starttime + 29999) - System.currentTimeMillis()));
        applyto.registerEffect(this, starttime, schedule);
    }

    public final void applyEnergyBuff(final MapleCharacter applyto, final boolean infinity) {
        final List<Pair<MapleBuffStat, Integer>> stat = this.statups;

        final long starttime = System.currentTimeMillis();
        if (infinity) {
            applyto.setBuffedValue(MapleBuffStat.ENERGY_CHARGE, 0);
            applyto.getClient().sendPacket(MaplePacketCreator.能量条(stat, duration / 1000));
            applyto.registerEffect(this, starttime, null);
        } else {
            applyto.cancelEffect(this, true, -1);
            //          applyto.getClient().sendPacket(MaplePacketCreator.能量条(applyto.getId(), duration / 1000)); //????????????????
            applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveEnergyChargeTest(applyto.getId(), 10000, duration / 1000), false);
            final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime);
            final ScheduledFuture<?> schedule = BuffTimer.getInstance().schedule(cancelAction, ((starttime + duration) - System.currentTimeMillis()));
            this.statups = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.ENERGY_CHARGE, 10000));
            applyto.registerEffect(this, starttime, schedule);
            this.statups = stat;
        }
    }

    private final void applyBuffEffect(final MapleCharacter applyfrom, final MapleCharacter applyto, final boolean primary, final int newDuration) {
        int localDuration = newDuration;
        // localDuration = 5000;
        if (primary) {
            localDuration = alchemistModifyVal(applyfrom, localDuration, false);
            applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.showBuffeffect(applyto.getId(), sourceid, 1), false);
        }
        List<Pair<MapleBuffStat, Integer>> localstatups = statups;
        boolean normal = true;
        switch (sourceid) {
            case 5001005: {
                if (localstatups.size() > 0) {
                    if (isDash()) {
                        applyto.getClient().sendPacket(MaplePacketCreator.giveDash(localstatups, localDuration / 1000));
                        applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.showDashEffecttoOthers(applyto.getId(), localstatups, localDuration / 1000), false);
//            } else if (isInfusion()) {
//                applyto.getClient().sendPacket(MaplePacketCreator.giveInfusion(localDuration / 1000, x));
                    } else {
                        applyto.getClient().sendPacket(MaplePacketCreator.giveBuff((skill ? sourceid : -sourceid), localDuration, statups, this));
                    }
                }
                break;
            }
            case 5121009: // Speed Infusion
            case 15111005:

            case 15001003: {
                applyto.getClient().sendPacket(MaplePacketCreator.givePirate(statups, localDuration / 1000, sourceid));
                // applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignPirate(statups, localDuration / 1000, applyto.getId(), sourceid), false);
                normal = false;
                break;
            }
            case 5211006: // Homing Beacon
            case 22151002: //killer wings
            case 5220011: {// Bullseye
                if (applyto.getLinkMid() > 0) {
                    applyto.getClient().sendPacket(MaplePacketCreator.cancelHoming());
                    applyto.getClient().sendPacket(MaplePacketCreator.giveHoming(sourceid, applyto.getLinkMid()));
                } else {
                    return;
                }
                normal = false;
                break;
            }
            case 13101006:
            case 4330001:
            case 4001003:
            case 14001003: { // Dark Sight
                final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DARKSIGHT, 0));//隐身
                applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                break;
            }
            //case 22131001: {//magic shield
            //final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAGIC_SHIELD, x));
            //applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto.getId(), stat, this), false);
            //break;
            //}
            case 32001003: //dark aura
            case 32120000: {
                final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DARK_AURA, 1));
                applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                applyto.cancelEffectFromBuffStat(MapleBuffStat.BLUE_AURA);
                applyto.cancelEffectFromBuffStat(MapleBuffStat.YELLOW_AURA);
                break;
            }
            case 32101002: //blue aura
            case 32110000: {
                final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.BLUE_AURA, 1));
                applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                applyto.cancelEffectFromBuffStat(MapleBuffStat.YELLOW_AURA);
                applyto.cancelEffectFromBuffStat(MapleBuffStat.DARK_AURA);
                break;
            }
            case 32101003: //yellow aura
            case 32120001: {
                final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.YELLOW_AURA, 1));
                applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                applyto.cancelEffectFromBuffStat(MapleBuffStat.BLUE_AURA);
                applyto.cancelEffectFromBuffStat(MapleBuffStat.DARK_AURA);
                break;
            }

            case 35001001: //flame
            case 35101009:
            case 35111007: //TEMP
            case 35101002: //TEMP
            case 35121013:
            //  case 35111004: siege
            case 35121005: { //missile
                final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MECH_CHANGE, 1));
                applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                break;
            }
            case 1111002:
            case 11111001: { // Combo
                final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.COMBO, 1));
                applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                break;
            }
            case 3101004:
            case 3201004:
            case 13101003: { // Soul Arrow
                final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SOULARROW, 0));
                applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                break;
            }
            case 4111002:
            case 14111000: { // Shadow Partne
                final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SHADOWPARTNER, 0));
                applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                break;
            }
            case 15111006: { // Spark
                localstatups = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SPARK, x));
                applyto.getClient().sendPacket(MaplePacketCreator.giveBuff(sourceid, localDuration, localstatups, this));
                normal = false;
                break;
            }

            case 1121010: // Enrage
                applyto.handleOrbconsume();
                break;
            default:
                if (isMorph() || isPirateMorph()) {
                    final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MORPH, Integer.valueOf(getMorph(applyto))));
                    applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                } else if (isMonsterRiding()) {
                    localDuration = 2100000000;
                    int mountid = parseMountInfo(applyto, sourceid);
                    int mountid2 = parseMountInfo_Pure(applyto, sourceid);
                    if (sourceid == 1013 && applyto.getMountId() != 0) {
                        mountid = applyto.getMountId();
                        mountid2 = applyto.getMountId();
                    }
                    if (mountid != 0 && mountid2 != 0) {
                        final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<>(MapleBuffStat.MONSTER_RIDING, 0));
                        applyto.cancelEffectFromBuffStat(MapleBuffStat.POWERGUARD);
                        applyto.cancelEffectFromBuffStat(MapleBuffStat.MANA_REFLECTION);
                        applyto.getClient().sendPacket(MaplePacketCreator.giveMount(applyto, mountid2, sourceid, stat));
                        applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.showMonsterRiding(applyto.getId(), stat, mountid, sourceid), false);
                    } else {
                        return;
                    }
                    normal = false;
                } else if (isSoaring()) {
                    localstatups = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.SOARING, 1));
                    applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), localstatups, this), false);
                    applyto.getClient().sendPacket(MaplePacketCreator.giveBuff(sourceid, localDuration, localstatups, this));
                    // normal = false;
                    //} else if (berserk > 0) {
                    //    final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.PYRAMID_PQ, berserk));
                    //    applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto.getId(), stat, this), false);
                } else if (isBerserkFury() || berserk2 > 0) {
                    final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.BERSERK_FURY, 1));
                    applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                } else if (isDivineBody()) {
                    final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DIVINE_BODY, 1));
                    applyto.getMap().broadcastMessage(applyto, MaplePacketCreator.giveForeignBuff(applyto, applyto.getId(), stat, this), false);
                }
                break;
        }
        if (!isMonsterRiding_()) {
            applyto.cancelEffect(this, true, -1, localstatups);
        }
        // Broadcast effect to self
        if (normal && statups.size() > 0) {
            applyto.getClient().sendPacket(MaplePacketCreator.giveBuff((skill ? sourceid : -sourceid), localDuration, statups, this));
        }
        final long starttime = System.currentTimeMillis();
        final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime);
        //System.out.println("Started effect " + sourceid + ". Duration: " + localDuration + ", Actual Duration: " + (((starttime + localDuration) - System.currentTimeMillis())));
        final ScheduledFuture<?> schedule = BuffTimer.getInstance().schedule(cancelAction, ((starttime + localDuration) - System.currentTimeMillis()));
        applyto.registerEffect(this, starttime, schedule, localstatups);
    }

    public static final int parseMountInfo(final MapleCharacter player, final int skillid) {
        switch (skillid) {
            case 1004: // Monster riding
            case 10001004:
            case 20001004:
            case 20011004:
            case 30001004:
                if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) (-118)) != null && player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) (-119)) != null) {
                    return player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) (-118)).getItemId();
                }
                return parseMountInfo_Pure(player, skillid);
            default:
                return GameConstants.getMountItem(skillid);
        }
    }

    public static final int parseMountInfo_Pure(final MapleCharacter player, final int skillid) {
        switch (skillid) {
            case 80001000:
            case 1004: // Monster riding
            case 11004: // Monster riding
            case 10001004:
            case 20001004:
            case 20011004:
            case 20021004:
                if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) (-18)) != null && player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) (-19)) != null) {
                    return player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) (-18)).getItemId();
                }
                return 0;
            default:
                return GameConstants.getMountItem(skillid);
        }
    }

    private final int calcHPChange(final MapleCharacter applyfrom, final boolean primary) {
        int hpchange = 0;
        if (hp != 0) {
            if (!skill) {
                if (primary) {
                    hpchange += alchemistModifyVal(applyfrom, hp, true);
                } else {
                    hpchange += hp;
                }
                if (applyfrom.hasDisease(MapleDisease.ZOMBIFY)) {
                    hpchange /= 2;
                }
            } else { // assumption: this is heal
                hpchange += makeHealHP(hp / 100.0, applyfrom.getStat().getTotalMagic(), 3, 5);
                if (applyfrom.hasDisease(MapleDisease.ZOMBIFY)) {
                    hpchange = -hpchange;
                }
            }
        }
        if (hpR != 0) {
            hpchange += (int) (applyfrom.getStat().getCurrentMaxHp() * hpR) / (applyfrom.hasDisease(MapleDisease.ZOMBIFY) ? 2 : 1);
        }
        // actually receivers probably never get any hp when it's not heal but whatever
        if (primary) {
            if (hpCon != 0) {
                hpchange -= hpCon;
            }
        }
        switch (this.sourceid) {//转化术
            case 4211001: //转化术
                final PlayerStats stat = applyfrom.getStat();
                int v42 = getY() + 100;
                int v38 = Randomizer.rand(1, 100) + 100;
                hpchange = (int) ((v38 * stat.getLuk() * 0.033 + stat.getDex()) * v42 * 0.002);
                hpchange += makeHealHP(getY() / 100.0, applyfrom.getStat().getTotalLuk(), 2.3, 3.5);
                break;
        }
        return hpchange;
    }

    private static final int makeHealHP(double rate, double stat, double lowerfactor, double upperfactor) {
        return (int) ((Math.random() * ((int) (stat * upperfactor * rate) - (int) (stat * lowerfactor * rate) + 1)) + (int) (stat * lowerfactor * rate));
    }

    private static final int getElementalAmp(final int job) {//魔力激化
        switch (job) {
            case 211:
            case 212:
                return 2110001;
            case 221:
            case 222:
                return 2210001;
            case 1211:
            case 1212:
                return 12110001;
            case 2215:
            case 2216:
            case 2217:
            case 2218:
                return 22150000;
        }
        return -1;
    }

    private final int calcMPChange(final MapleCharacter applyfrom, final boolean primary) {
        int mpchange = 0;
        if (mp != 0) {
            if (primary) {
                mpchange += alchemistModifyVal(applyfrom, mp, true);
            } else {
                mpchange += mp;
            }
        }
        if (mpR != 0) {
            mpchange += (int) (applyfrom.getStat().getCurrentMaxMp() * mpR);
        }
        if (primary) {
            if (mpCon != 0) {
                double mod = 1.0;

                final int ElemSkillId = getElementalAmp(applyfrom.getJob());
                if (ElemSkillId != -1) {
                    final ISkill amp = SkillFactory.getSkill(ElemSkillId);
                    final int ampLevel = applyfrom.getSkillLevel(amp);
                    if (ampLevel > 0) {
                        MapleStatEffect ampStat = amp.getEffect(ampLevel);
                        mod = ampStat.getX() / 100.0;
                    }
                }
                final Integer Concentrate = applyfrom.getBuffedSkill_X(MapleBuffStat.CONCENTRATE);
                final int percent_off = applyfrom.getStat().mpconReduce + (Concentrate == null ? 0 : Concentrate);
                if (applyfrom.getBuffedValue(MapleBuffStat.INFINITY) != null) {
                    mpchange = 0;
                } else {
                    mpchange -= (mpCon - (mpCon * percent_off / 100)) * mod;
                }
            }
        }
        return mpchange;
    }

    private final int alchemistModifyVal(final MapleCharacter chr, final int val, final boolean withX) {
        if (!skill) {
            int offset = chr.getStat().RecoveryUP;
            final MapleStatEffect alchemistEffect = getAlchemistEffect(chr);
            if (alchemistEffect != null) {
                offset += (withX ? alchemistEffect.getX() : alchemistEffect.getY());
            } else {
                offset += 100;
            }
            return (val * offset / 100);
        }
        return val;
    }

    private final MapleStatEffect getAlchemistEffect(final MapleCharacter chr) {//药剂精通
        ISkill al;
        switch (chr.getJob()) {
            case 411:
            case 412:
                al = SkillFactory.getSkill(4110000);
                if (chr.getSkillLevel(al) <= 0) {
                    return null;
                }
                return al.getEffect(chr.getSkillLevel(al));
            case 1411:
            case 1412:
                al = SkillFactory.getSkill(14110003);
                if (chr.getSkillLevel(al) <= 0) {
                    return null;
                }
                return al.getEffect(chr.getSkillLevel(al));
        }
        if (GameConstants.isResist(chr.getJob())) {
            al = SkillFactory.getSkill(30000002);
            if (chr.getSkillLevel(al) <= 0) {
                return null;
            }
            return al.getEffect(chr.getSkillLevel(al));
        }
        return null;
    }

    public final void setSourceId(final int newid) {
        sourceid = newid;
    }

    private final boolean isGmBuff() {
        switch (sourceid) {
            case 1005: // echo of hero acts like a gm buff
            case 10001005: // cygnus Echo
            case 20001005: // Echo
            case 20011005:
            case 30001005:
            case 9001000: // GM dispel
            case 9001001: // GM haste
            case 9001002: // GM Holy Symbol
            case 9001003: // GM Bless
            case 9001005: // GM resurrection
            case 9001008: // GM Hyper body
                return true;
            default:
                return false;
        }
    }

    private final boolean isEnergyCharge() {//能量获取
        return skill && (sourceid == 5110001 || sourceid == 15100004);
    }

    private final boolean isMonsterBuff() {//给怪物的BUFF
        switch (sourceid) {
            // case 1201006: // 压制术
            case 2101003: // fp slow
            case 2201003: // il slow
            case 12101001: // cygnus slow
            case 2211004: // il seal
            case 2111004: // fp seal
            case 12111002: // cygnus seal
            case 2311005: // doom
            case 4111003: // shadow web
            case 14111001: // cygnus web
            case 4121004: // Ninja ambush
            case 4221004: // Ninja ambush
            case 22151001:
            case 22141003:
            case 22121000:
            case 22161002:
            case 4321002:
                return skill;
        }
        return false;
    }

    public final void setPartyBuff(boolean pb) {
        this.partyBuff = pb;
    }

    private final boolean isPartyBuff() {
        if (lt == null || rb == null || !partyBuff) {
            return isSoulStone();
        }
        switch (sourceid) {
            case 1211003:
            case 1211004:
            case 1211005:
            case 1211006:
            case 1211007:
            case 1211008:
            case 1221003:
            case 1221004:
            case 11111007:
            //case 12101005://自然力重置
            case 4311001:
                return false;
        }
        return true;
    }

    public final boolean isHeal() {
        return sourceid == 2301002 || sourceid == 9101000;
    }

    public final boolean isResurrection() {
        return sourceid == 9001005 || sourceid == 2321006;
    }

    public final boolean isTimeLeap() {
        return sourceid == 5121010;
    }

    public final short getHp() {
        return hp;
    }

    public final short getMp() {
        return mp;
    }

    public final byte getMastery() {
        return mastery;
    }

    public final short getWatk() {
        return watk;
    }

    public final short getMatk() {
        return matk;
    }

    public final short getWdef() {
        return wdef;
    }

    public final short getMdef() {
        return mdef;
    }

    public final short getAcc() {
        return acc;
    }

    public final short getAvoid() {
        return avoid;
    }

    public final short getHands() {
        return hands;
    }

    public final short getSpeed() {
        return speed;
    }

    public final short getJump() {
        return jump;
    }

    public final int getDuration() {
        return duration;
    }

    public final boolean isOverTime() {
        return overTime;
    }

    public final List<Pair<MapleBuffStat, Integer>> getStatups() {
        return statups;
    }

    public final boolean sameSource(final MapleStatEffect effect) {
        return effect != null && this.sourceid == effect.sourceid && this.skill == effect.skill;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getZ() {
        return z;
    }

    public final short getDamage() {
        return damage;
    }

    public final byte getAttackCount() {
        return attackCount;
    }

    public final byte getBulletCount() {
        return bulletCount;
    }

    public final int getBulletConsume() {
        return bulletConsume;
    }

    public final byte getMobCount() {
        return mobCount;
    }

    public final int getMoneyCon() {
        return moneyCon;
    }

    public final int getCooldown() {
        return cooldown;
    }

    public final Map<MonsterStatus, Integer> getMonsterStati() {
        return monsterStatus;
    }

    public final int getBerserk() {
        return berserk;
    }

    public final boolean isHide() {
        return skill && sourceid == 9001004;
    }

    public final boolean isDragonBlood() {//龙之魂
        return skill && (sourceid == 1311008 || sourceid == 2211005 || sourceid == 2211005);
    }

    public final boolean isBerserk() {//恶龙附身
        return skill && sourceid == 1320006;
    }

    public final boolean isBeholder() {//灵魂助力
        return skill && sourceid == 1321007;
    }

    public final boolean isMPRecovery() {//生命分流
        return skill && sourceid == 5101005;
    }

    public final boolean isMonsterRiding_() {//骑兽
        return skill && (sourceid == 1004 || sourceid == 1016 || sourceid == 10001004 || sourceid == 20001004 || sourceid == 20011004 || sourceid == 30001004);
    }

    public final boolean isMonsterRiding() {
        return skill && (isMonsterRiding_() || GameConstants.getMountItem(sourceid) != 0);
    }

    public final boolean isMagicDoor() {//时空门
        return skill && (sourceid == 2311002 || sourceid == 8001 || sourceid == 10008001 || sourceid == 20008001 || sourceid == 20018001 || sourceid == 30008001);
    }

    public final boolean isMesoGuard() {//金钱护盾
        return skill && sourceid == 4211005;
    }

    public boolean isDash() {//疾驰和能量获取
        return this.sourceid == 5001005 || sourceid == 5110001;
    }

    public final boolean isCharge() {
        switch (sourceid) {
            case 1211003:
            case 1211008:
            case 11111007:
            case 12101005:
            case 15101006:
            case 21111005:
                return skill;
        }
        return false;
    }

    public final boolean isPoison() {
        switch (sourceid) {//毒状态屏蔽
            case 2111003://致命毒雾
            case 12111005: // 火焰屏障
            case 2101005://毒雾术
                // case 2111006:
                //case 2121003:
                //case 2221003:
                // case 3111003: //inferno, new//烈焰箭
                //case 22161002: //phantom imprint
                return skill;
        }
        return false;
    }

    /*
     * public boolean isPoison() { return skill && (sourceid == 2111003 ||
     * sourceid == 4221006 || sourceid == 12111005 || sourceid == 14111006 ||
     * sourceid == 22161003 || sourceid == 32121006 || sourceid == 1076 ||
     * sourceid == 11076 || sourceid == 4121015); // poison mist, smokescreen
     * and flame gear, recovery aura }
     */
    private final boolean isMist() {//在地图持续的技能状态
        return skill && (sourceid == 2111003 || sourceid == 4221006 || sourceid == 12111005 || sourceid == 14111006 || sourceid == 22161003); // poison mist, smokescreen and flame gear, recovery aura
    }

    private final boolean isSpiritClaw() {//暗器伤人，不消耗标
        return skill && sourceid == 4121006;
    }

    private final boolean isDispel() {//净化异常状态
        return skill && (sourceid == 2311001 || sourceid == 9001000);
    }

    private final boolean isHeroWill() {//勇士意志
        switch (sourceid) {
            case 1121011:
            case 1221012:
            case 1321010:
            case 2121008:
            case 2221008:
            case 2321009:
            case 3121009:
            case 3221008:
            case 4121009:
            case 4221008:
            case 5121008:
            case 5221010:
            case 21121008:
            case 22171004:
            case 4341008:
            case 32121008:
            case 33121008:
            case 35121008:
                return skill;
        }
        return false;
    }

    public final boolean isAranCombo() {//战神，矛连击
        return sourceid == 21000000;
    }

    public final boolean isCombo() {//斗气集中
        switch (sourceid) {
            case 1111002:
            case 11111001: // Combo
                return skill;
        }
        return false;
    }

    public final boolean isPirateMorph() {//超人变身
        switch (sourceid) {
            case 15111002:
            case 5111005:
            case 5121003:
                return skill;
        }
        return false;
    }

    public final boolean isMorph() {
        return morphId > 0;
    }

    public final int getMorph() {//超人变身橡胶桶
        switch (sourceid) {
            case 15111002:
            case 5111005:
                return 1000;
            case 5121003:
                return 1001;
            case 5101007:
                return 1002;
            case 13111005:
                return 1003;
        }
        return morphId;
    }

    public final boolean isDivineBody() {//金刚霸体
        switch (sourceid) {
            case 1010:
            case 10001010:// Invincible Barrier
            case 20001010:
            case 20011010:
            case 30001010:
                return skill;
        }
        return false;
    }

    public final boolean isBerserkFury() {
        switch (sourceid) {
            case 1011: // Berserk fury
            case 10001011:
            case 20001011:
            case 20011011:
            case 30001011:
                return skill;
        }
        return false;
    }

    public final int getMorph(final MapleCharacter chr) {
        final int morph = getMorph();
        switch (morph) {
            case 1000:
            case 1001:
            case 1003:
                return morph + (chr.getGender() == 1 ? 100 : 0);
        }
        return morph;
    }

    public final byte getLevel() {
        return level;
    }

    public final SummonMovementType getSummonMovementType() {//召唤兽类
        if (!skill) {
            return null;
        }
        switch (sourceid) {
            case 3211002: //替身术
            case 3111002: // 替身术
            case 13111004: // 替身术
            case 5211001: // 章鱼炮台
            case 5220002: // 超级章鱼炮台
                //case 4111007: //TEMP
                return SummonMovementType.STATIONARY;
            case 3211005: // golden eagle
            case 3111005: // golden hawk
            case 2311006: // summon dragon
            case 3221005: // frostprey
            case 3121006: // phoenix
                return SummonMovementType.CIRCLE_FOLLOW;
            case 5211002: // bird - pirate
                return SummonMovementType.CIRCLE_STATIONARY;
            case 32111006: //reaper
                return SummonMovementType.WALK_STATIONARY;
            case 1321007: // beholder
            case 2121005: // elquines
            case 2221005: // ifrit
            case 2321003: // bahamut
            case 12111004: // Ifrit
            case 11001004: // soul
            case 12001004: // flame
            case 13001004: // storm
            case 14001005: // darkness
            case 15001004: // lightning
                return SummonMovementType.FOLLOW;
        }
        return null;
    }

    public final boolean isSkill() {
        return skill;
    }

    public final int getSourceId() {
        return sourceid;
    }

    public final boolean isSoaring() {
        switch (sourceid) {
            case 1026: // Soaring
            case 10001026: // Soaring
            case 20001026: // Soaring
            case 20011026: // Soaring
            case 30001026:
                return skill;
        }
        return false;
    }

    public final boolean isFinalAttack() {//终极弓，终极剑
        switch (sourceid) {
            case 13101002:
            case 11101002:
                return skill;
        }
        return false;
    }

    /**
     *
     * @return true if the effect should happen based on it's probablity, false
     * otherwise
     */
    public final boolean makeChanceResult() {
        return prop == 100 || Randomizer.nextInt(99) < prop;
    }

    public final short getProb() {
        return prop;
    }

    private boolean isBattleShip() {//海盗船
        return (this.skill) && (this.sourceid == 5221006 || this.sourceid == 1016);
    }

    public static class CancelEffectAction implements Runnable {

        private final MapleStatEffect effect;
        private final WeakReference<MapleCharacter> target;
        private final long startTime;

        public CancelEffectAction(final MapleCharacter target, final MapleStatEffect effect, final long startTime) {
            this.effect = effect;
            this.target = new WeakReference<MapleCharacter>(target);
            this.startTime = startTime;
        }

        @Override
        public void run() {
            final MapleCharacter realTarget = target.get();
            if (realTarget != null && !realTarget.isClone()) {
                realTarget.cancelEffect(effect, false, startTime);
            }
        }
    }
}
