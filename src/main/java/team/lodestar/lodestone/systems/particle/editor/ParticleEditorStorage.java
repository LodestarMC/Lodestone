package team.lodestar.lodestone.systems.particle.editor;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

class ParticleEditorStorage {
    static final String PROJECT_EXTENSION = ".particle.json";

    static Path root(Minecraft client) {
        return client.gameDirectory.toPath().resolve("lodestone_particles");
    }

    static Path ensureRoot(Minecraft client) throws IOException {
        return ensureFolder(root(client));
    }

    static void openRootFolder(Minecraft client) throws IOException {
        openFolder(ensureRoot(client));
    }

    static Path ensureFolder(Path folder) throws IOException {
        Files.createDirectories(folder);
        return folder;
    }

    static void openFolder(Path folder) throws IOException {
        Util.getPlatform().openFile(ensureFolder(folder).toFile());
    }

    static Path saveProject(Minecraft client, ParticleEditorProject project) throws IOException {
        return saveProject(ensureRoot(client), project);
    }

    static Path saveProject(Path folder, ParticleEditorProject project) throws IOException {
        String safeName = safeFileName(project.name);
        Path root = ensureFolder(folder.resolve(safeName));
        Path projectPath = root.resolve(safeName + PROJECT_EXTENSION);
        Path javaPath = root.resolve(safeName + ".java");
        Files.writeString(projectPath, project.state.toProjectJson(project.name), StandardCharsets.UTF_8);
        Files.writeString(javaPath, project.state.toJavaFile(toClassName(safeName)), StandardCharsets.UTF_8);
        return projectPath;
    }

    static List<Path> listProjects(Minecraft client) throws IOException {
        return listProjects(root(client));
    }

    static List<Path> listProjects(Path folder) throws IOException {
        if (!Files.isDirectory(folder)) {
            return List.of();
        }
        try (Stream<Path> stream = Files.walk(folder, 2)) {
            return stream
                    .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(PROJECT_EXTENSION))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString(), String.CASE_INSENSITIVE_ORDER))
                    .toList();
        }
    }

    static LoadedProject loadProject(Path path) throws IOException {
        String json = Files.readString(path, StandardCharsets.UTF_8);
        String fallbackName = path.getFileName().toString().replace(PROJECT_EXTENSION, "");
        String name = ParticleEditorState.readProjectName(json, fallbackName);
        return new LoadedProject(name, ParticleEditorState.fromProjectJson(json), path);
    }

    static void deleteProject(Path path) throws IOException {
        if (path == null || !Files.isRegularFile(path)) {
            return;
        }
        String fileName = path.getFileName().toString();
        String baseName = fileName.endsWith(PROJECT_EXTENSION)
                ? fileName.substring(0, fileName.length() - PROJECT_EXTENSION.length())
                : fileName;
        Files.deleteIfExists(path);
        Files.deleteIfExists(path.resolveSibling(baseName + ".java"));

        Path folder = path.getParent();
        if (folder != null && Files.isDirectory(folder)) {
            try (Stream<Path> stream = Files.list(folder)) {
                if (stream.findAny().isEmpty()) {
                    Files.deleteIfExists(folder);
                }
            }
        }
    }

    static String safeFileName(String name) {
        String safe = name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_\\-]+", "_");
        safe = safe.replaceAll("_+", "_").replaceAll("^_+|_+$", "");
        return safe.isBlank() ? "particle" : safe;
    }

    private static String toClassName(String safeName) {
        StringBuilder builder = new StringBuilder("Generated");
        boolean upperNext = true;
        for (int i = 0; i < safeName.length(); i++) {
            char c = safeName.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                builder.append(upperNext ? Character.toUpperCase(c) : c);
                upperNext = false;
            } else {
                upperNext = true;
            }
        }
        builder.append("Particle");
        return builder.toString();
    }

    record LoadedProject(String name, ParticleEditorState state, Path path) {
    }
}
