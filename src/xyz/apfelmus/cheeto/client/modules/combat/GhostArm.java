/*    */ package xyz.apfelmus.cheeto.client.modules.combat;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ 
/*    */ @Module(name = "GhostArm", category = Category.COMBAT)
/*    */ public class GhostArm {
/*    */   @Setting(name = "Zombies", description = "Ignore Zombies")
/* 10 */   public BooleanSetting Zombies = new BooleanSetting(true);
/*    */   @Setting(name = "Players", description = "Ignore Players")
/* 12 */   public BooleanSetting Players = new BooleanSetting(true);
/*    */   @Setting(name = "ArmorStands", description = "Ignore ArmorStands")
/* 14 */   public BooleanSetting ArmorStands = new BooleanSetting(false);
/*    */   @Setting(name = "HideMobs", description = "Hides the mob instead of just letting you click through them")
/* 16 */   public BooleanSetting HideMobs = new BooleanSetting(true);
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\GhostArm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */