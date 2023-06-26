package team.lodestar.lodestone.systems.postprocess;

import team.lodestar.lodestone.LodestoneLib;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LodestoneGlslPreprocessor extends GlslPreprocessor {

    public static final LodestoneGlslPreprocessor PREPROCESSOR = new LodestoneGlslPreprocessor();

    @Nullable
    @Override
    public String applyImport(boolean p_173374_, @Nonnull String p_173375_) {
        LodestoneLib.LOGGER.debug("Loading moj_import in EffectProgram: " + p_173375_);

        ResourceLocation resourcelocation = new ResourceLocation(p_173375_);
        ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), "shaders/include/" + resourcelocation.getPath() + ".glsl");

        var resource = Minecraft.getInstance().getResourceManager().getResource(resourcelocation1).orElseThrow();

        try {
            return IOUtils.toString(resource.open(), StandardCharsets.UTF_8);
        } catch (IOException ioexception) {
            LodestoneLib.LOGGER.error("Could not open GLSL import {}: {}", p_173375_, ioexception.getMessage());
            return "#error " + ioexception.getMessage();
        }
    }
}
