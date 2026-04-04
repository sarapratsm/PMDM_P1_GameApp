package com.spm.gameapp.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameApi {
    private static final String BASE_URL = "https://api.rawg.io/api/";
    public static final String API_KEY = "b0e2846ab3e24c959a4b125c187cfe62";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Configurem un OkHttpClient amb timeouts més llargs per evitar SocketTimeoutException
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}