/*    */ package xyz.apfelmus.cf4m.configuration;
/*    */ 
/*    */ public interface IConfiguration {
/*    */   default void message(String message) {
/*  5 */     System.out.println(message);
/*    */   }
/*    */ 
/*    */   
/*    */   default void enable(Object module) {}
/*    */ 
/*    */   
/*    */   default void disable(Object module) {}
/*    */   
/*    */   default String prefix() {
/* 15 */     return ",";
/*    */   }
/*    */   
/*    */   default boolean config() {
/* 19 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cf4m\configuration\IConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */