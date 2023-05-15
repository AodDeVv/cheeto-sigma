/*    */ package xyz.apfelmus.cheeto.mixin.mixins;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.renderer.EntityRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.Vec3;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cheeto.client.modules.combat.GhostArm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({EntityRenderer.class})
/*    */ public class MixinEntityRenderer
/*    */ {
/*    */   @Inject(method = {"getMouseOver"}, at = {@At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0)}, locals = LocalCapture.CAPTURE_FAILSOFT)
/*    */   private void getMouseOver(float partialTicks, CallbackInfo ci, Entity entity, double d0, double d1, Vec3 vec3, boolean flag, boolean b, Vec3 vec31, Vec3 vec32, Vec3 vec33, float f, List<Entity> list, double d2, int j) {
/* 23 */     removeEntities(list);
/*    */   }
/*    */   
/*    */   @Inject(method = {"getMouseOver"}, at = {@At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0)}, locals = LocalCapture.CAPTURE_FAILSOFT)
/*    */   private void getMouseOver(float partialTicks, CallbackInfo ci, Entity entity, double d0, double d1, Vec3 vec3, boolean flag, int i, Vec3 vec31, Vec3 vec32, Vec3 vec33, float f, List<Entity> list, double d2, int j) {
/* 28 */     removeEntities(list);
/*    */   }
/*    */   
/*    */   private void removeEntities(List<Entity> list) {
/* 32 */     if (CF4M.INSTANCE.moduleManager.isEnabled("GhostArm")) {
/* 33 */       GhostArm ghostArm = (GhostArm)CF4M.INSTANCE.moduleManager.getModule("GhostArm");
/* 34 */       if (ghostArm.Zombies.isEnabled()) {
/* 35 */         list.removeIf(listEntity -> listEntity instanceof net.minecraft.entity.monster.EntityZombie);
/*    */       }
/*    */       
/* 38 */       if (ghostArm.Players.isEnabled()) {
/* 39 */         list.removeIf(listEntity -> listEntity instanceof net.minecraft.client.entity.EntityOtherPlayerMP);
/*    */       }
/*    */       
/* 42 */       if (ghostArm.ArmorStands.isEnabled())
/* 43 */         list.removeIf(listEntity -> listEntity instanceof net.minecraft.entity.item.EntityArmorStand); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\MixinEntityRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */