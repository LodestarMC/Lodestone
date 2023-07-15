package team.lodestar.lodestone.systems.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;
import team.lodestar.lodestone.systems.datagen.LodestoneDatagenBlockData;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A wrapper for Block Properties, allowing you to add {@link LodestoneThrowawayBlockData}
 * Some of these values are optional; they only get added during datagen.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class LodestoneBlockProperties {

	private final BlockBehaviour.Properties properties;

	private LodestoneBlockProperties(@NotNull BlockBehaviour.Properties properties) {
		this.properties = properties;
	}

	public static LodestoneBlockProperties of(@NotNull BlockBehaviour.Properties properties) {
		return new LodestoneBlockProperties(properties);
	}

	public Block.Properties getProperties() {
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
}