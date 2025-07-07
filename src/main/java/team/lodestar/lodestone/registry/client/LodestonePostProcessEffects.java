package team.lodestar.lodestone.registry.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;
import team.lodestar.lodestone.systems.postprocess.effects.*;

import java.util.function.Supplier;

@EventBusSubscriber(modid = LodestoneLib.LODESTONE, value = Dist.CLIENT)
public class LodestonePostProcessEffects {
    public static final BloomPostProcessor BLOOM = register(BloomPostProcessor::new);
    public static final PhysicallyBasedBloomPostProcessor PB_BLOOM = register(PhysicallyBasedBloomPostProcessor::new);

    private static <T extends PostProcessor> T register(Supplier<T> supplier) {
        return Minecraft.getInstance() == null ? null : supplier.get();
    }

    @SubscribeEvent
    public static void setupPostProcessEffects(FMLClientSetupEvent event) {
        PostProcessHandler.addInstance(BLOOM);
        PostProcessHandler.addInstance(PB_BLOOM);
    }

    /**
     * Enables bloom for your mod.
     */
    public static void enableBloom() {
        BLOOM.setActive(true);
    }

    /**
     * Permanently disables bloom and prevents other mods from enabling it.
     * Use this if your mod is incompatible with bloom.
     */
    public static void forceDisableBloom() {
        BLOOM.forceDisable();
    }

    /**
     * Enables physically based bloom for your mod.
     */
    public static void enablePhysicallyBasedBloom() {
        PB_BLOOM.setActive(true);
    }

    /**
     * Permanently disables physically based bloom and prevents other mods from enabling it.
     * Use this if your mod is incompatible with physically based bloom.
     */
    public static void forceDisablePhysicallyBasedBloom() {
        PB_BLOOM.forceDisable();
    }
}
