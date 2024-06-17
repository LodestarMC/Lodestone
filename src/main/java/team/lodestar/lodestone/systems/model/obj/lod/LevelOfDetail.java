package team.lodestar.lodestone.systems.model.obj.lod;

import team.lodestar.lodestone.systems.model.obj.ObjModel;

public class LevelOfDetail<T> {
    private final ObjModel model;
    private final T argument;

    public LevelOfDetail(ObjModel model, T argument) {
        this.model = model;
        this.argument = argument;
    }

    public ObjModel getModel() {
        return model;
    }

    public T getArgument() {
        return argument;
    }
}
