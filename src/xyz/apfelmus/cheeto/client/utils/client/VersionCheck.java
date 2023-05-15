/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ 
/*    */ import gg.essential.api.utils.Multithreading;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import org.apache.commons.codec.digest.DigestUtils;
/*    */ import xyz.apfelmus.cheeto.Cheeto;
/*    */ 
/*    */ public class VersionCheck
/*    */ {
/* 11 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   public static boolean nope = true;
/*    */   
/*    */   public static void check() {
/* 15 */     List<String> blacks = JsonUtils.getListFromUrl("https://apfelmus.xyz:18781/static/json/blacklist.json", "uuids");
/*    */     
/* 17 */     if (!blacks.isEmpty() && !blacks.contains(DigestUtils.sha1Hex(mc.func_110432_I().func_148255_b()))) {
/* 18 */       nope = false;
/*    */     }
/*    */     
/* 21 */     Multithreading.runAsync(() -> {
/*    */           double version = getVersion();
/*    */           
/*    */           String msg = (version == -1.0D) ? "There was an error during Update Check" : ((version > Cheeto.clientVersion) ? "There's a newer version of Cheeto available! Download it from the Discord if you haven't already! You might risk getting trolled." : "Latest version, Cheeto on top");
/*    */           
/*    */           while (true) {
/*    */             try {
/*    */               Thread.sleep(100L);
/*    */               
/*    */               if (mc.field_71441_e != null) {
/*    */                 Thread.sleep(1000L);
/*    */                 ChatUtils.send(msg, new String[0]);
/*    */                 break;
/*    */               } 
/* 35 */             } catch (InterruptedException e) {
/*    */               e.printStackTrace();
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static double getVersion() {
/*    */     try {
/* 44 */       return Double.parseDouble(WebUtils.getContent(CheetoStatus.yepUrls[5]));
/* 45 */     } catch (Exception e) {
/* 46 */       return -1.0D;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\VersionCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */