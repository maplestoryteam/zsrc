/*
组队系统
 */
package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import tools.MaplePacketCreator;
import tools.data.LittleEndianAccessor;

public class PartyHandler {

    public static void DenyPartyRequest(LittleEndianAccessor slea, MapleClient c) {
        /*队伍操作码*/
        int action = slea.readByte();
        /*队伍编号*/
        int partyid = slea.readInt();
        /*如果玩家的队伍为空*/
        if (c.getPlayer().getParty() == null) {
            /*声明变量，赋值*/
            MapleParty party = World.Party.getParty(partyid);
            /*如果队伍不为空*/
            if (party != null) {
                /*循环获取队伍操作码*/
                switch (action) {
                    /*如果是0x1B 接受邀请*/
                    case 0x1B:
                        /*如果队伍的人数小于6人*/
                        if (party.getMembers().size() < 6) {
                            /*通知队伍队员，新队员加入队伍*/
                            World.Party.updateParty(partyid, PartyOperation.JOIN, new MaplePartyCharacter(c.getPlayer()));
                            /*收到玩家的血条*/
                            c.getPlayer().receivePartyMemberHP();
                            /*更新玩家的血条*/
                            c.getPlayer().updatePartyMemberHP();
                        } else {
                            /*组队成员已满*/
                            c.sendPacket(MaplePacketCreator.partyStatusMessage(17));
                        }
                        break;
                    /*如果是0x16 拒绝邀请*/
                    case 0x16:
                        /*声明变量，赋值为获取是在线状态 获取队长的队伍编号*/
                        MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterById(party.getLeader().getId());
                        /*存在为真*/
                        if (cfrom != null) {
                            /*发送封包消息。通知邀请者，被邀请者“拒绝了组队招待邀请”*/
                            cfrom.getClient().sendPacket(MaplePacketCreator.partyStatusMessage(23, c.getPlayer().getName()));
                        }
                        break;
                    default:
                        //跳出循环
                        break;
                }

            } else {
                c.getPlayer().dropMessage(5, "要参加的队伍不存在。");
            }
        } else {
            c.getPlayer().dropMessage(5, "您已经有一个组队，无法加入其他组队!");
        }

    }

