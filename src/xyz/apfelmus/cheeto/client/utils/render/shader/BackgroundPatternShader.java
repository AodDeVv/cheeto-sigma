/*    */ package xyz.apfelmus.cheeto.client.utils.render.shader;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ 
/*    */ public class BackgroundPatternShader extends Shader implements Closeable {
/*  6 */   public static BackgroundPatternShader INSTANCE = new BackgroundPatternShader();
/*    */   private boolean isInUse = false;
/*    */   
/*    */   public BackgroundPatternShader() {
/* 10 */     super("shaders/vertex.vert", "shaders/bg_pattern.frag");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setupUniforms() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateUniforms() {}
/*    */ 
/*    */   
/*    */   public void startShader() {
/* 23 */     super.startShader();
/* 24 */     this.isInUse = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopShader() {
/* 29 */     super.stopShader();
/* 30 */     this.isInUse = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 35 */     if (this.isInUse) stopShader(); 
/*    */   }
/*    */   
/*    */   public void update(boolean enable) {
/* 39 */     if (enable) {
/* 40 */       INSTANCE.startShader();
/*    */     } else {
/* 42 */       INSTANCE.stopShader();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\render\shader\BackgroundPatternShader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */