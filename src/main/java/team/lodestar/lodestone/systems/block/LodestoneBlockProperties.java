package team.lodestar.lodestone.systems.block;

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
@SuppressWarnings("ALL")
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
        addOptionalThrowawayData(d -> d.needsPickaxe());
        return this;
    }

    public LodestoneBlockProperties needsAxe() {
        addOptionalThrowawayData(d -> d.needsAxe());
        return this;
    }

    public LodestoneBlockProperties needsShovel() {
        addOptionalThrowawayData(d -> d.needsShovel());
        return this;
    }

    public LodestoneBlockProperties needsHoe() {
        addOptionalThrowawayData(d -> d.needsHoe());
        return this;
    }

    public LodestoneBlockProperties needsStone() {
        addOptionalThrowawayData(d -> d.needsStone());
        return this;
    }

    public LodestoneBlockProperties needsIron() {
        addOptionalThrowawayData(d -> d.needsIron());
        return this;
    }

    public LodestoneBlockProperties needsDiamond() {
        addOptionalThrowawayData(d -> d.needsDiamond());
        return this;
    }

    public LodestoneBlockProperties hasCustomLoot() {
        addOptionalThrowawayData(d -> d.hasCustomLoot());
        return this;
    }

    public LodestoneBlockProperties isCutoutLayer() {
        addThrowawayData(d -> d.isCutoutLayer());
        return this;
    }

    @Override
    public LodestoneBlockProperties noCollission() {
        return (LodestoneBlockProperties) super.noCollission();
    }

    @Override
    public LodestoneBlockProperties noOcclusion() {
        return (LodestoneBlockProperties) super.noOcclusion();
    }

    @Override
    public LodestoneBlockProperties friction(float p_60912_) {
        return (LodestoneBlockProperties) super.friction(p_60912_);
    }

    @Override
    public LodestoneBlockProperties speedFactor(float p_60957_) {
        return (LodestoneBlockProperties) super.speedFactor(p_60957_);
    }

    @Override
    public LodestoneBlockProperties jumpFactor(float p_60968_) {
        return (LodestoneBlockProperties) super.jumpFactor(p_60968_);
    }

    @Override
    public LodestoneBlockProperties sound(SoundType p_60919_) {
        return (LodestoneBlockProperties) super.sound(p_60919_);
    }

    @Override
    public LodestoneBlockProperties lightLevel(ToIntFunction<BlockState> p_60954_) {
        return (LodestoneBlockProperties) super.lightLevel(p_60954_);
    }

    @Override
    public LodestoneBlockProperties strength(float p_60914_, float p_60915_) {
        return (LodestoneBlockProperties) super.strength(p_60914_, p_60915_);
    }

    @Override
    public LodestoneBlockProperties instabreak() {
        return (LodestoneBlockProperties) super.instabreak();
    }

    @Override
    public LodestoneBlockProperties strength(float p_60979_) {
        return (LodestoneBlockProperties) super.strength(p_60979_);
    }

    @Override
    public LodestoneBlockProperties randomTicks() {
        return (LodestoneBlockProperties) super.randomTicks();
    }

    @Override
    public LodestoneBlockProperties dynamicShape() {
        return (LodestoneBlockProperties) super.dynamicShape();
    }

    @Override
    public LodestoneBlockProperties noDrops() {
        return (LodestoneBlockProperties) super.noDrops();
    }

    @Override
    public LodestoneBlockProperties dropsLike(Block p_60917_) {
        return (LodestoneBlockProperties) super.dropsLike(p_60917_);
    }

    @Override
    public LodestoneBlockProperties lootFrom(Supplier<? extends Block> blockIn) {
        hasCustomLoot();
        return (LodestoneBlockProperties) super.lootFrom(blockIn);
    }

    @Override
    public LodestoneBlockProperties air() {
        return (LodestoneBlockProperties) super.air();
    }

    @Override
    public LodestoneBlockProperties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> p_60923_) {
        return (LodestoneBlockProperties) super.isValidSpawn(p_60923_);
    }

    @Override
    public LodestoneBlockProperties isRedstoneConductor(BlockBehaviour.StatePredicate p_60925_) {
        return (LodestoneBlockProperties) super.isRedstoneConductor(p_60925_);
    }

    @Override
    public LodestoneBlockProperties isSuffocating(BlockBehaviour.StatePredicate p_60961_) {
        return (LodestoneBlockProperties) super.isSuffocating(p_60961_);
    }

    @Override
    public LodestoneBlockProperties isViewBlocking(BlockBehaviour.StatePredicate p_60972_) {
        return (LodestoneBlockProperties) super.isViewBlocking(p_60972_);
    }

    @Override
    public LodestoneBlockProperties hasPostProcess(BlockBehaviour.StatePredicate p_60983_) {
        return (LodestoneBlockProperties) super.hasPostProcess(p_60983_);
    }

    @Override
    public LodestoneBlockProperties emissiveRendering(BlockBehaviour.StatePredicate p_60992_) {
        return (LodestoneBlockProperties) super.emissiveRendering(p_60992_);
    }

    @Override
    public LodestoneBlockProperties requiresCorrectToolForDrops() {
        return (LodestoneBlockProperties) super.requiresCorrectToolForDrops();
    }

    @Override
    public LodestoneBlockProperties color(MaterialColor p_155950_) {
        return (LodestoneBlockProperties) super.color(p_155950_);
    }

    @Override
    public LodestoneBlockProperties destroyTime(float p_155955_) {
        return (LodestoneBlockProperties) super.destroyTime(p_155955_);
    }

    @Override
    public LodestoneBlockProperties explosionResistance(float p_155957_) {
        return (LodestoneBlockProperties) super.explosionResistance(p_155957_);
    }
}