/*    */ package xyz.apfelmus.cheeto.client.utils.math;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class RandomUtil
/*    */ {
/*  8 */   private static Random rand = new Random();
/*    */   
/*    */   public static Vec3 randomVec() {
/* 11 */     return new Vec3(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
/*    */   }
/*    */   
/*    */   public static int randBetween(int a, int b) {
/* 15 */     return rand.nextInt(b - a + 1) + a;
/*    */   }
/*    */   
/*    */   public static double randBetween(double a, double b) {
/* 19 */     return rand.nextDouble() * (b - a) + a;
/*    */   }
/*    */   
/*    */   public static float randBetween(float a, float b) {
/* 23 */     return rand.nextFloat() * (b - a) + a;
/*    */   }
/*    */   
/*    */   public static int nextInt(int yep) {
/* 27 */     return rand.nextInt(yep);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\math\RandomUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */