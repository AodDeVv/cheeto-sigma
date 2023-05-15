/*     */ package xyz.apfelmus.cheeto.client.modules.combat;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cf4m.module.Category;
/*     */ import xyz.apfelmus.cheeto.client.events.LeftClickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.KeybindUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.RandomUtil;
/*     */ 
/*     */ @Module(name = "AutoClicker", category = Category.COMBAT)
/*     */ public class AutoClicker
/*     */ {
/*     */   @Setting(name = "BurstMode", description = "Drag Click Mode. (Will Click the Burstamount once and disable afterwards)")
/*  26 */   private BooleanSetting burstMode = new BooleanSetting(false);
/*     */   @Setting(name = "BurstAmount", description = "Amount to Burst Click")
/*  28 */   private IntegerSetting burstAmount = new IntegerSetting(
/*  29 */       Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(25)); @Setting(name = "OnClick", description = "Will click while you hold Left or Right Click")
/*  30 */   private BooleanSetting onClick = new BooleanSetting(false);
/*     */   @Setting(name = "Mode", description = "Left or Right Click duh")
/*  32 */   private ModeSetting mode = new ModeSetting("Left", 
/*  33 */       Arrays.asList(new String[] { "Left", "Right" })); @Setting(name = "MinCPS", description = "Minimum CPS")
/*  34 */   private IntegerSetting minCps = new IntegerSetting(
/*  35 */       Integer.valueOf(7), Integer.valueOf(0), Integer.valueOf(30)); @Setting(name = "MaxCPS", description = "Maximum CPS")
/*  36 */   private IntegerSetting maxCps = new IntegerSetting(
/*  37 */       Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(30));
/*     */   
/*     */   private static boolean toggled = false;
/*     */   
/*     */   private static long lastClickTime;
/*     */   private static long clickDelay;
/*     */   private static int clickedAmount;
/*  44 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  48 */     if (toggled) toggled = false;
/*     */     
/*  50 */     if (this.minCps.getCurrent().intValue() > this.maxCps.getCurrent().intValue()) {
/*  51 */       ChatUtils.send("MinCPS can't be higher than MaxCPS retard", new String[0]);
/*  52 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */       
/*     */       return;
/*     */     } 
/*  56 */     if (this.burstMode.isEnabled() && this.onClick.isEnabled()) {
/*  57 */       ChatUtils.send("Can't have BurstMode and OnClick enabled together.", new String[0]);
/*  58 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */     } 
/*     */     
/*  61 */     clickDelay = RandomUtil.randBetween(this.minCps.getCurrent().intValue(), this.maxCps.getCurrent().intValue());
/*  62 */     clickedAmount = 0;
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onLeftClick(LeftClickEvent event) {
/*  67 */     event.ci.cancel();
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRenderTick(Render3DEvent event) {
/*  72 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*     */       return; 
/*  74 */     if (this.burstMode.isEnabled()) {
/*  75 */       if (toggled) {
/*  76 */         float acTime = 1000.0F / this.minCps.getCurrent().intValue();
/*  77 */         if (clickedAmount < this.burstAmount.getCurrent().intValue()) {
/*  78 */           if ((float)(System.currentTimeMillis() - lastClickTime) >= acTime) {
/*  79 */             KeybindUtils.rightClick();
/*  80 */             lastClickTime = System.currentTimeMillis();
/*  81 */             clickedAmount++;
/*     */           } 
/*     */         } else {
/*  84 */           toggled = false;
/*  85 */           CF4M.INSTANCE.moduleManager.toggle(this);
/*     */         } 
/*     */       } else {
/*  88 */         toggled = true;
/*     */       }
/*     */     
/*  91 */     } else if (this.onClick.isEnabled()) {
/*  92 */       if (System.currentTimeMillis() - lastClickTime >= clickDelay) {
/*  93 */         lastClickTime = System.currentTimeMillis();
/*  94 */         clickDelay = (long)(1000.0F / RandomUtil.randBetween(this.minCps.getCurrent().intValue(), this.maxCps.getCurrent().intValue()));
/*     */         
/*  96 */         if (Mouse.isButtonDown(0) && this.mode.getCurrent().equals("Left") && !mc.field_71439_g.func_71039_bw()) {
/*  97 */           KeybindUtils.leftClick();
/*  98 */         } else if (Mouse.isButtonDown(1) && this.mode.getCurrent().equals("Right")) {
/*  99 */           KeybindUtils.rightClick();
/*     */         }
/*     */       
/*     */       } 
/* 103 */     } else if (Keyboard.isKeyDown(CF4M.INSTANCE.moduleManager.getKey(this))) {
/* 104 */       if (System.currentTimeMillis() - lastClickTime >= clickDelay) {
/* 105 */         lastClickTime = System.currentTimeMillis();
/* 106 */         clickDelay = (long)(1000.0F / RandomUtil.randBetween(this.minCps.getCurrent().intValue(), this.maxCps.getCurrent().intValue()));
/*     */         
/* 108 */         if (this.mode.getCurrent().equals("Left") && !mc.field_71439_g.func_71039_bw()) {
/* 109 */           KeybindUtils.leftClick();
/* 110 */         } else if (this.mode.getCurrent().equals("Right")) {
/* 111 */           KeybindUtils.rightClick();
/*     */         } 
/*     */       } 
/*     */     } else {
/* 115 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\AutoClicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */