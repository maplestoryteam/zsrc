package client.messages.commands.a;

import client.MapleCharacter;
import client.MapleClient;
import client.messages.commands.CommandExecute;
import handling.channel.ChannelServer;
import handling.world.World;
import scripting.NPCScriptManager;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;

/**
 *
 * @author 玩家
 */
public class PlayerCommand {

   /* public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.NORMAL;
    }*/

    public static class cd extends save {
    }
    public static class 存档 extends save {
    }
    public static class bz extends helpll {
    }
    public static class 帮助 extends helpll {
    }
 
 //   public static class 爆物 extends bw {
  //  }

    public static class 解卡 extends ck {
    }
    public static class jc extends 检测 {
    }
   // public static class yy extends 音乐 {
    //}
    /*public static class admin extends zev {
    }*/
    public static class jk extends ck {
    }

    public static class ck extends CommandExecute {

        @Override
    public int execute(MapleClient c, String[] splitted) {
            //   PredictCardFactory.getInstance().initialize();
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            c.getPlayer().dropMessage(5, "解除假死成功，具体时怎么假死的请反馈给ZEV，71447500");
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用解卡功能"));
            
            //c.getPlayer().dropMessage(6, "当前时间是" + FileoutputUtil.CurrentReadable_Time() + " GMT+8 | 经验值倍率 " + (Math.round(c.getPlayer().getEXPMod()) * 100) * Math.round(c.getPlayer().getStat().expBuff / 100.0) + "%, 怪物倍率 " + (Math.round(c.getPlayer().getDropMod()) * 100) * Math.round(c.getPlayer().getStat().dropBuff / 100.0) + "%, 金币倍率 " + Math.round(c.getPlayer().getStat().mesoBuff / 100.0) * 100 + "%");
            //c.getPlayer().dropMessage(6, "当前延迟 " + c.getPlayer().getClient().getLatency() + " 毫秒");
            //  NPCScriptManager.getInstance().start(c, 9102001);
            if (c.getPlayer().isAdmin()) {
                c.sendPacket(MaplePacketCreator.sendPyramidEnergy("massacre_hit", String.valueOf(50)));

//                  c.sendPacket(MaplePacketCreator.sendPyramidResult((byte) 1, 23));
//                MapleCharacter chr = c.getPlayer();
//                for (MaplePet pet : chr.getPets()) {
//                    if (pet.getSummoned()) {
//                        int newFullness = pet.getFullness() - PetDataFactory.getHunger(pet.getPetItemId());
//                        newFullness = 100;
//                        if (newFullness <= 5) {
//                            pet.setFullness(15);
//                            chr.unequipPet(pet, true);
//                        } else {
//                            pet.setFullness(newFullness);
//                            chr.getClient().sendPacket(PetPacket.updatePet(pet, chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), true));
//                        }
//                    }
//                }
//                c.getPlayer().getStat().setDex((short) 4);
//                c.getPlayer().updateSingleStat(MapleStat.DEX, 4);
                //   System.out.println(new Date(System.currentTimeMillis() + (long) (3 * 60 * 60 * 1000)));
                //     c.getPlayer().getGuild().gainGP(50);  
                //     c.getPlayer().saveToDB(false, false);
            }//      
            return 1;
        }
    }

    public static class save extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().saveToDB(false, false);
            c.getPlayer().dropMessage("当前时间是 " + FileoutputUtil.CurrentReadable_Time()+ " ，角色信息存档成功了");
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用个人存档功能"));
                      
