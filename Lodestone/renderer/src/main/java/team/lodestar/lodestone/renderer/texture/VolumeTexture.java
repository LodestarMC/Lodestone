package team.lodestar.lodestone.renderer.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL42.glBindImageTexture;

public class VolumeTexture extends AbstractTexture {
    private int width, height, depth;
    private boolean shouldReload = true;
    private boolean initialized = false;

    public VolumeTexture(int size) {
        this(size, size, size);
    }

    public VolumeTexture(int size, boolean shouldReload) {
        this(size);
        this.shouldReload = shouldReload;
    }

    public VolumeTexture(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public VolumeTexture(int width, int height, int depth, boolean shouldReload) {
        this(width, height, depth);
        this.shouldReload = shouldReload;
    }

    @Override
    public void load(@NotNull ResourceManager resourceManager) {
        if (!this.initialized) {
            this.createEmpty(true, InternalTextureFormat.RGBA16F);
            this.initialized = true;
        } else if (this.shouldReload) {
            createEmpty(true, InternalTextureFormat.RGBA16F);
        }
    }

    public void createEmpty(boolean linear, InternalTextureFormat format) {

        this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_3D, this.id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage3D(GL_TEXTURE_3D, 0, format.getGlFormat(), width, height, depth, 0, GL_RGBA, format.getGlType(), (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, linear ? GL_LINEAR : GL_NEAREST);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, linear ? GL_LINEAR : GL_NEAREST);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_3D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glGenerateMipmap(GL_TEXTURE_3D);
        glBindTexture(GL_TEXTURE_3D, 0);
        glBindImageTexture(0, this.id, 0, true, 0, GL_READ_ONLY, GL_RGBA16F);
    }

    @Override
    public void bind() {
        RenderSystem.assertOnRenderThreadOrInit();
        glBindTexture(GL_TEXTURE_3D, this.id);
    }

    @Override
    public void setFilter(boolean blur, boolean mipmap) {
        RenderSystem.assertOnRenderThreadOrInit();
        this.blur = blur;
        this.mipmap = mipmap;
        int i;
        short j;
        if (blur) {
            i = mipmap ? GL_LINEAR_MIPMAP_LINEAR : GL_LINEAR;
            j = GL_LINEAR;
        } else {
            i = mipmap ? GL_NEAREST_MIPMAP_LINEAR : GL_NEAREST;
            j = GL_NEAREST;
        }

        this.bind();
        GlStateManager._texParameter(GL_TEXTURE_3D, GL_TEXTURE_MIN_FILTER, i);
        GlStateManager._texParameter(GL_TEXTURE_3D, GL_TEXTURE_MAG_FILTER, j);
    }
}
