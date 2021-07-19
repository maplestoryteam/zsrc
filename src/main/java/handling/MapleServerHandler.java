/*
大区
 */
package handling;

import client.MapleClient;
import constants.ServerConstants;
import handling.cashshop.CashShopServer;
import handling.cashshop.handler.CashShopOperation;
import handling.cashshop.handler.MTSOperation;
import handling.channel.ChannelServer;
import handling.channel.handler.*;
import handling.login.LoginServer;
import handling.login.handler.CharLoginHandler;
import handling.netty.MaplePacketDecoder;
import handling.world.MapleParty;
import handling.world.World;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import server.MTSStorage;
import server.Randomizer;
import server.ServerProperties;
import tools.FileoutputUtil;
import tools.MapleAESOFB;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;
import tools.packet.LoginPacket;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static abc.Game.检测客户端版本1;
import static tools.FileoutputUtil.CurrentReadable_Date;
import static tools.FileoutputUtil.CurrentReadable_Time;

public class MapleServerHandler extends ChannelDuplexHandler {

    public static final boolean Log_Packets = true;
    private final int channel;
    private final List<String> BlockedIP = new ArrayList<>();
    private final Map<String, Pair<Long, Byte>> tracker = new ConcurrentHashMap<>();
    //Screw locking. Doesn't matter.
    //private static final ReentrantReadWriteLock IPLoggingLock = new ReentrantReadWriteLock();
    private static final String nl = System.getProperty("line.separator");
    private static final HashMap<String, FileWriter> logIPMap = new HashMap<>();
    private static final boolean debugMode = Boolean.parseBoolean(ServerProperties.getProperty("mxmxd.调试模式", "false"));
    //Note to Zero: Use an enumset. Don't iterate through an array.
    private static final EnumSet<RecvPacketOpcode> blocked = EnumSet.noneOf(RecvPacketOpcode.class);

    static {
        RecvPacketOpcode[] block = new RecvPacketOpcode[]{
            RecvPacketOpcode.NPC_ACTION,
            RecvPacketOpcode.MOVE_PLAYER,
            RecvPacketOpcode.MOVE_PET,
            RecvPacketOpcode.MOVE_SUMMON,
            RecvPacketOpcode.MOVE_DRAGON,
            RecvPacketOpcode.MOVE_LIFE,
            RecvPacketOpcode.HEAL_OVER_TIME,
            RecvPacketOpcode.STRANGE_DATA};
        blocked.addAll(Arrays.asList(block));
    }

    //如果IP被记录，则返回文件写入器。否则无效。
    private static FileWriter isLoggedIP(Channel sess) {
        String a = sess.remoteAddress().toString();
        String realIP = a.substring(a.indexOf('/') + 1, a.indexOf(':'));
        return logIPMap.get(realIP);
    }
// <editor-fold defaultstate="collapsed" desc="Packet Log Implementation">
    private static final int Log_Size = 10000;
    private static final ArrayList<LoggedPacket> Packet_Log = new ArrayList<LoggedPacket>(Log_Size);
    private static final ReentrantReadWriteLock Packet_Log_Lock = new ReentrantReadWriteLock();
    private static final File Packet_Log_Output = new File("PacketLog.txt");

    public static void log(LittleEndianAccessor packet, RecvPacketOpcode op, MapleClient c, Channel io) {
        if (blocked.contains(op)) {
            return;
        }
        try {
            Packet_Log_Lock.writeLock().lock();
            LoggedPacket logged = null;
            if (Packet_Log.size() == Log_Size) {
                logged = Packet_Log.remove(0);
            }
            //This way, we don't create new LoggedPacket objects, we reuse them =]
            if (logged == null) {
                logged = new LoggedPacket(packet, op, io.remoteAddress().toString(),
                        c == null ? -1 : c.getAccID(),
                        c == null || c.getAccountName() == null ? "[Null]" : c.getAccountName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getName() == null ? "[Null]" : c.getPlayer().getName());
            } else {
                logged.setInfo(packet, op, io.remoteAddress().toString(),
                        c == null ? -1 : c.getAccID(),
                        c == null || c.getAccountName() == null ? "[Null]" : c.getAccountName(),
                        c == null || c.getPlayer() == null || c.getPlayer().getName() == null ? "[Null]" : c.getPlayer().getName());
            }
            Packet_Log.add(logged);
        } finally {
            Packet_Log_Lock.writeLock().unlock();
        }
    }

    private static class LoggedPacket {

        private static final String nl = System.getProperty("line.separator");
        private String ip, accName, accId, chrName;
        private LittleEndianAccessor packet;
        private long timestamp;
        private RecvPacketOpcode op;

        public LoggedPacket(LittleEndianAccessor p, RecvPacketOpcode op, String ip, int id, String accName, String chrName) {
            setInfo(p, op, ip, id, accName, chrName);
        }

        public final void setInfo(LittleEndianAccessor p, RecvPacketOpcode op, String ip, int id, String accName, String chrName) {
            this.ip = ip;
            this.op = op;
            packet = p;
            this.accName = accName;
            this.chrName = chrName;
            timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[IP: ").append(ip).append("] [").append(accId).append('|').append(accName).append('|').append(chrName).append("] [Time: ").append(timestamp).append(']');
            sb.append(nl);
            sb.append("[Op: ").append(op.toString()).append(']');
            sb.append(" [Data: ").append(packet.toString()).append(']');
            return sb.toString();
        }
    }

