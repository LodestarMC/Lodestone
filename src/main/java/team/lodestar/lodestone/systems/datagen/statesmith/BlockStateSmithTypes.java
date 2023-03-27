package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockStateSmithTypes {

    public static ModularBlockStateSmith<Block> CUSTOM_MODEL = new ModularBlockStateSmith<>(Block.class, (block, provider, stateFunction, modelFileSupplier) -> {
        stateFunction.act(block, modelFileSupplier.generateModel(block, provider, name -> getPath(block, name)));
    });

    public static BlockStateSmith<Block> FULL_BLOCK = new BlockStateSmith<>(Block.class, (block, provider) -> {
        provider.simpleBlock(block);
    });

    public static BlockStateSmith<Block> CROSS_MODEL_BLOCK = new BlockStateSmith<>(Block.class, (block, provider) -> {
        String name = getBlockName(block);
        provider.models().cross(name, getPath(block, name));
    });

    public static BlockStateSmith<Block> LEAVES_BLOCK = new BlockStateSmith<>(Block.class, (block, provider) -> {
        String name = getBlockName(block);
        ModelFile leaves = provider.models().withExistingParent(name, new ResourceLocation("block/leaves")).texture("all", getPath(block, name));
        provider.simpleBlock(block, leaves);
    });

    public static BlockStateSmith<RotatedPillarBlock> LOG_BLOCK = new BlockStateSmith<>(RotatedPillarBlock.class, (block, provider) -> {
        provider.logBlock(block);
    });

    public static BlockStateSmith<RotatedPillarBlock> WOOD_BLOCK = new BlockStateSmith<>(RotatedPillarBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String textureName = name + "_log";
        ResourceLocation logTexture = getPath(block, textureName);
        provider.axisBlock(block, logTexture, logTexture);
    });

    public static BlockStateSmith<DirectionalBlock> DIRECTIONAL_BLOCK = new BlockStateSmith<>(DirectionalBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        ResourceLocation textureName = getPath(block, name);
        BlockModelBuilder directionalModel = provider.models().cubeColumnHorizontal(name, textureName, extend(textureName, "_top"));
        provider.directionalBlock(block, directionalModel);
    });

    public static BlockStateSmith<StairBlock> STAIRS_BLOCK = new BlockStateSmith<>(StairBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String textureName = name.replace("_stairs", "");
        provider.stairsBlock(block, getPath(block, textureName));
    });

    public static BlockStateSmith<SlabBlock> SLAB_BLOCK = new BlockStateSmith<>(SlabBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String textureName = name.replace("_slab", "");
        provider.slabBlock(block, getPath(block, textureName), getPath(block, textureName));
    });

    public static BlockStateSmith<WallBlock> WALL_BLOCK = new BlockStateSmith<>(WallBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String textureName = name.replace("_wall", "");
        provider.wallBlock(block, getPath(block, textureName));
    });

    public static BlockStateSmith<FenceBlock> FENCE_BLOCK = new BlockStateSmith<>(FenceBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String textureName = name.replace("_fence", "");
        provider.fenceBlock(block, getPath(block, textureName));
    });

    public static BlockStateSmith<FenceGateBlock> FENCE_GATE_BLOCK = new BlockStateSmith<>(FenceGateBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String textureName = name.replace("_fence_gate", "");
        provider.fenceGateBlock(block, getPath(block, textureName));
    });

    public static BlockStateSmith<PressurePlateBlock> PRESSURE_PLATE_BLOCK = new BlockStateSmith<>(PressurePlateBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String textureName = name.replace("_pressure_plate", "");
        provider.pressurePlateBlock(block, getPath(block, textureName));
    });

    public static BlockStateSmith<ButtonBlock> BUTTON_BLOCK = new BlockStateSmith<>(ButtonBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String textureName = name.replace("_button", "");
        provider.buttonBlock(block, getPath(block, textureName));
    });

    public static BlockStateSmith<DoorBlock> DOOR_BLOCK = new BlockStateSmith<>(DoorBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        provider.doorBlock(block, getPath(block, name + "_bottom"), getPath(block, name + "_top"));
    });

    public static BlockStateSmith<TrapDoorBlock> TRAPDOOR_BLOCK = new BlockStateSmith<>(TrapDoorBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        provider.trapdoorBlock(block, getPath(block, name), true);
    });

    public static BlockStateSmith<TorchBlock> TORCH_BLOCK = new BlockStateSmith<>(TorchBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        ModelFile torchModel = provider.models().torch(getBlockName(block), getPath(block, name));
        provider.getVariantBuilder(block).forAllStates(s -> ConfiguredModel.builder().modelFile(torchModel).build());
    });

    public static BlockStateSmith<WallTorchBlock> WALL_TORCH_BLOCK = new BlockStateSmith<>(WallTorchBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String textureName = name.replace("wall_", "");
        ModelFile torchModel = provider.models().torchWall(getBlockName(block), getPath(block, textureName));
        provider.horizontalBlock(block, torchModel);
    });

    public static BlockStateSmith<SignBlock> WOODEN_SIGN_BLOCK = new BlockStateSmith<>(SignBlock.class, (block, provider) -> {
        String name = getBlockName(block);
        String particleTextureName = name.replace("_wall", "").replace("_sign", "") + "_planks";
        provider.getVariantBuilder(block).forAllStates(s -> ConfiguredModel.builder().modelFile(provider.models().sign(name, getPath(block, particleTextureName))).build());
    });

    public static String getBlockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public static String getModIdFromBlock(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getNamespace();
    }

    public static ResourceLocation getPath(Block block, String path) {
        return new ResourceLocation(getModIdFromBlock(block), "block/"+path);
    }

    public static ResourceLocation extend(ResourceLocation resourceLocation, String suffix) {
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + suffix);
    }
}