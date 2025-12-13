package main.ticket.enums.bug;

import main.common.enums.EnumUtils;

public enum BugSeverity {
    MINOR,
    MODERATE,
    SEVERE;

    public static BugSeverity fromString(final String value) {
        return EnumUtils.fromString(BugSeverity.class, value);
    }
}
