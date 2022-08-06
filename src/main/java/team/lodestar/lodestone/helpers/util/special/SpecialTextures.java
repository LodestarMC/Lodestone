package team.lodestar.lodestone.helpers.util.special;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;

public enum SpecialTextures {
    BLANK("blank.png"),
    BLOCK("block.png"),
    CHECKERBOARD("checkerboard.png"),
    THIN_CHECKERBOARD("thin_checkerboard.png"),
    CUTOUT_CHEKERBOARD("cutout_checkerboard.png"),
    HIGHLIGHT_CHECKERBOARD("highlight_checkerboard.png"),
    SELECTION("selection.png"),

    ;

    public static final String PATH_TO_ASSET = "textures/special/";
    private ResourceLocation location;

    SpecialTextures(String name) {
        location = new ResourceLocation(PATH_TO_ASSET + name);
    }

    public ResourceLocation getLocation() {
        return location;
    }
}
