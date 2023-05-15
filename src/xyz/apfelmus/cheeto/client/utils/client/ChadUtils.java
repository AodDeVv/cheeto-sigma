/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.MouseHelper;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ public class ChadUtils {
/*  8 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   public static boolean isUngrabbed = false;
/*    */   private static MouseHelper oldMouseHelper;
/*    */   private static boolean doesGameWantUngrab = true;
/* 12 */   private static int oldRenderDist = 0;
/* 13 */   private static int oldFpsCap = 0;
/*    */   private static boolean improving = false;
/*    */   
/*    */   public static void ungrabMouse() {
/* 17 */     if (!mc.field_71415_G || isUngrabbed) {
/*    */       return;
/*    */     }
/* 20 */     if (oldMouseHelper == null) {
/* 21 */       oldMouseHelper = mc.field_71417_B;
/*    */     }
/* 23 */     mc.field_71474_y.field_82881_y = false;
/*    */     
/* 25 */     doesGameWantUngrab = !Mouse.isGrabbed();
/* 26 */     oldMouseHelper.func_74373_b();
/* 27 */     mc.field_71415_G = true;
/* 28 */     mc.field_71417_B = new MouseHelper()
/*    */       {
/*    */         public void func_74374_c() {}
/*    */         
/*    */         public void func_74372_a() {
/* 33 */           ChadUtils.doesGameWantUngrab = false;
/*    */         }
/*    */         
/*    */         public void func_74373_b() {
/* 37 */           ChadUtils.doesGameWantUngrab = true;
/*    */         }
/*    */       };
/* 40 */     isUngrabbed = true;
/*    */   }
/*    */   
/*    */   public static void regrabMouse() {
/* 44 */     if (!isUngrabbed) {
/*    */       return;
/*    */     }
/* 47 */     isUngrabbed = false;
/* 48 */     mc.field_71417_B = oldMouseHelper;
/* 49 */     if (!doesGameWantUngrab)
/* 50 */       mc.field_71417_B.func_74372_a(); 
/* 51 */     oldMouseHelper = null;
/*    */   }
/*    */   
/*    */   public static void improveCpuUsage() {
/* 55 */     if (!improving) {
/* 56 */       oldRenderDist = mc.field_71474_y.field_151451_c;
/* 57 */       oldFpsCap = mc.field_71474_y.field_74350_i;
/* 58 */       mc.field_71474_y.field_151451_c = 2;
/* 59 */       mc.field_71474_y.field_74350_i = 30;
/* 60 */       improving = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void revertCpuUsage() {
/* 65 */     mc.field_71474_y.field_151451_c = oldRenderDist;
/* 66 */     mc.field_71474_y.field_74350_i = oldFpsCap;
/* 67 */     improving = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\ChadUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */