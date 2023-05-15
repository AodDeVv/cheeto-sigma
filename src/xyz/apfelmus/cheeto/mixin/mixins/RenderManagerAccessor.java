package xyz.apfelmus.cheeto.mixin.mixins;

import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({RenderManager.class})
public interface RenderManagerAccessor {
  @Accessor
  double getRenderPosX();
  
  @Accessor
  double getRenderPosY();
  
  @Accessor
  double getRenderPosZ();
}


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\RenderManagerAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */