package team.lodestar.lodestone.systems.rendering.multipass;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import team.lodestar.lodestone.LodestoneLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DynamicTexture extends AbstractTexture {

    //thing where it renders stuff on
    private RenderTarget frameBuffer;

    private final int width;
    private final int height;
    private final ResourceLocation resourceLocation;

    //cpu side of the texture. used to dynamically edit it
    @Nullable
    private NativeImage cpuImage;

    public DynamicTexture(ResourceLocation resourceLocation, int width, int height) {
        // super(width, height, false);
        this.width = width;
        this.height = height;
        //register this texture
        this.resourceLocation = resourceLocation;
        Minecraft.getInstance().getTextureManager().register(resourceLocation, this);
    }

    public DynamicTexture(ResourceLocation resourceLocation, int size) {
        this(resourceLocation, size, size);
    }

    @Override
    public void load(ResourceManager manager) {
    }

    public RenderTarget getFrameBuffer() {
        //initialize the frame buffer (do not touch, magic code)
        if (this.frameBuffer == null) {
            this.frameBuffer = new MainTarget(width, height);
            this.id = this.frameBuffer.getColorTextureId(); // just in case
        }
        return this.frameBuffer;
    }

    //sets current frame buffer to this. Further render calls will draw here
    public void bindWrite() {
        getFrameBuffer().bindWrite(true);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ResourceLocation getTextureLocation() {
        return resourceLocation;
    }

    @Override
    public int getId() {
        return this.getFrameBuffer().getColorTextureId();
    }

    @Override
    public void releaseId() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(this::clearGlId0);
        } else {
            this.clearGlId0();
        }
    }

    private void clearGlId0() {
        if (this.frameBuffer != null) {
            this.frameBuffer.destroyBuffers();
            this.frameBuffer = null;
        }
        this.id = -1;
    }

    @Override
    public void close() {
        //releases texture id
        this.releaseId();
        //closes native image and texture
        if (this.cpuImage != null) {
            this.cpuImage.close();
            this.cpuImage = null;
        }
        //destroy render buffer
        //release register texture resource location and id. called just to be sure. releaseId should already do this
        Minecraft.getInstance().getTextureManager().release(resourceLocation);
    }

    public NativeImage getPixels() {
        if (this.cpuImage == null) {
            this.cpuImage = new NativeImage(width, height, false);
        }
        return cpuImage;
    }

    /**
     * Downloads the GPU texture to CPU for edit
     */
    public void download() {
        this.bind();
        getPixels().downloadTexture(0, false);
        //cpuImage.flipY();
    }

    /**
     * Uploads the image to GPU and closes its CPU side one
     */
    public void upload() {
        if (this.cpuImage != null) {
            this.bind();
            this.cpuImage.upload(0, 0, 0, false);
            this.cpuImage.close();
            this.cpuImage = null;
        } else {
            LodestoneLib.LOGGER.warn("Trying to upload disposed texture {}", (int) this.getId());
        }
    }

    public List<Path> saveTextureToFile(Path texturesDir) throws IOException {
        this.bind();
        String name = this.resourceLocation.getPath().replace("/", "_");

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        int parentTextureWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int parentTextureHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

        int minimumSize = Math.min(parentTextureWidth, parentTextureHeight);
        int mipmapLevels = Mth.log2(minimumSize);

        List<Path> textureFiles = new ArrayList<>();
        for (int level = 0; level < mipmapLevels; level++) {
            int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, level, GL11.GL_TEXTURE_WIDTH);
            int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, level, GL11.GL_TEXTURE_HEIGHT);
            int size = width * height;
            if (size == 0) {
                break;
            }

            BufferedImage bufferedimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Path output = texturesDir.resolve(name + "_mipmap_" + level + ".png");
            IntBuffer buffer = BufferUtils.createIntBuffer(size);
            int[] data = new int[size];

            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, level, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
            buffer.get(data);
            bufferedimage.setRGB(0, 0, width, height, data, 0, width);

            ImageIO.write(bufferedimage, "png", output.toFile());
            //   WoodGood.LOGGER.info("Exported png to: {}", output.toString());
            textureFiles.add(output);
        }
        return textureFiles;
    }
}