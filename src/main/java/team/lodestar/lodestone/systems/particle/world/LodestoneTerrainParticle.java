package team.lodestar.lodestone.systems.particle.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import team.lodestar.lodestone.systems.particle.world.options.LodestoneTerrainParticleOptions;

public class LodestoneTerrainParticle extends LodestoneWorldParticle {

    private final BlockPos blockPos;
    private final float uo;
    private final float vo;

    public LodestoneTerrainParticle(ClientLevel world, LodestoneTerrainParticleOptions data, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, null, x, y, z, xd, yd, zd);
        this.blockPos = data.blockPos;
        var state = data.blockState;
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
        this.gravity = 1.0F;
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;
        if (net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions.of(state).areBreakingParticlesTinted(state, level, blockPos)) {
            int i = Minecraft.getInstance().getBlockColors().getColor(state, level, blockPos, 0);
            this.rCol *= (float)(i >> 16 & 0xFF) / 255.0F;
            this.gCol *= (float)(i >> 8 & 0xFF) / 255.0F;
            this.bCol *= (float)(i & 0xFF) / 255.0F;
        }

        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    @Override
    public float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F);
    }

    @Override
    public float getU1() {
        return this.sprite.getU(this.uo / 4.0F);
    }

    @Override
    public float getV0() {
        return this.sprite.getV(this.vo / 4.0F);
    }

    @Override
    public float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F);
    }

    @Override
    public int getLightColor(float pPartialTick) {
        int i = super.getLightColor(pPartialTick);
        return i == 0 && this.level.hasChunkAt(this.blockPos) ? LevelRenderer.getLightColor(this.level, this.blockPos) : i;
    }
}