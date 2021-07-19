/*
NPC报错
 */
package scripting;

import abc.PNPC;
import client.MapleClient;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;

import static abc.Game.NPC前缀;
import static abc.Game.NPC错误文本提示;
import static abc.Game2.*;

public class NPCScriptManager extends AbstractScriptManager {

    private final Map<MapleClient, NPCConversationManager> cms = new WeakHashMap<MapleClient, NPCConversationManager>();
    private static final NPCScriptManager instance = new NPCScriptManager();

    public static final NPCScriptManager getInstance() {
        return instance;
    }

    public void start(MapleClient c, int npc) {
        start(c, npc, 0);
    }

    public final void start(final MapleClient c, final int npc, int wh) {
        if (c.getPlayer().getMapId() == 180000001) {
            return;
        }
        final String whStr = wh == 0 ? "" : "_" + wh;
        final String npcIdName = npc + whStr;
        if (npc > 100 && npc < 2000) {
            return;
        }
        if (npc == 9900004 && wh == 9900005) {
            if (gui.Start.ConfigValuesMap.get("拍卖行开关") > 0) {
                c.getPlayer().dropMessage(1, "管理员已经从后台关闭了拍卖行功能");
                return;
            }
            if (c.getPlayer().getMapId() == 100000203) {
                c.getPlayer().dropMessage(1, "此地图无法打开拍卖行");
                return;
            }
        }
        // 优化部分功能，需要快速访问
        //if (npc != 2000 && npc != 9900007 && npc != 9900004 && npc != 1012114 && npc != 9020002 && npc != 2040035 && npc != 9110014) {
        if (!c.canCallNpc(npcIdName)) {
            c.getPlayer().dropMessage(5, "操作过快了，请慢点。");
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        } else {
            c.setLastCallNpcTime(System.currentTimeMillis(), npcIdName);
        }
        //}

        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0 && c.getPlayer().isGM()) {
                //不显示的NPC代码
                PNPC itemjc2 = new PNPC();
                String items2 = itemjc2.getPM();
                if (npc != 2007) {
                    if (传送相关(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/传送相关/" + npc + "]");
                    } else if (传送相关(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/传送相关/" + npc + "," + wh + "]");
                    } else if (英语村(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-英语村/" + npc + "]");
                    } else if (英语村(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-英语村/" + npc + "," + wh + "]");
                    } else if (废弃都市(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-废弃都市/" + npc + "]");
                    } else if (废弃都市(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-废弃都市/" + npc + "," + wh + "]");
                    } else if (每日任务(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/每日任务/" + npc + "]");
                    } else if (每日任务(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/每日任务/" + npc + "," + wh + "]");
                    } else if (玩具塔(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-玩具塔/" + npc + "]");
                    } else if (玩具塔(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-玩具塔/" + npc + "," + wh + "]");
                    } else if (女神塔(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-女神塔/" + npc + "]");
                    } else if (女神塔(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-女神塔/" + npc + "," + wh + "]");
                    } else if (月妙(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-月妙/" + npc + "]");
                    } else if (月妙(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-月妙/" + npc + "," + wh + "]");
                    } else if (海盗船(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-海盗船/" + npc + "]");
                    } else if (海盗船(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-海盗船/" + npc + "," + wh + "]");
                    } else if (毒雾森林(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-毒雾森林/" + npc + "]");
                    } else if (毒雾森林(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-毒雾森林/" + npc + "," + wh + "]");
                    } else if (罗密欧与朱丽叶(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-罗密欧与朱丽叶/" + npc + "]");
                    } else if (罗密欧与朱丽叶(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/副本-罗密欧与朱丽叶/" + npc + "," + wh + "]");
                    } else if (冒险百科(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/冒险百科/" + npc + "]");
                    } else if (冒险百科(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/冒险百科/" + npc + "," + wh + "]");
                    } else if (礼包NPC(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/礼包相关/" + npc + "]");
                    } else if (礼包NPC(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/礼包相关/" + npc + "," + wh + "]");

                    } else if (zevms(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/zevms[加密执行文件/" + npc + "]");
                    } else if (zevms(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/zevms[加密执行文件/" + npc + "," + wh + "]");
                    } else if (飞天猪(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/飞天猪]/" + npc + "]");
                    } else if (飞天猪(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/飞天猪]/" + npc + "," + wh + "]");
                    } else if (消耗箱子(npc) && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/消耗箱子]/" + npc + "]");
                    } else if (消耗箱子(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/消耗箱子]/" + npc + "," + wh + "]");
                    } else if (活动相关(npc) && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/活动奖励]/" + npc + "," + wh + "]");
                    } else if (items2.contains("," + npc + ",") && wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/pnpc]/" + npc + "]");
                    } else if (items2.contains("," + npc + ",") && wh > 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/pnpc/" + npc + " _ " + wh + "]");
                    } else if (wh == 0) {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/npc/" + npc + "]");
                    } else {
                        c.getPlayer().dropMessage(5, "dialogue:[scripts/npc/" + npc + " _ " + wh + "]");
                    }
                }
            }

            if (!cms.containsKey(c)) {
                Invocable iv;
                PNPC itemjc2 = new PNPC();
                String items2 = itemjc2.getPM();
                if (传送相关(npc) && wh == 0) {
                    iv = getInvocable("传送相关/" + npc + ".js", c, true);
                } else if (传送相关(npc) && wh > 0) {
                    iv = getInvocable("传送相关/" + npc + "_" + wh + ".js", c, true);
                } else if (英语村(npc) && wh == 0) {
                    iv = getInvocable("副本-英语村/" + npc + ".js", c, true);
                } else if (英语村(npc) && wh > 0) {
                    iv = getInvocable("副本-英语村/" + npc + "_" + wh + ".js", c, true);
                } else if (废弃都市(npc) && wh == 0) {
                    iv = getInvocable("副本-废弃都市/" + npc + ".js", c, true);
                } else if (废弃都市(npc) && wh > 0) {
                    iv = getInvocable("副本-废弃都市/" + npc + "_" + wh + ".js", c, true);
                } else if (玩具塔(npc) && wh == 0) {
                    iv = getInvocable("副本-玩具塔/" + npc + ".js", c, true);
                } else if (玩具塔(npc) && wh > 0) {
                    iv = getInvocable("副本-玩具塔/" + npc + "_" + wh + ".js", c, true);
                } else if (女神塔(npc) && wh == 0) {
                    iv = getInvocable("副本-女神塔/" + npc + ".js", c, true);
                } else if (女神塔(npc) && wh > 0) {
                    iv = getInvocable("副本-女神塔/" + npc + "_" + wh + ".js", c, true);
                } else if (月妙(npc) && wh == 0) {
                    iv = getInvocable("副本-月妙/" + npc + ".js", c, true);
                } else if (月妙(npc) && wh > 0) {
                    iv = getInvocable("副本-月妙/" + npc + "_" + wh + ".js", c, true);
                } else if (海盗船(npc) && wh == 0) {
                    iv = getInvocable("副本-海盗船/" + npc + ".js", c, true);
                } else if (海盗船(npc) && wh > 0) {
                    iv = getInvocable("副本-海盗船/" + npc + "_" + wh + ".js", c, true);
                } else if (毒雾森林(npc) && wh == 0) {
                    iv = getInvocable("副本-毒雾森林/" + npc + ".js", c, true);
                } else if (毒雾森林(npc) && wh > 0) {
                    iv = getInvocable("副本-毒雾森林/" + npc + "_" + wh + ".js", c, true);
                } else if (罗密欧与朱丽叶(npc) && wh == 0) {
                    iv = getInvocable("副本-罗密欧与朱丽叶/" + npc + ".js", c, true);
                } else if (罗密欧与朱丽叶(npc) && wh > 0) {
                    iv = getInvocable("副本-罗密欧与朱丽叶/" + npc + "_" + wh + ".js", c, true);
                } else if (每日任务(npc) && wh == 0) {
                    iv = getInvocable("每日任务/" + npc + ".js", c, true);
                } else if (每日任务(npc) && wh > 0) {
                    iv = getInvocable("每日任务/" + npc + "_" + wh + ".js", c, true);
                } else if (冒险百科(npc) && wh == 0) {
                    iv = getInvocable("冒险百科/" + npc + ".js", c, true);
                } else if (冒险百科(npc) && wh > 0) {
                    iv = getInvocable("冒险百科/" + npc + "_" + wh + ".js", c, true);
                } else if (礼包NPC(npc) && wh == 0) {
                    iv = getInvocable("礼包相关/" + npc + ".js", c, true);
                } else if (礼包NPC(npc) && wh > 0) {
                    iv = getInvocable("礼包相关/" + npc + "_" + wh + ".js", c, true);
                } else if (zevms(npc) && wh == 0) {
                    iv = getInvocable("zevms/" + npc + ".js", c, true);
                } else if (zevms(npc) && wh > 0) {
                    iv = getInvocable("zevms/" + npc + "_" + wh + ".js", c, true);
                } else if (飞天猪(npc) && wh == 0) {
                    iv = getInvocable("飞天猪/" + npc + ".js", c, true);
                } else if (飞天猪(npc) && wh > 0) {
                    iv = getInvocable("飞天猪/" + npc + "_" + wh + ".js", c, true);
                } else if (消耗箱子(npc) && wh == 0) {
                    iv = getInvocable("消耗箱子/" + npc + ".js", c, true);
                } else if (消耗箱子(npc) && wh > 0) {
                    iv = getInvocable("消耗箱子/" + npc + "_" + wh + ".js", c, true);
                } else if (活动相关(npc) && wh > 0) {
                    iv = getInvocable("活动奖励/" + npc + "_" + wh + ".js", c, true);
                } else if (items2.contains("," + npc + ",") && wh == 0) {
                    iv = getInvocable("pnpc/" + npc + ".js", c, true);
                } else if (items2.contains("," + npc + ",") && wh > 0) {
                    iv = getInvocable("pnpc/" + npc + "_" + wh + ".js", c, true);
                } else if (wh == 0) {
                    iv = getInvocable("npc/" + npc + ".js", c, true);
                } else {
                    iv = getInvocable("npc/" + npc + "_" + wh + ".js", c, true);
                }

                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm;
                if (wh == 0) {
                    cm = new NPCConversationManager(c, npc, -1, (byte) -1, iv, 0);
                } else {
                    cm = new NPCConversationManager(c, npc, -1, (byte) -1, iv, wh);
                }

                cms.put(c, cm);
                if ((iv == null) || (getInstance() == null)) {

                    if (wh == 0) {
                        cm.sendOk("" + NPC错误文本提示 + "");
                        c.getPlayer().dropMessage(5, "[脚本错误]: " + npc + "");
                    } else {
                        cm.sendOk("" + NPC错误文本提示 + "");
                        c.getPlayer().dropMessage(5, "[脚本错误]: " + npc + "_" + wh + "");
                    }
                    cm.dispose();
                    return;
                }
                scriptengine.put("" + NPC前缀 + "", cm);
                //scriptengine.put("" + NPC前缀2 + "", cm);
                scriptengine.put("npcid", npc);
                c.getPlayer().setConversation(1);

                try {
                    iv.invokeFunction("start"); // Temporary until I've removed all of start
                } catch (NoSuchMethodException nsme) {
                    iv.invokeFunction("action", (byte) 1, (byte) 0, 0);
                }
            } else {
                c.sendPacket(MaplePacketCreator.enableActions());//解卡
                NPCScriptManager.getInstance().dispose(c);
            }
        } catch (final Exception e) {
            System.err.println("[错误提示] NPC： " + npc + "_" + wh + "." + e);
            if (c.getPlayer().isGM()) {
                c.getPlayer().dropMessage(6, "[错误提示] NPC： " + npc + "_" + wh + " 脚本错误 " + e + "");
            }
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing NPC script, NPC ID : " + npc + "_" + wh + "." + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public void action(final MapleClient c, final byte mode, final byte type, final int selection) {
        action(c, mode, type, selection, 0);
    }

    public final void action(final MapleClient c, final byte mode, final byte type, final int selection, int wh) {
        if (mode != -1) {
            final NPCConversationManager cm = cms.get(c);
            if (cm == null || cm.getLastMsg() > -1) {
                return;
            }
            final Lock lock = c.getNPCLock();
            lock.lock();
            try {
                if (cm.pendingDisposal) {
                    dispose(c);
                } else if (wh == 0) {
                    cm.getIv().invokeFunction("action", mode, type, selection);
                } else {
                    cm.getIv().invokeFunction("action", mode, type, selection, wh);
                }
            } catch (final Exception e) {
                if (c.getPlayer().isGM()) {
                    c.getPlayer().dropMessage(6, "[错误提示] NPC：" + cm.getNpc() + "_" + wh + "脚本错误,请将下面信息截图给ZEV \n" + e + "");
                }
                System.err.println("[错误提示] NPC： " + cm.getNpc() + "_" + wh + ":" + e);
                dispose(c);
                FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing NPC script, NPC ID : " + cm.getNpc() + "_" + wh + "." + e);
            } finally {
                lock.unlock();
            }
        }
    }

    public final void startQuest(final MapleClient c, final int npc, final int quest) {
        if (!MapleQuest.getInstance(quest).canStart(c.getPlayer(), null)) {
            return;
        }
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (!cms.containsKey(c)) {
                final Invocable iv = getInvocable("quest/" + quest + ".js", c, true);
                if (iv == null) {
                    dispose(c);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, quest, (byte) 0, iv, 0);
                cms.put(c, cm);
                scriptengine.put("qm", cm);
                c.getPlayer().setConversation(1);
                if (gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0 && c.getPlayer().isGM()) {
                    c.getPlayer().dropMessage(5, "对话任务:" + quest + "");
                }
                iv.invokeFunction("start", (byte) 1, (byte) 0, 0);
            } else {
                dispose(c);
            }
        } catch (final Exception e) {
            System.err.println("" + quest + " : " + npc + ":" + e);
            //FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Quest script. (" + quest + ")..NPCID: " + npc + ":" + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void startQuest(final MapleClient c, final byte mode, final byte type, final int selection) {
        final Lock lock = c.getNPCLock();
        final NPCConversationManager cm = cms.get(c);
        if (cm == null || cm.getLastMsg() > -1) {
            return;
        }
        lock.lock();
        try {
            if (cm.pendingDisposal) {
                dispose(c);
            } else {
                cm.getIv().invokeFunction("start", mode, type, selection);
            }
        } catch (Exception e) {
            if (gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0 && c.getPlayer().isGM()) {
                c.getPlayer().dropMessage("任务脚本错误:" + cm.getQuest() + "错误NPC: " + cm.getNpc() + ":" + e);
            }
            System.err.println("Error executing Quest script. (" + cm.getQuest() + ")...NPC: " + cm.getNpc() + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Quest script. (" + cm.getQuest() + ")..NPCID: " + cm.getNpc() + ":" + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void endQuest(final MapleClient c, final int npc, final int quest, final boolean customEnd) {
        if (!customEnd && !MapleQuest.getInstance(quest).canComplete(c.getPlayer(), null)) {
            return;
        }
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (!cms.containsKey(c)) {
                final Invocable iv = getInvocable("quest/" + quest + ".js", c, true);
                if (iv == null) {
                    dispose(c);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, quest, (byte) 1, iv, 0);
                cms.put(c, cm);
                scriptengine.put("qm", cm);
                c.getPlayer().setConversation(1);
                iv.invokeFunction("end", (byte) 1, (byte) 0, 0); // start it off as something
            } else {
            }
        } catch (Exception e) {
            if (gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0 && c.getPlayer().isGM()) {
                c.getPlayer().dropMessage("任务脚本错误:" + quest + " 错误NPC: " + quest + ":" + e);
            }
            System.err.println("Error executing Quest script. (" + quest + ")..NPCID: " + npc + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Quest script. (" + quest + ")..NPCID: " + npc + ":" + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void endQuest(final MapleClient c, final byte mode, final byte type, final int selection) {
        final Lock lock = c.getNPCLock();
        final NPCConversationManager cm = cms.get(c);
        if (cm == null || cm.getLastMsg() > -1) {
            return;
        }
        lock.lock();
        try {
            if (cm.pendingDisposal) {
                dispose(c);
            } else {
                cm.getIv().invokeFunction("end", mode, type, selection);
            }
        } catch (Exception e) {
            if (gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0 && c.getPlayer().isGM()) {
                c.getPlayer().dropMessage("任务脚本错误:" + cm.getQuest() + "错误NPC: " + cm.getNpc() + ":" + e);
            }
            System.err.println("Error executing Quest script. (" + cm.getQuest() + ")...NPC: " + cm.getNpc() + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing Quest script. (" + cm.getQuest() + ")..NPCID: " + cm.getNpc() + ":" + e);
            dispose(c);
        } finally {
            lock.unlock();
        }
    }

    public final void dispose(final MapleClient c) {
        final NPCConversationManager npccm = cms.get(c);
        PNPC itemjc2 = new PNPC();
        String items2 = itemjc2.getPM();
        if (npccm != null) {
            cms.remove(c);
            if (npccm.getType() == -1) {
                if (传送相关(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/传送相关/" + npccm.getNpc() + ".js");
                } else if (传送相关(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/传送相关/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (英语村(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/副本-英语村/" + npccm.getNpc() + ".js");
                } else if (英语村(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/副本-英语村/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (废弃都市(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/副本-废弃都市/" + npccm.getNpc() + ".js");
                } else if (废弃都市(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/副本-废弃都市/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (玩具塔(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/副本-玩具塔/" + npccm.getNpc() + ".js");
                } else if (玩具塔(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/副本-玩具塔/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (女神塔(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/副本-女神塔/" + npccm.getNpc() + ".js");
                } else if (女神塔(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/副本-女神塔/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (月妙(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/副本-月妙/" + npccm.getNpc() + ".js");
                } else if (月妙(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/副本-月妙/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (海盗船(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/副本-海盗船/" + npccm.getNpc() + ".js");
                } else if (海盗船(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/副本-海盗船/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (毒雾森林(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/副本-毒雾森林/" + npccm.getNpc() + ".js");
                } else if (毒雾森林(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/副本-毒雾森林/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (罗密欧与朱丽叶(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/副本-罗密欧与朱丽叶/" + npccm.getNpc() + ".js");
                } else if (罗密欧与朱丽叶(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/副本-罗密欧与朱丽叶/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (每日任务(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/每日任务/" + npccm.getNpc() + ".js");
                } else if (每日任务(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/每日任务/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (冒险百科(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/冒险百科/" + npccm.getNpc() + ".js");
                } else if (冒险百科(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/冒险百科/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (礼包NPC(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/礼包相关/" + npccm.getNpc() + ".js");
                } else if (礼包NPC(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/礼包相关/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (zevms(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/zevms/" + npccm.getNpc() + ".js");
                } else if (zevms(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/zevms/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (飞天猪(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/飞天猪/" + npccm.getNpc() + ".js");
                } else if (飞天猪(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/飞天猪/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (消耗箱子(npccm.getNpc()) && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/消耗箱子/" + npccm.getNpc() + ".js");
                } else if (消耗箱子(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/消耗箱子/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (活动相关(npccm.getNpc()) && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/活动奖励/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (items2.contains("," + npccm.getNpc() + ",") && npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/pnpc/" + npccm.getNpc() + ".js");
                } else if (items2.contains("," + npccm.getNpc() + ",") && npccm.getwh() > 0) {
                    c.removeScriptEngine("scripts/pnpc/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                } else if (npccm.getwh() == 0) {
                    c.removeScriptEngine("scripts/npc/" + npccm.getNpc() + ".js");
                } else {
                    c.removeScriptEngine("scripts/npc/" + npccm.getNpc() + "_" + npccm.getwh() + ".js");
                }
                //c.removeScriptEngine("scripts/npc/notcoded.js");
            } else {
                c.removeScriptEngine("scripts/quest/" + npccm.getQuest() + ".js");
            }
        }
        if (c.getPlayer() != null && c.getPlayer().getConversation() == 1) {
            c.getPlayer().setConversation(0);
        }
    }

    public final NPCConversationManager getCM(final MapleClient c) {
        return cms.get(c);
    }

}
