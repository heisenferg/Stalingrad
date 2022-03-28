package com.example.stalingrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button start;
    CargaJuego carga;
    private Juego j;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        j = new Juego(this);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();
        start = findViewById(R.id.buttonStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(j);
            }
        });
    }


    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
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