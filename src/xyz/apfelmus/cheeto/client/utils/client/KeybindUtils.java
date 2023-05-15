/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ 
/*    */ public class KeybindUtils
/*    */ {
/* 10 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   
/*    */   private static Method clickMouse;
/*    */   private static Method rightClickMouse;
/*    */   
/*    */   public static void setup() {
/*    */     try {
/* 17 */       clickMouse = Minecraft.class.getDeclaredMethod("clickMouse", new Class[0]);
/* 18 */     } catch (NoSuchMethodException e) {
/*    */       try {
/* 20 */         clickMouse = Minecraft.class.getDeclaredMethod("func_147116_af", new Class[0]);
/* 21 */       } catch (NoSuchMethodException ex) {
/* 22 */         ex.printStackTrace();
/*    */       } 
/*    */     } 
/*    */     
/*    */     try {
/* 27 */       rightClickMouse = Minecraft.class.getDeclaredMethod("rightClickMouse", new Class[0]);
/* 28 */     } catch (NoSuchMethodException e) {
/*    */       try {
/* 30 */         rightClickMouse = Minecraft.class.getDeclaredMethod("func_147121_ag", new Class[0]);
/* 31 */       } catch (NoSuchMethodException e1) {
/* 32 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */     
/* 36 */     if (clickMouse != null) {
/* 37 */       clickMouse.setAccessible(true);
/*    */     }
/*    */     
/* 40 */     if (rightClickMouse != null) {
/* 41 */       rightClickMouse.setAccessible(true);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void leftClick() {
/*    */     try {
/* 47 */       clickMouse.invoke(Minecraft.func_71410_x(), new Object[0]);
/* 48 */     } catch (InvocationTargetException|IllegalAccessException e) {
/* 49 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void rightClick() {
/*    */     try {
/* 55 */       rightClickMouse.invoke(Minecraft.func_71410_x(), new Object[0]);
/* 56 */     } catch (IllegalAccessException|InvocationTargetException illegalAccessException) {}
/*    */   }
/*    */ 
/*    */   
/*    */   public static void stopMovement() {
/* 61 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), false);
/* 62 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), false);
/* 63 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74351_w.func_151463_i(), false);
/* 64 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74368_y.func_151463_i(), false);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\KeybindUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */