/*    */ package xyz.apfelmus.cheeto.client.modules.render;
/*    */ 
/*    */ import xyz.apfelmus.cf4m.annotation.Event;
/*    */ import xyz.apfelmus.cf4m.annotation.Setting;
/*    */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*    */ import xyz.apfelmus.cf4m.module.Category;
/*    */ import xyz.apfelmus.cheeto.client.events.RenderLivingEventPre;
/*    */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*    */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*    */ 
/*    */ 
/*    */ @Module(name = "ShowHiddenMobs", category = Category.RENDER)
/*    */ public class ShowHiddenMobs
/*    */ {
/*    */   @Setting(name = "ShowFels")
/* 16 */   private BooleanSetting showFels = new BooleanSetting(true);
/*    */   @Setting(name = "ShowSA")
/* 18 */   private BooleanSetting showSA = new BooleanSetting(true);
/*    */   @Setting(name = "ShowBloods")
/* 20 */   private BooleanSetting showBloods = new BooleanSetting(true);
/*    */   @Setting(name = "ShowGhosts")
/* 22 */   private BooleanSetting showGhosts = new BooleanSetting(true);
/*    */ 
/*    */   
/*    */   @Event
/*    */   public void onBeforeRenderEntity(RenderLivingEventPre event) {
/* 27 */     if (event.entity.func_82150_aj()) {
/* 28 */       if (this.showFels.isEnabled() && event.entity instanceof net.minecraft.entity.monster.EntityEnderman) {
/* 29 */         event.entity.func_82142_c(false);
/*    */       }
/* 31 */       if (SkyblockUtils.isInDungeon() && event.entity instanceof net.minecraft.entity.player.EntityPlayer) {
/* 32 */         if (this.showSA.isEnabled() && event.entity.func_70005_c_().contains("Shadow Assassin")) {
/* 33 */           event.entity.func_82142_c(false);
/*    */         }
/* 35 */         if (this.showBloods.isEnabled()) {
/* 36 */           for (String name : new String[] { "Revoker", "Psycho", "Reaper", "Cannibal", "Mute", "Ooze", "Putrid", "Freak", "Leech", "Tear", "Parasite", "Flamer", "Skull", "Mr. Dead", "Vader", "Frost", "Walker", "Wandering Soul", "Bonzo", "Scarf", "Livid" }) {
/* 37 */             if (event.entity.func_70005_c_().contains(name)) {
/* 38 */               event.entity.func_82142_c(false);
/*    */               break;
/*    */             } 
/*    */           } 
/*    */         }
/*    */       } 
/* 44 */       if (this.showGhosts.isEnabled() && event.entity instanceof net.minecraft.entity.monster.EntityCreeper && SkyblockUtils.hasLine("The Mist"))
/* 45 */         event.entity.func_82142_c(false); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\render\ShowHiddenMobs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */