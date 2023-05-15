/*     */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*     */ import xyz.apfelmus.cheeto.client.settings.StringSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*     */ 
/*     */ public class TextComponent
/*     */   extends UIComponent {
/*     */   private Object setting;
/*     */   private String name;
/*     */   private String description;
/*     */   private boolean hovered;
/*     */   public boolean selected;
/*     */   private String currentValue;
/*     */   private Runnable onKeyTyped;
/*     */   
/*     */   public TextComponent(Object module, Object setting, int width) {
/*  22 */     this.setting = setting;
/*  23 */     this.name = CF4M.INSTANCE.settingManager.getName(module, setting);
/*  24 */     this.width = width;
/*  25 */     this.height = (int)(FontUtils.normal.getFontHeight() * 2.2D);
/*  26 */     this.currentValue = ((StringSetting)setting).getCurrent();
/*     */   }
/*     */   
/*     */   public TextComponent(String name, String initialValue, int width) {
/*  30 */     this.name = name;
/*  31 */     this.width = width;
/*  32 */     this.height = (int)(FontUtils.normal.getFontHeight() * 2.2D);
/*  33 */     this.currentValue = initialValue;
/*     */   }
/*     */   
/*     */   public TextComponent(String name, String initialValue, int width, Runnable onKeyTyped) {
/*  37 */     this.name = name;
/*  38 */     this.width = width;
/*  39 */     this.height = (int)(FontUtils.normal.getFontHeight() * 2.2D);
/*  40 */     this.currentValue = initialValue;
/*  41 */     this.onKeyTyped = onKeyTyped;
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(int mouseX, int mouseY, float partialTicks) {
/*  46 */     this.hovered = Render2DUtils.isHovered(this.x, this.y + FontUtils.normal.getFontHeight() + ConfigGUI.INSTANCE.getCurrentScroll(), this.width, FontUtils.normal.getFontHeight());
/*  47 */     FontUtils.normal.drawString(this.name, this.x + 5, this.y, -1);
/*     */     
/*  49 */     int fw = FontUtils.normal.getStringWidth(this.currentValue) + 15;
/*  50 */     int width = this.width;
/*     */     
/*  52 */     if (this.selected) width = Math.max(width, fw);
/*     */     
/*  54 */     Render2DUtils.drawBorderWH(this.x - 1, this.y + FontUtils.normal.getFontHeight() - 1, width + 2, FontUtils.normal.getFontHeight() + 2, 1, -15592942);
/*  55 */     Render2DUtils.drawGradientRectWH(this.x, this.y + FontUtils.normal.getFontHeight(), width, FontUtils.normal.getFontHeight(), -13619152, -14540254);
/*     */     
/*  57 */     String text = "";
/*     */     
/*  59 */     if (this.selected) {
/*  60 */       text = this.currentValue;
/*     */     }
/*  62 */     else if (fw <= width) {
/*  63 */       text = this.currentValue;
/*     */     } else {
/*  65 */       text = FontUtils.truncateString(this.currentValue, width - 15, FontUtils.normal);
/*     */     } 
/*     */ 
/*     */     
/*  69 */     FontUtils.normal.drawString(text, this.x + 10, this.y + FontUtils.normal.getFontHeight() + 1, this.selected ? -2236963 : -6974059);
/*     */ 
/*     */     
/*  72 */     if (this.selected && System.currentTimeMillis() % 1250L < 750L) {
/*  73 */       int cursorX = this.x + 12 + FontUtils.normal.getStringWidth(this.currentValue);
/*  74 */       Render2DUtils.drawGradientRectWH(cursorX, this.y + FontUtils.normal.getFontHeight() + 4, 2, FontUtils.normal.getFontHeight() - 8, -2236963, -4473925);
/*     */     } 
/*     */     
/*  77 */     boolean hov = Render2DUtils.isHovered(this.x + 5, this.y + ConfigGUI.INSTANCE.getCurrentScroll(), FontUtils.normal.getStringWidth(this.name), FontUtils.normal.getFontHeight());
/*  78 */     if (hov) {
/*  79 */       Render2DUtils.drawDescription(this.description);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  85 */     if (this.hovered && mouseButton == 0) {
/*  86 */       this.selected = !this.selected;
/*  87 */     } else if (this.selected && mouseButton == 0) {
/*  88 */       this.selected = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void keyTyped(char typedChar, int keyCode) {
/*  94 */     if (this.selected) {
/*  95 */       if (GuiScreen.func_146271_m() && keyCode == 14) {
/*  96 */         this.currentValue = "";
/*  97 */       } else if (GuiScreen.func_175279_e(keyCode)) {
/*  98 */         this.currentValue = ChatAllowedCharacters.func_71565_a(this.currentValue + GuiScreen.func_146277_j());
/*  99 */       } else if (keyCode == 14) {
/* 100 */         if (this.currentValue.length() > 0) {
/* 101 */           this.currentValue = this.currentValue.substring(0, this.currentValue.length() - 1);
/*     */         }
/* 103 */       } else if (ChatAllowedCharacters.func_71566_a(typedChar)) {
/* 104 */         this.currentValue += typedChar;
/*     */       } 
/*     */       
/* 107 */       if (this.onKeyTyped != null) {
/* 108 */         this.onKeyTyped.run();
/*     */       }
/*     */     } 
/*     */     
/* 112 */     if (this.setting != null) ((StringSetting)this.setting).setCurrent(this.currentValue); 
/*     */   }
/*     */   
/*     */   public void setCurrentValue(String currentValue) {
/* 116 */     this.currentValue = currentValue;
/*     */   }
/*     */   
/*     */   public String getCurrentValue() {
/* 120 */     return this.currentValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\TextComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */