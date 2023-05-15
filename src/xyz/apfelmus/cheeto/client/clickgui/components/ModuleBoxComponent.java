/*    */ package xyz.apfelmus.cheeto.client.clickgui.components;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*    */ 
/*    */ public class ModuleBoxComponent extends Box {
/*  7 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   
/*    */   public ModuleBoxComponent(int width, int height) {
/* 10 */     this.width = width;
/* 11 */     this.height = height;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {
/* 16 */     Render2DUtils.drawPatternRectWH(this.x, this.y, this.width, this.height, -14935011, -15461356);
/*    */     
/* 18 */     super.draw(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\components\ModuleBoxComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */