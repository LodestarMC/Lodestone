package team.lodestar.lodestone.systems.model;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.Resource;

import java.io.IOException;
import java.util.Optional;

public abstract class LodestoneParser<M extends IRenderableModel> {
    public void startParse(M model) {
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(model.getModelLocation());
        if (resource.isEmpty()) throw new RuntimeException("Lodestone Model not found at " + model.getModelLocation());
        try {
            parse(resource.get(), model);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Model file: " + model.getModelLocation(), e);
        }
    }

    public abstract void parse(Resource resource, M model) throws IOException;
}
