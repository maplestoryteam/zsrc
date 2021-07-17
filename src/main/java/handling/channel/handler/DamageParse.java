/*
角色攻击文件
 */
package handling.channel.handler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import client.ISkill;
import constants.GameConstants;
import client.inventory.IItem;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.inventory.MapleInventoryType;
import client.PlayerStats;
import client.SkillFactory;
import client.anticheat.CheatingOffense;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import static fumo.FumoSkill.FM;
import static gui.QQMsgServer.sendMsgToQQGroup;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.World;
import java.util.Map;
import server.MapleStatEffect;
import server.Randomizer;
import server.life.Element;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import pvp.MaplePvp;
import static scripting.NPCConversationManager.角色ID取名字;
import tools.MaplePacketCreator;
import tools.AttackPair;
import tools.Pair;
import tools.data.LittleEndianAccessor;

public class DamageParse {

    private final static int[] charges = {1211005, 1211006};

    //物理攻击
    public static void applyAttack(final AttackInfo attack, final ISkill theSkill, final MapleCharacter player, int attackCount, final double maxDamagePerMonster, final MapleStatEffect effect, final AttackType attack_type) {
        int totDamage = 0;
        final MapleMap map = player.getMap();
        int 全服决斗开关 = gui.Start.ConfigValuesMap.get("全服决斗开关");
        if (全服决斗开关 == 0) {
            MaplePvp.doPvP(player, map, attack);
        } else if (MapleParty.互相伤害 == 1) {
            if (player.getMapId() == 104000400 && player.getClient().getChannel() == 2) {
                MaplePvp.doPvP(player, map, attack);
            }
        }
        attackCount += 1;
        /**
         * <金钱炸弹>
         */
        if (attack.skill == 4211006) {
            for (AttackPair oned : attack.allDamage) {
                if (oned.attack != null) {
                    continue;
                }
                final MapleMapObject mapobject = map.getMapObject(oned.objectid, MapleMapObjectType.ITEM);
                if (mapobject != null) {
                    final MapleMapItem mapitem = (MapleMapItem) mapobject;
                    mapitem.getLock().lock();
                    try {
                        if (mapitem.getMeso() > 0) {
                            if (mapitem.isPickedUp()) {
                                return;
                            }
                            map.removeMapObject(mapitem);
                            map.broadcastMessage(MaplePacketCreator.explodeDrop(mapitem.getObjectId()));
                            mapitem.setPickedUp(true);
                        } else {
                            player.getCheatTracker().registerOffense(CheatingOffense.其他异常);
                            return;
                        }
                    } finally {
                        mapitem.getLock().unlock();
                    }
                } else {
                    player.getCheatTracker().registerOffense(CheatingOffense.金钱炸弹_不存在道具);
                    return;
                }
            }
        }
        int fixeddmg, totDamageToOneMonster = 0;
        long hpMob = 0;
        final PlayerStats stats = player.getStat();
        int CriticalDamage = stats.passive_sharpeye_percent();
        byte ShdowPartnerAttackPercentage = 0;
        if (attack_type == AttackType.RANGED_WITH_SHADOWPARTNER || attack_type == AttackType.NON_RANGED_WITH_MIRROR) {
            final MapleStatEffect shadowPartnerEffect;
            if (attack_type == AttackType.NON_RANGED_WITH_MIRROR) {
                shadowPartnerEffect = player.getStatForBuff(MapleBuffStat.MIRROR_IMAGE);
            } else {
                shadowPartnerEffect = player.getStatForBuff(MapleBuffStat.SHADOWPARTNER);
            }
            if (shadowPartnerEffect != null) {
                if (attack.skill != 0 && attack_type != AttackType.NON_RANGED_WITH_MIRROR) {
                    ShdowPartnerAttackPercentage = (byte) shadowPartnerEffect.getY();
                } else {
                    ShdowPartnerAttackPercentage = (byte) shadowPartnerEffect.getX();
                }
            }
            attackCount /= 2;
        }
        byte overallAttackCount;
        double maxDamagePerHit = 0;
        MapleMonster monster;
        MapleMonsterStats monsterstats;
        boolean Tempest;
        if (gui.Start.ConfigValuesMap.get("加速检测开关") != null) {
            if (gui.Start.ConfigValuesMap.get("加速检测开关") == 0) {
                if (attack.skill == 3121004 || attack.skill == 5221004) {
                    if (System.currentTimeMillis() - player.攻击加速 < 50) {
                        player.攻击加速判断++;
                        if (player.攻击加速判断 >= 10) {
                            int ch = World.Find.findChannel(角色ID取名字(player.getId()));
                            MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
                            if (target.ban("被攻速检测封号", player.isAdmin(), false, false)) {
                                String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，无延迟加速攻击，破坏游戏平衡，被系统永久封号。";
                                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                                sendMsgToQQGroup(信息);
                            }
                        }
                    }
                } else if (System.currentTimeMillis() - player.攻击加速 < 400) {
                    player.攻击加速判断++;
                    if (player.攻击加速判断 >= 10) {
                        int ch = World.Find.findChannel(角色ID取名字(player.getId()));
                        MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
                        if (target.ban("被攻速检测封号", player.isAdmin(), false, false)) {
                            String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，无延迟加速攻击，破坏游戏平衡，被系统永久封号。";
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                            sendMsgToQQGroup(信息);
                        }
                    }
                } else if (player.攻击加速判断 > 0) {
                    player.攻击加速判断 = 0;
                }
                player.攻击加速 = System.currentTimeMillis();
            }
        }
        for (final AttackPair oned : attack.allDamage) {
            monster = map.getMonsterByOid(oned.objectid);
            if (monster != null) {
                totDamageToOneMonster = 0;
                hpMob = monster.getHp();
                monsterstats = monster.getStats();
                fixeddmg = monsterstats.getFixedDamage();
                Tempest = monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006;
                maxDamagePerHit = CalculateMaxWeaponDamagePerHit(player, monster, attack, theSkill, effect, maxDamagePerMonster, CriticalDamage);
                overallAttackCount = 0;
                if (gui.Start.ConfigValuesMap.get("全屏检测开关") != null) {
                    if (gui.Start.ConfigValuesMap.get("全屏检测开关") == 0) {
                        全屏检测(player, monster, attack);
                    }
                }
                if (gui.Start.ConfigValuesMap.get("段数检测开关") != null) {
                    if (gui.Start.ConfigValuesMap.get("段数检测开关") == 0) {
                        技能攻击段数检测(player, effect, attack, attackCount);
                    }
                }
                if (gui.Start.ConfigValuesMap.get("群攻检测开关") != null) {
                    if (gui.Start.ConfigValuesMap.get("群攻检测开关") == 0) {
                        群攻数量检测(player, effect, attack);
                    }
                }
                long eachd;
                StringBuilder name = new StringBuilder();
                for (Pair<Integer, Boolean> eachde : oned.attack) {
                    eachd = eachde.left;
                    overallAttackCount++;
                    long 增加伤害 = 0;
                    //影子搭档击中了吗？让我们分开一次。
                    if (overallAttackCount - 1 == attackCount) {
                        maxDamagePerHit = (maxDamagePerHit / 100) * ShdowPartnerAttackPercentage;
                    }
                    /**
                     * <战士>
                     */
                    if (player.job >= 100 && player.job < 200) {
                        if (player.job == 110 || player.job == 111 || player.job == 112) {
                            if (player.F().get(FM("斗气集中")) != null) {
                                if (player.isActiveBuffedValue(1101006)) {
                                    eachd += eachd / 100 * player.F().get(FM("斗气集中"));
                                }
                            }
                        } else if (player.job == 120 || player.job == 120 || player.job == 122) {
                            if (player.F().get(FM("龙之献祭")) != null) {
                                if (player.getMaxHp() <= 70) {
                                    eachd += eachd * 1.3;
                                } else if (player.getMaxHp() <= 50) {
                                    eachd += eachd * 2.5;
                                } else if (player.getMaxHp() <= 30) {
                                    eachd += eachd * 3.7;
                                }
                                if (attack.skill == 1211002) {
                                    if (player.龙之献祭攻击次数 < 5) {
                                        player.龙之献祭攻击次数++;
                                    } else if (player.龙之献祭攻击次数 >= 5) {
                                        if (attack.skill == 1311006) {
                                            eachd += eachd / 100 * player.F().get(FM("龙之献祭"));
                                            player.龙之献祭攻击次数 = 0;
                                        }
                                    }
                                }
                            }
                        } else if (player.job == 120 || player.job == 120 || player.job == 122) {
                            if (player.F().get(FM("属性攻击")) != null) {
                                if (attack.skill == 1311005) {
                                    //烈焰状态，增加基础伤害
                                    if (player.isActiveBuffedValue(1211003) || player.isActiveBuffedValue(1211004)) {
                                        eachd += eachd / 100 * player.F().get(FM("属性攻击"));
                                    }
                                    //寒冰状态，增加吸血能力
                                    if (player.isActiveBuffedValue(1211005) || player.isActiveBuffedValue(1211006)) {
                                        player.addHP(player.F().get(FM("属性攻击")) * 10);
                                    }
                                    //雷鸣状态，增加秒杀
                                    if (player.isActiveBuffedValue(1211007) || player.isActiveBuffedValue(1211008)) {
                                        double r1 = Math.ceil(Math.random() * 3000);
                                        if (player.F().get(FM("属性攻击")) > r1) {
                                            if (player.getEquippedFuMoMap().get(7) != null) {
                                                maxDamagePerHit += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                                eachd += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                            } else {
                                                maxDamagePerHit += 10000 * 1000;
                                                eachd += 10000 * 1000;
                                            }
                                            player.dropMessage(5, "对普通怪物造成一击必杀");
                                        }
                                    }
                                    //神圣状态，累计伤害
                                    if (player.isActiveBuffedValue(1221003) || player.isActiveBuffedValue(1221004)) {
                                        if (player.属性攻击累计伤害次数 < 3) {
                                            player.属性攻击累计伤害 += eachd;
                                            player.属性攻击累计伤害次数++;
                                        }
                                        eachd += player.属性攻击累计伤害;
                                    }
                                }
                            }
                        }
                    } else if (player.job >= 300 && player.job < 400) {
                        if (player.job == 310 || player.job == 311 || player.job == 312) {
                            if (player.F().get(FM("箭扫射1")) != null) {
                                if (attack.skill == 3111006) {
                                    eachd += eachd / 100 * player.F().get(FM("箭扫射1"));
                                }
                            }
                        } else if (player.job == 320 || player.job == 321 || player.job == 322) {
                            if (player.F().get(FM("箭扫射2")) != null) {
                                if (attack.skill == 3211006) {
                                    eachd += eachd / 100 * player.F().get(FM("箭扫射2"));
                                }
                            }
                        }
                    } else if (player.job >= 400 && player.job < 500) {
                        if (player.job == 410 || player.job == 411 || player.job == 412) {
                            if (player.F().get(FM("落叶斩")) != null) {
                                if (attack.skill == 4211002) {
                                    eachd += player.枫叶数量 / 100 * player.F().get(FM("落叶斩"));
                                }
                            }
                        } else if (player.job == 420 || player.job == 421 || player.job == 422) {
                            if (player.F().get(FM("多重飞镖")) != null) {
                                if (attack.skill == 4111005) {
                                    eachd += player.飞镖数量 / 100 * player.F().get(FM("暗器伤人"));
                                }
                            }
                        }
                    } else if (player.job >= 500 && player.job < 600) {
                        if (player.job == 510 || player.job == 511 || player.job == 512) {
                            if (player.F().get(FM("超人变身")) != null) {
                                if (player.isActiveBuffedValue(5111005)) {
                                    eachd += eachd / 100 * player.F().get(FM("超人变身"));
                                }
                            }
                        } else if (player.job == 510 || player.job == 511 || player.job == 512) {
                            if (player.F().get(FM("双枪喷射")) != null) {
                                if (attack.skill == 5211004 || attack.skill == 5211005) {
                                    eachd += eachd / 100 * player.F().get(FM("双枪喷射"));
                                }
                            }
                        }

                    }
                    //增加对普通怪物的伤害
                    if (player.F().get(FM("强攻")) != null && !monster.getStats().isBoss()) {
                        增加伤害 += eachd / 100 * player.F().get(FM("强攻"));
                    }
                    if (player.F().get(FM("超强攻")) != null && monster.getStats().isBoss()) {
                        增加伤害 += eachd / 100 * player.F().get(FM("超强攻"));
                    }
                    if (player.F().get(FM("战争意志")) != null) {
                        增加伤害 += eachd / 100 * player.F().get(FM("战争意志"));
                    }
                    if (player.getEquippedFuMoMap().get(10) != null) {
                        增加伤害 += player.getEquippedFuMoMap().get(10);
                    }
                    if (player.F().get(FM("心有灵犀")) != null) {
                        增加伤害 += eachd / 100 * player.F().get(FM("心有灵犀"));
                    }
                    /**
                     * <仙人模式-物理>
                     */
                    if (gui.Start.个人信息设置.get("仙人模式" + player.getId() + "") != null) {
                        if (gui.Start.个人信息设置.get("仙人模式" + player.getId() + "") == 0) {
                            if (monster.getStats().isBoss()) {
                                int 契合 = (int) (gui.Start.个人信息设置.get("BUFF增益" + player.getId() + "") * 0.0001);
                                int 物理攻击力 = gui.Start.个人信息设置.get("物理攻击力" + player.getId() + "");
                                int 魔法攻击力 = gui.Start.个人信息设置.get("魔法攻击力" + player.getId() + "");
                                增加伤害 += eachd * (0.3 + (物理攻击力 + 魔法攻击力) * 契合);
                            }
                            double 仙人模式暴击 = Math.ceil(Math.random() * 100);
                            int 契合 = gui.Start.个人信息设置.get("BUFF增益" + player.getId() + "");
                            int 仙人模式攻击力 = (int) (gui.Start.个人信息设置.get("物理攻击力" + player.getId() + "") * 契合 * 0.001);
                            if (仙人模式暴击 <= 30) {
                                eachd += 仙人模式攻击力;
                            } else {
                                int 仙人模式暴击伤害加成 = gui.Start.个人信息设置.get("物理狂暴力" + player.getId() + "");
                                eachd += 仙人模式攻击力 * 2 + 仙人模式暴击伤害加成;
                            }
                            player.addHP((int) (gui.Start.个人信息设置.get("物理吸收力" + player.getId() + "") * 契合 * 0.002));
                            player.addMP((int) (gui.Start.个人信息设置.get("魔法吸收力" + player.getId() + "") * 契合 * 0.002));
                        }
                    }
                    if (player.job == 510 || player.job == 511 || player.job == 512) {
                        if (player.F().get(FM("双枪喷射")) != null) {
                            if (attack.skill == 5211004 || attack.skill == 5211005) {
                                增加伤害 += eachd / 100 * player.F().get(FM("双枪喷射"));
                            }
                        }
                    }
                    if (增加伤害 > 0) {
                        eachd += 增加伤害;
                    }
                    if (gui.Start.ConfigValuesMap.get("最高伤害") != null) {
                        if (eachd > gui.Start.ConfigValuesMap.get("最高伤害") * 2000000) {
                            int ch = World.Find.findChannel(角色ID取名字(player.getId()));
                            MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
                            if (target.ban("被高伤检测封号" + eachd, player.isAdmin(), false, false)) {
                                String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，破坏游戏平衡，被系统永久封号。";
                                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                                sendMsgToQQGroup(信息);
                            }
                        }
                    }
                    //这里记录玩家打出的最高伤害
                    if (eachd > player.最高伤害) {
                        player.最高伤害 = eachd;
                    }
                    //对普通怪物一击必杀
                    if (player.getEquippedFuMoMap().get(4) != null) {
                        if (!monster.getStats().isBoss()) {
                            double r1 = Math.ceil(Math.random() * 500);
                            if (player.getEquippedFuMoMap().get(4) > r1) {
                                if (player.getEquippedFuMoMap().get(7) != null) {
                                    maxDamagePerHit += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                    eachd += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                } else {
                                    maxDamagePerHit += 10000 * 1000;
                                    eachd += 10000 * 1000;
                                }
                                player.dropMessage(5, "对普通怪物造成一击必杀");
                            }
                        }
                    }
                    //对高级怪物一击必杀
                    if (player.getEquippedFuMoMap().get(5) != null) {
                        if (monster.getStats().isBoss()) {
                            double r2 = Math.ceil(Math.random() * 2000);
                            if (player.getEquippedFuMoMap().get(5) > r2) {
                                if (player.getEquippedFuMoMap().get(7) != null) {
                                    maxDamagePerHit += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                    eachd += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                } else {
                                    maxDamagePerHit += 10000 * 1000;
                                    eachd += 10000 * 1000;
                                }
                                player.dropMessage(5, "对高级怪物造成一击必杀");
                            }
                        }
                    }
                    //对所有怪物一击必杀
                    if (player.getEquippedFuMoMap().get(6) != null) {
                        double r3 = Math.ceil(Math.random() * 4000);
                        if (player.getEquippedFuMoMap().get(6) > r3) {
                            if (player.getEquippedFuMoMap().get(7) != null) {
                                maxDamagePerHit += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                eachd += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                            } else {
                                maxDamagePerHit += 10000 * 1000;
                                eachd += 10000 * 1000;
                            }
                            player.dropMessage(5, "对怪物造成一击必杀");
                        }
                    }
                    if (fixeddmg != -1) {
                        if (monsterstats.getOnlyNoramlAttack()) {
                            eachd = attack.skill != 0 ? 0 : fixeddmg;
                        } else {
                            eachd = fixeddmg;
                        }
                    } else if (monsterstats.getOnlyNoramlAttack()) {
                        eachd = attack.skill != 0 ? 0 : Math.min(eachd, (int) maxDamagePerHit);  // Convert to server calculated damage
                    }
                    totDamageToOneMonster += eachd;
                    //黑暗勇猛石巨人
                    if (monster.getId() == 9300021 && player.getPyramidSubway() != null) { //miss
                        player.getPyramidSubway().onMiss(player);
                    }
                    if (attack.skill == 14101006) {
                        stats.setHp(stats.getHp() + (int) (eachd * 0.04));
                    }
                }
                totDamage += totDamageToOneMonster;
                //给怪物仇恨
                if (player.F().get(FM("蒙蔽")) != null) {
                    double a = Math.ceil(Math.random() * 200);
                    if (a >= player.F().get(FM("蒙蔽"))) {
                        player.checkMonsterAggro(monster);
                    }
                } else {
                    player.checkMonsterAggro(monster);
                }
                if (attack.skill == 2301002 && !monsterstats.getUndead()) {
                    player.ban("修改WZ", true, true, false);
                    //player.天谴降临();
                    return;
                }
                // 敛财术
                if (player.getBuffedValue(MapleBuffStat.PICKPOCKET) != null) {
                    switch (attack.skill) {
                        case 0:
                        case 4001334:
                        case 4201005:
                        case 4211002:
                        case 4211004:
                        case 4221003:
                        case 4221007:
                            敛财术(player, monster, oned);
                            break;
                    }
                }
                if (player.F().get(FM("致命打击")) != null) {
                    double a = Math.ceil(Math.random() * 1000);
                    if (a <= player.F().get(FM("致命打击"))) {
                        monster.damage(player, (monster.getStats().isBoss() ? 2000000 : (monster.getHp() - 1)), true, attack.skill);
                    }
                }

                if (totDamageToOneMonster > 0) {
                    //圣域，用大锤子敲地,攻击15只以下的敌人.受到攻击的敌人体力只剩1.
                    if (attack.skill != 1221011) {
                        monster.damage(player, totDamageToOneMonster, true, attack.skill);
                    } else {
                        monster.damage(player, (monster.getStats().isBoss() ? 2000000 : (monster.getHp() - 1)), true, attack.skill);
                    }
                    if (monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)) {
                        player.addHP(-(7000 + Randomizer.nextInt(8000)));
                    }
                    //连环吸血
                    if (player.getBuffedValue(MapleBuffStat.COMBO_DRAIN) != null) {
                        stats.setHp((stats.getHp() + ((int) Math.min(monster.getMobMaxHp(), Math.min(((int) ((double) totDamage * (double) player.getStatForBuff(MapleBuffStat.COMBO_DRAIN).getX() / 100.0)), stats.getMaxHp() / 2)))), true);
                    }
                    switch (attack.skill) {
                        //生命吸收
                        case 4101005:
                        //能量耗转
                        case 5111004: {
                            stats.setHp((stats.getHp() + ((int) Math.min(monster.getMobMaxHp(), Math.min(((int) ((double) totDamage * (double) theSkill.getEffect(player.getSkillLevel(theSkill)).getX() / 100.0)), stats.getMaxHp() / 2)))), true);
                            break;
                        }
                        //导航
                        case 5211006:
                        //导航辅助
                        case 5220011: {
                            player.setLinkMid(monster.getObjectId());
                            break;
                        }
                        //龙之献祭
                        case 1311005: {
                            final int remainingHP = stats.getHp() - totDamage * effect.getX() / 100;
                            stats.setHp(remainingHP < 1 ? 1 : remainingHP);
                            break;
                        }
                        //一出双击
                        case 4221007:
                        //暗杀
                        case 4221001:
                        //落叶斩
                        case 4211002:
                        //回旋斩
                        case 4201005:
                        //诅咒术
                        case 4001002:
                        //二连击
                        case 4001334:
                        //三连环光击破
                        case 4121007:
                        //多重飞镖
                        case 4111005:
                        //双飞斩
                        case 4001344: {
                            //武器用毒液
                            final ISkill skill = SkillFactory.getSkill(4120005);
                            //武器用毒液
                            final ISkill skill2 = SkillFactory.getSkill(4220005);
                            if (player.getSkillLevel(skill) > 0) {
                                final MapleStatEffect venomEffect = skill.getEffect(player.getSkillLevel(skill));
                                MonsterStatusEffect monsterStatusEffect;

                                for (int i = 0; i < attackCount; i++) {
                                    if (venomEffect.makeChanceResult()) {
                                        if (monster.getVenomMulti() < 3) {
                                            monster.setVenomMulti((byte) (monster.getVenomMulti() + 1));
                                            monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.POISON, 1, 4120005, null, false);
                                            monster.applyStatus(player, monsterStatusEffect, false, venomEffect.getDuration(), true);
                                        }
                                    }
                                }
                            } else if (player.getSkillLevel(skill2) > 0) {
                                final MapleStatEffect venomEffect = skill2.getEffect(player.getSkillLevel(skill2));
                                MonsterStatusEffect monsterStatusEffect;

                                for (int i = 0; i < attackCount; i++) {
                                    if (venomEffect.makeChanceResult()) {
                                        if (monster.getVenomMulti() < 3) {
                                            monster.setVenomMulti((byte) (monster.getVenomMulti() + 1));
                                            monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.POISON, 1, 4220005, null, false);
                                            monster.applyStatus(player, monsterStatusEffect, false, venomEffect.getDuration(), true);
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        //神通术
                        case 4201004: {
                            monster.神通术(player);
                            break;
                        }
                        //抗压
                        case 21101003:
                        //双重重击
                        case 21000002:
                        //三重重击
                        case 21100001:
                        //战神突进
                        case 21100002:
                        //斗气爆裂
                        case 21100004:
                        //全力挥击
                        case 21110002:
                        //终极投掷
                        case 21110003:
                        //幻影狼牙
                        case 21110004:
                        //旋风
                        case 21110006:
                        //全力挥击- 双重重击
                        case 21110007:
                        //全力挥击 - 三重重击
                        case 21110008:
                        //战神之舞
                        case 21120002:
                        //巨熊咆哮
                        case 21120005:
                        //钻石星辰
                        case 21120006:
                        //战神之舞- 双重重击
                        case 21120009:
                        //战神之舞 - 三重重击
                        case 21120010: {
                            if (player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null && !monster.getStats().isBoss()) {
                                final MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.WK_CHARGE);
                                if (eff != null && eff.getSourceId() == 21111005) {
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.SPEED, eff.getX(), eff.getSourceId(), null, false), false, eff.getY() * 1000, false);
                                }
                            }
                            if (player.getBuffedValue(MapleBuffStat.BODY_PRESSURE) != null && !monster.getStats().isBoss()) {
                                final MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.BODY_PRESSURE);

                                if (eff != null && eff.makeChanceResult() && !monster.isBuffed(MonsterStatus.NEUTRALISE)) {
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.NEUTRALISE, 1, eff.getSourceId(), null, false), false, eff.getX() * 1000, false);
                                }
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    if (totDamageToOneMonster > 0) {
                        IItem weapon_ = player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
                        if (weapon_ != null) {
                            MonsterStatus stat = GameConstants.getStatFromWeapon(weapon_.getItemId()); //10001 = acc/darkness. 10005 = speed/slow.
                            if (stat != null && Randomizer.nextInt(100) < GameConstants.getStatChance()) {
                                final MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(stat, GameConstants.getXForStat(stat), GameConstants.getSkillForStat(stat), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, 10000, false, false);
                            }
                        }
                        if (player.getBuffedValue(MapleBuffStat.BLIND) != null) {
                            final MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.BLIND);

                            if (eff.makeChanceResult()) {
                                final MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.ACC, eff.getX(), eff.getSourceId(), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, false);
                            }

                        }
                        if (player.getBuffedValue(MapleBuffStat.HAMSTRING) != null) {
                            //击退箭
                            final ISkill skill = SkillFactory.getSkill(3121007);
                            final MapleStatEffect eff = skill.getEffect(player.getSkillLevel(skill));

                            if (eff.makeChanceResult()) {
                                final MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.SPEED, eff.getX(), 3121007, null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, false);
                            }
                        }
                        if (player.getJob() == 121) {
                            for (int charge : charges) {
                                final ISkill skill = SkillFactory.getSkill(charge);
                                if (player.isBuffFrom(MapleBuffStat.WK_CHARGE, skill)) {
                                    final MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.FREEZE, 1, charge, null, false);
                                    monster.applyStatus(player, monsterStatusEffect, false, skill.getEffect(player.getSkillLevel(skill)).getY() * 2000, false);
                                    break;
                                }
                            }
                        }
                    }
                    if (effect != null && effect.getMonsterStati().size() > 0) {
                        if (effect.makeChanceResult()) {
                            for (Map.Entry<MonsterStatus, Integer> z : effect.getMonsterStati().entrySet()) {
                                monster.applyStatus(player, new MonsterStatusEffect(z.getKey(), z.getValue(), theSkill.getId(), null, false), effect.isPoison(), effect.getDuration(), false);
                            }
                        }
                    }
                }
            }
        }
        if (attack.skill != 0 && attack.targets > 0 && attack.skill != 21101003 && attack.skill != 5110001 && attack.skill != 15100004 && attack.skill != 11101002 && attack.skill != 13101002) {
            effect.applyTo(player, attack.position);
        }
    }
    //魔法攻击

    public static final void applyAttackMagic(final AttackInfo attack, final ISkill theSkill, final MapleCharacter player, final MapleStatEffect effect) {
        if (effect == null) {
            player.getClient().sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        final PlayerStats stats = player.getStat();
        double maxDamagePerHit;
        if (attack.skill == 1000 || attack.skill == 10001000 || attack.skill == 20001000 || attack.skill == 20011000 || attack.skill == 30001000) {
            maxDamagePerHit = 40;
        } else if (GameConstants.isPyramidSkill(attack.skill)) {
            maxDamagePerHit = 1;
        } else {
            final double v75 = (effect.getMatk() * 0.058);
            maxDamagePerHit = stats.getTotalMagic() * (stats.getInt() * 0.5 + (v75 * v75) + effect.getMatk() * 3.3) / 100;
        }
        // 避免任何错误
        maxDamagePerHit *= 1.04;
        final Element element = player.getBuffedValue(MapleBuffStat.ELEMENT_RESET) != null ? Element.NEUTRAL : theSkill.getElement();

        double MaxDamagePerHit = 0;
        int totDamageToOneMonster, totDamage = 0, fixeddmg;
        byte overallAttackCount;
        boolean Tempest;
        MapleMonsterStats monsterstats;
        int CriticalDamage = stats.passive_sharpeye_percent();
        final ISkill eaterSkill = SkillFactory.getSkill(GameConstants.getMPEaterForJob(player.getJob()));
        final int eaterLevel = player.getSkillLevel(eaterSkill);
        final MapleMap map = player.getMap();
        int 全服决斗开关 = gui.Start.ConfigValuesMap.get("全服决斗开关");
        if (全服决斗开关 == 0) {
            MaplePvp.doPvP(player, map, attack);
        } else if (MapleParty.互相伤害 == 1) {
            if (player.getMapId() == 104000400 && player.getClient().getChannel() == 2) {
                MaplePvp.doPvP(player, map, attack);
            }
        }
        int last = effect.getAttackCount() > effect.getBulletCount() ? effect.getAttackCount() : effect.getBulletCount();
        if (gui.Start.ConfigValuesMap.get("加速检测开关") != null) {
            if (gui.Start.ConfigValuesMap.get("加速检测开关") == 0) {
                if (System.currentTimeMillis() - player.攻击加速 < 400) {
                    player.攻击加速判断++;
                    if (player.攻击加速判断 >= 10) {
                        int ch = World.Find.findChannel(角色ID取名字(player.getId()));
                        MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
                        if (target.ban("被攻速检测封号", player.isAdmin(), false, false)) {
                            String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，无延迟加速攻击，破坏游戏平衡，被系统永久封号。";
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                            sendMsgToQQGroup(信息);
                        }
                    }
                } else if (player.攻击加速判断 > 0) {
                    player.攻击加速判断 = 0;
                }
                player.攻击加速 = System.currentTimeMillis();
            }
        }
        for (final AttackPair oned : attack.allDamage) {
            final MapleMonster monster = map.getMonsterByOid(oned.objectid);
            if (monster != null) {
                Tempest = monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006 && !monster.getStats().isBoss();
                totDamageToOneMonster = 0;
                monsterstats = monster.getStats();
                fixeddmg = monsterstats.getFixedDamage();
                MaxDamagePerHit = CalculateMaxMagicDamagePerHit(player, theSkill, monster, monsterstats, stats, element, CriticalDamage, maxDamagePerHit);
                overallAttackCount = 0;
                if (gui.Start.ConfigValuesMap.get("全屏检测开关") != null) {
                    if (gui.Start.ConfigValuesMap.get("全屏检测开关") == 0) {
                        全屏检测(player, monster, attack);
                    }
                }
                if (gui.Start.ConfigValuesMap.get("段数检测开关") != null) {
                    if (gui.Start.ConfigValuesMap.get("段数检测开关") == 0) {
                        技能攻击段数检测(player, effect, attack, last);
                    }
                }
                if (gui.Start.ConfigValuesMap.get("群攻检测开关") != null) {
                    if (gui.Start.ConfigValuesMap.get("群攻检测开关") == 0) {
                        群攻数量检测(player, effect, attack);
                    }
                }
                long eachd;
                StringBuilder name = new StringBuilder();
                long 增加伤害 = 0;
                for (Pair<Integer, Boolean> eachde : oned.attack) {
                    eachd = eachde.left;
                    overallAttackCount++;
                    /**
                     * <魔法师>
                     */
                    if (player.job >= 200 && player.job < 300) {
                        /**
                         * <火毒>
                         */
                        if (player.job == 210 || player.job == 211 || player.job == 212) {
                            if (player.F().get(FM("末日烈焰")) != null) {
                                if (attack.skill == 2111002) {
                                    eachd += eachd / 100 * player.F().get(FM("末日烈焰"));
                                    player.末日烈焰伤害记录 = eachd;
                                    eachd += player.末日烈焰伤害记录;
                                }
                            }
                        }
                        /**
                         * <冰雷>
                         */
                        if (player.job == 220 || player.job == 221 || player.job == 222) {
                            if (player.F().get(FM("落雷枪")) != null) {
                                if (attack.skill == 2111002) {
                                    double r1 = Math.ceil(Math.random() * 3000);
                                    if (player.F().get(FM("落雷枪")) > r1) {
                                        if (player.getEquippedFuMoMap().get(7) != null) {
                                            maxDamagePerHit += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                            eachd += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                        } else {
                                            maxDamagePerHit += 10000 * 1000;
                                            eachd += 10000 * 1000;
                                        }
                                        player.dropMessage(5, "对普通怪物造成一击必杀");
                                    } else {
                                        eachd += eachd / 100 * player.F().get(FM("落雷枪"));
                                    }
                                }
                            }
                        }
                        /**
                         * <主教>
                         */
                        if (player.job == 230 || player.job == 231 || player.job == 232) {
                            if (player.F().get(FM("圣光")) != null) {
                                if (attack.skill == 2311004) {
                                    eachd += eachd / 100 * player.F().get(FM("圣光"));
                                    if (player.圣光无敌 < 2) {
                                        player.圣光无敌++;
                                    }
                                }
                            }
                        }
                    }
                    //增加对普通怪物的伤害
                    if (player.F().get(FM("强攻")) != null && !monster.getStats().isBoss()) {
                        增加伤害 += eachd / 100 * player.F().get(FM("强攻"));
                    }
                    if (player.F().get(FM("超强攻")) != null && monster.getStats().isBoss()) {
                        增加伤害 += eachd / 100 * player.F().get(FM("超强攻"));
                    }
                    if (player.F().get(FM("战争意志")) != null) {
                        增加伤害 += eachd / 100 * player.F().get(FM("战争意志"));
                    }
                    if (player.getEquippedFuMoMap().get(10) != null) {
                        增加伤害 += player.getEquippedFuMoMap().get(10);
                    }
                    if (player.F().get(FM("心有灵犀")) != null) {
                        增加伤害 += eachd / 100 * player.F().get(FM("心有灵犀"));
                    }
                    /**
                     * <仙人模式-魔法>
                     */
                    if (gui.Start.个人信息设置.get("仙人模式" + player.getId() + "") != null) {
                        if (gui.Start.个人信息设置.get("仙人模式" + player.getId() + "") == 0) {
                            if (monster.getStats().isBoss()) {
                                int 契合 = (int) (gui.Start.个人信息设置.get("BUFF增益" + player.getId() + "") * 0.00001);
                                int 物理攻击力 = gui.Start.个人信息设置.get("物理攻击力" + player.getId() + "");
                                int 魔法攻击力 = gui.Start.个人信息设置.get("魔法攻击力" + player.getId() + "");
                                增加伤害 += eachd * (0.3 + (物理攻击力 + 魔法攻击力) * 契合);
                            }
                            double 仙人模式暴击 = Math.ceil(Math.random() * 100);
                            int 契合 = gui.Start.个人信息设置.get("BUFF增益" + player.getId() + "");
                            int 仙人模式攻击力 = (int) (gui.Start.个人信息设置.get("魔法攻击力" + player.getId() + "") * 契合 * 0.001);
                            if (仙人模式暴击 <= 30) {
                                eachd += 仙人模式攻击力;
                            } else {
                                int 仙人模式暴击伤害加成 = gui.Start.个人信息设置.get("魔法狂暴力" + player.getId() + "");
                                eachd += 仙人模式攻击力 * 2 + 仙人模式暴击伤害加成;
                            }
                            player.addHP((int) (gui.Start.个人信息设置.get("物理吸收力" + player.getId() + "") * 契合 * 0.002));
                            player.addMP((int) (gui.Start.个人信息设置.get("魔法吸收力" + player.getId() + "") * 契合 * 0.002));
                        }
                    }
                    if (增加伤害 > 0) {
                        eachd += 增加伤害;
                    }
                    if (gui.Start.ConfigValuesMap.get("最高伤害") != null) {
                        if (eachd > gui.Start.ConfigValuesMap.get("最高伤害") * 10000) {
                            int ch = World.Find.findChannel(角色ID取名字(player.getId()));
                            MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
                            if (target.ban("被高伤检测封号" + eachd, player.isAdmin(), false, false)) {
                                String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，破坏游戏平衡，被系统永久封号。";
                                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                                sendMsgToQQGroup(信息);
                            }
                        }
                    }
                    //这里记录玩家打出的最高伤害
                    if (eachd > player.最高伤害) {
                        player.最高伤害 = eachd;
                    }
                    //对普通怪物一击必杀
                    if (player.getEquippedFuMoMap().get(4) != null) {
                        if (!monster.getStats().isBoss()) {
                            double r1 = Math.ceil(Math.random() * 500);
                            if (player.getEquippedFuMoMap().get(4) > r1) {
                                if (player.getEquippedFuMoMap().get(7) != null) {
                                    maxDamagePerHit += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                    eachd += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                } else {
                                    maxDamagePerHit += 10000 * 1000;
                                    eachd += 10000 * 1000;
                                }
                                player.dropMessage(5, "对普通怪物造成一击必杀");
                            }
                        }
                    }
                    //对高级怪物一击必杀
                    if (player.getEquippedFuMoMap().get(5) != null) {
                        if (monster.getStats().isBoss()) {
                            double r2 = Math.ceil(Math.random() * 2000);
                            if (player.getEquippedFuMoMap().get(5) > r2) {
                                if (player.getEquippedFuMoMap().get(7) != null) {
                                    maxDamagePerHit += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                    eachd += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                } else {
                                    maxDamagePerHit += 10000 * 1000;
                                    eachd += 10000 * 1000;
                                }
                                player.dropMessage(5, "对高级怪物造成一击必杀");
                            }
                        }
                    }
                    //对所有怪物一击必杀
                    if (player.getEquippedFuMoMap().get(6) != null) {
                        double r3 = Math.ceil(Math.random() * 4000);
                        if (player.getEquippedFuMoMap().get(6) > r3) {
                            if (player.getEquippedFuMoMap().get(7) != null) {
                                maxDamagePerHit += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                                eachd += 10000 * 1000 + (100000 * player.getEquippedFuMoMap().get(7));
                            } else {
                                maxDamagePerHit += 10000 * 1000;
                                eachd += 10000 * 1000;
                            }
                            player.dropMessage(5, "对怪物造成一击必杀");
                        }
                    }

                    if (fixeddmg != -1) {
                        eachd = monsterstats.getOnlyNoramlAttack() ? 0 : fixeddmg;
                        /*Rectangle bounds = new Rectangle((int) player.getPosition().getX(), (int) player.getPosition().getY(), 1, 1);
                            MapleMist mist = new MapleMist(bounds, player);
                            player.getMap().spawnMist(mist, 30000, true);*/
                        //}
                    } else if (monsterstats.getOnlyNoramlAttack()) {
                        eachd = 0; // Magic is always not a normal attack
                    }
                    totDamageToOneMonster += eachd;
                }
                totDamage += totDamageToOneMonster;
                //给怪物仇恨
                if (player.F().get(FM("蒙蔽")) != null) {
                    double a = Math.ceil(Math.random() * 200);
                    if (a >= player.F().get(FM("蒙蔽"))) {
                        player.checkMonsterAggro(monster);
                    }
                } else {
                    player.checkMonsterAggro(monster);
                }
                //群体治愈
                if (attack.skill == 2301002 && !monsterstats.getUndead()) {
                    player.getCheatTracker().registerOffense(CheatingOffense.治愈术攻击非不死系怪物);
                    return;
                }
                if (player.F().get(FM("致命打击")) != null) {
                    double a = Math.ceil(Math.random() * 1000);
                    if (a <= player.F().get(FM("致命打击"))) {
                        monster.damage(player, (monster.getStats().isBoss() ? 2000000 : (monster.getHp() - 1)), true, attack.skill);
                    }
                }
                if (totDamageToOneMonster > 0) {
                    monster.damage(player, totDamageToOneMonster, true, attack.skill);
                    if (monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT)) { //test
                        player.addHP(-(7000 + Randomizer.nextInt(8000))); //this is what it seems to be?
                    }
                    switch (attack.skill) {
                        //冰凤球
                        case 2221003:
                            monster.setTempEffectiveness(Element.FIRE, theSkill.getEffect(player.getSkillLevel(theSkill)).getDuration());
                            break;
                        //火凤球
                        case 2121003:
                            monster.setTempEffectiveness(Element.ICE, theSkill.getEffect(player.getSkillLevel(theSkill)).getDuration());
                            break;
                    }
                    if (effect.getMonsterStati().size() > 0) {
                        if (effect.makeChanceResult()) {
                            for (Map.Entry<MonsterStatus, Integer> z : effect.getMonsterStati().entrySet()) {
                                monster.applyStatus(player, new MonsterStatusEffect(z.getKey(), z.getValue(), theSkill.getId(), null, false), effect.isPoison(), effect.getDuration(), false);
                            }
                        }
                    }
                    if (eaterLevel > 0) {
                        eaterSkill.getEffect(eaterLevel).applyPassive(player, monster);
                    }
                }
            }
        }
        //群体治愈
        if (attack.skill
                != 2301002) {
            effect.applyTo(player);
        }
        //一种检测
        /*if (totDamage > 1) {
            final CheatTracker tracker = player.getCheatTracker();
            tracker.setAttacksWithoutHit(true);
            if (tracker.getAttacksWithoutHit() > 1000) {
                tracker.registerOffense(CheatingOffense.人物无敌, Integer.toString(tracker.getAttacksWithoutHit()));
            }
        }*/
    }

    private static final double CalculateMaxMagicDamagePerHit(final MapleCharacter chr, final ISkill skill, final MapleMonster monster, final MapleMonsterStats mobstats, final PlayerStats stats, final Element elem, final Integer sharpEye, final double maxDamagePerMonster) {
        final int dLevel = Math.max(mobstats.getLevel() - chr.getLevel(), 0);
        final int Accuracy = (int) (Math.floor((stats.getTotalInt() / 10.0)) + Math.floor((stats.getTotalLuk() / 10.0)));
        final int MinAccuracy = mobstats.getEva() * (dLevel * 2 + 51) / 120;

        if (MinAccuracy > Accuracy && skill.getId() != 1000 && skill.getId() != 10001000 && skill.getId() != 20001000 && skill.getId() != 20011000 && skill.getId() != 30001000 && !GameConstants.isPyramidSkill(skill.getId())) { // miss :P or HACK :O
            return 0;
        }
        double elemMaxDamagePerMob;

        switch (monster.getEffectiveness(elem)) {
            case IMMUNE://免疫
                elemMaxDamagePerMob = 1;
                break;
            case NORMAL://正常
                elemMaxDamagePerMob = ElementalStaffAttackBonus(elem, maxDamagePerMonster, stats);
                break;
            case WEAK://虚弱
                elemMaxDamagePerMob = ElementalStaffAttackBonus(elem, maxDamagePerMonster * 1.5, stats);
                break;
            case STRONG://强化
                elemMaxDamagePerMob = ElementalStaffAttackBonus(elem, maxDamagePerMonster * 0.5, stats);
                break;
            default:
                throw new RuntimeException("未知的状态");
        }

        elemMaxDamagePerMob -= mobstats.getMagicDefense() * 0.5;

        elemMaxDamagePerMob += ((double) elemMaxDamagePerMob / 100) * sharpEye;

        elemMaxDamagePerMob += (elemMaxDamagePerMob * (mobstats.isBoss() ? stats.bossdam_r : stats.dam_r)) / 100;
        switch (skill.getId()) {
            case 1000:
            //蜗牛投掷术
            case 10001000:
            //蜗牛投掷术
            case 20001000:
                elemMaxDamagePerMob = 40;
                break;
            case 1020:
            case 10001020:
            case 20001020:
            case 20011020:
            case 30001020:
                elemMaxDamagePerMob = 1;
                break;
        }
        //群体治愈
        if (skill.getId() == 2301002) {
            elemMaxDamagePerMob *= 2;
        }
        if (elemMaxDamagePerMob > 2000000) {
            elemMaxDamagePerMob = 2000000;
        } else if (elemMaxDamagePerMob < 0) {
            elemMaxDamagePerMob = 1;
        }
        return elemMaxDamagePerMob;
    }

    private static final double ElementalStaffAttackBonus(final Element elem, double elemMaxDamagePerMob, final PlayerStats stats) {
        switch (elem) {
            case FIRE:
                return (elemMaxDamagePerMob / 100) * stats.element_fire;
            case ICE:
                return (elemMaxDamagePerMob / 100) * stats.element_ice;
            case LIGHTING:
                return (elemMaxDamagePerMob / 100) * stats.element_light;
            case POISON:
                return (elemMaxDamagePerMob / 100) * stats.element_psn;
            default:
                return (elemMaxDamagePerMob / 100) * stats.def;
        }
    }

    //附魔偷钱
    private static void 附魔偷钱(final MapleCharacter player, final MapleMonster mob, AttackPair oned) {
        int maxmeso = 50000;
        final ISkill skill = SkillFactory.getSkill(4211003);
        final MapleStatEffect s = skill.getEffect(player.getSkillLevel(skill));
        for (Pair eachde : oned.attack) {
            Integer eachd = (Integer) eachde.left;
            if (s.makeChanceResult()) {
                player.getMap().spawnMesoDrop(Math.min((int) Math.max(eachd / 20000.0D * maxmeso, 1.0D), maxmeso), new Point((int) (mob.getTruePosition().getX() + Randomizer.nextInt(100) - 50.0D), (int) mob.getTruePosition().getY()), mob, player, false, (byte) 0);
            }
        }
    }

    //侠客敛财术
    private static void 敛财术(final MapleCharacter player, final MapleMonster mob, AttackPair oned) {
        int maxmeso = player.getBuffedValue(MapleBuffStat.PICKPOCKET);
        final ISkill skill = SkillFactory.getSkill(4211003);
        final MapleStatEffect s = skill.getEffect(player.getSkillLevel(skill));
        for (Pair eachde : oned.attack) {
            Integer eachd = (Integer) eachde.left;
            if (s.makeChanceResult()) {
                player.getMap().spawnMesoDrop(Math.min((int) Math.max(eachd / 20000.0D * maxmeso, 1.0D), maxmeso), new Point((int) (mob.getTruePosition().getX() + Randomizer.nextInt(100) - 50.0D), (int) mob.getTruePosition().getY()), mob, player, false, (byte) 0);
            }
        }
    }
    //掠财术

    private static void 掠财术(final MapleCharacter player, final MapleMonster mob, AttackPair oned) {
        int maxmeso = player.getBuffedValue(MapleBuffStat.PICKPOCKET);
        final ISkill skill = SkillFactory.getSkill(4211003);
        final MapleStatEffect s = skill.getEffect(player.getSkillLevel(skill));
        for (Pair eachde : oned.attack) {
            Integer eachd = (Integer) eachde.left;
            if (s.makeChanceResult()) {
                player.getMap().spawnMesoDrop(Math.min((int) Math.max(eachd / 20000.0D * maxmeso, 1.0D), maxmeso), new Point((int) (mob.getTruePosition().getX() + Randomizer.nextInt(100) - 50.0D), (int) mob.getTruePosition().getY()), mob, player, false, (byte) 0);
            }
        }
    }

    private static double CalculateMaxWeaponDamagePerHit(final MapleCharacter player, final MapleMonster monster, final AttackInfo attack, final ISkill theSkill, final MapleStatEffect attackEffect, double maximumDamageToMonster, final Integer CriticalDamagePercent) {
        if (player.getMapId() / 1000000 == 914) { //aran
            return 2000000;
        }
        List<Element> elements = new ArrayList<Element>();
        boolean defined = false;
        if (theSkill != null) {
            elements.add(theSkill.getElement());

            switch (theSkill.getId()) {
                case 3001004:
                case 33101001:
                    defined = true; //can go past 2000000
                    break;
                case 1000:
                case 10001000:
                case 20001000:
                case 20011000:
                case 30001000:
                    maximumDamageToMonster = 40;
                    defined = true;
                    break;
                case 1020:
                case 10001020:
                case 20001020:
                case 20011020:
                case 30001020:
                    maximumDamageToMonster = 1;
                    defined = true;
                    break;
                case 4331003: //Owl Spirit
                    maximumDamageToMonster = (monster.getStats().isBoss() ? 2000000 : monster.getHp());
                    defined = true;
                    break;
                case 3221007: // Sniping
                    maximumDamageToMonster = (monster.getStats().isBoss() ? 2000000 : monster.getMobMaxHp());
                    defined = true;
                    break;
                case 1221011://Heavens Hammer
                    maximumDamageToMonster = (monster.getStats().isBoss() ? 2000000 : monster.getHp() - 1);
                    defined = true;
                    break;
                case 4211006: // Meso Explosion
                    maximumDamageToMonster = 750000;
                    defined = true;
                    break;
                case 1009: // Bamboo Trust
                case 1001: // Bamboo Trust
                case 10001009:
                case 20001009:
                case 20011009:
                case 30001009:
                    defined = true;
                    maximumDamageToMonster = (monster.getStats().isBoss() ? monster.getMobMaxHp() / 30 * 100 : monster.getMobMaxHp());
                    break;
                case 3211006: //Sniper Strafe
                    if (monster.getStatusSourceID(MonsterStatus.FREEZE) == 3211003) { //blizzard in effect
                        defined = true;
                        maximumDamageToMonster = monster.getHp();
                    }
                    break;
            }
        }
        if (player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {//属性伤害增加
            int chargeSkillId = player.getBuffSource(MapleBuffStat.WK_CHARGE);

            switch (chargeSkillId) {
                case 1211003:
                case 1211004:
                    elements.add(Element.FIRE);//火属性
                    break;
                case 1211005:
                case 1211006:
                case 21111005:
                    elements.add(Element.ICE);//冰属性
                    break;
                case 1211007:
                case 1211008:
                case 15101006:
                    elements.add(Element.LIGHTING);//光属性
                    break;
                case 1221003:
                case 1221004:
                case 11111007:
                    elements.add(Element.HOLY);//神圣属性
                    break;
                case 12101005:
                    elements.clear(); //自然力重置
                    break;

            }
        }
        /*if (player.getEquippedFuMoMap().get(1) > 0) {
            elements.add(Element.FIRE);//火属性
            player.dropMessage(5,"[附魔]火属性");
        }*/
        if (player.getBuffedValue(MapleBuffStat.LIGHTNING_CHARGE) != null) {
            elements.add(Element.LIGHTING);
        }
        double elementalMaxDamagePerMonster = maximumDamageToMonster;
        if (elements.size() > 0) {
            double elementalEffect;

            switch (attack.skill) {
                case 3211003:
                case 3111003: // inferno and blizzard
                    elementalEffect = attackEffect.getX() / 200.0;
                    break;
                default:
                    elementalEffect = 0.5;
                    break;
            }
            for (Element element : elements) {
                switch (monster.getEffectiveness(element)) {
                    case IMMUNE:
                        elementalMaxDamagePerMonster = 1;
                        break;
                    case WEAK:
                        elementalMaxDamagePerMonster *= (1.0 + elementalEffect);
                        break;
                    case STRONG:
                        elementalMaxDamagePerMonster *= (1.0 - elementalEffect);
                        break;
                    default:
                        break; //normal nothing
                }
            }
        }
        // Calculate mob def
        final short moblevel = monster.getStats().getLevel();
        final short d = moblevel > player.getLevel() ? (short) (moblevel - player.getLevel()) : 0;
        elementalMaxDamagePerMonster = elementalMaxDamagePerMonster * (1 - 0.01 * d) - monster.getStats().getPhysicalDefense() * 0.5;

        // Calculate passive bonuses + Sharp Eye
        elementalMaxDamagePerMonster += ((double) elementalMaxDamagePerMonster / 100.0) * CriticalDamagePercent;

//	if (theSkill.isChargeSkill()) {
//	    elementalMaxDamagePerMonster = (double) (90 * (System.currentTimeMillis() - player.getKeyDownSkill_Time()) / 2000 + 10) * elementalMaxDamagePerMonster * 0.01;
//	}
        if (theSkill != null && theSkill.isChargeSkill() && player.getKeyDownSkill_Time() == 0) {
            return 0;
        }
        final MapleStatEffect homing = player.getStatForBuff(MapleBuffStat.HOMING_BEACON);
        if (homing != null && player.getLinkMid() == monster.getObjectId() && homing.getSourceId() == 5220011) { //bullseye
            elementalMaxDamagePerMonster += (elementalMaxDamagePerMonster * homing.getX());
        }
        final PlayerStats stat = player.getStat();
        elementalMaxDamagePerMonster += (elementalMaxDamagePerMonster * (monster.getStats().isBoss() ? stat.bossdam_r : stat.dam_r)) / 100.0;
        if (player.getDebugMessage()) {
            player.dropMessage("[伤害计算] 属性伤害:" + (int) elementalMaxDamagePerMonster);
        }
        if (elementalMaxDamagePerMonster > 2000000) {
            if (!defined) {
                elementalMaxDamagePerMonster = 2000000;
            }
        } else if (elementalMaxDamagePerMonster < 0) {
            elementalMaxDamagePerMonster = 1;
        }
        return elementalMaxDamagePerMonster;
    }

    public static final AttackInfo DivideAttack(final AttackInfo attack, final int rate) {
        attack.real = false;
        if (rate <= 1) {
            return attack; //lol
        }
        for (AttackPair p : attack.allDamage) {
            if (p.attack != null) {
                for (Pair<Integer, Boolean> eachd : p.attack) {
                    eachd.left /= rate; //too ex.
                }
            }
        }
        return attack;
    }

    public static final AttackInfo Modify_AttackCrit(final AttackInfo attack, final MapleCharacter chr, final int type) {
        final int CriticalRate = chr.getStat().passive_sharpeye_rate();
        final boolean shadow = (type == 2 && chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null) || (type == 1 && chr.getBuffedValue(MapleBuffStat.MIRROR_IMAGE) != null);
        if (attack.skill != 4211006 && attack.skill != 3211003 && attack.skill != 4111004 && (CriticalRate > 0 || attack.skill == 4221001 || attack.skill == 3221007)) { //blizz + shadow meso + m.e no crits
            for (AttackPair p : attack.allDamage) {
                if (p.attack != null) {
                    int hit = 0;
                    final int mid_att = p.attack.size() / 2;
                    final List<Pair<Integer, Boolean>> eachd_copy = new ArrayList<Pair<Integer, Boolean>>(p.attack);
                    for (Pair<Integer, Boolean> eachd : p.attack) {
                        hit++;
                        if (!eachd.right) {
                            if (attack.skill == 4221001) { //暗杀不暴击3暴击，总是最后
                                eachd.right = (hit == 4 && Randomizer.nextInt(100) < 90);
                            } else if (attack.skill == 3221007 || eachd.left > 2000000) { //一击要害
                                eachd.right = true;
                            } else if (shadow && hit > mid_att) { //shadowpartner copies second half to first half
                                eachd.right = eachd_copy.get(hit - 1 - mid_att).right;
                            } else {
                                //rough calculation
                                eachd.right = (Randomizer.nextInt(100)/*
                                         * chr.CRand().CRand32__Random_ForMonster()
                                         * % 100
                                         */) < CriticalRate;
                            }
                            eachd_copy.get(hit - 1).right = eachd.right;
                            //System.out.println("CRITICAL RATE: " + CriticalRate + ", passive rate: " + chr.getStat().passive_sharpeye_rate() + ", critical: " + eachd.right);
                        }
                    }
                }
            }
        }
        return attack;
    }

    public static final AttackInfo parseDmgMa(final LittleEndianAccessor lea, final MapleCharacter chr) {
        //System.out.println(lea.toString());
        final AttackInfo ret = new AttackInfo();

        lea.skip(1);
        lea.skip(8);
        ret.tbyte = lea.readByte();
        //System.out.println("TBYTE: " + tbyte);
        ret.targets = (byte) ((ret.tbyte >>> 4) & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        lea.skip(8); //?
        ret.skill = lea.readInt();
        lea.skip(12); // ORDER [4] bytes on v.79, [4] bytes on v.80, [1] byte on v.82
        switch (ret.skill) {
            case 2121001: // Big Bang
            case 2221001:
            case 2321001:
            case 22121000: //breath
            case 22151001:
                ret.charge = lea.readInt();
                break;
            default:
                ret.charge = -1;
                break;
        }
        lea.skip(1);
        ret.unk = 0;
        ret.display = lea.readByte(); // Always zero?
        ret.animation = lea.readByte();
        lea.skip(1); // Weapon subclass
        ret.speed = lea.readByte(); // Confirmed
        ret.lastAttackTickCount = lea.readInt(); // Ticks
//        lea.skip(4); //0

        int oid, damage;
        List<Pair<Integer, Boolean>> allDamageNumbers;
        ret.allDamage = new ArrayList<AttackPair>();

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
            lea.skip(14); // [1] Always 6?, [3] unk, [4] Pos1, [4] Pos2, [2] seems to change randomly for some attack

            allDamageNumbers = new ArrayList<Pair<Integer, Boolean>>();

            MapleMonster monster = chr.getMap().getMonsterByOid(oid);
            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
//                if (ret.skill > 0) {
//                    damage = Damage_SkillPD(chr, damage, ret);
//                } else {
//                    damage = Damage_NoSkillPD(chr, damage);
//                }
                allDamageNumbers.add(new Pair<Integer, Boolean>(Integer.valueOf(damage), false));
            }
            lea.skip(4); // CRC of monster [Wz Editing]
            ret.allDamage.add(new AttackPair(Integer.valueOf(oid), allDamageNumbers));
        }
        ret.position = lea.readPos();

        return ret;
    }

    public static final AttackInfo parseDmgM(final LittleEndianAccessor lea, final MapleCharacter chr) {
        //System.out.println(lea.toString());
        final AttackInfo ret = new AttackInfo();

        lea.skip(1);
        lea.skip(8);
        ret.tbyte = lea.readByte();
        //System.out.println("TBYTE: " + tbyte);
        ret.targets = (byte) ((ret.tbyte >>> 4) & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        lea.skip(8);
        ret.skill = lea.readInt();
        lea.skip(12); // ORDER [4] bytes on v.79, [4] bytes on v.80, [1] byte on v.82
        switch (ret.skill) {
            case 5101004: // Corkscrew
            case 15101003: // Cygnus corkscrew
            case 5201002: // Gernard
            case 14111006: // Poison bomb
            case 4341002:
            case 4341003:
                ret.charge = lea.readInt();
                break;
            default:
                ret.charge = 0;
                break;
        }
        lea.skip(1);
        ret.unk = 0;
        ret.display = lea.readByte(); // Always zero?
        ret.animation = lea.readByte();
        lea.skip(1); // Weapon class
        ret.speed = lea.readByte(); // Confirmed
        ret.lastAttackTickCount = lea.readInt(); // Ticks
//        lea.skip(4); //0

        ret.allDamage = new ArrayList<AttackPair>();

        if (ret.skill == 4211006) { // Meso Explosion
            return parseMesoExplosion(lea, ret, chr);
        }
        int oid, damage;
        List<Pair<Integer, Boolean>> allDamageNumbers;

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
//	    System.out.println(tools.HexTool.toString(lea.read(14)));
            lea.skip(14); // [1] Always 6?, [3] unk, [4] Pos1, [4] Pos2, [2] seems to change randomly for some attack

            allDamageNumbers = new ArrayList<Pair<Integer, Boolean>>();

            MapleMonster monster = chr.getMap().getMonsterByOid(oid);
            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
//                if (ret.skill > 0) {
//                    damage = Damage_SkillPD(chr, damage, ret);
//                } else {
//                    damage = Damage_NoSkillPD(chr, damage);
//                }
                // System.out.println("Damage: " + damage);
                allDamageNumbers.add(new Pair<Integer, Boolean>(Integer.valueOf(damage), false));
            }
            lea.skip(4); // CRC of monster [Wz Editing]
            ret.allDamage.add(new AttackPair(Integer.valueOf(oid), allDamageNumbers));
        }
        ret.position = lea.readPos();
        return ret;
    }

    public static final AttackInfo parseDmgR(final LittleEndianAccessor lea, final MapleCharacter chr) {
        //System.out.println(lea.toString()); //<-- packet needs revision
        final AttackInfo ret = new AttackInfo();

        lea.skip(1);
        lea.skip(8);
        ret.tbyte = lea.readByte();
        //System.out.println("TBYTE: " + tbyte);
        ret.targets = (byte) ((ret.tbyte >>> 4) & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        lea.skip(8);
        ret.skill = lea.readInt();

        lea.skip(12); // ORDER [4] bytes on v.79, [4] bytes on v.80, [1] byte on v.82

        switch (ret.skill) {
            case 3121004: // Hurricane
            case 3221001: // Pierce
            case 5221004: // Rapidfire
            case 13111002: // Cygnus Hurricane
                lea.skip(4); // extra 4 bytes
                break;
        }
        ret.charge = -1;
        lea.skip(1);
        ret.unk = 0;
        ret.display = lea.readByte(); // Always zero?
        ret.animation = lea.readByte();
        lea.skip(1); // Weapon class
        ret.speed = lea.readByte(); // Confirmed
        ret.lastAttackTickCount = lea.readInt(); // Ticks
//        lea.skip(4); //0
        ret.slot = (byte) lea.readShort();
        ret.csstar = (byte) lea.readShort();
        ret.AOE = lea.readByte(); // is AOE or not, TT/ Avenger = 41, Showdown = 0

        int damage, oid;
        List<Pair<Integer, Boolean>> allDamageNumbers;
        ret.allDamage = new ArrayList<AttackPair>();

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
//	    System.out.println(tools.HexTool.toString(lea.read(14)));
            lea.skip(14); // [1] Always 6?, [3] unk, [4] Pos1, [4] Pos2, [2] seems to change randomly for some attack

            MapleMonster monster = chr.getMap().getMonsterByOid(oid);
            allDamageNumbers = new ArrayList<Pair<Integer, Boolean>>();
            for (int j = 0; j < ret.hits; j++) {
                damage = lea.readInt();
//                if (ret.skill > 0) {
//                    damage = Damage_SkillPD(chr, damage, ret);
//                } else {
//                    damage = Damage_NoSkillPD(chr, damage);
//                }
                allDamageNumbers.add(new Pair<Integer, Boolean>(Integer.valueOf(damage), false));
                //System.out.println("Hit " + j + " from " + i + " to mobid " + oid + ", damage " + damage);
            }
            lea.skip(4); // CRC of monster [Wz Editing]
//	    System.out.println(tools.HexTool.toString(lea.read(4)));

            ret.allDamage.add(new AttackPair(Integer.valueOf(oid), allDamageNumbers));
        }
        lea.skip(4);
        ret.position = lea.readPos();

        return ret;
    }

    public static final AttackInfo parseMesoExplosion(final LittleEndianAccessor lea, final AttackInfo ret, final MapleCharacter chr) {
        //System.out.println(lea.toString(true));
        byte bullets;
        if (ret.hits == 0) {
            lea.skip(4);
            bullets = lea.readByte();
            for (int j = 0; j < bullets; j++) {
                ret.allDamage.add(new AttackPair(Integer.valueOf(lea.readInt()), null));
                lea.skip(1);
            }
            lea.skip(2); // 8F 02
            return ret;
        }

        int oid;
        List<Pair<Integer, Boolean>> allDamageNumbers;

        for (int i = 0; i < ret.targets; i++) {
            oid = lea.readInt();
            lea.skip(12);
            bullets = lea.readByte();
            allDamageNumbers = new ArrayList<Pair<Integer, Boolean>>();
            for (int j = 0; j < bullets; j++) {
                int damage = lea.readInt();
                //    damage = Damage_SkillPD(chr, damage, ret);
                allDamageNumbers.add(new Pair<Integer, Boolean>(Integer.valueOf(damage), false)); //m.e. never crits
            }
            ret.allDamage.add(new AttackPair(Integer.valueOf(oid), allDamageNumbers));
            lea.skip(4); // C3 8F 41 94, 51 04 5B 01
        }
        lea.skip(4);
        bullets = lea.readByte();

        for (int j = 0; j < bullets; j++) {
            ret.allDamage.add(new AttackPair(Integer.valueOf(lea.readInt()), null));
            lea.skip(1);
        }
        lea.skip(2); // 8F 02/ 63 02

        return ret;
    }

    public static void 全屏检测(MapleCharacter player, MapleMonster monster, AttackInfo ret) {
        double 怪物坐标X = monster.getPosition().getX();
        double 怪物坐标Y = monster.getPosition().getY();
        double X坐标误差 = player.getPosition().getX() - 怪物坐标X;
        double Y坐标误差 = player.getPosition().getY() - 怪物坐标Y;
        if (Y坐标误差 > 3000 && X坐标误差 > 3000) {
            int ch = World.Find.findChannel(角色ID取名字(player.getId()));
            MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
            if (target.ban("被全屏检测封号", player.isAdmin(), false, false)) {
                String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，半屏/全屏攻击，破坏游戏平衡，被系统永久封号。";
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                sendMsgToQQGroup(信息);
            }
        } else if (Y坐标误差 > 1000 || X坐标误差 > 1000) {
            player.误差次数++;
            if (player.误差次数 == 10) {
                int ch = World.Find.findChannel(角色ID取名字(player.getId()));
                MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
                if (target.ban("被全屏检测封号", player.isAdmin(), false, false)) {
                    String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，半屏/全屏攻击，破坏游戏平衡，被系统永久封号。";
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                    sendMsgToQQGroup(信息);
                }
            }
        } else {
            player.误差次数 = 0;
        }
    }

    public static final void 技能攻击段数检测(MapleCharacter player, MapleStatEffect effect, AttackInfo attack, int attackCount) {
        int last = attackCount;
        /**
         * <影分身>
         */
        if (player.isActiveBuffedValue(4111002)) {
            last *= 2;
        }
        if (attack.hits > last) {
            int ch = World.Find.findChannel(角色ID取名字(player.getId()));
            MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
            if (target.ban("被段数检测封号", player.isAdmin(), false, false)) {
                String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，修改客户端导致攻击次数异常，破坏游戏平衡，被系统永久封号。";
                World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                sendMsgToQQGroup(信息);
            }
        }
    }

    public static final void 群攻数量检测(MapleCharacter player, MapleStatEffect effect, AttackInfo attack) {
        if (attack.targets > 1) {
            if (player.getJob() >= 2110 && player.getJob() <= 2112) {
                if (attack.targets > 12) {
                    int ch = World.Find.findChannel(角色ID取名字(player.getId()));
                    MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
                    if (target.ban("被攻击数量检测封号", player.isAdmin(), false, false)) {
                        String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，修改客户端导致群攻异常，破坏游戏平衡，被系统永久封号。";
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                        sendMsgToQQGroup(信息);
                    }
                }

            } else if (attack.targets > effect.getMobCount()) {
                int ch = World.Find.findChannel(角色ID取名字(player.getId()));
                MapleCharacter target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(角色ID取名字(player.getId()));
                if (target.ban("被攻击数量检测封号", player.isAdmin(), false, false)) {
                    String 信息 = "[系统提醒] : 玩家 " + target.getName() + " 因为使用非法插件，修改客户端导致群攻异常，破坏游戏平衡，被系统永久封号。";
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, 信息));
                    sendMsgToQQGroup(信息);
                }
            }
        }
    }

    public static final String Damage_AttackCount(MapleCharacter player, MapleStatEffect effect, AttackInfo attack, int attackCount) {

        String reason = "null";
        int last = attackCount;
        boolean mirror_fix = false;
        if (player.getJob() >= 411 && player.getJob() <= 412) {
            mirror_fix = true;
        }
        if (mirror_fix) {
            last *= 2;
        }
        if (attack.hits > last) {
            reason = "封包伤害次数 : " + last + " 封包伤害次数: " + attack.skill;
        }
        return reason;
    }

    public static final String Damage_HighDamage(MapleCharacter player, MapleStatEffect effect, AttackInfo attack) {
        boolean BeginnerJob = player.getJob() == 0 || player.getJob() == 1000;
        String reason = "null";

        for (final AttackPair oned : attack.allDamage) {
            if (player.getMap().getMonsterByOid(oned.objectid) != null) {
                for (Pair<Integer, Boolean> eachde : oned.attack) {
                    if (eachde.left >= 2000000000) {//200000
                        reason = "技能 " + attack.skill + " 打怪伤害 " + eachde.left;
                    }
                    if (GameConstants.Novice_Skill(attack.skill) && eachde.left > 40) {
                        reason = "技能 " + attack.skill + " 打怪伤害 " + eachde.left;
                    }
                    if (BeginnerJob) {
                        if (eachde.left > 40) {
                            reason = "技能 " + attack.skill + " 打怪伤害 " + eachde.left;
                        }
                    } else if (eachde.left >= 20000000) {
                        reason = "技能 " + attack.skill + " 打怪伤害 " + eachde.left;
                    }
                }
            }
        }
        if (GameConstants.isElseSkill(attack.skill)) {
            reason = "null";
        }
        return reason;
    }

    public static final String Damage_MobCount(MapleCharacter player, MapleStatEffect effect, AttackInfo attack) {
        String reason = "null";
        if (attack.targets > effect.getMobCount()) {

            //reason = "打怪数量过多， 封包数量: " + attack.targets + " 正确数量:" + effect.getMobCount();
        }
        return reason;
    }

}
