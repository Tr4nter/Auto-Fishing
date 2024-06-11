package tr4nt.autofish.scheduler;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import tr4nt.autofish.AutoFish;
import tr4nt.autofish.RodEnum;
import tr4nt.autofish.TickEvent;


import java.util.ArrayList;

import static tr4nt.autofish.Utils.tick;


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
                    if (player.getStackInHand(playerHand).getItem() == Items.FISHING_ROD)
                    {
                        client.interactionManager.interactItem(player, playerHand);
                        if (RodEvent == RodEnum.CATCH) TickEvent.Catching = false;
                    }
                    removeQueue.add(list);


                }
            }
        );

        removeQueue.forEach((i)->{TaskList.remove(i);});

    }
}
