package team.lodestar.lodestone.helpers;

import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;

public class SoundHelper {


    public static void playSound(Entity target, SoundEvent soundEvent, float volume, float pitch) {
        playSound(target, soundEvent, target.getSoundSource(), volume, pitch);
    }

    /**
     * Plays a sound at a target's location, mimicking the behavior of {@link Player#playSound} but allowing for the sound to be heard even if not played on both logical sides.
     */
    public static void playSound(Entity target, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        target.level().playSound(null, target.getX(), target.getY(), target.getZ(), soundEvent, soundSource, volume, pitch);
    }
}
