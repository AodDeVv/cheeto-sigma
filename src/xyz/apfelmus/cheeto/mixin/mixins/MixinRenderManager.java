/*    */ package xyz.apfelmus.cheeto.mixin.mixins;
/*    */ 
/*    */ import net.minecraft.client.renderer.culling.ICamera;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cheeto.client.modules.combat.GhostArm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({RenderManager.class})
/*    */ public class MixinRenderManager
/*    */ {
/*    */   @Inject(method = {"shouldRender"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
/* 21 */     if (CF4M.INSTANCE.moduleManager.isEnabled("GhostArm")) {
/* 22 */       GhostArm ga = (GhostArm)CF4M.INSTANCE.moduleManager.getModule("GhostArm");
/* 23 */       if (ga.HideMobs.isEnabled()) {
/* 24 */         if (ga.Zombies.isEnabled() && 
/* 25 */           entityIn instanceof net.minecraft.entity.monster.EntityZombie) {
/* 26 */           cir.setReturnValue(Boolean.valueOf(false));
/*    */         }
/*    */ 
/*    */         
/* 30 */         if (ga.Players.isEnabled() && 
/* 31 */           entityIn instanceof net.minecraft.client.entity.EntityOtherPlayerMP) {
/* 32 */           cir.setReturnValue(Boolean.valueOf(false));
/*    */         }
/*    */ 
/*    */         
/* 36 */         if (ga.ArmorStands.isEnabled() && 
/* 37 */           entityIn instanceof net.minecraft.entity.item.EntityArmorStand) {
/* 38 */           cir.setReturnValue(Boolean.valueOf(false));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 43 */     if (CF4M.INSTANCE.moduleManager.isEnabled("SnowballHider") && 
/* 44 */       entityIn instanceof net.minecraft.entity.projectile.EntitySnowball)
/* 45 */       cir.setReturnValue(Boolean.valueOf(false)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\MixinRenderManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */