package team.lodestar.lodestone.setup;

import team.lodestar.lodestone.options.FireOffsetOption;
import team.lodestar.lodestone.options.ScreenshakeOption;
import team.lodestar.lodestone.systems.option.LodestoneOption;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;

@SuppressWarnings("ALL")
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LodestoneOptionRegistry {
    public static final ArrayList<LodestoneOption> OPTIONS = new ArrayList<>();

    @SubscribeEvent
    public static void registerOptions(FMLCommonSetupEvent event) {
        registerOption(new ScreenshakeOption());
        registerOption(new FireOffsetOption());
    }

    public static void registerOption(LodestoneOption option) {
        OPTIONS.add(option);
    }
}