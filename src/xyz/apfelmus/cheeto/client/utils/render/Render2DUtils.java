/*     */ package xyz.apfelmus.cheeto.client.utils.render;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ColorUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.shader.BackgroundPatternShader;
/*     */ 
/*     */ public class Render2DUtils {
/*  18 */   private static int backgroundList = GL11.glGenLists(1);
/*     */   
/*     */   public static void drawRectWH(int x, int y, int width, int height, int color) {
/*  21 */     drawRect(x, y, x + width, y + height, color);
/*     */   }
/*     */   
/*     */   public static void drawRect(int left, int top, int right, int bottom, int color) {
/*  25 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  26 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/*  27 */     GlStateManager.func_179147_l();
/*  28 */     GlStateManager.func_179090_x();
/*     */     
/*  30 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*  31 */     glColor(color);
/*     */     
/*  33 */     double scale = 1.0D / ResManager.getScaleFactor();
/*  34 */     GlStateManager.func_179094_E();
/*  35 */     GlStateManager.func_179139_a(scale, scale, 1.0D);
/*  36 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/*  37 */     worldrenderer.func_181662_b(left, bottom, 0.0D).func_181675_d();
/*  38 */     worldrenderer.func_181662_b(right, bottom, 0.0D).func_181675_d();
/*  39 */     worldrenderer.func_181662_b(right, top, 0.0D).func_181675_d();
/*  40 */     worldrenderer.func_181662_b(left, top, 0.0D).func_181675_d();
/*  41 */     tessellator.func_78381_a();
/*  42 */     GlStateManager.func_179121_F();
/*  43 */     GlStateManager.func_179098_w();
/*  44 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   public static void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
/*  48 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  49 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/*  50 */     GlStateManager.func_179147_l();
/*  51 */     GlStateManager.func_179090_x();
/*  52 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*  53 */     glColor(color);
/*     */     
/*  55 */     double scale = 1.0D / ResManager.getScaleFactor();
/*  56 */     GlStateManager.func_179094_E();
/*  57 */     GlStateManager.func_179139_a(scale, scale, 1.0D);
/*  58 */     worldrenderer.func_181668_a(4, DefaultVertexFormats.field_181705_e);
/*  59 */     worldrenderer.func_181662_b(x1, y1, 0.0D).func_181675_d();
/*  60 */     worldrenderer.func_181662_b(x2, y2, 0.0D).func_181675_d();
/*  61 */     worldrenderer.func_181662_b(x3, y3, 0.0D).func_181675_d();
/*  62 */     tessellator.func_78381_a();
/*  63 */     GlStateManager.func_179121_F();
/*  64 */     GlStateManager.func_179098_w();
/*  65 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   public static void glColor(int color) {
/*  69 */     float f3 = (color >> 24 & 0xFF) / 255.0F;
/*  70 */     float f = (color >> 16 & 0xFF) / 255.0F;
/*  71 */     float f1 = (color >> 8 & 0xFF) / 255.0F;
/*  72 */     float f2 = (color & 0xFF) / 255.0F;
/*  73 */     GlStateManager.func_179131_c(f, f1, f2, f3);
/*     */   }
/*     */   
/*     */   public static float[] getColor(int color) {
/*  77 */     float[] rgba = new float[4];
/*  78 */     rgba[3] = (color >> 24 & 0xFF) / 255.0F;
/*  79 */     rgba[0] = (color >> 16 & 0xFF) / 255.0F;
/*  80 */     rgba[1] = (color >> 8 & 0xFF) / 255.0F;
/*  81 */     rgba[2] = (color & 0xFF) / 255.0F;
/*  82 */     return rgba;
/*     */   }
/*     */   
/*     */   public static void drawGradientRectWH(int x, int y, int width, int height, int color1, int color2) {
/*  86 */     drawGradientRect(x, y, x + width, y + height, color1, color2);
/*     */   }
/*     */   
/*     */   public static void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
/*  90 */     float f3 = (color2 >> 24 & 0xFF) / 255.0F;
/*  91 */     float f = (color2 >> 16 & 0xFF) / 255.0F;
/*  92 */     float f1 = (color2 >> 8 & 0xFF) / 255.0F;
/*  93 */     float f2 = (color2 & 0xFF) / 255.0F;
/*  94 */     Tessellator tessellator = Tessellator.func_178181_a();
/*  95 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/*  96 */     GlStateManager.func_179147_l();
/*  97 */     GlStateManager.func_179090_x();
/*  98 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*  99 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 100 */     GlStateManager.func_179103_j(7425);
/*     */     
/* 102 */     double scale = 1.0D / ResManager.getScaleFactor();
/* 103 */     GlStateManager.func_179094_E();
/* 104 */     GlStateManager.func_179139_a(scale, scale, 1.0D);
/* 105 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/* 106 */     worldrenderer.func_181662_b(left, bottom, 0.0D).func_181666_a(f, f1, f2, f3).func_181675_d();
/* 107 */     worldrenderer.func_181662_b(right, bottom, 0.0D).func_181666_a(f, f1, f2, f3).func_181675_d();
/* 108 */     f3 = (color1 >> 24 & 0xFF) / 255.0F;
/* 109 */     f = (color1 >> 16 & 0xFF) / 255.0F;
/* 110 */     f1 = (color1 >> 8 & 0xFF) / 255.0F;
/* 111 */     f2 = (color1 & 0xFF) / 255.0F;
/* 112 */     worldrenderer.func_181662_b(right, top, 0.0D).func_181666_a(f, f1, f2, f3).func_181675_d();
/* 113 */     worldrenderer.func_181662_b(left, top, 0.0D).func_181666_a(f, f1, f2, f3).func_181675_d();
/* 114 */     tessellator.func_78381_a();
/* 115 */     GlStateManager.func_179121_F();
/* 116 */     GlStateManager.func_179098_w();
/* 117 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   public static void drawBorderWH(int x, int y, int width, int height, int thickness, int color) {
/* 121 */     drawBorder(x, y, x + width, y + height, thickness, color);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawBorder(int left, int top, int right, int bottom, int thickness, int color) {
/* 126 */     if (left < right) {
/* 127 */       int j = left;
/* 128 */       left = right;
/* 129 */       right = j;
/*     */     } 
/*     */     
/* 132 */     if (top < bottom) {
/* 133 */       int j = top;
/* 134 */       top = bottom;
/* 135 */       bottom = j;
/*     */     } 
/*     */     
/* 138 */     right++;
/* 139 */     top--;
/*     */     
/* 141 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 142 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/* 143 */     GlStateManager.func_179147_l();
/* 144 */     GlStateManager.func_179090_x();
/* 145 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 146 */     glColor(color);
/*     */     
/* 148 */     double scale = 1.0D / ResManager.getScaleFactor();
/* 149 */     GlStateManager.func_179094_E();
/* 150 */     GlStateManager.func_179139_a(scale, scale, 1.0D);
/* 151 */     GL11.glLineWidth(thickness);
/* 152 */     worldrenderer.func_181668_a(2, DefaultVertexFormats.field_181705_e);
/* 153 */     worldrenderer.func_181662_b(left, bottom, 0.0D).func_181675_d();
/* 154 */     worldrenderer.func_181662_b(right, bottom, 0.0D).func_181675_d();
/* 155 */     worldrenderer.func_181662_b(right, (top + 1), 0.0D).func_181675_d();
/* 156 */     worldrenderer.func_181662_b(left, top, 0.0D).func_181675_d();
/* 157 */     tessellator.func_78381_a();
/* 158 */     GlStateManager.func_179121_F();
/* 159 */     GlStateManager.func_179098_w();
/* 160 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   public static void drawPatternRectWH(int x, int y, int width, int height, int color1, int color2) {
/* 164 */     drawPatternRect(x, y, x + width, y + height, color1, color2);
/*     */   }
/*     */   
/*     */   public static void drawPatternRect(int left, int top, int right, int bottom, int color1, int color2) {
/* 168 */     BackgroundPatternShader.INSTANCE.startShader();
/* 169 */     drawRect(left, top, right, bottom, -1);
/* 170 */     BackgroundPatternShader.INSTANCE.stopShader();
/*     */   }
/*     */   
/*     */   public static void drawLine(int x, int y, int x1, int y1, int thickness, int color) {
/* 174 */     if (x == x1) {
/* 175 */       x++;
/* 176 */       x1++;
/*     */     } 
/*     */     
/* 179 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 180 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/* 181 */     GlStateManager.func_179147_l();
/* 182 */     GlStateManager.func_179090_x();
/* 183 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 184 */     glColor(color);
/*     */     
/* 186 */     double scale = 1.0D / ResManager.getScaleFactor();
/* 187 */     GlStateManager.func_179094_E();
/* 188 */     GlStateManager.func_179139_a(scale, scale, 1.0D);
/* 189 */     GL11.glLineWidth(thickness);
/* 190 */     worldrenderer.func_181668_a(1, DefaultVertexFormats.field_181705_e);
/* 191 */     worldrenderer.func_181662_b(x, y, 0.0D).func_181675_d();
/* 192 */     worldrenderer.func_181662_b(x1, y1, 0.0D).func_181675_d();
/* 193 */     tessellator.func_78381_a();
/* 194 */     GlStateManager.func_179121_F();
/* 195 */     GlStateManager.func_179098_w();
/* 196 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   public static void drawChromaLine(int x, int y, int width) {
/* 200 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 201 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/* 202 */     GlStateManager.func_179147_l();
/* 203 */     GlStateManager.func_179090_x();
/* 204 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 205 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 206 */     GlStateManager.func_179103_j(7425);
/*     */     
/* 208 */     double scale = 1.0D / ResManager.getScaleFactor();
/* 209 */     GlStateManager.func_179094_E();
/* 210 */     GlStateManager.func_179139_a(scale, scale, 1.0D);
/* 211 */     for (int i = 0; i < 2; i++) {
/* 212 */       worldrenderer.func_181668_a(3, DefaultVertexFormats.field_181706_f);
/* 213 */       for (int j = 0; j <= 8; j++) {
/* 214 */         int prog = j * width / 8;
/* 215 */         float[] col = getColor(ColorUtils.getChroma(5000.0F, prog / 5));
/* 216 */         worldrenderer.func_181662_b((x + prog), (y + i), 1.0D).func_181666_a(col[0], col[1], col[2], col[3] * ((i == 1) ? 0.6F : 1.0F)).func_181675_d();
/*     */       } 
/* 218 */       tessellator.func_78381_a();
/*     */     } 
/* 220 */     GlStateManager.func_179121_F();
/* 221 */     GlStateManager.func_179098_w();
/* 222 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   public static boolean isHovered(int x, int y, int width, int height) {
/* 226 */     int mouseX = Mouse.getX();
/* 227 */     int mouseY = getMouseY();
/* 228 */     return (mouseX >= x && mouseX - width < x && mouseY >= y && mouseY - height < y);
/*     */   }
/*     */   
/*     */   public static int getMouseY() {
/* 232 */     return (Minecraft.func_71410_x()).field_71440_d - Mouse.getY() - 1;
/*     */   }
/*     */   
/*     */   public static void drawLeftRoundedRect(float x, float y, float width, float height, float radius, int color) {
/* 236 */     width += x;
/* 237 */     x += radius;
/* 238 */     width -= radius;
/* 239 */     if (x < width) {
/* 240 */       float i = x;
/* 241 */       x = width;
/* 242 */       width = i;
/*     */     } 
/* 244 */     height += y;
/* 245 */     if (y < height) {
/* 246 */       float j = y;
/* 247 */       y = height;
/* 248 */       height = j;
/*     */     } 
/* 250 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 251 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/* 252 */     GlStateManager.func_179147_l();
/* 253 */     GlStateManager.func_179090_x();
/* 254 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 255 */     glColor(color);
/*     */     
/* 257 */     double scale = 1.0D / ResManager.getScaleFactor();
/* 258 */     GlStateManager.func_179094_E();
/* 259 */     GlStateManager.func_179139_a(scale, scale, 1.0D);
/* 260 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 261 */     worldrenderer.func_181662_b((width - radius), (y - radius), 0.0D).func_181675_d();
/* 262 */     worldrenderer.func_181662_b(width, (y - radius), 0.0D).func_181675_d();
/* 263 */     worldrenderer.func_181662_b(width, (height + radius), 0.0D).func_181675_d();
/* 264 */     worldrenderer.func_181662_b((width - radius), (height + radius), 0.0D).func_181675_d();
/* 265 */     tessellator.func_78381_a();
/* 266 */     drawArc(width, height + radius, radius, 180);
/* 267 */     drawArc(width, y - radius, radius, 270);
/* 268 */     GlStateManager.func_179121_F();
/* 269 */     GlStateManager.func_179098_w();
/* 270 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   public static void drawArc(float x, float y, float radius, int angleStart) {
/* 274 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 275 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/* 276 */     worldrenderer.func_181668_a(6, DefaultVertexFormats.field_181705_e);
/* 277 */     GlStateManager.func_179137_b(x, y, 0.0D);
/* 278 */     worldrenderer.func_181662_b(0.0D, 0.0D, 0.0D).func_181675_d();
/* 279 */     int points = 21; double i;
/* 280 */     for (i = 0.0D; i < points; i++) {
/* 281 */       double radians = Math.toRadians(i / points * 90.0D + angleStart);
/* 282 */       worldrenderer.func_181662_b(radius * Math.sin(radians), radius * Math.cos(radians), 0.0D).func_181675_d();
/*     */     } 
/* 284 */     tessellator.func_78381_a();
/* 285 */     GlStateManager.func_179137_b(-x, -y, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void drawTexture(ResourceLocation resourceLocation, int x, int y, int width, int height, int textureWidth, int textureHeight, int textureX, int textureY) {
/* 291 */     GlStateManager.func_179147_l();
/* 292 */     GlStateManager.func_179094_E();
/* 293 */     double scale = 1.0D / ResManager.getScaleFactor();
/* 294 */     GlStateManager.func_179139_a(scale, scale, 1.0D);
/* 295 */     Minecraft.func_71410_x().func_110434_K().func_110577_a(resourceLocation);
/* 296 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 297 */     Gui.func_146110_a(x, y, textureX, textureY, width, height, textureWidth, textureHeight);
/* 298 */     GlStateManager.func_179121_F();
/* 299 */     GlStateManager.func_179084_k();
/*     */   }
/*     */   
/*     */   public static void drawDescription(String description) {
/* 303 */     if (description != null && !description.isEmpty()) {
/* 304 */       int x = Mouse.getX() + 30;
/* 305 */       int y = getMouseY() - ConfigGUI.INSTANCE.getCurrentScroll();
/* 306 */       int width = FontUtils.normal.getStringWidth(description);
/* 307 */       int height = FontUtils.normal.getFontHeight();
/* 308 */       drawRectWH(x - 10, y - 7, width + 20, height + 10, -16119286);
/* 309 */       drawRectWH(x - 9, y - 6, width + 18, height + 8, -13619152);
/* 310 */       drawRectWH(x - 8, y - 5, width + 16, height + 6, -15198184);
/* 311 */       FontUtils.normal.drawString(description, x, y, -1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\render\Render2DUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */