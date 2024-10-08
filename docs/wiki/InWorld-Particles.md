One of the more sought out and key features of Lodestone is the Particle System.
Lodestone Particles are still based on the Particle Engine, it's not a brand new thing with no connection to minecraft, but it is quite elaborate.
The basic premise is to allow Lodestone Particles to have everything about them defined by the WorldParticleBuilder, hopefully preventing the need for any hardcoded behavior.

To begin, you will need to instantiate a WorldParticleBuilder using `WorldParticleBuilder.create`
```java
    var builder = WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
```
The Particle Builder will require a particle type and nothing else. Once that's done, you are free to edit the properties that your particle will be spawned with in any way you want. And once you're done, simply use one of the `spawn` methods provided by the builder.

Here's an example of an actual effect:
```java
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import team.lodestar.lodestone.registry.common.particle.*;
import team.lodestar.lodestone.systems.easing.*;
import team.lodestar.lodestone.systems.particle.builder.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;

import java.awt.Color;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExampleParticleEffect {
    
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            spawnExampleParticles(player.level(), player.position());
        }
    }

    public static void spawnExampleParticles(Level level, Vec3 pos) {
        Color startingColor = new Color(100, 0, 100);
        Color endingColor = new Color(0, 100, 200);
        WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                .setScaleData(GenericParticleData.create(0.5f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                .setLifetime(40)
                .addMotion(0, 0.01f, 0)
                .enableNoClip()
                .spawn(level, pos.x, pos.y, pos.z);
    }
}
```

The given codeblock is a class that when present in your mod will spawn a particle every tick at the player's position.
Worth noting is; for scale, transparency, color, and spin you will be using ParticleData classes. 
These include: `ColorParticleData` for color, `SpinParticleData` for rotation and `GenericParticleData` for everything else.
Any of these are simply instantiated using an appropriate `#create` function, and here you will define things such as starting, middle and ending values, a lifetime coefficient, as well as easings. Finalize this by using `#build`

For more examples of InWorld Particle Effects please look at this area of the Malum Repository: [Example Visual Effects](https://github.com/SammySemicolon/Malum-Mod/tree/1.6-1.20.1/src/main/java/com/sammy/malum/visual_effects)