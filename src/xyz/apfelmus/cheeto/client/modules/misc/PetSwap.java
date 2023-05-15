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
/*    */ @Module(name = "PetSwap", category = Category.MISC)
/*    */ public class PetSwap {
/* 17 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   @Setting(name = "PetSlot", description = "Slot of the pet you want to swap to")
/* 19 */   private IntegerSetting petSlot = new IntegerSetting(
/* 20 */       Integer.valueOf(11), Integer.valueOf(11), Integer.valueOf(44));
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
/* 31 */       ItemStack is = InventoryUtils.getStackInOpenContainerSlot(this.petSlot.getCurrent().intValue() - 1);
/*    */       
/* 33 */       if (is != null) {
/* 34 */         InventoryUtils.clickOpenContainerSlot(this.petSlot.getCurrent().intValue() - 1);
/* 35 */         mc.field_71439_g.func_71053_j();
/* 36 */         CF4M.INSTANCE.moduleManager.toggle(this);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\misc\PetSwap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */