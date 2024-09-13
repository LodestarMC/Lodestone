package team.lodestar.lodestone.registry.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.obj.lod.LODStrategy;
import team.lodestar.lodestone.systems.model.obj.lod.LevelOfDetail;
import team.lodestar.lodestone.systems.model.obj.lod.MultiLODModel;
import team.lodestar.lodestone.systems.model.obj.ObjModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = LodestoneLib.LODESTONE, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LodestoneOBJModels {
    public static Map<ResourceLocation, ModelHolder> MODELS = new HashMap<>();
    public static List<ObjModel> OBJ_MODELS = new ArrayList<>();
    public static List<MultiLODModel<?>> LOD_MODELS = new ArrayList<>();

//    public static final MultiLODModel<?> LOD_MODEL = registerObjModel(new MultiLODModel<>(
//            LODStrategy.Distance(lodBuilder -> {
//                lodBuilder.create(5.0f, LodestoneLib.lodestonePath("cube"));
//                lodBuilder.create(1.0f, LodestoneLib.lodestonePath("one"));
//                lodBuilder.create(15.0f, LodestoneLib.lodestonePath("two"));
//                lodBuilder.create(20.0f, LodestoneLib.lodestonePath("three"));
//            })
//    ));

    public static ObjModel registerObjModel(ObjModel objModel) {
        OBJ_MODELS.add(objModel);
        return objModel;
    }

    public static MultiLODModel<?> registerObjModel(MultiLODModel lodModel) {
        LOD_MODELS.add(lodModel);
        return lodModel;
    }

    public static ObjModel register(ObjModel objModel) {
        MODELS.put(objModel.getModelId(), new ModelHolder(objModel));
        return objModel;
    }

    public static void loadModels() {
        OBJ_MODELS.forEach(ObjModel::loadModel);
        LOD_MODELS.forEach(MultiLODModel::loadModel);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onClientSetup(FMLClientSetupEvent event) {
        loadModels();
    }

    public static class ModelHolder {
        private final ObjModel model;

        public ModelHolder(ObjModel model) {
            this.model = model;
        }
        public boolean isMultiLODModel() {
            return model instanceof MultiLODModel;
        }
    }
}
