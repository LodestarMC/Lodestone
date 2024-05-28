package team.lodestar.lodestone.systems.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.model.obj.modifier.ModifierBuilder;
import team.lodestar.lodestone.systems.model.obj.modifier.ObjModifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ObjModel implements ModifierBuilder {
    public ArrayList<Face> faces = new ArrayList<>();
    public ResourceLocation modelLocation;
    private final List<ObjModifier> modifiers = new ArrayList<>();

    /**
     * @param modelLocation The location of the model. Ex: new ResourceLocation("modid", "model") "/assets/modid/obj/model.obj"
     */
    public ObjModel(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
    }

    public ObjModel(ResourceLocation modelLocation, ModifierBuilderSetup setup) {
        this.modelLocation = modelLocation;
        setup.setup(this);
    }

    public void loadModel() {
        LodestoneLib.LOGGER.info("Loading model: " + modelLocation);
        String modID = this.modelLocation.getNamespace();
        String fileName = this.modelLocation.getPath();
        ResourceLocation resourceLocation = new ResourceLocation(modID, "obj/" + fileName + ".obj");
        LodestoneLib.LOGGER.info("Resolved resource location: " + resourceLocation);
        Optional<Resource> resourceO = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
        if (resourceO.isEmpty()) {
            LodestoneLib.LOGGER.error("Resource not found: " + resourceLocation);
            throw new RuntimeException("Resource not found: " + resourceLocation);
        }
        Resource resource = resourceO.get();
        ObjParser objParser = new ObjParser();
        try {
            objParser.parseObjFile(resource);
            this.faces = objParser.getFaces();
        } catch (IOException e) {
            LodestoneLib.LOGGER.error("Error parsing OBJ file: " + resourceLocation, e);
            e.printStackTrace();
        }
    }

    /**
     * Renders the model.
     *
     * @param poseStack     The pose stack.
     * @param buffer        The vertex consumer.
     * @param packedLight   The packed light. 0-255;
     */
    public void renderModel(PoseStack poseStack, VertexConsumer buffer, int packedLight) {
        faces.forEach(face -> {
            face.renderFace(poseStack, buffer, packedLight);
        });
    }

    @Override
    public void applyModifier(ObjModifier modifier) {
        modifiers.add(modifier);
    }

    @FunctionalInterface
    public interface ModifierBuilderSetup {
        void setup(ModifierBuilder builder);
    }
}
