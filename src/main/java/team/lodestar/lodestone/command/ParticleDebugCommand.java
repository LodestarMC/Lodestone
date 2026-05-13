package team.lodestar.lodestone.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import team.lodestar.lodestone.modules.rendering.handlers.ParticleHandler;
import team.lodestar.lodestone.modules.rendering.particle.pool.ParticlePool;
import team.lodestar.lodestone.modules.rendering.particle.pool.ParticlePoolGroup;
import team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ParticleDebugCommand {

    private static final int whitespaceWidth = 20;
    private static final int ITEMS_PER_PAGE = 10;

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("particles")
                .then(Commands.literal("editor")
                        .executes(ctx -> {
                            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new ParticleEditorScreen()));
                            return 1;
                        })
                )
                .then(Commands.literal("list")
                        .executes(ctx -> executeList(ctx.getSource(), 0))
                        .then(Commands.argument("page", IntegerArgumentType.integer(0))
                                .executes(ctx -> executeList(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "page")))
                        )
                )
                .then(Commands.literal("group")
                        .then(Commands.argument("hash", StringArgumentType.word())
                                .executes(ctx -> {
                                    String hash = StringArgumentType.getString(ctx, "hash");
                                    ParticlePoolGroup group = findGroup(hash);
                                    if (group != null) {
                                        new ParticleReport().buildGroupDetailsPage(component -> ctx.getSource().sendSuccess(() -> component, false), group);
                                    } else {
                                        ctx.getSource().sendFailure(Component.literal("Particle group not found."));
                                    }
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("clear")
                        .then(Commands.literal("all")
                                .executes(ctx -> {
                                    ParticleHandler.allPoolGroups().forEach(g -> g.pools().forEach(ParticlePool::clear));
                                    ctx.getSource().sendSuccess(() -> Component.literal("Cleared all particle pools.").withStyle(ChatFormatting.GREEN), false);
                                    return 1;
                                })
                        )
                        .then(Commands.argument("hash", StringArgumentType.word())
                                .executes(ctx -> {
                                    String hash = StringArgumentType.getString(ctx, "hash");
                                    ParticlePoolGroup group = findGroup(hash);
                                    if (group != null) {
                                        group.pools().forEach(ParticlePool::clear);
                                        ctx.getSource().sendSuccess(() -> Component.literal("Cleared particle group.").withStyle(ChatFormatting.GREEN), false);
                                    }
                                    return 1;
                                })
                        )
                );
    }

    private static int executeList(CommandSourceStack source, int page) {
        ParticleReport report = new ParticleReport();
        report.buildInteractiveMessage(component -> source.sendSuccess(() -> component, false), page);
        return 1;
    }

    private static ParticlePoolGroup findGroup(String hash) {
        for (ParticlePoolGroup group : ParticleHandler.allPoolGroups()) {
            if (String.valueOf(group.key().hashCode()).equals(hash)) {
                return group;
            }
        }
        return null;
    }

    protected static class ParticleReport {
        private final List<ParticlePoolGroup> groups;
        private final int totalParticles;

        protected ParticleReport() {
            this.groups = new ArrayList<>(ParticleHandler.allPoolGroups());
            this.totalParticles = groups.stream()
                    .flatMap(g -> g.pools().stream())
                    .mapToInt(ParticlePool::count)
                    .sum();
        }

        private void buildInteractiveMessage(Consumer<Component> consumer, int page) {
            buildHeader(consumer);
            buildInteractiveIndexPage(consumer, page);
            buildInteractiveFooter(consumer, page);
        }

        private void buildInteractiveIndexPage(Consumer<Component> consumer, int page) {
            int startIndex = page * ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, groups.size());

            if (groups.isEmpty()) {
                consumer.accept(Component.literal("No active particle pools.").withStyle(ChatFormatting.GRAY));
                return;
            }

            for (int i = startIndex; i < endIndex; i++) {
                ParticlePoolGroup group = groups.get(i);
                String hash = String.valueOf(group.key().hashCode());

                int groupParticles = group.pools().stream().mapToInt(ParticlePool::count).sum();
                int poolCount = group.pools().size();

                MutableComponent groupComponent = Component.literal(String.format("%02d. | Group %s | %d Particles ", i + 1, hash, groupParticles))
                        .withStyle(Style.EMPTY.withColor(i % 2 == 1 ? ChatFormatting.GRAY : ChatFormatting.WHITE)
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Mask: " + group.key().componentMask().toString() + "\nPools: " + poolCount))));

                MutableComponent detailsButton = Component.literal("[>]").withStyle(Style.EMPTY
                        .withColor(ChatFormatting.GREEN)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestonec particles group " + hash))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to inspect pools"))));

                MutableComponent clearButton = Component.literal("[X]").withStyle(Style.EMPTY
                        .withColor(ChatFormatting.RED)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestonec particles clear " + hash))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to clear this group"))));

                MutableComponent space = Component.literal("  ");

                consumer.accept(groupComponent.append(space).append(detailsButton).append(space).append(clearButton));
            }
        }

        protected void buildGroupDetailsPage(Consumer<Component> consumer, ParticlePoolGroup group) {
            String hash = String.valueOf(group.key().hashCode());
            int groupParticles = group.pools().stream().mapToInt(ParticlePool::count).sum();

            MutableComponent header = Component.literal("Particle Group Details:").withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE).withBold(true));
            MutableComponent hashComp = Component.literal("Hash: ").withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(hash).withStyle(ChatFormatting.GREEN));
            MutableComponent maskComp = Component.literal("Mask: ").withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(group.key().componentMask().toString()).withStyle(ChatFormatting.WHITE));
            MutableComponent totalComp = Component.literal("Total Particles: ").withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(String.valueOf(groupParticles)).withStyle(ChatFormatting.AQUA));

            buildWhitespace(consumer);
            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)));
            consumer.accept(header);
            consumer.accept(hashComp);
            consumer.accept(maskComp);
            consumer.accept(totalComp);
            consumer.accept(getDashedSpaceLine().withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)));

            consumer.accept(Component.literal("Pools:").withStyle(ChatFormatting.GOLD));

            List<ParticlePool> pools = group.pools();
            for (int i = 0; i < pools.size(); i++) {
                ParticlePool pool = pools.get(i);

                List<String> activeVisuals = new ArrayList<>();
                pool.getActiveVisualIds().forEach(id -> activeVisuals.add(String.valueOf(id)));
                String visualsStr = activeVisuals.isEmpty() ? "None" : String.join(", ", activeVisuals);

                MutableComponent poolComp = Component.literal(String.format("  Pool #%d | Count: %d/%d | Visual IDs: [%s]",
                                i + 1, pool.count(), pool.capacity(), visualsStr))
                        .withStyle(pool.count() == pool.capacity() ? ChatFormatting.RED : ChatFormatting.WHITE);

                consumer.accept(poolComp);
            }

            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)));

            MutableComponent backButton = Component.literal("[Back]")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestonec particles list 0"))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Back to Group List"))));

            MutableComponent clearButton = Component.literal("[Clear Group]")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.RED)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestonec particles clear " + hash))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Clear all particles in this group"))));

            consumer.accept(backButton.append("  ").append(clearButton));
        }

        private void buildHeader(Consumer<Component> consumer) {
            buildWhitespace(consumer);
            consumer.accept(Component.translatable("There are %s active Particle Groups", groups.size()).withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)));
            consumer.accept(Component.translatable("Total Particles: %s", totalParticles).withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)));
            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)));
        }

        private void buildInteractiveFooter(Consumer<Component> consumer, int page) {
            consumer.accept(getDashedLine().withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)));

            MutableComponent previousPage = Component.literal("[<]")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestonec particles list " + (page - 1)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Previous Page"))));

            MutableComponent nextPage = Component.literal("[>]")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestonec particles list " + (page + 1)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Next Page"))));

            MutableComponent clearAll = Component.literal("[Clear All]")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.RED)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestonec particles clear all"))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Clear all particles"))));

            MutableComponent reload = Component.literal("[Reload]")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lodestonec particles list " + page))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Refresh List"))));

            MutableComponent pageNumber = Component.literal(String.valueOf(page)).withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD));
            MutableComponent navigation = previousPage.append("  ").append(pageNumber).append("  ").append(nextPage);

            consumer.accept(navigation.append("  ").append(clearAll).append("  ").append(reload));
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
