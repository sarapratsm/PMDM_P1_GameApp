package com.spm.gameapp.api;

import com.spm.gameapp.api.model.Game;
import com.spm.gameapp.api.model.GameList;
import com.spm.gameapp.api.model.GameScreenshotsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GameApiService {

    @GET("games")
    Call<GameList> getGames(
            @Query("key") String apiKey,
            @Query("page_size") int size,
            @Query("ordering") String ordering,
            @Query("platforms") String platforms
    );

    @GET("games/{id}")
    Call<Game> getGameDetail(
            @Path("id") int gameId,
            @Query("key") String apiKey
    );

    @GET("games/{id}/screenshots")
    Call<GameScreenshotsResponse> getGameScreenshots(
            @Path("id") int gameId,
            @Query("key") String apiKey
    );
}
