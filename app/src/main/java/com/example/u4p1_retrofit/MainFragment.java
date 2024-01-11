package com.example.u4p1_retrofit;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.u4p1_retrofit.databinding.FragmentMainBinding;
import com.example.u4p1_retrofit.databinding.ViewholderBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

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

        MainViewModel MainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ContenidosAdapter contenidosAdapter = new ContenidosAdapter();
        binding.rvContenidos.setAdapter(contenidosAdapter);

        binding.texto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MainViewModel.buscar();
                return false;
            }
        });

        MainViewModel.getRespuesta().observe(getViewLifecycleOwner(),
                respuesta -> contenidosAdapter.establecerListaContenido(respuesta.results));
    }

    static class ContenidosAdapter extends RecyclerView.Adapter<ContenidoViewHolder> {
        List<Main.Contenido> listaContenido;

        @NonNull
        @Override
        public ContenidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContenidoViewHolder(ViewholderBinding.inflate(LayoutInflater.from(parent.getContext())));
        }

        @Override
        public void onBindViewHolder(@NonNull ContenidoViewHolder holder, int position) {
            Main.Contenido contenido = listaContenido.get(position);
            holder.binding.title.setText(contenido.trackName);
            holder.binding.artist.setText(contenido.artistName);
            Picasso.get().load(contenido.artworkUrl100).into(holder.binding.artwork);
        }

        @Override
        public int getItemCount() {
            return listaContenido == null ? 0 : listaContenido.size();
        }

        void establecerListaContenido(List<Main.Contenido> listaContenido) {
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