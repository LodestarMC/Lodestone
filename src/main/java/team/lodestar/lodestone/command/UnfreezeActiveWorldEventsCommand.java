package team.lodestar.lodestone.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import team.lodestar.lodestone.command.arguments.WorldEventInstanceArgument;
import team.lodestar.lodestone.command.arguments.WorldEventTypeArgument;
import team.lodestar.lodestone.component.LodestoneComponents;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.List;

public class UnfreezeActiveWorldEventsCommand {

    public UnfreezeActiveWorldEventsCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("unfreeze")
                .requires(cs -> cs.hasPermission(2))
                // Unfreeze all active world events
                .then(Commands.literal("all")
                        .executes(ctx -> {
                            LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(ctx.getSource().getLevel()).ifPresent(c -> {
                                List<WorldEventInstance> activeWorldEvents = c.activeWorldEvents;
                                List<WorldEventInstance> currentlyFrozen = activeWorldEvents.stream().filter(WorldEventInstance::isFrozen).toList();
                                if (currentlyFrozen.isEmpty()) {
                                    ctx.getSource().sendFailure(Component.translatable("command.lodestone.worldevent.unfreeze.all.fail").withStyle(ChatFormatting.RED));
                                } else {
                                    currentlyFrozen.forEach(instance -> instance.frozen = false);
                                    currentlyFrozen.forEach(WorldEventInstance::setDirty);
                                    ctx.getSource().sendSuccess(() -> Component.translatable("command.lodestone.worldevent.unfreeze.all.success", currentlyFrozen.size()).withStyle(ChatFormatting.AQUA), true);
                                }
                            });
                            return 1;
                        })
                )
                // Unfreeze a specific world event instance
                .then(Commands.literal("single")
                        .then(Commands.argument("target", WorldEventInstanceArgument.worldEventInstance())
                                .executes(ctx -> {
                                    WorldEventInstance event = WorldEventInstanceArgument.getEventInstance(ctx, "target");
                                    if (!event.isFrozen()) {
                                        ctx.getSource().sendFailure(Component.translatable("command.lodestone.worldevent.unfreeze.target.fail").withStyle(ChatFormatting.RED));
                                    } else {
                                        event.frozen = false;
                                        event.setDirty();
                                        ctx.getSource().sendSuccess(() -> Component.translatable("command.lodestone.worldevent.unfreeze.target.success", event.type.id.toString()).withStyle(ChatFormatting.AQUA), true);
                                    }
                                    return 1;
                                })
                        )
                )
                // Unfreeze all active world events of a specific type
                .then(Commands.literal("type")
                        .then(Commands.argument("type", WorldEventTypeArgument.worldEventType())
                                .executes(ctx -> {
                                    WorldEventType type = WorldEventTypeArgument.getEventType(ctx, "type");
                                    LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(ctx.getSource().getLevel()).ifPresent(c -> {
                                        List<WorldEventInstance> activeWorldEvents = c.activeWorldEvents.stream().filter(instance -> instance.type == type).toList();
                                        List<WorldEventInstance> currentlyFrozen = activeWorldEvents.stream().filter(WorldEventInstance::isFrozen).toList();
                                        if (currentlyFrozen.isEmpty()) {
                                            ctx.getSource().sendFailure(Component.translatable("command.lodestone.worldevent.unfreeze.type.fail", type.id.toString()).withStyle(ChatFormatting.RED));
                                        } else {
                                            currentlyFrozen.forEach(instance -> instance.frozen = false);
                                            currentlyFrozen.forEach(WorldEventInstance::setDirty);
                                            ctx.getSource().sendSuccess(() -> Component.translatable("command.lodestone.worldevent.unfreeze.type.success", currentlyFrozen.size(), type.id.toString()).withStyle(ChatFormatting.AQUA), true);
                                        }
                                    });
                                    return 1;
                                })
                        )
                );
    }

}