package team.lodestar.lodestone.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import team.lodestar.lodestone.command.arguments.WorldEventTypeArgument;
import team.lodestar.lodestone.component.LodestoneComponents;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.List;
import java.util.function.Consumer;

public class ListActiveWorldEventsCommand {

    private static final int whitespaceWidth = 20;
    private static final int ITEMS_PER_PAGE = 10;
    private static final int currentPage = 0;
    public ListActiveWorldEventsCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("list")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("all")
                        .executes((context) -> {
                            CommandSourceStack source = context.getSource();
                            ServerLevel level = source.getLevel();
                            ActiveWorldEventReport report = new ActiveWorldEventReport(level);
                            report.buildInteractiveMessage(component -> source.sendSuccess(() -> component, true), 0);
                            return 1;
                        })
                )
                .then(Commands.literal("type")
                        .then(Commands.argument("worldEventType", WorldEventTypeArgument.worldEventType())
                                .executes(ctx -> {
                                    WorldEventType worldEventType = WorldEventTypeArgument.getEventType(ctx, "worldEventType");
                                    CommandSourceStack source = ctx.getSource();
                                    ServerLevel level = source.getLevel();
                                    ActiveWorldEventReport report = new ActiveWorldEventReport(level, worldEventType);
                                    report.buildInteractiveMessage(component -> source.sendSuccess(() -> component, true), 0);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("page")
                        .then(Commands.argument("page", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    int page = IntegerArgumentType.getInteger(ctx, "page");
                                    CommandSourceStack source = ctx.getSource();
                                    ServerLevel level = source.getLevel();
                                    ActiveWorldEventReport report = new ActiveWorldEventReport(level);
                                    report.buildInteractiveMessage(component -> source.sendSuccess(() -> component, true), page);
                                    return 1;
                                })
                        )
                );
    }

    protected static class ActiveWorldEventReport {
        private int instanceCount;
        private int frozenCount;
        private List<WorldEventInstance> activeWorldEvents;

        protected ActiveWorldEventReport(ServerLevel level) {
            LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(level).ifPresent(c -> {
                instanceCount = c.activeWorldEvents.size();
                frozenCount = (int) c.activeWorldEvents.stream().filter(WorldEventInstance::isFrozen).count();
                activeWorldEvents = c.activeWorldEvents;
            });
        }

        protected ActiveWorldEventReport(ServerLevel level, WorldEventType worldEventType) {
            LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(level).ifPresent(c -> {
                instanceCount = (int) c.activeWorldEvents.stream().filter(worldEventInstance -> worldEventInstance.type.equals(worldEventType)).count();
                frozenCount = (int) c.activeWorldEvents.stream().filter(worldEventInstance -> worldEventInstance.type.equals(worldEventType) && worldEventInstance.isFrozen()).count();
                activeWorldEvents = c.activeWorldEvents;
            });
        }

        private void buildInteractiveMessage(Consumer<Component> consumer, int page) {
            buildHeader(consumer);
            buildInteractiveIndexPage(consumer, page);
            buildInteractiveFooter(consumer, page);
        }

        private void buildInteractiveIndexPage(Consumer<Component> consumer, int page) {
            int startIndex = page * ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, activeWorldEvents.size());

            for (int i = startIndex; i < endIndex; i++) {
                WorldEventInstance event = activeWorldEvents.get(i);
                MutableComponent eventComponent = Component.literal(String.format("%02d. | %s | ", i + 1, event.type.id.toString()))
                        .withStyle(Style.EMPTY.withColor(i % 2 == 1 ? ChatFormatting.GRAY : ChatFormatting.WHITE)
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(event.toString()))))
                        .append(Component.literal("[>]").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent get " + event.uuid.toString()))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to see details")))));
                consumer.accept(eventComponent);
            }
        }

        protected void buildInstanceDetailsPage(Consumer<Component> consumer, WorldEventInstance worldEventInstance) {
            buildWhitespace(consumer);
            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
            CompoundTag tag = worldEventInstance.serializeNBT(new CompoundTag());
            consumer.accept(Component.literal("World Event Details:").withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withBold(true)));
            consumer.accept(Component.literal("UUID: ").append('"'+worldEventInstance.uuid.toString()+'"').withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Copy UUID to clipboard")))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, worldEventInstance.uuid.toString()))));
            consumer.accept(Component.literal("Type: ").append('"'+worldEventInstance.type.id.toString()+'"').withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(worldEventInstance.type.toString())))));

            consumer.accept(getDashedSpaceLine().withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)));

            consumer.accept(Component.literal("Data:").withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
            tag.tags.forEach((key, value) -> {
                if (key.equals("uuid") || key.equals("type")) return;
                MutableComponent keyComponent = Component.literal(key + ": ").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
                MutableComponent valueComponent = Component.literal(value.toString()).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE));
                consumer.accept(keyComponent.append(valueComponent));
            });
            // Back button
            MutableComponent backButton = Component.literal("[Back]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent list page " + currentPage))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Back to World Event List"))));
            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
            consumer.accept(backButton);
        }

        private void buildHeader(Consumer<Component> consumer) {
            buildWhitespace(consumer);
            consumer.accept(Component.translatable("There are %s active World Events", instanceCount).withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
            consumer.accept(Component.translatable("%s events are frozen", frozenCount).withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)));
            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
        }

        private void buildInteractiveFooter(Consumer<Component> consumer, int page) {
            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));

            MutableComponent previousPage = Component.literal("[<]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent list page " + (page - 1)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Previous Page"))));

            MutableComponent nextPage = Component.literal("[>]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent list page " + (page + 1)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Next Page"))));

            consumer.accept(previousPage.append("  " + page + "  ").append(nextPage));
        }

        private void buildWhitespace(Consumer<Component> consumer) {
            for (int i = 0; i < whitespaceWidth; i++) consumer.accept(Component.literal(" "));
        }

        private static MutableComponent getDashedLine() {
            return Component.literal("-".repeat(40));
        }

        private static MutableComponent getDashedSpaceLine() {
            return Component.literal("- ".repeat(25));
        }
    }
}
