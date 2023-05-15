/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ 
/*    */ public class WebUtils
/*    */ {
/*    */   public static String getContent(String url) throws Exception {
/* 13 */     URL website = new URL(url);
/* 14 */     URLConnection connection = website.openConnection();
/* 15 */     BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/* 16 */     StringBuilder response = new StringBuilder();
/*    */     
/*    */     String inputLine;
/* 19 */     while ((inputLine = in.readLine()) != null) {
/* 20 */       response.append(inputLine);
/*    */     }
/* 22 */     in.close();
/*    */     
/* 24 */     return response.toString();
/*    */   }
/*    */   
/*    */   public static String postContent(String url, String data) throws Exception {
/* 28 */     URL website = new URL(url);
/* 29 */     URLConnection connection = website.openConnection();
/* 30 */     connection.setDoOutput(true);
/* 31 */     connection.setRequestProperty("Content-Type", "text/plain");
/* 32 */     connection.setRequestProperty("Content-Length", String.valueOf(data.length()));
/* 33 */     connection.getOutputStream().write(data.getBytes());
/* 34 */     BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/* 35 */     StringBuilder response = new StringBuilder();
/*    */     
/*    */     String inputLine;
/* 38 */     while ((inputLine = in.readLine()) != null) {
/* 39 */       response.append(inputLine);
/*    */     }
/* 41 */     in.close();
/*    */     
/* 43 */     return response.toString();
/*    */   }
/*    */   
/*    */   public static byte[] getBytes(String url) {
/*    */     try {
/* 48 */       URL website = new URL(url);
/* 49 */       URLConnection connection = website.openConnection();
/* 50 */       return IOUtils.toByteArray(connection.getInputStream());
/* 51 */     } catch (IOException e) {
/* 52 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\WebUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */