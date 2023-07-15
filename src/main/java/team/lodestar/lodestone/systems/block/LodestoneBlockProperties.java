package team.lodestar.lodestone.systems.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;
import team.lodestar.lodestone.systems.datagen.LodestoneDatagenBlockData;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

/**
 * An extension of Block Properties, allowing you to add {@link LodestoneThrowawayBlockData}
 * Some of these values are optional; they only get added during datagen.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class LodestoneBlockProperties extends BlockBehaviour.Properties {

	private LodestoneBlockProperties() {
		super();
	}

	public static LodestoneBlockProperties of() {
		return new LodestoneBlockProperties();
	}

	public static LodestoneBlockProperties copy(BlockBehaviour pBlockBehaviour) {
		LodestoneBlockProperties properties = LodestoneBlockProperties.of();
		properties.destroyTime = pBlockBehaviour.properties.destroyTime;
		properties.explosionResistance = pBlockBehaviour.properties.explosionResistance;
		properties.hasCollision = pBlockBehaviour.properties.hasCollision;
		properties.isRandomlyTicking = pBlockBehaviour.properties.isRandomlyTicking;
		properties.lightEmission = pBlockBehaviour.properties.lightEmission;
		properties.mapColor = pBlockBehaviour.properties.mapColor;
		properties.soundType = pBlockBehaviour.properties.soundType;
		properties.friction = pBlockBehaviour.properties.friction;
		properties.speedFactor = pBlockBehaviour.properties.speedFactor;
		properties.dynamicShape = pBlockBehaviour.properties.dynamicShape;
		properties.canOcclude = pBlockBehaviour.properties.canOcclude;
		properties.isAir = pBlockBehaviour.properties.isAir;
		properties.requiresCorrectToolForDrops = pBlockBehaviour.properties.requiresCorrectToolForDrops;
		properties.jumpFactor = pBlockBehaviour.properties.jumpFactor;
		properties.drops = pBlockBehaviour.properties.drops;
		properties.ignitedByLava = pBlockBehaviour.properties.ignitedByLava;
		properties.forceSolidOn = pBlockBehaviour.properties.forceSolidOn;
		properties.pushReaction = pBlockBehaviour.properties.pushReaction;
		properties.spawnParticlesOnBreak = pBlockBehaviour.properties.spawnParticlesOnBreak;
		properties.instrument = pBlockBehaviour.properties.instrument;
		properties.replaceable = pBlockBehaviour.properties.replaceable;
		properties.isValidSpawn = pBlockBehaviour.properties.isValidSpawn;
		properties.isRedstoneConductor = pBlockBehaviour.properties.isRedstoneConductor;
		properties.isSuffocating = pBlockBehaviour.properties.isSuffocating;
		properties.isViewBlocking = pBlockBehaviour.properties.isViewBlocking;
		properties.emissiveRendering = pBlockBehaviour.properties.emissiveRendering;
		properties.requiredFeatures = pBlockBehaviour.properties.requiredFeatures;
		properties.offsetFunction = pBlockBehaviour.properties.offsetFunction;
		properties.hasPostProcess = pBlockBehaviour.properties.hasPostProcess;

		return properties;
	}

	public LodestoneBlockProperties addThrowawayData(Function<LodestoneThrowawayBlockData, LodestoneThrowawayBlockData> function) {
		ThrowawayBlockDataHandler.THROWAWAY_DATA_CACHE.put(this, function.apply(ThrowawayBlockDataHandler.THROWAWAY_DATA_CACHE.getOrDefault(this, new LodestoneThrowawayBlockData())));
		return this;
	}

	public LodestoneThrowawayBlockData getThrowawayData() {
		return ThrowawayBlockDataHandler.THROWAWAY_DATA_CACHE.getOrDefault(this, LodestoneThrowawayBlockData.EMPTY);
	}

	public LodestoneBlockProperties setCutoutRenderType() {
		return setRenderType(() -> RenderType::cutoutMipped);
	}

	public LodestoneBlockProperties setRenderType(Supplier<Supplier<RenderType>> renderType) {
		if (FMLEnvironment.dist.isClient()) {
			addThrowawayData(d -> d.setRenderType(renderType));
		}
		return this;
	}

	public LodestoneBlockProperties addDatagenData(Function<LodestoneDatagenBlockData, LodestoneDatagenBlockData> function) {
		if (DatagenModLoader.isRunningDataGen()) {
			ThrowawayBlockDataHandler.DATAGEN_DATA_CACHE.put(this, function.apply(ThrowawayBlockDataHandler.DATAGEN_DATA_CACHE.getOrDefault(this, new LodestoneDatagenBlockData())));
		}
		return this;
	}

	public LodestoneDatagenBlockData getDatagenData() {
		return ThrowawayBlockDataHandler.DATAGEN_DATA_CACHE.getOrDefault(this, LodestoneDatagenBlockData.EMPTY);
	}

	public LodestoneBlockProperties addTag(TagKey<Block> tag) {
		addDatagenData(d -> d.addTag(tag));
		return this;
	}

	@SafeVarargs
	public final LodestoneBlockProperties addTags(TagKey<Block>... tags) {
		addDatagenData(d -> d.addTags(tags));
		return this;
	}

	public LodestoneBlockProperties hasInheritedLoot() {
		addDatagenData(LodestoneDatagenBlockData::hasInheritedLoot);
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
	public LodestoneBlockProperties noLootTable() {
		return (LodestoneBlockProperties) super.noLootTable();
	}

	@Override
	@NotNull
	@SuppressWarnings("deprecation")
	public LodestoneBlockProperties dropsLike(@NotNull Block block) {
		if (DatagenModLoader.isRunningDataGen()) {
			getDatagenData().hasInheritedLootTable = true;
		}
		return (LodestoneBlockProperties) super.dropsLike(block);
	}

	@Override
	@NotNull
	public LodestoneBlockProperties lootFrom(@NotNull Supplier<? extends Block> blockIn) {
		hasInheritedLoot();
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
	public LodestoneBlockProperties mapColor(@NotNull Function<BlockState, MapColor> p_285406_) {
		return (LodestoneBlockProperties) super.mapColor(p_285406_);
	}

	@Override
	@NotNull
	public LodestoneBlockProperties mapColor(@NotNull DyeColor p_285331_) {
		return (LodestoneBlockProperties) super.mapColor(p_285331_);
	}

	@Override
	@NotNull
	public LodestoneBlockProperties mapColor(@NotNull MapColor p_285137_) {
		return (LodestoneBlockProperties) super.mapColor(p_285137_);
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

	@Override
	@NotNull
	public LodestoneBlockProperties ignitedByLava() {
		return (LodestoneBlockProperties) super.ignitedByLava();
	}

	@Override
	@NotNull
	public LodestoneBlockProperties liquid() {
		return (LodestoneBlockProperties) super.liquid();
	}

	@Override
	@NotNull
	public LodestoneBlockProperties forceSolidOn() {
		return (LodestoneBlockProperties) super.forceSolidOn();
	}

	@Override
	@NotNull
	public LodestoneBlockProperties pushReaction(@NotNull PushReaction p_278265_) {
		return (LodestoneBlockProperties) super.pushReaction(p_278265_);
	}

	@Override
	@NotNull
	public LodestoneBlockProperties offsetType(@NotNull BlockBehaviour.OffsetType pOffsetType) {
		return (LodestoneBlockProperties) super.offsetType(pOffsetType);
	}

	@Override
	@NotNull
	public LodestoneBlockProperties noParticlesOnBreak() {
		return (LodestoneBlockProperties) super.noParticlesOnBreak();
	}

	@Override
	@NotNull
	public LodestoneBlockProperties requiredFeatures(FeatureFlag @NotNull ... pRequiredFeatures) {
		return (LodestoneBlockProperties) super.requiredFeatures(pRequiredFeatures);
	}

	@Override
	@NotNull
	public LodestoneBlockProperties instrument(@NotNull NoteBlockInstrument p_282170_) {
		return (LodestoneBlockProperties) super.instrument(p_282170_);
	}

	@Override
	@NotNull
	public LodestoneBlockProperties replaceable() {
		return (LodestoneBlockProperties) super.replaceable();
	}
}