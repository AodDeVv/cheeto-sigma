/*    */ package xyz.apfelmus.cheeto.client.modules.world;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.init.Blocks;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ 
/*    */ @Module(name = "BetterStonk", category = Category.WORLD)
/*    */ public class BetterStonk {
/*    */   @Setting(name = "ShiftOnly")
/* 18 */   private BooleanSetting shiftOnly = new BooleanSetting(false);
/*    */ 
/*    */   
/* 21 */   private static Minecraft mc = Minecraft.func_71410_x();
/* 22 */   final ArrayList<Block> interactables = new ArrayList<>(Arrays.asList(new Block[] { Blocks.field_180410_as, Blocks.field_150467_bQ, (Block)Blocks.field_150461_bJ, Blocks.field_150324_C, Blocks.field_180412_aq, Blocks.field_150382_bo, Blocks.field_150483_bI, Blocks.field_150462_ai, (Block)Blocks.field_150486_ae, Blocks.field_180409_at, (Block)Blocks.field_150453_bW, (Block)Blocks.field_180402_cm, Blocks.field_150367_z, Blocks.field_150409_cd, Blocks.field_150381_bn, Blocks.field_150477_bB, Blocks.field_150460_al, (Block)Blocks.field_150438_bZ, Blocks.field_180411_ar, Blocks.field_150442_at, Blocks.field_150323_B, (Block)Blocks.field_150455_bV, (Block)Blocks.field_150441_bU, (Block)Blocks.field_150416_aS, (Block)Blocks.field_150413_aR, Blocks.field_150472_an, Blocks.field_150444_as, Blocks.field_150415_aT, Blocks.field_150447_bR, Blocks.field_150471_bO, Blocks.field_150430_aB, Blocks.field_180413_ao, (Block)Blocks.field_150465_bP }));
/*    */   
/*    */   @Event
/*    */   public void onTick(ClientTickEvent event) {
/* 26 */     if (mc.field_71476_x.func_178782_a() == null)
/* 27 */       return;  if (!mc.field_71474_y.field_74312_F.func_151470_d())
/* 28 */       return;  if (mc.field_71439_g.field_71071_by.func_70448_g() == null)
/* 29 */       return;  if (this.shiftOnly.isEnabled() && !mc.field_71439_g.func_70093_af())
/*    */       return; 
/* 31 */     if (mc.field_71439_g.field_71071_by.func_70448_g().func_82833_r().contains("Stonk")) {
/* 32 */       Block block = (Minecraft.func_71410_x()).field_71441_e.func_180495_p(mc.field_71476_x.func_178782_a()).func_177230_c();
/* 33 */       if (!this.interactables.contains(block))
/* 34 */         mc.field_71441_e.func_175698_g(mc.field_71476_x.func_178782_a()); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\world\BetterStonk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */