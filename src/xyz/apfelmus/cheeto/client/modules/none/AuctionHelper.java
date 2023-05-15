/*    */ package xyz.apfelmus.cheeto.client.modules.none;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.BackgroundDrawnEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.WindowClickEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.WorldUnloadEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.VersionCheck;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.InventoryUtils;
/*    */ 
/*    */ @Module(name = "AuctionHelper", category = Category.NONE)
/*    */ public class AuctionHelper {
/*    */   @Setting(name = "Mode", description = "Instant will skip the Confirm screen entirely, Confirm will only skip the last screen")
/* 23 */   private ModeSetting mode = new ModeSetting("Confirm", 
/* 24 */       Arrays.asList(new String[] { "Confirm", "Instant" })); @Setting(name = "BedMode", description = "Spams the grace period bed")
/* 25 */   private BooleanSetting bedMode = new BooleanSetting(false);
/*    */   @Setting(name = "Debug", description = "Prints debug info to the screen")
/* 27 */   private BooleanSetting debug = new BooleanSetting(false);
/*    */ 
/*    */   
/* 30 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   private static boolean buying = false;
/* 32 */   private static int windowId = 1;
/*    */   
/*    */   @Event
/*    */   public void onBackgroundDrawn(BackgroundDrawnEvent event) {
/* 36 */     if (this.mode.getCurrent().equals("Instant")) {
/* 37 */       String windowName = InventoryUtils.getInventoryName();
/*    */       
/* 39 */       if ("BIN Auction View".equals(windowName)) {
/* 40 */         ItemStack is = InventoryUtils.getStackInOpenContainerSlot(31);
/* 41 */         if (is != null && (is.func_77973_b() == Items.field_151074_bl || (this.bedMode.isEnabled() && is.func_77973_b() == Items.field_151104_aV))) {
/* 42 */           buying = true;
/* 43 */           windowId = mc.field_71439_g.field_71070_bA.field_75152_c;
/* 44 */           mc.field_71442_b.func_78753_a(windowId, 31, 2, 0, (EntityPlayer)mc.field_71439_g);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 49 */     if (this.bedMode.isEnabled()) {
/* 50 */       String windowName = InventoryUtils.getInventoryName();
/* 51 */       if (buying && "Confirm Purchase".equals(windowName)) {
/* 52 */         mc.field_71442_b.func_78753_a(windowId + 1, 11, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 53 */         mc.field_71439_g.func_71053_j();
/*    */         
/* 55 */         if (!VersionCheck.nope()) {
/* 56 */           mc.field_71439_g.func_71165_d("I just bought an Auction with Cheeto Sigma (gg/chromahud) while not paying for it!");
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 61 */     if (this.debug.isEnabled()) {
/* 62 */       FontUtils.normal.drawChromaString(String.valueOf(mc.field_71439_g.field_71070_bA.field_75152_c), 20, 20);
/*    */     }
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onWindowClick(WindowClickEvent event) {
/* 68 */     if (!this.bedMode.isEnabled()) {
/* 69 */       String windowName = InventoryUtils.getInventoryName();
/* 70 */       if (event.slotId == 31 && (buying || "BIN Auction View".equals(windowName))) {
/* 71 */         mc.field_71442_b.func_78753_a(event.windowId + 1, 11, 0, 0, (EntityPlayer)mc.field_71439_g);
/* 72 */         buying = false;
/* 73 */         mc.field_71439_g.func_71053_j();
/*    */         
/* 75 */         if (!VersionCheck.nope()) {
/* 76 */           mc.field_71439_g.func_71165_d("I just bought an Auction with Cheeto Sigma (gg/chromahud) while not paying for it!");
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onWorldChange(WorldUnloadEvent event) {
/* 84 */     buying = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\none\AuctionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */