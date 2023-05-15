/*     */ package xyz.apfelmus.cheeto.client.modules.world;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientChatReceivedEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.PlayerInteractEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.FloatSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render3DUtils;
/*     */ 
/*     */ @Module(name = "FairySoulAura", category = Category.WORLD)
/*     */ public class FairySoulAura {
/*     */   @Setting(name = "ClickRange", description = "If it doesn't work, reduce range")
/*  32 */   private FloatSetting clickRange = new FloatSetting(
/*  33 */       Float.valueOf(2.75F), Float.valueOf(0.0F), Float.valueOf(5.0F)); @Setting(name = "ClickSlot")
/*  34 */   private IntegerSetting clickSlot = new IntegerSetting(
/*  35 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "ClickDelay")
/*  36 */   private FloatSetting clickDelay = new FloatSetting(
/*  37 */       Float.valueOf(500.0F), Float.valueOf(0.0F), Float.valueOf(2500.0F)); @Setting(name = "Stonkless")
/*  38 */   private BooleanSetting stonkless = new BooleanSetting(true);
/*     */ 
/*     */   
/*  41 */   private static Minecraft mc = Minecraft.func_71410_x();
/*  42 */   private static List<Entity> clicked = new ArrayList<>();
/*  43 */   private List<Entity> soulsNear = new ArrayList<>();
/*  44 */   private List<Entity> foundSouls = new ArrayList<>();
/*     */   private Entity selected;
/*  46 */   private Method syncCurrentPlayItem = null;
/*     */   
/*     */   private long lastClickTime;
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  52 */     if (this.syncCurrentPlayItem == null) {
/*     */       try {
/*  54 */         this.syncCurrentPlayItem = PlayerControllerMP.class.getDeclaredMethod("syncCurrentPlayItem", new Class[0]);
/*  55 */       } catch (NoSuchMethodException e) {
/*     */         try {
/*  57 */           this.syncCurrentPlayItem = PlayerControllerMP.class.getDeclaredMethod("func_78750_j", new Class[0]);
/*  58 */         } catch (NoSuchMethodException noSuchMethodException) {}
/*     */       } 
/*     */     }
/*     */     
/*  62 */     if (this.syncCurrentPlayItem != null) {
/*  63 */       this.syncCurrentPlayItem.setAccessible(true);
/*     */     }
/*  65 */     this.soulsNear.clear();
/*  66 */     this.foundSouls.clear();
/*  67 */     clicked.clear();
/*  68 */     this.selected = null;
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/*  73 */     if (mc.field_71441_e != null && mc.field_71439_g != null) {
/*  74 */       for (Entity e : mc.field_71441_e.field_72996_f) {
/*  75 */         if (!this.soulsNear.contains(e) && 
/*  76 */           e instanceof EntityArmorStand) {
/*  77 */           ItemStack stack = ((EntityArmorStand)e).func_82169_q(3);
/*     */           
/*  79 */           if (stack != null && stack.func_82833_r().equals("Head")) {
/*  80 */             NBTTagCompound skullOwner = stack.func_179543_a("SkullOwner", false);
/*     */             
/*  82 */             if (skullOwner != null && 
/*  83 */               skullOwner.func_74779_i("Id").equals("57a4c8dc-9b8e-3d41-80da-a608901a6147") && 
/*  84 */               !this.foundSouls.contains(e)) {
/*  85 */               this.soulsNear.add(e);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  93 */       if (!this.stonkless.isEnabled()) {
/*  94 */         for (Entity e : new ArrayList(this.soulsNear)) {
/*  95 */           Vec3 eyes = mc.field_71439_g.func_174824_e(1.0F);
/*  96 */           if (clicked.contains(e))
/*     */             continue; 
/*  98 */           if (e.func_70011_f(eyes.field_72450_a, eyes.field_72448_b - 2.0D, eyes.field_72449_c) < this.clickRange.getCurrent().floatValue()) {
/*  99 */             if ((float)(System.currentTimeMillis() - this.lastClickTime) >= this.clickDelay.getCurrent().floatValue()) {
/* 100 */               handleClick(e);
/* 101 */               clicked.add(e);
/*     */             } 
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 107 */         clicked.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRender(Render3DEvent event) {
/* 114 */     this.selected = null;
/* 115 */     for (Entity e : new ArrayList(this.soulsNear)) {
/* 116 */       if (this.selected == null && RotationUtils.lookingAt(e.func_180425_c().func_177982_a(0, 1, 0), this.clickRange.getCurrent().floatValue())) {
/* 117 */         this.selected = e;
/*     */         
/*     */         continue;
/*     */       } 
/* 121 */       if (!clicked.contains(e)) {
/* 122 */         Render3DUtils.drawFairySoulOutline(e, event.partialTicks, -1); continue;
/*     */       } 
/* 124 */       Render3DUtils.drawFairySoulOutline(e, event.partialTicks, -12345273);
/*     */     } 
/*     */ 
/*     */     
/* 128 */     if (this.selected != null) {
/* 129 */       Render3DUtils.drawFairySoulOutline(this.selected, event.partialTicks, -19712);
/*     */     }
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onInteract(PlayerInteractEvent event) {
/* 135 */     if (this.stonkless.isEnabled() && 
/* 136 */       this.selected != null && !clicked.contains(this.selected)) {
/* 137 */       MovingObjectPosition omo = mc.field_71476_x;
/* 138 */       if (omo != null && 
/* 139 */         omo.field_72313_a == MovingObjectPosition.MovingObjectType.ENTITY && omo.field_72308_g.equals(this.selected)) {
/*     */         return;
/*     */       }
/*     */       
/* 143 */       if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
/* 144 */         clicked.add(this.selected);
/* 145 */         handleClick(this.selected);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onWorldLoad(WorldUnloadEvent event) {
/* 153 */     clicked.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onChat(ClientChatReceivedEvent event) {
/* 160 */     String msg = StringUtils.func_76338_a(event.message.func_150260_c());
/*     */     
/* 162 */     if (msg.equals("SOUL! You found a Fairy Soul!") || msg.equals("You have already found that Fairy Soul!")) {
/* 163 */       Optional<Entity> nearest = this.soulsNear.stream().min(Comparator.comparing(a -> Float.valueOf(a.func_70032_d((Entity)mc.field_71439_g))));
/* 164 */       nearest.ifPresent(e -> {
/*     */             this.foundSouls.add(e);
/*     */             this.soulsNear.remove(e);
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleClick(Entity e) {
/* 172 */     this.lastClickTime = System.currentTimeMillis();
/* 173 */     if (mc.field_71476_x == null) {
/*     */       return;
/*     */     }
/*     */     
/* 177 */     MovingObjectPosition movingObject = mc.field_71476_x;
/*     */     
/* 179 */     if (this.clickSlot.getCurrent().intValue() != 0) {
/* 180 */       int holding = mc.field_71439_g.field_71071_by.field_70461_c;
/* 181 */       mc.field_71439_g.field_71071_by.field_70461_c = this.clickSlot.getCurrent().intValue();
/* 182 */       syncItem();
/* 183 */       Vec3 vec3 = new Vec3(movingObject.field_72307_f.field_72450_a - e.field_70165_t, movingObject.field_72307_f.field_72448_b - e.field_70163_u, movingObject.field_72307_f.field_72449_c - e.field_70161_v);
/* 184 */       mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity(e, vec3));
/* 185 */       e.func_174825_a((EntityPlayer)mc.field_71439_g, vec3);
/* 186 */       mc.field_71439_g.field_71071_by.field_70461_c = holding;
/*     */     } else {
/* 188 */       syncItem();
/* 189 */       Vec3 vec3 = new Vec3(movingObject.field_72307_f.field_72450_a - e.field_70165_t, movingObject.field_72307_f.field_72448_b - e.field_70163_u, movingObject.field_72307_f.field_72449_c - e.field_70161_v);
/* 190 */       mc.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity(e, vec3));
/*     */     } 
/*     */     
/* 193 */     if (!this.stonkless.isEnabled()) {
/* 194 */       this.soulsNear.remove(e);
/* 195 */       clicked.add(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void syncItem() {
/* 200 */     if (this.syncCurrentPlayItem != null)
/*     */       try {
/* 202 */         this.syncCurrentPlayItem.invoke(mc.field_71442_b, new Object[0]);
/* 203 */       } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 204 */         e.printStackTrace();
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\world\FairySoulAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */