package server.maps;

public interface AnimatedMapleMapObject extends MapleMapObject {

    int getStance();

    void setStance(int stance);

    boolean isFacingLeft();

    int getFh();

    void setFh(int fh);
}
