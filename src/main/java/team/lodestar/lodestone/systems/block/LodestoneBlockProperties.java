package team.lodestar.lodestone.systems.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.TagKey;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import team.lodestar.lodestone.systems.block.data.LodestoneDatagenBlockData;
import team.lodestar.lodestone.systems.block.data.LodestoneThrowawayBlockData;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

/**
 * An extension of Block Properties, allowing you to add {@link LodestoneThrowawayBlockData}
 * Some of these values are optional; they only get added during datagen.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class LodestoneBlockProperties extends BlockBehaviour.Properties {

    public LodestoneBlockProperties(Material material, MaterialColor color) {
        super(material, (state) -> color);
    }

    public LodestoneBlockProperties(Material material) {
        super(material, (state) -> material.getColor());
    }

    public LodestoneBlockProperties(Material pMaterial, Function<BlockState, MaterialColor> pMaterialColor) {
        super(pMaterial, pMaterialColor);
    }

    public static LodestoneBlockProperties copy(BlockBehaviour pBlockBehaviour) {
        LodestoneBlockProperties properties = new LodestoneBlockProperties(pBlockBehaviour.material, pBlockBehaviour.properties.materialColor);
        properties.material = pBlockBehaviour.properties.material;
        properties.destroyTime = pBlockBehaviour.properties.destroyTime;
        properties.explosionResistance = pBlockBehaviour.properties.explosionResistance;
        properties.hasCollision = pBlockBehaviour.properties.hasCollision;
        properties.isRandomlyTicking = pBlockBehaviour.properties.isRandomlyTicking;
        properties.lightEmission = pBlockBehaviour.properties.lightEmission;
        properties.materialColor = pBlockBehaviour.properties.materialColor;
        properties.soundType = pBlockBehaviour.properties.soundType;
        properties.friction = pBlockBehaviour.properties.friction;
        properties.speedFactor = pBlockBehaviour.properties.speedFactor;
        properties.dynamicShape = pBlockBehaviour.properties.dynamicShape;
        properties.canOcclude = pBlockBehaviour.properties.canOcclude;
        properties.isAir = pBlockBehaviour.properties.isAir;
        properties.requiresCorrectToolForDrops = pBlockBehaviour.properties.requiresCorrectToolForDrops;
        return properties;
    }

    public LodestoneBlockProperties addThrowawayData(Function<LodestoneThrowawayBlockData, LodestoneThrowawayBlockData> function) {
        ThrowawayBlockDataHandler.THROWAWAY_DATA_CACHE.put(this, function.apply(getThrowawayData()));
        return this;
    }

    public LodestoneThrowawayBlockData getThrowawayData() {
        return ThrowawayBlockDataHandler.THROWAWAY_DATA_CACHE.getOrDefault(this, LodestoneThrowawayBlockData.EMPTY);
    }

    public LodestoneBlockProperties setCutoutRenderType() {
        return setRenderType(()->RenderType::cutoutMipped);
    }

    public LodestoneBlockProperties setRenderType(Supplier<Supplier<RenderType>> renderType) {
        if (FMLEnvironment.dist.isClient()) {
            addThrowawayData(d -> d.setRenderType(renderType));
        }
        return this;
    }

    public LodestoneBlockProperties addDatagenData(Function<LodestoneDatagenBlockData, LodestoneDatagenBlockData> function) {
        if (DatagenModLoader.isRunningDataGen()) {
            ThrowawayBlockDataHandler.DATAGEN_DATA_CACHE.put(this, function.apply(getDatagenData()));
        }
        return this;
    }

    public LodestoneDatagenBlockData getDatagenData() {
        return ThrowawayBlockDataHandler.DATAGEN_DATA_CACHE.getOrDefault(this, LodestoneDatagenBlockData.EMPTY);
    }

    public LodestoneBlockProperties addTags(TagKey<Block>... tags) {
        addDatagenData(d -> d.addTags(tags));
        return this;
    }

    public LodestoneBlockProperties needsPickaxe() {
        addDatagenData(LodestoneDatagenBlockData::needsPickaxe);
        return this;
    }

    public LodestoneBlockProperties needsAxe() {
        addDatagenData(LodestoneDatagenBlockData::needsAxe);
        return this;
    }

    public LodestoneBlockProperties needsShovel() {
        addDatagenData(LodestoneDatagenBlockData::needsShovel);
        return this;
    }

    public LodestoneBlockProperties needsHoe() {
        addDatagenData(LodestoneDatagenBlockData::needsHoe);
        return this;
    }

    public LodestoneBlockProperties needsStone() {
        addDatagenData(LodestoneDatagenBlockData::needsStone);
        return this;
    }

    public LodestoneBlockProperties needsIron() {
        addDatagenData(LodestoneDatagenBlockData::needsIron);
        return this;
    }

    public LodestoneBlockProperties needsDiamond() {
        addDatagenData(LodestoneDatagenBlockData::needsDiamond);
        return this;
    }

    @Override
    @NotNull
    public LodestoneBlockProperties noCollission() {
        return (LodestoneBlockProperties) super.noCollission();
    }

    @Override
    @NotNull
    public LodestoneBlockProperties noOcclusion() {
        return (LodestoneBlockProperties) super.noOcclusion();
    }

    @Override
    @NotNull
    public LodestoneBlockProperties friction(float friction) {
        return (LodestoneBlockProperties) super.friction(friction);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties speedFactor(float factor) {
        return (LodestoneBlockProperties) super.speedFactor(factor);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties jumpFactor(float factor) {
        return (LodestoneBlockProperties) super.jumpFactor(factor);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties sound(@NotNull SoundType type) {
        return (LodestoneBlockProperties) super.sound(type);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties lightLevel(@NotNull ToIntFunction<BlockState> lightMap) {
        return (LodestoneBlockProperties) super.lightLevel(lightMap);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties strength(float destroyTime, float explosionResistance) {
        return (LodestoneBlockProperties) super.strength(destroyTime, explosionResistance);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties instabreak() {
        return (LodestoneBlockProperties) super.instabreak();
    }

    @Override
    @NotNull
    public LodestoneBlockProperties strength(float strength) {
        return (LodestoneBlockProperties) super.strength(strength);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties randomTicks() {
        return (LodestoneBlockProperties) super.randomTicks();
    }

    @Override
    @NotNull
    public LodestoneBlockProperties dynamicShape() {
        return (LodestoneBlockProperties) super.dynamicShape();
    }

    @Override
    @NotNull
    public LodestoneBlockProperties noDrops() {
        return (LodestoneBlockProperties) super.noDrops();
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public LodestoneBlockProperties dropsLike(@NotNull Block block) {
        return (LodestoneBlockProperties) super.dropsLike(block);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties lootFrom(@NotNull Supplier<? extends Block> blockIn) {
        return (LodestoneBlockProperties) super.lootFrom(blockIn);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties air() {
        return (LodestoneBlockProperties) super.air();
    }

    @Override
    @NotNull
    public LodestoneBlockProperties isValidSpawn(@NotNull BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return (LodestoneBlockProperties) super.isValidSpawn(predicate);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties isRedstoneConductor(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.isRedstoneConductor(predicate);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties isSuffocating(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.isSuffocating(predicate);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties isViewBlocking(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.isViewBlocking(predicate);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties hasPostProcess(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.hasPostProcess(predicate);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties emissiveRendering(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.emissiveRendering(predicate);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties requiresCorrectToolForDrops() {
        return (LodestoneBlockProperties) super.requiresCorrectToolForDrops();
    }

    @Override
    @NotNull
    public LodestoneBlockProperties color(@NotNull MaterialColor materialColor) {
        return (LodestoneBlockProperties) super.color(materialColor);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties destroyTime(float destroyTime) {
        return (LodestoneBlockProperties) super.destroyTime(destroyTime);
    }

    @Override
    @NotNull
    public LodestoneBlockProperties explosionResistance(float explosionResistance) {
        return (LodestoneBlockProperties) super.explosionResistance(explosionResistance);
    }
}
