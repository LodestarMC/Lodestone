package team.lodestar.lodestone.setup;

import team.lodestar.lodestone.LodestoneLib;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class LodestonePaintings {
    public static final DeferredRegister<Motive> PAINTING_MOTIVES = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, LodestoneLib.LODESTONE);

    public static void register(IEventBus bus) {
        PAINTING_MOTIVES.register(bus);
        PAINTING_MOTIVES.register("lefunny", () -> new Motive(64, 64));
        PAINTING_MOTIVES.register("microfunny", () -> new Motive(16, 16));
    }
}