/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileInputStream;
/*    */ 
/*    */ public class FileUtils {
/*    */   public static String readFile(File file) {
/*  7 */     StringBuilder stringBuilder = new StringBuilder();
/*    */     
/*    */     try {
/* 10 */       FileInputStream fileInputStream = new FileInputStream(file);
/* 11 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
/*    */       String line;
/* 13 */       while ((line = bufferedReader.readLine()) != null) {
/* 14 */         stringBuilder.append(line).append('\n');
/*    */       }
/* 16 */     } catch (Exception e) {
/* 17 */       e.printStackTrace();
/*    */     } 
/* 19 */     return stringBuilder.toString();
/*    */   }
/*    */   
/*    */   public static String readInputStream(InputStream inputStream) {
/* 23 */     StringBuilder stringBuilder = new StringBuilder();
/*    */     
/*    */     try {
/* 26 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
/*    */       String line;
/* 28 */       while ((line = bufferedReader.readLine()) != null) {
/* 29 */         stringBuilder.append(line).append('\n');
/*    */       }
/* 31 */     } catch (Exception e) {
/* 32 */       e.printStackTrace();
/*    */     } 
/* 34 */     return stringBuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */