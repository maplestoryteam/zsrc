/*
ÕÊº“’ŸªΩ ﬁ




 */
package handling.channel.handler;

import client.*;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import server.MapleStatEffect;
import server.life.MapleMonster;
import server.life.SummonAttackEntry;
import server.maps.*;
import server.movement.LifeMovementFragment;
import tools.MaplePacketCreator;
import tools.data.LittleEndianAccessor;
import tools.packet.MobPacket;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static fumo.FumoSkill.FM;

public class SummonHandler {

    public static final void MoveDragon(final LittleEndianAccessor slea, final MapleCharacter chr) {
        slea.skip(8); //POS
        final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 5);
        if (chr != null && chr.getDragon() != null) {
            final Point pos = chr.getDragon().getPosition();
            MovementParse.updatePosition(res, chr.getDragon(), 0);
            if (!chr.isHidden()) {
                chr.getMap().broadcastMessage(chr, MaplePacketCreator.moveDragon(chr.getDragon(), pos, res), chr.getPosition());
            }
            //∑÷…Ìœ‡πÿ
//            WeakReference<MapleCharacter>[] clones = chr.getClones();
//            for (int i = 0; i < clones.length; i++) {
//                if (clones[i].get() != null) {
//                    final MapleMap map = chr.getMap();
//                    final MapleCharacter clone = clones[i].get();
//                    final List<LifeMovementFragment> res3 = new ArrayList<LifeMovementFragment>(res);
//                    CloneTimer.getInstance().schedule(new Runnable() {
//                        public void run() {
//                            try {
//                                if (clone.getMap() == map && clone.getDragon() != null) {
//                                    final Point startPos = clone.getDragon().getPosition();
//                                    MovementParse.updatePosition(res3, clone.getDragon(), 0);
//
//                                    if (!clone.isHidden()) {
//                                        map.broadcastMessage(clone, MaplePacketCreator.moveDragon(clone.getDragon(), startPos, res3), clone.getPosition());
//                                    }
//                                }
//                            } catch (Exception e) {
//                                //very rarely swallowed
//                            }
//                        }
//                    }, 500 * i + 500);
//                }
//            }
        }
    }

    public static final void MoveSummon(final LittleEndianAccessor slea, final MapleCharacter chr) {
        final int oid = slea.readInt();
        Point startPos = new Point(slea.readShort(), slea.readShort());
        List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 4);
