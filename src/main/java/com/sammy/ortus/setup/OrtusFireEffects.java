package com.sammy.ortus.setup;


import com.sammy.ortus.systems.fireeffect.FireEffectType;

import java.util.HashMap;

public class OrtusFireEffects {

    public static final HashMap<String, FireEffectType> FIRE_TYPES = new HashMap<>();

//    public static final FireEffectType METEOR_FIRE = registerType(new FireEffectType("meteor_fire", 1, 20));
//    public static final FireEffectType GREATER_FIRE = registerType(new FireEffectType("greater_fire", 1, 10));

    private static FireEffectType registerType(FireEffectType type) {
        FIRE_TYPES.put(type.id, type);
        return type;
    }
}