package handling.login;

import client.MapleClient;
import handling.channel.ChannelServer;
import server.ServerProperties;
import server.Timer.PingTimer;
import tools.MaplePacketCreator;
import tools.packet.LoginPacket;

import java.util.Map;
import java.util.Map.Entry;

public class LoginWorker {

    private static long lastUpdate = 0;

    public static void registerClient(final MapleClient c) {
        int 禁止登陆开关 = gui.Start.ConfigValuesMap.get("禁止登陆开关");
        if (!c.isGm()) {
            if (禁止登陆开关 <= 0) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "游戏正在维护中。"));
                c.sendPacket(LoginPacket.getLoginFailed(7));
                return;
            }
        }

        if (LoginServer.isAdminOnly() && !c.isGm()) {
            c.sendPacket(MaplePacketCreator.serverNotice(1, "游戏正在维护。"));
            c.sendPacket(LoginPacket.getLoginFailed(1));
            return;
        }

        if (System.currentTimeMillis() - lastUpdate > 10 * 60 * 1000) { // Update once every 10 minutes
            lastUpdate = System.currentTimeMillis();
            final Map<Integer, Integer> load = ChannelServer.getChannelLoad();
            int usersOn = 0;

            if (load == null || load.size() <= 0) { // In an unfortunate event that client logged in before load
                lastUpdate = 0;
                c.sendPacket(LoginPacket.getLoginFailed(7));
                return;
            }
            double loads = load.size();
            double userlimit = LoginServer.getUserLimit();
            final double loadFactor = 1200 / ((double) LoginServer.getUserLimit() / load.size());
            for (Entry<Integer, Integer> entry : load.entrySet()) {
                usersOn += entry.getValue();
                load.put(entry.getKey(), Math.min(1200, (int) (entry.getValue() * loadFactor)));

            }
            LoginServer.setLoad(load, usersOn);
            lastUpdate = System.currentTimeMillis();

        }

        if (c.finishLogin() == 0) {
            if (c.getGender() == 10) {
                c.sendPacket(LoginPacket.getGenderNeeded(c));//
            } else {
                c.sendPacket(LoginPacket.getAuthSuccessRequest(c));
                if (gui.Start.ConfigValuesMap.get("蓝蜗牛开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(0, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.蓝蜗牛状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("蘑菇仔开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(1, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.蘑菇仔状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("绿水灵开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(2, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.绿水灵状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("漂漂猪开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(3, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.漂漂猪状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("小青蛇开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(4, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.小青蛇状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("红螃蟹开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(5, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.红螃蟹状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("大海龟开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(6, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.大海龟状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("章鱼怪开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(7, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.章鱼怪状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("顽皮猴开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(8, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.顽皮猴状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("星精灵开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(9, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.星精灵状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("胖企鹅开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(10, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.胖企鹅状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("白雪人开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(11, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.白雪人状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("石头人开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(12, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.石头人状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("紫色猫开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(13, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.紫色猫状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("大灰狼开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(14, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.大灰狼状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("小白兔开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(15, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.小白兔状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("喷火龙开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(16, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.喷火龙状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("火野猪开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(17, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.火野猪状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("青鳄鱼开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(18, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.青鳄鱼状态"))));
                }
                if (gui.Start.ConfigValuesMap.get("花蘑菇开关") == 0) {
                    c.sendPacket(LoginPacket.getServerList(19, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.花蘑菇状态"))));
                }
                c.sendPacket(LoginPacket.getEndOfServerList());
            }
            c.setIdleTask(PingTimer.getInstance().schedule(new Runnable() {

                public void run() {
//                    c.getSession().close();
                }
            }, 10 * 60 * 10000));
        } else if (c.getGender() == 10) {
            c.sendPacket(LoginPacket.getGenderNeeded(c));

        } else {
            c.sendPacket(LoginPacket.getAuthSuccessRequest(c));
            if (gui.Start.ConfigValuesMap.get("蓝蜗牛开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(0, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.蓝蜗牛状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("蘑菇仔开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(1, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.蘑菇仔状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("绿水灵开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(2, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.绿水灵状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("漂漂猪开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(3, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.漂漂猪状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("小青蛇开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(4, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.小青蛇状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("红螃蟹开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(5, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.红螃蟹状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("大海龟开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(6, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.大海龟状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("章鱼怪开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(7, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.章鱼怪状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("顽皮猴开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(8, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.顽皮猴状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("星精灵开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(9, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.星精灵状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("胖企鹅开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(10, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.胖企鹅状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("白雪人开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(11, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.白雪人状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("石头人开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(12, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.石头人状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("紫色猫开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(13, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.紫色猫状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("大灰狼开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(14, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.大灰狼状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("小白兔开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(15, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.小白兔状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("喷火龙开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(16, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.喷火龙状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("火野猪开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(17, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.火野猪状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("青鳄鱼开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(18, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.青鳄鱼状态"))));
            }
            if (gui.Start.ConfigValuesMap.get("花蘑菇开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(19, LoginServer.getServerName(), LoginServer.getLoad(), Integer.parseInt(ServerProperties.getProperty("ZEV.花蘑菇状态"))));
            }
            c.sendPacket(LoginPacket.getEndOfServerList());
        }
        /* c.sendPacket(LoginPacket.getLoginFailed(7));

            System.out.println("登录Z");
            return
        if (server.Start.ConfigValuesMap.get("蓝蜗牛开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(0, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("蘑菇仔开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(1, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("绿水灵开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(2, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("漂漂猪开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(3, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("小青蛇开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(4, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("红螃蟹开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(5, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("大海龟开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(6, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("章鱼怪开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(7, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("顽皮猴开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(8, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("星精灵开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(9, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("胖企鹅开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(10, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("白雪人开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(11, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("石头人开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(12, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("紫色猫开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(13, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("大灰狼开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(14, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("小白兔开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(15, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("喷火龙开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(16, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("火野猪开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(17, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("青鳄鱼开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(18, LoginServer.getServerName(), LoginServer.getLoad()));
            }
            if (server.Start.ConfigValuesMap.get("花蘑菇开关") == 0) {
                c.sendPacket(LoginPacket.getServerList(19, LoginServer.getServerName(), LoginServer.getLoad()));
            }
        
        ;*/
    }
}
