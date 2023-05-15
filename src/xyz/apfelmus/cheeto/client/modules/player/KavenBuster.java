/*    */ package xyz.apfelmus.cheeto.client.modules.player;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.StringUtils;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.WorldUnloadEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.Render3DUtils;
/*    */ 
/*    */ @Module(name = "KavenBuster", description = "Will show you people that are likely using a shitty ratted mod", category = Category.PLAYER)
/*    */ public class KavenBuster {
/*    */   @Setting(name = "ShowSpots")
/* 25 */   private BooleanSetting showSpots = new BooleanSetting(false);
/*    */ 
/*    */   
/* 28 */   private static Minecraft mc = Minecraft.func_71410_x();
/* 29 */   private static List<Entity> macroers = new ArrayList<>();
/*    */   
/* 31 */   private static List<BlockPos> kavenSpots = new ArrayList<>(Arrays.asList(new BlockPos[] { new BlockPos(-101, 215, 125), new BlockPos(-89, 156, 83), new BlockPos(11, 227, 132), new BlockPos(61, 148, 178), new BlockPos(68, 153, 174), new BlockPos(-15, 215, 137), new BlockPos(-33, 243, -35), new BlockPos(48, 218, 122), new BlockPos(104, 164, 49), new BlockPos(-10, 216, 155), new BlockPos(5, 215, 163), new BlockPos(50, 226, -28), new BlockPos(50, 149, 2), new BlockPos(104, 165, 32), new BlockPos(-148, 214, -23), new BlockPos(-2, 135, 178), new BlockPos(-62, 154, 155) }));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 53 */     macroers.clear();
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onTick(ClientTickEvent event) {
/* 58 */     for (Entity e : mc.field_71441_e.field_72996_f) {
/* 59 */       if (!(e instanceof net.minecraft.client.entity.EntityOtherPlayerMP) || !kavenSpots.contains(e.func_180425_c()) || 
/* 60 */         e.func_70005_c_().equals("Goblin ") || e.func_70005_c_().contains("Treasuer Hunter") || e.func_70005_c_().contains("Crystal Sentry"))
/* 61 */         continue;  String formatted = e.func_145748_c_().func_150254_d();
/* 62 */       if (StringUtils.func_76338_a(formatted).equals(formatted)) {
/*    */         continue;
/*    */       }
/* 65 */       if (formatted.startsWith("§r") && !formatted.startsWith("§r§")) {
/*    */         continue;
/*    */       }
/* 68 */       if (!macroers.contains(e)) {
/* 69 */         macroers.add(e);
/* 70 */         BlockPos pos = e.func_180425_c();
/* 71 */         ChatUtils.send("KavenMod Enjoyer at &7" + pos.func_177958_n() + "," + pos.func_177956_o() + "," + pos.func_177952_p() + " &f: " + e.func_70005_c_(), new String[0]);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Event
/*    */   public void onRenderTick(Render3DEvent event) {
/* 79 */     if (this.showSpots.isEnabled()) {
/* 80 */       for (BlockPos bp : kavenSpots) {
/* 81 */         Render3DUtils.renderEspBox(bp, event.partialTicks, -256);
/*    */       }
/*    */     }
/*    */     
/* 85 */     for (Entity macroer : new ArrayList(macroers)) {
/* 86 */       if (mc.field_71441_e.field_72996_f.contains(macroer)) {
/* 87 */         Render3DUtils.renderBoundingBox(macroer, event.partialTicks, -16711936); continue;
/*    */       } 
/* 89 */       ChatUtils.send("KavenMod Enjoyer left: " + macroer.func_70005_c_(), new String[0]);
/* 90 */       macroers.remove(macroer);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Event
/*    */   public void onWorldLoad(WorldUnloadEvent event) {
/* 97 */     macroers.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\player\KavenBuster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */