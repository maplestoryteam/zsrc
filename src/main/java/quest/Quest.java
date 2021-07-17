package quest;

public class Quest {

    public static boolean Quest(int a) {
        switch (a) {
            case 3936://阿里安特任务
            case 2000://酋长的地契
            case 2103://内拉的梦
            case 2104://潘喜的红色毛球
            case 2254://沙漠的卡乐卡萨
            case 2257://卡乐卡萨的告白
            case 2258://白天说话订满会听见
            case 2259://晚上说话蝎子会听见
            case 2260://前往蘑菇城
            case 2261://骆驼重复1
            case 2262://骆驼重复2
            case 2263://骆驼重复3
            case 2232://登陆同学
            case 3953://沙漠
            case 6029://锻造技能
            case 21729://战神的第四次收集
                return true;
            default:
                return false;
        }
    }

    public static boolean canForfeit(int questid) {
        switch (questid) {
            case 6029://锻造技能
            case 2001://酋长的地契
            case 2261://骆驼重复1
            case 2262://骆驼重复2
            case 2263://骆驼重复3
            case 20010:
            case 20015:
            case 20020:
                return false;
            default:
                return true;
        }
    }
}
