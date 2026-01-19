package main.users.enums.developer;

import main.common.EnumUtils;

public enum Seniority {
    JUNIOR,
    MID,
    SENIOR;

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static Seniority fromString(final String value) {
        return EnumUtils.fromString(Seniority.class, value);
    }
}
