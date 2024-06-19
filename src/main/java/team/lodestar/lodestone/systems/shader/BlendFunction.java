package team.lodestar.lodestone.systems.shader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;
import team.lodestar.lodestone.LodestoneLib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import static org.lwjgl.opengl.GL14.*;

public enum BlendFunction {
    ADD("add", GL_FUNC_ADD),
    SUBTRACT("subtract", GL_FUNC_SUBTRACT),
    REVERSE_SUBTRACT("reverse_subtract", GL_FUNC_REVERSE_SUBTRACT),
    MIN("min", GL_MIN),
    MAX("max", GL_MAX);

    private final String name;
    private final int glConst;

    BlendFunction(String name, int glConst) {
        this.name = name;
        this.glConst = glConst;
    }

    public String getName() {
        return name;
    }

    public int getGlConst() {
        return glConst;
    }

    public static BlendFunction getFromName(String name) {
        for (BlendFunction blendFunction : values()) {
            if (blendFunction.getName().equals(name)) {
                return blendFunction;
            }
        }
        return ADD;
    }

    private static final Codec<BlendFunction> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("func").forGetter(BlendFunction::name)
            ).apply(instance, BlendFunction::getFromName)
    );

    public static void test() {
        ResourceProvider resourceProvider = Minecraft.getInstance().getResourceManager();
        ResourceLocation location = LodestoneLib.lodestonePath("test.json");

        try {
            Reader reader = resourceProvider.openAsReader(location);
            JsonObject jsonobject = GsonHelper.parse(reader);
            JsonElement jsonElement = jsonobject.get("help");
            DataResult<BlendFunction> result = CODEC.parse(JsonOps.INSTANCE, jsonElement);
            result.result().ifPresent(LodestoneLib.LOGGER::info);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
