package team.lodestar.lodestone.systems.worldevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.registry.client.LodestoneWorldEventRendererRegistry;

public class WorldEventType {

    public final ResourceLocation id;
    public final EventInstanceSupplier<?> supplier;
    public final boolean clientSynced;


    /**
     * @param id The id of the event type
     * @param supplier The supplier for the event instance
     * @param clientSynced Should this event exist on the client? It will be automatically synced upon creation of the event in {@link WorldEventInstance#sync(net.minecraft.world.level.Level)}
     */
    public WorldEventType(ResourceLocation id, EventInstanceSupplier<?> supplier, boolean clientSynced) {
        this.id = id;
        this.supplier = supplier;
        this.clientSynced = clientSynced;
    }

    public WorldEventType(ResourceLocation id, EventInstanceSupplier<?> supplier) {
        this(id, supplier, false);
    }

    public boolean isClientSynced() {
        return clientSynced;
    }

    public WorldEventInstance createInstance(CompoundTag tag) {
        return supplier.getInstance().deserializeNBT(tag);
    }

    public static class Builder<T extends WorldEventInstance> {
        private final ResourceLocation id;
        private final EventInstanceSupplier<T> supplier;
        private boolean clientSynced;
        private WorldEventRenderer<T> renderer;

        private Builder(EventInstanceSupplier<T> supplier, ResourceLocation id) {
            this.id = id;
            this.supplier = supplier;
        }

        /**
         * Creates a new world event type.
         *
         * @param supplier The supplier for the event instance.
         * @param id The ID of the event type.
         * @return A new Builder instance for the specified event type.
         */
        public static <T extends WorldEventInstance> Builder<T> of(EventInstanceSupplier<T> supplier, ResourceLocation id) {
            return new Builder<>(supplier, id);
        }

        /**
         * Sets the event to be client-synced and assigns a renderer for the event.
         * The event will be automatically synced upon creation in {@link WorldEventInstance#sync(net.minecraft.world.level.Level)}.
         *
         * @param renderer The renderer for the event. Set to null if you don't want a renderer for this world event.
         * @return The builder instance with the clientSynced flag set and the renderer assigned.
         */
        public Builder<T> clientSynced(WorldEventRenderer<T> renderer) {
            this.clientSynced = true;
            this.renderer = renderer;
            return this;
        }

        /**
         * Sets the event to be client-synced without assigning a renderer.
         * The event will be automatically synced upon creation in {@link WorldEventInstance#sync(net.minecraft.world.level.Level)}.
         *
         * <p>If you want to use a renderer, use {@link #clientSynced(WorldEventRenderer)}.</p>
         *
         * @return The builder instance with the clientSynced flag set and no renderer assigned.
         */
        public Builder<T> clientSynced() {
            return clientSynced(null);
        }

        /**
         * Builds the WorldEventType with the specified parameters.
         * Registers the renderer if it is set.
         *
         * @return The built WorldEventType.
         */
        public WorldEventType build() {
            WorldEventType type = new WorldEventType(this.id, this.supplier, this.clientSynced);
            LodestoneWorldEventRendererRegistry.registerRenderer(type, this.renderer);
            return type;
        }
    }

    public interface EventInstanceSupplier<T extends WorldEventInstance> {
        T getInstance();
    }
}