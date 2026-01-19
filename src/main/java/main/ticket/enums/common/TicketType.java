package main.ticket.enums.common;

import main.common.EnumUtils;

public enum TicketType {
    BUG,
    FEATURE_REQUEST,
    UI_FEEDBACK;

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static TicketType fromString(final String value) {
        return EnumUtils.fromString(TicketType.class, value);
    }
}
