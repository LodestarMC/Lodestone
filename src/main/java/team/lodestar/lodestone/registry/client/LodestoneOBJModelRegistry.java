package team.lodestar.lodestone.registry.client;

import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.obj.lod.MultiLODModel;
import team.lodestar.lodestone.systems.model.obj.ObjModel;
import team.lodestar.lodestone.systems.model.obj.lod.LODStrategy;

import java.util.ArrayList;
import java.util.List;

public class LodestoneOBJModelRegistry {
    public static List<ObjModel> OBJ_MODELS = new ArrayList<>();
    public static List<MultiLODModel<?>> LOD_MODELS = new ArrayList<>();

//    public static final MultiLODModel<?> LOD_MODEL = registerObjModel(new MultiLODModel<>(
//            LODStrategy.Distance(lodBuilder -> {
//                lodBuilder.create(Mth.square(5.0f), LodestoneLib.lodestonePath("cube"));
//                lodBuilder.create(Mth.square(10.0f), LodestoneLib.lodestonePath("one"));
//                lodBuilder.create(Mth.square(15.0f), LodestoneLib.lodestonePath("two"));
//                lodBuilder.create(Mth.square(20.0f), LodestoneLib.lodestonePath("three"));
//            })
//    ));

    public static ObjModel registerObjModel(ObjModel objModel) {
        OBJ_MODELS.add(objModel);
        return objModel;
    }

    public static MultiLODModel<?> registerObjModel(MultiLODModel<?> lodModel) {
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
