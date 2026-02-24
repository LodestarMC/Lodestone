package team.lodestar.lodestone.systems.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.network.particle.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.network.particle.*;
import team.lodestar.lodestone.systems.particle.data.color.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class WeaponParticleEffectType<T extends WeaponParticleEffectType.WeaponParticleEffectData> extends NetworkedParticleEffectType<T> {

    public static class WeaponParticleEffectData implements NetworkedParticleEffectExtraData {
        public static final Codec<WeaponParticleEffectData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Vec3.CODEC.fieldOf("direction").forGetter(WeaponParticleEffectData::getDirection),
                Codec.BOOL.fieldOf("mirror").forGetter(WeaponParticleEffectData::isMirrored),
                Codec.FLOAT.fieldOf("slashRotation").forGetter(WeaponParticleEffectData::getSlashRotation)
        ).apply(instance, WeaponParticleEffectData::new));

        public static final StreamCodec<ByteBuf, WeaponParticleEffectData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        public static final WeaponParticleEffectData DEFAULT = new WeaponParticleEffectData(Vec3.ZERO, false, 0);
        private final Vec3 direction;
        private final boolean isMirrored;
        private final float slashRotation;

        public WeaponParticleEffectData(Vec3 direction, boolean isMirrored, float slashRotation) {
            this.direction = direction;
            this.isMirrored = isMirrored;
            this.slashRotation = slashRotation;
        }

        public WeaponParticleEffectData withDirection(Vec3 direction) {
            return modify(direction, isMirrored, slashRotation);
        }

        public WeaponParticleEffectData withMirror(boolean mirror) {
            return modify(direction, mirror, slashRotation);
        }

        public WeaponParticleEffectData withRotation(float slashRotation) {
            return modify(direction, isMirrored, slashRotation);
        }

        public WeaponParticleEffectData modify(Vec3 direction, boolean isMirrored, float slashRotation) {
            return new WeaponParticleEffectData(direction, isMirrored, slashRotation);
        }

        public Vec3 getDirection() {
            return direction;
        }

        public boolean isMirrored() {
            return isMirrored;
        }

        public float getSlashRotation() {
            return slashRotation;
        }

    }
    public static WeaponParticleEffectData createData(Vec3 direction, boolean mirror, float angle) {
        return new WeaponParticleEffectData(direction, mirror, angle);
    }

    @Override
    public Optional<StreamCodec<ByteBuf, ? extends NetworkedParticleEffectExtraData>> getExtraCodec() {
        return Optional.of(WeaponParticleEffectData.STREAM_CODEC);
    }

    @Override
    public Optional<? extends NetworkedParticleEffectExtraData> getDefaultExtraData() {
        return Optional.of(WeaponParticleEffectData.DEFAULT);
    }

    public WeaponParticleEffectType(String id) {
        super(id);
    }

    @Override
    public WeaponParticleEffectBuilder<T> createEffect(BlockPos position) {
        return createEffect().at(position);
    }

    @Override
    public WeaponParticleEffectBuilder<T> createEffect(Vec3 position) {
        return createEffect().at(position);
    }

    @Override
    public WeaponParticleEffectBuilder<T> createEffect(Entity target) {
        return createEffect().targets(target);
    }

    @Override
    public WeaponParticleEffectBuilder<T> createEffect() {
        return new WeaponParticleEffectBuilder<>(this);
    }

    @SuppressWarnings({"UnusedReturnValue", "unchecked"})
    public static class WeaponParticleEffectBuilder<T extends WeaponParticleEffectData> extends ParticleEffectBuilder<T> {

        protected Vec3 direction;
        protected Entity source;
        protected Entity target;
        protected boolean tiedToTarget;

        protected boolean isMirrored;
        protected float slashRotation;

        protected Vec3 absoluteOffset = Vec3.ZERO;
        protected float horizontalOffset, forwardOffset, upwardOffset;
        protected float horizontalDeviation, verticalDeviation, deviationAngle;
        protected boolean exactDeviationAngle;

        public WeaponParticleEffectBuilder(WeaponParticleEffectType<T> type) {
            super(type);
        }

        /**
         * Ties the effect to an entity.
         * If no target is given, the attacker's look angle will be used as the direction.
         *
         * @param source The entity initiating the effect
         */
        public WeaponParticleEffectBuilder<T> originatesFrom(Entity source) {
            this.source = source;
            return this;
        }

        /**
         * Aims the effect at a given entity.
         * If no source is given, the direction will be random.
         *
         * @param target The entity to aim the effect at
         */
        public WeaponParticleEffectBuilder<T> targets(Entity target) {
            this.target = target;
            return this;
        }

        /**
         * Sets a predetermined direction for the effect to use.
         * If a source or target is given, they will still be considered when determining the effect's position.
         *
         * @param direction The direction for the effect
         */
        public WeaponParticleEffectBuilder<T> aimedAt(Vec3 direction) {
            this.direction = direction;
            return this;
        }

        /**
         * Spawns the effect at the target's position offset towards the source instead.
         * Innate behavior has the effect spawn originating from the source.
         */
        public WeaponParticleEffectBuilder<T> tiedToTarget() {
            tiedToTarget = true;
            return this;
        }

        public WeaponParticleEffectBuilder<T> mirroredRandomly(RandomSource random) {
            return mirrored(random.nextBoolean());
        }

        public WeaponParticleEffectBuilder<T> mirrored() {
            return mirrored(true);
        }

        public WeaponParticleEffectBuilder<T> mirrored(boolean isMirrored) {
            this.isMirrored = isMirrored;
            return this;
        }

        public WeaponParticleEffectBuilder<T> randomSlashRotation(RandomSource random) {
            return slashRotation(random.nextFloat() * 6.28f);
        }

        public WeaponParticleEffectBuilder<T> verticalSlashRotation() {
            return slashRotation(1.57f);
        }

        public WeaponParticleEffectBuilder<T> slashRotation(float slashRotation) {
            this.slashRotation = slashRotation;
            return this;
        }

        public WeaponParticleEffectBuilder<T> setOffsetsFromHand(InteractionHand hand) {
            return horizontalOffset(hand.equals(InteractionHand.MAIN_HAND) ? 0.4f : -0.4f);
        }

        public WeaponParticleEffectBuilder<T> horizontalOffset(float horizontalOffset) {
            this.horizontalOffset = horizontalOffset;
            return this;
        }

        public WeaponParticleEffectBuilder<T> forwardOffset(float forwardOffset) {
            this.forwardOffset = forwardOffset;
            return this;
        }

        public WeaponParticleEffectBuilder<T> upwardOffset(float upwardOffset) {
            this.upwardOffset = upwardOffset;
            return this;
        }

        public WeaponParticleEffectBuilder<T> randomOffset(RandomSource random, float min, float max) {
            return absoluteOffset(new Vec3(
                    random.nextFloat() * (max - min) + min,
                    random.nextFloat() * (max - min) + min,
                    random.nextFloat() * (max - min) + min)
            );
        }

        public WeaponParticleEffectBuilder<T> absoluteOffset(Vec3 absoluteOffset) {
            this.absoluteOffset = absoluteOffset;
            return this;
        }


        public WeaponParticleEffectBuilder<T> deviation(float horizontalDeviation, float verticalDeviation, float deviationAngle) {
            return deviation(horizontalDeviation, verticalDeviation).deviationAngle(deviationAngle);
        }

        public WeaponParticleEffectBuilder<T> deviation(float horizontalDeviation, float verticalDeviation) {
            return horizontalDeviation(horizontalDeviation).verticalDeviation(verticalDeviation);
        }

        public WeaponParticleEffectBuilder<T> deviation(float deviation) {
            return horizontalDeviation(deviation).verticalDeviation(deviation);
        }

        public WeaponParticleEffectBuilder<T> horizontalDeviation(float horizontalDeviation) {
            this.horizontalDeviation = horizontalDeviation;
            return this;
        }

        public WeaponParticleEffectBuilder<T> verticalDeviation(float verticalDeviation) {
            this.verticalDeviation = verticalDeviation;
            return this;
        }

        public WeaponParticleEffectBuilder<T> randomDeviationAngle(RandomSource random) {
            return deviationAngle(random.nextFloat() * 6.28f);
        }

        public WeaponParticleEffectBuilder<T> deviationAngle(float deviationAngle) {
            this.deviationAngle = deviationAngle;
            this.exactDeviationAngle = true;
            return this;
        }

        protected Vec3 getEffectPosition(Vec3 direction) {
            Vec3 positionToUse = null;
            if (position != null) {
                positionToUse = position.getAsVector();
            } else {
                Entity anchor = null;
                if (tiedToTarget && target != null) {
                    anchor = target;
                } else if (source != null) {
                    anchor = source;
                }
                if (anchor != null) {
                    positionToUse = anchor.position().add(0, anchor.getBbHeight() / 2f, 0);
                }
            }
            if (positionToUse == null) {
                throw new IllegalArgumentException("Networked Weapon Particle Effect failed to provide a valid position to spawn at. This is a bug.");
            }

            float yRot = ((float) (Mth.atan2(direction.x, direction.z) * (double) (180F / (float) Math.PI)));
            float yaw = (float) Math.toRadians(yRot);
            var left = new Vec3(-Math.cos(yaw), 0, Math.sin(yaw));
            var up = left.cross(direction);
            var offset = absoluteOffset
                    .add(direction.scale(forwardOffset))
                    .add(up.scale(upwardOffset))
                    .add(left.scale(horizontalOffset));
            return positionToUse.add(offset);
        }

        protected Vec3 getEffectDirection(RandomSource random) {
            Vec3 directionToUse = direction;
            if (directionToUse == null) {
                if (target != null && source != null) {
                    directionToUse = target.position().subtract(source.position()).normalize();
                }
                else if (source != null) {
                    directionToUse = source.getLookAngle();
                }
                else {
                    double theta = random.nextDouble() * 2 * Math.PI;
                    double phi = Math.acos(2 * random.nextDouble() - 1);
                    double x = Math.sin(phi) * Math.cos(theta);
                    double y = Math.sin(phi) * Math.sin(theta);
                    double z = Math.cos(phi);
                    directionToUse = new Vec3(x, y, z);
                }
            }
            if (horizontalDeviation == 0 && verticalDeviation == 0) {
                return directionToUse;
            }
            float yRot = ((float) (Mth.atan2(directionToUse.x, directionToUse.z) * (double) (180F / (float) Math.PI)));
            float yaw = (float) Math.toRadians(yRot);
            var left = new Vec3(-Math.cos(yaw), 0, Math.sin(yaw));
            var up = left.cross(directionToUse);
            float leftOffset = horizontalDeviation;
            float upOffset = verticalDeviation;
            if (exactDeviationAngle) {
                leftOffset *= Mth.sin(deviationAngle);
                upOffset *= Mth.cos(deviationAngle);
            }
            return directionToUse
                    .add(left.scale(leftOffset))
                    .add(up.scale(upOffset))
                    .normalize();
        }

        public float getSlashRotation() {
            return slashRotation + (isMirrored ? 3.14f : 0);
        }

        protected void updateData(ServerLevel level) {
            Vec3 effectDirection = getEffectDirection(level.getRandom());
            Vec3 effectPosition = getEffectPosition(effectDirection);
            at(effectPosition);
            if (color == null) {
                color(ColorParticleData.createGrayParticleColor(level.getRandom()));
            }
            customData((T) getCustomData()
                    .withDirection(effectDirection)
                    .withMirror(isMirrored)
                    .withRotation(slashRotation));
        }

        @Override
        public WeaponParticleEffectBuilder<T> spawn(ServerLevel level) {
            updateData(level);
            return (WeaponParticleEffectBuilder<T>)super.spawn(level);
        }

        @Override
        public WeaponParticleEffectBuilder<T> at(BlockPos position) {
            return (WeaponParticleEffectBuilder<T>)super.at(position);
        }

        @Override
        public WeaponParticleEffectBuilder<T> at(Vec3 position) {
            return (WeaponParticleEffectBuilder<T>)super.at(position);
        }

        @Override
        public WeaponParticleEffectBuilder<T> at(Entity target) {
            return (WeaponParticleEffectBuilder<T>)super.at(target);
        }

        @Override
        public WeaponParticleEffectBuilder<T> at(NetworkedParticleEffectPositionData position) {
            return (WeaponParticleEffectBuilder<T>)super.at(position);
        }

        @Override
        public WeaponParticleEffectBuilder<T> color(Color color) {
            return (WeaponParticleEffectBuilder<T>)super.color(color);
        }

        @Override
        public WeaponParticleEffectBuilder<T> color(ColorParticleDataWrapper color) {
            return (WeaponParticleEffectBuilder<T>)super.color(color);
        }

        @Override
        public WeaponParticleEffectBuilder<T> color(List<? extends ColorParticleDataWrapper> colors) {
            return (WeaponParticleEffectBuilder<T>) super.color(colors);
        }

        @Override
        public WeaponParticleEffectBuilder<T> color(NetworkedParticleEffectColorData color) {
            return (WeaponParticleEffectBuilder<T>)super.color(color);
        }

        @Override
        public WeaponParticleEffectBuilder<T> customData(T extra) {
            return (WeaponParticleEffectBuilder<T>)super.customData(extra);
        }

        @Override
        public WeaponParticleEffectBuilder<T> spawn(Consumer<NetworkedParticleEffectPayload> sender) {
            return (WeaponParticleEffectBuilder<T>)super.spawn(sender);
        }
    }

}