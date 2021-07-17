package server;

import java.lang.ref.WeakReference;
import client.MapleCharacter;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;

public class MapleCarnivalChallenge {

    WeakReference<MapleCharacter> challenger;
    String challengeinfo = "";

    public MapleCarnivalChallenge(MapleCharacter challenger) {
        this.challenger = new WeakReference<MapleCharacter>(challenger);
        challengeinfo += "#b";
        for (MaplePartyCharacter pc : challenger.getParty().getMembers()) {
            MapleCharacter c = challenger.getMap().getCharacterById(pc.getId());
            if (c != null) {
                challengeinfo += ("玩家名字: " + c.getName() + " / 等级: " + c.getLevel() + " / 职业:  " + getJobNameById(c.getJob()) + "\r\n");
            }
        }
        challengeinfo += "#k";
    }

    public MapleCharacter getChallenger() {
        return challenger.get();
    }

    public String getChallengeInfo() {
        return challengeinfo;
    }

    public static final String getJobNameById(int job) {
        switch (job) {
            case 0:
                return "新手";
            case 1000:
                return "初心者";
            case 2000:
                return "战童";
            case 100:
                return "战士";
            case 110:
                return "剑客";
            case 111:
                return "勇士";
            case 112:
                return "英雄";
            case 120:
                return "准骑士";
            case 121:
                return "骑士";
            case 122:
                return "圣骑士";
            case 130:
                return "枪战士";
            case 131:
                return "龙骑士";
            case 132:
                return "黑骑士";
            case 200:
                return "魔法师";
            case 210:
                return "法师(火,毒)";
            case 211:
                return "巫师(火,毒)";
            case 212:
                return "魔导师(火,毒)";
            case 220:
                return "法师(冰,雷)";
            case 221:
                return "巫师(冰,雷)";
            case 222:
                return "魔导师(冰,雷)";
            case 230:
                return "牧师";
            case 231:
                return "祭司";
            case 232:
                return "主教";
            case 300:
                return "弓箭手";
            case 310:
                return "猎人";
            case 311:
                return "射手";
            case 312:
                return "神射手";
            case 320:
                return "弩弓手";
            case 321:
                return "游侠";
            case 322:
                return "箭神";
            case 400:
                return "飞侠";
            case 410:
                return "刺客";
            case 411:
                return "无影人";
            case 412:
                return "隐士";
            case 420:
                return "侠客";
            case 421:
                return "独行客";
            case 422:
                return "侠盗";
            case 500:
                return "海盜";
            case 510:
                return "拳手";
            case 511:
                return "斗士";
            case 512:
                return "冲锋队长";
            case 520:
                return "火枪手";
            case 521:
                return "大副";
            case 522:
                return "船长";
            case 1100:
                return "魂骑士(一转)";
            case 1110:
                return "魂骑士(二转)";
            case 1111:
                return "魂骑士(三转)";
            case 1112:
                return "魂骑士(四转)";
            case 1200:
                return "炎术士(一转)";
            case 1210:
                return "炎术士(二转)";
            case 1211:
                return "炎术士(三转)";
            case 1212:
                return "炎术士(四转)";
            case 1300:
                return "风灵使者(一转)";
            case 1310:
                return "风灵使者(二转)";
            case 1311:
                return "风灵使者(三转)";
            case 1312:
                return "风灵使者(四转)";
            case 1400:
                return "夜行者(一转)";
            case 1410:
                return "夜行者(二转)";
            case 1411:
                return "夜行者(三转)";
            case 1412:
                return "夜行者(四转)";
            case 1500:
                return "奇袭者(一转)";
            case 1510:
                return "奇袭者(二转)";
            case 1511:
                return "奇袭者(三转)";
            case 1512:
                return "奇袭者(四转)";
            case 2100:
                return "战神(一转)";
            case 2110:
                return "战神(二转)";
            case 2111:
                return "战神(三转)";
            case 2112:
                return "战神(四转)";
            default:
                return "未知职业";
        }
    }

    public static final String getJobBasicNameById(int job) {
        switch (job) {
            case 0:
                return "新手";
            case 1000:
                return "初心者";
            case 2000:
                return "战童";
            case 100:
                return "战士";
            case 110:
                return "剑客";
            case 111:
                return "勇士";
            case 112:
                return "英雄";
            case 120:
                return "准骑士";
            case 121:
                return "骑士";
            case 122:
                return "圣骑士";
            case 130:
                return "枪战士";
            case 131:
                return "龙骑士";
            case 132:
                return "黑骑士";

            case 200:
                return "魔法师";
            case 210:
                return "法师(火,毒)";
            case 211:
                return "巫师(火,毒)";
            case 212:
                return "魔导师(火,毒)";
            case 220:
                return "法师(冰,雷)";
            case 221:
                return "巫师(冰,雷)";
            case 222:
                return "魔导师(冰,雷)";
            case 230:
                return "牧师";
            case 231:
                return "祭司";
            case 232:
                return "主教";

            case 300:
                return "弓箭手";
            case 310:
                return "猎人";
            case 311:
                return "射手";
            case 312:
                return "神射手";
            case 320:
                return "弩弓手";
            case 321:
                return "游侠";
            case 322:
                return "箭神";

            case 400:
                return "飞侠";
            case 410:
                return "刺客";
            case 411:
                return "无影人";
            case 412:
                return "隐士";
            case 420:
                return "侠客";
            case 421:
                return "独行客";
            case 422:
                return "侠盗";
            case 500:
                return "海盜";
            case 510:
                return "拳手";
            case 511:
                return "斗士";
            case 512:
                return "冲锋队长";
            case 520:
                return "火枪手";
            case 521:
                return "大副";
            case 522:
                return "船长";

            case 1100:
                return "魂骑士(一转)";
            case 1110:
                return "魂骑士(二转)";
            case 1111:
                return "魂骑士(三转)";
            case 1112:
                return "魂骑士(四转)";

            case 1200:
                return "炎术士(一转)";
            case 1210:
                return "炎术士(二转)";
            case 1211:
                return "炎术士(三转)";
            case 1212:
                return "炎术士(四转)";

            case 1300:
                return "风灵使者(一转)";
            case 1310:
                return "风灵使者(二转)";
            case 1311:
                return "风灵使者(三转)";
            case 1312:
                return "风灵使者(四转)";

            case 1400:
                return "夜行者(一转)";
            case 1410:
                return "夜行者(二转)";
            case 1411:
                return "夜行者(三转)";
            case 1412:
                return "夜行者(四转)";

            case 1500:
                return "奇袭者(一转)";
            case 1510:
                return "奇袭者(二转)";
            case 1511:
                return "奇袭者(三转)";
            case 1512:
                return "奇袭者(四转)";

            case 2100:
                return "战神(一转)";
            case 2110:
                return "战神(二转)";
            case 2111:
                return "战神(三转)";
            case 2112:
                return "战神(四转)";

            default:
                return "未知职业";
        }
    }
}
