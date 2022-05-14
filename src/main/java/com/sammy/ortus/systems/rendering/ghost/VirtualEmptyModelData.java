package com.sammy.ortus.systems.rendering.ghost;

import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public enum VirtualEmptyModelData implements IModelData {
    INSTANCE;

    public static boolean is(IModelData data) {
        return data == INSTANCE;
    }

    @Override
    public boolean hasProperty(ModelProperty<?> prop) {
        return false;
    }

    @Override
    public <T> T getData(ModelProperty<T> prop) {
        return null;
    }

    @Override
    public <T> T setData(ModelProperty<T> prop, T data) {
        return null;
    }
}
