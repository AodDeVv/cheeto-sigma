/*    */ package xyz.apfelmus.cf4m.event;
/*    */ 
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ 
/*    */ 
/*    */ public class Listener
/*    */ {
/*    */   private final At at;
/*    */   private boolean cancel;
/*    */   
/*    */   public Listener(At at) {
/* 13 */     this.at = at;
/* 14 */     this.cancel = false;
/*    */   }
/*    */   
/*    */   public void call() {
/* 18 */     this.cancel = false;
/*    */     
/* 20 */     if (CF4M.INSTANCE.eventManager == null)
/* 21 */       return;  CopyOnWriteArrayList<EventBean> eventBeans = CF4M.INSTANCE.eventManager.getEvent(getClass());
/*    */     
/* 23 */     if (eventBeans == null) {
/*    */       return;
/*    */     }
/*    */     
/* 27 */     for (EventBean event : eventBeans) {
/*    */       try {
/* 29 */         event.getMethod().invoke(event.getObject(), new Object[] { this });
/* 30 */       } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 31 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean getCancel() {
/* 37 */     return this.cancel;
/*    */   }
/*    */   
/*    */   public void setCancelled(boolean cancel) {
/* 41 */     this.cancel = cancel;
/*    */   }
/*    */   
/*    */   public At getAt() {
/* 45 */     return this.at;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public enum At
/*    */   {
/* 52 */     HEAD,
/* 53 */     TAIL,
/* 54 */     NONE;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\event\Listener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */