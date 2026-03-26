package com.spm.gameapp.core.enums;

public enum OrderValue {
    DEFAULT("", "Per defecte"),
    RATING("-rating", "Puntuació"),
    NAME("name", "Nom"),
    RELEASED("-released", "Data de sortida"),
    ADDED("-added", "Afegits recentment");

    private final String value;
    private final String label;

    OrderValue(String value, String label) {
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
