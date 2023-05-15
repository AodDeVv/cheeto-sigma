/*    */ package xyz.apfelmus.cheeto.client.utils.render.font;
/*    */ 
/*    */ import java.awt.Font;
/*    */ 
/*    */ public class FontUtils {
/*  6 */   public static GlyphFontRenderer small = new GlyphFontRenderer(new Font("Tahoma", 0, 20));
/*  7 */   public static GlyphFontRenderer normal = new GlyphFontRenderer(new Font("Tahoma", 0, 30));
/*  8 */   public static GlyphFontRenderer normalBold = new GlyphFontRenderer(new Font("Tahoma", 1, 30));
/*  9 */   public static GlyphFontRenderer semiBig = new GlyphFontRenderer(new Font("Tahoma", 0, 60));
/* 10 */   public static GlyphFontRenderer semiBigBold = new GlyphFontRenderer(new Font("Tahoma", 1, 60));
/* 11 */   public static GlyphFontRenderer big = new GlyphFontRenderer(new Font("Tahoma", 0, 80));
/* 12 */   public static GlyphFontRenderer bigBold = new GlyphFontRenderer(new Font("Tahoma", 1, 80));
/* 13 */   public static GlyphFontRenderer superbigBold = new GlyphFontRenderer(new Font("Tahoma", 1, 160));
/*    */   
/*    */   public static String truncateString(String currentValue, int maxWidth, GlyphFontRenderer font) {
/* 16 */     String text = "";
/* 17 */     int triple = font.getStringWidth("...");
/* 18 */     for (int i = 0; i < currentValue.length(); i++) {
/* 19 */       if (font.getStringWidth(currentValue.substring(0, i)) < maxWidth - triple) {
/* 20 */         text = currentValue.substring(0, i);
/*    */       } else {
/* 22 */         text = text + "...";
/*    */         break;
/*    */       } 
/*    */     } 
/* 26 */     return text;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\render\font\FontUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */