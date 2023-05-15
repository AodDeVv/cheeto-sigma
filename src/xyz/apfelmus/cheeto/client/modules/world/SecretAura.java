/*     */ package xyz.apfelmus.cheeto.client.modules.world;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.tileentity.TileEntitySkull;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*     */ import net.minecraftforge.fml.common.ModContainer;
/*     */ import org.apache.commons.codec.digest.DigestUtils;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientChatReceivedEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.PlayerInteractEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.WorldUnloadEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.FloatSetting;
/*     */ import xyz.apfelmus.cheeto.client.settings.IntegerSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.CheetoStatus;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render3DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ @Module(name = "SecretAura", category = Category.WORLD)
/*     */ public class SecretAura {
/*     */   @Setting(name = "ScanRange")
/*  42 */   private IntegerSetting scanRange = new IntegerSetting(
/*  43 */       Integer.valueOf(7), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "ClickRange")
/*  44 */   private FloatSetting clickRange = new FloatSetting(
/*  45 */       Float.valueOf(5.0F), Float.valueOf(0.0F), Float.valueOf(8.0F)); @Setting(name = "ClickSlot")
/*  46 */   private IntegerSetting clickSlot = new IntegerSetting(
/*  47 */       Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(8)); @Setting(name = "Chests")
/*  48 */   private BooleanSetting chests = new BooleanSetting(true);
/*     */   @Setting(name = "ChestClose")
/*  50 */   private BooleanSetting chestClose = new BooleanSetting(true);
/*     */   @Setting(name = "Levers")
/*  52 */   private BooleanSetting levers = new BooleanSetting(true);
/*     */   @Setting(name = "Essences")
/*  54 */   private BooleanSetting essences = new BooleanSetting(true);
/*     */   @Setting(name = "SecretESP")
/*  56 */   private BooleanSetting secretEsp = new BooleanSetting(true);
/*     */   @Setting(name = "ReClickTime")
/*  58 */   private IntegerSetting reClickTime = new IntegerSetting(
/*  59 */       Integer.valueOf(1500), Integer.valueOf(0), Integer.valueOf(5000)); @Setting(name = "StonklessStonk")
/*  60 */   private BooleanSetting stonklessStonk = new BooleanSetting(true);
/*     */ 
/*     */   
/*  63 */   private static Minecraft mc = Minecraft.func_71410_x();
/*  64 */   private static List<SecretBlock> blocksNear = new ArrayList<>();
/*  65 */   private static List<SecretBlock> clicked = new ArrayList<>();
/*     */   
/*     */   private boolean inChest = false;
/*     */   private BlockPos selected;
/*     */   private static BlockPos lastPos;
/*     */   
/*     */   public static boolean tGT5uMvjKCoG5kZn() {
/*  72 */     if (!CheetoStatus.yep) return false; 
/*  73 */     ModContainer mod = (ModContainer)Loader.instance().getIndexedModList().get("ChromaHUD");
/*  74 */     if (mod != null) {
/*  75 */       File f = mod.getSource();
/*  76 */       if (f != null) {
/*     */         try {
/*  78 */           return DigestUtils.sha1Hex(Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0])).equals(CheetoStatus.yepCock);
/*  79 */         } catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  89 */     blocksNear.clear();
/*  90 */     clicked.clear();
/*  91 */     this.inChest = false;
/*  92 */     this.selected = null;
/*  93 */     lastPos = null;
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/*  98 */     if (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat))
/*  99 */       return;  if (SkyblockUtils.isInDungeon()) {
/* 100 */       if (this.reClickTime.getCurrent().intValue() != 0) {
/* 101 */         clicked.removeIf(sb -> (sb.clickedTime + this.reClickTime.getCurrent().intValue() < System.currentTimeMillis()));
/*     */       }
/*     */       
/* 104 */       if (!this.inChest) {
/* 105 */         Vec3 eyes = mc.field_71439_g.func_174824_e(1.0F);
/* 106 */         BlockPos ppos = mc.field_71439_g.func_180425_c().func_177982_a(0, 1, 0);
/* 107 */         int sr = this.scanRange.getCurrent().intValue();
/* 108 */         Vec3i range = new Vec3i(sr, sr, sr);
/* 109 */         for (BlockPos bp : BlockPos.func_177980_a(ppos.func_177973_b(range), ppos.func_177971_a(range))) {
/* 110 */           Type t = isSecret(bp);
/* 111 */           if (t == null)
/* 112 */             continue;  blocksNear.add(new SecretBlock(bp, t));
/*     */         } 
/*     */         
/* 115 */         if (!this.stonklessStonk.isEnabled()) {
/* 116 */           blocksNear.removeIf(bp -> (eyes.func_72438_d((new Vec3((Vec3i)bp.pos)).func_72441_c(0.5D, 0.5D, 0.5D)) > sr));
/* 117 */           blocksNear.stream().filter(bp -> (!clicked.contains(bp) && eyes.func_72438_d((new Vec3((Vec3i)bp.pos)).func_72441_c(0.5D, 0.5D, 0.5D)) <= this.clickRange.getCurrent().floatValue())).min(Comparator.comparing(bp -> Double.valueOf(eyes.func_72438_d((new Vec3((Vec3i)bp.pos)).func_72441_c(0.5D, 0.5D, 0.5D))))).ifPresent(bp -> {
/*     */                 handleClick(bp);
/*     */                 
/*     */                 clicked.add(bp);
/*     */               });
/*     */         } 
/*     */       } 
/* 124 */       if (this.inChest && this.chestClose.isEnabled()) {
/* 125 */         if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiChest) {
/* 126 */           mc.field_71439_g.func_71053_j();
/* 127 */           this.inChest = false;
/*     */         } 
/*     */       } else {
/* 130 */         this.inChest = false;
/*     */       } 
/*     */       
/* 133 */       lastPos = mc.field_71439_g.func_180425_c();
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRender(Render3DEvent event) {
/* 139 */     this.selected = null;
/* 140 */     if (this.stonklessStonk.isEnabled()) {
/* 141 */       for (SecretBlock bp : new ArrayList(blocksNear)) {
/* 142 */         if (this.selected == null && RotationUtils.lookingAt(bp.pos, this.clickRange.getCurrent().floatValue())) {
/* 143 */           this.selected = bp.pos;
/*     */           
/*     */           continue;
/*     */         } 
/* 147 */         Render3DUtils.renderEspBox(bp.pos, event.partialTicks, -1754827);
/*     */       } 
/*     */       
/* 150 */       if (this.selected != null) {
/* 151 */         Render3DUtils.renderEspBox(this.selected, event.partialTicks, -19712);
/*     */       }
/* 153 */     } else if (this.secretEsp.isEnabled()) {
/* 154 */       for (SecretBlock bp : new ArrayList(blocksNear)) {
/* 155 */         Render3DUtils.renderEspBox(bp.pos, event.partialTicks, clicked.contains(bp) ? -5314048 : -1754827);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onInteract(PlayerInteractEvent event) {
/* 162 */     if (this.stonklessStonk.isEnabled() && 
/* 163 */       this.selected != null && !clicked.contains(new SecretBlock(this.selected))) {
/* 164 */       MovingObjectPosition omo = mc.field_71476_x;
/* 165 */       if (omo != null && 
/* 166 */         omo.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && omo.func_178782_a().equals(this.selected)) {
/*     */         return;
/*     */       }
/*     */       
/* 170 */       if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
/* 171 */         SecretBlock sel = new SecretBlock(this.selected);
/* 172 */         clicked.add(sel);
/* 173 */         handleClick(sel);
/* 174 */         mc.field_71439_g.func_71038_i();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onWorldLoad(WorldUnloadEvent event) {
/* 182 */     clicked.clear();
/* 183 */     this.inChest = false;
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onChat(ClientChatReceivedEvent event) {
/* 188 */     if (event.message.func_150260_c().contains("locked")) {
/* 189 */       this.inChest = false;
/*     */     }
/*     */   }
/*     */   
/*     */   private Type isSecret(BlockPos xyz) {
/* 194 */     IBlockState bs = mc.field_71441_e.func_180495_p(xyz);
/* 195 */     Block block = bs.func_177230_c();
/* 196 */     Vec3 eyes = mc.field_71439_g.func_174824_e(1.0F);
/*     */     
/* 198 */     SecretBlock sec = new SecretBlock(xyz);
/* 199 */     if (!clicked.contains(sec) && !blocksNear.contains(sec) && Math.sqrt(xyz.func_177957_d(eyes.field_72450_a, eyes.field_72448_b, eyes.field_72449_c)) <= this.scanRange.getCurrent().intValue()) {
/* 200 */       if (this.chests.isEnabled() && block instanceof net.minecraft.block.BlockChest) {
/* 201 */         TileEntityChest te = (TileEntityChest)mc.field_71441_e.func_175625_s(xyz);
/*     */         
/* 203 */         if (te.field_145989_m == 0.0F) {
/* 204 */           return Type.CHEST;
/*     */         }
/*     */       } 
/* 207 */       if (this.levers.isEnabled() && block instanceof net.minecraft.block.BlockLever) {
/* 208 */         return Type.LEVER;
/*     */       }
/* 210 */       if (this.essences.isEnabled() && block instanceof net.minecraft.block.BlockSkull) {
/* 211 */         TileEntitySkull te = (TileEntitySkull)mc.field_71441_e.func_175625_s(xyz);
/* 212 */         GameProfile gp = te.func_152108_a();
/*     */         
/* 214 */         if (gp != null) {
/* 215 */           BlockPos bp = te.func_174877_v();
/* 216 */           if (bp != null && 
/* 217 */             gp.getId().toString().equals("26bb1a8d-7c66-31c6-82d5-a9c04c94fb02")) {
/* 218 */             return Type.ESSENCE;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 225 */     return null;
/*     */   }
/*     */   
/*     */   private void handleClick(SecretBlock xyz) {
/* 229 */     xyz.clickedTime = System.currentTimeMillis();
/* 230 */     if (this.clickSlot.getCurrent().intValue() != 0) {
/* 231 */       int holding = mc.field_71439_g.field_71071_by.field_70461_c;
/* 232 */       mc.field_71439_g.field_71071_by.field_70461_c = this.clickSlot.getCurrent().intValue();
/* 233 */       mc.field_71442_b.func_178890_a(mc.field_71439_g, mc.field_71441_e, mc.field_71439_g.field_71071_by.func_70448_g(), xyz.pos, EnumFacing.func_176733_a(RotationUtils.getRotation(xyz.pos).getYaw()).func_176734_d(), mc.field_71476_x.field_72307_f);
/* 234 */       mc.field_71439_g.field_71071_by.field_70461_c = holding;
/*     */     } else {
/* 236 */       mc.field_71442_b.func_178890_a(mc.field_71439_g, mc.field_71441_e, mc.field_71439_g.field_71071_by.func_70448_g(), xyz.pos, EnumFacing.func_176733_a(RotationUtils.getRotation(xyz.pos).getYaw()).func_176734_d(), mc.field_71476_x.field_72307_f);
/*     */     } 
/* 238 */     if (!this.stonklessStonk.isEnabled())
/* 239 */       clicked.add(xyz); 
/*     */   }
/*     */   
/*     */   enum Type
/*     */   {
/* 244 */     CHEST,
/* 245 */     LEVER,
/* 246 */     ESSENCE;
/*     */   }
/*     */   
/*     */   static class SecretBlock {
/*     */     private BlockPos pos;
/*     */     private SecretAura.Type type;
/*     */     private long clickedTime;
/*     */     
/*     */     public SecretBlock(BlockPos pos) {
/* 255 */       this.pos = pos;
/* 256 */       this.clickedTime = System.currentTimeMillis();
/*     */     }
/*     */     
/*     */     public SecretBlock(BlockPos pos, SecretAura.Type type) {
/* 260 */       this.pos = pos;
/* 261 */       this.type = type;
/* 262 */       this.clickedTime = System.currentTimeMillis();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 267 */       return this.pos.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 272 */       if (obj instanceof SecretBlock) {
/* 273 */         return this.pos.equals(((SecretBlock)obj).pos);
/*     */       }
/*     */       
/* 276 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\world\SecretAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */