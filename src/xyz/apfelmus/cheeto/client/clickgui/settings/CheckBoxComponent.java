/*    */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*    */ 
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ public class CheckBoxComponent
/*    */   extends UIComponent {
/*    */   private Object module;
/*    */   private Object setting;
/*    */   private String name;
/*    */   private String description;
/*    */   private boolean hovered;
/*    */   
/*    */   public CheckBoxComponent(Object module, Object setting) {
/* 18 */     this.module = module;
/* 19 */     this.setting = setting;
/* 20 */     this.name = (setting == null) ? "Enable" : CF4M.INSTANCE.settingManager.getName(module, setting);
/* 21 */     this.description = (setting == null) ? "Enable/Disable the module" : CF4M.INSTANCE.settingManager.getDescription(module, setting);
/* 22 */     this.width = 18 + FontUtils.normal.getStringWidth(this.name);
/* 23 */     this.height = FontUtils.normal.getFontHeight();
/*    */   }
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {
/*    */     boolean enabled;
/* 28 */     int boxSize = 15;
/* 29 */     int offset = (this.height - boxSize) / 2;
/* 30 */     this.hovered = Render2DUtils.isHovered(this.x, this.y + offset + ConfigGUI.INSTANCE.getCurrentScroll(), boxSize, boxSize);
/*    */ 
/*    */     
/* 33 */     if (this.setting == null) {
/* 34 */       enabled = CF4M.INSTANCE.moduleManager.isEnabled(this.module);
/*    */     } else {
/* 36 */       enabled = ((BooleanSetting)this.setting).isEnabled();
/*    */     } 
/*    */     
/* 39 */     Render2DUtils.drawBorderWH(this.x, this.y + offset, 15, 15, 1, -15592942);
/* 40 */     if (this.hovered) {
/* 41 */       Render2DUtils.drawGradientRectWH(this.x + 1, this.y + offset + 1, 13, 13, enabled ? -7210667 : -9342607, enabled ? -9584591 : -10592674);
/*    */     } else {
/* 43 */       Render2DUtils.drawGradientRectWH(this.x + 1, this.y + offset + 1, 13, 13, enabled ? -8980437 : -11842741, enabled ? -10970584 : -13092808);
/*    */     } 
/*    */     
/* 46 */     FontUtils.normal.drawString(this.name, this.x + 20, this.y + 1, -1);
/*    */     
/* 48 */     boolean hov = Render2DUtils.isHovered(this.x + 20, this.y + ConfigGUI.INSTANCE.getCurrentScroll() + 1, FontUtils.normal.getStringWidth(this.name), FontUtils.normal.getFontHeight());
/* 49 */     if (hov) {
/* 50 */       Render2DUtils.drawDescription(this.description);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 56 */     if (this.hovered && mouseButton == 0)
/* 57 */       if (this.setting == null) {
/* 58 */         CF4M.INSTANCE.moduleManager.toggle(this.module);
/*    */       } else {
/* 60 */         ((BooleanSetting)this.setting).setState(!((BooleanSetting)this.setting).isEnabled());
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\CheckBoxComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */