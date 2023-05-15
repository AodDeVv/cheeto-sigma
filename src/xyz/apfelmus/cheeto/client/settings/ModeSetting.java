/*    */ package xyz.apfelmus.cheeto.client.settings;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class ModeSetting {
/*    */   private String current;
/*    */   private final List<String> modes;
/*    */   
/*    */   public ModeSetting(String current, List<String> modes) {
/* 10 */     this.current = current;
/* 11 */     this.modes = modes;
/*    */   }
/*    */   
/*    */   public String getCurrent() {
/* 15 */     return this.current;
/*    */   }
/*    */   
/*    */   public void setCurrent(String current) {
/* 19 */     this.current = current;
/*    */   }
/*    */   
/*    */   public List<String> getModes() {
/* 23 */     return this.modes;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\settings\ModeSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */