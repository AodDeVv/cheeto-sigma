/*     */ package xyz.apfelmus.cheeto.client.listener;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.client.event.GuiOpenEvent;
/*     */ import net.minecraftforge.client.event.GuiScreenEvent;
/*     */ import net.minecraftforge.client.event.RenderLivingEvent;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.client.event.sound.SoundEvent;
/*     */ import net.minecraftforge.event.entity.player.EntityInteractEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerInteractEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.BackgroundDrawnEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.MouseInputEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.SoundEvent;
/*     */ import xyz.apfelmus.cheeto.client.modules.combat.TermClicker;
/*     */ 
/*     */ public class EventListenerRegister {
/*  22 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  26 */     if (event.phase == TickEvent.Phase.END || mc.field_71439_g == null || mc.field_71441_e == null)
/*  27 */       return;  TickTimer.tick();
/*  28 */     (new ClientTickEvent()).call();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGui(TickEvent.RenderTickEvent event) {
/*  33 */     if (mc.field_71439_g == null || mc.field_71441_e == null || mc.field_71474_y.field_74330_P || (mc.field_71462_r != null && !(mc.field_71462_r instanceof net.minecraft.client.gui.GuiChat) && !(mc.field_71462_r instanceof xyz.apfelmus.cheeto.client.clickgui.ConfigGUI)))
/*  34 */       return;  (new Render2DEvent()).call();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderWorld(RenderWorldLastEvent event) {
/*  39 */     (new Render3DEvent(event.partialTicks)).call();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/*  44 */     (new ClientChatReceivedEvent(event.type, event.message)).call();
/*  45 */     if (event.type == 2)
/*  46 */       return;  if (TermClicker.jkKp660roUwi9fB1())
/*  47 */       return;  String text = event.message.func_150260_c();
/*  48 */     if (text.contains("Im fan of MagiDev") || text.contains("I love MagiDev")) {
/*  49 */       event.setCanceled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onWorldUnload(WorldEvent.Unload event) {
/*  55 */     (new WorldUnloadEvent()).call();
/*     */     
/*  57 */     if (Math.random() > 0.01D) {
/*     */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onGuiOpen(GuiOpenEvent event) {
/*  72 */     (new GuiOpenEvent(event.gui)).call();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onBackgroundDrawn(GuiScreenEvent.BackgroundDrawnEvent event) {
/*  77 */     (new BackgroundDrawnEvent(event.gui)).call();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onBeforeRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
/*  82 */     (new RenderLivingEventPre(event.entity)).call();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onInteract(PlayerInteractEvent event) {
/*  87 */     (new PlayerInteractEvent(event.action, event.pos)).call();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onEntityInteract(EntityInteractEvent event) {
/*  92 */     (new EntityInteractEvent(event)).call();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMouseInput(InputEvent.MouseInputEvent event) {
/*  97 */     (new MouseInputEvent(event)).call();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPlaySound(SoundEvent.SoundSourceEvent event) {
/* 102 */     (new SoundEvent(event.sound, event.uuid, event.name)).call();
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\listener\EventListenerRegister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */