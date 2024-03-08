package tr4nt.autofish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;

import java.time.Instant;
import java.util.Date;

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

}
