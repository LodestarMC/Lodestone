package team.lodestar.lodestone.systems.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import team.lodestar.lodestone.registry.common.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DimensionPlacementFilter extends PlacementFilter {
    public static final Codec<DimensionPlacementFilter> CODEC = RecordCodecBuilder.create((codec) -> codec.group(
            ResourceLocation.CODEC.listOf().fieldOf("dimensions").forGetter(o -> o.dimensions.stream().map(ResourceKey::location).collect(Collectors.toList()))).apply(codec, (r) -> new DimensionPlacementFilter(r.stream().map(o -> ResourceKey.create(Registries.DIMENSION, o)).collect(Collectors.toSet()))));

    private final Set<ResourceKey<Level>> dimensions;

    protected DimensionPlacementFilter(Set<ResourceKey<Level>> dimensions) {
        this.dimensions = dimensions;
    }

    public static DimensionPlacementFilter of(Set<ResourceKey<Level>> dimensions) {
        return new DimensionPlacementFilter(dimensions);
    }

    @Override
    public PlacementModifierType<?> type() {
        return LodestonePlacementFillerRegistry.DIMENSION;
    }

    public static Set<ResourceKey<Level>> fromStrings(List<? extends String> dimensions) {
        return dimensions.stream().map(o -> ResourceKey.create(Registries.DIMENSION, (ResourceLocation.parse(o)))).collect(Collectors.toSet());
    }

    @Override
    protected boolean shouldPlace(PlacementContext pContext, RandomSource pRandom, BlockPos pPos) {
        ServerLevel level = pContext.getLevel().getLevel();
        return dimensions.contains(level.dimension());
    }
}