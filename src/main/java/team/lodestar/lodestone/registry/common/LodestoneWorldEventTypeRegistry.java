package team.lodestar.lodestone.registry.common;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.List;

public class LodestoneWorldEventTypeRegistry {
    public static ResourceKey<Registry<WorldEventType>> WORLD_EVENT_TYPE_KEY = ResourceKey.createRegistryKey(LodestoneLib.lodestonePath("world_event_type"));
    private static final DeferredRegister<WorldEventType> WORLD_EVENT_TYPES = DeferredRegister.create(WORLD_EVENT_TYPE_KEY, LodestoneLib.LODESTONE);
    public static final Registry<WorldEventType> WORLD_EVENT_TYPE_REGISTRY = WORLD_EVENT_TYPES.makeRegistry(builder -> builder.sync(true));


    /**
     * Creates a new world event type registry for the given mod ID.
     *
     * @param modId Your mod ID.
     * @return The deferred register for WorldEventType.
     */
    public static DeferredRegister<WorldEventType> createRegistry(String modId) {
        return DeferredRegister.create(WORLD_EVENT_TYPE_KEY, modId);
    }

    public static List<WorldEventType> getEventTypes() {
        return WORLD_EVENT_TYPE_REGISTRY.stream().toList();
    }
}
