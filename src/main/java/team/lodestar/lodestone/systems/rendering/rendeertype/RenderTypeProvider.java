package team.lodestar.lodestone.systems.rendering.rendeertype;

import team.lodestar.lodestone.*;
import team.lodestar.lodestone.systems.rendering.*;
import team.lodestar.lodestone.systems.rendering.*;

import java.util.concurrent.*;
import java.util.function.*;

/**
 * A provider for creating {@link LodestoneRenderType} instances.
 * Render types are cached to allow for non-static, conditional or procedural render type instances to be created
 * The cache is based on a {@link RenderTypeToken}, which will consider any added uniform handlers and modifiers as a separate token, leading to a different render type.
 */
public class RenderTypeProvider {

    private final Function<RenderTypeToken, LodestoneRenderType> provider;
    private final ConcurrentHashMap<RenderTypeToken, LodestoneRenderType> cache = new ConcurrentHashMap<>();

    private boolean hasGoneNuclear;
    private long nuclearTimeCache = 0;
    private int nuclearRenderTypeCount = 0;

    public RenderTypeProvider(Function<RenderTypeToken, LodestoneRenderType> provider) {
        this.provider = provider;
    }

    public LodestoneRenderTypeBuilder apply(RenderTypeToken token) {
        return new LodestoneRenderTypeBuilder(this, token);
    }

    /**
     * Creates a new {@link LodestoneRenderType} using the provided token and builder.
     * @param token the token to create the render type with
     * @return the created {@link LodestoneRenderType}
     */
    protected LodestoneRenderType createRenderType(RenderTypeToken token) {
        if (hasGoneNuclear) {
            return cache.entrySet().iterator().next().getValue();
        }
        if (cache.containsKey(token)) {
            return cache.get(token);
        }
        // TODO: test if we need this
        // We regenerate the token, which creates a new UUID and ensures that each RenderTypeToken stored in our cache is unique
        RenderTypeToken unique = token.unique();
        var renderType = provider.apply(unique);
        cache.put(unique, renderType);
        if (checkNuclear()) {
            hasGoneNuclear = true;
            LodestoneLib.LOGGER.warn(
                    "RenderTypeProvider has been called too often in a short time! This is very dangerous. " +
                            "Current count: {}, Time since last check: {}ms" +
                            "Render Type Provider: {}, Render Type Token: {}",
                    nuclearRenderTypeCount, System.currentTimeMillis() - nuclearTimeCache, this, unique);
        }
        return renderType;
    }

    @Override
    public String toString() {
        var cacheSample = cache.entrySet().stream()
                .limit(5)
                .map(entry -> entry.getKey().getIdentifier() + " -> " + entry.getValue().name)
                .toList();

        return "RenderTypeProvider{" + "provider=" + provider + ", cache sample=" + cacheSample + '}';
    }

    public boolean checkNuclear() {
        if (cache.size() >= 200) {
            return true;
        }
        int maxCalls = 1000;
        long thresholdMillis = 5000;
        if (nuclearTimeCache == 0) {
            nuclearTimeCache = System.currentTimeMillis();
        }

        nuclearRenderTypeCount++;

        long now = System.currentTimeMillis();
        if (now - nuclearTimeCache > thresholdMillis) {
            boolean tooOften = nuclearRenderTypeCount > maxCalls;
            nuclearRenderTypeCount = 0;
            nuclearTimeCache = now;
            return tooOften;
        }

        return false;
    }
}