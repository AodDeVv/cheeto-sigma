/*     */ package xyz.apfelmus.cheeto.client.modules.misc;
/*     */ import java.awt.Point;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.Vec3;
/*     */ import xyz.apfelmus.cf4m.annotation.Event;
/*     */ import xyz.apfelmus.cf4m.annotation.Setting;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Enable;
/*     */ import xyz.apfelmus.cf4m.annotation.module.Module;
/*     */ import xyz.apfelmus.cheeto.client.events.ClientTickEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.EntityInteractEvent;
/*     */ import xyz.apfelmus.cheeto.client.events.Render3DEvent;
/*     */ import xyz.apfelmus.cheeto.client.settings.BooleanSetting;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.ColorUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.client.KeybindUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.render.Render3DUtils;
/*     */ import xyz.apfelmus.cheeto.client.utils.skyblock.SkyblockUtils;
/*     */ 
/*     */ @Module(name = "ArrowAlign", category = Category.MISC)
/*     */ public class ArrowAlign {
/*     */   @Setting(name = "ShowClicks", description = "Show needed amount of clicks")
/*  36 */   private static BooleanSetting showClicks = new BooleanSetting(true);
/*     */   @Setting(name = "ClickNeeded", description = "Automatically click the needed amount of clicks when you right click the item frame")
/*  38 */   private static BooleanSetting clickNeeded = new BooleanSetting(true);
/*     */   
/*  40 */   private static BlockPos topLeft = new BlockPos(-2, 125, 79);
/*  41 */   private static BlockPos botRight = new BlockPos(-2, 121, 75);
/*     */ 
/*     */   
/*     */   private static List<BlockPos> box;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  49 */     box = (List<BlockPos>)StreamSupport.stream(BlockPos.func_177980_a(topLeft, botRight).spliterator(), false).sorted((a, b) -> (a.func_177956_o() == b.func_177956_o()) ? (b.func_177952_p() - a.func_177952_p()) : Integer.compare(b.func_177956_o(), a.func_177956_o())).collect(Collectors.toList());
/*     */   }
/*  51 */   private static LinkedHashSet<MazeSpace> grid = new LinkedHashSet<>();
/*  52 */   private static HashMap<Point, Integer> directionSet = new HashMap<>();
/*  53 */   private static int ticks = 0;
/*     */   
/*  55 */   private static EnumFacing[] directions = reverse((EnumFacing[])EnumFacing.field_176754_o.clone());
/*     */   private static boolean clicking = false;
/*     */   
/*     */   private enum SpaceType
/*     */   {
/*  60 */     EMPTY,
/*  61 */     PATH,
/*  62 */     START,
/*  63 */     END;
/*     */   }
/*     */   
/*     */   private static class GridMove {
/*     */     Point point;
/*     */     int directionNum;
/*     */     
/*     */     public GridMove(Point point, int directionNum) {
/*  71 */       this.point = point;
/*  72 */       this.directionNum = directionNum;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MazeSpace {
/*     */     BlockPos framePos;
/*     */     ArrowAlign.SpaceType type;
/*     */     Point coords;
/*     */     
/*     */     public MazeSpace(BlockPos framePos, ArrowAlign.SpaceType type, Point coords) {
/*  82 */       this.framePos = framePos;
/*  83 */       this.type = type;
/*  84 */       this.coords = coords;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  89 */       return this.framePos + " - " + this.type + " - " + this.coords;
/*     */     }
/*     */   }
/*     */   
/*  93 */   private static Minecraft mc = Minecraft.func_71410_x();
/*     */   
/*     */   @Enable
/*     */   public void onEnable() {
/*  97 */     grid.clear();
/*  98 */     directionSet.clear();
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onTick(ClientTickEvent event) {
/* 103 */     if (!SkyblockUtils.isInDungeon())
/*     */       return; 
/* 105 */     if (ticks % 20 == 0) {
/* 106 */       if (mc.field_71439_g.func_174831_c(topLeft) <= 625.0D) {
/* 107 */         if (grid.size() < 25) {
/* 108 */           List<EntityItemFrame> frames = mc.field_71441_e.func_175644_a(EntityItemFrame.class, v -> (v != null && box.contains(v.func_180425_c()) && v.func_82335_i() != null && (v.func_82335_i().func_77973_b().equals(Items.field_151032_g) || v.func_82335_i().func_77973_b().equals(Item.func_150898_a(Blocks.field_150325_L)))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 115 */           if (!frames.isEmpty()) {
/* 116 */             for (int i = 0; i < box.size(); i++) {
/* 117 */               int row = i % 5;
/* 118 */               int column = (int)Math.floor((i / 5.0F));
/* 119 */               Point coords = new Point(row, column);
/* 120 */               int finalI = i;
/*     */ 
/*     */ 
/*     */               
/* 124 */               EntityItemFrame frame = frames.stream().filter(v -> v.func_180425_c().equals(box.get(finalI))).findFirst().orElse(null);
/* 125 */               if (frame != null) {
/* 126 */                 ItemStack item = frame.func_82335_i();
/* 127 */                 SpaceType type = SpaceType.EMPTY;
/*     */                 
/* 129 */                 if (item.func_77973_b() == Items.field_151032_g) {
/* 130 */                   type = SpaceType.PATH;
/* 131 */                 } else if (item.func_77973_b() == Item.func_150898_a(Blocks.field_150325_L)) {
/* 132 */                   if (item.func_77952_i() == 5) {
/* 133 */                     type = SpaceType.START;
/* 134 */                   } else if (item.func_77952_i() == 14) {
/* 135 */                     type = SpaceType.END;
/*     */                   } else {
/* 137 */                     type = SpaceType.PATH;
/*     */                   } 
/*     */                 } 
/* 140 */                 grid.add(new MazeSpace(frame.func_174857_n(), type, coords));
/*     */               } else {
/* 142 */                 grid.add(new MazeSpace(null, SpaceType.EMPTY, coords));
/*     */               } 
/*     */             } 
/*     */           }
/* 146 */         } else if (directionSet.isEmpty()) {
/*     */ 
/*     */           
/* 149 */           List<MazeSpace> startPositions = (List<MazeSpace>)grid.stream().filter(it -> (it.type == SpaceType.START)).collect(Collectors.toList());
/*     */ 
/*     */           
/* 152 */           List<MazeSpace> endPositions = (List<MazeSpace>)grid.stream().filter(it -> (it.type == SpaceType.END)).collect(Collectors.toList());
/*     */           
/* 154 */           int[][] layout = getLayout();
/*     */           
/* 156 */           for (MazeSpace start : startPositions) {
/* 157 */             for (MazeSpace endPosition : endPositions) {
/* 158 */               List<Point> pointMap = solve(layout, start.coords, endPosition.coords);
/*     */               
/* 160 */               if (pointMap.size() == 0)
/* 161 */                 continue;  List<GridMove> moveSet = convertPointMapToMoves(pointMap);
/* 162 */               for (GridMove move : moveSet) {
/* 163 */                 directionSet.put(move.point, Integer.valueOf(move.directionNum));
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/* 169 */       ticks = 0;
/*     */     } 
/* 171 */     ticks++;
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRightClick(EntityInteractEvent event) {
/* 176 */     if (clicking || !SkyblockUtils.isInDungeon())
/*     */       return; 
/* 178 */     if (event.event.target instanceof EntityItemFrame) {
/* 179 */       EntityItemFrame frame = (EntityItemFrame)event.event.target;
/* 180 */       for (MazeSpace space : grid) {
/* 181 */         if (!frame.func_174857_n().equals(space.framePos) || 
/* 182 */           space.type != SpaceType.PATH)
/* 183 */           continue;  int neededClicks = ((Integer)directionSet.getOrDefault(space.coords, Integer.valueOf(0))).intValue() - frame.func_82333_j();
/* 184 */         if (neededClicks == 0) event.event.setCanceled(true); 
/* 185 */         if (neededClicks < 0) neededClicks += 8; 
/* 186 */         if (neededClicks > 1 && clickNeeded.isEnabled()) {
/* 187 */           clicking = true;
/* 188 */           for (int i = 0; i < neededClicks - 1; i++) {
/* 189 */             KeybindUtils.rightClick();
/*     */           }
/* 191 */           clicking = false;
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Event
/*     */   public void onRenderWorld(Render3DEvent event) {
/* 200 */     if (!showClicks.isEnabled())
/* 201 */       return;  for (MazeSpace space : grid) {
/* 202 */       if (space.type != SpaceType.PATH || space.framePos == null)
/* 203 */         continue;  EntityItemFrame frame = mc.field_71441_e.field_72996_f.stream().filter(v -> (v instanceof EntityItemFrame && ((EntityItemFrame)v).func_174857_n().equals(space.framePos))).findFirst().orElse(null);
/* 204 */       if (frame == null)
/* 205 */         continue;  int neededClicks = ((Integer)directionSet.getOrDefault(space.coords, Integer.valueOf(0))).intValue() - frame.func_82333_j();
/* 206 */       if (neededClicks == 0)
/* 207 */         continue;  if (neededClicks < 0) neededClicks += 8; 
/* 208 */       Render3DUtils.draw3DString(
/* 209 */           getVec3RelativeToGrid(space.coords.x, space.coords.y).func_72441_c(0.1D, 0.6D, 0.5D), "" + neededClicks, 
/*     */           
/* 211 */           ColorUtils.getChroma(3000.0F, 0), event.partialTicks);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Event
/*     */   public void onWorldLoad(WorldUnloadEvent event) {
/* 219 */     grid.clear();
/* 220 */     directionSet.clear();
/*     */   }
/*     */   
/*     */   private Vec3 getVec3RelativeToGrid(int row, int column) {
/* 224 */     return new Vec3((Vec3i)topLeft.func_177977_b().func_177964_d(row).func_177979_c(column));
/*     */   }
/*     */   
/*     */   private List<GridMove> convertPointMapToMoves(List<Point> solution) {
/* 228 */     Collections.reverse(solution);
/* 229 */     List<GridMove> moves = new ArrayList<>();
/*     */     
/* 231 */     for (int i = 0; i < solution.size() - 1; i++) {
/* 232 */       Point current = solution.get(i);
/* 233 */       Point next = solution.get(i + 1);
/* 234 */       int diffX = current.x - next.x;
/* 235 */       int diffY = current.y - next.y;
/*     */       
/* 237 */       for (EnumFacing dir : EnumFacing.field_176754_o) {
/* 238 */         int dirX = dir.func_176730_m().func_177958_n();
/* 239 */         int dirY = dir.func_176730_m().func_177952_p();
/*     */         
/* 241 */         if (dirX == diffX && dirY == diffY) {
/* 242 */           int rotation = 0;
/* 243 */           switch (dir.func_176734_d()) {
/*     */             case NORTH:
/* 245 */               rotation = 7;
/*     */               break;
/*     */             case SOUTH:
/* 248 */               rotation = 3;
/*     */               break;
/*     */             case WEST:
/* 251 */               rotation = 5;
/*     */               break;
/*     */             case EAST:
/* 254 */               rotation = 1;
/*     */               break;
/*     */           } 
/* 257 */           moves.add(new GridMove(current, rotation));
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 262 */     Collections.reverse(solution);
/* 263 */     return moves;
/*     */   }
/*     */   
/*     */   private static List<Point> solve(int[][] grid, Point start, Point end) {
/* 267 */     LinkedList<Point> queue = new LinkedList<>();
/* 268 */     Point[][] gridCopy = new Point[grid.length][(grid[0]).length];
/*     */     
/* 270 */     queue.addLast(start);
/* 271 */     gridCopy[start.y][start.x] = start;
/*     */     
/* 273 */     while (queue.size() != 0) {
/* 274 */       Point currPos = queue.pollFirst();
/*     */       
/* 276 */       for (EnumFacing dir : directions) {
/* 277 */         Point nextPos = move(grid, gridCopy, currPos, dir);
/*     */ 
/*     */         
/* 280 */         queue.addLast(nextPos);
/* 281 */         gridCopy[nextPos.y][nextPos.x] = new Point(currPos.x, currPos.y);
/*     */         
/* 283 */         if (nextPos != null && end.equals(new Point(nextPos.x, nextPos.y))) {
/* 284 */           List<Point> steps = new ArrayList<>();
/*     */           
/* 286 */           Point tmp = currPos;
/* 287 */           int count = 0;
/* 288 */           steps.add(nextPos);
/* 289 */           steps.add(currPos);
/* 290 */           while (tmp != start) {
/* 291 */             count++;
/* 292 */             tmp = gridCopy[tmp.y][tmp.x];
/* 293 */             steps.add(tmp);
/*     */           } 
/* 295 */           return steps;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 300 */     return new ArrayList<>();
/*     */   }
/*     */   
/*     */   private static Point move(int[][] grid, Point[][] gridCopy, Point currPos, EnumFacing dir) {
/* 304 */     int x = currPos.x;
/* 305 */     int y = currPos.y;
/* 306 */     int diffX = dir.func_176730_m().func_177958_n();
/* 307 */     int diffY = dir.func_176730_m().func_177952_p();
/* 308 */     int i = (x + diffX >= 0 && x + diffX < (grid[0]).length && y + diffY >= 0 && y + diffY < grid.length && grid[y + diffY][x + diffX] != 1) ? 1 : 0;
/*     */     
/* 310 */     if (gridCopy[y + i * diffY][x + i * diffX] != null)
/*     */     {
/* 312 */       return null;
/*     */     }
/* 314 */     return new Point(x + i * diffX, y + i * diffY);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[][] getLayout() {
/* 319 */     int[][] ret = new int[5][5];
/*     */     
/* 321 */     for (int row = 0; row < 5; row++) {
/* 322 */       for (int col = 0; col < 5; col++) {
/* 323 */         int finalRow = row;
/* 324 */         int finalCol = col;
/*     */ 
/*     */ 
/*     */         
/* 328 */         MazeSpace space = grid.stream().filter(it -> it.coords.equals(new Point(finalRow, finalCol))).findFirst().orElse(null);
/* 329 */         ret[col][row] = (space != null) ? ((space.framePos != null) ? 0 : 1) : 1;
/*     */       } 
/*     */     } 
/*     */     
/* 333 */     return ret;
/*     */   }
/*     */   
/*     */   private static EnumFacing[] reverse(EnumFacing[] array) {
/* 337 */     for (int i = 0; i < array.length / 2; i++) {
/* 338 */       EnumFacing temp = array[i];
/* 339 */       array[i] = array[array.length - i - 1];
/* 340 */       array[array.length - i - 1] = temp;
/*     */     } 
/*     */     
/* 343 */     return array;
/*     */   }
/*     */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\client\modules\misc\ArrowAlign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */