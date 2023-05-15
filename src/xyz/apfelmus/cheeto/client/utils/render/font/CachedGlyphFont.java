/*    */ package xyz.apfelmus.cheeto.client.utils.render.font;
/*    */ 
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class CachedGlyphFont {
/*    */   private int tex;
/*    */   private int width;
/*    */   private long lastUsage;
/*    */   
/*    */   public CachedGlyphFont(int tex, int width) {
/* 11 */     this.tex = tex;
/* 12 */     this.width = width;
/* 13 */     this.lastUsage = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void finalize() throws Throwable {
/* 18 */     GL11.glDeleteTextures(this.tex);
/*    */   }
/*    */   
/*    */   public int getTex() {
/* 22 */     return this.tex;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 26 */     return this.width;
/*    */   }
/*    */   
/*    */   public long getLastUsage() {
/* 30 */     return this.lastUsage;
/*    */   }
/*    */   
/*    */   public void setLastUsage(long lastUsage) {
/* 34 */     this.lastUsage = lastUsage;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\render\font\CachedGlyphFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */