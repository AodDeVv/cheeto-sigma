/*    */ package xyz.apfelmus.cheeto.mixin.mixins;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.server.S12PacketEntityVelocity;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cheeto.client.events.PacketReceivedEvent;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*    */ 
/*    */ @Mixin({NetworkManager.class})
/*    */ public class MixinNetworkManager
/*    */ {
/*    */   @Inject(method = {"channelRead0"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
/* 22 */     if ((Minecraft.func_71410_x()).field_71439_g != null && SkyblockUtils.isInSkyblock()) {
/* 23 */       if (CF4M.INSTANCE.moduleManager.isEnabled("AntiKB") && !(Minecraft.func_71410_x()).field_71439_g.func_180799_ab()) {
/* 24 */         ItemStack held = (Minecraft.func_71410_x()).field_71439_g.func_70694_bm();
/* 25 */         if (held == null || (!held.func_82833_r().contains("Bonzo's Staff") && !held.func_82833_r().contains("Jerry-chine Gun"))) {
/* 26 */           if (packet instanceof net.minecraft.network.play.server.S27PacketExplosion) {
/* 27 */             ci.cancel();
/*    */           }
/* 29 */           if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packet).func_149412_c() == (Minecraft.func_71410_x()).field_71439_g.func_145782_y()) {
/* 30 */             ci.cancel();
/*    */           }
/*    */         } 
/*    */       } 
/*    */       
/* 35 */       (new PacketReceivedEvent(packet)).call();
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At("HEAD")})
/*    */   private void send(Packet<?> packet, CallbackInfo ci) {}
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\MixinNetworkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */