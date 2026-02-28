package team.lodestar.lodestone.renderer.texture;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL42.*;

public class BlankTexture extends AbstractTexture {
    private int width, height;

    @Override
    public void load(@NotNull ResourceManager resourceManager) throws IOException {
        int size = 200;
        this.width = size;
        this.height = size;
        createEmpty(size, size, true, InternalTextureFormat.RGBA8);
    }

    public void createEmpty(int width, int height, boolean linear, InternalTextureFormat format) {
        this.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, format.getGlFormat(), this.width, this.height, 0, format.getTexelFormat(), format.getGlType(), (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, linear ? GL_LINEAR : GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, linear ? GL_LINEAR : GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindImageTexture(0, this.id, 0, false, 0, GL_READ_ONLY, format.getGlFormat());
    }

    @Override
    public void bind() {
        super.bind();
    }
}
