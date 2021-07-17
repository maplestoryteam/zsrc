/*
过图
 */package server.maps;

import static abc.访问地区.新手冒险家;
import java.awt.Point;

import client.MapleClient;
import client.MapleQuestStatus;
import client.SkillFactory;
import database.DatabaseConnection;
import handling.world.MapleParty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scripting.EventManager;
import scripting.NPCScriptManager;
import server.Randomizer;
import server.MapleItemInformationProvider;
import server.life.MapleLifeFactory;
import server.quest.MapleQuest;
import server.quest.MapleQuest.MedalQuest;
import tools.MaplePacketCreator;
import tools.packet.UIPacket;

public class MapScriptMethods {

    private static final Point witchTowerPos = new Point(-60, 184);
    private static final String[] 武陵道场入口 = {
        "我一直在等你！如果你有一丝的勇气，你会走在那个门的右边!",
        "如何勇敢的你采取的道场训练塔!",
        "我会确保你会后悔带上了道场训练塔!",
        "我做你的倡导坚韧！但不要把你的勇气与鲁莽!",
        "如果你想一步一步地走到失败的道路上，用一切方法去做!"};

    private static enum onFirstUserEnter {
        pepeking_effect,
        dojang_Eff,
        PinkBeen_before,
        onRewordMap,
        StageMsg_together,
        StageMsg_davy,
        party6weatherMsg,
        StageMsg_juliet,
        StageMsg_romio,
        moonrabbit_mapEnter,
        astaroth_summon,
        boss_Ravana,
        killing_BonusSetting,
        killing_MapSetting,
        metro_firstSetting,
        balog_bonusSetting,
        balog_summon,
        easy_balog_summon,
        Sky_TrapFEnter,
        shammos_Fenter,
        PRaid_D_Fenter,
        PRaid_B_Fenter,
        GhostF,
        NULL;

        private static onFirstUserEnter fromString(String Str) {
            try {
                return valueOf(Str);
            } catch (IllegalArgumentException ex) {
                return NULL;
            }
        }
    };

    private static enum onUserEnter {
        babyPigMap,
        crash_Dragon,
        evanleaveD,
        getDragonEgg,
        meetWithDragon,
        go1010100,
        go1010200,
        go1010300,
        go1010400,
        evanPromotion,
        PromiseDragon,
        evanTogether,
        incubation_dragon,
        TD_MC_Openning,
        TD_MC_gasi,
        TD_MC_gasi2,
        TD_MC_title,
        cygnusJobTutorial,
        cygnusTest,
        startEreb,
        dojang_Msg,
        dojang_1st,
        reundodraco,
        undomorphdarco,
        explorationPoint,
        goAdventure,
        go10000,
        go20000,
        go30000,
        go40000,
        go50000,
        go1000000,
        go1010000,
        go1020000,
        go2000000,
        go104000000,
        goArcher,
        goPirate,
        goRogue,
        goMagician,
        goSwordman,
        goLith,
        iceCave,
        mirrorCave,
        aranDirection,
        rienArrow,
        rien,
        check_count,
        Massacre_first,
        Massacre_result,
        aranTutorAlone,
        evanAlone,
        dojang_QcheckSet,
        Sky_StageEnter,
        outCase,
        balog_buff,
        balog_dateSet,
        Sky_BossEnter,
        Sky_GateMapEnter,
        shammos_Enter,
        shammos_Result,
        shammos_Base,
        dollCave00,
        dollCave01,
        Sky_Quest,
        enterBlackfrog,
        onSDI,
        blackSDI,
        summonIceWall,
        metro_firstSetting,
        start_itemTake,
        PRaid_D_Enter,
        PRaid_B_Enter,
        PRaid_Revive,
        PRaid_W_Enter,
        PRaid_WinEnter,
        PRaid_FailEnter,
        Ghost,
        NULL;

        private static onUserEnter fromString(String Str) {
            try {
                return valueOf(Str);
            } catch (IllegalArgumentException ex) {
                return NULL;
            }
        }
    };

