package tr4nt.autofish.scheduler;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import tr4nt.autofish.AutoFish;
import tr4nt.autofish.AutoFishClient;
import tr4nt.autofish.RodEnum;
import tr4nt.autofish.TickEvent;
import tr4nt.autofish.config.ConfigFile;


import java.util.ArrayList;

import static tr4nt.autofish.Utils.*;


public class Ticker implements ClientTickEvents.StartTick {
    public static ArrayList TaskList = new ArrayList();


    @Override
    public void onStartTick(MinecraftClient client) {
//        if (!ConfigFile.getValue("autoplantcrops").getAsBoolean() || !KeyInputHandler.isOn) on = false ;
        ArrayList removeQueue = new ArrayList();
        TaskList.forEach((list) -> {
                ArrayList list1 = (ArrayList) list;
                PlayerEntity player = (PlayerEntity) list1.get(0);
                Hand playerHand = (Hand) list1.get(1);
                long latency = (long) list1.get(2);
                long tick = (long) list1.get(3);
                RodEnum RodEvent = (RodEnum) list1.get(4);


//            AutoPlantCropsClient.LOGGER.info(String.valueOf(Long.compare((tick()-(long) list1.get(2)),(long) list1.get(3))));
                if (Long.compare((tick()-tick),latency)==1)
                {
                    if (client.player == null)
                    {
                        removeQueue.add(list);
                        return;

                    }
                    ItemStack itemStack = player.getStackInHand(playerHand);
                    if (itemStack.getItem().asItem() instanceof FishingRodItem)
                    {
                        player.swingHand(playerHand);
                        if (RodEvent == RodEnum.RELEASE)
                        {
                            swapToNewRod(client, playerHand, AutoFishClient.lastSwappedSlot);

                        } else {
                            if (ConfigFile.getValue("SwapRodIfAlmostBroken").getAsBoolean()) {
                                if (getRodDurability(itemStack) <= 1) {
                                    AutoFishClient.lastSwappedSlot = swapToNewRod(client, playerHand, false);
                                }
                            }
                        }
                        client.interactionManager.interactItem(player, playerHand);
                        AutoFishClient.lastAct = RodEvent;
                        AutoFishClient.lastActTime = tick();
                        if (RodEvent == RodEnum.CATCH && getRodDurability(itemStack) == 1 && ConfigFile.getValue("SwitchRodAfterBroken").getAsBoolean())
                        {
                            swapToNewRod(client, playerHand, true);
                        }


                    }
                    removeQueue.add(list);


                }
            }
        );

        removeQueue.forEach((i)->{TaskList.remove(i);});

    }
}
