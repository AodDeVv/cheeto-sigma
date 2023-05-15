/*    */ package xyz.apfelmus.cheeto.client.utils.math;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.Vec3;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*    */ 
/*    */ public class VecUtils
/*    */ {
/* 16 */   private static Minecraft mc = Minecraft.func_71410_x();
/* 17 */   private static Map<Integer, KeyBinding> keyBindMap = new HashMap<Integer, KeyBinding>()
/*    */     {
/*    */     
/*    */     };
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<KeyBinding> getNeededKeyPresses(Vec3 from, Vec3 to) {
/* 25 */     List<KeyBinding> damnIThinkIShouldHaveRatherUsed4SwitchCasesToDetermineTheNeededKeyPresses = new ArrayList<>();
/*    */     
/* 27 */     Rotation neededRot = RotationUtils.getNeededChange(RotationUtils.getRotation(from, to));
/* 28 */     double neededYaw = (neededRot.getYaw() * -1.0F);
/*    */     
/* 30 */     keyBindMap.forEach((k, v) -> {
/*    */           if (Math.abs(k.intValue() - neededYaw) < 67.5D || Math.abs(k.intValue() - neededYaw + 360.0D) < 67.5D) {
/*    */             damnIThinkIShouldHaveRatherUsed4SwitchCasesToDetermineTheNeededKeyPresses.add(v);
/*    */           }
/*    */         });
/*    */     
/* 36 */     return damnIThinkIShouldHaveRatherUsed4SwitchCasesToDetermineTheNeededKeyPresses;
/*    */   }
/*    */   
/*    */   public static Vec3 floorVec(Vec3 vec3) {
/* 40 */     return new Vec3(Math.floor(vec3.field_72450_a), Math.floor(vec3.field_72448_b), Math.floor(vec3.field_72449_c));
/*    */   }
/*    */   
/*    */   public static Vec3 ceilVec(Vec3 vec3) {
/* 44 */     return new Vec3(Math.ceil(vec3.field_72450_a), Math.ceil(vec3.field_72448_b), Math.ceil(vec3.field_72449_c));
/*    */   }
/*    */   
/*    */   public static double getHorizontalDistance(Vec3 vec1, Vec3 vec2) {
/* 48 */     double d0 = vec1.field_72450_a - vec2.field_72450_a;
/* 49 */     double d2 = vec1.field_72449_c - vec2.field_72449_c;
/* 50 */     return MathHelper.func_76133_a(d0 * d0 + d2 * d2);
/*    */   }
/*    */   
/*    */   public static List<KeyBinding> getOppositeKeys(List<KeyBinding> kbs) {
/* 54 */     List<KeyBinding> ret = new ArrayList<>();
/*    */     
/* 56 */     keyBindMap.forEach((k, v) -> {
/*    */           if (kbs.stream().anyMatch(())) {
/*    */             ret.add(keyBindMap.get(Integer.valueOf((k.intValue() + 180) % 360)));
/*    */           }
/*    */         });
/*    */     
/* 62 */     return ret;
/*    */   }
/*    */   
/*    */   public static Vec3 times(Vec3 vec, float mult) {
/* 66 */     return new Vec3(vec.field_72450_a * mult, vec.field_72448_b * mult, vec.field_72449_c * mult);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\math\VecUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */