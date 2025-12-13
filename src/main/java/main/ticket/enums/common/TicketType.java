package main.ticket.enums.common;

import main.common.enums.EnumUtils;

public enum TicketType {
    BUG,
    FEATURE_REQUEST,
    UI_FEEDBACK;

    public static TicketType fromString(final String value) {
        return EnumUtils.fromString(TicketType.class, value);
    }
}
