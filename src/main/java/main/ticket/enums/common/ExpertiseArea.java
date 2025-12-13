package main.ticket.enums.common;

import main.common.enums.EnumUtils;

public enum ExpertiseArea {
    FRONTEND,
    BACKEND,
    DEVOPS,
    DESIGN,
    DB;

    public static ExpertiseArea fromString(final String value) {
        return EnumUtils.fromString(ExpertiseArea.class, value);
    }
}
