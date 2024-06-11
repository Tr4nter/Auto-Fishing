package tr4nt.autofish;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventDispatcher;
import net.minecraft.world.event.listener.SimpleGameEventDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr4nt.autofish.commands.ListCommands;
import tr4nt.autofish.commands.SetNumberCommand;
import tr4nt.autofish.config.ConfigFile;
import tr4nt.autofish.scheduler.Ticker;

import java.util.ArrayList;
import java.util.Map;

import static tr4nt.autofish.Utils.isNumber;
import static tr4nt.autofish.Utils.newOption;

public class AutoFishClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("auto-fish");
	public static final ArrayList commandList = new ArrayList();
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ConfigFile.register("autofishconf");
		commandList.add(newOption("RodReleaseDelay", "100"));
		commandList.add(newOption("RodCatchDelay", "100"));
		commandList.forEach((i)->
		{
			Map<String, String> iz = (Map<String,String>) i;
			ConfigFile.addValue(iz, false);
			String name = (String) iz.keySet().toArray()[0];
			String value = (String) iz.values().toArray()[0];

			if (isNumber(value)) {
				ClientCommandRegistrationCallback.EVENT.register(new SetNumberCommand(name)::register);

			}

		});
		ClientCommandRegistrationCallback.EVENT.register(new ListCommands()::register);
		ClientTickEvents.START_CLIENT_TICK.register(new TickEvent());
		Ticker ticka = new Ticker();
		ClientTickEvents.START_CLIENT_TICK.register(ticka);
	}
}