/*     */ package xyz.apfelmus.cheeto.client.clickgui.settings;
/*     */ 
/*     */ import gg.essential.api.utils.Multithreading;
/*     */ import java.util.List;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.components.Box;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.components.HBox;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.components.VBox;
/*     */ import xyz.apfelmus.cheeto.client.configs.ClientConfig;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ChatUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.font.FontUtils;
/*     */ 
/*     */ public class ConfigComponent
/*     */   extends Box {
/*     */   private VBox vBox;
/*     */   private DropdownComponent dropdown;
/*     */   
/*     */   public ConfigComponent() {
/*  20 */     this.vBox = new VBox();
/*  21 */     add(new UIComponent[] { (UIComponent)this.vBox });
/*  22 */     this.vBox.addX(8);
/*  23 */     this.vBox.addY(20);
/*     */     
/*  25 */     List<String> configs = ClientConfig.getConfigs();
/*     */     
/*  27 */     this.dropdown = new DropdownComponent(configs, ClientConfig.getActiveConfig(), 200);
/*     */     
/*  29 */     HBox configBox = new HBox(10);
/*  30 */     configBox.add(new UIComponent[] { this.dropdown });
/*  31 */     TextComponent renameField = new TextComponent("Rename Config:", this.dropdown.getCurrentValue(), 280);
/*  32 */     ButtonComponent loadButton = new ButtonComponent("Load", 70, () -> {
/*     */           if (!ClientConfig.setActiveConfig(this.dropdown.getCurrentValue())) {
/*     */             ChatUtils.send("The config you tried to load doesn't exist - weird!", new String[0]);
/*     */           } else {
/*     */             renameField.setCurrentValue(this.dropdown.getCurrentValue());
/*     */           } 
/*     */         });
/*  39 */     loadButton.addY(FontUtils.normal.getFontHeight());
/*  40 */     configBox.add(new UIComponent[] { loadButton });
/*  41 */     this.vBox.add(new UIComponent[] { (UIComponent)configBox });
/*  42 */     this.vBox.add(new UIComponent[] { renameField });
/*     */     
/*  44 */     HBox newRename = new HBox(10);
/*  45 */     newRename.add(new UIComponent[] { new ButtonComponent("New", 135, () -> {
/*     */               String newName = renameField.getCurrentValue();
/*     */               if (ClientConfig.getConfigs().contains(newName)) {
/*     */                 ClientConfig.createConfig(newName + "_copy");
/*     */               } else {
/*     */                 ClientConfig.createConfig(newName);
/*     */               } 
/*     */               this.dropdown.setCurrentValue(ClientConfig.getActiveConfig());
/*     */               this.dropdown.addOption(ClientConfig.getActiveConfig());
/*     */             }) });
/*  55 */     newRename.add(new UIComponent[] { new ButtonComponent("Rename", 135, () -> {
/*     */               if (ClientConfig.getActiveConfig().equals(this.dropdown.getCurrentValue())) {
/*     */                 ClientConfig.renameConfig(renameField.getCurrentValue());
/*     */                 this.dropdown.setCurrentValue(ClientConfig.getActiveConfig());
/*     */                 this.dropdown.updateOptions(ClientConfig.getConfigs());
/*     */               } else {
/*     */                 ChatUtils.send("Load the config before you can rename!", new String[0]);
/*     */               } 
/*     */             }) });
/*  64 */     this.vBox.add(new UIComponent[] { (UIComponent)newRename });
/*  65 */     this.vBox.add(new UIComponent[] { new ButtonComponent("Export to Clipboard", 280, () -> {
/*     */               ChatUtils.send("Config uploading...", new String[0]);
/*     */ 
/*     */ 
/*     */               
/*     */               Multithreading.runAsync(());
/*     */             }) });
/*     */ 
/*     */ 
/*     */     
/*  75 */     this.vBox.add(new UIComponent[] { new ButtonComponent("Import from Clipboard", 280, () -> {
/*     */               ChatUtils.send("Config downloading...", new String[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               Multithreading.runAsync(());
/*     */             }) });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     this.width = this.vBox.width + 16;
/*  88 */     this.height = this.vBox.height + 28;
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(int mouseX, int mouseY, float partialTicks) {
/*  93 */     String text = "Configs";
/*  94 */     Render2DUtils.drawBorderWH(this.x - 2, this.y - 2, this.width + 4, this.height + 4, 1, -16119286);
/*  95 */     Render2DUtils.drawBorderWH(this.x - 1, this.y - 1, this.width + 2, this.height + 2, 1, -13619152);
/*  96 */     Render2DUtils.drawRectWH(this.x, this.y, this.width, this.height, -15198184);
/*  97 */     int stringWidth = FontUtils.normal.getStringWidth(text);
/*  98 */     Render2DUtils.drawRectWH(this.x + 8, this.y - 2, stringWidth + 12, 2, -15658735);
/*     */     
/* 100 */     FontUtils.normal.drawHVCenteredString(text, this.x + stringWidth / 2 + 16, this.y + FontUtils.normal.getFontHeight() / 5, -1);
/*     */ 
/*     */     
/* 103 */     super.draw(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\settings\ConfigComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */