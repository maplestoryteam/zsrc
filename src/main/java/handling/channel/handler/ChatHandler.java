/*
玩家说话，聊天
 */
package handling.channel.handler;

import client.MapleClient;
import client.MapleCharacter;
import client.messages.CommandProcessor;
import constants.ServerConstants.CommandType;
import static gui.QQ通信.群通知;
import gui.活动推雪球比赛;
import handling.channel.ChannelServer;
import handling.world.MapleMessenger;
import handling.world.MapleMessengerCharacter;
import handling.world.MapleParty;
import handling.world.World;
import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import server.Timer;
import server.custom.bossrank4.BossRankManager4;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import static tools.FileoutputUtil.CurrentReadable_Time;
import tools.MaplePacketCreator;
import tools.data.LittleEndianAccessor;

public class ChatHandler {

    public static final void GeneralChat(final String text, final byte unk, final MapleClient c, final MapleCharacter chr) {
        if (chr != null) {

            try {
                boolean condition = CommandProcessor.processCommand(c, text, CommandType.玩家指令);
                if (condition) {
                    return;
                }
            } catch (Throwable e) {
                System.err.println(e);
            }
            if (gui.Start.ConfigValuesMap.get("玩家聊天开关") > 0) {
                c.sendPacket(MaplePacketCreator.serverNotice(1, "管理员从后台关闭了聊天功能"));
                return;
            }

            if (!chr.isGM() && text.length() >= 80) {
                return;
            }
            if (gui.Start.ConfigValuesMap.get("仙人模式开关") != null) {
                if (c.getPlayer().getLevel() >= 160) {
                    if ("开启仙人模式".equals(text)) {
                        chr.仙人模式();
                    } else if ("关闭仙人模式".equals(text)) {
                        chr.关闭仙人模式();
                    }
                }
            } else {
                chr.dropMessage(5, "暂时无法查到你的仙人模式数据，请等待下次更新维护后。");
            }
            int 时 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int 分 = Calendar.getInstance().get(Calendar.MINUTE);
            int 星期 = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if (MapleParty.活动安排 == 0) {
                if (星期 == 1 || 星期 == 7) {
                    if (时 >= 10) {
                        if ("我要玩雪球赛".equals(text) && chr.Getcharacterz("" + chr.getId() + "", 10) <= 0) {
                            MapleParty.雪球赛++;
                            chr.Gaincharacterz("" + chr.getId() + "", 10, 1);
                            chr.dropMessage(1, "[活动投票] \r\n投票雪球赛成功");
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[自助活动] : " + chr.getName() + " 表示想玩雪球赛。"));
                            群通知("[自助活动] : " + chr.getName() + " 表示想玩雪球赛。");
                        }
                    }
                    if (MapleParty.雪球赛 >= gui.Start.ConfigValuesMap.get("响应人数设置")) {
                        活动推雪球比赛.开始推雪球();
                    }
                } else if (星期 > 1 && 星期 < 7) {
                    if (时 >= 17) {
                        if ("我要玩雪球赛".equals(text) && chr.Getcharacterz("" + chr.getId() + "", 10) <= 0) {
                            MapleParty.雪球赛++;
                            chr.Gaincharacterz("" + chr.getId() + "", 10, 1);
                            chr.dropMessage(1, "[活动投票] \r\n投票雪球赛成功");
                            World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, "[自助活动] : " + chr.getName() + " 表示想玩雪球赛。"));
                            群通知("[自助活动] : " + chr.getName() + " 表示想玩雪球赛。");
                        }
                    }
                    if (MapleParty.雪球赛 >= gui.Start.ConfigValuesMap.get("响应人数设置")) {
                        活动推雪球比赛.开始推雪球();
                    }
                }
            }
            if (chr.getBossLog("玩家上线提示") == 0) {
                if ("我知道了233".equals(text)) {
                    chr.setBossLog("玩家上线提示");
                }
            }
            if (chr.getBossLog("管理上线提示") == 0) {
                if ("老子知道了233".equals(text)) {
                    chr.setBossLog("管理上线提示");
                }
            }
            //彩旦
            if ("我爱冒险岛".equals(text)) {
                if (chr.getBossLog("彩旦2") == 0 && c.getPlayer().getMap().getCharactersSize() > 1) {
                    chr.modifyCSPoints(1, 50, true);
                    chr.modifyCSPoints(2, 50, true);
                    chr.setBossLog("彩旦2");
                    chr.dropMessage(1, "[小彩旦] : \r\n恭喜你触发彩旦，获得 50 点券/抵用券。");
                }
            }
            //彩旦
            if ("买了否冷".equals(text) && c.getPlayer().getMap().getCharactersSize() > 1) {
                if (chr.getBossLog("彩旦5") == 0) {
                    MapleParty.买了否冷++;
                    chr.setBossLog("彩旦5");
                }
            }
            if ("why".equals(text) && c.getPlayer().getMap().getCharactersSize() > 1) {
                if (chr.getBossLog("彩旦5") == 1) {
                    MapleParty.买了否冷 -= 1;
                    chr.setBossLog("彩旦5");
                    chr.modifyCSPoints(1, 66, true);
                    chr.modifyCSPoints(2, 66, true);
                    chr.dropMessage(1, "[小彩旦] : \r\n恭喜你触发彩旦，获得 66 点券/抵用券。");
                }
            }
            if (chr.isHidden()) {
                chr.getMap().broadcastGMMessage(chr, MaplePacketCreator.getChatText(chr.getId(), text, c.getPlayer().isGM(), unk), true);
            } else {
                chr.getCheatTracker().checkMsg();
                chr.getMap().broadcastMessage(MaplePacketCreator.getChatText(chr.getId(), text, c.getPlayer().isGM(), unk), c.getPlayer().getPosition());
                if (gui.Start.ConfigValuesMap.get("聊天记录开关") <= 0) {
                    FileoutputUtil.logToFile("服务端记录信息/玩家档案" + c.getPlayer().getName() + "/聊天记录.txt", "[时间: " + CurrentReadable_Time() + " ]内容: " + text + "\r\n");
                }
            }
            BossRankManager4.getInstance().setLog(chr.getId(), chr.getName(), "唠叨经验", (byte) 2, (byte) 1);
        }
    }

    public static final void Others(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final int type = slea.readByte();
        final byte numRecipients = slea.readByte();
        int recipients[] = new int[numRecipients];

        for (byte i = 0; i < numRecipients; i++) {
            recipients[i] = slea.readInt();
        }
        final String chattext = slea.readMapleAsciiString();
        if (chr == null || !chr.getCanTalk()) {
            c.sendPacket(MaplePacketCreator.serverNotice(6, "你已经被禁言，因此无法说话."));
            return;
        }
        if (CommandProcessor.processCommand(c, chattext, CommandType.玩家指令)) {
            return;
        }
        chr.getCheatTracker().checkMsg();
        switch (type) {
            case 0:
                World.Buddy.buddyChat(recipients, chr.getId(), chr.getName(), chattext);
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“buddyChat”" + chattext);
                }
                break;
            //组队说话
            case 1:
                if (chr.getParty() == null) {
                    break;
                }
                World.Party.partyChat(chr.getParty().getId(), chattext, chr.getName());
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“partyChat”1：" + chattext + "2：" + chr.getName());
                }
                break;
            case 2:
                if (chr.getGuildId() <= 0) {
                    break;
                }
                World.Guild.guildChat(chr.getGuildId(), chr.getName(), chr.getId(), chattext);
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“guildChat”1：" + chr.getGuildId() + "2：" + chr.getName() + "3：" + chr.getId() + "4：" + chattext);
                }
                break;
            //家族对话
            case 3:
                if (chr.getGuildId() <= 0) {
                    break;
                }
                World.Alliance.allianceChat(chr.getGuildId(), chr.getName(), chr.getId(), chattext);
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“allianceChat”1：" + chr.getGuildId() + "2：" + chr.getName() + "3：" + chr.getId() + "4：" + chattext);
                }
                break;
        }
    }

    public static final void Messenger(final LittleEndianAccessor slea, final MapleClient c) {
        String input;
        MapleMessenger messenger = c.getPlayer().getMessenger();

        switch (slea.readByte()) {
            case 0x00: // open
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“Messenger”-0x00");
                }
                if (messenger == null) {
                    int messengerid = slea.readInt();
                    if (messengerid == 0) { // create
                        c.getPlayer().setMessenger(World.Messenger.createMessenger(new MapleMessengerCharacter(c.getPlayer())));
                    } else { // join
                        messenger = World.Messenger.getMessenger(messengerid);
                        if (messenger != null) {
                            final int position = messenger.getLowestPosition();
                            if (position > -1 && position < 4) {
                                c.getPlayer().setMessenger(messenger);
                                World.Messenger.joinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()), c.getPlayer().getName(), c.getChannel());
                            }
                        }
                    }
                }
                break;
            case 0x02: // exit
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“Messenger”-0x02");
                }
                if (messenger != null) {
                    final MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(c.getPlayer());
                    World.Messenger.leaveMessenger(messenger.getId(), messengerplayer);
                    c.getPlayer().setMessenger(null);
                }
                break;
            case 0x03: // invite
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“Messenger”-0x03");
                }
                if (messenger != null) {
                    final int position = messenger.getLowestPosition();
                    if (position <= -1 || position >= 4) {
                        return;
                    }
                    input = slea.readMapleAsciiString();
                    final MapleCharacter target = c.getChannelServer().getPlayerStorage().getCharacterByName(input);

                    if (target != null) {
                        if (target.getMessenger() == null) {
                            if (!target.isGM() || c.getPlayer().isGM()) {
                                c.sendPacket(MaplePacketCreator.messengerNote(input, 4, 1));
                                target.getClient().sendPacket(MaplePacketCreator.messengerInvite(c.getPlayer().getName(), messenger.getId()));
                            } else {
                                c.sendPacket(MaplePacketCreator.messengerNote(input, 4, 0));
                            }
                        } else {
                            c.sendPacket(MaplePacketCreator.messengerChat(c.getPlayer().getName() + " : " + target.getName() + "已经是使用枫叶信使."));
                        }
                    } else if (World.isConnected(input)) {
                        World.Messenger.messengerInvite(c.getPlayer().getName(), messenger.getId(), input, c.getChannel(), c.getPlayer().isGM());
                    } else {
                        c.sendPacket(MaplePacketCreator.messengerNote(input, 4, 0));
                    }
                }
                break;
            case 0x05: // decline
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“Messenger”-0x05");
                }
                final String targeted = slea.readMapleAsciiString();
                final MapleCharacter target = c.getChannelServer().getPlayerStorage().getCharacterByName(targeted);
                if (target != null) { // This channel
                    if (target.getMessenger() != null) {
                        target.getClient().sendPacket(MaplePacketCreator.messengerNote(c.getPlayer().getName(), 5, 0));
                    }
                } else // Other channel
                 if (!c.getPlayer().isGM()) {
                        World.Messenger.declineChat(targeted, c.getPlayer().getName());
                    }
                break;
            case 0x06: // message
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“Messenger”-0x06");
                }
                if (messenger != null) {
                    World.Messenger.messengerChat(messenger.getId(), slea.readMapleAsciiString(), c.getPlayer().getName());

                }
                break;
        }
    }

    public static final void Whisper_Find(final LittleEndianAccessor slea, final MapleClient c) {
        final byte mode = slea.readByte();
//        slea.readInt(); //ticks
        switch (mode) {
            case 68: //buddy
            case 5: { // Find
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“Whisper_Find”-5/68");
                }
                if (gui.Start.ConfigValuesMap.get("游戏找人开关") > 0) {
                    c.getPlayer().dropMessage(5, "找人功能被关闭");
                    return;
                }
                final String recipient = slea.readMapleAsciiString();
                MapleCharacter player = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient);
                if (player != null) {
                    if (!player.isGM() || c.getPlayer().isGM() && player.isGM()) {

                        c.sendPacket(MaplePacketCreator.getFindReplyWithMap(player.getName(), player.getMap().getId(), mode == 68));
                    } else {
                        c.sendPacket(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                    }
                } else { // Not found
                    int ch = World.Find.findChannel(recipient);
                    if (ch > 0) {
                        player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(recipient);
                        if (player == null) {
                            break;
                        }
                        if (player != null) {
                            if (!player.isGM() || (c.getPlayer().isGM() && player.isGM())) {
                                c.sendPacket(MaplePacketCreator.getFindReply(recipient, (byte) ch, mode == 68));
                            } else {
                                c.sendPacket(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                            }
                            return;
                        }
                    }
                    if (ch == -10) {
                        c.sendPacket(MaplePacketCreator.getFindReplyWithCS(recipient, mode == 68));
                    } else if (ch == -20) {
                        c.sendPacket(MaplePacketCreator.getFindReplyWithMTS(recipient, mode == 68));
                    } else {
                        c.sendPacket(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                    }
                }
                break;
            }
            case 6: { // Whisper
                if (c.getPlayer().isAdmin() && gui.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    c.getPlayer().dropMessage(5, "handling.channel.handler“Whisper_Find”-6");
                }
                if (!c.getPlayer().getCanTalk()) {
                    c.sendPacket(MaplePacketCreator.serverNotice(6, "你已经被禁言，因此无法说话."));
                    return;
                }
                c.getPlayer().getCheatTracker().checkMsg();
                final String recipient = slea.readMapleAsciiString();
                final String text = slea.readMapleAsciiString();
                final int ch = World.Find.findChannel(recipient);
                if (ch > 0) {
                    MapleCharacter player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(recipient);
                    if (player == null) {
                        break;
                    }
                    player.getClient().sendPacket(MaplePacketCreator.getWhisper(c.getPlayer().getName(), c.getChannel(), text));
                    if (!c.getPlayer().isGM() && player.isGM()) {
                        c.sendPacket(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                    } else {
                        c.sendPacket(MaplePacketCreator.getWhisperReply(recipient, (byte) 1));
                    }
                } else {
                    c.sendPacket(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                }
            }
            break;
        }
    }

    /*
    2,0,0,0      新手
    -1，-1，-1，1 全职业
    -8,0,0,0    战士全选
    0,31,0,0    魔法师全选
    0，-32,1,0  海盗全选
    0,0,30,0    飞侠全选
    0,0，-32,1  弓箭手全选
    ******************************判断1
    2，新手
    4，战神
    16，勇士
    32，骑士
    64，龙骑士
    -128，魂骑士
    【全选】，-8
    ******************************判断2
    16，炎术士
    8，祭师
    4，冰雷巫师
    2，火毒巫师
    【全选】，31
    判断3
     */
    private static ScheduledFuture<?> 队员搜索线程 = null;
    public static boolean 搜索完毕 = false;
    public static int 搜索次数 = 0;
    public static int 人数 = 0;

    public static void 关闭搜索线程() {
        if (队员搜索线程 != null) {
            队员搜索线程.cancel(true);
            队员搜索线程 = null;
            搜索次数 = 0;
            人数 = 0;
        }
    }

    public static final void PARTY_SS(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        //c.getPlayer().dropMessage(1, "搜索组队测试");slea.toString(true)
        final int 最低等级 = slea.readByte();
        slea.skip(3);
        final int 最高等级 = slea.readByte();
        slea.skip(3);
        final int 队伍人数 = slea.readByte();
        slea.skip(3);
        final int A = slea.readByte();
        final int B = slea.readByte();
        final int C = slea.readByte();
        final int D = slea.readByte();
        int 全职业判断 = A + B + C + D;
        if (全职业判断 != -2) {
            c.getPlayer().dropMessage(1, "请勾选上全职业，在开始搜索。");
            return;
        }
        if (队员搜索线程 != null) {
            c.getPlayer().dropMessage(1, "正在搜索中。");
            return;
        }
        队员搜索线程 = Timer.BuffTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                搜索次数++;
                搜索完毕 = false;
                c.getPlayer().dropMessage(6, "[寻找组队]:正在第 " + 搜索次数 + " 次寻找 " + chr.getClient().getChannel() + " 频道内等级在 " + 最低等级 + " - " + 最高等级 + " 级，并且没有组队的玩家。");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000 * 8);
                            for (ChannelServer cserv1 : ChannelServer.getAllInstances()) {
                                for (MapleCharacter party : cserv1.getPlayerStorage().getAllCharacters()) {
                                    if (chr.getClient().getChannel() == party.getClient().getChannel()) {
                                        if (party.getParty() == null) {
                                            if (party.getLevel() >= 最低等级 && party.getLevel() <= 最高等级) {
                                                MapleCharacter invited = c.getChannelServer().getPlayerStorage().getCharacterByName(party.getName());
                                                invited.getClient().sendPacket(MaplePacketCreator.partyInvite(c.getPlayer()));
                                                c.getPlayer().dropMessage(5, "[寻找组队]:找到玩家 ( " + party.getName() + " ) 所在地图 ( " + party.getMap().getMapName().toString() + " )");
                                                搜索完毕 = true;
                                                人数++;
                                            }
                                        }
                                    }
                                }
                            }
                            if (搜索完毕 == true) {
                                c.getPlayer().dropMessage(1, "搜索完毕找到 " + 人数 + " 个符合等级条件玩家，并且已经向他/她们发生了组队邀请。");
                                关闭搜索线程();
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                }.start();
            }
        }, 10 * 1000);
    }

    public static final void MAPLETV(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final int 养殖物品 = slea.readByte();
        final int B = slea.readByte();
        final int C = slea.readByte();
        final int D = slea.readByte();
        final int F = slea.readByte();
        switch (养殖物品) {

            case -9://04220153
                MapleQuest.getInstance(21763).forceStart(c.getPlayer(), 0, null);
                break;
            case -71://鸡蛋4220089
                MapleQuest.getInstance(8252).forceStart(c.getPlayer(), 0, null);
                break;
            case -35://金鸡蛋4220125
                MapleQuest.getInstance(8252).forceStart(c.getPlayer(), 0, null);
                break;
            case -92://鱼缸 4220068
                MapleQuest.getInstance(8588).forceStart(c.getPlayer(), 0, null);
                break;
            case -115:// 04220045
                MapleQuest.getInstance(7691).forceStart(c.getPlayer(), 0, null);
                break;
            case 117://鬼娃娃 04220020
                MapleQuest.getInstance(1).forceStart(c.getPlayer(), 0, null);
                break;

            default:
                break;
        }
        c.getPlayer().dropMessage(6, "-------------------------------");
        c.getPlayer().dropMessage(6, "B  " + 养殖物品);
//        c.getPlayer().dropMessage(6, "C  " + C);
//        c.getPlayer().dropMessage(6, "D  " + D);
//        c.getPlayer().dropMessage(6, "F  " + F);
        c.getPlayer().dropMessage(6, "" + slea.toString(true));

    }

}
