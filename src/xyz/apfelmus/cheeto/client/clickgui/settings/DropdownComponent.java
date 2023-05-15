/*     */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*     */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*     */ 
/*     */ public class DropdownComponent
/*     */   extends UIComponent {
/*     */   private Object setting;
/*     */   private String name;
/*     */   private String description;
/*     */   private boolean hovered;
/*     */   public boolean expanded;
/*     */   private String currentValue;
/*     */   private List<String> modes;
/*     */   private boolean normal = false;
/*  21 */   private int largest = 0;
/*     */   
/*     */   public DropdownComponent(Object module, Object setting, int width) {
/*  24 */     this.setting = setting;
/*  25 */     this.name = CF4M.INSTANCE.settingManager.getName(module, setting);
/*  26 */     this.width = width;
/*  27 */     this.height = (int)(FontUtils.normal.getFontHeight() * 2.25D);
/*  28 */     this.currentValue = ((ModeSetting)setting).getCurrent();
/*  29 */     this.modes = ((ModeSetting)setting).getModes();
/*  30 */     this.largest = Math.max(((Integer)this.modes.stream().map(FontUtils.normal::getStringWidth).max(Comparator.naturalOrder()).orElse(Integer.valueOf(this.width - 20))).intValue() + 20, this.width);
/*     */   }
/*     */   
/*     */   public DropdownComponent(List<String> modes, String current, int width) {
/*  34 */     this.name = "Config:";
/*  35 */     this.width = width;
/*  36 */     this.height = (int)(FontUtils.normal.getFontHeight() * 2.25D);
/*  37 */     this.modes = modes;
/*  38 */     this.currentValue = current;
/*  39 */     this.largest = Math.max(((Integer)modes.stream().map(FontUtils.normal::getStringWidth).max(Comparator.naturalOrder()).orElse(Integer.valueOf(this.width - 20))).intValue() + 20, this.width);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(int mouseX, int mouseY, float partialTicks) {
/*  44 */     this.hovered = Render2DUtils.isHovered(this.x, this.y + FontUtils.normal.getFontHeight() + ConfigGUI.INSTANCE.getCurrentScroll(), this.width, (this.expanded ? this.modes.size() : 1) * FontUtils.normal.getFontHeight());
/*  45 */     FontUtils.normal.drawString(this.name, this.x + 5, this.y, -1);
/*     */ 
/*     */     
/*  48 */     if (!this.expanded) {
/*  49 */       Render2DUtils.drawBorderWH(this.x - 1, this.y + FontUtils.normal.getFontHeight() - 1, this.width + 2, FontUtils.normal.getFontHeight() + 2, 1, -15592942);
/*  50 */       Render2DUtils.drawGradientRectWH(this.x, this.y + FontUtils.normal.getFontHeight(), this.width, FontUtils.normal.getFontHeight(), -13619152, -14540254);
/*     */       
/*  52 */       int x = this.x + this.width - 12;
/*  53 */       int y = (int)(this.y + FontUtils.normal.getFontHeight() * 1.55D);
/*  54 */       Render2DUtils.drawTriangle(x - 5, y - 5, x, y - 1, x + 5, y - 5, -15592942);
/*  55 */       Render2DUtils.drawTriangle(x - 5, y - 4, x, y, x + 5, y - 4, this.hovered ? -2236963 : -6974059);
/*     */       
/*  57 */       int fw = FontUtils.normal.getStringWidth(this.currentValue) + 15;
/*  58 */       int width = this.width;
/*     */       
/*  60 */       String text = "";
/*     */       
/*  62 */       if (fw <= width) {
/*  63 */         text = this.currentValue;
/*     */       } else {
/*  65 */         text = FontUtils.truncateString(this.currentValue, width - 15, FontUtils.normal);
/*     */       } 
/*     */       
/*  68 */       FontUtils.normal.drawString(text, this.x + 10, this.y + FontUtils.normal.getFontHeight() + 1, this.hovered ? -2236963 : -6974059);
/*     */     } else {
/*  70 */       int i = FontUtils.normal.getFontHeight() * this.modes.size();
/*     */       
/*  72 */       Render2DUtils.drawBorderWH(this.x - 1, this.y + FontUtils.normal.getFontHeight() - 1, this.largest + 2, i + 2, 1, -15592942);
/*  73 */       Render2DUtils.drawGradientRectWH(this.x, this.y + FontUtils.normal.getFontHeight(), this.largest, i, -13619152, -14540254);
/*  74 */       for (int j = 0; j < this.modes.size(); j++) {
/*  75 */         String mode = this.modes.get(j);
/*  76 */         int y = this.y + FontUtils.normal.getFontHeight() + 1 + j * FontUtils.normal.getFontHeight();
/*  77 */         FontUtils.normal.drawString(mode, this.x + 10, y, Render2DUtils.isHovered(this.x, y - 1 + ConfigGUI.INSTANCE.getCurrentScroll(), this.width, FontUtils.normal.getFontHeight()) ? -2236963 : (this.currentValue.equals(mode) ? -1 : -6974059));
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     boolean hov = Render2DUtils.isHovered(this.x + 5, this.y + ConfigGUI.INSTANCE.getCurrentScroll(), FontUtils.normal.getStringWidth(this.name), FontUtils.normal.getFontHeight());
/*  82 */     if (hov) {
/*  83 */       Render2DUtils.drawDescription(this.description);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  89 */     if (this.hovered && mouseButton == 0) {
/*  90 */       if (!this.expanded) {
/*  91 */         this.expanded = true;
/*  92 */         ConfigGUI.INSTANCE.currentDropdown = this;
/*     */       } else {
/*  94 */         for (int j = 0; j < this.modes.size(); j++) {
/*  95 */           String mode = this.modes.get(j);
/*  96 */           int y = this.y + FontUtils.normal.getFontHeight() + 1 + j * FontUtils.normal.getFontHeight();
/*  97 */           if (Render2DUtils.isHovered(this.x, y - 1 + ConfigGUI.INSTANCE.getCurrentScroll(), this.width, FontUtils.normal.getFontHeight())) {
/*  98 */             if (this.setting != null) ((ModeSetting)this.setting).setCurrent(mode); 
/*  99 */             this.currentValue = mode;
/* 100 */             this.expanded = false;
/* 101 */             ConfigGUI.INSTANCE.currentDropdown = null;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 106 */     } else if (this.expanded && mouseButton == 0) {
/* 107 */       this.expanded = false;
/* 108 */       ConfigGUI.INSTANCE.currentDropdown = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getCurrentValue() {
/* 113 */     return this.currentValue;
/*     */   }
/*     */   
/*     */   public void setCurrentValue(String currentValue) {
/* 117 */     this.currentValue = currentValue;
/*     */   }
/*     */   
/*     */   public void addOption(String option) {
/* 121 */     this.modes.add(option);
/* 122 */     this.largest = Math.max(FontUtils.normal.getStringWidth(option) + 20, this.width);
/*     */   }
/*     */   
/*     */   public void updateOptions(List<String> options) {
/* 126 */     this.modes = options;
/* 127 */     this.largest = Math.max(((Integer)options.stream().map(FontUtils.normal::getStringWidth).max(Comparator.naturalOrder()).orElse(Integer.valueOf(this.width - 20))).intValue() + 20, this.width);
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\DropdownComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */