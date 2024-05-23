package team.lodestar.lodestone.systems.textureloader;
/*TODO
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.client.textures.ForgeTextureMetadata;
import net.minecraftforge.client.textures.ITextureAtlasSpriteLoader;
import org.jetbrains.annotations.NotNull;

public class LodestoneTextureAtlasSpriteLoader implements ITextureAtlasSpriteLoader {

    private final LodestoneTextureLoader.TextureModifier modifier;

    public LodestoneTextureAtlasSpriteLoader(@NotNull LodestoneTextureLoader.TextureModifier modifier) {
        this.modifier = modifier;
    }

    @Override
    public SpriteContents loadContents(ResourceLocation name, Resource resource, FrameSize frameSize, NativeImage
            image, AnimationMetadataSection animationMeta, ForgeTextureMetadata forgeMeta) {
        return new SpriteContents(name, frameSize, modifier.modifyTexture(image), animationMeta, forgeMeta);
    }

    @Override
    @NotNull
    public TextureAtlasSprite makeSprite(ResourceLocation atlasName, SpriteContents contents, int atlasWidth, int atlasHeight, int spriteX, int spriteY, int mipmapLevel) {
        return new TextureAtlasSprite(atlasName, contents, atlasWidth, atlasHeight, spriteX, spriteY);
    }
}

 */