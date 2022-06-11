package com.sammy.ortus.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

public class NBTHelper {

    public static ClientboundLevelChunkPacketData.BlockEntityInfo createFriendlyBlockEntityPacket(BlockEntity blockEntity) {
        CompoundTag compoundtag = blockEntity.getUpdateTag();
        BlockPos blockpos = blockEntity.getBlockPos();
        int i = SectionPos.sectionRelative(blockpos.getX()) << 4 | SectionPos.sectionRelative(blockpos.getZ());
        return new ClientboundLevelChunkPacketData.BlockEntityInfo(i, blockpos.getY(), blockEntity.getType(), compoundtag);
    }

    public static CompoundTag filterTag(CompoundTag orig, TagFilter filter) {
        if (filter.filters.isEmpty()) {
            return orig;
        }
        CompoundTag copy = orig.copy();
        removeTags(copy, filter);
        return copy;
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