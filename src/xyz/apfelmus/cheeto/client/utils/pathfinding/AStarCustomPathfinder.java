/*     */ package xyz.apfelmus.cheeto.client.utils.pathfinding;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.VecUtils;
/*     */ 
/*     */ public class AStarCustomPathfinder {
/*     */   private Vec3 startVec3;
/*  17 */   private ArrayList<Vec3> path = new ArrayList<>(); private Vec3 endVec3;
/*  18 */   private ArrayList<Hub> hubs = new ArrayList<>();
/*  19 */   private ArrayList<Hub> hubsToWork = new ArrayList<>();
/*     */   
/*     */   private double minDistanceSquared;
/*     */   private boolean nearest = true;
/*  23 */   private static Vec3[] flatCardinalDirections = new Vec3[] { new Vec3(1.0D, 0.0D, 0.0D), new Vec3(-1.0D, 0.0D, 0.0D), new Vec3(0.0D, 0.0D, 1.0D), new Vec3(0.0D, 0.0D, -1.0D) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   public AStarCustomPathfinder(Vec3 startVec3, Vec3 endVec3, double minDistanceSquared) {
/*  33 */     this.startVec3 = VecUtils.floorVec(startVec3);
/*  34 */     this.endVec3 = VecUtils.floorVec(endVec3);
/*  35 */     this.minDistanceSquared = minDistanceSquared;
/*     */   }
/*     */   
/*     */   public ArrayList<Vec3> getPath() {
/*  39 */     return this.path;
/*     */   }
/*     */   
/*     */   public void compute() {
/*  43 */     compute(1000, 4);
/*     */   }
/*     */   
/*     */   public void compute(int loops, int depth) {
/*  47 */     this.path.clear();
/*  48 */     this.hubsToWork.clear();
/*  49 */     ArrayList<Vec3> initPath = new ArrayList<>();
/*  50 */     initPath.add(this.startVec3);
/*  51 */     this.hubsToWork.add(new Hub(this.startVec3, null, initPath, this.startVec3.func_72436_e(this.endVec3), 0.0D, 0.0D));
/*     */     int i;
/*  53 */     label63: for (i = 0; i < loops; i++) {
/*  54 */       Collections.sort(this.hubsToWork, new CompareHub());
/*  55 */       int j = 0;
/*  56 */       if (this.hubsToWork.size() == 0) {
/*     */         break;
/*     */       }
/*  59 */       for (Hub hub : new ArrayList(this.hubsToWork)) {
/*  60 */         j++;
/*  61 */         if (j > depth) {
/*     */           break;
/*     */         }
/*  64 */         this.hubsToWork.remove(hub);
/*  65 */         this.hubs.add(hub);
/*     */         
/*  67 */         for (Vec3 direction : flatCardinalDirections) {
/*  68 */           Vec3 loc = VecUtils.ceilVec(hub.getLoc().func_178787_e(direction));
/*  69 */           if (checkPositionValidity(loc, true)) {
/*  70 */             if (isSlab(loc.func_72441_c(0.0D, -1.0D, 0.0D), BlockSlab.EnumBlockHalf.BOTTOM)) {
/*  71 */               if (addHub(hub, loc.func_72441_c(0.0D, -0.5D, 0.0D), 0.0D)) {
/*     */                 break label63;
/*     */               }
/*     */             }
/*  75 */             else if (addHub(hub, loc, 0.0D)) {
/*     */               break label63;
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  82 */         for (Vec3 direction : flatCardinalDirections) {
/*  83 */           Vec3 loc = VecUtils.ceilVec(hub.getLoc().func_178787_e(direction).func_72441_c(0.0D, 1.0D, 0.0D));
/*  84 */           if (checkPositionValidity(loc, true) && checkPositionValidity(hub.getLoc().func_72441_c(0.0D, 1.0D, 0.0D), false)) {
/*  85 */             if (isSlab(loc.func_72441_c(0.0D, -1.0D, 0.0D), BlockSlab.EnumBlockHalf.BOTTOM)) {
/*  86 */               if (addHub(hub, loc.func_72441_c(0.0D, -0.5D, 0.0D), 0.0D)) {
/*     */                 break label63;
/*     */               }
/*  89 */             } else if (!isSlab(hub.getLoc(), BlockSlab.EnumBlockHalf.BOTTOM) && 
/*  90 */               addHub(hub, loc, 0.0D)) {
/*     */               break label63;
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  97 */         for (Vec3 direction : flatCardinalDirections) {
/*  98 */           Vec3 loc = VecUtils.ceilVec(hub.getLoc().func_178787_e(direction).func_72441_c(0.0D, -1.0D, 0.0D));
/*  99 */           if (checkPositionValidity(loc, true) && checkPositionValidity(loc.func_72441_c(0.0D, 1.0D, 0.0D), false)) {
/* 100 */             if (isSlab(loc, BlockSlab.EnumBlockHalf.BOTTOM)) {
/* 101 */               if (addHub(hub, loc.func_72441_c(0.0D, 0.5D, 0.0D), 0.0D)) {
/*     */                 break label63;
/*     */               }
/*     */             }
/* 105 */             else if (addHub(hub, loc, 0.0D)) {
/*     */               break label63;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 114 */     if (this.nearest) {
/* 115 */       this.hubs.sort(new CompareHub());
/* 116 */       this.path = ((Hub)this.hubs.get(0)).getPath();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean checkPositionValidity(Vec3 loc, boolean checkGround) {
/* 121 */     return checkPositionValidity((int)loc.field_72450_a, (int)loc.field_72448_b, (int)loc.field_72449_c, checkGround);
/*     */   }
/*     */   
/*     */   public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
/* 125 */     BlockPos block1 = new BlockPos(x, y, z);
/* 126 */     BlockPos block2 = new BlockPos(x, y + 1, z);
/* 127 */     BlockPos block3 = new BlockPos(x, y - 1, z);
/* 128 */     return (!isBlockSolid(block1) && !isBlockSolid(block2) && (isBlockSolid(block3) || !checkGround) && isSafeToWalkOn(block3));
/*     */   }
/*     */   
/*     */   public static boolean isSlab(Vec3 loc, BlockSlab.EnumBlockHalf slabType) {
/* 132 */     IBlockState bs = mc.field_71441_e.func_180495_p(new BlockPos(loc));
/* 133 */     return (bs.func_177230_c() instanceof BlockSlab && bs.func_177229_b((IProperty)BlockSlab.field_176554_a) == slabType);
/*     */   }
/*     */   
/*     */   private static boolean isBlockSolid(BlockPos block) {
/* 137 */     IBlockState bs = mc.field_71441_e.func_180495_p(block);
/*     */     
/* 139 */     if (bs != null) {
/* 140 */       Block b = bs.func_177230_c();
/*     */       
/* 142 */       return (mc.field_71441_e.func_175665_u(block) || b instanceof BlockSlab || b instanceof net.minecraft.block.BlockStairs || b instanceof net.minecraft.block.BlockCactus || b instanceof net.minecraft.block.BlockChest || b instanceof net.minecraft.block.BlockEnderChest || b instanceof net.minecraft.block.BlockSkull || b instanceof net.minecraft.block.BlockPane || b instanceof net.minecraft.block.BlockFence || b instanceof net.minecraft.block.BlockWall || b instanceof net.minecraft.block.BlockGlass || b instanceof net.minecraft.block.BlockPistonBase || b instanceof net.minecraft.block.BlockPistonExtension || b instanceof net.minecraft.block.BlockPistonMoving || b instanceof net.minecraft.block.BlockStainedGlass || b instanceof net.minecraft.block.BlockTrapDoor);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isSafeToWalkOn(BlockPos block) {
/* 164 */     IBlockState bs = mc.field_71441_e.func_180495_p(block);
/*     */     
/* 166 */     if (bs != null) {
/* 167 */       Block b = bs.func_177230_c();
/*     */       
/* 169 */       return (!(b instanceof net.minecraft.block.BlockFence) && !(b instanceof net.minecraft.block.BlockWall));
/*     */     } 
/*     */     
/* 172 */     return false;
/*     */   }
/*     */   
/*     */   public Hub isHubExisting(Vec3 loc) {
/* 176 */     for (Hub hub : this.hubs) {
/* 177 */       if ((hub.getLoc()).field_72450_a == loc.field_72450_a && (hub.getLoc()).field_72448_b == loc.field_72448_b && (hub.getLoc()).field_72449_c == loc.field_72449_c) {
/* 178 */         return hub;
/*     */       }
/*     */     } 
/* 181 */     for (Hub hub : this.hubsToWork) {
/* 182 */       if ((hub.getLoc()).field_72450_a == loc.field_72450_a && (hub.getLoc()).field_72448_b == loc.field_72448_b && (hub.getLoc()).field_72449_c == loc.field_72449_c) {
/* 183 */         return hub;
/*     */       }
/*     */     } 
/* 186 */     return null;
/*     */   }
/*     */   
/*     */   public boolean addHub(Hub parent, Vec3 loc, double cost) {
/* 190 */     Hub existingHub = isHubExisting(loc);
/* 191 */     double totalCost = cost;
/* 192 */     if (parent != null) {
/* 193 */       totalCost += parent.getTotalCost();
/*     */     }
/* 195 */     if (existingHub == null) {
/* 196 */       if ((loc.field_72450_a == this.endVec3.field_72450_a && loc.field_72448_b == this.endVec3.field_72448_b && loc.field_72449_c == this.endVec3.field_72449_c) || (this.minDistanceSquared != 0.0D && loc.func_72436_e(this.endVec3) <= this.minDistanceSquared)) {
/* 197 */         this.path.clear();
/* 198 */         this.path = parent.getPath();
/* 199 */         this.path.add(loc);
/* 200 */         return true;
/*     */       } 
/* 202 */       ArrayList<Vec3> path = new ArrayList<>(parent.getPath());
/* 203 */       path.add(loc);
/* 204 */       this.hubsToWork.add(new Hub(loc, parent, path, loc.func_72436_e(this.endVec3), cost, totalCost));
/*     */     }
/* 206 */     else if (existingHub.getCost() > cost) {
/* 207 */       ArrayList<Vec3> path = new ArrayList<>(parent.getPath());
/* 208 */       path.add(loc);
/* 209 */       existingHub.setLoc(loc);
/* 210 */       existingHub.setParent(parent);
/* 211 */       existingHub.setPath(path);
/* 212 */       existingHub.setSquareDistanceToFromTarget(loc.func_72436_e(this.endVec3));
/* 213 */       existingHub.setCost(cost);
/* 214 */       existingHub.setTotalCost(totalCost);
/*     */     } 
/* 216 */     return false;
/*     */   }
/*     */   
/*     */   private class Hub {
/* 220 */     private Vec3 loc = null;
/* 221 */     private Hub parent = null;
/*     */     private ArrayList<Vec3> path;
/*     */     private double squareDistanceToFromTarget;
/*     */     private double cost;
/*     */     private double totalCost;
/*     */     
/*     */     public Hub(Vec3 loc, Hub parent, ArrayList<Vec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
/* 228 */       this.loc = loc;
/* 229 */       this.parent = parent;
/* 230 */       this.path = path;
/* 231 */       this.squareDistanceToFromTarget = squareDistanceToFromTarget;
/* 232 */       this.cost = cost;
/* 233 */       this.totalCost = totalCost;
/*     */     }
/*     */     
/*     */     public Vec3 getLoc() {
/* 237 */       return this.loc;
/*     */     }
/*     */     
/*     */     public Hub getParent() {
/* 241 */       return this.parent;
/*     */     }
/*     */     
/*     */     public ArrayList<Vec3> getPath() {
/* 245 */       return this.path;
/*     */     }
/*     */     
/*     */     public double getSquareDistanceToFromTarget() {
/* 249 */       return this.squareDistanceToFromTarget;
/*     */     }
/*     */     
/*     */     public double getCost() {
/* 253 */       return this.cost;
/*     */     }
/*     */     
/*     */     public void setLoc(Vec3 loc) {
/* 257 */       this.loc = loc;
/*     */     }
/*     */     
/*     */     public void setParent(Hub parent) {
/* 261 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void setPath(ArrayList<Vec3> path) {
/* 265 */       this.path = path;
/*     */     }
/*     */     
/*     */     public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
/* 269 */       this.squareDistanceToFromTarget = squareDistanceToFromTarget;
/*     */     }
/*     */     
/*     */     public void setCost(double cost) {
/* 273 */       this.cost = cost;
/*     */     }
/*     */     
/*     */     public double getTotalCost() {
/* 277 */       return this.totalCost;
/*     */     }
/*     */     
/*     */     public void setTotalCost(double totalCost) {
/* 281 */       this.totalCost = totalCost;
/*     */     }
/*     */   }
/*     */   
/*     */   public class CompareHub
/*     */     implements Comparator<Hub> {
/*     */     public int compare(AStarCustomPathfinder.Hub o1, AStarCustomPathfinder.Hub o2) {
/* 288 */       return 
/* 289 */         (int)(o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - o2.getSquareDistanceToFromTarget() + o2.getTotalCost());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\pathfinding\AStarCustomPathfinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */