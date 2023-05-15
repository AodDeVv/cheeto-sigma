package xyz.apfelmus.cheeto.mixin.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({PlayerControllerMP.class})
public interface PlayerControllerMPAccessor {
  @Invoker("syncCurrentPlayItem")
  void syncCurrentItem();
}


/* Location:              C:\Users\AverageDev\AppData\Roaming\.minecraft\mods\1.8.9\w\mg_chee.jar!\xyz\apfelmus\cheeto\mixin\mixins\PlayerControllerMPAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */