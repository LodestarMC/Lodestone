package team.lodestar.lodestone.systems.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * A simple capability, that's it. It's easier to remember to import LodestoneCapability rather than {@link INBTSerializable<CompoundTag>}
 */
public interface LodestoneCapability extends INBTSerializable<CompoundTag> {

}