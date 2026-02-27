package team.lodestar.lodestone.datagen.implementation;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.data.event.*;
import team.lodestar.lodestone.LodestoneLib;

@EventBusSubscriber(modid = LodestoneLib.LODESTONE)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        var lookupProvider = event.getLookupProvider();
        var packOutput = event.getGenerator().getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();

        boolean includeClient = event.includeClient();
        boolean includeServer = event.includeServer();

        var blockTagDatagen = new LodestoneBlockTagDatagen(packOutput, lookupProvider, existingFileHelper);
        var itemTagDatagen = new LodestoneItemTagDatagen(packOutput, lookupProvider, blockTagDatagen.contentsGetter(), existingFileHelper);
        var damageTypeDatagen = new LodestoneDamageTypeDatagen(packOutput, lookupProvider, existingFileHelper);

        event.getGenerator().addProvider(includeServer, blockTagDatagen);
        event.getGenerator().addProvider(includeServer, itemTagDatagen);
        event.getGenerator().addProvider(includeServer, damageTypeDatagen);
    }
}