    public static void PartyOperatopn(LittleEndianAccessor slea, MapleClient c) {
        /*队伍操作包*/
        int operation = slea.readByte();
        /*声明变量party 获取玩家队伍*/
        MapleParty party = c.getPlayer().getParty();
        /*声明变量 partyplayer 获取队伍里面的玩家*/
        MaplePartyCharacter partyplayer = new MaplePartyCharacter(c.getPlayer());

        switch (operation) {
            case 1: //创建队伍
                /*如果获取玩家队伍为空*/
                if (party == null) {
                    /*赋值party 线程中创建一个队伍*/
                    party = World.Party.createParty(partyplayer);
                    /*给予队伍*/
                    c.getPlayer().setParty(party);
                    /*发送组队的封包*/
                    c.sendPacket(MaplePacketCreator.partyCreated(party.getId()));
                    /*如果队伍玩家是队长，并且队伍人数 = 1人*/
                } else if (partyplayer.equals(party.getLeader()) && party.getMembers().size() == 1) { //only one, reupdate
                    c.sendPacket(MaplePacketCreator.partyCreated(party.getId()));
                } else {
                    c.getPlayer().dropMessage(5, "你不能创建一个组队,因为你已经存在一个队伍中");
                }
                break;
            case 2: // 解散and离开队伍
                /*如果队伍不为空*/
                if (party != null) { //
                    /*如果离开的队伍的玩家是队长*/
                    if (partyplayer.equals(party.getLeader())) { // 解散
                        /*通知队员，队长解散队伍*/
                        World.Party.updateParty(party.getId(), PartyOperation.DISBAND, partyplayer);
                        /*在副本中*/
                        if (c.getPlayer().getEventInstance() != null) {
                            c.getPlayer().getEventInstance().disbandParty();
                        }
                        /*在副本中*/
                        if (c.getPlayer().getPyramidSubway() != null) {
                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());
                        }
                    } else {
                        /*通知队伍其他人，队员退出队伍*/
                        World.Party.updateParty(party.getId(), PartyOperation.LEAVE, partyplayer);
                        /*在副本中*/
                        if (c.getPlayer().getEventInstance() != null) {
                            c.getPlayer().getEventInstance().leftParty(c.getPlayer());
                        }
                        /*在副本中*/
                        if (c.getPlayer().getPyramidSubway() != null) {
                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());
                        }
                    }
                    /*设置空组队状态*/
                    c.getPlayer().setParty(null);
                }
                break;
            case 3: //加入队伍
                /*队伍ID编号*/
                int partyid = slea.readInt();
                /*角色的队伍为空*/
                if (party == null) {
                    /*party二次赋值*/
                    party = World.Party.getParty(partyid);
                    /*如果队伍不为空*/
                    if (party != null) {
                        /*如果队伍人数小于6人*/
                        if (party.getMembers().size() < 6) {
                            int a = 1;
                            for (final MaplePartyCharacter chr : party.getMembers()) {
                                if (chr.getMapid() != c.getPlayer().getMapId()) {
                                    a = 0;
                                }
                            }
                            if (a > 0) {
                                /*通知队员，有人加入队伍*/
                                World.Party.updateParty(party.getId(), PartyOperation.JOIN, partyplayer);
                                /*收到玩家的血条*/
                                c.getPlayer().receivePartyMemberHP();
                                /*更新玩家的血条*/
                                c.getPlayer().updatePartyMemberHP();
                            } else {
                                c.getPlayer().dropMessage(5, "无法加入组队。");
                            }
                        } else {
                            /*发送封包“组队成员已满”*/
                            c.sendPacket(MaplePacketCreator.partyStatusMessage(17));
                        }
                    } else {
                        c.getPlayer().dropMessage(5, "要加入的队伍不存在");
                    }
                } else {
                    c.getPlayer().dropMessage(5, "你已经有队伍了，无法再加入其他队伍。请退组再试！");
                }
                break;
            case 4: // 邀请
                /*声明变量并赋值 获取频道在线玩家名称*/
                MapleCharacter invited = c.getChannelServer().getPlayerStorage().getCharacterByName(slea.readMapleAsciiString());
                /*如果invited不为空*/
                if (invited != null) {
                    /*如果被邀请的玩家队伍为空，并且邀请者队伍不为空*/
                    if (invited.getParty() == null && party != null) {
                        /*如果队伍人数小于6人*/
                        if (party.getMembers().size() < 6) {
                            //c.sendPacket(MaplePacketCreator.partyStatusMessage(23, invited.getName()));
                            /*发送消息，邀请对方加入组队*/
                            if (invited.getMapId() == c.getPlayer().getMapId() && c.getPlayer().getClient().getChannel() == invited.getClient().getChannel()) {
                                invited.getClient().sendPacket(MaplePacketCreator.partyInvite(c.getPlayer()));
                            } else {
                                c.getPlayer().dropMessage(5, "无法邀请目标加入组队。");
                            }
                        } else {
                            /*队伍成员已满*/
                            c.sendPacket(MaplePacketCreator.partyStatusMessage(17));
                        }
                    } else {
                        /*发送消息提示被邀请的玩家“已经加入其他组”*/
                        c.sendPacket(MaplePacketCreator.partyStatusMessage(16));
                    }
                } else {
                    c.sendPacket(MaplePacketCreator.partyStatusMessage(18));
                }
                break;
            case 5:  // 驱逐队员
                if ((party == null) || (partyplayer == null) || (!partyplayer.equals(party.getLeader()))) {
                    break;
                }
                /*如果队伍中的玩家是队长*/
                if (partyplayer.equals(party.getLeader())) {
                    /*声明expelled变量并赋值 获取队伍的编号*/
                    MaplePartyCharacter expelled = party.getMemberById(slea.readInt());
                    /*队伍编号不为空*/
                    if (expelled != null) {
                        /*发送通知其他队员，队员被驱逐出队伍*/
                        World.Party.updateParty(party.getId(), PartyOperation.EXPEL, expelled);
                        /*如果是在副本中*/
                        if (c.getPlayer().getEventInstance() != null) {
                            /*如果队伍被驱逐的玩家是在线的*/
                            if (expelled.isOnline()) {
                                /*结束副本，全队传送出去*/
                                c.getPlayer().getEventInstance().disbandParty();
                            }
                        }
                        /*副本中*/
                        if (c.getPlayer().getPyramidSubway() != null && expelled.isOnline()) {
                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());
                        }
                    }
                }
                break;
            case 6: // 改变队长
                /*队伍不为空*/
                if (party != null) {
                    /*声明变量 newleader 新的队长 =获取队伍编号*/
                    MaplePartyCharacter newleader = party.getMemberById(slea.readInt());
                    /*如果被给予新队长的队伍ID编号是存在的。并且给予新队长的队员是队长*/
                    if (newleader != null && partyplayer.equals(party.getLeader())) {
                        /*设置队伍的队长，给予新队长*/
                        party.setLeader(newleader);//给予其队长
                        /*发送队伍更新队长状态。R键界面，队伍中的队长五角星，会转移到新队长。*/
                        World.Party.updateParty(party.getId(), PartyOperation.SILENT_UPDATE, newleader);
                    }
                    /*结束*/
                    c.sendPacket(MaplePacketCreator.enableActions());
                }
                break;
            default:
                System.out.println("未知的队伍操作. 0x0" + operation);
                break;
        }
    }
}

