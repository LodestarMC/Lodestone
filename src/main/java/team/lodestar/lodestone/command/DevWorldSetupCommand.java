package team.lodestar.lodestone.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import team.lodestar.lodestone.data.LodestoneLangDatagen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

public class DevWorldSetupCommand {
    public DevWorldSetupCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("devsetup")
                .requires(cs -> cs.hasPermission(2))
                .executes((context) -> {
                    CommandSourceStack source = context.getSource();
                    MinecraftServer server = source.getServer();
                    GameRules rules = server.getGameRules();
                    rules.getRule(GameRules.RULE_KEEPINVENTORY).set(true, server);
                    rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, server);
                    rules.getRule(GameRules.RULE_DAYLIGHT).set(false, server);
                    rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
                    rules.getRule(GameRules.RULE_MOBGRIEFING).set(false, server);
                    source.getLevel().setWeatherParameters(6000, 0, false, false);
                    source.getLevel().setDayTime(16000);
                    source.sendSuccess(new TranslatableComponent(LodestoneLangDatagen.getCommand("devsetup")), true);
                    return 1;
                });
    }
}
