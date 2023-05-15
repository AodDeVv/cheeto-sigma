/*     */ package xyz.apfelmus.cheeto.client.modules.misc;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.inventory.GuiChest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerChest;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cf4m.module.Category;
/*     */ import xyz.apfelmus.cheeto.client.events.BackgroundDrawnEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.GuiOpenEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ 
/*     */ @Module(name = "ExperimentSolver", category = Category.MISC)
/*     */ public class ExperimentSolver
/*     */ {
/*     */   @Setting(name = "Chronomatron", description = "Solve Chronomattron?")
/*  29 */   BooleanSetting chronomatron = new BooleanSetting(true);
/*     */   @Setting(name = "Ultrasequencer", description = "Solve Ultrasequencer?")
/*  31 */   BooleanSetting ultrasequencer = new BooleanSetting(true);
/*     */ 
/*     */   
/*  34 */   private static int until = 0;
/*  35 */   private static int tickAmount = 0;
/*  36 */   private static Minecraft mc = Minecraft.func_71410_x();
/*  37 */   private static Slot[] clickInOrderSlots = new Slot[36];
/*  38 */   private static int lastChronomatronRound = 0;
/*  39 */   private static List<String> chronomatronPattern = new ArrayList<>();
/*  40 */   private static int chronomatronMouseClicks = 0;
/*  41 */   private static int lastUltraSequencerClicked = 0;
/*  42 */   private static long lastInteractTime = 0L;
/*     */   
/*     */   @Event
/*     */   public void onTick(BackgroundDrawnEvent event) {
/*  46 */     if (mc.field_71462_r instanceof GuiChest) {
/*  47 */       GuiChest inventory = (GuiChest)event.gui;
/*  48 */       Container containerChest = inventory.field_147002_h;
/*  49 */       if (containerChest instanceof ContainerChest) {
/*  50 */         List<Slot> invSlots = containerChest.field_75151_b;
/*  51 */         String invName = ((ContainerChest)containerChest).func_85151_d().func_145748_c_().func_150260_c().trim();
/*     */ 
/*     */         
/*  54 */         if (this.chronomatron.isEnabled() && invName.startsWith("Chronomatron (")) {
/*  55 */           EntityPlayerSP player = mc.field_71439_g;
/*  56 */           if (player.field_71071_by.func_70445_o() == null && invSlots.size() > 48 && ((Slot)invSlots.get(49)).func_75211_c() != null) {
/*  57 */             if (((Slot)invSlots.get(49)).func_75211_c().func_82833_r().startsWith("§7Timer: §a") && ((Slot)invSlots.get(4)).func_75211_c() != null) {
/*  58 */               int round = (((Slot)invSlots.get(4)).func_75211_c()).field_77994_a;
/*  59 */               int timerSeconds = Integer.parseInt(StringUtils.func_76338_a(((Slot)invSlots.get(49)).func_75211_c().func_82833_r()).replaceAll("[^\\d]", ""));
/*  60 */               if (round != lastChronomatronRound && timerSeconds == round + 2) {
/*  61 */                 lastChronomatronRound = round;
/*  62 */                 for (int i = 10; i <= 43; i++) {
/*  63 */                   ItemStack stack = ((Slot)invSlots.get(i)).func_75211_c();
/*  64 */                   if (stack != null && 
/*  65 */                     stack.func_77973_b() == Item.func_150898_a(Blocks.field_150406_ce)) {
/*  66 */                     chronomatronPattern.add(stack.func_82833_r());
/*     */                     
/*     */                     break;
/*     */                   } 
/*     */                 } 
/*     */               } 
/*  72 */               if (chronomatronMouseClicks < chronomatronPattern.size() && player.field_71071_by.func_70445_o() == null) {
/*  73 */                 for (int i = 10; i <= 43; i++) {
/*  74 */                   ItemStack glass = ((Slot)invSlots.get(i)).func_75211_c();
/*  75 */                   if (glass != null && player.field_71071_by.func_70445_o() == null && tickAmount % 5 == 0) {
/*  76 */                     Slot glassSlot = invSlots.get(i);
/*  77 */                     if (glass.func_82833_r().equals(chronomatronPattern.get(chronomatronMouseClicks))) {
/*  78 */                       mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, glassSlot.field_75222_d, 0, 0, (EntityPlayer)mc.field_71439_g);
/*  79 */                       lastInteractTime = 0L;
/*  80 */                       chronomatronMouseClicks++;
/*     */                       break;
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */               }
/*  86 */             } else if (((Slot)invSlots.get(49)).func_75211_c().func_82833_r().equals("§aRemember the pattern!")) {
/*  87 */               chronomatronMouseClicks = 0;
/*     */             } 
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  93 */         if (this.ultrasequencer.isEnabled() && invName.startsWith("Ultrasequencer (")) {
/*  94 */           EntityPlayerSP player = mc.field_71439_g;
/*  95 */           if (invSlots.size() > 48 && ((Slot)invSlots.get(49)).func_75211_c() != null && player.field_71071_by.func_70445_o() == null && ((Slot)invSlots.get(49)).func_75211_c().func_82833_r().startsWith("§7Timer: §a")) {
/*  96 */             lastUltraSequencerClicked = 0;
/*  97 */             for (Slot slot4 : clickInOrderSlots) {
/*  98 */               if (slot4 != null && slot4.func_75211_c() != null && StringUtils.func_76338_a(slot4.func_75211_c().func_82833_r()).matches("\\d+")) {
/*  99 */                 int number = Integer.parseInt(StringUtils.func_76338_a(slot4.func_75211_c().func_82833_r()));
/* 100 */                 if (number > lastUltraSequencerClicked) {
/* 101 */                   lastUltraSequencerClicked = number;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             
/* 106 */             if (clickInOrderSlots[lastUltraSequencerClicked] != null && player.field_71071_by.func_70445_o() == null && tickAmount % 2 == 0 && lastUltraSequencerClicked != 0 && until == lastUltraSequencerClicked) {
/* 107 */               Slot nextSlot = clickInOrderSlots[lastUltraSequencerClicked];
/* 108 */               mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, nextSlot.field_75222_d, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 109 */               until = lastUltraSequencerClicked + 1;
/* 110 */               tickAmount = 0;
/*     */             } 
/*     */             
/* 113 */             if (clickInOrderSlots[lastUltraSequencerClicked] != null && player.field_71071_by.func_70445_o() == null && tickAmount == 18 && lastUltraSequencerClicked < 1) {
/* 114 */               Slot nextSlot = clickInOrderSlots[lastUltraSequencerClicked];
/* 115 */               mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, nextSlot.field_75222_d, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 116 */               tickAmount = 0;
/* 117 */               until = 1;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onGuiOpen(GuiOpenEvent event) {
/* 128 */     clickInOrderSlots = new Slot[36];
/* 129 */     lastChronomatronRound = 0;
/* 130 */     chronomatronPattern.clear();
/* 131 */     chronomatronMouseClicks = 0;
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/* 136 */     tickAmount++;
/* 137 */     if (tickAmount % 20 == 0) {
/* 138 */       tickAmount = 0;
/*     */     }
/* 140 */     if (mc.field_71462_r instanceof GuiChest && 
/* 141 */       this.ultrasequencer.isEnabled()) {
/* 142 */       ContainerChest chest = (ContainerChest)mc.field_71439_g.field_71070_bA;
/* 143 */       List<Slot> invSlots = ((GuiChest)mc.field_71462_r).field_147002_h.field_75151_b;
/* 144 */       String chestName = chest.func_85151_d().func_145748_c_().func_150260_c().trim();
/*     */       
/* 146 */       if (chestName.startsWith("Ultrasequencer (") && ((Slot)invSlots.get(49)).func_75211_c() != null && ((Slot)invSlots.get(49)).func_75211_c().func_82833_r().equals("§aRemember the pattern!"))
/* 147 */         for (int l = 9; l <= 44; l++) {
/* 148 */           if (invSlots.get(l) != null && (
/* 149 */             (Slot)invSlots.get(l)).func_75211_c() != null) {
/* 150 */             String itemName = StringUtils.func_76338_a(((Slot)invSlots.get(l)).func_75211_c().func_82833_r());
/* 151 */             if (itemName.matches("\\d+")) {
/* 152 */               int number = Integer.parseInt(itemName);
/* 153 */               clickInOrderSlots[number - 1] = invSlots.get(l);
/*     */             } 
/*     */           } 
/*     */         }  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\misc\ExperimentSolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */