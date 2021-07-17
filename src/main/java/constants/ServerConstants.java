/*
游戏输出
 */
package constants;

import server.ServerProperties;

public class ServerConstants {

    // 开始投票
    public static final boolean PollEnabled = false;//falseture
    public static final String Poll_Question = "Are you mudkiz?";
    public static final String[] Poll_Answers = {"test1", "test2", "test3"};
    // 最后的投票
    /*public static final short 检测客户端版本 = 79;//MAPLE_VERSION
    public static final String 检测客户端小版本 = "1";//MAPLE_PATCH*/
    public static final boolean Use_Fixed_IV = false;//false
    public static final int MIN_MTS = 110;
    public static final int MTS_BASE = 100; //+1000 to everything in MSEA but cash is costly here
    public static final int MTS_TAX = 10; //+% to everything
    public static final int MTS_MESO = 5000; //mesos needed
    public static final int CHANNEL_COUNT = 200;
    //服务端输出操作
    public static boolean 封包显示 = Boolean.parseBoolean(ServerProperties.getProperty("ZEV.封包显示", "false"));//
    public static boolean 调试输出封包 = Boolean.parseBoolean(ServerProperties.getProperty("ZEV.调试输出封包", "ture"));
    public static boolean 自动注册 = true;
    public static boolean Super_password = false;
    public static final boolean PACKET_ERROR_OFF = Boolean.parseBoolean(ServerProperties.getProperty("ZEV.记录38错误", "false"));
    public static String Gateway_IP_String;
    public static boolean 调试模式;
    public static boolean loadop;
    public static String superpw = "";
    public static String PACKET_ERROR = "";
    //为了解决卡号
    public static boolean clientAutoDisconnect = true;

    public static String getPACKET_ERROR() {
        return PACKET_ERROR;
    }

    public static void setPACKET_ERROR(String PACKET_ERROR) {
        ServerConstants.PACKET_ERROR = PACKET_ERROR;
    }

    public static boolean getAutoReg() {
        return 自动注册;
    }

    public static String ChangeAutoReg() {
        自动注册 = !getAutoReg();
        return 自动注册 ? "开启" : "关闭";
    }

    public static final byte Class_Bonus_EXP(final int job) {
        switch (job) {
            case 3000: //whenever these arrive, they'll give bonus
            case 3200:
            case 3210:
            case 3211:
            case 3212:
            case 3300:
            case 3310:
            case 3311:
            case 3312:
            case 3500:
            case 3510:
            case 3511:
            case 3512:
                return 10;
        }
        return 0;
    }

    public static enum PlayerGMRank {//管理符号
        玩家指令('@', 0),
        巡查管理('*', 1),
        活动管理('*', 2),
        游戏管理('*', 3);
        /*NORMAL('@', 0),
        INTERN('!', 1),
        GM('!', 2),
        ADMIN('!', 3);
        SUPERADMIN('!', 3);*/
        private char commandPrefix;
        private int level;

        private PlayerGMRank(char ch, int level) {
            commandPrefix = ch;
            this.level = level;
        }

        public char getCommandPrefix() {
            return commandPrefix;
        }

        public int getLevel() {
            return level;
        }

    }

    public static enum CommandType {

        玩家指令(0),
        TRADE(1);
        private int level;

        private CommandType(int level) {
            this.level = level;
        }

        public int getType() {
            return level;
        }
    }
}
