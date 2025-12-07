package tr4nt.autofish;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

import net.minecraft.world.event.GameEvent;
import tr4nt.autofish.config.ConfigFile;
import tr4nt.autofish.mixin.client.FishingBobberEntityMixin;

import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import tr4nt.autofish.Utils;
import tr4nt.autofish.scheduler.Ticker;

import static tr4nt.autofish.Utils.*;

public class TickEvent implements ClientTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return;
        if (getHandWithRod(client) == null) return;
        if (player.fishHook == null) return;
        FishingBobberEntity fishHook = player.fishHook;

        FishingBobberEntityMixin fishHookMixin = (FishingBobberEntityMixin)  fishHook;
        if (fishHookMixin.getCaughtFish())
        {
            if (!Ticker.TaskList.isEmpty()) return;
            if (client.interactionManager == null) return;

            Hand handWithRod = getHandWithRod(client);
            if (handWithRod == null) return;


            ItemStack fishingRodStack = player.getStackInHand(handWithRod);



            queueRodInteraction(client, player, handWithRod, RodEnum.CATCH);
            int rodDurability = getRodDurability(fishingRodStack);
            if (ConfigFile.getValue("StopCastIfAlmostBroken").getAsBoolean() && AutoFishClient.lastSwappedSlot == -1)
            {

                if (rodDurability == 2) {
                    return;
                }
            }

            queueRodInteraction(client, player, handWithRod, RodEnum.RELEASE);


        }
    }
}
