/*     */ package xyz.apfelmus.cheeto.client.modules.world;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockColored;
/*     */ import net.minecraft.block.BlockStone;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Disable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientChatReceivedEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.modules.player.CommMacro;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChadUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.RandomUtil;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.TimeHelper;
/*     */ import xyz.apfelmus.cheeto.client.utils.mining.Location;
/*     */ import xyz.apfelmus.cheeto.client.utils.mining.PathPoint;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render3DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.InventoryUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ @Module(name = "AutoMithril", category = Category.WORLD)
/*     */ public class AutoMithril implements Runnable {
/*     */   @Setting(name = "MiningSpot", description = "Spot to mine at, requires Etherwarp")
/*  54 */   private ModeSetting miningSpot = new ModeSetting("None", 
/*  55 */       getMiningSpotNames()); @Setting(name = "PickSlot")
/*  56 */   private IntegerSetting pickSlot = new IntegerSetting(
/*  57 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "Sneak", description = "Makes the player sneak while mining")
/*  58 */   private BooleanSetting sneak = new BooleanSetting(false);
/*     */   @Setting(name = "BlueWool")
/*  60 */   private BooleanSetting blueWool = new BooleanSetting(true);
/*     */   @Setting(name = "Prismarine")
/*  62 */   private BooleanSetting prismarine = new BooleanSetting(true);
/*     */   @Setting(name = "Titanium")
/*  64 */   private BooleanSetting titanium = new BooleanSetting(true);
/*     */   @Setting(name = "GrayShit")
/*  66 */   private BooleanSetting grayShit = new BooleanSetting(true);
/*     */   @Setting(name = "LookTime")
/*  68 */   private IntegerSetting lookTime = new IntegerSetting(
/*  69 */       Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(2500)); @Setting(name = "MaxMineTime", description = "Set to slightly more than it takes to mine")
/*  70 */   private IntegerSetting maxMineTime = new IntegerSetting(
/*  71 */       Integer.valueOf(5000), Integer.valueOf(0), Integer.valueOf(10000)); @Setting(name = "AotvSlot")
/*  72 */   private IntegerSetting aotvSlot = new IntegerSetting(
/*  73 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "WarpLookTime", description = "Set higher if low mana or bad ping")
/*  74 */   private IntegerSetting warpLookTime = new IntegerSetting(
/*  75 */       Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(2500)); @Setting(name = "WarpTime", description = "Set higher if low mana or bad ping")
/*  76 */   private IntegerSetting warpTime = new IntegerSetting(
/*  77 */       Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "MaxPlayerRange", description = "Range the bot will warp out at")
/*  78 */   private IntegerSetting maxPlayerRange = new IntegerSetting(
/*  79 */       Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(10)); @Setting(name = "Ungrab", description = "Automatically tabs out")
/*  80 */   private BooleanSetting ungrab = new BooleanSetting(true);
/*     */ 
/*     */   
/*  83 */   private static Minecraft mc = Minecraft.func_71410_x();
/*  84 */   private static Location currentLocation = null;
/*  85 */   private static List<PathPoint> path = null;
/*     */   private Thread thread;
/*  87 */   private List<BetterBlockPos> blocksNear = new ArrayList<>();
/*  88 */   private List<BlockPos> blacklist = new ArrayList<>();
/*  89 */   private int delayMs = 500;
/*     */   
/*     */   private BlockPos curBlockPos;
/*     */   private Block curBlock;
/*     */   private TimeHelper mineTimer;
/*     */   private Vec3 startRot;
/*     */   private Vec3 endRot;
/*  96 */   private static TimeHelper warpTimer = new TimeHelper();
/*  97 */   private static TimeHelper recoverTimer = new TimeHelper();
/*  98 */   private static TimeHelper boostTimer = new TimeHelper();
/*  99 */   private static TimeHelper refuelTimer = new TimeHelper();
/* 100 */   private static TimeHelper unlagTimer = new TimeHelper();
/*     */   
/* 102 */   private static BlockPos oldPos = null;
/* 103 */   private static int oldDrillSlot = -1;
/*     */   
/* 105 */   private static String fsMsg = "";
/*     */   
/*     */   enum MineState {
/* 108 */     WARP_FORGE,
/* 109 */     NAVIGATING,
/* 110 */     REFUEL,
/* 111 */     CHOOSE,
/* 112 */     LOOK,
/* 113 */     MINE;
/*     */   }
/*     */   
/*     */   private enum WarpState {
/* 117 */     SETUP,
/* 118 */     LOOK,
/* 119 */     WARP;
/*     */   }
/*     */   
/*     */   private enum RefuelState {
/* 123 */     CLICK_MERCHANT,
/* 124 */     CLICK_DRILL_IN,
/* 125 */     CLICK_FUEL_IN,
/* 126 */     REFUEL_DRILL,
/* 127 */     CLICK_DRILL_OUT,
/* 128 */     CLICK_BACK_IN_INV,
/* 129 */     DONE_REFUELING;
/*     */   }
/*     */   
/* 132 */   private MineState mineState = MineState.CHOOSE;
/* 133 */   private WarpState warpState = WarpState.SETUP;
/* 134 */   private RefuelState refuelState = RefuelState.CLICK_MERCHANT;
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/* 138 */     if (this.miningSpot.getCurrent().equals("None") || CF4M.INSTANCE.moduleManager.isEnabled("CommMacro")) {
/* 139 */       this.mineState = MineState.CHOOSE;
/*     */     }
/* 141 */     else if (this.aotvSlot.getCurrent().intValue() > 0 && this.pickSlot.getCurrent().intValue() > 0) {
/* 142 */       this.mineState = MineState.WARP_FORGE;
/* 143 */       this.refuelState = RefuelState.CLICK_MERCHANT;
/* 144 */       currentLocation = null;
/* 145 */       warpTimer.reset();
/* 146 */       recoverTimer.reset();
/* 147 */       boostTimer.reset();
/*     */     } else {
/* 149 */       ChatUtils.send("Configure your fucking Slots if you want FailSafes retard", new String[0]);
/* 150 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */     } 
/*     */ 
/*     */     
/* 154 */     this.warpState = WarpState.SETUP;
/* 155 */     this.blocksNear.clear();
/* 156 */     this.blacklist.clear();
/* 157 */     this.mineTimer = new TimeHelper();
/* 158 */     this.curBlockPos = null;
/* 159 */     this.curBlock = null;
/* 160 */     this.startRot = null;
/* 161 */     this.endRot = null;
/* 162 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), this.sneak.isEnabled());
/*     */     
/* 164 */     if (this.ungrab.isEnabled() && !CF4M.INSTANCE.moduleManager.isEnabled("CommMacro"))
/* 165 */       ChadUtils.ungrabMouse(); 
/*     */   }
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/* 170 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74312_F.func_151463_i(), false);
/* 171 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
/* 172 */     mc.field_71442_b.func_78767_c();
/*     */     
/* 174 */     if (this.ungrab.isEnabled() && !CF4M.INSTANCE.moduleManager.isEnabled("CommMacro"))
/* 175 */       ChadUtils.regrabMouse(); 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/* 180 */     if (this.mineState == MineState.REFUEL && mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiChest) {
/* 181 */       if (currentLocation != null && currentLocation.name.equals("REFUEL") && "Drill Anvil".equals(InventoryUtils.getInventoryName())) {
/* 182 */         ItemStack aboveDrill; ItemStack hopper; switch (this.refuelState) {
/*     */           case SETUP:
/* 184 */             oldDrillSlot = InventoryUtils.getSlotForItem("Drill", Items.field_179562_cC);
/* 185 */             mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, oldDrillSlot, 0, 1, (EntityPlayer)mc.field_71439_g);
/* 186 */             this.refuelState = RefuelState.CLICK_FUEL_IN;
/*     */             break;
/*     */           
/*     */           case LOOK:
/* 190 */             aboveDrill = InventoryUtils.getStackInOpenContainerSlot(20);
/* 191 */             if (aboveDrill != null && aboveDrill.func_77952_i() == 5) {
/* 192 */               int fuelSlot = InventoryUtils.getSlotForItem("Volta", Items.field_151144_bL);
/* 193 */               if (fuelSlot == -1) {
/* 194 */                 fuelSlot = InventoryUtils.getSlotForItem("Oil Barrel", Items.field_151144_bL);
/*     */               }
/* 196 */               if (fuelSlot == -1) {
/* 197 */                 ChatUtils.send("Bozo you don't have any fuel", new String[0]);
/* 198 */                 CF4M.INSTANCE.moduleManager.toggle(this);
/* 199 */                 mc.field_71439_g.func_71053_j();
/*     */                 break;
/*     */               } 
/* 202 */               mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, fuelSlot, 0, 1, (EntityPlayer)mc.field_71439_g);
/* 203 */               this.refuelState = RefuelState.REFUEL_DRILL;
/*     */             } 
/*     */             break;
/*     */           
/*     */           case WARP:
/* 208 */             hopper = InventoryUtils.getStackInOpenContainerSlot(22);
/* 209 */             if (hopper != null && hopper.func_77948_v()) {
/* 210 */               mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, 22, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 211 */               refuelTimer.reset();
/* 212 */               this.refuelState = RefuelState.CLICK_DRILL_OUT;
/*     */             } 
/*     */             break;
/*     */ 
/*     */           
/*     */           case null:
/* 218 */             if (refuelTimer.hasReached(500L)) {
/* 219 */               ItemStack oldDrill = InventoryUtils.getStackInOpenContainerSlot(29);
/*     */               
/* 221 */               if (oldDrill == null) {
/* 222 */                 mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, 13, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 223 */                 refuelTimer.reset();
/* 224 */                 this.refuelState = RefuelState.CLICK_BACK_IN_INV;
/*     */               } 
/*     */             } 
/*     */             break;
/*     */           
/*     */           case null:
/* 230 */             if (refuelTimer.hasReached(500L)) {
/* 231 */               ItemStack barrier = InventoryUtils.getStackInOpenContainerSlot(13);
/*     */               
/* 233 */               if (barrier != null && barrier.func_77973_b() == Item.func_150898_a(Blocks.field_180401_cv)) {
/* 234 */                 mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, oldDrillSlot, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 235 */                 refuelTimer.reset();
/* 236 */                 this.refuelState = RefuelState.DONE_REFUELING;
/*     */               } 
/*     */             } 
/*     */             break;
/*     */           
/*     */           case null:
/* 242 */             if (refuelTimer.hasReached(500L)) {
/* 243 */               mc.field_71439_g.func_71053_j();
/* 244 */               this.mineState = MineState.WARP_FORGE;
/* 245 */               this.refuelState = RefuelState.CLICK_MERCHANT;
/* 246 */               currentLocation = null;
/* 247 */               recoverTimer.reset();
/*     */             } 
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*     */       return;
/*     */     } 
/* 256 */     if (this.thread == null || !this.thread.isAlive()) {
/* 257 */       (this.thread = new Thread(this)).setDaemon(false);
/* 258 */       this.thread.setPriority(1);
/* 259 */       this.thread.start();
/*     */     } 
/*     */     
/* 262 */     if (this.mineState == MineState.CHOOSE || this.mineState == MineState.LOOK || this.mineState == MineState.MINE) {
/* 263 */       if (unlagTimer.hasReached(30000L)) {
/* 264 */         this.blacklist.clear();
/* 265 */         unlagTimer.reset();
/*     */       } 
/*     */       
/* 268 */       if (!CF4M.INSTANCE.moduleManager.isEnabled("CommMacro")) {
/* 269 */         boolean warpOut = false;
/* 270 */         int rongo = this.maxPlayerRange.getCurrent().intValue();
/*     */         
/* 272 */         if (currentLocation == null || !currentLocation.name.equals("REFUEL")) {
/* 273 */           if (boostTimer.hasReached(125000L)) {
/* 274 */             mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 275 */             boostTimer.reset();
/*     */           } 
/*     */           
/* 278 */           if (rongo != 0) {
/* 279 */             String warpName = "";
/* 280 */             for (Entity e : mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72314_b(rongo, (rongo >> 1), rongo), a -> (a instanceof net.minecraft.client.entity.EntityOtherPlayerMP || a instanceof EntityArmorStand))) {
/* 281 */               if (e instanceof EntityArmorStand) {
/* 282 */                 ItemStack bushSlot = ((EntityArmorStand)e).func_71124_b(4);
/* 283 */                 if (bushSlot != null && Item.func_150898_a((Block)Blocks.field_150330_I) == bushSlot.func_77973_b()) {
/* 284 */                   warpOut = true;
/* 285 */                   warpName = "Dead Bush"; break;
/*     */                 }  continue;
/*     */               } 
/* 288 */               if (!(e instanceof net.minecraft.client.entity.EntityOtherPlayerMP) || 
/* 289 */                 e.func_70005_c_().equals("Goblin ") || e.func_70005_c_().contains("Treasuer Hunter") || e.func_70005_c_().contains("Crystal Sentry"))
/* 290 */                 continue;  String formatted = e.func_145748_c_().func_150254_d();
/* 291 */               if (StringUtils.func_76338_a(formatted).equals(formatted)) {
/*     */                 continue;
/*     */               }
/* 294 */               if (formatted.startsWith("§r") && !formatted.startsWith("§r§")) {
/*     */                 continue;
/*     */               }
/* 297 */               warpOut = true;
/* 298 */               warpName = e.func_70005_c_();
/*     */             } 
/*     */             
/* 301 */             if (warpOut && recoverTimer.hasReached(5000L)) {
/* 302 */               if (!this.miningSpot.getCurrent().equals("None")) {
/* 303 */                 ChatUtils.send("Switching lobbies cause a nice person is near you: " + warpName, new String[0]);
/* 304 */                 mc.field_71439_g.func_71165_d("/warp home");
/* 305 */                 this.mineState = MineState.WARP_FORGE;
/* 306 */                 KeyBinding.func_74506_a();
/* 307 */                 recoverTimer.reset();
/*     */               } else {
/* 309 */                 ChatUtils.send("A person was near you, but you didn't configure a Mining Spot, sending to your island: " + warpName, new String[0]);
/* 310 */                 mc.field_71439_g.func_71165_d("/warp home");
/* 311 */                 KeyBinding.func_74506_a();
/* 312 */                 if (CF4M.INSTANCE.moduleManager.isEnabled(this)) {
/* 313 */                   CF4M.INSTANCE.moduleManager.toggle(this);
/*     */                 }
/*     */               } 
/* 316 */               recoverTimer.reset();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 322 */       this.blocksNear.removeIf(v -> {
/*     */             Vec3 randPoint = v.points.get(RandomUtil.randBetween(0, v.points.size() - 1));
/*     */             MovingObjectPosition mop = mc.field_71441_e.func_72933_a(mc.field_71439_g.func_174824_e(1.0F), randPoint);
/* 325 */             return (mop != null && mop.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) ? (
/* 326 */               (!mop.func_178782_a().equals(v.blockPos) || randPoint.func_72438_d(mc.field_71439_g.func_174824_e(1.0F)) >= mc.field_71442_b.func_78757_d())) : true;
/*     */           });
/*     */ 
/*     */ 
/*     */       
/* 331 */       if (this.blocksNear.stream().noneMatch(v -> v.blockPos.equals(this.curBlockPos))) {
/* 332 */         this.mineState = MineState.CHOOSE;
/*     */       }
/*     */     } 
/*     */     
/* 336 */     if (recoverTimer.hasReached(5000L)) {
/* 337 */       BlockPosWithVec closest; IBlockState ibs; SkyblockUtils.Location curLoc = SkyblockUtils.getLocation();
/*     */ 
/*     */       
/* 340 */       switch (curLoc) {
/*     */         case SETUP:
/* 342 */           switch (this.mineState) {
/*     */             case SETUP:
/* 344 */               if (currentLocation == null || !currentLocation.name.equals("REFUEL")) {
/* 345 */                 Optional<Location> loc = CommMacro.miningJson.locations.stream().filter(v -> v.name.equals(this.miningSpot.getCurrent())).findFirst();
/* 346 */                 loc.ifPresent(location -> currentLocation = location);
/*     */               } 
/* 348 */               if (currentLocation != null) {
/* 349 */                 ChatUtils.send("Navigating to: " + currentLocation.name, new String[0]);
/* 350 */                 mc.field_71439_g.func_71165_d("/warp forge");
/* 351 */                 path = null;
/* 352 */                 this.mineState = MineState.NAVIGATING; break;
/*     */               } 
/* 354 */               ChatUtils.send("Couldn't determine location, very weird", new String[0]);
/* 355 */               CF4M.INSTANCE.moduleManager.toggle(this);
/*     */               break;
/*     */ 
/*     */             
/*     */             case LOOK:
/* 360 */               if (mc.field_71439_g.func_180425_c().equals(new BlockPos(1, 149, -68)) && path == null) {
/* 361 */                 path = new ArrayList<>(currentLocation.path);
/* 362 */                 oldPos = null;
/* 363 */                 KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), true);
/* 364 */                 warpTimer.reset();
/* 365 */                 this.warpState = WarpState.SETUP;
/*     */               } 
/*     */               break;
/*     */             
/*     */             case WARP:
/* 370 */               if (currentLocation != null && currentLocation.name.equals("REFUEL") && this.refuelState == RefuelState.CLICK_MERCHANT) {
/* 371 */                 List<Entity> possible = mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72314_b(5.0D, 3.0D, 5.0D), a -> a.func_70005_c_().contains("Jotraeline Greatforge"));
/* 372 */                 if (!possible.isEmpty()) {
/* 373 */                   mc.field_71442_b.func_78768_b((EntityPlayer)mc.field_71439_g, possible.get(0));
/* 374 */                   this.refuelState = RefuelState.CLICK_DRILL_IN;
/*     */                 } 
/*     */               } 
/*     */               break;
/*     */             
/*     */             case null:
/* 380 */               if (boostTimer.hasReached(125000L)) {
/* 381 */                 mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 382 */                 boostTimer.reset();
/*     */               } 
/*     */               
/* 385 */               closest = getClosestBlock(null);
/*     */               
/* 387 */               if (closest != null) {
/* 388 */                 this.curBlockPos = closest.getBlockPos();
/* 389 */                 IBlockState iBlockState = mc.field_71441_e.func_180495_p(this.curBlockPos);
/* 390 */                 if (iBlockState != null) {
/* 391 */                   this.curBlock = iBlockState.func_177230_c();
/*     */                 }
/* 393 */                 this.startRot = closest.getVec3();
/* 394 */                 this.endRot = null;
/* 395 */                 RotationUtils.setup(RotationUtils.getRotation(closest.getVec3()), Long.valueOf(this.lookTime.getCurrent().intValue()));
/* 396 */                 this.mineState = MineState.LOOK; break;
/*     */               } 
/* 398 */               KeyBinding.func_74510_a(mc.field_71474_y.field_74312_F.func_151463_i(), false);
/*     */               break;
/*     */ 
/*     */ 
/*     */             
/*     */             case null:
/* 404 */               if (this.mineTimer.hasReached(this.maxMineTime.getCurrent().intValue())) {
/* 405 */                 this.blocksNear.removeIf(v -> v.blockPos.equals(this.curBlockPos));
/* 406 */                 this.blacklist.add(this.curBlockPos);
/* 407 */                 this.mineState = MineState.CHOOSE;
/*     */                 
/*     */                 break;
/*     */               } 
/* 411 */               if (mc.field_71476_x == null || mc.field_71476_x.field_72313_a != MovingObjectPosition.MovingObjectType.BLOCK) {
/* 412 */                 this.blocksNear.removeIf(v -> v.blockPos.equals(this.curBlockPos));
/* 413 */                 this.blacklist.add(this.curBlockPos);
/* 414 */                 this.mineState = MineState.CHOOSE;
/*     */                 
/*     */                 break;
/*     */               } 
/* 418 */               ibs = mc.field_71441_e.func_180495_p(this.curBlockPos);
/* 419 */               if (ibs != null) {
/* 420 */                 if ((this.curBlock != null && ibs.func_177230_c() != this.curBlock) || ibs.func_177230_c() == Blocks.field_150357_h || ibs.func_177230_c() == Blocks.field_150350_a) {
/* 421 */                   this.blocksNear.removeIf(v -> v.blockPos.equals(this.curBlockPos));
/* 422 */                   this.mineState = MineState.CHOOSE;
/* 423 */                   this.curBlockPos = null;
/* 424 */                   this.curBlock = null;
/* 425 */                   this.startRot = null;
/* 426 */                   this.endRot = null;
/*     */                   break;
/*     */                 } 
/* 429 */                 if (!mc.field_71474_y.field_74312_F.func_151470_d() && mc.field_71462_r == null) {
/* 430 */                   mc.field_71415_G = true;
/* 431 */                   KeyBinding.func_74507_a(mc.field_71474_y.field_74312_F.func_151463_i());
/* 432 */                   KeyBinding.func_74510_a(mc.field_71474_y.field_74312_F.func_151463_i(), true);
/*     */                 } 
/*     */               } 
/*     */               break;
/*     */           } 
/*     */ 
/*     */           
/*     */           break;
/*     */         
/*     */         case LOOK:
/* 442 */           ChatUtils.send("Detected player in Island, re-warping", new String[0]);
/* 443 */           fsMsg = "/warp forge";
/*     */           break;
/*     */         
/*     */         case WARP:
/* 447 */           ChatUtils.send("Detected player in Hub, re-warping", new String[0]);
/* 448 */           fsMsg = "/warp forge";
/*     */           break;
/*     */         
/*     */         case null:
/* 452 */           ChatUtils.send("Detected player at Lift, re-warping", new String[0]);
/* 453 */           fsMsg = "/warp forge";
/*     */           break;
/*     */         
/*     */         case null:
/* 457 */           ChatUtils.send("Detected player in Lobby, re-warping", new String[0]);
/* 458 */           fsMsg = "/play skyblock";
/*     */           break;
/*     */         
/*     */         case null:
/* 462 */           ChatUtils.send("Detected player in Limbo, re-warping", new String[0]);
/* 463 */           fsMsg = "/l";
/*     */           break;
/*     */       } 
/*     */       
/* 467 */       if (curLoc != SkyblockUtils.Location.SKYBLOCK && 
/* 468 */         !this.miningSpot.getCurrent().equals("None") && !CF4M.INSTANCE.moduleManager.isEnabled("CommMacro")) {
/* 469 */         this.mineState = MineState.WARP_FORGE;
/* 470 */         mc.field_71439_g.func_71165_d(fsMsg);
/* 471 */         KeyBinding.func_74506_a();
/* 472 */         recoverTimer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRender(Render3DEvent event) {
/*     */     BlockPosWithVec next;
/* 480 */     List<BetterBlockPos> copy = new ArrayList<>(this.blocksNear);
/* 481 */     for (BetterBlockPos bbp : copy) {
/* 482 */       if (!bbp.blockPos.equals(this.curBlockPos)) {
/* 483 */         Render3DUtils.renderEspBox(bbp.blockPos, event.partialTicks, -16711681);
/*     */       }
/*     */     } 
/*     */     
/* 487 */     if (copy.stream().anyMatch(v -> v.blockPos.equals(this.curBlockPos))) {
/* 488 */       Render3DUtils.renderEspBox(this.curBlockPos, event.partialTicks, -65536);
/*     */     }
/* 490 */     if (this.startRot != null && this.endRot != null) {
/* 491 */       Render3DUtils.drawLine(this.startRot, this.endRot, 1.0F, event.partialTicks);
/*     */     }
/* 493 */     if (this.startRot != null) {
/* 494 */       Render3DUtils.renderSmallBox(this.startRot, -16711936);
/*     */     }
/* 496 */     if (this.endRot != null) {
/* 497 */       Render3DUtils.renderSmallBox(this.endRot, -65536);
/*     */     }
/*     */     
/* 500 */     if (mc.field_71462_r != null)
/*     */       return; 
/* 502 */     if (this.mineState == MineState.NAVIGATING && path != null) {
/* 503 */       switch (this.warpState) {
/*     */         case SETUP:
/* 505 */           if (path.size() > 0) {
/* 506 */             if (warpTimer.hasReached(this.warpTime.getCurrent().intValue()) && !mc.field_71439_g.func_180425_c().equals(oldPos)) {
/* 507 */               PathPoint a = path.get(0);
/* 508 */               path.remove(0);
/* 509 */               RotationUtils.setup(RotationUtils.getRotation(new Vec3(a.x, a.y, a.z)), Long.valueOf(this.warpLookTime.getCurrent().intValue()));
/* 510 */               oldPos = mc.field_71439_g.func_180425_c();
/* 511 */               this.warpState = WarpState.LOOK; break;
/* 512 */             }  if (warpTimer.hasReached(2500L)) {
/* 513 */               ChatUtils.send("Got stuck while tp'ing, re-navigating", new String[0]);
/* 514 */               mc.field_71439_g.func_71165_d("/l");
/* 515 */               recoverTimer.reset();
/* 516 */               warpTimer.reset();
/*     */             }  break;
/*     */           } 
/* 519 */           if (!this.sneak.isEnabled()) {
/* 520 */             KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
/*     */           }
/* 522 */           if (this.pickSlot.getCurrent().intValue() > 0 && this.pickSlot.getCurrent().intValue() <= 8) {
/* 523 */             mc.field_71439_g.field_71071_by.field_70461_c = this.pickSlot.getCurrent().intValue() - 1;
/*     */           }
/* 525 */           if (currentLocation.name.equals("REFUEL")) {
/* 526 */             this.mineState = MineState.REFUEL; break;
/*     */           } 
/* 528 */           this.mineState = MineState.CHOOSE;
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case LOOK:
/* 534 */           if (System.currentTimeMillis() <= RotationUtils.endTime) {
/* 535 */             RotationUtils.update(); break;
/*     */           } 
/* 537 */           RotationUtils.update();
/* 538 */           warpTimer.reset();
/* 539 */           this.warpState = WarpState.WARP;
/*     */           break;
/*     */ 
/*     */         
/*     */         case WARP:
/* 544 */           if (warpTimer.hasReached(this.warpTime.getCurrent().intValue())) {
/* 545 */             mc.field_71439_g.field_71071_by.field_70461_c = this.aotvSlot.getCurrent().intValue() - 1;
/* 546 */             mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 547 */             warpTimer.reset();
/* 548 */             this.warpState = WarpState.SETUP;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     
/*     */     }
/* 554 */     switch (this.mineState) {
/*     */       case null:
/* 556 */         if (System.currentTimeMillis() <= RotationUtils.endTime) {
/* 557 */           RotationUtils.update(); break;
/*     */         } 
/* 559 */         if (!RotationUtils.done)
/* 560 */           RotationUtils.update(); 
/* 561 */         this.mineTimer.reset();
/*     */         
/* 563 */         next = getClosestBlock(this.curBlockPos);
/*     */         
/* 565 */         if (next != null) {
/* 566 */           Optional<BetterBlockPos> bbp = copy.stream().filter(v -> v.blockPos.equals(this.curBlockPos)).findFirst();
/*     */           
/* 568 */           if (bbp.isPresent()) {
/* 569 */             List<Vec3> points = ((BetterBlockPos)bbp.get()).points;
/* 570 */             points.stream()
/* 571 */               .min(Comparator.comparing(v -> Float.valueOf(RotationUtils.getNeededChange(RotationUtils.getRotation(next.getVec3()), RotationUtils.getRotation(v)).getValue())))
/* 572 */               .ifPresent(nextPointOnSameBlock -> {
/*     */                   this.curBlock = mc.field_71441_e.func_180495_p(this.curBlockPos).func_177230_c();
/*     */                   this.endRot = nextPointOnSameBlock;
/*     */                   RotationUtils.setup(RotationUtils.getRotation(nextPointOnSameBlock), Long.valueOf(this.maxMineTime.getCurrent().intValue()));
/*     */                 });
/*     */           } 
/*     */         } 
/* 579 */         this.mineState = MineState.MINE;
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 584 */         if (System.currentTimeMillis() <= RotationUtils.endTime) {
/* 585 */           RotationUtils.update(); break;
/*     */         } 
/* 587 */         this.startRot = null;
/* 588 */         this.endRot = null;
/* 589 */         if (!RotationUtils.done) {
/* 590 */           RotationUtils.update();
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 598 */     if (!CF4M.INSTANCE.moduleManager.isEnabled("CommMacro")) {
/* 599 */       String msg = event.message.func_150260_c();
/* 600 */       if (this.mineState == MineState.CHOOSE || this.mineState == MineState.LOOK || this.mineState == MineState.MINE) {
/* 601 */         if (msg.startsWith("Mining Speed Boost is now available!")) {
/* 602 */           mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 603 */           boostTimer.reset();
/* 604 */         } else if (msg.startsWith("Your") && msg.contains("is empty! Refuel it by talking to a Drill Mechanic!")) {
/* 605 */           if (!this.miningSpot.getCurrent().equals("None")) {
/* 606 */             currentLocation = CommMacro.miningJson.locations.get(CommMacro.miningJson.locations.size() - 1);
/* 607 */             this.mineState = MineState.WARP_FORGE;
/*     */           } else {
/* 609 */             ChatUtils.send("Can't use Drill Refuel without a Mining Spot", new String[0]);
/* 610 */             CF4M.INSTANCE.moduleManager.toggle(this);
/*     */           } 
/*     */         } 
/*     */       }
/* 614 */       if (msg.contains("You can't fast travel while in combat!")) {
/* 615 */         mc.field_71439_g.func_71165_d("/l");
/* 616 */         recoverTimer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<String> getMiningSpotNames() {
/* 622 */     List<String> ret = new ArrayList<>();
/* 623 */     ret.add("None");
/* 624 */     if (CommMacro.miningJson == null) {
/* 625 */       ret.add("Buy Sigma Nigger");
/*     */     } else {
/* 627 */       ret.addAll((Collection<? extends String>)CommMacro.miningJson.locations.subList(0, CommMacro.miningJson.locations.size() - 5).stream()
/* 628 */           .map(v -> v.name)
/* 629 */           .collect(Collectors.toList()));
/*     */     } 
/* 631 */     return ret;
/*     */   }
/*     */   
/*     */   private void addBlockIfHittable(BlockPos xyz) {
/* 635 */     List<Vec3> pointsOnBlock = RotationUtils.getPointsOnBlock(xyz);
/*     */     
/* 637 */     for (Vec3 point : pointsOnBlock) {
/* 638 */       MovingObjectPosition mop = mc.field_71441_e.func_72933_a(mc.field_71439_g.func_174824_e(1.0F), point);
/*     */       
/* 640 */       if (mop != null && mop.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && 
/* 641 */         mop.func_178782_a().equals(xyz) && point.func_72438_d(mc.field_71439_g.func_174824_e(1.0F)) < mc.field_71442_b.func_78757_d()) {
/* 642 */         if (this.blocksNear.stream().noneMatch(v -> v.blockPos.equals(xyz))) {
/* 643 */           this.blocksNear.add(new BetterBlockPos(xyz, new ArrayList<>(Collections.singletonList(point)))); continue;
/*     */         } 
/* 645 */         this.blocksNear.stream()
/* 646 */           .filter(v -> v.blockPos.equals(xyz))
/* 647 */           .findFirst()
/* 648 */           .ifPresent(v -> v.points.add(point));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BlockPosWithVec getClosestBlock(BlockPos excluding) {
/* 656 */     BlockPos closest = null;
/* 657 */     Rotation closestRot = null;
/* 658 */     Vec3 closestPoint = null;
/*     */     
/* 660 */     List<BetterBlockPos> asd = new ArrayList<>(this.blocksNear);
/*     */     
/* 662 */     asd.removeIf(v -> v.blockPos.equals(excluding));
/*     */     
/* 664 */     List<BetterBlockPos> tits = new ArrayList<>();
/* 665 */     if (CommMacro.hugeTits) {
/* 666 */       asd.forEach(bbp -> {
/*     */             IBlockState bs = mc.field_71441_e.func_180495_p(bbp.blockPos);
/*     */             
/*     */             Block b = bs.func_177230_c();
/*     */             
/*     */             if (b == Blocks.field_150348_b && bs.func_177229_b((IProperty)BlockStone.field_176247_a) == BlockStone.EnumType.DIORITE_SMOOTH) {
/*     */               tits.add(bbp);
/*     */             }
/*     */           });
/* 675 */       if (!tits.isEmpty()) {
/* 676 */         asd = tits;
/*     */       }
/*     */     } 
/*     */     
/* 680 */     List<BetterBlockPos> miths = new ArrayList<>();
/* 681 */     if (CommMacro.mithril) {
/* 682 */       asd.forEach(bbp -> {
/*     */             IBlockState bs = mc.field_71441_e.func_180495_p(bbp.blockPos);
/*     */             
/*     */             Block b = bs.func_177230_c();
/*     */             
/*     */             if (b == Blocks.field_150325_L && bs.func_177229_b((IProperty)BlockColored.field_176581_a) == EnumDyeColor.GRAY) {
/*     */               miths.add(bbp);
/*     */             } else if (b == Blocks.field_150406_ce && bs.func_177229_b((IProperty)BlockColored.field_176581_a) == EnumDyeColor.CYAN) {
/*     */               miths.add(bbp);
/*     */             } 
/*     */           });
/* 693 */       if (!miths.isEmpty()) {
/* 694 */         asd = miths;
/*     */       }
/*     */     } 
/*     */     
/* 698 */     for (BetterBlockPos bbp : asd) {
/* 699 */       for (Vec3 point : bbp.points) {
/* 700 */         Rotation endRot = RotationUtils.getRotation(point);
/* 701 */         Rotation needed = RotationUtils.getNeededChange(endRot);
/*     */         
/* 703 */         if (closestRot != null && needed.getValue() >= closestRot.getValue())
/*     */           continue; 
/* 705 */         closest = bbp.blockPos;
/* 706 */         closestRot = needed;
/* 707 */         closestPoint = point;
/*     */       } 
/*     */     } 
/*     */     
/* 711 */     if (closest != null) {
/* 712 */       return new BlockPosWithVec(closest, closestPoint);
/*     */     }
/* 714 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 719 */     while (!this.thread.isInterrupted() && mc.field_71439_g != null && mc.field_71441_e != null) {
/* 720 */       if (CF4M.INSTANCE.moduleManager.isEnabled(this)) {
/* 721 */         int radius = 6;
/*     */         
/* 723 */         int px = MathHelper.func_76128_c(mc.field_71439_g.field_70165_t);
/* 724 */         int py = MathHelper.func_76128_c(mc.field_71439_g.field_70163_u + 1.0D);
/* 725 */         int pz = MathHelper.func_76128_c(mc.field_71439_g.field_70161_v);
/* 726 */         Vec3 eyes = mc.field_71439_g.func_174824_e(1.0F);
/*     */         
/* 728 */         for (int x = px - radius; x < px + radius + 1; x++) {
/* 729 */           for (int y = py - radius; y < py + radius + 1; y++) {
/* 730 */             for (int z = pz - radius; z < pz + radius + 1; z++) {
/* 731 */               BlockPos xyz = new BlockPos(x, y, z);
/* 732 */               IBlockState bs = mc.field_71441_e.func_180495_p(xyz);
/*     */               
/* 734 */               if (px != xyz.func_177958_n() || pz != xyz.func_177952_p())
/*     */               {
/* 736 */                 if ((new ArrayList(this.blocksNear)).stream().noneMatch(v -> v.blockPos.equals(xyz)) && !this.blacklist.contains(xyz)) {
/* 737 */                   Block block = bs.func_177230_c();
/* 738 */                   if (Math.sqrt(xyz.func_177957_d(eyes.field_72450_a, eyes.field_72448_b, eyes.field_72449_c)) <= 6.0D) {
/* 739 */                     if (block == Blocks.field_150325_L) {
/* 740 */                       if (bs.func_177229_b((IProperty)BlockColored.field_176581_a) == EnumDyeColor.LIGHT_BLUE) {
/* 741 */                         if (this.blueWool.isEnabled()) {
/* 742 */                           addBlockIfHittable(xyz);
/*     */                         }
/* 744 */                       } else if (bs.func_177229_b((IProperty)BlockColored.field_176581_a) == EnumDyeColor.GRAY && 
/* 745 */                         this.grayShit.isEnabled()) {
/* 746 */                         addBlockIfHittable(xyz);
/*     */                       }
/*     */                     
/* 749 */                     } else if (block == Blocks.field_180397_cI) {
/* 750 */                       if (this.prismarine.isEnabled()) {
/* 751 */                         addBlockIfHittable(xyz);
/*     */                       }
/* 753 */                     } else if (block == Blocks.field_150348_b) {
/* 754 */                       if (bs.func_177229_b((IProperty)BlockStone.field_176247_a) == BlockStone.EnumType.DIORITE_SMOOTH && 
/* 755 */                         this.titanium.isEnabled()) {
/* 756 */                         addBlockIfHittable(xyz);
/*     */                       }
/*     */                     }
/* 759 */                     else if (block == Blocks.field_150406_ce && 
/* 760 */                       bs.func_177229_b((IProperty)BlockColored.field_176581_a) == EnumDyeColor.CYAN && 
/* 761 */                       this.grayShit.isEnabled()) {
/* 762 */                       addBlockIfHittable(xyz);
/*     */                     } 
/*     */                   }
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 772 */         for (BetterBlockPos bbp : new ArrayList(this.blocksNear)) {
/* 773 */           IBlockState state = mc.field_71441_e.func_180495_p(bbp.blockPos);
/* 774 */           Block block = null;
/*     */           
/* 776 */           if (state != null) {
/* 777 */             block = state.func_177230_c();
/*     */           }
/*     */           
/* 780 */           if (Math.sqrt(bbp.blockPos.func_177957_d(eyes.field_72450_a, eyes.field_72448_b, eyes.field_72449_c)) > 5.0D || block == Blocks.field_150357_h || block == Blocks.field_150350_a) {
/* 781 */             this.blocksNear.remove(bbp);
/*     */           }
/*     */         } 
/*     */         
/*     */         try {
/* 786 */           Thread.sleep(this.delayMs);
/* 787 */         } catch (InterruptedException e) {
/* 788 */           e.printStackTrace();
/*     */         }  continue;
/*     */       } 
/* 791 */       this.thread.interrupt();
/*     */     } 
/*     */ 
/*     */     
/* 795 */     this.thread = null;
/*     */   }
/*     */   
/*     */   private static class BetterBlockPos {
/*     */     BlockPos blockPos;
/*     */     List<Vec3> points;
/*     */     
/*     */     public BetterBlockPos(BlockPos blockPos, List<Vec3> points) {
/* 803 */       this.blockPos = blockPos;
/* 804 */       this.points = points;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlockPosWithVec {
/*     */     private BlockPos blockPos;
/*     */     private Vec3 vec3;
/*     */     
/*     */     public BlockPosWithVec(BlockPos blockPos, Vec3 vec3) {
/* 813 */       this.blockPos = blockPos;
/* 814 */       this.vec3 = vec3;
/*     */     }
/*     */     
/*     */     public BlockPos getBlockPos() {
/* 818 */       return this.blockPos;
/*     */     }
/*     */     
/*     */     public Vec3 getVec3() {
/* 822 */       return this.vec3;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\world\AutoMithril.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */