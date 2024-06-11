package tr4nt.autofish;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.event.GameEvent;
import tr4nt.autofish.mixin.client.FishingBobberEntityMixin;

import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import tr4nt.autofish.Utils;

import static tr4nt.autofish.Utils.*;

public class TickEvent implements ClientTickEvents.StartTick{
    public static boolean Catching = false;
    @Override
    public void onStartTick(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return;
        if (player.getMainHandStack().getItem() != Items.FISHING_ROD) return;
        if (player.fishHook == null) return;
        FishingBobberEntity fishHook = player.fishHook;
        FishingBobberEntityMixin fishHookMixin = (FishingBobberEntityMixin) fishHook;
        if (fishHookMixin.getCaughtFish())
        {
            if (Catching) return;
            if (client.interactionManager == null) return;
            Hand handWithRod;
            if (player.getMainHandStack().getItem() == Items.FISHING_ROD)
            {
                handWithRod = Hand.MAIN_HAND;
            } else
            {
                handWithRod = Hand.OFF_HAND;
            }
            queueRodInteraction(client, player, handWithRod, RodEnum.CATCH);
            Catching = true;
            queueRodInteraction(client, player, handWithRod, RodEnum.RELEASE);


        }
    }
}
