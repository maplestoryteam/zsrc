/*     */ package handling.cashshop;
/*     */ 
/*     */

import abc.Game;
import handling.channel.PlayerStorage;
import handling.netty.ServerConnection;
import handling.world.MapleParty;
import tools.FileoutputUtil;

import java.net.InetSocketAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CashShopServer
/*     */ {
/*     */   private static String ip;
/*     */   private static InetSocketAddress InetSocketadd;
/*  21 */   private static final int PORT = Game.商城端口;
/*     */   
/*     */   private static ServerConnection acceptor;
/*     */   private static PlayerStorage players;
/*     */   
/*     */   public static void run_startup_configurations() {
/*  27 */     ip = MapleParty.IP地址 + ":" + PORT;
/*  28 */     players = new PlayerStorage(-10);
/*  29 */     playersMTS = new PlayerStorage(-20);
/*     */     
/*     */     try {
/*  32 */       acceptor = new ServerConnection(PORT, 0, -10);
/*  33 */       acceptor.run();
/*  34 */       System.out.println("○ 加载钓鱼物品:  完成");
/*     */ 
/*     */ 
/*     */       
/*  38 */       System.out.println("○ 启动游戏商城:");
/*     */       
/*  40 */       System.out.println("○ 商城: 启动端口 " + PORT);
/*  41 */       System.out.println("○ 商城: " + FileoutputUtil.CurrentReadable_Time() + " 读取商城上架时装中···");
/*  42 */     } catch (Exception e) {
/*  43 */       System.err.println("Binding to port " + PORT + " failed");
/*  44 */       e.printStackTrace();
/*  45 */       throw new RuntimeException("Binding failed.", e);
/*     */     } 
/*     */   }
/*     */   private static PlayerStorage playersMTS; private static boolean finishedShutdown = false;
/*     */   
/*  50 */   public static final String getIP() { return ip; }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final PlayerStorage getPlayerStorage() { return players; }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public static final PlayerStorage getPlayerStorageMTS() { return playersMTS; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void shutdown() {
/*  95 */     if (finishedShutdown) {
/*     */       return;
/*     */     }
/*  98 */     System.out.println("正在断开商城内玩家...");
/*  99 */     players.disconnectAll();
/*     */ 
/*     */     
/* 102 */     System.out.println("关闭游戏商城服务器...");
/*     */     
/* 104 */     finishedShutdown = true;
/*     */   }
/*     */ 
/*     */   
/* 108 */   public static boolean isShutdown() { return finishedShutdown; }
/*     */ }


/* Location:              C:\Users\Administrator\Desktop\1.jar!\handling\cashshop\CashShopServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */