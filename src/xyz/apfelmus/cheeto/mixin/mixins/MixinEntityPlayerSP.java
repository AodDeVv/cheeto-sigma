/*    */ package xyz.apfelmus.cheeto.mixin.mixins;
/*    */ 
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cheeto.client.events.ClientChatSentEvent;
/*    */ 
/*    */ @Mixin({EntityPlayerSP.class})
/*    */ public class MixinEntityPlayerSP {
/*    */   @Inject(at = {@At("HEAD")}, method = {"sendChatMessage"}, cancellable = true)
/*    */   private void sendChatMessage(String message, CallbackInfo ci) {
/* 15 */     (new ClientChatSentEvent(message)).call();
/* 16 */     if (CF4M.INSTANCE.commandManager.isCommand(message))
/* 17 */       ci.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\MixinEntityPlayerSP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */