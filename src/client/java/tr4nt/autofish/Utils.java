package tr4nt.autofish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import tr4nt.autofish.config.ConfigFile;
import tr4nt.autofish.mixin.client.PlayerInventoryAccessor;
import tr4nt.autofish.scheduler.Ticker;

import java.io.ObjectInputFilter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Utils {
    public static long tick()
    {
        return Date.from(Instant.now()).getTime();
    }

    public static long getLatency(MinecraftClient client) {
        ServerInfo sinf = client.getCurrentServerEntry();
        if (sinf == null) return 0;
        return sinf.ping;
    }

    public static boolean isNumber(String val) {
        try {
            Integer.parseInt(val);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static HashMap<String, String> newOption(String s1, String s2) {
        HashMap<String, String> temp = new HashMap<>();
        temp.put(s1, s2);
        return temp;
    }

    public static int getRodDurability(ItemStack rodStack)
    {
        return rodStack.getMaxDamage() - rodStack.getDamage();
    }

    public static int convertToScreenHandlerSlot(int slot) {
        if (slot >= 0 && slot <= 8) {
            return 36 + slot;
        } else if (slot >= 9 && slot <= 35) {
            return slot;
        } else if (slot == 36) {
            return 45;
        } else {
            return -1;
        }
    }



    public static void swapSlots(MinecraftClient client,int SlotA, int SlotB)
    {
        int syncId = client.player.currentScreenHandler.syncId;
        ClientPlayerEntity player = client.player;

        client.interactionManager.clickSlot(syncId, convertToScreenHandlerSlot(SlotA), 0, SlotActionType.PICKUP, player);
        client.interactionManager.clickSlot(syncId, convertToScreenHandlerSlot(SlotB) , 0, SlotActionType.PICKUP, player);
        client.interactionManager.clickSlot(syncId, convertToScreenHandlerSlot(SlotA), 0, SlotActionType.PICKUP, player);



    }

    public static void swapSlots(MinecraftClient client, int SlotA)
    {
        int syncId = client.player.currentScreenHandler.syncId;
        ClientPlayerEntity player = client.player;


        client.interactionManager.clickSlot(syncId, convertToScreenHandlerSlot(SlotA), 40, SlotActionType.SWAP, player);



    }

    public static int swapToNewRod(MinecraftClient client, Hand hand, boolean ignoreDurability) {
        ItemStack currentRod = client.player.getStackInHand(hand);
        ClientPlayerEntity player = client.player;
        int currentSelectedSlot =  ((PlayerInventoryAccessor) player.getInventory()).getSelectedSlot();

        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack == currentRod) continue;
            if (!stack.isOf(Items.FISHING_ROD)) continue;
            if (getRodDurability(stack) <= 1 && !ignoreDurability) continue;

            if (hand == Hand.MAIN_HAND)
            {
                swapSlots(client, currentSelectedSlot, i);
            } else
            {
                swapSlots(client ,i);
            }
            return i;
        }
        return -1;
    }

    public static void swapToNewRod(MinecraftClient client, Hand hand, int swappedSlot) {
        ClientPlayerEntity player = client.player;
        int currentSelectedSlot =  ((PlayerInventoryAccessor) player.getInventory()).getSelectedSlot();
        if (swappedSlot < 0) return;

        if (hand == Hand.MAIN_HAND)
        {
            AutoFishClient.LOGGER.info("SWITCH BACK TO OLD ROD");
            swapSlots(client, currentSelectedSlot, swappedSlot);
        } else
        {
            swapSlots(client ,swappedSlot);
        }
        AutoFishClient.lastSwappedSlot = -1;



    }

    public static void queueRodInteraction(MinecraftClient client, PlayerEntity player, Hand playerHand, RodEnum RodEventType) {
        long latency = getLatency(client);
        long delay = 0;
        if (RodEventType == RodEnum.RELEASE)
        {
            delay = ConfigFile.getValue("RodReleaseDelay").getAsInt();

        } else if (RodEventType == RodEnum.CATCH) {
            delay = ConfigFile.getValue("RodCatchDelay").getAsInt();

        }

        ArrayList info = new ArrayList();
        info.add(player);
        info.add(playerHand);
        long taskSize = Ticker.TaskList.size()+1;

        info.add((delay*taskSize));


        info.add(tick());
        info.add(RodEventType);
        Ticker.TaskList.add(info);
    }

    public static Hand getHandWithRod(MinecraftClient client)
    {
        if (client.player.getMainHandStack().getItem().asItem() == Items.FISHING_ROD) {
            return Hand.MAIN_HAND;
        } else if (client.player.getOffHandStack().getItem().asItem() == Items.FISHING_ROD)
        {
            return Hand.OFF_HAND;
        } else {
            return null;
        }
    }
}
