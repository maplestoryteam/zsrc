
package client;

import server.MapleStatEffect;
import server.life.Element;

public interface ISkill {

    int getId();

    MapleStatEffect getEffect(int level);

    byte getMaxLevel();

    int getAnimationTime();

    public boolean canBeLearnedBy(int job);

    public boolean isFourthJob();

    public boolean getAction();

    public boolean isTimeLimited();

    public int getMasterLevel();

    public Element getElement();

    public boolean isBeginnerSkill();

    public boolean hasRequiredSkill();

    public boolean isInvisible();

    public boolean isChargeSkill();

    public int getRequiredSkillLevel();

    public int getRequiredSkillId();

    public String getName();
}
