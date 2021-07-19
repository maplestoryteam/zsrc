package pvp;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleStat;
import handling.channel.handler.AttackInfo;
import handling.world.World;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.MaplePacketCreator;
import tools.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static gui.QQMsgServer.sendMsgToQQGroup;
import static pvp.Pvpskill.*;

public class MaplePvp {

    /**
     * 声明类型
     */
    private static int 基础攻击力;
    private static int maxDis;
    private static int maxHeight;
    private static boolean isAoe;
    public static boolean isLeft;
    public static boolean isRight;

    /**
     * DamageBalancer 攻击判断区域
     *
     * @param player 自己
     * @param attackedPlayers 目标
     */
    private static void DamageBalancer(AttackInfo attack, MapleCharacter player) {
        /**
         * 普通高攻击 *******************
         */
        if (attack.skill == 0) {
            基础攻击力 = SK(50, 100);
            maxDis = 300;
            maxHeight = 35;
            /**
             * 近战攻击 *********************
             */
        } else if (近战攻击(attack)) {
            maxDis = 130;
            maxHeight = 40;
            isAoe = false;
            int skillleve = player.getSkillLevel(attack.skill);
            if (gui.Start.PVP技能伤害.get("" + attack.skill + "") != null) {
                基础攻击力 = skillleve * gui.Start.PVP技能伤害.get("" + attack.skill + "");
            } else {
                基础攻击力 = (int) (Math.floor(Math.random() * (400 - 200) + 100));
            }
        } else if (远程攻击(attack)) {
            maxDis = 300;
            maxHeight = 35;
            isAoe = false;
            int skillleve = player.getSkillLevel(attack.skill);
            if (gui.Start.PVP技能伤害.get("" + attack.skill + "") != null) {
                基础攻击力 = skillleve * gui.Start.PVP技能伤害.get("" + attack.skill + "");
            } else {
                基础攻击力 = (int) (Math.floor(Math.random() * (300 - 200) + 150));
            }
        } else if (群体攻击(attack)) {
            maxDis = 350;
            maxHeight = 350;
            isAoe = true;
            int skillleve = player.getSkillLevel(attack.skill);
            if (gui.Start.PVP技能伤害.get("" + attack.skill + "") != null) {
                基础攻击力 = skillleve * gui.Start.PVP技能伤害.get("" + attack.skill + "");
            } else {
                基础攻击力 = (int) (Math.floor(Math.random() * (400 - 250) + 250));
            }
        } else {
            maxDis = 350;
            maxHeight = 350;
        }
    }

    /**
     * BUFF 攻击后判断的BUFF
     *
     * @param player 自己
     * @param attackedPlayers 目标
     */
    private static void BUFF(MapleCharacter player, MapleCharacter attackedPlayers) {
        /*
        *初始化BUFF类型
         */
        Integer mguard = attackedPlayers.getBuffedValue(MapleBuffStat.MAGIC_GUARD);
        Integer mesoguard = attackedPlayers.getBuffedValue(MapleBuffStat.MESOGUARD);
        if (mguard != null) {
            List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>(1);
            int mploss = (int) (基础攻击力 / .5);
            基础攻击力 *= .70;
            if (mploss > attackedPlayers.getMp()) {
                基础攻击力 /= .70;
                attackedPlayers.cancelBuffStats(MapleBuffStat.MAGIC_GUARD);
            } else {
                attackedPlayers.setMp(attackedPlayers.getMp() - mploss);
                stats.add(new Pair<MapleStat, Integer>(MapleStat.MP, attackedPlayers.getMp()));
                attackedPlayers.getClient().sendPacket(MaplePacketCreator.updatePlayerStats(stats, player.getJob()));
            }
        } else if (mesoguard != null) {
            int mesoloss = (int) (基础攻击力 * .75);
            基础攻击力 *= .75;
            if (mesoloss > attackedPlayers.getMeso()) {
                基础攻击力 /= .75;
                attackedPlayers.cancelBuffStats(MapleBuffStat.MESOGUARD);
            } else {
                attackedPlayers.gainMeso(-mesoloss, false);
            }
        }

    }

