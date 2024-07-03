package team.lodestar.lodestone.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class LodestoneMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    public static boolean isSodium059Installed() {
        return FabricLoader.getInstance().getModContainer("sodium").map(mod -> {
            try {
                return mod.getMetadata().getVersion().compareTo(Version.parse("0.5.9")) >= 0;
            } catch (VersionParsingException e) {
                throw new RuntimeException(e);
            }
        }).orElse(false);
    }

    public static boolean isSodiumLess059Installed() {
        return FabricLoader.getInstance().getModContainer("sodium").map(mod -> {
            try {
                return mod.getMetadata().getVersion().compareTo(Version.parse("0.5.9")) < 0;
            } catch (VersionParsingException e) {
                throw new RuntimeException(e);
            }
        }).orElse(false);
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean sodiumLoaded = FabricLoader.getInstance().isModLoaded("sodium");
        if (mixinClassName.startsWith("team.lodestar.lodestone.mixin.client.integration.sodium510")) {
            return isSodium059Installed();
        }
        if (mixinClassName.startsWith("team.lodestar.lodestone.mixin.client.integration.sodium58")) {
            return isSodiumLess059Installed();
        }
        if (mixinClassName.startsWith("team.lodestar.lodestone.mixin.client.integration.iris")) {
            return FabricLoader.getInstance().isModLoaded("iris");
        }

        if (mixinClassName.startsWith("team.lodestar.lodestone.mixin.client.integration.notsodium")) {
            return !sodiumLoaded;
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
