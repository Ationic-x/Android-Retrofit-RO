package com.example.u4p1_retrofit;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.u4p1_retrofit.databinding.FragmentMainBinding;
import com.example.u4p1_retrofit.databinding.ViewholderBinding;
import org.jetbrains.annotations.NotNull;
import oupson.apng.decoder.ApngDecoder;

import java.io.File;
import java.util.List;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private boolean visible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMainBinding.inflate(inflater, container, false)).getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getAll(requireContext());
        ContenidosAdapter contenidosAdapter = new ContenidosAdapter(getContext());
        binding.rvContenidos.setAdapter(contenidosAdapter);


        ArrayAdapter<CharSequence> value_adapter = ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.value_array,
                android.R.layout.simple_spinner_item
        );
        ArrayAdapter<CharSequence> element_adapter = ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.element_array,
                android.R.layout.simple_spinner_item
        );
        ArrayAdapter<CharSequence> race_adapter = ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.race_array,
                android.R.layout.simple_spinner_item
        );
        ArrayAdapter<CharSequence> size_adapter = ArrayAdapter.createFromResource(
                this.requireContext(),
                R.array.size_array,
                android.R.layout.simple_spinner_item
        );
        value_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        element_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        race_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.levelSpinner.setAdapter(value_adapter);
        binding.jexpSpinner.setAdapter(value_adapter);
        binding.bexpSpinner.setAdapter(value_adapter);
        binding.eleSpinner.setAdapter(element_adapter);
        binding.raceSpinner.setAdapter(race_adapter);
        binding.sizeSpinner.setAdapter(size_adapter);

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.filterMobs(requireContext(),
                        binding.iName.getText().toString(),
                        binding.eleSpinner.getSelectedItem().toString(),
                        binding.raceSpinner.getSelectedItem().toString(),
                        binding.sizeSpinner.getSelectedItem().toString(),
                        binding.levelSpinner.getSelectedItemPosition(),
                        binding.iLevel.getText().toString(),
                        binding.bexpSpinner.getSelectedItemPosition(),
                        binding.iBexp.getText().toString(),
                        binding.jexpSpinner.getSelectedItemPosition(),
                        binding.iJexp.getText().toString());
            }
        });

        mainViewModel.getMobsLiveData().observe(getViewLifecycleOwner(),
                respuesta -> contenidosAdapter.establecerListaContenido(respuesta));

        binding.expandOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.expandableArea.getVisibility() == View.VISIBLE){
                    binding.option.setText(R.string.expand);
                    binding.expandableArea.animate()
                            .translationY(-binding.expandableArea.getHeight())
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    binding.expandableArea.setVisibility(View.GONE);
                                }
                            })
                            .start();
                    binding.expandOption.animate()
                            .translationY(-binding.expandableArea.getHeight())
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    binding.expandOption.setTranslationY(0);
                                }
                            })
                            .start();
                    binding.rvContenidos.animate()
                            .translationY(-binding.expandableArea.getHeight())
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    binding.rvContenidos.setTranslationY(0);
                                }
                            })
                            .start();
                } else {
                    binding.option.setText(R.string.collapse);
                    binding.expandableArea.setVisibility(View.VISIBLE);
                    binding.expandOption.setTranslationY(-binding.expandableArea.getHeight());
                    binding.rvContenidos.setTranslationY(-binding.expandableArea.getHeight());
                    binding.expandOption.animate()
                            .translationY(0)
                            .setDuration(300)
                            .start();
                    binding.rvContenidos.animate()
                            .translationY(0)
                            .setDuration(300)
                            .start();
                    binding.expandableArea.animate()
                            .translationY(0)
                            .setDuration(300)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    binding.expandableArea.setVisibility(View.VISIBLE);
                                }
                            })
                            .start();
                }
            }
        });
    }

    static class ContenidosAdapter extends RecyclerView.Adapter<ContenidoViewHolder> {
        List<Main.Mob> listaContenido;
        private Context context;

        public ContenidosAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ContenidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContenidoViewHolder(ViewholderBinding.inflate(LayoutInflater.from(parent.getContext())));
        }

        @Override
        public void onBindViewHolder(@NonNull ContenidoViewHolder holder, int position) {
            Main.Mob contenido = listaContenido.get(position);
            String level = contenido.fields.level == null ? "1" : contenido.fields.level.integerValue;
            String agi = contenido.fields.agi == null ? "1" : contenido.fields.agi.integerValue;
            String luk = contenido.fields.luk == null ? "1" : contenido.fields.luk.integerValue;
            String dex = contenido.fields.dex == null ? "1" : contenido.fields.dex.integerValue;
            String str = contenido.fields.str == null ? "1" : contenido.fields.str.integerValue;
            String hp = contenido.fields.hp == null ? "1" : contenido.fields.hp.integerValue;
            String bExp = contenido.fields.baseExp == null ? "1" : contenido.fields.baseExp.integerValue;
            String jExp = contenido.fields.jobExp == null ? "1" : contenido.fields.jobExp.integerValue;
            String atkDelayMs = contenido.fields.attackDelay == null ? "1" : contenido.fields.attackDelay.integerValue;
            String attackMotion = contenido.fields.attackMotion == null ? "1" : contenido.fields.attackMotion.integerValue;
            String[] splitedName = contenido.name.split("/");
            String walkSpeed = contenido.fields.walkSpeed == null ? "1"  : contenido.fields.walkSpeed.integerValue;
            String atk = contenido.fields.attack == null ? "1" : contenido.fields.attack.integerValue;
            String atk2 = contenido.fields.attack2 == null ? "1" : contenido.fields.attack2.integerValue;
            int hit100 = 100 + 100 + Integer.parseInt(level) + Integer.parseInt(agi);
            int flee95 = 170 + Integer.parseInt(level) + Integer.parseInt(dex);
            float bExpHp = Float.parseFloat(bExp) / Float.parseFloat(hp);
            float jExpHp = Float.parseFloat(jExp) / Float.parseFloat(hp);
            float atkDelay = Float.parseFloat(atkDelayMs) / 1000;
            float cellSpeed = 1000 / Float.parseFloat(walkSpeed);
            float aspd = (float)(Math.round(1000 / Float.parseFloat(attackMotion) * 100)) / 100;
            float bAtk = Float.parseFloat(level) / 4 + Float.parseFloat(str) + Float.parseFloat(dex) / 5 + Float.parseFloat(luk) / 3;
            holder.binding.name.setText(contenido.fields.name.stringValue);
            //holder.binding.aegisName.setText(context.getResources().getString(R.string.aegisName, contenido.fields.aegisName.stringValue));
            holder.binding.id.setText(context.getResources().getString(R.string.id, splitedName[splitedName.length - 1]));
            holder.binding.hp.setText(hp);
            ApngDecoder.decodeApngAsyncInto(context, contenido.fields.img.stringValue, holder.binding.img);
            holder.binding.neutral.setText(contenido.fields.elementStats.mapValue.fields.neutral.integerValue + "%");
            holder.binding.lvl.setText(level);
            holder.binding.water.setText(contenido.fields.elementStats.mapValue.fields.water.integerValue + "%");
            holder.binding.race.setText(contenido.fields.race.stringValue);
            holder.binding.earth.setText(contenido.fields.elementStats.mapValue.fields.earth.integerValue + "%");
            holder.binding.property.setText(contenido.fields.element.stringValue + " " + contenido.fields.elementLevel.integerValue);
            holder.binding.fire.setText(contenido.fields.elementStats.mapValue.fields.fire.integerValue + "%");
            holder.binding.size.setText(contenido.fields.size.stringValue);
            holder.binding.wind.setText(contenido.fields.elementStats.mapValue.fields.wind.integerValue + "%");
            holder.binding.hit.setText(String.valueOf(hit100));
            holder.binding.poison.setText(contenido.fields.elementStats.mapValue.fields.poison.integerValue + "%");
            holder.binding.flee.setText(String.valueOf(flee95));
            holder.binding.baseExp.setText(bExp);
            holder.binding.holy.setText(contenido.fields.elementStats.mapValue.fields.holy.integerValue + "%");
            holder.binding.walkSpeed.setText(String.format("%.2f", cellSpeed));
            holder.binding.jobExp.setText(jExp);
            holder.binding.shadow.setText(contenido.fields.elementStats.mapValue.fields.dark.integerValue + "%");
            holder.binding.atkDelay.setText(String.format("%.3f", atkDelay));
            holder.binding.baseExpHp.setText(String.format("%.3f", bExpHp));
            holder.binding.ghost.setText(contenido.fields.elementStats.mapValue.fields.ghost.integerValue + "%");
            holder.binding.atk.setText(Math.round(bAtk + Float.parseFloat(atk)) + " ~ " + Math.round(bAtk + Float.parseFloat(atk2)));
            holder.binding.jobExpHp.setText(String.format("%.3f", jExpHp));
            holder.binding.undead.setText(contenido.fields.elementStats.mapValue.fields.undead.integerValue + "%");
            holder.binding.aspd.setText(String.format("%.2f", aspd));
            holder.binding.def.setText(contenido.fields.defense == null ? "1" : contenido.fields.defense.integerValue);
            holder.binding.magicDef.setText(contenido.fields.magicDefense == null ? "1" : contenido.fields.magicDefense.integerValue);
            holder.binding.atkRange.setText(contenido.fields.attackRange == null ? "1" : contenido.fields.attackRange.integerValue);
            holder.binding.str.setText(str);
            holder.binding.agi.setText(agi);
            holder.binding.vit.setText(contenido.fields.vit == null ? "1" : contenido.fields.vit.integerValue);
            holder.binding.intelligence.setText(contenido.fields._int == null ? "1" : contenido.fields._int.integerValue);
            holder.binding.dex.setText(dex);
            holder.binding.luk.setText(luk);
            holder.binding.spellRange.setText(contenido.fields.skillRange == null ? "1" : contenido.fields.skillRange.integerValue);
            holder.binding.sightRange.setText(contenido.fields.chaseRange == null ? "1" : contenido.fields.chaseRange.integerValue);
        }

        @Override
        public int getItemCount() {
            return listaContenido == null ? 0 : listaContenido.size();
        }

        void establecerListaContenido(List<Main.Mob> listaContenido) {
            this.listaContenido = listaContenido;
            notifyDataSetChanged();
        }
    }

    static class ContenidoViewHolder extends RecyclerView.ViewHolder {
        ViewholderBinding binding;

        public ContenidoViewHolder(@NonNull ViewholderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}