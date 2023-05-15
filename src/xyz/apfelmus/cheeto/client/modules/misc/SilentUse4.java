/*    */ package xyz.apfelmus.cheeto.client.modules.misc;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Disable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.Flags;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*    */ 
/*    */ @Module(name = "SilentUse4", category = Category.MISC)
/*    */ public class SilentUse4 {
/*    */   @Setting(name = "MainSlot")
/* 16 */   private IntegerSetting mainSlot = new IntegerSetting(
/* 17 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "UseSlot")
/* 18 */   private IntegerSetting useSlot = new IntegerSetting(
/* 19 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "LeftClick")
/* 20 */   private BooleanSetting leftClick = new BooleanSetting(false);
/*    */ 
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 25 */     Flags.silentUsing = true;
/* 26 */     if (this.leftClick.isEnabled()) {
/* 27 */       SkyblockUtils.silentClick(this.mainSlot.getCurrent().intValue(), this.useSlot.getCurrent().intValue());
/*    */     } else {
/* 29 */       SkyblockUtils.silentUse(this.mainSlot.getCurrent().intValue(), this.useSlot.getCurrent().intValue());
/*    */     } 
/* 31 */     CF4M.INSTANCE.moduleManager.toggle(this);
/*    */   }
/*    */   
/*    */   @Disable
/*    */   public void onDisable() {
/* 36 */     Flags.silentUsing = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\misc\SilentUse4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */