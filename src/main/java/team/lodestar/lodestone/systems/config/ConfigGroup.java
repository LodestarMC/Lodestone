package team.lodestar.lodestone.systems.config;

public class ConfigGroup {
    public final String configGroupId;

    public ConfigGroup(String modId, String configType) {
        this.configGroupId = modId + ":" + configType;
    }
}
