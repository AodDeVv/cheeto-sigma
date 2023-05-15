/*    */ package xyz.apfelmus.cf4m.manager;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.event.EventBean;
/*    */ import xyz.apfelmus.cf4m.event.Listener;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventManager
/*    */ {
/* 18 */   private final LinkedHashMap<Class<? extends Listener>, CopyOnWriteArrayList<EventBean>> events = Maps.newLinkedHashMap();
/*    */ 
/*    */   
/*    */   public void register(Object o) {
/* 22 */     Class<?> type = o.getClass();
/*    */     
/* 24 */     for (Method method : type.getDeclaredMethods()) {
/* 25 */       if ((method.getParameterTypes()).length == 1 && method.isAnnotationPresent((Class)Event.class)) {
/* 26 */         method.setAccessible(true);
/*    */         
/* 28 */         Class<? extends Listener> listener = (Class)method.getParameterTypes()[0];
/* 29 */         EventBean eventBean = new EventBean(o, method, ((Event)method.<Event>getAnnotation(Event.class)).priority());
/*    */         
/* 31 */         if (this.events.containsKey(listener)) {
/* 32 */           if (!((CopyOnWriteArrayList)this.events.get(listener)).contains(eventBean)) {
/* 33 */             ((CopyOnWriteArrayList<EventBean>)this.events.get(listener)).add(eventBean);
/*    */           }
/*    */         } else {
/* 36 */           this.events.put(listener, new CopyOnWriteArrayList<>(Collections.singletonList(eventBean)));
/*    */         } 
/*    */         
/* 39 */         this.events.values().forEach(methodBeans -> methodBeans.sort(Comparator.comparingInt(EventBean::getPriority)));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void unregister(Object o) {
/* 45 */     this.events.values().forEach(methodBeans -> methodBeans.removeIf(()));
/* 46 */     this.events.entrySet().removeIf(event -> ((CopyOnWriteArrayList)event.getValue()).isEmpty());
/*    */   }
/*    */   
/*    */   public CopyOnWriteArrayList<EventBean> getEvent(Class<? extends Listener> type) {
/* 50 */     return this.events.get(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\manager\EventManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */