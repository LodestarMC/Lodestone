package team.lodestar.lodestone.registry.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import team.lodestar.lodestone.command.*;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class LodestoneCommandRegistry {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(Commands.literal("lode")
                        .then(DevWorldSetupCommand.register())
                        .then(Commands.literal("worldevent")
                                .then(RemoveActiveWorldEventsCommand.register())
                                .then(ListActiveWorldEventsCommand.register())
                                .then(GetDataWorldEventCommand.register())
                                .then(FreezeActiveWorldEventsCommand.register())
                                .then(UnfreezeActiveWorldEventsCommand.register())
                        )
                //.then(ScreenshakeCommand.register())
        );
        dispatcher.register(Commands.literal(LODESTONE)
                .redirect(cmd));
    }

    /*TODO
    public static <T extends ArgumentType<?>> void registerArgumentType(ResourceLocation key, Class<T> argumentClass, ArgumentSerializer<T> serializer) {
        ArgumentTypes.register(key.toString(), argumentClass, serializer);
    }

     */
}