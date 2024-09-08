package team.lodestar.lodestone.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.concurrent.CompletableFuture;

public class WorldEventTypeArgument implements ArgumentType<WorldEventType> {
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_WORLD_EVENT_TYPE = new DynamicCommandExceptionType((object) ->
            Component.translatable("argument.lodestone.id.invalid", object.toString()));

    protected WorldEventTypeArgument() {
    }

    public static WorldEventTypeArgument worldEventType() {
        return new WorldEventTypeArgument();
    }

    public static WorldEventType getEventType(CommandContext<?> context, String name) {
        return context.getArgument(name, WorldEventType.class);
    }

    @Override
    public WorldEventType parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation resourceLocation = ResourceLocation.read(reader);
        WorldEventType worldEventType = LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(resourceLocation);
        if (worldEventType != null) {
            return worldEventType;
        } else {
            throw ERROR_UNKNOWN_WORLD_EVENT_TYPE.create(resourceLocation);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (WorldEventType type : LodestoneWorldEventTypeRegistry.EVENT_TYPES.values()) {
            if (type != null) {
                builder.suggest(type.id.toString());
            }
        }
        return builder.buildFuture();
    }
}
