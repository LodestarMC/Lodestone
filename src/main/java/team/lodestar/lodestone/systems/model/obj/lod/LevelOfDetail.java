package team.lodestar.lodestone.systems.model.obj.lod;

import team.lodestar.lodestone.systems.model.obj.ObjModel;

public record LevelOfDetail<T>(ObjModel model, T argument) {
}