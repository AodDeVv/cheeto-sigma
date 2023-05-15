/*     */ package xyz.apfelmus.cheeto.client.modules.render;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cf4m.module.Category;
/*     */ import xyz.apfelmus.cheeto.Cheeto;
/*     */ import xyz.apfelmus.cheeto.client.events.Render2DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.shader.ChromaShader;
/*     */ 
/*     */ 
/*     */ @Module(name = "HUD", category = Category.RENDER)
/*     */ public class HUD
/*     */ {
/*     */   @Setting(name = "Watermark")
/*  27 */   private BooleanSetting watermark = new BooleanSetting(true);
/*     */   @Setting(name = "ModuleList")
/*  29 */   private BooleanSetting moduleList = new BooleanSetting(true);
/*     */   @Setting(name = "Logo")
/*  31 */   private ModeSetting logo = new ModeSetting("smirk_cat", 
/*  32 */       Arrays.asList(new String[] { "smirk_cat", "bing_chilling", "rat", "mouse" })); @Setting(name = "Chroma")
/*  33 */   private BooleanSetting chroma = new BooleanSetting(true);
/*     */   @Setting(name = "MoreChroma")
/*  35 */   private BooleanSetting moreChroma = new BooleanSetting(false);
/*     */ 
/*     */   
/*  38 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   @Event
/*     */   public void onRender(Render2DEvent event) {
/*  42 */     if (this.watermark.isEnabled()) renderTopString();
/*     */     
/*  44 */     if (!this.moduleList.isEnabled()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  49 */     List<Object> modules = (List<Object>)CF4M.INSTANCE.moduleManager.getModules().stream().filter(v -> CF4M.INSTANCE.moduleManager.isEnabled(v)).sorted((o1, o2) -> FontUtils.normalBold.getStringWidth(getModuleName(o2)) - FontUtils.normalBold.getStringWidth(getModuleName(o1))).collect(Collectors.toList());
/*     */     
/*  51 */     renderModuleList(modules);
/*     */   }
/*     */   
/*     */   private void renderTopString() {
/*  55 */     if (this.chroma.isEnabled()) {
/*  56 */       FontUtils.semiBigBold.drawChromaString(Cheeto.name, 12 + FontUtils.semiBigBold.getFontHeight(), 2);
/*     */     } else {
/*  58 */       FontUtils.semiBigBold.drawString(Cheeto.name, 12 + FontUtils.semiBigBold.getFontHeight(), 2, -403684);
/*     */     } 
/*  60 */     int nameLength = FontUtils.semiBigBold.getFontHeight() + FontUtils.semiBigBold.getStringWidth(Cheeto.name);
/*  61 */     if (this.chroma.isEnabled()) {
/*  62 */       FontUtils.normalBold.drawChromaString(" v" + Cheeto.version, nameLength + FontUtils.normalBold.getFontHeight() / 2, 2 + FontUtils.normalBold.getFontHeight() * 3 / 4);
/*     */     } else {
/*  64 */       FontUtils.normalBold.drawString(" v" + Cheeto.version, nameLength + FontUtils.normalBold.getFontHeight() / 2, 2 + FontUtils.normalBold.getFontHeight() * 3 / 4, -403684);
/*     */     } 
/*  66 */     Render2DUtils.drawTexture(new ResourceLocation("chromahud:" + this.logo.getCurrent() + ".png"), 2, 2, FontUtils.semiBigBold.getFontHeight(), FontUtils.semiBigBold.getFontHeight(), FontUtils.semiBigBold.getFontHeight(), FontUtils.semiBigBold.getFontHeight(), 0, 0);
/*     */   }
/*     */   
/*     */   private void renderModuleList(List<Object> modules) {
/*  70 */     int yStart = 0;
/*     */     
/*  72 */     for (Object module : modules) {
/*  73 */       int sw = FontUtils.normalBold.getStringWidth(getModuleName(module));
/*  74 */       int fh = FontUtils.normalBold.getFontHeight();
/*  75 */       int startX = mc.field_71443_c - sw - fh / 2;
/*     */       
/*  77 */       int animStartX = mc.field_71443_c;
/*  78 */       long start = CF4M.INSTANCE.moduleManager.getActivatedTime(module);
/*  79 */       float spentMillis = (float)(System.currentTimeMillis() - start);
/*  80 */       float relativeProgress = spentMillis / 500.0F;
/*  81 */       int relativeX = (int)((startX - animStartX) * RotationUtils.easeOutCubic(relativeProgress) + animStartX);
/*  82 */       relativeX = Math.max(startX, relativeX);
/*  83 */       int relativeHeight = (int)Math.ceil((fh * RotationUtils.easeOutCubic(relativeProgress)));
/*  84 */       relativeHeight = Math.min(relativeHeight, fh);
/*     */       
/*  86 */       Render2DUtils.drawRectWH(relativeX - 1, yStart, mc.field_71443_c - startX + 1, relativeHeight, -1778384896);
/*  87 */       if (this.chroma.isEnabled()) {
/*  88 */         ChromaShader.INSTANCE.startShader();
/*  89 */         Render2DUtils.drawLeftRoundedRect((relativeX - 1), yStart, (fh / 5), relativeHeight, (fh / 5), -1);
/*  90 */         ChromaShader.INSTANCE.stopShader();
/*     */       } else {
/*  92 */         Render2DUtils.drawLeftRoundedRect((relativeX - 1), yStart, (fh / 5), relativeHeight, (fh / 5), -403684);
/*     */       } 
/*     */       
/*  95 */       if (this.chroma.isEnabled() && this.moreChroma.isEnabled()) {
/*  96 */         FontUtils.normalBold.drawVCenteredChromaString(getModuleName(module), relativeX + fh / 4, 1 + yStart + fh / 2);
/*     */       } else {
/*  98 */         FontUtils.normalBold.drawVCenteredString(getModuleName(module), relativeX + fh / 4, 1 + yStart + fh / 2, -3618616);
/*     */       } 
/*     */       
/* 101 */       yStart += relativeHeight;
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getModuleName(Object module) {
/* 106 */     return CF4M.INSTANCE.moduleManager.getName(module);
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\render\HUD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */