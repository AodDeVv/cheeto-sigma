/*     */ package xyz.apfelmus.cheeto.client.clickgui.components;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import xyz.apfelmus.cheeto.client.clickgui.settings.UIComponent;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render2DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.ResManager;
/*     */ 
/*     */ 
/*     */ public class HBox
/*     */   extends Box
/*     */ {
/*     */   public int curX;
/*     */   public int curY;
/*     */   public int maxChildHeight;
/*     */   private int currentScroll;
/*     */   private int targetScroll;
/*     */   private int lastScroll;
/*     */   private long lastScrollTime;
/*     */   private boolean scrollHovered;
/*     */   private boolean dragging;
/*     */   private int scrollStartY;
/*     */   private int dragStartY;
/*     */   
/*     */   public HBox() {}
/*     */   
/*     */   public HBox(int spacing) {
/*  29 */     super(spacing);
/*     */   }
/*     */   
/*     */   public HBox(int spacing, boolean scrollable) {
/*  33 */     super(spacing, scrollable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(UIComponent... components) {
/*  38 */     for (UIComponent component : components) {
/*  39 */       if (this.curX != 0) {
/*  40 */         this.curX += this.spacing;
/*     */       }
/*     */       
/*  43 */       if (this.maxWidth != 0) {
/*  44 */         if (this.curX + component.getWidth() > this.maxWidth) {
/*  45 */           this.curY += this.maxChildHeight + this.spacing;
/*  46 */           this.maxChildHeight = 0;
/*  47 */           this.curX = 0;
/*     */         } 
/*     */         
/*  50 */         if (this.maxChildHeight < component.getHeight()) {
/*  51 */           this.maxChildHeight = component.getHeight();
/*     */         }
/*     */       } 
/*     */       
/*  55 */       if (this.height < this.curY + component.getHeight()) {
/*  56 */         this.height = this.curY + component.getHeight();
/*     */       }
/*     */       
/*  59 */       addToChild(this.curX, this.curY, component);
/*  60 */       if (this.curX + component.getWidth() > this.width) {
/*  61 */         this.width = this.curX + component.getWidth();
/*  62 */       } else if (this.width + component.getWidth() < this.maxWidth) {
/*  63 */         this.width += component.getWidth();
/*     */       } 
/*  65 */       this.curX += component.getWidth();
/*     */       
/*  67 */       updateChild(this, component);
/*     */     } 
/*     */     
/*  70 */     getChildren().addAll(Arrays.asList(components));
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(int mouseX, int mouseY, float partialTicks) {
/*  75 */     if (this.scrollable) {
/*  76 */       float spentMillis = (float)(System.currentTimeMillis() - this.lastScrollTime);
/*  77 */       float relativeProgress = spentMillis / 250.0F;
/*  78 */       this.currentScroll = (int)((this.targetScroll - this.lastScroll) * RotationUtils.easeOutCubic(relativeProgress) + this.lastScroll);
/*     */       
/*  80 */       float maxRange = (this.height - this.maxHeight);
/*  81 */       if (maxRange > 0.0F) {
/*  82 */         int scrollBarHeight = Math.min(this.maxHeight, (int)(this.maxHeight / this.height * this.maxHeight));
/*  83 */         if (this.dragging) {
/*  84 */           int y = Render2DUtils.getMouseY();
/*  85 */           int change = this.dragStartY - y;
/*  86 */           change = (int)(change * maxRange / (this.maxHeight - scrollBarHeight));
/*  87 */           this.currentScroll = this.scrollStartY + change;
/*  88 */           this.currentScroll = Math.min(0, Math.max(this.maxHeight - this.height, this.currentScroll));
/*  89 */           this.targetScroll = this.currentScroll;
/*     */         } 
/*     */         
/*  92 */         float scrollDiff = (this.maxHeight - scrollBarHeight);
/*  93 */         float scroll = (this.currentScroll * -1);
/*  94 */         int scrollBarY = (int)(this.y + scroll / maxRange * scrollDiff);
/*     */         
/*  96 */         this.scrollHovered = Render2DUtils.isHovered(this.x + this.maxWidth + 5, scrollBarY, 5, scrollBarHeight);
/*     */         
/*  98 */         Render2DUtils.drawRectWH(this.x + this.maxWidth + 5, scrollBarY, 5, scrollBarHeight, -12698050);
/*  99 */         Render2DUtils.drawRectWH(this.x + this.maxWidth + 6, scrollBarY + 1, 3, scrollBarHeight - 2, -13882324);
/*     */       } 
/*     */       
/* 102 */       GL11.glPushMatrix();
/* 103 */       GL11.glTranslatef(0.0F, (int)(this.currentScroll / ResManager.getScaleFactor()), 0.0F);
/*     */     } 
/* 105 */     super.draw(mouseX, mouseY, partialTicks);
/* 106 */     if (this.scrollable) {
/* 107 */       GL11.glPopMatrix();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseScrolled(int delta) {
/* 113 */     super.mouseScrolled(delta);
/*     */     
/* 115 */     if (this.scrollable) {
/* 116 */       this.lastScroll = this.currentScroll;
/* 117 */       this.lastScrollTime = System.currentTimeMillis();
/* 118 */       this.targetScroll += delta;
/* 119 */       this.targetScroll = Math.min(0, Math.max(this.maxHeight - this.height, this.targetScroll));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
/* 125 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     
/* 127 */     if (this.scrollable && this.scrollHovered) {
/* 128 */       this.dragging = true;
/* 129 */       this.dragStartY = Render2DUtils.getMouseY();
/* 130 */       this.scrollStartY = this.currentScroll;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY, int state) {
/* 136 */     super.mouseReleased(mouseX, mouseY, state);
/*     */     
/* 138 */     this.dragging = false;
/*     */   }
/*     */   
/*     */   public void resetScroll() {
/* 142 */     this.lastScroll = this.currentScroll;
/* 143 */     this.lastScrollTime = System.currentTimeMillis();
/* 144 */     this.targetScroll = 0;
/*     */   }
/*     */   
/*     */   public int getCurrentScroll() {
/* 148 */     return this.currentScroll;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\clickgui\components\HBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */