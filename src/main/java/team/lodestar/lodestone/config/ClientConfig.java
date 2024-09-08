package team.lodestar.lodestone.config;

import io.github.fabricators_of_create.porting_lib.config.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import team.lodestar.lodestone.systems.config.LodestoneConfig;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;


public class ClientConfig extends LodestoneConfig {


    public static ConfigValueHolder<Boolean> DELAYED_PARTICLE_RENDERING = new ConfigValueHolder<>(LODESTONE, "client/graphics/particle", builder ->
            builder.comment("Should particles render on the delayed buffer? This means they will properly render after clouds & water do, but could cause issues with mods like sodium.")
                    .define("buffer_particles", true));

    public static ConfigValueHolder<Boolean> EXPERIMENTAL_FABULOUS_LAYERING = new ConfigValueHolder<>(LODESTONE, "client/graphics", builder ->
            builder.comment("Should lodestone use experimental fabulous graphics layering? You pretty much never wanna turn this on at the moment unless you're a developer.")
                    .define("experimental_fabulous_layering", false));

    public static ConfigValueHolder<Double> FIRE_OVERLAY_OFFSET = new ConfigValueHolder<>(LODESTONE, "client/graphics/fire", builder ->
            builder.comment("Downwards offset of Minecraft's first-person fire overlay. Higher numbers cause it to visually display lower and free up more screen space.")
                    .defineInRange("fire_overlay_offset", 0d, 0d, 1d));
    public static ConfigValueHolder<Double> SCREENSHAKE_INTENSITY = new ConfigValueHolder<>(LODESTONE, "client/screenshake", builder ->
            builder.comment("Intensity of screenshake. Higher numbers increase amplitude. Disable to turn off screenshake.")
                    .defineInRange("screenshake_intensity", 1d, 0d, 5d));
    public static ConfigValueHolder<Boolean> ENABLE_SCREEN_PARTICLES = new ConfigValueHolder<>(LODESTONE, "client/screen_particles", builder ->
            builder.comment("Are screen particles enabled?")
                    .define("enable_screen_particles", true));

    public static ConfigValueHolder<Boolean> DISABLE_SHADER_ON_PARTCLES = new ConfigValueHolder<>(LODESTONE, "client/graphics/particle", builder ->
            builder.comment("Can be used to see WorldParticles with Iris")
                    .define("disable_shader_particles", false)
    );

    public ClientConfig(ModConfigSpec.Builder builder) {
        super(LODESTONE, "client", builder);
    }

    public static final ClientConfig INSTANCE;
    public static final ModConfigSpec clientSpec;

    static {
        final Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        clientSpec = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
}