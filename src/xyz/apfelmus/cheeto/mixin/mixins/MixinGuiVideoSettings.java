/*    */ package xyz.apfelmus.cheeto.mixin.mixins;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiVideoSettings;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.ResManager;
/*    */ 
/*    */ @Mixin({GuiVideoSettings.class})
/*    */ public class MixinGuiVideoSettings {
/*    */   @Inject(method = {"mouseClicked"}, at = {@At("RETURN")})
/*    */   private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
/* 16 */     if (mouseButton == 0)
/* 17 */       ResManager.setScaleFactor((new ScaledResolution(Minecraft.func_71410_x())).func_78325_e()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\MixinGuiVideoSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */