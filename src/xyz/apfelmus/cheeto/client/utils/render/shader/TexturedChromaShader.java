/*    */ package xyz.apfelmus.cheeto.client.utils.render.shader;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import org.lwjgl.opengl.GL20;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.TickTimer;
/*    */ import xyz.apfelmus.cheeto.mixin.mixins.MinecraftAccessor;
/*    */ 
/*    */ public class TexturedChromaShader
/*    */   extends Shader implements Closeable {
/* 10 */   public static TexturedChromaShader INSTANCE = new TexturedChromaShader();
/*    */   
/*    */   private boolean isInUse = false;
/* 13 */   float chromaSize = 35.0F;
/* 14 */   float saturation = 1.0F;
/*    */   
/*    */   public TexturedChromaShader() {
/* 17 */     super("shaders/textured_vertex.vert", "shaders/textured_chroma.frag");
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupUniforms() {
/* 22 */     setupUniform("chromaSize");
/* 23 */     setupUniform("timeOffset");
/* 24 */     setupUniform("saturation");
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateUniforms() {
/* 29 */     GL20.glUniform1f(getUniform("chromaSize"), this.chromaSize * mc.field_71443_c / 100.0F);
/* 30 */     GL20.glUniform1f(getUniform("timeOffset"), (TickTimer.ticks + (((MinecraftAccessor)mc).getTimer()).field_74281_c) / 65.0F);
/* 31 */     GL20.glUniform1f(getUniform("saturation"), this.saturation);
/*    */   }
/*    */ 
/*    */   
/*    */   public void startShader() {
/* 36 */     super.startShader();
/* 37 */     this.isInUse = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopShader() {
/* 42 */     super.stopShader();
/* 43 */     this.isInUse = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 48 */     if (this.isInUse) stopShader(); 
/*    */   }
/*    */   
/*    */   public void update(boolean enable, float chromaSize, float saturation) {
/* 52 */     INSTANCE.chromaSize = chromaSize;
/* 53 */     INSTANCE.saturation = saturation;
/*    */     
/* 55 */     if (enable) {
/* 56 */       INSTANCE.startShader();
/*    */     } else {
/* 58 */       INSTANCE.stopShader();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\render\shader\TexturedChromaShader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */