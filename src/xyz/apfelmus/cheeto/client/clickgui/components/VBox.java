/*    */ package xyz.apfelmus.cheeto.client.clickgui.components;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import xyz.apfelmus.cheeto.client.clickgui.settings.UIComponent;
/*    */ 
/*    */ public class VBox
/*    */   extends Box
/*    */ {
/*    */   public int curX;
/*    */   public int curY;
/*    */   public int maxChildWidth;
/*    */   
/*    */   public VBox() {}
/*    */   
/*    */   public VBox(int spacing) {
/* 16 */     super(spacing);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(UIComponent... components) {
/* 21 */     for (UIComponent component : components) {
/* 22 */       if (this.curY != 0) {
/* 23 */         this.curY += this.spacing;
/*    */       }
/*    */       
/* 26 */       if (this.maxHeight != 0) {
/* 27 */         if (this.curY + component.getHeight() > this.maxHeight) {
/* 28 */           this.curX += this.maxChildWidth + this.spacing;
/* 29 */           this.maxChildWidth = 0;
/* 30 */           this.curY = 0;
/*    */         } 
/*    */         
/* 33 */         if (this.maxChildWidth < component.getWidth()) {
/* 34 */           this.maxChildWidth = component.getWidth();
/*    */         }
/*    */       } 
/*    */       
/* 38 */       if (this.width < this.curX + component.getWidth()) {
/* 39 */         this.width = this.curX + component.getWidth();
/*    */       }
/*    */       
/* 42 */       addToChild(this.curX, this.curY, component);
/* 43 */       if (this.curY + component.getHeight() > this.height) {
/* 44 */         this.height = this.curY + component.getHeight();
/* 45 */       } else if (this.height + component.getHeight() < this.maxHeight) {
/* 46 */         this.height += component.getHeight();
/*    */       } 
/* 48 */       this.curY += component.getHeight();
/*    */       
/* 50 */       updateChild(this, component);
/*    */     } 
/*    */     
/* 53 */     getChildren().addAll(Arrays.asList(components));
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\components\VBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */