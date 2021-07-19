
package server.maps;

import client.MapleClient;

import java.awt.*;

public interface MapleMapObject {

    int getObjectId();

    void setObjectId(final int id);

    MapleMapObjectType getType();

    Point getPosition();

    void setPosition(final Point position);

    void sendSpawnData(final MapleClient client);

    //public void setPickedUp(final boolean pickedUp);
    void sendDestroyData(final MapleClient client);
}
