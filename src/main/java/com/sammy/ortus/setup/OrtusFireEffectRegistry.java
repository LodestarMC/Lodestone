package com.sammy.ortus.setup;


import com.sammy.ortus.systems.fireeffect.FireEffectType;

import java.util.HashMap;

public class OrtusFireEffectRegistry {

    public static final HashMap<String, FireEffectType> FIRE_TYPES = new HashMap<>();

    public static FireEffectType registerType(FireEffectType type) {
        FIRE_TYPES.put(type.id, type);
        return type;
    }
}