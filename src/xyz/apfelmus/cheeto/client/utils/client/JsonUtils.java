/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParser;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import xyz.apfelmus.cheeto.client.utils.fishing.FishingJson;
/*    */ import xyz.apfelmus.cheeto.client.utils.mining.MiningJson;
/*    */ 
/*    */ public class JsonUtils {
/* 13 */   public static Gson gson = new Gson();
/* 14 */   private static JsonParser parser = new JsonParser();
/*    */   
/*    */   public static MiningJson getMiningJson() {
/* 17 */     MiningJson mj = null;
/*    */     
/*    */     try {
/* 20 */       mj = (MiningJson)gson.fromJson(WebUtils.getContent(CheetoStatus.yepUrls[4]), MiningJson.class);
/* 21 */     } catch (Exception e) {
/* 22 */       e.printStackTrace();
/*    */     } 
/*    */     
/* 25 */     return mj;
/*    */   }
/*    */   
/*    */   public static FishingJson getFishingJson() {
/* 29 */     FishingJson mj = null;
/*    */     
/*    */     try {
/* 32 */       mj = (FishingJson)gson.fromJson(WebUtils.getContent(CheetoStatus.yepUrls[3]), FishingJson.class);
/* 33 */     } catch (Exception e) {
/* 34 */       e.printStackTrace();
/*    */     } 
/*    */     
/* 37 */     return mj;
/*    */   }
/*    */   
/*    */   public static List<String> getListFromUrl(String url, String name) {
/* 41 */     List<String> ret = new ArrayList<>();
/*    */     
/*    */     try {
/* 44 */       JsonObject json = (JsonObject)parser.parse(WebUtils.getContent(url));
/* 45 */       json.getAsJsonArray(name).forEach(je -> ret.add(je.getAsString()));
/* 46 */     } catch (Exception e) {
/* 47 */       e.printStackTrace();
/*    */     } 
/*    */     
/* 50 */     return ret;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\JsonUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */