/*     */ package xyz.apfelmus.cheeto.client.utils.skyblock;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import xyz.apfelmus.cheeto.mixin.mixins.PlayerControllerMPAccessor;
/*     */ 
/*     */ public class SkyblockUtils {
/*  25 */   private static Minecraft mc = Minecraft.func_71410_x();
/*  26 */   private static final ArrayList<Block> interactables = new ArrayList<>(Arrays.asList(new Block[] { Blocks.field_180410_as, Blocks.field_150467_bQ, (Block)Blocks.field_150461_bJ, Blocks.field_150324_C, Blocks.field_180412_aq, Blocks.field_150382_bo, Blocks.field_150483_bI, Blocks.field_150462_ai, (Block)Blocks.field_150486_ae, Blocks.field_180409_at, (Block)Blocks.field_150453_bW, (Block)Blocks.field_180402_cm, Blocks.field_150367_z, Blocks.field_150409_cd, Blocks.field_150381_bn, Blocks.field_150477_bB, Blocks.field_150460_al, (Block)Blocks.field_150438_bZ, Blocks.field_180411_ar, Blocks.field_150442_at, Blocks.field_150323_B, (Block)Blocks.field_150455_bV, (Block)Blocks.field_150441_bU, (Block)Blocks.field_150416_aS, (Block)Blocks.field_150413_aR, Blocks.field_150472_an, Blocks.field_150444_as, Blocks.field_150415_aT, Blocks.field_150447_bR, Blocks.field_150471_bO, Blocks.field_150430_aB, Blocks.field_180413_ao, (Block)Blocks.field_150465_bP }));
/*     */   
/*     */   public static int getItemInSlot(String itemName) {
/*  29 */     for (int i = 0; i < 8; i++) {
/*  30 */       ItemStack is = mc.field_71439_g.field_71071_by.func_70301_a(i);
/*     */       
/*  32 */       if (is != null && 
/*  33 */         is.func_82833_r().contains(itemName)) {
/*  34 */         return i;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  39 */     return -1;
/*     */   }
/*     */   
/*     */   public enum Location {
/*  43 */     ISLAND,
/*  44 */     HUB,
/*  45 */     LIFT,
/*  46 */     SKYBLOCK,
/*  47 */     LOBBY,
/*  48 */     LIMBO,
/*  49 */     THE_MIST,
/*  50 */     NONE;
/*     */   }
/*     */   
/*     */   public static void ghostBlock() {
/*  54 */     if (mc.field_71476_x.func_178782_a() == null)
/*     */       return; 
/*  56 */     Block block = (Minecraft.func_71410_x()).field_71441_e.func_180495_p(mc.field_71476_x.func_178782_a()).func_177230_c();
/*  57 */     if (!interactables.contains(block)) {
/*  58 */       mc.field_71441_e.func_175698_g(mc.field_71476_x.func_178782_a());
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getMobHp(EntityArmorStand aStand) {
/*  63 */     double mobHp = -1.0D;
/*  64 */     String abbr = "";
/*  65 */     Pattern pattern = Pattern.compile(" ?.+? ([.\\d]+)([Mk]?)/[.\\d]+[Mk]? ?");
/*  66 */     String stripped = stripString(aStand.func_70005_c_());
/*  67 */     Matcher mat = pattern.matcher(stripped);
/*     */     
/*  69 */     if (stripped.contains("Grinch")) {
/*  70 */       return 1;
/*     */     }
/*     */     
/*  73 */     if (mat.matches()) {
/*     */       try {
/*  75 */         mobHp = Double.parseDouble(mat.group(1));
/*  76 */         abbr = mat.group(2);
/*  77 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */     
/*  81 */     if (mobHp != -1.0D) {
/*  82 */       mobHp *= (abbr.equals("k") ? 'Ϩ' : (abbr.equals("M") ? 1000000 : true));
/*     */     }
/*     */     
/*  85 */     return (int)Math.ceil(mobHp);
/*     */   }
/*     */   
/*     */   public static int getSlayerHp(EntityArmorStand slayerStand) {
/*  89 */     double mobHp = -1.0D;
/*  90 */     String abbr = "";
/*  91 */     Pattern pattern = Pattern.compile(".+? ([.\\d]+)([Mk]?)");
/*  92 */     String stripped = stripString(slayerStand.func_70005_c_());
/*  93 */     Matcher mat = pattern.matcher(stripped);
/*     */     
/*  95 */     if (mat.matches()) {
/*     */       try {
/*  97 */         mobHp = Double.parseDouble(mat.group(1));
/*  98 */         abbr = mat.group(2);
/*  99 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */     
/* 103 */     if (mobHp != -1.0D) {
/* 104 */       mobHp *= (abbr.equals("k") ? 'Ϩ' : (abbr.equals("M") ? 1000000 : true));
/*     */     }
/*     */     
/* 107 */     return (int)Math.ceil(mobHp);
/*     */   }
/*     */   
/*     */   public static int getJacobsCount() {
/* 111 */     if (mc != null && mc.field_71439_g != null) {
/* 112 */       ScoreObjective sbo = mc.field_71441_e.func_96441_U().func_96539_a(1);
/* 113 */       if (sbo != null) {
/* 114 */         List<String> scoreboard = getSidebarLines();
/* 115 */         scoreboard.add(StringUtils.func_76338_a(sbo.func_96678_d()));
/* 116 */         for (String s : scoreboard) {
/* 117 */           String validated = stripString(s);
/*     */           
/* 119 */           if (validated.contains("Collected") || validated.contains("with")) {
/* 120 */             String[] split = validated.split(" ");
/* 121 */             if (split.length == 3)
/* 122 */               return Integer.parseInt(split[2].replace(",", "")); 
/* 123 */             if (split.length == 4) {
/* 124 */               return Integer.parseInt(split[3].replace(",", ""));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     return -1;
/*     */   }
/*     */   
/*     */   public static Entity getEntityCuttingOtherEntity(Entity e, Class<?> entityType) {
/* 135 */     List<Entity> possible = mc.field_71441_e.func_175674_a(e, e.func_174813_aQ().func_72314_b(0.3D, 2.0D, 0.3D), a -> (!a.field_70128_L && !a.equals(mc.field_71439_g) && !(a instanceof net.minecraft.entity.projectile.EntityFireball) && !(a instanceof net.minecraft.entity.projectile.EntityFishHook) && (entityType == null || entityType.isInstance(a))));
/* 136 */     if (!possible.isEmpty()) {
/* 137 */       return Collections.<Entity>min(possible, Comparator.comparing(e2 -> Float.valueOf(e2.func_70032_d(e))));
/*     */     }
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Location getLocation() {
/* 145 */     if (isInIsland()) return Location.ISLAND;
/*     */     
/* 147 */     if (isInHub()) return Location.HUB;
/*     */     
/* 149 */     if (isAtLift()) return Location.LIFT;
/*     */     
/* 151 */     if (isInMist()) return Location.THE_MIST;
/*     */     
/* 153 */     if (isInSkyblock()) return Location.SKYBLOCK;
/*     */     
/* 155 */     if (isInLobby()) return Location.LOBBY;
/*     */     
/* 157 */     IBlockState ibs = mc.field_71441_e.func_180495_p(mc.field_71439_g.func_180425_c().func_177977_b());
/* 158 */     if (ibs != null && 
/* 159 */       ibs.func_177230_c() == Blocks.field_150344_f) return Location.LIMBO;
/*     */ 
/*     */     
/* 162 */     return Location.NONE;
/*     */   }
/*     */   
/*     */   public static boolean isInIsland() {
/* 166 */     return hasLine("Your Island");
/*     */   }
/*     */   
/*     */   public static boolean isJacobs() {
/* 170 */     return hasLine("Jacob's Contest");
/*     */   }
/*     */   
/*     */   public static boolean isInHub() {
/* 174 */     return (hasLine("Village") && !hasLine("Dwarven"));
/*     */   }
/*     */   public static boolean isInDojo() {
/* 177 */     return hasLine("Challenge:");
/*     */   }
/*     */   public static boolean isAtLift() {
/* 180 */     return hasLine("The Lift");
/*     */   }
/*     */   
/*     */   public static boolean isInDungeon() {
/* 184 */     return (hasLine("Cleared:") || hasLine("Start"));
/*     */   }
/*     */   
/*     */   public static boolean isInFloor(String floor) {
/* 188 */     return hasLine("The Catacombs (" + floor + ")");
/*     */   }
/*     */   
/*     */   public static boolean slayerBossSpawned() {
/* 192 */     return hasLine("Slay the boss!");
/*     */   }
/*     */   
/*     */   public static boolean isInSkyblock() {
/* 196 */     return hasLine("SKYBLOCK");
/*     */   }
/*     */   
/*     */   public static boolean isInLobby() {
/* 200 */     return (hasLine("HYPIXEL") || hasLine("PROTOTYPE"));
/*     */   }
/*     */   
/*     */   public static boolean isInMist() {
/* 204 */     return hasLine("The Mist");
/*     */   }
/*     */   
/*     */   public static boolean hasLine(String sbString) {
/* 208 */     if (mc != null && mc.field_71439_g != null) {
/* 209 */       ScoreObjective sbo = mc.field_71441_e.func_96441_U().func_96539_a(1);
/* 210 */       if (sbo != null) {
/* 211 */         List<String> scoreboard = getSidebarLines();
/* 212 */         scoreboard.add(StringUtils.func_76338_a(sbo.func_96678_d()));
/* 213 */         for (String s : scoreboard) {
/* 214 */           String validated = stripString(s);
/*     */           
/* 216 */           if (validated.contains(sbString)) {
/* 217 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 223 */     return false;
/*     */   }
/*     */   
/*     */   public static String stripString(String s) {
/* 227 */     char[] nonValidatedString = StringUtils.func_76338_a(s).toCharArray();
/* 228 */     StringBuilder validated = new StringBuilder();
/*     */     
/* 230 */     for (char a : nonValidatedString) {
/* 231 */       if (a < '' && a > '\024') {
/* 232 */         validated.append(a);
/*     */       }
/*     */     } 
/*     */     
/* 236 */     return validated.toString();
/*     */   }
/*     */   
/*     */   private static List<String> getSidebarLines() {
/* 240 */     List<String> lines = new ArrayList<>();
/* 241 */     Scoreboard scoreboard = (Minecraft.func_71410_x()).field_71441_e.func_96441_U();
/* 242 */     if (scoreboard == null) {
/* 243 */       return lines;
/*     */     }
/*     */     
/* 246 */     ScoreObjective objective = scoreboard.func_96539_a(1);
/*     */     
/* 248 */     if (objective == null) {
/* 249 */       return lines;
/*     */     }
/*     */     
/* 252 */     Collection<Score> scores = scoreboard.func_96534_i(objective);
/*     */     
/* 254 */     List<Score> list = new ArrayList<>();
/*     */     
/* 256 */     for (Score s : scores) {
/* 257 */       if (s != null && s.func_96653_e() != null && !s.func_96653_e().startsWith("#")) {
/* 258 */         list.add(s);
/*     */       }
/*     */     } 
/*     */     
/* 262 */     if (list.size() > 15) {
/* 263 */       scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
/*     */     } else {
/* 265 */       scores = list;
/*     */     } 
/*     */     
/* 268 */     for (Score score : scores) {
/* 269 */       ScorePlayerTeam team = scoreboard.func_96509_i(score.func_96653_e());
/* 270 */       lines.add(ScorePlayerTeam.func_96667_a((Team)team, score.func_96653_e()));
/*     */     } 
/*     */     
/* 273 */     return lines;
/*     */   }
/*     */   
/*     */   public static void silentUse(int mainSlot, int useSlot) {
/* 277 */     int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     
/* 279 */     if (useSlot > 0 && useSlot <= 9) {
/* 280 */       mc.field_71439_g.field_71071_by.field_70461_c = useSlot - 1;
/* 281 */       ((PlayerControllerMPAccessor)mc.field_71442_b).syncCurrentItem();
/* 282 */       mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(mc.field_71439_g.field_71071_by.func_70448_g()));
/*     */     } 
/*     */     
/* 285 */     if (mainSlot > 0 && mainSlot <= 9) {
/* 286 */       mc.field_71439_g.field_71071_by.field_70461_c = mainSlot - 1;
/* 287 */     } else if (mainSlot == 0) {
/* 288 */       mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void silentClick(int mainSlot, int useSlot) {
/* 293 */     int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
/*     */     
/* 295 */     if (useSlot > 0 && useSlot <= 9) {
/* 296 */       mc.field_71439_g.field_71071_by.field_70461_c = useSlot - 1;
/* 297 */       ((PlayerControllerMPAccessor)mc.field_71442_b).syncCurrentItem();
/* 298 */       KeybindUtils.leftClick();
/*     */     } 
/*     */     
/* 301 */     if (mainSlot > 0 && mainSlot <= 9) {
/* 302 */       mc.field_71439_g.field_71071_by.field_70461_c = mainSlot - 1;
/* 303 */     } else if (mainSlot == 0) {
/* 304 */       mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\skyblock\SkyblockUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */