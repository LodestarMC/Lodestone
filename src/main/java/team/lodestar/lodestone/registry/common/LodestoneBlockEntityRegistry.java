package team.lodestar.lodestone.registry.common;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.lodestar.lodestone.systems.block.sign.LodestoneStandingSignBlock;
import team.lodestar.lodestone.systems.block.sign.LodestoneWallSignBlock;
import team.lodestar.lodestone.systems.blockentity.LodestoneSignBlockEntity;
import team.lodestar.lodestone.systems.multiblock.ILodestoneMultiblockComponent;
import team.lodestar.lodestone.systems.multiblock.MultiBlockComponentEntity;

import java.util.ArrayList;
import java.util.Arrays;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;


public class LodestoneBlockEntityRegistry {

    public static final LazyRegistrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = LazyRegistrar.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, LODESTONE);

    public static final RegistryObject<BlockEntityType<MultiBlockComponentEntity>> MULTIBLOCK_COMPONENT = BLOCK_ENTITY_TYPES.register("multiblock_component", () -> BlockEntityType.Builder.of(MultiBlockComponentEntity::new, getBlocks(ILodestoneMultiblockComponent.class)).build(null));
    public static final RegistryObject<BlockEntityType<LodestoneSignBlockEntity>> SIGN = BLOCK_ENTITY_TYPES.register("sign", () -> BlockEntityType.Builder.of(LodestoneSignBlockEntity::new, getBlocks(LodestoneStandingSignBlock.class, LodestoneWallSignBlock.class)).build(null));

    public static Block[] getBlocks(Class<?>... blockClasses) {

        var blocks = BuiltInRegistries.BLOCK;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (Arrays.stream(blockClasses).anyMatch(b -> b.isInstance(block))) {
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }

    public static Block[] getBlocksExact(Class<?> clazz) {
        var blocks = BuiltInRegistries.BLOCK;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (clazz.equals(block.getClass())) {
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }

    public static class ClientOnly {

        public static void registerRenderer() {
            BlockEntityRenderers.register(SIGN.get(), SignRenderer::new);
        }
    }
}