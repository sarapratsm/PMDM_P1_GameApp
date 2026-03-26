package com.spm.gameapp.db.dao;

import com.spm.gameapp.db.model.SavedGame;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
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
