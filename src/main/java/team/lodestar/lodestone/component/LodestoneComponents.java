package team.lodestar.lodestone.component;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import team.lodestar.lodestone.LodestoneLib;

public class LodestoneComponents implements EntityComponentInitializer, WorldComponentInitializer {
    public static final ComponentKey<LodestoneWorldComponent> LODESTONE_WORLD_COMPONENT =
            ComponentRegistry.getOrCreate(LodestoneLib.lodestonePath("world_event"), LodestoneWorldComponent.class);


    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(LODESTONE_WORLD_COMPONENT, LodestoneWorldComponent::new);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
    }
}
