package team.lodestar.lodestone.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.*;
import java.util.stream.Collectors;

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

    public static CompoundTag filterTag(CompoundTag tag, String... filters) {
        return filterTag(tag, List.of(filters));
    }

    public static CompoundTag filterTag(CompoundTag tag, Collection<String> filters) {
        return filterTags(tag.copy(), filters);
    }

    /**
     * Filters out any nbt from a CompoundTag with a key that doesn't match any of the filters.
     * Nested CompoundTags are also filtered.
     * If you want to filter a nested CompoundTag, you'll need to pass a "path" towards the nbt you want to keep.
     * An example of this would be passing "fireEffect" and "fireEffect/duration".
     * The CompoundTag under the name of "fireEffect" would be kept, but everything except "duration" inside it would be removed.
     */
    public static CompoundTag filterTags(CompoundTag tag, Collection<String> filters) {
        CompoundTag newTag = new CompoundTag();
        //We look through the NBT and copy any Tags with a key that "filters" contains.
        for (String filter : filters) {
            Tag entry = tag.get(filter);
            if (entry != null) {
                //If the entry we copied over is a CompoundTag, we also apply our filters to the CompoundTag.
                //If that CompoundTag also contains a CompoundTag, it will also be filtered.
                if (entry instanceof CompoundTag compoundEntry) {
                    Collection<String> updatedFilters = filters.stream().filter(s -> s.contains(filter+"/")).map(s -> s.substring(s.indexOf("/")+1)).collect(Collectors.toList());
                    if (!updatedFilters.isEmpty()) {
                        entry = filterTags(compoundEntry, updatedFilters);
                    }
                }
                newTag.put(filter, entry);
            }
        }
        return newTag;
    }
}