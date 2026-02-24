package team.lodestar.lodestone.systems.particle;

import net.minecraft.client.particle.*;
import org.joml.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.screen.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.options.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

public class LodestoneCommonParticleData<T extends IParticle> {

    public final ParticleEngine.MutableSpriteSet spriteSet;
    public final SimpleParticleOptions.ParticleSpritePicker spritePicker;

    protected final ColorParticleData colorData;
    protected final GenericParticleData transparencyData;
    protected final GenericParticleData scaleData;
    @Nullable
    protected final GenericParticleData lengthData;
    protected final SpinParticleData spinData;

    public final Collection<Consumer<T>> spawnActors;
    public final Collection<Consumer<T>> tickActors;
    public final Collection<Consumer<T>> renderActors;

    public Vector3f oldTravelledDistance = new Vector3f();
    public Vector3f travelledDistance = new Vector3f();

    public float partialTicksCache;

    public int lifeDelay;
    private float quadLength;

    float[] hsv1 = new float[3], hsv2 = new float[3];

    public LodestoneCommonParticleData(WorldParticleOptions options, ParticleEngine.MutableSpriteSet spriteSet) {
        this(spriteSet, options.spritePicker, options.colorData, options.transparencyData, options.scaleData, options.lengthData, options.spinData,
                options.spawnActors, options.tickActors, options.renderActors);
    }

    public LodestoneCommonParticleData(ScreenParticleOptions options, ParticleEngine.MutableSpriteSet spriteSet) {
        this(spriteSet, options.spritePicker, options.colorData, options.transparencyData, options.scaleData, options.lengthData, options.spinData,
                options.spawnActors, options.tickActors, options.renderActors);
    }

    public LodestoneCommonParticleData(ParticleEngine.MutableSpriteSet spriteSet, SimpleParticleOptions.ParticleSpritePicker spritePicker, ColorParticleData colorData, GenericParticleData transparencyData, GenericParticleData scaleData, @Nullable GenericParticleData lengthData, SpinParticleData spinData, Collection<Consumer<T>> spawnActors, Collection<Consumer<T>> tickActors, Collection<Consumer<T>> renderActors) {
        this.spriteSet = spriteSet;
        this.spritePicker = spritePicker;
        this.colorData = colorData;
        this.transparencyData = transparencyData;
        this.scaleData = scaleData;
        this.lengthData = lengthData != WorldParticleOptions.DEFAULT_GENERIC ? lengthData : null;
        this.spinData = spinData;
        this.spawnActors = spawnActors;
        this.tickActors = tickActors;
        this.renderActors = renderActors;
    }
}