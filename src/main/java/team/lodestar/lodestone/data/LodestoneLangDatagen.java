package team.lodestar.lodestone.data;

import net.minecraft.data.PackOutput;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import team.lodestar.lodestone.helpers.DataHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

public class LodestoneLangDatagen extends LanguageProvider {
    public LodestoneLangDatagen(PackOutput output) {
        super(output, LODESTONE, "en_us");
    }

    @Override
    protected void addTranslations() {
        Set<RegistryObject<Attribute>> attributes = new HashSet<>(LodestoneAttributeRegistry.ATTRIBUTES.getEntries());

        attributes.forEach(a -> {
            String name = DataHelper.toTitleCase(a.getId().getPath(), "_");
            add("attribute.name.lodestone." + a.getId().getPath(), name);
        });

        add("lodestone.options.screenshakeIntensity", "Screenshake Intensity");
        add("lodestone.options.screenshakeIntensity.tooltip", "Controls how much screenshake is applied to your screen.");

        add("lodestone.options.fireOffset", "Fire Overlay Offset");
        add("lodestone.options.fireOffset.tooltip", "Offsets the fire overlay effect downwards, clearing up your vision.");

        add("lodestone.command.devsetup", "Command Successful, World is now setup for not-annoying development work");
        add("lodestone.command.screenshake", "Command Successful, enjoy your screenshake.");
    }

    @Override
    public String getName() {
        return "Lodestone Lang Entries";
    }
}