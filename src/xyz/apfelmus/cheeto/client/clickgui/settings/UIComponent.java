/*    */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*    */ 
/*    */ 
/*    */ public abstract class UIComponent
/*    */ {
/*    */   protected int x;
/*    */   protected int y;
/*    */   protected int width;
/*    */   protected int height;
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {}
/*    */   
/*    */   public int getX() {
/* 14 */     return this.x;
/*    */   }
/*    */   
/*    */   public int getY() {
/* 18 */     return this.y;
/*    */   }
/*    */   
/*    */   public void setX(int x) {
/* 22 */     this.x = x;
/*    */   }
/*    */   
/*    */   public void setY(int y) {
/* 26 */     this.y = y;
/*    */   }
/*    */   
/*    */   public void addX(int x) {
/* 30 */     this.x += x;
/*    */   }
/*    */   
/*    */   public void addY(int y) {
/* 34 */     this.y += y;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 38 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 42 */     return this.height;
/*    */   }
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}
/*    */   
/*    */   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {}
/*    */   
/*    */   public void keyTyped(char typedChar, int keyCode) {}
/*    */   
/*    */   public void mouseReleased(int mouseX, int mouseY, int state) {}
/*    */   
/*    */   public void mouseScrolled(int delta) {}
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\UIComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */