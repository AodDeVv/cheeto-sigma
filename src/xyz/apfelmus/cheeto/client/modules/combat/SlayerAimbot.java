/*     */ package xyz.apfelmus.cheeto.client.modules.combat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.monster.EntityBlaze;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntitySpider;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.Vec3;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientChatReceivedEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.modules.misc.ArmorSwap;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.KeybindUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.RandomUtil;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.TimeHelper;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.VecUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ @Module(name = "SlayerAimbot", category = Category.COMBAT)
/*     */ public class SlayerAimbot {
/*     */   @Setting(name = "AimSpeed", description = "Speed of rotations")
/*  48 */   private IntegerSetting aimSpeed = new IntegerSetting(
/*  49 */       Integer.valueOf(45), Integer.valueOf(5), Integer.valueOf(100)); @Setting(name = "Range", description = "Range duhhh")
/*  50 */   private IntegerSetting range = new IntegerSetting(
/*  51 */       Integer.valueOf(4), Integer.valueOf(3), Integer.valueOf(7)); @Setting(name = "Blatant", description = "Always face the target")
/*  52 */   private BooleanSetting blatant = new BooleanSetting(false);
/*     */   @Setting(name = "Minibosses", description = "Aim at minibosses")
/*  54 */   private BooleanSetting miniBosses = new BooleanSetting(true);
/*     */   @Setting(name = "DisableForTP", description = "Disable when holding AOTE/AOTV")
/*  56 */   private BooleanSetting disable = new BooleanSetting(false);
/*     */   @Setting(name = "AutoClick", description = "Automatically click")
/*  58 */   private BooleanSetting autoClick = new BooleanSetting(false);
/*     */   @Setting(name = "MinCPS", description = "Minimum CPS")
/*  60 */   private IntegerSetting minCps = new IntegerSetting(
/*  61 */       Integer.valueOf(7), Integer.valueOf(0), Integer.valueOf(20)); @Setting(name = "MaxCPS", description = "Maximum CPS")
/*  62 */   private IntegerSetting maxCps = new IntegerSetting(
/*  63 */       Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(20)); @Setting(name = "TradeMode", description = "Won't attack your own boss")
/*  64 */   private BooleanSetting tradeMode = new BooleanSetting(false);
/*     */   @Setting(name = "MagicFindSet", description = "TradeMode MagicFind Set")
/*  66 */   private IntegerSetting magicFindSet = new IntegerSetting(
/*  67 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(9)); @Setting(name = "DamageSet", description = "TradeMode Damage Set")
/*  68 */   private IntegerSetting damageSet = new IntegerSetting(
/*  69 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(9)); @Setting(name = "MainSlot", description = "Slot to hit with normally")
/*  70 */   private IntegerSetting mainSlot = new IntegerSetting(
/*  71 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(9)); @Setting(name = "DaeAxeSlot", description = "Will hold DaeAxe in TradeMode")
/*  72 */   private IntegerSetting daeAxeSlot = new IntegerSetting(
/*  73 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(9)); @Setting(name = "AntiAFK", description = "Will do small movements")
/*  74 */   private BooleanSetting antiAfk = new BooleanSetting(true);
/*     */   @Setting(name = "RevMode", description = "Will spin around the Rev (stupid)")
/*  76 */   private BooleanSetting revMode = new BooleanSetting(false);
/*     */   @Setting(name = "DetectionMode", description = "Ways of detecting a spawned Slayer Boss")
/*  78 */   private ModeSetting detectionMode = new ModeSetting("AutoPet", 
/*  79 */       Arrays.asList(new String[] { "AutoPet", "Scoreboard", "Chat" })); @Setting(name = "GloomSilentUse", description = "WIP")
/*  80 */   private IntegerSetting gloomSilentUse = new IntegerSetting(
/*  81 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(4));
/*     */ 
/*     */ 
/*     */   
/*  85 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   private static EntityArmorStand slayerStand;
/*     */   private static Entity slayerMob;
/*     */   private static boolean isMini;
/*     */   private static boolean shouldAttack;
/*  91 */   private static TimeHelper clickTimer = new TimeHelper();
/*  92 */   private static TimeHelper gloomTimer = new TimeHelper();
/*  93 */   private static long clickWaitTime = 0L;
/*     */   
/*     */   private static Vec3 startPos;
/*  96 */   private static int ticks = 0;
/*  97 */   private static int currentHp = -1;
/*  98 */   private static int currentHpMax = -1;
/*  99 */   private static int currentOverflow = -1;
/*     */   
/* 101 */   private static final Pattern HEALTH_PATTERN_S = Pattern.compile("(?<health>[0-9]+)/(?<maxHealth>[0-9]+)❤(?<wand>\\+(?<wandHeal>[0-9]+)[▆▅▄▃▂▁])?");
/* 102 */   private static final Pattern MANA_PATTERN_S = Pattern.compile("(?<num>[0-9,]+)/(?<den>[0-9,]+)✎(| Mana| (?<overflow>-?[0-9,]+)ʬ)");
/* 103 */   private static final Pattern AUTOPET_PATTERN_S = Pattern.compile(".*?Autopet equipped your \\[Lvl \\d+] (.+?)( )?! .*?");
/*     */   
/*     */   private enum SlayerBoss {
/* 106 */     SELF,
/* 107 */     TRADER;
/*     */   }
/*     */   
/* 110 */   private static SlayerBoss slayerBoss = SlayerBoss.SELF;
/*     */   
/* 112 */   private static final Map<String, Class<?>> slayerMap = new HashMap<String, Class<?>>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   private static final Map<String, Class<?>> miniMap = new HashMap<String, Class<?>>()
/*     */     {
/*     */     
/*     */     };
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
/*     */   @Enable
/*     */   public void onEnable() {
/* 140 */     slayerMob = null;
/* 141 */     slayerStand = null;
/* 142 */     isMini = false;
/* 143 */     startPos = mc.field_71439_g.func_174791_d();
/* 144 */     slayerBoss = SlayerBoss.SELF;
/* 145 */     currentOverflow = -1;
/*     */     
/* 147 */     if (this.autoClick.isEnabled() && 
/* 148 */       this.minCps.getCurrent().intValue() > this.maxCps.getCurrent().intValue()) {
/* 149 */       ChatUtils.send("MinCPS can't be higher than MaxCPS retard", new String[0]);
/* 150 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/* 157 */     KeybindUtils.stopMovement();
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/* 162 */     if (slayerMob != null && this.tradeMode.isEnabled()) {
/* 163 */       if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*     */         return; 
/* 165 */       if (this.revMode.isEnabled()) {
/* 166 */         KeyBinding.func_74510_a(mc.field_71474_y.field_74351_w.func_151463_i(), (slayerMob.func_70032_d((Entity)mc.field_71439_g) > 3.0F));
/* 167 */         KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), true);
/*     */       } else {
/* 169 */         KeybindUtils.stopMovement();
/*     */         
/* 171 */         if (this.antiAfk.isEnabled() && ++ticks > 40) {
/* 172 */           ticks = 0;
/* 173 */           Rotation afk = new Rotation(mc.field_71439_g.field_70177_z, mc.field_71439_g.field_70125_A);
/* 174 */           afk.addYaw((float)(Math.random() * 4.0D - 2.0D));
/* 175 */           RotationUtils.setup(afk, Long.valueOf(RandomUtil.randBetween(400, 600)));
/* 176 */           List<KeyBinding> neededPresses = VecUtils.getNeededKeyPresses(mc.field_71439_g.func_174791_d(), startPos);
/* 177 */           neededPresses.forEach(v -> KeyBinding.func_74510_a(v.func_151463_i(), true));
/*     */         } 
/*     */       } 
/* 180 */     } else if (this.tradeMode.isEnabled()) {
/* 181 */       KeybindUtils.stopMovement();
/*     */     } 
/*     */     
/* 184 */     if (slayerMob == null) {
/* 185 */       Map<Entity, EntityArmorStand> allPossibleSlayers = new HashMap<>();
/* 186 */       Map<Entity, EntityArmorStand> allPossibleMinis = new HashMap<>();
/*     */       
/* 188 */       int rongo = this.range.getCurrent().intValue();
/* 189 */       for (Iterator<Entity> iterator = mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72314_b(rongo, rongo, rongo), null).iterator(); iterator.hasNext(); ) { Entity e = iterator.next();
/* 190 */         if (e instanceof EntityArmorStand) {
/* 191 */           slayerMap.forEach((k, v) -> {
/*     */                 if (e.func_95999_t().contains(k) && e.func_70032_d((Entity)mc.field_71439_g) <= this.range.getCurrent().intValue()) {
/*     */                   Entity slay = SkyblockUtils.getEntityCuttingOtherEntity(e, v);
/*     */                   
/*     */                   if (slay != null) {
/*     */                     allPossibleSlayers.put(slay, (EntityArmorStand)e);
/*     */                   }
/*     */                 } 
/*     */               });
/* 200 */           if (this.miniBosses.isEnabled()) {
/* 201 */             miniMap.forEach((k, v) -> {
/*     */                   if (e.func_95999_t().contains(k) && e.func_70032_d((Entity)mc.field_71439_g) <= this.range.getCurrent().intValue()) {
/*     */                     Entity mini = SkyblockUtils.getEntityCuttingOtherEntity(e, v);
/*     */                     
/*     */                     if (mini != null) {
/*     */                       allPossibleMinis.put(mini, (EntityArmorStand)e);
/*     */                     }
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         }  }
/*     */       
/* 213 */       if (!allPossibleSlayers.isEmpty()) {
/* 214 */         slayerMob = Collections.<Entity>min(allPossibleSlayers.keySet(), Comparator.comparing(e2 -> Float.valueOf(e2.func_70032_d((Entity)mc.field_71439_g))));
/* 215 */         slayerStand = allPossibleSlayers.get(slayerMob);
/* 216 */       } else if (!allPossibleMinis.isEmpty()) {
/* 217 */         slayerMob = Collections.<Entity>min(allPossibleMinis.keySet(), Comparator.comparing(e2 -> Float.valueOf(e2.func_70032_d((Entity)mc.field_71439_g))));
/* 218 */         slayerStand = allPossibleMinis.get(slayerMob);
/* 219 */         isMini = true;
/*     */       }
/*     */     
/* 222 */     } else if (this.miniBosses.isEnabled() && isMini) {
/* 223 */       Map<Entity, EntityArmorStand> allPossibleSlayers = new HashMap<>();
/*     */       
/* 225 */       int rongo = this.range.getCurrent().intValue();
/* 226 */       for (Iterator<Entity> iterator = mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72314_b(rongo, rongo, rongo), null).iterator(); iterator.hasNext(); ) { Entity e = iterator.next();
/* 227 */         if (e instanceof EntityArmorStand) {
/* 228 */           slayerMap.forEach((k, v) -> {
/*     */                 if (e.func_95999_t().contains(k) && e.func_70032_d((Entity)mc.field_71439_g) <= this.range.getCurrent().intValue()) {
/*     */                   Entity slay = SkyblockUtils.getEntityCuttingOtherEntity(e, v);
/*     */                   
/*     */                   if (slay != null) {
/*     */                     allPossibleSlayers.put(slay, (EntityArmorStand)e);
/*     */                   }
/*     */                 } 
/*     */               });
/*     */         } }
/*     */       
/* 239 */       if (!allPossibleSlayers.isEmpty()) {
/* 240 */         slayerMob = Collections.<Entity>min(allPossibleSlayers.keySet(), Comparator.comparing(e2 -> Float.valueOf(e2.func_70032_d((Entity)mc.field_71439_g))));
/* 241 */         slayerStand = allPossibleSlayers.get(slayerMob);
/* 242 */         isMini = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onRender(Render3DEvent event) {
/* 250 */     if (this.disable.isEnabled()) {
/* 251 */       ItemStack currentHeld = mc.field_71439_g.func_70694_bm();
/* 252 */       if (currentHeld == null || currentHeld.func_82833_r().contains("Aspect of the "))
/*     */         return; 
/* 254 */     }  if (slayerMob != null) {
/* 255 */       int mobHp = SkyblockUtils.getSlayerHp(slayerStand);
/*     */       
/* 257 */       if (slayerMob.func_70032_d((Entity)mc.field_71439_g) > 5.0F || slayerMob.field_70128_L || mobHp == 0) {
/* 258 */         slayerMob = null;
/* 259 */         slayerStand = null;
/*     */         
/*     */         return;
/*     */       } 
/* 263 */       if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*     */         return; 
/* 265 */       if (!this.tradeMode.isEnabled()) shouldAttack = true; 
/* 266 */       if (this.detectionMode.getCurrent().equals("Scoreboard") && 
/* 267 */         this.tradeMode.isEnabled()) {
/* 268 */         ArmorSwap swap = (ArmorSwap)CF4M.INSTANCE.moduleManager.getModule("ArmorSwap");
/*     */         
/* 270 */         if (SkyblockUtils.slayerBossSpawned()) {
/* 271 */           shouldAttack = false;
/*     */           
/* 273 */           if (slayerBoss == SlayerBoss.TRADER) {
/* 274 */             if (this.magicFindSet.getCurrent().intValue() > 0) {
/* 275 */               swap.armorSlot.setCurrent(this.magicFindSet.getCurrent());
/* 276 */               CF4M.INSTANCE.moduleManager.toggle(swap);
/*     */             } 
/*     */             
/* 279 */             if (this.daeAxeSlot.getCurrent().intValue() > 0) {
/* 280 */               mc.field_71439_g.field_71071_by.field_70461_c = this.daeAxeSlot.getCurrent().intValue() - 1;
/*     */             }
/*     */             
/* 283 */             slayerBoss = SlayerBoss.SELF;
/*     */           }
/*     */         
/* 286 */         } else if (slayerBoss == SlayerBoss.SELF) {
/* 287 */           if (this.damageSet.getCurrent().intValue() > 0) {
/* 288 */             swap.armorSlot.setCurrent(this.damageSet.getCurrent());
/* 289 */             CF4M.INSTANCE.moduleManager.toggle(swap);
/*     */           } 
/*     */           
/* 292 */           if (this.mainSlot.getCurrent().intValue() > 0) {
/* 293 */             mc.field_71439_g.field_71071_by.field_70461_c = this.mainSlot.getCurrent().intValue() - 1;
/*     */           }
/*     */           
/* 296 */           slayerBoss = SlayerBoss.TRADER;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 302 */       if (shouldAttack || this.revMode.isEnabled()) {
/* 303 */         if (this.blatant.isEnabled() || mc.field_71476_x == null || mc.field_71476_x.field_72313_a != MovingObjectPosition.MovingObjectType.ENTITY || !mc.field_71476_x.field_72308_g.equals(slayerMob)) {
/* 304 */           if (!RotationUtils.done) {
/* 305 */             RotationUtils.update();
/*     */           }
/* 307 */           if (this.revMode.isEnabled()) {
/* 308 */             Vec3 aim = slayerMob.func_174791_d().func_72441_c(0.0D, 0.1D, 0.0D);
/*     */             
/* 310 */             RotationUtils.setup(RotationUtils.getRotation(aim), Long.valueOf((100 - this.aimSpeed.getCurrent().intValue()) * 10L));
/*     */           } else {
/* 312 */             double n = RotationUtils.fovFromEntity(slayerMob);
/* 313 */             double complimentSpeed = n * ThreadLocalRandom.current().nextDouble(-1.47328D, 2.48293D) / 100.0D;
/* 314 */             float val = (float)-(complimentSpeed + n / (101.0D - (float)ThreadLocalRandom.current().nextDouble(this.aimSpeed.getCurrent().intValue() - 4.723847D, this.aimSpeed.getCurrent().intValue())));
/* 315 */             mc.field_71439_g.field_70177_z += val;
/*     */           } 
/*     */         } 
/*     */         
/* 319 */         if (this.autoClick.isEnabled() && shouldAttack) {
/* 320 */           int curBossHp = SkyblockUtils.getSlayerHp(slayerStand);
/*     */           
/* 322 */           if (clickTimer.hasReached(clickWaitTime) && curBossHp > 0) {
/* 323 */             double randCps = RandomUtil.randBetween(this.minCps.getCurrent().intValue(), this.maxCps.getCurrent().intValue());
/* 324 */             clickWaitTime = (long)(1000.0D / randCps);
/*     */             
/* 326 */             KeybindUtils.leftClick();
/*     */             
/* 328 */             clickTimer.reset();
/*     */           }
/*     */         
/*     */         } 
/* 332 */       } else if (!RotationUtils.done) {
/* 333 */         RotationUtils.reset();
/*     */       }
/*     */     
/* 336 */     } else if (!RotationUtils.done) {
/* 337 */       RotationUtils.reset();
/*     */     } 
/*     */   }
/*     */   
/* 341 */   private static List<String> slayerPets = new ArrayList<>(Arrays.asList(new String[] { "Wolf", "Tarantula", "Golden Dragon" }));
/* 342 */   private static List<String> magicFindPets = new ArrayList<>(Arrays.asList(new String[] { "Megalodon", "Griffin", "Black Cat" }));
/*     */   
/*     */   @Event
/*     */   public void onChat(ClientChatReceivedEvent event) {
/* 346 */     if (event.type == 2) {
/* 347 */       String unformatted = event.message.func_150260_c();
/* 348 */       for (String section : unformatted.split(" {3,}")) {
/* 349 */         if (section.contains("❤")) {
/* 350 */           Matcher m = HEALTH_PATTERN_S.matcher(StringUtils.func_76338_a(section));
/*     */           
/* 352 */           if (m.matches()) {
/* 353 */             currentHp = Integer.parseInt(m.group("health"));
/* 354 */             currentHpMax = Integer.parseInt(m.group("maxHealth"));
/*     */           } 
/* 356 */         } else if (section.contains("✎")) {
/* 357 */           Matcher m = MANA_PATTERN_S.matcher(StringUtils.func_76338_a(section));
/*     */           
/* 359 */           if (m.matches()) {
/* 360 */             int overflow = -1;
/* 361 */             if (m.group("overflow") != null) {
/* 362 */               overflow = Integer.parseInt(m.group("overflow").replaceAll(",", ""));
/*     */             }
/*     */             
/* 365 */             currentOverflow = overflow;
/*     */           } 
/*     */         } 
/*     */       } 
/* 369 */     } else if (this.tradeMode.isEnabled() && this.detectionMode.getCurrent().equals("AutoPet")) {
/* 370 */       String msg = SkyblockUtils.stripString(event.message.func_150260_c());
/* 371 */       Pattern pat = AUTOPET_PATTERN_S;
/* 372 */       Matcher mat = pat.matcher(msg);
/*     */       
/* 374 */       if (mat.matches()) {
/* 375 */         String pet = mat.group(1);
/* 376 */         ArmorSwap swap = (ArmorSwap)CF4M.INSTANCE.moduleManager.getModule("ArmorSwap");
/*     */         
/* 378 */         if (slayerPets.contains(pet)) {
/* 379 */           shouldAttack = true;
/* 380 */           if (this.damageSet.getCurrent().intValue() > 0) {
/* 381 */             swap.armorSlot.setCurrent(this.damageSet.getCurrent());
/* 382 */             CF4M.INSTANCE.moduleManager.toggle(swap);
/*     */           } 
/*     */           
/* 385 */           if (this.mainSlot.getCurrent().intValue() > 0) {
/* 386 */             mc.field_71439_g.field_71071_by.field_70461_c = this.mainSlot.getCurrent().intValue() - 1;
/*     */           }
/* 388 */         } else if (magicFindPets.contains(pet)) {
/* 389 */           shouldAttack = false;
/* 390 */           if (this.magicFindSet.getCurrent().intValue() > 0) {
/* 391 */             swap.armorSlot.setCurrent(this.magicFindSet.getCurrent());
/* 392 */             CF4M.INSTANCE.moduleManager.toggle(swap);
/*     */           } 
/*     */           
/* 395 */           if (this.daeAxeSlot.getCurrent().intValue() > 0) {
/* 396 */             mc.field_71439_g.field_71071_by.field_70461_c = this.daeAxeSlot.getCurrent().intValue() - 1;
/*     */           }
/*     */           
/* 399 */           if (this.gloomSilentUse.getCurrent().intValue() != 0) {
/* 400 */             RotationUtils.setup(new Rotation(mc.field_71439_g.field_70177_z, 50.0F), Long.valueOf(250L));
/*     */           }
/*     */         } 
/*     */       } 
/* 404 */     } else if (this.tradeMode.isEnabled() && this.detectionMode.getCurrent().equals("Chat")) {
/* 405 */       ArmorSwap swap = (ArmorSwap)CF4M.INSTANCE.moduleManager.getModule("ArmorSwap");
/* 406 */       String msg = SkyblockUtils.stripString(event.message.func_150260_c());
/* 407 */       if (msg.equals("  SLAYER QUEST COMPLETE!")) {
/* 408 */         shouldAttack = true;
/* 409 */         if (this.damageSet.getCurrent().intValue() > 0) {
/* 410 */           swap.armorSlot.setCurrent(this.damageSet.getCurrent());
/* 411 */           CF4M.INSTANCE.moduleManager.toggle(swap);
/*     */         } 
/*     */         
/* 414 */         if (this.mainSlot.getCurrent().intValue() > 0) {
/* 415 */           mc.field_71439_g.field_71071_by.field_70461_c = this.mainSlot.getCurrent().intValue() - 1;
/*     */         }
/* 417 */       } else if (msg.startsWith("LOOT SHARE")) {
/* 418 */         shouldAttack = false;
/* 419 */         if (this.magicFindSet.getCurrent().intValue() > 0) {
/* 420 */           swap.armorSlot.setCurrent(this.magicFindSet.getCurrent());
/* 421 */           CF4M.INSTANCE.moduleManager.toggle(swap);
/*     */         } 
/*     */         
/* 424 */         if (this.daeAxeSlot.getCurrent().intValue() > 0) {
/* 425 */           mc.field_71439_g.field_71071_by.field_70461_c = this.daeAxeSlot.getCurrent().intValue() - 1;
/*     */         }
/*     */         
/* 428 */         if (this.gloomSilentUse.getCurrent().intValue() != 0) {
/* 429 */           RotationUtils.setup(new Rotation(mc.field_71439_g.field_70177_z, 50.0F), Long.valueOf(250L));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onWorldUnload(WorldUnloadEvent event) {
/* 437 */     slayerMob = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\SlayerAimbot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */