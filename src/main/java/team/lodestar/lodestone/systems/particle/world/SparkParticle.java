package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.rendering.*;

import java.util.*;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.*;

public class SparkParticle extends GenericParticle {

    public static final VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setParticleFormat();

    public SparkParticle(ClientLevel world, WorldParticleOptions data, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, spriteSet, x, y, z, xd, yd, zd);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        consumer = getVertexConsumer(consumer);
        builder.setColorRaw(rCol, gCol, bCol).setAlpha(alpha);
    }

    @Override
    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return FIRST_INDEX;
    }
}