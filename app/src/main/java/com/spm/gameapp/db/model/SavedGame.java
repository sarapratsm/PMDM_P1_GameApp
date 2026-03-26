package com.spm.gameapp.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_games")
public class SavedGame {

    //Guardar les dades minimes per a poder mostrar-les a l'usuari si està offline.
    @PrimaryKey
    private int gameId;
    private String title;
    private String imgUrl;


}
