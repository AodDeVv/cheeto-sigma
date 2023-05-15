/*    */ package xyz.apfelmus.cheeto.client.utils.client;
/*    */ 
/*    */ public class Rotation {
/*    */   private float yaw;
/*    */   private float pitch;
/*    */   
/*    */   public Rotation(float yaw, float pitch) {
/*  8 */     this.yaw = yaw;
/*  9 */     this.pitch = pitch;
/*    */   }
/*    */   
/*    */   public float getYaw() {
/* 13 */     return this.yaw;
/*    */   }
/*    */   
/*    */   public void setYaw(float yaw) {
/* 17 */     this.yaw = yaw;
/*    */   }
/*    */   
/*    */   public float getPitch() {
/* 21 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public void setPitch(float pitch) {
/* 25 */     this.pitch = pitch;
/*    */   }
/*    */   
/*    */   public void addYaw(float yaw) {
/* 29 */     this.yaw += yaw;
/*    */   }
/*    */   
/*    */   public void addPitch(float pitch) {
/* 33 */     this.pitch += pitch;
/*    */   }
/*    */   
/*    */   public float getValue() {
/* 37 */     return Math.abs(this.yaw) + Math.abs(this.pitch);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return "Rotation{yaw=" + this.yaw + ", pitch=" + this.pitch + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\clien\\utils\client\Rotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */