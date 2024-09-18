package team.lodestar.lodestone.systems.model;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.Resource;
import team.lodestar.lodestone.systems.model.obj.IndexedModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public abstract class LodestoneParser<M extends IndexedModel> {
    public void startParse(M model) {
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(model.getAssetLocation());
        if (resource.isEmpty()) throw new RuntimeException("Lodestone Model" + model.getModelId() + " not found at " + model.getAssetLocation());
        try {
            InputStream inputStream = resource.get().open();
            parse(inputStream, model);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Model file: " + model.getModelId(), e);
        }
    }

    public abstract void parse(InputStream inputStream, M model) throws IOException;
}
