/*package com.david.melodyxprueba;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ReproductorFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    private ImageView playPauseButton;
    private ImageView nextButton;
    private ImageView previousButton;
    private TextView textViewStartTime;
    private SeekBar seekBar;
    private Runnable updateSeekBar;
    private Handler mHandler;
    private String trackUrl;
    private String trackName;
    private String artistName;
    private String albumImage;
    private int position;
    private ArrayList<ArtistasSpotify> mArtistas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reproductor_layout, container, false);

        // Obtener la imagen del track
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.album_foto2);

        // Obtener los colores de la imagen
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
                // Obtener los colores dominantes de la imagen
                int startColor = palette.getDominantColor(0xFF000000); // Color de inicio predeterminado (negro)
                int endColor = palette.getLightVibrantColor(0xFFFFFFFF); // Color de fin predeterminado (blanco)

                // Actualizar los colores del fondo de gradiente
                View backgroundView = view.findViewById(R.id.backgroundView);
                GradientDrawable gradientDrawable = (GradientDrawable) backgroundView.getBackground();
                gradientDrawable.setColors(new int[]{startColor, endColor});
            }
        });

        // Obtener la información de la canción del Bundle
        Bundle args = getArguments();
        if (args != null) {
            //trackUrl = args.getString("trackUrl");
            trackName = args.getString("trackName");
            artistName = args.getString("artistName");
            albumImage = args.getString("albumImage");
            position = args.getInt("position");
        }

        // Inicializar las variables
        mHandler = new Handler();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Configurar elementos de la interfaz de usuario
        TextView tvTrackName = view.findViewById(R.id.trackName);
        TextView tvArtistName = view.findViewById(R.id.artistName);
        ImageView imgAlbum = view.findViewById(R.id.albumImage);
        textViewStartTime = view.findViewById(R.id.textViewStartTime);
        TextView textViewEndTime = view.findViewById(R.id.textViewEndTime);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        nextButton = view.findViewById(R.id.nextButton);
        previousButton = view.findViewById(R.id.previousButton);
        seekBar = view.findViewById(R.id.seekBar);

        // Cargar datos en la interfaz de usuario
        tvTrackName.setText(trackName);
        tvArtistName.setText(artistName);
        Glide.with(this).load(albumImage).into(imgAlbum);

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
                    String endTime = milliSecondsToTimer(duration);
                    textViewEndTime.setText(endTime);

                    // Actualizar la posición actual de la canción en el SeekBar
                    seekBar.setMax(duration);

                    // Comenzar la reproducción del audio
                    playMediaPlayer();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al reproducir la canción", Toast.LENGTH_SHORT).show();
        }

        // Configurar el botón de reproducción/pausa
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

        // Configurar el botón de siguiente
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArtistas.size() - 1) {
                    position++;
                    ArtistasSpotify artista = mArtistas.get(position);
                    trackUrl = artista.getTrack();
                    trackName = artista.getTrackName();
                    artistName = artista.getArtistName();
                    albumImage = artista.getAlbumImage();

                    // Actualizar la interfaz de usuario
                    tvTrackName.setText(trackName);
                    tvArtistName.setText(artistName);
                    Glide.with(ReproductorFragment.this).load(albumImage).into(imgAlbum);

                    // Reproducir la nueva canción
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(trackUrl);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                // Actualizar el tiempo final del audio
                                int duration = mediaPlayer.getDuration();
                                String endTime = milliSecondsToTimer(duration);
                                textViewEndTime.setText(endTime);

                                // Actualizar la posición actual de la canción en el SeekBar
                                seekBar.setMax(duration);

                                // Comenzar la reproducción del audio
                                playMediaPlayer();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error al reproducir la canción", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No hay más canciones", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el botón de anterior
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    position--;
                    ArtistasSpotify artista = mArtistas.get(position);
                    trackUrl = artista.getTrack();
                    trackName = artista.getTrackName();
                    artistName = artista.getArtistName();
                    albumImage = artista.getAlbumImage();

                    // Actualizar la interfaz de usuario
                    tvTrackName.setText(trackName);
                    tvArtistName.setText(artistName);
                    Glide.with(ReproductorFragment.this).load(albumImage).into(imgAlbum);

                    // Reproducir la nueva canción
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(trackUrl);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                // Actualizar el tiempo final del audio
                                int duration = mediaPlayer.getDuration();
                                String endTime = milliSecondsToTimer(duration);
                                textViewEndTime.setText(endTime);

                                // Actualizar la posición actual de la canción en el SeekBar
                                seekBar.setMax(duration);

                                // Comenzar la reproducción del audio
                                playMediaPlayer();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error al reproducir la canción", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No hay más canciones", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Pausar la reproducción del audio mientras se realiza un seguimiento del SeekBar
                pauseMediaPlayer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Reanudar la reproducción del audio después de realizar un seguimiento del SeekBar
                playMediaPlayer();
            }
        });

        // Actualizar el SeekBar y el tiempo transcurrido del audio
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
                String currentTime = milliSecondsToTimer(currentPosition);
                textViewStartTime.setText(currentTime);
                mHandler.postDelayed(this, 1000); // Actualizar cada 1 segundo
            }
        };
        mHandler.postDelayed(updateSeekBar, 0); // Comenzar a actualizar el SeekBar y el tiempo transcurrido

        return view;
    }

    private void playMediaPlayer() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.ic_pause_final);
        }
    }

    private void pauseMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setImageResource(R.drawable.ic_play_final);
        }
    }

    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            finalTimerString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }

        return finalTimerString;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mHandler.removeCallbacks(updateSeekBar);
    }
}
*/


/*public class ReproductorActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Button playPauseButton;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reproductor_layout);

        Intent intent = getIntent();
        String trackUrl = intent.getStringExtra("trackUrl");
        String trackName = intent.getStringExtra("trackName");
        String artistName = intent.getStringExtra("artistName");

        TextView tvArtistName = findViewById(R.id.txtArtistNameMiniPlayer);
        tvArtistName.setText(artistName);

        TextView tvTrackName = findViewById(R.id.trackName);
        tvTrackName.setText(trackName);

        ImageView ivTrackImage = findViewById(R.id.miniImgTrack);
        Glide.with(this).load(trackUrl).into(ivTrackImage);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(trackUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseMediaPlayer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playMediaPlayer();
            }
        });

        playPauseButton = findViewById(R.id.playPauseButton);
        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);

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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para reproducir la siguiente canción
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para reproducir la canción anterior
            }
        });

        handler = new Handler();
    }

    private void playMediaPlayer() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            updateSeekBar();
            playPauseButton.setBackgroundResource(R.drawable.ic_pause);
        }
    }

    private void pauseMediaPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }
}*/