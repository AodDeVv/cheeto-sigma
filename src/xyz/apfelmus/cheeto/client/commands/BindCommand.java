/*    */ package xyz.apfelmus.cheeto.client.commands;
/*    */ 
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.command.Command;
/*    */ import xyz.apfelmus.cf4m.annotation.command.Exec;
/*    */ import xyz.apfelmus.cf4m.annotation.command.Param;
/*    */ 
/*    */ @Command(name = {"bind", "b"}, description = "Binds a module to a key")
/*    */ public class BindCommand {
/*    */   @Exec
/*    */   public void exec(@Param("Module") String moduleName, @Param("Key") String key) {
/* 13 */     Object m = CF4M.INSTANCE.moduleManager.getModule(moduleName);
/*    */     
/* 15 */     if (m == null) {
/* 16 */       CF4M.INSTANCE.configuration.message("The module &l" + moduleName + "&r does not exist");
/*    */       
/*    */       return;
/*    */     } 
/* 20 */     int bk = Keyboard.getKeyIndex(key.toUpperCase());
/*    */     
/* 22 */     if (key.equalsIgnoreCase("NONE")) {
/* 23 */       CF4M.INSTANCE.moduleManager.setKey(m, 0);
/* 24 */       CF4M.INSTANCE.configuration.message("&l" + CF4M.INSTANCE.moduleManager.getName(m) + "&r has been unbound");
/* 25 */       CF4M.INSTANCE.configManager.save();
/*    */     }
/* 27 */     else if (bk != 0) {
/* 28 */       CF4M.INSTANCE.moduleManager.setKey(m, bk);
/* 29 */       CF4M.INSTANCE.configuration.message("&l" + CF4M.INSTANCE.moduleManager.getName(m) + "&r has been bound to &7" + key.toUpperCase());
/* 30 */       CF4M.INSTANCE.configManager.save();
/*    */     } else {
/* 32 */       CF4M.INSTANCE.configuration.message("Invalid Key: &7" + key.toUpperCase());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\commands\BindCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */