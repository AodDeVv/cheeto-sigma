/*    */ package xyz.apfelmus.cheeto.client.settings;
/*    */ 
/*    */ public class FloatSetting {
/*    */   private Float current;
/*    */   private Float min;
/*    */   private Float max;
/*    */   
/*    */   public FloatSetting(Float current, Float min, Float max) {
/*  9 */     this.current = current;
/* 10 */     this.min = min;
/* 11 */     this.max = max;
/*    */   }
/*    */   
/*    */   public Float getCurrent() {
/* 15 */     return this.current;
/*    */   }
/*    */   
/*    */   public void setCurrent(Float current) {
/* 19 */     this.current = (current.floatValue() < this.min.floatValue()) ? this.min : ((current.floatValue() > this.max.floatValue()) ? this.max : current);
/*    */   }
/*    */   
/*    */   public Float getMin() {
/* 23 */     return this.min;
/*    */   }
/*    */   
/*    */   public void setMin(Float min) {
/* 27 */     this.min = min;
/*    */   }
/*    */   
/*    */   public Float getMax() {
/* 31 */     return this.max;
/*    */   }
/*    */   
/*    */   public void setMax(Float max) {
/* 35 */     this.max = max;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\settings\FloatSetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */