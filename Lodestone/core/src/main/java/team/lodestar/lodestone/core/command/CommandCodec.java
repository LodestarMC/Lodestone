package team.lodestar.lodestone.core.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.BiFunction;

public interface CommandCodec<T> {
    /**
     * Appends required arguments to a parent node and adds executes() at the end.
     */
    ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, T, Integer> onExecute);

    /**
     * Used to extract values from ctx and produce the instance at the very end :3
     */
    T decode(CommandContext<CommandSourceStack> ctx);
}

