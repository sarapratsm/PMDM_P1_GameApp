package com.spm.gameapp.core.enums;

/*Usats per ordenar els jocs
* Available fields: name, released, added, created, updated, rating, metacritic.
* You can reverse the sort order adding a hyphen, for example: -released.*/
public enum OrderValue {
    DEFAULT("", "Per defecte"),
    RATING("-rating", "Puntuació"),
    NAME("name", "Nom"),
    RELEASED("-released", "Data de sortida"),
    CREATED("-created", "Data de creació"),
    UPDATED("-updated", "Data de modificació"),
    METACRITIC("-metacritic", "Metacritic"),
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
