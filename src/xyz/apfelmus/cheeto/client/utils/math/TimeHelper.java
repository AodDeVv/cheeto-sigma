/*    */ package xyz.apfelmus.cheeto.client.utils.math;
/*    */ 
/*    */ public class TimeHelper {
/*  4 */   private long lastMS = 0L;
/*    */   
/*    */   public TimeHelper() {
/*  7 */     reset();
/*    */   }
/*    */   
/*    */   public int convertToMS(int d) {
/* 11 */     return 1000 / d;
/*    */   }
/*    */   
/*    */   public long getCurrentMS() {
/* 15 */     return System.nanoTime() / 1000000L;
/*    */   }
/*    */   
/*    */   public boolean hasReached(long milliseconds) {
/* 19 */     return (getCurrentMS() - this.lastMS >= milliseconds);
/*    */   }
/*    */   
/*    */   public boolean hasTimeReached(long delay) {
/* 23 */     return (System.currentTimeMillis() - this.lastMS >= delay);
/*    */   }
/*    */   
/*    */   public long getDelay() {
/* 27 */     return System.currentTimeMillis() - this.lastMS;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 31 */     this.lastMS = getCurrentMS();
/*    */   }
/*    */   
/*    */   public void setLastMS() {
/* 35 */     this.lastMS = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public void setLastMS(long lastMS) {
/* 39 */     this.lastMS = lastMS;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\math\TimeHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */