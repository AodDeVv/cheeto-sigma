/*    */ package xyz.apfelmus.cf4m.manager;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.io.File;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.config.Config;
/*    */ import xyz.apfelmus.cf4m.annotation.config.Load;
/*    */ import xyz.apfelmus.cf4m.annotation.config.Save;
/*    */ 
/*    */ public class ConfigManager
/*    */ {
/*    */   private HashMap<String, Object> configs;
/*    */   
/*    */   public ConfigManager() {
/* 20 */     this.configs = Maps.newHashMap();
/*    */     
/* 22 */     (new File(CF4M.INSTANCE.dir)).mkdir();
/* 23 */     (new File(CF4M.INSTANCE.dir, "configs")).mkdir();
/*    */     try {
/* 25 */       for (Class<?> type : CF4M.INSTANCE.classManager.getClasses()) {
/* 26 */         if (type.isAnnotationPresent((Class)Config.class)) {
/* 27 */           this.configs.put(((Config)type.<Config>getAnnotation(Config.class)).name(), type.newInstance());
/*    */         }
/*    */       } 
/* 30 */     } catch (IllegalAccessException|InstantiationException e) {
/* 31 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public Object getConfig(String name) {
/* 36 */     return this.configs.getOrDefault(name, null);
/*    */   }
/*    */   
/*    */   public String getName(Object object) {
/* 40 */     for (Map.Entry<String, Object> e : this.configs.entrySet()) {
/* 41 */       if (e.getValue().equals(object)) {
/* 42 */         return e.getKey();
/*    */       }
/*    */     } 
/* 45 */     return null;
/*    */   }
/*    */   
/*    */   public String getPath(Object object) {
/* 49 */     if (this.configs.containsValue(object)) {
/* 50 */       return CF4M.INSTANCE.dir + File.separator + "configs" + File.separator + getName(object) + ".json";
/*    */     }
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public ArrayList<String> getConfigs() {
/* 56 */     return Lists.newArrayList(this.configs.keySet());
/*    */   }
/*    */   
/*    */   public void load() {
/* 60 */     this.configs.values().forEach(config -> {
/*    */           for (Method method : config.getClass().getMethods()) {
/*    */             method.setAccessible(true);
/*    */             if (method.isAnnotationPresent((Class)Load.class)) {
/*    */               try {
/*    */                 method.invoke(config, new Object[0]);
/* 66 */               } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/*    */                 e.printStackTrace();
/*    */               } 
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   public void save() {
/* 75 */     if (CF4M.INSTANCE.moduleManager.isEnabled("ClickGUI")) {
/* 76 */       CF4M.INSTANCE.moduleManager.toggle("ClickGUI");
/*    */     }
/* 78 */     this.configs.values().forEach(config -> {
/*    */           for (Method method : config.getClass().getMethods()) {
/*    */             method.setAccessible(true);
/*    */             if (method.isAnnotationPresent((Class)Save.class))
/*    */               try {
/*    */                 method.invoke(config, new Object[0]);
/* 84 */               } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/*    */                 e.printStackTrace();
/*    */               }  
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\manager\ConfigManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */