package server.custom.auction;

public enum AuctionState {
    �¼�(0), �ϼ�(1), ����(2);
    private final int state;

    AuctionState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public static AuctionState getState(int state) {
        for (AuctionState as : values()) {
            if (as.state == state) {
                return as;
            }
        }
        return null;
    }

}
