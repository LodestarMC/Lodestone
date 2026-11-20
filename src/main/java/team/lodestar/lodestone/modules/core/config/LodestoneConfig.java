package team.lodestar.lodestone.modules.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;

public abstract class LodestoneConfig {

    protected final ArrayList<ConfigValueHolder<?>> configValues = new ArrayList<>();

    public <T> ConfigValueHolder<T> create(String path, ConfigValueHolder.ConfigValueSupplier<T> valueSupplier) {
        var holder = new ConfigValueHolder<>(path, valueSupplier);
        configValues.add(holder);
        return holder;
    }

    public void createValues(ModConfigSpec.Builder builder) {
        for (ConfigValueHolder<?> configValue : configValues) {
            configValue.bake(builder);
        }
    }
}