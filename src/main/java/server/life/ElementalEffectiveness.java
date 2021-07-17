package server.life;

public enum ElementalEffectiveness {

    NORMAL, IMMUNE, STRONG, WEAK, NEUTRAL;

    public static ElementalEffectiveness getByNumber(int num) {
        switch (num) {
            case 1:
                //免疫
                return IMMUNE;
            case 2:
                //增强
                return STRONG;
            case 3:
                //虚弱
                return WEAK;
            case 4:
                //无视
                return NEUTRAL;
            default:
                throw new IllegalArgumentException("Unkown effectiveness: " + num);
        }
    }
}
