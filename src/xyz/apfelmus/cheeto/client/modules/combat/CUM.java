/*    */ package xyz.apfelmus.cheeto.client.modules.combat;
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.InventoryUtils;
/*    */ 
/*    */ @Module(name = "CUM", category = Category.COMBAT)
/*    */ public class CUM {
/* 16 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   @Setting(name = "MainSlot", description = "Slot of the weapon you want held")
/* 18 */   private IntegerSetting mainSlot = new IntegerSetting(
/* 19 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "PickupStash", description = "Runs /pickupstash after activation")
/* 20 */   private BooleanSetting pickupStash = new BooleanSetting(true);
/*    */   @Setting(name = "InvMode", description = "A bit bannable")
/* 22 */   private BooleanSetting invMode = new BooleanSetting(false);
/*    */ 
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 27 */     int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*    */     
/* 29 */     if (!this.invMode.isEnabled()) {
/* 30 */       for (int i = 0; i < 8; i++) {
/* 31 */         ItemStack a = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 32 */         if (a != null && a.func_82833_r().contains("Snowball")) {
/* 33 */           InventoryUtils.throwSlot(i);
/*    */         }
/*    */       } 
/*    */     } else {
/* 37 */       int snowballSlot = InventoryUtils.getAvailableHotbarSlot("Snowball");
/*    */       
/* 39 */       if (snowballSlot == -1 || InventoryUtils.getAllSlots(snowballSlot, "Snowball").size() == 0) {
/* 40 */         CF4M.INSTANCE.moduleManager.toggle(this);
/*    */         
/*    */         return;
/*    */       } 
/* 44 */       InventoryUtils.throwSlot(snowballSlot);
/*    */       
/* 46 */       for (Iterator<Integer> iterator = InventoryUtils.getAllSlots(snowballSlot, "Snowball").iterator(); iterator.hasNext(); ) { int slotNum = ((Integer)iterator.next()).intValue();
/* 47 */         ItemStack curInSlot = mc.field_71439_g.field_71071_by.func_70301_a(snowballSlot);
/*    */         
/* 49 */         if (curInSlot == null) {
/* 50 */           mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71069_bz.field_75152_c, slotNum, snowballSlot, 2, (EntityPlayer)mc.field_71439_g);
/*    */         }
/*    */         
/* 53 */         InventoryUtils.throwSlot(snowballSlot); }
/*    */     
/*    */     } 
/*    */     
/* 57 */     if (this.mainSlot.getCurrent().intValue() > 0 && this.mainSlot.getCurrent().intValue() <= 8) {
/* 58 */       mc.field_71439_g.field_71071_by.field_70461_c = this.mainSlot.getCurrent().intValue() - 1;
/*    */     } else {
/* 60 */       mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
/*    */     } 
/*    */     
/* 63 */     if (this.pickupStash.isEnabled()) {
/* 64 */       mc.field_71439_g.func_71165_d("/pickupstash");
/*    */     }
/* 66 */     CF4M.INSTANCE.moduleManager.toggle(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\CUM.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */