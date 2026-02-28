package team.lodestar.lodestone.core.command;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.function.BiFunction;

public final class CommandCodecs {

    public static <A, V> CommandField<A, V> arg(String name, ArgumentType<A> type, BiFunction<CommandContext<CommandSourceStack>, String, V> getter) {
        return new SimpleCommandField<>(name, type, getter);
    }

    public static CommandField<?, Vec3> vec3(String name) {
        return new SimpleCommandField<>(name, Vec3Argument.vec3(false), Vec3Argument::getVec3);
    }

    public static CommandField<?, Vec2> vec2(String name) {
        return new SimpleCommandField<>(name, Vec2Argument.vec2(false), Vec2Argument::getVec2);
    }

    public static CommandField<?, Float> floatValue(String name) {
        return new SimpleCommandField<>(name, FloatArgumentType.floatArg(), FloatArgumentType::getFloat);
    }

    public static CommandField<?, Float> floatValue(String name, float min) {
        return new SimpleCommandField<>(name, FloatArgumentType.floatArg(min), FloatArgumentType::getFloat);
    }

    public static CommandField<?, Float> floatValue(String name, float min, float max) {
        return new SimpleCommandField<>(name, FloatArgumentType.floatArg(min, max), FloatArgumentType::getFloat);
    }

    public static CommandField<?, Double> doubleValue(String name) {
        return new SimpleCommandField<>(name, DoubleArgumentType.doubleArg(), DoubleArgumentType::getDouble);
    }

    public static CommandField<?, Double> doubleValue(String name, double min) {
        return new SimpleCommandField<>(name, DoubleArgumentType.doubleArg(min), DoubleArgumentType::getDouble);
    }

    public static CommandField<?, Double> doubleValue(String name, double min, double max) {
        return new SimpleCommandField<>(name, DoubleArgumentType.doubleArg(min, max), DoubleArgumentType::getDouble);
    }

    public static CommandField<?, Integer> intValue(String name) {
        return new SimpleCommandField<>(name, IntegerArgumentType.integer(), IntegerArgumentType::getInteger);
    }

    public static CommandField<?, Integer> intValue(String name, int min) {
        return new SimpleCommandField<>(name, IntegerArgumentType.integer(min), IntegerArgumentType::getInteger);
    }

    public static CommandField<?, Integer> intValue(String name, int min, int max) {
        return new SimpleCommandField<>(name, IntegerArgumentType.integer(min, max), IntegerArgumentType::getInteger);
    }

    public static CommandField<?, Long> longValue(String name) {
        return new SimpleCommandField<>(name, LongArgumentType.longArg(), LongArgumentType::getLong);
    }

    public static CommandField<?, Long> longValue(String name, long min) {
        return new SimpleCommandField<>(name, LongArgumentType.longArg(min), LongArgumentType::getLong);
    }

    public static CommandField<?, Long> longValue(String name, long min, long max) {
        return new SimpleCommandField<>(name, LongArgumentType.longArg(min, max), LongArgumentType::getLong);
    }

    public static CommandField<?, String> stringValue(String name) {
        return new SimpleCommandField<>(name, StringArgumentType.string(), StringArgumentType::getString);
    }

    public static CommandField<?, Boolean> booleanValue(String name) {
        return new SimpleCommandField<>(name, BoolArgumentType.bool(), BoolArgumentType::getBool);
    }

    public static CommandField<?, UUID> uuid(String name) {
        return new SimpleCommandField<>(name, UuidArgument.uuid(), UuidArgument::getUuid);
    }

    public static CommandField<?, ResourceLocation> resourceLocation(String name) {
        return new SimpleCommandField<>(name, ResourceLocationArgument.id(), ResourceLocationArgument::getId);
    }
}
