/*
OX答题

 */
package server.events;

import client.MapleCharacter;
import client.MapleStat;
import server.Timer.EventTimer;
import server.events.MapleOxQuizFactory.MapleOxQuizEntry;
import server.maps.MapleMap;
import tools.MaplePacketCreator;
import tools.Pair;

import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;

public class MapleOxQuiz extends MapleEvent {

    private ScheduledFuture<?> oxSchedule, oxSchedule2;
    private int timesAsked = 0;

    public MapleOxQuiz(final int channel, final int[] mapid) {
        super(channel, mapid);
    }

    private void resetSchedule() {
        if (oxSchedule != null) {
            oxSchedule.cancel(false);
            oxSchedule = null;
        }
        if (oxSchedule2 != null) {
            oxSchedule2.cancel(false);
            oxSchedule2 = null;
        }
    }

    @Override
    public void onMapLoad(MapleCharacter chr) {
        if (chr.getMapId() == mapid[0] && !chr.isGM()) {
            chr.canTalk(false);
        }
    }

    @Override
    public void reset() {
        super.reset();
        getMap(0).getPortal("join00").setPortalState(false);
        resetSchedule();
        timesAsked = 0;
    }

    @Override
    public void unreset() {
        super.unreset();
        getMap(0).getPortal("join00").setPortalState(true);
        resetSchedule();
    }
    //apparently npc says 10 questions

    @Override
    public void startEvent() {
        sendQuestion();

    }

    public void sendQuestion() {
        sendQuestion(getMap(109020001));
        
    }

    public void sendQuestion(final MapleMap toSend) {
        if (oxSchedule2 != null) {
            oxSchedule2.cancel(false);
        }
        oxSchedule2 = EventTimer.getInstance().schedule(new Runnable() {

            public void run() {
                int number = 0;
                for (MapleCharacter mc : toSend.getCharactersThreadsafe()) {
                    if (mc.isGM() || !mc.isAlive()) {
                        number++;
                    }
                }
                if (toSend.getCharactersSize() - number <= 0 || timesAsked == 10) {
                    toSend.broadcastMessage(MaplePacketCreator.serverNotice(6, "人数不足！活动自动结束！"));
                    unreset();
                    for (MapleCharacter chr : toSend.getCharactersThreadsafe()) {
                        if (chr != null && !chr.isGM() && chr.isAlive()) {
                            chr.canTalk(true);
                            //chr.finishAchievement(19);
                            givePrize(chr);
                            warpBack(chr);
                        }
                    }
                    //prizes here
                    return;
                }
                final Entry<Pair<Integer, Integer>, MapleOxQuizEntry> question = MapleOxQuizFactory.getInstance().grabRandomQuestion();
                toSend.broadcastMessage(MaplePacketCreator.showOXQuiz(question.getKey().left, question.getKey().right, true));
                toSend.broadcastMessage(MaplePacketCreator.getClock(12)); //quickly change to 12
                if (oxSchedule != null) {
                    oxSchedule.cancel(false);
                }
                oxSchedule = EventTimer.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        toSend.broadcastMessage(MaplePacketCreator.showOXQuiz(question.getKey().left, question.getKey().right, false));
                        timesAsked++;
                        for (MapleCharacter chr : toSend.getCharactersThreadsafe()) {
                            if (chr != null && !chr.isGM() && chr.isAlive()) { // make sure they aren't null... maybe something can happen in 12 seconds.
                                if (!isCorrectAnswer(chr, question.getValue().getAnswer())) {
                                    chr.getStat().setHp((short) 0);
                                    chr.updateSingleStat(MapleStat.HP, 0);
                                } else {
                                    chr.gainExp(3000, true, true, false);
                                }
                            }
                        }
                        sendQuestion();
                    }
                }, 12000); // Time to answer = 30 seconds ( Ox Quiz packet shows a 30 second timer.
            }
        }, 10000);
    }

    private boolean isCorrectAnswer(MapleCharacter chr, int answer) {
        double x = chr.getPosition().getX();
        double y = chr.getPosition().getY();
        if ((x > -234 && y > -26 && answer == 0) || (x < -234 && y > -26 && answer == 1)) {
            chr.dropMessage(6, "[OX答题活动] 恭喜答对!"); 
            return true;
        }
        chr.dropMessage(6, "[OX答题活动] 你答错了!");
        return false;
    }
}
