package team.lodestar.lodestone.systems.postprocess;

import team.lodestar.lodestone.LodestoneLib;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LodestoneGlslPreprocessor extends GlslPreprocessor {
    @Nullable
    @Override
    public String applyImport(boolean p_173374_, String p_173375_) {
        LodestoneLib.LOGGER.debug("Loading moj_import in EffectProgram: " + p_173375_);

        ResourceLocation resourcelocation = new ResourceLocation(p_173375_);
        ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), "shaders/include/" + resourcelocation.getPath() + ".glsl");

        try {
            Resource resource1 = Minecraft.getInstance().getResourceManager().getResource(resourcelocation1);

            String s2;
            try {
                s2 = IOUtils.toString(resource1.getInputStream(), StandardCharsets.UTF_8);
            } catch (Throwable throwable1) {
                if (resource1 != null) {
                    try {
                        resource1.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }
                }

                throw throwable1;
            }

            if (resource1 != null) {
                resource1.close();
            }

            return s2;
        } catch (IOException ioexception) {
            LodestoneLib.LOGGER.error("Could not open GLSL import {}: {}", p_173375_, ioexception.getMessage());
            return "#error " + ioexception.getMessage();
        }
    }
}
