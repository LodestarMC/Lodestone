package team.lodestar.lodestone.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ScreenshakeCommand {
    public ScreenshakeCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("screenshake")
                .requires(cs -> cs.hasPermission(2));
//                .then(Commands.argument("intensity", FloatArgumentType.floatArg(0))
//                        .then(Commands.argument("falloffTransformSpeed", FloatArgumentType.floatArg(0))
//                                .then(Commands.argument("timeBeforeFastFalloff", IntegerArgumentType.integer(0))
//                                        .then(Commands.argument("slowFalloff", FloatArgumentType.floatArg(0))
//                                                .then(Commands.argument("fastFalloff", FloatArgumentType.floatArg(0))
//                                                        .executes((context) -> {
//                                                            CommandSourceStack source = context.getSource();
//                                                            if (source.getEntity() instanceof ServerPlayer player) {
//                                                                LodestonePacketRegistry.ORTUS_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ScreenshakePacket(
//                                                                        FloatArgumentType.getFloat(context, "intensity"),
//                                                                        FloatArgumentType.getFloat(context, "falloffTransformSpeed"),
//                                                                        IntegerArgumentType.getInteger(context, "timeBeforeFastFalloff"),
//                                                                        FloatArgumentType.getFloat(context, "slowFalloff"),
//                                                                        FloatArgumentType.getFloat(context, "fastFalloff")));
//                                                            }
//                                                            source.sendSuccess(new TranslatableComponent(LodestoneLangDatagen.getCommand("screenshake")), true);
//                                                            return 1;
//                                                        }))))))
//                .then(Commands.argument("position", BlockPosArgument.blockPos())
//                        .then(Commands.argument("falloffDistance", FloatArgumentType.floatArg(0))
//                                .then(Commands.argument("maxDistance", FloatArgumentType.floatArg(0))
//                                        .then(Commands.argument("intensity", FloatArgumentType.floatArg(0))
//                                                .then(Commands.argument("falloffTransformSpeed", FloatArgumentType.floatArg(0))
//                                                        .then(Commands.argument("timeBeforeFastFalloff", IntegerArgumentType.integer(0))
//                                                                .then(Commands.argument("slowFalloff", FloatArgumentType.floatArg(0))
//                                                                        .then(Commands.argument("fastFalloff", FloatArgumentType.floatArg(0))
//                                                                                .executes((context) -> {
//                                                                                    CommandSourceStack source = context.getSource();
//                                                                                    if (source.getEntity() instanceof ServerPlayer player) {
//                                                                                        LodestonePacketRegistry.ORTUS_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PositionedScreenshakePacket(
//                                                                                                BlockHelper.fromBlockPos(BlockPosArgument.getLoadedBlockPos(context, "position")),
//                                                                                                FloatArgumentType.getFloat(context, "falloffDistance"),
//                                                                                                FloatArgumentType.getFloat(context, "maxDistance"),
//                                                                                                FloatArgumentType.getFloat(context, "intensity"),
//                                                                                                FloatArgumentType.getFloat(context, "falloffTransformSpeed"),
//                                                                                                IntegerArgumentType.getInteger(context, "timeBeforeFastFalloff"),
//                                                                                                FloatArgumentType.getFloat(context, "slowFalloff"),
//                                                                                                FloatArgumentType.getFloat(context, "fastFalloff")));
//                                                                                    }
//                                                                                    source.sendSuccess(new TranslatableComponent(LodestoneLangDatagen.getCommand("screenshake")), true);
//                                                                                    return 1;
//                                                                                })))))))));

    }
}