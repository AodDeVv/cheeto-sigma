/*     */ package xyz.apfelmus.cf4m.manager;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Disable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cf4m.annotation.module.extend.Name;
/*     */ import xyz.apfelmus.cf4m.event.events.KeyboardEvent;
/*     */ import xyz.apfelmus.cf4m.module.Category;
/*     */ import xyz.apfelmus.cf4m.module.ValueBean;
/*     */ 
/*     */ public class ModuleManager {
/*  22 */   private final LinkedHashMap<Object, LinkedHashSet<ValueBean>> modules = Maps.newLinkedHashMap();
/*     */   
/*     */   public ModuleManager() {
/*  25 */     CF4M.INSTANCE.eventManager.register(this);
/*     */     try {
/*  27 */       Class<?> extend = null;
/*  28 */       HashMap<String, Field> findFields = Maps.newHashMap();
/*  29 */       for (Class<?> type : CF4M.INSTANCE.classManager.getClasses()) {
/*  30 */         if (type.isAnnotationPresent((Class)Extend.class)) {
/*  31 */           extend = type;
/*  32 */           for (Field field : type.getDeclaredFields()) {
/*  33 */             field.setAccessible(true);
/*  34 */             if (field.isAnnotationPresent((Class)Name.class)) {
/*  35 */               Name name = field.<Name>getAnnotation(Name.class);
/*  36 */               findFields.put(name.name(), field);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  42 */       for (Class<?> type : CF4M.INSTANCE.classManager.getClasses()) {
/*  43 */         if (type.isAnnotationPresent((Class)Module.class)) {
/*  44 */           Object extendObject = (extend != null) ? extend.newInstance() : null;
/*  45 */           Object moduleObject = type.newInstance();
/*  46 */           LinkedHashSet<ValueBean> valueBeans = Sets.newLinkedHashSet();
/*  47 */           for (Map.Entry<String, Field> entry : findFields.entrySet()) {
/*  48 */             valueBeans.add(new ValueBean(entry.getKey(), entry.getValue(), extendObject));
/*     */           }
/*  50 */           this.modules.put(moduleObject, valueBeans);
/*     */         } 
/*     */       } 
/*  53 */     } catch (IllegalAccessException|InstantiationException e) {
/*  54 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getName(Object module) {
/*  59 */     if (this.modules.containsKey(module)) {
/*  60 */       return ((Module)module.getClass().<Module>getAnnotation(Module.class)).name();
/*     */     }
/*  62 */     return null;
/*     */   }
/*     */   
/*     */   public long getActivatedTime(Object module) {
/*  66 */     if (this.modules.containsKey(module)) {
/*  67 */       return ((Module)module.getClass().<Module>getAnnotation(Module.class)).time();
/*     */     }
/*     */     
/*  70 */     return -1L;
/*     */   }
/*     */   
/*     */   public void setActivatedTime(Object module, long value) {
/*  74 */     if (this.modules.containsKey(module)) {
/*     */       try {
/*  76 */         TypeAnnotation(Proxy.getInvocationHandler(module.getClass().getAnnotation(Module.class)), "time", Long.valueOf(value));
/*  77 */       } catch (NoSuchFieldException|IllegalAccessException e) {
/*  78 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isEnabled(Object module) {
/*  84 */     if (this.modules.containsKey(module)) {
/*  85 */       return ((Module)module.getClass().<Module>getAnnotation(Module.class)).enable();
/*     */     }
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isEnabled(String module) {
/*  91 */     Object mod = CF4M.INSTANCE.moduleManager.getModule(module);
/*  92 */     return isEnabled(mod);
/*     */   }
/*     */   
/*     */   private void setEnable(Object module, boolean value) {
/*  96 */     if (this.modules.containsKey(module)) {
/*  97 */       if (value) {
/*  98 */         setActivatedTime(module, System.currentTimeMillis());
/*     */       }
/*     */       try {
/* 101 */         TypeAnnotation(Proxy.getInvocationHandler(module.getClass().getAnnotation(Module.class)), "enable", Boolean.valueOf(value));
/* 102 */       } catch (NoSuchFieldException|IllegalAccessException e) {
/* 103 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getKey(Object module) {
/* 109 */     if (this.modules.containsKey(module)) {
/* 110 */       return ((Module)module.getClass().<Module>getAnnotation(Module.class)).key();
/*     */     }
/* 112 */     return 0;
/*     */   }
/*     */   
/*     */   public void setKey(Object module, int value) {
/* 116 */     if (this.modules.containsKey(module)) {
/*     */       try {
/* 118 */         TypeAnnotation(Proxy.getInvocationHandler(module.getClass().getAnnotation(Module.class)), "key", Integer.valueOf(value));
/* 119 */       } catch (NoSuchFieldException|IllegalAccessException e) {
/* 120 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public Category getCategory(Object module) {
/* 126 */     if (this.modules.containsKey(module)) {
/* 127 */       return ((Module)module.getClass().<Module>getAnnotation(Module.class)).category();
/*     */     }
/* 129 */     return Category.NONE;
/*     */   }
/*     */   
/*     */   public String getDescription(Object module) {
/* 133 */     if (this.modules.containsKey(module)) {
/* 134 */       return ((Module)module.getClass().<Module>getAnnotation(Module.class)).description();
/*     */     }
/* 136 */     return "";
/*     */   }
/*     */   
/*     */   public boolean isSilent(Object module) {
/* 140 */     if (this.modules.containsKey(module)) {
/* 141 */       return ((Module)module.getClass().<Module>getAnnotation(Module.class)).silent();
/*     */     }
/* 143 */     return false;
/*     */   }
/*     */   
/*     */   public <T> T getValue(Object module, String name) {
/*     */     try {
/* 148 */       if (this.modules.containsKey(module)) {
/* 149 */         for (ValueBean valueBean : this.modules.get(module)) {
/* 150 */           if (valueBean.getName().equals(name)) {
/* 151 */             return (T)valueBean.getField().get(valueBean.getObject());
/*     */           }
/*     */         } 
/*     */       }
/* 155 */     } catch (IllegalAccessException e) {
/* 156 */       e.printStackTrace();
/*     */     } 
/* 158 */     return null;
/*     */   }
/*     */   
/*     */   public <T> void setValue(Object module, String name, T value) {
/*     */     try {
/* 163 */       if (this.modules.containsKey(module)) {
/* 164 */         for (ValueBean valueBean : this.modules.get(module)) {
/* 165 */           if (valueBean.getName().equals(name)) {
/* 166 */             valueBean.getField().set(valueBean.getObject(), value);
/*     */           }
/*     */         } 
/*     */       }
/* 170 */     } catch (IllegalAccessException e) {
/* 171 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setEnabled(Object module, boolean enabled) {
/* 176 */     if (this.modules.containsKey(module)) {
/* 177 */       Class<?> type = module.getClass();
/*     */       
/* 179 */       if ((enabled && isEnabled(module)) || (!enabled && !isEnabled(module))) {
/*     */         return;
/*     */       }
/*     */       
/* 183 */       setEnable(module, enabled);
/*     */       
/* 185 */       if (isEnabled(module)) {
/* 186 */         CF4M.INSTANCE.configuration.enable(module);
/* 187 */         CF4M.INSTANCE.eventManager.register(module);
/*     */       } else {
/* 189 */         CF4M.INSTANCE.configuration.disable(module);
/* 190 */         CF4M.INSTANCE.eventManager.unregister(module);
/*     */       } 
/*     */       
/* 193 */       for (Method method : type.getDeclaredMethods()) {
/* 194 */         method.setAccessible(true);
/*     */         try {
/* 196 */           if (isEnabled(module)) {
/* 197 */             if (method.isAnnotationPresent((Class)Enable.class)) {
/* 198 */               method.invoke(module, new Object[0]);
/*     */             }
/*     */           }
/* 201 */           else if (method.isAnnotationPresent((Class)Disable.class)) {
/* 202 */             method.invoke(module, new Object[0]);
/*     */           }
/*     */         
/* 205 */         } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 206 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setEnabled(String module, boolean enabled) {
/* 213 */     setEnabled(getModule(module), enabled);
/*     */   }
/*     */   
/*     */   public void toggle(Object module) {
/* 217 */     if (this.modules.containsKey(module)) {
/* 218 */       Class<?> type = module.getClass();
/*     */       
/* 220 */       setEnable(module, !isEnabled(module));
/*     */       
/* 222 */       if (isEnabled(module)) {
/* 223 */         CF4M.INSTANCE.configuration.enable(module);
/* 224 */         CF4M.INSTANCE.eventManager.register(module);
/*     */       } else {
/* 226 */         CF4M.INSTANCE.configuration.disable(module);
/* 227 */         CF4M.INSTANCE.eventManager.unregister(module);
/*     */       } 
/*     */       
/* 230 */       for (Method method : type.getDeclaredMethods()) {
/* 231 */         method.setAccessible(true);
/*     */         try {
/* 233 */           if (isEnabled(module)) {
/* 234 */             if (method.isAnnotationPresent((Class)Enable.class)) {
/* 235 */               method.invoke(module, new Object[0]);
/*     */             }
/*     */           }
/* 238 */           else if (method.isAnnotationPresent((Class)Disable.class)) {
/* 239 */             method.invoke(module, new Object[0]);
/*     */           }
/*     */         
/* 242 */         } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 243 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toggle(String module) {
/* 250 */     toggle(getModule(module));
/*     */   }
/*     */   
/*     */   @Event
/*     */   private void onKey(KeyboardEvent e) {
/* 255 */     for (Object module : getModules()) {
/* 256 */       if (getKey(module) == e.getKey()) {
/* 257 */         toggle(module);
/* 258 */         CF4M.INSTANCE.configManager.save();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void TypeAnnotation(InvocationHandler invocationHandler, String name, Object value) throws NoSuchFieldException, IllegalAccessException {
/* 264 */     Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
/* 265 */     memberValues.setAccessible(true);
/* 266 */     Map<String, Object> map = (Map<String, Object>)memberValues.get(invocationHandler);
/* 267 */     map.put(name, value);
/*     */   }
/*     */   
/*     */   public ArrayList<Object> getModules() {
/* 271 */     return Lists.newArrayList(this.modules.keySet());
/*     */   }
/*     */   
/*     */   public ArrayList<Object> getModules(Category category) {
/* 275 */     return (ArrayList<Object>)getModules().stream().filter(module -> getCategory(module).equals(category)).collect(Collectors.toCollection(Lists::newArrayList));
/*     */   }
/*     */   
/*     */   public Object getModule(String name) {
/* 279 */     for (Object module : getModules()) {
/* 280 */       if (getName(module).equalsIgnoreCase(name)) {
/* 281 */         return module;
/*     */       }
/*     */     } 
/* 284 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\manager\ModuleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */