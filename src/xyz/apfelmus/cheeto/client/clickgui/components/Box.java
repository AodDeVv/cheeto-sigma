/*     */ package xyz.apfelmus.cheeto.client.clickgui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.shader.Framebuffer;
/*     */ import org.lwjgl.opengl.EXTFramebufferObject;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.settings.UIComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Box
/*     */   extends UIComponent
/*     */ {
/*  19 */   protected static Minecraft mc = Minecraft.func_71410_x();
/*     */   private List<UIComponent> children;
/*     */   int spacing;
/*     */   int minWidth;
/*     */   int minHeight;
/*     */   int maxWidth;
/*     */   int maxHeight;
/*     */   boolean scrollable;
/*     */   
/*     */   public Box() {
/*  29 */     this.children = new ArrayList<>();
/*  30 */     this.spacing = 5;
/*     */   }
/*     */   
/*     */   public Box(int spacing) {
/*  34 */     this.children = new ArrayList<>();
/*  35 */     this.spacing = spacing;
/*     */   }
/*     */   
/*     */   public Box(int spacing, boolean scrollable) {
/*  39 */     this.children = new ArrayList<>();
/*  40 */     this.spacing = spacing;
/*  41 */     this.scrollable = scrollable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(int mouseX, int mouseY, float partialTicks) {
/*  46 */     super.draw(mouseX, mouseY, partialTicks);
/*     */     
/*  48 */     if (this.scrollable) {
/*  49 */       GL11.glScissor(this.x - 5, mc.field_71440_d - this.y + this.maxHeight + 5, this.maxWidth + 10, this.maxHeight + 10);
/*  50 */       GL11.glEnable(3089);
/*     */     } 
/*     */     
/*  53 */     for (UIComponent child : Lists.reverse(this.children)) {
/*  54 */       child.draw(mouseX, mouseY, partialTicks);
/*     */     }
/*     */     
/*  57 */     if (this.scrollable) GL11.glDisable(3089);
/*     */   
/*     */   }
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/*  62 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     
/*  64 */     for (UIComponent child : new ArrayList(this.children)) {
/*  65 */       child.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
/*  71 */     super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
/*     */     
/*  73 */     for (UIComponent child : this.children) {
/*  74 */       child.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int state) {
/*  80 */     super.mouseReleased(mouseX, mouseY, state);
/*     */     
/*  82 */     for (UIComponent child : this.children) {
/*  83 */       child.mouseReleased(mouseX, mouseY, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void keyTyped(char typedChar, int keyCode) {
/*  89 */     super.keyTyped(typedChar, keyCode);
/*     */     
/*  91 */     for (UIComponent child : this.children) {
/*  92 */       child.keyTyped(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseScrolled(int delta) {
/*  98 */     super.mouseScrolled(delta);
/*     */     
/* 100 */     for (UIComponent child : this.children) {
/* 101 */       child.mouseScrolled(delta);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<UIComponent> getChildren() {
/* 106 */     return this.children;
/*     */   }
/*     */   
/*     */   public void setMinWidth(int minWidth) {
/* 110 */     this.minWidth = minWidth;
/*     */   }
/*     */   
/*     */   public void setMinHeight(int minHeight) {
/* 114 */     this.minHeight = minHeight;
/*     */   }
/*     */   
/*     */   public void setMaxWidth(int maxWidth) {
/* 118 */     this.maxWidth = maxWidth;
/*     */   }
/*     */   
/*     */   public void setMaxHeight(int maxHeight) {
/* 122 */     this.maxHeight = maxHeight;
/*     */   }
/*     */   
/*     */   public void add(UIComponent... components) {
/* 126 */     for (UIComponent comp : components) {
/* 127 */       updateChild(this, comp);
/*     */     }
/*     */     
/* 130 */     this.children.addAll(Arrays.asList(components));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addX(int x) {
/* 135 */     super.addX(x);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addY(int y) {
/* 140 */     super.addY(y);
/*     */   }
/*     */   
/*     */   public void updateChild(UIComponent main, UIComponent child) {
/* 144 */     child.addX(main.getX());
/* 145 */     child.addY(main.getY());
/*     */     
/* 147 */     if (child instanceof Box) {
/* 148 */       for (UIComponent cmp : ((Box)child).getChildren()) {
/* 149 */         updateChild(main, cmp);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addToChild(int x, int y, UIComponent child) {
/* 155 */     child.addX(x);
/* 156 */     child.addY(y);
/*     */     
/* 158 */     if (child instanceof Box) {
/* 159 */       for (UIComponent cmp : ((Box)child).getChildren()) {
/* 160 */         addToChild(x, y, cmp);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 166 */     this.width = 0;
/* 167 */     this.height = 0;
/* 168 */     this.children.clear();
/*     */   }
/*     */   
/*     */   public static void checkSetupFBO(Framebuffer framebuffer) {
/* 172 */     if (framebuffer != null && 
/* 173 */       framebuffer.field_147624_h > -1) {
/* 174 */       setupFBO(framebuffer);
/* 175 */       framebuffer.field_147624_h = -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setupFBO(Framebuffer framebuffer) {
/* 180 */     EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.field_147624_h);
/* 181 */     int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
/* 182 */     EXTFramebufferObject.glBindRenderbufferEXT(36161, stencilDepthBufferID);
/* 183 */     EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, mc.field_71443_c, mc.field_71440_d);
/* 184 */     EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferID);
/* 185 */     EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferID);
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\components\Box.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */