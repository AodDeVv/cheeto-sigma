/*    */ package xyz.apfelmus.cheeto.client.settings;
/*    */ 
/*    */ public class BooleanSetting {
/*    */   private boolean enabled;
/*    */   
/*    */   public BooleanSetting(boolean enable) {
/*  7 */     this.enabled = enable;
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 11 */     return this.enabled;
/*    */   }
/*    */   
/*    */   public void setState(boolean enable) {
/* 15 */     this.enabled = enable;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\settings\BooleanSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */