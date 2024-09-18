package team.lodestar.lodestone.systems.model.obj.modifier.modifiers;

import team.lodestar.lodestone.systems.model.obj.modifier.ModiferSettings;

public class TriangulateSettings extends ModiferSettings {
    public QuadMethod quadMethod = QuadMethod.ShortestDiagonal;
    public NgonMethod ngonMethod = NgonMethod.Clip;

    public TriangulateSettings(QuadMethod quadMethod, NgonMethod ngonMethod) {
        this.quadMethod = quadMethod;
        this.ngonMethod = ngonMethod;
    }

    public enum QuadMethod {
        ShortestDiagonal,
        LongestDiagonal;
    }

    public enum NgonMethod {
        Clip;
    }
}
