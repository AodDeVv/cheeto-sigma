/*     */ package xyz.apfelmus.cheeto.client.modules.world;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.Vec3;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Disable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientChatReceivedEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.ModeSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChadUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.KeybindUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.Rotation;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.TimeHelper;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.InventoryUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ @Module(name = "AutoForaging", category = Category.WORLD)
/*     */ public class AutoForaging {
/*     */   @Setting(name = "SaplingSlot", description = "Slot for Saplings")
/*  34 */   private IntegerSetting saplingSlot = new IntegerSetting(
/*  35 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "BoneMealSlot", description = "Slot for Ench Bonemeal")
/*  36 */   private IntegerSetting boneMealSlot = new IntegerSetting(
/*  37 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "AxeSlot", description = "Slot for your Tree Cap")
/*  38 */   private IntegerSetting axeSlot = new IntegerSetting(
/*  39 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "RodSlot", description = "Slot for Rod for Pet Swapping")
/*  40 */   private IntegerSetting rodSlot = new IntegerSetting(
/*  41 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "Delay")
/*  42 */   private IntegerSetting delay = new IntegerSetting(
/*  43 */       Integer.valueOf(200), Integer.valueOf(0), Integer.valueOf(1000)); @Setting(name = "Direction")
/*  44 */   private ModeSetting direction = new ModeSetting("NORTH", new ArrayList(
/*  45 */         Arrays.asList((Object[])new String[] { "NORTH", "EAST", "SOUTH", "WEST" }))); @Setting(name = "Ungrab", description = "Automatically tabs out")
/*  46 */   private BooleanSetting ungrab = new BooleanSetting(true);
/*     */ 
/*     */   
/*  49 */   private Minecraft mc = Minecraft.func_71410_x(); private TimeHelper yepTimer;
/*     */   private TimeHelper failSafeTimer;
/*     */   private ForagingState foragingState;
/*     */   private int currentTree;
/*     */   private int treeWait;
/*     */   
/*  55 */   enum ForagingState { TREE,
/*  56 */     BONEMEAL,
/*  57 */     RODSWAP,
/*  58 */     HARVEST,
/*  59 */     LOOKING; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  68 */     this.foragingState = ForagingState.TREE;
/*  69 */     this.currentTree = 1;
/*  70 */     this.yepTimer = new TimeHelper();
/*  71 */     this.failSafeTimer = new TimeHelper();
/*  72 */     this.treeWait = this.delay.getCurrent().intValue();
/*     */     
/*  74 */     if (this.ungrab.isEnabled())
/*  75 */       ChadUtils.ungrabMouse(); 
/*     */   }
/*     */   
/*     */   @Disable
/*     */   public void onDisable() {
/*  80 */     if (this.ungrab.isEnabled())
/*  81 */       ChadUtils.regrabMouse(); 
/*     */   } @Event
/*     */   public void onTick(ClientTickEvent event) {
/*     */     BlockPos bp;
/*     */     IBlockState ibs;
/*  86 */     if (!SkyblockUtils.isInIsland()) {
/*  87 */       this.mc.field_71439_g.func_85030_a("random.orb", 1.0F, 1.0F);
/*     */       
/*     */       return;
/*     */     } 
/*  91 */     int saplingCount = Math.max(InventoryUtils.getAmountInHotbar("Jungle Sapling"), InventoryUtils.getAmountInHotbar("Dark Oak Sapling"));
/*  92 */     int boneMealCount = InventoryUtils.getAmountInHotbar("Enchanted Bone Meal");
/*     */     
/*  94 */     if (saplingCount < 5 || boneMealCount < 2)
/*     */       return; 
/*  96 */     ItemStack heldItem = this.mc.field_71439_g.func_70694_bm();
/*  97 */     if (heldItem != null && (
/*  98 */       heldItem.func_77973_b() == Items.field_151098_aY || heldItem.func_77973_b() == Items.field_151148_bJ)) {
/*  99 */       this.mc.field_71439_g.func_85030_a("random.orb", 1.0F, 1.0F);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 104 */     int xMod = 0;
/* 105 */     int zMod = 0;
/*     */     
/* 107 */     switch (this.direction.getCurrent()) {
/*     */       case "NORTH":
/* 109 */         zMod = -2;
/*     */         break;
/*     */       
/*     */       case "EAST":
/* 113 */         xMod = 3;
/*     */         break;
/*     */       
/*     */       case "SOUTH":
/* 117 */         xMod = 1;
/* 118 */         zMod = 3;
/*     */         break;
/*     */       
/*     */       case "WEST":
/* 122 */         xMod = -2;
/* 123 */         zMod = 1;
/*     */         break;
/*     */     } 
/*     */     
/* 127 */     Vec3 posVec = this.mc.field_71439_g.func_174791_d();
/* 128 */     Vec3 middle = (new Vec3(MathHelper.func_76128_c(posVec.field_72450_a), MathHelper.func_76128_c(posVec.field_72448_b), MathHelper.func_76128_c(posVec.field_72449_c))).func_178787_e(new Vec3(xMod, 1.0D, zMod));
/*     */     
/* 130 */     Rotation rot = RotationUtils.getRotation(middle);
/* 131 */     switch (this.foragingState) {
/*     */       case TREE:
/* 133 */         if (this.yepTimer.hasReached(this.treeWait)) {
/* 134 */           swapSlot(this.saplingSlot.getCurrent().intValue());
/* 135 */           if (this.currentTree == 1) {
/* 136 */             rot.addYaw(1.5F);
/* 137 */             rot.addPitch(-0.5F);
/* 138 */           } else if (this.currentTree == 2) {
/* 139 */             rot.addYaw(-1.0F);
/* 140 */             rot.addPitch(-1.0F);
/* 141 */           } else if (this.currentTree == 3) {
/* 142 */             rot.addYaw(-1.5F);
/* 143 */             rot.addPitch(0.5F);
/* 144 */           } else if (this.currentTree == 4) {
/* 145 */             rot.addYaw(0.5F);
/* 146 */             rot.addPitch(0.5F);
/*     */           } 
/*     */           
/* 149 */           RotationUtils.setup(rot, Long.valueOf(this.delay.getCurrent().intValue() / 2L));
/* 150 */           this.foragingState = ForagingState.LOOKING;
/*     */         } 
/*     */         break;
/*     */       
/*     */       case BONEMEAL:
/* 155 */         if (this.yepTimer.hasReached(this.delay.getCurrent().intValue())) {
/* 156 */           swapSlot(this.boneMealSlot.getCurrent().intValue());
/* 157 */           KeybindUtils.rightClick();
/* 158 */           this.yepTimer.reset();
/* 159 */           this.foragingState = ForagingState.RODSWAP;
/*     */         } 
/*     */         break;
/*     */       
/*     */       case RODSWAP:
/* 164 */         if (this.yepTimer.hasReached(this.delay.getCurrent().intValue())) {
/* 165 */           SkyblockUtils.silentUse(this.axeSlot.getCurrent().intValue(), this.rodSlot.getCurrent().intValue());
/* 166 */           KeybindUtils.rightClick();
/* 167 */           this.yepTimer.reset();
/* 168 */           this.failSafeTimer.reset();
/* 169 */           this.foragingState = ForagingState.HARVEST;
/*     */         } 
/*     */         break;
/*     */       
/*     */       case HARVEST:
/* 174 */         if (this.failSafeTimer.hasReached(2000L)) {
/* 175 */           this.foragingState = ForagingState.TREE;
/* 176 */           this.currentTree = 1;
/*     */         } 
/*     */         
/* 179 */         if (this.mc.field_71476_x == null || 
/* 180 */           this.mc.field_71476_x.field_72313_a != MovingObjectPosition.MovingObjectType.BLOCK)
/* 181 */           break;  bp = this.mc.field_71476_x.func_178782_a();
/* 182 */         ibs = this.mc.field_71441_e.func_180495_p(bp);
/* 183 */         if (ibs == null || 
/* 184 */           ibs.func_177230_c() != Blocks.field_150364_r)
/*     */           break; 
/* 186 */         if (this.yepTimer.hasReached(this.delay.getCurrent().intValue())) {
/* 187 */           KeybindUtils.leftClick();
/* 188 */           this.yepTimer.reset();
/* 189 */           this.foragingState = ForagingState.TREE;
/* 190 */           this.currentTree = 1;
/* 191 */           this.treeWait = 500;
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onRenderWorld(Render3DEvent event) {
/* 200 */     if (this.foragingState == ForagingState.LOOKING) {
/* 201 */       if (System.currentTimeMillis() <= RotationUtils.endTime) {
/* 202 */         RotationUtils.update();
/* 203 */         this.yepTimer.reset();
/*     */       }
/* 205 */       else if (!this.yepTimer.hasReached((this.delay.getCurrent().intValue() / 2))) {
/* 206 */         RotationUtils.update();
/*     */       } else {
/* 208 */         KeybindUtils.rightClick();
/* 209 */         this.yepTimer.reset();
/* 210 */         if (this.currentTree++ < 4) {
/* 211 */           this.foragingState = ForagingState.TREE;
/* 212 */           this.treeWait = this.delay.getCurrent().intValue();
/*     */         } else {
/* 214 */           this.foragingState = ForagingState.BONEMEAL;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onChat(ClientChatReceivedEvent event) {
/* 223 */     if (event.type == 2)
/*     */       return; 
/* 225 */     String msg = StringUtils.func_76338_a(event.message.func_150260_c());
/*     */     
/* 227 */     if (msg.startsWith("From") || msg.matches("\\[SkyBlock] .*? is visiting Your Island!.*") || msg.contains("has invited you to join their party!")) {
/* 228 */       this.mc.field_71439_g.func_85030_a("mob.enderdragon.growl", 1.0F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   private void swapSlot(int slot) {
/* 233 */     if (slot > 0 && slot <= 8)
/* 234 */       this.mc.field_71439_g.field_71071_by.field_70461_c = slot - 1; 
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\world\AutoForaging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */