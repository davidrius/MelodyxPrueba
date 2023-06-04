package com.david.melodyxprueba;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterPersonalizadoArtistaPrueba extends ArrayAdapter<ArtistasSpotify>{

    private Context mContext;
    private int mResource;
    private ArrayList<ArtistasSpotify> mArtistas;
    private MediaPlayer mediaPlayer;
    private ImageView playPauseButton;
    private ImageView nextButton;
    private ImageView previousButton;
    private TextView textViewStartTime;
    private SeekBar seekBar;
    private Runnable updateSeekBar;
    private Handler mHandler;
    //private FloatingMiniPlayerFragment floatingMiniPlayerFragment;
    //private ReproductorFragment reproductorActivity;


    public AdapterPersonalizadoArtistaPrueba(@NonNull Context context, int resource, @NonNull ArrayList<ArtistasSpotify> artistas) {
        super(context, resource, artistas);
        mContext = context;
        mResource = resource;
        mArtistas = artistas;
        mHandler = new android.os.Handler();
        mediaPlayer = new MediaPlayer();
        //floatingMiniPlayerFragment = new FloatingMiniPlayerFragment();
        //floatingMiniPlayerFragment.setFloatingMiniPlayerClickListener(this);

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
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArtistasSpotify artistasSpotify = getItem(position);

        // Inflar el diseño correspondiente según la posición
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (position == 0) {
                convertView = inflater.inflate(R.layout.artist_overview_image, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.artist_overview_top_tracks_list, parent, false);
            }
        }

        // Unir el código con las vistas del diseño
        if (position == 0) {
            // Código para artist_overview_image.xml
            ImageView ivArtistImage = convertView.findViewById(R.id.artistImage);
            TextView tvArtistName = convertView.findViewById(R.id.artistName);

            tvArtistName.setText(artistasSpotify.getArtistName());
            Glide.with(getContext()).load(artistasSpotify.getArtistAvatarImage()).into(ivArtistImage);

        } else {
            // Código para artist_overview_top_tracks_list.xml
            //TextView tvSeguidores = convertView.findViewById(R.id.txtSeguidores);
            TextView tvTrackName = convertView.findViewById(R.id.txtTrackName);
            ImageView ivTrackImage = convertView.findViewById(R.id.imgTrack);
            TextView tvTrackPlayCount = convertView.findViewById(R.id.txtTrackPlays);

            //tvSeguidores.setText(artistasSpotify.getArtistFollowers().toString());
            tvTrackPlayCount.setText(artistasSpotify.getTrackPlayCount());
            tvTrackName.setText(artistasSpotify.getTrackName());
            //getTrackImage()
            Glide.with(getContext()).load(artistasSpotify.getAlbumImage()).into(ivTrackImage);

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trackUrl = artistasSpotify.getTrack();
                reproducirMp3(trackUrl, artistasSpotify, position);

                /*Intent intent = new Intent(mContext, ReproductorActivity.class);
                intent.putExtra("trackUrl", artistasSpotify.getTrack());
                intent.putExtra("trackName", artistasSpotify.getTrackName());
                intent.putExtra("artistName", artistasSpotify.getArtistName());
                intent.putExtra("albumImage", artistasSpotify.getAlbumImage());
                intent.putExtra("position", position);
                mContext.startActivity(intent);*/
            }
        });

        return convertView;
    }

    private void reproducirMp3(String trackUrl, ArtistasSpotify artistasSpotify,  int position) {
        if (!TextUtils.isEmpty(trackUrl)) {
            if (isConnectedToInternet()) {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                View layout = inflater.inflate(R.layout.reproductor_layout, null);

                TextView tvtrackName2 = layout.findViewById(R.id.trackName);
                tvtrackName2.setText(artistasSpotify.getTrackName());

                TextView tvArtistName2 = layout.findViewById(R.id.artistName);
                tvArtistName2.setText(artistasSpotify.getArtistName());

                TextView textViewEndTime = layout.findViewById(R.id.textViewEndTime);
                textViewStartTime = layout.findViewById(R.id.textViewStartTime);

                ImageView imgAlbum = layout.findViewById(R.id.albumImage);
                Glide.with(getContext()).load(artistasSpotify.getAlbumImage()).into(imgAlbum);


                //ReproductorActivity
                /*Intent intent = new Intent(mContext, ReproductorActivity.class);
                intent.putExtra("trackUrl", artistasSpotify.getTrack());
                intent.putExtra("trackName", artistasSpotify.getTrackName());
                intent.putExtra("artistName", artistasSpotify.getArtistName());
                intent.putExtra("albumImage", artistasSpotify.getAlbumImage());
                intent.putExtra("position", position);
                mContext.startActivity(intent);*/

                // Configura el diseño y muestra la vista en un cuadro de diálogo o en otra parte según tus necesidades
                // Por ejemplo:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.FullScreenDialog);
                builder.setView(layout);
                AlertDialog dialog = builder.create();
                dialog.show();


                ImageView btnClose = layout.findViewById(R.id.btnDown);
                /*// Obtener la referencia al botón de cierre
                ImageView btnClose = layout.findViewById(R.id.btnDown);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        // Mostrar el diálogo del adaptador nuevamente
                        dialog.show();

                        // Abrir el fragmento FloatingMiniPlayerFragment
                        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(android.R.id.content, floatingMiniPlayerFragment)
                                .commit();
                    }
                });*/

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        //Floating Mini Player
                        FloatingMiniPlayerFragment floatingMiniPlayerFragment = new FloatingMiniPlayerFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("trackName", artistasSpotify.getTrackName());
                        bundle.putString("artistName", artistasSpotify.getArtistName());
                        bundle.putString("trackImageUrl", artistasSpotify.getAlbumImage());
                        bundle.putString("trackUrl", artistasSpotify.getTrack());
                        bundle.putInt("pauseIcon", R.drawable.ic_pause_final);
                        bundle.putInt("playIcon", R.drawable.ic_play_final);
                        bundle.putParcelableArrayList("artistas", mArtistas);
                        floatingMiniPlayerFragment.setArguments(bundle);
                        //floatingMiniPlayerFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "floatingMiniPlayer");

                        //ReproductorActivity
                        /*ReproductorFragment reproductorActivity = new ReproductorFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("trackName", artistasSpotify.getTrackName());
                        bundle1.putString("artistName", artistasSpotify.getArtistName());
                        bundle1.putString("trackImageUrl", artistasSpotify.getAlbumImage());
                        bundle1.putString("trackUrl", artistasSpotify.getTrack());*/

                        Log.d("ReproductorActivity", "trackUrl: " + artistasSpotify.getTrack());

                        //reproductorActivity.setArguments(bundle1);

                        // Abrir el fragmento FloatingMiniPlayerFragment
                        ((AppCompatActivity) mContext).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(android.R.id.content, floatingMiniPlayerFragment)
                                .commit();
                    }
                });

                Window dialogWindow = dialog.getWindow(); // Obtener la referencia a la ventana del cuadro de diálogo
                if (dialogWindow != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialogWindow.getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialogWindow.setAttributes(layoutParams);

                    Drawable albumDrawable = imgAlbum.getDrawable();

                    Bitmap albumBitmap = null;
                    if (albumDrawable instanceof BitmapDrawable) {
                        albumBitmap = ((BitmapDrawable) albumDrawable).getBitmap();
                    } else if (albumDrawable instanceof VectorDrawable || albumDrawable instanceof VectorDrawableCompat) {
                        albumBitmap = Bitmap.createBitmap(albumDrawable.getIntrinsicWidth(), albumDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(albumBitmap);
                        albumDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        albumDrawable.draw(canvas);
                    }

                    int defaultColor = Color.BLACK; // Color predeterminado en caso de que no se pueda extraer el color dominante
                    if (albumBitmap != null) {
                        Palette.from(albumBitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int dominantColor = palette.getDominantColor(defaultColor);

                                // Aplicar el color a la barra de estado
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    dialogWindow.setStatusBarColor(dominantColor);
                                }
                            }
                        });
                    }
                }


                //Floating Mini Player
                /*FloatingMiniPlayerFragment floatingMiniPlayerFragment = new FloatingMiniPlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("trackName", artistasSpotify.getTrackName());
                bundle.putString("artistName", artistasSpotify.getArtistName());
                bundle.putString("trackImageUrl", artistasSpotify.getAlbumImage());
                floatingMiniPlayerFragment.setArguments(bundle);
                floatingMiniPlayerFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "floatingMiniPlayer");*/


                playPauseButton = layout.findViewById(R.id.playPauseButton);
                nextButton = layout.findViewById(R.id.nextButton);
                previousButton = layout.findViewById(R.id.previousButton);
                seekBar = layout.findViewById(R.id.seekBar);

                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(trackUrl);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            seekBar.setMax(mp.getDuration());
                            textViewEndTime.setText(formatDuration(mediaPlayer.getDuration() / 1000));
                            playMediaPlayer();

                            //nuevo
                            mHandler.post(updateSeekBar);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
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

                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int nextTrackPosition = position + 1;
                        if (nextTrackPosition < mArtistas.size()) {
                            ArtistasSpotify nextTrack = mArtistas.get(nextTrackPosition);
                            reproducirMp3(nextTrack.getTrack(), nextTrack, nextTrackPosition);
                        } else {
                            ArtistasSpotify firstTrack = mArtistas.get(0);
                            reproducirMp3(firstTrack.getTrack(), firstTrack, 0);
                        }
                    }
                });

                previousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int previousTrackPosition = position - 1;
                        if (previousTrackPosition >= 0) {
                            ArtistasSpotify previousTrack = mArtistas.get(previousTrackPosition);
                            reproducirMp3(previousTrack.getTrack(), previousTrack, previousTrackPosition);
                        } else {
                            ArtistasSpotify lastTrack = mArtistas.get(mArtistas.size() - 1);
                            int lastTrackPosition = mArtistas.size() - 1;
                            reproducirMp3(lastTrack.getTrack(), lastTrack, lastTrackPosition);
                        }
                    }
                });

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
                        textViewEndTime.setText(formatDuration(mediaPlayer.getDuration() / 1000));
                        playPauseButton.setImageResource(R.drawable.ic_play_final);

                        //nuevo
                        mHandler.removeCallbacks(updateSeekBar);

                        //Simula el click y pas a la siguiente canción
                        nextButton.performClick();
                    }
                });

            } else {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View noInternetView = inflater.inflate(R.layout.no_internet_connection, null);
                Toast toast = new Toast(getContext());
                toast.setView(noInternetView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Log.e("Error", "La URL del archivo de música es nula o vacía");
        }
    }

    protected void playMediaPlayer() {
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.ic_pause_final);

            //Obtener el tiempo de la canción actualizado por segundo
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

    private void updateStartTimeTextView(int currentPosition) {
        int minutes = currentPosition / 60000;
        int seconds = (currentPosition % 60000) / 1000;
        String timeString = String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
        textViewStartTime.setText(timeString);
    }

    protected void pauseMediaPlayer() {
        mediaPlayer.pause();
        playPauseButton.setImageResource(R.drawable.ic_play_final);

        //nuevo
        mHandler.removeCallbacks(updateSeekBar);
    }

    private String formatDuration(int durationInSeconds) {
        int minutes = durationInSeconds / 60;
        int seconds = durationInSeconds % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}
