package main.ticket.enums.common;

import main.common.EnumUtils;

public enum ExpertiseArea {
    FRONTEND,
    BACKEND,
    DEVOPS,
    DESIGN,
    DB;

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static ExpertiseArea fromString(final String value) {
        return EnumUtils.fromString(ExpertiseArea.class, value);
    }
}
