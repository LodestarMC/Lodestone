package team.lodestar.lodestone.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import io.github.fabricators_of_create.porting_lib.util.ServerLifecycleHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.component.LodestoneComponents;
import team.lodestar.lodestone.mixin.ClientSuggestionProviderMixin;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraft.commands.arguments.UuidArgument.ERROR_INVALID_UUID;

public class WorldEventInstanceArgument implements ArgumentType<WorldEventInstance> {
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_WORLD_EVENT_INSTANCE = new DynamicCommandExceptionType((object) ->
            Component.translatable("argument.lodestone.id.invalid", object.toString()));
    private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");

    protected WorldEventInstanceArgument() {
    }
    public static WorldEventInstanceArgument worldEventInstance() {
        return new WorldEventInstanceArgument();
    }

    public static WorldEventInstance getEventInstance(CommandContext<?> context, String name) {
        return context.getArgument(name, WorldEventInstance.class);
    }

    @Override
    public WorldEventInstance parse(StringReader reader) throws CommandSyntaxException {
        String s = reader.readString();
        Matcher matcher = ALLOWED_CHARACTERS.matcher(s);
        AtomicReference<WorldEventInstance> eventInstance = new AtomicReference<>();
        if (matcher.find()) {
            String s1 = matcher.group(1);

            try {
                UUID uuid = UUID.fromString(s1);
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                if (server == null) return null;
                Set<ResourceKey<Level>> levels = server.registryAccess().registry(Registries.DIMENSION).get().registryKeySet();
                levels.forEach(levelResourceKey -> {
                    if (levelResourceKey == null) return;
                    Level level = server.getLevel(levelResourceKey);
                    if (level == null) return;
                    LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(level).ifPresent(capability -> capability.activeWorldEvents.forEach(worldEventInstance -> {
                        if (worldEventInstance.uuid.equals(uuid)) {
                            eventInstance.set(worldEventInstance);
                        }
                    }));
                });
                if (eventInstance.get() == null) {
                    throw ERROR_UNKNOWN_WORLD_EVENT_INSTANCE.create(uuid);
                } else {
                    return eventInstance.get();
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                throw ERROR_INVALID_UUID.create();
            }
        }
        return eventInstance.get();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        S s = context.getSource();
        if (s instanceof SharedSuggestionProvider sharedsuggestionprovider) {
            sharedsuggestionprovider.levels().forEach(levelResourceKey -> {
                if (levelResourceKey == null) return;

                final Minecraft client = ((ClientSuggestionProviderMixin) sharedsuggestionprovider).getMinecraft();
                if (client == null) return;
                LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(client).ifPresent(capability -> {
                    capability.activeWorldEvents.forEach(worldEventInstance -> {
                        builder.suggest(worldEventInstance.uuid.toString());
                    });
                });
            });
        }

        return builder.buildFuture();
    }

}