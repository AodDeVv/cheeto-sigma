/*     */ package xyz.apfelmus.cf4m.manager;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Parameter;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.command.Command;
/*     */ import xyz.apfelmus.cf4m.annotation.command.Exec;
/*     */ import xyz.apfelmus.cf4m.annotation.command.Param;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandManager
/*     */ {
/*  24 */   private final HashMap<Object, String[]> commands = Maps.newHashMap();
/*  25 */   private final String prefix = CF4M.INSTANCE.configuration.prefix();
/*     */   public CommandManager() {
/*     */     try {
/*  28 */       for (Class<?> type : CF4M.INSTANCE.classManager.getClasses()) {
/*  29 */         if (type.isAnnotationPresent((Class)Command.class)) {
/*  30 */           this.commands.put(type.newInstance(), ((Command)type.<Command>getAnnotation(Command.class)).name());
/*     */         }
/*     */       } 
/*  33 */     } catch (InstantiationException|IllegalAccessException e) {
/*  34 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCommand(String rawMessage) {
/*  39 */     if (!rawMessage.startsWith(this.prefix)) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     boolean safe = ((rawMessage.split(this.prefix)).length > 1);
/*     */     
/*  45 */     if (safe) {
/*  46 */       String beheaded = rawMessage.split(this.prefix)[1];
/*  47 */       List<String> args = Lists.newArrayList((Object[])beheaded.split(" "));
/*  48 */       String key = args.get(0);
/*  49 */       args.remove(key);
/*     */       
/*  51 */       Object command = getCommand(key);
/*     */       
/*  53 */       if (command != null) {
/*  54 */         if (!execCommand(command, args)) {
/*  55 */           for (Method method : command.getClass().getDeclaredMethods()) {
/*  56 */             if (method.isAnnotationPresent((Class)Exec.class)) {
/*  57 */               Parameter[] parameters = method.getParameters();
/*  58 */               List<String> params = Lists.newArrayList();
/*  59 */               for (Parameter parameter : parameters) {
/*  60 */                 params.add("<" + (parameter.isAnnotationPresent((Class)Param.class) ? ((Param)parameter.<Param>getAnnotation(Param.class)).value() : "NULL") + "|" + parameter.getType().getSimpleName() + ">");
/*     */               }
/*  62 */               CF4M.INSTANCE.configuration.message(key + " " + params);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } else {
/*  67 */         help();
/*     */       } 
/*     */     } else {
/*  70 */       help();
/*     */     } 
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   private boolean execCommand(Object command, List<String> args) {
/*  76 */     for (Method method : command.getClass().getDeclaredMethods()) {
/*  77 */       method.setAccessible(true);
/*     */       
/*  79 */       if ((method.getParameterTypes()).length == args.size() && method.isAnnotationPresent((Class)Exec.class)) {
/*  80 */         List<Object> params = Lists.newArrayList();
/*  81 */         for (int i = 0; i < (method.getParameterTypes()).length; i++) {
/*  82 */           String arg = args.get(i);
/*  83 */           Class<?> paramType = method.getParameterTypes()[i];
/*     */           
/*     */           try {
/*  86 */             if (paramType.equals(Boolean.class)) {
/*  87 */               params.add(Boolean.valueOf(Boolean.parseBoolean(arg)));
/*  88 */             } else if (paramType.equals(Integer.class)) {
/*  89 */               params.add(Integer.valueOf(Integer.parseInt(arg)));
/*  90 */             } else if (paramType.equals(Float.class)) {
/*  91 */               params.add(Float.valueOf(Float.parseFloat(arg)));
/*  92 */             } else if (paramType.equals(Double.class)) {
/*  93 */               params.add(Double.valueOf(Double.parseDouble(arg)));
/*  94 */             } else if (paramType.equals(Long.class)) {
/*  95 */               params.add(Long.valueOf(Long.parseLong(arg)));
/*  96 */             } else if (paramType.equals(Short.class)) {
/*  97 */               params.add(Short.valueOf(Short.parseShort(arg)));
/*  98 */             } else if (paramType.equals(Byte.class)) {
/*  99 */               params.add(Byte.valueOf(Byte.parseByte(arg)));
/* 100 */             } else if (paramType.equals(String.class)) {
/* 101 */               params.add(String.valueOf(arg));
/*     */             } 
/* 103 */           } catch (Exception e) {
/* 104 */             CF4M.INSTANCE.configuration.message(e.getMessage());
/* 105 */             e.printStackTrace();
/*     */           } 
/*     */         } 
/*     */         
/*     */         try {
/* 110 */           if (params.size() == 0) {
/* 111 */             method.invoke(command, new Object[0]);
/*     */           } else {
/* 113 */             method.invoke(command, params.toArray());
/*     */           } 
/* 115 */           return true;
/* 116 */         } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/*     */           
/* 118 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/* 122 */     return false;
/*     */   }
/*     */   
/*     */   private void help() {
/* 126 */     for (Map.Entry<Object, String[]> entry : this.commands.entrySet()) {
/* 127 */       CF4M.INSTANCE.configuration.message(Arrays.toString((Object[])entry.getValue()) + " - " + getDescription(entry.getKey()));
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDescription(Object object) {
/* 132 */     if (this.commands.containsKey(object)) {
/* 133 */       return ((Command)object.getClass().<Command>getAnnotation(Command.class)).description();
/*     */     }
/* 135 */     return null;
/*     */   }
/*     */   
/*     */   public String[] getKey(Object object) {
/* 139 */     return this.commands.get(object);
/*     */   }
/*     */   
/*     */   private Object getCommand(String key) {
/* 143 */     for (Map.Entry<Object, String[]> entry : this.commands.entrySet()) {
/* 144 */       for (String s : (String[])entry.getValue()) {
/* 145 */         if (s.equalsIgnoreCase(key)) {
/* 146 */           return entry.getKey();
/*     */         }
/*     */       } 
/*     */     } 
/* 150 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\manager\CommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */