package team.lodestar.lodestone.systems.entityrenderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

public class LodestoneBoatRenderer extends BoatRenderer {
    private final ResourceLocation boatTexture;
    private final BoatModel boatModel;

    public LodestoneBoatRenderer(EntityRendererProvider.Context context, ResourceLocation boatTexture, boolean hasChest) {
        super(context, hasChest);
        this.boatTexture = boatTexture;
        this.boatModel = new BoatModel(BoatModel.createBodyModel().bakeRoot());
        this.boatResources = Stream.of(Boat.Type.values()).collect(
                ImmutableMap.toImmutableMap((type) -> type, (type) -> Pair.of(boatTexture, boatModel)));
    }
}