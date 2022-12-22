package team.lodestar.lodestone.handlers;

import net.minecraft.core.Direction;
import team.lodestar.lodestone.helpers.ItemHelper;
import team.lodestar.lodestone.setup.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.item.IEventResponderItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import team.lodestar.lodestone.systems.rendering.particle.ParticleBuilders;

import java.awt.*;

/**
 * A handler for firing {@link IEventResponderItem} events
 */
public class ItemEventHandler {

    public static void respondToDeath(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        LivingEntity target = event.getEntityLiving();
        LivingEntity attacker = null;
        if (event.getSource().getEntity() instanceof LivingEntity directAttacker) {
            attacker = directAttacker;
        }
        if (attacker == null) {
            attacker = target.getLastHurtByMob();
        }
        if (attacker != null) {
            LivingEntity finalAttacker = attacker;
            ItemHelper.getEventResponders(attacker).forEach(s -> ((IEventResponderItem) s.getItem()).killEvent(event, finalAttacker, target, s));
        }

        Color color = Color.WHITE;
        ParticleBuilders.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setAlpha(0.08f, 0.32f, 0)
                .setLifetime(15)
                .setSpin(0.2f)
                .setScale(0.15f, 0.2f, 0)
                .setScaleEasing(Easing.QUINTIC_OUT, Easing.SINE_IN)
                .setColor(color, color)
                .enableNoClip()
                .randomOffset(0.1f, 0f)
                .randomMotion(0.001f, 0.002f)
                .repeatSurroundBlock(target.level, target.blockPosition(), 6, Direction.UP, Direction.DOWN);

        ParticleBuilders.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setAlpha(0.04f, 0.16f, 0)
                .setLifetime(20)
                .setSpin(0.1f)
                .setScale(0.35f, 0.4f, 0)
                .setScaleEasing(Easing.QUINTIC_OUT, Easing.SINE_IN)
                .setColor(color, color)
                .randomOffset(0.2f, 0)
                .enableNoClip()
                .randomMotion(0.001f, 0.002f)
                .repeatSurroundBlock(target.level, target.blockPosition(), 6, Direction.UP, Direction.DOWN);
    }

    public static void respondToHurt(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0) {
            return;
        }
        LivingEntity target = event.getEntityLiving();
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ItemHelper.getEventResponders(attacker).forEach(s -> ((IEventResponderItem) s.getItem()).hurtEvent(event, attacker, target, s));
            ItemHelper.getEventResponders(target).forEach(s -> ((IEventResponderItem) s.getItem()).takeDamageEvent(event, attacker, target, s));
        }
    }
}