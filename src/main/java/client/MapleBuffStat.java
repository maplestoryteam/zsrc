package client;

import java.io.Serializable;

public enum MapleBuffStat implements Serializable {
    //DICE = 0x100000, the roll is determined in the long-ass packet itself as buffedvalue
    //ENERGY CHARGE = 0x400000
    //teleport mastery = 0x800000
    //PIRATE'S REVENGE = 0x4000000
    //DASH = 0x8000000 and 0x10000000
    //SPEED INFUSION = 0x40000000
    //MONSTER RIDER = 0x20000000
    //COMBAT ORDERS = 0x1000000
    影分身(0x400000000000000L),
    ENHANCED_WDEF(0x1, true),
    ENHANCED_MDEF(0x2, true),
    PERFECT_ARMOR(0x4, true),
    SATELLITESAFE_PROC(0x8, true),
    SATELLITESAFE_ABSORB(0x10, true),
    CRITICAL_RATE_BUFF(0x40, true),
    MP_BUFF(0x80, true),
    DAMAGE_TAKEN_BUFF(0x100, true),
    DODGE_CHANGE_BUFF(0x200, true),
    CONVERSION(0x400, true),
    REAPER(0x800, true),
    MECH_CHANGE(0x2000, true), //determined in packet by [skillLevel or something] [skillid] 1E E0 58 52???
    DARK_AURA(0x8000, true),
    BLUE_AURA(0x10000, true),
    YELLOW_AURA(0x20000, true),
    //ENERGY_CHARGE(0x80000000000L, true),
    ENERGY_CHARGE(0x2000000000L, true),
    DASH(1610612736L),
    DASH_SPEED(549755813888L),
    DASH_JUMP(0x4000000000L),
    //DASH_SPEED(0x100000000000L, true),
    // DASH_JUMP(0x200000000000L, true),
    疾驰移动(549755813888L),
    疾驰跳跃(0x4000000000L),
    // MONSTER_RIDING(0x400000000000L, true),
    // SPEED_INFUSION(0x800000000000L, true),
    // HOMING_BEACON(0x1000000000000L, true),
    MONSTER_RIDING(0x10000000000L, true),
    SPEED_INFUSION(/*
     * 0x40000000000L
             */140737488355328L, true),
    HOMING_BEACON(0x80000000000L, true),
    ELEMENT_RESET(0x200000000000000L, true),
    // BODY_PRESSURE(0x8000000000000000L, true),
    BODY_PRESSURE(17592186044416L, true),
    SMART_KNOCKBACK(0x1, false),
    PYRAMID_PQ(0x2, false),
    LIGHTNING_CHARGE(0x4, false),
    //POST BB
    //DUMMY_STAT0     (0x8000000L, true), //appears on login
    //DUMMY_STAT1     (0x10000000L, true),
    //DUMMY_STAT2     (0x20000000L, true),
    //DUMMY_STAT3     (0x40000000L, true),
    //DUMMY_STAT4     (0x80000000L, true),

    SOUL_STONE(0x20000000000L, true), //same as pyramid_pq
    MAGIC_SHIELD(0x800000000000L, true),
    MAGIC_RESISTANCE(0x1000000000000L, true),
    SOARING(0x4000000000000L, true),
    //    LIGHTNING_CHARGE(0x10000000000000L, true),
    //db stuff
    MIRROR_IMAGE(0x20000000000000L, true),
    OWL_SPIRIT(0x40000000000000L, true),
    FINAL_CUT(0x100000000000000L, true),
    THORNS(0x200000000000000L, true),
    DAMAGE_BUFF(0x400000000000000L, true),
    RAINING_MINES(0x1000000000000000L, true),
    ENHANCED_MAXHP(0x2000000000000000L, true),
    ENHANCED_MAXMP(0x4000000000000000L, true),
    ENHANCED_WATK(0x8000000000000000L, true),
    MORPH(0x2),
    RECOVERY(0x4),
    MAPLE_WARRIOR(0x8),
    STANCE(0x10),
    SHARP_EYES(0x20),
    MANA_REFLECTION(0x40),
    DRAGON_ROAR(0x80), // Stuns the user

