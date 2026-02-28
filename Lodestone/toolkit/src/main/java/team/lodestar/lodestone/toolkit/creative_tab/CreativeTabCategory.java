package team.lodestar.lodestone.toolkit.creative_tab;

import com.mojang.datafixers.util.*;
import net.minecraft.world.item.*;

import java.util.*;
import java.util.function.*;

public record CreativeTabCategory(String mod, String id,
                                  List<Either<Supplier<ItemStack>, Operation>> items) {
    public String getHeaderLangKey() {
        return mod + ".itemGroup.header." + id;
    }

    public enum Operation {
        NEXT_LINE
    }

    public record CategoryHeader(CreativeTabCategory category) {

    }
}