//        slea.skip(4); //startPOS
//        final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 4);
        if (chr == null) {
            return;
        }
        for (MapleSummon sum : chr.getSummons().values()) {
            if (sum.getObjectId() == oid && sum.getMovementType() != SummonMovementType.STATIONARY) {
                final Point pos = sum.getPosition();
                MovementParse.updatePosition(res, sum, 0);
                if (!sum.isChangedMap()) {
                    chr.getMap().broadcastMessage(chr, MaplePacketCreator.moveSummon(chr.getId(), oid, pos, res), sum.getPosition());
                }
                break;
            }
        }
    }

    public static final void DamageSummon(final LittleEndianAccessor slea, final MapleCharacter chr) {
        if (chr == null || !chr.isAlive() || chr.getMap() == null) {
            return;
        }
        //     int skillid = slea.readInt();
        int unkByte = slea.readByte();
        int damage = slea.readInt();
        int monsterIdFrom = slea.readInt();
        //       slea.readByte(); // stance

        final Iterator<MapleSummon> iter = chr.getSummons().values().iterator();
        MapleSummon summon;
        boolean remove = false;
        try {
            while (iter.hasNext()) {
                summon = iter.next();
                if (summon.isPuppet() && summon.getOwnerId() == chr.getId() && damage > 0) {
                    summon.addHP((short) -damage);
                    if (summon.getHP() <= 0) {
                        remove = true;
                    }
                    chr.getMap().broadcastMessage(chr, MaplePacketCreator.damageSummon(chr.getId(), summon.getSkill(), damage, unkByte, monsterIdFrom), summon.getTruePosition());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SummonHandler.DamageSummon£∫" + e);
        }
        if (remove) {
            chr.cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
        }
        /*if (SkillFactory.getSkill(skillid) != null) {
            MapleSummon summon;
            while (iter.hasNext()) {
                summon = iter.next();
                if (summon.isPuppet() && summon.getOwnerId() == chr.getId()) { //We can only have one puppet(AFAIK O.O) so this check is safe.
                    summon.addHP((short) -damage);
                    if (summon.getHP() <= 0) {
                        chr.cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
                    }
                    chr.getMap().broadcastMessage(chr, MaplePacketCreator.damageSummon(chr.getId(), skillid, damage, unkByte, monsterIdFrom), summon.getPosition());
                    break;
                }
            }
        }*/
    }

    public static void SummonAttack(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || !chr.isAlive()) {
            return;
        }
        final MapleMap map = chr.getMap();
        final MapleMapObject obj = map.getMapObject(slea.readInt(), MapleMapObjectType.SUMMON);
        if (obj == null) {
            return;
        }
        final MapleSummon summon = (MapleSummon) obj;
        if (summon.getOwnerId() != chr.getId() || summon.getSkillLevel() <= 0) {
            return;
        }
        final SummonSkillEntry sse = SkillFactory.getSummonData(summon.getSkill());
        if (sse == null) {
            return;
        }
        slea.skip(8);//1
        int tick = slea.readInt();
        chr.updateTick(tick);
        summon.CheckSummonAttackFrequency(chr, tick);
        slea.skip(8);//2
        final byte animation = slea.readByte();
        slea.skip(8);//3
        final byte numAttacked = slea.readByte();
        final List<SummonAttackEntry> allDamage = new ArrayList<SummonAttackEntry>();
        chr.getCheatTracker().checkSummonAttack();
        final ISkill summonSkill = SkillFactory.getSkill(summon.getSkill());
        final MapleStatEffect summonEffect = summonSkill.getEffect(summon.getSkillLevel());
        for (int i = 0; i < numAttacked; i++) {
            final MapleMonster mob = map.getMonsterByOid(slea.readInt());

            if (mob == null) {
                continue;
            }
            slea.skip(14); // who knows
            int damage = slea.readInt();
            if (summonSkill.getId() == 3221005 || summonSkill.getId() == 3121006 || summonSkill.getId() == 5221003
                    || summonSkill.getId() == 5211002 || summonSkill.getId() == 5211001) {
                damage = (int) (damage * 0.1);
            }
            if (damage > 10 * 10000) {
                damage = 10 * 10000;
            }
            if (chr.F().get(FM("—∏Ω›ÕªœÆ")) != null) {
                damage += chr.F().get(FM("—∏Ω›ÕªœÆ"));
            }
            if (chr.F().get(FM("—µ¡∑”–∑Ω")) != null && !mob.getStats().isBoss()) {
                damage += damage / 100 * chr.F().get(FM("—µ¡∑”–∑Ω"));
            }
            if (chr.F().get(FM("—µ¡∑”–Àÿ")) != null && mob.getStats().isBoss()) {
                damage += damage / 100 * chr.F().get(FM("—µ¡∑”–Àÿ"));
            }
            if (chr.F().get(FM("–ƒ”–¡Èœ¨")) != null) {
                damage += damage / 100 * chr.F().get(FM("–ƒ”–¡Èœ¨"));
            }
            //final int damage = 131452000;
            allDamage.add(new SummonAttackEntry(mob, damage));
            //System.out.println(damage);
        }
        if (!summon.isChangedMap()) {
            //    map.broadcastMessage(chr, MaplePacketCreator.summonAttack(summon.getOwnerId(), summon.getObjectId(), animation, allDamage), summon.getPosition());
        }

        if (summonEffect == null) {
            return;
        }
        for (SummonAttackEntry attackEntry : allDamage) {
            //final int toDamage = attackEntry.getDamage();
            int toDamage = attackEntry.getDamage();//
            final MapleMonster mob = attackEntry.getMonster();
            if (toDamage > 0 && summonEffect.getMonsterStati().size() > 0) {
                if (summonEffect.makeChanceResult()) {
                    for (Map.Entry<MonsterStatus, Integer> z : summonEffect.getMonsterStati().entrySet()) {
                        mob.applyStatus(chr, new MonsterStatusEffect(z.getKey(), z.getValue(), summonSkill.getId(), null, false), summonEffect.isPoison(), 4000, false);
                    }
                }
            }
            if (chr.isGM() || toDamage < 120000) {
                mob.damage(chr, toDamage, true);
                chr.checkMonsterAggro(mob);
                if (!mob.isAlive()) {
                    chr.getClient().sendPacket(MobPacket.killMonster(mob.getObjectId(), 1));
                }
            } else {
                //    AutobanManager.getInstance().autoban(c, "High Summon Damage (" + toDamage + " to " + attackEntry.getMonster().getId() + ")");
                // TODO : Check player's stat for damage checking.
            }
            //System.out.println(" " + toDamage);
        }
        if (summon.isGaviota()) {
            chr.getMap().broadcastMessage(MaplePacketCreator.removeSummon(summon, true));
            chr.getMap().removeMapObject(summon);
            chr.removeVisibleMapObject(summon);
            chr.cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
            chr.cancelEffectFromBuffStat(MapleBuffStat.REAPER);
            //TODO: Multi Summoning, must do something about hack buffstat
        }
    }
}
