/*    */ package xyz.apfelmus.cheeto.client.utils.pathfinding;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.util.Vec3i;
/*    */ 
/*    */ public class Pathfinder {
/*    */   private static AStarCustomPathfinder pathfinder;
/*    */   public static List<Vec3> path;
/*    */   
/*    */   public static void setup(BlockPos from, BlockPos to, double minDistance) {
/* 13 */     pathfinder = new AStarCustomPathfinder(new Vec3((Vec3i)from), new Vec3((Vec3i)to), minDistance);
/* 14 */     pathfinder.compute();
/* 15 */     path = pathfinder.getPath();
/*    */   }
/*    */   
/*    */   public static Vec3 getCurrent() {
/* 19 */     if (path != null && !path.isEmpty()) {
/* 20 */       return path.get(0);
/*    */     }
/*    */     
/* 23 */     return null;
/*    */   }
/*    */   
/*    */   public static boolean hasNext() {
/* 27 */     return (path != null && path.size() > 1);
/*    */   }
/*    */   
/*    */   public static Vec3 getNext() {
/* 31 */     return path.get(1);
/*    */   }
/*    */   
/*    */   public static boolean goNext() {
/* 35 */     if (path != null && path.size() > 1) {
/* 36 */       path.remove(0);
/* 37 */       return true;
/*    */     } 
/* 39 */     path = null;
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean hasPath() {
/* 45 */     return (path != null && !path.isEmpty());
/*    */   }
/*    */   
/*    */   public static Vec3 getGoal() {
/* 49 */     return path.get(path.size() - 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\pathfinding\Pathfinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */