/*    */ package xyz.apfelmus.cheeto.mixin.mixins;
/*    */ 
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.apache.commons.codec.digest.DigestUtils;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.CapeStorage;
/*    */ 
/*    */ @Mixin({AbstractClientPlayer.class})
/*    */ public class MixinAbstractClientPlayer {
/*    */   @Shadow
/*    */   private NetworkPlayerInfo field_175157_a;
/*    */   
/*    */   @Inject(method = {"getLocationCape"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void getLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
/* 21 */     String dashed = this.field_175157_a.func_178845_a().getId().toString();
/* 22 */     ResourceLocation cape = CapeStorage.getCape(DigestUtils.sha1Hex(dashed.replaceAll("-", "")));
/*    */     
/* 24 */     if (cape != null)
/* 25 */       cir.setReturnValue(cape); 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\MixinAbstractClientPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */