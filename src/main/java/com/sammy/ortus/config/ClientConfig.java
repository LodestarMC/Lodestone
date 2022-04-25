package com.sammy.ortus.config;

import com.sammy.ortus.systems.config.OrtusConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import static com.sammy.ortus.OrtusLib.ORTUS;

public class ClientConfig extends OrtusConfig {

    public static ConfigValueHolder<Boolean> DELAYED_PARTICLE_RENDERING = new ConfigValueHolder<>(ORTUS, "client/graphics/particle", builder ->
            builder.comment("Should particles render on the delayed buffer? This means they will properly render after clouds & water do, but could cause issues with mods like sodium.")
                    .define("buffer_particles", true));
    public static ConfigValueHolder<Double> FIRE_OVERLAY_OFFSET  = new ConfigValueHolder<>(ORTUS, "client/graphics/fire", builder ->
            builder.comment("Downwards offset of Minecraft's first-person fire overlay. Higher numbers cause it to visually display lower and free up more screen space.")
                    .defineInRange("fire_overlay_offset", 0d, 0d, 1d));
    public static ConfigValueHolder<Double> SCREENSHAKE_INTENSITY  = new ConfigValueHolder<>(ORTUS, "client/screenshake", builder ->
            builder.comment("Intensity of screenshake. Higher numbers increase amplitude.")
                    .defineInRange("screenshake_intensity", 1d, 0d, 1d));

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        super("ortus/client", builder);
    }

    public static final ClientConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
}