package com.spm.gameapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.spm.gameapp.db.dao.GameEntryDao;
import com.spm.gameapp.db.dao.SavedGameDao;
import com.spm.gameapp.db.model.GameEntry;
import com.spm.gameapp.db.model.SavedGame;

@Database(entities = {SavedGame.class, GameEntry.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedGameDao savedGameDao();
    public abstract GameEntryDao gameEntryDao();

    public static final String DATABASE_NAME = "game_database";
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration() // Facilita el desenvolupament en canviar l'esquema
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
