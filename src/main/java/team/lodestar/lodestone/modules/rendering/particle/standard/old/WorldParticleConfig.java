//package team.lodestar.lodestone.modules.rendering.particle.standard.world.options;
//
//import net.minecraft.client.particle.*;
//import net.minecraft.core.particles.*;
//import team.lodestar.lodestone.helpers.*;
//import team.lodestar.lodestone.modules.rendering.*;
//import team.lodestar.lodestone.modules.rendering.particle.standard.*;
//import team.lodestar.lodestone.modules.rendering.particle.standard.render_types.*;
//import team.lodestar.lodestone.modules.rendering.particle.standard.renderer.*;
//import team.lodestar.lodestone.modules.rendering.particle.standard.world.*;
//import team.lodestar.lodestone.systems.rendering.LodestoneRenderLayer;
//
//import java.util.*;
//import java.util.function.*;
//
//
//public class WorldParticleConfig extends LodestoneParticleConfig implements ParticleOptions {
//
//    public final ParticleType<?> type;
//
//    public LodestoneParticleRenderer behavior = BillboardParticleRenderer.INSTANCE;
//    public ParticleRenderType renderType = LodestoneWorldParticleRenderType.ADDITIVE;
//    public LodestoneRenderLayer renderLayer = LodestoneRenderingSystem.DEFERRED_RENDER;
//
//    public final Collection<Consumer<LodestoneWorldParticle>> tickActors = new ArrayList<>();
//    public final Collection<Consumer<LodestoneWorldParticle>> spawnActors = new ArrayList<>();
//    public final Collection<Consumer<LodestoneWorldParticle>> renderActors = new ArrayList<>();
//
//    public int particleLight = RenderHelper.FULL_BRIGHT;
//
//    public boolean noClip = false;
//
//    public WorldParticleConfig(ParticleType<?> type) {
//        super();
//        this.type = type;
//    }
//
//    public WorldParticleConfig(Supplier<? extends LodestoneWorldParticleType> type) {
//        this(type.get());
//    }
//
//    public WorldParticleConfig setBehavior(LodestoneParticleRenderer behavior) {
//        this.behavior = behavior;
//        return this;
//    }
//
//    public WorldParticleConfig setBehaviorIfDefault(LodestoneParticleRenderer newBehavior) {
//        if (behavior.equals(BillboardParticleRenderer.INSTANCE)) {
//            return setBehavior(newBehavior);
//        }
//        return this;
//    }
//
//    @Override
//    public ParticleType<?> getType() {
//        return type;
//    }
//}