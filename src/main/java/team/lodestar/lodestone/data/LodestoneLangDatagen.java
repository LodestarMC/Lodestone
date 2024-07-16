package team.lodestar.lodestone.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;

import java.util.HashSet;
import java.util.Set;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

public class LodestoneLangDatagen extends LanguageProvider {
    public LodestoneLangDatagen(PackOutput output) {
        super(output, LODESTONE, "en_us");
    }

    @Override
    protected void addTranslations() {
        Set<DeferredHolder<Attribute, ? extends Attribute>> attributes = new HashSet<>(LodestoneAttributeRegistry.ATTRIBUTES.getEntries());

        attributes.forEach(a -> {
            String name = DataHelper.toTitleCase(a.getId().getPath(), "_");
            add("attribute.name.lodestone." + a.getId().getPath(), name);
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
        return "command." + LODESTONE + "." + command;
    }

    public void addOption(String option, String result) {
        add(getOption(option), result);
    }

    public static String getOption(String option) {
        return "options." + LODESTONE + "." + option;
    }

    public void addOptionTooltip(String option, String result) {
        add(getOptionTooltip(option), result);
    }

    public static String getOptionTooltip(String option) {
        return "options." + LODESTONE + "." + option + ".tooltip";
    }

    @Override
    public String getName() {
        return "Lodestone Lang Entries";
    }
}