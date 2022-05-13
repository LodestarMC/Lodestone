package com.sammy.ortus.systems.blockentity.behaviour;

import com.sammy.ortus.systems.rendering.outline.ChasingAABBOutline;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ValueBox extends ChasingAABBOutline {
    protected Component label;
    protected Component sublabel = TextComponent.EMPTY;
    protected Component scrollTooltip = TextComponent.EMPTY;
    protected Vec3 labelOffset = Vec3.ZERO;

    protected BlockPos pos;
    protected BlockState blockState;

    public ValueBox(Component label, AABB bb, BlockPos pos) {
        super(bb);
        this.label = label;
        this.pos = pos;
        this.blockState = Minecraft.getInstance().level.getBlockState(pos);
    }
}
