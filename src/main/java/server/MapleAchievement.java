package server;

/**
 *
 * @author KyleShum
 */
public class MapleAchievement {

    /* private String name;
     private int reward;
     private boolean notice;

     public MapleAchievement(String name, int reward) {
     this.name = name;
     this.reward = reward;
     this.notice = true;
     }

     public MapleAchievement(String name, int reward, boolean notice) {
     this.name = name;
     this.reward = reward;
     this.notice = notice;
     }

     public String getName() {
     return name;
     }

     public void setName(String name) {
     this.name = name;
     }

     public int getReward() {
     return reward;
     }

     public void setReward(int reward) {
     this.reward = reward;
     }

     public boolean getNotice() {
     return notice;
     }

     public void finishAchievement(MapleCharacter chr) {
     chr.modifyAchievementCSPoints(1, reward);
     chr.setAchievementFinished(MapleAchievements.getInstance().getByMapleAchievement(this));
     if (notice && !chr.isGM()) {
     World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[Achievement] Congratulations to " + chr.getName() + " on " + name + " and rewarded with " + reward + " A-cash!").getBytes());
     } else {
     chr.getClient().sendPacket(MaplePacketCreator.serverNotice(5, "[Achievement] You've gained " + reward + " A-cash as you " + name + "."));
     }
     }*/
}
