package tr4nt.autofish.mixin.client;

import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FishingBobberEntity.class)
public interface FishingBobberEntityMixin {

    @Accessor("caughtFish")
    boolean getCaughtFish();
}
