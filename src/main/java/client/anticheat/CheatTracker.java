package client.anticheat;

import client.MapleCharacter;
import constants.GameConstants;
import handling.world.World;
import server.AutobanManager;
import server.Timer.CheatTimer;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CheatTracker {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock rL = lock.readLock(), wL = lock.writeLock();
    private final Map<CheatingOffense, CheatingOffenseEntry> offenses = new LinkedHashMap<CheatingOffense, CheatingOffenseEntry>();
    private final WeakReference<MapleCharacter> chr;
    // For keeping track of speed attack hack.
    private int lastAttackTickCount = 0;
    private byte Attack_tickResetCount = 0;
    private long Server_ClientAtkTickDiff = 0;
    private long lastDamage = 0;
    private long takingDamageSince;
    private int numSequentialDamage = 0;
    private long lastDamageTakenTime = 0;
    private byte numZeroDamageTaken = 0;
    private int numSequentialSummonAttack = 0;
    private long summonSummonTime = 0;
    private int numSameDamage = 0;
    private Point lastMonsterMove;
    private int monsterMoveCount;
    private int attacksWithoutHit = 0;
    private byte dropsPerSecond = 0;
    private long lastDropTime = 0;
    private byte msgsPerSecond = 0;
    private long lastMsgTime = 0;
    private ScheduledFuture<?> invalidationTask;
    private int gm_message = 50;
    private int lastTickCount = 0, tickSame = 0;
    private long lastASmegaTime = 0;
    private final long[] lastTime = new long[6];

    public CheatTracker(final MapleCharacter chr) {
        this.chr = new WeakReference<MapleCharacter>(chr);
        invalidationTask = CheatTimer.getInstance().register(new InvalidationTask(), 60000);
        takingDamageSince = System.currentTimeMillis();
    }
//    public final void 攻速检测(final MapleCharacter player ) {
//        //战士职业群
//        if (player.getJob() >= 100 && player.getJob() < 200) {
//            //英雄职业群
//            if (player.getJob() == 112) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 英雄攻速检测 = gui.Start.ConfigValuesMap.get("英雄攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 英雄攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX112");
//                    return;
//                }
//            } else if (player.getJob() == 122) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 圣骑士攻速检测 = gui.Start.ConfigValuesMap.get("圣骑士攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 圣骑士攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX122");
//                    return;
//                }
//            } else if (player.getJob() == 132) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 黑骑士攻速检测 = gui.Start.ConfigValuesMap.get("黑骑士攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 黑骑士攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX132");
//                    return;
//                }
//            } else if (player.getJob() == 111) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 勇士攻速检测 = gui.Start.ConfigValuesMap.get("勇士攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 勇士攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX111");
//                    return;
//                }
//            } else if (player.getJob() == 121) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 骑士攻速检测 = gui.Start.ConfigValuesMap.get("骑士攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 骑士攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX121");
//                    return;
//                }
//            } else if (player.getJob() == 131) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 龙骑士攻速检测 = gui.Start.ConfigValuesMap.get("龙骑士攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 龙骑士攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX131");
//                    return;
//                }
//            } else if (player.getJob() == 110) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 剑客攻速检测 = gui.Start.ConfigValuesMap.get("剑客攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 剑客攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX110");
//                    return;
//                }
//            } else if (player.getJob() == 120) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 准骑士攻速检测 = gui.Start.ConfigValuesMap.get("准骑士攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 准骑士攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX120");
//                    return;
//                }
//            } else if (player.getJob() == 130) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 枪战士攻速检测 = gui.Start.ConfigValuesMap.get("枪战士攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 枪战士攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX130");
//                    return;
//                }
//            } else if (player.getJob() == 100) {
//                
//                long nowTimestamp = System.currentTimeMillis();
//                int 战士攻速检测 = gui.Start.ConfigValuesMap.get("战士攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 战士攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX100");
//                    return;
//                }
//            }
//        }
//        //魔法师职业类
//        if (player.getJob() >= 200 && player.getJob() < 300) {
//            if (player.getJob() == 200) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 魔法师攻速检测 = gui.Start.ConfigValuesMap.get("魔法师攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 魔法师攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX200");
//                    return;
//                }
//            } else if (player.getJob() == 212) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 火毒魔导师攻速检测 = gui.Start.ConfigValuesMap.get("火毒魔导师攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 火毒魔导师攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX212");
//                    return;
//                }
//            } else if (player.getJob() == 222) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 冰雷魔导师攻速检测 = gui.Start.ConfigValuesMap.get("冰雷魔导师攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 冰雷魔导师攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX222");
//                    return;
//                }
//            } else if (player.getJob() == 232) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 主教攻速检测 = gui.Start.ConfigValuesMap.get("主教攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 主教攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX232");
//                    return;
//                }
//            } else if (player.getJob() == 211) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 火毒巫师攻速检测 = gui.Start.ConfigValuesMap.get("火毒巫师攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 火毒巫师攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX211");
//                    return;
//                }
//             } else if (player.getJob() == 211) {
//                long nowTimestamp = System.currentTimeMillis();
//                int 火毒巫师攻速检测 = gui.Start.ConfigValuesMap.get("火毒巫师攻速检测");
//                if (nowTimestamp - player.角色攻速检测 < 火毒巫师攻速检测) {
//                    player.highSpeedGamageCount++;
//                } else {
//                    player.角色攻速检测 = nowTimestamp;
//                    player.highSpeedGamageCount = 0;
//                }
//                if (player.highSpeedGamageCount > 15) {
//                    player.dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你使用加速类插件\r\n代码：YX211");
//                    return;
//                }
//            }
//        }
//    }

    public final void 锁定伤害(final int eachd) {
        if (lastDamage == eachd) {
            numSameDamage++;

            if (numSameDamage > 5) {
                numSameDamage = 0;
                //dropMessage(1, "<天谴之兆>\r\n\r\n警告：系统检测到你锁定伤害");
            }
        } else {
            lastDamage = eachd;
            numSameDamage = 0;
        }
    }

    /*public final void checkSameDamage(final int dmg) {
        if (dmg > 2000 && lastDamage == dmg) {
            numSameDamage++;

            if (numSameDamage > 5) {
                numSameDamage = 0;
                registerOffense(CheatingOffense.伤害相同, numSameDamage + " times: " + dmg);

            }
        } else {
            lastDamage = dmg;
            numSameDamage = 0;
        }
    }*/
    public final void checkAttack(final int skillId, final int tickcount) {
        final short AtkDelay = GameConstants.getAttackDelay(skillId);
        /*if ((tickcount - lastAttackTickCount) < AtkDelay) {
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "变速! "));
               
            registerOffense(CheatingOffense.快速攻击);
        }*/

        final long STime_TC = System.currentTimeMillis() - tickcount; // hack = - more
        if (Server_ClientAtkTickDiff - STime_TC > 250) { // 250 is the ping, TODO
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "变速! "));
            registerOffense(CheatingOffense.快速攻击2);
        }
        // if speed hack, client tickcount values will be running at a faster pace
        // For lagging, it isn't an issue since TIME is running simotaniously, client
        // will be sending values of older time

//	System.out.println("Delay [" + skillId + "] = " + (tickcount - lastAttackTickCount) + ", " + (Server_ClientAtkTickDiff - STime_TC));
        Attack_tickResetCount++; // Without this, the difference will always be at 100
        if (Attack_tickResetCount >= (AtkDelay <= 200 ? 2 : 4)) {
            Attack_tickResetCount = 0;
            Server_ClientAtkTickDiff = STime_TC;
        }
        chr.get().updateTick(tickcount);
        lastAttackTickCount = tickcount;
    }

    public final void checkTakeDamage(final int damage) {//触碰无敌MISS检测
        numSequentialDamage++;
        lastDamageTakenTime = System.currentTimeMillis();

        // System.out.println("tb" + timeBetweenDamage);
        // System.out.println("ns" + numSequentialDamage);
        // System.out.println(timeBetweenDamage / 1500 + "(" + timeBetweenDamage / numSequentialDamage + ")");
        /* if (lastDamageTakenTime - takingDamageSince / 500 < numSequentialDamage) {
            registerOffense(CheatingOffense.怪物碰撞过快);//检测
        }*/
        if (lastDamageTakenTime - takingDamageSince > 4500) {
            takingDamageSince = lastDamageTakenTime;
            numSequentialDamage = 0;
        }
        /*
         * (non-thieves) Min Miss Rate: 2% Max Miss Rate: 80% (thieves) Min Miss
         * Rate: 5% Max Miss Rate: 95%
         */
        if (damage == 0) {
            numZeroDamageTaken++;
            if (numZeroDamageTaken >= 35) { // Num count MSEA a/b players
                numZeroDamageTaken = 0;

                registerOffense(CheatingOffense.回避率过高);//检测
            }

        } else if (damage != -1) {
            numZeroDamageTaken = 0;
        }
    }

    public final void checkMoveMonster(final Point pos, MapleCharacter chr) {

        //double dis = Math.abs(pos.distance(lastMonsterMove));
        if (pos.equals(this.lastMonsterMove)) {
            monsterMoveCount++;
            /*if (monsterMoveCount > 50) {
                //   registerOffense(CheatingOffense.MOVE_MONSTERS);
                monsterMoveCount = 0;
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理员信息] 开挂玩家[" + MapleCharacterUtil.makeMapleReadable(chr.getName()) + "] 地图ID[" + chr.getMapId() + "] 怀疑使用吸怪! "));
                String note = "时间：" + FileoutputUtil.CurrentReadable_Time() + " "
                        + "|| 玩家名字：" + chr.getName() + ""
                        + "|| 玩家地图：" + chr.getMapId() + "\r\n";
                FileoutputUtil.packetLog("log\\吸怪检测\\" + chr.getName() + ".log", note);
            }*///检测
            /*
             * } else if ( dis > 1500 ) { monsterMoveCount++; if
             * (monsterMoveCount > 15) {
             * registerOffense(CheatingOffense.MOVE_MONSTERS); }
             */
        } else {
            lastMonsterMove = pos;
            monsterMoveCount = 1;
        }
    }

    public final void resetSummonAttack() {
        summonSummonTime = System.currentTimeMillis();
        numSequentialSummonAttack = 0;
    }

    public final boolean checkSummonAttack() {
        numSequentialSummonAttack++;
        //estimated
        // System.out.println(numMPRegens + "/" + allowedRegens);
        /*if ((System.currentTimeMillis() - summonSummonTime) / (2000 + 1) < numSequentialSummonAttack) {
            registerOffense(CheatingOffense.召唤兽快速攻击);
            return false;
        }*///检测
        return true;
    }

    public final void checkDrop() {
        checkDrop(false);
    }

    public final void checkDrop(final boolean dc) {
        if ((System.currentTimeMillis() - lastDropTime) < 1000) {
            dropsPerSecond++;
            if (dropsPerSecond >= (dc ? 32 : 16) && chr.get() != null) {
//                if (dc) {
//                    chr.get().getClient().getSession().close();
//                } else {
                chr.get().getClient().setMonitored(true);
//                }
            }
        } else {
            dropsPerSecond = 0;
        }
        lastDropTime = System.currentTimeMillis();
    }

    public boolean canAvatarSmega2() {
        if (lastASmegaTime + 10000 > System.currentTimeMillis() && chr.get() != null && !chr.get().isGM()) {
            return false;
        }
        lastASmegaTime = System.currentTimeMillis();
        return true;
    }

    public synchronized boolean GMSpam(int limit, int type) {
        if (type < 0 || lastTime.length < type) {
            type = 1; // default xD
        }
        if (System.currentTimeMillis() < limit + lastTime[type]) {
            return true;
        }
        lastTime[type] = System.currentTimeMillis();
        return false;
    }

    public final void checkMsg() { //ALL types of msg. caution with number of  msgsPerSecond
        if ((System.currentTimeMillis() - lastMsgTime) < 1000) { //luckily maplestory has auto-check for too much msging
            msgsPerSecond++;
            /*
             * if (msgsPerSecond > 10 && chr.get() != null) {
             * chr.get().getClient().getSession().close(); }
             */
        } else {
            msgsPerSecond = 0;
        }
        lastMsgTime = System.currentTimeMillis();
    }

    public final int getAttacksWithoutHit() {
        return attacksWithoutHit;
    }

    public final void setAttacksWithoutHit(final boolean increase) {
        if (increase) {
            this.attacksWithoutHit++;
        } else {
            this.attacksWithoutHit = 0;
        }
    }

    public final void registerOffense(final CheatingOffense offense) {
        registerOffense(offense, null);
    }

    public final void registerOffense(final CheatingOffense offense, final String param) {
        final MapleCharacter chrhardref = chr.get();
        if (chrhardref == null || !offense.isEnabled() || chrhardref.isClone() || chrhardref.isGM()) {
            return;
        }
        CheatingOffenseEntry entry = null;
        rL.lock();
        try {
            entry = offenses.get(offense);
        } finally {
            rL.unlock();
        }
        if (entry != null && entry.isExpired()) {
            expireEntry(entry);
            entry = null;
        }
        if (entry == null) {
            entry = new CheatingOffenseEntry(offense, chrhardref.getId());
        }
        if (param != null) {
            entry.setParam(param);
        }
        entry.incrementCount();
        if (offense.shouldAutoban(entry.getCount())) {
            final byte type = offense.getBanType();
            if (type == 1) {
                AutobanManager.getInstance().autoban(chrhardref.getClient(), StringUtil.makeEnumHumanReadable(offense.name()));
            } else if (type == 2) {
                //怪物碰撞过快 回避率过高 快速攻击 快速攻击2 怪物移动 伤害相同
                String outputFileName = "断线";
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[GM信息] " + chrhardref.getName() + " 自动断线 类别: " + offense + " 原因: " + (param == null ? "" : (" - " + param))));
                FileoutputUtil.logToFile_chr(chrhardref, "logs/Hack/" + outputFileName + ".txt", "\r\n " + FileoutputUtil.NowTime() + " 类别" + offense + " 原因 " + (param == null ? "" : (" - " + param)));
                chrhardref.getClient().getSession().close();
                //chrhardref.getClient().getSession().close();
            } else if (type == 3) {
            }
            gm_message = 50;
            return;
        }
        wL.lock();
        try {
            offenses.put(offense, entry);
        } finally {
            wL.unlock();
        }
        switch (offense) {

            case 魔法伤害过高:
            case 魔法伤害过高2:
            case 攻击过高2:
            case 攻击力过高:
            case 快速攻击:
            case 快速攻击2:
            case 攻击范围过大:
            case 召唤兽攻击范围过大:
            case 伤害相同:

            case 吸怪:
            case 怪物移动:

            case 回避率过高:
                String show = offense.name();
                gm_message--;
                if (gm_message % 5 == 0) {
                    String msg = "[管理员信息] " + chrhardref.getName() + " 疑似 " + show
                            + "地图ID [" + chrhardref.getMapId() + "]" + ""
                            + (param == null ? "" : (" - " + param));
                    World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, msg));
                    FileoutputUtil.logToFile_chr(chrhardref, FileoutputUtil.hack_log, show);
                }
                if (gm_message == 0) {

                    // System.out.println(MapleCharacterUtil.makeMapleReadable(chrhardref.getName()) + "怀疑使用外挂");
                    // World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[管理員訊息] 开挂玩家[" + MapleCharacterUtil.makeMapleReadable(chrhardref.getName()) + "] 地图ID[" + chrhardref.getMapId() + "] suspected of hacking! " + StringUtil.makeEnumHumanReadable(offense.name()) + (param == null ? "" : (" - " + param))));
                    /*
                     * String note = "时间：" +
                     * FileoutputUtil.CurrentReadable_Time() + " " + "|| 玩家名字："
                     * + chrhardref.getName() + "" + "|| 玩家地图：" +
                     * chrhardref.getMapId() + "" + "|| 外挂类型：" + offense.name()
                     * + "\r\n"; FileoutputUtil.packetLog("log\\外挂检测\\" +
                     * chrhardref.getName() + ".log", note);
                     */ //                    AutobanManager.getInstance().autoban(chrhardref.getClient(), StringUtil.makeEnumHumanReadable(offense.name()));
                    gm_message = 50;
                }
                break;
        }
        CheatingOffensePersister.getInstance().persistEntry(entry);
    }

    public void updateTick(int newTick) {
        if (newTick == lastTickCount) { //definitely packet spamming
/*      
             * if (tickSame >= 5) { chr.get().getClient().getSession().close();
             * //i could also add a check for less than, but i'm not too worried
             * at the moment :) } else {
             */
            tickSame++;
//	    }
        } else {
            tickSame = 0;
        }
        lastTickCount = newTick;
    }

    public final void expireEntry(final CheatingOffenseEntry coe) {
        wL.lock();
        try {
            offenses.remove(coe.getOffense());
        } finally {
            wL.unlock();
        }
    }

    public final int getPoints() {
        int ret = 0;
        CheatingOffenseEntry[] offenses_copy;
        rL.lock();
        try {
            offenses_copy = offenses.values().toArray(new CheatingOffenseEntry[offenses.size()]);
        } finally {
            rL.unlock();
        }
        for (final CheatingOffenseEntry entry : offenses_copy) {
            if (entry.isExpired()) {
                expireEntry(entry);
            } else {
                ret += entry.getPoints();
            }
        }
        return ret;
    }

    public final Map<CheatingOffense, CheatingOffenseEntry> getOffenses() {
        return Collections.unmodifiableMap(offenses);
    }

    public final String getSummary() {
        final StringBuilder ret = new StringBuilder();
        final List<CheatingOffenseEntry> offenseList = new ArrayList<CheatingOffenseEntry>();
        rL.lock();
        try {
            for (final CheatingOffenseEntry entry : offenses.values()) {
                if (!entry.isExpired()) {
                    offenseList.add(entry);
                }
            }
        } finally {
            rL.unlock();
        }
        Collections.sort(offenseList, new Comparator<CheatingOffenseEntry>() {

            @Override
            public final int compare(final CheatingOffenseEntry o1, final CheatingOffenseEntry o2) {
                final int thisVal = o1.getPoints();
                final int anotherVal = o2.getPoints();
                return (thisVal < anotherVal ? 1 : (thisVal == anotherVal ? 0 : -1));
            }
        });
        final int to = Math.min(offenseList.size(), 4);
        for (int x = 0; x < to; x++) {
            ret.append(StringUtil.makeEnumHumanReadable(offenseList.get(x).getOffense().name()));
            ret.append(": ");
            ret.append(offenseList.get(x).getCount());
            if (x != to - 1) {
                ret.append(" ");
            }
        }
        return ret.toString();
    }

    public final void dispose() {
        if (invalidationTask != null) {
            invalidationTask.cancel(false);
        }
        invalidationTask = null;
    }

    private final class InvalidationTask implements Runnable {

        @Override
        public final void run() {
            CheatingOffenseEntry[] offenses_copy;
            rL.lock();
            try {
                offenses_copy = offenses.values().toArray(new CheatingOffenseEntry[offenses.size()]);
            } finally {
                rL.unlock();
            }
            for (CheatingOffenseEntry offense : offenses_copy) {
                if (offense.isExpired()) {
                    expireEntry(offense);
                }
            }
            if (chr.get() == null) {
                dispose();
            }
        }
    }
}
