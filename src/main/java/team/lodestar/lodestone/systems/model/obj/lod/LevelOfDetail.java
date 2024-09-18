package team.lodestar.lodestone.systems.model.obj.lod;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

/**
 * Represents a single level of detail for an {@link MultiLODModel}
 * <p>Extends {@link ObjModel} to allow for the same rendering and loading capabilities</p>
 * <p>Stores an argument that can be used to determine when to render this level of detail</p>
 * @param <T> The type of the argument
 */
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