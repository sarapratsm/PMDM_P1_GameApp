package com.spm.gameapp.repository;

import android.os.Handler;
import android.os.Looper;

import com.spm.gameapp.api.GameApi;
import com.spm.gameapp.api.GameApiService;
import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.api.model.GameList;
import com.spm.gameapp.db.dao.SavedGameDao;
import com.spm.gameapp.db.model.SavedGame;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;

/**
 * Repositori centralitzat de l'aplicació.
 * Gestiona la comunicació amb l'API externa (RAWG) i la base de dades local (Room).
 */
public class GameRepository {
    private final GameApiService apiService;
    private final SavedGameDao savedGameDao;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GameRepository(SavedGameDao savedGameDao) {
        this.apiService = GameApi.getClient().create(GameApiService.class);
        this.savedGameDao = savedGameDao;
    }

    // --- MÈTODES D'API (RETROFIT) ---

    /**
     * Recupera el llistat de jocs des de l'API de RAWG amb opcions d'ordenació i plataforma.
     */
    public Call<GameList> getGames(String ordering, String platforms) {
        String ord = (ordering == null || ordering.isEmpty()) ? null : ordering;
        String plat = (platforms == null || platforms.isEmpty()) ? null : platforms;
        return apiService.getGames(GameApi.API_KEY, 20, ord, plat);
    }

    /**
     * Recupera el detall d'un joc concret per ID des de l'API.
     */
    public Call<Game> getGameDetail(int gameId) {
        return apiService.getGameDetail(gameId, GameApi.API_KEY);
    }

    // --- MÈTODES DE BASE DE DADES (ROOM) ---

    /**
     * Interfície genèrica per a les operacions de base de dades que retornen dades.
     */
    public interface RepositoryCallback<T> {
        void onComplete(T result);
        void onError(Throwable t);
        void onLoaded(T games);
    }

    /**
     * Desa un joc a la base de dades local.
     */
    public void saveGame(SavedGame game, Runnable onComplete) {
        executor.execute(() -> {
            savedGameDao.insert(game);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    /**
     * Elimina un joc de la base de dades local.
     */
    public void deleteGame(SavedGame game, Runnable onComplete) {
        executor.execute(() -> {
            savedGameDao.delete(game);
            if (onComplete != null) mainHandler.post(onComplete);
        });
    }

    /**
     * Recupera tots els jocs guardats de forma asíncrona.
     */
    public void getAllSavedGames(RepositoryCallback<List<SavedGame>> callback) {
        executor.execute(() -> {
            try {
                List<SavedGame> games = savedGameDao.getAllSavedGames();
                new Handler(Looper.getMainLooper()).post(() -> callback.onLoaded(games));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(e));
            }
        });
    }

    /**
     * Comprova si un joc està guardat a la base de dades.
     */
    public void isGameSaved(int gameId, RepositoryCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                SavedGame game = savedGameDao.getSavedGameById(gameId);
                mainHandler.post(() -> callback.onComplete(game != null));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }
}