    /**
     * killMonster jisha
     *
     * @param player 自己
     * @param attackedPlayers 目标
     * @param map 地图
     */
    private static void killMonster(MapleCharacter player, MapleCharacter attackedPlayers, MapleMap map) {
        //KDA计算
        int KDA1 = player.GetPvpkills() - player.GetPvpdeaths() / 2;
        int KDA2 = attackedPlayers.GetPvpkills() - attackedPlayers.GetPvpdeaths() / 2;
        //召唤透明怪物
        MapleMonster pvpMob = MapleLifeFactory.getMonster(9400711);
        //给予双方数值
        player.GainPvpkills(1);
        attackedPlayers.GainPvpdeaths(1);
        //推送公告信息
        player.getClient().sendPacket(MaplePacketCreator.serverNotice(6, "[决斗信息]:你杀死了 " + attackedPlayers.getName() + " ,战斗值上升了！"));
        attackedPlayers.getClient().sendPacket(MaplePacketCreator.serverNotice(6, "[决斗信息]:你被 " + player.getName() + " 杀死了，战斗值减少了！"));
        sendMsgToQQGroup("[决斗信息] : [" + KDA1 + "](" + player.getName() + ") 击败了 [" + KDA2 + "](" + attackedPlayers.getName() + ") 。");
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[决斗信息] : [" + KDA1 + "](" + player.getName() + ") 击败了 [" + KDA2 + "](" + attackedPlayers.getName() + ") 。"));
        //清空透明怪物
        map.killMonster(pvpMob, player, false, false, (byte) 1);
    }

    /**
     * monsterBomb
     *
     * @param player 自己
     * @param attackedPlayers 目标
     * @param map 地图
     * @param attack 技能
     */
    private static void monsterBomb(MapleCharacter player, MapleCharacter attackedPlayers, MapleMap map, AttackInfo attack) {
        //判断BUFF技能
        BUFF(player, attackedPlayers);
        //召唤出透明怪物
        MapleMonster pvpMob = MapleLifeFactory.getMonster(9400711);
        map.spawnMonsterOnGroundBelow(pvpMob, attackedPlayers.getPosition());
        for (int attacks = 0; attacks < attack.hits; attacks++) {
            //攻击目标后显示
            attackedPlayers.getClient().sendPacket(MaplePacketCreator.damagePlayer(0, pvpMob.getId(), attackedPlayers.getId(), 基础攻击力, 0, attack.animation, 0, false, pvpMob.getId(), attackedPlayers.getPosition().x, attackedPlayers.getPosition().y));
            player.getClient().sendPacket(MaplePacketCreator.damagePlayer(-1, pvpMob.getId(), attackedPlayers.getId(), 基础攻击力, 0, attack.animation, 0, false, pvpMob.getId(), attackedPlayers.getPosition().x, attackedPlayers.getPosition().y));
            attackedPlayers.addHP(-基础攻击力);
        }
        //被攻击的目标显示
        attackedPlayers.getClient().sendPacket(MaplePacketCreator.sendHint("#b<受到伤害>\r\n\r\n#e#r " + 基础攻击力 + "\r\n", 200, 2));
        //目标死亡后执行语句
        if (attackedPlayers.getHp() <= 0 && !attackedPlayers.isAlive()) {
            killMonster(player, attackedPlayers, map);
        }
    }

