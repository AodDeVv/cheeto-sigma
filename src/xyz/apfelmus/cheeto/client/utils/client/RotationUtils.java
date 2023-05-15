/*     */ package xyz.apfelmus.cheeto.client.utils.client;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import org.lwjgl.util.vector.Vector3f;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.RandomUtil;
/*     */ 
/*     */ public class RotationUtils {
/*  15 */   private static final Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   public static Rotation startRot;
/*     */   public static Rotation neededChange;
/*     */   public static Rotation endRot;
/*     */   public static long startTime;
/*     */   public static long endTime;
/*     */   public static boolean done = true;
/*  23 */   private static final float[][] BLOCK_SIDES = new float[][] { { 0.5F, 0.01F, 0.5F }, { 0.5F, 0.99F, 0.5F }, { 0.01F, 0.5F, 0.5F }, { 0.99F, 0.5F, 0.5F }, { 0.5F, 0.5F, 0.01F }, { 0.5F, 0.5F, 0.99F } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Rotation getRotation(Vec3 vec) {
/*  33 */     Vec3 eyes = mc.field_71439_g.func_174824_e(1.0F);
/*     */     
/*  35 */     return getRotation(eyes, vec);
/*     */   }
/*     */   
/*     */   public static Rotation getRotation(Vec3 from, Vec3 to) {
/*  39 */     double diffX = to.field_72450_a - from.field_72450_a;
/*  40 */     double diffY = to.field_72448_b - from.field_72448_b;
/*  41 */     double diffZ = to.field_72449_c - from.field_72449_c;
/*     */     
/*  43 */     return new Rotation(
/*  44 */         MathHelper.func_76142_g((float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D)), 
/*  45 */         (float)-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Rotation getRotation(BlockPos bp) {
/*  50 */     Vec3 vec = new Vec3(bp.func_177958_n() + 0.5D, bp.func_177956_o() + 0.5D, bp.func_177952_p() + 0.5D);
/*     */     
/*  52 */     return getRotation(vec);
/*     */   }
/*     */   
/*     */   public static void setup(Rotation rot, Long aimTime) {
/*  56 */     done = false;
/*  57 */     startRot = new Rotation(mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A);
/*  58 */     neededChange = getNeededChange(startRot, rot);
/*  59 */     endRot = new Rotation(startRot.getYaw() + neededChange.getYaw(), startRot.getPitch() + neededChange.getPitch());
/*  60 */     startTime = System.currentTimeMillis();
/*  61 */     endTime = System.currentTimeMillis() + aimTime.longValue();
/*     */   }
/*     */   
/*     */   public static void reset() {
/*  65 */     done = true;
/*  66 */     startRot = null;
/*  67 */     neededChange = null;
/*  68 */     endRot = null;
/*  69 */     startTime = 0L;
/*  70 */     endTime = 0L;
/*     */   }
/*     */   
/*     */   public static void update() {
/*  74 */     if (System.currentTimeMillis() <= endTime) {
/*  75 */       mc.field_71439_g.field_70177_z = interpolate(startRot.getYaw(), endRot.getYaw());
/*  76 */       mc.field_71439_g.field_70125_A = interpolate(startRot.getPitch(), endRot.getPitch());
/*     */     }
/*  78 */     else if (!done) {
/*  79 */       mc.field_71439_g.field_70177_z = endRot.getYaw();
/*  80 */       mc.field_71439_g.field_70125_A = endRot.getPitch();
/*  81 */       reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void snapAngles(Rotation rot) {
/*  87 */     mc.field_71439_g.field_70177_z = rot.getYaw();
/*  88 */     mc.field_71439_g.field_70125_A = rot.getPitch();
/*     */   }
/*     */   
/*     */   private static float interpolate(float start, float end) {
/*  92 */     float spentMillis = (float)(System.currentTimeMillis() - startTime);
/*  93 */     float relativeProgress = spentMillis / (float)(endTime - startTime);
/*  94 */     return (end - start) * easeOutCubic(relativeProgress) + start;
/*     */   }
/*     */   
/*     */   public static float easeOutCubic(double number) {
/*  98 */     return (float)Math.max(0.0D, Math.min(1.0D, 1.0D - Math.pow(1.0D - number, 3.0D)));
/*     */   }
/*     */   
/*     */   public static Rotation getNeededChange(Rotation startRot, Rotation endRot) {
/* 102 */     float yawChng = MathHelper.func_76142_g(endRot.getYaw()) - MathHelper.func_76142_g(startRot.getYaw());
/*     */     
/* 104 */     if (yawChng <= -180.0F) {
/* 105 */       yawChng = 360.0F + yawChng;
/* 106 */     } else if (yawChng > 180.0F) {
/* 107 */       yawChng = -360.0F + yawChng;
/*     */     } 
/*     */     
/* 110 */     if (!VersionCheck.nope) {
/* 111 */       if (yawChng < 0.0F) {
/* 112 */         yawChng += 360.0F;
/*     */       } else {
/* 114 */         yawChng -= 360.0F;
/*     */       } 
/*     */     }
/*     */     
/* 118 */     return new Rotation(yawChng, endRot
/*     */         
/* 120 */         .getPitch() - startRot.getPitch());
/*     */   }
/*     */ 
/*     */   
/*     */   public static double fovFromEntity(Entity en) {
/* 125 */     return ((mc.field_71439_g.field_70177_z - fovToEntity(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
/*     */   }
/*     */   
/*     */   public static double fovFromVec3(Vec3 vec) {
/* 129 */     return ((mc.field_71439_g.field_70177_z - fovToVec3(vec)) % 360.0D + 540.0D) % 360.0D - 180.0D;
/*     */   }
/*     */   
/*     */   public static float fovToVec3(Vec3 vec) {
/* 133 */     double x = vec.field_72450_a - mc.field_71439_g.field_70165_t;
/* 134 */     double z = vec.field_72449_c - mc.field_71439_g.field_70161_v;
/* 135 */     double yaw = Math.atan2(x, z) * 57.2957795D;
/* 136 */     return (float)(yaw * -1.0D);
/*     */   }
/*     */   
/*     */   public static float fovToEntity(Entity ent) {
/* 140 */     double x = ent.field_70165_t - mc.field_71439_g.field_70165_t;
/* 141 */     double z = ent.field_70161_v - mc.field_71439_g.field_70161_v;
/* 142 */     double yaw = Math.atan2(x, z) * 57.2957795D;
/* 143 */     return (float)(yaw * -1.0D);
/*     */   }
/*     */   
/*     */   public static Rotation getNeededChange(Rotation endRot) {
/* 147 */     Rotation startRot = new Rotation(mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A);
/* 148 */     return getNeededChange(startRot, endRot);
/*     */   }
/*     */   
/*     */   public static List<Vec3> getBlockSides(BlockPos bp) {
/* 152 */     List<Vec3> ret = new ArrayList<>();
/*     */     
/* 154 */     for (float[] side : BLOCK_SIDES) {
/* 155 */       ret.add((new Vec3((Vec3i)bp)).func_72441_c(side[0], side[1], side[2]));
/*     */     }
/*     */     
/* 158 */     return ret;
/*     */   }
/*     */   
/*     */   public static boolean lookingAt(BlockPos blockPos, float range) {
/* 162 */     float stepSize = 0.15F;
/* 163 */     Vec3 position = new Vec3(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
/* 164 */     Vec3 look = mc.field_71439_g.func_70676_i(0.0F);
/* 165 */     Vector3f step = new Vector3f((float)look.field_72450_a, (float)look.field_72448_b, (float)look.field_72449_c);
/* 166 */     step.scale(stepSize / step.length());
/* 167 */     for (int i = 0; i < Math.floor((range / stepSize)) - 2.0D; i++) {
/* 168 */       BlockPos blockAtPos = new BlockPos(position.field_72450_a, position.field_72448_b, position.field_72449_c);
/* 169 */       if (blockAtPos.equals(blockPos))
/* 170 */         return true; 
/* 171 */       position = position.func_178787_e(new Vec3(step.x, step.y, step.z));
/*     */     } 
/* 173 */     return false;
/*     */   }
/*     */   
/*     */   public static Vec3 getVectorForRotation(float pitch, float yaw) {
/* 177 */     float f2 = -MathHelper.func_76134_b(-pitch * 0.017453292F);
/* 178 */     return new Vec3((MathHelper.func_76126_a(-yaw * 0.017453292F - 3.1415927F) * f2), MathHelper.func_76126_a(-pitch * 0.017453292F), (MathHelper.func_76134_b(-yaw * 0.017453292F - 3.1415927F) * f2));
/*     */   }
/*     */   
/*     */   public static Vec3 getLook(Vec3 vec) {
/* 182 */     double diffX = vec.field_72450_a - mc.field_71439_g.field_70165_t;
/* 183 */     double diffY = vec.field_72448_b - mc.field_71439_g.field_70163_u + mc.field_71439_g.func_70047_e();
/* 184 */     double diffZ = vec.field_72449_c - mc.field_71439_g.field_70161_v;
/* 185 */     double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
/* 186 */     return getVectorForRotation((float)-(MathHelper.func_181159_b(diffY, dist) * 180.0D / Math.PI), (float)(MathHelper.func_181159_b(diffZ, diffX) * 180.0D / Math.PI - 90.0D));
/*     */   }
/*     */   
/*     */   public static EnumFacing calculateEnumfacing(Vec3 pos) {
/* 190 */     int x = MathHelper.func_76128_c(pos.field_72450_a);
/* 191 */     int y = MathHelper.func_76128_c(pos.field_72448_b);
/* 192 */     int z = MathHelper.func_76128_c(pos.field_72449_c);
/* 193 */     MovingObjectPosition position = calculateIntercept(new AxisAlignedBB(x, y, z, (x + 1), (y + 1), (z + 1)), pos, 50.0F);
/* 194 */     return (position != null) ? position.field_178784_b : null;
/*     */   }
/*     */   
/*     */   public static MovingObjectPosition calculateIntercept(AxisAlignedBB aabb, Vec3 block, float range) {
/* 198 */     Vec3 vec3 = mc.field_71439_g.func_174824_e(1.0F);
/* 199 */     Vec3 vec4 = getLook(block);
/* 200 */     return aabb.func_72327_a(vec3, vec3.func_72441_c(vec4.field_72450_a * range, vec4.field_72448_b * range, vec4.field_72449_c * range));
/*     */   }
/*     */   
/*     */   public static List<Vec3> getPointsOnBlock(BlockPos bp) {
/* 204 */     List<Vec3> ret = new ArrayList<>();
/*     */     
/* 206 */     for (float[] side : BLOCK_SIDES) {
/* 207 */       for (int i = 0; i < 20; i++) {
/* 208 */         float x = side[0];
/* 209 */         float y = side[1];
/* 210 */         float z = side[2];
/*     */         
/* 212 */         if (x == 0.5D) x = RandomUtil.randBetween(0.1F, 0.9F); 
/* 213 */         if (y == 0.5D) y = RandomUtil.randBetween(0.1F, 0.9F); 
/* 214 */         if (z == 0.5D) z = RandomUtil.randBetween(0.1F, 0.9F);
/*     */         
/* 216 */         ret.add((new Vec3((Vec3i)bp)).func_72441_c(x, y, z));
/*     */       } 
/*     */     } 
/*     */     
/* 220 */     return ret;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\RotationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */