package team.lodestar.lodestone;

import net.fabricmc.api.ClientModInitializer;
import team.lodestar.lodestone.registry.common.LodestoneBlockEntityRegistry;
import team.lodestar.lodestone.registry.common.LodestoneOptionRegistry;

public class LodestoneLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        LodestoneBlockEntityRegistry.ClientOnly.registerRenderer();
        LodestoneOptionRegistry.addOption();
    }
}
