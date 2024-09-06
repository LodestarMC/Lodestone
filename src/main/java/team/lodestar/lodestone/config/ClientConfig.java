package team.lodestar.lodestone.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import team.lodestar.lodestone.systems.config.LodestoneConfig;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;


public class ClientConfig extends LodestoneConfig {

    public static ConfigValueHolder<Boolean> DELAYED_PARTICLE_RENDERING = new ConfigValueHolder<>(LODESTONE, "client/graphics/particle", builder ->
            builder.comment("Should particles render on the delayed buffer? This means they will properly render after clouds & water do, but could cause issues with mods like sodium.")
                    .define("buffer_particles", true));

    public static ConfigValueHolder<Double> FIRE_OVERLAY_OFFSET = new ConfigValueHolder<>(LODESTONE, "client/graphics/fire", builder ->
            builder.comment("Downwards offset of Minecraft's first-person fire overlay. Higher numbers cause it to visually display lower and free up more screen space.")
                    .defineInRange("fire_overlay_offset", 0d, 0d, 1d));
    public static ConfigValueHolder<Double> SCREENSHAKE_INTENSITY = new ConfigValueHolder<>(LODESTONE, "client/screenshake", builder ->
            builder.comment("Intensity of screenshake. Higher numbers increase amplitude. Disable to turn off screenshake.")
                    .defineInRange("screenshake_intensity", 1d, 0d, 5d));
    public static ConfigValueHolder<Boolean> ENABLE_SCREEN_PARTICLES = new ConfigValueHolder<>(LODESTONE, "client/screen_particles", builder ->
            builder.comment("Are screen particles enabled?")
                    .define("enable_screen_particles", true));

    public static ConfigValueHolder<Double> LOD_DISTANCE_MULTIPLIER = new ConfigValueHolder<>(LODESTONE, "client/model", builder ->
            builder.comment("Multiplier for the distance at which model levels of detail are rendered. Higher numbers increase the distance at which detailed models are rendered.")
                    .defineInRange("lod_distance_multiplier", 1d, 0.1d, 10d));

    public ClientConfig(ModConfigSpec.Builder builder) {
        super(LODESTONE, "client", builder);
    }

    public static final ClientConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        final Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
}