    public void writeLog() {
        try {
            FileWriter fw = new FileWriter(Packet_Log_Output, true);
            try {
                Packet_Log_Lock.readLock().lock();
                String nl = System.getProperty("line.separator");
                for (LoggedPacket loggedPacket : Packet_Log) {
                    fw.write(loggedPacket.toString());
                    fw.write(nl);
                }
                fw.flush();
                fw.close();
            } finally {
                Packet_Log_Lock.readLock().unlock();
            }
        } catch (IOException ex) {
            System.out.println("Error writing log to file.");
        }
    }

    // </editor-fold>

    public MapleServerHandler(int channel) {
        this.channel = channel;
    }

//    @Override
//    public void messageSent(final IoSession session, final Object message) throws Exception {
////        if (MapleParty.调试3 > 0) {
////            System.out.println("messageSent");
////        }
//        final Runnable r = ((MaplePacket) message).getOnSend();
//        if (r != null) {
//            r.run();
//        }
//        super.messageSent(session, message);
//    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (MapleParty.调试3 > 0) {
            System.out.println("exceptionCaught");
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {

        // Start of IP checking
        final String address = ctx.channel().remoteAddress().toString().split(":")[0];
        if (BlockedIP.contains(address)) {
//            System.out.print("自动断开连接A");
//            session.close();
            return;
        }
        final Pair<Long, Byte> track = tracker.get(address);
        byte count;
        if (track == null) {
            count = 1;
        } else {
            count = track.right;
            final long difference = System.currentTimeMillis() - track.left;
            if (difference < 2000) { // Less than 2 sec
                count++;
            } else if (difference > 20000) { // Over 20 sec
                count = 1;
            }
            if (count >= 10) {
                //System.out.print("自动断开连接A2");
                BlockedIP.add(address);
                tracker.remove(address); // Cleanup
                ctx.channel().close();
                return;
            }
        }
        tracker.put(address, new Pair<>(System.currentTimeMillis(), count));
        String IP = address.substring(address.indexOf('/') + 1);
        if (channel > -1) {
            if (ChannelServer.getInstance(channel).isShutdown()) {
                //System.out.print("自动断开连接B");
                ctx.channel().close();
                return;
            }
            if (!LoginServer.containsIPAuth(IP)) {
//                System.out.print("自动断开连接C");
//                session.close();
//                return;
            }
        } else if (channel == -10) {
            if (CashShopServer.isShutdown()) {
                //System.out.print("自动断开连接D");
                ctx.channel().close();
                return;
            }
        } else if (LoginServer.isShutdown()) {
            //System.out.print("自动断开连接E");
            ctx.channel().close();
            return;
        }
        if (MapleParty.服务端启动中 == 0) {
            ctx.channel().close();
            return;
        }
        final byte[] serverRecv = new byte[]{70, 114, 12, (byte) Randomizer.nextInt(255)};
        final byte[] serverSend = new byte[]{82, 48, 120, (byte) Randomizer.nextInt(255)};
        final byte[] ivRecv = ServerConstants.Use_Fixed_IV ? new byte[]{9, 0, 0x5, 0x5F} : serverRecv;
        final byte[] ivSend = ServerConstants.Use_Fixed_IV ? new byte[]{1, 0x5F, 4, 0x3F} : serverSend;

        final MapleClient client = new MapleClient(new MapleAESOFB(ivSend, (short) (0xFFFF - 检测客户端版本1)), new MapleAESOFB(ivRecv, 检测客户端版本1), ctx.channel());
        client.setChannel(channel);

        MaplePacketDecoder.DecoderState decoderState = new MaplePacketDecoder.DecoderState();
        ctx.channel().attr(MaplePacketDecoder.DECODER_STATE_KEY).set(decoderState);
        ctx.writeAndFlush(LoginPacket.getHello(检测客户端版本1, ServerConstants.Use_Fixed_IV ? serverSend : ivSend, ServerConstants.Use_Fixed_IV ? serverRecv : ivRecv));
        ctx.channel().attr(MapleClient.CLIENT_KEY).set(client);
        //读数空闲,心跳包
        //ctx.channel().attr(IdleState.READER_IDLE).set(60);
        //写入空闲
        //ctx.channel().attr(WRITER_IDLE).set(60);
        //频道
        //System.out.println(channel);
        //登陆账号的界面
        if (channel == -1 && channel != -10) {
            //显示欢迎弹窗
            if (gui.Start.ConfigValuesMap.get("欢迎弹窗开关") <= 0) {
                ctx.channel().write(MaplePacketCreator.serverNotice(1, "欢迎 (*^__^*) 来到\r\n\r\n< " + MapleParty.开服名字 + " >"));
            }
            //输出IP地址
            System.out.println("[服务端]" + CurrentReadable_Time() + " : IP地址（" + ctx.channel().remoteAddress().toString() + "）访问服务器 √");
        }
        World.Client.addClient(client);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        if (MapleParty.调试3 > 0) {
            System.out.println("sessionClosed");
        }
       MapleClient client = ctx.channel().attr(MapleClient.CLIENT_KEY).get();

        if (client != null) {
            try {
//                FileWriter fw = isLoggedIP(session);
//                if (fw != null) {
//                    fw.write("=== Session Closed ===");
//                    fw.write(nl);
//                    fw.flush();
//                }
                client.disconnect(true, channel == -10);
            } finally {
                World.Client.removeClient(client);
                ctx.close();
                ctx.channel().attr(MapleClient.CLIENT_KEY).remove();
            }
        }
        super.channelInactive(ctx);
    }

