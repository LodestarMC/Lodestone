package team.lodestar.lodestone.toolkit.creative_tab;

import com.mojang.datafixers.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;
import java.util.function.*;

@SuppressWarnings("UnusedReturnValue")
public class CreativeTabCategoryBuilder {

    protected final CategorizedCreativeTab categorizedTab;
    protected final String mod;
    protected final String id;
    protected final ArrayList<Either<Supplier<ItemStack>, CreativeTabCategory.Operation>> items = new ArrayList<>();

    public CreativeTabCategoryBuilder(CategorizedCreativeTab categorizedTab, String mod, String id) {
        this.categorizedTab = categorizedTab;
        this.mod = mod;
        this.id = id;
    }

    public final CreativeTabCategoryBuilder addItems(Consumer<CreativeTabCategoryBuilder> itemAdder) {
        itemAdder.accept(this);
        return this;
    }

    public CreativeTabCategoryBuilder addItems(Item... items) {
        for (Item item : items) {
            addItem(item);
        }
        return this;
    }

    @SafeVarargs
    public final <T extends Item> CreativeTabCategoryBuilder addItems(Supplier<T>... items) {
        for (Supplier<T> item : items) {
            addItem(item);
        }
        return this;
    }

    @SafeVarargs
    public final CreativeTabCategoryBuilder addItemStacks(Supplier<ItemStack>... items) {
        for (Supplier<ItemStack> stack : items) {
            addItemStack(stack);
        }
        return this;
    }

    @SafeVarargs
    public final <T extends Block> CreativeTabCategoryBuilder addBlocks(Supplier<T>... blocks) {
        for (Supplier<T> block : blocks) {
            addBlockItem(block);
        }
        return this;
    }

    public final CreativeTabCategoryBuilder addItem(Item item) {
        return addItemStack(item.getDefaultInstance());
    }

    public final CreativeTabCategoryBuilder addItemStack(ItemStack item) {
        items.add(Either.left(() -> item));
        return this;
    }

    public final CreativeTabCategoryBuilder addBlockItem(Block block) {
        items.add(Either.left(() -> block.asItem().getDefaultInstance()));
        return this;
    }

    public final <T extends Item> CreativeTabCategoryBuilder addItem(Supplier<T> item) {
        return addItemStack(() -> item.get().getDefaultInstance());
    }

    public final CreativeTabCategoryBuilder addItemStack(Supplier<ItemStack> item) {
        items.add(Either.left(item));
        return this;
    }

    public final <T extends Block> CreativeTabCategoryBuilder addBlockItem(Supplier<T> block) {
        items.add(Either.left(() -> block.get().asItem().getDefaultInstance()));
        return this;
    }

    public CreativeTabCategoryBuilder nextLine() {
        items.add(Either.right(CreativeTabCategory.Operation.NEXT_LINE));
        return this;
    }

    public void bake() {
        categorizedTab.getCategories().put(id, new CreativeTabCategory(mod, id, items));
    }
}