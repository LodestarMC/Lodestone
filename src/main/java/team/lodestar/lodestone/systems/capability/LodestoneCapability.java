package team.lodestar.lodestone.systems.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * A simple capability, that's it. It's easier to remember to import LodestoneCapability rather than {@link INBTSerializable<CompoundTag>}
 */
public interface LodestoneCapability extends INBTSerializable<CompoundTag> {

}