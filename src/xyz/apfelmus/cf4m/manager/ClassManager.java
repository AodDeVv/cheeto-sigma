/*    */ package xyz.apfelmus.cf4m.manager;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.google.common.reflect.ClassPath;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.Configuration;
/*    */ import xyz.apfelmus.cf4m.configuration.IConfiguration;
/*    */ 
/*    */ public class ClassManager {
/* 13 */   private final ArrayList<Class<?>> classes = Lists.newArrayList();
/*    */   
/*    */   public ClassManager(ClassLoader classLoader) {
/*    */     try {
/* 17 */       for (UnmodifiableIterator<ClassPath.ClassInfo> unmodifiableIterator = ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClasses().iterator(); unmodifiableIterator.hasNext(); ) { ClassPath.ClassInfo info = unmodifiableIterator.next();
/* 18 */         if (!info.getName().startsWith(CF4M.INSTANCE.packName) || 
/* 19 */           info.getName().contains("mixin"))
/* 20 */           continue;  Class<?> type = classLoader.loadClass(info.getName());
/* 21 */         if (type.isAnnotationPresent((Class)Configuration.class)) {
/* 22 */           CF4M.INSTANCE.configuration = (IConfiguration)type.newInstance();
/*    */         }
/* 24 */         this.classes.add(type); }
/*    */ 
/*    */     
/* 27 */     } catch (IOException|ClassNotFoundException|InstantiationException|IllegalAccessException e) {
/* 28 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public ArrayList<Class<?>> getClasses() {
/* 33 */     return this.classes;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\manager\ClassManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */