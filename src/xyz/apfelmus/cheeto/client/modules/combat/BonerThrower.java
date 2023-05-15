/*     */ package xyz.apfelmus.cheeto.client.modules.combat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cf4m.module.Category;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.TimeHelper;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.InventoryUtils;
/*     */ 
/*     */ @Module(name = "BonerThrower", category = Category.COMBAT)
/*     */ public class BonerThrower {
/*     */   @Setting(name = "SilentUse", description = "Always stay on the configured slot")
/*  23 */   private BooleanSetting silentUse = new BooleanSetting(false);
/*     */   @Setting(name = "ThrowDelay", description = "Throw delay in milliseconds")
/*  25 */   private IntegerSetting throwDelay = new IntegerSetting(
/*  26 */       Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "MainSlot", description = "Slot of the weapon you want held")
/*  27 */   private IntegerSetting mainSlot = new IntegerSetting(
/*  28 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "InvMode", description = "A bit bannable")
/*  29 */   private BooleanSetting invMode = new BooleanSetting(false);
/*     */ 
/*     */   
/*  32 */   private static Minecraft mc = Minecraft.func_71410_x();
/*  33 */   private static List<Integer> boners = new ArrayList<>();
/*  34 */   private static TimeHelper throwTimer = new TimeHelper();
/*  35 */   private static int throwSlot = -1;
/*     */   private static boolean first = true;
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  40 */     boners.clear();
/*  41 */     throwSlot = -1;
/*  42 */     first = true;
/*  43 */     throwTimer.reset();
/*  44 */     if (!this.invMode.isEnabled()) {
/*  45 */       for (int i = 0; i < 8; i++) {
/*  46 */         ItemStack a = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*  47 */         if (a != null && a.func_82833_r().contains("Bonemerang")) {
/*  48 */           boners.add(Integer.valueOf(i));
/*     */         }
/*     */       } 
/*     */     } else {
/*  52 */       throwSlot = InventoryUtils.getAvailableHotbarSlot("Bonemerang");
/*     */       
/*  54 */       boners = InventoryUtils.getAllSlots(throwSlot, "Bonemerang");
/*  55 */       if (throwSlot == -1 || boners.isEmpty()) {
/*  56 */         CF4M.INSTANCE.moduleManager.toggle(this);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRenderTick(Render3DEvent event) {
/*  63 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*     */       return; 
/*  65 */     if (throwTimer.hasReached(this.throwDelay.getCurrent().intValue())) {
/*  66 */       int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */       
/*  68 */       if (!this.invMode.isEnabled()) {
/*  69 */         if (!boners.isEmpty()) {
/*  70 */           InventoryUtils.throwSlot(((Integer)boners.get(0)).intValue());
/*  71 */           boners.remove(0);
/*     */         }
/*     */       
/*  74 */       } else if (first) {
/*  75 */         InventoryUtils.throwSlot(throwSlot);
/*  76 */         first = false;
/*     */       } else {
/*  78 */         if (!boners.isEmpty()) {
/*  79 */           mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71069_bz.field_75152_c, ((Integer)boners.get(0)).intValue(), throwSlot, 2, (EntityPlayer)mc.field_71439_g);
/*  80 */           boners.remove(0);
/*     */         } 
/*  82 */         InventoryUtils.throwSlot(throwSlot);
/*     */       } 
/*     */ 
/*     */       
/*  86 */       if (this.silentUse.isEnabled()) {
/*  87 */         if (this.mainSlot.getCurrent().intValue() > 0 && this.mainSlot.getCurrent().intValue() <= 8) {
/*  88 */           mc.field_71439_g.field_71071_by.field_70461_c = this.mainSlot.getCurrent().intValue() - 1;
/*     */         } else {
/*  90 */           mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
/*     */         } 
/*  92 */       } else if (boners.isEmpty() && 
/*  93 */         this.mainSlot.getCurrent().intValue() > 0 && this.mainSlot.getCurrent().intValue() <= 8) {
/*  94 */         mc.field_71439_g.field_71071_by.field_70461_c = this.mainSlot.getCurrent().intValue() - 1;
/*     */       } 
/*     */ 
/*     */       
/*  98 */       if (boners.isEmpty())
/*  99 */         CF4M.INSTANCE.moduleManager.toggle(this); 
/* 100 */       throwTimer.reset();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\BonerThrower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */