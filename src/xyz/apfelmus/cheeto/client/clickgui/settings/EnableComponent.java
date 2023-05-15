/*    */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*    */ 
/*    */ import xyz.apfelmus.cheeto.client.clickgui.components.Box;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ public class EnableComponent extends Box {
/*    */   private Object module;
/*    */   
/*    */   public EnableComponent(Object module, int width) {
/* 10 */     this.module = module;
/* 11 */     this.width = width;
/* 12 */     this.height = FontUtils.normal.getFontHeight();
/*    */     
/* 14 */     add(new UIComponent[] { new CheckBoxComponent(module, null) });
/* 15 */     KeybindComponent kb = new KeybindComponent(module);
/* 16 */     add(new UIComponent[] { kb });
/* 17 */     kb.setX(this.x + this.width - FontUtils.normal.getStringWidth(String.format("[%s]", new Object[] { kb.getCurrentKey() })));
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\EnableComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */