package team.lodestar.lodestone.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import team.lodestar.lodestone.handlers.FireEffectHandler;
import team.lodestar.lodestone.helpers.NBTHelper;
import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;

public class LodestoneEntityComponent implements AutoSyncedComponent {

    public FireEffectInstance fireEffectInstance;

    private final Entity entity;

    public LodestoneEntityComponent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        FireEffectHandler.deserializeNBT(this, tag);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        FireEffectHandler.serializeNBT(this, tag);
    }
}
