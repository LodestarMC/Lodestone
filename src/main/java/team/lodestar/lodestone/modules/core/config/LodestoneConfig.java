package team.lodestar.lodestone.modules.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class LodestoneConfig {

    protected final ArrayList<ConfigValueHolder<?>> configValues = new ArrayList<>();

    @SuppressWarnings("RedundantOperationOnEmptyContainer")
    public LodestoneConfig(ModConfigSpec.Builder builder) {
        for (ConfigValueHolder<?> configValue : configValues) {
            configValue.bake(builder);
        }
    }

    public <T> ConfigValueHolder<T> create(String path, ConfigValueHolder.ConfigValueSupplier<T> valueSupplier) {
        var holder = new ConfigValueHolder<>(path, valueSupplier);
        configValues.add(holder);
        return holder;
    }
}