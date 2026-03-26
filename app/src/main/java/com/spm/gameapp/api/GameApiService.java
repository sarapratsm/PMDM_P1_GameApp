package com.spm.gameapp.api;

import com.spm.gameapp.api.model.GameList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GameApiService {

    @GET("games")
    Call<GameList> getGames(
            @Query("key") String apiKey,
            @Query("page_size") int size,
            @Query("ordering") String ordering,
            @Query("platforms") String platforms
    );
}