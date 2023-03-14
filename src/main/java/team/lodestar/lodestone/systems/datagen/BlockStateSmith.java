package team.lodestar.lodestone.systems.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import team.lodestar.lodestone.LodestoneLib;

public abstract class BlockStateSmith<T extends Block> {

    public void act(BlockStateProvider provider, Block block) {
        if (getBlockClass().isInstance(block)) {
            act(getBlockClass().cast(block), provider);
        }
        else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + block);
        }
    }

    protected abstract void act(T block, BlockStateProvider provider);

    abstract Class<T> getBlockClass();
    BlockStateSmith<Block> FULL_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(Block b, BlockStateProvider p) {
            p.simpleBlock(b);
        }

        @Override
        Class<Block> getBlockClass() {
            return Block.class;
        }
    };

    BlockStateSmith<Block> CROSS_MODEL_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(Block b, BlockStateProvider p) {
            String name = getBlockName(b);
            p.models().cross(name, getPath(b, "block/"+name));
        }

        @Override
        Class<Block> getBlockClass() {
            return Block.class;
        }
    };

    BlockStateSmith<RotatedPillarBlock> LOG_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(RotatedPillarBlock b, BlockStateProvider p) {
            p.logBlock(b);
        }

        @Override
        Class<RotatedPillarBlock> getBlockClass() {
            return RotatedPillarBlock.class;
        }
    };

    BlockStateSmith<RotatedPillarBlock> WOOD_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(RotatedPillarBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String textureName = name + "_log";
            ResourceLocation path = getPath(b, "block/" + textureName);
            p.axisBlock(b, path, path);
        }

        @Override
        Class<RotatedPillarBlock> getBlockClass() {
            return RotatedPillarBlock.class;
        }
    };

    BlockStateSmith<StairBlock> STAIRS_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(StairBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String textureName = name.replace("_stairs", "");
            p.stairsBlock(b, getPath(b, "block/"+textureName));
        }

        @Override
        Class<StairBlock> getBlockClass() {
            return StairBlock.class;
        }
    };

    BlockStateSmith<SlabBlock> SLAB_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(SlabBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String textureName = name.replace("_slab", "");
            p.slabBlock(b, getPath(b, textureName), getPath(b, "block/"+textureName));
        }

        @Override
        Class<SlabBlock> getBlockClass() {
            return SlabBlock.class;
        }
    };

    BlockStateSmith<WallBlock> WALL_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(WallBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String textureName = name.replace("_wall", "");
            p.wallBlock(b, getPath(b, "block/"+textureName));
        }

        @Override
        Class<WallBlock> getBlockClass() {
            return WallBlock.class;
        }
    };

    BlockStateSmith<FenceBlock> FENCE_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(FenceBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String textureName = name.replace("_fence", "");
            p.fenceBlock(b, getPath(b, "block/"+textureName));
        }

        @Override
        Class<FenceBlock> getBlockClass() {
            return FenceBlock.class;
        }
    };

    BlockStateSmith<FenceGateBlock> FENCE_GATE_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(FenceGateBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String textureName = name.replace("_fence_gate", "");
            p.fenceGateBlock(b, getPath(b, "block/"+textureName));
        }

        @Override
        Class<FenceGateBlock> getBlockClass() {
            return FenceGateBlock.class;
        }
    };

    BlockStateSmith<PressurePlateBlock> PRESSURE_PLATE_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(PressurePlateBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String textureName = name.replace("_pressure_plate", "");
            p.pressurePlateBlock(b, getPath(b, "block/"+textureName));
        }

        @Override
        Class<PressurePlateBlock> getBlockClass() {
            return PressurePlateBlock.class;
        }
    };

    BlockStateSmith<ButtonBlock> BUTTON_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(ButtonBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String textureName = name.replace("_button", "");
            p.buttonBlock(b, getPath(b, "block/"+textureName));
        }

        @Override
        Class<ButtonBlock> getBlockClass() {
            return ButtonBlock.class;
        }
    };

    BlockStateSmith<DoorBlock> DOOR_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(DoorBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            p.doorBlock(b, getPath(b, "block/"+name+"_bottom"), getPath(b, "block/"+name+"_top"));
        }

        @Override
        Class<DoorBlock> getBlockClass() {
            return DoorBlock.class;
        }
    };

    BlockStateSmith<TrapDoorBlock> TRAPDOOR_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(TrapDoorBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            p.trapdoorBlock(b, getPath(b, "block/"+name), true);
        }

        @Override
        Class<TrapDoorBlock> getBlockClass() {
            return TrapDoorBlock.class;
        }
    };

    BlockStateSmith<TorchBlock> TORCH_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(TorchBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            ModelFile torchModel = p.models().torch(getBlockName(b), getPath(b, "block/" + name));
            p.getVariantBuilder(b).forAllStates(s -> ConfiguredModel.builder().modelFile(torchModel).build());
        }

        @Override
        Class<TorchBlock> getBlockClass() {
            return TorchBlock.class;
        }
    };

    BlockStateSmith<WallTorchBlock> WALL_TORCH_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(WallTorchBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String textureName = name.replace("wall_", "");
            ModelFile torchModel = p.models().torchWall(getBlockName(b), getPath(b, "block/" + textureName));
            horizontalFacingBlock(b, p, torchModel);
        }

        @Override
        Class<WallTorchBlock> getBlockClass() {
            return WallTorchBlock.class;
        }
    };

    BlockStateSmith<SignBlock> WOODEN_SIGN_BLOCK = new BlockStateSmith<>() {
        @Override
        protected void act(SignBlock b, BlockStateProvider p) {
            String name = getBlockName(b);
            String particleTextureName = name.replace("_wall", "").replace("_sign", "") + "_planks";
            p.getVariantBuilder(b).forAllStates(s -> ConfiguredModel.builder().modelFile(p.models().sign(name, getPath(b, particleTextureName))).build());
        }

        @Override
        Class<SignBlock> getBlockClass() {
            return SignBlock.class;
        }
    };

    static void horizontalFacingBlock(Block block, BlockStateProvider provider, ModelFile modelFile) {
        provider.getVariantBuilder(block).forAllStates(s -> ConfiguredModel.builder().modelFile(modelFile).rotationY(((int) s.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build());
    }

    static String getBlockName(Block block) {
        return block.getRegistryName().getPath();
    }

    private static String getModIdFromBlock(Block block) {
        return block.getRegistryName().getNamespace();
    }

    private static ResourceLocation getPath(Block block, String path) {
        return new ResourceLocation(getModIdFromBlock(block), path);
    }
}
