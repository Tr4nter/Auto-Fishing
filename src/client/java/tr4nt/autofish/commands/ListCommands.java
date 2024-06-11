package tr4nt.autofish.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import tr4nt.autofish.config.ConfigFile;

import java.util.ArrayList;

import static tr4nt.autofish.Utils.isNumber;


public class ListCommands  {
    private String buff = "";
    public static ArrayList commands = new ArrayList<>();

    public void register(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        {
            fabricClientCommandSourceCommandDispatcher.register(ClientCommandManager.literal("AutoFishCommandList").executes(this::run));

        }

    }

    private int run(CommandContext<FabricClientCommandSource> fabricClientCommandSourceCommandContext) {
        buff = "";


        ListCommands.commands.forEach((i)->
        {
            String key = (String) i;
            String val = String.valueOf(ConfigFile.getValue(key));
            String colourText = isNumber(val) ? "\2473"+val+"ms" : val.equals("true")? "\2472"+val : "\2474"+val;
            buff+="\247f"+key+": "+colourText+"\n";
        });
        fabricClientCommandSourceCommandContext.getSource().sendFeedback(Text.literal(buff));
        return 1;
    }
}
