/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParser;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.nio.file.Files;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.imageio.ImageIO;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class CapeStorage
/*    */ {
/* 19 */   private static HashMap<String, ResourceLocation> capes = new HashMap<>();
/* 20 */   private static File capeFolder = new File("Cheeto", "capes");
/* 21 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   
/*    */   public static boolean loadCapes() {
/* 24 */     if (!capeFolder.exists()) {
/* 25 */       capeFolder.mkdirs();
/*    */     }
/* 27 */     JsonObject json = null;
/*    */     try {
/* 29 */       json = (new JsonParser()).parse(WebUtils.getContent(CheetoStatus.yepUrls[2])).getAsJsonObject();
/* 30 */     } catch (Exception exception) {}
/*    */ 
/*    */     
/* 33 */     if (json == null) return false;
/*    */     
/* 35 */     for (Map.Entry<String, JsonElement> ce : (Iterable<Map.Entry<String, JsonElement>>)json.get("capes").getAsJsonObject().entrySet()) {
/* 36 */       String owner = ce.getKey();
/* 37 */       String cape = ((JsonElement)ce.getValue()).getAsString();
/* 38 */       String url = CheetoStatus.yepUrls[1] + cape;
/* 39 */       File capePath = new File(capeFolder, cape + ".png");
/*    */       
/* 41 */       if (!capePath.exists()) {
/*    */         try {
/* 43 */           Files.copy((new URL(url)).openStream(), capePath.toPath(), new java.nio.file.CopyOption[0]);
/* 44 */         } catch (IOException e) {
/* 45 */           e.printStackTrace();
/*    */         } 
/*    */       }
/*    */       
/*    */       try {
/* 50 */         capes.put(owner, mc.func_110434_K().func_110578_a("chromahud", new DynamicTexture(ImageIO.read(capePath))));
/* 51 */       } catch (IOException e) {
/* 52 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */     
/* 56 */     return (capes.size() > 0);
/*    */   }
/*    */   
/*    */   public static ResourceLocation getCape(String uuid) {
/* 60 */     return capes.get(uuid);
/*    */   }
/*    */   
/*    */   public static void clearCapes() {
/* 64 */     capes.clear();
/*    */     
/* 66 */     if (capeFolder.exists()) {
/* 67 */       File[] files = capeFolder.listFiles();
/* 68 */       if (files != null)
/* 69 */         for (File file : files)
/* 70 */           file.delete();  
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\CapeStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */