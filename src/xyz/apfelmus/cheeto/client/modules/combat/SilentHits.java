/*    */ package xyz.apfelmus.cheeto.client.modules.combat;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.LeftClickEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.Flags;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*    */ 
/*    */ @Module(name = "SilentHits", category = Category.COMBAT)
/*    */ public class SilentHits {
/*    */   @Setting(name = "UseSlot1", description = "Just a slot to hit with on every click")
/* 15 */   private IntegerSetting useSlot1 = new IntegerSetting(
/* 16 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "UseSlot2", description = "Just a slot to hit with on every click")
/* 17 */   private IntegerSetting useSlot2 = new IntegerSetting(
/* 18 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "UseSlot3", description = "Just a slot to hit with on every click")
/* 19 */   private IntegerSetting useSlot3 = new IntegerSetting(
/* 20 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8));
/*    */   
/* 22 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   
/*    */   @Event
/*    */   public void onLeftClick(LeftClickEvent event) {
/* 26 */     if (!Flags.silentUsing) {
/* 27 */       if (this.useSlot1.getCurrent().intValue() > 0 && this.useSlot1.getCurrent().intValue() <= 8) {
/* 28 */         SkyblockUtils.silentUse(mc.field_71439_g.field_71071_by.field_70461_c + 1, this.useSlot1.getCurrent().intValue());
/*    */       }
/* 30 */       if (this.useSlot2.getCurrent().intValue() > 0 && this.useSlot2.getCurrent().intValue() <= 8) {
/* 31 */         SkyblockUtils.silentUse(mc.field_71439_g.field_71071_by.field_70461_c + 1, this.useSlot2.getCurrent().intValue());
/*    */       }
/* 33 */       if (this.useSlot3.getCurrent().intValue() > 0 && this.useSlot3.getCurrent().intValue() <= 8)
/* 34 */         SkyblockUtils.silentUse(mc.field_71439_g.field_71071_by.field_70461_c + 1, this.useSlot3.getCurrent().intValue()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\SilentHits.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */