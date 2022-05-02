package com.sammy.ortus.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.sammy.ortus.data.OrtusLangDatagen;
import com.sammy.ortus.helpers.DataHelper;
import com.sammy.ortus.network.screenshake.PositionedScreenshakePacket;
import com.sammy.ortus.network.screenshake.ScreenshakePacket;
import com.sammy.ortus.setup.OrtusPacketRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class ScreenshakeCommand {
    public ScreenshakeCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("screenshake")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("intensity", FloatArgumentType.floatArg(0))
                        .then(Commands.argument("falloffTransformSpeed", FloatArgumentType.floatArg(0))
                                .then(Commands.argument("timeBeforeFastFalloff", IntegerArgumentType.integer(0))
                                        .then(Commands.argument("slowFalloff", FloatArgumentType.floatArg(0))
                                                .then(Commands.argument("fastFalloff", FloatArgumentType.floatArg(0))
                                                        .executes((context) -> {
                                                            CommandSourceStack source = context.getSource();
                                                            if (source.getEntity() instanceof ServerPlayer player) {
                                                                OrtusPacketRegistry.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new ScreenshakePacket(
                                                                        FloatArgumentType.getFloat(context, "intensity"),
                                                                        FloatArgumentType.getFloat(context, "falloffTransformSpeed"),
                                                                        IntegerArgumentType.getInteger(context, "timeBeforeFastFalloff"),
                                                                        FloatArgumentType.getFloat(context, "slowFalloff"),
                                                                        FloatArgumentType.getFloat(context, "fastFalloff")));
                                                            }
                                                            source.sendSuccess(new TranslatableComponent(OrtusLangDatagen.getCommand("screenshake")), true);
                                                            return 1;
                                                        }))))))
                .then(Commands.argument("position", BlockPosArgument.blockPos())
                        .then(Commands.argument("falloffDistance", FloatArgumentType.floatArg(0))
                                .then(Commands.argument("maxDistance", FloatArgumentType.floatArg(0))
                                        .then(Commands.argument("intensity", FloatArgumentType.floatArg(0))
                                                .then(Commands.argument("falloffTransformSpeed", FloatArgumentType.floatArg(0))
                                                        .then(Commands.argument("timeBeforeFastFalloff", IntegerArgumentType.integer(0))
                                                                .then(Commands.argument("slowFalloff", FloatArgumentType.floatArg(0))
                                                                        .then(Commands.argument("fastFalloff", FloatArgumentType.floatArg(0))
                                                                                .executes((context) -> {
                                                                                    CommandSourceStack source = context.getSource();
                                                                                    if (source.getEntity() instanceof ServerPlayer player) {
                                                                                        OrtusPacketRegistry.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PositionedScreenshakePacket(
                                                                                                DataHelper.fromBlockPos(BlockPosArgument.getLoadedBlockPos(context, "position")),
                                                                                                FloatArgumentType.getFloat(context, "falloffDistance"),
                                                                                                FloatArgumentType.getFloat(context, "maxDistance"),
                                                                                                FloatArgumentType.getFloat(context, "intensity"),
                                                                                                FloatArgumentType.getFloat(context, "falloffTransformSpeed"),
                                                                                                IntegerArgumentType.getInteger(context, "timeBeforeFastFalloff"),
                                                                                                FloatArgumentType.getFloat(context, "slowFalloff"),
                                                                                                FloatArgumentType.getFloat(context, "fastFalloff")));
                                                                                    }
                                                                                    source.sendSuccess(new TranslatableComponent(OrtusLangDatagen.getCommand("screenshake")), true);
                                                                                    return 1;
                                                                                })))))))));
    }
}