    @Override
    //玩家操作
    public void channelRead(final ChannelHandlerContext ctx, final Object message) {
        try {

            final LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream((byte[]) message));
            if (slea.available() < 2) {
                return;
            }

            final short header_num = slea.readShort();
            // Console output part
            for (final RecvPacketOpcode recv : RecvPacketOpcode.values()) {
                if (recv.getValue() == header_num) {
                    if (debugMode && !RecvPacketOpcode.isSpamHeader(recv)) {
                        final StringBuilder sb = new StringBuilder("接收数据 - 已处理: " + recv + "\n");
                        //sb.append(tools.HexTool.toString((byte[]) message)).append("\n").append(tools.HexTool.toStringFromAscii((byte[]) message));
                        System.out.println("b" + sb);
                    }
                    final MapleClient c = ctx.channel().attr(MapleClient.CLIENT_KEY).get();
                    if (!c.isReceiving()) {
                        return;
                    }
                    if (recv.NeedsChecking()) {
                        if (!c.isLoggedIn()) {
                            return;
                        }
                    }
                    if (c.getPlayer() != null && c.isMonitored()) {
                        if (!blocked.contains(recv)) {
                            FileoutputUtil.log("log\\Monitored\\" + c.getPlayer().getName() + ".log", recv + " (" + Integer.toHexString(header_num) + ") Handled: \r\n" + slea + "\r\n");
                        }
                    }
                    if (Log_Packets) {
                        log(slea, recv, c, ctx.channel());
                    }
//                    System.out.println("recv：" + recv);
//                    System.out.println("slea：" + slea);
//                    System.out.println("c：" + c);
//                    System.out.println("cs：" + cs);
                    handlePacket(recv, slea, c, channel == -10);

                    //Log after the packet is handle. You'll see why =]
//                    FileWriter fw = isLoggedIP(session);
//                    if (fw != null && !blocked.contains(recv)) {
//                        if (recv == RecvPacketOpcode.PLAYER_LOGGEDIN && c != null) { // << This is why. Win.
//                            fw.write(">> [AccountName: "
//                                    + (c.getAccountName() == null ? "null" : c.getAccountName())
//                                    + "] | [IGN: "
//                                    + (c.getPlayer() == null || c.getPlayer().getName() == null ? "null" : c.getPlayer().getName())
//                                    + "] | [Time: "
//                                    + FileoutputUtil.CurrentReadable_Time()
//                                    + "]");
//                            fw.write(nl);
//                        }
//                        fw.write("[" + recv.toString() + "]" + slea.toString(true));
//                        fw.write(nl);
//                        fw.flush();
//                    }
                    return;
                }
            }
            if (debugMode) {
                final StringBuilder sb = new StringBuilder("已接收数据 - 未處理: ");
                sb.append(tools.HexTool.toString((byte[]) message)).append("\n").append(tools.HexTool.toStringFromAscii((byte[]) message));
                System.out.println("r" + sb);
            }
        } catch (RejectedExecutionException ex) {
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
            e.printStackTrace();
        }

    }

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object status) throws Exception {
        if (MapleParty.调试3 > 0) {
            System.out.println("sessionIdle");
        }
        MapleClient client = ctx.channel().attr(MapleClient.CLIENT_KEY).get();
        /**
         * <设置心跳>
         */
        if (client != null) {
            client.sendPing();
        } else {
            ctx.channel().close();
            return;
        }
        super.userEventTriggered(ctx, status);
    }

    public static void handlePacket(final RecvPacketOpcode header, final LittleEndianAccessor slea, final MapleClient c, final boolean cs) throws Exception {

        //System.out.println(header);
        /*System.out.println(slea);*/
        //System.out.println(header);
        switch (header) {
            case PONG:
                c.pongReceived();
                if (MapleParty.调试3 > 0) {
                    System.out.println("PONG");
                }
                break;
            case STRANGE_DATA:
                if (MapleParty.调试3 > 0) {
                    System.out.println("待处理·STRANGE_DATA");
                }
                break;
            case EFFECT_ON_OFF:
            case NEW_SX:
                if (MapleParty.调试3 > 0) {
                    System.out.println("待处理·NEW_SX，EFFECT_ON_OFF");
                }
                break;
            case HELLO_LOGIN://进游戏登陆注册
                CharLoginHandler.Welcome(c);
                //现在没什么事做，HackShield的心跳
                if (MapleParty.调试3 > 0) {
                    System.out.println("HELLO_LOGIN");
                }
                break;
            //进游戏登陆注册
            case HELLO_CHANNEL:
                CharLoginHandler.Welcome(c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("HELLO_CHANNEL");
                }
                break;
            //登陆账号
            case LOGIN_PASSWORD:
                /*try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {
                    System.out.println("线程锁开启失败");
                }*/
                CharLoginHandler.login(slea, c);

                if (MapleParty.调试3 > 0) {
                    System.out.println("登陆账号");
                }
                break;
            //显示频道
            case SERVERLIST_REQUEST:
                CharLoginHandler.ServerListRequest(c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("SERVERLIST_REQUEST");
                }
                break;
            //显示频道
            case LICENSE_REQUEST:
                CharLoginHandler.ServerListRequest(c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("LICENSE_REQUEST");
                }
                break;
            //进入频道
            case CHARLIST_REQUEST:
                CharLoginHandler.CharlistRequest(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("进入频道");
                }
                break;
            //点击频道
            case SERVERSTATUS_REQUEST:
                CharLoginHandler.ServerStatusRequest(c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("点击频道");
                }
                break;
            //角色名字？
            case CHECK_CHAR_NAME:
                CharLoginHandler.CheckCharName(slea.readMapleAsciiString(), c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("创建角色成功");
                }
                break;
            //创建角色
            case CREATE_CHAR:
                CharLoginHandler.CreateChar(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("创建角色");
                }
                break;
            //删除角色
            case DELETE_CHAR:
                //CharLoginHandler.DeleteChar(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("删除角色");
                }
                break;
            //选择频道进入游戏
            case CHAR_SELECT:
                CharLoginHandler.Character_WithoutSecondPassword(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("选择频道进入游戏");
                }
                break;
            //第二_ _密码auth，登陆？
            case AUTH_SECOND_PASSWORD:
                CharLoginHandler.Character_WithSecondPassword(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("AUTH_SECOND_PASSWORD");
                }
                break;
            //以某种方式解决这个问题？？？？
            case RSA_KEY:
                c.sendPacket(LoginPacket.StrangeDATA());
                if (MapleParty.调试3 > 0) {
                    System.out.println("RSA_KEY");
                }
                break;
            // END OF LOGIN SERVER
            //游戏中切换频道
            case CHANGE_CHANNEL://频道
                InterServerHandler.ChangeChannel(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("切换频道");
                }
                break;
            case SET_GENDER://选择频道
                CharLoginHandler.SetGenderRequest(slea, c);
                /* if ( server.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    System.out.println("注册？");
                }*/
                break;
            //链接频道服务器
            case PLAYER_LOGGEDIN://进入游戏服务器
                final int playerid = slea.readInt();
                if (cs) {
                    //商城服务器
                    CashShopOperation.EnterCS(playerid, c);
                } else {
                    //游戏服务器
                    InterServerHandler.Loggedin(playerid, c);
                }
                if (MapleParty.调试3 > 0) {
                    System.out.println("链接频道服务器");
                }
                break;
            //进入游戏后执行`读取/存储角色信息
            case PLAYER_UPDATE:
                PlayerHandler.UpdateHandler(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("读取/存储角色信息");
                }
                break;
            //角色移动
            case MOVE_PLAYER:
                PlayerHandler.MovePlayer(slea, c, c.getPlayer());
                /* if ( server.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    System.out.println("角色移动数据");
                }*/
                break;
            //点击角色
            case CHAR_INFO_REQUEST:
                c.getPlayer().updateTick(slea.readInt());
                PlayerHandler.CharInfoRequest(slea.readInt(), c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("点击角色");
                }

                break;
            //角色攻击
            case CLOSE_RANGE_ATTACK:
                PlayerHandler.closeRangeAttack(slea, c, c.getPlayer(), false);
                if (MapleParty.调试3 > 0) {
                    System.out.println("角色攻击");
                }
                break;
            //远程攻击
            case RANGED_ATTACK:
                PlayerHandler.rangedAttack(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("远程攻击");
                }
                break;
            //近距攻击
            case PASSIVE_ENERGY:
                PlayerHandler.closeRangeAttack(slea, c, c.getPlayer(), true);
                if (MapleParty.调试3 > 0) {
                    System.out.println("近距攻击?");
                }
                break;
            //使用攻击技能
            case MAGIC_ATTACK:
                PlayerHandler.MagicDamage(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("使用攻击技能");
                }
                break;
            //特殊动作,使用BUFF技能
            case SPECIAL_MOVE:
                PlayerHandler.SpecialMove(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("使用BUFF技能");
                }
                break;
            //玩家表情
            case FACE_EXPRESSION://
                PlayerHandler.ChangeEmotion(slea.readInt(), c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("玩家表情");
                }
                break;
            //角色受到伤害
            case TAKE_DAMAGE:
                PlayerHandler.TakeDamage(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("角色受到伤害");
                }
                break;
            //角色恢复
            case HEAL_OVER_TIME:

                PlayerHandler.Heal(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("角色恢复");
                }
                break;
            //取消BUFF
            case CANCEL_BUFF:
                PlayerHandler.CancelBuffHandler(slea.readInt(), c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("鼠标右上角取消BUFF");
                }
                break;
            //物品抵消？
            case CANCEL_ITEM_EFFECT:
                PlayerHandler.CancelItemEffect(slea.readInt(), c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("CANCEL_ITEM_EFFECT");
                }
                break;
            //上椅子
            case USE_CHAIR:
                PlayerHandler.UseChair(slea.readInt(), c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("上椅子");
                }
                break;
            //下椅子
            case CANCEL_CHAIR:
                PlayerHandler.CancelChair(slea.readShort(), c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("下椅子");
                }
                break;
            //原地复活术
            case USE_ITEMEFFECT:
            case WHEEL_OF_FORTUNE:
                PlayerHandler.UseItemEffect(slea.readInt(), c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("原地复活术1、原地复活术2");
                }
                break;
            //打开技能
            case SKILL_EFFECT:
                PlayerHandler.SkillEffect(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("SKILL_EFFECT");//
                }
                break;
            //丢金币
            case MESO_DROP:
                c.getPlayer().updateTick(slea.readInt());
                PlayerHandler.DropMeso(slea.readInt(), c.getPlayer());
                /* if ( server.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    System.out.println("丢金币");
                }*/
                break;
            //怪物书图鉴
            case MONSTER_BOOK_COVER:
                PlayerHandler.ChangeMonsterBookCover(slea.readInt(), c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("怪物书图鉴");
                }
                break;
            //保存键盘快捷键
            case CHANGE_KEYMAP:
                PlayerHandler.ChangeKeymap(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("保存键盘快捷键");
                }
                break;
            //退出商城
            case CHANGE_MAP:
                if (cs) {
                    if (ServerConstants.调试模式) {
                        System.out.println("退出商城");
                    }
                    CashShopOperation.LeaveCS(slea, c, c.getPlayer());
                } else {
                    PlayerHandler.ChangeMap(slea, c, c.getPlayer());
                }
                /* if(调试=="开"){
                     System.out.println("退出商城");
                }*/
                break;
            //地图传送点错误
            case CHANGE_MAP_SPECIAL:
                slea.skip(1);
                PlayerHandler.ChangeMapSpecial(slea, slea.readMapleAsciiString(), c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("地图传送点错误");
                }
                break;
            //传送点
            case USE_INNER_PORTAL:
                slea.skip(1);
                PlayerHandler.InnerPortal(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("传送点");
                }
                break;
            //瞬移石头，添加地图
            case TROCK_ADD_MAP:
                PlayerHandler.TrockAddMap(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("TROCK_ADD_MAP");
                }
                break;
            //战神连击
            case ARAN_COMBO:
                PlayerHandler.AranCombo(c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("ARAN_COMBO");
                }
                break;
            //技能宏
            case SKILL_MACRO:
                PlayerHandler.ChangeSkillMacro(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("技能宏");
                }
                break;
            //背包使用消耗箱子
            case ITEM_BAOWU:
                InventoryHandler.UsePenguinBox(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("使用消耗箱子");
                }
                break;
            //孙子兵法？
            case ITEM_SUNZI:
                InventoryHandler.SunziBF(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("ITEM_SUNZI");
                }
                break;
            //增加人气
            case GIVE_FAME:
                PlayersHandler.GiveFame(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("增加/减少人气");
                }
                break;
            //改变玩家外观
            case TRANSFORM_PLAYER:
                PlayersHandler.TransformPlayer(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("改变玩家外观");
                }
                break;
            //物品。注释？
            case NOTE_ACTION:
                PlayersHandler.Note(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("NOTE_ACTION");
                }
                break;
            //使用_门传送门？
            case USE_DOOR:
                PlayersHandler.UseDoor(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("USE_DOOR");
                }
                break;
            //攻击反应堆
            case DAMAGE_REACTOR:
                PlayersHandler.HitReactor(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("攻击反应堆");
                }
                break;
            //反应堆信息？
            case TOUCH_REACTOR:
                PlayersHandler.TouchReactor(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("TOUCH_REACTOR");
                }
                break;
            //..????????????
            case CLOSE_CHALKBOARD:
                c.getPlayer().setChalkboard(null);
                if (MapleParty.调试3 > 0) {
                    System.out.println("CLOSE_CHALKBOARD");
                }
                break;
            //项目制造商
            case ITEM_MAKER:
                //ItemMakerHandler.ItemMaker(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("ITEM_MAKER");
                }
                break;
            //背包道具整理
            case ITEM_SORT:
                InventoryHandler.ItemSort(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("背包道具整理");
                }
                break;
            //角色5110000巧克力红心
            case ITEM_GATHER:
                InventoryHandler.ItemGather(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("巧克力红心");
                }
                break;
            //角色丢出/使用物品
            case ITEM_MOVE:
                InventoryHandler.ItemMove(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("角色丢出/使用物品");
                }
                break;
            //角色捡起物品
            case ITEM_PICKUP://
                InventoryHandler.Pickup_Player(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("角色捡起物品");
                }
                break;
            //广播喇叭物品，特殊栏？
            case USE_CASH_ITEM:
                InventoryHandler.UseCashItem(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("特殊栏");
                }
                break;
            //消耗抵用券？
            case quest_KJ:
                InventoryHandler.QuestKJ(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("quest_KJ");
                }
                break;
            //使用消耗品
            case USE_ITEM:
                InventoryHandler.UseItem(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("使用消耗品");
                }
                break;
            //放大镜？？？？？
            case USE_MAGNIFY_GLASS:
                InventoryHandler.UseMagnify(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("放大镜");
                }
                break;
            //另一种消耗物品的使用?
            case USE_SCRIPTED_NPC_ITEM:
                InventoryHandler.UseScriptedNPCItem(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("USE_SCRIPTED_NPC_ITEM");
                }
                break;
            //使用什么东西，但是不能在 纯净雪人栖息地
            case USE_RETURN_SCROLL:
                InventoryHandler.UseReturnScroll(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("USE_RETURN_SCROLL");
                }
                break;
            //匠人之魂技能？
            case USE_UPGRADE_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll((byte) slea.readShort(), (byte) slea.readShort(), (byte) slea.readShort(), c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("匠人之魂技能1");
                }
                break;
            //匠人之魂技能？
            case USE_POTENTIAL_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll((byte) slea.readShort(), (byte) slea.readShort(), (byte) 0, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("匠人之魂技能2");
                }
                break;
            //匠人之魂技能？
            case USE_EQUIP_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll((byte) slea.readShort(), (byte) slea.readShort(), (byte) 0, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("匠人之魂技能3");
                }
                break;
            //使用召唤包
            case USE_SUMMON_BAG:
                InventoryHandler.UseSummonBag(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("USE_SUMMON_BAG");
                }
                break;
            //金蛋隐身蛋？
            case USE_TREASUER_CHEST:
                InventoryHandler.UseTreasureChest(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("金蛋银蛋");
                }
                break;
            case ITEM_MZD:
                InventoryHandler.UseTreasureChest(slea, c, c.getPlayer());
                break;
            //使用技能书？
            case USE_SKILL_BOOK:
