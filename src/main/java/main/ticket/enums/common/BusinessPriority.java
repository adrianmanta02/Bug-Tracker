package main.ticket.enums.common;

import lombok.Getter;
import main.common.EnumUtils;

@Getter
public enum BusinessPriority {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);

    private int priority;
    BusinessPriority(final int priority) {
        this.priority = priority;
    }

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static BusinessPriority fromString(final String value) {
        return EnumUtils.fromString(BusinessPriority.class, value);
    }

    /**
     * Switches the current BusinessPriority for a Ticket to the next one in order.
     * Stops once reached CRITICAL priority.
     * @return the new {@code BusinessPriority} enum value.
     */
    public BusinessPriority next() {
        return switch (this) {
            case LOW -> MEDIUM;
            case MEDIUM -> HIGH;
            case HIGH, CRITICAL -> CRITICAL;
        };
    }
}
