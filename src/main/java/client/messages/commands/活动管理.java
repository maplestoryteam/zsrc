package client.messages.commands;

import constants.ServerConstants.PlayerGMRank;
import client.MapleClient;
import handling.world.World;
import scripting.EventManager;
import server.Timer;
import server.events.MapleEvent;
import server.events.MapleEventType;
import tools.MaplePacketCreator;
import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author 管理权限2
 */
public class 活动管理 {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.活动管理;
    }

}
