package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import tools.MaplePacketCreator;

public class MapleDragon extends AbstractAnimatedMapleMapObject {

    private final int owner;
    private final int jobid;

    public MapleDragon(MapleCharacter owner) {
        super();
        this.owner = owner.getId();
        this.jobid = owner.getJob();
        if (jobid < 2200 || jobid > 2218) {
            throw new RuntimeException("试图为非埃文创造一条龙");
        }
        setPosition(owner.getPosition());
        setStance(4);
    }

    @Override
    public void sendSpawnData(MapleClient client) {
       // client.sendPacket(MaplePacketCreator.spawnDragon(this));
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.sendPacket(MaplePacketCreator.removeDragon(this.owner));
    }

    public int getOwner() {
        return this.owner;
    }

    public int getJobId() {
        return this.jobid;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.SUMMON;
    }
}
