/*     */ package xyz.apfelmus.cheeto.client.modules.player;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.ModContainer;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientChatReceivedEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChadUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.CheetoStatus;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.JsonUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.KeybindUtils;
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
/*     */ @Module(name = "CommMacro", category = Category.PLAYER)
/*     */ public class CommMacro {
/*     */   @Setting(name = "LookTime", description = "Set higher if low mana or bad ping")
/*  51 */   private IntegerSetting lookTime = new IntegerSetting(
/*  52 */       Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(2500)); @Setting(name = "WarpTime", description = "Set higher if low mana or bad ping")
/*  53 */   private IntegerSetting warpTime = new IntegerSetting(
/*  54 */       Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "MaxPlayerRange", description = "Range of another player at which you will be warped out")
/*  55 */   private IntegerSetting maxPlayerRange = new IntegerSetting(
/*  56 */       Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(10)); @Setting(name = "PickSlot", description = "Slot of your Pickaxe/Drill")
/*  57 */   private IntegerSetting pickSlot = new IntegerSetting(
/*  58 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "AotvSlot", description = "Slot of your AOTV")
/*  59 */   private IntegerSetting aotvSlot = new IntegerSetting(
/*  60 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "PigeonSlot", description = "Slot of your Pigeon")
/*  61 */   private IntegerSetting pigeonSlot = new IntegerSetting(
/*  62 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "PigeonLess", description = "Poor fuck kys")
/*  63 */   private BooleanSetting pigeonLess = new BooleanSetting(false);
/*     */   @Setting(name = "Ungrab", description = "Automatically tabs out")
/*  65 */   private BooleanSetting ungrab = new BooleanSetting(true);
/*     */ 
/*     */   
/*  68 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*  70 */   public static MiningJson miningJson = JsonUtils.getMiningJson();
/*  71 */   private static Quest currentQuest = null;
/*  72 */   private static Location currentLocation = null;
/*  73 */   private static List<PathPoint> path = null;
/*  74 */   private static TimeHelper pigeonTimer = new TimeHelper();
/*  75 */   private static TimeHelper warpTimer = new TimeHelper();
/*  76 */   private static TimeHelper recoverTimer = new TimeHelper();
/*  77 */   private static TimeHelper boostTimer = new TimeHelper();
/*  78 */   private static TimeHelper refuelTimer = new TimeHelper();
/*     */   
/*  80 */   private static BlockPos oldPos = null;
/*     */   
/*     */   public static boolean hugeTits = false;
/*     */   public static boolean mithril = false;
/*  84 */   private static int oldDrillSlot = -1;
/*     */   
/*  86 */   private static final List<Vec3> emisPath = new ArrayList<>(Arrays.asList(new Vec3[] { new Vec3(11.5D, 149.0D, -64.5D), new Vec3(9.5D, 149.0D, -40.5D), new Vec3(9.5D, 145.0D, -17.5D), new Vec3(43.5D, 134.5D, 20.5D) }));
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Vec3> pathIter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean rospa9Da323XO92T() {
/*  96 */     if (!CheetoStatus.yep) return false; 
/*  97 */     ModContainer mod = (ModContainer)Loader.instance().getIndexedModList().get("ChromaHUD");
/*  98 */     if (mod != null) {
/*  99 */       File f = mod.getSource();
/* 100 */       if (f != null) {
/*     */         try {
/* 102 */           return DigestUtils.sha1Hex(Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0])).equals(CheetoStatus.yepCock);
/* 103 */         } catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 108 */     return false;
/*     */   }
/*     */   
/*     */   private enum CommState {
/* 112 */     PIGEONLESS_NAV,
/* 113 */     VROOM,
/* 114 */     CLICK_PIGEON,
/* 115 */     IN_PIGEON,
/* 116 */     WARP_FORGE,
/* 117 */     NAVIGATE,
/* 118 */     COMMIT,
/* 119 */     COMMITTING;
/*     */   }
/*     */   
/*     */   private enum WarpState {
/* 123 */     SETUP,
/* 124 */     LOOK,
/* 125 */     WARP;
/*     */   }
/*     */   
/*     */   private enum RefuelState {
/* 129 */     CLICK_MERCHANT,
/* 130 */     CLICK_DRILL_IN,
/* 131 */     CLICK_FUEL_IN,
/* 132 */     REFUEL_DRILL,
/* 133 */     CLICK_DRILL_OUT,
/* 134 */     CLICK_BACK_IN_INV,
/* 135 */     DONE_REFUELING;
/*     */   }
/*     */   
/* 138 */   private static CommState commState = CommState.CLICK_PIGEON;
/* 139 */   private static WarpState warpState = WarpState.SETUP;
/* 140 */   private static RefuelState refuelState = RefuelState.CLICK_MERCHANT;
/*     */   
/*     */   private enum Quest {
/* 143 */     MITHRIL_MINER("Mithril Miner"),
/* 144 */     TITANIUM_MINER("Titanium Miner"),
/* 145 */     UPPER_MINES_MITHRIL("Upper Mines Mithril"),
/* 146 */     ROYAL_MINES_MITHRIL("Royal Mines Mithril"),
/* 147 */     LAVA_SPRINGS_MITHRIL("Lava Springs Mithril"),
/* 148 */     CLIFFSIDE_VEINS_MITHRIL("Cliffside Veins Mithril"),
/* 149 */     RAMPARTS_QUARRY_MITHRIL("Rampart's Quarry Mithril"),
/* 150 */     UPPER_MINES_TITANIUM("Upper Mines Titanium"),
/* 151 */     ROYAL_MINES_TITANIUM("Royal Mines Titanium"),
/* 152 */     LAVA_SPRINGS_TITANIUM("Lava Springs Titanium"),
/* 153 */     CLIFFSIDE_VEINS_TITANIUM("Cliffside Veins Titanium"),
/* 154 */     RAMPARTS_QUARRY_TITANIUM("Rampart's Quarry Titanium"),
/* 155 */     GOBLIN_SLAYER("Goblin Slayer"),
/* 156 */     ICE_WALKER_SLAYER("Ice Walker Slayer");
/*     */     
/*     */     String questName;
/*     */     
/*     */     Quest(String questName) {
/* 161 */       this.questName = questName;
/*     */     }
/*     */   }
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/* 167 */     currentQuest = null;
/* 168 */     currentLocation = null;
/* 169 */     pigeonTimer.reset();
/* 170 */     warpTimer.reset();
/* 171 */     recoverTimer.reset();
/* 172 */     boostTimer.reset();
/* 173 */     commState = this.pigeonLess.isEnabled() ? CommState.PIGEONLESS_NAV : CommState.CLICK_PIGEON;
/* 174 */     warpState = WarpState.SETUP;
/* 175 */     refuelState = RefuelState.CLICK_MERCHANT;
/*     */     
/* 177 */     if (miningJson == null) {
/* 178 */       ChatUtils.send("An error occured while getting Mining Locations, reloading...", new String[0]);
/* 179 */       miningJson = JsonUtils.getMiningJson();
/* 180 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */       
/*     */       return;
/*     */     } 
/* 184 */     if (this.pickSlot.getCurrent().intValue() == 0 || this.aotvSlot.getCurrent().intValue() == 0 || (!this.pigeonLess.isEnabled() && this.pigeonSlot.getCurrent().intValue() == 0)) {
/* 185 */       ChatUtils.send("Configure your fucking Item Slots retard", new String[0]);
/* 186 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */     } 
/*     */     
/* 189 */     if (this.ungrab.isEnabled())
/* 190 */       ChadUtils.ungrabMouse(); 
/*     */   }
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/* 195 */     hugeTits = false;
/* 196 */     mithril = false;
/* 197 */     KeyBinding.func_74506_a();
/* 198 */     if (CF4M.INSTANCE.moduleManager.isEnabled("AutoMithril"))
/* 199 */       CF4M.INSTANCE.moduleManager.toggle("AutoMithril"); 
/* 200 */     if (CF4M.INSTANCE.moduleManager.isEnabled("IceGoblinSlayer"))
/* 201 */       CF4M.INSTANCE.moduleManager.toggle("IceGoblinSlayer"); 
/* 202 */     if (this.ungrab.isEnabled())
/* 203 */       ChadUtils.regrabMouse(); 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/* 208 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiChest && 
/* 209 */       currentLocation != null && currentLocation.name.equals("REFUEL") && "Drill Anvil".equals(InventoryUtils.getInventoryName())) {
/* 210 */       ItemStack aboveDrill; ItemStack hopper; switch (refuelState) {
/*     */         case MITHRIL_MINER:
/* 212 */           oldDrillSlot = InventoryUtils.getSlotForItem("Drill", Items.field_179562_cC);
/* 213 */           mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, oldDrillSlot, 0, 1, (EntityPlayer)mc.field_71439_g);
/* 214 */           refuelState = RefuelState.CLICK_FUEL_IN;
/*     */           break;
/*     */         
/*     */         case TITANIUM_MINER:
/* 218 */           aboveDrill = InventoryUtils.getStackInOpenContainerSlot(20);
/* 219 */           if (aboveDrill != null && aboveDrill.func_77952_i() == 5) {
/* 220 */             int fuelSlot = InventoryUtils.getSlotForItem("Volta", Items.field_151144_bL);
/* 221 */             if (fuelSlot == -1) {
/* 222 */               fuelSlot = InventoryUtils.getSlotForItem("Oil Barrel", Items.field_151144_bL);
/*     */             }
/* 224 */             if (fuelSlot == -1) {
/* 225 */               ChatUtils.send("Bozo you don't have any fuel", new String[0]);
/* 226 */               CF4M.INSTANCE.moduleManager.toggle(this);
/* 227 */               mc.field_71439_g.func_71053_j();
/*     */               break;
/*     */             } 
/* 230 */             mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, fuelSlot, 0, 1, (EntityPlayer)mc.field_71439_g);
/* 231 */             refuelState = RefuelState.REFUEL_DRILL;
/*     */           } 
/*     */           break;
/*     */         
/*     */         case null:
/* 236 */           hopper = InventoryUtils.getStackInOpenContainerSlot(22);
/* 237 */           if (hopper != null && hopper.func_77948_v()) {
/* 238 */             mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, 22, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 239 */             refuelTimer.reset();
/* 240 */             refuelState = RefuelState.CLICK_DRILL_OUT;
/*     */           } 
/*     */           break;
/*     */ 
/*     */         
/*     */         case null:
/* 246 */           if (refuelTimer.hasReached(500L)) {
/* 247 */             ItemStack oldDrill = InventoryUtils.getStackInOpenContainerSlot(29);
/*     */             
/* 249 */             if (oldDrill == null) {
/* 250 */               mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, 13, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 251 */               refuelTimer.reset();
/* 252 */               refuelState = RefuelState.CLICK_BACK_IN_INV;
/*     */             } 
/*     */           } 
/*     */           break;
/*     */         
/*     */         case null:
/* 258 */           if (refuelTimer.hasReached(500L)) {
/* 259 */             ItemStack barrier = InventoryUtils.getStackInOpenContainerSlot(13);
/*     */             
/* 261 */             if (barrier != null && barrier.func_77973_b() == Item.func_150898_a(Blocks.field_180401_cv)) {
/* 262 */               mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, oldDrillSlot, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 263 */               refuelTimer.reset();
/* 264 */               refuelState = RefuelState.DONE_REFUELING;
/*     */             } 
/*     */           } 
/*     */           break;
/*     */         
/*     */         case null:
/* 270 */           if (refuelTimer.hasReached(500L)) {
/* 271 */             mc.field_71439_g.func_71053_j();
/* 272 */             commState = this.pigeonLess.isEnabled() ? CommState.PIGEONLESS_NAV : CommState.CLICK_PIGEON;
/* 273 */             refuelState = RefuelState.CLICK_MERCHANT;
/* 274 */             recoverTimer.reset();
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 281 */     if (recoverTimer.hasReached(5000L)) {
/* 282 */       boolean warpOut; int rongo; SkyblockUtils.Location curLoc = SkyblockUtils.getLocation();
/* 283 */       switch (curLoc) {
/*     */         case MITHRIL_MINER:
/* 285 */           switch (commState) {
/*     */             case MITHRIL_MINER:
/* 287 */               mc.field_71439_g.func_71165_d("/warp forge");
/* 288 */               pathIter = new ArrayList<>(emisPath);
/* 289 */               commState = CommState.VROOM;
/* 290 */               recoverTimer.reset();
/*     */               break;
/*     */             
/*     */             case TITANIUM_MINER:
/* 294 */               if (pathIter.size() > 0) {
/* 295 */                 Vec3 currentPoint = pathIter.get(0);
/*     */                 
/* 297 */                 if (mc.field_71439_g.func_174791_d().func_72438_d(currentPoint) < 2.0D) {
/* 298 */                   pathIter.remove(0);
/*     */                   
/*     */                   return;
/*     */                 } 
/* 302 */                 Rotation rot = RotationUtils.getRotation(currentPoint);
/* 303 */                 rot.setPitch(mc.field_71439_g.field_70125_A);
/*     */                 
/* 305 */                 RotationUtils.setup(rot, Long.valueOf(250L));
/*     */                 
/* 307 */                 KeybindUtils.stopMovement();
/* 308 */                 for (KeyBinding kb : VecUtils.getNeededKeyPresses(mc.field_71439_g.func_174791_d(), currentPoint))
/* 309 */                   KeyBinding.func_74510_a(kb.func_151463_i(), true); 
/*     */                 break;
/*     */               } 
/* 312 */               commState = CommState.CLICK_PIGEON;
/* 313 */               KeybindUtils.stopMovement();
/*     */               break;
/*     */ 
/*     */             
/*     */             case null:
/* 318 */               if (pigeonTimer.hasReached(5000L)) {
/* 319 */                 if (this.pigeonLess.isEnabled()) {
/* 320 */                   Entity emissary = getEmissary();
/* 321 */                   if (emissary == null) {
/* 322 */                     ChatUtils.send("Couldn't find an Emissary or King :(", new String[0]); break;
/*     */                   } 
/* 324 */                   mc.field_71442_b.func_78768_b((EntityPlayer)mc.field_71439_g, emissary);
/* 325 */                   commState = CommState.IN_PIGEON;
/* 326 */                   pigeonTimer.reset();
/*     */                   break;
/*     */                 } 
/* 329 */                 SkyblockUtils.silentUse(0, this.pigeonSlot.getCurrent().intValue());
/* 330 */                 commState = CommState.IN_PIGEON;
/* 331 */                 pigeonTimer.reset();
/*     */               } 
/*     */               break;
/*     */ 
/*     */             
/*     */             case null:
/* 337 */               if (pigeonTimer.hasReached(1000L) && mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiChest) {
/* 338 */                 int complSlot = getCompletedSlot();
/* 339 */                 if (complSlot != -1) {
/* 340 */                   InventoryUtils.clickOpenContainerSlot(complSlot);
/* 341 */                   pigeonTimer.reset();
/*     */                   return;
/*     */                 } 
/* 344 */                 List<Integer> questSlots = getQuestSlots();
/* 345 */                 if (questSlots.isEmpty())
/*     */                   return; 
/* 347 */                 List<Quest> quests = new ArrayList<>();
/* 348 */                 for (null = questSlots.iterator(); null.hasNext(); ) { int i = ((Integer)null.next()).intValue();
/* 349 */                   ItemStack is = InventoryUtils.getStackInOpenContainerSlot(i);
/* 350 */                   List<String> itemLore = getLore(is);
/* 351 */                   if (itemLore.size() > 4) {
/* 352 */                     String lore = itemLore.get(4);
/*     */                     
/* 354 */                     Quest q = getQuest(lore);
/* 355 */                     if (q != null) {
/* 356 */                       quests.add(q);
/*     */                     }
/*     */                   }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 366 */                 if (!this.pigeonLess.isEnabled()) {
/* 367 */                   Location oldLoc = currentLocation;
/* 368 */                   for (Quest q : quests) {
/* 369 */                     if (oldLoc != null && questCanBeDone(q, oldLoc)) {
/* 370 */                       ChatUtils.send("No Navigation necessary.", new String[0]);
/* 371 */                       currentQuest = q;
/* 372 */                       mc.field_71439_g.func_71053_j();
/* 373 */                       commState = CommState.COMMIT;
/*     */                       
/*     */                       return;
/*     */                     } 
/*     */                   } 
/*     */                 } 
/* 379 */                 for (Quest q : quests) {
/* 380 */                   Location loc = getLocation(q);
/*     */                   
/* 382 */                   if (loc != null) {
/* 383 */                     currentQuest = q;
/* 384 */                     currentLocation = loc;
/* 385 */                     mc.field_71439_g.func_71053_j();
/* 386 */                     commState = CommState.WARP_FORGE; return;
/*     */                   } 
/*     */                 }  break;
/*     */               } 
/* 390 */               if (pigeonTimer.hasReached(1000L)) {
/* 391 */                 commState = CommState.CLICK_PIGEON;
/*     */               }
/*     */               break;
/*     */             
/*     */             case null:
/* 396 */               if (currentLocation == null) {
/* 397 */                 ChatUtils.send("Couldn't determine Commission", new String[0]);
/* 398 */                 pigeonTimer.reset();
/* 399 */                 commState = CommState.CLICK_PIGEON;
/*     */                 
/*     */                 break;
/*     */               } 
/* 403 */               ChatUtils.send("Navigating to: " + currentLocation.name, new String[0]);
/* 404 */               mc.field_71439_g.func_71165_d("/warp forge");
/* 405 */               path = null;
/* 406 */               commState = CommState.NAVIGATE;
/*     */               break;
/*     */             
/*     */             case null:
/* 410 */               if (mc.field_71439_g.func_180425_c().equals(new BlockPos(1, 149, -68)) && path == null) {
/* 411 */                 path = new ArrayList<>(currentLocation.path);
/* 412 */                 warpTimer.reset();
/* 413 */                 oldPos = null;
/* 414 */                 KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), true);
/* 415 */                 warpState = WarpState.SETUP;
/*     */               } 
/*     */               break;
/*     */             
/*     */             case null:
/* 420 */               if (currentQuest == Quest.GOBLIN_SLAYER || currentQuest == Quest.ICE_WALKER_SLAYER) {
/* 421 */                 CF4M.INSTANCE.moduleManager.toggle("IceGoblinSlayer");
/* 422 */                 commState = CommState.COMMITTING; break;
/* 423 */               }  if (currentLocation != null && currentLocation.name.equals("REFUEL") && refuelState == RefuelState.CLICK_MERCHANT) {
/* 424 */                 List<Entity> possible = mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72314_b(5.0D, 3.0D, 5.0D), a -> a.func_70005_c_().contains("Jotraeline Greatforge"));
/* 425 */                 if (!possible.isEmpty()) {
/* 426 */                   mc.field_71442_b.func_78768_b((EntityPlayer)mc.field_71439_g, possible.get(0));
/* 427 */                   refuelState = RefuelState.CLICK_DRILL_IN;
/*     */                 }  break;
/* 429 */               }  if (currentLocation != null && !currentLocation.name.equals("REFUEL")) {
/* 430 */                 if (this.pickSlot.getCurrent().intValue() > 0 && this.pickSlot.getCurrent().intValue() <= 8) {
/* 431 */                   mc.field_71439_g.field_71071_by.field_70461_c = this.pickSlot.getCurrent().intValue() - 1;
/*     */                 }
/* 433 */                 hugeTits = currentQuest.questName.contains("Titanium");
/* 434 */                 mithril = currentQuest.questName.contains("Mithril");
/* 435 */                 CF4M.INSTANCE.moduleManager.toggle("AutoMithril");
/* 436 */                 commState = CommState.COMMITTING;
/*     */               } 
/*     */               break;
/*     */             
/*     */             case null:
/* 441 */               warpOut = false;
/* 442 */               rongo = this.maxPlayerRange.getCurrent().intValue();
/*     */               
/* 444 */               if (currentQuest != Quest.GOBLIN_SLAYER && currentQuest != Quest.ICE_WALKER_SLAYER && !currentLocation.name.equals("REFUEL")) {
/* 445 */                 if (boostTimer.hasReached(125000L)) {
/* 446 */                   mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 447 */                   boostTimer.reset();
/*     */                 } 
/*     */                 
/* 450 */                 if (rongo == 0)
/*     */                   break; 
/* 452 */                 String warpName = "";
/* 453 */                 for (Entity e : mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72314_b(rongo, (rongo >> 1), rongo), a -> (a instanceof net.minecraft.client.entity.EntityOtherPlayerMP || a instanceof EntityArmorStand))) {
/* 454 */                   if (e instanceof EntityArmorStand) {
/* 455 */                     ItemStack bushSlot = ((EntityArmorStand)e).func_71124_b(4);
/* 456 */                     if (bushSlot != null && Item.func_150898_a((Block)Blocks.field_150330_I) == bushSlot.func_77973_b()) {
/* 457 */                       warpOut = true;
/* 458 */                       warpName = "Dead Bush"; break;
/*     */                     }  continue;
/*     */                   } 
/* 461 */                   if (!(e instanceof net.minecraft.client.entity.EntityOtherPlayerMP) || 
/* 462 */                     e.func_70005_c_().equals("Goblin ") || e.func_70005_c_().contains("Treasuer Hunter") || e.func_70005_c_().contains("Crystal Sentry"))
/*     */                     continue; 
/* 464 */                   String formatted = e.func_145748_c_().func_150254_d();
/* 465 */                   if (StringUtils.func_76338_a(formatted).equals(formatted)) {
/*     */                     continue;
/*     */                   }
/* 468 */                   if (formatted.startsWith("§r") && !formatted.startsWith("§r§")) {
/*     */                     continue;
/*     */                   }
/* 471 */                   warpOut = true;
/* 472 */                   warpName = e.func_70005_c_();
/*     */                 } 
/*     */                 
/* 475 */                 if (warpOut) {
/* 476 */                   ChatUtils.send("Switching lobbies cause a nice person is near you: " + warpName, new String[0]);
/* 477 */                   mc.field_71439_g.func_71165_d("/warp home");
/* 478 */                   if (CF4M.INSTANCE.moduleManager.isEnabled("AutoMithril"))
/* 479 */                     CF4M.INSTANCE.moduleManager.toggle("AutoMithril"); 
/* 480 */                   if (CF4M.INSTANCE.moduleManager.isEnabled("IceGoblinSlayer"))
/* 481 */                     CF4M.INSTANCE.moduleManager.toggle("IceGoblinSlayer"); 
/* 482 */                   KeyBinding.func_74506_a();
/* 483 */                   recoverTimer.reset();
/* 484 */                   commState = this.pigeonLess.isEnabled() ? CommState.PIGEONLESS_NAV : CommState.CLICK_PIGEON;
/*     */                 } 
/*     */               } 
/*     */               break;
/*     */           } 
/*     */           
/*     */           break;
/*     */         case TITANIUM_MINER:
/* 492 */           ChatUtils.send("Detected player in Island, re-warping", new String[0]);
/* 493 */           mc.field_71439_g.func_71165_d("/warp forge");
/*     */           break;
/*     */         
/*     */         case null:
/* 497 */           ChatUtils.send("Detected player in Hub, re-warping", new String[0]);
/* 498 */           mc.field_71439_g.func_71165_d("/warp forge");
/*     */           break;
/*     */         
/*     */         case null:
/* 502 */           ChatUtils.send("Detected player at Lift, re-warping", new String[0]);
/* 503 */           mc.field_71439_g.func_71165_d("/warp forge");
/*     */           break;
/*     */         
/*     */         case null:
/* 507 */           ChatUtils.send("Detected player in Lobby, re-warping", new String[0]);
/* 508 */           mc.field_71439_g.func_71165_d("/play skyblock");
/*     */           break;
/*     */         
/*     */         case null:
/* 512 */           ChatUtils.send("Detected player in Limbo, re-warping", new String[0]);
/* 513 */           mc.field_71439_g.func_71165_d("/l");
/*     */           break;
/*     */       } 
/*     */       
/* 517 */       if (curLoc != SkyblockUtils.Location.SKYBLOCK) {
/* 518 */         if (CF4M.INSTANCE.moduleManager.isEnabled("AutoMithril"))
/* 519 */           CF4M.INSTANCE.moduleManager.toggle("AutoMithril"); 
/* 520 */         if (CF4M.INSTANCE.moduleManager.isEnabled("IceGoblinSlayer"))
/* 521 */           CF4M.INSTANCE.moduleManager.toggle("IceGoblinSlayer"); 
/* 522 */         commState = this.pigeonLess.isEnabled() ? CommState.PIGEONLESS_NAV : CommState.CLICK_PIGEON;
/* 523 */         KeyBinding.func_74506_a();
/* 524 */         recoverTimer.reset();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onWorldUnload(WorldUnloadEvent event) {
/* 531 */     currentQuest = null;
/* 532 */     currentLocation = null;
/*     */   }
/*     */   
/*     */   private Entity getEmissary() {
/* 536 */     List<Entity> possible = new ArrayList<>();
/* 537 */     for (Entity e : mc.field_71441_e.field_72996_f) {
/* 538 */       if (e instanceof EntityArmorStand) {
/* 539 */         String name = StringUtils.func_76338_a(e.func_145748_c_().func_150260_c());
/* 540 */         if ((name.startsWith("Emissary") || name.startsWith("King ")) && !name.contains("Braum")) {
/* 541 */           possible.add(e);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 546 */     return possible.stream().min(Comparator.comparing(e -> Float.valueOf(e.func_70032_d((Entity)mc.field_71439_g)))).orElse(null);
/*     */   }
/*     */   
/*     */   private int getCompletedSlot() {
/* 550 */     for (int i = 9; i < 18; i++) {
/* 551 */       ItemStack is = InventoryUtils.getStackInOpenContainerSlot(i);
/* 552 */       if (is != null && 
/* 553 */         SkyblockUtils.stripString(is.func_82833_r()).startsWith("Commission #")) {
/* 554 */         List<String> itemLore = getLore(is);
/* 555 */         if (itemLore.stream().anyMatch(v -> v.toLowerCase().contains("completed"))) {
/* 556 */           return i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 561 */     return -1;
/*     */   }
/*     */   
/*     */   private List<Integer> getQuestSlots() {
/* 565 */     List<Integer> ret = new ArrayList<>();
/* 566 */     for (int i = 9; i < 18; i++) {
/* 567 */       ItemStack is = InventoryUtils.getStackInOpenContainerSlot(i);
/* 568 */       if (is != null && 
/* 569 */         SkyblockUtils.stripString(is.func_82833_r()).startsWith("Commission #")) {
/* 570 */         List<String> itemLore = getLore(is);
/* 571 */         if (itemLore.stream().noneMatch(v -> v.toLowerCase().contains("completed"))) {
/* 572 */           ret.add(Integer.valueOf(i));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 577 */     return ret;
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 582 */     String msg = event.message.func_150260_c();
/* 583 */     if (commState == CommState.COMMITTING) {
/* 584 */       if (msg.startsWith("Mining Speed Boost is now available!")) {
/* 585 */         mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 586 */         boostTimer.reset();
/* 587 */       } else if (msg.contains("Commission Complete! Visit the King to claim your rewards!") && !msg.contains(":")) {
/* 588 */         if (CF4M.INSTANCE.moduleManager.isEnabled("AutoMithril"))
/* 589 */           CF4M.INSTANCE.moduleManager.toggle("AutoMithril"); 
/* 590 */         if (CF4M.INSTANCE.moduleManager.isEnabled("IceGoblinSlayer"))
/* 591 */           CF4M.INSTANCE.moduleManager.toggle("IceGoblinSlayer"); 
/* 592 */         commState = this.pigeonLess.isEnabled() ? CommState.PIGEONLESS_NAV : CommState.CLICK_PIGEON;
/* 593 */       } else if (msg.startsWith("Your") && msg.contains("is empty! Refuel it by talking to a Drill Mechanic!")) {
/* 594 */         if (CF4M.INSTANCE.moduleManager.isEnabled("AutoMithril"))
/* 595 */           CF4M.INSTANCE.moduleManager.toggle("AutoMithril"); 
/* 596 */         if (CF4M.INSTANCE.moduleManager.isEnabled("IceGoblinSlayer"))
/* 597 */           CF4M.INSTANCE.moduleManager.toggle("IceGoblinSlayer"); 
/* 598 */         currentLocation = miningJson.locations.get(miningJson.locations.size() - 1);
/* 599 */         commState = CommState.WARP_FORGE;
/*     */       } 
/*     */     }
/* 602 */     if (msg.contains("You can't fast travel while in combat!")) {
/* 603 */       ChatUtils.send("Detected travel in combat, evacuating", new String[0]);
/* 604 */       mc.field_71439_g.func_71165_d("/l");
/* 605 */       recoverTimer.reset();
/* 606 */       commState = this.pigeonLess.isEnabled() ? CommState.PIGEONLESS_NAV : CommState.CLICK_PIGEON;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRenderTick(Render3DEvent event) {
/* 612 */     if (this.pigeonLess.isEnabled()) {
/* 613 */       if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/* 614 */         return;  if (pathIter.size() > 0 && commState == CommState.VROOM) {
/* 615 */         List<Vec3> lines = new ArrayList<>(pathIter);
/* 616 */         lines.add(0, mc.field_71439_g.func_174791_d());
/* 617 */         lines = (List<Vec3>)lines.stream().map(v -> v.func_178786_a(0.5D, 0.0D, 0.5D)).collect(Collectors.toList());
/* 618 */         Render3DUtils.draw3DChromaString(((Vec3)pathIter.get(0)).func_72441_c(0.0D, 1.0D, 0.0D), "VROOM VROOM", event.partialTicks);
/* 619 */         Render3DUtils.drawLines(lines, 2.0F, event.partialTicks);
/*     */       } 
/*     */ 
/*     */       
/* 623 */       if (!RotationUtils.done) {
/* 624 */         RotationUtils.update();
/*     */       }
/*     */     } 
/* 627 */     if (commState == CommState.NAVIGATE && path != null) {
/* 628 */       switch (warpState) {
/*     */         case MITHRIL_MINER:
/* 630 */           if (path.size() > 0) {
/* 631 */             if (warpTimer.hasReached(this.warpTime.getCurrent().intValue()) && !mc.field_71439_g.func_180425_c().equals(oldPos)) {
/* 632 */               PathPoint a = path.get(0);
/* 633 */               path.remove(0);
/* 634 */               RotationUtils.setup(RotationUtils.getRotation(new Vec3(a.x, a.y, a.z)), Long.valueOf(this.lookTime.getCurrent().intValue()));
/* 635 */               oldPos = mc.field_71439_g.func_180425_c();
/* 636 */               warpState = WarpState.LOOK; break;
/* 637 */             }  if (warpTimer.hasReached(2500L)) {
/* 638 */               ChatUtils.send("Got stuck while tp'ing, re-navigating", new String[0]);
/* 639 */               mc.field_71439_g.func_71165_d("/l");
/* 640 */               recoverTimer.reset();
/* 641 */               warpTimer.reset();
/* 642 */               commState = CommState.CLICK_PIGEON;
/*     */             }  break;
/*     */           } 
/* 645 */           KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
/* 646 */           commState = CommState.COMMIT;
/*     */           break;
/*     */ 
/*     */         
/*     */         case TITANIUM_MINER:
/* 651 */           if (System.currentTimeMillis() <= RotationUtils.endTime) {
/* 652 */             RotationUtils.update(); break;
/*     */           } 
/* 654 */           RotationUtils.update();
/* 655 */           warpTimer.reset();
/* 656 */           warpState = WarpState.WARP;
/*     */           break;
/*     */ 
/*     */         
/*     */         case null:
/* 661 */           if (warpTimer.hasReached(this.warpTime.getCurrent().intValue())) {
/* 662 */             mc.field_71439_g.field_71071_by.field_70461_c = this.aotvSlot.getCurrent().intValue() - 1;
/* 663 */             mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 664 */             warpTimer.reset();
/* 665 */             warpState = WarpState.SETUP;
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getLore(ItemStack is) {
/* 673 */     List<String> lore = new ArrayList<>();
/* 674 */     if (is == null || !is.func_77942_o()) {
/* 675 */       return lore;
/*     */     }
/* 677 */     NBTTagCompound nbt = is.func_77978_p();
/* 678 */     if (nbt.func_74775_l("display") != null) {
/* 679 */       NBTTagCompound display = nbt.func_74775_l("display");
/* 680 */       if (display.func_150295_c("Lore", 8) != null) {
/* 681 */         NBTTagList nbtLore = display.func_150295_c("Lore", 8);
/* 682 */         for (int i = 0; i < nbtLore.func_74745_c(); i++) {
/* 683 */           lore.add(SkyblockUtils.stripString(nbtLore.func_179238_g(i).toString()).replaceAll("\"", ""));
/*     */         }
/*     */       } 
/*     */     } 
/* 687 */     return lore;
/*     */   }
/*     */   
/*     */   public static Quest getQuest(String lore) {
/* 691 */     for (Quest q : Quest.values()) {
/* 692 */       if (q.questName.equalsIgnoreCase(lore)) {
/* 693 */         return q;
/*     */       }
/*     */     } 
/* 696 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean questCanBeDone(Quest quest, Location loc) {
/* 700 */     if (loc.name.equals("REFUEL")) {
/* 701 */       return false;
/*     */     }
/* 703 */     switch (quest) {
/*     */       case MITHRIL_MINER:
/*     */       case TITANIUM_MINER:
/* 706 */         return (!loc.name.equals("ICE WALKER SLAYER") && !loc.name.equals("GOBLIN SLAYER"));
/*     */     } 
/*     */     
/* 709 */     if (quest.questName.toLowerCase().contains(loc.name.toLowerCase())) {
/* 710 */       return true;
/*     */     }
/*     */     
/* 713 */     String name = null;
/* 714 */     if (loc.name.contains("COMMISSION")) {
/* 715 */       name = StringUtils.substringBefore(loc.name, " COMMISSION");
/* 716 */     } else if (loc.name.contains(" (")) {
/* 717 */       name = StringUtils.substringBefore(loc.name, " (");
/*     */     } 
/*     */     
/* 720 */     if (name != null && quest.questName.toLowerCase().contains(name.toLowerCase())) {
/* 721 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 726 */     return false;
/*     */   }
/*     */   public static Location getLocation(Quest quest) {
/*     */     List<Location> sub;
/* 730 */     switch (quest)
/*     */     { case MITHRIL_MINER:
/*     */       case TITANIUM_MINER:
/* 733 */         sub = miningJson.locations.subList(0, miningJson.locations.size() - 5);
/* 734 */         if (sub.size() > 0) {
/* 735 */           return sub.get(RandomUtil.randBetween(0, sub.size() - 1));
/*     */         }
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
/* 764 */         return null; }  List<Location> possible = new ArrayList<>(); for (Location loc : miningJson.locations) { if (quest.questName.toLowerCase().contains(loc.name.toLowerCase())) { possible.add(loc); continue; }  String name = null; if (loc.name.contains("COMMISSION")) { name = StringUtils.substringBefore(loc.name, " COMMISSION"); } else if (loc.name.contains(" (")) { name = StringUtils.substringBefore(loc.name, " ("); }  if (name != null && quest.questName.toLowerCase().contains(name.toLowerCase())) possible.add(loc);  }  if (possible.size() > 0) return possible.get(RandomUtil.randBetween(0, possible.size() - 1));  return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\player\CommMacro.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */