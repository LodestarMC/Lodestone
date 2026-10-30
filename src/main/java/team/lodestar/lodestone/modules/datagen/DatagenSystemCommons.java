package team.lodestar.lodestone.modules.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

public class DatagenSystemCommons {

    /**
     * Stores the textures used by the most recently generated block. Used for more easily generating item models based off of blocks which have weirdly specific custom item models, like walls.
     */
    protected static final HashMap<String, ResourceLocation> BLOCK_MODEL_TEXTURE_REFERENCE = new HashMap<>();
    protected static final Set<ResourceLocation> IMMUTABLE_TEXTURES = new HashSet<>();

    public static Block CURRENT_BLOCK;

    public static PathModifier MODEL_PARENT = new PathModifier();

    public static PathModifier BLOCK_TEXTURE = new PathModifier();
    public static PathModifier BLOCK_MODEL = new PathModifier();

    public static PathModifier ITEM_TEXTURE = new PathModifier();
    public static PathModifier ITEM_MODEL = new PathModifier();

    public static ResourceLocation getBlockTextureFromBlockModel(String key) {
        if (!BLOCK_MODEL_TEXTURE_REFERENCE.containsKey(key)) {
            throw new IllegalArgumentException("Cannot find block texture associated with key: " + key);
        }
        var texture = BLOCK_MODEL_TEXTURE_REFERENCE.get(key);
        escapeTextureFolderHierarchy(texture);
        return texture;
    }

    public static void writeBlockTextureFromBlockModel(String key, ResourceLocation texture) {
        BLOCK_MODEL_TEXTURE_REFERENCE.put(key, texture);
    }

    public static void clearCachedBlockTextures() {
        BLOCK_MODEL_TEXTURE_REFERENCE.clear();
    }

    public static ResourceLocation escapeTextureFolderHierarchy(ResourceLocation texture) {
        IMMUTABLE_TEXTURES.add(texture);
        return texture;
    }

    public static ResourceLocation modifyTexturePath(ResourceLocation texture) {
        if (IMMUTABLE_TEXTURES.contains(texture)) {
            return texture;
        }
        if (texture.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            return texture;
        }
        var item = ITEM_TEXTURE.apply(texture, "item/");
        var block = BLOCK_TEXTURE.apply(texture, "block/");
        return item.orElseGet(() -> block.orElse(texture));
    }

    public static ResourceLocation modifyModelParentPath(ResourceLocation modelPath) {
        if (modelPath.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            return modelPath;
        }
        var item = MODEL_PARENT.apply(modelPath, "item/");
        var block = MODEL_PARENT.apply(modelPath, "block/");
        return item.orElseGet(() -> block.orElse(modelPath));
    }

    public static ResourceLocation modifyModelPath(ResourceLocation modelPath) {
        if (modelPath.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) {
            return modelPath;
        }
        var item = ITEM_MODEL.apply(modelPath, "item/");
        var block = BLOCK_MODEL.apply(modelPath, "block/");
        return item.orElseGet(() -> block.orElse(modelPath));
    }

    public static class PathModifier {
        private String folder = "";
        private UnaryOperator<String> oneTimeModifier;

        public void setFolder(String folder) {
            if (!folder.isEmpty() && !folder.endsWith("/")) {
                folder += "/";
            }
            this.folder = folder;
        }

        public void clearFolder() {
            this.folder = "";
        }

        public String getFolder() {
            return folder;
        }

        public void modify(UnaryOperator<String> oneTimeModifier) {
            this.oneTimeModifier = oneTimeModifier;
        }

        public void clearModifier() {
            this.oneTimeModifier = null;
        }

        public Optional<ResourceLocation> apply(ResourceLocation location, String regex) {
            if (location.getPath().contains(regex)) {
                return Optional.of(location.withPath(p -> modifyPath(p, regex)));
            }
            return Optional.empty();
        }

        private String modifyPath(String path, String regex) {
            var replacement = path.replace(regex, regex + folder);
            if (oneTimeModifier != null) {
                replacement = oneTimeModifier.apply(replacement);
                oneTimeModifier = null;
            }
            return replacement;
        }
    }
}