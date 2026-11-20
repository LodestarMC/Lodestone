package team.lodestar.lodestone.modules.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigValueHolder<T> {

    private final String path;
    private final ConfigValueSupplier<T> valueSupplier;
    private ModConfigSpec.ConfigValue<T> config;

    public ConfigValueHolder(String path, ConfigValueSupplier<T> valueSupplier) {
        this.path = path;
        this.valueSupplier = valueSupplier;
    }

    public void bake(ModConfigSpec.Builder builder) {
        builder.push(path);
        config = valueSupplier.createBuilder(builder);
        builder.pop();
    }

    public ModConfigSpec.ConfigValue<T> getConfig() {
        return config;
    }

    public void setValue(T value) {
        config.set(value);
    }

    public T getValue() {
        return config.get();
    }

    public interface ConfigValueSupplier<T> {
        ModConfigSpec.ConfigValue<T> createBuilder(ModConfigSpec.Builder builder);
    }
}
