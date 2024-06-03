package team.lodestar.lodestone.registry.common;

import com.mojang.brigadier.arguments.ArgumentType;
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
/*
    public static final RegistryObject<ArgumentTypeInfo<WorldEventTypeArgument,?>> WORLD_EVENT_TYPE_ARG = COMMAND_ARGUMENT_TYPES.register("world_event_type_arg",
            () -> SingletonArgumentInfo.contextFree(WorldEventTypeArgument::worldEventType));


 */
    /*
    public static final RegistryObject<ArgumentTypeInfo<WorldEventInstanceArgument,?>> WORLD_EVENT_INSTANCE_ARG = COMMAND_ARGUMENT_TYPES.register("world_event_instance_arg",
            () -> SingletonArgumentInfo.contextFree(WorldEventInstanceArgument::worldEventInstance));


     */
    //ArgumentTypeRegistry.registerArgumentType(LodestoneLib.lodestonePath("world_event_type_arg"), WorldEventTypeArgument.class, SingletonArgumentInfo.contextFree(WorldEventTypeArgument::worldEventType));
    //    ArgumentTypeRegistry.registerArgumentType(LodestoneLib.lodestonePath("world_event_instance_arg"), WorldEventInstanceArgument.class, SingletonArgumentInfo.contextFree(WorldEventInstanceArgument::worldEventInstance));

    public static final RegistryObject<ArgumentTypeInfo<WorldEventTypeArgument,?>> WORLD_EVENT_TYPE_ARG = COMMAND_ARGUMENT_TYPES.register("world_event_type_arg",
            () -> registerByClass(WorldEventTypeArgument.class, SingletonArgumentInfo.contextFree(WorldEventTypeArgument::worldEventType)));

    public static final RegistryObject<ArgumentTypeInfo<WorldEventInstanceArgument,?>> WORLD_EVENT_INSTANCE_ARG = COMMAND_ARGUMENT_TYPES.register("world_event_type_arg",
            () -> registerByClass(WorldEventInstanceArgument.class, SingletonArgumentInfo.contextFree(WorldEventInstanceArgument::worldEventInstance)));

    public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>> I registerByClass(Class<A> infoClass, I argumentTypeInfo) {
        ArgumentTypeInfos.BY_CLASS.put(infoClass, argumentTypeInfo);
        return argumentTypeInfo;
    }

    public static void registerArgumentTypes() {
        COMMAND_ARGUMENT_TYPES.register();
    }
}
