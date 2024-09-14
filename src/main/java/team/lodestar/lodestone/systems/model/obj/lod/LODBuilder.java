package team.lodestar.lodestone.systems.model.obj.lod;

import net.minecraft.resources.ResourceLocation;

/**
 * An interface for building the levels of detail for a {@link MultiLODModel}
 * @param <T> The type of the argument
 */
@FunctionalInterface
public interface LODBuilder<T> {
    void create(T argument, ResourceLocation modelLocation);
}
