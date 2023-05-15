/*     */ package xyz.apfelmus.cheeto.client.utils.render.font;
/*     */ 
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*     */ 
/*     */ public class GlyphFont {
/*     */   private Font font;
/*  18 */   private static final Map<String, Map<Integer, Boolean>> glCapMap = new HashMap<>(); private FontMetrics fontMetrics; private int fontHeight;
/*     */   private Map<String, CachedGlyphFont> cachedChars;
/*     */   
/*     */   public GlyphFont(Font font) {
/*  22 */     this.font = font;
/*  23 */     this.fontMetrics = (new Canvas()).getFontMetrics(this.font);
/*  24 */     this.fontHeight = (this.fontMetrics.getHeight() <= 0) ? font.getSize() : (this.fontMetrics.getHeight() + 3);
/*  25 */     this.cachedChars = new HashMap<>();
/*     */   }
/*     */   
/*     */   public void drawStringRaw(String text, double x, double y, int color) {
/*  29 */     double scale = 0.25D;
/*     */     
/*  31 */     GL11.glPushMatrix();
/*  32 */     GL11.glScaled(scale, scale, scale);
/*  33 */     GL11.glTranslated(x * 2.0D, y * 2.0D - 2.0D, 0.0D);
/*  34 */     Render2DUtils.glColor(color);
/*     */     
/*  36 */     for (char c : text.toCharArray()) {
/*  37 */       GL11.glTranslatef(drawChar(String.valueOf(c)), 0.0F, 0.0F);
/*     */     }
/*     */     
/*  40 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public CachedGlyphFont renderCharImage(String chr) {
/*  44 */     int charWidth = this.fontMetrics.stringWidth(chr);
/*     */     
/*  46 */     BufferedImage image = new BufferedImage(charWidth, this.fontHeight, 2);
/*  47 */     Graphics2D graphics = image.createGraphics();
/*     */     
/*  49 */     graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*  50 */     graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*     */     
/*  52 */     graphics.setFont(this.font);
/*  53 */     graphics.setPaint(Color.WHITE);
/*  54 */     graphics.drawString(chr, 0, this.fontMetrics.getAscent());
/*  55 */     graphics.dispose();
/*     */     
/*  57 */     return new CachedGlyphFont(loadGlTexture(image), charWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   public int drawChar(String chr) {
/*     */     CachedGlyphFont cached;
/*  63 */     if (this.cachedChars.containsKey(chr)) {
/*  64 */       cached = this.cachedChars.get(chr);
/*     */     } else {
/*  66 */       cached = renderAndCacheTexture(chr);
/*     */     } 
/*     */     
/*  69 */     int originalTex = GL11.glGetInteger(32873);
/*     */     
/*  71 */     GL11.glBindTexture(3553, cached.getTex());
/*  72 */     GL11.glBegin(7);
/*     */     
/*  74 */     GL11.glTexCoord2d(1.0D, 0.0D);
/*  75 */     GL11.glVertex2d(cached.getWidth(), 0.0D);
/*  76 */     GL11.glTexCoord2d(0.0D, 0.0D);
/*  77 */     GL11.glVertex2d(0.0D, 0.0D);
/*  78 */     GL11.glTexCoord2d(0.0D, 1.0D);
/*  79 */     GL11.glVertex2d(0.0D, this.fontHeight);
/*  80 */     GL11.glTexCoord2d(1.0D, 1.0D);
/*  81 */     GL11.glVertex2d(cached.getWidth(), this.fontHeight);
/*     */     
/*  83 */     GL11.glEnd();
/*  84 */     GL11.glBindTexture(3553, originalTex);
/*     */     
/*  86 */     return cached.getWidth();
/*     */   }
/*     */   
/*     */   private CachedGlyphFont renderAndCacheTexture(String chr) {
/*  90 */     CachedGlyphFont cached = renderCharImage(chr);
/*  91 */     this.cachedChars.put(chr, cached);
/*  92 */     return cached;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text) {
/*  96 */     return this.fontMetrics.stringWidth(text) / 2;
/*     */   }
/*     */   
/*     */   public int getFontHeight() {
/* 100 */     return this.fontHeight;
/*     */   }
/*     */   
/*     */   private int loadGlTexture(BufferedImage image) {
/* 104 */     int tex = GL11.glGenTextures();
/*     */     
/* 106 */     GL11.glBindTexture(3553, tex);
/*     */     
/* 108 */     GL11.glTexParameteri(3553, 10242, 10497);
/* 109 */     GL11.glTexParameteri(3553, 10243, 10497);
/* 110 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 111 */     GL11.glTexParameteri(3553, 10240, 9729);
/*     */     
/* 113 */     GL11.glTexImage2D(3553, 0, 6408, image.getWidth(), image.getHeight(), 0, 6408, 5121, readImageToBuffer(image));
/* 114 */     GL11.glBindTexture(3553, 0);
/*     */     
/* 116 */     return tex;
/*     */   }
/*     */   
/*     */   private ByteBuffer readImageToBuffer(BufferedImage bufferedImage) {
/* 120 */     int[] rgbArray = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
/*     */     
/* 122 */     ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * rgbArray.length);
/* 123 */     for (int rgb : rgbArray) {
/* 124 */       byteBuffer.putInt(rgb << 8 | rgb >> 24 & 0xFF);
/*     */     }
/* 126 */     byteBuffer.flip();
/*     */     
/* 128 */     return byteBuffer;
/*     */   }
/*     */   
/*     */   void preGlHints() {
/* 132 */     GL11.glEnable(3008);
/* 133 */     GL11.glEnable(3042);
/* 134 */     GL11.glBlendFunc(770, 771);
/* 135 */     GL11.glEnable(3553);
/* 136 */     clearCaps("FONT");
/* 137 */     disableGlCap(2929, "FONT");
/*     */   }
/*     */   
/*     */   void postGlHints() {
/* 141 */     resetCaps("FONT");
/* 142 */     GL11.glDisable(3042);
/*     */   }
/*     */   
/*     */   public void resetCaps(String scale) {
/* 146 */     if (!glCapMap.containsKey(scale)) {
/*     */       return;
/*     */     }
/* 149 */     Map<Integer, Boolean> map = glCapMap.get(scale);
/* 150 */     map.forEach(this::setGlState);
/* 151 */     map.clear();
/*     */   }
/*     */   
/*     */   public void clearCaps(String scale) {
/* 155 */     if (!glCapMap.containsKey(scale)) {
/*     */       return;
/*     */     }
/* 158 */     Map<Integer, Boolean> map = glCapMap.get(scale);
/* 159 */     map.clear();
/*     */   }
/*     */   
/*     */   public void enableGlCap(int cap, String scale) {
/* 163 */     setGlCap(cap, true, scale);
/*     */   }
/*     */   
/*     */   public void setGlCap(int cap, boolean state, String scale) {
/* 167 */     if (!glCapMap.containsKey(scale)) {
/* 168 */       glCapMap.put(scale, new HashMap<>());
/*     */     }
/* 170 */     ((Map<Integer, Boolean>)glCapMap.get(scale)).put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
/* 171 */     setGlState(cap, state);
/*     */   }
/*     */   
/*     */   public void setGlState(int cap, boolean state) {
/* 175 */     if (state) {
/* 176 */       GL11.glEnable(cap);
/*     */     } else {
/* 178 */       GL11.glDisable(cap);
/*     */     } 
/*     */   }
/*     */   public void disableGlCap(int cap, String scale) {
/* 182 */     setGlCap(cap, false, scale);
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\render\font\GlyphFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */