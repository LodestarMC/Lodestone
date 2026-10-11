//package team.lodestar.lodestone.modules.rendering.particle.standard.old;
//
//import net.minecraft.client.multiplayer.*;
//import net.minecraft.client.particle.*;
//
//import javax.annotation.*;
//
//public class LodestoneTerrainParticleType extends AbstractLodestoneParticleType<LodestoneTerrainParticleOptions> {
//
//    public LodestoneTerrainParticleType() {
//        super();
//    }
//
//    public static class Factory implements ParticleProvider<LodestoneTerrainParticleOptions> {
//
//        public Factory() {
//        }
//
//        @Nullable
//        @Override
//        public Particle createParticle(LodestoneTerrainParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
//            return new LodestoneTerrainParticle(world, data, x, y, z, mx, my, mz);
//        }
//    }
//}