/*     */ package xyz.apfelmus.cheeto.client.utils.render.shader;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.lwjgl.opengl.ARBShaderObjects;
/*     */ import org.lwjgl.opengl.GL20;
/*     */ 
/*     */ public abstract class Shader {
/*  14 */   protected static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   private int programID;
/*     */   
/*     */   private Map<String, Integer> uniformMap;
/*     */   
/*     */   public Shader(String vertexShader, String fragmentShader) {
/*     */     int vertexShaderID, fragmentShaderID;
/*     */     try {
/*  23 */       vertexShaderID = createShader(vertexShader, 35633);
/*  24 */       fragmentShaderID = createShader(fragmentShader, 35632);
/*  25 */     } catch (Exception e) {
/*  26 */       e.printStackTrace();
/*     */       
/*     */       return;
/*     */     } 
/*  30 */     if (vertexShaderID == 0 || fragmentShaderID == 0)
/*     */       return; 
/*  32 */     this.programID = ARBShaderObjects.glCreateProgramObjectARB();
/*     */     
/*  34 */     if (this.programID == 0) {
/*     */       return;
/*     */     }
/*  37 */     ARBShaderObjects.glAttachObjectARB(this.programID, vertexShaderID);
/*  38 */     ARBShaderObjects.glAttachObjectARB(this.programID, fragmentShaderID);
/*     */     
/*  40 */     ARBShaderObjects.glLinkProgramARB(this.programID);
/*  41 */     ARBShaderObjects.glValidateProgramARB(this.programID);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startShader() {
/*  46 */     GL20.glUseProgram(this.programID);
/*     */     
/*  48 */     if (this.uniformMap == null) {
/*  49 */       this.uniformMap = new HashMap<>();
/*  50 */       setupUniforms();
/*     */     } 
/*     */     
/*  53 */     updateUniforms();
/*     */   }
/*     */   
/*     */   public void stopShader() {
/*  57 */     GL20.glUseProgram(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void setupUniforms();
/*     */ 
/*     */   
/*     */   public abstract void updateUniforms();
/*     */   
/*     */   private int createShader(String shader, int type) {
/*     */     try {
/*  68 */       ResourceLocation resource = new ResourceLocation("chromahud:" + shader);
/*  69 */       BufferedInputStream bis = new BufferedInputStream(mc.func_110442_L().func_110536_a(resource).func_110527_b());
/*  70 */       String shaderSource = IOUtils.toString(bis);
/*  71 */       IOUtils.closeQuietly(bis);
/*     */       
/*  73 */       int shaderID = ARBShaderObjects.glCreateShaderObjectARB(type);
/*     */       
/*  75 */       if (shaderID == 0) return 0;
/*     */       
/*  77 */       ARBShaderObjects.glShaderSourceARB(shaderID, shaderSource);
/*  78 */       ARBShaderObjects.glCompileShaderARB(shaderID);
/*     */       
/*  80 */       if (ARBShaderObjects.glGetObjectParameteriARB(shaderID, 35713) == 0) {
/*  81 */         throw new RuntimeException("Error creating shader: " + getLogInfo(shaderID));
/*     */       }
/*  83 */       return shaderID;
/*  84 */     } catch (IOException e) {
/*  85 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getLogInfo(int i) {
/*  90 */     return ARBShaderObjects.glGetInfoLogARB(i, ARBShaderObjects.glGetObjectParameteriARB(i, 35716));
/*     */   }
/*     */   
/*     */   public void setUniform(String uniformName, int location) {
/*  94 */     this.uniformMap.put(uniformName, Integer.valueOf(location));
/*     */   }
/*     */   
/*     */   public void setupUniform(String uniformName) {
/*  98 */     setUniform(uniformName, GL20.glGetUniformLocation(this.programID, uniformName));
/*     */   }
/*     */   
/*     */   public int getUniform(String uniformName) {
/* 102 */     return ((Integer)this.uniformMap.get(uniformName)).intValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\render\shader\Shader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */