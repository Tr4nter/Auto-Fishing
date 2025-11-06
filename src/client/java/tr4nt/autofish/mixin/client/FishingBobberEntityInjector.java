package tr4nt.autofish.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tr4nt.autofish.AutoFishClient;
import tr4nt.autofish.RodEnum;
import tr4nt.autofish.config.ConfigFile;

import static tr4nt.autofish.Utils.*;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityInjector {
    @Shadow @Nullable public abstract PlayerEntity getPlayerOwner();

    @Inject(at=@At("TAIL"), method="onRemoved")
    private void onRemoved(CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (this.getPlayerOwner() != client.player) return;
        if (!ConfigFile.getValue("RecastIfBobberKilled").getAsBoolean()) return;
        if (AutoFishClient.lastAct == RodEnum.CATCH) return;

        if ((tick()-AutoFishClient.lastActTime) >= ConfigFile.getValue("BobberKillTimeThreshold").getAsInt())
        {


            Hand h = getHandWithRod(client);
            if (h==null) return;
            queueRodInteraction(client, client.player, getHandWithRod(client), RodEnum.RELEASE);
        }
    }

    @Inject(at=@At("TAIL"), method= "<init>*")
    private void onInit(CallbackInfo info) {
        AutoFishClient.lastAct = RodEnum.RELEASE;
    }
}
