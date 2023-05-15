/*     */ package xyz.apfelmus.cheeto.client.modules.combat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientChatReceivedEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.TimeHelper;
/*     */ import xyz.apfelmus.cheeto.client.utils.math.VecUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ import xyz.apfelmus.cheeto.mixin.mixins.PlayerControllerMPAccessor;
/*     */ 
/*     */ @Module(name = "BlazeQOL", category = Category.COMBAT)
/*     */ public class BlazeQOL {
/*     */   @Setting(name = "AutoDagger", description = "Automatically manages Daggers")
/*  34 */   private BooleanSetting autoDagger = new BooleanSetting(true);
/*     */   @Setting(name = "DetectionRange", description = "Range for detecting mobs")
/*  36 */   private IntegerSetting detectionRange = new IntegerSetting(
/*  37 */       Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(10)); @Setting(name = "AutoCloak", description = "Automatically Wither Cloak for fire pillars (slightly buggy)")
/*  38 */   private BooleanSetting autoCloak = new BooleanSetting(true);
/*     */   @Setting(name = "CloakThreshhold", description = "Cloak x seconds before fire pillar explosion")
/*  40 */   private IntegerSetting cloakThreshhold = new IntegerSetting(
/*  41 */       Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(3));
/*     */   
/*  43 */   private static Minecraft mc = Minecraft.func_71410_x();
/*  44 */   private static final Pattern NAME_PATTERN = Pattern.compile(".*?(SPIRIT|CRYSTAL|ASHEN|AURIC) .*");
/*  45 */   private static final Pattern DAGGER_PATTERN = Pattern.compile("(\\w+ )?(\\w+?) Dagger");
/*  46 */   private static final Pattern PILLAR_PATTERN = Pattern.compile(".*?(\\d)s \\d hits.*");
/*  47 */   private static final Pattern PILLAR_EXPLOSION_PATTERN = Pattern.compile(".*?You took .+? true damage from an exploding fire pillar!.*?");
/*     */   
/*  49 */   private static TimeHelper timer = new TimeHelper();
/*     */   
/*  51 */   private static final List<String> normalDaggers = Arrays.asList(new String[] { "Twilight", "Mawdredge", "Deathripper" });
/*  52 */   private static final List<String> fireDaggers = Arrays.asList(new String[] { "Firedust", "Kindlebane", "Pyrochaos" });
/*     */   private static boolean cloaked = false;
/*     */   
/*     */   enum BlazeBoss
/*     */   {
/*  57 */     NONE((String)Collections.emptyList(), Items.field_151041_m),
/*  58 */     SPIRIT((String)BlazeQOL.normalDaggers, Items.field_151040_l),
/*  59 */     CRYSTAL((String)BlazeQOL.normalDaggers, Items.field_151048_u),
/*  60 */     ASHEN((String)BlazeQOL.fireDaggers, Items.field_151052_q),
/*  61 */     AURIC((String)BlazeQOL.fireDaggers, Items.field_151010_B);
/*     */     
/*     */     private List<String> swordNames;
/*     */     private Item sword;
/*     */     
/*     */     BlazeBoss(List<String> swordNames, Item sword) {
/*  67 */       this.swordNames = swordNames;
/*  68 */       this.sword = sword;
/*     */     }
/*     */   }
/*     */   
/*  72 */   private BlazeBoss curBoss = BlazeBoss.NONE;
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  76 */     this.curBoss = BlazeBoss.NONE;
/*  77 */     cloaked = false;
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/*  82 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat)) {
/*     */       return;
/*     */     }
/*  85 */     AxisAlignedBB playerAABB = mc.field_71439_g.func_174813_aQ();
/*     */     
/*  87 */     if (this.autoCloak.isEnabled()) {
/*  88 */       List<Entity> inRange = mc.field_71441_e.func_175644_a(EntityArmorStand.class, e -> true);
/*     */       
/*  90 */       inRange = (List<Entity>)inRange.stream().sorted(Comparator.comparing(v -> Float.valueOf(v.func_70032_d((Entity)mc.field_71439_g)))).collect(Collectors.toList());
/*     */       
/*  92 */       for (Entity e : inRange) {
/*  93 */         Matcher m = PILLAR_PATTERN.matcher(StringUtils.func_76338_a(e.func_70005_c_()));
/*     */         
/*  95 */         if (m.matches()) {
/*  96 */           int time = Integer.parseInt(m.group(1));
/*     */           
/*  98 */           if (time <= this.cloakThreshhold.getCurrent().intValue() && !cloaked) {
/*  99 */             int witherCloakSlot = SkyblockUtils.getItemInSlot("Wither Cloak Sword");
/* 100 */             if (witherCloakSlot != -1) {
/* 101 */               SkyblockUtils.silentUse(0, witherCloakSlot + 1);
/* 102 */               cloaked = true;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 110 */     if (this.autoDagger.isEnabled()) {
/* 111 */       int range = this.detectionRange.getCurrent().intValue();
/* 112 */       playerAABB = playerAABB.func_72314_b(range, range, range);
/* 113 */       Vec3 lookVec = mc.field_71439_g.func_70040_Z();
/* 114 */       lookVec = VecUtils.times(lookVec, range);
/* 115 */       playerAABB = playerAABB.func_72317_d(lookVec.field_72450_a, 0.0D, lookVec.field_72449_c);
/*     */       
/* 117 */       List<Entity> inRange = mc.field_71441_e.func_175674_a((Entity)mc.field_71439_g, playerAABB, e -> e instanceof EntityArmorStand);
/*     */       
/* 119 */       inRange = (List<Entity>)inRange.stream().sorted(Comparator.comparing(v -> Float.valueOf(v.func_70032_d((Entity)mc.field_71439_g)))).collect(Collectors.toList());
/*     */       
/* 121 */       this.curBoss = BlazeBoss.NONE;
/*     */       
/* 123 */       for (Entity e : inRange) {
/* 124 */         Matcher m = NAME_PATTERN.matcher(StringUtils.func_76338_a(e.func_70005_c_()));
/*     */         
/* 126 */         if (m.matches()) {
/* 127 */           this.curBoss = BlazeBoss.valueOf(m.group(1));
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 132 */       if (this.curBoss != BlazeBoss.NONE) {
/* 133 */         int swordSlot = getSwordSlot(this.curBoss.swordNames);
/*     */         
/* 135 */         if (swordSlot != -1) {
/* 136 */           ItemStack held = mc.field_71439_g.func_70694_bm();
/*     */           
/* 138 */           if (held != null) {
/* 139 */             boolean normalDagger = normalDaggers.stream().anyMatch(v -> held.func_82833_r().contains(v));
/* 140 */             boolean fireDagger = fireDaggers.stream().anyMatch(v -> held.func_82833_r().contains(v));
/*     */             
/* 142 */             if (!normalDagger && !fireDagger)
/*     */               return; 
/* 144 */             if (mc.field_71439_g.field_71071_by.field_70461_c != swordSlot) {
/* 145 */               mc.field_71439_g.field_71071_by.field_70461_c = swordSlot;
/* 146 */               ((PlayerControllerMPAccessor)mc.field_71442_b).syncCurrentItem();
/*     */             } 
/*     */             
/* 149 */             if (timer.hasReached(1000L)) {
/* 150 */               Item item = held.func_77973_b();
/*     */               
/* 152 */               if (item != this.curBoss.sword) {
/* 153 */                 mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, held);
/* 154 */                 timer.reset();
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onChat(ClientChatReceivedEvent event) {
/* 165 */     if (event.type == 2)
/*     */       return; 
/* 167 */     String msg = StringUtils.func_76338_a(event.message.func_150260_c());
/*     */     
/* 169 */     if (msg.equals("Creeper Veil Activated!")) {
/* 170 */       cloaked = true;
/* 171 */     } else if (msg.equals("Creper Veil De-activated!")) {
/* 172 */       cloaked = false;
/*     */     } 
/*     */     
/* 175 */     Matcher m = PILLAR_EXPLOSION_PATTERN.matcher(msg);
/*     */     
/* 177 */     if (m.matches()) {
/* 178 */       int witherCloakSlot = SkyblockUtils.getItemInSlot("Wither Cloak Sword");
/* 179 */       if (witherCloakSlot != -1 && cloaked) {
/* 180 */         SkyblockUtils.silentUse(0, witherCloakSlot + 1);
/* 181 */         cloaked = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getSwordSlot(List<String> swordNames) {
/* 187 */     int ret = -1;
/*     */     
/* 189 */     for (int i = 0; i < 8; i++) {
/* 190 */       ItemStack is = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 191 */       if (is != null) {
/* 192 */         String itemName = StringUtils.func_76338_a(is.func_82833_r());
/* 193 */         Matcher m = DAGGER_PATTERN.matcher(itemName);
/*     */         
/* 195 */         if (m.matches()) {
/* 196 */           String sword = m.group(2);
/*     */           
/* 198 */           if (swordNames.contains(sword)) {
/* 199 */             return i;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 205 */     return ret;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\combat\BlazeQOL.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */