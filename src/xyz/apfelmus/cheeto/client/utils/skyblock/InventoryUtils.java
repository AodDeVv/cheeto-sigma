/*     */ package xyz.apfelmus.cheeto.client.utils.skyblock;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.ContainerChest;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class InventoryUtils {
/*  16 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   public static String getInventoryName() {
/*  19 */     if (mc.field_71462_r instanceof net.minecraft.client.gui.inventory.GuiChest) {
/*  20 */       ContainerChest chest = (ContainerChest)mc.field_71439_g.field_71070_bA;
/*  21 */       IInventory inv = chest.func_85151_d();
/*     */       
/*  23 */       return inv.func_145818_k_() ? inv.func_70005_c_() : null;
/*     */     } 
/*     */     
/*  26 */     return null;
/*     */   }
/*     */   
/*     */   public static ItemStack getStackInSlot(int slot) {
/*  30 */     return mc.field_71439_g.field_71071_by.func_70301_a(slot);
/*     */   }
/*     */   
/*     */   public static ItemStack getStackInOpenContainerSlot(int slot) {
/*  34 */     if (((Slot)mc.field_71439_g.field_71070_bA.field_75151_b.get(slot)).func_75216_d()) {
/*  35 */       return ((Slot)mc.field_71439_g.field_71070_bA.field_75151_b.get(slot)).func_75211_c();
/*     */     }
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getSlotForItem(String itemName, Item item) {
/*  42 */     for (Slot slot : mc.field_71439_g.field_71070_bA.field_75151_b) {
/*  43 */       if (slot.func_75216_d()) {
/*  44 */         ItemStack is = slot.func_75211_c();
/*  45 */         if (is.func_77973_b() == item && is.func_82833_r().contains(itemName)) {
/*  46 */           return slot.field_75222_d;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  51 */     return -1;
/*     */   }
/*     */   
/*     */   public static void clickOpenContainerSlot(int slot, int nextWindow) {
/*  55 */     int id = slot + nextWindow;
/*  56 */     if (id > 100) id -= 100; 
/*  57 */     mc.field_71442_b.func_78753_a(id, slot, 0, 0, (EntityPlayer)mc.field_71439_g);
/*     */   }
/*     */   
/*     */   public static void clickOpenContainerSlot(int slot) {
/*  61 */     mc.field_71442_b.func_78753_a(mc.field_71439_g.field_71070_bA.field_75152_c, slot, 0, 0, (EntityPlayer)mc.field_71439_g);
/*     */   }
/*     */   
/*     */   public static int getAvailableHotbarSlot(String name) {
/*  65 */     for (int i = 0; i < 8; i++) {
/*  66 */       ItemStack is = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*  67 */       if (is == null || is.func_82833_r().contains(name)) {
/*  68 */         return i;
/*     */       }
/*     */     } 
/*  71 */     return -1;
/*     */   }
/*     */   
/*     */   public static List<Integer> getAllSlots(int throwSlot, String name) {
/*  75 */     List<Integer> ret = new ArrayList<>();
/*     */     
/*  77 */     for (int i = 9; i < 44; i++) {
/*  78 */       ItemStack is = ((Slot)mc.field_71439_g.field_71069_bz.field_75151_b.get(i)).func_75211_c();
/*  79 */       if (is != null && is.func_82833_r().contains(name) && i - 36 != throwSlot) {
/*  80 */         ret.add(Integer.valueOf(i));
/*     */       }
/*     */     } 
/*     */     
/*  84 */     return ret;
/*     */   }
/*     */   
/*     */   public static void throwSlot(int slot) {
/*  88 */     ItemStack curInSlot = mc.field_71439_g.field_71071_by.func_70301_a(slot);
/*     */     
/*  90 */     if (curInSlot != null) {
/*  91 */       if (curInSlot.func_82833_r().contains("Snowball")) {
/*  92 */         int ss = curInSlot.field_77994_a;
/*  93 */         for (int i = 0; i < ss; i++) {
/*  94 */           mc.field_71439_g.field_71071_by.field_70461_c = slot;
/*  95 */           mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.field_71071_by.func_70301_a(slot));
/*     */         } 
/*     */       } else {
/*  98 */         mc.field_71439_g.field_71071_by.field_70461_c = slot;
/*  99 */         mc.field_71442_b.func_78769_a((EntityPlayer)mc.field_71439_g, (World)mc.field_71441_e, mc.field_71439_g.field_71071_by.func_70301_a(slot));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getAmountInHotbar(String item) {
/* 105 */     for (int i = 0; i < 8; i++) {
/* 106 */       ItemStack is = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 107 */       if (is != null && StringUtils.func_76338_a(is.func_82833_r()).equals(item)) {
/* 108 */         return is.field_77994_a;
/*     */       }
/*     */     } 
/*     */     
/* 112 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getItemInHotbar(String itemName) {
/* 116 */     for (int i = 0; i < 8; i++) {
/* 117 */       ItemStack is = mc.field_71439_g.field_71071_by.func_70301_a(i);
/* 118 */       if (is != null && StringUtils.func_76338_a(is.func_82833_r()).contains(itemName)) {
/* 119 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 123 */     return -1;
/*     */   }
/*     */   
/*     */   public static List<ItemStack> getInventoryStacks() {
/* 127 */     List<ItemStack> ret = new ArrayList<>();
/* 128 */     for (int i = 9; i < 44; i++) {
/* 129 */       Slot slot = mc.field_71439_g.field_71069_bz.func_75139_a(i);
/* 130 */       if (slot != null) {
/* 131 */         ItemStack stack = slot.func_75211_c();
/* 132 */         if (stack != null) {
/* 133 */           ret.add(stack);
/*     */         }
/*     */       } 
/*     */     } 
/* 137 */     return ret;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\skyblock\InventoryUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */