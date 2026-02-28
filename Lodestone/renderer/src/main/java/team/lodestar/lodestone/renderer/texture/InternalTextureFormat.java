package team.lodestar.lodestone.renderer.texture;

import javax.annotation.Nullable;

import static org.lwjgl.opengl.GL31.*;

public enum InternalTextureFormat {
    RGBA8(GL_RGBA8, GL_UNSIGNED_BYTE, 4, GL_RGBA),
    RGBA16F(GL_RGBA16F, GL_HALF_FLOAT, 4, GL_RGBA),
    RGBA32F(GL_RGBA32F, GL_FLOAT, 4, GL_RGBA),
    RGBA16(GL_RGBA16, GL_UNSIGNED_SHORT, 4, GL_RGBA),
    RGBA16_SNORM(GL_RGBA16_SNORM, GL_SHORT, 4, GL_RGBA),
    RGBA8_SNORM(GL_RGBA8_SNORM, GL_BYTE, 4, GL_RGBA);

    private final int glFormat;
    private final int glType;
    private final int channels;
    private final int texelFormat;

    InternalTextureFormat(int glFormat, int glType, int channels, int texelFormat) {
        this.glFormat = glFormat;
        this.glType = glType;
        this.channels = channels;
        this.texelFormat = texelFormat;
    }

    public int getGlFormat() {
        return glFormat;
    }

    public int getGlType() {
        return glType;
    }

    public int getChannels() {
        return channels;
    }

    public int getTexelFormat() {
        return texelFormat;
    }

    public static @Nullable InternalTextureFormat fromString(String format) {
        for (InternalTextureFormat value : values()) {
            if (value.name().equalsIgnoreCase(format)) {
                return value;
            }
        }
        return null;
    }

}
