package com.example.u4p1_retrofit;

import com.squareup.moshi.Json;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public class Main {
    public static class Todos {
        @Json(name = "Name")
        List<Contenido> results;
    }

    public static class Contenido {
        @Json(name = "Name")
        String artistName;
        @Json(name = "Race")
        String trackName;
        @Json(name = "img")
        String artworkUrl100;
    }

    public static Api api = new Retrofit.Builder()
            .baseUrl("https://firestore.googleapis.com/v1/projects/u4p1retrofit-a64c4/databases/(default)/documents/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(Api.class);

    public interface Api {
        @GET("{id}/")
        Call<Todos> obtener();
    }
}
