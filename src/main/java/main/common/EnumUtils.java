package main.common;

import java.util.Arrays;

public final class EnumUtils {
    private EnumUtils() { }
    /**
     * Given a value for a field, it converts it to an enum value
     * associated with the string.
     * @param enumClass the name of the enum class
     * @param value the string value of the field that has to be converted
     * @return an enum type value
     * @param <E> the type of the field converted from string to enum value
     */
    public static <E extends Enum<E>> E fromString(final Class<E> enumClass,
                                                   final String value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid value "
                                + value
                                + " for enum "
                                + enumClass.getSimpleName()));
    }
}
