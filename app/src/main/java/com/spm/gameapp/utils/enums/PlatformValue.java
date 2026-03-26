package com.spm.gameapp.utils.enums;

public enum PlatformValue {
    DEFAULT("", "All platforms"),
    WINDOWS("7", "Windows"),
    LINUX("4", "Linux"),
    IOS("14", "iOS"),
    ANDROID("21", "Android"),
    PS5("187", "PS5"),
    XBOX_SERIES_X("18", "XBOX Series X"),
    SWITCH("7", "Nintendo Switch");

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