    public static void doPvP(MapleCharacter player, MapleMap map, AttackInfo attack) {

        DamageBalancer(attack, player);
        if (isAoe) {
            isLeft = true;
            isRight = true;
            for (MapleCharacter attackedPlayers : player.getMap().getNearestPvpChar(player.getPosition(), maxDis, maxHeight, Collections.unmodifiableCollection(player.getMap().getCharacters()))) {
                if (attackedPlayers.isAlive() && (player.getParty() == null || player.getParty() != attackedPlayers.getParty())) {
                    monsterBomb(player, attackedPlayers, map, attack);
                }
            }
        } else if (attack.animation < 0) {// && attack.stance <= 0) {
            isLeft = true;
            isRight = false;
            for (MapleCharacter attackedPlayers : player.getMap().getNearestPvpChar(player.getPosition(), maxDis, maxHeight, Collections.unmodifiableCollection(player.getMap().getCharacters()))) {
                if (attackedPlayers.isAlive() && (player.getParty() == null || player.getParty() != attackedPlayers.getParty())) {
                    monsterBomb(player, attackedPlayers, map, attack);
                }
            }
        } else {
            isLeft = false;
            isRight = true;
            for (MapleCharacter attackedPlayers : player.getMap().getNearestPvpChar(player.getPosition(), maxDis, maxHeight, Collections.unmodifiableCollection(player.getMap().getCharacters()))) {
                if (attackedPlayers.isAlive() && (player.getParty() == null || player.getParty() != attackedPlayers.getParty())) {
                    monsterBomb(player, attackedPlayers, map, attack);
                }
            }
        }

    }

}
///*
//    private static void DamageBalancer(AttackInfo attack, MapleCharacter player) {
//        /**
//         * 普通高攻击 *******************
//         */
//        if (attack.skill == 0) {
//            基础攻击力 = SK(50, 100);
//            maxDis = 300;
//            maxHeight = 35;
//            /**
//             * 近战攻击 *********************
//             */
//        } else if (近战攻击(attack)) {
//            maxDis = 130;
//            maxHeight = 40;
//            isAoe = false;
//            int skillleve = player.getSkillLevel(attack.skill);
//            switch (attack.skill) {
//                //强力攻击
//                case 1001004:
//                    基础攻击力 = skillleve * skilname("强力攻击", "强力攻击+");
//                    break;
//                //群体攻击
//                case 1001005:
//                    isAoe = true;
//                    基础攻击力 = skillleve * skilname("群体攻击", "群体攻击+");
//                    break;
//                //二连击
//                case 4001334:
//                    基础攻击力 = skillleve * skilname("二连击", "二连击+");
//                    break;
//                //百裂拳
//                case 5001001:
//                    基础攻击力 = skillleve * skilname("百裂拳", "百裂拳+");
//                    break;
//                //百裂拳
//                case 5001002:
//                    基础攻击力 = skillleve * skilname("半月踢", "半月踢+");
//                    break;
//                //百裂拳
//                case 1121008:
//                    基础攻击力 = skillleve * skilname("轻舞飞扬", "轻舞飞扬+");
//                    break;
//                //连环环破
//                case 1221009:
//                    基础攻击力 = skillleve * skilname("连环环破", "连环环破+");
//                    break;
//                //枪连击
//                case 1311001:
//                    基础攻击力 = skillleve * skilname("枪连击", "枪连击+");
//                    break;
//                //矛连击
//                case 1311002:
//                    基础攻击力 = skillleve * skilname("矛连击", "矛连击+");
//                    break;
//                //无双枪
//                case 1311003:
//                    基础攻击力 = skillleve * skilname("无双枪", "无双枪+");
//                    break;
//                //无双矛
//                case 1311004:
//                    基础攻击力 = skillleve * skilname("无双矛", "无双矛+");
//                    break;
//                //龙之献祭
//                case 1311005:
//                    基础攻击力 = skillleve * skilname("龙之献祭", "龙之献祭+");
//                    break;
//                //强弓
//                case 3101003:
//                    基础攻击力 = skillleve * skilname("强弓", "强弓+");
//                    break;
//                //强弩
//                case 3201003:
//                    基础攻击力 = skillleve * skilname("强弩", "强弩+");
//                    break;
//                //突进
//                case 1121006:
//                case 1221007:
//                case 1321003:
//                    基础攻击力 = SK(100, 200);
//                default:
//                    基础攻击力 = (int) (Math.floor(Math.random() * (500 - 250) + 250));
//                    break;
//            }
//            /**
//             * 远程攻击 *********************
//             */
//        } else if (远程攻击(attack)) {
//            maxDis = 300;
//            maxHeight = 35;
//            isAoe = false;
//            int skillleve = player.getSkillLevel(attack.skill);
//            switch (attack.skill) {
//                //魔法弹
//                case 2001004:
//                    基础攻击力 = skillleve * skilname("魔法弹", "魔法弹+");
//                    break;
//                //魔法双击
//                case 2001005:
//                    基础攻击力 = skillleve * skilname("魔法双击", "魔法双击+");
//                    break;
//                //双飞斩
//                case 4001344:
//                    基础攻击力 = skillleve * skilname("双飞斩", "双飞斩+");
//                    break;
//                //断魂箭
//                case 3001004:
//                    基础攻击力 = skillleve * skilname("断魂箭", "断魂箭+");
//                    break;
//                //二连射
//                case 3001005:
//                    基础攻击力 = skillleve * skilname("二连射", "二连射+");
//                    break;
//                //双弹射击
//                case 5001003:
//                    基础攻击力 = skillleve * skilname("双弹射击", "双弹射击+");
//                    break;
//                //火焰箭
//                case 2101004:
//                    基础攻击力 = skillleve * skilname("火焰箭", "火焰箭+");
//                    break;
//                //毒雾术
//                case 2101005:
//                    基础攻击力 = skillleve * skilname("毒雾术", "毒雾术+");
//                    break;
//                //火毒合击
//                case 2111006:
//                    基础攻击力 = skillleve * skilname("火毒合击", "火毒合击+");
//                    break;
//                //火凤球
//                case 2121003:
//                    基础攻击力 = skillleve * skilname("火凤球", "火凤球+");
//                    break;
//                //冰冻术
//                case 2201004:
//                    基础攻击力 = skillleve * skilname("冰冻术", "冰冻术+");
//                    break;
//                //落雷枪
//                case 2211003:
//                    基础攻击力 = skillleve * skilname("落雷枪", "落雷枪+");
//                    break;
//                //冰雷合击
//                case 2211006:
//                    基础攻击力 = skillleve * skilname("冰雷合击", "冰雷合击+");
//                    break;
//                //冰凤球
//                case 2221003:
//                    基础攻击力 = skillleve * skilname("冰凤球", "冰凤球+");
//                    break;
//                //圣箭术
//                case 2301005:
//                    基础攻击力 = skillleve * skilname("圣箭术", "圣箭术+");
//                    break;
//                //光芒飞箭
//                case 2321007:
//                    基础攻击力 = skillleve * skilname("光芒飞箭", "光芒飞箭+");
//                    break;
//                //箭扫射
//                case 3111006:
//                    基础攻击力 = skillleve * skilname("箭扫射", "箭扫射+");
//                    break;
//                //飞龙冲击波
//                case 3121003:
//                    基础攻击力 = skillleve * skilname("飞龙冲击波", "飞龙冲击波+");
//                    break;
//                //暴风箭雨
//                case 3121004:
//                    基础攻击力 = skillleve * skilname("暴风箭雨", "暴风箭雨+");
//                    break;
//                //箭扫射
//                case 3211006:
//                    基础攻击力 = skillleve * skilname("箭扫射2", "箭扫射2+");
//                    break;
//                //穿透箭
//                case 3221001:
//                    基础攻击力 = skillleve * skilname("穿透箭2", "穿透箭2+");
//                    break;
//                //一击要害箭
//                case 3221007:
//                    基础攻击力 = skillleve * skilname("一击要害箭", "一击要害箭+");
//                    break;
//                //生命吸收
//                case 4101005:
//                    基础攻击力 = skillleve * skilname("生命吸收", "生命吸收+");
//                    break;
//                //金钱攻击
//                case 4111004:
//                    基础攻击力 = skillleve * skilname("金钱攻击", "金钱攻击+");
//                    break;
//                //多重飞镖
//                case 4111005:
//                    isAoe = true;
//                    基础攻击力 = skillleve * skilname("多重飞镖", "多重飞镖+");
//                    break;
//                //挑衅
//                case 4121003:
//                    isAoe = true;
//                    基础攻击力 = skillleve * skilname("挑衅", "挑衅+");
//                    break;
//                //三连环光击破
//                case 4121007:
//                    isAoe = true;
//                    基础攻击力 = skillleve * skilname("三连环光击破", "三连环光击破+");
//                    break;
//
//                default:
//                    基础攻击力 = (int) (Math.floor(Math.random() * (400 - 250) + 250));
//                    break;
//            }
//            /**
//             * 群体攻击 *********************
//             */
//        } else if (群体攻击(attack)) {
//            maxDis = 350;
//            maxHeight = 350;
//            isAoe = true;
//            int skillleve = player.getSkillLevel(attack.skill);
//            switch (attack.skill) {
//                //虎咆哮
//                case 1111008:
//                    基础攻击力 = skillleve * skilname("虎咆哮", "虎咆哮+");
//                    break;
//                //属性攻击
//                case 1211002:
//                    基础攻击力 = skillleve * skilname("属性攻击", "属性攻击+");
//                    break;
//                //圣域
//                case 1221011:
//                    基础攻击力 = skillleve * skilname("圣域", "圣域+");
//                    break;
//                //龙咆哮
//                case 1311006:
//                    基础攻击力 = skillleve * skilname("龙咆哮", "龙咆哮+");
//                    break;
//                //末日烈焰
//                case 2111002:
//                    基础攻击力 = skillleve * skilname("末日烈焰", "末日烈焰+");
//                    break;
//                //致命毒雾
//                case 2111003:
//                    基础攻击力 = skillleve * skilname("致命毒雾", "致命毒雾+");
//                    break;
//                //创世之破1
//                case 2121001:
//                    基础攻击力 = skillleve * skilname("创世之破1", "创世之破1+");
//                    break;
//                //连环爆破
//                case 2121006:
//                    基础攻击力 = skillleve * skilname("连环爆破", "连环爆破+");
//                    break;
//                //天降落星
//                case 2121007:
//                    基础攻击力 = skillleve * skilname("天降落星", "天降落星+");
//                    break;
//                //雷电术
//                case 2201005:
//                    基础攻击力 = skillleve * skilname("雷电术", "雷电术+");
//                    break;
//                //冰咆哮
//                case 2211002:
//                    基础攻击力 = skillleve * skilname("冰咆哮", "冰咆哮+");
//                    break;
//                //创世之破2
//                case 2221001:
//                    基础攻击力 = skillleve * skilname("创世之破2", "创世之破2+");
//                    break;
//                //链环闪电
//                case 2221006:
//                    基础攻击力 = skillleve * skilname("链环闪电", "链环闪电+");
//                    break;
//                //落霜冰破
//                case 2221007:
//                    基础攻击力 = skillleve * skilname("落霜冰破", "落霜冰破+");
//                    break;
//                //落霜冰破
//                case 2311004:
//                    基础攻击力 = skillleve * skilname("圣光", "圣光+");
//                    break;
//                //创世之破3
//                case 2321001:
//                    基础攻击力 = skillleve * skilname("创世之破3", "创世之破3+");
//                    break;
//                //圣光普照
//                case 2321008:
//                    基础攻击力 = skillleve * skilname("圣光普照", "圣光普照+");
//                    break;
//                //爆炸箭
//                case 3101005:
//                    基础攻击力 = skillleve * skilname("爆炸箭", "爆炸箭+");
//                    break;
//                //烈焰箭
//                case 3111003:
//                    基础攻击力 = skillleve * skilname("烈焰箭", "烈焰箭+");
//                    break;
//                //箭雨
//                case 3111004:
//                    基础攻击力 = skillleve * skilname("箭雨", "箭雨+");
//                    break;
//                //穿透箭
//                case 3201005:
//                    基础攻击力 = skillleve * skilname("穿透箭", "穿透箭+");
//                    break;
//                //寒冰箭
//                case 3211003:
//                    基础攻击力 = skillleve * skilname("寒冰箭", "寒冰箭+");
//                    break;
//                //升龙弩
//                case 3211004:
//                    基础攻击力 = skillleve * skilname("升龙弩", "升龙弩+");
//                    break;
//                //影网术
//                case 4111003:
//                    基础攻击力 = skillleve * skilname("影网术", "影网术+");
//                    break;
//                //忍者伏击
//                case 4121004:
//                    基础攻击力 = skillleve * skilname("忍者伏击", "忍者伏击+");
//                    break;
//                //忍者冲击
//                case 4121008:
//                    基础攻击力 = skillleve * skilname("忍者冲击", "忍者冲击+");
//                    break;
//                default:
//                    基础攻击力 = (int) (Math.floor(Math.random() * (400 - 250) + 250));
//                    break;
//            }
//        } else {
//            maxDis = 350;
//            maxHeight = 350;
//        }
//    }
//*/
