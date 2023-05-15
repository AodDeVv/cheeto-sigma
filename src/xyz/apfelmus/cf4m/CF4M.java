/*    */ package xyz.apfelmus.cf4m;
/*    */ import xyz.apfelmus.cf4m.configuration.IConfiguration;
/*    */ import xyz.apfelmus.cf4m.manager.ClassManager;
/*    */ import xyz.apfelmus.cf4m.manager.CommandManager;
/*    */ import xyz.apfelmus.cf4m.manager.EventManager;
/*    */ import xyz.apfelmus.cf4m.manager.SettingManager;
/*    */ 
/*    */ public enum CF4M {
/*  9 */   INSTANCE;
/*    */   
/*    */   public ConfigManager configManager;
/*    */   public CommandManager commandManager;
/*    */   public SettingManager settingManager;
/*    */   public ModuleManager moduleManager;
/*    */   public EventManager eventManager;
/*    */   public ClassManager classManager;
/*    */   public IConfiguration configuration;
/*    */   public String dir;
/*    */   public String packName;
/*    */   
/*    */   public void run(Class<?> mainClass) {
/* 22 */     this.packName = mainClass.getPackage().getName();
/* 23 */     this.dir = (new File(".", mainClass.getSimpleName())).toString();
/* 24 */     this.configuration = new IConfiguration() {  }
/*    */       ;
/* 26 */     this.classManager = new ClassManager(mainClass.getClassLoader());
/* 27 */     this.eventManager = new EventManager();
/* 28 */     this.moduleManager = new ModuleManager();
/* 29 */     this.settingManager = new SettingManager();
/* 30 */     this.commandManager = new CommandManager();
/* 31 */     this.configManager = new ConfigManager();
/* 32 */     if (this.configuration.config()) {
/* 33 */       this.configManager.load();
/* 34 */       Runtime.getRuntime().addShutdownHook(new Thread(() -> this.configManager.save()));
/*    */     } 
/*    */   }
/*    */   
/*    */   public void run(Object o, String dir) {
/* 39 */     this.dir = dir;
/* 40 */     run(o.getClass());
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\CF4M.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */