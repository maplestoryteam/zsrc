package server.maps;

public abstract class AbstractAnimatedMapleMapObject extends AbstractMapleMapObject implements AnimatedMapleMapObject {

    private int stance;
    private int fh;

    public int getStance() {
        return stance;
    }

    public void setStance(int stance) {
        this.stance = stance;
    }

    public boolean isFacingLeft() {
        return getStance() % 2 != 0;
    }

    public int getFacingDirection() {
        return getStance() % 2;
    }

    @Override
    public int getFh() {
        return fh;
    }

    @Override
    public void setFh(int fh) {
        this.fh = fh;
    }
}