    SPIRIT_CLAW(0x100),
    INFINITY(0x200),
    HOLY_SHIELD(0x400),
    HAMSTRING(0x800),
    BLIND(0x1000),
    CONCENTRATE(0x2000),
    ECHO_OF_HERO(0x8000),
    稳住泰山(0x8000),//1221002
    UNKNOWN3(0x10000),
    GHOST_MORPH(0x20000),
    ARIANT_COSS_IMU(0x40000), // The white ball around you

    DROP_RATE(0x100000),
    MESO_RATE(0x200000),
    EXPRATE(0x400000),
    ACASH_RATE(0x800000),
    GM_HIDE(0x1000000),
    UNKNOWN7(0x2000000),
    ILLUSION(0x4000000),
    BERSERK_FURY(0x8000000),
    DIVINE_BODY(0x10000000),
    SPARK(0x20000000),
    ARIANT_COSS_IMU2(0x40000000), // no idea, seems the same
    FINALATTACK(0x80000000L),
    WATK(0x100000000L),
    WDEF(0x200000000L),
    MATK(0x400000000L),
    MDEF(0x800000000L),
    ACC(0x1000000000L),
    AVOID(0x2000000000L),
    HANDS(0x4000000000L),
    SPEED(0x8000000000L),
    JUMP(0x10000000000L),
    MAGIC_GUARD(0x20000000000L),
    DARKSIGHT(0x40000000000L),
    BOOSTER(0x80000000000L),
    POWERGUARD(0x100000000000L),
    MAXHP(0x200000000000L),
    MAXMP(0x400000000000L),
    INVINCIBLE(0x800000000000L),
    SOULARROW(0x1000000000000L),
    SUMMON(0x20000000000000L), //hack buffstat for summons ^.- (does/should not increase damage... hopefully <3)
    WK_CHARGE(0x40000000000000L),//减少MP？

    DRAGONBLOOD(0x80000000000000L),//减少生命

    HOLY_SYMBOL(0x100000000000000L),//神圣祈祷
    神圣祈祷(0x100000000000000L),//神圣祈祷
    MESOUP(0x200000000000000L),
    SHADOWPARTNER(0x400000000000000L),
    PICKPOCKET(0x800000000000000L),
    PUPPET(0x800000000000000L), // HACK - shares buffmask with pickpocket - odin special ^.-

