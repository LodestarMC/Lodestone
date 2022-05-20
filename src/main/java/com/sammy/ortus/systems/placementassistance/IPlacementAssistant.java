package com.sammy.ortus.systems.placementassistance;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Predicate;

public interface IPlacementAssistant {

    public void onPlace(Level level, BlockHitResult hit, BlockState blockState);

    public void assist(Level level, BlockHitResult hit, BlockState blockState);

    @OnlyIn(Dist.CLIENT)
    public void showAssistance(ClientLevel level, BlockHitResult hit, BlockState blockState);

    public Predicate<ItemStack> canAssist();
}
