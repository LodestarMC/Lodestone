package team.lodestar.lodestone.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import team.lodestar.lodestone.command.arguments.WorldEventInstanceArgument;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class GetDataWorldEventCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("get")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("target", WorldEventInstanceArgument.worldEventInstance())
                        .executes((context) -> {
                            CommandSourceStack source = context.getSource();
                            ServerLevel level = source.getLevel();
                            WorldEventInstance eventInstance = WorldEventInstanceArgument.getEventInstance(context, "target");
                            ListActiveWorldEventsCommand.ActiveWorldEventReport report = new ListActiveWorldEventsCommand.ActiveWorldEventReport(level);
                            report.buildInstanceDetailsPage(component -> source.sendSuccess(() -> component, true), eventInstance);
                            return 1;
                        })
                );
    }
}
