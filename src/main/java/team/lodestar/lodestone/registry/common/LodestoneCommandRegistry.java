package team.lodestar.lodestone.registry.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.command.*;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
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