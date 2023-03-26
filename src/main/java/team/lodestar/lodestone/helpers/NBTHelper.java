package team.lodestar.lodestone.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.*;

public class NBTHelper {

    /**
     * Saves a block position to nbt.
     */
    public static void saveBlockPos(CompoundTag compoundNBT, BlockPos pos) {
        compoundNBT.putInt("X", pos.getX());
        compoundNBT.putInt("Y", pos.getY());
        compoundNBT.putInt("Z", pos.getZ());
    }

    /**
     * Saves a block position to nbt with extra text to differentiate it.
     */
    public static void saveBlockPos(CompoundTag compoundNBT, BlockPos pos, String extra) {
        compoundNBT.putInt(extra + "_X", pos.getX());
        compoundNBT.putInt(extra + "_Y", pos.getY());
        compoundNBT.putInt(extra + "_Z", pos.getZ());
    }

    /**
     * Loads a block position from nbt.
     */
    public static BlockPos loadBlockPos(CompoundTag tag) {
        return tag.contains("X") ? new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z")) : null;
    }

    /**
     * Loads a block position from nbt with extra text as input.
     */
    public static BlockPos loadBlockPos(CompoundTag tag, String extra) {
        return tag.contains(extra + "_X") ? new BlockPos(tag.getInt(extra + "_X"), tag.getInt(extra + "_Y"), tag.getInt(extra + "_Z")) : null;
    }

    public static CompoundTag filterTag(CompoundTag orig, TagFilter filter) {
        if (filter.filters.isEmpty()) {
            return orig;
        }
        CompoundTag copy = orig.copy();
        return removeTags(copy, filter);
    }

    public static CompoundTag removeTags(CompoundTag tag, TagFilter filter) {
        CompoundTag newTag = new CompoundTag();
        for (String i : filter.filters) {
            if (tag.contains(i)) {
                if (filter.isWhitelist) {
                    newTag.put(i, newTag);
                } else {
                    tag.remove(i);
                }
            } else {
                for (String key : tag.getAllKeys()) {
                    Tag value = tag.get(key);
                    if (value instanceof CompoundTag ctag) {
                        removeTags(ctag, filter);
                    }
                }
            }
        }
        if (filter.isWhitelist) {
            tag = newTag;
        }
        return tag;
    }

    public static TagFilter create(String... filters) {
        return new TagFilter(filters);
    }

    public static class TagFilter {
        public final ArrayList<String> filters = new ArrayList<>();
        public boolean isWhitelist;

        public TagFilter(String... filters) {
            this.filters.addAll(List.of(filters));
        }

        public TagFilter setWhitelist() {
            this.isWhitelist = true;
            return this;
        }
    }
}