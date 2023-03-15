package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class BlockStateSmithTypes {

    public static ModelFuncBlockStateSmith<Block> PREDEFINED_MODEL = new ModelFuncBlockStateSmith<>(Block.class, (b, p, s) -> {
        String name = getBlockName(b);
        ModelFile predefinedModel = p.models().getExistingFile(getPath(b, "block/" + name));
        s.act(b, p, predefinedModel);
    });

    public static BlockStateSmith<Block> FULL_BLOCK = new BlockStateSmith<>(Block.class, (b, p) -> p.simpleBlock(b));

    public static BlockStateSmith<Block> CROSS_MODEL_BLOCK = new BlockStateSmith<>(Block.class, (b, p) -> {
        String name = getBlockName(b);
        p.models().cross(name, getPath(b, "block/" + name));
    });

    public static BlockStateSmith<Block> LEAVES_BLOCK = new BlockStateSmith<>(Block.class, (b, p) -> {
        String name = getBlockName(b);
        ModelFile leaves = p.models().withExistingParent(name, new ResourceLocation("block/leaves")).texture("all", getPath(b, "block/" + name));
        p.simpleBlock(b, leaves);
    });

    public static BlockStateSmith<RotatedPillarBlock> LOG_BLOCK = new BlockStateSmith<>(RotatedPillarBlock.class, (b, p) -> {
        p.logBlock(b);
    });

    public static BlockStateSmith<RotatedPillarBlock> WOOD_BLOCK = new BlockStateSmith<>(RotatedPillarBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String textureName = name + "_log";
        ResourceLocation path = getPath(b, "block/" + textureName);
        p.axisBlock(b, path, path);
    });

    public static BlockStateSmith<StairBlock> STAIRS_BLOCK = new BlockStateSmith<>(StairBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String textureName = name.replace("_stairs", "");
        p.stairsBlock(b, getPath(b, "block/" + textureName));
    });

    public static BlockStateSmith<SlabBlock> SLAB_BLOCK = new BlockStateSmith<>(SlabBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String textureName = name.replace("_slab", "");
        p.slabBlock(b, getPath(b, textureName), getPath(b, "block/" + textureName));
    });

    public static BlockStateSmith<WallBlock> WALL_BLOCK = new BlockStateSmith<>(WallBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String textureName = name.replace("_wall", "");
        p.wallBlock(b, getPath(b, "block/" + textureName));
    });

    public static BlockStateSmith<FenceBlock> FENCE_BLOCK = new BlockStateSmith<>(FenceBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String textureName = name.replace("_fence", "");
        p.fenceBlock(b, getPath(b, "block/" + textureName));
    });

    public static BlockStateSmith<FenceGateBlock> FENCE_GATE_BLOCK = new BlockStateSmith<>(FenceGateBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String textureName = name.replace("_fence_gate", "");
        p.fenceGateBlock(b, getPath(b, "block/" + textureName));
    });

    public static BlockStateSmith<PressurePlateBlock> PRESSURE_PLATE_BLOCK = new BlockStateSmith<>(PressurePlateBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String textureName = name.replace("_pressure_plate", "");
        p.pressurePlateBlock(b, getPath(b, "block/" + textureName));
    });

    public static BlockStateSmith<ButtonBlock> BUTTON_BLOCK = new BlockStateSmith<>(ButtonBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String textureName = name.replace("_button", "");
        p.buttonBlock(b, getPath(b, "block/" + textureName));
    });

    public static BlockStateSmith<DoorBlock> DOOR_BLOCK = new BlockStateSmith<>(DoorBlock.class, (b, p) -> {
        String name = getBlockName(b);
        p.doorBlock(b, getPath(b, "block/" + name + "_bottom"), getPath(b, "block/" + name + "_top"));
    });

    public static BlockStateSmith<TrapDoorBlock> TRAPDOOR_BLOCK = new BlockStateSmith<>(TrapDoorBlock.class, (b, p) -> {
        String name = getBlockName(b);
        p.trapdoorBlock(b, getPath(b, "block/" + name), true);
    });

    public static BlockStateSmith<TorchBlock> TORCH_BLOCK = new BlockStateSmith<>(TorchBlock.class, (b, p) -> {
        String name = getBlockName(b);
        ModelFile torchModel = p.models().torch(getBlockName(b), getPath(b, "block/" + name));
        p.getVariantBuilder(b).forAllStates(s -> ConfiguredModel.builder().modelFile(torchModel).build());
    });

    public static BlockStateSmith<WallTorchBlock> WALL_TORCH_BLOCK = new BlockStateSmith<>(WallTorchBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String textureName = name.replace("wall_", "");
        ModelFile torchModel = p.models().torchWall(getBlockName(b), getPath(b, "block/" + textureName));
        p.horizontalBlock(b, torchModel);
    });

    public static BlockStateSmith<SignBlock> WOODEN_SIGN_BLOCK = new BlockStateSmith<>(SignBlock.class, (b, p) -> {
        String name = getBlockName(b);
        String particleTextureName = name.replace("_wall", "").replace("_sign", "") + "_planks";
        p.getVariantBuilder(b).forAllStates(s -> ConfiguredModel.builder().modelFile(p.models().sign(name, getPath(b, "block/" + particleTextureName))).build());
    });

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