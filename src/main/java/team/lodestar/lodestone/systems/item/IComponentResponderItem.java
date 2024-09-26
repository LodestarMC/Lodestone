package team.lodestar.lodestone.systems.item;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Optional;

public interface IComponentResponderItem {
    default void readComponent(int stackQuantity, DataComponentMap.Builder mutableMap, ComponentGetter originalSupplier) {}
    default void setComponent(int stackQuantity, DataComponentMap.Builder mutableMap, ModifyComponentOperation<?> diff, ComponentGetter originalSupplier) {}
    default void removeComponent(int stackQuantity, DataComponentMap.Builder mutableMap, RemoveComponentOperation<?> diff, ComponentGetter originalSupplier) {}

    interface ComponentGetter {
        <T> Optional<T> get(DataComponentType<T> type);

        default <T> Optional<T> get(DeferredHolder<DataComponentType<?>, DataComponentType<T>> type) {
            return get(type.get());
        }

        static ComponentGetter create(DataComponentMap map) {
            return new IComponentResponderItem.ComponentGetter() {
                public <T> Optional<T> get(DataComponentType<T> type) {
                    return Optional.ofNullable(map.get(type));
                }
            };
        }
    }

    class ModifyComponentOperation<T> extends ComponentOperation<T> {
        public final T candidate;

        public ModifyComponentOperation(T oldValue, T candidate, DataComponentType<T> type) {
            super(oldValue, type);
            this.candidate = candidate;
        }

        public <T> Optional<ModifyComponentOperation<T>> maybeOfType(DataComponentType<T> type) {
            return type.equals(this.type) ? Optional.of((ModifyComponentOperation<T>)this) : Optional.empty();
        }
    }

    class RemoveComponentOperation<T> extends ComponentOperation<T> {

        public RemoveComponentOperation(T oldValue, DataComponentType<T> type) { super(oldValue, type); }

        public <T> Optional<RemoveComponentOperation<T>> maybeOfType(DataComponentType<T> type) {
            return type.equals(this.type) ? Optional.of((RemoveComponentOperation<T>)this) : Optional.empty();
        }
    }

    class ComponentOperation<T> {
        public final T oldValue;
        public final DataComponentType<T> type;
        public boolean isCancelled = false;

        public ComponentOperation(T oldValue, DataComponentType<T> type) {
            this.oldValue = oldValue;
            this.type = type;
        }

        public void cancel() { isCancelled = true; }
    }
}
