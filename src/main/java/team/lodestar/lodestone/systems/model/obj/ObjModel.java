package team.lodestar.lodestone.systems.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import team.lodestar.lodestone.LodestoneLib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ObjModel {
    public ArrayList<Face> faces = new ArrayList<>();
    public ResourceLocation modelLocation;
    protected ObjParser objParser;

    public ObjModel(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
        this.objParser = new ObjParser();
    }

    public void loadModel() {
        LodestoneLib.LOGGER.info("Loading model: " + modelLocation);
        String modID = this.modelLocation.getNamespace();
        String fileName = this.modelLocation.getPath();
        ResourceLocation resourceLocation = new ResourceLocation(modID, "obj/" + fileName + ".obj");
        Optional<Resource> resourceO = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
        if (resourceO.isEmpty()) {
            throw new RuntimeException("Resource not found: " + resourceLocation);
        }
        Resource resource = resourceO.get();
        try {
            this.objParser.parseObjFile(resource);
            this.faces = objParser.getFaces();
        } catch (IOException e) {
            LodestoneLib.LOGGER.error("Error parsing OBJ file: " + resourceLocation, e);
            e.printStackTrace();
        }
    }

    /**
     * Renders the model.
     * @param poseStack     The pose stack.
     * @param renderType    The render type.
     * @param packedLight   The packed light.
     */
    public void renderModel(PoseStack poseStack, RenderType renderType, int packedLight) {
        faces.forEach(face -> face.renderFace(poseStack, renderType, packedLight));
    }
}
