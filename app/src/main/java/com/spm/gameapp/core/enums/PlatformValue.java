package com.spm.gameapp.core.enums;

/*Usats per filtrar per plataformes.*/
public enum PlatformValue {
    DEFAULT("", "All platforms"),
    WINDOWS("7", "Windows"),
    PS4("16", "PS4"),
    XBOX_ONE("1", "XBOX ONE"),
    SWITCH("7", "Nintendo Switch"),
    IOS("14", "iOS"),
    LINUX("4", "Linux"),
    ANDROID("21", "Android");


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
