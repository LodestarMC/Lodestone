package com.sammy.ortus.systems.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * A simple capability, that's it. It's easier to remember to import OrtusCapability rather than {@link INBTSerializable<CompoundTag>}
 */
public interface OrtusCapability extends INBTSerializable<CompoundTag> {
}
