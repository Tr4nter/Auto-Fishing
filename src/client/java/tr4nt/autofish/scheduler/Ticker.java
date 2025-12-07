package tr4nt.autofish.scheduler;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import tr4nt.autofish.*;
import tr4nt.autofish.config.ConfigFile;


import java.util.ArrayList;
import java.util.Iterator;

import static tr4nt.autofish.Utils.*;


public class Ticker implements ClientTickEvents.StartTick {
    public static ArrayList TaskList = new ArrayList();
    public static boolean iterating = false;

    @Override
    public void onStartTick(MinecraftClient client) {
        if (iterating) return;
        if (TaskList.isEmpty()) return;

        iterating = true;


        Iterator<ArrayList> it = TaskList.iterator();
        while (it.hasNext()) {
            ArrayList list1 = it.next();
            PlayerEntity player = (PlayerEntity) list1.get(0);
            Hand playerHand = (Hand) list1.get(1);
            long latency = (long) list1.get(2);
            long tick = (long) list1.get(3);
            RodEnum RodEvent = (RodEnum) list1.get(4);

            if (client.player == null) {
                it.remove();
                continue;
            }

            if (Utils.tick() - tick <= latency) continue;
            ItemStack itemStack = player.getStackInHand(playerHand);
            if (itemStack == null || itemStack.isEmpty()) {
                it.remove();
                continue;
            }
            if (!(itemStack.getItem() instanceof FishingRodItem))
            {
                it.remove();
                continue;
            }

            player.swingHand(playerHand);
            if (RodEvent == RodEnum.RELEASE) {
                swapToNewRod(client, playerHand, AutoFishClient.lastSwappedSlot);
            } else if (ConfigFile.getValue("SwapRodIfAlmostBroken").getAsBoolean() && Utils.getRodDurability(itemStack) <= 1) {
                AutoFishClient.lastSwappedSlot = swapToNewRod(client, playerHand, false);
            }
            int _retry = 0;

            while (_retry < 2) {
                Object result = client.interactionManager.interactItem(player, playerHand);
                ++_retry;
                String resultString = result.toString().toLowerCase();
                if (resultString.contains("success") || resultString.contains("fishing_rod") && resultString.contains("client")) {
                    break;
                }
            }
            AutoFishClient.lastAct = RodEvent;
            AutoFishClient.lastActTime = Utils.tick();
            if (RodEvent == RodEnum.CATCH && Utils.getRodDurability(itemStack) == 1 && ConfigFile.getValue("SwitchRodAfterBroken").getAsBoolean()) {
                swapToNewRod(client, playerHand, true);
            }

            it.remove();



        }
        iterating = false;
    }
}
