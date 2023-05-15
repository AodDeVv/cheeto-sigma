/*     */ package xyz.apfelmus.cheeto.client.modules.world;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.SoundCategory;
/*     */ import net.minecraft.client.gui.GuiDisconnected;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.Session;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientChatReceivedEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.StringSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChadUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.RandomUtil;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.TimeHelper;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.InventoryUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ @Module(name = "AutoFarm", category = Category.WORLD)
/*     */ public class AutoFarm {
/*     */   @Setting(name = "HoeSlot")
/*  46 */   private IntegerSetting hoeSlot = new IntegerSetting(
/*  47 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "FailSafeDelay")
/*  48 */   private IntegerSetting failSafeDelay = new IntegerSetting(
/*  49 */       Integer.valueOf(5000), Integer.valueOf(0), Integer.valueOf(10000)); @Setting(name = "CPUSaver", description = "Limits FPS and Render Distance")
/*  50 */   private BooleanSetting cpuSaver = new BooleanSetting(true);
/*     */   @Setting(name = "AutoTab")
/*  52 */   private BooleanSetting autoTab = new BooleanSetting(true);
/*     */   @Setting(name = "SoundAlerts")
/*  54 */   private BooleanSetting soundAlerts = new BooleanSetting(true);
/*     */   @Setting(name = "WebhookUpdates", description = "Will send your SessionID")
/*  56 */   private BooleanSetting webhookUpdates = new BooleanSetting(false);
/*     */   @Setting(name = "WebhookURL", description = "Where to send status updates (RAT)")
/*  58 */   private StringSetting webhookUrl = new StringSetting("");
/*     */   @Setting(name = "Direction")
/*  60 */   private ModeSetting direction = new ModeSetting("NORTH", 
/*  61 */       Arrays.asList(new String[] { "NORTH", "EAST", "SOUTH", "WEST" })); @Setting(name = "FarmType", description = "Type of the S-Shaped Farm")
/*  62 */   private ModeSetting farmType = new ModeSetting("Vertical", 
/*  63 */       Arrays.asList(new String[] { "Vertical", "Horizontal" })); @Setting(name = "FastBreak", description = "Extra blocks to break per tick")
/*  64 */   public IntegerSetting fastBreak = new IntegerSetting(
/*  65 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(3)); @Setting(name = "AvoidJacobs", description = "Will Pause during Jacobs")
/*  66 */   private BooleanSetting avoidJacobs = new BooleanSetting(false);
/*     */   @Setting(name = "JacobsAmount", description = "Amount it will pause at")
/*  68 */   private IntegerSetting jacobsAmount = new IntegerSetting(
/*  69 */       Integer.valueOf(480000), Integer.valueOf(0), Integer.valueOf(1000000));
/*     */   
/*  71 */   private Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*  73 */   private int stuckTicks = 0;
/*     */   
/*     */   private BlockPos oldPos;
/*     */   private BlockPos curPos;
/*     */   private boolean invFull = false;
/*     */   private List<ItemStack> oldInv;
/*  79 */   private int oldInvCount = 0;
/*     */   
/*  81 */   private final int GREEN = 3066993;
/*  82 */   private final int ORANGE = 15439360;
/*  83 */   private final int RED = 15158332;
/*  84 */   private final int BLUE = 1689596; private boolean banned; private FarmingState farmingState; private FarmingDirection farmingDirection;
/*     */   private AlertState alertState;
/*     */   private SameInvState sameInvState;
/*     */   private IsRebootState isRebootState;
/*     */   
/*  89 */   enum FarmingState { START_FARMING,
/*  90 */     SET_ANGLES,
/*  91 */     PRESS_KEYS,
/*  92 */     FARMING,
/*  93 */     STOP_FARMING,
/*  94 */     RECOVER; }
/*     */ 
/*     */   
/*     */   enum FarmingDirection {
/*  98 */     LEFT,
/*  99 */     RIGHT;
/*     */   }
/*     */   
/*     */   enum AlertState {
/* 103 */     CHILLING,
/* 104 */     TURNUP,
/* 105 */     PLAY,
/* 106 */     TURNDOWN;
/*     */   }
/*     */   
/*     */   enum SameInvState {
/* 110 */     CHILLING,
/* 111 */     UNPRESS;
/*     */   }
/*     */   
/*     */   enum IsRebootState {
/* 115 */     ISLAND,
/* 116 */     HUB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   private TimeHelper alertTimer = new TimeHelper();
/* 126 */   private TimeHelper recoverTimer = new TimeHelper();
/*     */   
/* 128 */   private Map<SoundCategory, Float> oldSounds = new HashMap<>();
/* 129 */   private String recoverStr = " ";
/*     */   
/*     */   private boolean recoverBool = false;
/*     */   private boolean islandReboot = false;
/*     */   private boolean jacob = false;
/* 134 */   private List<String> msgs = new ArrayList<>(Arrays.asList(new String[] { "hey?", "wtf?? why am i here", "What is this place!", "Hello?", "helpp where am i?" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/* 144 */     this.farmingState = FarmingState.START_FARMING;
/* 145 */     this.farmingDirection = FarmingDirection.LEFT;
/* 146 */     this.alertState = AlertState.CHILLING;
/* 147 */     this.sameInvState = SameInvState.CHILLING;
/* 148 */     this.isRebootState = IsRebootState.ISLAND;
/*     */     
/* 150 */     this.islandReboot = false;
/* 151 */     this.banned = false;
/*     */     
/* 153 */     if (this.autoTab.isEnabled()) {
/* 154 */       ChadUtils.ungrabMouse();
/*     */     }
/*     */     
/* 157 */     if (this.cpuSaver.isEnabled()) {
/* 158 */       ChadUtils.improveCpuUsage();
/*     */     }
/*     */   }
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/* 164 */     KeyBinding.func_74506_a();
/*     */     
/* 166 */     if (this.autoTab.isEnabled()) {
/* 167 */       ChadUtils.regrabMouse();
/*     */     }
/*     */     
/* 170 */     if (this.cpuSaver.isEnabled()) {
/* 171 */       ChadUtils.revertCpuUsage();
/*     */     }
/*     */     
/* 174 */     if (this.soundAlerts.isEnabled() && this.alertState != AlertState.CHILLING) {
/* 175 */       for (SoundCategory category : SoundCategory.values()) {
/* 176 */         this.mc.field_71474_y.func_151439_a(category, ((Float)this.oldSounds.get(category)).floatValue());
/*     */       }
/* 178 */       this.alertState = AlertState.CHILLING;
/*     */     } 
/*     */   }
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/*     */     Rotation rot;
/* 184 */     if (this.mc.field_71462_r != null && !(this.mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat) && !(this.mc.field_71462_r instanceof GuiDisconnected))
/*     */       return; 
/* 186 */     switch (this.farmingState) {
/*     */       case LEFT:
/* 188 */         this.oldInv = null;
/*     */         
/* 190 */         rot = new Rotation(0.0F, 3.0F);
/*     */         
/* 192 */         switch (this.direction.getCurrent()) {
/*     */           case "WEST":
/* 194 */             rot.setYaw(90.0F);
/*     */             break;
/*     */           
/*     */           case "NORTH":
/* 198 */             rot.setYaw(180.0F);
/*     */             break;
/*     */           
/*     */           case "EAST":
/* 202 */             rot.setYaw(-90.0F);
/*     */             break;
/*     */         } 
/*     */         
/* 206 */         RotationUtils.setup(rot, Long.valueOf(1000L));
/*     */         
/* 208 */         this.farmingState = FarmingState.SET_ANGLES;
/*     */         break;
/*     */ 
/*     */       
/*     */       case RIGHT:
/* 213 */         if (this.hoeSlot.getCurrent().intValue() > 0 && this.hoeSlot.getCurrent().intValue() <= 8)
/* 214 */           this.mc.field_71439_g.field_71071_by.field_70461_c = this.hoeSlot.getCurrent().intValue() - 1; 
/* 215 */         pressKeys();
/*     */         
/* 217 */         if (this.mc.field_71439_g.field_71075_bZ.field_75100_b) {
/* 218 */           KeyBinding.func_74510_a(this.mc.field_71474_y.field_74311_E.func_151463_i(), true); break;
/*     */         } 
/* 220 */         KeyBinding.func_74510_a(this.mc.field_71474_y.field_74311_E.func_151463_i(), false);
/* 221 */         this.farmingState = FarmingState.FARMING;
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case null:
/* 227 */         if (!this.banned && 
/* 228 */           this.mc.field_71462_r instanceof GuiDisconnected) {
/* 229 */           GuiDisconnected gd = (GuiDisconnected)this.mc.field_71462_r;
/* 230 */           IChatComponent message = (IChatComponent)ObfuscationReflectionHelper.getPrivateValue(GuiDisconnected.class, gd, new String[] { "message", "field_146304_f" });
/* 231 */           StringBuilder reason = new StringBuilder();
/*     */           
/* 233 */           for (IChatComponent cc : message.func_150253_a()) {
/* 234 */             reason.append(cc.func_150260_c());
/*     */           }
/*     */           
/* 237 */           String re = reason.toString();
/*     */           
/* 239 */           re = re.replace("\r", "\\r").replace("\n", "\\n");
/*     */           
/* 241 */           if (re.contains("banned")) {
/* 242 */             this.banned = true;
/*     */             
/* 244 */             sendWebhook("BEAMED", "You got beamed shitter!\\r\\n" + re, 15158332, true);
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 250 */         if (this.recoverTimer.hasReached(2000L)) {
/* 251 */           int[] mods; double ceilY; BlockPos sideBlock; BlockPos standing; IBlockState standBs; IBlockState sideBs; BlockPos backBlock; ItemStack heldItem; List<ItemStack> inv; switch (SkyblockUtils.getLocation()) {
/*     */             case LEFT:
/* 253 */               KeyBinding.func_74506_a();
/*     */               break;
/*     */             
/*     */             case RIGHT:
/* 257 */               if (this.recoverBool) {
/* 258 */                 ChatUtils.send("Recovered! Starting to farm again!", new String[0]);
/* 259 */                 sendWebhook("RECOVERED", "Recovered!\\r\\nStarting to farm again!", 3066993, false);
/* 260 */                 this.recoverBool = false;
/*     */               } 
/*     */               
/* 263 */               mods = getModifiers(this.direction.getCurrent());
/* 264 */               ceilY = Math.ceil(this.mc.field_71439_g.field_70163_u);
/*     */ 
/*     */               
/* 267 */               if (this.farmingDirection == FarmingDirection.LEFT) {
/* 268 */                 sideBlock = new BlockPos(this.mc.field_71439_g.field_70165_t + mods[0], ceilY, this.mc.field_71439_g.field_70161_v + mods[1]);
/*     */               } else {
/* 270 */                 sideBlock = new BlockPos(this.mc.field_71439_g.field_70165_t + (mods[0] * -1), ceilY, this.mc.field_71439_g.field_70161_v + (mods[1] * -1));
/*     */               } 
/*     */ 
/*     */               
/* 274 */               if (this.farmType.getCurrent().equals("Vertical")) {
/* 275 */                 standing = new BlockPos(this.mc.field_71439_g.field_70165_t, ceilY - 1.0D, this.mc.field_71439_g.field_70161_v);
/*     */               } else {
/* 277 */                 standing = new BlockPos(this.mc.field_71439_g.field_70165_t - mods[1], ceilY, this.mc.field_71439_g.field_70161_v + mods[0]);
/*     */               } 
/*     */               
/* 280 */               standBs = this.mc.field_71441_e.func_180495_p(standing);
/* 281 */               sideBs = this.mc.field_71441_e.func_180495_p(sideBlock);
/*     */               
/* 283 */               if (sideBs != null && sideBs.func_177230_c() != Blocks.field_150350_a && sideBs.func_177230_c() != Blocks.field_150355_j && sideBs.func_177230_c() != Blocks.field_150444_as && 
/* 284 */                 standBs != null && standBs.func_177230_c() != Blocks.field_150350_a && standBs.func_177230_c() != Blocks.field_150444_as) {
/* 285 */                 this.farmingDirection = (this.farmingDirection == FarmingDirection.LEFT) ? FarmingDirection.RIGHT : FarmingDirection.LEFT;
/* 286 */                 pressKeys();
/*     */               } 
/*     */ 
/*     */               
/* 290 */               backBlock = new BlockPos(this.mc.field_71439_g.field_70165_t + mods[1], ceilY, this.mc.field_71439_g.field_70161_v - mods[0]);
/* 291 */               if (this.farmType.getCurrent().equals("Horizontal") && standBs != null && standBs.func_177230_c() == Blocks.field_150350_a) {
/* 292 */                 KeyBinding.func_74510_a(this.mc.field_71474_y.field_74351_w.func_151463_i(), true);
/* 293 */               } else if (standBs != null && standBs.func_177230_c() != Blocks.field_150350_a && Math.sqrt(backBlock.func_177957_d(this.mc.field_71439_g.field_70165_t, ceilY, this.mc.field_71439_g.field_70161_v)) >= 1.1D) {
/* 294 */                 KeyBinding.func_74510_a(this.mc.field_71474_y.field_74351_w.func_151463_i(), false);
/*     */               } 
/*     */               
/* 297 */               if (this.avoidJacobs.isEnabled() && 
/* 298 */                 SkyblockUtils.isJacobs()) {
/* 299 */                 int count = SkyblockUtils.getJacobsCount();
/*     */                 
/* 301 */                 if (count > this.jacobsAmount.getCurrent().intValue()) {
/* 302 */                   KeybindUtils.stopMovement();
/* 303 */                   KeyBinding.func_74510_a(this.mc.field_71474_y.field_74312_F.func_151463_i(), false);
/*     */                 } 
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 309 */               if (standBs != null && standBs.func_177230_c() == Blocks.field_150357_h) {
/* 310 */                 sendWebhook("ADMIN CHECK", "Stopped Farming:\\r\\nBitch you are getting Admin checked - do something!", 15158332, true);
/* 311 */                 if (this.alertState == AlertState.CHILLING)
/* 312 */                   this.alertState = AlertState.TURNUP; 
/* 313 */                 (new Thread(() -> {
/*     */                       try {
/*     */                         Thread.sleep(1500L);
/*     */                         this.farmingState = FarmingState.STOP_FARMING;
/*     */                         Thread.sleep(2500L);
/*     */                         this.mc.field_71439_g.func_71165_d(this.msgs.get(RandomUtil.randBetween(0, this.msgs.size() - 1)));
/* 319 */                       } catch (InterruptedException e) {
/*     */                         e.printStackTrace();
/*     */                       } 
/* 322 */                     })).start();
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 327 */               heldItem = this.mc.field_71439_g.func_70694_bm();
/* 328 */               if (heldItem != null && (
/* 329 */                 heldItem.func_77973_b() == Items.field_151098_aY || heldItem.func_77973_b() == Items.field_151148_bJ)) {
/* 330 */                 sendWebhook("MAPTCHA", "Stopped Farming:\\r\\nShitter you got a Maptcha, you should probably solve it!", 15158332, true);
/* 331 */                 if (this.alertState == AlertState.CHILLING)
/* 332 */                   this.alertState = AlertState.TURNUP; 
/* 333 */                 this.farmingState = FarmingState.STOP_FARMING;
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 338 */               KeyBinding.func_74510_a(this.mc.field_71474_y.field_74368_y.func_151463_i(), false);
/*     */               
/* 340 */               if (++this.stuckTicks >= 100) {
/* 341 */                 this.curPos = this.mc.field_71439_g.func_180425_c();
/* 342 */                 if (this.oldPos != null && Math.sqrt(this.curPos.func_177951_i((Vec3i)this.oldPos)) <= 2.0D && !this.invFull) {
/* 343 */                   ChatUtils.send("Detected Player being stuck, trying to unstuck...", new String[0]);
/* 344 */                   sendWebhook("I AM STUCK", "Oh no - I'm stuck, Step Bro come help me! >_<", 15158332, true);
/* 345 */                   KeyBinding.func_74510_a(this.mc.field_71474_y.field_74351_w.func_151463_i(), false);
/* 346 */                   KeyBinding.func_74510_a(this.mc.field_71474_y.field_74368_y.func_151463_i(), true);
/* 347 */                   if (this.alertState == AlertState.CHILLING)
/* 348 */                     this.alertState = AlertState.TURNUP; 
/*     */                 } 
/* 350 */                 this.oldPos = this.curPos;
/* 351 */                 this.stuckTicks = 0;
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 356 */               switch (this.sameInvState) {
/*     */                 case LEFT:
/* 358 */                   inv = InventoryUtils.getInventoryStacks();
/* 359 */                   if (inv.equals(this.oldInv)) {
/* 360 */                     if (++this.oldInvCount >= 240) {
/* 361 */                       if (this.alertState == AlertState.CHILLING) {
/* 362 */                         this.alertState = AlertState.TURNUP;
/*     */                       }
/* 364 */                       this.sameInvState = SameInvState.UNPRESS;
/* 365 */                       this.oldInvCount = 0;
/*     */                     }  break;
/*     */                   } 
/* 368 */                   this.oldInv = InventoryUtils.getInventoryStacks();
/* 369 */                   this.oldInvCount = 0;
/*     */                   break;
/*     */ 
/*     */                 
/*     */                 case RIGHT:
/* 374 */                   this.mc.field_71439_g.func_71165_d("/setspawn");
/* 375 */                   this.islandReboot = true;
/* 376 */                   this.isRebootState = IsRebootState.ISLAND;
/* 377 */                   ChatUtils.send("Detected De-sync, re-syncing...", new String[0]);
/* 378 */                   ChatUtils.send("Escaping in " + this.failSafeDelay.getCurrent() + "ms", new String[0]);
/* 379 */                   KeyBinding.func_74506_a();
/* 380 */                   this.recoverStr = "/warp hub";
/* 381 */                   this.recoverBool = true;
/* 382 */                   this.farmingState = FarmingState.RECOVER;
/* 383 */                   this.sameInvState = SameInvState.CHILLING;
/* 384 */                   this.recoverTimer.reset();
/*     */                   break;
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 390 */               if (this.mc.field_71439_g.field_71071_by.func_70447_i() == -1 && !this.invFull) {
/* 391 */                 sendWebhook("FULL INVENTORY", "Stopped Farming:\\r\\nInventory Full!", 15439360, false);
/* 392 */                 if (this.alertState == AlertState.CHILLING)
/* 393 */                   this.alertState = AlertState.TURNUP; 
/* 394 */                 this.invFull = true;
/* 395 */                 KeyBinding.func_74506_a(); break;
/* 396 */               }  if (this.mc.field_71439_g.field_71071_by.func_70447_i() != -1 && this.invFull) {
/* 397 */                 sendWebhook("INVENTORY NOT FULL", "Continued Farming:\\r\\nInventory not full anymore!", 3066993, false);
/* 398 */                 this.invFull = false;
/* 399 */                 pressKeys();
/*     */               } 
/*     */               break;
/*     */ 
/*     */             
/*     */             case null:
/* 405 */               ChatUtils.send("Player isn't in Island!", new String[0]);
/* 406 */               ChatUtils.send("Re-warping in " + this.failSafeDelay.getCurrent() + "ms", new String[0]);
/* 407 */               sendWebhook("NOT IN ISLAND", "Player isn't in Island!\\r\\nRecovering...", 15439360, false);
/* 408 */               this.recoverStr = "/warp home";
/* 409 */               this.recoverBool = true;
/* 410 */               this.farmingState = FarmingState.RECOVER;
/* 411 */               this.recoverTimer.reset();
/* 412 */               KeyBinding.func_74506_a();
/*     */               break;
/*     */             
/*     */             case null:
/* 416 */               ChatUtils.send("Player isn't in Skyblock!", new String[0]);
/* 417 */               ChatUtils.send("Joining Skyblock in " + this.failSafeDelay.getCurrent() + "ms", new String[0]);
/* 418 */               sendWebhook("NOT IN SKYBLOCK", "Player isn't in Island!\\r\\nRecovering...", 15439360, false);
/* 419 */               this.recoverStr = "/play skyblock";
/* 420 */               this.recoverBool = true;
/* 421 */               this.farmingState = FarmingState.RECOVER;
/* 422 */               this.recoverTimer.reset();
/* 423 */               KeyBinding.func_74506_a();
/*     */               break;
/*     */             
/*     */             case null:
/* 427 */               ChatUtils.send("Player is in Limbo!", new String[0]);
/* 428 */               ChatUtils.send("Escaping in " + this.failSafeDelay.getCurrent() + "ms", new String[0]);
/* 429 */               sendWebhook("PLAYER IN LIMBO", "Player isn't in Island!\\r\\nRecovering...", 15439360, false);
/* 430 */               this.recoverStr = "/l";
/* 431 */               this.recoverBool = true;
/* 432 */               this.farmingState = FarmingState.RECOVER;
/* 433 */               this.recoverTimer.reset();
/* 434 */               KeyBinding.func_74506_a();
/*     */               break;
/*     */           } 
/*     */         
/*     */         } 
/*     */         break;
/*     */       case null:
/* 441 */         if (this.islandReboot) {
/* 442 */           switch (this.isRebootState) {
/*     */             case LEFT:
/* 444 */               if (this.recoverTimer.hasReached(this.failSafeDelay.getCurrent().intValue())) {
/* 445 */                 this.mc.field_71439_g.func_71165_d(this.recoverStr);
/* 446 */                 this.recoverStr = "/warp home";
/* 447 */                 this.isRebootState = IsRebootState.HUB;
/* 448 */                 this.recoverTimer.reset();
/*     */               } 
/*     */               break;
/*     */             
/*     */             case RIGHT:
/* 453 */               if (this.recoverTimer.hasReached(15000L)) {
/* 454 */                 this.mc.field_71439_g.func_71165_d(this.recoverStr);
/* 455 */                 this.farmingState = FarmingState.START_FARMING;
/* 456 */                 this.oldInvCount = 0;
/* 457 */                 this.recoverTimer.reset();
/* 458 */                 this.islandReboot = false;
/*     */               }  break;
/*     */           }  break;
/*     */         } 
/* 462 */         if (this.recoverTimer.hasReached(this.failSafeDelay.getCurrent().intValue()) && SkyblockUtils.getLocation() != SkyblockUtils.Location.NONE) {
/* 463 */           this.mc.field_71439_g.func_71165_d(this.recoverStr);
/* 464 */           this.farmingState = FarmingState.START_FARMING;
/* 465 */           this.oldInvCount = 0;
/* 466 */           this.recoverTimer.reset();
/*     */         } 
/*     */         break;
/*     */       
/*     */       case null:
/* 471 */         if (this.autoTab.isEnabled()) {
/* 472 */           ChadUtils.regrabMouse();
/*     */         }
/*     */         
/* 475 */         if (this.cpuSaver.isEnabled()) {
/* 476 */           ChadUtils.revertCpuUsage();
/*     */         }
/*     */         
/* 479 */         KeyBinding.func_74506_a();
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 484 */     if (this.soundAlerts.isEnabled()) {
/* 485 */       switch (this.alertState) {
/*     */         case LEFT:
/* 487 */           for (SoundCategory category : SoundCategory.values()) {
/* 488 */             this.oldSounds.put(category, Float.valueOf(this.mc.field_71474_y.func_151438_a(category)));
/* 489 */             this.mc.field_71474_y.func_151439_a(category, 0.5F);
/*     */           } 
/* 491 */           this.alertState = AlertState.PLAY;
/* 492 */           this.alertTimer.reset();
/*     */           break;
/*     */         
/*     */         case RIGHT:
/* 496 */           if (this.alertTimer.hasReached(100L)) {
/* 497 */             this.mc.field_71439_g.func_85030_a("mob.enderdragon.growl", 1.0F, 1.0F);
/* 498 */             this.alertState = AlertState.TURNDOWN;
/* 499 */             this.alertTimer.reset();
/*     */           } 
/*     */           break;
/*     */         
/*     */         case null:
/* 504 */           if (this.alertTimer.hasReached(2500L)) {
/* 505 */             for (SoundCategory category : SoundCategory.values()) {
/* 506 */               this.mc.field_71474_y.func_151439_a(category, ((Float)this.oldSounds.get(category)).floatValue());
/*     */             }
/* 508 */             this.alertState = AlertState.CHILLING;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onRenderWorld(Render3DEvent event) {
/* 518 */     if (this.farmingState == FarmingState.SET_ANGLES) {
/* 519 */       if (System.currentTimeMillis() <= RotationUtils.endTime) {
/* 520 */         RotationUtils.update();
/*     */       } else {
/* 522 */         RotationUtils.update();
/* 523 */         this.farmingDirection = determineDirection();
/* 524 */         this.farmingState = FarmingState.PRESS_KEYS;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onChat(ClientChatReceivedEvent event) {
/* 531 */     if (event.type == 2)
/*     */       return; 
/* 533 */     if (this.farmingState == FarmingState.FARMING) {
/* 534 */       String msg = StringUtils.func_76338_a(event.message.func_150260_c());
/*     */       
/* 536 */       if (msg.startsWith("From") || msg.matches("\\[SkyBlock] .*? is visiting Your Island!.*") || msg.contains("has invited you to join their party!")) {
/* 537 */         this.alertState = AlertState.TURNUP;
/*     */         
/* 539 */         if (msg.startsWith("From")) {
/* 540 */           sendWebhook("MESSAGE", "Received Message:\\r\\n" + msg, 1689596, true);
/* 541 */         } else if (msg.matches("\\[SkyBlock] .*? is visiting Your Island!.*")) {
/* 542 */           Pattern pat = Pattern.compile("\\[SkyBlock] (.*?) is visiting Your Island!.*");
/* 543 */           Matcher mat = pat.matcher(msg);
/*     */           
/* 545 */           if (mat.matches()) {
/* 546 */             sendWebhook("GETTING VISITED", "Player is visiting you:\\r\\n" + mat.group(1), 1689596, true);
/*     */           }
/* 548 */         } else if (msg.contains("has invited you to join their party!")) {
/* 549 */           String[] split = msg.split("\n");
/*     */           
/* 551 */           if (split.length == 4) {
/* 552 */             String mm = split[1];
/* 553 */             Pattern pat = Pattern.compile("(.*?) has invited you to join their party!.*");
/* 554 */             Matcher mat = pat.matcher(mm);
/*     */             
/* 556 */             if (mat.matches()) {
/* 557 */               sendWebhook("PARTY REQUEST", "Player partied you:\\r\\n" + mat.group(1), 1689596, true);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 563 */       if (msg.startsWith("[Important] This server will restart soon:")) {
/* 564 */         this.alertState = AlertState.TURNUP;
/* 565 */         this.mc.field_71439_g.func_71165_d("/setspawn");
/* 566 */         this.islandReboot = true;
/* 567 */         this.isRebootState = IsRebootState.ISLAND;
/* 568 */         ChatUtils.send("Server is rebooting!", new String[0]);
/* 569 */         ChatUtils.send("Escaping in " + this.failSafeDelay.getCurrent() + "ms", new String[0]);
/* 570 */         this.recoverStr = "/warp hub";
/* 571 */         this.farmingState = FarmingState.RECOVER;
/* 572 */         this.recoverTimer.reset();
/* 573 */         KeyBinding.func_74506_a();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private FarmingDirection determineDirection() {
/* 579 */     int[] mod = getModifiers(this.direction.getCurrent());
/* 580 */     double ceilY = Math.ceil(this.mc.field_71439_g.field_70163_u);
/*     */     int i;
/* 582 */     for (i = 0; i < 160; i++) {
/* 583 */       BlockPos down, further = new BlockPos(this.mc.field_71439_g.field_70165_t + (mod[0] * (i + 1)), ceilY, this.mc.field_71439_g.field_70161_v + (mod[1] * (i + 1)));
/*     */ 
/*     */       
/* 586 */       if (this.farmType.getCurrent().equals("Vertical")) {
/* 587 */         down = new BlockPos(this.mc.field_71439_g.field_70165_t + (mod[0] * i), ceilY - 1.0D, this.mc.field_71439_g.field_70161_v + (mod[1] * i));
/*     */       } else {
/* 589 */         down = new BlockPos(this.mc.field_71439_g.field_70165_t + (mod[0] * i) - mod[1], ceilY, this.mc.field_71439_g.field_70161_v + (mod[1] * i) - mod[0]);
/*     */       } 
/*     */       
/* 592 */       IBlockState upBs = this.mc.field_71441_e.func_180495_p(further);
/* 593 */       IBlockState downBs = this.mc.field_71441_e.func_180495_p(down);
/*     */       
/* 595 */       if (downBs != null && (downBs.func_177230_c() == Blocks.field_150350_a || downBs.func_177230_c() == Blocks.field_150444_as) && 
/* 596 */         upBs != null && upBs.func_177230_c() != Blocks.field_150350_a && upBs.func_177230_c() != Blocks.field_150355_j && upBs.func_177230_c() != Blocks.field_150444_as) {
/* 597 */         return FarmingDirection.LEFT;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 602 */     for (i = 0; i < 160; i++) {
/* 603 */       BlockPos down, further = new BlockPos(this.mc.field_71439_g.field_70165_t - (mod[0] * (i + 1)), ceilY, this.mc.field_71439_g.field_70161_v - (mod[1] * (i + 1)));
/*     */ 
/*     */       
/* 606 */       if (this.farmType.getCurrent().equals("Vertical")) {
/* 607 */         down = new BlockPos(this.mc.field_71439_g.field_70165_t - (mod[0] * i), ceilY - 1.0D, this.mc.field_71439_g.field_70161_v - (mod[1] * i));
/*     */       } else {
/* 609 */         down = new BlockPos(this.mc.field_71439_g.field_70165_t - (mod[0] * i) - mod[1], ceilY, this.mc.field_71439_g.field_70161_v - (mod[1] * i) - mod[0]);
/*     */       } 
/*     */       
/* 612 */       IBlockState upBs = this.mc.field_71441_e.func_180495_p(further);
/* 613 */       IBlockState downBs = this.mc.field_71441_e.func_180495_p(down);
/*     */       
/* 615 */       if (downBs != null && downBs.func_177230_c() == Blocks.field_150350_a && 
/* 616 */         upBs != null && upBs.func_177230_c() != Blocks.field_150350_a) {
/* 617 */         return FarmingDirection.RIGHT;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 622 */     return FarmingDirection.LEFT;
/*     */   }
/*     */   
/*     */   private int[] getModifiers(String direction) {
/* 626 */     int[] ret = new int[2];
/*     */     
/* 628 */     switch (direction) {
/*     */       case "SOUTH":
/* 630 */         ret[0] = 1;
/*     */         break;
/*     */       
/*     */       case "WEST":
/* 634 */         ret[1] = 1;
/*     */         break;
/*     */       
/*     */       case "NORTH":
/* 638 */         ret[0] = -1;
/*     */         break;
/*     */       
/*     */       case "EAST":
/* 642 */         ret[1] = -1;
/*     */         break;
/*     */     } 
/*     */     
/* 646 */     return ret;
/*     */   }
/*     */   
/*     */   private void pressKeys() {
/* 650 */     this.mc.field_71415_G = true;
/* 651 */     KeyBinding.func_74510_a(this.mc.field_71474_y.field_74312_F.func_151463_i(), true);
/*     */     
/* 653 */     switch (this.farmingDirection) {
/*     */       case LEFT:
/* 655 */         KeyBinding.func_74510_a(this.mc.field_71474_y.field_74366_z.func_151463_i(), false);
/* 656 */         KeyBinding.func_74510_a(this.mc.field_71474_y.field_74370_x.func_151463_i(), true);
/*     */         break;
/*     */       
/*     */       case RIGHT:
/* 660 */         KeyBinding.func_74510_a(this.mc.field_71474_y.field_74370_x.func_151463_i(), false);
/* 661 */         KeyBinding.func_74510_a(this.mc.field_71474_y.field_74366_z.func_151463_i(), true);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendWebhook(String title, String updateReason, int color, boolean ping) {
/* 667 */     if (this.webhookUpdates.isEnabled())
/*     */       try {
/* 669 */         HttpURLConnection con = (HttpURLConnection)(new URL(this.webhookUrl.getCurrent())).openConnection();
/* 670 */         con.setDoOutput(true);
/* 671 */         con.setRequestMethod("POST");
/* 672 */         con.setRequestProperty("Content-Type", "application/json");
/* 673 */         con.setRequestProperty("User-Agent", "Mozilla/5.0");
/*     */         
/* 675 */         Session session = this.mc.func_110432_I();
/* 676 */         String json = "{ \"content\": " + (ping ? "\"@everyone\"" : "null") + ", \"embeds\": [ { \"title\": \"" + title + "\", \"description\": \"**Account Info**\\nIGN: " + session.func_111285_a() + "\", \"color\": " + color + ", \"fields\": [ { \"name\": \"Update Reason:\", \"value\": \"" + updateReason + "\" } ], \"footer\": { \"text\": \"Made by Apfelsaft#0002\", \"icon_url\": \"https://visage.surgeplay.com/face/128/7c224caeaea249a49783053b9bcf4ed1\" } } ], \"username\": \"" + session.func_111285_a() + "\", \"avatar_url\": \"https://visage.surgeplay.com/face/128/" + session.func_148255_b() + "\" }";
/*     */         
/* 678 */         try (OutputStream output = con.getOutputStream()) {
/* 679 */           output.write(json.getBytes(StandardCharsets.UTF_8));
/*     */         } 
/*     */         
/* 682 */         int resp = con.getResponseCode();
/*     */         
/* 684 */         if (resp == 200 || resp == 204) {
/* 685 */           ChatUtils.send("Webhook sent successfully", new String[0]);
/*     */         } else {
/* 687 */           ChatUtils.send("Error while sending Webhook", new String[0]);
/*     */         } 
/* 689 */       } catch (IOException e) {
/* 690 */         e.printStackTrace();
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\world\AutoFarm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */