package team.lodestar.lodestone.systems.capability;

import io.github.fabricators_of_create.porting_lib.core.util.INBTSerializable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

/**
 * A basic provider for your lodestone capability
 */
public class LodestoneCapabilityProvider<C extends INBTSerializable<CompoundTag>> implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final C instance;
    private final LazyOptional<C> capOptional;

    private final Capability<C> capability;

    public LodestoneCapabilityProvider(Capability<C> capability, NonNullSupplier<C> capInstance) {
        this.capability = capability;
        this.instance = capInstance.get();
        this.capOptional = LazyOptional.of(capInstance);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return capability.orEmpty(cap, capOptional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.instance.deserializeNBT(nbt);
    }
}