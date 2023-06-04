package com.david.melodyxprueba;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.david.melodyxprueba.ArtistasSpotify;
import com.david.melodyxprueba.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;

public class PruebaAPI {

    private Context context;
    private SharedPreferences sharedPreferences;
    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

    public PruebaAPI(Context context, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    public ArrayList<ArtistasSpotify> getArtistas() {

        try {
            //String url = "https://mocki.io/v1/1bcc32a5-91c6-455a-9c25-32333715ad96";
            String url = "https://mocki.io/v1/d59e1107-3f95-480a-b3a4-cb59eac2912a";
            String artistInfo = HttpUtils.get(url);

            //Name
            JSONObject jsonArtistInfo = new JSONObject(artistInfo);
            JSONObject resultArtistInfo = jsonArtistInfo.getJSONObject("artistInfo");
            String name = resultArtistInfo.getString("name");

            //Followers
            JSONObject jsonArtistStats = new JSONObject(artistInfo);
            JSONObject resultArtistStats = jsonArtistStats.getJSONObject("stats");
            Integer followers = resultArtistStats.getInt("followers");

            //Top Tracks
            JSONObject jsonTrack = new JSONObject(artistInfo);
            JSONArray topTracksArray = jsonTrack.getJSONArray("topTracks");

            //Visuals
            JSONObject jsonVisuals = new JSONObject(artistInfo);
            JSONObject resultVisuals = jsonVisuals.getJSONObject("visuals");
            JSONArray avatarArray = resultVisuals.getJSONArray("avatar");
            JSONObject avatarObject = avatarArray.getJSONObject(0);
            String avatarUrl = avatarObject.getString("url");

            ArrayList<ArtistasSpotify> datosArtistas = new ArrayList<>();

            for(int i = 0; i < topTracksArray.length(); i++) {
                JSONObject trackJson = topTracksArray.getJSONObject(i);

                //Imagen Album
                JSONObject albumObject = trackJson.getJSONObject("album");
                JSONArray coverArray = albumObject.getJSONArray("cover");
                JSONObject coverObject = coverArray.getJSONObject(1);
                String coverUrl = coverObject.getString("url");
                JSONObject coverObject1 = coverArray.getJSONObject(2);
                String coverUrl1 = coverObject1.getString("url");

                //PlayCount
                int trackPlayCount = trackJson.getInt("playCount");
                String formattedPlayCount = numberFormat.format(trackPlayCount);


                ArtistasSpotify datosTracks = new ArtistasSpotify();

                datosTracks.setArtistName(name);
                datosTracks.setTrack(trackJson.getString("trackUrl"));
                datosTracks.setTrackName(trackJson.getString("name"));
                datosTracks.setArtistAvatarImage(avatarUrl);
                datosTracks.setArtistFollowers(followers);
                datosTracks.setTrackImage(coverUrl);
                datosTracks.setAlbumImage(coverUrl1);
                datosTracks.setTrackPlayCount(formattedPlayCount);

                datosArtistas.add(datosTracks);

            }

            Log.e("Datos:", datosArtistas.toString());
            return datosArtistas;


        } catch(IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;


    }

    public void saveArtistas(ArrayList<ArtistasSpotify> artistas) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(artistas);
        editor.putString("artistas", json);
        editor.apply();
    }

    public ArrayList<ArtistasSpotify> getArtistasFromCache() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("artistas", null);
        Type type = new TypeToken<ArrayList<ArtistasSpotify>>() {}.getType();
        return gson.fromJson(json, type);
    }


}