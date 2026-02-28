package team.lodestar.lodestone.core.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.function.BiFunction;

public class SimpleCommandField<A, V> implements CommandField<A, V> {
    private final String name;
    private final ArgumentType<A> type;
    private final BiFunction<CommandContext<CommandSourceStack>, String, V> getter;

    public SimpleCommandField(String name, ArgumentType<A> type, BiFunction<CommandContext<CommandSourceStack>, String, V> getter) {
        this.name = name;
        this.type = type;
        this.getter = getter;
    }

    @Override public String getName() { return name; }

    @Override
    public RequiredArgumentBuilder<CommandSourceStack, A> getArgument() {
        return Commands.argument(name, type);
    }

    @Override
    public V getValue(CommandContext<CommandSourceStack> ctx) {
        return getter.apply(ctx, name);
    }
}
