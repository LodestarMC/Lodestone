package team.lodestar.lodestone.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.function.Supplier;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    private Supplier<ResourceKey<LootTable>> lootTableSupplier;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lodestone$injectSupplier(CallbackInfo ci, @Local BlockBehaviour.Properties properties){
        var lootTableCache = properties.drops;
        if (lootTableCache != null) {
            this.lootTableSupplier = () -> lootTableCache;
        } else if (p_60452_.lootTableSupplier != null) {
            this.lootTableSupplier = p_60452_.lootTableSupplier;
        } else {
            this.lootTableSupplier = () -> {
                BlockBehaviour block = (BlockBehaviour) (Object) this;
                ResourceLocation resourcelocation = BuiltInRegistries.BLOCK.getKey(block.asBlock());
                return ResourceKey.create(Registries.LOOT_TABLE, resourcelocation.withPrefix("blocks/"));
            };
        }
    }
}