//                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseSkillBook(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("使用技能书");
                }
                break;
            //对怪物使用的道具物品
            case USE_CATCH_ITEM:
                InventoryHandler.UseCatchItem(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("对怪物使用的道具物品");
                }
                break;
            //恢复宠物疲劳补药，吃？
            case USE_MOUNT_FOOD:
                InventoryHandler.UseMountFood(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("宠物物品？");
                }
                break;
            //保护怪物
            case HYPNOTIZE_DMG:
                MobHandler.HypnotizeDmg(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("HYPNOTIZE_DMG");
                }
                break;
            //护送？？
            case MOB_NODE:
                MobHandler.MobNode(slea, c.getPlayer());
                break;
            //显示节点
            case DISPLAY_NODE:
                MobHandler.DisplayNode(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("DISPLAY_NODE");
                }
                break;
            //怪物移动
            case MOVE_LIFE:
                MobHandler.MoveMonster(slea, c, c.getPlayer());
                /* if ( server.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    System.out.println("角色攻击怪物");
                }*/
                break;
            //怪物移动线程
            case AUTO_AGGRO:
                MobHandler.AutoAggro(slea.readInt(), c.getPlayer());
                /*if (server.Start.ConfigValuesMap.get("脚本显码开关") <= 0) {
                    System.out.println("怪物移动线程");
                }*/
                break;
            //友爱伤害？
            case FRIENDLY_DAMAGE:
                MobHandler.FriendlyDamage(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("FRIENDLY_DAMAGE");
                }
                break;
            //击杀怪物？
            case MONSTER_BOMB:
                MobHandler.MonsterBomb(slea.readInt(), c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("MONSTER_BOMB");
                }
                break;
            //NPC商店
            case NPC_SHOP:
                NPCHandler.NPCShop(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("NPC_SHOP");
                }
                break;
            //开始对话NPC
            case NPC_TALK:
                NPCHandler.NPCTalk(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("开始对话NPC");
                }
                break;
            //结束对话NPC
            case NPC_TALK_MORE:
                NPCHandler.NPCMoreTalk(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("结束对话NPC");
                }
                break;
            //结婚类型？判断地图就传送
            case MARRAGE_RECV:
                NPCHandler.MarrageNpc(c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("MARRAGE_RECV");
                }
                break;
            //持续对话
            case NPC_ACTION:
                NPCHandler.NPCAnimation(slea, c);
                /*if(调试=="开"){
                     System.out.println("持续对话");
                }*/
                break;
            //任务
            case QUEST_ACTION:
                NPCHandler.QuestAction(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("任务1");
                }
                break;
            //游戏仓库
            case STORAGE:
                NPCHandler.Storage(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("游戏仓库");
                }
                break;
            //说话?
            case GENERAL_CHAT:
