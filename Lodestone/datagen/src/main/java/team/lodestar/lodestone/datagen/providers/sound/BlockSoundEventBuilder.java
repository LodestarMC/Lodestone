package team.lodestar.lodestone.datagen.providers.sound;

import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import team.lodestar.lodestone.core.sound.RegistryReadyBlockSoundType;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class BlockSoundEventBuilder {

    protected final String path;

    protected final SoundOptions breakSoundOptions;
    protected final SoundOptions stepSoundOptions;
    protected final SoundOptions placeSoundOptions;
    protected final SoundOptions hitSoundOptions;
    protected final SoundOptions fallSoundOptions;

    public static BlockSoundEventBuilder create(String path, RegistryReadyBlockSoundType soundType) {
        return new BlockSoundEventBuilder(path, soundType);
    }

    public BlockSoundEventBuilder(String path, RegistryReadyBlockSoundType soundType) {
        this(path, soundType.getBreakSoundHolder(), soundType.getStepSoundHolder(), soundType.getPlaceSoundHolder(), soundType.getHitSoundHolder(), soundType.getFallSoundHolder());
    }

    public BlockSoundEventBuilder(String path,
                                  Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound, Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound) {
        this.path = path;
        this.breakSoundOptions = new SoundOptions(breakSound, "break");
        this.stepSoundOptions = new SoundOptions(stepSound, "step");
        this.placeSoundOptions = new SoundOptions(placeSound, "place");
        this.hitSoundOptions = new SoundOptions(hitSound, "hit");
        this.fallSoundOptions = new SoundOptions(fallSound, "fall");
    }

    public BlockSoundEventBuilder modifySoundDefinitions(Consumer<SoundDefinition> modifier) {
        return this
                .modifyBreakSoundDefinition(modifier)
                .modifyStepSoundDefinition(modifier)
                .modifyPlaceSoundDefinition(modifier)
                .modifyHitSoundDefinition(modifier)
                .modifyFallSoundDefinition(modifier);
    }

    public BlockSoundEventBuilder addBreakPlaceSounds(SoundDefinition.Sound... sounds) {
        return modifyBreakPlaceSoundDefinitions(s -> s.with(sounds));
    }

    public BlockSoundEventBuilder addStepHitFallSounds(SoundDefinition.Sound... sounds) {
        return modifyStepHitFallSoundDefinitions(s -> s.with(sounds));
    }

    public BlockSoundEventBuilder modifyBreakPlaceSoundDefinitions(Consumer<SoundDefinition> modifier) {
        return modifyBreakSoundDefinition(modifier).modifyPlaceSoundDefinition(modifier);
    }

    public BlockSoundEventBuilder modifyStepHitFallSoundDefinitions(Consumer<SoundDefinition> modifier) {
        return modifyStepSoundDefinition(modifier).modifyHitSoundDefinition(modifier).modifyFallSoundDefinition(modifier);
    }

    public BlockSoundEventBuilder modifyBreakSoundDefinition(Consumer<SoundDefinition> modifier) {
        breakSoundOptions.modifyDefinition(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyStepSoundDefinition(Consumer<SoundDefinition> modifier) {
        stepSoundOptions.modifyDefinition(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyPlaceSoundDefinition(Consumer<SoundDefinition> modifier) {
        placeSoundOptions.modifyDefinition(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyHitSoundDefinition(Consumer<SoundDefinition> modifier) {
        hitSoundOptions.modifyDefinition(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyFallSoundDefinition(Consumer<SoundDefinition> modifier) {
        fallSoundOptions.modifyDefinition(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifySounds(Consumer<SoundDefinition.Sound> modifier) {
        return this
                .modifyBreakSound(modifier)
                .modifyStepSound(modifier)
                .modifyPlaceSound(modifier)
                .modifyHitSound(modifier)
                .modifyFallSound(modifier);
    }

    public BlockSoundEventBuilder modifyBreakPlaceSounds(Consumer<SoundDefinition.Sound> modifier) {
        return modifyBreakSound(modifier).modifyPlaceSound(modifier);
    }

    public BlockSoundEventBuilder modifyStepHitFallSounds(Consumer<SoundDefinition.Sound> modifier) {
        return modifyStepSound(modifier).modifyHitSound(modifier).modifyFallSound(modifier);
    }

    public BlockSoundEventBuilder modifyBreakSound(Consumer<SoundDefinition.Sound> modifier) {
        breakSoundOptions.modifySound(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyStepSound(Consumer<SoundDefinition.Sound> modifier) {
        stepSoundOptions.modifySound(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyPlaceSound(Consumer<SoundDefinition.Sound> modifier) {
        placeSoundOptions.modifySound(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyHitSound(Consumer<SoundDefinition.Sound> modifier) {
        hitSoundOptions.modifySound(modifier);
        return this;
    }

    public BlockSoundEventBuilder modifyFallSound(Consumer<SoundDefinition.Sound> modifier) {
        fallSoundOptions.modifySound(modifier);
        return this;
    }

    public BlockSoundEventBuilder setBreakPlaceSoundPaths(String path) {
        return setBreakSoundPath(path).setPlaceSoundPath(path);
    }

    public BlockSoundEventBuilder setStepHitFallSoundPaths(String path) {
        return setStepSoundPath(path).setHitSoundPath(path).setFallSoundPath(path);
    }

    public BlockSoundEventBuilder setBreakSoundPath(String path) {
        breakSoundOptions.replaceSoundPath(path);
        return this;
    }

    public BlockSoundEventBuilder setStepSoundPath(String path) {
        stepSoundOptions.replaceSoundPath(path);
        return this;
    }

    public BlockSoundEventBuilder setPlaceSoundPath(String path) {
        placeSoundOptions.replaceSoundPath(path);
        return this;
    }

    public BlockSoundEventBuilder setHitSoundPath(String path) {
        hitSoundOptions.replaceSoundPath(path);
        return this;
    }

    public BlockSoundEventBuilder setFallSoundPath(String path) {
        fallSoundOptions.replaceSoundPath(path);
        return this;
    }


    public BlockSoundEventBuilder setBreakPlaceSoundNames(String name) {
        return setBreakSoundName(name).setPlaceSoundName(name);
    }

    public BlockSoundEventBuilder setStepHitFallSoundNames(String name) {
        return setStepSoundName(name).setHitSoundName(name).setFallSoundName(name);
    }

    public BlockSoundEventBuilder setBreakSoundName(String name) {
        breakSoundOptions.replaceSoundName(name);
        return this;
    }

    public BlockSoundEventBuilder setStepSoundName(String name) {
        stepSoundOptions.replaceSoundName(name);
        return this;
    }

    public BlockSoundEventBuilder setPlaceSoundName(String name) {
        placeSoundOptions.replaceSoundName(name);
        return this;
    }

    public BlockSoundEventBuilder setHitSoundName(String name) {
        hitSoundOptions.replaceSoundName(name);
        return this;
    }

    public BlockSoundEventBuilder setFallSoundName(String name) {
        fallSoundOptions.replaceSoundName(name);
        return this;
    }

    public void addSounds() {
        add(breakSoundOptions, "place");
        add(stepSoundOptions, "hit");
        add(placeSoundOptions, "break");
        add(hitSoundOptions, "step");
        add(fallSoundOptions, "hit", "step");
    }

    public SoundDefinition add(SoundOptions soundOptions, String... fallbacks) {
        var pathOrFallback = soundOptions.soundPathReplacement == null ? path : soundOptions.soundPathReplacement;
        var instance = LodestoneBlockSoundEventSystem.INSTANCE;
        return instance.add(soundOptions.soundEvent,
                s -> s.with(instance.allSounds(pathOrFallback, soundOptions.soundName, soundOptions.soundModifier, fallbacks)),
                soundOptions.soundDefinitionModifier);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class SoundOptions {

        protected final Supplier<SoundEvent> soundEvent;
        protected String soundName;
        protected String soundPathReplacement;

        protected Consumer<SoundDefinition.Sound> soundModifier = s -> {
        };
        protected Consumer<SoundDefinition> soundDefinitionModifier = s -> {
        };

        public SoundOptions(Supplier<SoundEvent> soundEvent, String soundName) {
            this.soundEvent = soundEvent;
            this.soundName = soundName;
        }

        public SoundOptions replaceSoundName(String soundName) {
            this.soundName = soundName;
            return this;
        }

        public SoundOptions replaceSoundPath(String soundPathReplacement) {
            this.soundPathReplacement = soundPathReplacement;
            return this;
        }

        public SoundOptions modifySound(Consumer<SoundDefinition.Sound> modifier) {
            this.soundModifier = this.soundModifier.andThen(modifier);
            return this;
        }

        public SoundOptions modifyDefinition(Consumer<SoundDefinition> modifier) {
            this.soundDefinitionModifier = this.soundDefinitionModifier.andThen(modifier);
            return this;
        }
    }
}