package com.spm.gameapp.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entitat que representa una entrada del blog associada a un joc.
 * Permet desar comentaris de text i una referència a una imatge (screenshot).
 */
@Entity(tableName = "game_entries")
public class GameEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;          // Identificador únic de l'entrada
    private int gameId;      // ID del joc al qual pertany l'entrada
    private String comment;  // Text del comentari (blog)
    private String date;     // Data de creació de l'entrada
    private String imgUrl;   // Ruta de la captura de pantalla seleccionada

    /**
     * Constructor per crear una nova entrada de blog.
     * @param gameId ID del joc associat.
     * @param comment Text del comentari.
     * @param date Data de l'entrada.
     * @param imgUrl Ruta de la imatge.
     */
    public GameEntry(int gameId, String comment, String date, String imgUrl) {
        this.gameId = gameId;
        this.comment = comment;
        this.date = date;
        this.imgUrl = imgUrl;
    }

    // Getters i Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
