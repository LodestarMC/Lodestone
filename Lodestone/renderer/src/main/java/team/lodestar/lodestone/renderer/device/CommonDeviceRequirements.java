package team.lodestar.lodestone.renderer.device;

public class CommonDeviceRequirements {
    public static final DeviceRequirementSet EMPTY = DeviceRequirementSet.of();
    public static final DeviceRequirementSet INSTANCED_DRAW = DeviceRequirementSet.of(DeviceRequirement.INSTANCED_DRAW);
    public static final DeviceRequirementSet COMPUTE = DeviceRequirementSet.of(DeviceRequirement.COMPUTE_SHADERS);
    public static final DeviceRequirementSet SSBO = DeviceRequirementSet.of(DeviceRequirement.SSBO);
    public static final DeviceRequirementSet COMPUTE_SSBO = DeviceRequirementSet.of(DeviceRequirement.COMPUTE_SHADERS, DeviceRequirement.SSBO);
}