//                c.getPlayer().updateTick(slea.readInt());
                ChatHandler.GeneralChat(slea.readMapleAsciiString(), slea.readByte(), c, c.getPlayer());
                break;
            //说话?
            case PARTYCHAT:
                ChatHandler.Others(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("PARTYCHAT");
                }
                break;
            case PARTY_SS://搜索
                ChatHandler.PARTY_SS(slea, c, c.getPlayer());
                //System.out.println("PARTY_SS");
                break;
            //说话类型？
            case WHISPER:
                ChatHandler.Whisper_Find(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("WHISPER");
                }
                break;
            //枫叶信使？
            case MESSENGER:
                ChatHandler.Messenger(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("MESSENGER");
                }
                break;
            //自动分配能力点
            case AUTO_ASSIGN_AP://
                StatsHandling.AutoAssignAP(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("自动分配能力点");
                }
                break;
            //加能力点
            case DISTRIBUTE_AP:
                StatsHandling.DistributeAP(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("使用能力点");
                }
                break;
            //加技能
            case DISTRIBUTE_SP:
                c.getPlayer().updateTick(slea.readInt());
                StatsHandling.DistributeSP(slea.readInt(), c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("使用技能点");
                }
                break;
            //玩家建立什么物品，小游戏？雇佣？
            case PLAYER_INTERACTION:
                PlayerInteractionHandler.PlayerInteraction(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("开小游戏？");
                }
                break;
            //打开家族
            case GUILD_OPERATION:
                GuildHandler.Guild(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("GUILD_OPERATION");
                }
                break;
            //塔罗牌
            case UPDATE_CHAR_INFO:
                //System.err.println("UPDATE_CHAR_INFO");
                //PlayersHandler.UpdateCharInfo(slea.readAsciiString(), c);
                PlayersHandler.UpdateCharInfo(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("UPDATE_CHAR_INFO");
                }
                break;
            //家族名字？
            case DENY_GUILD_REQUEST:
                slea.skip(1);
                GuildHandler.DenyGuildRequest(slea.readMapleAsciiString(), c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("DENY_GUILD_REQUEST");
                }
                break;
            //加入家族
            case ALLIANCE_OPERATION:
                AllianceHandler.HandleAlliance(slea, c, false);
                if (MapleParty.调试3 > 0) {
                    System.out.println("ALLIANCE_OPERATION");
                }
                break;
            //加入家族
            case DENY_ALLIANCE_REQUEST:
                AllianceHandler.HandleAlliance(slea, c, true);
                if (MapleParty.调试3 > 0) {
                    System.out.println("DENY_ALLIANCE_REQUEST");
                }
                break;
            //创建什么新职位？
            case BBS_OPERATION:
                BBSHandler.BBSOperatopn(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("BBS_OPERATION");
                }
                break;
            //创建组队
            case PARTY_OPERATION:
                PartyHandler.PartyOperatopn(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("创建/解散组队");
                }
                break;
            //加入组队
            case DENY_PARTY_REQUEST:
                PartyHandler.DenyPartyRequest(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("DENY_PARTY_REQUEST");
                }
                break;
            //预算操作
            case BUDDYLIST_MODIFY:
                BuddyListHandler.BuddyOperation(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("BUDDYLIST_MODIFY");
                }
                break;
            //骑士团的任务？
            case CYGNUS_SUMMON:
                UserInterfaceHandler.CygnusSummon_NPCRequest(c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("CYGNUS_SUMMON");
                }
                break;
            //船舶系统
            case SHIP_OBJECT:
                UserInterfaceHandler.ShipObjectRequest(slea.readInt(), c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("SHIP_OBJECT");
                }
                break;
            //商城购买
            case BUY_CS_ITEM:
                CashShopOperation.BuyCashItem(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("商城购买物品");
                }
                break;
            //???????????
            case TOUCHING_CS:
                CashShopOperation.TouchingCashShop(c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("TOUCHING_CS");
                }
                break;
            //???????????
            case COUPON_CODE:
                FileoutputUtil.log(FileoutputUtil.PacketEx_Log, "Coupon : \n" + slea.toString(true));
                System.out.println("33" + slea);
                slea.skip(2);
                CashShopOperation.CouponCode(slea.readMapleAsciiString(), c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("COUPON_CODE");
                }
                break;
            //???????????
            case CS_UPDATE:
                CashShopOperation.CSUpdate(c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("CS_UPDATE");
                }
                break;
            //???????????
            case TOUCHING_MTS:
                MTSOperation.MTSUpdate(MTSStorage.getInstance().getCart(c.getPlayer().getId()), c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("TOUCHING_MTS");
                }
                break;
            //???????????
            case MTS_TAB:
                MTSOperation.MTSOperation(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("MTS_TAB");
                }
                break;
            //????????
            case DAMAGE_SUMMON:
                //   slea.skip(4);
                SummonHandler.DamageSummon(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("DAMAGE_SUMMON");
                }
                break;
            //坐骑？
            case MOVE_SUMMON:
                SummonHandler.MoveSummon(slea, c.getPlayer());
