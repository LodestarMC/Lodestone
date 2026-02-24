package team.lodestar.lodestone.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.common.commands.arguments.WorldEventTypeArgument;
import team.lodestar.lodestone.handlers.worldevent.WorldEventHandler;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypes;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class CreateWorldEventsCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        var create = Commands.literal("create").requires(cs -> cs.hasPermission(2));

        for (WorldEventType type : LodestoneWorldEventTypes.getEventTypes()) {
            var typeArg = Commands.argument("type", WorldEventTypeArgument.worldEventType());

            if (type.commandCodec != null) {
                type.commandCodec.appendTo(typeArg, (source, instance) -> {
                    Level level = source.getLevel();
                    source.sendSuccess(() -> Component.literal("Created world event of type " + instance.type.id.toString()).withStyle(ChatFormatting.DARK_GREEN), true);
                    WorldEventHandler.addWorldEvent(level, instance);
                    return 1;
                });
            } else {
                typeArg.executes(ctx -> {
                    CommandSourceStack source = ctx.getSource();
                    source.sendFailure(Component.literal("World Event Type " + type.id.toString() + " has no command codec and cannot be created via command.").withStyle(ChatFormatting.RED));
                    return 0;
                });
            }

            create.then(typeArg);
        }

        return create;
    }
}
