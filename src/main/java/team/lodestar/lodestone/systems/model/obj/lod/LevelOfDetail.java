package team.lodestar.lodestone.systems.model.obj.lod;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

public class LevelOfDetail<T> extends ObjModel {
    private final T argument;

    public LevelOfDetail(ResourceLocation resourceLocation, T argument) {
        super(resourceLocation);
        this.argument = argument;
    }

    public T getArgument() {
        return argument;
    }
}