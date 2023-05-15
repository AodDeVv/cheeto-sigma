/*    */ package xyz.apfelmus.cheeto.client.modules.render;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.Render2DEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.FloatSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.ResManager;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ @Module(name = "ShieldCD", category = Category.RENDER)
/*    */ public class ShieldCD
/*    */ {
/*    */   @Setting(name = "xPos")
/* 22 */   private IntegerSetting xPos = new IntegerSetting(
/* 23 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100)); @Setting(name = "yPos")
/* 24 */   private IntegerSetting yPos = new IntegerSetting(
/* 25 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100)); @Setting(name = "RGB")
/* 26 */   private BooleanSetting rgb = new BooleanSetting(true);
/*    */   @Setting(name = "Scale")
/* 28 */   private FloatSetting scale = new FloatSetting(
/* 29 */       Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(2.5F));
/*    */   
/* 31 */   public static long LastShield = 0L;
/*    */   
/*    */   @Event
/*    */   public void onRender(Render2DEvent event) {
/* 35 */     long timeDiffy = System.currentTimeMillis() - LastShield;
/*    */     
/* 37 */     if (!((Minecraft.func_71410_x()).field_71462_r instanceof xyz.apfelmus.cheeto.client.clickgui.ConfigGUI) && (Minecraft.func_71410_x()).field_71462_r != null) {
/*    */       return;
/*    */     }
/* 40 */     GlStateManager.func_179094_E();
/* 41 */     GlStateManager.func_179152_a(this.scale.getCurrent().floatValue(), this.scale.getCurrent().floatValue(), this.scale.getCurrent().floatValue());
/* 42 */     ScaledResolution scaled = new ScaledResolution(Minecraft.func_71410_x());
/* 43 */     int sf = ResManager.getScaleFactor();
/* 44 */     int scaledX = (int)((scaled.func_78326_a() * sf) * this.xPos.getCurrent().intValue() / 100.0D / this.scale.getCurrent().floatValue());
/* 45 */     int scaledY = (int)((scaled.func_78328_b() * sf) * this.yPos.getCurrent().intValue() / 100.0D / this.scale.getCurrent().floatValue());
/* 46 */     if (timeDiffy >= 5000L) {
/* 47 */       if (this.rgb.isEnabled()) {
/* 48 */         FontUtils.normal.drawHVCenteredChromaString("Shield: Ready", scaledX, scaledY);
/*    */       } else {
/* 50 */         FontUtils.normal.drawHVCenteredString("Shield: Ready", scaledX, scaledY, Color.GREEN.getRGB());
/*    */       } 
/*    */     } else {
/* 53 */       FontUtils.normal.drawHVCenteredString(String.format("Shield: %.3fs", new Object[] { Float.valueOf((float)(5000L - timeDiffy) / 1000.0F) }), scaledX, scaledY, Color.ORANGE.getRGB());
/*    */     } 
/* 55 */     GlStateManager.func_179121_F();
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\render\ShieldCD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */