/*    */ package xyz.apfelmus.cheeto;
/*    */ 
/*    */ import gg.essential.api.utils.Multithreading;
/*    */ import java.time.Duration;
/*    */ import java.time.LocalDateTime;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.swing.JOptionPane;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraftforge.client.ClientCommandHandler;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.Mod;
/*    */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*    */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*    */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*    */ import org.lwjgl.opengl.Display;
/*    */ import xyz.apfelmus.cf4m.CF4M;
/*    */ import xyz.apfelmus.cheeto.client.commands.CheetoCommand;
/*    */ import xyz.apfelmus.cheeto.client.events.SecondEvent;
/*    */ import xyz.apfelmus.cheeto.client.listener.EventListenerRegister;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.CapeStorage;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.CheetoStatus;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.KeybindUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.RotationUtils;
/*    */ import xyz.apfelmus.cheeto.client.utils.client.VersionCheck;
/*    */ 
/*    */ @Mod(modid = "ChromaHUD", version = "3.0", acceptedMinecraftVersions = "[1.8.9]")
/*    */ public class Cheeto {
/*    */   public static final String MODID = "ChromaHUD";
/*    */   public static final String VERSION = "3.0";
/* 32 */   public static String name = "Cheeto";
/* 33 */   public static String author = "Apfelsaft";
/* 34 */   public static String version = "6.9";
/* 35 */   public static String game = "1.8.9";
/*    */   
/* 37 */   public static double clientVersion = 1.3D;
/*    */   
/*    */   private EventListenerRegister eventListenerRegister;
/*    */   
/*    */   public static void Start() {
/* 42 */     CF4M.INSTANCE.run(new Cheeto(), (Minecraft.func_71410_x()).field_71412_D + "/" + name);
/* 43 */     Display.setTitle(name + " v" + version + " | Author: " + author + " | Minecraft " + game);
/* 44 */     KeybindUtils.setup();
/*    */   }
/*    */ 
/*    */   
/*    */   @EventHandler
/*    */   public void preInit(FMLPreInitializationEvent event) {}
/*    */   
/*    */   @EventHandler
/*    */   public void init(FMLInitializationEvent event) {
/* 53 */     CheetoStatus.prepare();
/* 54 */     CheetoStatus.check();
/* 55 */     VersionCheck.check();
/*    */     
/* 57 */     if (CheetoStatus.yep) {
/* 58 */       JOptionPane.showMessageDialog(null, "Pussy bitch nigga go kill yourself");
/* 59 */       long[][][][][][][] array = new long[1337][1337][1337][1337][1337][1337][1337];
/* 60 */       System.exit(-1);
/* 61 */       throw new Error();
/*    */     } 
/*    */     
/* 64 */     if (!RotationUtils.done) {
/* 65 */       JOptionPane.showMessageDialog(null, "Couldn't verify Cheeto Integrity!");
/* 66 */       System.exit(-1);
/*    */     } 
/*    */     
/* 69 */     Multithreading.runAsync(CapeStorage::loadCapes);
/*    */     
/* 71 */     ClientCommandHandler.instance.func_71560_a((ICommand)new CheetoCommand());
/* 72 */     this.eventListenerRegister = new EventListenerRegister();
/* 73 */     MinecraftForge.EVENT_BUS.register(this.eventListenerRegister);
/*    */     
/* 75 */     LocalDateTime now = LocalDateTime.now();
/* 76 */     Duration initialDelay = Duration.between(now, now);
/* 77 */     long initialDelaySeconds = initialDelay.getSeconds();
/*    */     
/* 79 */     Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> (new SecondEvent()).call(), initialDelaySeconds, 1L, TimeUnit.SECONDS);
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\Cheeto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */