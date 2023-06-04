package com.david.melodyxprueba;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ArtistasSpotify implements Parcelable {

    public ArtistasSpotify() {
        // Constructor sin argumentos
    }

    private String artistName;
    private String artistPopularity;
    private String musicGenere;
    private String prizeMoney;
    private Integer followers;
    private String track;
    private ArrayList<String> trackUrl;
    private String trackName;
    private String artistAvatarImage;
    private String trackImage;
    private String albumImage;
    private String trackPlayCount;


    public String getArtistName() {
        return artistName;
    }
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistPopularity() {
        return artistPopularity;
    }
    public void setArtistPopularity(String artistPopularity) {
        this.artistPopularity = artistPopularity;
    }

    public String getMusicGenere() {
        return musicGenere;
    }
    public void setMusicGenere(String musicGenere) {
        this.musicGenere = musicGenere;
    }

    public Integer getArtistFollowers() {
        return followers;
    }
    public void setArtistFollowers(Integer followers) {
        this.followers = followers;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistAvatarImage() {
        return artistAvatarImage;
    }

    public void setArtistAvatarImage(String artistAvatarImage) {
        this.artistAvatarImage = artistAvatarImage;
    }

    public String getTrackImage() {
        return trackImage;
    }

    public void setTrackImage(String trackImage) {
        this.trackImage = trackImage;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public String getTrackPlayCount() {
        return trackPlayCount;
    }

    public void setTrackPlayCount(String trackPlayCount) {
        this.trackPlayCount = trackPlayCount;
    }

    public ArrayList<String> getTrackUrl() {
        return trackUrl;
    }
    public void setTrackUrl(ArrayList<String> track) {
        this.trackUrl = trackUrl;
    }

    public String getPrizeMoney() {
        return prizeMoney;
    }
    public void setPrizeMoney(String prizeMoney) {
        this.prizeMoney = prizeMoney;
    }


    protected ArtistasSpotify(Parcel in) {
        // Lee los valores del Parcel y asigna a las variables de instancia
        artistName = in.readString();
        artistPopularity = in.readString();
        musicGenere = in.readString();
        prizeMoney = in.readString();
        followers = in.readInt();
        track = in.readString();
        trackUrl = in.createStringArrayList();
        trackName = in.readString();
        artistAvatarImage = in.readString();
        trackImage = in.readString();
        albumImage = in.readString();
        trackPlayCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Escribe los valores de las variables de instancia en el Parcel
        dest.writeString(artistName);
        dest.writeString(artistPopularity);
        dest.writeString(musicGenere);
        dest.writeString(prizeMoney);
        dest.writeInt(followers);
        dest.writeString(track);
        dest.writeStringList(trackUrl);
        dest.writeString(trackName);
        dest.writeString(artistAvatarImage);
        dest.writeString(trackImage);
        dest.writeString(albumImage);
        dest.writeString(trackPlayCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArtistasSpotify> CREATOR = new Creator<ArtistasSpotify>() {
        @Override
        public ArtistasSpotify createFromParcel(Parcel in) {
            return new ArtistasSpotify(in);
        }

        @Override
        public ArtistasSpotify[] newArray(int size) {
            return new ArtistasSpotify[size];
        }
    };


    public String toString() {
        return
                artistName + '\'' +
                        artistPopularity + '\'' +
                        musicGenere + '\''
                ;
    }
}

