//package team.lodestar.lodestone.modules.rendering.particle.standard.screen;
//
//import team.lodestar.lodestone.modules.rendering.particle.standard.LodestoneParticleConfig;
//import team.lodestar.lodestone.modules.rendering.particle.standard.render_types.LodestoneScreenParticleRenderType;
//
//import java.util.*;
//import java.util.function.Consumer;
//
//public class ScreenParticleConfig extends LodestoneParticleConfig {
//
//    public final ScreenParticleType<?> type;
//    public LodestoneScreenParticleRenderType renderType = LodestoneScreenParticleRenderType.ADDITIVE;
//
//    public final Collection<Consumer<LodestoneScreenParticle>> tickActors = new ArrayList<>();
//    public final Collection<Consumer<LodestoneScreenParticle>> spawnActors = new ArrayList<>();
//    public final Collection<Consumer<LodestoneScreenParticle>> renderActors = new ArrayList<>();
//
//    public boolean tracksStack;
//    public double stackTrackXOffset;
//    public double stackTrackYOffset;
//
//    public ScreenParticleConfig(ScreenParticleType<?> type) {
//        super();
//        this.type = type;
//    }
//}