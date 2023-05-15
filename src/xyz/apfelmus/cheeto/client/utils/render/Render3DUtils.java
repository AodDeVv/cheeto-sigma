/*     */ package xyz.apfelmus.cheeto.client.utils.render;
/*     */ import java.awt.Color;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cheeto.client.modules.render.ESP;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ColorUtils;
/*     */ import xyz.apfelmus.cheeto.mixin.mixins.RenderManagerAccessor;
/*     */ 
/*     */ public class Render3DUtils {
/*  28 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   public static void renderStarredMobBoundingBox(Entity entity, float partialTicks) {
/*  31 */     RenderManagerAccessor rm = (RenderManagerAccessor)mc.func_175598_ae();
/*     */     
/*  33 */     double renderPosX = rm.getRenderPosX();
/*  34 */     double renderPosY = rm.getRenderPosY();
/*  35 */     double renderPosZ = rm.getRenderPosZ();
/*     */     
/*  37 */     double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks - renderPosX;
/*  38 */     double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks - renderPosY;
/*  39 */     double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks - renderPosZ;
/*     */     
/*  41 */     int color = ColorUtils.getChroma(3000.0F, 0);
/*     */     
/*  43 */     AxisAlignedBB entityBox = entity.func_174813_aQ();
/*  44 */     AxisAlignedBB aabb = AxisAlignedBB.func_178781_a(entityBox.field_72340_a - entity.field_70165_t + x - 0.4D, entityBox.field_72338_b - entity.field_70163_u + y - (
/*     */         
/*  46 */         entity.func_95999_t().contains("Fels") ? 3.15D : 2.1D), entityBox.field_72339_c - entity.field_70161_v + z - 0.4D, entityBox.field_72336_d - entity.field_70165_t + x + 0.4D, entityBox.field_72337_e - entity.field_70163_u + y, entityBox.field_72334_f - entity.field_70161_v + z + 0.4D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     drawFilledBoundingBox(aabb, color);
/*     */   }
/*     */   
/*     */   public static void renderMiniBoundingBox(Entity entity, float partialTicks, int color) {
/*  57 */     RenderManagerAccessor rm = (RenderManagerAccessor)mc.func_175598_ae();
/*     */     
/*  59 */     double renderPosX = rm.getRenderPosX();
/*  60 */     double renderPosY = rm.getRenderPosY();
/*  61 */     double renderPosZ = rm.getRenderPosZ();
/*     */     
/*  63 */     double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks - renderPosX;
/*  64 */     double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks - renderPosY;
/*  65 */     double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks - renderPosZ;
/*     */     
/*  67 */     AxisAlignedBB entityBox = entity.func_174813_aQ().func_72314_b(0.1D, 0.1D, 0.1D);
/*  68 */     AxisAlignedBB aabb = AxisAlignedBB.func_178781_a(entityBox.field_72340_a - entity.field_70165_t + x, entityBox.field_72338_b - entity.field_70163_u + y, entityBox.field_72339_c - entity.field_70161_v + z, entityBox.field_72336_d - entity.field_70165_t + x, entityBox.field_72337_e - entity.field_70163_u + y, entityBox.field_72334_f - entity.field_70161_v + z);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     drawFilledBoundingBox(aabb, color);
/*     */   }
/*     */   
/*     */   public static void renderBoundingBox(Entity entity, float partialTicks, int color) {
/*  81 */     RenderManagerAccessor rm = (RenderManagerAccessor)mc.func_175598_ae();
/*     */     
/*  83 */     double renderPosX = rm.getRenderPosX();
/*  84 */     double renderPosY = rm.getRenderPosY();
/*  85 */     double renderPosZ = rm.getRenderPosZ();
/*     */     
/*  87 */     double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks - renderPosX;
/*  88 */     double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks - renderPosY;
/*  89 */     double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks - renderPosZ;
/*     */     
/*  91 */     AxisAlignedBB bbox = entity.func_174813_aQ();
/*  92 */     AxisAlignedBB aabb = new AxisAlignedBB(bbox.field_72340_a - entity.field_70165_t + x, bbox.field_72338_b - entity.field_70163_u + y, bbox.field_72339_c - entity.field_70161_v + z, bbox.field_72336_d - entity.field_70165_t + x, bbox.field_72337_e - entity.field_70163_u + y, bbox.field_72334_f - entity.field_70161_v + z);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     if (entity instanceof net.minecraft.entity.item.EntityArmorStand) {
/* 102 */       aabb = aabb.func_72314_b(0.3D, 2.0D, 0.3D);
/*     */     }
/*     */     
/* 105 */     drawFilledBoundingBox(aabb, color);
/*     */   }
/*     */   
/*     */   public static void renderSmallBox(Vec3 vec, int color) {
/* 109 */     RenderManagerAccessor rm = (RenderManagerAccessor)mc.func_175598_ae();
/*     */     
/* 111 */     double renderPosX = rm.getRenderPosX();
/* 112 */     double renderPosY = rm.getRenderPosY();
/* 113 */     double renderPosZ = rm.getRenderPosZ();
/*     */     
/* 115 */     double x = vec.field_72450_a - renderPosX;
/* 116 */     double y = vec.field_72448_b - renderPosY;
/* 117 */     double z = vec.field_72449_c - renderPosZ;
/*     */     
/* 119 */     AxisAlignedBB aabb = new AxisAlignedBB(x - 0.05D, y - 0.05D, z - 0.05D, x + 0.05D, y + 0.05D, z + 0.05D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     drawFilledBoundingBox(aabb, color);
/*     */   }
/*     */   
/*     */   public static void drawFilledBoundingBox(AxisAlignedBB aabb, int color) {
/* 132 */     GlStateManager.func_179147_l();
/* 133 */     GlStateManager.func_179097_i();
/* 134 */     GlStateManager.func_179140_f();
/* 135 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 136 */     GlStateManager.func_179090_x();
/* 137 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 138 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/*     */     
/* 140 */     float a = (color >> 24 & 0xFF) / 255.0F;
/* 141 */     float r = (color >> 16 & 0xFF) / 255.0F;
/* 142 */     float g = (color >> 8 & 0xFF) / 255.0F;
/* 143 */     float b = (color & 0xFF) / 255.0F;
/*     */     
/* 145 */     ESP esp = (ESP)CF4M.INSTANCE.moduleManager.getModule("ESP");
/* 146 */     float opacity = esp.boxOpacity.getCurrent().floatValue();
/*     */     
/* 148 */     GlStateManager.func_179131_c(r, g, b, a * opacity);
/* 149 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 150 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
/* 151 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
/* 152 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
/* 153 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
/* 154 */     tessellator.func_78381_a();
/* 155 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 156 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
/* 157 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
/* 158 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
/* 159 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
/* 160 */     tessellator.func_78381_a();
/* 161 */     GlStateManager.func_179131_c(r, g, b, a * opacity);
/* 162 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 163 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
/* 164 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
/* 165 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
/* 166 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
/* 167 */     tessellator.func_78381_a();
/* 168 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 169 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
/* 170 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
/* 171 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
/* 172 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
/* 173 */     tessellator.func_78381_a();
/* 174 */     GlStateManager.func_179131_c(r, g, b, a * opacity);
/* 175 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 176 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
/* 177 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72339_c).func_181675_d();
/* 178 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
/* 179 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c).func_181675_d();
/* 180 */     tessellator.func_78381_a();
/* 181 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 182 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
/* 183 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72334_f).func_181675_d();
/* 184 */     worldrenderer.func_181662_b(aabb.field_72336_d, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
/* 185 */     worldrenderer.func_181662_b(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72334_f).func_181675_d();
/* 186 */     tessellator.func_78381_a();
/* 187 */     GlStateManager.func_179131_c(r, g, b, a);
/* 188 */     RenderGlobal.func_181561_a(aabb);
/* 189 */     GlStateManager.func_179098_w();
/* 190 */     GlStateManager.func_179126_j();
/* 191 */     GlStateManager.func_179084_k();
/* 192 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public static void drawChestOutline(BlockPos chestPos) {
/* 196 */     RenderManagerAccessor rm = (RenderManagerAccessor)mc.func_175598_ae();
/*     */     
/* 198 */     double renderPosX = rm.getRenderPosX();
/* 199 */     double renderPosY = rm.getRenderPosY();
/* 200 */     double renderPosZ = rm.getRenderPosZ();
/*     */     
/* 202 */     double x = chestPos.func_177958_n() - renderPosX + 0.062D;
/* 203 */     double y = chestPos.func_177956_o() - renderPosY;
/* 204 */     double z = chestPos.func_177952_p() - renderPosZ + 0.062D;
/*     */     
/* 206 */     GL11.glBlendFunc(770, 771);
/* 207 */     GL11.glEnable(3042);
/* 208 */     GL11.glLineWidth(3.0F);
/* 209 */     GL11.glDisable(3553);
/* 210 */     GL11.glDisable(2929);
/* 211 */     GL11.glDepthMask(false);
/* 212 */     GL11.glColor4f(0.1764706F, 0.49019608F, 0.98039216F, 1.0F);
/* 213 */     RenderGlobal.func_181561_a(new AxisAlignedBB(x, y, z, x + 0.876D, y + 0.875D, z + 0.876D));
/* 214 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 215 */     GL11.glEnable(3553);
/* 216 */     GL11.glEnable(2929);
/* 217 */     GL11.glDepthMask(true);
/* 218 */     GL11.glDisable(3042);
/*     */   }
/*     */   
/*     */   public static void renderEspBox(BlockPos blockPos, float partialTicks, int color) {
/* 222 */     if (blockPos != null) {
/* 223 */       IBlockState blockState = mc.field_71441_e.func_180495_p(blockPos);
/*     */       
/* 225 */       if (blockState != null) {
/* 226 */         Block block = blockState.func_177230_c();
/* 227 */         block.func_180654_a((IBlockAccess)mc.field_71441_e, blockPos);
/* 228 */         double d0 = mc.field_71439_g.field_70142_S + (mc.field_71439_g.field_70165_t - mc.field_71439_g.field_70142_S) * partialTicks;
/* 229 */         double d1 = mc.field_71439_g.field_70137_T + (mc.field_71439_g.field_70163_u - mc.field_71439_g.field_70137_T) * partialTicks;
/* 230 */         double d2 = mc.field_71439_g.field_70136_U + (mc.field_71439_g.field_70161_v - mc.field_71439_g.field_70136_U) * partialTicks;
/* 231 */         drawFilledBoundingBox(block.func_180646_a((World)mc.field_71441_e, blockPos).func_72314_b(0.002D, 0.002D, 0.002D).func_72317_d(-d0, -d1, -d2), color);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void drawFairySoulOutline(Entity entity, float partialTicks, int color) {
/* 237 */     RenderManagerAccessor rm = (RenderManagerAccessor)mc.func_175598_ae();
/*     */     
/* 239 */     double renderPosX = rm.getRenderPosX();
/* 240 */     double renderPosY = rm.getRenderPosY();
/* 241 */     double renderPosZ = rm.getRenderPosZ();
/*     */     
/* 243 */     double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks - renderPosX;
/* 244 */     double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks - renderPosY;
/* 245 */     double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks - renderPosZ;
/*     */     
/* 247 */     if (color == -1) {
/* 248 */       color = ColorUtils.getChroma(3000.0F, 0);
/*     */     }
/* 250 */     AxisAlignedBB entityBox = entity.func_174813_aQ();
/* 251 */     AxisAlignedBB aabb = AxisAlignedBB.func_178781_a(entityBox.field_72340_a - entity.field_70165_t + x - 0.2D, entityBox.field_72338_b - entity.field_70163_u + y + 1.45D, entityBox.field_72339_c - entity.field_70161_v + z - 0.2D, entityBox.field_72336_d - entity.field_70165_t + x + 0.2D, entityBox.field_72337_e - entity.field_70163_u + y + 0.4D, entityBox.field_72334_f - entity.field_70161_v + z + 0.2D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     drawFilledBoundingBox(aabb, color);
/*     */   }
/*     */   
/*     */   public static void draw3DString(Vec3 pos, String text, int color, float partialTicks) {
/* 264 */     EntityPlayerSP entityPlayerSP = mc.field_71439_g;
/* 265 */     double x = pos.field_72450_a - ((EntityPlayer)entityPlayerSP).field_70142_S + (pos.field_72450_a - ((EntityPlayer)entityPlayerSP).field_70165_t - pos.field_72450_a - ((EntityPlayer)entityPlayerSP).field_70142_S) * partialTicks;
/* 266 */     double y = pos.field_72448_b - ((EntityPlayer)entityPlayerSP).field_70137_T + (pos.field_72448_b - ((EntityPlayer)entityPlayerSP).field_70163_u - pos.field_72448_b - ((EntityPlayer)entityPlayerSP).field_70137_T) * partialTicks;
/* 267 */     double z = pos.field_72449_c - ((EntityPlayer)entityPlayerSP).field_70136_U + (pos.field_72449_c - ((EntityPlayer)entityPlayerSP).field_70161_v - pos.field_72449_c - ((EntityPlayer)entityPlayerSP).field_70136_U) * partialTicks;
/* 268 */     RenderManager renderManager = mc.func_175598_ae();
/* 269 */     float f = 1.6F;
/* 270 */     float f1 = 0.016666668F * f;
/* 271 */     int width = mc.field_71466_p.func_78256_a(text) / 2;
/* 272 */     GlStateManager.func_179094_E();
/* 273 */     GlStateManager.func_179137_b(x, y, z);
/* 274 */     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 275 */     GlStateManager.func_179114_b(-renderManager.field_78735_i, 0.0F, 1.0F, 0.0F);
/* 276 */     GlStateManager.func_179114_b(renderManager.field_78732_j, 1.0F, 0.0F, 0.0F);
/* 277 */     GlStateManager.func_179152_a(-f1, -f1, -f1);
/* 278 */     GlStateManager.func_179147_l();
/* 279 */     GlStateManager.func_179140_f();
/* 280 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 281 */     mc.field_71466_p.func_78276_b(text, -width, 0, color);
/* 282 */     GlStateManager.func_179084_k();
/* 283 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 284 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public static void draw3DChromaString(Vec3 pos, String text, float partialTicks) {
/* 288 */     EntityPlayerSP entityPlayerSP = mc.field_71439_g;
/* 289 */     double x = pos.field_72450_a - ((EntityPlayer)entityPlayerSP).field_70142_S + (pos.field_72450_a - ((EntityPlayer)entityPlayerSP).field_70165_t - pos.field_72450_a - ((EntityPlayer)entityPlayerSP).field_70142_S) * partialTicks;
/* 290 */     double y = pos.field_72448_b - ((EntityPlayer)entityPlayerSP).field_70137_T + (pos.field_72448_b - ((EntityPlayer)entityPlayerSP).field_70163_u - pos.field_72448_b - ((EntityPlayer)entityPlayerSP).field_70137_T) * partialTicks;
/* 291 */     double z = pos.field_72449_c - ((EntityPlayer)entityPlayerSP).field_70136_U + (pos.field_72449_c - ((EntityPlayer)entityPlayerSP).field_70161_v - pos.field_72449_c - ((EntityPlayer)entityPlayerSP).field_70136_U) * partialTicks;
/* 292 */     RenderManager renderManager = mc.func_175598_ae();
/* 293 */     float f = 1.6F;
/* 294 */     float f1 = 0.016666668F * f;
/* 295 */     int width = mc.field_71466_p.func_78256_a(text) / 2;
/* 296 */     GlStateManager.func_179094_E();
/* 297 */     GlStateManager.func_179137_b(x, y, z);
/* 298 */     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 299 */     GlStateManager.func_179114_b(-renderManager.field_78735_i, 0.0F, 1.0F, 0.0F);
/* 300 */     GlStateManager.func_179114_b(renderManager.field_78732_j, 1.0F, 0.0F, 0.0F);
/* 301 */     GlStateManager.func_179152_a(-f1, -f1, -f1);
/* 302 */     GlStateManager.func_179147_l();
/* 303 */     GlStateManager.func_179097_i();
/* 304 */     GlStateManager.func_179132_a(false);
/* 305 */     GlStateManager.func_179140_f();
/* 306 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 307 */     double tmpX = -width;
/* 308 */     for (char tc : text.toCharArray()) {
/* 309 */       long t = System.currentTimeMillis() - (int)tmpX * 10L;
/* 310 */       int i = Color.HSBtoRGB((float)(t % 2000L) / 2000.0F, 0.88F, 0.88F);
/* 311 */       String tmp = String.valueOf(tc);
/* 312 */       mc.field_71466_p.func_175065_a(tmp, (int)tmpX, 0.0F, i, true);
/* 313 */       tmpX += mc.field_71466_p.func_78263_a(tc);
/*     */     } 
/*     */     
/* 316 */     GlStateManager.func_179132_a(true);
/* 317 */     GlStateManager.func_179126_j();
/* 318 */     GlStateManager.func_179084_k();
/* 319 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 320 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public static void drawLine(Vec3 start, Vec3 end, float thickness, float partialTicks) {
/* 324 */     Entity render = Minecraft.func_71410_x().func_175606_aa();
/* 325 */     WorldRenderer worldRenderer = Tessellator.func_178181_a().func_178180_c();
/*     */     
/* 327 */     double realX = render.field_70142_S + (render.field_70165_t - render.field_70142_S) * partialTicks;
/* 328 */     double realY = render.field_70137_T + (render.field_70163_u - render.field_70137_T) * partialTicks;
/* 329 */     double realZ = render.field_70136_U + (render.field_70161_v - render.field_70136_U) * partialTicks;
/*     */     
/* 331 */     GlStateManager.func_179094_E();
/* 332 */     GlStateManager.func_179137_b(-realX, -realY, -realZ);
/* 333 */     GlStateManager.func_179090_x();
/* 334 */     GlStateManager.func_179140_f();
/* 335 */     GL11.glDisable(3553);
/* 336 */     GlStateManager.func_179147_l();
/* 337 */     GlStateManager.func_179118_c();
/* 338 */     GL11.glLineWidth(thickness);
/* 339 */     GlStateManager.func_179097_i();
/* 340 */     GlStateManager.func_179132_a(false);
/* 341 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*     */     
/* 343 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 344 */     worldRenderer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
/* 345 */     worldRenderer.func_181662_b(start.field_72450_a, start.field_72448_b, start.field_72449_c).func_181666_a(1.0F, 0.65F, 0.0F, 1.0F).func_181675_d();
/* 346 */     worldRenderer.func_181662_b(end.field_72450_a, end.field_72448_b, end.field_72449_c).func_181666_a(1.0F, 0.65F, 0.0F, 1.0F).func_181675_d();
/* 347 */     Tessellator.func_178181_a().func_78381_a();
/*     */     
/* 349 */     GlStateManager.func_179137_b(realX, realY, realZ);
/* 350 */     GlStateManager.func_179084_k();
/* 351 */     GlStateManager.func_179141_d();
/* 352 */     GlStateManager.func_179098_w();
/* 353 */     GlStateManager.func_179126_j();
/* 354 */     GlStateManager.func_179132_a(true);
/* 355 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 356 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public static void drawLines(List<Vec3> poses, float thickness, float partialTicks) {
/* 360 */     Entity render = Minecraft.func_71410_x().func_175606_aa();
/* 361 */     WorldRenderer worldRenderer = Tessellator.func_178181_a().func_178180_c();
/*     */     
/* 363 */     double realX = render.field_70142_S + (render.field_70165_t - render.field_70142_S) * partialTicks;
/* 364 */     double realY = render.field_70137_T + (render.field_70163_u - render.field_70137_T) * partialTicks;
/* 365 */     double realZ = render.field_70136_U + (render.field_70161_v - render.field_70136_U) * partialTicks;
/*     */     
/* 367 */     GlStateManager.func_179094_E();
/* 368 */     GlStateManager.func_179137_b(-realX, -realY, -realZ);
/* 369 */     GlStateManager.func_179090_x();
/* 370 */     GlStateManager.func_179140_f();
/* 371 */     GL11.glDisable(3553);
/* 372 */     GlStateManager.func_179147_l();
/* 373 */     GlStateManager.func_179118_c();
/* 374 */     GL11.glLineWidth(thickness);
/* 375 */     GlStateManager.func_179097_i();
/* 376 */     GlStateManager.func_179132_a(false);
/* 377 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*     */     
/* 379 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 380 */     worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/* 381 */     int num = 0;
/* 382 */     for (Vec3 pos : poses) {
/* 383 */       int i = ColorUtils.getChroma(2500.0F, num++ * 5);
/* 384 */       worldRenderer.func_181662_b(pos.field_72450_a + 0.5D, pos.field_72448_b + 0.5D, pos.field_72449_c + 0.5D).func_181666_a((i >> 16 & 0xFF) / 255.0F, (i >> 8 & 0xFF) / 255.0F, (i & 0xFF) / 255.0F, (i >> 24 & 0xFF) / 255.0F)
/*     */ 
/*     */ 
/*     */         
/* 388 */         .func_181675_d();
/*     */     } 
/* 390 */     Tessellator.func_178181_a().func_78381_a();
/*     */     
/* 392 */     GlStateManager.func_179137_b(realX, realY, realZ);
/* 393 */     GlStateManager.func_179084_k();
/* 394 */     GlStateManager.func_179141_d();
/* 395 */     GlStateManager.func_179098_w();
/* 396 */     GlStateManager.func_179126_j();
/* 397 */     GlStateManager.func_179132_a(true);
/* 398 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 399 */     GlStateManager.func_179121_F();
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\render\Render3DUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */