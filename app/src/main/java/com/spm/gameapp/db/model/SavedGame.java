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

    public SavedGame(int gameId, String title, String imgUrl) {
        this.gameId = gameId;
        this.title = title;
        this.imgUrl = imgUrl;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
