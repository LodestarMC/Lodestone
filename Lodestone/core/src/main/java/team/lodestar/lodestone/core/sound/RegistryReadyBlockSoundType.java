package team.lodestar.lodestone.core.sound;

import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

@SuppressWarnings("unused")
public abstract class RegistryReadyBlockSoundType extends ExtendedSoundType {

    protected final DeferredHolder<SoundEvent, SoundEvent> breakSound;
    protected final DeferredHolder<SoundEvent, SoundEvent> stepSound;
    protected final DeferredHolder<SoundEvent, SoundEvent> placeSound;
    protected final DeferredHolder<SoundEvent, SoundEvent> hitSound;
    protected final DeferredHolder<SoundEvent, SoundEvent> fallSound;

    public RegistryReadyBlockSoundType(Function<SoundEvent, DeferredHolder<SoundEvent, SoundEvent>> registry, Function<String, ResourceLocation> path, String name) {
        this(registry, path, name, 1f, 1f);
    }

    public RegistryReadyBlockSoundType(Function<SoundEvent, DeferredHolder<SoundEvent, SoundEvent>> registry, Function<String, ResourceLocation> path, String name, float volume, float pitch) {
        super(volume, pitch, null, null, null, null, null);
        breakSound = registry.apply(SoundEvent.createVariableRangeEvent(path.apply(name + "_break")));
        placeSound = registry.apply(SoundEvent.createVariableRangeEvent(path.apply(name + "_place")));
        stepSound = registry.apply(SoundEvent.createVariableRangeEvent(path.apply(name + "_step")));
        hitSound = registry.apply(SoundEvent.createVariableRangeEvent(path.apply(name + "_hit")));
        fallSound = registry.apply(SoundEvent.createVariableRangeEvent(path.apply(name + "_fall")));
    }

    @Override
    public @NotNull SoundEvent getBreakSound() {
        return breakSound.get();
    }

    @Override
    public @NotNull SoundEvent getStepSound() {
        return stepSound.get();
    }

    @Override
    public @NotNull SoundEvent getPlaceSound() {
        return placeSound.get();
    }

    @Override
    public @NotNull SoundEvent getHitSound() {
        return hitSound.get();
    }

    @Override
    public @NotNull SoundEvent getFallSound() {
        return fallSound.get();
    }

    public DeferredHolder<SoundEvent, SoundEvent> getBreakSoundHolder() {
        return breakSound;
    }

    public DeferredHolder<SoundEvent, SoundEvent> getStepSoundHolder() {
        return stepSound;
    }

    public DeferredHolder<SoundEvent, SoundEvent> getPlaceSoundHolder() {
        return placeSound;
    }

    public DeferredHolder<SoundEvent, SoundEvent> getHitSoundHolder() {
        return hitSound;
    }

    public DeferredHolder<SoundEvent, SoundEvent> getFallSoundHolder() {
        return fallSound;
    }
}