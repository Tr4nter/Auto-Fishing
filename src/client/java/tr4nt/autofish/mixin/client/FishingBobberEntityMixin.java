package tr4nt.autofish.mixin.client;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tr4nt.autofish.AutoFishClient;


@Mixin(FishingBobberEntity.class)
public interface FishingBobberEntityMixin  {


    @Accessor("caughtFish")
    boolean getCaughtFish();



}
