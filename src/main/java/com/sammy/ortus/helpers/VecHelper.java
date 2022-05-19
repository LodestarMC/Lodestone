package com.sammy.ortus.helpers;

import net.minecraft.core.Vec3i;

/**
 * A collection of methods designed to simplify and unify the use of vectors
 */
public class VecHelper {
    /**
     * A directional enum for each of the cardinal directions.
     */
    enum Dir {
        UP, DOWN, NORTH, EAST, SOUTH, WEST
    }

    /**
     * A method that takes in a direction enum (E.G. "UP") and returns a Vec3i object facing that direction
     */
    public static Vec3i offsetDir(Dir dir) {
        Vec3i outVector = new Vec3i(0, 0, 0);
        switch (dir) {

            case UP -> {outVector.offset(0, 1, 0);}
            case DOWN -> {outVector.offset(0, -1, 0);}
            case NORTH -> {outVector.offset(0, 0, -1);}
            case EAST -> {outVector.offset(1, 0, 0);}
            case SOUTH -> {outVector.offset(0, 0, 1);}
            case WEST -> {outVector.offset(-1, 0,0 );}
        }
        return outVector;
    }
}
