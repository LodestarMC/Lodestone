package team.lodestar.lodestone.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.command.arguments.WorldEventInstanceArgument;
import team.lodestar.lodestone.command.arguments.WorldEventTypeArgument;
import team.lodestar.lodestone.component.LodestoneComponents;
import team.lodestar.lodestone.network.worldevent.UpdateWorldEventPacket;
import team.lodestar.lodestone.registry.common.LodestonePacketRegistry;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RemoveActiveWorldEventsCommand {

    public RemoveActiveWorldEventsCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("remove")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("all")
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();
                            Level level = source.getLevel();
                            AtomicInteger count = new AtomicInteger();
                            LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(level).ifPresent(c -> {
                                count.set(c.activeWorldEvents.size());
                                c.activeWorldEvents.forEach(instance -> {
                                    instance.end(level);
                                    LodestonePacketRegistry.LODESTONE_CHANNEL.sendToClientsInCurrentServer(new UpdateWorldEventPacket(instance.uuid, instance.synchronizeNBT()));
                                });
                            });
                            if (count.get() > 0) {
                                source.sendSuccess(() -> Component.translatable("command.lodestone.worldevent.remove.all.success", String.valueOf(count.get())), true);
                                return 1;
                            } else {
                                source.sendFailure(Component.translatable("command.lodestone.worldevent.remove.all.fail"));
                                return 0;
                            }
                        })
                )
                .then(Commands.literal("single")
                        .then(Commands.argument("target", WorldEventInstanceArgument.worldEventInstance())
                                .executes(ctx -> {
                                    CommandSourceStack source = ctx.getSource();
                                    WorldEventInstance event = WorldEventInstanceArgument.getEventInstance(ctx, "target");
                                    event.end(source.getLevel());
                                    LodestonePacketRegistry.LODESTONE_CHANNEL.sendToClientsInCurrentServer(new UpdateWorldEventPacket(event.uuid, event.synchronizeNBT()));
                                    source.sendSuccess(() -> Component.translatable("command.lodestone.worldevent.remove.target.success", event.type.id.toString()).withStyle(ChatFormatting.AQUA), true);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("type")
                        .then(Commands.argument("type", WorldEventTypeArgument.worldEventType())
                                .executes(ctx -> {
                                    CommandSourceStack source = ctx.getSource();
                                    WorldEventType type = WorldEventTypeArgument.getEventType(ctx, "type");
                                    AtomicInteger count = new AtomicInteger();
                                    LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(source.getLevel()).ifPresent(c -> {
                                        List<WorldEventInstance> activeWorldEvents = c.activeWorldEvents.stream().filter(instance -> instance.type == type).toList();
                                        count.set(activeWorldEvents.size());
                                        activeWorldEvents.forEach(instance -> {
                                            instance.end(source.getLevel());
                                            LodestonePacketRegistry.LODESTONE_CHANNEL.sendToClientsInCurrentServer(new UpdateWorldEventPacket(instance.uuid, instance.synchronizeNBT()));
                                        });
                                    });
                                    if (count.get() > 0) {
                                        source.sendSuccess(() -> Component.translatable("command.lodestone.worldevent.remove.type.success", String.valueOf(count.get()), type.id.toString()).withStyle(ChatFormatting.AQUA), true);
                                        return 1;
                                    } else {
                                        source.sendFailure(Component.translatable("command.lodestone.worldevent.remove.type.fail", type.id.toString()).withStyle(ChatFormatting.RED));
                                        return 0;
                                    }
                                })
                        )
                );
    }
}