//    public static final void DenyPartyRequest(final LittleEndianAccessor slea, final MapleClient c) {
//        final int action = slea.readByte();
//        final int partyid = slea.readInt();
//        //    MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterByName(slea.readMapleAsciiString());
//        //      if (cfrom != null) {
//        //        cfrom.getClient().sendPacket(MaplePacketCreator.partyStatusMessage(23, c.getPlayer().getName()));
//        //    }
//        if (c.getPlayer().getParty() == null) {
//            MapleParty party = World.Party.getParty(partyid);
//            if (party != null) {
//                if (action == 0x1B) { //accept
//                    if (party.getMembers().size() < 12 ) {//组队人数
//                        World.Party.updateParty(partyid, PartyOperation.JOIN, new MaplePartyCharacter(c.getPlayer()));
//                        c.getPlayer().receivePartyMemberHP();
//                        c.getPlayer().updatePartyMemberHP();
//                    } else {
//                        c.sendPacket(MaplePacketCreator.partyStatusMessage(17));
//                    }
//                } else if (action != 0x16) {
//                    final MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterById(party.getLeader().getId());
//                    if (cfrom != null) {
//                        cfrom.getClient().sendPacket(MaplePacketCreator.partyStatusMessage(23, c.getPlayer().getName()));
//                    }
//                }
//            } else {
//                c.getPlayer().dropMessage(5, "要参加的队伍不存在。");
//            }
//        } else {
//            c.getPlayer().dropMessage(5, "您已经有一个组队，无法加入其他组队!");
//        }
//
//    }
//
//    public static final void PartyOperatopn(final LittleEndianAccessor slea, final MapleClient c) {
//        final int operation = slea.readByte();
//        MapleParty party = c.getPlayer().getParty();
//        MaplePartyCharacter partyplayer = new MaplePartyCharacter(c.getPlayer());
//
//        switch (operation) {
//            case 1: // create
//                if (c.getPlayer().getParty() == null) {
//                    party = World.Party.createParty(partyplayer);
//                    c.getPlayer().setParty(party);
//                    c.sendPacket(MaplePacketCreator.partyCreated(party.getId()));
//
//                } else {
//                    if (partyplayer.equals(party.getLeader()) && party.getMembers().size() == 1) { //only one, reupdate
//                        c.sendPacket(MaplePacketCreator.partyCreated(party.getId()));
//                    } else {
//                        c.getPlayer().dropMessage(5, "你不能创建一个组队,因为你已经存在一个队伍中");
//                    }
//                }
//                break;
//            case 2: // leave
//                if (party != null) { //are we in a party? o.O"
//                    if (partyplayer.equals(party.getLeader())) { // disband
//                        World.Party.updateParty(party.getId(), PartyOperation.DISBAND, partyplayer);
//                        if (c.getPlayer().getEventInstance() != null) {
//                            c.getPlayer().getEventInstance().disbandParty();
//                        }
//                        if (c.getPlayer().getPyramidSubway() != null) {
//                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());
//                        }
//                    } else {
//                        World.Party.updateParty(party.getId(), PartyOperation.LEAVE, partyplayer);
//                        if (c.getPlayer().getEventInstance() != null) {
//                            c.getPlayer().getEventInstance().leftParty(c.getPlayer());
//                        }
//                        if (c.getPlayer().getPyramidSubway() != null) {
//                            c.getPlayer().getPyramidSubway().fail(c.getPlayer());
//                        }
//                    }
//                    c.getPlayer().setParty(null);
//                }
//                break;
//            case 3: // accept invitation
//                final int partyid = slea.readInt();
//                if (c.getPlayer().getParty() == null) {
//                    party = World.Party.getParty(partyid);
//                    if (party != null) {
//                        if (party.getMembers().size() < 12 ) {//组队人数
//                            World.Party.updateParty(party.getId(), PartyOperation.JOIN, partyplayer);
//                            c.getPlayer().receivePartyMemberHP();
//                            c.getPlayer().updatePartyMemberHP();
//                        } else {
//                            c.sendPacket(MaplePacketCreator.partyStatusMessage(17));
//                        }
//                    } else {
//                        c.getPlayer().dropMessage(5, "要加入的队伍不存在");
//                    }
//                } else {
//                    c.getPlayer().dropMessage(5, "你不能创建一个组队,因为你已经存在一个队伍中");
//                }
//                break;
//            case 4: // invite
//                // TODO store pending invitations and check against them
//                final MapleCharacter invited = c.getChannelServer().getPlayerStorage().getCharacterByName(slea.readMapleAsciiString());
//                if (invited != null) {
//                    if (invited.getParty() == null && party != null) {
//                        if (party.getMembers().size() < 6 ) {//组队人数
//                            //  c.sendPacket(MaplePacketCreator.partyStatusMessage(23, invited.getName()));
//                            invited.getClient().sendPacket(MaplePacketCreator.partyInvite(c.getPlayer()));
//                        } else {
//                            break;
//                            // c.sendPacket(MaplePacketCreator.partyStatusMessage(16));
//                        }
//                    } else {
//                        c.sendPacket(MaplePacketCreator.partyStatusMessage(16));
//                    }
//                } else {
//                    c.sendPacket(MaplePacketCreator.partyStatusMessage(18));
//                }
//                break;
//            case 5: // expel
//                if (partyplayer != null && party != null) {
//                    if (partyplayer.equals(party.getLeader())) {
//                        final MaplePartyCharacter expelled = party.getMemberById(slea.readInt());
//                        if (expelled != null) {
//                            World.Party.updateParty(party.getId(), PartyOperation.EXPEL, expelled);
//                            if (c.getPlayer().getEventInstance() != null) {
//                                /*
//                                 * if leader wants to boot someone, then the
//                                 * whole party gets expelled TODO: Find an
//                                 * easier way to get the character behind a
//                                 * MaplePartyCharacter possibly remove just the
//                                 * expellee.
//                                 */
//                                if (expelled.isOnline()) {
//                                    c.getPlayer().getEventInstance().disbandParty();
//                                }
//                            }
//                            if (c.getPlayer().getPyramidSubway() != null && expelled.isOnline()) {
//                                c.getPlayer().getPyramidSubway().fail(c.getPlayer());
//                            }
//                        }
//                    }
//                }
//                break;
//            case 6: // change leader
//
//                if (party != null) {
//                    final MaplePartyCharacter newleader = party.getMemberById(slea.readInt());
//                    if (newleader != null && partyplayer.equals(party.getLeader())) {
//                        party.setLeader(newleader);
//                        World.Party.updateParty(party.getId(), PartyOperation.SILENT_UPDATE, newleader);
//                    }
//                    c.sendPacket(MaplePacketCreator.enableActions());
//                }
//                break;
//            default:
//                System.out.println("Unhandled Party function." + operation);
//                break;
//        }
//    }

