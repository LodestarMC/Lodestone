package com.sammy.ortus.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

public class NBTHelper {

    public static CompoundTag filterTag(CompoundTag orig, TagFilter filter) {
        if (filter.isEmpty()) {
            return orig;
        }
        CompoundTag copy = orig.copy();
        removeTags(copy, filter);
        return copy;
    }

    public static void removeTags(CompoundTag tag, TagFilter filter) {
        Iterator<Map.Entry<String, Tag>> iterator = tag.tags.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Tag> next = iterator.next();
            String key = next.getKey();

            if (filter.excluded.stream().anyMatch(p -> p.equals(key)) || filter.permitted.stream().noneMatch(p -> p.equals(key))) {
                iterator.remove();
            } else if (next.getValue() instanceof CompoundTag compoundTag) {
                removeTags(compoundTag, filter);
            }
        }
    }

    public static TagFilter create() {
        return new TagFilter();
    }

    public static class TagFilter {
        public String root;
        public ArrayList<String> permitted = new ArrayList<>();
        public ArrayList<String> excluded = new ArrayList<>();

        public TagFilter() {

        }

        public TagFilter setRoot(String root) {
            this.root = root;
            return this;
        }

        public TagFilter permit(String... permitted) {
            this.permitted.addAll(List.of(permitted));
            return this;
        }

        public TagFilter exclude(String... excluded) {
            this.excluded.addAll(List.of(excluded));
            return this;
        }

        public boolean isEmpty() {
            return permitted.isEmpty() && excluded.isEmpty();
        }
    }
}