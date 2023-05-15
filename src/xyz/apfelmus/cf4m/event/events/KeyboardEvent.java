/*    */ package xyz.apfelmus.cf4m.event.events;
/*    */ 
/*    */ import xyz.apfelmus.cf4m.event.Listener;
/*    */ 
/*    */ public class KeyboardEvent extends Listener {
/*    */   private final int key;
/*    */   
/*    */   public KeyboardEvent(int key) {
/*  9 */     super(Listener.At.NONE);
/* 10 */     this.key = key;
/*    */   }
/*    */   
/*    */   public int getKey() {
/* 14 */     return this.key;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\event\events\KeyboardEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */