package team.lodestar.lodestone.systems.model.objold.lod;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface LODBuilder<T> {
    void create(T argument, ResourceLocation modelLocation);
}