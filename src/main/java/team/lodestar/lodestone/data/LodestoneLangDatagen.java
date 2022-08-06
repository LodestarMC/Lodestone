package team.lodestar.lodestone.data;

import team.lodestar.lodestone.setup.LodestoneAttributeRegistry;
import team.lodestar.lodestone.helpers.DataHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;

import static team.lodestar.lodestone.LodestoneLib.ORTUS;

public class LodestoneLangDatagen extends LanguageProvider {
    public LodestoneLangDatagen(DataGenerator gen) {
        super(gen, ORTUS, "en_us");
    }

    @Override
    protected void addTranslations() {
        Set<RegistryObject<Attribute>> attributes = new HashSet<>(LodestoneAttributeRegistry.ATTRIBUTES.getEntries());

        attributes.forEach(a -> {
            String name = DataHelper.toTitleCase(a.getId().getPath(), "_");
            add("attribute.name.lodestone." + a.get().getRegistryName().getPath(), name);
        });
        addOption("screenshake_intensity", "Screenshake Intensity");
        addOptionTooltip("screenshake_intensity", "Controls how much screenshake is applied to your screen.");

        addOption("fire_offset", "Fire Overlay Offset");
        addOptionTooltip("fire_offset", "Offsets the fire overlay effect downwards, clearing up your vision.");

        addCommand("devsetup", "World setup for not-annoying development work");
        addCommand("screenshake", "Command Successful, enjoy your screenshake.");

    }

    public void addCommand(String command, String feedback) {
        add(getCommand(command), feedback);
    }

    public static String getCommand(String command) {
        return "command." + ORTUS + "." + command;
    }

    public void addOption(String option, String result) {
        add(getOption(option), result);
    }

    public static String getOption(String option) {
        return "options." + ORTUS + "." + option;
    }

    public void addOptionTooltip(String option, String result) {
        add(getOptionTooltip(option), result);
    }

    public static String getOptionTooltip(String option) {
        return "options." + ORTUS + "." + option + ".tooltip";
    }

    @Override
    public String getName() {
        return "Lodestone Lang Entries";
    }
}