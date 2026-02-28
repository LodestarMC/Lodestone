package team.lodestar.lodestone.systems.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import team.lodestar.lodestone.systems.blockentity.*;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class CachedBlockEntitySoundInstance<T extends LodestoneBlockEntity> extends LodestoneBlockEntitySoundInstance<T> {

    private static final Map<BlockPos, CachedBlockEntitySoundInstance<?>> ACTIVE_SOUNDS = new WeakHashMap<>();

    public CachedBlockEntitySoundInstance(T blockEntity, Supplier<SoundEvent> soundEvent, float volume, float pitch) {
        super(blockEntity, soundEvent.get(), volume, pitch);
    }

    @Override
    public void stop() {
        super.stop();
        ACTIVE_SOUNDS.remove(pos);
    }

    public static void playSound(LodestoneBlockEntity blockEntity, CachedBlockEntitySoundInstance<?> sound) {
        boolean success = false;
        if (ACTIVE_SOUNDS.containsKey(pos)) {
            var existingSound = ACTIVE_SOUNDS.get(pos);
            if (!existingSound.location.equals(sound.location)) {
                existingSound.stop();
                ACTIVE_SOUNDS.put(pos, sound);
                success = true;
            }
        }
        else {
            ACTIVE_SOUNDS.put(pos, sound);
            success = true;
        }
        if (success) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(sound);
        }
    }
}