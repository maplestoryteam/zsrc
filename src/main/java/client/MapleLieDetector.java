package client;

import scripting.LieDetectorScript;
import server.Timer.EtcTimer;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import tools.HexTool;
import tools.MaplePacketCreator;
import tools.Pair;

public class MapleLieDetector {

    public MapleCharacter chr;

    public byte type;

    public int attempt;

    public String tester;

    public String answer;

    public boolean inProgress;

    public boolean passed;

    public MapleLieDetector(MapleCharacter c) {
        this.chr = c;
        reset();
    }

    public boolean startLieDetector(final String tester, final boolean isItem, boolean anotherAttempt) {
        if ((!anotherAttempt) && ((this.chr.isClone()) || ((isPassed()) && (isItem)) || (inProgress()) || (this.attempt == 3))) {
            return false;
        }
        Pair captcha = LieDetectorScript.getImageBytes();
        if (captcha == null) {
            return false;
        }
        byte[] image = HexTool.getByteArrayFromHexString((String) captcha.getLeft());
        this.answer = ((String) captcha.getRight());
        this.tester = tester;
        this.inProgress = true;
        this.type = (byte) (isItem ? 0 : 1);
        this.attempt += 1;

        this.chr.getClient().sendPacket(MaplePacketCreator.sendLieDetector(image, this.attempt));
        EtcTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if ((!MapleLieDetector.this.isPassed()) && (MapleLieDetector.this.chr != null)) {
                    if (MapleLieDetector.this.attempt >= 3) {
                        MapleCharacter search_chr = chr.getMap().getCharacterByName(tester);
                        if ((search_chr != null) && (search_chr.getId() != MapleLieDetector.this.chr.getId())) {
                            search_chr.dropMessage(5, MapleLieDetector.this.chr.getName() + " 没用通过测谎仪的检测，恭喜你获得7000的金币.");
                            search_chr.gainMeso(7000, true);
                        }
                        end();
                        chr.getClient().sendPacket(MaplePacketCreator.LieDetectorResponse((byte) 8, (byte) 4));
                        MapleMap map = MapleLieDetector.this.chr.getClient().getChannelServer().getMapFactory().getMap(180000001);
                        chr.getQuestNAdd(MapleQuest.getInstance(123456)).setCustomData(String.valueOf(30 * 60));
                        chr.changeMap(map, map.getPortal(0));
                        // World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "[GM Message] 玩家: " + MapleLieDetector.this.chr.getName() + " (等级 " + MapleLieDetector.this.chr.getLevel() + ") 未通过测谎仪检测，系统将其监禁30分钟！"));
                    } else {
                        startLieDetector(tester, isItem, true);
                    }
                }
            }
        }, 60 * 1000);//60秒

        return true;
    }

    public int getAttempt() {
        return this.attempt;
    }

    public byte getLastType() {
        return this.type;
    }

    public String getTester() {
        return this.tester;
    }

    public String getAnswer() {
        return this.answer;
    }

    public boolean inProgress() {
        return this.inProgress;
    }

    public boolean isPassed() {
        return this.passed;
    }

    public void end() {
        this.inProgress = false;
        this.passed = true;
        this.attempt = 0;
    }

    public void reset() {
        this.tester = "";
        this.answer = "";
        this.attempt = 0;
        this.inProgress = false;
        this.passed = false;
    }
}