    能量(0x2000000000L, true),
    能量获取(0x2000000000L, true),
    骑宠技能(1099511627776L),
    MESOGUARD(0x1000000000000000L),
    /**
     * ***<战神技能mask>****
     */
    //矛连击强化(4294967296L, 3),//攻击力的mask
    //矛连击强化(0x100000000L, 3),
    //矛连击强化(0x40, 3),
    ARAN_COMBO(0x1000000000000000L, true),
    //连环吸血
    COMBO_DRAIN(0x2000000000000000L, true),
    COMBO_BARRIER(0x4000000000000000L, true),
    矛连击强化(8589934592L, true),
    矛连击强化防御(8589934592L, true),//防御的mask
    矛连击强化魔法防御(34359738368L, true),//魔法防御的mask
    抗压(17592186044416L),//伤害反击的mask 17592186044416
    //连环吸血(18014398509481984L),//吸血技能的mask
    灵巧击退(140737488355328L), //减少怪物碰撞伤害百分比的mask
    战神之盾(140737488355328L),//减少怪物碰撞伤害百分比的mask
    /////////////
    物理攻击(0x100000000L),
    物理防御(0x200000000L),
    魔法攻击(0x400000000L),
    魔法防御(0x800000000L),
    命中率(0x1000000000L),
    回避率(0x2000000000L),
    手技(0x4000000000L),
    移动速度(0x8000000000L),
    跳跃力(0x10000000000L),
    魔法盾(0x20000000000L),
    隐身术(0x40000000000L),
    攻击加速(0x80000000000L),
    伤害反击(0x100000000000L),
    最大HP(0x200000000000L),
    最大MP(0x400000000000L),
    神之保护(0x800000000000L),
    无形箭弩(0x1000000000000L),
    斗气集中(0x20000000000000L),
    COMBO(0x20000000000000L),
    召唤兽(0x20000000000000L), //hack buffstat for summons ^.- (does/should not increase damage... hopefully <3)
    属性攻击(0x40000000000000L),
    龙之力(0x80000000000000L),
    聚财术(0x200000000000000L),
    敛财术(0x800000000000000L),
    替身术(0x800000000000000L), // HACK - shares buffmask with 敛财术 - odin special ^.-
    金钱护盾(0x1000000000000000L),
    金属机甲(0x2000, true), //determined in packet by [skillLevel or something] [skillid] 1E E0 58 52???  机甲改变？
    黑暗灵气(0x8000, true),
    蓝色灵气(0x10000, true),
    黄色灵气(0x20000, true),
    灵魂助力(0x800000, 4),
    能量获得(0x2000000000L, true),
    骑兽技能(0x10000000000L, true),
    极速领域(0x40000000000L, true),//修复极速领域技能38掉线
    //导航辅助(0x100000000000L, true),
    //导航辅助(0x20000000, 12),
    导航辅助(0x40000000, 8), //probably changed
    自然力重置(0x200000000000000L, true),
    //灵巧击退(0x1, false),//?
    灵魂之石(0x20000000000L, true), //等同于金字塔PQ
    提高队员攻击力_BUFF(0x400000000000000L, true),
    增强_最大HP(0x2000000000000000L, true),
    增强_最大MP(0x4000000000000000L, true),
    增强_物理攻击(0x8000000000000000L, true),
    增强_物理防御力(0x1, true),
    增强_魔法防御力(0x2, true),
    变身(0x2),
    团队治疗(0x4),
    冒险岛勇士(0x8),
    稳如泰山(0x10),
    火眼晶晶(0x20),
    魔法反击(0x40),
    龙咆哮(0x80), // Stuns the user
    暗器伤人(0x100),
    终极无限(0x200),
    圣灵之盾(0x400),
    击退箭(0x800),
    刺眼箭(0x1000),
    集中精力(0x2000),
    英雄之回声(0x8000),
    掉落_率(0x100000),
    金币_率(0x200000),
    经验_率(0x400000),
    现金_率(0x800000),
    GM_隐藏术(0x1000000),//不知道是什么。服务端也没有调用
    狂暴战魂(0x8000000),
    金刚霸体(0x10000000),
    闪光击(0x20000000),
    终极弓剑(0x80000000L),
    冰雪矛(0x4000, 2),
    连环吸血(0x2000000000000000L, true),
    战神抗压(0x20, 3),
    // 爆击强化(0x20,true),
    //矛连击强化防御(8589934592L, true),//防御的mask
    //矛连击强化魔法防御(34359738368L, true),//魔法防御的mask
    //抗压(17592186044416L),//伤害反击的mask 17592186044416
    //连环吸血(18014398509481984L),//吸血技能的mask
    //战神之盾(140737488355328L),//减少怪物碰撞伤害百分比的mask
    //矛连击强化(0x40, 3),

    WEAKEN(0x4000000000000000L),//不知道是什么。服务端也没有调用
    子弹数量(0xFFFFF, 1);

    /**
     * ***<无法识别的技能>*****
     */
    //矛连击强化(0x40, 3),
    /**
     * *************************
     */
    private static final long serialVersionUID = 0L;
    private final long buffstat;
    private final long maskPos;
    private final boolean first;

    MapleBuffStat(long buffstat) {
        this.buffstat = buffstat;
        this.maskPos = 4L;
        first = false;
    }

    MapleBuffStat(long buffstat, long maskPos) {
        this.buffstat = buffstat;
        this.maskPos = maskPos;
        this.first = false;
    }

    MapleBuffStat(long buffstat, boolean first) {
        this.buffstat = buffstat;
        this.maskPos = 4L;
        this.first = first;
    }

    public long getMaskPos() {
        return this.maskPos;
    }

    public final boolean isFirst() {
        return first;
    }

    public final long getValue() {
        return buffstat;
    }

}
