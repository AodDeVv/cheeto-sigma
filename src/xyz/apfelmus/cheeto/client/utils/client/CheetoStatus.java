/*     */ package xyz.apfelmus.cheeto.client.utils.client;
/*     */ 
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.Files;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraftforge.fml.common.Loader;
/*     */ import net.minecraftforge.fml.common.ModContainer;
/*     */ import org.apache.commons.codec.digest.DigestUtils;
/*     */ import org.apache.http.client.utils.URIBuilder;
/*     */ import xyz.apfelmus.cheeto.client.modules.world.GhostBlock;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CheetoStatus
/*     */ {
/*  24 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   public static boolean yep = false;
/*  26 */   public static String[] yepUrls = new String[] { "https://gist.githubusercontent.com/Apfelmus1337/da641d3805bddf800eef170cbb0068ec/raw/sea_creatures.json", "https://gist.githubusercontent.com/Apfelmus1337/9db11b6cfb0bc32be244e8a909c6bf6c/raw/capes.json", "bTwmZXM8cGxpdywiLTBiNSVsJTI/LT1/aiQvdSsifG4paCsueCgkdz04YG8rIiF6bg==", "https://gist.githubusercontent.com/noemtdev/f93f6cd3ed2e58e0b786afdcab0d170f/raw/fishing.json", "https://gist.githubusercontent.com/Apfelmus1337/db85f05991a60ef4f1a6059353120764/raw/mining.json", "http://apfelmus.xyz:6969/cheeto/version", "bTwmZXM8cGxpdywiLTBiNSVsJTI/LT1/aiQvaDo0", "bTwmZXM8cGxpdywiLTBiNSVsJTI/LT1/aiQvZTAtbm4taA==" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   private static String serverId = null;
/*     */   public static String yepCock;
/*     */   public static String[] yupUrls;
/*     */   
/*     */   public static void prepare() { try {
/*  41 */       serverId = generateHash(mc.func_110432_I().func_148255_b());
/*  42 */     } catch (NoSuchAlgorithmException noSuchAlgorithmException) {}
/*     */ 
/*     */ 
/*     */     
/*  46 */     try { mc.func_152347_ac().joinServer(mc.func_110432_I().func_148256_e(), mc.func_110432_I().func_148254_d(), serverId); }
/*  47 */     catch (AuthenticationException e)
/*  48 */     { e.printStackTrace(); }  } public static void check() { try { serverId = generateHash(mc.func_110432_I().func_148255_b()); } catch (NoSuchAlgorithmException noSuchAlgorithmException) {} try { mc.func_152347_ac().joinServer(mc.func_110432_I().func_148256_e(), mc.func_110432_I().func_148254_d(), serverId); } catch (AuthenticationException e) { e.printStackTrace(); }
/*     */      }
/*     */ 
/*     */   
/*     */   void chuck() {
/*  53 */     resp = getResponse(serverId);
/*     */     
/*  55 */     if (resp != null) {
/*  56 */       if (resp.length <= 56) {
/*  57 */         Arrays.fill((Object[])yepUrls, "https://gist.githubusercontent.com/Apfelmus1337/db85f05991a60ef4f1a6059353120764/raw/mining.json");
/*     */       } else {
/*  59 */         Object a = Arrays.copyOfRange((byte[])resp, 0, 22);
/*  60 */         Object b = Arrays.copyOfRange((byte[])resp, 22, resp.length);
/*  61 */         for (int i = 0; i < yepUrls.length; i++) {
/*  62 */           yepUrls[i] = GhostBlock.AAhmjPyDR9YfpBU9(yepUrls[i], (byte[])a);
/*     */         }
/*  64 */         yepCock = GhostBlock.AAhmjPyDR9YfpBU9(new String((byte[])b), (byte[])a);
/*  65 */         yep = true;
/*     */       } 
/*     */     } else {
/*  68 */       Arrays.fill((Object[])yepUrls, "https://gist.githubusercontent.com/Apfelmus1337/db85f05991a60ef4f1a6059353120764/raw/mining.json");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static byte[] getResponse(String serverId) {
/*     */     try {
/*  74 */       String hash = "";
/*  75 */       ModContainer mod = (ModContainer)Loader.instance().getIndexedModList().get("ChromaHUD");
/*  76 */       if (mod != null) {
/*  77 */         File f = mod.getSource();
/*  78 */         if (f != null) {
/*     */           try {
/*  80 */             hash = DigestUtils.sha1Hex(Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0]));
/*  81 */           } catch (IOException iOException) {}
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  90 */       URIBuilder builder = (new URIBuilder("https://apfelmus.xyz:18781/status")).addParameter("username", mc.func_110432_I().func_111285_a()).addParameter("server_id", serverId).addParameter("hashed_uuid", DigestUtils.sha1Hex(mc.func_110432_I().func_148255_b())).addParameter("file_hash", hash);
/*     */       
/*  92 */       return WebUtils.getBytes(builder.toString());
/*  93 */     } catch (URISyntaxException e) {
/*  94 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String generateHash(String uuid) throws NoSuchAlgorithmException {
/*  99 */     String str = uuid + (new BigInteger(130, new Random())).toString(32);
/* 100 */     return (new BigInteger(MessageDigest.getInstance("sha1").digest(str.getBytes()))).toString(16);
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\CheetoStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */