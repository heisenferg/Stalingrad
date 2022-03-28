package com.example.stalingrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class CargaJuego extends AppCompatActivity {

    private Juego j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        j = new Juego(this);
        setContentView(j);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();
    }

    private void hideSystemUI() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            //A partir de kitkat
            j.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            //cuando se presiona volumen, por ej, se cambia la visibilidad, hay que volver
            //a ocultar
            j.setOnSystemUiVisibilityChangeListener(new
                                                            View.OnSystemUiVisibilityChangeListener() {
                                                                @Override
                                                                public void onSystemUiVisibilityChange(int visibility) {
                                                                    hideSystemUI();
                                                                }
                                                            });
        }
    }


}