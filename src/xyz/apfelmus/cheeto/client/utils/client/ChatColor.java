/*     */ package xyz.apfelmus.cheeto.client.utils.client;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public enum ChatColor
/*     */ {
/*  10 */   BLACK('0', 0),
/*  11 */   DARK_BLUE('1', 1),
/*  12 */   DARK_GREEN('2', 2),
/*  13 */   DARK_AQUA('3', 3),
/*  14 */   DARK_RED('4', 4),
/*  15 */   DARK_PURPLE('5', 5),
/*  16 */   GOLD('6', 6),
/*  17 */   GRAY('7', 7),
/*  18 */   DARK_GRAY('8', 8),
/*  19 */   BLUE('9', 9),
/*  20 */   GREEN('a', 10),
/*  21 */   AQUA('b', 11),
/*  22 */   RED('c', 12),
/*  23 */   LIGHT_PURPLE('d', 13),
/*  24 */   YELLOW('e', 14),
/*  25 */   WHITE('f', 15),
/*  26 */   MAGIC('k', 16, true),
/*  27 */   BOLD('l', 17, true),
/*  28 */   STRIKETHROUGH('m', 18, true),
/*  29 */   UNDERLINE('n', 19, true),
/*  30 */   ITALIC('o', 20, true),
/*  31 */   RESET('r', 21);
/*     */   
/*     */   static {
/*  34 */     STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
/*  35 */     BY_ID = Maps.newHashMap();
/*  36 */     BY_CHAR = Maps.newHashMap();
/*     */ 
/*     */     
/*  39 */     for (ChatColor color : values()) {
/*  40 */       BY_ID.put(Integer.valueOf(color.intCode), color);
/*  41 */       BY_CHAR.put(Character.valueOf(color.code), color);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final char COLOR_CHAR = '§';
/*     */   private static final Pattern STRIP_COLOR_PATTERN;
/*     */   private static final Map<Integer, ChatColor> BY_ID;
/*     */   private static final Map<Character, ChatColor> BY_CHAR;
/*     */   private final int intCode;
/*     */   private final char code;
/*     */   private final boolean isFormat;
/*     */   private final String toString;
/*     */   
/*     */   ChatColor(char code, int intCode, boolean isFormat) {
/*  55 */     this.code = code;
/*  56 */     this.intCode = intCode;
/*  57 */     this.isFormat = isFormat;
/*  58 */     this.toString = new String(new char[] { '§', code });
/*     */   }
/*     */   
/*     */   public static ChatColor getByChar(char code) {
/*  62 */     return BY_CHAR.get(Character.valueOf(code));
/*     */   }
/*     */   
/*     */   public static ChatColor getByChar(String code) {
/*  66 */     Validate.notNull(code, "Code cannot be null", new Object[0]);
/*  67 */     Validate.isTrue((code.length() > 0), "Code must have at least one char", new Object[0]);
/*     */     
/*  69 */     return BY_CHAR.get(Character.valueOf(code.charAt(0)));
/*     */   }
/*     */   
/*     */   public static String stripColor(String input) {
/*  73 */     if (input == null) {
/*  74 */       return null;
/*     */     }
/*     */     
/*  77 */     return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
/*     */   }
/*     */   
/*     */   public static String format(String textToTranslate) {
/*  81 */     char[] b = textToTranslate.toCharArray();
/*  82 */     for (int i = 0; i < b.length - 1; i++) {
/*  83 */       if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
/*  84 */         b[i] = '§';
/*  85 */         b[i + 1] = Character.toLowerCase(b[i + 1]);
/*     */       } 
/*     */     } 
/*  88 */     return new String(b);
/*     */   }
/*     */   
/*     */   public static String getLastColors(String input) {
/*  92 */     StringBuilder result = new StringBuilder();
/*  93 */     int length = input.length();
/*     */     
/*  95 */     for (int index = length - 1; index > -1; index--) {
/*  96 */       char section = input.charAt(index);
/*  97 */       if (section == '§' && index < length - 1) {
/*  98 */         char c = input.charAt(index + 1);
/*  99 */         ChatColor color = getByChar(c);
/*     */         
/* 101 */         if (color != null) {
/* 102 */           result.insert(0, color.toString());
/*     */           
/* 104 */           if (color.isColor() || color.equals(RESET)) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     return result.toString();
/*     */   }
/*     */   
/*     */   public char getChar() {
/* 115 */     return this.code;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     return this.toString;
/*     */   }
/*     */   
/*     */   public boolean isFormat() {
/* 124 */     return this.isFormat;
/*     */   }
/*     */   
/*     */   public boolean isColor() {
/* 128 */     return (!this.isFormat && this != RESET);
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\ChatColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */