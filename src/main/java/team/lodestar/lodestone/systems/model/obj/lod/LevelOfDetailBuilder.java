package team.lodestar.lodestone.systems.model.obj.lod;

import net.minecraft.resources.ResourceLocation;

public interface LevelOfDetailBuilder {
    void create(float argument, ResourceLocation modelLocation);
}
