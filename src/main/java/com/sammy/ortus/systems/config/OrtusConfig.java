package com.sammy.ortus.systems.config;

import com.mojang.datafixers.util.Pair;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.*;

/**
 * A config system allowing for static initialization of config values.
 * Value holders are stored in a hashmap, with the key being a pair represented by a path towards your config value
 */
public class OrtusConfig {

    @SuppressWarnings("rawtypes")
    public static final HashMap<Pair<OrtusConfig, ConfigPath>, ArrayList<ConfigValueHolder>> VALUE_HOLDERS = new HashMap<>();

    /**
     * @param builder - a forge config builder instance.
     */
    @SuppressWarnings("rawtypes")
    public OrtusConfig(ForgeConfigSpec.Builder builder) {
        Iterator<Map.Entry<Pair<OrtusConfig, ConfigPath>, ArrayList<ConfigValueHolder>>> iterator = VALUE_HOLDERS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Pair<OrtusConfig, ConfigPath>, ArrayList<ConfigValueHolder>> next = iterator.next();
            Pair<OrtusConfig, ConfigPath> s = next.getKey();
            ArrayList<ConfigValueHolder> h = next.getValue();
            if (s.getFirst().equals(this)) {
                builder.push(List.of(s.getSecond().strings));
                for (ConfigValueHolder configValueHolder : h) {
                    configValueHolder.setConfig(builder);
                }
                h.forEach(v -> v.config = v.valueSupplier.createBuilder(builder));
                builder.pop(s.getSecond().strings.length);
                iterator.remove();
            }
        }
    }

    public static class ConfigValueHolder<T> {
        private final BuilderSupplier<T> valueSupplier;
        private ForgeConfigSpec.ConfigValue<T> config;

        /**
         * @param config        - The config instance we are registering to.
         * @param path          - Path towards your value separated with "/". The first string from a split of your path will be removed and added to the configType.
         * @param valueSupplier - Supplier to your config value. {@link ConfigValueHolder#config} will be set to {@link ConfigValueHolder#valueSupplier#getConfigValue()} when config is initialized.
         */
        public ConfigValueHolder(OrtusConfig config, String path, BuilderSupplier<T> valueSupplier) {
            this.valueSupplier = valueSupplier;
            ArrayList<String> entirePath = new ArrayList<>(List.of(path.split("/")));
            VALUE_HOLDERS.computeIfAbsent(Pair.of(config, new ConfigPath(entirePath.toArray(new String[]{}))), (p) -> new ArrayList<>()).add(this);
        }

        public void setConfig(ForgeConfigSpec.Builder builder) {
            config = valueSupplier.createBuilder(builder);
        }

        public ForgeConfigSpec.ConfigValue<T> getConfig() {
            return config;
        }

        public void setConfigValue(T t) {
            config.set(t);
        }

        public T getConfigValue() {
            return config.get();
        }
    }

    public interface BuilderSupplier<T> {
        ForgeConfigSpec.ConfigValue<T> createBuilder(ForgeConfigSpec.Builder builder);
    }

    public record ConfigPath(String... strings) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConfigPath otherPath = (ConfigPath) o;
            return Arrays.equals(strings, otherPath.strings);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(strings);
        }
    }
}