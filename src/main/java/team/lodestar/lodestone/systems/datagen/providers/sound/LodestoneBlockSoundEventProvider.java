package team.lodestar.lodestone.systems.datagen.providers.sound;

import net.minecraft.data.*;
import net.neoforged.neoforge.common.data.*;
import team.lodestar.lodestone.systems.sound.*;
import team.lodestar.lodestone.systems.sound.*;

import java.util.function.*;

public abstract class LodestoneBlockSoundEventProvider extends LodestoneSoundEventProvider {

    public static LodestoneBlockSoundEventProvider INSTANCE;

    public LodestoneBlockSoundEventProvider(PackOutput packOutput, String modId, ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
        INSTANCE = this;
    }

    public void add(RegistryReadyBlockSoundType soundType, String path) {
        add(soundType, path, c -> {
        });
    }

    public void add(RegistryReadyBlockSoundType soundType, String path, Consumer<BlockSoundEventBuilder> modifier) {
        var builder = BlockSoundEventBuilder.create(path, soundType);
        modifier.accept(builder);
        builder.addSounds();
    }

    public SoundEventBuilderBlueprint blueprint(String path) {
        return blueprint(path, c -> {
        });
    }

    public SoundEventBuilderBlueprint blueprint(String path, Consumer<BlockSoundEventBuilder> modifier) {
        return new SoundEventBuilderBlueprint(path, modifier);
    }

    public static class SoundEventBuilderBlueprint {
        protected final String path;
        protected Consumer<BlockSoundEventBuilder> modifier;

        public SoundEventBuilderBlueprint(String path, Consumer<BlockSoundEventBuilder> modifier) {
            this.path = path;
            this.modifier = modifier;
        }

        public SoundEventBuilderBlueprint modify(Consumer<BlockSoundEventBuilder> extraModifier) {
            modifier = modifier.andThen(extraModifier);
            return this;
        }

        public SoundEventBuilderBlueprint addAll(RegistryReadyBlockSoundType... soundTypes) {
            for (RegistryReadyBlockSoundType soundType : soundTypes) {
                add(soundType);
            }
            return this;
        }

        public SoundEventBuilderBlueprint add(RegistryReadyBlockSoundType soundType) {
            INSTANCE.add(soundType, path, modifier);
            return this;
        }

        public SoundEventBuilderBlueprint add(RegistryReadyBlockSoundType soundType, Consumer<BlockSoundEventBuilder> extraModifier) {
            INSTANCE.add(soundType, path, modifier.andThen(extraModifier));
            return this;
        }
    }
}