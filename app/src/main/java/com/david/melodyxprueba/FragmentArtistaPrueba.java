package com.david.melodyxprueba;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.david.melodyxprueba.databinding.FragmentArtistaOverviewBinding;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FragmentArtistaPrueba extends Fragment {

    private FragmentArtistaOverviewBinding binding;
    private AdapterPersonalizadoArtistaPrueba adapter1;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArtistaOverviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swiperefreshlayout);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
                //swipeRefreshLayout.setEnabled(false);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<ArtistasSpotify> items = new ArrayList<>();

        adapter1 = new AdapterPersonalizadoArtistaPrueba(
                getContext(),
                R.layout.artist_overview_image,
                items
        );

        binding.lvDatosArtistas.setAdapter(adapter1);
        refresh();
    }

    public void refresh() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        executor.execute(() -> {
            PruebaAPI pruebaAPI = new PruebaAPI(getContext(), sharedPreferences);
            ArrayList<ArtistasSpotify> artistas = pruebaAPI.getArtistasFromCache();

            if (artistas == null) {
                artistas = pruebaAPI.getArtistas();
                pruebaAPI.saveArtistas(artistas);
            }

            final ArrayList<ArtistasSpotify> finalArtistas = artistas;
            handler.post(() -> {
                adapter1.clear();
                adapter1.addAll(finalArtistas);
            });
        });

        FancyToast.makeText(getContext(), "Cargando...", FancyToast.LENGTH_LONG, FancyToast.CONFUSING, false).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
