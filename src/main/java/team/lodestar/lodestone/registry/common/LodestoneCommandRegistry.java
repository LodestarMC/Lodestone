package team.lodestar.lodestone.registry.common;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.command.*;
import team.lodestar.lodestone.command.arguments.WorldEventInstanceArgument;
import team.lodestar.lodestone.command.arguments.WorldEventTypeArgument;

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
}