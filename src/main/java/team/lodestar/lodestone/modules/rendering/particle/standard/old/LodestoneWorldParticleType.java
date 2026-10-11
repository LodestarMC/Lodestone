//package team.lodestar.lodestone.modules.rendering.particle.standard.old;
//
//import net.minecraft.client.multiplayer.*;
//import net.minecraft.client.particle.*;
//import team.lodestar.lodestone.modules.rendering.particle.standard.world.*;
//import team.lodestar.lodestone.modules.rendering.particle.standard.world.options.*;
//
//import javax.annotation.*;
//
//public class LodestoneWorldParticleType extends AbstractLodestoneParticleType<WorldParticleConfig> {
//
//    public LodestoneWorldParticleType() {
//        super();
//    }
//
//    public static class Factory implements ParticleProvider<WorldParticleConfig> {
//        private final SpriteSet sprite;
//
//        public Factory(SpriteSet sprite) {
//            this.sprite = sprite;
//        }
//
//        @Nullable
//        @Override
//        public Particle createParticle(WorldParticleConfig data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
//            return new LodestoneWorldParticle(world, data, (ParticleEngine.MutableSpriteSet) sprite, x, y, z, mx, my, mz);
//        }
//    }
//}