/*     */ package xyz.apfelmus.cheeto.client.modules.misc;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerChest;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Disable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.BackgroundDrawnEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.RandomUtil;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ @Module(name = "ApfelTerms", category = Category.MISC)
/*     */ public class ApfelTerms {
/*     */   @Setting(name = "Delay", description = "Delay between each Terminal Click")
/*  33 */   private IntegerSetting delay = new IntegerSetting(
/*  34 */       Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "RandomRange", description = "Extra randomization for the click delay")
/*  35 */   private IntegerSetting randomRange = new IntegerSetting(
/*  36 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(500)); @Setting(name = "MaxLag", description = "Set to something slightly above your ping")
/*  37 */   private IntegerSetting maxLag = new IntegerSetting(
/*  38 */       Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "Pingless", description = "Will spoof WindowID to click faster")
/*  39 */   private BooleanSetting pingless = new BooleanSetting(true);
/*     */   @Setting(name = "Debug", description = "Will show info about the Terminals")
/*  41 */   private BooleanSetting debug = new BooleanSetting(true);
/*     */   @Setting(name = "DebugX", description = "X position of the debug text")
/*  43 */   private IntegerSetting debugX = new IntegerSetting(
/*  44 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100)); @Setting(name = "DebugY", description = "Y position of the debug text")
/*  45 */   private IntegerSetting debugY = new IntegerSetting(
/*  46 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100));
/*     */   
/*  48 */   private static Terminal currentTerm = Terminal.NONE;
/*  49 */   private static List<TermSlot> clickQueue = new ArrayList<>();
/*     */   
/*     */   private static boolean recalculate = false;
/*  52 */   private static final int[] mazeDirection = new int[] { -9, -1, 1, 9 };
/*  53 */   private static char letterNeeded = Character.MIN_VALUE;
/*  54 */   private static String colorNeeded = null;
/*     */   
/*  56 */   private static List<Integer> colors = new ArrayList<>(Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(4), Integer.valueOf(13), Integer.valueOf(11), Integer.valueOf(14) }));
/*     */   
/*     */   private static int windowId;
/*     */   private static long lastClickTime;
/*     */   private static long clickDelay;
/*  61 */   private static int windowClicks = 0;
/*  62 */   private static int currentSlot = 0;
/*     */   
/*  64 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   private enum Terminal {
/*  67 */     NONE,
/*  68 */     MAZE,
/*  69 */     ORDER,
/*  70 */     PANES,
/*  71 */     NAME,
/*  72 */     COLOR,
/*  73 */     SAME,
/*  74 */     TIMING;
/*     */   }
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/*  79 */     currentTerm = Terminal.NONE;
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onGuiOpen(GuiOpenEvent event) {
/*  84 */     windowClicks = 0;
/*  85 */     currentTerm = Terminal.NONE;
/*  86 */     clickQueue.clear();
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onGuiDraw(BackgroundDrawnEvent event) {
/*  91 */     if (!SkyblockUtils.isInDungeon())
/*     */       return; 
/*  93 */     if (event.gui instanceof GuiChest) {
/*  94 */       Container container = ((GuiChest)event.gui).field_147002_h;
/*     */       
/*  96 */       if (container instanceof ContainerChest) {
/*  97 */         List<Slot> invSlots = container.field_75151_b;
/*     */         
/*  99 */         if (currentTerm == Terminal.NONE) {
/* 100 */           String chestName = ((ContainerChest)container).func_85151_d().func_145748_c_().func_150260_c();
/*     */           
/* 102 */           if (chestName.equals("Navigate the maze!")) {
/* 103 */             currentTerm = Terminal.MAZE;
/* 104 */           } else if (chestName.equals("Click in order!")) {
/* 105 */             currentTerm = Terminal.ORDER;
/* 106 */           } else if (chestName.equals("Correct all the panes!")) {
/* 107 */             currentTerm = Terminal.PANES;
/* 108 */           } else if (chestName.equals("Change all to same color!")) {
/* 109 */             currentTerm = Terminal.SAME;
/* 110 */           } else if (chestName.equals("Click the button on time!")) {
/* 111 */             currentTerm = Terminal.TIMING;
/* 112 */           } else if (chestName.startsWith("What starts with:")) {
/* 113 */             currentTerm = Terminal.NAME;
/* 114 */           } else if (chestName.startsWith("Select all the")) {
/* 115 */             currentTerm = Terminal.COLOR;
/*     */           } 
/*     */         } else {
/* 118 */           if (clickQueue.isEmpty() || recalculate) {
/* 119 */             int randTime = RandomUtil.randBetween(-(this.randomRange.getCurrent().intValue() / 2), this.randomRange.getCurrent().intValue() / 2);
/* 120 */             clickDelay = (this.delay.getCurrent().intValue() + randTime);
/* 121 */             recalculate = getClicks((ContainerChest)container);
/*     */           } else {
/* 123 */             switch (currentTerm) {
/*     */               case PURPLE:
/*     */               case LIME:
/*     */               case null:
/* 127 */                 for (TermSlot slot : clickQueue) {
/* 128 */                   if (slot.clicked > 0L && System.currentTimeMillis() - slot.clicked >= this.maxLag.getCurrent().intValue()) {
/* 129 */                     Slot s = invSlots.get(slot.slot.field_75222_d);
/* 130 */                     if (s.func_75216_d() && s.func_75211_c().func_77952_i() != 5) {
/* 131 */                       currentSlot = clickQueue.indexOf(slot);
/* 132 */                       windowClicks = currentSlot;
/*     */                       break;
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */                 break;
/*     */ 
/*     */               
/*     */               case null:
/*     */               case null:
/* 142 */                 for (TermSlot slot : clickQueue) {
/* 143 */                   if (slot.clicked > 0L && System.currentTimeMillis() - slot.clicked >= this.maxLag.getCurrent().intValue()) {
/* 144 */                     Slot s = invSlots.get(slot.slot.field_75222_d);
/* 145 */                     if (s.func_75216_d() && !s.func_75211_c().func_77948_v()) {
/* 146 */                       currentSlot = clickQueue.indexOf(slot);
/* 147 */                       windowClicks = currentSlot;
/*     */                       break;
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */                 break;
/*     */ 
/*     */               
/*     */               case null:
/* 156 */                 for (TermSlot slot : clickQueue) {
/* 157 */                   if (slot.clicked > 0L && System.currentTimeMillis() - slot.clicked >= this.maxLag.getCurrent().intValue()) {
/* 158 */                     Slot s = invSlots.get(slot.slot.field_75222_d);
/* 159 */                     if (s.func_75216_d() && s.func_75211_c().func_77952_i() == slot.color) {
/* 160 */                       currentSlot = clickQueue.indexOf(slot);
/* 161 */                       windowClicks = currentSlot;
/*     */                       break;
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */                 break;
/*     */             } 
/*     */           
/*     */           } 
/* 170 */           if (!clickQueue.isEmpty() && System.currentTimeMillis() - lastClickTime >= clickDelay && 
/* 171 */             currentTerm != Terminal.NONE) {
/* 172 */             if (currentSlot >= clickQueue.size()) {
/* 173 */               currentSlot = 0;
/*     */             }
/* 175 */             clickSlot(clickQueue.get(currentSlot));
/* 176 */             int randTime = RandomUtil.randBetween(-(this.randomRange.getCurrent().intValue() / 2), this.randomRange.getCurrent().intValue() / 2);
/* 177 */             clickDelay = (this.delay.getCurrent().intValue() + randTime);
/*     */             
/* 179 */             if (currentTerm == Terminal.TIMING) {
/* 180 */               clickQueue.remove(0);
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 185 */         if (this.debug.isEnabled()) {
/* 186 */           List<String> texts = new ArrayList<>(Arrays.asList(new String[] {
/* 187 */                   String.format("Current Term: %s", new Object[] { currentTerm
/* 188 */                     }), String.format("Start WindowID: %d", new Object[] { Integer.valueOf(windowId)
/* 189 */                     }), String.format("Current WindowID: %d", new Object[] { Integer.valueOf(mc.field_71439_g.field_71070_bA.field_75152_c)
/* 190 */                     }), String.format("Pingless Clicks: %d", new Object[] { Integer.valueOf(windowClicks)
/* 191 */                     }), String.format("ClickQueue Size: %d", new Object[] { Integer.valueOf(clickQueue.size())
/* 192 */                     }), String.format("Current Slot: %d", new Object[] { Integer.valueOf(currentSlot) })
/*     */                 }));
/*     */           
/* 195 */           ScaledResolution scaled = new ScaledResolution(Minecraft.func_71410_x());
/* 196 */           int sf = ResManager.getScaleFactor();
/* 197 */           int drawX = (int)((scaled.func_78326_a() * sf) * this.debugX.getCurrent().intValue() / 100.0D);
/* 198 */           int drawY = (int)((scaled.func_78328_b() * sf) * this.debugY.getCurrent().intValue() / 100.0D);
/* 199 */           for (String text : texts) {
/* 200 */             FontUtils.normal.drawChromaString(text, drawX, drawY);
/* 201 */             drawY += FontUtils.normal.getFontHeight() + 1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/* 210 */     if (!SkyblockUtils.isInDungeon())
/*     */       return; 
/* 212 */     if (!(mc.field_71462_r instanceof GuiChest)) {
/* 213 */       currentTerm = Terminal.NONE;
/* 214 */       clickQueue.clear();
/* 215 */       letterNeeded = Character.MIN_VALUE;
/* 216 */       colorNeeded = null;
/* 217 */       windowClicks = 0;
/* 218 */       currentSlot = 0;
/*     */     }  } private static boolean getClicks(ContainerChest cc) { int startSlot, endSlot; boolean[] mazeVisited;
/*     */     int j;
/*     */     List<Slot> panes;
/*     */     int i, solve, confirm, align, bop, k;
/* 223 */     List<Slot> invSlots = cc.field_75151_b;
/* 224 */     String chestName = cc.func_85151_d().func_145748_c_().func_150260_c();
/* 225 */     clickQueue.clear();
/*     */     
/* 227 */     switch (currentTerm) {
/*     */       case PURPLE:
/* 229 */         startSlot = -1;
/* 230 */         endSlot = -1;
/* 231 */         mazeVisited = new boolean[54];
/*     */         
/* 233 */         for (Slot slot : invSlots) {
/* 234 */           if (startSlot >= 0 && endSlot >= 0)
/* 235 */             break;  if (slot.field_75224_c == mc.field_71439_g.field_71071_by)
/* 236 */             continue;  ItemStack stack = slot.func_75211_c();
/* 237 */           if (stack == null || 
/* 238 */             stack.func_77973_b() != Item.func_150898_a((Block)Blocks.field_150397_co))
/* 239 */             continue;  if (stack.func_77952_i() == 5) { startSlot = slot.field_75222_d; continue; }
/*     */           
/* 241 */           if (stack.func_77952_i() != 14)
/* 242 */             continue;  endSlot = slot.field_75222_d;
/*     */         } 
/*     */         
/* 245 */         while (startSlot != endSlot) {
/* 246 */           boolean newSlotChosen = false;
/*     */           
/* 248 */           for (int m = 0; m < 4; m++) {
/* 249 */             int slotNumber = startSlot + mazeDirection[m];
/* 250 */             if (slotNumber == endSlot) return false; 
/* 251 */             if (slotNumber >= 0 && slotNumber <= 53 && (m != 1 || slotNumber % 9 != 8) && (
/* 252 */               m != 2 || slotNumber % 9 != 0) && 
/* 253 */               !mazeVisited[slotNumber]) {
/* 254 */               ItemStack stack = ((Slot)invSlots.get(slotNumber)).func_75211_c();
/* 255 */               if (stack != null && 
/* 256 */                 stack.func_77973_b() == Item.func_150898_a((Block)Blocks.field_150397_co) && stack.func_77952_i() == 0) {
/* 257 */                 clickQueue.add(new TermSlot(invSlots.get(slotNumber)));
/* 258 */                 startSlot = slotNumber;
/* 259 */                 mazeVisited[slotNumber] = true;
/* 260 */                 newSlotChosen = true;
/*     */ 
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 269 */           if (!newSlotChosen) {
/* 270 */             return true;
/*     */           }
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case LIME:
/* 277 */         while (clickQueue.size() < 14) {
/* 278 */           clickQueue.add((TermSlot)null);
/*     */         }
/* 280 */         for (j = 10; j <= 25; j++) {
/* 281 */           if (j != 17 && 
/* 282 */             j != 18) {
/* 283 */             ItemStack stack = ((Slot)invSlots.get(j)).func_75211_c();
/* 284 */             if (stack != null && 
/* 285 */               stack.func_77973_b() == Item.func_150898_a((Block)Blocks.field_150397_co) && stack.func_77952_i() == 14 && stack.field_77994_a < 15) {
/* 286 */               clickQueue.set(stack.field_77994_a - 1, new TermSlot(invSlots.get(j)));
/*     */             }
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 292 */         if (clickQueue.removeIf(Objects::isNull)) {
/* 293 */           return true;
/*     */         }
/*     */         break;
/*     */       
/*     */       case null:
/* 298 */         for (Slot slot : invSlots) {
/* 299 */           if (slot.field_75224_c == mc.field_71439_g.field_71071_by || 
/* 300 */             slot.field_75222_d < 9 || slot.field_75222_d > 35 || slot.field_75222_d % 9 <= 1 || 
/* 301 */             slot.field_75222_d % 9 >= 7)
/*     */             continue; 
/* 303 */           ItemStack itemStack = slot.func_75211_c();
/* 304 */           if (itemStack == null) return true; 
/* 305 */           if (itemStack.func_77973_b() != Item.func_150898_a((Block)Blocks.field_150397_co) || itemStack.func_77952_i() != 14) {
/*     */             continue;
/*     */           }
/* 308 */           clickQueue.add(new TermSlot(slot));
/*     */         } 
/*     */         break;
/*     */       
/*     */       case null:
/* 313 */         letterNeeded = chestName.charAt(chestName.indexOf("'") + 1);
/* 314 */         if (letterNeeded != '\000') {
/* 315 */           for (Slot slot : invSlots) {
/* 316 */             if (slot.field_75224_c == mc.field_71439_g.field_71071_by || 
/* 317 */               slot.field_75222_d < 9 || slot.field_75222_d > 44 || slot.field_75222_d % 9 == 0 || 
/* 318 */               slot.field_75222_d % 9 == 8)
/*     */               continue; 
/* 320 */             ItemStack itemStack = slot.func_75211_c();
/* 321 */             if (itemStack == null) return true; 
/* 322 */             if (itemStack.func_77948_v() || 
/* 323 */               StringUtils.func_76338_a(itemStack.func_82833_r()).charAt(0) != letterNeeded) {
/*     */               continue;
/*     */             }
/* 326 */             clickQueue.add(new TermSlot(slot));
/*     */           } 
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 333 */         for (EnumDyeColor color : EnumDyeColor.values()) {
/* 334 */           String colorName = color.func_176610_l().replaceAll("_", " ").toUpperCase();
/* 335 */           if (chestName.contains(colorName)) {
/* 336 */             colorNeeded = color.func_176762_d();
/*     */             break;
/*     */           } 
/*     */         } 
/* 340 */         if (colorNeeded != null) {
/* 341 */           for (Slot slot : invSlots) {
/* 342 */             if (slot.field_75224_c == mc.field_71439_g.field_71071_by || 
/* 343 */               slot.field_75222_d < 9 || slot.field_75222_d > 44 || slot.field_75222_d % 9 == 0 || 
/* 344 */               slot.field_75222_d % 9 == 8)
/*     */               continue; 
/* 346 */             ItemStack itemStack = slot.func_75211_c();
/* 347 */             if (itemStack == null) return true; 
/* 348 */             if (itemStack.func_77948_v() || 
/* 349 */               !itemStack.func_77977_a().contains(colorNeeded))
/*     */               continue; 
/* 351 */             clickQueue.add(new TermSlot(slot));
/*     */           } 
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 358 */         panes = new ArrayList<>();
/* 359 */         for (i = 12; i <= 32; i++) {
/* 360 */           Slot slot = invSlots.get(i);
/* 361 */           if (slot.func_75216_d()) {
/* 362 */             ItemStack is = slot.func_75211_c();
/* 363 */             if (is.func_77952_i() != 0 && is.func_77952_i() != 0) {
/* 364 */               panes.add(slot);
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 369 */         solve = getFewestNeededClicks(panes);
/*     */         
/* 371 */         for (Slot s : panes) {
/* 372 */           if (s.func_75216_d()) {
/* 373 */             ItemStack is = s.func_75211_c();
/* 374 */             int clicks = getClicksForColor(is.func_77952_i(), solve);
/*     */             
/* 376 */             if (clicks != 0) {
/* 377 */               for (int m = 0; m < Math.abs(clicks); m++) {
/* 378 */                 TermSlot ts = new TermSlot(s);
/* 379 */                 ts.clickButton = (clicks > 0) ? 0 : 1;
/* 380 */                 clickQueue.add(ts);
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case null:
/* 389 */         confirm = 0;
/* 390 */         align = -1;
/* 391 */         bop = -1;
/*     */         
/* 393 */         for (k = 1; k <= 50; k++) {
/* 394 */           Slot slot = invSlots.get(k);
/* 395 */           if (slot.func_75216_d()) {
/* 396 */             ItemStack stack = slot.func_75211_c();
/* 397 */             EnumDyeColor color = EnumDyeColor.func_176764_b(stack.func_77952_i());
/* 398 */             switch (color) {
/*     */               case PURPLE:
/* 400 */                 if (align == -1) {
/* 401 */                   align = k % 9;
/*     */                 }
/*     */                 break;
/*     */               case LIME:
/* 405 */                 if (stack.func_77973_b() == Item.func_150898_a(Blocks.field_150406_ce)) {
/* 406 */                   confirm = k; break;
/* 407 */                 }  if (stack.func_77973_b() == Item.func_150898_a((Block)Blocks.field_150397_co) && 
/* 408 */                   bop == -1) {
/* 409 */                   bop = k % 9;
/*     */                 }
/*     */                 break;
/*     */             } 
/*     */           
/*     */           } 
/*     */         } 
/* 416 */         if (confirm == bop) {
/* 417 */           clickQueue.add(new TermSlot(invSlots.get(confirm))); break;
/*     */         } 
/* 419 */         return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 425 */     return false; }
/*     */ 
/*     */   
/*     */   private static int getFewestNeededClicks(List<Slot> panes) {
/* 429 */     int bestColor = -1;
/* 430 */     int bestClicks = 20;
/*     */     
/* 432 */     for (Iterator<Integer> iterator = colors.iterator(); iterator.hasNext(); ) { int color = ((Integer)iterator.next()).intValue();
/*     */ 
/*     */ 
/*     */       
/* 436 */       int clicks = panes.stream().filter(Slot::func_75216_d).mapToInt(v -> Math.abs(getClicksForColor(v.func_75211_c().func_77952_i(), color))).sum();
/*     */       
/* 438 */       if (clicks > 0 && clicks < bestClicks) {
/* 439 */         bestColor = color;
/* 440 */         bestClicks = clicks;
/*     */       }  }
/*     */ 
/*     */     
/* 444 */     return bestColor;
/*     */   }
/*     */   
/*     */   private static int getClicksForColor(int from, int to) {
/* 448 */     int fromIndex = colors.indexOf(Integer.valueOf(from));
/* 449 */     int toIndex = colors.indexOf(Integer.valueOf(to));
/*     */     
/* 451 */     int clicks = toIndex - fromIndex;
/* 452 */     int floored = Math.floorMod(clicks, colors.size());
/*     */     
/* 454 */     if (clicks > colors.size() / 2) {
/* 455 */       return clicks - colors.size();
/*     */     }
/*     */     
/* 458 */     return (floored < Math.abs(clicks)) ? floored : clicks;
/*     */   }
/*     */   
/*     */   private void clickSlot(TermSlot slot) {
/* 462 */     if (windowClicks == 0) {
/* 463 */       windowId = mc.field_71439_g.field_71070_bA.field_75152_c;
/*     */     }
/* 465 */     if (slot.clickButton != 2 && slot.slot.func_75216_d()) {
/* 466 */       slot.color = slot.slot.func_75211_c().func_77952_i();
/*     */     }
/* 468 */     if (this.pingless.isEnabled()) {
/* 469 */       int id = windowId + windowClicks;
/* 470 */       if (id > 100) id -= 100; 
/* 471 */       mc.field_71442_b.func_78753_a(windowId + (this.pingless.isEnabled() ? windowClicks : 0), slot.slot.field_75222_d, slot.clickButton, 0, (EntityPlayer)mc.field_71439_g);
/* 472 */       windowClicks++;
/*     */     } else {
/* 474 */       mc.field_71442_b.func_78753_a(windowId, slot.slot.field_75222_d, slot.clickButton, 0, (EntityPlayer)mc.field_71439_g);
/*     */     } 
/*     */     
/* 477 */     slot.clicked = System.currentTimeMillis();
/* 478 */     lastClickTime = System.currentTimeMillis();
/* 479 */     currentSlot++;
/*     */   }
/*     */   
/*     */   private static class TermSlot {
/*     */     public Slot slot;
/*     */     public int color;
/* 485 */     public int clickButton = 2;
/*     */     public long clicked;
/*     */     
/*     */     public TermSlot(Slot slot) {
/* 489 */       this.slot = slot;
/* 490 */       this.clicked = -1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 495 */       return String.format("%d: %d - %d", new Object[] { Integer.valueOf(this.slot.field_75222_d), Integer.valueOf(this.color), Integer.valueOf(this.clickButton) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\misc\ApfelTerms.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */