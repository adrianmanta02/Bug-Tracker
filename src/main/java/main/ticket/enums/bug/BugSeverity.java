package main.ticket.enums.bug;

import main.common.enums.EnumUtils;
public enum BugSeverity {
    MINOR(1),
    MODERATE(2),
    SEVERE(3);

    private int severityFactor;
    BugSeverity(final int severityFactor) {
        this.severityFactor = severityFactor;
    }

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static BugSeverity fromString(final String value) {
        return EnumUtils.fromString(BugSeverity.class, value);
    }

    public int getSeverityFactor() {
        return this.severityFactor;
    }
}
