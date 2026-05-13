package team.lodestar.lodestone.systems.particle.editor;
//ik this might be done way better than this, buuuut idk how
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.modules.core.easing.Easing;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.editor.data.BehaviorMode;
import team.lodestar.lodestone.systems.particle.editor.data.ColorGroupData;
import team.lodestar.lodestone.systems.particle.editor.data.LightMode;
import team.lodestar.lodestone.systems.particle.editor.data.ParticleCurveData;
import team.lodestar.lodestone.systems.particle.editor.data.ParticleCurveGroupData;
import team.lodestar.lodestone.systems.particle.editor.data.ParticleOutputMode;
import team.lodestar.lodestone.systems.particle.editor.data.ParticlePreset;
import team.lodestar.lodestone.systems.particle.editor.data.PreviewPlaybackMode;
import team.lodestar.lodestone.systems.particle.editor.data.RenderLayerMode;
import team.lodestar.lodestone.systems.particle.editor.data.RenderMode;
import team.lodestar.lodestone.systems.particle.editor.data.ScreenParticlePreset;
import team.lodestar.lodestone.systems.particle.editor.data.ScreenRenderMode;
import team.lodestar.lodestone.systems.particle.editor.data.ScreenshakeGroupData;
import team.lodestar.lodestone.systems.particle.editor.data.SpawnDistributionMode;
import team.lodestar.lodestone.systems.particle.editor.data.SpinGroupData;
import team.lodestar.lodestone.systems.particle.editor.data.TransparencyGroupData;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeBuilder;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;
import team.lodestar.lodestone.systems.particle.world.behaviors.BillboardParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.DirectionalParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.LodestoneParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.PointyDirectionalParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.SparkParticleBehavior;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class EditorState {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Object> customGroupData = new HashMap<>();

    public ParticlePreset particle;
    public ScreenParticlePreset screenParticle;
    public ParticleOutputMode outputMode;
    public SpawnDistributionMode spawnDistribution;
    public PreviewPlaybackMode previewPlayback;
    public RenderMode renderMode;
    public ScreenRenderMode screenRenderMode;
    public RenderLayerMode renderLayer;
    public BehaviorMode behavior;
    public SimpleParticleOptions.ParticleSpritePicker spritePicker;
    public LightMode lightMode;

    public boolean depthFade;
    public boolean forceSpawn;
    public boolean noClip;
    public boolean autoPreview;
    public boolean followCamera;
    public boolean shapeOverlay;
    public boolean scaleLengthLinked;
    public int lightLevel;
    public int spawnCount;
    public int previewInterval;
    public int lifetime;
    public int lifeDelay;

    public double previewDistance;
    public double previewYOffset;
    public double fixedPreviewX;
    public double fixedPreviewY;
    public double fixedPreviewZ;
    public double motionX;
    public double motionY;
    public double motionZ;
    public double randomMotionX;
    public double randomMotionY;
    public double randomMotionZ;
    public double randomOffsetX;
    public double randomOffsetY;
    public double randomOffsetZ;
    public double shapeSizeX;
    public double shapeSizeY;
    public double shapeSizeZ;
    public double directionX;
    public double directionY;
    public double directionZ;
    public double screenX;
    public double screenY;
    public double stackTrackXOffset;
    public double stackTrackYOffset;

    public float gravity;
    public float friction;
    public float lifetimeModifier;
    public float lifeDelayModifier;
    public float gravityModifier;
    public float frictionModifier;
    public float sparkLengthCenter;

    public boolean forcedDirection;
    public boolean screenTrackStack;
    public ColorGroupData color;
    public TransparencyGroupData transparency;
    public ParticleCurveGroupData scale;
    public ParticleCurveGroupData length;
    public SpinGroupData spin;
    public ScreenshakeGroupData screenshake;

    public EditorState() {
        reset();
    }

    public void reset() {
        particle = ParticlePreset.WISP;
        screenParticle = ScreenParticlePreset.WISP;
        outputMode = ParticleOutputMode.WORLD;
        spawnDistribution = SpawnDistributionMode.BUILDER_RANDOM;
        previewPlayback = PreviewPlaybackMode.LOOPING;
        renderMode = RenderMode.ADDITIVE;
        screenRenderMode = ScreenRenderMode.ADDITIVE;
        renderLayer = RenderLayerMode.DEFERRED;
        behavior = BehaviorMode.BILLBOARD;
        spritePicker = SimpleParticleOptions.ParticleSpritePicker.WITH_AGE;
        lightMode = LightMode.FULL_BRIGHT;
        depthFade = false;
        forceSpawn = true;
        noClip = true;
        autoPreview = true;
        followCamera = false;
        shapeOverlay = true;
        color = ColorGroupData.createDefault();
        transparency = TransparencyGroupData.createDefault();
        scale = ParticleCurveGroupData.createDefault(0.18f);
        length = ParticleCurveGroupData.createDefault(0.18f);
        spin = SpinGroupData.createDefault();
        screenshake = ScreenshakeGroupData.createDefault();
        enableVisualData();
        scaleLengthLinked = false;
        lightLevel = RenderHelper.FULL_BRIGHT;
        spawnCount = 4;
        previewInterval = 3;
        lifetime = 28;
        lifeDelay = 0;
        previewDistance = 2.75;
        previewYOffset = -0.15;
        fixedPreviewX = 0.0;
        fixedPreviewY = 0.0;
        fixedPreviewZ = 0.0;
        motionX = 0.0;
        motionY = 0.01;
        motionZ = 0.0;
        randomMotionX = 0.035;
        randomMotionY = 0.035;
        randomMotionZ = 0.035;
        randomOffsetX = 0.18;
        randomOffsetY = 0.18;
        randomOffsetZ = 0.18;
        shapeSizeX = 0.18;
        shapeSizeY = 0.18;
        shapeSizeZ = 0.18;
        gravity = 0.0f;
        friction = 0.96f;
        lifetimeModifier = 1.0f;
        lifeDelayModifier = 1.0f;
        gravityModifier = 1.0f;
        frictionModifier = 1.0f;
        forcedDirection = false;
        directionX = 0.0;
        directionY = 1.0;
        directionZ = 0.0;
        screenX = 80.0;
        screenY = 80.0;
        screenTrackStack = false;
        stackTrackXOffset = 0.0;
        stackTrackYOffset = 0.0;
        sparkLengthCenter = 0.0f;
    }

    public WorldParticleBuilder createBuilder() {
        WorldParticleBuilder builder = WorldParticleBuilder.create(particle.supplier.get())
                .setRenderType(renderMode.get(depthFade))
                .setRenderTarget(renderLayer.layer)
                .setBehavior(createBehavior())
                .setSpritePicker(spritePicker)
                .setForceSpawn(forceSpawn)
                .setNoClip(noClip)
                .setLifetime(lifetime)
                .setLifeDelay(lifeDelay)
                .setGravity(gravity)
                .setFriction(friction)
                .setLifetimeModifier(lifetimeModifier)
                .setLifeDelayModifier(lifeDelayModifier)
                .setGravityModifier(value -> value * gravityModifier)
                .setFrictionModifier(value -> value * frictionModifier)
                .setMotion(motionX, motionY, motionZ)
                .setRandomMotion(randomMotionX, randomMotionY, randomMotionZ);
        if (color.enabled) {
            builder.setColorData(ColorParticleData.create(red(color.startColor), green(color.startColor), blue(color.startColor), red(color.endColor), green(color.endColor), blue(color.endColor))
                    .setCoefficient(color.coefficient)
                    .setEasing(color.easing));
        }
        applyCurveData(builder);
        if (spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
            builder.setRandomOffset(randomOffsetX, randomOffsetY, randomOffsetZ);
        }

        switch (lightMode) {
            case NATURAL -> builder.setNaturalLighting();
            case CUSTOM -> builder.setLightLevel(lightLevel);
            case FULL_BRIGHT -> builder.setFullBrightLighting();
        }
        return builder;
    }

    public ScreenParticleBuilder createScreenBuilder(ScreenParticleHolder target) {
        ScreenParticleBuilder builder = ScreenParticleBuilder.create(screenParticle.type, target)
                .setRenderType(screenRenderMode.renderType)
                .setSpritePicker(spritePicker)
                .setLifetime(lifetime)
                .setLifeDelay(lifeDelay)
                .setGravity(gravity)
                .setFriction(friction)
                .setLifetimeModifier(lifetimeModifier)
                .setLifeDelayModifier(lifeDelayModifier)
                .setGravityModifier(value -> value * gravityModifier)
                .setFrictionModifier(value -> value * frictionModifier)
                .setMotion(motionX, motionY)
                .setRandomMotion(randomMotionX, randomMotionY);
        if (color.enabled) {
            builder.setColorData(ColorParticleData.create(red(color.startColor), green(color.startColor), blue(color.startColor), red(color.endColor), green(color.endColor), blue(color.endColor))
                    .setCoefficient(color.coefficient)
                    .setEasing(color.easing));
        }
        applyCurveData(builder);
        if (spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
            builder.setRandomOffset(randomOffsetX, randomOffsetY);
        }
        return builder;
    }

    private void applyCurveData(WorldParticleBuilder builder) {
        if (transparency.enabled) {
            builder.setTransparencyData(transparency.curve.buildGeneric());
        }
        if (scale.enabled) {
            builder.setScaleData(scale.curve.buildGeneric());
        }
        if (length.enabled) {
            builder.setLengthData(length.curve.buildGeneric());
        }
        if (spin.enabled) {
            builder.setSpinData(spin.curve.buildSpin(spin.offset));
        }
    }

    private void applyCurveData(ScreenParticleBuilder builder) {
        if (transparency.enabled) {
            builder.setTransparencyData(transparency.curve.buildGeneric());
        }
        if (scale.enabled) {
            builder.setScaleData(scale.curve.buildGeneric());
        }
        if (length.enabled) {
            builder.setLengthData(length.curve.buildGeneric());
        }
        if (spin.enabled) {
            builder.setSpinData(spin.curve.buildSpin(spin.offset));
        }
    }

    public Vec3 fixedPreviewPosition() {
        return new Vec3(fixedPreviewX, fixedPreviewY, fixedPreviewZ);
    }

    public void setFixedPreviewPosition(Vec3 position) {
        fixedPreviewX = position.x;
        fixedPreviewY = position.y;
        fixedPreviewZ = position.z;
    }

    public void enableVisualData() {
        color.enabled = true;
        transparency.enabled = true;
        scale.enabled = true;
        length.enabled = true;
        spin.enabled = true;
    }

    public Vec3 createWorldDistributionOffset(RandomSource random) {
        return createDistributionOffset(random, false);
    }

    public Vec3 createScreenDistributionOffset(RandomSource random) {
        return createDistributionOffset(random, true);
    }

    private Vec3 createDistributionOffset(RandomSource random, boolean screen) {
        double xRadius = Math.max(0.0, shapeSizeX);
        double yRadius = Math.max(0.0, shapeSizeY);
        double zRadius = screen ? 0.0 : Math.max(0.0, shapeSizeZ);
        return switch (spawnDistribution) {
            case BUILDER_RANDOM, POINT -> Vec3.ZERO;
            case CUBE -> new Vec3(randomSigned(random, xRadius), randomSigned(random, yRadius), screen ? 0.0 : randomSigned(random, zRadius));
            case CIRCLE -> {
                double angle = random.nextDouble() * Math.PI * 2.0;
                double radius = Math.sqrt(random.nextDouble());
                if (screen) {
                    yield new Vec3(Math.cos(angle) * radius * xRadius, Math.sin(angle) * radius * yRadius, 0.0);
                }
                yield new Vec3(Math.cos(angle) * radius * xRadius, 0.0, Math.sin(angle) * radius * zRadius);
            }
            case SPHERE -> {
                if (screen) {
                    double angle = random.nextDouble() * Math.PI * 2.0;
                    double radius = Math.sqrt(random.nextDouble());
                    yield new Vec3(Math.cos(angle) * radius * xRadius, Math.sin(angle) * radius * yRadius, 0.0);
                }
                double angle = random.nextDouble() * Math.PI * 2.0;
                double vertical = random.nextDouble() * 2.0 - 1.0;
                double radius = Math.cbrt(random.nextDouble());
                double horizontal = Math.sqrt(Math.max(0.0, 1.0 - vertical * vertical));
                yield new Vec3(Math.cos(angle) * horizontal * radius * xRadius, vertical * radius * yRadius, Math.sin(angle) * horizontal * radius * zRadius);
            }
        };
    }

    private static double randomSigned(RandomSource random, double radius) {
        return (random.nextDouble() * 2.0 - 1.0) * radius;
    }

    public ScreenshakeInstance createScreenshake(Vec3 center) {
        ScreenshakeBuilder builder = ScreenshakeBuilder.create()
                .setDuration(screenshake.duration)
                .setStrength(screenshake.startStrength, screenshake.middleStrength, screenshake.endStrength)
                .setEasing(screenshake.startEasing, screenshake.endEasing)
                .setCoefficient(screenshake.coefficient);
        if (screenshake.positioned) {
            builder.placedAt(center, screenshake.falloffDistance, screenshake.falloffEasing);
        }
        return builder.build();
    }

    public LodestoneParticleBehavior createBehavior() {
        Vec3 direction = new Vec3(directionX, directionY, directionZ);
        return switch (behavior) {
            case BILLBOARD -> BillboardParticleBehavior.INSTANCE;
            case DIRECTIONAL -> forcedDirection ? DirectionalParticleBehavior.directional(direction) : DirectionalParticleBehavior.directional();
            case POINTY_DIRECTIONAL -> forcedDirection ? PointyDirectionalParticleBehavior.pointyDirectional(direction) : PointyDirectionalParticleBehavior.pointyDirectional();
            case SPARK -> {
                SparkParticleBehavior spark = SparkParticleBehavior.sparkBehavior().setLengthCenter(sparkLengthCenter);
                if (forcedDirection) {
                    spark.setForcedDirection(direction);
                }
                yield spark;
            }
        };
    }

    public String toJava() {
        return outputMode == ParticleOutputMode.SCREEN ? toScreenJava() : toWorldJava();
    }

    private String toWorldJava() {
        StringBuilder builder = new StringBuilder();
        builder.append("WorldParticleBuilder.create(").append(worldParticleJavaName()).append(")\n");
        builder.append("        .setRenderType(").append(renderMode.javaName(depthFade)).append(")\n");
        builder.append("        .setRenderTarget(").append(renderLayer.javaName).append(")\n");
        builder.append("        .setBehavior(").append(behaviorJava()).append(")\n");
        builder.append("        .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.").append(spritePicker.name()).append(")\n");
        builder.append("        .setForceSpawn(").append(forceSpawn).append(")\n");
        builder.append("        .setNoClip(").append(noClip).append(")\n");
        builder.append("        .setLifetime(").append(lifetime).append(")\n");
        builder.append("        .setLifeDelay(").append(lifeDelay).append(")\n");
        builder.append("        .setGravity(").append(formatNumber(gravity)).append("f)\n");
        builder.append("        .setFriction(").append(formatNumber(friction)).append("f)\n");
        builder.append("        .setLifetimeModifier(").append(formatNumber(lifetimeModifier)).append("f)\n");
        builder.append("        .setLifeDelayModifier(").append(formatNumber(lifeDelayModifier)).append("f)\n");
        builder.append("        .setGravityModifier(value -> value * ").append(formatNumber(gravityModifier)).append("f)\n");
        builder.append("        .setFrictionModifier(value -> value * ").append(formatNumber(frictionModifier)).append("f)\n");
        builder.append("        .setMotion(").append(formatNumber(motionX)).append(", ").append(formatNumber(motionY)).append(", ").append(formatNumber(motionZ)).append(")\n");
        builder.append("        .setRandomMotion(").append(formatNumber(randomMotionX)).append(", ").append(formatNumber(randomMotionY)).append(", ").append(formatNumber(randomMotionZ)).append(")\n");
        if (spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
            builder.append("        .setRandomOffset(").append(formatNumber(randomOffsetX)).append(", ").append(formatNumber(randomOffsetY)).append(", ").append(formatNumber(randomOffsetZ)).append(")\n");
        }
        appendLighting(builder);
        appendColorJava(builder);
        appendCurveJava(builder);
        if (usesManualDistribution()) {
            builder.insert(0, "WorldParticleBuilder builder = ");
            builder.append("        ;\n");
            appendWorldDistributionJava(builder);
        } else {
            builder.append("        .repeat(level, pos, ").append(spawnCount).append(");");
        }
        appendScreenshakeJava(builder, "pos");
        return builder.toString();
    }

    private String toScreenJava() {
        StringBuilder builder = new StringBuilder();
        builder.append("ScreenParticleBuilder.create(").append(screenParticle.javaName).append(", target)\n");
        builder.append("        .setRenderType(").append(screenRenderMode.javaName).append(")\n");
        builder.append("        .setSpritePicker(SimpleParticleOptions.ParticleSpritePicker.").append(spritePicker.name()).append(")\n");
        builder.append("        .setLifetime(").append(lifetime).append(")\n");
        builder.append("        .setLifeDelay(").append(lifeDelay).append(")\n");
        builder.append("        .setGravity(").append(formatNumber(gravity)).append("f)\n");
        builder.append("        .setFriction(").append(formatNumber(friction)).append("f)\n");
        builder.append("        .setLifetimeModifier(").append(formatNumber(lifetimeModifier)).append("f)\n");
        builder.append("        .setLifeDelayModifier(").append(formatNumber(lifeDelayModifier)).append("f)\n");
        builder.append("        .setGravityModifier(value -> value * ").append(formatNumber(gravityModifier)).append("f)\n");
        builder.append("        .setFrictionModifier(value -> value * ").append(formatNumber(frictionModifier)).append("f)\n");
        builder.append("        .setMotion(").append(formatNumber(motionX)).append(", ").append(formatNumber(motionY)).append(")\n");
        builder.append("        .setRandomMotion(").append(formatNumber(randomMotionX)).append(", ").append(formatNumber(randomMotionY)).append(")\n");
        if (spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
            builder.append("        .setRandomOffset(").append(formatNumber(randomOffsetX)).append(", ").append(formatNumber(randomOffsetY)).append(")\n");
        }
        appendColorJava(builder);
        appendCurveJava(builder);
        if (usesManualDistribution()) {
            builder.insert(0, "ScreenParticleBuilder builder = ");
            builder.append("        ;\n");
            appendScreenDistributionJava(builder);
        } else {
            if (screenTrackStack) {
                builder.append("        .repeatOnStack(").append(formatNumber(stackTrackXOffset)).append(", ").append(formatNumber(stackTrackYOffset)).append(", ").append(spawnCount).append(");");
            } else {
                builder.append("        .repeat(").append(formatNumber(screenX)).append(", ").append(formatNumber(screenY)).append(", ").append(spawnCount).append(");");
            }
        }
        appendScreenshakeJava(builder, null);
        return builder.toString();
    }

    private boolean usesManualDistribution() {
        return spawnDistribution == SpawnDistributionMode.SPHERE || spawnDistribution == SpawnDistributionMode.CIRCLE || spawnDistribution == SpawnDistributionMode.CUBE;
    }

    private void appendWorldDistributionJava(StringBuilder builder) {
        builder.append("for (int i = 0; i < ").append(spawnCount).append("; i++) {\n");
        appendWorldOffsetJava(builder);
        builder.append("    builder.spawn(level, pos.add(offset));\n");
        builder.append("}");
    }

    private void appendScreenDistributionJava(StringBuilder builder) {
        builder.append("java.util.concurrent.ThreadLocalRandom random = java.util.concurrent.ThreadLocalRandom.current();\n");
        builder.append("for (int i = 0; i < ").append(spawnCount).append("; i++) {\n");
        appendScreenOffsetJava(builder);
        if (screenTrackStack) {
            builder.append("    builder.spawnOnStack(").append(formatNumber(stackTrackXOffset)).append(" + offset.x, ").append(formatNumber(stackTrackYOffset)).append(" + offset.y);\n");
        } else {
            builder.append("    builder.spawn(").append(formatNumber(screenX)).append(" + offset.x, ").append(formatNumber(screenY)).append(" + offset.y);\n");
        }
        builder.append("}");
    }

    private void appendWorldOffsetJava(StringBuilder builder) {
        switch (spawnDistribution) {
            case CUBE -> builder.append("    Vec3 offset = new Vec3((level.random.nextDouble() * 2.0 - 1.0) * ").append(formatNumber(shapeSizeX))
                    .append(", (level.random.nextDouble() * 2.0 - 1.0) * ").append(formatNumber(shapeSizeY))
                    .append(", (level.random.nextDouble() * 2.0 - 1.0) * ").append(formatNumber(shapeSizeZ)).append(");\n");
            case CIRCLE -> {
                builder.append("    double angle = level.random.nextDouble() * Math.PI * 2.0;\n");
                builder.append("    double radius = Math.sqrt(level.random.nextDouble());\n");
                builder.append("    Vec3 offset = new Vec3(Math.cos(angle) * radius * ").append(formatNumber(shapeSizeX))
                        .append(", 0.0, Math.sin(angle) * radius * ").append(formatNumber(shapeSizeZ)).append(");\n");
            }
            case SPHERE -> {
                builder.append("    double angle = level.random.nextDouble() * Math.PI * 2.0;\n");
                builder.append("    double vertical = level.random.nextDouble() * 2.0 - 1.0;\n");
                builder.append("    double radius = Math.cbrt(level.random.nextDouble());\n");
                builder.append("    double horizontal = Math.sqrt(Math.max(0.0, 1.0 - vertical * vertical));\n");
                builder.append("    Vec3 offset = new Vec3(Math.cos(angle) * horizontal * radius * ").append(formatNumber(shapeSizeX))
                        .append(", vertical * radius * ").append(formatNumber(shapeSizeY))
                        .append(", Math.sin(angle) * horizontal * radius * ").append(formatNumber(shapeSizeZ)).append(");\n");
            }
            default -> builder.append("    Vec3 offset = Vec3.ZERO;\n");
        }
    }

    private void appendScreenOffsetJava(StringBuilder builder) {
        switch (spawnDistribution) {
            case CUBE -> builder.append("    Vec3 offset = new Vec3((random.nextDouble() * 2.0 - 1.0) * ").append(formatNumber(shapeSizeX))
                    .append(", (random.nextDouble() * 2.0 - 1.0) * ").append(formatNumber(shapeSizeY))
                    .append(", 0.0);\n");
            case CIRCLE, SPHERE -> {
                builder.append("    double angle = random.nextDouble() * Math.PI * 2.0;\n");
                builder.append("    double radius = Math.sqrt(random.nextDouble());\n");
                builder.append("    Vec3 offset = new Vec3(Math.cos(angle) * radius * ").append(formatNumber(shapeSizeX))
                        .append(", Math.sin(angle) * radius * ").append(formatNumber(shapeSizeY)).append(", 0.0);\n");
            }
            default -> builder.append("    Vec3 offset = Vec3.ZERO;\n");
        }
    }

    private void appendScreenshakeJava(StringBuilder builder, String centerExpression) {
        if (!screenshake.enabled || !screenshake.onBurst) {
            return;
        }
        builder.append("\n");
        builder.append("ScreenshakeHandler.addScreenshake(ScreenshakeBuilder.create()\n");
        builder.append(".setDuration(").append(screenshake.duration).append(")\n");
        builder.append(".setStrength(").append(formatNumber(screenshake.startStrength)).append("f, ")
                .append(formatNumber(screenshake.middleStrength)).append("f, ")
                .append(formatNumber(screenshake.endStrength)).append("f)\n");
        builder.append(".setEasing(Easing.").append(easingConstant(screenshake.startEasing)).append(", Easing.")
                .append(easingConstant(screenshake.endEasing)).append(")\n");
        builder.append(".setCoefficient(").append(formatNumber(screenshake.coefficient)).append("f)");
        if (screenshake.positioned && centerExpression != null) {
            builder.append("\n.placedAt(").append(centerExpression).append(", ")
                    .append(formatNumber(screenshake.falloffDistance)).append("f, Easing.")
                    .append(easingConstant(screenshake.falloffEasing)).append(")");
        }
        builder.append("\n        .build());");
    }

    private void appendColorJava(StringBuilder builder) {
        if (!color.enabled) {
            return;
        }
        builder.append(".setColorData(ColorParticleData.create(")
                .append(formatNumber(red(color.startColor))).append("f, ")
                .append(formatNumber(green(color.startColor))).append("f, ")
                .append(formatNumber(blue(color.startColor))).append("f, ")
                .append(formatNumber(red(color.endColor))).append("f, ")
                .append(formatNumber(green(color.endColor))).append("f, ")
                .append(formatNumber(blue(color.endColor))).append("f)")
                .append(".setCoefficient(").append(formatNumber(color.coefficient)).append("f)")
                .append(".setEasing(Easing.").append(easingConstant(color.easing)).append("))\n");
    }

    private void appendCurveJava(StringBuilder builder) {
        if (transparency.enabled) {
            builder.append("        .setTransparencyData(").append(transparency.curve.toJavaGeneric("GenericParticleData")).append(")\n");
        }
        if (scale.enabled) {
            builder.append("        .setScaleData(").append(scale.curve.toJavaGeneric("GenericParticleData")).append(")\n");
        }
        if (length.enabled) {
            builder.append("        .setLengthData(").append(length.curve.toJavaGeneric("GenericParticleData")).append(")\n");
        }
        if (spin.enabled) {
            builder.append("        .setSpinData(").append(spin.curve.toJavaGeneric("SpinParticleData")).append(".setSpinOffset(").append(formatNumber(spin.offset)).append("f))\n");
        }
    }

    public String toProjectJson(String name) {
        JsonObject root = new JsonObject();
        root.addProperty("format", "lodestone_particle_editor");
        root.addProperty("version", 1);
        root.addProperty("name", name);
        root.addProperty("outputMode", outputMode.label);
        root.addProperty("particle", particle.label);
        root.addProperty("screenParticle", screenParticle.label);
        root.addProperty("renderType", renderMode.label);
        root.addProperty("screenRenderType", screenRenderMode.label);
        root.addProperty("renderLayer", renderLayer.label);
        root.addProperty("behavior", behavior.label);
        root.addProperty("spritePicker", spritePicker.name());
        root.addProperty("lighting", lightMode.label);
        root.addProperty("depthFade", depthFade);
        root.addProperty("forceSpawn", forceSpawn);
        root.addProperty("noClip", noClip);
        root.addProperty("lightLevel", lightLevel);
        root.addProperty("lifetime", lifetime);
        root.addProperty("lifeDelay", lifeDelay);
        root.addProperty("lifetimeModifier", lifetimeModifier);
        root.addProperty("lifeDelayModifier", lifeDelayModifier);

        JsonObject preview = new JsonObject();
        preview.addProperty("spawnCount", spawnCount);
        preview.addProperty("livePreview", autoPreview);
        preview.addProperty("playback", previewPlayback.label);
        preview.addProperty("emitInterval", previewInterval);
        preview.addProperty("followCamera", followCamera);
        preview.addProperty("distance", previewDistance);
        preview.addProperty("yOffset", previewYOffset);
        preview.addProperty("fixedX", fixedPreviewX);
        preview.addProperty("fixedY", fixedPreviewY);
        preview.addProperty("fixedZ", fixedPreviewZ);
        root.add("preview", preview);

        JsonObject screen = new JsonObject();
        screen.addProperty("x", screenX);
        screen.addProperty("y", screenY);
        screen.addProperty("trackStack", screenTrackStack);
        screen.addProperty("stackXOffset", stackTrackXOffset);
        screen.addProperty("stackYOffset", stackTrackYOffset);
        root.add("screen", screen);

        JsonObject motion = new JsonObject();
        motion.addProperty("x", motionX);
        motion.addProperty("y", motionY);
        motion.addProperty("z", motionZ);
        motion.addProperty("randomX", randomMotionX);
        motion.addProperty("randomY", randomMotionY);
        motion.addProperty("randomZ", randomMotionZ);
        motion.addProperty("spawnDistribution", spawnDistribution.label);
        motion.addProperty("shapeOverlay", shapeOverlay);
        motion.addProperty("offsetX", randomOffsetX);
        motion.addProperty("offsetY", randomOffsetY);
        motion.addProperty("offsetZ", randomOffsetZ);
        JsonObject shape = new JsonObject();
        shape.addProperty("x", shapeSizeX);
        shape.addProperty("y", shapeSizeY);
        shape.addProperty("z", shapeSizeZ);
        motion.add("shapeSize", shape);
        motion.addProperty("gravity", gravity);
        motion.addProperty("friction", friction);
        motion.addProperty("gravityModifier", gravityModifier);
        motion.addProperty("frictionModifier", frictionModifier);
        motion.addProperty("forcedDirection", forcedDirection);
        motion.addProperty("directionX", directionX);
        motion.addProperty("directionY", directionY);
        motion.addProperty("directionZ", directionZ);
        motion.addProperty("sparkLengthCenter", sparkLengthCenter);
        root.add("motion", motion);

        JsonObject appearance = new JsonObject();
        appearance.addProperty("startColor", colorString(color.startColor));
        appearance.addProperty("endColor", colorString(color.endColor));
        appearance.addProperty("colorDataEnabled", color.enabled);
        appearance.addProperty("colorCoefficient", color.coefficient);
        appearance.addProperty("colorEasing", color.easing.name);
        appearance.addProperty("transparencyDataEnabled", transparency.enabled);
        appearance.addProperty("scaleDataEnabled", scale.enabled);
        appearance.addProperty("lengthDataEnabled", length.enabled);
        appearance.addProperty("spinDataEnabled", spin.enabled);
        appearance.addProperty("scaleLengthLinked", scaleLengthLinked);
        appearance.addProperty("spinOffset", spin.offset);
        appearance.add("transparency", curveToJson(transparency.curve));
        appearance.add("scale", curveToJson(scale.curve));
        appearance.add("length", curveToJson(length.curve));
        appearance.add("spin", curveToJson(spin.curve));
        root.add("appearance", appearance);
        EditorGroups.writeProjectData(root, this);

        JsonObject effects = new JsonObject();
        effects.addProperty("screenshake", screenshake.enabled);
        effects.addProperty("screenshakeOnBurst", screenshake.onBurst);
        effects.addProperty("screenshakePositioned", screenshake.positioned);
        effects.addProperty("screenshakeDuration", screenshake.duration);
        effects.addProperty("screenshakeStartStrength", screenshake.startStrength);
        effects.addProperty("screenshakeMiddleStrength", screenshake.middleStrength);
        effects.addProperty("screenshakeEndStrength", screenshake.endStrength);
        effects.addProperty("screenshakeStartEasing", screenshake.startEasing.name);
        effects.addProperty("screenshakeEndEasing", screenshake.endEasing.name);
        effects.addProperty("screenshakeCoefficient", screenshake.coefficient);
        effects.addProperty("screenshakeFalloffDistance", screenshake.falloffDistance);
        effects.addProperty("screenshakeFalloffEasing", screenshake.falloffEasing.name);
        root.add("effects", effects);
        return GSON.toJson(root);
    }

    public String toJavaFile(String className) {
        if (outputMode == ParticleOutputMode.SCREEN) {
            return """
                    import net.minecraft.world.phys.Vec3;
                    import team.lodestar.lodestone.handlers.ScreenshakeHandler;
                    import team.lodestar.lodestone.modules.core.easing.Easing;
                    import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleTypes;
                    import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
                    import team.lodestar.lodestone.systems.particle.builder.ScreenParticleBuilder;
                    import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
                    import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
                    import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
                    import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
                    import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
                    import team.lodestar.lodestone.systems.screenshake.ScreenshakeBuilder;

                    public final class %s {
                        private %s() {
                        }

                        public static void spawn(ScreenParticleHolder target) {
                    %s
                        }
                    }
                    """.formatted(className, className, indent(toJava(), 8));
        }
        return """
                import net.minecraft.world.level.Level;
                import net.minecraft.world.phys.Vec3;
                import team.lodestar.lodestone.handlers.LodestoneRenderHandler;
                import team.lodestar.lodestone.handlers.ScreenshakeHandler;
                import team.lodestar.lodestone.modules.core.easing.Easing;
                import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
                import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
                import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
                import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
                import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
                import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
                import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
                import team.lodestar.lodestone.systems.particle.world.behaviors.BillboardParticleBehavior;
                import team.lodestar.lodestone.systems.particle.world.behaviors.DirectionalParticleBehavior;
                import team.lodestar.lodestone.systems.particle.world.behaviors.PointyDirectionalParticleBehavior;
                import team.lodestar.lodestone.systems.particle.world.behaviors.SparkParticleBehavior;
                import team.lodestar.lodestone.systems.screenshake.ScreenshakeBuilder;

                public final class %s {
                    private %s() {
                    }

                    public static void spawn(Level level, Vec3 pos) {
                %s
                    }
                }
                """.formatted(className, className, indent(toJava(), 8));
    }

    public EditorState copy() {
        return fromProjectJson(toProjectJson("copy"));
    }

    @SuppressWarnings("unchecked")
    public <T> T customGroupData(String key, Supplier<T> defaultFactory) {
        return (T) customGroupData.computeIfAbsent(key, ignored -> defaultFactory.get());
    }

    public <T> void setCustomGroupData(String key, T data) {
        customGroupData.put(key, data);
    }

    public static String readProjectName(String json, String fallback) {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            return string(root, "name", fallback);
        } catch (RuntimeException ignored) {
            return fallback;
        }
    }

    public static EditorState fromProjectJson(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        EditorState state = new EditorState();
        state.outputMode = enumByLabel(ParticleOutputMode.values(), string(root, "outputMode", state.outputMode.label), value -> value.label, state.outputMode);
        state.particle = enumByLabel(ParticlePreset.values(), string(root, "particle", state.particle.label), value -> value.label, state.particle);
        state.screenParticle = enumByLabel(ScreenParticlePreset.values(), string(root, "screenParticle", state.screenParticle.label), value -> value.label, state.screenParticle);
        state.renderMode = enumByLabel(RenderMode.values(), string(root, "renderType", state.renderMode.label), value -> value.label, state.renderMode);
        state.screenRenderMode = enumByLabel(ScreenRenderMode.values(), string(root, "screenRenderType", state.screenRenderMode.label), value -> value.label, state.screenRenderMode);
        state.renderLayer = enumByLabel(RenderLayerMode.values(), string(root, "renderLayer", state.renderLayer.label), value -> value.label, state.renderLayer);
        state.behavior = enumByLabel(BehaviorMode.values(), string(root, "behavior", state.behavior.label), value -> value.label, state.behavior);
        state.spritePicker = enumByName(SimpleParticleOptions.ParticleSpritePicker.values(), string(root, "spritePicker", state.spritePicker.name()), state.spritePicker);
        state.lightMode = enumByLabel(LightMode.values(), string(root, "lighting", state.lightMode.label), value -> value.label, state.lightMode);
        state.depthFade = bool(root, "depthFade", state.depthFade);
        state.forceSpawn = bool(root, "forceSpawn", state.forceSpawn);
        state.noClip = bool(root, "noClip", state.noClip);
        state.lightLevel = integer(root, "lightLevel", state.lightLevel);
        state.lifetime = integer(root, "lifetime", state.lifetime);
        state.lifeDelay = integer(root, "lifeDelay", state.lifeDelay);
        state.lifetimeModifier = (float) decimal(root, "lifetimeModifier", state.lifetimeModifier);
        state.lifeDelayModifier = (float) decimal(root, "lifeDelayModifier", state.lifeDelayModifier);

        JsonObject preview = object(root, "preview");
        state.spawnCount = integer(preview, "spawnCount", integer(root, "spawnCount", state.spawnCount));
        state.autoPreview = bool(preview, "livePreview", bool(root, "livePreview", state.autoPreview));
        state.previewPlayback = enumByLabel(PreviewPlaybackMode.values(), string(preview, "playback", state.previewPlayback.label), value -> value.label, state.previewPlayback);
        state.previewInterval = integer(preview, "emitInterval", state.previewInterval);
        state.followCamera = bool(preview, "followCamera", bool(root, "followCamera", state.followCamera));
        state.previewDistance = decimal(preview, "distance", state.previewDistance);
        state.previewYOffset = decimal(preview, "yOffset", state.previewYOffset);
        state.fixedPreviewX = decimal(preview, "fixedX", state.fixedPreviewX);
        state.fixedPreviewY = decimal(preview, "fixedY", state.fixedPreviewY);
        state.fixedPreviewZ = decimal(preview, "fixedZ", state.fixedPreviewZ);

        JsonObject screen = object(root, "screen");
        state.screenX = decimal(screen, "x", state.screenX);
        state.screenY = decimal(screen, "y", state.screenY);
        state.screenTrackStack = bool(screen, "trackStack", state.screenTrackStack);
        state.stackTrackXOffset = decimal(screen, "stackXOffset", state.stackTrackXOffset);
        state.stackTrackYOffset = decimal(screen, "stackYOffset", state.stackTrackYOffset);

        JsonObject motion = object(root, "motion");
        state.motionX = decimal(motion, "x", array(root, "motion", 0, state.motionX));
        state.motionY = decimal(motion, "y", array(root, "motion", 1, state.motionY));
        state.motionZ = decimal(motion, "z", array(root, "motion", 2, state.motionZ));
        state.randomMotionX = decimal(motion, "randomX", array(root, "randomMotion", 0, state.randomMotionX));
        state.randomMotionY = decimal(motion, "randomY", array(root, "randomMotion", 1, state.randomMotionY));
        state.randomMotionZ = decimal(motion, "randomZ", array(root, "randomMotion", 2, state.randomMotionZ));
        state.spawnDistribution = enumByLabel(SpawnDistributionMode.values(), string(motion, "spawnDistribution", string(root, "spawnDistribution", state.spawnDistribution.label)), value -> value.label, state.spawnDistribution);
        state.shapeOverlay = bool(motion, "shapeOverlay", bool(root, "shapeOverlay", state.shapeOverlay));
        state.randomOffsetX = decimal(motion, "offsetX", array(root, "randomOffset", 0, state.randomOffsetX));
        state.randomOffsetY = decimal(motion, "offsetY", array(root, "randomOffset", 1, state.randomOffsetY));
        state.randomOffsetZ = decimal(motion, "offsetZ", array(root, "randomOffset", 2, state.randomOffsetZ));
        JsonObject shapeSize = object(motion, "shapeSize");
        state.shapeSizeX = decimal(shapeSize, "x", decimal(motion, "shapeSizeX", state.randomOffsetX));
        state.shapeSizeY = decimal(shapeSize, "y", decimal(motion, "shapeSizeY", state.randomOffsetY));
        state.shapeSizeZ = decimal(shapeSize, "z", decimal(motion, "shapeSizeZ", state.randomOffsetZ));
        state.gravity = (float) decimal(motion, "gravity", decimal(root, "gravity", state.gravity));
        state.friction = (float) decimal(motion, "friction", decimal(root, "friction", state.friction));
        state.gravityModifier = (float) decimal(motion, "gravityModifier", decimal(root, "gravityModifier", state.gravityModifier));
        state.frictionModifier = (float) decimal(motion, "frictionModifier", decimal(root, "frictionModifier", state.frictionModifier));
        state.forcedDirection = bool(motion, "forcedDirection", state.forcedDirection);
        state.directionX = decimal(motion, "directionX", state.directionX);
        state.directionY = decimal(motion, "directionY", state.directionY);
        state.directionZ = decimal(motion, "directionZ", state.directionZ);
        state.sparkLengthCenter = (float) decimal(motion, "sparkLengthCenter", state.sparkLengthCenter);

        JsonObject appearance = object(root, "appearance");
        state.color.startColor = color(appearance, "startColor", color(root, "startColor", state.color.startColor));
        state.color.endColor = color(appearance, "endColor", color(root, "endColor", state.color.endColor));
        state.color.enabled = bool(appearance, "colorDataEnabled", bool(root, "colorDataEnabled", state.color.enabled));
        state.color.coefficient = (float) decimal(appearance, "colorCoefficient", state.color.coefficient);
        state.color.easing = easing(string(appearance, "colorEasing", state.color.easing.name), state.color.easing);
        state.transparency.enabled = optionalDataEnabled(appearance, "transparencyDataEnabled", "transparency");
        state.scale.enabled = optionalDataEnabled(appearance, "scaleDataEnabled", "scale");
        state.length.enabled = optionalDataEnabled(appearance, "lengthDataEnabled", "length");
        state.spin.enabled = optionalDataEnabled(appearance, "spinDataEnabled", "spin");
        state.scaleLengthLinked = bool(appearance, "scaleLengthLinked", state.scaleLengthLinked);
        state.spin.offset = (float) decimal(appearance, "spinOffset", state.spin.offset);
        readCurve(object(appearance, "transparency"), state.transparency.curve);
        readCurve(object(appearance, "scale"), state.scale.curve);
        readCurve(object(appearance, "length"), state.length.curve);
        readCurve(object(appearance, "spin"), state.spin.curve);
        if (state.scaleLengthLinked) {
            state.scale.curve.linked = true;
            state.length.curve.linked = true;
            state.length.curve.setLinkedValue(state.scale.curve.start);
        }

        JsonObject effects = object(root, "effects");
        state.screenshake.enabled = bool(effects, "screenshake", state.screenshake.enabled);
        state.screenshake.onBurst = bool(effects, "screenshakeOnBurst", state.screenshake.onBurst);
        state.screenshake.positioned = bool(effects, "screenshakePositioned", state.screenshake.positioned);
        state.screenshake.duration = integer(effects, "screenshakeDuration", state.screenshake.duration);
        state.screenshake.startStrength = (float) decimal(effects, "screenshakeStartStrength", state.screenshake.startStrength);
        state.screenshake.middleStrength = (float) decimal(effects, "screenshakeMiddleStrength", state.screenshake.middleStrength);
        state.screenshake.endStrength = (float) decimal(effects, "screenshakeEndStrength", state.screenshake.endStrength);
        state.screenshake.startEasing = easing(string(effects, "screenshakeStartEasing", state.screenshake.startEasing.name), state.screenshake.startEasing);
        state.screenshake.endEasing = easing(string(effects, "screenshakeEndEasing", state.screenshake.endEasing.name), state.screenshake.endEasing);
        state.screenshake.coefficient = (float) decimal(effects, "screenshakeCoefficient", state.screenshake.coefficient);
        state.screenshake.falloffDistance = (float) decimal(effects, "screenshakeFalloffDistance", state.screenshake.falloffDistance);
        state.screenshake.falloffEasing = easing(string(effects, "screenshakeFalloffEasing", state.screenshake.falloffEasing.name), state.screenshake.falloffEasing);
        EditorGroups.readProjectData(root, state);
        return state;
    }

    private static boolean optionalDataEnabled(JsonObject object, String enabledKey, String dataKey) {
        if (object.has(enabledKey)) {
            return bool(object, enabledKey, false);
        }
        return object.has(dataKey);
    }

    public String previewSnapshot() {
        return toJava() +
                "|colorData=" + color.enabled +
                "|curves=" + transparency.enabled + "," + scale.enabled + "," + length.enabled + "," + spin.enabled +
                "|live=" + autoPreview +
                "|playback=" + previewPlayback.label +
                "|follow=" + followCamera +
                "|fixed=" + formatNumber(fixedPreviewX) + "," + formatNumber(fixedPreviewY) + "," + formatNumber(fixedPreviewZ) +
                "|distribution=" + spawnDistribution.label +
                "|shape=" + formatNumber(shapeSizeX) + "," + formatNumber(shapeSizeY) + "," + formatNumber(shapeSizeZ) +
                "|interval=" + previewInterval +
                "|distance=" + formatNumber(previewDistance) +
                "|yOffset=" + formatNumber(previewYOffset);
    }

    private String behaviorJava() {
        String direction = "new Vec3(" + formatNumber(directionX) + ", " + formatNumber(directionY) + ", " + formatNumber(directionZ) + ")";
        return switch (behavior) {
            case BILLBOARD -> "BillboardParticleBehavior.INSTANCE";
            case DIRECTIONAL -> forcedDirection ? "DirectionalParticleBehavior.directional(" + direction + ")" : "DirectionalParticleBehavior.directional()";
            case POINTY_DIRECTIONAL -> forcedDirection ? "PointyDirectionalParticleBehavior.pointyDirectional(" + direction + ")" : "PointyDirectionalParticleBehavior.pointyDirectional()";
            case SPARK -> {
                String spark = "SparkParticleBehavior.sparkBehavior().setLengthCenter(" + formatNumber(sparkLengthCenter) + "f)";
                yield forcedDirection ? spark + ".setForcedDirection(" + direction + ")" : spark;
            }
        };
    }

    private String worldParticleJavaName() {
        return particle.javaName;
    }

    private void appendLighting(StringBuilder builder) {
        switch (lightMode) {
            case NATURAL -> builder.append("        .setNaturalLighting()\n");
            case CUSTOM -> builder.append("        .setLightLevel(").append(lightLevel).append(")\n");
            case FULL_BRIGHT -> builder.append("        .setFullBrightLighting()\n");
        }
    }

    private static JsonObject curveToJson(ParticleCurveData curve) {
        JsonObject object = new JsonObject();
        object.addProperty("start", curve.start);
        object.addProperty("middle", curve.middle);
        object.addProperty("end", curve.end);
        object.addProperty("coefficient", curve.coefficient);
        object.addProperty("trinary", curve.trinary);
        object.addProperty("linked", curve.linked);
        object.addProperty("endpointsLinked", curve.endpointsLinked);
        object.addProperty("easing", curve.startEasing.name);
        object.addProperty("endEasing", curve.endEasing.name);
        return object;
    }

    private static void readCurve(JsonObject object, ParticleCurveData curve) {
        if (object.size() == 0) {
            return;
        }
        curve.start = (float) decimal(object, "start", curve.start);
        curve.middle = (float) decimal(object, "middle", curve.middle);
        curve.end = (float) decimal(object, "end", curve.end);
        curve.coefficient = (float) decimal(object, "coefficient", curve.coefficient);
        curve.trinary = bool(object, "trinary", curve.trinary);
        curve.linked = bool(object, "linked", curve.linked);
        curve.endpointsLinked = bool(object, "endpointsLinked", curve.endpointsLinked);
        if (curve.endpointsLinked) {
            curve.setFinal(curve.start);
        }
        curve.startEasing = easing(string(object, "easing", curve.startEasing.name), curve.startEasing);
        curve.endEasing = easing(string(object, "endEasing", curve.endEasing.name), curve.endEasing);
    }

    private static JsonObject object(JsonObject object, String key) {
        if (object.has(key) && object.get(key).isJsonObject()) {
            return object.getAsJsonObject(key);
        }
        return new JsonObject();
    }

    private static String string(JsonObject object, String key, String fallback) {
        if (object.has(key) && object.get(key).isJsonPrimitive()) {
            return object.get(key).getAsString();
        }
        return fallback;
    }

    private static boolean bool(JsonObject object, String key, boolean fallback) {
        if (object.has(key) && object.get(key).isJsonPrimitive()) {
            return object.get(key).getAsBoolean();
        }
        return fallback;
    }

    private static int integer(JsonObject object, String key, int fallback) {
        if (object.has(key) && object.get(key).isJsonPrimitive()) {
            return object.get(key).getAsInt();
        }
        return fallback;
    }

    private static double decimal(JsonObject object, String key, double fallback) {
        if (object.has(key) && object.get(key).isJsonPrimitive()) {
            return object.get(key).getAsDouble();
        }
        return fallback;
    }

    private static double array(JsonObject object, String key, int index, double fallback) {
        if (object.has(key) && object.get(key).isJsonArray() && object.getAsJsonArray(key).size() > index) {
            return object.getAsJsonArray(key).get(index).getAsDouble();
        }
        return fallback;
    }

    private static int color(JsonObject object, String key, int fallback) {
        String value = string(object, key, "");
        if (value.isEmpty()) {
            return fallback;
        }
        String clean = value.startsWith("#") ? value.substring(1) : value;
        try {
            return Integer.parseUnsignedInt(clean, 16) & 0xFFFFFF;
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static String colorString(int color) {
        return "#" + String.format(Locale.ROOT, "%06X", color & 0xFFFFFF);
    }

    private static Easing easing(String name, Easing fallback) {
        Easing easing = Easing.valueOf(name);
        return easing != null ? easing : fallback;
    }

    private static <T extends Enum<T>> T enumByName(T[] values, String name, T fallback) {
        for (T value : values) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return fallback;
    }

    private static <T extends Enum<T>> T enumByLabel(T[] values, String name, Function<T, String> namer, T fallback) {
        for (T value : values) {
            if (value.name().equalsIgnoreCase(name) || namer.apply(value).equalsIgnoreCase(name)) {
                return value;
            }
        }
        return fallback;
    }

    private static String indent(String text, int spaces) {
        String prefix = " ".repeat(spaces);
        return prefix + text.replace("\n", "\n" + prefix);
    }

    private static float red(int rgb) {
        return ((rgb >> 16) & 255) / 255.0f;
    }

    private static float green(int rgb) {
        return ((rgb >> 8) & 255) / 255.0f;
    }

    private static float blue(int rgb) {
        return (rgb & 255) / 255.0f;
    }

    public static String formatNumber(float value) {
        return formatNumber((double) value);
    }

    public static String formatNumber(double value) {
        return String.format(Locale.ROOT, "%.4f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    public static String easingConstant(Easing easing) {
        return easing.name.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(Locale.ROOT);
    }
}
