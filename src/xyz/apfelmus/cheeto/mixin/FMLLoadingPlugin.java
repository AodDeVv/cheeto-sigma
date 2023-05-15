/*    */ package xyz.apfelmus.cheeto.mixin;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
/*    */ import org.spongepowered.asm.launch.MixinBootstrap;
/*    */ import org.spongepowered.asm.mixin.Mixins;
/*    */ 
/*    */ public class FMLLoadingPlugin
/*    */   implements IFMLLoadingPlugin
/*    */ {
/*    */   public FMLLoadingPlugin() {
/* 12 */     MixinBootstrap.init();
/* 13 */     Mixins.addConfiguration("mixins.cheeto.json");
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getASMTransformerClass() {
/* 18 */     return new String[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public String getModContainerClass() {
/* 23 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSetupClass() {
/* 28 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void injectData(Map<String, Object> data) {}
/*    */ 
/*    */   
/*    */   public String getAccessTransformerClass() {
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\FMLLoadingPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */