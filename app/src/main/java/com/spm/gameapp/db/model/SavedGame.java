package com.spm.gameapp.db.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "saved_games")
public class SavedGame {

    //Guardar les dades mínimes per a poder mostrar-les a l'usuari si està offline.
    @PrimaryKey
    private int gameId;
    private String title;
    private String imgUrl;

    @Ignore
    private List<GameEntry> comments;

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

    public List<GameEntry> getComments() {
        return comments;
    }

    public void setComments(List<GameEntry> comments) {
        this.comments = comments;
    }
}
