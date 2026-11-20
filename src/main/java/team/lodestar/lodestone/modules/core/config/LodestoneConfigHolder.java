package team.lodestar.lodestone.modules.core.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Supplier;

public class LodestoneConfigHolder<T extends LodestoneConfig> {

    protected final ModConfig.Type configType;
    protected final T instance;
    protected final ModConfigSpec spec;

    public LodestoneConfigHolder(ModConfig.Type configType, Supplier<T> supplier) {
        var builder = new ModConfigSpec.Builder();
        this.configType = configType;
        this.instance = supplier.get();
        instance.createValues(builder);

        this.spec = builder.build();
    }

    public void register(ModContainer modContainer) {
        modContainer.registerConfig(configType, spec);
    }

    public T getConfig() {
        return instance;
    }

    public ModConfigSpec getSpec() {
        return spec;
    }
}
