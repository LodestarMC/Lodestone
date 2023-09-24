package team.lodestar.lodestone;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.lodestar.lodestone.compability.CuriosCompat;
import team.lodestar.lodestone.compability.JeiCompat;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.data.LodestoneBlockTagDatagen;
import team.lodestar.lodestone.data.LodestoneItemTagDatagen;
import team.lodestar.lodestone.data.LodestoneLangDatagen;
import team.lodestar.lodestone.handlers.screenparticle.ParticleEmitterHandler;
import team.lodestar.lodestone.setup.*;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleDataBuilder;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

import java.awt.*;

import static net.minecraft.util.Mth.nextFloat;

@Mod(LodestoneLib.LODESTONE)
public class LodestoneLib {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String LODESTONE = "lodestone";
    public static final RandomSource RANDOM = RandomSource.create();

    //TODO remove
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LODESTONE);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LODESTONE);

    public static final RegistryObject<Item> EXAMPLE_ITEM =
            ITEMS.register("example_item", () -> new TestItem(new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public static class TestItem extends Item implements ParticleEmitterHandler.ItemParticleSupplier {

        public TestItem(Properties pProperties) {
            super(pProperties);
        }

        @Override
        public void spawnLateParticles(ScreenParticleHolder target, Level level, float partialTick, ItemStack stack, float x, float y) {
            RandomSource rand = Minecraft.getInstance().level.getRandom();
            Color color = new Color(255, 0, 255);
            Color endColor = new Color(0, 255, 255);
            ScreenParticleBuilder.create(LodestoneScreenParticleRegistry.SPARKLE, target)
                    .setTransparencyData(GenericParticleData.create(0.04f, 0f).setEasing(Easing.SINE_IN_OUT).build())
                    .setScaleData(GenericParticleData.create(0.8f + rand.nextFloat() * 0.1f, 0).setEasing(Easing.SINE_IN_OUT, Easing.BOUNCE_IN_OUT).build())
                    .setColorData(ColorParticleData.create(color, endColor).setCoefficient(2f).build())
                    .setLifetime(10 + rand.nextInt(10))
                    .setRandomOffset(0.05f)
                    .setRandomMotion(0.05f, 0.05f)
                    .spawnOnStack(0, 0);

            ScreenParticleBuilder.create(LodestoneScreenParticleRegistry.WISP, target)
                    .setTransparencyData(GenericParticleData.create(0.02f, 0f).setEasing(Easing.SINE_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(nextFloat(rand, 0.2f, 0.4f)).setEasing(Easing.EXPO_OUT).build())
                    .setScaleData(GenericParticleData.create(0.6f + rand.nextFloat() * 0.4f, 0).setEasing(Easing.EXPO_OUT).build())
                    .setColorData(ColorParticleData.create(color, endColor).setCoefficient(1.25f).build())
                    .setLifetime(20 + rand.nextInt(8))
                    .setRandomOffset(0.1f)
                    .setRandomMotion(0.4f, 0.4f)
                    .spawnOnStack(0, 0)
                    .setLifetime(10 + rand.nextInt(2))
                    .setSpinData(SpinParticleData.create(nextFloat(rand, 0.05f, 0.1f)).build())
                    .setScaleData(GenericParticleData.create(0.8f + rand.nextFloat() * 0.4f, 0f).build())
                    .setRandomMotion(0.01f, 0.01f)
                    .spawnOnStack(0, 0);
        }
    }

    public LodestoneLib() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        LodestoneCommandRegistry.registerArgumentTypes();

        //TODO remove
        ITEMS.register(modBus);
        CREATIVE_MODE_TABS.register(modBus);

        LodestoneBlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modBus);
        LodestoneParticleRegistry.PARTICLES.register(modBus);
        LodestoneAttributeRegistry.ATTRIBUTES.register(modBus);
        LodestoneRecipeSerializerRegistry.RECIPE_SERIALIZERS.register(modBus);
        LodestonePaintings.register(modBus);

        JeiCompat.init();
        CuriosCompat.init();

        modBus.addListener(this::gatherData);
    }

    public static ResourceLocation lodestonePath(String path) {
        return new ResourceLocation(LODESTONE, path);
    }

    public void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new LodestoneLangDatagen(event.getGenerator().getPackOutput()));
        LodestoneBlockTagDatagen blockTagDatagen = new LodestoneBlockTagDatagen(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        event.getGenerator().addProvider(true, blockTagDatagen);
        event.getGenerator().addProvider(true, new LodestoneItemTagDatagen(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTagDatagen.contentsGetter(), event.getExistingFileHelper()));
    }
}