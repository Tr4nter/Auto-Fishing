package tr4nt.autofish.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.MinecartItem;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tr4nt.autofish.AutoFishClient;
import tr4nt.autofish.RodEnum;

import static tr4nt.autofish.Utils.getHandWithRod;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(at=@At("HEAD"), method="interactItem", cancellable = true)
    private void use(CallbackInfoReturnable<?> cir)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        Hand h = getHandWithRod(client);
        if (h != null)
        {
            if (AutoFishClient.lastAct == RodEnum.RELEASE)
            {
                AutoFishClient.lastAct = RodEnum.CATCH;
            }
        }

    }
}
