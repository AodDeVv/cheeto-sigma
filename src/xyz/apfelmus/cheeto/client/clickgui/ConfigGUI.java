/*     */ package xyz.apfelmus.cheeto.client.clickgui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.module.Category;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.components.CategoryBoxComponent;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.components.HBox;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.components.MainBoxComponent;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.components.ModuleBoxComponent;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.settings.CategoryButtonComponent;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.settings.ConfigComponent;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.settings.ModuleComponent;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.settings.SearchComponent;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.settings.UIComponent;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.ResManager;
/*     */ 
/*     */ public class ConfigGUI
/*     */   extends GuiScreen
/*     */ {
/*     */   public static ConfigGUI INSTANCE;
/*     */   public MainBoxComponent mainBox;
/*     */   public ModuleBoxComponent moduleBox;
/*     */   public HBox mainHBox;
/*  28 */   public static Category lastCategory = Category.COMBAT; public CategoryBoxComponent categoryBox; public HBox moduleHBox; public ScaledResolution scaledResolution; public UIComponent currentDropdown; public Minecraft field_146297_k;
/*     */   
/*     */   public ConfigGUI() {
/*  31 */     INSTANCE = this;
/*  32 */     this.mainHBox = new HBox(1);
/*  33 */     this.moduleHBox = new HBox(10, true);
/*  34 */     this.field_146297_k = Minecraft.func_71410_x();
/*     */     
/*  36 */     this.scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
/*     */     
/*  38 */     int screenWidth = this.field_146297_k.field_71443_c;
/*  39 */     int screenHeight = this.field_146297_k.field_71440_d;
/*     */     
/*  41 */     int width = Math.max((int)(screenWidth * 0.6D), 1152);
/*  42 */     int height = Math.max((int)(screenHeight * 0.75D), 762);
/*     */     
/*  44 */     this.categoryBox = new CategoryBoxComponent(height);
/*     */     
/*  46 */     this.mainBox = new MainBoxComponent(width, height);
/*  47 */     this.moduleBox = new ModuleBoxComponent(width - 75, height);
/*  48 */     this.mainBox.setX((screenWidth - width) / 2);
/*  49 */     this.mainBox.setY((screenHeight - height) / 2);
/*     */     
/*  51 */     for (Category c : Category.values()) {
/*  52 */       CategoryButtonComponent cat = new CategoryButtonComponent(c);
/*  53 */       cat.addY(16);
/*  54 */       this.categoryBox.add(new UIComponent[] { (UIComponent)cat });
/*     */     } 
/*     */     
/*  57 */     this.mainBox.add(new UIComponent[] { (UIComponent)this.mainHBox });
/*  58 */     this.mainHBox.add(new UIComponent[] { (UIComponent)this.categoryBox, (UIComponent)this.moduleBox });
/*  59 */     this.moduleBox.add(new UIComponent[] { (UIComponent)this.moduleHBox });
/*  60 */     this.moduleHBox.addX(25);
/*  61 */     this.moduleHBox.addY(32);
/*  62 */     this.moduleHBox.setMaxWidth(this.moduleBox.getWidth() - 50);
/*  63 */     this.moduleHBox.setMaxHeight(this.moduleBox.getHeight() - 64);
/*     */     
/*  65 */     displayCategory(lastCategory);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/*  70 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*  71 */     this.mainBox.draw(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175273_b(Minecraft mcIn, int w, int h) {
/*  76 */     super.func_175273_b(mcIn, w, h);
/*  77 */     this.field_146297_k.func_147108_a(new ConfigGUI());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  82 */     super.func_73864_a(mouseX, mouseY, mouseButton);
/*     */     
/*  84 */     if (this.currentDropdown != null) {
/*  85 */       this.currentDropdown.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     } else {
/*  87 */       this.mainBox.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146273_a(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
/*  93 */     super.func_146273_a(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
/*     */     
/*  95 */     this.mainBox.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146286_b(int mouseX, int mouseY, int state) {
/* 100 */     super.func_146286_b(mouseX, mouseY, state);
/*     */     
/* 102 */     this.mainBox.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
/* 107 */     super.func_73869_a(typedChar, keyCode);
/*     */     
/* 109 */     this.mainBox.keyTyped(typedChar, keyCode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146274_d() throws IOException {
/* 114 */     super.func_146274_d();
/* 115 */     int scroll = Mouse.getEventDWheel() / ResManager.getScaleFactor();
/* 116 */     if (scroll != 0) this.mainBox.mouseScrolled(scroll);
/*     */   
/*     */   }
/*     */   
/*     */   public boolean func_73868_f() {
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_146281_b() {
/* 126 */     super.func_146281_b();
/* 127 */     CF4M.INSTANCE.configManager.save();
/*     */   }
/*     */   
/*     */   public void displayCategory(Category category) {
/* 131 */     lastCategory = category;
/* 132 */     this.moduleHBox.clear();
/* 133 */     this.moduleHBox.curX = 0;
/* 134 */     this.moduleHBox.curY = 0;
/* 135 */     this.moduleHBox.maxChildHeight = 0;
/* 136 */     this.moduleHBox.resetScroll();
/*     */     
/* 138 */     for (UIComponent cat : this.categoryBox.getChildren()) {
/* 139 */       if (cat instanceof CategoryButtonComponent) {
/* 140 */         if (((CategoryButtonComponent)cat).category != category) {
/* 141 */           ((CategoryButtonComponent)cat).selected = false; continue;
/*     */         } 
/* 143 */         ((CategoryButtonComponent)cat).selected = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 148 */     if (!category.equals(Category.SETTINGS)) {
/* 149 */       for (Object module : CF4M.INSTANCE.moduleManager.getModules(category)) {
/* 150 */         this.moduleHBox.add(new UIComponent[] { (UIComponent)new ModuleComponent(module) });
/*     */       } 
/*     */     } else {
/* 153 */       this.moduleHBox.add(new UIComponent[] { (UIComponent)new ConfigComponent() });
/* 154 */       this.moduleHBox.add(new UIComponent[] { (UIComponent)new SearchComponent() });
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getCurrentScroll() {
/* 159 */     return this.moduleHBox.getCurrentScroll();
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\ConfigGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */