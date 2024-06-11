package tr4nt.autofish.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import tr4nt.autofish.config.ConfigFile;

import java.util.HashMap;

public class CommandUtils {
    public static void flipCommand(CommandContext<FabricClientCommandSource> context, String commandName)
    {
        boolean reversed = !ConfigFile.getValue(commandName).getAsBoolean();
        HashMap<String, String> map = new HashMap<>();
        map.put(commandName, String.valueOf(reversed));

        ConfigFile.addValue(map, true);
        String colourFull = reversed ? "\2472"+String.valueOf(reversed) : "\2474"+String.valueOf(reversed);
        context.getSource().sendFeedback(Text.literal(commandName + " has been switched to " + colourFull));

    }

}
