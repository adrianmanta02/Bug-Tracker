package main.ticket.enums.bug;

import main.common.enums.EnumUtils;

public enum BugFrequency {
    RARE,
    OCCASIONAL,
    FREQUENT,
    ALWAYS;

    public static BugFrequency fromString(final String value) {
        return EnumUtils.fromString(BugFrequency.class, value);
    }
}
