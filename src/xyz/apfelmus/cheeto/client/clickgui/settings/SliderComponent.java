/*    */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*    */ 
/*    */ import org.lwjgl.input.Mouse;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*    */ import xyz.apfelmus.cheeto.client.settings.FloatSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ public class SliderComponent extends UIComponent {
/*    */   private Object setting;
/*    */   private String name;
/*    */   private String description;
/*    */   private boolean hovered;
/*    */   private boolean dragging;
/*    */   private double percentage;
/*    */   private double min;
/*    */   private double max;
/*    */   private double currentValue;
/*    */   
/*    */   public SliderComponent(Object module, Object setting, int width) {
/* 23 */     this.setting = setting;
/* 24 */     this.name = CF4M.INSTANCE.settingManager.getName(module, setting);
/* 25 */     this.width = width;
/* 26 */     this.height = (int)(FontUtils.normal.getFontHeight() * 1.75D);
/*    */     
/* 28 */     if (setting instanceof IntegerSetting) {
/* 29 */       IntegerSetting set = (IntegerSetting)setting;
/* 30 */       this.min = set.getMin().intValue();
/* 31 */       this.max = set.getMax().intValue();
/* 32 */       this.currentValue = set.getCurrent().intValue();
/* 33 */     } else if (setting instanceof FloatSetting) {
/* 34 */       FloatSetting set = (FloatSetting)setting;
/* 35 */       this.min = set.getMin().floatValue();
/* 36 */       this.max = set.getMax().floatValue();
/* 37 */       this.currentValue = set.getCurrent().floatValue();
/*    */     } 
/*    */     
/* 40 */     this.percentage = (this.currentValue - this.min) / (this.max - this.min);
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {
/* 45 */     this.hovered = Render2DUtils.isHovered(this.x, this.y + FontUtils.normal.getFontHeight() + ConfigGUI.INSTANCE.getCurrentScroll(), this.width, 12);
/* 46 */     FontUtils.normal.drawString(this.name, this.x + 5, this.y, -1);
/*    */     
/* 48 */     Render2DUtils.drawBorderWH(this.x, this.y + FontUtils.normal.getFontHeight(), this.width, 12, 1, -15592942);
/* 49 */     Render2DUtils.drawGradientRectWH(this.x + 1, this.y + FontUtils.normal.getFontHeight() + 1, this.width - 2, 10, -13619152, -14540254);
/*    */     
/* 51 */     if (this.dragging) {
/* 52 */       this.percentage = getCurrentPercentage();
/* 53 */       setCurrentValue(this.percentage);
/*    */     } 
/*    */     
/* 56 */     int sliderWidth = (int)((this.width - 2) * this.percentage);
/* 57 */     Render2DUtils.drawGradientRectWH(this.x + 1, this.y + FontUtils.normal.getFontHeight() + 1, sliderWidth, 10, -8980437, -10970584);
/* 58 */     if (this.setting instanceof FloatSetting) {
/* 59 */       FontUtils.small.drawHVCenteredString(String.format("%.1f", new Object[] { Double.valueOf(this.currentValue) }), this.x + sliderWidth, (int)(this.y + FontUtils.normal.getFontHeight() * 1.5D), -1);
/* 60 */     } else if (this.setting instanceof IntegerSetting) {
/* 61 */       FontUtils.small.drawHVCenteredString((int)this.currentValue + ((this.name.contains("Time") || this.name.contains("Delay")) ? "ms" : ""), this.x + sliderWidth, (int)(this.y + FontUtils.normal.getFontHeight() * 1.5D), -1);
/*    */     } 
/*    */     
/* 64 */     boolean hov = Render2DUtils.isHovered(this.x + 5, this.y + ConfigGUI.INSTANCE.getCurrentScroll(), FontUtils.normal.getStringWidth(this.name), FontUtils.normal.getFontHeight());
/* 65 */     if (hov) {
/* 66 */       Render2DUtils.drawDescription(this.description);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 72 */     if (this.hovered && mouseButton == 0) {
/* 73 */       this.dragging = true;
/* 74 */       this.percentage = getCurrentPercentage();
/* 75 */       setCurrentValue(this.percentage);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseReleased(int mouseX, int mouseY, int state) {
/* 81 */     this.dragging = false;
/*    */   }
/*    */   
/*    */   private float getCurrentPercentage() {
/* 85 */     int min = this.x;
/* 86 */     int max = this.x + this.width - 1;
/*    */     
/* 88 */     return Math.max(Math.min((Mouse.getX() - min) / (max - min), 1.0F), 0.0F);
/*    */   }
/*    */   
/*    */   private void setCurrentValue(double percentage) {
/* 92 */     this.currentValue = this.min + percentage * (this.max - this.min);
/*    */     
/* 94 */     if (this.setting instanceof IntegerSetting) {
/* 95 */       ((IntegerSetting)this.setting).setCurrent(Integer.valueOf((int)this.currentValue));
/* 96 */     } else if (this.setting instanceof FloatSetting) {
/* 97 */       ((FloatSetting)this.setting).setCurrent(Float.valueOf((float)this.currentValue));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\SliderComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */