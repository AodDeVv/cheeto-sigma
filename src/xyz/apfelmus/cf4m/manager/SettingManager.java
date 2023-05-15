/*    */ package xyz.apfelmus.cf4m.manager;
/*    */ 
/*    */ import com.google.common.collect.LinkedHashMultimap;
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.ArrayList;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ 
/*    */ public class SettingManager
/*    */ {
/* 11 */   private final LinkedHashMultimap<Object, Field> settings = LinkedHashMultimap.create();
/*    */   
/*    */   public SettingManager() {
/* 14 */     for (Object module : CF4M.INSTANCE.moduleManager.getModules()) {
/* 15 */       for (Field field : module.getClass().getDeclaredFields()) {
/* 16 */         field.setAccessible(true);
/* 17 */         if (field.isAnnotationPresent((Class)Setting.class)) {
/* 18 */           this.settings.put(module, field);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getName(Object module, Object setting) {
/* 25 */     if (this.settings.containsKey(module)) {
/* 26 */       for (Field field : this.settings.get(module)) {
/*    */         try {
/* 28 */           if (field.get(module).equals(setting)) {
/* 29 */             return ((Setting)field.<Setting>getAnnotation(Setting.class)).name();
/*    */           }
/* 31 */         } catch (IllegalAccessException e) {
/* 32 */           e.printStackTrace();
/*    */         } 
/*    */       } 
/*    */     }
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   public String getDescription(Object module, Object setting) {
/* 40 */     if (this.settings.containsKey(module)) {
/* 41 */       for (Field field : this.settings.get(module)) {
/*    */         try {
/* 43 */           if (field.get(module).equals(setting)) {
/* 44 */             return ((Setting)field.<Setting>getAnnotation(Setting.class)).description();
/*    */           }
/* 46 */         } catch (IllegalAccessException e) {
/* 47 */           e.printStackTrace();
/*    */         } 
/*    */       } 
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   public Object getSetting(Object module, String name) {
/* 55 */     for (Object setting : getSettings(module)) {
/* 56 */       if (getName(module, setting).equalsIgnoreCase(name)) {
/* 57 */         return setting;
/*    */       }
/*    */     } 
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public ArrayList<Object> getSettings(Object module) {
/* 64 */     if (this.settings.containsKey(module)) {
/* 65 */       ArrayList<Object> setting = new ArrayList();
/* 66 */       this.settings.get(module).forEach(field -> {
/*    */             try {
/*    */               setting.add(field.get(module));
/* 69 */             } catch (IllegalAccessException e) {
/*    */               e.printStackTrace();
/*    */             } 
/*    */           });
/* 73 */       return setting;
/*    */     } 
/* 75 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\manager\SettingManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */