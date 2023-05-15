/*    */ package xyz.apfelmus.cheeto.client.modules.misc;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.BackgroundDrawnEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.InventoryUtils;
/*    */ 
/*    */ @Module(name = "ArmorSwap", category = Category.MISC)
/*    */ public class ArmorSwap {
/* 17 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   @Setting(name = "ArmorSlot", description = "Slot of the armor set to swap to")
/* 19 */   public IntegerSetting armorSlot = new IntegerSetting(
/* 20 */       Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(9));
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 24 */     mc.field_71439_g.func_71165_d("/pets");
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onTick(BackgroundDrawnEvent event) {
/* 29 */     String invName = InventoryUtils.getInventoryName();
/* 30 */     if (invName != null && invName.endsWith("Pets")) {
/* 31 */       ItemStack is = InventoryUtils.getStackInOpenContainerSlot(48);
/*    */       
/* 33 */       if (is != null) {
/* 34 */         InventoryUtils.clickOpenContainerSlot(48);
/* 35 */         InventoryUtils.clickOpenContainerSlot(32, 1);
/* 36 */         InventoryUtils.clickOpenContainerSlot(this.armorSlot.getCurrent().intValue() + 35, 2);
/* 37 */         mc.field_71439_g.func_71053_j();
/* 38 */         CF4M.INSTANCE.moduleManager.toggle(this);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\misc\ArmorSwap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */