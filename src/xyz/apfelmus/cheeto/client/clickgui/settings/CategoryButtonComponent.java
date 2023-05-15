/*    */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*    */ 
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ public class CategoryButtonComponent extends UIComponent {
/*    */   public Category category;
/*    */   public boolean selected;
/*    */   private boolean hovered;
/*    */   
/*    */   public CategoryButtonComponent(Category category) {
/* 14 */     this.category = category;
/* 15 */     this.height = 84;
/* 16 */     this.width = 74;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {
/* 21 */     this.hovered = Render2DUtils.isHovered(this.x, this.y, this.width, this.height);
/* 22 */     if (this.selected) {
/* 23 */       Render2DUtils.drawLine(this.x, this.y - 2, this.x + this.width - 1, this.y - 2, 1, -16777216);
/* 24 */       Render2DUtils.drawLine(this.x, this.y - 1, this.x + this.width, this.y - 1, 1, -13619152);
/* 25 */       Render2DUtils.drawLine(this.x, this.y + this.height, this.x + this.width, this.y + this.height, 1, -13619152);
/* 26 */       Render2DUtils.drawLine(this.x, this.y + this.height + 1, this.x + this.width - 1, this.y + this.height + 1, 1, -16777216);
/*    */       
/* 28 */       Render2DUtils.drawPatternRectWH(this.x + 1, this.y, this.width, this.height, -14935011, -15461356);
/*    */     } 
/* 30 */     if (this.hovered) {
/* 31 */       int width = FontUtils.big.getStringWidth(this.category.name().charAt(0) + this.category.name().substring(1).toLowerCase()) + this.width - FontUtils.big.getStringWidth(String.valueOf(this.category.name().charAt(0)));
/* 32 */       Render2DUtils.drawLine(this.x, this.y - 2, this.x + width + 2, this.y - 2, 1, -16777216);
/* 33 */       Render2DUtils.drawLine(this.x, this.y - 1, this.x + width + 1, this.y - 1, 1, -13619152);
/* 34 */       Render2DUtils.drawLine(this.x, this.y + this.height, this.x + width + 1, this.y + this.height, 1, -13619152);
/* 35 */       Render2DUtils.drawLine(this.x, this.y + this.height + 1, this.x + width + 2, this.y + this.height + 1, 1, -16777216);
/* 36 */       Render2DUtils.drawLine(this.x + width, this.y, this.x + width, this.y + this.height, 1, -13619152);
/* 37 */       Render2DUtils.drawLine(this.x + width + 1, this.y - 1, this.x + width + 1, this.y + this.height + 1, 1, -16777216);
/* 38 */       Render2DUtils.drawPatternRectWH(this.x + 1, this.y, width - 1, this.height, -14935011, -15461356);
/*    */     } 
/* 40 */     String firstChar = this.category.name().substring(0, 1);
/* 41 */     FontUtils.big.drawVCenteredString(this.hovered ? (firstChar + this.category.name().substring(1).toLowerCase()) : firstChar, this.x + this.width / 2 - FontUtils.big.getStringWidth(firstChar) / 2, this.y + this.height / 2, this.hovered ? -5855578 : (this.selected ? -3092272 : -10790053));
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 46 */     if (this.hovered && mouseButton == 0)
/* 47 */       ConfigGUI.INSTANCE.displayCategory(this.category); 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\CategoryButtonComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */