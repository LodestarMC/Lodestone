package team.lodestar.lodestone.systems.block;

import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.helpers.DataHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
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

    public static void setRenderLayers(FMLClientSetupEvent event) {
        DataHelper.getAll(new ArrayList<>(ForgeRegistries.BLOCKS.getValues()), b -> b.properties instanceof LodestoneBlockProperties && ((LodestoneBlockProperties) b.properties).getThrowawayData().isCutoutLayer).forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, RenderType.cutoutMipped()));
    }

    public LodestoneBlockProperties addOptionalThrowawayData(Function<LodestoneThrowawayBlockData, LodestoneThrowawayBlockData> function) {
        if (DatagenModLoader.isRunningDataGen()) {
            addThrowawayData(function);
        }
        return this;
    }

    public LodestoneBlockProperties addThrowawayData(Function<LodestoneThrowawayBlockData, LodestoneThrowawayBlockData> function) {
        LodestoneThrowawayBlockData.DATA_CACHE.put(this, function.apply(LodestoneThrowawayBlockData.DATA_CACHE.getOrDefault(this, new LodestoneThrowawayBlockData())));
        return this;
    }

    public LodestoneThrowawayBlockData getThrowawayData() {
        return LodestoneThrowawayBlockData.DATA_CACHE.getOrDefault(this, new LodestoneThrowawayBlockData());
    }

    public LodestoneBlockProperties needsPickaxe() {
        addOptionalThrowawayData(LodestoneThrowawayBlockData::needsPickaxe);
        return this;
    }

    public LodestoneBlockProperties needsAxe() {
        addOptionalThrowawayData(LodestoneThrowawayBlockData::needsAxe);
        return this;
    }

    public LodestoneBlockProperties needsShovel() {
        addOptionalThrowawayData(LodestoneThrowawayBlockData::needsShovel);
        return this;
    }

    public LodestoneBlockProperties needsHoe() {
        addOptionalThrowawayData(LodestoneThrowawayBlockData::needsHoe);
        return this;
    }

    public LodestoneBlockProperties needsStone() {
        addOptionalThrowawayData(LodestoneThrowawayBlockData::needsStone);
        return this;
    }

    public LodestoneBlockProperties needsIron() {
        addOptionalThrowawayData(LodestoneThrowawayBlockData::needsIron);
        return this;
    }

    public LodestoneBlockProperties needsDiamond() {
        addOptionalThrowawayData(LodestoneThrowawayBlockData::needsDiamond);
        return this;
    }

    public LodestoneBlockProperties hasCustomLoot() {
        addOptionalThrowawayData(LodestoneThrowawayBlockData::hasCustomLoot);
        return this;
    }

    public LodestoneBlockProperties isCutoutLayer() {
        addThrowawayData(LodestoneThrowawayBlockData::isCutoutLayer);
        return this;
    }

    // this method name keeps me up at night
    @Override
    @NotNull
    public LodestoneBlockProperties noCollission() {
        return (LodestoneBlockProperties) super.noCollission();
    }

    @NotNull
    public LodestoneBlockProperties noCollision() {
        return noCollission();
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
        hasCustomLoot();
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