    //5120000*下雪
    //5120001*小红花
    //5120002*小气泡
    //5120003*下雪花
    //5120008*下枫叶
    //5120004*下礼物
    //5120005*下巧克力
    //5120006*下七彩花瓣
    //5120007*下糖果
    //5120009*防烟花
    //5120010*下可乐
    public static void startScript_FirstUser(MapleClient c, String scriptName) {//地图脚本
        if (MapleParty.调试3 > 0 && c.getPlayer().isGM()) {
            c.getPlayer().dropMessage(6, "dialogue:[Script_FirstUser[ " + scriptName + " ]");

        }
        switch (onFirstUserEnter.fromString(scriptName)) {
            //武陵
            case dojang_Eff: {
                int temp = (c.getPlayer().getMapId() - 925000000) / 100;
                int stage = (int) (temp - ((temp / 100) * 100));
                sendDojoClock(c, getTiming(stage) * 60);
                sendDojoStart(c, stage - getDojoStageDec(stage));
                break;
            }
            //挑战PB
            case PinkBeen_before: {
                handlePinkBeanStart(c);
                break;
            }
            //暴力熊
            case onRewordMap: {
                reloadWitchTower(c);
                break;
            }
            case GhostF: {
                //c.getPlayer().getMap().startMapEffect("这个地图感觉阴森森的..有种莫名的奇怪感觉..", 5120025);
                break;
            }

            //地狱大公
            case astaroth_summon: {
                c.getPlayer().getMap().resetFully();
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9400633), new Point(600, -26));
                break;
            }
            case boss_Ravana: {
                c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.serverNotice(5, "那已经出现!"));
                break;
            }
            //地铁平台
            case killing_BonusSetting: {
                //spawns monsters according to mapid
                //910320010-910320029 = Train 999 bubblings.
                //926010010-926010029 = 30 Yetis
                //926010030-926010049 = 35 Yetis
                //926010050-926010069 = 40 Yetis
                //926010070-926010089 - 50 Yetis (specialized? immortality)
                //TODO also find positions to spawn these at
                c.getPlayer().getMap().resetFully();
                c.sendPacket(MaplePacketCreator.showEffect("killing/bonus/bonus"));
                c.sendPacket(MaplePacketCreator.showEffect("killing/bonus/stage"));
                Point pos1 = null, pos2 = null, pos3 = null;
                int spawnPer = 0;
                int mobId = 0;
                //9700019, 9700029
                //9700021 = one thats invincible
                if (c.getPlayer().getMapId() >= 910320010 && c.getPlayer().getMapId() <= 910320029) {
                    pos1 = new Point(121, 218);
                    pos2 = new Point(396, 43);
                    pos3 = new Point(-63, 43);
                    mobId = 9700020;
                    spawnPer = 10;
                } else if (c.getPlayer().getMapId() >= 926010010 && c.getPlayer().getMapId() <= 926010029) {
                    pos1 = new Point(0, 88);
                    pos2 = new Point(-326, -115);
                    pos3 = new Point(361, -115);
                    mobId = 9700019;
                    spawnPer = 10;
                } else if (c.getPlayer().getMapId() >= 926010030 && c.getPlayer().getMapId() <= 926010049) {
                    pos1 = new Point(0, 88);
                    pos2 = new Point(-326, -115);
                    pos3 = new Point(361, -115);
                    mobId = 9700019;
                    spawnPer = 15;
                } else if (c.getPlayer().getMapId() >= 926010050 && c.getPlayer().getMapId() <= 926010069) {
                    pos1 = new Point(0, 88);
                    pos2 = new Point(-326, -115);
                    pos3 = new Point(361, -115);
                    mobId = 9700019;
                    spawnPer = 20;
                } else if (c.getPlayer().getMapId() >= 926010070 && c.getPlayer().getMapId() <= 926010089) {
                    pos1 = new Point(0, 88);
                    pos2 = new Point(-326, -115);
                    pos3 = new Point(361, -115);
                    mobId = 9700029;
                    spawnPer = 20;
                } else {
                    break;
                }
                for (int i = 0; i < spawnPer; i++) {
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobId), new Point(pos1));
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobId), new Point(pos2));
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobId), new Point(pos3));
                }
                c.getPlayer().startMapTimeLimitTask(120, c.getPlayer().getMap().getReturnMap());
                break;
            }

            case balog_summon:
            case easy_balog_summon: {
                break;
            }
            case metro_firstSetting:
            case killing_MapSetting:
            case Sky_TrapFEnter:
            case balog_bonusSetting: { //not needed
                c.getPlayer().getMap().resetFully();
                break;
            }
            default: {
                //  System.out.println("未處理的腳本 : " + scriptName + ", 型態 : onUserEnter - 地圖ID " + c.getPlayer().getMapId());
                //    FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "未處理的腳本 : " + scriptName + ", 型態 : onUserEnter - 地圖ID " + c.getPlayer().getMapId());
                break;
            }
        }

    }//地图1

    public static void startScript_User(final MapleClient c, String scriptName) {

        if (MapleParty.调试3 > 0 && c.getPlayer().isGM()) {
            c.getPlayer().dropMessage(6, "dialogue:[Script_User[ " + scriptName + " ]");
        }
        String data = "";
        switch (onUserEnter.fromString(scriptName)) {

            case go10000:
            case go20000:
            case go30000:
            case go40000:
            case go50000:
            case go1000000:
            case go1020000:
            case go104000000:
                c.sendPacket(UIPacket.IntroDisableUI(false));
                c.sendPacket(UIPacket.IntroLock(false));
            //c.sendPacket(MaplePacketCreator.enableActions());
            case go2000000:
            case go1010000:
            case go1010100:
            case go1010200:
            case go1010300:
            case go1010400: {
                c.sendPacket(UIPacket.MapNameDisplay(c.getPlayer().getMapId()));
                break;
            }
            //冒险家出啊恒动画，0
            case goAdventure: {
                showIntro2(c, "Effect/Direction3.img/goAdventure/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            }
            //骑士团动画，913040100
            case cygnusTest:
            case cygnusJobTutorial: {
                showIntro(c, "Effect/Direction.img/cygnusJobTutorial/Scene" + (c.getPlayer().getMapId() - 913040100));
                break;
            }
            //女神塔任务，920010000
            case start_itemTake: { //nothing to go on inside the map
                final EventManager em = c.getChannelServer().getEventSM().getEventManager("OrbisPQ");
                if (em != null && em.getProperty("pre").equals("0")) {
                    NPCScriptManager.getInstance().dispose(c);
                    NPCScriptManager.getInstance().start(c, 2013001);
                }
                break;
            }
            //战神920010000
            case startEreb:
            case mirrorCave:
            case babyPigMap:
            case evanleaveD: {
                c.sendPacket(UIPacket.IntroDisableUI(false));
                c.sendPacket(UIPacket.IntroLock(false));
                c.sendPacket(MaplePacketCreator.enableActions());
                break;
            }
            //武陵入口
            case dojang_Msg: {
                c.getPlayer().getMap().startMapEffect(武陵道场入口[Randomizer.nextInt(武陵道场入口.length)], 5120024);
                break;
            }
            //武陵关口卡挑战提示
            case dojang_1st: {
                c.getPlayer().writeMulungEnergy();
                break;
            }
            //变身成龙
            case undomorphdarco:
            case reundodraco: {
                c.getPlayer().cancelEffect(MapleItemInformationProvider.getInstance().getItemEffect(2210016), false, -1);
                break;
            }
            //战神动画
            case aranDirection: {
                switch (c.getPlayer().getMapId()) {
                    case 914090010:
                        data = "Effect/Direction1.img/aranTutorial/Scene0";
                        break;
                    case 914090011:
                        data = "Effect/Direction1.img/aranTutorial/Scene1" + (c.getPlayer().getGender() == 0 ? "0" : "1");
                        break;
                    case 914090012:
                        data = "Effect/Direction1.img/aranTutorial/Scene2" + (c.getPlayer().getGender() == 0 ? "0" : "1");
                        break;
                    case 914090013:
                        data = "Effect/Direction1.img/aranTutorial/Scene3";
                        break;
                    case 914090100:
                        data = "Effect/Direction1.img/aranTutorial/HandedPoleArm" + (c.getPlayer().getGender() == 0 ? "0" : "1");
                        break;
                    case 914090200:
                        data = "Effect/Direction1.img/aranTutorial/Maha";
                        break;
                }
                showIntro(c, data);
                break;
            }

            //914090001
            //蘑菇城动画
            case TD_MC_Openning: {
                //一开始执行这个
                showIntro2(c, "Effect/Direction2.img/open/back0");
                showIntro2(c, "Effect/Direction2.img/open/back1");
                showIntro2(c, "Effect/Direction2.img/open/chat");
                showIntro2(c, "Effect/Direction2.img/open/frame");
                showIntro2(c, "Effect/Direction2.img/open/light");
                showIntro2(c, "Effect/Direction2.img/open/line");
                showIntro2(c, "Effect/Direction2.img/open/out");
                showIntro2(c, "Effect/Direction2.img/open/pepeKing");
                showIntro2(c, "Effect/Direction2.img/open/violeta0");
                showIntro2(c, "Effect/Direction2.img/open/violeta1");
                break;
            }
            //蘑菇城动画
            case TD_MC_gasi: {
                showIntro2(c, "Effect/Direction2.img/gasi/gasi1");
                showIntro2(c, "Effect/Direction2.img/gasi/gasi2");
                showIntro2(c, "Effect/Direction2.img/gasi/gasi22");
                showIntro2(c, "Effect/Direction2.img/gasi/gasi3");
                showIntro2(c, "Effect/Direction2.img/gasi/gasi4");
                showIntro2(c, "Effect/Direction2.img/gasi/gasi5");
                showIntro2(c, "Effect/Direction2.img/gasi/gasi6");
                showIntro2(c, "Effect/Direction2.img/gasi/gasi7");
                showIntro2(c, "Effect/Direction2.img/gasi/gasi8");
//                showIntro2(c, "Effect/Direction2.img/gasi");
                break;
            }
            case TD_MC_gasi2: {
                c.sendPacket(UIPacket.IntroDisableUI(false));
                c.sendPacket(UIPacket.IntroLock(false));
                c.sendPacket(MaplePacketCreator.enableActions());
                break;
            }
            //蘑菇城出来
            case TD_MC_title: {
                c.sendPacket(UIPacket.IntroDisableUI(false));
                c.sendPacket(UIPacket.IntroLock(false));
                c.sendPacket(MaplePacketCreator.enableActions());
                c.sendPacket(UIPacket.MapEff("temaD/enter/mushCatle"));
                break;
            }
            //战神
            case iceCave: {
                //教程技能
                c.getPlayer().changeSkillLevel(SkillFactory.getSkill(20000014), (byte) -1, (byte) 0);
                c.getPlayer().changeSkillLevel(SkillFactory.getSkill(20000015), (byte) -1, (byte) 0);
                c.getPlayer().changeSkillLevel(SkillFactory.getSkill(20000016), (byte) -1, (byte) 0);
                c.getPlayer().changeSkillLevel(SkillFactory.getSkill(20000017), (byte) -1, (byte) 0);
                c.getPlayer().changeSkillLevel(SkillFactory.getSkill(20000018), (byte) -1, (byte) 0);
                c.sendPacket(UIPacket.ShowWZEffect("Effect/Direction1.img/aranTutorial/ClickLirin", -1));
                c.sendPacket(UIPacket.IntroDisableUI(false));
                c.sendPacket(UIPacket.IntroLock(false));
                c.sendPacket(MaplePacketCreator.enableActions());
                break;
            }
            //战神
            case rienArrow: {
                if (c.getPlayer().getInfoQuest(21019).equals("miss=o;helper=clear")) {
                    c.getPlayer().updateInfoQuest(21019, "miss=o;arr=o;helper=clear");
                    c.sendPacket(UIPacket.AranTutInstructionalBalloon("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3"));
                }
                break;
            }
            //明珠港动画
            case goLith: {
                showIntro2(c, "Effect/Direction3.img/goLith/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            }
            //职业动画
            case goArcher: {
                showIntro2(c, "Effect/Direction3.img/archer/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            }
            //职业动画
            case goPirate: {
                showIntro2(c, "Effect/Direction3.img/pirate/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            }
            //职业动画
            case goRogue: {
                showIntro2(c, "Effect/Direction3.img/rogue/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            }
            //职业动画
            case goMagician: {
                showIntro2(c, "Effect/Direction3.img/magician/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            }
            //职业动画
            case goSwordman: {
                showIntro2(c, "Effect/Direction3.img/swordman/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            }
            //航行？
            case rien: {
                if (c.getPlayer().getQuestStatus(21101) == 2 && c.getPlayer().getInfoQuest(21019).equals("miss=o;arr=o;helper=clear")) {
                    c.getPlayer().updateInfoQuest(21019, "miss=o;arr=o;ck=1;helper=clear");
                }
                c.sendPacket(UIPacket.IntroDisableUI(false));
                c.sendPacket(UIPacket.IntroLock(false));
                break;
            }
            //地铁，金字塔
            case Massacre_first: {
                if (c.getPlayer().getPyramidSubway() == null) {
                    c.getPlayer().setPyramidSubway(new Event_PyramidSubway(c.getPlayer()));
                }
                break;
            }
            //地铁，金字塔失败
            case Massacre_result: {
                c.sendPacket(MaplePacketCreator.showEffect("pvp/lose"));
                break;
            }
            //每个地图都会执行这个
            case explorationPoint: {
                //这是明珠港
                if (c.getPlayer().getMapId() == 104000000) {
                    c.sendPacket(UIPacket.IntroDisableUI(false));
                    c.sendPacket(UIPacket.IntroLock(false));
                    c.sendPacket(MaplePacketCreator.enableActions());
                    c.sendPacket(UIPacket.MapNameDisplay(c.getPlayer().getMapId()));
                }
                break;
            }
            default: {
                // System.out.println("未处理脚本 : " + scriptName);
                //System.out.println("地图代码   : " + c.getPlayer().getMapId());
                //FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "未處理的腳本 : " + scriptName + ", 型態 : onUserEnter - 地圖ID " + c.getPlayer().getMapId());
                break;
            }
        }

    }//地图2

    public static void Gainquestinfo2(String Name, int Channale, int Piot) {
        try {
            int ret = Getquestinfo2(Name, Channale);
            if (ret == -1) {
                ret = 0;
                PreparedStatement ps = null;
                try {
                    ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO questinfo2 (channel, Name,Point) VALUES (?, ?, ?)");
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
            PreparedStatement ps = con.prepareStatement("UPDATE questinfo2 SET `Point` = ? WHERE Name = ? and channel = ?");
            ps.setInt(1, ret);
            ps.setString(2, Name);
            ps.setInt(3, Channale);
            ps.execute();
            ps.close();
        } catch (SQLException sql) {
            System.err.println("Getquestinfo2!!55" + sql);
        }
    }

    public static int Getquestinfo2(String Name, int Channale) {
        int ret = -1;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM questinfo2 WHERE channel = ? and Name = ?");
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

    private static final int getTiming(int ids) {
        if (ids <= 5) {
            return 5;
        } else if (ids >= 7 && ids <= 11) {
            return 6;
        } else if (ids >= 13 && ids <= 17) {
            return 7;
        } else if (ids >= 19 && ids <= 23) {
            return 8;
        } else if (ids >= 25 && ids <= 29) {
            return 9;
        } else if (ids >= 31 && ids <= 35) {
            return 10;
        } else if (ids >= 37 && ids <= 38) {
            return 15;
        }
        return 0;
    }

    private static final int getDojoStageDec(int ids) {
        if (ids <= 5) {
            return 0;
        } else if (ids >= 7 && ids <= 11) {
            return 1;
        } else if (ids >= 13 && ids <= 17) {
            return 2;
        } else if (ids >= 19 && ids <= 23) {
            return 3;
        } else if (ids >= 25 && ids <= 29) {
            return 4;
        } else if (ids >= 31 && ids <= 35) {
            return 5;
        } else if (ids >= 37 && ids <= 38) {
            return 6;
        }
        return 0;
    }

    private static void showIntro(final MapleClient c, final String data) {
        c.sendPacket(UIPacket.IntroDisableUI(true));
        c.sendPacket(UIPacket.IntroLock(true));
        //c.sendPacket(UIPacket.ShowWZEffect(data, -1));
        c.sendPacket(UIPacket.ShowWZEffectS(data, -1));
    }

    private static void showIntro2(final MapleClient c, final String data) {
        c.sendPacket(UIPacket.IntroDisableUI(true));
        c.sendPacket(UIPacket.IntroLock(true));
        c.sendPacket(UIPacket.ShowWZEffectS(data, -1));
    }

    private static void sendDojoClock(MapleClient c, int time) {
        c.sendPacket(MaplePacketCreator.getClock(time));
    }

    private static void sendDojoStart(MapleClient c, int stage) {
        c.sendPacket(MaplePacketCreator.environmentChange("Dojang/start", 4));
        c.sendPacket(MaplePacketCreator.environmentChange("dojang/start/stage", 3));
        c.sendPacket(MaplePacketCreator.environmentChange("dojang/start/number/" + stage, 3));
        c.sendPacket(MaplePacketCreator.trembleEffect(0, 1));
    }

    //挑战PB的时候召唤NPC
    private static void handlePinkBeanStart(MapleClient c) {
        final MapleMap map = c.getPlayer().getMap();
        if (!map.containsNPC(2141000) && c.getPlayer().getMap().getAllMonstersThreadsafe().isEmpty()) {
            map.resetFully();
            map.spawnNpc(2141000, new Point(-190, -42));
        }
    }

    //暴力熊？
    private static void reloadWitchTower(MapleClient c) {
        final MapleMap map = c.getPlayer().getMap();
        map.killAllMonsters(false);

        final int level = c.getPlayer().getLevel();
        int mob;
        if (level <= 10) {
            mob = 9300367;
        } else if (level <= 20) {
            mob = 9300368;
        } else if (level <= 30) {
            mob = 9300369;
        } else if (level <= 40) {
            mob = 9300370;
        } else if (level <= 50) {
            mob = 9300371;
        } else if (level <= 60) {
            mob = 9300372;
        } else if (level <= 70) {
            mob = 9300373;
        } else if (level <= 80) {
            mob = 9300374;
        } else if (level <= 90) {
            mob = 9300375;
        } else if (level <= 100) {
            mob = 9300376;
        } else {
            mob = 9300377;
        }
        map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mob), witchTowerPos);
    }
}//地图脚本
/*
        if (gui.Start.ConfigValuesMap.get("下雪天开关") <= 0) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120000, false));
        } else if (gui.Start.ConfigValuesMap.get("下红花开关") <= 0) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120001, false));
        } else if (gui.Start.ConfigValuesMap.get("下气泡开关") <= 0) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120002, false));
        } else if (gui.Start.ConfigValuesMap.get("下雪花开关") <= 0) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120003, false));
        } else if (gui.Start.ConfigValuesMap.get("下枫叶开关") <= 0) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.startMapEffect("", 5120008, false));
        }
        /*NPCScriptManager.getInstance().dispose(c);
        NPCScriptManager.getInstance().start(c, 2000, 1);//*/
 /*if (c.getPlayer().getLevel() > 0 && c.getPlayer().getMapId() != 910320001
                && c.getPlayer().getMapId() != 910320000 && c.getPlayer().getMapId() != 910320100
                && c.getPlayer().getMapId() != 910320200 && c.getPlayer().getMapId() != 910320300
                && c.getPlayer().getMapId() != 926010100 && c.getPlayer().getMapId() != 926010010
                && c.getPlayer().getMapId() != 926020001 && c.getPlayer().getMapId() != 926020002
                && c.getPlayer().getMapId() != 926020003 && c.getPlayer().getMapId() != 926020004
                && c.getPlayer().getMapId() != 926013100 && c.getPlayer().getMapId() != 270050100) {
            NPCScriptManager.getInstance().dispose(c);
            NPCScriptManager.getInstance().start(c, 2000, 1);//
            return;
        }*/
//c.getPlayer().getMap().setPermanentWeather(5120000);
/* case shammos_Enter: { //nothing to go on inside the map
                // c.sendPacket(MaplePacketCreator.sendPyramidEnergy("shammos_LastStage", String.valueOf((c.getPlayer().getMapId() % 1000) / 100)));
                if (c.getPlayer().getEventInstance() != null && c.getPlayer().getMapId() == 921120500) {
                    NPCScriptManager.getInstance().dispose(c); //only boss map.
                    NPCScriptManager.getInstance().start(c, 2022006);
                }
                break;
            }*/

 /* case PRaid_W_Enter: {
                // c.sendPacket(MaplePacketCreator.sendPyramidEnergy("PRaid_expPenalty", "0"));
                //  c.sendPacket(MaplePacketCreator.sendPyramidEnergy("PRaid_ElapssedTimeAtField", "0"));
                // c.sendPacket(MaplePacketCreator.sendPyramidEnergy("PRaid_Point", "-1"));
                //  c.sendPacket(MaplePacketCreator.sendPyramidEnergy("PRaid_Bonus", "-1"));
                //  c.sendPacket(MaplePacketCreator.sendPyramidEnergy("PRaid_Total", "-1"));
                //  c.sendPacket(MaplePacketCreator.sendPyramidEnergy("PRaid_Team", ""));
                //  c.sendPacket(MaplePacketCreator.sendPyramidEnergy("PRaid_IsRevive", "0"));
                //   c.getPlayer().writePoint("PRaid_Point", "-1");
                //  c.getPlayer().writeStatus("Red_Stage", "1");
                //  c.getPlayer().writeStatus("Blue_Stage", "1");
                //   c.getPlayer().writeStatus("redTeamDamage", "0");
                //   c.getPlayer().writeStatus("blueTeamDamage", "0");
                break;
            }
            case PRaid_D_Enter:
            case PRaid_B_Enter:
            case PRaid_WinEnter: //handled by event
            case PRaid_FailEnter: //also
            case PRaid_Revive: //likely to subtract points or remove a life, but idc rly
            case metro_firstSetting:
            case blackSDI:
            case summonIceWall:
            case onSDI:
            case enterBlackfrog:
            case Sky_Quest: //forest that disappeared 240030102
            case dollCave00:
            case dollCave01:
            case shammos_Base:
            case shammos_Result:
            case Sky_BossEnter:
            case Sky_GateMapEnter:
            case balog_dateSet:
            case balog_buff:
            case outCase:
            case Sky_StageEnter:
            case dojang_QcheckSet:
            case evanTogether:
            case aranTutorAlone:
            case Ghost: {
                //c.getPlayer().getMap().startMapEffect("这个地图感觉阴森森的..有一种莫名的奇怪感觉..", 5120025);
                break;
            }
            case evanAlone: {
                c.sendPacket(MaplePacketCreator.enableActions());
                break;
            }
            case crash_Dragon:
                showIntro2(c, "Effect/Direction4.img/crash/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            case getDragonEgg:
                showIntro2(c, "Effect/Direction4.img/getDragonEgg/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            case meetWithDragon:
                showIntro2(c, "Effect/Direction4.img/meetWithDragon/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;
            case PromiseDragon:
                showIntro2(c, "Effect/Direction4.img/PromiseDragon/Scene" + (c.getPlayer().getGender() == 0 ? "0" : "1"));
                break;*/
 /*case go2000000:
            case go1010000:
            case go1010100:
            case go1010200:
            case go1010300:
            case go1010400: {
                c.sendPacket(UIPacket.MapNameDisplay(c.getPlayer().getMapId()));//开关地图名称显示
                break;
            }
            case check_count: {
                if (c.getPlayer().getMapId() == 950101010 && (!c.getPlayer().haveItem(4001433, 20) || c.getPlayer().getLevel() < 50)) { //ravana Map
                    final MapleMap mapp = c.getChannelServer().getMapFactory().getMap(950101100); //exit Map
                    c.getPlayer().changeMap(mapp, mapp.getPortal(0));
                }
                break;
MedalQuest m = null;
//                for (MedalQuest mq : MedalQuest.values()) {
//                    for (int i : mq.maps) {
//                        if (c.getPlayer().getMapId() == i) {
//                            m = mq;
//                            break;
//                        }
//                    }
//                }
//                if (m != null && c.getPlayer().getLevel() >= m.level && c.getPlayer().getQuestStatus(m.questid) != 2) {
//                    if (c.getPlayer().getQuestStatus(m.lquestid) != 1) {
//                        MapleQuest.getInstance(m.lquestid).forceStart(c.getPlayer(), 0, "0");
//                    }
//                    if (c.getPlayer().getQuestStatus(m.questid) != 1) {
//                        MapleQuest.getInstance(m.questid).forceStart(c.getPlayer(), 0, null);
//                        final StringBuilder sb = new StringBuilder("enter=");
//                        for (int i = 0; i < m.maps.length; i++) {
//                            sb.append("0");
//                        }
//                        c.getPlayer().updateInfoQuest(m.questid - 2005, sb.toString());
//                        MapleQuest.getInstance(m.questid - 1995).forceStart(c.getPlayer(), 0, "0");
//                    }
//                    final String quest = c.getPlayer().getInfoQuest(m.questid - 2005);
//                    final MapleQuestStatus stat = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(m.questid - 1995));
//                    if (stat.getCustomData() == null) {
//                        stat.setCustomData("0");
//                    }
//                    int number = Integer.parseInt(stat.getCustomData());
//                    final StringBuilder sb = new StringBuilder("enter=");
//                    boolean changedd = false;
//                    for (int i = 0; i < m.maps.length; i++) {
//                        boolean changed = false;
//                        try {
//                            if (c.getPlayer().getMapId() == m.maps[i]) {
//                                if (quest.substring(i + 6, i + 7).equals("0")) {
//                                    sb.append("1");
//                                    changed = true;
//                                    changedd = true;
//                                }
//                            }
//                            if (!changed) {
//                                sb.append(quest.substring(i + 6, i + 7));
//                            }
//                        } catch (Exception ex) {
//                        }
//                    }
//                    //访问任务
//                    if (changedd) {
//                        number++;
//                        c.getPlayer().updateInfoQuest(m.questid - 2005, sb.toString());
//                        MapleQuest.getInstance(m.questid - 1995).forceStart(c.getPlayer(), 0, String.valueOf(number));
//                        c.getPlayer().dropMessage(5, "称号 [" + String.valueOf(m) + "] 访问 [" + number + " / " + m.maps.length + "] 个地区。");
//                        // c.sendPacket(MaplePacketCreator.showQuestMsg("称号 " + String.valueOf(m) + " 已访问 " + number + "/" + m.maps.length + " 个地区"));
//                    }
//                }
            }.

 if (新手冒险家(c.getPlayer().getMapId()) && Getquestinfo2("新手冒险家", c.getPlayer().getId()) >= 20) {
                    int 玩家 = c.getPlayer().getId();

                    if (Getquestinfo2("104000000", 玩家) == 0) {
                        Gainquestinfo2("104000000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("104010001", 玩家) == 0) {
                        Gainquestinfo2("104010001", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("100000006", 玩家) == 0) {
                        Gainquestinfo2("100000006", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("104020000", 玩家) == 0) {
                        Gainquestinfo2("104020000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("100000000", 玩家) == 0) {
                        Gainquestinfo2("100000000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("100010000", 玩家) == 0) {
                        Gainquestinfo2("100010000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("100040000", 玩家) == 0) {
                        Gainquestinfo2("100040000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("100040100", 玩家) == 0) {
                        Gainquestinfo2("100040100", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("101010103", 玩家) == 0) {
                        Gainquestinfo2("101010103", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("101020000", 玩家) == 0) {
                        Gainquestinfo2("101020000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("101000000", 玩家) == 0) {
                        Gainquestinfo2("101000000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("102000000", 玩家) == 0) {
                        Gainquestinfo2("102000000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("101030104", 玩家) == 0) {
                        Gainquestinfo2("101030104", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("101030406", 玩家) == 0) {
                        Gainquestinfo2("101030406", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("102020300", 玩家) == 0) {
                        Gainquestinfo2("102020300", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("103000000", 玩家) == 0) {
                        Gainquestinfo2("103000000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("102050000", 玩家) == 0) {
                        Gainquestinfo2("102050000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("103010001", 玩家) == 0) {
                        Gainquestinfo2("103010001", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("103030200", 玩家) == 0) {
                        Gainquestinfo2("103030200", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    } else if (Getquestinfo2("110000000", 玩家) == 0) {
                        Gainquestinfo2("110000000", 玩家, 1);
                        Gainquestinfo2("新手冒险家", 玩家, 1);
                        c.getPlayer().dropMessage(5, "称号 [ 新手冒险家 ] 需要访问 [ " + Getquestinfo2("新手冒险家", 玩家) + " / 20 ] 个地区。");
                    }
                }
 */
