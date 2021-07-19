package server.events;

public enum MapleEventType {

    打椰子比赛("椰子比赛", new int[]{109080000}), 
    打瓶盖比赛("打瓶盖", new int[]{109080010}), 
    向高地比赛("向高地", new int[]{109040000, 109040001, 109040002, 109040003, 109040004}),
    上楼上楼("上楼上楼", new int[]{109030001, 109030002, 109030003}),
    OX答题比赛("答题", new int[]{109020001}),
    推雪球比赛("雪球赛", new int[]{109060000}); 
    public String command;
    public int[] mapids;

    MapleEventType(String comm, int[] mapids) {
        this.command = comm;
        this.mapids = mapids;
    }

    public static final MapleEventType getByString(final String splitted) {
        for (MapleEventType t : MapleEventType.values()) {
            if (t.command.equalsIgnoreCase(splitted)) {
                return t;
            }
        }
        return null;
    }
}
