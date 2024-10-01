package team.lodestar.lodestone.registry.common;

import io.github.fabricators_of_create.porting_lib.util.DeferredRegister;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.core.DefaultedRegistry;
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
import java.util.function.Supplier;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;


public class LodestoneBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, LODESTONE);

    public static final Supplier<BlockEntityType<MultiBlockComponentEntity>> MULTIBLOCK_COMPONENT = BLOCK_ENTITY_TYPES.register("multiblock_component", () -> BlockEntityType.Builder.of(MultiBlockComponentEntity::new, getBlocks(ILodestoneMultiblockComponent.class)).build(null));
    public static final Supplier<BlockEntityType<LodestoneSignBlockEntity>> SIGN = BLOCK_ENTITY_TYPES.register("sign", () -> BlockEntityType.Builder.of(LodestoneSignBlockEntity::new, getBlocks(LodestoneStandingSignBlock.class, LodestoneWallSignBlock.class)).build(null));

    public static Block[] getBlocks(Class<?>... blockClasses) {
        DefaultedRegistry<Block> blocks = BuiltInRegistries.BLOCK;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (Arrays.stream(blockClasses).anyMatch(b -> b.isInstance(block))) {
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }

    public static Block[] getBlocksExact(Class<?> clazz) {
        DefaultedRegistry<Block> blocks = BuiltInRegistries.BLOCK;
        ArrayList<Block> matchingBlocks = new ArrayList<>();
        for (Block block : blocks) {
            if (clazz.equals(block.getClass())) {
                matchingBlocks.add(block);
            }
        }
        return matchingBlocks.toArray(new Block[0]);
    }

    public static class ClientOnly {

        public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
               event.registerBlockEntityRenderer(SIGN.get(), SignRenderer::new);
        }
    }
}