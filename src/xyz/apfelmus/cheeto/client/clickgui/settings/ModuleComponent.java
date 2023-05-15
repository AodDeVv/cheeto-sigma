/*    */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*    */ import xyz.apfelmus.cheeto.client.clickgui.components.Box;
/*    */ import xyz.apfelmus.cheeto.client.clickgui.components.VBox;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ public class ModuleComponent
/*    */   extends Box
/*    */ {
/*    */   private Object module;
/*    */   private VBox settings;
/*    */   
/*    */   public ModuleComponent(Object module) {
/* 19 */     this.module = module;
/* 20 */     this.width = 197;
/*    */     
/* 22 */     this.settings = new VBox();
/* 23 */     add(new UIComponent[] { (UIComponent)this.settings });
/* 24 */     this.settings.addX(8);
/* 25 */     this.settings.addY(20);
/* 26 */     this.settings.setMaxHeight(500);
/* 27 */     this.settings.add(new UIComponent[] { (UIComponent)new EnableComponent(this.module, this.width - 16) });
/*    */     
/* 29 */     List<Object> settings = CF4M.INSTANCE.settingManager.getSettings(this.module);
/* 30 */     if (settings != null) {
/* 31 */       for (Object setting : new ArrayList(settings)) {
/* 32 */         if (setting instanceof xyz.apfelmus.cheeto.client.settings.BooleanSetting) {
/* 33 */           this.settings.add(new UIComponent[] { new CheckBoxComponent(this.module, setting) }); continue;
/* 34 */         }  if (setting instanceof xyz.apfelmus.cheeto.client.settings.IntegerSetting || setting instanceof xyz.apfelmus.cheeto.client.settings.FloatSetting) {
/* 35 */           this.settings.add(new UIComponent[] { new SliderComponent(this.module, setting, this.width - 16) }); continue;
/* 36 */         }  if (setting instanceof xyz.apfelmus.cheeto.client.settings.ModeSetting) {
/* 37 */           this.settings.add(new UIComponent[] { new DropdownComponent(this.module, setting, this.width - 16) }); continue;
/* 38 */         }  if (setting instanceof xyz.apfelmus.cheeto.client.settings.StringSetting) {
/* 39 */           this.settings.add(new UIComponent[] { new TextComponent(this.module, setting, this.width - 16) });
/*    */         }
/*    */       } 
/*    */     }
/*    */     
/* 44 */     this.width = this.settings.width + 16;
/* 45 */     this.height = this.settings.height + 24;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {
/* 50 */     String moduleName = CF4M.INSTANCE.moduleManager.getName(this.module);
/* 51 */     Render2DUtils.drawBorderWH(this.x - 2, this.y - 2, this.width + 4, this.height + 4, 1, -16119286);
/* 52 */     Render2DUtils.drawBorderWH(this.x - 1, this.y - 1, this.width + 2, this.height + 2, 1, -13619152);
/* 53 */     Render2DUtils.drawRectWH(this.x, this.y, this.width, this.height, -15198184);
/* 54 */     int stringWidth = FontUtils.normal.getStringWidth(moduleName);
/* 55 */     Render2DUtils.drawRectWH(this.x + 8, this.y - 2, stringWidth + 12, 2, -15658735);
/*    */     
/* 57 */     FontUtils.normal.drawVCenteredString(moduleName, this.x + 16, this.y + FontUtils.normal.getFontHeight() / 5, -1);
/*    */ 
/*    */     
/* 60 */     super.draw(mouseX, mouseY, partialTicks);
/*    */     
/* 62 */     String desc = CF4M.INSTANCE.moduleManager.getDescription(this.module);
/* 63 */     if (desc.isEmpty())
/* 64 */       return;  boolean hov = Render2DUtils.isHovered(this.x + 16, this.y - FontUtils.normal.getFontHeight() / 3 + ConfigGUI.INSTANCE.getCurrentScroll() + 1, FontUtils.normal.getStringWidth(moduleName), FontUtils.normal.getFontHeight());
/* 65 */     if (hov)
/* 66 */       Render2DUtils.drawDescription(desc); 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\ModuleComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */