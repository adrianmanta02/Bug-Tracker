package main.ticket.enums.common;

import main.common.enums.EnumUtils;

public enum TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED;

    public static TicketStatus fromString(final String value) {
        return EnumUtils.fromString(TicketStatus.class, value);
    }
}
