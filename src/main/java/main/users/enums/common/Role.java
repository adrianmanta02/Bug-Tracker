package main.users.enums.common;

import main.common.enums.EnumUtils;

public enum Role {
    REPORTER,
    DEVELOPER,
    MANAGER;

    public static Role fromString(final String value) {
        return EnumUtils.fromString(Role.class, value);
    }
}
