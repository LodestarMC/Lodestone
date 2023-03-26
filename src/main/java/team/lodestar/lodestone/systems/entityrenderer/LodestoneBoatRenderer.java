package team.lodestar.lodestone.systems.entityrenderer;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.NotNull;

public class LodestoneBoatRenderer extends BoatRenderer {
    private final ResourceLocation boatTexture;
    private final BoatModel boatModel;

    public LodestoneBoatRenderer(EntityRendererProvider.Context context, ResourceLocation boatTexture, boolean hasChest) {
        super(context, hasChest);
        this.boatTexture = boatTexture;
        this.boatModel = new BoatModel(BoatModel.createBodyModel(hasChest).bakeRoot(), hasChest);
    }

    @Override
    @NotNull
    public Pair<ResourceLocation, BoatModel> getModelWithLocation(Boat boat) {
        return Pair.of(boatTexture, boatModel);
    }
}