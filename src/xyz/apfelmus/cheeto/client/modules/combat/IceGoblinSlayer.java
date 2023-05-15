/*     */ package xyz.apfelmus.cheeto.client.modules.combat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.util.Vec3;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Disable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cf4m.module.Category;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ @Module(name = "IceGoblinSlayer", category = Category.COMBAT)
/*     */ public class IceGoblinSlayer {
/*     */   @Setting(name = "AimTime", description = "Time it takes to aim")
/*  30 */   private IntegerSetting aimTime = new IntegerSetting(
/*  31 */       Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "ClickDelay", description = "Delay between clicks")
/*  32 */   private IntegerSetting clickDelay = new IntegerSetting(
/*  33 */       Integer.valueOf(200), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "AntiAFK", description = "Will do small movements")
/*  34 */   private BooleanSetting antiAfk = new BooleanSetting(true);
/*     */   @Setting(name = "Radius", description = "Radius to search for entities")
/*  36 */   private IntegerSetting radius = new IntegerSetting(
/*  37 */       Integer.valueOf(30), Integer.valueOf(0), Integer.valueOf(30)); @Setting(name = "ItemSlot", description = "Juju/Term/Frozen Scythe")
/*  38 */   private IntegerSetting itemSlot = new IntegerSetting(
/*  39 */       Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(8));
/*     */   
/*  41 */   private static Minecraft mc = Minecraft.func_71410_x();
/*  42 */   private static Entity currentMob = null;
/*  43 */   private static List<Entity> blacklist = new ArrayList<>();
/*  44 */   private static long curEnd = 0L;
/*  45 */   private static int ticks = 0;
/*     */   
/*     */   enum KillState {
/*  48 */     SELECT,
/*  49 */     AIM,
/*  50 */     KILL;
/*     */   }
/*     */   
/*     */   enum AfkState {
/*  54 */     LEFT,
/*  55 */     RIGHT;
/*     */   }
/*     */   
/*  58 */   private static KillState killState = KillState.SELECT;
/*  59 */   private static AfkState afkState = AfkState.LEFT;
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  63 */     curEnd = 0L;
/*  64 */     currentMob = null;
/*  65 */     killState = KillState.SELECT;
/*  66 */     afkState = AfkState.LEFT;
/*  67 */     blacklist.clear();
/*     */   }
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/*  72 */     if (this.antiAfk.isEnabled()) {
/*  73 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), false);
/*  74 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), false);
/*  75 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), false);
/*     */     } 
/*     */   } @Event
/*     */   public void onTick(ClientTickEvent event) {
/*     */     List<Entity> allPossible;
/*     */     int rongo;
/*  81 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*     */       return; 
/*  83 */     if (this.antiAfk.isEnabled()) {
/*  84 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74311_E.func_151463_i(), true);
/*  85 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), false);
/*  86 */       KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), false);
/*     */     } 
/*     */     
/*  89 */     if (++ticks > 40) {
/*  90 */       ticks = 0;
/*  91 */       blacklist.clear();
/*     */       
/*  93 */       if (this.antiAfk.isEnabled()) {
/*  94 */         switch (afkState) {
/*     */           case SELECT:
/*  96 */             KeyBinding.func_74510_a(mc.field_71474_y.field_74370_x.func_151463_i(), true);
/*  97 */             afkState = AfkState.RIGHT;
/*     */             break;
/*     */           
/*     */           case KILL:
/* 101 */             KeyBinding.func_74510_a(mc.field_71474_y.field_74366_z.func_151463_i(), true);
/* 102 */             afkState = AfkState.LEFT;
/*     */             break;
/*     */         } 
/*     */       
/*     */       }
/*     */     } 
/* 108 */     switch (killState) {
/*     */       case SELECT:
/* 110 */         allPossible = new ArrayList<>();
/* 111 */         rongo = this.radius.getCurrent().intValue();
/* 112 */         for (Entity e : mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72314_b(rongo, rongo, rongo), a -> a instanceof EntityArmorStand)) {
/* 113 */           if (Stream.<String>of(new String[] { "knifethrower", "goblin", "walker" }).noneMatch(v -> e.func_95999_t().toLowerCase().contains(v)))
/* 114 */             continue;  Entity mob = SkyblockUtils.getEntityCuttingOtherEntity(e, EntityOtherPlayerMP.class);
/* 115 */           if (mob == null)
/* 116 */             return;  if (!mc.field_71439_g.func_70685_l(mob) || 
/* 117 */             mob.field_70128_L || 
/* 118 */             mob.func_70032_d((Entity)mc.field_71439_g) >= this.radius.getCurrent().intValue() || 
/* 119 */             SkyblockUtils.getMobHp((EntityArmorStand)e) <= 0)
/*     */             continue; 
/* 121 */           if (!blacklist.contains(mob)) allPossible.add(mob);
/*     */         
/*     */         } 
/* 124 */         if (!allPossible.isEmpty()) {
/* 125 */           currentMob = Collections.<Entity>min(allPossible, Comparator.comparing(e2 -> Float.valueOf(e2.func_70032_d((Entity)mc.field_71439_g))));
/* 126 */           Vec3 vec = currentMob.func_174791_d();
/* 127 */           vec = vec.func_72441_c(0.0D, 1.0D, 0.0D);
/* 128 */           Rotation rot = RotationUtils.getRotation(vec);
/* 129 */           RotationUtils.setup(rot, Long.valueOf(this.aimTime.getCurrent().intValue()));
/* 130 */           curEnd = RotationUtils.endTime;
/* 131 */           killState = KillState.AIM;
/*     */         } 
/*     */         break;
/*     */       
/*     */       case KILL:
/* 136 */         SkyblockUtils.silentUse(this.itemSlot.getCurrent().intValue(), this.itemSlot.getCurrent().intValue());
/* 137 */         blacklist.add(currentMob);
/* 138 */         currentMob = null;
/* 139 */         killState = KillState.SELECT;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRender(Render3DEvent event) {
/* 146 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*     */       return; 
/* 148 */     if (killState == KillState.AIM) {
/* 149 */       if (currentMob.field_70128_L) {
/* 150 */         blacklist.add(currentMob);
/* 151 */         currentMob = null;
/* 152 */         killState = KillState.SELECT;
/*     */         return;
/*     */       } 
/* 155 */       if (System.currentTimeMillis() <= curEnd + this.clickDelay.getCurrent().intValue()) {
/* 156 */         RotationUtils.update();
/*     */       } else {
/* 158 */         killState = KillState.KILL;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\IceGoblinSlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */