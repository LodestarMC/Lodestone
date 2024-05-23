package team.lodestar.lodestone.systems.capability;

import io.github.fabricators_of_create.porting_lib.core.util.INBTSerializable;
import net.minecraft.nbt.CompoundTag;

/**
 * A simple capability, that's it. It's easier to remember to import LodestoneCapability rather than {@link INBTSerializable<CompoundTag>}
 */
public interface LodestoneCapability extends INBTSerializable<CompoundTag> {

}