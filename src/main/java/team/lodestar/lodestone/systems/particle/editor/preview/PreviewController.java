package team.lodestar.lodestone.systems.particle.editor.preview;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen;
import team.lodestar.lodestone.systems.particle.editor.EditorState;
import team.lodestar.lodestone.systems.particle.editor.data.LivePreviewScope;
import team.lodestar.lodestone.systems.particle.editor.data.ParticleOutputMode;
import team.lodestar.lodestone.systems.particle.editor.data.PreviewPlaybackMode;
import team.lodestar.lodestone.systems.particle.editor.data.SpawnDistributionMode;
import team.lodestar.lodestone.systems.particle.editor.project.EditorProject;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PreviewController {
    private final ParticleEditorScreen screen;
    private final List<EditorProject> projects;
    private final Set<LodestoneWorldParticle> particles = Collections.newSetFromMap(new IdentityHashMap<>());
    private final ScreenParticleHolder screenParticles = new ScreenParticleHolder();
    private final Map<EditorProject, Integer> livePreviewTicks = new IdentityHashMap<>();
    private final Set<EditorProject> livePreviewOneShots = Collections.newSetFromMap(new IdentityHashMap<>());
    private String lastPreviewSnapshot;

    public PreviewController(ParticleEditorScreen screen, List<EditorProject> projects) {
        this.screen = screen;
        this.projects = projects;
    }

    public void init() {
        PreviewSession.stop(false);
        initializeFixedPreviewPositions();
        resetLivePreviewTimers();
        captureSnapshot();
    }

    public void tickParticles() {
        prunePreviewParticles();
        screenParticles.tick();
    }

    public void renderScreenParticles(GuiGraphics graphics) {
        screenParticles.render(graphics);
    }

    public void spawnPreviewWithEffects() {
        spawnPreview(screen.activeProject(), true);
    }

    public void spawnAllPreviews() {
        int spawned = 0;
        for (EditorProject project : projects) {
            if (project.visible) {
                spawnPreview(project, true);
                spawned++;
            }
        }
        screen.message("Spawned visible particle projects: " + spawned);
    }

    public void tickLivePreview() {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) {
            return;
        }
        Set<EditorProject> previewedProjects = Collections.newSetFromMap(new IdentityHashMap<>());
        for (EditorProject project : projects) {
            if (!shouldLivePreview(project)) {
                continue;
            }
            previewedProjects.add(project);
            EditorState previewState = project.state;
            if (previewState.previewPlayback != PreviewPlaybackMode.LOOPING) {
                if (!livePreviewOneShots.contains(project)) {
                    spawnPreview(project);
                    livePreviewOneShots.add(project);
                }
                livePreviewTicks.remove(project);
                continue;
            }
            int ticks = livePreviewTicks.getOrDefault(project, 0) - 1;
            if (ticks <= 0) {
                spawnPreview(project);
                ticks = Math.max(1, previewState.previewInterval);
            }
            livePreviewTicks.put(project, ticks);
            livePreviewOneShots.remove(project);
        }
        livePreviewTicks.keySet().removeIf(project -> !previewedProjects.contains(project));
        livePreviewOneShots.removeIf(project -> !previewedProjects.contains(project));
    }

    public void playScreenshakePreview() {
        playScreenshake(screen.state());
    }

    public void refreshLivePreview() {
        clearPreviewParticles();
        resetLivePreviewTimers();
        captureSnapshot();
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) {
            return;
        }
        for (EditorProject project : projects) {
            if (shouldLivePreview(project)) {
                spawnPreview(project);
                if (project.state.previewPlayback == PreviewPlaybackMode.LOOPING) {
                    livePreviewTicks.put(project, Math.max(1, project.state.previewInterval));
                } else {
                    livePreviewOneShots.add(project);
                }
            }
        }
    }

    public void handleRealtimePreviewChanges() {
        String snapshot = livePreviewSnapshot();
        if (!snapshot.equals(lastPreviewSnapshot)) {
            lastPreviewSnapshot = snapshot;
            refreshLivePreview();
        }
    }

    public void captureSnapshot() {
        lastPreviewSnapshot = livePreviewSnapshot();
    }

    public void clearPreviewParticles() {
        particles.forEach(LodestoneWorldParticle::remove);
        particles.clear();
        ScreenParticleHandler.clearParticles(screenParticles);
    }

    public void clearWorldParticlesOnly() {
        particles.clear();
    }

    public void renderScreenShapeOverlays(GuiGraphics graphics) {
        if (screen.projectPickerOpen || screen.loadPickerOpen) {
            return;
        }
        ScreenShapeOverlay.render(graphics, projects);
    }

    public void renderWorldShapeOverlays(PoseStack poseStack, Camera camera) {
        if (screen.projectPickerOpen || screen.loadPickerOpen) {
            return;
        }
        int index = 0;
        for (EditorProject project : projects) {
            EditorState overlayState = project.state;
            if (project.visible && overlayState.shapeOverlay && overlayState.outputMode == ParticleOutputMode.WORLD) {
                OverlayRenderer.renderShape(poseStack, camera, getPreviewPosition(overlayState), overlayState, index);
                index++;
            }
        }
    }

    public Vec3 calculateCameraPreviewPosition() {
        return calculateCameraPreviewPosition(screen.state());
    }

    public void initializeFixedPreviewPosition(EditorState targetState) {
        if (!targetState.followCamera && isUnsetFixedPreviewPosition(targetState)) {
            targetState.setFixedPreviewPosition(calculateCameraPreviewPosition(targetState));
        }
    }

    public void onClose(boolean canStartBackgroundPreview) {
        if (canStartBackgroundPreview) {
            PreviewSession.start(projects);
            clearWorldParticlesOnly();
            if (PreviewSession.isActive()) {
                screen.message("Background particle preview started");
            }
        } else {
            clearPreviewParticles();
        }
    }

    private void spawnPreview(EditorProject project) {
        spawnPreview(project, false);
    }

    private void spawnPreview(EditorProject project, boolean includeEffects) {
        if (!project.visible) {
            return;
        }
        spawnPreview(project.state);
        if (includeEffects && project.state.screenshake.onBurst) {
            playScreenshake(project.state);
        }
    }

    private void spawnPreview(EditorState previewState) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) {
            return;
        }
        if (previewState.outputMode == ParticleOutputMode.SCREEN) {
            var builder = previewState.createScreenBuilder(screenParticles);
            if (previewState.spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
                if (previewState.screenTrackStack) {
                    builder.repeatOnStack(previewState.stackTrackXOffset, previewState.stackTrackYOffset, previewState.spawnCount);
                } else {
                    builder.repeat(previewState.screenX, previewState.screenY, previewState.spawnCount);
                }
                return;
            }
            for (int i = 0; i < previewState.spawnCount; i++) {
                Vec3 offset = previewState.createScreenDistributionOffset(client.level.random);
                if (previewState.screenTrackStack) {
                    builder.spawnOnStack(previewState.stackTrackXOffset + offset.x, previewState.stackTrackYOffset + offset.y);
                } else {
                    builder.spawn(previewState.screenX + offset.x, previewState.screenY + offset.y);
                }
            }
            return;
        }
        Vec3 pos = getPreviewPosition(previewState);
        for (int i = 0; i < previewState.spawnCount; i++) {
            previewState.createBuilder().addSpawnActor(this::trackPreviewParticle).spawn(client.level, pos.add(previewState.createWorldDistributionOffset(client.level.random)));
        }
    }

    private boolean shouldLivePreview(EditorProject project) {
        if (!project.visible || !project.state.autoPreview) {
            return false;
        }
        return screen.livePreviewScope == LivePreviewScope.VISIBLE_PROJECTS || project == screen.activeProject();
    }

    private void resetLivePreviewTimers() {
        livePreviewTicks.clear();
        livePreviewOneShots.clear();
    }

    private void playScreenshake(EditorState targetState) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null || !targetState.screenshake.enabled) {
            return;
        }
        ScreenshakeHandler.addScreenshake(targetState.createScreenshake(getPreviewPosition(targetState)));
    }

    private String livePreviewSnapshot() {
        StringBuilder builder = new StringBuilder("scope=").append(screen.livePreviewScope.label);
        if (screen.livePreviewScope == LivePreviewScope.VISIBLE_PROJECTS) {
            for (int i = 0; i < projects.size(); i++) {
                appendProjectPreviewSnapshot(builder, projects.get(i), i);
            }
        } else {
            appendProjectPreviewSnapshot(builder, screen.activeProject(), screen.activeProjectIndex);
        }
        return builder.toString();
    }

    private void appendProjectPreviewSnapshot(StringBuilder builder, EditorProject project, int index) {
        builder.append('|')
                .append(index)
                .append(":visible=").append(project.visible)
                .append(":auto=").append(project.state.autoPreview)
                .append(":").append(project.state.previewSnapshot());
    }

    private void trackPreviewParticle(LodestoneWorldParticle particle) {
        particles.add(particle);
    }

    private void prunePreviewParticles() {
        particles.removeIf(particle -> !particle.isAlive());
    }

    private Vec3 getPreviewPosition(EditorState previewState) {
        if (previewState.followCamera) {
            return calculateCameraPreviewPosition(previewState);
        }
        return previewState.fixedPreviewPosition();
    }

    private Vec3 calculateCameraPreviewPosition(EditorState previewState) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return Vec3.ZERO;
        }
        Vec3 look = client.player.getLookAngle();
        return client.player.getEyePosition().add(look.scale(previewState.previewDistance)).add(0, previewState.previewYOffset, 0);
    }

    private void initializeFixedPreviewPositions() {
        for (EditorProject project : projects) {
            initializeFixedPreviewPosition(project.state);
        }
    }

    private boolean isUnsetFixedPreviewPosition(EditorState targetState) {
        return targetState.fixedPreviewX == 0.0 && targetState.fixedPreviewY == 0.0 && targetState.fixedPreviewZ == 0.0;
    }
}
