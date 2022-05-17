package com.sammy.ortus.systems.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ForgeSoundType;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ExtendedSoundType extends ForgeSoundType {
    public ExtendedSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        super(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, hitSoundIn, fallSoundIn);
    }

    public void onPlayBreakSound(Level level, BlockPos pos) {
        level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, getBreakSound(), SoundSource.BLOCKS, (getVolume() + 1.0F) / 2.0F, getPitch() * 0.8F, false);
    }

    public void onPlayStepSound(Level level, BlockPos pos, SoundSource category) {
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), getStepSound(), category, getVolume() * 0.15F, getPitch());
    }

    public void onPlayPlaceSound(Level level, BlockPos pos, Player player) {
        level.playSound(player, pos, getPlaceSound(), SoundSource.BLOCKS, (getVolume() + 1.0F) / 2.0F, getPitch() * 0.8F);
    }

    @OnlyIn(value = Dist.CLIENT)
    public void onPlayHitSound(BlockPos pos) {
        Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(getHitSound(), SoundSource.BLOCKS, (getVolume() + 1.0F) / 8.0F, getPitch() * 0.5F, pos));
    }

    public void onPlayFallSound(Level level, BlockPos pos, SoundSource category) {
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), getFallSound(), category, getVolume() * 0.5F, getPitch() * 0.75f);
    }
}