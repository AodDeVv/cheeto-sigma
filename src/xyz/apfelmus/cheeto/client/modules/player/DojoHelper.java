/*    */ package xyz.apfelmus.cheeto.client.modules.player;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.monster.EntitySkeleton;
/*    */ import net.minecraft.util.Vec3;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Disable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.FloatSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.math.VecUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Module(name = "DojoHelper", description = "Will do certain Dojo Challenges for you, active after entering", category = Category.PLAYER)
/*    */ public class DojoHelper
/*    */ {
/*    */   @Setting(name = "Mode")
/* 36 */   private ModeSetting mode = new ModeSetting("Control", 
/* 37 */       Arrays.asList(new String[] { "Control" })); @Setting(name = "LookSpeed")
/* 38 */   private IntegerSetting lookSpeed = new IntegerSetting(
/* 39 */       Integer.valueOf(80), Integer.valueOf(5), Integer.valueOf(100)); @Setting(name = "LookForward", description = "Will look x blocks infront of the Wither Skeleton (Control)")
/* 40 */   private FloatSetting lookForward = new FloatSetting(
/* 41 */       Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(5.0F));
/*    */   
/* 43 */   private static Entity witherSkel = null;
/*    */   
/* 45 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 49 */     if (!SkyblockUtils.isInDojo()) {
/* 50 */       ChatUtils.send("You are not in the Dojo!", new String[0]);
/* 51 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*    */       return;
/*    */     } 
/* 54 */     witherSkel = null;
/*    */   }
/*    */   
/*    */   @Disable
/*    */   public void onDisable() {
/* 59 */     RotationUtils.reset();
/*    */   }
/*    */   @Event
/*    */   public void onTick(ClientTickEvent event) {
/*    */     Vec3 aimPos;
/* 64 */     switch (this.mode.getCurrent()) {
/*    */       case "Control":
/* 66 */         if (witherSkel == null) {
/* 67 */           for (Entity e : mc.field_71441_e.func_175644_a(EntitySkeleton.class, e -> (e.func_82202_m() == 1 && e.func_70032_d((Entity)mc.field_71439_g) < 25.0F))) {
/* 68 */             if (mc.field_71439_g.func_70685_l(e)) {
/* 69 */               witherSkel = e; return;
/*    */             } 
/*    */           } 
/*    */           break;
/*    */         } 
/* 74 */         aimPos = witherSkel.func_174824_e(1.0F);
/*    */         
/* 76 */         aimPos = aimPos.func_178787_e(VecUtils.times(witherSkel.func_70040_Z(), this.lookForward.getCurrent().floatValue()));
/*    */         
/* 78 */         RotationUtils.setup(RotationUtils.getRotation(aimPos), Long.valueOf((100 - this.lookSpeed.getCurrent().intValue()) * 10L));
/*    */         break;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Event
/*    */   public void onRenderTick(Render3DEvent event) {
/* 86 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/* 87 */       return;  if (!RotationUtils.done) RotationUtils.update(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\player\DojoHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */