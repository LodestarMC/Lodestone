package team.lodestar.lodestone.systems.particle.editor.ui;

import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.particle.editor.ParticleEditorScreen;
import team.lodestar.lodestone.systems.particle.editor.EditorState;
import team.lodestar.lodestone.systems.particle.editor.data.*;
import team.lodestar.lodestone.systems.particle.editor.api.EditorContext;
import team.lodestar.lodestone.systems.particle.editor.api.EditorDataGroup;
import team.lodestar.lodestone.systems.particle.editor.api.EditorTab;
import team.lodestar.lodestone.systems.particle.editor.api.EditorRegistry;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public final class TabEntries {
    private final ParticleEditorScreen screen;
    private final EditorContext context;
    private final EntryFactory entries;

    public TabEntries(ParticleEditorScreen screen, EditorContext context, EntryFactory entries) {
        this.screen = screen;
        this.context = context;
        this.entries = entries;
    }

    public List<Entry> buildEntries(EditorTab targetTab) {
        List<Entry> output = new ArrayList<>();
        EditorTab previousBuildingTab = screen.buildingTab;
        screen.buildingTab = targetTab;
        try {
            if (targetTab.equals(EditorTab.EMITTER)) {
                if (!screen.projectBrowserVisible) {
                    addProjectEntries(output);
                }
                addEmitterEntries(output);
            } else if (targetTab.equals(EditorTab.MOTION)) {
                addMotionEntries(output);
            } else {
                addRegisteredDataGroups(output, targetTab);
            }
        } finally {
            screen.buildingTab = previousBuildingTab;
        }
        return output;
    }

    public void refreshCategoryDefinitions() {
        screen.categoryDefinitions.clear();
        for (EditorTab targetTab : EditorRegistry.tabs()) {
            buildEntries(targetTab);
        }
    }

    public List<Entry> buildGroupEntries(EditorDataGroup<?> group) {
        List<Entry> output = new ArrayList<>();
        group.buildEntries(context, output);
        return output;
    }

    private EditorState state() {
        return screen.state();
    }

    private void addProjectEntries(List<Entry> output) {
        section(output, "projects", "section.projects", "Projects", section -> {
            section.add(entries.displayEntry(text("entry.active_project", "Active Project"), (screen.activeProjectIndex + 1) + "/" + screen.projects.size() + " " + screen.activeProject().name));
            section.add(entries.actionEntry(text("action.previous_project", "Previous Project"), () -> screen.selectProject(screen.activeProjectIndex - 1)));
            section.add(entries.actionEntry(text("action.next_project", "Next Project"), () -> screen.selectProject(screen.activeProjectIndex + 1)));
            section.add(entries.actionEntry(text("action.add_project", "Add Project"), screen::addProject));
            section.add(entries.actionEntry(text("action.save_projects", "Save Projects"), screen::saveAllProjects));
        });
    }

    private void addEmitterEntries(List<Entry> output) {
        EditorState state = state();
        section(output, "source", "section.source", "Source", section -> {
            section.add(entries.enumEntry(text("entry.output_mode", "Output Mode"), () -> state.outputMode, value -> state.outputMode = value, ParticleOutputMode.values(), value -> value.label));
            if (state.outputMode == ParticleOutputMode.SCREEN) {
                section.add(entries.enumEntry(text("entry.screen_particle", "Screen Particle"), () -> state.screenParticle, value -> state.screenParticle = value, ScreenParticlePreset.values(), value -> value.label));
            } else {
                section.add(entries.enumEntry(text("entry.particle", "Particle"), () -> state.particle, value -> state.particle = value, ParticlePreset.values(), value -> value.label));
            }
        });

        section(output, "render", "section.render", "Render", section -> {
            if (state.outputMode == ParticleOutputMode.SCREEN) {
                section.add(entries.enumEntry(text("entry.screen_render_type", "Screen Render Type"), () -> state.screenRenderMode, value -> state.screenRenderMode = value, ScreenRenderMode.values(), value -> value.label));
                section.add(entries.doubleEntry(text("entry.screen_x", "Screen X"), () -> state.screenX, value -> state.screenX = value, -4096.0, 4096.0, 1.0));
                section.add(entries.doubleEntry(text("entry.screen_y", "Screen Y"), () -> state.screenY, value -> state.screenY = value, -4096.0, 4096.0, 1.0));
                section.add(entries.boolEntry(text("entry.track_stack", "Track Stack"), () -> state.screenTrackStack, value -> state.screenTrackStack = value));
                if (state.screenTrackStack) {
                    section.add(entries.doubleEntry(text("entry.stack_x_offset", "Stack X Offset"), () -> state.stackTrackXOffset, value -> state.stackTrackXOffset = value, -512.0, 512.0, 1.0));
                    section.add(entries.doubleEntry(text("entry.stack_y_offset", "Stack Y Offset"), () -> state.stackTrackYOffset, value -> state.stackTrackYOffset = value, -512.0, 512.0, 1.0));
                }
            } else {
                section.add(entries.enumEntry(text("entry.render_type", "Render Type"), () -> state.renderMode, value -> state.renderMode = value, RenderMode.values(), value -> value.label));
                section.add(entries.boolEntry(text("entry.depth_fade", "Depth Fade"), () -> state.depthFade, value -> state.depthFade = value));
                section.add(entries.enumEntry(text("entry.render_layer", "Render Layer"), () -> state.renderLayer, value -> state.renderLayer = value, RenderLayerMode.values(), value -> value.label));
                section.add(entries.enumEntry(text("entry.behavior", "Behavior"), () -> state.behavior, value -> state.behavior = value, BehaviorMode.values(), value -> value.label));
            }
            section.add(entries.enumEntry(text("entry.sprite_picker", "Sprite Picker"), () -> state.spritePicker, value -> state.spritePicker = value, SimpleParticleOptions.ParticleSpritePicker.values(), Enum::name));
            if (state.outputMode == ParticleOutputMode.WORLD) {
                section.add(entries.enumEntry(text("entry.lighting", "Lighting"), () -> state.lightMode, value -> state.lightMode = value, LightMode.values(), value -> value.label));
            }
            if (state.outputMode == ParticleOutputMode.WORLD && state.lightMode == LightMode.CUSTOM) {
                section.add(entries.intEntry(text("entry.light_value", "Light Value"), () -> state.lightLevel, value -> state.lightLevel = value, -1, RenderHelper.FULL_BRIGHT, 16));
            }
        });

        section(output, "lifetime", "section.lifetime", "Lifetime", section -> {
            if (state.outputMode == ParticleOutputMode.WORLD) {
                section.add(entries.boolEntry(text("entry.force_spawn", "Force Spawn"), () -> state.forceSpawn, value -> state.forceSpawn = value));
                section.add(entries.boolEntry(text("entry.no_clip", "No Clip"), () -> state.noClip, value -> state.noClip = value));
            }
            section.add(entries.intEntry(text("entry.lifetime", "Lifetime"), () -> state.lifetime, value -> state.lifetime = value, 1, 6000, 1));
            section.add(entries.intEntry(text("entry.life_delay", "Life Delay"), () -> state.lifeDelay, value -> state.lifeDelay = value, 0, 6000, 1));
            section.add(entries.floatEntry(text("entry.lifetime_modifier", "Lifetime Modifier"), () -> state.lifetimeModifier, value -> state.lifetimeModifier = value, 0.0f, 8.0f, 0.05f));
            section.add(entries.floatEntry(text("entry.life_delay_modifier", "Life Delay Modifier"), () -> state.lifeDelayModifier, value -> state.lifeDelayModifier = value, 0.0f, 8.0f, 0.05f));
        });

        section(output, "preview", "section.preview", "Preview", section -> section.addAll(buildPreviewEntries()));
    }

    private List<Entry> buildPreviewEntries() {
        EditorState state = state();
        List<Entry> output = new ArrayList<>();
        output.add(entries.boolEntry(text("entry.live_preview", "Live Preview"), () -> state.autoPreview, value -> state.autoPreview = value));
        output.add(entries.enumEntry(text("entry.live_preview_scope", "Live Scope"), () -> screen.livePreviewScope, value -> screen.livePreviewScope = value, LivePreviewScope.values(), this::livePreviewScopeLabel));
        output.add(entries.enumEntry(text("entry.preview_playback", "Playback"), () -> state.previewPlayback, value -> state.previewPlayback = value, PreviewPlaybackMode.values(), this::previewPlaybackLabel));
        output.add(entries.intEntry(text("entry.spawn_count", "Spawn Count"), () -> state.spawnCount, value -> state.spawnCount = value, 1, 512, 1));
        if (state.previewPlayback == PreviewPlaybackMode.LOOPING) {
            output.add(entries.intEntry(text("entry.emit_interval", "Emit Interval"), () -> state.previewInterval, value -> state.previewInterval = value, 1, 200, 1));
        }
        output.add(entries.enumEntry(text("entry.preview_mode", "Preview Mode"), () -> state.followCamera ? PreviewPositionMode.CAMERA : PreviewPositionMode.FIXED, value -> {
            state.followCamera = value == PreviewPositionMode.CAMERA;
            if (!state.followCamera) {
                state.setFixedPreviewPosition(screen.calculateCameraPreviewPosition());
            }
        }, PreviewPositionMode.values(), this::previewPositionLabel));
        if (!state.followCamera) {
            output.add(entries.actionEntry(text("entry.move_fixed_point", "Move Fixed Point"), () -> {
                state.setFixedPreviewPosition(screen.calculateCameraPreviewPosition());
                screen.refreshLivePreview();
            }));
            output.add(entries.doubleEntry(text("entry.fixed_x", "Fixed X"), () -> state.fixedPreviewX, value -> state.fixedPreviewX = value, -30000000.0, 30000000.0, 0.05));
            output.add(entries.doubleEntry(text("entry.fixed_y", "Fixed Y"), () -> state.fixedPreviewY, value -> state.fixedPreviewY = value, -2048.0, 2048.0, 0.05));
            output.add(entries.doubleEntry(text("entry.fixed_z", "Fixed Z"), () -> state.fixedPreviewZ, value -> state.fixedPreviewZ = value, -30000000.0, 30000000.0, 0.05));
        }
        output.add(entries.doubleEntry(text("entry.preview_distance", "Preview Distance"), () -> state.previewDistance, value -> state.previewDistance = value, 0.25, 32.0, 0.25));
        output.add(entries.doubleEntry(text("entry.preview_y_offset", "Preview Y Offset"), () -> state.previewYOffset, value -> state.previewYOffset = value, -16.0, 16.0, 0.1));
        output.add(entries.actionEntry(text("action.spawn_burst", "Spawn Burst"), screen::spawnPreviewWithEffects));
        output.add(entries.actionEntry(text("action.spawn_all_projects", "Spawn All Projects"), screen::spawnAllPreviews));
        return output;
    }

    private void addMotionEntries(List<Entry> output) {
        EditorState state = state();
        section(output, "motion.velocity", "section.velocity", "Velocity", section -> {
            section.add(entries.doubleEntry(text("entry.motion_x", "Motion X"), () -> state.motionX, value -> state.motionX = value, -8.0, 8.0, 0.01));
            section.add(entries.doubleEntry(text("entry.motion_y", "Motion Y"), () -> state.motionY, value -> state.motionY = value, -8.0, 8.0, 0.01));
            if (state.outputMode == ParticleOutputMode.WORLD) {
                section.add(entries.doubleEntry(text("entry.motion_z", "Motion Z"), () -> state.motionZ, value -> state.motionZ = value, -8.0, 8.0, 0.01));
            }
        });

        section(output, "motion.randomness", "section.randomness", "Randomness", section -> {
            section.add(entries.doubleEntry(text("entry.random_motion_x", "Random Motion X"), () -> state.randomMotionX, value -> state.randomMotionX = value, 0.0, 8.0, 0.01));
            section.add(entries.doubleEntry(text("entry.random_motion_y", "Random Motion Y"), () -> state.randomMotionY, value -> state.randomMotionY = value, 0.0, 8.0, 0.01));
            if (state.outputMode == ParticleOutputMode.WORLD) {
                section.add(entries.doubleEntry(text("entry.random_motion_z", "Random Motion Z"), () -> state.randomMotionZ, value -> state.randomMotionZ = value, 0.0, 8.0, 0.01));
            }
        });

        section(output, "motion.spawn_area", "section.spawn_area", "Spawn Area", section -> {
            section.add(entries.enumEntry(text("entry.spawn_shape", "Spawn Shape"), () -> state.spawnDistribution, value -> state.spawnDistribution = value, SpawnDistributionMode.values(), this::spawnDistributionLabel));
            section.add(entries.boolEntry(text("entry.shape_overlay", "Shape Overlay"), () -> state.shapeOverlay, value -> state.shapeOverlay = value));
            if (state.spawnDistribution == SpawnDistributionMode.BUILDER_RANDOM) {
                section.add(entries.doubleEntry(text("entry.builder_offset_x", "Builder Offset X"), () -> state.randomOffsetX, value -> state.randomOffsetX = value, 0.0, 16.0, 0.01));
                section.add(entries.doubleEntry(text("entry.builder_offset_y", "Builder Offset Y"), () -> state.randomOffsetY, value -> state.randomOffsetY = value, 0.0, 16.0, 0.01));
                if (state.outputMode == ParticleOutputMode.WORLD) {
                    section.add(entries.doubleEntry(text("entry.builder_offset_z", "Builder Offset Z"), () -> state.randomOffsetZ, value -> state.randomOffsetZ = value, 0.0, 16.0, 0.01));
                }
            } else if (state.spawnDistribution != SpawnDistributionMode.POINT) {
                section.add(new ShapeSizeEntry(text("entry.shape_size", "Shape Size"), state, EditorState::formatNumber));
                section.add(entries.doubleEntry(text("entry.shape_uniform", "Shape Uniform"), this::shapeUniformSize, this::setShapeUniformSize, 0.0, 64.0, 0.05));
                section.add(entries.doubleEntry(text("entry.shape_x", "Shape X"), () -> state.shapeSizeX, value -> state.shapeSizeX = value, 0.0, 64.0, 0.05));
                if (shapeUsesY()) {
                    section.add(entries.doubleEntry(text("entry.shape_y", "Shape Y"), () -> state.shapeSizeY, value -> state.shapeSizeY = value, 0.0, 64.0, 0.05));
                }
                if (shapeUsesZ()) {
                    section.add(entries.doubleEntry(text("entry.shape_z", "Shape Z"), () -> state.shapeSizeZ, value -> state.shapeSizeZ = value, 0.0, 64.0, 0.05));
                }
            }
        });

        section(output, "motion.physics", "section.physics", "Physics", section -> {
            section.add(entries.floatEntry(text("entry.gravity", "Gravity"), () -> state.gravity, value -> state.gravity = value, -4.0f, 4.0f, 0.01f));
            section.add(entries.floatEntry(text("entry.friction", "Friction"), () -> state.friction, value -> state.friction = value, 0.0f, 2.0f, 0.01f));
            section.add(entries.floatEntry(text("entry.gravity_modifier", "Gravity Modifier"), () -> state.gravityModifier, value -> state.gravityModifier = value, 0.0f, 8.0f, 0.05f));
            section.add(entries.floatEntry(text("entry.friction_modifier", "Friction Modifier"), () -> state.frictionModifier, value -> state.frictionModifier = value, 0.0f, 8.0f, 0.05f));
        });

        if (state.outputMode == ParticleOutputMode.SCREEN) {
            return;
        }
        section(output, "motion.behavior_data", "section.behavior_data", "Behavior Data", section -> {
            section.add(entries.boolEntry(text("entry.forced_direction", "Forced Direction"), () -> state.forcedDirection, value -> state.forcedDirection = value));
            if (state.forcedDirection) {
                section.add(entries.doubleEntry(text("entry.direction_x", "Direction X"), () -> state.directionX, value -> state.directionX = value, -1.0, 1.0, 0.05));
                section.add(entries.doubleEntry(text("entry.direction_y", "Direction Y"), () -> state.directionY, value -> state.directionY = value, -1.0, 1.0, 0.05));
                section.add(entries.doubleEntry(text("entry.direction_z", "Direction Z"), () -> state.directionZ, value -> state.directionZ = value, -1.0, 1.0, 0.05));
            }
            if (state.behavior == BehaviorMode.SPARK) {
                section.add(entries.floatEntry(text("entry.spark_length_center", "Spark Length Center"), () -> state.sparkLengthCenter, value -> state.sparkLengthCenter = value, -2.0f, 2.0f, 0.05f));
            }
        });
    }

    private void addRegisteredDataGroups(List<Entry> output, EditorTab targetTab) {
        for (EditorDataGroup<?> group : EditorRegistry.groups(targetTab)) {
            optionalGroupSection(output, group);
        }
    }

    private void optionalGroupSection(List<Entry> output, EditorDataGroup<?> group) {
        if (screen.findFloatingPanel(group) != null) {
            return;
        }
        optionalSection(output, group, () -> group.isEnabled(state()), value -> setGroupEnabled(group, value));
    }

    private void setGroupEnabled(EditorDataGroup<?> group, boolean enabled) {
        group.setEnabled(state(), enabled);
        if (!enabled) {
            FloatingTabPanel panel = screen.findFloatingPanel(group);
            if (panel != null) {
                screen.floatingPanels.remove(panel);
            }
        }
    }

    private void section(List<Entry> output, String key, String translationKey, String fallback, Consumer<List<Entry>> builder) {
        String label = text(translationKey, fallback);
        screen.categoryDefinitions.put(key, new CategoryDefinition(key, label, builder, screen.buildingTab));
        if (screen.findFloatingPanel(key) != null) {
            return;
        }
        output.add(new CategoryEntry(key, label, builder, ParticleEditorScreen.COLLAPSED_CATEGORIES));
        if (!ParticleEditorScreen.COLLAPSED_CATEGORIES.contains(key)) {
            builder.accept(output);
        }
    }

    private void optionalSection(List<Entry> output, EditorDataGroup<?> group, BooleanSupplier enabled, Consumer<Boolean> setter) {
        output.add(new OptionalCategoryEntry(group.key(), groupLabel(group), group, enabled, setter, ParticleEditorScreen.COLLAPSED_CATEGORIES, screen::onStateEdited));
        if (enabled.getAsBoolean() && !ParticleEditorScreen.COLLAPSED_CATEGORIES.contains(group.key())) {
            group.buildEntries(context, output);
        }
    }

    private String groupLabel(EditorDataGroup<?> group) {
        return text(group.translationKey(), group.fallback());
    }

    private String previewPlaybackLabel(PreviewPlaybackMode value) {
        return text("preview_playback." + value.label, value.label);
    }

    private String livePreviewScopeLabel(LivePreviewScope value) {
        return text("preview_scope." + value.label, value.label);
    }

    private String spawnDistributionLabel(SpawnDistributionMode value) {
        return text("spawn_shape." + value.label, value.label);
    }

    private String previewPositionLabel(PreviewPositionMode value) {
        return text("preview_mode." + value.label, value.label);
    }

    private boolean shapeUsesY() {
        EditorState state = state();
        return state.outputMode == ParticleOutputMode.SCREEN || state.spawnDistribution != SpawnDistributionMode.CIRCLE;
    }

    private boolean shapeUsesZ() {
        return state().outputMode == ParticleOutputMode.WORLD;
    }

    private double shapeUniformSize() {
        EditorState state = state();
        if (state.outputMode == ParticleOutputMode.WORLD && state.spawnDistribution == SpawnDistributionMode.CIRCLE) {
            return (state.shapeSizeX + state.shapeSizeZ) * 0.5;
        }
        if (state.outputMode == ParticleOutputMode.SCREEN) {
            return (state.shapeSizeX + state.shapeSizeY) * 0.5;
        }
        return (state.shapeSizeX + state.shapeSizeY + state.shapeSizeZ) / 3.0;
    }

    private void setShapeUniformSize(double value) {
        EditorState state = state();
        state.shapeSizeX = value;
        if (shapeUsesY()) {
            state.shapeSizeY = value;
        }
        if (shapeUsesZ()) {
            state.shapeSizeZ = value;
        }
    }

    private String text(String key, String fallback) {
        return context.text(key, fallback);
    }
}
