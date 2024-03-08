package tr4nt.autofish;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.event.GameEvent;
import tr4nt.autofish.mixin.client.FishingBobberEntityMixin;

import java.time.Instant;
import java.util.Date;

import tr4nt.autofish.Utils;

import static tr4nt.autofish.Utils.getLatency;
import static tr4nt.autofish.Utils.tick;

public class TickEvent implements ClientTickEvents.StartTick{
    private long currentTime = tick();
    @Override
    public void onStartTick(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return;
        if (player.getMainHandStack().getItem() != Items.FISHING_ROD) return;
        if (player.fishHook == null) return;

        if (((FishingBobberEntityMixin) player.fishHook).getCaughtFish())
        {

            if (!(Long.compare(tick()-currentTime, getLatency(client)*10) == 1)) return;


            assert client.interactionManager != null;
            currentTime = tick();

            client.interactionManager.interactItem(player, player.getActiveHand());

            if (player.getMainHandStack().getItem() == Items.FISHING_ROD)
            {
                client.interactionManager.interactItem(player, player.getActiveHand());
                return;
            };
        }
    }
}
