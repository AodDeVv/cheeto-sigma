/*    */ package xyz.apfelmus.cheeto.client.modules.combat;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.Loader;
/*    */ import net.minecraftforge.fml.common.ModContainer;
/*    */ import org.apache.commons.codec.digest.DigestUtils;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.CheetoStatus;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.InventoryUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*    */ 
/*    */ 
/*    */ @Module(name = "TermClicker", category = Category.COMBAT)
/*    */ public class TermClicker
/*    */ {
/*    */   @Setting(name = "CPS", description = "Clicks per second")
/* 30 */   private IntegerSetting cps = new IntegerSetting(
/* 31 */       Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(50)); @Setting(name = "Silent", description = "Will click your Term without being on the Slot")
/* 32 */   private BooleanSetting silent = new BooleanSetting(true);
/*    */   @Setting(name = "Mode", description = "Hold needs a keybind, will click when you hold the key")
/* 34 */   private ModeSetting mode = new ModeSetting("Hold", 
/* 35 */       Arrays.asList(new String[] { "Hold", "Toggle" })); @Setting(name = "Weapon", description = "Slot of the Weapon")
/* 36 */   private ModeSetting weapon = new ModeSetting("Terminator", 
/* 37 */       Arrays.asList(new String[] { "Terminator", "Juju Shortbow" }));
/*    */   
/* 39 */   private static long lastClickTime = 0L;
/*    */   
/* 41 */   private static Minecraft mc = Minecraft.func_71410_x();
/*    */   
/*    */   public static boolean jkKp660roUwi9fB1() {
/* 44 */     if (!CheetoStatus.yep) return false; 
/* 45 */     ModContainer mod = (ModContainer)Loader.instance().getIndexedModList().get("ChromaHUD");
/* 46 */     if (mod != null) {
/* 47 */       File f = mod.getSource();
/* 48 */       if (f != null) {
/*    */         try {
/* 50 */           return DigestUtils.sha1Hex(Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0])).equals(CheetoStatus.yepCock);
/* 51 */         } catch (IOException iOException) {}
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 56 */     return false;
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onRenderTick(Render3DEvent event) {
/* 61 */     if (this.mode.getCurrent().equals("Hold")) {
/* 62 */       if (Keyboard.isKeyDown(CF4M.INSTANCE.moduleManager.getKey(this))) {
/* 63 */         clickTermIfNeeded();
/*    */       } else {
/* 65 */         CF4M.INSTANCE.moduleManager.toggle(this);
/*    */       } 
/* 67 */     } else if (this.mode.getCurrent().equals("Toggle")) {
/* 68 */       clickTermIfNeeded();
/*    */     } 
/*    */   }
/*    */   
/*    */   private void clickTermIfNeeded() {
/* 73 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*    */       return; 
/* 75 */     float acTime = 1000.0F / this.cps.getCurrent().intValue();
/* 76 */     if ((float)(System.currentTimeMillis() - lastClickTime) >= acTime) {
/* 77 */       int termSlot = InventoryUtils.getItemInHotbar(this.weapon.getCurrent());
/*    */       
/* 79 */       if (termSlot != -1) {
/* 80 */         SkyblockUtils.silentUse(this.silent.isEnabled() ? (mc.field_71439_g.field_71071_by.field_70461_c + 1) : (termSlot + 1), termSlot + 1);
/*    */       }
/* 82 */       lastClickTime = System.currentTimeMillis();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\TermClicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */