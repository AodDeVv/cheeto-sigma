/*    */ package xyz.apfelmus.cheeto.mixin.mixins;
/*    */ 
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.network.play.server.S2APacketParticles;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import xyz.apfelmus.cheeto.client.modules.player.AutoFish;
/*    */ 
/*    */ @Mixin({NetHandlerPlayClient.class})
/*    */ public class MixinNetHandlerPlayClient {
/*    */   @Inject(method = {"handleParticles"}, at = {@At("HEAD")})
/*    */   private void handleParticles(S2APacketParticles packetIn, CallbackInfo ci) {
/* 15 */     AutoFish.handleParticles(packetIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\MixinNetHandlerPlayClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */