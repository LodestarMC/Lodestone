package team.lodestar.lodestone.setup;


import team.lodestar.lodestone.systems.fireeffect.FireEffectType;

import java.util.HashMap;

public class LodestoneFireEffectRegistry {

    public static final HashMap<String, FireEffectType> FIRE_TYPES = new HashMap<>();

    public static FireEffectType registerType(FireEffectType type) {
        FIRE_TYPES.put(type.id, type);
        return type;
    }
}