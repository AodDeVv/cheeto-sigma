/*     */ package xyz.apfelmus.cheeto.mixin.mixins;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import xyz.apfelmus.cf4m.CF4M;
/*     */ import xyz.apfelmus.cf4m.event.events.KeyboardEvent;
/*     */ import xyz.apfelmus.cheeto.Cheeto;
/*     */ import xyz.apfelmus.cheeto.client.events.LeftClickEvent;
/*     */ import xyz.apfelmus.cheeto.client.modules.world.AutoFarm;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.ResManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Mixin({Minecraft.class})
/*     */ public class MixinMinecraft
/*     */ {
/*     */   @Shadow
/*     */   public GuiScreen field_71462_r;
/*     */   @Shadow
/*     */   public GameSettings field_71474_y;
/*     */   @Shadow
/*     */   public boolean field_71415_G;
/*     */   @Shadow
/*     */   public MovingObjectPosition field_71476_x;
/*     */   @Shadow
/*     */   private Entity field_175622_Z;
/*     */   @Shadow
/*     */   public PlayerControllerMP field_71442_b;
/*     */   @Shadow
/*     */   public WorldClient field_71441_e;
/*     */   @Shadow
/*     */   public EntityPlayerSP field_71439_g;
/*     */   
/*     */   @Inject(method = {"startGame"}, at = {@At("RETURN")})
/*     */   private void startGame(CallbackInfo ci) {
/*  55 */     Cheeto.Start();
/*     */   }
/*     */   
/*     */   @Inject(method = {"runTick"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V")})
/*     */   private void dispatchKeypresses(CallbackInfo ci) {
/*  60 */     if (Keyboard.getEventKeyState() && this.field_71462_r == null) {
/*  61 */       (new KeyboardEvent((Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + 256) : Keyboard.getEventKey())).call();
/*     */     }
/*     */   }
/*     */   
/*     */   @Inject(method = {"clickMouse"}, at = {@At("HEAD")})
/*     */   private void onLeftClick(CallbackInfo ci) {
/*  67 */     (new LeftClickEvent(ci)).call();
/*     */   }
/*     */   
/*     */   @Inject(method = {"sendClickBlockToController"}, at = {@At("RETURN")})
/*     */   private void sendClickBlockToController(CallbackInfo ci) {
/*  72 */     AutoFarm af = (AutoFarm)CF4M.INSTANCE.moduleManager.getModule("AutoFarm");
/*  73 */     int extraClicks = af.fastBreak.getCurrent().intValue();
/*  74 */     boolean shouldClick = (this.field_71462_r == null && this.field_71474_y.field_74312_F.func_151470_d() && this.field_71415_G);
/*     */     
/*  76 */     if (this.field_71476_x != null && 
/*  77 */       CF4M.INSTANCE.moduleManager.isEnabled(af) && extraClicks > 0 && shouldClick) {
/*  78 */       for (int i = 0; i < extraClicks; ) {
/*  79 */         BlockPos clickedBlock = this.field_71476_x.func_178782_a();
/*  80 */         this.field_71476_x = this.field_175622_Z.func_174822_a(this.field_71442_b.func_78757_d(), 1.0F);
/*  81 */         if (this.field_71476_x != null && this.field_71476_x.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
/*  82 */           BlockPos newBlock = this.field_71476_x.func_178782_a();
/*  83 */           if (!newBlock.equals(clickedBlock) && this.field_71441_e.func_180495_p(newBlock).func_177230_c() != Blocks.field_150350_a)
/*     */           {
/*  85 */             this.field_71442_b.func_180511_b(newBlock, this.field_71476_x.field_178784_b);
/*     */           }
/*     */           i++;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Inject(method = {"resize"}, at = {@At("RETURN")})
/*     */   private void onResize(int width, int height, CallbackInfo ci) {
/*  97 */     ResManager.setScaleFactor((new ScaledResolution(Minecraft.func_71410_x())).func_78325_e());
/*     */   }
/*     */   
/*     */   @Inject(method = {"toggleFullscreen"}, at = {@At("RETURN")})
/*     */   private void onFullscreen(CallbackInfo ci) {
/* 102 */     ResManager.setScaleFactor((new ScaledResolution(Minecraft.func_71410_x())).func_78325_e());
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\MixinMinecraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */