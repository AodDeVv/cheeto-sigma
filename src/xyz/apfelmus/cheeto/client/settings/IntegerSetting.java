/*    */ package xyz.apfelmus.cheeto.client.settings;
/*    */ 
/*    */ public class IntegerSetting {
/*    */   private Integer current;
/*    */   private Integer min;
/*    */   private Integer max;
/*    */   
/*    */   public IntegerSetting(Integer current, Integer min, Integer max) {
/*  9 */     this.current = current;
/* 10 */     this.min = min;
/* 11 */     this.max = max;
/*    */   }
/*    */   
/*    */   public Integer getCurrent() {
/* 15 */     return this.current;
/*    */   }
/*    */   
/*    */   public void setCurrent(Integer current) {
/* 19 */     this.current = (current.intValue() < this.min.intValue()) ? this.min : ((current.intValue() > this.max.intValue()) ? this.max : current);
/*    */   }
/*    */   
/*    */   public Integer getMin() {
/* 23 */     return this.min;
/*    */   }
/*    */   
/*    */   public void setMin(Integer min) {
/* 27 */     this.min = min;
/*    */   }
/*    */   
/*    */   public Integer getMax() {
/* 31 */     return this.max;
/*    */   }
/*    */   
/*    */   public void setMax(Integer max) {
/* 35 */     this.max = max;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\settings\IntegerSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */