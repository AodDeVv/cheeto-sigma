/*    */ package xyz.apfelmus.cheeto.client.modules.render;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Module(name = "ClickGUI", category = Category.RENDER, key = 157, silent = true)
/*    */ public class ClickGUI
/*    */ {
/*    */   @Enable
/*    */   public void onEnable() {
/* 20 */     Minecraft.func_71410_x().func_147108_a((GuiScreen)new ConfigGUI());
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\render\ClickGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */