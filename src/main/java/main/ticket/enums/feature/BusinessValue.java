package main.ticket.enums.feature;

import lombok.Getter;
import main.common.EnumUtils;

@Getter
public enum BusinessValue {
    S(1),
    M(3),
    L(6),
    XL(10);

    private int businessValue;
    BusinessValue(final int businessValue) {
        this.businessValue = businessValue;
    }

    /**
     * Converts the enum Value into String
     * @param value enum value associated
     * @return String conversion for value.
     */
    public static BusinessValue fromString(final String value) {
        return EnumUtils.fromString(BusinessValue.class, value);
    }
}
