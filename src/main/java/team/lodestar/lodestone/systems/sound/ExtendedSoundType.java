package team.lodestar.lodestone.systems.sound;

import io.github.fabricators_of_create.porting_lib.util.LazySoundType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/**
 * An ExtendedSoundType is a simple extension of ForgeSoundType, also providing hooks for when each individual sound is played.
 */
public class ExtendedSoundType extends LazySoundType {
    public ExtendedSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        super(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, hitSoundIn, fallSoundIn);
    }


    /**
     * Called by mixin injection when the block broken sound plays in
     * <p>{@link net.minecraft.client.renderer.LevelRenderer#levelEvent(int, BlockPos, int)}
     * <p> Example Implementation, matches the original logic of the parent sound being played:
     * <pre>{@code level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, getBreakSound(), SoundSource.BLOCKS, (getVolume() + 1.0F) / 2.0F, getPitch() * 0.8F, false);}</pre>
     */
    public void onPlayBreakSound(Level level, BlockPos pos) {
    }

    /**
     * Called by mixin injection when an entity triggers the block step sound in
     * <p>{@link net.minecraft.world.entity.Entity#playStepSound(BlockPos, BlockState)} (BlockPos, BlockState)}
     * <p> Example Implementation, matches the original logic of the parent sound being played:
     * <pre>{@code level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), getStepSound(), category, getVolume() * 0.15F, getPitch());}</pre>
     */
    public void onPlayStepSound(Level level, BlockPos pos, BlockState state, SoundSource category) {
    }

    /**
     * Called by mixin injection when a player triggers the block place sound in
     * <p>{@link net.minecraft.world.item.BlockItem#place(BlockPlaceContext)}
     * <p> Example Implementation, matches the original logic of the parent sound being played:
     * <pre>{@code level.playSound(player, pos, getPlaceSound(), SoundSource.BLOCKS, (getVolume() + 1.0F) / 2.0F, getPitch() * 0.8F);}</pre>
     */
    public void onPlayPlaceSound(Level level, BlockPos pos, Player player) {
    }

    /**
     * Called by mixin injection when the block breaking progress sound is played in
     * <p> {@link net.minecraft.client.multiplayer.MultiPlayerGameMode#continueDestroyBlock(BlockPos, Direction)}
     * <p> Example Implementation, matches the original logic of the parent sound being played:
     * <pre>{@code Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(getHitSound(), SoundSource.BLOCKS, (getVolume() + 1.0F) / 8.0F, getPitch() * 0.5F, pos));}</pre>
     */
    @Environment(EnvType.CLIENT)
    public void onPlayHitSound(BlockPos pos) {

    }

    /**
     * Called by mixin injection when an entity plays the fall sound in
     * <p> {@link LivingEntity#playBlockFallSound()}
     * <p> Example Implementation, matches the original logic of the parent sound being played:
     * <pre>{@code level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), getFallSound(), category, getVolume() * 0.5F, getPitch() * 0.75f);}</pre>
     */
    public void onPlayFallSound(Level level, BlockPos pos, SoundSource category) {
    }
}