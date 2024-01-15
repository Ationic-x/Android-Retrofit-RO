package com.example.u4p1_retrofit;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;

import okio.BufferedSource;
import okio.Okio;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Main.Mob>> mobsLiveData;
    private Main.AllMobs allMobs;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mobsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Main.Mob>> getMobsLiveData() {
        return mobsLiveData;
    }

    public void buscar(Context context) {
        File archivo = context.getFileStreamPath("mobs.json");
        if(archivo.exists()){
            Log.d("MainViewModel", "Calling from file");
            if(mobsLiveData.getValue() == null) {
                try {
                    BufferedSource bufferedSource = Okio.buffer(Okio.source(archivo));
                    Moshi moshi = new Moshi.Builder().build();
                    JsonAdapter<Main.AllMobs> jsonAdapter = moshi.adapter(Main.AllMobs.class);
                    Main.AllMobs allMobs = jsonAdapter.fromJson(bufferedSource);
                    if(allMobs != null) {
                        mobsLiveData.postValue(allMobs.mobs);
                    }
                } catch (IOException ioe) {
                    Log.d("MainViewModel", ioe.getMessage());
                }
            }
        } else {
            obtenerMobsPagina(null);
            if(allMobs != null) {
                try {
                    Moshi moshi = new Moshi.Builder().build();
                    JsonAdapter<Main.AllMobs> jsonAdapter = moshi.adapter(Main.AllMobs.class);
                    FileOutputStream outputStream = context.openFileOutput("mobs.json", Context.MODE_PRIVATE);
                    outputStream.write(jsonAdapter.toJson(allMobs).getBytes());
                    outputStream.close();
                } catch (IOException ioe) {
                    Log.d("MainViewModel", ioe.getMessage());
                }
            }
        }
    }

    private void obtenerMobsPagina(String nextPageToken) {
        Main.api.getMobs(nextPageToken).enqueue(new Callback<Main.AllMobs>() {
            @Override
            public void onResponse(Call<Main.AllMobs> call, Response<Main.AllMobs> response) {
                if (response.isSuccessful()) {
                    Main.AllMobs allMobs = response.body();
                    if (allMobs != null) {
                        // Procesa los datos obtenidos
                        List<Main.Mob> mobsActuales = mobsLiveData.getValue();
                        if (mobsActuales == null) {
                            mobsActuales = new ArrayList<>();
                        }
                        mobsActuales.addAll(allMobs.mobs);
                        mobsLiveData.setValue(mobsActuales);

                        // Verifica si hay más páginas y realiza la solicitud para la siguiente página
                        //String nextPageToken = allMobs.nextPageToken;
                        if (nextPageToken != null && !nextPageToken.isEmpty()) {
                            obtenerMobsPagina(nextPageToken);
                        } else {
                            allMobs.mobs = mobsLiveData.getValue();
                            MainViewModel.this.allMobs = allMobs;
                        }
                    }
                } else {
                    Log.e("MainViewModel", "Error en la solicitud: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Main.AllMobs> call, Throwable t) {
                Log.e("MainViewModel", "Error en la solicitud: " + t.getMessage(), t);
            }
        });
    }
}
