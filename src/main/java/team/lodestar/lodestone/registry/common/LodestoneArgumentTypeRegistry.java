package team.lodestar.lodestone.registry.common;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.command.arguments.WorldEventInstanceArgument;
import team.lodestar.lodestone.command.arguments.WorldEventTypeArgument;

import java.util.function.Supplier;

public class LodestoneArgumentTypeRegistry {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, LodestoneLib.LODESTONE);

    public static final Supplier<ArgumentTypeInfo<WorldEventTypeArgument,?>> WORLD_EVENT_TYPE_ARG = COMMAND_ARGUMENT_TYPES.register("world_event_type_arg", () -> SingletonArgumentInfo.contextFree(WorldEventTypeArgument::worldEventType));
    public static final Supplier<ArgumentTypeInfo<WorldEventInstanceArgument,?>> WORLD_EVENT_INSTANCE_ARG = COMMAND_ARGUMENT_TYPES.register("world_event_instance_arg", () -> SingletonArgumentInfo.contextFree(WorldEventInstanceArgument::worldEventInstance));



    public static void registerArgumentTypes() {
        ArgumentTypeInfos.registerByClass(WorldEventTypeArgument.class, WORLD_EVENT_TYPE_ARG.get());
        ArgumentTypeInfos.registerByClass(WorldEventInstanceArgument.class, WORLD_EVENT_INSTANCE_ARG.get());
    }
    public static void register(IEventBus modEventBus) {
        COMMAND_ARGUMENT_TYPES.register(modEventBus);
    }
}
