package team.lodestar.lodestone.modules.core.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Function;

public class LodestoneConfigHolder<T extends LodestoneConfig> {

    protected final ModConfig.Type configType;
    protected final T instance;
    protected final ModConfigSpec spec;

    public LodestoneConfigHolder(ModConfig.Type configType, Function<ModConfigSpec.Builder, T> supplier) {
        var build = new ModConfigSpec.Builder().configure(supplier);
        this.configType = configType;
        this.spec = build.getRight();
        this.instance = build.getLeft();
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
