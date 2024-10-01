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
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(event.toString()))));

                MutableComponent detailsButton = Component.literal("[>]").withStyle(Style.EMPTY
                        .withColor(ChatFormatting.GREEN)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent get " + event.uuid.toString()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to see details"))));

                MutableComponent freezeButton = Component.literal("[F]").withStyle(Style.EMPTY
                        .withColor(ChatFormatting.AQUA)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent freeze single " + event.uuid.toString()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to freeze"))));

                MutableComponent unfreezeButton = Component.literal("[U]").withStyle(Style.EMPTY
                        .withColor(ChatFormatting.DARK_AQUA)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent unfreeze single " + event.uuid.toString()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to unfreeze"))));

                MutableComponent removeButton = Component.literal("[X]").withStyle(Style.EMPTY
                        .withColor(ChatFormatting.RED)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent remove single " + event.uuid.toString()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to remove"))));

                MutableComponent space = Component.literal("  ");

                consumer.accept(eventComponent.append(space).append(detailsButton).append(space).append(event.isFrozen() ? unfreezeButton : freezeButton).append(space).append(removeButton));
            }
        }

        protected void buildInstanceDetailsPage(Consumer<Component> consumer, WorldEventInstance worldEventInstance) {
            MutableComponent worldEventDetailsHeader = Component.literal("World Event Details:").withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withBold(true));

            MutableComponent worldEventUUID = Component.literal("UUID: ").withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                    .append(Component.literal(worldEventInstance.uuid.toString()).withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, worldEventInstance.uuid.toString()))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy")))
                    ));
            MutableComponent worldEventType = Component.literal("Type: ").withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                    .append(Component.literal(worldEventInstance.type.id.toString()).withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent list type " + worldEventInstance.type.id.toString()))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to filter by type")))
                    ));

            MutableComponent frozenStatus = Component.literal("Frozen: ").withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                    .append(Component.literal(worldEventInstance.isFrozen() ? "Yes" : "No").withStyle(Style.EMPTY
                            .withColor(worldEventInstance.isFrozen() ? ChatFormatting.DARK_AQUA : ChatFormatting.GREEN)
                    ));
            MutableComponent worldEventData = Component.literal("Data:").withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD));

            buildWhitespace(consumer);
            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
            consumer.accept(worldEventDetailsHeader);
            consumer.accept(worldEventUUID);
            consumer.accept(worldEventType);
            consumer.accept(frozenStatus);
            consumer.accept(getDashedSpaceLine().withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)));
            consumer.accept(worldEventData);
            buildDetailsBody(consumer, worldEventInstance.serializeNBT());
            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
            buildInteractiveFooter(consumer, worldEventInstance);
        }

        private void buildDetailsBody(Consumer<Component> consumer, CompoundTag tag) {
            tag.tags.forEach((key, value) -> {
                if (key.equals("uuid") || key.equals("type") || key.equals("discarded") || key.equals("frozen")) return;
                MutableComponent keyComponent = Component.literal(key + ": ").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
                MutableComponent valueComponent = Component.literal(value.toString()).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE));
                consumer.accept(keyComponent.append(valueComponent));
            });
        }

        private void buildInteractiveFooter(Consumer<Component> consumer, WorldEventInstance instance) {
            MutableComponent backButton = Component.literal("[Back]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent list page " + currentPage))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Back to World Event List"))));

            MutableComponent freezeButton = Component.literal("[Freeze]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.DARK_BLUE)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent freeze single " + instance.uuid.toString()))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Freeze this World Event"))));

            MutableComponent unfreezeButton = Component.literal("[Unfreeze]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.DARK_AQUA)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent unfreeze single " + instance.uuid.toString()))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Unfreeze this World Event"))));

            MutableComponent removeButton = Component.literal("[Remove]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.RED)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent remove single " + instance.uuid.toString()))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Remove this World Event"))));

            MutableComponent reloadButton = Component.literal("[Reload]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.YELLOW)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent get " + instance.uuid.toString()))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Reload this World Event"))));

            consumer.accept(backButton.append("  ").append(instance.isFrozen() ? unfreezeButton : freezeButton).append("  ").append(removeButton).append("  ").append(reloadButton));
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

            MutableComponent removeAll = Component.literal("[Remove All]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.RED)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent remove all"))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Remove all active World Events"))));

            MutableComponent freezeAll = Component.literal("[Freeze All]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.DARK_BLUE)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent freeze all"))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Freeze all active World Events"))));

            MutableComponent unfreezeAll = Component.literal("[Unfreeze All]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.DARK_AQUA)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent unfreeze all"))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Unfreeze all active World Events"))));

            MutableComponent reload = Component.literal("[Reload]")
                    .withStyle(Style.EMPTY
                            .withColor(ChatFormatting.YELLOW)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestone worldevent list page " + page))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Reload active World Events"))));

            MutableComponent pageNumber = Component.literal(String.valueOf(page)).withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD));
            MutableComponent navigation = previousPage.append("  ").append(pageNumber).append("  ").append(nextPage);
            MutableComponent freezeUnfreeze = frozenCount == instanceCount ? unfreezeAll : freezeAll;
            consumer.accept(navigation.append("  ").append(freezeUnfreeze).append("  ").append(removeAll).append("  ").append(reload));
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
