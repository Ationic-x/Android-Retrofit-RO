package com.example.u4p1_retrofit;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
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
import java.util.stream.Collectors;

public class MainViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Main.Mob>> mobsLiveData;
    private static final String uri = "mobs";

    public MainViewModel(@NonNull Application application) {
        super(application);
        mobsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Main.Mob>> getMobsLiveData() {
        return mobsLiveData;
    }

    public void getAll(Context context) {
        File file = context.getFileStreamPath("mobs.json");
        if (file.exists()) {
            Log.d("MainViewModel", "Calling from file");
            if (mobsLiveData.getValue() == null) {
                try {
                    BufferedSource bufferedSource = Okio.buffer(Okio.source(file));
                    Moshi moshi = new Moshi.Builder().build();
                    JsonAdapter<Main.AllMobs> jsonAdapter = moshi.adapter(Main.AllMobs.class);
                    Main.AllMobs allMobs = jsonAdapter.fromJson(bufferedSource);
                    if (allMobs != null) {
                        mobsLiveData.postValue(allMobs.mobs);
                    }
                } catch (IOException ioe) {
                    Log.d("MainViewModel", ioe.getMessage());
                }
            }
        } else {
            obtenerMobsPagina(null, context);
        }
    }

    private void obtenerMobsPagina(String nextPageToken, Context context) {
        Main.api.getMobs(uri, nextPageToken).enqueue(new Callback<Main.AllMobs>() {
            @Override
            public void onResponse(Call<Main.AllMobs> call, Response<Main.AllMobs> response) {
                if (response.isSuccessful()) {
                    Main.AllMobs allMobs = response.body();
                    if (allMobs != null) {
                        List<Main.Mob> mobsActuales = mobsLiveData.getValue();
                        if (mobsActuales == null) {
                            mobsActuales = new ArrayList<>();
                        }
                        mobsActuales.addAll(allMobs.mobs);
                        mobsLiveData.setValue(mobsActuales);

                        // In case to see all the values just use allMobs.nextPageToken
                        if (nextPageToken != null && !nextPageToken.isEmpty()) {
                            obtenerMobsPagina(nextPageToken, context);
                        } else {
                            allMobs.mobs = mobsLiveData.getValue();
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

    public void filterMobs(Context context, String name, String element, String race, String size, int spLevel, String level, int spBExp, String bExp, int spJExp, String jExp) {
        File file = context.getFileStreamPath("mobs.json");
        if (file.exists()) {
            try {
                BufferedSource bufferedSource = Okio.buffer(Okio.source(file));
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<Main.AllMobs> jsonAdapter = moshi.adapter(Main.AllMobs.class);
                Main.AllMobs allMobs = jsonAdapter.fromJson(bufferedSource);
                if (allMobs != null) {
                    if (!name.isEmpty())
                        allMobs.mobs = allMobs.mobs.stream().filter(mob -> mob.fields.name.stringValue.toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
                    if (!element.equals("Any"))
                        allMobs.mobs = allMobs.mobs.stream().filter(mob -> mob.fields.element.stringValue.toLowerCase().equals(element.toLowerCase())).collect(Collectors.toList());
                    if (!race.equals("Any"))
                        allMobs.mobs = allMobs.mobs.stream().filter(mob -> mob.fields.race.stringValue.toLowerCase().equals(race.toLowerCase())).collect(Collectors.toList());
                    if (!race.equals("Any"))
                        allMobs.mobs = allMobs.mobs.stream().filter(mob -> mob.fields.race.stringValue.toLowerCase().equals(size.toLowerCase())).collect(Collectors.toList());
                    try {
                        if (!level.isEmpty()) {
                            switch (spLevel) {
                                case 0:
                                    allMobs.mobs = allMobs.mobs.stream().filter(mob -> mob.fields.level.integerValue.toLowerCase().equals(level.toLowerCase())).collect(Collectors.toList());
                                    break;
                                case 1:
                                    allMobs.mobs = allMobs.mobs.stream().filter(mob -> {
                                                int mobLevel = Integer.parseInt(mob.fields.level.integerValue);
                                                int filterLevel = Integer.parseInt(level);
                                                return mobLevel < filterLevel;
                                            })
                                            .collect(Collectors.toList());
                                    break;
                                default:
                                    allMobs.mobs = allMobs.mobs.stream().filter(mob -> {
                                                int mobLevel = Integer.parseInt(mob.fields.level.integerValue);
                                                int filterLevel = Integer.parseInt(level);
                                                return mobLevel > filterLevel;
                                            })
                                            .collect(Collectors.toList());
                                    break;
                            }
                        }
                        if (!bExp.isEmpty()) {
                            int filterBaseExp = Integer.parseInt(bExp);
                            switch (spBExp) {
                                case 0:
                                    allMobs.mobs = allMobs.mobs.stream().filter(mob -> mob.fields.baseExp.integerValue.toLowerCase().equals(bExp.toLowerCase())).collect(Collectors.toList());
                                    break;
                                case 1:
                                    allMobs.mobs = allMobs.mobs.stream().filter(mob -> {
                                                int mobBaseExp = Integer.parseInt(mob.fields.baseExp.integerValue);
                                                return mobBaseExp < filterBaseExp;
                                            })
                                            .collect(Collectors.toList());
                                    break;
                                default:
                                    allMobs.mobs = allMobs.mobs.stream().filter(mob -> {
                                                int mobBaseExp = Integer.parseInt(mob.fields.baseExp.integerValue);
                                                return mobBaseExp > filterBaseExp;
                                            })
                                            .collect(Collectors.toList());
                                    break;
                            }
                        }
                        if (!jExp.isEmpty()) {
                            int filterJobExp = Integer.parseInt(jExp);
                            switch (spJExp) {
                                case 0:
                                    allMobs.mobs = allMobs.mobs.stream().filter(mob -> mob.fields.jobExp.integerValue.toLowerCase().equals(jExp.toLowerCase())).collect(Collectors.toList());
                                    break;
                                case 1:
                                    allMobs.mobs = allMobs.mobs.stream().filter(mob -> {
                                                int mobJobExp = Integer.parseInt(mob.fields.jobExp.integerValue);
                                                return mobJobExp < filterJobExp;
                                            })
                                            .collect(Collectors.toList());
                                    break;
                                default:
                                    allMobs.mobs = allMobs.mobs.stream().filter(mob -> {
                                                int mobJobExp = Integer.parseInt(mob.fields.jobExp.integerValue);
                                                return mobJobExp > filterJobExp;
                                            })
                                            .collect(Collectors.toList());
                                    break;
                            }
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Someone of this field are wrong: Level, Base Exp or Job Exp", Toast.LENGTH_SHORT).show();
                    }
                    mobsLiveData.postValue(allMobs.mobs);
                }
            } catch (IOException ioe) {
                Log.d("MainViewModel", ioe.getMessage());
            }
        }
        ;
    }
}
