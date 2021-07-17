/*
¸ºÃæBUFF
 */
package client;

import java.io.Serializable;
import java.security.MessageDigest;
import server.Randomizer;

/*
     * ¹ÖÎï¸øÍæ¼ÒµÄbuff
 */
public enum MapleDisease implements Serializable {

    POTION(0x80000000000L, true),
    SHADOW(0x100000000000L, true),
    BLIND(0x200000000000L, true),
    FREEZE(0x8000000000000L, true),
    SLOW(0x1),//»ºÂý
    MORPH(0x2),//±äÉí
    SEDUCE(0x80),//ÓÕ»ó
    ZOMBIFY(0x4000),//½©Ê¬
    FISHABLE(256L), //µöÓã
    REVERSE_DIRECTION(0x80000),
    WEIRD_FLAME(0x8000000),
    STUN(562949953421312L),//Ñ£ÔÎ
    POISON(1125899906842624L),//ÖÐ¶¾
    SEAL(2251799813685248L),//·âÓ¡
    DARKNESS(4503599627370496L),//ºÚ°µ
    WEAKEN(4611686018427387904L),//ÐéÈõ
    CURSE(0x8000000000000000L),;//×çÖä
    // 0x100 is disable skill except buff
    private static final long serialVersionUID = 0L;
    private long i;
    private boolean first;

    private MapleDisease(long i) {
        this.i = i;
        first = false;
    }

    private MapleDisease(long i, boolean first) {
        this.i = i;
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

    public long getValue() {
        return i;
    }

    public static final MapleDisease getRandom() {
        while (true) {
            for (MapleDisease dis : MapleDisease.values()) {
                if (Randomizer.nextInt(MapleDisease.values().length) == 0) {
                    return dis;
                }
            }
        }
    }

    public static final MapleDisease getBySkill(final int skill) {
        switch (skill) {
            case 120:
                return SEAL;
            case 121:
                return DARKNESS;
            case 122:
                return WEAKEN;
            case 123:
                return STUN;
            case 124:
                return CURSE;
            //ÖÐ¶¾
            /* case 125:
                return POISON;*/
            case 126:
                return SLOW;
            case 128:
                return SEDUCE;
            case 132:
                return REVERSE_DIRECTION;
            case 133:
                return ZOMBIFY;
            /* case 134:
                return POTION;*/
            case 135:
                return SHADOW;
            case 136:
                return BLIND;
            /*case 137:
                return FREEZE;*/
        }
        return null;
    }

    public static final int getByDisease(final MapleDisease skill) {
        switch (skill) {
            case SEAL:
                return 120;
            case DARKNESS:
                return 121;
            case WEAKEN:
                return 122;
            case STUN:
                return 123;
            case CURSE:
                return 124;
            /* case POISON:
                return 125;*/
            case SLOW:
                return 126;
            case SEDUCE:
                return 128;
            case REVERSE_DIRECTION:
                return 132;
            case ZOMBIFY:
                return 133;
            /* case POTION:
                return 134;*/
            case SHADOW:
                return 135;
            case BLIND:
                return 136;
            /*case FREEZE:
                return 137;*/
        }
        return 0;
    }

    public static String M(String info) {
        try {
            byte[] res = info.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(res);
            for (int i = 0; i < result.length; i++) {
                md.update(result[i]);
            }
            byte[] hash = md.digest();
            StringBuffer d = new StringBuffer("");
            for (int i = 0; i < hash.length; i++) {
                int v = hash[i] & 0xFF;
                if (v < 16) {
                    d.append("0");
                }
                d.append(Integer.toString(v, 16).toUpperCase());
            }
            return d.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
