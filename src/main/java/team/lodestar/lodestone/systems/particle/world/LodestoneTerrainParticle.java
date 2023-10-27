package team.lodestar.lodestone.systems.particle.world;

import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.*;
import team.lodestar.lodestone.systems.particle.options.*;

public class LodestoneTerrainParticle extends GenericParticle<LodestoneTerrainParticleOptions> {

    private final BlockPos blockPos;
    private final float uo;
    private final float vo;

    public LodestoneTerrainParticle(ClientLevel world, LodestoneTerrainParticleOptions data, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, null, x, y, z, xd, yd, zd);
        this.blockPos = data.blockPos;
        if (!data.blockState.is(Blocks.GRASS_BLOCK)) {
            int i = Minecraft.getInstance().getBlockColors().getColor(data.blockState, world, data.blockPos, 0);
            this.rCol *= (float) (i >> 16 & 255) / 255.0F;
            this.gCol *= (float) (i >> 8 & 255) / 255.0F;
            this.bCol *= (float) (i & 255) / 255.0F;
        }

        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;

        setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(data.blockState));
    }


    @Override
    protected float getU0() {
        return this.sprite.getU(((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    protected float getU1() {
        return this.sprite.getU((this.uo / 4.0F * 16.0F));
    }

    @Override
    protected float getV0() {
        return this.sprite.getV((this.vo / 4.0F * 16.0F));
    }

    @Override
    protected float getV1() {
        return this.sprite.getV(((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    public int getLightColor(float pPartialTick) {
        int i = super.getLightColor(pPartialTick);
        return i == 0 && this.level.hasChunkAt(this.blockPos) ? LevelRenderer.getLightColor(this.level, this.blockPos) : i;
    }
}