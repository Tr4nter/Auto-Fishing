package tr4nt.autofish.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import tr4nt.autofish.config.ConfigFile;

import java.util.ArrayList;

import static tr4nt.autofish.commands.CommandUtils.flipCommand;
import static tr4nt.autofish.commands.ListCommands.commands;

public class CommandMain {
    private String commandNameBig;
    public ArrayList<String> commandsToFlip = new ArrayList<String>();

    public CommandMain(String commandName) {
        this.commandNameBig = commandName;
        commands.add(commandName);

    }

    public void register(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        {
            fabricClientCommandSourceCommandDispatcher.register(ClientCommandManager.literal(this.commandNameBig).executes(this::run));

        }

    }

    private int run(CommandContext<FabricClientCommandSource> fabricClientCommandSourceCommandContext)
    {
        flipCommand(fabricClientCommandSourceCommandContext, this.commandNameBig);
        commandsToFlip.forEach((String name) ->
        {
            boolean commandStatus = ConfigFile.getValue(name).getAsBoolean();
            if (!commandStatus) return;
            flipCommand(fabricClientCommandSourceCommandContext, name);

        });
        return 1;
    }

}




//    private int run(CommandContext<FabricClientCommandSource> the) {
//
//    }


