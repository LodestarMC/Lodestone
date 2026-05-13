package team.lodestar.lodestone.systems.particle.editor.project;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ProjectNames {
    private ProjectNames() {
    }

    public static boolean isAllowedNameChar(char c) {
        return !Character.isISOControl(c) && "\\/:*?\"<>|".indexOf(c) < 0;
    }

    public static String sanitizeInput(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (isAllowedNameChar(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String sanitize(String value) {
        String clean = sanitizeInput(value).trim();
        return clean.isBlank() ? "particle_1" : clean;
    }

    public static String unique(String baseName, List<EditorProject> projects) {
        return unique(baseName, projects, -1);
    }

    public static String unique(String baseName, List<EditorProject> projects, int ignoredIndex) {
        String clean = baseName == null || baseName.isBlank() ? "particle" : baseName;
        String candidate = clean;
        int suffix = 2;
        while (hasNamed(candidate, projects, ignoredIndex)) {
            candidate = clean + "_" + suffix++;
        }
        return candidate;
    }

    public static Set<Integer> shiftSelectionAfterRemoval(Set<Integer> selection, int removedIndex) {
        Set<Integer> shifted = new HashSet<>();
        for (Integer selected : selection) {
            if (selected < removedIndex) {
                shifted.add(selected);
            } else if (selected > removedIndex) {
                shifted.add(selected - 1);
            }
        }
        return shifted;
    }

    private static boolean hasNamed(String name, List<EditorProject> projects, int ignoredIndex) {
        int index = 0;
        for (EditorProject project : projects) {
            if (index != ignoredIndex && project.name.equalsIgnoreCase(name)) {
                return true;
            }
            index++;
        }
        return false;
    }
}
