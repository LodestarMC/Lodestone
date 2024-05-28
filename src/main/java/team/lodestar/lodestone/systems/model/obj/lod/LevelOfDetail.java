package team.lodestar.lodestone.systems.model.obj.lod;

import team.lodestar.lodestone.systems.model.obj.ObjModel;

public class LevelOfDetail {
    private final ObjModel model;
    private final float argument;

    public LevelOfDetail(ObjModel model, float argument) {
        this.model = model;
        this.argument = argument;
    }

    public ObjModel getModel() {
        return model;
    }

    public float getArgument() {
        return argument;
    }


}
