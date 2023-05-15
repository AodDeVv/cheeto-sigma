/*    */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*    */ 
/*    */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ public class ButtonComponent extends UIComponent {
/*    */   private String name;
/*    */   private boolean hovered;
/*    */   private Runnable callback;
/*    */   
/*    */   public ButtonComponent(String name, int width, Runnable callback) {
/* 13 */     this.name = name;
/* 14 */     this.width = width;
/* 15 */     this.height = FontUtils.normal.getFontHeight();
/* 16 */     this.callback = callback;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {
/* 21 */     this.hovered = Render2DUtils.isHovered(this.x, this.y + ConfigGUI.INSTANCE.getCurrentScroll(), this.width, this.height);
/*    */     
/* 23 */     Render2DUtils.drawBorderWH(this.x - 1, this.y - 1, this.width + 2, this.height + 2, 1, -15592942);
/* 24 */     Render2DUtils.drawGradientRectWH(this.x, this.y, this.width, this.height, this.hovered ? -11579569 : -13619152, this.hovered ? -12369085 : -14540254);
/*    */     
/* 26 */     FontUtils.normal.drawHVCenteredString(this.name, this.x + this.width / 2, this.y + this.height / 2 + 2, -6974059);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 31 */     if (this.hovered && mouseButton == 0)
/* 32 */       this.callback.run(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\ButtonComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */