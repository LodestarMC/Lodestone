package team.lodestar.lodestone.events.types.worldevent;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;
/*TODO
public class WorldEventTypeRegistryEvent extends Event implements IModBusEvent {
    public WorldEventType create(ResourceLocation id, WorldEventType.EventInstanceSupplier instanceSupplier) {
        LodestoneLib.LOGGER.info("Registering world event type: " + id);
        WorldEventType worldEventType = new WorldEventType(id, instanceSupplier);
        LodestoneWorldEventTypeRegistry.registerEventType(worldEventType);
        return worldEventType;
    }
}

 */
