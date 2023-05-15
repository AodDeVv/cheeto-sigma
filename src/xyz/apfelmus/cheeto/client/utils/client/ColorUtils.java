/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.util.ChatAllowedCharacters;
/*    */ import xyz.apfelmus.cheeto.client.utils.math.RandomUtil;
/*    */ 
/*    */ public class ColorUtils
/*    */ {
/*  9 */   private static int[] hexColors = generateHexColors();
/*    */   
/*    */   public static int getChroma(float speed, int offset) {
/* 12 */     return Color.HSBtoRGB((float)((System.currentTimeMillis() - offset * 10L) % (long)speed) / speed, 0.88F, 0.88F);
/*    */   }
/*    */   
/*    */   private static int[] generateHexColors() {
/* 16 */     int[] ret = new int[16];
/*    */     
/* 18 */     for (int i = 0; i < 16; i++) {
/* 19 */       int base = (i >> 3 & 0x1) * 85;
/*    */       
/* 21 */       int red = (i >> 2 & 0x1) * 170 + base + ((i == 6) ? 85 : 0);
/* 22 */       int green = (i >> 1 & 0x1) * 170 + base;
/* 23 */       int blue = (i & 0x1) * 170 + base;
/*    */       
/* 25 */       ret[i] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
/*    */     } 
/*    */     
/* 28 */     return ret;
/*    */   }
/*    */   
/*    */   public static int getHexColor(int colorIndex) {
/* 32 */     return hexColors[colorIndex];
/*    */   }
/*    */   
/*    */   public static String randomMagicText(String text) {
/* 36 */     StringBuilder sb = new StringBuilder();
/* 37 */     String allowedCharacters = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\000\000\000\000\000\000\000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\000";
/*    */     
/* 39 */     for (char c : text.toCharArray()) {
/* 40 */       if (ChatAllowedCharacters.func_71566_a(c)) {
/* 41 */         int index = RandomUtil.nextInt(allowedCharacters.length());
/* 42 */         sb.append(allowedCharacters.toCharArray()[index]);
/*    */       } 
/*    */     } 
/*    */     
/* 46 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\ColorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */