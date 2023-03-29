package team.lodestar.lodestone.systems.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class SoundMotifSoundType extends ExtendedSoundType {
    public final Supplier<SoundEvent> motifSound;
    public SoundMotifSoundType(Supplier<SoundEvent> motifSound, float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        super(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, hitSoundIn, fallSoundIn);
        this.motifSound = motifSound;
    }

    @Override
    public void onPlayBreakSound(Level level, BlockPos pos) {
        level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, motifSound.get(), SoundSource.BLOCKS, (getVolume() + 1.0F) / 4.0F, getPitch() * 1.9F, false);
    }

    @Override
    public void onPlayStepSound(Level level, BlockPos pos, BlockState state, SoundSource category) {
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), motifSound.get(), category, getVolume() * 0.2F, getPitch() * 2F);
    }

    @Override
    public void onPlayPlaceSound(Level level, BlockPos pos, Player player) {
        level.playSound(player, pos, motifSound.get(), SoundSource.BLOCKS, (getVolume() + 1.0F) / 4.0F, getPitch() * 1.8F);
    }

    @Override
    public void onPlayHitSound(BlockPos pos) {
        Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(motifSound.get(), SoundSource.BLOCKS, (getVolume() + 1.0F) / 8.0F, getPitch() * 1.75F, SoundInstance.createUnseededRandom(), pos));
    }

    @Override
    public void onPlayFallSound(Level level, BlockPos pos, SoundSource category) {
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), motifSound.get(), category, getVolume() * 0.5F, getPitch() * 1.75F);
    }
}
