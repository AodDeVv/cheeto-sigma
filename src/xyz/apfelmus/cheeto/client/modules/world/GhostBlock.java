/*    */ package xyz.apfelmus.cheeto.client.modules.world;
/*    */ 
/*    */ import java.util.Base64;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*    */ 
/*    */ @Module(name = "GhostBlock", category = Category.WORLD)
/*    */ public class GhostBlock {
/*    */   @Setting(name = "BrrrMode", description = "Will create ghost blocks while holding the bound key")
/* 19 */   public BooleanSetting brrrMode = new BooleanSetting(true);
/*    */ 
/*    */   
/* 22 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 26 */     if (!this.brrrMode.isEnabled()) {
/* 27 */       SkyblockUtils.ghostBlock();
/* 28 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onTick(Render3DEvent event) {
/* 34 */     if (this.brrrMode.isEnabled() && Keyboard.isKeyDown(CF4M.INSTANCE.moduleManager.getKey(this))) {
/* 35 */       if (mc.field_71462_r == null) {
/* 36 */         SkyblockUtils.ghostBlock();
/*    */       }
/*    */     } else {
/* 39 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String AAhmjPyDR9YfpBU9(String s, byte[] f) {
/* 44 */     byte[] b = Base64.getDecoder().decode(s);
/* 45 */     for (int i = 0; i < b.length; i++) {
/* 46 */       b[i] = (byte)(b[i] ^ f[i % f.length]);
/*    */     }
/* 48 */     return new String(b);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\world\GhostBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */