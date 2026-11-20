package team.lodestar.lodestone.modules.core.config;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class ConfigValueHolder<T> {

    private static final Splitter DOT_SPLITTER = Splitter.on(".");

    private final ArrayList<String> path;
    private final ConfigValueSupplier<T> valueSupplier;
    private ModConfigSpec.ConfigValue<T> config;

    public ConfigValueHolder(String path, ConfigValueSupplier<T> valueSupplier) {
        this.path = Lists.newArrayList(DOT_SPLITTER.split(path));
        this.valueSupplier = valueSupplier;
    }

    public void bake(ModConfigSpec.Builder builder) {
        builder.push(path);
        config = valueSupplier.createBuilder(builder);
        builder.pop(path.size());
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