//                if (MapleParty.调试3 > 0) {
//                    System.out.println("MOVE_SUMMON");
//                }
                break;
            //召唤兽
            case SUMMON_ATTACK:
                SummonHandler.SummonAttack(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("SUMMON_ATTACK");
                }
                break;
            //运动龙
            case MOVE_DRAGON:
                SummonHandler.MoveDragon(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("MOVE_DRAGON");
                }
                break;
            //宠物捡起拾取例外列表
            case PET_EXCEPTIONLIST:
                PetHandler.PickExceptionList(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("PET_EXCEPTIONLIST");
                }
                break;
            //召唤宠物
            case SPAWN_PET:
                PetHandler.SpawnPet(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("SPAWN_PET");
                }
                break;
            //宠物技能
            case MOVE_PET:
                PetHandler.MovePet(slea, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("MOVE_PET");
                }
                break;
            //宠物聊天
            case PET_CHAT:
                if (slea.available() < 12) {
                    break;
                }
                PetHandler.PetChat((int) slea.readLong(), slea.readShort(), slea.readMapleAsciiString(), c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("PET_CHAT");
                }
                break;
            //宠物命令
            case PET_COMMAND:
                PetHandler.PetCommand(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("PET_COMMAND");
                }
                break;
            //宠物对象
            case PET_FOOD:
                PetHandler.PetFood(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("PET_FOOD");
                }
                break;
            //宠物捡起物品
            case PET_LOOT:
                InventoryHandler.Pickup_Pet(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("PET_LOOT");
                }
                break;
            //宠物，绯红？
            case PET_AUTO_POT:
                PetHandler.Pet_AutoPotion(slea, c, c.getPlayer());
                if (MapleParty.调试3 > 0) {
                    System.out.println("PET_AUTO_POT");
                }
                break;
            //嘉年华
            case MONSTER_CARNIVAL:
                MonsterCarnivalHandler.MonsterCarnival(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("MONSTER_CARNIVAL");
                }
                break;
            case DUEY_ACTION:
                DueyHandler.DueyOperation(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("DUEY_ACTION");
                }
                break;
            //开启雇佣
            case USE_HIRED_MERCHANT:
                HiredMerchantHandler.UseHiredMerchant(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("USE_HIRED_MERCHANT");
                }
                break;
            //雇佣领回道具
            case MERCH_ITEM_STORE:
                HiredMerchantHandler.MerchantItemStore(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("MERCH_ITEM_STORE");
                }
                break;
            //是什么作用
            case CANCEL_DEBUFF:
                // Ignore for now
                if (MapleParty.调试3 > 0) {
                    System.out.println("CANCEL_DEBUFF");
                }
                break;
            //雪球赛
            case LEFT_KNOCK_BACK:
                PlayerHandler.leftKnockBack(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("LEFT_KNOCK_BACK");
                }
                break;
            //什么都没有？
            case SNOWBALL:
                PlayerHandler.snowBall(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("SNOWBALL");
                }
                break;
            //聊天按钮
            case ChatRoom_SYSTEM:
                //PlayersHandler.ChatRoomHandler(slea, c);
                PlayersHandler.聊天(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("打开聊天");
                }
                break;
            //拍卖按钮
            case ENTER_MTS:
                PlayersHandler.拍卖(slea, c);
                //InterServerHandler.EnterMTS(c, c.getPlayer(), true);
                if (MapleParty.调试3 > 0) {
                    System.out.println("点击拍卖");
                }
                break;
            case LIE_DETECTOR:
                PlayersHandler.LieDetector(slea, c, c.getPlayer(), true);
                System.out.println("LIE_DETECTOR");
                break;
            case LIE_DETECTOR_RESPONSE:
                PlayersHandler.LieDetectorResponse(slea, c);
                System.out.println("LIE_DETECTOR_RESPONSE");
                break;
            case LIE_DETECTOR_REFRESH:
                PlayersHandler.LieDetectorRefresh(slea, c);
                System.out.println("LIE_DETECTOR_REFRESH");
                break;
            //商城按钮
            case ENTER_CASH_SHOP://
                slea.readInt();
                PlayersHandler.商城(slea, c);
                break;
            //打椰子，瓶盖
            case COCONUT:
                PlayersHandler.hitCoconut(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("COCONUT");
                }
                break;
            //作用在神木村的功能，不是神木村就不执行？
            case REPAIR:
                NPCHandler.repair(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("REPAIR");
                }
                break;
            //作用在神木村的功能，不是神木村就不执行？
            case REPAIR_ALL:
                NPCHandler.repairAll(c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("REPAIR_ALL");
                }
                break;
            //说了句谢谢你？和船舶系统一起的
            case GAME_POLL:
                UserInterfaceHandler.InGame_Poll(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("GAME_POLL");
                }
                break;
            //商店搜索器
            case OWL:
                InventoryHandler.Owl(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("OWL");
                }
                break;
            //进去雇佣
            case OWL_WARP:
                InventoryHandler.OwlWarp(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("OWL_WARP");
                }
                break;
            case USE_OWL_MINERVA:
                InventoryHandler.OwlMinerva(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("USE_OWL_MINERVA");
                }
                break;
            //RPG游戏？小游戏？？
            case RPS_GAME:
                NPCHandler.RPSGame(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("RPS_GAME");
                }
                break;
            //任务物品？
            case QUEST_ITEM:
                NPCHandler.QuestItemUI(slea, c);
                break;
            case UPDATE_QUEST:
                NPCHandler.UpdateQuest(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("UPDATE_QUEST");
                }
                break;
            //任务物品？，给鸡蛋涂粉末
            case USE_ITEM_QUEST:
                NPCHandler.UseItemQuest(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("USE_ITEM_QUEST");
                }
                break;
            //玩家对抗赛活动
            case FOLLOW_REQUEST:
                PlayersHandler.FollowRequest(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("FOLLOW_REQUEST");
                }
                break;
            //玩家对抗赛活动
            case FOLLOW_REPLY:
                PlayersHandler.FollowReply(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("FOLLOW_REPLY");
                }
                break;
            //结婚类型
            case RING_ACTION:
                PlayersHandler.RingAction(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("RING_ACTION");
                }
                break;
            //打开校谱
            case REQUEST_FAMILY:
                FamilyHandler.RequestFamily(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("打开校谱");
                }
                break;
            //打开学院
            case OPEN_FAMILY:
                FamilyHandler.OpenFamily(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("打开学院");
                }
                break;
            //学院邀请
            case FAMILY_OPERATION:
                FamilyHandler.FamilyOperation(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("FAMILY_OPERATION");
                }
                break;
            //踢出，解散家族
            case DELETE_JUNIOR:
                FamilyHandler.DeleteJunior(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("DELETE_JUNIOR");
                }
                break;
            //退出家族
            case DELETE_SENIOR:
                FamilyHandler.DeleteSenior(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("DELETE_SENIOR");
                }
                break;
            //学院传人
            case USE_FAMILY:
                FamilyHandler.UseFamily(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("USE_FAMILY");
                }
                break;
            //打开学院
            case FAMILY_PRECEPT:
                FamilyHandler.FamilyPrecept(slea, c);
                if (MapleParty.调试3 > 0) {
                    System.out.println("打开学院");
                }
                break;
            //学院拉人过来
            case FAMILY_SUMMON:
                FamilyHandler.FamilySummon(slea, c);
                break;
            //打开学院
            case ACCEPT_FAMILY:
                FamilyHandler.AcceptFamily(slea, c);
                break;
            // 豆豆机
            case BEANS_GAME1:
                //BeanGame.BeanGame1(slea, c);
                InventoryHandler.TestDouDou(slea, c);
                //c.sendPacket(MaplePacketCreator.serverNotice(1, "豆豆机尚未修复，非常抱歉。"));
                break;
            // 豆豆机
            case BEANS_GAME2:
                //InventoryHandler.TestDouDou(slea, c);
                //BeanGame.BeanGame2(slea, c);
                break;
            case MOONRABBIT_HP://月妙
                PlayerHandler.Rabbit(slea, c);
                break;
            //鱼缸
            case MAPLETV://鱼缸
                ChatHandler.MAPLETV(slea, c, c.getPlayer());
                break;

            default:
                FileoutputUtil.logToZev("服务端信息文件/服务端报错信息/" + CurrentReadable_Date() + "/handling.txt", "[未经处理的] 客户端包 [" + header + "] 发现了\r\n");
                System.out.println("[未经处理的] 客户端包 [" + header + "] 发现了");
                break;
        }
    }
}
