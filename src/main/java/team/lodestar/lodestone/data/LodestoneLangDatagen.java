package team.lodestar.lodestone.data;

import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.world.entity.ai.attributes.Attribute;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;

import java.util.HashSet;
import java.util.Set;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

public class LodestoneLangDatagen extends FabricLanguageProvider {

    protected LodestoneLangDatagen(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }


    @Override
    public void generateTranslations(TranslationBuilder builder) {
        Set<RegistryObject<Attribute>> attributes = new HashSet<>(LodestoneAttributeRegistry.ATTRIBUTES.getEntries());

        attributes.forEach(a -> {
            String name = DataHelper.toTitleCase(a.getId().getPath(), "_");
            addOption(builder,"attribute.name.lodestone." + a.getId().getPath(), name);
        });
        addOption(builder,"screenshake_intensity", "Screenshake Intensity");
        addOptionTooltip(builder, "screenshake_intensity", "Controls how much screenshake is applied to your screen.");

        addOption(builder,"fire_offset", "Fire Overlay Offset");
        addOptionTooltip(builder, "fire_offset", "Offsets the fire overlay effect downwards, clearing up your vision.");

        addCommand(builder, "devsetup", "World setup for not-annoying development work");
        addCommand(builder, "screenshake", "Command Successful, enjoy your screenshake.");
    }

    public void addCommand(TranslationBuilder builder, String command, String feedback) {
        addOption(builder, getCommand(command), feedback);
    }

    public static String getCommand(String command) {
        return "command." + LODESTONE + "." + command;
    }

    public void addOption(TranslationBuilder builder, String option, String result) {
        builder.add(getOption(option), result);
    }

    public static String getOption(String option) {
        return "options." + LODESTONE + "." + option;
    }

    public void addOptionTooltip(TranslationBuilder builder, String option, String result) {
        addOption(builder, getOptionTooltip(option), result);
    }

    public static String getOptionTooltip(String option) {
        return "options." + LODESTONE + "." + option + ".tooltip";
    }



    @Override
    public String getName() {
        return "Lodestone Lang Entries";
    }
}