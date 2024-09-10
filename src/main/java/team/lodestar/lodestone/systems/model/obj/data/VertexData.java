package team.lodestar.lodestone.systems.model.obj.data;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.LightTexture;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import team.lodestar.lodestone.systems.model.obj.VertexException;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

/**
 * Stores the data for a single vertex, allowing for easy definition and retrieval of vertex attributes
 * <p>Due to the usage of a {@link DefaultOverrideHashMap}, default values can be set for each attribute, which will be used if no override is provided</p>
 * <p>When parsing a model, default values are typically set to the parsed values from the model file</p>
 * <p>You can override these values by calling {@link #put(VertexFormatElement, Float...)} and retrieve them with {@link #get(VertexFormatElement)}</p>
 */
public class VertexData implements VertexConsumer {
    private DefaultOverrideHashMap<VertexFormatElement, List<Float>> vertexAttributes;

    public VertexData() {
        this.vertexAttributes = new DefaultOverrideHashMap<>();
    }

    /**
     * Adds override data for the given element
     *
     * @param element
     * @param data
     */
    public void put(VertexFormatElement element, Float... data) {
        this.vertexAttributes.putOverride(element, List.of(data));
    }

    /**
     * Puts the default data for the given element, default values will be used unless overridden by {@link #put(VertexFormatElement, Float...)}
     *
     * Default data typically includes the parsed values from the model file
     *
     * @param element
     * @param data
     */
    public void putDefault(VertexFormatElement element, Float... data) {
        this.vertexAttributes.putDefault(element, List.of(data));
    }

    protected void putBulkDefault(Vector3f position, Vector3f normal, Vector2f uv) {
        this.putDefault(VertexFormatElement.POSITION, position.x, position.y, position.z);
        this.putDefault(VertexFormatElement.NORMAL, normal.x, normal.y, normal.z);
        this.putDefault(VertexFormatElement.UV, uv.x, uv.y);
        this.putDefault(VertexFormatElement.COLOR, 1.0f, 1.0f, 1.0f, 1.0f);
        this.putDefault(VertexFormatElement.UV2, (float) (LightTexture.FULL_BRIGHT & 65535), (float) (LightTexture.FULL_BRIGHT >> 16 & 65535));
    }

    public void clear() {
        this.vertexAttributes.clearOverride();
    }

    protected boolean hasElement(VertexFormatElement element) {
        return this.vertexAttributes.containsKeyOverride(element);
    }

    protected boolean hasDefaultElement(VertexFormatElement element) {
        return this.vertexAttributes.containsKeyDefault(element);
    }

    private boolean hasElementOrDefault(VertexFormatElement element) {
        return this.vertexAttributes.containsKeyOrDefault(element);
    }

    private List<Float> getDefaultAttribute(VertexFormatElement element) {
        return this.vertexAttributes.getDefault(element);
    }

    public List<Float> getOrDefaultAttribute(VertexFormatElement element) {
        return this.vertexAttributes.getOrDefault(element, null);
    }

    public List<Float> get(VertexFormatElement element) throws VertexException {
        if (!this.hasElementOrDefault(element)) {
            throw new VertexException("Vertex is missing required element: " + element);
        } else {
            return this.getOrDefaultAttribute(element);
        }
    }

    public void setupAttributes(VertexFormat format) {
        int vertexSize = format.getVertexSize();
        for (int i = 0; i < format.getElements().size(); i++) {
            VertexFormatElement element = format.getElements().get(i);
            GlStateManager._enableVertexAttribArray(i);
            element.setupBufferState(i, format.getOffset(element), vertexSize);
        }
    }



    public int storeAttribute(VertexFormatElement element, float[] data) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length).put(data).flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vbo;
    }


    @Override
    public @NotNull VertexConsumer addVertex(float pX, float pY, float pZ) {
        this.put(VertexFormatElement.POSITION, pX, pY, pZ);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setColor(int pRed, int pGreen, int pBlue, int pAlpha) {
        this.put(VertexFormatElement.COLOR, (float) pRed, (float) pGreen, (float) pBlue, (float) pAlpha);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv(float pU, float pV) {
        this.put(VertexFormatElement.UV, pU, pV);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv1(int pU, int pV) {
        this.put(VertexFormatElement.UV1, (float) pU, (float) pV);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv2(int pU, int pV) {
        this.put(VertexFormatElement.UV2, (float) pU, (float) pV);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setNormal(float pNormalX, float pNormalY, float pNormalZ) {
        this.put(VertexFormatElement.NORMAL, pNormalX, pNormalY, pNormalZ);
        return this;
    }

    public void supplyPosition(VertexConsumer vertexConsumer, PoseStack poseStack) {
        List<Float> position = this.getOrDefaultAttribute(VertexFormatElement.POSITION);
        vertexConsumer.addVertex(poseStack.last().pose(), position.get(0), position.get(1), position.get(2));
    }

    public void supplyColor(VertexConsumer vertexConsumer) {
        List<Float> color = this.getOrDefaultAttribute(VertexFormatElement.COLOR);
        vertexConsumer.setColor((int) (color.get(0) * 255), (int) (color.get(1) * 255), (int) (color.get(2) * 255), (int) (color.get(3) * 255));
    }

    public void supplyUv(VertexConsumer vertexConsumer) {
        List<Float> uv = this.getOrDefaultAttribute(VertexFormatElement.UV);
        vertexConsumer.setUv(uv.get(0), uv.get(1));
    }

    public void supplyUv1(VertexConsumer vertexConsumer) {
        List<Float> uv1 = this.getOrDefaultAttribute(VertexFormatElement.UV1);
        vertexConsumer.setUv1(uv1.get(0).intValue(), uv1.get(1).intValue());
    }

    public void supplyUv2(VertexConsumer vertexConsumer) {
        List<Float> uv2 = this.getOrDefaultAttribute(VertexFormatElement.UV2);
        vertexConsumer.setUv2(uv2.get(0).intValue(), uv2.get(1).intValue());
    }

    public void supplyNormal(VertexConsumer vertexConsumer, PoseStack poseStack) {
        List<Float> normal = this.getOrDefaultAttribute(VertexFormatElement.NORMAL);
        vertexConsumer.setNormal(poseStack.last(), normal.get(0), normal.get(1), normal.get(2));
    }

    public void supplyVertexData(VertexConsumer vertexConsumer, VertexFormat format, PoseStack poseStack) {
        format.getElements().forEach(element -> {
            if (element == VertexFormatElement.POSITION) {
                this.supplyPosition(vertexConsumer, poseStack);
            } else if (element == VertexFormatElement.COLOR) {
                this.supplyColor(vertexConsumer);
            } else if (element == VertexFormatElement.UV) {
                this.supplyUv(vertexConsumer);
            } else if (element == VertexFormatElement.UV1) {
                this.supplyUv1(vertexConsumer);
            } else if (element == VertexFormatElement.UV2) {
                this.supplyUv2(vertexConsumer);
            } else if (element == VertexFormatElement.NORMAL) {
                this.supplyNormal(vertexConsumer, poseStack);
            }
        });
    }
}