            return 1;
        }
    }

    /*public static class help extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            NPCScriptManager npc = NPCScriptManager.getInstance();
            npc.start(c, 9101001);
            return 1;
        }
    }*/
    
     /*public static class zev extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            NPCScriptManager npc = NPCScriptManager.getInstance();
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]唤出管理者后台功能"));
           
            npc.start(c, 2000,9);
            return 1;
        }
    }*/
    // public static class bw extends CommandExecute {

       // @Override
        //public int execute(MapleClient c, String[] splitted) {
           // NPCScriptManager.getInstance().dispose(c);
          //  c.sendPacket(MaplePacketCreator.enableActions());
            //NPCScriptManager npc = NPCScriptManager.getInstance();
           // npc.start(c, 2000,1);
            //return 1;
       // }
  //  }

    /*public static class Mobdrop extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.sendPacket(MaplePacketCreator.enableActions());
            NPCScriptManager npc = NPCScriptManager.getInstance();
            npc.start(c, 9900004,225);
            return 1;
        }
    }*/

    public static class GM extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted[1] == null) {
                c.getPlayer().dropMessage(6, "@GM后加上空格，输入要说的话。谢谢.");
                return 1;
            }
            if (c.getPlayer().isGM()) {
                c.getPlayer().dropMessage(6, "因为你自己是管理，无法使用此命令,可以尝试!cngm <讯息> 建立GM聊天頻道~");
                return 1;
            }
            if (!c.getPlayer().getCheatTracker().GMSpam(100000, 1)) { // 5 minutes.
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "頻道 " + c.getPlayer().getClient().getChannel() + " 玩家 [" + c.getPlayer().getName() + "] : " + StringUtil.joinStringFrom(splitted, 1)));
                c.getPlayer().dropMessage(6, "讯息已经发给GM了!");
            } else {
                c.getPlayer().dropMessage(6, "为了防止对GM刷屏所以每1分鐘只能发一次.");
            }
            return 1;
        }
    }

    public static class helpll extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
           // c.getPlayer().dropMessage(1, "《ZEVMS》\r\n\r\n玩家指令列表\r\n\r\n\r\n@cd  或 @存档   <保存数据>\r\n@GM + 想说的话  <对话管理>\r\n\r\n- 作者；ZEV，71447500 -\r\n");
           // c.getPlayer().dropMessage(5, "@bw  或 @爆物   <怪物爆率>");
            c.getPlayer().dropMessage(5, "@cd  或 @存档   <保存数据>");
            c.getPlayer().dropMessage(5, "@jk  或 @解卡   <解除假死>");
           // c.getPlayer().dropMessage(5, "@yy  或 @音乐   <音乐旋律>");
          //  c.getPlayer().dropMessage(5, "@jc + 角色      <检测作弊>");
            c.getPlayer().dropMessage(5, "@GM + 想说的话  <对话管理>");
         //  c.getPlayer().dropMessage(5, "@gg  或 @公告   <游戏资讯>");
           c.getPlayer().dropMessage(5, "- 作者；ZEV，71447500 -");
            return 1;
        }
    }
        public static class 检测 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {

            if (splitted.length < 2) {
                return 0;
            }
            final StringBuilder builder = new StringBuilder();
            MapleCharacter other;
            String name = splitted[1];
            int ch = World.Find.findChannel(name);
            if (ch <= 0) {
                c.getPlayer().dropMessage(6, "玩家必须在线");
                return 0;
            }
            other = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);

            if (other == null) {
                builder.append("角色不存在");
                c.getPlayer().dropMessage(6, builder.toString());
            } else {
                if (other.getClient().getLastPing() <= 0) {
                    other.getClient().sendPing();
                }
                builder.append(MapleClient.getLogMessage(other, ""));
                builder.append(" 在 ").append(other.getPosition().x);
                builder.append(" /").append(other.getPosition().y);

                builder.append(" || HP : ");
                builder.append(other.getStat().getHp());
                builder.append(" /");
                builder.append(other.getStat().getCurrentMaxHp());

                builder.append(" || MP : ");
                builder.append(other.getStat().getMp());
                builder.append(" /");
                builder.append(other.getStat().getCurrentMaxMp());

                builder.append(" || 物理攻击 : ");
                builder.append(other.getStat().getTotalWatk());
                builder.append(" || 魔法攻击 : ");
                builder.append(other.getStat().getTotalMagic());
               // builder.append(" || 最高攻擊 : ");
               // builder.append(other.getStat().getCurrentMaxBaseDamage());
                //builder.append(" || 攻擊%數 : ");
                //builder.append(other.getStat().dam_r);
                //builder.append(" || BOSS攻擊%數 : ");
               // builder.append(other.getStat().bossdam_r);

                builder.append(" || 力量 : ");
                builder.append(other.getStat().getStr());
                builder.append(" || 敏捷 : ");
                builder.append(other.getStat().getDex());
                builder.append(" || 智力 : ");
                builder.append(other.getStat().getInt());
                builder.append(" || 幸運 : ");
                builder.append(other.getStat().getLuk());

               // builder.append(" || 力量 : ");
               // builder.append(other.getStat().getTotalStr());
               // builder.append(" || 敏捷 : ");
                //builder.append(other.getStat().getTotalDex());
               // builder.append(" || 智力 : ");
                //builder.append(other.getStat().getTotalInt());
               // builder.append(" || 幸运 : ");
               // builder.append(other.getStat().getTotalLuk());

                builder.append(" || 经验值 : ");
                builder.append(other.getExp());

                builder.append(" || 组队状态 : ");
                builder.append(other.getParty() != null);

                //builder.append(" || 交易狀態: ");
               // builder.append(other.getTrade() != null);
               // builder.append(" || Latency: ");
               // builder.append(other.getClient().getLatency());
              //  builder.append(" || 最後PING: ");
               // builder.append(other.getClient().getLastPing());
               // builder.append(" || 最後PONG: ");
              //  builder.append(other.getClient().getLastPong());
                builder.append(" || IP: ");
                builder.append(other.getClient().getSessionIPAddress());
                other.getClient().DebugMessage(builder);

                c.getPlayer().dropMessage(6, builder.toString());
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(5, "[管理员信息]:指令信息 - [玩家:" + c.getPlayer().getName() + "]使用检测玩家信息功能"));
            
            }
            return 1;
        }
}
        public String getMessage() {
            return new StringBuilder().append("@检测 <角色名称> - 查看角色当前状态").toString();

        }
         /*   public static class 音乐 extends CommandExecute {

        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                return 0;
            }
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.musicChange(splitted[1]));
            return 1;
        }

        public String getMessage() {
            return new StringBuilder().append("@音乐 - 播放音乐").toString();
        }
    }*/
    }

    
