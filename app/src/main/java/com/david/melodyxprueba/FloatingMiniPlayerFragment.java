package com.david.melodyxprueba;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

//public class FloatingMiniPlayerFragment extends BottomSheetDialogFragment {
public class FloatingMiniPlayerFragment extends Fragment {

    /*private BottomSheetBehavior bottomSheetBehavior;

    public FloatingMiniPlayerFragment() {
        // Required empty public constructor
    }*/

    private AdapterPersonalizadoArtistaPrueba adapter;
    private String trackImageUrl;
    private String artistName;
    private String trackName;
    private int pauseIcon;
    private int playIcon;
    private String trackUrl;
    private ImageView playPauseButton;
    private ImageView miniPlayPauseButton;
    private ImageView nextButton;
    private ImageView previousButton;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView textViewStartTime;
    private TextView textViewEndTime;
    private Runnable updateSeekBar;
    private Handler mHandler;
    private int position;
    private ArrayList<ArtistasSpotify> mArtistas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mini_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getDialog().setCanceledOnTouchOutside(false);
        //getDialog().setCancelable(false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), ReproductorActivity.class);
                //startActivity(intent);
                // Configurar el diseño del fragmento
                /*View layout = FloatingMiniPlayerFragment.this.onCreateView(LayoutInflater.from(getContext()), null, null);

                // Configurar el cuadro de diálogo
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.FullScreenDialog);
                builder.setView(layout);
                AlertDialog dialog = builder.create();

                // Mostrar el cuadro de diálogo
                dialog.show();*/
            }
        });

        miniPlayPauseButton = view.findViewById(R.id.miniPlayPauseButton);

        Bundle bundle = getArguments();
        if (bundle != null) {

            artistName = bundle.getString("artistName");
            trackName = bundle.getString("trackName");
            trackImageUrl = bundle.getString("trackImageUrl");
            //btnPause = getArguments().getInt("pauseBtn");
            pauseIcon = bundle.getInt("pauseIcon");
            playIcon = bundle.getInt("playIcon");
            trackUrl = bundle.getString("trackUrl");

            TextView tvArtistName3 = view.findViewById(R.id.txtArtistNameMiniPlayer);
            tvArtistName3.setText(artistName);

            TextView tvTrackName = view.findViewById(R.id.trackName);
            tvTrackName.setText(trackName);

            ImageView ivTrackImage = view.findViewById(R.id.miniImgTrack);
            Glide.with(getContext()).load(trackImageUrl).into(ivTrackImage);
        }

        LinearLayout linearLayout = view.findViewById(R.id.linearLayoutMiniPlayer);
        //CardView cardView = view.findViewById(R.id.linearLayoutMiniPlayer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la vista de reproductor_layout en pantalla grande
                openReproductorLayout();
            }
        });
        // Aquí puedes inicializar los elementos del floating mini player y configurar su comportamiento
        // por ejemplo, puedes controlar la reproducción de música, mostrar información de la canción, etc.

    }

    private void openReproductorLayout() {
        // Crea un intent para abrir la nueva actividad (ReproductorActivity en este ejemplo)
        //Intent intent = new Intent(getContext(), ReproductorActivity.class);
        // Aquí puedes agregar cualquier dato adicional que necesites pasar a la actividad
        // Por ejemplo, puedes pasar el ID de la canción o cualquier otro parámetro necesario
        // intent.putExtra("songId", songId);
        //startActivity(intent);

        /*Fragment reproductorActivity = new ReproductorFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, reproductorActivity);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.FullScreenDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reproductor_layout, null);

        ImageView ivAlbumImage = dialogView.findViewById(R.id.albumImage);
        Glide.with(getContext()).load(trackImageUrl).into(ivAlbumImage);

        TextView tvArtistName = dialogView.findViewById(R.id.artistName);
        tvArtistName.setText(artistName);

        TextView tvTrackName = dialogView.findViewById(R.id.trackName);
        tvTrackName.setText(trackName);

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Obtener el contexto y la lista de artistas desde los argumentos
        Context context = requireContext();
        ArrayList<ArtistasSpotify> artistas = getArguments().getParcelableArrayList("artistas");

        // Crear una instancia del adaptador
        AdapterPersonalizadoArtistaPrueba adapter = new AdapterPersonalizadoArtistaPrueba(context, R.layout.reproductor_layout, artistas);

        //adapter.playMediaPlayer(); // Ejemplo: reproducir la música
        //adapter.pauseMediaPlayer(); // Ejemplo: pausar la música
        //adapter.nextTrack(); // Ejemplo: avanzar a la siguiente canción
        //adapter.previousTrack(); // Ejemplo: retroceder a la canción anterior

        mediaPlayer = new MediaPlayer();
        mHandler = new Handler();
        textViewStartTime = dialogView.findViewById(R.id.textViewStartTime);
        textViewEndTime = dialogView.findViewById(R.id.textViewEndTime);
        seekBar = dialogView.findViewById(R.id.seekBar);
        position = 0;
        mArtistas = getArguments().getParcelableArrayList("artistas");

        playPauseButton = dialogView.findViewById(R.id.playPauseButton);
        nextButton = dialogView.findViewById(R.id.nextButton);
        previousButton = dialogView.findViewById(R.id.previousButton);

            // Configurar el reproductor
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(trackUrl);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // Actualizar el tiempo final del audio
                        int duration = mediaPlayer.getDuration();
                        String endTime = updateStartTimeTextView(duration);
                        textViewEndTime.setText(endTime);

                        // Actualizar la posición actual de la canción en el SeekBar
                        seekBar.setMax(duration);

                        // Comenzar la reproducción del audio
                        playMediaPlayer();

                        mHandler.post(updateSeekBar);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al reproducir la canción", Toast.LENGTH_SHORT).show();
        }

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    pauseMediaPlayer();
                } else {
                    playMediaPlayer();
                }
            }
        });

        miniPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    pauseMediaPlayer();
                } else {
                    playMediaPlayer();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               playPreviousSong();
           }
        });

        //miniPlayPauseButton

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.postDelayed(updateSeekBar, 100);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar.setProgress(0);
                textViewEndTime.setText(updateStartTimeTextView(mediaPlayer.getDuration() / 1000));
                playPauseButton.setImageResource(R.drawable.ic_play_final);

                //nuevo
                mHandler.removeCallbacks(updateSeekBar);

                //Simula el click y pas a la siguiente canción
                nextButton.performClick();
            }
        });


        ImageView btnClose = dialogView.findViewById(R.id.btnDown);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showFloatingMiniPlayerFragment(artistName, trackName, trackImageUrl);
            }
        });


    }

    private void showFloatingMiniPlayerFragment(String artistName, String trackName, String trackImageUrl) {

        FloatingMiniPlayerFragment floatingMiniPlayerFragment = new FloatingMiniPlayerFragment();

        Bundle bundle = new Bundle();
        bundle.putString("artistName", artistName);
        bundle.putString("trackName", trackName);
        bundle.putString("trackImageUrl", trackImageUrl);
        floatingMiniPlayerFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, floatingMiniPlayerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();




    }

    protected void playMediaPlayer() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.ic_pause_final);
            miniPlayPauseButton.setImageResource(R.drawable.ic_pause_final);
        }

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    updateStartTimeTextView(currentPosition);
                }
                mHandler.postDelayed(this, 100);
            }
        };

        // Obtener el tiempo de la canción actualizado por segundo
        updateStartTimeTextView(0);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    updateStartTimeTextView(currentPosition);
                    mHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    protected void pauseMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setImageResource(R.drawable.ic_play_final);
            miniPlayPauseButton.setImageResource(R.drawable.ic_play_final);
        }

        // Detener la actualización del tiempo de reproducción
        mHandler.removeCallbacks(updateSeekBar);
    }

    private String updateStartTimeTextView(int currentPosition) {
        int minutes = currentPosition / 60000;
        int seconds = (currentPosition % 60000) / 1000;
        String timeString = String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
        textViewStartTime.setText(timeString);

        return timeString;
    }

    private void playNextSong() {
        int currentIndex = -1;

        // Buscar el índice de la canción actual en la lista de artistas
        for (int i = 0; i < mArtistas.size(); i++) {
            ArtistasSpotify artist = mArtistas.get(i);
            if (artist.getTrack().equals(trackUrl)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex != -1) {
            int nextIndex = (currentIndex + 1) % mArtistas.size(); // Obtener el índice de la siguiente canción

            ArtistasSpotify nextArtist = mArtistas.get(nextIndex);
            String nextTrackUrl = nextArtist.getTrack();

            // Detener la reproducción actual
            mediaPlayer.stop();
            mediaPlayer.reset();

            try {
                // Configurar la nueva canción para reproducir
                mediaPlayer.setDataSource(nextTrackUrl);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // Actualizar la información de la canción actual
                        trackUrl = nextTrackUrl;
                        artistName = nextArtist.getArtistName();
                        trackName = nextArtist.getTrackName();
                        trackImageUrl = nextArtist.getTrackImage();

                        // Actualizar la interfaz de usuario con la nueva información
                        updateUI();

                        // Comenzar la reproducción de la siguiente canción
                        playMediaPlayer();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al reproducir la siguiente canción", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playPreviousSong() {
        int currentIndex = -1;

        // Buscar el índice de la canción actual en la lista de artistas
        for (int i = 0; i < mArtistas.size(); i++) {
            ArtistasSpotify artist = mArtistas.get(i);
            if (artist.getTrack().equals(trackUrl)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex != -1) {
            int previousIndex = (currentIndex - 1 + mArtistas.size()) % mArtistas.size(); // Obtener el índice de la canción anterior

            ArtistasSpotify previousArtist = mArtistas.get(previousIndex);
            String previousTrackUrl = previousArtist.getTrack();

            // Detener la reproducción actual
            mediaPlayer.stop();
            mediaPlayer.reset();

            try {
                // Configurar la nueva canción para reproducir
                mediaPlayer.setDataSource(previousTrackUrl);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // Actualizar la información de la canción actual
                        trackUrl = previousTrackUrl;
                        artistName = previousArtist.getArtistName();
                        trackName = previousArtist.getTrackName();
                        trackImageUrl = previousArtist.getTrackImage();

                        // Actualizar la interfaz de usuario con la nueva información
                        updateUI();

                        // Comenzar la reproducción de la canción anterior
                        playMediaPlayer();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al reproducir la canción anterior", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void updateUI() {
        // Actualizar la información de la canción en la interfaz de usuario
        TextView tvArtistName3 = getView().findViewById(R.id.txtArtistNameMiniPlayer);
        tvArtistName3.setText(artistName);

        TextView tvTrackName = getView().findViewById(R.id.trackName);
        tvTrackName.setText(trackName);

        ImageView ivTrackImage = getView().findViewById(R.id.miniImgTrack);
        Glide.with(getContext()).load(trackImageUrl).into(ivTrackImage);
    }

}

