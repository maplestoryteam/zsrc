
package client;

import server.MapleStatEffect;
import server.life.Element;

public interface ISkill {

    int getId();

    MapleStatEffect getEffect(int level);

    byte getMaxLevel();

    int getAnimationTime();

    boolean canBeLearnedBy(int job);

    boolean isFourthJob();

    boolean getAction();

    boolean isTimeLimited();

    int getMasterLevel();

    Element getElement();

    boolean isBeginnerSkill();

    boolean hasRequiredSkill();

    boolean isInvisible();

    boolean isChargeSkill();

    int getRequiredSkillLevel();

    int getRequiredSkillId();

    String getName();
}
