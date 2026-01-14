package main.users.enums.common;

import main.common.enums.EnumUtils;

public enum Role {
    REPORTER,
    DEVELOPER,
    MANAGER;

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static Role fromString(final String value) {
        return EnumUtils.fromString(Role.class, value);
    }
}
