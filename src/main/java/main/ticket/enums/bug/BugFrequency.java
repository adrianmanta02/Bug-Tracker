package main.ticket.enums.bug;

import main.common.EnumUtils;

public enum BugFrequency {
    RARE(1),
    OCCASIONAL(2),
    FREQUENT(3),
    ALWAYS(4);

    private int frequency;
    BugFrequency(final int frequency) {
        this.frequency = frequency;
    }

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static BugFrequency fromString(final String value) {
        return EnumUtils.fromString(BugFrequency.class, value);
    }

    public int getFrequency() {
        return this.frequency;
    }
}
