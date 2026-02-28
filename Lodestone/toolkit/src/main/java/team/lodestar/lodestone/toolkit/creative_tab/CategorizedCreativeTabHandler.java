package team.lodestar.lodestone.toolkit.creative_tab;

import com.mojang.datafixers.util.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

import java.util.*;
import java.util.function.*;

public class CategorizedCreativeTabHandler {
    
    public static Optional<CategorizedCreativeTab> getOpenCategorizedTab() {
        var selectedTab = CreativeModeInventoryScreen.selectedTab;
        if (selectedTab instanceof CategorizedCreativeTab categorizedTab) {
            return Optional.of(categorizedTab);
        }
        return Optional.empty();
    }

    public static void modifyTab(CreativeModeInventoryScreen.ItemPickerMenu menu) {
        getOpenCategorizedTab().ifPresent(t -> fillMenu(menu, t));
    }

    public static boolean renderSlot(GuiGraphics guiGraphics, Slot slot) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof CreativeModeInventoryScreen screen) {
            if (!(slot instanceof CreativeModeInventoryScreen.CustomCreativeSlot)) {
                return false;
            }
            var optional = getOpenCategorizedTab();
            if (optional.isEmpty()) {
                return false;
            }
            var categorizedTab = optional.get();
            var item = slot.getItem();
            if (item.isEmpty()) {
                var menu = screen.getMenu();
                int row = menu.getRowIndexForScroll(screen.scrollOffs);
                int column = slot.getContainerSlot() % 9;
                int itemIndex = row * 9 + Mth.floor(slot.getSlotIndex() / 9f) * 9;
                var pose = guiGraphics.pose();
                pose.pushPose();
                pose.translate(0.0F, 0.0F, 100.0F);

                var headers = categorizedTab.getHeaders();
                if (headers.containsKey(itemIndex)) {
                    var header = headers.get(itemIndex);
                    var texture = categorizedTab.getHeaderTexture(header, row, column);
                    if (texture.isPresent()) {
                        var sprite = minecraft.getGuiSprites().getSprite(texture.get());
                        guiGraphics.blit(slot.x - 1, slot.y - 2, 0, 18, 20, sprite);
                    }
                    if (column == 0) {
                        var font = minecraft.font;
                        var title = Component.translatable(header.category().getHeaderLangKey());
                        int x = slot.x + 80 - font.width(title) / 2;
                        int y = slot.y + 1;
                        pose.pushPose();
                        pose.translate(0.0F, 0.0F, 100.0F);
                        guiGraphics.drawString(font, title, x, y, 4210752, false);
                        pose.popPose();
                    }
                } else {
                    var texture = categorizedTab.getEmptySlotTexture(row, column);
                    if (texture.isPresent()) {
                        var sprite = minecraft.getGuiSprites().getSprite(texture.get());
                        guiGraphics.blit(slot.x, slot.y, 0, 16, 16, sprite);
                    }
                }
                pose.popPose();
                return true;
            }
        }
        return false;
    }

    public static boolean disableSlotHighlight(Slot slot) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof CreativeModeInventoryScreen) {
            if (!(slot instanceof CreativeModeInventoryScreen.CustomCreativeSlot)) {
                return false;
            }
            if (getOpenCategorizedTab().isEmpty()) {
                return false;
            }
            return slot.getItem().isEmpty();
        }
        return false;
    }

    public static void fillMenu(CreativeModeInventoryScreen.ItemPickerMenu menu, CategorizedCreativeTab categorizedTab) {
        var categories = categorizedTab.getCategories().values();
        var items = menu.items;
        categorizedTab.getHeaders().clear();
        items.clear();
        for (CreativeTabCategory category : categories) {
            addCategoryHeader(menu, categorizedTab, category);
            for (Either<Supplier<ItemStack>, CreativeTabCategory.Operation> either : category.items()) {
                either.ifLeft(i -> {
                    var item = i.get();
                    if (categorizedTab.isItemVisible(item)) {
                        items.add(item);
                    }
                });
                either.ifRight(e -> clearRow(menu, false));
            }
            clearRow(menu, false);
        }
    }

    public static void addCategoryHeader(CreativeModeInventoryScreen.ItemPickerMenu menu, CategorizedCreativeTab categorizedTab, CreativeTabCategory category) {
        var items = menu.items;
        var index = items.size();
        clearRow(menu, true);
        categorizedTab.getHeaders().put(index, new CreativeTabCategory.CategoryHeader(category));
    }

    public static void clearRow(CreativeModeInventoryScreen.ItemPickerMenu menu, boolean force) {
        var items = menu.items;
        int missing = 9 - items.size() % 9;
        if (force || missing != 9) {
            for (int i = 0; i < missing; i++) {
                items.add(ItemStack.EMPTY);
            }
        }
    }
}
