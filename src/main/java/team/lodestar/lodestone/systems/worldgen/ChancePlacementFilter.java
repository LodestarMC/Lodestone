package team.lodestar.lodestone.systems.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import team.lodestar.lodestone.registry.common.*;

public class ChancePlacementFilter extends PlacementFilter {
    public static final Codec<ChancePlacementFilter> CODEC = ExtraCodecs.POSITIVE_FLOAT.fieldOf("chance").xmap(ChancePlacementFilter::new, (p_191907_) -> p_191907_.chance).codec();
    private final float chance;

    public ChancePlacementFilter(float chance) {
        this.chance = chance;
    }

    public PlacementModifierType<?> type() {
        return LodestonePlacementFillers.CHANCE;
    }

    @Override
    protected boolean shouldPlace(PlacementContext pContext, RandomSource pRandom, BlockPos pPos) {
        return pRandom.nextFloat() < chance;
    }
}