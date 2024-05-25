package team.lodestar.lodestone.registry.common;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import team.lodestar.lodestone.command.*;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

public class LodestoneCommandRegistry {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<CommandSourceStack> command = dispatcher.register(Commands.literal("lode")
                    .then(DevWorldSetupCommand.register())
                    .then(Commands.literal("worldevent")
                            .then(RemoveActiveWorldEventsCommand.register())
                            .then(ListActiveWorldEventsCommand.register())
                            .then(GetDataWorldEventCommand.register())
                            .then(FreezeActiveWorldEventsCommand.register())
                            .then(UnfreezeActiveWorldEventsCommand.register())
                    )
            );
            dispatcher.register(Commands.literal(LODESTONE).redirect(command));
        });
    }

    public static void registerArgumentTypes() {
    }

    /*TODO
    public static <T extends ArgumentType<?>> void registerArgumentType(ResourceLocation key, Class<T> argumentClass, ArgumentSerializer<T> serializer) {
        ArgumentTypes.register(key.toString(), argumentClass, serializer);
    }

     */
}