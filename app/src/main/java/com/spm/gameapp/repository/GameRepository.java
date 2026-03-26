package com.spm.gameapp.repository;

import com.spm.gameapp.api.GameApi;
import com.spm.gameapp.api.GameApiService;
import com.spm.gameapp.api.model.GameList;
import com.spm.gameapp.db.dao.SavedGameDao;

import retrofit2.Call;

public class GameRepository {
    private final GameApiService apiService;
    private final SavedGameDao savedGameDao;

    public GameRepository(SavedGameDao savedGameDao) {
        this.apiService = GameApi.getClient().create(GameApiService.class);
        this.savedGameDao = savedGameDao;
    }

    public Call<GameList> getGames(String ordering, String platforms) {
        return apiService.getGames(GameApi.API_KEY, 20, ordering, platforms);
    }
}
