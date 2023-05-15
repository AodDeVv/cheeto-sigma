/*    */ package xyz.apfelmus.cheeto.client.clickgui.components;
/*    */ 
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*    */ 
/*    */ public class CategoryBoxComponent
/*    */   extends VBox {
/*    */   public CategoryBoxComponent(int maxHeight) {
/*  8 */     this.maxHeight = maxHeight;
/*  9 */     this.spacing = 10;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {
/* 14 */     Render2DUtils.drawLine(this.x + this.width - 2, this.y, this.x + this.width - 2, this.y + this.maxHeight, 1, -16777216);
/* 15 */     Render2DUtils.drawLine(this.x + this.width - 1, this.y, this.x + this.width - 1, this.y + this.maxHeight, 1, -13619152);
/*    */     
/* 17 */     super.draw(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\components\CategoryBoxComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */