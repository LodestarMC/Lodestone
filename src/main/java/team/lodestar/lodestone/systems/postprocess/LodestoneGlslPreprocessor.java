package team.lodestar.lodestone.systems.postprocess;

import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.LodestoneLib;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LodestoneGlslPreprocessor extends GlslPreprocessor {

    public static final LodestoneGlslPreprocessor PREPROCESSOR = new LodestoneGlslPreprocessor();

    @Nullable
    @Override
    public String applyImport(boolean pUseFullPath, String pDirectory) {
        ResourceLocation resourcelocation = ResourceLocation.parse(pDirectory);
        ResourceLocation resourcelocation1 = ResourceLocation.fromNamespaceAndPath(resourcelocation.getNamespace(), "shaders/include/" + resourcelocation.getPath());
        try {
            Resource resource1 = Minecraft.getInstance().getResourceManager().getResource(resourcelocation1).get();

            return IOUtils.toString(resource1.open(), StandardCharsets.UTF_8);
        } catch (IOException ioexception) {
            LodestoneLib.LOGGER.error("Could not open GLSL import {}: {}", pDirectory, ioexception.getMessage());
            return "#error " + ioexception.getMessage();
        }
    }
}
