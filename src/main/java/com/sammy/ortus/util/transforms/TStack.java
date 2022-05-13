package com.sammy.ortus.util.transforms;

public interface TStack<Self> {
    Self pushPose();

    Self popPose();
}