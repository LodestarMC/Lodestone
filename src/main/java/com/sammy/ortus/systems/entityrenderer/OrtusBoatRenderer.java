package com.sammy.ortus.systems.entityrenderer;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public class OrtusBoatRenderer extends BoatRenderer {
    private final ResourceLocation boatTexture;
    private final BoatModel boatModel;

    public OrtusBoatRenderer(EntityRendererProvider.Context context, ResourceLocation boatTexture) {
        super(context);
        this.boatTexture = boatTexture;
        boatModel = new BoatModel(BoatModel.createBodyModel().bakeRoot());
    }

    public Pair<ResourceLocation, BoatModel> getModelWithLocation(Boat boat) {
        return Pair.of(boatTexture, boatModel);
    }
}