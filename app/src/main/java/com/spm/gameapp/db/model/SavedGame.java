package com.spm.gameapp.db.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

/**
 * Entitat que representa un joc guardat a la base de dades local (Room).
 * Emmagatzema la informació mínima necessària per a la visualització offline.
 * Confiem en Glide per a la gestió de la memòria cau de les imatges mitjançant la URL.
 */
@Entity(tableName = "saved_games")
public class SavedGame {

    @PrimaryKey
    private int gameId;
    
    private String title;
    
    private String imgUrl;

    @Ignore
    private List<GameEntry> comments;

    /**
     * Constructor per a Room i per a la creació de nous jocs desats.
     * @param gameId ID únic del joc (provinent de l'API RAWG).
     * @param title Títol del joc.
     * @param imgUrl URL de la imatge de fons.
     */
    public SavedGame(int gameId, String title, String imgUrl) {
        this.gameId = gameId;
        this.title = title;
        this.imgUrl = imgUrl;
    }

    // --- GETTERS I SETTERS ---

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
