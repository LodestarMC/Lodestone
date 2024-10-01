package team.lodestar.lodestone.systems.postprocess;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.EffectInstance;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.LodestoneLib;


import java.util.ArrayList;
import java.util.List;

public abstract class MultiInstancePostProcessor<I extends DynamicShaderFxInstance> extends PostProcessor {
    private final List<DynamicShaderFxInstance> instances = new ArrayList<>(getMaxInstances());

    private final ShaderDataBuffer dataBuffer = new ShaderDataBuffer();

    /**
     * THIS VALUE SHOULD NOT CHANGE!!!
     *
     * @return max fx instance count
     */
    protected abstract int getMaxInstances();

    /**
     * THIS VALUE SHOULD NOT CHANGE!!!
     *
     * @return the size of data (how many floats) it takes for passing one fx instance to the shader
     */
    protected abstract int getDataSizePerInstance();

    @Override
    public void init() {
        super.init();

        dataBuffer.generate((long) getMaxInstances() * getDataSizePerInstance());
    }

    /**
     * Add a fx instance
     *
     * @return the instance got added or null if the amount of instances has reached the max
     */
    @Nullable
    public I addFxInstance(I instance) {
        if (instances.size() >= getMaxInstances()) {
            LodestoneLib.LOGGER.warn("Failed to add fx instance to " + this + ": reached max instance count of " + getMaxInstances());
            return null;
        }
        instances.add(instance);
        setActive(true);
        return instance;
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {
        for (int i = instances.size() - 1; i >= 0; i--) {
            DynamicShaderFxInstance instance = instances.get(i);
            instance.update(MC.getTimer().getGameTimeDeltaPartialTick(false));
            if (instance.isRemoved()) {
                instances.remove(i);
            }
        }

        if (instances.isEmpty()) {
            setActive(false);
            return;
        }

        float[] data = new float[instances.size() * getDataSizePerInstance()];
        for (int ins = 0; ins < instances.size(); ins++) {
            DynamicShaderFxInstance instance = instances.get(ins);
            int offset = ins * getDataSizePerInstance();
            instance.writeDataToBuffer((index, d) -> {
                if (index >= getDataSizePerInstance() || index < 0)
                    throw new IndexOutOfBoundsException(index);
                data[offset + index] = d;
            });
        }


//        float[] data = new float[getMaxInstances() * getDataSizePerInstance()];
//        for (int i=0; i<getMaxInstances() * getDataSizePerInstance(); i++) {
//            data[i] = (float) (time % 1F);
//        }


        dataBuffer.upload(data);
    }

    protected void setDataBufferUniform(EffectInstance effectInstance, String bufferName, String countName) {
        dataBuffer.apply(effectInstance, bufferName);
        effectInstance.safeGetUniform(countName).set(instances.size());
    }
}
