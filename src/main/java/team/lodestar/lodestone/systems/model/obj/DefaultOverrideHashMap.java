package team.lodestar.lodestone.systems.model.obj;

import java.util.HashMap;
import java.util.Map;

public class DefaultOverrideHashMap<K,V> {
    private final Map<K, Entry<V>> map;

    public DefaultOverrideHashMap(int initialCapacity, float loadFactor) {
        this.map = new HashMap<>(initialCapacity, loadFactor);
    }

    public DefaultOverrideHashMap(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity, 0.75f);
    }

    public DefaultOverrideHashMap() {
        this.map = new HashMap<>();
    }

    public void putOverride(K key, V value) {
        this.map.computeIfAbsent(key, k -> new Entry<>()).setOverride(value);
    }

    public void putDefault(K key, V value) {
        this.map.computeIfAbsent(key, k -> new Entry<>()).setDefault(value);
    }

    public void clearOverride() {
        this.map.values().forEach(Entry::clearOverride);
    }

    public void clearDefault() {
        this.map.values().forEach(Entry::clearDefault);
    }

    public V getDefault(K key) {
        return this.map.get(key).getDefaultValue();
    }

    public V getOverride(K key) {
        return this.map.get(key).getOverrideValue();
    }

    public V getOrDefault(K key) {
        return this.map.get(key).getOrDefault();
    }

    public boolean containsKeyDefault(K key) {
        return this.map.containsKey(key) && this.map.get(key).hasDefault();
    }

    public boolean containsKeyOverride(K key) {
        return this.map.containsKey(key) && this.map.get(key).hasOverride();
    }

    public boolean containsKeyOrDefault(K key) {
        return this.map.containsKey(key) && this.map.get(key).hasDefault();
    }

    public V getOrDefault(K key, V defaultValue) {
        return this.map.get(key) != null ? this.map.get(key).getOrDefault() : defaultValue;
    }

    public void removeDefault(K key) {
        this.map.get(key).clearDefault();
    }

    public void removeOverride(K key) {
        this.map.get(key).clearOverride();
    }

    public void removeFromBoth(K key) {
        this.map.get(key).clear();
    }

    public void putAllDefault(DefaultOverrideHashMap<K, V> map) {
        map.map.forEach((k, v) -> this.map.computeIfAbsent(k, key -> new Entry<>()).setDefault(v.getDefaultValue()));
    }

    public void putAllOverride(DefaultOverrideHashMap<K, V> map) {
        map.map.forEach((k, v) -> this.map.computeIfAbsent(k, key -> new Entry<>()).setOverride(v.getOverrideValue()));
    }

    public static class Entry<V> {
        private V defaultValue;
        private V overrideValue;

        public Entry() {

        }

        public void setDefault(V defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setOverride(V overrideValue) {
            this.overrideValue = overrideValue;
        }

        public V getDefaultValue() {
            return this.defaultValue;
        }

        public V getOverrideValue() {
            return this.overrideValue;
        }

        public V getOrDefault() {
            return this.overrideValue != null ? this.overrideValue : this.defaultValue;
        }

        public void clearDefault() {
            this.defaultValue = null;
        }

        public void clearOverride() {
            this.overrideValue = null;
        }

        public void clear() {
            this.clearDefault();
            this.clearOverride();
        }

        public boolean hasDefault() {
            return this.defaultValue != null;
        }

        public boolean hasOverride() {
            return this.overrideValue != null;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || this.getClass() != obj.getClass()) return false;
            Entry<?> entry = (Entry<?>) obj;
            return this.defaultValue.equals(entry.defaultValue) && this.overrideValue.equals(entry.overrideValue);
        }
    }
}
