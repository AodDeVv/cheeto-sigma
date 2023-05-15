/*    */ package xyz.apfelmus.cheeto.client.clickgui.components;
/*    */ 
/*    */ import xyz.apfelmus.cheeto.client.modules.world.SecretAura;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ public class MainBoxComponent extends Box {
/*    */   public MainBoxComponent(int width, int height) {
/*  9 */     this.width = width;
/* 10 */     this.height = height;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {
/* 15 */     Render2DUtils.drawBorderWH(this.x - 7, this.y - 7, this.width + 14, this.height + 14, 1, -15592942);
/* 16 */     Render2DUtils.drawBorderWH(this.x - 6, this.y - 6, this.width + 12, this.height + 12, 1, -12698050);
/* 17 */     Render2DUtils.drawRectWH(this.x - 5, this.y - 5, this.width + 10, this.height + 10, -13882324);
/* 18 */     Render2DUtils.drawBorderWH(this.x - 2, this.y - 2, this.width + 4, this.height + 4, 1, -12698050);
/* 19 */     Render2DUtils.drawRectWH(this.x - 1, this.y - 1, this.width + 2, this.height + 2, -15461356);
/*    */     
/* 21 */     Render2DUtils.drawChromaLine(this.x, this.y, this.width);
/*    */     
/* 23 */     super.draw(mouseX, mouseY, partialTicks);
/*    */     
/* 25 */     if (!SecretAura.tGT5uMvjKCoG5kZn()) {
/* 26 */       FontUtils.superbigBold.drawHVCenteredChromaString("  ", mc.field_71443_c / 2, mc.field_71440_d / 4);
/* 27 */       FontUtils.superbigBold.drawHVCenteredChromaString(" ", mc.field_71443_c / 2, mc.field_71440_d / 2);
/* 28 */       FontUtils.bigBold.drawHVCenteredChromaString("Cracked by AlphaComputer#9846 or [Skyblock] MagiDev", mc.field_71443_c / 2, mc.field_71440_d / 2 + mc.field_71440_d / 4);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\components\MainBoxComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */