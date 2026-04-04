package com.spm.gameapp.repository;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.spm.gameapp.api.GameApi;
import com.spm.gameapp.api.GameApiService;
import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.api.model.GameList;
import com.spm.gameapp.api.model.GameScreenshotsResponse;
import com.spm.gameapp.api.model.Screenshot;
import com.spm.gameapp.db.dao.GameEntryDao;
import com.spm.gameapp.db.dao.SavedGameDao;
import com.spm.gameapp.db.model.GameEntry;
import com.spm.gameapp.db.model.SavedGame;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repositori centralitzat de l'aplicació.
 * Gestiona la comunicació amb l'API externa (RAWG) i la base de dades local (Room).
 * Actua com a Font Única de Veritat (Single Source of Truth).
 */
public class GameRepository {
    private static final String TAG = "GameRepository";
    private final GameApiService apiService;
    private final SavedGameDao savedGameDao;
    private final GameEntryDao gameEntryDao;
    
    // Executor per a operacions en fil secundari per no bloquejar la UI
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    // Handler per retornar els resultats al fil principal
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GameRepository(SavedGameDao savedGameDao, GameEntryDao gameEntryDao) {
        this.apiService = GameApi.getClient().create(GameApiService.class);
        this.savedGameDao = savedGameDao;
        this.gameEntryDao = gameEntryDao;
    }

    // --- MÈTODES D'API (RETROFIT) ---

    /**
     * Recupera el llistat de jocs des de l'API de RAWG.
     */
    public Call<GameList> getGames(String ordering, String platforms) {
        Log.d(TAG, "getGames: carregant jocs amb ordre=" + ordering + " i plataformes=" + platforms);
        String ord = (ordering == null || ordering.isEmpty()) ? null : ordering;
        String plat = (platforms == null || platforms.isEmpty()) ? null : platforms;
        return apiService.getGames(GameApi.API_KEY, 24, ord, plat);
    }

    /**
     * Recupera els screenshots d'un joc concret per ID.
     * Notifica una llista de Screenshots directament al ViewModel.
     */
    public void getGameScreenshots(int gameId, RepositoryCallback<List<Screenshot>> callback) {
        apiService.getGameScreenshots(gameId, GameApi.API_KEY).enqueue(new Callback<GameScreenshotsResponse>() {
            @Override
            public void onResponse(Call<GameScreenshotsResponse> call, Response<GameScreenshotsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Aquí extraiem la llista del 'wrapper' de l'API
                    callback.onLoaded(response.body().getResults());
                } else {
                    callback.onError(new Exception("Error carregant captures: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<GameScreenshotsResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }




    /**
     * Recupera el detall d'un joc concret per ID.
     */
    public Call<Game> getGameDetail(int gameId) {
        Log.d(TAG, "getGameDetail: carregant detall del joc ID=" + gameId);
        return apiService.getGameDetail(gameId, GameApi.API_KEY);
    }

    public void deleteSavedGame(SavedGame game, Object o) {
        Log.d(TAG, "deleteSavedGame: eliminant joc " + game.getTitle());
        executor.execute(() -> {
            try {
                savedGameDao.delete(game);
                Log.d(TAG, "deleteSavedGame: Joc eliminat correctament: " + game.getTitle());
            } catch (Exception e) {
                Log.e(TAG, "deleteSavedGame: Error eliminant el joc", e);
            }
        });
    }

    // --- MÈTODES DE BASE DE DADES (ROOM) ---

    /**
     * Interfície genèrica per a les operacions de base de dades.
     */
    public interface RepositoryCallback<T> {
        void onLoaded(T data);
        void onError(Throwable t);
    }

    // --- GESTIÓ DE SAVED GAMES ---

    public void saveGame(SavedGame game, Runnable onComplete) {
        Log.d(TAG, "saveGame: desant joc " + game.getTitle());
        executor.execute(() -> {
            try {
                savedGameDao.insert(game);
                if (onComplete != null) mainHandler.post(onComplete);
                Log.d(TAG, "saveGame: Joc desat correctament: " + game.getTitle());
            } catch (Exception e) {
                Log.e(TAG, "saveGame: Error desant el joc", e);
            }
        });
    }

    public void deleteGame(SavedGame game, Runnable onComplete) {
        Log.d(TAG, "deleteGame: eliminant joc " + game.getTitle());
        executor.execute(() -> {
            try {
                savedGameDao.delete(game);
                if (onComplete != null) mainHandler.post(onComplete);
                Log.d(TAG, "deleteGame: Joc eliminat correctament: " + game.getTitle());
            } catch (Exception e) {
                Log.e(TAG, "deleteGame: Error eliminant el joc", e);
            }
        });
    }

    public void getAllSavedGames(RepositoryCallback<List<SavedGame>> callback) {
        Log.d(TAG, "getAllSavedGames: carregant tots els jocs desats");
        executor.execute(() -> {
            try {
                List<SavedGame> games = savedGameDao.getAllSavedGames();
                Log.d(TAG, "getAllSavedGames: carregats " + games.size() + " jocs");
                mainHandler.post(() -> callback.onLoaded(games));
            } catch (Exception e) {
                Log.e(TAG, "getAllSavedGames: Error carregant jocs", e);
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    // Rep el gameId i el callback com a paràmetres
    public void isGameSaved(int gameId, RepositoryCallback<Boolean> callback) {
        Log.d(TAG, "isGameSaved: comprovant si el joc ID=" + gameId + " està desat");
        executor.execute(() -> {
            try {
                // Busquem a la base de dades
                SavedGame game = savedGameDao.getSavedGameById(gameId);
                boolean isSaved = (game != null);

                Log.d(TAG, "isGameSaved: el joc ID=" + gameId + (isSaved ? " SÍ" : " NO") + " està desat");

                // Retornem el resultat al fil principal mitjançant el callback
                mainHandler.post(() -> callback.onLoaded(isSaved));
            } catch (Exception e) {
                Log.e(TAG, "isGameSaved: Error comprovant si el joc està desat", e);
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    // --- GESTIÓ DE GAME ENTRIES (BLOG) ---

    public void insertGameEntry(GameEntry entry, Runnable onComplete) {
        Log.d(TAG, "insertGameEntry: afegint entrada de blog per al joc ID=" + entry.getGameId());
        executor.execute(() -> {
            try {
                gameEntryDao.insert(entry);
                if (onComplete != null) mainHandler.post(onComplete);
                Log.d(TAG, "insertGameEntry: Entrada de blog afegida correctament");
            } catch (Exception e) {
                Log.e(TAG, "insertGameEntry: Error afegint entrada de blog", e);
            }
        });
    }

    public void getEntriesForGame(int gameId, RepositoryCallback<List<GameEntry>> callback) {
        Log.d(TAG, "getEntriesForGame: carregant entrades de blog per al joc ID=" + gameId);
        executor.execute(() -> {
            try {
                List<GameEntry> entries = gameEntryDao.getGameEntriesByGameId(gameId);
                Log.d(TAG, "getEntriesForGame: carregades " + entries.size() + " entrades");
                mainHandler.post(() -> callback.onLoaded(entries));
            } catch (Exception e) {
                Log.e(TAG, "getEntriesForGame: Error carregant entrades de blog", e);
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    public void deleteGameEntry(GameEntry entry, Runnable onComplete) {
        Log.d(TAG, "deleteGameEntry: eliminant entrada de blog ID=" + entry.getId());
        executor.execute(() -> {
            try {
                gameEntryDao.delete(entry);
                if (onComplete != null) mainHandler.post(onComplete);
                Log.d(TAG, "deleteGameEntry: Entrada de blog eliminada correctament");
            } catch (Exception e) {
                Log.e(TAG, "deleteGameEntry: Error eliminant entrada de blog", e);
            }
        });
    }
}
