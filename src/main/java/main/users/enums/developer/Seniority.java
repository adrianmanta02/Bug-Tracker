package main.users.enums.developer;

import main.common.enums.EnumUtils;

public enum Seniority {
    JUNIOR,
    MID,
    SENIOR;

    public static Seniority fromString(final String value) {
        return EnumUtils.fromString(Seniority.class, value);
    }
}
