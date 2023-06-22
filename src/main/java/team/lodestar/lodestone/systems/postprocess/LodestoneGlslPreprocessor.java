package team.lodestar.lodestone.systems.postprocess;

import com.google.common.collect.*;
import net.minecraft.*;
import team.lodestar.lodestone.LodestoneLib;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LodestoneGlslPreprocessor extends GlslPreprocessor {

    public static final LodestoneGlslPreprocessor PREPROCESSOR = new LodestoneGlslPreprocessor();

    @Nullable
    @Override
    public String applyImport(boolean pUseFullPath, String pDirectory) {
        ResourceLocation resourcelocation = new ResourceLocation(pDirectory);
        ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), "shaders/include/" + resourcelocation.getPath());
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
            LodestoneLib.LOGGER.error("Could not open GLSL import {}: {}", pDirectory, ioexception.getMessage());
            return "#error " + ioexception.getMessage();
        }
    }
}
