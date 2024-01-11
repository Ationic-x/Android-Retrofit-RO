package com.example.u4p1_retrofit;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private final MutableLiveData<Main.Todos> todos;

    public MainViewModel(@NonNull Application application) {
        super(application);
        todos = new MutableLiveData<>();
    }

    public MutableLiveData<Main.Todos> getRespuesta() {
        return todos;
    }

    public void buscar() {
        Main.api.obtener().enqueue(new Callback<Main.Todos>() {
            @Override
            public void onResponse(Call<Main.Todos> call, Response<Main.Todos> response) {
                if (response.body() != null) {
                    Log.d("Prueba", response.body().results.toString());
                    todos.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Main.Todos> call, Throwable throwable) {
                Log.d("ItunesViewModel", "Error en la llamada a la API de Itunes");
            }
        });
    }
}
