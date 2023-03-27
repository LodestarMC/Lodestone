package team.lodestar.lodestone.config;

import team.lodestar.lodestone.systems.config.ConfigGroup;
import team.lodestar.lodestone.systems.config.ConfigValueHolder;
import team.lodestar.lodestone.systems.config.LodestoneConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

public class ClientConfig extends LodestoneConfig {

    public static final ConfigGroup CLIENT = new ConfigGroup(LODESTONE, "client");


    public static ConfigValueHolder<Boolean> DELAYED_PARTICLE_RENDERING = new ConfigValueHolder<>(CLIENT, "graphics/particle", builder ->
            builder.comment("Should particles render on the delayed buffer? This means they will properly render after clouds & water do, but could cause issues with mods like sodium.")
                    .define("buffer_particles", true));
    public static ConfigValueHolder<Double> FIRE_OVERLAY_OFFSET  = new ConfigValueHolder<>(CLIENT, "graphics/fire", builder ->
            builder.comment("Downwards offset of Minecraft's first-person fire overlay. Higher numbers cause it to visually display lower and free up more screen space.")
                    .defineInRange("fire_overlay_offset", 0d, 0d, 1d));
    public static ConfigValueHolder<Double> SCREENSHAKE_INTENSITY  = new ConfigValueHolder<>(CLIENT, "graphics/screenshake", builder ->
            builder.comment("Intensity of screenshake. Higher numbers increase amplitude. Disable to turn off screenshake.")
                    .defineInRange("screenshake_intensity", 1d, 0d, 5d));
    public static ConfigValueHolder<Boolean> ENABLE_SCREEN_PARTICLES  = new ConfigValueHolder<>(CLIENT, "graphics/screen_particles", builder ->
            builder.comment("Are screen particles enabled?")
                    .define("enable_screen_particles", true));

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        super(CLIENT, builder);
    }

    public static final ClientConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
}