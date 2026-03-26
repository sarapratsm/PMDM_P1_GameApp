package com.spm.gameapp.db.dao;

import com.spm.gameapp.db.model.SavedGame;
import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface SavedGameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SavedGame savedGame);

    @Query("SELECT * FROM saved_games")
    List<SavedGame> getAllSavedGames();

    @Query("SELECT * FROM saved_games WHERE gameId = :gameId")
    SavedGame getSavedGameById(int gameId);

    @Delete
    void delete(SavedGame savedGame);
}
