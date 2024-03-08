package tr4nt.autofish;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventDispatcher;
import net.minecraft.world.event.listener.SimpleGameEventDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoFishClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("auto-fish");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ClientTickEvents.START_CLIENT_TICK.register(new TickEvent());
	}
}