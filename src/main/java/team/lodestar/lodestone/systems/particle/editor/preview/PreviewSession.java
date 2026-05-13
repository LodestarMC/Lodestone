package team.lodestar.lodestone.systems.particle.editor.preview;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.editor.EditorState;
import team.lodestar.lodestone.systems.particle.editor.data.ParticleOutputMode;
import team.lodestar.lodestone.systems.particle.editor.data.PreviewPlaybackMode;
import team.lodestar.lodestone.systems.particle.editor.project.EditorProject;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class PreviewSession {
    private static final Set<LodestoneWorldParticle> PARTICLES = Collections.newSetFromMap(new IdentityHashMap<>());
    private static final List<PreviewEmitter> EMITTERS = new ArrayList<>();
    private static boolean active;

    private PreviewSession() {
    }

    public static void start(List<EditorProject> projects) {
        stop(true);
        for (EditorProject project : projects) {
            if (project.visible && project.state.outputMode == ParticleOutputMode.WORLD) {
                EMITTERS.add(new PreviewEmitter(project.state.copy()));
            }
        }
        active = !EMITTERS.isEmpty();
    }

    public static void tick(Minecraft client) {
        if (!active) {
            return;
        }
        ClientLevel level = client.level;
        if (level == null || client.player == null) {
            stop(false);
            return;
        }
        PARTICLES.removeIf(particle -> !particle.isAlive());
        for (PreviewEmitter emitter : EMITTERS) {
            EditorState state = emitter.state;
            if (!state.autoPreview) {
                continue;
            }
            if (state.previewPlayback == PreviewPlaybackMode.ONE_SHOT && emitter.spawned) {
                continue;
            }
            emitter.ticks--;
            if (emitter.ticks <= 0) {
                spawn(level, client, state);
                emitter.spawned = true;
                if (state.previewPlayback == PreviewPlaybackMode.LOOPING) {
                    emitter.ticks = Math.max(1, state.previewInterval);
                }
            }
        }
    }

    public static void stop(boolean clearParticles) {
        if (clearParticles) {
            PARTICLES.forEach(LodestoneWorldParticle::remove);
        }
        PARTICLES.clear();
        EMITTERS.clear();
        active = false;
    }

    public static boolean isActive() {
        return active;
    }

    private static void spawn(ClientLevel level, Minecraft client, EditorState state) {
        Vec3 pos = previewPosition(client, state);
        for (int i = 0; i < state.spawnCount; i++) {
            state.createBuilder().addSpawnActor(PARTICLES::add).spawn(level, pos.add(state.createWorldDistributionOffset(level.random)));
        }
    }

    private static Vec3 previewPosition(Minecraft client, EditorState state) {
        if (!state.followCamera) {
            return state.fixedPreviewPosition();
        }
        Vec3 look = client.player.getLookAngle();
        return client.player.getEyePosition().add(look.scale(state.previewDistance)).add(0, state.previewYOffset, 0);
    }

    private static class PreviewEmitter {
        final EditorState state;
        int ticks;
        boolean spawned;

        PreviewEmitter(EditorState state) {
            this.state = state;
        }
    }
}
