/*     */ package xyz.apfelmus.cheeto.client.utils.render.font;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ColorUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.ResManager;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.shader.TexturedChromaShader;
/*     */ 
/*     */ public class GlyphFontRenderer {
/*     */   GlyphFont defaultFont;
/*     */   private GlyphFont boldFont;
/*     */   private GlyphFont italicFont;
/*     */   private GlyphFont boldItalicFont;
/*     */   
/*     */   public GlyphFontRenderer(Font font) {
/*  18 */     this.defaultFont = new GlyphFont(font);
/*  19 */     this.boldFont = new GlyphFont(font.deriveFont(1));
/*  20 */     this.italicFont = new GlyphFont(font.deriveFont(2));
/*  21 */     this.boldItalicFont = new GlyphFont(font.deriveFont(3));
/*     */   }
/*     */   
/*     */   public void drawString(String text, int x, int y, int color) {
/*  25 */     drawString(text, x, y, color, true);
/*     */   }
/*     */   
/*     */   public void drawVCenteredString(String text, int x, int y, int color) {
/*  29 */     drawString(text, x, y - getFontHeight() / 2.0F, color, true);
/*     */   }
/*     */   
/*     */   public void drawVCenteredChromaString(String text, int x, int y) {
/*  33 */     drawChromaString(text, x, y - getFontHeight() / 2);
/*     */   }
/*     */   
/*     */   public void drawHVCenteredString(String text, int x, int y, int color) {
/*  37 */     drawString(text, x - getStringWidth(text) / 2.0F, y - getFontHeight() / 2.0F, color, true);
/*     */   }
/*     */   
/*     */   public void drawHVCenteredChromaString(String text, int x, int y) {
/*  41 */     drawChromaString(text, x - getStringWidth(text) / 2, y - getFontHeight() / 2);
/*     */   }
/*     */   
/*     */   public int drawChromaString(String text, int x, int y) {
/*  45 */     float sf = ResManager.getScaleFactor() * 2.0F / 3.0F;
/*  46 */     drawText(text, x + sf, y + sf, (new Color(0, 0, 0, 150)).getRGB(), true);
/*  47 */     TexturedChromaShader.INSTANCE.startShader();
/*  48 */     int xx = drawText(text, x, y, -1, false);
/*  49 */     TexturedChromaShader.INSTANCE.stopShader();
/*     */     
/*  51 */     return xx;
/*     */   }
/*     */   
/*     */   public int drawString(String text, float x, float y, int color, boolean shadow) {
/*  55 */     if (shadow) {
/*  56 */       float sf = ResManager.getScaleFactor() * 2.0F / 3.0F;
/*  57 */       drawText(text, x + sf, y + sf, (new Color(0, 0, 0, 150)).getRGB(), true);
/*     */     } 
/*  59 */     return drawText(text, x, y, color, false);
/*     */   }
/*     */   
/*     */   private int drawText(String text, float x, float y, int color, boolean ignoreColor) {
/*  63 */     GlStateManager.func_179094_E();
/*  64 */     int sf = ResManager.getScaleFactor();
/*  65 */     double scale = 1.0D / sf;
/*  66 */     GlStateManager.func_179139_a(scale, scale, scale);
/*  67 */     GlStateManager.func_179137_b(x - 1.5D, y + 0.5D, 0.0D);
/*  68 */     GlStateManager.func_179152_a(sf, sf, sf);
/*     */     
/*  70 */     this.defaultFont.preGlHints();
/*     */     
/*  72 */     int hexColor = color;
/*  73 */     if ((hexColor & 0xFC000000) == 0) {
/*  74 */       hexColor |= 0xFF000000;
/*     */     }
/*     */     
/*  77 */     int alpha = hexColor >> 24 & 0xFF;
/*     */     
/*  79 */     if (text.contains("§")) {
/*  80 */       String[] parts = text.split("§");
/*  81 */       GlyphFont currentFont = this.defaultFont;
/*  82 */       double width = 0.0D;
/*     */       
/*  84 */       boolean randomCase = false;
/*  85 */       boolean bold = false;
/*  86 */       boolean italic = false;
/*  87 */       boolean strikeThrough = false;
/*  88 */       boolean underline = false;
/*     */       
/*  90 */       for (int i = 0; i < parts.length; i++) {
/*  91 */         String part = parts[i];
/*     */         
/*  93 */         if (part.isEmpty())
/*     */           break; 
/*  95 */         if (i == 0) {
/*  96 */           currentFont.drawStringRaw(part, width, 0.0D, hexColor);
/*  97 */           width += currentFont.getStringWidth(part);
/*     */         } else {
/*  99 */           String words = part.substring(1);
/* 100 */           char type = part.charAt(0);
/* 101 */           int colorIndex = getColorIndex(type);
/*     */           
/* 103 */           if (colorIndex >= 0 && colorIndex <= 15) {
/* 104 */             if (!ignoreColor) {
/* 105 */               hexColor = ColorUtils.getHexColor(colorIndex) | alpha << 24;
/*     */             }
/*     */           } else {
/* 108 */             switch (colorIndex) {
/*     */               case 16:
/* 110 */                 randomCase = true;
/*     */                 break;
/*     */               
/*     */               case 17:
/* 114 */                 bold = true;
/*     */                 break;
/*     */               
/*     */               case 18:
/* 118 */                 strikeThrough = true;
/*     */                 break;
/*     */               
/*     */               case 19:
/* 122 */                 underline = true;
/*     */                 break;
/*     */               
/*     */               case 20:
/* 126 */                 italic = true;
/*     */                 break;
/*     */               
/*     */               case 21:
/* 130 */                 hexColor = color;
/* 131 */                 if ((hexColor & 0xFC000000) == 0) {
/* 132 */                   hexColor |= 0xFF000000;
/*     */                 }
/*     */                 
/* 135 */                 bold = false;
/* 136 */                 italic = false;
/* 137 */                 randomCase = false;
/* 138 */                 underline = false;
/* 139 */                 strikeThrough = false;
/*     */                 break;
/*     */             } 
/*     */           
/*     */           } 
/* 144 */           if (bold && italic) {
/* 145 */             currentFont = this.boldItalicFont;
/* 146 */           } else if (bold) {
/* 147 */             currentFont = this.boldFont;
/* 148 */           } else if (italic) {
/* 149 */             currentFont = this.italicFont;
/*     */           } else {
/* 151 */             currentFont = this.defaultFont;
/*     */           } 
/*     */           
/* 154 */           currentFont.drawStringRaw(randomCase ? ColorUtils.randomMagicText(words) : words, width, 0.0D, hexColor);
/*     */           
/* 156 */           if (strikeThrough) {
/* 157 */             Render2DUtils.drawLine((int)(width / 2.0D + 1.0D), (int)(currentFont.getFontHeight() / 3.0D), 
/* 158 */                 (int)((width + currentFont.getStringWidth(words)) / 2.0D + 1.0D), 
/* 159 */                 (int)(currentFont.getFontHeight() / 3.0D), 1, hexColor);
/*     */           }
/*     */ 
/*     */           
/* 163 */           if (underline) {
/* 164 */             Render2DUtils.drawLine((int)(width / 2.0D + 1.0D), (int)(currentFont.getFontHeight() / 2.0D), 
/* 165 */                 (int)((width + currentFont.getStringWidth(words)) / 2.0D + 1.0D), 
/* 166 */                 (int)(currentFont.getFontHeight() / 2.0D), 1, hexColor);
/*     */           }
/*     */ 
/*     */           
/* 170 */           width += currentFont.getStringWidth(words);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 174 */       this.defaultFont.drawStringRaw(text, 0.0D, 0.0D, color);
/*     */     } 
/*     */     
/* 177 */     this.defaultFont.postGlHints();
/* 178 */     GlStateManager.func_179137_b(-(x - 1.5D), -(y + 0.5D), 0.0D);
/* 179 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 180 */     GlStateManager.func_179121_F();
/*     */     
/* 182 */     return getStringWidth(text);
/*     */   }
/*     */   
/*     */   public int getStringWidth(String text) {
/* 186 */     String currentText = text;
/*     */     
/* 188 */     if (currentText.contains("§")) {
/* 189 */       String[] parts = currentText.split("§");
/*     */       
/* 191 */       GlyphFont currentFont = this.defaultFont;
/* 192 */       int width = 0;
/* 193 */       boolean bold = false;
/* 194 */       boolean italic = false;
/*     */       
/* 196 */       for (int i = 0; i < parts.length; i++) {
/* 197 */         String part = parts[i];
/*     */         
/* 199 */         if (part.isEmpty())
/*     */           break; 
/* 201 */         if (i == 0) {
/* 202 */           width += currentFont.getStringWidth(part);
/*     */         } else {
/* 204 */           String words = part.substring(1);
/* 205 */           char type = part.charAt(0);
/* 206 */           int colorIndex = getColorIndex(type);
/*     */           
/* 208 */           if (colorIndex < 16) {
/* 209 */             bold = italic = false;
/*     */           } else {
/* 211 */             switch (colorIndex) {
/*     */               case 17:
/* 213 */                 bold = true;
/*     */                 break;
/*     */               
/*     */               case 20:
/* 217 */                 italic = true;
/*     */                 break;
/*     */               
/*     */               case 21:
/* 221 */                 bold = italic = false;
/*     */                 break;
/*     */             } 
/*     */           
/*     */           } 
/* 226 */           if (bold && italic) {
/* 227 */             currentFont = this.boldItalicFont;
/* 228 */           } else if (bold) {
/* 229 */             currentFont = this.boldFont;
/* 230 */           } else if (italic) {
/* 231 */             currentFont = this.italicFont;
/*     */           } else {
/* 233 */             currentFont = this.defaultFont;
/*     */           } 
/*     */           
/* 236 */           width += currentFont.getStringWidth(words);
/*     */         } 
/*     */       } 
/*     */       
/* 240 */       return width * ResManager.getScaleFactor() / 2;
/*     */     } 
/* 242 */     return this.defaultFont.getStringWidth(currentText) * ResManager.getScaleFactor() / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFontHeight() {
/* 247 */     return this.defaultFont.getFontHeight() / 2 * ResManager.getScaleFactor() / 2;
/*     */   }
/*     */   
/*     */   private int getColorIndex(char type) {
/* 251 */     if (type >= '0' && type <= '9') return type - 48; 
/* 252 */     if (type >= 'a' && type <= 'f') return type - 97 + 10; 
/* 253 */     if (type >= 'k' && type <= 'o') return type - 107 + 16; 
/* 254 */     if (type == 'r') return 21; 
/* 255 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\render\font\GlyphFontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */