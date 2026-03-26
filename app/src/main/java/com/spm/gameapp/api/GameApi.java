package com.spm.gameapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameApi {
    private static final String BASE_URL = "https://api.rawg.io/api/";
    public static final String API_KEY = "b0e2846ab3e24c959a4b125c187cfe62";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}