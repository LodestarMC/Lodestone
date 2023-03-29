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

    public static CompoundTag filterTags(CompoundTag tag, String... filters) {
        return filterTags(tag, new HashSet<>(Arrays.asList(filters)));
    }

    /**
     * Filters out any nbt from a CompoundTag with a key that doesn't match any of the filters.
     * Nested CompoundTags are also filtered.
     * If you want to filter a nested CompoundTag, you'll need to pass a "path" towards the nbt you want to keep.
     * An example of this would be passing "fireEffect/duration".
     * The CompoundTag under the name of "fireEffect" would be kept, but everything inside it except "duration" would be removed.
     */
    public static CompoundTag filterTags(CompoundTag tag, Set<String> filters) {
        CompoundTag newTag = new CompoundTag();

        Set<String> subFilters = new HashSet<>();
        //We look through all the filters, and make sure that each filter also has its path as a separate filter
        //In case of "fireEffect/duration", this for loop would just add "fireEffect" as its own filter to our list of filters.
        filters.forEach(s -> {
            while (s.contains("/")) {
                int index = s.lastIndexOf("/");
                String path = s.substring(0, index);
                subFilters.add(path);
                s = path;
            }
        });
        filters.addAll(subFilters);

        //We look through the NBT and copy any Tags with a key that "filters" contains.
        for (String filter : filters) {
            Tag entry = tag.get(filter);
            if (entry != null) {
                //If the entry we copied over is a CompoundTag, we also apply our filters to the CompoundTag.
                //If that CompoundTag also contains a CompoundTag, it will also be filtered.
                if (entry instanceof CompoundTag compoundEntry) {
                    Set<String> updatedFilters = filters.stream().filter(s -> s.contains(filter+"/")).map(s -> s.substring(s.indexOf("/")+1)).collect(Collectors.toSet());
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