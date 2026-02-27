package team.lodestar.lodestone.datagen.providers.sound;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"NullableProblems", "unused"})
public abstract class LodestoneSoundEventSystem extends SoundDefinitionsProvider {

    public static LodestoneSoundEventSystem INSTANCE;

    public final String modId;
    public final ExistingFileHelper helper;

    public LodestoneSoundEventSystem(PackOutput packOutput, String modId, ExistingFileHelper existingFileHelper) {
        super(packOutput, modId, existingFileHelper);
        this.modId = modId;
        this.helper = existingFileHelper;
        INSTANCE = this;
    }

    public SoundDefinition definition(SoundEvent soundEvent) {
        return SoundDefinition.definition().subtitle(subtitle(soundEvent));
    }

    @Override
    public void add(String soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
    }

    @Override
    public void add(Supplier<SoundEvent> soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
    }

    @Override
    public void add(SoundEvent soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
    }

    @Override
    public void add(ResourceLocation soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
    }

    @SafeVarargs
    public final SoundDefinition add(Supplier<SoundEvent> soundEvent, Consumer<SoundDefinition>... modifiers) {
        var definition = definition(soundEvent.get());
        for (Consumer<SoundDefinition> modifier : modifiers) {
            modifier.accept(definition);
        }
        add(soundEvent, definition);
        return definition;
    }

    public static SoundDefinition.Sound sound(String name) {
        return sound(name.contains(":") ? ResourceLocation.parse(name) : ResourceLocation.fromNamespaceAndPath(INSTANCE.modId, name));
    }

    public SoundDefinition.Sound[] sounds(String name, int variants) {
        SoundDefinition.Sound[] sounds = new SoundDefinition.Sound[variants];
        for (int i = 0; i < variants; i++) {
            var id = name + (i + 1);
            var resourceLocation = name.contains(":") ? ResourceLocation.parse(id) : ResourceLocation.fromNamespaceAndPath(modId, id);
            sounds[i] = sound(resourceLocation);
        }
        return sounds;
    }

    public SoundDefinition.Sound[] sounds(String name, int variants, Consumer<SoundDefinition.Sound> modifier) {
        var sounds = sounds(name, variants);
        for (SoundDefinition.Sound sound : sounds) {
            modifier.accept(sound);
        }
        return sounds;
    }

    public SoundDefinition.Sound[] allSounds(String path, Consumer<SoundDefinition.Sound> modifier) {
        var sounds = allSounds(path);
        for (SoundDefinition.Sound sound : sounds) {
            modifier.accept(sound);
        }
        return sounds;
    }

    public SoundDefinition.Sound[] allSounds(String basePath, String name, Consumer<SoundDefinition.Sound> modifier, String... fallbacks) {
        var sounds = allSounds(basePath, name, fallbacks);
        for (SoundDefinition.Sound sound : sounds) {
            modifier.accept(sound);
        }
        return sounds;
    }

    public SoundDefinition.Sound[] allSounds(String path) {
        int index = path.lastIndexOf("/");
        var name = path.substring(index + 1);
        return allSounds(path.substring(0, index), name);
    }

    public SoundDefinition.Sound[] allSounds(String basePath, String name, String... fallbacks) {
        var sounds = new ArrayList<SoundDefinition.Sound>();
        int counter = 1;
        var leftoverFallbacks = new ArrayList<>(List.of(fallbacks));

        if (!basePath.isEmpty() && !basePath.endsWith("/")) {
            basePath += "/";
        }
        while (true) {
            var id = basePath + name + counter;
            var path = basePath.contains(":") ? ResourceLocation.parse(id) : ResourceLocation.fromNamespaceAndPath(modId, id);
            boolean valid = helper.exists(path, PackType.CLIENT_RESOURCES, ".ogg", "sounds");
            if (valid) {
                sounds.add(sound(path));
            } else {
                if (counter == 1 && !leftoverFallbacks.isEmpty()) {
                    name = leftoverFallbacks.removeFirst();
                    continue;
                }
                break;
            }
            counter++;
        }
        if (sounds.isEmpty()) {
            throw new UnsupportedOperationException("Sound Definition is empty for sound: " + (basePath + name));
        }
        var array = new SoundDefinition.Sound[sounds.size()];
        for (int i = 0; i < sounds.size(); i++) {
            array[i] = sounds.get(i);
        }
        return array;
    }

    public static SoundDefinition.Sound sound(String modId, String id) {
        return sound(id.contains(":") ? ResourceLocation.parse(id) : ResourceLocation.fromNamespaceAndPath(modId, id));
    }

    public String subtitle(SoundEvent soundEvent) {
        return subtitle(soundEvent.getLocation());
    }

    public String subtitle(ResourceLocation id) {
        return modId + ".subtitle." + id.getPath();
    }
}
