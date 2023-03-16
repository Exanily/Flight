package org.example.dto;

public enum TimeZoneCity {
    VVO("+10"),
    TLV("+03");
    private final String value;

    TimeZoneCity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
