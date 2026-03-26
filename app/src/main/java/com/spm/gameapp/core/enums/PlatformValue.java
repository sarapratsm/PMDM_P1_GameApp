package com.spm.gameapp.core.enums;

public enum PlatformValue {
    DEFAULT("", "All platforms"),
    PC("4", "PC"),
    PS5("187", "PS5"),
    XBOX_ONE("1", "XBOX ONE"),
    NES("18", "NES"),
    PS4("16", "PS4");

    private final String value;
    private final String label;

    PlatformValue(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }


}
