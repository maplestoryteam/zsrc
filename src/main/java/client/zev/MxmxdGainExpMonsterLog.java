package client.zev;

/**
 *
 * @author appxking
 */
public class MxmxdGainExpMonsterLog {
    public int HpTop;
    public int HpNow;
    public int MpTop;
    public int MpNow;
    public int MapId;
    public int SkillId;
    public int MobId;
    public int KilledCount;
    public int ExpAmount;
    public long Spend;
    public String Time;

    public MxmxdGainExpMonsterLog(int hpTop, int hpNow, int mpTop, int mpNow, int mapId, int skillId, int mobId, int killedCount, int expAmount, long spend, String time) {
        HpTop = hpTop;
        HpNow = hpNow;
        MpTop = mpTop;
        MpNow = mpNow;
        MapId = mapId;
        SkillId = skillId;
        MobId = mobId;
        KilledCount = killedCount;
        ExpAmount = expAmount;
        Spend = spend;
        Time = time;
    }
}
