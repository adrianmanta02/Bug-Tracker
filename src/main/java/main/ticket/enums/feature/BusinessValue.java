package main.ticket.enums.feature;

import main.common.enums.EnumUtils;

public enum BusinessValue {
    S,
    M,
    L,
    XL;

    public static BusinessValue fromString(final String value) {
        return EnumUtils.fromString(BusinessValue.class, value);
    }
}
