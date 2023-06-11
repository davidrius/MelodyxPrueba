package com.david.melodyxprueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Splash Screen

        try {
            Thread.sleep(2000);

            setTheme(R.style.Theme_MelodyxPrueba);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        final Fragment fragmentArtistaPrueba = new FragmentArtistaPrueba();

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragmentArtistaPrueba).commit();
        }
    }
}