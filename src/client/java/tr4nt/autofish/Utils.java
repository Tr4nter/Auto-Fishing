package tr4nt.autofish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import tr4nt.autofish.config.ConfigFile;
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
}
