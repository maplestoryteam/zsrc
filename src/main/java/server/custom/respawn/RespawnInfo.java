package server.custom.respawn;

import java.awt.*;

public class RespawnInfo {

    private int mapId;
    private int mobId;
    private int mobCount;
    private Point spawnPoint;
    private int channelId;
    private long hp;
    private int exp;
    private int s_day;//����
    private int s_hour;//С�r
    private int s_min;//���

    /**
     *
     * @param mapId
     * @param mobId
     * @param mobCount
     * @param spawnPoint
     * @param channelId
     * @param hp
     * @param exp
     * @param s_day
     * @param s_hour
     * @param s_min
     */
//    public RespawnInfo(int mapId, int mobId, int mobCount, Point spawnPoint, int channelId, long hp, int exp, int s_day, int s_hour, int s_min) {
//        this.mapId = mapId;
//        this.mobId = mobId;
//        this.mobCount = mobCount;
//        this.spawnPoint = spawnPoint;
//        this.channelId = channelId;
//        this.hp = hp;
//        this.exp = exp;
//        this.s_day = s_day;
//        this.s_hour = s_hour;
//        this.s_min = s_min;
//    }
    public RespawnInfo(int mapId, int mobId, int mobCount, Point spawnPoint, long hp, int exp) {
        this.mapId = mapId;
        this.mobId = mobId;
        this.mobCount = mobCount;
        this.spawnPoint = spawnPoint;
        this.hp = hp;
        this.exp = exp;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getMobId() {
        return mobId;
    }

    public void setMobId(int mobId) {
        this.mobId = mobId;
    }

    public int getMobCount() {
        return mobCount;
    }

    public void setMobCount(int mobCount) {
        this.mobCount = mobCount;
    }

    public Point getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Point spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getS_day() {
        return s_day;
    }

    public void setS_day(int s_day) {
        this.s_day = s_day;
    }

    public int getS_hour() {
        return s_hour;
    }

    public void setS_hour(int s_hour) {
        this.s_hour = s_hour;
    }

    public int getS_min() {
        return s_min;
    }

    public void setS_min(int s_min) {
        this.s_min = s_min;
    }

}
