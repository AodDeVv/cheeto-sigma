/*    */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*    */ 
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ public class KeybindComponent extends UIComponent {
/*    */   private Object module;
/* 11 */   private String currentKey = "-";
/*    */   private boolean hovered;
/*    */   private boolean changing;
/*    */   
/*    */   public KeybindComponent(Object module) {
/* 16 */     this.module = module;
/* 17 */     this.height = FontUtils.normal.getFontHeight();
/*    */     
/* 19 */     int key = CF4M.INSTANCE.moduleManager.getKey(this.module);
/* 20 */     if (key != 0) {
/* 21 */       this.currentKey = Keyboard.getKeyName(key);
/*    */     }
/*    */     
/* 24 */     updateWidth();
/*    */   }
/*    */   
/*    */   private void updateWidth() {
/* 28 */     int oldWidth = this.width;
/* 29 */     this.width = FontUtils.normal.getStringWidth(String.format("[%s]", new Object[] { this.currentKey }));
/* 30 */     this.x += oldWidth - this.width;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(int mouseX, int mouseY, float partialTicks) {
/* 35 */     this.hovered = Render2DUtils.isHovered(this.x, this.y + ConfigGUI.INSTANCE.getCurrentScroll(), this.width, this.height);
/*    */     
/* 37 */     FontUtils.normal.drawString(String.format("[%s]", new Object[] { this.currentKey }), this.x, this.y, this.hovered ? -9276814 : -11776948);
/*    */   }
/*    */ 
/*    */   
/*    */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 42 */     if (this.hovered && mouseButton == 0) {
/* 43 */       this.changing = !this.changing;
/* 44 */       this.currentKey = "Press";
/* 45 */       updateWidth();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void keyTyped(char typedChar, int keyCode) {
/* 51 */     if (this.changing) {
/* 52 */       if (keyCode == 1 || keyCode == 14) {
/* 53 */         this.currentKey = "-";
/* 54 */         CF4M.INSTANCE.moduleManager.setKey(this.module, 0);
/*    */       } else {
/* 56 */         this.currentKey = Keyboard.getKeyName(keyCode);
/* 57 */         CF4M.INSTANCE.moduleManager.setKey(this.module, keyCode);
/*    */       } 
/* 59 */       updateWidth();
/* 60 */       this.changing = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getCurrentKey() {
/* 65 */     return this.currentKey;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\KeybindComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */