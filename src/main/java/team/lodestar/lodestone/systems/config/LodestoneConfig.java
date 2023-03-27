package team.lodestar.lodestone.systems.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A config system allowing for static initialization of config values.
 * Config values are stored into hashmaps, which are linked to a ConfigGroup key
 */
@SuppressWarnings("rawtypes")
public class LodestoneConfig {

    public static final ConcurrentHashMap<ConfigGroup, HashMap<String, ArrayList<ConfigValueHolder>>> VALUE_HOLDERS = new ConcurrentHashMap<>();

    /**
     * @param configGroup - a unique identifier for your config to be used as key to access your config values. For example: "lodestone:client".
     * @param builder    - a forge config builder instance.
     */
    @SuppressWarnings("rawtypes")
    public LodestoneConfig(ConfigGroup configGroup, ForgeConfigSpec.Builder builder) {
        HashMap<String, ArrayList<ConfigValueHolder>> map = VALUE_HOLDERS.get(configGroup);
        for (Map.Entry<String, ArrayList<ConfigValueHolder>> valueHolder : map.entrySet()) {
            List<String> path = List.of(valueHolder.getKey().split("/"));
            builder.push(path);
            ArrayList<ConfigValueHolder> h = valueHolder.getValue();
            for (ConfigValueHolder configValueHolder : h) {
                configValueHolder.setConfig(builder);
            }
            builder.pop(path.size());
        }
    }

    public static HashMap<String, ArrayList<ConfigValueHolder>> getConfigEntries(ConfigGroup configGroup) {
        return LodestoneConfig.VALUE_HOLDERS.computeIfAbsent(configGroup, c -> new HashMap<>());
    }

    public interface BuilderSupplier<T> {
        ForgeConfigSpec.ConfigValue<T> createBuilder(ForgeConfigSpec.Builder builder);
    }
}