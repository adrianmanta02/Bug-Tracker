package main.ticket.enums.feature;

import main.common.enums.EnumUtils;

public enum CustomerDemand {
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH;

    public static CustomerDemand fromString(final String value) {
        return EnumUtils.fromString(CustomerDemand.class, value);
    }
}
