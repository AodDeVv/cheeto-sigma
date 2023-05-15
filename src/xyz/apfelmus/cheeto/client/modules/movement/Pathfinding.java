/*     */ package xyz.apfelmus.cheeto.client.modules.movement;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Disable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ColorUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.KeybindUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.TimeHelper;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.VecUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.pathfinding.Pathfinder;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render3DUtils;
/*     */ 
/*     */ @Module(name = "Pathfinding", description = "Do not toggle this module, it won't work", category = Category.MOVEMENT)
/*     */ public class Pathfinding {
/*     */   @Setting(name = "UnstuckTime")
/*  28 */   private IntegerSetting unstucktime = new IntegerSetting(
/*  29 */       Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(10)); @Setting(name = "LookTime")
/*  30 */   private IntegerSetting lookTime = new IntegerSetting(
/*  31 */       Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(1000));
/*     */   
/*  33 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*  35 */   private static int stuckTicks = 0;
/*     */   
/*     */   private static BlockPos oldPos;
/*     */   private static BlockPos curPos;
/*     */   private static TimeHelper unstucker;
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  43 */     stuckTicks = 0;
/*  44 */     oldPos = null;
/*  45 */     curPos = null;
/*     */     
/*  47 */     if (!Pathfinder.hasPath()) {
/*  48 */       ChatUtils.send("Pussy bitch, no path found", new String[0]);
/*  49 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */     } else {
/*  51 */       ChatUtils.send("Navigating to: " + Pathfinder.getGoal(), new String[0]);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/*  57 */     Pathfinder.path = null;
/*  58 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), false);
/*  59 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), false);
/*  60 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74351_w.func_151463_i(), false);
/*  61 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74368_y.func_151463_i(), false);
/*  62 */     KeyBinding.func_74510_a(mc.field_71474_y.field_74314_A.func_151463_i(), false);
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/*  67 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*     */       return; 
/*  69 */     if (Pathfinder.hasPath()) {
/*  70 */       if (++stuckTicks >= this.unstucktime.getCurrent().intValue() * 20) {
/*  71 */         curPos = mc.field_71439_g.func_180425_c();
/*  72 */         if (oldPos != null && Math.sqrt(curPos.func_177951_i((Vec3i)oldPos)) <= 0.1D) {
/*  73 */           KeyBinding.func_74510_a(mc.field_71474_y.field_74314_A.func_151463_i(), true);
/*  74 */           KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), true);
/*  75 */           unstucker = new TimeHelper();
/*  76 */           unstucker.reset();
/*     */           return;
/*     */         } 
/*  79 */         oldPos = curPos;
/*  80 */         stuckTicks = 0;
/*     */       } 
/*     */       
/*  83 */       if (unstucker != null && unstucker.hasReached(2000L)) {
/*  84 */         KeyBinding.func_74510_a(mc.field_71474_y.field_74314_A.func_151463_i(), false);
/*  85 */         KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), false);
/*  86 */         unstucker = null;
/*     */       } 
/*     */       
/*  89 */       Vec3 first = Pathfinder.getCurrent().func_72441_c(0.5D, 0.0D, 0.5D);
/*     */       
/*  91 */       Rotation needed = RotationUtils.getRotation(first);
/*  92 */       needed.setPitch(mc.field_71439_g.field_70125_A);
/*     */       
/*  94 */       if (VecUtils.getHorizontalDistance(mc.field_71439_g.func_174791_d(), first) > 0.69D) {
/*  95 */         if (RotationUtils.done && needed.getYaw() < 135.0F) {
/*  96 */           RotationUtils.setup(needed, Long.valueOf(this.lookTime.getCurrent().intValue()));
/*     */         }
/*     */         
/*  99 */         if (Pathfinder.hasNext()) {
/* 100 */           Vec3 next = Pathfinder.getNext().func_72441_c(0.5D, 0.0D, 0.5D);
/* 101 */           double xDiff = Math.abs(Math.abs(next.field_72450_a) - Math.abs(first.field_72450_a));
/* 102 */           double zDiff = Math.abs(Math.abs(next.field_72449_c) - Math.abs(first.field_72449_c));
/* 103 */           mc.field_71439_g.func_70031_b(((xDiff == 1.0D && zDiff == 0.0D) || (xDiff == 0.0D && zDiff == 1.0D)));
/*     */         } 
/*     */         
/* 106 */         Vec3 lastTick = new Vec3(mc.field_71439_g.field_70142_S, mc.field_71439_g.field_70137_T, mc.field_71439_g.field_70136_U);
/* 107 */         Vec3 diffy = mc.field_71439_g.func_174791_d().func_178788_d(lastTick);
/* 108 */         diffy = diffy.func_72441_c(diffy.field_72450_a * 4.0D, 0.0D, diffy.field_72449_c * 4.0D);
/* 109 */         Vec3 nextTick = mc.field_71439_g.func_174791_d().func_178787_e(diffy);
/*     */         
/* 111 */         KeybindUtils.stopMovement();
/* 112 */         List<KeyBinding> neededPresses = VecUtils.getNeededKeyPresses(mc.field_71439_g.func_174791_d(), first);
/*     */         
/* 114 */         if (Math.abs(nextTick.func_72438_d(first) - mc.field_71439_g.func_174791_d().func_72438_d(first)) <= 0.05D || nextTick.func_72438_d(first) <= mc.field_71439_g.func_174791_d().func_72438_d(first)) {
/* 115 */           neededPresses.forEach(v -> KeyBinding.func_74510_a(v.func_151463_i(), true));
/*     */         }
/*     */         
/* 118 */         if (Math.abs(mc.field_71439_g.field_70163_u - first.field_72448_b) > 0.5D) {
/* 119 */           KeyBinding.func_74510_a(mc.field_71474_y.field_74314_A.func_151463_i(), (mc.field_71439_g.field_70163_u < first.field_72448_b));
/*     */         } else {
/* 121 */           KeyBinding.func_74510_a(mc.field_71474_y.field_74314_A.func_151463_i(), false);
/*     */         } 
/*     */       } else {
/* 124 */         RotationUtils.reset();
/* 125 */         if (!Pathfinder.goNext()) {
/* 126 */           KeybindUtils.stopMovement();
/*     */         }
/*     */       }
/*     */     
/* 130 */     } else if (CF4M.INSTANCE.moduleManager.isEnabled(this)) {
/* 131 */       ChatUtils.send("Done navigating", new String[0]);
/* 132 */       CF4M.INSTANCE.moduleManager.toggle(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onRender(Render3DEvent event) {
/* 139 */     if (Pathfinder.path != null && !Pathfinder.path.isEmpty()) {
/* 140 */       Render3DUtils.drawLines(Pathfinder.path, 2.0F, event.partialTicks);
/* 141 */       Vec3 last = ((Vec3)Pathfinder.path.get(Pathfinder.path.size() - 1)).func_72441_c(0.0D, -1.0D, 0.0D);
/* 142 */       Render3DUtils.renderEspBox(new BlockPos(last), event.partialTicks, ColorUtils.getChroma(3000.0F, (int)(last.field_72450_a + last.field_72448_b + last.field_72449_c)));
/*     */     } 
/*     */     
/* 145 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*     */       return; 
/* 147 */     if (!RotationUtils.done)
/* 148 */       RotationUtils.update(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\movement\Pathfinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */