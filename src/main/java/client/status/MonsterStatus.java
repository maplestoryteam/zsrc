/*
BUFFÐ§¹û·â°ü
 */
package client.status;

import java.io.Serializable;

public enum MonsterStatus implements Serializable {
    NEUTRALISE(0x02), // µÖÏû
    WATK(0x100000000L),
    WDEF(0x200000000L),
    MATK(0x400000000L),
    MDEF(0x800000000L),
    ACC(0x1000000000L),
    AVOID(0x2000000000L),//ÉÁ±Ü
    SPEED(0x4000000000L),//¼ÓËÙÐ§¹û
    /*
    ²»¶¯
    »èÃÔ£¬±ù¶³
     */
    STUN(0x8000000000L),
    ±ù¶³(256), //±ù¶³
    FREEZE(256), //±ù¶³
    ÖÐ¶¾(0x20000000000L),//ÖÐ¶¾
    POISON(0x20000000000L),//ÖÐ¶¾
    ·âÓ¡(0x40000000000L),//·âÓ¡
    SEAL(0x40000000000L),//·âÓ¡
    SHOWDOWN(0x80000000000L),
    WEAPON_ATTACK_UP(0x100000000000L),
    WEAPON_DEFENSE_UP(0x200000000000L),
    MAGIC_ATTACK_UP(0x400000000000L),
    MAGIC_DEFENSE_UP(0x800000000000L),
    DOOM(0x1000000000000L),
    SHADOW_WEB(0x2000000000000L),
    WEAPON_IMMUNITY(0x4000000000000L),
    MAGIC_IMMUNITY(0x8000000000000L),
    DAMAGE_IMMUNITY(0x20000000000000L),
    NINJA_AMBUSH(0x40000000000000L),
    VENOMOUS_WEAPON(0x100000000000000L),
    DARKNESS(0x200000000000000L),
    EMPTY(0x800000000000000L),
    HYPNOTIZE(0x1000000000000000L),
    WEAPON_DAMAGE_REFLECT(0x2000000000000000L),
    MAGIC_DAMAGE_REFLECT(0x4000000000000000L),
    SUMMON(0x8000000000000000L)
    ;
    static final long serialVersionUID = 0L;
    private final long i;
    private final boolean first;

    MonsterStatus(long i) {
        this.i = i;
        first = false;
    }

    MonsterStatus(int i, boolean first) {
        this.i = i;
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isEmpty() {
        return this == SUMMON || this == EMPTY;
    }

    public long getValue() {
        return i;
    }
}

    /*WATK(0x1),
    WDEF(0x2),
    MATK(0x4),
    MDEF(0x8),
    ACC(0x10),
    AVOID(0x20),
    SPEED(0x40),
    STUN(0x80), //this is possibly only the bowman stun
    FREEZE(0x100),
    POISON(0x200),
    SEAL(0x400),
    TAUNT(0x800),
    WEAPON_ATTACK_UP(0x1000),
    WEAPON_DEFENSE_UP(0x2000),
    MAGIC_ATTACK_UP(0x4000),
    MAGIC_DEFENSE_UP(0x8000),
    DOOM(0x10000),
    SHADOW_WEB(0x20000),
    WEAPON_IMMUNITY(0x40000),
    MAGIC_IMMUNITY(0x80000),
    NINJA_AMBUSH(0x400000),
    HYPNOTIZED(0x10000000),
    VENOMOUS_WEAPON(0x1000000L),
    DARKNESS(0x2000000L),
    EMPTY(0x8000000L),
    HYPNOTIZE(0x10000000L),
    WEAPON_DAMAGE_REFLECT(0x20000000L),
    MAGIC_DAMAGE_REFLECT(0x40000000L),
    SUMMON(0x80000000L) //all summon bag mobs have.*/
