/*    */ package xyz.apfelmus.cheeto.mixin.mixins;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.ModContainer;
/*    */ import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ 
/*    */ @Mixin({FMLHandshakeMessage.ModList.class})
/*    */ public class MixinModlist
/*    */ {
/*    */   @Shadow
/*    */   private Map<String, String> modTags;
/*    */   
/*    */   @Inject(method = {"<init>(Ljava/util/List;)V"}, at = {@At("RETURN")})
/*    */   private void removeModID(List<ModContainer> modList, CallbackInfo ci) {
/* 23 */     if (!Minecraft.func_71410_x().func_71387_A())
/* 24 */       this.modTags.remove("ChromaHUD"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\MixinModlist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */