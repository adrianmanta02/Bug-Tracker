package main.ticket.enums.feature;

import lombok.Getter;
import main.common.enums.EnumUtils;

@Getter
public enum CustomerDemand {
    LOW(1),
    MEDIUM(3),
    HIGH(6),
    VERY_HIGH(10);

    private int customerDemand;
    CustomerDemand(final int customerDemand) {
        this.customerDemand = customerDemand;
    }

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static CustomerDemand fromString(final String value) {
        return EnumUtils.fromString(CustomerDemand.class, value);
    }
}
