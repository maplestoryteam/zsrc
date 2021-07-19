
package handling.channel.handler;

import client.ISkill;
import client.MapleCharacter;
import client.SkillFactory;
import constants.GameConstants;
import server.AutobanManager;
import server.MapleStatEffect;
import tools.AttackPair;

import java.awt.*;
import java.util.List;

public class AttackInfo {

    public int skill, charge, lastAttackTickCount;
    public List<AttackPair> allDamage;
    public Point position;
    public byte hits, targets, tbyte, display, animation, speed, csstar, AOE, slot, unk;
    public boolean real = true;
    public boolean isCloseRangeAttack = false;//pvp
    public final MapleStatEffect getAttackEffect(final MapleCharacter chr, int skillLevel, final ISkill skill_) {
        if (GameConstants.isMulungSkill(skill) || GameConstants.isPyramidSkill(skill)) {
            skillLevel = 1;
        } else if (skillLevel <= 0) {
            return null;
        }
        if (GameConstants.isLinkedAranSkill(skill)) {
            final ISkill skillLink = SkillFactory.getSkill(skill);
            if (display > 80) {
                if (!skillLink.getAction()) {
                    AutobanManager.getInstance().autoban(chr.getClient(), "No delay hack, SkillID : " + skill); // 2 of the same autobans? wtf...
                    return null;
                }
            }
            return skillLink.getEffect(skillLevel);
        }
        if (display > 80) {
            if (!skill_.getAction()) {
                AutobanManager.getInstance().autoban(chr.getClient(), "No delay hack, SkillID : " + skill);
                return null;
            }
        }
        return skill_.getEffect(skillLevel);
    }
}
