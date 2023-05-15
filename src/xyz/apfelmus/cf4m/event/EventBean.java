/*    */ package xyz.apfelmus.cf4m.event;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class EventBean {
/*    */   private final Object object;
/*    */   private final Method method;
/*    */   private final int priority;
/*    */   
/*    */   public EventBean(Object object, Method method, int priority) {
/* 11 */     this.object = object;
/* 12 */     this.method = method;
/* 13 */     this.priority = priority;
/*    */   }
/*    */   
/*    */   public Object getObject() {
/* 17 */     return this.object;
/*    */   }
/*    */   
/*    */   public Method getMethod() {
/* 21 */     return this.method;
/*    */   }
/*    */   
/*    */   public int getPriority() {
/* 25 */     return this.priority;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\event\EventBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */