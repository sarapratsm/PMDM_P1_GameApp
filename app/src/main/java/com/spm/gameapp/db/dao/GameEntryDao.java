package com.spm.gameapp.db.dao;

import androidx.room.*;

import com.spm.gameapp.db.model.GameEntry;

import java.util.List;

@Dao
public interface GameEntryDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameEntry gameEntry);

    @Query("SELECT * FROM game_entries WHERE gameId = :gameId")
    List<GameEntry> getGameEntriesByGameId(int gameId);

    @Query("DELETE FROM game_entries WHERE gameId = :gameId")
    void deleteGameEntriesByGameId(int gameId);

    @Query("SELECT * FROM game_entries")
    List<GameEntry> getAllGameEntries();

    @Delete
    void delete(GameEntry gameEntry);
}
