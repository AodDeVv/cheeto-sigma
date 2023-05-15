/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class ChatUtils {
/*  7 */   private static final String PREFIX = ChatColor.format("&6[&eCheeto&6]&f ");
/*    */   
/*    */   public static void send(String text, String... args) {
/* 10 */     if ((Minecraft.func_71410_x()).field_71439_g == null)
/* 11 */       return;  text = String.format(text, (Object[])args);
/* 12 */     StringBuilder messageBuilder = new StringBuilder();
/* 13 */     for (String word : text.split(" ")) {
/* 14 */       word = ChatColor.format(ChatColor.getLastColors(text) + word);
/* 15 */       messageBuilder.append(word).append(" ");
/*    */     } 
/* 17 */     (Minecraft.func_71410_x()).field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(PREFIX + ChatColor.format(messageBuilder.toString().trim())));
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\ChatUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */