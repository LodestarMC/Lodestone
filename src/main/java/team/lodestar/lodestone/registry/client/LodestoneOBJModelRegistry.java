package team.lodestar.lodestone.registry.client;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.obj.lod.MultiLODModel;
import team.lodestar.lodestone.systems.model.obj.ObjModel;
import team.lodestar.lodestone.systems.model.obj.lod.strategy.LODStrategy;
import team.lodestar.lodestone.systems.model.obj.modifier.TriangulateModifier;

import java.util.ArrayList;
import java.util.List;

public class LodestoneOBJModelRegistry {
    public static List<ObjModel> OBJ_MODELS = new ArrayList<>();
    public static List<MultiLODModel> LOD_MODELS = new ArrayList<>();

//    public static final ObjModel ICO_SPHERE = registerObjModel(new ObjModel(LodestoneLib.lodestonePath("cube")));
//
//    public static final ObjModel ICO_SPHERE_MODIFIED = registerObjModel(new ObjModel(LodestoneLib.lodestonePath("cube"), modifierBuilder -> {
//        modifierBuilder.applyModifier(new TriangulateModifier());
//    }));
//
//    public static final MultiLODModel LOD_MODEL = registerObjModel(new MultiLODModel(LODStrategy.Distance, lodBuilder -> {
//        lodBuilder.create(5.0f, LodestoneLib.lodestonePath("cube"));
//        lodBuilder.create(10.0f, LodestoneLib.lodestonePath("one"));
//        lodBuilder.create(15.0f, LodestoneLib.lodestonePath("two"));
//        lodBuilder.create(20.0f, LodestoneLib.lodestonePath("three"));
//    }));

    public static ObjModel registerObjModel(ObjModel objModel) {
        OBJ_MODELS.add(objModel);
        return objModel;
    }

    public static MultiLODModel registerObjModel(MultiLODModel lodModel) {
        LOD_MODELS.add(lodModel);
        return lodModel;
    }

    public static void loadModels() {
        OBJ_MODELS.forEach(ObjModel::loadModel);
        LOD_MODELS.forEach(MultiLODModel::loadModel);
    }


    public static void onClientSetup() {
        loadModels();
    }
}
