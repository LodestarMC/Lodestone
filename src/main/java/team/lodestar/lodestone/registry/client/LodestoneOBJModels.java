package team.lodestar.lodestone.registry.client;

import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.obj.lod.LODStrategy;
import team.lodestar.lodestone.systems.model.obj.lod.MultiLODModel;
import team.lodestar.lodestone.systems.model.obj.ObjModel;
import team.lodestar.lodestone.systems.model.obj.modifier.modifiers.TriangulateModifier;
import team.lodestar.lodestone.systems.model.obj.modifier.modifiers.TriangulateSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LodestoneOBJModels {
    // TODO: Track models by ResourceLocation & cache their modification history to prevent reparsing and reapplying modifiers
    public static List<ObjModel> OBJ_MODELS = new ArrayList<>();
    public static List<MultiLODModel> LOD_MODELS = new ArrayList<>();

    // TODO: Get rid of this
//    public static final MultiLODModel LOD_MODEL = register(new MultiLODModel(
//            LODStrategy.Distance(lodBuilder -> {
//                lodBuilder.create(5.0f, LodestoneLib.lodestonePath("cube"));
//                lodBuilder.create(1.0f, LodestoneLib.lodestonePath("one"));
//                lodBuilder.create(15.0f, LodestoneLib.lodestonePath("two"));
//                lodBuilder.create(20.0f, LodestoneLib.lodestonePath("three"));
//            })
//    ));
//
//    public static final MultiLODModel LOD_MODEL_GRAPHICS = register(new MultiLODModel(
//            LODStrategy.Graphics(lodBuilder -> {
//                lodBuilder.create(GraphicsStatus.FAST, LodestoneLib.lodestonePath("cube"));
//                lodBuilder.create(GraphicsStatus.FANCY, LodestoneLib.lodestonePath("one"));
//                lodBuilder.create(GraphicsStatus.FABULOUS, LodestoneLib.lodestonePath("two"));
//            })
//    ));
//
//    public static final ObjModel CUBE = register(new ObjModel(LodestoneLib.lodestonePath("cube")));
//    public static final ObjModel SUZANNE = register(ObjModel.Builder.of(LodestoneLib.lodestonePath("one"))
//            .withModifiers(queue -> {
//                queue.queueModifier(new TriangulateModifier());
//            })
//            .build()
//    );

    public static ObjModel register(ObjModel objModel) {
        OBJ_MODELS.add(objModel);
        return objModel;
    }

    public static MultiLODModel register(MultiLODModel lodModel) {
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
