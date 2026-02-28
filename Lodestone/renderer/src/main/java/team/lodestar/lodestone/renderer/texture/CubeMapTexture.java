package team.lodestar.lodestone.renderer.texture;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Optional;

import static org.lwjgl.opengl.GL30.*;

public class CubeMapTexture extends AbstractTexture {
    protected final ResourceLocation location;
    protected final boolean alpha;
    private int width, height;
    private static final String[] CUBE_MAP_SIDES = {
            "right", "left", "top", "bottom", "front", "back"
    };

    public CubeMapTexture(ResourceLocation location, boolean hasAlphaChannel) {
        this.location = location;
        this.alpha = hasAlphaChannel;
    }

    @Override
    public void load(@NotNull ResourceManager resourceManager) {
        this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, id);

        for (int i = 0; i < CUBE_MAP_SIDES.length; i++) {
            ResourceLocation sideLocation = ResourceLocation.fromNamespaceAndPath(location.getNamespace(), location.getPath() + "_" + CUBE_MAP_SIDES[i] + (this.alpha ? ".png" : ".jpg"));
            Optional<Resource> resource = resourceManager.getResource(sideLocation);
            if (resource.isPresent()) {
                try {
                    ByteBuffer textureData = TextureUtil.readResource(resource.get().open());
                    textureData.rewind();

                    ByteBuffer image;
                    try (MemoryStack stack = MemoryStack.stackPush()) {
                        IntBuffer width = stack.mallocInt(1);
                        IntBuffer height = stack.mallocInt(1);
                        IntBuffer channels = stack.mallocInt(1);

                        image = STBImage.stbi_load_from_memory(textureData, width, height, channels, alpha ? 4 : 3);
                        if (image == null) {
                            throw new IOException("Could not load image: " + STBImage.stbi_failure_reason());
                        }
                        int texFormat = this.alpha ? GL_RGBA : GL_RGB;
                        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, texFormat, width.get(), height.get(), 0, texFormat, GL_UNSIGNED_BYTE, image);
                        this.width = width.get(0);
                        this.height = height.get(0);
                        STBImage.stbi_image_free(image);
                    }


                } catch (Exception e) {
                    throw new RuntimeException("Failed to load cubemap side: " + sideLocation, e);
                }
            }
        }

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);


    }

    @Override
    public void bind() {
        RenderSystem.assertOnRenderThreadOrInit();
        glBindTexture(GL_TEXTURE_CUBE_MAP, this.id);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
