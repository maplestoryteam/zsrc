/*
 ZEVMS冒险岛(079)游戏服务端脚本
 类型：装备附魔类类型
 时间：2018-08-09
 作者：ZEV，坐和放宽
 引用; 1032002_1,1032002_2,1032002_3
 */
var jt = "#fUI/Basic/BtHide3/mouseOver/0#";
function start() {
    status = -1;
    action(1, 0, 0)
}
function action(mode, type, selection) {
    if (status <= 0 && mode <= 0) {
        cm.dispose();
        return
    }
    if (mode == 1) {
        status++
    } else {
        status--

    }
    if (status <= 0) {
        var selStr = "你想做什么?\r\n\r\n";
        selStr += "#b#L0#进入到钓鱼场#v4220084##l\r\n";
        selStr += "#b#L1#购买普通鱼饵#v2300000##l\r\n";
        selStr += "#b#L2#购买钓鱼椅子#v3011000##l\r\n ";
        selStr += "#b#L3#使用鱼饵罐头#v5350000##l\r\n";
        selStr += "#b#L4#钓鱼说明攻略#v4161004##l\r\n";
        cm.sendSimple(selStr)
    } else if (status == 1) {
        switch (selection) {
            //进入渔场
            case 0:
                if (cm.haveItem(5340000) || cm.haveItem(5340001)) {
                    if (cm.haveItem(3011000)) {
                        cm.saveLocation("FISHING");
                        cm.warp(741000200);
                        cm.dispose();
                    } else {
                        cm.sendNext("你必须有钓鱼椅才能钓鱼!");
                        cm.safeDispose();
                    }
                } else {
                    cm.sendNext("你必须有钓鱼竿才能钓鱼!");
                    cm.safeDispose();
                }
                cm.safeDispose();
                break;
                //购买鱼饵
            case 1:
                if (cm.canHold(2300000, 120) && cm.getMeso() >= 3000) {
                    if (!cm.haveItem(2300000)) {
                        cm.gainMeso(-3000);
                        cm.gainItem(2300000, 120);
                        cm.sendNext("快乐钓鱼~");
                    } else {
                        cm.sendNext("你已经有了钓鱼的诱饵.");
                    }
                } else {
                    cm.sendOk("请检查是否有足够背包空间.");
                }
                cm.safeDispose();
                break;
                //购买椅子
            case 2:
                if (cm.haveItem(3011000)) {
                    cm.sendNext("你已经有一把钓鱼椅了。每个角色都只能有1个钓鱼椅.");
                } else {
                    if (cm.canHold(3011000) && cm.getMeso() >= 50000) {
                        cm.gainMeso(-50000);
                        cm.gainItem(3011000, 1);
                        cm.sendNext("快乐钓鱼~");
                    } else {
                        cm.sendOk("请检查是否有足够的金币或足够的背包栏.");
                    }
                }
                cm.safeDispose();
                break;
                //使用鱼罐头
            case 3:
                if (cm.canHold(2300001, 120) && cm.haveItem(5350000, 1)) {
                    if (!cm.haveItem(2300001)) {
                        cm.gainItem(2300001, 120);
                        cm.gainItem(5350000, -1);
                        cm.sendNext("快乐钓鱼~");
                    } else {
                        cm.sendNext("你已经有了钓鱼的诱饵.");
                    }
                } else {
                    cm.sendOk("请检查背包空间或你没有高级鱼饵罐头请从商城购买.");
                }
                cm.safeDispose();
                break;
                //钓鱼攻略
            case 4:
                cm.sendOk("你需要10级以上，有鱼竿、鱼饵，钓椅进入钓鱼场。你将在每1分钟一次。跟渔场记录人余明看看你捕捉记录!");
                cm.safeDispose();
                break;

        }
    }
}