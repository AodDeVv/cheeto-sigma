/*    */ package xyz.apfelmus.cf4m.module;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ 
/*    */ public class ValueBean {
/*    */   private final String name;
/*    */   private final Field field;
/*    */   private final Object object;
/*    */   
/*    */   public ValueBean(String name, Field field, Object object) {
/* 11 */     this.name = name;
/* 12 */     this.field = field;
/* 13 */     this.object = object;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 17 */     return this.name;
/*    */   }
/*    */   
/*    */   public Field getField() {
/* 21 */     return this.field;
/*    */   }
/*    */   
/*    */   public Object getObject() {
/* 25 */     return this.object;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\module\ValueBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */