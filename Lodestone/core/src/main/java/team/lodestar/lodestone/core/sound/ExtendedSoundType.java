package team.lodestar.lodestone.core.sound;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.DeferredSoundType;

import java.util.function.Supplier;

/**
 * An ExtendedSoundType is an extension of DeferredSoundType, that provides hooks for when each individual block sound is played.
 */
@SuppressWarnings("unused")
public class ExtendedSoundType extends DeferredSoundType {

    public ExtendedSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        super(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, hitSoundIn, fallSoundIn);
    }

    /**
     * Called by mixin injection when the block broken sound plays in
     * <p>{@link net.minecraft.client.renderer.LevelRenderer#levelEvent(int, BlockPos, int)}
     */
    public void onPlayBreakSound(Level level, Player player, BlockPos pos, BlockState state, EquivalentEffectSoundAcceptor acceptor) {
    }

    /**
     * Called by mixin injection when an entity triggers the block step sound in
     * <p>{@link net.minecraft.world.entity.Entity#playStepSound(BlockPos, BlockState)}
     */
    public void onPlayStepSound(Level level, Entity entity, BlockPos pos, BlockState state, EquivalentEffectSoundAcceptor acceptor) {
    }

    /**
     * Called by mixin injection when a player triggers the block place sound in
     * <p>{@link net.minecraft.world.item.BlockItem#place(BlockPlaceContext)}
     */
    public void onPlayPlaceSound(Level level, Player player, BlockPos pos, EquivalentEffectSoundAcceptor acceptor) {
    }

    /**
     * Called by mixin injection when the block breaking progress sound is played in
     * <p> {@link net.minecraft.client.multiplayer.MultiPlayerGameMode#continueDestroyBlock(BlockPos, Direction)}
     */
    public void onPlayHitSound(Level level, Player player, BlockPos pos, EquivalentEffectSoundAcceptor acceptor) {

    }

    /**
     * Called by mixin injection when an entity plays the fall sound in
     * <p> {@link LivingEntity#playBlockFallSound()}
     */
    public void onPlayFallSound(Level level, LivingEntity entity, BlockPos pos, EquivalentEffectSoundAcceptor acceptor) {
    }

    /**
     * An interface that serves as a context-appropriate vanilla-matching playSound call.
     * Volume and Pitch parameters will be modified in the same way as vanilla does it for block sounds.
     * Avoid this if you want more precise control over volume/pitch.
     */
    public interface EquivalentEffectSoundAcceptor {
        default void playSound(Supplier<SoundEvent> event, float volume, float pitch) {
            playSound(event.get(), volume, pitch);
        }
        void playSound(SoundEvent event, float volume, float pitch);
    }
}