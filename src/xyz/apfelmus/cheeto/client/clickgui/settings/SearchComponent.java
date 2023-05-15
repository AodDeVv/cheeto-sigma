/*     */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.module.Category;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.ConfigGUI;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.components.Box;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.components.VBox;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*     */ 
/*     */ public class SearchComponent
/*     */   extends Box {
/*     */   private VBox vBox;
/*     */   private TextComponent search;
/*     */   private List<SearchEntry> results;
/*     */   private int largest;
/*     */   
/*     */   public SearchComponent() {
/*  21 */     this.vBox = new VBox();
/*  22 */     add(new UIComponent[] { (UIComponent)this.vBox });
/*  23 */     this.vBox.addX(8);
/*  24 */     this.vBox.addY(20);
/*     */     
/*  26 */     this.search = new TextComponent("Search:", "", 280, this::keyTyped);
/*  27 */     this.vBox.add(new UIComponent[] { this.search });
/*  28 */     this.results = new ArrayList<>();
/*     */     
/*  30 */     this.width = this.vBox.width + 16;
/*  31 */     this.height = this.vBox.height + 28;
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(int mouseX, int mouseY, float partialTicks) {
/*  36 */     String text = "Search";
/*  37 */     Render2DUtils.drawBorderWH(this.x - 2, this.y - 2, this.width + 4, this.height + 4, 1, -16119286);
/*  38 */     Render2DUtils.drawBorderWH(this.x - 1, this.y - 1, this.width + 2, this.height + 2, 1, -13619152);
/*  39 */     Render2DUtils.drawRectWH(this.x, this.y, this.width, this.height, -15198184);
/*  40 */     int stringWidth = FontUtils.normal.getStringWidth(text);
/*  41 */     Render2DUtils.drawRectWH(this.x + 8, this.y - 2, stringWidth + 12, 2, -15658735);
/*     */     
/*  43 */     FontUtils.normal.drawHVCenteredString(text, this.x + stringWidth / 2 + 16, this.y + FontUtils.normal.getFontHeight() / 5, -1);
/*     */ 
/*     */     
/*  46 */     super.draw(mouseX, mouseY, partialTicks);
/*     */     
/*  48 */     int x = this.x + 8;
/*  49 */     int startY = this.search.y + this.search.height;
/*  50 */     if (this.results.size() > 0) {
/*  51 */       Render2DUtils.drawBorderWH(x - 1, startY - 1, this.largest - 14, this.results.size() * FontUtils.normal.getFontHeight() + 2, 1, -15592942);
/*  52 */       Render2DUtils.drawGradientRectWH(x, startY, this.largest - 16, this.results.size() * FontUtils.normal.getFontHeight(), -13619152, -14540254);
/*  53 */       for (int i = 0; i < this.results.size(); i++) {
/*  54 */         int y = startY + 1 + FontUtils.normal.getFontHeight() * i;
/*  55 */         FontUtils.normal.drawString(((SearchEntry)this.results.get(i)).toString(), this.x + 18, startY + FontUtils.normal.getFontHeight() * i, Render2DUtils.isHovered(x, y - 1 + ConfigGUI.INSTANCE.getCurrentScroll(), this.width, FontUtils.normal.getFontHeight()) ? -2236963 : -6974059);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  62 */     if (mouseButton != 0)
/*  63 */       return;  int x = this.x + 8;
/*  64 */     int startY = this.search.y + this.search.height;
/*  65 */     if (this.results.size() > 0) {
/*  66 */       for (int i = 0; i < this.results.size(); i++) {
/*  67 */         int y = startY + 1 + FontUtils.normal.getFontHeight() * i;
/*  68 */         if (Render2DUtils.isHovered(x, y - 1 + ConfigGUI.INSTANCE.getCurrentScroll(), this.width, FontUtils.normal.getFontHeight())) {
/*  69 */           ConfigGUI.INSTANCE.displayCategory((this.results.get(i)).category);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*  74 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   private void keyTyped() {
/*  78 */     this.results.clear();
/*  79 */     String query = this.search.getCurrentValue().toLowerCase();
/*  80 */     if (query.length() < 1)
/*  81 */       return;  this.largest = 280;
/*  82 */     List<Object> modules = CF4M.INSTANCE.moduleManager.getModules();
/*  83 */     for (Object module : modules) {
/*  84 */       if (this.results.size() >= 8)
/*  85 */         return;  String moduleName = CF4M.INSTANCE.moduleManager.getName(module);
/*  86 */       if (moduleName.toLowerCase().contains(query)) {
/*  87 */         Category cat = CF4M.INSTANCE.moduleManager.getCategory(module);
/*  88 */         SearchEntry entry = new SearchEntry(cat, moduleName);
/*  89 */         this.results.add(entry);
/*  90 */         this.largest = Math.max(this.largest, FontUtils.normal.getStringWidth(entry.toString()));
/*     */       } 
/*     */       
/*  93 */       List<Object> settings = CF4M.INSTANCE.settingManager.getSettings(module);
/*  94 */       if (settings == null)
/*  95 */         continue;  for (Object setting : settings) {
/*  96 */         if (this.results.size() >= 8)
/*  97 */           return;  String settingName = CF4M.INSTANCE.settingManager.getName(module, setting);
/*  98 */         if (settingName.toLowerCase().contains(query)) {
/*  99 */           Category cat = CF4M.INSTANCE.moduleManager.getCategory(module);
/* 100 */           SearchEntry entry = new SearchEntry(cat, moduleName, settingName);
/* 101 */           this.results.add(entry);
/* 102 */           this.largest = Math.max(this.largest, FontUtils.normal.getStringWidth(entry.toString()) + 36);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static class SearchEntry {
/*     */     private Category category;
/*     */     private String categoryString;
/*     */     private String module;
/*     */     private String setting;
/*     */     
/*     */     public SearchEntry(Category category, String module, String setting) {
/* 115 */       this.category = category;
/* 116 */       this.module = module;
/* 117 */       this.setting = setting;
/* 118 */       setCategory();
/*     */     }
/*     */     
/*     */     public SearchEntry(Category category, String module) {
/* 122 */       this.category = category;
/* 123 */       this.module = module;
/* 124 */       setCategory();
/*     */     }
/*     */     
/*     */     private void setCategory() {
/* 128 */       String cat = this.category.name();
/* 129 */       this.categoryString = cat.charAt(0) + cat.substring(1).toLowerCase();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 134 */       if (this.setting != null) {
/* 135 */         return this.categoryString + " > " + this.module + " > " + this.setting;
/*     */       }
/* 137 */       return this.categoryString + " > " + this.module;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\SearchComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */