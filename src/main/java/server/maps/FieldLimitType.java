package server.maps;

public enum FieldLimitType {

    Jump(0x1),
    MovementSkills(0x2),
    SummoningBag(0x04),
    MysticDoor(0x08),
    ChannelSwitch(0x10),
    RegularExpLoss(0x20),
    VipRock(0x40),
    Minigames(0x80),
    NoClue1(0x100), // APQ and a couple quest maps have this
    Mount(0x200),
    PotionUse(0x400), //or 0x40000
    Event(0x2000),
    Pet(0x8000), //needs confirmation
    Event2(0x10000),
    DropDown(0x20000), //   NoClue7(0x40000) // Seems to .. disable Rush if 0x2 is set
    ;
    private final int i;

    FieldLimitType(int i) {
        this.i = i;
    }

    public final int getValue() {
        return i;
    }

    public final boolean check(int fieldlimit) {
        return (fieldlimit & i) == i;
    }
}
