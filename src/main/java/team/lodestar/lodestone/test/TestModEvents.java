package team.lodestar.lodestone.test;

import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.handlers.WorldEventHandler;

@Mod.EventBusSubscriber(modid = LodestoneLib.LODESTONE)
public class TestModEvents {
    @SubscribeEvent
    public static void punchCowEvent(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (event.getEntity() instanceof Cow) {
                WorldEventHandler.addWorldEvent(event.getEntity().level(), new TestWorldEvent());
            }
        }
    }
}
