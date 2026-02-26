package team.lodestar.lodestone.systems.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class LodestoneBlockEntitySoundInstance<T extends LodestoneBlockEntity> extends AbstractTickableSoundInstance {

    protected final T blockEntity;
    protected final BlockPos pos;

    public LodestoneBlockEntitySoundInstance(T blockEntity, SoundEvent soundEvent, float volume, float pitch) {
        super(soundEvent, SoundSource.BLOCKS, blockEntity.getLevel().getRandom());
        this.blockEntity = blockEntity;
        this.pos = blockEntity.getBlockPos();
        this.volume = volume;
        this.pitch = pitch;
        this.delay = 0;
        this.looping = true;
        this.x = pos.getX() + 0.5f;
        this.y = pos.getY() + 0.5f;
        this.z = pos.getZ() + 0.5f;
    }

    @Override
    public void tick() {
        if (blockEntity.isRemoved()) {
            stop();
        }
    }
}
