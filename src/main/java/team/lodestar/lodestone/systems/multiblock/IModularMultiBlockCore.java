package team.lodestar.lodestone.systems.multiblock;

import net.minecraft.core.BlockPos;
import team.lodestar.lodestone.helpers.NBTHelper;

public interface IModularMultiBlockCore extends IMultiBlockCore {

    @Override
    default boolean isModular() {
        return true;
    }

    void separate(BlockPos pos);

    void connect(BlockPos pos);

    void reset();
}