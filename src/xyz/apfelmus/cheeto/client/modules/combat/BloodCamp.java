/*    */ package xyz.apfelmus.cheeto.client.modules.combat;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.Vec3;
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*    */ import xyz.apfelmus.cheeto.client.events.WorldUnloadEvent;
/*    */ import xyz.apfelmus.cheeto.client.settings.FloatSetting;
/*    */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*    */ 
/*    */ 
/*    */ @Module(name = "BloodCamp", category = Category.COMBAT)
/*    */ public class BloodCamp
/*    */ {
/*    */   @Setting(name = "AimTime", description = "Time it takes to aim")
/* 27 */   private IntegerSetting aimTime = new IntegerSetting(
/* 28 */       Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "ClickDelay", description = "Time beteween rotation and click")
/* 29 */   private IntegerSetting clickDelay = new IntegerSetting(
/* 30 */       Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "AimLow", description = "Aim specified value under mob")
/* 31 */   private FloatSetting aimLow = new FloatSetting(
/* 32 */       Float.valueOf(1.0F), Float.valueOf(0.0F), Float.valueOf(5.0F));
/*    */   
/* 34 */   private static Minecraft mc = Minecraft.func_71410_x();
/* 35 */   private List<String> names = new ArrayList<>(Arrays.asList(new String[] { "Revoker", "Psycho", "Reaper", "Parasite", "Cannibal", "Mute", "Ooze", "Putrid", "Freak", "Leech", "Flamer", "Tear", "Skull", "Mr. Dead", "Vader", "Frost", "Walker" }));
/* 36 */   private List<String> clickedNames = new ArrayList<>();
/* 37 */   private KillState ks = KillState.SELECT;
/* 38 */   private long curEnd = 0L;
/*    */   
/*    */   enum KillState {
/* 41 */     SELECT,
/* 42 */     AIM,
/* 43 */     KILL;
/*    */   }
/*    */   
/*    */   @Enable
/*    */   public void onEnable() {
/* 48 */     this.curEnd = 0L;
/* 49 */     this.clickedNames.clear();
/* 50 */     this.ks = KillState.SELECT;
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onRender(Render3DEvent event) {
/* 55 */     switch (this.ks) {
/*    */       case SELECT:
/* 57 */         for (Entity e : mc.field_71441_e.field_72996_f) {
/* 58 */           if (e instanceof net.minecraft.client.entity.EntityOtherPlayerMP && 
/* 59 */             !this.clickedNames.contains(e.func_70005_c_().trim()) && this.names.contains(e.func_70005_c_().trim())) {
/* 60 */             Vec3 vec = e.func_174791_d();
/* 61 */             vec = vec.func_72441_c(0.0D, (-1.0F * this.aimLow.getCurrent().floatValue()), 0.0D);
/* 62 */             Rotation rot = RotationUtils.getRotation(vec);
/* 63 */             RotationUtils.setup(rot, Long.valueOf(this.aimTime.getCurrent().intValue()));
/* 64 */             this.curEnd = RotationUtils.endTime;
/* 65 */             this.clickedNames.add(e.func_70005_c_().trim());
/* 66 */             this.ks = KillState.AIM;
/*    */           } 
/*    */         } 
/*    */         break;
/*    */ 
/*    */       
/*    */       case AIM:
/* 73 */         if (System.currentTimeMillis() <= this.curEnd + this.clickDelay.getCurrent().intValue()) {
/* 74 */           RotationUtils.update(); break;
/*    */         } 
/* 76 */         this.ks = KillState.KILL;
/*    */         break;
/*    */ 
/*    */       
/*    */       case KILL:
/* 81 */         KeyBinding.func_74507_a(mc.field_71474_y.field_74312_F.func_151463_i());
/* 82 */         this.ks = KillState.SELECT;
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   @Event
/*    */   public void onWorldLoad(WorldUnloadEvent event) {
/* 89 */     this.clickedNames.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\BloodCamp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */