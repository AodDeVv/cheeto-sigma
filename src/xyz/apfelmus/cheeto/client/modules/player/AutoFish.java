/*     */ package xyz.apfelmus.cheeto.client.modules.player;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.play.server.S2APacketParticles;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientChatReceivedEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.SoundEvent;
/*     */ import xyz.apfelmus.cheeto.client.modules.misc.ArmorSwap;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChadUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.CheetoStatus;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.JsonUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.KeybindUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.fishing.Location;
/*     */ import xyz.apfelmus.cheeto.client.utils.fishing.PathPoint;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.RandomUtil;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.TimeHelper;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.VecUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ @Module(name = "AutoFish", category = Category.PLAYER)
/*     */ public class AutoFish {
/*  51 */   public static FishingJson fishingJson = JsonUtils.getFishingJson(); @Setting(name = "FishingSpot", description = "Spot to fish at, requires Etherwarp")
/*  52 */   private ModeSetting fishingSpot = new ModeSetting("None", 
/*  53 */       getFishingSpotNames()); @Setting(name = "RecastDelay", description = "Delay between recasts")
/*  54 */   private IntegerSetting recastDelay = new IntegerSetting(
/*  55 */       Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(2000)); @Setting(name = "AntiAfk", description = "Will do small movements")
/*  56 */   private BooleanSetting antiAfk = new BooleanSetting(true);
/*     */   @Setting(name = "AimSpeed", description = "Speed of rotations")
/*  58 */   private IntegerSetting aimSpeed = new IntegerSetting(
/*  59 */       Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "RodSlot", description = "Slot of the rod")
/*  60 */   private IntegerSetting rodSlot = new IntegerSetting(
/*  61 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "WhipSlot", description = "Configure for Automatic SC Killing")
/*  62 */   private IntegerSetting whipSlot = new IntegerSetting(
/*  63 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "KillMode", description = "Left, Right Click, or Hyperion")
/*  64 */   private ModeSetting killMode = new ModeSetting("Right", 
/*  65 */       Arrays.asList(new String[] { "Left", "Right", "Hyperion" })); @Setting(name = "HypDamage", description = "Damage you deal with Hyperion")
/*  66 */   private IntegerSetting hypDamage = new IntegerSetting(
/*  67 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(3000000)); @Setting(name = "KillPrio", description = "Kill SC before Re-casting (Sword)")
/*  68 */   private BooleanSetting killPrio = new BooleanSetting(false);
/*     */   @Setting(name = "SCRange", description = "Range for Sea Creature Killing")
/*  70 */   private IntegerSetting scRange = new IntegerSetting(
/*  71 */       Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(20)); @Setting(name = "PetSwap", description = "Activates PetSwap on bobber in water")
/*  72 */   private BooleanSetting petSwap = new BooleanSetting(false);
/*     */   @Setting(name = "AotvSlot", description = "Slot of your AOTV")
/*  74 */   private IntegerSetting aotvSlot = new IntegerSetting(
/*  75 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "WarpLookTime", description = "Set higher if low mana or bad ping")
/*  76 */   private IntegerSetting warpLookTime = new IntegerSetting(
/*  77 */       Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(2500)); @Setting(name = "WarpTime", description = "Set higher if low mana or bad ping")
/*  78 */   private IntegerSetting warpTime = new IntegerSetting(
/*  79 */       Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "WarpOutOnDeath", description = "Warps you out when you die")
/*  80 */   private BooleanSetting warpOutOnDeath = new BooleanSetting(false);
/*     */   @Setting(name = "MaxPlayerRange", description = "Range the bot will warp out at")
/*  82 */   private IntegerSetting maxPlayerRange = new IntegerSetting(
/*  83 */       Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(10)); @Setting(name = "AssistMode", description = "Will only reel in.")
/*  84 */   private BooleanSetting assistMode = new BooleanSetting(false);
/*     */   @Setting(name = "SlugMode", description = "Will only reel in after 30 seconds")
/*  86 */   private BooleanSetting slugMode = new BooleanSetting(false);
/*     */   @Setting(name = "Sneak", description = "Makes the Player sneak while fishing")
/*  88 */   private BooleanSetting sneak = new BooleanSetting(false);
/*     */   @Setting(name = "Ungrab", description = "Automatically tabs out")
/*  90 */   private BooleanSetting ungrab = new BooleanSetting(true);
/*     */   @Setting(name = "Debug", description = "Will print shit in chat")
/*  92 */   private BooleanSetting debug = new BooleanSetting(false);
/*     */   @Setting(name = "Ember Mode", description = "Does the Ember Swapping thing")
/*  94 */   private BooleanSetting ember = new BooleanSetting(false);
/*     */   @Setting(name = "EmberSlot", description = "Ember Armor Slot in Wardrobe")
/*  96 */   private IntegerSetting emberSlot = new IntegerSetting(
/*  97 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(9)); @Setting(name = "TrophySlot", description = "Trophy Armor Slot in Wardrobe")
/*  98 */   private IntegerSetting trophySlot = new IntegerSetting(
/*  99 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(9)); @Setting(name = "DebugX")
/* 100 */   private IntegerSetting debugX = new IntegerSetting(
/* 101 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100)); @Setting(name = "DebugY")
/* 102 */   private IntegerSetting debugY = new IntegerSetting(
/* 103 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100));
/*     */   
/* 105 */   private static Minecraft mc = Minecraft.func_71410_x();
/* 106 */   private static List<String> fishingMobs = JsonUtils.getListFromUrl(CheetoStatus.yepUrls[0], "mobs");
/*     */   
/* 108 */   private static TimeHelper warpTimer = new TimeHelper();
/* 109 */   private static TimeHelper throwTimer = new TimeHelper();
/* 110 */   private static TimeHelper inWaterTimer = new TimeHelper();
/* 111 */   private static TimeHelper killTimer = new TimeHelper();
/* 112 */   private static TimeHelper recoverTimer = new TimeHelper();
/* 113 */   private static TimeHelper waitTimer = new TimeHelper();
/* 114 */   private static TimeHelper onObsidianTimer = new TimeHelper();
/* 115 */   private static EntityArmorStand curScStand = null;
/* 116 */   private static Entity curSc = null;
/*     */   private static boolean killing = false;
/* 118 */   private static int clicksLeft = 0;
/*     */   
/*     */   private static boolean flash = false;
/* 121 */   private static Location currentLocation = null;
/* 122 */   private static List<PathPoint> path = null;
/* 123 */   private static BlockPos oldPos = null;
/*     */   
/* 125 */   private static double oldBobberPosY = 0.0D;
/*     */   private static boolean oldBobberInWater = false;
/* 127 */   private static int ticks = 0;
/* 128 */   private static Vec3 startPos = null;
/* 129 */   private static Rotation startRot = null;
/*     */   
/*     */   private static boolean openedWar = false;
/*     */   
/*     */   private static boolean emberArmor = false;
/*     */   private static boolean trophyArmor = false;
/* 135 */   private static List<ParticleEntry> particleList = new ArrayList<>();
/*     */   
/*     */   private enum AutoFishState {
/* 138 */     EMBER_OPENWAR,
/* 139 */     EMBER_WAIT,
/* 140 */     EMBER_TROPHY,
/* 141 */     WARP_ISLAND,
/* 142 */     WARP_SPOT,
/* 143 */     NAVIGATING,
/* 144 */     THROWING,
/* 145 */     IN_WATER,
/* 146 */     FISH_BITE;
/*     */   }
/*     */   
/*     */   private enum WarpState {
/* 150 */     SETUP,
/* 151 */     LOOK,
/* 152 */     WARP;
/*     */   }
/*     */   
/*     */   private enum AAState {
/* 156 */     AWAY,
/* 157 */     BACK;
/*     */   }
/*     */   
/* 160 */   private AutoFishState afs = AutoFishState.THROWING;
/* 161 */   private WarpState warpState = WarpState.SETUP;
/* 162 */   private AAState aaState = AAState.AWAY;
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/* 166 */     resetVariables();
/* 167 */     RotationUtils.reset();
/*     */     
/* 169 */     if (this.assistMode.isEnabled()) {
/* 170 */       this.afs = AutoFishState.IN_WATER;
/*     */       
/*     */       return;
/*     */     } 
/* 174 */     if (this.killMode.getCurrent().equals("Hyperion") && this.hypDamage.getCurrent().intValue() <= 0) {
/* 175 */       ChatUtils.send("Configure your HypDamage pls ty", new String[0]);
/* 176 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */       
/*     */       return;
/*     */     } 
/* 180 */     if (this.rodSlot.getCurrent().intValue() == 0) {
/* 181 */       ChatUtils.send("Configure your RodSlot pls ty", new String[0]);
/* 182 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */       
/*     */       return;
/*     */     } 
/* 186 */     if (this.whipSlot.getCurrent().intValue() != 0 && fishingMobs.isEmpty()) {
/* 187 */       ChatUtils.send("An error occured while getting Fishing Mobs, reloading...", new String[0]);
/* 188 */       fishingMobs = JsonUtils.getListFromUrl(CheetoStatus.yepUrls[0], "mobs");
/* 189 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */       
/*     */       return;
/*     */     } 
/* 193 */     if (this.ember.isEnabled()) {
/* 194 */       if (this.emberSlot.getCurrent().intValue() == 0 || this.trophySlot.getCurrent().intValue() == 0) {
/* 195 */         ChatUtils.send("Configure your trophy and ember slot nigga", new String[0]);
/* 196 */         CF4M.INSTANCE.moduleManager.toggle(this);
/*     */         return;
/*     */       } 
/* 199 */       this.recastDelay.setCurrent(Integer.valueOf(1000));
/*     */     } 
/*     */ 
/*     */     
/* 203 */     if (!this.fishingSpot.getCurrent().equals("None")) {
/* 204 */       if (fishingJson == null) {
/* 205 */         ChatUtils.send("An error occured while getting Fishing Locations, reloading...", new String[0]);
/* 206 */         fishingJson = JsonUtils.getFishingJson();
/* 207 */         CF4M.INSTANCE.moduleManager.toggle(this);
/*     */         
/*     */         return;
/*     */       } 
/* 211 */       if (this.aotvSlot.getCurrent().intValue() == 0) {
/* 212 */         ChatUtils.send("Configure your AotvSlot pls ty", new String[0]);
/* 213 */         CF4M.INSTANCE.moduleManager.toggle(this);
/*     */         
/*     */         return;
/*     */       } 
/* 217 */       this.afs = AutoFishState.WARP_ISLAND;
/* 218 */       this.warpState = WarpState.SETUP;
/*     */     } 
/*     */     
/* 221 */     startPos = mc.field_71439_g.func_174791_d();
/* 222 */     startRot = new Rotation(mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A);
/* 223 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), this.sneak.isEnabled());
/*     */     
/* 225 */     if (this.ungrab.isEnabled())
/* 226 */       ChadUtils.ungrabMouse(); 
/*     */   }
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/* 231 */     if (this.assistMode.isEnabled())
/*     */       return; 
/* 233 */     if (this.sneak.isEnabled())
/* 234 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false); 
/* 235 */     KeybindUtils.stopMovement();
/* 236 */     RotationUtils.reset();
/*     */     
/* 238 */     if (this.ungrab.isEnabled())
/* 239 */       ChadUtils.regrabMouse(); 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onInteract(PlayerInteractEvent event) {
/* 244 */     throwTimer.reset();
/* 245 */     inWaterTimer.reset();
/*     */   }
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/*     */     ItemStack heldItem;
/* 250 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/* 251 */       return;  ArmorSwap swap = (ArmorSwap)CF4M.INSTANCE.moduleManager.getModule("ArmorSwap");
/* 252 */     if (!this.assistMode.isEnabled()) {
/*     */       
/* 254 */       KeybindUtils.stopMovement();
/*     */       
/* 256 */       if (this.antiAfk.isEnabled() && this.afs != AutoFishState.WARP_ISLAND && this.afs != AutoFishState.WARP_SPOT && this.afs != AutoFishState.NAVIGATING && ++ticks > 40) {
/* 257 */         ticks = 0;
/*     */         
/* 259 */         List<KeyBinding> neededPresses = VecUtils.getNeededKeyPresses(mc.field_71439_g.func_174791_d(), startPos);
/* 260 */         neededPresses.forEach(v -> KeyBinding.func_74510_a(v.func_151463_i(), true));
/*     */         
/* 262 */         if (RotationUtils.done) {
/* 263 */           Rotation afk; switch (this.aaState) {
/*     */             case SETUP:
/* 265 */               afk = new Rotation(startRot.getYaw(), startRot.getPitch());
/* 266 */               afk.addYaw((float)(Math.random() * 4.0D - 2.0D));
/* 267 */               afk.addPitch((float)(Math.random() * 4.0D - 2.0D));
/* 268 */               RotationUtils.setup(afk, Long.valueOf(RandomUtil.randBetween(400, 600)));
/* 269 */               this.aaState = AAState.BACK;
/*     */               break;
/*     */             
/*     */             case LOOK:
/* 273 */               RotationUtils.setup(startRot, Long.valueOf(RandomUtil.randBetween(400, 600)));
/* 274 */               this.aaState = AAState.AWAY;
/*     */               break;
/*     */           } 
/*     */ 
/*     */         
/*     */         } 
/*     */       } 
/* 281 */       if (this.whipSlot.getCurrent().intValue() != 0) {
/* 282 */         if (curScStand == null || curScStand.field_70128_L || curSc == null || curSc.field_70128_L) {
/* 283 */           curScStand = null;
/* 284 */           curSc = null;
/*     */         } 
/*     */         
/* 287 */         if (curScStand != null && 
/* 288 */           SkyblockUtils.getMobHp(curScStand) <= 0) {
/* 289 */           if (this.rodSlot.getCurrent().intValue() > 0 && this.rodSlot.getCurrent().intValue() <= 8) {
/* 290 */             mc.field_71439_g.field_71071_by.field_70461_c = this.rodSlot.getCurrent().intValue() - 1;
/*     */           }
/*     */           
/* 293 */           curScStand = null;
/* 294 */           curSc = null;
/*     */         } 
/*     */ 
/*     */         
/* 298 */         if (curSc == null && killing) {
/* 299 */           RotationUtils.setup(startRot, Long.valueOf(this.aimSpeed.getCurrent().intValue()));
/* 300 */           killing = false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 305 */     particleList.removeIf(v -> (System.currentTimeMillis() - v.timeAdded > 1000L));
/*     */     
/* 307 */     if (!this.assistMode.isEnabled()) {
/* 308 */       SkyblockUtils.Location sbLoc = SkyblockUtils.getLocation();
/*     */       
/* 310 */       if (recoverTimer.hasReached(5000L)) {
/* 311 */         String recoverMsg = "";
/*     */         
/* 313 */         switch (sbLoc) {
/*     */           case SETUP:
/* 315 */             ChatUtils.send("Detected player in Lobby, re-warping", new String[0]);
/* 316 */             recoverMsg = "/play skyblock";
/*     */             break;
/*     */           
/*     */           case LOOK:
/* 320 */             ChatUtils.send("Detected player in Limbo, re-warping", new String[0]);
/* 321 */             recoverMsg = "/l";
/*     */             break;
/*     */         } 
/*     */         
/* 325 */         if (!recoverMsg.equals("")) {
/* 326 */           resetVariables();
/*     */           
/* 328 */           RotationUtils.reset();
/* 329 */           mc.field_71439_g.func_71165_d(recoverMsg);
/* 330 */           this.afs = AutoFishState.WARP_ISLAND;
/* 331 */           this.warpState = WarpState.SETUP;
/*     */         } 
/* 333 */         recoverTimer.reset();
/*     */       } 
/*     */     } 
/*     */     
/* 337 */     if ((this.afs == AutoFishState.THROWING || this.afs == AutoFishState.IN_WATER || this.afs == AutoFishState.FISH_BITE || this.afs == AutoFishState.EMBER_OPENWAR || this.afs == AutoFishState.EMBER_WAIT || this.afs == AutoFishState.EMBER_TROPHY) && !this.assistMode.isEnabled()) {
/* 338 */       int rongo = this.maxPlayerRange.getCurrent().intValue();
/* 339 */       if (rongo != 0) {
/* 340 */         String warpName = "";
/* 341 */         boolean warpOut = false;
/*     */         
/* 343 */         for (Entity e : mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72314_b(rongo, (rongo >> 1), rongo), a -> (a instanceof net.minecraft.client.entity.EntityOtherPlayerMP || a instanceof EntityArmorStand))) {
/* 344 */           if (e instanceof EntityArmorStand) {
/* 345 */             ItemStack bushSlot = ((EntityArmorStand)e).func_71124_b(4);
/* 346 */             if (bushSlot != null && Item.func_150898_a((Block)Blocks.field_150330_I) == bushSlot.func_77973_b()) {
/* 347 */               warpOut = true;
/* 348 */               warpName = "Dead Bush"; break;
/*     */             }  continue;
/*     */           } 
/* 351 */           if (!(e instanceof net.minecraft.client.entity.EntityOtherPlayerMP) || 
/* 352 */             fishingMobs.stream().anyMatch(v -> e.func_70005_c_().contains(v)))
/* 353 */             continue;  String formatted = e.func_145748_c_().func_150254_d();
/* 354 */           if (StringUtils.func_76338_a(formatted).equals(formatted)) {
/*     */             continue;
/*     */           }
/* 357 */           if (formatted.startsWith("§r") && !formatted.startsWith("§r§")) {
/*     */             continue;
/*     */           }
/* 360 */           warpOut = true;
/* 361 */           warpName = e.func_70005_c_();
/*     */         } 
/*     */ 
/*     */         
/* 365 */         if (warpOut) {
/* 366 */           if (!this.fishingSpot.getCurrent().equals("None")) {
/* 367 */             ChatUtils.send("Switching lobbies cause a nice person is near you: " + warpName, new String[0]);
/* 368 */             resetVariables();
/*     */             
/* 370 */             RotationUtils.reset();
/* 371 */             this.afs = AutoFishState.WARP_ISLAND;
/* 372 */             this.warpState = WarpState.SETUP;
/*     */           } else {
/* 374 */             mc.field_71439_g.func_71165_d("/warp home");
/* 375 */             ChatUtils.send("A nice person was near you (" + warpName + "), but you didn't configure a fishing spot, so I evacuated you", new String[0]);
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 380 */       if (this.sneak.isEnabled()) {
/* 381 */         KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), true);
/*     */       }
/* 383 */       if (this.killPrio.isEnabled()) {
/* 384 */         findAndSetCurrentSeaCreature();
/*     */         
/* 386 */         if (curSc != null) {
/* 387 */           this.afs = AutoFishState.THROWING;
/* 388 */           throwTimer.reset();
/*     */         } 
/* 390 */       } else if (curScStand != null && 
/* 391 */         SkyblockUtils.getMobHp(curScStand) <= 0) {
/* 392 */         curScStand = null;
/* 393 */         curSc = null;
/*     */         
/* 395 */         if (this.rodSlot.getCurrent().intValue() > 0 && this.rodSlot.getCurrent().intValue() <= 8) {
/* 396 */           mc.field_71439_g.field_71071_by.field_70461_c = this.rodSlot.getCurrent().intValue() - 1;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 402 */     switch (this.afs) {
/*     */       case SETUP:
/* 404 */         if (!this.ember.isEnabled() || openedWar)
/* 405 */           return;  waitTimer.reset();
/* 406 */         openedWar = true;
/* 407 */         if (!mc.field_71439_g.func_82169_q(1).func_82833_r().contains("Ember")) {
/* 408 */           swap.armorSlot.setCurrent(this.emberSlot.getCurrent());
/* 409 */           CF4M.INSTANCE.moduleManager.toggle(swap);
/*     */         } 
/* 411 */         this.afs = AutoFishState.THROWING;
/* 412 */         onObsidianTimer.reset();
/*     */         break;
/*     */       case LOOK:
/* 415 */         if (onObsidianTimer.hasReached(30000L)) {
/* 416 */           onObsidianTimer.reset();
/* 417 */           openedWar = false;
/* 418 */           this.afs = AutoFishState.EMBER_TROPHY;
/*     */         } 
/*     */         break;
/*     */       case WARP:
/* 422 */         if (!this.ember.isEnabled() || openedWar)
/* 423 */           return;  openedWar = true;
/* 424 */         swap.armorSlot.setCurrent(this.trophySlot.getCurrent());
/* 425 */         CF4M.INSTANCE.moduleManager.toggle(swap);
/* 426 */         if (!mc.field_71439_g.func_82169_q(1).func_82833_r().contains("Ember")) {
/* 427 */           this.afs = AutoFishState.EMBER_TROPHY;
/*     */           break;
/*     */         } 
/* 430 */         this.afs = AutoFishState.IN_WATER;
/* 431 */         flash = true;
/*     */         break;
/*     */       case null:
/* 434 */         if (warpTimer.hasReached(5000L)) {
/* 435 */           Optional<Location> loc = fishingJson.locations.stream().filter(v -> v.name.equals(this.fishingSpot.getCurrent())).findFirst();
/* 436 */           loc.ifPresent(location -> currentLocation = location);
/* 437 */           if (currentLocation != null) {
/* 438 */             ChatUtils.send("Navigating to: " + currentLocation.name.split(" - ")[1], new String[0]);
/* 439 */             mc.field_71439_g.func_71165_d("/warp home");
/* 440 */             warpTimer.reset();
/* 441 */             this.afs = AutoFishState.WARP_SPOT; break;
/*     */           } 
/* 443 */           ChatUtils.send("Couldn't determine location, very weird", new String[0]);
/* 444 */           CF4M.INSTANCE.moduleManager.toggle(this);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 450 */         if (warpTimer.hasReached(5000L)) {
/* 451 */           String warpLoc = currentLocation.name.split(" - ")[0];
/* 452 */           mc.field_71439_g.func_71165_d(warpLoc);
/* 453 */           warpTimer.reset();
/* 454 */           path = null;
/* 455 */           this.afs = AutoFishState.NAVIGATING;
/*     */         } 
/*     */         break;
/*     */       
/*     */       case null:
/* 460 */         if (warpTimer.hasReached(5000L) && path == null) {
/* 461 */           path = new ArrayList<>(currentLocation.path);
/* 462 */           oldPos = null;
/* 463 */           KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), true);
/* 464 */           warpTimer.reset();
/* 465 */           this.warpState = WarpState.SETUP;
/*     */         } 
/*     */         break;
/*     */       
/*     */       case null:
/* 470 */         if (mc.field_71439_g.field_71104_cf == null && throwTimer.hasReached(this.recastDelay.getCurrent().intValue())) {
/* 471 */           if (this.rodSlot.getCurrent().intValue() > 0 && this.rodSlot.getCurrent().intValue() <= 8) {
/* 472 */             mc.field_71439_g.field_71071_by.field_70461_c = this.rodSlot.getCurrent().intValue() - 1;
/*     */           }
/* 474 */           mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 475 */           throwTimer.reset();
/* 476 */           inWaterTimer.reset();
/* 477 */           flash = false;
/* 478 */           if (this.ember.isEnabled()) {
/* 479 */             this.afs = AutoFishState.EMBER_WAIT;
/*     */             break;
/*     */           } 
/* 482 */           this.afs = AutoFishState.IN_WATER; break;
/* 483 */         }  if (throwTimer.hasReached(2500L) && mc.field_71439_g.field_71104_cf != null) {
/* 484 */           this.afs = AutoFishState.FISH_BITE;
/*     */         }
/*     */         break;
/*     */       
/*     */       case null:
/* 489 */         heldItem = mc.field_71439_g.func_70694_bm();
/* 490 */         if (heldItem != null && heldItem.func_77973_b() == Items.field_151112_aM) {
/* 491 */           if (throwTimer.hasReached(500L) && mc.field_71439_g.field_71104_cf != null) {
/* 492 */             if (mc.field_71439_g.field_71104_cf.func_70090_H() || mc.field_71439_g.field_71104_cf.func_180799_ab()) {
/*     */               
/* 494 */               if (!this.assistMode.isEnabled() && 
/* 495 */                 !this.killPrio.isEnabled()) {
/* 496 */                 findAndSetCurrentSeaCreature();
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 501 */               if (!oldBobberInWater) {
/* 502 */                 if (this.petSwap.isEnabled()) {
/* 503 */                   KeybindUtils.stopMovement();
/* 504 */                   CF4M.INSTANCE.moduleManager.toggle("PetSwap");
/*     */                 } 
/*     */                 
/* 507 */                 oldBobberInWater = true;
/* 508 */                 inWaterTimer.reset();
/*     */               } 
/*     */               
/* 511 */               EntityFishHook bobber = mc.field_71439_g.field_71104_cf;
/*     */               
/* 513 */               if ((flash || inWaterTimer.hasReached(this.slugMode.isEnabled() ? 30000L : 2500L)) && ((Math.abs(bobber.field_70159_w) < 0.01D && Math.abs(bobber.field_70179_y) < 0.01D) || flash)) {
/* 514 */                 double movement = bobber.field_70163_u - oldBobberPosY;
/* 515 */                 oldBobberPosY = bobber.field_70163_u;
/*     */                 
/* 517 */                 if ((movement < -0.04D && bobberIsNearParticles(bobber)) || bobber.field_146043_c != null)
/* 518 */                   this.afs = AutoFishState.FISH_BITE; 
/*     */               }  break;
/*     */             } 
/* 521 */             if (inWaterTimer.hasReached(2500L) && !this.assistMode.isEnabled() && 
/* 522 */               !this.ember.isEnabled()) this.afs = AutoFishState.FISH_BITE;  break;
/*     */           } 
/* 524 */           if (throwTimer.hasReached(1000L) && mc.field_71439_g.field_71104_cf == null && !this.assistMode.isEnabled() && 
/* 525 */             !this.ember.isEnabled()) {
/* 526 */             throwTimer.reset();
/* 527 */             this.afs = AutoFishState.THROWING;
/*     */           }  break;
/*     */         } 
/* 530 */         if (this.killPrio.isEnabled() && !this.assistMode.isEnabled()) {
/* 531 */           RotationUtils.setup(startRot, Long.valueOf(this.recastDelay.getCurrent().intValue()));
/* 532 */           oldBobberInWater = false;
/* 533 */           throwTimer.reset();
/* 534 */           this.afs = AutoFishState.THROWING; break;
/* 535 */         }  if (this.rodSlot.getCurrent().intValue() > 0 && this.rodSlot.getCurrent().intValue() <= 8 && !this.assistMode.isEnabled()) {
/* 536 */           mc.field_71439_g.field_71071_by.field_70461_c = this.rodSlot.getCurrent().intValue() - 1;
/*     */         }
/*     */         break;
/*     */       
/*     */       case null:
/* 541 */         if (!this.assistMode.isEnabled() && 
/* 542 */           this.rodSlot.getCurrent().intValue() > 0 && this.rodSlot.getCurrent().intValue() <= 8) {
/* 543 */           mc.field_71439_g.field_71071_by.field_70461_c = this.rodSlot.getCurrent().intValue() - 1;
/*     */         }
/*     */         
/* 546 */         mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 547 */         if (!this.assistMode.isEnabled()) RotationUtils.setup(startRot, Long.valueOf(this.recastDelay.getCurrent().intValue())); 
/* 548 */         oldBobberInWater = false;
/* 549 */         throwTimer.reset();
/* 550 */         inWaterTimer.reset();
/* 551 */         if (this.ember.isEnabled()) {
/* 552 */           this.afs = AutoFishState.EMBER_OPENWAR;
/* 553 */           openedWar = false;
/*     */           break;
/*     */         } 
/* 556 */         if (this.assistMode.isEnabled()) {
/* 557 */           this.afs = AutoFishState.IN_WATER; break;
/*     */         } 
/* 559 */         this.afs = AutoFishState.THROWING;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onSecond(SecondEvent event) {
/* 567 */     if (trophyArmor || emberArmor) {
/* 568 */       if (mc.field_71462_r == null) {
/* 569 */         mc.field_71439_g.func_71165_d("/wardrobe");
/* 570 */       } else if (emberArmor) {
/* 571 */         mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, this.emberSlot.getCurrent().intValue() + 35, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 572 */         emberArmor = false;
/* 573 */         this.afs = AutoFishState.THROWING;
/* 574 */       } else if (trophyArmor) {
/* 575 */         mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, this.trophySlot.getCurrent().intValue() + 35, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 576 */         trophyArmor = false;
/* 577 */         this.afs = AutoFishState.IN_WATER;
/*     */       } 
/* 579 */       mc.field_71439_g.func_71053_j();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void findAndSetCurrentSeaCreature() {
/* 584 */     if (this.whipSlot.getCurrent().intValue() != 0) {
/* 585 */       int ranga = this.scRange.getCurrent().intValue();
/* 586 */       List<Entity> mobs = mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72314_b(ranga, (ranga >> 1), ranga), e -> e instanceof EntityArmorStand);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 591 */       Optional<Entity> filtered = mobs.stream().filter(v -> (v.func_70032_d((Entity)mc.field_71439_g) < ranga && !v.func_70005_c_().contains(mc.field_71439_g.func_70005_c_()) && fishingMobs.stream().anyMatch(()))).min(Comparator.comparing(v -> Float.valueOf(v.func_70032_d((Entity)mc.field_71439_g))));
/*     */       
/* 593 */       if (filtered.isPresent()) {
/* 594 */         curScStand = (EntityArmorStand)filtered.get();
/* 595 */         curSc = SkyblockUtils.getEntityCuttingOtherEntity((Entity)curScStand, null);
/* 596 */         clicksLeft = (int)Math.ceil((SkyblockUtils.getMobHp(curScStand) / this.hypDamage.getCurrent().intValue()));
/* 597 */         if (curSc != null && SkyblockUtils.getMobHp(curScStand) > 0) {
/* 598 */           killing = true;
/* 599 */           switch (this.killMode.getCurrent()) {
/*     */             case "Left":
/* 601 */               RotationUtils.setup(RotationUtils.getRotation(curSc.func_174791_d().func_72441_c(0.0D, curSc.func_70047_e(), 0.0D)), Long.valueOf(this.aimSpeed.getCurrent().intValue()));
/*     */               break;
/*     */             
/*     */             case "Right":
/* 605 */               RotationUtils.setup(RotationUtils.getRotation(curSc.func_174791_d()), Long.valueOf(this.aimSpeed.getCurrent().intValue()));
/*     */               break;
/*     */             
/*     */             case "Hyperion":
/* 609 */               RotationUtils.setup(new Rotation(mc.field_71439_g.field_70177_z, 90.0F), Long.valueOf(this.aimSpeed.getCurrent().intValue()));
/*     */               break;
/*     */           } 
/* 612 */         } else if (SkyblockUtils.getMobHp(curScStand) <= 0) {
/* 613 */           curScStand = null;
/* 614 */           curSc = null;
/*     */           
/* 616 */           if (this.rodSlot.getCurrent().intValue() > 0 && this.rodSlot.getCurrent().intValue() <= 8) {
/* 617 */             mc.field_71439_g.field_71071_by.field_70461_c = this.rodSlot.getCurrent().intValue() - 1;
/*     */           }
/*     */         } 
/* 620 */       } else if (RotationUtils.done) {
/* 621 */         curScStand = null;
/* 622 */         curSc = null;
/*     */         
/* 624 */         if (this.rodSlot.getCurrent().intValue() > 0 && this.rodSlot.getCurrent().intValue() <= 8) {
/* 625 */           mc.field_71439_g.field_71071_by.field_70461_c = this.rodSlot.getCurrent().intValue() - 1;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onPlaySound(SoundEvent event) {
/* 633 */     if (event.name.equals("mob.guardian.elder.idle")) {
/* 634 */       if (this.debug.isEnabled()) {
/* 635 */         ChatUtils.send("Flash proc detected", new String[0]);
/*     */       }
/* 637 */       flash = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRenderTick(Render3DEvent event) {
/* 643 */     if (curSc != null) {
/* 644 */       Render3DUtils.renderBoundingBox(curSc, event.partialTicks, -16776961);
/*     */     }
/* 646 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*     */       return; 
/* 648 */     if (!RotationUtils.done) {
/* 649 */       RotationUtils.update();
/*     */     }
/*     */     
/* 652 */     if (this.afs == AutoFishState.NAVIGATING && path != null) {
/* 653 */       switch (this.warpState) {
/*     */         case SETUP:
/* 655 */           if (path.size() > 0) {
/* 656 */             if (warpTimer.hasReached(this.warpTime.getCurrent().intValue()) && !mc.field_71439_g.func_180425_c().equals(oldPos)) {
/* 657 */               PathPoint a = path.get(0);
/* 658 */               path.remove(0);
/* 659 */               RotationUtils.setup(RotationUtils.getRotation(new Vec3(a.x, a.y, a.z)), Long.valueOf(this.warpLookTime.getCurrent().intValue()));
/* 660 */               oldPos = mc.field_71439_g.func_180425_c();
/* 661 */               this.warpState = WarpState.LOOK; break;
/* 662 */             }  if (warpTimer.hasReached(2500L)) {
/* 663 */               ChatUtils.send("Got stuck while tp'ing, re-navigating", new String[0]);
/* 664 */               mc.field_71439_g.func_71165_d("/l");
/* 665 */               recoverTimer.reset();
/* 666 */               warpTimer.reset();
/*     */             }  break;
/*     */           } 
/* 669 */           if (warpTimer.hasReached(1000L)) {
/* 670 */             if (!this.sneak.isEnabled()) {
/* 671 */               KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
/*     */             }
/*     */             
/* 674 */             startRot = currentLocation.rotation;
/* 675 */             startPos = mc.field_71439_g.func_174791_d();
/* 676 */             RotationUtils.setup(startRot, Long.valueOf(this.recastDelay.getCurrent().intValue()));
/*     */             
/* 678 */             throwTimer.reset();
/* 679 */             this.afs = AutoFishState.THROWING;
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case LOOK:
/* 685 */           if (System.currentTimeMillis() <= RotationUtils.endTime) {
/* 686 */             RotationUtils.update(); break;
/*     */           } 
/* 688 */           RotationUtils.update();
/* 689 */           warpTimer.reset();
/* 690 */           this.warpState = WarpState.WARP;
/*     */           break;
/*     */ 
/*     */         
/*     */         case WARP:
/* 695 */           if (warpTimer.hasReached(this.warpTime.getCurrent().intValue())) {
/* 696 */             mc.field_71439_g.field_71071_by.field_70461_c = this.aotvSlot.getCurrent().intValue() - 1;
/* 697 */             mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 698 */             warpTimer.reset();
/* 699 */             this.warpState = WarpState.SETUP;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/* 707 */     if (curSc != null) {
/* 708 */       if (this.killMode.getCurrent().equals("Hyperion")) {
/* 709 */         if (killTimer.hasReached(125L)) {
/* 710 */           if (this.whipSlot.getCurrent().intValue() > 0 && this.whipSlot.getCurrent().intValue() <= 8) {
/* 711 */             mc.field_71439_g.field_71071_by.field_70461_c = this.whipSlot.getCurrent().intValue() - 1;
/*     */           }
/*     */           
/* 714 */           if (mc.field_71439_g.field_70125_A > 89.0F) {
/* 715 */             if (clicksLeft-- > 0) {
/* 716 */               KeybindUtils.rightClick();
/*     */             }
/*     */             
/* 719 */             killTimer.reset();
/*     */           }
/*     */         
/*     */         } 
/* 723 */       } else if (killTimer.hasReached(125L)) {
/* 724 */         if (this.whipSlot.getCurrent().intValue() > 0 && this.whipSlot.getCurrent().intValue() <= 8) {
/* 725 */           mc.field_71439_g.field_71071_by.field_70461_c = this.whipSlot.getCurrent().intValue() - 1;
/*     */         }
/* 727 */         switch (this.killMode.getCurrent()) {
/*     */           case "Left":
/* 729 */             KeybindUtils.leftClick();
/*     */             break;
/*     */           
/*     */           case "Right":
/* 733 */             KeybindUtils.rightClick();
/*     */             break;
/*     */         } 
/* 736 */         killTimer.reset();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onRender2D(Render2DEvent event) {
/* 744 */     if (!((Minecraft.func_71410_x()).field_71462_r instanceof xyz.apfelmus.cheeto.client.clickgui.ConfigGUI) && (Minecraft.func_71410_x()).field_71462_r != null)
/*     */       return; 
/* 746 */     if (this.debug.isEnabled()) {
/* 747 */       EntityFishHook bobber = mc.field_71439_g.field_71104_cf;
/* 748 */       double movement = 0.0D;
/* 749 */       if (bobber != null) {
/* 750 */         movement = bobber.field_70163_u - oldBobberPosY;
/*     */       }
/* 752 */       boolean first = (flash || inWaterTimer.hasReached(this.slugMode.isEnabled() ? 30000L : 2500L));
/* 753 */       boolean second = flash;
/* 754 */       if (bobber != null) {
/* 755 */         second = ((Math.abs(bobber.field_70159_w) < 0.01D && Math.abs(bobber.field_70179_y) < 0.01D) || flash);
/*     */       }
/* 757 */       List<String> texts = new ArrayList<>(Arrays.asList(new String[] {
/* 758 */               String.format("InWaterTimer: %d", new Object[] { Long.valueOf(inWaterTimer.getDelay())
/* 759 */                 }), String.format("Flash: %s", new Object[] { Boolean.valueOf(flash)
/* 760 */                 }), String.format("Movement: %f", new Object[] { Double.valueOf(movement)
/* 761 */                 }), String.format("First Bool: %s", new Object[] { Boolean.valueOf(first)
/* 762 */                 }), String.format("Second Bool: %s", new Object[] { Boolean.valueOf(second) })
/*     */             }));
/*     */       
/* 765 */       ScaledResolution scaled = new ScaledResolution(Minecraft.func_71410_x());
/* 766 */       int sf = ResManager.getScaleFactor();
/* 767 */       int drawX = (int)((scaled.func_78326_a() * sf) * this.debugX.getCurrent().intValue() / 100.0D);
/* 768 */       int drawY = (int)((scaled.func_78328_b() * sf) * this.debugY.getCurrent().intValue() / 100.0D);
/* 769 */       for (String text : texts) {
/* 770 */         FontUtils.normal.drawChromaString(text, drawX, drawY);
/* 771 */         drawY += FontUtils.normal.getFontHeight() + 1;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onWorldUnload(WorldUnloadEvent event) {
/* 778 */     if (CF4M.INSTANCE.moduleManager.isEnabled(this) && this.fishingSpot.getCurrent().equals("None")) {
/* 779 */       ChatUtils.send("Your server closed and you didn't have a Spot configured, stopping AutoFish", new String[0]);
/* 780 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */       
/*     */       return;
/*     */     } 
/* 784 */     if (!this.fishingSpot.getCurrent().equals("None") && this.afs != AutoFishState.WARP_ISLAND && this.afs != AutoFishState.WARP_SPOT && this.afs != AutoFishState.NAVIGATING) {
/* 785 */       this.afs = AutoFishState.THROWING;
/* 786 */       this.aaState = AAState.AWAY;
/* 787 */       throwTimer.reset();
/* 788 */       inWaterTimer.reset();
/* 789 */       warpTimer.reset();
/* 790 */       recoverTimer.reset();
/* 791 */       ticks = 0;
/* 792 */       oldBobberPosY = 0.0D;
/* 793 */       oldBobberInWater = false;
/* 794 */       curScStand = null;
/* 795 */       curSc = null;
/* 796 */       killing = true;
/* 797 */       particleList.clear();
/*     */       
/* 799 */       RotationUtils.reset();
/* 800 */       this.afs = AutoFishState.WARP_ISLAND;
/* 801 */       this.warpState = WarpState.SETUP;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onChat(ClientChatReceivedEvent event) {
/* 807 */     if (event.type == 2)
/*     */       return; 
/* 809 */     String unformatted = event.message.func_150260_c();
/* 810 */     String stripped = StringUtils.func_76338_a(unformatted);
/*     */     
/* 812 */     if (stripped.startsWith(" ☠ You ")) {
/* 813 */       Multithreading.runAsync(() -> {
/*     */             CF4M.INSTANCE.moduleManager.toggle(this);
/*     */             ChatUtils.send("Bozo you died while AutoFishing!", new String[0]);
/*     */             if (this.warpOutOnDeath.isEnabled()) {
/*     */               ChatUtils.send("I'll send you back to your island in 10 seconds... noob", new String[0]);
/*     */               try {
/*     */                 Thread.sleep(10000L);
/* 820 */               } catch (InterruptedException e) {
/*     */                 e.printStackTrace();
/*     */               } 
/*     */               mc.field_71439_g.func_71165_d("/warp home");
/*     */             } else {
/*     */               ChatUtils.send("I'll leave you here to rot, you deserve it", new String[0]);
/*     */               try {
/*     */                 Thread.sleep(2500L);
/*     */                 ChatUtils.send("Just a joke, time to fish again!", new String[0]);
/*     */                 Thread.sleep(1500L);
/*     */                 CF4M.INSTANCE.moduleManager.toggle(this);
/* 831 */               } catch (InterruptedException e) {
/*     */                 throw new RuntimeException(e);
/*     */               } 
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public static void handleParticles(S2APacketParticles packet) {
/* 840 */     if (packet.func_179749_a() == EnumParticleTypes.WATER_WAKE || packet.func_179749_a() == EnumParticleTypes.SMOKE_NORMAL) {
/* 841 */       particleList.add(new ParticleEntry(new Vec3(packet.func_149220_d(), packet.func_149226_e(), packet.func_149225_f()), System.currentTimeMillis()));
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean bobberIsNearParticles(EntityFishHook bobber) {
/* 846 */     return particleList.stream().anyMatch(v -> (VecUtils.getHorizontalDistance(bobber.func_174791_d(), v.position) < 0.2D));
/*     */   }
/*     */   
/*     */   private static class ParticleEntry {
/*     */     public Vec3 position;
/*     */     public long timeAdded;
/*     */     
/*     */     public ParticleEntry(Vec3 position, long timeAdded) {
/* 854 */       this.position = position;
/* 855 */       this.timeAdded = timeAdded;
/*     */     }
/*     */   }
/*     */   
/*     */   private List<String> getFishingSpotNames() {
/* 860 */     List<String> ret = new ArrayList<>();
/* 861 */     ret.add("None");
/* 862 */     if (fishingJson == null) {
/* 863 */       ret.add("Buy Sigma Nigger");
/*     */     } else {
/* 865 */       ret.addAll((Collection<? extends String>)fishingJson.locations.stream()
/* 866 */           .map(v -> v.name)
/* 867 */           .collect(Collectors.toList()));
/*     */     } 
/* 869 */     return ret;
/*     */   }
/*     */   
/*     */   private void resetVariables() {
/* 873 */     if (this.ember.isEnabled()) {
/* 874 */       this.afs = AutoFishState.EMBER_OPENWAR;
/*     */     } else {
/* 876 */       this.afs = AutoFishState.THROWING;
/*     */     } 
/* 878 */     this.aaState = AAState.AWAY;
/* 879 */     throwTimer.reset();
/* 880 */     inWaterTimer.reset();
/* 881 */     warpTimer.reset();
/* 882 */     recoverTimer.reset();
/* 883 */     ticks = 0;
/* 884 */     oldBobberPosY = 0.0D;
/* 885 */     oldBobberInWater = false;
/* 886 */     curScStand = null;
/* 887 */     curSc = null;
/* 888 */     killing = true;
/* 889 */     clicksLeft = 0;
/* 890 */     flash = false;
/* 891 */     particleList.clear();
/* 892 */     trophyArmor = false;
/* 893 */     emberArmor = false;
/* 894 */     openedWar = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\player\AutoFish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */