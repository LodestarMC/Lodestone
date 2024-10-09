package team.lodestar.lodestone.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ReflectionHelper {

    /**
     * Copies fields from one object to another.
     *
     * @param fieldNames The names of the fields to absorb. If empty, all fields will be absorbed.
     */
    public static <T> void copyFields(T from, T to, String... fieldNames) {
        ArrayList<Field> fields = new ArrayList<>();
        if (fieldNames.length == 0) {
            fields.addAll(List.of(from.getClass().getDeclaredFields()));
        } else {
            Set<String> fieldNameSet = Set.of(fieldNames);
            fields.addAll(Arrays.stream(from.getClass().getDeclaredFields())
                    .filter(f -> fieldNameSet.contains(f.getName())).toList()
            );
        }
        for (Field field : fields) {
            try {
                if (Modifier.isStatic(field.getModifiers())) continue;
                field.setAccessible(true);
                field.set(to, field.get(from));
            } catch (IllegalAccessException ignored) {}
            // Won't happen due to setAccessible
        }
    }

    /**
     * Gets a field from an object, regardless of access restrictions.
     * Returns empty optional if invalid.
     *
     * @param name The name of the field.
     * @param object The object to get the field from.
     */
    public static <T> Optional<T> getField(String name, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return Optional.of((T) field.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Optional.empty();
        }
    }

}
