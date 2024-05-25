package team.lodestar.lodestone.registry.common;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.command.arguments.WorldEventInstanceArgument;
import team.lodestar.lodestone.command.arguments.WorldEventTypeArgument;

public class LodestoneArgumentTypeRegistry {
    public static final LazyRegistrar<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = LazyRegistrar.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, LodestoneLib.LODESTONE);

    public static final RegistryObject<ArgumentTypeInfo<WorldEventTypeArgument,?>> WORLD_EVENT_TYPE_ARG = COMMAND_ARGUMENT_TYPES.register("world_event_type_arg", () -> SingletonArgumentInfo.contextFree(WorldEventTypeArgument::worldEventType));
    public static final RegistryObject<ArgumentTypeInfo<WorldEventInstanceArgument,?>> WORLD_EVENT_INSTANCE_ARG = COMMAND_ARGUMENT_TYPES.register("world_event_instance_arg", () -> SingletonArgumentInfo.contextFree(WorldEventInstanceArgument::worldEventInstance));

    public static void registerArgumentTypes() {
        ArgumentTypeRegistry.registerArgumentType(LodestoneLib.lodestonePath("world_event_type_arg"), WorldEventTypeArgument.class, WORLD_EVENT_TYPE_ARG.get());
        ArgumentTypeRegistry.registerArgumentType(LodestoneLib.lodestonePath("world_event_instance_arg"), WorldEventInstanceArgument.class, WORLD_EVENT_INSTANCE_ARG.get());
    }
}
