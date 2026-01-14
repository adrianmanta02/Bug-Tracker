package main.ticket.enums.common;

import main.common.enums.EnumUtils;

public enum TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED;

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static TicketStatus fromString(final String value) {
        return EnumUtils.fromString(TicketStatus.class, value);
    }

    /**
     * Switches the current {@code TicketStatus} for a
     * {@link main.ticket.Ticket} to the next one in order.
     * Stops once reached {@code CLOSED} status.
     * @return the new {@code TicketStatus} enum value.
     */
    public TicketStatus next() {
        return switch (this) {
            case OPEN -> IN_PROGRESS;
            case IN_PROGRESS -> RESOLVED;
            case RESOLVED, CLOSED -> CLOSED;
        };
    }
}
