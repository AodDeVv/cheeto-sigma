/*    */ package xyz.apfelmus.cheeto.client.modules.render;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.network.play.server.S03PacketTimeUpdate;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.PacketReceivedEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.Render2DEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.WorldUnloadEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.ResManager;
/*    */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Module(name = "TPSViewer", description = "Will approximate TPS of your current server", category = Category.RENDER)
/*    */ public class TPSViewer
/*    */ {
/*    */   @Setting(name = "xPos")
/* 32 */   private IntegerSetting xPos = new IntegerSetting(
/* 33 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100)); @Setting(name = "yPos")
/* 34 */   private IntegerSetting yPos = new IntegerSetting(
/* 35 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(100)); @Setting(name = "RGB")
/* 36 */   private BooleanSetting rgb = new BooleanSetting(true);
/*    */ 
/*    */   
/* 39 */   public static List<Float> serverTPS = new ArrayList<>();
/* 40 */   private static long systemTime = 0L;
/* 41 */   private static long serverTime = 0L;
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 45 */     serverTPS.clear();
/* 46 */     systemTime = 0L;
/* 47 */     serverTime = 0L;
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onRender(Render2DEvent event) {
/* 52 */     ScaledResolution scaled = new ScaledResolution(Minecraft.func_71410_x());
/* 53 */     int sf = ResManager.getScaleFactor();
/* 54 */     int scaledX = (int)((scaled.func_78326_a() * sf) * this.xPos.getCurrent().intValue() / 100.0D);
/* 55 */     int scaledY = (int)((scaled.func_78328_b() * sf) * this.yPos.getCurrent().intValue() / 100.0D);
/* 56 */     if (this.rgb.isEnabled()) {
/* 57 */       FontUtils.normal.drawHVCenteredChromaString(String.format("TPS: %.1f", new Object[] { Double.valueOf(calcTps()) }), scaledX, scaledY);
/*    */     } else {
/* 59 */       FontUtils.normal.drawHVCenteredString(String.format("TPS: %.1f", new Object[] { Double.valueOf(calcTps()) }), scaledX, scaledY, -1);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onPacket(PacketReceivedEvent event) {
/* 65 */     if (event.packet instanceof S03PacketTimeUpdate) {
/* 66 */       S03PacketTimeUpdate s03packet = (S03PacketTimeUpdate)event.packet;
/*    */       
/* 68 */       if (systemTime == 0L) {
/* 69 */         systemTime = System.currentTimeMillis();
/* 70 */         serverTime = s03packet.func_149366_c();
/*    */       } else {
/* 72 */         long newSystemTime = System.currentTimeMillis();
/* 73 */         long newServerTime = s03packet.func_149366_c();
/* 74 */         float tps = (float)(serverTime - newServerTime) / (float)(systemTime - newSystemTime) / 50.0F * 20.0F;
/* 75 */         if (tps <= 20.0F)
/* 76 */           serverTPS.add(Float.valueOf(tps)); 
/* 77 */         systemTime = newSystemTime;
/* 78 */         serverTime = newServerTime;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onServerJoin(WorldUnloadEvent event) {
/* 85 */     serverTPS.clear();
/* 86 */     systemTime = 0L;
/* 87 */     serverTime = 0L;
/*    */   }
/*    */   
/*    */   private double calcTps() {
/* 91 */     for (; serverTPS.size() > 10; serverTPS.remove(0));
/* 92 */     return (new ArrayList(serverTPS)).stream().mapToDouble(x -> x.floatValue()).sum() / serverTPS.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\render\TPSViewer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */