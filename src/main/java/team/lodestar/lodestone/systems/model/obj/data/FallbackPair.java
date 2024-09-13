package team.lodestar.lodestone.systems.model.obj.data;

public class FallbackPair<T> {
    T defaultValue;
    T overrideValue;

    public FallbackPair() {
        this(null, null);
    }

    public FallbackPair(T defaultValue) {
        this(defaultValue, null);
    }

    public FallbackPair(T defaultValue, T overrideValue) {
        this.defaultValue = defaultValue;
        this.overrideValue = overrideValue;
    }

    public FallbackPair<T> of(T defaultValue, T overrideValue) {
        return new FallbackPair<>(defaultValue, overrideValue);
    }

    public static <T> FallbackPair<T> ofDefault(T defaultValue) {
        return new FallbackPair<>(defaultValue, null);
    }

    public void setDefault(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setOverride(T overrideValue) {
        this.overrideValue = overrideValue;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public T getOverrideValue() {
        return this.overrideValue;
    }

    public T get() {
        return this.hasOverride() ? this.overrideValue : this.defaultValue;
    }

    public T getOrDefault(T defaultValue) {
        return this.get() != null ? this.get() : defaultValue;
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
        FallbackPair<?> entry = (FallbackPair<?>) obj;
        return this.defaultValue.equals(entry.defaultValue) && this.overrideValue.equals(entry.overrideValue);
    }
}