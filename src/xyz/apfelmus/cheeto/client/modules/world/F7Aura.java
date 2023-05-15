/*    */ package xyz.apfelmus.cheeto.client.modules.world;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.GuiOpenEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.WorldUnloadEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.FloatSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*    */ 
/*    */ @Module(name = "F7Aura", description = "Aura for Crystals and Terms", category = Category.WORLD)
/*    */ public class F7Aura {
/*    */   @Setting(name = "Range")
/* 26 */   FloatSetting range = new FloatSetting(
/* 27 */       Float.valueOf(5.0F), Float.valueOf(0.0F), Float.valueOf(7.0F));
/*    */   
/* 29 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   private static boolean clicked = false;
/* 31 */   private static List<Entity> terms = new ArrayList<>();
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 35 */     terms.clear();
/* 36 */     clicked = false;
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onTick(ClientTickEvent event) {
/*    */     try {
/* 42 */       if (!SkyblockUtils.isInDungeon())
/* 43 */         return;  BlockPos pp = mc.field_71439_g.func_180425_c();
/* 44 */       for (int i = pp.func_177956_o(); i > 0; i--) {
/* 45 */         IBlockState bs = mc.field_71441_e.func_180495_p(new BlockPos(pp.func_177958_n(), i, pp.func_177952_p()));
/*    */         
/* 47 */         if (bs != null) {
/* 48 */           Block b = bs.func_177230_c();
/*    */           
/* 50 */           if (b != Blocks.field_150350_a) {
/* 51 */             if (b == Blocks.field_150353_l || b == Blocks.field_150356_k) {
/*    */               return;
/*    */             }
/*    */             
/*    */             break;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */       
/* 60 */       for (Entity e : mc.field_71441_e.field_72996_f) {
/* 61 */         if (e instanceof net.minecraft.entity.item.EntityArmorStand && e.func_70005_c_().contains("CLICK HERE") && !clicked && mc.field_71439_g.field_71070_bA.field_75152_c == 0 && !terms.contains(e) && e.func_70032_d((Entity)mc.field_71439_g) < this.range.getCurrent().floatValue()) {
/* 62 */           mc.field_71442_b.func_78768_b((EntityPlayer)mc.field_71439_g, e);
/* 63 */           clicked = true;
/* 64 */           terms.add(e);
/*    */           break;
/*    */         } 
/*    */       } 
/* 68 */     } catch (Exception exception) {}
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onGui(GuiOpenEvent event) {
/* 73 */     clicked = false;
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onWorldLoad(WorldUnloadEvent event) {
/* 78 */     terms.clear();
/* 79 */     clicked = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\world\F7Aura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */