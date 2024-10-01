package team.lodestar.lodestone.systems.particle.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import team.lodestar.lodestone.systems.particle.world.options.LodestoneItemCrumbsParticleOptions;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

public class LodestoneItemCrumbParticle extends LodestoneWorldParticle {

    private final float uo;
    private final float vo;

    public LodestoneItemCrumbParticle(ClientLevel world, LodestoneItemCrumbsParticleOptions data, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, null, x, y, z, xd, yd, zd);
        var model = Minecraft.getInstance().getItemRenderer().getModel(data.stack, world, null, 0);
        this.setSprite(model.getOverrides().resolve(model, data.stack, world, null, 0).getParticleIcon(ModelData.EMPTY));
        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    @Override
    public float getU0() {
        return this.sprite.getU(((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    public float getU1() {
        return this.sprite.getU((this.uo / 4.0F * 16.0F));
    }

    @Override
    public float getV0() {
        return this.sprite.getV((this.vo / 4.0F * 16.0F));
    }

    @Override
    public float getV1() {
        return this.sprite.getV(((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    public int getLightColor(float pPartialTick) {
        BlockPos blockpos = new BlockPos((int) this.x, (int) this.y, (int) this.z);
        return this.level.hasChunkAt(blockpos) ? LevelRenderer.getLightColor(this.level, blockpos) : 0;
    }
}