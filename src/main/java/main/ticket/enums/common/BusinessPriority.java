package main.ticket.enums.common;

import main.common.enums.EnumUtils;

public enum BusinessPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public static BusinessPriority fromString(final String value) {
        return EnumUtils.fromString(BusinessPriority.class, value);
    }
}
