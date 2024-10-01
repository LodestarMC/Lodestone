package team.lodestar.lodestone.mixin.client;

import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FabricSpriteProviderImpl.class)
public interface FabricSpriteProviderImplAccessor {
    @Invoker("<init>")
    static FabricSpriteProviderImpl FabricSpriteProviderImpl(ParticleEngine manager, SpriteSet delegate) {
        throw new AssertionError();
    }
}