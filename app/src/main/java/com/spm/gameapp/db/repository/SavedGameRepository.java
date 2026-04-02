package com.spm.gameapp.db.repository;

import androidx.lifecycle.MutableLiveData;

import com.spm.gameapp.db.dao.SavedGameDao;
import com.spm.gameapp.db.model.SavedGame;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavedGameRepository {

    private SavedGameDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private SavedGameDao savedGameDao;
    public SavedGameRepository(SavedGameDao savedGameDao) {
        this.dao = savedGameDao;
    }

    public void insertSavedGame(SavedGame game, Runnable onComplete) {
        executor.execute(() -> {
            savedGameDao.insert(game);
            if (onComplete != null) onComplete.run();
        });
    }

    public List<SavedGame> getAllSavedGames() {
        // Nota: Room no permet consultes a la UI Thread
        return savedGameDao.getAllSavedGames();
    }

    public void deleteSavedGame(SavedGame game, Runnable onComplete) {
        executor.execute(() -> {
            savedGameDao.delete(game);
            if (onComplete != null) onComplete.run();
        });
    }



}
