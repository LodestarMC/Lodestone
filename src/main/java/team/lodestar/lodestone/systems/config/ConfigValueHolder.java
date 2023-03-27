package team.lodestar.lodestone.systems.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;

public class ConfigValueHolder<T> {
    private final LodestoneConfig.BuilderSupplier<T> valueSupplier;
    private ForgeConfigSpec.ConfigValue<T> config;

    /**
     * @param configGroup   - The config group this config value belongs to. For example, If we want to add a config value to "lodestone:config", we would need to use that
     * @param path          - Path towards your value, separated by "/".
     * @param valueSupplier - Supplier to your config value. {@link ConfigValueHolder#config} will be set to the supplied value when config is initialized.
     */
    public ConfigValueHolder(ConfigGroup configGroup, String path, LodestoneConfig.BuilderSupplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
        LodestoneConfig.getConfigEntries(configGroup).computeIfAbsent(path, s -> new ArrayList<>()).add(this);
    }


    public void setConfig(ForgeConfigSpec.Builder builder) {
        config = valueSupplier.createBuilder(builder);
    }

    public void setConfigValue(T t) {
        config.set(t);
    }

    public ForgeConfigSpec.ConfigValue<T> getConfig() {
        return config;
    }

    public T getConfigValue() {
        return config.get();
    }
}
