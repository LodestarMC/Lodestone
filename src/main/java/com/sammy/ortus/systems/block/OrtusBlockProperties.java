package com.sammy.ortus.systems.block;

import com.sammy.ortus.helpers.DataHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.sammy.ortus.systems.block.OrtusThrowawayBlockData.DATA_CACHE;

/**
 * An extension of Block Properties, allowing you to add {@link OrtusThrowawayBlockData}
 */
@SuppressWarnings("ALL")
public class OrtusBlockProperties extends BlockBehaviour.Properties {

    public OrtusBlockProperties(Material material, MaterialColor color) {
        super(material, (state) -> color);
    }

    public OrtusBlockProperties(Material material) {
        super(material, (state) -> material.getColor());
    }

    public static void setRenderLayers(FMLClientSetupEvent event) {
        DataHelper.getAll(new ArrayList<>(ForgeRegistries.BLOCKS.getValues()), b -> b.properties instanceof OrtusBlockProperties && ((OrtusBlockProperties) b.properties).getThrowawayData().isCutoutLayer).forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, RenderType.cutoutMipped()));
    }

    public OrtusBlockProperties addThrowawayData(Function<OrtusThrowawayBlockData, OrtusThrowawayBlockData> function) {
        DATA_CACHE.put(this, function.apply(DATA_CACHE.getOrDefault(this, new OrtusThrowawayBlockData())));
        return this;
    }
    public OrtusThrowawayBlockData getThrowawayData() {
        return DATA_CACHE.getOrDefault(this, new OrtusThrowawayBlockData());
    }

    public OrtusBlockProperties needsPickaxe() {
        addThrowawayData(d -> d.needsPickaxe());
        return this;
    }

    public OrtusBlockProperties needsAxe() {
        addThrowawayData(d -> d.needsAxe());
        return this;
    }

    public OrtusBlockProperties needsShovel() {
        addThrowawayData(d -> d.needsShovel());
        return this;
    }

    public OrtusBlockProperties needsHoe() {
        addThrowawayData(d -> d.needsHoe());
        return this;
    }

    public OrtusBlockProperties needsStone() {
        addThrowawayData(d -> d.needsStone());
        return this;
    }

    public OrtusBlockProperties needsIron() {
        addThrowawayData(d -> d.needsIron());
        return this;
    }

    public OrtusBlockProperties needsDiamond() {
        addThrowawayData(d -> d.needsDiamond());
        return this;
    }

    public OrtusBlockProperties isCutoutLayer() {
        addThrowawayData(d -> d.isCutoutLayer());
        return this;
    }

    public OrtusBlockProperties hasCustomLoot() {
        addThrowawayData(d -> d.hasCustomLoot());
        return this;
    }

    @Override
    public OrtusBlockProperties noCollission() {
        return (OrtusBlockProperties) super.noCollission();
    }

    @Override
    public OrtusBlockProperties noOcclusion() {
        return (OrtusBlockProperties) super.noOcclusion();
    }

    @Override
    public OrtusBlockProperties friction(float p_60912_) {
        return (OrtusBlockProperties) super.friction(p_60912_);
    }

    @Override
    public OrtusBlockProperties speedFactor(float p_60957_) {
        return (OrtusBlockProperties) super.speedFactor(p_60957_);
    }

    @Override
    public OrtusBlockProperties jumpFactor(float p_60968_) {
        return (OrtusBlockProperties) super.jumpFactor(p_60968_);
    }

    @Override
    public OrtusBlockProperties sound(SoundType p_60919_) {
        return (OrtusBlockProperties) super.sound(p_60919_);
    }

    @Override
    public OrtusBlockProperties lightLevel(ToIntFunction<BlockState> p_60954_) {
        return (OrtusBlockProperties) super.lightLevel(p_60954_);
    }

    @Override
    public OrtusBlockProperties strength(float p_60914_, float p_60915_) {
        return (OrtusBlockProperties) super.strength(p_60914_, p_60915_);
    }

    @Override
    public OrtusBlockProperties instabreak() {
        return (OrtusBlockProperties) super.instabreak();
    }

    @Override
    public OrtusBlockProperties strength(float p_60979_) {
        return (OrtusBlockProperties) super.strength(p_60979_);
    }

    @Override
    public OrtusBlockProperties randomTicks() {
        return (OrtusBlockProperties) super.randomTicks();
    }

    @Override
    public OrtusBlockProperties dynamicShape() {
        return (OrtusBlockProperties) super.dynamicShape();
    }

    @Override
    public OrtusBlockProperties noDrops() {
        return (OrtusBlockProperties) super.noDrops();
    }

    @Override
    public OrtusBlockProperties dropsLike(Block p_60917_) {
        return (OrtusBlockProperties) super.dropsLike(p_60917_);
    }

    @Override
    public OrtusBlockProperties lootFrom(Supplier<? extends Block> blockIn) {
        return (OrtusBlockProperties) super.lootFrom(blockIn);
    }

    @Override
    public OrtusBlockProperties air() {
        return (OrtusBlockProperties) super.air();
    }

    @Override
    public OrtusBlockProperties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> p_60923_) {
        return (OrtusBlockProperties) super.isValidSpawn(p_60923_);
    }

    @Override
    public OrtusBlockProperties isRedstoneConductor(BlockBehaviour.StatePredicate p_60925_) {
        return (OrtusBlockProperties) super.isRedstoneConductor(p_60925_);
    }

    @Override
    public OrtusBlockProperties isSuffocating(BlockBehaviour.StatePredicate p_60961_) {
        return (OrtusBlockProperties) super.isSuffocating(p_60961_);
    }

    @Override
    public OrtusBlockProperties isViewBlocking(BlockBehaviour.StatePredicate p_60972_) {
        return (OrtusBlockProperties) super.isViewBlocking(p_60972_);
    }

    @Override
    public OrtusBlockProperties hasPostProcess(BlockBehaviour.StatePredicate p_60983_) {
        return (OrtusBlockProperties) super.hasPostProcess(p_60983_);
    }

    @Override
    public OrtusBlockProperties emissiveRendering(BlockBehaviour.StatePredicate p_60992_) {
        return (OrtusBlockProperties) super.emissiveRendering(p_60992_);
    }

    @Override
    public OrtusBlockProperties requiresCorrectToolForDrops() {
        return (OrtusBlockProperties) super.requiresCorrectToolForDrops();
    }

    @Override
    public OrtusBlockProperties color(MaterialColor p_155950_) {
        return (OrtusBlockProperties) super.color(p_155950_);
    }

    @Override
    public OrtusBlockProperties destroyTime(float p_155955_) {
        return (OrtusBlockProperties) super.destroyTime(p_155955_);
    }

    @Override
    public OrtusBlockProperties explosionResistance(float p_155957_) {
        return (OrtusBlockProperties) super.explosionResistance(p_155957_);
    }
}