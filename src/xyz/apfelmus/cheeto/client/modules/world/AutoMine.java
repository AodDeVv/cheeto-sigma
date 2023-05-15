/*     */ package xyz.apfelmus.cheeto.client.modules.world;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.RandomUtil;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.TimeHelper;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render3DUtils;
/*     */ 
/*     */ @Module(name = "AutoMine", category = Category.WORLD)
/*     */ public class AutoMine implements Runnable {
/*     */   @Setting(name = "Sneak", description = "Makes the player sneak while mining")
/*  33 */   private BooleanSetting sneak = new BooleanSetting(false);
/*     */   @Setting(name = "CoalOre")
/*  35 */   private BooleanSetting coalOre = new BooleanSetting(true);
/*     */   @Setting(name = "LapisOre")
/*  37 */   private BooleanSetting lapisOre = new BooleanSetting(true);
/*     */   @Setting(name = "IronOre")
/*  39 */   private BooleanSetting ironOre = new BooleanSetting(true);
/*     */   @Setting(name = "GoldOre")
/*  41 */   private BooleanSetting goldOre = new BooleanSetting(true);
/*     */   @Setting(name = "RedstoneOre")
/*  43 */   private BooleanSetting redstoneOre = new BooleanSetting(true);
/*     */   @Setting(name = "DiamondOre")
/*  45 */   private BooleanSetting diamondOre = new BooleanSetting(true);
/*     */   @Setting(name = "EmeraldOre")
/*  47 */   private BooleanSetting emeraldOre = new BooleanSetting(true);
/*     */   @Setting(name = "GoldBlocks")
/*  49 */   private BooleanSetting goldBlocks = new BooleanSetting(true);
/*     */   @Setting(name = "LookTime")
/*  51 */   private IntegerSetting lookTime = new IntegerSetting(
/*  52 */       Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(2500)); @Setting(name = "MaxMineTime", description = "Set to slightly more than it takes to mine")
/*  53 */   private IntegerSetting maxMineTime = new IntegerSetting(
/*  54 */       Integer.valueOf(5000), Integer.valueOf(0), Integer.valueOf(10000));
/*     */   
/*  56 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   private Thread thread;
/*  58 */   private Map<BlockPos, List<Vec3>> blocksNear = new HashMap<>();
/*  59 */   private List<BlockPos> blacklist = new ArrayList<>();
/*  60 */   private int delayMs = 500;
/*     */   private BlockPos curBlockPos;
/*     */   private Block curBlock;
/*     */   private TimeHelper mineTimer;
/*     */   private Vec3 startRot;
/*     */   private Vec3 endRot;
/*     */   
/*     */   enum MineState {
/*  68 */     CHOOSE,
/*  69 */     LOOK,
/*  70 */     MINE;
/*     */   }
/*     */   
/*  73 */   private MineState mineState = MineState.CHOOSE;
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  77 */     this.mineState = MineState.CHOOSE;
/*  78 */     this.blocksNear.clear();
/*  79 */     this.blacklist.clear();
/*  80 */     this.mineTimer = new TimeHelper();
/*  81 */     this.curBlockPos = null;
/*  82 */     this.curBlock = null;
/*  83 */     this.startRot = null;
/*  84 */     this.endRot = null;
/*  85 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), this.sneak.isEnabled());
/*     */   }
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/*  90 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74312_F.func_151463_i(), false);
/*  91 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
/*  92 */     mc.field_71442_b.func_78767_c();
/*     */   } @Event
/*     */   public void onTick(ClientTickEvent event) {
/*     */     AutoMithril.BlockPosWithVec closest;
/*     */     IBlockState ibs;
/*  97 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/*     */       return; 
/*  99 */     if (this.thread == null || !this.thread.isAlive()) {
/* 100 */       (this.thread = new Thread(this)).setDaemon(false);
/* 101 */       this.thread.setPriority(1);
/* 102 */       this.thread.start();
/*     */     } 
/*     */     
/* 105 */     Iterator<Map.Entry<BlockPos, List<Vec3>>> it = this.blocksNear.entrySet().iterator();
/*     */     
/* 107 */     while (it.hasNext()) {
/* 108 */       Map.Entry<BlockPos, List<Vec3>> entry = it.next();
/* 109 */       Vec3 randPoint = ((List<Vec3>)entry.getValue()).get(RandomUtil.randBetween(0, ((List)entry.getValue()).size() - 1));
/* 110 */       MovingObjectPosition mop = mc.field_71441_e.func_72933_a(mc.field_71439_g.func_174824_e(1.0F), randPoint);
/*     */       
/* 112 */       if (mop != null && mop.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
/* 113 */         if (!mop.func_178782_a().equals(entry.getKey()) || randPoint.func_72438_d(mc.field_71439_g.func_174824_e(1.0F)) >= mc.field_71442_b.func_78757_d())
/* 114 */           it.remove(); 
/*     */         continue;
/*     */       } 
/* 117 */       it.remove();
/*     */     } 
/*     */ 
/*     */     
/* 121 */     if (!this.blocksNear.containsKey(this.curBlockPos)) {
/* 122 */       this.mineState = MineState.CHOOSE;
/*     */     }
/*     */     
/* 125 */     switch (this.mineState) {
/*     */       case CHOOSE:
/* 127 */         closest = getClosestBlock(null);
/*     */         
/* 129 */         if (closest != null) {
/* 130 */           this.curBlockPos = closest.getBlockPos();
/* 131 */           IBlockState iBlockState = mc.field_71441_e.func_180495_p(this.curBlockPos);
/* 132 */           if (iBlockState != null) {
/* 133 */             this.curBlock = iBlockState.func_177230_c();
/*     */           }
/* 135 */           this.startRot = closest.getVec3();
/* 136 */           this.endRot = null;
/* 137 */           RotationUtils.setup(RotationUtils.getRotation(closest.getVec3()), Long.valueOf(this.lookTime.getCurrent().intValue()));
/* 138 */           this.mineState = MineState.LOOK; break;
/*     */         } 
/* 140 */         KeyBinding.func_74510_a(mc.field_71474_y.field_74312_F.func_151463_i(), false);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case MINE:
/* 146 */         if (this.mineTimer.hasReached(this.maxMineTime.getCurrent().intValue())) {
/* 147 */           this.blocksNear.remove(this.curBlockPos);
/* 148 */           this.mineState = MineState.CHOOSE;
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 153 */         ibs = mc.field_71441_e.func_180495_p(this.curBlockPos);
/* 154 */         if (ibs != null) {
/* 155 */           if ((this.curBlock != null && ibs.func_177230_c() != this.curBlock) || ibs.func_177230_c() == Blocks.field_150357_h || ibs.func_177230_c() == Blocks.field_150350_a) {
/* 156 */             this.blocksNear.remove(this.curBlockPos);
/* 157 */             this.mineState = MineState.CHOOSE;
/* 158 */             this.curBlock = null;
/* 159 */             this.startRot = null;
/* 160 */             this.endRot = null;
/*     */             break;
/*     */           } 
/* 163 */           if (!mc.field_71474_y.field_74312_F.func_151470_d() && mc.field_71462_r == null) {
/* 164 */             mc.field_71415_G = true;
/* 165 */             KeyBinding.func_74507_a(mc.field_71474_y.field_74312_F.func_151463_i());
/* 166 */             KeyBinding.func_74510_a(mc.field_71474_y.field_74312_F.func_151463_i(), true);
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onRender(Render3DEvent event) {
/*     */     AutoMithril.BlockPosWithVec next;
/* 177 */     for (BlockPos bp : new ArrayList(((Map)(new HashMap<>(this.blocksNear)).clone()).keySet())) {
/* 178 */       if (!bp.equals(this.curBlockPos)) {
/* 179 */         Render3DUtils.renderEspBox(bp, event.partialTicks, -16711681);
/*     */       }
/*     */     } 
/*     */     
/* 183 */     if (this.blocksNear.containsKey(this.curBlockPos)) {
/* 184 */       Render3DUtils.renderEspBox(this.curBlockPos, event.partialTicks, -65536);
/*     */     }
/* 186 */     if (this.startRot != null && this.endRot != null) {
/* 187 */       Render3DUtils.drawLine(this.startRot, this.endRot, 1.0F, event.partialTicks);
/*     */     }
/* 189 */     if (this.startRot != null) {
/* 190 */       Render3DUtils.renderSmallBox(this.startRot, -16711936);
/*     */     }
/* 192 */     if (this.endRot != null) {
/* 193 */       Render3DUtils.renderSmallBox(this.endRot, -65536);
/*     */     }
/*     */     
/* 196 */     switch (this.mineState) {
/*     */       case LOOK:
/* 198 */         if (System.currentTimeMillis() <= RotationUtils.endTime) {
/* 199 */           RotationUtils.update(); break;
/*     */         } 
/* 201 */         if (!RotationUtils.done)
/* 202 */           RotationUtils.update(); 
/* 203 */         this.mineTimer.reset();
/*     */         
/* 205 */         next = getClosestBlock(this.curBlockPos);
/*     */         
/* 207 */         if (next != null && this.blocksNear.containsKey(this.curBlockPos)) {
/* 208 */           ((List)this.blocksNear.get(this.curBlockPos)).stream()
/* 209 */             .min(Comparator.comparing(v -> Float.valueOf(RotationUtils.getNeededChange(RotationUtils.getRotation(next.getVec3()), RotationUtils.getRotation(v)).getValue())))
/* 210 */             .ifPresent(nextPointOnSameBlock -> {
/*     */                 this.curBlock = mc.field_71441_e.func_180495_p(this.curBlockPos).func_177230_c();
/*     */                 this.endRot = nextPointOnSameBlock;
/*     */                 RotationUtils.setup(RotationUtils.getRotation(nextPointOnSameBlock), Long.valueOf(this.maxMineTime.getCurrent().intValue()));
/*     */               });
/*     */         }
/* 216 */         this.mineState = MineState.MINE;
/*     */         break;
/*     */ 
/*     */       
/*     */       case MINE:
/* 221 */         if (System.currentTimeMillis() <= RotationUtils.endTime) {
/* 222 */           RotationUtils.update(); break;
/*     */         } 
/* 224 */         this.startRot = null;
/* 225 */         this.endRot = null;
/* 226 */         if (!RotationUtils.done) {
/* 227 */           RotationUtils.update();
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addBlockIfHittable(BlockPos xyz) {
/* 234 */     List<Vec3> pointsOnBlock = RotationUtils.getPointsOnBlock(xyz);
/* 235 */     for (Vec3 point : pointsOnBlock) {
/* 236 */       MovingObjectPosition mop = mc.field_71441_e.func_72933_a(mc.field_71439_g.func_174824_e(1.0F), point);
/*     */       
/* 238 */       if (mop != null && mop.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && 
/* 239 */         mop.func_178782_a().equals(xyz) && point.func_72438_d(mc.field_71439_g.func_174824_e(1.0F)) < mc.field_71442_b.func_78757_d()) {
/* 240 */         if (!this.blocksNear.containsKey(xyz)) {
/* 241 */           this.blocksNear.put(xyz, new ArrayList<>(Collections.singletonList(point))); continue;
/*     */         } 
/* 243 */         ((List<Vec3>)this.blocksNear.get(xyz)).add(point);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private AutoMithril.BlockPosWithVec getClosestBlock(BlockPos excluding) {
/* 251 */     BlockPos closest = null;
/* 252 */     Rotation closestRot = null;
/* 253 */     Vec3 closestPoint = null;
/*     */     
/* 255 */     List<BlockPos> asd = new ArrayList<>(this.blocksNear.keySet());
/* 256 */     asd.remove(excluding);
/*     */     
/* 258 */     for (BlockPos bp : asd) {
/* 259 */       for (Vec3 point : this.blocksNear.get(bp)) {
/* 260 */         Rotation endRot = RotationUtils.getRotation(point);
/* 261 */         Rotation needed = RotationUtils.getNeededChange(endRot);
/*     */         
/* 263 */         if (closestRot != null && needed.getValue() >= closestRot.getValue())
/*     */           continue; 
/* 265 */         closest = bp;
/* 266 */         closestRot = needed;
/* 267 */         closestPoint = point;
/*     */       } 
/*     */     } 
/*     */     
/* 271 */     if (closest != null) {
/* 272 */       return new AutoMithril.BlockPosWithVec(closest, closestPoint);
/*     */     }
/* 274 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 279 */     while (!this.thread.isInterrupted() && mc.field_71439_g != null && mc.field_71441_e != null) {
/* 280 */       if (CF4M.INSTANCE.moduleManager.isEnabled(this)) {
/* 281 */         int radius = 6;
/*     */         
/* 283 */         int px = MathHelper.func_76128_c(mc.field_71439_g.field_70165_t);
/* 284 */         int py = MathHelper.func_76128_c(mc.field_71439_g.field_70163_u + 1.0D);
/* 285 */         int pz = MathHelper.func_76128_c(mc.field_71439_g.field_70161_v);
/* 286 */         Vec3 eyes = mc.field_71439_g.func_174824_e(1.0F);
/*     */         
/* 288 */         for (int x = px - radius; x < px + radius + 1; x++) {
/* 289 */           for (int y = py - radius; y < py + radius + 1; y++) {
/* 290 */             for (int z = pz - radius; z < pz + radius + 1; z++) {
/* 291 */               BlockPos xyz = new BlockPos(x, y, z);
/* 292 */               IBlockState bs = mc.field_71441_e.func_180495_p(xyz);
/*     */               
/* 294 */               if (!this.blocksNear.containsKey(xyz) && !this.blacklist.contains(xyz)) {
/* 295 */                 Block block = bs.func_177230_c();
/* 296 */                 if (Math.sqrt(xyz.func_177957_d(eyes.field_72450_a, eyes.field_72448_b, eyes.field_72449_c)) <= 6.0D) {
/* 297 */                   if (block == Blocks.field_150365_q && this.coalOre.isEnabled()) {
/* 298 */                     addBlockIfHittable(xyz);
/* 299 */                   } else if (block == Blocks.field_150369_x && this.lapisOre.isEnabled()) {
/* 300 */                     addBlockIfHittable(xyz);
/* 301 */                   } else if (block == Blocks.field_150366_p && this.ironOre.isEnabled()) {
/* 302 */                     addBlockIfHittable(xyz);
/* 303 */                   } else if (block == Blocks.field_150352_o && this.goldOre.isEnabled()) {
/* 304 */                     addBlockIfHittable(xyz);
/* 305 */                   } else if ((block == Blocks.field_150450_ax || block == Blocks.field_150439_ay) && this.redstoneOre.isEnabled()) {
/* 306 */                     addBlockIfHittable(xyz);
/* 307 */                   } else if (block == Blocks.field_150482_ag && this.diamondOre.isEnabled()) {
/* 308 */                     addBlockIfHittable(xyz);
/* 309 */                   } else if (block == Blocks.field_150412_bA && this.emeraldOre.isEnabled()) {
/* 310 */                     addBlockIfHittable(xyz);
/* 311 */                   } else if (block == Blocks.field_150340_R && this.goldBlocks.isEnabled()) {
/* 312 */                     addBlockIfHittable(xyz);
/*     */                   } 
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 320 */         for (BlockPos bp : (new HashMap<>(this.blocksNear)).keySet()) {
/* 321 */           IBlockState state = mc.field_71441_e.func_180495_p(bp);
/* 322 */           Block block = null;
/*     */           
/* 324 */           if (state != null) {
/* 325 */             block = state.func_177230_c();
/*     */           }
/*     */           
/* 328 */           if (Math.sqrt(bp.func_177957_d(eyes.field_72450_a, eyes.field_72448_b, eyes.field_72449_c)) > 5.0D || block == Blocks.field_150357_h || block == Blocks.field_150350_a) {
/* 329 */             this.blocksNear.remove(bp);
/*     */           }
/*     */         } 
/*     */         
/*     */         try {
/* 334 */           Thread.sleep(this.delayMs);
/* 335 */         } catch (InterruptedException e) {
/* 336 */           e.printStackTrace();
/*     */         }  continue;
/*     */       } 
/* 339 */       this.thread.interrupt();
/*     */     } 
/*     */ 
/*     */     
/* 343 */     this.thread = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\world\AutoMine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */