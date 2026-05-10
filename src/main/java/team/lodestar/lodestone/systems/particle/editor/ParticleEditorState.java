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
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeBuilder;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;
import team.lodestar.lodestone.systems.particle.world.behaviors.BillboardParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.DirectionalParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.LodestoneParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.PointyDirectionalParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.SparkParticleBehavior;

import java.util.Locale;
import java.util.function.Function;

class ParticleEditorState {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    ParticlePreset particle;
    ScreenParticlePreset screenParticle;
    ParticleOutputMode outputMode;
    SpawnDistributionMode spawnDistribution;
    PreviewPlaybackMode previewPlayback;
    RenderMode renderMode;
    ScreenRenderMode screenRenderMode;
    RenderLayerMode renderLayer;
    BehaviorMode behavior;
    SimpleParticleOptions.ParticleSpritePicker spritePicker;
    LightMode lightMode;

    boolean depthFade;
    boolean forceSpawn;
    boolean noClip;
    boolean autoPreview;
    boolean followCamera;
    boolean shapeOverlay;
    boolean colorDataEnabled;
    boolean transparencyDataEnabled;
    boolean scaleDataEnabled;
    boolean lengthDataEnabled;
    boolean spinDataEnabled;
    boolean screenshakeEnabled;
    boolean screenshakeOnBurst;
    boolean screenshakePositioned;
    boolean scaleLengthLinked;
    int lightLevel;
    int spawnCount;
    int previewInterval;
    int lifetime;
    int lifeDelay;
    int screenshakeDuration;

    double previewDistance;
    double previewYOffset;
    double fixedPreviewX;
    double fixedPreviewY;
    double fixedPreviewZ;
    double motionX;
    double motionY;
    double motionZ;
    double randomMotionX;
    double randomMotionY;
    double randomMotionZ;
    double randomOffsetX;
    double randomOffsetY;
    double randomOffsetZ;
    double shapeSizeX;
    double shapeSizeY;
    double shapeSizeZ;
    double directionX;
    double directionY;
    double directionZ;
    double screenX;
    double screenY;
    double stackTrackXOffset;
    double stackTrackYOffset;

    float gravity;
    float friction;
    float lifetimeModifier;
    float lifeDelayModifier;
    float gravityModifier;
    float frictionModifier;
    float sparkLengthCenter;
    float colorCoefficient;
    float spinOffset;
    float screenshakeStartStrength;
    float screenshakeMiddleStrength;
    float screenshakeEndStrength;
    float screenshakeCoefficient;
    float screenshakeFalloffDistance;

    boolean forcedDirection;
    boolean screenTrackStack;
    int startColor;
    int endColor;
    Easing colorEasing;
    Easing screenshakeStartEasing;
    Easing screenshakeEndEasing;
    Easing screenshakeFalloffEasing;
    ParticleCurveData transparency;
    ParticleCurveData scale;
    ParticleCurveData length;
    ParticleCurveData spin;

    ParticleEditorState() {
        reset();
    }

    void reset() {
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
        enableVisualData();
        screenshakeEnabled = false;
        screenshakeOnBurst = false;
        screenshakePositioned = false;
        scaleLengthLinked = false;
        lightLevel = RenderHelper.FULL_BRIGHT;
        spawnCount = 4;
        previewInterval = 3;
        lifetime = 28;
        lifeDelay = 0;
        screenshakeDuration = 20;
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
        startColor = 0x79D9FF;
        endColor = 0xFFFFFF;
        colorCoefficient = 1.0f;
        colorEasing = Easing.SINE_IN_OUT;
        spinOffset = 0.0f;
        screenshakeStartStrength = 0.25f;
        screenshakeMiddleStrength = 0.0f;
        screenshakeEndStrength = 0.0f;
        screenshakeCoefficient = 1.0f;
        screenshakeFalloffDistance = 16.0f;
        screenshakeStartEasing = Easing.SINE_OUT;
        screenshakeEndEasing = Easing.SINE_IN;
        screenshakeFalloffEasing = Easing.SINE_IN_OUT;
        transparency = new ParticleCurveData(0.9f, 0.0f, 0.0f, false);
        transparency.startEasing = Easing.SINE_IN;
        scale = new ParticleCurveData(0.18f, 0.0f, 0.0f, false);
        scale.linked = true;
        scale.setLinkedValue(scale.start);
        scale.startEasing = Easing.SINE_IN_OUT;
        length = new ParticleCurveData(0.18f, 0.0f, 0.0f, false);
        length.linked = true;
        length.setLinkedValue(length.start);
        length.startEasing = Easing.SINE_IN_OUT;
        spin = new ParticleCurveData(0.0f, 0.0f, 0.0f, false);
    }

    WorldParticleBuilder createBuilder() {
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
        if (colorDataEnabled) {
            builder.setColorData(ColorParticleData.create(red(startColor), green(startColor), blue(startColor), red(endColor), green(endColor), blue(endColor))
                    .setCoefficient(colorCoefficient)
                    .setEasing(colorEasing));
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

    ScreenParticleBuilder createScreenBuilder(ScreenParticleHolder target) {
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
        if (colorDataEnabled) {
            builder.setColorData(ColorParticleData.create(red(startColor), green(startColor), blue(startColor), red(endColor), green(endColor), blue(endColor))
                    .setCoefficient(colorCoefficient)
                    .setEasing(colorEasing));
        }
        applyCurveData(builder);
        if (spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
            builder.setRandomOffset(randomOffsetX, randomOffsetY);
        }
        return builder;
    }

    private void applyCurveData(WorldParticleBuilder builder) {
        if (transparencyDataEnabled) {
            builder.setTransparencyData(transparency.buildGeneric());
        }
        if (scaleDataEnabled) {
            builder.setScaleData(scale.buildGeneric());
        }
        if (lengthDataEnabled) {
            builder.setLengthData(length.buildGeneric());
        }
        if (spinDataEnabled) {
            builder.setSpinData(spin.buildSpin(spinOffset));
        }
    }

    private void applyCurveData(ScreenParticleBuilder builder) {
        if (transparencyDataEnabled) {
            builder.setTransparencyData(transparency.buildGeneric());
        }
        if (scaleDataEnabled) {
            builder.setScaleData(scale.buildGeneric());
        }
        if (lengthDataEnabled) {
            builder.setLengthData(length.buildGeneric());
        }
        if (spinDataEnabled) {
            builder.setSpinData(spin.buildSpin(spinOffset));
        }
    }

    Vec3 fixedPreviewPosition() {
        return new Vec3(fixedPreviewX, fixedPreviewY, fixedPreviewZ);
    }

    void setFixedPreviewPosition(Vec3 position) {
        fixedPreviewX = position.x;
        fixedPreviewY = position.y;
        fixedPreviewZ = position.z;
    }

    void enableVisualData() {
        colorDataEnabled = true;
        transparencyDataEnabled = true;
        scaleDataEnabled = true;
        lengthDataEnabled = true;
        spinDataEnabled = true;
    }

    Vec3 createWorldDistributionOffset(RandomSource random) {
        return createDistributionOffset(random, false);
    }

    Vec3 createScreenDistributionOffset(RandomSource random) {
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

    ScreenshakeInstance createScreenshake(Vec3 center) {
        ScreenshakeBuilder builder = ScreenshakeBuilder.create()
                .setDuration(screenshakeDuration)
                .setStrength(screenshakeStartStrength, screenshakeMiddleStrength, screenshakeEndStrength)
                .setEasing(screenshakeStartEasing, screenshakeEndEasing)
                .setCoefficient(screenshakeCoefficient);
        if (screenshakePositioned) {
            builder.placedAt(center, screenshakeFalloffDistance, screenshakeFalloffEasing);
        }
        return builder.build();
    }

    LodestoneParticleBehavior createBehavior() {
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

    String toJava() {
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
        if (!screenshakeEnabled || !screenshakeOnBurst) {
            return;
        }
        builder.append("\n");
        builder.append("ScreenshakeHandler.addScreenshake(ScreenshakeBuilder.create()\n");
        builder.append(".setDuration(").append(screenshakeDuration).append(")\n");
        builder.append(".setStrength(").append(formatNumber(screenshakeStartStrength)).append("f, ")
                .append(formatNumber(screenshakeMiddleStrength)).append("f, ")
                .append(formatNumber(screenshakeEndStrength)).append("f)\n");
        builder.append(".setEasing(Easing.").append(easingConstant(screenshakeStartEasing)).append(", Easing.")
                .append(easingConstant(screenshakeEndEasing)).append(")\n");
        builder.append(".setCoefficient(").append(formatNumber(screenshakeCoefficient)).append("f)");
        if (screenshakePositioned && centerExpression != null) {
            builder.append("\n.placedAt(").append(centerExpression).append(", ")
                    .append(formatNumber(screenshakeFalloffDistance)).append("f, Easing.")
                    .append(easingConstant(screenshakeFalloffEasing)).append(")");
        }
        builder.append("\n        .build());");
    }

    private void appendColorJava(StringBuilder builder) {
        if (!colorDataEnabled) {
            return;
        }
        builder.append(".setColorData(ColorParticleData.create(")
                .append(formatNumber(red(startColor))).append("f, ")
                .append(formatNumber(green(startColor))).append("f, ")
                .append(formatNumber(blue(startColor))).append("f, ")
                .append(formatNumber(red(endColor))).append("f, ")
                .append(formatNumber(green(endColor))).append("f, ")
                .append(formatNumber(blue(endColor))).append("f)")
                .append(".setCoefficient(").append(formatNumber(colorCoefficient)).append("f)")
                .append(".setEasing(Easing.").append(easingConstant(colorEasing)).append("))\n");
    }

    private void appendCurveJava(StringBuilder builder) {
        if (transparencyDataEnabled) {
            builder.append("        .setTransparencyData(").append(transparency.toJavaGeneric("GenericParticleData")).append(")\n");
        }
        if (scaleDataEnabled) {
            builder.append("        .setScaleData(").append(scale.toJavaGeneric("GenericParticleData")).append(")\n");
        }
        if (lengthDataEnabled) {
            builder.append("        .setLengthData(").append(length.toJavaGeneric("GenericParticleData")).append(")\n");
        }
        if (spinDataEnabled) {
            builder.append("        .setSpinData(").append(spin.toJavaGeneric("SpinParticleData")).append(".setSpinOffset(").append(formatNumber(spinOffset)).append("f))\n");
        }
    }

    String toProjectJson(String name) {
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
        appearance.addProperty("startColor", colorString(startColor));
        appearance.addProperty("endColor", colorString(endColor));
        appearance.addProperty("colorDataEnabled", colorDataEnabled);
        appearance.addProperty("colorCoefficient", colorCoefficient);
        appearance.addProperty("colorEasing", colorEasing.name);
        appearance.addProperty("transparencyDataEnabled", transparencyDataEnabled);
        appearance.addProperty("scaleDataEnabled", scaleDataEnabled);
        appearance.addProperty("lengthDataEnabled", lengthDataEnabled);
        appearance.addProperty("spinDataEnabled", spinDataEnabled);
        appearance.addProperty("scaleLengthLinked", scaleLengthLinked);
        appearance.addProperty("spinOffset", spinOffset);
        appearance.add("transparency", curveToJson(transparency));
        appearance.add("scale", curveToJson(scale));
        appearance.add("length", curveToJson(length));
        appearance.add("spin", curveToJson(spin));
        root.add("appearance", appearance);

        JsonObject effects = new JsonObject();
        effects.addProperty("screenshake", screenshakeEnabled);
        effects.addProperty("screenshakeOnBurst", screenshakeOnBurst);
        effects.addProperty("screenshakePositioned", screenshakePositioned);
        effects.addProperty("screenshakeDuration", screenshakeDuration);
        effects.addProperty("screenshakeStartStrength", screenshakeStartStrength);
        effects.addProperty("screenshakeMiddleStrength", screenshakeMiddleStrength);
        effects.addProperty("screenshakeEndStrength", screenshakeEndStrength);
        effects.addProperty("screenshakeStartEasing", screenshakeStartEasing.name);
        effects.addProperty("screenshakeEndEasing", screenshakeEndEasing.name);
        effects.addProperty("screenshakeCoefficient", screenshakeCoefficient);
        effects.addProperty("screenshakeFalloffDistance", screenshakeFalloffDistance);
        effects.addProperty("screenshakeFalloffEasing", screenshakeFalloffEasing.name);
        root.add("effects", effects);
        return GSON.toJson(root);
    }

    String toJavaFile(String className) {
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

    ParticleEditorState copy() {
        return fromProjectJson(toProjectJson("copy"));
    }

    static String readProjectName(String json, String fallback) {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            return string(root, "name", fallback);
        } catch (RuntimeException ignored) {
            return fallback;
        }
    }

    static ParticleEditorState fromProjectJson(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        ParticleEditorState state = new ParticleEditorState();
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
        state.startColor = color(appearance, "startColor", color(root, "startColor", state.startColor));
        state.endColor = color(appearance, "endColor", color(root, "endColor", state.endColor));
        state.colorDataEnabled = bool(appearance, "colorDataEnabled", bool(root, "colorDataEnabled", state.colorDataEnabled));
        state.colorCoefficient = (float) decimal(appearance, "colorCoefficient", state.colorCoefficient);
        state.colorEasing = easing(string(appearance, "colorEasing", state.colorEasing.name), state.colorEasing);
        state.transparencyDataEnabled = optionalDataEnabled(appearance, "transparencyDataEnabled", "transparency");
        state.scaleDataEnabled = optionalDataEnabled(appearance, "scaleDataEnabled", "scale");
        state.lengthDataEnabled = optionalDataEnabled(appearance, "lengthDataEnabled", "length");
        state.spinDataEnabled = optionalDataEnabled(appearance, "spinDataEnabled", "spin");
        state.scaleLengthLinked = bool(appearance, "scaleLengthLinked", state.scaleLengthLinked);
        state.spinOffset = (float) decimal(appearance, "spinOffset", state.spinOffset);
        readCurve(object(appearance, "transparency"), state.transparency);
        readCurve(object(appearance, "scale"), state.scale);
        readCurve(object(appearance, "length"), state.length);
        readCurve(object(appearance, "spin"), state.spin);
        state.enableVisualData();
        if (state.scaleLengthLinked) {
            state.scale.linked = true;
            state.length.linked = true;
            state.length.setLinkedValue(state.scale.start);
        }

        JsonObject effects = object(root, "effects");
        state.screenshakeEnabled = bool(effects, "screenshake", state.screenshakeEnabled);
        state.screenshakeOnBurst = bool(effects, "screenshakeOnBurst", state.screenshakeOnBurst);
        state.screenshakePositioned = bool(effects, "screenshakePositioned", state.screenshakePositioned);
        state.screenshakeDuration = integer(effects, "screenshakeDuration", state.screenshakeDuration);
        state.screenshakeStartStrength = (float) decimal(effects, "screenshakeStartStrength", state.screenshakeStartStrength);
        state.screenshakeMiddleStrength = (float) decimal(effects, "screenshakeMiddleStrength", state.screenshakeMiddleStrength);
        state.screenshakeEndStrength = (float) decimal(effects, "screenshakeEndStrength", state.screenshakeEndStrength);
        state.screenshakeStartEasing = easing(string(effects, "screenshakeStartEasing", state.screenshakeStartEasing.name), state.screenshakeStartEasing);
        state.screenshakeEndEasing = easing(string(effects, "screenshakeEndEasing", state.screenshakeEndEasing.name), state.screenshakeEndEasing);
        state.screenshakeCoefficient = (float) decimal(effects, "screenshakeCoefficient", state.screenshakeCoefficient);
        state.screenshakeFalloffDistance = (float) decimal(effects, "screenshakeFalloffDistance", state.screenshakeFalloffDistance);
        state.screenshakeFalloffEasing = easing(string(effects, "screenshakeFalloffEasing", state.screenshakeFalloffEasing.name), state.screenshakeFalloffEasing);
        return state;
    }

    private static boolean optionalDataEnabled(JsonObject object, String enabledKey, String dataKey) {
        if (object.has(enabledKey)) {
            return bool(object, enabledKey, false);
        }
        return object.has(dataKey);
    }

    String previewSnapshot() {
        return toJava() +
                "|colorData=" + colorDataEnabled +
                "|curves=" + transparencyDataEnabled + "," + scaleDataEnabled + "," + lengthDataEnabled + "," + spinDataEnabled +
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

    static String formatNumber(float value) {
        return formatNumber((double) value);
    }

    static String formatNumber(double value) {
        return String.format(Locale.ROOT, "%.4f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    static String easingConstant(Easing easing) {
        return easing.name.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase(Locale.ROOT);
    }
}
