package com.sammy.ortus.helpers;

import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public class NBTHelper {

    @SuppressWarnings("ConstantConditions")
    public static CompoundTag filterTag(CompoundTag orig, String... filters) {
        if (filters.length == 0) {
            return orig;
        }
        CompoundTag newTag = new CompoundTag();
        for (String key : orig.getAllKeys()) {
            for (String filter : filters) {
                if (orig.contains(filter)) {
                    newTag.put(key, orig.get(key));
                }
            }
        }
        return newTag;
    }
}