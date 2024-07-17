package team.lodestar.lodestone.systems.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LodestoneBoatEntity extends Boat {
    private final Supplier<Item> boatItem;

    /**
     * @deprecated Use {@link LodestoneBoatEntity#LodestoneBoatEntity(EntityType, Level, Supplier)}} instead
     */
    @Deprecated
    public LodestoneBoatEntity(EntityType<? extends LodestoneBoatEntity> type, Level level, Supplier<Item> boatItem, Supplier<Item> plankItem) {
        this(type, level, boatItem);
    }

    public LodestoneBoatEntity(EntityType<? extends LodestoneBoatEntity> type, Level level, Supplier<Item> boatItem) {
        super(type, level);
        this.boatItem = boatItem;
    }

    @Override
    protected void checkFallDamage(double dY, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        this.lastYd = this.getDeltaMovement().y;
        if (!this.isPassenger()) {
            if (onGround) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != Status.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.causeFallDamage(this.fallDistance, 1.0F, level().damageSources().fall());
                    if (!this.level().isClientSide && !this.isRemoved()) {
                        this.kill();
                        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (int i = 0; i < 3; ++i) {
                                this.spawnAtLocation(this.getVariant().getPlanks());
                            }

                            for (int j = 0; j < 2; ++j) {
                                this.spawnAtLocation(Items.STICK);
                            }
                        }
                    }
                }

                this.fallDistance = 0.0F;
            } else if (!this.level().getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && dY < 0.0D) {
                this.fallDistance = (float) ((double) this.fallDistance - dY);
            }

        }
    }

    @Override
    @NotNull
    public Item getDropItem() {
        return boatItem.get();
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
