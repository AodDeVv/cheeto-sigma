/*     */ package xyz.apfelmus.cheeto.client.modules.render;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.monster.EntityBlaze;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntitySpider;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cf4m.module.Category;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.FloatSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ColorUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render3DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ 
/*     */ @Module(name = "ESP", category = Category.RENDER)
/*     */ public class ESP
/*     */ {
/*     */   @Setting(name = "StarredMobs")
/*  30 */   private BooleanSetting starredMobs = new BooleanSetting(true);
/*     */   @Setting(name = "Bats")
/*  32 */   private BooleanSetting bats = new BooleanSetting(true);
/*     */   @Setting(name = "Dragon")
/*  34 */   private BooleanSetting dragon = new BooleanSetting(true);
/*     */   @Setting(name = "Arrows")
/*  36 */   private BooleanSetting arrows = new BooleanSetting(true);
/*     */   @Setting(name = "CrystalHollows", description = "Will show chests")
/*  38 */   private BooleanSetting crystalHollows = new BooleanSetting(true);
/*     */   @Setting(name = "Revenant")
/*  40 */   private BooleanSetting revenant = new BooleanSetting(true);
/*     */   @Setting(name = "Tarantula")
/*  42 */   private BooleanSetting tarantula = new BooleanSetting(true);
/*     */   @Setting(name = "Sven")
/*  44 */   private BooleanSetting sven = new BooleanSetting(true);
/*     */   @Setting(name = "Voidgloom")
/*  46 */   private BooleanSetting voidgloom = new BooleanSetting(true);
/*     */   @Setting(name = "Blaze")
/*  48 */   private BooleanSetting blaze = new BooleanSetting(true);
/*     */   @Setting(name = "Fishing", description = "Will highlight Guardians and Golems")
/*  50 */   private BooleanSetting fishing = new BooleanSetting(true);
/*     */   @Setting(name = "BoxOpacity")
/*  52 */   public FloatSetting boxOpacity = new FloatSetting(
/*  53 */       Float.valueOf(0.3F), Float.valueOf(0.0F), Float.valueOf(1.0F));
/*     */   
/*  55 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*  57 */   private static Map<String, Integer> voidglooms = new HashMap<String, Integer>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*  63 */   private static Map<String, Integer> svens = new HashMap<String, Integer>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*  69 */   private static Map<String, Integer> tarantulas = new HashMap<String, Integer>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*  75 */   private static Map<String, Integer> revenants = new HashMap<String, Integer>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private static Map<String, Integer> blazes = new HashMap<String, Integer>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onESP(Render3DEvent event) {
/*  91 */     if (this.starredMobs.isEnabled() || this.bats.isEnabled() || this.dragon.isEnabled() || this.arrows.isEnabled()) {
/*  92 */       for (Iterator<Entity> iterator = mc.field_71441_e.field_72996_f.iterator(); iterator.hasNext(); ) { Entity e = iterator.next();
/*  93 */         if (this.starredMobs.isEnabled() && 
/*  94 */           e instanceof net.minecraft.entity.item.EntityArmorStand && !e.field_70128_L && e.func_95999_t().contains("✯")) {
/*  95 */           Render3DUtils.renderStarredMobBoundingBox(e, event.partialTicks);
/*     */         }
/*     */ 
/*     */         
/*  99 */         if (this.bats.isEnabled() && 
/* 100 */           e instanceof net.minecraft.entity.passive.EntityBat && !e.field_70128_L) {
/* 101 */           Render3DUtils.renderBoundingBox(e, event.partialTicks, -16711936);
/*     */         }
/*     */ 
/*     */         
/* 105 */         if (this.dragon.isEnabled() && 
/* 106 */           e instanceof net.minecraft.entity.boss.EntityDragon) {
/* 107 */           for (Entity part : e.func_70021_al()) {
/* 108 */             Render3DUtils.renderBoundingBox(part, event.partialTicks, ColorUtils.getChroma(3000.0F, 0));
/*     */           }
/*     */         }
/*     */ 
/*     */         
/* 113 */         if (this.arrows.isEnabled() && 
/* 114 */           e instanceof net.minecraft.entity.projectile.EntityArrow) {
/* 115 */           Render3DUtils.renderBoundingBox(e, event.partialTicks, -1);
/*     */         }
/*     */ 
/*     */         
/* 119 */         if (e instanceof net.minecraft.entity.item.EntityArmorStand) {
/* 120 */           if (this.revenant.isEnabled()) {
/* 121 */             revenants.forEach((k, v) -> {
/*     */                   if (!e.field_70128_L && e.func_95999_t().contains(k)) {
/*     */                     Entity yep = SkyblockUtils.getEntityCuttingOtherEntity(e, EntityZombie.class);
/*     */                     
/*     */                     if (yep != null) {
/*     */                       Render3DUtils.renderMiniBoundingBox(yep, event.partialTicks, v.intValue());
/*     */                     }
/*     */                   } 
/*     */                 });
/*     */           }
/* 131 */           if (this.tarantula.isEnabled()) {
/* 132 */             tarantulas.forEach((k, v) -> {
/*     */                   if (!e.field_70128_L && e.func_95999_t().contains(k)) {
/*     */                     Entity yep = SkyblockUtils.getEntityCuttingOtherEntity(e, EntitySpider.class);
/*     */                     
/*     */                     if (yep != null) {
/*     */                       Render3DUtils.renderMiniBoundingBox(yep, event.partialTicks, v.intValue());
/*     */                     }
/*     */                   } 
/*     */                 });
/*     */           }
/* 142 */           if (this.sven.isEnabled()) {
/* 143 */             svens.forEach((k, v) -> {
/*     */                   if (!e.field_70128_L && e.func_95999_t().contains(k)) {
/*     */                     Entity yep = SkyblockUtils.getEntityCuttingOtherEntity(e, EntityWolf.class);
/*     */                     
/*     */                     if (yep != null) {
/*     */                       Render3DUtils.renderMiniBoundingBox(yep, event.partialTicks, v.intValue());
/*     */                     }
/*     */                   } 
/*     */                 });
/*     */           }
/* 153 */           if (this.voidgloom.isEnabled()) {
/* 154 */             voidglooms.forEach((k, v) -> {
/*     */                   if (!e.field_70128_L && e.func_95999_t().contains(k)) {
/*     */                     Entity yep = SkyblockUtils.getEntityCuttingOtherEntity(e, EntityEnderman.class);
/*     */                     
/*     */                     if (yep != null) {
/*     */                       Render3DUtils.renderMiniBoundingBox(yep, event.partialTicks, v.intValue());
/*     */                     }
/*     */                   } 
/*     */                 });
/*     */           }
/* 164 */           if (this.blaze.isEnabled()) {
/* 165 */             blazes.forEach((k, v) -> {
/*     */                   if (!e.field_70128_L && e.func_95999_t().contains(k)) {
/*     */                     Entity yep = SkyblockUtils.getEntityCuttingOtherEntity(e, EntityBlaze.class);
/*     */                     
/*     */                     if (yep != null) {
/*     */                       Render3DUtils.renderMiniBoundingBox(yep, event.partialTicks, v.intValue());
/*     */                     }
/*     */                   } 
/*     */                 });
/*     */           }
/*     */         } 
/* 176 */         if (this.fishing.isEnabled()) {
/* 177 */           if (e instanceof net.minecraft.entity.monster.EntityGuardian) {
/* 178 */             Render3DUtils.renderBoundingBox(e, event.partialTicks, -16711681); continue;
/* 179 */           }  if (e instanceof net.minecraft.entity.monster.EntityIronGolem) {
/* 180 */             Render3DUtils.renderBoundingBox(e, event.partialTicks, -8355712);
/*     */           }
/*     */         }  }
/*     */     
/*     */     }
/*     */     
/* 186 */     if (this.crystalHollows.isEnabled())
/* 187 */       for (TileEntity te : (Minecraft.func_71410_x()).field_71441_e.field_147482_g) {
/* 188 */         if (te instanceof net.minecraft.tileentity.TileEntityChest)
/* 189 */           Render3DUtils.drawChestOutline(te.func_174877_v()); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\render\ESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */