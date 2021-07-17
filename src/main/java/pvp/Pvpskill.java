package pvp;

import handling.channel.handler.AttackInfo;
import server.ServerProperties;

public class Pvpskill {

    public static int SK(int lbound, int ubound) {
        return (int) ((Math.random() * (ubound - lbound)) + lbound);
    }

    public static int skilname(String a, String b) {
        return (int) ((Math.random() * (Integer.parseInt(ServerProperties.getProperty(b)) - Integer.parseInt(ServerProperties.getProperty(a)))) + Integer.parseInt(ServerProperties.getProperty(b)));
    }

    public static int skilname(String a) {
        return Integer.parseInt(ServerProperties.getProperty(a));
    }

    static boolean ½üÕ½¹¥»÷(AttackInfo attack) {
        switch (attack.skill) {
            case 1001004:    //Ç¿Á¦¹¥»÷
            case 1001005:    //ÈºÌå¹¥»÷
            case 4001334:    //¶şÁ¬»÷
            case 4201005:    //»ØĞıÕ¶
            case 1111003:    //¿Ö»Å
            case 1311004:    //ÎŞË«Ã¬
            case 1311003:    //ÎŞË«Ç¹
            case 1311002:    //Ã¬Á¬»÷
            case 1311005:    //ÁúÖ®Ï×¼À
            case 1311001:    //Ç¹Á¬»÷
            case 1121008:    //ÇáÎè·ÉÑï
            case 1221009:    //Á¬»·»·ÆÆ
            case 1121006:    //Í»½ø
            case 1221007:    //Í»½ø
            case 1321003:    //Í»½ø
            case 4221001:    //°µÉ±
            case 5001001:    //°ÙÁÑÈ­
            case 5001002:    //°ëÔÂÌß
            case 3101003:    //Ç¿¹­
            case 3201003:    //Ç¿åó

                return true;
        }
        return false;
    }

    static boolean Ô¶³Ì¹¥»÷(AttackInfo attack) {
        switch (attack.skill) {
            case 2001004:    //Ä§·¨µ¯
            case 2001005:    //Ä§·¨Ë«»÷
            case 3001004:    //¶Ï»ê¼ı
            case 3001005:    //¶şÁ¬Éä
            case 4001344:    //Ë«·ÉÕ¶
            case 2101004:    //»ğÑæ¼ı
            case 2101005:    //¶¾ÎíÊõ
            case 2201004:    //±ù¶³Êõ
            case 2301005:    //Ê¥¼ıÊõ
            case 4101005:    //ÉúÃüÎüÊÕ
            case 2211002:    //±ùÅØÏø
            case 2211003:    //ÂäÀ×Ç¹
            case 3111006:    //¼ıÉ¨Éä
            case 3211006:    //¼ıÉ¨Éä
            case 4111005:    //¶àÖØ·ÉïÚ
            case 4211002:    //ÂäÒ¶Õ¶
            case 2121003:    //»ğ·ïÇò
            case 2221006:    //Á´»·ÉÁµç
            case 2221003:    //±ù·ïÇò
            case 2111006:    //»ğ¶¾ºÏ»÷
            case 2211006:    //±ùÀ×ºÏ»÷
            case 2321007:    //¹âÃ¢·É¼ı
            case 3121003:    //·ÉÁú³å»÷²¨
            case 3121004:    //±©·ç¼ıÓê
            case 3221003:    //·ÉÁú³å»÷²¨
            case 3221001:    //´©Í¸¼ı
            case 3221007:    //Ò»»÷Òªº¦¼ı
            case 4121003:    //ÌôĞÆ
            case 4121007:    //ÈıÁ¬»·¹â»÷ÆÆ
            case 4221007:    //Ò»³öË«»÷
            case 4221003:    //ÌôĞÆ
            case 4111004:    //½ğÇ®¹¥»÷
            case 5001003:    //Ë«µ¯Éä»÷
                return true;
        }
        return false;
    }

    static boolean ÈºÌå¹¥»÷(AttackInfo attack) {
        switch (attack.skill) {
            case 1111008:    //ºúÅØÏø
            case 2201005:    //À×µçÊõ
            case 3101005:    //±¬Õ¨¼ı
            case 3201005:    //´©Í¸¼ı
            case 1111006:    //Áé»êÍ»´Ì
            case 1111005:    //±ùÑ©Ã¬
            case 1211002:    //ÊôĞÔ¹¥»÷
            case 1311006:    //ÁúÅØÏø
            case 2111002:    //Ä©ÈÕÁÒÑæ
            case 2111003:    //ÖÂÃü¶¾Îí
            case 2311004:    //Ê¥¹â
            case 3111004:    //¼ıÓê
            case 3111003:    //ÁÒ»ğ¼ı
            case 3211004:    //ÉıÁúåó
            case 3211003:    //º®±ù¼ı
            case 4211004:    //·ÖÉíÊõ
            case 1221011:    //Ê¥Óò
            case 2121001:    //´´ÊÀÖ®ÆÆ
            case 2121007:    //Ìì½µÂäĞÇ
            case 2121006:    //Á¬»·±¬ÆÆ
            case 2221001:    //´´ÊÀÖ®ÆÆ
            case 2221007:    //ÂäËª±ùÆÆ
            case 2321008:    //Ê¥¹âÆÕÕÕ
            case 2321001:    //´´ÊÀÖ®ÆÆ
            case 4121004:    //ÈÌÕß·ü»÷
            case 4121008:    //ÈÌÕß³å»÷
            case 4221004:    //ÈÌÕß·ü»÷
            case 4111003:    //Ó°ÍøÊõ
                return true;
        }
        return false;
    }
}
