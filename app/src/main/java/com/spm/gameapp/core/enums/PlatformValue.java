package com.spm.gameapp.core.enums;

public enum PlatformValue {
    DEFAULT("", "All platforms"),
    WINDOWS("7", "Windows"),
    LINUX("4", "Linux"),
    MAC("10", "Mac"),
    IOS("14", "iOS"),
    ANDROID("21", "Android"),
    PS5("187", "PS5"),
    PS4("16", "PS4"),
    XBOX_ONE("1", "XBOX ONE"),
    XBOX_SERIES_X("18", "XBOX Series X"),
    SWITCH("7", "Nintendo Switch"),
    NINTENDO_3DS("3", "Nintendo 3DS"),
    NINTENDO_DS("4", "Nintendo DS"),
    NINTENDO_WII("5", "Nintendo Wii");

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
