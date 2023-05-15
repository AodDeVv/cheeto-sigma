/*    */ package xyz.apfelmus.cheeto.client.modules.combat;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ 
/*    */ @Module(name = "SilentHeal", category = Category.COMBAT)
/*    */ public class SilentHeal {
/* 14 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   @Setting(name = "AtomSlot", description = "Slot of your Katana")
/* 16 */   private IntegerSetting atomSlot = new IntegerSetting(
/* 17 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "SussySlot", description = "Slot of your Sussy Scylla")
/* 18 */   private IntegerSetting sussySlot = new IntegerSetting(
/* 19 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "WandSlot", description = "Slot of your Wand of Atonement")
/* 20 */   private IntegerSetting wandSlot = new IntegerSetting(
/* 21 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "PigmanSlot", description = "Slot of your PigmanSword")
/* 22 */   private IntegerSetting pigmanSlot = new IntegerSetting(
/* 23 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8));
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 27 */     if (this.sussySlot.getCurrent().intValue() > 0 && this.sussySlot.getCurrent().intValue() <= 8) {
/* 28 */       mc.field_71439_g.field_71071_by.field_70461_c = this.sussySlot.getCurrent().intValue() - 1;
/* 29 */       mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/* 30 */       ShieldCD.LastShield = System.currentTimeMillis();
/*    */     } 
/*    */     
/* 33 */     if (this.wandSlot.getCurrent().intValue() > 0 && this.wandSlot.getCurrent().intValue() <= 8) {
/* 34 */       mc.field_71439_g.field_71071_by.field_70461_c = this.wandSlot.getCurrent().intValue() - 1;
/* 35 */       mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/*    */     } 
/*    */     
/* 38 */     if (this.pigmanSlot.getCurrent().intValue() > 0 && this.pigmanSlot.getCurrent().intValue() <= 8) {
/* 39 */       mc.field_71439_g.field_71071_by.field_70461_c = this.pigmanSlot.getCurrent().intValue() - 1;
/* 40 */       mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.func_70694_bm());
/*    */     } 
/*    */     
/* 43 */     if (this.atomSlot.getCurrent().intValue() > 0 && this.atomSlot.getCurrent().intValue() <= 8) {
/* 44 */       mc.field_71439_g.field_71071_by.field_70461_c = this.atomSlot.getCurrent().intValue() - 1;
/*    */     }
/*    */     
/* 47 */     CF4M.INSTANCE.moduleManager.toggle(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\SilentHeal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */