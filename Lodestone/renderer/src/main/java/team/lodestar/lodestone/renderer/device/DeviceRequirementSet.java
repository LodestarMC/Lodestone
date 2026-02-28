package team.lodestar.lodestone.renderer.device;

import java.util.Set;

public class DeviceRequirementSet {
    private final Set<DeviceRequirement> requirements;

    public DeviceRequirementSet(DeviceRequirement... requirements) {
        this.requirements = Set.of(requirements);
    }

    public static DeviceRequirementSet of(DeviceRequirement... requirements) {
        return new DeviceRequirementSet(requirements);
    }

    public boolean test() {
        boolean failed = false;
        for (DeviceRequirement requirement : requirements) {
            if (!requirement.test()) {
                failed = true;
                break;
            }
        }
        return !failed;
    }
}
