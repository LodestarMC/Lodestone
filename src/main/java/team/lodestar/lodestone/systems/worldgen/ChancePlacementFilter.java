package team.lodestar.lodestone.systems.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import team.lodestar.lodestone.registry.common.*;

public class ChancePlacementFilter extends PlacementFilter {

    public static final MapCodec<ChancePlacementFilter> CODEC = RecordCodecBuilder.mapCodec(
            p_191953_ -> p_191953_.group(ExtraCodecs.POSITIVE_FLOAT.fieldOf("chance").forGetter(obj -> obj.chance))
                    .apply(p_191953_, ChancePlacementFilter::new)
    );

    private final float chance;

    public ChancePlacementFilter(float chance) {
        this.chance = chance;
    }

    public PlacementModifierType<?> type() {
        return LodestonePlacementFillers.CHANCE.get();
    }

    @Override
    protected boolean shouldPlace(PlacementContext pContext, RandomSource pRandom, BlockPos pPos) {
        return pRandom.nextFloat() < chance;
    }
}